package com.hazard157.prisex24.m5.srcvideo;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.srcvideo.IPsxResources.*;
import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.m5.std.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link SourceVideoM5Model}.
 *
 * @author hazard157
 */
class SourceVideosMpc
    extends MultiPaneComponentModown<ISourceVideo>
    implements IPsxGuiContextable {

  static final ITsNodeKind<Camera> NK_CAM = new TsNodeKind<>( "Cam", //$NON-NLS-1$
      Camera.class, true, ICONID_CAMERA_GENERIC );

  static final ITsNodeKind<IEpisode> NK_EP = new TsNodeKind<>( "Ep", //$NON-NLS-1$
      IEpisode.class, true, null );

  static final ITsNodeKind<ISourceVideo> NK_SV = new TsNodeKind<>( "SV", //$NON-NLS-1$
      ISourceVideo.class, false, ICONID_VIDEO_X_GENERIC );

  final ITsTreeMaker<ISourceVideo> tmByCameras = new ITsTreeMaker<>() {

    @SuppressWarnings( "unchecked" )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISourceVideo> aItems ) {
      IStringMapEdit<DefaultTsNode<Camera>> mapCamNodes = new StringMap<>();
      for( ISourceVideo sv : aItems ) {
        String camId = sv.cameraId();
        DefaultTsNode<Camera> parent = mapCamNodes.findByKey( camId );
        if( parent == null ) {
          Camera cam = camerasUnit.items().getByKey( camId );
          parent = new DefaultTsNode<>( NK_CAM, aRootNode, cam );
          mapCamNodes.put( camId, parent );
        }
        DefaultTsNode<ISourceVideo> svNode = new DefaultTsNode<>( NK_SV, parent, sv );
        parent.addNode( svNode );
      }
      return (IList<ITsNode>)(Object)mapCamNodes.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_SV;
    }
  };

  final ITsTreeMaker<ISourceVideo> tmByEpisodes = new ITsTreeMaker<>() {

    @SuppressWarnings( "unchecked" )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISourceVideo> aItems ) {
      IStringMapEdit<DefaultTsNode<IEpisode>> mapEpisodeNodes = new StringMap<>();
      for( ISourceVideo sv : aItems ) {
        String epId = sv.episodeId();
        DefaultTsNode<IEpisode> parent = mapEpisodeNodes.findByKey( epId );
        if( parent == null ) {
          IEpisode ep = episodesUnit.items().getByKey( epId );
          parent = new DefaultTsNode<>( NK_EP, aRootNode, ep ) {

            @Override
            protected Image doGetImage( EIconSize aIconSize ) {
              TsImage mi = psxService().findThumb( ep.frame(), EThumbSize.SZ180 );
              if( mi != null ) {
                return mi.image();
              }
              return null;
            }
          };
          mapEpisodeNodes.put( epId, parent );
        }
        DefaultTsNode<ISourceVideo> svNode = new DefaultTsNode<>( NK_SV, parent, sv );
        parent.addNode( svNode );
      }
      return (IList<ITsNode>)(Object)mapEpisodeNodes.values();
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_SV;
    }
  };

  final IUnitCameras  camerasUnit;
  final IUnitEpisodes episodesUnit;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  public SourceVideosMpc( ITsGuiContext aContext, IM5Model<ISourceVideo> aModel,
      IM5ItemsProvider<ISourceVideo> aItemsProvider, IM5LifecycleManager<ISourceVideo> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    tree().setThumbSize( apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_LISTS ).asValobj() );
    camerasUnit = tsContext().get( IUnitCameras.class );
    episodesUnit = tsContext().get( IUnitEpisodes.class );
    itemsProvider().genericChangeEventer().addListener( aSource -> refresh() );
  }

  void adjustImageColumnWidth() {
    IM5Column<?> col = tree().columnManager().columns().findByKey( PsxM5FrameFieldDef.FID_FRAME );
    if( col != null ) {
      col.setWidth( tree().thumbSize().size() + 32 );
    }
  }

  @Override
  protected void doAfterCreateControls() {
    treeModeManager().addTreeMode( new TreeModeInfo<>( "ByCameras", //$NON-NLS-1$
        STR_TREE_BY_CAMERAS, STR_TREE_BY_CAMERAS_D, null, tmByCameras ) );
    treeModeManager().addTreeMode( new TreeModeInfo<>( "ByEpisodes", //$NON-NLS-1$
        STR_TREE_BY_EPISODES_D, STR_TREE_BY_EPISODES_D, null, tmByEpisodes ) );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    ISourceVideo sel = selectedItem();
    switch( aActionId ) {
      case ACTID_PLAY:
        if( sel != null ) {
          psxService().playEpisodeVideo(
              new Svin( sel.episodeId(), sel.cameraId(), new Secint( 0, sel.info().duration() - 1 ) ) );
        }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ISourceVideo aSel ) {
    toolbar().setActionEnabled( ACTID_PLAY, aIsSel );
  }

  @Override
  protected void doAfterActionProcessed( String aActionId ) {
    switch( aActionId ) {
      case ACTID_VIEW_AS_LIST:
      case ACTID_VIEW_AS_TREE:
        adjustImageColumnWidth();
        break;
      default:
        break;
    }
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    aActs.add( ACDEF_PLAY );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doFillTree() {
    super.doFillTree();
    adjustImageColumnWidth();
  }

}
