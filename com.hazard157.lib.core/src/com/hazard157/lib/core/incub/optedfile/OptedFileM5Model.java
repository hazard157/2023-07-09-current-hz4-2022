package com.hazard157.lib.core.incub.optedfile;

import static com.hazard157.lib.core.incub.optedfile.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.EHorAlignment;
import org.toxsoft.core.tsgui.m5.model.IM5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5Model;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base class for M5-model of the {@link OptedFile}.
 * <p>
 * Usage:
 * <ul>
 * <li>subclass this class;</li>
 * <li>add needed additional fields (usually they correspond to the konn options in {@link T#params()};</li>
 * <li>define and use lifecycle manager;</li>
 * <li>optionally define and use panel creator.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - modelled {@link OptedFile} subclass
 */
public class OptedFileM5Model<T extends OptedFile>
    extends M5Model<T> {

  /**
   * ID of the field {@link #DIRECTORY}.
   */
  public static final String FID_DIRECTORY = "directory"; //$NON-NLS-1$

  /**
   * ID of the field {@link #PATH}.
   */
  public static final String FID_PATH = "path"; //$NON-NLS-1$

  /**
   * ID of the field {@link #FILE_SIZE}.
   */
  public static final String FID_FILE_SIZE = "fileSize"; //$NON-NLS-1$

  /**
   * File name (with extension and without path).
   */
  public final IM5AttributeFieldDef<T> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_NAME, STR_D_NAME );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( T aEntity ) {
      return avStr( aEntity.file().getName() );
    }

  };

  /**
   * Directory of the file.
   */
  public final IM5AttributeFieldDef<T> DIRECTORY = new M5AttributeFieldDef<>( FID_DIRECTORY, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_DIRECTORY, STR_D_DIRECTORY );
      setFlags( M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( T aEntity ) {
      return avStr( aEntity.file().getParent() );
    }

  };

  /**
   * File name (with extension and without path).
   */
  public final IM5AttributeFieldDef<T> PATH = new M5AttributeFieldDef<>( FID_PATH, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_PATH, STR_D_PATH );
      setFlags( M5FF_DETAIL | M5FF_READ_ONLY );
    }

    protected IAtomicValue doGetFieldValue( T aEntity ) {
      return avStr( aEntity.file().getAbsolutePath() );
    }

  };

  /**
   * File name (with extension and without path).
   */
  public final IM5AttributeFieldDef<T> FILE_SIZE = new M5AttributeFieldDef<>( FID_FILE_SIZE, DDEF_INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_FILE_SIZE, STR_D_FILE_SIZE );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      params().setValobj( M5_OPDEF_COLUMN_ALIGN, EHorAlignment.RIGHT );
      params().setStr( TSID_FORMAT_STRING, "%,d" ); //$NON-NLS-1$
    }

    protected IAtomicValue doGetFieldValue( T aEntity ) {
      return avInt( aEntity.file().length() );
    }

  };

  /**
   * Constructor.
   *
   * @param aModelId String - the model ID (IDpath)
   * @param aEntityClass {@link Class}&lt;T&gt; - the enetity class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public OptedFileM5Model( String aModelId, Class<T> aEntityClass ) {
    super( aModelId, aEntityClass );
    setNameAndDescription( STR_N_M5M_OPTED_FILE, STR_D_M5M_OPTED_FILE );
    addFieldDefs( NAME, DIRECTORY, PATH, FILE_SIZE );
  }

}
