package com.hazard157.lib.core.excl_done.partman;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Свойства вью для его создания и настройки.
 *
 * @author hazard157
 */
public final class PartInfo {

  private final String partId;

  private String  label           = EMPTY_STRING;
  private String  tooltip         = EMPTY_STRING;
  private String  iconUri         = null;
  private String  contributionUri = null;
  private boolean closeable       = false;

  /**
   * Constructor.
   *
   * @param aPartId String - part ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is a blank string
   */
  public PartInfo( String aPartId ) {
    partId = TsErrorUtils.checkNonBlank( aPartId );
  }

  /**
   * Creates {@link PartInfo} filled with properties of argument.
   *
   * @param aPart {@link MPart} - an E4 UI part
   * @return {@link PartInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static PartInfo ofPart( MPart aPart ) {
    TsNullArgumentRtException.checkNull( aPart );
    PartInfo pinf = new PartInfo( aPart.getElementId() );
    pinf.setCloseable( aPart.isCloseable() );
    pinf.setContributionUri( aPart.getContributionURI() );
    pinf.setIconUri( aPart.getIconURI() );
    pinf.setLabel( aPart.getLabel() );
    pinf.setTooltip( aPart.getTooltip() );
    return pinf;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * returns the part ID used as {@link MPart#getElementId()}.
   *
   * @return String - the part ID
   */
  public String partId() {
    return partId;
  }

  /**
   * Returns part label used as tab name {@link MPart#getLabel()}.
   *
   * @return String - visible label of the part
   */
  public String getLabel() {
    return label;
  }

  /**
   * Returns part label used as tab name {@link MPart#getLabel()}.
   *
   * @param aLabel String - visible label of the part
   */
  public void setLabel( String aLabel ) {
    label = aLabel;
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip( String aTooltip ) {
    tooltip = aTooltip;
  }

  public String getIconUri() {
    return iconUri;
  }

  public void setIconUri( String aIconUri ) {
    iconUri = aIconUri;
  }

  public void setIconUri( String aPluginId, String aIconId, EIconSize aIconSize ) {
    iconUri = TsIconManagerUtils.makeStdIconUriString( aPluginId, aIconId, aIconSize );
  }

  /**
   * Returns the URI of the class making part's content.
   *
   * @return String - contribution class URI
   */
  public String getContributionUri() {
    return contributionUri;
  }

  /**
   * Sets the URI of the class making part's content.
   * <p>
   * Contribution URI may be build as<br>
   * <code>String uri ="bundleclass://" + PLUGIN_ID + "/" + UipartClass.class.getName();</code>
   *
   * @param aContributionUri String - contribution class URI
   */
  public void setContributionUri( String aContributionUri ) {
    contributionUri = aContributionUri;
  }

  public void setContributionUri( String aPluginId, Class<?> aClass ) {
    TsErrorUtils.checkNonBlank( aPluginId );
    TsNullArgumentRtException.checkNull( aClass );
    contributionUri = "bundleclass://" + aPluginId + '/' + aClass.getName(); //$NON-NLS-1$
  }

  public boolean isCloseable() {
    return closeable;
  }

  public void setCloseable( boolean aCloseable ) {
    closeable = aCloseable;
  }

}
