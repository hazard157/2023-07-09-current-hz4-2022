package com.hazard157.prisex24.glib.frasel;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.utils.animkind.*;
import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * {@link ISvinFramesSelectionStrategy} implementation.
 *
 * @author hazard157
 */

public class SvinFramesSelectionStrategy
    implements ISvinFramesSelectionStrategy, IPsxGuiContextable {

  private final GenericChangeEventer eventer;
  private final ITsGuiContext        tsContext;
  private final IStringListEdit      cameraIds = new StringArrayList();

  private EAnimationKind animationKind  = EAnimationKind.ANIMATED;
  private boolean        onlySvinFrames = false;
  private EFramesPerSvin framesPerSvin  = EFramesPerSvin.SELECTED;

  private IStringListEdit tmpCamList = new StringArrayList();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SvinFramesSelectionStrategy( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    eventer = new GenericChangeEventer( this );

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
    if( onlySvinFrames ) {
      tmpCamList.add( aSvin.cameraId() );
    }
    else {
      if( cameraIds.isEmpty() ) {
        tmpCamList.setAll( allFrames1.listCameraIds() );
      }
      else {
        tmpCamList.setAll( cameraIds );
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
      if( animationKind.accept( f.isAnimated() ) && //
          aSvin.interval().contains( f.secNo() ) && //
          tmpCamList.hasElem( f.cameraId() ) ) {
        ll.add( f );
      }
    }
    // process framesPerSvin
    switch( framesPerSvin ) {
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
        throw new TsNotAllEnumsUsedRtException( framesPerSvin.id() );
    }
  }

  private void internalSelectOneFromSvinFrames( Svin aSvin, IList<IFrame> aSelectedFrames, IListEdit<IFrame> aFrames ) {
    // now return simply the first frame, prefer animated one
    if( animationKind.isAnimated() ) {
      for( IFrame f : aSelectedFrames ) {
        if( f.isAnimated() && aSvin.interval().contains( f.secNo() ) ) {
          aFrames.add( f );
          return;
        }
      }
    }
    // reteurn still frame, even if not allowed by #animationKind
    for( IFrame f : aSelectedFrames ) {
      if( aSvin.interval().contains( f.secNo() ) ) {
        aFrames.add( f );
        return;
      }
    }
  }

  private void internalFindIntervalBestFrame( Svin aSvin, IList<IFrame> aAllFrames, IListEdit<IFrame> aFrames ) {
    // now return simply the first frame, prefer animated one
    if( animationKind.isAnimated() ) {
      for( IFrame f : aAllFrames ) {
        if( f.isAnimated() && aSvin.interval().contains( f.secNo() ) ) {
          aFrames.add( f );
          return;
        }
      }
    }
    // reteurn still frame, even if not allowed by #animationKind
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

  // ------------------------------------------------------------------------------------
  // IGenericChangeListener
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // ISvinFramesSelectionStrategy
  //

  @Override
  public EAnimationKind animationKind() {
    return animationKind;
  }

  @Override
  public void setAnimationKind( EAnimationKind aAnimationKind ) {
    TsNullArgumentRtException.checkNull( aAnimationKind );
    if( animationKind != aAnimationKind ) {
      animationKind = aAnimationKind;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public boolean isOnlySvinCams() {
    return onlySvinFrames;
  }

  @Override
  public void setOnlySvinCams( boolean aOnlySvinCams ) {
    if( onlySvinFrames != aOnlySvinCams ) {
      onlySvinFrames = aOnlySvinCams;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public IStringList cameraIds() {
    return cameraIds;
  }

  @Override
  public void setCameraIds( IStringList aCameraIds ) {
    TsNullArgumentRtException.checkNull( aCameraIds );
    if( !cameraIds.equals( aCameraIds ) ) {
      cameraIds.setAll( aCameraIds );
      eventer.fireChangeEvent();
    }
  }

  @Override
  public EFramesPerSvin framesPerSvin() {
    return framesPerSvin;
  }

  @Override
  public void setFramesPerSvin( EFramesPerSvin aFramesPerSvin ) {
    TsNullArgumentRtException.checkNull( aFramesPerSvin );
    if( framesPerSvin != aFramesPerSvin ) {
      framesPerSvin = aFramesPerSvin;
      eventer.fireChangeEvent();
    }
  }

  @Override
  public IList<IFrame> selectFrames( Svin aSvin ) {
    TsNullArgumentRtException.checkNull( aSvin );
    String epId = aSvin.episodeId();
    IFramesSet allFrames = cofsFrames().listEpisodeFrames( epId );
    int bc = TsCollectionsUtils.getListInitialCapacity( TsCollectionsUtils.estimateOrder( allFrames.items().size() ) );
    IListEdit<IFrame> ll = new ElemLinkedBundleList<>( bc, true );
    collectFrames( aSvin, ll );
    return ll;
  }

}
