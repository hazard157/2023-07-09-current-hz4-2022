package com.hazard157.psx24.planning.m5;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static com.hazard157.psx.proj3.pleps.IUnitPlepsConstants.*;
import static com.hazard157.psx24.planning.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx24.core.m5.visumple.*;

/**
 * M5-model of {@link IStir}.
 *
 * @author hazard157
 */
public class StirM5Model
    extends M5Model<IStir> {

  /**
   * Field: sequence number of the STIR in the {@link IPlep#stirs()}, starting from 1.
   */
  public static final M5AttributeFieldDef<IStir> SEQ_NO = new M5AttributeFieldDef<>( FID_SEQ_NO, DDEF_INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STIR_SEQ_NO, STR_D_STIR_SEQ_NO );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IStir aEntity ) {
      int index = aEntity.plep().stirs().indexOf( aEntity );
      return avInt( index + 1 );
    }

  };

  /**
   * Field: {@link IStir#duration()}.
   */
  public static final M5AttributeFieldDef<IStir> START = new M5AttributeFieldDef<>( FID_START, DT_VIDEO_POSITION ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STIR_START, STR_D_STIR_START );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    private int getStart( IStir aEntity ) {
      int index = aEntity.plep().stirs().indexOf( aEntity );
      if( index < 0 ) {
        return 0;
      }
      int start = 0;
      for( int i = 0; i < index; i++ ) {
        start += aEntity.plep().stirs().get( i ).duration();
      }
      return start;
    }

    @Override
    protected IAtomicValue doGetFieldValue( IStir aEntity ) {
      return avInt( getStart( aEntity ) );
    }

    @Override
    protected String doGetFieldValueName( IStir aEntity ) {
      return mmss( getStart( aEntity ) );
    }

  };

  /**
   * Field: {@link IStir#name()}.
   */
  public static final IM5AttributeFieldDef<IStir> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STIR_NAME, STR_D_STIR_NAME );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IStir aEntity ) {
      return avStr( aEntity.name() );
    }

  };

  /**
   * Field: {@link IStir#duration()}.
   */
  public static final M5AttributeFieldDef<IStir> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_STIR_DURATION, STR_D_STIR_DURATION );
          setFlags( M5FF_COLUMN );
          setDefaultValue( OP_STIR_DURATION.defaultValue() );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return avInt( aEntity.duration() );
        }

        @Override
        protected String doGetFieldValueName( IStir aEntity ) {
          return mmss( aEntity.duration() );
        }

      };

  /**
   * Field: {@link IStir#description()}.
   */
  public static final IM5AttributeFieldDef<IStir> DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DDEF_DESCRIPTION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_STIR_DESCRIPTION, STR_D_STIR_DESCRIPTION );
          setFlags( M5FF_DETAIL );
          params().setInt( OPDEF_VERTICAL_SPAN, 4 );
          params().setBool( OPDEF_IS_HEIGHT_FIXED, true );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return avStr( aEntity.description() );
        }

      };

  /**
   * Hidden field: new STIR insertion point in {@link IStir}.
   */
  public static final IM5AttributeFieldDef<IStir> INSERTION_INDEX =
      new M5AttributeFieldDef<>( OPID_STIR_INSERTION_INDEX, DDEF_INTEGER ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_HIDDEN );
          setDefaultValue( OP_STIR_INSERTION_INDEX.defaultValue() );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return AV_N1;
        }

      };

  /**
   * Field: {@link IStir#visumples()}.
   */
  public final VisumpleM5FieldDef<IStir> VISUMPLES = new VisumpleM5FieldDef<>();

  /**
   * Read-only field: numbed of VIUMPLEs in {@link IStir#visumples()}.
   */
  public final IM5AttributeFieldDef<IStir> NUM_VISUMPLES =
      new M5AttributeFieldDef<>( IPlepM5Constants.FID_NUM_VISUMPLES, INTEGER, //
          TSID_NAME, STR_STIR_NUM_VISUMPLES, //
          TSID_DESCRIPTION, STR_STIR_NUM_VISUMPLES_D, //
          M5_OPDEF_FLAGS, avInt( M5FF_READ_ONLY | M5FF_COLUMN ), //
          TSID_DEFAULT_VALUE, AV_0 //
      ) {

        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return avInt( aEntity.visumples().size() );
        }

      };

  /**
   * Constructor.
   */
  public StirM5Model() {
    super( IPlepM5Constants.MID_STIR, IStir.class );
    addFieldDefs( SEQ_NO, START, DURATION, NUM_VISUMPLES, NAME, DESCRIPTION, VISUMPLES, INSERTION_INDEX );
    setPanelCreator( new M5DefaultPanelCreator<IStir>() {

      @Override
      protected IM5CollectionPanel<IStir> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IStir> aItemsProvider, IM5LifecycleManager<IStir> aLifecycleManager ) {
        MultiPaneComponentModown<IStir> mpc = new StirMpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IStir> doCreateDefaultLifecycleManager() {
    return null;
  }

  @Override
  protected IM5LifecycleManager<IStir> doCreateLifecycleManager( Object aMaster ) {
    return new StirLifecycleManager( this, IPlep.class.cast( aMaster ) );
  }

}
