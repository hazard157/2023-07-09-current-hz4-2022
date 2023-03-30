package com.hazard157.psx24.core.m5.svin;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;

import com.hazard157.psx.common.stuff.svin.*;

class SvinM5EntityPanelWithValedsController
    extends M5EntityPanelWithValedsController<Svin> {

  SvinM5EntityPanelWithValedsController() {
    // nop
  }

  private void updateCamerasListOfEpisode() {

    // TODO SvinM5EntityPanelWithValedsController.updateCamerasListOfEpisode()
  }

  @Override
  public void afterEditorsCreated() {
    // IValedControl epEditor = editors().get( PsxM5EpisodeIdFieldDef.FID_EPISODE_ID );
    // IValedControl epCamerasList = editors().get( PsxM5CameraIdFieldDef.FID_CAMERA_ID );
  }

  @Override
  public void afterSetValues( IM5Bunch<Svin> aValues ) {
    updateCamerasListOfEpisode();
  }

  @Override
  public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<Svin, ?> aFieldDef,
      boolean aEditFinished ) {
    updateCamerasListOfEpisode();
    return true;
  }

}
