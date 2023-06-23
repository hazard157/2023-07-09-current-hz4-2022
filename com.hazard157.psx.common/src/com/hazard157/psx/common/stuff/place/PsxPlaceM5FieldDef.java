package com.hazard157.psx.common.stuff.place;

import static com.hazard157.psx.common.stuff.place.IPlaceableConstants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;

/**
 * {@link IM5AttributeFieldDef} implementation for {@link IPlaceable#place()}.
 *
 * @author hazard157
 * @param <T> - expected subclass of {@link IPlaceable}
 */
public class PsxPlaceM5FieldDef<T extends IPlaceable>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   */
  public PsxPlaceM5FieldDef() {
    super( DDEF_PLACE );
    setFlags( M5FF_DETAIL );
  }

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return avStr( aEntity.place() );
  }

}
