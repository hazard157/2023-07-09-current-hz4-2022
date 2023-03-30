package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательные методы для работы с сюжетом.
 *
 * @author hazard157
 */
public class StoryUtils {

  /**
   * создает иерархический номер сцены в сюжете, что-то типа "3.2.1".
   * <p>
   * Нумерация начинается с 1, а не с 0.
   *
   * @param aScene {@link IScene} - сцена
   * @return String - иерархический номер сцены в сюжете, что-то типа "3.2.1"
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static String getNumberString( IScene aScene ) {
    TsNullArgumentRtException.checkNull( aScene );
    String numStr = TsLibUtils.EMPTY_STRING;
    IScene s = aScene;
    while( s.parent() != null ) {
      int no = 1 + s.parent().childScenes().keys().indexOf( s.interval() );
      numStr = Integer.toString( no ) + numStr;
      s = s.parent();
      if( s.parent() != null ) {
        numStr = "." + numStr; //$NON-NLS-1$
      }
    }
    return numStr;
  }

  /**
   * Запрет на создание экземпляров.
   */
  private StoryUtils() {
    // nop
  }

}
