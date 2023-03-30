package com.hazard157.psx.proj3.episodes.proplines.impl;

import static com.hazard157.psx.proj3.episodes.proplines.impl.IPsxResources.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Реализация {@link IMarkLine}.
 *
 * @author hazard157
 * @param <E> - конкретный тип маркера
 * @param <M> - конкретный наследник {@link Mark}
 */
public abstract class AbstractMarkLine<E, M extends Mark<E>>
    extends AbstractAnyPropLineBase
    implements IMarkLine<E, M> {

  private final E              fillerMarker;
  final IEntityKeeper<E>       markerKeeper;
  private final IIntMapEdit<E> markersMap = new SortedIntMap<>();

  /**
   * Просто конструктор.
   *
   * @param aFillerMarker &lt;E&gt; - маркер-заполнитель
   * @param aMarkerKeeper {@link IEntityKeeper}&lt;E&gt; - хранитель маркеров
   */
  public AbstractMarkLine( E aFillerMarker, IEntityKeeper<E> aMarkerKeeper ) {
    TsNullArgumentRtException.checkNulls( aFillerMarker, aMarkerKeeper );
    fillerMarker = aFillerMarker;
    markerKeeper = aMarkerKeeper;
    ensureIntergity();
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * После выполнения метода гарантирована целостность пометок.
   * <p>
   * В частности, в целостность входит:
   * <ul>
   * <li>наличие пометки на 0-вой секунде;</li>
   * <li>отсутствие двух подряд пометок с одинаковым маркером;</li>
   * <li>???</li>
   * </ul>
   */
  void ensureIntergity() {
    // обспечим пометку в 0-вой секунде
    if( markersMap.isEmpty() || !markersMap.hasKey( 0 ) ) {
      markersMap.put( 0, fillerMarker );
    }
    // создадим если нужно новуй карту без подряд пометок с одинаковы маркером
    IIntMapEdit<E> map = new IntMap<>();
    for( int i = 0, count = markersMap.size(); i < count; ) {
      // сначала кладем текущую пометку
      int currSec = markersMap.keys().getValue( i );
      E currMarker = markersMap.values().get( i );
      map.put( currSec, currMarker );
      // теперь пропускаем цикл до следующего маркера, не равного текущему
      E nextMarker;
      do {
        nextMarker = markersMap.values().get( i++ );
        if( i < count ) { // если все последующие маркеры равны текущему, выходим по исчерпанию пометок
          break;
        }
      } while( !nextMarker.equals( currMarker ) );
    }

  }

  private int getMarkStart( int aSec ) {
    int sec1 = 0;
    for( int i = 0, count = markersMap.size(); i < count; i++ ) {
      int currSec = markersMap.keys().getValue( i );
      if( currSec > aSec ) {
        break;
      }
      sec1 = currSec;
    }
    return sec1;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private IList<M> createMarksList( IIntMap<E> aMarksMap ) {
    IListBasicEdit ml = new SortedElemLinkedBundleList();
    for( int i = 0, count = aMarksMap.size(); i < count; i++ ) {
      E marker = aMarksMap.values().get( i );
      int sec1 = aMarksMap.keys().getValue( i );
      int sec2;
      if( i < count - 1 ) {
        sec2 = aMarksMap.keys().getValue( i + 1 ) - 1;
      }
      else {
        sec2 = MAX_MMSS_VALUE;
      }
      //
      if( sec2 >= duration() - 1 ) {
        sec2 = duration() - 1;
      }
      //
      Secint in = new Secint( sec1, sec2 );
      M mark = doCreateMark( in, marker );
      ml.add( mark );
      if( sec2 >= duration() - 1 ) {
        break;
      }
    }
    return ml;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aDw ) {
    StrioUtils.writeIntMap( aDw, EMPTY_STRING, markersMap, markerKeeper, true );
  }

  @Override
  public void read( IStrioReader aDr ) {
    IIntMap<E> m = StrioUtils.readIntMap( aDr, EMPTY_STRING, markerKeeper );
    markersMap.clear();
    markersMap.putAll( m );
    ensureIntergity();
    fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsClearableCollection
  //

  @Override
  public void clear() {
    if( !markersMap.isEmpty() ) {
      markersMap.clear();
      ensureIntergity();
      fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IMarkLine
  //

  @Override
  public IIntMap<E> markersMap() {
    return markersMap;
  }

  @Override
  public E fillerMarker() {
    return fillerMarker;
  }

  @Override
  public IList<M> marksList() {
    return createMarksList( markersMap );
  }

  @Override
  public M addMarkAt( int aStartSec, E aMark ) {
    checkSec( aStartSec );
    TsNullArgumentRtException.checkNull( aMark );
    E oldM = markersMap.findByKey( aStartSec );
    // если что-то изменилось, примерим изменения
    if( !Objects.equals( aMark, oldM ) ) {
      markersMap.put( aStartSec, aMark );
      ensureIntergity();
      fireChangeEvent();
    }
    return getMarkAt( aStartSec );
  }

  @Override
  public M addMarkIn( Secint aIn, E aMark ) {
    TsNullArgumentRtException.checkNulls( aIn, aMark );
    // найдем пометки, которые приходится на начало и конец интервала
    int inStartMarkStartSec = getMarkStart( aIn.start() );
    int inStartMarkIndex = markersMap.keys().indexOfValue( inStartMarkStartSec );
    int inEndMarkStartSec = getMarkStart( aIn.end() + 1 );
    int inEndMarkIndex = markersMap.keys().indexOfValue( inEndMarkStartSec );
    E afterEndMarker = markersMap.getByKey( inEndMarkStartSec );
    // теперь создаем временную карту
    IIntMapEdit<E> map = new IntMap<>();
    /**
     * Во временной карте без изменения переходят элементы из оригинальной карты с индексами до inStartMarkIndex
     * (включительно) и после inEndMarkIndex.
     * <p>
     * между ними же вставляется новая пометка, и обрезанный "хвост" от старой пометки
     */
    // переносим все пометки до inStartMarkIndex
    for( int i = 0; i < inStartMarkIndex; i++ ) {
      int sec = markersMap.keys().getValue( i );
      E marker = markersMap.values().get( i );
      map.put( sec, marker );
    }
    // добавим новую пометку, которая может переписать последнюю импортированную пометку
    map.put( aIn.start(), aMark );
    // добавим "ховст" обрезаемой старой помекти (но она может быть переписана ниже!)
    map.put( aIn.end() + 1, afterEndMarker );
    // переносим все пометки после inEndMarkIndex
    for( int i = inEndMarkIndex + 1, count = markersMap.size(); i < count; i++ ) {
      int sec = markersMap.keys().getValue( i );
      E marker = markersMap.values().get( i );
      map.put( sec, marker );
    }
    // если не было изменений, просто выходим
    if( !map.equals( markersMap ) ) {
      markersMap.putAll( map );
      // ensureIntergity(); - если нет ошибок в алгоритмах, этот вызов тут не нужен
      fireChangeEvent();
    }
    return getMarkAt( aIn.start() );
  }

  @Override
  public M removeByMarkerSec( int aMarkerSec ) {
    int index = markersMap.keys().indexOfValue( aMarkerSec );
    TsItemNotFoundRtException.checkTrue( index < 0, FMT_ERR_NO_MARKER_AT_SEC, Integer.valueOf( aMarkerSec ) );
    return removeByListIndex( index );
  }

  @Override
  public M removeByListIndex( int aIndex ) {
    int sec1 = markersMap.keys().getValue( aIndex );
    E marker = markersMap.values().get( aIndex );
    int sec2;
    if( aIndex < markersMap.size() - 1 ) {
      sec2 = markersMap.keys().getValue( aIndex + 1 ) - 1;
    }
    else {
      sec2 = MAX_MMSS_VALUE;
    }
    markersMap.removeByKey( sec1 );
    ensureIntergity();
    fireChangeEvent();
    return doCreateMark( new Secint( sec1, sec2 ), marker );
  }

  @Override
  public M getMarkAt( int aSec ) {
    checkSec( aSec );
    int sec1 = 0, sec2 = MAX_MMSS_VALUE;
    for( int i = 0, count = markersMap.size(); i < count; i++ ) {
      int currSec = markersMap.keys().getValue( i );
      if( currSec > aSec ) {
        sec2 = currSec;
        break;
      }
      sec1 = currSec;
    }
    return doCreateMark( new Secint( sec1, sec2 - 1 ), markersMap.getByKey( sec1 ) );
  }

  @Override
  public IList<M> getMarksIn( Secint aIn ) {
    IListEdit<M> list = new ElemLinkedBundleList<>();
    for( int i = 0, count = markersMap.size(); i < count; i++ ) {
      int sec1 = markersMap.keys().getValue( i );
      E marker = markersMap.values().get( i );
      int sec2;
      if( i < count - 1 ) {
        sec2 = markersMap.keys().getValue( i );
      }
      else {
        sec2 = MAX_MMSS_VALUE;
      }
      if( sec2 < aIn.start() ) { // не дошли до начала интервала, переходим к следующей пометке
        continue;
      }
      if( sec1 > aIn.end() ) { // пролетели интервали, можно закончить цикл
        break;
      }
      // тут однозначно интервал sec1-sec2 пересекается с aIn
      list.add( doCreateMark( new Secint( sec1, sec2 ), marker ) );
    }
    return list;
  }

  @Override
  public ISecintsList getMarkSecints( E aMark, Secint aIn ) {
    TsNullArgumentRtException.checkNulls( aMark, aIn );
    ISecintsListEdit ll = new SecintsList();
    for( int i = 0, count = markersMap.size(); i < count; i++ ) {
      E marker = markersMap.values().get( i );
      // пропустим не интересующий маркер
      if( !marker.equals( aMark ) ) {
        continue;
      }
      int sec1 = markersMap.keys().getValue( i );
      int sec2;
      if( i < count - 1 ) {
        sec2 = markersMap.keys().getValue( i );
      }
      else {
        sec2 = MAX_MMSS_VALUE;
      }
      ll.add( new Secint( sec1, sec2 ) );
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // Для переопределения
  //

  abstract protected M doCreateMark( Secint aIn, E aMarker );

}
