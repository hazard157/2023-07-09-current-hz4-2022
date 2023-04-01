package com.hazard157.psx24.core.m5.trailer;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  String FMT_ERR_INV_EPISODE_ID     = "Надо выбрать существующий эпизод - эпизод %s не сущствет";
  String FMT_ERR_DUP_TRAILER_ID     = "Трейлер с идентификатором %s уже существует";
  String FMT_ERR_NONSELF_EPISODE_ID = "Здесь надо вбрать только текущий эпизод %s";

  /**
   * {@link TrailerM5Model}
   */
  String STR_N_M5M_TRAILER                = "Трейлер";
  String STR_D_M5M_TRAILER                = "Описание трейлера эпизода";
  String STR_N_TR_LOCAL_ID                = "Локальный";
  String STR_D_TR_LOCAL_ID                = "Локальный идентификатор (ИД-имя) трейлера";
  String STR_N_TR_DURATION                = "Время";
  String STR_D_TR_DURATION                = "Оценочная (по набору подклипов) длительность трейлера";
  String STR_N_TR_PLANNED_DURATION        = "План";
  String STR_D_TR_PLANNED_DURATION        = "Плановая длительность трейлера";
  String MSG_WARN_TRAILER_EMPTY_NAME      = "Рекомендуется указать название трейлера";
  String MSG_ERR_INV_PLANNED_DURATION     = "Плановая длительность трейлера должна быть больше нуля";
  String MSG_WARN_SHORT_PLANNED_DURATION  = "Подозрительно малая плановая длительность трейлера";
  String STR_N_TREE_MODE_BY_EPISODE       = "По эпизодам";
  String STR_D_TREE_MODE_BY_EPISODE       = "Трейлеры по эпизодам";
  String STR_N_TREE_MODE_BY_NAME          = "По именам";
  String STR_D_TREE_MODE_BY_NAME          = "Трейлеры по сходству имен (локальных ИДов)";
  String FMT_ERR_UNKNOWN_EPISODES_TRAILER = "Трейлер %s относится к несуществующему эпизоду %s";

}
