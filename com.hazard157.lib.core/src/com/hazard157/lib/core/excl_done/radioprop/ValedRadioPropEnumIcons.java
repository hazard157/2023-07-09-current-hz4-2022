package com.hazard157.lib.core.excl_done.radioprop;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * {@link IRadioPropEnum} editor as icons {@link IRadioPropEnum#iconId()} line.
 *
 * @author hazard157
 * @param <V> - the edited enum type
 */
public class ValedRadioPropEnumIcons<V extends Enum<V> & IRadioPropEnum>
    extends AbstractValedControl<V, IconsLineWidget> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".RadioPropEnumIcons"; //$NON-NLS-1$

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
      AbstractValedControl<V, ?> e = new ValedRadioPropEnumIcons<>( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory<>();

  /**
   * All constants of enum in order of declaration so first item must have ID {@link IRadioPropEnum#UNKNOWN_ID}.
   */
  final IList<V> allItems;

  /**
   * Kepps value before {@link #createControl(Composite)} call.<br>
   * After getControl() is created, value is hold in {@link IconsLineWidget}.
   */
  int initialValueIndex;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedRadioPropEnumIcons( ITsGuiContext aTsContext ) {
    super( aTsContext );
    Class<V> enumClass = IValedEnumConstants.getEnumClassFromContext( aTsContext );
    allItems = new ElemArrayList<>( enumClass.getEnumConstants() );
    initialValueIndex = 0; // UNKNOWN item
  }

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @param aEnumClass {@link Class}&lt;V&gt; - the enumeration class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedRadioPropEnumIcons( ITsGuiContext aTsContext, Class<V> aEnumClass ) {
    super( aTsContext );
    allItems = new ElemArrayList<>( aEnumClass.getEnumConstants() );
    initialValueIndex = 0; // UNKNOWN item
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected IconsLineWidget doCreateControl( Composite aParent ) {
    IStringListEdit iconIds = new StringArrayList();
    for( V v : allItems ) {
      iconIds.add( v.iconId() != null ? v.iconId() : TsLibUtils.EMPTY_STRING );
    }
    IconsLineWidget widget = new IconsLineWidget( aParent, tsContext(), allItems, iconIds, 0 );
    widget.eventer().addListener( widgetValueChangeListener );
    return widget;
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
    int index = initialValueIndex;
    if( getControl() != null ) {
      index = getControl().getSelectionIndex();
    }
    return allItems.get( index );
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    V value = (aValue == null) ? allItems.first() : aValue;
    int index = allItems.indexOf( value );
    if( index < 0 ) {
      index = 0;
    }
    if( getControl() != null ) {
      getControl().setSelectionIndex( index );
    }
  }

  @Override
  protected void doClearValue() {
    initialValueIndex = 0;
    if( getControl() != null ) {
      getControl().setSelectionIndex( 0 );
    }
  }

}
