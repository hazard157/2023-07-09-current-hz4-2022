package com.hazard157.lib.core.utils.stdops;

import static com.hazard157.lib.core.utils.stdops.IHzOptionsConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.utils.*;

/**
 * Entity with {@link IHzOptionsConstants#OPDEF_IMAGE_FILE} parameter in {@link #params()}.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public interface IImageFileOptionCapable
    extends IParameterized {

  default File imageFile() {
    return OPDEF_IMAGE_FILE.getValue( params() ).asValobj();
  }

}
