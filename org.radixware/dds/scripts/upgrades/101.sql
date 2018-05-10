connect SYS/&SYS_PASSWORD& as sysdba
/

grant execute on DBMS_STREAMS_ADM to &USER&
/
grant execute on DBMS_STREAMS_ADM to &USER&_RUN_ROLE
/

connect &USER&/&PASSWORD&
/
