package com.hazard157.psx24.core.glib.plv;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * Рисовальщик типа {@link EPlvLayoutMode#HOR_LINE}.
 *
 * @author goga
 */
class PictureGridCanvasVerLine
    extends AbstractPictureGridCanvas {

  /**
   * Конструктор рисовальщика.
   *
   * @param aParent {@link ScrolledComposite} - родительская панель прокрутки
   */
  public PictureGridCanvasVerLine( ScrolledComposite aParent ) {
    super( aParent );
    ITsPoint cellSize = calcCellSize();
    setSize( margins().leftMargin() + cellSize.y() + margins().rightMargin(), SWT.DEFAULT );
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
    int deltaY = cellSize.y() + margins().verInterval();
    int count = items.size();
    for( int row = 0; row < count; row++ ) {
      int y = margins().topMargin();
      if( row > 0 ) {
        y += row * deltaY;
      }
      cells.add( new TsRectangle( margins().leftMargin(), y, cellSize.x(), cellSize.y() ) );
    }
    int height = margins().topMargin() + margins().bottomMargin() + 1;
    if( count > 0 ) {
      height += count * cellSize.y() + (count - 1) * margins().horInterval();
    }
    Point selfSize = getSize();
    if( height != selfSize.y ) {
      setSize( selfSize.x, height );
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
      case SWT.ARROW_DOWN:
      case SWT.ARROW_RIGHT: {
        int index = aIndex + 1;
        if( index >= items.size() ) {
          index = 0;
        }
        return index;
      }
      case SWT.ARROW_UP:
      case SWT.ARROW_LEFT: {
        int index = aIndex - 1;
        if( index < 0 ) {
          index = items.size() - 1;
        }
        return index;
      }
      case SWT.PAGE_DOWN: {
        int pageLen = calcFittedRowCount( getSize().y );
        int index = aIndex + pageLen;
        if( index >= items.size() ) {
          index = 0;
        }
        return index;
      }
      case SWT.PAGE_UP: {
        int pageLen = calcFittedRowCount( getSize().y );
        int index = aIndex - pageLen;
        if( index < 0 ) {
          index = items.size() - 1;
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
