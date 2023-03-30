package com.hazard157.psx.proj3.cameras.impl;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.proj3.cameras.*;

/**
 * Реализация {@link IUnitCameras}.
 *
 * @author hazard157
 */
public class UnitCameras
    extends AbstractSinentManager<Camera, CameraInfo>
    implements IUnitCameras {

  /**
   * Конструктор.
   */
  public UnitCameras() {
    super( TsLibUtils.EMPTY_STRING, CameraKeeper.KEEPER );
  }

  @Override
  protected Camera doCreateItem( String aId, CameraInfo aInfo ) {
    return new Camera( aId, aInfo );
  }

}
