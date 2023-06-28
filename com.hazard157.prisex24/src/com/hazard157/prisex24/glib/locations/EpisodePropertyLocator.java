package com.hazard157.prisex24.glib.locations;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.e4.services.mwsloc.*;
import com.hazard157.common.e4.services.mwsloc.impl.*;
import com.hazard157.prisex24.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * {@link IMwsLocator} implementation: selects episode in episodes perspective.
 * <p>
 * TODO specify which property (story, planes, etc) to locate at what time position.
 *
 * @author hazard157
 */
public class EpisodePropertyLocator
    extends MwsAbstractLocator
    implements IPsxGuiContextable {

  /**
   * The locator ID.
   */
  public static final String LOCATOR_ID = TS_FULL_ID + ".MwsUipartLocator"; //$NON-NLS-1$

  private static final String OPID_EPISODE_ID = "episodeId"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public EpisodePropertyLocator( ITsGuiContext aContext ) {
    super( LOCATOR_ID, aContext );
  }

  // ------------------------------------------------------------------------------------
  // Location creation
  //

  /**
   * Creates the location to select the episode in episodes perspective.
   *
   * @param aEpisode {@link IEpisode} - the episode
   * @return {@link IMwsLocation} - location instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IMwsLocation ofEpisode( IEpisode aEpisode ) {
    TsNullArgumentRtException.checkNull( aEpisode );
    return ofEpisode( aEpisode.id() );
  }

  /**
   * Creates the location to select the episode in episodes perspective.
   *
   * @param aEpisodeId String - the episode ID
   * @return {@link IMwsLocation} - location instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException episode ID is syntactical invalid
   */
  public static IMwsLocation ofEpisode( String aEpisodeId ) {
    EPsxIncidentKind.EPISODE.idStrValidator().checkValid( aEpisodeId );
    MwsLocation loc = new MwsLocation( LOCATOR_ID );
    loc.args().params().setStr( OPID_EPISODE_ID, aEpisodeId );
    return loc;
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractLocator
  //

  @Override
  protected void doLocate( IMwsLocation aLocation ) {
    String episodeId = aLocation.args().params().getStr( OPID_EPISODE_ID );
    e4Helper().switchToPerspective( PERSPID_EPISODES, PARTID_EISODES_LIST );
    currentEpisodeService().setCurrent( unitEpisodes().items().getByKey( episodeId ) );
  }

  @Override
  protected boolean doCanLocate( IMwsLocation aLocation ) {
    String episodeId = aLocation.args().params().getStr( OPID_EPISODE_ID, null );
    if( episodeId == null ) {
      return false;
    }
    if( !unitEpisodes().items().hasKey( episodeId ) ) {
      return false;
    }
    return true;
  }

}
