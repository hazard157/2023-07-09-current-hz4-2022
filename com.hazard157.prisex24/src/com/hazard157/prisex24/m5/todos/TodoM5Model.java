package com.hazard157.prisex24.m5.todos;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.m5.IPsxM5Constants.*;
import static com.hazard157.prisex24.m5.todos.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.proj3.todos.*;

/**
 * {@link ITodo} M5 model.
 *
 * @author hazard157
 */
public class TodoM5Model
    extends M5Model<ITodo> {

  /**
   * {@link ITodo#id()}
   */
  public static final IM5AttributeFieldDef<ITodo> ID = new M5AttributeFieldDef<>( FID_ID, INTEGER, //
      TSID_NAME, STR_TD_TODO_ID, //
      TSID_DESCRIPTION, STR_TD_TODO_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_READ_ONLY | M5FF_HIDDEN ) //
  ) {

    protected IAtomicValue doGetFieldValue( ITodo aEntity ) {
      return avInt( aEntity.id() );
    }
  };

  /**
   * {@link ITodo#creationTime()}
   */
  public static final IM5AttributeFieldDef<ITodo> CREATION_TIME =
      new M5AttributeFieldDef<>( FID_CREATION_TIME, TIMESTAMP, //
          TSID_NAME, STR_TD_CREATION_TIME, //
          TSID_DESCRIPTION, STR_TD_CREATION_TIME_D, //
          M5_OPID_FLAGS, avInt( M5FF_COLUMN | M5FF_READ_ONLY ) //
      ) {

        protected IAtomicValue doGetFieldValue( ITodo aEntity ) {
          return avTimestamp( aEntity.id() );
        }

        protected String doGetFieldValueName( ITodo aEntity ) {
          Long time = Long.valueOf( aEntity.creationTime() );
          return String.format( "%tF %tT", time, time ); //$NON-NLS-1$
        }

      };

  /**
   * {@link ITodo#priority()}
   */
  public static final IM5AttributeFieldDef<ITodo> PRIORITY = new M5AttributeFieldDef<>( FID_PRIORITY, VALOBJ ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_TD_PRIORITY, STR_TD_PRIORITY_D );
      setFlags( M5FF_COLUMN );
      setDefaultValue( EPriority.NORMAL.atomicValue() );
    }

    protected IAtomicValue doGetFieldValue( ITodo aEntity ) {
      return aEntity.priority().atomicValue();
    }

  };

  /**
   * {@link ITodo#text()}
   */
  public static final IM5AttributeFieldDef<ITodo> TEXT = new M5AttributeFieldDef<>( FID_TEXT, STRING, //
      TSID_NAME, STR_TD_TEXT, //
      TSID_DESCRIPTION, STR_TD_TEXT_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    protected IAtomicValue doGetFieldValue( ITodo aEntity ) {
      return avStr( aEntity.text() );
    }
  };

  /**
   * {@link ITodo#note()}
   */
  public static final IM5AttributeFieldDef<ITodo> NOTE = new M5AttributeFieldDef<>( FID_NOTE, STRING, //
      TSID_NAME, STR_TD_NOTE, //
      TSID_DESCRIPTION, STR_TD_NOTE_D, //
      IValedControlConstants.OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME, //
      ValedStringText.OPDEF_IS_MULTI_LINE, AV_TRUE, //
      IValedControlConstants.OPID_VERTICAL_SPAN, avInt( 5 ) //
  ) {

    @Override
    protected void doInit() {
      addFlags( M5FF_DETAIL );
    }

    protected IAtomicValue doGetFieldValue( ITodo aEntity ) {
      return avStr( aEntity.note() );
    }
  };

  /**
   * {@link ITodo#isDone()}
   */
  public static final IM5AttributeFieldDef<ITodo> IS_DONE = new M5AttributeFieldDef<>( FID_IS_DONE, BOOLEAN, //
      TSID_NAME, STR_TD_IS_DONE, //
      TSID_DESCRIPTION, STR_TD_IS_DONE_D, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK_AV, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    protected IAtomicValue doGetFieldValue( ITodo aEntity ) {
      return avBool( aEntity.isDone() );
    }
  };

  /**
   * {@link ITodo#relatedTodoIds()}
   */
  public static final IM5MultiLookupKeyFieldDef<ITodo, ITodo> RELATED_TODO_IDS =
      new M5MultiLookupKeyFieldDef<>( FID_RELATED_TODO_IDS, MID_TODO, FID_TODO_ID, Long.class ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_TD_RELATED_TODOS, STR_TD_RELATED_TODOS_D );
          setFlags( M5FF_DETAIL );
          setLookupProvider( new IM5LookupProvider<ITodo>() {

            @Override
            public String getName( ITodo aItem ) {
              Long time = Long.valueOf( aItem.creationTime() );
              return String.format( "%tF %tT - %s", time, time, aItem.text() ); //$NON-NLS-1$
            }

            @Override
            public IList<ITodo> listItems() {
              IUnitTodos ut = tsContext().get( IUnitTodos.class );
              return ut.todos().values();
            }
          } );
        }

        protected IList<ITodo> doGetFieldValue( ITodo aEntity ) {
          IListEdit<ITodo> result = new ElemArrayList<>();
          IUnitTodos ut = tsContext().get( IUnitTodos.class );
          for( Long id : aEntity.relatedTodoIds() ) {
            ITodo relTodo = ut.todos().findByKey( id );
            if( relTodo != null ) {
              result.add( relTodo );
            }
          }
          return result;
        }

      };

  /**
   * {@link ITodo#fulfilStages()}
   */
  public static final M5MultiModownFieldDef<ITodo, IFulfilStage> FULFIL_STAGES = //
      new M5MultiModownFieldDef<>( FID_FULFIL_STAGES, MID_FULFIL_STAGE ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_TD_FULFIL_STAGES, STR_TD_FULFIL_STAGES_D );
          setFlags( M5FF_DETAIL );
        }

        protected IList<IFulfilStage> doGetFieldValue( ITodo aEntity ) {
          return aEntity.fulfilStages();
        }

      };

  /**
   * Constructor.
   */
  public TodoM5Model() {
    super( MID_TODO, ITodo.class );
    setNameAndDescription( STR_M5M_TODO, STR_M5M_TODO_D );
    addFieldDefs( ID, CREATION_TIME, PRIORITY, IS_DONE, TEXT, NOTE, RELATED_TODO_IDS, FULFIL_STAGES );
    setPanelCreator( new TodoM5ModelPanelCreator() );
  }

  @Override
  protected IM5LifecycleManager<ITodo> doCreateDefaultLifecycleManager() {
    IUnitTodos unitTodos = tsContext().get( IUnitTodos.class );
    if( unitTodos == null ) {
      return null;
    }
    return new TodoM5LifecycleManager( this, unitTodos );
  }

  @Override
  protected IM5LifecycleManager<ITodo> doCreateLifecycleManager( Object aMaster ) {
    return new TodoM5LifecycleManager( this, IUnitTodos.class.cast( aMaster ) );
  }

}
