package com.hazard157.psx24.core.utils.ftstep;

import static com.hazard157.psx24.core.IPsx24CoreConstants.*;
import static com.hazard157.psx24.core.IPsxAppActions.*;
import static com.hazard157.psx24.core.utils.ftstep.IHzResources.*;

import java.util.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.common.utils.ftstep.*;

/**
 * Создатель выпадающего меню к действию управления {@link IFrameTimeSteppable} сущностью.
 * <p>
 * Испольльзование:
 * <ul>
 * <li>добавить в тулбар действие {@link #AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU} или
 * {@link #AI_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL_MENU};</li>
 * <li>создать экземпляр этогокласса
 * <code>IMenuCreator menuCreator = new FrameTimeSteppableExZoomDropDownMenuCreator(...)</code>;</li>
 * <li>важно понимать, что использовать команды ZOOM_FIT можно, если управляемая сущность имеет понятие "вместить в
 * видимую оюлесть". В таком случае можно использовать пункт и выпадающее меню
 * {@link #AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU} и нужно задать в <code>true</code> использование FIT меню понкта
 * методом {@link #setFitMenuItem(boolean)};</li>
 * <li>задать меню к действию методом {@link TsToolbar#setActionMenu(String, IMenuCreator) toolbar.setActionMenu(
 * AID_FRAME_TIME_STEPPABLE_ZOOM_FIT/OIGINAL, menuCreator )};</li>
 * <li>в обработчике тулбара {@link ITsToolbarListener#onToolButtonPressed(String)} обработать действие
 * {#AID_FRAME_TIME_STEPPABLE_ZOOM_FIT} или {{@link #AID_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL} в зависимости от
 * используемого в тулюаре дествия. Для ZOOM_ORIGINAL надо задать методом
 * {@link IFrameTimeSteppable#setFrameTimeStep(ESecondsStep)}, где новый шаг нужно получить метдом
 * {@link IFrameTimeSteppable#defaultFrameTimeStep()};</li>
 * <li>при использовании действия ZOOM_FIT надо <b>обязательно переопределить</b> метод
 * {@link #doSetFitSize(IFrameTimeSteppable)}.</li>
 * </ul>
 * <p>
 * Внимание: интервал управляемой сущности {@link IFrameTimeSteppable#defaultFrameTimeStep()} должен находится в списке
 * {@link #getAvailableFrameTimeSteps()}. Иначе поведение при масштабировании "больше"/"меньше" окажется странным.
 * Например, если интервал по умолчанию меньше минимально допустимого, то уменьшение интервала приведет к его увеличению
 * (до наименьшего из {@link #getAvailableFrameTimeSteps()}).
 *
 * @author goga
 */
