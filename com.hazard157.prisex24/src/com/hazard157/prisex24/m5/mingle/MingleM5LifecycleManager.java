package com.hazard157.prisex24.m5.mingle;

import static com.hazard157.prisex24.m5.mingle.IPsxResources.*;
import static com.hazard157.psx.proj3.mingle.IMingleConstants.*;

import java.time.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.mingle.*;
import com.hazard157.psx.proj3.mingle.impl.*;

class MingleM5LifecycleManager
    extends M5LifecycleManager<IMingle, IUnitMingles> {

  public MingleM5LifecycleManager( IM5Model<IMingle> aModel, IUnitMingles aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  private static IOptionSet makeMingleInfo( IM5Bunch<IMingle> aValues ) {
    IOptionSetEdit p = new OptionSet();
    for( IDataDef dd : ALL_MINGLE_OPS ) {
      p.setValue( dd, aValues.getAsAv( dd.id() ) );
    }
    return p;
  }

  private ValidationResult internalValidateFileSystem( IOptionSet aInfo ) {
    LocalDate ld = OPDEF_DATE.getValue( aInfo ).asValobj();
    ValidationResult vr = ValidationResult.SUCCESS;
    // FIXME check if original media directory exists
    // IPfsOriginalMedia fsOm = tsContext().get( IPfsOriginalMedia.class );
    // if( !fsOm.listDates().hasElem( ld ) ) {
    // vr = ValidationResult.warn( FMT_WARN_NO_ORIG_MEDIA_DIR, ld.toString() );
    // }
    // check if episode already exists for the same date
    String episodeId = EpisodeUtils.localDate2EpisodeId( ld );
    IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
    if( unitEpisodes.items().hasKey( episodeId ) ) {
      vr = ValidationResult.warn( FMT_WARN_EPISODE_OF_SAME_DATE, ld.toString() );
    }
    return vr;
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<IMingle> aValues ) {
    IOptionSet info = makeMingleInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = MingleUtils.localDate2MingleId( ld );
    ValidationResult vr = master().svs().validator().canCreateItem( id, info );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, internalValidateFileSystem( info ) );
  }

  @Override
  protected IMingle doCreate( IM5Bunch<IMingle> aValues ) {
    IOptionSet info = makeMingleInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = MingleUtils.localDate2MingleId( ld );
    return master().createItem( id, info );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<IMingle> aValues ) {
    IOptionSet info = makeMingleInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = MingleUtils.localDate2MingleId( ld );
    ValidationResult vr = master().svs().validator().canEditItem( aValues.originalEntity().id(), id, info );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, internalValidateFileSystem( info ) );
  }

  @Override
  protected IMingle doEdit( IM5Bunch<IMingle> aValues ) {
    IOptionSet info = makeMingleInfo( aValues );
    LocalDate ld = OPDEF_DATE.getValue( info ).asValobj();
    String id = MingleUtils.localDate2MingleId( ld );
    return master().editItem( aValues.originalEntity().id(), id, info );
  }

  @Override
  protected ValidationResult doBeforeRemove( IMingle aEntity ) {
    return master().svs().validator().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( IMingle aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<IMingle> doListEntities() {
    return master().items();
  }

}
