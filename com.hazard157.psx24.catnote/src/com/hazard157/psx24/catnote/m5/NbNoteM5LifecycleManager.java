package com.hazard157.psx24.catnote.m5;

import static com.hazard157.psx24.catnote.m5.INbNotebookM5Constants.*;
import static com.hazard157.psx24.catnote.main.INbNotebookConstants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * Lifecycle manager for {@link NbNoteM5Model}.
 *
 * @author hazard157
 */
class NbNoteM5LifecycleManager
    extends M5LifecycleManager<INbNote, INbNotebook> {

  public NbNoteM5LifecycleManager( IM5Model<INbNote> aModel, INbNotebook aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    TsNullArgumentRtException.checkNull( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IOptionSet makeParams( IM5Bunch<INbNote> aValues ) {
    IOptionSetEdit p = new OptionSet();
    p.setValue( TSID_ID, aValues.getAsAv( FID_ID ) );
    p.setValue( TSID_NAME, aValues.getAsAv( FID_NAME ) );
    p.setValue( TSID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ) );
    INbCategory c = aValues.getAs( FID_NB_NOTE_CATEGORY_ID, INbCategory.class );
    String categId = TsLibUtils.EMPTY_STRING;
    if( c != null ) {
      categId = c.id();
    }
    p.setStr( OP_CATEGORY_ID, categId );
    p.setValue( OP_NOTE_KIND, avValobj( aValues.getAs( FID_NB_NOTE_KIND, ENbNoteKind.class ) ) );
    return p;
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<INbNote> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().notes().svs().validator().canCreateItem( id, p );
  }

  @Override
  protected INbNote doCreate( IM5Bunch<INbNote> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().notes().createItem( id, p );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<INbNote> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().notes().svs().validator().canEditItem( aValues.originalEntity().id(), id, p );
  }

  @Override
  protected INbNote doEdit( IM5Bunch<INbNote> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IOptionSet p = makeParams( aValues );
    return master().notes().editItem( aValues.originalEntity().id(), id, p );
  }

  @Override
  protected ValidationResult doBeforeRemove( INbNote aEntity ) {
    return master().notes().svs().validator().canRemoveItem( aEntity.id() );
  }

  @Override
  protected void doRemove( INbNote aEntity ) {
    master().notes().removeItem( aEntity.id() );
  }

  @Override
  protected IList<INbNote> doListEntities() {
    return master().notes().items();
  }

}