public class FrameTimeSteppableDropDownMenuCreator
    extends AbstractMenuCreator {

  /**
   * ID of action "adjust FrameTimeStamp to fit entity in visible area".
   */
  public static final String AID_FRAME_TIME_STEPPABLE_ZOOM_FIT = PSX_ACT_ID + ".FtsZoomFit"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_FRAME_TIME_STEPPABLE_ZOOM_IN}.
   */
  public static final String AID_FRAME_TIME_STEPPABLE_ZOOM_IN = PSX_ACT_ID + ".FtsZoomIn"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_FRAME_TIME_STEPPABLE_ZOOM_OUT}.
   */
  public static final String AID_FRAME_TIME_STEPPABLE_ZOOM_OUT = PSX_ACT_ID + ".FtsZoomOut"; //$NON-NLS-1$

  /**
   * ID of action "reset FrameTimeStep to original value@.
   */
  public static final String AID_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL = PSX_ACT_ID + ".FtsZoomOrig"; //$NON-NLS-1$

  /**
   * Действие с выпадающим меню для управления интервалом значка {@link IFrameTimeSteppable}.
   * <p>
   * Кнопка вызывает действие {@link #AID_FRAME_TIME_STEPPABLE_ZOOM_FIT}.
   * <p>
   * <b>Внимание:</b> этому действию нельзя напрямую задавать {@link IMenuCreator}. Надо задать меню тулбару методом
   * {@link TsToolbar#setActionChecked(String, boolean)}.
   */
  public static final ITsActionDef AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU =
      TsActionDef.ofMenu2( AID_FRAME_TIME_STEPPABLE_ZOOM_FIT, //
          ACT_T_FRAME_TIME_STEPPABLE_ZOOM_FIT, ACT_P_FRAME_TIME_STEPPABLE_ZOOM_FIT, ICON_TIMELINE_ZOOM_FIT );

  /**
   * Действие увеличение интервала миниатюры.
   */
  public static final ITsActionDef AI_FRAME_TIME_STEPPABLE_ZOOM_IN =
      TsActionDef.ofPush2( AID_FRAME_TIME_STEPPABLE_ZOOM_IN, //
          ACT_T_FRAME_TIME_STEPPABLE_ZOOM_IN, ACT_P_FRAME_TIME_STEPPABLE_ZOOM_IN, ICON_TIMELINE_ZOOM_IN );

  /**
   * Действие уменьшения интервала миниатюры.
   */
  public static final ITsActionDef AI_FRAME_TIME_STEPPABLE_ZOOM_OUT =
      TsActionDef.ofPush2( AID_FRAME_TIME_STEPPABLE_ZOOM_OUT, //
          ACT_T_FRAME_TIME_STEPPABLE_ZOOM_OUT, ACT_P_FRAME_TIME_STEPPABLE_ZOOM_OUT, ICON_TIMELINE_ZOOM_OUT );

  /**
   * Действие уменьшения интервала миниатюры.
   */
  public static final ITsActionDef AI_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL_MENU = TsActionDef.ofMenu2(
      AID_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL, //
      ACT_T_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL, ACT_P_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL, ICON_TIMELINE_ZOOM_ORIGINAL );

  final IFrameTimeSteppable subject;
  final ITsIconManager      iconManager;

  final IListEdit<ESecondsStep> availableFrameTimeSteps = new ElemArrayList<>( ESecondsStep.values() );

  private boolean hasIndividualSizesMenuItems = true;
  private boolean hasFitMenuItem              = false;

  EIconSize menuIconSize = EIconSize.IS_16X16;

  /**
   * Конструктор.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность
   * @param aContext {@link ITsGuiContext} - контекст приложения уровня окна
   * @param aMenuIconSize {@link EIconSize} - интервал значков в выпадающем меню
   * @param aMinFrameTimeStep {@link ESecondsStep} - минимальныо допустимый интервал миниатюры
   * @param aMaxFrameTimeStep {@link ESecondsStep} - максимальныо допустимый интервал миниатюры
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FrameTimeSteppableDropDownMenuCreator( IFrameTimeSteppable aSubject, ITsGuiContext aContext,
      EIconSize aMenuIconSize, ESecondsStep aMinFrameTimeStep, ESecondsStep aMaxFrameTimeStep ) {
    TsNullArgumentRtException.checkNulls( aSubject, aContext, aMenuIconSize, aMinFrameTimeStep, aMaxFrameTimeStep );
    subject = aSubject;
    iconManager = aContext.get( ITsIconManager.class );
    menuIconSize = aMenuIconSize;
    setAvalaiableFrameTimeStepsRange( aMinFrameTimeStep, aMaxFrameTimeStep );
  }

  /**
   * Конструктор с полным набором шагов.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность
   * @param aContext {@link ITsGuiContext} - контекст приложения уровня окна
   * @param aMenuIconSize {@link EIconSize} - интервал значков в выпадающем меню
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FrameTimeSteppableDropDownMenuCreator( IFrameTimeSteppable aSubject, ITsGuiContext aContext,
      EIconSize aMenuIconSize ) {
    this( aSubject, aContext, aMenuIconSize, ESecondsStep.minStep(), ESecondsStep.maxStep() );
  }

  /**
   * Конструктор с полным набором шагов в выпадающем меню {@link EIconSize#IS_16X16}.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность
   * @param aContext {@link ITsGuiContext} - контекст приложения уровня окна
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FrameTimeSteppableDropDownMenuCreator( IFrameTimeSteppable aSubject, ITsGuiContext aContext ) {
    this( aSubject, aContext, EIconSize.IS_16X16, ESecondsStep.minStep(), ESecondsStep.maxStep() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класска
  //

  @SuppressWarnings( "unused" )
  @Override
  protected boolean fillMenu( Menu aMenu ) {
    MenuItem mItem;
    // zoom fit
    if( isFitMenuItem() ) {
      mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU.nmName() );
      mItem.setToolTipText( AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU.description() );
      mItem.setImage( iconManager.loadStdIcon( AI_FRAME_TIME_STEPPABLE_ZOOM_FIT_MENU.iconId(), menuIconSize ) );
      mItem.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent e ) {
          doSetFitSize( subject );
        }
      } );
    }
    // zoom original
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( String.format( FMT_N_ORIGINAL_SIZE, subject.defaultFrameTimeStep().nmName() ) );
    mItem.setToolTipText( String.format( FMT_D_ORIGINAL_SIZE, subject.defaultFrameTimeStep().nmName() ) );
    mItem.setImage( iconManager.loadStdIcon( AI_FRAME_TIME_STEPPABLE_ZOOM_ORIGINAL_MENU.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doSetOriginalSize( subject );
      }
    } );
    mItem.setEnabled( subject.getFrameTimeStep() != subject.defaultFrameTimeStep() );
    // separator
    new MenuItem( aMenu, SWT.SEPARATOR );
    // zoom out
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( AI_FRAME_TIME_STEPPABLE_ZOOM_OUT.nmName() );
    mItem.setToolTipText( AI_FRAME_TIME_STEPPABLE_ZOOM_OUT.description() );
    mItem.setImage( iconManager.loadStdIcon( AI_FRAME_TIME_STEPPABLE_ZOOM_OUT.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomOut( subject );
      }
    } );
    mItem.setEnabled( subject.getFrameTimeStep() != getAvailableFrameTimeSteps().first() );
    // zoom in
    mItem = new MenuItem( aMenu, SWT.PUSH );
    mItem.setText( AI_FRAME_TIME_STEPPABLE_ZOOM_IN.nmName() );
    mItem.setToolTipText( AI_FRAME_TIME_STEPPABLE_ZOOM_IN.description() );
    mItem.setImage( iconManager.loadStdIcon( AI_FRAME_TIME_STEPPABLE_ZOOM_IN.iconId(), menuIconSize ) );
    mItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        doZoomIn( subject );
      }
    } );
    mItem.setEnabled( subject.getFrameTimeStep() != getAvailableFrameTimeSteps().last() );
    if( hasIndividualSizesMenuItems ) {
      // separator
      new MenuItem( aMenu, SWT.SEPARATOR );
      // availableFrameTimeSteps
      for( ESecondsStep sz : availableFrameTimeSteps ) {
        mItem = new MenuItem( aMenu, SWT.CHECK );
        mItem.setText( sz.nmName() );
        mItem.setToolTipText( sz.description() );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            doSetFrameTimeStep( subject, sz );
          }
        } );
        mItem.setSelection( sz == subject.getFrameTimeStep() );
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает интервал значков в созданном меню.
   *
   * @return {@link EIconSize} - интервал значков в меню
   */
  public EIconSize getMenuIconSize() {
    return menuIconSize;
  }

  /**
   * Задает интервал значков в созданном меню.
   *
   * @param aMenuIconSize {@link EIconSize} - интервал значков в меню
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setMenuIconSize( EIconSize aMenuIconSize ) {
    menuIconSize = TsNullArgumentRtException.checkNull( aMenuIconSize );
  }

  /**
   * Определяет, будет ли в меню пункт индивидуальные пункты задания интервала для каждого из
   * {@link #getAvailableFrameTimeSteps()}.
   *
   * @return boolean - признак наличия индивидуальных пунктов меню допустимых интервалов
   */
  public boolean isFrameTimeStepsMenuItems() {
    return hasIndividualSizesMenuItems;
  }

  /**
   * Задает, будет ли в меню пункт индивидуальные пункты задания интервала для каждого из
   * {@link #getAvailableFrameTimeSteps()}.
   *
   * @param aValue boolean - признак наличия индивидуальных пунктов меню допустимых интервалов
   */
  public void setFrameTimeStepsMenuItems( boolean aValue ) {
    hasIndividualSizesMenuItems = aValue;
  }

  /**
   * Определяет, будет ли в меню пункт действия {@link #AID_FRAME_TIME_STEPPABLE_ZOOM_FIT}.
   *
   * @return boolean - признак наличия пункта меню "вместить в видимую область"
   */
  public boolean isFitMenuItem() {
    return hasFitMenuItem;
  }

  /**
   * Задает, будет ли в меню пункт действия {@link #AID_FRAME_TIME_STEPPABLE_ZOOM_FIT}.
   *
   * @param aValue boolean - признак наличия пункта меню "вместить в видимую область"
   */
  public void setFitMenuItem( boolean aValue ) {
    hasFitMenuItem = aValue;
  }

  /**
   * Возвращает раземры миниатюр, которые будут использоваться для изменения интервалов.
   * <p>
   * Возвращаемый список всегда содержит хотя бы один элемент и всегда отсортирован по учеличениу интервалов.
   *
   * @return {@link IList}&lt;{@link ESecondsStep}&gt; - список допустимых интервалов
   */
  public IList<ESecondsStep> getAvailableFrameTimeSteps() {
    return availableFrameTimeSteps;
  }

  /**
   * Задает интервалы миниатюр, которые будут использоваться для изменения интервалов.
   * <p>
   * Можно указывать люые интервалы, диапазон будет свормирован автоматически, даже если aMinSize < aMaxSize. Можно
   * указывать однаковый минимум и максимум - но это не имеет смысла.
   * <p>
   * Внимание: среди задаваемых интервалов должен быть интервал по умолчанию
   * {@link IFrameTimeSteppable#defaultFrameTimeStep()}.
   *
   * @param aMinSize {@link ESecondsStep} - нижняя граница диапазона
   * @param aMaxSize {@link ESecondsStep} - верхняя граница диапазона
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setAvalaiableFrameTimeStepsRange( ESecondsStep aMinSize, ESecondsStep aMaxSize ) {
    TsNullArgumentRtException.checkNulls( aMinSize, aMaxSize );
    availableFrameTimeSteps.clear();
    ESecondsStep minStep = aMinSize;
    ESecondsStep maxStep = aMaxSize;
    if( minStep.stepSecs() > maxStep.stepSecs() ) {
      minStep = aMaxSize;
      maxStep = aMinSize;
    }
    ESecondsStep fts = minStep;
    while( true ) {
      availableFrameTimeSteps.add( fts );
      if( fts == maxStep ) {
        break;
      }
      fts = fts.zoomOut();
    }
  }

  /**
   * Задает интервалы миниатюр, которые будут использоваться для изменения интервалов.
   * <p>
   * Надо задать хотя бы один интервал. Размеры можно указывать в произвольном порядке, они буду отсортированы.
   * <p>
   * Внимание: среди задаваемых интервалов должен быть интервал по умолчанию
   * {@link IFrameTimeSteppable#defaultFrameTimeStep()}.
   *
   * @param aSizes {@link ESecondsStep}[] - массив интервалов
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент пусто массив
   */
  public void setAvalaiableFrameTimeSteps( ESecondsStep... aSizes ) {
    TsErrorUtils.checkArrayArg( aSizes, 1 );
    Arrays.sort( aSizes );
    availableFrameTimeSteps.setAll( aSizes );
  }

  /**
   * Возвращает следующий (больший) допустимый интервал миниатюры.
   * <p>
   * Смысл метода в том, что он возвращает интервалы только из допустимых {@link #getAvailableFrameTimeSteps()}. Если
   * аргумент и так наибольший допустимый, то возвращает аргумент.
   * <p>
   * Если аргумент - больше наибольшего допустимого, то возвращает наибольший допустимый интервал, который <b>меньше</b>
   * аргумента!
   *
   * @param aFrameTimeStep {@link ESecondsStep} - исходный интервал
   * @return {@link ESecondsStep} - больший допустимый интервал, может быть <b>меньше</b> аргумента!
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ESecondsStep getNextFrameTimeStep( ESecondsStep aFrameTimeStep ) {
    TsNullArgumentRtException.checkNull( aFrameTimeStep );
    if( availableFrameTimeSteps.size() == 1 ) {
      return availableFrameTimeSteps.first();
    }
    if( aFrameTimeStep.stepSecs() < availableFrameTimeSteps.first().stepSecs() ) {
      return availableFrameTimeSteps.first();
    }
    if( aFrameTimeStep.stepSecs() >= availableFrameTimeSteps.last().stepSecs() ) {
      return availableFrameTimeSteps.last();
    }
    for( ESecondsStep sz = aFrameTimeStep; sz.stepSecs() < availableFrameTimeSteps.last().stepSecs(); sz =
        sz.zoomOut() ) {
      if( availableFrameTimeSteps.hasElem( sz.zoomOut() ) ) {
        return sz.zoomOut();
      }
    }
    throw new TsInternalErrorRtException();
  }

  /**
   * Возвращает предыдущий (меньший) допустимый интервал миниатюры.
   * <p>
   * Смысл метода в том, что он возвращает интервалы только из допустимых {@link #getAvailableFrameTimeSteps()}. Если
   * аргумент и так наименьший допустимый, то возвращает аргумент.
   * <p>
   * Если аргумент - меньше наименьшего допустимого, то возвращает наименьший допустимый интервал, который <b>больше</b>
   * аргумента!
   *
   * @param aFrameTimeStep {@link ESecondsStep} - исходный интервал
   * @return {@link ESecondsStep} - меньший допустимый интервал, может быть <b>больше</b> аргумента!
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ESecondsStep getPrevFrameTimeStep( ESecondsStep aFrameTimeStep ) {
    TsNullArgumentRtException.checkNull( aFrameTimeStep );
    if( availableFrameTimeSteps.size() == 1 ) {
      return availableFrameTimeSteps.first();
    }
    if( aFrameTimeStep.stepSecs() <= availableFrameTimeSteps.first().stepSecs() ) {
      return availableFrameTimeSteps.first();
    }
    if( aFrameTimeStep.stepSecs() > availableFrameTimeSteps.last().stepSecs() ) {
      return availableFrameTimeSteps.last();
    }
    for( ESecondsStep sz = aFrameTimeStep; sz.stepSecs() > availableFrameTimeSteps.first().stepSecs(); sz =
        sz.zoomIn() ) {
      if( availableFrameTimeSteps.hasElem( sz.zoomIn() ) ) {
        return sz.zoomIn();
      }
    }
    throw new TsInternalErrorRtException();
  }

  // ------------------------------------------------------------------------------------
  // May be overriden
  //

  /**
   * Насчледник должен осуществить сброс шага так, чтобы сущность полностью вместилась в отображаемоую область.
   * <p>
   * В базовом классе выбрасывает исключение {@link TsUnderDevelopmentRtException}, при переопределении нельзя вызывать
   * метод базового класса.
   * <p>
   * Внимание: для фактического изменения интервала следует вызывать метод
   * {@link #doSetFrameTimeStep(IFrameTimeSteppable, ESecondsStep)}.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность, не бывает <code>null</code>
   * @throws TsUnderDevelopmentRtException - в базовом классе выбрасывается всегда
   */
  public void doSetFitSize( IFrameTimeSteppable aSubject ) {
    throw new TsUnderDevelopmentRtException( FMT_ERR_NO_DO_SET_FIT_SIZE, "doSetFitSize", //$NON-NLS-1$
        FrameTimeSteppableDropDownMenuCreator.class.getSimpleName() );
  }

  /**
   * Осуществляет сброс интервала в {@link IFrameTimeSteppable#defaultFrameTimeStep()}.
   * <p>
   * Метод не вызывается, если текущий интервал {@link IFrameTimeSteppable#getFrameTimeStep()} равен
   * {@link IFrameTimeSteppable#defaultFrameTimeStep()}.
   * <p>
   * В базовом классе устанавливает начальный интервал {@link IFrameTimeSteppable#defaultFrameTimeStep()} управляемой
   * сущности. Вызывать ли родительский метод при переопределении - зависит от логики использования.
   * <p>
   * Внимание: для фактического изменения интервала вызывает метод
   * {@link #doSetFrameTimeStep(IFrameTimeSteppable, ESecondsStep)}.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность, не бывает <code>null</code>
   */
  public void doSetOriginalSize( IFrameTimeSteppable aSubject ) {
    doSetFrameTimeStep( aSubject, aSubject.defaultFrameTimeStep() );
  }

  /**
   * Осуществляет уменьшение масштаба.
   * <p>
   * Метод не вызывается, если текущий интервал {@link IFrameTimeSteppable#getFrameTimeStep()} равен
   * {@link ESecondsStep#minStep()}.
   * <p>
   * В базовом классе устанавливает предыдущий интервал {@link #getPrevFrameTimeStep(ESecondsStep)}. Вызывать ли
   * родительский метод при переопределении - зависит от логики использования.
   * <p>
   * Внимание: для фактического изменения интервала вызывает метод
   * {@link #doSetFrameTimeStep(IFrameTimeSteppable, ESecondsStep)}.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность, не бывает <code>null</code>
   */
  public void doZoomOut( IFrameTimeSteppable aSubject ) {
    doSetFrameTimeStep( aSubject, getPrevFrameTimeStep( subject.getFrameTimeStep() ) );
  }

  /**
   * Осуществляет увеличение масштаба.
   * <p>
   * Метод не вызывается, если текущий интервал {@link IFrameTimeSteppable#getFrameTimeStep()} равен
   * {@link ESecondsStep#maxStep()}.
   * <p>
   * В базовом классе устанавливает следующий интервал {@link #getNextFrameTimeStep(ESecondsStep)}. Вызывать ли
   * родительский метод при переопределении - зависит от логики использования.
   * <p>
   * Внимание: для фактического изменения интервала вызывает метод
   * {@link #doSetFrameTimeStep(IFrameTimeSteppable, ESecondsStep)}.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность, не бывает <code>null</code>
   */
  public void doZoomIn( IFrameTimeSteppable aSubject ) {
    doSetFrameTimeStep( aSubject, getNextFrameTimeStep( subject.getFrameTimeStep() ) );
  }

  /**
   * Изменяет интервал на заданный.
   * <p>
   * В базовом классе просто устанавливает заданный интервал aFrameTimeStep. Вызывать ли родительский метод при
   * переопределении - зависит от логики использования.
   * <p>
   * Внимание: этот метод неявно вызывается всеми остальными <code>doXxx()</code> методами изменения интервалов.
   *
   * @param aSubject {@link IFrameTimeSteppable} - управляемая сущность, не бывает <code>null</code>
   * @param aSize {@link ESecondsStep} - новый шаг, не бывает <code>null</code>
   */
  public void doSetFrameTimeStep( IFrameTimeSteppable aSubject, ESecondsStep aSize ) {
    aSubject.setFrameTimeStep( aSize );
  }

}
