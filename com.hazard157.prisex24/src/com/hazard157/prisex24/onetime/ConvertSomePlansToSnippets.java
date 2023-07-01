package com.hazard157.prisex24.onetime;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

import com.hazard157.prisex24.*;
import com.hazard157.prisex24.pdus.snippets.*;
import com.hazard157.psx.proj3.pleps.*;

/**
 * Convert some plans to snippets.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public class ConvertSomePlansToSnippets
    implements IPsxGuiContextable {

  private final ITsGuiContext tsContext;

  public ConvertSomePlansToSnippets( ITsGuiContext aContext ) {
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void makeSnippetsFromStirs( IPlep aPlep, String aCategory ) {
    int count = 1;
    for( IStir s : aPlep.stirs() ) {
      String id = aPlep.id() + ".no" + count++;
      if( unitSnippets().items().hasKey( id ) ) {
        continue;
      }
      IOptionSetEdit p = new OptionSet();
      p.setStr( TSID_NAME, s.name() );
      p.setStr( TSID_DESCRIPTION, s.description() );
      p.setStr( ISnippetConstants.OPDEF_CATEGORY, aCategory );

      // TODO no visumples

      unitSnippets().createItem( id, p );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void convertStirsToSnippets() {
    IUnitPleps unitPleps = tsContext.get( IUnitPleps.class );
    IPlep p = unitPleps.items().findByKey( "ideas.shooting" );
    if( p != null ) {
      makeSnippetsFromStirs( p, "shoot" );
    }
    p = unitPleps.items().findByKey( "ideas.collection" );
    if( p != null ) {
      makeSnippetsFromStirs( p, "idea" );
    }
  }

}
