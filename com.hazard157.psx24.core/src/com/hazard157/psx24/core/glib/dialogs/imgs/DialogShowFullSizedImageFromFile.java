package com.hazard157.psx24.core.glib.dialogs.imgs;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.glib.dialogs.imgs.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.legacy.picview.*;

/**
 * Простой диалог показа изображения в оригинальном масштабе (прямо из файла).
 *
 * @author goga
 */
public class DialogShowFullSizedImageFromFile {

  static class DialogContent
      extends AbstractTsDialogPanel<File, ITsGuiContext> {

    private static final String AID_COPY_IMAGE = PSX_ACT_ID + ".CopyImage"; //$NON-NLS-1$

    private static final ITsActionDef AI_COPY_IMAGE = TsActionDef.ofPush2( AID_COPY_IMAGE, //
        ACT_T_COPY_IMAGE, ACT_P_COPY_IMAGE, ICONID_ARROW_RIGHT );

    private final PictureViewer pictureViewer;

    File              currImageFile = null;
    TsImage           currImage     = null;
    final IList<File> filesList;

    protected DialogContent( Composite aParent, TsDialog<File, ITsGuiContext> aOwnerDialog, IList<File> aImageFiles ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      IListEdit<ITsActionDef> additionalActions = new ElemArrayList<>();
      if( aImageFiles != null && aImageFiles.size() > 1 ) {
        additionalActions.add( ACDEF_GO_FIRST );
        additionalActions.add( ACDEF_GO_PREV );
        additionalActions.add( ACDEF_GO_NEXT );
        additionalActions.add( ACDEF_GO_LAST );
        filesList = aImageFiles;
      }
      else {
        filesList = IList.EMPTY;
      }
      additionalActions.add( AI_COPY_IMAGE );
      pictureViewer = new PictureViewer( this, tsContext(), SWT.CENTER, EMPTY_STRING,
          tsContext().get( IAnimationSupport.class ), additionalActions );
      pictureViewer.setActionsHandler( new IPictureViewerActionsHandler() {

        @Override
        public void updateActionsState( PictureViewer aSource, TsToolbar aToolbar ) {
          int currIndex = -1;
          if( currImageFile != null ) {
            currIndex = filesList.indexOf( currImageFile );
          }
          aToolbar.setActionEnabled( ACTID_GO_FIRST, currIndex != 0 );
          aToolbar.setActionEnabled( ACTID_GO_PREV, currIndex > 0 );
          aToolbar.setActionEnabled( ACTID_GO_NEXT, currIndex < (filesList.size() - 1) && currIndex >= 0 );
          aToolbar.setActionEnabled( ACTID_GO_LAST, currIndex != (filesList.size() - 1) );
        }

        @Override
        public void handleAction( PictureViewer aSource, String aActionId ) {
          switch( aActionId ) {
            case ACTID_GO_FIRST: {
              moveTo( currImageFile, ETsCollMove.FIRST );
              break;
            }
            case ACTID_GO_PREV: {
              moveTo( currImageFile, ETsCollMove.PREV );
              break;
            }
            case ACTID_GO_NEXT: {
              moveTo( currImageFile, ETsCollMove.NEXT );
              break;
            }
            case ACTID_GO_LAST: {
              moveTo( currImageFile, ETsCollMove.LAST );
              break;
            }
            case AID_COPY_IMAGE: {
              // TODO DialogShowFullSizedImageFromFile - AID_COPY_IMAGE
              break;
            }
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
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
            moveTo( currImageFile, ETsCollMove.PREV );
          }
          else {
            moveTo( currImageFile, ETsCollMove.NEXT );
          }
        }
      } );
      pictureViewer.getControl().setLayoutData( BorderLayout.CENTER );
    }

    @Override
    protected void doSetDataRecord( File aData ) {
      currImageFile = aData;
      currImage = null;
      ITsPoint sz = new TsPoint( 16, 16 );
      if( currImageFile != null ) {
        ITsImageManager imagesManager = tsContext().get( ITsImageManager.class );
        currImage = imagesManager.findImage( currImageFile );
        if( currImage != null ) {
          sz = currImage.imageSize();
        }
        this.layout();
        this.getShell().layout();
      }
      ownerDialog().setTitle( makeTitleString( currImageFile ) );
      pictureViewer.getControl().setMinimumWidth( sz.x() );
      pictureViewer.getControl().setMinimumHeight( sz.y() );
      pictureViewer.setMultiImage( currImage );
    }

    @Override
    protected File doGetDataRecord() {
      return currImageFile;
    }

    void moveTo( File aCurr, ETsCollMove aMoveDirection ) {
      File toSel = aMoveDirection.findElemAt( aCurr, filesList, 10, false );
      if( toSel == null ) {
        toSel = aCurr;
      }
      setDataRecord( toSel );
    }

  }

  static String makeTitleString( File aImageFile ) {
    if( aImageFile != null ) {
      return String.format( FMT_T_DSFSI_IMAGE, aImageFile.getPath() );
    }
    return EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Показывает изображение в модальном диалоге.
   *
   * @param aImageFile {@link File} - показываемое изображение
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @param aImageFiles {@link IList}&lt;{@link File}&gt; - файлы для просмотра кнопками next/prev или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void show( File aImageFile, ITsGuiContext aContext, IList<File> aImageFiles ) {
    TsNullArgumentRtException.checkNulls( aImageFile, aContext );
    Shell shell = aContext.get( Shell.class );
    String caption = STR_C_DSFSI_IMAGE;
    String title = makeTitleString( aImageFile );
    ITsDialogInfo cdi = new TsDialogInfo( aContext, shell, caption, title, ITsDialogConstants.DF_NO_APPROVE );
    IDialogPanelCreator<File, ITsGuiContext> creator =
        ( aParent, aOwnerDialog ) -> new DialogContent( aParent, aOwnerDialog, aImageFiles );
    TsDialog<File, ITsGuiContext> d = new TsDialog<>( cdi, aImageFile, aContext, creator );
    d.execDialog();
  }

  /**
   * Показывает изображение в модальном диалоге.
   *
   * @param aImageFile {@link File} - показываемое изображение
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void show( File aImageFile, ITsGuiContext aContext ) {
    show( aImageFile, aContext, null );
  }

}
