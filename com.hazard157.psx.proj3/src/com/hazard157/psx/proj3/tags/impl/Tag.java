package com.hazard157.psx.proj3.tags.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.txtproj.lib.categs.impl.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Реализация {@link ITag}.
 *
 * @author hazard157
 */
class Tag
    extends Category<ITag>
    implements ITag {

  static final ICategoryCreator CREATOR = ( aCatalogue, aId, aParams ) -> new Tag( aCatalogue, aId, aParams );

  Tag( Catalogue<?> aCatalogue, String aId, IOptionSet aParams ) {
    super( aCatalogue, aId, aParams );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITag
  //

  @Override
  public String nmName() {
    return localId();
  }

  @Override
  public void setParams( IOptionSet aInfo ) {
    params().setAll( aInfo );
  }

  // @Override
  // public void changeName( String aName ) {
  // StridUtils.checkValidIdName( aName );
  // if( aName.equals( nmName() ) ) {
  // return;
  // }
  // // String newId = CatalogueUtils.makeCategoryId( id(), aName );
  // // TsItemAlreadyExistsRtException.checkTrue( parent.childNodes().hasKey( newId ) );
  // // name = aName;
  // // processIdChange();
  // // tagManager().nodesChanged();
  // // TODO реализовать Tag.changeName()
  // throw new TsUnderDevelopmentRtException( "Tag.changeName()" );
  // }

  @Override
  public String nodeName() {
    return localId();
  }

  @Override
  public ITag parent() {
    return (ITag)super.parent();
  }

  @Override
  public IStridablesList<ITag> childNodes() {
    return super.childCategories();
  }

  @Override
  public IStridablesList<ITag> allNodesBelow() {
    return super.scionCategories();
  }

  @Override
  public IStridablesList<ITag> allLeafsBelow() {
    return catalogue().listScionCategories( id(), false, false, true );
  }

  @Override
  public IStridablesList<ITag> allGroupsBelow() {
    return catalogue().listScionCategories( id(), false, true, false );
  }

  @Override
  public ITag findByName( String aNodeName ) {
    String newId = CatalogueUtils.makeCategoryId( id(), aNodeName );
    return (ITag)catalogue().findCategory( newId );
  }

  @Override
  public ITag addNode( String aNodeName, IOptionSet aInfo ) {
    return catalogue().createCategory( id(), aNodeName, aInfo );
  }

  @Override
  public ITag addNode( String aNodeName, Object... aNamesAndValues ) {
    return addNode( aNodeName, OptionSetUtils.createOpSet( aNamesAndValues ) );
  }

  @Override
  public ITag removeNode( String aNodeId ) {
    ITag tag = (ITag)catalogue().getCategory( aNodeId );
    catalogue().removeCategory( aNodeId );
    return tag;
  }

  @Override
  public String formatNodeId( String aNodeName ) {
    return CatalogueUtils.makeCategoryId( id(), aNodeName );
  }

}
