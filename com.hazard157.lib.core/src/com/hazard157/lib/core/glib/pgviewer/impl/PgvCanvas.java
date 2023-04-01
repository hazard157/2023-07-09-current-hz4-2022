package com.hazard157.lib.core.glib.pgviewer.impl;

import static com.hazard157.lib.core.glib.pgviewer.IPicsGridViewerConstants.*;

import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.derivative.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.glib.cellsgrid.*;
import com.hazard157.lib.core.glib.pgviewer.*;

/**
 * Канва в {@link ScrolledComposite}, собственно рисующий сетку миниатюр.
 * <p>
 * Точнее, ристуес строки ячеек, с переносом, в порядке слева-направо, сверху-внизю
 *
 * @author hazard157
 * @param <V> - тип ображаемых сущностей
 */
class PgvCanvas<V>
    extends TsAbstractCanvas
    implements ITsUserInputListener, IThumbSizeableEx {

  /**
   * Элемент, содержащий сущность и его миниатюру для отрисовки.
   *
   * @author hazard157
   */
  class PgvItem
      implements IImageAnimationCallback, ICloseable {

    final V   entity;
    final int cellIndex;

    private IImageAnimator iman         = null;
    private TsImage        mi           = null;
    private int            currentIndex = 0;

    public PgvItem( V aEntity, int aCellIndex ) {
      entity = aEntity;
      cellIndex = aCellIndex;
    }

    void paintItem( GC aGc, ITsRectangle aRect, @SuppressWarnings( "unused" ) boolean aIsSelected ) {
      boolean isLabels = OP_IS_LABELS_SHOWN.getValue( tsContext().params() ).asBool();
      boolean isLabel2 = OP_IS_LABEL2_SHOWN.getValue( tsContext().params() ).asBool();
      // отрисовка изображения
      Image image = getCurrFrame();
      if( image != null ) {
        aGc.drawImage( image, aRect.x1(), aRect.y1() );
      }
      // подписи
      if( isLabels ) {
        int bottomAreaHeight = aRect.height() - aRect.width();
        int l1CenterY, l2CenterY;
        if( isLabel2 ) {
          l1CenterY = aRect.y2() - bottomAreaHeight * 3 / 4;
          l2CenterY = aRect.y2() - bottomAreaHeight * 1 / 4;
        }
        else {
          l1CenterY = aRect.y2() - bottomAreaHeight / 2;
          l2CenterY = 0; // не используется
        }
        // отрисовка label1
        String s = visualsProvider.getLabel1( entity );
        Point p = aGc.textExtent( s );
        int x = aRect.x1() + (aRect.width() - p.x) / 2;
        aGc.drawText( s, x, l1CenterY - p.y / 2 );
        // отрисовка label2
        if( isLabel2 ) {
          s = visualsProvider.getLabel2( entity );
          p = aGc.textExtent( s );
          x = aRect.x1() + (aRect.width() - p.x) / 2;
          aGc.drawText( s, x, l2CenterY - p.y / 2 );
        }
      }
    }

    Image getCurrFrame() {
      if( mi != null && currentIndex >= 0 && currentIndex < mi.count() && !mi.isDisposed() ) {
        return mi.frames().get( currentIndex );
      }
      return null;
    }

    V getEntity() {
      return entity;
    }

    /**
     * Загружает изображения.
     * <p>
     * Метод должен вызываться из основного GUI-потока. Во избещание заморозки GUI, следует организовать
     * последовательный вызов этих методов для последовательности элементов {@link PgvItem} через
     * {@link Display#asyncExec(Runnable)}. Так и сделано в {@link PgvCanvas#loadNextImage()}.
     */
    void loadImage() {
      // загрузка изображения и анимация, если нужно
      mi = visualsProvider.getThumb( entity, thumbSize() );
      if( mi != null ) {
        if( mi.isAnimated() ) {
          iman = animationSupport.registerImage( mi, PgvItem.this, entity );
          iman.resume();
        }
        redrawCell( cellIndex );
      }
    }

    // ------------------------------------------------------------------------------------
    // IImageAnimationCallback
    //

    @Override
    public void onDrawFrame( IImageAnimator aImageAnimator, int aIndex, Object aUserData ) {
      currentIndex = aIndex;
      redrawCell( cellIndex );
    }

    @Override
    public void close() {
      if( iman != null ) {
        animationSupport.unregister( iman );
        iman = null;
      }
    }

  }

  /**
   * Класс всплывающей подсказки.
   *
   * @author hazard157
   */
  class EntityTooltip
      extends ToolTip {

    EntityTooltip( Control control ) {
      super( control );
    }

    @Override
    protected Composite createToolTipContentArea( Event aEvent, Composite aParent ) {
      CLabel label = new CLabel( aParent, SWT.SHADOW_NONE );
      Color bk = colorManager().getColor( ETsColor.WHITE );
      label.setBackground( bk );
      Color fg = colorManager().getColor( ETsColor.BLACK );
      label.setForeground( fg );
      int index = cellsGrid.getIndexAtCoors( aEvent.x, aEvent.y );
      label.setText( visualsProvider.getTooltip( items.get( index ).getEntity() ) );
      return label;
    }

    @Override
    protected boolean shouldCreateToolTip( Event aEvent ) {
      if( super.shouldCreateToolTip( aEvent ) ) {
        return visualsProvider != IPgvVisualsProvider.DEFAULT && cellsGrid.getIndexAtCoors( aEvent.x, aEvent.y ) >= 0;
      }
      return false;
    }

  }

  final IQueue<PgvItem> itemsWithImagesToLoadQueue = new Queue<>();

  final IAnimationSupport animationSupport;
  final ToolTip           tooltipControl;
  final PicsGridViewer<V> owner;

  IPgvVisualsProvider<V> visualsProvider = IPgvVisualsProvider.DEFAULT;

  /**
   * For owner to add/remove llisteners.
   */
  public final TsUserInputEventsBinder userInputEventsBinder;

  // ------------------------------------------------------------------------------------
  // thumbSize
  private final GenericChangeEventer thumbSizeChangeEventer;
  private final EThumbSize           defaultThumbSize;
  private EThumbSize                 thumbSize = EThumbSize.SZ256;

  // ------------------------------------------------------------------------------------
  // cells
  final IListEdit<PgvItem> items     = new ElemArrayList<>( 256 );
  final CellsGrid          cellsGrid = new CellsGrid();

  // ------------------------------------------------------------------------------------
  // selection
  int selectedIndex = -1;

  private boolean selfResize = false;

  /**
   * Конструктор.
   *
   * @param aParent {@link ScrolledComposite} - родительская панель прокрутки
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aOwner {@link PicsGridViewer} - компонента-владелец
   */
  public PgvCanvas( Composite aParent, ITsGuiContext aContext, PicsGridViewer<V> aOwner ) {
    super( aParent, aContext );
    owner = TsNullArgumentRtException.checkNull( aOwner );
    thumbSizeChangeEventer = new GenericChangeEventer( owner );
    userInputEventsBinder = new TsUserInputEventsBinder( aOwner );
    userInputEventsBinder.bindToControl( this, TsUserInputEventsBinder.BIND_ALL_INPUT_EVENTS );
    userInputEventsBinder.addTsUserInputListener( this );
    animationSupport = new AnimationSupport( getDisplay(), 10 );
    animationSupport.resume();
    defaultThumbSize = OP_DEFAULT_THUMB_SIZE.getValue( tsContext().params() ).asValobj();
    thumbSize = defaultThumbSize;
    tooltipControl = new EntityTooltip( this );
    cellsGrid.eventer().addListener( aSource -> adjustCanvasHeight() );
    adjustCellSize();
    adjustCanvasHeight();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  /**
   * Отрисовывает указанную ячейку и вокруг на толщине рамки.
   * <p>
   * Если индекс имеет недопустимое значение, метод ничего не делает.
   *
   * @param aIndex int - индекс ячейки, также индекс элемента в {@link #items}
   */
  void redrawCell( int aIndex ) {
    if( aIndex >= 0 && aIndex < cellsGrid.getCellsCount() ) {
      ITsRectangle r = cellsGrid.getCell( aIndex );
      int bw = cellsGrid.margins().borderWidth();
      redraw( r.x1() - bw - 1, r.y1() - bw - 1, r.width() + 2 * bw + 2, r.height() + 2 * bw + 2, false );
    }
  }

  /**
   * Подстраивает высоту канвы под вычисленную в {@link #cellsGrid} высоту для размещения всех строк сетки.
   * <p>
   * При изменении пользователем размера окна (панели), меняется ширина канвы, при заданном количестве ячеек, следует
   * изменить высоту так, чтобы в канву умещались все ячейки в порядке слева-направо и сверху-вниз.
   *
   * @return boolean - признак того, что размер по высоте был действительно изменен
   */
  boolean adjustCanvasHeight() {
    selfResize = true;
    try {
      int h = cellsGrid.getCanvasHeight();
      Point sz = getSize();
      if( sz.y != h ) {
        setSize( sz.x, h );
        Composite grandma = getParent().getParent();
        grandma.layout( true, true );
        return true;
      }
    }
    finally {
      selfResize = false;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractTsE4Canvas
  //

  @Override
  public void paint( GC aGc, ITsRectangle aPaintBounds ) {
    // цикл рисования по всем ячейкам
    for( int i = 0, count = cellsGrid.getCellsCount(); i < count; i++ ) {
      PgvItem item = items.get( i );
      ITsRectangle r = cellsGrid.getCell( i );
      item.paintItem( aGc, r, i == selectedIndex );
      // отрисовка границы только у выделенной ячейки
      if( i == selectedIndex ) {
        Color oldFg = aGc.getForeground();
        RGB rgbSelBorder = OP_SELECTION_BORDER_COLOR.getValue( tsContext().params() ).asValobj();
        Color selBorderColor = colorManager().getColor( rgbSelBorder );
        aGc.setForeground( selBorderColor );
        for( int b = 1; b < cellsGrid.margins().borderWidth(); b++ ) {
          aGc.drawRectangle( r.x1() - b, r.y1() - b, r.width() + 2 * b - 1, r.height() + 2 * b - 1 );
        }
        aGc.setForeground( oldFg );
      }
    }
  }

  @Override
  protected void doControlResized( ControlEvent aEvent ) {
    if( !selfResize ) {
      Point p = getSize();
      cellsGrid.setCanvasWidth( p.x );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    int index = cellsGrid.getIndexAtCoors( aCoors.x(), aCoors.y() );
    V entity = index >= 0 ? items.get( index ).getEntity() : null;
    owner.fireTsDoubleClickEvent( entity );
    return false;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    int index = cellsGrid.getIndexAtCoors( aCoors.x(), aCoors.y() );
    pgvSetSelectedIndex( index );
    return false;
  }

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    int index = selectedIndex;
    int col = -1, row = -1;
    if( index >= 0 ) {
      col = index % cellsGrid.getColsCount();
      row = index / cellsGrid.getColsCount();
    }
    switch( aCode ) {
      case SWT.ARROW_UP: {
        if( --row >= 0 ) {
          index = cellsGrid.getIndex( col, row );
        }
        break;
      }
      case SWT.ARROW_DOWN: {
        if( ++row < (cellsGrid.getRowsCount() - 1) || col < cellsGrid.getColsInRow( row ) ) {
          index = cellsGrid.getIndex( col, row );
        }
        break;
      }
      case SWT.ARROW_LEFT: {
        if( --index < -1 ) {
          index = cellsGrid.getCellsCount() - 1;
        }
        break;
      }
      case SWT.ARROW_RIGHT: {
        if( ++index >= cellsGrid.getCellsCount() ) {
          index = -1;
        }
        break;
      }
      case SWT.HOME:
        index = 0;
        break;
      case SWT.END:
        index = cellsGrid.getCellsCount() - 1;
        break;
      // TODO перелистывание на страницу
      // case SWT.PAGE_DOWN:
      // break;
      // case SWT.PAGE_UP:
      // break;
      default: {
        return false;
      }
    }
    pgvSetSelectedIndex( index );
    revealItem( index );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  /**
   * Вычисляет высоту прямоугольника для вывода текста.
   * <p>
   * Выстоа вычисляется исходя из высоты текущего шрифта.
   *
   * @return int - высота прямоугольник для вывода текста
   */
  private int getTextAreaHeight() {
    GC gc = new GC( this );
    Point p = gc.textExtent( "ABCDEFGHIJKLMONOPQRSTUVWXYZabcdefghijklmonopqrstuvwxyz" ); //$NON-NLS-1$
    boolean isLabels = OP_IS_LABELS_SHOWN.getValue( tsContext().params() ).asBool();
    boolean isLabel2 = OP_IS_LABEL2_SHOWN.getValue( tsContext().params() ).asBool();
    int h = 0;
    if( isLabels ) {
      h = p.y;
      if( isLabel2 ) {
        h += p.y;
      }
    }
    return h + 4;
  }

  private void initializeItems( IList<V> aEntities ) {
    TsNullArgumentRtException.checkNull( aEntities );
    // прекратим текущие анимации и очистим список registeredAnimators
    itemsWithImagesToLoadQueue.clear();
    while( !items.isEmpty() ) {
      items.removeByIndex( 0 ).close();
    }
    // создаем новые элементы
    for( int i = 0; i < aEntities.size(); i++ ) {
      V e = aEntities.get( i );
      PgvItem item = new PgvItem( e, i );
      items.add( item );
      itemsWithImagesToLoadQueue.putTail( item );
    }
    loadNextImage();
  }

  void loadNextImage() {
    getDisplay().asyncExec( () -> {
      // начнем обработку очередного элемента|, если он есть
      PgvItem next = itemsWithImagesToLoadQueue.getHeadOrNull();
      if( next == null ) {
        return;
      }
      // загрузка изображения и анимация, если нужно
      next.loadImage();
      // загрузка следующего изображения
      loadNextImage();
    } );

  }

  private IList<V> getEntities() {
    IListEdit<V> ll = new ElemArrayList<>( items.size() );
    for( PgvItem i : items ) {
      ll.add( i.getEntity() );
    }
    return ll;
  }

  private void adjustCellSize() {
    int textAreaHeight = getTextAreaHeight();
    cellsGrid.setCellSize( thumbSize.size(), thumbSize.size() + textAreaHeight );
  }

  private int ensureRowInRange( int aRow ) {
    TsInternalErrorRtException.checkTrue( cellsGrid.getRowsCount() == 0 );
    if( aRow < 0 ) {
      return 0;
    }
    if( aRow >= cellsGrid.getRowsCount() ) {
      return cellsGrid.getRowsCount() - 1;
    }
    return aRow;
  }

  void revealItem( int aIndex ) {
    if( items.isEmpty() ) {
      return;
    }
    // найдем строку, которую надо показать (столбцы всегда все видны!)
    int rowToSel = 0;
    if( aIndex >= 0 ) {
      rowToSel = aIndex / cellsGrid.getColsCount();
    }
    // определим, какие строки сейчас видны ПОЛНОСТЬЮ
    ScrolledComposite parent = (ScrolledComposite)getParent();
    Point origin = parent.getOrigin();
    Rectangle viewport = parent.getClientArea();
    int startY = cellsGrid.margins().topMargin();
    int rowH = cellsGrid.getCellHeight() + cellsGrid.margins().verInterval();
    // индекс первой полностью видимой строки
    int visibleRow1 = 0;
    if( origin.y > startY ) {
      visibleRow1 = ensureRowInRange( (origin.y - startY) / rowH + 1 );
    }
    // индекс последней полностью видимой строки
    int visibleRow2 = ensureRowInRange( (origin.y + viewport.height - startY) / rowH - 1 );
    if( visibleRow2 < visibleRow1 ) {
      visibleRow2 = visibleRow1;
    }
    // если нужная строка видна, ничего не делаем
    boolean isAbove = rowToSel < visibleRow1;
    boolean isBelow = rowToSel > visibleRow2;
    if( !isAbove && !isBelow ) {
      return;
    }
    // сделаем так, чтобы нужная строка была видна: в самом верху, если она была скрыта выше виидмой области,
    // в самом низу, если она была скрыта ниже виидмой области
    int newTopRow = visibleRow1; // какая строка должна стать первым видимим
    if( isAbove ) {
      // выделенный элемент будет в первой видимой строке
      newTopRow = ensureRowInRange( rowToSel );
    }
    else {
      if( isBelow ) {
        // выделенный элемент будет в последней видимой строке
        int visibleRowsCount = visibleRow2 - visibleRow1;
        newTopRow = ensureRowInRange( rowToSel - visibleRowsCount );
      }
    }
    if( newTopRow == visibleRow1 ) {
      return;
    }
    // зададим координаты
    int newOriginY;
    if( newTopRow > 0 ) {
      newOriginY = startY + newTopRow * rowH - cellsGrid.margins().verInterval() + 1;
    }
    else {
      newOriginY = startY;
    }
    parent.setOrigin( origin.x, newOriginY );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает индекс выделенной сущности в списке, которы был задан {@link #pgvSetEntities(IList)}.
   *
   * @return int - индекс визуально выеделенного элемента или -1
   */
  public int pgvGetSelectionIndex() {
    return selectedIndex;
  }

  /**
   * Задает выделенную сущность по индексу в списке {@link #pgvSetEntities(IList)}.
   *
   * @param aIndex int - индекс в списке или -1 для снятия выделения
   * @throws TsIllegalArgumentRtException индекс выодит за допустимые пределы
   */
  public void pgvSetSelectedIndex( int aIndex ) {
    if( selectedIndex != aIndex && aIndex >= -1 && aIndex < items.size() ) {
      int oldIndex = selectedIndex;
      selectedIndex = aIndex;
      redrawCell( oldIndex );
      redrawCell( selectedIndex );
      V selEntity = selectedIndex >= 0 ? items.get( selectedIndex ).getEntity() : null;
      revealItem( selectedIndex );
      owner.fireTsSelectionEvent( selEntity );
    }
  }

  /**
   * Задает сущности для отображения их
   *
   * @param aEntities {@link IList}&lt;V&gt; - список сущностей
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void pgvSetEntities( IList<V> aEntities ) {
    initializeItems( aEntities );
    cellsGrid.setCellsCount( items.size() );
    selectedIndex = -1;
    redraw();
  }

  public IGridMargins pgvGetMargins() {
    return cellsGrid.margins();
  }

  public void pgvSetMargins( IGridMargins aMrgins ) {
    cellsGrid.setMargins( aMrgins );
  }

  public IPgvVisualsProvider<V> pgvGetVisualsProvider() {
    return visualsProvider;
  }

  public void pgvSetVisualsProvider( IPgvVisualsProvider<V> aProvider ) {
    TsNullArgumentRtException.checkNull( aProvider );
    if( !visualsProvider.equals( aProvider ) ) {
      visualsProvider = aProvider;
      initializeItems( getEntities() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize != aThumbSize ) {
      thumbSize = aThumbSize;
      initializeItems( getEntities() );
      adjustCellSize();
      thumbSizeChangeEventer.fireChangeEvent();
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return defaultThumbSize;
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeChangeEventer;
  }

}
