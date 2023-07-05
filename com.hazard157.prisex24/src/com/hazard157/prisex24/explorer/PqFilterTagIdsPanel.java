package com.hazard157.prisex24.explorer;

import static com.hazard157.prisex24.explorer.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.explorer.filters.*;
import com.hazard157.prisex24.m5.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Панель настройки фильтра {@link PqFilterTagIds}.
 *
 * @author hazard157
 */
public class PqFilterTagIdsPanel
    extends AbstractFilterPanel {

  private final ValedAvBooleanCheck      valedIsAny;
  private final IM5CollectionPanel<ITag> panel;

  /**
   * Конструктор панели, предназаначенной для использования вне диалога.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aData T - начальные данные для отображения, может быть null
   * @param aContext C - контекст радктирования / показа структры данных T
   * @throws TsNullArgumentRtException aParent или aContext = null
   */
  public PqFilterTagIdsPanel( Composite aParent, ITsSingleFilterParams aData, ITsGuiContext aContext ) {
    super( aParent, aData, aContext, 0 );
    this.setLayout( new BorderLayout() );
    // valedIsAny
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ValedBooleanCheck.OPDEF_TEXT.setValue( ctx.params(), avStr( STR_N_TAGS_IS_ANY ) );
    valedIsAny = new ValedAvBooleanCheck( ctx );
    valedIsAny.setValue( AV_FALSE );
    valedIsAny.createControl( this );
    valedIsAny.getControl().setLayoutData( BorderLayout.NORTH );
    valedIsAny.eventer().addListener( notificationValedControlChangeListener );
    // panel
    ctx = new TsGuiContext( tsContext() );
    IM5Model<ITag> model = m5().getModel( IPsxM5Constants.MID_TAG, ITag.class );
    IRootTag rootTag = tsContext().get( IRootTag.class );
    IM5LifecycleManager<ITag> lm = model.getLifecycleManager( rootTag );
    OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_COLUMN_HEADER.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_CHECKS.setValue( ctx.params(), AV_TRUE );
    panel = model.panelCreator().createCollChecksPanel( ctx, lm.itemsProvider() );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.checkSupport().checksChangeEventer().addListener( notificationGenericChangeListener );
    setMinHeightDisplayRelative( 60 );
  }

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aOwnerDialog {@link TsDialog} - родительский диалог
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PqFilterTagIdsPanel( Composite aParent, TsDialog<ITsSingleFilterParams, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    // panel
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5Model<ITag> model = m5().getModel( IPsxM5Constants.MID_TAG, ITag.class );
    IRootTag rootTag = tsContext().get( IRootTag.class );
    IM5LifecycleManager<ITag> lm = model.getLifecycleManager( rootTag );
    // OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
    // OPDEF_IS_COLUMN_HEADER.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_SUPPORTS_CHECKS.setValue( ctx.params(), AV_TRUE );
    panel = model.panelCreator().createCollChecksPanel( ctx, lm.itemsProvider() );
    panel.createControl( this );
    panel.getControl().setLayoutData( BorderLayout.CENTER );
    panel.checkSupport().checksChangeEventer().addListener( notificationGenericChangeListener );
    // valedIsAny
    ctx = new TsGuiContext( tsContext() );
    ValedBooleanCheck.OPDEF_TEXT.setValue( ctx.params(), avStr( STR_N_TAGS_IS_ANY ) );
    valedIsAny = new ValedAvBooleanCheck( ctx );
    valedIsAny.setValue( AV_FALSE );
    valedIsAny.createControl( this );
    valedIsAny.getControl().setLayoutData( BorderLayout.NORTH );
    valedIsAny.eventer().addListener( notificationValedControlChangeListener );
  }

  IRootTag rootTag() {
    return tsContext().get( IRootTag.class );
  }

  @Override
  protected void doSetDataRecord( ITsSingleFilterParams aData ) {
    if( aData == null || aData == ITsSingleFilterParams.NONE ) {
      resetFilter();
      return;
    }
    TsIllegalArgumentRtException.checkFalse( aData.typeId().equals( PqFilterTagIds.TYPE_ID ) );
    boolean isAny = aData.params().getBool( PqFilterTagIds.OPID_IS_ANY, false );
    valedIsAny.setValue( avBool( isAny ) );
    IStringList tagIds = aData.params().getValobj( PqFilterTagIds.OPID_TAG_IDS, IStringList.EMPTY );
    try {
      panel.checkSupport().checksChangeEventer().pauseFiring();
      panel.checkSupport().setAllItemsCheckState( false );
      for( ITag t : rootTag().allLeafsBelow() ) {
        if( tagIds.hasElem( t.id() ) ) {
          panel.checkSupport().setItemCheckState( t, true );
        }
      }
    }
    finally {
      panel.checkSupport().checksChangeEventer().resumeFiring( true );
    }
  }

  @Override
  protected ITsSingleFilterParams doGetDataRecord() {
    IStringListEdit tagIds = new StringLinkedBundleList();
    for( ITag t : rootTag().allLeafsBelow() ) {
      if( panel.checkSupport().getItemCheckState( t ) ) {
        tagIds.add( t.id() );
      }
    }
    if( tagIds.isEmpty() ) {
      return ITsSingleFilterParams.NONE;
    }
    boolean isAny = valedIsAny.getValue().asBool();
    return PqFilterTagIds.makeFilterParams( tagIds, isAny );
  }

  @Override
  public void resetFilter() {
    panel.checkSupport().setAllItemsCheckState( false );
    valedIsAny.setValue( AV_FALSE );
  }

}
