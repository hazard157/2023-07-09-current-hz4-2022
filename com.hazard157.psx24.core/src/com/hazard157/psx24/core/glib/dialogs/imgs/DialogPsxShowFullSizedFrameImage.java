package com.hazard157.psx24.core.glib.dialogs.imgs;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.glib.dialogs.imgs.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.excl_plan.picview.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.prisex.*;

/**
 * Версия DialogShowFullSizedImageFromFile для PSX {@link IFrame}.
 *
 * @author hazard157
 */
public class DialogPsxShowFullSizedFrameImage {

  /**
   * Last copy destination app pref ID in default prefs bundle.
   */
  public static final String APPREFID_LAST_DESTINATION = "FrameCopyLastDestination"; //$NON-NLS-1$

  static class DialogContent
      extends AbstractTsDialogPanel<IFrame, ITsGuiContext> {

    private final PictureViewer pictureViewer;

    IFrame         currFrame  = null;
    TsImage        currImage  = null;
    IList<IFrame>  framesList = IList.EMPTY;
    IPsxFileSystem fileSystem;
    IPrefBundle    prefBundle;

    protected DialogContent( Composite aParent, TsDialog<IFrame, ITsGuiContext> aOwnerDialog, IList<IFrame> aFrames ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      fileSystem = tsContext().get( IPsxFileSystem.class );
      prefBundle = tsContext().get( IPrefBundle.class );
      IListEdit<ITsActionDef> additionalActions = new ElemArrayList<>();
      if( aFrames != null && aFrames.size() > 1 ) {
        additionalActions.add( ACDEF_GO_FIRST );
        additionalActions.add( ACDEF_GO_PREV );
        additionalActions.add( ACDEF_GO_NEXT );
        additionalActions.add( ACDEF_GO_LAST );
        framesList = aFrames;
      }
      additionalActions.add( AI_COPY_FRAME );
      pictureViewer = new PictureViewer( this, tsContext(), SWT.CENTER, EMPTY_STRING,
          tsContext().get( IAnimationSupport.class ), additionalActions );
      pictureViewer.setActionsHandler( new IPictureViewerActionsHandler() {

        @Override
        public void updateActionsState( PictureViewer aSource, TsToolbar aToolbar ) {
          int currIndex = -1;
          if( currFrame != null ) {
            currIndex = framesList.indexOf( currFrame );
          }
          aToolbar.setActionEnabled( ACTID_GO_FIRST, currIndex != 0 );
          aToolbar.setActionEnabled( ACTID_GO_PREV, currIndex > 0 );
          aToolbar.setActionEnabled( ACTID_GO_NEXT, currIndex < (framesList.size() - 1) && currIndex >= 0 );
          aToolbar.setActionEnabled( ACTID_GO_LAST, currIndex != (framesList.size() - 1) );
        }

        @Override
        public void handleAction( PictureViewer aSource, String aActionId ) {
          switch( aActionId ) {
            case ACTID_GO_FIRST: {
              moveTo( currFrame, ETsCollMove.FIRST );
              break;
            }
            case ACTID_GO_PREV: {
              moveTo( currFrame, ETsCollMove.PREV );
              break;
            }
            case ACTID_GO_NEXT: {
              moveTo( currFrame, ETsCollMove.NEXT );
              break;
            }
            case ACTID_GO_LAST: {
              moveTo( currFrame, ETsCollMove.LAST );
              break;
            }
            case AID_COPY_FRAME: {
              if( currFrame != null ) {
                try {
                  String path = prefBundle.prefs().getStr( APPREFID_LAST_DESTINATION, TsLibUtils.EMPTY_STRING );
                  File destDir = TsRcpDialogUtils.askDirOpen( getShell(), path );
                  if( destDir != null ) {
                    IPrisexService prisexService = tsContext().get( IPrisexService.class );
                    prisexService.copyFrameImage( currFrame, destDir );
                    path = destDir.getAbsolutePath();
                    prefBundle.prefs().setStr( APPREFID_LAST_DESTINATION, path );
                  }
                }
                catch( Exception ex ) {
                  LoggerUtils.errorLogger().error( ex );
                  TsDialogUtils.error( getShell(), ex );
                }
              }
              break;
            }
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
          // TODO Auto-generated method stub

        }
      } );
      pictureViewer.addMouseListener( new IPictureViewerMouseListener() {

        @Override
        public void onPictureViewerMouseClick( PictureViewer aSource, boolean aIsDoubleClick, int aStateMask, int aX,
            int aY ) {
          // nop
        }

        @Override
        public void onPictureViewerMouseWheel( PictureViewer aSource, int aScrollLines, int aStateMask ) {
          if( aScrollLines > 0 ) {
            moveTo( currFrame, ETsCollMove.PREV );
          }
          else {
            moveTo( currFrame, ETsCollMove.NEXT );
          }
        }
      } );
      pictureViewer.getControl().setLayoutData( BorderLayout.CENTER );
      pictureViewer.setExpandToFit( true );
    }

    @Override
    protected void doSetDataRecord( IFrame aData ) {
      currFrame = aData;
      currImage = null;
      ITsPoint sz = new TsPoint( 16, 16 );
      if( currFrame != null ) {
        File f = fileSystem.findFrameFile( currFrame );
        if( f != null ) {
          ITsImageManager imagesManager = tsContext().get( ITsImageManager.class );
          currImage = imagesManager.findImage( f );
          if( currImage != null ) {
            sz = currImage.imageSize();
          }
        }
        this.layout();
        this.getShell().layout();
      }
      ownerDialog().setTitle( makeTitleString( currFrame ) );
      pictureViewer.getControl().setMinimumWidth( sz.x() );
      pictureViewer.getControl().setMinimumHeight( sz.y() );
      pictureViewer.setMultiImage( currImage );
    }

    @Override
    protected IFrame doGetDataRecord() {
      return currFrame;
    }

    void moveTo( IFrame aCurrFrame, ETsCollMove aMoveDirection ) {
      IFrame toSel = null;
      toSel = switch( aMoveDirection ) {
        case FIRST -> framesList.first();
        case PREV -> framesList.prev( aCurrFrame );
        case NEXT -> framesList.next( aCurrFrame );
        case LAST -> framesList.last();
        case JUMP_NEXT -> aCurrFrame; // TODO ???
        case JUMP_PREV -> aCurrFrame; // TODO ???
        case MIDDLE -> aCurrFrame; // TODO ???
        case NONE -> aCurrFrame;
        default -> throw new TsNotAllEnumsUsedRtException();
      };
      if( toSel == null ) {
        toSel = aCurrFrame;
      }
      setDataRecord( toSel );
    }

  }

  static String makeTitleString( IFrame aFrame ) {
    if( aFrame != null ) {
      return String.format( FMT_T_DPSFSFI_IMAGE, aFrame.toString() );
    }
    return EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  // TODO public static void show( IList<File> aList, int aCurrIndex, ITsGuiContext aContext )

  /**
   * Показывает изображение в модальном диалоге.
   *
   * @param aFrame {@link IFrame} - показываемый кадр
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @param aImageFiles {@link IList}&lt;{@link IFrame}&gt; - кадры для просмотра next/prev или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void show( IFrame aFrame, ITsGuiContext aContext, IList<IFrame> aImageFiles ) {
    TsNullArgumentRtException.checkNulls( aFrame, aContext );
    Shell shell = aContext.get( Shell.class );
    String caption = STR_C_DPSFSFI_IMAGE;
    String title = makeTitleString( aFrame );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ITsDialogInfo cdi = new TsDialogInfo( ctx, shell, caption, title, DF_NO_APPROVE );
    IDialogPanelCreator<IFrame, ITsGuiContext> creator =
        ( aParent, aOwnerDialog ) -> new DialogContent( aParent, aOwnerDialog, aImageFiles );
    TsDialog<IFrame, ITsGuiContext> d = new TsDialog<>( cdi, aFrame, aContext, creator );
    d.execDialog();
  }

  /**
   * Показывает изображение в модальном диалоге.
   *
   * @param aFrame {@link IFrame} - показываемый кадр
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void show( IFrame aFrame, ITsGuiContext aContext ) {
    show( aFrame, aContext, null );
  }

  /**
   * Показывает изображение в модальном диалоге.
   *
   * @param aFrame {@link IFrame} - показываемый кадр
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @param aFrames {@link IList}&lt;{@link IFrame}&gt; - кадры для просмотра next/prev или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void showNonModal( IFrame aFrame, ITsGuiContext aContext, IList<IFrame> aFrames ) {
    TsNullArgumentRtException.checkNulls( aFrame, aContext );
    Shell shell = aContext.get( Shell.class );
    String caption = STR_C_DPSFSFI_IMAGE;
    String title = makeTitleString( aFrame );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ITsDialogInfo cdi = new TsDialogInfo( ctx, shell, caption, title, DF_NO_APPROVE | DF_NONMODAL );
    IDialogPanelCreator<IFrame, ITsGuiContext> creator =
        ( aParent, aOwnerDialog ) -> new DialogContent( aParent, aOwnerDialog, aFrames );
    TsDialog<IFrame, ITsGuiContext> d = new TsDialog<>( cdi, aFrame, aContext, creator );
    d.execDialog();
  }
}
