package com.hazard157.psx24.films.e4.services;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.incub.*;

/**
 * Служба работы с фильмами.
 *
 * @author hazard157
 */
public interface IPsxFilmsService {

  /**
   * Возвращает директорию фмльмов.
   *
   * @return {@link File} - директория фильмов
   */
  File filmsDir();

  /**
   * Загружает GIF-анимированную миниатюру фильма.
   *
   * @param aFilmFile {@link File} - файл фильма, может быть null
   * @param aThumb {@link EThumbSize} - размер миниатюры
   * @return {@link TsImage} - изображение миниатюры или <code>null</code>
   */
  TsImage loadFilmThumb( File aFilmFile, EThumbSize aThumb );

  /**
   * Возвращает файлы фильмов.
   * <p>
   * Каждый раз перечитывает директорию фильмов.
   *
   * @return {@link IList}&lt;{@link File}&gt; - файлы фильмов
   */
  IList<File> listFilmFiles();

  /**
   * Возвращает список ключевых слов, примененных для пометки фильмов.
   *
   * @return {@link IStringList} - список ключевых слов
   */
  IStringList listExistingKeywords();

  /**
   * Возвращает настройки модуля.
   *
   * @return {@link IPrefBundle} - настройки модуля
   */
  IPrefBundle modulePrefs();

  /**
   * Возвращает файл проекта Kdenlive запрошенного фильма, не проверяя его существование.
   *
   * @param aFilmFile {@link File} - файл фильма
   * @return {@link File} - файл проекта Kdenlive запрошенного фильма, может не существовать
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  File kdenliveProjectFile( File aFilmFile );

  /**
   * Возвращает настроенный менеджер конвой-файлов фильмов.
   *
   * @return {@link ConvoyFileManager} - менеджер конвой-файлов фильмов
   */
  ConvoyFileManager cfm();

  /**
   * Возвращает извещатель об изменениях в файловой системе в директории фильмов.
   * <p>
   * Генерирует сообщения {@link IGenericChangeListener#onGenericChangeEvent(Object)} когда в фаловой системе в
   * директории фильмов обнаруживает изменения.
   *
   * @return {@link IGenericChangeEventer} - извещатель
   */
  IGenericChangeEventer fsChangeEventProducer();

}
