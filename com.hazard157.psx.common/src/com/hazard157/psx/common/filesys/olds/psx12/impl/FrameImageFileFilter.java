package com.hazard157.psx.common.filesys.olds.psx12.impl;

import java.io.*;

import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.files.*;

class FrameImageFileFilter
    extends TsFileFilter {

  public FrameImageFileFilter( String aExtension ) {
    super( EFsObjKind.FILE, new SingleStringList( aExtension ) );
  }

  @Override
  public boolean accept( File aPathName ) {
    if( super.accept( aPathName ) ) {
      return PsxFileSystemUtils.isEpisodeFrameFileName2( aPathName.getName() );
    }
    return false;
  }

}
