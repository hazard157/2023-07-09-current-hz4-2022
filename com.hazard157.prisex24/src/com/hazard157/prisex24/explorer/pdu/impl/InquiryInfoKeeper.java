package com.hazard157.prisex24.explorer.pdu.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.prisex24.explorer.pdu.*;

/**
 * Keeper of the {@link InquiryInfo}.
 *
 * @author hazard157
 */
public class InquiryInfoKeeper
    extends AbstractEntityKeeper<InquiryInfo> {

  private static final String KW_INFO = "Info"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<InquiryInfo> KEEPER = new InquiryInfoKeeper();

  private InquiryInfoKeeper() {
    super( InquiryInfo.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, InquiryInfo aEntity ) {
    aSw.writeAsIs( KW_INFO );
    aSw.writeChars( CHAR_SPACE, CHAR_EQUAL, CHAR_SPACE, CHAR_SET_BEGIN );
    aSw.writeSpace();
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    aSw.writeSpace();
    aSw.writeQuotedString( aEntity.description() );
    aSw.writeSpace();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected InquiryInfo doRead( IStrioReader aSr ) {
    aSr.ensureString( KW_INFO );
    aSr.ensureChar( CHAR_EQUAL );
    aSr.readSetBegin();
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    String description = aSr.readQuotedString();
    aSr.readSetNext();
    return new InquiryInfo( name, description );
  }

}
