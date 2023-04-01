package com.hazard157.psx24.core.glib.plv;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * Рисовальщик типа {@link EPlvLayoutMode#HOR_LINE}.
 *
 * @author hazard157
 */
class PictureGridCanvasHorLine
    extends AbstractPictureGridCanvas {

  /**
   * Конструктор рисовальщика.
   *
   * @param aParent {@link ScrolledComposite} - родительская панель прокрутки
   */
  public PictureGridCanvasHorLine( ScrolledComposite aParent ) {
    super( aParent );
    ITsPoint cellSize = calcCellSize();
    setSize( SWT.DEFAULT, margins().topMargin() + cellSize.y() + margins().bottomMargin() );
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
      if( cells.get( i ).a().x() >= origin.x ) {
        firstVisibleIndex = i;
        break;
      }
    }
    int lastVisibleIndex = -1;
    for( int i = items.size() - 1; i >= 0; i-- ) {
      if( cells.get( i ).b().x() <= (origin.x + r.width) ) {
        lastVisibleIndex = i;
        break;
      }
    }
    if( aIndex >= firstVisibleIndex && aIndex <= lastVisibleIndex ) {
      return;
    }
    ITsRectangle cellR = cells.get( aIndex );
    if( aIndex == lastVisibleIndex + 1 ) { // частично видимый в конце элемент ставим последин
      parent.setOrigin( cellR.b().x() - r.width, origin.y );
      return;
    }
    if( aIndex == firstVisibleIndex + 1 ) { // частично видимый в начале элемент ставим первым
      parent.setOrigin( cellR.b().x() - 1, origin.y );
      return;
    }
    // всех остальныч ставим в центр
    parent.setOrigin( cellR.a().x() - r.width / 2 + cellR.width() / 2, origin.y );
  }

  @Override
  boolean doRecalcPlacesAndResizeIfNeeded() {
    ITsPoint cellSize = calcCellSize();
    int deltaX = cellSize.x() + margins().horInterval();
    int count = items.size();
    for( int col = 0; col < count; col++ ) {
      int x = margins().leftMargin();
      if( col > 0 ) {
        x += col * deltaX;
      }
      cells.add( new TsRectangle( x, margins().topMargin(), cellSize.x(), cellSize.y() ) );
    }
    int width = margins().leftMargin() + margins().rightMargin() + 1;
    if( count > 0 ) {
      width += count * cellSize.x() + (count - 1) * margins().horInterval();
    }
    Point selfSize = getSize();
    if( width != selfSize.x ) {
      setSize( width, selfSize.y );
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
        int pageLen = calcFittedColCount( getSize().x );
        int index = aIndex + pageLen;
        if( index >= items.size() ) {
          index = 0;
        }
        return index;
      }
      case SWT.PAGE_UP: {
        int pageLen = calcFittedColCount( getSize().x );
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
