package com.hazard157.lib.core.glib.pgviewer.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.glib.cellsgrid.*;
import com.hazard157.lib.core.glib.pgviewer.*;

/**
 * Реализация {@link IPicsGridViewer}.
 *
 * @author goga
 * @param <V> - тип ображаемых сущностей
 */
public class PicsGridViewer<V>
    extends TsStdEventsProducerPanel<V>
    implements IPicsGridViewer<V> {

  private final IListEdit<V>      items = new ElemLinkedBundleList<>();
  private final ScrolledComposite scroller;
  private final PgvCanvas<V>      canvas;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  @SuppressWarnings( "unchecked" )
  public PicsGridViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    scroller = new ScrolledComposite( this, SWT.BORDER | SWT.V_SCROLL );
    scroller.setLayoutData( BorderLayout.CENTER );
    canvas = new PgvCanvas<>( scroller, tsContext(), this );
    scroller.setMinSize( new Point( 32, Integer.MAX_VALUE ) );
    scroller.setExpandHorizontal( true );
    // scroller.setExpandVertical( true );
    scroller.setContent( canvas );
    scroller.layout();
    scroller.addDisposeListener( aE -> setItems( IList.EMPTY ) );
    adjustMinSize();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void adjustMinSize() {
    IGridMargins m = canvas.pgvGetMargins();
    setMinimumHeight( thumbSize().size() + 4 * m.borderWidth() + m.bottomMargin() + m.topMargin() );
    setMinimumWidth( thumbSize().size() + 4 * m.borderWidth() + m.leftMargin() + m.rightMargin() );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public V selectedItem() {
    int index = canvas.pgvGetSelectionIndex();
    if( index < 0 ) {
      return null;
    }
    return items.get( index );
  }

  @Override
  public void setSelectedItem( V aItem ) {
    int index = -1;
    if( aItem != null ) {
      index = items.indexOf( aItem );
    }
    canvas.pgvSetSelectedIndex( index );
  }

  // ------------------------------------------------------------------------------------
  // IThumbSizeableEx
  //

  @Override
  public EThumbSize thumbSize() {
    return canvas.thumbSize();
  }

  @Override
  public void setThumbSize( EThumbSize aThumbSize ) {
    canvas.setThumbSize( aThumbSize );
    adjustMinSize();
  }

  @Override
  public EThumbSize defaultThumbSize() {
    return canvas.defaultThumbSize();
  }

  @Override
  public IGenericChangeEventer thumbSizeEventer() {
    return canvas.thumbSizeEventer();
  }

  // ------------------------------------------------------------------------------------
  // IPicsGridViewer
  //

  @Override
  public IList<V> items() {
    return items;
  }

  @Override
  public void setItems( IList<V> aItems ) {
    if( aItems != null && !aItems.isEmpty() ) {
      items.setAll( aItems );
    }
    else {
      items.clear();
    }
    refresh();
  }

  @Override
  public IGridMargins getMargins() {
    return canvas.pgvGetMargins();
  }

  @Override
  public void setMargins( IGridMargins aMargins ) {
    canvas.pgvSetMargins( aMargins );
  }

  @Override
  public IPgvVisualsProvider<V> getVisualsProvider() {
    return canvas.pgvGetVisualsProvider();
  }

  @Override
  public void setVisualsProvider( IPgvVisualsProvider<V> aVisualsProvider ) {
    canvas.pgvSetVisualsProvider( aVisualsProvider );
  }

  @Override
  public void refresh() {
    canvas.pgvSetEntities( items );
  }

  @Override
  public TsComposite getControl() {
    return this;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputProducer
  //

  @Override
  public void addTsUserInputListener( ITsUserInputListener aListener ) {
    canvas.userInputEventsBinder.addTsUserInputListener( aListener );
  }

  @Override
  public void removeTsUserInputListener( ITsUserInputListener aListener ) {
    canvas.userInputEventsBinder.removeTsUserInputListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputProducer
  //

  @Override
  public void addTsMouseInputListener( ITsMouseInputListener aListener ) {
    canvas.userInputEventsBinder.addTsMouseInputListener( aListener );
  }

  @Override
  public void removeTsMouseInputListener( ITsMouseInputListener aListener ) {
    canvas.userInputEventsBinder.removeTsMouseInputListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputProducer
  //

  @Override
  public void addTsKeyInputListener( ITsKeyInputListener aListener ) {
    canvas.userInputEventsBinder.addTsKeyInputListener( aListener );
  }

  @Override
  public void removeTsKeyInputListener( ITsKeyInputListener aListener ) {
    canvas.userInputEventsBinder.removeTsKeyInputListener( aListener );
  }

}
