package com.hazard157.prisex24.valeds.frames;

import static com.hazard157.prisex24.valeds.frames.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Factory of {@link IFrameable#frame()} editor VALEDs.
 *
 * @author hazard157
 */
public class ValedFrameFactory
    extends AbstractValedControlFactory {

  /**
   * Option: thumbnail size for frame preview
   */
  public static final IDataDef OPDEF_THUMB_SIZE = DataDef.create( "ValedFrame.ThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_THUMB_SIZE_D, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128, EThumbSize.KEEPER, EThumbSize.KEEPER_ID ) //
  );

  /**
   * Factory name.
   */
  public static final String FACTORY_NAME = "ValedFrameFactory"; //$NON-NLS-1$

  /**
   * Factory singleton.
   */
  public static final ValedFrameFactory FACTORY = new ValedFrameFactory();

  private ValedFrameFactory() {
    super( FACTORY_NAME );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IValedControl<IFrame> doCreateEditor( ITsGuiContext aContext ) {
    return new ValedFrameEditor( aContext );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  protected IValedControl<IFrame> doCreateViewer( ITsGuiContext aContext ) {
    return new ValedFrameViewer( aContext );
  }

}
