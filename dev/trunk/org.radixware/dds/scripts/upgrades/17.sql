begin
execute immediate 'alter table RDX_AC_USER modify (INVALIDLOGONTIME null)';
exception when others then
    if SQLCODE = -01451 THEN
        NULL; -- suppresses ORA-01451 'column to be modified to NULL cannot be modified to NULL'
      ELSE
         RAISE;
    END IF;
end;
/

