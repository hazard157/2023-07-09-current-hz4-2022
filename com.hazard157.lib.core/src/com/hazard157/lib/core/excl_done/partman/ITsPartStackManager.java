package com.hazard157.lib.core.excl_done.partman;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simplifies {@link MPartStack} usage for dynamic parts management.
 *
 * @author hazard157
 */
public interface ITsPartStackManager {

  /**
   * Returns managed part stack.
   *
   * @return {@link MPartStack} - managed part stack, never is <code>null</code>
   */
  MPartStack getPartStack();

  /**
   * Finds part by ID.
   * <p>
   * Warning: method is earching only managed part from {@link #listManagedParts()}.
   *
   * @param aPartId String - the part ID
   * @return {@link MPart} - found part or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  MPart findPart( String aPartId );

  /**
   * Returns existing managed part (parts created by this manager).
   *
   * @return {@link IStringMap}&lt;{@link MPart}&gt; - map "part ID" - "the part"
   */
  IStringMap<MPart> listManagedParts();

  /**
   * Creates new managed part in {@link #getPartStack()}.
   * <p>
   * Created part becomes the active one.
   * <p>
   * Part is created with {@link EPartService#REMOVE_ON_HIDE_TAG} tag so when close part will be disposed.
   *
   * @param aInfo {@link PartInfo} - information about part to be created
   * @return {@link MPart} - created part
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException part with same ID already exists
   */
  MPart createPart( PartInfo aInfo );

}
