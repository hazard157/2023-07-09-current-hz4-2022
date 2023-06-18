package com.hazard157.prisex24.utils.frasel;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * Selects frames based on {@link ISvinFramesParams} parameters.
 *
 * @author hazard157
 */

public final class SvinFramesSelector
    implements IPsxGuiContextable {

  private final ITsGuiContext tsContext;
  private IStringListEdit     tmpCamList = new StringArrayList();

  private ISvinFramesParams params = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SvinFramesSelector( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void collectFrames( Svin aSvin, IListEdit<IFrame> aFrames ) {
    TsNullArgumentRtException.checkNulls( aSvin, aFrames );
    IFramesSet allFrames1 = cofsFrames().listEpisodeFrames( aSvin.episodeId() );
    if( allFrames1.isEmpty() ) {
      return;
    }
    // tmpCamList -> which cameras to include
    tmpCamList.clear();
    if( params.isOnlySvinCams() ) {
      tmpCamList.add( aSvin.cameraId() );
    }
    else {
      if( params.cameraIds().isEmpty() ) {
        tmpCamList.setAll( allFrames1.listCameraIds() );
      }
      else {
        tmpCamList.setAll( params.cameraIds() );
      }
    }
    // preselect frames in interval
    int estCount = 5 * aSvin.interval().duration(); // 5 = estimated cams-per-episode
    int bc = TsCollectionsUtils.getListInitialCapacity( TsCollectionsUtils.estimateOrder( estCount ) );
    IListEdit<IFrame> intervalFrames = new ElemLinkedBundleList<>( bc, true );
    allFrames1.collectInInterval( aSvin.interval(), intervalFrames );
    if( intervalFrames.isEmpty() ) {
      return;
    }
    // filter out according to current settings
    IListEdit<IFrame> ll = new ElemLinkedBundleList<>( bc, true );
    for( IFrame f : intervalFrames ) {
      if( params.animationKind().accept( f.isAnimated() ) && //
          aSvin.interval().contains( f.secNo() ) && //
          (params.secondsStep().accept( f.secNo() ) || f.isAnimated()) && //
          tmpCamList.hasElem( f.cameraId() ) ) {
        ll.add( f );
      }
    }
    // process framesPerSvin
    switch( params.framesPerSvin() ) {
      case SELECTED: {
        aFrames.addAll( ll );
        return;
      }
      case ONE_NO_MORE: {
        if( ll.size() <= 1 ) {
          aFrames.addAll( ll );
          return;
        }
        internalSelectOneFromSvinFrames( aSvin, ll, aFrames );
        return;
      }
      case FORCE_ONE: {
        if( ll.size() == 1 ) {
          aFrames.addAll( ll );
          return;
        }
        if( ll.size() > 1 ) {
          internalSelectOneFromSvinFrames( aSvin, ll, aFrames );
          return;
        }
        internalFindIntervalBestFrame( aSvin, intervalFrames, aFrames );
        return;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( params.framesPerSvin().id() );
    }
  }

  private void internalSelectOneFromSvinFrames( Svin aSvin, IList<IFrame> aSelectedFrames, IListEdit<IFrame> aFrames ) {
    // now return simply the first frame, prefer animated one
    if( params.animationKind().isAnimated() ) {
      for( IFrame f : aSelectedFrames ) {
        if( f.isAnimated() && aSvin.interval().contains( f.secNo() ) ) {
          aFrames.add( f );
          return;
        }
      }
    }
    // Return still frame, even if not allowed by #animationKind
    for( IFrame f : aSelectedFrames ) {
      if( aSvin.interval().contains( f.secNo() ) ) {
        aFrames.add( f );
        return;
      }
    }
  }

  private void internalFindIntervalBestFrame( Svin aSvin, IList<IFrame> aAllFrames, IListEdit<IFrame> aFrames ) {
    // now return simply the first frame, prefer animated one
    if( params.animationKind().isAnimated() ) {
      for( IFrame f : aAllFrames ) {
        if( f.isAnimated() && aSvin.interval().contains( f.secNo() ) ) {
          aFrames.add( f );
          return;
        }
      }
    }
    // return still frame, even if not allowed by #animationKind
    for( IFrame f : aAllFrames ) {
      if( aSvin.interval().contains( f.secNo() ) ) {
        aFrames.add( f );
        return;
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

  /**
   * Selects and returns frames for the specified SVIN.
   * <p>
   * If SVIN contains invalid data (eg non-existing episode, bad interval) then returns an empty list.
   *
   * @param aSvin {@link Svin} - the SVIN
   * @param aParams {@link ISvinFramesParams} - frame selection parameters
   * @return {@link IList}&lt;{@link IFrame}&gt; - selected frames
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IList<IFrame> selectFrames( Svin aSvin, ISvinFramesParams aParams ) {
    TsNullArgumentRtException.checkNulls( aSvin, aParams );
    String epId = aSvin.episodeId();
    params = aParams;
    IFramesSet allFrames = cofsFrames().listEpisodeFrames( epId );
    int bc = TsCollectionsUtils.getListInitialCapacity( TsCollectionsUtils.estimateOrder( allFrames.items().size() ) );
    IListEdit<IFrame> ll = new ElemLinkedBundleList<>( bc, true );
    collectFrames( aSvin, ll );
    return ll;
  }

}
