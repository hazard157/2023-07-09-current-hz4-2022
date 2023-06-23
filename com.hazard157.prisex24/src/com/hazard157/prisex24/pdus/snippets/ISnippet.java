package com.hazard157.prisex24.pdus.snippets;

import static com.hazard157.prisex24.pdus.snippets.ISnippetConstants.*;

import org.toxsoft.core.tslib.bricks.strid.*;

import com.hazard157.common.quants.visumple.*;
import com.hazard157.psx.common.stuff.place.*;

/**
 * The snippet is illustrated memo
 * <p>
 * Snippet have unique ID to be referenced from other part of application like PLEPs.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISnippet
    extends IStridableParameterized, //
    IParamsVisumplable, IParamsPlaceable {

  default String category() {
    return OPDEF_CATEGORY.getValue( params() ).asString();
  }

}
