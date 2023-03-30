package com.hazard157.psx24.explorer.unit.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.filter.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx24.explorer.filters.*;
import com.hazard157.psx24.explorer.unit.*;

class InquiryItemKeeper
    extends AbstractEntityKeeper<InquiryItem> {

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<InquiryItem> KEEPER = new InquiryItemKeeper();

  private InquiryItemKeeper() {
    super( InquiryItem.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null );
  }

  @Override
  protected void doWrite( IStrioWriter sw, InquiryItem aEntity ) {
    sw.writeChar( CHAR_ARRAY_BEGIN );
    if( aEntity.fpMap().isEmpty() ) {
      sw.writeChar( CHAR_ARRAY_END );
      return;
    }
    sw.incNewLine();
    for( int i = 0; i < aEntity.fpMap().size(); i++ ) {
      sw.writeChars( CHAR_SET_BEGIN, CHAR_SPACE );
      EPqSingleFilterKind k = aEntity.fpMap().keys().get( i );
      TsSingleFilterParamsKeeper.KEEPER.write( sw, aEntity.fpMap().getByKey( k ) );
      sw.writeSeparatorChar();
      sw.writeSpace();
      sw.writeBoolean( aEntity.isInverted( k ) );
      sw.writeChars( CHAR_SPACE, CHAR_SET_END );
      if( i < aEntity.fpMap().size() - 1 ) {
        sw.writeSeparatorChar();
        sw.writeEol();
      }
    }
    sw.decNewLine();
    sw.writeChar( CHAR_ARRAY_END );
  }

  @Override
  protected InquiryItem doRead( IStrioReader sr ) {
    InquiryItem item = new InquiryItem();
    if( sr.readArrayBegin() ) {
      do {
        sr.ensureChar( CHAR_SET_BEGIN );
        ITsSingleFilterParams p = TsSingleFilterParamsKeeper.KEEPER.read( sr );
        EPqSingleFilterKind k = EPqSingleFilterKind.getById( p.typeId() );
        sr.ensureSeparatorChar();
        boolean isInverted = sr.readBoolean();
        item.fpMap().put( k, p );
        item.setInverted( k, isInverted );
        sr.ensureChar( CHAR_SET_END );
      } while( sr.readArrayNext() );
    }
    return item;
  }

  // @Override
  // protected InquiryItem doRead( IDvReader aDr ) {
  //
  // IStridReader sr = aDr.stridReader();
  // sr.readListBegin();
  //
  // InquiryItem item = new InquiryItem();
  // StridRwUtils.readMap( aDr, "FilterParams", EPqSingleFilterKind.KEEPER, TsSingleFilterParamsKeeper.KEEPER,
  // item.fpMap() );
  //
  // sr.readListEnd();
  //
  // return item;
  // }

}
