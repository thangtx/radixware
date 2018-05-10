drop trigger atu5WO5N3BEVLOBDCLSAALOMT5GDM
/

drop trigger ati5WO5N3BEVLOBDCLSAALOMT5GDM
/

drop trigger atd5WO5N3BEVLOBDCLSAALOMT5GDM
/

alter table RDX_PC_SENTMESSAGE
	add DELIVERYSTATUS VARCHAR2(100 char) null
/

alter table RDX_PC_CHANNELUNIT
	add DELIVERYTRACKINGPERIOD NUMBER(9,0) null
/

alter table RDX_PC_CHANNELUNIT
	add DELIVERYTRACKINGPOLICY VARCHAR2(100 char) default 'None' not null
/

alter table RDX_PC_CHANNELUNIT
	add DELIVERYTRACKINGRETRYPERIOD NUMBER(9,0) null
/

alter table RDX_PC_SENTMESSAGE
	add LASTDELIVERYSTATUSCHANGEDATE DATE null
/

alter table RDX_PC_SENTMESSAGE
	add SMPPMESSAGEID VARCHAR2(64 char) null
/

create or replace trigger atd5WO5N3BEVLOBDCLSAALOMT5GDM
after delete on RDX_PC_CHANNELUNIT
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colB2VQZVZFVLOBDCLSAALOMT5GDM',:old.KIND);
            RDX_AUDIT.addValueStr(vChangeData,'old.colQYYL4LVVA5CFVMK63MXXWHGJV4',:old.DELIVERYTRACKINGPOLICY);
            RDX_AUDIT.addValueInt(vChangeData,'old.colDHUCZH4SC5A6VKLQWEXJLA7AJM',:old.DELIVERYTRACKINGPERIOD);
            RDX_AUDIT.addValueInt(vChangeData,'old.colOSQWZLFLI5G3RCM5C7JFSQX6M4',:old.DELIVERYTRACKINGRETRYPERIOD);
   end if;

   vClassGuid := 'aec5WO5N3BEVLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5WO5N3BEVLOBDCLSAALOMT5GDM', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger ati5WO5N3BEVLOBDCLSAALOMT5GDM
