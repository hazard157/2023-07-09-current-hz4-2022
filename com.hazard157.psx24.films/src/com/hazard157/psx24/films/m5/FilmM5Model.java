package com.hazard157.psx24.films.m5;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.psx24.films.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx24.core.utils.*;
import com.hazard157.psx24.films.e4.services.*;

/**
 * Модель объектов типа {@link File}.
 *
 * @author hazard157
 */
public class FilmM5Model
    extends M5Model<File> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "psx.Film"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #PATH}.
   */
  public static final String FID_PATH = "Path"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #LENGTH}.
   */
  public static final String FID_LENGTH = "Length"; //$NON-NLS-1$

  /**
   * Атрибут {@link File#getName()}.
   */
  public static final M5AttributeFieldDef<File> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_NAME, STR_D_FILE_NAME );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( File aEntity ) {
      return avStr( aEntity.getName() );
    }
  };

  /**
   * Атрибут {@link File#getPath()}.
   */
  public static final M5AttributeFieldDef<File> PATH = new M5AttributeFieldDef<>( FID_PATH, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_PATH, STR_D_FILE_PATH );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_DETAIL );
    }

    @Override
    protected IAtomicValue doGetFieldValue( File aEntity ) {
      return avStr( aEntity.getPath() );
    }
  };

  /**
   * Атрибут {@link File#length()}.
   */
  public final M5AttributeFieldDef<File> LENGTH = new M5AttributeFieldDef<>( FID_LENGTH, DDEF_INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_LENGTH, STR_D_FILE_LENGTH );
      setDefaultValue( AV_0 );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( File aEntity ) {
      return avInt( aEntity.length() );
    }

    @SuppressWarnings( { "boxing" } )
    @Override
    protected String doGetFieldValueName( File aEntity ) {
      long len = aEntity.length();
      return String.format( "%,d", len ); //$NON-NLS-1$
    }

  };

  /**
   * Атрибут {@link File#length()}.
   */
  public final M5AttributeFieldDef<File> DURATION = new M5AttributeFieldDef<>( FID_DURATION, DDEF_INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_DURATION, STR_D_DURATION );
      setDefaultValue( AV_0 );
      setFlags( M5FF_COLUMN | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( File aEntity ) {
      VideoFileDurationCache dc = tsContext().get( VideoFileDurationCache.class );
      return avInt( dc.getVideoDuration( aEntity ) );
    }

    @Override
    protected String doGetFieldValueName( File aEntity ) {
      VideoFileDurationCache dc = tsContext().get( VideoFileDurationCache.class );
      int dur = dc.getVideoDuration( aEntity );
      return HmsUtils.hhmmss( dur );
    }

  };

  /**
   * Конструктор.
   */
  public FilmM5Model() {
    super( MODEL_ID, File.class );
    setNameAndDescription( STR_N_M5M_FILE, STR_D_M5M_FILE );
    addFieldDefs( NAME, PATH, DURATION, LENGTH );
  }

  @Override
  protected IM5LifecycleManager<File> doCreateDefaultLifecycleManager() {
    return new FilmLifecyleManager( this, null );
  }

  @Override
  protected IM5LifecycleManager<File> doCreateLifecycleManager( Object aMaster ) {
    return new FilmLifecyleManager( this, IPsxFilmsService.class.cast( aMaster ) );
  }

}
