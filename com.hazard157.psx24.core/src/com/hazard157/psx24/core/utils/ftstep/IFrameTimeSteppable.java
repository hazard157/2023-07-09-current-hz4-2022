package com.hazard157.psx24.core.utils.ftstep;

import com.hazard157.psx.common.utils.ftstep.*;

/**
 * Mixin interace for mostly for visual elements with changeable frame time step.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IFrameTimeSteppable {

  ESecondsStep getFrameTimeStep();

  void setFrameTimeStep( ESecondsStep aStep );

  ESecondsStep defaultFrameTimeStep();

}
