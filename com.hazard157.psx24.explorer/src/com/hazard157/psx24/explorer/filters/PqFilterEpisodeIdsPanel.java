package com.hazard157.psx24.explorer.filters;

import static com.hazard157.psx24.explorer.filters.IPsxResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.core.glib.dialogs.epsel.*;
import com.hazard157.psx24.explorer.gui.filters.*;

/**
 * Панель настройки фильтра {@link PqFilterAnyText}.
 *
 * @author goga
 */
public class PqFilterEpisodeIdsPanel
    extends AbstractFilterPanel {

  private final SelectionAdapter btnSelectEpisodeIdsListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent e ) {
      whenEpisodeIdsSelectionButtonPressed();
    }
  };

  private ValedAvStringText valedEpisodeIds;
  private Button            btnSelectEpisodeIds;

  private IStringListBasicEdit episodeIds = new SortedStringLinkedBundleList();

  /**
   * Конструктор панели, предназаначенной для использования вне диалога.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aData T - начальные данные для отображения, может быть null
   * @param aContext C - контекст радктирования / показа структры данных T
   * @throws TsNullArgumentRtException aParent или aContext = null
   */
  public PqFilterEpisodeIdsPanel( Composite aParent, ITsSingleFilterParams aData, ITsGuiContext aContext ) {
    super( aParent, aData, aContext, 0 );
    init();
  }

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aOwnerDialog {@link TsDialog} - родительский диалог
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PqFilterEpisodeIdsPanel( Composite aParent, TsDialog<ITsSingleFilterParams, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  private void init() {
    this.setLayout( new BorderLayout() );
    // label
    Label l = new Label( this, SWT.LEFT );
    l.setText( STR_N_EP_IDS );
    l.setToolTipText( STR_D_EP_IDS );
    l.setLayoutData( BorderLayout.WEST );
    // valedEpisodeIds
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_TOOLTIP_TEXT.setValue( ctx.params(), avStr( STR_D_EP_IDS ) );
    valedEpisodeIds = new ValedAvStringText( ctx );
    valedEpisodeIds.createControl( this );
    valedEpisodeIds.getControl().setLayoutData( BorderLayout.CENTER );
    valedEpisodeIds.eventer().addListener( notificationValedControlChangeListener );
    // btnSelectEpisodeIds
    btnSelectEpisodeIds = new Button( this, SWT.PUSH );
    btnSelectEpisodeIds.setText( TsLibUtils.EMPTY_STRING );
    btnSelectEpisodeIds.setText( STR_T_SEL_EP_IDS_BTN );
    btnSelectEpisodeIds.setToolTipText( STR_P_SEL_EP_IDS_BTN );
    btnSelectEpisodeIds.setLayoutData( BorderLayout.EAST );
    btnSelectEpisodeIds.addSelectionListener( btnSelectEpisodeIdsListener );
  }

  @Override
  protected void doSetDataRecord( ITsSingleFilterParams aData ) {
    if( aData == null || aData == ITsSingleFilterParams.NONE ) {
      resetFilter();
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aData.typeId().equals( PqFilterEpisodeIds.TYPE_ID ) );
    episodeIds.setAll( (IStringList)aData.params().getValobj( PqFilterEpisodeIds.OPID_EPISODE_IDS ) );
    valedEpisodeIds.setValue( avStr( makeEpisodeIdstext() ) );
  }

  private String makeEpisodeIdstext() {
    StringBuilder sb = new StringBuilder();
    if( episodeIds.size() <= 3 ) {
      for( int i = 0; i < episodeIds.size(); i++ ) {
        sb.append( episodeIds.get( i ) );
        if( i < episodeIds.size() - 1 ) {
          sb.append( ", " ); //$NON-NLS-1$
        }
      }
      return sb.toString();
    }
    for( int i = 0; i < 2; i++ ) {
      sb.append( episodeIds.get( i ) );
      if( i < 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
    }
    sb.append( " ... " ); //$NON-NLS-1$
    sb.append( episodeIds.last() );
    return sb.toString();
  }

  void whenEpisodeIdsSelectionButtonPressed() {
    IStringList sel = DialogSelectEpisodeIds.select( tsContext(), episodeIds );
    if( sel == null ) {
      return;
    }
    episodeIds.setAll( sel );
    valedEpisodeIds.setValue( avStr( makeEpisodeIdstext() ) );
    fireContentChangeEvent();
  }

  @Override
  protected ITsSingleFilterParams doGetDataRecord() {
    String text = valedEpisodeIds.getValue().asString().trim();
    if( text.isEmpty() ) {
      return ITsSingleFilterParams.NONE;
    }
    return PqFilterEpisodeIds.makeFilterParams( episodeIds );
  }

  @Override
  public void resetFilter() {
    valedEpisodeIds.setValue( AV_STR_EMPTY );
    episodeIds.clear();
  }

}
