package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.episodes.EpisodeM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * LM for {@link EpisodeM5Model}.
 *
 * @author hazard157
 */
class EpisodeLifecycleManager
    extends M5LifecycleManager<IEpisode, IUnitEpisodes> {

  public EpisodeLifecycleManager( IM5Model<IEpisode> aModel, IUnitEpisodes aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  private static final EpisodeInfo makeInfo( IM5Bunch<IEpisode> aValues ) {
    long when = WHEN.getFieldValue( aValues ).asLong();
    String name = NAME.getFieldValue( aValues ).asString();
    String descr = DESCRIPTION.getFieldValue( aValues ).asString();
    String place = PLACE.getFieldValue( aValues ).asString();
    int dur = DURATION.getFieldValue( aValues ).asInt();
    Secint actIn = ACTION_INTERVAL.getFieldValue( aValues );
    String defTrailerId = DEF_TRAILER_ID.getFieldValue( aValues ).asString();
    String notes = NOTES.getFieldValue( aValues ).asString();
    IFrame frame = FRAME.getFieldValue( aValues );
    return new EpisodeInfo( when, name, descr, place, dur, actIn, defTrailerId, notes, frame );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<IEpisode> aValues ) {
    EpisodeInfo info = makeInfo( aValues );
    String epId = EpisodeUtils.when2EpisodeId( info.when() );
    return master().canCreateItem( epId, info );
  }

  @Override
  protected IEpisode doCreate( IM5Bunch<IEpisode> aValues ) {
    EpisodeInfo info = makeInfo( aValues );
    String epId = EpisodeUtils.when2EpisodeId( info.when() );
    return master().createItem( epId, info );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<IEpisode> aValues ) {
    EpisodeInfo info = makeInfo( aValues );
    return master().canEditItem( aValues.originalEntity().id(), aValues.originalEntity().id(), info );
  }

  @Override
  protected IEpisode doEdit( IM5Bunch<IEpisode> aValues ) {
    EpisodeInfo info = makeInfo( aValues );
    aValues.originalEntity().setInfo( info );
    return aValues.originalEntity();
  }

  @Override
  protected ValidationResult doBeforeRemove( IEpisode aEntity ) {
    return master().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( IEpisode aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<IEpisode> doListEntities() {
    return master().items();
  }

}
