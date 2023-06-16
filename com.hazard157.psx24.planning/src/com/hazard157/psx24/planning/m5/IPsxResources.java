package com.hazard157.psx24.planning.m5;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  // String STR_N_M5M_PLEP = "План";
  // String STR_D_M5M_PLEP = "Планируемый эпизод";
  // String STR_N_PLEP_NAME = "Название";
  // String STR_D_PLEP_NAME = "Название эпизода";
  // String STR_N_PLEP_DESCRIPTION = "Описание";
  // String STR_D_PLEP_DESCRIPTION = "Описание эпизода";
  // String STR_N_PLEP_PLACE = "Место";
  // String STR_D_PLEP_PLACE = "Планируемое место эпизода";
  //
  // String STR_N_COPY_PLEP = "Копировать";
  // String STR_D_COPY_PLEP = "Скопировать план с изменением идентификатора";
  //
  // String STR_N_TM_BY_PLACE = "Места";
  // String STR_D_TM_BY_PLACE = "Группировка по планируемым местам";

  String STR_N_M5M_STIR           = "Движение";
  String STR_D_M5M_STIR           = "Однотипные движения или смена позы, элемент плана";
  String STR_N_STIR_SEQ_NO        = "№";
  String STR_D_STIR_SEQ_NO        = "Порялковый номер в планируемом эпизоде";
  String STR_N_STIR_START         = "Начало";
  String STR_D_STIR_START         = "Время начала движения с момента начала эпизода";
  String STR_STIR_NUM_VISUMPLES   = "Nvis";
  String STR_STIR_NUM_VISUMPLES_D = "Количество иллюстраций";
  String STR_N_STIR_NAME          = "Название";
  String STR_D_STIR_NAME          = "Название движения";
  String STR_N_STIR_DESCRIPTION   = "Описание";
  String STR_D_STIR_DESCRIPTION   = "Описание движения";
  String STR_N_STIR_DURATION      = "Время";
  String STR_D_STIR_DURATION      = "Длительнось движения в секундах";
  String FMT_MSG_STIR_SUMMARY     = "Длительность: %s, всего движений: %d";

  String STR_N_M5M_TRACK       = "Трек";
  String STR_D_M5M_TRACK       = "Трек - часть песни";
  String STR_N_TRACK_SEQ_NO    = "№";
  String STR_D_TRACK_SEQ_NO    = "Порядковый номер в планируемом эпизоде";
  String STR_N_TRACK_SONG_ID   = "Песня";
  String STR_D_TRACK_SONG_ID   = "Идентификатор песни";
  String STR_N_TRACK_START     = "Начало";
  String STR_D_TRACK_START     = "Время начала трека с момента начала эпизода";
  String STR_N_TRACK_NAME      = "Название";
  String STR_D_TRACK_NAME      = "Название трека (песни)";
  String STR_N_TRACK_INTERVAL  = "Интервал";
  String STR_D_TRACK_INTERVAL  = "Интервал внутри песни";
  String STR_N_TRACK_DURATION  = "Время";
  String STR_D_TRACK_DURATION  = "Длительнось трека в секундах";
  String FMT_MSG_TRACK_SUMMARY = "Длительность: %s, всего треков: %d";
  String STR_N_UNKNOWN_SONG    = "<<неизвестная песня>>";
  String FMT_UNKNOWN_SONG      = "Нет песни с идентификатором '%s'";
  String FMT_NO_SONG_FILE      = "Нет файла песни %s";

}
