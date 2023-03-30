package com.hazard157.psx.common.stuff.svin;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.valobj.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Хранитель обхектов типа {@link Svin}.
 *
 * @author hazard157
 */
public class SvinKeeper
    extends AbstractEntityKeeper<Svin> {

  /**
   * Идентификатор регистрации хранителя в {@link TsValobjUtils#registerKeeper(String, IEntityKeeper)}.
   */
  public static final String KEEPER_ID = "Svin"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хрпанителя.
   */
  public static final IEntityKeeper<Svin> KEEPER = new SvinKeeper();

  private SvinKeeper() {
    super( Svin.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, Svin aEntity ) {
    aSw.writeAsIs( aEntity.episodeId() );
    aSw.writeSeparatorChar();
    aSw.writeAsIs( aEntity.cameraId() );
    aSw.writeSeparatorChar();
    SecintKeeper.KEEPER.write( aSw, aEntity.interval() );
  }

  @Override
  protected Svin doRead( IStrioReader aSr ) {
    String epId = aSr.readIdName();
    aSr.ensureSeparatorChar();
    String camId = null;
    camId = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    Secint in = null;
    in = SecintKeeper.KEEPER.read( aSr );
    char ch = aSr.peekChar( EStrioSkipMode.SKIP_BYPASSED );
    IFrame frame = IFrame.NONE;
    if( ch == CHAR_ITEM_SEPARATOR ) {
      aSr.ensureSeparatorChar();
      frame = FrameKeeper.KEEPER.read( aSr );
    }
    return new Svin( epId, camId, in, frame );
  }

}
