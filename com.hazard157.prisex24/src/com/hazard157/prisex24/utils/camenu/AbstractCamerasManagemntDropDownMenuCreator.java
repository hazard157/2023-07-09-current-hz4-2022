package com.hazard157.prisex24.utils.camenu;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Creates shown cameras management drop-down menu for the action {@link IPrisex24CoreConstants#ACDEF_CAM_ID_MENU}.
 *
 * @author hazard157
 */
public abstract class AbstractCamerasManagemntDropDownMenuCreator
    extends AbstractMenuCreator
    implements IPsxGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   * <p>
   * The following actions must be handled by the specified handler:
   * <ul>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_CREATE} - create GIF at selected frame;</li>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_RECREATE_ALL} - recreate all GIFs in the speicifed context;</li>
   * <li>{@link IPrisex24CoreConstants#ACDEF_GIF_REMOVE} - remove specified GIF. Called only if the selected frame is an
   * animated GIF.</li>
   * </ul>
   *
   * @param aContext - {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractCamerasManagemntDropDownMenuCreator( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private EThumbSize readThumbSizeFromAppSettings() {
    return APPREF_THUMB_SIZE_IN_MENUS.getValue( prefBundle( PBID_PSX24_COMMON ).prefs() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // AbstractMenuCreator
  //

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    IFrame camsFrame = getSelectedFrame();
    if( camsFrame == null ) {
      return false;
    }
    IStringMap<ISourceVideo> svs = unitSourceVideos().episodeSourceVideos( camsFrame.episodeId() );
    IEpisode e = unitEpisodes().items().findByKey( camsFrame.episodeId() );
    if( e == null || svs.isEmpty() ) {
      return false;
    }
    EThumbSize thumbSize = readThumbSizeFromAppSettings();
    for( final String camId : svs.keys() ) {
      MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( camId );
      ISourceVideo sv = svs.getByKey( camId );
      TsImage mi = psxService().findThumb( sv.frame(), thumbSize );
      if( mi != null ) {
        mItem.setImage( mi.image() );
      }
      mItem.addSelectionListener( new SelectionListenerAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          setShownCameraIds( new SingleStringList( camId ) );
        }
      } );
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract void setShownCameraIds( IStringList aCameraIds );

  protected abstract IFrame getSelectedFrame();

}
