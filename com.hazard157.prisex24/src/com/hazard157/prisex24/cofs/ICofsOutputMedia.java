package com.hazard157.prisex24.cofs;

/**
 * COFS - access to the output media files.
 * <p>
 * Output media files includes:
 * <ul>
 * <li>films;</li>
 * <li>???other common episode output files;</li>
 * <li>episode framents;</li>
 * <li>episode trailes;</li>
 * <li>gaze output (artifacts) ???;</li>
 * <li>mingle output (artifacts) ???;</li>
 * <li>???;</li>
 * <li>wallpapers;</li>
 * <li>development files temporary output;</li>
 * <li>;</li>
 * <li>;</li>
 * <li>;</li>
 * <li>zzz.</li>
 * </ul>
 * In general, output media can be divided into two categories (by content):
 * <ul>
 * <li>incident media - is created from one incident;</li>
 * <li>general media - is created from multiple incidents.</li>
 * </ul>
 * Another media categorization criterion comes from the way of creation and hence the kind of media created:
 * <ul>
 * <li>still image files (JPGs);</li>
 * <li>animated image files (GIFs). They differ from short clips in that they are looped and designed to repeat
 * continuously.;</li>
 * <li>small clips illustrating idea, or video shooting methods, or media processing ways, etc.;</li>
 * <li>kind of video called episode trailers;</li>
 * <li>kind of long video - films, usually the compile same type of content from multiple incidents;</li>
 * <li>(TODO are artifacts the output media?) artifacts - images, GIFs, video clips, audio clips used for other media
 * development;</li>
 * <li>what else ???.</li>
 * </ul>
 * All media files have meta information. Part of meta info "belonging to incident" is derived from the placement of the
 * file in COFS file system. remaining part is placed in media file convoy files.
 *
 * @author hazard157
 */
public interface ICofsOutputMedia {

  // TODO divide output media access to different interfaces like ICofsFilms

}
