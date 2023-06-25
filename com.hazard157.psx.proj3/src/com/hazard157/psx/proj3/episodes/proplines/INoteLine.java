package com.hazard157.psx.proj3.episodes.proplines;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;

/**
 * Пометка текстовыми заметками продолжительной во времени сущности.
 *
 * @author hazard157
 */
public interface INoteLine
    extends IAnyPropLineBase {

  /**
   * Возвращает пометки заметками.
   *
   * @return IMap&lt;{@link Secint},String&gt; - карта "интервал" - "заметка"
   */
  IMap<Secint, String> marksMap();

  /**
   * Возвращает все пометки в виде списка.
   *
   * @return IList&lt;{@link MarkNote}&gt; - список мометок интервал/строка
   */
  IList<MarkNote> listMarks();

  /**
   * Добавляет новую или замещает существующую пометку.
   *
   * @param aIn {@link Secint} - интервал
   * @param aNote String - текст заметки
   * @return {@link MarkNote} - новая пометка
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  MarkNote putMark( Secint aIn, String aNote );

  /**
   * Удаляет пометку.
   * <p>
   * Если нет такой пометки, ничего не делает.
   *
   * @param aIn {@link Secint} - интервал
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void removeMark( Secint aIn );

  /**
   * Возвращает заметки в указанную секунду.
   *
   * @param aSec int - секунда
   * @return {@link IStringList} - список заметок
   * @throws TsIllegalArgumentRtException aSec < 0
   */
  IStringList getNotesAt( int aSec );

  /**
   * Возвращает заметки в указанном интервале.
   *
   * @param aIn {@link Secint} - интервал
   * @return {@link IStringList} - список заметок
   * @throws TsNullArgumentRtException аргумент = null
   */
  IStringList getNotesAt( Secint aIn );

}
