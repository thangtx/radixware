create or replace trigger UPO_J5AGLYGB6RGKNJ36XBBX5AZ4ZA
after delete on RDX_TST_PARENT
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA', :old.ID);
end;
/

