package com.hazard157.psx.proj3.songs;

import static org.toxsoft.core.tslib.av.EAtomicType.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Константы {@link IUnitSongs}.
 *
 * @author hazard157
 */
public interface IUnitSongsConstants {

  /**
   * Идентификатор по умолчанию для регистрации компоненты в проекте методом
   * {@link ITsProject#registerUnit(String, IProjDataUnit, boolean)}.
   */
  String DEFAULT_SONG_MANAGER_PDU_KEYWORD = "SongManager"; //$NON-NLS-1$

  /**
   * Идентификатор опции {@link #OP_FILE_PATH}.
   */
  String OPID_FILE_PATH = "filePath"; //$NON-NLS-1$

  /**
   * Опция {@link ISong#filePath()}.
   */
  IDataDef OP_FILE_PATH = DataDef.create( OPID_FILE_PATH, STRING );

  /**
   * Идентификатор опции {@link #OP_DURATION}.
   */
  String OPID_DURATION = "duration"; //$NON-NLS-1$

  /**
   * Опция {@link ISong#duration()}.
   */
  IDataDef OP_DURATION = DataDef.create( OPID_DURATION, INTEGER );

}
