package com.hazard157.psx24.core.m5.plane;

import static com.hazard157.psx24.core.m5.plane.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * Модель сущностей типа {@link PlaneGuide}.
 *
 * @author goga
 */
public class PlaneGuideM5Model
    extends M5Model<PlaneGuide> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "psx.PlaneGuide"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #IS_NATURALLY_LONG}.
   */
  public static final String FID_IS_NATURALLY_LONG = "isNaturallyLong"; //$NON-NLS-1$

  /**
   * Атрибут {@link PlaneGuide#name()}
   */
  public static final M5AttributeFieldDef<PlaneGuide> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_NAME ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FIELD_NAME, STR_D_FIELD_NAME );
    }

    @Override
    protected IAtomicValue doGetFieldValue( PlaneGuide aEntity ) {
      return avStr( aEntity.name() );
    }
  };

  /**
   * Поле {@link PlaneGuide#cameraId()}
   */
  public static final PsxM5CameraIdFieldDef<PlaneGuide> CAMERA_ID = new PsxM5CameraIdFieldDef<>();

  /**
   * Поле {@link PlaneGuide#frame()}
   */
  public static final PsxM5FrameFieldDef<PlaneGuide> FRAME = new PsxM5FrameFieldDef<>();

  /**
   * Атрибут {@link PlaneGuide#isNaturallyLong()}
   */
  public static final M5AttributeFieldDef<PlaneGuide> IS_NATURALLY_LONG =
      new M5AttributeFieldDef<>( FID_IS_NATURALLY_LONG, DDEF_TS_BOOL ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_FIELD_IS_NATURALLY_LONG, STR_D_FIELD_IS_NATURALLY_LONG );
        }

        @Override
        protected IAtomicValue doGetFieldValue( PlaneGuide aEntity ) {
          return avBool( aEntity.isNaturallyLong() );
        }
      };

  static class LifecycleManager
      extends M5LifecycleManager<PlaneGuide, Object> {

    public LifecycleManager( IM5Model<PlaneGuide> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    private static PlaneGuide makeEntity( IM5Bunch<PlaneGuide> aValues ) {
      String name = NAME.getFieldValue( aValues ).asString();
      String cameraId = CAMERA_ID.getFieldValue( aValues );
      IFrame frame = FRAME.getFieldValue( aValues );
      boolean isLong = IS_NATURALLY_LONG.getFieldValue( aValues ).asBool();
      return new PlaneGuide( cameraId, name, frame, isLong );
    }

    @Override
    protected PlaneGuide doCreate( IM5Bunch<PlaneGuide> aValues ) {
      return makeEntity( aValues );
    }

    @Override
    protected PlaneGuide doEdit( IM5Bunch<PlaneGuide> aValues ) {
      return makeEntity( aValues );
    }

    @Override
    protected void doRemove( PlaneGuide aEntity ) {
      // nop
    }

  }

  /**
   * Конструктор.
   */
  public PlaneGuideM5Model() {
    super( MODEL_ID, PlaneGuide.class );
    addFieldDefs( IS_NATURALLY_LONG, NAME, CAMERA_ID, FRAME );
  }

  @Override
  protected IM5LifecycleManager<PlaneGuide> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<PlaneGuide> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
