package com.hazard157.psx24.timeline.main2.stripes;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx24.timeline.main2.*;

/**
 * Базовый класс полосы, отображаемой на диаграмме эпизода.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public abstract class AbstractStripe
    extends StridableParameterized
    implements ITsGuiContextable {

  private static final int MIN_HEIGHT = 8;

  private IStripeOwner owner = null;

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор (ИД-путь) полосы
   * @param aParams {@link IOptionSet} - начальные значения {@link #params()}
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public AbstractStripe( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  final public void initialize( IStripeOwner aOwner ) {
    TsNullArgumentRtException.checkNull( aOwner );
    TsIllegalStateRtException.checkNoNull( owner );
    owner = aOwner;
    doInitialize();
  }

  final void informOnEpisodeChanged( IEpisode aEpisode ) {
    doOnEpisodeChange( aEpisode );
  }

  final public int getHeight() {
    try {
      int height = doGetHeight();
      TsInternalErrorRtException.checkTrue( height < MIN_HEIGHT );
      return height;
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    return MIN_HEIGHT;
  }

  final public void paint( GC aGc, TsRectangle aFlowArea, TsRectangle aTitleArea, int aStartSec, int aEndSec ) {
    doPaintFlow( aGc, aFlowArea, aStartSec, aEndSec );
    doPaintTitle( aGc, aTitleArea, aStartSec, aEndSec );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return owner.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

  /**
   * Returns painter component API.
   *
   * @return {@link IStripeOwner} - painter component API
   */
  public IStripeOwner owner() {
    TsIllegalStateRtException.checkNull( owner );
    return owner;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected abstract int doGetHeight();

  protected abstract void doPaintFlow( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec );

  protected abstract void doPaintTitle( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec );

  protected void doInitialize() {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doOnEpisodeChange( IEpisode aEpisode ) {
    // nop
  }

}
