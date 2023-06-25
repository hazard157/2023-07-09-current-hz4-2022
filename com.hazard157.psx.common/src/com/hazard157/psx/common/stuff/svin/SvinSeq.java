package com.hazard157.psx.common.stuff.svin;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.secint.*;

/**
 * {@link ISvinSeqEdit} implementation.
 *
 * @author hazard157
 */
public final class SvinSeq
    implements ISvinSeqEdit {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ISvinSeq> KEEPER =
      new AbstractEntityKeeper<>( ISvinSeq.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISvinSeq aEntity ) {
          StrioUtils.writeCollection( aSw, EMPTY_STRING, aEntity.svins(), SvinKeeper.KEEPER );
        }

        @Override
        protected ISvinSeq doRead( IStrioReader aSr ) {
          IList<Svin> ll = StrioUtils.readCollection( aSr, EMPTY_STRING, SvinKeeper.KEEPER );
          return new SvinSeq( ll );
        }
      };

  private final GenericChangeEventer    eventer;
  private final INotifierListEdit<Svin> svinsList;
  private final IListReorderer<Svin>    reorderer;

  /**
   * Constructor.
   */
  public SvinSeq() {
    eventer = new GenericChangeEventer( this );
    svinsList = new NotifierListEditWrapper<>( new ElemArrayList<>( 100 ) );
    reorderer = new ListReorderer<>( svinsList );
    svinsList.addCollectionChangeListener( eventer );
  }

  /**
   * Constructor.
   *
   * @param aSvins {@link IList}&lt;{@link Svin}&gt; - initial content
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SvinSeq( IList<Svin> aSvins ) {
    this();
    svinsList.addAll( aSvins );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // ISvinListEdit
  //

  @Override
  public IListEdit<Svin> svins() {
    return svinsList;
  }

  @Override
  public IListReorderer<Svin> reorderer() {
    return reorderer;
  }

  @Override
  public IList<Svin> listByEpisode( String aEpisodeId ) {
    TsNullArgumentRtException.checkNull( aEpisodeId );
    IListEdit<Svin> ll = new ElemArrayList<>( svinsList.size() );
    for( Svin s : svinsList ) {
      if( s.episodeId().equals( aEpisodeId ) ) {
        ll.add( s );
      }
    }
    return ll;
  }

  @Override
  public IStringList listEpisodeIds() {
    IStringListEdit ll = new StringArrayList();
    for( Svin s : svinsList ) {
      if( !ll.hasElem( s.episodeId() ) ) {
        ll.add( s.episodeId() );
      }
    }
    return ll;
  }

  @Override
  public IList<Svin> listByCamera( String aCameraId ) {
    TsNullArgumentRtException.checkNull( aCameraId );
    IListEdit<Svin> ll = new ElemArrayList<>( svinsList.size() );
    for( Svin s : svinsList ) {
      if( s.cameraId().equals( aCameraId ) ) {
        ll.add( s );
      }
    }
    return ll;
  }

  @Override
  public IStringList listCameraIds() {
    IStringListEdit ll = new StringArrayList();
    for( Svin s : svinsList ) {
      if( !ll.hasElem( s.cameraId() ) ) {
        ll.add( s.cameraId() );
      }
    }
    return ll;
  }

  @Override
  public IList<Svin> listByInterval( Secint aInterval, boolean aCutSvins ) {
    TsNullArgumentRtException.checkNull( aInterval );
    IListEdit<Svin> ll = new ElemArrayList<>( svinsList.size() );
    for( Svin s : svinsList ) {
      if( s.interval().intersects( aInterval ) ) {
        if( aCutSvins ) {
          Secint cutInterval = Secint.intersection( s.interval(), aInterval );
          Svin cutSvin = new Svin( s.episodeId(), s.cameraId(), cutInterval, s.frame() );
          ll.add( cutSvin );
        }
        else {
          ll.add( s );
        }
      }
    }
    return ll;
  }

}
