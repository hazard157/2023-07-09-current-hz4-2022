package com.hazard157.psx24.catnote.main.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.txtproj.lib.impl.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * {@link INbNotebook} implementation.
 *
 * @author hazard157
 */
public class NbNotebook
    extends AbstractProjDataUnit
    implements INbNotebook {

  private static final String KW_CATEGORIES = "Categories"; //$NON-NLS-1$
  private static final String KW_NOTES      = "Notes";      //$NON-NLS-1$

  private final IStriparManager<INbCategory> categoryManager = new StriparManager<>( NbCategory.CREATOR );
  private final IStriparManager<INbNote>     noteManager     = new StriparManager<>( NbNote.CREATOR );

  /**
   * Constructor.
   */
  public NbNotebook() {
    categoryManager.genericChangeEventer().addListener( genericChangeEventer );
    noteManager.genericChangeEventer().addListener( genericChangeEventer );
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjDataUnit
  //

  @Override
  protected void doWrite( IStrioWriter aSw ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.incNewLine();
    // categories
    StrioUtils.writeKeywordHeader( aSw, KW_CATEGORIES, false );
    categoryManager.write( aSw );
    aSw.writeEol();
    // notes
    StrioUtils.writeKeywordHeader( aSw, KW_NOTES, false );
    noteManager.write( aSw );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected void doRead( IStrioReader aSr ) {
    genericChangeEventer.pauseFiring();
    try {
      aSr.ensureChar( CHAR_SET_BEGIN );
      // categories
      StrioUtils.ensureKeywordHeader( aSr, KW_CATEGORIES );
      categoryManager.read( aSr );
      // notes
      StrioUtils.ensureKeywordHeader( aSr, KW_NOTES );
      noteManager.read( aSr );
      aSr.ensureChar( CHAR_SET_END );
    }
    finally {
      genericChangeEventer.resumeFiring( true );
    }
  }

  @Override
  protected void doClear() {
    genericChangeEventer.pauseFiring();
    try {
      categoryManager.clear();
      noteManager.clear();
    }
    finally {
      genericChangeEventer.resumeFiring( true );
    }
  }

  // ------------------------------------------------------------------------------------
  // INbNotebook
  //

  @Override
  public IStriparManager<INbNote> notes() {
    return noteManager;
  }

  @Override
  public IStriparManager<INbCategory> rootCategory() {
    return categoryManager;
  }

}
