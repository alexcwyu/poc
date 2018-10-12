package net.alexyu.poc.bigdata.repository.jdbc;

import net.alexyu.common.jdbc.BaseJdbcRepository;
import net.alexyu.common.jdbc.IDExtractor;
import net.alexyu.common.jdbc.RowUnmapper;
import net.alexyu.common.jdbc.TableDescription;
import net.alexyu.poc.model.proto.Position;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class PositionRepository extends BaseJdbcRepository<Position, Pair<String, String>> {

    private static final String TBL_NAME = "positions";
    private static final String acct_id = "acct_id";
    private static final String inst_id = "inst_id";
    private static final String total_qty = "total_qty";
    private static final String avg_price = "avg_price";
    private static final String create_timestamp = "create_timestamp";
    private static final String update_timestamp = "update_timestamp";


    public static final RowMapper<Position> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Position.Builder builder = Position.newBuilder();
        builder.setAcctId(rs.getString(acct_id));
        builder.setInstId(rs.getString(inst_id));
        builder.setTotalQty(rs.getDouble(total_qty));
        builder.setAvgPrice(rs.getDouble(avg_price));
        builder.setCreateTimestamp(rs.getLong(create_timestamp));
        builder.setUpdateTimestamp(rs.getLong(update_timestamp));

        return builder.build();
    };

    private static final RowUnmapper<Position> ROW_UNMAPPER = (Position position) -> {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put(acct_id, position.getAcctId());
        row.put(inst_id, position.getInstId());
        row.put(total_qty, position.getTotalQty());
        row.put(avg_price, position.getAvgPrice());
        row.put(create_timestamp, position.getCreateTimestamp());
        row.put(update_timestamp, position.getUpdateTimestamp());
        return row;
    };

    public static final IDExtractor<Position, Pair<String, String>> ID_EXTRACTOR
            = (Position position) -> Pair.of(position.getAcctId(), position.getInstId());

    public PositionRepository() {
        super(ROW_MAPPER, ROW_UNMAPPER, ID_EXTRACTOR, new TableDescription(TBL_NAME, null, acct_id, inst_id));
    }


}
