package com.hazard157.psx24.core.m5.std;

import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static com.hazard157.psx24.core.m5.std.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.valeds.frames.*;

/**
 * Поле {@link IFrameable#frame()}.
 *
 * @author goga
 * @param <T> - тип сущности, реализующий {@link IFrameable}
 */
public class PsxM5FrameFieldDef<T extends IFrameable>
    extends M5SingleLookupFieldDef<T, IFrame> {

  /**
   * Идентификатор поля {@link IFrameable#frame()}.
   */
  public static final String FID_FRAME = "Frame"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public PsxM5FrameFieldDef() {
    super( FID_FRAME, MID_FRAME );
    setNameAndDescription( STR_N_FD_FRAME, STR_D_FD_FRAME );
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
    IPsxFileSystem fileSystem = tsContext().get( IPsxFileSystem.class );
    File file = fileSystem.findFrameFile( doGetFieldValue( aEntity ) );
    if( file != null ) {
      TsImage mi = imageManager().findThumb( file, EThumbSize.findIncluding( aIconSize ) );
      if( mi != null ) {
        return mi.image();
      }
    }
    return null;
  }

  @Override
  protected TsImage doGetFieldValueThumb( T aEntity, EThumbSize aThumbSize ) {
    IPsxFileSystem fileSystem = tsContext().get( IPsxFileSystem.class );
    File file = fileSystem.findFrameFile( doGetFieldValue( aEntity ) );
    if( file != null ) {
      TsImage mi = imageManager().findThumb( file, aThumbSize );
      if( mi != null ) {
        return mi;
      }
    }
    return null;
  }

}
