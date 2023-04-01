package com.hazard157.psx24.core.glib.frlstviewer_ep;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link DialogShowMultiImage}
   */
  String DLG_C_SHOW_MULTI_IMAGE            = "Просмотр кадра";
  String MSG_WARN_NULL_MULTI_IMAGE_TO_SHOW = "Нечего показывать :(";

  /**
   * {@link EpisodeFramesListViewer}
   */
  String ASK_RECREATE_SEL_CAM_GIFS        = "Пересоздать GIFы камеры %s эпизода %s ?";
  String ASK_RECREATE_ALL_CAM_GIFS        = "Пересоздать все GIFы эпизода %s ?";
  String STR_N_TOOLBAR                    = "Кадры:";
  String ASK_DELETE_FRAME                 = "Удалить кадр %s ?";
  String FMT_NO_MULTI_IMAGE_FOR_FRAME     = "Не могу загрузить изображение кадра %s";
  String FMT_LOADED_MULTI_IMAGE_FOR_FRAME = "Загружено изображение для кадра %s";
  String FMT_ERR_CANT_CREATE_GIF          = "Ошибка создания GIF-файла кадра %s";
  String FMT_ERR_NOT_ONE_EP_FRAMES        =
      "В списке есть кадры более одного эпизода (%s, %s),\nНе могу ничего отобразить в этом режиме";
  String MSG_WARN_NO_FRAMES_TO_SHOW       = "Нет пригодных к отображению кадров";
  String MSG_WARN_NOT_ALL_FRAMES_FOR_GIF  = "Не все кадры существуют для 5-секундного клипа начиная с это секунды";
  String STR_N_UNSCENED_NODE              = "Кадры вне сцен сюжета";
  String STR_N_UNNOTES_NODE               = "Кадры вне интевалов с заметками";
  String STR_N_TM_BY_MINUTES              = "По минутам";
  String STR_D_TM_BY_MINUTES              = "Группировка кадров по минутам";
  String STR_N_TM_BY_20SEC                = "По 20 сек";
  String STR_D_TM_BY_20SEC                = "Группировка кадров по 20 екундным интервалам";
  String STR_N_TM_BY_SCENES               = "По сценам";
  String STR_D_TM_BY_SCENES               = "Сцены сюжета с кадрами";
  String STR_N_TM_BY_NOTES                = "По заметкам";
  String STR_D_TM_BY_NOTES                = "Группировка кадров по интервалам пометок";
  String DLG_C_FMT_SHOW_TEST_GIF          = "Проба кадра %s";

  /**
   * {@link IEpisodeFramesListViewer}
   */
  String STR_N_CAM_MENU_THUMB_SIZE     = "Размер значка камер";
  String STR_D_CAM_MENU_THUMB_SIZE     = "Размер значков в выпадающем пункте меню камер";
  String STR_N_FRAME_VIEWER_THUMB_SIZE = "Размер миниатюры кадра";
  String STR_D_FRAME_VIEWER_THUMB_SIZE = "Размер просматриваемого кадра в правой части панели выбора/просмотра кадров";
  String MSG_CREATING_GIF_FRAMES       = "Создаются кадры для анимации";
  String MSG_CREATING_GIF_IMAGE        = "Создается GIF-анимация из кадров";

}
