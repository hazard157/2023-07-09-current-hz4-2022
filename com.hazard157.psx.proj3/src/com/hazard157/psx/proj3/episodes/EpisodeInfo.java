package com.hazard157.psx.proj3.episodes;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;

/**
 * Описание эпизода.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public final class EpisodeInfo
    implements IFrameable {

  private final long   when;
  private final String name;
  private final String description;
  private final String place;
  private final int    duration;
  private final Secint actionInterval;
  private final String defaultTrailerId;
  private final String notes;
  private final IFrame frame;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aWhen long - метка времени эпизода
   * @param aName String - название
   * @param aDescription String - описание
   * @param aPlace String - место, где случился эпизод
   * @param aDuration int - длительность в секундах
   * @param aActionInterval {@link Secint} - интервал акта
   * @param aDefaultTrailerId String - идентификатор трейлера по умолчанию или пустая строка
   * @param aNotes String - что еще нужно делать
   * @param aFrame {@link IFrame} - иллюстрирующий кадр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aWhen выходит за допустимые пределы
   * @throws TsValidationFailedRtException aDuration выходит за допустимые пределы
   * @throws TsIllegalArgumentRtException aActionInterval выходит за допустимые пределы
   */
  public EpisodeInfo( long aWhen, String aName, String aDescription, String aPlace, int aDuration,
      Secint aActionInterval, String aDefaultTrailerId, String aNotes, IFrame aFrame ) {
    EpisodeUtils.checkWhen( aWhen );
    TsNullArgumentRtException.checkNulls( aName, aDescription, aPlace, aNotes, aActionInterval, aDefaultTrailerId,
        aFrame );
    HmsUtils.checkMmSsDuration( aDuration );
    TsIllegalArgumentRtException.checkTrue( aActionInterval.end() > aDuration );
    when = aWhen;
    name = aName;
    description = aDescription;
    place = aPlace;
    duration = aDuration;
    actionInterval = aActionInterval;
    defaultTrailerId = aDefaultTrailerId;
    notes = aNotes;
    frame = aFrame;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    return frame;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает метку времени начала эпизода.
   *
   * @return long - метка времени начала эпизода
   */
  public long when() {
    return when;
  }

  /**
   * Возвращает название эпизода.
   *
   * @return String - название эпизода
   */
  public String name() {
    return name;
  }

  /**
   * Возвращает описание эпизода.
   *
   * @return String - описание эпизода
   */
  public String description() {
    return description;
  }

  /**
   * Возвращает название места, где случился эпизод.
   *
   * @return String - место, где случился эпизод
   */
  public String place() {
    return place;
  }

  /**
   * Возвращает длительность эпизода в секундах.
   *
   * @return int - длительность эпизода в секундах
   */
  public int duration() {
    return duration;
  }

  /**
   * Возвращает интервал активного действия.
   *
   * @return int - интервал действия в секундах
   */
  public Secint actionInterval() {
    return actionInterval;
  }

  /**
   * Возвращает идентификатор трейлера по умолчанию.
   *
   * @return String - идентификатор трейлера по умолчанию или пустая строка
   */
  public String defaultTrailerId() {
    return defaultTrailerId;
  }

  /**
   * Возвращает перечень того, какие работы по эпизоду еще не выполнены.
   *
   * @return String - какие работы по эпизоду еще не выполнены
   */
  public String notes() {
    return notes;
  }

}
