package com.hazard157.psx.common.stuff.frame;

/**
 * Сущность, иллюстрируемая кадром исходного клипа {@link IFrame}.
 *
 * @author hazard157
 */
public interface IFrameable {

  /**
   * Возвращает кадр, иллюстрирующую эту сущность.
   *
   * @return {@link IFrame} - иллюстрирующеий кадр, может быть {@link IFrame#NONE}, но не null
   */
  IFrame frame();

}
