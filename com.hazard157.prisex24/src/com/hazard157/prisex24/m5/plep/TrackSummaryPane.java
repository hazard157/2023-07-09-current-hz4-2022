package com.hazard157.prisex24.m5.plep;

import static com.hazard157.prisex24.m5.plep.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.utils.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Summary pane for {@link TrackMpc}.
 *
 * @author hazard157
 */
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
