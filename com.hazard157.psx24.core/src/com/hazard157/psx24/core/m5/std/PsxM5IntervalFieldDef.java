package com.hazard157.psx24.core.m5.std;

import static com.hazard157.psx24.core.m5.std.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.lib.core.excl_plan.secint.m5.*;
import com.hazard157.lib.core.excl_plan.secint.valed.*;
import com.hazard157.psx.common.stuff.*;

/**
 * Поле {@link IEpisodeIntervalable#interval()}.
 *
 * @author hazard157
 * @param <T> - конкретный тип, реализующмй {@link IEpisodeIntervalable}
 */
public class PsxM5IntervalFieldDef<T extends IEpisodeIntervalable>
    extends M5SingleModownFieldDef<T, Secint> {

  /**
   * Идентификатор поля {@link IEpisodeIntervalable#interval()}.
   */
  public static final String FID_INTERVAL = "Interval"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public PsxM5IntervalFieldDef() {
    super( FID_INTERVAL, SecintM5Model.MODEL_ID );
  }

  @Override
  protected void doInit() {
    setNameAndDescription( STR_N_FIELD_INTERVAL, STR_D_FIELD_INTERVAL );
    setFlags( M5FF_COLUMN );
    setDefaultValue( Secint.MAXIMUM );
    setValedEditor( ValedSecintFactory.FACTORY_NAME );
  }

  @Override
  protected Secint doGetFieldValue( T aEntity ) {
    return aEntity.interval();
  }

}
