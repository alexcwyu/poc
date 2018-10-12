package net.alexyu.poc.bigdata.repository.springdata;

import net.alexyu.poc.entity.Position;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends CrudRepository<Position, Position.PositionId> {
}
