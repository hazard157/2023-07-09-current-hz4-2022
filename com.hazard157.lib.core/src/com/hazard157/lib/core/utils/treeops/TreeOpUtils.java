package com.hazard157.lib.core.utils.treeops;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательные методы работы с деревьями.
 *
 * @author goga
 */
public class TreeOpUtils {

  private static class NapSelectionPanel
      extends AbstractTsDialogPanel<ENodeAddPlace, IList<ENodeAddPlace>> {

    private IListEdit<Button> radioButtons = new ElemArrayList<>( ENodeAddPlace.values().length );

    protected NapSelectionPanel( Composite aParent, TsDialog<ENodeAddPlace, IList<ENodeAddPlace>> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new RowLayout( SWT.VERTICAL ) );
      for( ENodeAddPlace e : ENodeAddPlace.values() ) {
        Button b = new Button( this, SWT.RADIO );
        b.setText( StridUtils.printf( StridUtils.FORMAT_NAME_DESCRIPTION, e ) );
        if( environ().hasElem( e ) ) {
          b.setEnabled( false );
        }
        radioButtons.add( b );
      }
    }

    @Override
    protected void doSetDataRecord( ENodeAddPlace aData ) {
      for( int i = 0; i < radioButtons.size(); i++ ) {
        Button b = radioButtons.get( i );
        if( aData != null && aData.ordinal() == i ) {
          b.setSelection( true );
        }
        else {
          b.setSelection( false );
        }
      }
    }

    @Override
    protected ENodeAddPlace doGetDataRecord() {
      for( int i = 0; i < radioButtons.size(); i++ ) {
        Button b = radioButtons.get( i );
        if( b.getSelection() ) {
          return ENodeAddPlace.values()[i];
        }
      }
      return null;
    }

  }

  /**
   * Выводит диалог выбора одного из режимов {@link ENodeAddPlace}.
   *
   * @param aDefaultSelected {@link ENodeAddPlace} - выбранное изначально место или null
   * @param aCdi {@link ITsDialogInfo} - параметры диалогового окна
   * @param aDisallowdNaps {@link ENodeAddPlace}[] - массив запрещенных к выбору мест
   * @return {@link ENodeAddPlace} - выбранное метсто или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ENodeAddPlace askNap( ENodeAddPlace aDefaultSelected, ITsDialogInfo aCdi,
      ENodeAddPlace... aDisallowdNaps ) {
    TsErrorUtils.checkArrayArg( aDisallowdNaps );
    return askNap( aDefaultSelected, aCdi, new ElemArrayList<>( aDisallowdNaps ) );
  }

  /**
   * Выводит диалог выбора одного из режимов {@link ENodeAddPlace}.
   *
   * @param aDefaultSelected {@link ENodeAddPlace} - выбранное изначально место или null
   * @param aCdi {@link ITsDialogInfo} - параметры диалогового окна
   * @param aDisallowdNaps IList&lt;{@link ENodeAddPlace}&gt; - список запрещенных к выбору мест
   * @return {@link ENodeAddPlace} - выбранное метсто или null
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static ENodeAddPlace askNap( ENodeAddPlace aDefaultSelected, ITsDialogInfo aCdi,
      IList<ENodeAddPlace> aDisallowdNaps ) {
    IDialogPanelCreator<ENodeAddPlace, IList<ENodeAddPlace>> creator = NapSelectionPanel::new;
    TsDialog<ENodeAddPlace, IList<ENodeAddPlace>> d = new TsDialog<>( aCdi, aDefaultSelected, aDisallowdNaps, creator );
    return d.execData();
  }

  /**
   * Запрет на создание экземпляров.
   */
  private TreeOpUtils() {
    // nop
  }

}
