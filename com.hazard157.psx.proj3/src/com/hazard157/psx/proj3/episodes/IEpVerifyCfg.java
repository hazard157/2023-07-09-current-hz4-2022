package com.hazard157.psx.proj3.episodes;

import static com.hazard157.psx.proj3.episodes.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Настройки верификации эпизода.
 *
 * @author hazard157
 */
public interface IEpVerifyCfg
    extends IKeepableEntity, IGenericChangeEventCapable, IParameterizedEdit {

  /**
   * Возвращает редактируемый список идентификаторов ярлыков, которые отсутствуют в эпизоде.
   * <p>
   * Смысл этого в том, чтобы не выдавать пердупреждение об отсутствии этого ярлыка в эпизоде. Фактчески, это означает,
   * что эпизод был просмотрен на предмет этого ярлыка.
   *
   * @return {@link IStringListBasicEdit} - редактируемый список идентификаторов ярлыков, которые отсутствуют в эпизоде
   */
  IStringListBasicEdit nonExistingTagIds();

  /**
   * Параметр {@link #params()}: Определяет, отключено ли предупреждение о слишком коротких длителностьях сцен.
   */
  IDataDef IS_MIN_SCENE_DURATION_WARNING_DISABLED = DataDef.create( "isMinSceneDurationWarningDisabled", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_MIN_SCENE_DURATION_WARNING_DISABLED, //
      TSID_DESCRIPTION, STR_D_IS_MIN_SCENE_DURATION_WARNING_DISABLED, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Параметр {@link #params()}: Определяет, отключено ли предупреждение о слишком большом количестве дочерных сцен.
   */
  IDataDef IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED = DataDef.create( "isMaxChildScenesCountWarningDisabled", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED, //
      TSID_DESCRIPTION, STR_D_IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Описания всех пераметров {@link #params()}.
   */
  IStridablesList<IDataDef> ALL_OPS = new StridablesList<>( //
      IS_MIN_SCENE_DURATION_WARNING_DISABLED, //
      IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED //
  );
}
