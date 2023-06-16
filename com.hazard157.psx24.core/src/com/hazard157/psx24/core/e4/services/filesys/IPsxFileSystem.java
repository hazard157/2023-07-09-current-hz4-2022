package com.hazard157.psx24.core.e4.services.filesys;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Интерфейс работы с файлами приложения PSX (изображения, исходные видео и др).
 * <p>
 * Ссылка лежит в контексте приложения.
 *
 * @author hazard157
 */
public interface IPsxFileSystem {

  /**
   * Возвращает эпизоды, кадрый которых возможно есть в файловой системе.
   * <p>
   * Метод просто ищет а соответствующей директории поддиректории с именем, совпадающим с форматом идентификатора
   * эпизода.
   *
   * @return {@link IStringList} - список идентификаторов эпизодов
   */
  IStringList listProbableEpisodes();

  /**
   * Возвращает файл кадра эпизода, если таковой существует.
   *
   * @param aFrame {@link IFrame} - искомый кадр
   * @return {@link File} - файл иллюстрации или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  File findFrameFile( IFrame aFrame );

  /**
   * Возвращает файл значка из поддиректории значков, если таковой существует.
   * <p>
   * Аргумент, заданный относительным путем (не абсолютным) ищется в поддиректории значков.
   *
   * @param aPath String - путь к значку, включая имя файла с расшируением
   * @return {@link File} - файл значка или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  File findIconFile( String aPath );

  /**
   * Возвращает файл исходного видео, если таковой существует.
   *
   * @param aSourceVideoId String - идентификатор исходного видео
   * @return {@link File} - файл исходного видео или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  File findSourceVideoFile( String aSourceVideoId );

  /**
   * Возвращает файл трейлера эпизода, если таковой существует.
   *
   * @param aTrailerId String - полный идентификатор трейлера
   * @return {@link File} - файл трейлера эпизода или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  File findTrailerFile( String aTrailerId );

  /**
   * Находит файл фильма, если таковой существует.
   *
   * @param aFilmId String - идентификатор фильма
   * @return {@link File} - файл фильма или null
   * @throws TsNullArgumentRtException аргумент = null
   */
  // File findFilmFile( String aFilmId );

  /**
   * Находит проект kdenlive, связанный с фильмом.
   *
   * @param aFilmId String - идентификатор фильма
   * @return {@link File} - проект *.kdenlive или <code>null</code>
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент не валидный идентификатор эпизода
   */
  // File findFilmKdenliveProject( String aFilmId );

  /**
   * Возвращает список кадров, имеющих анимированный изображения.
   * <p>
   * Возвращаются кадры для заданного эпизода, указанной камеры и в заданном интервале. Если камера не задана
   * {@link Svin#hasCam()} = <code>false</code>, то возвращаются кадры со всех камер.
   *
   * @param aCriteria {@link FrameSelectionCriteria} - параметры запроса
   * @return IList&lt;{@link IFrame}&gt; - список анимированных кадров
   * @throws TsNullArgumentRtException аргумент = null
   */
  IList<IFrame> listEpisodeFrames( FrameSelectionCriteria aCriteria );

  /**
   * Возвращает список анимированных кадров {@link Svin}-а.
   * <p>
   * Если анимированных кадров нет ни одного, то пытается подобрать хотя бы один кадр с существующим файлом.
   *
   * @param aSvin {@link Svin} - интервал эпизода
   * @return {@link IList}&lt;{@link IFrame}&gt; - список (возможно пустой) иллюстрирующих интервал кадров
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IFrame> listSvinIllustrationFrames( Svin aSvin );

  /**
   * Returns all Kdenlive project files of given episode.
   *
   * @param aEpisodeId String - the episode OD
   * @return IList&lt;File&gt; - list of project files *.kdenlive
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid episode ID
   */
  IList<File> listEpisodeKdenliveProjects( String aEpisodeId );

