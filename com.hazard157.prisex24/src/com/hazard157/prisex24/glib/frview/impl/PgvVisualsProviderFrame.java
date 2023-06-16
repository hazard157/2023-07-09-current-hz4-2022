package com.hazard157.prisex24.glib.frview.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.prisex24.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * {@link ITsVisualsProvider} implementation for {@link IFrame}.
 * <p>
 * The flags <code>LF_XXX</code> determines which component will be displayed by on separate lines in the following
 * order: {@link #LF_HMS}, {@link #LF_DATE}, {@link #LF_CAM}.
 *
 * @author hazard157
 */
public class PgvVisualsProviderFrame
    implements ITsVisualsProvider<IFrame>, IPsxGuiContextable {

  /**
   * The flag to display frame No (as "MM:SS.FF").
   */
  public static final int LF_HMS = 0x02;

  /**
   * The flag to display frame date (as "YYYY-MM-DD").
   */
  public static final int LF_DATE = 0x01;

  /**
   * The flag to display frame camera ID.
   */
  public static final int LF_CAM = 0x04;

  // FIXME add LF_EPISODE

  private final ITsGuiContext tsContext;

  private final int flags;
  private final int nameLinesCount;

  /**
   * Constructor.
   * <p>
   * If any flags argument is 0, then an empty string will be returned by corresponding getLabelX() method.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aFlags int - the ORed LF_XXX flags for {@link #getName(IFrame)}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PgvVisualsProviderFrame( ITsGuiContext aContext, int aFlags ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    flags = aFlags;
    int count = 0;
    for( int bit = 0x0000_0001, i = 0; i < 32; i++, bit <<= 1 ) {
      if( (flags & bit) != 0 ) {
        ++count;
      }
    }
    nameLinesCount = count;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static String formatLabel( IFrame aFrame, int aFlags ) {
    StringBuilder sb = new StringBuilder();
    // the frame No as MM:SS.FF
    if( (aFlags & LF_HMS) != 0 ) {
      String s = HhMmSsFfUtils.mmssff( aFrame.frameNo() );
      sb.append( s );
      sb.append( '\n' );
    }
    // the date as YYYY-MM-DD
    if( (aFlags & LF_DATE) != 0 ) {
      String s = EPsxIncidentKind.EPISODE.id2date( aFrame.episodeId() ).toString();
      sb.append( s );
      sb.append( '\n' );
    }
    // camera ID
    if( (aFlags & LF_CAM) != 0 ) {
      sb.append( aFrame.cameraId() );
      sb.append( '\n' );
    }
    return sb.toString();
  }

  @Override
  public TsImage getThumb( IFrame aEntity, EThumbSize aThumbSize ) {
    return psxService().findThumb( aEntity, aThumbSize );
  }

  @Override
  public String getName( IFrame aEntity ) {
    return formatLabel( aEntity, flags );
  }

  @Override
  public String getDescription( IFrame aEntity ) {
    return aEntity.toString();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the number of lines in the {@link #getName(IFrame)} text.
   *
   * @return int - number of lines in the name text
   */
  public int getNameLinesCount() {
    return nameLinesCount;
  }

  /**
   * Determines what labels are under frames.
   *
   * @return int - ORed bits {@link PgvVisualsProviderFrame}<code>.LF_XXX</code>
   */
  public int getDisplayedInfoFlags() {
    return flags;
  }

  // ------------------------------------------------------------------------------------
  // IPsx26GuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
