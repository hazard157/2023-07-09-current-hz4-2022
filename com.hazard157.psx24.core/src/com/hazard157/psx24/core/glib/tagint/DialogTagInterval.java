package com.hazard157.psx24.core.glib.tagint;

import static com.hazard157.psx24.core.glib.tagint.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.lib.core.excl_plan.secint.valed.*;
import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx24.core.m5.*;

/**
 * Диалог выбора ярлыка и интервала.
 *
 * @author hazard157
 */
public class DialogTagInterval {

  static class DialogContent
      extends AbstractTsDialogPanel<Pair<String, Secint>, IEclipseContext> {

    final IM5CollectionPanel<ITag> panelTagId;
    final ValedSecint              valedSecint;
    Text                           txtFilter = null;

    protected DialogContent( Composite aParent, TsDialog<Pair<String, Secint>, IEclipseContext> aOwnerDialog ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      // panelTagId
      IM5Domain m5 = tsContext().get( IM5Domain.class );
      IM5Model<ITag> model = m5.getModel( IPsxM5Constants.MID_TAG, ITag.class );
      ITsGuiContext ctx = new TsGuiContext( tsContext() );
      IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AV_FALSE );
      IUnitTags unitTags = tsContext().get( IUnitTags.class );
      IM5LifecycleManager<ITag> lm = model.getLifecycleManager( unitTags.root() );
      panelTagId = model.panelCreator().createCollViewerPanel( ctx, lm.itemsProvider() );
      panelTagId.createControl( this );
      panelTagId.getControl().setLayoutData( BorderLayout.CENTER );
      // bottomBoard with label and interval editor
      TsComposite bottomBoard = new TsComposite( this );
      bottomBoard.setLayout( new GridLayout( 2, false ) );
      bottomBoard.setLayoutData( BorderLayout.SOUTH );
      Label l = new Label( bottomBoard, SWT.LEFT );
      l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false ) );
      l.setText( STR_L_INTERVAL );
      // valedSecint
      ctx = new TsGuiContext( tsContext() );
      valedSecint = new ValedSecint( ctx );
      valedSecint.createControl( bottomBoard );
      valedSecint.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
      panelTagId.addTsSelectionListener( ( aSource, aSelectedItem ) -> fireContentChangeEvent() );
      valedSecint.eventer().addListener( ( aSource, aEditFinished ) -> fireContentChangeEvent() );
      // setup
      valedSecint.getControl().setFocus();
    }

    // ------------------------------------------------------------------------------------
    //
    //

    @Override
    protected ValidationResult validateData() {
      ITag selTag = panelTagId.selectedItem();
      if( selTag == null ) {
        return ValidationResult.error( MSG_ERR_TAG_ID_MUST_BE_SELECTED );
      }
      if( !selTag.childNodes().isEmpty() ) {
        return ValidationResult.error( FMT_ERR_NON_LEAF_TAG_SELECTED );
      }
      ValidationResult vr = valedSecint.canGetValue();
      if( vr.isError() ) {
        return vr;
      }
      return vr;
    }

    @Override
    protected void doSetDataRecord( Pair<String, Secint> aData ) {
      if( aData == null ) {
        panelTagId.setSelectedItem( null );
        valedSecint.setValue( null );
        return;
      }
      ITag tag = tsContext().get( IUnitTags.class ).root().allNodesBelow().findByKey( aData.left() );
      panelTagId.setSelectedItem( tag );
      valedSecint.setValue( aData.right() );
    }

    @Override
    protected Pair<String, Secint> doGetDataRecord() {
      ITag selTag = panelTagId.selectedItem();
      if( selTag == null ) {
        return null;
      }
      String tagId = selTag.id();
      Secint in = valedSecint.getValue();
      if( tagId == null || in == null ) {
        return null;
      }
      return new Pair<>( tagId, in );
    }

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Выводит диалог выбора ярлыкаи его интервала.
   * <p>
   * Пара {@link Pair}&lt;String, {@link Secint}&gt; содержит ИД ярлыка и интервал пометки ярлыком.
   *
   * @param aContext {@link ITsGuiContext} - контекст приложения
   * @param aInitVals {@link Pair}&lt;String, {@link Secint}&gt; - начальные значения или null
   * @param aAllowedInterval {@link Secint} - допустимый интервал изменения интеравала пометки или null без ограничения
   * @return {@link Pair}&lt;String, {@link Secint}&gt; - введенные значения или null
   */
  public static Pair<String, Secint> select( ITsGuiContext aContext, Pair<String, Secint> aInitVals,
      Secint aAllowedInterval ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<Pair<String, Secint>, IEclipseContext> creator = ( aParent, aOwnerDialog ) -> {
      DialogContent c = new DialogContent( aParent, aOwnerDialog );
      Rectangle shellSize = aContext.get( Shell.class ).getBounds();
      c.setMinimumHeight( (int)(shellSize.y * 0.8) );
      c.valedSecint.setLimits( aAllowedInterval );
      return c;
    };
    TsDialogInfo cdi = new TsDialogInfo( aContext, DLG_C_TAG_INTERVAL, DLG_T_TAG_INTERVAL );
    cdi.setMinSizeShellRelative( 50, 80 );
    TsDialog<Pair<String, Secint>, IEclipseContext> d =
        new TsDialog<>( cdi, aInitVals, aContext.eclipseContext(), creator );
    return d.execData();
  }

}
