package com.hazard157.psx.proj3.cameras;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IPsxResources {

  /**
   * {@link Camera}
   */
  String STR_N_M3_CAMERA              = "Камера";
  String STR_D_M3_CAMERA              = "Камера, используемая для съемок";
  String STR_N_CAM_ID                 = "Идентификатор";
  String STR_D_CAM_ID                 = "Идентификатор камеры";
  String STR_N_CAM_NAME               = "Название";
  String STR_D_CAM_NAME               = "Название камеры";
  String STR_N_CAM_DESCRIPTION        = "Описание";
  String STR_D_CAM_DESCRIPTION        = "Описание камеры";
  String STR_N_CAM_IS_STILL_AVAILABLE = "Доступен?";
  String STR_D_CAM_IS_STILL_AVAILABLE = "Признак, что камера еще доступна для съемок";

  /**
   * {@link ECameraKind}
   */
  String STR_N_CK_GENERIC = "Общий";
  String STR_D_CK_GENERIC = "Устроиство, снимающее видео, без уточнения вида";
  String STR_N_CK_VHS     = "VHS";
  String STR_D_CK_VHS     = "Аналоговая VHS видеокамера";
  String STR_N_CK_DV      = "DV";
  String STR_D_CK_DV      = "Цифровая DV видекамера";
  String STR_N_CK_WEB     = "Web";
  String STR_D_CK_WEB     = "Web-камера, подключенная к компьютеру";
  String STR_N_CK_FOTO    = "Фото";
  String STR_D_CK_FOTO    = "Фотоаппарат в режиме видеосъемки";
  String STR_N_CK_PHONE   = "Смартфон";
  String STR_D_CK_PHONE   = "Смартфон или планшет";

}
