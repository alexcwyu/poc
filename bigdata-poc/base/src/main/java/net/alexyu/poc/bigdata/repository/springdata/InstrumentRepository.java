package net.alexyu.poc.bigdata.repository.springdata;

import net.alexyu.poc.entity.Instrument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends CrudRepository<Instrument, String> {
}
