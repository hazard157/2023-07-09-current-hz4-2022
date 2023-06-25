package com.hazard157.lib.core.excl_done.stripent.impl;

import static com.hazard157.lib.core.excl_done.stripent.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.impl.*;

import com.hazard157.lib.core.excl_done.stripent.*;

/**
 * {@link IStripentManager} base implementation.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public class StripentManager<T extends IStripent>
    extends AbstractProjDataUnit
    implements IStripentManager<T> {

  private final ITsMapValidator<String, T> builtinValidator = new ITsMapValidator<>() {

    @Override
    public ValidationResult canPut( INotifierMap<String, T> aSource, String aKey, T aExistingItem, T aNewItem ) {
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemove( INotifierMap<String, T> aSource, String aKey ) {
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canAdd( INotifierMap<String, T> aSource, String aKey, T aExistingItem, T aNewItem ) {
      if( aExistingItem != null ) {
        return ValidationResult.error( FMT_ERR_ITEM_ALREADY_EXISTS, aKey );
      }
      return ValidationResult.SUCCESS;
    }
  };

  private final IGenericChangeListener itemPropsChangeListener =
      aSource -> this.items.fireItemByRefChangeEvent( aSource );

  private static final String KW_PARAMS = "Params"; //$NON-NLS-1$
  private static final String KW_ITEMS  = "Items";  //$NON-NLS-1$

  private final INotifierOptionSetEdit params = new NotifierOptionSetEditWrapper( new OptionSet() );

  private final INotifierStridablesListEdit<T> items =
      new NotifierStridablesListEditWrapper<>( new StridablesList<>() );

  private final IListReorderer<T> reorderer;

  private final IStripentCreator<T> creator;
  private final IEntityKeeper<T>    keeper;

  /**
   * Constructor.
   *
   * @param aCreator {@link IStripentCreator} - the entity instances factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException keywod is not an IDname
   */
  public StripentManager( IStripentCreator<T> aCreator ) {
    creator = TsNullArgumentRtException.checkNull( aCreator );
    keeper = new AbstractStridableParameterizedKeeper<>( creator.entityClass(), null ) {

      @Override
      protected T doCreate( String aId, IOptionSet aParams ) {
        return creator.create( aId, aParams );
      }

    };
    items.addCollectionChangeValidator( builtinValidator );
    items.addCollectionChangeListener( collectionChangeListener );
    params.addCollectionChangeListener( collectionChangeListener );
    reorderer = new ListReorderer<>( items );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjDataUnit
  //

  @Override
  protected final void doWrite( IStrioWriter aSw ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.incNewLine();
    StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
    OptionSetKeeper.KEEPER_INDENTED.write( aSw, params );
    aSw.writeEol();
    doWriteBeforeStripents( aSw );
    StrioUtils.writeCollection( aSw, KW_ITEMS, items, keeper, true );
    doWriteAfterStripents( aSw );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
    aSw.writeEol();
  }

  @Override
  protected final void doRead( IStrioReader aSr ) {
    genericChangeEventer().pauseFiring();
    aSr.ensureChar( CHAR_SET_BEGIN );
    StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
    params.setAll( OptionSetKeeper.KEEPER.read( aSr ) );
    doReadBeforeStripents( aSr );
    IList<T> readItems = StrioUtils.readCollection( aSr, KW_ITEMS, keeper );
    // clear items
    while( !items.isEmpty() ) {
      T e = items.removeByIndex( 0 );
      e.genericChangeEventer().removeListener( itemPropsChangeListener );
    }
    // add read items
    for( T e : readItems ) {
      e.genericChangeEventer().addListener( itemPropsChangeListener );
      items.add( e );
    }
    doReadAfterStripents( aSr );
    aSr.ensureChar( CHAR_SET_END );
    genericChangeEventer().resumeFiring( true );
  }

  @Override
  protected void doClear() {
    items.pauseFiring();
    genericChangeEventer().pauseFiring();
    while( !items.isEmpty() ) {
      T e = items.removeByIndex( 0 );
      e.genericChangeEventer().removeListener( itemPropsChangeListener );
    }
    items.resumeFiring( true );
    genericChangeEventer().resumeFiring( true );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform additional read operation before {@link #items} are read.
   * <p>
   * During this method generic change event generation is paused.
   *
   * @param aDr {@link IStrioReader} - reader
   */
  protected void doReadBeforeStripents( IStrioReader aDr ) {
    // nop
  }

  /**
   * Subclass may perform additional read operation after {@link #items} are read.
   * <p>
   * During this method generic change event generation is paused.
   *
   * @param aDr {@link IStrioReader} - reader
   */
  protected void doReadAfterStripents( IStrioReader aDr ) {
    // nop
  }

  /**
   * Subclass may perform additional write operation before {@link #items} are read.
   *
   * @param aDw {@link IStrioWriter} - writer
   */
  protected void doWriteBeforeStripents( IStrioWriter aDw ) {
    // nop
  }

  /**
   * Subclass may perform additional write operation after {@link #items} are read.
   *
   * @param aDw {@link IStrioWriter} - writer
   */
  protected void doWriteAfterStripents( IStrioWriter aDw ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public INotifierOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IStripentManager
  //

  @Override
  public INotifierStridablesListEdit<T> items() {
    return items;
  }

  @Override
  public IStripentCreator<T> creator() {
    return creator;
  }

  @Override
  public IListReorderer<T> reorderer() {
    return reorderer;
  }

}
