package com.hazard157.psx24.core.m5.episode;

import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * Контроллер панели редактирования эпизода.
 *
 * @author goga
 */
public class EpisodeViewerPanelController
    extends M5EntityPanelWithValedsController<IEpisode> {

  /**
   * Конструктор.
   */
  public EpisodeViewerPanelController() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void updateDefaultTrailerSelectionCombo( String aEpisodeId ) {
    ValedComboSelector<IAtomicValue> trailerIdCombo = getEditor( FID_DEF_TRAILER_ID, ValedComboSelector.class );
    if( aEpisodeId == null || !EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( aEpisodeId ) ) {
      trailerIdCombo.setItems( IList.EMPTY );
      return;
    }
    IEpisode e = tsContext().get( IUnitEpisodes.class ).items().findByKey( aEpisodeId );
    if( e == null ) {
      trailerIdCombo.setItems( IList.EMPTY );
      return;
    }
    IStringList traIds = tsContext().get( IUnitTrailers.class ).listTrailersByEpisode( aEpisodeId ).ids();
    IListEdit<IAtomicValue> avTraIds = new ElemArrayList<>();
    for( String tid : traIds ) {
      avTraIds.add( avStr( TrailerUtils.extractLocalId( tid ) ) );
    }
    trailerIdCombo.setItems( avTraIds );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5EntityPanelWithValedsController
  //

  @Override
  public void beforeSetValues( IM5Bunch<IEpisode> aValues ) {
    String episodeId = aValues.getAs( PsxM5EpisodeIdFieldDef.FID_EPISODE_ID, String.class );
    updateDefaultTrailerSelectionCombo( episodeId );
  }

}
