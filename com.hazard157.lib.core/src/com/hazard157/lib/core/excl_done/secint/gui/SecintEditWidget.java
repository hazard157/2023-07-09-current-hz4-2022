package com.hazard157.lib.core.excl_done.secint.gui;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.widgets.mpv.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_done.secint.*;

/**
 * Реализация {@link ISecintEditWidget}.
 *
 * @author hazard157
 */
public class SecintEditWidget
    implements ISecintEditWidget {

  private static final String STR_LABEL_MIDDLE   = " - ";   //$NON-NLS-1$
  private static final String STR_LABEL_DURATION = "⁢   -"; //$NON-NLS-1$

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

  private final IMpvSecsDuration startMpv;
  private final IMpvSecsDuration endMpv;
  private final IMpvSecsDuration durationMpv;

  private MultiPartValueWidget startWidget    = null;
  private MultiPartValueWidget endWidget      = null;
  private MultiPartValueWidget durationWidget = null;
  private Label                labelMiddle    = null;
  private Label                labelDur       = null;

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
    startMpv = IMpvSecsDuration.create( false, true );
    endMpv = IMpvSecsDuration.create( false, true );
    durationMpv = IMpvSecsDuration.create( false, true );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  private void internalSetValues( int aStart, int aDuration ) {
    Secint in = allowedRange.inRange( aStart, aStart + aDuration - 1 );
    try {
      isInternalEdit = true;
      startMpv.setValueSecs( in.start() );
      endMpv.setValueSecs( in.end() );
      durationMpv.setValueSecs( in.duration() );
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
    int s = startMpv.getValueSecs();
    int d = durationMpv.getValueSecs();
    internalSetValues( s, d );
  }

  void onEndChanged() {
    int s = startMpv.getValueSecs();
    int e = endMpv.getValueSecs();
    int d = e - s + 1;
    internalSetValues( s, d );
  }

  void onDurationChanged() {
    int s = startMpv.getValueSecs();
    int d = durationMpv.getValueSecs();
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
        saved = new Secint( startMpv.getValueSecs(), endMpv.getValueSecs() );
      }
      // создаем виджеты редакторов
      recreateControls();
      // восстановим сохраненное значение
      startMpv.setValueSecs( saved.start() );
      endMpv.setValueSecs( saved.end() );
      durationMpv.setValueSecs( saved.duration() );
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
    startWidget = new MultiPartValueWidget( board, SWT.NONE, startMpv );
    labelMiddle = new Label( board, SWT.RIGHT );
    labelMiddle.setText( STR_LABEL_MIDDLE );
    endWidget = new MultiPartValueWidget( board, SWT.NONE, endMpv );
    labelDur = new Label( board, SWT.RIGHT );
    labelDur.setText( STR_LABEL_DURATION );
    durationWidget = new MultiPartValueWidget( board, SWT.None, durationMpv );
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
      int s = startMpv.getValueSecs();
      int e = endMpv.getValueSecs();
      try {
        eventer.pauseFiring();
        startMpv.setRange( new IntRange( allowedRange.start(), allowedRange.end() - 1 ) );
        endMpv.setRange( new IntRange( allowedRange.start(), allowedRange.end() ) );
        durationMpv.setRange( new IntRange( 1, allowedRange.duration() ) );
      }
      finally {
        eventer.resumeFiring( false );
      }
      internalSetValues( s, e - s + 1 );
    }
  }

  @Override
  public Secint getValue() {
    return new Secint( startMpv.getValueSecs(), endMpv.getValueSecs() );
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
    // startMpv.setValueSecs( aStart );
    // endMpv.setValueSecs( aEnd );
    // durationMpv.setValueSecs( aEnd - aStart + 1 );
    // }
    // finally {
    // genericChangeListenersHolder.resumeFiring( true );
    // }
  }

}
