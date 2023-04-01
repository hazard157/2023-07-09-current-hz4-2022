package com.hazard157.psx24.core.e4.uiparts.eps;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;
import com.hazard157.psx24.core.m5.note.*;

/**
 * Вью работы с {@link IEpisode#noteLine()}.
 *
 * @author hazard157
 */
public class UipartEpisodeNotes
    extends AbstractEpisodeUipart {

  private final ITsSelectionChangeListener<MarkNote> guideSelectionListener = ( aSource, aSelectedItem ) -> {
    if( aSelectedItem == null || episode() == null ) {
      this.currentFramesListService.setCurrent( null );
      this.selectedSvinsService.setSvin( null );
      return;
    }
    IListEdit<IFrame> frames = new ElemArrayList<>();
    Svin svin = new Svin( episode().id(), aSelectedItem.cameraId(), aSelectedItem.in() );
    FrameSelectionCriteria criteria = new FrameSelectionCriteria( svin, EAnimationKind.ANIMATED, false );
    frames.addAll( this.fileSystem.listEpisodeFrames( criteria ) );
    if( frames.isEmpty() ) {
      int secNo = aSelectedItem.in().start() + aSelectedItem.in().duration() / 2;
      IFrame frame = new Frame( episode().id(), aSelectedItem.cameraId(), secNo * FPS, false );
      frames.add( frame );
    }
    this.currentFramesListService.setCurrent( frames );
    this.selectedSvinsService.setSvin( svin );
  };

  @Inject
  ICurrentFramesListService currentFramesListService;

  @Inject
  IPsxSelectedSvinsService selectedSvinsService;

  IPsxFileSystem               fileSystem;
  IM5Model<MarkNote>           model;
  IM5CollectionPanel<MarkNote> panel;

  @Override
  protected void doCreatePartContent( Composite aParent ) {
    fileSystem = tsContext().get( IPsxFileSystem.class );
    model = m5().getModel( MarkNoteM5Model.MODEL_ID, MarkNote.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = model.panelCreator().createCollEditPanel( ctx, null, null );
    panel.createControl( aParent );
    panel.addTsSelectionListener( guideSelectionListener );
  }

  @Override
  protected void doSetEpisode() {
    if( episode() == null ) {
      panel.setItemsProvider( null );
      panel.setLifecycleManager( null );
      panel.refresh();
      return;
    }
    IM5LifecycleManager<MarkNote> lm = model.getLifecycleManager( episode().noteLine() );
    panel.setLifecycleManager( lm );
    panel.setItemsProvider( lm.itemsProvider() );
    panel.refresh();
  }

  @Override
  protected void doHandleEpisodeContentChange() {
    doSetEpisode();
  }

}
