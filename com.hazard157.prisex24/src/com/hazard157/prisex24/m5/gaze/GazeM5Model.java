package com.hazard157.prisex24.m5.gaze;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.psx.proj3.gaze.IGazeConstants.*;
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

import com.hazard157.psx.proj3.gaze.*;

/**
 * M5-model of {@link IGaze}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class GazeM5Model
    extends M5Model<IGaze> {

  // public static final M5AttributeFieldDef<IGaze> ID = new M5StdFieldDefId<>() {
  //
  // @Override
  // protected void doInit() {
  // removeFlags( M5FF_COLUMN );
  // addFlags( M5FF_HIDDEN );
  // }
  //
  // };

  public static final M5AttributeFieldDef<IGaze> NAME = new M5StdFieldDefName<>();

  public static final M5AttributeFieldDef<IGaze> DESCRIPTION = new M5StdFieldDefDescription<>();

  public static final M5AttributeFieldDef<IGaze> DATE = new M5StdFieldDefDataDef<>( OPDEF_DATE ) {

    @Override
    protected void doInit() {
      addFlags( M5FF_COLUMN );
    }

  };

  public static final M5AttributeFieldDef<IGaze> RATING = new M5StdFieldDefDataDef<>( OPDEF_RATING ) {

    @Override
    protected void doInit() {
      addFlags( M5FF_COLUMN );
    }

    protected String doGetFieldValueName( IGaze aEntity ) {
      return aEntity.rating().gaugeString();
    }

  };

  public static final M5AttributeFieldDef<IGaze> PLACE = new M5StdFieldDefDataDef<>( OPDEF_PLACE );

  /**
   * Constructor.
   */
  public GazeM5Model() {
    super( MID_GAZE, IGaze.class );
    addFieldDefs( DATE, RATING, NAME, DESCRIPTION, PLACE );
    setPanelCreator( new M5DefaultPanelCreator<IGaze>() {

      @Override
      protected IM5CollectionPanel<IGaze> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IGaze> aItemsProvider, IM5LifecycleManager<IGaze> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<IGaze> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        mpc.treeModeManager().addTreeMode( TreeMakers.TMI_BY_RATING );
        mpc.treeModeManager().addTreeMode( TreeMakers.TMI_BY_YEAR );
        mpc.treeModeManager().addTreeMode( TreeMakers.TMI_BY_MONTH );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IGaze> doCreateDefaultLifecycleManager() {
    IUnitGazes master = tsContext().get( IUnitGazes.class );
    TsInternalErrorRtException.checkNull( master );
    return new GazeM5LifecycleManager( this, master );
  }

  @Override
  protected IM5LifecycleManager<IGaze> doCreateLifecycleManager( Object aMaster ) {
    return new GazeM5LifecycleManager( this, IUnitGazes.class.cast( aMaster ) );
  }

}
