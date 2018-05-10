drop trigger TBUR_RDX_AC_USER
/

create or replace trigger TBUR_RDX_AC_USER
before update of PWDHASH on RDX_AC_USER
for each row
begin
    :new.LASTPWDCHANGETIME := sysdate;
    if not UPDATING ('MUSTCHANGEPWD') then
        :new.MUSTCHANGEPWD := 0;
    end if;
end;
/

