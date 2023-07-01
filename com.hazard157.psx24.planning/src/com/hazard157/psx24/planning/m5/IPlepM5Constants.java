package com.hazard157.psx24.planning.m5;

import static com.hazard157.psx.common.IPsxHardConstants.*;

/**
 * Planned episodes M5 modeling constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPlepM5Constants {

  String MID_STIR          = PSX_ID + ".Stir"; //$NON-NLS-1$
  String FID_NUM_VISUMPLES = "numVisumples";   //$NON-NLS-1$

  String MID_TRACK    = PSX_ID + ".Track"; //$NON-NLS-1$
  String FID_SONG_ID  = "songId";          //$NON-NLS-1$
  String FID_INTERVAL = "interval";        //$NON-NLS-1$

  String MID_PLEP        = PSX_ID + ".Plep"; //$NON-NLS-1$
  String FID_PLACE       = "Place";          //$NON-NLS-1$
  String FID_STIRS_COUNT = "StirsCount";     //$NON-NLS-1$
  String FID_PREP_STEPS  = "PrepSteps";      //$NON-NLS-1$

}
