package com.hazard157.psx24.core.glib.frlstviewer_ep;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.glib.frlstviewer_ep.IPsxResources.*;

import java.io.*;
import java.lang.reflect.*;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.*;

/**
 * Creates temporary GIF animation for preview purposes.
 *
 * @author hazard157
 */
class TestGifFileCreator
    implements IRunnableWithProgress, IPsxStdReferences {

  private static final String TMP_DIR_PREFIX = "psxTesGifCreation_"; //$NON-NLS-1$

  static final int BYPASS_FRAMES = 1; // ОПТИМИЗАЦИЯ: (1..FPS) каждый какой кадр используется при создании тестового GIF

  final IFrame        frame;
  final ITsGuiContext tsContext;

  TsImage generatedImage = null;

  public TestGifFileCreator( IFrame aFrame, ITsGuiContext aContext ) {
    frame = aFrame;
    tsContext = aContext;
  }

  @Override
  public void run( IProgressMonitor aMonitor )
      throws InvocationTargetException,
      InterruptedException {
    int animFramesCount = ANIMATED_GIF_SECS * FPS;
    aMonitor.beginTask( MSG_CREATING_GIF_FRAMES, animFramesCount );
    int sec = frame.secNo();
    IListEdit<Image> frameImages = new ElemArrayList<>();
    int frameNo1 = sec * FPS;
    int frameNo2 = frameNo1 + animFramesCount - 1;
    for( int fno = frameNo1; fno <= frameNo2; fno += BYPASS_FRAMES ) {
      IFrame tmpFrame = new Frame( frame.episodeId(), frame.cameraId(), fno, false );
      File tmpFrameFile = psxFileSystem().findFrameFile( tmpFrame );
      TsImage mi = TsImageUtils.loadTsImage( tmpFrameFile, tsContext.get( Display.class ) );
      if( mi == null ) {
        break;
      }
      frameImages.add( mi.image() );
      aMonitor.worked( BYPASS_FRAMES );
    }
    aMonitor.beginTask( MSG_CREATING_GIF_IMAGE, animFramesCount );
    long delay = 1000 / FPS * BYPASS_FRAMES;
    generatedImage = TsImage.create( frameImages, delay );

    aMonitor.done();
  }

  public TsImage getGeneratedImage() {
    return generatedImage;
  }

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
