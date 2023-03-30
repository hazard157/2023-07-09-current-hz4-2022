package com.hazard157.psx24.core.glib.frlstviewer_ep;

import static com.hazard157.psx24.core.glib.frlstviewer_ep.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.incub.acsupart.*;
import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Управление режимом просмотра в виде дерева в {@link EpisodeFramesListViewer}.
 *
 * @author goga
 */
class EflvTreeModeManager
    extends TreeModeManager<IFrame>
    implements ITsActionProcessor {

  /**
   * Группировщик в дерево кадров по минутам.
   */
  final ITsTreeMaker<IFrame> tmByMinutes = new ITsTreeMaker<>() {

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IFrame> aItems ) {
      IMapEdit<Integer, DefaultTsNode<Integer>> map = new ElemMap<>();
      for( IFrame f : aItems ) {
        if( !f.isDefined() ) {
          continue;
        }
        Integer minute = Integer.valueOf( f.secNo() / 60 );
        DefaultTsNode<Integer> rNode = map.findByKey( minute );
        if( rNode == null ) {
          rNode = new DefaultTsNode<>( NK_TIME, aRootNode, minute );
          rNode.setName( HmsUtils.mmss( minute.intValue() * 60 ) );
          map.put( minute, rNode );
        }
        rNode.addNode( new DefaultTsNode<>( NK_FRAME, rNode, f ) );
      }
      return (IList)map.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_FRAME;
    }
  };

  /**
   * Группировщик в дерево кадров по 20 сек.
   */
  final ITsTreeMaker<IFrame> tmBy20Sec = new ITsTreeMaker<>() {

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IFrame> aItems ) {
      IMapEdit<Integer, DefaultTsNode<Integer>> map = new ElemMap<>();
      for( IFrame f : aItems ) {
        if( !f.isDefined() ) {
          continue;
        }
        Integer noOf20Sec = Integer.valueOf( f.secNo() / 20 );
        DefaultTsNode<Integer> rNode = map.findByKey( noOf20Sec );
        if( rNode == null ) {
          rNode = new DefaultTsNode<>( NK_TIME, aRootNode, noOf20Sec );
          rNode.setName( HmsUtils.mmss( noOf20Sec.intValue() * 20 ) );
          map.put( noOf20Sec, rNode );
        }
        rNode.addNode( new DefaultTsNode<>( NK_FRAME, rNode, f ) );
      }
      return (IList)map.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_FRAME;
    }
  };

  final ITsTreeMaker<IFrame> tmByScenes = new ITsTreeMaker<>() {

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IFrame> aItems ) {
      IEpisode episode = getSingleEpisodeFromFramesList( aItems );
      if( episode == null ) {
        return IList.EMPTY;
      }
      IStory story = episode.story();
      IListEdit<DefaultTsNode<IScene>> rootNodes = new ElemArrayList<>();
      IMapEdit<IScene, DefaultTsNode<IScene>> map = new ElemMap<>();
      // создаем узлы для ВСЕХ сцен эпизода
      for( IScene s : story.childScenes() ) {
        DefaultTsNode<IScene> n = new DefaultTsNode<>( NK_SCENE, aRootNode, s );
        rootNodes.add( n );
        map.put( s, n );
        addChilds( n, map );
      }

      // добавим кадры в соответсвующие сценам узлы
      DefaultTsNode<IScene> unscenedNode = null;
      for( IFrame f : aItems ) {
        if( !f.isDefined() ) {
          continue;
        }
        IScene s = story.findBestSceneFor( new Secint( f.secNo(), f.secNo() ), true );
        // если кадр вне сцены, добавим его в специальный узел "кадры вне
        // сцен"
        if( s == null || s == story ) {
          if( unscenedNode == null ) {
            unscenedNode = new DefaultTsNode<>( NK_SCENE, aRootNode, null );
            unscenedNode.setName( STR_N_UNSCENED_NODE );
            rootNodes.insert( 0, unscenedNode );
          }
          DefaultTsNode<IFrame> fnode = new DefaultTsNode<>( NK_FRAME, unscenedNode, f );
          unscenedNode.addNode( fnode );
          continue;
        }
        DefaultTsNode<IScene> sceneNode = map.getByKey( s );
        DefaultTsNode<IFrame> fnode = new DefaultTsNode<>( NK_FRAME, sceneNode, f );
        sceneNode.addNode( fnode );
      }
      return (IList)rootNodes;
    }

    private final void addChilds( DefaultTsNode<IScene> aNode, IMapEdit<IScene, DefaultTsNode<IScene>> aMap ) {
      for( IScene s : aNode.entity().childScenes() ) {
        DefaultTsNode<IScene> n = new DefaultTsNode<>( NK_SCENE, aNode, s );
        aMap.put( s, n );
        addChilds( n, aMap );
        aNode.addNode( n );
      }
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_FRAME;
    }
  };

  static final IDataDef LAST_TREE_MODE_ID = DataDef.create( "LastTreeModeId", STRING, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  static final String TMID_BY_MINUTS = "ByMinutes"; //$NON-NLS-1$
  static final String TMID_BY_20SEC  = "By20Sec";   //$NON-NLS-1$
  static final String TMID_BY_SCENES = "ByScenes";  //$NON-NLS-1$
  static final String TMID_BY_NOTES  = "ByNotes";   //$NON-NLS-1$

  static final ITsNodeKind<Integer> NK_TIME  = new TsNodeKind<>( "Time", Integer.class, true );  //$NON-NLS-1$
  static final ITsNodeKind<IScene>  NK_SCENE = new TsNodeKind<>( "Scene", IScene.class, true );  //$NON-NLS-1$
  static final ITsNodeKind<String>  NK_NOTE  = new TsNodeKind<>( "Notes", String.class, true );  //$NON-NLS-1$
  static final ITsNodeKind<IFrame>  NK_FRAME = new TsNodeKind<>( "Frame", IFrame.class, false ); //$NON-NLS-1$

  final ITsTreeMaker<IFrame> tmByNotes = new ITsTreeMaker<>() {

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IFrame> aItems ) {
      IEpisode episode = getSingleEpisodeFromFramesList( aItems );
      if( episode == null ) {
        return IList.EMPTY;
      }
      IStringMapEdit<DefaultTsNode<String>> rootsMap = new StringMap<>();
      // создадим карту всех узлов, включая последний узел - кадры вне заметок
      INoteLine noteLine = episode.noteLine();
      for( Secint in : noteLine.marksMap().keys() ) {
        String note = noteLine.marksMap().getByKey( in );
        DefaultTsNode<String> noteNode = new DefaultTsNode<>( NK_NOTE, aRootNode, note );
        noteNode.setName( in.toString() + " - " + note ); //$NON-NLS-1$
        rootsMap.put( note, noteNode );
      }
      DefaultTsNode<String> unnotedFrames = new DefaultTsNode<>( NK_NOTE, aRootNode, STR_N_UNNOTES_NODE );
      rootsMap.put( STR_N_UNNOTES_NODE, unnotedFrames );
      // разложим кадры по узлам
      for( IFrame f : aItems ) {
        // найдем интервал заметки, к которой относится кадр (или null)
        Secint noteInterval = null;
        for( Secint in : noteLine.marksMap().keys() ) {
          if( in.contains( f.secNo() ) ) {
            noteInterval = in;
            break;
          }
        }
        // если кадр вне заметок, добавим в специальный узел
        if( noteInterval == null ) {
          DefaultTsNode<IFrame> frameNode = new DefaultTsNode<>( NK_FRAME, unnotedFrames, f );
          unnotedFrames.addNode( frameNode );
          continue;
        }
        // добавим в узел, соответствующий заметке
        String note = noteLine.marksMap().getByKey( noteInterval );
        DefaultTsNode<String> noteNode = rootsMap.getByKey( note );
        DefaultTsNode<IFrame> frameNode = new DefaultTsNode<>( NK_FRAME, noteNode, f );
        noteNode.addNode( frameNode );
      }

      return (IList)rootsMap.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_FRAME;
    }
  };

  final IUnitEpisodes ue;
  final Shell         shell;

  public EflvTreeModeManager( ITsGuiContext aTsContext ) {
    super( true );
    ue = aTsContext.get( IUnitEpisodes.class );
    shell = aTsContext.get( Shell.class );
    addTreeMode( new TreeModeInfo<>( TMID_BY_MINUTS, STR_N_TM_BY_MINUTES, STR_N_TM_BY_MINUTES, null, tmByMinutes ) );
    addTreeMode( new TreeModeInfo<>( TMID_BY_20SEC, STR_N_TM_BY_20SEC, STR_N_TM_BY_20SEC, null, tmBy20Sec ) );
    addTreeMode( new TreeModeInfo<>( TMID_BY_SCENES, STR_N_TM_BY_SCENES, STR_N_TM_BY_SCENES, null, tmByScenes ) );
    addTreeMode( new TreeModeInfo<>( TMID_BY_NOTES, STR_N_TM_BY_NOTES, STR_N_TM_BY_NOTES, null, tmByNotes ) );
    // TODO по камерам
    // TODO по планам DVD
    // TODO по заметкам
    // TODO по ярлыкам
  }

  /**
   * Возвращает единственный эпизод, к которому относятся кадры (для группираторов дерева).
   * <p>
   * Если аргумент пустая коллекция, просто возвращает <code>null</code>.
   * <p>
   * Если в аргументе содержатся кадры более одного эпизода, выдает соответствующий диалог с сообщением и возвращает
   * <code>null</code>.
   * <p>
   * Если в аргументе содержатся тольео неопределенные кадры, или кадры с несуществующих эпизодов, выдает
   * соответствующий диалог с сообщением и возвращает <code>null</code>.
   *
   * @param aItems {@link IList}&lt;{@link IFrame}&gt; - анализируемые кадры
   * @return {@link IEpisode} - единственный эпизод или <code>null</code>
   */
  final IEpisode getSingleEpisodeFromFramesList( IList<IFrame> aItems ) {
    TsNullArgumentRtException.checkNull( aItems );
    if( aItems.isEmpty() ) {
      return null;
    }
    // проверим, что все кадры от одного эпизода
    IEpisode episode = null;
    for( IFrame f : aItems ) {
      IEpisode e = ue.items().findByKey( f.episodeId() );
      if( e != null ) {
        if( episode != null ) {
          if( !episode.id().equals( e.id() ) ) {
            TsDialogUtils.error( shell, FMT_ERR_NOT_ONE_EP_FRAMES, episode.id(), e.id() );
            return null;
          }
        }
        else {
          episode = e;
        }
      }
    }
    // проверим, что в аргументе был хотя бы один кадр существующего эпизода
    if( episode == null ) {
      TsDialogUtils.warn( shell, MSG_WARN_NO_FRAMES_TO_SHOW );
      return null;
    }
    return episode;
  }

  @Override
  public boolean processAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_VIEW_AS_TREE: {
        if( isCurrentTreeMode() ) {
          TreeModeInfo<IFrame> currModeInfo = treeModeInfoes().findByKey( currModeId() );
          TreeModeInfo<IFrame> nextModeInfo = treeModeInfoes().next( currModeInfo );
          if( nextModeInfo == null ) {
            nextModeInfo = treeModeInfoes().first();
          }
          setCurrentMode( nextModeInfo.id() );
        }
        else {
          setCurrentMode( TMID_BY_MINUTS );
        }
        return true;
      }
      case ACTID_VIEW_AS_LIST: {
        setCurrentMode( null );
        return true;
      }
      default:
        return false;
    }
  }

}
