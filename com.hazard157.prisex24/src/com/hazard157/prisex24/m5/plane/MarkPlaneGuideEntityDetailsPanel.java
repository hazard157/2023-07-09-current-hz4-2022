package com.hazard157.prisex24.m5.plane;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.utils.*;

import com.hazard157.psx.proj3.episodes.proplines.*;

class MarkPlaneGuideEntityDetailsPanel
    extends M5DefaultEntityDetailsPanel<MarkPlaneGuide> {

  MarkPlaneGuideEntityDetailsPanel( ITsGuiContext aContext, IM5Model<MarkPlaneGuide> aModel ) {
    super( aContext, aModel );
  }

  /**
   * Initializes layout for the fields:
   * <ul>
   * <li>{@link MarkPlaneGuideM5Model#INTERVAL}</li>
   * <li>{@link MarkPlaneGuideM5Model#CAMERA_ID}</li>
   * <li>{@link MarkPlaneGuideM5Model#NAME}</li>
   * <li>{@link MarkPlaneGuideM5Model#FRAME}</li>
   * </ul>
   */
  @Override
  protected void doInitLayout() {
    IVecBorderLayout lMain = new VecBorderLayout();
    // left panel (all field editors except image view)
    IVecLadderLayout lLeft = new VecLadderLayout( true );
    for( String fieldId : editors().keys() ) {
      if( fieldId.equals( MarkPlaneGuideM5Model.FRAME.id() ) ) {
        continue;
      }
      IValedControl<?> varEditor = editors().getByKey( fieldId );
      int verSpan = varEditor.params().getInt( OPDEF_VERTICAL_SPAN );
      boolean isPrefWidthFixed = varEditor.params().getBool( OPDEF_IS_WIDTH_FIXED );
      boolean isPrefHeighFixed = varEditor.params().getBool( OPDEF_IS_HEIGHT_FIXED );
      boolean useLabel = !varEditor.params().getBool( OPDEF_NO_FIELD_LABEL );
      EHorAlignment horAl = isPrefWidthFixed ? EHorAlignment.LEFT : EHorAlignment.FILL;
      EVerAlignment verAl = isPrefHeighFixed ? EVerAlignment.TOP : EVerAlignment.FILL;
      IM5FieldDef<?, ?> fd = model().fieldDefs().getByKey( fieldId );
      String label = TsLibUtils.EMPTY_STRING;
      if( !fd.nmName().isEmpty() ) {
        label = fd.nmName() + ": "; //$NON-NLS-1$
      }
      IVecLadderLayoutData layoutData = new VecLadderLayoutData( useLabel, false, verSpan, label, horAl, verAl );
      lLeft.addControl( varEditor, layoutData );
    }
    // right panel - frame view
    IVecBorderLayout lRight = new VecBorderLayout();
    IValedControl<?> varEditor = editors().getByKey( MarkPlaneGuideM5Model.FRAME.id() );
    lRight.addControl( varEditor, EBorderLayoutPlacement.CENTER );
    IVecBoard bRight = new VecBoard();
    bRight.setLayout( lRight );
    // create main board from two
    IVecBoard bLeft = new VecBoard();
    bLeft.setLayout( lLeft );
    lMain.addControl( bLeft, EBorderLayoutPlacement.CENTER );
    lMain.addControl( bRight, EBorderLayoutPlacement.EAST );
    board().setLayout( lMain );
  }

}
