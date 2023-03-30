package com.hazard157.psx24.catnote.m5.note;

import static com.hazard157.psx24.catnote.m5.INbNotebookM5Constants.*;
import static com.hazard157.psx24.catnote.m5.note.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.catnote.main.*;

/**
 * {@link INbNote} M5 model.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public class NbNoteM5Model
    extends M5Model<INbNote> {

  public static final IM5AttributeFieldDef<INbNote> ID = new M5StdFieldDefId<>();

  public static final IM5AttributeFieldDef<INbNote> NAME = new M5StdFieldDefName<>();

  public static final IM5AttributeFieldDef<INbNote> DESCRIPTION = new M5StdFieldDefDescription<>() {

    @Override
    protected void doInit() {
      params().setInt( OPDEF_VERTICAL_SPAN, 10 );
    }

  };

  public static final IM5SingleLookupFieldDef<INbNote, ENbNoteKind> KIND =
      new M5SingleLookupFieldDef<>( FID_NB_NOTE_KIND, MID_NB_NOTE_KIND ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_NOTE_KIND, STR_D_NOTE_KIND );
          setFlags( M5FF_COLUMN );
          setDefaultValue( ENbNoteKind.MISC );
        }

        protected ENbNoteKind doGetFieldValue( INbNote aEntity ) {
          return aEntity.kind();
        }

      };

  public static IM5FieldDef<INbNote, INbCategory> CATEGORY_ID =
      new M5SingleLookupKeyFieldDef<>( FID_NB_NOTE_CATEGORY_ID, MID_NB_CATEGORY, FID_ID, String.class ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_NOTE_CATEGORY, STR_D_NOTE_CATEGORY );
          setFlags( M5FF_COLUMN );
          // FIXME GOGA надо сделать каркас ValedM5LifecycleManagedEntityFieldEditor
          // FIXME setEditorFactory();
        }

        protected INbCategory doGetFieldValue( INbNote aEntity ) {
          INbNotebook nb = tsContext().get( INbNotebook.class );
          return nb.rootCategory().items().findByKey( aEntity.categoryId() );
        }

        protected String doGetFieldValueName( INbNote aEntity ) {
          INbCategory c = getFieldValue( aEntity );
          return c != null ? c.id() : TsLibUtils.EMPTY_STRING;
        }

      };

  /**
   * Constructor.
   */
  public NbNoteM5Model() {
    super( MID_NB_NOTE, INbNote.class );
    setNameAndDescription( STR_N_M5M_NOTE, STR_D_M5M_NOTE );
    addFieldDefs( ID, KIND, CATEGORY_ID, NAME, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<INbNote> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<INbNote> aItemsProvider, IM5LifecycleManager<INbNote> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<INbNote> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<INbNote> doCreateDefaultLifecycleManager() {
    INbNotebook nb = tsContext().get( INbNotebook.class );
    TsInternalErrorRtException.checkNull( nb );
    return new NbNoteM5LifecycleManager( this, nb );
  }

  @Override
  protected IM5LifecycleManager<INbNote> doCreateLifecycleManager( Object aMaster ) {
    return new NbNoteM5LifecycleManager( this, INbNotebook.class.cast( aMaster ) );
  }

}
