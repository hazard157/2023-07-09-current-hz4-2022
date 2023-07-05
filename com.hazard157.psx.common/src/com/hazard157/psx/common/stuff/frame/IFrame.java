package com.hazard157.psx.common.stuff.frame;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.*;
import com.hazard157.psx.common.stuff.*;

/**
 * Frame identification of the episodes's source clip.
 * <p>
 * Note that (apart from the constant {@link #NONE}) the real value of the episode ID {@link #episodeId()} is expected.
 * Also, the actual value of {@link #cameraId()} is sometimes used even if no frame is set (ie, {@link #frameNo()} =
 * -1).
 * <p>
 * If {@link #isAnimated()} = <code>false</code>, one frame with number {@link #frameNo()} is identified. If
 * {@link #isAnimated()} = <code><code>true</code> then a GIF-animated image is used containing
 * {@link IPsxHardConstants#ANIMATED_GIF_SECS} seconds of the clip, starting at frame {@link #frameNo()} (usually frame
 * number at the same time, it is aligned on the boundary of a second).
 * <p>
 * Sorting (interface {@link Comparable}) is in ascending order {@link #episodeId()}, {@link #frameNo()},
 * {@link #cameraId()}, and finally {@link #isAnimated()}.
 *
 * @author hazard157
 */
public interface IFrame
    extends IEpisodeIdable, ICameraIdable, Comparable<IFrame> {

  /**
   * "Null", missing frame.
   */
  IFrame NONE = new InternalNoneFrame();

  /**
   * Returns the episode ID.
   *
   * @return String - episode ID or {@link IStridable#NONE_ID} for {@link #NONE}
   */
  @Override
  String episodeId();

  // /**
  // * Возвращает дату события.
  // *
  // * @return {@link LocalDate} - дата события
  // */
  // LocalDate whenDate();

  /**
   * Returns the camera ID that uniquely identifies the source clip of the episode.
   *
   * @return String - camera ID or {@link IStridable#NONE_ID}
   */
  @Override
  String cameraId();

  /**
   * Returns the frame zero-based number from the beginning of the video.
   * <p>
   * The frame rate per second of the source clip is assumed to be {@link IPsxHardConstants#FPS}).
   * <p>
   * For an animated frame, the number of the first frame of the animation is returned, and for still frames, the number
   * of the single frame.
   *
   * @return int - frame number or -1 for {@link IFrame#NONE}
   */
  int frameNo();

  /**
   * Returns the number of the second corresponding to {@link #frameNo()}.
   *
   * @return int - second number equal to {@link #frameNo()} / {@link IPsxHardConstants#FPS}
   */
  default int secNo() {
    return frameNo() / FPS;
  }

  /**
   * Returns the attribute of an animated image (mini-clip).
   *
   * @return boolean - animated frame sign
   */
  boolean isAnimated();

  /**
   * Specifies that the object describes a valid frame.
   * <p>
   * Frame validity means that all fields of the object have meaningful values, namely:
   * <ul>
   * <li>{@link #episodeId()} - is a syntactically valid episode ШВ;</li>
   * <li>{@link #cameraId()} - is a valid ID path;</li>
   * <li>{@link #frameNo()} - greater than or equal to 0.</li> *
   * </ul>
   * However, validity does not mean that such a frame actually exists. In particular, there may not be an episode with
   * such an identifier, the camera may not be defined (at least for this episode), and the frame number may be outside
   * the duration of the episode.
   *
   * @return boolean - the sign that the object describes a valid frame
   */
  boolean isDefined();

  /**
   * Determine if the frame is exactly on the border of a second.
   *
   * @return boolean - a sign that the frame corresponds to a second exactly
   */
  default boolean isSecAligned() {
    return ((frameNo() % FPS) == 0);
  }

}

class InternalNoneFrame
    implements IFrame {

  @Override
  public int compareTo( IFrame o ) {
    if( o == IFrame.NONE ) {
      return 0;
    }
    return -1;
  }

  @Override
  public String episodeId() {
    throw new TsNullObjectErrorRtException();
  }

  // @Override
  // public LocalDate whenDate() {
  // throw new TsNullObjectErrorRtException();
  // }

  @Override
  public String cameraId() {
    return IStridable.NONE_ID;
  }

  @Override
  public int frameNo() {
    return -1;
  }

  @Override
  public boolean isAnimated() {
    return false;
  }

  @Override
  public boolean isDefined() {
    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals( Object aObj ) {
    return aObj == this;
  }

  @Override
  public String toString() {
    return "NONE"; //$NON-NLS-1$
  }

}
