package com.hazard157.psx24.timeline.main2.stripes;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Range selection stripe.
 *
 * @author hazard157
 */
public class RangeScale
    extends AbstractStripe {

  private int startSec = 0;
  private int endSec   = 0;

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор (ИД-путь) полосы
   * @param aParams {@link IOptionSet} - начальные значения {@link #params()}
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public RangeScale( String aId, IOptionSet aParams ) {
    super( aId, aParams );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractStripe
  //

  @Override
  protected void doInitialize() {
    startSec = 0;
    endSec = owner().getDuration() - 1;
  }

  @Override
  protected int doGetHeight() {
    // TODO ???
    return 20;
  }

  @Override
  protected void doPaintFlow( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {

    drawHandle( aGc, aArea, owner().xCoor( aStartSec ) );
    drawHandle( aGc, aArea, owner().xCoor( (aEndSec - aStartSec) / 2 ) );
    drawHandle( aGc, aArea, owner().xCoor( aEndSec ) );

    // TODO Auto-generated method stub

  }

  @Override
  protected void doPaintTitle( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {
    // TODO Auto-generated method stub

  }

  static int[] handleCoor = new int[6];

  private void drawHandle( GC aGc, TsRectangle aArea, double aX ) {
    int x = (int)aX;
    Color bkColor = aGc.getBackground();
    aGc.setBackground( colorManager().getColor( ETsColor.GREEN ) );

    handleCoor[0] = x;
    handleCoor[1] = aArea.y1() + 1;
    handleCoor[2] = x + 10;
    handleCoor[3] = aArea.y1() + 15;
    handleCoor[4] = x - 10;
    handleCoor[5] = aArea.y1() + 15;
    aGc.fillPolygon( handleCoor );

    aGc.setForeground( colorManager().getColor( ETsColor.DARK_GRAY ) );
    aGc.drawPolygon( handleCoor );

    aGc.setBackground( bkColor );

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @SuppressWarnings( "javadoc" )
  public Secint getRange() {
    return new Secint( startSec, endSec );
  }

}
