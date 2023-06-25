package com.hazard157.lib.core.excl_plan.visumple;

import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Смешиваемый интерфейс редактируемой иллюстрируемой визамплами сущности.
 *
 * @author hazard157
 */
public interface IVisumplableEdit
    extends IVisumplable {

  /**
   * Возвращает список иллюстрации.
   *
   * @param aVisumples {@link IVisumplesList} - список иллюстрации
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setVisumples( IVisumplesList aVisumples );

}
