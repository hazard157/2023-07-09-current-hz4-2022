package com.hazard157.prisex24.valeds.frames;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.glib.frview.*;
import com.hazard157.prisex24.glib.frview.impl.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * {@link IFrame} editor - selects frame for the specified episode.
 *
 * @author hazard157
 */
public class ValedFrameEditor
    extends AbstractValedControl<IFrame, Control> {

  private final IEpisodeFrameSelector framesViewer;

  /**
   * Constructor for subclasses.
   *
   * @param aTsContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedFrameEditor( ITsGuiContext aTsContext ) {
    super( aTsContext );
    params().setInt( IValedControlConstants.OPID_VERTICAL_SPAN, 15 );
    framesViewer = new EpisodeFramesSelector( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return framesViewer.createControl( aParent );
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected IFrame doGetUnvalidatedValue() {
    IFrame sel = framesViewer.selectedItem();
    if( sel != null ) {
      return sel;
    }
    return IFrame.NONE;
  }

  @Override
  protected void doSetUnvalidatedValue( IFrame aValue ) {
    framesViewer.setSelectedItem( aValue );
  }

  @Override
  protected void doClearValue() {
    framesViewer.setSelectedItem( null );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the displayed frames episode.
   *
   * @return String - the episode ID or <code>null</code>
   */
  public String getEpisodeId() {
    return framesViewer.getEpisodeId();
  }

  /**
   * Sets the displayed frames episode.
   *
   * @param aEpisodeId String - the episode ID or <code>null</code>
   */
  public void setEpisodeId( String aEpisodeId ) {
    framesViewer.setEpisodeId( aEpisodeId );
  }

}
