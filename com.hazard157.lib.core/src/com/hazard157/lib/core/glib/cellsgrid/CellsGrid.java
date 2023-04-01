package com.hazard157.lib.core.glib.cellsgrid;

import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.ITsRectangle;
import org.toxsoft.core.tslib.bricks.geometry.impl.TsRectangle;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Сетка прямоугольников из ячеек одинакового размера, располагаемые п горизонтали слева-напрвои и потом сверху-вниз.
 * <p>
 * Считается, что тощина границы {@link IGridMargins#borderWidth()} обрамляет ячейку снаружи заданного в
 * {@link #setCellSize(int, int)} размера.
 * <p>
 * При изменении параметров генерирует сообщение {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
 *
 * @author hazard157
 */
public class CellsGrid
    implements ICellsGrid {

  private final GenericChangeEventer genericChangeEventer;

  private final GridMargins margins = new GridMargins();

  // параметры, которые задаются пользователем
  int cellW      = 16;
  int cellH      = 16;
  int canvasW    = 100;
  int cellsCount = 0;

  // вычисляемые параметры
  int colsCount = 0;
  int rowsCount = 0;
  int canvasH   = 100;

  // прямоугольники ячеек
  IListEdit<ITsRectangle> cells = new ElemArrayList<>( getListInitialCapacity( estimateOrder( 300 ) ) );

  /**
   * Конструктор.
   */
  public CellsGrid() {
    genericChangeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация
  //

  private void recalculate() {
    cells.clear();
    // определим размеры ячейки
    int outerW = cellW + 2 * margins.borderWidth();
    int outerH = cellH + 2 * margins.borderWidth();
    // подсчитаем количество столбцов и при заданной ширине
    int effectiveWidth = canvasW - margins.leftMargin() - margins.rightMargin();
    colsCount = effectiveWidth / outerW;
    if( colsCount < 1 ) {
      colsCount = 1;
    }
    // создадим ячейки
    int row = 0;
    int col = 0;
    int x = margins.leftMargin();
    int y = margins.rightMargin();
    int deltaX = outerW + margins.horInterval();
    int deltaY = outerH + margins.verInterval();
    for( int i = 0; i < cellsCount; i++ ) {
      ITsRectangle rect = new TsRectangle( x + margins.borderWidth(), y + margins.borderWidth(), cellW, cellH );
      cells.add( rect );
      x += deltaX;
      if( ++col >= colsCount ) {
        col = 0;
        ++row;
        x = margins.leftMargin();
        y += deltaY;
      }
    }
    // подсчитаем количество строк
    rowsCount = row; // количество полных строк
    if( col != 0 ) { // если в последней строке есть эелемнты, то плюс 1 строка
      ++rowsCount;
    }
    // вычислим высоту
    canvasH = margins.topMargin() + margins.bottomMargin(); // сначала подсичитаем границы сверху/снизу
    if( rowsCount != 0 ) { // если есть строки, то добавим их высоту
      canvasH += rowsCount * outerH; // высота всех ячеек
      canvasH += (rowsCount - 1) * margins.verInterval(); // высота всех промежутков
    }
    genericChangeEventer.fireChangeEvent();
  }

  @Override
  public IGridMargins margins() {
    return margins;
  }

  @Override
  public IList<ITsRectangle> getCells() {
    return cells;
  }

  @Override
  public int getCanvasWidth() {
    return canvasW;
  }

  @Override
  public int getCanvasHeight() {
    return canvasH;
  }

  @Override
  public int getRowsCount() {
    return rowsCount;
  }

  @Override
  public int getColsCount() {
    return colsCount;
  }

  @Override
  public int getCellWidth() {
    return cellW;
  }

  @Override
  public int getCellHeight() {
    return cellH;
  }

  @Override
  public int getCellsCount() {
    return cellsCount;
  }

  @Override
  public int getColsInRow( int aRow ) {
    if( aRow < 0 || aRow >= rowsCount || colsCount == 0 ) {
      return 0;
    }
    if( aRow < rowsCount - 1 ) {
      return colsCount;
    }
    int n = cellsCount - ((rowsCount - 1) * colsCount);
    return n;
  }

  @Override
  public int getRowY( int aRow ) {
    int rowHeight = cellH + margins.verInterval();
    int y = margins.topMargin() + aRow * rowHeight;
    return y;
  }

  @Override
  public int getColX( int aCol ) {
    int colWdth = cellW + margins.horInterval();
    int x = margins.leftMargin() + aCol * colWdth;
    return x;
  }

  @Override
  public int getIndexAtCoors( int aX, int aY ) {
    for( int i = 0; i < cells.size(); i++ ) {
      if( cells.get( i ).contains( aX, aY ) ) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int getIndex( int aCol, int aRow ) {
    if( aRow < 0 || aRow >= rowsCount ) {
      return -1;
    }
    if( aCol < 0 || aCol >= getColsInRow( aRow ) ) {
      return -1;
    }
    return aRow * colsCount + aCol;
  }

  @Override
  public ITsRectangle getCell( int aCol, int aRow ) {
    return cells.get( getIndex( aCol, aRow ) );
  }

  @Override
  public ITsRectangle getCell( int aIndex ) {
    return cells.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // API редактирования
  //

  /**
   * Задает новые значения границ.
   * <p>
   * При изменении значения пересчитывает внутренности и генерирует сообщение
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aMargins {@link IGridMargins} - новые значения границ
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void setMargins( IGridMargins aMargins ) {
    TsNullArgumentRtException.checkNull( aMargins );
    if( !margins.equals( aMargins ) ) {
      margins.copyFrom( aMargins );
      recalculate();
    }
  }

  /**
   * Задает размер ячеек.
   * <p>
   * При изменении значения пересчитывает внутренности и генерирует сообщение
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aWidth int - ширина ячейки в пикселях
   * @param aHeght int - высота ячейки в пикселях
   * @throws TsIllegalArgumentRtException любая размерность < 1
   */
  public void setCellSize( int aWidth, int aHeght ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 1 || aHeght < 1 );
    if( cellW != aWidth || cellH != aHeght ) {
      cellW = aWidth;
      cellH = aHeght;
      recalculate();
    }
  }

  /**
   * Задает ширину канвы в пикселях.
   * <p>
   * При изменении значения пересчитывает внутренности и генерирует сообщение
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aWidth int - ширина канвы в пикселях
   * @throws TsIllegalArgumentRtException аргумент < 0
   */
  public void setCanvasWidth( int aWidth ) {
    TsIllegalArgumentRtException.checkTrue( aWidth < 0 );
    if( canvasW != aWidth ) {
      canvasW = aWidth;
      recalculate();
    }
  }

  /**
   * Задает суммарное количество ячеек.
   * <p>
   * При изменении значения пересчитывает внутренности и генерирует сообщение
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aCellsCount int - нновое кличество ячеек
   * @throws TsIllegalArgumentRtException аргумент < 0
   */
  public void setCellsCount( int aCellsCount ) {
    TsIllegalArgumentRtException.checkTrue( aCellsCount < 0 );
    if( cellsCount != aCellsCount ) {
      cellsCount = aCellsCount;
      recalculate();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns change eventer.
   *
   * @return {@link IGenericChangeEventer} - the eventer
   */
  public IGenericChangeEventer eventer() {
    return genericChangeEventer;
  }

}
