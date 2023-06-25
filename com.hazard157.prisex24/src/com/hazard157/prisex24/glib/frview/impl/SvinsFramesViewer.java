package com.hazard157.prisex24.glib.frview.impl;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.common.quants.ankind.AnimationKindDropDownMenuCreator.*;
import static com.hazard157.common.quants.secstep.SecondsSteppableDropDownMenuCreator.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.utils.frasel.FramesPerSvinDropDownMenuCreator.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;
import static org.toxsoft.core.tsgui.graphics.image.impl.ThumbSizeableDropDownMenuCreator.*;
import static org.toxsoft.core.tslib.coll.impl.TsCollectionsUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.common.quants.secint.*;
import com.hazard157.common.quants.secstep.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.glib.dialogs.*;
import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.utils.camenu.*;
import com.hazard157.prisex24.utils.frasel.*;
import com.hazard157.prisex24.utils.gifmgmt.*;
import com.hazard157.prisex24.utils.playmenu.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;

/**
 * {@link ISvinsFramesViewer} implementation.
 *
 * @author hazard157
 */
public class SvinsFramesViewer
    extends TsStdEventsProducerPanel<IFrame>
    implements ISvinsFramesViewer, ITsActionHandler, IPsxGuiContextable {

  private final ISvinSeqEdit      svinSeq = new SvinSeq();
  private final ISvinFramesParams svinFramesParams;

  private final TsToolbar         toolbar;
  private final IFramesGridViewer fgViewer;

  private final AbstractGifManagemntDropDownMenuCreator gifMenuCreator;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SvinsFramesViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    svinFramesParams = new SvinFramesParams();
    this.setLayout( new BorderLayout() );
    // fgViewer
    toolbar = new TsToolbar( tsContext() );
    toolbar.setVertical( false );
    toolbar.setActionDefs( //
        ACDEF_GIF_TEST_MENU, ACDEF_SEPARATOR, //
        AI_THUMB_SIZEABLE_ZOOM_MENU, AI_SEC_STEPPABLE_ZOOM_ORIGINAL_MENU, AI_ANIMATION_KINDABLE_MENU, ACDEF_SEPARATOR, //
        ACDEF_CAM_ID_MENU, //
        ACDEF_PLAY_MENU, ACDEF_SEPARATOR, //
        ACDEF_WORK_WITH_FRAMES, //
        AI_FRAMES_PER_SVIN_MENU, //
        ACDEF_SEPARATOR, //
        ACDEF_COPY_FRAME, //
        ACDEF_RUN_KDENLIVE //

    );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // fgViewer
    fgViewer = new FramesGridViewer( this, tsContext() );
    fgViewer.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    toolbar.addListener( this );
    gifMenuCreator = new AbstractGifManagemntDropDownMenuCreator( tsContext() ) {

      @Override
      protected IFrame doGetFrame() {
        return selectedItem();
      }

    };
    toolbar.setActionMenu( ACTID_GIF_TEST, gifMenuCreator );
    toolbar.setActionMenu( AID_THUMB_SIZEABLE_ZOOM_MENU, new ThumbSizeableDropDownMenuCreator(
        fgViewer.thumbSizeManager(), tsContext(), IS_16X16, PSX_MIN_FRAME_THUMB_SIZE, PSX_MAX_FRAME_THUMB_SIZE ) );
    toolbar.setActionMenu( ACTID_CAM_ID_MENU, new AbstractCamerasManagemntDropDownMenuCreator( tsContext() ) {

      @Override
      protected IStringList listAvailableCameraIds() {
        return listAllCamearsOfAllEpuisodesOfAllSvins();
      }

      @Override
      protected void setShownCameraIds( IStringList aCameraIds ) {
        svinFramesParams.setCameraIds( aCameraIds );
      }

      @Override
      protected IFrame getSelectedFrame() {
        return selectedItem();
      }
    } );
    toolbar.setActionMenu( AID_ANIMATION_KINDABLE_MENU,
        new AnimationKindDropDownMenuCreator( svinFramesParams, tsContext() ) );
    toolbar.setActionMenu( AID_SEC_STEPPABLE_ZOOM_ORIGINAL,
        new SecondsSteppableDropDownMenuCreator( svinFramesParams, tsContext(), IS_24X24 ) );
    toolbar.setActionMenu( AID_FRAMES_PER_SVIN_MENU,
        new FramesPerSvinDropDownMenuCreator( svinFramesParams, tsContext() ) );
    svinSeq.genericChangeEventer().addListener( s -> whenSvinSeqChanged() );
    svinFramesParams.genericChangeEventer().addListener( s -> whenSvinFramesStrategyChanged() );
    fgViewer.addTsSelectionListener( ( src, sel ) -> whenSelectedFrameChanges( sel ) );
    fgViewer.addTsDoubleClickListener( ( src, sel ) -> handleAction( ACTID_PLAY ) );
    fgViewer.addTsSelectionListener( selectionChangeEventHelper );
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    IFrame sel = selectedItem();
    switch( aActionId ) {
      case ACTID_WORK_WITH_FRAMES: {
        if( sel != null ) {
          DialogWorkWithFrames.open( tsContext(), sel );
        }
        break;
      }
      case ACTID_CAM_ID_MENU: {
        svinFramesParams.setCameraIds( listAllCamearsOfAllEpuisodesOfAllSvins() );
        break;
      }
      case ACTID_PLAY: {
        if( sel != null ) {
          Svin svin = getFrameSvin( sel );
          psxService().playEpisodeVideo( svin );
        }
        break;
      }
      case AID_SEC_STEPPABLE_ZOOM_ORIGINAL: {
        svinFramesParams.setDefaultTimeStep();
        break;
      }
      case AID_ANIMATION_KINDABLE_MENU: {
        svinFramesParams.setDefaultAnimationKind();
        break;
      }
      case AID_THUMB_SIZEABLE_ZOOM_MENU: {
        fgViewer.thumbSizeManager().setDefaultThumbSize();
        break;
      }
      case ACTID_GIF_TEST: {
        gifMenuCreator.handleAction( aActionId );
        break;
      }
      case AID_FRAMES_PER_SVIN_MENU: {
        EFramesPerSvin toSet;
        switch( svinFramesParams.framesPerSvin() ) {
          case SELECTED: {
            toSet = EFramesPerSvin.FORCE_ONE;
            break;
          }
          case AT_LEAST_ONE: {
            toSet = EFramesPerSvin.SELECTED;
            break;
          }
          case ONE_NO_MORE: {
            toSet = EFramesPerSvin.SELECTED;
            break;
          }
          case FORCE_ONE: {
            toSet = EFramesPerSvin.SELECTED;
            break;
          }
          default:
            throw new TsNotAllEnumsUsedRtException( svinFramesParams.framesPerSvin().id() );
        }
        svinFramesParams.setFramesPerSvin( toSet );
        break;
      }
      case ACTID_COPY_FRAME: {
        if( sel != null ) {
          psxService().copyFrameImage( sel );
        }
        break;
      }
      case ACTID_RUN_KDENLIVE: {
        if( sel != null ) {
          psxService().runKdenliveFor( sel );
        }
        break;
      }
      default:
        TsDialogUtils.warn( getShell(), aActionId );
    }
    updateActionsState();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void updateActionsState() {
    IFrame sel = selectedItem();
    boolean isSel = sel != null;
    toolbar.setActionEnabled( ACTID_WORK_WITH_FRAMES, isSel );
    toolbar.setActionEnabled( ACTID_PLAY, isSel );
    toolbar.setActionEnabled( ACTID_GIF_TEST, isSel );
    toolbar.setActionEnabled( ACTID_COPY_FRAME, isSel );
    toolbar.setActionEnabled( ACTID_RUN_KDENLIVE, isSel );
  }

  private IStringList listAllCamearsOfAllEpuisodesOfAllSvins() {
    IStringListEdit llCamIds = new StringArrayList();
    for( String epId : svinSeq.listEpisodeIds() ) {
      IStringList epCamIds = unitSourceVideos().episodeSourceVideos( epId ).keys();
      for( String camId : epCamIds ) {
        if( !llCamIds.hasElem( epId ) ) {
          llCamIds.add( camId );
        }
      }
    }
    return llCamIds;
  }

  private void recrateUnfilteredFramesListAndSetToFgViewer() {
    // optimize
    int totalDuration = 0;
    for( Svin s : svinSeq.svins() ) {
      totalDuration += s.interval().duration();
    }
    int order = estimateOrder( 5 * totalDuration );
    // SVINs -> frames
    SvinFramesSelector sfs = new SvinFramesSelector( tsContext() );
    IListBasicEdit<IFrame> ll = new SortedElemLinkedBundleList<>( getListInitialCapacity( order ), true );
    for( Svin s : svinSeq.svins() ) {
      ll.addAll( sfs.selectFrames( s, svinFramesParams ) );
    }
    // display
    fgViewer.setFrames( ll );
  }

  private void whenSvinFramesStrategyChanged() {
    recrateUnfilteredFramesListAndSetToFgViewer();
  }

  private void whenSvinSeqChanged() {
    recrateUnfilteredFramesListAndSetToFgViewer();
  }

  private void whenSelectedFrameChanges( IFrame aSelectedItem ) {
    if( aSelectedItem != null ) {
      toolbar.setActionMenu( ACTID_PLAY, new AbstractPlayMenuCreator( tsContext() ) {

        @Override
        protected Svin getPlayableSvin() {
          return getFrameSvin( aSelectedItem );
        }
      } );
      updateActionsState();
    }
  }

  /**
   * Return SVIN -5 + 30 sec from the specified frame.
   *
   * @param aFrame {@link IFrame} - the frame or <code>null</code>
   * @return {@link Svin} - the svin or null
   */
  private static Svin getFrameSvin( IFrame aFrame ) {
    if( aFrame == null ) {
      return null;
    }
    int startSec = aFrame.frameNo() / FPS - 5;
    if( startSec < 0 ) {
      startSec = 0;
    }
    Secint in = new Secint( startSec, startSec + 30 );
    return new Svin( aFrame.episodeId(), aFrame.cameraId(), in );
  }

  // ------------------------------------------------------------------------------------
  // TsStdEventsProducerPanel
  //

  @Override
  public IFrame selectedItem() {
    return fgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    fgViewer.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeChangeCapable
  //

  @Override
  public IThumbSizeableEx thumbSizeManager() {
    return fgViewer.thumbSizeManager();
  }

  // ------------------------------------------------------------------------------------
  // ISvinsFramesViewer
  //

  @Override
  public ISvinSeqEdit svinSeq() {
    return svinSeq;
  }

  @Override
  public ISvinFramesParams svinFramesParams() {
    return svinFramesParams;
  }

  @Override
  public int getDisplayedInfoFlags() {
    return fgViewer.getDisplayedInfoFlags();
  }

  @Override
  public void setDisplayedInfoFlags( int aPgvLfFlags ) {
    fgViewer.setDisplayedInfoFlags( aPgvLfFlags );
  }

  @Override
  public TsComposite getControl() {
    return this;
  }

}
