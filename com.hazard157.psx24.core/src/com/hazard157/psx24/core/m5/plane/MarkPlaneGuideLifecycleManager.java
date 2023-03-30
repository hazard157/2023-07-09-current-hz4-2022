package com.hazard157.psx24.core.m5.plane;

import static com.hazard157.psx24.core.m5.plane.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.proplines.*;

class MarkPlaneGuideLifecycleManager
    extends M5LifecycleManager<MarkPlaneGuide, IMlPlaneGuide> {

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model} - модель
   * @param aMaster &lt;{@link IMlPlaneGuide}&gt; - линия пометок, может быть null
   * @throws TsNullArgumentRtException aModel = null
   */
  public MarkPlaneGuideLifecycleManager( IM5Model<MarkPlaneGuide> aModel, IMlPlaneGuide aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<MarkPlaneGuide> aValues ) {
    int start = MarkPlaneGuideM5Model.START.getFieldValue( aValues ).asInt();
    if( master().markersMap().hasKey( start ) ) {
      return ValidationResult.error( FMT_ERR_ALREADY_START, HmsUtils.mmmss( start ) );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected MarkPlaneGuide doCreate( IM5Bunch<MarkPlaneGuide> aValues ) {
    int start = MarkPlaneGuideM5Model.START.getFieldValue( aValues ).asInt();
    PlaneGuide guide = MarkPlaneGuideM5Model.GUIDE.getFieldValue( aValues );
    return master().addMarkAt( start, guide );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<MarkPlaneGuide> aValues ) {
    int start = MarkPlaneGuideM5Model.START.getFieldValue( aValues ).asInt();
    if( aValues.originalEntity().in().start() != start ) {
      if( master().markersMap().hasKey( start ) ) {
        return ValidationResult.error( FMT_ERR_ALREADY_START, HmsUtils.mmmss( start ) );
      }
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected MarkPlaneGuide doEdit( IM5Bunch<MarkPlaneGuide> aValues ) {
    int start = MarkPlaneGuideM5Model.START.getFieldValue( aValues ).asInt();
    PlaneGuide guide = MarkPlaneGuideM5Model.GUIDE.getFieldValue( aValues );
    return master().addMarkAt( start, guide );
  }

  @Override
  protected ValidationResult doBeforeRemove( MarkPlaneGuide aEntity ) {
    int start = aEntity.in().start();
    if( !master().markersMap().hasKey( start ) ) {
      return ValidationResult.error( FMT_ERR_NO_START, HmsUtils.mmmss( start ) );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( MarkPlaneGuide aEntity ) {
    master().removeByMarkerSec( aEntity.in().start() );
  }

  @Override
  protected IList<MarkPlaneGuide> doListEntities() {
    return master().marksList();
  }

}
