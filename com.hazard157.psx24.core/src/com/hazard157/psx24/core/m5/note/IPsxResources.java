package com.hazard157.psx24.core.m5.note;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link MarkNoteM5Model}
   */
  String STR_N_FIELD_START    = "Начало";
  String STR_D_FIELD_START    = "Момент времени начала главы";
  String STR_N_FIELD_DURATION = "Длительность";
  String STR_D_FIELD_DURATION = "Продолжительность главы главы";
  String STR_N_FIELD_NOTE     = "Заметка";
  String STR_D_FIELD_NOTE     = "Текст заметки";
  String STR_N_FIELD_INTERVAL = "Интервал";
  String STR_D_FIELD_INTERVAL = "Интервал главы";

  /**
   * {@link MarkNoteLifecycleManager}
   */
  String MSG_ERR_EMPTY_NOTE          = "Текст заметки должен быть задан";
  String FMT_ERR_SECINT_OUT_OF_RANGE = "Интервал заметки %s выходит за допустимые пределы %s";
  String FMT_ERR_ALREADY_INTERVAL    = "Точно на  интервале %s уже есть пометка";
  String FMT_ERR_NO_INTERVAL         = "Нет пометки с интервалом %s";
  String FMT_WARN_NOTES_INTERSECT    = "Интервал %s пересекается с уже существующими";

}
