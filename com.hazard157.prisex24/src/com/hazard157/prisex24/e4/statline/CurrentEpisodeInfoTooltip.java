package com.hazard157.prisex24.e4.statline;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.e4.statline.IPsxResources.*;

import java.io.*;

import org.eclipse.jface.window.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Tooltip implementation to display some episode info and frame thumbnail {@link IEpisode#frame()}.
 *
 * @author hazard157
 */
public class CurrentEpisodeInfoTooltip
    extends DefaultToolTip
    implements IPsxGuiContextable {

  /**
   * The means to provide the tooltip information about displayed episode.
   *
   * @author hazard157
   */
  public interface IEpisodeProvider {

    /**
     * Returns the episode for which the tooltip will be invoked.
     *
     * @return {@link IEpisode} - the episode or null
     */
    IEpisode getEpisode();
  }

  private final ITsGuiContext    tsContext;
  private final IEpisodeProvider episodeProvider;

  /**
   * Constructor.
   *
   * @param aControl {@link Control} - the control to which the tooltip is bound
   * @param aContext {@link ITsGuiContext} - the context
   * @param aEpProvider {@link IEpisodeProvider} - provides episode to display information for
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CurrentEpisodeInfoTooltip( Control aControl, ITsGuiContext aContext, IEpisodeProvider aEpProvider ) {
    super( aControl, SWT.WRAP, false );
    TsNullArgumentRtException.checkNulls( aContext, aEpProvider );
    tsContext = aContext;
    episodeProvider = aEpProvider;
    setPopupDelay( 100 );
  }

  // ------------------------------------------------------------------------------------
  // DefaultToolTip
  //

  @Override
  protected Image getImage( Event aEvent ) {
    IEpisode e = episodeProvider.getEpisode();
    if( e == null ) {
      return null;
    }
    File f = cofsFrames().findFrameFile( e.frame() );
    if( f != null ) {
      IOptionSet prefs = prefBundle( PBID_HZ_COMMON ).prefs();
      EThumbSize thumbSize = APPREF_THUMB_SIZE_IN_MENUS.getValue( prefs ).asValobj();
      TsImage tsim = imageManager().findThumb( f, thumbSize );
      if( tsim != null ) {
        return tsim.image();
      }
    }
    return null;
  }

  @Override
  protected String getText( Event aEvent ) {
    IEpisode e = episodeProvider.getEpisode();
    if( e == null ) {
      return STR_NO_EPISODE_D;
    }
    String descr = e.description();
    // TODO word-wrap description, eg 80 chars
    return String.format( "%s\n%s\n%s", e.incidentDate().toString(), e.nmName(), descr ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
