package com.hazard157.psx24.core.m5.visumple;

import static com.hazard157.psx24.core.m5.visumple.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.validators.*;
import org.toxsoft.core.tslib.utils.files.*;

import com.hazard157.lib.core.excl_plan.visumple.*;

/**
 * Модель объектов типа {@link Visumple}.
 *
 * @author hazard157
 */
public class VisumpleM5Model
    extends M5Model<Visumple> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "mi.Visumple"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #FILE_NAME}.
   */
  public static final String FID_FILE_NAME = "FileName"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #FILE_PATH}.
   */
  public static final String FID_FILE_PATH = "FilePath"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #NOTES}.
   */
  public static final String FID_NOTES = "Notes"; //$NON-NLS-1$

  /**
   * Атрибут {@link Visumple#filePath()} в виде только имени файла.
   */
  public static final M5AttributeFieldDef<Visumple> FILE_NAME =
      new M5AttributeFieldDef<>( FID_FILE_NAME, DDEF_STRING ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_VS_FILE_PATH, STR_D_VS_FILE_PATH );
          setDefaultValue( AV_STR_EMPTY );
          setFlags( M5FF_COLUMN | M5FF_HIDDEN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Visumple aEntity ) {
          return avStr( TsFileUtils.extractFileName( aEntity.filePath() ) );
        }

      };

  /**
   * Атрибут {@link Visumple#filePath()}
   */
  public static final M5AttributeFieldDef<Visumple> FILE_PATH =
      new M5AttributeFieldDef<>( FID_FILE_PATH, DDEF_STRING ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_VS_FILE_PATH, STR_D_VS_FILE_PATH );
          setDefaultValue( AV_STR_EMPTY );
          validator().addValidator( EmptyStringAvValidator.ERROR_VALIDATOR );
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Visumple aEntity ) {
          return avStr( aEntity.filePath() );
        }

      };

  /**
   * Атрибут {@link Visumple#notes()}
   */
  public static final M5AttributeFieldDef<Visumple> NOTES = new M5AttributeFieldDef<>( FID_NOTES, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_VS_NOTES, STR_D_VS_NOTES );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Visumple aEntity ) {
      return avStr( aEntity.notes() );
    }

  };

  /**
   * Управление жизненным циклом сущностей {@link Visumple}.
   *
   * @author hazard157
   */
  private static class DefaultLifecycleManager
      extends M5LifecycleManager<Visumple, Object> {

    DefaultLifecycleManager( IM5Model<Visumple> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    @Override
    protected Visumple doCreate( IM5Bunch<Visumple> aValues ) {
      String filePath = VisumpleM5Model.FILE_PATH.getFieldValue( aValues ).asString();
      String notes = VisumpleM5Model.NOTES.getFieldValue( aValues ).asString();
      IOptionSetEdit p = new OptionSet();
      p.setStr( FID_NOTES, notes );

      // FIXME params !

      return new Visumple( filePath, p );
    }

    @Override
    protected Visumple doEdit( IM5Bunch<Visumple> aValues ) {
      String filePath = VisumpleM5Model.FILE_PATH.getFieldValue( aValues ).asString();
      String notes = VisumpleM5Model.NOTES.getFieldValue( aValues ).asString();
      IOptionSetEdit p = new OptionSet();

      p.setStr( FID_NOTES, notes );

      // FIXME params !

      return new Visumple( filePath, p );
    }

    @Override
    protected void doRemove( Visumple aEntity ) {
      // nop
    }

  }

  /**
   * Конструктор.
   */
  public VisumpleM5Model() {
    super( MODEL_ID, Visumple.class );
    setNameAndDescription( STR_N_M5M_VISUMPLE, STR_D_M5M_VISUMPLE );
    addFieldDefs( FILE_NAME, FILE_PATH, NOTES );
    setPanelCreator( new VisumpleM5PanelCreator() );
  }

  @Override
  protected IM5LifecycleManager<Visumple> doCreateDefaultLifecycleManager() {
    return new DefaultLifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<Visumple> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

}
