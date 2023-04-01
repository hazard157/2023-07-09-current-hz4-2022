package com.hazard157.psx24.core.glib.tagint;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Показывает ярлыки в виде дерева, начиная с указанного в качестве входного элемента узла.
 * <p>
 * Корневой элемент и все узлы дерева имеют тип {@link ITag}.
 *
 * @author hazard157
 */
public class TagsTreeContentProvider
    implements ITreeContentProvider {

  final static ITag[] EMPTY_TAGS_ARRAY = {};
  final boolean       showOnlyNonLeafs;
  private ITag        input            = null; // запомним вход, чтобы getParent() не поднимался выше

  /**
   * Строка фильтрования ярлыков, содержащих это строку.
   * <p>
   * Пустая строка означает отсутствие фильтра.
   */
  String filterString = TsLibUtils.EMPTY_STRING;

  /**
   * Конструктор.
   * <p>
   * Равнозначно вызову {@link TagsTreeContentProvider#TagsTreeContentProvider(boolean)} с аргументом aShowLeafs =
   * <code>true</code>.
   */
  public TagsTreeContentProvider() {
    this( true );
  }

  /**
   * Конструктор.
   *
   * @param aShowLeafs boolean - признак показа листьев дерева
   */
  public TagsTreeContentProvider( boolean aShowLeafs ) {
    showOnlyNonLeafs = !aShowLeafs;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private Object[] getChilds( ITag aNode ) {
    IListEdit<ITag> childs = new ElemArrayList<>();
    for( ITag node : aNode.childNodes() ) {
      boolean isIncluded = true;
      // если листья не показываем, то проверим, есть ли дети у этого узла
      if( showOnlyNonLeafs && node.childNodes().isEmpty() ) {
        isIncluded = false;
      }
      isIncluded = TagsUtils.isTagAccepterByTextFilter( node, filterString );
      if( isIncluded ) {
        childs.add( node );
      }
    }
    return childs.toArray( EMPTY_TAGS_ARRAY );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITreeContentProvider
  //

  @Override
  public void dispose() {
    // nop
  }

  @Override
  public void inputChanged( Viewer viewer, Object oldInput, Object newInput ) {
    // nop
  }

  @Override
  public Object[] getElements( Object aInputElement ) {
    if( !(aInputElement instanceof ITag node) ) {
      throw new TsIllegalArgumentRtException();
    }
    input = node;
    return getChilds( node );
  }

  @Override
  public Object[] getChildren( Object aElement ) {
    if( aElement instanceof ITag node ) {
      return getChilds( node );
    }
    return EMPTY_TAGS_ARRAY;
  }

  @Override
  public Object getParent( Object aElement ) {
    if( aElement == input ) {
      return null;
    }
    if( aElement instanceof ITag node ) {
      return node.parent();
    }
    return null;
  }

  @Override
  public boolean hasChildren( Object aElement ) {
    if( aElement instanceof ITag node ) {
      return !node.childNodes().isEmpty();
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает строку фильтрования ярлыков, содержащих это строку.
   *
   * @return String - строка фильтра или пустая строка при отсусттвии фильтрования.
   */
  public String getFilterString() {
    return filterString;
  }

  /**
   * Задает {@link #getFilterString()}.
   *
   * @param aFilterString String - строка фильтра или пустая строка при отсусттвии фильтрования.
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public void setFilterString( String aFilterString ) {
    TsNullArgumentRtException.checkNull( aFilterString );
    filterString = aFilterString;
  }

}
