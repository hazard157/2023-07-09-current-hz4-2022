package com.hazard157.psx24.explorer.filters;

import static com.hazard157.psx24.explorer.filters.IPsxResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;

import com.hazard157.psx24.explorer.gui.filters.*;

/**
 * Панель настройки фильтра {@link PqFilterAnyText}.
 *
 * @author goga
 */
public class PqFilterAnyTextPanel
    extends AbstractFilterPanel {

  private ValedAvStringText              valedText;
  private ValedEnumCombo<ETextMatchMode> valedMatchMode;
  private ValedAvBooleanCheck            valedCheckTags;
  private ValedAvBooleanCheck            valedCheckScenes;
  private ValedAvBooleanCheck            valedCheckNotes;
  private ValedAvBooleanCheck            valedCheckPlanes;

  /**
   * Конструктор панели, предназаначенной для использования вне диалога.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aData T - начальные данные для отображения, может быть null
   * @param aContext C - контекст радктирования / показа структры данных T
   * @throws TsNullArgumentRtException aParent или aContext = null
   */
  public PqFilterAnyTextPanel( Composite aParent, ITsSingleFilterParams aData, ITsGuiContext aContext ) {
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
  public PqFilterAnyTextPanel( Composite aParent, TsDialog<ITsSingleFilterParams, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void init() {
    this.setLayout( new GridLayout( 2, false ) );
    Label l;
    ITsGuiContext ctx;
    // text
    l = new Label( this, SWT.LEFT );
    l.setText( STR_N_TEXT );
    l.setToolTipText( STR_D_TEXT );
    ctx = new TsGuiContext( tsContext() );
    OPDEF_TOOLTIP_TEXT.setValue( ctx.params(), avStr( STR_D_TEXT ) );
    valedText = new ValedAvStringText( ctx );
    valedText.createControl( this );
    valedText.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    valedText.eventer().addListener( notificationValedControlChangeListener );
    // matchMode
    l = new Label( this, SWT.LEFT );
    l.setText( STR_N_MATCH_MODE );
    l.setToolTipText( STR_D_MATCH_MODE );
    ctx = new TsGuiContext( tsContext() );
    OPDEF_TOOLTIP_TEXT.setValue( ctx.params(), avStr( STR_D_MATCH_MODE ) );
    ctx.put( IValedEnumConstants.REFID_ENUM_CLASS, ETextMatchMode.class );
    valedMatchMode = (ValedEnumCombo)(IValedControl)ValedEnumCombo.FACTORY.createEditor( ctx );
    valedMatchMode.createControl( this );
    valedMatchMode.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    valedText.eventer().addListener( notificationValedControlChangeListener );
    valedMatchMode.setValue( ETextMatchMode.CONTAINS );

    l = new Label( this, SWT.LEFT );
    l.setText( STR_N_SEARCH_IN );

    // checkboxex board
    TsComposite chbxBoard = new TsComposite( this );
    chbxBoard.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    RowLayout rl = new RowLayout( SWT.HORIZONTAL );
    rl.fill = true;
    chbxBoard.setLayout( rl );
    // inTags inScenes inNotes inPlanes
    valedCheckTags = addCheckBoxToBoard( chbxBoard, STR_N_CHECK_TAGS, STR_D_CHECK_TAGS );
    valedCheckScenes = addCheckBoxToBoard( chbxBoard, STR_N_CHECK_SCENES, STR_D_CHECK_SCENES );
    valedCheckNotes = addCheckBoxToBoard( chbxBoard, STR_N_CHECK_NOTES, STR_D_CHECK_NOTES );
    valedCheckPlanes = addCheckBoxToBoard( chbxBoard, STR_N_CHECK_PLANES, STR_D_CHECK_PLANES );
  }

  @SuppressWarnings( "rawtypes" )
  private ValedAvBooleanCheck addCheckBoxToBoard( Composite aBoard, String aText, String aTooltip ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_TOOLTIP_TEXT.setValue( ctx.params(), avStr( aTooltip ) );
    ValedBooleanCheck.OPDEF_TEXT.setValue( ctx.params(), avStr( aText ) );
    ValedAvBooleanCheck valed = (ValedAvBooleanCheck)(IValedControl)ValedAvBooleanCheck.FACTORY.createEditor( ctx );
    valed.createControl( aBoard );
    valed.eventer().addListener( notificationValedControlChangeListener );
    return valed;
  }

  @Override
  protected void doSetDataRecord( ITsSingleFilterParams aData ) {
    if( aData == null || aData == ITsSingleFilterParams.NONE ) {
      resetFilter();
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aData.typeId().equals( PqFilterAnyText.TYPE_ID ) );
    valedText.setValue( aData.params().getValue( PqFilterAnyText.OPID_TEXT ) );
    valedMatchMode.setValue( aData.params().getValobj( PqFilterAnyText.OPID_MATCH_MODE ) );
    valedCheckTags.setValue( aData.params().getValue( PqFilterAnyText.OPID_IN_TAGS ) );
    valedCheckScenes.setValue( aData.params().getValue( PqFilterAnyText.OPID_IN_SCENES ) );
    valedCheckNotes.setValue( aData.params().getValue( PqFilterAnyText.OPID_IN_NOTES ) );
    valedCheckPlanes.setValue( aData.params().getValue( PqFilterAnyText.OPID_IN_PLANES ) );
  }

  @Override
  protected ITsSingleFilterParams doGetDataRecord() {
    String text = valedText.getValue().asString().trim();
    if( text.isEmpty() ) {
      return ITsSingleFilterParams.NONE;
    }
    ETextMatchMode matchMode = valedMatchMode.getValue();
    boolean inTags = valedCheckTags.getValue().asBool();
    boolean inScenes = valedCheckScenes.getValue().asBool();
    boolean inNotes = valedCheckNotes.getValue().asBool();
    boolean inPlanes = valedCheckPlanes.getValue().asBool();
    return PqFilterAnyText.makeFilterParams( text, matchMode, inTags, inScenes, inNotes, inPlanes );
  }

  @Override
  public void resetFilter() {
    valedText.setValue( AV_STR_EMPTY );
    valedMatchMode.setValue( ETextMatchMode.CONTAINS );
    valedCheckTags.setValue( AV_FALSE );
    valedCheckScenes.setValue( AV_FALSE );
    valedCheckNotes.setValue( AV_FALSE );
    valedCheckPlanes.setValue( AV_FALSE );
  }

}
