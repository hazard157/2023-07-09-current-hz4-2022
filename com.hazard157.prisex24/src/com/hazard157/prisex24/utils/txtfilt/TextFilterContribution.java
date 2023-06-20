package com.hazard157.prisex24.utils.txtfilt;

import static com.hazard157.prisex24.utils.txtfilt.IPsxResources.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.util.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Простая панель фильтра по введенной текстовой строке, добавляемая в панель инструментов {@link TsToolbar}.
 * <p>
 * Содержит кнопку -защелку включения фильтра и поле ввода текста. Считается, что когда кнопка зафиксирована в нажатом
 * состоянии, фильтрация включена. Получить состояние кнопки (включения фильтрации) нужно методом {@link #isFilterOn()}.
 * Текст фильтра возвращается методом {@link #getFilterText()}. Изменения состояния включения фильтра и редактирования
 * текста вызывает генерацию сообщения {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
 * <p>
 * Программное задание состояния включения фильтра {@link #setFilterOn(boolean)} и текста {@link #setFilterText(String)}
 * не приводит к генерации сообщений.
 *
 * @author hazard157
 */
public class TextFilterContribution
    implements IGenericChangeEventCapable {

  private static final String FILTER_TEXT_CONTRIBUTION_ITEM_ID = "FilterText"; //$NON-NLS-1$

  final GenericChangeEventer eventer;

  final TsToolbar toolbar;
  boolean         emptyTextMeansOff = true;
  Text            txtFilter         = null;

  /**
   * Конструктор.
   * <p>
   * Конструктор добавляет в конец панели инструемнтов кпоку-защелку {@link ITsStdActionDefs#ACDEF_FILTER} и контроль
   * редактирования текста фильтра.
   *
   * @param aToolbar {@link TsToolbar} - панель инструментов
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public TextFilterContribution( TsToolbar aToolbar ) {
    TsNullArgumentRtException.checkNull( aToolbar );
    toolbar = aToolbar;
    eventer = new GenericChangeEventer( this );
    aToolbar.addActionDef( new TsActionDef( ACDEF_FILTER.id(), IAction.AS_CHECK_BOX, ACDEF_FILTER.params() ) );
    aToolbar.addContributionItem( new ControlContribution( FILTER_TEXT_CONTRIBUTION_ITEM_ID ) {

      @Override
      protected Control createControl( Composite aParent ) {
        txtFilter = new Text( aParent, SWT.BORDER );
        txtFilter.setMessage( MSG_ENTER_FILTER_TEXT );
        txtFilter.addModifyListener( aE -> {
          if( toolbar.isActionChecked( ACTID_FILTER ) ) {
            eventer.fireChangeEvent();
          }
        } );
        return txtFilter;
      }
    } );
    aToolbar.addListener( aActionId -> {
      if( Objects.equals( aActionId, ACTID_FILTER ) ) {
        eventer.fireChangeEvent();
      }
    } );
    aToolbar.setActionChecked( ACTID_FILTER, true );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Определяет, интерпретируется ли пустая текстова строка как отключенный фильтр методлом {@link #isFilterOn()}.
   * <p>
   * Изначально возвращает <code>true</code>, то есть, при пустой строке {@link #getFilterText()} метод
   * {@link #isFilterOn()} возвращает значение <code>false</code>, вне зависимости от положения кнопки-защелки
   * {@link ITsStdActionDefs#ACDEF_FILTER}.
   *
   * @return boolean - признак отключения фильтра при пустой строке
   */
  public boolean isEmptyTextOffFilter() {
    return emptyTextMeansOff;
  }

  /**
   * Задает признак {@link #isEmptyTextOffFilter()}.
   *
   * @param aEmptyTextTurnsFilterOff boolean - признак отключения фильтра при пустой строке
   */
  public void setEmptyTextOffFilter( boolean aEmptyTextTurnsFilterOff ) {
    emptyTextMeansOff = aEmptyTextTurnsFilterOff;
  }

  /**
   * Возвращает признак включения фильтра.
   * <p>
   * Обратите внимание, что нажатая кнопка не всегда означает включенный фильтр {@link #isFilterOn()} =
   * <code>true</code>. Пояснения смотри к {@link #isEmptyTextOffFilter()}.
   *
   * @return boolean - призак использования фильтра
   */
  public boolean isFilterOn() {
    if( toolbar.isActionChecked( ACTID_FILTER ) ) {
      if( !emptyTextMeansOff || !getFilterText().isEmpty() ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Менят состояние кнопки {@link ITsStdActionDefs#ACDEF_FILTER}.
   * <p>
   * Обратите внимание, что нажатая кнопка не всегда означает включенный фильтр {@link #isFilterOn()} =
   * <code>true</code>. Пояснения смотри к {@link #isEmptyTextOffFilter()}.
   * <p>
   * Не генерирует сообщение {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aOn boolean - состояние кнопки<br>
   *          <b>true</b> - кнопка будет нажата;<br>
   *          <b>false</b> - кнопка будет отжата.
   */
  public void setFilterOn( boolean aOn ) {
    toolbar.setActionChecked( ACTID_FILTER, aOn );
  }

  /**
   * Возвращает текст фильтра.
   *
   * @return Ыекштп - текст фильтра
   */
  public String getFilterText() {
    return txtFilter.getText();
  }

  /**
   * Задает текст фильтра.
   * <p>
   * Не генерирует сообщение {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   *
   * @param aText String - новый текст фильтра
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  public void setFilterText( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    eventer.pauseFiring();
    try {
      txtFilter.setText( aText );
    }
    finally {
      eventer.resumeFiring( false );
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
