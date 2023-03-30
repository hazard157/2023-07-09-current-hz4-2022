package com.hazard157.psx.common.utils.ftstep;

import static com.hazard157.psx.common.utils.ftstep.IHzResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Seconds interval for sparse display of video frames.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public enum ESecondsStep
    implements IStridable {

  SEC_01( "sec01", STR_N_STEP_01_SEC, STR_D_STEP_01_SEC, 1 ), //$NON-NLS-1$
  SEC_02( "sec02", STR_N_STEP_02_SEC, STR_D_STEP_02_SEC, 2 ), //$NON-NLS-1$
  SEC_03( "sec03", STR_N_STEP_03_SEC, STR_D_STEP_03_SEC, 3 ), //$NON-NLS-1$
  SEC_05( "sec05", STR_N_STEP_05_SEC, STR_D_STEP_05_SEC, 5 ), //$NON-NLS-1$
  SEC_10( "sec10", STR_N_STEP_10_SEC, STR_D_STEP_10_SEC, 10 ), //$NON-NLS-1$
  SEC_15( "sec15", STR_N_STEP_15_SEC, STR_D_STEP_15_SEC, 15 ), //$NON-NLS-1$
  SEC_20( "sec20", STR_N_STEP_20_SEC, STR_D_STEP_20_SEC, 20 ), //$NON-NLS-1$
  SEC_30( "sec30", STR_N_STEP_30_SEC, STR_D_STEP_30_SEC, 30 ), //$NON-NLS-1$
  SEC_60( "sec60", STR_N_STEP_60_SEC, STR_D_STEP_60_SEC, 60 ), //$NON-NLS-1$
  SEC_120( "sec120", STR_N_STEP_120_SEC, STR_D_STEP_120_SEC, 120 ), //$NON-NLS-1$
  SEC_300( "sec300", STR_N_STEP_300_SEC, STR_D_STEP_300_SEC, 300 ), //$NON-NLS-1$

  ;

  /**
   * Идентификатор регистрации хранителя {@link #KEEPER} в реестре {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "ETimelineSteps"; //$NON-NLS-1$

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ESecondsStep> KEEPER = new StridableEnumKeeper<>( ESecondsStep.class );

  private static IStridablesListEdit<ESecondsStep> list = null;

  private final String id;
  private final String name;
  private final String description;
  private final int    stepSecs;

  ESecondsStep( String aId, String aName, String aDescription, int aStepSecs ) {
    id = aId;
    name = aName;
    description = aDescription;
    stepSecs = aStepSecs;
  }

  // --------------------------------------------------------------------------
  // Реализация интерфейса IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns secons between show frames.
   *
   * @return int - step duration in seconds
   */
  public int stepSecs() {
    return stepSecs;
  }

  /**
   * Возвращает следующий шаг в торону увеличения масштаба.
   *
   * @return {@link ESecondsStep} - бОльший масштаб или эта же константа, если достигнут максимальный масштаб
   */
  public ESecondsStep zoomIn() {
    int index = asList().indexOf( this ) - 1;
    if( index < 0 ) {
      index = 0;
    }
    return asList().get( index );
  }

  /**
   * Возвращает следующий шаг в торону уменьшения масштаба.
   *
   * @return {@link ESecondsStep} - меньший масштаб или эта же константа, если достигнут минимальный масштаб
   */
  public ESecondsStep zoomOut() {
    int index = asList().indexOf( this ) + 1;
    if( index >= asList().size() ) {
      index = asList().size() - 1;
    }
    return asList().get( index );
  }

  /**
   * Возвращает минимальный шаг (шаг с минималдьным интервалом).
   *
   * @return {@link ESecondsStep} - минимальный шаг
   */
  public static ESecondsStep minStep() {
    return asList().first();
  }

  public boolean isMaxZoomIn() {
    return this == asList().first();
  }

  /**
   * Возвращает максимальный шаг (шаг с максимальным интервалом).
   *
   * @return {@link ESecondsStep} - максимальный шаг
   */
  public static ESecondsStep maxStep() {
    return asList().last();
  }

  public boolean isMaxZoomOut() {
    return this == asList().last();
  }

  public int countStepsInDureation( int aDurationSecs ) {
    if( aDurationSecs < stepSecs ) {
      return 1;
    }
    int count = aDurationSecs / stepSecs;
    if( aDurationSecs % stepSecs > 0 ) {
      ++count;
    }
    return count;
  }

  public static ESecondsStep getFloor( int aDurationSecs ) {
    if( aDurationSecs <= asList().first().stepSecs() ) {
      return asList().first();
    }
    if( aDurationSecs >= asList().last().stepSecs() ) {
      return asList().last();
    }
    for( ESecondsStep s : asList() ) {
      if( s.stepSecs() >= aDurationSecs ) {
        return s;
      }
    }
    throw new TsInternalErrorRtException(); // must not happen
  }

  public static ESecondsStep getCeiling( int aDurationSecs ) {
    if( aDurationSecs <= asList().first().stepSecs() ) {
      return asList().first();
    }
    if( aDurationSecs >= asList().last().stepSecs() ) {
      return asList().last();
    }
    for( int i = asList().size() - 1; i >= 0; i++ ) {
      ESecondsStep s = asList().get( i );
      if( s.stepSecs() <= aDurationSecs ) {
        return s;
      }
    }
    throw new TsInternalErrorRtException(); // must not happen
  }

  /**
   * Возвращает все константы в виде списка.
   *
   * @return {@link IStridablesList}&lt; {@link ESecondsStep} &gt; - список всех констант
   */
  public static IStridablesList<ESecondsStep> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - нет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemById( String aId ) {
    return findById( aId ) != null;
  }

  /**
   * Находит константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETimelineSteps - найденная константа, или <code>null</code> если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ESecondsStep findById( String aId ) {
    return asList().findByKey( aId );
  }

  /**
   * Возвращает константу по идентификатору.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETimelineSteps - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static ESecondsStep getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - нет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static boolean isItemByName( String aName ) {
    return findByName( aName ) != null;
  }

  /**
   * Находит константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ETimelineSteps - найденная константа, или <code>null</code> если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   */
  public static ESecondsStep findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ESecondsStep item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени.
   *
   * @param aName String - имя искомой константы
   * @return ETimelineSteps - найденная константа
   * @throws TsNullArgumentRtException аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static ESecondsStep getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
