package com.hazard157.psx.common.stuff.frame;

import static com.hazard157.psx.common.IPsxHardConstants.*;

import java.time.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.*;
import com.hazard157.psx.common.stuff.*;

/**
 * Идентификация кадра исходного клипа эпизода.
 * <p>
 * Обратите внимание, что (кроме константы {@link #NONE}) ожидается реальное значение идентификатора эпизода
 * {@link #episodeId()}. Также, часто используется реальное значение {@link #cameraId()}, даже если значок не задан (то
 * есть, {@link #frameNo()} = -1).
 * <p>
 * Если {@link #isAnimated()} = <code>false</code>, идентифицируется один кадр с номером {@link #frameNo()}. Если
 * {@link #isAnimated()} = <code><code>true</code>, то используется gif-нимированное изображение, содержащее 5 секунд
 * клипа, начиная с кадра {@link #frameNo()} (обычно, номер кадра при этом выравнен на границе секунды).
 * <p>
 * Сортировка (интерфейс {@link Comparable}) происходит по возрастанию {@link #episodeId()}, {@link #frameNo()},
 * {@link #cameraId()}, и наконец {@link #isAnimated()}.
 *
 * @author hazard157
 */
public interface IFrame
    extends IEpisodeIdable, ICameraIdable, Comparable<IFrame> {

  /**
   * "Нулевой", отсутсвующий кадр.
   */
  IFrame NONE = new InternalNoneFrame();

  /**
   * Возвращает идентификатор эпизода.
   *
   * @return String - идентификатор эпизода или {@link IStridable#NONE_ID} для {@link #NONE}
   */
  @Override
  String episodeId();

  /**
   * Возвращает дату события.
   *
   * @return {@link LocalDate} - дата события
   */
  LocalDate whenDate();

  /**
   * Возвращает идентификатор снявшей камеры, что однозначно определяет исходный клип эпизода.
   *
   * @return String - идентификатор снявшей камеры или {@link IStridable#NONE_ID}
   */
  @Override
  String cameraId();

  /**
   * Возвращает кадр с начала эпизода (частота кадров исходного клипа считается равной {@link IPsxHardConstants#FPS} ).
   * <p>
   * Для анимированного значка возвращается номер первого кадра анимации, а для картинкми - номер единственного кадра.
   *
   * @return int - номер кадра (может быть -1 для не заданного значка)
   */
  int frameNo();

  /**
   * Возвращает номер секунды, соответствующей {@link #frameNo()}.
   *
   * @return int - номер секунды, равный {@link #frameNo()} / {@link IPsxHardConstants#FPS}
   */
  default int secNo() {
    return frameNo() / FPS;
  }

  /**
   * Возвращает признак анимированного изображения (мини-клипа).
   *
   * @return boolean - признак анимированного изображения
   */
  boolean isAnimated();

  /**
   * Возвращает признак, что объект описывает валидный кадр.
   * <p>
   * Валидность кадра означает, что все поля объекта имеют осмысленные значения, а именно:
   * <ul>
   * <li>{@link #episodeId()} - является синтаксически верным идентификатором эпизода;</li>
   * <li>{@link #cameraId()} - является валидным ИД-путем;</li>
   * <li>{@link #frameNo()} - больше или равно 0.</li>
   * </ul>
   * Вместе с тем, валидность не означает, что такой кадр в действительности существует. В частности, может не
   * существовать эпизода с таким идентификатором, камера может быть не определена (хотя бы для данного эпизода) и номер
   * кадра может выходить за пределы длителности эпизода.
   *
   * @return boolean - признак, что объект описывает валидный кадр
   */
  boolean isDefined();

  /**
   * Опредеяет, находится ли кадро точно на границе секунды.
   *
   * @return boolean - признак, что кадр соответствует секунде ровно
   */
  default boolean isSecAligned() {
    return ((frameNo() % FPS) == 0);
  }

}

class InternalNoneFrame
    implements IFrame {

  @Override
  public int compareTo( IFrame o ) {
    if( o == IFrame.NONE ) {
      return 0;
    }
    return -1;
  }

  @Override
  public String episodeId() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public LocalDate whenDate() {
    throw new TsNullObjectErrorRtException();
  }

  @Override
  public String cameraId() {
    return IStridable.NONE_ID;
  }

  @Override
  public int frameNo() {
    return -1;
  }

  @Override
  public boolean isAnimated() {
    return false;
  }

  @Override
  public boolean isDefined() {
    return false;
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals( Object aObj ) {
    return aObj == this;
  }

  @Override
  public String toString() {
    return "NONE"; //$NON-NLS-1$
  }

}
