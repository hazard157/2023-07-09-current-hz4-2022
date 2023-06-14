package com.hazard157.prisex24.e4.services.currep;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * {@link ICurrentEpisodeService} implementation.
 *
 * @author hazard157
 */
public class CurrentEpisodeService
    extends CurrentEntityService<IEpisode>
    implements ICurrentEpisodeService {

  private final IUnitEpisodes unitEpisodes;

  /**
   * Конструктор.
   *
   * @param aAppContext {@link IEclipseContext} - контекст приложения
   * @throws TsNullArgumentRtException аргумент = null
   */
  public CurrentEpisodeService( IEclipseContext aAppContext ) {
    super( aAppContext );
    unitEpisodes = appContext().get( IUnitEpisodes.class );
  }

  @Override
  public void select( ETsCollMove aDirection ) {
    TsNullArgumentRtException.checkNull( aDirection );
    IEpisode toSel = aDirection.findElemAt( current(), unitEpisodes.items(), 5, true );
    setCurrent( toSel );
  }

}
