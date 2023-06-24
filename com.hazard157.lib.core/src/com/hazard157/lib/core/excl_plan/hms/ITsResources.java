package com.hazard157.lib.core.excl_plan.hms;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link EHmsPart}
   */
  String STR_N_HMS_HH = Messages.getString( "ITsResources.STR_N_HMS_HH" ); //$NON-NLS-1$
  String STR_D_HMS_HH = Messages.getString( "ITsResources.STR_D_HMS_HH" ); //$NON-NLS-1$
  String STR_N_HMS_MM = Messages.getString( "ITsResources.STR_N_HMS_MM" ); //$NON-NLS-1$
  String STR_D_HMS_MM = Messages.getString( "ITsResources.STR_D_HMS_MM" ); //$NON-NLS-1$
  String STR_N_HMS_SS = Messages.getString( "ITsResources.STR_N_HMS_SS" ); //$NON-NLS-1$
  String STR_D_HMS_SS = Messages.getString( "ITsResources.STR_D_HMS_SS" ); //$NON-NLS-1$

  /**
   * {@link HmsUtils}
   */
  String FMT_ERR_SECS_IS_NEGATIVE     = Messages.getString( "ITsResources.FMT_ERR_SECS_IS_NEGATIVE" );     //$NON-NLS-1$
  String FMT_ERR_SECS_IS_OVER_MAX     = Messages.getString( "ITsResources.FMT_ERR_SECS_IS_OVER_MAX" );     //$NON-NLS-1$
  String FMT_ERR_DURATION_IS_NEGATIVE = Messages.getString( "ITsResources.FMT_ERR_DURATION_IS_NEGATIVE" ); //$NON-NLS-1$
  String FMT_WARN_DURATION_IS_ZERO    = Messages.getString( "ITsResources.FMT_WARN_DURATION_IS_ZERO" );    //$NON-NLS-1$
  String FMT_ERR_DURATION_IS_OVER_MAX = Messages.getString( "ITsResources.FMT_ERR_DURATION_IS_OVER_MAX" ); //$NON-NLS-1$
  String MSG_ERR_INV_MM_SS_FORMAT     = Messages.getString( "ITsResources.MSG_ERR_INV_MM_SS_FORMAT" );     //$NON-NLS-1$
  String MSG_ERR_INV_HH_MM_SS_FORMAT  = Messages.getString( "ITsResources.MSG_ERR_INV_HH_MM_SS_FORMAT" );  //$NON-NLS-1$

  /**
   * {@link HmsValue}
   */
  String FMT_ERR_MIN_VAL_LT_0   = Messages.getString( "ITsResources.FMT_ERR_MIN_VAL_LT_0" );   //$NON-NLS-1$
  String FMT_ERR_MAX_VAL_LT_0   = Messages.getString( "ITsResources.FMT_ERR_MAX_VAL_LT_0" );   //$NON-NLS-1$
  String FMT_ERR_MIN_VAL_GT_MAX = Messages.getString( "ITsResources.FMT_ERR_MIN_VAL_GT_MAX" ); //$NON-NLS-1$
  String FMT_ERR_MAX_VAL_GT_MAX = Messages.getString( "ITsResources.FMT_ERR_MAX_VAL_GT_MAX" ); //$NON-NLS-1$
  String FMT_ERR_MAX_LT_MIN_VAL = Messages.getString( "ITsResources.FMT_ERR_MAX_LT_MIN_VAL" ); //$NON-NLS-1$
  String FMT_ERR_VAL_LT_MIN     = Messages.getString( "ITsResources.FMT_ERR_VAL_LT_MIN" );     //$NON-NLS-1$
  String FMT_ERR_VAL_GT_MAX     = Messages.getString( "ITsResources.FMT_ERR_VAL_GT_MAX" );     //$NON-NLS-1$
  String ERR_MSG_INV_TEXT       = Messages.getString( "ITsResources.ERR_MSG_INV_TEXT" );       //$NON-NLS-1$
  String ERR_MSG_INV_TEXT_VAL   = Messages.getString( "ITsResources.ERR_MSG_INV_TEXT_VAL" );   //$NON-NLS-1$

}
