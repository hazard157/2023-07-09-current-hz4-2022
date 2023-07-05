package com.hazard157.prisex24.explorer.pdu.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.prisex24.explorer.pdu.*;

/**
 * Keeper of the {@link Inquiry}.
 *
 * @author hazard157
 */
public class InquiryKeeper
    extends AbstractEntityKeeper<Inquiry> {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<Inquiry> KEEPER = new InquiryKeeper();

  private static final String KW_INQUIRY_PARAMS = "InquiryParams"; //$NON-NLS-1$

  private InquiryKeeper() {
    super( Inquiry.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter sw, Inquiry aEntity ) {
    sw.incNewLine();
    // id()
    sw.writeAsIs( aEntity.id() );
    sw.writeSeparatorChar();
    sw.writeEol();
    // info();
    InquiryInfoKeeper.KEEPER.write( sw, aEntity.info() );
    sw.writeEol();
    // fpList()
    StrioUtils.writeCollection( sw, KW_INQUIRY_PARAMS, aEntity.items(), InquiryItemKeeper.KEEPER, true );
    sw.writeEol();
    sw.decNewLine();
  }

  @Override
  protected Inquiry doRead( IStrioReader sr ) {
    String id = sr.readIdPath();
    sr.ensureSeparatorChar();
    InquiryInfo info = InquiryInfoKeeper.KEEPER.read( sr );
    Inquiry es = new Inquiry( id, info );
    IList<InquiryItem> ll = StrioUtils.readCollection( sr, KW_INQUIRY_PARAMS, InquiryItemKeeper.KEEPER );
    es.items().setAll( ll );
    return es;
  }

}
