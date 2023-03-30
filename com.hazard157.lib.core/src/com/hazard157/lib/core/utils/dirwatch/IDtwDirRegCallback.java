package com.hazard157.lib.core.utils.dirwatch;

import java.nio.file.*;

/**
 * Informs when {@link DirTreeWatcher} registers directory for monitoring.
 *
 * @author hazard157
 */
public interface IDtwDirRegCallback {

  /**
   * An empty listener singleton.
   */
  IDtwDirRegCallback NONE = aDir -> {
    // nop
  };

  /**
   * Called when direcotry was registered.
   * <p>
   * <b>Warning:</b> method is called from internal thread of {@link DirTreeWatcher}.
   *
   * @param aDir {@link Path} - directory that has just started being monitored
   */
  void onDirRegistered( Path aDir );

}
