package com.hazard157.prisex24.e4.handlers;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.e4.handlers.IPsxResources.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.e4.services.currep.*;

/**
 * Command: select and run episode Kdenlive project.
 *
 * @author hazard157
 */
public class CmdRunEpisodeKdenlive {

  @Execute
  void execute( Shell aShell, IEclipseContext aAppContext, IPsxCofs aCofs,
      ICurrentEpisodeService aCurrentEpisodeService ) {
    if( aCurrentEpisodeService.current() == null ) {
      TsDialogUtils.warn( aShell, MSG_WARN_NO_CURRENT_EPISODE );
      return;
    }
    IList<File> projs = aCofs.listEpisodeKdenliveProjects( aCurrentEpisodeService.current().id() );
    ITsDialogInfo cdi = TsDialogInfo.forSelectEntity( new TsGuiContext( aAppContext ) );
    File sel = DialogItemsList.select( cdi, projs, null, ITsNameProvider.DEFAULT );
    if( sel != null ) {
      TsMiscUtils.runProgram( PROGRAM_KDENLIVE, sel.getAbsolutePath() );
    }
  }

}
