package com.hazard157.psx.common.stuff.sc;

import java.io.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.common.utils.ftstep.*;

/**
 * Single criteria to select frames.
 *
 * @author hazard157
 */
public interface ISingleCriterion
    extends ITsFilter<IFrame> {

  /**
   * No frames selector criterion singletone.
   */
  ISingleCriterion NONE = new InternalNoneSingleCriterion();

  /**
   * Returns the valid ID oth the existing episode.
   *
   * @return String - the valid ID oth the existing episode
   */
  String episodeId();

  /**
   * Returns the ID of camera of frames to include.
   * <p>
   * An empty string means that frames for all cameras must be included in selection.
   *
   * @return String - the camera ID or an empty string
   */
  String cameraId();

  /**
   * Returns the interval of frames to be included in selection.
   * <p>
   * Selection will include frames from {@link Secint#start()} second during {@link Secint#duration()} seconds.
   *
   * @return {@link Secint} - the time interval
   */
  Secint interval();

  /**
   * Returns the kind of animated frames to be included in selection.
   *
   * @return {@link EAnimationKind} - the animation kind
   */
  EAnimationKind kind();

  /**
   * Determines if only the frames aligned on seconds boundary are included inselection.
   *
   * @return boolean - <b>true</b> - only each {@link #timeStep()} second-aligned frames are included;<br>
   *         <b>false</b> - all frames in interval will be included in selection.
   */
  boolean isOnlySecAligned();

  /**
   * Returns the second-aligned frames time step.
   * <p>
   * The return value is meaningful only when the frames aligned on seconds boundary are enabled, that is
   * {@link #isOnlySecAligned()} = <code>true</code>.
   *
   * @return {@link ESecondsStep} - time step
   */
  ESecondsStep timeStep();

}

/**
 * Internal class for {@link ITsFilter#NULL} singleton implementation.
 *
 * @author hazard157
 */
class InternalNoneSingleCriterion
    implements ISingleCriterion, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * This method guarantees that serialized {@link ITsFilter#NULL} will be read correctly.
   *
   * @return Object - always {@link ITsFilter#NULL}
   * @throws ObjectStreamException never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ISingleCriterion.NONE;
  }

  @Override
  public boolean accept( IFrame aObj ) {
    return false;
  }

  @Override
  public String episodeId() {
    return EpisodeUtils.EPISODE_ID_NONE;
  }

  @Override
  public String cameraId() {
    return IStridable.NONE_ID;
  }

  @Override
  public Secint interval() {
    return Secint.NULL;
  }

  @Override
  public EAnimationKind kind() {
    return EAnimationKind.SINGLE;
  }

  @Override
  public boolean isOnlySecAligned() {
    return false;
  }

  @Override
  public ESecondsStep timeStep() {
    return ESecondsStep.SEC_01;
  }

}
