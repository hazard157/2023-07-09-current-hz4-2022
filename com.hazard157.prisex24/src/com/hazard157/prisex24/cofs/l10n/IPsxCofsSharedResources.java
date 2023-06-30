package com.hazard157.prisex24.cofs.l10n;

import com.hazard157.prisex24.cofs.impl.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxCofsSharedResources {

  /**
   * {@link IPsxCofsInternalConstants}
   */
  String STR_N_MASTER_MEDIA_DATE = Messages.getString( "STR_N_MASTER_MEDIA_DATE" ); //$NON-NLS-1$
  String STR_D_MASTER_MEDIA_DATE = Messages.getString( "STR_D_MASTER_MEDIA_DATE" ); //$NON-NLS-1$

  /**
   * {@link PfsMasterMediaExisting}
   */
  String FMT_WARN_INV_MASTER_DATA_DIR   = Messages.getString( "FMT_WARN_INV_MASTER_DATA_DIR" );   //$NON-NLS-1$
  String FMT_ERR_NO_INCIDENT_MASTER_DIR = Messages.getString( "FMT_ERR_NO_INCIDENT_MASTER_DIR" ); //$NON-NLS-1$

  /**
   * {@link CofsFrames}
   */
  String FMT_WARN_NO_FRAMES_ROOT_DIR = Messages.getString( "FMT_WARN_NO_FRAMES_ROOT_DIR" ); //$NON-NLS-1$

  /**
   * {@link CofsFilms}
   */
  String FMT_WARN_NO_SH_FILE     = Messages.getString( "FMT_WARN_NO_SH_FILE" );     //$NON-NLS-1$
  String FMT_CREATING_FILM_THUMB = Messages.getString( "FMT_CREATING_FILM_THUMB" ); //$NON-NLS-1$

}
