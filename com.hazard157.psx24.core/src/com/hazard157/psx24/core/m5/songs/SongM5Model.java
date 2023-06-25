package com.hazard157.psx24.core.m5.songs;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static com.hazard157.psx24.core.m5.songs.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.rcp.valed.IValedFileConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.songs.*;

/**
 * M5-модель сущности {@link ISong}.
 *
 * @author hazard157
 */
public class SongM5Model
    extends M5Model<ISong> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "psx.Song"; //$NON-NLS-1$

  /**
   * Идентификатор атрибута {@link #FILE_PATH}.
   */
  public static final String FID_FILE_PATH = "filePath"; //$NON-NLS-1$

  /**
   * Атрибут {@link ISong#id()}
   */
  public static final M5AttributeFieldDef<ISong> ID = new M5StdFieldDefId<>( STR_N_SONG_ID, STR_D_SONG_ID );

  /**
   * Атрибут {@link ISong#nmName()}
   */
  public static final M5AttributeFieldDef<ISong> NAME = new M5StdFieldDefName<>( STR_N_SONG_NAME, STR_D_SONG_NAME );

  /**
   * Атрибут {@link ISong#description()}
   */
  public static final M5AttributeFieldDef<ISong> DESCRIPTION =
      new M5StdFieldDefDescription<>( STR_N_SONG_DESCRIPTION, STR_D_SONG_DESCRIPTION );

  /**
   * Атрибут {@link ISong#filePath()}
   */
  public static final M5AttributeFieldDef<ISong> FILE_PATH =
      new M5AttributeFieldDef<>( FID_FILE_PATH, DT_FILE_OPEN_NAME ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_D_SONG_FILE_PATH, STR_D_SONG_FILE_PATH );
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ISong aEntity ) {
          return avStr( aEntity.filePath() );
        }

      };

  /**
   * Атрибут {@link ISong#duration()}
   */
  public static final M5AttributeFieldDef<ISong> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_D_SONG_DURATION, STR_D_SONG_DURATION );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ISong aEntity ) {
          return avInt( aEntity.duration() );
        }

        @Override
        protected String doGetFieldValueName( ISong aEntity ) {
          return HmsUtils.mmss( aEntity.duration() );
        }

      };

  /**
   * Конструктор.
   */
  public SongM5Model() {
    super( MODEL_ID, ISong.class );
    addFieldDefs( ID, DURATION, NAME, DESCRIPTION, FILE_PATH );
    setPanelCreator( new M5DefaultPanelCreator<ISong>() {

      @Override
      protected IM5CollectionPanel<ISong> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISong> aItemsProvider, IM5LifecycleManager<ISong> aLifecycleManager ) {
        MultiPaneComponentModown<ISong> mpc =
            new SongsListMultiPaneComponent( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<ISong> doCreateDefaultLifecycleManager() {
    IUnitSongs master = tsContext().get( IUnitSongs.class );
    TsInternalErrorRtException.checkNull( master );
    return new SongM5LifecycleManager( this, master );
  }

  @Override
  protected IM5LifecycleManager<ISong> doCreateLifecycleManager( Object aMaster ) {
    TsNullArgumentRtException.checkNull( aMaster );
    return new SongM5LifecycleManager( this, IUnitSongs.class.cast( aMaster ) );
  }

}
