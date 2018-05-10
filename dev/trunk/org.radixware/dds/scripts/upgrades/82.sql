alter table RDX_INSTANCE
	add AADCDGADDRESS VARCHAR2(100 char) null
/

alter table RDX_INSTANCE
	add AADCMEMBERID NUMBER(1,0) null
/

alter table RDX_SYSTEM
	add AADCMEMBERID NUMBER(1,0) null
/

alter table RDX_INSTANCE
	add AADCMYSCN NUMBER(18,0) null
/

alter table RDX_INSTANCE
	add AADCMYTIME TIMESTAMP(6) null
/

