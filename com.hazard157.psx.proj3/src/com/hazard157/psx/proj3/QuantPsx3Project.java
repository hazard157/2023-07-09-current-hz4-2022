package com.hazard157.psx.proj3;

import java.io.*;
import java.time.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.bound.*;
import org.toxsoft.core.txtproj.lib.impl.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.impl.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx.proj3.gaze.impl.*;
import com.hazard157.psx.proj3.mingle.*;
import com.hazard157.psx.proj3.mingle.impl.*;
import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx.proj3.pleps.impl.*;
import com.hazard157.psx.proj3.songs.*;
import com.hazard157.psx.proj3.songs.impl.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx.proj3.sourcevids.impl.*;
import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx.proj3.tags.impl.*;
import com.hazard157.psx.proj3.todos.*;
import com.hazard157.psx.proj3.todos.impl.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx.proj3.trailers.impl.*;

/**
 * PRISEX project quant.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class QuantPsx3Project
    extends AbstractQuant {

  /**
   * Command line argument name for {@link ProgramArgs} to specify PSXv3 textual project file path.
   */
  public static final String CMDLINE_ARG_PROJECT_FILE_PATH = "project"; //$NON-NLS-1$

  private static final String DEFAULT_PROJ_FILE_PATH = "/home/hmade/data/projects/ver3/prisex.txt"; //$NON-NLS-1$

  public static final String UNITID_CAMERAS       = "Cameras";         //$NON-NLS-1$
  public static final String UNITID_EPISODES      = "Episodes";        //$NON-NLS-1$
  public static final String UNITID_SOURCE_VIDEOS = "SourceVideos";    //$NON-NLS-1$
  public static final String UNITID_TODOS         = "Todos3";          //$NON-NLS-1$
  public static final String UNITID_TRAILERS      = "Trailers";        //$NON-NLS-1$
  public static final String UNITID_TAGS          = "TagManager";      //$NON-NLS-1$
  public static final String UNITID_SONGS         = "SongManager";     //$NON-NLS-1$
  public static final String UNITID_PLEPS         = "PlannedEpisodes"; //$NON-NLS-1$
  public static final String UNITID_GAZES         = "Gazes";           //$NON-NLS-1$
  public static final String UNITID_MINGLES       = "Mingles";         //$NON-NLS-1$

  private static final int    PROJ_FILE_FORMAT_VERSTION = 3;
  private static final String PROJ_FILE_APP_ID          = "ru.tsdev.goga.prisex"; //$NON-NLS-1$

  /**
   * Information about project file v31.
   */
  public static final TsProjectFileFormatInfo PROJECT_FILE_FORMAT_INFO =
      new TsProjectFileFormatInfo( PROJ_FILE_APP_ID, PROJ_FILE_FORMAT_VERSTION );

  static final IMapEdit<LocalDate, LocalDateTime> EPISODES_WHENS_MAP = new ElemMap<>();

  static {
    EPISODES_WHENS_MAP.put( LocalDate.of( 1995, 5, 6 ), LocalDateTime.of( 1995, 5, 6, 11, 39 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2001, 1, 1 ), LocalDateTime.of( 2001, 1, 1, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2002, 1, 1 ), LocalDateTime.of( 2002, 1, 1, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2002, 3, 23 ), LocalDateTime.of( 2002, 3, 23, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2003, 9, 14 ), LocalDateTime.of( 2003, 9, 14, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2005, 3, 26 ), LocalDateTime.of( 2005, 3, 26, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2009, 3, 21 ), LocalDateTime.of( 2009, 3, 21, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2009, 8, 1 ), LocalDateTime.of( 2009, 8, 1, 13, 0 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2009, 10, 7 ), LocalDateTime.of( 2009, 10, 7, 7, 30 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2009, 10, 20 ), LocalDateTime.of( 2009, 10, 20, 23, 15 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2009, 10, 31 ), LocalDateTime.of( 2009, 10, 31, 16, 3 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2009, 12, 19 ), LocalDateTime.of( 2009, 12, 19, 12, 35 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 1, 10 ), LocalDateTime.of( 2010, 1, 10, 12, 30 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 1, 25 ), LocalDateTime.of( 2010, 1, 25, 6, 48 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 3, 2 ), LocalDateTime.of( 2010, 3, 2, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 3, 21 ), LocalDateTime.of( 2010, 3, 21, 21, 1 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 7, 11 ), LocalDateTime.of( 2010, 7, 11, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 7, 30 ), LocalDateTime.of( 2010, 7, 30, 22, 51 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 8, 9 ), LocalDateTime.of( 2010, 8, 9, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 9, 2 ), LocalDateTime.of( 2010, 9, 2, 2, 15 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 9, 11 ), LocalDateTime.of( 2010, 9, 11, 19, 11 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2010, 12, 26 ), LocalDateTime.of( 2010, 12, 26, 9, 4 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2011, 1, 10 ), LocalDateTime.of( 2011, 1, 10, 21, 4 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2011, 3, 20 ), LocalDateTime.of( 2011, 3, 20, 23, 59 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2011, 10, 23 ), LocalDateTime.of( 2011, 10, 23, 11, 13 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2012, 3, 3 ), LocalDateTime.of( 2012, 3, 3, 8, 50 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2012, 5, 7 ), LocalDateTime.of( 2012, 5, 7, 12, 29 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2012, 6, 24 ), LocalDateTime.of( 2012, 6, 24, 12, 43 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2014, 1, 3 ), LocalDateTime.of( 2014, 1, 3, 11, 45 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2014, 1, 11 ), LocalDateTime.of( 2014, 1, 11, 16, 46 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2014, 9, 22 ), LocalDateTime.of( 2014, 9, 22, 10, 00 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2016, 1, 30 ), LocalDateTime.of( 2016, 1, 30, 13, 54 ) );
    EPISODES_WHENS_MAP.put( LocalDate.of( 2017, 06, 10 ), LocalDateTime.of( 2017, 06, 10, 14, 51 ) );
  }

  /**
   * Converts existing episode's {@link IEpisode#when()} to the {@link LocalDateTime}.
   * <p>
   * Direct conversion from epoch milliseconds does not works because <code><b>long</b></code> timestamps are created
   * with different time zones.
   * <p>
   * FIXME this is kind of trick and works for episodes until 2023-03-29!
   *
   * @param aWhen long - an existing episode {@link IEpisode#when()}
   * @return {@link LocalDateTime} - episode starting timestamp
   */
  @SuppressWarnings( { "boxing", "nls" } )
  public static LocalDateTime episodeWhenToLdt( long aWhen ) {
    String epId3 = EpisodeUtils.when2EpisodeId( aWhen );
    LocalDate ld3 = EpisodeUtils.episodeId2LocalDate( epId3 );
    LocalDateTime ldt = EPISODES_WHENS_MAP.findByKey( ld3 );
    if( ldt == null ) {
      throw new TsInternalErrorRtException( "No episode found at %tF %tT", aWhen, aWhen );
    }
    return ldt;
  }

  /**
   * Конструктор.
   */
  public QuantPsx3Project() {
    super( QuantPsx3Project.class.getSimpleName() );
    TsValobjUtils.registerKeeper( FramesList.KEEPER_ID, FramesList.KEEPER );
    TsValobjUtils.registerKeeper( Frame.KEEPER_ID, Frame.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ITsProject proj = aAppContext.get( ITsProject.class );
    if( proj == null ) {
      proj = new TsProject( PROJECT_FILE_FORMAT_INFO );
      aAppContext.set( ITsProject.class, proj );
      ITsProjectFileBound bound = new TsProjectFileBound( proj, IOptionSet.NULL );
      aAppContext.set( ITsProjectFileBound.class, bound );
      ProgramArgs programArgs = aAppContext.get( ProgramArgs.class );
      String path = programArgs.getArgValue( CMDLINE_ARG_PROJECT_FILE_PATH, DEFAULT_PROJ_FILE_PATH );
      File projectFile = new File( path );
      bound.open( projectFile );
    }
    initProject( aAppContext );
    ITsProjectFileBound bound = aAppContext.get( ITsProjectFileBound.class );
    if( bound.hasFileBound() ) {
      LoggerUtils.defaultLogger().info( "OK loading project %s", bound.getFile().getAbsolutePath() ); //$NON-NLS-1$
    }
  }

  /**
   * Наполняет проект (ссылку из контекста) модулями PSX v3.
   * <p>
   * Объявлен публичным, для использования в других приложениях например, PSX следующей версии, для осуществудения
   * импорта из проекта версии 3.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void initProject( IEclipseContext aAppContext ) {
    TsNullArgumentRtException.checkNull( aAppContext );
    ITsProject proj = aAppContext.get( ITsProject.class );
    TsInternalErrorRtException.checkNull( proj );
    // регистрация модулей проекта
    IUnitEpisodes unitEpisodes = new UnitEpisodes();
    proj.registerUnit( UNITID_EPISODES, unitEpisodes, true );
    aAppContext.set( IUnitEpisodes.class, unitEpisodes );
    //
    IUnitSourceVideos unitSourceVideos = new UnitSourceVideos();
    proj.registerUnit( UNITID_SOURCE_VIDEOS, unitSourceVideos, true );
    aAppContext.set( IUnitSourceVideos.class, unitSourceVideos );
    //
    IUnitTodos unitTodos = new UnitTodos();
    proj.registerUnit( UNITID_TODOS, unitTodos, true );
    aAppContext.set( IUnitTodos.class, unitTodos );
    //
    IUnitCameras unitCameras = new UnitCameras();
    proj.registerUnit( UNITID_CAMERAS, unitCameras, true );
    aAppContext.set( IUnitCameras.class, unitCameras );
    //
    IUnitTrailers unitTrailers = new UnitTrailers();
    proj.registerUnit( UNITID_TRAILERS, unitTrailers, true );
    aAppContext.set( IUnitTrailers.class, unitTrailers );
    //
    IUnitSongs unitSongs = new UnitSongs();
    proj.registerUnit( UNITID_SONGS, unitSongs, true );
    aAppContext.set( IUnitSongs.class, unitSongs );
    //
    IUnitPleps unitPleps = new UnitPleps();
    proj.registerUnit( UNITID_PLEPS, unitPleps, true );
    aAppContext.set( IUnitPleps.class, unitPleps );
    //
    IUnitTags unit = new UnitTags();
    proj.registerUnit( UNITID_TAGS, unit, true );
    aAppContext.set( IUnitTags.class, unit );
    aAppContext.set( IRootTag.class, unit.root() );
    //
    IUnitGazes unitGazes = new UnitGazes();
    proj.registerUnit( UNITID_GAZES, unitGazes, true );
    aAppContext.set( IUnitGazes.class, unitGazes );
    //
    IUnitMingles unitMingles = new UnitMingles();
    proj.registerUnit( UNITID_MINGLES, unitMingles, true );
    aAppContext.set( IUnitMingles.class, unitMingles );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IPsxProj3Constants.init( aWinContext );
  }

}
