package com.hazard157.psx24.core.glib.plv;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Просмотрщик списка изображений в виде строки, столбца или таблицы.
 * <p>
 * Просматриваемые изображения с сопутствующей информацией передаются в виде списка (или отдельных) элементов
 * {@link PlvItem}.
 *
 * @author goga
 */
public class PicturesListViewer
    implements ITsDoubleClickEventProducer<PlvItem>, ITsSelectionProvider<PlvItem>, IThumbSizeable {

  private final TsDoubleClickEventHelper<PlvItem>     doubleClickEventHelper;
  private final TsSelectionChangeEventHelper<PlvItem> selectionChangeEventHelper;
  private final EPlvLayoutMode                        imagesLayoutMode;
  private final TsComposite                           board;
  private final ScrolledComposite                     scroller;
  final AbstractPictureGridCanvas                     canvas;

  private boolean isAutoSelectNewItem = true;

  /**
   * Конструктор панели.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aImagesLayoutMode {@link EPlvLayoutMode} - режим расположения значков на панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PicturesListViewer( Composite aParent, EPlvLayoutMode aImagesLayoutMode ) {
    TsNullArgumentRtException.checkNulls( aParent, aImagesLayoutMode );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    imagesLayoutMode = aImagesLayoutMode;
    board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // создание контролей
    switch( imagesLayoutMode ) {
      case HOR_LINE:
        scroller = new ScrolledComposite( board, SWT.BORDER | SWT.H_SCROLL );
        canvas = new PictureGridCanvasHorLine( scroller );
        break;
      case VER_LINE:
        scroller = new ScrolledComposite( board, SWT.BORDER | SWT.V_SCROLL );
        canvas = new PictureGridCanvasVerLine( scroller );
        break;
      case ROWS:
        scroller = new ScrolledComposite( board, SWT.BORDER | SWT.V_SCROLL );
        canvas = new PictureGridCanvasRows( scroller );
        scroller.setMinSize( new Point( canvas.thumbSize().size(), Integer.MAX_VALUE ) );
        scroller.setExpandHorizontal( true );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    scroller.setLayoutData( BorderLayout.CENTER );
    scroller.setContent( canvas );
    canvas.setDoubleCkickHandler( new ITsDoubleClickListener<PlvItem>() {

      @Override
      public void onTsDoubleClick( Object aSource, PlvItem aSelectedItem ) {
        fireDoubleClickEvent( aSelectedItem );
      }
    } );
    canvas.setSelectionChangeHandler( new ITsSelectionChangeListener<PlvItem>() {

      @Override
      public void onTsSelectionChanged( Object aSource, PlvItem aSelectedItem ) {
        fireSelectionChangeEvent( aSelectedItem );
      }
    } );
    scroller.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent aE ) {
        clearItems();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void fireSelectionChangeEvent( PlvItem aSelItem ) {
    selectionChangeEventHelper.fireTsSelectionEvent( aSelItem );
  }

  void fireDoubleClickEvent( PlvItem aSelItem ) {
    doubleClickEventHelper.fireTsDoublcClickEvent( aSelItem );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<PlvItem> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<PlvItem> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionProvider
  //

  @Override
  public PlvItem selectedItem() {
    int selIndex = canvas.selectedIndex();
    if( selIndex >= 0 ) {
      return canvas.items().get( selIndex );
    }
    return null;
  }

  @Override
  public void setSelectedItem( PlvItem aItem ) {
    int index = -1;
    if( aItem != null ) {
      index = canvas.items().indexOf( aItem );
    }
    canvas.setSelectedIndex( index );
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<PlvItem> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<PlvItem> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IThumbSizeable
  //

  /**
   * Возвращает отображаемый размер картинок в пикселях.
   *
   * @return {@link EThumbSize} - отображаемый размер картинок в пикселях
   */
  @Override
  public EThumbSize thumbSize() {
    return canvas.thumbSize();
  }

  /**
   * Изменяет отображаемый размер картинок.
   *
   * @param aThumbSize {@link EThumbSize} - новый размер картинок
   */
  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    canvas.setThumbSize( aThumbSize );
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return canvas.defaultThumbSize();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает режим расположения значков на панели.
   * <p>
   * Этот параметр задается в конструкторе и не может быть изменен.
   *
   * @return {@link EPlvLayoutMode} - режим расположения значков на панели
   */
  public EPlvLayoutMode imagesLayoutMode() {
    return imagesLayoutMode;
  }

  /**
   * Определяет, показываются ли подписи {@link PlvItem#label1()} к значкам.
   *
   * @return boolean - признак показа ли подписей 1 к значкам
   */
  public boolean isLabels1Shown() {
    return canvas.isLabels1Shown();
  }

  /**
   * Определяет, показываются ли подписи {@link PlvItem#label2()} к значкам.
   *
   * @return boolean - признак показа ли подписей 2 к значкам
   */
  public boolean isLabels2Shown() {
    return canvas.isLabels2Shown();
  }

  /**
   * Задает, показываются ли подписи к значкам.
   *
   * @param aShown1 boolean - признак показа ли подписей 1 к значкам
   * @param aShown2 boolean - признак показа ли подписей 2 к значкам
   */
  public void setLabelsShown( boolean aShown1, boolean aShown2 ) {
    canvas.setLabelsShown( aShown1, aShown2 );
  }

  /**
   * Определяет, будут ли новые элементы автоматически становится выделенными.
   *
   * @return boolean - признак, что новые элементы автоматически становится выделенными
   */
  public boolean isAutoSelectNewItem() {
    return isAutoSelectNewItem;
  }

  /**
   * Задает, будут ли новые элементы автоматически становится выделенными.
   *
   * @param aAutoSelect boolean - признак, что новые элементы автоматически становится выделенными
   */
  public void setAutoSelectNewItem( boolean aAutoSelect ) {
    isAutoSelectNewItem = aAutoSelect;
  }

  /**
   * Возвращает список отображаемых элементов.
   *
   * @return {@link IList}&lt;{@link PlvItem}&gt; - список отображаемых элементов
   */
  public IList<PlvItem> items() {
    return canvas.items();
  }

  /**
   * Задает все элементы разом, очищая предыдущий список.
   *
   * @param aItems {@link IList}&lt;{@link PlvItem}&gt; - список элементов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setItems( IList<PlvItem> aItems ) {
    canvas.setItems( aItems );
  }

  /**
   * Добавляет отображаемый элемент в конец списка.
   *
   * @param aItem {@link PlvItem} - отображаемый элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addItem( PlvItem aItem ) {
    canvas.addItem( aItem );
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
    canvas.insertItem( aIndex, aItem );
  }

  /**
   * Удяляет элемент из списка.
   *
   * @param aItem {@link PlvItem} - удаляемый элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void removeItem( PlvItem aItem ) {
    int index = canvas.items().indexOf( aItem );
    if( index >= 0 ) {
      canvas.removeItem( index );
    }
  }

  /**
   * Очищает список отображаемых элементов.
   */
  public void clearItems() {
    canvas.clearItems();
  }

  /**
   * Возвращает индекс выделенного элемента списке {@link #items()}.
   *
   * @return int - индекс выделенного элемента или -1
   */
  public int selectedIndex() {
    return canvas.selectedIndex();
  }

  /**
   * Изменяет выделенный элемент.
   *
   * @param aIndex int - индекс выделяемого элемента (в списке {@link #items()} или -1)
   * @throws TsIllegalArgumentRtException индекс имеет недопустимое значение
   */
  public void setSelectedIndex( int aIndex ) {
    canvas.setSelectedIndex( aIndex );
  }

  /**
   * Делает выбранным первый элемент с указанной пользовательской ссылкой.
   * <p>
   * Если аргумент null, то происходит проверка {@link PlvItem#userData()} на null, иначе используется
   * {@link Object#equals(Object)}.
   * <p>
   * Если элемент не найден, то выделение убирается.
   *
   * @param aUserData Object - пользовательская ссылка
   */
  public void selectByUserData( Object aUserData ) {
    int index = -1;
    for( int i = 0, count = items().size(); i < count; i++ ) {
      PlvItem plv = items().get( i );
      if( aUserData == null ) {
        if( plv.userData() == null ) {
          index = i;
          break;
        }
      }
      else {
        if( plv.userData() != null ) {
          if( plv.userData().equals( aUserData ) ) {
            index = i;
            break;
          }
        }
      }
    }
    setSelectedIndex( index );
  }

  /**
   * Находит элемент по пользовательской ссылке.
   * <p>
   * Если аргумент null, то происходит проверка {@link PlvItem#userData()} на null, иначе используется
   * {@link Object#equals(Object)}.
   * <p>
   * Если элемент не найден, то возвращается null.
   *
   * @param aUserData Object - пользовательская ссылка
   * @return {@link PlvItem} - элемент или null
   */
  public PlvItem findItemByUserData( Object aUserData ) {
    for( int i = 0, count = items().size(); i < count; i++ ) {
      PlvItem plv = items().get( i );
      if( aUserData == null ) {
        if( plv.userData() == null ) {
          return plv;
        }
      }
      else {
        if( plv.userData() != null ) {
          if( plv.userData().equals( aUserData ) ) {
            return plv;
          }
        }
      }
    }
    return null;
  }

  /**
   * Генерирует сообщение {@link ITsSelectionChangeListener#onTsSelectionChanged(Object, Object)}.
   */
  public void fireSelectionEvent() {
    int index = selectedIndex();
    if( index >= 0 ) {
      fireSelectionChangeEvent( items().get( index ) );
    }
    else {
      fireSelectionChangeEvent( null );
    }
  }

  /**
   * Возвращает SWT-контроль, реализующий панель.
   *
   * @return {@link TsComposite} - SWT-контроль, реализующий панель
   */
  public TsComposite getControl() {
    return board;
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
    canvas.setTooltipProvider( aTooltipProvider );
  }

}
