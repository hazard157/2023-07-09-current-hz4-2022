package com.hazard157.psx24.core.glib.plv;

/**
 * Настройка границ и интервалов рисования сетки значков.
 *
 * @author hazard157
 */
class PgMargins {

  private static final int MIN_PROP_VALUE = 0;
  private static final int MAX_PROP_VALUE = 16;

  private int borderWidth = 3;
  private int leftMargin = 3;
  private int rightMargin = 3;
  private int topMargin = 3;
  private int bottomMargin = 3;
  private int horInterval = 3;
  private int verInterval = 3;

  /**
   * Создает объект со значениями по умолчанию.
   */
  PgMargins() {
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
  // API класса
  //

  /**
   * Возвращает толщину границы вокруг ячейки сетки.
   *
   * @return - int толщина границы вокруг ячейки сетки в пикселях
   */
  public int borderWidth() {
    return borderWidth;
  }

  void setBorderWidth( int aBorderWidth ) {
    borderWidth = ensureInRange( aBorderWidth );
  }

  /**
   * Возвращает расстояние между сеткой и левым краем панели.
   *
   * @return int - расстояние между сеткой и левым краем панели в пикселях
   */
  public int leftMargin() {
    return leftMargin;
  }

  void setLeftMargin( int aLeftMargin ) {
    leftMargin = ensureInRange( aLeftMargin );
  }

  /**
   * Возвращает расстояние между сеткой и правым краем панели.
   *
   * @return int - расстояние между сеткой и правым краем панели в пикселях
   */
  public int rightMargin() {
    return rightMargin;
  }

  void setRightMargin( int aRightMargin ) {
    rightMargin = ensureInRange( aRightMargin );
  }

  /**
   * Возвращает расстояние между сеткой и верхним краем панели.
   *
   * @return int - расстояние между сеткой и верхним краем панели в пикселях
   */
  public int topMargin() {
    return topMargin;
  }

  void setTopMargin( int aTopMargin ) {
    topMargin = ensureInRange( aTopMargin );
  }

  /**
   * Возвращает расстояние между сеткой и нижним краем панели.
   *
   * @return int - расстояние между сеткой и нижним краем панели в пикселях
   */
  public int bottomMargin() {
    return bottomMargin;
  }

  void setBottomMargin( int aBottomMargin ) {
    bottomMargin = ensureInRange( aBottomMargin );
  }

  /**
   * Возвращает расстояние между ячейками сетки по горизонтали.
   *
   * @return int - расстояние между ячейками сетки по горизонтали в пикселях
   */
  public int horInterval() {
    return horInterval;
  }

  void setHorInterval( int aHorInterval ) {
    horInterval = ensureInRange( aHorInterval );
  }

  /**
   * Возвращает расстояние между ячейками сетки по вертикали.
   *
   * @return int - расстояние между ячейками сетки по вертикали в пикселях
   */
  public int verInterval() {
    return verInterval;
  }

  void setVerInterval( int aVerInterval ) {
    verInterval = ensureInRange( aVerInterval );
  }

}
