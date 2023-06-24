package com.hazard157.lib.core.excl_done.kwmark.m5;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.m5.model.IM5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5Model;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;

import com.hazard157.lib.core.excl_done.kwmark.manager.*;

/**
 * M5-model of {@link String} as on of the keywords {@link IKeywordManager}.
 *
 * @author hazard157
 */
public class KeywordM5Model
    extends M5Model<String> {

  /**
   * Mode ID.
   */
  public static final String MODEL_ID = HZ_ID + ".Keyword"; //$NON-NLS-1$

  /**
   * Field ID of {@link #KEYWORD}.
   */
  public static final String FID_KEYWORD = "keyword"; //$NON-NLS-1$

  /**
   * Attribute: keyword itself.
   */
  public static final IM5AttributeFieldDef<String> KEYWORD =
      new M5AttributeFieldDef<>( FID_KEYWORD, EAtomicType.STRING ) {

        protected IAtomicValue doGetFieldValue( String aEntity ) {
          return avStr( aEntity );
        }

      };

  /**
   * Constructor.
   */
  public KeywordM5Model() {
    super( MODEL_ID, String.class );
    addFieldDefs( KEYWORD );
  }

}
