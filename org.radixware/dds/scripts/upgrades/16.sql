begin
    execute immediate 'alter table RDX_TESTCASE add EXTGUID VARCHAR2(100 char) not null';
exception
    when OTHERS then
      IF SQLCODE = -1430 THEN
        NULL; -- suppresses ORA-01430 'column being added already exists in table'
      ELSE
         RAISE;
      END IF;
end;
/
update RDX_TESTCASE 
        set EXTGUID = rawtohex(sys_guid()) 
        where EXTGUID is null
/
begin
    execute immediate 'alter table RDX_TESTCASE modify EXTGUID not null';
exception
    when OTHERS then
      IF SQLCODE = -01442 THEN
        NULL; -- suppresses ORA-01442 'column to be modified to NOT NULL is already NOT NULL'
      ELSE
         RAISE;
      END IF;
end;
/