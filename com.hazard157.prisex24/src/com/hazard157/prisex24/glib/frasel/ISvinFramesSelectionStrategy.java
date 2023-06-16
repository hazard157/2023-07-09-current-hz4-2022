package com.hazard157.prisex24.glib.frasel;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Determines rules how to make list of frames from the SVIN.
 * <p>
 * Selection is mare by rules:
 * <ul>
 * <li>{@link #animationKind()} - determines which kind of frames will be processed further;</li>
 * <li>{@link #isOnlySvinCams()} - if set to <code>true</code> filters out non {@link Svin#cameraId()} frames ;</li>
 * <li>{@link #cameraIds()} determines frames of cameras to include, works if {@link #isOnlySvinCams()} =
 * <code>false</code>;</li>
 * <li>{@link #framesPerSvin()} - behavior depends on specified value:</li>
 * <ul>
 * <li>{@link EFramesPerSvin#SELECTED} - resulting frames remain unchanged;</li>
 * <li>{@link EFramesPerSvin#ONE_NO_MORE} - if number of frames are >1, retains only one frame, preferring the animated
 * frame, otherwise selected frames (0 or 1) will be returned;</li>
 * <li>{@link EFramesPerSvin#FORCE_ONE} - is much like {@link EFramesPerSvin#ONE_NO_MORE}, but if no frame became
 * selected, chooses one frame (violating previous rules), preferring the animated frame.</li>
 * </ul>
 * </ul>
 * Generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} on every parameter change.
 *
 * @author hazard157
 */
public interface ISvinFramesSelectionStrategy
    extends IGenericChangeEventCapable {

  /**
   * Determines which kind of frames to select - still, animated or both.
   *
   * @return {@link EAnimationKind} - accepted images kind
   */
  EAnimationKind animationKind();

  /**
   * Sets {@link #animationKind()}.
   *
   * @param aAnimationKind {@link EAnimationKind} - accepted images kind
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setAnimationKind( EAnimationKind aAnimationKind );

  /**
   * Determines if frames only with {@link Svin#cameraId()} will be selected.
   *
   * @return boolean - <code>true</code> onlySVIN camera frames will be selected, <code>false</code> - any camera
   */
  boolean isOnlySvinCams();

  /**
   * Sets {@link #isOnlySvinCams()}.
   *
   * @param aOnlySvinCams boolean - <code>true</code> only SVIN camera frames, <code>false</code> - any camera
   */
  void setOnlySvinCams( boolean aOnlySvinCams );

  /**
   * Determines which cameras to select if {@link #isOnlySvinCams()} is <code>false</code>.
   * <p>
   * An empty list indicates that any camera will be accepted.
   *
   * @return {@link IStringList} - the camera IDs or an empty list for any camera
   */
  IStringList cameraIds();

  /**
   * Sets {@link #cameraIds()}.
   *
   * @param aCameraIds {@link IStringList} - the camera IDs or an empty list for any camera
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setCameraIds( IStringList aCameraIds );

  /**
   * Determines how many frames will be selected per SVIN.
   *
   * @return {@link EFramesPerSvin} - frames per SVIN
   */
  EFramesPerSvin framesPerSvin();

  /**
   * Sets {@link #framesPerSvin()} value.
   *
   * @param aFramesPerSvin {@link EFramesPerSvin} - frames per SVIN
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setFramesPerSvin( EFramesPerSvin aFramesPerSvin );

  /**
   * Selects and returns frames for the specified SVIN.
   * <p>
   * If SVIN contains invalid data (eg non-existing episode, bad interval) then returns an empty list.
   *
   * @param aSvin {@link Svin} - the SVIN
   * @return {@link IList}&lt;{@link IFrame}&gt; - selected frames
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IFrame> selectFrames( Svin aSvin );

}
