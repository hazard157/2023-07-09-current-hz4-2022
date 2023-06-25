package com.hazard157.psx.proj3.episodes.proplines.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Реализация {@link INoteLine}.
 *
 * @author hazard157
 */
public class NoteLine
    extends AbstractAnyPropLineBase
    implements INoteLine {

  private final IMapEdit<Secint, String> marksMap = new SortedElemMap<>();

  /**
   * Конструктор.
   */
  public NoteLine() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса INoteLine
  //

  @Override
  public IMap<Secint, String> marksMap() {
    return marksMap;
  }

  @Override
  public IList<MarkNote> listMarks() {
    IListBasicEdit<MarkNote> list = new SortedElemLinkedBundleList<>();
    for( Secint in : marksMap.keys() ) {
      list.add( new MarkNote( in, marksMap.getByKey( in ) ) );
    }
    return list;
  }

  @Override
  public MarkNote putMark( Secint aIn, String aNote ) {
    marksMap.put( aIn, aNote );
    fireChangeEvent();
    return new MarkNote( aIn, aNote );
  }

  @Override
  public void removeMark( Secint aIn ) {
    if( marksMap.removeByKey( aIn ) != null ) {
      fireChangeEvent();
    }
  }

  @Override
  public IStringList getNotesAt( int aSec ) {
    TsIllegalArgumentRtException.checkTrue( aSec < 0 );
    if( marksMap.isEmpty() ) {
      return IStringList.EMPTY;
    }
    IStringListBasicEdit notes = new SortedStringLinkedBundleList();
    for( int i = 0, count = marksMap.size(); i < count; i++ ) {
      Secint in = marksMap.keys().get( i );
      if( in.contains( aSec ) ) {
        notes.add( marksMap.values().get( i ) );
      }
    }
    return notes;
  }

  @Override
  public IStringList getNotesAt( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    if( marksMap.isEmpty() ) {
      return IStringList.EMPTY;
    }
    IStringListBasicEdit notes = new SortedStringLinkedBundleList();
    for( int i = 0, count = marksMap.size(); i < count; i++ ) {
      Secint in = marksMap.keys().get( i );
      if( in.intersects( aIn ) ) {
        notes.add( marksMap.values().get( i ) );
      }
    }
    return notes;
  }

  @Override
  public void clear() {
    if( !marksMap.isEmpty() ) {
      fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aDw ) {
    StrioUtils.writeMap( aDw, EMPTY_STRING, marksMap, Secint.KEEPER, StringKeeper.KEEPER, true );
  }

  @Override
  public void read( IStrioReader aDr ) {
    marksMap.clear();
    StrioUtils.readMap( aDr, EMPTY_STRING, Secint.KEEPER, StringKeeper.KEEPER, marksMap );
    fireChangeEvent();
  }

}
