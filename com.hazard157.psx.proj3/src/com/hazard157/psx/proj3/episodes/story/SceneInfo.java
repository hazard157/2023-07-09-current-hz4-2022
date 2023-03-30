package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Неизменяемый класс - описание сцены {@link IScene}.
 *
 * @author hazard157
 */
public final class SceneInfo
    implements IFrameable {

  /**
   * "Нулевое" описание сцены.
   */
  public static SceneInfo NULL = new SceneInfo( TsLibUtils.EMPTY_STRING, IFrame.NONE );

  private final String name;
  private final IFrame frame;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aName String - название сцены
   * @param aFrame {@link IFrame} - иллюстрация
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SceneInfo( String aName, IFrame aFrame ) {
    TsNullArgumentRtException.checkNulls( aName, aFrame );
    name = aName;
    frame = aFrame;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает название сцены.
   *
   * @return String - название сцены
   */
  public String name() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  /**
   * Возвращает иллюстрацию.
   *
   * @return {@link IFrame} - иллюстрация
   */
  @Override
  public IFrame frame() {
    return frame;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof SceneInfo ) {
      SceneInfo obj = (SceneInfo)aObj;
      return name.equals( obj.name ) && frame.equals( obj.frame );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + name.hashCode();
    result = TsLibUtils.PRIME * result + frame.hashCode();
    return result;
  }

}
