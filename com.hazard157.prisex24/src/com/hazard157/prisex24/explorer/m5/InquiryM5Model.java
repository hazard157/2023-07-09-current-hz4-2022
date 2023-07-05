package com.hazard157.prisex24.explorer.m5;

import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

import com.hazard157.prisex24.explorer.pdu.*;

/**
 * Модель {@link Inquiry} для отображения в списках
 *
 * @author hazard157
 */
public class InquiryM5Model
    extends M5Model<Inquiry> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = PSX_ID_PREFIX + "Inquiry"; //$NON-NLS-1$

  /**
   * Field {@link Inquiry#id()}
   */
  public static final M5AttributeFieldDef<Inquiry> ID = new M5StdFieldDefId<>() {

    protected String doGetFieldValueName( Inquiry aEntity ) {
      return StridUtils.getLast( aEntity.id() );
    }

  };

  /**
   * Field {@link Inquiry#nmName()}
   */
  public static final M5AttributeFieldDef<Inquiry> NAME = new M5StdFieldDefName<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( M5FF_COLUMN );
    }

  };

  /**
   * Field {@link Inquiry#description()}
   */
  public static final M5AttributeFieldDef<Inquiry> DESCRIPTION = new M5StdFieldDefDescription<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( M5FF_COLUMN );
    }

  };

  /**
   * Конструктор.
   */
  public InquiryM5Model() {
    super( MODEL_ID, Inquiry.class );
    addFieldDefs( ID, NAME, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<Inquiry>() {

      @Override
      protected IM5CollectionPanel<Inquiry> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<Inquiry> aItemsProvider, IM5LifecycleManager<Inquiry> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_COLUMN_HEADER.setValue( aContext.params(), AV_FALSE );
        MultiPaneComponentModown<Inquiry> mpc = new InquiryMpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<Inquiry> doCreateDefaultLifecycleManager() {
    IUnitExplorer unitExplorer = tsContext().get( IUnitExplorer.class );
    return new InquiryLifecycleManager( this, unitExplorer );
  }

  @Override
  protected IM5LifecycleManager<Inquiry> doCreateLifecycleManager( Object aMaster ) {
    return new InquiryLifecycleManager( this, IUnitExplorer.class.cast( aMaster ) );
  }

}
