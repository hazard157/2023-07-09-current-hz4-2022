package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Репер начала плана съемки (какой камерой показывать происходящее).
 *
 * @author hazard157
 */
public class PlaneGuide
    implements IFrameable, ICameraIdable {

  /**
   * Репер, используемый по умолчанию, для обозначения еще не размеченного пространства.
   */
  public static final PlaneGuide FILLER = new PlaneGuide( IStridable.NONE_ID, "???", IFrame.NONE, false ); //$NON-NLS-1$

  private final String  camId;
  private final String  name;
  private final IFrame  frame;
  private final boolean naturallyLong;

  /**
   * Конструктор.
   *
   * @param aCamId String - идентификато (ИД-путь) камеры
   * @param aName String - название репера
   * @param aFrame {@link IFrame} - иллюстрирующие кадр
   * @param aNaturallyLong boolean - признак естественно длинного плана
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException aSec выходит за допустимые пределы
   */
  public PlaneGuide( String aCamId, String aName, IFrame aFrame, boolean aNaturallyLong ) {
    TsNullArgumentRtException.checkNulls( aName, aFrame );
    camId = StridUtils.checkValidIdPath( aCamId );
    name = aName;
    frame = aFrame;
    naturallyLong = aNaturallyLong;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает название репера.
   *
   * @return String - название репера
   */
  public String name() {
    return name;
  }

  /**
   * Возвращает признак естественно длинного плана.
   * <p>
   * Используется дл отмены проверки длительности. Может еще в чем-то пригодится :)
   *
   * @return boolean - признак естественно длинного плана
   */
  public boolean isNaturallyLong() {
    return naturallyLong;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICameraIdable
  //

  @Override
  public String cameraId() {
    return camId;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    return frame;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return camId + " - " + name() + " isLong=" + Boolean.toString( naturallyLong ); //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof PlaneGuide ) {
      PlaneGuide that = (PlaneGuide)aObj;
      return camId.equals( that.camId ) && name.equals( that.name ) && frame.equals( that.frame )
          && naturallyLong == that.naturallyLong;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + camId.hashCode();
    result = TsLibUtils.PRIME * result + name.hashCode();
    result = TsLibUtils.PRIME * result + frame.hashCode();
    result = TsLibUtils.PRIME * result + (naturallyLong ? 1 : 0);
    return result;
  }

}
