drop trigger atuX5TD7JDVVHWDBROXAAIT4AGD7E
/

drop trigger atiX5TD7JDVVHWDBROXAAIT4AGD7E
/

drop trigger atdZUTGVH5TKRFWZEUGZHC5KRIOFA
/

drop trigger atdZBS5MS2BI3OBDCIOAALOMT5GDM
/

drop trigger atdZ3SDCSSEI3OBDCIOAALOMT5GDM
/

drop trigger atdYDGQACJVVLOBDCLSAALOMT5GDM
/

drop trigger atdY2BRNTJ5VLOBDCLSAALOMT5GDM
/

drop trigger atdXOKR7CSUNFG3DCVQGB5LWIWIIU
/

drop trigger atdXI6CAOIGDBAGJEE6JQEGJME43Q
/

drop trigger atdX5TD7JDVVHWDBROXAAIT4AGD7E
/

drop trigger atdWZB7K4HLJPOBDCIUAALOMT5GDM
/

drop trigger atdUEE3YXA72HNRDB7BAALOMT5GDM
/

drop trigger atdSY4KIOLTGLNRDHRZABQAQH3XQ4
/

drop trigger atdS4NVQKVQI5HLTG33G43UK5QMXU
/

drop trigger atdR7FXMYDVVHWDBROXAAIT4AGD7E
/

drop trigger atdQZ2AJHN3PFDWXC5BT6I66OS5PQ
/

drop trigger atdQ23AYDTTGLNRDHRZABQAQH3XQ4
/

drop trigger atdPCE24TUPIHNRDJIEACQMTAIZT4
/

drop trigger atdM7J46MP6F3PBDIJEABQAQH3XQ4
/

drop trigger atdM47V6KTSNJC3FKWW2W6DME6Z2I
/

drop trigger atdLSU45NKDI3OBDCIOAALOMT5GDM
/

drop trigger atdJ6SOXKD3ZHOBDCMTAALOMT5GDM
/

drop trigger atdJ5AGLYGB6RGKNJ36XBBX5AZ4ZA
/

drop trigger atdHC6VVBZ4I3OBDCIOAALOMT5GDM
/

drop trigger atdHBE24OUE2DNRDB7AAALOMT5GDM
/

drop trigger atdGPFDUXBHJ3NRDJIRACQMTAIZT4
/

drop trigger atdFJAEQT3TGLNRDHRZABQAQH3XQ4
/

drop trigger atdFDFDUXBHJ3NRDJIRACQMTAIZT4
/

drop trigger atdDYWJCJTTGLNRDHRZABQAQH3XQ4
/

drop trigger atdCRD53OZ5I3OBDCIOAALOMT5GDM
/

drop trigger atdC2OWQGDVVHWDBROXAAIT4AGD7E
/

drop trigger atdB6QEDVV5BNEWZHKZRTVR67TKOY
/

drop trigger atdARTOV5KBI3OBDCIOAALOMT5GDM
/

drop trigger atdAAU55TOEHJWDRPOYAAYQQ2Y3GB
/

drop trigger atd7HTJAWJXVLOBDCLSAALOMT5GDM
/

drop trigger atd6HF4SHAXLDNBDJA6ACQMTAIZT4
/

drop trigger atd5WO5N3BEVLOBDCLSAALOMT5GDM
/

drop trigger atd5HP4XTP3EGWDBRCRAAIT4AGD7E
/

drop trigger atd52CHFNO3EGWDBRCRAAIT4AGD7E
/

drop trigger atd4UBCGVD5ZZF6ZIJN4J3YISPFZA
/

drop trigger atd42K4K2TTGLNRDHRZABQAQH3XQ4
/

drop trigger atd3DD7EMEOIHNRDJIEACQMTAIZT4
/

drop trigger TBIDU_RDX_SYSTEM
/

drop trigger TAIDU_RDX_SYSTEM
/

