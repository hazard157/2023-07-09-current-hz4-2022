package com.hazard157.lib.core.utils;

import static org.toxsoft.core.tsgui.utils.IMediaFileConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;

/**
 * Misc PSX utility methods.
 *
 * @author hazard157
 */
public class PsxUtils {

  /**
   * Runs default (for PSX) application for specified file, "plays" the file.
   *
   * @param aFile {@link File} - the file
   */
  public static final void runDefaultApp( File aFile ) {
    if( !TsFileUtils.isFileReadable( aFile ) ) {
      return;
    }
    String ext = TsFileUtils.extractExtension( aFile.getName() ).toLowerCase();
    // video
    if( VIDEO_FILE_EXT_LIST.hasElem( ext ) ) {
      TsMiscUtils.runProgram( "smplayer", aFile.getAbsolutePath() ); //$NON-NLS-1$
      return;
    }
    // image
    if( IMAGE_FILE_EXT_LIST.hasElem( ext ) ) {
      TsMiscUtils.runProgram( "gwenview", aFile.getAbsolutePath() ); //$NON-NLS-1$
      return;
    }
    // audio
    if( AUDIO_FILE_EXT_LIST.hasElem( ext ) ) {
      TsMiscUtils.runProgram( "mpv", aFile.getAbsolutePath() ); //$NON-NLS-1$
      return;
    }
    TsMiscUtils.runProgram( "xdg-open", aFile.getAbsolutePath() ); //$NON-NLS-1$
  }

  /**
   * Вычисляет новое значение перемещения виртуального ползунка.
   *
   * @param aAmount {@link ETsCollMove} - значение перемещения
   * @param aMin int - минимальное значение
   * @param aMax int - максимальное значение
   * @param aStep int - значение шага
   * @param aJump int - значение прижка
   * @param aValue int - начальное значение
   * @return int - новое значение в пределах от aMin до aMax включительно
   * @throws TsIllegalArgumentRtException aMin > aMax
   * @throws TsIllegalArgumentRtException aStep < 0
   * @throws TsIllegalArgumentRtException aJump < 0
   */
  @SuppressWarnings( { "nls", "boxing" } )
  public static int displaceValue( ETsCollMove aAmount, int aMin, int aMax, int aStep, int aJump, int aValue ) {
    TsIllegalArgumentRtException.checkTrue( aMin > aMax, "aMin = %d < aMax = %d", aMin, aMax );
    TsIllegalArgumentRtException.checkTrue( aStep < 0, "aStep = %d < 0", aStep );
    TsIllegalArgumentRtException.checkTrue( aJump < 0, "aJump = %d < 0", aJump );
    int value = aValue;
    switch( aAmount ) {
      case NONE:
        break;
      case FIRST:
        value = aMin;
        break;
      case LAST:
        value = aMax;
        break;
      case MIDDLE:
        value = (aMax - aMin) / 2;
        break;
      case PREV:
        value -= aStep;
        break;
      case NEXT:
        value += aStep;
        break;
      case JUMP_PREV:
        value -= aJump;
        break;
      case JUMP_NEXT:
        value += aJump;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    if( value < aMin ) {
      value = aMin;
    }
    if( value > aMax ) {
      value = aMax;
    }
    return value;
  }

  /**
   * No subclassing.
   */
  private PsxUtils() {
    // nop
  }

}
