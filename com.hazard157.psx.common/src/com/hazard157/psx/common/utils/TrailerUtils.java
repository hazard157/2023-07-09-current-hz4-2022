package com.hazard157.psx.common.utils;

import static com.hazard157.psx.common.utils.IHzResources.*;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * вспомогательные методы работы с трейлерами.
 *
 * @author hazard157
 */
public class TrailerUtils {

  /**
   * Валидатор строки идентификатора трейлера.
   */
  public static final ITsValidator<String> TRAILER_ID_VALIDATOR = new ITsValidator<>() {

    @Override
    public ValidationResult validate( String aValue ) {
      return validateTrailerId( aValue );
    }
  };

  /**
   * Валидатор атомарного значения идентификатора трейлера.
   */
  public static final ITsValidator<IAtomicValue> TRAILER_ID_VALIDATOR_AV = new ITsValidator<>() {

    @Override
    public ValidationResult validate( IAtomicValue aValue ) {
      return validateTrailerId( aValue.asString() );
    }
  };

  /**
   * Создает идентификатор трейлера.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aLocalId String - локальный идентификатор трейлера (внутри эпизода), ИД-имя
   * @return String - идентификатор трейлера
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валидный идентификатор эпизода
   * @throws TsIllegalArgumentRtException aLocalId не ИД-имя
   */
  public static String createTrailerId( String aEpisodeId, String aLocalId ) {
    EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    StridUtils.checkValidIdName( aLocalId );
    return StridUtils.makeIdPath( aEpisodeId, aLocalId );
  }

  /**
   * Проверяет идентификатор трейлера. на корректность и выдает ошибку с соответствующим сообщением.
   *
   * @param aTrailerId String - проверяемый идентификатор трейлера.
   * @return {@link ValidationResult} - результат проверки, {@link ValidationResult#SUCCESS} - успех
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ValidationResult validateTrailerId( String aTrailerId ) {
    ValidationResult vr = StridUtils.validateIdPath( aTrailerId );
    if( vr.isError() ) {
      return vr;
    }
    IStringList comps = StridUtils.getComponents( aTrailerId );
    if( comps.size() != 2 ) {
      return ValidationResult.error( FMT_ERR_TRID_NOT_2_ID_COMPS, aTrailerId );
    }
    if( !EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( comps.first() ) ) {
      return ValidationResult.error( FMT_ERR_TRID_INV_EPISODE_ID, aTrailerId );
    }
    if( comps.last().equals( IStridable.NONE_ID ) ) {
      return ValidationResult.error( MSG_WARN_TRID_FOO_LOCAL_ID );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Проверяет идентификатор трейлера на корректность и при неверном идентификаторе выбрасывает исключение.
   *
   * @param aTrailerId String - проверяемый идентификатор трейлера
   * @return String - всегда возвращает аргумент
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException неверный идентификатор трейлера
   */
  public static String checkValidTrailerId( String aTrailerId ) {
    TsValidationFailedRtException.checkError( validateTrailerId( aTrailerId ) );
    return aTrailerId;
  }

  /**
   * Проверяет идентификатор трейлера на корректность.
   *
   * @param aTrailerId String - проверяемый идентификатор трейлера
   * @return boolean - признак корректности идентификатора трейлера
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isValidTrailerId( String aTrailerId ) {
    return validateTrailerId( aTrailerId ).isOk();
  }

  /**
   * Извлекает идентификатор эпизода из идентификатора трейлера.
   *
   * @param aTrailerId String - идентификатор трейлера
   * @return String - идентификатор эпизода
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException агрумент - не валдидный идентификатор трейлера
   */
  public static String extractEpisodeId( String aTrailerId ) {
    checkValidTrailerId( aTrailerId );
    return StridUtils.getComponent( aTrailerId, 0 );
  }

  /**
   * Извлекает локальный идентификатор из идентификатора трейлера.
   *
   * @param aTrailerId String - идентификатор трейлера
   * @return String - локальный идентификатор
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException агрумент - не валдидный идентификатор трейлера
   */
  public static String extractLocalId( String aTrailerId ) {
    checkValidTrailerId( aTrailerId );
    return StridUtils.getComponent( aTrailerId, 1 );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Запрет на создание экземспляров.
   */
  private TrailerUtils() {
    // nop
  }

}
