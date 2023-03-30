package com.hazard157.psx24.core.e4.statline;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.e4.statline.IPsxResources.*;

import javax.annotation.*;
import javax.inject.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.currep.*;

/**
 * Status line tool control displayes current episode ID.
 *
 * @author hazard157
 */
public class StatLineCurrentEpisodeId
    implements ITsMouseInputListener {

  // TODO on hower show current episode tooltip

  @Inject
  ICurrentEpisodeService currentEpisodeService;

  @Inject
  IUnitEpisodes unitEpisodes;

  ITsE4Helper e4Helper;

  TsUserInputEventsBinder mouseHelper;

  Label label;

  @PostConstruct
  void init( Composite aParent, ITsE4Helper aE4Helper ) {
    mouseHelper = new TsUserInputEventsBinder( this );
    e4Helper = aE4Helper;
    currentEpisodeService.addCurrentEntityChangeListener( this::updateOnCurrentEpisodeChange );
    label = new Label( aParent, SWT.BORDER );
    mouseHelper.bindToControl( label,
        TsUserInputEventsBinder.BIND_MOUSE_BUTTONS | TsUserInputEventsBinder.BIND_MOUSE_WHEEL );
    mouseHelper.addTsMouseInputListener( this );
    updateOnCurrentEpisodeChange( currentEpisodeService.current() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //
  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateOnCurrentEpisodeChange( IEpisode aCurrent ) {
    if( aCurrent == null ) {
      label.setText( STR_T_NO_EPISODE );
      label.setToolTipText( STR_P_NO_EPISODE );
      return;
    }
    label.setText( aCurrent.id() );
    String s = String.format( "%s\n%s", aCurrent.nmName(), aCurrent.description() ); //$NON-NLS-1$
    label.setToolTipText( s );
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
    IEpisode e = move.findElemAt( currentEpisodeService.current(), unitEpisodes.items(), 5, false );
    currentEpisodeService.setCurrent( e );
    return true;
  }

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    e4Helper.execCmd( CMDID_GOTO_EPISODE_SELECT );
    return true;
  }

}
