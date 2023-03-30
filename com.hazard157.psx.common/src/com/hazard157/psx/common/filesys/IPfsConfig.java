package com.hazard157.psx.common.filesys;

import java.io.*;

/**
 * PSX file system configuration constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPfsConfig {

  File EPISODES_ROOT_DIR              = new File( "/home/hmade/episodes/" );             //$NON-NLS-1$
  File EPISODE_NONSEC_FRAMES_ROOT_DIR = new File( "/home/.psx/episodes/frames-nonsec" ); //$NON-NLS-1$

  String EPSUBDIR_FRAMES_ANIM = "frames-anim";  //$NON-NLS-1$
  String EPSUBDIR_FRAMES_SECS = "frames-still"; //$NON-NLS-1$

}
