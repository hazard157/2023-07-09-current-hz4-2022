package com.hazard157.psx24.core.glib.tagline;

import static com.hazard157.psx24.core.glib.tagline.IPsxResources.*;

import org.eclipse.core.runtime.dynamichelpers.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.psx.proj3.episodes.proplines.*;
import com.hazard157.psx.proj3.tags.*;

/**
 * Узел дерева {@link ITsTreeViewer}, соответствующий узлу (не листу!) в дереве ярлыков {@link IUnitTags}.
 * <p>
 * Узел для режима дерева.
 *
 * @author goga
 */
public class TsNodeTreeTagGroup
    extends AbstractTsNode<ITag> {

  /**
   * Идентификатор типа узла.
   */
  public static final String KIND_ID = "Tag"; //$NON-NLS-1$

  /**
   * Тип узла.
   */
  public static ITsNodeKind<ITag> KIND =
      new TsNodeKind<>( KIND_ID, STR_N_NK_TAG_GROUP, STR_D_NK_TAG_GROUP, ITag.class, true, null );

  private final ITagLine          tagline;
  private final ITsFilter<String> tagIdFilter;

  /**
   * Конструктор.
   *
   * @param aParent {@link ITsNode} - родительский узел или null
   * @param aTagLine {@link ITagLine} - теглайн эпизода
   * @param aEntity {@link ITag} - группа ярлыков
   * @param aTagIdFilter {@link IFilter}&lt;String&gt; - фильтр отбора ярлыков по идентификаторам
   */
  public TsNodeTreeTagGroup( ITsNode aParent, ITagLine aTagLine, ITag aEntity, ITsFilter<String> aTagIdFilter ) {
    super( KIND, aParent, aEntity );
    TsNullArgumentRtException.checkNulls( aTagLine, aTagIdFilter );
    tagline = aTagLine;
    tagIdFilter = aTagIdFilter;
    setName( aEntity.nodeName() );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected IList<ITsNode> doGetNodes() {
    IListEdit<ITsNode> nodes = new ElemLinkedBundleList<>();
    for( ITag tn : entity().childNodes() ) {
      // ярлык (не группу) лорбавимЮ, если его ИД проходит фильтр
      if( tn.childNodes().isEmpty() ) {
        if( tagIdFilter.accept( tn.id() ) ) {
          nodes.add( new TsNodeTreeTagLeaf( this, tagline, tn ) );
        }
      }
      else {
        if( tagIdFilter.accept( tn.id() ) ) {
          nodes.add( new TsNodeTreeTagGroup( this, tagline, tn, tagIdFilter ) );
        }
      }
    }
    return nodes;
  }

}
