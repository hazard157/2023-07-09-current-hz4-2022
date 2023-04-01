package com.hazard157.psx.common.filesys.olds.psx24;

import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.filesys.impl.*;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  String MSG_CREATINRG_GIFS             = "Создаются GIF-анимированные кадры...";
  String FMT_ERR_NO_EPISODE_DIR_FOR_GIF = "Нет директории эпизода %s";
  String FMT_ERR_NO_CAM_DIR_FOR_GIF     = "Эпизод %s, нет директории камеры  %s";

  String FMT_CREATING_FILM_THUMB = "Создается анимированная миниатюра фильма %s";

  /**
   * {@link PfsOriginalMediaExisting}
   */
  String FMT_WARN_INV_ORIG_DATA_DIR = "Invalid subdirectory in original media directory %s";
  String FMT_ERR_INV_ORIDG_DATE     = "No event subdirectory was on date %s";

  /**
   * {@link IPfsOriginalMedia}
   */
  String STR_N_ORIGINAL_MEDIA_DATE = "Date";
  String STR_D_ORIGINAL_MEDIA_DATE = "Original event date";

}
