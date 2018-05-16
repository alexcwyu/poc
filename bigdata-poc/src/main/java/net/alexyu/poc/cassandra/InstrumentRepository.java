package net.alexyu.poc.cassandra;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import net.alexyu.poc.model.proto.Instrument;

import java.util.List;

public class InstrumentRepository implements Respository<Instrument, String> {

    private static String INSERT_CQL = "insert into instruments " +
            "(inst_id, symbol, exch_id, name, type, ccy_id, country, alt_symbols, alt_exch_ids, " +
            "gics_sector, gics_industry_group, gics_industry, gics_sub_industry, alt_classifications, " +
            "und_inst_id, option_type, option_style, strike, exp_date, multiplier) " +
            "values " +
            "(:inst_id, :symbol, :exch_id, :name, :type, :ccy_id, :country, :alt_symbols, :alt_exch_ids, " +
            ":gics_sector, :gics_industry_group, :gics_industry, :gics_sub_industry, :alt_classifications, " +
            ":und_inst_id, :option_type, :option_style, :strike, :exp_date, :multiplier)";

    private static String SELECT_ALL_CQL = "select inst_id, symbol, exch_id, name, type, ccy_id, country, alt_symbols, " +
            "alt_exch_ids, gics_sector, gics_industry_group, gics_industry, gics_sub_industry, alt_classifications, " +
            "und_inst_id, option_type, option_style, strike, exp_date, multiplier from instruments";


    private static String SELECT_CQL = SELECT_ALL_CQL + " where inst_id = ?";

    private static String DELETE_ALL_CQL = "delete from instruments";

    private static String DELETE_CQL = DELETE_ALL_CQL + " where inst_id = ?";

    private final Session session;
    private final PreparedStatement insertStatement;
    private final PreparedStatement selectStatement;
    private final PreparedStatement deleteStatement;


    public InstrumentRepository(Session session) {
        this.session = session;
        this.insertStatement = session.prepare(INSERT_CQL);
        this.selectStatement = session.prepare(SELECT_CQL);
        this.deleteStatement = session.prepare(DELETE_CQL);
    }


    private BoundStatement bind(Instrument instrument) {
        BoundStatement bound = insertStatement
                .bind("inst_id", instrument.getInstId());
        //.bind("symbol",instrument.getSym)
        return bound;
    }

    @Override
    public void insert(Instrument instrument) {

    }

    @Override
    public void insert(List<Instrument> list) {

    }

    @Override
    public Instrument select(String primaryKey) {
        BoundStatement bound = selectStatement.bind(primaryKey);
        ResultSet resultSet = session.execute(bound);

        return null;
    }

    @Override
    public List<Instrument> selectAll() {
        return null;
    }

    @Override
    public void delete(String primaryKey) {

    }

    @Override
    public void deleteAll() {

    }
}
