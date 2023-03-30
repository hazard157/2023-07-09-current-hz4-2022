package com.hazard157.psx.proj3.bricks.beq2.impl;

import static com.hazard157.psx.proj3.bricks.beq2.impl.IPsxResources.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.bricks.beq2.*;
import com.hazard157.psx.proj3.bricks.beq2.filter.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * {@link IBeqProcessor}
 *
 * @author hazard157
 */
public class BeqProcessor
    implements IBeqProcessor {

  private final ISvinSeq inputData;

  /**
   * Набор посекундных срезов, соответствующий входным данным.
   * <p>
   * Набор представляь из собой сложную струтуру:
   * <ul>
   * <li>сначала это карта, ключи в которых - идентификатор эпизода, а значения посекундные срезы эпизода;</li>
   * <li>посекундные срезы эпизода организованы в виде списка, который содержит в себе карту срезов, соответствующие
   * {@link Svin}-ам входных данных. Список сортирован по времени, поскольку {@link IBeqResult} содержит сортированные
   * списки {@link Svin}-ов;</li>
   * <li>срез {@link Svin}-а это карта "секунда" - {@link SecondSlice}.</li>
   * </ul>
   */
  private final IStringMapEdit<IListEdit<IIntMapEdit<SecondSlice>>> secondSlices = new StringMap<>();

  private final ITsFilterFactoriesRegistry<SecondSlice> ffReg = new TsFilterFactoriesRegistry<>( SecondSlice.class );

  /**
   * Конструктор с инвариантами.
   *
   * @param aInputData {@link IList}&lt;{@link Svin}&gt; - input data
   * @param aEpMan {@link IUnitEpisodes} - episodes manager
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public BeqProcessor( IList<Svin> aInputData, IUnitEpisodes aEpMan ) {
    TsNullArgumentRtException.checkNull( aInputData );
    inputData = new SvinSeq( aInputData );
    for( EBeqSingleFilterKind k : EBeqSingleFilterKind.asList() ) {
      ffReg.register( k.factory() );
    }
    if( !inputData.svins().isEmpty() ) {
      for( String epId : inputData.svins().epinsMap().keys() ) {
        // проверка корректности (существования эпизода)
        IEpisode e = aEpMan.items().findByKey( epId );
        if( e == null ) {
          LoggerUtils.errorLogger().warning( FMT_WARN_UNKNOWN_EPISODE_IN_QUERY, epId );
          continue;
        }
        // создаем список карт
        IListEdit<IIntMapEdit<SecondSlice>> list = new ElemArrayList<>();
        secondSlices.put( epId, list );
        // формирует для каждоко эпизода посекундный срез состояний QpFilterInput
        for( Svin svin : inputData.epinsMap().getByKey( epId ) ) {
          IIntMapEdit<SecondSlice> ssMap = new IntMap<>( 157 );
          list.add( ssMap );
          for( int sec = svin.interval().start(); sec <= svin.interval().end(); sec++ ) {
            SecondSlice fin = e.slices().get( sec );
            ssMap.put( sec, fin );
          }
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IBeqProcessor
  //

  @Override
  public ISvinSeq input() {
    return inputData;
  }

  @Override
  public ISvinSeqEdit queryData( ITsCombiFilterParams aFilterParams ) {
    TsNullArgumentRtException.checkNull( aFilterParams );
    ISvinSeqEdit result = new SvinSeq();
    if( inputData.svins().isEmpty() ) {
      return result;
    }
    if( aFilterParams == ITsCombiFilterParams.NONE ) {
      return result;
    }
    if( aFilterParams == ITsCombiFilterParams.ALL ) {
      return inputData;
    }
    // IListBasicEdit<Svin> svins = new ElemLinkedBundleList<>();
    ITsFilter<SecondSlice> filter = TsCombiFilter.create( aFilterParams, ffReg );
    for( String epId : secondSlices.keys() ) {
      IListEdit<IIntMapEdit<SecondSlice>> list = secondSlices.getByKey( epId );
      for( IIntMap<SecondSlice> ssMap : list ) {
        int startSec = -1;
        int prevSec = -1;
        for( int i = 0; i < ssMap.size(); i++ ) {
          int sec = ssMap.keys().getValue( i );
          SecondSlice ss = ssMap.values().get( i );
          boolean accepted = filter.accept( ss );
          if( accepted ) {
            if( startSec == -1 ) {
              startSec = sec;
            }
            prevSec = sec;
          }
          else {
            if( startSec != -1 ) {
              result.svins().add( new Svin( epId, new Secint( startSec, prevSec ) ) );
              startSec = -1;
              prevSec = -1;
            }
          }
        }
        if( startSec >= 0 ) {
          result.svins().add( new Svin( epId, new Secint( startSec, prevSec ) ) );
        }
      }
    }
    return result;
  }

}
