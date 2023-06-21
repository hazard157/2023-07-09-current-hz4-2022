package com.hazard157.psx24.core.m5.visumple;

import static com.hazard157.psx24.core.m5.visumple.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.quants.visumple.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.glib.dialogs.frsel.*;
import com.hazard157.psx24.core.glib.dialogs.imgs.*;

/**
 * Панель редактирования сущностей {@link Visumple}.
 *
 * @author hazard157
 */
final class VisumpleEditPanel
    extends M5AbstractEntityPanel<Visumple> {

  private final ITsActionHandler toolbarListener = this::processAction;

  private final ModifyListener filePathModifyListener = e -> updateActionsState();

  TsToolbar toolbar;
  Text      txtFilePath;
  Text      txtNotes;

  /**
   * Редактируемый {@link Visumple}, задается из {@link IM5Bunch#originalEntity()}, нужен для редактироваия.
   */
  Visumple currVisumple = null;

  IPdwWidget imageWidget;

  VisumpleEditPanel( ITsGuiContext aContext, IM5Model<Visumple> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void processAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    switch( aActionId ) {
      case ACTID_OPEN: {
        File f = getExistingFile();
        if( f == null ) {
          f = getExistingDir();
        }
        String initPath = (f != null) ? f.getAbsolutePath() : EMPTY_STRING;
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < IMAGE_FILE_EXT_LIST.size(); i++ ) {
          sb.append( "*." ); //$NON-NLS-1$
          sb.append( IMAGE_FILE_EXT_LIST.get( i ) );
          if( i < IMAGE_FILE_EXT_LIST.size() - 1 ) {
            sb.append( ';' );
          }
        }
        IStringListEdit exts = new StringLinkedBundleList();
        exts.add( sb.toString() );
        for( String s : IMediaFileConstants.IMAGE_FILE_EXT_LIST ) {
          exts.add( "*." + s ); //$NON-NLS-1$
        }
        f = TsRcpDialogUtils.askFileOpen( getShell(), initPath, exts );
        changeFile( f );
        break;
      }
      case ACTID_ADD: {
        ITsGuiContext ctx = new TsGuiContext( tsContext() );
        IFrame frame = DialogAnyFrameSelector.select( ctx, IFrame.NONE );
        if( frame != null ) {
          DialogPsxShowFullSizedFrameImage.show( frame, tsContext() );
          IPsxFileSystem fs = tsContext().get( IPsxFileSystem.class );
          File f = fs.findFrameFile( frame );
          if( f != null ) {
            changeFile( f );
          }
          else {
            TsDialogUtils.warn( getShell(), FMT_WARN_NO_FRAME_FILE, frame.toString() );
          }
        }
        break;
      }
      case ACTID_ZOOM_FIT_BEST:
        imageWidget.setFitInfo( RectFitInfo.BEST );
        imageWidget.redraw();
        break;
      case ACTID_ZOOM_ORIGINAL:
        imageWidget.setFitInfo( RectFitInfo.NONE );
        imageWidget.redraw();
        break;
      case ACTID_GO_PREV: {
        File dir = getExistingDir();
        if( dir != null ) {
          File currFile = getExistingFile();
          IListBasicEdit<File> imgFiles = new SortedElemLinkedBundleList<>();
          imgFiles.addAll( TsFileUtils.listChilds( dir, IMediaFileConstants.FF_IMAGES ) );
          File f = ETsCollMove.PREV.findElemAt( currFile, imgFiles, 10, true );
          changeFile( f );
        }
        break;
      }
      case ACTID_GO_NEXT: {
        File dir = getExistingDir();
        if( dir != null ) {
          File currFile = getExistingFile();
          IListBasicEdit<File> imgFiles = new SortedElemLinkedBundleList<>();
          imgFiles.addAll( TsFileUtils.listChilds( dir, IMediaFileConstants.FF_IMAGES ) );
          File f = ETsCollMove.NEXT.findElemAt( currFile, imgFiles, 10, true );
          changeFile( f );
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  void updateActionsState() {
    boolean hasPrev = false;
    boolean hasNext = false;
    File dir = getExistingDir();
    if( dir != null ) {
      File f = getExistingFile();
      IListBasicEdit<File> imgFiles = new SortedElemLinkedBundleList<>();
      imgFiles.addAll( dir.listFiles( IMediaFileConstants.FF_IMAGES ) );
      hasPrev = (ETsCollMove.PREV.findElemAt( f, imgFiles, 10, true ) != null);
      hasNext = (ETsCollMove.NEXT.findElemAt( f, imgFiles, 10, true ) != null);
    }
    toolbar.setActionEnabled( ACTID_OPEN, isEditable() );
    toolbar.setActionEnabled( ACTID_GO_PREV, isEditable() && hasPrev );
    toolbar.setActionEnabled( ACTID_GO_NEXT, isEditable() && hasNext );
    boolean bestFit = imageWidget.getFitInfo().fitMode() == ERectFitMode.FIT_BOTH;
    toolbar.setActionChecked( ACTID_ZOOM_FIT_BEST, bestFit );
    toolbar.setActionChecked( ACTID_ZOOM_ORIGINAL, !bestFit );
  }

  private File getExistingDir() {
    File f = new File( txtFilePath.getText() );
    if( f.exists() ) {
      if( f.isDirectory() ) {
        return f;
      }
      return f.getParentFile();
    }
    File dir = f.getParentFile();
    if( dir != null && dir.isDirectory() ) {
      return dir;
    }
    return null;
  }

  private File getExistingFile() {
    File f = new File( txtFilePath.getText() );
    if( IMediaFileConstants.hasImageExtension( f.getPath() ) && f.exists() ) {
      return f;
    }
    return null;
  }

  private void changeFile( File aFile ) {
    if( aFile != null ) {
      TsImage mi = imageManager().findImage( aFile );
      imageWidget.setTsImage( mi );
      imageWidget.redraw();
      txtFilePath.setText( aFile.getPath() );
      fireChangeEvent();
    }
    else {
      imageWidget.setTsImage( null );
      imageWidget.redraw();
      txtFilePath.setText( EMPTY_STRING );
      fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // переопределенные методы
  //

  @Override
  public TsComposite doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // toolbar
    toolbar = TsToolbar.create( board, tsContext(), EIconSize.IS_16X16, //
        ACDEF_OPEN, ACDEF_ADD, ACDEF_SEPARATOR, //
        ACDEF_ZOOM_FIT_BEST, ACDEF_ZOOM_ORIGINAL, ACDEF_SEPARATOR, //
        ACDEF_GO_PREV, ACDEF_GO_NEXT //
    );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // backplane
    TsComposite pane1 = new TsComposite( board );
    pane1.setLayout( new GridLayout( 2, false ) );
    pane1.setLayoutData( BorderLayout.CENTER );
    // txtFilePath
    Label l = new Label( pane1, SWT.LEFT );
    l.setText( VisumpleM5Model.FILE_PATH.nmName() );
    l.setToolTipText( VisumpleM5Model.FILE_PATH.description() );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
    txtFilePath = new Text( pane1, SWT.BORDER );
    txtFilePath.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    txtFilePath.setMessage( VisumpleM5Model.FILE_PATH.description() );
    txtFilePath.addModifyListener( filePathModifyListener );
    // txtNotes
    l = new Label( pane1, SWT.LEFT );
    l.setText( VisumpleM5Model.NOTES.nmName() );
    l.setToolTipText( VisumpleM5Model.NOTES.description() );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, false ) );
    txtNotes = new Text( pane1, SWT.BORDER );
    txtNotes.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    txtNotes.setMessage( VisumpleM5Model.NOTES.description() );
    // image
    imageWidget = new PdwWidgetSimple( tsContext() );
    imageWidget.createControl( pane1 );
    imageWidget.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 8 ) );
    // setup
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.setAreaPreferredSize( EThumbSize.SZ360.pointSize() );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    updateActionsState();
    return board;
  }

  @Override
  protected void doSetValues( IM5Bunch<Visumple> aValues ) {
    if( aValues == null ) {
      txtFilePath.setText( EMPTY_STRING );
      txtNotes.setText( EMPTY_STRING );
      imageWidget.setTsImage( null );
      currVisumple = null;
      return;
    }
    currVisumple = aValues.originalEntity();
    String filePath = VisumpleM5Model.FILE_PATH.getFieldValue( aValues ).asString();
    txtFilePath.setText( filePath );
    txtNotes.setText( VisumpleM5Model.NOTES.getFieldValue( aValues ).asString() );
    TsImage mi = imageManager().findImage( new File( filePath ) );
    imageWidget.setTsImage( mi );
    imageWidget.redraw();
  }

  @Override
  protected ValidationResult doCollectValues( IM5BunchEdit<Visumple> aBunch ) {
    String filePath = txtFilePath.getText();
    if( filePath.isEmpty() ) {
      return ValidationResult.error( MSG_ERR_NO_FILE_PATH );
    }
    File f = new File( filePath );
    if( !f.exists() ) {
      return ValidationResult.warn( FMT_WARN_NO_SUCH_FILE, f.getAbsolutePath() );
    }
    aBunch.fillFrom( currVisumple, true );
    VisumpleM5Model.FILE_PATH.setFieldValue( aBunch, avStr( txtFilePath.getText() ) );
    VisumpleM5Model.NOTES.setFieldValue( aBunch, avStr( txtNotes.getText() ) );
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doEditableStateChanged() {
    txtFilePath.setEditable( isEditable() );
    txtNotes.setEditable( isEditable() );
    updateActionsState();
  }

}
