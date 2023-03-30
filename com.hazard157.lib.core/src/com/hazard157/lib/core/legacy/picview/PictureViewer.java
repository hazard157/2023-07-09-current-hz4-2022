package com.hazard157.lib.core.legacy.picview;

import static com.hazard157.lib.core.legacy.picview.ITsResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Просмотрщик изображения.
 * <p>
 * Просмотрщик обладает следующими возможностьями:
 * <ul>
 * <li>изменение масштаба отображения картинки, см. методы {@link #setZoom(int)}, {@link #setExpandToFit(boolean)};</li>
 * <li>оборажаемое изображения задается разными способами, см. методы setImage();</li>
 * <li>автоматически обрамляет большую картинку полосами прокрутки;</li>
 * <li>при создании конструктором {@link PictureViewer#PictureViewer(Composite,ITsGuiContext, int, IAnimationSupport)},
 * поддерживает отображение анимированных изображений;</li>
 * <li>имеет опциональный виджет подписи, который создается при вызове {@link #setLabelText(String)}.</li>
 * </ul>
 * <p>
 * Просмотрщик сам не является композитом, а создает в конструеторе контроль {@link #getControl()} для отображения
 * картинки.
 *
 * @author goga
 */
public class PictureViewer
    implements Picture.IMouseEventShooter, ITsGuiContextable {

  // FIXME ограничить перемещение изображения за пределы видимой области

  private static final int   MAX_ZOOM        = 10 * 100;
  private static final int   MIN_ZOOM        = 10;
  private static final float ZOOM_IN_FACTOR  = (float)1.25;
  private static final float ZOOM_OUT_FACTOR = (float)1.25;

  private static final String AID_EXPAND_TO_FIT = "PictureViewer.ExpandToFit"; //$NON-NLS-1$
  private static final String AID_CLEAR_IMAGE   = "PictureViewer.ClearImage";  //$NON-NLS-1$

  /**
   * Описание действия: режим растягивания.
   */
  private static final ITsActionDef AI_EXPAND_TO_FIT = TsActionDef.ofCheck2( AID_EXPAND_TO_FIT, //
      STR_ACT_TEXT_EXPAND_TO_FIT, STR_ACT_TOOLTIP_EXPAND_TO_FIT, ICONID_VIEW_FULLSCREEN );

  /**
   * Описание действия: очистить изображение.
   */
  private static final ITsActionDef AI_CLEAR_IMAGE = TsActionDef.ofPush2( AID_CLEAR_IMAGE, //
      STR_ACT_TEXT_CLEAR_IMAGE, STR_ACT_TOOLTIP_CLEAR_IMAGE, ICONID_EDIT_CLEAR );

  /**
   * Кнопки по умочанию на панели инструментов посмотра изображения.
   */
  private final ITsActionDef[] defaultActions =
      { ACDEF_ZOOM_IN, ACDEF_ZOOM_OUT, ACDEF_ZOOM_ORIGINAL, ACDEF_SEPARATOR, AI_EXPAND_TO_FIT, ACDEF_ZOOM_FIT_BEST,
          ACDEF_ZOOM_FIT_WIDTH, ACDEF_ZOOM_FIT_HEIGHT, ACDEF_SEPARATOR, AI_CLEAR_IMAGE };

  /**
   * Кнопки этого экземпляра просмотрщика на панели инструментов посмотра изображения.
   */
  private final IListEdit<ITsActionDef> currentActions = new ElemArrayList<>();

  /**
   * Обработчик пользовательских дейстий.
   */
  private IPictureViewerActionsHandler actionsHandler = IPictureViewerActionsHandler.NONE;

  /**
   * Уничтожает ресурсы при уничтожении отображающего контроля.
   */
  private final DisposeListener disposeListener = e -> releaseResources();

  private final ITsActionHandler toolbarListener = this::onToolBarButtonPressed;

  /**
   * Метод перерисовки очередного кадра анимированного изображения.
   */
  private final IImageAnimationCallback animatorCallback = new IImageAnimationCallback() {

    @Override
    public void onDrawFrame( IImageAnimator aImageAnimator, int aIndex, Object aUserData ) {
      viewerControl.setImage( aImageAnimator.multiImage().frames().get( aIndex ) );
    }

  };

  private final MouseListener childWidgetMouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent e ) {
      fireMouseClickEvent( false, e.stateMask, e.x, e.y );
    }

    @Override
    public void mouseDoubleClick( MouseEvent e ) {
      fireMouseClickEvent( true, e.stateMask, e.x, e.y );
    }
  };

  private final KeyListener keyboardListener = new KeyAdapter() {

    @Override
    public void keyPressed( KeyEvent aEvent ) {
      processKeyPress( aEvent );
    }

  };

  /**
   * Список слушателей изменения параметров просмотра.
   */
  private final IListEdit<IPictureViewerChangeListener> changeListeners = new ElemArrayList<>();

  /**
   * Список слушателей изменения событий мыши.
   */
  private final IListEdit<IPictureViewerMouseListener> mouseListeners = new ElemArrayList<>();

  /**
   * Панель инструментов (при наличии) или null.
   */
  TsToolbar toolBar;

  /**
   * При использовании панели инструметов подложка (или null).
   */
  final TsComposite board;

  /**
   * Визуальная компонента, которая отображает картинку.
   */
  final Picture viewerControl;

  /**
   * Подпись под картинкой.
   * <p>
   */
  CLabel label = null;

  /**
   * Поддержка анимированных изображений, если значение null - то анимированные изображения не поддерживаются.
   */
  private final IAnimationSupport animationSupport;

  /**
   * Зарегистрированный аниматор изображения, null означает, что нет анимированного изображения.
   * <p>
   * Хранит загруженное анимированное изображение при наличи поодержки анимации ({@link #animationSupport} != null), при
   * отсутствии поддержки анимации, загруженное многокадровое изображение хранится в {@link #multiImage}.
   * <p>
   * В зависимости от того, что отображает просмотрщих (один {@link Image} или анимированное {@link TsImage}),
   * одновременно может быть не-null только одна из трех ссылок:
   * <ul>
   * <li>{@link #imageAnimator} - многокадровое изображене при наличии анимации;</li>
   * <li>{@link #multiImage} - многокадровое изображене при отсутствии анимации;</li>
   * <li>{@link #image} - единичное изображение.</li>
   * </ul>
   * Вне зависимости от того, какой из них не-null, применяется признак {@link #needDispose}.
   */
  private IImageAnimator imageAnimator = null;

  /**
   * Многокадровое изображение, используемое при отсутствии поддержки анимации.
   * <p>
   * При наличи поодержки анимации ({@link #animationSupport} != null), загруженное многокадровое изображение хранится в
   * {@link #imageAnimator}.
   * <p>
   * В зависимости от того, что отображает просмотрщих (один {@link Image} или анимированное {@link TsImage}),
   * одновременно может быть не-null только одна из трех ссылок:
   * <ul>
   * <li>{@link #imageAnimator} - многокадровое изображене при наличии анимации;</li>
   * <li>{@link #multiImage} - многокадровое изображене при отсутствии анимации;</li>
   * <li>{@link #image} - единичное изображение.</li>
   * </ul>
   * Вне зависимости от того, какой из них не-null, применяется признак {@link #needDispose}.
   */
  private TsImage multiImage = null;

  /**
   * Отображаемое изображение, или null.
   * <p>
   * В зависимости от того, что отображает просмотрщих (один {@link Image} или анимированное {@link TsImage}),
   * одновременно может быть не-null только одна из трех ссылок:
   * <ul>
   * <li>{@link #imageAnimator} - многокадровое изображене при наличии анимации;</li>
   * <li>{@link #multiImage} - многокадровое изображене при отсутствии анимации;</li>
   * <li>{@link #image} - единичное изображение.</li>
   * </ul>
   * Вне зависимости от того, какой из них не-null, применяется признак {@link #needDispose}.
   */
  Image image = null;

  /**
   * Признак того, что ресурсы {@link #image} должны быть освобождены из этого объекта.
   * <p>
   * Если признак равен false, то ответственность за освобождение ресурсов несет тот, кто задал изображение.
   * <p>
   * В зависимости от того, что отображает просмотрщих (один {@link Image} или анимированное {@link TsImage}),
   * одновременно мощет быть не-null только одна из ссылок {@link #imageAnimator} или {@link #image}. Вне зависимости от
   * того, какой из них не-null, применяется признак {@link #needDispose}.
   */
  boolean needDispose = true;

  private final ITsGuiContext tsContext;

  /**
   * Создает контроль для просмотра картинки с возможностью анимации.
   * <p>
   * К контролю-содержимому просмотрщика при создании в добавок к стилям aStyle всегда устанавливаются стили
   * {@link SWT#H_SCROLL} и {@link SWT#V_SCROLL}.
   * <p>
   * Контроль понимает стиль {@link SWT#CENTER} - в этом случае маленькое изображение будет рисоваться посреди контроля
   * (а без этого стиля - в левом верхнем углу).
   *
   * @param aParent {@link Composite} - родительский контроль
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStyle int - флаги настроек просмотрщика
   * @param aToolBarName String - название в панели инструментов или null для отсутствия панели инструментов
   * @param aAnimationSupport {@link IAnimationSupport} - средство поддержки анимации или null
   * @param aActs {@link ITsActionDef} - дополнительные действия на панели инструментов
   * @throws TsNullArgumentRtException любой аргумент = null
   * @see SWT#H_SCROLL
   * @see SWT#V_SCROLL
   */
  public PictureViewer( Composite aParent, ITsGuiContext aContext, int aStyle, String aToolBarName,
      IAnimationSupport aAnimationSupport, IList<ITsActionDef> aActs ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    viewerControl = new Picture( board, aStyle | SWT.H_SCROLL | SWT.V_SCROLL, this );
    viewerControl.setLayoutData( BorderLayout.CENTER );
    currentActions.addAll( defaultActions );
    if( aActs != null ) {
      currentActions.addAll( aActs );
    }
    if( aToolBarName == null ) {
      toolBar = null;
    }
    else {
      toolBar = TsToolbar.create( board, tsContext(), aToolBarName, EIconSize.IS_16X16,
          currentActions.toArray( new ITsActionDef[0] ) );
      toolBar.getControl().setLayoutData( BorderLayout.NORTH );
      toolBar.addListener( toolbarListener );
    }
    animationSupport = aAnimationSupport;
    viewerControl.addDisposeListener( disposeListener );
    updateToolbarButtons();
    viewerControl.addMouseListener( childWidgetMouseListener );
    // board.addKeyListener( keyboardListener );
    // viewerControl.addKeyListener( keyboardListener );
    viewerControl.imageControl.addKeyListener( keyboardListener );
    // viewerControl.imageControl.addDragL
  }

  /**
   * Создает контроль для просмотра картинки с возможностью анимации.
   * <p>
   * К контролю-содержимому просмотрщика при создании в добавок к стилям aStyle всегда устанавливаются стили
   * {@link SWT#H_SCROLL} и {@link SWT#V_SCROLL}.
   * <p>
   * Контроль понимает стиль {@link SWT#CENTER} - в этом случае маленькое изображение будет рисоваться посреди контроля
   * (а без этого стиля - в левом верхнем углу).
   *
   * @param aParent {@link Composite} - родительский контроль
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStyle int - флаги настроек просмотрщика
   * @param aToolBarName String - название в панели инструментов или null для отсутствия панели инструментов
   * @param aAnimationSupport {@link IAnimationSupport} - средство поддержки анимации или null
   * @throws TsNullArgumentRtException любой аргумент = null
   * @see SWT#H_SCROLL
   * @see SWT#V_SCROLL
   */
  public PictureViewer( Composite aParent, ITsGuiContext aContext, int aStyle, String aToolBarName,
      IAnimationSupport aAnimationSupport ) {
    this( aParent, aContext, aStyle, aToolBarName, aAnimationSupport, null );
  }

  /**
   * Создает контроль для просмотра картинки с возможностью анимации, без панели инструментов.
   * <p>
   * К контролю-содержимому просмотрщика при создании в добавок к стилям aStyle всегда устанавливаются стили
   * {@link SWT#H_SCROLL} и {@link SWT#V_SCROLL}.
   * <p>
   * Контроль понимает стиль {@link SWT#CENTER} - в этом случае маленькое изображение будет рисоваться посреди контроля
   * (а без этого стиля - в левом верхнем углу).
   *
   * @param aParent {@link Composite} - родительский контроль
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStyle int - флаги настроек просмотрщика
   * @param aAnimationSupport {@link IAnimationSupport} - средство поддержки анимации или null
   * @see SWT#H_SCROLL
   * @see SWT#V_SCROLL
   */
  public PictureViewer( Composite aParent, ITsGuiContext aContext, int aStyle, IAnimationSupport aAnimationSupport ) {
    this( aParent, aContext, aStyle, null, aAnimationSupport );
  }

  /**
   * Создает контроль для просмотра картинки без возможности анимации, без панели инструментов.
   * <p>
   * К контролю-содержимому просмотрщика при создании в добавок к стилям aStyle всегда устанавливаются стили
   * {@link SWT#H_SCROLL} и {@link SWT#V_SCROLL}.
   * <p>
   * Контроль понимает стиль {@link SWT#CENTER} - в этом случае маленькое изображение будет рисоваться посреди контроля
   * (а без этого стиля - в левом верхнем углу).
   *
   * @param aParent {@link Composite} - родительский контроль
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStyle int - флаги настроек просмотрщика
   * @throws TsNullArgumentRtException любой аргумент = null
   * @see SWT#H_SCROLL
   * @see SWT#V_SCROLL
   * @see SWT#CENTER
   */
  public PictureViewer( Composite aParent, ITsGuiContext aContext, int aStyle ) {
    this( aParent, aContext, aStyle, null, null );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Останавливает анимацию и если нужно, освобождает ресурсы.
   */
  void releaseResources() {
    if( imageAnimator != null ) {
      animationSupport.unregister( imageAnimator );
      if( needDispose ) {
        imageAnimator.multiImage().dispose();
      }
      imageAnimator = null;
    }
    if( multiImage != null ) {
      if( needDispose ) {
        multiImage.dispose();
      }
      multiImage = null;
    }
    if( image != null ) {
      if( needDispose ) {
        image.dispose();
      }
      image = null;
    }
  }

  /**
   * Реакция на нажатие кнопок панели инструментов.
   *
   * @param aItemId int - идентификатор действия нажатой кнопки
   */
  void onToolBarButtonPressed( String aItemId ) {
    int zoom = getZoom();
    if( zoom < 0 ) { // вычислим реальный масштаб в режиме адаптивного масштабирования
      zoom = (int)viewerControl.getRealZoom();
    }
    switch( aItemId ) {
      case ACTID_ZOOM_FIT_BEST:
        setZoom( HzImageUtils.ZOOM_FIT_BEST );
        break;
      case ACTID_ZOOM_FIT_HEIGHT:
        setZoom( HzImageUtils.ZOOM_FIT_HEIGHT );
        break;
      case ACTID_ZOOM_FIT_WIDTH:
        setZoom( HzImageUtils.ZOOM_FIT_WIDTH );
        break;
      case ACTID_ZOOM_ORIGINAL:
        setZoom( HzImageUtils.ZOOM_ORIGINAL );
        break;
      case ACTID_ZOOM_IN:
        zoom = (int)(zoom * ZOOM_IN_FACTOR);
        if( zoom > MAX_ZOOM ) {
          zoom = MAX_ZOOM;
        }
        setZoom( zoom );
        break;
      case ACTID_ZOOM_OUT:
        zoom = (int)(zoom / ZOOM_OUT_FACTOR);
        if( zoom < MIN_ZOOM ) {
          zoom = MIN_ZOOM;
        }
        setZoom( zoom );
        break;
      case AID_EXPAND_TO_FIT:
        if( toolBar != null ) {
          setExpandToFit( toolBar.getAction( AID_EXPAND_TO_FIT ).isChecked() );
        }
        break;
      case AID_CLEAR_IMAGE:
        clearImage();
        break;
      default:
        actionsHandler.handleAction( this, aItemId );
        break;
    }
    updateToolbarButtons();
  }

  void processKeyPress( KeyEvent aEvent ) {
    ETsCollMove hor = ETsCollMove.NONE;
    ETsCollMove ver = ETsCollMove.NONE;
    // boolean isCtrl = (aEvent.stateMask & SWT.CTRL) != 0;
    boolean isShift = (aEvent.stateMask & SWT.SHIFT) != 0;
    // boolean isAlt = (aEvent.stateMask & SWT.ALT) != 0;
    switch( aEvent.keyCode ) {
      case SWT.ARROW_UP:
        if( isShift ) {
          ver = ETsCollMove.PREV;
        }
        else {
          ver = ETsCollMove.JUMP_PREV;
        }
        break;
      case SWT.ARROW_DOWN:
        if( isShift ) {
          ver = ETsCollMove.NEXT;
        }
        else {
          ver = ETsCollMove.JUMP_NEXT;
        }
        break;
      case SWT.ARROW_LEFT:
        if( isShift ) {
          hor = ETsCollMove.PREV;
        }
        else {
          hor = ETsCollMove.JUMP_PREV;
        }
        break;
      case SWT.ARROW_RIGHT:
        if( isShift ) {
          hor = ETsCollMove.NEXT;
        }
        else {
          hor = ETsCollMove.JUMP_NEXT;
        }
        break;
      case SWT.PAGE_UP:
        ver = ETsCollMove.FIRST;
        break;
      case SWT.PAGE_DOWN:
        ver = ETsCollMove.LAST;
        break;
      case SWT.HOME:
        hor = ETsCollMove.FIRST;
        break;
      case SWT.END:
        hor = ETsCollMove.LAST;
        break;
      case 'c':
      case 'C':
        hor = ETsCollMove.MIDDLE;
        ver = ETsCollMove.MIDDLE;
        break;
      default:
        break;
    }
    displacePicture( hor, ver );
  }

  void fireImageChangeEvent() {
    if( changeListeners.isEmpty() ) {
      return;
    }
    IList<IPictureViewerChangeListener> ll = new ElemArrayList<>( changeListeners );
    for( IPictureViewerChangeListener l : ll ) {
      try {
        l.onPictureViewerImageChanged( this );
      }
      catch( Exception e ) {
        LoggerUtils.errorLogger().error( e );
      }
    }
  }

  void fireParamChangeEvent() {
    updateToolbarButtons();
    if( changeListeners.isEmpty() ) {
      return;
    }
    IList<IPictureViewerChangeListener> ll = new ElemArrayList<>( changeListeners );
    for( IPictureViewerChangeListener l : ll ) {
      try {
        l.onPictureViewerParamChanged( this );
      }
      catch( Exception e ) {
        LoggerUtils.errorLogger().error( e );
      }
    }
  }

  @Override
  public void fireMouseClickEvent( boolean aIsDoubleClick, int aStateMask, int aX, int aY ) {
    if( mouseListeners.isEmpty() ) {
      return;
    }
    IList<IPictureViewerMouseListener> ll = new ElemArrayList<>( mouseListeners );
    for( IPictureViewerMouseListener l : ll ) {
      try {
        l.onPictureViewerMouseClick( this, aIsDoubleClick, aStateMask, aX, aY );
      }
      catch( Exception e ) {
        LoggerUtils.errorLogger().error( e );
      }
    }
  }

  @Override
  public void fireMouseScrollEvent( int aScrollLines, int aStateMask ) {
    // FIXME отменяем прокрутку, вместо нео ввли перетаскивание мышью
    // if( getZoom() != TsImageUtils.ZOOM_FIT_BEST && getZoom() != TsImageUtils.ZOOM_FIT_HEIGHT ) {
    // return;
    // }
    if( mouseListeners.isEmpty() ) {
      return;
    }
    IList<IPictureViewerMouseListener> ll = new ElemArrayList<>( mouseListeners );
    for( IPictureViewerMouseListener l : ll ) {
      try {
        l.onPictureViewerMouseWheel( this, aScrollLines, aStateMask );
      }
      catch( Exception e ) {
        LoggerUtils.errorLogger().error( e );
      }
    }
  }

  void updateToolbarButtons() {
    if( toolBar != null ) {
      toolBar.setActionEnabled( ACTID_ZOOM_ORIGINAL, getZoom() != HzImageUtils.ZOOM_ORIGINAL );
      toolBar.setActionChecked( ACTID_ZOOM_ORIGINAL, getZoom() == HzImageUtils.ZOOM_ORIGINAL );
      toolBar.setActionEnabled( ACTID_ZOOM_FIT_BEST, getZoom() != HzImageUtils.ZOOM_FIT_BEST );
      toolBar.setActionChecked( ACTID_ZOOM_FIT_BEST, getZoom() == HzImageUtils.ZOOM_FIT_BEST );
      toolBar.setActionEnabled( ACTID_ZOOM_FIT_HEIGHT, getZoom() != HzImageUtils.ZOOM_FIT_HEIGHT );
      toolBar.setActionChecked( ACTID_ZOOM_FIT_HEIGHT, getZoom() == HzImageUtils.ZOOM_FIT_HEIGHT );
      toolBar.setActionEnabled( ACTID_ZOOM_FIT_WIDTH, getZoom() != HzImageUtils.ZOOM_FIT_WIDTH );
      toolBar.setActionChecked( ACTID_ZOOM_FIT_WIDTH, getZoom() == HzImageUtils.ZOOM_FIT_WIDTH );
      boolean isFitMode = getZoom() < 0;
      toolBar.setActionEnabled( ACTID_ZOOM_IN, getZoom() < MAX_ZOOM || isFitMode );
      toolBar.setActionEnabled( ACTID_ZOOM_OUT, getZoom() > MIN_ZOOM || isFitMode );
      toolBar.setActionChecked( AID_EXPAND_TO_FIT, getExpandToFit() );
      actionsHandler.updateActionsState( this, toolBar );
    }
  }

  /**
   * Метод удваивает все вхождения значка амперсанд, во избежание преобразования в знак подчеркивания.
   *
   * @param aText String - исходный текст
   * @return String - текст с удвоенными амперсандами
   */
  private static String correctAmpersand( String aText ) {
    if( aText.indexOf( '&' ) < 0 ) {
      return aText;
    }
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < aText.length(); i++ ) {
      char ch = aText.charAt( i );
      sb.append( ch );
      if( ch == '&' ) {
        sb.append( ch );
      }
    }
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает визуальный контроль, рисующий картинку.
   * <p>
   * Может использоваться например для задания режима расположения методом {@link Control#setLayoutData(Object)} или
   * установки минимального размера {@link TsComposite#setMinimumSize(Point)}.
   *
   * @return {@link TsComposite} - визуальный контроль
   */
  public TsComposite getControl() {
    return board;
  }

  /**
   * Возвращает отображаемое мульти-изображение.
   * <p>
   * Если отображается обычное изображение, или вовсе нет отбражаемого изображения, возвращает null.
   *
   * @return {@link TsImage} - отображаемое мульти-изображение или null
   */
  public TsImage getMultiImage() {
    if( imageAnimator != null ) {
      return imageAnimator.multiImage();
    }
    return multiImage;
  }

  /**
   * Возвращает отображаемое изображение.
   * <p>
   * Если отображается мульти-изображение, или вовсе нет отбражаемого изображения, возвращает null.
   *
   * @return {@link Image} - отображаемое изображение или null
   */
  public Image getImage() {
    return image;
  }

  /**
   * Задает масштаб изображения по правилам {@link Picture#setZoom(int)}.
   *
   * @param aZoom int - масштаб изображения
   * @return int - предыдущее значение масштаба изображения
   * @throws TsIllegalArgumentRtException aZoom = 0, или выходит за допустимые пределы
   */
  public int setZoom( int aZoom ) {
    if( aZoom != viewerControl.getZoom() ) {
      int z = viewerControl.setZoom( aZoom );
      fireParamChangeEvent();
      return z;
    }
    return aZoom;
  }

  /**
   * Возвращает значение текущего масштаба отображения изображения.
   *
   * @return int - текущее масштабирование
   */
  public int getZoom() {
    return viewerControl.getZoom();
  }

  /**
   * Задает признак увеличения изображения в режимах адаптивного масштабирования по правилам
   * {@link Picture#setExpandToFit(boolean)}.
   *
   * @param aExpand boolean - признак увеличения маленького изображения в режимах адаптивного масштабирования
   */
  public void setExpandToFit( boolean aExpand ) {
    if( aExpand != viewerControl.getExpandToFit() ) {
      viewerControl.setExpandToFit( aExpand );
      fireParamChangeEvent();
    }
  }

  /**
   * Возвращает признак увеличения изображения в режимах адаптивного масштабирования.
   *
   * @return boolean - признак увеличения маленького изображения в режимах адаптивного масштабирования
   * @see #setExpandToFit(boolean)
   */
  public boolean getExpandToFit() {
    return viewerControl.getExpandToFit();
  }

  /**
   * Задает рисуемое изображение по имени файла картинки.
   * <p>
   * Этот контроль становиться владельцем (ответственным за уничтожение) загруженного изображения.
   *
   * @param aFilePath String - путь к файлу изображения
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIoRtException указанный файл не может быть прочитан
   */
  public void setImageFile( String aFilePath ) {
    TsNullArgumentRtException.checkNull( aFilePath );
    setImageFile( new File( aFilePath ) );
  }

  /**
   * Задает рисуемое изображение по ссылке на файл.
   * <p>
   * Этот контроль становиться владельцем (ответственным за уничтожение) загруженного изображения.
   *
   * @param aFile File - файл изображения
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIoRtException указанный файл не может быть прочитан
   */
  public void setImageFile( File aFile ) {
    TsFileUtils.checkFileReadable( aFile );
    TsImage img = org.toxsoft.core.tsgui.graphics.image.TsImageUtils.loadTsImage( aFile, viewerControl.getDisplay() );
    setMultiImage( img, true );
  }

  /**
   * Задает анимированное изображение, не становясь ответственным за освобождение ресурсов изображения.
   * <p>
   * Если аргумент равен null, то очищает изображение (как метод {@link #clearImage()}).
   *
   * @param aTsImage {@link TsImage} - анимированное изображение или null
   */
  public void setMultiImage( TsImage aTsImage ) {
    setMultiImage( aTsImage, false );
  }

  /**
   * Задает рисуемое изображение, не становясь ответственным за освобождение ресурсов изображения.
   * <p>
   * Если аргумент равен null, то очищает изображение (как метод {@link #clearImage()}).
   * <p>
   * Данный контроль <b>не</b> становится владельцем изображения, и ответственность за освобождение ресурсов изображения
   * остается за пользователем класса. Вызов этого метода эквивалентно вызову {@link #setImage(Image, boolean)
   * setImage(img,false)}.
   *
   * @param aImage {@link Image} - рисуемое изображение или null
   */
  public void setImage( Image aImage ) {
    setImage( aImage, false );
  }

  /**
   * Задает анимированное изображение с указанием отвественного за освобождение ресурсов изображения.
   * <p>
   * Если аргумент равен null, то очищает изображение (как метод {@link #clearImage()}).
   *
   * @param aTsImage {@link TsImage} - анимированное изображение или null
   * @param aDisposeOwner boolean - признак того, что ресурсы картинки будут освобождаться контролем <br>
   *          <b>true</b> - при уничтожении контроля или смене изображения будут также освобождены ресурсы картины (то
   *          есть, будет вызван {@link TsImage#dispose()};<br>
   *          <b>false</b> - ответственность за освобождение ресурсов картинки остается за вызвашим.
   */
  public void setMultiImage( TsImage aTsImage, boolean aDisposeOwner ) {
    releaseResources();
    if( aTsImage != null ) {
      if( animationSupport == null || aTsImage.isSingleFrame() ) { // не анимируем или единичное изображение
        multiImage = aTsImage;
        needDispose = aDisposeOwner;
        viewerControl.setImage( aTsImage.image() );
      }
      else { // анимированное изображение
        imageAnimator = animationSupport.registerImage( aTsImage, animatorCallback, null );
        needDispose = aDisposeOwner;
        imageAnimator.resume();
      }
    }
    else {
      viewerControl.setImage( null );
    }
    fireImageChangeEvent();
    updateToolbarButtons();
  }

  /**
   * Задает рисуемое изображение с указанием отвественного за освобождение ресурсов изображения.
   * <p>
   * Если аргумент aImage равен null, то очищает изображение (как метод {@link #clearImage()}).
   *
   * @param aImage {@link Image} - рисуемое изображение или null
   * @param aDisposeOwner boolean - признак того, что ресурсы картинки будут освобождаться контролем <br>
   *          <b>true</b> - при уничтожении контроля или смене изображения будут также освобождены ресурсы картины (то
   *          есть, будет вызван {@link Image#dispose()};<br>
   *          <b>false</b> - ответственность за освобождение ресурсов картинки остается за вызвашим.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setImage( Image aImage, boolean aDisposeOwner ) {
    releaseResources();
    image = aImage;
    viewerControl.setImage( aImage );
    needDispose = aDisposeOwner;
    fireImageChangeEvent();
  }

  /**
   * Очищает отображаемое изображение.
   */
  public void clearImage() {
    releaseResources();
    viewerControl.setImage( null );
    fireImageChangeEvent();
  }

  /**
   * Возвращает расположение панели инструментов.
   *
   * @return {@link EBorderLayoutPlacement} - расположение панели инструментов или null если нет панели
   */
  public EBorderLayoutPlacement getToolbarPlacement() {
    if( toolBar == null ) {
      return null;
    }
    return (EBorderLayoutPlacement)toolBar.getControl().getLayoutData();
  }

  /**
   * Задает расположение панели инструментов.
   * <p>
   * Only {@link EBorderLayoutPlacement#NORTH} and {@link EBorderLayoutPlacement#SOUTH} are allowed, all others are
   * considered as NORT.
   *
   * @param aPlacement {@link EBorderLayoutPlacement} - расположение панели инструментов
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException aPlacement == {@link EBorderLayoutPlacement#CENTER}
   */
  public void setToolbarPlacement( EBorderLayoutPlacement aPlacement ) {
    TsNullArgumentRtException.checkNull( aPlacement );
    TsIllegalArgumentRtException.checkTrue( aPlacement == EBorderLayoutPlacement.CENTER );
    if( toolBar == null ) {
      return;
    }
    EBorderLayoutPlacement oldPlacement = getToolbarPlacement();
    EBorderLayoutPlacement newPlacement = switch( aPlacement ) {
      case SOUTH -> EBorderLayoutPlacement.SOUTH;
      case CENTER -> EBorderLayoutPlacement.NORTH;
      case EAST -> EBorderLayoutPlacement.NORTH;
      case NORTH -> EBorderLayoutPlacement.NORTH;
      case WEST -> EBorderLayoutPlacement.NORTH;
      default -> EBorderLayoutPlacement.NORTH;
    };
    if( oldPlacement != newPlacement ) {
      toolBar.getControl().setLayoutData( newPlacement );
    }
  }

  /**
   * Задает текст подписи к картинке.
   * <p>
   * Если виджета подписи не было, то он создается. Для уничтожения виджета подписи следует передать аргумент null.
   *
   * @param aText String - текст подписи или null
   */
  public void setLabelText( String aText ) {
    if( aText == null ) {
      if( label != null ) {
        label.removeMouseListener( childWidgetMouseListener );
        label.dispose();
        label = null;
        board.layout();
      }
      return;
    }
    String text = correctAmpersand( aText );
    if( label == null ) {
      label = new CLabel( board, SWT.CENTER );
      label.setLayoutData( BorderLayout.SOUTH );
      label.addMouseListener( childWidgetMouseListener );
    }
    label.setText( text );
    label.setToolTipText( text );
    board.layout();
  }

  /**
   * Возвращает виджет подписи для тонкой настроки цвета, шрифта и др.
   * <p>
   * Если виджет подписи не существует (не был вызван {@link #setLabelText(String)}, то возвращает null.
   *
   * @return {@link CLabel} - виджет подписи или null
   */
  public CLabel getLabel() {
    return label;
  }

  /**
   * Фокусирует на изрображении.
   *
   * @return boolean - аналогично {@link Control#setFocus()}
   */
  public boolean setFocuOnImage() {
    if( viewerControl != null && !viewerControl.isDisposed() ) {
      return viewerControl.imageControl.setFocus();
    }
    return false;
  }

  /**
   * Перемещает изображение (если оно перемещаемо).
   *
   * @param aHor {@link ETsCollMove} - на сколько переместить по горизонтали
   * @param aVer {@link ETsCollMove} - на сколько переместить по вертикали
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void displacePicture( ETsCollMove aHor, ETsCollMove aVer ) {
    if( viewerControl != null && !viewerControl.isDisposed() ) {
      viewerControl.displacePicture( aHor, aVer );
    }
  }

  /**
   * Добавляет слушатель изменении параметров просмотра.
   *
   * @param aListener {@link IPictureViewerChangeListener} - слушатель изменении параметров просмотра
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addChangeListener( IPictureViewerChangeListener aListener ) {
    if( !changeListeners.hasElem( aListener ) ) {
      changeListeners.add( aListener );
    }
  }

  /**
   * Удаляет слушатель изменении параметров просмотра.
   *
   * @param aListener {@link IPictureViewerChangeListener} - слушатель изменении параметров просмотра
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void removeChangeListener( IPictureViewerChangeListener aListener ) {
    changeListeners.remove( aListener );
  }

  /**
   * Добавляет слушатель изменении щелчков мыши.
   *
   * @param aListener {@link IPictureViewerMouseListener} - слушатель изменении щелчков мыши
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addMouseListener( IPictureViewerMouseListener aListener ) {
    if( !mouseListeners.hasElem( aListener ) ) {
      mouseListeners.add( aListener );
    }
  }

  /**
   * Удаляет слушатель щелчков мыши.
   *
   * @param aListener {@link IPictureViewerMouseListener} - слушатель щелчков мыши
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void removeMouseListener( IPictureViewerMouseListener aListener ) {
    mouseListeners.remove( aListener );
  }

  /**
   * Возвращает обработчик пользовательских действий.
   *
   * @return {@link IPictureViewerActionsHandler} - обработчик пользовательских действий, не бывает <code>null</code>
   */
  public IPictureViewerActionsHandler actionsHandler() {
    return actionsHandler;
  }

  /**
   * Задает обработчик пользовательских действий.
   *
   * @param aHandler {@link IPictureViewerActionsHandler} - обработчик пользовательских действий
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public void setActionsHandler( IPictureViewerActionsHandler aHandler ) {
    TsNullArgumentRtException.checkNull( aHandler );
    actionsHandler = aHandler;
  }

}
