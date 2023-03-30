package com.hazard157.psx.proj3.trailers;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Информация об осмысленных видах трейлеров, которые может иметь любой эпизод.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public final class SensibleTrailerInfo
    extends Stridable {

  /**
   * Синглтон хранителя.
   */
  public static final IEntityKeeper<SensibleTrailerInfo> KEEPER =
      new AbstractEntityKeeper<>( SensibleTrailerInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, SensibleTrailerInfo aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.nmName() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.description() );
        }

        @Override
        protected SensibleTrailerInfo doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String name = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          String descr = aSr.readQuotedString();
          return new SensibleTrailerInfo( id, name, descr );
        }
      };

  /**
   * Конструктор.
   *
   * @param aId String - идентификатор (ИД-путь) сущности
   * @param aName String - название сущности
   * @param aDescription String - описание сущности
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-путь
   */
  public SensibleTrailerInfo( String aId, String aName, String aDescription ) {
    super( aId, aName, aDescription );
  }

}
