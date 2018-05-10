alter table RDX_JS_TASK
	add DIRECTORYID NUMBER(9,0) null
/

alter table RDX_JS_TASK
	add constraint FK_RDX_JS_TASK_DIR foreign key (DIRECTORYID)
	references RDX_JS_TASK (ID) on delete cascade
/

