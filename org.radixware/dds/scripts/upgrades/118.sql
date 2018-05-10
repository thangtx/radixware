create global temporary table RDX_EVENTCODEMLS(
	VERSIONNUM NUMBER(18,0) not null,
	BUNDLEID VARCHAR2(100 char) not null,
	STRINGID VARCHAR2(100 char) not null,
	LANGUAGE VARCHAR2(100 char) not null,
	STRINGVALUE VARCHAR2(4000 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_EVENTCODEMLS to &USER&_RUN_ROLE
/

create unique index PK_RDX_EVENTCODEMLS on RDX_EVENTCODEMLS (VERSIONNUM asc, BUNDLEID asc, STRINGID asc, LANGUAGE asc)
/

alter table RDX_EVENTCODEMLS add constraint PK_RDX_EVENTCODEMLS primary key (VERSIONNUM, BUNDLEID, STRINGID, LANGUAGE) rely
/

