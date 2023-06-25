package com.hazard157.prisex24.m5.std;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.std.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.lib.core.excl_plan.secint.gui.*;
import com.hazard157.lib.core.excl_plan.secint.valed.*;
import com.hazard157.psx.common.stuff.*;

/**
 * Field {@link IEpisodeIntervalable#interval()}.
 *
 * @author hazard157
 * @param <T> - {@link IEpisodeIntervalable} implementing type
 */
public class PsxM5IntervalFieldDef<T extends IEpisodeIntervalable>
    extends M5SingleModownFieldDef<T, Secint> {

  /**
   * Constructor.
   */
  public PsxM5IntervalFieldDef() {
    super( FID_INTERVAL, SecintM5Model.MODEL_ID );
  }

  @Override
  protected void doInit() {
    setNameAndDescription( STR_FIELD_INTERVAL, STR_FIELD_INTERVAL_D );
    setFlags( M5FF_COLUMN );
    setDefaultValue( Secint.MAXIMUM );
    setValedEditor( ValedSecintFactory.FACTORY_NAME );
  }

  @Override
  protected Secint doGetFieldValue( T aEntity ) {
    return aEntity.interval();
  }

}
