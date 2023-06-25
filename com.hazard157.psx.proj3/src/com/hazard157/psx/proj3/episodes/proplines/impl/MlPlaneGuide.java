package com.hazard157.psx.proj3.episodes.proplines.impl;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

/**
 * Реализация {@link IMlPlaneGuide}.
 *
 * @author hazard157
 */
public class MlPlaneGuide
    extends AbstractMarkLine<PlaneGuide, MarkPlaneGuide>
    implements IMlPlaneGuide {

  /**
   * Конструктор.
   */
  public MlPlaneGuide() {
    super( PlaneGuide.FILLER, PlaneGuideKeeper.KEEPER );
  }

  @Override
  protected MarkPlaneGuide doCreateMark( Secint aIn, PlaneGuide aMarker ) {
    return new MarkPlaneGuide( aIn, aMarker );
  }

}
