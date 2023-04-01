package com.hazard157.psx24.core.m5.svin;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Менедже ЖЦ сущнстей {@link Svin}.
 *
 * @author hazard157
 */
class SvinLifecycleManager
    extends M5LifecycleManager<Svin, Object> {

  SvinLifecycleManager( IM5Model<Svin> aModel ) {
    super( aModel, true, true, true, true, null );
  }

  @Override
  protected Svin doCreate( IM5Bunch<Svin> aValues ) {
    String episodeId = SvinM5Model.EPISODE_ID.getFieldValue( aValues );
    String camId = SvinM5Model.CAM_ID.getFieldValue( aValues );
    Secint in = SvinM5Model.INTERVAL.getFieldValue( aValues );
    return new Svin( episodeId, camId, in );
  }

  @Override
  protected Svin doEdit( IM5Bunch<Svin> aValues ) {
    return doCreate( aValues );
  }

  @Override
  protected void doRemove( Svin aEntity ) {
    // nop
  }

}
