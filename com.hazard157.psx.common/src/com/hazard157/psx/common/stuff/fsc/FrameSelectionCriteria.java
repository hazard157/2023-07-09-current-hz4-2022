package com.hazard157.psx.common.stuff.fsc;

import static com.hazard157.psx.common.IPsxHardConstants.*;
import static com.hazard157.psx.common.stuff.fsc.IPsxResources.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.common.quants.ankind.*;
import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.svin.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.common.utils.ftstep.*;

/**
 * Правила выбора кадра эпизода, используется в различных виджетах и панелях.
 * <p>
 * Это неизменяемый класс.
 *
 * @author hazard157
 */
public class FrameSelectionCriteria {

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "FrameSelectionCriteria"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<FrameSelectionCriteria> KEEPER =
      new AbstractEntityKeeper<>( FrameSelectionCriteria.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, FrameSelectionCriteria aEntity ) {
          SvinKeeper.KEEPER.write( aSw, aEntity.svin() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.animationKind().id() );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.isOnlySecAligned() );
        }

        @Override
        protected FrameSelectionCriteria doRead( IStrioReader aSr ) {
          Svin svin = SvinKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          EAnimationKind showFrameType = EAnimationKind.asList().findByKey( aSr.readIdName() );
          aSr.ensureSeparatorChar();
          boolean onlySecAligned = aSr.readBoolean();
          return new FrameSelectionCriteria( svin, showFrameType, onlySecAligned );
        }

      };

  /**
   * Пустой критерий, которому не соответствует ни один эпизод и соответственно, ни один кадр.
   */
  public static final FrameSelectionCriteria NONE = new FrameSelectionCriteria(
      new Svin( EpisodeUtils.EPISODE_ID_NONE, IStridable.NONE_ID, Secint.ZERO ), EAnimationKind.BOTH, true );

  /**
   * Описание параметра для хранения критерия в наборе {@link IOptionSet}.
   */
  public static final IDataDef CRITERA = DataDef.create( "FrameSelectionCriteria", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_FRAME_SELECTION_CRITERIA, //
      TSID_DESCRIPTION, STR_D_FRAME_SELECTION_CRITERIA, //
      TSID_KEEPER_ID, KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( NONE, KEEPER, KEEPER_ID ) //
  );

  private final Svin           svin;
  private final EAnimationKind animationKind;
  private final boolean        onlySecAligned;
  private final ESecondsStep   secondsStep;

  /**
   * Конструктор.
   *
   * @param aSvin {@link Svin} - интервал эпизода (с необязательным интервалом и камерой)
   * @param aAnimated {@link EAnimationKind} - какие кадры будут отобраны
   * @param aOnlySecAligned boolean - признак, что включать только кадры, ровно на границе секунд
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FrameSelectionCriteria( Svin aSvin, EAnimationKind aAnimated, boolean aOnlySecAligned ) {
    TsNullArgumentRtException.checkNulls( aSvin, aAnimated );
    svin = aSvin;
    animationKind = aAnimated;
    onlySecAligned = aOnlySecAligned;
    secondsStep = ESecondsStep.SEC_01;
  }

  /**
   * Конструктор.
   *
   * @param aSvin {@link Svin} - интервал эпизода (с необязательным интервалом и камерой)
   * @param aAnimated {@link EAnimationKind} - какие кадры будут отобраны
   * @param aFrameTimeStep {@link ESecondsStep} - интервал между одиночными кадрами
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FrameSelectionCriteria( Svin aSvin, EAnimationKind aAnimated, ESecondsStep aFrameTimeStep ) {
    TsNullArgumentRtException.checkNulls( aSvin, aAnimated, aFrameTimeStep );
    svin = aSvin;
    animationKind = aAnimated;
    onlySecAligned = true;
    secondsStep = aFrameTimeStep;
  }

  // ------------------------------------------------------------------------------------
  // API фильтрования
  //

  /**
   * Проверят, удовлетворяет ли кадр критерию выборки.
   *
   * @param aFrame {@link IFrame} - проверяемый кадр
   * @return boolean - признак удовлетоврения критерию
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean isAccepted( IFrame aFrame ) {
    TsNullArgumentRtException.checkNull( aFrame );
    if( !aFrame.isDefined() ) {
      return false;
    }
    // отбраковка по animationKind
    if( !animationKind.accept( aFrame.isAnimated() ) ) {
      return false;
    }
    // отбраковка onlySecAligned/frameTimeStep
    int fsec = aFrame.frameNo() / FPS;
    if( onlySecAligned ) {
      if( !aFrame.isSecAligned() ) {
        return false;
      }
      if( fsec % secondsStep.stepSecs() != 0 ) {
        return false;
      }
    }
    // отбраковка по svin.episodeId()
    if( !svin.episodeId().equals( aFrame.episodeId() ) ) {
      return false;
    }
    // отбраковка по svin.cameraId()
    if( svin.hasCam() && !svin.cameraId().equals( aFrame.cameraId() ) ) {
      return false;
    }
    // обработка svin.interval()
    return svin.interval().contains( fsec );
  }

  // ------------------------------------------------------------------------------------
  // API свойств
  //

  /**
   * Возвращает интервал эпизода.
   *
   * @return {@link Svin} - интервал эпизода
   */
  public Svin svin() {
    return svin;
  }

  /**
   * Возвращает какие кадры будут отобраны.
   *
   * @return {@link EAnimationKind} - какие кадры будут отобраны
   */
  public EAnimationKind animationKind() {
    return animationKind;
  }

  /**
   * Возвращает параметр признак, что включать только кадры, ровно на границе секунд.
   *
   * @return boolean - признак, что включать только кадры, ровно на границе секунд
   */
  public boolean isOnlySecAligned() {
    return onlySecAligned;
  }

  /**
   * Возвращает интервал между выбираемыми кадрами в режиме "только-выровненныепо-секундам-кадры".
   * <p>
   * Этот параметр применяется только когда включен режим {@link #isOnlySecAligned()} = <code>true</code>.
   *
   * @return {@link ESecondsStep} - интервал между выбираемыми одиночными кадрами
   */
  public ESecondsStep frameTimeStep() {
    return secondsStep;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( svin.toString() );
    sb.append( " - " );
    sb.append( animationKind.id() );
    return sb.toString();
  }

}
