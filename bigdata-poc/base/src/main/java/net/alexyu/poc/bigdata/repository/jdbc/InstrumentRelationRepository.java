package net.alexyu.poc.bigdata.repository.jdbc;

import net.alexyu.common.jdbc.BaseJdbcRepository;
import net.alexyu.common.jdbc.IDExtractor;
import net.alexyu.common.jdbc.RowUnmapper;
import net.alexyu.common.jdbc.TableDescription;
import net.alexyu.poc.model.proto.InstrumentRelation;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class InstrumentRelationRepository extends BaseJdbcRepository<InstrumentRelation, Pair<String, String>> {

    private static final String TBL_NAME = "instrument_relations";
    private static final String inst_id = "inst_id";
    private static final String const_inst_id = "const_inst_id";
    private static final String weight = "weight";


    public static final RowMapper<InstrumentRelation> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        InstrumentRelation.Builder builder = InstrumentRelation.newBuilder();
        builder.setInstId(rs.getString(inst_id));
        builder.setConstInstId(rs.getString(const_inst_id));
        builder.setWeight(rs.getDouble(weight));
        return builder.build();
    };

    private static final RowUnmapper<InstrumentRelation> ROW_UNMAPPER = (InstrumentRelation instrumentRelation) -> {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put(inst_id, instrumentRelation.getInstId());
        row.put(const_inst_id, instrumentRelation.getConstInstId());
        row.put(weight, instrumentRelation.getWeight());
        return row;
    };

    public static final IDExtractor<InstrumentRelation, Pair<String, String>> ID_EXTRACTOR
            = (InstrumentRelation instrumentRelation) -> Pair.of(instrumentRelation.getInstId(), instrumentRelation.getConstInstId());

    public InstrumentRelationRepository() {
        super(ROW_MAPPER, ROW_UNMAPPER, ID_EXTRACTOR, new TableDescription(TBL_NAME, null, inst_id, const_inst_id));
    }


}

