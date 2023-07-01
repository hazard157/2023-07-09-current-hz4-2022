package com.hazard157.psx24.core.m5.songs;

import static com.hazard157.psx.proj3.songs.IUnitSongsConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.songs.*;

/**
 * LM for {@link SongM5Model}.
 *
 * @author hazard157
 */
class SongM5LifecycleManager
    extends M5LifecycleManager<ISong, IUnitSongs> {

  public SongM5LifecycleManager( IM5Model<ISong> aModel, IUnitSongs aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  IOptionSet makeParame( IM5Bunch<ISong> aValues ) {
    IOptionSetEdit p = new OptionSet();
    String name = aValues.get( SongM5Model.NAME ).asString();
    DDEF_NAME.setValue( p, avStr( name ) );
    String descr = aValues.get( SongM5Model.DESCRIPTION ).asString();
    DDEF_DESCRIPTION.setValue( p, avStr( descr ) );
    String filePath = aValues.get( SongM5Model.FILE_PATH ).asString();
    OP_FILE_PATH.setValue( p, avStr( filePath ) );
    int duration = aValues.get( SongM5Model.DURATION ).asInt();
    OP_DURATION.setValue( p, avInt( duration ) );
    return p;
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISong> aValues ) {
    String id = aValues.get( SongM5Model.ID ).asString();
    IOptionSet p = makeParame( aValues );
    return master().svs().validator().canCreateItem( id, p );
  }

  @Override
  protected ISong doCreate( IM5Bunch<ISong> aValues ) {
    String id = aValues.get( SongM5Model.ID ).asString();
    IOptionSet p = makeParame( aValues );
    return master().createItem( id, p );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISong> aValues ) {
    String id = aValues.get( SongM5Model.ID ).asString();
    IOptionSet p = makeParame( aValues );
    return master().svs().validator().canEditItem( aValues.originalEntity().id(), id, p );
  }

  @Override
  protected ISong doEdit( IM5Bunch<ISong> aValues ) {
    String id = aValues.get( SongM5Model.ID ).asString();
    IOptionSet p = makeParame( aValues );
    return master().editItem( aValues.originalEntity().id(), id, p );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISong aEntity ) {
    return master().svs().validator().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( ISong aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<ISong> doListEntities() {
    return master().items();
  }

}
