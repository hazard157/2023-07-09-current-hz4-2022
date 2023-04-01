package com.hazard157.psx24.core.m5.srcvids;

import static com.hazard157.psx24.core.m5.srcvids.IPsxResources.*;
import static com.hazard157.psx24.core.m5.srcvids.SourceVideoM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * Менеджер ЖЦ {@link ISourceVideo} для правки и просмтора исходников одного эпизода.
 *
 * @author hazard157
 */
public class EpisodeSourceVideoLifecycleManager
    extends M5LifecycleManager<ISourceVideo, IUnitSourceVideos> {

  private final IEpisode episode;

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model} - модель
   * @param aEpisode {@link IEpisode} - эпизод
   * @param aMaster {@link IUnitSourceVideos} - менеджер исходных видео
   * @throws TsNullArgumentRtException aModel = null
   */
  public EpisodeSourceVideoLifecycleManager( IM5Model<ISourceVideo> aModel, IEpisode aEpisode,
      IUnitSourceVideos aMaster ) {
    super( aModel, false, true, false, true, aMaster );
    episode = aEpisode;
  }

  private static SourceVideoInfo makeSourceVideoInfo( IM5Bunch<ISourceVideo> aValues ) {
    int duration = DURATION.getFieldValue( aValues ).asInt();
    String location = LOCATION.getFieldValue( aValues ).asString();
    String description = DESCRIPTION.getFieldValue( aValues ).asString();
    // IFrame frame = FRAME.getFieldValue( aValues );
    IFrame frame = aValues.getAs( PsxM5FrameFieldDef.FID_FRAME, IFrame.class );
    return new SourceVideoInfo( duration, location, description, frame );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISourceVideo> aValues ) {
    String episodeId = EPISODE_ID.getFieldValue( aValues );
    if( !episodeId.equals( aValues.originalEntity().episodeId() ) ) {
      return ValidationResult.error( MSG_ERR_CANT_CHANGE_EPISODE_ID );
    }
    String camId = CAM_ID.getFieldValue( aValues );
    if( !camId.equals( aValues.originalEntity().cameraId() ) ) {
      return ValidationResult.error( MSG_ERR_CANT_CHANGE_CAMERA_ID );
    }
    String id = SourceVideoUtils.createSourceVideoId( episodeId, camId );
    SourceVideoInfo svInfo = makeSourceVideoInfo( aValues );
    return master().canEditItem( aValues.originalEntity().id(), id, svInfo );
  }

  @Override
  protected ISourceVideo doEdit( IM5Bunch<ISourceVideo> aValues ) {
    String episodeId = EPISODE_ID.getFieldValue( aValues );
    String camId = CAM_ID.getFieldValue( aValues );
    String id = SourceVideoUtils.createSourceVideoId( episodeId, camId );
    SourceVideoInfo svInfo = makeSourceVideoInfo( aValues );
    return master().editItem( aValues.originalEntity().id(), id, svInfo );
  }

  @Override
  protected IList<ISourceVideo> doListEntities() {
    if( episode == null ) {
      return IList.EMPTY;
    }
    return master().episodeSourceVideos( episode.id() ).values();
  }

}
