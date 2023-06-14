package com.hazard157.psx24.core.m5.todos;

import static com.hazard157.psx24.core.m5.todos.IPsxResources.*;
import static com.hazard157.psx24.core.m5.todos.ITodoM5Constants.*;

import org.toxsoft.core.tsgui.m5.std.models.enums.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * M5-Model of {@link EPriority}.
 *
 * @author hazard157
 */
public class PriorityM5Model
    extends M5StridableEnumModelBase<EPriority> {

  /**
   * Constructor.
   */
  public PriorityM5Model() {
    super( MID_PRIORITY, EPriority.class );
    setNameAndDescription( STR_N_M5M_PRIORITY, STR_D_M5M_PRIORITY );
  }

}
