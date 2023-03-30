package com.hazard157.psx.proj3.trailers.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.impl.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.psx.proj3.trailers.*;

/**
 * Реализация {@link IUnitTrailers}.
 *
 * @author hazard157
 */
public class UnitTrailers
    extends AbstractProjDataUnit
    implements IUnitTrailers {

  private static final String KW_STI = "SensibleTrailerInfos"; //$NON-NLS-1$

  private final TrailersSinentManager tsm = new TrailersSinentManager();

  private final INotifierStridablesListEdit<SensibleTrailerInfo> sensibleTrailerInfos =
      new NotifierStridablesListEditWrapper<>( new StridablesList<SensibleTrailerInfo>() );

  /**
   * Конструктор.
   */
  public UnitTrailers() {
    // super( StringUtils.EMPTY_STRING, TrailerKeeper.KEEPER );
    sensibleTrailerInfos.addCollectionChangeListener( collectionChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация AbstractProjDataUnit
  //

  @Override
  protected void doWrite( IStrioWriter aSw ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    aSw.incNewLine();
    tsm.write( aSw );
    aSw.writeEol();
    StrioUtils.writeCollection( aSw, KW_STI, sensibleTrailerInfos, SensibleTrailerInfo.KEEPER, true );
    aSw.decNewLine();
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  protected void doRead( IStrioReader aSr ) {
    genericChangeEventer().pauseFiring();
    try {
      aSr.ensureChar( CHAR_SET_BEGIN );
      tsm.read( aSr );
      sensibleTrailerInfos.setAll( StrioUtils.readCollection( aSr, KW_STI, SensibleTrailerInfo.KEEPER ) );
      aSr.ensureChar( CHAR_SET_END );
    }
    finally {
      genericChangeEventer().resumeFiring( true );
    }
  }

  @Override
  protected void doClear() {
    genericChangeEventer().pauseFiring();
    try {
      tsm.clear();
      sensibleTrailerInfos.clear();
    }
    finally {
      genericChangeEventer().resumeFiring( true );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IUnitTrailer
  //

  @Override
  public IStridablesList<Trailer> listTrailersByEpisode( String aEpisodeId ) {
    TsNullArgumentRtException.checkNull( aEpisodeId );
    IStridablesListBasicEdit<Trailer> map = new SortedStridablesList<>();
    for( Trailer item : tsm.items() ) {
      if( item.episodeId().equals( aEpisodeId ) ) {
        map.add( item );
      }
    }
    return map;
  }

  @Override
  public ISinentManager<Trailer, TrailerInfo> tsm() {
    return tsm;
  }

  @Override
  public INotifierStridablesListEdit<SensibleTrailerInfo> sensibleTrailerInfos() {
    return sensibleTrailerInfos;
  }

}