after insert on RDX_PC_CHANNELUNIT
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
            RDX_AUDIT.addValueStr(vChangeData,'new.colB2VQZVZFVLOBDCLSAALOMT5GDM',:new.KIND);
            RDX_AUDIT.addValueInt(vChangeData,'new.colNSHJLYZFVLOBDCLSAALOMT5GDM',:new.SENDPERIOD);
            RDX_AUDIT.addValueInt(vChangeData,'new.col6ARRB4BFVLOBDCLSAALOMT5GDM',:new.RECVPERIOD);
            RDX_AUDIT.addValueStr(vChangeData,'new.colWASXULBLVLOBDCLSAALOMT5GDM',:new.SENDADDRESS);
            RDX_AUDIT.addValueStr(vChangeData,'new.col4UVYQI3O4DOBDAPTABQJO5ADDQ',:new.POP3ADDRESS);
            RDX_AUDIT.addValueStr(vChangeData,'new.colNOHTUMZLVLOBDCLSAALOMT5GDM',:new.RECVADDRESS);
            RDX_AUDIT.addValueStr(vChangeData,'new.colPSI5Q6JLVLOBDCLSAALOMT5GDM',:new.MESSADDRESSREGEXP);
            RDX_AUDIT.addValueInt(vChangeData,'new.colVV73PCZLVLOBDCLSAALOMT5GDM',:new.ROUTINGPRIORITY);
            RDX_AUDIT.addValueStr(vChangeData,'new.colRPM5HGZLVLOBDCLSAALOMT5GDM',:new.ENCODING);
            RDX_AUDIT.addValueStr(vChangeData,'new.colHEW6Y7BQVLOBDCLSAALOMT5GDM',:new.SMPPSYSTEMID);
            RDX_AUDIT.addValueStr(vChangeData,'new.colZM275BJQVLOBDCLSAALOMT5GDM',:new.SMPPSYSTEMTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col2KCZRM4VHBC2XNT32CRN5UDKWE',:new.SMPPSESSIONTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col66NFXWMWJ5HDBJFYGSP3WGJZDI',:new.SMPPSESSIONTON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colSXQZYQ3C7JCLFP4XIHFNAS42P4',:new.SMPPSESSIONNPI);
            RDX_AUDIT.addValueStr(vChangeData,'new.colZ5DQRVBQVLOBDCLSAALOMT5GDM',:new.SMPPPASSWORD);
            RDX_AUDIT.addValueStr(vChangeData,'new.colAYWB5XRQVLOBDCLSAALOMT5GDM',:new.SMPPSOURCEADDRESS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colADKKX2RQVLOBDCLSAALOMT5GDM',:new.SMPPSOURCEADDRESSTON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colK43Q5NBRVLOBDCLSAALOMT5GDM',:new.SMPPSOURCEADDRESSNPI);
            RDX_AUDIT.addValueInt(vChangeData,'new.colHTZKDXRRVLOBDCLSAALOMT5GDM',:new.SMPPDESTINATIONTON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colHPZKDXRRVLOBDCLSAALOMT5GDM',:new.SMPPDESTINATIONNPI);
            RDX_AUDIT.addValueInt(vChangeData,'new.colJUHQZ4BRVLOBDCLSAALOMT5GDM',:new.SMPPINTERFACE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colJYHQZ4BRVLOBDCLSAALOMT5GDM',:new.SMPPMAXLEN);
            RDX_AUDIT.addValueStr(vChangeData,'new.colHR7J6PZSVLOBDCLSAALOMT5GDM',:new.EMAILLOGIN);
            RDX_AUDIT.addValueStr(vChangeData,'new.colVKPD2RZSVLOBDCLSAALOMT5GDM',:new.EMAILPASSWORD);
            RDX_AUDIT.addValueStr(vChangeData,'new.colDSHWAVJSVLOBDCLSAALOMT5GDM',:new.ADDRESSTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.col6CT2K2RSVLOBDCLSAALOMT5GDM',:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colTQE4J6T4NJEWHIDFMFRCFVLYIU',:new.FILEFORMAT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colV4JLFAINZBDR3MMLVE36PDT4FA',:new.SENDTIMEOUT);
            RDX_AUDIT.addValueStr(vChangeData,'new.colQYYL4LVVA5CFVMK63MXXWHGJV4',:new.DELIVERYTRACKINGPOLICY);
            RDX_AUDIT.addValueInt(vChangeData,'new.colDHUCZH4SC5A6VKLQWEXJLA7AJM',:new.DELIVERYTRACKINGPERIOD);
            RDX_AUDIT.addValueInt(vChangeData,'new.colOSQWZLFLI5G3RCM5C7JFSQX6M4',:new.DELIVERYTRACKINGRETRYPERIOD);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5WO5N3BEVLOBDCLSAALOMT5GDM', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu5WO5N3BEVLOBDCLSAALOMT5GDM
after update of ADDRESSTEMPLATE, DELIVERYTRACKINGPERIOD, DELIVERYTRACKINGPOLICY, DELIVERYTRACKINGRETRYPERIOD, EMAILLOGIN, EMAILPASSWORD, ENCODING, FILEFORMAT, KIND, MESSADDRESSREGEXP, POP3ADDRESS, RECVADDRESS, RECVPERIOD, ROUTINGPRIORITY, SENDADDRESS, SENDPERIOD, SENDTIMEOUT, SMPPDESTINATIONNPI, SMPPDESTINATIONTON, SMPPINTERFACE, SMPPMAXLEN, SMPPPASSWORD, SMPPSESSIONNPI, SMPPSESSIONTON, SMPPSESSIONTYPE, SMPPSOURCEADDRESS, SMPPSOURCEADDRESSNPI, SMPPSOURCEADDRESSTON, SMPPSYSTEMID, SMPPSYSTEMTYPE, SUBJECTTEMPLATE on RDX_PC_CHANNELUNIT
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
            RDX_AUDIT.addChangedValueStr(vChangeData,'colB2VQZVZFVLOBDCLSAALOMT5GDM',:old.KIND,:new.KIND);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colNSHJLYZFVLOBDCLSAALOMT5GDM',:old.SENDPERIOD,:new.SENDPERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col6ARRB4BFVLOBDCLSAALOMT5GDM',:old.RECVPERIOD,:new.RECVPERIOD);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colWASXULBLVLOBDCLSAALOMT5GDM',:old.SENDADDRESS,:new.SENDADDRESS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col4UVYQI3O4DOBDAPTABQJO5ADDQ',:old.POP3ADDRESS,:new.POP3ADDRESS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colNOHTUMZLVLOBDCLSAALOMT5GDM',:old.RECVADDRESS,:new.RECVADDRESS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colPSI5Q6JLVLOBDCLSAALOMT5GDM',:old.MESSADDRESSREGEXP,:new.MESSADDRESSREGEXP);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colVV73PCZLVLOBDCLSAALOMT5GDM',:old.ROUTINGPRIORITY,:new.ROUTINGPRIORITY);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colRPM5HGZLVLOBDCLSAALOMT5GDM',:old.ENCODING,:new.ENCODING);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colHEW6Y7BQVLOBDCLSAALOMT5GDM',:old.SMPPSYSTEMID,:new.SMPPSYSTEMID);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colZM275BJQVLOBDCLSAALOMT5GDM',:old.SMPPSYSTEMTYPE,:new.SMPPSYSTEMTYPE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col2KCZRM4VHBC2XNT32CRN5UDKWE',:old.SMPPSESSIONTYPE,:new.SMPPSESSIONTYPE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col66NFXWMWJ5HDBJFYGSP3WGJZDI',:old.SMPPSESSIONTON,:new.SMPPSESSIONTON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colSXQZYQ3C7JCLFP4XIHFNAS42P4',:old.SMPPSESSIONNPI,:new.SMPPSESSIONNPI);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colZ5DQRVBQVLOBDCLSAALOMT5GDM',:old.SMPPPASSWORD,:new.SMPPPASSWORD);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colAYWB5XRQVLOBDCLSAALOMT5GDM',:old.SMPPSOURCEADDRESS,:new.SMPPSOURCEADDRESS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colADKKX2RQVLOBDCLSAALOMT5GDM',:old.SMPPSOURCEADDRESSTON,:new.SMPPSOURCEADDRESSTON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colK43Q5NBRVLOBDCLSAALOMT5GDM',:old.SMPPSOURCEADDRESSNPI,:new.SMPPSOURCEADDRESSNPI);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colHTZKDXRRVLOBDCLSAALOMT5GDM',:old.SMPPDESTINATIONTON,:new.SMPPDESTINATIONTON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colHPZKDXRRVLOBDCLSAALOMT5GDM',:old.SMPPDESTINATIONNPI,:new.SMPPDESTINATIONNPI);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJUHQZ4BRVLOBDCLSAALOMT5GDM',:old.SMPPINTERFACE,:new.SMPPINTERFACE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJYHQZ4BRVLOBDCLSAALOMT5GDM',:old.SMPPMAXLEN,:new.SMPPMAXLEN);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colHR7J6PZSVLOBDCLSAALOMT5GDM',:old.EMAILLOGIN,:new.EMAILLOGIN);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colVKPD2RZSVLOBDCLSAALOMT5GDM',:old.EMAILPASSWORD,:new.EMAILPASSWORD);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colDSHWAVJSVLOBDCLSAALOMT5GDM',:old.ADDRESSTEMPLATE,:new.ADDRESSTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6CT2K2RSVLOBDCLSAALOMT5GDM',:old.SUBJECTTEMPLATE,:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTQE4J6T4NJEWHIDFMFRCFVLYIU',:old.FILEFORMAT,:new.FILEFORMAT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colV4JLFAINZBDR3MMLVE36PDT4FA',:old.SENDTIMEOUT,:new.SENDTIMEOUT);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colQYYL4LVVA5CFVMK63MXXWHGJV4',:old.DELIVERYTRACKINGPOLICY,:new.DELIVERYTRACKINGPOLICY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colDHUCZH4SC5A6VKLQWEXJLA7AJM',:old.DELIVERYTRACKINGPERIOD,:new.DELIVERYTRACKINGPERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colOSQWZLFLI5G3RCM5C7JFSQX6M4',:old.DELIVERYTRACKINGRETRYPERIOD,:new.DELIVERYTRACKINGRETRYPERIOD);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5WO5N3BEVLOBDCLSAALOMT5GDM', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

