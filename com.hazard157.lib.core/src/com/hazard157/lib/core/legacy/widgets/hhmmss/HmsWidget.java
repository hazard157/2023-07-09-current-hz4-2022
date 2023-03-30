package com.hazard157.lib.core.legacy.widgets.hhmmss;

import static org.toxsoft.core.tsgui.utils.HmsUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.legacy.hms.*;

/**
 * Контроль редактирования метки или длительности времени в секундах в виде "ЧЧ:ММ:СС" или "ММ:СС".
 * <p>
 * Контроль может отображать время/интервал в коротком "ММ:СС" и длинном "ЧЧ:ММ:СС" формате (зависит от наличия бита
 * стиля {@link SWT#SHORT}). Кроме того, отличается режим работы с интервалом или моментом времени. В режиме момента
 * времени максимальное значение может быть "59:59:59" ("59:59"), а для интервала первая компонента может принимать
 * значение до 99, то есть "99:59:59" ("99:59"), что позволяет задавть интервалы до 100 часлов (100 минут).
 *
 * @author goga
 */
public class HmsWidget
    extends AbstractNnnWidget {

  private static final String MMSS_TEMPLATE          = "00:00";                 //$NON-NLS-1$
  private static final int    MMSS_TEMPLATE_LENGTH   = MMSS_TEMPLATE.length();
  private static final String HHMMSS_TEMPLATE        = "00:00:00";              //$NON-NLS-1$
  private static final int    HHMMSS_TEMPLATE_LENGTH = HHMMSS_TEMPLATE.length();

  private final IGenericChangeListener hmsValueChangeListener = aSource -> updateOnHmsValueChange();

  final String    template;        // шаблон текста виде "00:00:00" или "00:00"
  final int       templateLength;  // длина шаблона template
  final int       maxVisibleValue; // максимально допустимое отображаемое значение "99:59:59" или "99:59"
  final IHmsValue value;           // текщее значение

  boolean ignoreVerify = true; // признак игнорирования правки в text изнутри кода этого виджета

  /**
   * Конструктор.
   * <p>
   * Кроме используемых родительским {@link Composite} битами стиля aStyle, использует следующие биты:
   * <ul>
   * <li>{@link SWT#READ_ONLY} - не позволяет редактировать значение;</li>
   * <li>{@link SWT#TIME} - виджет используется для ввода момента времени, а не интервала времени.;</li>
   * <li>{@link SWT#SHORT} - использует формат ММ:СС вместо ЧЧ:ММ:СС;</li>
   * </ul>
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aStyle int - стиль контроля, биты <code>SWT.XXX</code>, собранные по ИЛИ
   */
  public HmsWidget( Composite aParent, int aStyle ) {
    super( aParent, aStyle );
    this.setLayout( new SelfLayout() );
    int maxValue;
    if( isHoursUsed() ) {
      template = HHMMSS_TEMPLATE;
      templateLength = HHMMSS_TEMPLATE_LENGTH;
      maxValue = MAX_HHMMSS_VALUE;
    }
    else {
      template = MMSS_TEMPLATE;
      templateLength = MMSS_TEMPLATE_LENGTH;
      maxValue = MAX_MMSS_VALUE;
    }
    if( isTime() ) {
      maxValue = isHoursUsed() ? MAX_DAY_TIME_VALUE : MAX_HOUR_TIME_VALUE;
    }
    maxVisibleValue = maxValue;
    text.setText( template );
    value = new HmsValue();
    value.configure( isHoursUsed(), 0, maxValue, false );
    value.genericChangeEventer().addListener( hmsValueChangeListener );
    ignoreVerify = false;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private boolean isHoursUsed() {
    return (getStyle() & SWT.SHORT) == 0;
  }

  private boolean isTime() {
    return (getStyle() & SWT.TIME) != 0;
  }

  @Override
  protected void doHandleVerify( Event aEvent ) {
    // игнорируем вставку из кода этого метода
    if( ignoreVerify ) {
      return;
    }
    // указываем SWT, чтобы он ничего не делал в результате это правки - мы сами все сделаем
    aEvent.doit = false;
    // если текст поступил за пределами шаблона TEMPLATE, то игнорируем его
    int start = aEvent.start;
    if( start >= templateLength ) {
      return;
    }
    // измененноый текст, который поступил в событии
    String newText = aEvent.text;
    int length = newText.length();
    // теперь сфоримруем отредактированный текст, и посмотрим, правильный ли он
    StringBuilder sb = new StringBuilder( text.getText() );
    int end = aEvent.start + length;
    sb.replace( aEvent.start, end, newText );
    ValidationResult vr = value.validateText( sb.toString() );
    // если правильный, то изменим значение
    if( !vr.isError() ) {
      value.setValue( sb.toString() );
      text.setSelection( end, end );
    }
  }

  /**
   * Находит компоненту по позиции курсора в тексте {@link #template}.
   *
   * @param aPos - позиция курсора от 0 до {@link #templateLength} (включительно)
   * @return {@link EHmsPart} - компонента
   * @throws TsIllegalArgumentRtException позиция имеет недопустимое значение
   */
  EHmsPart getPartByCaretPos( int aPos ) {
    TsIllegalArgumentRtException.checkTrue( aPos < 0 || aPos > templateLength );
    if( !text.isFocusControl() ) {
      return EHmsPart.SS;
    }
    if( aPos >= templateLength - 2 ) {
      return EHmsPart.SS;
    }
    if( aPos >= templateLength - 5 ) {
      return EHmsPart.MM;
    }

    return EHmsPart.HH;
  }

  @Override
  protected void doHandleChangeRequest( int aDelta ) {
    int pos = text.getCaretPosition();
    EHmsPart part = getPartByCaretPos( pos );
    value.changeValue( part, aDelta );
  }

  void updateOnHmsValueChange() {
    int pos = text.getCaretPosition();
    ignoreVerify = true;
    text.setText( value.getText() );
    text.setSelection( pos, pos );
    ignoreVerify = false;
    fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает значение.
   *
   * @return int - значение в секундах в пределах 0 .. {@link #maxValue()}
   */
  public int getValue() {
    return value.getValue();
  }

  /**
   * Проверят, можно ли задать запрошенное значение.
   * <p>
   * Проверяет значение на нахождение в пределах {@link #minValue()} .. {@link #maxValue()}.
   *
   * @param aValue int - количество секунд
   * @return {@link ValidationResult} - результат проверки
   */
  public ValidationResult canSetValue( int aValue ) {
    return value.validateValue( aValue );
  }

  /**
   * Задает значение.
   *
   * @param aValue int - значение в секундах
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  public void setValue( int aValue ) {
    TsValidationFailedRtException.checkError( value.validateValue( aValue ) );
    value.setValue( aValue );
  }

  /**
   * Задает значение по компонентам.
   *
   * @param aHh int - часы
   * @param aMm int - минуты
   * @param aSs int - секунды
   * @throws TsIllegalArgumentRtException любая компонента < 0
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  public void setValue( int aHh, int aMm, int aSs ) {
    value.setValue( aHh, aMm, aSs );
  }

  /**
   * Возвращает минимально допустимое значение.
   *
   * @return int - минимально допустимое значение в секундах, больше или равно 0
   */
  public int minValue() {
    return value.minValue();
  }

  /**
   * Возвращает максимально допустимое значение.
   *
   * @return int - максимально допустимое значение в секундах, больше или равно {@link #minValue()}
   */
  public int maxValue() {
    return value.maxValue();
  }

  /**
   * Задает границы изменения значения.
   * <p>
   * После изменения границ, существующее значение "загоняется" в указанный диапазон.
   *
   * @param aMinValue int - минимальное значение в секундах
   * @param aMaxValue int - маскимальное значение в секундах
   * @throws TsIllegalArgumentRtException любое значение < 0
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   * @throws TsIllegalArgumentRtException любое значение больше максимально отображаемого
   */
  public void setLimits( int aMinValue, int aMaxValue ) {
    TsIllegalArgumentRtException.checkTrue( aMinValue < 0 || aMaxValue < 0 );
    TsIllegalArgumentRtException.checkTrue( aMaxValue < aMinValue );
    TsIllegalArgumentRtException.checkTrue( aMaxValue > maxVisibleValue );
    value.configure( isHoursUsed(), aMinValue, aMaxValue, false );
  }

  /**
   * Создает биты стилей для конструктора {@link HmsWidget#HmsWidget(Composite, int)}.
   *
   * @param aIsHoursUsed boolean - признак длинного формата "ЧЧ:ММ:СС" (а не "ММ:СС")
   * @param aIsReadOnly boolean - принак отсутствия возможности редактирования
   * @param aIsTime boolean - признак времени суток "23:59:59" (для краткого формата "59:59")
   * @return int - нужные из битов {@link SWT#SHORT}, {@link SWT#READ_ONLY} и {@link SWT#TIME} собранные по ИЛИ
   */
  public static int makeStyle( boolean aIsHoursUsed, boolean aIsReadOnly, boolean aIsTime ) {
    int style = SWT.SHORT;
    if( aIsHoursUsed ) {
      style = 0;
    }
    if( aIsReadOnly ) {
      style |= SWT.READ_ONLY;
    }
    if( aIsTime ) {
      style |= SWT.TIME;
    }
    return style;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return value.getText();
  }

  // TEST
  @SuppressWarnings( "javadoc" )
  public static void main( String[] aArgs ) {
    Display display = new Display();
    Shell shell = new Shell( display );
    shell.setLayout( new BorderLayout() );
    HmsWidget widget = new HmsWidget( shell, SWT.TIME );
    widget.setValue( 96 );
    shell.pack();
    shell.open();
    while( !shell.isDisposed() ) {
      if( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    display.dispose();
  }

}
