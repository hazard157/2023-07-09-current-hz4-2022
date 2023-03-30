package com.hazard157.psx24.core.utils.gifgen;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;

/**
 * Панаметры генерации анимированной минатюры видеоряда методом {@link PsxGifGenFromSink#createThumb(File, IList)}.
 *
 * @author goga
 */
public class PsxGifGenParams {

  /**
   * Режимы генерации.
   *
   * @author goga
   */
  public enum EMode {

    /**
     * Анимация содержит заданное в {@link PsxGifGenParams#getFramesNum()} кадров.
     * <p>
     * Эти 1-секундные кадры равномерно распределенных по источнику.
     * <p>
     * Позволяет создавать анимацию фиксированной длительности, вне зависимости от длительнсоти источника.
     */
    FIXED_FRAMES,

    /**
     * Кадры анимации создается из кадров источника с интервалом {@link PsxGifGenParams#getDeltaSec()} между ними.
     * <p>
     * Длительность созданной анимации пропорциональа длительности источника.
     */
    FIXED_DELTA,

    /**
     * Минимум по одному кадру на {@link Svin} истоника.
     * <p>
     * Для длительных кусков (см. параметр {@link PsxGifGenParams#getSvinReckonSecs()}, генерируются дополнительные
     * кадры.
     * <p>
     * Длительность созданной анимации зависит и от количества кусков в истонику, и от длительности источника.
     */
    SVINS_RECKONED

  }

  // boolean fixedFramesMode = false;
  EMode mode                         = EMode.FIXED_DELTA;
  int   setpointFramesNum            = 10;
  int   setpointDeltaSec             = 5;
  int   setpointBypassedFramesOnAnim = 0;
  int   setpointFinalStillDelay      = 2000;
  int   setpointSvinReckonSecs       = 3;

  /**
   * Конструктор.
   */
  public PsxGifGenParams() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает режим генерации анимации.
   *
   * @return {@link EMode} - режим генерации анимации
   */
  public EMode getMode() {
    return mode;
  }

  /**
   * Задает режим {@link #getMode()}.
   *
   * @param aMode {@link EMode} - режим генерации анимации
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public void setMode( EMode aMode ) {
    TsNullArgumentRtException.checkNull( aMode );
    mode = aMode;
  }

  // /**
  // * Определяет, как будет генерироваться миниатюра - с фиксированными итервалами между кадрами или заданным
  // количество
  // * кадров.
  // * <p>
  // * По умолчанию возвращает <code>false</code>.
  // *
  // * @return boolean - признак режима фиксированного количества кадров
  // */
  // public boolean isFixedFramesMode() {
  // return fixedFramesMode;
  // }
  //
  // /**
  // * Задает режим {@link #isFixedFramesMode()}.
  // *
  // * @param aFixedFramesMode boolean - режим {@link #isFixedFramesMode()}
  // */
  // public void setFixedFramesMode( boolean aFixedFramesMode ) {
  // fixedFramesMode = aFixedFramesMode;
  // }

  /**
   * Возвращает количество кадров анимации миниатюры для режима {@link EMode#FIXED_FRAMES}.
   * <p>
   * В этом режиме анимировнная миниатюра видеоряда содержит заданное (возвращаемое этим методом) количество кадров.
   * Каждый кадр показывается в течение 1 секунды. То есть, получается анимация длительностью столько секунд, столько
   * возвращает этот метод.
   * <p>
   * По умолчанию возвращает 10.
   *
   * @return int - количество кадров анимации миниатюры
   */
  public int getFramesNum() {
    return setpointFramesNum;
  }

  /**
   * Задает значение {@link #getFramesNum()}.
   *
   * @param aFramesNum int - значение {@link #getFramesNum()}
   */
  public void setFramesNum( int aFramesNum ) {
    setpointFramesNum = aFramesNum;
    if( aFramesNum < 1 ) {
      setpointFramesNum = 1;
    }
    if( aFramesNum > 60 ) {
      setpointFramesNum = 60;
    }
  }

  /**
   * Возвращает интервал в видеоряде между кадрами анимации миниатюры для режима {@link EMode#FIXED_DELTA}.
   * <p>
   * По умолчанию возвращает 5.
   *
   * @return int - интервал (в секундах) в видеоряде между кадрами анимации миниатюры
   */
  public int getDeltaSec() {
    return setpointDeltaSec;
  }

  /**
   * Задает значение {@link #getDeltaSec()}.
   *
   * @param aDeltaSec int - значение {@link #getDeltaSec()}
   */
  public void setDeltaSec( int aDeltaSec ) {
    setpointDeltaSec = aDeltaSec;
    if( aDeltaSec < 2 ) {
      setpointDeltaSec = 2;
    }
    if( aDeltaSec > 60 ) {
      setpointDeltaSec = 60;
    }
  }

