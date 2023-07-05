package com.hazard157.prisex24.valeds.frames;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IFrameable#frame()} as {@link IAtomicValue} editor.
 *
 * @author hazard157
 */
public class ValedAvValobjFrameEditor
    extends AbstractAvValobjWrapperValedControl<IFrame> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjS5HostList"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjFrameEditor( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( Frame.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjFrameEditor( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedFrameFactory.FACTORY );
  }

}
