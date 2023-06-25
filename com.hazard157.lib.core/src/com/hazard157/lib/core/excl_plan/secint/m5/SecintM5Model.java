package com.hazard157.lib.core.excl_plan.secint.m5;

import static com.hazard157.lib.core.IHzLibConstants.*;
import static com.hazard157.lib.core.excl_plan.secint.m5.IPsxResources.*;
import static com.hazard157.lib.core.excl_plan.secint.m5.ISecintM5Constants.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Модель объектов типа {@link Secint}.
 *
 * @author hazard157
 */
public class SecintM5Model
    extends M5Model<Secint> {

  /**
   * Идентификатор модели объектов типа {@link Secint}.
   */
  public static final String MODEL_ID = "com.hazard157.Secint"; //$NON-NLS-1$

  /**
   * Атрибут {@link Secint#start()}.
   */
  public static final M5AttributeFieldDef<Secint> START = new M5AttributeFieldDef<>( FID_START, DT_VIDEO_POSITION ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_SN_START, STR_D_SN_START );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Secint aEntity ) {
      return avInt( aEntity.start() );
    }

    @Override
    protected String doGetFieldValueName( Secint aEntity ) {
      return mmss( aEntity.start() );
    }

  };

  /**
   * Атрибут {@link Secint#end()}.
   */
  public static final M5AttributeFieldDef<Secint> END = new M5AttributeFieldDef<>( FID_END, DT_VIDEO_POSITION ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_SN_END, STR_D_SN_END );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected IAtomicValue doGetFieldValue( Secint aEntity ) {
      return avInt( aEntity.end() );
    }

    @Override
    protected String doGetFieldValueName( Secint aEntity ) {
      return mmss( aEntity.end() );
    }

  };

  /**
   * Атрибут {@link Secint#duration()}.
   */
  public static final M5AttributeFieldDef<Secint> DURATION =
      new M5AttributeFieldDef<>( FID_DURATION, DT_VIDEO_DURATION ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_SN_DURATION, STR_D_SN_DURATION );
          setFlags( M5FF_COLUMN );
        }

        @Override
        protected IAtomicValue doGetFieldValue( Secint aEntity ) {
          return avInt( aEntity.duration() );
        }

        @Override
        protected String doGetFieldValueName( Secint aEntity ) {
          return mmss( aEntity.duration() );
        }

      };

  /**
   * Коснтруктор.
   */
  public SecintM5Model() {
    super( MODEL_ID, Secint.class );
    setNameAndDescription( STR_N_M5M_SECINT, STR_D_M5M_SECINT );
    addFieldDefs( START, END, DURATION );
    setPanelCreator( new M5DefaultPanelCreator<Secint>() {

      @Override
      protected IM5EntityPanel<Secint> doCreateEntityViewerPanel( ITsGuiContext aContext ) {
        return new EditorPanel( aContext, null, model(), true );
      }

      @Override
      protected IM5EntityPanel<Secint> doCreateEntityEditorPanel( ITsGuiContext aContext,
          IM5LifecycleManager<Secint> aLifecycleManager ) {
        return new EditorPanel( aContext, aLifecycleManager, model(), false );
      }

    } );
  }

  @Override
  protected IM5LifecycleManager<Secint> doCreateDefaultLifecycleManager() {
    return new DefaultLifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<Secint> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

  static class EditorPanel
      extends M5EntityPanelWithValeds<Secint> {

    public EditorPanel( ITsGuiContext aContext, IM5LifecycleManager<Secint> aLifecycleManager, IM5Model<Secint> aModel,
        boolean aViewer ) {
      super( aContext, aModel, aViewer );
      setLifecycleManager( aLifecycleManager );
    }

    @Override
    protected void doInitLayout() {
      IVecRowLayout ll = new VecRowLayout( ETsOrientation.HORIZONTAL );
      ll.addControl( editors().getByKey( FID_START ), IVecRowLayoutData.DEFAULT );
      ll.addControl( editors().getByKey( FID_END ), IVecRowLayoutData.DEFAULT );
      ll.addControl( new AbstractSwtLazyWrapper<Label>() {

        @Override
        protected Label doCreateControl( Composite aParent ) {
          Label l = new Label( aParent, SWT.LEFT );
          l.setText( " -> " ); //$NON-NLS-1$
          return l;
        }
      }, IVecRowLayoutData.DEFAULT );
      ll.addControl( editors().getByKey( FID_DURATION ), IVecRowLayoutData.DEFAULT );
      board().setLayout( ll );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<Secint, ?> aFieldDef,
        boolean aEditFinished ) {
      IValedControl<IAtomicValue> vcStart = (IValedControl<IAtomicValue>)editors().getByKey( START.id() );
      IValedControl<IAtomicValue> vcEnd = (IValedControl<IAtomicValue>)editors().getByKey( END.id() );
      IValedControl<IAtomicValue> vcDur = (IValedControl<IAtomicValue>)editors().getByKey( DURATION.id() );
      int start = vcStart.getValue().asInt();
      int end = vcEnd.getValue().asInt();
      int dur = vcDur.getValue().asInt();
      switch( aFieldDef.id() ) {
        case FID_START: {
          if( start < 0 ) {
            start = 0;
          }
          if( start > MAX_MMSS_VALUE ) {
            start = MAX_MMSS_VALUE;
          }
          end = start + dur - 1;
          if( end > MAX_MMSS_VALUE ) {
            end = MAX_MMSS_VALUE;
            dur = end - start + 1;
          }
          break;
        }
        case FID_END: {
          if( end < 0 ) {
            end = 0;
          }
          if( end > MAX_MMSS_VALUE ) {
            end = MAX_MMSS_VALUE;
          }
          if( start > end ) {
            start = end;
          }
          dur = end - start + 1;
          break;
        }
        case FID_DURATION: {
          if( dur < 1 ) {
            dur = 1;
          }
          if( dur > MAX_MMSS_VALUE ) {
            dur = MAX_MMSS_VALUE;
          }
          end = start + dur - 1;
          if( end > MAX_MMSS_VALUE ) {
            end = MAX_MMSS_VALUE;
            if( start > end ) {
              start = end;
            }
            dur = end - start + 1;
          }
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
      vcStart.setValue( avInt( start ) );
      vcEnd.setValue( avInt( end ) );
      vcDur.setValue( avInt( dur ) );
      return true;
    }

  }

  /**
   * Управление жизненным циклом {@link Secint}.
   *
   * @author hazard157
   */
  private static class DefaultLifecycleManager
      extends M5LifecycleManager<Secint, Object> {

    DefaultLifecycleManager( IM5Model<Secint> aModel ) {
      super( aModel, true, true, true, false, null );
    }

    private static ValidationResult check( IM5Bunch<Secint> aValues ) {
      int start = START.getFieldValue( aValues ).asInt();
      int end = END.getFieldValue( aValues ).asInt();
      int dur = DURATION.getFieldValue( aValues ).asInt();
      if( start < 0 ) {
        return ValidationResult.error( FMT_ERR_SECINT_START_LT_0, hhmmss( start ) );
      }
      if( end < 0 ) {
        return ValidationResult.error( FMT_ERR_SECINT_END_LT_0, hhmmss( end ) );
      }
      if( start > end ) {
        return ValidationResult.error( FMT_ERR_SECINT_START_GT_END, hhmmss( start ), hhmmss( end ) );
      }
      if( dur != (end - start + 1) ) {
        return ValidationResult.error( FMT_ERR_SECINT_INV_DUR, hhmmss( dur ), hhmmss( start ), hhmmss( end ) );
      }
      return ValidationResult.SUCCESS;
    }

    private static Secint build( IM5Bunch<Secint> aValues ) {
      int start = START.getFieldValue( aValues ).asInt();
      int end = END.getFieldValue( aValues ).asInt();
      return new Secint( start, end );
    }

    @Override
    protected ValidationResult doBeforeCreate( IM5Bunch<Secint> aValues ) {
      return check( aValues );
    }

    @Override
    protected Secint doCreate( IM5Bunch<Secint> aValues ) {
      return build( aValues );
    }

    @Override
    protected ValidationResult doBeforeEdit( IM5Bunch<Secint> aValues ) {
      return check( aValues );
    }

    @Override
    protected Secint doEdit( IM5Bunch<Secint> aValues ) {
      return build( aValues );
    }

    @Override
    protected ValidationResult doBeforeRemove( Secint aEntity ) {
      return ValidationResult.SUCCESS;
    }

    @Override
    protected void doRemove( Secint aEntity ) {
      // nop
    }

  }
}
