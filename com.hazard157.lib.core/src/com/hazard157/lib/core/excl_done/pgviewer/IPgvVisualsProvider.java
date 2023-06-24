package com.hazard157.lib.core.excl_done.pgviewer;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Поставщик отображаемого изображения текстов для просмотрщика {@link IPicsGridViewer}.
 *
 * @author hazard157
 * @param <V> - тип ображаемых сущностей
 */
public interface IPgvVisualsProvider<V> {

  /**
   * Поставщие текстов по умолчанию.
   */
  @SuppressWarnings( "rawtypes" )
  IPgvVisualsProvider DEFAULT = new InternalDefaultTextxProvider();

  /**
   * Возвращает миниатюру сущности.
   *
   * @param aEntity &lt;V&gt; - отображаемая сущность
   * @param aThumbSize {@link EThumbSize} - запраиваемый размер значка
   * @return {@link TsImage} - изображение миниатюры размером {@link EThumbSize} или <code>null</code>
   */
  TsImage getThumb( V aEntity, EThumbSize aThumbSize );

  /**
   * Возвращает текст первой строки подписи.
   *
   * @param aEntity &lt;V&gt; - подписываемая сущность
   * @return String - текст первой строки подписи, не должен быть <code>null</code>
   */
  String getLabel1( V aEntity );

  /**
   * Возвращает текст второй строки подписи.
   *
   * @param aEntity &lt;V&gt; - отображаемая сущность
   * @return String - текст первой строки подписи, не должен быть <code>null</code>
   */
  String getLabel2( V aEntity );

  /**
   * Возвращает текст всплыающей подсказки.
   *
   * @param aEntity &lt;V&gt; - отображаемая сущность
   * @return String - текст подсказки или <code>null</code>, если подсказки нет
   */
  String getTooltip( V aEntity );

}

@SuppressWarnings( "rawtypes" )
class InternalDefaultTextxProvider
    implements IPgvVisualsProvider {

  @Override
  public TsImage getThumb( Object aEntity, EThumbSize aThumbSize ) {
    return null;
  }

  @Override
  public String getLabel1( Object aEntity ) {
    return aEntity.toString();
  }

  @Override
  public String getLabel2( Object aEntity ) {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public String getTooltip( Object aEntity ) {
    return TsLibUtils.EMPTY_STRING;
  }

}
