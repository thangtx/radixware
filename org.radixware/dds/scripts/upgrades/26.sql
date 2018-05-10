alter table RDX_PC_EVENTSUBSCRIPTION
	add EXTGUID VARCHAR2(50 char)
/

update RDX_PC_EVENTSUBSCRIPTION set EXTGUID = sys_guid() where EXTGUID is null
/

alter table RDX_PC_EVENTSUBSCRIPTION
	modify (EXTGUID VARCHAR2(50 char) not null)
/        