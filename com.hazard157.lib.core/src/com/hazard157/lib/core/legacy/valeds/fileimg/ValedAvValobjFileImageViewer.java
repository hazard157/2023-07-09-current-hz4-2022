package com.hazard157.lib.core.legacy.valeds.fileimg;

import static com.hazard157.lib.core.legacy.valeds.fileimg.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.legacy.picview.*;

/**
 * Редактор (точнее, просмотрщик) атрибута, который содержит имя файла изображения.
 * <p>
 * Использует кеш {@link ITsImageManager} из контекст приложения.
 *
 * @author goga
 */
public class ValedAvValobjFileImageViewer
    extends AbstractValedControl<IAtomicValue, Control> {

  /**
   * Идентификатор параметра {@link #OPDEF_VIEWER_SIZE}.
   */
  public static final String OPID_VIEWER_SIZE = "ru.toxsoft.tsgui.ValedAvImageFileViewer.ViewerSize"; //$NON-NLS-1$

  /**
   * Размер области просмотра изображения.<br>
   * Тип: {@link EThumbSize}<br>
   * Применение: редактор будет отображать изображение в квадратной области указанного размера.<br>
   * По умолчанию: {@link EThumbSize#SZ128}
   */
  public static final IDataDef OPDEF_VIEWER_SIZE = DataDef.create( OPID_VIEWER_SIZE, VALOBJ, //
      TSID_NAME, STR_N_VAIFNV_VIEWER_SIZE, //
      TSID_DESCRIPTION, STR_D_VAIFNV_VIEWER_SIZE, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 ) //
  );

  private final ITsImageManager imagesCache;
  private PictureViewer         picViewer = null;
  private IAtomicValue          value     = IAtomicValue.NULL;

  ValedAvValobjFileImageViewer( ITsGuiContext aTsContext ) {
    super( aTsContext );
    imagesCache = tsContext().get( ITsImageManager.class );
    setParamIfNull( OPDEF_NO_FIELD_LABEL, AV_TRUE );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, avInt( 8 ) );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private void updateViewer() {
    picViewer.clearImage();
    // задаем размер области просмотра
    EThumbSize viewerSize = OPDEF_VIEWER_SIZE.getValue( params() ).asValobj();
    picViewer.getControl().setMinimumSize( viewerSize.pointSize() );
    picViewer.getControl().setMaximumSize( viewerSize.pointSize() );
    picViewer.getControl().setSize( viewerSize.size(), viewerSize.size() );
    // загрузим миниатюру соответствующего размера
    File imgFile = new File( value.asString() );
    if( !imgFile.exists() ) {
      return;
    }
    TsImage mi = null;
    try {
      mi = imagesCache.findThumb( imgFile, viewerSize );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      clearValue();
      return;
    }
    if( mi != null ) {
      picViewer.setMultiImage( mi );
    }
    picViewer.setExpandToFit( false );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    picViewer = new PictureViewer( aParent, tsContext(), SWT.CENTER );
    picViewer.setExpandToFit( false );
    updateViewer();
    return picViewer.getControl();
  }

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    if( aId == null || aId.equals( OPID_VIEWER_SIZE ) ) {
      updateViewer();
    }
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    value = aValue != null ? aValue : IAtomicValue.NULL;
    updateViewer();
  }

  @Override
  protected void doClearValue() {
    value = IAtomicValue.NULL;
    picViewer.clearImage();
  }

}
