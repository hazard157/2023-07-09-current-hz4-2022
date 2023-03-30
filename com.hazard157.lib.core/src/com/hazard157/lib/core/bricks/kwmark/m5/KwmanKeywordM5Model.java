package com.hazard157.lib.core.bricks.kwmark.m5;

import static com.hazard157.lib.core.bricks.kwmark.m5.IHzResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.lib.core.bricks.kwmark.manager.*;

/**
 * {@link IKeywordManager} keywords M5-model.
 *
 * @author goga
 */
public class KwmanKeywordM5Model
    extends M5Model<String> {

  /**
   * Model ID.
   */
  public static final String MODEL_ID = "KwMan.Keyword"; //$NON-NLS-1$

  /**
   * ID of attribute {@link #KEYWORD}.
   */
  public static final String FID_KEYWORD = "keyword"; //$NON-NLS-1$

  /**
   * Keyword as STRING attribute.
   */
  public static final M5AttributeFieldDef<String> KEYWORD = new M5AttributeFieldDef<>( FID_KEYWORD, DDEF_IDPATH ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN | M5FF_INVARIANT );
      setNameAndDescription( STR_N_ATTR_KEYWORD, STR_D_ATTR_KEYWORD );
    }

    protected IAtomicValue doGetFieldValue( String aEntity ) {
      return avStr( aEntity );
    }

  };

  /**
   * Constructor.
   */
  public KwmanKeywordM5Model() {
    super( MODEL_ID, String.class );
    addFieldDefs( KEYWORD );
  }

  @Override
  protected IM5LifecycleManager<String> doCreateLifecycleManager( Object aMaster ) {
    IKeywordManager master = IKeywordManager.class.cast( aMaster );
    return new KwmanKeywordM5LifecycleManager( this, master );
  }

}
