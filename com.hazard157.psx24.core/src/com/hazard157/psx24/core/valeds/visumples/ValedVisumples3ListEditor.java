package com.hazard157.psx24.core.valeds.visumples;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.visumple3.*;

/**
 * Editor for {@link IVisumples3List}.
 *
 * @author hazard157
 */
public class ValedVisumples3ListEditor
    extends AbstractValedControl<IVisumples3List, Control> {

  /**
   * Factory name.
   */
  public static final String FACTORY_NAME = "Visumples3ListEditor"; //$NON-NLS-1$

  /**
   * Factory singleton.
   */
  public static final IValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IVisumples3List> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedVisumples3ListEditor( aContext );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IVisumples3List> doCreateViewer( ITsGuiContext aContext ) {
      return new ValedVisumples3ListViewer( aContext );
    }

  };

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedVisumples3ListEditor( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected IVisumples3List doGetUnvalidatedValue() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doSetUnvalidatedValue( IVisumples3List aValue ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void doClearValue() {
    // TODO Auto-generated method stub

  }

}
