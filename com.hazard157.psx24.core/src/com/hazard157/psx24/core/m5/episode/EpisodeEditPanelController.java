package com.hazard157.psx24.core.m5.episode;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.psx24.core.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.m5.std.*;
import com.hazard157.psx24.core.valeds.frames.*;

/**
 * Контроллер панели редактирования эпизода.
 *
 * @author goga
 */
public class EpisodeEditPanelController
    extends EpisodeViewerPanelController {

  /**
   * Конструктор.
   */
  public EpisodeEditPanelController() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void updateFrameSelectionCriteria() {
    String episodeId = lastValues().get( EpisodeM5Model.EPISODE_ID );
    FrameSelectionCriteria criteria = FrameSelectionCriteria.NONE;
    if( episodeId != null && EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( episodeId ) ) {
      Svin svin = new Svin( episodeId );
      criteria = new FrameSelectionCriteria( svin, EAnimationKind.BOTH, true );
    }
    ValedFrameEditor frameEditor = (ValedFrameEditor)editors().getByKey( EpisodeM5Model.FRAME.id() );
    frameEditor.setSelectionCriteria( criteria );
  }

  private void updateDefaultTrailerSelectionCombo() {
    String episodeId = lastValues().get( EpisodeM5Model.EPISODE_ID );
    IStringList traIds = tsContext().get( IUnitTrailers.class ).listTrailersByEpisode( episodeId ).ids();
    IListEdit<IAtomicValue> avTraIds = new ElemArrayList<>();
    for( String tid : traIds ) {
      avTraIds.add( avStr( TrailerUtils.extractLocalId( tid ) ) );
    }
    ValedComboSelector<IAtomicValue> trailerIdCombo = getEditor( FID_DEF_TRAILER_ID, ValedComboSelector.class );
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
