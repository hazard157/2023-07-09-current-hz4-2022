package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

import com.hazard157.psx.common.stuff.frame.*;

/**
 * Хранитель объектов типа {@link SceneInfo}.
 *
 * @author hazard157
 */
public class SceneInfoKeeper
    extends AbstractEntityKeeper<SceneInfo> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<SceneInfo> KEEPER = new SceneInfoKeeper();

  private SceneInfoKeeper() {
    super( SceneInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, SceneInfo aEntity ) {
    aSw.writeQuotedString( aEntity.name() );
    aSw.writeSeparatorChar();
    Frame.KEEPER.write( aSw, aEntity.frame() );
  }

  @Override
  protected SceneInfo doRead( IStrioReader aSr ) {
    String name = aSr.readQuotedString();
    aSr.ensureSeparatorChar();
    IFrame frame = Frame.KEEPER.read( aSr );
    return new SceneInfo( name, frame );
  }

}
