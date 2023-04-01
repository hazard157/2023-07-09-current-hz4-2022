package com.hazard157.psx24.intro.utils;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.glib.pgviewer.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx24.core.e4.services.filesys.*;

/**
 * Реализация {@link IPgvVisualsProvider} для сущнстей типа {@link IFrame}
 *
 * @author hazard157
 */
public class FramePgvVisualsProvider
    implements IPgvVisualsProvider<IFrame> {

  /**
   * Признак показа даты эпизода в виде "ГГГГ-ММ-СС".
   */
  public static final int LF_DATE = 0x01;

  /**
   * Признак показа момента кадра в виде "ЧЧ:ММ:СС".
   */
  public static final int LF_HMS = 0x02;

  /**
   * Признак показа идентификатора камеры.
   */
  public static final int LF_CAM = 0x04;

  private final ITsGuiContext tsContext;
  private final int           l1flags;
  private final int           l2flags;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aL1Flags int - содержимое {@link #getLabel1(IFrame)}, флаги LF_XXX собранные по ИЛИ
   * @param aL2Flags int - содержимое {@link #getLabel2(IFrame)}, флаги LF_XXX собранные по ИЛИ
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FramePgvVisualsProvider( ITsGuiContext aContext, int aL1Flags, int aL2Flags ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    l1flags = aL1Flags;
    l2flags = aL2Flags;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // private TsImage getNoneImageForEpisode( EThumbSize aThumbSize ) {
  // int sz = aThumbSize.size();
  // ITsIconManager iconManager = tsContext.get( ITsIconManager.class );
  // Image img = iconManager.loadStdIcon( ICON_PSX_NONE_EPISODE_IMAGE, EIconSize.findIncluding( sz, sz ) );
  // return AnimationSupportUtils.createMultiImage( new Image[] { img }, 0 );
  // }

  private static String formatLabel( IFrame aFrame, int aFlags ) {
    StringBuilder sb = new StringBuilder();
    boolean wasPrev = false;
    // дата эпизода ГГГГ-ММ-ДД
    if( (aFlags & LF_DATE) != 0 ) {
      String s = EpisodeUtils.ymdFromId( aFrame.episodeId() );
      sb.append( s );
      wasPrev = true;
    }
    // время кадра ЧЧ:ММ:СС
    if( (aFlags & LF_HMS) != 0 ) {
      if( wasPrev ) {
        sb.append( ' ' );
      }
      String s = HhMmSsFfUtils.mmssff( aFrame.frameNo() );
      sb.append( s );
      wasPrev = true;
    }
    // идентификатор камеры
    if( (aFlags & LF_CAM) != 0 ) {
      if( wasPrev ) {
        sb.append( ' ' );
      }
      sb.append( aFrame.cameraId() );
      wasPrev = true;
    }
    return sb.toString();
  }

  @Override
  public TsImage getThumb( IFrame aEntity, EThumbSize aThumbSize ) {
    TsNullArgumentRtException.checkNulls( aEntity, aThumbSize );
    IPsxFileSystem fileSystem = tsContext.get( IPsxFileSystem.class );
    File f = fileSystem.findFrameFile( aEntity );
    if( f == null ) {
      return null;
    }
    ITsImageManager imageManager = tsContext.get( ITsImageManager.class );
    return imageManager.findThumb( f, aThumbSize );
  }

  @Override
  public String getLabel1( IFrame aEntity ) {
    return formatLabel( aEntity, l1flags );
  }

  @Override
  public String getLabel2( IFrame aEntity ) {
    return formatLabel( aEntity, l2flags );
  }

  @Override
  public String getTooltip( IFrame aEntity ) {
    return aEntity.toString();
  }

}
