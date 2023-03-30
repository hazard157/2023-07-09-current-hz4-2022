package com.hazard157.lib.core.incub.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.ResourceBundle.Control;

/**
 * Вспомогательный класс для чтения ресурсов локализации в кодировке UTF8.
 * <p>
 * Описание проблемы и источник решения:<a
 * href=http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle>
 * http://stackoverflow.com/questions/4659929/how-to-use-utf-8-in-resource-properties-with-resourcebundle</a>
 * <p>
 * Пример использования: <br>
 * <code>ResourceBundle bundle = ResourceBundle.getBundle("com.example.i18n.text", new UTF8Control());</code>
 * <p>
 * Код поправлен для удаления предупреждений try-with-resources
 *
 * @author mvk
 */
public class TsL10nUtf8Control
    extends Control {

  // ------------------------------------------------------------------------------------
  // Реализация
  //

  @Override
  public ResourceBundle newBundle( String aBaseName, Locale aLocale, String aFormat, ClassLoader aLoader,
      boolean aReload )
      throws IllegalAccessException,
      InstantiationException,
      IOException {
    // The below is a copy of the default implementation.
    String bundleName = toBundleName( aBaseName, aLocale );
    String resourceName = toResourceName( bundleName, "properties" ); //$NON-NLS-1$
    ResourceBundle bundle = null;
    try( InputStream stream = getInputStream( aLoader, aReload, resourceName ); ) {
      if( stream != null ) {
        try {
          // Only this line is changed to make it to read properties files as UTF-8.
          bundle = new PropertyResourceBundle( new InputStreamReader( stream, "UTF-8" ) ); //$NON-NLS-1$
        }
        finally {
          stream.close();
        }
      }
    }
    return bundle;
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  // Формирует поток чтения ресурса
  private static InputStream getInputStream( ClassLoader aLoader, boolean aReload, String aResourceName )
      throws IOException {
    if( aReload ) {
      URL url = aLoader.getResource( aResourceName );
      if( url != null ) {
        URLConnection connection = url.openConnection();
        if( connection != null ) {
          connection.setUseCaches( false );
          return connection.getInputStream();
        }
      }
      return null;
    }
    return aLoader.getResourceAsStream( aResourceName );
  }

}
