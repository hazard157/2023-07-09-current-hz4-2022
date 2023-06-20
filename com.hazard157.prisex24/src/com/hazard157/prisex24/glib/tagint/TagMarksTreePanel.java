package com.hazard157.prisex24.glib.tagint;

import static com.hazard157.prisex24.glib.tagint.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.checkstate.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Компонента - дерево, редактор пометки ярлыками с поддержкой {@link ECheckState#GRAYED}.
 *
 * @author hazard157
 */
public class TagMarksTreePanel
    extends TsComposite
    implements IGenericChangeEventCapable, ITsSelectionProvider<ITag>, ITsDoubleClickEventProducer<ITag> {

  // FIXME обрабатывать offer -> hide/show unoffered/offered,
  // FIXME Use RADIO behaviour on radio groups

  class CheckStateProvider
      implements ICheckStateProvider {

    private ECheckState tagListCheckState( IList<ITag> aTags ) {
      boolean allChecked = true;
      boolean anyChecks = false;
      for( ITag t1 : aTags ) {
        ECheckState cs = tagMarks.findByKey( t1.id() );
        if( cs == null ) {
          cs = ECheckState.UNCHECKED;
        }
        switch( cs ) {
          case CHECKED:
            anyChecks = true;
            break;
          case GRAYED:
            anyChecks = true;
            allChecked = false;
            break;
          case UNCHECKED:
            allChecked = false;
            break;
          default:
            throw new TsNotAllEnumsUsedRtException();
        }
      }
      if( allChecked ) {
        return ECheckState.CHECKED;
      }
      if( anyChecks ) {
        return ECheckState.GRAYED;
      }
      return ECheckState.UNCHECKED;
    }

    @Override
    public boolean isChecked( Object aElement ) {
      if( aElement instanceof ITag tn ) {
        if( tn.childNodes().isEmpty() ) {
          ECheckState cs = tagMarks.findByKey( tn.id() );
          if( cs != null ) {
            return cs.isAnyCheck();
          }
          return false;
        }
        return tagListCheckState( tn.allNodesBelow() ).isAnyCheck();
      }
      return false;
    }

    @Override
    public boolean isGrayed( Object aElement ) {
      if( aElement instanceof ITag tn ) {
        if( tn.childNodes().isEmpty() ) {
          ECheckState cs = tagMarks.findByKey( tn.id() );
          if( cs != null ) {
            return cs.isGrayCheck();
          }
          return false;
        }
        return tagListCheckState( tn.allNodesBelow() ).isGrayCheck();
      }
      return false;
    }
  }

  final IGenericChangeListener tagManagerChangeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      ITag selItem = selectedItem();
      tagsTree.refresh();
      setSelectedItem( selItem );
    }
  };

  private final ITsActionHandler toolbarListener = this::processAction;

  private final IListEdit<ITagMarksCheckStateChangeListener> listeners = new ElemLinkedBundleList<>();

  final TsSelectionChangeEventHelper<ITag> selectionChangeEventHelper;
  final TsDoubleClickEventHelper<ITag>     doubleClickEventHelper;
  final GenericChangeEventer               genericChangeEventer;
  final CheckboxTreeViewer                 tagsTree;
  final IUnitTags                          tagsUnit;
  final IStringMapEdit<ECheckState>        tagMarks = new StringMap<>();
  final TsToolbar                          toolbar;
  final ITsGuiContext                      tsContext;

  /**
   * Создает компоненту без выбранных ярлыков.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aTagManager {@link IUnitTags} - менеджер ярлыков
   * @param aContext {@link ITsGuiContext} - the context
   */
  public TagMarksTreePanel( Composite aParent, IUnitTags aTagManager, ITsGuiContext aContext ) {
    super( aParent, SWT.NO_FOCUS );
    TsNullArgumentRtException.checkNulls( aTagManager, aContext );
    tsContext = aContext;
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
    genericChangeEventer = new GenericChangeEventer( this );
    setMinimumWidth( 380 );
    setMinimumHeight( 260 );
    setLayout( new BorderLayout() );
    // model = TsNullArgumentRtException.checkNull( aModel );
    tagsUnit = aTagManager;
    // toolbar
    toolbar = TsToolbar.create( this, tsContext, EIconSize.IS_16X16, ACDEF_COLLAPSE_ALL );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( toolbarListener );
    // tagsTree
    tagsTree = new CheckboxTreeViewer( this );
    tagsTree.getControl().setLayoutData( BorderLayout.CENTER );
    tagsTree.setContentProvider( new TagsTreeContentProvider() );
    tagsTree.setLabelProvider( new TagsTreeLabelProvider() );
    TreeColumn col0 = new TreeColumn( tagsTree.getTree(), SWT.LEFT );
    TreeColumn col1 = new TreeColumn( tagsTree.getTree(), SWT.LEFT );
    col0.setWidth( 220 );
    col0.setText( STR_COL_NAME_TAG_ID );
    col1.setWidth( 340 );
    col1.setText( STR_COL_NAME_TAG_DESCRIPTION );
    tagsTree.getTree().setHeaderVisible( true );
    tagsTree.getTree().setLinesVisible( true );
    tagsTree.setInput( tagsUnit.root() );
    tagsTree.setCheckStateProvider( new CheckStateProvider() );
    tagsTree.addCheckStateListener( event -> {
      processItemCheckStateChange( event );
      genericChangeEventer.fireChangeEvent();
    } );
    tagsTree.addSelectionChangedListener( event -> selectionChangeEventHelper.fireTsSelectionEvent( selectedItem() ) );
    tagsTree.addDoubleClickListener( aEvent -> doubleClickEventHelper.fireTsDoublcClickEvent( selectedItem() ) );
    tagsUnit.genericChangeEventer().addListener( tagManagerChangeListener );
    addDisposeListener( aE -> tagsUnit.genericChangeEventer().removeListener( tagManagerChangeListener ) );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void processAction( String aActionId ) {
    if( !toolbar.isActionEnabled( aActionId ) ) {
      return;
    }
    switch( aActionId ) {
      case ACTID_COLLAPSE_ALL:
        tagsTree.collapseAll();
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    updateActionsState();
  }

  void updateActionsState() {
    toolbar.setActionEnabled( ACTID_COLLAPSE_ALL, true );
  }

  ECheckState getTagCheckState( String aTagId ) {
    ECheckState cs = tagMarks.findByKey( aTagId );
    if( cs == null ) {
      return ECheckState.UNCHECKED;
    }
    return cs;
  }

  void processItemCheckStateChange( CheckStateChangedEvent aEvent ) {
    if( !(aEvent.getElement() instanceof ITag) ) {
      return;
    }
    ITag node = (ITag)aEvent.getElement();
    IStringListEdit tagIds = new StringLinkedBundleList();
    if( aEvent.getChecked() ) {
      for( String tId : node.allNodesBelow().keys() ) {
        if( getTagCheckState( tId ) != ECheckState.CHECKED ) {
          tagMarks.put( tId, ECheckState.CHECKED );
          tagIds.add( tId );
        }
      }
      if( getTagCheckState( node.id() ) != ECheckState.CHECKED ) {
        tagMarks.put( node.id(), ECheckState.CHECKED );
        tagIds.add( node.id() );
      }
    }
    else {
      for( String tId : node.allNodesBelow().keys() ) {
        if( getTagCheckState( tId ) != ECheckState.UNCHECKED ) {
          tagMarks.removeByKey( tId );
          tagIds.add( tId );
        }
      }
      if( getTagCheckState( node.id() ) != ECheckState.UNCHECKED ) {
        tagMarks.removeByKey( node.id() );
        tagIds.add( node.id() );
      }
    }
    tagsTree.refresh( node );
    if( !listeners.isEmpty() && !tagIds.isEmpty() ) {
      IList<ITagMarksCheckStateChangeListener> ll = new ElemLinkedBundleList<>( listeners );
      for( ITagMarksCheckStateChangeListener l : ll ) {
        if( aEvent.getChecked() ) {
          l.onTagsChecked( tagIds );
        }
        else {
          l.onTagsUnchecked( tagIds );
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает карту пометки ярлыками.
   *
   * @return {@link IStringMap} - карта "ИД ярлыка" - "состояние пометки на интервале"
   */
  public IStringMap<ECheckState> getTagMarks() {
    return tagMarks;
  }

  /**
   * Задает карту пометки ярлыками.
   *
   * @param aTagMarks {@link IStringMap} - карта "ИД ярлыка" - "состояние пометки на интервале"
   * @param aFireEvent boolean - признак генерации сообщения {@link IGenericChangeListener#onGenericChangeEvent(Object)}
   */
  public void setTagMarks( IStringMap<ECheckState> aTagMarks, boolean aFireEvent ) {
    tagMarks.setAll( aTagMarks );
    tagsTree.refresh();
    if( aFireEvent ) {
      genericChangeEventer.fireChangeEvent();
    }
  }

  /**
   * Возвращает выбранный узел дерева.
   *
   * @return {@link ITag} - выбранный элемент
   */
  @Override
  public ITag selectedItem() {
    IStructuredSelection selection = (IStructuredSelection)tagsTree.getSelection();
    if( selection == null || selection.isEmpty() ) {
      return null;
    }
    return (ITag)selection.getFirstElement();
  }

  /**
   * Возвращает идентификатор выбранного ярлыка {@link ITag}.
   * <p>
   * Если нет выбранного элемента, или выбрана группа, возвращает null.
   *
   * @return String - идентификатор выбранного ярлыка или null
   */
  public String getSelectedTagId() {
    IStructuredSelection selection = (IStructuredSelection)tagsTree.getSelection();
    if( selection == null || selection.isEmpty() ) {
      return null;
    }
    Object o = selection.getFirstElement();
    if( o instanceof ITag ) {
      return ((ITag)o).id();
    }
    return null;
  }

  /**
   * Делает выбранным и видимым в дереве объект (который вернет метод {@link #selectedItem()}).
   *
   * @param aTagItem {@link ITag} - выбыраемый объект, может быть null
   */
  @Override
  public void setSelectedItem( ITag aTagItem ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aTagItem != null ) {
      selection = new StructuredSelection( aTagItem );
    }
    tagsTree.setSelection( selection, true );
  }

  /**
   * Делает выбранным и видимым в дереве указанный ярлык.
   * <p>
   * Если аргумент равен null или нет ярлыка {@link ITag} с таким идентификатором, убирает выбранность в дереве.
   *
   * @param aTagId String - идентификатор ярлыка или null
   */
  public void selectTag( String aTagId ) {
    IStructuredSelection selection = StructuredSelection.EMPTY;
    if( aTagId != null ) {
      ITag tag = tagsUnit.root().allNodesBelow().findByKey( aTagId );
      if( tag != null ) {
        selection = new StructuredSelection( tag );
      }
    }
    tagsTree.setSelection( selection, true );
  }

  /**
   * Добавляет слушатель изменений в помектах ярлыками.
   *
   * @param aListener {@link ITagMarksCheckStateChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addTagMarksCheckStateChangeListener( ITagMarksCheckStateChangeListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  /**
   * Удаляет слушатель изменений в помектах ярлыками.
   *
   * @param aListener {@link ITagMarksCheckStateChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void removeTagMarksCheckStateChangeListener( ITagMarksCheckStateChangeListener aListener ) {
    listeners.remove( aListener );
  }

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<ITag> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<ITag> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<ITag> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<ITag> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

}
