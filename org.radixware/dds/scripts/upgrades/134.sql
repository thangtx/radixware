create or replace package RDX_Utils as

	function getSysTimeZone return varchar2;

	function getSysTimeZoneOffsetDays return number;

	function dateToTsTz(
		pDate in date
	) return timestamp with time zone;

	function getUnixEpochMillis(
		pTimestamp in timestamp with time zone
	) return integer;

	function getUnixEpochMillis return integer;

	function getUnixEpochMillisDate(
		pDate in date
	) return integer;

	function unixEpochMsToTsTzUtc(
		pMillis in number
	) return timestamp with time zone;

	function unixEpochMsToDate(
		pMillis in number
	) return date;

	function blobToClob(
		pData in blob
	) return clob;

	function gunzipToClob(
		pCompressedData in blob
	) return clob;
end;
/

grant execute on RDX_Utils to &USER&_RUN_ROLE
/

create or replace package body RDX_Utils as

	function getSysTimeZone return varchar2
	is
	    vTimeZoneStr varchar2(10);
	begin
	    select extract(TIMEZONE_HOUR from systimestamp) || ':' || extract(TIMEZONE_MINUTE from systimestamp) into vTimeZoneStr from dual;
	    return vTimeZoneStr;
	end;

	function getSysTimeZoneOffsetDays return number
	is
	    hoursOffset number;
	    minutesOffset number;
	begin
	    select extract(TIMEZONE_HOUR from systimestamp), extract(TIMEZONE_MINUTE from systimestamp) into hoursOffset, minutesOffset from dual;
	    return (60 * hoursOffset + minutesOffset) / 1440.0;
	end;

	function dateToTsTz(
		pDate in date
	) return timestamp with time zone
	is
	begin
	    return from_tz(cast(pDate AS TIMESTAMP), RDX_Utils.getSysTimeZone());
	end;

	function getUnixEpochMillis(
		pTimestamp in timestamp with time zone
	) return integer
	is
	    res integer;
	begin
	    select (extract(day from(sys_extract_utc(pTimestamp) - to_timestamp('1970-01-01', 'YYYY-MM-DD'))) * 86400000 
	            + to_number(to_char(sys_extract_utc(pTimestamp), 'SSSSSFF3'))) into res from dual;
	    return res;
	end;

	function getUnixEpochMillis return integer
	is
	begin
	    return RDX_Utils.getUnixEpochMillis(systimestamp);
	end;

	function getUnixEpochMillisDate(
		pDate in date
	) return integer
	is
	begin
	    return RDX_Utils.getUnixEpochMillis( RDX_Utils.dateToTsTz(pDate) );
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

	function unixEpochMsToDate(
		pMillis in number
	) return date
	is
	    res date;
	begin
	    select cast(RDX_Utils.unixEpochMsToTsTzUtc(pMillis) as date) + RDX_Utils.getSysTimeZoneOffsetDays() into res from dual;
	    return res;
	end;

	function blobToClob(
		pData in blob
	) return clob
	is
	    res clob;
	    destOffset integer := 1;
	    srcOffset  integer := 1;
	    utf8 constant integer := 873;
	    langCtx integer := dbms_lob.default_lang_ctx;
	    warn integer;
	begin
	    dbms_lob.createTemporary(res, true, dbms_lob.transaction);
	    dbms_lob.convertToClob(
	            dest_lob => res,
	            src_blob => pData,
	            amount => dbms_lob.lobmaxsize,
	            dest_offset => destOffset,
	            src_offset => srcOffset,
	            blob_csid    => utf8,
	            lang_context => langCtx,
	            warning => warn);
	    return res;
	end;

	function gunzipToClob(
		pCompressedData in blob
	) return clob
	is
	    decompressedBlob blob := utl_compress.lz_uncompress(pCompressedData);
	    decompressedClob clob := RDX_Utils.blobToClob(decompressedBlob);
	begin
	    return decompressedClob;
	end;
end;
/

