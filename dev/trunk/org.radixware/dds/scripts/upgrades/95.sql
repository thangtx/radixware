create or replace package RDX_Utils as

	function getUnixEpochMillis return number;

	function unixEpochMsToTsTzUtc(
		pMillis in number
	) return timestamp with time zone;
end;
/

grant execute on RDX_Utils to &USER&_RUN_ROLE
/

create or replace package body RDX_Utils as

	function getUnixEpochMillis return number
	is
	    res number;
	begin
	    select (extract(day from(sys_extract_utc(systimestamp) - to_timestamp('1970-01-01', 'YYYY-MM-DD'))) * 86400000 
	            + to_number(to_char(sys_extract_utc(systimestamp), 'SSSSSFF3'))) into res from dual;
	    return res;
	end;

	function unixEpochMsToTsTzUtc(
		pMillis in number
	) return timestamp with time zone
	is
	    res timestamp with time zone;
	begin
	    select 
	        to_timestamp_tz('1970-01-01 UTC', 'yyyy-mm-dd tzr')+ numtodsinterval(pMillis/1000,'second')  
	    into res
	    from dual;    
	    return res;
	end;
end;
/

