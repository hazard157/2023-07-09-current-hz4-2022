package com.hazard157.psx24.core.m5.tags;

import static com.hazard157.psx24.core.m5.tags.IPsxResource.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * LM for {@link TagM5Model}.
 *
 * @author hazard157
 */
class TagLifecycleManager
    extends M5LifecycleManager<ITag, ITag> {

  public TagLifecycleManager( IM5Model<ITag> aModel, ITag aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  IOptionSet createInfo( IM5Bunch<ITag> aValues ) {
    IOptionSetEdit info = new OptionSet();
    TagM5Model.DESCRIPTION.dataDef().setValue( info, aValues.get( TagM5Model.DESCRIPTION ) );
    TagM5Model.IS_LEAF.dataDef().setValue( info, aValues.get( TagM5Model.IS_LEAF ) );
    TagM5Model.IS_MANDATORY.dataDef().setValue( info, aValues.get( TagM5Model.IS_MANDATORY ) );
    TagM5Model.IS_RADIO.dataDef().setValue( info, aValues.get( TagM5Model.IS_RADIO ) );
    TagM5Model.IN_RADIO_PRIORITY.dataDef().setValue( info, aValues.get( TagM5Model.IN_RADIO_PRIORITY ) );
    return info;
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ITag> aValues ) {
    String name = aValues.get( TagM5Model.NAME ).asString();
    if( master().findByName( name ) != null ) {
      return ValidationResult.error( FMT_ERR_TAG_NAME_ALREADY_EXISTS, master().id(), name );
    }
    IOptionSet info = createInfo( aValues );
    return master().catalogue().svs().validator().canCreateCategory( master().id(), name, info );
  }

  @Override
  protected ITag doCreate( IM5Bunch<ITag> aValues ) {
    IOptionSet info = createInfo( aValues );
    String name = aValues.get( TagM5Model.NAME ).asString();
    return master().addNode( name, info );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ITag> aValues ) {
    ITag tag = aValues.originalEntity();
    String name = aValues.get( TagM5Model.NAME ).asString();
    if( master().findByName( name ) != null ) {
      if( !tag.nodeName().equals( name ) ) {
        return ValidationResult.error( FMT_ERR_TAG_NAME_ALREADY_EXISTS, master().id(), name );
      }
    }
    // проверим, можно ли делать изменения
    boolean isLeaf = aValues.get( TagM5Model.IS_LEAF ).asBool();
    if( !tag.childNodes().isEmpty() && isLeaf ) {
      return ValidationResult.error( FMT_ERR_CANT_MAKE_CHILDED_NODE_LEAF, tag.id() );
    }
    ValidationResult vr = master().catalogue().svs().validator().canChangeCaregoryLocalId( tag.id(), name );
    if( vr.isError() ) {
      return vr;
    }
    IOptionSet info = createInfo( aValues );
    return ValidationResult.firstNonOk( vr, master().catalogue().svs().validator().canEditCategory( tag.id(), info ) );
  }

  @Override
  protected ITag doEdit( IM5Bunch<ITag> aValues ) {
    ITag tag = aValues.originalEntity();
    IOptionSet info = createInfo( aValues );
    String name = aValues.get( TagM5Model.NAME ).asString();
    tag.setParams( info );
    return tag.catalogue().changeCaregoryLocalId( tag.id(), name );
  }

  @Override
  protected ValidationResult doBeforeRemove( ITag aTag ) {
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !aTag.childNodes().isEmpty() ) {
      vr = ValidationResult.warn( FMT_WARN_REMOVE_TAG_WITH_CHILDS, aTag.id() );
    }
    return ValidationResult.firstNonOk( vr, master().catalogue().svs().validator().canRemoveCategory( aTag.id() ) );
  }

  @Override
  protected void doRemove( ITag aEntity ) {
    aEntity.parent().removeNode( aEntity.id() );
  }

  @Override
  protected IList<ITag> doListEntities() {
    return master().allLeafsBelow();
  }

}