create or replace package RDX_AUDIT as

	procedure dailyMaintenance;

	procedure updateTableAuditState(
		tableGuid in varchar2,
		tableParentGuid in varchar2
	);

	procedure enableTrigger(
		pId in varchar2,
		pType in varchar2
	);

	procedure disableTrigger(
		pTableid in varchar2,
		pType in varchar2,
		pParentTableid in varchar2
	);

	procedure addValueNum(
		pData in out clob,
		pName in varchar2,
		pValue in number
	);

	procedure addValueInt(
		pData in out clob,
		pName in varchar2,
		pValue in integer
	);

	procedure addValueStr(
		pData in out clob,
		pName in varchar2,
		pValue in varchar2
	);

	procedure addValueDate(
		pData in out clob,
		pName in varchar2,
		pValue in date
	);

	procedure regPropNumUpdate(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in number,
		newVal in number
	);

	procedure regPropStrUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in varchar2,
		newVal in varchar2
	);

	procedure regPropBoolUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in boolean,
		newVal in boolean
	);

	procedure regPropDateTimeUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in date,
		newVal in date
	);

	procedure regPropClobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in CLOB,
		newVal in CLOB
	);

	procedure regPropBlobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in BLOB,
		newVal in BLOB
	);

	procedure regPropRawUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in RAW,
		newVal in RAW
	);

	procedure regPropIntUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in integer,
		newVal in integer
	);

	procedure addValueClob(
		pData in out clob,
		pName in varchar2,
		pValue in CLOB
	);

	procedure addValueBlob(
		pData in out clob,
		pName in varchar2,
		pValue in BLOB
	);

	procedure addValueRaw(
		pData in out clob,
		pName in varchar2,
		pValue in RAW
	);

	procedure addValueBool(
		pData in out clob,
		pName in varchar2,
		pValue in boolean
	);

	procedure regPropNumCreated(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	);

	procedure regPropStrCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	);

	procedure regPropBoolCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	);

	procedure regPropDateTimeCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	);

	procedure regPropClobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	);

	procedure regPropBlobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	);

	procedure regPropRawCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	);

	procedure regPropIntCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	);

	procedure regPropNumDeleted(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	);

	procedure regPropStrDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	);

	procedure regPropBoolDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	);

	procedure regPropDateTimeDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	);

	procedure regPropClobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	);

	procedure regPropBlobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	);

	procedure regPropRawDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	);

	procedure regPropIntDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	);

	procedure addChangedValueInt(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in integer,
		pNewValue in integer
	);

	procedure addChangedValueNum(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in number,
		pNewValue in number
	);

	procedure addChangedValueBool(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in boolean,
		pNewValue in boolean
	);

	procedure addChangedValueStr(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in varchar2,
		pNewValue in varchar2
	);

	procedure addChangedValueClob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in CLOB,
		pNewValue in CLOB
	);

	procedure addChangedValueDateTime(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in date,
		pNewValue in date
	);

	procedure addChangedValueRaw(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in RAW,
		pNewValue in RAW
	);

	procedure addChangedValueBlob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in BLOB,
		pNewValue in BLOB
	);

	procedure updateTableAuditStateEventType(
		pTableGuid in varchar2,
		pEventType in varchar2,
		pParentTableGuid in varchar2
	);
end;
/

grant execute on RDX_AUDIT to &USER&_RUN_ROLE
/

create or replace package RDX_AUDIT_Vars as

	DefaultAuditScheme varchar2(50);
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_AUDIT_Vars to &USER&_RUN_ROLE
/

create or replace trigger TAIDU_RDX_SYSTEM
after delete or insert or update on RDX_SYSTEM
begin
    RDX_AUDIT_Vars.DEFAULTAUDITSCHEME := NULL;
end;
/

create or replace trigger TBIDU_RDX_SYSTEM
before delete or insert or update on RDX_SYSTEM
declare auditSchemaId varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into auditSchemaId
      from RDX_SYSTEM
      where ID=1;
   exception
      when no_data_found then 
         auditSchemaId := null;
   end;
    
   if auditSchemaId IS NOT NULL then 
       RDX_AUDIT_Vars.DEFAULTAUDITSCHEME := auditSchemaId;
   else
       RDX_AUDIT_Vars.DEFAULTAUDITSCHEME := '-1';
   end if;
end;
/

