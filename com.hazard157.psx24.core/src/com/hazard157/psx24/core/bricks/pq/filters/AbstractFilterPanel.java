package com.hazard157.psx24.core.bricks.pq.filters;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Базовый класс панели настройки (создания параметров) единичного фильтра заданного типа.
 *
 * @author hazard157
 */
public abstract class AbstractFilterPanel
    extends AbstractTsDialogPanel<ITsSingleFilterParams, ITsGuiContext> {

  private boolean inverted = false;

  /**
   * Конструктор панели, предназаначенной для использования вне диалога.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aData T - начальные данные для отображения, может быть null
   * @param aContext C - контекст радктирования / показа структры данных T
   * @param aFlags int - флаги настройки панели, собранные по ИЛИ биты {@link TsDialog}.DF_XXX
   * @throws TsNullArgumentRtException aParent или aContext = null
   */
  public AbstractFilterPanel( Composite aParent, ITsSingleFilterParams aData, ITsGuiContext aContext, int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
  }

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aOwnerDialog {@link TsDialog} - родительский диалог
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public AbstractFilterPanel( Composite aParent, TsDialog<ITsSingleFilterParams, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
  }

  // ------------------------------------------------------------------------------------
  // Для наследников
  //

  @SuppressWarnings( "javadoc" )
  public IUnitEpisodes uEpisodes() {
    return tsContext().get( IUnitEpisodes.class );
  }

  @SuppressWarnings( "javadoc" )
  public IUnitTags uTags() {
    return tsContext().get( IUnitTags.class );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @SuppressWarnings( "javadoc" )
  public void setFilterParams( ITsSingleFilterParams aParams ) {
    if( aParams != null ) {
      doSetDataRecord( aParams );
    }
    else {
      doSetDataRecord( ITsSingleFilterParams.NONE );
    }
  }

  @SuppressWarnings( "javadoc" )
  public ITsSingleFilterParams getFilterParams() {
    ITsSingleFilterParams p = doGetDataRecord();
    if( p != null ) {
      return p;
    }
    return ITsSingleFilterParams.NONE;
  }

  @SuppressWarnings( "javadoc" )
  public void setInverted( boolean aInvert ) {
    inverted = aInvert;
  }

  @SuppressWarnings( "javadoc" )
  public boolean isInverted() {
    return inverted;
  }

  /**
   * Сбрасывает фильтр на начальные установки, обычно {@link ITsSingleFilterParams#NONE}.
   */
  public abstract void resetFilter();

}
