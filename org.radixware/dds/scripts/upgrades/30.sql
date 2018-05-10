create table RDX_CM_CHANGELOG(
	UPDEFID VARCHAR2(100 char) not null,
	UPOWNERENTITYID VARCHAR2(100 char) not null,
	UPOWNERPID VARCHAR2(200 char) not null,
	COMMENTS VARCHAR2(2000 char) null,
	LOCALNOTES VARCHAR2(2000 char) null)
/

grant select, update, insert, delete on RDX_CM_CHANGELOG to &USER&_RUN_ROLE
/

create table RDX_CM_REVISION(
	UPDEFID VARCHAR2(100 char) not null,
	UPOWNERENTITYID VARCHAR2(100 char) not null,
	UPOWNERPID VARCHAR2(200 char) not null,
	SEQ NUMBER(9,0) not null,
	TIME DATE not null,
	DESCRIPTION VARCHAR2(2000 char) not null,
	AUTHOR VARCHAR2(250 char) null,
	DOCREF VARCHAR2(200 char) null,
	APPVER VARCHAR2(1000 char) null,
	LOCALNOTES VARCHAR2(2000 char) null)
/

grant select, update, insert, delete on RDX_CM_REVISION to &USER&_RUN_ROLE
/

create unique index PK_RDX_CM_CHANGELOG on RDX_CM_CHANGELOG (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc)
/

create unique index PK_RDX_CM_REVISION on RDX_CM_REVISION (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc, SEQ asc)
/

alter table RDX_CM_CHANGELOG add constraint PK_RDX_CM_CHANGELOG primary key (UPDEFID, UPOWNERENTITYID, UPOWNERPID) rely
/

alter table RDX_CM_REVISION add constraint PK_RDX_CM_REVISION primary key (UPDEFID, UPOWNERENTITYID, UPOWNERPID, SEQ) rely
/

alter table RDX_CM_CHANGELOG
	add constraint FK_RDX_CM_CHANGELOG_UPVALREF foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID)
	references RDX_UPVALREF (DEFID, OWNERENTITYID, OWNERPID) on delete cascade
/

alter table RDX_CM_REVISION
	add constraint FK_RDX_CM_REVISION_CHANGELOG foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID)
	references RDX_CM_CHANGELOG (UPDEFID, UPOWNERENTITYID, UPOWNERPID) on delete cascade
/

