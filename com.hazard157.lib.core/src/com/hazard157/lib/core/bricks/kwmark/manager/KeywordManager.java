package com.hazard157.lib.core.bricks.kwmark.manager;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.impl.*;

/**
 * {@link IKeywordManager} implementation.
 *
 * @author hazard157
 */
public class KeywordManager
    extends AbstractProjDataUnit
    implements IKeywordManager {

  private static final String KW_ALL_ITEMS = "All"; //$NON-NLS-1$

  private final IStringListBasicEdit allItems = new SortedStringLinkedBundleList();

  /**
   * OPTIMIZE create cached lists of all groups, all keywords and keywords by groups
   */

  /**
   * Constructor.
   */
  public KeywordManager() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Internals
  //

  private IStringList extractGroups() {
    IStringListEdit ll = new StringArrayList();
    for( String s : allItems ) {
      if( !StridUtils.isIdAPath( s ) ) {
        ll.add( s );
      }
    }
    return ll;
  }

  private IStringList extractKeywords() {
    IStringListEdit ll = new StringArrayList();
    for( String s : allItems ) {
      if( StridUtils.isIdAPath( s ) ) {
        ll.add( s );
      }
    }
    return ll;
  }

  private IStringList extractKeywords( String aGroup ) {
    IStringListEdit ll = new StringArrayList();
    for( String s : allItems ) {
      IStringList comps = StridUtils.getComponents( s );
      if( comps.size() == 2 && comps.first().equals( aGroup ) ) {
        ll.add( s );
      }
    }
    return ll;
  }

  private IStringList extractLocals( String aGroup ) {
    IStringListEdit ll = new StringArrayList();
    for( String s : allItems ) {
      IStringList comps = StridUtils.getComponents( s );
      if( comps.size() == 2 && comps.first().equals( aGroup ) ) {
        ll.add( comps.last() );
      }
    }
    return ll;
  }

  // ------------------------------------------------------------------------------------
  // AbstractProjDataUnit
  //

  @Override
  protected void doWrite( IStrioWriter aSw ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.incNewLine();
    StrioUtils.writeKeywordHeader( aSw, KW_ALL_ITEMS );
    aSw.writeChar( CHAR_ARRAY_BEGIN );
    int identLevel = aSw.getIndentLevel() + 1;
    for( String s : allItems ) {
      boolean isGroup = !StridUtils.isIdAPath( s );
      int currIndentLevel = isGroup ? identLevel : identLevel + 1;
      aSw.setIndentLevel( currIndentLevel );
      aSw.writeEol();
      aSw.writeAsIs( s );
      aSw.writeSeparatorChar();
    }
    aSw.setIndentLevel( identLevel );
    aSw.decNewLine();
    aSw.writeChar( CHAR_ARRAY_END );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected void doRead( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    StrioUtils.ensureKeywordHeader( aSr, KW_ALL_ITEMS );
    IStringList sl = StringListKeeper.KEEPER.read( aSr );
    if( !TsCollectionsUtils.isListsEqual( sl, allItems ) ) {
      allItems.setAll( sl );
      genericChangeEventer.fireChangeEvent();
    }
    aSr.ensureChar( CHAR_SET_END );
  }

  @Override
  protected void doClear() {
    if( !allItems.isEmpty() ) {
      allItems.clear();
      genericChangeEventer.fireChangeEvent();
    }
  }

  // ------------------------------------------------------------------------------------
  // IKeywordManager
  //

  @Override
  public IStringList listAll() {
    return allItems;
  }

  @Override
  public IStringList listGroups() {
    return extractGroups();
  }

  @Override
  public IStringList listKeywords() {
    return extractKeywords();
  }

  @Override
  public IStringList listKeywords( String aGroupName, boolean aOnlyLocalNames ) {
    StridUtils.checkValidIdName( aGroupName );
    if( aOnlyLocalNames ) {
      return extractLocals( aGroupName );
    }
    return extractKeywords( aGroupName );
  }

  @Override
  public String define( String aName ) {
    IStringList comps = StridUtils.getComponents( aName );
    if( allItems.hasElem( aName ) ) {
      return aName;
    }
    switch( comps.size() ) {
      case 1: { // defining the group
        // OK, go ahead and add aName as group
        break;
      }
      case 2: { // defining the keyword
        String group = comps.first();
        if( !allItems.hasElem( group ) ) {
          allItems.add( group );
        }
        // OK, go ahead and add aName as keyword
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    allItems.add( aName );
    genericChangeEventer.fireChangeEvent();
    return aName;
  }

  @Override
  public void remove( String aName ) {
    // TODO Auto-generated method stub

  }

}
