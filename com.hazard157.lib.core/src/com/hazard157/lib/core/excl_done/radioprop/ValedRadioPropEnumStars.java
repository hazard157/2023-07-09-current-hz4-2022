package com.hazard157.lib.core.excl_done.radioprop;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.excl_done.radioprop.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * {@link IRadioPropEnum} editor as yellow/dimmed stars line.
 *
 * @author hazard157
 * @param <V> - the edited enum type
 */
public class ValedRadioPropEnumStars<V extends Enum<V> & IRadioPropEnum>
    extends AbstractValedControl<V, Control> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".RadioPropEnumStars"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   * @param <V> - the edited enum type
   */
  static class Factory<V extends Enum<V> & IRadioPropEnum>
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<V> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<V, ?> e = new ValedRadioPropEnumStars<>( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory<>();

  /**
   * Star line drawing widget.
   *
   * @author hazard157
   */
  class VrssWidget
      extends Canvas {

    public VrssWidget( Composite aParent ) {
      super( aParent, SWT.NONE );
      addPaintListener( new PaintListener() {

        @Override
        public void paintControl( PaintEvent aE ) {
          int index = valueItems.indexOf( value ); // индекс последней желтой звездочки (начинается от -1)
          for( int i = 0; i < valueItems.size(); i++ ) {
            String iconId;
            if( i > index ) {
              iconId = ICON_STAR_DIMMED;
            }
            else {
              iconId = ICON_STAR_YELLOW;
            }
            Image icon = iconManager().loadStdIcon( iconId, getIconSize() );
            aE.gc.drawImage( icon, i * getIconSize().size(), 0 );
          }
        }
      } );
      addMouseListener( new MouseAdapter() {

        @Override
        public void mouseDown( MouseEvent aE ) {
          if( !isEditable() ) {
            return;
          }
          switch( aE.button ) {
            case 1: { // left button
              int index = aE.x / getIconSize().size();
              V newValue = valueItems.get( index );
              if( newValue != value ) {
                value = newValue;
                redraw();
                fireModifyEvent( true );
              }
              break;
            }
            case 2: { // middle button
              if( value != getUnknownConstant() ) {
                value = getUnknownConstant();
                redraw();
                fireModifyEvent( true );
              }
              break;
            }
            default:
              break;
          }
        }

      } );
      addMouseWheelListener( new MouseWheelListener() {

        @Override
        public void mouseScrolled( MouseEvent aE ) {
          if( !isEditable() ) {
            return;
          }
          V newValue = value;
          if( aE.count > 0 ) {
            newValue = allItems.next( value );
          }
          if( aE.count < 0 ) {
            newValue = allItems.prev( value );
          }
          if( newValue != null && newValue != value ) {
            value = newValue;
            redraw();
            fireModifyEvent( true );
          }
        }
      } );
      addMouseMoveListener( new MouseMoveListener() {

        @Override
        public void mouseMove( MouseEvent aE ) {
          if( !isEditable() ) {
            return;
          }
          int index = aE.x / getIconSize().size();
          String tooltip = TsLibUtils.EMPTY_STRING;
          if( valueItems.isInRange( index ) ) {
            String lclickName = valueItems.get( index ).nmName();
            String rclickName = getUnknownConstant().nmName();
            tooltip = String.format( FMT_P_SELECION_LINE, lclickName, rclickName );
          }
          VrssWidget.this.setToolTipText( tooltip );
        }
      } );
    }

    @Override
    public Point computeSize( int aWHint, int aHHint, boolean aChanged ) {
      int w = getIconSize().size() * valueItems.size();
      int h = getIconSize().size();
      return new Point( w, h );
    }

  }

  /**
   * All constants of enum in order of declaration so first item must have ID {@link IRadioPropEnum#UNKNOWN_ID}.
   */
  final IList<V> allItems;

  /**
   * Список значимых rjycnfyn по мере возрастания, то есть, кронстанты {@link #allItems} без
   * {@link IRadioPropEnum#UNKNOWN_ID}.
   */
  final IList<V> valueItems;
  V              value;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedRadioPropEnumStars( ITsGuiContext aTsContext ) {
    super( aTsContext );
    Class<V> enumClass = IValedEnumConstants.getEnumClassFromContext( aTsContext );
    allItems = new ElemArrayList<>( enumClass.getEnumConstants() );
    IListEdit<V> ll = new ElemArrayList<>( allItems );
    V unknownItem = ll.removeByIndex( 0 );
    if( !unknownItem.isUnknown() ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_NO_FIRST_UNKNOWN_VALUE_IN_ENUM, enumClass.getSimpleName() );
    }
    valueItems = ll;
    value = getUnknownConstant();
  }

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @param aEnumClass {@link Class}&lt;V&gt; - the enumeration class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedRadioPropEnumStars( ITsGuiContext aTsContext, Class<V> aEnumClass ) {
    super( aTsContext );
    allItems = new ElemArrayList<>( aEnumClass.getEnumConstants() );
    IListEdit<V> ll = new ElemArrayList<>( allItems );
    V unknownItem = ll.removeByIndex( 0 );
    if( !unknownItem.isUnknown() ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_NO_FIRST_UNKNOWN_VALUE_IN_ENUM, aEnumClass.getSimpleName() );
    }
    valueItems = ll;
    value = getUnknownConstant();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private final V getUnknownConstant() {
    return allItems.first();
  }

  private final EIconSize getIconSize() {
    return IValedRadioPropEnumConstants.OPDEF_ICON_SIZE.getValue( params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected VrssWidget doCreateControl( Composite aParent ) {
    VrssWidget c = new VrssWidget( aParent );
    return c;
  }

  @Override
  protected void doSetTooltip( String aTooltipText ) {
    getControl().setToolTipText( aTooltipText );
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected V doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    value = (aValue == null) ? getUnknownConstant() : aValue;
    if( getControl() != null ) {
      getControl().redraw();
    }
  }

  @Override
  protected void doClearValue() {
    value = getUnknownConstant();
    if( getControl() != null ) {
      getControl().redraw();
    }
  }

}
