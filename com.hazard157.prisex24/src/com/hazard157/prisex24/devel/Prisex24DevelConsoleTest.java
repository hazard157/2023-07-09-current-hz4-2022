package com.hazard157.prisex24.devel;

import static org.toxsoft.core.tslib.utils.TsTestUtils.*;

import java.sql.*;
import java.time.*;

/**
 * Console test for different development needs.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public class Prisex24DevelConsoleTest {

  public static void main( String[] args ) {
    pl( "Prisex24DevelConsoleTest started" );
    nl();
    checkTimes();

    nl();
    pl( "Finished." );
    nl();
  }

  static void checkTimes() {
    Timestamp ts = new Timestamp( System.currentTimeMillis() );
    pl( "  ts = %s", ts.toString() );

    long when = ts.getTime();
    Long date = Long.valueOf( when );
    pl( "  when = %tF %tT", date, date ); //$NON-NLS-1$

    LocalDateTime ldt = ts.toLocalDateTime();
    pl( "  ldt = %s", ldt.toString() );

    // ZoneId sysDefault = ZoneId.systemDefault();
    // pl( " ZoneId.systemDefault() = %s", sysDefault.toString() );
    //
    // Instant inst = Instant.now();
    // pl( " Instant.now() = %s", inst.toString() );
    //
    // LocalDateTime ldt = LocalDateTime.now( sysDefault );
    // pl( " LocalDateTime.now(sysDefault) = %s", ldt.toString() );
    //
    // Instant instant = ldt.toInstant( ZoneOffset.UTC );
    // pl( " Instanf from LocalDateTime.now(sysDefault) = %s", instant.toString() );
  }

}
