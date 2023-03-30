package com.hazard157.psx.proj3.episodes.story;

import static com.hazard157.psx.proj3.episodes.story.IPsxResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Реализация {@link IStory}.
 *
 * @author hazard157
 */
public class Story
    extends AbstractScene
    implements IStory {

  private final GenericChangeEventer eventer;
  private final IEpisode             episode;

  /**
   * Конструктор с инвариантами.
   *
   * @param aEpisode {@link IEpisode} - эпизод-владелец
   * @param aIn {@link Secint} - интервал эпизода в виде [0,x)
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException интервал не начинается с 0
   */
  public Story( IEpisode aEpisode, Secint aIn ) {
    super( null, aIn, new SceneInfo( STR_N_STORY, IFrame.NONE ) );
    eventer = new GenericChangeEventer( this );
    episode = TsNullArgumentRtException.checkNull( aEpisode );
    TsIllegalArgumentRtException.checkTrue( aIn.start() != 0 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IKeepableEntity
  //

  private static final String KW_STORY = "Story"; //$NON-NLS-1$

  private static void writeScene( IStrioWriter aSw, IScene aS ) {
    aSw.writeChar( CHAR_SET_BEGIN );
    SecintKeeper.KEEPER.write( aSw, aS.interval() );
    aSw.writeSeparatorChar();
    SceneInfoKeeper.KEEPER.write( aSw, aS.info() );
    aSw.writeSeparatorChar();
    if( !aS.childScenes().isEmpty() ) {
      aSw.writeChar( CHAR_ARRAY_BEGIN );
      aSw.incNewLine();
      for( int i = 0, count = aS.childScenes().size(); i < count; i++ ) {
        IScene s = aS.childScenes().values().get( i );
        writeScene( aSw, s );
        if( i < count - 1 ) {
          aSw.writeSeparatorChar();
          aSw.writeEol();
        }
      }
      aSw.decNewLine();
      aSw.writeChar( CHAR_ARRAY_END );
    }
    else {
      aSw.writeChars( CHAR_ARRAY_BEGIN, CHAR_ARRAY_END );
    }
    aSw.writeChar( CHAR_SET_END );
  }

  private static void readScene( IStrioReader aSr, AbstractScene aParent ) {
    aSr.ensureChar( CHAR_SET_BEGIN );
    Secint in = SecintKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    SceneInfo sceneInfo = SceneInfoKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    Scene newScene = (Scene)aParent.addScene( in, sceneInfo );
    if( aSr.readArrayBegin() ) {
      do {
        readScene( aSr, newScene );
      } while( aSr.readArrayNext() );
    }
    aSr.ensureChar( CHAR_SET_END );
  }

  @Override
  public void write( IStrioWriter aSw ) {
    aSw.writeAsIs( KW_STORY );
    aSw.writeChars( CHAR_EQUAL, CHAR_SET_BEGIN );
    if( !childScenes().isEmpty() ) {
      aSw.incNewLine();
      for( int i = 0, count = childScenes().size(); i < count; i++ ) {
        IScene s = childScenes().values().get( i );
        writeScene( aSw, s );
        if( i < count - 1 ) {
          aSw.writeSeparatorChar();
          aSw.writeEol();
        }
      }
      aSw.decNewLine();
    }
    aSw.writeChar( CHAR_SET_END );
  }

  @Override
  public void read( IStrioReader aSr ) {
    aSr.ensureString( KW_STORY );
    aSr.ensureChar( CHAR_EQUAL );
    if( aSr.readSetBegin() ) {
      do {
        readScene( aSr, this );
      } while( aSr.readSetNext() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEpisodeIdable
  //

  @Override
  public String episodeId() {
    return episode.id();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IStory
  //

  // @Override
  // public Episode episode() {
  // return episode;
  // }

  @Override
  public void setSecint( Secint aIn ) {
    internalSetSecint( aIn );
  }

  @Override
  public void clear() {
    if( !childScenes().isEmpty() ) {
      internalClear();
      scenesChanged();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса AbstractScene
  //

  @Override
  protected Story getRoot() {
    return this;
  }

  void scenesChanged() {
    eventer.fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducer
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IFrameable
  //

  @Override
  public IFrame frame() {
    return episode.frame();
  }

}
