begin
  execute IMMEDIATE 'alter table RDX_PC_OUTMESSAGE add MASKEDBODY CLOB null';
exception when others then
    if SQLCODE = -01430 THEN
        NULL; -- suppresses ORA-01430 'column being added already exists in table'
      ELSE
         RAISE;
    END IF;
end;
/

begin
  execute IMMEDIATE 'alter table RDX_PC_SENTMESSAGE add MASKEDBODY CLOB null';
exception when others then
    if SQLCODE = -01430 THEN
        NULL; -- suppresses ORA-01430 'column being added already exists in table'
      ELSE
         RAISE;
    END IF;
end;
/