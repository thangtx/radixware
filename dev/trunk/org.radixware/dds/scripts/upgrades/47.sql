create unique index IDX_RDX_JS_TASK_RID on RDX_JS_TASK (RID asc)
/

alter table RDX_JS_TASK add constraint UNQ_RDX_JS_TASK_RID unique (RID) rely
/

