package com.hazard157.psx24.explorer.filters;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Фильтр по идентификаторам ярлыков.
 *
 * @author hazard157
 */
class PqFilterTagIds
    implements ITsFilter<SecondSlice> {

  /**
   * Идентификатор типа фильтра,
   */
  public static final String TYPE_ID = "pq.filter.TagIds"; //$NON-NLS-1$

  /**
   * Фабрика создания фильтра из значений параметров.
   */
  public static final ITsSingleFilterFactory<SecondSlice> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, SecondSlice.class ) {

        @Override
        protected ITsFilter<SecondSlice> doCreateFilter( IOptionSet aParams ) {
          IStringList tagIds = aParams.getValobj( OPID_TAG_IDS );
          boolean iaAny = aParams.getBool( OPID_IS_ANY );
          return new PqFilterTagIds( tagIds, iaAny );
        }
      };

  public static final String OPID_TAG_IDS = "tagIds"; //$NON-NLS-1$

  public static final String OPID_IS_ANY = "isAny"; //$NON-NLS-1$

  private final IStringList tagIds;
  private final boolean     any;

  /**
   * Конструктор.
   *
   * @param aTagIds {@link IStringList} - идентификаторы ярлыков
   * @param aIsAny boolean - принак, чир хотя бы один ярлык должен совпадать, а не все ярлыки
   */
  public PqFilterTagIds( IStringList aTagIds, boolean aIsAny ) {
    tagIds = new StringArrayList( aTagIds );
    any = aIsAny;
  }

  /**
   * Создает параметры фильтра.
   *
   * @param aTagIds {@link IStringList} - идентификаторы ярлыков
   * @param aIsAny boolean - принак, чир хотя бы один ярлык должен совпадать, а не все ярлыки
   * @return {@link ITsSingleFilterParams} - параметры фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsSingleFilterParams makeFilterParams( IStringList aTagIds, boolean aIsAny ) {
    IOptionSetEdit p = new OptionSet();
    p.setValobj( OPID_TAG_IDS, aTagIds );
    p.setBool( OPID_IS_ANY, aIsAny );
    return new TsSingleFilterParams( TYPE_ID, p );
  }

  /**
   * Возвращает удобочитаему строку параметров фильтра.
   *
   * @param aParams {@link ITsSingleFilterParams} - параметры этого фильтра
   * @return String - однострочный текст
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException параметры не от этого фильтра
   */
  @SuppressWarnings( "nls" )
  public static final String makeHumanReadableString( ITsSingleFilterParams aParams ) {
    TsNullArgumentRtException.checkNull( aParams );
    TsIllegalArgumentRtException.checkFalse( aParams.typeId().equals( TYPE_ID ) );
    StringBuilder sb = new StringBuilder();
    IStringList tagIds = aParams.params().getValobj( PqFilterTagIds.OPID_TAG_IDS );
    for( int i = 0, n = tagIds.size(); i < n; i++ ) {
      sb.append( StridUtils.getLast( tagIds.get( i ) ) );
      sb.append( ' ' );
    }
    if( tagIds.size() > 1 ) {
      boolean isAny = aParams.params().getBool( PqFilterTagIds.OPID_IS_ANY );
      String strIsAny = isAny ? "ANY" : "ALL";
      return String.format( "Tags: %s %s", strIsAny, sb.toString() );
    }
    return String.format( "Tags: %s", sb.toString() );
  }

  @Override
  public boolean accept( SecondSlice aObj ) {
    // любой ярлык
    if( any ) {
      return TsCollectionsUtils.intersects( aObj.tagIds(), tagIds );
    }
    // все ярлыки
    return TsCollectionsUtils.contains( aObj.tagIds(), tagIds );
  }

}
