package com.hazard157.lib.core.excl_done.acsupart;

import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.actions.ITsActionDef;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractPart;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base class for UI parts implementing {@link ITsActionsSupporter}.
 *
 * @author hazard157
 */
public abstract class AbstractActSupportUipart
    extends MwsAbstractPart
    implements ITsActionsSupporter {

  private final String                       actionSupportCtxRefId;
  private final IStringMapEdit<ITsActionDef> actionsMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aActionSupportCtxRefId String - ID to {@link ITsActionsSupporter} reference on<code>this</code>
   * @param aInfos {@link ITsActionDef}[] - initial content of {@link #listActions()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument not an IDpath
   */
  public AbstractActSupportUipart( String aActionSupportCtxRefId, ITsActionDef... aInfos ) {
    actionSupportCtxRefId = StridUtils.checkValidIdPath( aActionSupportCtxRefId );
    for( ITsActionDef ainf : aInfos ) {
      actionsMap.put( ainf.id(), ainf );
    }
  }

  /**
   * Constructor for subclass without actual actions support.
   */
  protected AbstractActSupportUipart() {
    actionSupportCtxRefId = TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsActionsSupporter
  //

  @Override
  final public IStringMapEdit<ITsActionDef> listActions() {
    return actionsMap;
  }

  @Override
  public boolean isActionEnabled( String aActionId ) {
    return false;
  }

  @Override
  public boolean processAction( String aActionId ) {
    TsDialogUtils.underDevelopment( getShell() );
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractTsE4Part
  //

  @Override
  final protected void doInit( Composite aParent ) {
    if( !actionSupportCtxRefId.isEmpty() ) {
      getWindowContext().set( actionSupportCtxRefId, this );
    }
    doDoInit( aParent );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает ИД размещения ссылки на {@link ITsActionsSupporter} <code>this</code>
   *
   * @return String - ИД ссылки на {@link ITsActionsSupporter} или пустая строка
   */
  public String getActionSupportCtxRefId() {
    return actionSupportCtxRefId;
  }

  // ------------------------------------------------------------------------------------
  // To be implemented
  //

  protected abstract void doDoInit( Composite aParent );

}
