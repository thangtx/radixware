create or replace package RDX_Utils as

	function getSysTimeZone return varchar2;

	function dateToTsTz(
		pDate in date
	) return timestamp with time zone;

	function getUnixEpochMillis(
		pTimestamp in timestamp with time zone := null
	) return number;

	function unixEpochMsToTsTzUtc(
		pMillis in number
	) return timestamp with time zone;

	function getDateUnixEpochMillis(
		pDate in date := null
	) return integer;
end;
/

grant execute on RDX_Utils to &USER&_RUN_ROLE
/

create or replace package body RDX_Utils as

	sysTimeZone varchar2(10);

	function getSysTimeZone return varchar2
	is
	begin
	    if sysTimeZone is null then
	        select extract(TIMEZONE_HOUR from systimestamp) || ':' || extract(TIMEZONE_MINUTE from systimestamp) into sysTimeZone from dual;
	    end if;
	    return sysTimeZone;
	end;

	function dateToTsTz(
		pDate in date
	) return timestamp with time zone
	is
	begin
	    return from_tz(cast(nvl(pDate, sysdate) AS TIMESTAMP), RDX_Utils.getSysTimeZone());
	end;

	function getUnixEpochMillis(
		pTimestamp in timestamp with time zone := null
	) return number
	is
	    res number;
	begin
	    select (extract(day from(sys_extract_utc(nvl(pTimestamp, systimestamp)) - to_timestamp('1970-01-01', 'YYYY-MM-DD'))) * 86400000 
	            + to_number(to_char(sys_extract_utc(nvl(pTimestamp, systimestamp)), 'SSSSSFF3'))) into res from dual;
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

	function getDateUnixEpochMillis(
		pDate in date := null
	) return integer
	is
	begin
	    return RDX_Utils.getUnixEpochMillis( RDX_Utils.dateToTsTz(pDate) );
	end;
end;
/

