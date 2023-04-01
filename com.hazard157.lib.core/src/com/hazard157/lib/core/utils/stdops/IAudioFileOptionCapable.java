package com.hazard157.lib.core.utils.stdops;

import static com.hazard157.lib.core.utils.stdops.IHzOptionsConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.av.utils.*;

/**
 * Entity with {@link IHzOptionsConstants#OPDEF_AUDIO_FILE} parameter in {@link #params()}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IAudioFileOptionCapable
    extends IParameterized {

  default File audioFile() {
    return OPDEF_AUDIO_FILE.getValue( params() ).asValobj();
  }

}
