drop trigger TADR_RDX_PC_OUTMESSAGE
/

alter table RDX_PC_OUTMESSAGE
	add STOREATTACHINHIST NUMBER(1,0) default 1 not null
/

alter table RDX_PC_SENTMESSAGE
	add STOREATTACHINHIST NUMBER(1,0) null
/

create or replace trigger TADR_RDX_PC_OUTMESSAGE
after delete on RDX_PC_OUTMESSAGE
for each row
Begin
  if :old.HISTMODE = 0 or :old.STOREATTACHINHIST = 0 then
    Delete from RDX_PC_ATTACHMENT where RDX_PC_ATTACHMENT.MESSID=:old.ID;
  end if;
End;
/

