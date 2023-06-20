package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.lib.core.quants.secint.valed.*;
import com.hazard157.prisex24.valeds.frames.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Контроллер панели редактирования сцены.
 *
 * @author hazard157
 */
public class SceneEditPanelController
    extends M5EntityPanelWithValedsController<IScene> {

  private final String episodeId;
  private final Secint allowedInterval;

  /**
   * Конструктор.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aAlloweedInterval {@link Secint} - интервал, за который не должен выходить интервал сцены
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException неверный формат идентификатора эпизода
   */
  public SceneEditPanelController( String aEpisodeId, Secint aAlloweedInterval ) {
    episodeId = EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    allowedInterval = TsNullArgumentRtException.checkNull( aAlloweedInterval );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void updateFrameSelectionCriteria() {
    ValedFrameEditor frameEditor = (ValedFrameEditor)editors().getByKey( SceneM5Model.FRAME.id() );
    frameEditor.setEpisodeId( episodeId );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5EntityPanelWithValedsController
  //

  @SuppressWarnings( { "unchecked" } )
  @Override
  public void afterSetValues( IM5Bunch<IScene> aValues ) {
    IValedControl<Secint> intervalEditor = (IValedControl<Secint>)editors().getByKey( FID_INTERVAL );
    ValedSecint.MIN_START.setValue( intervalEditor.params(), avInt( allowedInterval.start() ) );
    ValedSecint.MAX_END.setValue( intervalEditor.params(), avInt( allowedInterval.end() ) );
    updateFrameSelectionCriteria();
  }

  @Override
  public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<IScene, ?> aFieldDef,
      boolean aEditFinished ) {
    if( aFieldDef.id().equals( FID_INTERVAL ) ) {
      updateFrameSelectionCriteria();
    }
    return true;
  }

}
