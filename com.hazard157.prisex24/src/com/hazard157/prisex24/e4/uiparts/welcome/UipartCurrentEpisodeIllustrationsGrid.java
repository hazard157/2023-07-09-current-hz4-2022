package com.hazard157.prisex24.e4.uiparts.welcome;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.glib.frview.impl.PgvVisualsProviderFrame.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.uiparts.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Displays current episode illustrations as thumbnails grid.
 *
 * @author hazard157
 */
public class UipartCurrentEpisodeIllustrationsGrid
    extends PsxAbstractUipart {

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  IFramesGridViewer fgViewer;

  @Override
  protected void doInit( Composite aParent ) {
    currentEpisodeService.addCurrentEntityChangeListener( c -> whenCurrEpisodeChanges() );
    // fgViewer
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    EThumbSize thumbSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    IPicsGridViewerConstants.OPDEF_DEFAULT_THUMB_SIZE.setValue( ctx.params(), avValobj( thumbSize ) );
    fgViewer = new FramesGridViewer( aParent, ctx );
    fgViewer.setDisplayedInfoFlags( LF_CAM | LF_HMS );
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( ( s, o, i ) -> whenHzCommonAppPrefsChanged() );
    fgViewer.addTsDoubleClickListener( ( src, sel ) -> {
      if( sel != null ) {
        int start = sel.secNo() > 5 ? sel.secNo() - 4 : 0;
        Svin svin = new Svin( sel.episodeId(), sel.cameraId(), new Secint( start, start + 10 * 3600 ) );
        psxService().playEpisodeVideo( svin );
      }
    } );
    whenCurrEpisodeChanges();
  }

  private void whenHzCommonAppPrefsChanged() {
    EThumbSize thumbSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    fgViewer.thumbSizeManager().setThumbSize( thumbSize );
  }

  protected void whenCurrEpisodeChanges() {
    IEpisode sel = currentEpisodeService.current();
    String label;
    if( sel != null ) {
      fgViewer.setFrames( sel.getIllustrations( false ) );
      label = String.format( "%s - %s", sel.incidentDate().toString(), sel.nmName() ); //$NON-NLS-1$
    }
    else {
      fgViewer.setFrames( IList.EMPTY );
      label = "---"; //$NON-NLS-1$
    }
    getSelfPart().setLabel( label );
  }

}