  /**
   * Задает количество пропускаемых кадров исходного видео при создании кадров миниатюры.
   * <p>
   * Каждый кадр миниатюры показывается 1 секунду. При этом, сам кадр может быть анимированным, то есть, миниатюра будет
   * состоять из анимированных примерно 1-секундных фрагментов. Этот параметр регулирует, как формируется анимация
   * фрагмента.
   * <p>
   * Если метод возвращает 0, то нет анимации фрагмента, и в течение 1-ой секунды показывается один кадр исходного
   * видео. Другое возвращаемое значение бывает в диапазоне 1-10, и задает, степень "разряжения" кадров исходного видео.
   * "Разряжать" кадры нужно для уменьшения размера GIF-файла, и что гораздо важнее, потребление ресусров загруженного
   * {@link TsImage}.
   * <p>
   * Значение 1 приводит к тому, что каждый первый кадр исходного видео (который имеет частоту кадров 25/сек) будет
   * показан. То есть, каждый односекундный фрагмент миниатюры будет анимирован 25-ю кадрами. Например, степень
   * "разраяжения" 2 означает, что каждый второй кадр исходного видео будет использован, и фрагмент будет анимирован
   * 12,5 (реально, 13) кадрами.
   * <p>
   * По умолчанию возвращает 0.
   *
   * @return int - количество пропускаемых кадров исходного видео при создании кадров миниатюры
   */
  public int getBypassedFramesOnAnim() {
    return setpointBypassedFramesOnAnim;
  }

  /**
   * Задает значение {@link #getBypassedFramesOnAnim()}.
   *
   * @param aBypassedFramesOnAnim int - значение {@link #getBypassedFramesOnAnim()}
   */
  public void setBypassedFramesOnAnim( int aBypassedFramesOnAnim ) {
    setpointBypassedFramesOnAnim = aBypassedFramesOnAnim;
    if( aBypassedFramesOnAnim < 0 ) {
      setpointBypassedFramesOnAnim = 0;
    }
    if( aBypassedFramesOnAnim > 10 ) {
      setpointBypassedFramesOnAnim = 10;
    }
  }

  /**
   * Возвращает значение задержки последнего кадра миниатюры.
   * <p>
   * После окончания анимации миниатюры (фактически, мини-клипа), обычно сразу она воспроизводится сначала. Чтобы
   * выизуально выделить окончание окончание мини-клипа, надо можно последний кадр задержать на некторое время. Данный
   * параметр задает это время задержки. Значение0 - нет задержки, мини-клип будет крутиться непрерывно.
   * <p>
   * По умолчанию возвращает 2000.
   *
   * @return int - задержка (в миллисекундах) последнего кадра анимированной миниатюры
   */
  public int getFinalStillDelay() {
    return setpointFinalStillDelay;
  }

  /**
   * Задает значение {@link #getFinalStillDelay()}.
   *
   * @param aFinalDelay int - задержка (в миллисекундах) последнего кадра анимированной миниатюры
   */
  public void setFinalStillDelay( int aFinalDelay ) {
    setpointFinalStillDelay = aFinalDelay;
    if( aFinalDelay < 0 ) {
      setpointFinalStillDelay = 0;
    }
    if( aFinalDelay > 100_000 ) {
      setpointFinalStillDelay = 100_000;
    }
  }

  /**
   * Возвращает учитываемую длz дополнительных кадров длительность куска для режима {@link EMode#SVINS_RECKONED}.
   * <p>
   * Эта длительность (ReckonSecs) учитывается следующим образом. Обычно, для каждого куска генерируется один кадр,
   * взятый с середины куска. Если же длительность куска превышает ReckonSecs, то для каждой ReckonSecs части
   * генерируется один кадр. Последняя часть, меньшая чем двукратный ReckonSecs считается одним куском.
   * <p>
   * Позволяет соотносить длительность и содержимое генерированной анимации с содержимым источника.
   * <p>
   * По умолчанию возвращает 3.
   *
   * @return int - длительность (в секундах) куска, который учитывается дл дополнительных кадров
   */
  public int getSvinReckonSecs() {
    return setpointSvinReckonSecs;
  }

  /**
   * Задает значение {@link #getSvinReckonSecs()}.
   *
   * @param aSvinReckonSecs int - длительность (в секундах) куска, который учитывается дл дополнительных кадров
   */
  public void setSvinReckonSecs( int aSvinReckonSecs ) {
    setpointSvinReckonSecs = aSvinReckonSecs;
    if( aSvinReckonSecs < 2 ) {
      setpointSvinReckonSecs = 2;
    }
    if( aSvinReckonSecs > 60 ) {
      setpointSvinReckonSecs = 60;
    }
  }

}
