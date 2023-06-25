package com.hazard157.psx24.core.m5.svin;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.excl_plan.secint.m5.ISecintM5Constants.*;
import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static com.hazard157.psx24.core.m5.svin.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * Модель объектов типа {@link Svin}.
 *
 * @author hazard157
 */
public class SvinM5Model
    extends M5Model<Svin>
    implements IPsxGuiContextable {

  /**
   * Поле {@link Svin#episodeId()}.
   */
  public static final M5FieldDef<Svin, String> EPISODE_ID = new PsxM5EpisodeIdFieldDef<>();

  /**
   * Поле {@link Svin#cameraId()}.
   */
  public static final M5FieldDef<Svin, String> CAM_ID = new PsxM5CameraIdFieldDef<>();

  /**
   * Поле {@link Svin#interval()}.
   */
  public static final M5FieldDef<Svin, Secint> INTERVAL = new PsxM5IntervalFieldDef<>();

  /**
   * Поле {@link Svin#frame()}.
   */
  public final IM5SingleLookupFieldDef<Svin, IFrame> FRAME = new PsxM5FrameFieldDef<>() {

    @Override
    protected void doInit() {
      addFlags( M5FF_COLUMN );
    }

    protected IFrame doGetFieldValue( Svin aEntity ) {
      IFrame frame = super.doGetFieldValue( aEntity );
      if( frame == null || frame == IFrame.NONE ) {
        IEpisode e = unitEpisodes().items().findByKey( aEntity.episodeId() );
        if( e != null ) {
          IScene bestScene = e.story().findBestSceneFor( aEntity.interval(), true );
          if( bestScene != null ) {
            frame = bestScene.frame();
          }
        }
      }
      return frame;
    }

  };

  /**
   * Поле длительности.
   */
  public static final M5AttributeFieldDef<Svin> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_DURATION, STR_D_DURATION );
          setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Svin aEntity ) {
          return avInt( aEntity.interval().duration() );
        }

        @Override
        protected String doGetFieldValueName( Svin aEntity ) {
          return HmsUtils.mmmss( aEntity.interval().duration() );
        }

      };

  /**
   * Конструктор.
   */
  public SvinM5Model() {
    super( MID_SVIN, Svin.class );
    setNameAndDescription( STR_N_M5M_SVIN, STR_D_M5M_SVIN );
    addFieldDefs(
        // TODO FRAME, //

        EPISODE_ID, INTERVAL, DURATION, CAM_ID );
    setPanelCreator( new M5DefaultPanelCreator<Svin>() {

      @Override
      protected IM5CollectionPanel<Svin> doCreateCollViewerPanel( ITsGuiContext aContext,
          IM5ItemsProvider<Svin> aItemsProvider ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUMMARY_PANE.setValue( aContext.params(), AV_TRUE );
        SvinM5Mpc mpc = new SvinM5Mpc( aContext, model(), aItemsProvider, null );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<Svin> doCreateDefaultLifecycleManager() {
    return new SvinLifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<Svin> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
