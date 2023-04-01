package com.hazard157.psx24.films.glib;

import static com.hazard157.psx24.films.glib.IFilmPropsConstants.*;
import static com.hazard157.psx24.films.glib.IPsxResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.rating.*;
import com.hazard157.lib.core.quants.valeds.radioprop.*;
import com.hazard157.psx24.films.glib.valeds.*;

/**
 * Панель для редактирования {@link IOptionSet} со свойствами файла порнилла.
 * <p>
 * Свойства файла порнилаа перечислены в {@link IFilmPropsConstants}.
 *
 * @author hazard157
 */
public class FilmFileConvoyPanel
    extends AbstractTsDialogPanel<IOptionSet, ITsGuiContext> {

  private final IValedControlValueChangeListener valedControlValueChangeListener = ( aSource, aEditFinished ) -> {
    if( aEditFinished ) {
      fireContentChangeEvent();
    }
  };

  ValedRadioPropEnumStars<ERating> vrssRating;
  ValedAvStringText                valedNotes;
  ValedAvPsxKeywords               valedKeywords;

  /**
   * Конструктор панели, предназаначенной для использования вне диалога.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aData T - начальные данные для отображения, может быть null
   * @param aContext C - контекст радктирования / показа структры данных T
   * @param aFlags int - флаги настройки панели, собранные по ИЛИ биты {@link TsDialog}.DF_XXX
   * @throws TsNullArgumentRtException aParent или aContext = null
   */
  public FilmFileConvoyPanel( Composite aParent, IOptionSet aData, ITsGuiContext aContext, int aFlags ) {
    super( aParent, aContext, aData, aContext, aFlags );
    init();
  }

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aOwnerDialog {@link TsDialog} - родительский диалог
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public FilmFileConvoyPanel( Composite aParent, TsDialog<IOptionSet, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  private void init() {
    this.setLayout( new GridLayout( 2, false ) );
    ITsGuiContext ctx;
    // vrssRating
    Label l = new Label( this, SWT.RIGHT );
    l.setText( STR_L_RATING );
    ctx = new TsGuiContext( tsContext() );
    IValedEnumConstants.REFDEF_ENUM_CLASS.setRef( ctx, ERating.class );
    vrssRating = new ValedRadioPropEnumStars<>( ctx );
    vrssRating.createControl( this );
    vrssRating.getControl().setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 1, 1 ) );
    vrssRating.eventer().addListener( valedControlValueChangeListener );
    // valedNotes
    l = new Label( this, SWT.RIGHT );
    l.setText( STR_L_NOTES );
    ctx = new TsGuiContext( tsContext() );
    valedNotes = new ValedAvStringText( ctx );
    valedNotes.createControl( this );
    valedNotes.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    valedNotes.eventer().addListener( valedControlValueChangeListener );
    // valedKeywords
    l = new Label( this, SWT.RIGHT );
    l.setText( STR_L_KEYWORDS );
    ctx = new TsGuiContext( tsContext() );
    valedKeywords = new ValedAvPsxKeywords( ctx );
    valedKeywords.createControl( this );
    valedKeywords.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
    valedKeywords.eventer().addListener( valedControlValueChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected void doSetDataRecord( IOptionSet aData ) {
    genericChangeEventer().pauseFiring();
    try {
      if( aData == null ) {
        vrssRating.clearValue();
        valedNotes.clearValue();
        valedKeywords.clearValue();
        return;
      }
      vrssRating.setValue( FP_RATING.getValue( aData ).asValobj() );
      valedNotes.setValue( FP_NOTES.getValue( aData ) );
      valedKeywords.setValue( FP_KEYWORDS.getValue( aData ) );
    }
    finally {
      genericChangeEventer().resumeFiring( false );
    }
  }

  @Override
  protected IOptionSet doGetDataRecord() {
    IOptionSetEdit p = new OptionSet();
    FP_RATING.setValue( p, avValobj( vrssRating.getValue() ) );
    FP_NOTES.setValue( p, valedNotes.getValue() );
    FP_KEYWORDS.setValue( p, valedKeywords.getValue() );
    return p;
  }

}
