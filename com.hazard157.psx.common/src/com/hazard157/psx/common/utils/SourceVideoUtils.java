package com.hazard157.psx.common.utils;

import static com.hazard157.psx.common.utils.IHzResources.*;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * вспомогательные методы работы с исходными видео.
 *
 * @author hazard157
 */
public class SourceVideoUtils {

  /**
   * Валидатор строки идентификатора исходного видео.
   */
  public static final ITsValidator<String> SOURCE_VIDEO_ID_VALIDATOR = new ITsValidator<>() {

    @Override
    public ValidationResult validate( String aValue ) {
      return validateSourceVideoId( aValue );
    }
  };

  /**
   * Валидатор атомарного значения идентификатора исходного видео.
   */
  public static final ITsValidator<IAtomicValue> SOURCE_VIDEO_ID_VALIDATOR_AV = new ITsValidator<>() {

    @Override
    public ValidationResult validate( IAtomicValue aValue ) {
      return validateSourceVideoId( aValue.asString() );
    }
  };

  /**
   * Создает идентификатор исходного видео.
   *
   * @param aEpisodeId String - идентификатор эпизода
   * @param aCamId String - идентификатор камеры
   * @return String - идентификатор исходного видео
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aEpisodeId не валидный идентификатор эпизода
   * @throws TsIllegalArgumentRtException aCamId не ИД-путь
   */
  public static String createSourceVideoId( String aEpisodeId, String aCamId ) {
    EpisodeUtils.EPISODE_ID_VALIDATOR.checkValid( aEpisodeId );
    StridUtils.checkValidIdPath( aCamId );
    return StridUtils.makeIdPath( aEpisodeId, aCamId );
  }

  /**
   * Проверяет идентификатор исходного видео. на корректность и выдает ошибку с соответствующим сообщением.
   *
   * @param aSourceVideoId String - проверяемый идентификатор исходного видео.
   * @return {@link ValidationResult} - результат проверки, {@link ValidationResult#SUCCESS} - успех
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ValidationResult validateSourceVideoId( String aSourceVideoId ) {
    ValidationResult vr = StridUtils.validateIdPath( aSourceVideoId );
    if( vr.isError() ) {
      return vr;
    }
    IStringList comps = StridUtils.getComponents( aSourceVideoId );
    if( comps.size() < 2 ) {
      return ValidationResult.error( FMT_ERR_SVID_TOO_FEW_ID_COMPS, aSourceVideoId );
    }
    if( !EpisodeUtils.EPISODE_ID_VALIDATOR.isValid( comps.first() ) ) {
      return ValidationResult.error( FMT_ERR_SVID_INV_EPISODE_ID, aSourceVideoId );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Проверяет идентификатор исходного видео на корректность и при неверном идентификаторе выбрасывает исключение.
   *
   * @param aSourceVideoId String - проверяемый идентификатор исходного видео
   * @return String - всегда возвращает аргумент
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException неверный идентификатор исходного видео
   */
  public static String checkValidSourceVideoId( String aSourceVideoId ) {
    TsValidationFailedRtException.checkError( validateSourceVideoId( aSourceVideoId ) );
    return aSourceVideoId;
  }

  /**
   * Проверяет идентификатор исходного видео на корректность.
   *
   * @param aSourceVideoId String - проверяемый идентификатор исходного видео
   * @return boolean - признак корректности идентификатора исходного видео
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isValidSourceVideoId( String aSourceVideoId ) {
    return validateSourceVideoId( aSourceVideoId ).isOk();
  }

  /**
   * Извлекает идентификатор эпизода из идентификатора исходного видео.
   *
   * @param aSourceVideoId String - идентификатор исходного видео
   * @return String - идентификатор эпизода
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException агрумент - не валдидный идентификатор исходного видео
   */
  public static String extractEpisodeId( String aSourceVideoId ) {
    checkValidSourceVideoId( aSourceVideoId );
    return StridUtils.getComponent( aSourceVideoId, 0 );
  }

  /**
   * Извлекает идентификатор камеры из идентификатора исходного видео.
   *
   * @param aSourceVideoId String - идентификатор исходного видео
   * @return String - идентификатор камеры
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException агрумент - не валдидный идентификатор исходного видео
   */
  public static String extractCamId( String aSourceVideoId ) {
    checkValidSourceVideoId( aSourceVideoId );
    IStringList comps = StridUtils.getComponents( aSourceVideoId );
    return StridUtils.makeIdPath( comps, 1, comps.size() - 1 );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Запрет на создание экземспляров.
   */
  private SourceVideoUtils() {
    // nop
  }

}
