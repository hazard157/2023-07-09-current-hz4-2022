package com.hazard157.lib.core.excl_done.fsviewers.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_done.fsviewers.*;
import com.hazard157.lib.core.glib.pgviewer.*;
import com.hazard157.lib.core.glib.pgviewer.impl.*;

/**
 * Реализация {@link IFsListThumbViewer}.
 *
 * @author hazard157
 */
public class FsListThumbViewer
    implements IFsListThumbViewer, ITsGuiContextable {

  private final IPgvVisualsProvider<File> visualsProvider = new IPgvVisualsProvider<>() {

    @Override
    public TsImage getThumb( File aEntity, EThumbSize aThumbSize ) {
      return imageManager().findThumb( aEntity, aThumbSize );
    }

    @Override
    public String getLabel1( File aEntity ) {
      return aEntity.getName();
    }

    @Override
    public String getLabel2( File aEntity ) {
      return TsLibUtils.EMPTY_STRING;
    }

    @Override
    public String getTooltip( File aEntity ) {
      return aEntity.getAbsolutePath();
    }

  };

  private final ITsDoubleClickListener<File> plcDoubleClickListener = new ITsDoubleClickListener<>() {

    @Override
    public void onTsDoubleClick( Object aSource, File aSelectedItem ) {
      doubleClickHelper.fireTsDoublcClickEvent( aSelectedItem );
    }
  };

  private final ITsSelectionChangeListener<File> plvSelectionListener = new ITsSelectionChangeListener<>() {

    @Override
    public void onTsSelectionChanged( Object aSource, File aSelectedItem ) {
      selectionChangeHelper.fireTsSelectionEvent( aSelectedItem );
    }
  };

  final TsDoubleClickEventHelper<File>     doubleClickHelper;
  final TsSelectionChangeEventHelper<File> selectionChangeHelper;
  final ITsGuiContext                      tsContext;
  final IPicsGridViewer<File>              pgViewer;
  // final IListEdit<File> files = new ElemLinkedBundleList<>();

  EThumbSize thumbSize = EThumbSize.SZ96;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsInternalErrorRtException в контексте приложения нет необходимых ссылок
   */
  public FsListThumbViewer( Composite aParent, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aParent, aContext );
    tsContext = aContext;
    doubleClickHelper = new TsDoubleClickEventHelper<>( this );
    selectionChangeHelper = new TsSelectionChangeEventHelper<>( this );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    IPicsGridViewerConstants.OP_DEFAULT_THUMB_SIZE.setValue( ctx.params(), avValobj( EThumbSize.SZ128 ) );
    pgViewer = new PicsGridViewer<>( aParent, ctx );
    pgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    pgViewer.setThumbSize( EThumbSize.SZ96 );
    pgViewer.setVisualsProvider( visualsProvider );
    pgViewer.addTsSelectionListener( plvSelectionListener );
    pgViewer.addTsDoubleClickListener( plcDoubleClickListener );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  // private void reloadThumbs() {
  // IMap<File, TsImage> map = MultimediaUtils.loadThumbs( files, thumbSize, tsContext.get( ITsImageManager.class ),
  // tsContext.get( Shell.class ) );
  // pgViewer.clearItems();
  // for( File f : map.keys() ) {
  // TsImage mi = map.getByKey( f );
  // String label1 = f.getName();
  // if( !mi.isDisposed() ) {
  // PlvItem item = new PlvItem( mi, label1, StringUtils.EMPTY_STRING, f );
  // pgViewer.addItem( item );
  // }
  // else {
  // LoggerUtils.errorLogger().error( FMT_ERR_DISPOSED_MI_IN_MAP, f.getPath() );
  // }
  // }
  // }

  // ------------------------------------------------------------------------------------
  // IThumbSizeable
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize != aThumbSize ) {
      thumbSize = aThumbSize;
      pgViewer.setThumbSize( thumbSize() );
      pgViewer.refresh();
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return EThumbSize.SZ96;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFsListThumbViewer
  //

  @Override
  public IList<File> items() {
    return pgViewer.items();
  }

  @Override
  public void setItems( IList<File> aItems ) {
    if( aItems != null ) {
      pgViewer.setItems( aItems );
    }
    else {
      pgViewer.setItems( IList.EMPTY );
    }
  }

  @Override
  public Control getControl() {
    return pgViewer.getControl();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionProvider
  //

  @Override
  public File selectedItem() {
    return pgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( File aItem ) {
    pgViewer.setSelectedItem( aItem );
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<File> aListener ) {
    selectionChangeHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<File> aListener ) {
    selectionChangeHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<File> aListener ) {
    doubleClickHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<File> aListener ) {
    doubleClickHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
