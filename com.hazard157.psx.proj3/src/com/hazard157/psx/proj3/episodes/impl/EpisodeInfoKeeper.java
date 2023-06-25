package com.hazard157.psx.proj3.episodes.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Хранитель объектов типа {@link EpisodeInfo}.
 *
 * @author hazard157
 */
public class EpisodeInfoKeeper
    extends AbstractEntityKeeper<EpisodeInfo> {

  private static final String KW_INFO = "Info"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<EpisodeInfo> KEEPER = new EpisodeInfoKeeper();

  private EpisodeInfoKeeper() {
    super( EpisodeInfo.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, EpisodeInfo aEntity ) {
    aSw.writeAsIs( KW_INFO );
    aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE, CHAR_SET_BEGIN );
    aSw.incNewLine();
    aSw.writeDateTime( aEntity.when() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.description() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    aSw.writeQuotedString( aEntity.place() );
    aSw.writeSeparatorChar();
    HmsUtils.writeMmSs( aSw, aEntity.duration() );
    aSw.writeSeparatorChar();
    Secint.KEEPER.write( aSw, aEntity.actionInterval() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.defaultTrailerId() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.notes() );
    aSw.writeSeparatorChar();
    FrameKeeper.KEEPER.write( aSw, aEntity.frame() );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected EpisodeInfo doRead( IStrioReader aSr ) {
    aSr.ensureString( KW_INFO );
    aSr.ensureChar( CHAR_EQUAL );
    aSr.ensureChar( CHAR_SET_BEGIN );
    long when = aSr.readTimestamp();
    aSr.ensureSeparatorChar();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String place = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    int duration = HmsUtils.readMmSs( aSr );
    aSr.ensureSeparatorChar();
    Secint actionInterval = Secint.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    String defTrailerId = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String todoNotes = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IFrame frame = FrameKeeper.KEEPER.read( aSr );
    aSr.ensureChar( CHAR_SET_END );
    return new EpisodeInfo( when, name, description, place, duration, actionInterval, defTrailerId, todoNotes, frame );
  }

}
