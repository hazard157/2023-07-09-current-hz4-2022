package com.hazard157.psx24.explorer.gui.filters;

import static com.hazard157.psx24.explorer.gui.filters.IPsxResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx24.explorer.filters.*;
import com.hazard157.psx24.explorer.unit.*;

/**
 * Панель фильтров по одному всех типов, которые генерируют один {@link ITsCombiFilterParams}.
 * <p>
 * Панель собрает фильтры по AND, имеет возможность любому ставить NOT, и главное, как создает параметры, так и обратно
 * восстанавливает виджеты из этого {@link ITsCombiFilterParams}.
 * <p>
 * При изменении параметров генерирует сообщение {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
 *
 * @author hazard157
 */
public class PanelAllFiltersSet
    extends TsPanel
    implements IGenericChangeEventCapable {

  class FilterHeader
      extends TsComposite {

    final Button invertButton;
    final Button clearButton;

    FilterHeader( Composite aParent, String aName ) {
      super( aParent );
      this.setLayout( new BorderLayout() );
      // filter name label font
      FontData fd = this.getFont().getFontData()[0];
      fd.setStyle( SWT.BOLD );
      IFontInfo fontInfo = fontManager().data2info( fd );
      Font labelFont = fontManager().getFont( fontInfo );
      // label
      Label l = new Label( this, SWT.LEFT );
      l.setFont( labelFont );
      l.setText( aName );
      l.setLayoutData( BorderLayout.CENTER );
      // buttons pane
      TsComposite buttonPane = new TsComposite( this );
      RowLayout rowLayout = new RowLayout( SWT.HORIZONTAL );
      rowLayout.marginBottom = rowLayout.marginLeft = rowLayout.marginRight = rowLayout.marginTop = 0;
      rowLayout.fill = true;
      buttonPane.setLayout( rowLayout );
      buttonPane.setLayoutData( BorderLayout.EAST );
      // invert button
      invertButton = new Button( buttonPane, SWT.CHECK );
      invertButton.setText( STR_N_INVERT_FILTER );
      invertButton.setToolTipText( STR_D_INVERT_FILTER );
      // clear button
      clearButton = new Button( buttonPane, SWT.PUSH | SWT.FLAT );
      clearButton.setText( STR_N_CLEAR_FILTER );
      clearButton.setToolTipText( STR_D_CLEAR_FILTER );
    }

    public void addClearButtonSelectionListener( SelectionListener aListener ) {
      clearButton.addSelectionListener( aListener );
    }

    public void addInvertButtonSelectionListener( SelectionListener aListener ) {
      invertButton.addSelectionListener( aListener );
    }

  }

  private final IGenericChangeListener anyFilterChamgeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      eventer.fireChangeEvent();
    }
  };

  final GenericChangeEventer eventer;

  final FilterHeader            pqEpIdsHeader;
  final PqFilterEpisodeIdsPanel pqEpIds;
  final FilterHeader            pqAnyTextHeader;
  final PqFilterAnyTextPanel    pqAnyText;
  final FilterHeader            pqTagIdsHeader;
  final PqFilterTagIdsPanel     pqTagIds;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelAllFiltersSet( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    eventer = new GenericChangeEventer( this );
    this.setLayout( new BorderLayout() );
    ITsGuiContext ctx;

    // tabFolder
    TabFolder tabFolder = new TabFolder( this, SWT.TOP );
    tabFolder.setLayoutData( BorderLayout.CENTER );

    // tagIds
    TabItem tiTagIds = new TabItem( tabFolder, SWT.NONE );
    tiTagIds.setText( STR_N_TAB_FILTER_TAG_IDS );
    Composite boardTagIds = new Composite( tabFolder, SWT.NONE );
    tiTagIds.setControl( boardTagIds );
    boardTagIds.setLayout( new BorderLayout() );
    pqTagIdsHeader = new FilterHeader( boardTagIds, STR_N_FILTER_TAG_IDS );
    pqTagIdsHeader.setLayoutData( BorderLayout.NORTH );
    pqTagIdsHeader.addClearButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqTagIds.resetFilter();
        eventer.fireChangeEvent();
      }
    } );
    pqTagIdsHeader.addInvertButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqTagIds.setInverted( ((Button)aEvent.getSource()).getSelection() );
        eventer.fireChangeEvent();
      }
    } );
    ctx = new TsGuiContext( tsContext() );
    pqTagIds = new PqFilterTagIdsPanel( boardTagIds, null, ctx );
    pqTagIds.setLayoutData( BorderLayout.CENTER );
    pqTagIds.genericChangeEventer().addListener( anyFilterChamgeListener );

    // anyText
    TabItem tiAnyText = new TabItem( tabFolder, SWT.NONE );
    tiAnyText.setText( STR_N_TAB_FILTER_ANY_TEXT );
    Composite boardAnyText = new Composite( tabFolder, SWT.NONE );
    tiAnyText.setControl( boardAnyText );
    boardAnyText.setLayout( new BorderLayout() );
    pqAnyTextHeader = new FilterHeader( boardAnyText, STR_N_FILTER_ANY_TEXT );
    pqAnyTextHeader.setLayoutData( BorderLayout.NORTH );
    pqAnyTextHeader.addClearButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqAnyText.resetFilter();
        eventer.fireChangeEvent();
      }
    } );
    pqAnyTextHeader.addInvertButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqAnyText.setInverted( ((Button)aEvent.getSource()).getSelection() );
        eventer.fireChangeEvent();
      }
    } );
    ctx = new TsGuiContext( tsContext() );
    pqAnyText = new PqFilterAnyTextPanel( boardAnyText, null, ctx );
    pqAnyText.setLayoutData( BorderLayout.CENTER );
    pqAnyText.genericChangeEventer().addListener( anyFilterChamgeListener );

    // epIds
    TabItem tiEpIds = new TabItem( tabFolder, SWT.NONE );
    tiEpIds.setText( STR_N_TAB_FILTER_EPISODE_IDS );
    Composite boardEpIds = new Composite( tabFolder, SWT.NONE );
    tiEpIds.setControl( boardEpIds );
    boardEpIds.setLayout( new BorderLayout() );
    pqEpIdsHeader = new FilterHeader( boardEpIds, STR_N_FILTER_EP_IDS );
    pqEpIdsHeader.setLayoutData( BorderLayout.NORTH );
    pqEpIdsHeader.addClearButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqEpIds.resetFilter();
        eventer.fireChangeEvent();
      }
    } );
    pqEpIdsHeader.addInvertButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqEpIds.setInverted( ((Button)aEvent.getSource()).getSelection() );
        eventer.fireChangeEvent();
      }
    } );
    ctx = new TsGuiContext( tsContext() );
    Composite b2 = new Composite( boardEpIds, SWT.NONE );
    b2.setLayout( new BorderLayout() );
    b2.setLayoutData( BorderLayout.CENTER );
    pqEpIds = new PqFilterEpisodeIdsPanel( b2, null, ctx );
    pqEpIds.setLayoutData( BorderLayout.NORTH );
    pqEpIds.genericChangeEventer().addListener( anyFilterChamgeListener );

  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducerEx
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает параметры фильтра, заданные в панели.
   *
   * @return {@link InquiryItem} - заданноые в панели параметры фильтра
   */
  public InquiryItem getInquiryItem() {
    InquiryItem item = new InquiryItem();
    ITsSingleFilterParams spEpIds = pqEpIds.getFilterParams();
    if( spEpIds != ITsSingleFilterParams.NONE ) {
      item.fpMap().put( EPqSingleFilterKind.EPISODE_IDS, spEpIds );
      item.setInverted( EPqSingleFilterKind.EPISODE_IDS, pqEpIds.isInverted() );
    }
    ITsSingleFilterParams spAnyText = pqAnyText.getFilterParams();
    if( spAnyText != ITsSingleFilterParams.NONE ) {
      item.fpMap().put( EPqSingleFilterKind.ANY_TEXT, spAnyText );
      item.setInverted( EPqSingleFilterKind.ANY_TEXT, pqAnyText.isInverted() );
    }
    ITsSingleFilterParams spTagIds = pqTagIds.getFilterParams();
    if( spTagIds != ITsSingleFilterParams.NONE ) {
      item.fpMap().put( EPqSingleFilterKind.TAG_IDS, spTagIds );
      item.setInverted( EPqSingleFilterKind.TAG_IDS, pqTagIds.isInverted() );
    }
    return item;
  }

  /**
   * Показывает параметры в виджетах панели.
   *
   * @param aItem {@link InquiryItem} - параметры фильтра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public void setFilterParams( InquiryItem aItem ) {
    TsNullArgumentRtException.checkNull( aItem );
    eventer.pauseFiring();
    // TODO сбросить также NOT виджеты
    // pqEpIds
    pqEpIds.resetFilter();
    if( aItem.fpMap().hasKey( EPqSingleFilterKind.EPISODE_IDS ) ) {
      pqEpIds.setFilterParams( aItem.fpMap().getByKey( EPqSingleFilterKind.EPISODE_IDS ) );
      pqEpIds.setInverted( aItem.isInverted( EPqSingleFilterKind.EPISODE_IDS ) );
      pqEpIdsHeader.invertButton.setSelection( pqEpIds.isInverted() );
    }
    // pqAnyText
    pqAnyText.resetFilter();
    if( aItem.fpMap().hasKey( EPqSingleFilterKind.ANY_TEXT ) ) {
      pqAnyText.setFilterParams( aItem.fpMap().getByKey( EPqSingleFilterKind.ANY_TEXT ) );
      pqAnyText.setInverted( aItem.isInverted( EPqSingleFilterKind.ANY_TEXT ) );
      pqAnyTextHeader.invertButton.setSelection( pqAnyText.isInverted() );
    }
    // pqTagIds
    pqTagIds.resetFilter();
    if( aItem.fpMap().hasKey( EPqSingleFilterKind.TAG_IDS ) ) {
      pqTagIds.setFilterParams( aItem.fpMap().getByKey( EPqSingleFilterKind.TAG_IDS ) );
      pqTagIds.setInverted( aItem.isInverted( EPqSingleFilterKind.TAG_IDS ) );
      pqTagIdsHeader.invertButton.setSelection( pqTagIds.isInverted() );
    }
    eventer.resumeFiring( true );
  }

  /**
   * Сбрасывает все панеи в {@link ITsSingleFilterParams#NONE}.
   */
  public void resetFilterParans() {
    pqEpIds.resetFilter();
    pqAnyText.resetFilter();
    pqTagIds.resetFilter();
  }
}
