package com.hazard157.prisex24.m5.snippet;

import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.snippet.IPsxResources.*;
import static com.hazard157.prisex24.pdus.snippets.ISnippetConstants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.common.incub.striparldm.*;
import com.hazard157.common.quants.visumple.impl.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.pdus.snippets.*;
import com.hazard157.psx.common.stuff.place.*;

/**
 * M5-model of {@link ISnippet}.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class SnippetM5Model
    extends M5Model<ISnippet>
    implements IPsxGuiContextable {

  public final IM5AttributeFieldDef<ISnippet> ID = new M5StdFieldDefId<>();

  public final IM5AttributeFieldDef<ISnippet> NAME = new M5StdFieldDefName<>();

  public final IM5AttributeFieldDef<ISnippet> DESCRIPTION = new M5StdFieldDefDescription<>();

  public final IM5AttributeFieldDef<ISnippet> PLACE = new PsxPlaceM5FieldDef<>();

  public final IM5AttributeFieldDef<ISnippet> CATEGORY = new M5AttributeFieldDef<>( FID_CATEGORY, OPDEF_CATEGORY, //
      M5_OPDEF_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    @Override
    protected IAtomicValue doGetFieldValue( ISnippet aEntity ) {
      return avStr( aEntity.category() );
    }

  };

  public final IM5AttributeFieldDef<ISnippet> VISUMPLES = new VisumplesM5AttributeFieldDef<>();

  /**
   * Constructor.
   */
  public SnippetM5Model() {
    super( MID_SNIPPET, ISnippet.class );
    addFieldDefs( ID, CATEGORY, NAME, DESCRIPTION, PLACE, VISUMPLES );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<ISnippet> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISnippet> aItemsProvider, IM5LifecycleManager<ISnippet> aLifecycleManager ) {
        IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( aContext.params(),
            avValobj( BorderLayout.EAST ) );
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ISnippet> mpc = new MultiPaneComponentModown<>( aContext, model(), aItemsProvider );
        TreeModeInfo<ISnippet> tmi1 = new TreeModeInfo<>( "ByCateg", //$NON-NLS-1$
            STR_TMI_BY_CATEGORY, STR_TMI_BY_CATEGORY_D, null, new TreeMakerByCategory() );
        mpc.treeModeManager().addTreeMode( tmi1 );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

      @Override
      protected IM5CollectionPanel<ISnippet> doCreateCollViewerPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISnippet> aItemsProvider ) {
        IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( aContext.params(),
            avValobj( BorderLayout.EAST ) );
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ISnippet> mpc = new MultiPaneComponentModown<>( aContext, model(), aItemsProvider );
        TreeModeInfo<ISnippet> tmi1 = new TreeModeInfo<>( "ByCateg", //$NON-NLS-1$
            STR_TMI_BY_CATEGORY, STR_TMI_BY_CATEGORY_D, null, new TreeMakerByCategory() );
        mpc.treeModeManager().addTreeMode( tmi1 );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<ISnippet> doCreateDefaultLifecycleManager() {
    return new StriparM5LifecycleManager<>( this, unitSnippets() );
  }

  @Override
  protected IM5LifecycleManager<ISnippet> doCreateLifecycleManager( Object aMaster ) {
    return new StriparM5LifecycleManager<>( this, IUnitSnippets.class.cast( aMaster ) );
  }

}
