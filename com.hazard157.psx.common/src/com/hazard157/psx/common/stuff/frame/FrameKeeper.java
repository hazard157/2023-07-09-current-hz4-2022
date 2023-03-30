package com.hazard157.psx.common.stuff.frame;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

import com.hazard157.psx.common.utils.*;

/**
 * Хранитель объектов типа {@link IFrame}.
 *
 * @author hazard157
 */
public class FrameKeeper
    extends AbstractEntityKeeper<IFrame> {

  /**
   * Идентификатор регистрации хранителя в {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)}.
   */
  public static final String KEEPER_ID = "Frame"; //$NON-NLS-1$

  /**
   * Хранение {@link IFrame} в текстовом представлении.
   */
  public static final IEntityKeeper<IFrame> KEEPER = new FrameKeeper();

  /**
   * Атомарное значение, соответствующее сохраненному {@link IFrame#NONE}.
   */
  public static final IAtomicValue AV_FRAME_NONE = AvUtils.avValobj( IFrame.NONE, KEEPER, KEEPER_ID );

  private FrameKeeper() {
    super( IFrame.class, EEncloseMode.ENCLOSES_BASE_CLASS, IFrame.NONE );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

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

}
