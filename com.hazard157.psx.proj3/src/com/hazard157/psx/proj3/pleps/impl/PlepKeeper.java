package com.hazard157.psx.proj3.pleps.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx.proj3.pleps.*;

/**
 * Хранитель объектов типа {@link Plep}.
 *
 * @author hazard157
 */
public class PlepKeeper
    extends AbstractEntityKeeper<IPlep> {

  /**
   * Хранение {@link Plep} в текстовом представлении.
   */
  public static final IEntityKeeper<IPlep> KEEPER = new PlepKeeper();

  private static final String KW_STIRS  = "Stirs";  //$NON-NLS-1$
  private static final String KW_TRACKS = "Tracks"; //$NON-NLS-1$

  private PlepKeeper() {
    super( IPlep.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractEntityKeeper
  //

  @Override
  protected void doWrite( IStrioWriter aSw, IPlep aEntity ) {
    aSw.incNewLine();
    // id()
    aSw.writeAsIs( aEntity.id() );
    aSw.writeSeparatorChar();
    aSw.writeEol();
    // info();
    PlepInfoKeeper.KEEPER.write( aSw, aEntity.info() );
    aSw.writeEol();
    // stirs
    StrioUtils.writeCollection( aSw, KW_STIRS, aEntity.stirs(), Stir.KEEPER, true );
    // tracks
    StrioUtils.writeCollection( aSw, KW_TRACKS, aEntity.tracks(), Track.KEEPER, true );
    aSw.decNewLine();
  }

  @Override
  protected IPlep doRead( IStrioReader aSr ) {
    String id = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    PlepInfo info = PlepInfoKeeper.KEEPER.read( aSr );
    Plep p = new Plep( id, info );
    // stirs
    IList<IStir> ll1 = StrioUtils.readCollection( aSr, KW_STIRS, Stir.KEEPER );
    for( IStir s : ll1 ) {
      p.newStir( -1, s.params() );
    }
    // tracks
    IList<ITrack> ll2 = StrioUtils.readCollection( aSr, KW_TRACKS, Track.KEEPER );
    for( ITrack t : ll2 ) {
      p.newTrack( -1, t.songId(), t.interval() );
    }
    return p;
  }

}
