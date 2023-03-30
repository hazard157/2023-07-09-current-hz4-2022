package com.hazard157.psx.proj3.sourcevids;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Инофрмаиця об исходном видео.
 *
 * @author hazard157
 */
public class SourceVideoInfo
    implements IFrameable {

  private final int    duration;
  private final String location;
  private final String description;
  private final IFrame frame;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aDuration int - длительность в секундах
   * @param aLocation String - место расположения камеры
   * @param aDescription String - пояснения к видео, к съемке - направление, увеличение и др.
   * @param aFrame {@link IFrame} - иллюстрирующий кадр
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException aDuration выходит за допустимые пределы
   */
  public SourceVideoInfo( int aDuration, String aLocation, String aDescription, IFrame aFrame ) {
    TsNullArgumentRtException.checkNulls( aLocation, aDescription, aFrame );
    HmsUtils.checkMmSsDuration( aDuration );
    location = aLocation;
    description = aDescription;
    duration = aDuration;
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
   * Возвращает длительность видео в секундах.
   *
   * @return int - длительность видео в секундах
   */
  public int duration() {
    return duration;
  }

  /**
   * Возвращает название место расположения камеры.
   *
   * @return String - место расположения камеры
   */
  public String location() {
    return location;
  }

  /**
   * Возвращает пояснения к видео, к съемке - направление, увеличение и др.
   *
   * @return String - пояснения к видео, к съемке - направление, увеличение и др
   */
  public String description() {
    return description;
  }

}