create or replace trigger atd3DD7EMEOIHNRDJIEACQMTAIZT4
after delete on RDX_JS_JOBEXECUTORUNIT
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aec3DD7EMEOIHNRDJIEACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl3DD7EMEOIHNRDJIEACQMTAIZT4', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd42K4K2TTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USER2ROLE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl42K4K2TTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colCN3XEDKAZDNBDGMDABQAQH3XQ4',:old.USERNAME);
            RDX_AUDIT.addValueInt(vChangeData,'old.colDC3DKGLHPDNRDHTWABQAQH3XQ4',:old.ISOWN);
            RDX_AUDIT.addValueStr(vChangeData,'old.colFXSCOJCAZDNBDGMDABQAQH3XQ4',:old.ROLEID);
            RDX_AUDIT.addValueInt(vChangeData,'old.col4T2IAH72O5E6RNZPX3MNT44CQE',:old.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addValueStr(vChangeData,'old.colD7VLPHOA3VC4XL3QOLTWZLUHJI',:old.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aec42K4K2TTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl42K4K2TTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl42K4K2TTGLNRDHRZABQAQH3XQ4', 'D','tbl42K4K2TTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atd4UBCGVD5ZZF6ZIJN4J3YISPFZA
after delete on RDX_TST_AUDITMASTER
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colIY54HRUWQRC5LI4AMLNJDHNCAU',:old.COLINT);
            RDX_AUDIT.addValueStr(vChangeData,'old.colSW4DJGZ5LRCLFJCYZLQZGDNAME',:old.COLCHAR);
            RDX_AUDIT.addValueNum(vChangeData,'old.col6WWL3SC5TZBMZGHZSPPPJVV2BQ',:old.COLNUM);
            RDX_AUDIT.addValueDate(vChangeData,'old.colJ7FU6BTM75HQTCIAKYQDRXDY5U',:old.COLDATE);
            RDX_AUDIT.addValueDate(vChangeData,'old.col4TWPKWPOL5EB3GHUIUL6UFRHWQ',:old.COLTIMESTAMP);
            RDX_AUDIT.addValueStr(vChangeData,'old.colV5EPQ4QOLREEZOV2UUHUWZHYEE',:old.COLSTR);
            RDX_AUDIT.addValueRaw(vChangeData,'old.colTGVNWDGLGNBI3N5BYXZLI5RXQQ',:old.COLBIN);
            RDX_AUDIT.addValueBlob(vChangeData,'old.colFDVZCLAYD5HZRN2JGORFYFA3NE',:old.COLBLOB);
            RDX_AUDIT.addValueClob(vChangeData,'old.colL76ZGR3OPVHQFHYKDPWUIZQA34',:old.COLCLOB);
            RDX_AUDIT.addValueStr(vChangeData,'old.colI7RDFSIDUZE4PPBNSJFU4RCEIM',:old.COLARRINT);
            RDX_AUDIT.addValueClob(vChangeData,'old.colGAR4IF3GRVAFLA4DTQXPQGCSME',:old.COLARRCHAR);
            RDX_AUDIT.addValueClob(vChangeData,'old.colY5PXD2VKE5FBTFGVSCPTLGE46U',:old.COLARRSTR);
            RDX_AUDIT.addValueStr(vChangeData,'old.colE3SYXVPOCVEIFEEYBEIFXY66XQ',:old.COLARRNUM);
            RDX_AUDIT.addValueClob(vChangeData,'old.colYZYQHXB75VHEVG2BTGNGPNJZEA',:old.COLARRDATETIME);
            RDX_AUDIT.addValueClob(vChangeData,'old.col54URWAVOAVE4ZFLUZI7QEQYCDM',:old.COLARRBIN);
            RDX_AUDIT.addValueClob(vChangeData,'old.colD6G6MRNNKVGJFGSYHPF3DFQ66Y',:old.COLARRPARENTREF);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA', 'D','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atd52CHFNO3EGWDBRCRAAIT4AGD7E
after delete on RDX_INSTANCE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl52CHFNO3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aec52CHFNO3EGWDBRCRAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl52CHFNO3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl52CHFNO3EGWDBRCRAAIT4AGD7E', 'D','tbl52CHFNO3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd5HP4XTP3EGWDBRCRAAIT4AGD7E
after delete on RDX_UNIT
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd5WO5N3BEVLOBDCLSAALOMT5GDM
after delete on RDX_PC_CHANNELUNIT
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

create or replace trigger atd6HF4SHAXLDNBDJA6ACQMTAIZT4
after delete on RDX_SCP2SAP
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SYSTEMID||'~'||:old.SAPID||'~'||RDX_ENTITY.PackPIDStr(:old.SCPNAME);

   vClassGuid := 'aec6HF4SHAXLDNBDJA6ACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl6HF4SHAXLDNBDJA6ACQMTAIZT4', 'D','tbl6HF4SHAXLDNBDJA6ACQMTAIZT4');
end;
/

create or replace trigger atd7HTJAWJXVLOBDCLSAALOMT5GDM
after delete on RDX_PC_EVENTSUBSCRIPTION
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl7HTJAWJXVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colDHOISLJZVLOBDCLSAALOMT5GDM',:old.ISACTIVE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colLHS5GRJZVLOBDCLSAALOMT5GDM',:old.USERGROUPNAME);
            RDX_AUDIT.addValueStr(vChangeData,'old.col5OTM5EZZVLOBDCLSAALOMT5GDM',:old.CHANNELKIND);
            RDX_AUDIT.addValueStr(vChangeData,'old.colZQGUBQRZVLOBDCLSAALOMT5GDM',:old.EVENTSOURCE);
            RDX_AUDIT.addValueInt(vChangeData,'old.col6MRC5ZR3VLOBDCLSAALOMT5GDM',:old.MINEVENTSEVERITY);
            RDX_AUDIT.addValueStr(vChangeData,'old.colJOXDSNJ4VLOBDCLSAALOMT5GDM',:old.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colIS5OGPB4VLOBDCLSAALOMT5GDM',:old.BODYTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colRJPBERR4VLOBDCLSAALOMT5GDM',:old.LANGUAGE);
            RDX_AUDIT.addValueInt(vChangeData,'old.col3RTQXCB5VLOBDCLSAALOMT5GDM',:old.LOWIMPORTANCEMAXSEV);
            RDX_AUDIT.addValueInt(vChangeData,'old.colAQICTNR5VLOBDCLSAALOMT5GDM',:old.NORMALIMPORTANCEMAXSEV);
            RDX_AUDIT.addValueInt(vChangeData,'old.colVNCG43RIBREHHI5FFCUATH4PTM',:old.TOSTOREINHIST);
            RDX_AUDIT.addValueStr(vChangeData,'old.colVH3DNHSDUBD3JPNV535DVDBYBU',:old.LIMITMESSSUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.col2FJBO5EEGJCW3MOVEG3KLYQCGA',:old.LIMITMESSBODYTEMPLATE);
   end if;

   vClassGuid := 'aec7HTJAWJXVLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl7HTJAWJXVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl7HTJAWJXVLOBDCLSAALOMT5GDM', 'D','tbl7HTJAWJXVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atdAAU55TOEHJWDRPOYAAYQQ2Y3GB
after delete on RDX_SCP
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblAAU55TOEHJWDRPOYAAYQQ2Y3GB'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME)||'~'||:old.SYSTEMID;

   vClassGuid := 'aecAAU55TOEHJWDRPOYAAYQQ2Y3GB';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblAAU55TOEHJWDRPOYAAYQQ2Y3GB',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblAAU55TOEHJWDRPOYAAYQQ2Y3GB', 'D','tblAAU55TOEHJWDRPOYAAYQQ2Y3GB');
