package com.hazard157.lib.core.quants.secint;

import java.util.Iterator;

import org.toxsoft.core.tslib.bricks.events.change.GenericChangeEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventer;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.coll.impl.SortedElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Сортированный список интервалов, поддерживающий добавление с объединением.
 * <p>
 * Список гарантирует, что ни один интервал не пересекается и не соприкасается с другим. То есть, при итерации по списку
 * между соседними интервалами всегда есть промежуток не менее 1 секунды.
 *
 * @author hazard157
 */
public class SecintsList
    implements ISecintsListEdit {

  private final GenericChangeEventer   eventer;
  private final IListBasicEdit<Secint> items = new SortedElemLinkedBundleList<>();

  /**
   * Создает пустой список.
   */
  public SecintsList() {
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Создает список с указанным содержимым.
   * <p>
   * При пересечении элементов в исходном списке, производит объединение.
   *
   * @param aItems {@link ITsCollection}&lt;{@link Secint}&gt; - список интервалов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public SecintsList( ITsCollection<Secint> aItems ) {
    this();
    for( Secint in : aItems ) {
      add( in );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void fireChangedEvent() {
    eventer.fireChangeEvent();
  }

  private boolean internalAdd( Secint aIn ) {
    Secint newIn = aIn;
    IListBasicEdit<Secint> list = new SortedElemLinkedBundleList<>();
    for( int i = 0; i < items.size(); i++ ) {
      Secint in = items.get( i );
      if( in.contains( aIn ) ) {
        return false; // новыйй интервал уже содержится в списке
      }
      if( newIn.contains( in ) ) {
        continue;
      }
      if( !in.intersects( newIn ) && !in.touches( newIn ) ) {
        list.add( in );
        continue;
      }
      newIn = Secint.union( in, newIn );
    }
    list.add( newIn );
    items.setAll( list );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsCollection
  //

  @Override
  public boolean hasElem( Secint aElem ) {
    return items.hasElem( aElem );
  }

  @Override
  public Iterator<Secint> iterator() {
    return items.iterator();
  }

  @Override
  public boolean isEmpty() {
    return items.isEmpty();
  }

  @Override
  public int size() {
    return items.size();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsLinearReferenceCollection
  //

  @Override
  public int indexOf( Secint aElem ) {
    return items.indexOf( aElem );
  }

  @Override
  public Secint get( int aIndex ) {
    return items.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IList
  //

  @Override
  public Secint[] toArray( Secint[] aSrcArray ) {
    return items.toArray( aSrcArray );
  }

  @Override
  public Object[] toArray() {
    return items.toArray();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISecintsList
  //

  @Override
  public IList<Secint> items() {
    return items;
  }

  @Override
  public IList<Secint> listGaps( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    IListEdit<Secint> list = new ElemLinkedBundleList<>();
    // вообще есть пересечение?
    boolean isIntersection = false;
    for( Secint in : items ) {
      if( in.start() > aIn.end() ) {
        break; // этот и остальные элементы за пределами искомого интервала
      }
      if( in.intersects( aIn ) ) {
        if( in.contains( aIn ) ) { // полностью в элементе этого списка, вернем пустой обратный список
          return list;
        }
        isIntersection = true;
        break;
      }
    }
    if( isIntersection ) {
      // пройдем по списку, и для каждого интервала, найдем пустой участок ПОСЛЕ него
      boolean isFirst = true; // первое пересечение рассмотрим отдельно - также надо посмотреть интервал ДО него
      for( int i = 0, n = items.size(); i < n; i++ ) {
        Secint in = items.get( i );
        if( in.end() < aIn.start() ) {
          continue; // этот элемент до начала искомого интервала
        }
        if( in.start() > aIn.end() ) {
          break; // этот и остальные элементы за пределами искомого интервала
        }
        // для первого пересечения найдем интервал между ним ипредыдущим
        if( isFirst && in.start() > 0 ) {
          int prevEnd = -1;
          if( i > 0 ) {
            prevEnd = items.get( i - 1 ).end();
          }
          Secint prev = new Secint( prevEnd + 1, in.start() - 1 );
          Secint newIn = Secint.intersection( aIn, prev );
          if( newIn != null ) {
            list.add( newIn );
          }
          isFirst = true;
        }
        // найдем пустой интервал ЗА текущим
        if( in.end() < aIn.end() ) {
          int nextStart = aIn.end();
          if( i < n - 1 ) {
            nextStart = items.get( i + 1 ).start() - 1;
          }
          Secint next = new Secint( in.end() + 1, nextStart );
          Secint newIn = Secint.intersection( aIn, next );
          if( newIn != null ) {
            list.add( newIn );
          }
        }
      }
    }
    else {
      list.add( aIn );
    }
    return list;
  }

  @Override
  public IGenericChangeEventer eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISecintsListEdit
  //

  @Override
  public boolean add( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    boolean retval = internalAdd( aIn );
    if( retval ) {
      fireChangedEvent();
    }
    return retval;
  }

  @Override
  public boolean remove( Secint aIn ) {
    boolean retval = false;
    TsNullArgumentRtException.checkNull( aIn );
    IListBasicEdit<Secint> list = new SortedElemLinkedBundleList<>();
    for( int i = 0; i < items.size(); i++ ) {
      Secint in = items.get( i );
      if( !aIn.intersects( in ) ) { // интервалы не пересекаются - этот интервал останется в списке
        list.add( in );
        continue;
      }
      retval = true;
      if( aIn.contains( in ) ) { // старый интервал полностью удаляется
        continue;
      }
      // интервалы пересекаются: либо удаляется серединка, либо начало, либо конец интервала in
      if( aIn.start() > in.start() && aIn.end() < in.end() ) { // удалим середину интервала in
        Secint in1 = new Secint( in.start(), aIn.start() - 1 );
        list.add( in1 );
        Secint in2 = new Secint( aIn.end() + 1, in.end() );
        list.add( in2 );
        continue;
      }
      if( aIn.start() <= in.start() ) { // удалим начало интервала in
        Secint in2 = new Secint( aIn.end() + 1, in.end() );
        list.add( in2 );
        continue;
      }
      if( aIn.end() >= in.end() ) { // удалим начало интервала in
        Secint in1 = new Secint( in.start(), aIn.start() - 1 );
        list.add( in1 );
        continue;
      }
    }
    items.setAll( list );
    if( retval ) {
      fireChangedEvent();
    }
    return retval;
  }

  @Override
  public boolean add( ITsCollection<Secint> aList ) {
    TsNullArgumentRtException.checkNull( aList );
    boolean retval = false;
    for( Secint in : aList ) {
      if( internalAdd( in ) ) {
        retval = true;
      }
    }
    if( retval ) {
      fireChangedEvent();
    }
    return retval;
  }

  @Override
  public void clear() {
    boolean wasEmpty = items.isEmpty();
    items.clear();
    if( !wasEmpty ) {
      fireChangedEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for( int i = 0, count = size(); i < count; i++ ) {
      sb.append( items.get( i ) );
      if( i < count - 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
    }
    return sb.toString();
  }

}
