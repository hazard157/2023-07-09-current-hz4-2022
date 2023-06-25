package com.hazard157.lib.core.excl_done.utils;

import static com.hazard157.lib.core.excl_done.utils.ITsResources.*;

import java.util.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Набор методов общего назначения, никак не попадающие в другие разделы.
 *
 * @author hazard157
 */
public final class HzMiscUtils {

  // private static final int PS_WAIT_TIMEOUT_SECS = 5;
  // private static final int PROCESS_EXIT_WAIT_POLL_INTERVAL_MSECS = 10;

  /**
   * Запускает внешенюю программу с указанными аргументами.
   * <p>
   * Время таймаута - 5 сек.
   *
   * @param aProgramName String - имя программы (полный путь или имя для поиска в пути PATH)
   * @param aArgs String[] - аргументы комндной строки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException имя программы пустая строка
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  // public static void runProgram( String aProgramName, String... aArgs ) {
  // runProgram( PS_WAIT_TIMEOUT_SECS, aProgramName, aArgs );
  // }

  /**
   * Запускает внешенюю программу с указанными аргументами.
   *
   * @param aTimeoutSecs int - максимальное время ожидания завершения программы (0= не ждать)
   * @param aProgramName String - имя программы (полный путь или имя для поиска в пути PATH)
   * @param aArgs String[] - аргументы комндной строки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException имя программы пустая строка
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  // public static void runProgram( int aTimeoutSecs, String aProgramName, String... aArgs ) {
  // TsNullArgumentRtException.checkNull( aProgramName );
  // TsErrorUtils.checkArrayArg( aArgs );
  // TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
  // try {
  // String[] cmdarr = new String[1 + aArgs.length];
  // cmdarr[0] = aProgramName;
  // for( int i = 0; i < aArgs.length; i++ ) {
  // cmdarr[i + 1] = aArgs[i];
  // }
  // Process p = Runtime.getRuntime().exec( cmdarr );
  // int toutSecs = aTimeoutSecs;
  // if( toutSecs < 0 ) {
  // toutSecs = 0;
  // }
  // if( toutSecs > 365 * 24 * 3600 ) {
  // toutSecs = 365 * 24 * 3600;
  // }
  // p.waitFor( toutSecs, TimeUnit.SECONDS );
  // }
  // catch( Exception ex ) {
  // throw new TsIoRtException( ex, aProgramName );
  // }
  // }

  /**
   * Запускает внешенюю программу и возвращает его консольный выход.
   *
   * @param aProgramName String - имя программы (полный путь или имя для поиска в пути PATH)
   * @param aArgs String[] - аргументы комндной строки
   * @return {@link Pair}&lt;IStringList,IStringList&gt; - пара выходов консоль/ошибки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException имя программы пустая строка
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  // public static Pair<IStringList, IStringList> runUtility( String aProgramName, String... aArgs ) {
  // TsNullArgumentRtException.checkNull( aProgramName );
  // TsErrorUtils.checkArrayArg( aArgs );
  // TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
  // try {
  // String[] cmdarr = new String[1 + aArgs.length];
  // cmdarr[0] = aProgramName;
  // for( int i = 0; i < aArgs.length; i++ ) {
  // cmdarr[i + 1] = aArgs[i];
  // }
  // // запуск программы
  // Process proc = Runtime.getRuntime().exec( cmdarr );
  // proc.waitFor( PS_WAIT_TIMEOUT_SECS, TimeUnit.SECONDS );
  // String s = null;
  // // стандартный выход
  // BufferedReader stdInput = new BufferedReader( new InputStreamReader( proc.getInputStream() ) );
  // IStringListEdit slOut = new StringLinkedBundleList();
  // while( (s = stdInput.readLine()) != null ) {
  // slOut.add( s );
  // }
  // // выход ошибок
  // BufferedReader stdError = new BufferedReader( new InputStreamReader( proc.getErrorStream() ) );
  // IStringListEdit slErr = new StringLinkedBundleList();
  // while( (s = stdError.readLine()) != null ) {
  // slErr.add( s );
  // }
  // return new Pair<>( slOut, slErr );
  // }
  // catch( Exception ex ) {
  // throw new TsIoRtException( ex, aProgramName );
  // }
  // }

  /**
   * Запускает программу и немедленно возвращает управление.
   *
   * @param aProgramName String - имя программы (полный путь или имя для поиска в пути PATH)
   * @param aArgs String[] - аргументы комндной строки
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException имя программы пустая строка
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  // public static void runNoWait( String aProgramName, String... aArgs ) {
  // TsNullArgumentRtException.checkNull( aProgramName );
  // TsErrorUtils.checkArrayArg( aArgs );
  // TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
  // String[] cmdarr = new String[1 + aArgs.length];
  // cmdarr[0] = aProgramName;
  // for( int i = 0; i < aArgs.length; i++ ) {
  // cmdarr[i + 1] = aArgs[i];
  // }
  // try {
  // Runtime.getRuntime().exec( cmdarr );
  // }
  // catch( IOException ex ) {
  // throw new TsIoRtException( ex, aProgramName );
  // }
  // }

  /**
   * Запускает внешенюю программу и ожидает его завершения.
   *
   * @param aProgramName String - имя программы (полный путь или имя для поиска в пути PATH)
   * @param aArgs String[] - аргументы комндной строки
   * @return int - код возврата вызванной команды
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException имя программы пустая строка
   * @throws TsIoRtException возникла проблема при запуске внешней программы
   */
  // public static int runAndWait( String aProgramName, String... aArgs ) {
  // TsNullArgumentRtException.checkNull( aProgramName );
  // TsErrorUtils.checkArrayArg( aArgs );
  // TsIllegalArgumentRtException.checkTrue( aProgramName.length() == 0 );
  // String[] cmdarr = new String[1 + aArgs.length];
  // cmdarr[0] = aProgramName;
  // for( int i = 0; i < aArgs.length; i++ ) {
  // cmdarr[i + 1] = aArgs[i];
  // }
  // try {
  // // запуск программы
  // Process proc = Runtime.getRuntime().exec( cmdarr );
  // // перехвать вывода программы
  // BufferedReader stdInput = new BufferedReader( new InputStreamReader( proc.getInputStream() ) );
  // BufferedReader stdError = new BufferedReader( new InputStreamReader( proc.getErrorStream() ) );
  // // ждем окончания программы
  // while( proc.isAlive() ) {
  // // и пока ждем, выбираем вывод программы в никуда, иначе программа может встать (если много пишет в вывод)
  // while( stdInput.ready() ) {
  // stdInput.read();
  // // TsTestUtils.p( "%c", stdInput.read() );
  // }
  // // выход ошибок
  // while( stdError.ready() ) {
  // stdError.read();
  // // TsTestUtils.p( "%c", stdError.read() );
  // }
  // // некторое время дадим другим потокам поработать нормально
  // Thread.sleep( PROCESS_EXIT_WAIT_POLL_INTERVAL_MSECS );
  // }
  // proc.waitFor();
  // return proc.exitValue();
  // }
  // catch( IOException | InterruptedException ex ) {
  // // FIXME throw new TsThreadingRtException( ex );
  // throw new TsIoRtException( ex, aProgramName );
  // }
  // }

