package com.hazard157.psx24.films.e4.uiparts;

import static com.hazard157.psx24.films.IPsx24FilmsConstants.*;

import java.io.*;

import javax.inject.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.widgets.pdw.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;

import com.hazard157.psx24.films.e4.services.*;

/**
 * Вью миниатюры текущего фильма.
 *
 * @author hazard157
 */
public class UipartFilmThumb
    extends MwsAbstractPart {

  private final ITsCollectionChangeListener prefsChangeListener = ( aSource, aOp, aItem ) -> {
    String opid = (String)aItem;
    if( aItem == null || opid.endsWith( OP_THUMB_SIZE.id() ) ) {
      EThumbSize thsz = OP_THUMB_SIZE.getValue( this.filmsService.modulePrefs().prefs() ).asValobj();
      this.imageWidget.setAreaPreferredSize( thsz.pointSize() );
      refreshImage();
    }
  };

  @Inject
  ICurrentFilmService currentFilmService;

  @Inject
  IPsxFilmsService filmsService;

  IPdwWidget imageWidget;

  @Override
  protected void doInit( Composite aParent ) {
    filmsService.modulePrefs().prefs().addCollectionChangeListener( prefsChangeListener );
    imageWidget = new PdwWidgetSimple( tsContext() );
    EThumbSize thSize = OP_THUMB_SIZE.getValue( filmsService.modulePrefs().prefs() ).asValobj();
    imageWidget.setAreaPreferredSize( thSize.pointSize() );
    imageWidget.setFulcrum( ETsFulcrum.CENTER );
    imageWidget.setFitInfo( RectFitInfo.BEST );
    imageWidget.createControl( aParent );
    currentFilmService.addCurrentEntityChangeListener( aCurrent -> refreshImage() );
  }

  void refreshImage() {
    File currFilmFile = currentFilmService.current();
    ITsPoint size = imageWidget.getAreaPreferredSize();
    EThumbSize thSize = EThumbSize.findIncluding( size.x(), size.y() );
    imageWidget.setTsImage( filmsService.loadFilmThumb( currFilmFile, thSize ) );
    imageWidget.redraw();
  }

}
