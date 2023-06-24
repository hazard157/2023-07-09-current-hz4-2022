package com.hazard157.prisex24;

import org.toxsoft.core.tsgui.bricks.ctx.*;

import com.hazard157.common.e4.services.mps.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.e4.services.psx.*;
import com.hazard157.prisex24.pdus.snippets.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Extending {@link ITsGuiContextable} with PSX services.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxGuiContextable
    extends ITsGuiContextable {

  default IPsxCofs psxCofs() {
    return tsContext().get( IPsxCofs.class );
  }

  default ICofsFrames cofsFrames() {
    return tsContext().get( IPsxCofs.class ).cofsFrames();
  }

  default ICofsOutputMedia cofsOutputMedia() {
    return tsContext().get( IPsxCofs.class ).cofsOutputMedia();
  }

  default IPrisex24Service psxService() {
    return tsContext().get( IPrisex24Service.class );
  }

  default IUnitEpisodes unitEpisodes() {
    return tsContext().get( IUnitEpisodes.class );
  }

  default IUnitSourceVideos unitSourceVideos() {
    return tsContext().get( IUnitSourceVideos.class );
  }

  default IUnitSnippets unitSnippets() {
    return tsContext().get( IUnitSnippets.class );
  }

  default IMediaPlayerService mediaPlayer() {
    return tsContext().get( IMediaPlayerService.class );
  }

  default ICurrentEpisodeService currentEpisodeService() {
    return tsContext().get( ICurrentEpisodeService.class );
  }

}
