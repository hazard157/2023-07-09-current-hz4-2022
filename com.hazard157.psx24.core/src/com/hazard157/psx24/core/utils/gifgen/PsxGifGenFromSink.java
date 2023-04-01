package com.hazard157.psx24.core.utils.gifgen;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.utils.gifgen.IPsxResources.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Создает анимированный gif из списка {@link Svin}-ов с заданными в {@link PsxGifGenParams}.
 *
 * @author hazard157
 */
public class PsxGifGenFromSink {

  private final IEclipseContext windowContext;

  private final PsxGifGenParams params = new PsxGifGenParams();

  /**
   * Конструктор.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public PsxGifGenFromSink( IEclipseContext aWinContext ) {
    TsNullArgumentRtException.checkNull( aWinContext );
    windowContext = aWinContext;
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Возвращает контекст приложения уровня окна приложения.
   *
   * @return {@link IEclipseContext} - контекст приложения уровня окна приложения
   */
  public IEclipseContext windowContext() {
    return windowContext;
  }

  /**
   * Возвращает ссылку из контекста с выбрасыванием исключения при её отсутствии в контексте.
   *
   * @param <T> - тип ссылки
   * @param aRefClass {@link Class}&lt;T&gt; - тип ссылки
   * @return &lt;T&gt; - найденная ссылка
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такой ссылки в контексте
   */
  <T> T ctxRef( Class<T> aRefClass ) {
    TsNullArgumentRtException.checkNull( aRefClass );
    T ref = windowContext.get( aRefClass );
    if( ref == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_REF_IN_CONTEXT, aRefClass.getName() );
    }
    return ref;
  }

  /**
   * Возвращает виджет окна.
   *
   * @return {@link Shell} - виджет окна
   */
  Shell getShell() {
    return ctxRef( Shell.class );
  }

  /**
   * Возвращает ссылку на {@link IPsxFileSystem}.
   *
   * @return {@link IPsxFileSystem} - доступ к медия-файлам приложения
   */
  IPsxFileSystem fileSystem() {
    return ctxRef( IPsxFileSystem.class );
  }

  /**
   * Возвращает домен моделирования сущностей приложения.
   *
   * @return {@link IM5Domain} - домен моделирования сущностей приложения
   */
  IM5Domain m5() {
    IM5Domain d = windowContext.get( IM5Domain.class );
    TsIllegalStateRtException.checkNull( d, MSG_ERR_NO_DOMAIN_IN_WIN_CONTEXT );
    return d;
  }

  @SuppressWarnings( "nls" )
  void createGifFromFrames( File aGifFile, IList<IFrame> aFrames, int aDelayMilliSecs ) {
    TsFileUtils.checkFileAppendable( aGifFile );
    TsNullArgumentRtException.checkNull( aFrames );
    TsIllegalArgumentRtException.checkTrue( aFrames.isEmpty() );
    TsIllegalArgumentRtException.checkTrue( aDelayMilliSecs < 0 );
    IStringListEdit args = new StringArrayList( 64 );
    args.add( "convert" );
    addDelayMsecs( args, aDelayMilliSecs );
    String lastFileName = null;
    for( int i = 0, count = aFrames.size(); i < count; i++ ) {
      IFrame frame = aFrames.get( i );
      TsIllegalArgumentRtException.checkFalse( frame.isDefined() );
      TsIllegalArgumentRtException.checkTrue( frame.isAnimated() );
      File ff = fileSystem().findFrameFile( frame );
      if( ff != null ) {
        lastFileName = ff.getAbsolutePath();
        args.add( lastFileName );
      }
      else {
        TsTestUtils.pl( "No file for frame %s", frame.toString() );
      }
    }
    if( lastFileName != null && params.getFinalStillDelay() > 0 ) {
      args.remove( args.last() ); // удалим последний
      addDelayMsecs( args, params.getFinalStillDelay() );
      args.add( lastFileName ); // и вставим с задержкой
    }
    args.add( aGifFile.getAbsolutePath() );
    // DEBUG TsTestUtils.printStringList( args, true, true );
    TsMiscUtils.runProgram( "gm", args.toArray() );
  }

  @SuppressWarnings( "nls" )
  private static void addDelayMsecs( IStringListEdit aArgs, int aDelayMilliSecs ) {
    int delaySantiSecs = aDelayMilliSecs / 10;
    if( delaySantiSecs == 0 ) {
      delaySantiSecs = 1;
    }
    aArgs.add( "-delay" );
    aArgs.add( Integer.toString( delaySantiSecs ) );
  }

  private void addAnimPartFrames( String aEpisodeId, String aCameraId, int aStartSec, IListEdit<IFrame> aFrames ) {
    int bypassFramesOnAnim = params.getBypassedFramesOnAnim();
    if( bypassFramesOnAnim > 0 ) {
      for( int i = 0; i < FPS / bypassFramesOnAnim + 1; i++ ) {
        IFrame f = new Frame( aEpisodeId, aCameraId, aStartSec * FPS + i * bypassFramesOnAnim, false );
        aFrames.add( f );
      }
    }
    else {
      IFrame f = new Frame( aEpisodeId, aCameraId, aStartSec * FPS, false );
      aFrames.add( f );
    }
  }

  private IListEdit<IFrame> createFramesForFixedFramesMode( IList<Svin> aSink ) {
    int dur = 0;
    for( Svin sp : aSink ) {
      dur += sp.interval().duration();
    }
    int deltaSecs = dur / params.getFramesNum();
    IListEdit<IFrame> frames = new ElemArrayList<>( 100 );
    int animFrameNo = 0;
    int cStart = 0;
    for( Svin sp : aSink ) {
      Secint cIn = new Secint( cStart, cStart + sp.interval().duration() - 1 );
      int animSecNo = deltaSecs * animFrameNo;
      while( cIn.contains( animSecNo ) ) {
        int fSec = sp.interval().start() + (animSecNo - cIn.start());
        addAnimPartFrames( sp.episodeId(), sp.cameraId(), fSec, frames );
        ++animFrameNo;
        animSecNo = deltaSecs * animFrameNo;
      }
      cStart = cStart + sp.interval().duration() - 1;
    }
    return frames;
  }

  private IListEdit<IFrame> createFramesForFixedDeltaMode( IList<Svin> aSink ) {
    int deltaSecs = params.getDeltaSec();
    IListEdit<IFrame> frames = new ElemArrayList<>( 100 );
    int animFrameNo = 0;
    int cStart = 0;
    for( Svin sp : aSink ) {
      Secint cIn = new Secint( cStart, cStart + sp.interval().duration() - 1 );
      int animSecNo = deltaSecs * animFrameNo;
      while( cIn.contains( animSecNo ) ) {
        int fSec = sp.interval().start() + (animSecNo - cIn.start());
        addAnimPartFrames( sp.episodeId(), sp.cameraId(), fSec, frames );
        ++animFrameNo;
        animSecNo = deltaSecs * animFrameNo;
      }
      cStart = cStart + sp.interval().duration() - 1;
    }
    return frames;
  }

  private IListEdit<IFrame> createFramesForSvinReckonedMode( IList<Svin> aSink ) {
    int reckonSecs = params.getSvinReckonSecs();
    // сначала сформируем новый источник, с разделенными длинныими кусками
    IListEdit<Svin> sink = new ElemArrayList<>();
    for( Svin s1 : aSink ) {
      // технические куски по одной секунде - игнорируются
      if( s1.interval().duration() <= 1 ) {
        continue;
      }
      // короткие куски подут прямо в дело
      if( s1.interval().duration() <= reckonSecs ) {
        sink.add( s1 );
        continue;
      }
      // длинные разобъем на части
      int start = s1.interval().start(); // переманное начало генерируемых кусков
      int endOfS1 = s1.interval().end(); // окончание рассматриваемого куска
      while( endOfS1 - start + 1 > reckonSecs ) {
        // последный огрызок засунем полностью
        if( endOfS1 - start + 1 < 2 * reckonSecs ) {
          Svin s2 = new Svin( s1.episodeId(), s1.cameraId(), new Secint( start, endOfS1 ) );
          sink.add( s2 );
          break;
        }
        Svin s2 = new Svin( s1.episodeId(), s1.cameraId(), new Secint( start, start + reckonSecs ) );
        sink.add( s2 );
        start += reckonSecs;
      }
    }

    // DEBUG
    for( Svin sp : sink ) {
      TsTestUtils.pl( sp.toString() );
    }

    // теперь создадим набор кадров - по одному на кусок нового источника
    IListEdit<IFrame> frames = new ElemArrayList<>( 100 );
    for( Svin sp : sink ) {
      int fSec = sp.interval().start() + sp.interval().duration() / 2;
      addAnimPartFrames( sp.episodeId(), sp.cameraId(), fSec, frames );
    }
    return frames;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает пареметры генерации миниатюр.
   *
   * @return {@link PsxGifGenParams} - редактируемые пареметры генерации миниатюр
   */
  public PsxGifGenParams params() {
    return params;
  }

  /**
   * Создает анимированной gif-файл текущими установками.
   * <p>
   * Метод запускает внежнюю программу GraphicsMagik и возвращается немедленно.
   *
   * @param aGifFile {@link File} - имя выходного GIF-файла
   * @param aSink IList&lt;{@link Svin}&lt; - источник видео (список кусков)
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIoRtException афйл не может быть записан
   */
  public void createThumb( File aGifFile, IList<Svin> aSink ) {
    TsFileUtils.checkFileAppendable( aGifFile );
    TsNullArgumentRtException.checkNull( aSink );
    IList<IFrame> frames = switch( params.getMode() ) {
      case FIXED_FRAMES -> createFramesForFixedFramesMode( aSink );
      case FIXED_DELTA -> createFramesForFixedDeltaMode( aSink );
      case SVINS_RECKONED -> createFramesForSvinReckonedMode( aSink );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    int delayMilliSecs;
    if( params.getBypassedFramesOnAnim() != 0 ) {
      delayMilliSecs = 1000 / FPS * params.getBypassedFramesOnAnim();
    }
    else {
      delayMilliSecs = 1000;
    }
    createGifFromFrames( aGifFile, frames, delayMilliSecs );
  }

}
