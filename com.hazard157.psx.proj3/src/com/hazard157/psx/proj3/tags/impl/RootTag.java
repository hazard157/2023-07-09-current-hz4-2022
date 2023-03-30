package com.hazard157.psx.proj3.tags.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.txtproj.lib.categs.impl.*;

import com.hazard157.psx.proj3.tags.*;

class RootTag
    extends Catalogue<ITag>
    implements IRootTag {

  RootTag() {
    super( Tag.CREATOR );
  }

  @Override
  public ITag parent() {
    return null;
  }

  @Override
  public void setParams( IOptionSet aInfo ) {
    // nop
  }

  @Override
  public String nodeName() {
    return EMPTY_STRING;
  }

  @Override
  public IStridablesList<ITag> childNodes() {
    return childCategories();
  }

  @Override
  public IStridablesList<ITag> allNodesBelow() {
    return scionCategories();
  }

  @Override
  public IStridablesList<ITag> allLeafsBelow() {
    return listScionCategories( EMPTY_STRING, false, false, true );
  }

  @Override
  public IStridablesList<ITag> allGroupsBelow() {
    return listScionCategories( EMPTY_STRING, false, true, false );
  }

  @Override
  public ITag findByName( String aNodeName ) {
    return childCategories().findByKey( aNodeName );
  }

  @Override
  public ITag addNode( String aNodeName, IOptionSet aInfo ) {
    return createCategory( EMPTY_STRING, aNodeName, aInfo );
  }

  @Override
  public ITag addNode( String aNodeName, Object... aNamesAndValues ) {
    return createCategory( EMPTY_STRING, aNodeName, OptionSetUtils.createOpSet( aNamesAndValues ) );
  }

  @Override
  public ITag removeNode( String aNodeId ) {
    ITag tag = getCategory( aNodeId );
    removeCategory( aNodeId );
    return tag;
  }

  @Override
  public String formatNodeId( String aNodeName ) {
    return CatalogueUtils.makeCategoryId( EMPTY_STRING, aNodeName );
  }

}
