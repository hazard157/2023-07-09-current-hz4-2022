package com.hazard157.psx.proj3.episodes.story;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Сцена в дереве сцен.
 *
 * @author hazard157
 */
public interface IScene
    extends IEpisodeIntervalable, IFrameable {

  /**
   * Возвращает интервал сцены.
   *
   * @return {@link Secint} - интервал внутры сюжета {@link #root()}
   */
  @Override
  Secint interval();

  /**
   * Возвращает описание сцены.
   *
   * @return {@link SceneInfo} - описание сцены
   */
  SceneInfo info();

  /**
   * Задает описание сцены.
   *
   * @param aInfo {@link SceneInfo} - описание сцены
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setInfo( SceneInfo aInfo );

  /**
   * Возвращает сюжет - корневую сцену.
   *
   * @return {@link IStory} - сюжет (корневая сцена)
   */
  IStory root();

  /**
   * Возвращает родительскую сцену.
   * <p>
   * Для сюжета {@link IStory} возвращает null.
   *
   * @return {@link IScene} - родительская сцена или null
   */
  IScene parent();

  /**
   * Возвращает дочерные сцены.
   * <p>
   * В возращаемой карте:
   * <ul>
   * <li>нет пересекающихся интервалов;</li>
   * <li>могут быть пропуски - сцены не полностью покрывают интервал {@link #interval()};</li>
   * <li>ключи {@link IMap#keys()} отсортированы по возрастанию.</li>
   * </ul>
   *
   * @return IMap&lt;{@link Secint},{@link IScene}&gt; - карта "интервал сцены" - "сцена"
   */
  IMap<Secint, IScene> childScenes();

  /**
   * Определят, является ли сцена пустым (то есть, не содержит дочерные сцены).
   * <p>
   * Равнозначно проверке {@link IMap#isEmpty()} дочерных узлов {@link #childScenes()}.
   *
   * @return boolean - принак сцены без разбиения на дочерные
   */
  boolean isEmpty();

  /**
   * Возвращает незанятый интервал непосредственно перед заданной дочерней сценой.
   *
   * @param aChildIndex int - индлекс сцены (в пределах 0 .. {@link #childScenes()}.size()-1)
   * @return {@link Secint} - незанятый интервал до сцены или null, если перед сценой нет незанятого интервала
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   */
  Secint inBeforeChild( int aChildIndex );

  /**
   * Возвращает незанятый интервал непосредственно после заданной дочерней сценой.
   *
   * @param aChildIndex int - индлекс сцены (в пределах 0 .. {@link #childScenes()}.size()-1)
   * @return {@link Secint} - незанятый интервал после сцены или null, если после сценой нет незанятого интервала
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   */
  Secint inAfterChild( int aChildIndex );

  /**
   * Добавляет сцену.
   *
   * @param aIn {@link Secint} - интервал добавляемой сцены
   * @param aInfo {@link SceneInfo} - описание сцены
   * @return {@link IScene} - созданная сцена
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка {@link #canAddScene(Secint, SceneInfo)}
   */
  IScene addScene( Secint aIn, SceneInfo aInfo );

  /**
   * Удаляет сцену.
   * <p>
   * Если нет такой сцены, то метод ничего не делает, просто возвращает null.
   *
   * @param aIn {@link Secint} - интервал удаляемой сцены
   * @return {@link IScene} - удаленная сцена или null
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка {@link #canRemoveScene(Secint)}
   */
  IScene removeScene( Secint aIn );

  /**
   * Изменят интервал сцены.
   *
   * @param aIn {@link Secint} - интервал перемещаемой сцены
   * @param aNewIn {@link Secint} - новый интервал перемещаемой сцены
   * @return {@link IScene} - измененная сцена
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка {@link #canReintervalScene(Secint, Secint)}
   */
  IScene reintervalScene( Secint aIn, Secint aNewIn );

  /**
   * Редактирует дочернюю сцену.
   *
   * @param aChildIndex int - индлекс сцены (в пределах 0 .. {@link #childScenes()}.size()-1)
   * @param aIn {@link Secint} - новый интервал сцены
   * @param aInfo {@link SceneInfo} - новое описание сцены
   * @return {@link IScene} - измененная сцена
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   * @throws TsValidationFailedRtException не прошла проверка {@link #canEditChild(int, Secint, SceneInfo)}
   */
  IScene editChildScene( int aChildIndex, Secint aIn, SceneInfo aInfo );

  /**
   * Проверяет, можно ли добавить сцену, не задев существующие.
   *
   * @param aIn {@link Secint} - интервал добавляемой сцены
   * @param aInfo {@link SceneInfo} - описание сцены
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canAddScene( Secint aIn, SceneInfo aInfo );

  /**
   * Проверяет, можно ли изменить сцену.
   *
   * @param aChildIndex int - индлекс сцены (в пределах 0 .. {@link #childScenes()}.size()-1)
   * @param aIn {@link Secint} - новый интервал сцены
   * @param aInfo {@link SceneInfo} - новое описание сцены
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   */
  ValidationResult canEditChild( int aChildIndex, Secint aIn, SceneInfo aInfo );

  /**
   * Проверяет, можно ли удалить сцену.
   * <p>
   * Если нет такой сцены, то возвращает {@link ValidationResult#SUCCESS}.
   *
   * @param aIn {@link Secint} - интервал удаляемой сцены
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canRemoveScene( Secint aIn );

  /**
   * Проверяет, можно ли изменить интервал сцены.
   *
   * @param aIn {@link Secint} - интервал перемещаемой сцены
   * @param aNewIn {@link Secint} - новый интервал перемещаемой сцены
   * @return {@link ValidationResult} - результат проверки
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValidationResult canReintervalScene( Secint aIn, Secint aNewIn );

  /**
   * Возвращает все сцены дерева этого корня.
   * <p>
   * Возвращаемый список не включает в себя эту сцену.
   *
   * @return {@link IList}&lt;{@link IScene}&gt; - список сцен
   */
  IList<IScene> allScenesBelow();

  /**
   * Возвращает сцены - листья дерева этого корня.
   *
   * @return {@link IList}&lt;{@link IScene}&gt; - список сцен
   */
  IList<IScene> leafScenes();

  /**
   * Находит сцену (внутри данной сцены), лучше всего соотвутсвующую интервалу.
   *
   * @param aIn {@link Secint} - интервал
   * @param aGetLowest boolean - вернуть наиболее "дочернюю" сцену, а не родительскую
   * @return {@link IScene} - найденная сцена или null, если интервал вне этой сцены
   * @throws TsNullArgumentRtException аргумент = null
   */
  IScene findBestSceneFor( Secint aIn, boolean aGetLowest );

}
