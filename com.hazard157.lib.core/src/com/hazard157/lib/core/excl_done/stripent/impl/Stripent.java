package com.hazard157.lib.core.excl_done.stripent.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_done.stripent.*;

/**
 * {@link IStripent} base implementation.
 *
 * @author hazard157
 */
public class Stripent
    implements IStripent {

  private final GenericChangeEventer   eventer;
  private final INotifierOptionSetEdit params = new NotifierOptionSetEditWrapper( new OptionSet() );

  private final String id;

  /**
   * Constructor.
   *
   * @param aId String the entity ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public Stripent( String aId, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    eventer = new GenericChangeEventer( this );
    params.addAll( aParams );
    params.addCollectionChangeListener( eventer );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return params.getValue( DDEF_NAME ).asString();
  }

  @Override
  public String description() {
    return params.getValue( DDEF_DESCRIPTION ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public INotifierOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public GenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  @SuppressWarnings( "javadoc" )
  public static void writeSinentBase( IStrioWriter aSw, IStripent aEntity ) {
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.params() );
  }

  @SuppressWarnings( "javadoc" )
  public static IStridableParameterized readSinentBase( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
    return new StridableParameterized( id, params );
  }

}
