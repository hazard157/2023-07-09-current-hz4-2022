package com.hazard157.psx24.intro.utils;

import static com.hazard157.psx24.intro.utils.IPsxResources.*;

import java.io.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.dialogs.*;
import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.*;
import com.hazard157.psx24.core.bricks.pq.*;
import com.hazard157.psx24.core.bricks.pq.filters.*;
import com.hazard157.psx24.core.bricks.unit.*;
import com.hazard157.psx24.core.e4.services.filesys.*;
import com.hazard157.psx24.core.utils.*;

/**
 * Shows an arbitrary GIF animated frame in a separate window.
 *
 * @author hazard157
 */
public class StartupGifDisplay
    implements IPsxGuiContextable {

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
  // private final IUnitEpisodes unitEpisodes;
  private final IPsxFileSystem fileSystem;
  private final IEpisode       episode;

  private IListEdit<IFrame> frList = IList.EMPTY;

  private int frameIndex = 0;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StartupGifDisplay( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    fileSystem = tsContext.get( IPsxFileSystem.class );
    // selection criteria by random episode
    episode = getRandomEpisode();
    if( episode == null ) {
      return;
    }

    InquiryItem ii = new InquiryItem();
    ITsSingleFilterParams sfp = PqFilterEpisodeIds.makeFilterParams( new SingleStringList( episode.id() ) );
    ii.fpMap().put( EPqSingleFilterKind.EPISODE_IDS, sfp );
    // selection criteria by tags
    sfp = PqFilterTagIds.makeFilterParams( SELECTION_TAG_IDS, true );
    ii.fpMap().put( EPqSingleFilterKind.TAG_IDS, sfp );
    // select intervals
    ISvinSeq inAll = PqQueryProcessor.createFull( unitEpisodes() );
    PqQueryProcessor p = new PqQueryProcessor( inAll, unitEpisodes() );
    ITsCombiFilterParams cfp = ii.getFilterParams();
    ISvinSeq out = p.queryData( cfp );
    IList<Svin> svins = out.listByEpisode( episode.id() );
    if( svins == null || svins.isEmpty() ) {
      return;
    }
    SecintsList sl = new SecintsList();
    for( Svin s : svins ) {
      // expand DOWN the intervals a bit to use animated frames capturing the interval
      int start = s.interval().start() - (IPsxHardConstants.ANIMATED_GIF_SECS / 2 + 1);
      if( start < 0 ) {
        start = 0;
      }
      sl.add( new Secint( start, s.interval().end() ) );
    }
    // list all animated frames of the episode
    IPsxFileSystem fs = tsContext.get( IPsxFileSystem.class );
    Svin svin = Svin.removeCamId( episode.svin() );
    FrameSelectionCriteria fsc = new FrameSelectionCriteria( svin, EAnimationKind.ANIMATED, true );
    IList<IFrame> epFrames = fs.listEpisodeFrames( fsc );
    // select only frames from the specified intervals
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
      // preload the first GIF image
      int fIndex = new Random().nextInt( frList.size() );
      File f = fileSystem.findFrameFile( frList.get( fIndex ) );
      if( f != null ) {
        ITsImageManager imagesManager = tsContext.get( ITsImageManager.class );
        imagesManager.findImage( f );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IEpisode getRandomEpisode() {
    if( unitEpisodes().items().isEmpty() ) {
      return null;
    }
    Random random = new Random();
    int rind = random.nextInt();
    if( rind < 0 ) {
      rind = -rind;
    }
    rind %= unitEpisodes().items().size();
    return unitEpisodes().items().get( rind );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Display an animation in the separate window.
   */
  public void show() {
    if( frList.isEmpty() ) {
      Shell shell = tsContext.get( Shell.class );
      TsDialogUtils.warn( shell, FMT_WARN_FRAMES_LIST_EMPTY,
          episode != null ? episode.id() : Objects.toString( episode ) );
      return;
    }
    DialogShowImageFiles.showItemsNonModal( tsContext, frList, frList.get( frameIndex ),
        new FrameVisualsProvider( tsContext ) );
  }

}
