package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.common.quants.secint.ISecintConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.prisex24.m5.std.*;
import com.hazard157.prisex24.valeds.frames.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;

/**
 * Panel controller for {@link IM5EntityPanel} in edit mode.
 *
 * @author hazard157
 */
public class EpisodeEditPanelController
    extends EpisodeViewerPanelController {

  /**
   * Constructor.
   */
  public EpisodeEditPanelController() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateFrameSelectionCriteria() {
    String episodeId = lastValues().get( EpisodeM5Model.EPISODE_ID );
    ValedFrameEditor frameEditor = (ValedFrameEditor)editors().getByKey( EpisodeM5Model.FRAME.id() );
    frameEditor.setEpisodeId( episodeId );
  }

  private void updateDefaultTrailerSelectionCombo() {
    String episodeId = lastValues().get( EpisodeM5Model.EPISODE_ID );
    IStringList traIds = tsContext().get( IUnitTrailers.class ).listTrailersByEpisode( episodeId ).ids();
    IListEdit<IAtomicValue> avTraIds = new ElemArrayList<>();
    for( String tid : traIds ) {
      avTraIds.add( avStr( TrailerUtils.extractLocalId( tid ) ) );
    }
    ValedComboSelector<IAtomicValue> trailerIdCombo = getEditor( FID_DEF_TRAILER_ID, ValedComboSelector.class );
    if( avTraIds.isEmpty() ) {
      avTraIds.add( AV_STR_NONE_ID );
    }
    trailerIdCombo.setItems( avTraIds );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5EntityPanelWithValedsController
  //

  @Override
  public void afterSetValues( IM5Bunch<IEpisode> aValues ) {
    updateFrameSelectionCriteria();
    updateDefaultTrailerSelectionCombo();
  }

  @Override
  public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<IEpisode, ?> aFieldDef,
      boolean aEditFinished ) {
    switch( aFieldDef.id() ) {
      case PsxM5EpisodeIdFieldDef.FID_EPISODE_ID:
        updateDefaultTrailerSelectionCombo();
        updateFrameSelectionCriteria();
        break;
      case FID_DURATION:
        updateFrameSelectionCriteria();
        break;
      default:
        break;
    }
    return true;
  }

}
