CREATE TABLE real_estate_sales(
  id  SERIAL PRIMARY KEY,
  name VARCHAR(50),
  serial_nbr INT,
  list_year INT,
  date_recorded DATE,
  assessed_value REAL,
  sale_price REAL,
  additional_remarks REAL,
  sales_ratio DOUBLE PRECISION ,
  non_use_code INT,
  residential_type VARCHAR(50),
  residential_units INT,
  address VARCHAR(255),
  location VARCHAR(255)
);