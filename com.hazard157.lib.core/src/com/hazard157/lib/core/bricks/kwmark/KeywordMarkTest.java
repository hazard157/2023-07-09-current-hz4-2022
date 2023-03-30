package com.hazard157.lib.core.bricks.kwmark;

import static org.toxsoft.core.tslib.utils.TsTestUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.bricks.kwmark.manager.*;

/**
 * Utility and helper methods.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public class KeywordMarkTest {

  // static final File ROOT_DIR = new File( "/home/clouds/hazard157.ru/hdero/download/" );

  static final File KWMAN_FILE = new File( "/home/goga/keywords-test.txt" );

  static IGenericChangeListener changeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      pl( "===> change event!" );
    }

  };

  private static void checkKeywordManager() {
    IKeywordManager kwMan = new KeywordManager();
    kwMan.genericChangeEventer().addListener( changeListener );
    //
    if( TsFileUtils.isFileReadable( KWMAN_FILE ) ) {
      StrioUtils.readKeepableEntity( KWMAN_FILE, kwMan );
      pl( "Read from file %s", KWMAN_FILE.getAbsolutePath() );
    }
    //
    kwMan.define( "pose.riding" );
    kwMan.define( "action.fuck" );
    kwMan.define( "action.play" );
    kwMan.define( "pose.doggy" );
    //
    StrioUtils.writeKeepableEntity( KWMAN_FILE, kwMan );
    pl( "Write to file %s", KWMAN_FILE.getAbsolutePath() );
  }

  public static void main( String[] aArgs ) {
    pl( "Keyword marks test." );
    checkKeywordManager();
    pl( "Keyword marks test." );
  }

}
