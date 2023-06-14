package com.hazard157.prisex24.m5.todos;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.todos.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.sql.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx.proj3.todos.*;
import com.hazard157.psx.proj3.todos.impl.*;

/**
 * {@link IFulfilStage} M5 model.
 *
 * @author hazard157
 */
public class FulfilStageM5Model
    extends M5Model<IFulfilStage> {

  /**
   * {@link IFulfilStage#when()}
   */
  public static final IM5AttributeFieldDef<IFulfilStage> WHEN = new M5AttributeFieldDef<>( FID_WHEN, TIMESTAMP, //
      TSID_NAME, STR_FS_WHEN, //
      TSID_DESCRIPTION, STR_FS_WHEN_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( IFulfilStage aEntity ) {
      return avTimestamp( aEntity.when() );
    }
  };

  /**
   * {@link IFulfilStage#name()}
   */
  public static final IM5AttributeFieldDef<IFulfilStage> NAME = new M5AttributeFieldDef<>( FID_NAME, STRING, //
      TSID_NAME, STR_FS_NAME, //
      TSID_DESCRIPTION, STR_FS_NAME_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( IFulfilStage aEntity ) {
      return avStr( aEntity.name() );
    }
  };

  /**
   * {@link IFulfilStage#description()}
   */
  public static final IM5AttributeFieldDef<IFulfilStage> DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DDEF_DESCRIPTION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_FS_DESCRIPTION, STR_FS_DESCRIPTION_D );
          params().setValueIfNull( OPID_VERTICAL_SPAN, avInt( 5 ) );
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IFulfilStage aEntity ) {
          return avStr( aEntity.description() );
        }
      };

  /**
   * Constructor.
   */
  public FulfilStageM5Model() {
    super( MID_FULFIL_STAGE, IFulfilStage.class );
    setNameAndDescription( STR_M5M_FULFIL_STAGE, STR_M5M_FULFIL_STAGE_D );
    addFieldDefs( WHEN, NAME, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      @Override
      protected IM5CollectionPanel<IFulfilStage> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IFulfilStage> aItemsProvider, IM5LifecycleManager<IFulfilStage> aLifecycleManager ) {
        // TODO Auto-generated method stub
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
        OPDEF_DETAILS_PANE_PLACE.setValue( aContext.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
        MultiPaneComponentModown<IFulfilStage> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager ) {

              @Override
              protected void doAdjustEntityCreationInitialValues( IM5BunchEdit<IFulfilStage> aValues ) {
                aValues.set( FID_WHEN, avTimestamp( new Timestamp( System.currentTimeMillis() ).getTime() ) );
              }

            };
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<IFulfilStage> doCreateDefaultLifecycleManager() {
    return new M5LifecycleManager<>( this, true, true, true, false, null ) {

      @Override
      protected IFulfilStage doCreate( IM5Bunch<IFulfilStage> aValues ) {
        long when = aValues.getAsAv( FID_WHEN ).asLong();
        String name = aValues.getAsAv( FID_NAME ).asString();
        String description = aValues.getAsAv( FID_DESCRIPTION ).asString();
        return new FulfilStage( when, name, description );
      }

      @Override
      protected IFulfilStage doEdit( IM5Bunch<IFulfilStage> aValues ) {
        return create( aValues );
      }

      @Override
      protected void doRemove( IFulfilStage aEntity ) {
        // nop
      }

    };
  }

}
