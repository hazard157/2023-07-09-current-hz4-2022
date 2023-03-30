package com.hazard157.lib.core.glib.fsviewers.impl;

import java.io.File;
import java.util.Objects;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsDoubleClickListener;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsSelectionChangeListener;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.TsDoubleClickEventHelper;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.TsSelectionChangeEventHelper;
import org.toxsoft.core.tsgui.bricks.tstree.ITsTreeViewerConsole;
import org.toxsoft.core.tsgui.bricks.tstree.impl.TsTreeViewerConsole;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.image.EThumbSize;
import org.toxsoft.core.tsgui.graphics.image.TsImage;
import org.toxsoft.core.tsgui.utils.jface.TableLabelProviderAdapter;
import org.toxsoft.core.tsgui.utils.jface.ViewerPaintHelper;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.EFsObjKind;

import com.hazard157.lib.core.glib.fsviewers.IFsIconProvider;
import com.hazard157.lib.core.glib.fsviewers.IFsTreeViewer;
import com.hazard157.lib.core.incub.kof.IKofFileSystem;
import com.hazard157.lib.core.incub.optedfile.OptedFile;

/**
 * Реализация просмотрщика {@link IFsTreeViewer}.
 *
 * @author hazard157
 * @param <T> - type of OptedFile subclass
 */
