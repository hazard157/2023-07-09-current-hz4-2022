package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.common.quants.secint.gui.ISecintM5Constants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.episodes.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.common.quants.secint.gui.*;
import com.hazard157.common.quants.secint.valed.*;
import com.hazard157.prisex24.cofs.*;
import com.hazard157.prisex24.e4.services.psx.*;
import com.hazard157.prisex24.m5.std.*;
import com.hazard157.prisex24.valeds.frames.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * M5-model of {@link IEpisode}.
 *
 * @author hazard157
 */
public class EpisodeM5Model
    extends M5Model<IEpisode> {

  /**
   * Field {@link IEpisode#episodeId()}.
   */
  public static final M5FieldDef<IEpisode, String> EPISODE_ID = new PsxM5EpisodeIdFieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      setFlags( M5FF_HIDDEN );
    }

  };

  /**
   * Field {@link EpisodeInfo#when()}
   */
  public static final M5AttributeFieldDef<IEpisode> WHEN = new M5AttributeFieldDef<>( FID_WHEN, DT_DATE_DMY ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_EP_WHEN, STR_EP_WHEN_D );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IEpisode aEntity ) {
      return avTimestamp( aEntity.info().when() );
    }

    @Override
    protected String doGetFieldValueName( IEpisode aEntity ) {
      return EpisodeUtils.ymdFromId( aEntity.id() );
    }

    @Override
    protected Image doGetFieldValueIcon( IEpisode aEntity, EIconSize aIconSize ) {
      IPrisex24Service psxServ = tsContext().get( IPrisex24Service.class );
      TsImage mi = psxServ.findThumb( aEntity.frame(), EThumbSize.findIncluding( aIconSize ) );
      if( mi != null ) {
        return mi.image();
      }
      return null;
    }

    protected TsImage doGetFieldValueThumb( IEpisode aEntity, EThumbSize aThumbSize ) {
      ICofsFrames cofsFrames = tsContext().get( ICofsFrames.class );
      File file = cofsFrames.findFrameFile( aEntity.frame() );
      if( file != null ) {
        ITsImageManager imageManager = tsContext().get( ITsImageManager.class );
        return imageManager.findThumb( file, aThumbSize );
      }
      return null;
    }

  };

  /**
   * Field {@link IEpisode#nmName()}
   */
  public static final M5AttributeFieldDef<IEpisode> NAME = new M5StdFieldDefName<>();

  /**
   * Field {@link IEpisode#description()}
   */
  public static final M5AttributeFieldDef<IEpisode> DESCRIPTION = new M5StdFieldDefDescription<>();

  /**
   * Field {@link EpisodeInfo#duration()}.
   */
  public static final M5AttributeFieldDef<IEpisode> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_EP_DURATION, STR_EP_DURATION_D );
          setFlags( M5FF_COLUMN );
          setFlags( M5FF_COLUMN );
          setValedEditor( ValedAvIntHhmmss.FACTORY_NAME );
          params().setBool( ValedAvIntHhmmss.IS_ONLY_MMSS, true );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IEpisode aEntity ) {
          return avInt( aEntity.info().duration() );
        }

        @Override
        protected String doGetFieldValueName( IEpisode aEntity ) {
          return mmss( aEntity.info().duration() );
        }

      };

  /**
   * Field {@link IEpisode#frame()}.
   */
  public static final IM5FieldDef<IEpisode, IFrame> FRAME = new PsxM5FrameFieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      removeFlags( M5FF_DETAIL );
      ValedFrameFactory.OPDEF_THUMB_SIZE.setValue( params(), avValobj( EThumbSize.SZ360 ) );
      IValedControlConstants.OPDEF_NO_FIELD_LABEL.setValue( params(), AV_TRUE );
    }

  };

  /**
   * Ссылка {@link EpisodeInfo#actionInterval()}.
   */
  public static final IM5FieldDef<IEpisode, Secint> ACTION_INTERVAL =
      new M5SingleModownFieldDef<>( FID_ACTION_INTERVAL, SecintM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_EP_ACTION_INTERVAL, STR_EP_ACTION_INTERVAL_D );
          setValedEditor( ValedSecintFactory.FACTORY_NAME );
        }

        @Override
        protected Secint doGetFieldValue( IEpisode aEntity ) {
          return aEntity.info().actionInterval();
        }

        @Override
        protected String doGetFieldValueName( IEpisode aEntity ) {
          Secint in = aEntity.info().actionInterval();
          return mmss( in.start() ) + " - " + mmss( in.end() ); //$NON-NLS-1$
        }
      };

  /**
   * Field {@link EpisodeInfo#place()}.
   */
  public static final M5AttributeFieldDef<IEpisode> PLACE = new M5AttributeFieldDef<>( FID_PLACE, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_DETAIL );
      setNameAndDescription( STR_EP_PLACE, STR_EP_PLACE_D );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IEpisode aEntity ) {
      return avStr( aEntity.info().place() );
    }
  };

  /**
   * Field {@link EpisodeInfo#notes()}.
   */
  public static final M5AttributeFieldDef<IEpisode> DEF_TRAILER_ID =
      new M5AttributeFieldDef<>( FID_DEF_TRAILER_ID, DDEF_STRING ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_EP_DEF_TRAILER_ID, STR_EP_DEF_TRAILER_ID_D );
          setValedEditor( ValedComboSelector.FACTORY_NAME );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IEpisode aEntity ) {
          return avStr( aEntity.info().defaultTrailerId() );
        }
      };

  /**
   * Field {@link EpisodeInfo#notes()}.
   */
  public static final M5AttributeFieldDef<IEpisode> NOTES = new M5AttributeFieldDef<>( FID_NOTE, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_EP_NOTES, STR_EP_NOTES_D );
      params().setValueIfNull( ValedStringText.OPID_IS_MULTI_LINE, AV_TRUE );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IEpisode aEntity ) {
      return avStr( aEntity.info().notes() );
    }
  };

  /**
   * Конструктор.
   */
  public EpisodeM5Model() {
    super( MID_EPISODE, IEpisode.class );
    addFieldDefs( EPISODE_ID, WHEN, DURATION, ACTION_INTERVAL, NAME, PLACE, DESCRIPTION, DEF_TRAILER_ID, NOTES, FRAME );
    setPanelCreator( new M5DefaultPanelCreator<IEpisode>() {

      @Override
      protected IM5EntityPanel<IEpisode> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<IEpisode> aLifecycleManager ) {
        M5EntityPanelWithValedsController<IEpisode> controller = new EpisodeEditPanelController();
        M5EntityPanelWithValeds<IEpisode> p = new M5EntityPanelWithValeds<>( aContext, model(), false, controller );
        return p;
      }

      @Override
      protected IM5EntityPanel<IEpisode> doCreateEntityViewerPanel( ITsGuiContext aContext ) {
        M5EntityPanelWithValedsController<IEpisode> controller = new EpisodeViewerPanelController();
        M5EntityPanelWithValeds<IEpisode> p = new M5EntityPanelWithValeds<>( aContext, model(), true, controller );
        return p;
      }

      @Override
      protected IM5CollectionPanel<IEpisode> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IEpisode> aItemsProvider, IM5LifecycleManager<IEpisode> aLifecycleManager ) {
        MultiPaneComponentModown<IEpisode> mpc = new EpisodeMpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IEpisode> doCreateDefaultLifecycleManager() {
    IUnitEpisodes unitEpisodes = tsContext().get( IUnitEpisodes.class );
    return new EpisodeLifecycleManager( this, unitEpisodes );
  }

  @Override
  protected IM5LifecycleManager<IEpisode> doCreateLifecycleManager( Object aMaster ) {
    return new EpisodeLifecycleManager( this, IUnitEpisodes.class.cast( aMaster ) );
  }

}
