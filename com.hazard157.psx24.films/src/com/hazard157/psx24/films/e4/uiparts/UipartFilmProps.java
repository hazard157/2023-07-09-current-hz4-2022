package com.hazard157.psx24.films.e4.uiparts;

import java.io.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.currentity.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

import com.hazard157.psx24.films.e4.services.*;
import com.hazard157.psx24.films.glib.*;

/**
 * Вью своств текущего фильма.
 *
 * @author hazard157
 */
public class UipartFilmProps
    extends MwsAbstractPart {

  private final ICurrentEntityChangeListener<File> currentFilmChangeListener = this::updateOnFilmFile;

  private final IGenericChangeListener propsChangeListener = aSource -> whenPropsChanged();

  @Inject
  ICurrentFilmService currentFilmService;

  @Inject
  IPsxFilmsService filmsService;

  FilmFileConvoyPanel panel;

  File filmFile = null;

  @Override
  protected void doInit( Composite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = new FilmFileConvoyPanel( aParent, null, ctx,
        ITsDialogConstants.DF_V_SCROLLER | ITsDialogConstants.DF_NEED_VALIDATION );
    currentFilmService.addCurrentEntityChangeListener( currentFilmChangeListener );
    panel.genericChangeEventer().addListener( propsChangeListener );
  }

  void whenPropsChanged() {
    if( filmFile == null ) {
      return;
    }
    IOptionSet props = panel.getDataRecord();
    filmsService.cfm().writeFileConvoy( filmFile, props, OptionSetKeeper.KEEPER_INDENTED );
  }

  void updateOnFilmFile( File aFilmFile ) {
    filmFile = aFilmFile;
    if( filmFile == null || !filmFile.exists() ) {
      panel.setDataRecord( IOptionSet.NULL );
      panel.setEnabled( false );
      return;
    }
    IOptionSet inf = filmsService.cfm().readFileConvoy( filmFile, OptionSetKeeper.KEEPER, IOptionSet.NULL );
    panel.setDataRecord( inf );
  }

}
