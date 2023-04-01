package com.hazard157.psx24.films.glib;

import static com.hazard157.psx24.films.glib.IPsxResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.lib.core.quants.rating.*;
import com.hazard157.psx24.films.glib.valeds.*;

/**
 * Свойства порнила (файлов и директорий), хранимые в конвой-файлах.
 * <p>
 * Конфой-файлы сохраняются/загружаются средствами {@link PornillConvoyFileManager}. И для директорий, и для файлов в
 * качестве содержимого конвоя используется {@link IOptionSet}.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface IFilmPropsConstants {

  String CONVOY_FILE_EXT = "pornill"; //$NON-NLS-1$

  IDataDef FP_RATING = DataDef.create( "Rating", VALOBJ, //$NON-NLS-1$
      TSID_NAME, "Оценка", //
      TSID_DESCRIPTION, "Общая оценка порнилла", //
      TSID_KEEPER_ID, ERating.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ERating.UNKNOWN, ERating.KEEPER, ERating.KEEPER_ID ) //
  );

  IDataDef FP_NOTES = DataDef.create( "Notes", STRING, //
      TSID_NAME, "Заметки", //
      TSID_DESCRIPTION, "Произвольная дополнительная информация", //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Список ключевых слов, которыми помечен фильм.
   */
  IDataDef FP_KEYWORDS = DataDef.create( "Keywords", VALOBJ, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avValobj( IStringList.EMPTY ), //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvPsxKeywords.FACTORY_NAME, //
      TSID_KEEPER_ID, StringListKeeper.KEEPER_ID, //
      TSID_NAME, STR_N_KEYWORDS, //
      TSID_DESCRIPTION, STR_D_KEYWORDS //
  );

}
