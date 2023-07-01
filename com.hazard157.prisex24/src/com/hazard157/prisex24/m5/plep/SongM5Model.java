package com.hazard157.prisex24.m5.plep;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.plep.IPsxResources.*;
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

import com.hazard157.prisex24.m5.*;
import com.hazard157.psx.proj3.songs.*;

/**
 * M5-model of {@link ISong}.
 *
 * @author hazard157
 */
public class SongM5Model
    extends M5Model<ISong> {

  /**
   * Field {@link ISong#id()}
   */
  public static final M5AttributeFieldDef<ISong> ID = new M5StdFieldDefId<>( STR_SONG_ID, STR_SONG_ID_D );

  /**
   * Field {@link ISong#nmName()}
   */
  public static final M5AttributeFieldDef<ISong> NAME = new M5StdFieldDefName<>( STR_SONG_NAME, STR_SONG_NAME_D );

  /**
   * Field {@link ISong#description()}
   */
  public static final M5AttributeFieldDef<ISong> DESCRIPTION =
      new M5StdFieldDefDescription<>( STR_SONG_DESCRIPTION, STR_SONG_DESCRIPTION_D );

  /**
   * Field {@link ISong#filePath()}
   */
  public static final M5AttributeFieldDef<ISong> FILE_PATH =
      new M5AttributeFieldDef<>( FID_FILE_PATH, DT_FILE_OPEN_NAME ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_SONG_FILE_PATH, STR_SONG_FILE_PATH_D );
          setFlags( M5FF_DETAIL );
        }

        @Override
        protected IAtomicValue doGetFieldValue( ISong aEntity ) {
          return avStr( aEntity.filePath() );
        }

      };

  /**
   * Field {@link ISong#duration()}
   */
  public static final M5AttributeFieldDef<ISong> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_SONG_DURATION, STR_SONG_DURATION_D );
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
   * Constructor.
   */
  public SongM5Model() {
    super( IPsxM5Constants.MID_SONG, ISong.class );
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
