package com.hazard157.psx24.core.m5.tags;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.m5.tags.IPsxResource.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.rcp.valed.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.validators.*;

import com.hazard157.psx.proj3.tags.*;
import com.hazard157.psx24.core.m5.*;

/**
 * Модель объектов типа {@link ITag}.
 *
 * @author hazard157
 */
public class TagM5Model
    extends M5Model<ITag> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = IPsxM5Constants.MID_TAG;

  /**
   * Идентификатор атрибута {@link #ICON_NAME}.
   */
  public static final String FID_ICON_NAME = "IconName"; //$NON-NLS-1$

  /**
   * Атрибут {@link ITag#id()}.
   */
  public static final M5AttributeFieldDef<ITag> ID = new M5AttributeFieldDef<>( FID_ID, DDEF_IDPATH ) {

    @Override
    protected IAtomicValue doGetFieldValue( ITag aEntity ) {
      return avStr( aEntity.id() );
    }
  };

  /**
   * Атрибут {@link ITag#nmName()}.
   */
  public static final M5AttributeFieldDef<ITag> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_IDNAME ) {

    @Override
    protected IAtomicValue doGetFieldValue( ITag aEntity ) {
      return avStr( aEntity.nmName() );
    }
  };

  /**
   * Атрибут {@link IAvMetaConstants#DDEF_DESCRIPTION} (он же {@link ITag#description()}).
   */
  public static final M5StdFieldDefDataDef<ITag> DESCRIPTION = new M5StdFieldDefDataDef<>( DDEF_DESCRIPTION ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( ITag aEntity ) {
      return dataDef().getValue( aEntity.params() );
    }
  };

  /**
   * Атрибут {@link ITagsConstants#IS_LEAF}.
   */
  public static final M5StdFieldDefDataDef<ITag> IS_LEAF = new M5StdFieldDefDataDef<>( ITagsConstants.IS_LEAF ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( ITag aEntity ) {
      return dataDef().getValue( aEntity.params() );
    }
  };

  /**
   * Атрибут {@link ITagsConstants#IS_MANDATORY}.
   */
  public static final M5StdFieldDefDataDef<ITag> IS_MANDATORY =
      new M5StdFieldDefDataDef<>( ITagsConstants.IS_MANDATORY ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ITag aEntity ) {
          return dataDef().getValue( aEntity.params() );
        }
      };

  /**
   * Атрибут {@link ITagsConstants#IS_RADIO}.
   */
  public static final M5StdFieldDefDataDef<ITag> IS_RADIO = new M5StdFieldDefDataDef<>( ITagsConstants.IS_RADIO ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( ITag aEntity ) {
      return dataDef().getValue( aEntity.params() );
    }
  };

  /**
   * Атрибут {@link ITagsConstants#ICON_NAME}
   */
  public static final M5StdFieldDefDataDef<ITag> ICON_NAME = new M5StdFieldDefDataDef<>( ITagsConstants.ICON_NAME ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( ITag aEntity ) {
      return dataDef().getValue( aEntity.params() );
    }
  };

  /**
   * Атрибут {@link ITagsConstants#IN_RADIO_PRIORITY}
   */
  public static final M5StdFieldDefDataDef<ITag> IN_RADIO_PRIORITY =
      new M5StdFieldDefDataDef<>( ITagsConstants.IN_RADIO_PRIORITY ) {

        @Override
        protected IAtomicValue doGetFieldValue( ITag aEntity ) {
          return dataDef().getValue( aEntity.params() );
        }
      };

  /**
   * Конструктор.
   */
  public TagM5Model() {
    super( MODEL_ID, ITag.class );
    setNameAndDescription( STR_N_TAG, STR_D_TAG );
    ID.setNameAndDescription( STR_N_TAG_ID, STR_D_TAG_ID );
    ID.setDefaultValue( DEFAULT_ID_AV );
    ID.validator().addValidator( IdPathStringAvValidator.IDPATH_VALIDATOR );
    ID.setFlags( M5FF_COLUMN | M5FF_HIDDEN | M5FF_DETAIL );
    NAME.setNameAndDescription( STR_N_TAG_NAME, STR_D_TAG_NAME );
    NAME.setDefaultValue( DEFAULT_NAME_AV );
    NAME.validator().addValidator( IdNameStringAvValidator.IDNAME_VALIDATOR );
    ICON_NAME.setValedEditor( ValedAvStringFile.FACTORY_NAME );
    ICON_NAME.params().setBool( IValedFileConstants.OPID_IS_OPEN_DIALOG, true );
    ICON_NAME.params().setBool( IValedFileConstants.OPID_MUST_EXIST, false );
    ICON_NAME.params().setBool( IValedFileConstants.OPID_IS_DIRECTORY, false );
    addFieldDefs( ID, NAME, DESCRIPTION, IS_LEAF, IS_MANDATORY, IS_RADIO, ICON_NAME, IN_RADIO_PRIORITY );
    setPanelCreator( new M5DefaultPanelCreator<ITag>() {

      @Override
      protected IM5CollectionPanel<ITag> doCreateCollChecksPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ITag> aItemsProvider ) {
        IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
        IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_CHECKS.setValue( aContext.params(), AV_TRUE );
        IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
        IRootTag rootTag = aContext.get( IRootTag.class );
        TreeModeInfo<ITag> tmi1 = new TreeModeInfo<>( "ByGroup", //$NON-NLS-1$
            STR_N_TMI_BY_GROUP, STR_D_TMI_BY_GROUP, ICON_TAGS_LIST, new TreeMakerByGroups( rootTag ) );
        MultiPaneComponentModown<ITag> mpc = new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, null );
        mpc.treeModeManager().addTreeMode( tmi1 );
        mpc.treeModeManager().setCurrentMode( tmi1.id() );
        IM5CollectionPanel<ITag> p = new M5CollectionPanelMpcModownWrapper<>( mpc, true );
        p.setItemsProvider( aItemsProvider );
        return p;
      }

      @Override
      protected IM5CollectionPanel<ITag> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ITag> aItemsProvider, IM5LifecycleManager<ITag> aLifecycleManager ) {
        IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ITag> mpc = new TagMpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

      @Override
      protected IM5CollectionPanel<ITag> doCreateCollViewerPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ITag> aItemsProvider ) {
        IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_FALSE );
        IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ITag> mpc = new TagMpc( aContext, model(), aItemsProvider, null );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, true );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<ITag> doCreateDefaultLifecycleManager() {
    IUnitTags unitTags = tsContext().get( IUnitTags.class );
    return new TagLifecycleManager( this, unitTags.root() );
  }

  @Override
  protected IM5LifecycleManager<ITag> doCreateLifecycleManager( Object aMaster ) {
    return new TagLifecycleManager( this, ITag.class.cast( aMaster ) );
  }

}
