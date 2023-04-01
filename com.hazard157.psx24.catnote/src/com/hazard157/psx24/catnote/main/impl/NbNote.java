package com.hazard157.psx24.catnote.main.impl;

import static com.hazard157.psx24.catnote.main.INbNotebookConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * {@link INbNote} implementation.
 *
 * @author hazard157
 */
class NbNote
    extends StridableParameterized
    implements INbNote, IGenericChangeEventCapable {

  static final IStriparCreator<INbNote> CREATOR = NbNote::new;

  private final ITsCollectionChangeListener paramsChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      eventer.fireChangeEvent();
    }
  };

  final GenericChangeEventer eventer;

  private final INotifierOptionSetEdit paredit;

  public NbNote( String aId, IOptionSet aParams ) {
    super( aId, aParams );
    eventer = new GenericChangeEventer( this );
    paredit = new NotifierOptionSetEditWrapper( super.params() );
    paredit.addCollectionChangeListener( paramsChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public INotifierOptionSetEdit params() {
    return paredit;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // INbNote
  //

  @Override
  public ENbNoteKind kind() {
    return OP_NOTE_KIND.getValue( paredit ).asValobj();
  }

  @Override
  public String categoryId() {
    return OP_CATEGORY_ID.getValue( paredit ).asString();
  }

}
