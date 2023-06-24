package com.hazard157.psx.proj3.gaze.impl;

import static com.hazard157.psx.proj3.gaze.IGazeConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.common.quants.rating.*;
import com.hazard157.psx.proj3.gaze.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * {@link IGaze} implementation.
 *
 * @author hazard157
 */
public class Gaze
    extends StridableParameterized
    implements IGaze {

  /**
   * Empty touch singleton.
   */
  public static final IGaze NULL = new Gaze( IStridable.NONE_ID, IOptionSet.NULL );

  /**
   * Factory instance.
   */
  public static final IStriparCreator<IGaze> CREATOR = Gaze::new;

  /**
   * Constructor.
   *
   * @param aId String - the gaze ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public Gaze( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // IPsxIncident
  //

  @Override
  final public EPsxIncidentKind incidentKind() {
    return EPsxIncidentKind.GAZE;
  }

  @Override
  public LocalDate incidentDate() {
    return OPDEF_DATE.getValue( params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // IRatingable
  //

  @Override
  public ERating rating() {
    return OPDEF_RATING.getValue( params() ).asValobj();
  }

  // ------------------------------------------------------------------------------------
  // IGaze
  //

  @Override
  public String place() {
    return OPDEF_PLACE.getValue( params() ).asString();
  }

}
