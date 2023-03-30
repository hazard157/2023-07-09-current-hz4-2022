package com.hazard157.psx24.core;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;

import com.hazard157.lib.core.e4.services.mps.*;
import com.hazard157.psx.common.filesys.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx.proj3.songs.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx.proj3.todos.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;

/**
 * Mixin interface for PSX standard references access from context.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public interface IPsxStdReferences
    extends ITsGuiContextable {

  default IPsxFileSystem psxFileSystem() {
    return tsContext().get( IPsxFileSystem.class );
  }

  default IPsxFileSystem fileSys() {
    return tsContext().get( IPsxFileSystem.class );
  }

  default IPfsOriginalMedia fsOriginalMedia() {
    return tsContext().get( IPfsOriginalMedia.class );
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

  default IUnitTodos unitTodos() {
    return tsContext().get( IUnitTodos.class );
  }

  default IUnitTrailers unitTrailers() {
    return tsContext().get( IUnitTrailers.class );
  }

  default IUnitSongs unitSongs() {
    return tsContext().get( IUnitSongs.class );
  }

  default IUnitPleps unitPleps() {
    return tsContext().get( IUnitPleps.class );
  }

  default IUnitTags unitTags() {
    return tsContext().get( IUnitTags.class );
  }

  default IUnitGazes unitGazes() {
    return tsContext().get( IUnitGazes.class );
  }

  default IPsxSelectedSvinsService selectedSvinsService() {
    return tsContext().get( IPsxSelectedSvinsService.class );
  }

  default IAppPreferences appPreferences() {
    return tsContext().get( IAppPreferences.class );
  }

  default IPrefBundle prefBundle( String aBundleId ) {
    return appPreferences().getBundle( aBundleId );
  }

  default IMediaPlayerService mediaPlayerService() {
    return tsContext().get( IMediaPlayerService.class );
  }

}
