package com.hazard157.prisex24.m5.svin;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;

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
   * Constructor.
   */
  public SvinM5Model() {
    super( MID_SVIN, Svin.class );
    // TODO Auto-generated constructor stub
  }

}
