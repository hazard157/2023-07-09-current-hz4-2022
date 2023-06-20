package com.hazard157.prisex24.glib.tagline;

import static com.hazard157.prisex24.glib.tagline.IPsxResources.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Узел дерева {@link ITsTreeViewer}, соответствующий листу (не узлу!) в дереве ярлыков {@link IUnitTags}.
 * <p>
 * Узел для режима списка.
 *
 * @author hazard157
 */
public class TsNodeListTag
    extends AbstractTsNode<ITag> {

  /**
   * Идентификатор типа узла.
   */
  public static final String KIND_ID = "ListTag"; //$NON-NLS-1$

  /**
   * Тип узла.
   */
  public static ITsNodeKind<ITag> KIND =
      new TsNodeKind<>( KIND_ID, STR_N_NK_TAG_LEAF, STR_D_NK_TAG_LEAF, ITag.class, true, null );

  private final ITagLine tagline;

  /**
   * Конструктор.
   *
   * @param aParent {@link ITsNode} - родительский узел или null
   * @param aTagLine {@link ITagLine} - теглайн эпизода
   * @param aEntity {@link ITag} - группа ярлыков
   */
  public TsNodeListTag( ITsNode aParent, ITagLine aTagLine, ITag aEntity ) {
    super( KIND, aParent, aEntity );
    TsNullArgumentRtException.checkNull( aTagLine );
    tagline = aTagLine;
    setName( aEntity.id() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected IList<ITsNode> doGetNodes() {
    IListEdit<ITsNode> nodes = new ElemLinkedBundleList<>();
    for( Secint in : tagline.marks( entity().id() ).values() ) {
      TsNodeTreeTagInterval n = new TsNodeTreeTagInterval( this, entity(), in );
      nodes.add( n );
    }
    return nodes;
  }

}
