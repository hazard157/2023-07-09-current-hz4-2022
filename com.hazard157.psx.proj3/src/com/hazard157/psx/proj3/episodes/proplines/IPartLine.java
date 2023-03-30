package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Линия деления видеоряда на непрерывные части.
 *
 * @author hazard157
 * @param <T> - тип информации о части
 */
public interface IPartLine<T>
    extends IAnyPropLineBase {

  /**
   * Возвращает количество частей.
   *
   * @return int - количество частей
   */
  int count();

  /**
   * Возвращает начало части.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @return int - начало части в секундах
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  int partStart( int aIndex );

  /**
   * Возвращает длительность части.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @return int - длительность части в секундах
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  int partDuration( int aIndex );

  /**
   * Возвращает интервал времени части.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @return {@link Secint} - интервал времени части
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  Secint partInterval( int aIndex );

  /**
   * Возвращает информацию о части.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @return &lt;T&gt; - информация о части
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  T partInfo( int aIndex );

  /**
   * Доабавляет часть в указанное место ряда.
   *
   * @param aStart int - начальная секунда
   * @param aInfo &lt;T&gt; - инфорамция о части
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemAlreadyExistsRtException на указанной секунде уже есть часть
   * @throws TsIllegalArgumentRtException начало выходит за длительность ряда
   */
  void addPart( int aStart, T aInfo );

  /**
   * Задает началное время части.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @param aStart int - начальная секунда
   * @throws TsIllegalArgumentRtException недопустимый индекс
   * @throws TsItemAlreadyExistsRtException на указанной секунде уже есть часть
   * @throws TsIllegalArgumentRtException начало выходит за длительность ряда
   */
  void setPartStart( int aIndex, int aStart );

  /**
   * Задает длительность части.
   * <p>
   * Фактически, смещает следующие части на значение изменения длительности части.
   * <p>
   * Обратите внимание, что попытка установки длительности для последней части игнорируется.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @param aDuration int - длительности части
   * @throws TsIllegalArgumentRtException недопустимый индекс
   * @throws TsIllegalArgumentRtException невозможно настолько изменить длительность - выход за пределы ряда
   */
  void setPartDuration( int aIndex, int aDuration );

  /**
   * Задает информацию о части.
   *
   * @param aIndex int - индекс части (0 .. {@link #count()}-1)
   * @param aInfo &lt;T&gt; - инфорамция о части
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException недопустимый индекс
   */
  void setPartInfo( int aIndex, T aInfo );

}
