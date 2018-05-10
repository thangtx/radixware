create or replace package body RDX_JS_EventSchedule as

	CURSOR cEventScheduleItems (SchId RDX_JS_EVENTSCHDITEM.SCHEDULEID%TYPE)  is
	                          Select CALENDARID calendarId, ENDTIME endTime, EVENTTIME eventTime, PERIOD period,
	                          REPEATABLE repeatable, STARTTIME startTime, TIMEZONEREGION timeZoneRegion
	                          From RDX_JS_EVENTSCHDITEM
	                          Where SCHEDULEID=SchId;

	function getDaySeconds(
		pTimestamp in timestamp
	) return integer
	is
	begin
	    return extract(Hour from pTimestamp)*3600 + extract(Minute from pTimestamp)*60 + extract(Second from pTimestamp);
	end;

	function getDaySecondsTz(
		pTimestamp in timestamp with time zone
	) return integer
	is
	    res integer;
	    diff interval day to second;
	begin
	    diff := pTimestamp - from_tz(trunc(pTimestamp), extract(TIMEZONE_REGION from pTimestamp));
	    return round(extract(hour from diff) * 3600 + extract(minute from diff) * 60 + extract(second from diff));
	end;

	function toTimestampWithTzSafe(
		pTimestamp in timestamp,
		pTzRegion in varchar2 := null
	) return timestamp with time zone
	is
	    res1 timestamp with time zone := null;
	    res2 timestamp with time zone := null;
	    FIELD_NOT_FOUND_IN_DATETIME EXCEPTION; PRAGMA EXCEPTION_INIT(FIELD_NOT_FOUND_IN_DATETIME, -01878);
	begin
	    begin
	        res1 := from_tz(pTimestamp, nvl(pTzRegion, sessiontimezone));
	    exception when FIELD_NOT_FOUND_IN_DATETIME then
	        null;
	    end;
	    res2 := from_tz(trunc(pTimestamp), nvl(pTzRegion, sessiontimezone)) + NumToDSInterval(RDX_JS_EventSchedule.getDaySeconds(pTimestamp), 'second');
	    if 
	        res1 is null or 
	        (res1 is not null 
	        and res2 is not null 
	        and to_char(res1, 'HH24:MI') = to_char(res2, 'HH24:MI'))
	    then
	        return res2;
	    else 
	        return res1;
	    end if;
	end;

	function prevTime(
		pStartTime in integer,
		pEndTime in integer,
		pPeriod in integer,
		pNow in integer
	) return integer
	is
	    res integer;
	begin
	    if pNow <= pStartTime THEN
	        return null;
	    end if;
	    
	    if pNow >= pEndTime THEN
	        return (TRUNC((pEndTime-1-pStartTime)/pPeriod))*pPeriod + pStartTime;
	    end if;

	    res := (TRUNC((pNow-pStartTime)/pPeriod))*pPeriod + pStartTime;
	    if res = pNow THEN
	      res := res - pPeriod;
	    end if;
	    return res;
	end;

	function nextTime(
		pStartTime in integer,
		pEndTime in integer,
		pPeriod in integer,
		pNow in integer
	) return integer
	--высчитывает следующее время для заданных начала, конца, периода
	--все параметры одного типа (минуты/секудны)
	is
	    res integer;
	begin
	    if pNow >= pEndTime THEN
	        return null;
	    end if;
	    if pNow < pStartTime THEN
	        return pStartTime;
	    end if;
	    
	    res := (TRUNC((pNow-pStartTime)/pPeriod)+1)*pPeriod + pStartTime;
	    if res >= pEndTime THEN
	        return null;
	    else
	        return res;
	    end if;
	end;

	function next(
		pId in integer,
		pDateTime in timestamp
	) return timestamp
	is
	    minTimestamp timestamp := null;
	    tmpTimestamp timestamp;
	    tmpDate date;
	    tmpDaySeconds integer;
	    
	    tmpFrom timestamp;
	    tmpFromWithTz timestamp with time zone;
	    tmpToday date;
	    tmpTomorrow date;
	    tmpFromDaySeconds integer; 
	    inToday boolean;

	    todayDefault date;
	    tomorrowDefault date;
	    fromDaySecondsDefault integer;
	begin
	    if pDateTime IS NULL THEN
	        return NULL;
	    end if;
	    
	    todayDefault := Trunc(pDateTime);
	    tomorrowDefault := todayDefault + interval '1' day;
	    fromDaySecondsDefault := RDX_JS_EventSchedule.getDaySeconds(pDateTime);
	    
	    for schedItem in cEventScheduleItems(pId) LOOP
	        
	        if schedItem.timeZoneRegion is not null then
	            tmpFromWithTz := RDX_JS_EventSchedule.toTimestampWithTzSafe(pDateTime) at time zone (schedItem.timeZoneRegion);            
	            tmpToday := Trunc(tmpFromWithTz);
	            tmpTomorrow := tmpToday + interval '1' day;
	            tmpFromDaySeconds := RDX_JS_EventSchedule.getDaySecondsTz(tmpFromWithTz);
	        else
	            tmpFrom := pDateTime;
	            tmpToday := todayDefault;
	            tmpTomorrow := tomorrowDefault;
	            tmpFromDaySeconds := fromDaySecondsDefault;
	        end if;
	    
	        if schedItem.REPEATABLE > 0 THEN
	            tmpDaySeconds := RDX_JS_EventSchedule.nextTime(schedItem.STARTTIME * 60, schedItem.ENDTIME * 60, schedItem.PERIOD, tmpFromDaySeconds);
	        else
	            tmpDaySeconds := schedItem.EVENTTIME * 60;
	        end if;

	        inToday := tmpDaySeconds is NOT NULL and tmpDaySeconds > tmpFromDaySeconds
	                and 
	            (schedItem.CALENDARID IS NULL or RDX_JS_CalendarSchedule.isIn(pId => schedItem.CALENDARID, pDate => tmpToday) > 0);
	            
	        IF inToday THEN
	            tmpDate := tmpToday;
	        ELSE       
	            if schedItem.CALENDARID IS NULL THEN
	                tmpDate := tmpTomorrow;                     
	            else
	                tmpDate := RDX_JS_CalendarSchedule.next(pId => schedItem.CALENDARID, pDate => tmpToday);
	            end if;
	            if schedItem.REPEATABLE > 0 THEN
	                tmpDaySeconds := schedItem.STARTTIME * 60;
	            end if;         
	        END IF;  

	        if tmpDate is not null then
	            tmpTimestamp := cast(tmpDate as timestamp) + NumToDSInterval(tmpDaySeconds, 'second');
	 
	            if schedItem.timeZoneRegion is not null then
	                tmpTimestamp := RDX_JS_EventSchedule.toTimestampWithTzSafe(tmpTimestamp, schedItem.timeZoneRegion) at time zone sessiontimezone;
	            end if;
	            
	            if minTimestamp is null or minTimestamp > tmpTimestamp then
	                minTimestamp := tmpTimestamp;
	            end if;
	        end if;
	    end LOOP;

	    return minTimestamp; 
	end;

	function prev(
		pId in integer,
		pDateTime in timestamp
	) return timestamp
	is
	    maxTimestamp timestamp := null;
	    tmpTimestamp timestamp;
	    tmpDate date;
	    tmpDaySeconds integer;

	    tmpFrom timestamp;
	    tmpFromWithTz timestamp with time zone;
	    tmpToday date;
	    tmpYesterday date;
	    tmpFromDaySeconds integer; 
	    inToday boolean;  
	    
	    todayDefault date;
	    yesterdayDefault date;
	    fromDaySecondsDefault integer;
	begin
	    if pDateTime IS NULL THEN
	        return NULL;
	    end if;
	    
	    todayDefault := Trunc(pDateTime);
	    yesterdayDefault := todayDefault - interval '1' day;
	    fromDaySecondsDefault := RDX_JS_EventSchedule.getDaySeconds(pDateTime);

	    for schedItem in cEventScheduleItems(pId) LOOP
	    
	        if schedItem.timeZoneRegion is not null then
	            tmpFromWithTz := RDX_JS_EventSchedule.toTimestampWithTzSafe(pDateTime) at time zone (schedItem.timeZoneRegion);            
	            tmpToday := Trunc(tmpFromWithTz);
	            tmpYesterday := tmpToday - interval '1' day;
	            tmpFromDaySeconds := RDX_JS_EventSchedule.getDaySecondsTz(tmpFromWithTz);
	        else
	            tmpFrom := pDateTime;
	            tmpToday := todayDefault;
	            tmpYesterday := yesterdayDefault;
	            tmpFromDaySeconds := fromDaySecondsDefault;
	        end if;
	        
	        if schedItem.REPEATABLE > 0 THEN
	            tmpDaySeconds := RDX_JS_EventSchedule.prevTime(schedItem.STARTTIME * 60, schedItem.ENDTIME*60, schedItem.PERIOD, tmpFromDaySeconds);
	        else
	            tmpDaySeconds := schedItem.EVENTTIME * 60;
	        end if;

	        inToday := tmpDaySeconds is NOT NULL and tmpDaySeconds < tmpFromDaySeconds
	                and (schedItem.CALENDARID IS NULL or RDX_JS_CalendarSchedule.isIn(schedItem.calendarId, tmpToday) > 0);
	                
	        IF inToday THEN
	            tmpDate := tmpToday;
	        ELSE       
	            if schedItem.CALENDARID IS NULL THEN
	                tmpDate := tmpYesterday;                     
	            else
	                tmpDate := RDX_JS_CalendarSchedule.prev(schedItem.CALENDARID, tmpToday);
	            end if;
	            if schedItem.REPEATABLE > 0 THEN
	                tmpDaySeconds := RDX_JS_EventSchedule.prevTime(schedItem.STARTTIME * 60, schedItem.ENDTIME * 60, schedItem.PERIOD, schedItem.ENDTIME * 60 + 1);
	            end if;         
	        END IF;
	        
	        if tmpDate is not null then
	            tmpTimestamp := cast(tmpDate as timestamp) + NumToDSInterval(tmpDaySeconds, 'second');
	 
	            if schedItem.timeZoneRegion is not null then
	                tmpTimestamp := RDX_JS_EventSchedule.toTimestampWithTzSafe(tmpTimestamp, schedItem.timeZoneRegion) at time zone sessiontimezone;                     
	            end if;
	            
	            if maxTimestamp is null or maxTimestamp < tmpTimestamp then
	                maxTimestamp := tmpTimestamp;
	            end if;
	        end if;
	    end LOOP;

	    return maxTimestamp;  
	end;
end;
/

