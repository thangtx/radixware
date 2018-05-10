alter table RDX_PC_OUTMESSAGE
	add ISUSSD NUMBER(1,0) null
/

alter table RDX_PC_SENTMESSAGE
	add ISUSSD NUMBER(1,0) null
/

alter table RDX_PC_OUTMESSAGE
	add USSDSERVICEOP NUMBER(9,0) null
/

alter table RDX_PC_SENTMESSAGE
	add USSDSERVICEOP NUMBER(9,0) null
/

