package com.hazard157.lib.core.excl_done.visumple3;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Visumple3 (VISUal exaMPLE) - illustrative image.
 *
 * @author hazard157
 */
public final class Visumple3
    implements IParameterized {

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "Visumple3"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<Visumple3> KEEPER =
      new AbstractEntityKeeper<>( Visumple3.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, Visumple3 aEntity ) {
          aSw.writeQuotedString( aEntity.filePath() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected Visumple3 doRead( IStrioReader aSr ) {
          String filePath = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new Visumple3( filePath, params );
        }

      };

  private final String     filePath;
  private final IOptionSet params;

  /**
   * Конструктор.
   *
   * @param aFilePath String - путь к файлу
   * @param aParams {@link IOptionSet} - значения {@link #params()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException путь к файлу - пастая строка
   */
  public Visumple3( String aFilePath, IOptionSet aParams ) {
    TsErrorUtils.checkNonBlank( aFilePath );
    TsNullArgumentRtException.checkNull( aParams );
    params = new OptionSet( aParams );
    filePath = aFilePath;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Return path to the visumple file.
   *
   * @return String - file path
   */
  public String filePath() {
    return filePath;
  }

  /**
   * Returns notes to visumple.
   *
   * @return String - notes
   */
  public String notes() {
    return params.getStr( FID_NOTES, TsLibUtils.EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return "file=" + filePath + ", notes=" + notes();
  }

  @Override
  public int hashCode() {
    final int prime = TsLibUtils.PRIME;
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = prime * result + filePath.hashCode();
    result = prime * result + params.hashCode();
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj ) {
      return true;
    }
    if( obj == null ) {
      return false;
    }
    if( getClass() != obj.getClass() ) {
      return false;
    }
    Visumple3 other = (Visumple3)obj;
    return filePath.equals( other.filePath ) && params.equals( other.params );
  }

}
