package com.hazard157.prisex24.glib.tagint;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Вспомогательные методы работы с ярлыками.
 *
 * @author hazard157
 */
public class TagsUtils {

  /**
   * Проверяет, что язел ярлыков соответствует текстовому фильтру.
   * <p>
   * Соответствие узла означает, что либо сам узел, либо хотя бы один элемент в поддереве соответствует фильтру.
   * <p>
   * Пустой строке соответствует любой ярлык.
   *
   * @param aNode {@link ITag} - проверяемый узел
   * @param aText String - текст фильтра
   * @return boolean - результат проверки на соответствие
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public static boolean isTagAccepterByTextFilter( ITag aNode, String aText ) {
    if( aNode == null || aText == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aText.isEmpty() ) {
      return true;
    }
    String text = aText.toLowerCase();
    return isAcceptedByTextFilter( aNode, text );
  }

  /**
   * Находит первый ярлык в списке, соответствующий текстовому фильтру.
   *
   * @param aTags IList&lt;{@link ITag}&gt; - проверяемый список ярдлыков
   * @param aText String - текст фильтра
   * @return {@link ITag} - найденный ярлык или <code>null</code> если нет совпадения
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public static ITag getFirstMatchingTag( IList<ITag> aTags, String aText ) {
    if( aTags == null || aText == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aText.isEmpty() || aTags.isEmpty() ) {
      return aTags.first();
    }
    String text = aText.toLowerCase();
    for( ITag tag : aTags ) {
      if( tag.id().toLowerCase().contains( text ) ) {
        return tag;
      }
    }
    return null;
  }

  /**
   * Возвращает читабельное представление {@link IStringList} для использования в {@link Object#toString()}.
   *
   * @param aStringList {@link IStringList} - список строк
   * @return String - читабельное представление {@link IStringList}
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static String sl2str( IStringList aStringList ) {
    TsNullArgumentRtException.checkNull( aStringList );
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < aStringList.size(); i++ ) {
      sb.append( aStringList.get( i ) );
      if( i < aStringList.size() - 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
    }
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private static boolean isAcceptedByTextFilter( ITag aNode, String aLowerCaseText ) {
    if( aNode.id().toLowerCase().contains( aLowerCaseText ) ) {
      return true;
    }
    for( ITag child : aNode.childNodes() ) {
      if( isAcceptedByTextFilter( child, aLowerCaseText ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Запрет на создание экземпляров.
   */
  private TagsUtils() {
    // nop
  }

}
