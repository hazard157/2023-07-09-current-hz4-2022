package com.hazard157.psx.proj3.songs.impl;

import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx.proj3.songs.*;

/**
 * Реализация {@link IUnitSongs}.
 * 
 * @author hazard157
 */
public class UnitSongs
    extends StriparManager<ISong>
    implements IUnitSongs {

  /**
   * Конструктор.
   */
  public UnitSongs() {
    super( Song.CREATOR );
  }

}
