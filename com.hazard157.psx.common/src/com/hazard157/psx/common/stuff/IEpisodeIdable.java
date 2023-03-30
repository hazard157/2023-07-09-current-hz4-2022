package com.hazard157.psx.common.stuff;

/**
 * Entities related to the episode.
 *
 * @author hazard157
 */
public interface IEpisodeIdable {

  /**
   * Returns the episode ID.
   * <p>
   * Always returns valid value: an IDpath that satisfies the syntax requirements of the episode ID validation TODO ???
   *
   * @return String - syntaxicaly valid episode ID
   */
  String episodeId();

}