end;
/

create or replace trigger atdARTOV5KBI3OBDCIOAALOMT5GDM
after delete on RDX_JS_INTERVALSCHDITEM
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblARTOV5KBI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SCHEDULEID||'~'||:old.ID;

   vClassGuid := 'aecARTOV5KBI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblARTOV5KBI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblARTOV5KBI3OBDCIOAALOMT5GDM', 'D','tblARTOV5KBI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdB6QEDVV5BNEWZHKZRTVR67TKOY
after delete on RDX_TST_CHILD
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblB6QEDVV5BNEWZHKZRTVR67TKOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.col6OTOT3XC7BHKVIE3Y6WRX4O2OY',:old.CHILDTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'old.colZDX2SYK7TJAFTJENP6ATORQOTI',:old.COLBOOL);
            RDX_AUDIT.addValueStr(vChangeData,'old.colVTKWXW5C7RHLVACWBAAOZ3XJGY',:old.COLCHAR);
            RDX_AUDIT.addValueDate(vChangeData,'old.col7CYI2GEMAJA5LNUWSJ27NVIUZ4',:old.COLDATETIME);
   end if;

   vClassGuid := 'aecB6QEDVV5BNEWZHKZRTVR67TKOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblB6QEDVV5BNEWZHKZRTVR67TKOY',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblB6QEDVV5BNEWZHKZRTVR67TKOY', 'D','tblB6QEDVV5BNEWZHKZRTVR67TKOY');
end;
/

create or replace trigger atdC2OWQGDVVHWDBROXAAIT4AGD7E
after delete on RDX_SERVICE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblC2OWQGDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SYSTEMID||'~'||RDX_ENTITY.PackPIDStr(:old.URI);

   vClassGuid := 'aecC2OWQGDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblC2OWQGDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblC2OWQGDVVHWDBROXAAIT4AGD7E', 'D','tblC2OWQGDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atdCRD53OZ5I3OBDCIOAALOMT5GDM
after delete on RDX_JS_CALENDARITEM
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblCRD53OZ5I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblCRD53OZ5I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblCRD53OZ5I3OBDCIOAALOMT5GDM', 'D','tblCRD53OZ5I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdDYWJCJTTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USER2USERGROUP
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblDYWJCJTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.USERNAME)||'~'||RDX_ENTITY.PackPIDStr(:old.GROUPNAME);

   vClassGuid := 'aecDYWJCJTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblDYWJCJTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblDYWJCJTTGLNRDHRZABQAQH3XQ4', 'D','tblDYWJCJTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdFDFDUXBHJ3NRDJIRACQMTAIZT4
after delete on RDX_STATION
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFDFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);

   vClassGuid := 'aecFDFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFDFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFDFDUXBHJ3NRDJIRACQMTAIZT4', 'D','tblFDFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atdFJAEQT3TGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USERGROUP2ROLE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFJAEQT3TGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colXDHHLFZRY3NBDGMCABQAQH3XQ4',:old.GROUPNAME);
            RDX_AUDIT.addValueStr(vChangeData,'old.colQNEGZPZRY3NBDGMCABQAQH3XQ4',:old.ROLEID);
            RDX_AUDIT.addValueInt(vChangeData,'old.colAULGQS3XVFDL5DE7L3MW46URPQ',:old.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addValueStr(vChangeData,'old.colDWXM6MYALRBC3OYCWTWRGS4DIE',:old.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aecFJAEQT3TGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFJAEQT3TGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFJAEQT3TGLNRDHRZABQAQH3XQ4', 'D','tblFJAEQT3TGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdGPFDUXBHJ3NRDJIRACQMTAIZT4
after delete on RDX_USER2STATION
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblGPFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.STATIONNAME)||'~'||RDX_ENTITY.PackPIDStr(:old.USERNAME);

   vClassGuid := 'aecGPFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblGPFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblGPFDUXBHJ3NRDJIRACQMTAIZT4', 'D','tblGPFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atdHBE24OUE2DNRDB7AAALOMT5GDM
after delete on RDX_NETCHANNEL
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHBE24OUE2DNRDB7AAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.col6OTLV7WBZVGDFM7SYNM5KXYSGQ',:old.SERVERKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colSOCMOJR6SRCGBL63P2ALGCKNUQ',:old.CLIENTCERTALIASES);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHBE24OUE2DNRDB7AAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHBE24OUE2DNRDB7AAALOMT5GDM', 'D','tblHBE24OUE2DNRDB7AAALOMT5GDM');
end;
/

create or replace trigger atdHC6VVBZ4I3OBDCIOAALOMT5GDM
after delete on RDX_JS_CALENDAR
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHC6VVBZ4I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colUJIT2SZ5I3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHC6VVBZ4I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHC6VVBZ4I3OBDCIOAALOMT5GDM', 'D','tblHC6VVBZ4I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdJ5AGLYGB6RGKNJ36XBBX5AZ4ZA
after delete on RDX_TST_PARENT
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecJ5AGLYGB6RGKNJ36XBBX5AZ4ZA';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA', 'D','tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA');
end;
/