  /**
   * Finds frame file and loads thumbnail image.
   *
   * @param aFrame {@link IFrame} - the frame
   * @param aThumbSize {@link EThumbSize} - asked size of the thumbnail
   * @return {@link TsImage} - loaded thumbnail or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  TsImage findThumb( IFrame aFrame, EThumbSize aThumbSize );

  /**
   * Аналогичен {@link #findThumb(IFrame, EThumbSize)}, но возвращает "квадраное" изображение.
   * <p>
   * Квадратные изображения (то есть, изображения размером каждой стороны в {@link EThumbSize#size()} пикселей) иногда
   * нужны для корректного отображения в SWT. Напрмер, значки в дереве JFace {@link TreeViewer} всегда запоняют
   * выделенный квадрат. Во избежание нарушения пропорции изображения и нужен этот метод.
   *
   * @param aFrame {@link IFrame} - запрошенный кадр
   * @param aThumbSize {@link EThumbSize} - размер получаемого миниатюры
   * @return {@link TsImage} - изображение миниатюры или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  // TsImage findThumbSquare( IFrame aFrame, EThumbSize aThumbSize );

  /**
   * Запускает процесс фонового кеширования и загрузки миниатюр указанного списка кадров.
   * <p>
   * Выдача миниатюры клиенту происходит в несколько этапов:
   * <ul>
   * <li>создание файла миниатюры вызовом внешней программы, является очень долгой процедурой и зависит от размера
   * исходного файла, скорости диска и процессора и т.п. Этот этап выполняется один раз - при последующих запусках
   * программы используется существуюий файл;</li>
   * <li>загрузка в кеш изображений миниатюры из созданного файла миниатюры. Выполняется каждый раз, когда миниатюры нет
   * в кеше в оперативной памяти. Для одиночных изображений фремя фиксировано, и зависит от запрошенного размера
   * миниатюры. Для анимированных изображений время увеличивается, и тем сильнее, чем больше кадров в анимации;</li>
   * <li>вызов {@link IFrameLoadedCallback#imageLoaded(IFrame, TsImage)} из основного GUI-потока выполнения.</li>
   * </ul>
   * В общем случае, метод запускает фоновый поток выполнения, который осуществляет все вышеприведенные этапы. Однако,
   * для тех изображений миниатюр, которые находятся кеше т.е. выполнен второй этап загрузки), обратный вызов происходит
   * прямо во время выполнения этого метода. Аргумент aImmediatelyLoadExistingThumbs позволяет также в теле этого метода
   * загружать миниатюры, для которых файлы уже существуют. А вот загрузка миниатюр, для которых файлов не существует,
   * всегда происходит в фоновом режиме.
   *
   * @param aFrames IList&lt;{@link IFrame}&gt; - список кадров для кеширования
   * @param aThumbSize {@link EThumbSize} - размер запрошенных миниатюр
   * @param aCallback {@link IFrameLoadedCallback} - действия, совершаемые по завершении загрузки отдельных изображений
   * @param aImmediatelyLoadExistingThumbs boolean - признак немедленной загрузки миниатюры с существующим файлом
   * @return {@link IFrameLoaderThread} - созданный и работающий поток загрузки изображений
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  IFrameLoaderThread startThumbsLoading( IList<IFrame> aFrames, EThumbSize aThumbSize, IFrameLoadedCallback aCallback,
      boolean aImmediatelyLoadExistingThumbs );

  /**
   * Создает анимированый GIF файл из кадров эпизода.
   * <p>
   * Выводит прогресс диалог.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aCamId String - идентификатор камеры
   * @param aStartSec int - секунда начала анимированного клипа
   * @return boolean - признак успешного создания GIF-файла
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  boolean createGifAnimation( String aEpisodeId, String aCamId, int aStartSec );

  /**
   * Пересоздает указанные кадры в виде анимированных GIFов.
   * <p>
   * Для создания кадров используется {@link IFrame#secNo()}.
   *
   * @param aFrames IList&lt{@link IFrame}&gt; - список кадров
   */
  void recreateGifAnimations( IList<IFrame> aFrames );

  /**
   * Creates illustrative image file for specified PSX video file.
   * <p>
   * Video file may be specified either as absolute or as relative to PSX files system root. Anyway it may be reside in
   * PSX file system,
   *
   * @param aPsxVideoFile {@link File} - video file somewhere in PSX file system
   * @return {@link File} - the existing (or created) illustrative image file or <code>null</code> if can't create
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  File enusurePsxVideoIllustrationImage( File aPsxVideoFile );

  /**
   * Возвращает исследователь файловой системы ресурсов приложения PSX.
   *
   * @return {@link IFsExplorer} - исследователь файловой системы
   */
  // IFsExplorer fsExplorer();

  /**
   * Returns means to access to the original media files.
   *
   * @return {@link IPfsOriginalMedia} - original media files
   */
  IPfsOriginalMedia originalMedia();

}
