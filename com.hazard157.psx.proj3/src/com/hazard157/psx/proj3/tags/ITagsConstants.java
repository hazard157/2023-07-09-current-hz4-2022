package com.hazard157.psx.proj3.tags;

import static com.hazard157.psx.proj3.tags.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Tag management constants.
 *
 * @author hazard157
 */
public interface ITagsConstants {

  /**
   * Признак, что ярлык не может иметь дочерные ярлыки.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: <b>true</b> - ярлык без дальнейшего уточнеия; <b>false</b> - ярлык может иметь уточняющие дочерные ярлыки.
   * <br>
   * Значение по умолчанию: false
   */
  IDataDef IS_LEAF = DataDef.create( "IsLeaf", BOOLEAN, //$NON-NLS-1$
      TSID_DESCRIPTION, STR_D_TIP_IS_LEAF, //
      TSID_NAME, STR_N_TIP_IS_LEAF, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Признак, что это радио-группа дочерных ярлыков.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: <b>true</b> - объект можно пометить только одним из дочерных ярлыков; <b>false</b> - объекты можно помечать
   * любым количеством дочерных ярлыков.<br>
   * Значение по умолчанию: false
   */
  IDataDef IS_RADIO = DataDef.create( "IsRadio", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_TIP_IS_RADIO, //
      TSID_DESCRIPTION, STR_D_TIP_IS_RADIO, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Признак, что один из дочерных ярлыков обязательно должен быть в пометках.<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: <b>true</b> - хотя бы один из ярлыков должен быть в любой пометке; <b>false</b> - ярлыки и дочки
   * необязательны к использованию в пометках.<br>
   * Значение по умолчанию: false
   */
  IDataDef IS_MANDATORY = DataDef.create( "IsMandatory", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_TIP_IS_MANDATORY, //
      TSID_DESCRIPTION, STR_D_TIP_IS_MANDATORY, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Имя файла (путь) значка.<br>
   * Тип данных: примитивный {@link EAtomicType#STRING}<br>
   * Формат: абсолютный или относительный (к корневой директории значков приложения) путь к jpg/gif файлу. Пустая строка
   * означает, что значок не задан<br>
   * Значение по умолчанию: "" (пустая строка)
   */
  IDataDef ICON_NAME = DataDef.create( "IconName", STRING, //$NON-NLS-1$
      TSID_NAME, STR_N_TIP_ICON_NAME, //
      TSID_DESCRIPTION, STR_D_TIP_ICON_NAME, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Приоритет при выборе ярлыка внутри радиогруппы (сначала назначается более высокий приоритет) -100..+100.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: 0 - обычный приоритет по умолчанию, >0 - повышение приоритета, <0 - понижение приоритета<br>
   * Значение по умолчанию: 0 (обычный приоритет)
   */
  IDataDef IN_RADIO_PRIORITY = DataDef.create( "InRadioPriority", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_TIP_IN_RADIO_PRIORITY, //
      TSID_DESCRIPTION, STR_D_TIP_IN_RADIO_PRIORITY, //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  /**
   * All options list.
   */
  IStridablesList<IDataDef> ALL_OPS = new StridablesList<>( DDEF_DESCRIPTION, //
      IS_LEAF, //
      IS_RADIO, //
      IS_MANDATORY, //
      ICON_NAME, //
      IN_RADIO_PRIORITY //
  );

}