create or replace trigger atdJ6SOXKD3ZHOBDCMTAALOMT5GDM
after delete on RDX_USERFUNC
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.UPDEFID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERENTITYID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERPID);
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colYF2NT733ZHOBDCMTAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ6SOXKD3ZHOBDCMTAALOMT5GDM', 'D','tblJ6SOXKD3ZHOBDCMTAALOMT5GDM');
end;
/

create or replace trigger atdLSU45NKDI3OBDCIOAALOMT5GDM
after delete on RDX_JS_EVENTSCHD
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblLSU45NKDI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colYZCK5ZCDI3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := 'aecLSU45NKDI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblLSU45NKDI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblLSU45NKDI3OBDCIOAALOMT5GDM', 'D','tblLSU45NKDI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdM47V6KTSNJC3FKWW2W6DME6Z2I
after delete on RDX_NETHUB
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colBBSLHM5KRFC6VCPLABSQU5OUIY',:old.SAPID);
   end if;

   vClassGuid := 'aecM47V6KTSNJC3FKWW2W6DME6Z2I';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM47V6KTSNJC3FKWW2W6DME6Z2I', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atdM7J46MP6F3PBDIJEABQAQH3XQ4
after delete on RDX_AC_APPROLE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblM7J46MP6F3PBDIJEABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colMFRUYXP6F3PBDIJEABQAQH3XQ4',:old.TITLE);
   end if;

   vClassGuid := 'aecM7J46MP6F3PBDIJEABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblM7J46MP6F3PBDIJEABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM7J46MP6F3PBDIJEABQAQH3XQ4', 'D','tblM7J46MP6F3PBDIJEABQAQH3XQ4');
end;
/

create or replace trigger atdPCE24TUPIHNRDJIEACQMTAIZT4
after delete on RDX_JS_JOBEXECUTORUNITBOOST
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblPCE24TUPIHNRDJIEACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SPEED;

   vClassGuid := 'aecPCE24TUPIHNRDJIEACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblPCE24TUPIHNRDJIEACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblPCE24TUPIHNRDJIEACQMTAIZT4', 'D','tblPCE24TUPIHNRDJIEACQMTAIZT4');
end;
/

create or replace trigger atdQ23AYDTTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USERGROUP
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQ23AYDTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);

   vClassGuid := 'aecQ23AYDTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQ23AYDTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQ23AYDTTGLNRDHRZABQAQH3XQ4', 'D','tblQ23AYDTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdQZ2AJHN3PFDWXC5BT6I66OS5PQ
after delete on RDX_MESSAGEQUEUE
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

create or replace trigger atdR7FXMYDVVHWDBROXAAIT4AGD7E
after delete on RDX_SAP
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblR7FXMYDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colIHQTWYMFTJC5VEZAM3WWYLHZK4',:old.SERVERKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.col6F5NIDGUUNDYNLVMFKURFV4QOY',:old.SERVERCERTALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colFT5F6TB3TRFDHHAFWSW7TIYZRA',:old.CLIENTKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.col6X5HPPFXLJGOBCWX5CU6VISYTU',:old.CLIENTCERTALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colJ4MTAXJHPZHJZK6TW6NHKZY5MM',:old.CIPHERSUITES);
   end if;

   vClassGuid := 'aecR7FXMYDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblR7FXMYDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblR7FXMYDVVHWDBROXAAIT4AGD7E', 'D','tblR7FXMYDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atdS4NVQKVQI5HLTG33G43UK5QMXU
after delete on RDX_TST_AUDITDETAIL
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colB7LCJXCMXVFF7PZ7UH3YBVJXOQ',:old.TITLE);
   end if;

   vClassGuid := 'aecS4NVQKVQI5HLTG33G43UK5QMXU';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblS4NVQKVQI5HLTG33G43UK5QMXU', 'D','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atdSY4KIOLTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USER
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblSY4KIOLTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);

   vClassGuid := 'aecSY4KIOLTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblSY4KIOLTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblSY4KIOLTGLNRDHRZABQAQH3XQ4', 'D','tblSY4KIOLTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdUEE3YXA72HNRDB7BAALOMT5GDM
after delete on RDX_NETPORTHANDLER
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colUVIFSVD4RHWDBRCXAAIT4AGD7E',:old.SAPID);
   end if;

   vClassGuid := 'aecUEE3YXA72HNRDB7BAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblUEE3YXA72HNRDB7BAALOMT5GDM', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atdWZB7K4HLJPOBDCIUAALOMT5GDM
after delete on RDX_JS_TASK
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblWZB7K4HLJPOBDCIUAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblWZB7K4HLJPOBDCIUAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblWZB7K4HLJPOBDCIUAALOMT5GDM', 'D','tblWZB7K4HLJPOBDCIUAALOMT5GDM');
end;
/

