package com.hazard157.lib.core.excl_done.partman;

import static com.hazard157.lib.core.excl_done.partman.ITsResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.e4.ui.workbench.modeling.EPartService.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsPartStackManager} implementation.
 *
 * @author hazard157
 */
public class TsPartStackManager
    implements ITsPartStackManager {

  private final IStringMapEdit<MPart> stackParts = new StringMap<>();

  private final MApplication  application;
  private final EModelService modelService;
  private final EPartService  partService;
  private final MPartStack    partStack;

  /**
   * Constructor.
   *
   * @param aWinContext {@link IEclipseContext} - windows context
   * @param aPartStackId String - part stack ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such part stack found
   */
  public TsPartStackManager( IEclipseContext aWinContext, String aPartStackId ) {
    TsNullArgumentRtException.checkNulls( aWinContext, aPartStackId );
    modelService = aWinContext.get( EModelService.class );
    TsInternalErrorRtException.checkNull( modelService );
    partService = aWinContext.get( EPartService.class );
    TsInternalErrorRtException.checkNull( partService );
    application = aWinContext.get( MApplication.class );
    TsInternalErrorRtException.checkNull( application );
    //
    partStack = (MPartStack)modelService.find( aPartStackId, application );
    if( partStack == null ) {
      throw new TsItemNotFoundRtException( FMT_ERR_NO_SUCH_PART_STACK, aPartStackId );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsPartStackManager
  //

  @Override
  public MPartStack getPartStack() {
    return partStack;
  }

  @Override
  public MPart findPart( String aPartId ) {
    return listManagedParts().findByKey( aPartId );
  }

  @Override
  public IStringMap<MPart> listManagedParts() {
    // удалим из stackParts вью, который были закрыты
    IStringListEdit llClosedPartIds = new StringArrayList();
    for( String pid : stackParts.keys() ) {
      MPart part = stackParts.getByKey( pid );
      if( part.getObject() == null ) { // отсутствие содержимого - значит закрыт
        llClosedPartIds.add( pid );
      }
    }
    for( String pid : llClosedPartIds ) {
      stackParts.removeByKey( pid );
    }
    //
    return stackParts;
  }

  @Override
  public MPart createPart( PartInfo aInfo ) {
    TsNullArgumentRtException.checkNull( aInfo );
    if( listManagedParts().hasKey( aInfo.partId() ) ) {
      throw new TsItemAlreadyExistsRtException( FMT_ERR_PART_ALREADY_EXISTS, aInfo.partId() );
    }
    MPart part = MBasicFactory.INSTANCE.createPart();
    part.setElementId( aInfo.partId() );
    part.setLabel( aInfo.getLabel() );
    part.setTooltip( aInfo.getTooltip() );
    part.setIconURI( aInfo.getIconUri() );
    part.setContributionURI( aInfo.getContributionUri() );
    part.setCloseable( aInfo.isCloseable() );
    part.getTags().add( EPartService.REMOVE_ON_HIDE_TAG );
    partStack.getChildren().add( part );
    MPart createdPart = partService.showPart( part, PartState.ACTIVATE );
    stackParts.put( aInfo.partId(), createdPart );
    return createdPart;
  }

}
