package com.hazard157.psx24.core.e4.handlers;

import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.e4.handlers.IPsxResources.*;

import java.io.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.psx24.core.e4.services.currep.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Команда "Запуск одного из проектов kdelive эпизода".
 *
 * @author hazard157
 */
public class CmdRunEpisodeKdenlive {

  @Execute
  void execute( Shell aShell, IEclipseContext aAppContext, IPsxFileSystem aFileSystem,
      ICurrentEpisodeService aCurrentEpisodeService ) {
    if( aCurrentEpisodeService.current() == null ) {
      TsDialogUtils.warn( aShell, MSG_WARN_NO_CURRENT_EPISODE );
      return;
    }
    IList<File> projs = aFileSystem.listEpisodeKdenliveProjects( aCurrentEpisodeService.current().id() );
    ITsDialogInfo cdi = TsDialogInfo.forSelectEntity( new TsGuiContext( aAppContext ) );
    File sel = DialogItemsList.select( cdi, projs, null, ITsNameProvider.DEFAULT );
    if( sel != null ) {
      TsMiscUtils.runProgram( PROGRAM_KDENLIVE, sel.getAbsolutePath() );
    }
  }

}
