package com.hazard157.prisex24.e4.handlers;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;

import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.glib.dialogs.*;
import com.hazard157.psx.proj3.episodes.*;

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
