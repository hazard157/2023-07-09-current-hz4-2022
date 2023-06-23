package com.hazard157.prisex24.apptree;

import com.hazard157.prisex24.cofs.*;
import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx.proj3.songs.*;
import com.hazard157.psx.proj3.sourcevids.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * The application object used as {@link PqtRootNode#entity()}.
 * <p>
 * Declares entities used for child nodes so it becomes clear what subtrees are under PRISEX application tree.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPrisexApplication {

  IUnitCameras unitCameras();

  IUnitSourceVideos unitSourceVideos();

  IUnitEpisodes unitEpisodes();

  IUnitGazes unitGazes();

  IUnitTags unitTags();

  IUnitSongs unitSongs();

  ICofsOutputMedia cofsOutputMedia();

  // IUnitPleps unitPleps();
  // IUnitTodos unitTodos();
  // IUnitTrailers unitTrailers();

}
