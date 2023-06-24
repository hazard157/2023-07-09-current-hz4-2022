package com.hazard157.lib.core.quants.secint.glib;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.hhmmss.*;
import com.hazard157.lib.core.quants.secint.*;

/**
 * Реализация {@link ISecintEditWidget}.
 *
 * @author hazard157
 */
public class SecintEditWidget
    implements ISecintEditWidget {

  private static final String STR_LABEL_MIDDLE   = " - ";   //$NON-NLS-1$
  private static final String STR_LABEL_DURATION = "⁢   ↹"; //$NON-NLS-1$

  private final IGenericChangeListener startChangeListsner = aSource -> {
    if( !this.isInternalEdit ) {
      onStartChanged();
    }
  };

  private final IGenericChangeListener endChangeListsner = aSource -> {
    if( !this.isInternalEdit ) {
      onEndChanged();
    }
  };

  private final IGenericChangeListener durationChangeListsner = aSource -> {
    if( !this.isInternalEdit ) {
      onDurationChanged();
    }
  };

  private final GenericChangeEventer eventer;

  private boolean   onlyMmSs = true;
  private boolean   editable = true;
  private Composite board    = null;

  private Secint allowedRange = new Secint( 0, 119 ); // допустимые пределы интервала

  private HmsWidget startWidget    = null;
  private HmsWidget endWidget      = null;
  private HmsWidget durationWidget = null;
  private Label     labelMiddle    = null;
  private Label     labelDur       = null;

  /**
   * Признак программного (внутри этого виджета) редактирования значений в виджетах.
   * <p>
   * Когда признак выставлен, обработчики xxxChangeListener() отключаются.
   */
  boolean isInternalEdit = false;

  /**
   * Конструктор.
   */
  public SecintEditWidget() {
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void internalSetValues( int aStart, int aDuration ) {
    Secint in = allowedRange.inRange( aStart, aStart + aDuration - 1 );
    try {
      isInternalEdit = true;
      startWidget.setValue( in.start() );
      endWidget.setValue( in.end() );
      durationWidget.setValue( in.duration() );
    }
    catch( Exception ex ) {
      ex.printStackTrace();
      throw ex;
    }
    finally {
      isInternalEdit = false;
      Secint curr = getValue();
      if( in != curr ) {
        eventer.fireChangeEvent();
      }
    }
  }

  void onStartChanged() {
    int s = startWidget.getValue();
    int d = durationWidget.getValue();
    internalSetValues( s, d );
  }

  void onEndChanged() {
    int s = startWidget.getValue();
    int e = endWidget.getValue();
    int d = e - s + 1;
    internalSetValues( s, d );
  }

  void onDurationChanged() {
    int s = startWidget.getValue();
    int d = durationWidget.getValue();
    internalSetValues( s, d );
  }

  void reinitWidget() {
    if( board == null ) {
      return;
    }
    board.setLayoutDeferred( true );
    try {
      // сохраним текущее значение интервала
      Secint saved = allowedRange;
      if( startWidget != null ) {
        saved = new Secint( startWidget.getValue(), endWidget.getValue() );
      }
      // создаем виджеты редакторов
      recreateControls();
      // восстановим сохраненное значение
      startWidget.setValue( saved.start() );
      endWidget.setValue( saved.end() );
      durationWidget.setValue( saved.duration() );
      // теперь можно и слушатели поставить
      startWidget.genericChangeEventer().addListener( startChangeListsner );
      endWidget.genericChangeEventer().addListener( endChangeListsner );
      durationWidget.genericChangeEventer().addListener( durationChangeListsner );
    }
    finally {
      board.setLayoutDeferred( false );
    }
  }

  private void recreateControls() {
    if( startWidget != null ) {
      startWidget.dispose();
      endWidget.dispose();
      labelDur.dispose();
      durationWidget.dispose();
    }
    RowLayout rowLayout = new RowLayout( SWT.HORIZONTAL );
    rowLayout.marginLeft = 0;
    rowLayout.marginRight = 0;
    rowLayout.marginTop = 0;
    rowLayout.marginBottom = 0;
    rowLayout.pack = true;
    rowLayout.justify = false;
    rowLayout.center = true;
    board.setLayout( rowLayout );
    int style = HmsWidget.makeStyle( !onlyMmSs, !editable, false );
    startWidget = new HmsWidget( board, style );
    labelMiddle = new Label( board, SWT.RIGHT );
    labelMiddle.setText( STR_LABEL_MIDDLE );
    endWidget = new HmsWidget( board, style );
    labelDur = new Label( board, SWT.RIGHT );
    labelDur.setText( STR_LABEL_DURATION );
    durationWidget = new HmsWidget( board, style );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILazyControl
  //

  @Override
  public Control createControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NONE );
    reinitWidget();
    return board;
  }

  @Override
  public Control getControl() {
    return board;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ISecintEditWidget
  //

  @Override
  public boolean isOnlyMmSs() {
    return onlyMmSs;
  }

  @Override
  public void setOnlyMmSs( boolean aIsOnlyMmSs ) {
    if( onlyMmSs != aIsOnlyMmSs ) {
      onlyMmSs = aIsOnlyMmSs;
      reinitWidget();
    }
  }

  @Override
  public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( editable != aEditable ) {
      editable = aEditable;
      reinitWidget();
    }
  }

  @Override
  public Secint getLimits() {
    return allowedRange;
  }

  @Override
  public void setLimits( int aMinStart, int aMaxEnd ) {
    allowedRange = new Secint( aMinStart, aMaxEnd );
    // обновим контроли, если они уже созданы
    if( getControl() != null ) {
      int s = startWidget.getValue();
      int e = endWidget.getValue();
      try {
        eventer.pauseFiring();
        startWidget.setLimits( allowedRange.start(), allowedRange.end() - 1 );
        endWidget.setLimits( allowedRange.start(), allowedRange.end() );
        durationWidget.setLimits( 1, allowedRange.duration() );
      }
      finally {
        eventer.resumeFiring( false );
      }
      internalSetValues( s, e - s + 1 );
    }
  }

  @Override
  public Secint getValue() {
    return new Secint( startWidget.getValue(), endWidget.getValue() );
  }

  @Override
  public void setValue( Secint aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    setValue( aValue.start(), aValue.end() );
  }

  @Override
  public void setValue( int aStart, int aEnd ) {
    TsValidationFailedRtException.checkError( Secint.checkCanCreate( aStart, aEnd ) );
    internalSetValues( aStart, aEnd - aStart + 1 );
    // try {
    // genericChangeListenersHolder.pauseFiring();
    // startWidget.setValue( aStart );
    // endWidget.setValue( aEnd );
    // durationWidget.setValue( aEnd - aStart + 1 );
    // }
    // finally {
    // genericChangeListenersHolder.resumeFiring( true );
    // }
  }

}
