package com.hazard157.lib.core.bricks.kwmark.manager;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Manage keywords for compicated entities tagging.
 * <p>
 * Keywords are IDpaths consisting of tho components - group name and local name. By definition groups are IDnames.
 *
 * @author hazard157
 */
public interface IKeywordManager
    extends IProjDataUnit {

  /**
   * Returns all keywrods and groups.
   *
   * @return {@link IStringList} - alphabetically sorted list of groups and keywords
   */
  IStringList listAll();

  /**
   * Returns all groups.
   *
   * @return {@link IStringList} - alphabetically sorted list of groups
   */
  IStringList listGroups();

  /**
   * Returns all keywords.
   *
   * @return {@link IStringList} - alphabetically sorted list of keywords
   */
  IStringList listKeywords();

  /**
   * Returns the keywords of specified group.
   * <p>
   * For non-existing groups or groups without any keyword returns
   *
   * @param aGroupName String - the groups name (IDname)
   * @param aOnlyLocalNames boolean - <code>true</code> returns only local names of keywords
   * @return {@link IStringList} - alphabetically sorted list of keywords or only local names
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDname
   */
  IStringList listKeywords( String aGroupName, boolean aOnlyLocalNames );

  /**
   * Creates now keyword/group or does nothing if keyword already exists.
   * <p>
   * If aName is IDname, it is considered as group name. Two component IDpaths are considered as keyword name. If group
   * nae in keyword does not exists it will be created.
   *
   * @param aName String - the keyword/group name (IDpath)
   * @return String - always returns argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   * @throws TsIllegalArgumentRtException argument is an IDpath but has more than two components
   */
  String define( String aName );

  /**
   * Removes specified keyword/group.
   * <p>
   * If no such item exists method does nothing.
   * <p>
   * Warning: together with group all it's child keywrods are removed.
   *
   * @param aName String - the keyword/group name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void remove( String aName );

}
