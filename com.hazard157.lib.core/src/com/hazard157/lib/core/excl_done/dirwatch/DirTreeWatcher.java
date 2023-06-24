package com.hazard157.lib.core.excl_done.dirwatch;

import static java.nio.file.LinkOption.*;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.synch.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Class monitors specified file system directories and informs about any changes.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class DirTreeWatcher
    implements ICloseable {

  /**
   * A continuously running daemon thread that watches for changes to the file system.
   *
   * @author hazard157
   */
  class WatchThread
      extends Thread {

    private static final long THREAD_SPEEP_MSECS = 250;

    private final WatchService             watcher;
    private final IMapEdit<WatchKey, Path> map = new SynchronizedMap<>( new ElemMap<>() );

    /**
     * Constructor.
     *
     * @throws IOException - {@link WatchService} can not be started
     */
    @SuppressWarnings( "resource" )
    public WatchThread()
        throws IOException {
      watcher = FileSystems.getDefault().newWatchService();
    }

    // ------------------------------------------------------------------------------------
    // Runnnable
    //

    //
    // TODO TRANSLATE
    //

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public void run() {
      IListEdit<DtwChangeEvent> changeEvents = new ElemLinkedBundleList<>();
      for( ;; ) {
        try {
          // wait for key to be signalled
          WatchKey key = watcher.take();
          Path dir = map.getByKey( key );
          if( dir == null ) {
            continue;
          }

          // выберем все события
          for( WatchEvent<?> event : key.pollEvents() ) {
            WatchEvent.Kind kind = event.kind();
            if( kind == OVERFLOW ) {
              continue;
            }
            // Context for directory entry event is the file name of entry
            WatchEvent<Path> ev = (WatchEvent<Path>)event;
            Path child = dir.resolve( ev.context() );
            // if directory is created, and watching recursively, then register it and its sub-directories
            if( kind == ENTRY_CREATE && Files.isDirectory( child, NOFOLLOW_LINKS ) ) {
              registerRecursively( child, IDtwDirRegCallback.NONE );
            }
            changeEvents.add( new DtwChangeEvent( child, kind ) );
          }
          // reset key and remove from set if directory no longer accessible
          if( !key.reset() ) {
            map.removeByKey( key );
          }
          if( !changeEvents.isEmpty() ) {
            fireChangeEvent( changeEvents );
            changeEvents = new ElemLinkedBundleList<>();
          }
          Thread.sleep( THREAD_SPEEP_MSECS );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    // ------------------------------------------------------------------------------------
    // Внутренные методы
    //

    void register( Path aDir, IDtwDirRegCallback aCallback )
        throws IOException {
      WatchKey key = aDir.register( watcher, ENTRY_CREATE, ENTRY_DELETE );
      aCallback.onDirRegistered( aDir );
      map.put( key, aDir );
    }

    void registerRecursively( final Path aRootDir, IDtwDirRegCallback aCallback )
        throws IOException {
      Files.walkFileTree( aRootDir, new SimpleFileVisitor<Path>() {

        @Override
        public FileVisitResult preVisitDirectory( Path dir, BasicFileAttributes attrs )
            throws IOException {
          register( dir, aCallback );
          return FileVisitResult.CONTINUE;
        }
      } );
    }

    // ------------------------------------------------------------------------------------
    // API
    //

    void setWatchRoots( IList<Path> aRoots, IDtwDirRegCallback aCallback ) {
      map.clear();
      for( Path root : aRoots ) {
        try {
          if( TsFileUtils.isDirReadable( root.toFile() ) ) {
            registerRecursively( root, aCallback );
          }
        }
        catch( IOException ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

  }

  private final Display               display;
  final IListEdit<IDtwChangeListener> listeners   = new ElemArrayList<>();
  private WatchThread                 watchThread = null;

  /**
   * Конструктор.
   *
   * @param aDisplay {@link Display} - дисплей, в главном потоке которого будут вызываться методы обратного вызова
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public DirTreeWatcher( Display aDisplay ) {
    TsNullArgumentRtException.checkNull( aDisplay );
    display = aDisplay;
    try {
      watchThread = new WatchThread();
    }
    catch( IOException ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    if( watchThread != null ) {
      watchThread.setDaemon( true );
      watchThread.start();
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void fireChangeEvent( IList<DtwChangeEvent> aEvents ) {
    display.asyncExec( () -> {
      if( listeners.isEmpty() ) {
        return;
      }
      IList<IDtwChangeListener> ll = new ElemArrayList<>( listeners );
      for( IDtwChangeListener l : ll ) {
        l.onFileSystemChanged( aEvents );
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    watchThread.setWatchRoots( IList.EMPTY, IDtwDirRegCallback.NONE );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает корневые директорий для мониторинга.
   *
   * @param aRoots IList&lt;{@link Path}&gt; - корневые директорий для мониторинга
   * @param aCallback {@link IDtwDirRegCallback} - извещатель о регистрациях директорий
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException люой аргумент - не директория
   */
  public void setWatchRoots( IList<Path> aRoots, IDtwDirRegCallback aCallback ) {
    TsNullArgumentRtException.checkNulls( aRoots, aCallback );
    IListEdit<Path> roots = new ElemLinkedBundleList<>();
    for( Path p : aRoots ) {
      p = p.normalize();
      if( !roots.hasElem( p ) ) {
        roots.add( p );
      }
    }
    watchThread.setWatchRoots( roots, aCallback );
  }

  public void setWatchRoot( File aDir, IDtwDirRegCallback aCallback ) {
    TsFileUtils.checkDirReadable( aDir );
    Path path = Path.of( aDir.getAbsolutePath() );
    setWatchRoots( new SingleItemList<>( path ), aCallback );
  }

  /**
   * Добавляет слушатель изменений в файловой системе.
   *
   * @param aListener {@link IDtwChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addFileSystemChangeListener( IDtwChangeListener aListener ) {
    synchronized (listeners) {
      if( !listeners.hasElem( aListener ) ) {
        listeners.add( aListener );
      }
    }
  }

  /**
   * Удаляет слушатель изменений в файловой системе.
   *
   * @param aListener {@link IDtwChangeListener} - слушатель
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void removeFileSystemChangeListener( IDtwChangeListener aListener ) {
    synchronized (listeners) {
      listeners.remove( aListener );
    }
  }

}
