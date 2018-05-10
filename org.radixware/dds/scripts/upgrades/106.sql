alter table RDX_MESSAGEQUEUE
	add CIPHERSUITES VARCHAR2(4000 char) null
/

alter table RDX_MESSAGEQUEUE
	add CLIENTKEYALIASES VARCHAR2(4000 char) null
/

alter table RDX_MESSAGEQUEUE
	add SECURITYPROTOCOL NUMBER(9,0) default 0 not null
/

alter table RDX_MESSAGEQUEUE
	add SERVERCERTALIASES VARCHAR2(4000 char) null
/

create or replace trigger atdQZ2AJHN3PFDWXC5BT6I66OS5PQ
after delete on RDX_MESSAGEQUEUE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQZ2AJHN3PFDWXC5BT6I66OS5PQ'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colL6K3EEEXRBG4BEGHD54YVL5VVE',:old.SERVERCERTALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.col24OZ2N37RNEQPPFU36C2MKSK2A',:old.CLIENTKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colU44WLH4LFZFR5M4IQMPS5TIT7I',:old.CIPHERSUITES);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQZ2AJHN3PFDWXC5BT6I66OS5PQ',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQZ2AJHN3PFDWXC5BT6I66OS5PQ', 'D','tblQZ2AJHN3PFDWXC5BT6I66OS5PQ');
end;
/

create or replace trigger atiQZ2AJHN3PFDWXC5BT6I66OS5PQ
after insert on RDX_MESSAGEQUEUE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQZ2AJHN3PFDWXC5BT6I66OS5PQ'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colTTNAG76GNNHGFLD7J2IHJSMUSA',:new.SECURITYPROTOCOL);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQZ2AJHN3PFDWXC5BT6I66OS5PQ',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQZ2AJHN3PFDWXC5BT6I66OS5PQ', 'I','tblQZ2AJHN3PFDWXC5BT6I66OS5PQ');
end;
/

create or replace trigger atuQZ2AJHN3PFDWXC5BT6I66OS5PQ
after update of CIPHERSUITES, CLIENTKEYALIASES, SECURITYPROTOCOL, SERVERCERTALIASES on RDX_MESSAGEQUEUE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQZ2AJHN3PFDWXC5BT6I66OS5PQ'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTTNAG76GNNHGFLD7J2IHJSMUSA',:old.SECURITYPROTOCOL,:new.SECURITYPROTOCOL);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colL6K3EEEXRBG4BEGHD54YVL5VVE',:old.SERVERCERTALIASES,:new.SERVERCERTALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col24OZ2N37RNEQPPFU36C2MKSK2A',:old.CLIENTKEYALIASES,:new.CLIENTKEYALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colU44WLH4LFZFR5M4IQMPS5TIT7I',:old.CIPHERSUITES,:new.CIPHERSUITES);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQZ2AJHN3PFDWXC5BT6I66OS5PQ',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQZ2AJHN3PFDWXC5BT6I66OS5PQ', 'U','tblQZ2AJHN3PFDWXC5BT6I66OS5PQ');
end;
/

