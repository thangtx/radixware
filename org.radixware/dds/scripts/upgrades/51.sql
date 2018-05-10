create or replace package body RDX_JS_CalendarSchedule as

	--constants
	class_guid_abs_calendar CONSTANT VARCHAR(29) := 'aclVV67ZO4QF5GJ7KY7J2AW6UMQUI';
	class_guid_day_of_week CONSTANT VARCHAR(29) := 'aclHUCM56OUWZDBXCTBJLJ2S6QQ7E';
	class_guid_day_of_month CONSTANT VARCHAR(29) := 'aclPJITOKTE6ZDY5OXI7Z53SB222I';
	class_guid_abs_date CONSTANT VARCHAR(29) := 'aclRFJRGJ5REZDHDDTBB5R34QG4GQ';
	class_guid_inc_calendar CONSTANT VARCHAR(29) := 'aclDWOG2C7CWVCWBFOEMH62ET56HI';
	class_guid_daily CONSTANT VARCHAR(29) := 'aclWCDMIFJ755C37LMG7MTQA4DEZQ';
	class_guid_day_of_quarter CONSTANT VARCHAR(29) := 'aclZMU6F5DMMZCHPPRW4E4ISFQZ4U';
	class_guid_day_of_year CONSTANT VARCHAR(29) := 'acl3A5HLW7W7ZFE3E6QYCXAC5CYE4';

	foreread_size CONSTANT INTEGER := 100; --days

	/*
	  ЭЛЕМЕНТЫ КАЛЕНДАРЯ
	*/
	--элемент со свойствами
	TYPE TCalendarItem IS RECORD (
	  classGuid RDX_JS_CALENDARITEM.CLASSGUID%Type,
	  oper RDX_JS_CALENDARITEM.OPER%Type,
	  offsetDir RDX_JS_CALENDARITEM.OFFSETDIR%Type,
	  offset RDX_JS_CALENDARITEM.OFFSET%Type,
	  absDate RDX_JS_CALENDARITEM.ABSDATE%Type,
	  incCalendarId RDX_JS_CALENDARITEM.INCCALENDARID%Type,
	  dayOfWeek int);

	--nested table элементов календаря
	TYPE TItemsNestedTable IS TABLE OF TCalendarItem;
	TYPE TItemsByCalendar IS TABLE OF TItemsNestedTable INDEX BY binary_integer;
	cachedItems TItemsByCalendar;

	/*
	  ЗАКЭШИРОВАННЫЙ ПЕРОИД ДАТ КАЛЕНДАРЯ
	*/
	TYPE TDateById IS TABLE OF DATE INDEX BY binary_integer;
	cachedPeriodsBegin TDateById;
	cachedPeriodsEnd TDateById;

	/*
	  ЗАКЭШИРОВАННЫЕ ДАТЫ КАЛЕНДАРЯ
	*/
	--входит или нет такая дата в календарь (кэш дат)
	TYPE TDateMap is TABLE of BOOLEAN INDEX BY VARCHAR2(10);         -- key='YYYY-MM-DD'
	key_format CONSTANT varchar2(10) := 'YYYY-MM-DD';
	TYPE TDateMapById IS TABLE OF TDateMap INDEX BY binary_integer;
	cachedDates TDateMapById;

	--время последнего обновления таблицы календарей
	lastUpdateTime date;

	function isInCached(
		pId in integer,
		pDate in date
	) return boolean RESULT_CACHE;

	function getDayOfWeek(
		pDate in date
	) return integer deterministic
	is
	    vDayOfWeek int;
	begin
	    vDayOfWeek := TO_NUMBER(TO_CHAR(pDate, 'D'));
	    --в наборе констант ПН=2, ВТ=3, ... ВС=1, а функция возвращает в зависимости от локали
	    vDayOfWeek := (vDayOfWeek + 5 - TO_NUMBER(TO_CHAR(TO_DATE('04-01-2007', 'DD-MM-YYYY'), 'D'))) mod 7;
	    if vDayOfWeek = 0 THEN
	        vDayOfWeek := 7;
	    end if;
	    return vDayOfWeek;
	end;

	function getDaysInMonth(
		pDate in date
	) return integer deterministic
	is
	begin
	    return TO_NUMBER(TO_CHAR(LAST_DAY(pDate), 'DD'));
	end;

	function getDayOfMonth(
		pDate in date
	) return integer deterministic
	is
	begin
	    return TO_NUMBER(TO_CHAR(pDate, 'DD'));
	end;

	function dateToKey(
		pDate in date
	) return varchar2 deterministic
	is
	begin
	    return TO_CHAR(pDate, key_format);
	end;

	function keyToDate(
		pKey in varchar2
	) return date deterministic
	is
	begin
	    return TO_DATE(pKey, key_format);
	end;

	function isDayInQuarter(
		pFromOffset in integer,
		pOffset in integer,
		pDate in date
	) return integer deterministic
	is
	 outDate date;
	begin
	  if pFromOffset>0 then
	    select trunc(pDate,'q')+pOffset-1 into outDate from dual;
	  else 
	    select trunc(add_months(pDate, 3),'q') -pOffset into outDate from dual;
	  end if;
	  
	  if outDate = pDate then
	   return 1;
	  else 
	   return 0;
	  end if; 
	end;

	function isDayInYear(
		pFromOffset in integer,
		pOffset in integer,
		pDate in date
	) return integer deterministic
	is
	outDate date;
	begin
	 if pFromOffset>0 then
	   select trunc(pDate,'YEAR')+pOffset-1 into outDate from dual;
	 else 
	   select trunc(add_months(pDate, 12),'YEAR') -pOffset into outDate from dual;
	 end if;

	 if outDate = pDate then
	  return 1;
	 else 
	  return 0;
	 end if; 
	end;

	procedure doRefreshCache(
		pId in integer
	)
	is
	    CURSOR cCalendarItems (CalId RDX_JS_CALENDARITEM.CALENDARID%TYPE)  is
	        Select ABSDATE, CLASSGUID, INCCALENDARID, OFFSET, OFFSETDIR, OPER
	        From RDX_JS_CALENDARITEM
	        Where CALENDARID = CalId
	        order by SEQ;   
	    sCur varchar2(10);
	    items TItemsNestedTable := TItemsNestedTable();
	    curItem TCalendarItem;
	    vCalendarClassGuid varchar2(30);
	begin
	    select CLASSGUID into vCalendarClassGuid from RDX_JS_CALENDAR where ID = pId;
	    
	    IF vCalendarClassGuid != class_guid_abs_calendar THEN
	        raise_application_error(-20001, 'Wrong calendar type');
	    END IF;

	    FOR vCalendarItem in cCalendarItems(pId) LOOP
	        curItem.classGuid :=  vCalendarItem.CLASSGUID;
	        curItem.oper :=  vCalendarItem.OPER;
	        curItem.offsetDir :=  vCalendarItem.OFFSETDIR;
	        curItem.offset :=  vCalendarItem.OFFSET; 
	        curItem.absDate :=  vCalendarItem.ABSDATE;
	        curItem.incCalendarId :=  vCalendarItem.INCCALENDARID;

	        IF curItem.classGuid = class_guid_day_of_week THEN
	            IF curItem.OffsetDir < 0 THEN
	                curItem.dayOfWeek :=  curItem.offset + 8;
	            ELSE
	                curItem.dayOfWeek :=  curItem.offset;
	            END IF;
	        END IF;

	        items.EXTEND();
	        items(items.LAST) := curItem;
	    END LOOP;

	    cachedItems(pId) := items;
	end;

	procedure refreshCache(
		pId in integer
	)
	is
	    vLastUp date;
	BEGIN
	    IF pId is null THEN
	        raise_application_error(-20002, 'Calendar ID is NULL');
	    END IF;

	    SELECT max(LASTUPDATETIME) INTO vLastUp FROM RDX_JS_CALENDAR;
	    
	    IF lastUpdateTime is null or vLastUp != lastUpdateTime THEN
	        cachedItems.DELETE();
	        cachedPeriodsBegin.DELETE();
	        cachedItems.DELETE();
	        cachedPeriodsEnd.DELETE();
	        cachedDates.DELETE();
	        lastUpdateTime := vLastUp;
	    END IF;

	    IF not cachedItems.EXISTS(pId) THEN
	        RDX_JS_CalendarSchedule.doRefreshCache(pId => pId);
	    END IF;
	end;

	procedure extendSubPeriod(
		pId in integer,
		pBegin in date,
		pEnd in date
	)
	is
	    vItems CONSTANT TItemsNestedTable := cachedItems(pId);
	    vCurDate DATE := pBegin;
	    vDates TDateMap;          
	    vCurItem TCalendarItem;
	    vCounter INT;
	    vResult BOOLEAN;
	    vDayMatches BOOLEAN;
	    vIsInIncCalendar BOOLEAN;
	begin
	    IF cachedDates.EXISTS(pId) THEN
	        vDates := cachedDates(pId);
	    END IF;
	    
	    --dates loop
	    WHILE vCurDate <= pEnd LOOP
	        vResult := null;
	        vDayMatches := null;

	        --items loop
	        vCounter := vItems.FIRST;
	        WHILE vCounter IS NOT NULL LOOP
	            vCurItem := vItems(vCounter);
	            CASE vCurItem.classGuid
	                WHEN class_guid_day_of_year THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.isDayInYear(vCurItem.OffsetDir, vCurItem.Offset, vCurDate)=1;
	            
	                WHEN class_guid_day_of_quarter THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.isDayInQuarter(vCurItem.OffsetDir, vCurItem.Offset, vCurDate)=1;            
	            
	                WHEN class_guid_day_of_week THEN
	                    IF RDX_JS_CalendarSchedule.getDayOfWeek(pDate => vCurDate) = vCurItem.dayOfWeek THEN
	                        vDayMatches := true;
	                    END IF;

	                WHEN class_guid_day_of_month THEN
	                    IF vCurItem.OffsetDir > 0 THEN
	                        vDayMatches := RDX_JS_CalendarSchedule.getDayOfMonth(pDate => vCurDate) = vCurItem.Offset;
	                    ELSE
	                        vDayMatches := vCurItem.Offset = (RDX_JS_CalendarSchedule.getDaysInMonth(pDate => vCurDate) - RDX_JS_CalendarSchedule.getDayOfMonth(pDate => vCurDate) + 1);
	                    END IF;

	                WHEN class_guid_abs_date THEN
	                    vDayMatches := vCurDate = vCurItem.AbsDate;
	                   
	                WHEN class_guid_daily THEN
	                    vDayMatches := true;

	                WHEN class_guid_inc_calendar THEN
	                    vIsInIncCalendar := RDX_JS_CalendarSchedule.isInCached(pId => vCurItem.incCalendarId, pDate => vCurDate);
	                    IF vIsInIncCalendar is not null THEN
	                         vResult := (vCurItem.oper = '+' and vIsInIncCalendar = true) or 
	                         (vCurItem.oper = '-' and 
	                         vIsInIncCalendar = true -- change in RADIX-12059 from 'false' to 'true'
	                         ); 
	                    END IF;
	            END CASE;

	            if vCurItem.classGuid != class_guid_inc_calendar and vDayMatches THEN
	                vResult := vCurItem.Oper = '+';
	            END IF;

	            vCounter := vItems.NEXT(vCounter);
	        END LOOP; --items loop

	        IF vResult IS NOT NULL THEN
	            vDates(RDX_JS_CalendarSchedule.dateToKey(pDate => vCurDate)) := vResult;
	        END IF;

	        vCurDate := vCurDate + 1;

	    END LOOP; --dates loop

	    cachedDates(pId) := vDates;
	end;

	procedure extendCachedPeriod(
		pId in integer,
		pDate in date
	)
	is
	    vThisDayOfWeek INT;
	    vBegin DATE;
	    vNewBegin DATE;
	    vEnd DATE;
	    vNewEnd DATE;
	    vDate DATE;
	    vNeedExtension boolean := false;
	begin
	    RDX_JS_CalendarSchedule.refreshCache(pId => pId);

	    if not cachedPeriodsBegin.EXISTS(pId) THEN
	        vNeedExtension := true;
	    else
	        vBegin := cachedPeriodsBegin(pId);
	        vEnd := cachedPeriodsEnd(pId);
	        if (ABS(vBegin - pDate) < foreread_size) or (ABS(vEnd - pDate) < foreread_size) THEN
	            vNeedExtension := true;
	        end if; 
	    end if;

	    if vNeedExtension THEN
	        vNewBegin := pDate - foreread_size;
	        if (vBegin is null) or (vNewBegin < vBegin) THEN
	            if vBegin is null THEN
	                vDate := pDate;
	            else
	                vDate := vBegin - 1;
	            end if;
	            RDX_JS_CalendarSchedule.extendSubPeriod(pId => pId, pBegin => vNewBegin, pEnd => vDate);
	            vBegin := vNewBegin;	  
	        end if;

	        vNewEnd := pDate + foreread_size;
	        if vEnd is null or vNewEnd > vEnd THEN
	            if vEnd is null THEN
	                vDate := pDate + 1;
	            else
	                vDate := vEnd + 1;
	            end if;
	            RDX_JS_CalendarSchedule.extendSubPeriod(pId => pId, pBegin => vDate, pEnd => vNewEnd);
	            vEnd := vNewEnd;
	        end if;
	        cachedPeriodsBegin(pId) := vBegin;
	        cachedPeriodsEnd(pId) := vEnd;
	    end if;
	end;

	function isInCached(
		pId in integer,
		pDate in date
	) return boolean RESULT_CACHE RELIES_ON(RDX_JS_CALENDARITEM)
	is
	begin
	    IF pDate IS NULL THEN
	        return NULL;
	    END IF;

	    RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => pDate);

	    IF cachedDates.EXISTS(pId)
	            and cachedDates(pId).EXISTS(RDX_JS_CalendarSchedule.dateToKey(pDate => pDate)) THEN
	        return cachedDates(pId)(RDX_JS_CalendarSchedule.dateToKey(pDate => pDate));
	    ELSE
	        return null;
	    END IF;
	end;

	function isIn(
		pId in integer,
		pDate in date
	) return integer
	is
	begin
	    IF RDX_JS_CalendarSchedule.isInCached(pId => pId, pDate => TRUNC(pDate)) = true THEN
	        return 1;
	    ELSE 
	        return 0;
	    END IF;
	end;

	function nextCached(
		pId in integer,
		pDate in date
	) return date RESULT_CACHE RELIES_ON(RDX_JS_CALENDARITEM)
	is
	    vIndex varchar2(10);
	    vDate DATE := pDate;
	begin
	    IF vDate IS NULL THEN
	        return NULL;
	    END IF;
	    
	    RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vDate);

	    IF cachedDates.EXISTS(pId) THEN
	        vIndex := RDX_JS_CalendarSchedule.dateToKey(pDate => vDate);
	        FOR i in 1..1000 LOOP
	            vIndex := cachedDates(pId).NEXT(vIndex);      
	            IF vIndex is null THEN 
	                return null;
	            ELSIF cachedDates(pId)(vIndex) = true THEN
	                return RDX_JS_CalendarSchedule.keyToDate(pKey => vIndex);
	            END IF;
	            vDate := RDX_JS_CalendarSchedule.keyToDate(pKey => vIndex);
	            RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vDate);
	        END LOOP;
	    END IF;

	    return NULL;
	end;

	function next(
		pId in integer,
		pDate in date
	) return date
	is
	begin
	    return RDX_JS_CalendarSchedule.nextCached(pId => pId, pDate => TRUNC(pDate));
	end;

	function nextNotInCached(
		pId in integer,
		pDate in date
	) return date RESULT_CACHE RELIES_ON(RDX_JS_CALENDARITEM)
	is
	    vIndex varchar2(10);
	    vCurDate DATE := pDate;
	begin
	    IF pDate IS NULL THEN
	        return NULL;
	    END IF;

	    RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vCurDate);

	    IF cachedDates.EXISTS(pId) THEN
	        FOR i in 1..1000 LOOP
	            vCurDate := vCurDate + 1;
	            IF RDX_JS_CalendarSchedule.isInCached(pId => pId, pDate => vCurDate) is null or 
	                    RDX_JS_CalendarSchedule.isInCached(pId => pId, pDate => vCurDate) = false THEN
	                return vCurDate;
	            END IF;
	            RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vCurDate);
	        END LOOP;
	    END IF;

	    return null;
	end;

	function nextNotIn(
		pId in integer,
		pDate in date
	) return date
	is
	begin
	    return RDX_JS_CalendarSchedule.nextNotInCached(pId => pId, pDate => TRUNC(pDate));
	end;

	function prevCached(
		pId in integer,
		pDate in date
	) return date RESULT_CACHE RELIES_ON(RDX_JS_CALENDARITEM)
	is
	    vIndex varchar2(10);
	    vDate DATE := pDate;
	begin
	    IF vDate IS NULL THEN
	    return NULL;
	    END IF;

	    RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vDate);

	    IF cachedDates.EXISTS(pId) THEN
	        vIndex := RDX_JS_CalendarSchedule.dateToKey(pDate => vDate); 
	        FOR i in 1..1000 LOOP
	            vIndex := cachedDates(pId).PRIOR(vIndex);      
	            IF vIndex is null THEN 
	                return null;
	            ELSIF cachedDates(pId)(vIndex) = true THEN
	                return RDX_JS_CalendarSchedule.keyToDate(pKey => vIndex);
	            END IF;
	            vDate := RDX_JS_CalendarSchedule.keyToDate(pKey => vIndex);
	            RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vDate);
	        END LOOP;
	    END IF;
	    
	    return NULL;
	end;

	function prev(
		pId in integer,
		pDate in date
	) return date
	is
	begin
	    return RDX_JS_CalendarSchedule.prevCached(pId => pId, pDate => TRUNC(pDate));
	end;

	function prevNotInCached(
		pId in integer,
		pDate in date
	) return date RESULT_CACHE RELIES_ON(RDX_JS_CALENDARITEM)
	is
	    vIndex varchar2(10);
	    vCurDate DATE := pDate;
	begin
	    IF pDate IS NULL THEN
	        return NULL;
	    END IF;

	    RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vCurDate);

	    IF cachedDates.EXISTS(pId) THEN
	        FOR i in 1..1000 LOOP
	            vCurDate := vCurDate - 1;
	            
	            IF RDX_JS_CalendarSchedule.isInCached(pId => pId, pDate => vCurDate) is null or 
	                    RDX_JS_CalendarSchedule.isInCached(pId => pId, pDate => vCurDate) = false THEN
	                return vCurDate;
	            END IF;
	            RDX_JS_CalendarSchedule.extendCachedPeriod(pId => pId, pDate => vCurDate);
	        END LOOP;
	    END IF;

	    return null;
	end;

	function prevNotIn(
		pId in integer,
		pDate in date
	) return date
	is
	begin
	    return RDX_JS_CalendarSchedule.prevNotInCached(pId => pId, pDate => TRUNC(pDate));
	end;
end;
/

