package com.hazard157.prisex24.m5.mingle;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.psx.proj3.mingle.IMingleConstants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.mingle.*;

/**
 * M5-model of {@link IMingle}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class MingleM5Model
    extends M5Model<IMingle> {

  // public static final M5AttributeFieldDef<IMingle> ID = new M5StdFieldDefId<>() {
  //
  // @Override
  // protected void doInit() {
  // removeFlags( M5FF_COLUMN );
  // addFlags( M5FF_HIDDEN );
  // }
  //
  // };

  public static final M5AttributeFieldDef<IMingle> NAME = new M5StdFieldDefName<>();

  public static final M5AttributeFieldDef<IMingle> DESCRIPTION = new M5StdFieldDefDescription<>();

  public static final M5AttributeFieldDef<IMingle> DATE = new M5StdFieldDefDataDef<>( OPDEF_DATE ) {

    @Override
    protected void doInit() {
      addFlags( M5FF_COLUMN );
    }

  };

  public static final M5AttributeFieldDef<IMingle> PLACE = new M5StdFieldDefDataDef<>( OPDEF_PLACE );

  /**
   * Constructor.
   */
  public MingleM5Model() {
    super( MID_MINGLE, IMingle.class );
    addFieldDefs( DATE, NAME, DESCRIPTION, PLACE );
    setPanelCreator( new M5DefaultPanelCreator<IMingle>() {

      @Override
      protected IM5CollectionPanel<IMingle> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IMingle> aItemsProvider, IM5LifecycleManager<IMingle> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<IMingle> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        mpc.treeModeManager().addTreeMode( TreeMakers.TMI_BY_YEAR );
        mpc.treeModeManager().addTreeMode( TreeMakers.TMI_BY_MONTH );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IMingle> doCreateDefaultLifecycleManager() {
    IUnitMingles master = tsContext().get( IUnitMingles.class );
    TsInternalErrorRtException.checkNull( master );
    return new MingleM5LifecycleManager( this, master );
  }

  @Override
  protected IM5LifecycleManager<IMingle> doCreateLifecycleManager( Object aMaster ) {
    return new MingleM5LifecycleManager( this, IUnitMingles.class.cast( aMaster ) );
  }

}
