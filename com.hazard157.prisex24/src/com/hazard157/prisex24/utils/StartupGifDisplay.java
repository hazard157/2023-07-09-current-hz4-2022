package com.hazard157.prisex24.utils;

import static com.hazard157.prisex24.utils.IPsxResources.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.dialogs.*;
import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secint.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.explorer.filters.*;
import com.hazard157.prisex24.explorer.pdu.*;
import com.hazard157.prisex24.explorer.pq.*;
import com.hazard157.prisex24.utils.frasel.*;
import com.hazard157.psx.common.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.proj3.episodes.*;

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
  private final IEpisode      episode;

  private IList<IFrame> frList = IList.EMPTY;

  private int frameIndex = 0;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StartupGifDisplay( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
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

    ISvinFramesParams svinFramesParams = new SvinFramesParams();
    svinFramesParams.setAnimationKind( EAnimationKind.ANIMATED );
    svinFramesParams.setCameraIds( IStringList.EMPTY );
    svinFramesParams.setFramesPerSvin( EFramesPerSvin.SELECTED );
    svinFramesParams.setOnlySvinCams( false );
    SvinFramesSelector svinFramesSelector = new SvinFramesSelector( aContext );
    frList = svinFramesSelector.selectFrames( svins, svinFramesParams );

    if( !frList.isEmpty() ) {
      // preload the first GIF image
      int fIndex = new Random().nextInt( frList.size() );
      File f = cofsFrames().findFrameFile( frList.get( fIndex ) );
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
   *
   * @return {@link IEpisode} - the random episode of stratup GIF or <code>null</code>
   */
  public IEpisode show() {
    if( !frList.isEmpty() ) {
      DialogShowImageFiles.showItemsNonModal( tsContext, frList, frList.get( frameIndex ),
          new FrameVisualsProvider( tsContext ) );
    }
    else {
      TsDialogUtils.warn( getShell(), FMT_WARN_FRAMES_LIST_EMPTY,
          episode != null ? episode.id() : Objects.toString( episode ) );
    }
    return episode;
  }

}
