begin
    execute immediate 'drop index IDX_RDX_SAP_NAMEINSYSTEM';
    execute immediate 'create index IDX_RDX_SAP_NAMEINSYSTEM on RDX_SAP (SYSTEMID asc, URI asc, NAME asc)';
exception when OTHERS then
    null;
end;
/
begin
    execute immediate 'alter table RDX_AADC_OGGERROR add ID NUMBER(18,0) null';
exception when OTHERS then
    null;
end;
/

create or replace trigger TBIR_RDX_AADC_OGGERROR
before insert on RDX_AADC_OGGERROR
for each row
begin
  SELECT SQN_RDX_EVENTLOGID.NEXTVAL INTO :new.id FROM dual;
end;
/


begin
    execute immediate 'alter table RDX_AADC_OGGERROR modify (FILERBA null)';
    execute immediate 'alter table RDX_AADC_OGGERROR modify (FILESEQNO null)';
    execute immediate 'update RDX_AADC_OGGERROR set id = SQN_RDX_EVENTLOGID.nextval';
    execute immediate 'ALTER TABLE RDX_AADC_OGGERROR MODIFY (ID  NOT NULL)';
exception when OTHERS then
    null;
end;
/


alter table RDX_AADC_OGGERROR drop constraint PK_RDX_AADC_OGGERROR cascade
/
drop index PK_RDX_AADC_OGGERROR
/
create unique index PK_RDX_AADC_OGGERROR on RDX_AADC_OGGERROR (ID asc)
/
alter table RDX_AADC_OGGERROR add constraint PK_RDX_AADC_OGGERROR primary key (ID) rely
/

begin
    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
        trig_owner => sys_context( 'userenv', 'current_schema' ), 
        trig_name  => 'TAIR_RDX_AADC_OGGERROR',
        fire_once  => false);
end;
/



