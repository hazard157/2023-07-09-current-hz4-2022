package com.hazard157.psx.proj3.sourcevids.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Хранитель объектов типа {@link SourceVideoInfo}.
 *
 * @author hazard157
 */
public class SourceVideoInfoKeeper
    extends AbstractEntityKeeper<SourceVideoInfo> {

  private static final String KW_INFO = "Info"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<SourceVideoInfo> KEEPER = new SourceVideoInfoKeeper();

  private SourceVideoInfoKeeper() {
    super( SourceVideoInfo.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, SourceVideoInfo aEntity ) {
    aSw.writeAsIs( KW_INFO );
    aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE, CHAR_SET_BEGIN );
    aSw.incNewLine();
    HmsUtils.writeMmSs( aSw, aEntity.duration() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.location() );
    aSw.writeSeparatorChar();
    aSw.writeQuotedString( aEntity.description() );
    aSw.writeSeparatorChar();
    Frame.KEEPER.write( aSw, aEntity.frame() );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected SourceVideoInfo doRead( IStrioReader aSr ) {
    aSr.ensureString( KW_INFO );
    aSr.ensureChar( CHAR_EQUAL );
    aSr.ensureChar( CHAR_SET_BEGIN );
    int duration = HmsUtils.readMmSs( aSr );
    aSr.ensureSeparatorChar();
    String location = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IFrame frame = Frame.KEEPER.read( aSr );
    aSr.ensureChar( CHAR_SET_END );
    return new SourceVideoInfo( duration, location, description, frame );
  }

}
