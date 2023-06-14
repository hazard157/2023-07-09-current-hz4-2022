package com.hazard157.psx24.core.m5.tags;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface IPsxResource {

  /**
   * {@link TagLifecycleManager}
   */
  String MSG_ERR_TAG_INV_MASTER_OBJ          = Messages.getString( "MSG_ERR_TAG_INV_MASTER_OBJ" );          //$NON-NLS-1$
  String FMT_ERR_CANT_MAKE_CHILDED_NODE_LEAF = Messages.getString( "FMT_ERR_CANT_MAKE_CHILDED_NODE_LEAF" ); //$NON-NLS-1$
  String FMT_ERR_TAG_NAME_ALREADY_EXISTS     = Messages.getString( "FMT_ERR_TAG_NAME_ALREADY_EXISTS" );     //$NON-NLS-1$
  String FMT_WARN_REMOVE_TAG_WITH_CHILDS     = Messages.getString( "FMT_WARN_REMOVE_TAG_WITH_CHILDS" );     //$NON-NLS-1$

  /**
   * {@link TagM5Model}
   */
  String STR_TAG        = Messages.getString( "STR_TAG" );        //$NON-NLS-1$
  String STR_TAG_D      = Messages.getString( "STR_TAG_D" );      //$NON-NLS-1$
  String STR_TAG_ID     = Messages.getString( "STR_TAG_ID" );     //$NON-NLS-1$
  String STR_TAG_ID_D   = Messages.getString( "STR_TAG_ID_D" );   //$NON-NLS-1$
  String STR_TAG_NAME   = Messages.getString( "STR_TAG_NAME" );   //$NON-NLS-1$
  String STR_TAG_NAME_D = Messages.getString( "STR_TAG_NAME_D" ); //$NON-NLS-1$

  /**
   * {@link TagMpc}
   */
  String STR_TMI_BY_GROUP          = Messages.getString( "STR_TMI_BY_GROUP" );          //$NON-NLS-1$
  String STR_TMI_BY_GROUP_D        = Messages.getString( "STR_TMI_BY_GROUP_D" );        //$NON-NLS-1$
  String FMT_ERR_NO_CHILDS_IN_LEAF = Messages.getString( "FMT_ERR_NO_CHILDS_IN_LEAF" ); //$NON-NLS-1$
  String DLG_FMT_NEW_CHILD_TAG_D   = Messages.getString( "DLG_FMT_NEW_CHILD_TAG_D" );   //$NON-NLS-1$
  String DLG_NEW_ROOT_TAG_D        = Messages.getString( "DLG_NEW_ROOT_TAG_D" );        //$NON-NLS-1$
  String DLG_NEW_TAG               = Messages.getString( "DLG_NEW_TAG" );               //$NON-NLS-1$

}
