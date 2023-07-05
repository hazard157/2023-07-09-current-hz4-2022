package com.hazard157.psx24.core.m5.svin;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.m5.svin.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.util.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.playmenu.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * {@link IM5CollectionPanel} implementation for {@link SvinM5Model}.
 *
 * @author hazard157
 */
public class SvinM5Mpc
    extends MultiPaneComponentModown<Svin> {

  static final String AID_SHOW_ALL_SVINS_IN_FRAMES_LIST = PSX_ID + 1;

  static final ITsActionDef AI_SHOW_ALL_SVINS_IN_FRAMES_LIST = TsActionDef.ofPush2( AID_SHOW_ALL_SVINS_IN_FRAMES_LIST,
      STR_N_SHOW_ALL_SVINS_IN_FRAMES_LIST, STR_D_SHOW_ALL_SVINS_IN_FRAMES_LIST, ICON_PSX_ONE_ICON_PER_EPIN );

  final IPlayMenuSupport playMenuSupport;

  static final String NKID_SVIN      = "Svin";     //$NON-NLS-1$
  static final String NKID_EP_ID     = "EpId";     //$NON-NLS-1$
  static final String NKID_CAM_ID    = "CamId";    //$NON-NLS-1$
  static final String NKID_EP_SECINT = "EpSecint"; //$NON-NLS-1$

  static final ITsNodeKind<Svin>   NK_SVIN   = new TsNodeKind<>( NKID_SVIN, Svin.class, false );
  static final ITsNodeKind<String> NK_EP_ID  = new TsNodeKind<>( NKID_EP_ID, String.class, true );
  static final ITsNodeKind<String> NK_CAM_ID = new TsNodeKind<>( NKID_CAM_ID, String.class, true );

  static final ITsNodeKind<Svin> NK_EP_SECINT                 = new TsNodeKind<>( NKID_EP_SECINT, Svin.class, true );
  static final String            EP_SECINT_ENTITY_SVIN_CAM_ID = "EpSecintEntityCameraId";                            //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - items provider or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - LM or <code>null</code>
   */
  public SvinM5Mpc( ITsGuiContext aContext, IM5Model<Svin> aModel, IM5ItemsProvider<Svin> aItemsProvider,
      IM5LifecycleManager<Svin> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    IMultiPaneComponentConstants.OPDEF_DBLCLICK_ACTION_ID.setValue( tsContext().params(), avStr( AID_PLAY ) );
    playMenuSupport = tsContext().get( IPlayMenuSupport.class );
    TreeModeInfo<Svin> tmiByEpisode = new TreeModeInfo<>( TreeMakerByEpisode.MODE_ID, STR_N_TMM_BY_EPISODE,
        STR_D_TMM_BY_EPISODE, null, new TreeMakerByEpisode( tsContext() ) );
    treeModeManager().addTreeMode( tmiByEpisode );
    //
    TreeModeInfo<Svin> tmiByEpSecint = new TreeModeInfo<>( TreeMakerByEpSecint.MODE_ID, STR_N_TMM_BY_EP_SECINT,
        STR_D_TMM_BY_EP_SECINT, null, new TreeMakerByEpSecint( tsContext() ) );
    treeModeManager().addTreeMode( tmiByEpSecint );
    //
    IUnitEpisodes ue = aContext.get( IUnitEpisodes.class );
    TreeModeInfo<Svin> tmiByCamera = new TreeModeInfo<>( TreeMakerByCamera.MODE_ID, STR_N_TMM_BY_CAMERA,
        STR_D_TMM_BY_CAMERA, null, new TreeMakerByCamera( ue ) );
    treeModeManager().addTreeMode( tmiByCamera );
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    if( !aActs.isEmpty() ) {
      aActs.add( ACDEF_SEPARATOR );
    }
    aActs.add( new TsActionDef( AI_PLAY_MENU ) );
    aActs.add( ACDEF_SEPARATOR );
    aActs.add( AI_SHOW_ALL_SVINS_IN_FRAMES_LIST );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doCreateTreeColumns() {
    for( IM5FieldDef<Svin, ?> fdef : model().fieldDefs() ) {
      if( (fdef.flags() & M5FF_COLUMN) != 0 ) {
        switch( fdef.id() ) {
          case PsxM5IntervalFieldDef.FID_INTERVAL: {
            IM5Getter<Svin, Secint> getter = new IM5Getter<>() {

              @Override
              public String getName( Svin aItem ) {
                if( aItem == null || aItem.cameraId().equals( EP_SECINT_ENTITY_SVIN_CAM_ID ) ) {
                  return TsLibUtils.EMPTY_STRING;
                }
                return aItem.interval().toString();
              }

              @Override
              public Secint getValue( Svin aEntity ) {
                return aEntity.interval();
              }
            };
            tree().columnManager().add( fdef.id(), getter );
            break;
          }
          case PsxM5CameraIdFieldDef.FID_CAMERA_ID: {
            IM5Getter<Svin, String> getter = new IM5Getter<>() {

              @Override
              public String getName( Svin aItem ) {
                if( aItem == null || aItem.cameraId().equals( EP_SECINT_ENTITY_SVIN_CAM_ID ) ) {
                  return TsLibUtils.EMPTY_STRING;
                }
                return aItem.cameraId();
              }

              @Override
              public String getValue( Svin aEntity ) {
                return aEntity.cameraId();
              }
            };
            tree().columnManager().add( fdef.id(), getter );
            break;
          }
          default: {
            tree().columnManager().add( fdef.id() );
            break;
          }
        }
      }
    }
  }

  @Override
  protected IMpcSummaryPane<Svin> doCreateSummaryPane() {
    @SuppressWarnings( "boxing" )
    MpcSummaryPaneMessage.IMessageProvider<Svin> mp =
        ( aOwner, aSelectedNode, aSelEntity, aAllItems, aFilteredItems ) -> {
          String msg;
          IStringListEdit epAllIds = new StringArrayList( 128 );
          IStringListEdit epFilteredIds = new StringArrayList( 128 );
          int nAll = aAllItems.size();
          int dAll = 0;
          for( Svin s1 : aAllItems ) {
            dAll += s1.interval().duration();
            if( !epAllIds.hasElem( s1.episodeId() ) ) {
              epAllIds.add( s1.episodeId() );
            }
          }
          if( aOwner.tree().filterManager().isFiltered() ) {
            IList<Svin> fileredItems = aOwner.tree().filterManager().items();
            int nFiltered = aAllItems.size();
            int dFiltered = 0;
            for( Svin s2 : fileredItems ) {
              dFiltered += s2.interval().duration();
              if( !epFilteredIds.hasElem( s2.episodeId() ) ) {
                epFilteredIds.add( s2.episodeId() );
              }
            }
            msg = String.format( FMT_FILTERED_SUMMARY, nAll, nFiltered, HmsUtils.hhmmss( dAll ),
                HmsUtils.hhmmss( dFiltered ), epAllIds.size(), epFilteredIds.size() );
          }
          else {
            msg = String.format( FMT_NON_FILTER_SUMMARY, nAll, HmsUtils.hhmmss( dAll ), epAllIds.size() );
          }
          return msg;
        };
    MpcSummaryPaneMessage<Svin> sp = new MpcSummaryPaneMessage<>( this, mp );
    return sp;
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    switch( aActionId ) {
      case AID_SHOW_ALL_SVINS_IN_FRAMES_LIST: {
        showOneFramePerSvinInFramesList();
        break;
      }
      case AID_PLAY: {
        Svin sel = selectedItem();
        if( sel != null ) {
          IPrisexService prisexService = tsContext().get( IPrisexService.class );
          prisexService.playEpisodeVideo( sel );
        }
        break;
      }
      default:
        break;
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, Svin aSel ) {
    toolbar().setActionEnabled( AID_PLAY, aIsSel );
    updatePlayMenu();
  }

  void showOneFramePerSvinInFramesList() {
    // check preconditions
    ICurrentFramesListService currentFramesListService = tsContext().get( ICurrentFramesListService.class );
    IPsxSelectedSvinsService selectedSvinsService = tsContext().get( IPsxSelectedSvinsService.class );
    IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
    if( unitEpisodes == null ) {
      currentFramesListService.setCurrent( IList.EMPTY );
      selectedSvinsService.setSvin( null );
      return;
    }
    // prepare map of SVINs lists by episodes
    IMapEdit<String, IListBasicEdit<Svin>> map = new SortedElemMap<>();
    IListEdit<Svin> svins = new ElemArrayList<>();
    for( Svin svin : tree().items() ) {
      IListBasicEdit<Svin> ll = map.findByKey( svin.episodeId() );
      if( ll == null ) {
        ll = new SortedElemLinkedBundleList<>();
        map.put( svin.episodeId(), ll );
      }
      ll.add( svin );
      svins.add( svin );
    }
    // prepare list of displayed frames
    boolean isSinglePerEpisode = Objects.equals( treeModeManager().currModeId(), TreeMakerByEpisode.MODE_ID );
    IListBasicEdit<IFrame> toShow = new SortedElemLinkedBundleList<>();
    for( String epId : map.keys() ) {
      IList<Svin> ll = map.getByKey( epId );
      if( isSinglePerEpisode ) {
        Svin chunk = ll.get( ll.size() / 2 );
        IEpisode e = unitEpisodes.items().findByKey( chunk.episodeId() );
        if( e != null ) {
          IScene scene = e.story().findBestSceneFor( chunk.interval(), true );
          toShow.add( scene.frame() );
        }
      }
      else {
        for( Svin chunk : ll ) {
          IEpisode e = unitEpisodes.items().findByKey( chunk.episodeId() );
          if( e != null ) {
            IScene scene = e.story().findBestSceneFor( chunk.interval(), true );
            toShow.add( scene.frame() );
          }
        }
      }
    }
    setSelectedItem( null );
    currentFramesListService.setCurrent( toShow );
    selectedSvinsService.setSvins( svins );
  }

  // void showAllSvinsInFramesList_old() {
  // // check preconditions
  // ICurrentFramesListService cfls = tsContext().get( ICurrentFramesListService.class );
  // IPsxSelectedSvinsService sss = tsContext().get( IPsxSelectedSvinsService.class );
  // IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
  // if( unitEpisodes == null ) {
  // cfls.setCurrent( IList.EMPTY );
  // sss.setSvin( null );
  // return;
  // }
  // // подготовим карту по эпизодам
  // IMapEdit<String, IListBasicEdit<Svin>> map = new SortedElemMap<>();
  // IListEdit<Svin> svins = new ElemArrayList<>();
  // for( Svin svin : tree().items() ) {
  // IListBasicEdit<Svin> ll = map.findByKey( svin.episodeId() );
  // if( ll == null ) {
  // ll = new SortedElemLinkedBundleList<>();
  // map.put( svin.episodeId(), ll );
  // }
  // ll.add( svin );
  // svins.add( svin );
  // }
  // // составим спиок кадров для отображения
  // boolean isSinglePerEpisode = Objects.equals( treeModeManager().currModeId(), TreeMakerByEpisode.MODE_ID );
  // IListBasicEdit<IFrame> toShow = new SortedElemLinkedBundleList<>();
  // for( String epId : map.keys() ) {
  // IList<Svin> ll = map.getByKey( epId );
  // if( isSinglePerEpisode ) {
  // Svin chunk = ll.get( ll.size() / 2 );
  // IEpisode e = unitEpisodes.items().findByKey( chunk.episodeId() );
  // if( e != null ) {
  // IScene scene = e.story().findBestSceneFor( chunk.interval(), true );
  // toShow.add( scene.frame() );
  // }
  // }
  // else {
  // for( Svin chunk : ll ) {
  // IEpisode e = unitEpisodes.items().findByKey( chunk.episodeId() );
  // if( e != null ) {
  // IScene scene = e.story().findBestSceneFor( chunk.interval(), true );
  // toShow.add( scene.frame() );
  // }
  // }
  // }
  // }
  // setSelectedItem( null );
  // cfls.setCurrent( toShow );
  // sss.setSvins( svins );
  // }

  private void updatePlayMenu() {
    Svin svin = selectedItem();
    if( svin != null ) {
      final IPlayMenuParamsProvider ppp = new IPlayMenuParamsProvider() {

        @Override
        public int spotlightSec() {
          IFrame f = svin.frame();
          if( f.isDefined() ) {
            return f.secNo();
          }
          return -1;
        }

        @Override
        public Svin playParams() {
          return svin;
        }

      };
      if( playMenuSupport != null ) {
        toolbar().setActionMenu( AID_PLAY, playMenuSupport.getPlayMenuCreator( tsContext(), ppp ) );
      }
    }
    else {
      toolbar().setActionMenu( AID_PLAY, null );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns a list of SVINs associated with the currently selected node.
   * <p>
   * For a normal node containing {@link Svin}, it is returned as a single element of the list. For group nodes, returns
   * a list of child SVINs.
   * <p>
   * If there is no currently selected node, returns an empty list.
   *
   * @return {@link IList}&lt;{@link Svin}&gt; - list of SVINs, never is <code>null</code>
   */
  public IList<Svin> getSelectedNodeSvins() {
    ITsNode selNode = (ITsNode)tree().console().selectedNode();
    if( selNode == null ) {
      return IList.EMPTY;
    }
    switch( selNode.kind().id() ) {
      case NKID_SVIN:
      case M5DefaultTreeMaker.KIND_ID:
        return new SingleItemList<>( (Svin)selNode.entity() );
      case NKID_EP_ID: {
        IListEdit<Svin> ll = new ElemArrayList<>();
        for( ITsNode n : selNode.childs() ) {
          if( n.kind() == NK_SVIN ) {
            ll.add( (Svin)n.entity() );
          }
        }
        return ll;
      }
      case NKID_EP_SECINT: {
        IListEdit<Svin> ll = new ElemArrayList<>();
        for( ITsNode n : selNode.childs() ) {
          if( n.kind() == NK_SVIN ) {
            ll.add( (Svin)n.entity() );
          }
        }
        return ll;
      }
      case NKID_CAM_ID: {
        IListEdit<Svin> ll = new ElemArrayList<>();
        for( ITsNode n : selNode.childs() ) {
          if( n.kind() == NK_SVIN ) {
            ll.add( (Svin)n.entity() );
          }
        }
        return ll;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
