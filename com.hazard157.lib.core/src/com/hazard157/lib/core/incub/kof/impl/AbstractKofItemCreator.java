package com.hazard157.lib.core.incub.kof.impl;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.incub.fs.*;

/**
 * {@link IKofItemCreator} abstract base class.
 *
 * @author hazard157
 * @param <T> - type of T subclass
 */
public abstract class AbstractKofItemCreator<T extends OptedFile>
    implements IKofItemCreator<T> {

  private final Class<T> itemClass;

  /**
   * Constructor.
   *
   * @param aItemClass {@link Class}&lt;T&gt; - the item class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractKofItemCreator( Class<T> aItemClass ) {
    itemClass = TsNullArgumentRtException.checkNull( aItemClass );
  }

  // ------------------------------------------------------------------------------------
  // IKofItemCreator
  //

  @Override
  final public Class<T> itemClass() {
    return itemClass;
  }

  @Override
  final public T create( File aFile ) {
    TsNullArgumentRtException.checkNull( aFile );
    T instance = doCreate( aFile );
    TsInternalErrorRtException.checkNull( instance );
    return instance;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract T doCreate( File aFile );

}
