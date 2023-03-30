package com.hazard157.psx24.explorer.m5;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;

import com.hazard157.psx24.explorer.gui.filters.*;
import com.hazard157.psx24.explorer.unit.*;

class InquiryItemEntityPanel
    extends M5AbstractEntityPanel<InquiryItem> {

  PanelAllFiltersSet panel = null;

  public InquiryItemEntityPanel( ITsGuiContext aContext, IM5Model<InquiryItem> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    panel = new PanelAllFiltersSet( aParent, tsContext() );
    panel.genericChangeEventer().addListener( aSource -> fireChangeEvent() );
    return panel;
  }

  @Override
  protected void doSetValues( IM5Bunch<InquiryItem> aBunch ) {
    if( panel == null ) {
      return;
    }
    if( aBunch != null ) {
      panel.setFilterParams( aBunch.get( InquiryItemM5Model.FP ) );
    }
    else {
      panel.resetFilterParans();
    }
  }

  @Override
  protected ValidationResult doCollectValues( IM5BunchEdit<InquiryItem> aBunch ) {
    aBunch.set( InquiryItemM5Model.FP, panel.getInquiryItem() );
    return ValidationResult.SUCCESS;
  }

}
