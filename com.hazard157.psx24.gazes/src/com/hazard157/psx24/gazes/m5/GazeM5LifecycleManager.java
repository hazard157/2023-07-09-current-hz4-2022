package com.hazard157.psx24.gazes.m5;

import static com.hazard157.psx.proj3.gaze.IGazeConstants.*;
import static com.hazard157.psx24.gazes.m5.GazeM5Model.*;
import static com.hazard157.psx24.gazes.m5.IPsxResources.*;

import java.time.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx.proj3.gaze.impl.*;

class GazeM5LifecycleManager
    extends M5LifecycleManager<IGaze, IUnitGazes> {

  public GazeM5LifecycleManager( IM5Model<IGaze> aModel, IUnitGazes aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  private static IOptionSet makeGazeInfo( IM5Bunch<IGaze> aValues ) {
    IOptionSetEdit p = new OptionSet();
    p.setValue( NAME.id(), NAME.getFieldValue( aValues ) );
    p.setValue( DESCRIPTION.id(), DESCRIPTION.getFieldValue( aValues ) );
    p.setValue( DATE.id(), DATE.getFieldValue( aValues ) );
    p.setValue( RATING.id(), RATING.getFieldValue( aValues ) );
    p.setValue( PLACE.id(), PLACE.getFieldValue( aValues ) );
    return p;
  }

  private ValidationResult internalValidateFileSystem( IOptionSet aInfo ) {
    LocalDate ld = OPDEF_DATE.getValue( aInfo ).asValobj();
    // check if original media directory exsists
    IPfsOriginalMedia fsOm = tsContext().get( IPfsOriginalMedia.class );
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !fsOm.listDates().hasElem( ld ) ) {
      vr = ValidationResult.warn( FMT_WARN_NO_ORIG_MEDIA_DIR, ld.toString() );
    }
    // check if episode already exists for the same date
    String episodeId = EpisodeUtils.localDate2EpisodeId( ld );
    IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
    if( unitEpisodes.items().hasKey( episodeId ) ) {
      vr = ValidationResult.warn( FMT_WARN_EPISODE_OF_SAME_DATE, ld.toString() );
    }
    return vr;
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<IGaze> aValues ) {
    IOptionSet info = makeGazeInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = GazeUtils.localDate2GazeId( ld );
    ValidationResult vr = master().svs().validator().canCreateItem( id, info );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, internalValidateFileSystem( info ) );
  }

  @Override
  protected IGaze doCreate( IM5Bunch<IGaze> aValues ) {
    IOptionSet info = makeGazeInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = GazeUtils.localDate2GazeId( ld );
    return master().createItem( id, info );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<IGaze> aValues ) {
    IOptionSet info = makeGazeInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = GazeUtils.localDate2GazeId( ld );
    ValidationResult vr = master().svs().validator().canEditItem( aValues.originalEntity().id(), id, info );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, internalValidateFileSystem( info ) );
  }

  @Override
  protected IGaze doEdit( IM5Bunch<IGaze> aValues ) {
    IOptionSet info = makeGazeInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = GazeUtils.localDate2GazeId( ld );
    return master().editItem( aValues.originalEntity().id(), id, info );
  }

  @Override
  protected ValidationResult doBeforeRemove( IGaze aEntity ) {
    return master().svs().validator().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( IGaze aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<IGaze> doListEntities() {
    return master().items();
  }

}
