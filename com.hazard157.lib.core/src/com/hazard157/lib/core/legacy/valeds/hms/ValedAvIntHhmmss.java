package com.hazard157.lib.core.legacy.valeds.hms;

import static com.hazard157.lib.core.legacy.valeds.hms.ITsResources.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.legacy.widgets.hhmmss.*;

/**
 * Целочисленный контроль редактирования длительности (или временной метки) в формате [ЧЧ:]ММ:СС.
 * <p>
 * В режиме {@link #isEditable()} = <code>false</code> виджет переключается в нередактируемый {@link Text}.
 *
 * @author goga
 */
public class ValedAvIntHhmmss
    extends AbstractValedControl<IAtomicValue, Control> {

  /**
   * Параметр использовать только вид "ММ:СС", а не "ЧЧ:ММ:СС".<br>
   * Тип данных: примитивный {@link EAtomicType#BOOLEAN}<br>
   * Формат: true - будет два спиннера ММ и СС, false - будет три спиннера ЧЧ, ММ и СС.<br>
   * Значение по умолчанию: <code>false</code>
   */
  public static IDataDef IS_ONLY_MMSS = DataDef.create( "ValedAvIntHhmmss", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_DAV_INT_HHMMSS_ONLY_MMSS, //
      TSID_DESCRIPTION, STR_D_DAV_INT_HHMMSS_ONLY_MMSS, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Valed factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvIntHhmmss"; //$NON-NLS-1$

  /**
   * Valed factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvIntHhmmss( aContext );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateViewer( ITsGuiContext aContext ) {
      return new ValedAvIntHhmmssReadOnly( aContext );
    }

  };

  private TsComposite backplane = null;

  /**
   * В режиме {@link #isEditable()} = <code>false</code> отображается текстом.
   */
  private Text text = null;

  /**
   * В режиме {@link #isEditable()} = <code>true</code> отображается как [HH:]MM:SS.
   */
  HmsWidget hmsWidget = null;

  IAtomicValue value = IAtomicValue.NULL;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedAvIntHhmmss( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void recreateWidgets() {
    boolean isTextOnly = text != null;
    boolean isNoWidgets = text == null && hmsWidget == null;
    if( !isNoWidgets ) {
      if( isTextOnly != isEditable() ) {
        return; // не изменился isReadonly(), не надо пересоздавать виджеты
      }
    }
    backplane.setLayoutDeferred( true );
    try {
      if( text != null ) {
        text.dispose();
        text = null;
      }
      if( hmsWidget != null ) {
        value = avInt( hmsWidget.getValue() );
        hmsWidget.dispose();
        hmsWidget = null;
      }
      if( isEditable() ) {
        hmsWidget = new HmsWidget( backplane, HmsWidget.makeStyle( !getIsOnlyMmss(), false, false ) );
        updateWidgetLimits();
        hmsWidget.genericChangeEventer().addListener( aSource -> {
          value = avInt( hmsWidget.getValue() );
          fireModifyEvent( true );
        } );
        value = avInt( hmsWidget.getValue() );
        hmsWidget.addFocusListener( notifyEditFinishedOnFocusLostListener );
      }
      else {
        text = new Text( backplane, SWT.BORDER );
        text.setEditable( false );
      }
      displayValue();
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true );
      backplane.layout( true );
    }
  }

  private void updateWidgetLimits() {
    TsInternalErrorRtException.checkNull( hmsWidget );
    int maxSecs = getIsOnlyMmss() ? MAX_MMSS_VALUE : MAX_HHMMSS_VALUE;
    int minValue = getMinValue();
    int maxValue = getMaxValue();
    if( minValue < 0 ) {
      minValue = 0;
    }
    else {
      if( minValue >= maxSecs ) {
        minValue = maxSecs - 1;
      }
    }
    if( maxValue <= 0 ) {
      maxValue = 1;
    }
    else {
      if( maxValue >= maxSecs ) {
        maxValue = maxSecs;
      }
    }

    if( maxValue <= minValue ) {
      minValue = maxValue - 1;
    }
    hmsWidget.setLimits( minValue, maxValue );
  }

  private void displayValue() {
    if( text != null ) {
      if( value.isAssigned() ) {
        String s;
        if( getIsOnlyMmss() ) {
          s = HmsUtils.mmss( value.asInt() );
        }
        else {
          s = HmsUtils.hhmmss( value.asInt() );
        }
        text.setText( s );
      }
      else {
        text.setText( TsLibUtils.EMPTY_STRING );
      }
    }
    if( hmsWidget != null ) {
      if( value.isAssigned() ) {
        hmsWidget.setValue( value.asInt() );
      }
      else {
        hmsWidget.setValue( getMinValue() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractDavControl
  //

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    if( isWidget() ) {
      updateWidgetLimits();
    }
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      boolean wasEditable = hmsWidget != null;
      if( wasEditable != isEditable() ) {
        recreateWidgets();
      }
    }
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new TsComposite( aParent );
    backplane.setLayout( new FillLayout() );
    recreateWidgets();
    return backplane;
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    value = aValue == null ? IAtomicValue.NULL : aValue;
    displayValue();
  }

  @Override
  protected void doClearValue() {
    value = IAtomicValue.NULL;
    displayValue();
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает значение параметра {@link #IS_ONLY_MMSS}.
   *
   * @return boolean - значение параметра {@link #IS_ONLY_MMSS}
   */
  public boolean getIsOnlyMmss() {
    return params().getBool( IS_ONLY_MMSS );
  }

  /**
   * Задает значение параметра {@link #IS_ONLY_MMSS}.
   *
   * @param aValue boolean - значение параметра {@link #IS_ONLY_MMSS}
   */
  public void setIsOnlyMmss( boolean aValue ) {
    params().setBool( IS_ONLY_MMSS, aValue );
  }

  /**
   * Возвращает значение параметра {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}, или
   * {@link IAvMetaConstants#TSID_MIN_EXCLUSIVE} + 1.
   *
   * @return int - значение параметра
   */
  public int getMinValue() {
    int minValue = Integer.MIN_VALUE;
    if( params().hasValue( TSID_MIN_INCLUSIVE ) ) {
      minValue = params().getInt( TSID_MIN_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MIN_EXCLUSIVE ) ) {
        minValue = params().getInt( TSID_MIN_EXCLUSIVE ) + 1;
      }
    }
    return minValue;
  }

  /**
   * Возвращает значение параметра {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}, или
   * {@link IAvMetaConstants#TSID_MAX_EXCLUSIVE} - 1.
   *
   * @return int - значение параметра
   */
  public int getMaxValue() {
    int maxValue = Integer.MAX_VALUE;
    if( params().hasValue( TSID_MAX_INCLUSIVE ) ) {
      maxValue = params().getInt( TSID_MAX_INCLUSIVE );
    }
    else {
      if( params().hasValue( TSID_MAX_EXCLUSIVE ) ) {
        maxValue = params().getInt( TSID_MAX_EXCLUSIVE ) - 1;
      }
    }
    return maxValue;
  }

  /**
   * Задает параметры виджета.
   *
   * @param aMinValue int - значение параметра {@link IAvMetaConstants#TSID_MIN_INCLUSIVE}
   * @param aMaxValue int - значение параметра {@link IAvMetaConstants#TSID_MAX_INCLUSIVE}
   * @throws TsIllegalArgumentRtException aPageStep < aStep
   * @throws TsIllegalArgumentRtException aMaxValue < aMinValue
   * @throws TsIllegalArgumentRtException aMaxValue или aMinValue выходит за 0..{@link HmsUtils#MAX_HHMMSS_VALUE}
   */
  public void setLimits( int aMinValue, int aMaxValue ) {
    TsIllegalArgumentRtException.checkTrue( aMaxValue < aMinValue );
    TsIllegalArgumentRtException.checkTrue( aMaxValue < 0 || aMaxValue > MAX_HHMMSS_VALUE );
    TsIllegalArgumentRtException.checkTrue( aMinValue < 0 || aMinValue > MAX_HHMMSS_VALUE );
    IOptionSetEdit ops = new OptionSet();
    ops.setInt( TSID_MIN_INCLUSIVE, aMinValue );
    ops.setInt( TSID_MAX_INCLUSIVE, aMaxValue );
    params().addAll( ops );
  }

}
