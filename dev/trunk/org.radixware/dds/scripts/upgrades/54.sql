alter table RDX_UNIT
	add PRIMARYUNITID NUMBER(9,0) null
/

alter table RDX_UNIT
	add constraint FK_RDX_UNIT_UNIT foreign key (PRIMARYUNITID)
	references RDX_UNIT (ID) on delete cascade
/

