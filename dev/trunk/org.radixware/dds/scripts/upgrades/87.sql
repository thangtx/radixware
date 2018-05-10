alter table RDX_NETCHANNEL
	add CURBUSYSESSIONCOUNT NUMBER(9,0) default 0 not null
/

alter table RDX_NETCHANNEL
	add ISCURBUSYSESSIONCOUNTON NUMBER(1,0) default 0 not null
/

