#IF DB_TYPE == "ORACLE" AND !isEnabled("org.radixware\\Partitioning") THEN
alter table rdx_eventlog enable row movement
/

alter table rdx_eventcontext enable row movement
/
#ENDIF

create or replace package body RDX_Trace as

	function put_internal(
		pCode in varchar2,
		pWords in clob,
		pComponent in varchar2,
		pSeverity in number,
		pContextTypes in varchar2,
		pContextIds in varchar2,
		pTime in timestamp := NULL,
		pUserName in varchar2 := NULL,
		pStationName in varchar2 := NULL,
		pIsSensitive in integer := 0
	) return integer
	is
	   vId  integer;                          
	   time TIMESTAMP;
	   sensTraceOn  integer;
	   pt  integer;
	   pt2 integer;
	   pi  integer;
	   pi2 integer;
	   ct  varchar2(250);
	   ci  varchar2(250);                                   
	   eDublicated         exception;
	   PRAGMA EXCEPTION_INIT(eDublicated,-1);  -- ORA-00001 unique constraint
	   PRAGMA AUTONOMOUS_TRANSACTION;
	begin
	    if pIsSensitive != 0 then
	        select ENABLESENSITIVETRACE into sensTraceOn from RDX_SYSTEM where ID = 1;
	        if sensTraceOn = 0 then 
	            commit;    
	            return null;
	        end if;    
	    end if;
	   select SQN_RDX_EVENTLOGID.NextVal into vId from DUAL;
	   if pTime is null then
	       time:=SYSTIMESTAMP;
	   else   
	       time := pTime;              
	   end if;
	   insert into RDX_EVENTLOG (RAISETIME, ID, CODE, WORDS, SEVERITY, COMPONENT, USERNAME, STATIONNAME, ISSENSITIVE)
	                 values (time, vId, pCode, pWords, pSeverity, pComponent, pUserName, pStationName, pIsSensitive);
	   pt:=1; pi:=1;
	   loop
	      exit when pContextTypes is null or pt>=length(pContextTypes) or
	                pContextIds is null or pi >= length(pContextIds);
	      pt2:=instr(pContextTypes,chr(0),pt);
	      ct:=substr(pContextTypes,pt,pt2-pt);
	      pi2:=instr(pContextIds,chr(0),pi); 
	      if (pi2 > pi) then
	         ci:=substr(pContextIds,pi,pi2-pi);
	      else
	         ci:='-';
	      end if;
	      pt:=pt2+1;  pi:=pi2+1;      
	      begin
	          insert into RDX_EVENTCONTEXT (TYPE, ID, RAISETIME, EVENTID)
	                            values (ct,ci,time,vId);
	      exception
	          when eDublicated then continue;
	      end;                      
	   end loop;   
	   commit;
	   return vId;
	exception
	   when OTHERS then
	      rollback;
	      return NULL;
	end;

	procedure put(
		pSeverity in integer,
		pCode in varchar2,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	begin
	  RDX_Trace.put_1word(pSeverity => pSeverity, pCode => pCode, pWord1 => pMess, pSource => pSource, pIsSensitive => pIsSensitive);
	end;

	procedure put(
		pSeverity in integer,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	begin
	  RDX_Trace.put(pSeverity => pSeverity, pCode => NULL, pMess => pMess, pSource => pSource, pIsSensitive => pIsSensitive); 
	end;

	procedure put_1word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_2word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_3word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pWord3 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2, e3 => pWord3), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_4word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pWord3 in varchar2,
		pWord4 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2, e3 => pWord3, e4 => pWord4), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure deleteDebug
	is
	begin
	   loop  
	      delete from RDX_EVENTLOG where RDX_EVENTLOG.SEVERITY = 0 and ROWNUM <= 1000;
	      exit when SQL%NOTFOUND;
	      commit;
	   end loop;
	end;

	procedure clearSensitiveData(
		pForce in boolean := false
	)
	is
	   isOn integer default 0;
	begin
	   if not pForce then
	       select ENABLESENSITIVETRACE
	         into isOn 
	         from RDX_SYSTEM where ID=1;
	   end if;  
	   if isOn = 0 then
	       loop  
	          update /*+ index(RDX_EVENTLOG IDX_RDX_EVENTLOG_HASSENSITIVE) */  RDX_EVENTLOG set WORDS = null where ISSENSITIVE > 0 and WORDS is not null and ROWNUM <= 10000;
	          exit when SQL%NOTFOUND;
	          commit;
	       end loop;
	   end if;    
	end;

	procedure createPartition(
		d in date
	)
	is
	begin
	  commit;
	  insert into RDX_EVENTLOG(RAISETIME, ID, WORDS, SEVERITY, COMPONENT) values (d, SQN_RDX_EVENTLOGID.NextVal, 'x',0,'x');
	  insert into RDX_EVENTCONTEXT(RAISETIME, EVENTID, TYPE, ID) values (d, SQN_RDX_EVENTLOGID.CurrVal, 'x','x');
	  rollback;
	end;

	procedure dropEventPartitionsOlderThan(
		d in date
	)
	is
	 NOT_EXIST exception;   PRAGMA EXCEPTION_INIT(NOT_EXIST, -2149);
	 THE_ONLY exception;    PRAGMA EXCEPTION_INIT(THE_ONLY, -14083);
	 THE_LAST exception;    PRAGMA EXCEPTION_INIT(THE_LAST, -14758);
	 upperExclusiveBoundary date;
	begin
	    for r in (select *
	              from   all_tab_partitions tp
	              where  (tp.table_name = 'RDX_EVENTLOG' or tp.table_name = 'RDX_EVENTCONTEXT')
	              and    tp.table_owner = (SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')))
	    loop
	        execute immediate 'SELECT ' || r.high_value || ' from dual'
	            into upperExclusiveBoundary;
	        if upperExclusiveBoundary <= d then
	            if upper(r.partition_name) not like '%' || upper('RDX_EVENTLOG') || '%' and  upper(r.partition_name) not like '%' || upper('RDX_EVENTCONTEXT') || '%' then
	                begin                                      
	                    execute immediate 'alter table ' || r.table_owner || '.' || r.table_name || ' drop partition ' ||
	                                  r.partition_name;
	                    RDX_Trace.put(0,  'Partition of ' || r.table_name || ' for less than ' || r.high_value ||' dropped', 'App.Db');
	                exception when NOT_EXIST or THE_ONLY or THE_LAST then null;
	                end;
	            END IF;
	        END IF;
	    END LOOP;
	end;

	procedure maintenance
	as
	  d date; 
	  res boolean;
	begin
	  --удаление устаревших разделов
	  select trunc(sysdate)-RDX_SYSTEM.EVENTSTOREDAYS into d from RDX_SYSTEM where id=1;
	  #IF DB_TYPE == "ORACLE" AND isEnabled("org.radixware\\Partitioning") THEN
	  RDX_Trace.dropEventPartitionsOlderThan(d);   
	  --подготовка будущих разделов
	  RDX_Trace.createPartition(sysdate+1);
	  RDX_Trace.createPartition(sysdate+2);
	  #ELSE
	  loop  
	     delete from RDX_EVENTCONTEXT where RDX_EVENTCONTEXT.RAISETIME < d and ROWNUM < 10000;
	     exit when SQL%NOTFOUND;
	     commit;
	  end loop;
	  execute immediate 'alter table RDX_EVENTCONTEXT shrink space cascade';
	  
	  loop  
	     delete from RDX_EVENTLOG where RDX_EVENTLOG.RAISETIME < d and ROWNUM < 10000;
	     exit when SQL%NOTFOUND;
	     commit;
	  end loop;
	  execute immediate 'alter table RDX_EVENTLOG shrink space cascade';

	  #ENDIF
	end;

	function put_records(
		pRecords in RDX_EVENT_LOG_RECORDS
	) return integer
	is
	  rec RDX_EVENT_LOG_RECORD;
	  vId integer := null;  
	begin
	  if pRecords is null then return null; end if;
	  for idx in 1 .. pRecords.count
	  loop
	    rec := pRecords(idx);
	    vId := rdx_trace.put_internal(rec.code, rec.words, rec.component, rec.severity, rec.contextTypes, rec.contextIds, rec.time, rec.userName, rec.stationName, rec.isSensitive);
	  end loop;
	  return vId;
	end;

	function translate(
		pBundleId in varchar2,
		pStringId in varchar2,
		pLanguage in varchar2,
		pWords in clob
	) return clob
	is
	    version integer := RDX_Arte.getVersion();
	    wordsArrStr RDX_Array.ARR_STR := null;
	    wordsArrClob RDX_Array.ARR_CLOB := null;
	    template varchar2(32767);
	    result clob := null;
	    word clob := null;
	    i integer := 1;
	    j integer;
	    wi integer;
	begin
	    begin
	        wordsArrStr := RDX_Array.fromStr(pWords);
	    exception
	        when OTHERS then
	            wordsArrClob := RDX_Array.fromStrToArrClob(pWords);
	    end;
	            
	    if pBundleId is NULL or pStringId is NULL then
	        if wordsArrStr is not NULL and wordsArrStr.count > 0 then
	            return wordsArrStr(1);
	        elsif wordsArrClob is not NULL and wordsArrClob.count > 0 then
	            return wordsArrClob(1);    
	        end if;
	        return null;
	    else
	        select STRINGVALUE into template from RDX_EVENTCODEMLS
	        where VERSIONNUM = version and BUNDLEID = pBundleId and STRINGID = pStringId and LANGUAGE = pLanguage;
	        
	        while i <= length(template) loop
	            j := instr(template, '%', i);
	            if j = 0 then
	                j := length(template) + 1;
	            end if;
	            result := result || substr(template, i, j - i);

	            if j < length(template) and substr(template, j + 1, 1) in ('1', '2', '3', '4', '5', '6', '7', '8', '9') then
	                wi := to_number(substr(template, j + 1, 1));
	                if wordsArrStr is not null and wordsArrStr.count >= wi then
	                    word := wordsArrStr(wi);
	                elsif wordsArrClob is not null and wordsArrClob.count >= wi then
	                    word := wordsArrClob(wi);
	                else
	                    word := 'null';
	                end if;
	                result := result || word;
	                i := j + 2;
	            elsif j <= length(template) then
	                result := result || '%';
	                i := j + 1;
	            else
	                i := j + 1;
	            end if;
	        end loop;
	    end if;
	    return result;
	exception
	    when NO_DATA_FOUND then
	        return '??? (code=' || pBundleId || '-' || pStringId || ', args = ' || pWords || ')';
	end;

	function translate(
		pQualifiedId in varchar2,
		pLanguage in varchar2,
		pWords in clob
	) return clob
	is
	    delimPos integer := instr(pQualifiedId, '-');
	    bundleId varchar2(100) := substr(pQualifiedId, 1, delimPos - 1);
	    stringId varchar2(100) := substr(pQualifiedId, delimPos + 1);
	    result clob := RDX_Trace.translate(bundleId, stringId, pLanguage, pWords);
	begin
	    return result;
	end;
end;
/

