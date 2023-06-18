package com.hazard157.prisex24.m5.std;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.std.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.prisex24.e4.services.psx.*;
import com.hazard157.prisex24.valeds.frames.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Field {@link IFrameable#frame()}.
 *
 * @author hazard157
 * @param <T> - {@link IFrameable} implementing type
 */
public class PsxM5FrameFieldDef<T extends IFrameable>
    extends M5SingleLookupFieldDef<T, IFrame> {

  /**
   * ID of field {@link IFrameable#frame()}.
   */
  public static final String FID_FRAME = "Frame"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public PsxM5FrameFieldDef() {
    super( FID_FRAME, MID_FRAME );
    setNameAndDescription( STR_FD_FRAME, STR_FD_FRAME_D );
    setValedEditor( ValedFrameFactory.FACTORY_NAME );
    setFlags( M5FF_DETAIL );
    setDefaultValue( IFrame.NONE );
    params().setValueIfNull( OPID_IS_WIDTH_FIXED, AV_FALSE );
    params().setValueIfNull( OPID_IS_HEIGHT_FIXED, AV_FALSE );
  }

  @Override
  protected IFrame doGetFieldValue( T aEntity ) {
    return aEntity.frame();
  }

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    // если есть изображение, то текст не нужен
    // (здесь можно проверять любой размер, просто, что есть файл кадра и оно нормальное изображение)
    if( doGetFieldValueThumb( aEntity, EThumbSize.SZ128 ) == null ) {
      return doGetFieldValue( aEntity ).toString();
    }
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  protected Image doGetFieldValueIcon( T aEntity, EIconSize aIconSize ) {
    IPrisex24Service psxServ = tsContext().get( IPrisex24Service.class );
    TsImage mi = psxServ.findThumb( aEntity.frame(), EThumbSize.findIncluding( aIconSize ) );
    if( mi != null ) {
      return mi.image();
    }
    return null;
  }

  @Override
  protected TsImage doGetFieldValueThumb( T aEntity, EThumbSize aThumbSize ) {
    IPrisex24Service psxServ = tsContext().get( IPrisex24Service.class );
    return psxServ.findThumb( aEntity.frame(), aThumbSize );
  }

}
