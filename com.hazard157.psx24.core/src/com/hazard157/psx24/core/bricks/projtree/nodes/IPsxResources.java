package com.hazard157.psx24.core.bricks.projtree.nodes;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  String STR_N_NK_EPISODE = "Эпизод";
  String STR_D_NK_EPISODE = "Эпизод";

  String STR_N_NK_EPISODE_INFO = "Информация";
  String STR_D_NK_EPISODE_INFO = "Информация об эпизоде";
  String FMT_N_EPISODE_INFO    = "Акт: %s, Место: %s";

  String STR_N_NK_STORY = "Сюжет";
  String STR_D_NK_STORY = "Сюжет эпизода";

  String STR_N_NK_SCENE = "Сцена";
  String STR_D_NK_SCENE = "Сцена cюжета эпизода";
  String FMT_N_SCENE    = "%s - %s";

  String STR_N_NK_TAGLINE = "Теглайн";
  String STR_D_NK_TAGLINE = "Теглайн - пометка эпизода ярлыками";

  String STR_N_NK_TAGLINE_TAG = "Ярлык";
  String STR_D_NK_TAGLINE_TAG = "Пометка эпизода ярлыком";
  String STR_N_TAGLINE_TAG    = "%s - %s";

  String STR_N_NK_TAGLINE_TAG_IN = "Интервал ярлыка";
  String STR_D_NK_TAGLINE_TAG_IN = "Интервал пометки эпизода ярлыком";

  String STR_N_NK_NOTES = "Заметки";
  String STR_D_NK_NOTES = "Заметки - пометка эпизода текстом";

  String STR_N_NK_NOTES_NOTE = "Заметка";
  String STR_D_NK_NOTES_NOTE = "Пометка эпизода текстом";
  String FMT_N_NOTELINE_NOTE = "%s - %s";

  String STR_N_NK_PLANES = "DVD-планы";
  String STR_D_NK_PLANES = "Планы для полного видеоряда DVD";

  String STR_N_NK_PLANES_PLANE = "План";
  String STR_D_NK_PLANES_PLANE = "План съемки";
  String FMT_N_PLANELINE_PLANE = "%s - %s";

  String STR_N_NK_EPISODE_ILLS = "Иллюстрации";
  String STR_D_NK_EPISODE_ILLS = "Иллюстрации к эпизоду";

  String STR_N_NK_EPISODE_ILLS_FRAME = "Кадр";
  String STR_D_NK_EPISODE_ILLS_FRAME = "Один из кадров иллюстрации к эпизоду";
  String FMT_N_ILLS_FRAME            = "%s";

  String STR_N_NK_CAMERA = "Эпизода";
  String STR_D_NK_CAMERA = "Снимающая камера";

  String STR_N_NK_SOURCE_VIDEO = "Исходное видео";
  String STR_D_NK_SOURCE_VIDEO = "Исходное видео эпизода снятое одной камерой";

  String STR_N_NK_TAG = "Ярлык";
  String STR_D_NK_TAG = "Ярлык для пометки эпизодов или группа ярлыков";

}
