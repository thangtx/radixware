begin
execute immediate 'alter table RDX_DDSVERSION add revision NUMBER(12,0) null';
exception when others then
    if sqlcode = -01430 then
        null;
    else
        raise;
    end if;
end;
/

begin
execute immediate 'alter table RDX_DDSVERSION add upgradeToRevision NUMBER(12,0) null';
exception when others then
    if sqlcode = -01430 then
        null;
    else
        raise;
    end if;
end;
/ 
