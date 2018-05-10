create index IDX_RDX_PC_SENTMESSAGE_DELIVER on RDX_PC_SENTMESSAGE (CHANNELID asc, DELIVERYSTATUS asc)
/

create or replace package body RDX_PC_Maintenance as

	procedure daily
	is
	  storeDays integer;
	  storeFailedDays integer;
	  
	  procedure expireDeliveryStatuses(pChannelId in integer, pPolicy in varchar2, pPeriod in integer)
	  is
	    vPeriod integer := case pPolicy when 'None' then 7 * 86400 else pPeriod end;
	    vBoundDate date := SYSDATE - vPeriod / 86400.0;
	  begin
	    loop
	        update RDX_PC_SENTMESSAGE set DELIVERYSTATUS = 'Unknown'
	        where CHANNELID = pChannelId and DELIVERYSTATUS = 'Tracking'
	          and SENTTIME < vBoundDate and ROWNUM < 10000;
	        exit when SQL%NotFound;
	        commit;
	    end loop;
	  end;
	  
	begin
	  Select PCMSTOREDAYS into storeDays from RDX_SYSTEM where ID=1;
	  Select RDX_SYSTEM.FAILEDOUTMESSAGESTOREDAYS into storeFailedDays from RDX_SYSTEM where ID=1;
	  
	  loop
	    Delete from RDX_PC_SENTMESSAGE where SENTTIME + storeDays < SYSDATE;
	    exit when SQL%NotFound;
	    commit;
	  end loop;
	  
	  loop
	    Delete from RDX_PC_RECVMESSAGE where RECVTIME + storeDays < SYSDATE;
	    exit when SQL%NotFound;
	    commit;
	  end loop;
	  
	  Insert into RDX_PC_SENTMESSAGE (RDX_PC_SENTMESSAGE.ADDRESS, RDX_PC_SENTMESSAGE.BODY, RDX_PC_SENTMESSAGE.CALLBACKCLASSNAME, RDX_PC_SENTMESSAGE.CALLBACKMETHODNAME, RDX_PC_SENTMESSAGE.CHANNELID,
	          RDX_PC_SENTMESSAGE.CHANNELKIND, RDX_PC_SENTMESSAGE.CREATETIME, RDX_PC_SENTMESSAGE.DESTENTITYGUID, RDX_PC_SENTMESSAGE.DESTPID, RDX_PC_SENTMESSAGE.DUETIME, RDX_PC_SENTMESSAGE.EXPIRETIME,
	          RDX_PC_SENTMESSAGE.HISTMODE, RDX_PC_SENTMESSAGE.ID, RDX_PC_SENTMESSAGE.IMPORTANCE, 
	          RDX_PC_SENTMESSAGE.SENDERROR, 
	          RDX_PC_SENTMESSAGE.SENTTIME, RDX_PC_SENTMESSAGE.SMPPBYTESSENT, RDX_PC_SENTMESSAGE.SMPPCHARSSENT, RDX_PC_SENTMESSAGE.SMPPENCODING, RDX_PC_SENTMESSAGE.SMPPPARTSSENT, RDX_PC_SENTMESSAGE.SOURCEENTITYGUID,
	          RDX_PC_SENTMESSAGE.SOURCEMSGID, RDX_PC_SENTMESSAGE.SOURCEPID, RDX_PC_SENTMESSAGE.SUBJECT
	        ) 
	  select RDX_PC_OUTMESSAGE.ADDRESS, RDX_PC_OUTMESSAGE.BODY, RDX_PC_OUTMESSAGE.CALLBACKCLASSNAME, RDX_PC_OUTMESSAGE.CALLBACKMETHODNAME, RDX_PC_OUTMESSAGE.CHANNELID,
	          RDX_PC_OUTMESSAGE.CHANNELKIND, RDX_PC_OUTMESSAGE.CREATETIME, RDX_PC_OUTMESSAGE.DESTENTITYGUID, RDX_PC_OUTMESSAGE.DESTPID, RDX_PC_OUTMESSAGE.DUETIME, RDX_PC_OUTMESSAGE.EXPIRETIME,
	          RDX_PC_OUTMESSAGE.HISTMODE, RDX_PC_OUTMESSAGE.ID, RDX_PC_OUTMESSAGE.IMPORTANCE, 
	          'Permanent transmission failure for this message was detected and keep time for failed messages was exhausted. Last send error was '||RDX_PC_OUTMESSAGE.FAILEDMESSAGE||', retry count was ' || to_char(RDX_PC_OUTMESSAGE.FAILEDTRYCOUNT,'999999'),
	          SYSDATE, 0, 0, RDX_PC_OUTMESSAGE.SMPPENCODING, 0, RDX_PC_OUTMESSAGE.SOURCEENTITYGUID,
	          RDX_PC_OUTMESSAGE.SOURCEMSGID, RDX_PC_OUTMESSAGE.SOURCEPID, RDX_PC_OUTMESSAGE.SUBJECT
	  from RDX_PC_OUTMESSAGE where RDX_PC_OUTMESSAGE.FAILEDLASTSENDDATE+storeFailedDays < SYSDATE;
	  commit;
	  
	  delete from RDX_PC_OUTMESSAGE o where exists (select 1 from RDX_PC_SENTMESSAGE s where o.ID = s.ID);
	  commit;
	  
	  for c in (select * from RDX_PC_CHANNELUNIT) loop
	    expireDeliveryStatuses(c.ID, c.DELIVERYTRACKINGPOLICY, c.DELIVERYTRACKINGPERIOD);
	  end loop;
	end;
end;
/

