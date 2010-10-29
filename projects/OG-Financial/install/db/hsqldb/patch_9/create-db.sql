-- IMPORTANT:
--
-- This file was generated by concatenating the three other .sql files together. It can be
-- used for testing, but the separate SQL sequences will be necessary if the Security Master
-- and Position Master need to be installed in different databases.
--
-- Please do not modify it - modify the originals and recreate this using 'ant create-db-sql'.


    create sequence hibernate_sequence start with 1 increment by 1;


-- create-db-config.sql: Config Master

-- design has one document
--  config
-- unitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence cfg_config_seq as bigint
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby/HSQL, not accepted by Postgresql

create table cfg_config (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    name varchar(255) not null,
    config_type varchar(255) not null,
    config blob not null,
    primary key (id),
    constraint cfg_chk_config_ver_order check (ver_from_instant <= ver_to_instant)
);

create index ix_cfg_config_oid on cfg_config(oid);
create index ix_cfg_config_config_type on cfg_config(config_type);


-- create-db-security.sql: Security Master

-- design has one document
--  security and associated identity key
-- bitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence sec_security_seq as bigint
    start with 1000 increment by 1 no cycle;
create sequence sec_idkey_seq as bigint
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby/HSQL, not accepted by Postgresql

create table sec_security (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    name varchar(255) not null,
    sec_type varchar(255) not null,
    primary key (id),
    constraint sec_fk_sec2sec foreign key (oid) references sec_security (id),
    constraint sec_chk_sec_ver_order check (ver_from_instant <= ver_to_instant),
    constraint sec_chk_sec_corr_order check (corr_from_instant <= corr_to_instant)
);

create table sec_idkey (
    id bigint not null,
    key_scheme varchar(255) not null,
    key_value varchar(255) not null,
    primary key (id),
    constraint sec_chk_idkey unique (key_scheme, key_value)
);

create table sec_security2idkey (
    security_id bigint not null,
    idkey_id bigint not null,
    constraint sec_fk_secidkey2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_secidkey2idkey foreign key (idkey_id) references sec_idkey (id)
);
-- sec_security_idkey is fully dependent of sec_security

