package com.hazard157.psx24.core.glib.plv;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.picview.*;

/**
 * Базовый класс собственно рисовальщиков изображения для {@link PicturesListViewer}.
 *
 * @author hazard157
 */
abstract class AbstractPictureGridCanvas
    extends Canvas
    implements Listener, IThumbSizeable {

  /**
   * Вспомгательный класс для обеспечения анимации {@link TsImage} в {@link AbstractPictureGridCanvas}.
   *
   * @author hazard157
   */
  class GifAnimator
      implements ICloseable {

    private final IImageAnimationCallback drawingCallback = ( aImageAnimator, aIndex, aUserData ) -> {
      int itemIdex = ((Integer)aUserData).intValue();
      // TODO понять, почему индекс бывает неправильным и устранить
      if( itemIdex < items.size() && itemIdex >= 0 ) {
        PlvItem item = items.get( itemIdex );
        item.nextIndex();
        redrawImage( itemIdex );
      }
    };

    private final IAnimationSupport                 an;
    private final IMapEdit<PlvItem, IImageAnimator> map = new ElemMap<>();

    public GifAnimator() {
      an = new AnimationSupport( getDisplay() );
      an.resume();
    }

    void addIfNeeded( PlvItem aItem ) {
      if( aItem.image().isSingleFrame() ) {
        return;
      }
      int index = items.indexOf( aItem );
      IImageAnimator iman = an.registerImage( aItem.image(), drawingCallback, Integer.valueOf( index ) );
      map.put( aItem, iman );
      iman.resume();
    }

    void remove( PlvItem aItem ) {
      if( aItem.image().isSingleFrame() ) {
        return;
      }
      IImageAnimator iman = map.removeByKey( aItem );
      if( iman != null ) {
        an.unregister( iman );
      }
    }

    void clear() {
      for( IImageAnimator iman : map.values() ) {
        an.unregister( iman );
      }
      map.clear();
    }

    @Override
    public void close() {
      clear();
      an.pause();
      an.dispose();
    }

  }

  private static final EThumbSize DEFAULT_THUMB_SIZE = EThumbSize.SZ128;

  /**
   * Слушетль отрисовки.
   */
  private final PaintListener paintListener = this::internalPaint;

  /**
   * Слушатель кнопок щелчков миши.
   */
  private final MouseListener mouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent e ) {
      int index = findItemIndexAt( e.x, e.y );
      if( index < 0 ) {
        return;
      }
      internalSelectItem( index, true );
    }

    @Override
    public void mouseDoubleClick( MouseEvent e ) {
      int index = findItemIndexAt( e.x, e.y );
      if( index < 0 ) {
        return;
      }
      if( index != selectedIndex ) {
        internalSelectItem( index, true );
      }
      fireDoubleClickEvent( index );
    }

  };

  final GifAnimator                           gifAnimator             = new GifAnimator();
  private ITsDoubleClickListener<PlvItem>     doubleClickListener     = null;
  private ITsSelectionChangeListener<PlvItem> selectionChangeListener = null;
  private final PgMargins                     margins                 = new PgMargins();
  protected final ScrolledComposite           parent;
  protected final IListEdit<PlvItem>          items                   = new ElemLinkedBundleList<>();
  protected final IListEdit<ITsRectangle>     cells                   = new ElemLinkedBundleList<>();

  private EThumbSize thumbSize      = DEFAULT_THUMB_SIZE;
  private boolean    isLabels1Shown = true;
  private boolean    isLabels2Shown = false;

  protected int selectedIndex = -1;

  final ToolTip       tooltipControl;
  IPlvTooltipProvider tooltipProvider = null;

  /**
   * Конструктор для наследников.
   *
   * @param aParent {@link ScrolledComposite} - родительская панель прокрутки
   */
  public AbstractPictureGridCanvas( ScrolledComposite aParent ) {
    super( aParent, SWT.NONE /* SWT.NO_BACKGROUND | SWT.NO_MERGE_PAINTS */ );
    parent = aParent;
    addPaintListener( paintListener );
    addMouseListener( mouseListener );
    addListener( SWT.KeyDown, this );
    tooltipControl = new ToolTip( this ) {

      @Override
      protected Composite createToolTipContentArea( Event aEvent, Composite aTpParent ) {
        PlvItem item = findItem( aEvent );
        return tooltipProvider.createToolTipContentArea( item, aEvent, aTpParent );
      }

      @Override
      protected boolean shouldCreateToolTip( Event aEvent ) {
        if( super.shouldCreateToolTip( aEvent ) ) {
          return tooltipProvider != null;
        }
        return false;
      }

      PlvItem findItem( Event aEvent ) {
        for( int i = 0; i < cells.size(); i++ ) {
          ITsRectangle r = cells.get( i );
          if( r.contains( aEvent.x, aEvent.y ) ) {
            return items.get( i );
          }
        }
        return null;
      }

    };

    addDisposeListener( aE -> {
      clearItems();
      gifAnimator.close();
    } );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void fireSelectionEvent() {
    if( selectionChangeListener != null ) {
      PlvItem sel = null;
      if( selectedIndex >= 0 ) {
        sel = items.get( selectedIndex );
      }
      selectionChangeListener.onTsSelectionChanged( this, sel );
    }
  }

  void fireDoubleClickEvent( int aIndex ) {
    if( doubleClickListener != null ) {
      PlvItem item = null;
      if( aIndex >= 0 && aIndex <= items.size() ) {
        item = items.get( aIndex );
      }
      doubleClickListener.onTsDoubleClick( this, item );
    }
  }

  void internalPaint( PaintEvent aE ) {
    for( int i = 0; i < items.size(); i++ ) {
      ITsRectangle cellRect = cells.get( i );
      // нарисуем картинку
      PlvItem item = items.get( i );
      TsImage mi = item.image();
      if( !mi.isDisposed() ) {
        Rectangle imgR = mi.image().getBounds();
        ITsPoint newSz = HzImageUtils.calcSize( imgR.width, imgR.height, thumbSize.size(), thumbSize.size(),
            HzImageUtils.ZOOM_FIT_BEST, false );
        int x = cellRect.x1() + margins.borderWidth() + (thumbSize.size() - newSz.x()) / 2;
        int y = cellRect.y1() + margins.borderWidth() + (thumbSize.size() - newSz.y()) / 2;
        // если надо уменьшить, рисуем с масштабированием
        if( newSz.x() < (imgR.width - 1) || newSz.y() < (imgR.height - 1) ) {
          aE.gc.drawImage( item.getCurrenrImage(), 0, 0, imgR.width, imgR.height, x, y, newSz.x(), newSz.y() );
        }
        else {
          aE.gc.drawImage( item.getCurrenrImage(), x, y );
        }
      }
      // draw labels
      if( isLabels1Shown || isLabels2Shown ) {
        // высота ячейки для одного или двух подписей, которые друг под другом
        int labelCellHight = cellRect.height() - 2 * margins.borderWidth() - thumbSize.size();
        int y = cellRect.y1() + margins.borderWidth() + thumbSize.size();
        if( isLabels1Shown ) {
          String s = item.label1();
          Point p = aE.gc.textExtent( s );
          // координаты центра подписи
          int labelCenterX = cellRect.x1() + cellRect.width() / 2;
          aE.gc.drawText( s, labelCenterX - p.x / 2, y );
          y += labelCellHight / 2;
        }
        if( isLabels2Shown ) {
          String s = item.label2();
          Point p = aE.gc.textExtent( s );
          // координаты центра подписи
          int labelCenterX = cellRect.x1() + cellRect.width() / 2;
          aE.gc.drawText( s, labelCenterX - p.x / 2, y );
        }
      }
      // нарисуем обрамление у текущего элемента
      if( i == selectedIndex ) {
        int selBorderWidth = margins.borderWidth();
        if( selBorderWidth > 2 ) {
          --selBorderWidth;
        }
        for( int j = 0; j < selBorderWidth; j++ ) {
          aE.gc.drawRectangle( cellRect.x1() + j, cellRect.y1() + j, cellRect.width() - 2 * j - 1,
              cellRect.height() - 2 * j - 1 );
        }
      }
    }
  }

  void internalSelectItem( int aIndex, boolean aFireEventIfNeeded ) {
    if( aIndex < -1 && aIndex >= items.size() ) {
      throw new TsIllegalArgumentRtException();
    }
    if( selectedIndex != aIndex ) {
      redrawCell( selectedIndex );
      selectedIndex = aIndex;
      redrawCell( selectedIndex );
      if( aFireEventIfNeeded ) {
        fireSelectionEvent();
      }
    }
    revealItem( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // Canvas
  //

  @Override
  public Point computeSize( int aWHint, int aHHint, boolean aChanged ) {
    // TODO ??? how to compute size?
    Point p = super.computeSize( aWHint, aHHint, aChanged );
    if( p.x < thumbSize.pointSize().x() ) {
      p.x = thumbSize.pointSize().x();
    }
    if( p.y < thumbSize.pointSize().y() ) {
      p.y = thumbSize.pointSize().y();
    }
    return p;
  }

  // ------------------------------------------------------------------------------------
  // Listener
  //

  @Override
  public void handleEvent( Event aEvent ) {
    if( aEvent.type != SWT.KeyDown ) {
      return;
    }
    switch( aEvent.keyCode ) {
      case SWT.ARROW_UP:
      case SWT.ARROW_DOWN:
      case SWT.ARROW_LEFT:
      case SWT.ARROW_RIGHT:
      case SWT.HOME:
      case SWT.END:
      case SWT.PAGE_DOWN:
      case SWT.PAGE_UP:
        internalSelectItem( getIndexShift( selectedIndex(), aEvent.keyCode ), true );
        break;
      default:
        break;
    }
  }

  // ------------------------------------------------------------------------------------
  // Для наследников
  //

  /**
   * Вычисляет размер ячейки исходя из текущих установок.
   *
   * @return {@link ITsPoint} - размер ячейки, включая границу и подпись в пикселях
   */
  protected ITsPoint calcCellSize() {
    int width = 2 * margins.borderWidth() + thumbSize.size();
    int height = 2 * margins.borderWidth() + thumbSize.size();
    if( isLabels1Shown || isLabels2Shown ) {
      GC gc = new GC( this );
      Point p = gc.textExtent( "Wg0" ); //$NON-NLS-1$
      int fontH = p.y;
      if( isLabels1Shown ) {
        height += fontH;
      }
      if( isLabels2Shown ) {
        height += fontH;
      }
    }
    return new TsPoint( width, height );
  }

  protected void recalcPlacesAndResizeIfNeeded() {
    cells.clear();
    if( doRecalcPlacesAndResizeIfNeeded() ) {
      Composite grandma = parent.getParent();
      grandma.layout( true );
    }
  }

  protected int findItemIndexAt( int aX, int aY ) {
    for( int i = 0, n = cells.size(); i < n; i++ ) {
      if( cells.get( i ).contains( aX, aY ) ) {
        return i;
      }
    }
    return -1;
  }

  protected int calcFittedColCount( int aWidth ) {
    ITsPoint cellSize = calcCellSize();
    int w1 = aWidth - margins.leftMargin() - margins.rightMargin();
    if( w1 < cellSize.x() ) {
      return 1;
    }
    for( int i = 1; i < 1000; i++ ) {
      int calculatedWidth = i * cellSize.x() + (i - 1) * margins.horInterval();
      if( calculatedWidth > w1 ) {
        return i - 1;
      }
    }
    throw new TsInternalErrorRtException();
  }

  protected int calcFittedRowCount( int aHeoght ) {
    ITsPoint cellSize = calcCellSize();
    int h1 = aHeoght - margins.topMargin() - margins.borderWidth();
    if( h1 < cellSize.y() ) {
      return 1;
    }
    for( int i = 1; i < 1000; i++ ) {
      int calculatedHeight = i * cellSize.y() + (i - 1) * margins.verInterval();
      if( calculatedHeight > h1 ) {
        return i - 1;
      }
    }
    throw new TsInternalErrorRtException();
  }

  protected void redrawCell( int aIndex ) {
    if( aIndex >= 0 && aIndex < cells.size() ) {
      ITsRectangle r = cells.get( aIndex );
      redraw( r.x1(), r.y1(), r.width(), r.height(), false );
    }
  }

  protected void redrawImage( int aIndex ) {
    if( aIndex >= 0 && aIndex < cells.size() ) {
      ITsRectangle r = cells.get( aIndex );
      int bw = margins.borderWidth();
      int x = r.x1() + bw;
      int y = r.y1() + bw;
      redraw( x, y, x + thumbSize.size(), y + thumbSize.size(), false );
    }
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeable
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public void setThumbSize( EThumbSize aSize ) {
    TsNullArgumentRtException.checkNull( aSize );
    if( thumbSize != aSize ) {
      thumbSize = aSize;
      recalcPlacesAndResizeIfNeeded();
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return DEFAULT_THUMB_SIZE;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Определяет, будет ли показаны подписи 1 к изображениям.
   * <p>
   * По умолчанию - <code>true</code>.
   *
   * @return boolean - признак показа подписей 1
   */
  public boolean isLabels1Shown() {
    return isLabels1Shown;
  }

  /**
   * Определяет, будет ли показаны подписи 2 к изображениям.
   * <p>
   * По умолчанию - <code>false</code>.
   *
   * @return boolean - признак показа подписей 2
   */
  public boolean isLabels2Shown() {
    return isLabels2Shown;
  }

  /**
   * Задает, будет ли показаны подписи к изображениям.
   *
   * @param aShown1 boolean - признак показа подписей 1
   * @param aShown2 boolean - признак показа подписей 2
   */
  public void setLabelsShown( boolean aShown1, boolean aShown2 ) {
    if( isLabels1Shown != aShown1 || isLabels2Shown != aShown2 ) {
      isLabels1Shown = aShown1;
      isLabels2Shown = aShown2;
      recalcPlacesAndResizeIfNeeded();
      redraw();
    }
  }

  /**
   * Возвращает данные о настроках расстоянии и интервалов сетки изрображении.
   *
   * @return {@link PgMargins} - настрокйи размеров и интервалов
   */
  public PgMargins margins() {
    return margins;
  }

  /**
   * Возвращает список отображаемых элементов.
   *
   * @return IList&lt;{@link PlvItem}&gt; - список отображаемых элементов
   */
  public IList<PlvItem> items() {
    return items;
  }

  /**
   * Возвращает индекс выбранного элемента.
   *
   * @return int - индекс выбранного элемента или -1
   */
  public int selectedIndex() {
    return selectedIndex;
  }

  /**
   * Задает выбранный элемент по индексу.
   *
   * @param aIndex int - индекс выбранного элемента или -1
   */
  public void setSelectedIndex( int aIndex ) {
    internalSelectItem( aIndex, false );
  }

  /**
   * Задает все элементы разом, очищая предыдущий список.
   *
   * @param aItems {@link IList}&lt;{@link PlvItem}&gt; - список элементов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setItems( IList<PlvItem> aItems ) {
    // очитска по аналогии с clearItems()
    items.clear();
    gifAnimator.clear();
    selectedIndex = -1;
    // добавление новых
    for( PlvItem item : aItems ) {
      items.add( item );
      gifAnimator.addIfNeeded( item );
    }
    recalcPlacesAndResizeIfNeeded();
    redraw();
  }

  /**
   * Добавляет отображаемый элемент в конец списка.
   *
   * @param aItem {@link PlvItem} - отображаемый элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addItem( PlvItem aItem ) {
    items.add( aItem );
    gifAnimator.addIfNeeded( aItem );
    recalcPlacesAndResizeIfNeeded();
    redraw();
  }

  /**
   * Добавляет отображаемый элемент в указанное место списка.
   *
   * @param aIndex int - индекс, который будет иметь добавленный элемент, в пределах 0..SIZE
   * @param aItem {@link PlvItem} - отображаемый элемент
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   */
  public void insertItem( int aIndex, PlvItem aItem ) {
    items.insert( aIndex, aItem );
    gifAnimator.addIfNeeded( aItem );
    recalcPlacesAndResizeIfNeeded();
    redraw();
  }

  /**
   * Удяляет элемент из списка.
   *
   * @param aIndex int - индекс удаляемого элемента
   * @return {@link PlvItem} - удаленный элемент
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   */
  public PlvItem removeItem( int aIndex ) {
    PlvItem item = items.removeByIndex( aIndex );
    gifAnimator.remove( item );
    if( selectedIndex >= items.size() ) {
      selectedIndex = items.size() - 1;
    }
    recalcPlacesAndResizeIfNeeded();
    redraw();
    return item;
  }

  /**
   * Очищает список отображаемых элементов.
   */
  public void clearItems() {
    gifAnimator.clear();
    items.clear();
    selectedIndex = -1;
    // этот метод вызывается и при удалении виджета
    if( !isDisposed() ) {
      recalcPlacesAndResizeIfNeeded();
      redraw();
    }
  }

  /**
   * Задает обработчик двойного щелчка мыши.
   *
   * @param aListener {@link ITsSelectionChangeListener} - обработчик двойного щелчка мыши, может быть null
   */
  public void setSelectionChangeHandler( ITsSelectionChangeListener<PlvItem> aListener ) {
    selectionChangeListener = aListener;
  }

  /**
   * Задает обработчик двойного щелчка мыши.
   *
   * @param aListener {@link ITsDoubleClickListener} - обработчик двойного щелчка мыши, может быть null
   */
  public void setDoubleCkickHandler( ITsDoubleClickListener<PlvItem> aListener ) {
    doubleClickListener = aListener;
  }

  /**
   * Задает поставщик всплывающей подсказки к элементам.
   * <p>
   * Вне элементов подсказка не появляется.
   * <p>
   * Для отказа от тултипов следует задать <code>null</code>.
   *
   * @param aTooltipProvider {@link IPlvTooltipProvider} - поставщие тултипа или <code>null</code>
   */
  public void setTooltipProvider( IPlvTooltipProvider aTooltipProvider ) {
    tooltipProvider = aTooltipProvider;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Наследник в этом методе должен рассчитать расположение картинок и если нужно, изменить свой размер.
   * <p>
   * Перерасчет происходит исходя и типа раскладки (каждый наследник реализует свой тип раскладки), текущих картинок
   * {@link #items}, установок {@link #margins()}, {@link #thumbSize()}, {@link #isLabels1Shown()} и
   * {@link #isLabels2Shown()}. Расчитанные расположения заносятся в {@link #cells} (пусто в момент вызодв метода), с
   * соответствем индексов со списком {@link #items}.
   *
   * @return boolean - признак, что размер рисовальщика был изменен, и требуется перераскладка родителя
   */
  abstract boolean doRecalcPlacesAndResizeIfNeeded();

  /**
   * Делает запрошенный элемент видимым.
   *
   * @param aIndex int - индекс запрошенного элемента
   */
  abstract void revealItem( int aIndex );

  /**
   * Наследник должен найти, на какой индекс следует перейти с заданного при нажатии указанной кнопки.
   * <p>
   * На взод подаются следующие кнокпи: {@link SWT#ARROW_DOWN},{@link SWT#ARROW_UP},{@link SWT#ARROW_LEFT},
   * {@link SWT#ARROW_RIGHT}, {@link SWT#HOME}, {@link SWT#END}, {@link SWT#PAGE_DOWN} и {@link SWT#PAGE_UP}.
   *
   * @param aIndex int - насальный индекс
   * @param aKeyCode int - SWT код нажатой кноки
   * @return int - индекс, который должен стать выбранным
   */
  abstract int getIndexShift( int aIndex, int aKeyCode );

}
