package com.hazard157.prisex24.explorer.pdu;

import org.toxsoft.core.txtproj.lib.sinent.*;

/**
 * Explorer saved inquiries.
 *
 * @author hazard157
 */
public interface IUnitExplorer
    extends ISinentManager<Inquiry, InquiryInfo> {

  /**
   * Explorer PDU unit ID.
   */
  String UNITID_EXPLORER = "Explorer_ts4"; //$NON-NLS-1$

}
