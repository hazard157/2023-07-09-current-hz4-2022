package com.hazard157.psx24.timeline.main2.stripes;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Time scale display.
 *
 * @author hazard157
 */
public class ScaleStripe
    extends AbstractStripe {

  /**
   * Main ticks height.
   */
  public static final IDataDef OP_MAIN_TICK_H = DataDef.create( "mainTickH", INTEGER, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avInt( 10 ) //
  );

  /**
   * Main tick line and text color.
   */
  public static final IDataDef OP_MAIN_TICK_COLOR = DataDef.create( "mainTickColor", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, ETsColor.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK ) //
  );

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор (ИД-путь) полосы
   * @param aParams {@link IOptionSet} - начальные значения {@link #params()}
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public ScaleStripe( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  @Override
  public int doGetHeight() {
    // TODO ???
    return 30;
  }

  @Override
  public void doPaintFlow( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {
    aGc.setForeground( colorManager().getColor( (ETsColor)OP_MAIN_TICK_COLOR.getValue( params() ).asValobj() ) );
    for( int sec = 0; sec < owner().getDuration(); sec += owner().timelineStep().stepSecs() ) {
      if( sec < aStartSec ) {
        continue;
      }
      if( sec > aEndSec ) {
        break;
      }
      String text = HmsUtils.mmss( sec );
      Point tsz = aGc.textExtent( text );
      int x = (int)owner().xCoor( sec );
      int text_y = aArea.y2() - tsz.y - 1;
      aGc.drawLine( x, aArea.y1() + 1, x, text_y - 1 );
      // int text_x = x - tsz.x / 2;
      int text_x = x;
      aGc.drawText( text, text_x, text_y );
    }
    aGc.setForeground( colorManager().getColor( ETsColor.RED ) );
    aGc.drawRectangle( aArea.x1(), aArea.y1(), aArea.width() - 3, aArea.height() - 1 );
  }

  @Override
  public void doPaintTitle( GC aGc, TsRectangle aArea, int aStartSec, int aEndSec ) {
    String text = nmName();
    Point tsz = aGc.textExtent( text );
    int text_x = aArea.x1() + (aArea.width() - tsz.x) / 2;
    int text_y = aArea.y1() + (aArea.width() - tsz.y) / 2;
    aGc.drawText( text, text_x, text_y );
  }

}
