package com.hazard157.psx24.core.glib.tagline;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Фильтра отбора ярлыков/групп по идентифкаторам.
 * <p>
 * В конструкторе задается идентификаторы ярлыков, которые принимает фильтр. В Фильтр отбирает те и только те листья
 * (ярлыки - не группы), которые перечислены в списоке дозволенных. И только те группы, у которых хотя бы одна ветка
 * имеет разрешенный лист.
 *
 * @author hazard157
 */
public class TagIdsFilter
    implements ITsFilter<String> {

  private final IStringList allTagIds;

  /**
   * Конструктор.
   *
   * @param aTagIds {@link IStringList} - идентификаторы ярлыков, включаемых в результат
   * @param aIncludeGroups boolean - признак включения родительских групп в фильтр
   */
  public TagIdsFilter( IStringList aTagIds, boolean aIncludeGroups ) {
    TsNullArgumentRtException.checkNull( aTagIds );
    IStringListBasicEdit tmpList = new SortedStringLinkedBundleList();
    for( String tagId : aTagIds ) {
      IStringList comps = StridUtils.getComponents( tagId );
      if( aIncludeGroups ) {
        for( int i = 0; i < comps.size(); i++ ) {
          String id = StridUtils.makeIdPath( comps, 0, i + 1 );
          if( !tmpList.hasElem( id ) ) {
            tmpList.add( id );
          }
        }
      }
      else {
        if( !tmpList.hasElem( tagId ) ) {
          tmpList.add( tagId );
        }
      }
    }
    allTagIds = tmpList;
  }

  @Override
  public boolean accept( String aObj ) {
    return allTagIds.hasElem( aObj );
  }

}
