drop trigger atu5HP4XTP3EGWDBRCRAAIT4AGD7E
/

drop trigger ati5HP4XTP3EGWDBRCRAAIT4AGD7E
/

alter table RDX_SYSTEM
	add AADCTESTEDMEMBERID NUMBER(1,0) null
/

alter table RDX_UNIT
	add AADCTESTMODE NUMBER(1,0) default 0 not null
/

create or replace trigger ati5HP4XTP3EGWDBRCRAAIT4AGD7E
after insert on RDX_UNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colEVX5YH2SS5VDBN2IAAUMFADAIA',:new.USE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colTYUCIQQYTZCDVDV7MJQVDW2M5A',:new.AADCTESTMODE);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu5HP4XTP3EGWDBRCRAAIT4AGD7E
after update of AADCTESTMODE, USE on RDX_UNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colEVX5YH2SS5VDBN2IAAUMFADAIA',:old.USE,:new.USE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTYUCIQQYTZCDVDV7MJQVDW2M5A',:old.AADCTESTMODE,:new.AADCTESTMODE);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

