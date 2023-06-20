package com.hazard157.psx24.core.m5.note;

import static com.hazard157.psx24.core.m5.note.IPsxResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;

import com.hazard157.lib.core.quants.secint.*;
import com.hazard157.psx.proj3.episodes.proplines.*;

class MarkNoteLifecycleManager
    extends M5LifecycleManager<MarkNote, INoteLine> {

  public MarkNoteLifecycleManager( IM5Model<MarkNote> aModel, INoteLine aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<MarkNote> aValues ) {
    Secint in = MarkNoteM5Model.INTERVAL.getFieldValue( aValues );
    if( master().marksMap().hasKey( in ) ) {
      return ValidationResult.error( FMT_ERR_ALREADY_INTERVAL, in.toString() );
    }
    Secint maxIn = new Secint( 0, master().duration() - 1 );
    if( !maxIn.contains( in ) ) {
      return ValidationResult.error( FMT_ERR_SECINT_OUT_OF_RANGE, in.toString(), maxIn.toString() );
    }
    String note = MarkNoteM5Model.NOTE.getFieldValue( aValues ).asString();
    if( note.isEmpty() ) {
      return ValidationResult.error( MSG_ERR_EMPTY_NOTE, in.toString() );
    }
    // пересечение с другими интервалами
    if( master().getNotesAt( in ).size() > 0 ) {
      return ValidationResult.warn( FMT_WARN_NOTES_INTERSECT, in.toString() );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected MarkNote doCreate( IM5Bunch<MarkNote> aValues ) {
    Secint in = MarkNoteM5Model.INTERVAL.getFieldValue( aValues );
    String note = MarkNoteM5Model.NOTE.getFieldValue( aValues ).asString();
    return master().putMark( in, note );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<MarkNote> aValues ) {
    Secint in = MarkNoteM5Model.INTERVAL.getFieldValue( aValues );
    if( !aValues.originalEntity().in().equals( in ) ) {
      if( master().marksMap().hasKey( in ) ) {
        return ValidationResult.error( FMT_ERR_ALREADY_INTERVAL, in.toString() );
      }
    }
    Secint maxIn = new Secint( 0, master().duration() - 1 );
    if( !maxIn.contains( in ) ) {
      return ValidationResult.error( FMT_ERR_SECINT_OUT_OF_RANGE, in.toString(), maxIn.toString() );
    }
    String note = MarkNoteM5Model.NOTE.getFieldValue( aValues ).asString();
    if( note.isEmpty() ) {
      return ValidationResult.error( MSG_ERR_EMPTY_NOTE, in.toString() );
    }
    // пересечение с другими интервалами
    for( MarkNote m : master().listMarks() ) {
      if( m.in().intersects( in ) && !m.equals( aValues.originalEntity() ) ) {
        return ValidationResult.warn( FMT_WARN_NOTES_INTERSECT, in.toString() );
      }
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected MarkNote doEdit( IM5Bunch<MarkNote> aValues ) {
    Secint in = MarkNoteM5Model.INTERVAL.getFieldValue( aValues );
    String note = MarkNoteM5Model.NOTE.getFieldValue( aValues ).asString();
    master().removeMark( aValues.originalEntity().interval() );
    return master().putMark( in, note );
  }

  @Override
  protected ValidationResult doBeforeRemove( MarkNote aEntity ) {
    Secint in = aEntity.in();
    if( !master().marksMap().hasKey( in ) ) {
      return ValidationResult.error( FMT_ERR_NO_INTERVAL, in.toString() );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doRemove( MarkNote aEntity ) {
    master().removeMark( aEntity.in() );
  }

  @Override
  protected IList<MarkNote> doListEntities() {
    return master().listMarks();
  }

}
