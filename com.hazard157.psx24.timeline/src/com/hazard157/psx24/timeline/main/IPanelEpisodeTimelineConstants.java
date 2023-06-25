package com.hazard157.psx24.timeline.main;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

import com.hazard157.common.quants.secstep.*;

/**
 * Timeline constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPanelEpisodeTimelineConstants {

  // ------------------------------------------------------------------------------------
  // FrameThumbSize
  //

  EThumbSize DEFAULT_TIMELINE_FRAME_THUMB_SIZE = EThumbSize.SZ128;

  String OPID_TIMELINE_FRAME_THUMB_SIZE = "EpisodeTimelineCanvas.FrameThumbSize"; //$NON-NLS-1$

  IDataDef OPDEF_TIMELINE_FRAME_THUMB_SIZE = DataDef.create( OPID_TIMELINE_FRAME_THUMB_SIZE, VALOBJ, //
      TSID_NAME, "", //
      TSID_DESCRIPTION, "", //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_TIMELINE_FRAME_THUMB_SIZE ) //
  );

  // ------------------------------------------------------------------------------------
  // ScaleTimeStep
  //

  ESecondsStep DEFAULT_TIMELINE_SCALE_TIME_STEP = ESecondsStep.SEC_20;

  String OPID_TIMELINE_SCALE_TIME_STEP = "EpisodeTimelineCanvas.ScaleTimeStep"; //$NON-NLS-1$

  IDataDef OPDEF_TIMELINE_SCALE_TIME_STEP = DataDef.create( OPID_TIMELINE_SCALE_TIME_STEP, VALOBJ, //
      TSID_NAME, "", //
      TSID_DESCRIPTION, "", //
      TSID_KEEPER_ID, ESecondsStep.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( DEFAULT_TIMELINE_SCALE_TIME_STEP ) //
  );

}
