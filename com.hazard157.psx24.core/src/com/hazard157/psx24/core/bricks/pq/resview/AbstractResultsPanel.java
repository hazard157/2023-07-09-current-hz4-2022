package com.hazard157.psx24.core.bricks.pq.resview;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.stuff.svin.*;

/**
 * Базовый класс панели отображения наследниками в разных видах.
 *
 * @author hazard157
 */
abstract class AbstractResultsPanel
    extends TsPanel
    implements ITsSelectionProvider<Svin> {

  protected final TsSelectionChangeEventHelper<Svin> selectionChangeEventHelper;

  private ISvinSeq results = ISvinSeq.EMPTY;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public AbstractResultsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает отображаемые результаты
   *
   * @return {@link ISvinSeq} - отображаемые результаты
   */
  public ISvinSeq getPqResults() {
    return results;
  }

  /**
   * Задает отображаемые результаты.
   *
   * @param aResults {@link ISvinSeq} - отображаемые результаты
   */
  public void setPqResults( ISvinSeq aResults ) {
    TsNullArgumentRtException.checkNull( aResults );
    results = aResults;
    refresh();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<Svin> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<Svin> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  @Override
  public abstract Svin selectedItem();

  @Override
  public abstract void setSelectedItem( Svin aItem );

  // ------------------------------------------------------------------------------------
  // Для переопределения наследниками
  //

  protected IList<Svin> getSelectionSvins() {
    Svin sel = selectedItem();
    if( sel != null ) {
      return new SingleItemList<>( sel );
    }
    return IList.EMPTY;
  }

  public IList<Svin> getAllSvins() {
    return results.svins();
  }

  protected abstract void refresh();
}
