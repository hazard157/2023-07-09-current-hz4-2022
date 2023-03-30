package com.hazard157.psx.proj3.pleps.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Хранитель объектов типа {@link PlepInfo}.
 *
 * @author hazard157
 */
public class PlepInfoKeeper
    extends AbstractEntityKeeper<PlepInfo> {

  private static final String KW_INFO = "Info"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<PlepInfo> KEEPER = new PlepInfoKeeper();

  private PlepInfoKeeper() {
    super( PlepInfo.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, PlepInfo aEntity ) {
    aSw.writeAsIs( KW_INFO );
    aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE, CHAR_SET_BEGIN );
    aSw.writeSpace();
    // name
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    aSw.writeSpace();
    // description
    aSw.writeQuotedString( aEntity.description() );
    aSw.writeSeparatorChar();
    aSw.writeSpace();
    // place
    aSw.writeQuotedString( aEntity.place() );
    aSw.writeSpace();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected PlepInfo doRead( IStrioReader aSr ) {
    aSr.ensureString( KW_INFO );
    aSr.ensureChar( CHAR_EQUAL );
    aSr.ensureChar( CHAR_SET_BEGIN );
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String place = aSr.readQuotedString();
    aSr.ensureChar( CHAR_SET_END );
    return new PlepInfo( name, description, place );
  }

}
