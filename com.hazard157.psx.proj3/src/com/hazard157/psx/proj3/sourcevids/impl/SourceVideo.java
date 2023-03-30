package com.hazard157.psx.proj3.sourcevids.impl;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Исходное виде эпизода, снятое одной камерой.
 *
 * @author hazard157
 */
class SourceVideo
    extends AbstractSinentity<SourceVideoInfo>
    implements ISourceVideo {

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор исходнго видео согласно {@link SourceVideoUtils#validateSourceVideoId(String)}
   * @param aInfo {@link SourceVideoInfo} - инофрмация об исходном видео
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException неверный идентификатор исходного видео
   */
  public SourceVideo( String aId, SourceVideoInfo aInfo ) {
    super( SourceVideoUtils.checkValidSourceVideoId( aId ), aInfo );
  }

  @Override
  public String id() {
    return super.id();
  }

  @Override
  public String nmName() {
    return info().location();
  }

  @Override
  public String description() {
    return info().description();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIdable
  //

  @Override
  public String episodeId() {
    return SourceVideoUtils.extractEpisodeId( id() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICameraIdable
  //

  @Override
  public String cameraId() {
    return SourceVideoUtils.extractCamId( id() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    return info().frame();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISourceVideo
  //

  /**
   * Возвращает длительность исходного видео.
   *
   * @return int - длительность исходного видео в секундах
   */
  @Override
  public int duration() {
    return info().duration();
  }

  /**
   * Возвращает название место расположения камеры.
   *
   * @return String - место расположения камеры
   */
  @Override
  public String location() {
    return info().location();
  }

}
