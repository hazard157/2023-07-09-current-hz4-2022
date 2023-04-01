package com.hazard157.psx.proj3.bricks.beq.filters;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Фильтр по идентификаторам ярлыков.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class BeqSingleFilterEpisodeIds
    implements ITsFilter<SecondSlice> {

  /**
   * Идентификатор типа фильтра {@link ISingleFilterFactory#id()},
   */
  public static final String TYPE_ID = "pq.filter.EpisodeIds"; //$NON-NLS-1$

  /**
   * Фабрика создания фильтра из значений параметров.
   */
  public static final ITsSingleFilterFactory<SecondSlice> FACTORY =
      new AbstractTsSingleFilterFactory<>( TYPE_ID, SecondSlice.class ) {

        @Override
        protected ITsFilter<SecondSlice> doCreateFilter( IOptionSet aParams ) {
          IStringList episodeIds = aParams.getValobj( OPID_EPISODE_IDS );
          return new BeqSingleFilterEpisodeIds( episodeIds );
        }
      };

  public static final String OPID_EPISODE_IDS = "episodeIds"; //$NON-NLS-1$

  private final IStringList episodeIds;

  /**
   * Конструктор.
   *
   * @param aEpisodeIds {@link IStringList} - идентификаторы эпизодов
   */
  public BeqSingleFilterEpisodeIds( IStringList aEpisodeIds ) {
    episodeIds = new StringArrayList( aEpisodeIds );
  }

  /**
   * Создает параметры фильтра.
   *
   * @param aEpisodeIds {@link IStringList} - идентификаторы ярлыков
   * @param aIsAny boolean - принак, чир хотя бы один ярлык должен совпадать, а не все ярлыки
   * @return {@link ITsSingleFilterParams} - параметры фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ITsSingleFilterParams makeFilterParams( IStringList aEpisodeIds ) {
    IOptionSetEdit p = new OptionSet();
    p.setValobj( OPID_EPISODE_IDS, aEpisodeIds );
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
    IStringList episodeIds = aParams.params().getValobj( BeqSingleFilterEpisodeIds.OPID_EPISODE_IDS );
    for( int i = 0, n = episodeIds.size(); i < n; i++ ) {
      sb.append( StridUtils.getLast( episodeIds.get( i ) ) );
      sb.append( ' ' );
    }
    return String.format( "Episodes: %s", sb.toString() );
  }

  @Override
  public boolean accept( SecondSlice aObj ) {
    return episodeIds.hasElem( aObj.episode().id() );
  }

}
