package com.hazard157.lib.core.excl_done.stdops;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.rcp.valed.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.time.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;

/**
 * Package constants.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface IHzOptionsConstants {

  String OPID_NOTE       = "note";      //$NON-NLS-1$
  String OPID_DURATION   = "duration";  //$NON-NLS-1$
  String OPID_IMAGE_FILE = "imageFile"; //$NON-NLS-1$
  String OPID_AUDIO_FILE = "audioFile"; //$NON-NLS-1$

  IDataDef OPDEF_NOTE = DataDef.create( OPID_NOTE, STRING, //
      TSID_NAME, "Заметки", //
      TSID_DESCRIPTION, "Заметки и пояснения", //

      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  IDataDef OPDEF_DURATION = DataDef.create( OPID_DURATION, INTEGER, //
      TSID_NAME, "Длительность", //
      TSID_DESCRIPTION, "Длительность в секундах", //
      IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvIntegerSecsDurationMpv.FACTORY_NAME, //
      ValedSecsDurationMpv.OPID_IS_HOURS_PART, AV_FALSE, //
      TSID_MIN_INCLUSIVE, avInt( 1 ), //
      TSID_DEFAULT_VALUE, avInt( 1 ) //
  );

  IDataDef OPDEF_IMAGE_FILE = DataDef.create( OPID_IMAGE_FILE, VALOBJ, //
      TSID_NAME, "Изображение", //
      TSID_DESCRIPTION, "Файл иллюстрирующего изображения", //
      TSID_KEEPER_ID, FileKeeper.KEEPER_ID, //
      // FIXME IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjImageFile.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  IDataDef OPDEF_AUDIO_FILE = DataDef.create( OPID_AUDIO_FILE, VALOBJ, //
      TSID_NAME, "Файл", //
      TSID_DESCRIPTION, "Путь к аудио файлу", //
      TSID_KEEPER_ID, FileKeeper.KEEPER_ID, //
      IValedControlConstants.OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjFile.FACTORY_NAME, //
      IValedFileConstants.OPDEF_IS_OPEN_DIALOG, AV_TRUE, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

}
