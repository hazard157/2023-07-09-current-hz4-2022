package com.hazard157.lib.core.excl_done.kwmark.m5;

import static com.hazard157.lib.core.excl_done.kwmark.m5.IHzResources.*;
import static com.hazard157.lib.core.excl_done.kwmark.m5.KwmanKeywordM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_done.kwmark.manager.*;

class KwmanKeywordM5LifecycleManager
    extends M5LifecycleManager<String, IKeywordManager> {

  public KwmanKeywordM5LifecycleManager( IM5Model<String> aModel, IKeywordManager aMaster ) {
    super( aModel, true, false, true, true, aMaster );
    TsNullArgumentRtException.checkNulls( aMaster );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<String> aValues ) {
    String kw = aValues.getAsAv( FID_KEYWORD ).asString();
    if( !StridUtils.isValidIdPath( kw ) ) {
      return ValidationResult.error( FMT_ERR_KW_INV_IDPATH, kw );
    }
    IStringList comps = StridUtils.getComponents( kw );
    switch( comps.size() ) {
      case 2: {
        break;
      }
      case 1: {
        return ValidationResult.error( FMT_ERR_KW_IS_IDNAME, kw );
      }
      default:
        return ValidationResult.error( FMT_ERR_KW_GT_2_COMPS, kw );
    }
    if( master().listKeywords().hasElem( kw ) ) {
      return ValidationResult.error( FMT_ERR_KW_GT_2_COMPS, kw );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected String doCreate( IM5Bunch<String> aValues ) {
    String kw = aValues.getAsAv( FID_KEYWORD ).asString();
    return master().define( kw );
  }

  @Override
  protected void doRemove( String aEntity ) {
    master().remove( aEntity );
  }

  @Override
  protected IList<String> doListEntities() {
    return new ElemArrayList<>( master().listAll() );
  }

}
