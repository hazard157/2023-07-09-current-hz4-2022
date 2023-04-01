package com.hazard157.psx.common.filesys.olds.psx12.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link FsFilmFilesManager}
   */
  String FMT_WARN_NO_SH_FILE     = "Не найден bash-скрипт %s";
  String FMT_CREATING_FILM_THUMB = "Создается анимированная миниатюра фильма %s";

  /**
   * {@link FsFrameManager}
   */
  String FMT_ERR_INV_FRAMES_DIR             = "Недоступна директория изображений не-секундных кадров %s";
  String FMT_ERR_INV_EPISODES_RESOURCES_DIR = "Недоступна директория ресурсов эпизодов %s";

  /**
   * {@link FsMediaPlayer}
   */
  String FMT_ERR_NO_SUCH_EPISODE                = "Нет эпизода с идентификатором '%s'";
  String FMT_ERR_NO_SOURCE_VIDEOS               = "Для эпизода '%s' не задано не одно исходное видео";
  String FMT_ERR_NO_SOURCE_VIDEO                = "Для эпизода '%s' не задано исходное видео для камеры '%s'";
  String FMT_ERR_NO_SOURCE_VIDEO_FILE           = "Для эпизода '%s' не существует файла исходное видео '%s'";
  String FMT_ERR_SOURCE_VIDEO_FILE_INACCESSABLE = "Недоступен для чтения файлисходного видео '%s'";

}
