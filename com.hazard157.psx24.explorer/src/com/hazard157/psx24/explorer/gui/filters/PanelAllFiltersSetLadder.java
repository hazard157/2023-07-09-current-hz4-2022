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
 * @author goga
 */
public class PanelAllFiltersSetLadder
    extends TsPanel
    implements IGenericChangeEventCapable {

  class FilterHeader
      extends TsComposite {

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
      buttonPane.setLayout( rowLayout );
      buttonPane.setLayoutData( BorderLayout.EAST );
      // clear button
      clearButton = new Button( buttonPane, SWT.PUSH | SWT.FLAT );
      clearButton.setText( STR_N_CLEAR_FILTER );
      clearButton.setToolTipText( STR_D_CLEAR_FILTER );
    }

    public void addClearButtonSelectionListener( SelectionListener aListener ) {
      clearButton.addSelectionListener( aListener );
    }

  }

  private final IGenericChangeListener anyFilterChamgeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      eventer.fireChangeEvent();
    }
  };

  final GenericChangeEventer eventer;

  final PqFilterEpisodeIdsPanel pqEpIds;
  final PqFilterAnyTextPanel    pqAnyText;
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
  public PanelAllFiltersSetLadder( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    eventer = new GenericChangeEventer( this );
    GridLayout gridLayout = new GridLayout( 1, false );
    this.setLayout( gridLayout );
    ITsGuiContext ctx;
    // epIds
    FilterHeader h = new FilterHeader( this, STR_N_FILTER_EP_IDS );
    h.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    h.addClearButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqEpIds.resetFilter();
        eventer.fireChangeEvent();
      }
    } );
    ctx = new TsGuiContext( tsContext() );
    pqEpIds = new PqFilterEpisodeIdsPanel( this, null, ctx );
    pqEpIds.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1 ) );
    pqEpIds.genericChangeEventer().addListener( anyFilterChamgeListener );
    // anyText
    h = new FilterHeader( this, STR_N_FILTER_ANY_TEXT );
    h.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    h.addClearButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqAnyText.resetFilter();
        eventer.fireChangeEvent();
      }
    } );
    ctx = new TsGuiContext( tsContext() );
    pqAnyText = new PqFilterAnyTextPanel( this, null, ctx );
    pqAnyText.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 1, 1 ) );
    pqAnyText.genericChangeEventer().addListener( anyFilterChamgeListener );
    // tagIds
    h = new FilterHeader( this, STR_N_FILTER_TAG_IDS );
    h.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false ) );
    h.addClearButtonSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        pqTagIds.resetFilter();
        eventer.fireChangeEvent();
      }
    } );
    ctx = new TsGuiContext( tsContext() );
    pqTagIds = new PqFilterTagIdsPanel( this, null, ctx );
    pqTagIds.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
    pqTagIds.genericChangeEventer().addListener( anyFilterChamgeListener );
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
    }
    ITsSingleFilterParams spAnyText = pqAnyText.getFilterParams();
    if( spAnyText != ITsSingleFilterParams.NONE ) {
      item.fpMap().put( EPqSingleFilterKind.ANY_TEXT, spAnyText );
    }
    ITsSingleFilterParams spTagIds = pqTagIds.getFilterParams();
    if( spTagIds != ITsSingleFilterParams.NONE ) {
      item.fpMap().put( EPqSingleFilterKind.TAG_IDS, spTagIds );
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
    // TODO сбросить также NOT виджеты
    // pqEpIds
    pqEpIds.resetFilter();
    if( aItem.fpMap().hasKey( EPqSingleFilterKind.EPISODE_IDS ) ) {
      pqEpIds.setFilterParams( aItem.fpMap().getByKey( EPqSingleFilterKind.EPISODE_IDS ) );
    }
    // pqAnyText
    pqAnyText.resetFilter();
    if( aItem.fpMap().hasKey( EPqSingleFilterKind.ANY_TEXT ) ) {
      pqAnyText.setFilterParams( aItem.fpMap().getByKey( EPqSingleFilterKind.ANY_TEXT ) );
    }
    // pqTagIds
    pqTagIds.resetFilter();
    if( aItem.fpMap().hasKey( EPqSingleFilterKind.TAG_IDS ) ) {
      pqTagIds.setFilterParams( aItem.fpMap().getByKey( EPqSingleFilterKind.TAG_IDS ) );
    }
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
