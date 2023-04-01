package com.hazard157.psx24.core.glib.dialogs.frsel;

import static com.hazard157.psx24.core.glib.dialogs.frsel.IPsxResources.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Диалог выбора любого кадра любого эпизода.
 * <p>
 * Использует {@link PanelAnyFrameSelector} для выбора кадра.
 *
 * @author hazard157
 */
public class DialogAnyFrameSelector {

  /**
   * Выводит диалог выбора изображения кадра эпизодов.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aIniFrame {@link IFrame} - начально отображаемый кадр или {@link IFrame#NONE}
   * @return {@link IFrame} - выбранный кадр или <code>null</code>
   * @throws TsNullArgumentRtException aAppContext = null
   */
  public static IFrame select( ITsGuiContext aContext, IFrame aIniFrame ) {
    TsNullArgumentRtException.checkNulls( aContext, aIniFrame );
    Shell shell = aContext.get( Shell.class );
    // нельзя вызвать диалог, если в данных нет ни одного эпизода
    IUnitEpisodes ue = aContext.get( IUnitEpisodes.class );
    if( ue.items().isEmpty() ) {
      TsDialogUtils.error( null, MSG_NO_EPSIDEOS_DEFINED );
      return null;
    }
    // создатель панели
    IDialogPanelCreator<IFrame, ITsGuiContext> creator = ( aParent, aOwnerDialog ) -> {
      PanelAnyFrameSelector dp = new PanelAnyFrameSelector( aParent, aOwnerDialog );
      Rectangle sb = shell.getBounds();
      dp.setMinimumHeight( sb.height * 80 / 100 );
      dp.setMaximumHeight( sb.height );
      dp.setMinimumWidth( sb.width * 65 / 100 );
      return dp;
    };
    ITsDialogInfo cdi = new TsDialogInfo( aContext, STR_C_DWWF, STR_T_DWWF );
    TsDialog<IFrame, ITsGuiContext> d = new TsDialog<>( cdi, aIniFrame, aContext, creator );
    return d.execData();
  }

}
