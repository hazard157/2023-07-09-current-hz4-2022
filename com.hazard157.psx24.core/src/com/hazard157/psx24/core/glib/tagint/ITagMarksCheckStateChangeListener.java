package com.hazard157.psx24.core.glib.tagint;

import org.toxsoft.core.tsgui.utils.checkstate.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Слушатель изменений в состоянии пометок в дереве {@link TagMarksTreePanel}.
 *
 * @author goga
 */
public interface ITagMarksCheckStateChangeListener {

  /**
   * Вызывается при установке у одного и более ярлыков состояния пометки в {@link ECheckState#CHECKED}.
   * <p>
   * В аргументе - списке ярлыков всегда есть идентификатор хотя бы одного ярлыка. В списке находятся только те ярлыки,
   * у которых изменилось состояние с {@link ECheckState#UNCHECKED} или с {@link ECheckState#GRAYED}.
   *
   * @param aTagIds {@link IStringList} - идентификаторы ярлыков, у коаторых устанвилось состояние
   *          {@link ECheckState#CHECKED}
   */
  void onTagsChecked( IStringList aTagIds );

  /**
   * Вызывается при сбросе у одного и более ярлыков состояния пометки в {@link ECheckState#UNCHECKED}.
   * <p>
   * В аргументе - списке ярлыков всегда есть идентификатор хотя бы одного ярлыка. В списке находятся только те ярлыки,
   * у которых изменилось состояние с {@link ECheckState#CHECKED} или с {@link ECheckState#GRAYED}.
   *
   * @param aTagIds {@link IStringList} - идентификаторы ярлыков, у коаторых устанвилось состояние
   *          {@link ECheckState#UNCHECKED}
   */
  void onTagsUnchecked( IStringList aTagIds );

}
