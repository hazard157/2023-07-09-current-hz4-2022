package com.hazard157.lib.core.quants.visumple3;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Смешиваемый интерфейс редактируемой иллюстрируемой визамплами сущности.
 *
 * @author hazard157
 */
public interface IVisumplable3Edit
    extends IVisumplable3 {

  /**
   * Возвращает список иллюстрации.
   *
   * @param aVisumples {@link IVisumples3List} - список иллюстрации
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setVisumples( IVisumples3List aVisumples );

}
