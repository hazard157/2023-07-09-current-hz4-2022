package com.hazard157.psx.proj3.episodes.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Реализация {@link IEpVerifyCfg}.
 *
 * @author hazard157
 */
public class EpVerifyCfg
    implements IEpVerifyCfg {

  private final ITsCollectionChangeListener collectionChangeListener = new ITsCollectionChangeListener() {

    @Override
    public void onCollectionChanged( Object aSource, ECrudOp aOp, Object aItem ) {
      eventer.fireChangeEvent();
    }
  };

  final GenericChangeEventer eventer;

  private final INotifierOptionSetEdit params = new NotifierOptionSetEditWrapper( new OptionSet() );

  private final INotifierStringListBasicEdit nonExistingTagIds = new NotifierStringListBasicEditWrapper(
      new SortedStringLinkedBundleList( TsCollectionsUtils.DEFAULT_BUNDLE_CAPACITY, false ) );

  // boolean minSceneDurationWarningDisabled = false;
  // boolean maxChildScenesCountWarningDisabled = false;

  /**
   * Конструктор.
   */
  public EpVerifyCfg() {
    eventer = new GenericChangeEventer( this );
    params.addCollectionChangeListener( collectionChangeListener );
    nonExistingTagIds.addCollectionChangeListener( collectionChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepableEntity
  //

  private static final String KW_NON_EXISTING_TAG_IDS = "nonExistingTagIds"; //$NON-NLS-1$
  private static final String KW_PARAMS               = "params";            //$NON-NLS-1$

  @Override
  public void write( IStrioWriter aSw ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.incNewLine();
    StrioUtils.writeCollection( aSw, KW_NON_EXISTING_TAG_IDS, nonExistingTagIds, StringKeeper.KEEPER, true );
    aSw.writeEol();
    StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
    OptionSetKeeper.KEEPER_INDENTED.write( aSw, params );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  public void read( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    nonExistingTagIds.setAll( StrioUtils.readCollection( aSr, KW_NON_EXISTING_TAG_IDS, StringKeeper.KEEPER ) );
    StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
    params.setAll( OptionSetKeeper.KEEPER.read( aSr ) );
    aSr.ensureChar( CHAR_SET_END );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpVerifyCfg
  //

  @Override
  public IStringListBasicEdit nonExistingTagIds() {
    return nonExistingTagIds;
  }

}
