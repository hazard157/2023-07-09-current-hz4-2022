package com.hazard157.psx24.core.m5.camera;

import static com.hazard157.psx24.core.m5.camera.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx.proj3.cameras.*;
import com.hazard157.psx.proj3.sourcevids.*;

/**
 * Модель сущностей типа {@link Camera}.
 *
 * @author hazard157
 */
public class CameraM5Model
    extends M5Model<Camera> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "psx.Camera"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #IS_CAM_AVAILABLE}.
   */
  public static final String FID_IS_CAM_AVAILABLE = "IsCamAvailable"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #USE_COUNT}.
   */
  public static final String FID_USE_COUNT = "UseCount"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #CAMERA_KIND}.
   */
  public static final String FID_CAMERA_KIND = "CameraKind"; //$NON-NLS-1$

  /**
   * Атрибут {@link Camera#id()}.
   */
  public static final M5AttributeFieldDef<Camera> ID = new M5AttributeFieldDef<>( FID_ID, DDEF_IDPATH ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_CAM_ID, STR_D_CAM_ID );
      setFlags( M5FF_COLUMN | M5FF_INVARIANT );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Camera aEntity ) {
      return avStr( aEntity.id() );
    }

    @Override
    protected Image doGetFieldValueIcon( Camera aEntity, EIconSize aIconSize ) {
      String iconId;
      if( aEntity.isCamAvailable() ) {
        iconId = aEntity.kind().iconId();
      }
      else {
        iconId = aEntity.kind().iconIdDimmed();
      }
      return iconManager().loadStdIcon( iconId, aIconSize );
    }

  };

  /**
   * Атрибут {@link Camera#isCamAvailable()}.
   */
  public final M5AttributeFieldDef<Camera> IS_CAM_AVAILABLE =
      new M5AttributeFieldDef<>( FID_IS_CAM_AVAILABLE, DDEF_TS_BOOL ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_CAM_IS_STILL_AVAILABLE, STR_D_CAM_IS_STILL_AVAILABLE );
          setFlags( M5FF_COLUMN );
          setDefaultValue( AV_FALSE );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Camera aEntity ) {
          return avBool( aEntity.isCamAvailable() );
        }

      };

  /**
   * Поле - сколько исходников снято камерой.
   */
  public final M5AttributeFieldDef<Camera> USE_COUNT = new M5AttributeFieldDef<>( FID_USE_COUNT, DDEF_INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_CAM_USE_COUNT, STR_D_CAM_USE_COUNT );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      setDefaultValue( AV_0 );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Camera aEntity ) {
      IUnitSourceVideos unitSourceVideos = tsContext().get( IUnitSourceVideos.class );
      return avInt( unitSourceVideos.listCameraSourceVideos( aEntity.id() ).size() );
    }

    @Override
    protected String doGetFieldValueName( Camera aEntity ) {
      IUnitSourceVideos unitSourceVideos = tsContext().get( IUnitSourceVideos.class );
      if( unitSourceVideos.listCameraSourceVideos( aEntity.id() ).isEmpty() ) {
        return "-"; //$NON-NLS-1$
      }
      return super.doGetFieldValueName( aEntity );
    }

  };

  /**
   * Атрибут {@link Camera#nmName()}.
   */
  public final M5AttributeFieldDef<Camera> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_NAME ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_CAM_NAME, STR_D_CAM_NAME );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Camera aEntity ) {
      return avStr( aEntity.nmName() );
    }

  };

  /**
   * Атрибут {@link Camera#description()}.
   */
  public final M5AttributeFieldDef<Camera> DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DDEF_DESCRIPTION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_CAM_DESCRIPTION, STR_D_CAM_DESCRIPTION );
          setFlags( M5FF_DETAIL );
          params().setValueIfNull( ValedStringText.OPID_IS_MULTI_LINE, AV_TRUE );
          params().setValueIfNull( OPID_IS_HEIGHT_FIXED, AV_FALSE );
          params().setValueIfNull( OPID_VERTICAL_SPAN, avInt( 3 ) );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Camera aEntity ) {
          return avStr( aEntity.description() );
        }

      };

  /**
   * Атрибут {@link Camera#kind()}.
   */
  public static IM5AttributeFieldDef<Camera> CAMERA_KIND = new M5AttributeFieldDef<>( FID_CAMERA_KIND, VALOBJ, //
      TSID_NAME, STR_N_CAMERA_KIND, //
      TSID_DESCRIPTION, STR_D_CAMERA_KIND, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL ), //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjEnumCombo.FACTORY_NAME, //
      TSID_KEEPER_ID, ECameraKind.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ECameraKind.GENERIC ) //
  ) {

    @Override
    protected void doInit() {
      valedRefs().put( IValedEnumConstants.REFID_ENUM_CLASS, ECameraKind.class );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Camera aEntity ) {
      return avValobj( aEntity.kind() );
    }

    @Override
    protected Image doGetFieldValueIcon( Camera aEntity, EIconSize aIconSize ) {
      String iconId;
      if( aEntity.isCamAvailable() ) {
        iconId = aEntity.kind().iconId();
      }
      else {
        iconId = aEntity.kind().iconIdDimmed();
      }
      return iconManager().loadStdIcon( iconId, aIconSize );
    }

  };

  /**
   * Конструктор.
   */
  public CameraM5Model() {
    super( MODEL_ID, Camera.class );
    setNameAndDescription( STR_N_M5M_CAMERA, STR_N_M5M_CAMERA );
    addFieldDefs( ID, CAMERA_KIND, IS_CAM_AVAILABLE, USE_COUNT, NAME, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<Camera>() {

      @Override
      protected IM5CollectionPanel<Camera> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<Camera> aItemsProvider, IM5LifecycleManager<Camera> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<Camera> mpc =
            new PanelUnitCameras( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<Camera> doCreateDefaultLifecycleManager() {
    IUnitCameras unitCameras = tsContext().get( IUnitCameras.class );
    return new CamerasLifecycleManager( this, unitCameras );
  }

  @Override
  protected IM5LifecycleManager<Camera> doCreateLifecycleManager( Object aMaster ) {
    return new CamerasLifecycleManager( this, IUnitCameras.class.cast( aMaster ) );
  }

}
