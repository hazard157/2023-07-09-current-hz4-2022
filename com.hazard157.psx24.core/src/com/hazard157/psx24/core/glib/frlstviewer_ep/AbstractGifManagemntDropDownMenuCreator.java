package com.hazard157.psx24.core.glib.frlstviewer_ep;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.glib.frlstviewer_ep.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.io.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.glib.dialogs.*;

abstract class AbstractGifManagemntDropDownMenuCreator
    extends AbstractMenuCreator {

  // TODO вынести эти контсанты в настройки программы
  static final EThumbSize TEST_GIF_SIZE = EThumbSize.SZ724;

  // static class TestGifFileCreator
  // implements IRunnableWithProgress {
  //
  // // private static final Image[] EMPTY_IMAGE_ARRAY = {};
  //
  // final IFrame frame;
  // final IPsxFileSystem fileSystem;
  //
  // TsImage generatedImage = null;
  //
  // public TestGifFileCreator( IFrame aFrame, IPsxFileSystem aFileSystem ) {
  // frame = aFrame;
  // fileSystem = aFileSystem;
  // }
  //
  // @Override
  // public void run( IProgressMonitor aMonitor )
  // throws InvocationTargetException,
  // InterruptedException {
  // int animFramesCount = ANIMATED_GIF_SECS * FPS;
  // aMonitor.beginTask( MSG_CREATING_GIF_FRAMES, animFramesCount );
  // int sec = frame.secNo();
  // IListEdit<Image> frameImages = new ElemArrayList<>();
  // int frameNo1 = sec * FPS;
  // int frameNo2 = frameNo1 + animFramesCount - 1;
  // for( int fno = frameNo1; fno <= frameNo2; fno += BYPASS_FRAMES ) {
  // IFrame tmpFrame = new Frame( frame.episodeId(), frame.cameraId(), fno, false );
  // TsImage mi = fileSystem.findThumb( tmpFrame, TEST_GIF_SIZE );
  // if( mi == null ) {
  // break;
  // }
  // frameImages.add( mi.image() );
  // aMonitor.worked( BYPASS_FRAMES );
  // }
  // aMonitor.beginTask( MSG_CREATING_GIF_IMAGE, animFramesCount );
  // long delay = 1000 / FPS * BYPASS_FRAMES;
  // generatedImage = TsImage.create( frameImages, delay );
  // aMonitor.done();
  // }
  //
  // public TsImage getGeneratedImage() {
  // return generatedImage;
  // }
  //
  // }

  final ITsGuiContext    tsContext;
  final ITsActionHandler listener;
  final IPsxFileSystem   fileSystem;
  final Shell            shell;
  final ITsImageManager  imageManager;

  public AbstractGifManagemntDropDownMenuCreator( ITsGuiContext aTsContext, ITsActionHandler aToolbarListener ) {
    TsNullArgumentRtException.checkNulls( aTsContext, aToolbarListener );
    tsContext = aTsContext;
    listener = aToolbarListener;
    fileSystem = tsContext.get( IPsxFileSystem.class );
    shell = tsContext.get( Shell.class );
    imageManager = tsContext.get( ITsImageManager.class );
  }

  void internalHandler( String aActionId ) {
    IFrame sel = getFrame();
    switch( aActionId ) {
      case AID_TEST_GIF: {
        if( sel == null ) {
          break;
        }
        TestGifFileCreator gc = new TestGifFileCreator( sel, tsContext );
        ProgressMonitorDialog d = new ProgressMonitorDialog( shell );
        try {
          d.run( true, false, gc );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          TsDialogUtils.error( shell, ex );
        }
        TsImage mi = gc.getGeneratedImage();
        if( mi == null ) {
          TsDialogUtils.warn( shell, MSG_WARN_NOT_ALL_FRAMES_FOR_GIF );
          break;
        }
        String caption = String.format( DLG_C_FMT_SHOW_TEST_GIF, sel.toString() );
        DialogShowMultiImage.showImage( tsContext, mi, caption, null );
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        if( sel != null ) {
          // создаем список рядом стоящих файлов изображений
          IListEdit<IFrame> navFrames = new ElemArrayList<>( 100 );
          int startFrameNo = sel.frameNo() - 5 * FPS;
          int endFrameNo = sel.frameNo() + 5 * FPS;
          for( int fNo = startFrameNo; fNo <= endFrameNo; fNo++ ) {
            IFrame frame = new Frame( sel.episodeId(), sel.cameraId(), fNo, true );
            File file = fileSystem.findFrameFile( frame );
            if( file != null ) {
              navFrames.add( frame );
            }
            frame = new Frame( sel.episodeId(), sel.cameraId(), fNo, false );
            file = fileSystem.findFrameFile( frame );
            if( file != null ) {
              navFrames.add( frame );
            }
          }
          DialogShowFrameFiles.show( tsContext, sel, navFrames );
        }
        break;
      }
      case AID_GIF_INFO: {
        if( sel == null ) {
          break;
        }
        TsInternalErrorRtException.checkNull( sel );
        File file = fileSystem.findFrameFile( sel );
        if( file == null ) {
          TsTestUtils.pl( FMT_NO_MULTI_IMAGE_FOR_FRAME, sel.toString() );
          return;
        }
        TsImage mi = imageManager.findImage( file );
        if( mi == null ) {
          TsTestUtils.pl( FMT_NO_MULTI_IMAGE_FOR_FRAME, sel.toString() );
          break;
        }
        TsTestUtils.pl( FMT_LOADED_MULTI_IMAGE_FOR_FRAME, sel.toString() );
        StringBuilder sb = new StringBuilder();
        for( String s : getInfoText( mi ) ) {
          sb.append( s );
          sb.append( '\n' );
        }
        TsDialogUtils.info( shell, sb.toString() );
        break;
      }
      default:
        listener.handleAction( aActionId );
        break;
    }
  }

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    IFrame sel = getFrame();
    if( sel == null ) {
      return false;
    }
    ITsIconManager iconManager = tsContext.get( ITsIconManager.class );
    IListEdit<ITsActionDef> actInfoes =
        new ElemArrayList<>( AI_CREATE_GIF, AI_TEST_GIF, AI_RECREATE_ALL_GIFS, AI_DELETE_GIF, AI_GIF_INFO );
    for( ITsActionDef actInfo : actInfoes ) {
      MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( actInfo.nmName() );
      mItem.setToolTipText( actInfo.description() );
      mItem.setImage( iconManager.loadStdIcon( actInfo.iconId(), EIconSize.IS_16X16 ) );
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aE ) {
          internalHandler( actInfo.id() );
        }
      } );
    }
    return true;
  }

  /**
   * Возвращает многострочный текст - информацию об изображении.
   *
   * @param aMi {@link TsImage} - изображение
   * @return {@link IStringList} - многострочный текст
   */
  @SuppressWarnings( { "nls", "boxing" } )
  IStringList getInfoText( TsImage aMi ) {
    IStringListEdit lines = new StringLinkedBundleList();
    if( aMi.isSingleFrame() ) {
      lines.add( String.format( "Non-animated MultiImage has 1 frame." ) );
      lines.add( String.format( "  frameSize = %d x %d", aMi.imageSize().x(), aMi.imageSize().y() ) );
    }
    else {
      if( aMi.isEvenAnimation() ) {
        lines.add( String.format( "Even animated MultiImage has %d frames.", aMi.frames().size() ) );
        lines.add( String.format( "  frameSize = %d x %d", aMi.imageSize().x(), aMi.imageSize().y() ) );
        lines.add( String.format( "  delay = %d msec", aMi.delay() ) );
      }
      else {
        lines.add( String.format( "UNEVEN MultiImage has %d frames.", aMi.frames().size() ) );
        for( int i = 0; i < aMi.count(); i++ ) {
          lines.add( String.format( "  frame[%d] -> %d x %d, delay = %d msec", i, aMi.imageSize().x(),
              aMi.imageSize().y(), aMi.delays().get( i ) ) );
        }
      }
    }
    return lines;
  }

  protected abstract IFrame getFrame();

}
