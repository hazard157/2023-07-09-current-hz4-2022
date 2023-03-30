package com.hazard157.psx24.core.m5.camera;

import static com.hazard157.psx24.core.m5.camera.CameraM5Model.*;
import static com.hazard157.psx24.core.m5.camera.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.cameras.*;

class CamerasLifecycleManager
    extends M5LifecycleManager<Camera, IUnitCameras> {

  public CamerasLifecycleManager( IM5Model<Camera> aModel, IUnitCameras aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<Camera> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    if( master().items().hasKey( id ) ) {
      return ValidationResult.error( FMT_ERR_CAMERA_ID_ALREADY_EXISTS, id );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected Camera doCreate( IM5Bunch<Camera> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    String name = aValues.getAsAv( FID_NAME ).asString();
    String description = aValues.getAsAv( FID_DESCRIPTION ).asString();
    boolean isAvailable = aValues.getAsAv( FID_IS_CAM_AVAILABLE ).asBool();
    ECameraKind kind = aValues.getAsAv( FID_CAMERA_KIND ).asValobj();
    return master().createItem( id, new CameraInfo( name, description, isAvailable, kind ) );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<Camera> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    if( !id.equals( aValues.originalEntity().id() ) ) {
      if( master().items().hasKey( id ) ) {
        return ValidationResult.error( FMT_ERR_CAMERA_ID_ALREADY_EXISTS, id );
      }
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected Camera doEdit( IM5Bunch<Camera> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    String name = aValues.getAsAv( FID_NAME ).asString();
    String description = aValues.getAsAv( FID_DESCRIPTION ).asString();
    boolean isAvailable = aValues.getAsAv( FID_IS_CAM_AVAILABLE ).asBool();
    ECameraKind kind = aValues.getAsAv( FID_CAMERA_KIND ).asValobj();
    return master().editItem( aValues.originalEntity().id(), id,
        new CameraInfo( name, description, isAvailable, kind ) );
  }

  @Override
  protected ValidationResult doBeforeRemove( Camera aEntity ) {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( Camera aEntity ) {
    master().removeItem( aEntity.id() );
  }

  @Override
  protected IList<Camera> doListEntities() {
    return master().items();
  }

}
