package com.hazard157.psx.proj3.episodes.story;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.proj3.episodes.story.IPsxResources.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.common.stuff.frame.*;

/**
 * Базовый класс всех реализаций {@link IScene}.
 *
 * @author hazard157
 */
abstract class AbstractScene
    implements IScene {

  private final AbstractScene            parent;
  private final IMapEdit<Secint, IScene> childScenes = new SortedElemMap<>();
  private Secint                         in;
  private SceneInfo                      info;

  /**
   * Конструктор для наследников.
   *
   * @param aParent {@link AbstractScene} - родитель
   * @param aIn {@link Secint} - интервал
   * @param aInfo {@link SceneInfo} - описание сцены
   */
  protected AbstractScene( AbstractScene aParent, Secint aIn, SceneInfo aInfo ) {
    TsNullArgumentRtException.checkNulls( aIn, aInfo );
    parent = aParent;
    in = aIn;
    info = aInfo;
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Если иллюстрация вне интервала, возвращает предупреждение..
   *
   * @param aIn {@link Secint} - интервал
   * @param aFrame {@link IFrame} - иллюстрация
   * @return {@link ValidationResult} - результат проверки
   */
  private static ValidationResult validateFrameSecint( Secint aIn, IFrame aFrame ) {
    int frameNo = aFrame.frameNo();
    if( frameNo >= 0 ) { // прверям, только если задана иллюстрация
      int frameSec = frameNo / FPS;
      if( !aIn.contains( frameSec ) ) {
        return ValidationResult.warn( FMT_WARN_FRAME_OUTSIDE_SCENE, aFrame.toString(), aIn.toString() );
      }
    }
    return ValidationResult.SUCCESS;
  }

  protected void internalSetSecint( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    if( !childScenes.isEmpty() ) {
      int fisrtSceneStart = childScenes.keys().get( childScenes.size() - 1 ).start();
      int lastSceneEnd = childScenes.keys().get( childScenes.size() - 1 ).end();
      Secint usedIn = new Secint( fisrtSceneStart, lastSceneEnd );
      if( !aIn.contains( usedIn ) ) {
        throw new TsIllegalArgumentRtException( FMT_ERR_NEW_INTERVAL_TOO_NARROW, aIn.toString(), usedIn.toString() );
      }
    }
    // НЕ НАДО ПЕРЕМЕЩАТЬ СЦЕНЫ! а если да, то по новой насовать в childScenes, с новыми ключами
    // Secint oldIn = in;
    in = aIn;
    // // при перемещении сцены переместим и дочерние сцены
    // if( oldIn.start() != in.start() ) {
    // int delta = in.start() - oldIn.start();
    // for( IScene s : childScenes ) {
    // Secint newIn = new Secint( s.in().start() + delta, s.in().end() + delta );
    // ((AbstractScene)s).internalSetSecint( newIn );
    // }
    // }
  }

  protected void internalClear() {
    childScenes.clear();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IScene
  //

  @Override
  public Secint interval() {
    return in;
  }

  @Override
  public IStory root() {
    return getRoot();
  }

  @Override
  public IScene parent() {
    return doGetParent();
  }

  @Override
  public SceneInfo info() {
    return info;
  }

  @Override
  public void setInfo( SceneInfo aInfo ) {
    TsNullArgumentRtException.checkNull( aInfo );
    if( !info.equals( aInfo ) ) {
      info = aInfo;
      getRoot().scenesChanged();
    }
  }

  @Override
  public IMap<Secint, IScene> childScenes() {
    return childScenes;
  }

  @Override
  public boolean isEmpty() {
    return childScenes.isEmpty();
  }

  @Override
  public Secint inBeforeChild( int aChildIndex ) {
    IScene sel = childScenes.values().get( aChildIndex );
    // найдем конец предыдущей сцены (даже если ее нет)
    int prevEnd = in.start() - 1;
    if( aChildIndex > 0 ) {
      prevEnd = childScenes.values().get( aChildIndex - 1 ).interval().end();
    }
    // есть интервал?
    if( sel.interval().start() - prevEnd > 1 ) {
      return new Secint( prevEnd + 1, sel.interval().start() - 1 );
    }
    return null;
  }

  @Override
  public Secint inAfterChild( int aChildIndex ) {
    IScene sel = childScenes.values().get( aChildIndex );
    // найдем начало следующей сцены (даже если ее нет)
    int nextStart = in.end() + 1;
    if( aChildIndex < childScenes.size() - 1 ) {
      nextStart = childScenes.values().get( aChildIndex + 1 ).interval().start();
    }
    // есть интервал?
    if( nextStart - sel.interval().end() > 1 ) {
      return new Secint( sel.interval().end() + 1, nextStart - 1 );
    }
    return null;
  }

  @Override
  public IScene addScene( Secint aIn, SceneInfo aInfo ) {
    TsValidationFailedRtException.checkError( canAddScene( aIn, aInfo ) );
    Scene s = new Scene( this, aIn, aInfo );
    childScenes.put( aIn, s );
    getRoot().scenesChanged();
    return s;
  }

  @Override
  public IScene removeScene( Secint aIn ) {
    TsValidationFailedRtException.checkError( canRemoveScene( aIn ) );
    IScene removed = childScenes.removeByKey( aIn );
    if( removed != null ) {
      getRoot().scenesChanged();
    }
    return removed;
  }

  @Override
  public IScene reintervalScene( Secint aIn, Secint aNewIn ) {
    TsValidationFailedRtException.checkError( canReintervalScene( aIn, aNewIn ) );
    AbstractScene scene = (AbstractScene)childScenes.getByKey( aIn );
    if( !aIn.equals( aNewIn ) ) {
      childScenes.removeByKey( aIn );
      scene.internalSetSecint( aNewIn );
      childScenes.put( aNewIn, scene );
      getRoot().scenesChanged();
    }
    return scene;
  }

  @Override
  public IScene editChildScene( int aChildIndex, Secint aIn, SceneInfo aInfo ) {
    TsNullArgumentRtException.checkNulls( aIn, aInfo );
    AbstractScene scene = (AbstractScene)childScenes.values().get( aChildIndex );
    boolean needReinterval = !aIn.equals( scene.interval() );
    if( needReinterval || !scene.info().equals( aInfo ) ) {
      if( needReinterval ) {
        childScenes.removeByKey( scene.interval() );
        scene.internalSetSecint( aIn );
        childScenes.put( aIn, scene );
      }
      scene.info = aInfo;
      getRoot().scenesChanged();
    }
    return scene;
  }

  @Override
  public ValidationResult canAddScene( Secint aIn, SceneInfo aInfo ) {
    TsNullArgumentRtException.checkNulls( aIn, aInfo );
    // проверим, что добавляемая сцена не выходит за пределы родителя
    if( !in.contains( aIn ) ) {
      return ValidationResult.error( FMT_ERR_CANT_ADD_OUT_OF_RANGE, aIn.toString(), in.toString() );
    }
    // проверим, что добавляемая сцена не пересекается с другими
    for( IScene s : childScenes ) {
      if( s.interval().intersects( aIn ) ) {
        return ValidationResult.error( FMT_ERR_CANT_ADD_SCENE_INTERSECTION, aIn.toString(), s.interval().toString() );
      }
    }
    // проверим, что название сцены задано
    ValidationResult vr = ValidationResult.SUCCESS;
    if( aInfo.name().isEmpty() || aInfo.name().equals( DEFAULT_NAME ) ) {
      vr = ValidationResult.warn( FMT_WARN_UNSET_SCENE_NAME );
    }
    return ValidationResult.firstNonOk( vr, validateFrameSecint( aIn, aInfo.frame() ) );
  }

  @Override
  public ValidationResult canEditChild( int aChildIndex, Secint aIn, SceneInfo aInfo ) {
    TsNullArgumentRtException.checkNulls( aIn, aInfo );
    IScene sel = childScenes.values().get( aChildIndex );
    ValidationResult vr = ValidationResult.SUCCESS;
    if( !aIn.equals( sel.interval() ) ) {
      vr = canReintervalScene( sel.interval(), aIn );
      if( vr.isError() ) {
        return vr;
      }
    }
    // проверим, что название сцены задано
    if( aInfo.name().isEmpty() || aInfo.name().equals( DEFAULT_NAME ) ) {
      vr = ValidationResult.firstNonOk( vr, ValidationResult.warn( FMT_WARN_UNSET_SCENE_NAME ) );
    }
    return ValidationResult.firstNonOk( vr, validateFrameSecint( aIn, aInfo.frame() ) );
  }

  @Override
  public ValidationResult canRemoveScene( Secint aIn ) {
    TsNullArgumentRtException.checkNull( aIn );
    IScene scene = childScenes.findByKey( aIn );
    if( scene == null ) {
      return ValidationResult.error( FMT_ERR_NO_SUCH_SCENE, aIn.toString() );
    }
    if( !scene.childScenes().isEmpty() ) {
      return ValidationResult.warn( FMT_WARN_DELETING_SCENE_WTH_CHILDS, aIn.toString() );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  public ValidationResult canReintervalScene( Secint aIn, Secint aNewIn ) {
    TsNullArgumentRtException.checkNulls( aIn, aNewIn );
    AbstractScene scene = (AbstractScene)childScenes.findByKey( aIn );
    if( scene == null ) {
      return ValidationResult.error( FMT_ERR_NO_SUCH_SCENE, aIn.toString() );
    }
    if( aIn.equals( aNewIn ) ) {
      return ValidationResult.SUCCESS;
    }
    // проверим, что сцена с измененной длительностью не выходит за пределы родителя
    if( !in.contains( aNewIn ) ) {
      return ValidationResult.error( FMT_ERR_CANT_RELOCATE_OUT_OF_RANGE, aIn.toString(), in.toString() );
    }
    // проверим, что новый интервал охватывает все дочерные сцены
    if( !scene.childScenes.isEmpty() ) {
      int fisrtSceneStart = scene.childScenes.keys().get( scene.childScenes.size() - 1 ).start();
      int lastSceneEnd = scene.childScenes.keys().get( scene.childScenes.size() - 1 ).end();
      Secint usedIn = new Secint( fisrtSceneStart, lastSceneEnd );
      if( !aNewIn.contains( usedIn ) ) {
        return ValidationResult.error( FMT_ERR_NEW_INTERVAL_TOO_NARROW, aNewIn.toString(), usedIn.toString() );
      }
    }
    // проверим, что сцена с измененной длительностью не пересекается с другими
    for( IScene s : childScenes ) {
      if( s.interval().equals( aIn ) ) { // пропускаем себя
        continue;
      }
      if( s.interval().intersects( aNewIn ) ) {
        return ValidationResult.error( FMT_ERR_CANT_ADD_SCENE_INTERSECTION, aNewIn.toString(),
            s.interval().toString() );
      }
    }
    // проверим, что дети сцены с измененной длительностью умещаются в новый интервал
    if( !scene.isEmpty() ) {
      int fisrtSceneStart = scene.childScenes.keys().get( scene.childScenes.size() - 1 ).start();
      int lastSceneEnd = scene.childScenes.keys().get( scene.childScenes.size() - 1 ).end();
      int childsDuration = lastSceneEnd - fisrtSceneStart + 1;
      if( aNewIn.duration() < childsDuration ) {
        return ValidationResult.error( FMT_ERR_CANT_REDUCE_SCENE_LESS_THAN_CHILDS, HmsUtils.mmss( aNewIn.duration() ),
            aNewIn.toString(), HmsUtils.mmss( childsDuration ) );
      }
    }
    return validateFrameSecint( aIn, scene.info.frame() );
  }

  @Override
  public IList<IScene> allScenesBelow() {
    IListEdit<IScene> list = new ElemLinkedBundleList<>();
    addScenesBelow( this, list );
    return list;
  }

  @Override
  public IList<IScene> leafScenes() {
    if( this.childScenes.isEmpty() ) {
      return new SingleItemList<>( this );
    }
    IListEdit<IScene> allList = new ElemLinkedBundleList<>();
    addScenesBelow( this, allList );
    IListEdit<IScene> list = new ElemLinkedBundleList<>();
    for( IScene s : allList ) {
      if( s.childScenes().isEmpty() ) {
        list.add( s );
      }
    }
    return list;
  }

  @Override
  public IScene findBestSceneFor( Secint aIn, boolean aGetLowest ) {
    TsNullArgumentRtException.checkNull( aIn );
    if( !aIn.intersects( in ) ) {
      return null;
    }
    IListEdit<IScene> intersectingChilds = new ElemLinkedBundleList<>();
    for( IScene child : childScenes ) {
      // рассмотрим только детей, пересекающихся с интервалом
      if( child.interval().intersects( aIn ) ) {
        // если ребенок содержит интервал, углубимся в него
        if( child.interval().contains( aIn ) ) {
          return child.findBestSceneFor( aIn, aGetLowest );
        }
        // просто пересекающейся интервал добавим в список
        intersectingChilds.add( child );
      }
    }
    // никто не пересекается с интервалом (например, вообще не было детей), вернем себя
    if( intersectingChilds.isEmpty() ) {
      return this;
    }
    // единственная дочка? вернем его
    if( intersectingChilds.size() == 1 ) {
      if( aGetLowest ) {
        return intersectingChilds.get( 0 ).findBestSceneFor( aIn, aGetLowest );
      }
      return intersectingChilds.get( 0 );
    }
    // по какому критрию считать "наиболее подходящим" ? ответ: кто больше занимает от интервала
    IScene bestScene = null;
    int inter = Integer.MIN_VALUE;
    for( IScene s : intersectingChilds ) {
      int len = Secint.intersection( s.interval(), aIn ).duration();
      if( len > inter ) {
        inter = len;
        bestScene = s;
      }
    }
    if( aGetLowest && bestScene != null ) {
      return bestScene.findBestSceneFor( aIn, aGetLowest );
    }
    return bestScene;
  }

  // ------------------------------------------------------------------------------------
  // Внутрипакетные метроды
  //

  private static void addScenesBelow( IScene aScene, IListEdit<IScene> aList ) {
    for( IScene s : aScene.childScenes() ) {
      aList.add( s );
      addScenesBelow( s, aList );
    }
  }

  AbstractScene doGetParent() {
    return parent;
  }

  // ------------------------------------------------------------------------------------
  // Методы Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return in.toString() + " - '" + info().name() + "'";
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения наследниками
  //

  /**
   * Наследник должен вернуть ссылку на сюжет.
   * <p>
   * Идея в том, что сцены {@link Scene} запрашивают сюжет у родителей, а сам сюжет {@link Story} возвращает себя.
   *
   * @return {@link Story} - сюжет, как коренбь всех сцен
   */
  protected abstract Story getRoot();

}
