package com.hazard157.psx.proj3.tags.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.impl.*;

import com.hazard157.psx.proj3.tags.*;

/**
 * Реализация {@link IUnitTags}.
 *
 * @author hazard157
 */
public class UnitTags
    extends AbstractProjDataUnit
    implements IUnitTags {

  private static final String KW_TAGS             = "Tags";                    //$NON-NLS-1$
  private static final String KW_INITIAL_ROOT_IDS = "InitialRootsForEpisodes"; //$NON-NLS-1$
  private static final String KW_TAG_OFFERS       = "TagOffers";               //$NON-NLS-1$

  private final IStringListEdit             initialRootsForEpisodes = new StringLinkedBundleList();
  private final IStringMapEdit<IStringList> tagOffersMap            = new StringMap<>();

  private final RootTag root;

  /**
   * Конструктор.
   */
  public UnitTags() {
    root = new RootTag();
    root.eventer().addListener( ( aCatalogue, aOp, aCategoryId ) -> genericChangeEventer().fireChangeEvent() );
  }

  // ------------------------------------------------------------------------------------
  // Методы AbstractProjDataUnit
  //

  @SuppressWarnings( "deprecation" )
  @Override
  protected void doWrite( IStrioWriter aSw ) {
    //
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.incNewLine();
    // tags
    StrioUtils.writeKeywordHeader( aSw, KW_TAGS );
    root.write( aSw );
    aSw.writeEol();
    // initialRootsForEpisodes
    StrioUtils.writeKeywordHeader( aSw, KW_INITIAL_ROOT_IDS );
    StringListKeeper.COMPAT_KEEPER.write( aSw, initialRootsForEpisodes );
    aSw.writeEol();
    // offers
    StrioUtils.writeStringMap( aSw, KW_TAG_OFFERS, tagOffersMap, StringListKeeper.COMPAT_KEEPER, true );
    //
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @SuppressWarnings( "deprecation" )
  @Override
  protected void doRead( IStrioReader aSr ) {
    TsNullArgumentRtException.checkNull( aSr );
    aSr.ensureChar( CHAR_SET_BEGIN );
    try {
      genericChangeEventer().pauseFiring();
      // tags
      StrioUtils.ensureKeywordHeader( aSr, KW_TAGS );
      root.read( aSr );
      // initialRootsForEpisodes
      StrioUtils.ensureKeywordHeader( aSr, KW_INITIAL_ROOT_IDS );
      initialRootsForEpisodes.addAll( StringListKeeper.COMPAT_KEEPER.read( aSr ) );
      // offers
      tagOffersMap.clear();
      StrioUtils.readStringMap( aSr, KW_TAG_OFFERS, StringListKeeper.COMPAT_KEEPER, tagOffersMap );
    }
    finally {
      genericChangeEventer().resumeFiring( true );
    }
    aSr.ensureChar( CHAR_SET_END );
  }

  @Override
  protected void doClear() {
    if( root.hasChildren() ) {
      root.clear();
      initialRootsForEpisodes.clear();
      tagOffersMap.clear();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IUnitTags
  //

  @Override
  public IRootTag root() {
    return root;
  }

  @Override
  public IStringList getInitialRootsIds() {
    return initialRootsForEpisodes;
  }

  @Override
  public void setInitialRootsIds( IStringList aRootIds ) {
    TsNullArgumentRtException.checkNulls( aRootIds );
    IStridablesList<ITag> childs = root.childCategories();
    for( String tagId : aRootIds ) {
      if( !childs.keys().hasElem( tagId ) ) {
        throw new TsIllegalArgumentRtException();
      }
    }
    if( initialRootsForEpisodes.equals( aRootIds ) ) {
      return;
    }
    initialRootsForEpisodes.setAll( aRootIds );
    genericChangeEventer().fireChangeEvent();
  }

  @Override
  public IStringList getMasterTagIds() {
    return tagOffersMap.keys();
  }

  @Override
  public IStringList getTagOfferIds( String aMasterId ) {
    IStridablesList<ITag> nodesBelow = root.scionCategories();
    ITag tag = nodesBelow.getByKey( aMasterId );
    TsIllegalArgumentRtException.checkFalse( tag.childNodes().isEmpty() );
    IStringList ids = tagOffersMap.findByKey( aMasterId );
    if( ids != null ) {
      return ids;
    }
    return IStringList.EMPTY;
  }

  @Override
  public void setTagOfferIds( String aMasterId, IStringList aOfferIds ) {
    IStridablesList<ITag> nodesBelow = root.scionCategories();
    ITag tag = nodesBelow.getByKey( aMasterId );
    TsIllegalArgumentRtException.checkFalse( tag.childNodes().isEmpty() );
    if( aOfferIds == null || aOfferIds.isEmpty() ) {
      if( tagOffersMap.removeByKey( aMasterId ) != null ) {
        genericChangeEventer().fireChangeEvent();
      }
      return;
    }
    for( String tagId : aOfferIds ) {
      ITag t = nodesBelow.findByKey( tagId );
      if( t == null ) {
        throw new TsItemNotFoundRtException();
      }
      if( t.childNodes().isEmpty() ) {
        throw new TsItemNotFoundRtException();
      }
    }
    IStringList ids = tagOffersMap.findByKey( aMasterId );
    if( Objects.equals( aOfferIds, ids ) ) {
      return;
    }
    tagOffersMap.put( aMasterId, aOfferIds );
    genericChangeEventer().fireChangeEvent();
  }

  @Override
  public IStridablesList<ITag> getEnabledTagGroups( IStringList aUsedTagIds ) {
    TsNullArgumentRtException.checkNull( aUsedTagIds );
    IStridablesList<ITag> nodesBelow = root.scionCategories();
    IStringListBasicEdit enabedTagIds = new SortedStringLinkedBundleList( TsCollectionsUtils.DEFAULT_BUNDLE_CAPACITY, //
        false ); // без повторов идентификаторов
    // добавим все корневые группы
    enabedTagIds.addAll( initialRootsForEpisodes );
    // добавим узлы содержащие указанные ярлыки и разрешаемые или узлы
    for( String usedId : aUsedTagIds ) {
      // добавим используемый узел
      ITag usedTag = nodesBelow.getByKey( usedId );
      if( usedTag.childNodes().isEmpty() ) { // для узла-листа добавим родительский идентификатор
        enabedTagIds.add( usedTag.parent().id() );
      }
      else {
        enabedTagIds.add( usedId ); // для узла-группы добавим свой идентификатор
      }
      // добавим разрешаемые ярлыком узлы
      if( usedTag.childNodes().isEmpty() ) {
        enabedTagIds.addAll( getTagOfferIds( usedId ) );
      }
    }
    return getTagsByIds( enabedTagIds );
  }

  @Override
  public IStridablesList<ITag> getTagsByIds( IStringList aTagIds ) {
    TsNullArgumentRtException.checkNull( aTagIds );
    if( aTagIds.isEmpty() ) {
      return IStridablesList.EMPTY;
    }
    IStridablesList<ITag> nodesBelow = root.scionCategories();
    IStridablesListEdit<ITag> result = new StridablesList<>();
    for( String tagId : aTagIds ) {
      result.add( nodesBelow.getByKey( tagId ) );
    }
    return result;
  }

}
