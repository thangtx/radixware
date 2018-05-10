create sequence SQN_RDX_PC_NOTIFICATIONSENDERI
	order
/

begin
	RDX_Aadc.afterSequenceDdl('SQN_RDX_PC_NOTIFICATIONSENDERI');
end;
/

grant select on SQN_RDX_PC_NOTIFICATIONSENDERI to &USER&_RUN_ROLE
/

create table RDX_PC_NOTIFICATIONSENDER(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(250 char) null,
	RID VARCHAR2(100 char) null,
	EXTGUID VARCHAR2(100 char) not null,
	CHANNELKIND VARCHAR2(100 char) null,
	ROUTINGKEY VARCHAR2(100 char) null,
	HISTMODE NUMBER(9,0) not null,
	STOREATTACHINHIST NUMBER(1,0) not null,
	SUBJECTTEMPLATE VARCHAR2(4000 char) null,
	BODYTEMPLATE VARCHAR2(4000 char) null,
	USERGROUPNAME VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_PC_NOTIFICATIONSENDER to &USER&_RUN_ROLE
/

create unique index PK_RDX_PC_NOTIFICATIONSENDER on RDX_PC_NOTIFICATIONSENDER (ID asc)
/

alter table RDX_PC_NOTIFICATIONSENDER add constraint PK_RDX_PC_NOTIFICATIONSENDER primary key (ID) rely
/

alter table RDX_PC_NOTIFICATIONSENDER
	add constraint FK_RDX_PC_NOTIFICATIONSENDER_U foreign key (USERGROUPNAME)
	references RDX_AC_USERGROUP (NAME) on delete set null
/


create or replace trigger UPO_VKKXVT2NARDUNGRKAOMYB6VWXI
after delete on RDX_PC_NOTIFICATIONSENDER
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblVKKXVT2NARDUNGRKAOMYB6VWXI', :old.ID);
end;
/


create or replace trigger atdVKKXVT2NARDUNGRKAOMYB6VWXI
after delete on RDX_PC_NOTIFICATIONSENDER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT_VARS.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblVKKXVT2NARDUNGRKAOMYB6VWXI'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colPH5JTQND3VDZTJJ7Q46PRKF7JE',:old.CHANNELKIND);
            RDX_AUDIT.addValueStr(vChangeData,'old.colTGS3PTFC5RGSLAFMQFUSXEJUOI',:old.ROUTINGKEY);
            RDX_AUDIT.addValueInt(vChangeData,'old.colEI7GIMO4KFAPRAU4HAVU6BQ3PI',:old.HISTMODE);
            RDX_AUDIT.addValueInt(vChangeData,'old.colHDP4Q3IHNRGLZFFT6IRB3M3G24',:old.STOREATTACHINHIST);
            RDX_AUDIT.addValueStr(vChangeData,'old.colYRJIL7ZCM5AK5LDULILF6B2FDA',:old.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colLB4DFGLQ7RD6TK65SWSARQVKGA',:old.BODYTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colXECQRSZBN5F27GJKI242EGT7NA',:old.USERGROUPNAME);
   end if;

   vClassGuid := 'aecVKKXVT2NARDUNGRKAOMYB6VWXI';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblVKKXVT2NARDUNGRKAOMYB6VWXI',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblVKKXVT2NARDUNGRKAOMYB6VWXI', 'D','tblVKKXVT2NARDUNGRKAOMYB6VWXI');
end;
/

create or replace trigger atiVKKXVT2NARDUNGRKAOMYB6VWXI
after insert on RDX_PC_NOTIFICATIONSENDER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblVKKXVT2NARDUNGRKAOMYB6VWXI'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colPH5JTQND3VDZTJJ7Q46PRKF7JE',:new.CHANNELKIND);
            RDX_AUDIT.addValueStr(vChangeData,'new.colTGS3PTFC5RGSLAFMQFUSXEJUOI',:new.ROUTINGKEY);
            RDX_AUDIT.addValueInt(vChangeData,'new.colEI7GIMO4KFAPRAU4HAVU6BQ3PI',:new.HISTMODE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colHDP4Q3IHNRGLZFFT6IRB3M3G24',:new.STOREATTACHINHIST);
            RDX_AUDIT.addValueStr(vChangeData,'new.colYRJIL7ZCM5AK5LDULILF6B2FDA',:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colLB4DFGLQ7RD6TK65SWSARQVKGA',:new.BODYTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colXECQRSZBN5F27GJKI242EGT7NA',:new.USERGROUPNAME);
   end if;

   vClassGuid := 'aecVKKXVT2NARDUNGRKAOMYB6VWXI';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblVKKXVT2NARDUNGRKAOMYB6VWXI',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblVKKXVT2NARDUNGRKAOMYB6VWXI', 'I','tblVKKXVT2NARDUNGRKAOMYB6VWXI');
end;
/

create or replace trigger atuVKKXVT2NARDUNGRKAOMYB6VWXI
after update on RDX_PC_NOTIFICATIONSENDER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblVKKXVT2NARDUNGRKAOMYB6VWXI'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colPH5JTQND3VDZTJJ7Q46PRKF7JE',:old.CHANNELKIND,:new.CHANNELKIND);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTGS3PTFC5RGSLAFMQFUSXEJUOI',:old.ROUTINGKEY,:new.ROUTINGKEY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colEI7GIMO4KFAPRAU4HAVU6BQ3PI',:old.HISTMODE,:new.HISTMODE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colHDP4Q3IHNRGLZFFT6IRB3M3G24',:old.STOREATTACHINHIST,:new.STOREATTACHINHIST);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colYRJIL7ZCM5AK5LDULILF6B2FDA',:old.SUBJECTTEMPLATE,:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colLB4DFGLQ7RD6TK65SWSARQVKGA',:old.BODYTEMPLATE,:new.BODYTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXECQRSZBN5F27GJKI242EGT7NA',:old.USERGROUPNAME,:new.USERGROUPNAME);
   end if;

   vClassGuid := 'aecVKKXVT2NARDUNGRKAOMYB6VWXI';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblVKKXVT2NARDUNGRKAOMYB6VWXI',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblVKKXVT2NARDUNGRKAOMYB6VWXI', 'U','tblVKKXVT2NARDUNGRKAOMYB6VWXI');
end;
/

