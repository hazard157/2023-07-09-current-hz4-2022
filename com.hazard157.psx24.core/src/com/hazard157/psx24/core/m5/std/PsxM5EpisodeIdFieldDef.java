package com.hazard157.psx24.core.m5.std;

import static com.hazard157.psx24.core.m5.std.IPsxResources.*;
import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.models.misc.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import com.hazard157.psx.common.stuff.*;
import com.hazard157.psx.common.utils.*;
import com.hazard157.psx.proj3.episodes.*;

/**
 * Поле {@link IEpisodeIdable#episodeId()}.
 *
 * @author goga
 * @param <T> - конкретный тип, реализующмй {@link IEpisodeIdable}
 */
public class PsxM5EpisodeIdFieldDef<T extends IEpisodeIdable>
    extends M5SingleLookupFieldDef<T, String> {

  /**
   * Идентификатор поля {@link IEpisodeIdable#episodeId()}.
   */
  public static final String FID_EPISODE_ID = "EpisodeId"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public PsxM5EpisodeIdFieldDef() {
    super( FID_EPISODE_ID, StringM5Model.MODEL_ID );
  }

  @Override
  protected void doInit() {
    setNameAndDescription( STR_N_FIELD_EPISODE_ID, STR_D_FIELD_EPISODE_ID );
    setFlags( M5FF_COLUMN );
    setDefaultValue( EpisodeUtils.EPISODE_ID_NONE );
    validator().addValidator( EpisodeUtils.EPISODE_ID_VALIDATOR );
    setLookupProvider( new IM5LookupProvider<String>() {

      @Override
      public IList<String> listItems() {
        IUnitEpisodes ue = tsContext().get( IUnitEpisodes.class );
        return new ElemArrayList<>( ue.items().keys() );
      }

      @Override
      public String getName( String aItem ) {
        return aItem;
      }
    } );
  }

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    return EpisodeUtils.ymdFromId( aEntity.episodeId() );
  }

  @Override
  protected String doGetFieldValue( T aEntity ) {
    return aEntity.episodeId();
  }

}
