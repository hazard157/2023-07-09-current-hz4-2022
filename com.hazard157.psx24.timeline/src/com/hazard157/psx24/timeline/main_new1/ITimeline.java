package com.hazard157.psx24.timeline.main_new1;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public interface ITimeline {

  IStridablesList<IStripe> stripes();

  void redraw();

  void redrawStripe( String aStripeId );

  void redrawTitles();

  void redrawFlow();

  enum ETitleAreaType {
    NONE,
    FIXED,
    PROPORTIONAL,
    PACKED
  }

  IDataDef OPDEF_IS_TITLE_AREA_WIDTH_IN_PIXELS = null;

  IDataDef OPDEF_TITLE_AREA_WIDTH_PIXELS = null;

  IDataDef OPDEF_TITLE_AREA_WIDTH_PERCENTS = null;

  IDataDef OPDEF_BACKGROUND_COLOR = null;

}
