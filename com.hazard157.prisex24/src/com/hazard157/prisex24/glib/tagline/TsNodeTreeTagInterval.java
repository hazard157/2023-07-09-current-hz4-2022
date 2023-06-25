package com.hazard157.prisex24.glib.tagline;

import static com.hazard157.prisex24.glib.tagline.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.excl_plan.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Узел дерева {@link ITsTreeViewer}, соответствующий одному интервалу из {@link ITagLine#marks(String)}.
 * <p>
 * Узел для режима дерева.
 *
 * @author hazard157
 */
public class TsNodeTreeTagInterval
    extends AbstractTsNode<Secint> {

  /**
   * Тип узла.
   */
  @SuppressWarnings( "nls" )
  public static ITsNodeKind<Secint> KIND =
      new TsNodeKind<>( "TagInterval", STR_N_NK_TAG_INTERVAL, STR_D_NK_TAG_INTERVAL, Secint.class, false, null );

  private final ITag owner;

  /**
   * Конструктор.
   *
   * @param aParent {@link ITsNode} - родительский узел или null
   * @param aOwner {@link ITag} - ярлык, чей этот интервал
   * @param aEntity {@link Secint} - иньервал
   */
  public TsNodeTreeTagInterval( ITsNode aParent, ITag aOwner, Secint aEntity ) {
    super( KIND, aParent, aEntity );
    TsNullArgumentRtException.checkNull( aOwner );
    owner = aOwner;
    setName( aEntity.fullStr() );
  }

  /**
   * Возвращает ярлык, чей этот интервал.
   *
   * @return {@link ITag} - ярлык, чей этот интервал
   */
  public ITag owner() {
    return owner;
  }

  @Override
  protected IList<ITsNode> doGetNodes() {
    return IList.EMPTY;
  }

}
