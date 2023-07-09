package com.hazard157.psx24.catnote.m5;

import static com.hazard157.psx24.catnote.m5.INbNotebookM5Constants.*;
import static com.hazard157.psx24.catnote.m5.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * {@link INbCategory} M5 model.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class NbCategoryM5Model
    extends M5Model<INbCategory> {

  public static final IM5AttributeFieldDef<INbCategory> ID = new M5StdFieldDefId<>();

  public static final IM5AttributeFieldDef<INbCategory> NAME = new M5StdFieldDefName<>();

  public static final IM5AttributeFieldDef<INbCategory> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Constructor.
   */
  public NbCategoryM5Model() {
    super( MID_NB_CATEGORY, INbCategory.class );
    setNameAndDescription( STR_M5M_CATEGORY, STR_M5M_CATEGORY_D );
    addFieldDefs( ID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<INbCategory> doCreateDefaultLifecycleManager() {
    INbNotebook nb = tsContext().get( INbNotebook.class );
    TsInternalErrorRtException.checkNull( nb );
    return new NbCategoryM5LifecycleManager( this, nb );
  }

  @Override
  protected IM5LifecycleManager<INbCategory> doCreateLifecycleManager( Object aMaster ) {
    return new NbCategoryM5LifecycleManager( this, INbNotebook.class.cast( aMaster ) );
  }

}
