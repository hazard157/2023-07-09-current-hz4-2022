package com.hazard157.prisex24.m5.plep;

import static com.hazard157.prisex24.m5.plep.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.utils.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Summary panel for {@link StirMpc}.
 *
 * @author hazard157
 */
class StirSummaryPane
    extends MpcSummaryPaneMessage<IStir> {

  private static final IMessageProvider<IStir> summaryMessageProvider =
      ( aOwner, aSelectedNode, aSelEntity, aAllItems, aFilteredItems ) -> {
        int count = aAllItems.size();
        int duration = 0;
        for( IStir s : aAllItems ) {
          duration += s.duration();
        }
        return String.format( FMT_MSG_STIR_SUMMARY, HmsUtils.hhmmss( duration ), Integer.valueOf( count ) );
      };

  public StirSummaryPane( MultiPaneComponent<IStir> aOwner ) {
    super( aOwner, summaryMessageProvider );
  }

}
