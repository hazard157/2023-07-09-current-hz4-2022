package com.hazard157.prisex24.utils;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.utils.IPsxResources.*;
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

import com.hazard157.common.dialogs.*;
import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Creates GIF management drop-down menu for the action {@link IPrisex24CoreConstants#ACDEF_GIF_CREATE_MENU}.
 * <p>
 * Creates menu with actions, some of them are handled here, but others require external handling. The actions are:
 * <ul>
 * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_CREATE} - needs external handling;</li>
 * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_TEST} - handled locally;</li>
 * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_RECREATE_ALL} - needs external handling;</li>
 * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_REMOVE} - needs external handling;</li>
 * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_INFO} - handled locally.</li>
 * </ul>
 *
 * @author hazard157
 */
public abstract class AbstractGifManagemntDropDownMenuCreator
    extends AbstractMenuCreator
    implements IPsxGuiContextable {

  private final ITsGuiContext    tsContext;
  private final ITsActionHandler actionHandler;

  /**
   * Constructor.
   * <p>
   * The following actions must be handled by the specified handler:
   * <ul>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_CREATE} - create GIF at selected frame;</li>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_RECREATE_ALL} - recreate all GIFs in the speicifed context;</li>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_REMOVE} - remove specified GIF. Called only if the selected frame is an
   * animated GIF.</li>
   * </ul>
   *
   * @param aContext - {@link ITsGuiContext} - the context
   * @param aActionHandler {@link ITsActionHandler} - handles action not handled by this class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractGifManagemntDropDownMenuCreator( ITsGuiContext aContext, ITsActionHandler aActionHandler ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
    actionHandler = aActionHandler;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void internalHandler( String aActionId ) {
    IFrame sel = doGetFrame();
    if( sel == null ) {
      return;
    }
    switch( aActionId ) {
      case ACTID_GIF_TEST: {
        TestGifFileCreator gc = new TestGifFileCreator( sel, tsContext );
        ProgressMonitorDialog d = new ProgressMonitorDialog( getShell() );
        try {
          d.run( true, false, gc );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
          TsDialogUtils.error( getShell(), ex );
        }
        TsImage mi = gc.getGeneratedImage();
        if( mi == null ) {
          TsDialogUtils.warn( getShell(), MSG_WARN_NOT_ALL_FRAMES_FOR_GIF );
          break;
        }
        String title = String.format( DLG_FMT_SHOW_TEST_GIF_D, sel.toString() );
        DialogShowMultiImage.showImage( tsContext, mi, null, title );
        // don't dispose image - it's frames are managed by ITsImageManager
        break;
      }
      case ACTID_GIF_INFO: {
        TsInternalErrorRtException.checkNull( sel );
        File file = cofsFrames().findFrameFile( sel );
        if( file == null ) {
          TsTestUtils.pl( FMT_ERR_LOAD_FROM_FRAME_FILE, sel.toString() );
          return;
        }
        TsImage mi = imageManager().findImage( file );
        if( mi == null ) {
          TsTestUtils.pl( FMT_ERR_LOAD_FROM_FRAME_FILE, sel.toString() );
          break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append( file.getAbsolutePath() );
        sb.append( '\n' );
        for( String s : getInfoText( mi ) ) {
          sb.append( s );
          sb.append( '\n' );
        }
        TsDialogUtils.info( getShell(), sb.toString() );
        break;
      }
      case ACTID_GIF_REMOVE: {
        if( sel.isAnimated() ) { // handle remove only for animated GIF frames
          actionHandler.handleAction( aActionId );
        }
        break;
      }
      default:
        actionHandler.handleAction( aActionId );
        break;
    }
  }

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    IFrame sel = doGetFrame();
    if( sel == null ) {
      return false;
    }
    ITsIconManager iconManager = tsContext.get( ITsIconManager.class );
    IListEdit<ITsActionDef> actInfoes = new ElemArrayList<>( //
        ACDEF_GIF_CREATE, //
        ACDEF_GIF_TEST, //
        ACDEF_GIF_RECREATE_ALL, //
        ACDEF_SEPARATOR, //
        ACDEF_GIF_REMOVE, //
        ACDEF_GIF_INFO //
    );
    for( ITsActionDef actInfo : actInfoes ) {
      if( actInfo == ACDEF_SEPARATOR ) {
        @SuppressWarnings( "unused" )
        MenuItem separator = new MenuItem( aMenu, SWT.SEPARATOR );
        continue;
      }
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
      if( actInfo == ACDEF_REMOVE ) {
        mItem.setEnabled( sel.isAnimated() );
      }
    }
    return true;
  }

  /**
   * Returns multiline text - information about the image.
   *
   * @param aMi {@link TsImage} - the image
   * @return {@link IStringList} - multiline text
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

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must return the frame, for which the menu is created.
   *
   * @return {@link IFrame} - the frame or <code>null</code>
   */
  protected abstract IFrame doGetFrame();

}
