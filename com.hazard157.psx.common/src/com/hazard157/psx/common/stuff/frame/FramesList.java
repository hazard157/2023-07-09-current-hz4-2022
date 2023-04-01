package com.hazard157.psx.common.stuff.frame;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IFramesList}.
 *
 * @author hazard157
 */
public class FramesList
    extends ElemArrayList<IFrame>
    implements IFramesList {

  private static final long serialVersionUID = -3047511022696994344L;

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "FramesList"; //$NON-NLS-1$

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<IFramesList> KEEPER =
      new AbstractEntityKeeper<>( IFramesList.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aDw, IFramesList aEntity ) {
          StrioUtils.writeCollection( aDw, EMPTY_STRING, aEntity, FrameKeeper.KEEPER );
        }

        @Override
        protected IFramesList doRead( IStrioReader aDr ) {
          IList<IFrame> ll = StrioUtils.readCollection( aDr, EMPTY_STRING, FrameKeeper.KEEPER );
          return new FramesList( ll );
        }
      };

  // ------------------------------------------------------------------------------------
  // Конструкторы
  //

  /**
   * Создает пустой список.
   */
  public FramesList() {
    // nop
  }

  /**
   * Конструктор копирования из списка aSource и разрешением хранения дубликатов.
   *
   * @param aSource IList&lt;IFrame&gt; - список элементов - источник
   * @throws TsNullArgumentRtException aSource = null
   */
  public FramesList( IList<IFrame> aSource ) {
    super( aSource );
  }

  /**
   * Создает список с начальным содержимым набора или массива aElems.
   *
   * @param aElems {@link IFrame}... - элементы списка (набор или массив)
   * @throws TsNullArgumentRtException любой элемент = null
   */
  public FramesList( IFrame... aElems ) {
    super( aElems );
  }

  /**
   * Конструктор копирования из коллекции aSource и разрешением хранения дубликатов.
   *
   * @param aSource Collection&lt;{@link IFrame}&gt; - коллекция элементов
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException любой элемент = null
   */
  public FramesList( Collection<IFrame> aSource ) {
    super( aSource );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFramesList
  //

  @Override
  public IFrame findNearest( int aSec, String aPreferredCamId ) {
    TsNullArgumentRtException.checkNull( aPreferredCamId );
    if( isEmpty() ) {
      return null;
    }
    // данный алгоритм не имеет особого смысла, если в списке кадры от РАЗНЫХ эпизодов - работает, но бессмысленен
    // найдем ближайшие кадры (их может быть несколько, на одинаковом расстоянии: с разной стороны и разыне камеры)
    IListEdit<IFrame> nearestFrames = new ElemArrayList<>();
    int minDist = Integer.MAX_VALUE;
    for( IFrame f : this ) {
      if( f.isDefined() ) {
        int dist = Math.abs( aSec - f.secNo() );
        if( dist < minDist ) {
          nearestFrames.clear();
        }
        if( dist <= minDist ) {
          nearestFrames.add( f );
          minDist = dist;
        }
      }
    }
    // выберем первый с подходящей камерой
    for( IFrame f : nearestFrames ) {
      if( f.cameraId().equals( aPreferredCamId ) ) {
        return f;
      }
    }
    return nearestFrames.first();
  }

}
