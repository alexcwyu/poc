DROP TABLE IF EXISTS positions CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS instrument_relations CASCADE;
DROP TABLE IF EXISTS instruments CASCADE;

CREATE TABLE instruments (
  inst_id             varchar(255),
  symbol              varchar(60)  NOT NULL,
  exch_id             varchar(60)  NOT NULL,
  name                varchar(255) NOT NULL,
  type                int          NOT NULL,
  ccy_id              varchar(3)   NOT NULL,
  country_id          varchar(30)  NOT NULL,

  gics_sector         int,
  gics_industry_group int,
  gics_industry       int,
  gics_sub_industry   int,

  und_inst_id         varchar(255) REFERENCES instruments (inst_id),
  option_type         int,
  option_style        int,
  strike              real,
  exp_date            int,
  conversion_ratio    real,

  alt_symbols         hstore,
  alt_exch_ids        hstore,
  alt_classifications hstore,
  PRIMARY KEY (inst_id)
);


CREATE TABLE instrument_relations (
  inst_id       varchar(255) REFERENCES instruments (inst_id),
  const_inst_id varchar(255) REFERENCES instruments (inst_id),
  weight        real NOT NULL,
  PRIMARY KEY (inst_id, const_inst_id)
);

CREATE TABLE accounts (
  acct_id   varchar(12),
  acct_name varchar(255) NOT NULL,
  firm_name varchar(255) NOT NULL,
  bus_group int          NOT NULL,
  region    int          NOT NULL,
  PRIMARY KEY (acct_id)
);


CREATE TABLE positions (
  acct_id          varchar(12) REFERENCES accounts (acct_id),
  inst_id          varchar(255) REFERENCES instruments (inst_id),
  total_qty        real   NOT NULL,
  avg_price        real   NOT NULL,
  create_timestamp bigint NOT NULL,
  update_timestamp bigint NOT NULL,
  PRIMARY KEY (acct_id, inst_id)
);