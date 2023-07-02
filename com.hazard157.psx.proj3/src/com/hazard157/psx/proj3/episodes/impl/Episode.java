package com.hazard157.psx.proj3.episodes.impl;

import java.time.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.proplines.impl.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * Эпизод.
 *
 * @author hazard157
 */
public class Episode
    extends AbstractSinentity<EpisodeInfo>
    implements IEpisode {

  private final IGenericChangeListener selfListener = aSource -> this.ssCache = null;

  private final IStory        story;
  private final ITagLine      tagLine    = new TagLine();
  private final INoteLine     noteLine   = new NoteLine();
  private final IMlPlaneGuide planesLine = new MlPlaneGuide();
  private final EpVerifyCfg   verifyCfg;

  private LocalDate date = null;

  /**
   * Кеш срезов, <code>null</code> означает, что кеш сброшен.
   */
  private IList<SecondSlice> ssCache = null;

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор эпизода согласно {@link EpisodeUtils#EPISODE_ID_VALIDATOR}
   * @param aInfo {@link EpisodeInfo} - инофрмация об эпизоде
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException неверный идентификатор эпизода
   */
  public Episode( String aId, EpisodeInfo aInfo ) {
    super( EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aId ), aInfo );
    story = new Story( this, new Secint( 0, aInfo.duration() - 1 ) );
    verifyCfg = new EpVerifyCfg();
    tagLine.setDuration( info().duration() );
    noteLine.setDuration( info().duration() );
    planesLine.setDuration( info().duration() );
    story.genericChangeEventer().addListener( eventer );
    tagLine.genericChangeEventer().addListener( eventer );
    noteLine.genericChangeEventer().addListener( eventer );
    planesLine.genericChangeEventer().addListener( eventer );
    verifyCfg.genericChangeEventer().addListener( eventer );
    eventer.addListener( selfListener );
  }

  @Override
  protected void onInfoChanged( EpisodeInfo aNewInfo, EpisodeInfo aOldInfo ) {
    if( aNewInfo.duration() != aOldInfo.duration() ) {
      story.setSecint( new Secint( 0, aNewInfo.duration() - 1 ) );
      tagLine.setDuration( info().duration() );
      noteLine.setDuration( info().duration() );
      planesLine.setDuration( info().duration() );
    }
    super.onInfoChanged( aNewInfo, aOldInfo );
  }

  // ------------------------------------------------------------------------------------
  // IPsxIncident
  //

  @Override
  final public EPsxIncidentKind incidentKind() {
    return EPsxIncidentKind.EPISODE;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String nmName() {
    return info().name();
  }

  @Override
  public String description() {
    return info().description();
  }

  // ------------------------------------------------------------------------------------
  // IEpisodeIdable
  //

  @Override
  public String episodeId() {
    return id();
  }

  // ------------------------------------------------------------------------------------
  // IFrameable
  //

  @Override
  public IFrame frame() {
    return info().frame();
  }

  // ------------------------------------------------------------------------------------
  // IEpisodeBase
  //

  @Override
  public long when() {
    return info().when();
  }

  @Override
  public LocalDate incidentDate() {
    if( date == null ) {
      date = EpisodeUtils.id2date( id() );
    }
    return date;
  }

  /**
   * Возвращает сюжет эпизода.
   *
   * @return {@link IStory} - сюжет эпизода
   */
  @Override
  public IStory story() {
    return story;
  }

  @Override
  public INoteLine noteLine() {
    return noteLine;
  }

  @Override
  public int duration() {
    return info().duration();
  }

  @Override
  public ITagLine tagLine() {
    return tagLine;
  }

  @Override
  public IMlPlaneGuide planesLine() {
    return planesLine;
  }

  /**
   * Возвращает иллюстрации к сюжету эпизода.
   * <p>
   * Иллюстрациями служат кадры, иллюстрирующие сцены первого уровня (то есть, дети сюжета -
   * {@link IStory#childScenes()}), а в случае запроса детальных иллюстрации (Detailed = <code>true</code>), кадры к
   * сценам, дочерным к дочкам сюжета.
   *
   * @param aDetailed boolean - выдать более детальные (больше) иллюстрации
   * @return {@link IList}&lt;{@link IFrame}&gt; - отсортированный список иллюстрации
   */
  @Override
  public IList<IFrame> getIllustrations( boolean aDetailed ) {
    IListEdit<IFrame> frames = new ElemLinkedBundleList<>();
    for( IScene s1 : story.childScenes() ) {
      if( aDetailed ) {
        for( IScene s2 : s1.childScenes() ) {
          frames.add( s2.frame() );
        }
      }
      else {
        frames.add( s1.frame() );
      }
    }
    return frames;
  }

  @Override
  public IEpVerifyCfg verifyCfg() {
    return verifyCfg;
  }

  @Override
  public IList<SecondSlice> slices() {
    if( ssCache == null ) {
      IListEdit<SecondSlice> ll = new ElemArrayList<>( this.duration() );
      for( int i = 0; i < this.duration(); i++ ) {
        ll.add( new SecondSlice( this, i ) );
      }
      ssCache = ll;
    }
    return ssCache;
  }

}
