
CREATE KEYSPACE IF NOT EXISTS algotrader WITH replication ={'class':'SimpleStrategy','replication_factor':1};

USE algotrader;

CREATE TABLE IF NOT EXISTS instruments(
  inst_id varchar,
  symbol varchar,
  exch_id varchar,
  name varchar,
  type varchar,
  ccy_id varchar,
  country varchar,
  alt_symbols map<varchar, varchar>,
  alt_exch_ids map<varchar, varchar>,
  gics_sector varchar,
  gics_industry_group varchar,
  gics_industry varchar,
  gics_sub_industry varchar,
  alt_classifications map<varchar, varchar>,
  und_inst_id varchar,
  option_type varchar,
  option_style varchar,
  strike double,
  exp_date int,
  multiplier double,
  PRIMARY KEY (inst_id)
);


CREATE TABLE IF NOT EXISTS positions{
  acct_id varchar,
  inst_id varchar,
  total_qty double,
  avg_price double,
  timestamp long,
  PRIMARY KEY (acct_id, inst_id)
}


CREATE TABLE IF NOT EXISTS accounts{
  acct_id varchar,
  acct_type varchar,
  ultimate_party_id varchar,
  bus_group varchar,
  region_id varchar,
  PRIMARY KEY (acct_id)
}