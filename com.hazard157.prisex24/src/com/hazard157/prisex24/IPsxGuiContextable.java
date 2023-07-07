package com.hazard157.prisex24;

import org.toxsoft.core.tsgui.bricks.ctx.*;

import com.hazard157.common.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.services.psx.*;
import com.hazard157.prisex24.pdus.snippets.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Extending {@link ITsGuiContextable} with PSX services.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxGuiContextable
    extends IHzGuiContextable {

  default IPsxCofs psxCofs() {
    return tsContext().get( IPsxCofs.class );
  }

  default ICofsFrames cofsFrames() {
    return tsContext().get( IPsxCofs.class ).cofsFrames();
  }

  default ICofsFilms cofsFilms() {
    return tsContext().get( IPsxCofs.class ).cofsFilms();
  }

  default ICofsTrailers cofsTrailers() {
    return tsContext().get( IPsxCofs.class ).cofsTrailers();
  }

  default ICofsGazes cofsGazes() {
    return tsContext().get( IPsxCofs.class ).cofsGazes();
  }

  default ICofsMingles cofsMingles() {
    return tsContext().get( IPsxCofs.class ).cofsMingles();
  }

  default IPrisex24Service psxService() {
    return tsContext().get( IPrisex24Service.class );
  }

  default IUnitEpisodes unitEpisodes() {
    return tsContext().get( IUnitEpisodes.class );
  }

  default IUnitCameras unitCameras() {
    return tsContext().get( IUnitCameras.class );
  }

  default IUnitSourceVideos unitSourceVideos() {
    return tsContext().get( IUnitSourceVideos.class );
  }

  default IUnitSnippets unitSnippets() {
    return tsContext().get( IUnitSnippets.class );
  }

  default ICurrentEpisodeService currentEpisodeService() {
    return tsContext().get( ICurrentEpisodeService.class );
  }

}
