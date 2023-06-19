package com.hazard157.prisex24.e4.handlers;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tslib.coll.helpers.*;

import com.hazard157.prisex24.e4.services.currep.*;

/**
 * Выбрать следующий эпизод.
 *
 * @author hazard157
 */
public class CmdEpisodeNext {

  @Execute
  void exec( ICurrentEpisodeService aCes ) {
    aCes.select( ETsCollMove.NEXT );
  }

}
