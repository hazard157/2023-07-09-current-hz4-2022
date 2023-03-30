package com.hazard157.lib.core.bricks.kwmark;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.txtproj.lib.*;

import com.hazard157.lib.core.bricks.kwmark.m5.*;
import com.hazard157.lib.core.bricks.kwmark.manager.*;
import com.hazard157.lib.core.bricks.kwmark.valeds.*;

/**
 * Plugin quant.
 *
 * @author hazard157
 */
public class QuantKeywordManager
    extends AbstractQuant {

  public static final String UNITID_KEYWORDS = "KeywordsManager"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public QuantKeywordManager() {
    super( QuantKeywordManager.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    //
    ITsProject proj = aAppContext.get( ITsProject.class );
    IKeywordManager kwMan = new KeywordManager();
    aAppContext.set( IKeywordManager.class, kwMan );
    if( proj != null ) {
      proj.registerUnit( UNITID_KEYWORDS, kwMan, true );
    }
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new KeywordM5Model() );
    m5.addModel( new KwmanKeywordM5Model() );
    //
    IValedControlFactoriesRegistry factoriesRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    factoriesRegistry.registerFactory( ValedAvContextKeywords.FACTORY );
  }

}
