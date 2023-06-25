package com.hazard157.psx.proj3.pleps;

import static com.hazard157.psx.proj3.pleps.IUnitPlepsConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.lib.core.excl_plan.visumple.*;

/**
 * Движение - элемент планируемого эпизода.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IStir
    extends IParameterizedEdit, IVisumplableEdit, IGenericChangeEventCapable {

  default String name() {
    return OP_STIR_NAME.getValue( params() ).asString();
  }

  default String description() {
    return OP_STIR_DESCRIPTION.getValue( params() ).asString();
  }

  default int duration() {
    return OP_STIR_DURATION.getValue( params() ).asInt();
  }

  // TODO NO_STRI_THUMB
  // default String thumbFilePath() {
  // return OP_STIR_THUMB_FILE_PATH.getValue( params() ).asString();
  // }

  // TODO add tag ids for color and action icon

  @Override
  INotifierOptionSetEdit params();

  /**
   * Возвращает родительский планируемый эпизод.
   *
   * @return {@link IPlep} - родительский планируемый эпизод
   */
  IPlep plep();

  /**
   * Calculates interval of this STIR in the PLEP.
   *
   * @return {@link Secint} - STIR interval
   */
  Secint getIntervalInPlep();

}
