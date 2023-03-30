package com.hazard157.lib.core.glib.pgviewer;

import static com.hazard157.lib.core.glib.pgviewer.ITsResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Константы настройки просмотрщика {@link IPicsGridViewer}.
 *
 * @author goga
 */
public interface IPicsGridViewerConstants {

  /**
   * Признак показа подписей под изображениями.
   */
  IDataDef OP_IS_LABELS_SHOWN = DataDef.create( "isLabelsShown", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_LABELS_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_LABELS_SHOWN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Признак показа втрой строки подписи под изображение.
   */
  IDataDef OP_IS_LABEL2_SHOWN = DataDef.create( "isLabel2Shown", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_LABEL2_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_LABEL2_SHOWN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Признак показа всплывающих подсказок к изображению.
   */
  IDataDef OP_IS_TOOLTIPS_SHOWN = DataDef.create( "isTooltipsShown", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_IS_TOOLTIPS_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_TOOLTIPS_SHOWN, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Размер отображаемых миниатюр по умолчанию.
   */
  IDataDef OP_DEFAULT_THUMB_SIZE = DataDef.create( "defaultThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_DEFAULT_THUMB_SIZE, //
      TSID_DESCRIPTION, STR_D_DEFAULT_THUMB_SIZE, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 )//
  );

  /**
   * Цвет границы вокруг выделенного элемента.
   */
  IDataDef OP_SELECTION_BORDER_COLOR = DataDef.create( "selectionBorderColor", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_SELECTION_BORDER_COLOR, //
      TSID_DESCRIPTION, STR_D_SELECTION_BORDER_COLOR, //
      TSID_KEEPER_ID, RGBKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.DARK_GRAY.rgb() )//
  );

}
