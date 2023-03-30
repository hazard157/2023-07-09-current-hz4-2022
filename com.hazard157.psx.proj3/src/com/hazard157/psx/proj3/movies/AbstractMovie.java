package com.hazard157.psx.proj3.movies;

import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Базовый класс всех сгенерированных видео.
 *
 * @author hazard157
 * @param <F> - конретный тип иформации, расширяет {@link MovieInfo}
 */
public class AbstractMovie<F extends MovieInfo>
    extends AbstractSinentity<F> {

  /**
   * Количество секунд перекрытия кусков в видео.
   */
  private static final int OVERLAP_SECS = 1;

  private final INotifierListEdit<Chunk> chunks = new NotifierListEditWrapper<>( new ElemLinkedBundleList<Chunk>() );

  protected AbstractMovie( String aId, F aInfo ) {
    super( aId, aInfo );
    chunks.addCollectionChangeListener( childCollChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISinentity
  //

  @Override
  public String nmName() {
    return info().name();
  }

  @Override
  public String description() {
    return info().description();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Подсчитывает и возвращает длительность фильма с учетом перекрытия кусков.
   *
   * @return int - длительность фильма
   */
  public int calcDuration() {
    int dur = 0;
    int count = chunks.size();
    // суммируем длительности кусков
    for( int i = 0; i < count; i++ ) {
      Chunk c = chunks.get( i );
      dur += c.interval().duration();
    }
    // вычитаем перекрытия
    if( count > 1 ) {
      dur -= (count - 1) * OVERLAP_SECS;
    }
    return dur;

  }

  /**
   * Возвращает индекс куска, на который приходится указанная секунда.
   *
   * @param aSecNo int - запрошенная секунда
   * @return int - индекс куска или -1
   */
  public int findChunkIndexAt( int aSecNo ) {
    if( aSecNo < 0 ) {
      return -1;
    }
    for( int i = 0, ss = 0; i < chunks.size(); i++ ) {
      Chunk c = chunks.get( i );
      int end = ss + c.interval().duration() - 1; // последняя секунда, ВКЛЮЧЕННАЯ в этот кусок
      if( aSecNo < end ) {
        return i;
      }
      ss = end - 1;
    }
    return -1;
  }

  /**
   * Возвращает кусок, на который приходится указанная секунда.
   *
   * @param aSecNo int - запрошенная секунда
   * @return {@link Chunk} - наденный кусок или <code>null</code>
   */
  public Chunk findChunkAt( int aSecNo ) {
    int index = findChunkIndexAt( aSecNo );
    if( index >= 0 ) {
      return chunks.get( index );
    }
    return null;
  }

  /**
   * Возвращает редактируемый список кусков эпизода, из которых состоит фильм.
   *
   * @return INotifierListEdit&lt;{@link Chunk}&gt; - редактируемый список кусков
   */
  public INotifierListEdit<Chunk> chunks() {
    return chunks;
  }

}
