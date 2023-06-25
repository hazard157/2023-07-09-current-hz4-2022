package com.hazard157.lib.core.excl_done.hhmmss;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс виджетов ввода даты/времени, которые справа имеют две кнопки изменения "вверх"/"вниз".
 *
 * @author hazard157
 */
public abstract class AbstractNnnWidget
    extends Composite
    implements IGenericChangeEventCapable {

  private final Listener keyDownListener = aEvent -> {
    switch( aEvent.keyCode ) {
      case SWT.ARROW_UP: {
        doHandleChangeRequest( +1 );
        break;
      }
      case SWT.ARROW_DOWN: {
        doHandleChangeRequest( -1 );
        break;
      }
      case SWT.PAGE_UP: {
        doHandleChangeRequest( +10 );
        break;
      }
      case SWT.PAGE_DOWN: {
        doHandleChangeRequest( -10 );
        break;
      }
      default:
        break;
    }
  };

  private final ISingleSourcing_MouseWheelListener mouseWheelListener = aEvent -> {
    int delta = -1;
    if( aEvent.count > 0 ) { // scroll up
      delta = +1;
    }
    if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
      delta *= 10;
    }
    doHandleChangeRequest( delta );
  };

  /**
   * Слушатель мыши на двух кнопках измнения значения {@link #twoButtonsPane}.
   */
  private final MouseListener twoButtonsMouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent aEvent ) {
      int delta = 1;
      if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
        delta = 10;
      }
      switch( twoButtonsPane.whereIsPoint( aEvent.x, aEvent.y ) ) {
        case OUTSIDE:
          break;
        case UP:
          doHandleChangeRequest( +delta );
          break;
        case DOWN:
          doHandleChangeRequest( -delta );
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  };

  private final Listener verifyListener = this::doHandleVerify;

  /**
   * Раскладка этого контроля.
   *
   * @author hazard157
   */
  protected class SelfLayout
      extends Layout {

    public SelfLayout() {
      // nop
    }

    @Override
    protected Point computeSize( Composite aComposite, int aWHint, int aHHint, boolean aFlushCache ) {
      if( twoButtonsPane == null ) {
        return text.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
      }
      Point textSize = text.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
      Point rightSize = twoButtonsPane.computeSize( SWT.DEFAULT, textSize.y, true );
      // высота контроля определяется выстоной текста
      // ширина контроля - ширина текста + предпочтительная ширина панели кнопок
      return new Point( textSize.x + rightSize.x, textSize.y );
    }

    @Override
    protected void layout( Composite aComposite, boolean aFlushCache ) {
      Point textSize = text.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
      text.setBounds( new Rectangle( 0, 0, textSize.x, textSize.y ) );
      if( twoButtonsPane != null ) {
        Point rightSize = twoButtonsPane.computeSize( SWT.DEFAULT, textSize.y, true );
        twoButtonsPane.setBounds( new Rectangle( textSize.x, 0, rightSize.x, textSize.y ) );
      }
    }

  }

  private final GenericChangeEventer eventer;

  protected final Text     text;          // виджет отображения текста
  final VertTwoButtonsPane twoButtonsPane;

  /**
   * Конструктор для наследников.
   * <p>
   * Кроме используемых родительским {@link Composite} битами стиля aStyle, использует следующие биты:
   * <ul>
   * <li>{@link SWT#READ_ONLY} - не позволяет редактировать значение;</li>
   * </ul>
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aStyle int - стиль контроля, биты <code>SWT.XXX</code>, собранные по ИЛИ
   */
  protected AbstractNnnWidget( Composite aParent, int aStyle ) {
    super( aParent, aStyle );
    eventer = new GenericChangeEventer( this );
    this.setLayout( new SelfLayout() );
    // text
    text = new Text( this, SWT.BORDER );
    text.setEditable( !isReadOnly() );
    // buttonPane
    if( !isReadOnly() ) {
      twoButtonsPane = new VertTwoButtonsPane( this );
      text.addListener( SWT.KeyDown, keyDownListener );
      text.addListener( SWT.Verify, verifyListener );
      twoButtonsPane.addMouseListener( twoButtonsMouseListener );
      TsSinglesourcingUtils.Control_addMouseWheelListener( text, mouseWheelListener );
      TsSinglesourcingUtils.Control_addMouseWheelListener( twoButtonsPane, mouseWheelListener );
    }
    else {
      twoButtonsPane = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducerEx
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //

  protected boolean isReadOnly() {
    return (getStyle() & SWT.READ_ONLY) != 0;
  }

  protected void fireChangeEvent() {
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения
  //

  /**
   * Наследник должен обработать запрос на изменение значения.
   * <p>
   * Обработка запроса (какую компоненту следует изменить) должна зависеть от положения курсора в редактируемом тексте.
   * <p>
   * Запрошенное изменение aDelta принимает одно из следующих значений: -1/+1 - запрос на изменение компоненты на
   * минимальную величину дискретнойти компоненты вниз/вверх, -10/+10 - то, же но изменение на бОльшую величину. Эти
   * значения соответствую поведению спиннера {@link Spinner} при нажатии соответственн клавиш "стрелка вниз" / "стрелка
   * вверх" и "Page Down" / "Page Up" на клавиатуре.
   *
   * @param aDelta int - запрошенное увеличение (-10, -1, +1, +10)
   */
  protected abstract void doHandleChangeRequest( int aDelta );

  /**
   * Наследник дожен проверить корректность всех редактирований текста в виджете {@link #text}.
   * <p>
   * TODO описать использование
   *
   * @param aEvent {@link Event} - событие редактирования
   */
  protected abstract void doHandleVerify( Event aEvent );

}
