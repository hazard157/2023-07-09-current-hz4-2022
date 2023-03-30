package com.hazard157.psx.proj3.episodes;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Характеристики эпизода (срез) в момент времени {@link #currSec()}.
 *
 * @author hazard157
 */
public final class SecondSlice {

  private final IEpisode episode;
  private final int      currSec;

  private final PlaneGuide        plane;
  private final IStringList       notes;
  private final IListEdit<IScene> scenes;
  private final IStringListEdit   tagIds;

  /**
   * Создает объект.
   *
   * @param aEpisode {@link IEpisode} - эпизод
   * @param aCurrSec int - проеряемая секунда
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException секунда выходит за пределы эпизода
   */
  public SecondSlice( IEpisode aEpisode, int aCurrSec ) {
    episode = TsNullArgumentRtException.checkNull( aEpisode );
    TsIllegalArgumentRtException.checkTrue( aCurrSec < 0 || aCurrSec >= episode.duration() );
    currSec = aCurrSec;
    if( currSec < episode.duration() ) {
      scenes = new ElemArrayList<>( 32 );
      checkAndAddScene( episode.story() );
      tagIds = new StringArrayList();
      tagIds.setAll( episode.tagLine().tagIdsAt( currSec ) );
      plane = episode.planesLine().getMarkAt( currSec ).marker();
      notes = episode.noteLine().getNotesAt( currSec );
    }
    else {
      scenes = IList.EMPTY;
      tagIds = IStringList.EMPTY;
      plane = null;
      notes = IStringList.EMPTY;
    }
  }

  private void checkAndAddScene( IScene aScene ) {
    if( aScene.interval().contains( currSec ) ) {
      scenes.add( aScene );
      for( IScene s : aScene.childScenes() ) {
        checkAndAddScene( s );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IQpFilterInput
  //

  /**
   * Возвращает эпизод.
   *
   * @return {@link IEpisode} - эпизод
   */
  public IEpisode episode() {
    return episode;
  }

  /**
   * Возвращает момент времени - проверяемую фильтром секунду эпизода.
   *
   * @return int - секнда с начала эпизода (отчет начинается с 0)
   */
  public int currSec() {
    return currSec;
  }

  /**
   * Возвращает все идентификаторы ярлыков, корыми помечен эпизод в момент {@link #currSec()}.
   *
   * @return {@link IStringList} - идентификаторы ярлыков
   */
  public IStringList tagIds() {
    return tagIds;
  }

  /**
   * Возвращает список сцен (от корня-эпизода до листа), который определен в момент {@link #currSec()}.
   *
   * @return IList&lt;{@link IScene}&gt; - список сцен, путь от эпизода-корня к листу
   */
  public IList<IScene> scenes() {
    return scenes;
  }

  /**
   * Возвращает план, который определен в момент {@link #currSec()}.
   *
   * @return {@link PlaneGuide} - подклип плана или <code>null</code>
   */
  public PlaneGuide plane() {
    return plane;
  }

  /**
   * Возвращает список заметок, которые определены в момент {@link #currSec()}.
   *
   * @return IStringList - список заметок
   */
  public IStringList notes() {
    return notes;
  }

}
