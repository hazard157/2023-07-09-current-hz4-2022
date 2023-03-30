package com.hazard157.psx24.core.e4.services.currep;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx24.core.e4.services.currframeslist.*;
import com.hazard157.psx24.core.e4.services.selsvins.*;

/**
 * Реализаци {@link ICurrentEpisodeService}.
 *
 * @author goga
 */
public class CurrentEpisodeService
    extends CurrentEntityService<IEpisode>
    implements ICurrentEpisodeService {

  private final IUnitEpisodes unitEpisodes;

  /**
   * Конструктор.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public CurrentEpisodeService( IEclipseContext aAppContext ) {
    super( aAppContext );
    unitEpisodes = appContext().get( IUnitEpisodes.class );
  }

  @Override
  public void select( ETsCollMove aDirection ) {
    TsNullArgumentRtException.checkNull( aDirection );
    switch( aDirection ) {
      case FIRST:
        setCurrent( unitEpisodes.items().first() );
        break;
      case PREV:
        if( current() == null ) {
          setCurrent( unitEpisodes.items().last() );
        }
        else {
          setCurrent( unitEpisodes.items().prev( current() ) );
        }
        break;
      case NEXT:
        if( current() == null ) {
          setCurrent( unitEpisodes.items().first() );
        }
        else {
          setCurrent( unitEpisodes.items().next( current() ) );
        }
        break;
      case LAST:
        setCurrent( unitEpisodes.items().last() );
        break;
      case JUMP_NEXT:
      case JUMP_PREV:
      case MIDDLE:
      case NONE:
        // TODO what to do here?
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected void afterListenersInformed() {
    ICurrentFramesListService cfls = appContext().get( ICurrentFramesListService.class );
    IPsxSelectedSvinsService sss = appContext().get( IPsxSelectedSvinsService.class );
    if( cfls != null ) {
      if( current() != null ) {
        boolean isDetailedEpisodeIllustrations = false;
        if( isDetailedEpisodeIllustrations ) {
          // соберем все сцены самого нижнего уровня
          IListEdit<IScene> scenes = new ElemArrayList<>();
          collectLowLeveScenes( current().story(), scenes );
          IListEdit<IFrame> frames = new ElemArrayList<>();
          for( IScene s : scenes ) {
            frames.add( s.frame() );
          }
          cfls.setCurrent( frames );
        }
        else {
          cfls.setCurrent( current().getIllustrations( false ) );
        }
        Svin epSvinNoCam = Svin.removeCamId( current().svin() );
        sss.setSvin( epSvinNoCam );
      }
      else {
        cfls.setCurrent( null );
        sss.setSvin( null );
      }
    }
  }

  private void collectLowLeveScenes( IScene aScene, IListEdit<IScene> aList ) {
    if( aScene.childScenes().isEmpty() ) {
      aList.add( aScene );
      return;
    }
    for( IScene s : aScene.childScenes() ) {
      collectLowLeveScenes( s, aList );
    }
  }

}
