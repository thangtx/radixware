update rdx_unit u set u.primaryUnitId = (select f.mainunitid from RDX_FALLBACKMQHANDLER f where f.unitid = u.id)
where exists (select 1 from RDX_FALLBACKMQHANDLER f where f.unitId = u.id)
/


drop table RDX_FALLBACKMQHANDLER cascade constraints
/

