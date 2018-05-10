begin
    execute immediate 'alter table RDX_JS_INTERVALSCHD add RID VARCHAR2(100 char) null';
exception when others then
    if SQLCODE = -01430 then
        null; -- suppresses ORA-01430 'column being added already exists in table'
      else
         raise;
    end if;
end;
/
begin
    execute immediate 'alter table RDX_JS_EVENTSCHD add RID VARCHAR2(100 char) null';
exception when others then
    if SQLCODE = -01430 then
        null; -- suppresses ORA-01430 'column being added already exists in table'
      else
         raise;
    end if;
end;
/





