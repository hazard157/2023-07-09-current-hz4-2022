package com.hazard157.prisex24.m5.svin;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.svin.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.m5.std.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * M5-model of {@link Svin}.
 *
 * @author hazard157
 */
public class SvinM5Model
    extends M5Model<Svin>
    implements IPsxGuiContextable {

  /**
   * Field {@link Svin#episodeId()}.
   */
  public static final M5FieldDef<Svin, String> EPISODE_ID = new PsxM5EpisodeIdFieldDef<>();

  /**
   * Field {@link Svin#cameraId()}.
   */
  public static final M5FieldDef<Svin, String> CAM_ID = new PsxM5CameraIdFieldDef<>();

  /**
   * Field {@link Svin#interval()}.
   */
  public static final M5FieldDef<Svin, Secint> INTERVAL = new PsxM5IntervalFieldDef<>();

  /**
   * Field {@link Svin#frame()}.
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
   * Field duration of interval {@link Svin#interval()}.
   */
  public static final M5AttributeFieldDef<Svin> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_DURATION, STR_DURATION_D );
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
   * Constructor.
   */
  public SvinM5Model() {
    super( MID_SVIN, Svin.class );
    setNameAndDescription( STR_M5M_SVIN, STR_M5M_SVIN_D );
    addFieldDefs( FRAME, EPISODE_ID, INTERVAL, DURATION, CAM_ID );
    // TODO Auto-generated constructor stub
  }

}
