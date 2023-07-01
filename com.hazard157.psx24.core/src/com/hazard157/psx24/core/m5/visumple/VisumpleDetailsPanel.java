package com.hazard157.psx24.core.m5.visumple;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.visumple.*;

/**
 * Панель детальной информации сущностей {@link Visumple}.
 *
 * @author hazard157
 */
final class VisumpleDetailsPanel
    extends M5AbstractEntityPanel<Visumple> {

  private final ITsActionHandler toolbarListener = this::processAction;

  TsToolbar          toolbar;
  IPdwWidget         imageWidget;
  IM5Bunch<Visumple> value = null;

  EThumbSize thumbSize = EThumbSize.SZ360;

  VisumpleDetailsPanel( ITsGuiContext aContext, IM5Model<Visumple> aModel ) {
    super( aContext, aModel, true );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void processAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    switch( aActionId ) {
      case ACTID_ZOOM_FIT_BEST:
        imageWidget.setFitInfo( RectFitInfo.BEST );
        imageWidget.redraw();
        break;
      case ACTID_ZOOM_ORIGINAL:
        imageWidget.setFitInfo( RectFitInfo.NONE );
        imageWidget.redraw();
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    updateActionsState();
  }

  private void updateActionsState() {
    boolean bestFit = imageWidget.getFitInfo().fitMode() == ERectFitMode.FIT_BOTH;
    toolbar.setActionChecked( ACTID_ZOOM_FIT_BEST, bestFit );
    toolbar.setActionChecked( ACTID_ZOOM_ORIGINAL, !bestFit );
  }

  // ------------------------------------------------------------------------------------
  // переопределенные методы
  //

  @Override
  public TsComposite doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent ) {

      @Override
      public Point computeSize( int wHint, int hHint, boolean changed ) {
        Point p = super.computeSize( wHint, hHint, changed );
        return p;
      }

      @Override
      public Point computeSize( int wHint, int hHint ) {
        Point p = super.computeSize( wHint, hHint );
        return p;
      }

    };
    board.setLayout( new BorderLayout() );
    // toolbar
    // toolbar =
    // new TsToolbar( board, EMPTY_STRING, EIconSize.IS_16X16, true, ACDEF_ZOOM_FIT_BEST, ACDEF_ZOOM_ORIGINAL );
    toolbar = TsToolbar.create( board, tsContext(), EIconSize.IS_16X16, //
        ACDEF_ZOOM_FIT_BEST, ACDEF_ZOOM_ORIGINAL //
    );
    //
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // image
    imageWidget = new PdwWidgetSimple( tsContext() );
    imageWidget.createControl( board );
    // imageWidget.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
    imageWidget.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.setAreaPreferredSize( thumbSize.pointSize() );
    imageWidget.setPreferredSizeFixed( true );
    // imageWidget.setFulcrum( ETsRectFulcrum.CENTER );
    imageWidget.redraw();
    return board;
  }

  @Override
  protected void doSetValues( IM5Bunch<Visumple> aValues ) {
    value = aValues;
    if( aValues == null ) {
      imageWidget.setTsImage( null );
      imageWidget.redraw();
      return;
    }
    String filePath = VisumpleM5Model.FILE_PATH.getFieldValue( aValues ).asString();
    TsImage mi = imageManager().findThumb( new File( filePath ), thumbSize );
    imageWidget.setTsImage( mi );
    imageWidget.redraw();
  }

  @Override
  protected ValidationResult doCollectValues( IM5BunchEdit<Visumple> aBunch ) {
    aBunch.fillFrom( value, true );
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doEditableStateChanged() {
    // nop
  }

}
