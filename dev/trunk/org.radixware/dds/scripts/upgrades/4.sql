drop trigger UPO_44TUT3JSR5BPTNBRF3PO2HF6OU
/

alter table COUNTER drop constraint PK_COUNTER cascade
/

drop index PK_COUNTER
/

create sequence SQN_COUNTERID
	order
/

grant select on SQN_COUNTERID to &USER&_RUN_ROLE
/

alter table COUNTER add TMP_ANYTYPE_TO_ANYTYPE NUMBER(18,0) null
/

update COUNTER set TMP_ANYTYPE_TO_ANYTYPE = NAME -- modify this query for correct types conversion
/

alter table COUNTER drop column NAME
/

alter table COUNTER rename column TMP_ANYTYPE_TO_ANYTYPE to ID
/

alter table COUNTER modify ( ID not null)
/

alter table COUNTER
	add UPDEFID VARCHAR2(100 char) not null
/

alter table COUNTER
	add UPOWNERENTITYID VARCHAR2(100 char) not null
/

alter table COUNTER
	add UPOWNERPID VARCHAR2(200 char) not null
/

create unique index IDX_COUNTER_USERPROPERTYOBJECT on COUNTER (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc)
/

create unique index PK_COUNTER on COUNTER (ID asc)
/

alter table COUNTER add constraint PK_COUNTER primary key (ID) rely
/

alter table COUNTER
	add constraint FK_COUNTER_UPVALREF foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID)
	references RDX_UPVALREF (DEFID, OWNERENTITYID, OWNERPID) on delete cascade
/

create or replace trigger UPF_44TUT3JSR5BPTNBRF3PO2HF6OU
after delete on COUNTER
begin
   RDX_ENTITY.flushUserPropOnDelOwner('tbl44TUT3JSR5BPTNBRF3PO2HF6OU');
end;
/

create or replace trigger UPS_44TUT3JSR5BPTNBRF3PO2HF6OU
after delete on COUNTER
for each row
begin
   RDX_ENTITY.ScheduleUserPropOnDelOwner('tbl44TUT3JSR5BPTNBRF3PO2HF6OU', :old.ID);
end;
/

