package com.hazard157.psx.proj3.mingle.impl;

import static com.hazard157.psx.proj3.mingle.IMingleConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx.proj3.incident.*;
import com.hazard157.psx.proj3.mingle.*;

/**
 * {@link IMingle} implementation.
 *
 * @author hazard157
 */
public class Mingle
    extends StridableParameterized
    implements IMingle {

  /**
   * Empty touch singleton.
   */
  public static final IMingle NULL = new Mingle( IStridable.NONE_ID, IOptionSet.NULL );

  /**
   * Factory instance.
   */
  public static final IStriparCreator<IMingle> CREATOR = Mingle::new;

  /**
   * Constructor.
   *
   * @param aId String - the mingle ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public Mingle( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // IPsxIncident
  //

  @Override
  final public EPsxIncidentKind incidentKind() {
    return EPsxIncidentKind.MINGLE;
  }

  @Override
  public LocalDate incidentDate() {
    return OPDEF_DATE.getValue( params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // IMingle
  //

  @Override
  public String place() {
    return OPDEF_PLACE.getValue( params() ).asString();
  }

}
