package com.hazard157.prisex24.m5.camera;

import static com.hazard157.prisex24.m5.camera.IPsxResources.*;
import static com.hazard157.psx.proj3.IPsxProj3Constants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Панель работы с менеджером камер {@link IUnitCameras}, который находится в контексте приложения.
 *
 * @author hazard157
 */
public class PanelUnitCameras
    extends MultiPaneComponentModown<Camera> {

  static final ITsNodeKind<Object> NK_AVAIL = new TsNodeKind<>( "Avail", Object.class, true, ICONID_CAMERA_GENERIC ); //$NON-NLS-1$

  static final ITsNodeKind<ECameraKind> NK_TYPE =
      new TsNodeKind<>( "Type", ECameraKind.class, true, ICONID_CAMERA_GENERIC ); //$NON-NLS-1$

  static final ITsNodeKind<Object> NK_USAGE = new TsNodeKind<>( "Usage", Object.class, true, ICONID_CAMERA_GENERIC ); //$NON-NLS-1$

  static final ITsNodeKind<Camera> NK_CAM = new TsNodeKind<>( "Cam", Camera.class, false, ICONID_CAMERA_GENERIC ); //$NON-NLS-1$

  final ITsTreeMaker<Camera> treeByAvailability = new ITsTreeMaker<>() {

    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Camera> aItems ) {
      DefaultTsNode<Object> nodeAvail = new DefaultTsNode<>( NK_AVAIL, aRootNode, this );
      nodeAvail.setName( STR_NODE_AVAILABLE_CAMS );
      DefaultTsNode<Object> nodeNone = new DefaultTsNode<>( NK_AVAIL, aRootNode, this );
      nodeNone.setName( STR_NODE_UNAVAILABLE_CAMS );
      nodeNone.setIconId( ICONID_CAMERA_GENERIC_DIMMED );
      for( Camera c : aItems ) {
        DefaultTsNode<Object> parent = nodeNone;
        if( c.isCamAvailable() ) {
          parent = nodeAvail;
        }
        DefaultTsNode<Camera> camNode = new DefaultTsNode<>( NK_CAM, parent, c );
        parent.addNode( camNode );
      }
      return new ElemArrayList<>( nodeAvail, nodeNone );
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_CAM;
    }
  };

  final ITsTreeMaker<Camera> treeByUsage = new ITsTreeMaker<>() {

    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Camera> aItems ) {
      IUnitSourceVideos unitSourceVideos = tsContext().get( IUnitSourceVideos.class );

      DefaultTsNode<Object> nodeUsed = new DefaultTsNode<>( NK_USAGE, aRootNode, this );
      nodeUsed.setName( STR_NODE_USED_CAMS );
      DefaultTsNode<Object> nodeUnused = new DefaultTsNode<>( NK_USAGE, aRootNode, this );
      nodeUnused.setName( STR_NODE_UNUSED_CAMS );
      nodeUnused.setIconId( ICONID_CAMERA_GENERIC_DIMMED );
      for( Camera c : aItems ) {
        DefaultTsNode<Object> parent = nodeUsed;
        if( unitSourceVideos.listCameraSourceVideos( c.id() ).isEmpty() ) {
          parent = nodeUnused;
        }
        DefaultTsNode<Camera> camNode = new DefaultTsNode<>( NK_CAM, parent, c );
        parent.addNode( camNode );
      }
      return new ElemArrayList<>( nodeUsed, nodeUnused );
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_CAM;
    }
  };

  final ITsTreeMaker<Camera> treeByType = new ITsTreeMaker<>() {

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Camera> aItems ) {
      IMapEdit<ECameraKind, DefaultTsNode<ECameraKind>> nodesTypes = new ElemMap<>();
      for( ECameraKind k : ECameraKind.asList() ) {
        DefaultTsNode<ECameraKind> node = new DefaultTsNode<>( NK_TYPE, aRootNode, k );
        node.setIconId( k.iconId() );
        node.setName( k.nmName() );
        nodesTypes.put( k, node );
      }
      for( Camera cam : aItems ) {
        DefaultTsNode<ECameraKind> parent = nodesTypes.getByKey( cam.kind() );
        DefaultTsNode<Camera> camNode = new DefaultTsNode<>( NK_CAM, parent, cam );
        parent.addNode( camNode );
      }
      return (IList)nodesTypes.values();
    }

    // @SuppressWarnings( { "rawtypes", "unchecked" } )
    // @Override
    // public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<Camera> aItems ) {
    // IStringMapEdit<DefaultTsNode<String>> nodesTypes = new StringMap<>();
    // for( Camera cam : aItems ) {
    // String typeId = StridUtils.getPathComponent( cam.id(), 0 );
    // DefaultTsNode<String> nType = nodesTypes.findByKey( typeId );
    // if( nType == null ) {
    // nType = new DefaultTsNode<>( NK_TYPE, aRootNode, typeId );
    // nodesTypes.put( typeId, nType );
    // }
    // DefaultTsNode<Camera> camNode = new DefaultTsNode<>( NK_CAM, nType, cam );
    // nType.addNode( camNode );
    // }
    // return (IList)nodesTypes.values();
    // }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_CAM;
    }
  };

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  public PanelUnitCameras( ITsGuiContext aContext, IM5Model<Camera> aModel, IM5ItemsProvider<Camera> aItemsProvider,
      IM5LifecycleManager<Camera> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    itemsProvider().genericChangeEventer().addListener( aSource -> {
      refresh();
    } );
  }

  @Override
  protected void doAfterCreateControls() {
    treeModeManager().addTreeMode(
        new TreeModeInfo<>( NK_AVAIL.id(), STR_GROUP_BY_AVAIL, STR_GROUP_BY_AVAIL_D, null, treeByAvailability ) );
    treeModeManager().addTreeMode(
        new TreeModeInfo<>( NK_USAGE.id(), STR_GROUP_BY_USAGE, STR_GROUP_BY_USAGE_D, null, treeByUsage ) );
    treeModeManager()
        .addTreeMode( new TreeModeInfo<>( NK_TYPE.id(), STR_GROUP_BY_TYPE_D, STR_GROUP_BY_TYPE_D, null, treeByType ) );
  }

}
