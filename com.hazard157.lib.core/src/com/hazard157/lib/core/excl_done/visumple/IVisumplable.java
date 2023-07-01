package com.hazard157.lib.core.excl_done.visumple;

/**
 * Mix-in interface of entities which has {@link IVisumplesList} field.
 *
 * @author hazard157
 */
public interface IVisumplable {

  /**
   * Returns VISUMPLEs of the entity.
   *
   * @return {@link IVisumplesList} - list of VISUMPLEs
   */
  IVisumplesList visumples();

}
