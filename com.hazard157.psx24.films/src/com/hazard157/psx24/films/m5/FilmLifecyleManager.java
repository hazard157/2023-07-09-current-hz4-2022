package com.hazard157.psx24.films.m5;

import java.io.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.psx24.films.e4.services.*;

/**
 * уМенеджер ЖЦ модели {@link FilmM5Model}.
 *
 * @author hazard157
 */
class FilmLifecyleManager
    extends M5LifecycleManager<File, IPsxFilmsService> {

  public FilmLifecyleManager( IM5Model<File> aModel, IPsxFilmsService aMaster ) {
    super( aModel, false, false, false, true, aMaster );

  }

  IPsxFilmsService fs() {
    if( master() != null ) {
      return master();
    }
    return tsContext().get( IPsxFilmsService.class );
  }

  @Override
  protected IList<File> doListEntities() {
    return fs().listFilmFiles();
  }

}
