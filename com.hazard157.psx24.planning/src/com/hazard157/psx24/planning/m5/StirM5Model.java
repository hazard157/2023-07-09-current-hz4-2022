package com.hazard157.psx24.planning.m5;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.quants.secint.m5.ISecintM5Constants.*;
import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.pleps.IUnitPlepsConstants.*;
import static com.hazard157.psx24.planning.m5.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.lib.core.legacy.valeds.fileimg.*;
import com.hazard157.psx.proj3.pleps.*;
import com.hazard157.psx24.core.m5.visumple.*;

/**
 * Модель сущностей типа {@link IStir}.
 *
 * @author goga
 */
public class StirM5Model
    extends M5Model<IStir> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = PSX_ID + ".Stir"; //$NON-NLS-1$

  /**
   * Поле: порядковый номе в {@link IPlep#stirs()}, начиная нумерацию с 1.
   */
  public static final M5AttributeFieldDef<IStir> SEQ_NO = new M5AttributeFieldDef<>( FID_SEQ_NO, DDEF_INTEGER ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STIR_SEQ_NO, STR_D_STIR_SEQ_NO );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IStir aEntity ) {
      int index = aEntity.plep().stirs().indexOf( aEntity );
      return avInt( index + 1 );
    }

    @Override
    protected Image doGetFieldValueIcon( IStir aEntity, EIconSize aIconSize ) {
      File file = new File( aEntity.thumbFilePath() );
      ITsImageManager imageManager = tsContext().get( ITsImageManager.class );
      TsImage mi = imageManager.findThumb( file, EThumbSize.findIncluding( aIconSize ) );
      if( mi != null ) {
        return mi.image();
      }
      return null;
    }

  };

  /**
   * Атрибут {@link IStir#duration()}.
   */
  public static final M5AttributeFieldDef<IStir> START = new M5AttributeFieldDef<>( FID_START, DT_VIDEO_POSITION ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STIR_START, STR_D_STIR_START );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY | M5FF_HIDDEN );
    }

    private int getStart( IStir aEntity ) {
      int index = aEntity.plep().stirs().indexOf( aEntity );
      if( index < 0 ) {
        return 0;
      }
      int start = 0;
      for( int i = 0; i < index; i++ ) {
        start += aEntity.plep().stirs().get( i ).duration();
      }
      return start;
    }

    @Override
    protected IAtomicValue doGetFieldValue( IStir aEntity ) {
      return avInt( getStart( aEntity ) );
    }

    @Override
    protected String doGetFieldValueName( IStir aEntity ) {
      return mmss( getStart( aEntity ) );
    }

  };

  /**
   * Атрибут {@link IStir#name()}.
   */
  public static final IM5AttributeFieldDef<IStir> NAME = new M5AttributeFieldDef<>( FID_NAME, DDEF_STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_STIR_NAME, STR_D_STIR_NAME );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IStir aEntity ) {
      return avStr( aEntity.name() );
    }

  };

  /**
   * Атрибут {@link IStir#duration()}.
   */
  public static final M5AttributeFieldDef<IStir> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_STIR_DURATION, STR_D_STIR_DURATION );
          setFlags( M5FF_COLUMN );
          setDefaultValue( OP_STIR_DURATION.defaultValue() );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return avInt( aEntity.duration() );
        }

        @Override
        protected String doGetFieldValueName( IStir aEntity ) {
          return mmss( aEntity.duration() );
        }

      };

  /**
   * Атрибут {@link IStir#description()}.
   */
  public static final IM5AttributeFieldDef<IStir> DESCRIPTION =
      new M5AttributeFieldDef<>( FID_DESCRIPTION, DDEF_DESCRIPTION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_STIR_DESCRIPTION, STR_D_STIR_DESCRIPTION );
          setFlags( M5FF_DETAIL );
          params().setInt( OPDEF_VERTICAL_SPAN, 3 );
          params().setBool( OPDEF_IS_HEIGHT_FIXED, true );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return avStr( aEntity.description() );
        }

      };

  /**
   * Атрибут {@link IStir#thumbFilePath()}
   */
  public static final IM5AttributeFieldDef<IStir> THUMB_FILE_PATH =
      new M5AttributeFieldDef<>( FID_THUMB_FILE_PATH, ValedAvValobjFileImage.DT_IMAGEFILE_NAME ) {

        @Override
        protected void doInit() {
          setNameAndDescription( OP_STIR_THUMB_FILE_PATH.nmName(), OP_STIR_THUMB_FILE_PATH.description() );
          setFlags( 0 );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return OP_STIR_THUMB_FILE_PATH.getValue( aEntity.params() );
        }

      };

  /**
   * Скрытое поле - индекс для вставления нового {@link IStir}.
   * <p>
   * Используется для передачи в менеджер ЖЦ предполагаемого места вставления нового IStir.
   */
  public static final IM5AttributeFieldDef<IStir> INSERTION_INDEX =
      new M5AttributeFieldDef<>( OPID_STIR_INSERTION_INDEX, DDEF_INTEGER ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_HIDDEN );
          setDefaultValue( OP_STIR_INSERTION_INDEX.defaultValue() );
        }

        @Override
        protected IAtomicValue doGetFieldValue( IStir aEntity ) {
          return AV_N1;
        }

      };

  /**
   * Поле {@link IStir#visumples()}.
   */
  public static VisumpleM5FieldDef<IStir> VISUMPLES = new VisumpleM5FieldDef<>() {

    @Override
    protected void doInit() {
      super.doInit();
      // removeFlags( M5FF_DETAIL );
    }

  };

  /**
   * Конструктор.
   */
  public StirM5Model() {
    super( MODEL_ID, IStir.class );
    addFieldDefs( SEQ_NO, START, DURATION, NAME, DESCRIPTION, THUMB_FILE_PATH, VISUMPLES, INSERTION_INDEX );
    setPanelCreator( new M5DefaultPanelCreator<IStir>() {

      @Override
      protected IM5CollectionPanel<IStir> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IStir> aItemsProvider, IM5LifecycleManager<IStir> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUMMARY_PANE.setValue( aContext.params(), AV_TRUE );
        OPDEF_DETAILS_PANE_PLACE.setValue( aContext.params(), avValobj( EBorderLayoutPlacement.EAST ) );
        MultiPaneComponentModown<IStir> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager ) {

              @Override
              protected IMpcSummaryPane<IStir> doCreateSummaryPane() {
                return new StirSummaryPane( this );
              }
            };
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<IStir> doCreateDefaultLifecycleManager() {
    return null;
  }

  @Override
  protected IM5LifecycleManager<IStir> doCreateLifecycleManager( Object aMaster ) {
    return new StirLifecycleManager( this, IPlep.class.cast( aMaster ) );
  }

}
