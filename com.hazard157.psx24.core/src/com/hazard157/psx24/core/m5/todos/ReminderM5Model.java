package com.hazard157.psx24.core.m5.todos;

import static com.hazard157.psx24.core.m5.todos.IPsxResources.*;
import static com.hazard157.psx24.core.m5.todos.ITodoM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;

import com.hazard157.psx.proj3.todos.*;
import com.hazard157.psx.proj3.todos.impl.*;

/**
 * {@link IReminder} M5-model.
 *
 * @author hazard157
 */
public class ReminderM5Model
    extends M5Model<IReminder> {

  /**
   * {@link IReminder#isActive()}
   */
  public static final IM5AttributeFieldDef<IReminder> IS_ACTIVE = new M5AttributeFieldDef<>( FID_IS_ACTIVE, BOOLEAN, //
      TSID_NAME, STR_N_REM_IS_ACTIVE, //
      TSID_DESCRIPTION, STR_D_REM_IS_ACTIVE, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK_AV //
  ) {

    protected IAtomicValue doGetFieldValue( IReminder aEntity ) {
      return avBool( aEntity.isActive() );
    }
  };

  /**
   * {@link IReminder#remindTimestamp()}
   */
  public static final IM5AttributeFieldDef<IReminder> REMIND_TIMESTAMP =
      new M5AttributeFieldDef<>( FID_REMIND_TIMESTAMP, TIMESTAMP, //
          TSID_NAME, STR_N_REM_REMIND_TIMESTAMP, //
          TSID_DESCRIPTION, STR_D_REM_REMIND_TIMESTAMP //
      ) {

        protected IAtomicValue doGetFieldValue( IReminder aEntity ) {
          return avTimestamp( aEntity.remindTimestamp() );
        }
      };

  /**
   * {@link IReminder#message()}
   */
  public static final IM5AttributeFieldDef<IReminder> MESSAGE = new M5AttributeFieldDef<>( FID_MESSAGE, STRING, //
      TSID_NAME, STR_N_REM_MESSAGE, //
      TSID_DESCRIPTION, STR_D_REM_MESSAGE//
  ) {

    protected IAtomicValue doGetFieldValue( IReminder aEntity ) {
      return avStr( aEntity.message() );
    }
  };

  /**
   * Constructor.
   */
  public ReminderM5Model() {
    super( MID_REMINDER, IReminder.class );
    setNameAndDescription( STR_N_M5M_REMINDER, STR_D_M5M_REMINDER );
    addFieldDefs( IS_ACTIVE, REMIND_TIMESTAMP, MESSAGE );
  }

  @Override
  protected IM5LifecycleManager<IReminder> doCreateDefaultLifecycleManager() {
    return new M5LifecycleManager<>( this, true, true, true, false, null ) {

      @Override
      protected IReminder doCreate( IM5Bunch<IReminder> aValues ) {
        boolean isActive = aValues.getAsAv( FID_IS_ACTIVE ).asBool();
        long remindTimestamp = aValues.getAsAv( FID_REMIND_TIMESTAMP ).asLong();
        String message = aValues.getAsAv( FID_MESSAGE ).asString();
        return new Reminder( isActive, remindTimestamp, message );
      }

      @Override
      protected IReminder doEdit( IM5Bunch<IReminder> aValues ) {
        return create( aValues );
      }

      @Override
      protected void doRemove( IReminder aEntity ) {
        // nop
      }

    };
  }

}
