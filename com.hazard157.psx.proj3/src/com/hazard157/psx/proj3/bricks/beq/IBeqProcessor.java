package com.hazard157.psx.proj3.bricks.beq;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Процессор (исполнитель) запросов.
 * <p>
 * Процессор выдает результат запроса {@link #queryData(ITsCombiFilterParams)} над входными данными
 * {@link #inputData()}.
 *
 * @author goga
 */
public interface IBeqProcessor {

  /**
   * Возвращает набор входных данных, над которыми будет осуществлена выборка.
   * <p>
   * Входные данные задаются один раз в конструкторе процессора.
   *
   * @return {@link IBeqResult} - входные данные
   */
  IBeqResult inputData();

  /**
   * Выполняет запрос - выбирает данные из {@link #inputData()} по критериям аргумента - параметров запроса.
   * <p>
   * Если входные данные пустые, то возвращает пустой набор.
   *
   * @param aFilterParams {@link ITsCombiFilterParams} - параметры запроса
   * @return {@link IBeqResult} - результаты запроса
   * @throws TsNullArgumentRtException аргумент = null
   */
  IBeqResult queryData( ITsCombiFilterParams aFilterParams );

}
