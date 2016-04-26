connect SYS/&SYS_PASSWORD& as sysdba
/

create user &USER& identified by &PASSWORD&
   default tablespace RADIX
   quota unlimited on RADIX
/
alter profile default limit PASSWORD_LIFE_TIME unlimited
/

grant create procedure      to &USER&
/
grant create sequence       to &USER&
/
grant create session        to &USER& 
/
grant create synonym        to &USER& 
/
grant create table          to &USER&
/
grant create trigger        to &USER&
/
grant create view           to &USER&
/
grant alter session         to &USER& 
/
grant create database link to &USER& 
/
grant create materialized view to &USER& 
/

-- JAVA debug
grant execute on SYS.DBMS_DEBUG_JDWP to &USER& 
/
grant DEBUG CONNECT SESSION to &USER& 
/
grant DEBUG ANY PROCEDURE to &USER& 
/

grant SELECT on SYS.V_$PARAMETER to &USER& 
/
grant SELECT on SYS.V_$SESSION to &USER&
/
grant SELECT on SYS.V_$process to &USER&
/
grant SELECT on SYS.dba_role_privs to &USER&
/
grant execute on DBMS_LOCK to &USER& 
/
grant execute on dbms_lob to &USER&
/
grant execute on DBMS_SQL to &USER&
/
grant execute on dbms_random to &USER&
/
grant alter system to &USER&
/
grant select on dbms_lock_allocated to &USER&
/
grant select on dba_lock to &USER&
/

-- SERVER RUN ROLE 
create role &USER&_RUN_ROLE
/
grant create session to &USER&_RUN_ROLE
/
grant alter session to &USER&_RUN_ROLE
/
grant SELECT on SYS.V_$SESSION to &USER&_RUN_ROLE
/
grant SELECT on SYS.V_$PROCESS to &USER&_RUN_ROLE
/
grant create type to &USER&_RUN_ROLE
/
grant execute on DBMS_LOCK to &USER&_RUN_ROLE 
/
grant execute on DBMS_LOB to &USER&_RUN_ROLE
/
grant execute on DBMS_SQL to &USER&_RUN_ROLE
/
grant execute on DBMS_RANDOM to &USER&_RUN_ROLE
/
grant alter system to &USER&_RUN_ROLE
/
grant select on dbms_lock_allocated to &USER&_RUN_ROLE
/
grant select on dba_lock to &USER&_RUN_ROLE
/


grant &USER&_RUN_ROLE to &USER&
/

connect &USER&/&PASSWORD&
/


create table RDX_DDSVERSION(
	LAYERURI VARCHAR2(100) not null,
	VERSION VARCHAR2(100) not null,
        UPGRADETOVERSION  VARCHAR2(100) null,
        UPGRADESTARTTIME DATE null,
        PREVCOMPATIBLEVER VARCHAR2(100) null,
	UPGRADEDATE DATE not null,
	LASTFILENAME VARCHAR2(100 char) null)
/

create table RDX_DDSUPGRADELOG(
	LAYERURI VARCHAR2(100 char) not null,
	FROMRELEASE VARCHAR2(100 char) not null,
	TORELEASE VARCHAR2(100 char) not null,
	FILENAME VARCHAR2(1000 char) not null,
	PROBLEMGUID VARCHAR2(50 char) null,
	UPGRADEDATE DATE not null)
/

create table RDX_DDSOPTION(
    LAYERURI VARCHAR2(100 BYTE) NOT NULL,
    NAME     VARCHAR2(100 BYTE) NOT NULL,
    VALUE    VARCHAR2(500 BYTE),
    CONSTRAINT RDX_DDSOPTION_PK_LAYERURI_NAME PRIMARY KEY(LAYERURI, NAME)
)
/


grant SELECT on RDX_DDSVERSION to &USER&_RUN_ROLE
/
grant SELECT on RDX_DDSUPGRADELOG to &USER&_RUN_ROLE
/
grant SELECT on RDX_DDSOPTION to &USER&_RUN_ROLE
/