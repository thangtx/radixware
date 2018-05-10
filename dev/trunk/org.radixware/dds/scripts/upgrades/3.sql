create table COUNTER(
	NAME VARCHAR2(100 char) not null,
	CLASSGUID VARCHAR2(100 char) null,
	VALUE_INCREMENT NUMBER(9,0) null,
	DEFAULTVALUE NUMBER(9,0) default 0 not null,
	VALUE NUMBER(9,0) default 0 not null,
	RESETPERIOD NUMBER(9,0) null,
	LASTUPDATETIME TIMESTAMP(6) null,
	LASTRESETTIME TIMESTAMP(6) null)
/

grant select, update, insert, delete on COUNTER to &USER&_RUN_ROLE
/

create unique index PK_COUNTER on COUNTER (NAME asc)
/

alter table COUNTER add constraint PK_COUNTER primary key (NAME) rely
/

create or replace trigger UPO_44TUT3JSR5BPTNBRF3PO2HF6OU
after delete on COUNTER
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl44TUT3JSR5BPTNBRF3PO2HF6OU', RDX_ENTITY.PackPIDStr(:old.NAME));
end;
/

