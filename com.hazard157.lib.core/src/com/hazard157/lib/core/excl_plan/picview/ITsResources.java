package com.hazard157.lib.core.excl_plan.picview;

import org.toxsoft.core.tsgui.graphics.image.*;

/**
 * Локализуемые ресурсы компоненты просмотра изображения.
 *
 * @author hazard157
 */
interface ITsResources {

  /**
   * {@link EThumbSize}
   */
  String FMT_N_THUMB_SIZE = Messages.getString( "FMT_N_THUMB_SIZE" ); //$NON-NLS-1$
  String FMT_D_THUMB_SIZE = Messages.getString( "FMT_D_THUMB_SIZE" ); //$NON-NLS-1$

  /**
   * {@link PictureViewer}.
   */
  String STR_ACT_TEXT_EXPAND_TO_FIT    = Messages.getString( "STR_ACT_TEXT_EXPAND_TO_FIT" );    //$NON-NLS-1$
  String STR_ACT_TOOLTIP_EXPAND_TO_FIT = Messages.getString( "STR_ACT_TOOLTIP_EXPAND_TO_FIT" ); //$NON-NLS-1$
  String STR_ACT_TEXT_CLEAR_IMAGE      = Messages.getString( "STR_ACT_TEXT_CLEAR_IMAGE" );      //$NON-NLS-1$
  String STR_ACT_TOOLTIP_CLEAR_IMAGE   = Messages.getString( "STR_ACT_TOOLTIP_CLEAR_IMAGE" );   //$NON-NLS-1$

}
