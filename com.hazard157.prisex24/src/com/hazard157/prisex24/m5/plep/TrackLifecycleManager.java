package com.hazard157.prisex24.m5.plep;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.psx.proj3.pleps.IUnitPlepsConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx.proj3.songs.*;

/**
 * LM for {@link TrackM5Model}.
 *
 * @author hazard157
 */
class TrackLifecycleManager
    extends M5LifecycleManager<ITrack, IPlep> {

  TrackLifecycleManager( IM5Model<ITrack> aModel, IPlep aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  @Override
  protected ITrack doCreate( IM5Bunch<ITrack> aValues ) {
    String songId = aValues.getAs( FID_SONG_ID, ISong.class ).id();
    Secint interval = aValues.getAsAv( FID_INTERVAL ).asValobj();
    int insertionIndex = aValues.getAsAv( OPID_TRACK_INSERTION_INDEX ).asInt();
    if( insertionIndex < -1 || insertionIndex > master().stirs().size() ) {
      insertionIndex = -1;
    }
    return master().newTrack( insertionIndex, songId, interval );
  }

  @Override
  protected ITrack doEdit( IM5Bunch<ITrack> aValues ) {
    String songId = aValues.getAs( FID_SONG_ID, ISong.class ).id();
    Secint interval = aValues.getAsAv( FID_INTERVAL ).asValobj();
    int index = master().tracks().indexOf( aValues.originalEntity() );
    try {
      master().eventer().pauseFiring();
      master().removeTrack( index );
      return master().newTrack( index, songId, interval );
    }
    finally {
      master().eventer().resumeFiring( true );
    }
  }

  @Override
  protected void doRemove( ITrack aEntity ) {
    int index = master().tracks().indexOf( aEntity );
    master().tracks().removeByIndex( index );
  }

  @Override
  protected IList<ITrack> doListEntities() {
    return master().tracks();
  }

  @Override
  protected IListReorderer<ITrack> doGetItemsReorderer() {
    return master().tracksReorderer();
  }

}
