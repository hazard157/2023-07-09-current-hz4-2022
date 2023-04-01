package com.hazard157.lib.core.glib.cellsgrid;

import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Редактируемая реализация {@link IGridMargins}.
 *
 * @author hazard157
 */
public class GridMargins
    implements IGridMargins {

  /**
   * Минимально допустимое значение любого параметра.
   * <p>
   * Параметры меньше минимального значение насильно принимают это значение.
   */
  public static final int MIN_PROP_VALUE = 0;

  /**
   * Максимально допустимое значение любого параметра.
   * <p>
   * Параметры меньше максимального значение насильно принимают это значение.
   */
  public static final int MAX_PROP_VALUE = 128;

  private int borderWidth  = 3;
  private int leftMargin   = 3;
  private int rightMargin  = 3;
  private int topMargin    = 3;
  private int bottomMargin = 3;
  private int horInterval  = 3;
  private int verInterval  = 3;

  /**
   * Создает объект со значениями по умолчанию.
   */
  public GridMargins() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private static int ensureInRange( int aPropValue ) {
    if( aPropValue < MIN_PROP_VALUE ) {
      return MIN_PROP_VALUE;
    }
    if( aPropValue > MAX_PROP_VALUE ) {
      return MAX_PROP_VALUE;
    }
    return aPropValue;
  }

  // ------------------------------------------------------------------------------------
  // IPgvMargins
  //

  @Override
  public int borderWidth() {
    return borderWidth;
  }

  @Override
  public int leftMargin() {
    return leftMargin;
  }

  @Override
  public int rightMargin() {
    return rightMargin;
  }

  @Override
  public int topMargin() {
    return topMargin;
  }

  @Override
  public int bottomMargin() {
    return bottomMargin;
  }

  @Override
  public int horInterval() {
    return horInterval;
  }

  @Override
  public int verInterval() {
    return verInterval;
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Копирует значения из источника в этот экземпляр.
   *
   * @param aSource {@link IGridMargins} - зисточника
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void copyFrom( IGridMargins aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    topMargin = ensureInRange( aSource.topMargin() );
    bottomMargin = ensureInRange( aSource.bottomMargin() );
    leftMargin = ensureInRange( aSource.leftMargin() );
    rightMargin = ensureInRange( aSource.rightMargin() );
    horInterval = ensureInRange( aSource.hashCode() );
    verInterval = ensureInRange( aSource.verInterval() );
    borderWidth = ensureInRange( aSource.borderWidth() );
  }

  /**
   * Задает {@link #borderWidth()}.
   *
   * @param aBorderWidth int - значение
   */
  public void setBorderWidth( int aBorderWidth ) {
    borderWidth = ensureInRange( aBorderWidth );
  }

  /**
   * Задает {@link #leftMargin()}.
   *
   * @param aLeftMargin int - значение
   */
  public void setLeftMargin( int aLeftMargin ) {
    leftMargin = ensureInRange( aLeftMargin );
  }

  /**
   * Задает {@link #rightMargin()}.
   *
   * @param aRightMargin int - значение
   */
  public void setRightMargin( int aRightMargin ) {
    rightMargin = ensureInRange( aRightMargin );
  }

  /**
   * Задает {@link #topMargin()}.
   *
   * @param aTopMargin int - значение
   */
  public void setTopMargin( int aTopMargin ) {
    topMargin = ensureInRange( aTopMargin );
  }

  /**
   * Задает {@link #bottomMargin()}.
   *
   * @param aBottomMargin int - значение
   */
  public void setBottomMargin( int aBottomMargin ) {
    bottomMargin = ensureInRange( aBottomMargin );
  }

  /**
   * Задает {@link #horInterval()}.
   *
   * @param aHorInterval int - значение
   */
  public void setHorInterval( int aHorInterval ) {
    horInterval = ensureInRange( aHorInterval );
  }

  /**
   * Задает {@link #verInterval()}.
   *
   * @param aVerInterval int - значение
   */
  public void setVerInterval( int aVerInterval ) {
    verInterval = ensureInRange( aVerInterval );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "Margins: Top=%d, Bottom=%d, Left=%d, Right=%d, Hor=%d, Ver=%d, Border=%d", //$NON-NLS-1$
        topMargin, bottomMargin, leftMargin, rightMargin, horInterval, verInterval, borderWidth );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof IGridMargins ) {
      IGridMargins that = (IGridMargins)aObj;
      return this.topMargin == that.topMargin() && this.bottomMargin == that.bottomMargin()
          && this.leftMargin == that.leftMargin() && this.rightMargin == that.rightMargin()
          && this.horInterval == that.horInterval() && this.verInterval == that.verInterval()
          && this.borderWidth == that.borderWidth();
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + topMargin;
    result = TsLibUtils.PRIME * result + bottomMargin;
    result = TsLibUtils.PRIME * result + leftMargin;
    result = TsLibUtils.PRIME * result + rightMargin;
    result = TsLibUtils.PRIME * result + horInterval;
    result = TsLibUtils.PRIME * result + verInterval;
    result = TsLibUtils.PRIME * result + borderWidth;
    return result;
  }

}
