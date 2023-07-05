package com.hazard157.psx24.explorer.unit;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Description of {@link Inquiry}.
 *
 * @author hazard157
 */
public final class InquiryInfo {

  private final String name;
  private final String description;

  /**
   * Constructor.
   *
   * @param aName String - the name
   * @param aDescription String - the description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public InquiryInfo( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    name = aName;
    description = aDescription;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the inquiry name.
   *
   * @return String - the inquiry name
   */
  public String name() {
    return name;
  }

  /**
   * Returns the inquiry description.
   *
   * @return String - the inquiry description
   */
  public String description() {
    return description;
  }

}
