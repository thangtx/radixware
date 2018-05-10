drop trigger atuWZB7K4HLJPOBDCIUAALOMT5GDM
/

drop trigger atiWZB7K4HLJPOBDCIUAALOMT5GDM
/

alter table RDX_JS_TASK
	add AADCMEMBERID NUMBER(1,0) null
/

alter table RDX_JS_JOBQUEUE
	add AADCMEMBERID NUMBER(1,0) null
/

create or replace trigger atiWZB7K4HLJPOBDCIUAALOMT5GDM
after insert on RDX_JS_TASK
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblWZB7K4HLJPOBDCIUAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.col4W7CE76W7VATBNLH5FLEE3FAVA',:new.AADCMEMBERID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colNAPRJRHLJPOBDCIUAALOMT5GDM',:new.SCHEDULEID);
            RDX_AUDIT.addValueStr(vChangeData,'new.colFDCRWKPMJPOBDCIUAALOMT5GDM',:new.CLASSGUID);
            RDX_AUDIT.addValueInt(vChangeData,'new.col7JDCGRXMJPOBDCIUAALOMT5GDM',:new.PRIORITY);
            RDX_AUDIT.addValueInt(vChangeData,'new.col33LE4YHMJPOBDCIUAALOMT5GDM',:new.PRIORITYBOOSTINGSPEED);
            RDX_AUDIT.addValueInt(vChangeData,'new.colYQWRAN7SK5GA3EGFI6TEPHYWL4',:new.EXPIREDPOLICY);
            RDX_AUDIT.addValueInt(vChangeData,'new.colRMH5P2FSTNABBMBNUIXU3AINEQ',:new.ISACTIVE);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblWZB7K4HLJPOBDCIUAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblWZB7K4HLJPOBDCIUAALOMT5GDM', 'I','tblWZB7K4HLJPOBDCIUAALOMT5GDM');
end;
/

create or replace trigger atuWZB7K4HLJPOBDCIUAALOMT5GDM
after update of AADCMEMBERID, CLASSGUID, EXPIREDPOLICY, ISACTIVE, PRIORITY, PRIORITYBOOSTINGSPEED, SCHEDULEID on RDX_JS_TASK
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblWZB7K4HLJPOBDCIUAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'col4W7CE76W7VATBNLH5FLEE3FAVA',:old.AADCMEMBERID,:new.AADCMEMBERID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colNAPRJRHLJPOBDCIUAALOMT5GDM',:old.SCHEDULEID,:new.SCHEDULEID);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colFDCRWKPMJPOBDCIUAALOMT5GDM',:old.CLASSGUID,:new.CLASSGUID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col7JDCGRXMJPOBDCIUAALOMT5GDM',:old.PRIORITY,:new.PRIORITY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col33LE4YHMJPOBDCIUAALOMT5GDM',:old.PRIORITYBOOSTINGSPEED,:new.PRIORITYBOOSTINGSPEED);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colYQWRAN7SK5GA3EGFI6TEPHYWL4',:old.EXPIREDPOLICY,:new.EXPIREDPOLICY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colRMH5P2FSTNABBMBNUIXU3AINEQ',:old.ISACTIVE,:new.ISACTIVE);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblWZB7K4HLJPOBDCIUAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblWZB7K4HLJPOBDCIUAALOMT5GDM', 'U','tblWZB7K4HLJPOBDCIUAALOMT5GDM');
end;
/

