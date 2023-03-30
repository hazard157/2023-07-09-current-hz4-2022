package com.hazard157.psx24.core.m5.srcvids;

import static com.hazard157.psx24.core.m5.srcvids.SourceVideoM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.m5.std.*;

class SourceVideoLifecycleManager
    extends M5LifecycleManager<ISourceVideo, IUnitSourceVideos> {

  SourceVideoLifecycleManager( IM5Model<ISourceVideo> aModel, IUnitSourceVideos aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  private static SourceVideoInfo makeSourceVideoInfo( IM5Bunch<ISourceVideo> aValues ) {
    int duration = DURATION.getFieldValue( aValues ).asInt();
    String location = LOCATION.getFieldValue( aValues ).asString();
    String description = DESCRIPTION.getFieldValue( aValues ).asString();
    // IFrame frame = FRAME.getFieldValue( aValues );
    IFrame frame = aValues.getAs( PsxM5FrameFieldDef.FID_FRAME, IFrame.class );
    return new SourceVideoInfo( duration, location, description, frame );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISourceVideo> aValues ) {
    String episodeId = EPISODE_ID.getFieldValue( aValues );
    String camId = CAM_ID.getFieldValue( aValues );
    String id = SourceVideoUtils.createSourceVideoId( episodeId, camId );
    SourceVideoInfo svInfo = makeSourceVideoInfo( aValues );
    return master().canCreateItem( id, svInfo );
  }

  @Override
  protected ISourceVideo doCreate( IM5Bunch<ISourceVideo> aValues ) {
    String episodeId = EPISODE_ID.getFieldValue( aValues );
    String camId = CAM_ID.getFieldValue( aValues );
    String id = SourceVideoUtils.createSourceVideoId( episodeId, camId );
    SourceVideoInfo svInfo = makeSourceVideoInfo( aValues );
    return master().createItem( id, svInfo );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISourceVideo> aValues ) {
    String episodeId = EPISODE_ID.getFieldValue( aValues );
    String camId = CAM_ID.getFieldValue( aValues );
    String id = SourceVideoUtils.createSourceVideoId( episodeId, camId );
    SourceVideoInfo svInfo = makeSourceVideoInfo( aValues );
    return master().canEditItem( aValues.originalEntity().id(), id, svInfo );
    //
    // String id = aValues.getAsAv( FID_ID ).asString();
    // if( !id.equals( aValues.originalEntity().id() ) ) {
    // if( master().items().hasKey( id ) ) {
    // return ValidationResult.error( FMT_ERR_SV_ID_ALREADY_EXISTS, id );
    // }
    // }
    // return ValidationResult.SUCCESS;
  }

  @Override
  protected ISourceVideo doEdit( IM5Bunch<ISourceVideo> aValues ) {
    String episodeId = EPISODE_ID.getFieldValue( aValues );
    String camId = CAM_ID.getFieldValue( aValues );
    String id = SourceVideoUtils.createSourceVideoId( episodeId, camId );
    SourceVideoInfo svInfo = makeSourceVideoInfo( aValues );
    return master().editItem( aValues.originalEntity().id(), id, svInfo );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISourceVideo aEntity ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( ISourceVideo aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<ISourceVideo> doListEntities() {
    return master().items();
  }

}
