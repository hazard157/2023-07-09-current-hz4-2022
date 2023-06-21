package com.hazard157.prisex24.e4.uiparts.refbooks;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Вью правки камер.
 *
 * @author hazard157
 */
public class UipartRefbookSourceVideos
    extends MwsAbstractPart {

  @Inject
  IUnitSourceVideos unitSourceVideos;

  IM5CollectionPanel<ISourceVideo> svPanel;
  ISvinsFramesViewer               flPanel;

  @Override
  protected void doInit( Composite aParent ) {
    SashForm sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // source videos list
    IM5Model<ISourceVideo> model = m5().getModel( MID_SOURCE_VIDEO, ISourceVideo.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5LifecycleManager<ISourceVideo> lm = model.getLifecycleManager( unitSourceVideos );
    OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(), avValobj( EBorderLayoutPlacement.SOUTH ) );
    OPDEF_IS_SUMMARY_PANE.setValue( ctx.params(), AV_TRUE );
    OPDEF_NODE_THUMB_SIZE.setValue( ctx.params(), avValobj( readThumbSize() ) );
    svPanel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    svPanel.createControl( sfMain );
    // frames list
    ctx = new TsGuiContext( tsContext() );
    flPanel = new SvinsFramesViewer( sfMain, ctx );
    // setup
    svPanel.addTsSelectionListener( ( aSource, aSel ) -> {
      if( aSel == null ) {
        flPanel.svinSeq().svins().clear();
        return;
      }
      Secint in = new Secint( 0, aSel.duration() - 1 );
      Svin svin = new Svin( aSel.episodeId(), aSel.cameraId(), in, aSel.frame() );
      flPanel.svinSeq().svins().setAll( svin );
    } );
    sfMain.setWeights( 350, 650 );
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( ( s, o, i ) -> whenApprefsChanged() );
  }

  EThumbSize readThumbSize() {
    IPrefBundle prefBundle = prefBundle( PBID_HZ_COMMON );
    return APPREF_THUMB_SIZE_IN_LISTS.getValue( prefBundle.prefs() ).asValobj();
  }

  void whenApprefsChanged() {
    EThumbSize oldThumbSize = flPanel.thumbSizeManager().thumbSize();
    EThumbSize newThumbSize = readThumbSize();
    if( oldThumbSize != newThumbSize ) {
      flPanel.thumbSizeManager().setThumbSize( newThumbSize );
    }
  }

}
