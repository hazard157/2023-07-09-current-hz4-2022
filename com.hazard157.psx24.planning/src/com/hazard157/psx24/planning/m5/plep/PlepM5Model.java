package com.hazard157.psx24.planning.m5.plep;

import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static com.hazard157.psx24.planning.m5.plep.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Модель сущностей типа {@link IPlep}.
 *
 * @author hazard157
 */
public class PlepM5Model
    extends M5Model<IPlep> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = PSX_ID_PREFIX + "Plep"; //$NON-NLS-1$

  /**
   * ID of field {@link #PLACE}.
   */
  public static final String FID_PLACE = "Place"; //$NON-NLS-1$

  /**
   * ID of field {@link #STIRS_COUNT}.
   */
  public static final String FID_STIRS_COUNT = "StirsCount"; //$NON-NLS-1$

  /**
   * ID of field {@link #PREP_STEPS}.
   */
  public static final String FID_PREP_STEPS = "PrepSteps"; //$NON-NLS-1$

  /**
   * Field {@link IPlep#id()}
   */
  public static final M5AttributeFieldDef<IPlep> ID = new M5StdFieldDefId<>();

  /**
   * Field shows number of STIRs in {@link IPlep#stirs()}
   */
  public static final M5AttributeFieldDef<IPlep> STIRS_COUNT =
      new M5AttributeFieldDef<>( FID_STIRS_COUNT, EAtomicType.INTEGER, //
          TSID_NAME, STR_PLEP_STIRS_COUNT, //
          TSID_DESCRIPTION, STR_PLEP_STIRS_COUNT_D, //
          M5_OPDEF_COLUMN_ALIGN, avValobj( EHorAlignment.CENTER ), //
          TSID_DEFAULT_VALUE, AV_0 //
      ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_COLUMN );
        }

        protected IAtomicValue doGetFieldValue( IPlep aEntity ) {
          return avInt( aEntity.stirs().size() );
        }

      };

  /**
   * Field {@link IPlep#nmName()}
   */
  public static final M5AttributeFieldDef<IPlep> NAME = new M5StdFieldDefName<>();

  /**
   * Field {@link IPlep#description()}
   */
  public static final M5AttributeFieldDef<IPlep> DESCRIPTION = new M5StdFieldDefDescription<>() {

    @Override
    protected void doInit() {
      super.doInit();
      OPDEF_VERTICAL_SPAN.setValue( params(), avInt( 8 ) );
      OPDEF_IS_HEIGHT_FIXED.setValue( params(), AV_FALSE );
    }

  };

  /**
   * Field {@link PlepInfo#place()}.
   */
  public static final M5AttributeFieldDef<IPlep> PLACE = new M5AttributeFieldDef<>( FID_PLACE, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL );
      setNameAndDescription( STR_N_PLEP_PLACE, STR_D_PLEP_PLACE );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IPlep aEntity ) {
      return avStr( aEntity.info().place() );
    }
  };

  /**
   * Field {@link IPlep#preparationSteps()}
   */
  public final IM5MultiModownFieldDef<IPlep, String> PREP_STEPS =
      new M5MultiModownFieldDef<>( FID_PREP_STEPS, StringM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_DETAIL );
          setNameAndDescription( STR_PLEP_PREP_STEPS, STR_PLEP_PREP_STEPS_D );
          IValedControlConstants.OPDEF_NO_FIELD_LABEL.setValue( params(), AV_TRUE );
        }

        protected IList<String> doGetFieldValue( IPlep aEntity ) {
          return aEntity.preparationSteps();
        }

      };

  /**
   * Constructor.
   */
  public PlepM5Model() {
    super( MODEL_ID, IPlep.class );
    addFieldDefs( ID, STIRS_COUNT, NAME, PLACE, DESCRIPTION, PREP_STEPS );
    setPanelCreator( new M5DefaultPanelCreator<IPlep>() {

      @Override
      protected IM5CollectionPanel<IPlep> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IPlep> aItemsProvider, IM5LifecycleManager<IPlep> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_REFRESH.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_HIDE_PANES.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<IPlep> mpc = new PlepMpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );

  }

  @Override
  protected IM5LifecycleManager<IPlep> doCreateDefaultLifecycleManager() {
    IUnitPleps unitPleps = tsContext().get( IUnitPleps.class );
    return new PlepLifecycleManager( this, unitPleps );
  }

  @Override
  protected IM5LifecycleManager<IPlep> doCreateLifecycleManager( Object aMaster ) {
    return new PlepLifecycleManager( this, IUnitPleps.class.cast( aMaster ) );
  }

}
