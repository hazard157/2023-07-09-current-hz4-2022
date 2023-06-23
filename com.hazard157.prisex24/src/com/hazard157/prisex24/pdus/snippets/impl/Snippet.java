package com.hazard157.prisex24.pdus.snippets.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.prisex24.pdus.snippets.*;

/**
 * {@link ISnippet} implementation.
 *
 * @author hazard157
 */
public class Snippet
    extends StridableParameterized
    implements ISnippet {

  static final IStriparCreator<ISnippet> CREATOR = Snippet::new;

  Snippet( String aId, IOptionSet aParams ) {
    super( aId, aParams );
  }

}
