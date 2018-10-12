package net.alexyu.poc.bigdata.repository.jdbc;

import net.alexyu.common.jdbc.BaseJdbcRepository;
import net.alexyu.common.jdbc.RowUnmapper;
import net.alexyu.common.jdbc.TableDescription;
import net.alexyu.poc.model.proto.Instrument;
import org.apache.logging.log4j.util.Strings;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class InstrumentRepository extends BaseJdbcRepository<Instrument, String> {

    private static final String TBL_NAME = "instruments";
    private static final String inst_id = "inst_id";
    private static final String symbol = "symbol";
    private static final String exch_id = "exch_id";
    private static final String name = "name";
    private static final String type = "type";
    private static final String ccy_id = "ccy_id";
    private static final String country_id = "country_id";

    private static final String gics_sector = "gics_sector";
    private static final String gics_industry_group = "gics_industry_group";
    private static final String gics_industry = "gics_industry";
    private static final String gics_sub_industry = "gics_sub_industry";

    private static final String und_inst_id = "und_inst_id";
    private static final String option_type = "option_type";
    private static final String option_style = "option_style";
    private static final String strike = "strike";
    private static final String exp_date = "exp_date";
    private static final String conversion_ratio = "conversion_ratio";

    private static final String alt_symbols = "alt_symbols";
    private static final String alt_exch_ids = "alt_exch_ids";
    private static final String alt_classifications = "alt_classifications";


    public static final RowMapper<Instrument> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Instrument.Builder builder = Instrument.newBuilder();

        builder.setInstId(rs.getString(inst_id));
        builder.setSymbol(rs.getString(symbol));
        builder.setExchId(rs.getString(exch_id));
        builder.setName(rs.getString(name));
        builder.setType(Instrument.InstType.forNumber(rs.getInt(type)));
        builder.setCcyId(rs.getString(ccy_id));
        builder.setCountryId(rs.getString(country_id));

        String underInstId = rs.getString(und_inst_id);
        if (underInstId!= null)
            builder.setUndInstId(underInstId);
        builder.setOptionStyle(Instrument.OptionStyle.forNumber(rs.getInt(option_style)));
        builder.setOptionType(Instrument.OptionType.forNumber(rs.getInt(option_type)));
        builder.setStrike(rs.getDouble(strike));
        builder.setExpDate(rs.getInt(exp_date));
        builder.setConversionRatio(rs.getDouble(conversion_ratio));

        builder.setGicsSector(rs.getInt(gics_sector));
        builder.setGicsIndustryGroup(rs.getInt(gics_industry_group));
        builder.setGicsIndustry(rs.getInt(gics_industry));
        builder.setGicsSubIndustry(rs.getInt(gics_sub_industry));

        return builder.build();
    };


    private static final RowUnmapper<Instrument> ROW_UNMAPPER = (Instrument instrument) -> {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put(inst_id, instrument.getInstId());
        row.put(symbol, instrument.getSymbol());
        row.put(exch_id, instrument.getExchId());
        row.put(name, instrument.getName());
        row.put(type, instrument.getType().getNumber());
        row.put(ccy_id, instrument.getCcyId());
        row.put(country_id, instrument.getCountryId());


        row.put(und_inst_id, (Strings.isBlank(instrument.getUndInstId()))? null : instrument.getUndInstId());
        row.put(option_type, instrument.getOptionType().getNumber());
        row.put(option_style, instrument.getOptionStyle().getNumber());
        row.put(strike, instrument.getStrike());
        row.put(exp_date, instrument.getExpDate());
        row.put(conversion_ratio, instrument.getConversionRatio());

        row.put(gics_sector, instrument.getGicsSector());
        row.put(gics_industry_group, instrument.getGicsIndustryGroup());
        row.put(gics_industry, instrument.getGicsIndustry());
        row.put(gics_sub_industry, instrument.getGicsSubIndustry());

        return row;
    };

    public InstrumentRepository() {
        super(ROW_MAPPER, ROW_UNMAPPER, Instrument::getInstId, new TableDescription(TBL_NAME, inst_id));
    }

}
