package com.hazard157.lib.core.incub;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Counts stages running times of some process.
 *
 * @author hazard157
 */
public class RunTimeCounter {

  /**
   * Finished stage item.
   *
   * @author hazard157
   * @param time long - finished stage starting time (millisecond after epoch)
   * @param name String - the stage name
   * @param delta long - stage duration in milliseconds
   */
  public static record Item ( long time, String name, long delta ) {

    @Override
    public String toString() {
      //
      Long date = Long.valueOf( time );
      String strTime = String.format( "%tF %tT", date, date ); //$NON-NLS-1$
      //
      int u = (int)(delta % 1000);
      int secs = (int)(delta / 1000);
      int m = secs / 60;
      int s = secs % 60;
      String strDelta = String.format( "%02d:%02d.%03d", //$NON-NLS-1$
          Integer.valueOf( m ), Integer.valueOf( s ), Integer.valueOf( u ) );
      //
      return String.format( "%s  %s  - %s", strTime, strDelta, name );
    }
  }

  private final IListEdit<Item> items = new ElemArrayList<>( 100 );

  /**
   * Constructor.
   */
  public RunTimeCounter() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void start( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    Item item = new Item( System.currentTimeMillis(), aName, 0 );
    items.clear();
    items.add( item );
  }

  public void stageEnded( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    TsIllegalStateRtException.checkTrue( items.isEmpty() );
    long t = System.currentTimeMillis();
    long d = t - items.last().time;
    items.add( new Item( t, aName, d ) );
  }

  public IList<Item> items() {
    return items;
  }

  public String makeMultiLineMessage() {
    StringBuilder sb = new StringBuilder();
    for( Item item : items ) {
      sb.append( item.toString() );
      sb.append( '\n' );
    }
    return sb.toString();
  }

}
