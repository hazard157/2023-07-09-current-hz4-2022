package com.hazard157.psx24.core.e4.handlers;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tslib.coll.helpers.*;

import com.hazard157.psx24.core.e4.services.currep.*;

/**
 * Выбрать следующий эпизод.
 *
 * @author goga
 */
public class CmdEpisodeNext {

  @Execute
  void exec( ICurrentEpisodeService aCes ) {
    aCes.select( ETsCollMove.NEXT );
  }

}
