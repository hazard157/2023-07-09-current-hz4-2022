package com.hazard157.psx24.core.m5.trailer;

import static com.hazard157.psx24.core.m5.trailer.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;

/**
 * Управление жизненным циклом сущностей {@link Trailer} для конкретного эпизода.
 *
 * @author hazard157
 */
public class EpisodeTrailerLifecycleManager
    extends M5LifecycleManager<Trailer, IUnitTrailers> {

  private final IEpisode episode;

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model} - модель
   * @param aEpisode {@link IEpisode} - эпизод
   * @param aMaster {@link IUnitTrailers} - менеджер трейлеров
   * @throws TsNullArgumentRtException aModel = null
   */
  public EpisodeTrailerLifecycleManager( IM5Model<Trailer> aModel, IEpisode aEpisode, IUnitTrailers aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    episode = TsNullArgumentRtException.checkNull( aEpisode );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<Trailer> aValues ) {
    String episodeId = TrailerM5Model.EPISODE_ID.getFieldValue( aValues );
    IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
    if( !unitEpisodes.items().hasKey( episodeId ) ) {
      return ValidationResult.error( FMT_ERR_INV_EPISODE_ID, episodeId );
    }
    if( !episodeId.equals( episode.id() ) ) {
      return ValidationResult.error( FMT_ERR_NONSELF_EPISODE_ID, episode.id() );
    }
    String localId = TrailerM5Model.LOCAL_ID.getFieldValue( aValues ).asString();
    String id = TrailerUtils.createTrailerId( episodeId, localId );
    if( master().tsm().items().hasKey( id ) ) {
      return ValidationResult.error( FMT_ERR_DUP_TRAILER_ID, id );
    }
    return ValidationResult.SUCCESS;
  }

  private IStridablesList<Trailer> episodeTrailers() {
    return master().listTrailersByEpisode( episode.id() );
  }

  @Override
  protected IList<Trailer> doListEntities() {
    return episodeTrailers();
  }

}
