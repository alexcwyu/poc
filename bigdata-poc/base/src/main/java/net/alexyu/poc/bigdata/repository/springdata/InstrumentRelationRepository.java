package net.alexyu.poc.bigdata.repository.springdata;

import net.alexyu.poc.entity.InstrumentRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRelationRepository extends CrudRepository<InstrumentRelation, InstrumentRelation.InstrumentRelationId> {
}

