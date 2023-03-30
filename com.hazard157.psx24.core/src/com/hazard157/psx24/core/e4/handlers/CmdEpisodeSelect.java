package com.hazard157.psx24.core.e4.handlers;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.currep.*;
import com.hazard157.psx24.core.glib.dialogs.epsel.*;

/**
 * Select episode and set as current one.
 *
 * @author hazard157
 */
public class CmdEpisodeSelect {

  @Execute
  void exec( IEclipseContext aAppContext, IUnitEpisodes aUnitEpisodes, ICurrentEpisodeService aCes ) {
    ITsGuiContext ctx = new TsGuiContext( aAppContext );
    String currEpisodeId = aCes.current() != null ? aCes.current().id() : null;
    String epId = DialogSelectSingleEpisodeId.select( ctx, currEpisodeId );
    if( epId != null ) {
      IEpisode e = aUnitEpisodes.items().getByKey( epId );
      aCes.setCurrent( e );
    }
  }

}
