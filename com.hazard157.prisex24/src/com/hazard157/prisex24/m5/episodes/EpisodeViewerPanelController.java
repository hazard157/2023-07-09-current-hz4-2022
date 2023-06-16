package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.prisex24.m5.std.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;

/**
 * Panel controller for {@link IM5EntityPanel} in viewer mode.
 *
 * @author hazard157
 */
public class EpisodeViewerPanelController
    extends M5EntityPanelWithValedsController<IEpisode> {

  private static final IList<IAtomicValue> EMPTY_IDS_LIST = new SingleItemList<>( AV_STR_NONE_ID );

  /**
   * Constructor.
   */
  public EpisodeViewerPanelController() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateDefaultTrailerSelectionCombo( String aEpisodeId ) {
    ValedComboSelector<IAtomicValue> trailerIdCombo = getEditor( FID_DEF_TRAILER_ID, ValedComboSelector.class );
    if( aEpisodeId == null || !EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( aEpisodeId ) ) {
      trailerIdCombo.setItems( EMPTY_IDS_LIST );
      return;
    }
    IEpisode e = tsContext().get( IUnitEpisodes.class ).items().findByKey( aEpisodeId );
    if( e == null ) {
      trailerIdCombo.setItems( EMPTY_IDS_LIST );
      return;
    }
    IStringList traIds = tsContext().get( IUnitTrailers.class ).listTrailersByEpisode( aEpisodeId ).ids();
    IListEdit<IAtomicValue> avTraIds = new ElemArrayList<>();
    for( String tid : traIds ) {
      avTraIds.add( avStr( TrailerUtils.extractLocalId( tid ) ) );
    }
    if( avTraIds.isEmpty() ) {
      avTraIds.add( AV_STR_NONE_ID );
    }
    trailerIdCombo.setItems( avTraIds );
  }

  // ------------------------------------------------------------------------------------
  // IM5EntityPanelWithValedsController
  //

  @Override
  public void beforeSetValues( IM5Bunch<IEpisode> aValues ) {
    String episodeId = aValues.getAs( PsxM5EpisodeIdFieldDef.FID_EPISODE_ID, String.class );
    updateDefaultTrailerSelectionCombo( episodeId );
  }

}
