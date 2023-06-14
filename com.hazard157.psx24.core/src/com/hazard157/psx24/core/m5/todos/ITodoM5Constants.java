package com.hazard157.psx24.core.m5.todos;

import com.hazard157.psx.proj3.todos.*;

/**
 * Константы M3-моделирования {@link IUnitTodos}.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface ITodoM5Constants {

  String TODO_M3ID_PREFIX = "ru.toxsoft.common.todo.";

  String MID_TODO         = TODO_M3ID_PREFIX + "Todo";
  String MID_REMINDER     = TODO_M3ID_PREFIX + "Reminder";
  String MID_FULFIL_STAGE = TODO_M3ID_PREFIX + "FulfilStage";
  String MID_PRIORITY     = TODO_M3ID_PREFIX + "Priority";

  String FID_TODO_ID          = "TodoId";         // long, not String
  String FID_RELATED_TODO_IDS = "RelatedTodoIds";
  String FID_CREATION_TIME    = "CreationTime";
  String FID_TEXT             = "Text";
  String FID_IS_DONE          = "IsDone";
  String FID_PRIORITY         = "Priority";
  String FID_REMINDER         = "Reminder";
  String FID_FULFIL_STAGES    = "FulfilStages";
  String FID_IS_ACTIVE        = "IsActive";
  String FID_REMIND_TIMESTAMP = "RemindTimestamp";
  String FID_MESSAGE          = "Message";
  String FID_WHEN             = "When";

}