create or replace trigger atdX5TD7JDVVHWDBROXAAIT4AGD7E
after delete on RDX_SYSTEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT_VARS.DEFAULTAUDITSCHEME
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblX5TD7JDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecX5TD7JDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblX5TD7JDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblX5TD7JDVVHWDBROXAAIT4AGD7E', 'D','tblX5TD7JDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atdXI6CAOIGDBAGJEE6JQEGJME43Q
after delete on RDX_WF_PROCESSTYPE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblXI6CAOIGDBAGJEE6JQEGJME43Q'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.col6SKMJNSZZNFVDNGCP26PG6QPEM',:old.CLERKROLEGUIDS);
            RDX_AUDIT.addValueStr(vChangeData,'old.col6KIGNQEL7NEWFDQMOEIO4POSTQ',:old.ADMINROLEGUIDS);
            RDX_AUDIT.addValueInt(vChangeData,'old.colNWJ6DLDISBBWZKPVZUMMGLM5LI',:old.PROCESSSTOREDAYS);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblXI6CAOIGDBAGJEE6JQEGJME43Q',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXI6CAOIGDBAGJEE6JQEGJME43Q', 'D','tblXI6CAOIGDBAGJEE6JQEGJME43Q');
end;
/

create or replace trigger atdXOKR7CSUNFG3DCVQGB5LWIWIIU
after delete on RDX_ARTEUNIT
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecXOKR7CSUNFG3DCVQGB5LWIWIIU';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXOKR7CSUNFG3DCVQGB5LWIWIIU', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atdY2BRNTJ5VLOBDCLSAALOMT5GDM
after delete on RDX_PC_EVENTSUBSCRIPTIONCODE
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblY2BRNTJ5VLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SUBSCRIPTIONID||'~'||RDX_ENTITY.PackPIDStr(:old.CODE);

   vClassGuid := 'aecY2BRNTJ5VLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblY2BRNTJ5VLOBDCLSAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblY2BRNTJ5VLOBDCLSAALOMT5GDM', 'D','tblY2BRNTJ5VLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atdYDGQACJVVLOBDCLSAALOMT5GDM
after delete on RDX_PC_CHANNELHANDLER
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblYDGQACJVVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.UNITID||'~'||:old.SEQ;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblYDGQACJVVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblYDGQACJVVLOBDCLSAALOMT5GDM', 'D','tblYDGQACJVVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atdZ3SDCSSEI3OBDCIOAALOMT5GDM
after delete on RDX_JS_EVENTSCHDITEM
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZ3SDCSSEI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SCHEDULEID||'~'||:old.ID;

   vClassGuid := 'aecZ3SDCSSEI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZ3SDCSSEI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZ3SDCSSEI3OBDCIOAALOMT5GDM', 'D','tblZ3SDCSSEI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdZBS5MS2BI3OBDCIOAALOMT5GDM
after delete on RDX_JS_INTERVALSCHD
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZBS5MS2BI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colKCIYPFSBI3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := 'aecZBS5MS2BI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZBS5MS2BI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZBS5MS2BI3OBDCIOAALOMT5GDM', 'D','tblZBS5MS2BI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdZUTGVH5TKRFWZEUGZHC5KRIOFA
after delete on RDX_JMSHANDLER
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
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colFMGGFQ6MORC67LQVLCPFLZHOJM',:old.SAPID);
   end if;

   vClassGuid := 'aecZUTGVH5TKRFWZEUGZHC5KRIOFA';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZUTGVH5TKRFWZEUGZHC5KRIOFA', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atiX5TD7JDVVHWDBROXAAIT4AGD7E
