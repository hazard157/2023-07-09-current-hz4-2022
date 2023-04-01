package com.hazard157.lib.core.legacy.valeds.fileimg;

import static com.hazard157.lib.core.legacy.valeds.fileimg.ITsResources.*;
import static com.hazard157.lib.core.legacy.valeds.fileimg.ValedAvValobjFileImageViewer.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.rcp.valed.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import com.hazard157.lib.core.legacy.picview.*;

/**
 * Редактор (точнее, просмотрщик) атрибута, который содержит имя файла изображения.
 * <p>
 * Использует кеш {@link ITsImageManager} из контекст приложения.
 *
 * @author hazard157
 */
public class ValedAvValobjFileImage
    extends AbstractValedControl<IAtomicValue, Control> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvImageFileName"; //$NON-NLS-1$

  /**
   * Data type {@link EAtomicType#STRING}: path to the image file.
   */
  public static final IDataType DT_IMAGEFILE_NAME = new DataType( EAtomicType.STRING, OptionSetUtils.createOpSet( //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY, //
      TSID_IS_NULL_ALLOWED, AV_TRUE, //
      ValedAvValobjFileImageViewer.OPDEF_VIEWER_SIZE, avValobj( EThumbSize.SZ360 ), //
      OPDEF_EDITOR_FACTORY_NAME, avStr( FACTORY_NAME ) //
  ) );

  /**
   * Valed factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjFileImage( aContext );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateViewer( ITsGuiContext aContext ) {
      return new ValedAvValobjFileImageViewer( aContext );
    }

  };

  final ITsImageManager imageManager;
  TsComposite           board                  = null;
  PictureViewer         picViewer              = null;
  ValedAvStringFile     valedFileName          = null;
  IAtomicValue          value                  = IAtomicValue.NULL;
  ValidationResult      cachedValidationResult = ValidationResult.SUCCESS;

  ValedAvValobjFileImage( ITsGuiContext aTsContext ) {
    super( aTsContext );
    imageManager = tsContext().get( ITsImageManager.class );
    setParamIfNull( OPDEF_NO_FIELD_LABEL, AV_TRUE );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, avInt( 8 ) );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  EThumbSize viewerSize() {
    return OPDEF_VIEWER_SIZE.getValue( params() ).asValobj();
  }

  void updateViewer() {
    // задаем размер области просмотра
    EThumbSize viewerSize = viewerSize();
    picViewer.getControl().setMinimumSize( viewerSize.pointSize() );
    board.setMaximumSize( viewerSize.pointSize() );
    picViewer.getControl().setMaximumSize( viewerSize.pointSize() );
    picViewer.getControl().setSize( viewerSize.size(), viewerSize.size() );
    // загрузим значок соответствующего размера
    File imgFile = new File( value.asString() );
    TsImage mi = null;
    if( imgFile.exists() ) {
      try {
        mi = imageManager.findThumb( imgFile, viewerSize );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        // игнорируем пока, mi = null приведет к сообщению об ошибке
      }
    }
    picViewer.setMultiImage( mi );
    if( mi == null && !value.asString().isEmpty() ) {
      cachedValidationResult = ValidationResult.warn( FMT_WARN_NOT_AN_IAMAGE, value.asString() );
    }
    else {
      cachedValidationResult = ValidationResult.SUCCESS;
    }
    picViewer.setExpandToFit( false );
    board.layout( true, true );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    board = new TsComposite( aParent, SWT.BORDER );
    board.setLayout( new BorderLayout() );
    // picViewer
    picViewer = new PictureViewer( board, tsContext(), SWT.CENTER );
    picViewer.getControl().setLayoutData( BorderLayout.CENTER );
    picViewer.setExpandToFit( false );
    // valedFileName
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    // ValedAvFileName.FILE_EXTENSIONS.setValue( ctx.params(), IMediaFileConstants.IMAGE_FILE_EXT_LIST );
    IValedFileConstants.OPDEF_IS_OPEN_DIALOG.setValue( ctx.params(), AV_TRUE );
    IValedFileConstants.OPDEF_MUST_EXIST.setValue( ctx.params(), AV_TRUE );
    IValedFileConstants.OPDEF_IS_DIRECTORY.setValue( ctx.params(), AV_FALSE );
    OPDEF_IS_HEIGHT_FIXED.setValue( ctx.params(), AV_FALSE );
    valedFileName = new ValedAvStringFile( ctx );
    valedFileName.createControl( board );
    valedFileName.getControl().setLayoutData( BorderLayout.SOUTH );
    valedFileName.setValue( value );
    valedFileName.eventer().addListener( ( aSource, aEditFinished ) -> {
      value = valedFileName.getValue();
      updateViewer();
      fireModifyEvent( true );
    } );
    updateViewer();
    aParent.layout( true, true );
    return board;
  }

  @Override
  public ValidationResult canGetValue() {
    return cachedValidationResult;
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
    valedFileName.setValue( aValue );
    updateViewer();
  }

  @Override
  protected void doClearValue() {
    value = IAtomicValue.NULL;
    picViewer.clearImage();
  }

}
