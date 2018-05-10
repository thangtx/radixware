create or replace package body RDX_SM_InstStateHist as

	procedure dailyMaintenance
	is
	    cMillisPerHour constant integer := 60 * 60 * 1000;
	    cMillisPerDay constant integer := 24 * cMillisPerHour;
	    vNowMillis integer := RDX_Utils.getUnixEpochMillis();
	    vMaxStoreDays integer;
	    vMaxStoreMillis integer;
	begin
	    for c in (select RDX_INSTANCE.ID as instId, nvl(RDX_INSTANCE.INSTSTATEHISTORYSTOREDAYS, RDX_SYSTEM.INSTSTATEHISTORYSTOREDAYS) as storeDays
	              from RDX_INSTANCE, RDX_SYSTEM where RDX_INSTANCE.SYSTEMID = RDX_SYSTEM.ID) loop
	        loop
	            delete /*+ index(RDX_SM_INSTANCESTATEHISTORY, PK_RDX_SM_INSTANCESTATEHISTORY) */ from RDX_SM_INSTANCESTATEHISTORY where INSTANCEID = c.instId and REGTIMEMILLIS < vNowMillis - c.storeDays * cMillisPerDay and RowNum < 10000;
	            exit when SQL%NotFound;
	            commit;
	        end loop;    
	    end loop;
	    
	    select max(nvl(RDX_INSTANCE.INSTSTATEHISTORYSTOREDAYS, RDX_SYSTEM.INSTSTATEHISTORYSTOREDAYS)) into vMaxStoreDays 
	    from RDX_INSTANCE, RDX_SYSTEM where RDX_INSTANCE.SYSTEMID = RDX_SYSTEM.ID;
	    vMaxStoreMillis := vMaxStoreDays * cMillisPerDay + cMillisPerHour;
	    
	    loop
	        delete from RDX_SM_STACKDATA where LASTUSAGETIMEMILLIS < vNowMillis - vMaxStoreMillis and RowNum < 10000;
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

