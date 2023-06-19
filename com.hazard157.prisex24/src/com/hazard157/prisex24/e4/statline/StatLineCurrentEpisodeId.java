package com.hazard157.prisex24.e4.statline;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.e4.statline.IPsxResources.*;

import javax.annotation.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Status line tool control displays current episode ID.
 *
 * @author hazard157
 */
public class StatLineCurrentEpisodeId
    implements ITsMouseInputListener, IPsxGuiContextable {

  ITsGuiContext tsContext;

  TsUserInputEventsBinder mouseHelper;

  Label label;

  @SuppressWarnings( "unused" )
  @PostConstruct
  void init( Composite aParent, IEclipseContext aContext ) {
    tsContext = new TsGuiContext( aContext );
    mouseHelper = new TsUserInputEventsBinder( this );
    currentEpisodeService().addCurrentEntityChangeListener( this::updateOnCurrentEpisodeChange );
    label = new Label( aParent, SWT.BORDER );
    mouseHelper.bindToControl( label,
        TsUserInputEventsBinder.BIND_MOUSE_BUTTONS | TsUserInputEventsBinder.BIND_MOUSE_WHEEL );
    mouseHelper.addTsMouseInputListener( this );
    updateOnCurrentEpisodeChange( currentEpisodeService().current() );
    new CurrentEpisodeInfoTooltip( label, tsContext, () -> currentEpisodeService().current() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //
  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateOnCurrentEpisodeChange( IEpisode aCurrent ) {
    if( aCurrent == null ) {
      label.setText( STR_NO_EPISODE );
    }
    else {
      label.setText( aCurrent.id() );
    }
    label.getParent().layout( true, true );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseWheel( Object aSource, int aState, ITsPoint aCoors, Control aWidget, int aScrollLines ) {
    ETsCollMove move = ETsCollMove.NONE;
    if( aScrollLines > 0 ) {
      move = ETsCollMove.PREV;
    }
    if( aScrollLines < 0 ) {
      move = ETsCollMove.NEXT;
    }
    IEpisode e = move.findElemAt( currentEpisodeService().current(), unitEpisodes().items(), 5, false );
    currentEpisodeService().setCurrent( e );
    return true;
  }

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    e4Helper().execCmd( CMDID_GOTO_EPISODE_SELECT );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
