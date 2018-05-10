alter table RDX_SYSTEM
	add AADCAFFINITYTIMEOUTSEC NUMBER(9,0) default 60 not null
/


begin
    execute immediate 'ALTER PACKAGE RDX_AUDIT COMPILE BODY';
end;
/

connect SYS/&SYS_PASSWORD& as sysdba
/

grant execute on DBMS_SCHEDULER to &USER&
/

grant execute on DBMS_SCHEDULER to &USER&_RUN_ROLE
/

grant CREATE ANY JOB to &USER&
/
grant CREATE ANY JOB to &USER&_RUN_ROLE
/

connect &USER&/&PASSWORD&
/