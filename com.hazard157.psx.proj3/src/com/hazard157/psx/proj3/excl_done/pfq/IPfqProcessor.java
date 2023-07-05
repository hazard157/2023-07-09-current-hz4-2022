package com.hazard157.psx.proj3.excl_done.pfq;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.impl.*;

/**
 * The Psx Fucking Query processor.
 * <p>
 * Processor is created with intput {@link #input()} specified in the constructor. After creation
 * {@link #queryData(ITsCombiFilterParams)} may be called many times with different argument.
 * <p>
 * Query is processed in {@link #queryData(ITsCombiFilterParams)} method. For second of each each {@link Svin} in
 * {@link #input()} episode {@link SecondSlice} from {@link Episode#slices()} is checked by the filter. All sequentially
 * accepted slices make a single {@link Svin} of {@link ISvinSeq#svins()}.
 *
 * @author hazard157
 */
public sealed interface IPfqProcessor permits PfqProcessor {

  /**
   * Returns the processor input.
   *
   * @return {@link ISvinSeq} - the input SVINs
   */
  ISvinSeq input();

  /**
   * Processes query and return new instance of the result.
   *
   * @param aFilterParams {@link ITsCombiFilterParams} - parameters of query filter
   * @return {@link ISvinSeq} - created instance of result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException unknown filter in given parameters
   */
  ISvinSeq queryData( ITsCombiFilterParams aFilterParams );

}
