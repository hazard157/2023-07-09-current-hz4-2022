package com.hazard157.psx24.intro.utils;

import static com.hazard157.psx24.intro.utils.IPsxResources.*;

import java.io.*;
import java.util.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.bricks.beq.*;
import com.hazard157.psx.proj3.bricks.beq.filters.*;
import com.hazard157.psx.proj3.bricks.beq.impl.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.glib.dialogs.imgs.*;

/**
 * Показывает произвольный GIF анимированный кадр в тодельном окне.
 *
 * @author hazard157
 */
public class StartupGifDisplay {

  @SuppressWarnings( "nls" )
  private static final IStringList SELECTION_TAG_IDS = new StringArrayList( //
      "action.heat.hell", //
      "action.heat.hot", //
      "view.cock_move", //
      "view.cock_stand", //
      "fuck.fuck_flags.intense", //
      "fuck.fuck_flags.she_moves", //
      "action.kind.cum", //
      "fuck.fuck_speed.fast" //
  );

  private final ITsGuiContext tsContext;
  private final IUnitEpisodes unitEpisodes;
  private final IEpisode      episode;

  private IListEdit<IFrame> frList = IList.EMPTY;

  private int frameIndex = 0;

  /**
   * Конструктор.
   *
   * @param aWinContext {@link IEclipseContext} - контекст приложения уровня окна
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public StartupGifDisplay( IEclipseContext aWinContext ) {
    tsContext = new TsGuiContext( aWinContext );
    unitEpisodes = tsContext.get( IUnitEpisodes.class );
    // фильтр по эпизоду
    episode = getRandomEpisode();
    if( episode == null ) {
      return;
    }
    BeqFilter bf = new BeqFilter();
    ITsSingleFilterParams sfp = BeqSingleFilterEpisodeIds.makeFilterParams( new SingleStringList( episode.id() ) );
    bf.fpMap().put( EBeqSingleFilterKind.EPISODE_IDS, sfp );
    // фильтр по ярлыкам
    sfp = BeqSingleFilterTagIds.makeFilterParams( SELECTION_TAG_IDS, true );
    bf.fpMap().put( EBeqSingleFilterKind.TAG_IDS, sfp );
    // выбор интервалов
    IBeqResult inAll = BeqUtils.createFull( unitEpisodes );
    IBeqProcessor p = new BeqProcessor( inAll, unitEpisodes );
    ITsCombiFilterParams cfp = bf.makeFilterParams();
    IBeqResult out = p.queryData( cfp );
    IList<Svin> svins = out.epinsMap().findByKey( episode.id() );
    if( svins == null || svins.isEmpty() ) {
      return;
    }
    SecintsList sl = new SecintsList();
    for( Svin s : svins ) {
      // немного расширим ВНИЗ интервалы для использования анимированных кадров, захватывающих интервал
      int start = s.interval().start() - (IPsxHardConstants.ANIMATED_GIF_SECS / 2 + 1);
      if( start < 0 ) {
        start = 0;
      }
      sl.add( new Secint( start, s.interval().end() ) );
    }
    // возьмем все анимированные кадры эпизода
    IPsxFileSystem fs = tsContext.get( IPsxFileSystem.class );
    Svin svin = Svin.removeCamId( episode.svin() );
    FrameSelectionCriteria fsc = new FrameSelectionCriteria( svin, EAnimationKind.ANIMATED, true );
    IList<IFrame> epFrames = fs.listEpisodeFrames( fsc );
    // отберем только кадры из указанных интервалов
    frList = new ElemArrayList<>( epFrames.size() );
    for( IFrame f : epFrames ) {
      for( Secint s : sl ) {
        if( s.contains( f.secNo() ) ) {
          frList.add( f );
          break;
        }
      }
    }
    if( !frList.isEmpty() ) {
      // предварительно загрузим первое GIF изображение
      frameIndex = new Random().nextInt( frList.size() );
      IPsxFileSystem fileSystem = tsContext.get( IPsxFileSystem.class );
      File f = fileSystem.findFrameFile( frList.get( frameIndex ) );
      if( f != null ) {
        ITsImageManager imagesManager = tsContext.get( ITsImageManager.class );
        imagesManager.findImage( f );
      }
    }
  }

  private IEpisode getRandomEpisode() {
    if( unitEpisodes.items().isEmpty() ) {
      return null;
    }
    // произвольный эпизод
    Random random = new Random();
    int rind = random.nextInt();
    if( rind < 0 ) {
      rind = -rind;
    }
    rind %= unitEpisodes.items().size();
    return unitEpisodes.items().get( rind );
  }

  /**
   * Собственно показ анимации в отдельно окне.
   */
  public void show() {
    if( frList.isEmpty() ) {
      Shell shell = tsContext.get( Shell.class );
      TsDialogUtils.warn( shell, FMT_WARN_FRAMES_LIST_EMPTY,
          episode != null ? episode.id() : Objects.toString( episode ) );
      return;
    }
    DialogPsxShowFullSizedFrameImage.showNonModal( frList.get( frameIndex ), tsContext, frList );
  }

}
