package com.hazard157.lib.core.excl_done.hms;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Количество секунд (всегда неотрицательное), представленное в виде компонент "ЧЧЧ:ММ:СС" или "МММ:СС".
 * <p>
 * Сравнение всех реализации происходит только по значению {@link #getValue()}.
 *
 * @author hazard157
 */
public interface IHmsValue
    extends IGenericChangeEventCapable, Comparable<IHmsValue> {

  /**
   * Определает, использует ли текстовое представление значение часов.
   *
   * @return boolean - текстовое представление использует значение часов<br>
   *         <b>true</b> - формат "ЧЧЧ:ММ:СС";<br>
   *         <b>false</b> - формат "МММ:СС".
   */
  boolean isHoursUsed();

  /**
   * Возвращает минимально допустимое значение.
   *
   * @return int - минимально допустимое значение в секундах, больше или равно 0
   */
  int minValue();

  /**
   * Возвращает максимально допустимое значение.
   *
   * @return int - максимально допустимое значение в секундах, больше или равно {@link #minValue()}
   */
  int maxValue();

  /**
   * Возвращает признак точного режима.
   * <p>
   * В точном режиме разделителями допускаются только символы двоеточия ':'. В неточном режиме - любой символ. В любом
   * режиме допускается только строки формата "HH.MM.SS" / "MM.SS", где вместо точки либо двоеточие, любо любой символ,
   * в том числе буква или цифра.
   *
   * @return aStrictMode boolean - признак точного режима
   */
  boolean isStrictMode();

  /**
   * Конфигурирует значение.
   * <p>
   * Если текущее значение выходит за допустимые пределы, оно "подгоняется" в допустимый предел.
   *
   * @param aIsHoursUsed boolean - текстовое представление использует значение часов<br>
   *          <b>true</b> - формат "ЧЧЧ:ММ:СС";<br>
   *          <b>false</b> - формат "МММ:СС".
   * @param aMinValue int - минимально допустимое значение в секундах
   * @param aMaxValue int - максимально допустимое значение в секундах
   * @param aStrictMode boolean - точный режим, разрешаются разделители только символ ':', иначе - любой
   * @throws TsIllegalArgumentRtException любой аргумент < 0
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   */
  void configure( boolean aIsHoursUsed, int aMinValue, int aMaxValue, boolean aStrictMode );

  /**
   * Возвращает текстовое представление максимального значения {@link #maxValue()}.
   * <p>
   * Формат возвращаемого текста зависит от признака {@link #isHoursUsed()}.
   *
   * @return String - текст вида "ЧЧЧ:ММ:СС" или "МММ:СС"
   */
  String maxValueText();

  /**
   * Возвращает текстовое представление минимального значения {@link #maxValue()}.
   * <p>
   * Формат возвращаемого текста зависит от признака {@link #isHoursUsed()}.
   *
   * @return String - текст вида "ЧЧЧ:ММ:СС" или "МММ:СС"
   */
  String minValueText();

  /**
   * Возвращает текстовое представление значения.
   *
   * @return String - текст вида "ЧЧЧ:ММ:СС" или "МММ:СС"
   */
  String getText();

  /**
   * Возвращает значение.
   *
   * @return int - значение в секундах в пределах 0 .. {@link #maxValue()}
   */
  int getValue();

  /**
   * Возвращает значение указанной компоненты.
   * <p>
   * Если не используются часы ({@link #isHoursUsed()}=<code>false</code>), то для компоненты {@link EHmsPart#HH} всегда
   * возвращает 0.
   *
   * @param aPart {@link EHmsPart} - компонента
   * @return int - значение компоненты
   */
  int getPartValue( EHmsPart aPart );

  // ------------------------------------------------------------------------------------
  // Методы редактирования
  //

  /**
   * Задает значение.
   *
   * @param aValue int - значение в секундах
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  void setValue( int aValue );

  /**
   * Задает значение по компонентам.
   *
   * @param aHh int - часы
   * @param aMm int - минуты
   * @param aSs int - секунды
   * @throws TsIllegalArgumentRtException любая компонента < 0
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  void setValue( int aHh, int aMm, int aSs );

  /**
   * Задает значение компоненты.
   * <p>
   * Идея метода в том, что в текстовом представлении "ЧЧЧ:ММ:СС" или "МММ:СС" изменить только одну компоненту, чтобы
   * остальные остались без изменения. Поэтому аргумент aValue имеет пределы. Например, для секунд это всегда 0..59.
   * <p>
   * Если часы не используются {@link #isHoursUsed()} = <code>false</code>, при указании {@link EHmsPart#HH} aValue
   * должно быть 0.
   *
   * @param aPart {@link EHmsPart} - компонента
   * @param aValue int - значение
   * @throws TsIllegalArgumentRtException aValue < 0
   * @throws TsIllegalArgumentRtException aValue выходит за допустимые пределы компоненты
   * @throws TsIllegalArgumentRtException aValue != 0 при задании часов, когда они не используются
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  void setPartValue( EHmsPart aPart, int aValue );

  /**
   * Изменяет значение компоненты на указанную величину.
   * <p>
   * Идея метода в том, что в текстовом представлении "ЧЧЧ:ММ:СС" или "МММ:СС" изменить только одну компоненту, чтобы
   * остальные остались без изменения. Поэтому, если не используются часы {@link #isHoursUsed()}=<code>false</code>,
   * попытка задать {@link EHmsPart#HH} приводит к исключению.
   * <p>
   * Если значение допустимой компоненты должно выйти за допустимые пределы, то етод не генерирует исключение, а
   * значение компоненты принимает макисмально/минимально допустимое значение.
   *
   * @param aPart {@link EHmsPart} - компонента
   * @param aDelta int - на какую величину изменить компонентукакую величину (в единицах компоненты)
   * @throws TsIllegalArgumentRtException aPart = {@link EHmsPart#HH} когда {@link #isHoursUsed()} = <code>false</code>
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  void changePartValue( EHmsPart aPart, int aDelta );

  /**
   * Изменяет значение на указанную величину (в единицах указанной компоненты).
   * <p>
   * Метод не генерирует исключений - если полученное значение должно выйти за допустимые пределы, то оно принимает
   * значение {@link #minValue()} или {@link #maxValue()}.
   *
   * @param aPart {@link EHmsPart} - компонента
   * @param aDelta int - на какую величину (в единицах компоненты) изменить значение
   */
  void changeValue( EHmsPart aPart, int aDelta );

  /**
   * Изменяет значение на указанную величину.
   * <p>
   * Метод не генерирует исключений - если полученное значение должно выйти за допустимые пределы, то оно принимает
   * значение {@link #minValue()} или {@link #maxValue()}.
   * <p>
   * Равнозначно вызову {@link #changeValue(EHmsPart, int) changeValue(SS,aDelta)};
   *
   * @param aDelta int - на сколько секунд изменить значение
   */
  void changeValue( int aDelta );

  /**
   * Задает значение в виде текстовой строки формата "ЧЧЧ:ММ:СС" или "МММ:СС".
   * <p>
   * Оба вида текстовой строки распознаются, главное, чтобы задаваемое значение не выходило за пределы
   * {@link #minValue()} .. {@link #maxValue()}.
   *
   * @param aText String - текстовая строка формата "ЧЧЧ:ММ:СС" или "МММ:СС"
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException неверный формат текстовой строки
   * @throws TsValidationFailedRtException значение выходит за пределы {@link #minValue()} .. {@link #maxValue()}
   */
  void setValue( String aText );

  /**
   * Проверяет текстовое представление на валидность и его значение на допустимость.
   * <p>
   * Выдает сообщение об ошибке, если текстовая строка не может быть интепретирована как метка/интервал времени в
   * секундах. Также возвращает ошибку если значение, представленное текстом выходит за пределы {@link #minValue()} ..
   * {@link #maxValue()}.
   *
   * @param aText String - текстовая строка формата "ЧЧ:ММ:СС" или "ММ:СС"
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  ValidationResult validateText( String aText );

  /**
   * Проверяет значение на нахождение в пределах {@link #minValue()} .. {@link #maxValue()}.
   *
   * @param aValue int - количество секунд
   * @return {@link ValidationResult} - результат проверки
   */
  ValidationResult validateValue( int aValue );

}
