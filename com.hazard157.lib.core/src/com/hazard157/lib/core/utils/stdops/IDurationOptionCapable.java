package com.hazard157.lib.core.utils.stdops;

import static com.hazard157.lib.core.utils.stdops.IHzOptionsConstants.*;

import org.toxsoft.core.tslib.av.utils.*;

/**
 * Entity with {@link IHzOptionsConstants#OPDEF_DURATION} parameter in {@link #params()}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IDurationOptionCapable
    extends IParameterized {

  default int duration() {
    return OPDEF_DURATION.getValue( params() ).asInt();
  }

}
