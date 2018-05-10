create or replace package RDX_SM_InstStateHist as

	procedure dailyMaintenance;

	function getStackByDigest(
		pDigest in varchar2
	) return clob;
end;
/

grant execute on RDX_SM_InstStateHist to &USER&_RUN_ROLE
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

	function getStackByDigest(
		pDigest in varchar2
	) return clob
	is
	    vCompressedStack blob;
	    vClob clob;
	begin
	    select COMPRESSEDSTACK into vCompressedStack from RDX_SM_STACKDATA where DIGEST = pDigest;
	    return RDX_Utils.gunzipToClob(vCompressedStack);
	exception
	    when NO_DATA_FOUND then
	        return '<stack not found>';
	end;
end;
/

