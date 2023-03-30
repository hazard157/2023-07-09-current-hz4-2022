package com.hazard157.psx.proj3.cameras;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Камера, используемая для съемок.
 *
 * @author hazard157
 */
public class Camera
    extends AbstractSinentity<CameraInfo>
    implements ICamera {

  /**
   * "Нулевой" объект для камер.
   * <p>
   * Поскольку у этого экземпляра идентификатор равен {@link IStridable#NONE_ID}, то и признак
   * {@link IStridable#isNone()} = <code>true</code>.
   */
  public static final Camera NULL =
      new Camera( NONE_ID, new CameraInfo( EMPTY_STRING, EMPTY_STRING, false, ECameraKind.GENERIC ) );

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор камеры (ИД-путь)
   * @param aInfo {@link CameraInfo} - описание камеры
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-путь
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

  /**
   * Возвращает {@link CameraInfo#isStillAvailable()}.
   *
   * @return boolean - признак, что камера еще доступна для съемок
   */
  @Override
  public boolean isCamAvailable() {
    return info().isStillAvailable();
  }

  @Override
  public ECameraKind kind() {
    return info().kind();
  }

}
