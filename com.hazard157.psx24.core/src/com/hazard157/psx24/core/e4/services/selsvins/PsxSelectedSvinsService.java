package com.hazard157.psx24.core.e4.services.selsvins;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;

/**
 * {@link IPsxSelectedSvinsService} implementation.
 *
 * @author hazard157
 */
public class PsxSelectedSvinsService
    implements IPsxSelectedSvinsService {

  private final GenericChangeEventer eventer;

  private final IListEdit<Svin> svins = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public PsxSelectedSvinsService() {
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IPsxSelectedSvinsService
  //

  @Override
  public IList<Svin> svins() {
    return svins;
  }

  @Override
  public void setSvins( IList<Svin> aSvins ) {
    TsNullArgumentRtException.checkNull( aSvins );
    if( svins.equals( aSvins ) ) {
      return;
    }
    svins.setAll( aSvins );
    eventer.fireChangeEvent();
  }

  @Override
  public IGenericChangeEventer eventer() {
    return eventer;
  }

}
