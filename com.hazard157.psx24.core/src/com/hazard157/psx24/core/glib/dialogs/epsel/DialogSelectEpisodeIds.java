package com.hazard157.psx24.core.glib.dialogs.epsel;

import static com.hazard157.psx24.core.glib.dialogs.epsel.IPsxResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Диалог выбора списка идентификаторов эпизодов.
 * <p>
 * Выводит список всех эпизодов с возможностью пометить (check)отдельные эпизоды. Возвращает список идентификаторов
 * помеченных эпизодов.
 *
 * @author hazard157
 */
public class DialogSelectEpisodeIds {

  private static class Panel
      extends AbstractTsDialogPanel<IStringList, ITsGuiContext> {

    static final IEpisode[] EMPTY_EPISODES_ARRAY = {};

    private final IContentProvider contentProvider = new StructuredContentProviderAdapter<IEpisode>() {

      @Override
      public IEpisode[] getElements( Object aInputElement ) {
        return tsContext().get( IUnitEpisodes.class ).items().toArray( EMPTY_EPISODES_ARRAY );
      }

    };

    private final ITableLabelProvider labelProvider = new TableLabelProviderAdapter() {

      @Override
      public String getColumnText( Object aElement, int aColumnIndex ) {
        return getText( aElement );
      }

      @Override
      public String getText( Object aElement ) {
        IEpisode e = (IEpisode)aElement;
        return StridUtils.printf( StridUtils.FORMAT_ID_NAME, e );
      }

    };

    private final ICheckStateProvider checkStateProvider = new ICheckStateProvider() {

      @Override
      public boolean isGrayed( Object aElement ) {
        return false;
      }

      @Override
      public boolean isChecked( Object aElement ) {
        return checkedEpisodeIds.hasElem( ((IEpisode)aElement).id() );
      }
    };

    private final ICheckStateListener checkStateListener = new ICheckStateListener() {

      @Override
      public void checkStateChanged( CheckStateChangedEvent aEvent ) {
        if( !(aEvent.getElement() instanceof IEpisode) ) {
          return;
        }
        IEpisode e = (IEpisode)aEvent.getElement();
        if( aEvent.getChecked() ) {
          if( !checkedEpisodeIds.hasElem( e.id() ) ) {
            checkedEpisodeIds.add( e.id() );
          }
        }
        else {
          checkedEpisodeIds.remove( e.id() );
        }
        tableViewer.refresh();
      }
    };

    private final ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {

      @Override
      public void selectionChanged( SelectionChangedEvent aEvent ) {
        IStructuredSelection sel = (IStructuredSelection)aEvent.getSelection();
        if( sel.isEmpty() ) {
          picViewer.setTsImage( null );
          ;
          return;
        }
        IEpisode e = (IEpisode)sel.getFirstElement();
        TsImage mi = tsContext().get( IPsxFileSystem.class ).findThumb( e.frame(), EP_THUMB_SIZE );
        picViewer.setTsImage( mi );
      }
    };

    private final SelectionListener btnClearAllListener = new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        checkedEpisodeIds.clear();
        tableViewer.refresh();
      }
    };

    private final SelectionListener btnSetAllListener = new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        checkedEpisodeIds.setAll( tsContext().get( IUnitEpisodes.class ).items().ids() );
        tableViewer.refresh();
      }
    };

    static final EThumbSize EP_THUMB_SIZE = EThumbSize.SZ360;

    private final Button      btnClearAll;
    private final Button      btnSetAll;
    final CheckboxTableViewer tableViewer;
    final IPdwWidget          picViewer;
    final IStringListEdit     checkedEpisodeIds = new StringLinkedBundleList();

    Panel( Composite aParent, TsDialog<IStringList, ITsGuiContext> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      if( dataRecordInput() != null ) {
        checkedEpisodeIds.setAll( dataRecordInput() );
      }
      // список эпизодов слева
      tableViewer = new CheckboxTableViewer( new Table( this, SWT.CHECK ) );
      tableViewer.getControl().setLayoutData( BorderLayout.WEST );
      tableViewer.setContentProvider( contentProvider );
      tableViewer.setCheckStateProvider( checkStateProvider );
      tableViewer.setLabelProvider( labelProvider );
      tableViewer.setInput( EMPTY_EPISODES_ARRAY ); // можно задать любой не-null объект
      tableViewer.addCheckStateListener( checkStateListener );
      tableViewer.addSelectionChangedListener( selectionChangedListener );
      // панель справа
      TsComposite right = new TsComposite( this );
      right.setLayout( new BorderLayout() );
      right.setLayoutData( BorderLayout.CENTER );
      right.setMinimumWidth( EP_THUMB_SIZE.size() + 2 * 5 );
      right.setMinimumHeight( 2 * EP_THUMB_SIZE.size() );
      // панель кнопок
      TsComposite buttonPane = new TsComposite( right );
      buttonPane.setLayout( new RowLayout( SWT.VERTICAL ) );
      buttonPane.setLayoutData( BorderLayout.NORTH );
      btnClearAll = new Button( buttonPane, SWT.PUSH );
      btnClearAll.setText( STR_T_BTN_CLEAR_ALL );
      btnClearAll.addSelectionListener( btnClearAllListener );
      btnSetAll = new Button( buttonPane, SWT.PUSH );
      btnSetAll.setText( STR_T_BTN_SET_ALL );
      btnSetAll.addSelectionListener( btnSetAllListener );
      // картинка снизу
      picViewer = new PdwWidgetSimple( tsContext() );
      picViewer.createControl( right );
      picViewer.setFitInfo( RectFitInfo.BEST_FILL );
      picViewer.setFulcrum( ETsFulcrum.CENTER );
      picViewer.setPreferredSizeFixed( false );
      picViewer.setFitInfo( RectFitInfo.BEST_FILL );
      picViewer.getControl().setLayoutData( BorderLayout.CENTER );
    }

    @Override
    protected void doSetDataRecord( IStringList aData ) {
      checkedEpisodeIds.clear();
      if( aData != null ) {
        IUnitEpisodes epMan = tsContext().get( IUnitEpisodes.class );
        for( String epId : aData ) {
          if( epMan.items().hasKey( epId ) ) {
            checkedEpisodeIds.add( epId );
          }
        }
      }
      tableViewer.refresh();
    }

    @Override
    protected IStringList doGetDataRecord() {
      return checkedEpisodeIds;
    }

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Выводит диалог и возвращает список идентификаторов помеченных эпизодов.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aCheckedEpisodeIds {@link IStringList} - список изначадльно помеченных эпизродов или null
   * @return {@link IStringList} - список идентификаторов эпизодов или null
   */
  public static final IStringList select( ITsGuiContext aContext, IStringList aCheckedEpisodeIds ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<IStringList, ITsGuiContext> creator = Panel::new;
    ITsDialogInfo cdi = new TsDialogInfo( aContext, STD_C_DIALOG_SELECT_EPISODE_IDS, STD_T_DIALOG_SELECT_EPISODE_IDS );
    TsDialog<IStringList, ITsGuiContext> d = new TsDialog<>( cdi, aCheckedEpisodeIds, aContext, creator );
    return d.execData();
  }

}
