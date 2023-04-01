package com.hazard157.psx24.core.m5.trailer;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import com.hazard157.psx.proj3.trailers.*;

/**
 * Модель объектов типа {@link Trailer}.
 *
 * @author hazard157
 */
public class EpisodeTrailerM5Model
    extends TrailerM5ModelBase {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "psx4.EpisodeTrailer"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public EpisodeTrailerM5Model() {
    super( MODEL_ID );
    addFieldDefs( ID, EPISODE_ID, LOCAL_ID, DURATION, PLANNED_DURATION, NAME, DESCRIPTION );
  }

  @Override
  protected void doInit() {
    EPISODE_ID.setFlags( M5FF_HIDDEN );
  }

}
