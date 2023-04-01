package com.hazard157.psx24.core.glib.dialogs.imgs;

import static com.hazard157.psx24.core.glib.dialogs.imgs.IPsxResources.*;
import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.glib.frlstviewer_ep.*;

/**
 * Диалог просмотра и работы с изображениями кадров эпизодов.
 *
 * @author hazard157
 */
public class DialogWorkWithFrames
    extends AbstractTsDialogPanel<IFrame, IEclipseContext> {

  static final String SETTINGS_NODE_ID = "DialogWorkWithFrames.FramesListViewer"; //$NON-NLS-1$

  final IEpisodeFramesListViewer framesViewer;

  DialogWorkWithFrames( Composite aParent, TsDialog<IFrame, IEclipseContext> aOwnerDialog,
      FrameSelectionCriteria aCriteria ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );

    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    EpisodeFramesListViewer.FRAME_VIEWER_THUMB_SIZE.setValue( ctx.params(), avValobj( EThumbSize.SZ512 ) );
    framesViewer = new EpisodeFramesListViewer( ctx );
    framesViewer.createControl( aParent ).setLayoutData( BorderLayout.CENTER );
    framesViewer.setCriteria( aCriteria );
  }

  @Override
  protected void doSetDataRecord( IFrame aData ) {
    framesViewer.setSelectedItem( aData );
  }

  @Override
  protected IFrame doGetDataRecord() {
    return framesViewer.selectedItem();
  }

  // ------------------------------------------------------------------------------------
  // API вызова диалога
  //

  /**
   * Выводит диалог работы с изображениями кадров указанного эпизода.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIniFrame {@link IFrame} - начально отображаемый кадр или {@link IFrame#NONE}
   * @param aEpisode {@link IEpisodeFramesListViewer} - эпизод
   * @throws TsNullArgumentRtException aAppContext = null
   */
  public static void openEpisode( ITsGuiContext aContext, IFrame aIniFrame, IEpisode aEpisode ) {
    TsNullArgumentRtException.checkNulls( aContext, aIniFrame, aEpisode );
    Svin svin = Svin.removeCamId( aEpisode.svin() );
    FrameSelectionCriteria criteria = new FrameSelectionCriteria( svin, EAnimationKind.BOTH, true );
    internalOpen( aContext, aIniFrame, criteria );
  }

  /**
   * Выводит диалог работы с изображениями кадров эпизодов.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIniFrame {@link IFrame} - начально отображаемый кадр или {@link IFrame#NONE}
   * @param aCriteria {@link FrameSelectionCriteria} - критерий отбора показываемых кадроы
   * @throws TsNullArgumentRtException aAppContext = null
   */
  private static void internalOpen( ITsGuiContext aContext, IFrame aIniFrame, FrameSelectionCriteria aCriteria ) {
    TsNullArgumentRtException.checkNulls( aContext, aIniFrame, aCriteria );
    Shell shell = aContext.get( Shell.class );
    // нельзя вызвать диалог, если в данных нет ни одного эпизода
    IUnitEpisodes ue = aContext.get( IUnitEpisodes.class );
    if( ue.items().isEmpty() ) {
      TsDialogUtils.error( null, MSG_NO_EPSIDEOS_DEFINED );
      return;
    }

    IDialogPanelCreator<IFrame, IEclipseContext> creator =
        ( aParent, aOwnerDialog ) -> new DialogWorkWithFrames( aParent, aOwnerDialog, aCriteria );
    TsDialogInfo cdi = new TsDialogInfo( aContext, shell, STR_C_DWWF, STR_T_DWWF, DF_NO_APPROVE | DF_NONMODAL );
    cdi.setMinSizeShellRelative( 65, 80 );
    TsDialog<IFrame, IEclipseContext> d = new TsDialog<>( cdi, aIniFrame, aContext.eclipseContext(), creator );
    d.execDialog();
  }

  /**
   * Выводит диалог выбора изображения кадра эпизодов.
   * <p>
   * Это такой же диалог, как и {@link #internalOpen(ITsGuiContext, IFrame, FrameSelectionCriteria)}, но модальный и
   * возвращает текущий выбранный кадр.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aIniFrame {@link IFrame} - начально отображаемый кадр или {@link IFrame#NONE}
   * @param aCriteria {@link FrameSelectionCriteria} - критерий отбора показываемых кадроы
   * @return {@link IFrame} - выбранный кадр или <code>null</code>
   * @throws TsNullArgumentRtException aAppContext = null
   */
  public static IFrame select( ITsGuiContext aContext, IFrame aIniFrame, FrameSelectionCriteria aCriteria ) {
    TsNullArgumentRtException.checkNulls( aContext, aIniFrame, aCriteria );
    Shell shell = aContext.get( Shell.class );
    // нельзя вызвать диалог, если в данных нет ни одного эпизода
    IUnitEpisodes ue = aContext.get( IUnitEpisodes.class );
    if( ue.items().isEmpty() ) {
      TsDialogUtils.error( null, MSG_NO_EPSIDEOS_DEFINED );
      return null;
    }

    IDialogPanelCreator<IFrame, IEclipseContext> creator = ( aParent, aOwnerDialog ) -> {
      DialogWorkWithFrames dc = new DialogWorkWithFrames( aParent, aOwnerDialog, aCriteria );
      Rectangle sb = shell.getBounds();
      dc.setMinimumHeight( sb.height * 80 / 100 );
      dc.setMaximumHeight( sb.height );
      dc.setMinimumWidth( sb.width * 65 / 100 );
      return dc;
    };
    ITsDialogInfo cdi = new TsDialogInfo( aContext, STR_C_DWWF, STR_T_DWWF );
    TsDialog<IFrame, IEclipseContext> d = new TsDialog<>( cdi, aIniFrame, aContext.eclipseContext(), creator );
    return d.execData();
  }

}
