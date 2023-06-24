package com.hazard157.lib.core.excl_done.flags;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * 64-bit long named flags set representation.
 *
 * @author hazard157
 */
public interface IFlagSet {

  void defineSingle( String aName, int aIndex );

  void defineGroup( String aName, int aIndex, int aWidth, IStringList aStateNames );

}
