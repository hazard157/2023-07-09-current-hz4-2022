package com.hazard157.prisex24.m5;

import static com.hazard157.prisex24.IPrisex24CoreConstants.*;

/**
 * PRISEX M5-modeling constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IPsxM5Constants {

  String MID_EPISODE = PSX_ID + ".Episode"; //$NON-NLS-1$

  String MID_TODO             = PSX_ID + ".Todo"; //$NON-NLS-1$
  String FID_NOTE             = "Note";           //$NON-NLS-1$
  String FID_CREATION_TIME    = "CreationTime";   //$NON-NLS-1$
  String FID_PRIORITY         = "Priority";       //$NON-NLS-1$
  String FID_TEXT             = "Text";           //$NON-NLS-1$
  String FID_IS_DONE          = "IsDone";         //$NON-NLS-1$
  String FID_TODO_ID          = "TodoId";         //$NON-NLS-1$
  String FID_RELATED_TODO_IDS = "RelatedTodoIds"; //$NON-NLS-1$
  String FID_FULFIL_STAGES    = "FulfilStages";   //$NON-NLS-1$

  String MID_FULFIL_STAGE = PSX_ID + "FulfilStage"; //$NON-NLS-1$
  String FID_WHEN         = "When";                 //$NON-NLS-1$

  String MID_TAG = PSX_ID + ".Tag"; //$NON-NLS-1$

}
