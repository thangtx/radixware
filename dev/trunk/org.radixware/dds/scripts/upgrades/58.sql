update rdx_unit u set u.primaryUnitId = (select f.parentId from RDX_JS_JOBSCHEDULERUNIT f where f.id = u.id)
where exists (select 1 from RDX_JS_JOBSCHEDULERUNIT f where f.id = u.id)
/

drop table RDX_JS_JOBSCHEDULERUNIT cascade constraints
/

