begin
    execute immediate 'drop index IDX_RDX_SM_STACKDATA_LASTUSAGE';
exception when others then
    if SQLCODE = -01418 then
        null; -- suppresses ORA-01418 'specified index does not exist'
    else
        raise;
    end if;
end;
/
