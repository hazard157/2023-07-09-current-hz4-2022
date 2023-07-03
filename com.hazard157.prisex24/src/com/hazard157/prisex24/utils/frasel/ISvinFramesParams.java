package com.hazard157.prisex24.utils.frasel;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secstep.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Determines parameters of the algorithm making list of frames from the SVIN.
 * <p>
 * Selection is made according to the rules:
 * <ul>
 * <li>{@link #animationKind()} - determines which kind of frames will be processed further;</li>
 * <li>{@link #isOnlySvinCams()} - if set to <code>true</code> filters out non {@link Svin#cameraId()} frames ;</li>
 * <li>{@link #cameraIds()} determines frames of cameras to include, works if {@link #isOnlySvinCams()} =
 * <code>false</code>;</li>
 * <li>{@link #secondsStep()} - determines how still frames will be bypassed;</li>
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
public interface ISvinFramesParams
    extends IGenericChangeEventCapable, IAnimationKindable, ISecondsSteppable {

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
   * Determines which still frames to bypass.
   *
   * @return {@link ESecondsStep} - the frames "density" in time
   */
  ESecondsStep secondsStep();

  /**
   * Sets {@link #secondsStep()}.
   *
   * @param aSecondsStep {@link ESecondsStep} - the frames "density" in time
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setSecondsStep( ESecondsStep aSecondsStep );

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
   * Sets all parameters at once.
   * <p>
   * Note: any argument may have value <code>null</code> indication that corresponding parameters would not be changed.
   *
   * @param aAnimationKind {@link EAnimationKind} - accepted images kind or <code>null</code>
   * @param aOnlySvinCams {@link Boolean} - <code>true</code> only SVIN camera frames, or <code>null</code>
   * @param aCameraIds {@link IStringList} - the camera IDs or an empty list for any camera or <code>null</code>
   * @param aSecondsStep {@link ESecondsStep} - {@link ESecondsStep} - the frames "density" in time
   * @param aFramesPerSvin {@link EFramesPerSvin} - frames per SVIN or <code>null</code>
   */
  void setParams( EAnimationKind aAnimationKind, Boolean aOnlySvinCams, IStringList aCameraIds,
      ESecondsStep aSecondsStep, EFramesPerSvin aFramesPerSvin );

  /**
   * Sets parameters at once .
   * <p>
   * Sets {@link #isOnlySvinCams()} = <code>false</code> and {@link #cameraIds()} to empty list.
   * <p>
   * Note: any argument may have value <code>null</code> indication that corresponding parameters would not be changed.
   *
   * @param aAnimationKind {@link EAnimationKind} - accepted images kind or <code>null</code>
   * @param aSecondsStep {@link ESecondsStep} - {@link ESecondsStep} - the frames "density" in time
   * @param aFramesPerSvin {@link EFramesPerSvin} - frames per SVIN or <code>null</code>
   */
  default void setParams( EAnimationKind aAnimationKind, ESecondsStep aSecondsStep, EFramesPerSvin aFramesPerSvin ) {
    setParams( aAnimationKind, Boolean.FALSE, IStringList.EMPTY, aSecondsStep, aFramesPerSvin );
  }

  /**
   * Sets all parameters at once from the source parameres holde..
   *
   * @param aSource {@link ISvinFramesParams} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setParams( ISvinFramesParams aSource );

}
