package com.hazard157.prisex24.m5.snippet;

import org.toxsoft.core.tsgui.m5.*;

import com.hazard157.common.incub.striparldm.*;
import com.hazard157.prisex24.pdus.snippets.*;

/**
 * LM for {@link SnippetM5Model}.
 *
 * @author hazard157
 */
class SnippetM5LifecycleManager
    extends StriparM5LifecycleManager<ISnippet> {

  public SnippetM5LifecycleManager( IM5Model<ISnippet> aModel, IUnitSnippets aMaster ) {
    super( aModel, aMaster );
  }

}
