package com.hazard157.lib.core.excl_done.radioprop;

import static com.hazard157.lib.core.excl_done.radioprop.ITsResources.*;
import static com.hazard157.lib.core.excl_done.radioprop.IValedRadioPropEnumConstants.*;
import static org.toxsoft.core.tslib.utils.TsMiscUtils.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.utils.IParameterizedEdit;
import org.toxsoft.core.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Icons in line widget.
 *
 * @author hazard157
 */
class IconsLineWidget
    extends Canvas
    implements ITsGuiContextable, IParameterizedEdit {

  private final GenericChangeEventer genericChangeEventProducer;

  private final ITsGuiContext               tsContext;
  private final IList<? extends IStridable> items;
  private final IStringList                 iconIds;
  private final int                         defaultIndex;

  private boolean editable       = true;
  private int     selectionIndex = 0;

  @SuppressWarnings( "boxing" )
  public IconsLineWidget( Composite aParent, ITsGuiContext aContext, IList<? extends IStridable> aItems,
      IStringList aIconIds, int aDefaultIndex ) {
    super( aParent, SWT.NONE );
    TsNullArgumentRtException.checkNulls( aContext, aItems, aIconIds );
    TsIllegalArgumentRtException.checkTrue( aItems.size() != aIconIds.size() );
    tsContext = aContext;
    TsIllegalArgumentRtException.checkTrue( aItems.isEmpty(), MSG_ERR_EMPTY_ITEMS );
    if( !isInRange( aDefaultIndex, 0, aItems.size() - 1 ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_INV_DEF_INDEX, aDefaultIndex, 0, aItems.size() - 1 );
    }
    defaultIndex = aDefaultIndex;
    items = new ElemArrayList<>( aItems );
    iconIds = new StringArrayList( aIconIds );
    genericChangeEventProducer = new GenericChangeEventer( this );
    addPaintListener( aEvent -> doPaint( aEvent ) );
    addMouseWheelListener( aEvent -> doMouseScrolled( aEvent ) );
    addMouseMoveListener( aEvent -> doMouseMove( aEvent ) );
    addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent aEvent ) {
        doMouseDown( aEvent );
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return tsContext.params();
  }

  // ------------------------------------------------------------------------------------
  // Canvas
  //

  @Override
  public Point computeSize( int aWHint, int aHHint, boolean aChanged ) {
    int count = items.size();
    int w = getIconSize().size() * count + getIconGap() * (count + 1); // все интервалы между и слева и справа
    int h = getIconSize().size() + 2 * getIconGap(); // высота значка плюс сверху и снизу
    return new Point( w, h );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void doPaint( PaintEvent aEvent ) {
    int x = getIconGap();
    int y = getIconGap();
    // process all drawn items
    for( int i = 0; i < items.size(); i++ ) {
      // draw an icon of item
      String iconId = iconIds.get( i );
      if( iconId.isBlank() ) {
        iconId = ITsStdIconIds.ICONID_UNKNOWN_ICON_ID;
      }
      Image icon = iconManager().loadStdIcon( iconId, getIconSize() );
      aEvent.gc.drawImage( icon, x, y );
      // draw selection rectangle
      if( i == selectionIndex ) {
        Color corrFg = aEvent.gc.getForeground();
        aEvent.gc.setForeground( getSelRectColor() );
        int bw = getSelRectBorderThikness();
        int sz = getIconSize().size() + 2 * bw - 1;
        int bx = x - bw;
        int by = y - bw;
        for( int j = 0; j < bw; j++, sz -= 2 ) {
          aEvent.gc.drawRectangle( bx + j, by + j, sz, sz );
        }
        aEvent.gc.setForeground( corrFg );
      }
      x += getIconSize().size() + getIconGap();
    }
  }

  public void doMouseDown( MouseEvent aEvent ) {
    if( !isEditable() ) {
      return;
    }
    switch( aEvent.button ) {
      case 1: { // left button
        int newIndex = getIndexOfCoor( aEvent.x );
        if( newIndex != selectionIndex ) {
          selectionIndex = newIndex;
          redraw();
          genericChangeEventProducer.fireChangeEvent();
        }
        break;
      }
      case 2: { // middle button
        if( selectionIndex != defaultIndex ) {
          selectionIndex = defaultIndex;
          redraw();
          genericChangeEventProducer.fireChangeEvent();
        }
        break;
      }
      default:
        break;
    }
  }

  public void doMouseScrolled( MouseEvent aEvent ) {
    if( !isEditable() ) {
      return;
    }
    int newIndex = selectionIndex;
    if( aEvent.count > 0 ) {
      ++newIndex;
    }
    if( aEvent.count < 0 ) {
      --newIndex;
    }
    newIndex = inRange( newIndex, 0, items.size() - 1 );

    if( newIndex != selectionIndex ) {
      selectionIndex = newIndex;
      redraw();
      genericChangeEventProducer.fireChangeEvent();
    }
  }

  public void doMouseMove( MouseEvent aEvent ) {
    if( !isEditable() ) {
      return;
    }
    int index = getIndexOfCoor( aEvent.x );
    String tooltip = TsLibUtils.EMPTY_STRING;
    if( items.isInRange( index ) ) {
      String lclickName = items.get( index ).nmName();
      String rclickName = items.get( defaultIndex ).nmName();
      tooltip = String.format( FMT_P_SELECION_LINE, lclickName, rclickName );
    }
    IconsLineWidget.this.setToolTipText( tooltip );
  }

  /**
   * Returns the index of icon in line under specified coors.
   *
   * @param aX int - X-coordinate relative to this widget
   * @return int - always valid index of icon in {@link ValedRadioPropEnumIcons#allItems}
   */
  private int getIndexOfCoor( int aX ) {
    int x = aX - getIconGap();
    if( x < 0 ) {
      return 0;
    }
    int index = aX / (getIconSize().size() + getIconGap());
    if( index > items.size() ) {
      return items.size();
    }
    return index;
  }

  final EIconSize getIconSize() {
    return OPDEF_ICON_SIZE.getValue( params() ).asValobj();
  }

  final int getIconGap() {
    return OPDEF_ICON_GAP.getValue( params() ).asInt();
  }

  final int getSelRectBorderThikness() {
    return getIconGap() - 1;
  }

  final Color getSelRectColor() {
    RGB rgb = OPDEF_SEL_RECT_COLOR.getValue( params() ).asValobj();
    return colorManager().getColor( rgb );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public int getSelectionIndex() {
    return selectionIndex;
  }

  public void setSelectionIndex( int aIndex ) {
    if( selectionIndex != aIndex ) {
      selectionIndex = aIndex;
      redraw();
    }
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable( boolean aValue ) {
    editable = aValue;
    redraw();
  }

  // ------------------------------------------------------------------------------------
  // To be overriden
  //

  IGenericChangeEventer eventer() {
    return genericChangeEventProducer;
  }

}
