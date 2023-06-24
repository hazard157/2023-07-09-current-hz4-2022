package com.hazard157.lib.core.excl_done.visumple3;

import java.util.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVisumples3List} editable implementation.
 *
 * @author hazard157
 */
public class Visumples3List
    extends ElemArrayList<Visumple3>
    implements IVisumples3List {

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "Visumples3List"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IVisumples3List> KEEPER =
      new AbstractEntityKeeper<>( IVisumples3List.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVisumples3List aEntity ) {
          StrioUtils.writeCollection( aSw, TsLibUtils.EMPTY_STRING, aEntity, Visumple3.KEEPER, true );
        }

        @Override
        protected IVisumples3List doRead( IStrioReader aSr ) {
          IList<Visumple3> ll = StrioUtils.readCollection( aSr, TsLibUtils.EMPTY_STRING, Visumple3.KEEPER );
          return new Visumples3List( ll );
        }

      };

  private static final long serialVersionUID = -2088895356597201389L;

  /**
   * Creates list with all invariant properties.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public Visumples3List( int aInitialCapacity, boolean aAllowDuplicates ) {
    super( aInitialCapacity, aAllowDuplicates );
  }

  /**
   * Constructor creates list with default capacity.
   *
   * @param aAllowDuplicates <b>true</b> - duplicate elements are allowed in list;<br>
   *          <b>false</b> - list will not contain duplicate elements.
   */
  public Visumples3List( boolean aAllowDuplicates ) {
    super( aAllowDuplicates );
  }

  /**
   * Creates empty list with duplicates allowed.
   *
   * @param aInitialCapacity int - initial capacity of list (if 0 capacity is set to default)
   * @throws TsIllegalArgumentRtException aInitialCapacity < 0
   */
  public Visumples3List( int aInitialCapacity ) {
    super( aInitialCapacity );
  }

  /**
   * Creates empty list with defaults.
   */
  public Visumples3List() {
    super();
  }

  /**
   * Creates default list with initial content.
   *
   * @param aElems &lt;Visumple3&gt;[] - array of list elements
   * @throws TsNullArgumentRtException array reference on any of it's element = <code>null</code>
   */
  public Visumples3List( Visumple3... aElems ) {
    super( aElems );
  }

  /**
   * Creates default list with initial content.
   *
   * @param aSource {@link Collection}&lt;Visumple3&gt; - collection of lisyt elements
   * @throws TsNullArgumentRtException aSource = null
   * @throws TsNullArgumentRtException source collection or any element = null
   */
  public Visumples3List( Collection<Visumple3> aSource ) {
    super( aSource );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ITsCollection}&lt;Visumple3&gt; - source collection
   * @throws TsNullArgumentRtException aSource = null
   */
  public Visumples3List( ITsCollection<Visumple3> aSource ) {
    super( aSource );
  }

}
