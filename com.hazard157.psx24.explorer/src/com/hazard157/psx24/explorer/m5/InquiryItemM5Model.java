package com.hazard157.psx24.explorer.m5;

import static com.hazard157.lib.core.quants.secint.m5.ISecintM5Constants.*;
import static com.hazard157.psx24.explorer.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;

import com.hazard157.psx24.explorer.unit.*;

/**
 * Модель элементов {@link Inquiry#items()} типа {@link InquiryItem}.
 *
 * @author hazard157
 */
public class InquiryItemM5Model
    extends M5Model<InquiryItem> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = PSX_ID_PREFIX + "InquiryItem"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #FP}.
   */
  public static final String FID_FP = "fp"; //$NON-NLS-1$

  /**
   * Поле, содержащее саму сущность.
   */
  public static IM5FieldDef<InquiryItem, InquiryItem> FP = new M5FieldDef<>( FID_FP, InquiryItem.class ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_INQUIRY_ITEM_FP, STR_D_INQUIRY_ITEM_FP );
      setDefaultValue( new InquiryItem() );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected InquiryItem doGetFieldValue( InquiryItem aEntity ) {
      return aEntity;
    }

    @Override
    protected String doGetFieldValueName( InquiryItem aEntity ) {
      return aEntity.toString();
    }

  };

  /**
   * Конструктор.
   */
  public InquiryItemM5Model() {
    super( MODEL_ID, InquiryItem.class );
    addFieldDefs( FP );
    setPanelCreator( new M5DefaultPanelCreator<InquiryItem>() {

      @Override
      protected IM5EntityPanel<InquiryItem> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<InquiryItem> aLifecycleManager ) {
        return new InquiryItemEntityPanel( aContext, model(), false );
      }

      @Override
      protected IM5CollectionPanel<InquiryItem> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<InquiryItem> aItemsProvider, IM5LifecycleManager<InquiryItem> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<InquiryItem> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<InquiryItem> doCreateLifecycleManager( Object aMaster ) {
    return new InquiryItemLifecycleManager( this, Inquiry.class.cast( aMaster ) );
  }

}
