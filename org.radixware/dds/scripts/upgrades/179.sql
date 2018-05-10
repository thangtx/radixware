create sequence SQN_RDX_AC_PARTITIONGROUPID
	increment by 1
	start with 1
	minvalue 1
	order
/

begin
	RDX_Aadc.afterSequenceDdl('SQN_RDX_AC_PARTITIONGROUPID');
end;
/

grant select on SQN_RDX_AC_PARTITIONGROUPID to &USER&_RUN_ROLE
/

create table RDX_AC_PARTITIONGROUP(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(250 char) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	PARTITIONS CLOB null)
/

grant select, update, insert, delete on RDX_AC_PARTITIONGROUP to &USER&_RUN_ROLE
/

alter table RDX_AC_USER2ROLE
	add PG$$1ZOQHCO35XORDCV2AANE2UAFXA NUMBER(9,0) null
/

alter table RDX_AC_USERGROUP2ROLE
	add PG$$1ZOQHCO35XORDCV2AANE2UAFXA NUMBER(9,0) null
/

alter table RDX_AC_USER2ROLE
	add PG$$4PQ4U65VK5HFVJ32XCUORBKRJM NUMBER(9,0) null
/

alter table RDX_AC_USERGROUP2ROLE
	add PG$$4PQ4U65VK5HFVJ32XCUORBKRJM NUMBER(9,0) null
/

create unique index PK_RDX_AC_PARTITIONGROUP on RDX_AC_PARTITIONGROUP (ID asc)
/

alter table RDX_AC_PARTITIONGROUP add constraint PK_RDX_AC_PARTITIONGROUP primary key (ID) rely
/

alter table RDX_AC_USERGROUP2ROLE
	add constraint FK_RDX_GRP2ROLE_PARTGROUP_DASH foreign key (PG$$4PQ4U65VK5HFVJ32XCUORBKRJM)
	references RDX_AC_PARTITIONGROUP (ID) on delete cascade
/

alter table RDX_AC_USERGROUP2ROLE
	add constraint FK_RDX_GRP2ROLE_PARTGROUP_USGR foreign key (PG$$1ZOQHCO35XORDCV2AANE2UAFXA)
	references RDX_AC_PARTITIONGROUP (ID) on delete cascade
/

alter table RDX_AC_USER2ROLE
	add constraint FK_RDX_USR2ROLE_PARTGROUP_DASH foreign key (PG$$4PQ4U65VK5HFVJ32XCUORBKRJM)
	references RDX_AC_PARTITIONGROUP (ID) on delete cascade
/

alter table RDX_AC_USER2ROLE
	add constraint FK_RDX_USR2ROLE_PARTGROUP_USGR foreign key (PG$$1ZOQHCO35XORDCV2AANE2UAFXA)
	references RDX_AC_PARTITIONGROUP (ID) on delete cascade
/


