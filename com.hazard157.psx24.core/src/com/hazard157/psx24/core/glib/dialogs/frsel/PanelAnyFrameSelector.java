package com.hazard157.psx24.core.glib.dialogs.frsel;

import static com.hazard157.psx24.core.glib.dialogs.frsel.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.glib.frlstviewer_ep.*;

/**
 * Панель просмотра и выбора любого кадра любого эпизода.
 * <p>
 * Слева в панели - список эпизодов, справа - панель выбора кадра эпизода.
 *
 * @author goga
 */
public class PanelAnyFrameSelector
    extends AbstractTsDialogPanel<IFrame, ITsGuiContext> {

  private final ITsSelectionChangeListener<ITsNode> episodeNodeChangeListener = ( aSource, aSelectedItem ) -> {
    if( aSelectedItem != null ) {
      whenEpisodeSelectionChanged( (IEpisode)aSelectedItem.entity() );
    }
    else {
      whenEpisodeSelectionChanged( null );
    }
  };

  private static final ITsNodeKind<IEpisode> NK_EP = new TsNodeKind<>( "Episode", //$NON-NLS-1$
      IEpisode.class, false );

  final IPsxFileSystem           fileSystem;
  final ITsTreeViewer            tree;
  final IEpisodeFramesListViewer framesViewer;

  protected PanelAnyFrameSelector( Composite aParent, IFrame aData, ITsGuiContext aContext, int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    tree = new TsTreeViewer( tsContext() );
    framesViewer = new EpisodeFramesListViewer( tsContext() );
    init();
  }

  protected PanelAnyFrameSelector( Composite aParent, TsDialog<IFrame, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    fileSystem = tsContext().get( IPsxFileSystem.class );
    tree = new TsTreeViewer( tsContext() );
    EpisodeFramesListViewer.FRAME_VIEWER_THUMB_SIZE.setValue( tsContext().params(), avValobj( EThumbSize.SZ512 ) );
    framesViewer = new EpisodeFramesListViewer( tsContext() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  void init() {
    this.setLayout( new BorderLayout() );
    // tree
    tree.createControl( this );
    tree.getControl().setLayoutData( BorderLayout.CENTER );
    tree.setIconSize( EIconSize.IS_48X48 );
    tree.addColumn( STR_N_ID, EHorAlignment.CENTER, new ITsVisualsProvider<ITsNode>() {

      @Override
      public String getName( ITsNode aItem ) {
        IEpisode e = IEpisode.class.cast( aItem.entity() );
        return e.id();
      }

      @Override
      public Image getIcon( ITsNode aItem, EIconSize aIconSize ) {
        IEpisode e = IEpisode.class.cast( aItem.entity() );
        EThumbSize thumbSize = EThumbSize.findIncluding( aIconSize );
        return fileSystem.findThumb( e.frame(), thumbSize ).image();
      }

    } );
    tree.addTsSelectionListener( episodeNodeChangeListener );
    // framesViewer
    framesViewer.createControl( this );
    framesViewer.getControl().setLayoutData( BorderLayout.EAST );
    // TODO fill with episodes
    IListEdit<ITsNode> roots = new ElemArrayList<>();
    IUnitEpisodes ue = tsContext().get( IUnitEpisodes.class );
    for( IEpisode e : ue.items() ) {
      DefaultTsNode<IEpisode> node = new DefaultTsNode<>( NK_EP, tree, e );
      roots.add( node );
    }
    tree.setRootNodes( roots );
  }

  void whenEpisodeSelectionChanged( IEpisode aEpisode ) {
    if( aEpisode == null ) {
      framesViewer.setCriteria( FrameSelectionCriteria.NONE );
      return;
    }
    Svin svin = new Svin( aEpisode.id(), IStridable.NONE_ID, new Secint( 0, aEpisode.duration() - 1 ) );
    FrameSelectionCriteria fsc = new FrameSelectionCriteria( svin, EAnimationKind.BOTH, true );
    framesViewer.setCriteria( fsc );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractDataRecordPanel
  //

  @Override
  protected void doSetDataRecord( IFrame aData ) {
    ITsNode nodeToSel = null;
    if( aData != null && aData.isDefined() ) {
      IUnitEpisodes ue = tsContext().get( IUnitEpisodes.class );
      IEpisode eb = ue.items().findByKey( aData.episodeId() );
      if( eb != null ) {
        nodeToSel = tree.findByEntity( eb, true );
      }
    }
    tree.setSelectedItem( nodeToSel );
    framesViewer.setSelectedItem( aData );
  }

  @Override
  protected IFrame doGetDataRecord() {
    return framesViewer.selectedItem();
  }

}
