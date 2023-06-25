package com.hazard157.psx.proj3.episodes.proplines.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tsgui.utils.checkstate.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Реализация ITagli
 *
 * @author hazard157
 */
public class TagLine
    extends AbstractAnyPropLineBase
    implements ITagLine {

  private final IStringListBasicEdit                usedTagIds = new SortedStringLinkedBundleList();
  private final IStringMapEdit<IIntMapEdit<Secint>> marksMap   = new StringMap<>();

  /**
   * Пустой конструктор.
   */
  public TagLine() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void sortMarksMap() {
    IStringMapEdit<IIntMapEdit<Secint>> map = new StringMap<>();
    IStringListBasicEdit ids = new SortedStringLinkedBundleList( marksMap.keys() );
    for( String s : ids ) {
      map.put( s, marksMap.getByKey( s ) );
    }
    marksMap.setAll( map );
  }

  private void fillMarks( String aTagId, ISecintsListEdit aList ) {
    IIntMap<Secint> inMap = marksMap.findByKey( aTagId );
    if( inMap != null ) {
      aList.add( inMap.values() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITagline
  //

  @Override
  public IStringList usedTagIds() {
    return usedTagIds;
  }

  @Override
  public IIntMap<Secint> marks( String aTagId ) {
    IIntMap<Secint> map = marksMap.findByKey( aTagId );
    if( map != null ) {
      return map;
    }
    return IIntMap.EMPTY;
  }

  @Override
  public ISecintsList calcMarks( String aTagId ) {
    TsNullArgumentRtException.checkNull( aTagId );
    ISecintsListEdit list = new SecintsList();
    fillMarks( aTagId, list );
    return list;
  }

  @Override
  public ISecintsList calcMarks( IStringList aTagIds ) {
    TsNullArgumentRtException.checkNull( aTagIds );
    ISecintsListEdit list = new SecintsList();
    for( String tagId : aTagIds ) {
      fillMarks( tagId, list );
    }
    return list;
  }

  @Override
  public IStringList tagIdsAt( int aSec ) {
    TsIllegalArgumentRtException.checkTrue( aSec < 0 );
    IStringListEdit tagIds = new StringArrayList( usedTagIds.size() );
    for( String tId : usedTagIds ) {
      IIntMap<Secint> marks = marksMap.getByKey( tId );
      for( int i = 0, n = marks.size(); i < n; i++ ) {
        Secint in = marks.values().get( i );
        if( in.isAfter( aSec ) ) {
          continue;
        }
        if( in.contains( aSec ) ) {
          tagIds.add( tId );
        }
      }
    }
    return tagIds;
  }

  @Override
  public IStringList tagIdsIn( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    IStringListEdit tagIds = new StringArrayList( usedTagIds.size() );
    for( String tId : usedTagIds ) {
      IIntMap<Secint> marks = marksMap.getByKey( tId );
      for( int i = 0, n = marks.size(); i < n; i++ ) {
        Secint in = marks.values().get( i );
        if( in.end() < aIn.start() ) {
          continue;
        }
        if( in.start() > aIn.end() ) {
          break;
        }
        if( in.intersects( aIn ) ) {
          tagIds.add( tId );
        }
      }
    }
    return tagIds;
  }

  @Override
  public IStringMap<ECheckState> tagMarksIn( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    IStringMapEdit<ECheckState> tagMarks = new StringMap<>();
    for( String tId : usedTagIds ) {
      IIntMap<Secint> marks = marksMap.getByKey( tId );
      for( int i = 0, n = marks.size(); i < n; i++ ) {
        Secint in = marks.values().get( i );
        if( in.end() < aIn.start() ) {
          continue;
        }
        if( in.start() > aIn.end() ) {
          break;
        }
        if( in.intersects( aIn ) ) {
          if( in.contains( aIn ) ) {
            tagMarks.put( tId, ECheckState.CHECKED );
          }
          else {
            tagMarks.put( tId, ECheckState.GRAYED );
          }
        }
      }
    }
    return tagMarks;
  }

  @Override
  public Secint findMark( String aTagId, int aSec ) {
    TsIllegalArgumentRtException.checkTrue( aSec < 0 );
    IIntMap<Secint> mm = marksMap.findByKey( aTagId );
    if( mm == null ) {
      return null;
    }
    for( Secint in : mm.values() ) {
      if( in.contains( aSec ) ) {
        return in;
      }
      if( aSec < in.start() ) {
        break;
      }
    }
    return null;
  }

  @Override
  public void addMark( String aTagId, Secint aSecint ) {
    StridUtils.checkValidIdPath( aTagId );
    TsNullArgumentRtException.checkNull( aSecint );
    IIntMapEdit<Secint> marks = marksMap.findByKey( aTagId );
    if( marks == null ) {
      marks = new SortedIntMap<>();
      marksMap.put( aTagId, marks );
      marks.put( aSecint.start(), aSecint );
      usedTagIds.add( aTagId );
      sortMarksMap();
      fireChangeEvent();
      return;
    }
    // составим промежуточный список (чтобы оставить ссылку на карту без изменений)
    IListEdit<Secint> list = new ElemLinkedBundleList<>();
    int start = aSecint.start(), end = aSecint.end();
    for( Secint in : marks.values() ) {
      if( aSecint.intersects( in ) || aSecint.touches( in ) ) {
        start = Math.min( start, in.start() );
        end = Math.max( end, in.end() );
      }
      else { // переносим только неперсекающейся интервалы
        list.add( in );
      }
    }
    // добавим один интевал вместо всех пересекующихся
    list.add( new Secint( start, end ) );
    // теперь перенесем в карту (где интервали отсортируются)
    marks.clear();
    for( Secint in : list ) {
      marks.put( in.start(), in );
    }
    sortMarksMap();
    fireChangeEvent();
  }

  @Override
  public boolean removeMark( String aTagId, int aStart ) {
    IIntMapEdit<Secint> marks = marksMap.findByKey( aTagId );
    if( marks == null ) {
      return false;
    }
    if( !marks.hasKey( aStart ) ) {
      return false;
    }
    marks.removeByKey( aStart );
    if( marks.isEmpty() ) {
      marksMap.removeByKey( aTagId );
      usedTagIds.remove( aTagId );
    }
    fireChangeEvent();
    return true;
  }

  @Override
  public boolean removeMark( String aTagId, Secint aSecint ) {
    TsNullArgumentRtException.checkNull( aSecint );
    IIntMapEdit<Secint> marks = marksMap.findByKey( aTagId );
    if( marks == null ) {
      return false;
    }
    IIntMapEdit<Secint> newMarks = new SortedIntMap<>();
    boolean wasChanges = false;
    for( Secint in : marks.values() ) {
      // нет пересечение - интервал остается а пометках
      if( !in.intersects( aSecint ) ) {
        newMarks.put( in.start(), in );
        continue;
      }
      wasChanges = true;
      // новый содержит старый - игнорируем интервал
      if( aSecint.contains( in ) ) {
        continue;
      }
      // в этом месте есть пересечение нового и старого, удалим пересекающуюся часть из пометок
      if( in.start() < aSecint.start() ) {
        Secint ni = new Secint( in.start(), aSecint.start() - 1 );
        newMarks.put( ni.start(), ni );
      }
      if( in.end() > aSecint.end() ) {
        Secint ni = new Secint( aSecint.end() + 1, in.end() );
        newMarks.put( ni.start(), ni );
      }
    }
    if( wasChanges ) {
      if( newMarks.isEmpty() ) {
        marksMap.removeByKey( aTagId );
        usedTagIds.remove( aTagId );
      }
      else {
        marks.setAll( newMarks );
      }
      fireChangeEvent();
    }
    return wasChanges;
  }

  @Override
  public boolean removeAllMarks( String aTagId ) {
    if( marksMap.removeByKey( aTagId ) != null ) {
      usedTagIds.remove( aTagId );
      fireChangeEvent();
      return true;
    }
    return false;
  }

  @Override
  public void clearTag( String aTagId ) {
    if( marksMap.removeByKey( aTagId ) != null ) {
      usedTagIds.remove( aTagId );
      fireChangeEvent();
    }
  }

  @Override
  public void clear() {
    if( !marksMap.isEmpty() ) {
      marksMap.clear();
      usedTagIds.clear();
      fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepableEntity
  //

  @Override
  public void write( IStrioWriter aSw ) {
    aSw.writeChars( CHAR_SET_BEGIN );
    if( marksMap.isEmpty() ) {
      aSw.writeChar( CHAR_SET_END );
      return;
    }
    sortMarksMap();
    aSw.incNewLine();
    for( int i = 0, n = marksMap.size(); i < n; i++ ) {
      String tagId = marksMap.keys().get( i );
      IIntMap<Secint> marks = marksMap.values().get( i );
      aSw.writeAsIs( tagId );
      aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE );
      Secint.KEEPER.writeColl( aSw, marks.values(), false );
      if( i < n - 1 ) {
        aSw.writeChar( CHAR_ITEM_SEPARATOR );
        aSw.writeEol();
      }
    }
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  public void read( IStrioReader aSr ) {
    usedTagIds.clear();
    marksMap.clear();
    if( aSr.readSetBegin() ) {
      do {
        String tagId = aSr.readIdPath();
        aSr.ensureChar( CHAR_EQUAL );
        IList<Secint> ins = Secint.KEEPER.readColl( aSr );
        IIntMapEdit<Secint> marks = new SortedIntMap<>();
        for( Secint in : ins ) {
          marks.put( in.start(), in );
        }
        marksMap.put( tagId, marks );
        usedTagIds.add( tagId );
      } while( aSr.readSetNext() );
    }
    fireChangeEvent();
  }

}
