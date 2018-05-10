alter table RDX_AU_AUDITLOG drop constraint PK_RDX_AU_AUDITLOG cascade
/

drop index PK_RDX_AU_AUDITLOG
/

alter table RDX_AU_AUDITLOG
	modify (ID NUMBER(18,0))
/

create unique index PK_RDX_AU_AUDITLOG on RDX_AU_AUDITLOG (EVENTTIME asc, ID asc, STOREDURATION asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

alter table RDX_AU_AUDITLOG add constraint PK_RDX_AU_AUDITLOG primary key (EVENTTIME, ID, STOREDURATION) rely
/

