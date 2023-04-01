package com.hazard157.psx24.core.utils;

import java.io.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Класс длля определения и кеширования длительностей видео-файлов.
 * <p>
 * Ссылка на экземпляр должен находится в контексте уровня приложения.
 *
 * @author hazard157
 */
public class VideoFileDurationCache {

  private final IMapEdit<File, Long>    mapTime = new ElemMap<>();
  private final IMapEdit<File, Integer> mapDurs = new ElemMap<>();

  /**
   * Конструктор.
   */
  public VideoFileDurationCache() {
    // nop
  }

  int determineDuration( File aFile ) {
    Pair<IStringList, IStringList> p = TsMiscUtils.runTool( "gg-determine-video-duration-msecs.sh", //$NON-NLS-1$
        aFile.getAbsolutePath() );
    String s = p.left().first();
    if( s == null || s.isEmpty() ) {
      return -1;
    }
    try {
      return Integer.parseInt( s ) / 1000;
    }
    catch( NumberFormatException ex ) {
      LoggerUtils.errorLogger().error( ex );
      return -1;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает длительность видео-файла.
   * <p>
   * Нсли файл после последнего запроса не менялся, возвращает данные из кеша, иначе вычисляет длительность, кладет в
   * кеш и возвращает.
   * <p>
   * Если длительность не может быть определена, возвпращает -1.
   *
   * @param aFile {@link File} - проверяемый файл
   * @return int - длительность в секундах или -1
   */
  public int getVideoDuration( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    if( !IMediaFileConstants.hasVideoExtension( aFile.getName() ) || !aFile.exists() ) {
      // удалим из кеша, например, файл был удален
      mapTime.removeByKey( aFile );
      mapDurs.removeByKey( aFile );
      return -1;
    }
    Integer dur = mapDurs.findByKey( aFile );
    // файла нет в кеше
    if( dur == null ) {
      dur = Integer.valueOf( determineDuration( aFile ) );
      if( dur.intValue() < 0 ) {
        return -1;
      }
      mapTime.put( aFile, Long.valueOf( aFile.lastModified() ) );
      mapDurs.put( aFile, dur );
      return dur.intValue();
    }
    // проверим актуальность информации о длительность
    Long lastFileModif = mapTime.getByKey( aFile );
    if( aFile.lastModified() > lastFileModif.longValue() ) {
      // актуализируем информацию
      lastFileModif = Long.valueOf( aFile.lastModified() );
      dur = Integer.valueOf( determineDuration( aFile ) );
      if( dur.intValue() < 0 ) {
        // удалим из кеша, например, файл был поврежден
        mapTime.removeByKey( aFile );
        mapDurs.removeByKey( aFile );
        return -1;
      }
      mapTime.put( aFile, lastFileModif );
      mapDurs.put( aFile, dur );
    }
    return dur.intValue();

  }

  /**
   * Сбрасывает всю кешированную информацию.
   */
  public void reset() {
    mapTime.clear();
    mapDurs.clear();
  }

}
