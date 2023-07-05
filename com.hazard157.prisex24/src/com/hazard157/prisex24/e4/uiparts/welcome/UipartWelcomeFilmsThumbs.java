package com.hazard157.prisex24.e4.uiparts.welcome;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.pgv.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.common.incub.opfil.*;
import com.hazard157.prisex24.e4.uiparts.*;

/**
 * View with episode thumbs.
 *
 * @author goga
 */
public class UipartWelcomeFilmsThumbs
    extends PsxAbstractUipart
    implements ITsSelectionProvider<IOptedFile> {

  private final ITsVisualsProvider<IOptedFile> visualsProvider = new ITsVisualsProvider<>() {

    @Override
    public String getName( IOptedFile aEntity ) {
      return aEntity != null ? TsFileUtils.extractBareFileName( aEntity.file().getName() ) : EMPTY_STRING;
    }

    @Override
    public String getDescription( IOptedFile aEntity ) {
      return aEntity != null ? aEntity.file().getAbsolutePath() : EMPTY_STRING;
    }

    @Override
    public TsImage getThumb( IOptedFile aEntity, EThumbSize aThumbSize ) {
      if( aEntity != null ) {
        File f = cofsFilms().getSummaryGif( aEntity );
        if( f != null ) {
          return imageManager().findThumb( f, aThumbSize );
        }
      }
      return null;
    }

  };

  IPicsGridViewer<IOptedFile> pgViewer;

  // ------------------------------------------------------------------------------------
  // AbstractPsx12Uipart
  //

  @Override
  protected void doInit( Composite aParent ) {
    IWelcomePerspectiveController controller = tsContext().get( IWelcomePerspectiveController.class );
    controller.setFilmsPart( this );
    pgViewer = new PicsGridViewer<>( aParent, tsContext() );
    pgViewer.setVisualsProvider( visualsProvider );
    pgViewer.addTsSelectionListener( ( src, sel ) -> whenFilmSelectionChanges( sel ) );
    pgViewer.addTsDoubleClickListener( ( src, sel ) -> whenFilmDoubleClicked( sel ) );
    //
    prefBundle( PBID_HZ_COMMON ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    prefBundle( PBID_WELCOME ).prefs().addCollectionChangeListener( ( s, o, i ) -> initViewerContent() );
    e4Helper().updateHandlersCanExecuteState();
    initViewerContent();
  }
  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void initViewerContent() {
    preloadImages();
    IOptedFile sel = pgViewer.selectedItem();
    EThumbSize thumbSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    boolean isForceStill = apprefValue( PBID_WELCOME, APPREF_WELCOME_IS_FORCE_STILL ).asBool();
    pgViewer.setFocreStill( isForceStill );
    pgViewer.setThumbSize( thumbSize );
    pgViewer.setItems( cofsFilms().listFilms( IOptionSet.NULL ) );
    pgViewer.setSelectedItem( sel );
  }

  private void preloadImages() {
    EThumbSize thumbSize = apprefValue( PBID_HZ_COMMON, APPREF_THUMB_SIZE_IN_GRIDS ).asValobj();
    for( IOptedFile optedFile : cofsFilms().listFilms( IOptionSet.NULL ) ) {
      File f = cofsFilms().getSummaryGif( optedFile );
      if( f != null ) {
        imageManager().findThumb( f, thumbSize );
      }
    }
  }

  private void whenFilmDoubleClicked( IOptedFile aSel ) {
    if( aSel != null ) {
      mps().playVideoFile( aSel.file() );
    }
  }

  private void whenFilmSelectionChanges( @SuppressWarnings( "unused" ) IOptedFile aSel ) {
    e4Helper().updateHandlersCanExecuteState();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IOptedFile> aListener ) {
    pgViewer.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IOptedFile> aListener ) {
    pgViewer.addTsSelectionListener( aListener );
  }

  @Override
  public IOptedFile selectedItem() {
    return pgViewer.selectedItem();
  }

  @Override
  public void setSelectedItem( IOptedFile aItem ) {
    pgViewer.setSelectedItem( aItem );
  }

}
