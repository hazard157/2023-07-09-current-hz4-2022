package com.hazard157.psx24.planning.m5;

import static com.hazard157.psx24.planning.m5.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.utils.*;

import com.hazard157.psx.proj3.pleps.*;

class TrackSummaryPane
    extends MpcSummaryPaneMessage<ITrack> {

  static IMessageProvider<ITrack> summaryMessageProvider =
      ( aOwner, aSelectedNode, aSelEntity, aAllItems, aFilteredItems ) -> {
        int count = aAllItems.size();
        int duration = 0;
        for( ITrack s : aAllItems ) {
          duration += s.duration();
        }
        return String.format( FMT_MSG_TRACK_SUMMARY, HmsUtils.hhmmss( duration ), Integer.valueOf( count ) );
      };

  public TrackSummaryPane( MultiPaneComponent<ITrack> aOwner ) {
    super( aOwner, summaryMessageProvider );
  }

}
