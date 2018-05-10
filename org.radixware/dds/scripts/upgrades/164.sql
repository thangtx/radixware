create or replace trigger UPO_WMN3P5J5ONCFPLJVGBFAKZXZXU
after delete on RDX_CM_PACKET
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblWMN3P5J5ONCFPLJVGBFAKZXZXU', :old.ID);
end;
/

