package com.hazard157.prisex24.utils.frasel;

import static com.hazard157.common.IHzConstants.*;
import static com.hazard157.prisex24.IPrisex24CoreConstants.*;
import static com.hazard157.prisex24.utils.frasel.EFramesPerSvin.*;
import static com.hazard157.prisex24.utils.frasel.IPsxResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creates {@link EFramesPerSvin} selection drop-down menu for the action {@link #AI_FRAMES_PER_SVIN_MENU}.
 * <p>
 * USer must handle {@link #AI_FRAMES_PER_SVIN_MENU} button click action and set any {@link EFramesPerSvin} as it
 * wishes. Note: it is recommended to set button action icon ID to the really used constant icon ID.
 *
 * @author hazard157
 */

public class FramesPerSvinDropDownMenuCreator
    extends AbstractMenuCreator
    implements ITsGuiContextable {

  /**
   * ID of action {@link #AI_FRAMES_PER_SVIN_MENU}.
   */
  public static final String AID_FRAMES_PER_SVIN_MENU = HZ_ACT_ID + ".FramesPerSvinDropDownMenu"; //$NON-NLS-1$

  /**
   * ID of action {@link #AI_FRAMES_PER_SVIN_SELECTED}.
   */
  public static final String AID_FRAMES_PER_SVIN_BOTH = HZ_ACT_ID + ".FramesPerSvin." + SELECTED.id(); //$NON-NLS-1$

  /**
   * ID of action {@link #AI_FRAMES_PER_SVIN_ONE_NO_MORE}.
   */
  public static final String AID_FRAMES_PER_SVIN_ONE_NO_MORE = HZ_ACT_ID + ".FramesPerSvin." + ONE_NO_MORE.id(); //$NON-NLS-1$

  /**
   * ID of action {@link #AI_FRAMES_PER_SVIN_FORCE_ONE}.
   */
  public static final String AID_FRAMES_PER_SVIN_FORCE_ONE = HZ_ACT_ID + ".FramesPerSvin." + FORCE_ONE.id(); //$NON-NLS-1$

  /**
   * Action with drop-down menu to control {@link ISvinFramesParams}.
   */
  public static final ITsActionDef AI_FRAMES_PER_SVIN_MENU = TsActionDef.ofMenu2( AID_FRAMES_PER_SVIN_MENU, //
      STR_FRAMES_PER_SVIN_MENU, STR_FRAMES_PER_SVIN_MENU_D, ICONID_FRAMES_PER_SVIN_QUESTION );

  /**
   * Action: set animation kind to {@link EFramesPerSvin#SELECTED}.
   */
  public static final ITsActionDef AI_FRAMES_PER_SVIN_SELECTED = TsActionDef.ofPush2( AID_FRAMES_PER_SVIN_BOTH, //
      SELECTED.nmName(), SELECTED.description(), SELECTED.iconId() );

  /**
   * Action: set animation kind to {@link EFramesPerSvin#ONE_NO_MORE}.
   */
  public static final ITsActionDef AI_FRAMES_PER_SVIN_ONE_NO_MORE =
      TsActionDef.ofPush2( AID_FRAMES_PER_SVIN_ONE_NO_MORE, //
          ONE_NO_MORE.nmName(), ONE_NO_MORE.description(), ONE_NO_MORE.iconId() );

  /**
   * Action: set animation kind to {@link EFramesPerSvin#FORCE_ONE}.
   */
  public static final ITsActionDef AI_FRAMES_PER_SVIN_FORCE_ONE = TsActionDef.ofPush2( AID_FRAMES_PER_SVIN_FORCE_ONE, //
      FORCE_ONE.nmName(), FORCE_ONE.description(), FORCE_ONE.iconId() );

  private static final IMap<EFramesPerSvin, ITsActionDef> mapKindAct;

  static {
    IMapEdit<EFramesPerSvin, ITsActionDef> map = new ElemMap<>();
    map.put( EFramesPerSvin.SELECTED, AI_FRAMES_PER_SVIN_SELECTED );
    map.put( EFramesPerSvin.ONE_NO_MORE, AI_FRAMES_PER_SVIN_ONE_NO_MORE );
    map.put( EFramesPerSvin.FORCE_ONE, AI_FRAMES_PER_SVIN_FORCE_ONE );
    mapKindAct = map;
  }

  final ITsGuiContext     tsContext;
  final ISvinFramesParams subject;

  /**
   * Constructor.
   *
   * @param aSubject {@link ISvinFramesParams} - managed entity
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public FramesPerSvinDropDownMenuCreator( ISvinFramesParams aSubject, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aSubject, aContext );
    subject = aSubject;
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // AbstractMenuCreator
  //

  @Override
  protected boolean fillMenu( Menu aMenu ) {
    for( EFramesPerSvin k : mapKindAct.keys() ) {
      ITsActionDef ad = mapKindAct.getByKey( k );
      MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
      mItem.setText( ad.nmName() );
      mItem.setToolTipText( ad.description() );
      mItem.setImage( iconManager().loadStdIcon( ad.iconId(), hdpiService().getMenuIconsSize() ) );
      mItem.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent e ) {
          doSetFramesPerSvin( subject, k );
        }
      } );
      mItem.setEnabled( subject.framesPerSvin() != k );
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Performs actual frames per SVIN change.
   * <p>
   * In the base class, simply sets the given size of aFramesPerSvin. Whether to call the parent method when overriding
   * depends on the usage logic.
   *
   * @param aSubject {@link ISvinFramesParams} - managed entity, never is <code>null</code>
   * @param aValue {@link EFramesPerSvin} - new value, never is <code>null</code>
   */
  public void doSetFramesPerSvin( ISvinFramesParams aSubject, EFramesPerSvin aValue ) {
    aSubject.setFramesPerSvin( aValue );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