after insert on RDX_SYSTEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT_VARS.DEFAULTAUDITSCHEME
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblX5TD7JDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colTCOQ4HOZRHWDBRCXAAIT4AGD7E',:new.NAME);
            RDX_AUDIT.addValueStr(vChangeData,'new.col546FXWYZJ3NRDJIRACQMTAIZT4',:new.ARTELANGUAGE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colWGL2VHS7RNBIRA3NYYV3KGAIPI',:new.ARTECOUNTRY);
            RDX_AUDIT.addValueStr(vChangeData,'new.colMJDA72X3RRHX5AQ4RSCK3YNA44',:new.ARTEGUITRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colMYF3QYYJ7BFS7AJ6YRMZUFBE4M',:new.ARTEFILETRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colXLSJR3OUKLNRDAQSABIFNQAAAE',:new.ARTEDBTRACEPROFILE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colM6ABGYQYNVGYRC6NTJ76DIBZ4I',:new.EASSESSIONACTIVITYMINS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colLPOXOGA2J3NRDJIRACQMTAIZT4',:new.EASSESSIONINACTIVITYMINS);
            RDX_AUDIT.addValueStr(vChangeData,'new.colM6KLR5HJ3JDI7DTS5S52SRQEFU',:new.EASKRBPRINCNAME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colRJ6YTFHGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD1);
            RDX_AUDIT.addValueInt(vChangeData,'new.col3J6FBUPGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD2);
            RDX_AUDIT.addValueInt(vChangeData,'new.col3N6FBUPGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD3);
            RDX_AUDIT.addValueInt(vChangeData,'new.colTJMVJWXGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD4);
            RDX_AUDIT.addValueInt(vChangeData,'new.colTNMVJWXGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD5);
            RDX_AUDIT.addValueStr(vChangeData,'new.colEKZMW4XGWPNBDPLGABQJO5ADDQ',:new.DEFAULTAUDITSCHEMEID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colDCLAU6NMGVDSNKN7ZPB6SQJE5Y',:new.BLOCKUSERINVALIDLOGONCNT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colQTEXFSBLXJES3AOB6SXXKNGQQE',:new.BLOCKUSERINVALIDLOGONMINS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colLRLGIOF3DVB2XEFQXLCJLPUYTU',:new.ENABLESENSITIVETRACE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colKDV6X7RAL5DCBO2HENGADFNFTY',:new.PROFILELOGSTOREDAYS);
   end if;

   vClassGuid := 'aecX5TD7JDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblX5TD7JDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblX5TD7JDVVHWDBROXAAIT4AGD7E', 'I','tblX5TD7JDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atuX5TD7JDVVHWDBROXAAIT4AGD7E
after update of AADCCOMMITEDLOCKEXP, AADCMEMBERID, AADCTESTEDMEMBERID, AADCUNLOCKTABLES, ARTECOUNTRY, ARTEDBTRACEPROFILE, ARTEFILETRACEPROFILE, ARTEGUITRACEPROFILE, ARTELANGUAGE, ASKUSERPWDAFTERINACTIVITY, AUDITSTOREPERIOD1, AUDITSTOREPERIOD2, AUDITSTOREPERIOD3, AUDITSTOREPERIOD4, AUDITSTOREPERIOD5, BLOCKUSERINVALIDLOGONCNT, BLOCKUSERINVALIDLOGONMINS, CERTATTRFORUSERLOGIN, DEFAULTAUDITSCHEMEID, DUALCONTROLFORASSIGNROLE, DUALCONTROLFORCFGMGMT, EASKRBPRINCNAME, EASSESSIONACTIVITYMINS, EASSESSIONINACTIVITYMINS, ENABLESENSITIVETRACE, EVENTSTOREDAYS, EXTSYSTEMCODE, FAILEDOUTMESSAGESTOREDAYS, INSTSTATEFORCEDGATHERPERIODSEC, INSTSTATEGATHERPERIODSEC, INSTSTATEHISTORYSTOREDAYS, LIMITEASSESSIONSPERUSR, METRICHISTORYSTOREDAYS, NAME, ORAIMPLSTMTCACHESIZE, PCMSTOREDAYS, PROFILELOGSTOREDAYS, PWDEXPIRATIONPERIOD, PWDMINLEN, PWDMUSTBEINMIXEDCASE, PWDMUSTCONTAINACHARS, PWDMUSTCONTAINNCHARS, PWDMUSTCONTAINSCHARS, PWDMUSTDIFFERFROMNAME, TEMPORARYPWDEXPIRATIONPERIOD, UNIQUEPWDSEQLEN, USEORAIMPLSTMTCACHE, WRITECONTEXTTOFILE on RDX_SYSTEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT_VARS.DEFAULTAUDITSCHEME
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblX5TD7JDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colFRZFQPKGI5EM7DCHTHZ4PSKVCA',:old.EXTSYSTEMCODE,:new.EXTSYSTEMCODE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col7WZAHHSUZVCAVPPUZDJX5WWCA4',:old.CERTATTRFORUSERLOGIN,:new.CERTATTRFORUSERLOGIN);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTCOQ4HOZRHWDBRCXAAIT4AGD7E',:old.NAME,:new.NAME);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col546FXWYZJ3NRDJIRACQMTAIZT4',:old.ARTELANGUAGE,:new.ARTELANGUAGE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colWGL2VHS7RNBIRA3NYYV3KGAIPI',:old.ARTECOUNTRY,:new.ARTECOUNTRY);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colMJDA72X3RRHX5AQ4RSCK3YNA44',:old.ARTEGUITRACEPROFILE,:new.ARTEGUITRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colMYF3QYYJ7BFS7AJ6YRMZUFBE4M',:old.ARTEFILETRACEPROFILE,:new.ARTEFILETRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXLSJR3OUKLNRDAQSABIFNQAAAE',:old.ARTEDBTRACEPROFILE,:new.ARTEDBTRACEPROFILE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colM6ABGYQYNVGYRC6NTJ76DIBZ4I',:old.EASSESSIONACTIVITYMINS,:new.EASSESSIONACTIVITYMINS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colLPOXOGA2J3NRDJIRACQMTAIZT4',:old.EASSESSIONINACTIVITYMINS,:new.EASSESSIONINACTIVITYMINS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colM6KLR5HJ3JDI7DTS5S52SRQEFU',:old.EASKRBPRINCNAME,:new.EASKRBPRINCNAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colMBAPYLBLXFEOPBVK2IYG2HSVC4',:old.LIMITEASSESSIONSPERUSR,:new.LIMITEASSESSIONSPERUSR);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI',:old.ASKUSERPWDAFTERINACTIVITY,:new.ASKUSERPWDAFTERINACTIVITY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colCK5BEU6WO5H4VKRDA46ALDEZ3M',:old.UNIQUEPWDSEQLEN,:new.UNIQUEPWDSEQLEN);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col2J4RCA57UBFPFNVYLNMEXJI5CU',:old.PWDMINLEN,:new.PWDMINLEN);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colSLXTNR7MBRF23O655OVBOAL5LA',:old.PWDMUSTDIFFERFROMNAME,:new.PWDMUSTDIFFERFROMNAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTY6XN5X66FD2FIC7ZQVWKRV7YQ',:old.PWDMUSTCONTAINACHARS,:new.PWDMUSTCONTAINACHARS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colEFLQTDCHFZCTFNM77TYMJH35JY',:old.PWDMUSTBEINMIXEDCASE,:new.PWDMUSTBEINMIXEDCASE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colOJXZPIRBDZG6HCJZ4F2JDH7UFI',:old.PWDMUSTCONTAINNCHARS,:new.PWDMUSTCONTAINNCHARS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colPSC5YLHYXJD3ZICERRHVMMWRZI',:old.PWDMUSTCONTAINSCHARS,:new.PWDMUSTCONTAINSCHARS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colIVNUPJC565CXRNA3SBZKHI2LG4',:old.PWDEXPIRATIONPERIOD,:new.PWDEXPIRATIONPERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col6EXIXL45PZEBZOF2MKFF7RJWSA',:old.TEMPORARYPWDEXPIRATIONPERIOD,:new.TEMPORARYPWDEXPIRATIONPERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colUMLKGQCFD6VDBNKJAAUMFADAIA',:old.EVENTSTOREDAYS,:new.EVENTSTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colIH2FRGR6EKWDRWGPAAUUN6FMUG',:old.PCMSTOREDAYS,:new.PCMSTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colRJ6YTFHGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD1,:new.AUDITSTOREPERIOD1);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3J6FBUPGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD2,:new.AUDITSTOREPERIOD2);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3N6FBUPGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD3,:new.AUDITSTOREPERIOD3);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTJMVJWXGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD4,:new.AUDITSTOREPERIOD4);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTNMVJWXGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD5,:new.AUDITSTOREPERIOD5);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colEKZMW4XGWPNBDPLGABQJO5ADDQ',:old.DEFAULTAUDITSCHEMEID,:new.DEFAULTAUDITSCHEMEID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colDCLAU6NMGVDSNKN7ZPB6SQJE5Y',:old.BLOCKUSERINVALIDLOGONCNT,:new.BLOCKUSERINVALIDLOGONCNT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colQTEXFSBLXJES3AOB6SXXKNGQQE',:old.BLOCKUSERINVALIDLOGONMINS,:new.BLOCKUSERINVALIDLOGONMINS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colLRLGIOF3DVB2XEFQXLCJLPUYTU',:old.ENABLESENSITIVETRACE,:new.ENABLESENSITIVETRACE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKDV6X7RAL5DCBO2HENGADFNFTY',:old.PROFILELOGSTOREDAYS,:new.PROFILELOGSTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colE2XLLDK4RVAMFAL7O6XQGRTVNY',:old.METRICHISTORYSTOREDAYS,:new.METRICHISTORYSTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKQKCWI72DVAO7OFVSNBZ7XN2WQ',:old.USEORAIMPLSTMTCACHE,:new.USEORAIMPLSTMTCACHE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJN25VAA7SVD5FKDTO47ERVQIDQ',:old.ORAIMPLSTMTCACHESIZE,:new.ORAIMPLSTMTCACHESIZE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJ7OWNFGW3RCUVIE7PJUK3OO5GI',:old.DUALCONTROLFORASSIGNROLE,:new.DUALCONTROLFORASSIGNROLE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col5QRDG2EULRGM3IGS4RAPZWD2RQ',:old.DUALCONTROLFORCFGMGMT,:new.DUALCONTROLFORCFGMGMT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col42FD7ZUMLNA77LXG67VMOVRC5I',:old.WRITECONTEXTTOFILE,:new.WRITECONTEXTTOFILE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col66PAXUAEFBGMZPUYOHGU32ZD2Q',:old.FAILEDOUTMESSAGESTOREDAYS,:new.FAILEDOUTMESSAGESTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colM2PT6HH34FHXRHWAOZLL6HQATU',:old.AADCMEMBERID,:new.AADCMEMBERID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q',:old.AADCTESTEDMEMBERID,:new.AADCTESTEDMEMBERID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colGS3V2W2ZNNEXJA3CYQJ5O4AFSA',:old.AADCCOMMITEDLOCKEXP,:new.AADCCOMMITEDLOCKEXP);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colLKDHAIE6KZH7BKNDT4ALVM2HCI',:old.INSTSTATEGATHERPERIODSEC,:new.INSTSTATEGATHERPERIODSEC);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colIG74GFS3QNA5PFW3P27DVCWSPQ',:old.INSTSTATEFORCEDGATHERPERIODSEC,:new.INSTSTATEFORCEDGATHERPERIODSEC);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colA4GEOKW25BFTZIBY5PJ5W3KTCI',:old.INSTSTATEHISTORYSTOREDAYS,:new.INSTSTATEHISTORYSTOREDAYS);
   end if;

   vClassGuid := 'aecX5TD7JDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblX5TD7JDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblX5TD7JDVVHWDBROXAAIT4AGD7E', 'U','tblX5TD7JDVVHWDBROXAAIT4AGD7E');
end;
/

begin
	RDX_ACS.AcsUtilsBuild();
end;
/

