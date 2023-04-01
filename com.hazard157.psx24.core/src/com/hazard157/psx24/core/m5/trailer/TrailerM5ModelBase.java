package com.hazard157.psx24.core.m5.trailer;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.quants.secint.m5.ISecintM5Constants.*;
import static com.hazard157.psx24.core.m5.trailer.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.validators.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.legacy.valeds.hms.*;
import com.hazard157.psx.proj3.trailers.*;
import com.hazard157.psx24.core.m5.std.*;

/**
 * Базовая модель объектов типа {@link Trailer}, и для трейлеров одного эпизода, и для справочника трейлеров.
 *
 * @author hazard157
 */
public class TrailerM5ModelBase
    extends M5Model<Trailer> {

  /**
   * Идентификатор поля {@link #PLANNED_DURATION}.
   */
  public static final String FID_PLANNED_DURATION = "PlannedDuration"; //$NON-NLS-1$

  /**
   * идентификатор поля {@link #LOCAL_ID}..
   */
  public static final String FID_LOCAL_ID = "LocalId"; //$NON-NLS-1$

  /**
   * Атрибут {@link Trailer#id()}
   */
  public final M5AttributeFieldDef<Trailer> ID = new M5StdFieldDefId<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( flags() | M5FF_HIDDEN );

    }
  };

  /**
   * Атрибут {@link Trailer#episodeId()}
   */
  public final M5SingleLookupFieldDef<Trailer, String> EPISODE_ID = new PsxM5EpisodeIdFieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( flags() & (~M5FF_COLUMN) );
    }
  };

  /**
   * Атрибут {@link Trailer#localId()}
   */
  public final M5AttributeFieldDef<Trailer> LOCAL_ID = new M5AttributeFieldDef<>( FID_LOCAL_ID, DDEF_IDPATH ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_TR_LOCAL_ID, STR_D_TR_LOCAL_ID );
      setDefaultValue( avStr( IStridable.NONE_ID ) );
      validator().addValidator( IdNameStringAvValidator.IDNAME_VALIDATOR );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Trailer aEntity ) {
      return avStr( aEntity.localId() );
    }
  };

  /**
   * Атрибут {@link Trailer#nmName()}.
   */
  public final M5AttributeFieldDef<Trailer> NAME = new M5StdFieldDefName<>();

  /**
   * Атрибут {@link Trailer#description()}.
   */
  public final M5AttributeFieldDef<Trailer> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Атрибут {@link Trailer#calcDuration()}.
   */
  public M5AttributeFieldDef<Trailer> DURATION = new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_TR_DURATION, STR_D_TR_DURATION );
      setDefaultValue( avInt( 0 ) );
      setValedEditor( ValedAvIntHhmmss.FACTORY_NAME );
      setFlags( M5FF_COLUMN | M5FF_HIDDEN );
    }

    @Override
    protected String doGetFieldValueName( Trailer aEntity ) {
      return HmsUtils.mmss( aEntity.calcDuration() );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Trailer aEntity ) {
      return avInt( aEntity.calcDuration() );
    }
  };

  /**
   * Атрибут {@link Trailer#calcDuration()}.
   */
  public M5AttributeFieldDef<Trailer> PLANNED_DURATION =
      new M5AttributeFieldDef<>( FID_PLANNED_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_TR_PLANNED_DURATION, STR_D_TR_PLANNED_DURATION );
          setDefaultValue( avInt( 300 ) );
          setValedEditor( ValedAvIntHhmmss.FACTORY_NAME );
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected String doGetFieldValueName( Trailer aEntity ) {
          return HmsUtils.mmss( aEntity.info().plannedDuration() );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Trailer aEntity ) {
          return avInt( aEntity.info().plannedDuration() );
        }
      };

  /**
   * Конструктор.
   *
   * @param aModelId String - идентификатор модели
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public TrailerM5ModelBase( String aModelId ) {
    super( aModelId, Trailer.class );
    setNameAndDescription( STR_N_M5M_TRAILER, STR_D_M5M_TRAILER );

    // addFieldDefs( ID, EPISODE_ID, LOCAL_ID, DURATION, PLANNED_DURATION, NAME, DESCRIPTION );

    // setLifecycleManagerCreator( new M5AbstractLifecycleManagerCreator<Trailer, IUnitTrailers>() {
    //
    // @Override
    // protected M5LifecycleManager<Trailer, IUnitTrailers> doCreateDefault() {
    // IUnitTrailers master = domain().appContext().get( IUnitTrailers.class );
    // if( master == null ) {
    // return null;
    // }
    // return doCreate( master );
    // }
    //
    // @Override
    // protected M5LifecycleManager<Trailer, IUnitTrailers> doCreate( IUnitTrailers aMaster ) {
    // return new UnitLifecycleManager( model(), aMaster );
    // }
    // } );

    setPanelCreator( new PanelCreator() );
  }

}
