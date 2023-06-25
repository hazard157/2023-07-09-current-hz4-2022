package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;

/**
 * Пометка - продолжительная по времени пометка видеоряда какой-либо сущностью (маркером).
 * <p>
 * Это неизменяемый класс.
 * <p>
 * Реализует интерфес {@link Comparable}, сравнивая только поля {@link #in()}.
 *
 * @author hazard157
 * @param <E> - конкртеный тип маркера
 */
public class Mark<E>
    implements IFrameable, ICameraIdable, IEpisodeIdable, IEpisodeIntervalable {

  private final Secint in;
  private final E      marker;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aIn {@link Secint} - интервал времени
   * @param aMarker E - маркер на интервале
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public Mark( Secint aIn, E aMarker ) {
    TsNullArgumentRtException.checkNulls( aIn, aMarker );
    in = aIn;
    marker = aMarker;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает инетрвал времени пометки.
   *
   * @return {@link Secint} - интервал времени, не бывает null
   */
  public Secint in() {
    return in;
  }

  /**
   * Возвращает маркер.
   *
   * @return E - маркер на интервале, не бывает null
   */
  public E marker() {
    return marker;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return in.toString() + "=" + marker.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof Mark ) {
      Mark<?> that = (Mark<?>)aThat;
      if( in.equals( that.in ) ) {
        return marker.equals( that.marker );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + in.hashCode();
    result = TsLibUtils.PRIME * result + marker.hashCode();
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIdable
  //

  @Override
  public String episodeId() {
    if( marker instanceof IEpisodeIdable ) {
      return ((IEpisodeIdable)marker).episodeId();
    }
    return EpisodeUtils.EPISODE_ID_NONE;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICameraIdable
  //

  @Override
  public String cameraId() {
    if( marker instanceof ICameraIdable ) {
      return ((ICameraIdable)marker).cameraId();
    }
    return IStridable.NONE_ID;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    if( marker instanceof IFrameable ) {
      return ((IFrameable)marker).frame();
    }
    return IFrame.NONE;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIntervalable
  //

  @Override
  public Secint interval() {
    return in;
  }

}
