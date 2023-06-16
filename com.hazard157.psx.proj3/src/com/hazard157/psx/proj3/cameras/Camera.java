package com.hazard157.psx.proj3.cameras;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * {@link ICamera} implementation.
 *
 * @author hazard157
 */
public class Camera
    extends AbstractSinentity<CameraInfo>
    implements ICamera {

  /**
   * "NULL" object singleton with camera ID {@link IStridable#NONE_ID}. {@link IStridable#isNone()} = <code>true</code>.
   */
  public static final Camera NULL =
      new Camera( NONE_ID, new CameraInfo( EMPTY_STRING, EMPTY_STRING, false, ECameraKind.GENERIC ) );

  /**
   * Constructor.
   *
   * @param aId String - camera ID (an IDPath)
   * @param aInfo {@link CameraInfo} - camera info
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public Camera( String aId, CameraInfo aInfo ) {
    super( aId, aInfo );
  }

  @Override
  public String id() {
    return super.id();
  }

  @Override
  public String nmName() {
    return info().name();
  }

  @Override
  public String description() {
    return info().description();
  }

  @Override
  public boolean isCamAvailable() {
    return info().isStillAvailable();
  }

  @Override
  public ECameraKind kind() {
    return info().kind();
  }

}
