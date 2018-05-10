create or replace package RDX_SM_InstStateHist as

	procedure dailyMaintenance;
end;
/

grant execute on RDX_SM_InstStateHist to &USER&_RUN_ROLE
/

create or replace package body RDX_Maintenance as

	procedure clearSessions(
		pIsActive in integer
	)
	is
	   limitMins integer;
	begin
	    if (pIsActive = 0) then
	        select EASSESSIONINACTIVITYMINS into limitMins from RDX_SYSTEM where ID = 1;
	    else 
	        select EASSESSIONACTIVITYMINS into limitMins from RDX_SYSTEM where ID = 1;
	    end if;
	    delete 
	        from RDX_EASSESSION
	        where ISACTIVE = pIsActive and
	            (sysdate - LASTCONNECTTIME)*60*24 > limitMins;
	end;

	-- Perform daily maintenance
	procedure daily
	is
	begin       
	   RDX_Trace.put(1, 'Radix System maintenance started', 'Arte.Db');
	   
	   --RDX_Trace.put(0, 'Oracle audit cleared', 'Arte.Db');
	   
	   RDX_AUDIT.dailyMaintenance();
	   RDX_Trace.put(0, 'Radix audit maintenance finished', 'Arte.Db');
	   
	   RDX_Trace.maintenance;
	   RDX_Trace.put(0,  'Event log cleared', 'Arte.Db');
	   
	   RDX_Maintenance.clearSessions(pIsActive => 0); 
	   RDX_Maintenance.clearSessions(pIsActive => 1); 
	   RDX_Trace.put(0,  'Session log cleared', 'Arte.Db');
	   
	   RDX_Maintenance.clearProfileLog;
	   RDX_Trace.put(0,  'Profiler log cleared', 'Arte.Db');
	   
	   RDX_Trace.clearSensitiveData;
	   RDX_Trace.put(0,  'Sensitive data removed from Event Log', 'Arte.Db');
	   
	   RDX_SM_METRIC.dailyMaintenance;
	   RDX_Trace.put(0,  'Metrics history cleared', 'Arte.Db');
	   
	   RDX_License.dailyMaintenance();
	   RDX_Trace.put(0, 'License maintenance finished', 'Arte.Db');
	   
	   RDX_WF_Maintenance.daily;
	   RDX_PC_Maintenance.daily;
	   
	   RDX_SM_InstStateHist.dailyMaintenance();
	   RDX_Trace.put(0,  'Instance state history cleared', 'Arte.Db');
	   
	   RDX_Trace.put(1,  'Radix System maintenance finished', 'Arte.Db');
	end;

	-- Ежеминутный (примерно) maintenance
	procedure continualy
	is
	begin       
	   RDX_Maintenance.clearSessions(pIsActive => 0);             
	end;

	procedure clearProfileLog
	is
	   pStoreDays number;
	begin
	   select PROFILELOGSTOREDAYS into pStoreDays from RDX_SYSTEM where ID =1;
	   loop  
	      delete /*+ index(RDX_PROFILERLOG PK_RDX_PROFILERLOG) */ from RDX_PROFILERLOG where PERIODENDTIME < sysdate - pStoreDays and ROWNUM <= 10000;
	      exit when SQL%NOTFOUND;
	      commit;
	   end loop;
	end;
end;
/

create or replace package body RDX_SM_InstStateHist as

	procedure dailyMaintenance
	is
	    cMillisPerDay constant integer := 24 * 60 * 60 * 1000;
	    vMaxStoreDays integer;
	begin
	    for c in (select RDX_INSTANCE.ID as instId, nvl(RDX_INSTANCE.INSTSTATEHISTORYSTOREDAYS, RDX_SYSTEM.INSTSTATEHISTORYSTOREDAYS) as storeDays
	              from RDX_INSTANCE, RDX_SYSTEM where RDX_INSTANCE.SYSTEMID = RDX_SYSTEM.ID) loop
	        loop
	            delete from RDX_SM_INSTANCESTATEHISTORY where INSTANCEID = c.instId and REGTIMEMILLIS < RDX_Utils.getUnixEpochMillis() - c.storeDays * cMillisPerDay and RowNum < 10000;
	            exit when SQL%NotFound;
	            commit;
	        end loop;    
	    end loop;
	    
	    select max(nvl(RDX_INSTANCE.INSTSTATEHISTORYSTOREDAYS, RDX_SYSTEM.INSTSTATEHISTORYSTOREDAYS)) + 1 into vMaxStoreDays 
	    from RDX_INSTANCE, RDX_SYSTEM where RDX_INSTANCE.SYSTEMID = RDX_SYSTEM.ID;
	    
	    loop
	        delete from RDX_SM_STACKDATA where LASTUSAGETIMEMILLIS < RDX_Utils.getUnixEpochMillis() - vMaxStoreDays * cMillisPerDay and RowNum < 10000;
	        exit when SQL%NotFound;
	        commit;
	    end loop;
	end;
end;
/

