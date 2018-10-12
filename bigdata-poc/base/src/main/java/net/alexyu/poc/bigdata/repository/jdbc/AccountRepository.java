package net.alexyu.poc.bigdata.repository.jdbc;

import net.alexyu.common.jdbc.BaseJdbcRepository;
import net.alexyu.common.jdbc.RowUnmapper;
import net.alexyu.common.jdbc.TableDescription;
import net.alexyu.poc.model.proto.Account;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class AccountRepository extends BaseJdbcRepository<Account, String> {

    private static final String TBL_NAME = "accounts";
    private static final String acct_id = "acct_id";
    private static final String acct_name = "acct_name";
    private static final String firm_name = "firm_name";
    private static final String bus_group = "bus_group";
    private static final String region = "region";

    public static final RowMapper<Account> ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Account.Builder builder = Account.newBuilder();
        builder.setAcctId(rs.getString(acct_id));
        builder.setAcctName(rs.getString(acct_name));
        builder.setFirmName(rs.getString(firm_name));
        builder.setBusGroup(Account.BusGroup.forNumber(rs.getInt(bus_group)));
        builder.setRegion(Account.Region.forNumber(rs.getInt(region)));
        return builder.build();
    };


    private static final RowUnmapper<Account> ROW_UNMAPPER = (Account account) -> {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put(acct_id, account.getAcctId());
        row.put(acct_name, account.getAcctName());
        row.put(firm_name, account.getFirmName());
        row.put(bus_group, account.getBusGroup().getNumber());
        row.put(region, account.getRegion().getNumber());
        return row;
    };

    public AccountRepository() {
        super(ROW_MAPPER, ROW_UNMAPPER, Account::getAcctId, new TableDescription(TBL_NAME, acct_id));
    }


}
