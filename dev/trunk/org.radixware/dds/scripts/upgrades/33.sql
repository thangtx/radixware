create or replace trigger UPF_J6SOXKD3ZHOBDCMTAALOMT5GDM
after delete on RDX_USERFUNC
begin
   RDX_ENTITY.flushUserPropOnDelOwner('tblJ6SOXKD3ZHOBDCMTAALOMT5GDM');
end;
/

create or replace trigger UPO_M7J46MP6F3PBDIJEABQAQH3XQ4
after delete on RDX_AC_APPROLE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblM7J46MP6F3PBDIJEABQAQH3XQ4', RDX_ENTITY.PackPIDStr(:old.GUID));
end;
/

create or replace trigger UPO_RHH7SYO5I5EFRGYIBOSVUXKD7U
after delete on RDX_USERREPORT
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblRHH7SYO5I5EFRGYIBOSVUXKD7U', RDX_ENTITY.PackPIDStr(:old.GUID));
end;
/

create or replace trigger UPO_X6WI554KVVCZPGNLK7CSZLOMTY
after delete on RDX_SB_PIPELINE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblX6WI554KVVCZPGNLK7CSZLOMTY', :old.ID);
end;
/

create or replace trigger UPS_J6SOXKD3ZHOBDCMTAALOMT5GDM
after delete on RDX_USERFUNC
for each row
begin
   RDX_ENTITY.ScheduleUserPropOnDelOwner('tblJ6SOXKD3ZHOBDCMTAALOMT5GDM', RDX_ENTITY.PackPIDStr(:old.UPDEFID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERENTITYID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERPID));
end;
/

