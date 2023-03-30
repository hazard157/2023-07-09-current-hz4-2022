package com.hazard157.psx.proj3.episodes.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.episodes.*;

/**
 * Хранитель объектов типа {@link IEpisode}.
 *
 * @author hazard157
 */
public class EpisodeKeeper
    extends AbstractEntityKeeper<IEpisode> {

  /**
   * Хранение {@link IEpisode} в текстовом представлении.
   */
  public static final IEntityKeeper<IEpisode> KEEPER = new EpisodeKeeper();

  private static final String KW_STORY       = "Story";      //$NON-NLS-1$
  private static final String KW_NOTE_LINE   = "NoteLine";   //$NON-NLS-1$
  private static final String KW_TAG_LINE    = "TagLine";    //$NON-NLS-1$
  private static final String KW_PLANES_LINE = "PlanesLine"; //$NON-NLS-1$
  private static final String KW_TODOS       = "Todos";      //$NON-NLS-1$

  private EpisodeKeeper() {
    super( IEpisode.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  private static void writeKw( IStrioWriter aSw, String aKeyword ) {
    aSw.writeAsIs( aKeyword );
    aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE );
  }

  private static void ensureKw( IStrioReader aSr, String aKeyword ) {
    aSr.ensureString( aKeyword );
    aSr.ensureChar( CHAR_EQUAL );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IEpisode aEntity ) {
    aSw.incNewLine();
    // id()
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    // info();
    EpisodeInfoKeeper.KEEPER.write( aSw, aEntity.info() );
    aSw.writeEol();
    // story()
    writeKw( aSw, KW_STORY );
    aEntity.story().write( aSw );
    aSw.writeEol();
    // noteLine()
    writeKw( aSw, KW_NOTE_LINE );
    aEntity.noteLine().write( aSw );
    aSw.writeEol();
    // tagLine()
    writeKw( aSw, KW_TAG_LINE );
    aEntity.tagLine().write( aSw );
    aSw.writeEol();
    // planesLine()
    writeKw( aSw, KW_PLANES_LINE );
    aEntity.planesLine().write( aSw );
    aSw.writeEol();
    // todos
    writeKw( aSw, KW_TODOS );
    aEntity.verifyCfg().write( aSw );
    aSw.decNewLine();
  }

  @Override
  protected IEpisode doRead( IStrioReader aSr ) {
    String id = aSr.readIdName();
    aSr.ensureSeparatorChar();
    EpisodeInfo info = EpisodeInfoKeeper.KEEPER.read( aSr );
    Episode e = new Episode( id, info );
    ensureKw( aSr, KW_STORY );
    e.story().read( aSr );
    ensureKw( aSr, KW_NOTE_LINE );
    e.noteLine().read( aSr );
    ensureKw( aSr, KW_TAG_LINE );
    e.tagLine().read( aSr );
    ensureKw( aSr, KW_PLANES_LINE );
    e.planesLine().read( aSr );
    ensureKw( aSr, KW_TODOS );
    e.verifyCfg().read( aSr );
    return e;
  }

}
