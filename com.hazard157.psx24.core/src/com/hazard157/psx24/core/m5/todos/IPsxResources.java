package com.hazard157.psx24.core.m5.todos;

import com.hazard157.psx.proj3.todos.*;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link FulfilStageM5Model}
   */
  String STR_N_M5M_FULFIL_STAGE = "Этап";
  String STR_D_M5M_FULFIL_STAGE = "Этап выполнения задачи";
  String STR_N_FS_WHEN          = "Когда";
  String STR_D_FS_WHEN          = "Дата/время выполнения этапа";
  String STR_N_FS_NAME          = "Название";
  String STR_D_FS_NAME          = "Название этапа";
  String STR_N_FS_DESCRIPTION   = "Описание";
  String STR_D_FS_DESCRIPTION   = "Подробное описание этапа";

  /**
   * {@link PriorityM5Model}
   */
  String STR_N_M5M_PRIORITY = "Важность";
  String STR_D_M5M_PRIORITY = "Приоритет (важность) дела";

  /**
   * {@link ReminderM5Model}
   */
  String STR_N_M5M_REMINDER         = "Напоминалка";
  String STR_D_M5M_REMINDER         = "Запланированное извещение о деле";
  String STR_N_REM_IS_ACTIVE        = "Активно?";
  String STR_D_REM_IS_ACTIVE        = "Признак включения (запланированности) извещения";
  String STR_N_REM_REMIND_TIMESTAMP = "Время";
  String STR_D_REM_REMIND_TIMESTAMP = "Дата/время запланированного извещения";
  String STR_N_REM_MESSAGE          = "Сообщение";
  String STR_D_REM_MESSAGE          = "Текст напоминания";

  /**
   * {@link TodoM5ModelPanelCreator}
   */
  String TAB_T_TODO_PROPS  = "Свойства";
  String TAB_P_TODO_PROPS  = "Свойства задачи";
  String TAB_T_TODO_TODOS  = "Связано";
  String TAB_P_TODO_TODOS  = "Связанные задачи";
  String TAB_T_TODO_STAGES = "Этапы";
  String TAB_P_TODO_STAGES = "Этапы выполнения";

  /**
   * {@link ITodo}
   */
  String STR_N_M5M_TODO         = "Дела";
  String STR_D_M5M_TODO         = "Информация о запланированном деле";
  String STR_N_TD_TODO_ID       = "ИД";
  String STR_D_TD_TODO_ID       = "Идентификатор";
  String STR_N_TD_CREATION_TIME = "Создано";
  String STR_D_TD_CREATION_TIME = "Время создания";
  String STR_N_TD_TEXT          = "Текст";
  String STR_D_TD_TEXT          = "Краткое описание дела";
  String STR_N_TD_NOTE          = "Заметки";
  String STR_D_TD_NOTE          = "Пояснения к делу";
  String STR_N_TD_IS_DONE       = "Сделано?";
  String STR_D_TD_IS_DONE       = "Признак завершения (исполнения) дела";
  String STR_N_TD_PRIORITY      = "Важность";
  String STR_D_TD_PRIORITY      = "Важность (приоритет) дела";
  String STR_N_TD_RELATED_TODOS = "Связанные дела";
  String STR_D_TD_RELATED_TODOS = "Связанные с этим делом другие дела";
  String STR_N_TD_REMONDER      = "Напоминалка";
  String STR_D_TD_REMINDER      = "Параметры извещения - напоминания о деле";
  String STR_N_TD_FULFIL_STAGES = "Этапы";
  String STR_D_TD_FULFIL_STAGES = "Этапы выполнения дела";

  /**
   * {@link TodoM5ModelPanelCreator}
   */
  String STR_N_NODE_DONE       = "Выполненные";
  String STR_N_NODE_UNDONE     = "Не сделанные";
  String STR_N_TMM_BY_DONE     = "Сделано?";
  String STR_D_TMM_BY_DONE     = "Разделение по групаам выпоненных и несдаллын задач";
  String STR_N_TMM_BY_PRIORITY = "Важность";
  String STR_D_TMM_BY_PRIORITY = "Группировка дел по важности";

}
