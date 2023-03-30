package com.hazard157.psx.proj3.bricks.pfq;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * {@link IPfqProcessor} implementation.
 *
 * @author hazard157
 */
public final class PfqProcessor
    implements IPfqProcessor {

  private final ITsFilterFactoriesRegistry<SecondSlice> ffReg = new TsFilterFactoriesRegistry<>( SecondSlice.class );

  private final ISvinSeq      input;
  private final IUnitEpisodes unitEpisodes;

  /**
   * Constructor.
   *
   * @param aInput {@link IList}&lt;{@link Svin}&gt; - input list of SVINs
   * @param aUnitEpisodes {@link IUnitEpisodes} - episodes data source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PfqProcessor( IList<Svin> aInput, IUnitEpisodes aUnitEpisodes ) {
    TsNullArgumentRtException.checkNulls( aInput, aUnitEpisodes );
    input = new SvinSeq( aInput );
    unitEpisodes = aUnitEpisodes;
  }

  // ------------------------------------------------------------------------------------
  // IPfqProcessor
  //

  @Override
  public ISvinSeq input() {
    return input;
  }

  @Override
  public ISvinSeq queryData( ITsCombiFilterParams aFilterParams ) {
    // TODO Auto-generated method stub
    return null;
  }

}
