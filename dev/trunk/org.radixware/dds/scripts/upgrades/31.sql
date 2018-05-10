create or replace package body RDX_License as

	procedure dailyMaintenance
	is
	   lastSent date;
	begin
	    select max(RDX_LICENSEREPORTLOG.CREATEDATE) into lastSent from RDX_LICENSEREPORTLOG where RDX_LICENSEREPORTLOG.SENT > 0;    
            /*
	    if lastSent is null then
	        RDX_Trace.put(2,'No license reports were sent to product vendor','App.License',0);
	    else 
	        if lastSent > (sysdate - 30) then
	            RDX_Trace.put(2,'Last sent license report expired', 'App.License',0);
	        end if;
	    end if;
	    */
	end;
end;
/

