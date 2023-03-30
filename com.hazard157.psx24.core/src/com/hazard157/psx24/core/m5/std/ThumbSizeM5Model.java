package com.hazard157.psx24.core.m5.std;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.std.models.enums.*;

/**
 * Модель сущностей типа {@link EThumbSize}.
 *
 * @author goga
 */
public class ThumbSizeM5Model
    extends M5StridableEnumModelBase<EThumbSize> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "ts.tsgui.ThumbSize"; //$NON-NLS-1$

  /**
   * конструктор.
   */
  public ThumbSizeM5Model() {
    super( MODEL_ID, EThumbSize.class );
  }

}
