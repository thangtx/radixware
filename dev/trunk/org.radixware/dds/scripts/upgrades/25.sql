begin
    execute immediate 'alter table RDX_CM_PACKET modify (LASTMODIFYUSER null)';
exception when others then
    if SQLCODE = -01451 THEN
        NULL; -- suppresses ORA-01451 'column to be modified to NULL cannot be modified to NULL'
      ELSE
         RAISE;
    END IF;
end;
/



