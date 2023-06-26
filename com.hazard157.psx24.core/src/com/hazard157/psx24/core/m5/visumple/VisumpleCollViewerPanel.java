package com.hazard157.psx24.core.m5.visumple;

import static com.hazard157.psx24.core.m5.visumple.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.common.dialogs.*;
import com.hazard157.lib.core.excl_plan.plv.*;
import com.hazard157.lib.core.excl_plan.visumple.*;

/**
 * Просмотрщик коллекции {@link Visumple} в таблицы миниатюр.
 *
 * @author hazard157
 */
public class VisumpleCollViewerPanel
    extends M5AbstractCollectionPanel<Visumple> {

  private final IGenericChangeListener itemsChangeListener = aSource -> refresh();

  private final ITsActionHandler toolbarListener = this::processAction;

  private IM5ItemsProvider<Visumple> itemsProvider;

  private static final EThumbSize DEFAULT_THUMB_SIZE = EThumbSize.SZ180;

  private TsToolbar          toolbar  = null;
  private PicturesListViewer plViewer = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @param aItemsProvider {@link IM5ItemsProvider} - постащик списка элементов, может быть <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public VisumpleCollViewerPanel( ITsGuiContext aContext, IM5Model<Visumple> aModel,
      IM5ItemsProvider<Visumple> aItemsProvider ) {
    super( aContext, aModel, true );
    setItemsProvider( aItemsProvider );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void processAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    switch( aActionId ) {
      case ACTID_ZOOM_IN:
        plViewer.setThumbSize( plViewer.thumbSize().nextSize() );
        refresh();
        break;
      case ACTID_ZOOM_ORIGINAL:
        plViewer.setThumbSize( DEFAULT_THUMB_SIZE );
        refresh();
        break;
      case ACTID_ZOOM_OUT:
        plViewer.setThumbSize( plViewer.thumbSize().prevSize() );
        refresh();
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация родительских методов
  //

  @Override
  protected TsComposite doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    toolbar = new TsToolbar( tsContext() );
    toolbar.setVertical( true );
    toolbar.setIconSize( EIconSize.IS_16X16 );
    toolbar.setActionDefs( ACDEF_ZOOM_IN, ACDEF_ZOOM_ORIGINAL_PUSHBUTTON, ACDEF_ZOOM_OUT );
    toolbar.createControl( board );
    toolbar.getControl().setLayoutData( BorderLayout.WEST );
    toolbar.addListener( toolbarListener );
    plViewer = new PicturesListViewer( board, EPlvLayoutMode.ROWS );
    plViewer.setThumbSize( DEFAULT_THUMB_SIZE );
    plViewer.getControl().setLayoutData( BorderLayout.CENTER );
    plViewer.addTsDoubleClickListener( ( aSource, aSelectedItem ) -> {
      if( aSelectedItem != null ) {
        // allow next/prev in image view dialog
        Visumple v = (Visumple)aSelectedItem.userData();
        File currFile = new File( v.filePath() );
        IListEdit<File> files = new ElemArrayList<>( items().size() );
        for( Visumple cv : items() ) {
          File f = new File( cv.filePath() );
          files.add( f );
        }
        DialogShowImageFiles.showFiles( tsContext(), files, currFile );
        doubleClickEventHelper.fireTsDoublcClickEvent( (Visumple)aSelectedItem.userData() );
      }
      else {
        doubleClickEventHelper.fireTsDoublcClickEvent( null );
      }
    } );
    plViewer.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      if( aSelectedItem != null ) {
        selectionChangeEventHelper.fireTsSelectionEvent( (Visumple)aSelectedItem.userData() );
      }
      else {
        selectionChangeEventHelper.fireTsSelectionEvent( null );
      }
    } );
    refresh();
    return board;
  }

  @Override
  public Visumple selectedItem() {
    PlvItem selItem = plViewer.selectedItem();
    if( selItem != null ) {
      return (Visumple)selItem.userData();
    }
    return null;
  }

  @Override
  public void setSelectedItem( Visumple aItem ) {
    plViewer.selectByUserData( aItem );
  }

  @Override
  public IList<Visumple> items() {
    if( itemsProvider != null ) {
      return new ElemArrayList<>( itemsProvider.listItems() );
    }
    return IList.EMPTY;
  }

  @Override
  public void refresh() {
    Visumple sel = selectedItem();
    ITsImageManager imageManager = tsContext().get( ITsImageManager.class );
    IList<Visumple> items = items();
    IListEdit<PlvItem> ll = new ElemArrayList<>( items.size() );
    for( Visumple v : items ) {
      File f = new File( v.filePath() );
      TsImage mi = imageManager.findThumb( f, plViewer.thumbSize() );
      if( mi != null ) {
        PlvItem item = new PlvItem( mi, f.getName(), EMPTY_STRING, v );
        ll.add( item );
      }
      else {
        LoggerUtils.errorLogger().error( FMT_ERR_NO_THUMB_OF_IMG_FILE, f.toString(), plViewer.thumbSize().toString() );
      }
    }
    plViewer.setItems( ll );
    setSelectedItem( sel );
  }

  @Override
  public IM5LifecycleManager<Visumple> lifecycleManager() {
    return null;
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<Visumple> aLifecycleManager ) {
    // nop
  }

  @Override
  public IM5ItemsProvider<Visumple> itemsProvider() {
    return itemsProvider;
  }

  @Override
  public void setItemsProvider( IM5ItemsProvider<Visumple> aItemsProvider ) {
    if( itemsProvider != null ) {
      itemsProvider.genericChangeEventer().removeListener( itemsChangeListener );
    }
    itemsProvider = aItemsProvider;
    if( itemsProvider != null ) {
      itemsProvider.genericChangeEventer().addListener( itemsChangeListener );
    }
  }

  @Override
  public ITsCheckSupport<Visumple> checkSupport() {
    return ITsCheckSupport.NONE;
  }

}
