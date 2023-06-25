package com.hazard157.psx24.core.m5.srcvids;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.excl_plan.secint.gui.ISecintM5Constants.*;
import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static com.hazard157.psx24.core.m5.srcvids.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.m5.std.*;
import com.hazard157.psx24.core.valeds.frames.*;

/**
 * Модель объектов типа {@link ISourceVideo}.
 *
 * @author hazard157
 */
public class SourceVideoM5Model
    extends M5Model<ISourceVideo> {

  /**
   * Атрибут {@link ISourceVideo#id()}.
   */
  public static final M5AttributeFieldDef<ISourceVideo> ID = new M5AttributeFieldDef<>( FID_ID, DDEF_IDPATH ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_SV_ID, STR_D_SV_ID );
      setFlags( M5FF_INVARIANT | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( ISourceVideo aEntity ) {
      return avStr( aEntity.id() );
    }

  };

  /**
   * Поле {@link ISourceVideo#episodeId()}.
   */
  public static final M5SingleLookupFieldDef<ISourceVideo, String> EPISODE_ID = new PsxM5EpisodeIdFieldDef<>();

  /**
   * Поле {@link ISourceVideo#cameraId()}.
   */
  public static final M5SingleLookupFieldDef<ISourceVideo, String> CAM_ID = new PsxM5CameraIdFieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( flags() | M5FF_COLUMN );
    }
  };

  /**
   * Атрибут {@link ISourceVideo#location()}.
   */
  public static final M5AttributeFieldDef<ISourceVideo> LOCATION =
      new M5AttributeFieldDef<>( FID_LOCATION, DDEF_STRING ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_SV_LOCATION, STR_D_SV_LOCATION );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ISourceVideo aEntity ) {
          return avStr( aEntity.location() );
        }

      };

  /**
   * Атрибут {@link ISourceVideo#description()}.
   */
  public static final M5AttributeFieldDef<ISourceVideo> DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DDEF_DESCRIPTION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_SV_DESCRIPTION, STR_D_SV_DESCRIPTION );
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ISourceVideo aEntity ) {
          return avStr( aEntity.description() );
        }

      };

  /**
   * Атрибут {@link ISourceVideo#description()}.
   */
  public static final M5AttributeFieldDef<ISourceVideo> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_SV_DURATION, STR_D_SV_DURATION );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ISourceVideo aEntity ) {
          return avInt( aEntity.duration() );
        }

        @Override
        protected String doGetFieldValueName( ISourceVideo aEntity ) {
          return HmsUtils.mmss( aEntity.duration() );
        }

      };

  /**
   * Поле {@link ISourceVideo#frame()}.
   */
  public final PsxM5FrameFieldDef<ISourceVideo> FRAME = new PsxM5FrameFieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      addFlags( M5FF_COLUMN );
      removeFlags( M5FF_DETAIL );
      params().setValueIfNull( M5_OPDEF_COLUMN_USE_THUMB.id(), AV_TRUE );
    }
  };

  /**
   * Конструктор.
   */
  public SourceVideoM5Model() {
    super( MID_SOURCE_VIDEO, ISourceVideo.class );
    setNameAndDescription( STR_N_M5M_SOURCE_VIDEO, STR_N_M5M_SOURCE_VIDEO );
    addFieldDefs( FRAME, ID, CAM_ID, DURATION, EPISODE_ID, LOCATION, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<ISourceVideo>() {

      @Override
      protected IM5CollectionPanel<ISourceVideo> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISourceVideo> aItemsProvider, IM5LifecycleManager<ISourceVideo> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        aContext.params().setValueIfNull( OPDEF_DETAILS_PANE_PLACE.id(), avValobj( EBorderLayoutPlacement.SOUTH ) );
        // if( !aContext.params().hasValue( INITIAL_VIEWER_DETAILS_RATIO ) ) {
        // INITIAL_VIEWER_DETAILS_RATIO.setValue( aContext.params(), avInt( 85 ) );
        // }
        MultiPaneComponentModown<ISourceVideo> mpc =
            new PanelUnitSourceVideos( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

      @Override
      protected IM5EntityPanel<ISourceVideo> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<ISourceVideo> aLifecycleManager ) {
        M5EntityPanelWithValedsController<ISourceVideo> controller = new M5EntityPanelWithValedsController<>() {

          @Override
          public void afterSetValues( IM5Bunch<ISourceVideo> aValues ) {
            String episodeId = EPISODE_ID.getFieldValue( aValues );
            Svin svin = new Svin( episodeId, Secint.MAXIMUM );
            FrameSelectionCriteria criteria = new FrameSelectionCriteria( svin, EAnimationKind.SINGLE, true );
            ValedFrameEditor vfe = (ValedFrameEditor)editors().getByKey( PsxM5FrameFieldDef.FID_FRAME );
            vfe.setSelectionCriteria( criteria );
            IFrame frame = FRAME.getFieldValue( aValues );
            vfe.setValue( frame );
          }
        };
        M5EntityPanelWithValeds<ISourceVideo> p = new M5EntityPanelWithValeds<>( aContext, model(), false, controller );
        p.setLifecycleManager( aLifecycleManager );
        return p;
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<ISourceVideo> doCreateDefaultLifecycleManager() {
    IUnitSourceVideos unitSourceVideos = tsContext().get( IUnitSourceVideos.class );
    return new SourceVideoLifecycleManager( this, unitSourceVideos );
  }

  @Override
  protected IM5LifecycleManager<ISourceVideo> doCreateLifecycleManager( Object aMaster ) {
    return new SourceVideoLifecycleManager( this, IUnitSourceVideos.class.cast( aMaster ) );
  }

}
