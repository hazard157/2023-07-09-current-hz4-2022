package com.hazard157.psx24.catnote.main.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * {@link INbCategory} implementation.
 *
 * @author goga
 */
class NbCategory
    extends StridableParameterized
    implements INbCategory {

  static final IStriparCreator<INbCategory> CREATOR = ( aId, aParams ) -> new NbCategory( aId, aParams );

  public NbCategory( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

}
