package com.hazard157.psx24.intro.e4.uiparts;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Preload the specified image thumbs.
 *
 * @author hazard157
 */
public class ImageThumbsPreloader
    implements Runnable, ITsGuiContextable {

  private final ITsGuiContext   tsContext;
  private final EThumbSize      thumbSize;
  private final IListEdit<File> files;

  public ImageThumbsPreloader( ITsGuiContext aContext, EThumbSize aThumbSize, IList<File> aFiles ) {
    TsNullArgumentRtException.checkNulls( aContext, aThumbSize, aFiles );
    tsContext = aContext;
    thumbSize = aThumbSize;
    files = new ElemLinkedBundleList<>( aFiles );
  }

  @Override
  public void run() {
    if( !files.isEmpty() ) {
      Display display = tsContext.get( Display.class );
      File f = files.removeByIndex( 0 );
      imageManager().findThumb( f, thumbSize );

      // DEBUG ---
      TsTestUtils.pl( "Preloaded thumb for %s", f.getAbsolutePath() );
      // ---

      display.asyncExec( this );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }
}
