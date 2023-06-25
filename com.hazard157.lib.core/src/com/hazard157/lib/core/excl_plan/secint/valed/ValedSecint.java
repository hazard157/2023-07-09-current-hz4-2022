package com.hazard157.lib.core.excl_plan.secint.valed;

import static com.hazard157.lib.core.excl_plan.secint.valed.IPsxResources.*;
import static org.toxsoft.core.tsgui.utils.HmsUtils.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.lib.core.excl_plan.secint.glib.*;

/**
 * Редактор (и просмотрщик) поля типа {@link Secint}.
 *
 * @author hazard157
 */
public class ValedSecint
    extends AbstractValedControl<Secint, Control> {

  /**
   * Минимальное значение начала интервала.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: значение в секундах (вклюительно)<br>
   * Значение по умолчанию: 0
   */
  public static final IDataDef MIN_START = DataDef.create( "ValedSecint.MinStart", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_MIN_START, //
      TSID_DESCRIPTION, STR_D_MIN_START, //
      TSID_DEFAULT_VALUE, AV_0 //
  );

  /**
   * Максимальное значение окончания интервала.<br>
   * Тип данных: примитивный {@link EAtomicType#INTEGER}<br>
   * Формат: значение в секундах (вклюительно)<br>
   * Значение по умолчанию: {@link HmsUtils#MAX_MMSS_VALUE} - 1
   */
  public static final IDataDef MAX_END = DataDef.create( "ValedSecint.MaxEnd", INTEGER, //$NON-NLS-1$
      TSID_NAME, STR_N_MAX_END, //
      TSID_DESCRIPTION, STR_D_MAX_END, //
      TSID_DEFAULT_VALUE, avInt( MAX_MMSS_VALUE - 1 ) //
  );

  private final ISecintEditWidget widget;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedSecint( ITsGuiContext aContext ) {
    super( aContext );
    widget = new SecintEditWidget();
    widget.genericChangeEventer().addListener( widgetValueChangeListener );
  }

  private static int putInRange( int aSec ) {
    if( aSec < 0 ) {
      return 0;
    }
    if( aSec > MAX_MMSS_VALUE ) {
      return MAX_MMSS_VALUE;
    }
    return aSec;
  }

  private void setLimits() {
    if( widget.getControl() == null ) {
      return;
    }
    int minStart = putInRange( params().getInt( MIN_START ) );
    int maxEnd = putInRange( params().getInt( MAX_END ) );
    if( maxEnd > minStart ) {
      widget.setLimits( minStart, maxEnd );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    widget.setOnlyMmSs( true );
    widget.createControl( aParent );
    setLimits();
    return widget.getControl();
  }

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    setLimits();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    widget.setEditable( aEditable );
  }

  @Override
  protected Secint doGetUnvalidatedValue() {
    return widget.getValue();
  }

  @Override
  protected void doSetUnvalidatedValue( Secint aValue ) {
    if( aValue != null ) {
      widget.setValue( aValue );
    }
    else {
      widget.setValue( Secint.ZERO );
    }
  }

  @Override
  protected void doClearValue() {
    widget.setValue( Secint.ZERO );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает допустимые пределы изменения редактируемого значения.
   * <p>
   * Контроль гарантирует, что ни программно, и не с помощю GUI нельзя задать интервал, выходящий за эти пределы.
   *
   * @return {@link Secint} - интервал внутри которого всегда находится значение {@link #getValue()}
   */
  public Secint getLimits() {
    return widget.getLimits();
  }

  /**
   * Задает новые пределы редактируемого значения.
   *
   * @param aLimits Secint - допустимый границы редактирования интервала {@link Secint#start()}
   * @throws TsValidationFailedRtException не прошла проверка {@link Secint#checkCanCreate(int, int)}
   */
  public void setLimits( Secint aLimits ) {
    widget.setLimits( aLimits.start(), aLimits.end() );
  }

}
