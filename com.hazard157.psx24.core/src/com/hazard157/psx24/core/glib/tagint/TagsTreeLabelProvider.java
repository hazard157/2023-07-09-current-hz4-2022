package com.hazard157.psx24.core.glib.tagint;

import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Поставщик текста ячеек для дерева всех ярлыков.
 * <p>
 * Поставщик ожидает, что все узлы дерева имеют тип {@link ITag}, и что в дереве два столбца, соответсвующие
 * {@link ITag#nmName()} и {@link ITag#description()}.
 *
 * @author goga
 */
public class TagsTreeLabelProvider
    extends TableLabelProviderAdapter {

  /**
   * Пустой конструктор.
   */
  public TagsTreeLabelProvider() {
    // nop
  }

  /**
   * Columns:<br>
   * 0 - id<br>
   * 1 - description<br>
   */
  @Override
  public String getColumnText( Object aElem, int aColumnIndex ) {
    if( aElem instanceof ITag node ) {
      switch( aColumnIndex ) {
        case 0:
          return node.nmName();
        case 1:
          return node.description();
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    return null;
  }

  @Override
  public String getText( Object element ) {
    if( !(element instanceof ITag node) ) {
      return TsLibUtils.EMPTY_STRING;
    }
    // return node.toString();
    return node.nmName();
  }

}
