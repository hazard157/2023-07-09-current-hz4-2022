package com.hazard157.psx24.core.valeds.frames;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx24.core.glib.frlstviewer_ep.*;

/**
 * Редактор поля {@link IFrameable#frame()}.
 *
 * @author hazard157
 */
public class ValedFrameEditor
    extends AbstractValedControl<IFrame, Control> {

  private final ITsSelectionChangeListener<IFrame> viewerSelectionChangeListener = ( aSource, aSelectedItem ) -> {
    if( aSelectedItem != null ) {
      this.lastValueBeforeCriteriaChange = aSelectedItem;
    }
  };

  private final IEpisodeFramesListViewer framesListViewer;

  /**
   * Значение, заданное в последний раз методом {@link #doSetUnvalidatedValue(IFrame)}.
   * <p>
   * Нужно запоминать, поскольку в момент задания критерии {@link #getSelectionCriteria()} может быть еще не задан
   * корректно, и значение может быть потеряно. Это значение также меняется при выборе кадра пользователем в списке.
   */
  IFrame lastValueBeforeCriteriaChange = null;

  /**
   * Конструктор.
   *
   * @param aTsContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedFrameEditor( ITsGuiContext aTsContext ) {
    super( aTsContext );
    params().setInt( IValedControlConstants.OPID_VERTICAL_SPAN, 15 );
    framesListViewer = new EpisodeFramesListViewer( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    framesListViewer.createControl( aParent );
    framesListViewer.addTsSelectionListener( viewerSelectionChangeListener );
    return framesListViewer.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // TODO ??? framesListViewer.setEnabled( aEditable );
  }

  @Override
  protected IFrame doGetUnvalidatedValue() {
    IFrame sel = framesListViewer.selectedItem();
    if( sel != null ) {
      return sel;
    }
    return IFrame.NONE;
  }

  @Override
  protected void doSetUnvalidatedValue( IFrame aValue ) {
    lastValueBeforeCriteriaChange = aValue;
    framesListViewer.setSelectedItem( aValue );
  }

  @Override
  protected void doClearValue() {
    framesListViewer.setSelectedItem( null );
  }

  // ------------------------------------------------------------------------------------
  // Дополнительное API
  //

  /**
   * Изменяет список по новому критерию показа кадров.
   *
   * @param aCriteria {@link FrameSelectionCriteria} - новый криетрии
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setSelectionCriteria( FrameSelectionCriteria aCriteria ) {
    framesListViewer.setCriteria( aCriteria );
    framesListViewer.setSelectedItem( lastValueBeforeCriteriaChange );
  }

  /**
   * Возвращает текущий критерии показа кадров.
   * <p>
   * Текущий критерий состоит из приведенных значений по умолчанию каждого из индивидуальных параметров.
   *
   * @return {@link FrameSelectionCriteria} - критерии показа кадров
   */
  public FrameSelectionCriteria getSelectionCriteria() {
    return framesListViewer.getCriteria();
  }

}
