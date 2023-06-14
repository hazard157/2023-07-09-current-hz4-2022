package com.hazard157.psx.proj3.episodes;

import java.time.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.txtproj.lib.sinent.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.episodes.story.*;
import com.hazard157.psx.proj3.incident.*;

/**
 * Базовый интерфейс эпизода.
 * <p>
 * Реализует общие для люой реализации эпизода методы, что для psx3, что для psx5 или еще будущих реализации.
 *
 * @author hazard157
 */
public interface IEpisode
    extends IPsxIncident, IEpisodeIdable, IFrameable, ISinentity<EpisodeInfo> {

  /**
   * Возвращает момент времен начала эпизода.
   *
   * @return long - начал эпизода (в миллисекундах эпохи)
   */
  long when();

  /**
   * Возвращает дату эпизода.
   *
   * @return {@link LocalDate} - дата эпизода
   */
  LocalDate date();

  /**
   * Возвращает сюжет эпизода.
   *
   * @return {@link IStory} - сюжет эпизода
   */
  IStory story();

  /**
   * Возвращает заметки к эпизоду.
   *
   * @return {@link INoteLine} - заметки
   */
  INoteLine noteLine();

  /**
   * Возвращает длительность эпизода.
   *
   * @return int - длительность эпизода в секундах
   */
  int duration();

  /**
   * Возвращает теглайн - редактируемый набор пометок эпизода ярлыками.
   *
   * @return {@link ITagLine} - теглайн эпизода
   */
  ITagLine tagLine();

  /**
   * Возвращает планы съемки.
   *
   * @return {@link IMlPlaneGuide} - линия отметки планов
   */
  IMlPlaneGuide planesLine();

  /**
   * Возвращает иллюстрации к сюжету эпизода.
   * <p>
   * Иллюстрациями служат кадры, иллюстрирующие сцены первого уровня (то есть, дети сюжета -
   * {@link IStory#childScenes()}), а в случае запроса детальных иллюстрации (Detailed = <code>true</code>), кадры к
   * сценам, дочерным к дочкам сюжета.
   *
   * @param aDetailed boolean - выдать более детальные (больше) иллюстрации
   * @return {@link IList}&lt;{@link IFrame}&gt; - отсортированный список иллюстрации
   */
  IList<IFrame> getIllustrations( boolean aDetailed );

  /**
   * Возвращает состояние дел по обработке эпизода.
   *
   * @return {@link IEpVerifyCfg} - состояние дел по обработке эпизода
   */
  IEpVerifyCfg verifyCfg();

  /**
   * Возвращает посекундные срезы характеристик эпизода.
   * <p>
   * Срезы формируются динамически, при обращении к этому и кешируются. Изменения в эпизоде только сбрасывает кеш, а не
   * обновляет. Обновление происходит только при обращении к этому методу. <b>Резюме:</b> обращение к методу является
   * настолько эффективным (не ресурсоемким), настолько это вообще возможно.
   *
   * @return {@link IList}&lt;{@link SecondSlice}&gt; - список срезов, где индекс это секунда эпизода
   */
  IList<SecondSlice> slices();

  /**
   * Создает {@link Svin} во весь эпизод.
   * <p>
   * В качестве идентификатор камеры используется камера иллюстрирующего кадра {@link IEpisode#frame()}.
   *
   * @return {@link Svin} - во весь эпизод
   */
  default Svin svin() {
    return new Svin( id(), frame().cameraId(), new Secint( 0, duration() - 1 ) );
  }

  @Override
  default String place() {
    return info().place();
  }

  /**
   * Возвращает удобочитаемую строку имени эпизода.
   * <p>
   * Что-то вида "ДАТА - название".
   *
   * @return String - удобочитаемая строка имени эпизода
   */
  default String readableName() {
    StringBuilder s = new StringBuilder().append( EpisodeUtils.ymdFromId( id() ) );
    s.append( " - " ).append( nmName() ); //$NON-NLS-1$
    return s.toString();
  }

}
