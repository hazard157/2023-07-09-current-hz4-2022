package com.hazard157.lib.core.excl_plan.visumple;

import static com.hazard157.lib.core.excl_plan.visumple.IVisumpleConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Visumple (VISUal exaMPLE) - illustrative image.
 *
 * @author hazard157
 */
public final class Visumple
    implements IParameterized {

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
  public Visumple( String aFilePath, IOptionSet aParams ) {
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
    return OP_NOTES.getValue( params ).asString();
  }

  /**
   * Returns tag marks of visumple.
   *
   * @return {@link IStringList} - tags list
   */
  public IStringList tags() {
    return OP_TAGS.getValue( params ).asValobj();
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
    Visumple other = (Visumple)obj;
    return filePath.equals( other.filePath ) && params.equals( other.params );
  }

}
