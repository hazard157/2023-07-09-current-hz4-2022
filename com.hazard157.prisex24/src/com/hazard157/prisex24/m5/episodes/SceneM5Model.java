package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.episodes.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

import com.hazard157.common.quants.secint.*;
import com.hazard157.common.quants.secint.gui.*;
import com.hazard157.common.quants.secint.valed.*;
import com.hazard157.prisex24.*;
import com.hazard157.prisex24.e4.services.currep.*;
import com.hazard157.prisex24.m5.std.*;
import com.hazard157.prisex24.valeds.frames.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.episodes.story.*;

/**
 * Модель объектов типа {@link IScene}.
 *
 * @author hazard157
 */
public class SceneM5Model
    extends M5Model<IScene>
    implements IPsxGuiContextable {

  /**
   * Атрибут - синтерзированное поле, строка номера сцены вида "3.2".
   */
  public static IM5AttributeFieldDef<IScene> SEQ_NO = new M5AttributeFieldDef<>( FID_SEQ_NO, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_SI_SEQ_NO, STR_SI_SEQ_NO_D );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IScene aEntity ) {
      IIntListEdit nums = new IntArrayList();
      IScene s = aEntity;
      while( s != s.root() ) {
        int no = 1 + s.parent().childScenes().values().indexOf( s );
        nums.insert( 0, no );
        s = s.parent();
      }
      StringBuilder sb = new StringBuilder();
      for( int i = 0, count = nums.size(); i < count; i++ ) {
        Integer no = nums.get( i );
        sb.append( no );
        if( i < count - 1 ) {
          sb.append( '.' );
        }
      }
      return avStr( sb.toString() );
    }

  };

  /**
   * Атрибут {@link SceneInfo#name()}
   */
  public static IM5AttributeFieldDef<IScene> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_NAME ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_SI_NAME, STR_SI_NAME_D );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IScene aEntity ) {
      return avStr( aEntity.info().name() );
    }

  };

  /**
   * Поле {@link IScene#interval()}
   */
  public static IM5FieldDef<IScene, Secint> INTERVAL =
      new M5SingleModownFieldDef<>( FID_INTERVAL, SecintM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_SI_INTERVAL, STR_SI_INTERVAL_D );
          setFlags( M5FF_COLUMN );
          setDefaultValue( Secint.ZERO );
          setValedEditor( ValedSecintFactory.FACTORY_NAME );
        }

        @Override
        protected Secint doGetFieldValue( IScene aEntity ) {
          return aEntity.interval();
        }

        @Override
        protected String doGetFieldValueName( IScene aEntity ) {
          return aEntity.interval().fullStr();
        }

      };

  /**
   * Поле {@link IScene#frame()}
   */
  public static IM5FieldDef<IScene, IFrame> FRAME = new PsxM5FrameFieldDef<>();

  /**
   * Поле {@link IScene#frame()} как изображение.
   */
  public final M5FieldDef<IScene, IFrame> FRAME_IMAGE = new M5FieldDef<>( FID_FRAME_IMAGE, IFrame.class, null ) {

    @Override
    protected void doInit() {
      setValedEditor( ValedFrameFactory.FACTORY_NAME );
      setFlags( M5FF_DETAIL | M5FF_HIDDEN );
    }

    @Override
    protected IFrame doGetFieldValue( IScene aEntity ) {
      return aEntity.frame();
    }

    @Override
    protected Image doGetFieldValueIcon( IScene aEntity, EIconSize aIconSize ) {
      EThumbSize thumbSize = EThumbSize.findIncluding( aIconSize ).prevSize();
      TsImage mi = psxService().findThumb( aEntity.frame(), thumbSize );
      if( mi != null ) {
        return mi.image();
      }
      return null;
    }

  };

  static final int                    FULL_DOTS_NUM = 100;
  private static final IAtomicValue[] DOT_STR_AVS   = new IAtomicValue[FULL_DOTS_NUM + 1];
  static {
    for( int num = 0; num <= FULL_DOTS_NUM; num++ ) {
      StringBuilder sb = new StringBuilder();
      for( int i = 0; i < num; i++ ) {
        sb.append( '.' );
      }
      DOT_STR_AVS[num] = avStr( sb.toString() );
    }
  }

  /**
   * Scene duration as dots string proportianal to scenee percentage.
   */
  public final M5AttributeFieldDef<IScene> DUR_PERC_STR = new M5AttributeFieldDef<>( FID_DUR_PERC_STR, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IScene aEntity ) {
      double scDur = aEntity.interval().duration();
      String epId = aEntity.root().episodeId();
      IUnitEpisodes ue = domain().tsContext().get( IUnitEpisodes.class );
      IEpisode episode = ue.items().getByKey( epId );
      // double fullDur = aEntity.root().einterval().duration();
      double fullDur = episode.info().actionInterval().duration();
      int perc = (int)(scDur / fullDur * FULL_DOTS_NUM);
      if( perc < 0 ) {
        perc = 0;
      }
      if( perc > FULL_DOTS_NUM ) {
        perc = FULL_DOTS_NUM;
      }
      return DOT_STR_AVS[perc];
    }

  };

  /**
   * Коснтруктор.
   */
  public SceneM5Model() {
    super( MID_SCENE, IScene.class );
    addFieldDefs( SEQ_NO, INTERVAL,
        // DUR_PERC_STR,
        NAME, FRAME, FRAME_IMAGE );
    setPanelCreator( new M5DefaultPanelCreator<IScene>() {

      @Override
      protected IM5EntityPanel<IScene> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<IScene> aLifecycleManager ) {
        ICurrentEpisodeService ces = aContext.get( ICurrentEpisodeService.class );
        IEpisode episode = ces.current();
        int epDur = episode.info().duration();
        if( epDur == 0 ) {
          epDur = 1;
        }
        M5EntityPanelWithValedsController<IScene> controller =
            new SceneEditPanelController( episode.id(), new Secint( 0, epDur - 1 ) );
        M5EntityPanelWithValeds<IScene> p =
            new M5EntityPanelWithValeds<>( aContext, SceneM5Model.this, false, controller );
        return p;
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IScene> doCreateDefaultLifecycleManager() {
    return null;
  }

  @Override
  protected IM5LifecycleManager<IScene> doCreateLifecycleManager( Object aMaster ) {
    return new SceneLifecycleManager( this, IScene.class.cast( aMaster ) );
  }

}
