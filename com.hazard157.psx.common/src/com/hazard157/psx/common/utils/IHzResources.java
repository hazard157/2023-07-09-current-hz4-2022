package com.hazard157.psx.common.utils;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IHzResources {

  /**
   * {@link EpisodeUtils}
   */
  String FMT_ERR_INV_PSX_DATE = "Дата (%s) эпизода выходит за  допустимые пределы";
  String FMT_ERR_INV_EP_ID    = "Идентификатор (%s) эпизода должен иметь вид 'eYYYY_MM_DD'";
  String FMT_ERR_INV_WHEN     = "Дата эпизода '%1$tF %1$tT' выходит за допустимые пределы";

  /**
   * {@link HhMmSsFfUtils}
   */
  String MSG_ERR_INV_MM_SS_FF_FORMAT = "Неверный формат количества кадров ММ:СС.КК";

  /**
   * {@link SourceVideoUtils}
   */
  String FMT_ERR_SVID_TOO_FEW_ID_COMPS = "ИД исходного видео '%s' должен состоянить из ИДов жпизода и камеры";
  String FMT_ERR_SVID_INV_EPISODE_ID   = "ИД исходного видео '%s' содержит недопустимы ИД эпизода";

  /**
   * {@link TrailerUtils}
   */
  String FMT_ERR_TRID_NOT_2_ID_COMPS = "ИД трейлера '%s' должен состоянить из ИД-имен эпизода и локального";
  String FMT_ERR_TRID_INV_EPISODE_ID = "ИД трейлера '%s' содержит недопустимы ИД эпизода";
  String MSG_WARN_TRID_FOO_LOCAL_ID  = "Рекомендуется изменить локальный ИД трейлера";

}
