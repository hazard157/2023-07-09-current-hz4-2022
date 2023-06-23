package com.hazard157.prisex24.pdus.snippets.impl;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.txtproj.lib.stripar.*;

import com.hazard157.prisex24.pdus.snippets.*;

/**
 * {@link IUnitSnippets} implementation.
 *
 * @author hazard157
 */
public class UnitSnippets
    extends StriparManager<ISnippet>
    implements IUnitSnippets {

  private IStringMap<IStridablesList<ISnippet>> cachedCategMap = null;

  /**
   * Constructor.
   */
  public UnitSnippets() {
    super( Snippet.CREATOR );
    defineOptions( ISnippetConstants.ALL_OPTION_DEFS );
    eventer().addListener( ( s, o, i ) -> whenSnippetsChanged() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenSnippetsChanged() {
    cachedCategMap = null;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void ensureCaches() {
    IStringMapEdit<IStridablesListBasicEdit<ISnippet>> map = new SortedStridMap<>();
    for( ISnippet s : items() ) {
      String categ = s.category();
      IStridablesListBasicEdit<ISnippet> ll = map.findByKey( categ );
      if( ll == null ) {
        ll = new SortedStridablesList<>();
        map.put( categ, ll );
      }
      ll.add( s );
    }
    cachedCategMap = (IStringMap)map;
  }

  // ------------------------------------------------------------------------------------
  // IUnitSnippets
  //

  @Override
  public IStringList listCategories() {
    ensureCaches();
    return cachedCategMap.keys();
  }

  @Override
  public IStridablesList<ISnippet> listByCategory( String aCategory ) {
    IStridablesList<ISnippet> ll = cachedCategMap.findByKey( aCategory );
    return ll != null ? ll : IStridablesList.EMPTY;
  }

}
