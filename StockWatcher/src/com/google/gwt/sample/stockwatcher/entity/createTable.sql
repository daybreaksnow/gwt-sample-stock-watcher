create table ADDRESS_USER(
 user_id bigint not null,
 username varchar(30),
 home_address_id bigint,
 billing_address_id bigint,
 version bigint
)
;

--User PK
ALTER TABLE ADDRESS_USER ADD PRIMARY KEY(user_id);

create table ADDRESS(
 address_id bigint not null,
 street varchar(250),
 city varchar(250),
 zip_code varchar(10),
 version bigint
)
;
--Address PK
ALTER TABLE ADDRESS ADD PRIMARY KEY(address_id);

--FK
ALTER TABLE ADDRESS_USER ADD CONSTRAINT USER_ADDRESS_FK
 FOREIGN KEY (home_address_id)
 REFERENCES ADDRESS(address_id);

 --FK
ALTER TABLE ADDRESS_USER ADD CONSTRAINT USER_ADDRESS_HOME_FK
 FOREIGN KEY (home_address_id)
 REFERENCES ADDRESS(address_id);

  --FK
ALTER TABLE ADDRESS_USER ADD CONSTRAINT USER_ADDRESS_BILLING_FK
 FOREIGN KEY (billing_address_id)
 REFERENCES ADDRESS(address_id);

--住所IDをユニークに
ALTER TABLE ADDRESS_USER ADD CONSTRAINT USER_ADDRESS_HOME_UNIQUE unique(home_address_id);
ALTER TABLE ADDRESS_USER ADD CONSTRAINT USER_ADDRESS_BILLING_UNIQUE unique(billing_address_id);
