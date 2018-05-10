alter table RDX_NETCHANNEL
	add RID VARCHAR2(100 char) null
/

alter table RDX_UNIT
	add RID VARCHAR2(100 char) null
/

create index IDX_RDX_NETCHANNEL_RID on RDX_NETCHANNEL (UNITID asc, RID asc)
/

create index IDX_RDX_UNIT_RID on RDX_UNIT (RID asc)
/

alter table RDX_UNIT add constraint UNQ_RDX_UNIT_RID unique (RID) rely
/

