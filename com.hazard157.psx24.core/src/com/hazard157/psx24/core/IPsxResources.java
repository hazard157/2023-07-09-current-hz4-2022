package com.hazard157.psx24.core;

/**
 * Локализуемые русурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link IPsx24CoreConstants}
   */
  String STR_N_PSX_COMMON_APREF_BUNDLE        = "Приложение PRISEX";
  String STR_D_PSX_COMMON_APREF_BUNDLE        = "Общие настройки для всяе частей приложения PRISEX";
  String STR_N_APPRM_THUMBSZ_FRAMES_IN_PANELS = "Кадры в панели";
  String STR_D_APPRM_THUMBSZ_FRAMES_IN_PANELS = "Размер миниатюр, выводимых при просмотре кадров в панелях";
  String STR_N_APPRM_THUMBSZ_FRAMES_IN_MENUS  = "Кадры в меню";
  String STR_D_APPRM_THUMBSZ_FRAMES_IN_MENUS  = "Размер миниатюр, выводимых при просмотре кадров в меню";
  String STR_N_APPRM_THUMBSZ_FRAMES_IN_TREES  = "Кадры в списках";
  String STR_D_APPRM_THUMBSZ_FRAMES_IN_TREES  = "Размер миниатюр, выводимых при просмотре кадров в списках/деревьях";

  /**
   * {@link IPsxAppActions}
   */
  String ACT_T_PLAY                  = "Воспроизведение";
  String ACT_P_PLAY                  = "Воспроизведение выделенного медия с выбором параметров просмотра";
  String ACT_T_PLAY_MENU             = "Воспроизведение";
  String ACT_P_PLAY_MENU             = "Воспроизведение выделенного медия";
  String ACT_T_RUN_PROGRAM           = "Выполнить";
  String ACT_P_RUN_PROGRAM           = "Запустить программу";
  String ACT_T_NEXT                  = "Следующий";
  String ACT_P_NEXT                  = "Перейти к следующему элементу";
  String ACT_T_NEXT_NEXT             = "Промотка вперед";
  String ACT_P_NEXT_NEXT             = "Перепрыгнйть группу элеемнтов вперед";
  String ACT_T_PREV                  = "Предыдущий";
  String ACT_P_PREV                  = "Перейти к предыдущемц элементу";
  String ACT_T_PREV_PREV             = "Промотка назад";
  String ACT_P_PREV_PREV             = "Перепрыгнйть группу элеемнтов назад";
  String ACT_T_CAM_ID_MENU           = "Камеры";
  String ACT_P_CAM_ID_MENU           = "Выбор конкретной камры или всех камер эпизода";
  String STR_T_SHOW_SINGLE           = "Одиночные";
  String STR_P_SHOW_SINGLE           = "Показывать одиночные кадры";
  String ACT_T_SPOT_ON               = "Переход";
  String ACT_P_SPOT_ON               = "Перейти к место соответствующем у месту";
  String ACT_T_COPY_FRAME            = "Копировать";
  String ACT_P_COPY_FRAME            = "Скопировать файл изображения в указанную папку";
  String ACT_T_ADD_TAG_INTERVAL      = "Ярлык";
  String ACT_P_ADD_TAG_INTERVAL      = "Пометить интервал ярлыком";
  String ACT_T_EPISODE_KDENLIVE_MENU = "Kdenlive";
  String ACT_P_EPISODE_KDENLIVE_MENU = "Запуск одного из проектов kdenlive эпизода";
  String ACT_T_SHOW_TAGLINE_AS_LIST  = "Списком";
  String ACT_P_SHOW_TAGLINE_AS_LIST  = "Показать ярлыки списком";
  String ACT_T_FILTER                = "Фильтр";
  String ACT_P_FILTER                = "Фильтровать по содержанию текста в идентификаторе ярлыка";

  String ACT_T_COALESCE_GIF     = "coalesce";
  String ACT_P_COALESCE_GIF     = "Поправить оптимизированный GIF";
  String ACT_T_GIF_INFO         = "Инфо";
  String ACT_P_GIF_INFO         = "Вывести диалог информации о файле анимированного изображения";
  String ACT_T_CREATE_GIF       = "Создать GIF";
  String ACT_P_CREATE_GIF       = "(Пере)создать 5-ти секундную GIF-анимацию из кадров эпизода";
  String ACT_T_TEST_GIF         = "Проверка GIF";
  String ACT_P_TEST_GIF         = "Формирует текстовую 5-ти секундную GIF-анимацию из кадров эпизода";
  String ACT_T_DELETE_GIF       = "Удалить GIF";
  String ACT_P_DELETE_GIF       = "Удалить файл выбранного анимированный кадр";
  String ACT_T_WORK_WITH_FRAMES = "Кадры";
  String ACT_P_WORK_WITH_FRAMES = "Работа с кадрами эпизода";

  String ACT_T_INCLUDE_ONLY_USED_TAGS = "Только используемые";
  String ACT_P_INCLUDE_ONLY_USED_TAGS = "Показать только используемые ярлыки";
  String ACT_T_RECREATE_ALL_GIFS      = "Переоздать GIF-ы";
  String ACT_P_RECREATE_ALL_GIFS      = "Пересоздать все GIF-анимации";
  String ACT_T_RUN_KDENLIVE           = "Kdenlive";
  String ACT_P_RUN_KDENLIVE           = "Запустить проект Kdenlive";
  String ACT_T_SHOW_AK_BOTH           = "Все кадры";
  String ACT_P_SHOW_AK_BOTH           = "Показывать все кадры: и анимированние, и одиночные";
  String ACT_T_SHOW_AK_ANIMATED       = "Анимированные кадры";
  String ACT_P_SHOW_AK_ANIMATED       = "Показывать только анимированные кадры";
  String ACT_T_SHOW_AK_SINGLE         = "Одиночные кадры";
  String ACT_P_SHOW_AK_SINGLE         = "Показывать только одиночные кадры";
  String ACT_T_ONE_BY_ONE             = "Один-к-одному";
  String ACT_P_ONE_BY_ONE             = "Показ рово одного кадра на один Svin";
  // String ACT_T_TIMELINE_ZOOM_OUT = "Уменьшить";
  // String ACT_P_TIMELINE_ZOOM_OUT = "Уменьшить шкалу времени";
  // String ACT_T_TIMELINE_ZOOM_ORIGINAL = "Начальное";
  // String ACT_P_TIMELINE_ZOOM_ORIGINAL = "Привести шкалу времени к начальному масштабу";
  // String ACT_T_TIMELINE_ZOOM_IN = "Увеличить";
  // String ACT_P_TIMELINE_ZOOM_IN = "Увеличить шкалу времени";

}
