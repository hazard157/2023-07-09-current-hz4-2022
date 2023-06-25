package com.hazard157.prisex24.m5.episodes;

import static com.hazard157.prisex24.m5.episodes.SceneM5Model.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.proj3.episodes.story.*;

class SceneLifecycleManager
    extends M5LifecycleManager<IScene, IScene> {

  public SceneLifecycleManager( IM5Model<IScene> aModel, IScene aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<IScene> aValues ) {
    IScene parent = master();
    Secint in = INTERVAL.getFieldValue( aValues );
    String name = NAME.getFieldValue( aValues ).asString();
    IFrame frame = FRAME.getFieldValue( aValues );
    SceneInfo info = new SceneInfo( name, frame );
    return parent.canAddScene( in, info );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<IScene> aValues ) {
    IScene parent = master();
    Secint in = INTERVAL.getFieldValue( aValues );
    String name = NAME.getFieldValue( aValues ).asString();
    IFrame frame = FRAME.getFieldValue( aValues );
    SceneInfo info = new SceneInfo( name, frame );
    return parent.canEditChild( parent.childScenes().values().indexOf( aValues.originalEntity() ), in, info );
  }

  @Override
  protected ValidationResult doBeforeRemove( IScene aEntity ) {
    return aEntity.parent().canRemoveScene( aEntity.interval() );
  }

  @Override
  protected IScene doCreate( IM5Bunch<IScene> aValues ) {
    String name = NAME.getFieldValue( aValues ).asString();
    Secint in = INTERVAL.getFieldValue( aValues );
    IFrame frame = FRAME.getFieldValue( aValues );
    return master().addScene( in, new SceneInfo( name, frame ) );
  }

  @Override
  protected IScene doEdit( IM5Bunch<IScene> aValues ) {
    String name = NAME.getFieldValue( aValues ).asString();
    IFrame frame = FRAME.getFieldValue( aValues );
    IScene s = aValues.originalEntity();
    if( !name.equals( s.info().name() ) || !frame.equals( s.frame() ) ) {
      s.setInfo( new SceneInfo( name, frame ) );
    }
    Secint in = INTERVAL.getFieldValue( aValues );
    if( !in.equals( s.interval() ) ) {
      s.parent().reintervalScene( s.interval(), in );
    }
    return s;
  }

  @Override
  protected void doRemove( IScene aEntity ) {
    aEntity.parent().removeScene( aEntity.interval() );
  }

  @Override
  protected IList<IScene> doListEntities() {
    return master().childScenes().values();
  }

}
