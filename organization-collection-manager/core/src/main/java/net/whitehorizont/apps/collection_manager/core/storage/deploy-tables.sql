CREATE TYPE organisation_type AS ENUM ('public', 'commercial', 'government', 'open_joint_stock_company');

CREATE TABLE IF NOT EXISTS organisations (
  -- owner
  id UUID PRIMARY KEY,
  name varchar NOT NULL,
  org_type organisation_type NOT NULL,
  annual_turnover double precision NOT NULL,
  full_name varchar NOT NULL,
  creation_date timestamp NOT NULL,
  coordinates_x integer NOT NULL,
  coordinates_y bigint NOT NULL,
  address_street varchar NOT NULL,
  address_zipcode varchar NOT NULL,
  address_location_x real NOT NULL,
  address_location_y double precision NOT NULL,
  address_location_z integer NOT NULL,
  address_location_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
  login VARCHAR PRIMARY KEY,
  hashed_password bytea NOT NULL,
  salt bytea NOT NULL
);