-- Hibernate controlled tables
create table sec_currency (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_commodityfuturetype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_bondfuturetype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_cashrate (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_unit (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_exchange (
    id bigint not null,
    name varchar(255) not null unique,
    description varchar(255),
    primary key (id)
);

create table sec_gics (
    id bigint not null,
    name varchar(8) not null unique,
    description varchar(255),
    primary key (id)
);

create table sec_equity (
    id bigint not null,
    security_id bigint not null,
    shortName varchar(255),
    exchange_id bigint not null,
    companyName varchar(255) not null,
    currency_id bigint not null,
    gicscode_id bigint,
    primary key (id),
    constraint sec_fk_equity2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_equity2currency foreign key (currency_id) references sec_currency(id),
    constraint sec_fk_equity2exchange foreign key (exchange_id) references sec_exchange(id),
    constraint sec_fk_equity2gics foreign key (gicscode_id) references sec_gics(id)
);

create table sec_option (
    id bigint not null,
    security_id bigint not null,
    option_security_type varchar(32) not null,
    option_exercise_type varchar(32) not null,
    option_payoff_style varchar(32) not null,
    option_type varchar(32) not null,
    strike double precision not null,
    expiry_date timestamp not null,
    expiry_zone varchar(50) not null,
    expiry_accuracy smallint not null,
    underlying_scheme varchar(255) not null,
    underlying_identifier varchar(255) not null,
    currency_id bigint not null,
    put_currency_id bigint,
    call_currency_id bigint,
    exchange_id bigint,
    counterparty varchar(255),
    power double precision,
    cap double precision,
    margined boolean,
    pointValue double precision,
    payment double precision,
    lowerbound double precision,
    upperbound double precision,
    choose_date timestamp,
    choose_zone varchar(50),
    underlyingstrike double precision,
    underlyingexpiry_date timestamp,
    underlyingexpiry_zone varchar(50),
    underlyingexpiry_accuracy smallint,
    reverse boolean,
    primary key (id),
    constraint sec_fk_option2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_option2currency foreign key (currency_id) references sec_currency (id),
    constraint sec_fk_option2putcurrency foreign key (put_currency_id) references sec_currency (id),
    constraint sec_fk_option2callcurrency foreign key (call_currency_id) references sec_currency (id),
    constraint sec_fk_option2exchange foreign key (exchange_id) references sec_exchange (id)
);

create table sec_frequency (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_daycount (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_businessdayconvention (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
);

create table sec_issuertype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_market (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_yieldconvention (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_guaranteetype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_coupontype (
    id bigint not null,
    name varchar(255) not null unique,
    primary key (id)
 );

create table sec_bond (
    id bigint not null,
    security_id bigint not null,
    bond_type varchar(32) not null,
    issuername varchar(255) not null,
    issuertype_id bigint not null,
    issuerdomicile varchar(255) not null,
    market_id bigint not null,
    currency_id bigint not null,
    yieldconvention_id bigint not null,
    guaranteetype_id bigint,
    maturity_date timestamp not null,
    maturity_zone varchar(50) not null,
    maturity_accuracy smallint not null,
    coupontype_id bigint not null,
    couponrate double precision not null,
    couponfrequency_id bigint not null,
    daycountconvention_id bigint not null,
    businessdayconvention_id bigint,
    announcement_date timestamp,
    announcement_zone varchar(50),
    interestaccrual_date timestamp not null,
    interestaccrual_zone varchar(50) not null,
    settlement_date timestamp not null,
    settlement_zone varchar(50) not null,
    firstcoupon_date timestamp not null,
    firstcoupon_zone varchar(50) not null,
    issuanceprice double precision not null,
    totalamountissued double precision not null,
    minimumamount double precision not null,
    minimumincrement double precision not null,
    paramount double precision not null,
    redemptionvalue double precision not null,
    primary key (id),
    constraint sec_fk_bond2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_bond2issuertype foreign key (issuertype_id) references sec_issuertype (id),
    constraint sec_fk_bond2market foreign key (market_id) references sec_market (id),
    constraint sec_fk_bond2currency foreign key (currency_id) references sec_currency (id),
    constraint sec_fk_bond2yieldconvention foreign key (yieldconvention_id) references sec_yieldconvention (id),
    constraint sec_fk_bond2guaranteetype foreign key (guaranteetype_id) references sec_guaranteetype (id),
    constraint sec_fk_bond2coupontype foreign key (coupontype_id) references sec_coupontype (id),
    constraint sec_fk_bond2frequency foreign key (couponfrequency_id) references sec_frequency (id),
    constraint sec_fk_bond2daycount foreign key (daycountconvention_id) references sec_daycount (id),
    constraint sec_fk_bond2businessdayconvention foreign key (businessdayconvention_id) references sec_businessdayconvention (id)
);

create table sec_future (
    id bigint not null,
    security_id bigint not null,
    future_type varchar(32) not null,
    expiry_date timestamp not null,
    expiry_zone varchar(50) not null,
    expiry_accuracy smallint not null,
    tradingexchange_id bigint not null,
    settlementexchange_id bigint not null,
    currency1_id bigint,
    currency2_id bigint,
    currency3_id bigint,
    bondtype_id bigint,
    commoditytype_id bigint,
    cashratetype_id bigint,
    unitname_id bigint,
    unitnumber double precision,
    underlying_scheme varchar(255),
    underlying_identifier varchar(255), 
    primary key (id),
    constraint sec_fk_future2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_future2exchange1 foreign key (tradingexchange_id) references sec_exchange (id),
    constraint sec_fk_future2exchange2 foreign key (settlementexchange_id) references sec_exchange (id),
    constraint sec_fk_future2currency1 foreign key (currency1_id) references sec_currency (id),
    constraint sec_fk_future2currency2 foreign key (currency2_id) references sec_currency (id),
    constraint sec_fk_future2currency3 foreign key (currency3_id) references sec_currency (id),
    constraint sec_fk_future2bondfuturetype foreign key (bondtype_id) references sec_bondfuturetype (id),
    constraint sec_fk_future2commodityfuturetype foreign key (commoditytype_id) references sec_commodityfuturetype (id),
    constraint sec_fk_future2cashrate foreign key (cashratetype_id) references sec_cashrate (id),
    constraint sec_fk_future2unit foreign key (unitname_id) references sec_unit (id)
);

create table sec_futurebundle (
    id bigint not null,
    future_id bigint not null,
    startDate timestamp,
    endDate timestamp,
    conversionFactor double precision not null,
    primary key (id),
    constraint sec_fk_futurebundle2future foreign key (future_id) references sec_future (id)
);

create table sec_futurebundleidentifier (
    bundle_id bigint not null,
    scheme varchar(255) not null,
    identifier varchar(255) not null,
    primary key (bundle_id, scheme, identifier),
    constraint sec_fk_futurebundleidentifier2futurebundle foreign key (bundle_id) references sec_futurebundle (id)
);

create table sec_cash (
    id bigint not null,
    security_id bigint not null,
    currency_id bigint not null,
    region_scheme varchar(255) not null,
    region_identifier varchar(255) not null,
    maturity_date timestamp not null,
    maturity_zone varchar(50) not null,
    primary key (id),
    constraint sec_fk_cash2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_cash2currency foreign key (currency_id) references sec_currency (id)
);

create table sec_fra (
    id bigint not null,
    security_id bigint not null,
    currency_id bigint not null,
    region_scheme varchar(255) not null,
    region_identifier varchar(255) not null,
    start_date timestamp not null,
    start_zone varchar(50) not null,
    end_date timestamp not null,
    end_zone varchar(50) not null,
    primary key (id),
    constraint sec_fk_fra2sec foreign key (security_id) references sec_security (id),
    constraint sec_fk_fra2currency foreign key (currency_id) references sec_currency (id)
);

create table sec_swap (
    id bigint not null,
    security_id bigint not null,
    swaptype varchar(32) not null,
    trade_date timestamp not null,
    trade_zone varchar(50) not null,
    effective_date timestamp not null,
    effective_zone varchar(50) not null,
    maturity_date timestamp not null,
    maturity_zone varchar(50) not null,
    forwardstart_date timestamp,
    forwardstart_zone varchar(50),
    counterparty varchar(255) not null,
    pay_legtype varchar(32) not null,
    pay_daycount_id bigint not null,
    pay_frequency_id bigint not null,
    pay_regionscheme varchar(255) not null,
    pay_regionid varchar(255) not null,
    pay_businessdayconvention_id bigint not null,
    pay_notionaltype varchar(32) not null,
    pay_notionalcurrency_id bigint,
    pay_notionalamount double precision,
    pay_notionalscheme varchar(255),
    pay_notionalid varchar(255),
    pay_rate double precision,
    pay_spread double precision,
    pay_rateidentifierscheme varchar(255),
    pay_rateidentifierid varchar(255),
    receive_legtype varchar(32) not null,
    receive_daycount_id bigint not null,
    receive_frequency_id bigint not null,
    receive_regionscheme varchar(255) not null,
    receive_regionid varchar(255) not null,
    receive_businessdayconvention_id bigint not null,
    receive_notionaltype varchar(32) not null,
    receive_notionalcurrency_id bigint,
    receive_notionalamount double precision,
    receive_notionalscheme varchar(255),
    receive_notionalid varchar(255),
    receive_rate double precision,
    receive_spread double precision,
    receive_rateidentifierscheme varchar(255),
    receive_rateidentifierid varchar(255),
    primary key (id),
    constraint sec_fk_swap2sec foreign key (security_id) references sec_security (id)
);

-- create-db-position.sql: Position Master

-- design has two documents
--  portfolio and tree of nodes (nested set model)
--  position and associated security key
-- bitemporal versioning exists at the document level
-- each time a document is changed, a new row is written
-- with only the end instant being changed on the old row

create sequence pos_master_seq
    start with 1000 increment by 1 no cycle;
-- "as bigint" required by Derby, not accepted by Postgresql

create table pos_portfolio (
    id bigint not null,
    oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    name varchar(255) not null,
    primary key (id),
    constraint pos_fk_port2port foreign key (oid) references pos_portfolio (id),
    constraint pos_chk_port_ver_order check (ver_from_instant <= ver_to_instant),
    constraint pos_chk_port_corr_order check (corr_from_instant <= corr_to_instant)
);

create table pos_node (
    id bigint not null,
    oid bigint not null,
    portfolio_id bigint not null,
    portfolio_oid bigint not null,
    parent_node_id bigint,
    depth int,
    tree_left bigint not null,
    tree_right bigint not null,
    name varchar(255),
    primary key (id),
    constraint pos_fk_node2node foreign key (oid) references pos_node (id),
    constraint pos_fk_node2portfolio foreign key (portfolio_id) references pos_portfolio (id),
    constraint pos_fk_node2parentnode foreign key (parent_node_id) references pos_node (id)
);
-- pos_node is fully dependent of pos_portfolio
-- portfolio_oid is an optimization (can be derived via portfolio_id)
-- parent_node_id is an optimization (tree_left/tree_right hold all the tree structure)
-- depth is an optimization (tree_left/tree_right hold all the tree structure)

create table pos_position (
    id bigint not null,
    oid bigint not null,
    portfolio_oid bigint not null,
    parent_node_oid bigint not null,
    ver_from_instant timestamp not null,
    ver_to_instant timestamp not null,
    corr_from_instant timestamp not null,
    corr_to_instant timestamp not null,
    quantity decimal(31,8) not null,
    primary key (id),
    constraint pos_fk_posi2posi foreign key (oid) references pos_position (id),
    constraint pos_chk_posi_ver_order check (ver_from_instant <= ver_to_instant),
    constraint pos_chk_posi_corr_order check (corr_from_instant <= corr_to_instant)
);
-- portfolio_oid is an optimization

create table pos_securitykey (
    id bigint not null,
    position_id bigint not null,
    id_scheme varchar(255) not null,
    id_value varchar(255) not null,
    primary key (id),
    constraint pos_fk_securitykey2position foreign key (position_id) references pos_position (id)
);
-- pos_securitykey is fully dependent of pos_position

-------------------------------------
-- Static data
-------------------------------------

create table rsk_observation_time (
    id int not null,
    label varchar(255) not null,                -- LDN_CLOSE
    
    primary key (id),
    
    constraint rsk_chk_uq_obs_time unique (label)
);

create table rsk_observation_datetime (
	id int not null,
	date_part date not null,  
	time_part time null,						-- null if time of LDN_CLOSE not fixed yet
	observation_time_id int not null,    		  
	
	primary key (id),
	
	constraint rsk_fk_obs_datetime2obs_time
	    foreign key (observation_time_id) references rsk_observation_time (id),
	    
	constraint rsk_chk_obs_datetime check 
	    (time_part is not null or observation_time_id is not null), 
	
	constraint rsk_chk_uq_obs_datetime unique (date_part, observation_time_id)
);

create table rsk_compute_host (
	id int not null,
	host_name varchar(255) not null,
	
	primary key (id),
	
	constraint rsk_chk_uq_compute_host unique (host_name)
);

create table rsk_compute_node (
	id int not null,
	compute_host_id int not null,
	node_name varchar(255) not null,
	
	primary key (id),
	
	constraint rsk_fk_cmpt_node2cmpt_host
	    foreign key (compute_host_id) references rsk_compute_host (id),
	    
	constraint rsk_chk_uq_compute_node unique (node_name)
);

create table rsk_opengamma_version (
	id int not null,
	version varchar(255) not null, 
	hash varchar(255) not null,
	
	primary key (id),
	
	constraint rsk_chk_uq_opengamma_version unique (version, hash)
);

-- DBTOOLDONOTCLEAR
create table rsk_computation_target_type (
	id int not null,	 	            
    name varchar(255) not null,
    
    primary key (id),
    
    constraint rsk_chk_cmpt_target_type check
        ((id = 0 and name = 'PORTFOLIO_NODE') or
         (id = 1 and name = 'POSITION') or 
         (id = 2 and name = 'SECURITY') or
         (id = 3 and name = 'PRIMITIVE'))
);

insert into rsk_computation_target_type (id, name) values (0, 'PORTFOLIO_NODE');
insert into rsk_computation_target_type (id, name) values (1, 'POSITION');
insert into rsk_computation_target_type (id, name) values (2, 'SECURITY');
insert into rsk_computation_target_type (id, name) values (3, 'PRIMITIVE');

create table rsk_computation_target (
	id int not null,
	type_id int not null,
	id_scheme varchar(255) not null,
	id_value varchar(255) not null,
	
	primary key (id),
	
	constraint rsk_fk_cmpt_target2tgt_type 
	    foreign key (type_id) references rsk_computation_target_type (id),
	    
	constraint rsk_chk_uq_computation_target unique (type_id, id_scheme, id_value)
);

-------------------------------------
-- LiveData inputs
-------------------------------------

create table rsk_live_data_field (
	id int not null,
	name varchar(255) not null,
	
	primary key (id),
	
	constraint rsk_chk_uq_live_data_field unique (name)
);

create table rsk_live_data_snapshot (
	id int not null,
	observation_datetime_id int not null,
	
	primary key (id),
	
	constraint rsk_fk_lv_data_snap2ob_dttime
	    foreign key (observation_datetime_id) references rsk_observation_datetime (id),
	    
	constraint rsk_chk_uq_live_data_snapshot unique (observation_datetime_id)
);

create table rsk_live_data_snapshot_entry (
	id bigint not null,
	snapshot_id int not null,
	computation_target_id int not null,
	field_id int not null,
	value double precision,
	
	primary key (id),
	
	constraint rsk_fk_snpsht_entry2snpsht
		foreign key (snapshot_id) references rsk_live_data_snapshot (id),
	constraint rsk_fk_spsht_entry2cmp_target
	    foreign key (computation_target_id) references rsk_computation_target (id),
	    
	constraint rsk_chk_uq_snapshot_entry unique (snapshot_id, computation_target_id, field_id) 	
);

-------------------------------------
-- Risk run
-------------------------------------

create table rsk_run (
	id int not null,
	opengamma_version_id int not null,
	master_process_host_id int not null,    -- machine where 'master' batch process was started
    run_reason varchar(255) not null,       -- 15 June main overnight batch run
    run_time_id int not null,
    valuation_time timestamp not null,	 	-- 15 June 2010 17:00:00 - 'T'
    view_oid varchar(255) not null,
    view_version varchar(255),
    live_data_snapshot_id int not null,
    create_instant timestamp not null,
    start_instant timestamp not null,       -- can be different from create_instant if is run is restarted
    end_instant	timestamp,
    num_restarts int not null,
    complete boolean not null,
    
    primary key (id),
    
    constraint rsk_fk_run2opengamma_version
        foreign key (opengamma_version_id) references rsk_opengamma_version (id),
    constraint rsk_fk_run2compute_host
        foreign key (master_process_host_id) references rsk_compute_host (id),
    constraint rsk_fk_run2obs_datetime
        foreign key (run_time_id) references rsk_observation_datetime (id),
    constraint rsk_fk_run2live_data_snapshot
        foreign key (live_data_snapshot_id) references rsk_live_data_snapshot (id)
);

create table rsk_calculation_configuration (
	id int not null,
	run_id int not null,
	name varchar(255) not null,
	
	primary key (id),
	
	constraint rsk_fk_calc_conf2run
	    foreign key (run_id) references rsk_run (id),
	
	constraint rsk_chk_uq_calc_conf unique (run_id, name)
);

-- Properties should be filled once only. If already there, use existing value.
--
-- Example properties:
-- 	- PositionMasterTime = 20100615170000
--  - GlobalRandomSeed = 54321
create table rsk_run_property (		
	id int not null,
	run_id int not null,
	property_key varchar(255) not null,
	property_value varchar(2000) not null,		    -- varchar(255) not enough
	
	primary key (id),

	constraint rsk_fk_run_property2run 
	    foreign key (run_id) references rsk_run (id)
);

-- DBTOOLDONOTCLEAR
create table rsk_run_status_code (
    id int not null,	 	            
    name varchar(255) not null,
    
    primary key (id),
    
    constraint rsk_chk_rsk_run_status_code check
        ((id = 0 and name = 'SUCCESS') or
         (id = 1 and name = 'FAILURE') or 
         (id = 2 and name = 'RUNNING') or
         (id = 3 and name = 'NOT_RUNNING'))
);

insert into rsk_run_status_code (id, name) values (0, 'SUCCESS');
insert into rsk_run_status_code (id, name) values (1, 'FAILURE');
insert into rsk_run_status_code (id, name) values (2, 'RUNNING');
insert into rsk_run_status_code (id, name) values (3, 'NOT_RUNNING');

create table rsk_run_status (
    id bigint not null, 
    calculation_configuration_id int not null,
    computation_target_id int not null,
    status int not null,

    constraint rsk_fk_run_status2calc_conf
        foreign key (calculation_configuration_id) references rsk_calculation_configuration (id),
    constraint rsk_fk_run_status2comp_tgt
        foreign key (computation_target_id) references rsk_computation_target (id),
    constraint rsk_fk_run_status2code
        foreign key (status) references rsk_run_status_code (id),

    constraint rsk_chk_uq_run_status unique (calculation_configuration_id, computation_target_id)
);


-------------------------------------
-- Risk
-------------------------------------

create table rsk_value_name (
    id int not null,
    name varchar(255) not null,
    
    primary key (id),
    
    constraint rsk_chk_uq_value_name unique (name)
);

create table rsk_value (
    id bigint not null,
    calculation_configuration_id int not null,
    value_name_id int not null,                 
    computation_target_id int not null,        
    run_id int not null,             	       -- shortcut
    value double precision not null,
    eval_instant timestamp not null,
    compute_node_id int not null,
    
    primary key (id),
    
    -- performance implications of these constraints?
    constraint rsk_fk_value2calc_conf
        foreign key (calculation_configuration_id) references rsk_calculation_configuration (id),
    constraint rsk_fk_value2run 
        foreign key (run_id) references rsk_run (id),
    constraint rsk_fk_value2value_name
        foreign key (value_name_id) references rsk_value_name (id),
    constraint rsk_fk_value2comp_target
        foreign key (computation_target_id) references rsk_computation_target (id),
    constraint rsk_fk_value2compute_node
        foreign key (compute_node_id) references rsk_compute_node (id),
        
    constraint rsk_chk_uq_value unique (calculation_configuration_id, value_name_id, computation_target_id)
);


create table rsk_compute_failure (			
    id bigint not null,
    function_id varchar(255) not null,
    exception_class varchar(255) not null,
    exception_msg varchar(255) not null,                  
    stack_trace varchar(2000) not null,         -- first 2000 chars. not including msg
    
    primary key (id),
    
    constraint rsk_chk_uq_compute_failure unique (function_id, exception_class, exception_msg, stack_trace)
);

-- how to aggregate risk failures?
create table rsk_failure (			
    id bigint not null,
    calculation_configuration_id int not null,
    value_name_id int not null,                 
    computation_target_id int not null,
    run_id int not null,             	       -- shortcut
    eval_instant timestamp not null,
    compute_node_id int not null,
    
    primary key (id),
    
    constraint rsk_fk_failure2calc_conf 
        foreign key (calculation_configuration_id) references rsk_calculation_configuration (id),
    constraint rsk_fk_failure2run 
        foreign key (run_id) references rsk_run (id),
    constraint rsk_fk_failure2value_name
        foreign key (value_name_id) references rsk_value_name (id),
    constraint rsk_fk_failure2com_target
        foreign key (computation_target_id) references rsk_computation_target (id),
    constraint rsk_fk_failure2node
       foreign key (compute_node_id) references rsk_compute_node (id),
        
    constraint rsk_chk_uq_failure unique (calculation_configuration_id, value_name_id, computation_target_id)
);    

create table rsk_failure_reason (
   id bigint not null,
   rsk_failure_id bigint not null,
   compute_failure_id bigint not null,
   
   primary key (id),
   
   constraint rsk_fk_fail_reason2failure
       foreign key (rsk_failure_id) references rsk_failure (id)
       on delete cascade,
   constraint rsk_fk_fail_reason2cmpt_fail
       foreign key (compute_failure_id) references rsk_compute_failure (id),

   constraint rsk_chk_uq_failure_reason unique (rsk_failure_id, compute_failure_id)
);


-------------------------------------
-- Views
-------------------------------------

create view vw_rsk as
select
rsk_computation_target_type.name as comp_target_type,
rsk_computation_target.id_scheme as comp_target_id_scheme,
rsk_computation_target.id_value as comp_target_id_value,
rsk_run.id as rsk_run_id,
rsk_observation_datetime.date_part as run_date,
rsk_observation_time.label as run_time,
rsk_calculation_configuration.name as calc_conf_name,
rsk_value_name.name,
rsk_value.value, 
rsk_value.eval_instant
from 
rsk_value, 
rsk_calculation_configuration,
rsk_value_name,
rsk_computation_target,
rsk_computation_target_type,
rsk_run,
rsk_compute_node,
rsk_observation_datetime,
rsk_observation_time
where
rsk_value.calculation_configuration_id = rsk_calculation_configuration.id and
rsk_value.value_name_id = rsk_value_name.id and
rsk_value.computation_target_id = rsk_computation_target.id and
rsk_computation_target.type_id = rsk_computation_target_type.id and
rsk_value.run_id = rsk_run.id and
rsk_value.compute_node_id = rsk_compute_node.id and
rsk_run.run_time_id = rsk_observation_datetime.id and
rsk_observation_datetime.observation_time_id = rsk_observation_time.id;

create view vw_rsk_failure as
select
rsk_failure.id,
rsk_computation_target_type.name as comp_target_type,
rsk_computation_target.id_scheme as comp_target_id_scheme,
rsk_computation_target.id_value as comp_target_id_value,
rsk_run.id as rsk_run_id,
rsk_observation_datetime.date_part as run_date,
rsk_observation_time.label as run_time,
rsk_calculation_configuration.name as calc_conf_name,
rsk_value_name.name,
rsk_failure.eval_instant
from 
rsk_failure, 
rsk_calculation_configuration,
rsk_value_name,
rsk_computation_target,
rsk_computation_target_type,
rsk_run,
rsk_compute_node,
rsk_observation_datetime,
rsk_observation_time
where
rsk_failure.calculation_configuration_id = rsk_calculation_configuration.id and
rsk_failure.value_name_id = rsk_value_name.id and
rsk_failure.computation_target_id = rsk_computation_target.id and
rsk_computation_target.type_id = rsk_computation_target_type.id and
rsk_failure.run_id = rsk_run.id and
rsk_failure.compute_node_id = rsk_compute_node.id and
rsk_run.run_time_id = rsk_observation_datetime.id and
rsk_observation_datetime.observation_time_id = rsk_observation_time.id;

create view vw_rsk_failure_reason as
select
rsk_failure_reason.id,
rsk_failure_reason.rsk_failure_id,
rsk_compute_failure.function_id,
rsk_compute_failure.exception_class,
rsk_compute_failure.exception_msg,
rsk_compute_failure.stack_trace 
from 
rsk_failure_reason,
rsk_compute_failure
where
rsk_failure_reason.compute_failure_id = rsk_compute_failure.id;
CREATE TABLE tss_data_source (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
CREATE UNIQUE INDEX idx_data_source_name on tss_data_source(name);

CREATE TABLE tss_data_provider (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
CREATE UNIQUE INDEX idx_data_provider_name on tss_data_provider(name);

CREATE TABLE tss_data_field (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
CREATE UNIQUE INDEX idx_data_field_name on tss_data_field(name);

CREATE TABLE tss_observation_time (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
CREATE UNIQUE INDEX idx_observation_time_name on tss_observation_time(name);

CREATE TABLE tss_identification_scheme (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
CREATE UNIQUE INDEX idx_identification_scheme_name on tss_identification_scheme(name);

CREATE TABLE tss_identifier_bundle (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);
CREATE UNIQUE INDEX idx_identifier_bundle_name on tss_identifier_bundle(name);

CREATE TABLE tss_meta_data (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	active INTEGER NOT NULL
	  CONSTRAINT active_constraint CHECK ( active IN (0, 1)),
	bundle_id BIGINT NOT NULL
	  constraint fk_tsk_bundle  REFERENCES tss_identifier_bundle(id),
	data_source_id BIGINT NOT NULL
	  constraint fk_tsk_data_source  REFERENCES tss_data_source(id),
	data_provider_id BIGINT NOT NULL
	  constraint fk_tsk_data_provider  REFERENCES tss_data_provider(id),
	data_field_id BIGINT NOT NULL
	  constraint fk_tsk_data_field  REFERENCES tss_data_field(id),
	observation_time_id BIGINT NOT NULL
	  constraint fk_tsk_observation_time  REFERENCES tss_observation_time(id)
);
CREATE INDEX idx_meta_data ON tss_meta_data (active, data_source_id, data_provider_id, data_field_id, observation_time_id);

CREATE TABLE tss_data_point (
	meta_data_id BIGINT NOT NULL
	  constraint fk_dp_meta_data  REFERENCES tss_meta_data (id),
	ts_date date NOT NULL,
	value DOUBLE NOT NULL,
	PRIMARY KEY (meta_data_id, ts_date)
);

CREATE TABLE tss_data_point_delta (
	meta_data_id BIGINT NOT NULL
	  constraint fk_dp_delta_meta_data  REFERENCES tss_meta_data (id),
	time_stamp TIMESTAMP NOT NULL,
	ts_date date NOT NULL,
	old_value DOUBLE NOT NULL,
	operation char(1) NOT NULL
	 CONSTRAINT operation_constraint CHECK ( operation IN ('I', 'U', 'D', 'Q'))
);


CREATE TABLE tss_intraday_data_point (
	meta_data_id BIGINT NOT NULL
	  constraint fk_i_dp_meta_data  REFERENCES tss_meta_data (id),
	ts_date TIMESTAMP NOT NULL,
	value DOUBLE NOT NULL,
	PRIMARY KEY (meta_data_id, ts_date)
);

CREATE TABLE tss_intraday_data_point_delta (
	meta_data_id BIGINT NOT NULL
	  constraint fk_i_dp_delta_meta_data  REFERENCES tss_meta_data (id),
	time_stamp TIMESTAMP NOT NULL,
	ts_date TIMESTAMP NOT NULL,
	old_value DOUBLE NOT NULL,
	operation char(1) NOT NULL
	 CONSTRAINT operation_constraint_i CHECK ( operation IN ('I', 'U', 'D', 'Q'))
);

CREATE TABLE tss_identifier (
	id BIGINT
	  GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	bundle_id BIGINT NOT NULL
	  constraint fk_identifier_bundle  REFERENCES tss_identifier_bundle(id),
	identification_scheme_id BIGINT NOT NULL
	  constraint fk_identifier_identification_scheme  REFERENCES tss_identification_scheme(id),
	identifier_value VARCHAR(255) NOT NULL,
	valid_from date,
	valid_to date
);
CREATE INDEX idx_identifier_scheme_value on tss_identifier (identification_scheme_id, identifier_value);
CREATE INDEX idx_identifier_value ON tss_identifier(identifier_value);