  /**
   * Сравнивает две {@link Comparable} ссылки, которые могут быть null.
   * <p>
   * Если обе ссылки не-null, возвращает результат {@link Comparable#compareTo(Object)}. В случае если обе ссылки null,
   * то они считаются равными и возвращается 0. Если только одна ссылка null, то она читается "меньше", чем не-null
   * ссылка.
   *
   * @param <V> - конкретный тип наследника {@link Comparable}
   * @param aThis &lt;V&gt; - ссылка на левый сравниваемый объект
   * @param aThat &lt;V&gt; - ссылка на правый сравниваемый объект
   * @return int - результат сравнения
   */
  public static <V extends Comparable<V>> int compare( V aThis, V aThat ) {
    if( aThis != null && aThat != null ) {
      return aThis.compareTo( aThat );
    }
    if( aThis != null ) {
      return +1;
    }
    if( aThat != null ) {
      return -1;
    }
    return 0;
  }

  private static final long KIB = 1024L;
  private static final long MIB = 1024L * KIB;
  private static final long GIB = 1024L * MIB;
  private static final long TIB = 1024L * GIB;

  /**
   * Формирует удобочитаемую строку размера в байтах.
   * <p>
   * Удобочитаемая строка имеет вид типа "1,2КБ", "27,2Гб" и т.п.
   *
   * @param aBytes long - размер в байтах
   * @return String - удобочитаемая строка
   * @throws TsIllegalArgumentRtException aBytes < 0
   */
  public static String makeHumanReadableBytes( long aBytes ) {
    TsIllegalArgumentRtException.checkTrue( aBytes < 0 );
    if( aBytes >= TIB ) {
      long gibs = aBytes / GIB;
      return String.format( FMT_HRB_TIB, Double.valueOf( gibs / 1024.0 ) );
    }
    if( aBytes >= GIB ) {
      return String.format( FMT_HRB_GIB, Double.valueOf( aBytes / GIB ) );
    }
    if( aBytes >= MIB ) {
      return String.format( FMT_HRB_MIB, Double.valueOf( aBytes / MIB ) );
    }
    if( aBytes >= KIB ) {
      return String.format( FMT_HRB_KIB, Double.valueOf( aBytes / KIB ) );
    }
    // aBytes < 1024
    return Long.valueOf( aBytes ) + STR_HRB_BYTE;
  }

  // ------------------------------------------------------------------------------------
  // l10n
  //

  private static final String L10N_FILES_EXT = ".messages"; //$NON-NLS-1$

  /**
   * Возвращает ресурсы локализации для указанного класса.
   * <p>
   * Ресурсы локализации должны в том же пакете, что и класс.
   *
   * @param aClass {@link Class} - запрашиваемый класс
   * @return {@link ResourceBundle} ресурсы локализации или <code>null</code> если нет ресурсов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ResourceBundle findResourceBundle( Class<?> aClass ) {
    TsNullArgumentRtException.checkNull( aClass );
    String bundleName = aClass.getPackage().getName() + L10N_FILES_EXT;
    try {
      return ResourceBundle.getBundle( bundleName, new TsL10nUtf8Control() );
    }
    catch( @SuppressWarnings( "unused" ) MissingResourceException ex ) {
      return null;
    }
  }

  /**
   * Проводит попытку локализации указанной строки через указанные ресурсы.
   * <p>
   * Если аргумент <code>null</code>, то просто возвращает aKey.
   *
   * @param aBundle {@link ResourceBundle} ресурсы локализации или <code>null</code>
   * @param aKey String - ключ строки для локализации
   * @return String локализованная строка, если строка не найдена то возвращается aKey
   * @throws TsNullArgumentRtException aKey = null
   */
  public static String tryLocalization( ResourceBundle aBundle, String aKey ) {
    TsNullArgumentRtException.checkNulls( aKey );
    if( aBundle == null ) {
      // Холостой вызов
      return aKey;
    }
    try {
      return aBundle.getString( aKey );
    }
    catch( @SuppressWarnings( "unused" ) MissingResourceException ex ) {
      return aKey;
    }
  }

  /**
   * Запрет на создание экземпляров.
   */
  private HzMiscUtils() {
    // nop
  }

}
