create or replace trigger UPO_M2NL42YXRRA5ZH27LCKIW5CQNI
after delete on RDX_USERREPORTVERSION
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblM2NL42YXRRA5ZH27LCKIW5CQNI', RDX_ENTITY.PackPIDStr(:old.REPORTGUID)||'~'||:old.VERSION);
end;
/