public class FsTreeViewer<T extends OptedFile>
    implements IFsTreeViewer<T> {

  /**
   * Поставщие элементов для {@link TreeViewer} на базе {@link IKofFileSystem}.
   *
   * @author goga
   */
  class ContentProvider
      implements ITreeContentProvider {

    private EFsObjKind shownKind = EFsObjKind.DIR;

    @Override
    public Object[] getElements( Object aInputElement ) {
      return fsProvider().listRoots().toArray();
    }

    @Override
    public Object[] getChildren( Object aParentElement ) {
      return fsProvider().listChilds( ((OptedFile)aParentElement).file(), shownKind ).toArray();
    }

    @Override
    public Object getParent( Object aElement ) {
      return fsProvider().getParent( (File)aElement );
    }

    @Override
    public boolean hasChildren( Object aElement ) {
      if( aElement instanceof OptedFile of ) {
        return fsProvider().hasChilds( of.file(), shownKind );
      }
      return false;
    }

    public boolean isFilesShown() {
      return shownKind.isFile();
    }

    public void setFilesShown( boolean aShown ) {
      shownKind = aShown ? EFsObjKind.BOTH : EFsObjKind.DIR;
    }

  }

  /**
   * Дерево всегда рисуется этим отрисовщиком.
   * <p>
   * Текст поставляет {@link FsTreeViewer#labelProvider}, а значки - {@link FsTreeViewer#fileIconProvider()}.
   *
   * @author goga
   */
  class PaintHelper
      extends ViewerPaintHelper<Tree> {

    public PaintHelper() {
      super( true, true, true );
    }

    @Override
    protected boolean doEraseItem( Event aEvent ) {
      resetBit( aEvent, SWT.BACKGROUND );
      resetBit( aEvent, SWT.FOREGROUND );
      return false;
    }

    @Override
    protected void doPaintItem( Event aEvent ) {
      TreeItem item = (TreeItem)aEvent.item;
      Rectangle bounds = item.getBounds( aEvent.index );
      File f = ((OptedFile)(item.getData())).file();
      Image image = null;
      String text = labelProvider.getColumnText( f, aEvent.index );
      EThumbSize thumbSize = EThumbSize.findIncluding( iconSize().size(), iconSize().size() );
      if( f.isFile() ) {
        if( isFileContentThumbsUsed() ) {
          TsImage mi = fileIconProvider().getFileContentThumbnail( f, thumbSize );
          if( mi != null ) {
            image = mi.image();
          }
        }
        else {
          image = fileIconProvider().getFileTypeIcon( f, iconSize() );
        }
      }
      else {
        image = fileIconProvider().getDefaultFolderIcon( iconSize() );
      }
      int x = bounds.x;
      if( image != null ) {
        aEvent.gc.drawImage( image, x + (thumbSize.size() - image.getImageData().width) / 2,
            bounds.y + (bounds.height - image.getImageData().height) / 2 );
        x += thumbSize.size();
      }
      Point extent = aEvent.gc.textExtent( text );
      aEvent.gc.drawText( text, x + 3, bounds.y + (bounds.height - extent.y) / 2, true );
    }

    @Override
    protected void doMeasureItem( Event aEvent ) {
      aEvent.height = iconSize().size();
    }

  }

  /**
   * Поставщик текста для ячеек таблицы, а изображения поставляются и рисуются отрисовщиком
   * {@link FsTreeViewer.PaintHelper}.
   *
   * @author goga
   */
  static class LabelProvider
      extends TableLabelProviderAdapter {

    @Override
    public String getColumnText( Object aElem, int aColumnIndex ) {
      if( aElem instanceof File f ) {
        switch( aColumnIndex ) {
          case COLIDX_NAME:
            return f.getName();
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
      }
      return TsLibUtils.EMPTY_STRING;
    }

    @Override
    public String getText( Object aElem ) {
      return getColumnText( aElem, COLIDX_NAME );
    }

  }

  /**
   * Индекс столбца "имя файла/директория", это первый столбец, рисующий также значок.
   */
  static final int COLIDX_NAME = 0;

  final ContentProvider                 contentProvider = new ContentProvider();
  final ITableLabelProvider             labelProvider   = new LabelProvider();
  final TsDoubleClickEventHelper<T>     doubleClickHelper;
  final TsSelectionChangeEventHelper<T> selectionChangeHelper;

  private final TreeViewer           treeViewer;
  private final ITsTreeViewerConsole console;

  private IKofFileSystem<T> fsProvider;
  private EIconSize         iconSize              = EIconSize.IS_16X16;
  private boolean           fileContentThumbsUsed = true;
  private IFsIconProvider   fileIconProvider      = IFsIconProvider.NULL;

  boolean isInternalSelection = false;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aFileSystemProvider {@link IEclipseContext} - поставщик файловой системы
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsInternalErrorRtException в контексте приложения нет необходимых ссылок
   */
  public FsTreeViewer( Composite aParent, IKofFileSystem<T> aFileSystemProvider ) {
    TsNullArgumentRtException.checkNulls( aParent, aFileSystemProvider );
    fsProvider = aFileSystemProvider;
    treeViewer = new TreeViewer( aParent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
    PaintHelper paintHelper = new PaintHelper();
    paintHelper.install( treeViewer.getTree() );
    console = new TsTreeViewerConsole( treeViewer );
    doubleClickHelper = new TsDoubleClickEventHelper<>( this );
    selectionChangeHelper = new TsSelectionChangeEventHelper<>( this );
    treeViewer.setContentProvider( contentProvider );
    treeViewer.setLabelProvider( labelProvider );
    treeViewer.setInput( this ); // все равно, что задавать, лишь бы не null
    treeViewer.addSelectionChangedListener( aEvent -> {
      isInternalSelection = true;
      try {
        selectionChangeHelper.fireTsSelectionEvent( selectedItem() );
      }
      finally {
        isInternalSelection = false;
      }
    } );
    treeViewer.addDoubleClickListener( aEvent -> {
      isInternalSelection = true;
      try {
        doubleClickHelper.fireTsDoublcClickEvent( selectedItem() );
      }
      finally {
        isInternalSelection = false;
      }
    } );
    treeViewer.getControl().addDisposeListener( aE -> paintHelper.deinstall() );
  }

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionChangeEventProducer
  //

  @SuppressWarnings( "unchecked" )
  @Override
  public T selectedItem() {
    IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
    T sel = null;
    if( selection != null && !selection.isEmpty() ) {
      sel = (T)selection.getFirstElement();
    }
    return sel;
  }

  @Override
  public void setSelectedItem( T aItem ) {
    if( isInternalSelection || Objects.equals( selectedItem(), aItem ) ) {
      return;
    }
    T toSel = aItem;
    // если не отображаются файлы, то выделим директорию
    if( toSel != null ) {
      if( !isFilesShown() && !toSel.isDirectory() ) {
        toSel = fsProvider().getParent( toSel.file() );
      }
    }
    // FIXME отработать раскрытие нераскрытых узлов
    ISelection selection = null;
    if( aItem != null ) {
      selection = new StructuredSelection( toSel );
    }
    treeViewer.setSelection( selection, true );
    if( aItem != null ) {
      treeViewer.reveal( aItem );
    }
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeHelper.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IIconSizeable
  //

  @Override
  public EIconSize iconSize() {
    return iconSize;
  }

  @Override
  public void setIconSize( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    if( iconSize != aIconSize ) {
      T sel = selectedItem();
      iconSize = aIconSize;
      refresh();
      setSelectedItem( sel );
    }
  }

  @Override
  public EIconSize defaultIconSize() {
    return EIconSize.IS_16X16;
  }

  // ------------------------------------------------------------------------------------
  // IFsTreeViewer
  //

  @Override
  public IKofFileSystem<T> fsProvider() {
    return fsProvider;
  }

  @Override
  public void setFsProvider( IKofFileSystem<T> aFileSystemProvider ) {
    TsNullArgumentRtException.checkNull( aFileSystemProvider );
    if( fsProvider != aFileSystemProvider ) {
      T sel = selectedItem();
      fsProvider = aFileSystemProvider;
      refresh();
      setSelectedItem( sel );
    }
  }

  @Override
  public boolean isFilesShown() {
    return contentProvider.isFilesShown();
  }

  @Override
  public void setFilesShown( boolean aShown ) {
    if( contentProvider.isFilesShown() != aShown ) {
      T sel = selectedItem();
      contentProvider.setFilesShown( aShown );
      refresh();
      setSelectedItem( sel );
    }
  }

  @Override
  public boolean isFileContentThumbsUsed() {
    return fileContentThumbsUsed;
  }

  @Override
  public void setFileContentThumbsUsed( boolean aUse ) {
    if( fileContentThumbsUsed != aUse ) {
      T sel = selectedItem();
      fileContentThumbsUsed = aUse;
      refresh();
      setSelectedItem( sel );
    }
  }

  @Override
  public IFsIconProvider fileIconProvider() {
    return fileIconProvider;
  }

  @Override
  public void setFileIconProvider( IFsIconProvider aProvider ) {
    TsNullArgumentRtException.checkNull( aProvider );
    if( fileIconProvider != aProvider ) {
      T sel = selectedItem();
      fileIconProvider = aProvider;
      refresh();
      setSelectedItem( sel );
    }
  }

  @Override
  public ITsTreeViewerConsole console() {
    return console;
  }

  @Override
  public void refresh() {
    treeViewer.setInput( this ); // все равно, что зававать, лишь бы не null
    treeViewer.refresh();
  }

  @Override
  public void refresh( T aRefreshRoot ) {
    TsNullArgumentRtException.checkNull( aRefreshRoot );
    T refreshRoot = aRefreshRoot;
    if( !refreshRoot.isDirectory() ) {
      refreshRoot = fsProvider().getParent( refreshRoot.file() ); // вернет null для корневых папок
    }
    if( refreshRoot != null ) {
      treeViewer.refresh( refreshRoot );
    }
    else {
      treeViewer.refresh();
    }
  }

  @Override
  public Control getControl() {
    return treeViewer.getControl();
  }

}
