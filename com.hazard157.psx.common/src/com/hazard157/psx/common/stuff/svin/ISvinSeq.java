package com.hazard157.psx.common.stuff.svin;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

import com.hazard157.lib.core.quants.secint.*;

/**
 * Read-only ordered list of SVINS as sequence of visualization.
 *
 * @author hazard157
 */
public interface ISvinSeq {

  /**
   * Returns list of SVINs making this set.
   *
   * @return {@link IList}&lt;{@link Svin}&gt; - list of SVINs
   */
  IList<Svin> svins();

  /**
   * Returns all SVINs with {@link Svin#episodeId()} equal to the given episode ID.
   *
   * @param aEpisodeId String - the episode ID
   * @return {@link IList}&lt;{@link Svin}&gt; - list of SVINs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<Svin> listByEpisode( String aEpisodeId );

  /**
   * Returns ID of episodes whose SVINs are present in {@link #svins()}.
   *
   * @return {@link IStringList} - episode IDs list
   */
  IStringList listEpisodeIds();

  /**
   * Returns all SVINs with {@link Svin#cameraId()} equal to the given camera ID.
   *
   * @param aCameraId String - the camera ID
   * @return {@link IList}&lt;{@link Svin}&gt; - list of SVINs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<Svin> listByCamera( String aCameraId );

  /**
   * Returns ID of cameras whose SVINs are present in {@link #svins()}.
   *
   * @return {@link IStringList} - camera IDs list
   */
  IStringList listCameraIds();

  /**
   * Selects SVINs intersecting with the given interval.
   * <p>
   * In the resulting list are included all SVINs intersecting with the <code>aInterval</code>. SVINs will be cut
   * (shrinked) so they'll fit in argument if {@link ISvinSeq} = <code>true</code>. If {@link ISvinSeq} =
   * <code>false</code> then SVINs are returned as they are in {@link #svins()} list.
   *
   * @param aInterval {@link Secint} - the selection interval
   * @param aCutSvins boolean - <code>true</code> to cut SVINs to fit in the <code>aInterval</code>
   * @return {@link IList}&lt;{@link Svin}&gt; - list of SVINs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<Svin> listByInterval( Secint aInterval, boolean aCutSvins );

}
