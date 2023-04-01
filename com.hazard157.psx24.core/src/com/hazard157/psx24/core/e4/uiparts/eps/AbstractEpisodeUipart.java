package com.hazard157.psx24.core.e4.uiparts.eps;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.core.e4.services.currep.*;

/**
 * Базовый класс всех вью, связанных с просмотром/редактированием свойств эпизода.
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
  // Методы для наследников
  //

  /**
   * Определяет, возникло ли изменение в эпизоде в результате редактирования в этом вью.
   * <p>
   * Если этот признак установлен, то базовый <b>НЕ</b> вызывает {@link #doHandleEpisodeContentChange()}, рассчитывая,
   * что изменения во вью отобразит наследник.
   *
   * @return boolean - признак редактирования эпизода в этом вью
   */
  public boolean isSelfChange() {
    return isSelfChange;
  }

  /**
   * Задает значение {@link #isSelfChange()}.
   *
   * @param aValue boolean - признак редактирования эпизода в этом вью
   */
  public void setSelfChange( boolean aValue ) {
    isSelfChange = aValue;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения
  //

  /**
   * Наследник должен создать содержимое вью.
   *
   * @param aParent {@link Composite} - родительская панель
   */
  abstract protected void doCreatePartContent( Composite aParent );

  /**
   * Наследник должен отработать сменту текущего эпизода.
   * <p>
   * Вызывается при изменении текущего эпизода в слежбе {@link ICurrentEpisodeService}.
   */
  abstract protected void doSetEpisode();

  /**
   * Наследник должен обновить свое содержимое при изменении содержимого текущего эпизода.
   */
  abstract protected void doHandleEpisodeContentChange();

}
