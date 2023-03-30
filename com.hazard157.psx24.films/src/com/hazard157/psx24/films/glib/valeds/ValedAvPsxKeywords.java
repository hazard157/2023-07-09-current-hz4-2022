package com.hazard157.psx24.films.glib.valeds;

import static com.hazard157.psx24.films.glib.valeds.IPsxResources.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.films.e4.services.*;

/**
 * Редактор списка ключевых слов пометки фильма.
 * <p>
 * Редактирует {@link IAtomicValue}, который содержит {@link EAtomicType#VALOBJ} значение с идентификатором кипера
 * {@link StringListKeeper#KEEPER_ID}.
 * <p>
 * Представляет соббой список ключевых слов с редактируемым ComboBox сверху. Можно выбрать один из
 * {@link IPsxFilmsService#listExistingKeywords()} в выпадающем списке или ввести новый и нажать "Добавить".
 *
 * @author goga
 */
public class ValedAvPsxKeywords
    extends AbstractValedControl<IAtomicValue, Control> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvPsxKeywords"; //$NON-NLS-1$

  /**
   * Фабрика редактора.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<IAtomicValue, Control> e = new ValedAvPsxKeywords( aContext );
      OPDEF_IS_WIDTH_FIXED.setValue( e.params(), AV_FALSE );
      OPDEF_IS_HEIGHT_FIXED.setValue( e.params(), AV_FALSE );
      OPDEF_VERTICAL_SPAN.setValue( e.params(), avInt( 8 ) );
      return e;
    }

    // TODO создать простой список для read-only контроля

  };

  private final SelectionListener buttonAddListener = new SelectionListenerAdapter() {

    @Override
    public void widgetSelected( SelectionEvent e ) {
      whenBtnAddPressed();
    }
  };

  private final SelectionListener buttonDelListener = new SelectionListenerAdapter() {

    @Override
    public void widgetSelected( SelectionEvent e ) {
      whenBtnDelPressed();
    }
  };

  private final SelectionListener comboSelectionListener = new SelectionListenerAdapter() {

    @Override
    public void widgetSelected( SelectionEvent e ) {
      // nop
    }

    @Override
    public void widgetDefaultSelected( SelectionEvent e ) {
      whenBtnAddPressed();
    }
  };

  private Combo  comboWidget;
  private List   listWidget;
  private Button btnAdd;
  private Button btnDel;

  /**
   * Конструктор для наследников.
   *
   * @param aTsContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedAvPsxKeywords( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void fillComboItems() {
    IPsxFilmsService pfs = tsContext().get( IPsxFilmsService.class );
    comboWidget.removeAll();
    for( String kw : pfs.listExistingKeywords() ) {
      comboWidget.add( kw );
    }
  }

  void whenBtnAddPressed() {
    String kw = comboWidget.getText().trim();
    if( kw.isEmpty() ) {
      return;
    }
    for( int i = 0, count = listWidget.getItemCount(); i < count; i++ ) {
      String s = listWidget.getItem( i );
      if( s.equals( kw ) ) {
        return;
      }
    }
    comboWidget.setText( TsLibUtils.EMPTY_STRING );
    listWidget.add( kw );
    fireModifyEvent( true );
    fillComboItems();
  }

  void whenBtnDelPressed() {
    int selIndex = listWidget.getSelectionIndex();
    if( selIndex < 0 ) {
      return;
    }
    listWidget.remove( selIndex );
    fireModifyEvent( true );
    fillComboItems();
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractValedControl
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    // north
    TsComposite northBoard = new TsComposite( board );
    northBoard.setLayout( new GridLayout( 3, false ) );
    northBoard.setLayoutData( BorderLayout.NORTH );
    comboWidget = new Combo( northBoard, SWT.BORDER | SWT.DROP_DOWN );
    comboWidget.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    btnAdd = new Button( northBoard, SWT.PUSH );
    btnAdd.setImage( iconManager.loadStdIcon( ICONID_LIST_ADD, EIconSize.IS_16X16 ) );
    btnAdd.setToolTipText( STR_D_BTN_ADD );
    btnAdd.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false ) );
    btnDel = new Button( northBoard, SWT.PUSH );
    btnDel.setImage( iconManager.loadStdIcon( ICONID_LIST_REMOVE, EIconSize.IS_16X16 ) );
    btnDel.setToolTipText( STR_D_BTN_DEL );
    btnDel.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false ) );
    // listWidget
    listWidget = new List( board, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION );
    listWidget.setLayoutData( BorderLayout.CENTER );
    // setup
    btnAdd.addSelectionListener( buttonAddListener );
    btnDel.addSelectionListener( buttonDelListener );
    comboWidget.addSelectionListener( comboSelectionListener );
    fillComboItems();
    return board;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( getControl() == null ) {
      return;
    }
    comboWidget.setEnabled( aEditable );
    btnAdd.setEnabled( aEditable );
    btnDel.setEnabled( aEditable );
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    IStringListEdit ll = new StringArrayList();
    for( int i = 0, count = listWidget.getItemCount(); i < count; i++ ) {
      String kw = listWidget.getItem( i );
      ll.add( kw );
    }
    return avValobj( ll );
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    IStringList ll = IStringList.EMPTY;
    if( aValue != null && aValue != IAtomicValue.NULL ) {
      ll = aValue.asValobj();
    }
    listWidget.removeAll();
    for( String kw : ll ) {
      listWidget.add( kw );
    }
  }

  @Override
  protected void doClearValue() {
    listWidget.removeAll();
  }

}
