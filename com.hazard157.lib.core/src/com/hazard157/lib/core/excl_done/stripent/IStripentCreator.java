package com.hazard157.lib.core.excl_done.stripent;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * {@link IStripent} entities creation factory.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IStripentCreator<T extends IStripent> {

  /**
   * Creates an instance.
   *
   * @param aId String - identifier
   * @param aParams {@link IOptionSet} - initial value of {@link IStripent#params()}
   * @return &lt;T&gt; - the new instance
   */
  T create( String aId, IOptionSet aParams );

  /**
   * Returns the entity class.
   *
   * @return {@link Class}&lt;T&gt; - the entity class
   */
  Class<T> entityClass();

}
