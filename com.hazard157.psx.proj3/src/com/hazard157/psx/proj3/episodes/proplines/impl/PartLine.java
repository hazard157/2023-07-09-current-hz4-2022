package com.hazard157.psx.proj3.episodes.proplines.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Реализация {@link IPartLine}.
 *
 * @author hazard157
 * @param <T> - тип информации о части
 */
public class PartLine<T>
    extends AbstractAnyPropLineBase
    implements IPartLine<T> {

  private final String         keyword;
  private IEntityKeeper<T>     infoKeeper;
  private final IIntMapEdit<T> map = new SortedIntMap<>();

  /**
   * Конструктор.
   *
   * @param aKeyword String ключевое слово записи линии (ИД-путь)
   * @param aInfoKeeper {@link IEntityKeeper}&lt;T&gt; - хранитель информации о части
   */
  public PartLine( String aKeyword, IEntityKeeper<T> aInfoKeeper ) {
    keyword = StridUtils.checkValidIdPath( aKeyword );
    infoKeeper = TsNullArgumentRtException.checkNull( aInfoKeeper );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void checkIndex( int aIndex ) {
    if( aIndex < 0 || aIndex >= map.size() ) {
      throw new TsIllegalArgumentRtException();
    }
  }

  private void checkStart( int aStart ) {
    if( aStart < 0 || aStart > duration() - 1 ) {
      throw new TsIllegalArgumentRtException();
    }
  }

  private int partEnd( int aIndex ) {
    checkIndex( aIndex );
    if( aIndex == map.size() - 1 ) {
      return duration() - 1;
    }
    return map.keys().getValue( aIndex + 1 ) - 1;
  }

  // ------------------------------------------------------------------------------------
  // Реализация родительских методов
  //

  @Override
  public void write( IStrioWriter aDw ) {
    StrioUtils.writeIntMap( aDw, keyword, map, infoKeeper, true );
  }

  @Override
  public void read( IStrioReader aDr ) {
    IIntMap<T> newMap = StrioUtils.readIntMap( aDr, keyword, infoKeeper );
    map.putAll( newMap );
    fireChangeEvent();
  }

  @Override
  public void clear() {
    if( !map.isEmpty() ) {
      map.clear();
      fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPartLine
  //

  @Override
  public int count() {
    return map.size();
  }

  @Override
  public int partStart( int aIndex ) {
    return map.keys().getValue( aIndex );
  }

  @Override
  public int partDuration( int aIndex ) {
    return partEnd( aIndex ) - partStart( aIndex ) + 1;
  }

  @Override
  public Secint partInterval( int aIndex ) {
    return new Secint( partStart( aIndex ), partEnd( aIndex ) );
  }

  @Override
  public T partInfo( int aIndex ) {
    return map.values().get( aIndex );
  }

  @Override
  public void addPart( int aStart, T aInfo ) {
    checkStart( aStart );
    if( map.keys().hasValue( aStart ) ) {
      throw new TsIllegalArgumentRtException();
    }
    map.put( aStart, aInfo );
    fireChangeEvent();
  }

  @Override
  public void setPartStart( int aIndex, int aStart ) {
    checkIndex( aIndex );
    checkStart( aStart );
    int oldStart = map.keys().getValue( aIndex );
    if( oldStart == aStart ) {
      return;
    }
    T info = map.removeByKey( oldStart );
    map.put( aStart, info );
    fireChangeEvent();
  }

  @Override
  public void setPartDuration( int aIndex, int aDuration ) {
    checkIndex( aIndex );
    TsIllegalArgumentRtException.checkTrue( aDuration < 0 );
    // игнорируем попытку установки подлительности последней части
    if( aIndex == map.size() - 1 ) {
      return;
    }
    int oldDur = partDuration( aIndex );
    int delta = aDuration - oldDur;
    // если сдвигать ничего не надо, уходим
    if( delta == 0 ) {
      return;
    }
    // проверим, что сдвиг не приведет выходу за длительность линии
    int lastStart = partStart( map.size() - 1 );
    if( lastStart + delta >= duration() ) {
      throw new TsIllegalArgumentRtException();
    }
    for( int i = map.size() - 1; i > aIndex; i++ ) {
      int oldStart = map.keys().getValue( i );
      T info = map.removeByKey( oldStart );
      map.put( oldStart + delta, info );
    }
    fireChangeEvent();
  }

  @Override
  public void setPartInfo( int aIndex, T aInfo ) {
    int start = map.keys().getValue( aIndex );
    map.put( start, aInfo );
    fireChangeEvent();
  }

}
