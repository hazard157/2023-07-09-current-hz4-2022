package com.hazard157.lib.core.excl_done.radioprop;

import static com.hazard157.lib.core.excl_done.radioprop.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.RGB;
import org.toxsoft.core.tsgui.graphics.colors.ETsColor;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;

import com.hazard157.lib.core.utils.IRadioPropEnum;

/**
 * {@link IRadioPropEnum} valeds constants.
 *
 * @author hazard157
 */
public interface IValedRadioPropEnumConstants {

  /**
   * ID of the option {@link #OPDEF_ICON_SIZE}.
   */
  String OPID_ICON_SIZE = VALED_OPID_PREFIX + "hz.ValedRadioPropEnum.IconsSize"; //$NON-NLS-1$

  /**
   * ID of the option {@link #OPDEF_ICON_GAP}.
   */
  String OPID_ICON_GAP = VALED_OPID_PREFIX + "hz.ValedRadioPropEnum.IconGap"; //$NON-NLS-1$

  /**
   * ID of the option {@link #OPDEF_SEL_RECT_COLOR}.
   */
  String OPID_SEL_RECT_COLOR = VALED_OPID_PREFIX + "hz.ValedRadioPropEnum.SelRectColor"; //$NON-NLS-1$

  /**
   * Enum constant illustrative icon size in the line.<br>
   * Type: {@link EAtomicType#VALOBJ} of {@link EIconSize}<br>
   * Usage: the icon size determines the size of the widget.<br>
   * Default: {@link EIconSize#IS_24X24}
   */
  IDataDef OPDEF_ICON_SIZE = DataDef.create( OPID_ICON_SIZE, EAtomicType.VALOBJ, //
      TSID_NAME, STR_N_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_ICON_SIZE, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_24X24 ) //
  );

  /**
   * Gap between icons in the line.<br>
   * Type: {@link EAtomicType#INTEGER}<br>
   * Usage: the gap between icons determines the size of the widget and thikness of the selection rectangle border.<br>
   * Default: 3
   */
  IDataDef OPDEF_ICON_GAP = DataDef.create( OPID_ICON_GAP, EAtomicType.INTEGER, //
      TSID_NAME, STR_N_ICON_GAP, //
      TSID_DESCRIPTION, STR_D_ICON_GAP, //
      TSID_DEFAULT_VALUE, avInt( 3 ) //
  );

  /**
   * Gap between icons in the line.<br>
   * Type: {@link EAtomicType#VALOBJ} of {@link RGB}<br>
   * Usage: the RGB color of the selection rectangle.<br>
   * Default: {@link ETsColor#DARK_RED}
   */
  IDataDef OPDEF_SEL_RECT_COLOR = DataDef.create( OPID_SEL_RECT_COLOR, EAtomicType.VALOBJ, //
      TSID_NAME, STR_N_SEL_RECT_COLOR, //
      TSID_DESCRIPTION, STR_D_SEL_RECT_COLOR, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.DARK_RED.rgb() ) //
  );

}
