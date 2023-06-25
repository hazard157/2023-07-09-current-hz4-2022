package com.hazard157.prisex24.m5.plane;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.excl_plan.secint.gui.ISecintM5Constants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.plane.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.lib.core.excl_plan.secint.gui.*;
import com.hazard157.lib.core.excl_plan.secint.valed.*;
import com.hazard157.prisex24.m5.std.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * M5-nodel of {@link MarkPlaneGuide}.
 *
 * @author hazard157
 */
public class MarkPlaneGuideM5Model
    extends M5Model<MarkPlaneGuide> {

  /**
   * Field {@link Secint#start() MarkPlaneGuide.in().start()}
   */
  public static final M5AttributeFieldDef<MarkPlaneGuide> START =
      new M5AttributeFieldDef<>( FID_START, DT_VIDEO_POSITION ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_COLUMN );
          setNameAndDescription( STR_FIELD_START, STR_FIELD_START_D );
        }

        @Override
        protected IAtomicValue doGetFieldValue( MarkPlaneGuide aEntity ) {
          return avInt( aEntity.in().start() );
        }

        @Override
        protected String doGetFieldValueName( MarkPlaneGuide aEntity ) {
          return HmsUtils.mmmss( aEntity.in().start() );
        }

      };

  /**
   * Field {@link Secint#duration() MarkPlaneGuide.in().duration()}
   */
  public static final M5AttributeFieldDef<MarkPlaneGuide> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_COLUMN | M5FF_HIDDEN | M5FF_READ_ONLY );
          setNameAndDescription( STR_FIELD_DURATION, STR_FIELD_DURATION_D );
        }

        @Override
        protected IAtomicValue doGetFieldValue( MarkPlaneGuide aEntity ) {
          return avInt( aEntity.in().start() );
        }

        @Override
        protected String doGetFieldValueName( MarkPlaneGuide aEntity ) {
          return HmsUtils.mmmss( aEntity.in().duration() );
        }

      };

  /**
   * Field {@link Secint#duration() MarkPlaneGuide.in().duration()}
   */
  public static final M5AttributeFieldDef<MarkPlaneGuide> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_NAME ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL | M5FF_COLUMN | M5FF_HIDDEN | M5FF_READ_ONLY );
      setNameAndDescription( STR_FIELD_NAME, STR_FIELD_NAME_D );
    }

    @Override
    protected IAtomicValue doGetFieldValue( MarkPlaneGuide aEntity ) {
      return avStr( aEntity.marker().name() );
    }

  };

  /**
   * Field {@link Secint#duration() MarkPlaneGuide.in().duration()}
   */
  public static final M5AttributeFieldDef<MarkPlaneGuide> CAMERA_ID =
      new M5AttributeFieldDef<>( FID_CAMERA_ID, DDEF_IDPATH ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_DETAIL | M5FF_COLUMN | M5FF_HIDDEN | M5FF_READ_ONLY );
          setNameAndDescription( STR_FIELD_CAMERA_ID, STR_FIELD_CAMERA_ID_D );
        }

        @Override
        protected IAtomicValue doGetFieldValue( MarkPlaneGuide aEntity ) {
          return avStr( aEntity.marker().cameraId() );
        }

      };

  /**
   * Field {@link MarkPlaneGuide#in()}
   */
  public static final M5SingleModownFieldDef<MarkPlaneGuide, Secint> INTERVAL =
      new M5SingleModownFieldDef<>( FID_INTERVAL, SecintM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_DETAIL | M5FF_HIDDEN );
          setNameAndDescription( STR_FIELD_INTERVAL, STR_FIELD_INTERVAL_D );
          setDefaultValue( Secint.MAXIMUM );
          setValedEditor( ValedSecintFactory.FACTORY_NAME );
        }

        @Override
        protected Secint doGetFieldValue( MarkPlaneGuide aEntity ) {
          return aEntity.in();
        }

      };

  /**
   * Field {@link MarkPlaneGuide#marker()}
   */
  public static final M5SingleModownFieldDef<MarkPlaneGuide, PlaneGuide> GUIDE =
      new M5SingleModownFieldDef<>( FID_GUIDE, PlaneGuideM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setFlags( 0 );
          setNameAndDescription( STR_FIELD_GUIDE, STR_FIELD_GUIDE_D );
          params().setValueIfNull( OPID_IS_WIDTH_FIXED, AV_FALSE );
          params().setValueIfNull( OPID_IS_HEIGHT_FIXED, AV_FALSE );
          params().setValueIfNull( OPID_VERTICAL_SPAN, avInt( 10 ) );
          params().setValueIfNull( OPID_NO_FIELD_LABEL, AV_TRUE );
        }

        @Override
        protected PlaneGuide doGetFieldValue( MarkPlaneGuide aEntity ) {
          return aEntity.marker();
        }

      };

  /**
   * Field {@link PlaneGuide#frame()}
   */
  public static final PsxM5FrameFieldDef<MarkPlaneGuide> FRAME = new PsxM5FrameFieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( M5FF_DETAIL | M5FF_HIDDEN | M5FF_READ_ONLY );
    }

  };

  /**
   * Конструктор.
   */
  public MarkPlaneGuideM5Model() {
    super( MID_MARK_PLANE_GUIDE, MarkPlaneGuide.class );
    addFieldDefs( INTERVAL, START, DURATION, CAMERA_ID, NAME, GUIDE, FRAME );
    setPanelCreator( new M5DefaultPanelCreator<MarkPlaneGuide>() {

      @Override
      protected IM5EntityPanel<MarkPlaneGuide> doCreateEntityDetailsPanel( ITsGuiContext aContext ) {
        return new MarkPlaneGuideEntityDetailsPanel( aContext, model() );
      }
    } );

    // TODO сделать нормальную панель правки списка (с кнопкой play)
    // TODO сделать нормальную панель редактирования
  }

  @Override
  protected IM5LifecycleManager<MarkPlaneGuide> doCreateDefaultLifecycleManager() {
    return null;
  }

  @Override
  protected IM5LifecycleManager<MarkPlaneGuide> doCreateLifecycleManager( Object aMaster ) {
    return new MarkPlaneGuideLifecycleManager( this, IMlPlaneGuide.class.cast( aMaster ) );
  }

}
