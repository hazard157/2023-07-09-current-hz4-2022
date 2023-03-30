package com.hazard157.psx.proj3.trailers;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.movies.*;

/**
 * Трейлер эпизода.
 *
 * @author hazard157
 */
public class Trailer
    extends AbstractMovie<TrailerInfo>
    implements IEpisodeIdable {

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор трейлера согласно {@link TrailerUtils#validateTrailerId(String)}
   * @param aInfo {@link TrailerInfo} - инофрмация о трейлере
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException неверный идентификатор трейлера
   */
  public Trailer( String aId, TrailerInfo aInfo ) {
    super( TrailerUtils.checkValidTrailerId( aId ), aInfo );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса INameable
  //

  @Override
  public String nmName() {
    return info().name();
  }

  @Override
  public String description() {
    return info().description();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIdable
  //

  /**
   * Возвращает идентификатор эпизода.
   *
   * @return String - валидный идентификатор эпизода
   */
  @Override
  public String episodeId() {
    return TrailerUtils.extractEpisodeId( id() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает кадры кусков в порядке раположения кусков.
   *
   * @return {@link IList}&lt;{@link IFrame}&gt; - кадры кусков трейлера
   */
  public IList<IFrame> listFramesOfChunks() {
    IListEdit<IFrame> frames = new ElemArrayList<>();
    for( Chunk c : chunks() ) {
      frames.add( c.frame() );
    }
    return frames;
  }

  /**
   * Возвращает локальный идентификатор (внутри эпизода) трейлера.
   *
   * @return String - локальный идентификатор (ИД-имя) трейлера
   */
  public String localId() {
    return TrailerUtils.extractLocalId( id() );
  }

}
