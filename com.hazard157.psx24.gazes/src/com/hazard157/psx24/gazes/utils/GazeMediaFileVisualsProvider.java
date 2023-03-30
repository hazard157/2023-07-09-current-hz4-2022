package com.hazard157.psx24.gazes.utils;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.glib.pgviewer.*;
import com.hazard157.lib.core.incub.optedfile.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Gaze media as {@link OptedFile} visuals provider for {@link IPicsGridViewer}.
 *
 * @author goga
 */
public class GazeMediaFileVisualsProvider
    implements IPgvVisualsProvider<OptedFile>, IPsxStdReferences {

  private final ITsGuiContext tsContext;

  private static final IMapEdit<EThumbSize, TsImage> audioThumbs   = new ElemMap<>();
  private static final IMapEdit<EThumbSize, TsImage> textThumbs    = new ElemMap<>();
  private static final IMapEdit<EThumbSize, TsImage> unknownThumbs = new ElemMap<>();

  /**
   * Constructor.
   *
   * @param aWinContext {@link ITsGuiContext} - the windows level context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public GazeMediaFileVisualsProvider( ITsGuiContext aWinContext ) {
    TsNullArgumentRtException.checkNull( aWinContext );
    tsContext = aWinContext;
  }

  /**
   * Disposes static images allocated while view is visible.
   */
  public static void disposeImagesOnProgramExit() {
    while( !audioThumbs.isEmpty() ) {
      audioThumbs.removeByKey( audioThumbs.keys().first() ).dispose();
    }
    while( !textThumbs.isEmpty() ) {
      textThumbs.removeByKey( textThumbs.keys().first() ).dispose();
    }
    while( !unknownThumbs.isEmpty() ) {
      unknownThumbs.removeByKey( unknownThumbs.keys().first() ).dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private Image createTransparentThumb( EThumbSize aThumbSize ) {
    Image tmp = new Image( getShell().getDisplay(), aThumbSize.size(), aThumbSize.size() );
    ImageData imageData = tmp.getImageData();
    for( int y = 0; y < aThumbSize.size(); y++ ) {
      for( int x = 0; x < aThumbSize.size(); x++ ) {
        imageData.setAlpha( x, y, 0 );
      }
    }
    tmp.dispose();
    return new Image( getShell().getDisplay(), imageData );
  }

  private TsImage makeMi( String aIconId, EThumbSize aThumbSize ) {
    // create transparent background of needed size
    Image empty = createTransparentThumb( aThumbSize );
    GC imageGC = new GC( empty );
    // load icon image
    EIconSize iconSize = EIconSize.findIncluding( aThumbSize.size(), aThumbSize.size() );
    Image iconImage = iconManager().loadStdIcon( aIconId, iconSize );
    // draw icon in center of trancparent background
    int x = (aThumbSize.size() - iconSize.size()) / 2;
    int y = (aThumbSize.size() - iconSize.size()) / 2;
    imageGC.drawImage( iconImage, x, y );
    imageGC.dispose();
    // create and return multi-image
    return TsImage.create( empty );
  }

  private TsImage getAudioThumb( EThumbSize aThumbSize ) {
    TsImage mi = audioThumbs.findByKey( aThumbSize );
    if( mi == null ) {
      mi = makeMi( ICON_AUDIO_X_GENERIC, aThumbSize );
      audioThumbs.put( aThumbSize, mi );
    }
    return mi;
  }

  private TsImage getTextThumb( EThumbSize aThumbSize ) {
    TsImage mi = textThumbs.findByKey( aThumbSize );
    if( mi == null ) {
      mi = makeMi( ICON_TEXT_X_GENERIC, aThumbSize );
      textThumbs.put( aThumbSize, mi );
    }
    return mi;
  }

  private TsImage getUnknwonThumb( EThumbSize aThumbSize ) {
    TsImage mi = unknownThumbs.findByKey( aThumbSize );
    if( mi == null ) {
      mi = makeMi( ICON_UNKNOWN_X_GENERIC, aThumbSize );
      unknownThumbs.put( aThumbSize, mi );
    }
    return mi;
  }

  // ------------------------------------------------------------------------------------
  // IPgvVisualsProvider
  //

  @Override
  public TsImage getThumb( OptedFile aEntity, EThumbSize aThumbSize ) {
    File file = aEntity.file();
    String ext = TsFileUtils.extractExtension( aEntity.file().getName() ).toLowerCase();
    // image file
    if( IMediaFileConstants.IMAGE_FILE_EXT_LIST.hasElem( ext ) ) {
      return imageManager().findThumb( file, aThumbSize );
    }
    // video file
    if( IMediaFileConstants.VIDEO_FILE_EXT_LIST.hasElem( ext ) ) {
      IPsxFileSystem fs = tsContext.get( IPsxFileSystem.class );
      File illustrationFile = fs.enusurePsxVideoIllustrationImage( file );
      if( illustrationFile != null ) {
        return imageManager().findThumb( illustrationFile, aThumbSize );
      }
      return null;
    }
    // audio file
    if( IMediaFileConstants.AUDIO_FILE_EXT_LIST.hasElem( ext ) ) {
      return getAudioThumb( aThumbSize );
    }
    if( "txt".equals( ext ) ) { //$NON-NLS-1$
      return getTextThumb( aThumbSize );
    }
    return getUnknwonThumb( aThumbSize );
  }

  @Override
  public String getLabel1( OptedFile aEntity ) {
    String bareName = TsFileUtils.extractBareFileName( aEntity.file().getName() );
    return bareName;
  }

  @Override
  public String getLabel2( OptedFile aEntity ) {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public String getTooltip( OptedFile aEntity ) {
    return aEntity.file().getAbsolutePath();
  }

  // ------------------------------------------------------------------------------------
  // IPsxStdReferences
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
