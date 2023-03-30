package com.hazard157.psx24.core.m5.std;

import static com.hazard157.psx24.core.m5.std.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.std.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.proj3.cameras.*;

/**
 * Поле {@link ICameraIdable#cameraId()}.
 *
 * @author goga
 * @param <T> - конкретный тип, реализующмй {@link ICameraIdable}
 */
public class PsxM5CameraIdFieldDef<T extends ICameraIdable>
    extends M5SingleLookupFieldDef<T, String> {

  /**
   * Идентификатор поля {@link ICameraIdable#cameraId()}.
   */
  public static final String FID_CAMERA_ID = "CameraId"; //$NON-NLS-1$

  private static final String NO_CAM_ID_STRING = "---"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public PsxM5CameraIdFieldDef() {
    super( FID_CAMERA_ID, StringM5Model.MODEL_ID );
  }

  @Override
  protected void doInit() {
    setNameAndDescription( STR_N_FIELD_CAMERA_ID, STR_D_FIELD_CAMERA_ID );
    setFlags( M5FF_COLUMN );
    setDefaultValue( IStridable.NONE_ID );
    validator().addValidator( IdPathStringValidator.IDPATH_VALIDATOR );
    setLookupProvider( new IM5LookupProvider<String>() {

      @Override
      public IList<String> listItems() {
        IUnitCameras uc = tsContext().get( IUnitCameras.class );
        return new ElemArrayList<>( uc.items().keys() );
      }

      @Override
      public String getName( String aItem ) {
        return aItem;
      }

    } );
  }

  @Override
  protected String doGetFieldValue( T aEntity ) {
    return aEntity.cameraId();
  }

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    String s = aEntity.cameraId();
    if( s.equals( IStridable.NONE_ID ) ) {
      return NO_CAM_ID_STRING;
    }
    return s;
  }

}
