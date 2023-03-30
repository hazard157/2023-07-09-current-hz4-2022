package com.hazard157.psx.proj3.songs.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx.proj3.songs.*;

/**
 * Реализация {@link ISong}.
 *
 * @author hazard157
 */
public class Song
    extends StridableParameterized
    implements ISong {

  static final IStriparCreator<ISong> CREATOR = ( aId, aParams ) -> new Song( aId, aParams );

  Song( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

}
