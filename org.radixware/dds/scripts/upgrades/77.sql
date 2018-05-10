create or replace package body RDX_AUDIT as

	function dropSubPartition(
		d in date,
		type in integer
	) return boolean
	is
	  dayName   varchar2(100);
	  NOT_EXIST exception;   PRAGMA EXCEPTION_INIT(NOT_EXIST, -2149);
	  THE_ONLY exception;    PRAGMA EXCEPTION_INIT(THE_ONLY, -14758);
	  THE_ONLY_2 exception;    PRAGMA EXCEPTION_INIT(THE_ONLY_2, -14083);
	  THE_ONLY_SUB exception;    PRAGMA EXCEPTION_INIT(THE_ONLY_SUB, -14629);
	  INVALID_SUB exception;    PRAGMA EXCEPTION_INIT(INVALID_SUB, -14702);
	begin
	   select to_char(d,'YYYY_MM_DD') into dayName from dual;
	   begin
	      EXECUTE IMMEDIATE 'alter table RDX_AU_AUDITLOG drop subpartition for (to_date(''' || dayName || ''', ''YYYY_MM_DD''), '|| TO_CHAR(type) ||')';
	   exception 
	     when NOT_EXIST or INVALID_SUB then
	          return false;
	     when THE_ONLY_SUB then
	      begin    
	         EXECUTE IMMEDIATE 'alter table RDX_AU_AUDITLOG drop partition for (to_date(''' || dayName || ''', ''YYYY_MM_DD''))';
	      exception 
	        when THE_ONLY or THE_ONLY_2 or NOT_EXIST then 
	          return false;
	      end;
	   end;
	   return true;
	end;

	procedure dropOldSubPartitions(
		duration in integer,
		type in integer
	)
	is
	   d date;
	begin
	   d:=sysdate-duration;
	   #IF DB_TYPE == "ORACLE" AND isEnabled("org.radixware\\Partitioning") THEN
	   loop
	      d:=d-1;  
	      exit when RDX_AUDIT.dropSubPartition(d, Type)!=TRUE;
	   end loop;
	   #ELSE
	   delete from RDX_AU_AUDITLOG where RDX_AU_AUDITLOG.EVENTTIME < d and RDX_AU_AUDITLOG.STOREDURATION = type;
	   #ENDIF
	end;

	procedure cleanPartitions
	is  
	  v1 integer; v2 integer; v3 integer; v4 integer;v5 integer;
	   
	begin
	   select AUDITSTOREPERIOD1, AUDITSTOREPERIOD2, AUDITSTOREPERIOD3, AUDITSTOREPERIOD4, AUDITSTOREPERIOD5
	      into v1,v2,v3,v4,v5 
	      from RDX_SYSTEM where ID=1;
	   RDX_AUDIT.dropOldSubPartitions(v1,1);
	   RDX_AUDIT.dropOldSubPartitions(v2,2);
	   RDX_AUDIT.dropOldSubPartitions(v3,3);
	   RDX_AUDIT.dropOldSubPartitions(v4,4);
	   RDX_AUDIT.dropOldSubPartitions(v5,5);
	end;

	procedure dailyMaintenance
	is 
	begin

	  --удаление разделов
	  RDX_AUDIT.cleanPartitions;
	  
	  --создание раздела на завтра
	  commit;
	  insert into RDX_AU_AUDITLOG (EVENTTIME, ID, EVENTTYPE,  STOREDURATION, TABLEID, PID) values (sysdate+1, SQN_RDX_AU_AUDITLOGID.NextVal, 'I', 1, 'x', 'x');
	  rollback;
	end;

	procedure enableTrigger(
		pId in varchar2,
		pType in varchar2
	)
	is
	    vQuery varchar2(250);
	begin
	    vQuery := 'ALTER TRIGGER at' || Lower(pType) || substr(pId,4) || ' ENABLE';
	    begin
	        EXECUTE IMMEDIATE vQuery;
	        exception when OTHERS then return;       
	    end;
	end;

	procedure disableTrigger(
		pTableid in varchar2,
		pType in varchar2,
		pParentTableid in varchar2
	)
	is 
	    vQuery varchar2(250);
	    vCnt integer;
	begin
	--    select count(*) into vCnt from RDX_AU_SCHEMEITEM where tableid = pParentTableid and eventType = Upper(pType);
	--    if vCnt <= 1 then 
	--        begin
	    vQuery := 'ALTER TRIGGER at' || Lower(pType) || substr(pTableId,4) || ' DISABLE';
	    begin
	        EXECUTE IMMEDIATE vQuery;
	        exception when OTHERS then return;       
	    end;
	--        end;    
	--    end if;    
	end;

	procedure updateTableAuditStateEventType(
		pTableGuid in varchar2,
		pEventType in varchar2,
		pParentTableGuid in varchar2
	)
	is
	    vSchemeId varchar2(50);
	    vCount integer;
	begin
	    select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID = 1;
	    if vSchemeId is not null then
	        select Count(*) into vCount from RDX_AU_SCHEMEITEM 
	            where SCHEMEGUID = vSchemeId and TABLEID = pParentTableGuid and EVENTTYPE = pEventType and Rownum < 2;
	        if vCount > 0 then 
	            RDX_AUDIT.enableTrigger(pTableGuid, pEventType);
	        else    
	            RDX_AUDIT.disableTrigger(pTableGuid, pEventType, pParentTableGuid);
	        end if;
	    else
	        RDX_AUDIT.disableTrigger(pTableGuid, pEventType, pParentTableGuid);
	    end if;
	end;

	procedure updateTableAuditState(
		tableGuid in varchar2,
		tableParentGuid in varchar2
	)
	is
	    vSaveData integer;
	begin
	    RDX_AUDIT.updateTableAuditStateEventType(tableGuid, 'U', tableParentGuid);
	    RDX_AUDIT.updateTableAuditStateEventType(tableGuid, 'I', tableParentGuid);
	    RDX_AUDIT.updateTableAuditStateEventType(tableGuid, 'D', tableParentGuid);
	end;

	function getStoreDuration(
		pSchemeId in varchar2,
		pTableId in varchar2,
		pType in varchar2
	) return integer
	is vStoreDuration integer;
	begin
	    begin
	        select STOREDURATION into vStoreDuration
	        from RDX_AU_SCHEMEITEM
	        where SCHEMEGUID = pSchemeId and
	        TABLEID = pTableId and 
	        EVENTTYPE = pType and
	        SAVEDATA = 1;
	    exception when no_data_found then vStoreDuration := -1;
	    end;   
	    return vStoreDuration; 
	end;

	procedure addValueStr(
		pData in out clob,
		pName in varchar2,
		pValue in varchar2
	)
	is
	  vTemp CLOB; 
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(Length(pValue))||']'||pValue;
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueBlob(
		pData in out clob,
		pName in varchar2,
		pValue in BLOB
	)
	is
	  vTemp CLOB; val varchar2(32767); 
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     val := UTL_RAW.CAST_TO_VARCHAR2(UTL_ENCODE.base64_encode(DBMS_LOB.SUBSTR(pValue)));
	     vTemp := pName||'=['||TO_CHAR(length(val))||']'||val;
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueRaw(
		pData in out clob,
		pName in varchar2,
		pValue in RAW
	)
	is
	  vTemp CLOB; val varchar2(32767);
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else 
	     val := UTL_RAW.CAST_TO_VARCHAR2(UTL_ENCODE.base64_encode(pValue));
	     vTemp := pName||'=['||TO_CHAR(Length(val))||']'||val;
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueClob(
		pData in out clob,
		pName in varchar2,
		pValue in CLOB
	)
	is
	  vTemp CLOB; 
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(dbms_lob.getlength(pValue))||']';
	     dbms_lob.append(vTemp,pValue);
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueBool(
		pData in out clob,
		pName in varchar2,
		pValue in boolean
	)
	is
	  vTemp CLOB; 
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;              
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	    if pValue then
	        vTemp := pName||'=[0]true';
	    else
	        vTemp := pName||'=[0]false';
	    end if;    
	  end if;     
	  pData:=pData||vTemp;
	end;

	procedure addValueInt(
		pData in out clob,
		pName in varchar2,
		pValue in integer
	)
	is
	  vTemp CLOB; 
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;              
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(Length(TO_CHAR(pValue)))||']'||TO_CHAR(pValue);
	  end if;     
	  pData:=pData||vTemp;
	end;

	procedure addValueNum(
		pData in out clob,
		pName in varchar2,
		pValue in number
	)
	is
	  vTemp CLOB; 
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(Length(TO_CHAR(pValue)))||']'||TO_CHAR(pValue,'FM9999999999999999999999999999999.9999999999999999999999999999999');
	  end if; 
	  pData:=pData||vTemp;
	end;

	procedure addValueDate(
		pData in out clob,
		pName in varchar2,
		pValue in date
	)
	is
	  vTemp CLOB;        
	  vDate varchar2(100);
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;             
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vDate := TO_CHAR(pValue,'DD.MM.YYYY HH24:MI:SS'); 
	     vTemp := pName||'=['||TO_CHAR(Length(vDate))||']'||vDate;
	  end if;
	  pData:=pData||vTemp;
	end;

	procedure regPropUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		updateData in CLOB,
		pidKey in varchar2
	)
	is 
	   vStoreDuration integer; 
	   vSchemeId varchar2(50);
	begin
	    vSchemeId:=schemeId;
	    if (schemeId is null) then
	        begin
	             select RDX_SYSTEM.DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM, RDX_AU_SCHEME where RDX_SYSTEM.ID =1 and RDX_AU_SCHEME.GUID = RDX_SYSTEM.DEFAULTAUDITSCHEMEID;
	        exception 
	            when no_data_found then return;
	        end;
	    end if;
	    vStoreDuration := RDX_AUDIT.getStoreDuration(vSchemeId,tableId,'U');
	    if vStoreDuration != -1 then
	        insert into RDX_AU_AUDITLOG 
	                (RDX_AU_AUDITLOG.ID,RDX_AU_AUDITLOG.EVENTTIME,RDX_AU_AUDITLOG.TABLEID,RDX_AU_AUDITLOG.CLASSID,
	                 RDX_AU_AUDITLOG.EVENTTYPE,RDX_AU_AUDITLOG.STOREDURATION,RDX_AU_AUDITLOG.USERNAME,
	                 RDX_AU_AUDITLOG.STATIONNAME,RDX_AU_AUDITLOG.PID,RDX_AU_AUDITLOG.EVENTDATA)
	             values 
	                (SQN_RDX_AU_AUDITLOGID.NextVal,SysDate,tableId,pClassId,
	                 'U',vStoreDuration,RDX_Arte.getUserName, 
	                 RDX_Arte.getStationName, pidKey, updateData);
	    end if;        
	end;

	procedure regPropIntUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in integer,
		newVal in integer
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueInt(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueInt(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId, tableId, pClassId, propId, vData,pid);
	end;

	procedure regPropRawUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in RAW,
		newVal in RAW
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueRaw(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueRaw(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId, tableId, pClassId, propId, vData, pid);
	end;

	procedure regPropBlobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in BLOB,
		newVal in BLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBlob(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueBlob(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId, tableId, pClassId, propId, vData, pid);
	end;

	procedure regPropClobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in CLOB,
		newVal in CLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueClob(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueClob(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId, pClassId, propId,vData,pid);
	end;

	procedure regPropDateTimeUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in date,
		newVal in date
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueDate(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueDate(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropNumCreated(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueNum(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId, pClassId, propId,vData,pid);
	end;

	procedure regPropStrCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueStr(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropBoolCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBool(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropDateTimeCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueDate(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId, pClassId, propId,vData,pid);
	end;

	procedure regPropClobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueClob(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropBlobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBlob(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropRawCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueRaw(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropNumDeleted(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueNum(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropStrDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueStr(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropBoolDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBool(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropDateTimeDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueDate(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropClobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueClob(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropBlobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBlob(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropRawDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueRaw(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropIntDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueInt(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropIntCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueInt(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropBoolUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in boolean,
		newVal in boolean
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBool(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueBool(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropStrUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in varchar2,
		newVal in varchar2
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueStr(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueStr(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropNumUpdate(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in number,
		newVal in number
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueNum(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueNum(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure addChangedValueDateTime(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in date,
		pNewValue in date
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueDate(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueDate(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueRaw(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in RAW,
		pNewValue in RAW
	)
	is
	begin
	    if  dbms_lob.compare(pOldValue,pNewValue)!=0 then
	        begin
	            RDX_AUDIT.addValueRaw(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueRaw(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueBlob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in BLOB,
		pNewValue in BLOB
	)
	is
	begin
	    if  dbms_lob.compare(pOldValue,pNewValue)!=0 then
	        begin
	            RDX_AUDIT.addValueBlob(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueBlob(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueClob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in CLOB,
		pNewValue in CLOB
	)
	is
	begin
	    if  dbms_lob.compare(pOldValue,pNewValue)!=0 then
	        begin
	            RDX_AUDIT.addValueClob(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueClob(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueBool(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in boolean,
		pNewValue in boolean
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueBool(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueBool(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueStr(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in varchar2,
		pNewValue in varchar2
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueStr(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueStr(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueNum(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in number,
		pNewValue in number
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueNum(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueNum(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueInt(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in integer,
		pNewValue in integer
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueInt(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueInt(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;
end;
/