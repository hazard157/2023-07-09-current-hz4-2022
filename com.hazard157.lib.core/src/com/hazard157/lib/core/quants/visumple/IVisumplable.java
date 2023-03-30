package com.hazard157.lib.core.quants.visumple;

/**
 * Mix-in interface of entities which has {@link IVisumplesList} field.
 *
 * @author hazard157
 */
public interface IVisumplable {

  /**
   * Returns Vvisumples of the entity.
   *
   * @return {@link IVisumplesList} - list of visumples
   */
  IVisumplesList visumples();

}
