package com.hazard157.prisex24.e4.uiparts.episodes;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * The base class of all views related to viewing/editing episode properties.
 *
 * @author hazard157
 */
public abstract class AbstractEpisodeUipart
    extends MwsAbstractPart {

  @Inject
  protected ICurrentEpisodeService currentEpisodeService;

  private final IGenericChangeListener episodeChangeListener = aSource -> {
    if( !isSelfChange() ) {
      doHandleEpisodeContentChange();
    }
  };

  private IEpisode episode      = null;
  private boolean  isSelfChange = false;

  @Override
  protected final void doInit( Composite aParent ) {
    currentEpisodeService.addCurrentEntityChangeListener( this::setEpisode );
    try {
      doCreatePartContent( aParent );
    }
    catch( Throwable ex ) {
      ex.printStackTrace();
    }
    setEpisode( currentEpisodeService.current() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает отображаемый эпизод.
   *
   * @return {@link IEpisode} - отображаемый эпизод или null
   */
  final public IEpisode episode() {
    return episode;
  }

  /**
   * Задает отображаемый эпизод.
   *
   * @param aEpisode {@link IEpisode} - отображаемый эпизод или null
   */
  final public void setEpisode( IEpisode aEpisode ) {
    if( episode != null ) {
      episode.eventer().removeListener( episodeChangeListener );
    }
    episode = aEpisode;
    if( episode != null ) {
      episode.eventer().addListener( episodeChangeListener );
    }
    doSetEpisode();
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Determines whether a change to the episode has occurred as a result of editing in this view.
   * <p>
   * If this flag is set, then the base class <b>NOT</b> calls {@link #doHandleEpisodeContentChange()}, expecting that
   * the child will display the changes in the view.
   *
   * @return boolean - the flag means that episode is being edited by this view
   */
  public boolean isSelfChange() {
    return isSelfChange;
  }

  /**
   * Sets {@link #isSelfChange()}.
   *
   * @param aValue boolean - this view editing flag
   */
  public void setSelfChange( boolean aValue ) {
    isSelfChange = aValue;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must create the UIpart content.
   *
   * @param aParent {@link Composite} - the parent
   */
  abstract protected void doCreatePartContent( Composite aParent );

  /**
   * Subclass must response to the current episode change.
   * <p>
   * Called as a result of the {@link ICurrentEpisodeService#current()} change.
   */
  abstract protected void doSetEpisode();

  /**
   * The subclass must update its content when the content of the current episode changes.
   */
  abstract protected void doHandleEpisodeContentChange();

}
