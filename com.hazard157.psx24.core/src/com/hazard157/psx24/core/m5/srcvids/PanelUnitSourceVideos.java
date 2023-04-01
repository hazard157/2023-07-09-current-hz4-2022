package com.hazard157.psx24.core.m5.srcvids;

import static com.hazard157.psx.proj3.IPsxProj3Constants.*;
import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.m5.srcvids.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

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
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.prisex.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * Панель работы с менеджером камер {@link IUnitSourceVideos}, который находится в контексте приложения.
 *
 * @author hazard157
 */
public class PanelUnitSourceVideos
    extends MultiPaneComponentModown<ISourceVideo> {

  /**
   * Размер миниатюр исходных видео в списках.
   */
  public static final IDataDef OP_SVL_THUMB_SIZE = DataDef.create( "psx.sourcevidslist.ThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 ), //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_NAME, STR_N_SVL_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_SVL_THUMB_SIZE //
  );

  static final ITsNodeKind<Camera> NK_CAM = new TsNodeKind<>( "Cam", //$NON-NLS-1$
      Camera.class, true, ICONID_CAMERA_GENERIC );

  static final ITsNodeKind<IEpisode> NK_EP = new TsNodeKind<>( "Ep", //$NON-NLS-1$
      IEpisode.class, true, null );

  static final ITsNodeKind<ISourceVideo> NK_SV = new TsNodeKind<>( "SV", //$NON-NLS-1$
      ISourceVideo.class, false, ICON_PSX_VIDEO_X_GENERIC );

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
              TsImage mi = fileSystem.findThumb( ep.frame(), EThumbSize.SZ180 );
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

  IPrefBundle prefBundle1;

  private final ITsCollectionChangeListener appPrefsChangeListener = ( aSource, aOp, aItem ) -> {
    String opId = (String)aItem;
    if( opId == null || opId.equals( OP_SVL_THUMB_SIZE.id() ) ) {
      tree().setThumbSize( OP_SVL_THUMB_SIZE.getValue( prefBundle1.prefs() ).asValobj() );
      refresh();
    }
  };

  final IUnitCameras   camerasUnit;
  final IUnitEpisodes  episodesUnit;
  final IPsxFileSystem fileSystem;
  // final IUnitSourceVideos svUnit;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  public PanelUnitSourceVideos( ITsGuiContext aContext, IM5Model<ISourceVideo> aModel,
      IM5ItemsProvider<ISourceVideo> aItemsProvider, IM5LifecycleManager<ISourceVideo> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    prefBundle1 = tsContext().get( IPrefBundle.class );
    prefBundle1.prefs().addCollectionChangeListener( appPrefsChangeListener );
    tree().setThumbSize( OP_SVL_THUMB_SIZE.getValue( prefBundle1.prefs() ).asValobj() );
    fileSystem = tsContext().get( IPsxFileSystem.class );
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
        STR_N_TREE_BY_CAMERAS, STR_D_TREE_BY_CAMERAS, null, tmByCameras ) );
    treeModeManager().addTreeMode( new TreeModeInfo<>( "ByEpisodes", //$NON-NLS-1$
        STR_D_TREE_BY_EPISODES, STR_D_TREE_BY_EPISODES, null, tmByEpisodes ) );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    ISourceVideo sel = selectedItem();
    switch( aActionId ) {
      case AID_PLAY:
        if( sel != null ) {
          tsContext().get( IPrisexService.class ).playEpisodeVideo(
              new Svin( sel.episodeId(), sel.cameraId(), new Secint( 0, sel.info().duration() - 1 ) ) );
        }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ISourceVideo aSel ) {
    toolbar().setActionEnabled( AID_PLAY, aIsSel );
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
    aActs.add( AI_PLAY );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doFillTree() {
    super.doFillTree();
    adjustImageColumnWidth();
  }

}
