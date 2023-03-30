package com.hazard157.psx24.core.valeds.frames;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.frame.*;
import com.hazard157.psx.common.stuff.fsc.*;
import com.hazard157.psx24.core.glib.frlstviewer_ep.*;

/**
 * Редактор поля {@link IFrameable#frame()}.
 *
 * @author goga
 */
public class ValedAvFrameEditor
    extends AbstractValedControl<IAtomicValue, Control> {

  private final ITsSelectionChangeListener<IFrame> viewerSelectionChangeListener = ( aSource, aSelectedItem ) -> {
    if( aSelectedItem != null ) {
      this.lastValueBeforeCriteraChange = aSelectedItem;
    }
  };

  private final IEpisodeFramesListViewer framesListViewer;

  /**
   * Значение, заданное в последний раз методом {@link #doSetUnvalidatedValue(IAtomicValue)}.
   * <p>
   * Нужно запоминать, поскольку в момент задания критерии {@link #getSelectionCriteria()} может быть еще не задан
   * корректно, и значение может быть потеряно. Это значение также меняется при выборе кадра пользователем в списке.
   */
  IFrame lastValueBeforeCriteraChange = null;

  /**
   * Конструктор.
   *
   * @param aTsContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedAvFrameEditor( ITsGuiContext aTsContext ) {
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
  protected IAtomicValue doGetUnvalidatedValue() {
    IFrame sel = framesListViewer.selectedItem();
    if( sel != null ) {
      return avValobj( sel );
    }
    return avValobj( IFrame.NONE );
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    IFrame frame = aValue == null ? IFrame.NONE : aValue.asValobj();
    lastValueBeforeCriteraChange = frame;
    framesListViewer.setSelectedItem( frame );
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
    framesListViewer.setSelectedItem( lastValueBeforeCriteraChange );
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
