package com.hazard157.psx24.core.m5.trailer;

import static com.hazard157.psx24.core.m5.trailer.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;

/**
 * Управление жизненным циклом сущностей {@link Trailer}.
 *
 * @author hazard157
 */
class UnitTrailerLifecycleManager
    extends M5LifecycleManager<Trailer, IUnitTrailers> {

  public UnitTrailerLifecycleManager( IM5Model<Trailer> aModel, IUnitTrailers aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<Trailer> aValues ) {
    String episodeId = TrailerM5Model.EPISODE_ID.getFieldValue( aValues );
    IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
    if( !unitEpisodes.items().hasKey( episodeId ) ) {
      return ValidationResult.error( FMT_ERR_INV_EPISODE_ID, episodeId );
    }
    String localId = TrailerM5Model.LOCAL_ID.getFieldValue( aValues ).asString();
    String id = TrailerUtils.createTrailerId( episodeId, localId );
    if( master().tsm().items().hasKey( id ) ) {
      return ValidationResult.error( FMT_ERR_DUP_TRAILER_ID, id );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected IList<Trailer> doListEntities() {
    return master().tsm().items();
  }

}
