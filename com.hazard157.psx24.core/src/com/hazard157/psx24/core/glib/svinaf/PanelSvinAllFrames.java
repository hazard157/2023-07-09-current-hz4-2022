package com.hazard157.psx24.core.glib.svinaf;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.glib.pgviewer.impl.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx24.core.*;

/**
 * {@link IPanelSvinAllFrames} implementation.
 *
 * @author hazard157
 */
public class PanelSvinAllFrames
    extends AbstractGenericCollPanel<IFrame>
    implements IPanelSvinAllFrames, IPsxStdReferences {

  private final String     APREFID_THUMB_SIZE = APPRMID_THUMBSZ_FRAMES_IN_PANELS;
  private final EThumbSize DEFAULT_THUMB_SIZE = APPRM_THUMBSZ_FRAMES_IN_PANELS.defaultValue().asValobj();

  private final GenericChangeEventer thumbSizeEventer;

  private final IStringMapEdit<PicsGridViewer<IFrame>> camPgvs           = new StringMap<>();
  private final IListEdit<IFrame>                      allFramesOfSvin   = new ElemLinkedBundleList<>();
  private final IListEdit<IFrame>                      shownFrames       = new ElemLinkedBundleList<>();
  private final IStringMapEdit<IList<IFrame>>          shownFramesByCams = new StringMap<>();

  private SashForm   sfMain    = null;
  private Svin       svin      = null;
  private EThumbSize thumbSize = DEFAULT_THUMB_SIZE;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public PanelSvinAllFrames( ITsGuiContext aContext ) {
    super( aContext );
    thumbSizeEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Fill {@link #allFramesOfSvin} by all possible frmes.
   */
  void internalUpdateOnSvinChange() {

    // TODO PanelSvinAllFrames.doCreateControl()
  }

  /**
   * Fill {@link #shownFrames} from
   */
  void internalMakeItemsListFromSvin() {
    if( svin == null ) {
      shownFrames.clear();
      return;
    }

    // TODO PanelSvinAllFrames.doCreateControl()
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericCollPanel
  //

  @Override
  public boolean isViewer() {
    return false;
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    sfMain = new SashForm( aParent, SWT.HORIZONTAL );
    // TODO create toolbar

    // TODO Auto-generated method stub

    return sfMain;
  }

  @Override
  public IFrame selectedItem() {
    for( PicsGridViewer<IFrame> pgv : camPgvs ) {
      IFrame sel = pgv.selectedItem();
      if( sel != null ) {
        return sel;
      }
    }
    return null;
  }

  @Override
  public void setSelectedItem( IFrame aItem ) {
    for( PicsGridViewer<IFrame> pgv : camPgvs ) {
      pgv.setSelectedItem( null );
    }
    if( aItem != null ) {
      for( PicsGridViewer<IFrame> pgv : camPgvs ) {
        if( pgv.items().hasElem( aItem ) ) {
          pgv.setSelectedItem( aItem );
          break;
        }
      }
    }
  }

  @Override
  public IList<IFrame> items() {
    return shownFrames;
  }

  @Override
  public void refresh() {
    // TODO make items list from scratch

    internalMakeItemsListFromSvin();
    for( PicsGridViewer<IFrame> pgv : camPgvs ) {
      pgv.refresh();
    }
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return thumbSize;
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNull( aThumbSize );
    if( thumbSize != aThumbSize ) {
      thumbSize = aThumbSize;
      for( PicsGridViewer<IFrame> pgv : camPgvs ) {
        pgv.setThumbSize( aThumbSize );
        pgv.refresh();
      }
    }
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return DEFAULT_THUMB_SIZE;
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return thumbSizeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IPanelSvinAllFrames
  //

  @Override
  public Svin getSvin() {
    return svin;
  }

  @Override
  public void setSvin( Svin aSvin ) {
    if( !Objects.equals( svin, aSvin ) ) {
      svin = aSvin;
      internalUpdateOnSvinChange();
      refresh();
    }
  }

}
