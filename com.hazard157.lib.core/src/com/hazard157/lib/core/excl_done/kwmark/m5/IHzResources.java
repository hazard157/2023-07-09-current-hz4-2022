package com.hazard157.lib.core.excl_done.kwmark.m5;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface IHzResources {

  String STR_N_ATTR_KEYWORD = "Keyword";
  String STR_D_ATTR_KEYWORD = "Keyword is IDpath of 2 components, if 1 component it is considered as group";

  String FMT_ERR_KW_INV_IDPATH = "Keyword '%s' must be an IDpath";
  String FMT_ERR_KW_GT_2_COMPS = "Keyword '%s' must must have 2 components";
  String FMT_ERR_KW_IS_IDNAME  = "IDname keyword '%s' is group but group must not be created directly";
  String FMT_ERR_KW_EXISTS     = "Keyword '%s' already exists";

}
