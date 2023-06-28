package com.hazard157.prisex24.glib;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * {@link ITsVisualsProvider} for {@link IEpisode}
 *
 * @author hazard157
 */
public class EpisodeVisualsProvider
    implements ITsVisualsProvider<IEpisode>, IPsxGuiContextable {

  private final ITsGuiContext tsContext;
  private final IPrefBundle   prefBundle;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EpisodeVisualsProvider( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    prefBundle = prefBundle( PBID_WELCOME );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private TsImage getNoneImageForEpisode( EThumbSize aThumbSize ) {
    int sz = aThumbSize.size();
    Image img = iconManager().loadStdIcon( ICONID_NONE_EPISODE_IMAGE, EIconSize.findIncluding( sz, sz ) );
    return TsImage.create( img );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsVisualsProvider
  //

  @Override
  public String getName( IEpisode aItem ) {
    String fmtStr = "%1$te %1$tB %1$tY"; //$NON-NLS-1$
    if( APPREF_WELCOME_IS_LABEL_AS_YMD.getValue( prefBundle.prefs() ).asBool() ) {
      fmtStr = "%tF"; //$NON-NLS-1$
    }
    return String.format( fmtStr, Long.valueOf( aItem.info().when() ) );
  }

  @Override
  public String getDescription( IEpisode aItem ) {
    return aItem != null ? aItem.nmName() + '\n' + '\n' + aItem.description() : TsLibUtils.EMPTY_STRING;
  }

  @Override
  public TsImage getThumb( IEpisode aItem, EThumbSize aThumbSize ) {
    boolean isStillForced = APPREF_WELCOME_IS_FORCE_STILL.getValue( prefBundle.prefs() ).asBool();
    File ff = cofsFrames().findFrameFile( aItem.frame() );
    TsImage mi = null;
    if( ff != null ) {
      mi = imageManager().findThumb( ff, aThumbSize );
    }
    if( mi == null ) {
      mi = getNoneImageForEpisode( aThumbSize );
    }
    if( mi.isAnimated() && isStillForced ) {
      mi = TsImage.create( mi.image() ); // SWT image will be disposed when anumated mi disposes
    }
    return mi;
  }

}
