package com.hazard157.psx.proj3.episodes.proplines.impl;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Итератор по теглайну.
 * <p>
 * Позволяет пройтись по теглайну кусками, внутри который неизменный состав ярлыков.
 * <p>
 * Теглайн допускает повторное использование.
 *
 * @author hazard157
 */
public class TaglineIterator {

  private final ITagLine tagLine;
  private int            currSec = 0;

  /**
   * Создает привязанный к теглайну итератор.
   *
   * @param aTagLine {@link ITagLine} - теглайн
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TaglineIterator( ITagLine aTagLine ) {
    TsNullArgumentRtException.checkNull( aTagLine );
    tagLine = aTagLine;
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  int lastIntervalEnd() {
    if( tagLine.usedTagIds().isEmpty() ) {
      return tagLine.duration() - 1;
    }
    int lastEnd = -1;
    for( String tagId : tagLine.usedTagIds() ) {
      IIntMap<Secint> ins = tagLine.marks( tagId );
      int end = ins.values().get( ins.size() - 1 ).end();
      lastEnd = Math.max( lastEnd, end );
    }
    return lastEnd;
  }

  private int findLastSecWithSameMark( String aTagId, int aSec ) {
    int lastSecWithSameMark = HmsUtils.MAX_HHMMSS_VALUE;
    IIntMap<Secint> marks = tagLine.marks( aTagId );
    for( int i = 0; i < marks.size(); i++ ) {
      Secint in = marks.values().get( i );
      if( aSec <= in.end() ) { // дошли до интервала,
        if( aSec >= in.start() ) {
          lastSecWithSameMark = in.end();
          break;
        }
        lastSecWithSameMark = in.start() - 1;
        break;
      }
    }

    // TsTestUtils.pl( "lastSecWithSameMark = %s %s %s", HhMmSsUtils.mmss( aSec ), HhMmSsUtils.mmss( lastSecWithSameMark
    // ), aTagId );

    return lastSecWithSameMark;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, есть ли следующий элемент итерации.
   *
   * @return boolean - признак наличия следующего элемента (успешного выполнения {@link #next()}
   */
  public boolean hasNext() {
    if( currSec > lastIntervalEnd() ) { // за последней пометкой или интервал пустой
      return false;
    }
    return true;
  }

  /**
   * Возвращает следующий элемент итерации - интервал с неизменным набором пометки ярлыками.
   *
   * @return {@link Pair}&lt; {@link Secint}, {@link IStringList} &gt; - "интервал" - "ярлыки пометки".
   */
  public Pair<Secint, IStringList> next() {
    int end = lastIntervalEnd();
    TsIllegalStateRtException.checkTrue( currSec > end );
    int start = currSec;
    for( String tagId : tagLine.usedTagIds() ) {
      int lastSameSec = findLastSecWithSameMark( tagId, start );
      end = Math.min( end, lastSameSec );
    }
    IStringList tagIds = tagLine.tagIdsAt( start );
    Secint in = new Secint( start, end );
    currSec = end + 1;
    return new Pair<>( in, tagIds );
  }

  /**
   * Сбрасывает итератор в начальное состояние и начинает итерацию с начала.
   */
  public void reset() {
    currSec = 0;
  }

}
