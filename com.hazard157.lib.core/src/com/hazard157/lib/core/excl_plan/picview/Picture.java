package com.hazard157.lib.core.excl_plan.picview;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.*;

/**
 * Визуальный контроль с полосками прокрутки, в котором отображается картинка.
 *
 * @author hazard157
 */
class Picture
    extends ScrolledComposite {

  // FIXME ограничить перемещение изображения за пределы видимой области

  interface IMouseEventShooter {

    void fireMouseClickEvent( boolean aIsDoubleClick, int aStateMask, int aX, int aY );

    void fireMouseScrollEvent( int aScrollLines, int aStateMask );

  }

  /**
   * Простой виджет, с заданными размерами, на которой рисуется изображение.
   *
   * @author hazard157
   */
  class ImageWidget
      extends Canvas {

    private final PaintListener paintListener = e -> {
      if( image != null && !image.isDisposed() ) {
        int imgW = image.getBounds().width;
        int imgH = image.getBounds().height;
        int x = 0;
        int y = 0;
        if( isCentered() ) {
          Rectangle bounds = getBounds(); // берем реальные, хотя они должны быть (ctlW,ctlH)
          x = (bounds.width - this.drawW) / 2;
          y = (bounds.height - this.drawH) / 2;
        }
        e.gc.drawImage( image, 0, 0, imgW, imgH, x, y, this.drawW, this.drawH );
      }
    };

    int drawW = 16;
    int drawH = 16;
    int ctlW  = 16;
    int ctlH  = 16;

    ImageWidget( Composite aParent ) {
      super( aParent, SWT.NONE );
      addPaintListener( paintListener );
    }

    // ------------------------------------------------------------------------------------
    // Переопределение методов Control
    //

    @Override
    public Point computeSize( int wHint, int hHint ) {
      return new Point( ctlW, ctlH );
    }

    @Override
    public Point computeSize( int wHint, int hHint, boolean aChanged ) {
      return new Point( ctlW, ctlH );
    }

    // ------------------------------------------------------------------------------------
    // API класса
    //

    /**
     * Задает размер контроля, в который будет "врисована" картина.
     *
     * @param aCtlW int - ширина виджета
     * @param aCtlH int - высота виджета
     * @param aDrawW int - ширина рисования картинки
     * @param aDrawH int - высота рисования картинки
     * @throws TsIllegalArgumentRtException любая размерность отрицательна
     */
    void setImageWidgetSize( int aCtlW, int aCtlH, int aDrawW, int aDrawH ) {
      TsIllegalArgumentRtException.checkTrue( aCtlW < 0 || aCtlH < 0 );
      TsIllegalArgumentRtException.checkTrue( aDrawW <= 0 || aDrawH <= 0 );
      ctlW = aCtlW;
      ctlH = aCtlH;
      drawW = aDrawW;
      drawH = aDrawH;
      setSize( ctlW, ctlH );
    }

  }

  private static final int STEPS_COUNT = 20;
  private static final int JUMPS_COUNT = 4;

  private static final Color FOCUSED_BKG = new Color( ETsColor.BLUE.rgb() );

  final ImageWidget imageControl;

  /**
   * Отображаемое изображение, или null.
   */
  Image image = null;

  /**
   * Признак выравнивания изображения по центру, берется из бита {@link SWT#CENTER} аргумента конструктора
   * {@link Picture#Picture(Composite, int, IMouseEventShooter)}.
   */
  private boolean centered;

  /**
   * Масштаб изображения.
   * <p>
   * Положительные значения в дапазоне 1..{@link HzImageUtils#MAX_ZOOM} означают процент масштаба (например, 200
   * означает двойной размер). Из отрицательных значений допускаются только {@link HzImageUtils#ZOOM_FIT_BEST},
   * {@link HzImageUtils#ZOOM_FIT_HEIGHT} и {@link HzImageUtils#ZOOM_FIT_WIDTH}.
   */
  private int zoomFactor = HzImageUtils.ZOOM_FIT_BEST;

  /**
   * Реальный масштаб изображения, смотрите {@link #getRealZoom()}.
   * <p>
   * Значение определяется во время рисования в {@link #redrawImage()}.
   */
  private double realZoomFactor = 100.0;

  /**
   * Признак увеличения изображения в режимах адаптивного масштабирования.
   * <p>
   * Если признак равен true, то в режимах адаптивного масштабирования {@link HzImageUtils#ZOOM_FIT_BEST},
   * {@link HzImageUtils#ZOOM_FIT_HEIGHT}, {@link HzImageUtils#ZOOM_FIT_WIDTH}, маленькое изображение будет увеличено,
   * чтобы вписаться касаясь границ отображения.
   */
  private boolean expandToFit = false;

  /**
   * Признак, что нажатие левой кнопки мыши должно начать перетаскивание, если <code>false</code> - отдается слушателю.
   */
  boolean mayStartDrag = false;

  /**
   * Признак нахождения в состоянии "перетаксикания".
   */
  boolean drag = false;

  final IMouseEventShooter mouseEventShooter;

  /**
   * Создает контроль для отображения изображения.
   *
   * @param aParent Composite - родительская панель
   * @param aStyle int - SWT стили создаваемого контроля
   * @param aMouseEventShooter {@link IMouseEventShooter} - кому передавать события мыши
   */
  public Picture( Composite aParent, int aStyle, IMouseEventShooter aMouseEventShooter ) {
    super( aParent, aStyle | (SWT.H_SCROLL | SWT.V_SCROLL) );
    mouseEventShooter = TsNullArgumentRtException.checkNull( aMouseEventShooter );
    imageControl = new ImageWidget( this );
    centered = (aStyle & SWT.CENTER) != 0;
    setContent( imageControl );
    addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent e ) {
        redrawImage();
      }

      @Override
      public void controlMoved( ControlEvent e ) {
        redrawImage();
      }
    } );

    imageControl.addFocusListener( new FocusListener() {

      @Override
      public void focusLost( FocusEvent e ) {
        focusChanged();
      }

      @Override
      public void focusGained( FocusEvent e ) {
        focusChanged();
      }
    } );

    TsSinglesourcingUtils.Control_addMouseMoveListener( imageControl, aEvent -> {
      if( drag ) {
        doDrag( aEvent );
      }
    } );

    imageControl.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent e ) {
        // FIXME масштабировать из точки клика мышкой
        if( e.button == 2 ) {
          if( getZoom() == HzImageUtils.ZOOM_ORIGINAL ) {
            setZoom( HzImageUtils.ZOOM_FIT_BEST );
          }
          else {
            setZoom( HzImageUtils.ZOOM_ORIGINAL );
            displacePicture( ETsCollMove.MIDDLE, ETsCollMove.MIDDLE );
          }
        }
        updateMayStartDragState();
        if( e.button == 1 && mayStartDrag && ((e.stateMask & (SWT.CTRL | SWT.SHIFT | SWT.ALT)) == 0) ) {
          startDrag( e );
        }
        else {
          mouseEventShooter.fireMouseClickEvent( false, e.stateMask, e.x, e.y );
        }
        updateMayStartDragState();
      }

      @Override
      public void mouseUp( MouseEvent aEvent ) {
        if( drag ) {
          endDrag();
        }
      }

      @Override
      public void mouseDoubleClick( MouseEvent e ) {
        mouseEventShooter.fireMouseClickEvent( true, e.stateMask, e.x, e.y );
      }

    } );
    TsSinglesourcingUtils.Control_addMouseWheelListener( imageControl, aE -> {
      if( (aE.stateMask & (SWT.CTRL | SWT.SHIFT | SWT.ALT)) == SWT.CTRL ) {
        int newZoom = getZoom();
        if( aE.count > 0 ) {
          newZoom = (int)(getZoom() * Math.sqrt( 2.0 ));
        }
        else {
          if( aE.count < 0 ) {
            newZoom = (int)(getZoom() / Math.sqrt( 2.0 ));
          }
        }
        if( newZoom > HzImageUtils.MAX_ZOOM ) {
          newZoom = HzImageUtils.MAX_ZOOM;
        }
        if( newZoom < 1 ) {
          newZoom = 1;
        }
        if( newZoom != getZoom() ) {
          setZoom( newZoom );
        }
        return;
      }
      mouseEventShooter.fireMouseScrollEvent( aE.count, aE.stateMask );
    } );

  }

  Point mouseStartPoint = new Point( 0, 0 );
  Point dragStartOrigin = new Point( 0, 0 );

  /**
   * Начинает перемещение изображения перетаскиванием захватом мыши.
   *
   * @param aEvent {@link MouseEvent} - событие, вызвавшее захват мыши
   */
  void startDrag( MouseEvent aEvent ) {
    drag = true;
    mouseStartPoint = imageControl.toDisplay( new Point( aEvent.x, aEvent.y ) );
    dragStartOrigin = getOrigin();
    // зачем это извращение знаков =- я не поял, но так работает...
    dragStartOrigin.x = -dragStartOrigin.x;
    dragStartOrigin.y = -dragStartOrigin.y;
    // окончание извращения
    updateMayStartDragState();
  }

  /**
   * Перемещает изображение следуя за захваченной мышью.
   *
   * @param aEvent {@link MouseEvent} - событие перемещения мыши
   */
  void doDrag( MouseEvent aEvent ) {
    // вычислим новые координаты x, y
    Point mouseCurrPoint = imageControl.toDisplay( new Point( aEvent.x, aEvent.y ) );
    int deltaX = mouseCurrPoint.x - mouseStartPoint.x;
    int deltaY = mouseCurrPoint.y - mouseStartPoint.y;
    int x = dragStartOrigin.x + deltaX;
    int y = dragStartOrigin.y + deltaY;
    // FIXME зададим границы перемещения картины
    // if( x > 0 ) {
    // x = 0;
    // }
    // if( y > 0 ) {
    // y = 0;
    // }
    imageControl.setLocation( x, y );
  }

  /**
   * Завершает перемещение изображения перетаскиванием захватом мыши.
   */
  void endDrag() {
    drag = false;
    // нужен ли этот вызов? или был отрисован при последнем doDrag()
    redraw();
  }

  void updateMayStartDragState() {
    if( drag || image == null ) {
      mayStartDrag = false;
      return;
    }
    // начинать перетаскивание можно, когда ScrolledControl имеет ScrollBar-ы
    if( getHorizontalBar() != null || getVerticalBar() != null ) {
      mayStartDrag = true;
    }
  }

  void focusChanged() {
    if( isFocusControl() ) {
      setBackground( FOCUSED_BKG );
    }
    else {
      setBackground( null );
    }
    updateMayStartDragState();
    redraw();
  }

  /**
   * Перерысовывает изображение {@link #image} с текущими установками {@link #getZoom()} и {@link #getExpandToFit()}.
   */
  void redrawImage() {
    if( isDisposed() ) {
      return;
    }
    updateMayStartDragState();
    Rectangle clientArea = getClientArea();
    if( clientArea.width <= 0 || clientArea.height <= 0 ) {
      return;
    }
    if( image != null ) {
      if( image.isDisposed() ) {
        return;
      }
      Rectangle imgBounds = image.getBounds();
      ITsPoint p = HzImageUtils.calcSize( imgBounds.width, imgBounds.height, clientArea.width, clientArea.height,
          zoomFactor, expandToFit );
      int drawW = p.x();
      int drawH = p.y();
      int ctlW = 0, ctlH = 0;
      switch( zoomFactor ) {
        case HzImageUtils.ZOOM_FIT_BEST:
          ctlW = drawW;
          ctlH = drawH;
          break;
        case HzImageUtils.ZOOM_FIT_HEIGHT:
          ctlW = drawW;
          break;
        case HzImageUtils.ZOOM_FIT_WIDTH:
          ctlH = drawH;
          break;
        default:
          ctlW = drawW;
          ctlH = drawH;
          break;
      }
      if( ctlW < clientArea.width ) {
        ctlW = clientArea.width;
      }
      if( ctlH < clientArea.height ) {
        ctlH = clientArea.height;
      }
      imageControl.setImageWidgetSize( ctlW, ctlH, drawW, drawH );
      realZoomFactor = 100.0 * (p.x()) / (imgBounds.width);
    }
    else {
      // imageControl.setImageWidgetSize( 16, 16, 16, 16 );
      imageControl.setImageWidgetSize( clientArea.width, clientArea.height, clientArea.width, clientArea.height );
      if( zoomFactor > 0 ) {
        realZoomFactor = zoomFactor;
      }
      else {
        realZoomFactor = 100.0;
      }
    }
    imageControl.redraw();
  }

  /**
   * Возвращает признак рисования изобращения по центру.
   *
   * @return boolean - признак рисования изобращения по центру
   */
  public boolean isCentered() {
    return centered;
  }

  /**
   * Задает признак рисования изобращения по центру.
   *
   * @param aCentered boolean - признак рисования изобращения по центру
   */
  public void setCentered( boolean aCentered ) {
    if( centered != aCentered ) {
      centered = aCentered;
      redrawImage();
    }
  }

  /**
   * Задает масштаб изображения.
   * <p>
   * Положительные значения в дапазоне 1..{@link HzImageUtils#MAX_ZOOM} означают процент масштаба (например, 200
   * означает двойной размер). Из отрицательных значений допускаются только константы режимов адаптивного
   * масштабирования {@link HzImageUtils#ZOOM_FIT_BEST}, {@link HzImageUtils#ZOOM_FIT_HEIGHT} и
   * {@link HzImageUtils#ZOOM_FIT_WIDTH}.
   * <p>
   * Если задан один из режимов адаптивного масштабирования, то размер изображения меняется вместе с изменением размера
   * контроля. При этом можно включить режим увеличения маленького изображения {@link #setExpandToFit(boolean)}.
   *
   * @param aZoom int - масштаб изображения
   * @return int - предыдущее значение масштаба изображения
   * @throws TsIllegalArgumentRtException недопустимое значение коэффициента масштабирования
   * @see HzImageUtils#ZOOM_ORIGINAL
   * @see HzImageUtils#ZOOM_FIT_BEST
   * @see HzImageUtils#ZOOM_FIT_HEIGHT
   * @see HzImageUtils#ZOOM_FIT_WIDTH
   */
  public int setZoom( int aZoom ) {
    TsIllegalArgumentRtException.checkTrue( !HzImageUtils.isValidZoom( aZoom ) );
    int prevZoom = zoomFactor;
    if( zoomFactor != aZoom ) {
      zoomFactor = aZoom;
      redrawImage();
    }
    return prevZoom;
  }

  /**
   * Возвращает значение текущего масштаба отображения изображения.
   *
   * @return int - текущее масштабирование
   */
  public int getZoom() {
    return zoomFactor;
  }

  /**
   * Возвращает текущий реальный масштаб отображения изображения.
   * <p>
   * Нужно для определения реального масштаба в режимах адаптивного масштабирования. При заданном значении масштаба
   * возвращает близкое к {@link #getZoom()} значение.
   *
   * @return double - текущий реальный масштаб
   */
  public double getRealZoom() {
    return realZoomFactor;
  }

  /**
   * Задает признак увеличения изображения в режимах адаптивного масштабирования.
   * <p>
   * Если признак равен true, то в режимах адаптивного масштабирования ({@link HzImageUtils#ZOOM_FIT_BEST},
   * {@link HzImageUtils#ZOOM_FIT_HEIGHT}, {@link HzImageUtils#ZOOM_FIT_WIDTH}), маленькое изображение будет увеличено,
   * чтобы вписаться касаясь границ отображения.
   *
   * @param aExpand boolean - признак увеличения маленького изображения в режимах адаптивного масштабирования
   */
  public void setExpandToFit( boolean aExpand ) {
    if( expandToFit != aExpand ) {
      expandToFit = aExpand;
      redrawImage();
    }
  }

  /**
   * Возвращает признак увеличения изображения в режимах адаптивного масштабирования.
   *
   * @return boolean - признак увеличения маленького изображения в режимах адаптивного масштабирования
   * @see #setExpandToFit(boolean)
   */
  public boolean getExpandToFit() {
    return expandToFit;
  }

  /**
   * Перемещает изображение (если оно перемещаемо).
   *
   * @param aHor {@link ETsCollMove} - на сколько переместить по горизонтали
   * @param aVer {@link ETsCollMove} - на сколько переместить по вертикали
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void displacePicture( ETsCollMove aHor, ETsCollMove aVer ) {
    TsNullArgumentRtException.checkNulls( aHor, aVer );
    Rectangle bounds = getClientArea();
    Point imgSize = imageControl.getSize();
    Point origin = getOrigin();
    Point newOrigin = new Point( origin.x, origin.y );
    // перемещаем по горизонтали
    if( imgSize.x > bounds.width ) {
      int len = imgSize.x - bounds.width;
      int step = len / STEPS_COUNT + 1;
      int jump = len / JUMPS_COUNT + 1;
      newOrigin.x = PsxUtils.displaceValue( aHor, 0, len, step, jump, origin.x );
    }
    // перемещаем по вуртикали
    if( imgSize.y > bounds.height ) {
      int len = imgSize.y - bounds.height;
      int step = len / STEPS_COUNT + 1;
      int jump = len / JUMPS_COUNT + 1;
      newOrigin.y = PsxUtils.displaceValue( aVer, 0, len, step, jump, origin.y );
    }
    if( !origin.equals( newOrigin ) ) {
      setOrigin( newOrigin );
    }
  }

  /**
   * Задает отображаемое изображение или null для очистки виджета.
   *
   * @param aImage {@link Image} - отображаемое изображение или null
   */
  public void setImage( Image aImage ) {
    image = aImage;
    redrawImage();
  }

  /**
   * Возвращает отображаемое изображения, или null, если ничего не отображается.
   * <p>
   * При отображинии анимированного изображения этот метод возвращает разные изображения по мере смены кадров.
   *
   * @return {@link Image} - отображаемое изображения, или null
   */
  public Image getImage() {
    return image;
  }

}
