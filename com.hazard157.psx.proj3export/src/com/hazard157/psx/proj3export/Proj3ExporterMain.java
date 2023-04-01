package com.hazard157.psx.proj3export;

import static com.hazard157.psx.proj3.QuantPsx3Project.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;
import static org.toxsoft.core.tslib.utils.TsTestUtils.*;

import java.io.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.core.txtproj.lib.*;
import org.toxsoft.core.txtproj.lib.bound.*;
import org.toxsoft.core.txtproj.lib.impl.*;

import com.hazard157.lib.core.bricks.kwmark.*;
import com.hazard157.lib.core.bricks.kwmark.manager.*;
import com.hazard157.lib.core.quants.rating.*;
import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.quants.visumple.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.cameras.impl.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.impl.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx.proj3.gaze.impl.*;
import com.hazard157.psx.proj3.movies.*;
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
import com.hazard157.psx24.catnote.e4.addons.*;
import com.hazard157.psx24.catnote.main.*;
import com.hazard157.psx24.catnote.main.impl.*;
import com.hazard157.psx24.explorer.e4.addons.*;
import com.hazard157.psx24.explorer.filters.*;
import com.hazard157.psx24.explorer.unit.*;
import com.hazard157.psx24.explorer.unit.impl.*;

/**
 * Exporter CLI runner.
 * <p>
 * Exported file is a sectioned file. Each section is a list of option sets. Thats all yet...
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public class Proj3ExporterMain {

  public static final String DEFAULT_PROJ3_FILE_PATH    = "/home/hmade/data/projects/ver3/prisex.txt";
  public static final String DEFAULT_EXPORTED_FILE_PATH = "/home/hmade/data/projects/ver3/proj3_exported.txt";

  public static final String CMDLINE_ARG_PROJ3_FILE_PATH = "proj3";
  public static final String CMDLINE_ARG_OUT_FILE_PATH   = "output";

  private static final boolean INDENTED_OUTPUT = true;

  private static final IEntityKeeper<IOptionSet> OPSET_KEEPER = OptionSetKeeper.getInstance( INDENTED_OUTPUT );

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void utilCopyStridable( IOptionSetEdit aOps, IStridable aItem ) {
    aOps.setStr( TSID_ID, aItem.id() );
    aOps.setStr( TSID_NAME, aItem.nmName() );
    aOps.setStr( TSID_DESCRIPTION, aItem.description() );
  }

  private static <T extends IStridable & IParameterized> void utilCopyStripar( IOptionSetEdit aOps, T aItem ) {
    utilCopyStridable( aOps, aItem );
    String iconId;
    if( aItem instanceof IStridableParameterized stripar ) {
      iconId = stripar.iconId();
    }
    else {
      iconId = aItem.params().getStr( TSID_ICON_ID, null );
    }
    if( iconId != null ) {
      aOps.setStr( TSID_ICON_ID, iconId );
    }
  }

  private static <T extends IStridable & IParameterized> void utilFullCopyStripar( IOptionSetEdit aOps, T aItem ) {
    utilCopyStripar( aOps, aItem );
    aOps.addAll( aItem.params() );
    utilCopyStripar( aOps, aItem );
  }

  private static void exportCameras( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting cameras... " );
    // read unit
    IUnitCameras unitCameras = new UnitCameras();
    aProj3.registerUnit( UNITID_CAMERAS, unitCameras, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( ICamera item : unitCameras.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      p.setStr( "kind", item.kind().id() );
      p.setBool( "isAvailable", item.isCamAvailable() );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Cameras", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportSourceVideos( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting source videos... " );
    // read unit
    IUnitSourceVideos unitSourceVideos = new UnitSourceVideos();
    aProj3.registerUnit( UNITID_SOURCE_VIDEOS, unitSourceVideos, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( ISourceVideo item : unitSourceVideos.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      p.setStr( "duration", HmsUtils.hhhmmss( item.duration() ) );
      // no need for following - item.nmName() is the same as a location
      // p.setStr( "location", item.location() );
      p.setStr( "frame", FrameKeeper.KEEPER.ent2str( item.frame() ) );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "SourceVideos", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportTags( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting tags... " );
    // read unit
    IUnitTags unitTags = new UnitTags();
    aProj3.registerUnit( UNITID_TAGS, unitTags, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( ITag item : unitTags.root().allNodesBelow() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStripar( p, item );
      p.setBool( "isLeaf", ITagsConstants.IS_LEAF.getValue( item.params() ).asBool() );
      p.setBool( "isRadio", ITagsConstants.IS_RADIO.getValue( item.params() ).asBool() );
      p.setBool( TSID_IS_MANDATORY, ITagsConstants.IS_MANDATORY.getValue( item.params() ).asBool() );
      p.setStr( "iconFileName", ITagsConstants.ICON_NAME.getValue( item.params() ).asString() );
      p.setInt( "priorityInRadioGroup", ITagsConstants.IN_RADIO_PRIORITY.getValue( item.params() ).asInt() );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Tags", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static IOptionSet makeScene( IScene aScene ) {
    IOptionSetEdit p = new OptionSet();
    p.setStr( "name", aScene.info().name() );
    p.setStr( "s", HmsUtils.mmss( aScene.interval().start() ) );
    p.setStr( "e", HmsUtils.mmss( aScene.interval().end() ) );
    p.setStr( "frame", FrameKeeper.KEEPER.ent2str( aScene.frame() ) );
    for( int i = 1; i <= aScene.childScenes().size(); i++ ) {
      IScene s = aScene.childScenes().values().get( i - 1 );
      IOptionSet ops = makeScene( s );
      p.setValobj( "Scene_" + i, ops );
    }
    return p;
  }

  private static void exportEpisodes( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting episodes... " );
    // read unit
    IUnitEpisodes unitEpisodes = new UnitEpisodes();
    aProj3.registerUnit( UNITID_EPISODES, unitEpisodes, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( IEpisode item : unitEpisodes.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      p.setStr( "frame", FrameKeeper.KEEPER.ent2str( item.frame() ) );
      p.setStr( "duration", HmsUtils.hhhmmss( item.duration() ) );
      p.setStr( "place", item.info().place() );
      p.setValobj( "when", episodeWhenToLdt( item.when() ) );
      p.setStr( "actionIntervalStart", HmsUtils.hhhmmss( item.info().actionInterval().start() ) );
      p.setStr( "actionIntervalEnd", HmsUtils.hhhmmss( item.info().actionInterval().end() ) );
      p.setStr( "defaultTrailerId", item.info().defaultTrailerId() );
      p.setStr( "whatIsNoteDone", item.info().notes() );
      // verifyCfg
      p.setValobj( "verifyCfg_nonExistingTagIds", item.verifyCfg().nonExistingTagIds() );
      p.setBool( "verifyCfg_isMaxChildScenesCountWarningDisabled",
          IEpVerifyCfg.IS_MAX_CHILD_SCENES_COUNT_WARNING_DISABLED.getValue( item.verifyCfg().params() ).asBool() );
      p.setBool( "verifyCfg_isMinSceneDurationWarningDisabled",
          IEpVerifyCfg.IS_MIN_SCENE_DURATION_WARNING_DISABLED.getValue( item.verifyCfg().params() ).asBool() );
      // noteLine
      for( int i = 1; i <= item.noteLine().listMarks().size(); i++ ) {
        MarkNote k = item.noteLine().listMarks().get( i - 1 );
        IOptionSetEdit ops = new OptionSet();
        ops.setStr( "inStart", HmsUtils.hhhmmss( k.in().start() ) );
        ops.setStr( "inEnd", HmsUtils.hhhmmss( k.in().end() ) );
        ops.setStr( "text", k.marker() );
        ops.setStr( "camId", k.cameraId() );
        ops.setStr( "frame", FrameKeeper.KEEPER.ent2str( k.frame() ) );
        p.setValobj( "NoteLineMark_" + i, ops );
      }
      // tagLine
      for( int i = 1; i <= item.tagLine().usedTagIds().size(); i++ ) {
        String tagId = item.tagLine().usedTagIds().get( i - 1 );
        IOptionSetEdit ops = new OptionSet();
        IIntMap<Secint> marks = item.tagLine().marks( tagId );
        for( int j = 1; j <= marks.size(); j++ ) {
          Secint in = marks.values().get( j - 1 );
          IOptionSetEdit opsMarks = new OptionSet();
          opsMarks.setStr( "s", HmsUtils.hhhmmss( in.start() ) );
          opsMarks.setStr( "e", HmsUtils.hhhmmss( in.end() ) );
          ops.setValobj( "Secint_" + j, opsMarks );
        }
        p.setValobj( "TagLineMark_" + tagId, ops );
      }
      // planesLine
      for( int i = 1; i <= item.planesLine().marksList().size(); i++ ) {
        IOptionSetEdit ops = new OptionSet();
        MarkPlaneGuide k = item.planesLine().marksList().get( i - 1 );
        ops.setStr( "inStart", HmsUtils.hhhmmss( k.in().start() ) );
        ops.setStr( "inEnd", HmsUtils.hhhmmss( k.in().end() ) );
        PlaneGuide g = k.marker();
        ops.setStr( "camId", g.cameraId() );
        ops.setStr( "name", g.name() );
        ops.setBool( "isNaturallyLong", g.isNaturallyLong() );
        ops.setStr( "frame", FrameKeeper.KEEPER.ent2str( g.frame() ) );
        p.setValobj( "PlanesLineMark_" + i, ops );
      }
      // story
      IOptionSet ops = makeScene( item.story() );
      p.setValobj( "Story", ops );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Episodes", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportGazes( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting gazes... " );
    // read unit
    IUnitGazes unitGazes = new UnitGazes();
    aProj3.registerUnit( UNITID_GAZES, unitGazes, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( IGaze item : unitGazes.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStripar( p, item );
      p.setStr( "rating", item.rating().id() );
      p.setStr( "date", item.incidentDate().toString() );
      p.setStr( "place", item.place() );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Gazes", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportSongs( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting songs... " );
    // read unit
    IUnitSongs unitSongs = new UnitSongs();
    aProj3.registerUnit( UNITID_SONGS, unitSongs, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( ISong item : unitSongs.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStripar( p, item );
      p.setStr( "songFilePath", item.filePath() );
      p.setStr( "duration", HmsUtils.hhhmmss( item.duration() ) );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Songs", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static IOptionSet makeVisumple( Visumple aVisumple ) {
    IOptionSetEdit p = new OptionSet();
    p.setStr( "filePath", aVisumple.filePath() );
    p.setValobj( "params", aVisumple.params() );
    return p;
  }

  private static IOptionSet makeStir( IStir aStir ) {
    IOptionSetEdit p = new OptionSet();
    p.setStr( TSID_NAME, aStir.name() );
    p.setStr( TSID_DESCRIPTION, aStir.description() );
    p.setStr( "duration", HmsUtils.mmmss( aStir.duration() ) );
    p.setStr( "thumbFilePath", aStir.thumbFilePath() );
    for( int i = 1; i <= aStir.visumples().size(); i++ ) {
      Visumple v = aStir.visumples().get( i - 1 );
      IOptionSet ops = makeVisumple( v );
      p.setValobj( "Visumple_" + i, ops );
    }
    return p;
  }

  private static IOptionSet makeTrack( ITrack aTrack ) {
    IOptionSetEdit p = new OptionSet();
    p.setStr( "songId", aTrack.songId() );
    p.setStr( "s", HmsUtils.mmss( aTrack.interval().start() ) );
    p.setStr( "e", HmsUtils.mmss( aTrack.interval().end() ) );
    return p;
  }

  private static void exportPleps( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting pleps... " );
    // read unit
    IUnitPleps unitPleps = new UnitPleps();
    aProj3.registerUnit( UNITID_PLEPS, unitPleps, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( IPlep item : unitPleps.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      p.setStr( "place", item.info().place() );
      // stirs
      for( int i = 1; i <= item.stirs().size(); i++ ) {
        IStir stir = item.stirs().get( i - 1 );
        IOptionSet ops = makeStir( stir );
        p.setValobj( "Stir_" + i, ops );
      }
      // tracks
      for( int i = 1; i <= item.tracks().size(); i++ ) {
        ITrack track = item.tracks().get( i - 1 );
        IOptionSet ops = makeTrack( track );
        p.setValobj( "Track_" + i, ops );
      }
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Pleps", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportTodos( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting todos... " );
    // read unit
    IUnitTodos unitTodos = new UnitTodos();
    aProj3.registerUnit( UNITID_TODOS, unitTodos, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( ITodo item : unitTodos.todos() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      p.setLong( "id", item.id() );
      p.setTime( "creationTime", item.creationTime() );
      p.setStr( "priority", item.priority().id() );
      p.setStr( "text", item.text() );
      p.setStr( "note", item.note() );
      p.setBool( "isDone", item.isDone() );
      p.setValobj( "relatedTodoIds", new LongArrayList( item.relatedTodoIds() ) );
      p.setBool( "reminderIsActive", item.reminder().isActive() );
      p.setTime( "reminderTimestamp", item.reminder().remindTimestamp() );
      p.setStr( "reminderMessage", item.reminder().message() );
      // fulfill stages
      for( int i = 1; i <= item.fulfilStages().size(); i++ ) {
        IFulfilStage fs = item.fulfilStages().get( i - 1 );
        IOptionSetEdit ops = new OptionSet();
        ops.setTime( "when", fs.when() );
        ops.setStr( TSID_NAME, fs.name() );
        ops.setStr( TSID_DESCRIPTION, fs.description() );
        p.setValobj( "fulfillStage_" + i, ops );
      }
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Todos", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportTrailers( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting trailers... " );
    // read unit
    IUnitTrailers unitTrailers = new UnitTrailers();
    aProj3.registerUnit( UNITID_TRAILERS, unitTrailers, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( Trailer item : unitTrailers.tsm().items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      p.setStr( "plannedDuration", HmsUtils.hhhmmss( item.info().plannedDuration() ) );
      for( int i = 1; i <= item.chunks().size(); i++ ) {
        Chunk ch = item.chunks().get( i - 1 );
        IOptionSetEdit ops = new OptionSet();
        ops.setStr( "epId", ch.episodeId() );
        ops.setStr( "camId", ch.cameraId() );
        ops.setStr( "name", ch.name() );
        ops.setStr( "inStart", HmsUtils.hhhmmss( ch.interval().start() ) );
        ops.setStr( "inEnd", HmsUtils.hhhmmss( ch.interval().end() ) );
        ops.setStr( "frame", FrameKeeper.KEEPER.ent2str( ch.frame() ) );
        p.setValobj( "chunk_" + i, ops );
      }
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Trailers", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
    aSw.writeEol();
    // SensibleTrailerInfo
    p( "Exporting sensible trailer infos... " );
    llItems.clear();
    for( SensibleTrailerInfo item : unitTrailers.sensibleTrailerInfos() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      // ======
      llItems.add( p );
    }
    writeKeywordHeader( aSw, "SensibleTrailerInfos", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static IOptionSet makeInquiryItem( InquiryItem aItem ) {
    IOptionSetEdit p = new OptionSet();
    for( EPqSingleFilterKind k : aItem.fpMap().keys() ) {
      ITsSingleFilterParams sfp = aItem.fpMap().getByKey( k );
      p.setBool( k.id() + "_isInverted", aItem.isInverted( k ) );
      p.setStr( k.id() + "_filterTypeId", sfp.typeId() );
      p.setValobj( k.id() + "_filterParams", sfp.params() );
    }
    return p;
  }

  private static void exportUnitExplorer( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting explorer inquiries... " );
    // read unit
    IUnitExplorer unit = new UnitExplorer();
    aProj3.registerUnit( AddonPsx24Explorer.UNITID_EXPLORER, unit, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( Inquiry item : unit.items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilCopyStridable( p, item );
      for( int i = 1; i <= item.items().size(); i++ ) {
        InquiryItem inqit = item.items().get( i - 1 );
        IOptionSet ops = makeInquiryItem( inqit );
        p.setValobj( "InquiryItem_" + i, ops );
      }
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Keywords", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportKeywordManager( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting keywords... " );
    // read unit
    IKeywordManager unitKwMan = new KeywordManager();
    aProj3.registerUnit( QuantKeywordManager.UNITID_KEYWORDS, unitKwMan, true );
    // prepare data
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    for( String item : unitKwMan.listAll() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      p.setStr( "keyword", item );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "Keywords", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportNbNotebook( ITsProject aProj3, IStrioWriter aSw ) {
    p( "Exporting NbNotebookCategories... " );
    // read unit
    INbNotebook unit = new NbNotebook();
    aProj3.registerUnit( AddonPsx24Catnote.UNITID_NOTEBOOK, unit, true );
    IListEdit<IOptionSetEdit> llItems = new ElemArrayList<>();
    // categories
    for( INbCategory item : unit.rootCategory().items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilFullCopyStripar( p, item );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "NbNotebookCategories", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
    // notes
    p( "Exporting NbNotebookNotes... " );
    llItems.clear();
    for( INbNote item : unit.notes().items() ) {
      IOptionSetEdit p = new OptionSet();
      // ======
      utilFullCopyStripar( p, item );
      // ======
      llItems.add( p );
    }
    // write data
    writeKeywordHeader( aSw, "NbNotebookNotes", true );
    writeOpSetList( aSw, llItems );
    pl( "Done %d", Integer.valueOf( llItems.size() ) );
  }

  private static void exportProj3( ITsProject aProj3, IStrioWriter aSw ) {
    //// units from QuantPsx3Project
    // cameras
    exportCameras( aProj3, aSw );
    aSw.writeEol();
    // source videos
    exportSourceVideos( aProj3, aSw );
    aSw.writeEol();
    // tags
    exportTags( aProj3, aSw );
    aSw.writeEol();
    // episodes
    exportEpisodes( aProj3, aSw );
    aSw.writeEol();
    // gazes
    exportGazes( aProj3, aSw );
    aSw.writeEol();
    // songs
    exportSongs( aProj3, aSw );
    aSw.writeEol();
    // 07 pleps
    exportPleps( aProj3, aSw );
    aSw.writeEol();
    // todos
    exportTodos( aProj3, aSw );
    aSw.writeEol();
    // trailers
    exportTrailers( aProj3, aSw );
    aSw.writeEol();
    //// units not listed in QuantPsx3Project
    // IUnitExplorer
    exportUnitExplorer( aProj3, aSw );
    aSw.writeEol();
    // IKeywordManager
    exportKeywordManager( aProj3, aSw );
    aSw.writeEol();
    // INbNotebook
    exportNbNotebook( aProj3, aSw );
    aSw.writeEol();
    aSw.writeEol();
  }

  // written list may be read by OptionSetKeeper.KEEPER.readColl( sr )
  private static void writeOpSetList( IStrioWriter aSw, IList<IOptionSetEdit> aOpSetList ) {
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    if( aOpSetList.isEmpty() ) {
      aSw.writeChar( CHAR_ARRAY_END );
      return;
    }
    aSw.incNewLine();
    for( IOptionSetEdit p : aOpSetList ) {
      OPSET_KEEPER.write( aSw, p );
      if( p != aOpSetList.last() ) {
        aSw.writeSeparatorChar();
        aSw.writeEol();
      }
    }
    aSw.decNewLine();
    aSw.writeChar( CHAR_ARRAY_END );
  }

  private static void initKeepers() {
    TsValobjUtils.registerKeeper( FramesList.KEEPER_ID, FramesList.KEEPER );
    TsValobjUtils.registerKeeper( FrameKeeper.KEEPER_ID, FrameKeeper.KEEPER );
    TsValobjUtils.registerKeeper( ERating.KEEPER_ID, ERating.KEEPER );
  }

  // ------------------------------------------------------------------------------------
  // Run application
  //

  public static void main( String[] args ) {
    pl( "Export PRISEX project v3 to common-readable form" );
    // prepare
    initKeepers();
    ProgramArgs progArgs = new ProgramArgs( args );
    String proj3FilePath = progArgs.getArgValue( CMDLINE_ARG_PROJ3_FILE_PATH, DEFAULT_PROJ3_FILE_PATH );
    String outFilePath = progArgs.getArgValue( CMDLINE_ARG_OUT_FILE_PATH, DEFAULT_EXPORTED_FILE_PATH );
    File proj3File = new File( proj3FilePath );
    File outFile = new File( outFilePath );
    pl( "Project 3 file: %s", proj3File.getAbsolutePath() );
    pl( "Output file:    %s", outFile.getAbsolutePath() );
    // open project 3
    ITsProject proj3 = new TsProject( PROJECT_FILE_FORMAT_INFO );
    ITsProjectFileBound bound = new TsProjectFileBound( proj3, IOptionSet.NULL );
    bound.open( proj3File );
    pl( "Project opened." );
    // export
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( outFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      exportProj3( proj3, sw );
    }
    catch( Exception ex ) {
      errl( "%s - %s", ex.getClass().getSimpleName(), ex.getMessage() );
      ex.printStackTrace();
      nl();
      System.exit( 1 );
    }
    pl( "Exported OK." );
    nl();
  }

}
