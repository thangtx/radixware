rename SQN_COUNTERID to SQN_RDX_COUNTERID
/

grant select on SQN_RDX_COUNTERID to &USER&_RUN_ROLE
/

rename COUNTER to RDX_COUNTER
/

grant select, update, insert, delete on RDX_COUNTER to &USER&_RUN_ROLE
/

alter index IDX_COUNTER_USERPROPERTYOBJECT rename to IDX_RDX_COUNTER_USERPROPERTYOB
/

alter index PK_COUNTER rename to PK_RDX_COUNTER
/

alter table RDX_COUNTER rename constraint PK_COUNTER to PK_RDX_COUNTER
/

alter table RDX_COUNTER rename constraint FK_COUNTER_UPVALREF to FK_RDX_COUNTER_UPVALREF
/

