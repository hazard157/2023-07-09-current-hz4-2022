package com.hazard157.lib.core.excl_plan.plv;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * Рисовальщик типа {@link EPlvLayoutMode#ROWS}.
 *
 * @author hazard157
 */
class PictureGridCanvasRows
    extends AbstractPictureGridCanvas {

  private final ControlListener resizeListener = new ControlAdapter() {

    @Override
    public void controlResized( ControlEvent e ) {
      if( selfResizing ) {
        return;
      }
      recalcPlacesAndResizeIfNeeded();
    }
  };

  boolean selfResizing = false;

  /**
   * Конструктор рисовальщика.
   *
   * @param aParent {@link ScrolledComposite} - родительская панель прокрутки
   */
  public PictureGridCanvasRows( ScrolledComposite aParent ) {
    super( aParent );
    addControlListener( resizeListener );
    setSize( thumbSize().size(), SWT.DEFAULT );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractPictureGridCanvas
  //

  @Override
  void revealItem( int aIndex ) {
    if( aIndex == -1 ) {
      return;
    }
    Point origin = parent.getOrigin();
    Rectangle r = parent.getClientArea();
    int firstVisibleIndex = -1;
    for( int i = 0, n = items.size(); i < n; i++ ) {
      if( cells.get( i ).a().y() >= origin.y ) {
        firstVisibleIndex = i;
        break;
      }
    }
    int lastVisibleIndex = -1;
    for( int i = items.size() - 1; i >= 0; i-- ) {
      if( cells.get( i ).b().y() <= (origin.y + r.height) ) {
        lastVisibleIndex = i;
        break;
      }
    }
    if( aIndex >= firstVisibleIndex && aIndex <= lastVisibleIndex ) {
      return;
    }
    ITsRectangle cellR = cells.get( aIndex );
    if( aIndex == lastVisibleIndex + 1 ) { // частично видимый в конце элемент ставим последин
      parent.setOrigin( origin.x, cellR.b().y() - r.height );
      return;
    }
    if( aIndex == firstVisibleIndex + 1 ) { // частично видимый в начале элемент ставим первым
      parent.setOrigin( origin.x, cellR.b().y() - 1 );
      return;
    }
    // всех остальныч ставим в центр
    parent.setOrigin( origin.x, cellR.a().y() - r.height / 2 + cellR.height() / 2 );
  }

  @Override
  boolean doRecalcPlacesAndResizeIfNeeded() {
    ITsPoint cellSize = calcCellSize();
    Point selfSize = getSize();
    int fittedColCount = calcFittedColCount( selfSize.x );
    int deltaX = cellSize.x() + margins().horInterval();
    int deltaY = cellSize.y() + margins().verInterval();
    for( int i = 0, n = items.size(); i < n; i++ ) {
      int col = i % fittedColCount;
      int row = i / fittedColCount;
      int x = margins().leftMargin();
      if( col > 0 ) {
        x += col * deltaX;
      }
      int y = margins().topMargin();
      if( row > 0 ) {
        y += row * deltaY;
      }
      cells.add( new TsRectangle( x, y, cellSize.x(), cellSize.y() ) );
    }
    int rowsCount = items.size() / fittedColCount + 1;
    // увеличим высоту, если надо
    int height = margins().topMargin() + rowsCount * margins().verInterval() + rowsCount * cellSize.y()
        + margins().bottomMargin();
    if( selfSize.y != height ) {
      selfResizing = true;
      setSize( selfSize.x, height );
      selfResizing = false;
      return true;
    }
    return false;
  }

  @Override
  int getIndexShift( int aIndex, int aKeyCode ) {
    if( items.isEmpty() ) {
      return -1;
    }
    switch( aKeyCode ) {
      case SWT.ARROW_RIGHT: {
        int index = aIndex + 1;
        if( index >= items.size() ) {
          index = items.size() - 1;
        }
        return index;
      }
      case SWT.ARROW_LEFT: {
        int index = aIndex - 1;
        if( index < 0 ) {
          index = 0;
        }
        return index;
      }
      case SWT.ARROW_DOWN: {
        int pageLen = calcFittedColCount( getSize().x );
        if( pageLen == 0 ) {
          pageLen = 1;
        }
        int index = aIndex + pageLen;
        if( index >= items.size() ) {
          index = items.size() - 1;
        }
        return index;
      }
      case SWT.ARROW_UP: {
        int pageLen = calcFittedColCount( getSize().x );
        if( pageLen == 0 ) {
          pageLen = 1;
        }
        int index = aIndex - pageLen;
        if( index < 0 ) {
          index = 0;
        }
        return index;
      }
      case SWT.PAGE_DOWN: {
        int cols = calcFittedColCount( getSize().x );
        int rows = calcFittedRowCount( getSize().y );
        int pageLen = cols * rows;
        if( pageLen == 0 ) {
          pageLen = 1;
        }
        int index = aIndex + pageLen;
        if( index >= items.size() ) {
          index = items.size() - 1;
        }
        return index;
      }
      case SWT.PAGE_UP: {
        int cols = calcFittedColCount( getSize().x );
        int rows = calcFittedRowCount( getSize().y );
        int pageLen = cols * rows;
        if( pageLen == 0 ) {
          pageLen = 1;
        }
        int index = aIndex - pageLen;
        if( index < 0 ) {
          index = 0;
        }
        return index;
      }
      case SWT.HOME:
        return 0;
      case SWT.END:
        return items.size() - 1;
      default:
        return aIndex;
    }
  }

}
