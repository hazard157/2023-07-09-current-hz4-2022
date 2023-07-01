package com.hazard157.lib.core.excl_done.visumple;

import java.util.Collection;

import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IVisumplesList} editable implementation.
 *
 * @author hazard157
 */
public class VisumplesList
    extends ElemArrayList<Visumple>
    implements IVisumplesList {

  private static final long serialVersionUID = -2088895356597201389L;

  /**
   * Creates list with all invariant properties.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public VisumplesList( int aInitialCapacity, boolean aAllowDuplicates ) {
    super( aInitialCapacity, aAllowDuplicates );
  }

  /**
   * Constructor creates list with default capacity.
   *
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   */
  public VisumplesList( boolean aAllowDuplicates ) {
    super( aAllowDuplicates );
  }

  /**
   * Creates empty list with duplicates allowed.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public VisumplesList( int aInitialCapacity ) {
    super( aInitialCapacity );
  }

  /**
   * Creates empty list with defaults.
   */
  public VisumplesList() {
    super();
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems &lt;Visumple&gt;[] - array of list elements
   * @throws TsNullArgumentRtException array reference on any of it's element = <code>null</code>
   */
  public VisumplesList( Visumple... aElems ) {
    super( aElems );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aSource {@link Collection}&lt;Visumple&gt; - collection of lisyt elements
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException source collection or any element = null
   */
  public VisumplesList( Collection<Visumple> aSource ) {
    super( aSource );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;Visumple&gt; - source collection
   * @throws TsNullArgumentRtException aSource = null
   */
  public VisumplesList( ITsCollection<Visumple> aSource ) {
    super( aSource );
  }

}
