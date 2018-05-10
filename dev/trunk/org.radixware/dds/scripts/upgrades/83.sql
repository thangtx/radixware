connect SYS/&SYS_PASSWORD& as sysdba
/

grant execute on DBMS_TRANSACTION to &USER&
/
grant SELECT on GV_$TRANSACTION to &USER& 
/
grant SELECT on GV_$DATABASE to &USER& 
/

grant execute on DBMS_TRANSACTION to &USER&_RUN_ROLE
/
grant SELECT on GV_$TRANSACTION to &USER&_RUN_ROLE
/
grant SELECT on GV_$DATABASE to &USER&_RUN_ROLE
/

connect &USER&/&PASSWORD&
/
