package com.hazard157.psx.common.stuff.frame;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.*;

/**
 * {@link IFrame} immutable implementation.
 *
 * @author hazard157
 */
public class Frame
    implements IFrame {

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "Frame"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IFrame> KEEPER =
      new AbstractEntityKeeper<>( IFrame.class, EEncloseMode.ENCLOSES_BASE_CLASS, IFrame.NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IFrame aSf ) {
          aSw.writeAsIs( aSf.episodeId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aSf.cameraId() );
          aSw.writeSeparatorChar();
          HhMmSsFfUtils.writeMmSsFf( aSw, aSf.frameNo() );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aSf.isAnimated() );
        }

        @Override
        protected IFrame doRead( IStrioReader aSr ) {
          String episodeId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String camId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          int frameNo = HhMmSsFfUtils.readMmSsFf( aSr );
          aSr.ensureSeparatorChar();
          boolean isAnimated = aSr.readBoolean();
          return new Frame( episodeId, camId, frameNo, isAnimated );
        }
      };

  /**
   * An atomic value corresponding to the stored {@link IFrame#NONE}.
   */
  public static final IAtomicValue AV_FRAME_NONE = AvUtils.avValobj( IFrame.NONE, KEEPER, KEEPER_ID );

  private final int     hashCode;
  private final String  episodeId;
  private final String  camId;
  private final int     frameNo;
  private final boolean isAnimated;

  // private transient LocalDate date;

  /**
   * Constructor.
   *
   * @param aEpisodeId String - the episode ID or {@link IStridable#NONE_ID}
   * @param aCamId String - the camera ID or {@link IStridable#NONE_ID}
   * @param aFrameNo int - first frame number or -1
   * @param aIsAnimated boolean - the animated sequence flag
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aEpisodeId is invalid episode ID
   * @throws TsIllegalArgumentRtException aCamId не ИД-путь
   */
  public Frame( String aEpisodeId, String aCamId, int aFrameNo, boolean aIsAnimated ) {
    if( !StridUtils.isNullId( aEpisodeId ) ) {
      EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    }
    episodeId = aEpisodeId;
    camId = StridUtils.checkValidIdPath( aCamId );
    frameNo = aFrameNo;
    isAnimated = aIsAnimated;
    hashCode = calcHashCode();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private int calcHashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + episodeId.hashCode();
    result = PRIME * result + cameraId().hashCode();
    result = PRIME * result + frameNo();
    return result;
  }

  /**
   * Returns a human-readable string to identify the frame (for example, in tables).
   *
   * @param aFrame {@link IFrame} - the frame
   * @return String - human-readable string to identify the frame
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "nls" )
  public static String humanReadableString( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    StringBuilder sb = new StringBuilder();
    sb.append( aFrame.episodeId() );
    sb.append( ' ' );
    sb.append( HhMmSsFfUtils.mmssff( aFrame.frameNo() ) );
    sb.append( ' ' );
    sb.append( aFrame.isAnimated() ? "ANIM" : "SOLE" );
    sb.append( ' ' );
    sb.append( aFrame.cameraId() );
    return sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // IFrame
  //

  @Override
  public String episodeId() {
    return episodeId;
  }

  // @Override
  // public LocalDate whenDate() {
  // if( date == null ) {
  // date = EpisodeUtils.episodeId2LocalDate( episodeId );
  // }
  // return date;
  // }

  @Override
  public String cameraId() {
    return camId;
  }

  @Override
  public int frameNo() {
    return frameNo;
  }

  @Override
  public boolean isAnimated() {
    return isAnimated;
  }

  @Override
  public boolean isDefined() {
    return frameNo >= 0 && !episodeId.equals( IStridable.NONE_ID ) && !camId.equals( IStridable.NONE_ID );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof IFrame that ) {
      return frameNo() == that.frameNo() && isAnimated == that.isAnimated() && cameraId().equals( that.cameraId() )
          && episodeId.equals( that.episodeId() );
    }
    return false;
  }

  @Override
  public String toString() {
    return humanReadableString( this );
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( IFrame aThat ) {
    int c = this.episodeId().compareTo( aThat.episodeId() );
    if( c != 0 ) {
      return c;
    }
    c = Integer.compare( this.frameNo(), aThat.frameNo() );
    if( c != 0 ) {
      return c;
    }
    c = this.cameraId().compareTo( aThat.cameraId() );
    if( c != 0 ) {
      return c;
    }
    if( this.isAnimated() == aThat.isAnimated() ) {
      return 0;
    }
    return aThat.isAnimated() ? 1 : -1;
  }

}
