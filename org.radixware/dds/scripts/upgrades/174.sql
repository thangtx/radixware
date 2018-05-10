create or replace package RDX_ACS_Vars as

	sysSuperAdminRoleId  constant varchar2(30) := 'rolSUPER_ADMIN_______________';
	error_Message constant varchar2(30) := 'Incorrect input string - ';
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_ACS_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_Entity as

	function isUserPropValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pPropValType in integer
	) return integer;

	procedure userPropOnDelOwner(
		pEntityID in varchar2,
		pInstancePID in varchar2
	);

	procedure flushUserPropOnDelOwner(
		pEntityID in varchar2
	);

	procedure scheduleUserPropOnDelOwner(
		pEntityID in varchar2,
		pInstancePID in varchar2
	);

	function packPidStr(
		s in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (packPidStr, RNDS);
	PRAGMA RESTRICT_REFERENCES (packPidStr, RNPS);
	PRAGMA RESTRICT_REFERENCES (packPidStr, WNDS);
	PRAGMA RESTRICT_REFERENCES (packPidStr, WNPS);

	procedure delUserPropVal(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pPropValType in integer,
		pIsUpdateAuditOn in integer,
		pAuditSchemeId in varchar2
	);

	function getUserPropInt(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getUserPropInt, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropInt, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropInt, WNPS);

	procedure setUserPropInt(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in integer,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in integer := NULL
	);

	function getUserPropBool(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getUserPropBool, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropBool, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropBool, WNPS);

	procedure setUserPropBool(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in integer,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropNum(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return number;
	PRAGMA RESTRICT_REFERENCES (getUserPropNum, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropNum, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropNum, WNPS);

	procedure setUserPropNum(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in number,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropStr(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (getUserPropStr, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropStr, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropStr, WNPS);

	procedure setUserPropStr(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in varchar2,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropChar(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (getUserPropChar, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropChar, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropChar, WNPS);

	procedure setUserPropChar(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in varchar2,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropClob(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return CLOB;
	PRAGMA RESTRICT_REFERENCES (getUserPropClob, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropClob, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropClob, WNPS);

	procedure setUserPropClob(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in CLOB,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropBlob(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return BLOB;
	PRAGMA RESTRICT_REFERENCES (getUserPropBlob, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropBlob, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropBlob, WNPS);

	procedure setUserPropBlob(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in BLOB,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropBin(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return RAW;
	PRAGMA RESTRICT_REFERENCES (getUserPropBin, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropBin, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropBin, WNPS);

	procedure setUserPropBin(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in RAW,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropDateTime(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return Timestamp;
	PRAGMA RESTRICT_REFERENCES (getUserPropDateTime, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropDateTime, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropDateTime, WNPS);

	procedure setUserPropDateTime(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in timestamp,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	procedure setUserPropRef(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in varchar2,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function getUserPropRef(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (getUserPropRef, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropRef, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropRef, WNPS);

	function getUserPropArrAsStr(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return CLOB;
	PRAGMA RESTRICT_REFERENCES (getUserPropArrAsStr, RNPS);
	PRAGMA RESTRICT_REFERENCES (getUserPropArrAsStr, WNDS);
	PRAGMA RESTRICT_REFERENCES (getUserPropArrAsStr, WNPS);

	procedure setUserPropArrAsStr(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in CLOB,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	);

	function isUserPropNumValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropNumValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropNumValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropNumValDefined, WNPS);

	function isUserPropIntValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropIntValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropIntValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropIntValDefined, WNPS);

	function isUserPropBoolValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropBoolValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropBoolValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropBoolValDefined, WNPS);

	function isUserPropDateTimeValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropDateTimeValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropDateTimeValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropDateTimeValDefined, WNPS);

	function isUserPropStrValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropStrValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropStrValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropStrValDefined, WNPS);

	function isUserPropCharValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropCharValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropCharValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropCharValDefined, WNPS);

	function isUserPropRefValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropRefValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropRefValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropRefValDefined, WNPS);

	function isUserPropBlobValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropBlobValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropBlobValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropBlobValDefined, WNPS);

	function isUserPropClobValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropClobValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropClobValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropClobValDefined, WNPS);

	function isUserPropArrValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isUserPropArrValDefined, RNPS);
	PRAGMA RESTRICT_REFERENCES (isUserPropArrValDefined, WNDS);
	PRAGMA RESTRICT_REFERENCES (isUserPropArrValDefined, WNPS);

	function createPid(
		field in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (createPid, RNDS);
	PRAGMA RESTRICT_REFERENCES (createPid, RNPS);
	PRAGMA RESTRICT_REFERENCES (createPid, WNDS);
	PRAGMA RESTRICT_REFERENCES (createPid, WNPS);

	function createPid(
		field in number
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (createPid, RNDS);
	PRAGMA RESTRICT_REFERENCES (createPid, RNPS);
	PRAGMA RESTRICT_REFERENCES (createPid, WNDS);
	PRAGMA RESTRICT_REFERENCES (createPid, WNPS);

	function createPid(
		field in timestamp
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (createPid, RNDS);
	PRAGMA RESTRICT_REFERENCES (createPid, RNPS);
	PRAGMA RESTRICT_REFERENCES (createPid, WNDS);
	PRAGMA RESTRICT_REFERENCES (createPid, WNPS);

	function addField2Pid(
		currentPid in varchar2,
		field in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (addField2Pid, RNDS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, RNPS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, WNDS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, WNPS);

	function addField2Pid(
		currentPid in varchar2,
		field in number
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (addField2Pid, RNDS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, RNPS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, WNDS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, WNPS);

	function addField2Pid(
		currentPid in varchar2,
		field in timestamp
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (addField2Pid, RNDS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, RNPS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, WNDS);
	PRAGMA RESTRICT_REFERENCES (addField2Pid, WNPS);

	procedure userPropOnDelValue(
		pEntityId in varchar2,
		pValuePid in varchar2
	);
end;
/

grant execute on RDX_Entity to &USER&_RUN_ROLE
/

create or replace package RDX_Entity_Vars as

	type TScheduledOnDelUpOwner is record (
	   sOwnerEntityId  varchar2(100),
	   sOwnerPid  varchar2(250)
	);
	type TScheduledOnDelUpOwnerQueue is table of TScheduledOnDelUpOwner index by binary_integer;
	scheduledOnDelUpOwnerQueue TScheduledOnDelUpOwnerQueue;

	TYPE StrByInt IS TABLE OF varchar2(100) INDEX BY BINARY_INTEGER;
	upValTabByValType StrByInt;
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_Entity_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_JS_Vars as

	TASK_EXECUTOR_CLASS_NAME constant varchar2(200) := 'org.radixware.ads.mdlOWL3XYSM2ZDNHOW7W7XKT54GRE.server.adc2YO4JA7JKJGGBLTARIEQHUSHGI';
	TASK_EXECUTOR_METHOD_NAME constant varchar2(200) := 'mthA4SZ4ODO4NFL3A5KMYIJ54MTZA';

	class_guid_abs_calendar CONSTANT VARCHAR(29) := 'aclVV67ZO4QF5GJ7KY7J2AW6UMQUI';
	class_guid_day_of_week CONSTANT VARCHAR(29) := 'aclHUCM56OUWZDBXCTBJLJ2S6QQ7E';
	class_guid_day_of_month CONSTANT VARCHAR(29) := 'aclPJITOKTE6ZDY5OXI7Z53SB222I';
	class_guid_abs_date CONSTANT VARCHAR(29) := 'aclRFJRGJ5REZDHDDTBB5R34QG4GQ';
	class_guid_inc_calendar CONSTANT VARCHAR(29) := 'aclDWOG2C7CWVCWBFOEMH62ET56HI';
	class_guid_daily CONSTANT VARCHAR(29) := 'aclWCDMIFJ755C37LMG7MTQA4DEZQ';
	class_guid_day_of_quarter CONSTANT VARCHAR(29) := 'aclZMU6F5DMMZCHPPRW4E4ISFQZ4U';
	class_guid_day_of_year CONSTANT VARCHAR(29) := 'acl3A5HLW7W7ZFE3E6QYCXAC5CYE4';

	/*
	  ЭЛЕМЕНТЫ КАЛЕНДАРЯ
	*/
	--элемент со свойствами
	TYPE TCalendarItem IS RECORD (
	  classGuid varchar2(29), -- RDX_JS_CALENDARITEM.CLASSGUID%Type,
	  oper char(1), -- RDX_JS_CALENDARITEM.OPER%Type,
	  offsetDir int, -- RDX_JS_CALENDARITEM.OFFSETDIR%Type,
	  offset int, -- RDX_JS_CALENDARITEM.OFFSET%Type,
	  absDate date, -- RDX_JS_CALENDARITEM.ABSDATE%Type,
	  incCalendarId int, --RDX_JS_CALENDARITEM.INCCALENDARID%Type,
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
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_JS_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_Sys_Vars as

	userName varchar2(250);
	stationName varchar2(250);
	clientLanguage varchar2(3);
	clientCountry varchar2(3);
	defVersion number;
	sessionId number;

	--filled after creating jdbc connection
	instanceId integer;
	sessionOwnerType varchar2(100);
	sessionOwnerId integer;
	--other
	targetExecutorId integer := -1;
	requestInfo varchar2(500);

	SUSPENSION_TAG constant RAW(10) := HEXTORAW('AADC0000');
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_Sys_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_WF_Vars as

	DEBUG_ROLE CONSTANT VARCHAR2(50) := 'rol7MTXJKWQORCJFD7EEZHGZ7TOJM';
	ADMIN_ROLE CONSTANT VARCHAR2(50) := 'rolHM6FIFA4JFCJFIYPB3P2KC435U';
	CLERK_ROLE CONSTANT VARCHAR2(50) := 'rolOYMAGGZIXNF6TMGZASCZET6VOI';
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_WF_Vars to &USER&_RUN_ROLE
/

create or replace package body RDX_ACS as

	Type TIdRecord IS RECORD (IdValue varchar(50));
	Type TIdRecordList is table of TIdRecord index by binary_integer;

	function packAccessPartitionValue(
		val in varchar2
	) return varchar2
	is
	res varchar2(32767);
	begin 
	  res := Replace(val, '\',  '\\');
	  res := Replace(res, ')',  '\)');
	  return res;
	end;

	function contentId(
		roleId in varchar2,
		list in TIdRecordList
	) return integer
	is
	begin

	 if (list.first() is not null) then
	   for i in list.first() .. list.last() 
	   loop 
	     if list.exists(i) and list(i).IdValue = roleid then         
	        return 1;
	     end if;
	   end loop;                               
	 end if;
	 return 0;
	end;

	function unPackAccessPartitionValue(
		val in varchar2
	) return varchar2
	is
	res varchar2(32767);
	begin
	  res := Replace(val, '\)', ')');
	  res := Replace(res, '\\', '\');
	  return res;
	end;

	procedure clearInheritRights(
		pUserGroup in varchar2
	)
	is
	begin
	    delete from RDX_AC_USER2ROLE u2r where u2r.isOwn = 0 and username in (select username from RDX_AC_USER2USERGROUP where GroupName=pUserGroup);
	end;

	-- area_           - область установленная в таблице user2role
	-- largerArea_  - область сформированная сервером для текущей таблицы
	-- largerArea_    не содержит  координат с флагом запрещено.
	-- 
	-- Если для каждого семейства из области largerArea_ найдется семейство с таким же не 
	--   заприщенным разделом в области area_ или в области area_ для этого семейства нет 
	--   ограничений то вернуть 1
	-- иначе 0
	function containsPointInArea(
		area_ in TRdxAcsArea,
		largerArea_ in TRdxAcsArea
	) return boolean
	is   
	     n1        boolean;
	     n2        boolean;  
	     largerAreaSize integer;
	     areaSize       integer;
	     sFamilyID varchar2(50); 
	begin
	                                                    
	   
	   if (largerArea_ is null) then 
	     return true;         
	   end if;
	   largerAreaSize  := largerArea_.boundaries.COUNT();   
	   if (largerAreaSize = 0) then 
	     return true;
	   end if;                              
	 
	   areaSize := area_.boundaries.COUNT();
	 
	   For i in 1 .. largerAreaSize
	     loop
	       sFamilyID := largerArea_.boundaries(i).FamilyID; 
	      n1 := largerArea_.boundaries(i).KeyVal is NULL;
	      <<label1>> for j in 1 .. areaSize
	      loop
	         if sFamilyID = area_.boundaries(j).FamilyID then
	           if (area_.boundaries(j).Prohibited = 1)then
	              return false;
	           end if;
	           n2 := area_.boundaries(j).KeyVal is null; 
	           if (
	               (n1 and n2) or
	                (
	                 (not n1) and
	                 (not n2) and
	                 (largerArea_.boundaries(i).KeyVal = area_.boundaries(j).KeyVal)
	                )
	               ) then
	              exit label1;
	           end if;
	           return false;
	         end if;
	      end loop; 
	     end loop;  
	   return true; 
	end;

	-- return (largerArea_ объемлит area_)
	function containsPointInArea2(
		largerArea_ in TRdxAcsArea,
		area_ in TRdxAcsArea
	) return boolean
	is   
	    n1        boolean;
	    n2        boolean;
	    find      boolean;
	    largerAreaSize integer;
	    areaSize       integer;
	    sFamilyID varchar2(50);
	begin
	    if (largerArea_ is null) then -- не партиционируемые права
	     return true;         
	    end if;
	    largerAreaSize      := largerArea_.boundaries.COUNT();   
	    if (largerAreaSize = 0) then -- Пустая коллекция, значит включает все пространство
	     return true;
	    end if;                              
	    if (area_ is null) then 
	     return false;         
	    end if;
	    areaSize := area_.boundaries.COUNT();
	    if (areaSize = 0) then
	     return false;
	    end if;

	    For i in 1 .. largerAreaSize
	     Loop
	       sFamilyID := largerArea_.boundaries(i).FamilyID;
	       if (largerArea_.boundaries(i).Prohibited = 0) then         
	         n1 := largerArea_.boundaries(i).KeyVal is NULL;
	         find := false;
	         <<label1>> for j in 1 .. areaSize 
	           loop
	             if sFamilyID = area_.boundaries(j).FamilyID then
	               n2 := area_.boundaries(j).KeyVal is null; 
	               if (
	                   (area_.boundaries(j).Prohibited = 1)or
	                   (n1 and n2) or 
	                    (
	                     (not n1) and 
	                     (not n2) and 
	                     (largerArea_.boundaries(i).KeyVal = area_.boundaries(j).KeyVal)
	                    )  
	                  ) then
	                  begin       
	                  find := true;
	                  exit label1;
	                  end;
	               end if;
	               return false;
	             end if;
	           end loop;
	           if not find then
	             return false;
	           end if;
	       else
	         n2 := false;
	         <<label2>>for j in 1 .. areaSize 
	                    loop
	                      if (sFamilyID = area_.boundaries(j).FamilyID) then  
	                         if (area_.boundaries(j).Prohibited = 1) then
	                            n2 := true;
	                            exit label2;
	                         else
	                           return false;
	                         end if;
	                      end if;
	                    end loop; 
	         if not n2 then
	           return false;
	         end if;
	       end if;
	     end loop;   
	    return true;
	 end;

	function userHasRoleForObjectInternal(
		pUser in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea
	) return integer
	is
	begin
	  return RDX_ACS.userHasRoleForObjectInternal(pUser , pRole , pArea , 1, null, null);
	end;

	function strToArea(
		str in varchar2,
		pos in out integer
	) return TRdxAcsArea
	is 
	    res TRdxAcsArea; 
	    prohibited integer;
	    newPos integer;
	    newPos2 integer;
	    newPos3 integer;
	    accessPartitionKey varchar2(30);
	    accessPartitionValue varchar2(32767);
	--    str varchar2(32767); 
	begin

	--z := s;
	--unpuck
	--    str :=RDX_ACS.unPackAccessPartitionValue(str_);
	--

	if SubStr(str, pos, 1)<>'(' then
	  RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message);
	end if; 
	pos:=pos+1;
	res := TRdxAcsArea(TRdxAcsCoordinates());
	<<lbl1>>while(true)
	    loop
	      if SubStr(str , pos, 1)=')' then
	         exit lbl1;
	      end if;
	      if SubStr(str , pos, 1)<>'(' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message || str);
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)='0' then
	         prohibited := 0;
	      else 
	        if SubStr(str , pos, 1)='1' then
	           prohibited := 1;
	        else
	           RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message || str);
	        end if;
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message || str);
	      end if;
	      pos:=pos+1;
	      accessPartitionKey := SubStr(str, pos,29); 
	      pos:=pos+29;
	      if SubStr(str, pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message || str);
	      end if;
	      pos := pos+1;
	      newPos:=InStr(str, ')', pos);
	      if newPos = 0 then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message || str);
	      end if;      
	      if newPos = pos then
	         accessPartitionValue := null;
	      else
	      
	      <<lbl2>>while (true)
	         loop
	         --\\\)\))
	         if (SubStr(str, newPos-1, 1)='\')then
	             newPos2 := newPos-2;
	             <<lbl3>>while (true)
	                loop
	                if (SubStr(str, newPos2, 1)<>'\')then
	                  exit lbl3;  
	                end if;
	                if (SubStr(str, newPos2-1, 1)<>'\')then
	                  exit lbl2;  
	                end if;
	                newPos2 := newPos2-2;
	                end loop;
	                newPos := newPos+1;
	                newPos:=InStr(str, ')', newPos);
	         else
	            exit lbl2;
	         end if;         
	         end loop;
	         accessPartitionValue := RDX_ACS.unPackAccessPartitionValue(SubStr(str, pos, newPos-pos));
	         
	         
	      end if;
	      pos := newPos+ 1;
	      res.boundaries.EXTEND();
	      res.boundaries(res.boundaries.COUNT()):=TRdxAcsCoordinate(prohibited, accessPartitionKey, accessPartitionValue);
	    end loop;
	return res;
	end;

	function userHasRoleForObjectInternal(
		pUser in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea,
		checkInheritGroupRights in integer,
		exceptingID1_ in integer,
		exceptingID2_ in integer
	) return integer
	is
	    Area TRdxAcsArea;
	    exceptingID1 integer;
	    exceptingID2 integer;
	begin 
	     exceptingID1 := nvl(exceptingID1_, -1);
	     exceptingID2 := nvl(exceptingID2_, -1);
	     
	   -- if (exceptingID1 is not null)then
	      begin       
	      --if (checkInheritGroupRights<>0)then
	          begin   
	          for ind in (Select * from RDX_AC_USER2ROLE
	                     where
	                      --(checkOnlyOld=0 or (checkOnlyOld<>0 and isNew=0)) and 
	                      isNew=0 and
	                      (checkInheritGroupRights<>0 or (checkInheritGroupRights=0 and isOwn <> 0)) and
	                      exceptingID1<>Id and 
	                      exceptingID2<>Id and 
	                      userName = pUser and  
	                      pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	             loop
	                 Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	                 if RDX_ACS.containsPointInArea2(Area, pArea) then
	                    return 1;
	                 end if;
	             end loop;
	          end;
	          
	         /* 
	      else
	          begin
	          for ind in (Select * from RDX_AC_USER2ROLE
	                     where isNew=0 and exceptingID1<>Id and exceptingID2<>Id and isOwn <> 0 and userName = pUser and  pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	             loop
	                 Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	                 if RDX_ACS.containsPointInArea2(Area, pArea) then
	                    return 1;
	                 end if;
	             end loop;
	          end;
	      end if;
	      */
	      end;
	    /*  
	    else
	      begin  
	      if (checkInheritGroupRights<>0)then
	          begin   
	          for ind in (Select * from RDX_AC_USER2ROLE
	                     where  userName = pUser and  pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	             loop
	                 Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	                 if RDX_ACS.containsPointInArea2(Area, pArea) then
	                    return 1;
	                 end if;
	             end loop;
	          end;
	      else
	          begin
	          for ind in (Select * from RDX_AC_USER2ROLE
	                     where isNew=0 and isOwn <> 0 and userName = pUser and  pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	             loop         
	                 Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	                 if RDX_ACS.containsPointInArea2(Area, pArea) then
	                    return 1;
	                 end if;
	             end loop;
	          end;
	      end if;
	      end;               
	    end if;
	    */
	    return 0;
	end;

	function groupHasRoleForObjectInternal(
		pGroup in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea
	) return integer
	is      
	begin          
	    return RDX_ACS.groupHasRoleForObjectInternal(pGroup , pRole , pArea ,null, null);
	end;

	function groupHasRoleForObjectInternal(
		pGroup in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea,
		exceptingID1_ in integer,
		exceptingID2_ in integer
	) return integer
	is      
	    Area TRdxAcsArea;   
	    exceptingID1 integer;
	    exceptingID2 integer;
	begin 
	     exceptingID1 := nvl(exceptingID1_, -1);
	     exceptingID2 := nvl(exceptingID2_, -1);

	    --if (exceptingID1 is not null) then
	    
	      begin
	      for ind in (Select * from RDX_AC_USERGROUP2ROLE
	                 where 
	                    --(checkOnlyOld=0 or (checkOnlyOld<>0 and isNew=0)) and 
	                    isNew=0 and
	                    exceptingID1<>id and 
	                    exceptingID2<>id and 
	                    groupName = pGroup and  
	                    pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	         loop
	             Area := RDX_ACS_UTILS.buildAssignedAccessAreaG2R(ind);
	             if RDX_ACS.containsPointInArea2(Area, pArea) then
	                return 1;
	             end if;
	         end loop;     
	      end;
	    
	    /*  
	    else
	      begin
	      for ind in (Select * from RDX_AC_USERGROUP2ROLE
	                   where isNew=0 and  groupName = pGroup and  pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	           loop
	               Area := RDX_ACS_UTILS.buildAssignedAccessAreaG2R(ind);
	               if RDX_ACS.containsPointInArea2(Area, pArea) then
	                  return 1;
	               end if;
	           end loop;
	      end;
	    end if;
	    */
	    return 0;
	end;

	function curUserHasRoleInArea(
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	begin 
	  return RDX_ACS.userHasRoleInArea(RDX_Arte.getUserName(), pRole, pPointList); 
	end;

	function userHasExplicitRoleInArea(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	    Area TRdxAcsArea; 
	    flag boolean;
	    dd integer;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and  userName = pUser and  pRole = roleId)
	     loop
	       if flag then
	          return 1;  
	       end if;
	       Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	       for i in pPointList.FIRST()..pPointList.LAST()
	         loop
	           if RDX_ACS.containsPointInArea2(Area, pPointList(i)) then
	              return 1;
	           end if;                                          
	         end loop;  
	     end loop;   
	    return 0;
	    exception 
	      when others then 
	      RAISE_APPLICATION_ERROR (
	         -20100, 
	         'raise exception detected - ' || SQLERRM || ' code = ' ||
	         TO_CHAR(SQLCODE) || ' in userHasRoleInArea(' ||
	         pUser || ', ' || pRole ||  ', ' || RDX_ACS.areaListToStr(pPointList) || ')' 
	         );  
	         null;
	end;

	function userHasRoleInArea(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	    Area TRdxAcsArea; 
	    flag boolean;
	    dd integer;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and  userName = pUser and  pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	     loop
	       if flag then
	          return 1;  
	       end if;
	       Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	       for i in pPointList.FIRST()..pPointList.LAST()
	         loop
	           if RDX_ACS.containsPointInArea2(Area, pPointList(i)) then
	              return 1;
	           end if;                                          
	         end loop;  
	     end loop;   
	    return 0;
	    exception 
	      when others then 
	      RAISE_APPLICATION_ERROR (
	         -20100, 
	         'raise exception detected - ' || SQLERRM || ' code = ' ||
	         TO_CHAR(SQLCODE) || ' in userHasRoleInArea(' ||
	         pUser || ', ' || pRole ||  ', ' || RDX_ACS.areaListToStr(pPointList) || ')' 
	         );  
	         null;
	end;

	function curUserHasAnyOfRolesInArea(
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.curUserHasRoleInArea(roles(idx), pPointList) != 0 then
	              return 1;
	           end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS_Vars.sysSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	function userHasAnyOfRolesInArea(
		pUser in varchar2,
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.userHasRoleInArea(pUser, roles(idx), pPointList) != 0 then
	              return 1;
	           end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS_Vars.sysSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	function curUserHasRoleForObject(
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	begin 
	    return RDX_ACS.userHasRoleForObject(RDX_Arte.getUserName(), pRole, pPointList); 
	end;

	function userHasRoleForObject(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer
	is     
	    Area TRdxAcsArea; 
	    flag boolean;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and userName = pUser and  pRole || RDX_ACS_Vars.sysSuperAdminRoleId LIKE '%' || roleId || '%')
	     loop
	       if flag then
	          return 1;  
	       end if;
	       Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	       for i in pPointList.FIRST()..pPointList.LAST()
	         loop
	           if RDX_ACS.containsPointInArea(Area, pPointList(i)) then
	              return 1;
	           end if;
	         end loop; 
	     end loop;        
	    return 0;
	end;

	function userHasAnyOfRolesForObject(
		pUser in varchar2,
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.userHasRoleInArea(pUser, roles(idx), pPointList) != 0 then
	              return 1;
	            end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS_Vars.sysSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	function curUserHasAnyOfRolesForObject(
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer
	is
	    roles RDX_Array.ARR_STR;
	begin
	    roles := RDX_Array.fromStr(pRolesArr);    
	    if roles.COUNT > 0 then
	        for idx in roles.FIRST .. roles.LAST loop
	           if RDX_ACS.curUserHasRoleForObject(roles(idx), pPointList) != 0 then
	              return 1;
	           end if;
	        end loop;
	    else
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS_Vars.sysSuperAdminRoleId, pPointList);
	    end if;
	    return 0;
	end;

	-- Принадлежит ли пользователь заданной группе
	function userIsInGroup(
		pUser in varchar2,
		pGroup in varchar2
	) return integer
	is
	    rez integer(1);
	begin     
	    rez:=0;
	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2USERGROUP WHERE ROWNUM <= 1 AND userName = pUser and groupName = pGroup;
	    return rez;
	end;

	-- Принадлежит ли текущий пользователь заданной группе
	function curUserIsInGroup(
		pGroup in varchar2
	) return integer
	is    
	begin  
	  return RDX_ACS.userIsInGroup(RDX_Arte.getUserName(), pGroup);
	end;

	function areaListToStr(
		areaList_ in TRdxAcsAreaList
	) return varchar2
	is
	    result_  varchar2(32767);   
	    size_    integer;
	begin
	     if areaList_ is null then
	        return null;
	     end if;
	     size_ := areaList_.COUNT(); 
	     result_ := '(';
	     for i in 1 .. size_
	       Loop 
	           result_ := result_ || RDX_ACS.areaToStr(areaList_(i));
	           if i=size_ then
	             result_ := result_ || ')'; 
	           end if;            
	       end loop;   
	    return result_;
	end;

	function areaToStr(
		area_ in TRdxAcsArea
	) return varchar2
	is
	    result_  varchar2(32767);   
	    size_    integer;
	begin
	    if area_ is null then
	      return null;
	    end if;
	    size_ := area_.boundaries.COUNT();

	    result_ := '(';
	    for i in 1 .. size_
	       Loop 
	           result_ := result_ || '(';
	           result_ := result_ || TO_CHAR(area_.boundaries(i).Prohibited) || ',';
	           result_ := result_ || area_.boundaries(i).FamilyID   || ',';
	           if area_.boundaries(i).KeyVal is not null then
	               result_ := result_ || RDX_ACS.packAccessPartitionValue(area_.boundaries(i).KeyVal);
	           end if;
	           result_ := result_ || ')'; 
	       end loop;   
	    result_ := result_ || ')';
	    return result_;
	end;

	procedure acsUtilsBuild
	is 
	    type str_list_type  is table of varchar2(50) index BY BINARY_INTEGER;
	    str_list str_list_type;
	    str_list2 str_list_type;
	 
	    cur_index  integer;
	    n  integer;
	    i  integer;

	    cursor_name INTEGER;
	    cursor_name2 INTEGER;
	    ret	    INTEGER;

	    x varchar2(32767);

	    --head_ clob;
	    body_ clob;

	    t1 varchar2(32767);
	    t2 varchar2(32767);
	 
	begin

	    cur_index := 1;
	    --dbms_lob.createTemporary(head_, false, dbms_lob.SESSION);
	    dbms_lob.createTemporary(body_, false, dbms_lob.SESSION);


	    for ind in (select * from user_tab_columns where  
	                  table_name = 'RDX_AC_USER2ROLE' 
	                and
	                  length(column_name) = 30 
	                and 
	                  column_name like 'PA$$%'
	               )                  
	    loop
	        x:=ind.column_name;
	        x:= SUBSTR(x, 2);
	        str_list(cur_index):=x;

	        x:=ind.column_name;
	        x:= 'apf' || SUBSTR(x, 5);
	        str_list2(cur_index):=x;

	        cur_index := cur_index+1;
	    end loop;
	 


	    -- body_ 
	    n := str_list.COUNT();



	    x:='CREATE OR REPLACE package body RDX_ACS_UTILS as' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	    -- compileRights
	    t1 := '        INSERT INTO RDX_AC_USER2ROLE ( userName, isOwn, RoleId, ID';
	    t2 := '               VALUES ( ind2.userName, 0, ind1.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';

	    For i in 1 .. n
	    loop 
	        t1 := t1 || ', P' || str_list(i) || ', M' || str_list(i);
	        t2 := t2 || ', IND1.P' || str_list(i) || ', IND1.M' || str_list(i);
	    end loop;


	    x:='procedure compileRights' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='is' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='begin' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='delete from RDX_AC_USER2ROLE u2r where u2r.isOwn = 0;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='for ind1 in (Select * from RDX_AC_USERGROUP2ROLE where isNew<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      for ind2 in (select * from RDX_AC_USER2USERGROUP where GroupName=ind1.GroupName and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='        loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	    x:='        ' || t1 || ')' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 
	    x:='        ' || t2 || ');' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 

	    x:='        end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);



	    --compileRightsForGroup

	    t1 := '        INSERT INTO RDX_AC_USER2ROLE ( userName, isOwn, RoleId, ID';
	    t2 := '               VALUES ( ind1.userName, 0, ind3.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';

	    For i in 1 .. n
	    loop 
	        t1 := t1 || ', P' || str_list(i) || ', M' || str_list(i);
	        t2 := t2 || ', IND3.P' || str_list(i) || ', IND3.M' || str_list(i);
	    null;
	    end loop;

	    x:='procedure compileRightsForGroup(' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   pUserGroup    in varchar2)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='is' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='begin' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='RDX_ACS.ClearInheritRights(pUserGroup);' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='for ind1 in (select * from RDX_AC_USER2USERGROUP where GroupName=pUserGroup and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='for ind2 in (select * from RDX_AC_USER2USERGROUP where UserName=ind1.UserName and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   for ind3 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind2.GroupName and isNew<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='     loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	    x:='        ' || t1 || ')' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 
	    x:='        ' || t2 || ');' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 

	    x:='   end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);


	    --compileRightsForUser 
	    t1 := '      INSERT INTO RDX_AC_USER2ROLE ( userName, isOwn, RoleId, ID';
	    t2 := '               VALUES (pUser, 0, ind3.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';

	    For i in 1 .. n
	    loop 
	        t1 := t1 || ', P' || str_list(i) || ', M' || str_list(i);
	        t2 := t2 || ', IND3.P' || str_list(i) || ', IND3.M' || str_list(i);
	    null;
	    end loop;

	    x:='procedure compileRightsForUser(' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   pUser in varchar2)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='is' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='begin' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='delete from RDX_AC_USER2ROLE u2r where u2r.isOwn = 0 and username = pUser and isNew<>1;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='for ind1 in (select * from RDX_AC_USER2USERGROUP where UserName=pUser and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='    for ind3 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind1.GroupName and isNew<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='    loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	    x:='        ' || t1 || ')' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 
	    x:='        ' || t2 || ');' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 

	    x:='    end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);



	    -- procedure compileRightsForGrpBeforeDel
	    t1 := '      INSERT INTO RDX_AC_USER2ROLE ( userName, isOwn, RoleId, ID';
	    t2 := '               VALUES (ind1.userName, 0, ind3.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';


	    For i in 1 .. n
	    loop 
	        t1 := t1 || ', P' || str_list(i) || ', M' || str_list(i);
	        t2 := t2 || ', IND3.P' || str_list(i) || ', IND3.M' || str_list(i);
	    null;
	    end loop;

	    x:='procedure compileRightsForGrpBeforeDel(' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='   pUserGroup    in varchar2)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='is' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='begin' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='RDX_ACS.ClearInheritRights(pUserGroup);' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='for ind1 in (select * from RDX_AC_USER2USERGROUP where GroupName=pUserGroup and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  for ind2 in (select * from RDX_AC_USER2USERGROUP where UserName=ind1.UserName and GroupName<>pUserGroup and state<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      for ind3 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind2.GroupName and isNew<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='      loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	    x:='        ' || t1 || ')' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 
	    x:='        ' || t2 || ');' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x); 


	    x:='      end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='  end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='end;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);


	    for i in 1 .. 2 
	    loop
	       if i = 1  then
	          x:='function buildAssignedAccessAreaU2R(row_ in RDX_AC_USER2ROLE%ROWTYPE)return TRdxAcsArea' || CHR(13);
	       else
	          x:='function buildAssignedAccessAreaG2R(row_ in RDX_AC_USERGROUP2ROLE%ROWTYPE)return TRdxAcsArea' || CHR(13);
	       end if;
	       DBMS_LOB.writeappend(body_, length(x), x);

	       x:='Is' || CHR(13);
	       DBMS_LOB.writeappend(body_, length(x), x);
	       x:='res TRdxAcsArea;' || CHR(13);
	       DBMS_LOB.writeappend(body_, length(x), x);
	       x:='Begin' || CHR(13);
	       DBMS_LOB.writeappend(body_, length(x), x);
	       x:='res := TRdxAcsArea(TRdxAcsCoordinates());' || CHR(13);
	       DBMS_LOB.writeappend(body_, length(x), x);

	       for cur_index in 1 .. n 
	       loop 
	            x:='      if row_.M' || str_list(cur_index) || '<>RDX_ACS.cRight_unbounded then' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='        res.boundaries.EXTEND();' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='        if row_.M' || str_list(cur_index) || ' = RDX_ACS.cRight_prohibited  then' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='            res.boundaries(res.boundaries.Count()):=TRdxAcsCoordinate(1, ' || CHR(39) || str_list2(cur_index) || CHR(39) || ', null);' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='        else' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='            res.boundaries(res.boundaries.Count()):=TRdxAcsCoordinate(0, ' || CHR(39) || str_list2(cur_index) || CHR(39) || ', row_.P' || str_list(cur_index) || ');' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='        end if;' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	            x:='      end if;' || CHR(13);
	            DBMS_LOB.writeappend(body_, length(x), x);
	       end loop;
	       x:=' return res;' || CHR(13);
	       DBMS_LOB.writeappend(body_, length(x), x);

	       x:='end;' || CHR(13);
	       DBMS_LOB.writeappend(body_, length(x), x);

	    end loop;
	    
	    
	    --moveRightsFromUserToGroup
	    x:='procedure moveRightsFromUserToGroup(' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='	user_ in varchar2,' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='	group_ in varchar2)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='is' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='begin' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:=' INSERT INTO  RDX_AC_USERGROUP  (name) VALUES (group_);' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:=' INSERT INTO  RDX_AC_USER2USERGROUP  (userName, groupName) VALUES (user_, group_);' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:=' for ind in (Select * from  RDX_AC_USER2ROLE where userName = User_ and  isOwn = 1 and isNew<>1)' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='     loop' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    
	    t1:='INSERT INTO RDX_AC_USERGROUP2ROLE (ID, GroupName, roleId';
	    t2:=') VALUES ( SQN_RDX_AC_USERGROUP2ROLEID.NEXTVAL, group_, IND.roleId';
	    
	    For i in 1 .. n
	    loop 
	        t1 := t1 || ', P' || str_list(i) || ', M' || str_list(i);
	        t2 := t2 || ', IND.P' || str_list(i) || ', IND.M' || str_list(i);
	    end loop;
	    x:= t1 || t2 || ');' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);    
	    
	    x:='      DELETE FROM RDX_AC_USER2ROLE WHERE id=ind.id;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    x:='     end loop;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);    
	    
	    x:='end;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);
	    ----------------------------
	    

	    x:='End;' || CHR(13);
	    DBMS_LOB.writeappend(body_, length(x), x);

	   

	    cursor_name := DBMS_SQL.OPEN_CURSOR;
	    DBMS_SQL.PARSE ( cursor_name, body_, DBMS_SQL.NATIVE );
	    ret := DBMS_SQL.EXECUTE ( cursor_name );
	    DBMS_SQL.CLOSE_CURSOR ( cursor_name );
	  
	end;

	function curUserAllRolesInAllAreas return varchar2
	is
	begin   
	    return RDX_ACS.userAllRolesInAllAreas(RDX_Arte.getUserName());  
	end;

	function userAllRolesInAllAreas(
		pUser in varchar2
	) return varchar2
	is
	    vRole  varchar2(30);
	    vRoles varchar2(4000);
	    Area TRdxAcsArea;
	begin
	    vRoles := '';
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where isNew=0 and  username = pUser)
	       loop
	          if vRoles is NULL then
	             vRoles := ind.ROLEID;
	          else
	             vRoles := vRoles || ',' || ind.ROLEID;
	          end if;
	       end loop; 
	    return vRoles;  
	end;

	function getCurUserAllRolesForObject(
		pPointList in TRdxAcsAreaList
	) return varchar2
	is
	begin   
	    return RDX_ACS.getAllRolesForObject(RDX_Arte.getUserName(), pPointList);  
	end;

	function getAllRolesForObject(
		pUser in varchar2,
		pPointList in TRdxAcsAreaList
	) return varchar2
	is
	    vRole  varchar2(30);
	    vRoles varchar2(4000);
	    Area TRdxAcsArea;
	    rightsList TIdRecordList;
	    curIndex integer;
	begin                                
	    vRoles := '';
	    if pPointList is null or pPointList.COUNT=0 then
	    begin             
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where isNew=0 and  username = pUser)
	       loop
	           if (RDX_ACS.contentId(ind.ROLEID, rightsList)=0) then

	               curIndex := rightsList.count()+1;
	               rightsList(curIndex).IdValue := ind.ROLEID;


	               if vRoles is NULL then
	                  vRoles := ind.ROLEID;
	               else
	                  vRoles := vRoles || ',' || ind.ROLEID;
	               end if;
	           end if;
	       end loop;
	    end;
	    else
	     begin                 
	     for ind in (select distinct * from RDX_AC_USER2ROLE where isNew=0 and  userName = pUser )
	        loop
	        Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	        for i in pPointList.FIRST..pPointList.LAST
	           loop
	              if RDX_ACS.containsPointInArea(Area, pPointList(i)) then
	                 begin
	                    if (RDX_ACS.contentId(ind.ROLEID, rightsList)=0) then

	                        curIndex := rightsList.count()+1;
	                        rightsList(curIndex).IdValue := ind.ROLEID;
	                        if vRoles is null then
	                           vRoles := ind.ROLEID;
	                        else
	                           vRoles := vRoles || ',' || ind.ROLEID;
	                        end if;
	                        EXIT;
	                     end if;
	                 end;
	              end if;
	           end loop;
	        end loop;
	     end;
	    end if;
	    return vRoles; 
	end;

	-- Имеет ли текущий пользователь права не меньшие чем запись в таблице User2Role с заданным идом
	function curUserHasRightsU2RId(
		ID_ in integer
	) return integer
	is
	    Area TRdxAcsArea;
	    Area2 TRdxAcsArea;
	begin
	    for ind in (select * from RDX_AC_USER2ROLE where id = id_)
	        loop
	          Area := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind);
	          for ind2 in (select * from RDX_AC_USER2ROLE where 
	                       isNew=0 and userName = RDX_Arte.getUserName() and               
	                       (roleid = ind.roleid or roleid = RDX_ACS_Vars.sysSuperAdminRoleId)
	                      )
	            loop
	                Area2 := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind2);
	                if RDX_ACS.containsPointInArea2(Area2, Area) then
	                    return 1;
	                end if;
	            end loop;   
	        end loop;
	    return 0;
	end;

	-- Имеет ли текущий пользователь права не меньшие чем запись в таблице UserGroup2Role с заданным идом
	function curUserHasRightsG2RId(
		ID_ in integer
	) return integer
	is
	    Area TRdxAcsArea;
	    Area2 TRdxAcsArea;
	begin       
	    for ind in (select * from RDX_AC_USERGROUP2ROLE where id = id_)
	        loop
	          Area :=  RDX_ACS_UTILS.buildAssignedAccessAreaG2R(ind);
	          for ind2 in (select * from RDX_AC_USER2ROLE where 
	                       isNew=0 and userName = RDX_Arte.getUserName() and               
	                       (roleid = ind.roleid or roleid = RDX_ACS_Vars.sysSuperAdminRoleId)
	                      )
	            loop
	                Area2 := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(ind2);
	                if RDX_ACS.containsPointInArea2(Area2, Area) then
	                   return 1;
	                end if;
	            end loop;   
	        end loop;
	    return 0;
	end;

	function strToArea(
		str_ in varchar2
	) return TRdxAcsArea
	is
	    pos_ integer;
	begin 
	    pos_:=1; 
	    return RDX_ACS.strToArea(str_, pos_);
	end;

	function strToAreaList(
		str in varchar2
	) return TRdxAcsAreaList
	is
	    list TRdxAcsAreaList;
	    pos_ integer;
	    area TRdxAcsArea;
	begin 
	    pos_:=1;
	    list := TRdxAcsAreaList();
	    if SubStr(str, pos_, 1)<>'(' then
	        RAISE_APPLICATION_ERROR (-20100, RDX_ACS_Vars.error_Message || str );
	    end if;
	    pos_:=pos_+1;
	    <<lbl>>while (SubStr(str, pos_, 1)<>')')
	            loop 
	                area :=  RDX_ACS.strToArea(str, pos_);
	                list.extend();
	                list(list.Count()):=area;     
	                pos_:=pos_+1;
	            end loop;                
	    return list;    
	end;

	function userHasRoleForObject(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in varchar2
	) return integer
	is
	begin
	return RDX_ACS.userHasRoleForObject(pUser, pRole,  RDX_ACS.strToAreaList(pPointList));
	end;

	function isGroupExist(
		name_ in varchar2
	) return integer
	is
	rez INTEGER;
	begin
	 rez := 0;
	 SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP WHERE ROWNUM <= 1 AND name_ = NAME;
	 return rez;
	end;

	function isCurUserHaveUserRights(
		user_ in varchar2
	) return integer
	is
	begin
	  return RDX_ACS.isUserHaveUserRights(user_, RDX_Arte.getUserName);
	end;

	function isUserHaveGroupRights(
		group_ in varchar2,
		user_ in varchar2
	) return integer
	is
	userArea TRdxAcsArea;
	groupArea TRdxAcsArea;
	flag boolean;
	  
	begin
	  for g2r_row in (select DISTINCT  * from RDX_AC_USERGROUP2ROLE where isNew=0 and groupname = group_)
	    loop                                                     
	    flag := false;
	    groupArea := RDX_ACS_UTILS.buildAssignedAccessAreaG2R(g2r_row);
	    
	    <<L>>for u2r_row in (select DISTINCT  * from RDX_AC_USER2ROLE U2R where isNew=0 and username = user_ and
	                            (g2r_row.roleId = U2R.roleId or U2R.roleId = RDX_ACS_Vars.sysSuperAdminRoleId))
	            loop      
	              userArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row);
	              if RDX_ACS.containsPointInArea2(userArea, groupArea) then
	                 flag := true;
	                 exit L;
	              end if; 
	            end loop; 
	    if not flag then
	      return 0; 
	    end if;
	    end loop;
	  return 1;
	end;

	-- user_ обладает не меньшими правами чем user2_
	function isUserHaveUserRights(
		user2_ in varchar2,
		user_ in varchar2
	) return integer
	is
	userArea TRdxAcsArea;
	lowerUserArea TRdxAcsArea;
	flag boolean;
	  
	begin
	  for u2r_row1 in (select DISTINCT * from RDX_AC_USER2ROLE where isNew=0 and username = user2_)
	    loop                                                     
	    flag := false;
	    lowerUserArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row1);
	    <<L>>for u2r_row in (select DISTINCT * from RDX_AC_USER2ROLE U2R where isNew=0 and username = user_ and
	                            (u2r_row1.roleId = U2R.roleId or U2R.roleId = RDX_ACS_Vars.sysSuperAdminRoleId))
	            loop
	              userArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row);
	              if RDX_ACS.containsPointInArea2(userArea, lowerUserArea) then
	                 flag := true;
	                 exit L;
	              end if; 
	            end loop; 
	    if not flag then
	      return 0; 
	    end if;
	    end loop;
	  return 1;
	end;

	function isCurUserHaveGroupRights(
		group_ in varchar2
	) return integer
	is
	begin
	  return RDX_ACS.isUserHaveGroupRights(group_, RDX_Arte.getUserName);
	end;

	function isGroupHaveRights(
		group_ in varchar2
	) return integer
	is
	rez INTEGER;
	begin
	 rez := 0;
	 SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM <= 1 AND groupName = group_;
	 return rez;
	end;

	-- User have own rights
	function isUserHaveOwnRights(
		user_ in varchar2
	) return integer
	is
	rez INTEGER;
	begin
	 rez := 0;
	 SELECT COUNT(*) INTO rez FROM RDX_AC_USER2ROLE WHERE ROWNUM <= 1 AND userName = user_ AND isOwn = 1;
	 return rez;
	end;

	function curUserGroupAdministered(
		pGroup in varchar2
	) return integer
	is
	begin
	 for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and userName = RDX_Arte.getUserName())
	     loop         
	         if ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_unbounded or
	            (ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_bounded and 
	             ind.PA$$1ZOQHCO35XORDCV2AANE2UAFXA is not null and
	             ind.PA$$1ZOQHCO35XORDCV2AANE2UAFXA = pGroup)                
	          then
	            return 1;
	          end if;
	      end loop;
	 return 0;
	end;

	function curUserNullGroupAdministered return integer
	is
	begin
	 for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and  userName = RDX_Arte.getUserName())
	     loop         
	         if ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_unbounded or
	            (ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_bounded and 
	             ind.PA$$1ZOQHCO35XORDCV2AANE2UAFXA is null)                
	          then
	            return 1;
	          end if;
	      end loop;
	 return 0;
	end;

	function isNewUserOrGroup2Role(
		userTable in integer,
		Id_ in integer
	) return integer
	is
	 rez integer := 0;
	begin
	 if (userTable = 1) then
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USER2ROLE where  ID = Id_ and ISNEW <> 0);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  else
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USERGROUP2ROLE where ID = Id_ and ISNEW <> 0);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  end  if;       
	 return rez;
	end;

	function mayReplaceOrRevokeRole(
		userTable in integer,
		Id_ in integer
	) return integer
	is
	 rez integer := 0;
	begin
	 if (userTable = 1) then
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USER2ROLE where ISNEW=0 AND ID = Id_) and  
	                                   not exists (Select * from RDX_AC_USER2ROLE where REPLACEDID = Id_);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  else
	    begin
	    select 1 into rez  from dual where exists (Select * from RDX_AC_USERGROUP2ROLE where ISNEW=0 AND ID = Id_) and  
	                                   not exists (Select * from RDX_AC_USERGROUP2ROLE where REPLACEDID = Id_);
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;
	  end  if;       
	 return rez;
	end;

	function getNext2RoleId(
		userTable in integer
	) return integer
	is
	begin
	   if (userTable=1) then
	      return SQN_RDX_AC_USER2ROLEID.nextVal;
	   else 
	      return SQN_RDX_AC_USERGROUP2ROLEID.nextVal;
	   end if;  
	end;

	function usedDualControlWhenAssignRoles return integer
	is
	 rez integer := 0;
	begin 
	    begin
	    select RDX_SYSTEM.DUALCONTROLFORASSIGNROLE into rez from RDX_SYSTEM where ID=1;
	    exception when NO_DATA_FOUND then rez  := 0;
	    end;       
	 return rez;
	end;

	procedure acceptRolesAndU2G(
		userTable in integer,
		userOrGroupName in varchar2,
		ignoredRoles out varchar2,
		ignoredUsersOrGroups out varchar2,
		addedRCount out integer,
		replacedRCount out integer,
		removedRCount out integer,
		addedU2GCount out integer,
		removedU2GCount out integer
	)
	-- simple, slow and clear method

	is 

	  curIndex integer;   
	  
	  Type TUser2RoleIdRecord IS RECORD (IdValue RDX_AC_USER2ROLE.ID%Type);
	  Type TUser2RoleIdRecordList is table of TUser2RoleIdRecord index by binary_integer;
	  
	  
	  ignoredRolesList TUser2RoleIdRecordList;
	  
	  addedRolesList TUser2RoleIdRecordList;
	  replacedRolesList TUser2RoleIdRecordList; 
	  removedRolesList TUser2RoleIdRecordList;
	  

	  addedRoles integer;
	  replacedRoles integer;  
	  removedRoles integer;
	  
	  addedU2G integer;  
	  removedU2G integer;
	  
	  flag boolean;
	  

	  Type TUserOrGroupNameRecord IS RECORD (NameValue RDX_AC_USER.NAME%Type);
	  Type TUserOrGroupNameRecordList is table of TUserOrGroupNameRecord index by binary_integer;


	  ignoredUsersOrGroupsList TUserOrGroupNameRecordList;

	  addedUsersOrGroupsList TUserOrGroupNameRecordList;
	  removedUsersOrGroupsList TUserOrGroupNameRecordList;
	  
	  outputBuffer varchar(32767);

	 
	 begin
	  addedRoles := 0;
	  replacedRoles := 0;
	  removedRoles := 0;
	  
	  addedU2G := 0;
	  removedU2G := 0;

	if (userTable=1) then
	 
	 
	        for item in (Select * from RDX_AC_USER2ROLE where 
	                USERNAME = userOrGroupName and 
	                ISNEW = 1 and 
	                ISOWN = 1 
	            )
	            loop
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredRolesList.count()+1;
	                    if (item.REPLACEDID is null) then
	                       ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                       ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    end if;   
	                    GOTO end_loop;
	                end if;
	                --finish check current editor
	                                
	                --start try add
	                if ((item.REPLACEDID is null) and (item.ROLEID is not null)) then
	                    if (RDX_ACS.curUserHasRightsU2RId(item.ID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                        addedRoles := addedRoles + 1;
	                        
	                        curIndex := addedRolesList.count()+1;
	                        addedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    GOTO end_loop;
	                end if;
	                --finish try add
	                
	                
	                --start try remove                
	                if ((item.REPLACEDID is not null) and (item.ROLEID is null)) then
	                    if (RDX_ACS.curUserHasRightsU2RId(item.REPLACEDID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        removedRoles := removedRoles + 1;
	                    
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    GOTO end_loop;
	                end if;
	                --finish try remove
	                
	                
	                --start try replace
	                if ((item.REPLACEDID is not null) and (item.ROLEID is not null)) then
	                    if (
	                         RDX_ACS.curUserHasRightsU2RId(item.REPLACEDID)=0
	                         or
	                         RDX_ACS.curUserHasRightsU2RId(item.ID)=0
	                       ) 
	                    then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        replacedRoles := replacedRoles + 1;
	                       
	                        curIndex := replacedRolesList.count()+1;
	                        replacedRolesList(curIndex).IdValue := item.ID;
	                       
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                    end if;
	                    GOTO end_loop;
	                end if;                
	                --finish try replace
	                <<end_loop>> 
	                null;
	            end loop;  
	            
	            --start accept
	            if (addedRolesList.first() is not null) then     
	                for i in addedRolesList.first() .. addedRolesList.last()
	                    loop
	                        if (addedRolesList.exists(i)) then
	                            update RDX_AC_USER2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                ID = addedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	            end if; 
	            
	            
	            if (replacedRolesList.first() is not null) then
	                for i in replacedRolesList.first() .. replacedRolesList.last()
	                    loop
	                        if (replacedRolesList.exists(i)) then
	                            update RDX_AC_USER2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName, REPLACEDID=null  where 
	                                ID = replacedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;            
	            end if; 
	            
	            
	            if (removedRolesList.first() is not null) then     
	                for i in removedRolesList.first() .. removedRolesList.last()
	                    loop
	                        if (removedRolesList.exists(i)) then
	                            delete from RDX_AC_USER2ROLE where ID = removedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            --finish accept
	            
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredRolesList.first() is not null) then     
	                for i in ignoredRolesList.first() .. ignoredRolesList.last()
	                    loop
	                        if (ignoredRolesList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredRoles := outputBuffer;
	                                    
	            addedRCount := addedRoles;
	            replacedRCount := replacedRoles;
	            removedRCount := removedRoles;
	            
	            
	        for item in (Select * from RDX_AC_USER2USERGROUP where 
	                USERNAME = userOrGroupName and 
	                STATE<>0
	            )
	            loop            
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredUsersOrGroupsList.count()+1;
	                    ignoredUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    goto end_loop;
	                end if;
	                --finish check current editor
	                
	                --start try add
	                if (item.STATE=1) then
	                    if (RDX_ACS.isCurUserHaveGroupRights(item.GROUPNAME)=0 or 
	                        RDX_ACS.isCurUserHaveUserRights(userOrGroupName)=0
	                       ) then -- access denied
	                    
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    else
	                        addedU2G := addedU2G + 1;
	                        curIndex := addedUsersOrGroupsList.count()+1;
	                        addedUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try add                
	                
	                --start try remove
	                if (item.STATE=2) then
	                    if (RDX_ACS.isCurUserHaveGroupRights(item.GROUPNAME)=0 or 
	                        RDX_ACS.isCurUserHaveUserRights(userOrGroupName)=0
	                       ) then -- access denied
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    else
	                        removedU2G := removedU2G + 1;
	                        curIndex := removedUsersOrGroupsList.count()+1;
	                        removedUsersOrGroupsList(curIndex).NameValue := item.GROUPNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try replace
	                <<end_loop>> 
	                null;
	            end loop;
	            
	            --start accept
	            if (addedUsersOrGroupsList.first() is not null) then     
	                for i in addedUsersOrGroupsList.first() .. addedUsersOrGroupsList.last()
	                    loop
	                        if (addedUsersOrGroupsList.exists(i)) then
	                            update RDX_AC_USER2USERGROUP SET STATE=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                USERNAME = userOrGroupName and GROUPNAME = addedUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;
	            end if;
	            
	            if (removedUsersOrGroupsList.first() is not null) then
	                for i in removedUsersOrGroupsList.first() .. removedUsersOrGroupsList.last()
	                    loop
	                        if (removedUsersOrGroupsList.exists(i)) then
	                            delete from RDX_AC_USER2USERGROUP where USERNAME = userOrGroupName and GROUPNAME = removedUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;            
	            end if;
	            --finish accept  
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredUsersOrGroupsList.first() is not null) then     
	                for i in ignoredUsersOrGroupsList.first() .. ignoredUsersOrGroupsList.last()
	                    loop
	                        if (ignoredUsersOrGroupsList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredUsersOrGroups := outputBuffer;
	                                    
	            addedU2GCount := addedU2G;
	            removedU2GCount := removedU2G;            
	    RDX_ACS_UTILS.compileRightsForUser(userOrGroupName);
	    
	else --groups
	--   
	--
	--
	null;


	        for item in (Select * from RDX_AC_USERGROUP2ROLE where 
	                GROUPNAME = userOrGroupName and 
	                ISNEW = 1
	            )
	            loop
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredRolesList.count()+1;
	                    if (item.REPLACEDID is null) then
	                       ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                       ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    end if;   
	                    goto end_loop;
	                end if;
	                --finish check current editor
	                     
	                --start try add
	                if ((item.REPLACEDID is null) and (item.ROLEID is not null)) then
	                    if (RDX_ACS.curUserHasRightsG2RId(item.ID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.ID;
	                    else
	                        addedRoles := addedRoles + 1;
	                        
	                        curIndex := addedRolesList.count()+1;
	                        addedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try add
	                
	                                
	                --start try remove                
	                if ((item.REPLACEDID is not null) and (item.ROLEID is null)) then
	                    if (RDX_ACS.curUserHasRightsG2RId(item.REPLACEDID)=0) then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        removedRoles := removedRoles + 1;
	                    
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.ID;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try remove
	                
	                 
	                
	                --start try replace
	                if ((item.REPLACEDID is not null) and (item.ROLEID is not null)) then
	                    if (
	                         RDX_ACS.curUserHasRightsG2RId(item.REPLACEDID)=0
	                         or
	                         RDX_ACS.curUserHasRightsG2RId(item.ID)=0
	                       ) 
	                    then -- access denied
	                        curIndex := ignoredRolesList.count()+1;
	                        ignoredRolesList(curIndex).IdValue := item.REPLACEDID;
	                    else
	                        replacedRoles := replacedRoles + 1;
	                       
	                        curIndex := replacedRolesList.count()+1;
	                        replacedRolesList(curIndex).IdValue := item.ID;
	                       
	                        curIndex := removedRolesList.count()+1;
	                        removedRolesList(curIndex).IdValue := item.REPLACEDID;
	                        
	                    end if;
	                    goto end_loop;
	                end if;
	                <<end_loop>> 
	                null;
	                --finish try replace
	            end loop;  
	            
	            --start accept
	            if (addedRolesList.first() is not null) then     
	                for i in addedRolesList.first() .. addedRolesList.last()
	                    loop
	                        if (addedRolesList.exists(i)) then
	                            update RDX_AC_USERGROUP2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                ID = addedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	            end if; 
	            
	            
	            if (replacedRolesList.first() is not null) then
	                for i in replacedRolesList.first() .. replacedRolesList.last()
	                    loop
	                        if (replacedRolesList.exists(i)) then
	                            update RDX_AC_USERGROUP2ROLE SET ISNEW=0, ACCEPTORNAME=RDX_Arte.getUserName, REPLACEDID=null  where 
	                                ID = replacedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;            
	            end if; 
	            
	            
	            if (removedRolesList.first() is not null) then     
	                for i in removedRolesList.first() .. removedRolesList.last()
	                    loop
	                        if (removedRolesList.exists(i)) then
	                            delete from RDX_AC_USERGROUP2ROLE where ID = removedRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            --finish accept
	            
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredRolesList.first() is not null) then     
	                for i in ignoredRolesList.first() .. ignoredRolesList.last()
	                    loop
	                        if (ignoredRolesList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredRolesList(i).IdValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredRoles := outputBuffer;
	                                    
	            addedRCount := addedRoles;
	            replacedRCount := replacedRoles;
	            removedRCount := removedRoles;
	            
	            
	            
	        for item in (Select * from RDX_AC_USER2USERGROUP where 
	                GROUPNAME = userOrGroupName and 
	                STATE<>0
	            )
	            loop            
	                --start check current editor
	                if (RDX_Arte.getUserName = item.EDITORNAME) then                    
	                    curIndex := ignoredUsersOrGroupsList.count()+1;
	                    ignoredUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    goto end_loop;
	                end if;
	                --finish check current editor
	                
	                --start try add
	                if (item.STATE=1) then
	                    if (RDX_ACS.isCurUserHaveUserRights(item.USERNAME)=0 or 
	                        RDX_ACS.isCurUserHaveGroupRights(userOrGroupName)=0
	                       ) then -- access denied
	                    
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    else
	                        addedU2G := addedU2G + 1;
	                        curIndex := addedUsersOrGroupsList.count()+1;
	                        addedUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try add                
	                
	                --start try remove
	                if (item.STATE=2) then
	                    if (RDX_ACS.isCurUserHaveUserRights(item.USERNAME)=0 or 
	                        RDX_ACS.isCurUserHaveGroupRights(userOrGroupName)=0
	                       ) then -- access denied
	                        curIndex := ignoredUsersOrGroupsList.count()+1;
	                        ignoredUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    else
	                        removedU2G := removedU2G + 1;
	                        curIndex := removedUsersOrGroupsList.count()+1;
	                        removedUsersOrGroupsList(curIndex).NameValue := item.USERNAME;
	                    end if;
	                    goto end_loop;
	                end if;
	                --finish try remove  
	                <<end_loop>> 
	                null;              
	            end loop;
	            
	            --start accept
	            if (addedUsersOrGroupsList.first() is not null) then     
	                for i in addedUsersOrGroupsList.first() .. addedUsersOrGroupsList.last()
	                    loop
	                        if (addedUsersOrGroupsList.exists(i)) then
	                            update RDX_AC_USER2USERGROUP SET STATE=0, ACCEPTORNAME=RDX_Arte.getUserName  where 
	                                GROUPNAME = userOrGroupName and USERNAME = addedUsersOrGroupsList(i).NameValue;
	                                null;
	                        end if;                    
	                    end loop;
	            end if;
	            
	            if (removedUsersOrGroupsList.first() is not null) then
	                for i in removedUsersOrGroupsList.first() .. removedUsersOrGroupsList.last()
	                    loop
	                        if (removedUsersOrGroupsList.exists(i)) then
	                            delete from RDX_AC_USER2USERGROUP where GROUPNAME = userOrGroupName and USERNAME = removedUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;            
	            end if;
	            --finish accept  
	            
	            flag := true;
	            outputBuffer := '';
	            if (ignoredUsersOrGroupsList.first() is not null) then     
	                for i in ignoredUsersOrGroupsList.first() .. ignoredUsersOrGroupsList.last()
	                    loop
	                        if (ignoredUsersOrGroupsList.exists(i)) then
	                            if (flag  = true)then
	                               flag := false;
	                            else
	                               outputBuffer := outputBuffer || chr(10);
	                            end if;                        
	                            outputBuffer := outputBuffer || ignoredUsersOrGroupsList(i).NameValue;
	                        end if;                    
	                    end loop;
	             end if; 
	            
	            ignoredUsersOrGroups := outputBuffer;
	                                    
	            addedU2GCount := addedU2G;
	            removedU2GCount := removedU2G; 
	        RDX_ACS_UTILS.compileRightsForGroup(userOrGroupName);            


	end if;


	  
	   
	end;

	procedure getRolesAndU2GCount(
		userTable in integer,
		userOrGroupName in varchar2,
		acceptedRCount out integer,
		unacceptedRCount out integer,
		acceptedU2GCount out integer,
		unacceptedU2GCount out integer
	)
	is
	begin
	  if (userTable = 1) then
	  
	    Select count (*) into unacceptedRCount from RDX_AC_USER2ROLE where USERNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and ISNEW=1;
	    Select count (*) into acceptedRCount from RDX_AC_USER2ROLE where USERNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and ISNEW=1;
	    
	    Select count (*) into unacceptedU2GCount from RDX_AC_USER2USERGROUP where USERNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and STATE<>0;
	    Select count (*) into acceptedU2GCount from RDX_AC_USER2USERGROUP where USERNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and STATE<>0;
	    
	  else
	  
	    Select count (*) into unacceptedRCount from RDX_AC_USERGROUP2ROLE where GROUPNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and ISNEW=1;
	    Select count (*) into acceptedRCount from RDX_AC_USERGROUP2ROLE where GROUPNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and ISNEW=1;
	    
	    Select count (*) into unacceptedU2GCount from RDX_AC_USER2USERGROUP where GROUPNAME = userOrGroupName and RDX_Arte.getUserName = EDITORNAME and STATE<>0;
	    Select count (*) into acceptedU2GCount from RDX_AC_USER2USERGROUP where GROUPNAME = userOrGroupName and RDX_Arte.getUserName <> EDITORNAME and STATE<>0;    
	    
	  end if;  
	end;

	procedure getNotAcceptedEntities(
		user2UserGroupCount out integer,
		firstUser2UserGroup out varchar2,
		userGroup2RoleCount out integer,
		firstUserGroup2Role out varchar2,
		user2RoleCount out integer,
		firstUser2Role out varchar2
	)
	is
	 rez  integer;
	 buff varchar2(200); 
	begin
	    rez:=0;

	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2USERGROUP WHERE ROWNUM <= 1 AND STATE<>0;
	    user2UserGroupCount:=rez;
	     
	    if (rez<>0) then
	      SELECT USERNAME || chr(10) || GROUPNAME INTO firstUser2UserGroup FROM RDX_AC_USER2USERGROUP WHERE ROWNUM = 1 AND STATE<>0;
	    end if;
	   
	   
	    SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    userGroup2RoleCount := rez;
	   
	    if (rez<>0) then
	        SELECT GROUPNAME  || chr(10) || ROLEID INTO firstUserGroup2Role FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM = 1 AND ISNEW<>0;      
	    end if;


	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    user2RoleCount := rez;    
	    
	    
	    if (rez<>0) then
	        SELECT USERNAME  || chr(10) || ROLEID INTO firstUser2Role FROM RDX_AC_USER2ROLE WHERE ROWNUM = 1 AND ISNEW<>0;        
	    end if;
	end;

	function haveNotAcceptedEntities return integer
	is
	rez integer;
	begin
	    rez:=0;

	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2USERGROUP WHERE ROWNUM <= 1 AND STATE<>0;
	    if (rez<>0) then
	      return 1;
	    end if;

	    SELECT COUNT(*) INTO rez FROM RDX_AC_USERGROUP2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    if (rez<>0) then
	      return 1;
	    end if;
	    
	    SELECT COUNT(*) INTO rez FROM RDX_AC_USER2ROLE WHERE ROWNUM <= 1 AND ISNEW<>0;
	    if (rez<>0) then
	      return 1;
	    end if;
	    
	    return 0;
	    
	end;
end;
/

create or replace package RDX_Aadc as

	procedure setupFirstMember;

	procedure setupSecondMember;

	procedure afterSequenceDdl(
		name in varchar2
	);

	procedure normalizeSequence(
		name in varchar2,
		-- null - read from database
		aadcMemberId in integer := null
	);

	procedure normalizeAllSequences;

	procedure restoreReplication(
		prevState in RAW
	);

	function suspendReplication return RAW;
end;
/

grant execute on RDX_Aadc to &USER&_RUN_ROLE
/

create or replace package body RDX_Aadc as

	procedure setupFirstMember
	is
	    prevTag RAW(10);
	begin
	    prevTag := RDX_Aadc.suspendReplication;
	    update RDX_SYSTEM set AADCMEMBERID = 1 where ID = 1;
	    RDX_Aadc.normalizeAllSequences;
	    RDX_Aadc.restoreReplication(prevTag);
	    update RDX_INSTANCE set AADCMEMBERID = 1;
	    update RDX_JS_JOBQUEUE set AADCMEMBERID = 1;
	end;

	procedure setupSecondMember
	is
	    prevTag RAW(10);
	begin
	    prevTag := RDX_Aadc.suspendReplication;
	    update RDX_SYSTEM set AADCMEMBERID = 2 where ID = 1;
	    RDX_Aadc.normalizeAllSequences;
	    RDX_Aadc.restoreReplication(prevTag);
	end;

	procedure afterSequenceDdl(
		name in varchar2
	)
	is
	begin
	    insert into RDX_AADC_SEQUENCEDDL(NAME, LASTTIME) values (name, systimestamp);
	end;

	procedure normalizeSequence(
		name in varchar2,
		-- null - read from database
		aadcMemberId in integer := null
	)
	is
	    memberId integer;
	    val integer;
	    minval integer; 
	    inc integer;
	    min_cur_ex EXCEPTION;
	    PRAGMA EXCEPTION_INIT(min_cur_ex, -04007);
	begin
	    memberId := aadcMemberId;
	    if memberId is null then
	        select AADCMEMBERID into memberId from RDX_SYSTEM where ID = 1;
	    end if;    
	    if memberId is null then
	        return;
	    end if;    
	    select min_value, increment_by into minval, inc from sys.user_sequences where sequence_name = upper(name);
	    if minval = memberId and inc = 2 then
	        return;
	    end if;
	    loop   
	        begin 
	            execute immediate 'alter sequence ' || name || ' MINVALUE ' || memberId;
	            exit;
	        exception 
	            when min_cur_ex then   
	                execute immediate 'select ' || name || '.nextVal from dual' into val;
	        end;        
	    end loop;    
	    loop 
	        execute immediate 'alter sequence ' || name || ' INCREMENT BY 2';
	        execute immediate 'select ' || name || '.nextVal from dual' into val;
	        exit when ((val - 1) mod 2 = memberId - 1);
	        execute immediate 'alter sequence ' || name || ' INCREMENT BY 1';
	        execute immediate 'select ' || name || '.nextVal from dual' into val;
	    end loop;  
	end;

	procedure normalizeAllSequences
	is
	    memberId integer;
	begin
	    select AADCMEMBERID into memberId from RDX_SYSTEM where ID = 1;
	    if memberId is null then
	        return;
	    end if;    
	    for rec in (select sequence_name from user_sequences) loop
	        RDX_Aadc.normalizeSequence(rec.sequence_name, memberId);
	    end loop;
	end;

	function suspendReplication return RAW
	is
	    prevTag RAW(10);
	begin
	    prevTag := DBMS_STREAMS.GET_TAG();
	    DBMS_STREAMS.SET_TAG(RDX_Sys_Vars.SUSPENSION_TAG);
	    return prevTag;
	end;

	procedure restoreReplication(
		prevState in RAW
	)
	is
	begin
	    DBMS_STREAMS.SET_TAG(prevState);
	end;
end;
/

create or replace package body RDX_Arte as

	procedure setSessionIsActive(
		pSessionId in integer,
		pIsActive in integer
	)
	is
	    pragma autonomous_transaction;
	begin
	    update RDX_EASSESSION
	       set ISACTIVE=pIsActive, LASTCONNECTTIME=SYSDATE  
	       where ID = pSessionId;
	    commit;
	end;

	procedure registerSession(
		pUserName in varchar2,
		pStationName in varchar2,
		pClientLanguage in varchar2,
		pClientCountry in varchar2,
		pSessionId in integer,
		pDefVersion in integer,
		pSapId in integer,
		pClientAddress in varchar2,
		pUnitId in integer
	)
	is
	begin
	   RDX_Sys_Vars.userName:=pUserName;
	   RDX_Sys_Vars.stationName:=pStationName;
	   RDX_Sys_Vars.clientLanguage:=pClientLanguage;
	   RDX_Sys_Vars.clientCountry:=pClientCountry;
	   RDX_Sys_Vars.sessionId:=pSessionId;
	   RDX_Sys_Vars.defVersion:=pDefVersion;
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo(requestInfo => 'U#'|| pUnitId || '-S#' || pSapId || '-' || pClientAddress);
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	procedure registerSession(
		pUserName in varchar2,
		pStationName in varchar2,
		pClientLanguage in varchar2,
		pClientCountry in varchar2,
		pSessionId in integer,
		pDefVersion in integer,
		pSapId in integer,
		pClientAddress in varchar2,
		pUnitId in integer,
		pJobId in integer,
		pTaskId in integer
	)
	is
	begin
	   RDX_Sys_Vars.userName:=pUserName;
	   RDX_Sys_Vars.stationName:=pStationName;
	   RDX_Sys_Vars.clientLanguage:=pClientLanguage;
	   RDX_Sys_Vars.clientCountry:=pClientCountry;
	   RDX_Sys_Vars.sessionId:=pSessionId;
	   RDX_Sys_Vars.defVersion:=pDefVersion;
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo( 'U#'|| pUnitId ||  '/J#' || nvl(to_char(pJobId), '-') || '/T#' || nvl(to_char(pTaskId), '-') );
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	function getUserName return varchar2
	is
	begin
	  return RDX_Sys_Vars.userName;
	end;

	function getVersion return integer
	is
	begin
	    if RDX_Sys_Vars.defVersion is null then
	       raise_application_error(-20001,'Definition version is not deployed to database. '||chr(10)||'onStartRequestExecution() method of Arte (java class) should have been called  '||chr(10)||'before the database request executing.');
	    end if;
	  return RDX_Sys_Vars.defVersion;
	end;

	function getStationName return varchar2
	is
	begin
	  return RDX_Sys_Vars.stationName;
	end;

	function getClientLanguage return varchar2
	is
	begin
	  return RDX_Sys_Vars.clientLanguage;
	end;

	function getSessionId return integer
	is
	begin
	  return RDX_Sys_Vars.sessionId;
	end;

	procedure unregisterSession(
		pEasSessionId in integer
	)
	is
	begin
	    if pEasSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pEasSessionId, pIsActive => 0);
	    end if;
	    RDX_Environment.setRequestInfo(null);
	end;
end;
/

create or replace package body RDX_Entity as

	function getUserPropInt(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	begin
	    return RDX_Entity.getUserPropNum(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	procedure setUserPropInt(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in integer,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in integer := NULL
	)
	is
	  oldVal integer;
	  valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 2);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropInt(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;    
	    RDX_Entity.setUserPropNum(pEntityId => pEntityId, pClassId => pClassId, pInstancePid => pInstancePid, pPropId => pPropId, pVal => pVal, pIsUpdateAuditOn => 0, pAuditSchemeId => NULL);
	    if pIsUpdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropIntCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else
	            RDX_AUDIT.regPropIntUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;
	    end if;
	end;

	function isUserPropValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pPropValType in integer
	) return integer
	is
	   res integer;
	begin
	     execute immediate 'select (case when exists' || 
	        '(select * from '|| RDX_Entity_Vars.upValTabByValType(pPropValType) ||'  where DEFID = :1 and OWNERENTITYID = :2 and OWNERPID = :3 )'||
	        'then 1 else 0 end) from dual'
	     into res
	     using pPropId, pEntityId, pInstancePid;
	     return res;
	end;

	procedure userPropOnDelOwner(
		pEntityID in varchar2,
		pInstancePID in varchar2
	)
	is   
	begin
	   delete RDX_UPVALNUM      where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	   delete RDX_UPVALSTR      where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	   delete RDX_UPVALRAW      where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	   delete RDX_UPVALREF      where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	   delete RDX_UPVALCLOB     where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	   delete RDX_UPVALBLOB     where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	   delete RDX_UPVALDATETIME where OWNERENTITYID = pEntityID and OWNERPID = pInstancePID;
	end;

	procedure flushUserPropOnDelOwner(
		pEntityID in varchar2
	)
	is
	  type tvc is table of varchar2(1000)  index by binary_integer;
	  i number;   
	  j number;
	  mustDie tvc;
	begin
	   i := RDX_Entity_Vars.scheduledOnDelUpOwnerQueue.first;
	   while i is not null loop 
	      if RDX_Entity_Vars.scheduledOnDelUpOwnerQueue(i).sOwnerEntityId = pEntityId then
	         mustDie(mustDie.count+1) := RDX_Entity_Vars.scheduledOnDelUpOwnerQueue(i).sOwnerPid;                
	         j := i;
	         i := RDX_Entity_Vars.scheduledOnDelUpOwnerQueue.next(i); 
	         RDX_Entity_Vars.scheduledOnDelUpOwnerQueue.delete(j);                 
	      else
	         i := RDX_Entity_Vars.scheduledOnDelUpOwnerQueue.next(i);           
	      end if;      
	   end loop;     
	   i := mustDie.first;
	   while(i is not null) loop            
	      RDX_Entity.userPropOnDelOwner(pEntityID => pEntityId, pInstancePID => mustDie(i));                  
	      i:= mustDie.next(i);
	   end loop;
	end;

	procedure scheduleUserPropOnDelOwner(
		pEntityID in varchar2,
		pInstancePID in varchar2
	)
	is   
	begin
	   RDX_Entity_Vars.scheduledOnDelUpOwnerQueue(RDX_Entity_Vars.scheduledOnDelUpOwnerQueue.count + 1).sOwnerEntityId :=pEntityId;  
	   RDX_Entity_Vars.scheduledOnDelUpOwnerQueue(RDX_Entity_Vars.scheduledOnDelUpOwnerQueue.count).sOwnerPid    := pInstancePid;  
	end;

	function packPidStr(
		s in varchar2
	) return varchar2 deterministic
	is
	  res varchar2(32767);
	begin
	  res := Replace(s,   CHR(92),  CHR(92)|| CHR(92));   -- '\' => '\\'
	  res := Replace(res, CHR(126), CHR(92)|| CHR(126)); -- '~' => '\~'
	  res := Replace(res, CHR(13),  CHR(92)|| 'r');      -- '\r' => '\\r'
	  res := Replace(res, CHR(10),  CHR(92)|| 'n');      -- '\n' => '\\n'
	  res := Replace(res, CHR(9),   CHR(92)|| 't');      -- '\t' => '\\t'
	  res := Replace(res, ' ',      CHR(92)|| ' ');     -- ' '  => '\ '
	  return res;
	end;

	procedure delUserPropVal(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pPropValType in integer,
		pIsUpdateAuditOn in integer,
		pAuditSchemeId in varchar2
	)
	is
	   oldValTime Timestamp;
	   oldValNum number;
	   oldValBool boolean;
	   oldValStr RDX_UPVALSTR.VAL%Type;
	   oldValRef RDX_UPVALREF.VAL%Type;
	   oldValRaw RDX_UPVALRAW.VAL%Type;
	   oldValClob Clob; 
	   oldValBlob Blob;
	   upTabName varchar2(100); 
	   valWasDefined integer;
	begin
	    upTabName := RDX_Entity_Vars.upValTabByValType(pPropValType);
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, pPropValType);
	        if (valWasDefined <> 0) then
	            if upTabName = 'RDX_UPVALDATETIME' then
	                oldValTime := RDX_Entity.getUserPropDateTime(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            else if upTabName = 'RDX_UPVALNUM' then
	                oldValNum := RDX_Entity.getUserPropNum(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            else if upTabName = 'RDX_UPVALSTR' then
	                oldValStr := RDX_Entity.getUserPropStr(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            else if upTabName = 'RDX_UPVALREF' then
	                oldValRef := RDX_Entity.getUserPropRef(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            else if upTabName = 'RDX_UPVALRAW' then
	                oldValRaw := RDX_Entity.getUserPropBin(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            else if upTabName = 'RDX_UPVALCLOB' then
	                oldValClob := RDX_Entity.getUserPropClob(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            else if upTabName = 'RDX_UPVALBLOB' then
	                oldValBlob := RDX_Entity.getUserPropBlob(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	            end if;end if;end if;end if;end if;end if;end if;    
	        end if;    
	    end if;    

	     execute immediate 'delete from '|| upTabName ||'  where DEFID = :1 and OWNERENTITYID = :2 and OWNERPID = :3 '
	     using pPropId, pEntityId, pInstancePid;
	     
	     if pIsUpdateAuditOn != 0 and valWasDefined <> 0 then
	        if upTabName = 'RDX_UPVALDATETIME' then
	            RDX_AUDIT.regPropDateTimeDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValTime);
	        else if upTabName = 'RDX_UPVALNUM' then
	            if (pPropValType = 2) then
	                RDX_AUDIT.regPropIntDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValNum);
	            else   
	                if (pPropValType = 1) then
	                    if (oldValNum is null) then
	                            oldValBool := NULL;
	                    else
	                        if oldValNum =  0 then     
	                            oldValBool := false;
	                        else    
	                            oldValBool := true;
	                        end if;    
	                    end if;    
	                    RDX_AUDIT.regPropBoolDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValBool);
	                else
	                    RDX_AUDIT.regPropNumDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValNum);
	                end if;
	            end if; 
	        else if upTabName = 'RDX_UPVALSTR' then
	            RDX_AUDIT.regPropStrDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValStr);
	        else if upTabName = 'RDX_UPVALREF' then
	            RDX_AUDIT.regPropStrDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValRef);
	        else if upTabName = 'RDX_UPVALRAW' then
	            RDX_AUDIT.regPropRawDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValRaw);
	        else if upTabName = 'RDX_UPVALCLOB' then
	            RDX_AUDIT.regPropClobDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValClob);
	        else if upTabName = 'RDX_UPVALBLOB' then
	            RDX_AUDIT.regPropBlobDeleted(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => oldValBlob);
	        end if;end if;end if;end if;end if;end if;end if;    
	     end if;
	exception when NO_DATA_FOUND then
	     null;
	end;

	function getUserPropBool(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	begin
	    return RDX_Entity.getUserPropNum(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	procedure setUserPropBool(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in integer,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	  oldVal integer; 
	  oldValBool boolean; 
	  newValBool boolean; 
	  valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 1);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropBool(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;    
	    RDX_Entity.setUserPropNum(pEntityId => pEntityId, pClassId => pClassId, pInstancePid => pInstancePid, pPropId => pPropId, pVal => pVal, pIsUpdateAuditOn => 0, pAuditSchemeId => NULL);
	    if pIsUpdateAuditOn != 0 then
	        if (pVal is null) then
	            newValBool := NULL;
	        else
	            if pVal = 0 then     
	                newValBool := false;
	            else    
	                newValBool := true;
	            end if;    
	        end if;    
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropBoolCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => newValBool);
	        else 
	            if (oldVal is null) then
	                    oldValBool := NULL;
	            else
	                if oldVal =  0 then     
	                    oldValBool := false;
	                else    
	                    oldValBool := true;
	                end if;    
	            end if;    
	            RDX_AUDIT.regPropBoolUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldValBool, newVal => newValBool);
	         end if;   
	    end if;    
	end;

	function getUserPropNum(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return number
	is
	    v RDX_UPVALNUM.VAL%TYPE;
	begin
	    select VAL into v from RDX_UPVALNUM
	    where DEFID = pPropId and 
	          OWNERENTITYID = pEntityId and
	          OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	procedure setUserPropNum(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in number,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal number;
	    valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 11);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropNum(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;    
	    merge into RDX_UPVALNUM P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsUpdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropNumCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else
	            RDX_AUDIT.regPropNumUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;    
	    end if;
	end;

	function getUserPropStr(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return varchar2
	is
	    v RDX_UPVALSTR.VAL%TYPE;
	begin
	    select VAL into v from RDX_UPVALSTR
	    where DEFID = pPropId and 
	          OWNERENTITYID = pEntityId and
	          OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	procedure setUserPropStr(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in varchar2,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal RDX_UPVALSTR.VAL%type;
	    valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 21);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropStr(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;
	    merge into RDX_UPVALSTR P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsUpdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropStrCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else
	            RDX_AUDIT.regPropStrUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;    
	    end if;
	end;

	function getUserPropChar(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return varchar2
	is
	begin
	    return RDX_Entity.getUserPropStr(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	procedure setUserPropChar(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in varchar2,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	begin
	    RDX_Entity.setUserPropStr(pEntityId => pEntityId, pClassId => pClassId, pInstancePid => pInstancePid, pPropId => pPropId, pVal => pVal, pIsUpdateAuditOn => pIsUpdateAuditOn, pAuditSchemeId => pAuditSchemeId);
	end;

	function getUserPropClob(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return CLOB
	is
	    v RDX_UPVALCLOB.VAL%TYPE;
	begin
	    select VAL into v from RDX_UPVALCLOB
	    where DEFID = pPropId and 
	          OWNERENTITYID = pEntityId and
	          OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	procedure setUserPropClob(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in CLOB,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal Clob;
	    valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 27);
	        if (valWasDefined <> 0) then
	           oldVal := RDX_Entity.getUserPropClob(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;   
	    end if;
	    merge into RDX_UPVALCLOB P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsUpdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropClobCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else
	            RDX_AUDIT.regPropClobUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;   
	    end if;
	end;

	function getUserPropBlob(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return BLOB
	is
	    v RDX_UPVALBLOB.VAL%TYPE;
	begin
	    select VAL into v from RDX_UPVALBLOB
	    where DEFID = pPropId and 
	          OWNERENTITYID = pEntityId and
	          OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	procedure setUserPropBlob(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in BLOB,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal Blob;
	    valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 28);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropBlob(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;
	    merge into RDX_UPVALBLOB P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsUpdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropBlobCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else 
	            RDX_AUDIT.regPropBlobUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;    
	    end if;
	end;

	function getUserPropBin(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return RAW
	is
	    v RDX_UPVALRAW.VAL%TYPE;
	begin
	    select VAL into v from RDX_UPVALRAW
	    where DEFID = pPropId and 
	          OWNERENTITYID = pEntityId and
	          OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	procedure setUserPropBin(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in RAW,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal RDX_UPVALRAW.VAL%TYPE;
	    valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then 
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 22);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropBin(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	       end if;     
	    end if;
	    merge into RDX_UPVALRAW P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsUpdateAuditOn != 0 then 
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropRawCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else 
	            RDX_AUDIT.regPropRawUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;    
	    end if;
	end;

	function getUserPropDateTime(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return Timestamp
	is
	    v RDX_UPVALDATETIME.VAL%TYPE;
	begin
	    select RDX_UPVALDATETIME.VAL into v from RDX_UPVALDATETIME
	    where RDX_UPVALDATETIME.DEFID = pPropId and 
	          RDX_UPVALDATETIME.OWNERENTITYID = pEntityId and
	          RDX_UPVALDATETIME.OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	procedure setUserPropDateTime(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in timestamp,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal Timestamp;
	    valWasDefined integer;
	begin
	    if pIsUpdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 12);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropDateTime(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;
	    merge into RDX_UPVALDATETIME P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsUpdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropDateTimeCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else 
	            RDX_AUDIT.regPropDateTimeUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;    
	    end if;
	end;

	procedure setUserPropRef(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in varchar2,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	    oldVal RDX_UPVALREF.VAL%TYPE;
	    valWasDefined integer;
	begin
	    if pIsupdateAuditOn != 0 then
	        valWasDefined := RDX_Entity.isUserPropValDefined(pEntityId, pInstancePid, pPropId, 31);
	        if (valWasDefined <> 0) then
	            oldVal := RDX_Entity.getUserPropRef(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	        end if;    
	    end if;
	    merge into RDX_UPVALREF P  
	    using (select pPropId PROPDEFID, pEntityId OWNERENTITYID, pInstancePid OWNERPID, pVal VAL from DUAL) D 
	    on (P.DEFID = D.PROPDEFID and P.OWNERENTITYID = D.OWNERENTITYID and P.OWNERPID = D.OWNERPID)
	    when matched then update set P.VAL = D.VAL 
	    when not matched then insert(P.DEFID, P.OWNERENTITYID, P.OWNERPID, P.VAL) 
	    values(D.PROPDEFID, D.OWNERENTITYID, D.OWNERPID, D.VAL);
	    if pIsupdateAuditOn != 0 then
	        if (valWasDefined = 0) then
	            RDX_AUDIT.regPropStrCreated(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, val => pVal);
	        else 
	            RDX_AUDIT.regPropStrUpdate(schemeId => pAuditSchemeId, tableId => pEntityId, pClassId => pClassId, propId => pPropId, pid => pInstancePid, oldVal => oldVal, newVal => pVal);
	        end if;    
	    end if;
	end;

	function getUserPropRef(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return varchar2
	is
	    v RDX_UPVALREF.VAL%TYPE;
	begin
	    select RDX_UPVALREF.VAL into v from RDX_UPVALREF
	    where RDX_UPVALREF.DEFID = pPropId and 
	          RDX_UPVALREF.OWNERENTITYID = pEntityId and
	          RDX_UPVALREF.OWNERPID = pInstancePid;
	    return v;
	exception when NO_DATA_FOUND then
	    return null;
	end;

	function getUserPropArrAsStr(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return CLOB
	is
	begin
	    return RDX_Entity.getUserPropClob(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	procedure setUserPropArrAsStr(
		pEntityId in varchar2,
		pClassId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2,
		pVal in CLOB,
		pIsUpdateAuditOn in integer := 0,
		pAuditSchemeId in varchar2 := NULL
	)
	is
	begin
	    RDX_Entity.setUserPropClob(pEntityId => pEntityId, pClassId => pClassId, pInstancePid => pInstancePid, pPropId => pPropId, pVal => pVal, pIsUpdateAuditOn => pIsUpdateAuditOn, pAuditSchemeId => pAuditSchemeId);
	end;

	function isUserPropNumValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	   res integer;
	begin
	     select (case when exists
	        (select * from RDX_UPVALNUM  where DEFID = pPropId and OWNERENTITYID = pEntityId and OWNERPID = pInstancePid)
	        then 1 else 0 end) 
	     into res
	     from dual;
	     return res;
	end;

	function isUserPropIntValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	begin
	    return RDX_Entity.isUserPropNumValDefined(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	function isUserPropBoolValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	begin
	    return RDX_Entity.isUserPropNumValDefined(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	function isUserPropDateTimeValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	   res integer;
	begin
	     select (case when exists
	        (select * from RDX_UPVALDATETIME  where DEFID = pPropId and OWNERENTITYID = pEntityId and OWNERPID = pInstancePid)
	        then 1 else 0 end) 
	     into res
	     from dual;
	     return res;
	end;

	function isUserPropClobValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	   res integer;
	begin
	     select (case when exists
	        (select * from RDX_UPVALCLOB  where DEFID = pPropId and OWNERENTITYID = pEntityId and OWNERPID = pInstancePid)
	        then 1 else 0 end) 
	     into res
	     from dual;
	     return res;
	end;

	function isUserPropBlobValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	   res integer;
	begin
	     select (case when exists
	        (select * from RDX_UPVALBLOB  where DEFID = pPropId and OWNERENTITYID = pEntityId and OWNERPID = pInstancePid)
	        then 1 else 0 end) 
	     into res
	     from dual;
	     return res;
	end;

	function isUserPropRefValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	   res integer;
	begin
	     select (case when exists
	        (select * from RDX_UPVALREF  where DEFID = pPropId and OWNERENTITYID = pEntityId and OWNERPID = pInstancePid)
	        then 1 else 0 end) 
	     into res
	     from dual;
	     return res;
	end;

	function isUserPropStrValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	   res integer;
	begin
	     select (case when exists
	        (select * from RDX_UPVALSTR  where DEFID = pPropId and OWNERENTITYID = pEntityId and OWNERPID = pInstancePid)
	        then 1 else 0 end) 
	     into res
	     from dual;
	     return res;
	end;

	function isUserPropArrValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	begin
	    return RDX_Entity.isUserPropClobValDefined(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	function isUserPropCharValDefined(
		pEntityId in varchar2,
		pInstancePid in varchar2,
		pPropId in varchar2
	) return integer
	is
	begin
	    return RDX_Entity.isUserPropStrValDefined(pEntityId => pEntityId, pInstancePid => pInstancePid, pPropId => pPropId);
	end;

	function createPid(
		field in varchar2
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.packPidStr(field);
	end;

	function createPid(
		field in number
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.createPid(To_Char(field));
	end;

	function createPid(
		field in timestamp
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.createPid(To_Char(field));
	end;

	function addField2Pid(
		currentPid in varchar2,
		field in varchar2
	) return varchar2 deterministic
	is
	begin
	 if (currentPid is null) then
	   return RDX_Entity.packPidStr(field);
	 else 
	   return currentPid || '~' || RDX_Entity.packPidStr(field);
	 end if;  
	end;

	function addField2Pid(
		currentPid in varchar2,
		field in number
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.addField2Pid(currentPid, To_Char(field));
	end;

	function addField2Pid(
		currentPid in varchar2,
		field in timestamp
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.addField2Pid(currentPid, To_Char(field));
	end;

	procedure userPropOnDelValue(
		pEntityId in varchar2,
		pValuePid in varchar2
	)
	is
	    ex_restricted exception;
	    pragma EXCEPTION_INIT(ex_restricted, -2291);
	    restrictExists integer;
	    refIndex integer;
	    currentVersion constant integer := Rdx_Arte.getVersion();

	    cursor refs_cur (pEntId in varchar2, pValPid in varchar2) is select e.deleteMode 
	        from rdx_entityuserref e inner join rdx_upvalref u
	        on e.userpropid = u.defid
	        where 
	        e.tableId = pEntId
	        and e.versionnum = currentVersion
	        and u.val = pValPid
	        for update of u.defid;
	    cursor arr_refs_cur (pEntId in varchar2, pValPid in varchar2) is select e.deleteMode, u.val 
	        from rdx_entityarruserref e, rdx_upvalclob u
	        where 
	        e.tableId = pEntId
	        and e.versionnum = currentVersion
	        and e.userpropid = u.defid
	        and rdx_array.searchRef(u.val, e.tableId, pValPid, 1) > 0 
	        for update of u.defid;
	    cursor restrByParRefExistsCur (pEntId in varchar2, pValPid in varchar2) is select 1 
	      from rdx_entityuserref e inner join rdx_upvalref u on e.userpropid = u.defid
	      where 
	      e.tableId = pEntId
	      and u.val = pValPid
	      and deletemode = 'RESTRICT' 
	      and rownum <= 1;
	    cursor restrByArrParRefExistsCur (pEntId in varchar2, pValPid in varchar2)  is select 1  
	      from 
	      (select e.deleteMode 
	      from rdx_entityarruserref e, rdx_upvalclob u
	      where 
	      e.tableId = pEntId
	      and e.versionnum = currentVersion
	      and e.deleteMode = 'RESTRICT'
	      and e.userpropid = u.defid
	      and rdx_array.searchRef(u.val, e.tableId, pValPid, 1) > 0 
	      and rownum <= 1);
	BEGIN

	restrictExists := 0;

	for rest_record  in restrByParRefExistsCur(pEntityId, pValuePid) loop
	    restrictExists := 1;
	end loop;

	if restrictExists > 0 then
	  raise ex_restricted;
	end if;

	for rest_record  in restrByArrParRefExistsCur(pEntityId, pValuePid) loop
	restrictExists := 1;
	end loop;

	if restrictExists > 0 then
	  raise ex_restricted;
	end if;

	for ref_record in refs_cur(pEntityId, pValuePid) loop
	  if ref_record.deleteMode = 'SET_NULL' then
	      update rdx_upvalref set val = null where current of refs_cur;
	  elsif ref_record.deleteMode = 'REMOVE_VALUE' then
	      delete from rdx_upvalref where current of refs_cur;
	  end if;
	end loop;

	for ref_record in arr_refs_cur(pEntityId, pValuePid) loop
	  if ref_record.deleteMode = 'SET_NULL' then
	      refIndex := 0;
	      loop
	          refIndex := Rdx_array.searchRef(ref_record.val, pEntityId, pValuePid, refIndex + 1);
	          if refIndex = 0 then
	              exit;
	          end if;
	          Rdx_array.setStr(ref_record.val, null, refIndex);
	      end loop;
	  elsif ref_record.deleteMode = 'REMOVE_VALUE' then
	      RDX_Array.removeAll(ref_record.val, RDX_Array.createRef(pEntityId, pValuePid));
	  end if;
	end loop;

	END;
end;
/

create or replace package body RDX_Entity_Vars as

	begin
	   upValTabByValType(12)    := 'RDX_UPVALDATETIME';
	   
	   upValTabByValType(2)     := 'RDX_UPVALNUM';
	   upValTabByValType(1)    := 'RDX_UPVALNUM';
	   upValTabByValType(11)     := 'RDX_UPVALNUM';
	   
	   upValTabByValType(21)         := 'RDX_UPVALSTR';
	   upValTabByValType(3)        := 'RDX_UPVALSTR';

	   upValTabByValType(31)   := 'RDX_UPVALREF';
	   upValTabByValType(32)   := 'RDX_UPVALREF';

	   upValTabByValType(22)      := 'RDX_UPVALRAW';

	   upValTabByValType(27)     := 'RDX_UPVALCLOB';

	   upValTabByValType(28)     := 'RDX_UPVALBLOB';

	   upValTabByValType(228)      := 'RDX_UPVALCLOB';
	   upValTabByValType(202)       := 'RDX_UPVALCLOB';
	   upValTabByValType(227)      := 'RDX_UPVALCLOB';
	   upValTabByValType(222)       := 'RDX_UPVALCLOB';
	   upValTabByValType(231)      := 'RDX_UPVALCLOB';
	   upValTabByValType(203)     := 'RDX_UPVALCLOB';
	   upValTabByValType(212) := 'RDX_UPVALCLOB';
	   upValTabByValType(221)      := 'RDX_UPVALCLOB';
	   upValTabByValType(201)     := 'RDX_UPVALCLOB';
	   upValTabByValType(211)      := 'RDX_UPVALCLOB';
end;
/

create or replace package body RDX_Environment as

	function calcProgramName(
		instanceId in integer
	) return varchar2
	is
	begin
	    if instanceId is not null then
	        return 'Instance' || '#' || to_char(instanceId);
	    else
	        return '<unknown_radix_prg>';
	    end if;
	end;

	function calcModuleName(
		sessionOwnerType in varchar2,
		sessionOwnerId in integer
	) return varchar2
	is
	    res varchar2(100);
	begin
	    
	    if sessionOwnerType is not null then
	        res := sessionOwnerType;
	        if sessionOwnerId is not null then
	            res := res || '#' || to_char(sessionOwnerId);
	        end if;
	    else 
	        res := '<unknown_radix_mdl>';
	    end if;
	    return res;
	end;

	procedure updateApplicationClientInfo
	is
	--max length of the string passed to dbms_application_info.set_client_info is 64
	    info varchar2(200) := '';
	begin
	    
	    info := RDX_Environment.calcProgramName(RDX_Sys_Vars.instanceId) || '/' || RDX_Environment.calcModuleName(RDX_Sys_Vars.sessionOwnerType, RDX_Sys_Vars.sessionOwnerId);
	    
	    if RDX_Sys_Vars.requestInfo is not null then
	        info := info || '/' || RDX_Sys_Vars.requestInfo;
	    end if;
	    
	    dbms_application_info.set_client_info(info);
	    dbms_application_info.set_action(RDX_Sys_Vars.requestInfo);
	end;

	procedure init(
		instanceId in integer,
		sessionOwnerType in varchar2,
		sessionOwnerId in integer
	)
	is
	begin
	    RDX_Sys_Vars.instanceId := instanceId;
	    RDX_Sys_Vars.sessionOwnerType := sessionOwnerType;
	    RDX_Sys_Vars.sessionOwnerId := sessionOwnerId;
	    RDX_Environment.updateApplicationClientInfo();
	    dbms_application_info.set_module(RDX_Environment.calcProgramName(instanceId) || '/' || RDX_Environment.calcModuleName(sessionOwnerType, sessionOwnerId), null);
	end;

	procedure setRequestInfo(
		requestInfo in varchar2
	)
	is
	begin
	    RDX_Sys_Vars.requestInfo := requestInfo;
	    RDX_Environment.updateApplicationClientInfo();
	end;

	function getTargetExecutorId return integer
	is
	begin
	    if RDX_Sys_Vars.targetExecutorId = -1 then
	        select RDX_INSTANCE.TARGETEXECUTORID into RDX_Sys_Vars.targetExecutorId from RDX_INSTANCE where RDX_INSTANCE.ID = RDX_Sys_Vars.instanceId;
	    end if;
	    return RDX_Sys_Vars.targetExecutorId;
	end;

	function getApplicationClientInfo return varchar2
	is
	info varchar2(100);
	begin
	    DBMS_APPLICATION_INFO.READ_CLIENT_INFO(info); 
	    return info;
	end;

	function getInstanceId return varchar2
	is
	begin
	    return RDX_Sys_Vars.instanceId;
	end;

	function getSessionOwnerType return varchar2
	is
	begin
	return RDX_Sys_Vars.sessionOwnerType;
	end;

	function getSessionOwnerId return integer
	is
	begin
	    return RDX_Sys_Vars.sessionOwnerId;
	end;
end;
/

create or replace package body RDX_JS_CalendarSchedule as

	--constants
	foreread_size CONSTANT INTEGER := 400; --days

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
	    return TO_CHAR(pDate, RDX_JS_Vars.key_format);
	end;

	function keyToDate(
		pKey in varchar2
	) return date deterministic
	is
	begin
	    return TO_DATE(pKey, RDX_JS_Vars.key_format);
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
	    items RDX_JS_Vars.TItemsNestedTable := RDX_JS_Vars.TItemsNestedTable();
	    curItem RDX_JS_Vars.TCalendarItem;
	    vCalendarClassGuid varchar2(30);
	begin
	    select CLASSGUID into vCalendarClassGuid from RDX_JS_CALENDAR where ID = pId;
	    
	    IF vCalendarClassGuid != RDX_JS_Vars.class_guid_abs_calendar THEN
	        raise_application_error(-20001, 'Wrong calendar type');
	    END IF;

	    FOR vCalendarItem in cCalendarItems(pId) LOOP
	        curItem.classGuid :=  vCalendarItem.CLASSGUID;
	        curItem.oper :=  vCalendarItem.OPER;
	        curItem.offsetDir :=  vCalendarItem.OFFSETDIR;
	        curItem.offset :=  vCalendarItem.OFFSET; 
	        curItem.absDate :=  vCalendarItem.ABSDATE;
	        curItem.incCalendarId :=  vCalendarItem.INCCALENDARID;

	        IF curItem.classGuid = RDX_JS_Vars.class_guid_day_of_week THEN
	            IF curItem.OffsetDir < 0 THEN
	                curItem.dayOfWeek :=  curItem.offset + 8;
	            ELSE
	                curItem.dayOfWeek :=  curItem.offset;
	            END IF;
	        END IF;

	        items.EXTEND();
	        items(items.LAST) := curItem;
	    END LOOP;

	    RDX_JS_Vars.cachedItems(pId) := items;
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
	    
	    IF RDX_JS_Vars.lastUpdateTime is null or vLastUp != RDX_JS_Vars.lastUpdateTime THEN
	        RDX_JS_Vars.cachedItems.DELETE();
	        RDX_JS_Vars.cachedPeriodsBegin.DELETE();
	        RDX_JS_Vars.cachedItems.DELETE();
	        RDX_JS_Vars.cachedPeriodsEnd.DELETE();
	        RDX_JS_Vars.cachedDates.DELETE();
	        RDX_JS_Vars.lastUpdateTime := vLastUp;
	    END IF;

	    IF not RDX_JS_Vars.cachedItems.EXISTS(pId) THEN
	        RDX_JS_CalendarSchedule.doRefreshCache(pId => pId);
	    END IF;
	end;

	procedure extendSubPeriod(
		pId in integer,
		pBegin in date,
		pEnd in date
	)
	is
	    vItems CONSTANT RDX_JS_Vars.TItemsNestedTable := RDX_JS_Vars.cachedItems(pId);
	    vCurDate DATE := pBegin;
	    vDates RDX_JS_Vars.TDateMap;          
	    vCurItem RDX_JS_Vars.TCalendarItem;
	    vCounter INT;
	    vResult BOOLEAN;
	    vDayMatches BOOLEAN;
	    vIsInIncCalendar BOOLEAN;
	begin
	    IF RDX_JS_Vars.cachedDates.EXISTS(pId) THEN
	        vDates := RDX_JS_Vars.cachedDates(pId);
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
	                WHEN RDX_JS_Vars.class_guid_day_of_year THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.isDayInYear(vCurItem.OffsetDir, vCurItem.Offset, vCurDate)=1;
	            
	                WHEN RDX_JS_Vars.class_guid_day_of_quarter THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.isDayInQuarter(vCurItem.OffsetDir, vCurItem.Offset, vCurDate)=1;            
	            
	                WHEN RDX_JS_Vars.class_guid_day_of_week THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.getDayOfWeek(pDate => vCurDate) = vCurItem.dayOfWeek;
	                WHEN RDX_JS_Vars.class_guid_day_of_month THEN
	                    IF vCurItem.OffsetDir > 0 THEN
	                        vDayMatches := RDX_JS_CalendarSchedule.getDayOfMonth(pDate => vCurDate) = vCurItem.Offset;
	                    ELSE
	                        vDayMatches := vCurItem.Offset = (RDX_JS_CalendarSchedule.getDaysInMonth(pDate => vCurDate) - RDX_JS_CalendarSchedule.getDayOfMonth(pDate => vCurDate) + 1);
	                    END IF;

	                WHEN RDX_JS_Vars.class_guid_abs_date THEN
	                    vDayMatches := vCurDate = vCurItem.AbsDate;
	                   
	                WHEN RDX_JS_Vars.class_guid_daily THEN
	                    vDayMatches := true;

	                WHEN RDX_JS_Vars.class_guid_inc_calendar THEN
	                    vIsInIncCalendar := RDX_JS_CalendarSchedule.isInCached(pId => vCurItem.incCalendarId, pDate => vCurDate);
	                    IF vIsInIncCalendar is not null THEN
	                         vResult := (vCurItem.oper = '+' and vIsInIncCalendar = true) or 
	                         (vCurItem.oper = '-' and 
	                         vIsInIncCalendar = true -- change in RADIX-12059 from 'false' to 'true'
	                         ); 
	                    END IF;
	            END CASE;

	            if vCurItem.classGuid != RDX_JS_Vars.class_guid_inc_calendar and vDayMatches THEN
	                vResult := vCurItem.Oper = '+';
	            END IF;
	            
	            vCounter := vItems.NEXT(vCounter);
	        END LOOP; --items loop
	        
	        IF vResult IS NOT NULL THEN
	            vDates(RDX_JS_CalendarSchedule.dateToKey(pDate => vCurDate)) := vResult;
	        END IF;

	        vCurDate := vCurDate + 1;

	    END LOOP; --dates loop

	    RDX_JS_Vars.cachedDates(pId) := vDates;
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
	    
	    if not RDX_JS_Vars.cachedPeriodsBegin.EXISTS(pId) THEN
	        vNeedExtension := true;
	    else
	        vBegin := RDX_JS_Vars.cachedPeriodsBegin(pId);
	        vEnd := RDX_JS_Vars.cachedPeriodsEnd(pId);
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
	        RDX_JS_Vars.cachedPeriodsBegin(pId) := vBegin;
	        RDX_JS_Vars.cachedPeriodsEnd(pId) := vEnd;
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

	    IF RDX_JS_Vars.cachedDates.EXISTS(pId)
	            and RDX_JS_Vars.cachedDates(pId).EXISTS(RDX_JS_CalendarSchedule.dateToKey(pDate => pDate)) THEN
	        return RDX_JS_Vars.cachedDates(pId)(RDX_JS_CalendarSchedule.dateToKey(pDate => pDate));
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

	    IF RDX_JS_Vars.cachedDates.EXISTS(pId) THEN
	        vIndex := RDX_JS_CalendarSchedule.dateToKey(pDate => vDate);
	        FOR i in 1..1000 LOOP
	            vIndex := RDX_JS_Vars.cachedDates(pId).NEXT(vIndex);      
	            IF vIndex is null THEN 
	                return null;
	            ELSIF RDX_JS_Vars.cachedDates(pId)(vIndex) = true THEN
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

	    IF RDX_JS_Vars.cachedDates.EXISTS(pId) THEN
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

	    IF RDX_JS_Vars.cachedDates.EXISTS(pId) THEN
	        vIndex := RDX_JS_CalendarSchedule.dateToKey(pDate => vDate); 
	        FOR i in 1..1000 LOOP
	            vIndex := RDX_JS_Vars.cachedDates(pId).PRIOR(vIndex);      
	            IF vIndex is null THEN 
	                return null;
	            ELSIF RDX_JS_Vars.cachedDates(pId)(vIndex) = true THEN
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

	    IF RDX_JS_Vars.cachedDates.EXISTS(pId) THEN
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

create or replace package body RDX_JS_EventSchedule as

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
	    
	    CURSOR cEventScheduleItems (SchId RDX_JS_EVENTSCHDITEM.SCHEDULEID%TYPE)  is
	                                Select CALENDARID calendarId, ENDTIME endTime, EVENTTIME eventTime, PERIOD period,
	                                REPEATABLE repeatable, STARTTIME startTime, TIMEZONEREGION timeZoneRegion
	                                From RDX_JS_EVENTSCHDITEM
	                                Where SCHEDULEID=SchId;
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
	    
	    CURSOR cEventScheduleItems (SchId RDX_JS_EVENTSCHDITEM.SCHEDULEID%TYPE)  is
	                                Select CALENDARID calendarId, ENDTIME endTime, EVENTTIME eventTime, PERIOD period,
	                                REPEATABLE repeatable, STARTTIME startTime, TIMEZONEREGION timeZoneRegion
	                                From RDX_JS_EVENTSCHDITEM
	                                Where SCHEDULEID=SchId;
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

create or replace package body RDX_JS_IntervalSchedule as

	function isIn(
		pId in integer,
		pDateTime in date
	) return integer
	is
	    ts timestamp;
	    minutes int;
	    
	    CURSOR cIntervalSchedule (SchId RDX_JS_INTERVALSCHDITEM.SCHEDULEID%TYPE)  is
	                            Select CALENDARID, ENDTIME, STARTTIME
	                            From RDX_JS_INTERVALSCHDITEM
	                            Where SCHEDULEID=SchId;
	begin
	    if pDateTime IS NULL THEN
	        return 0;                       
	    end if;
	    ts := To_TimeStamp(pDateTime);
	    minutes := extract(Minute from ts) + extract(Hour from ts)* 60;
	    FOR vIntervalSchedule in cIntervalSchedule(pId) LOOP
	        if  (vIntervalSchedule.CALENDARID IS NULL
	                or RDX_JS_CalendarSchedule.isIn(pId => vIntervalSchedule.CALENDARID, pDate => pDateTime) > 0)
	                    and
	                (vIntervalSchedule.STARTTIME <= minutes)
	                    and
	                (vIntervalSchedule.ENDTIME = 0 or vIntervalSchedule.ENDTIME > minutes) THEN
	            return 1;
	        end if;
	    end loop;

	    return 0;
	end;

	function nextStartTime(
		pId in integer,
		pDateTime in date
	) return date
	is
	    ts timestamp;
	    minutes integer;
	    today Date;
	    tomorrow Date;

	    bInToday boolean;
	    tDate Date;
	    minDate Date;
	    minMinutes integer;
	    
	    CURSOR cIntervalSchedule (SchId RDX_JS_INTERVALSCHDITEM.SCHEDULEID%TYPE)  is
	                          Select CALENDARID, ENDTIME, STARTTIME
	                          From RDX_JS_INTERVALSCHDITEM
	                          Where SCHEDULEID=SchId;
	begin
	    if pDateTime IS NULL THEN
	        return NULL;
	    end if;
	    
	    ts := To_TimeStamp(pDateTime);
	    minutes := extract(Minute from ts) + extract(Hour from ts)*60;
	    today := TRUNC(pDateTime);
	    tomorrow := today + 1;
	    
	    FOR vIntervalSchedule in cIntervalSchedule(pId) LOOP   
	        bInToday := vIntervalSchedule.STARTTIME > minutes and 
	            (vIntervalSchedule.CALENDARID IS NULL or RDX_JS_CalendarSchedule.isIn(pId => vIntervalSchedule.CALENDARID, pDate => today) > 0);
	        if bInToday THEN
	            tDate := today;
	        elsif vIntervalSchedule.CALENDARID IS NULL THEN
	            tDate := tomorrow;
	        else
	            tDate := RDX_JS_CalendarSchedule.next(pId => vIntervalSchedule.CALENDARID, pDate => today);
	        end if;

	        if tDate IS NOT NULL THEN
	            if minDate IS NULL or minDate > tDate or (minDate = tDate and minMinutes > vIntervalSchedule.STARTTIME) THEN
	                minDate := tDate;
	                minMinutes := vIntervalSchedule.STARTTIME;
	            end if;
	        end IF;
	    end LOOP; 

	    if minDate IS NOT NULL THEN
	        minDate := minDate + minMinutes/1440;
	    end if;
	    
	    return minDate;
	end;

	function nextFinishTime(
		pId in integer,
		pDateTime in date
	) return date
	is
	    ts timestamp;
	    minutes integer;
	    today Date;
	    tomorrow Date;

	    bInToday boolean;
	    tDate Date;
	    minDate Date;
	    minMinutes integer;
	    
	    CURSOR cIntervalSchedule (SchId RDX_JS_INTERVALSCHDITEM.SCHEDULEID%TYPE)  is
	                          Select CALENDARID, ENDTIME, STARTTIME
	                          From RDX_JS_INTERVALSCHDITEM
	                          Where SCHEDULEID=SchId;
	begin
	    if pDateTime IS NULL THEN
	        return NULL;
	    end if;
	    
	    ts := To_TimeStamp(pDateTime);
	    minutes := extract(Minute from ts) + extract(Hour from ts)*60;
	    today := TRUNC(pDateTime);
	    tomorrow := today + 1;                                                        
	    FOR vIntervalSchedule in cIntervalSchedule(pId) LOOP  
	        bInToday := vIntervalSchedule.ENDTIME > minutes and 
	            (vIntervalSchedule.CALENDARID IS NULL or RDX_JS_CalendarSchedule.isIn(pId => vIntervalSchedule.CALENDARID, pDate => today) > 0);
	        if bInToday THEN
	            tDate := today;
	        elsif vIntervalSchedule.CALENDARID IS NULL THEN
	            tDate := tomorrow;
	        else
	            tDate := RDX_JS_CalendarSchedule.next(pId => vIntervalSchedule.CALENDARID, pDate => today);
	        end if;

	        if tDate IS NOT NULL THEN
	            if minDate IS NULL or minDate > tDate or (minDate = tDate and minMinutes > vIntervalSchedule.ENDTIME) THEN
	                minDate := tDate;
	                minMinutes := vIntervalSchedule.ENDTIME;
	            end if;
	        end IF;
	    end LOOP;      

	    if minDate IS NOT NULL THEN
	        minDate := minDate + minMinutes/1440;
	    end if;
	    
	    return minDate;
	end;

	function prevStartTime(
		pId in integer,
		pDateTime in date
	) return date
	is
	    ts timestamp;
	    minutes integer;
	    today Date;
	    yesterday Date;

	    bInToday boolean;
	    tDate Date;
	    maxDate Date;                                 
	    maxMinutes integer;
	    
	    CURSOR cIntervalSchedule (SchId RDX_JS_INTERVALSCHDITEM.SCHEDULEID%TYPE)  is
	                          Select CALENDARID, ENDTIME, STARTTIME
	                          From RDX_JS_INTERVALSCHDITEM
	                          Where SCHEDULEID=SchId;
	begin
	    if pDateTime IS NULL THEN
	        return NULL;
	    end if;
	    
	    ts := To_TimeStamp(pDateTime);
	    minutes := extract(Minute from ts) + extract(Hour from ts)*60;
	    today := TRUNC(pDateTime);
	    yesterday := today - 1;   
	    
	    FOR vIntervalSchedule in cIntervalSchedule(pId) LOOP      
	        bInToday := vIntervalSchedule.STARTTIME < minutes and 
	            (vIntervalSchedule.CALENDARID IS NULL or RDX_JS_CalendarSchedule.isIn(pId => vIntervalSchedule.CALENDARID, pDate => today) > 0);
	        if bInToday THEN
	            tDate := today;
	        elsif vIntervalSchedule.CALENDARID IS NULL THEN
	            tDate := yesterday;
	        else
	            tDate := RDX_JS_CalendarSchedule.prev(pId => vIntervalSchedule.CALENDARID, pDate => today);
	        end if;

	        if tDate IS NOT NULL THEN
	            if maxDate IS NULL or maxDate < tDate or (maxDate = tDate and maxMinutes < vIntervalSchedule.STARTTIME) THEN
	                maxDate := tDate;
	                maxMinutes := vIntervalSchedule.STARTTIME;
	            end if;
	        end IF;
	    end LOOP;                     

	    if maxDate IS NOT NULL THEN
	        maxDate := maxDate + maxMinutes/1440;
	    end if;
	    return maxDate;
	end;

	function prevFinishTime(
		pId in integer,
		pDateTime in date
	) return date
	is
	    ts timestamp;
	    minutes integer;
	    today Date;
	    yesterday Date;  

	    bInToday boolean;
	    tDate Date;
	    maxDate Date;                                             
	    maxMinutes integer;
	    
	    CURSOR cIntervalSchedule (SchId RDX_JS_INTERVALSCHDITEM.SCHEDULEID%TYPE)  is
	                          Select CALENDARID, ENDTIME, STARTTIME
	                          From RDX_JS_INTERVALSCHDITEM
	                          Where SCHEDULEID=SchId;
	begin
	    if pDateTime IS NULL THEN
	        return NULL;
	    end if;
	    
	    ts := To_TimeStamp(pDateTime);
	    minutes := extract(Minute from ts) + extract(Hour from ts)*60;
	    today := TRUNC(pDateTime);
	    yesterday := today - 1;
	    
	    FOR vIntervalSchedule in cIntervalSchedule(pId) LOOP 
	        bInToday := vIntervalSchedule.ENDTIME < minutes and
	            (vIntervalSchedule.CALENDARID IS NULL or RDX_JS_CalendarSchedule.isIn(pId => vIntervalSchedule.CALENDARID, pDate => today) > 0);
	        if bInToday THEN
	            tDate := today;
	        elsif vIntervalSchedule.CALENDARID IS NULL THEN
	            tDate := yesterday;
	        else
	            tDate := RDX_JS_CalendarSchedule.prev(pId => vIntervalSchedule.CALENDARID, pDate => today);
	        end if;

	        if tDate IS NOT NULL THEN
	            if maxDate IS NULL or maxDate < tDate or (maxDate = tDate and maxMinutes < vIntervalSchedule.ENDTIME) THEN
	                maxDate := tDate;
	                maxMinutes := vIntervalSchedule.ENDTIME;
	            end if;
	        end IF;
	    end LOOP;    

	    if maxDate IS NOT NULL THEN
	        maxDate := maxDate + maxMinutes/1440;
	    end if;
	    
	    return maxDate;
	end;
end;
/

create or replace package body RDX_JS_JOB as

	function calcPriority(
		iBasePriority in integer,
		iCurPriority in integer,
		iPriorityBoostingSpeed in integer,
		tDueTime in timestamp
	) return integer
	is
	   iBoostDelayInSec integer;
	   tCurrentTime timestamp;
	   tElapsedInterval interval day to second;
	   iElapsedSeconds integer;
	   iDelaysCount integer;
	   iMaxCount integer;
	   iBoostedPriority integer;              
	begin
	   if (iPriorityBoostingSpeed<1) then
	      return iBasePriority;
	   end if;
	       
	   select DELAY, MAXINCREMENT, systimestamp into iBoostDelayInSec, iMaxCount, tCurrentTime
	   from RDX_JS_JOBEXECUTORUNITBOOST
	   where SPEED = iPriorityBoostingSpeed;
	   
	   if (tCurrentTime<=tDueTime) then
	      return iBasePriority;
	   end if;   
	   
	   tElapsedInterval := tCurrentTime - tDueTime;
	   
	   iElapsedSeconds :=
	      (extract(day from tElapsedInterval)) * 24*60*60 +
	      (extract(hour from tElapsedInterval)) * 60*60 +
	      (extract(minute from tElapsedInterval)) * 60 +
	      (extract(second from tElapsedInterval));
	      
	   iDelaysCount:=iElapsedSeconds/iBoostDelayInSec;
	   
	   if (iDelaysCount>iMaxCount) then
	      iDelaysCount := iMaxCount;
	   end if;
	   
	   iBoostedPriority := iBasePriority + iDelaysCount;
	   
	   if (iBoostedPriority>iCurPriority) then      
	      RDX_Trace.put(
	         2,
	         null, 
	         'Priority boosting. Base priority: ' || iBasePriority || ', boosted priority: ' || iBoostedPriority || ', delay: ' || iElapsedSeconds || 's',
	         'Server.Unit.JobExecutor');
	   end if;
	   return iBoostedPriority;
	exception
	   when NO_DATA_FOUND then return iBasePriority;
	end;

	function scheduleRelative(
		pAllowRerun in integer,
		iDelayMillis in integer,
		sJobClass in varchar2,
		sJobMethod in varchar2,
		iJobPriority in integer,
		iJobBoosting in integer,
		sExecRequesterId in varchar2,
		sCreatorEntityGuid in varchar2 := null,
		sCreatorPid in varchar2 := null,
		sTitle in varchar2 := null,
		sScpName in varchar2 := null,
		iTaskId in integer := null
	) return integer
	is
	begin  
	  return RDX_JS_JOB.schedule(iAllowRerun => pAllowRerun, tJobTime => systimestamp+numtodsinterval(iDelayMillis/1000, 'SECOND'), sJobClass => sJobClass, sJobMethod => sJobMethod, iJobPriority => iJobPriority, iJobBoosting => iJobBoosting, sExecRequesterId => sExecRequesterId, sCreatorEntityGuid => sCreatorEntityGuid, sCreatorPid => sCreatorPid, sTitle => sTitle, sScpName => sScpName, iTaskId => iTaskId);
	end;

	function schedule(
		iAllowRerun in integer,
		tJobTime in timestamp,
		sJobClass in varchar2,
		sJobMethod in varchar2,
		iJobPriority in integer,
		iJobBoosting in integer,
		sExecRequesterId in varchar2,
		sCreatorEntityGuid in varchar2 := null,
		sCreatorPid in varchar2 := null,
		sTitle in varchar2 := null,
		sScpName in varchar2 := null,
		iTaskId in integer := null,
		pAadcMemberId in integer := null
	) return integer
	is
	   iJobNewID number(18);
	   vAadcMemberId integer;
	begin
	   select SQN_RDX_JS_JOBID.NextVal into iJobNewID from dual;
	   if pAadcMemberId is null then
	      select AADCMEMBERID into vAadcMemberId from RDX_SYSTEM where ID = 1;
	   else
	      vAadcMemberId := pAadcMemberId;
	   end if;               
	   insert into RDX_JS_JOBQUEUE(ID,CREATORENTITYGUID, CREATORPID, DUETIME, CLASSNAME, METHODNAME, PRIORITY, CURPRIORITY, PRIORITYBOOSTINGSPEED, EXECREQUESTERID, TITLE, 
	        SCPNAME, TASKID, EXECUTORID, ALLOWRERUN, AADCMEMBERID)
	   values (iJobNewID, sCreatorEntityGuid, sCreatorPid,tJobTime, sJobClass, sJobMethod, nvl(iJobPriority,5), nvl(iJobPriority,5), iJobBoosting, sExecRequesterId, sTitle, 
	        sScpName, iTaskId, RDX_Environment.getTargetExecutorId(), iAllowRerun, vAadcMemberId);
	  return iJobNewID;
	end;

	procedure awake(
		iJobId in number
	)
	is
	begin
	  Update RDX_JS_JOBQUEUE set DUETIME=sysTimeStamp where ID=iJobID;  
	end;

	function addParam(
		iParJobId in number,
		sParName in varchar2,
		iParValType in integer,
		sParVal in varchar2
	) return integer
	is
	   iParSeq integer;
	begin
	   select count(*) into iParSeq
	   from RDX_JS_JOBPARAM 
	   where JOBID=iParJobId;
	   
	   insert into RDX_JS_JOBPARAM(JOBID, NAME, SEQ, VALTYPE, VAL)
	   values (iParJobId, sParName, iParSeq, iParValType, sParVal);
	   
	   return iParSeq;
	end;

	function addBlobParam(
		iParJobId in number,
		sParName in varchar2,
		parVal in blob
	) return integer
	is
	   iParSeq integer;
	begin
	   select count(*) into iParSeq
	   from RDX_JS_JOBPARAM 
	   where JOBID=iParJobId;
	   
	   insert into RDX_JS_JOBPARAM(JOBID, NAME, SEQ, VALTYPE, BLOBVAL)
	   values (iParJobId, sParName, iParSeq, 28, parVal);
	   
	   return iParSeq;
	end;

	function addClobParam(
		iParJobId in number,
		sParName in varchar2,
		parVal in clob
	) return integer
	is
	   iParSeq integer;
	begin
	   select count(*) into iParSeq
	   from RDX_JS_JOBPARAM 
	   where JOBID=iParJobId;
	   
	   insert into RDX_JS_JOBPARAM(JOBID, NAME, SEQ, VALTYPE, CLOBVAL)
	   values (iParJobId, sParName, iParSeq, 27, parVal);
	   
	   return iParSeq;
	end;

	function setParam(
		iParJobId in number,
		sParName in varchar2,
		sParVal in varchar2
	) return integer
	is
	   iParSeq integer;
	begin
	   select SEQ into iParSeq
	   from RDX_JS_JOBPARAM 
	   where JOBID=iParJobId and NAME=sParName;
	   
	   update RDX_JS_JOBPARAM 
	   set VAL=sParVal
	   where JOBID=iParJobId and NAME=sParName;
	   
	   return iParSeq;
	end;

	procedure shedulePeriodical(
		pTaskId in integer,
		pExecTime in date
	)
	is
	  taskTitle varchar2(500);
	begin
	   
	  select t.TITLE into taskTitle
	  from RDX_JS_TASK t where t.ID = pTaskId;
	  
	  RDX_JS_JOB.scheduleTask(taskId => pTaskId, initiatorName => 'Task #' || pTaskId || ' - ' || taskTitle, execTime => pExecTime, execToken => null);
	end;

	function calcThreadPriorityMap return varchar2
	is
	mapStr varchar2(100);
	cursor priorityCursor
	    is
	    select RDX_JS_JOBEXECUTORPRIORITYMAP.APPPRIORITY, RDX_JS_JOBEXECUTORPRIORITYMAP.SYSPRIORITY from RDX_JS_JOBEXECUTORPRIORITYMAP;
	priorityRec priorityCursor%ROWTYPE;
	isFirst number(1,0);
	begin
	    mapStr := '';
	    isFirst := 1;
	    for priorityRec in priorityCursor() loop
	        
	        if isFirst = 1 then
	            isFirst := 0;
	        else
	            mapStr := mapStr || ',';
	        end if;
	        
	        mapStr := mapStr || priorityRec.APPPRIORITY || '=' || priorityRec.SYSPRIORITY;
	    end loop;    
	    return mapStr;
	end;

	function adjustToNearestMinute(
		iTimestamp in timestamp
	) return timestamp
	is
	    tempTimestamp timestamp;
	begin
	    tempTimestamp := iTimestamp + interval '30' second;
	    return tempTimestamp - NUMTODSINTERVAL(extract (second from tempTimestamp), 'SECOND');
	end;

	procedure onStartJobExecution(
		jobId in integer
	)
	is
	 cnt integer := 0;
	cursor job_cur
	is 
	    select RDX_JS_JOBQUEUE.ID, RDX_JS_JOBQUEUE.TITLE, RDX_JS_JOBQUEUE.PROCESSORTITLE, RDX_JS_JOBQUEUE.TASKID from RDX_JS_JOBQUEUE
	    where RDX_JS_JOBQUEUE.ID = jobId
	    for update of RDX_JS_JOBQUEUE.PROCESSORTITLE;
	begin
	    for job_rec in job_cur
	    loop
	        cnt := cnt + 1;
	        if job_rec.PROCESSORTITLE is not null then
	            raise_application_error(-20000, 'Job #' || to_char(job_rec.ID) || ' "' || job_rec.TITLE || '"' || ' is already executing by ' || job_rec.PROCESSORTITLE);
	        end if;
	        update RDX_JS_JOBQUEUE set RDX_JS_JOBQUEUE.PROCESSORTITLE = RDX_Environment.getApplicationClientInfo(), RDX_JS_JOBQUEUE.SELFCHECKTIME = sysdate where current of job_cur;
	        if job_rec.TASKID is not null then
	            update RDX_JS_TASK set RDX_JS_TASK.SELFCHECKTIME = sysdate where RDX_JS_TASK.ID = job_rec.TASKID;
	        end if;
	    end loop;
	    
	    if cnt = 0 then
	        raise_application_error(-20000, 'Job #' || to_char(jobId) || ' does not exist');
	    end if;
	    
	    commit;
	end;

	procedure scheduleTask(
		taskId in integer,
		initiatorName in varchar2,
		-- null - now
		execTime in date,
		-- not null for nested tasks, taken from root task
		execToken in integer
	)
	is
	  vJobId number;
	  priority number;
	  priorityBoostingSpeed number;
	  scpName varchar2(200);
	  jobTitle varchar2(1000);
	  taskTitle varchar2(500);
	  curStatus varchar2(100);
	  actualExecTime date;
	  vAadcMemberId integer;
	begin
	  select           
	    t.PRIORITY, t.PRIORITYBOOSTINGSPEED, t.STATUS, t.TITLE,
	    (select t1.SCPNAME from RDX_JS_TASK t1 
	        where t1.SCPNAME is not null
	        start with t1.ID = t.ID
	        connect by prior t1.PARENTID = t1.ID and prior t1.SCPNAME is null),
	    t.AADCMEMBERID 
	    into priority, priorityBoostingSpeed, curStatus, taskTitle, scpName, vAadcMemberId
	  from RDX_JS_TASK t where t.ID = taskId for update of t.STATUS;
	  
	  if curStatus = 'Scheduled' or curStatus = 'Executing' or curStatus = 'Cancelling' then
	    raise_application_error(-20000, 'Task #' || to_char(taskId) || ' has wrong status:' || curStatus);
	  end if;
	  
	  select nvl(execTime, sysdate) into actualExecTime from dual;
	  
	  jobTitle := 'Initial job for task #' || to_char(taskId) || ' ''' || taskTitle || ''' created by ' || initiatorName;
	  
	  vJobId := RDX_JS_JOB.schedule(iAllowRerun => 0, tJobTime => actualExecTime, sJobClass => RDX_JS_Vars.TASK_EXECUTOR_CLASS_NAME, sJobMethod => RDX_JS_Vars.TASK_EXECUTOR_METHOD_NAME, iJobPriority => priority, iJobBoosting => priorityBoostingSpeed, sExecRequesterId => 'ScheduledTask-'|| taskId, sCreatorEntityGuid => 'tblWZB7K4HLJPOBDCIUAALOMT5GDM', sCreatorPid => taskId, sTitle => jobTitle, sScpName => scpName, iTaskId => taskId, pAadcMemberId => vAadcMemberId);
	    
	  insert into RDX_JS_JOBPARAM
	  columns (JOBID, NAME, SEQ, VALTYPE, VAL)
	  values (vJobId, 'taskId', 1, 2, taskId);
	  
	  insert into RDX_JS_JOBPARAM
	  columns (JOBID, NAME, SEQ, VALTYPE, VAL)
	  values (vJobId, 'execToken', 2, 2, execToken);

	  insert into RDX_JS_JOBPARAM
	  columns (JOBID, NAME, SEQ, VALTYPE, VAL)
	  select vJobId, 'prevExecTime', 3, 12, RDX_ValAsStr.dateTimeToValAsStr(PREVEXECTIME)
	     from RDX_JS_TASK
	     where ID = taskId;

	  insert into RDX_JS_JOBPARAM
	  columns (JOBID, NAME, SEQ, VALTYPE, VAL)
	  values (vJobId, 'curExecTime', 4, 12, RDX_ValAsStr.dateTimeToValAsStr(actualExecTime));
	    
	  update RDX_JS_TASK set 
	    LASTEXECTIME = actualExecTime,
	    STATUS = 'Scheduled',
	    RDX_JS_TASK.LASTSCHEDULINGTIME = sysdate
	  where ID = taskId; 
	end;

	function isTaskExecuteCall(
		pClassName in varchar2,
		pMethodName in varchar2
	) return integer
	is
	begin
	    
	    if pClassName = RDX_JS_Vars.TASK_EXECUTOR_CLASS_NAME and pMethodName = RDX_JS_Vars.TASK_EXECUTOR_METHOD_NAME then
	        return 1;
	    end if;
	    return 0;
	end;

	function adjustToNearest10Sec(
		iTimestamp in timestamp
	) return timestamp
	is
	    tempTimestamp timestamp;
	begin
	    tempTimestamp := iTimestamp + interval '5' second;
	    return tempTimestamp - NUMTODSINTERVAL(mod(extract (second from tempTimestamp), 10), 'SECOND');
	end;
end;
/

create or replace package body RDX_WF as



	function curUserIsProcessAdmin(
		pProcessId in integer
	) return integer
	is
	    adminRoleIds Clob;
	    accessArea TRdxAcsAreaList;
	    adminRoles RDX_Array.ARR_STR;
	begin
	    select RDX_ACS.strToAreaList(ACCESSAREA)
	        into accessArea
	        from RDX_WF_PROCESS
	        where RDX_WF_PROCESS.ID = pProcessId;

	    if RDX_ACS.curUserHasRoleInArea(RDX_WF_Vars.ADMIN_ROLE, accessArea) = 0 and RDX_ACS.curUserHasRoleInArea(RDX_WF_Vars.DEBUG_ROLE, accessArea) = 0 then
	        return 0;
	    end if;
	/*    
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where username = RDX_Arte.getUserName()) loop
	        if RDX_ACS.curUserHasRoleInArea(ind.ROLEID, accessArea) != 0 then
	            return 1;
	        end if;
	    end loop;
	*/    
	    select to_clob(RDX_WF_PROCESSTYPE.ADMINROLEGUIDS)
	        into adminRoleIds
	        from RDX_WF_PROCESSTYPE
	        where RDX_WF_PROCESSTYPE.GUID = (select RDX_WF_PROCESS.TYPEGUID from RDX_WF_PROCESS where RDX_WF_PROCESS.ID = pProcessId);
	    
	    adminRoles := RDX_Array.fromStr(adminRoleIds);    
	    if adminRoles.COUNT > 0 then
	        for idx in adminRoles.FIRST .. adminRoles.LAST loop
	            if RDX_ACS.curUserHasRoleInArea(adminRoles(idx), accessArea) != 0 then
	                return 1;
	            end if;
	        end loop;
	        return 0;
	     end if;      
	        
	    return 1;
	end;

	function curUserIsFormAdmin(
		pFormId in integer
	) return integer
	is
	    adminRoleIds Clob;
	    accessArea TRdxAcsAreaList;
	    adminRoles RDX_Array.ARR_STR;
	begin
	    select RDX_ACS.strToAreaList(nvl(RDX_WF_FORM.ACCESSAREA, RDX_WF_PROCESS.ACCESSAREA))
	        into accessArea
	        from RDX_WF_FORM, RDX_WF_PROCESS
	        where RDX_WF_FORM.ID = pFormId and RDX_WF_PROCESS.ID = RDX_WF_FORM.PROCESSID;

	    if RDX_ACS.curUserHasRoleInArea(RDX_WF_Vars.ADMIN_ROLE, accessArea) = 0 and RDX_ACS.curUserHasRoleInArea(RDX_WF_Vars.DEBUG_ROLE, accessArea) = 0then
	        return 0;
	    end if;
	/*    
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where username = RDX_Arte.getUserName()) loop
	        if RDX_ACS.curUserHasRoleInArea(ind.ROLEID, accessArea) != 0 then
	            return 1;
	        end if;
	    end loop;
	*/    
	    select to_clob(nvl(RDX_WF_FORM.ADMINROLEGUIDS, RDX_WF_PROCESSTYPE.ADMINROLEGUIDS))
	        into adminRoleIds
	        from RDX_WF_FORM, RDX_WF_PROCESS, RDX_WF_PROCESSTYPE
	        where RDX_WF_FORM.ID = pFormId and RDX_WF_PROCESS.ID = RDX_WF_FORM.PROCESSID and RDX_WF_PROCESSTYPE.GUID = RDX_WF_PROCESS.TYPEGUID;

	    adminRoles := RDX_Array.fromStr(adminRoleIds);    
	    if adminRoles.COUNT > 0 then
	        for idx in adminRoles.FIRST .. adminRoles.LAST loop
	            if RDX_ACS.curUserHasRoleInArea(adminRoles(idx), accessArea) != 0 then
	                return 1;
	            end if;
	        end loop;
	        return 0;
	     end if;      
	        
	    return 1;
	end;

	function curUserIsFormClerk(
		pFormId in integer
	) return integer
	is
	begin
	    return RDX_WF.userIsFormClerk(RDX_Arte.getUserName(), pFormId);
	end;

	function userIsFormClerk(
		pUser in varchar2,
		pFormId in integer
	) return integer
	is
	    adminRoleIds Clob;
	    accessArea TRdxAcsAreaList;
	    clerkRoles RDX_Array.ARR_STR;
	begin
	    select RDX_ACS.strToAreaList(nvl(RDX_WF_FORM.ACCESSAREA, RDX_WF_PROCESS.ACCESSAREA))
	        into accessArea
	        from RDX_WF_FORM, RDX_WF_PROCESS
	        where RDX_WF_FORM.ID = pFormId and RDX_WF_PROCESS.ID = RDX_WF_FORM.PROCESSID;

	    if RDX_ACS.userHasRoleInArea(pUser, RDX_WF_Vars.CLERK_ROLE, accessArea) = 0 then
	        return 0;
	    end if;
	/*    
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where username = pUser) loop
	        if RDX_ACS.userHasRoleInArea(pUser, ind.ROLEID, accessArea) = 0 then
	            return 0;
	        end if;
	    end loop;
	*/        
	    select to_clob(nvl(RDX_WF_FORM.CLERKROLEGUIDS, RDX_WF_PROCESSTYPE.CLERKROLEGUIDS))
	        into adminRoleIds
	        from RDX_WF_FORM, RDX_WF_PROCESS, RDX_WF_PROCESSTYPE
	        where RDX_WF_FORM.ID = pFormId and RDX_WF_PROCESS.ID = RDX_WF_FORM.PROCESSID and RDX_WF_PROCESSTYPE.GUID = RDX_WF_PROCESS.TYPEGUID;
	    
	    clerkRoles := RDX_Array.fromStr(adminRoleIds);    
	    if clerkRoles.COUNT > 0 then
	        for idx in clerkRoles.FIRST .. clerkRoles.LAST loop
	            if RDX_ACS.userHasRoleInArea(pUser, clerkRoles(idx), accessArea) != 0 then
	                return 1;
	            end if;
	        end loop;
	        return 0;
	     end if;      
	        
	    return 1;
	end;

	function getFormAccessibility(
		iFormId in integer
	) return integer
	IS
	   result INTEGER;

	BEGIN
	   SELECT 2 * RDX_ACS.curUserHasRoleInArea(ADMINROLEGUIDS, RDX_ACS.strToAreaList(ACCESSAREA)) + 
	              RDX_ACS.curUserHasRoleInArea(CLERKROLEGUIDS, RDX_ACS.strToAreaList(ACCESSAREA))
	     INTO result     
	     FROM RDX_WF_FORM
	    WHERE ID=iFormId;
	   
	   RETURN result;  
	END;

	function getProcessAccessibility(
		iProcessId in integer
	) return integer
	IS
	   result INTEGER := 1;

	BEGIN
	/***
	   SELECT DECODE
	          (
	             RDX_ACS.curUserHasRoleInArea(..., RDX_ACS.strToAreaList(ACCESSAREA)), 1, 
	             2,
	             DECODE
	             (
	                RDX_Arte.getUserName, OWNERNAME, 
	                1,
	                0
	             )
	          )
	     INTO result     
	     FROM RDX_WF_PROCESS
	    WHERE ID=iProcessId; 
	***/    
	   RETURN result; 
	END;

	function getSuitableClerk(
		pFormId in integer
	) return varchar2
	is
	begin
	    for c in 
	    (
	        select RDX_AC_USER.NAME from RDX_AC_USER 
	        where RDX_WF.userIsFormClerk(RDX_AC_USER.NAME, pFormId) != 0
	        and not exists(select 1 from RDX_AC_USER2ROLE where RDX_AC_USER2ROLE.USERNAME = RDX_AC_USER.NAME and RDX_AC_USER2ROLE.ROLEID = 'rolSUPER_ADMIN_______________') 
	        order by RDX_WF.getStateFormCount(RDX_AC_USER.NAME, 1) asc
	    ) loop
	        return c.name;
	    end loop;
	    return null;
	end;

	function getActiveSuitableClerk(
		pFormId in integer
	) return varchar2
	is
	    sessionInactivityMins integer;
	begin
	    select RDX_SYSTEM.EASSESSIONINACTIVITYMINS 
	        into sessionInactivityMins
	        from RDX_SYSTEM where RDX_SYSTEM.ID = 1;
	        
	    for c in 
	    (
	        select distinct RDX_AC_USER.NAME from RDX_AC_USER, RDX_EASSESSION
	        where RDX_EASSESSION.USERNAME = RDX_AC_USER.NAME
	        and RDX_WF.userIsFormClerk(RDX_AC_USER.NAME, pFormId) != 0 
	        and not exists(select 1 from RDX_AC_USER2ROLE where RDX_AC_USER2ROLE.USERNAME = RDX_AC_USER.NAME and RDX_AC_USER2ROLE.ROLEID = 'rolSUPER_ADMIN_______________') 
	        and RDX_EASSESSION.LASTCONNECTTIME > sysdate - 1.*sessionInactivityMins/24/60
	        order by RDX_WF.getStateFormCount(RDX_AC_USER.NAME, 1) asc
	    ) loop
	        return c.name;
	    end loop;
	    return null;
	end;



	function getFormState(
		iFormId in integer
	) return integer
	IS
	   dCloseTime      DATE;
	   dDueTime        DATE;
	   dOverdueTime    DATE;
	   dExpirationTime DATE;
	   dCurrentTime    DATE;
	   iProcessId      INTEGER;

	BEGIN
	   SELECT CLOSETIME, DUETIME, OVERDUETIME, EXPIRATIONTIME, PROCESSID
	     INTO dCloseTime, dDueTime, dOverdueTime, dExpirationTime, iProcessId
	     FROM RDX_WF_FORM
	    WHERE ID=iFormId;
	    
	   dCurrentTime := RDX_WF.getCurrentTime(iProcessId);
	   return RDX_WF.getFormState(dCloseTime, dDueTime, dOverdueTime, dExpirationTime, dCurrentTime);
	/*
	   IF dCloseTime IS NOT NULL THEN
	      IF dExpirationTime IS NULL OR dCloseTime<dExpirationTime THEN
	         RETURN 4;
	      ELSE
	         RETURN 3;
	      END IF;   
	   END IF;

	   IF dOverdueTime IS NOT NULL AND dCurrentTime>dOverdueTime THEN
	      RETURN 2;
	   ELSIF dCurrentTime>dDueTime THEN
	      RETURN 1;
	   END IF;

	   RETURN 0;
	*/   
	END;

	function getFormState(
		closeTime in date,
		dueTime in date,
		overdueTime in date,
		expirationTime in date,
		currentTime in date
	) return integer
	IS
	BEGIN
	   IF closeTime IS NOT NULL THEN
	      IF expirationTime IS NULL OR closeTime<expirationTime THEN
	         RETURN 4;
	      ELSE
	         RETURN 3;
	      END IF;   
	   END IF;

	   IF overdueTime IS NOT NULL AND currentTime>overdueTime THEN
	      RETURN 2;
	   ELSIF currentTime>dueTime THEN
	      RETURN 1;
	   END IF;

	   RETURN 0;
	END;



	function getOwnProcessCount(
		sUserName in varchar2
	) return integer
	IS
	   c INTEGER;

	BEGIN
	   SELECT COUNT(*)
	     INTO c
	     FROM RDX_WF_PROCESS
	    WHERE (OWNERNAME=sUserName OR OWNERNAME IS NULL AND sUserName IS NULL) AND
	          RDX_WF_PROCESS.STATE=1;

	   RETURN c;
	END;

	function getStateFormCount(
		sUserName in varchar2,
		iFormState in integer
	) return integer
	IS
	   c INTEGER;
	   
	BEGIN
	   SELECT COUNT(*)
	     INTO c
	     FROM RDX_WF_FORM
	    WHERE (CLERKNAME=sUserName OR CLERKNAME IS NULL AND sUserName IS NULL) AND
	          RDX_WF.getFormState(ID)=iFormState;
	   
	   RETURN c;       
	END;





	function getAreaCount(
		areaList in TRdxAcsAreaList
	) return integer
	IS
	   n INTEGER;

	BEGIN
	   n := 0;
	   IF areaList IS NOT NULL THEN 
	      IF areaList.LAST IS NOT NULL THEN
	         n := areaList.LAST;
	      END IF;   
	   END IF;
	   RETURN n;   
	END;

	function getAreaPointCount(
		areaList in TRdxAcsAreaList,
		areaNumber in integer
	) return integer
	IS
	   n INTEGER;
	   a TRdxAcsArea;

	BEGIN
	   n := 0;
	   IF areaList IS NOT NULL THEN
	      IF areaList.LAST IS NOT NULL AND areaList.LAST>=areaNumber THEN
	         a := areaList(areaNumber);
	         IF a IS NOT NULL THEN
	            IF a.boundaries IS NOT NULL THEN
	               IF a.boundaries.LAST IS NOT NULL THEN
	                  n := a.boundaries.LAST;
	               END IF;   
	            END IF;
	         END IF;      
	      END IF;
	   END IF;
	   RETURN n;   
	END;

	procedure getAreaPoint(
		areaList in TRdxAcsAreaList,
		areaNumber in integer,
		pointNumber in integer,
		familyId out varchar2,
		keyVal out varchar2
	)
	IS 
	   a TRdxAcsArea;
	   c TRdxAcsCoordinate;

	BEGIN
	   familyId := NULL;
	   keyVal := NULL;

	   IF areaList IS NOT NULL THEN
	      IF areaList.LAST IS NOT NULL AND areaList.LAST>=areaNumber THEN
	         a := areaList(areaNumber);
	         IF a IS NOT NULL THEN
	            IF a.boundaries.LAST IS NOT NULL AND a.boundaries.LAST>=pointNumber THEN  
	               c := areaList(areaNumber).boundaries(pointNumber);
	               IF c IS NOT NULL THEN
	                  familyId := c.FamilyId;
	                  keyVal := c.KeyVal;
	               END IF;
	            END IF;
	         END IF;
	      END IF;
	   END IF;               
	END;

	procedure setAreaPoint(
		areaList in out TRdxAcsAreaList,
		areaNumber in integer,
		pointNumber in integer,
		familyId in varchar2,
		keyVal in varchar2
	)
	IS
	   a TRdxAcsArea;
	   i INTEGER;
	   pointIndex INTEGER;

	BEGIN
	   IF areaList IS NULL THEN
	      areaList := TRdxAcsAreaList();
	   END IF;
	          
	   WHILE areaList.LAST IS NULL OR areaList.LAST<areaNumber LOOP
	      areaList.EXTEND;
	   END LOOP;
	   
	   a := areaList(areaNumber);

	   IF a IS NULL THEN
	      a := TRdxAcsArea(TRdxAcsCoordinates());
	   END IF;
	                
	   WHILE a.boundaries.LAST IS NULL OR a.boundaries.LAST<pointNumber LOOP
	      a.boundaries.EXTEND;
	   END LOOP;
	                  
	   IF a.boundaries(pointNumber) IS NULL THEN
	      a.boundaries(pointNumber) := TRdxAcsCoordinate(0, familyId, keyVal);
	   ELSE
	      a.boundaries(pointNumber).FamilyId := familyId;
	      a.boundaries(pointNumber).KeyVal := keyVal;          
	   END IF;
	   
	   areaList(areaNumber) := a;
	END;



	procedure getFormArea(
		formId in integer,
		area out TRdxAcsAreaList
	)
	IS
	BEGIN
	   SELECT RDX_ACS.strToAreaList(ACCESSAREA)
	     INTO area
	     FROM RDX_WF_FORM
	    WHERE ID=formId;
	END;

	procedure setFormArea(
		formId in integer,
		area in TRdxAcsAreaList
	)
	IS
	BEGIN
	   UPDATE RDX_WF_FORM
	      SET ACCESSAREA=RDX_ACS.areaListToStr(area)
	    WHERE ID=formId;              
	END;

	procedure clearFormArea(
		formId in integer
	)
	IS
	BEGIN
	   RDX_WF.setFormArea(formId, NULL);
	END;

	function getFormAreaCount(
		formId in integer
	) return integer
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getFormArea(formId, areaList);
	   RETURN RDX_WF.getAreaCount(areaList);    
	END;

	function getFormAreaPointCount(
		formId in integer,
		areaNumber in integer
	) return integer
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getFormArea(formId, areaList);
	   RETURN RDX_WF.getAreaPointCount(areaList, areaNumber);    
	END;

	procedure getFormAreaPoint(
		formId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId out varchar2,
		keyVal out varchar2
	)
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getFormArea(formId, areaList);
	   RDX_WF.getAreaPoint(areaList, areaNumber, pointNumber, familyId, keyVal);    
	END;

	procedure setFormAreaPoint(
		formId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId in varchar2,
		keyVal in varchar2
	)
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getFormArea(formId, areaList);
	   RDX_WF.setAreaPoint(areaList, areaNumber, pointNumber, familyId, keyVal);
	   RDX_WF.setFormArea(formId, areaList);    
	END;



	procedure getProcessArea(
		processId in integer,
		area out TRdxAcsAreaList
	)
	IS
	BEGIN
	   SELECT RDX_ACS.strToAreaList(ACCESSAREA)
	     INTO area
	     FROM RDX_WF_PROCESS 
	    WHERE ID=processId;
	END;

	procedure setProcessArea(
		processId in integer,
		area in TRdxAcsAreaList
	)
	IS
	BEGIN
	   UPDATE RDX_WF_PROCESS
	      SET ACCESSAREA=RDX_ACS.areaListToStr(area)
	    WHERE ID=processId;  
	END;

	procedure clearProcessArea(
		processId in integer
	)
	IS
	BEGIN
	   RDX_WF.setProcessArea(processId, NULL);
	END;

	function getProcessAreaCount(
		processId in integer
	) return integer
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getProcessArea(processId, areaList);
	   RETURN RDX_WF.getAreaCount(areaList);    
	END;

	function getProcessAreaPointCount(
		processId in integer,
		areaNumber in integer
	) return integer
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getProcessArea(processId, areaList);
	   RETURN RDX_WF.getAreaPointCount(areaList, areaNumber);    
	END;

	procedure getProcessAreaPoint(
		processId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId out varchar2,
		keyVal out varchar2
	)
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getProcessArea(processId, areaList);
	   RDX_WF.getAreaPoint(areaList, areaNumber, pointNumber, familyId, keyVal);    
	END;

	procedure setProcessAreaPoint(
		processId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId in varchar2,
		keyVal in varchar2
	)
	IS 
	   areaList TRdxAcsAreaList;

	BEGIN
	   RDX_WF.getProcessArea(processId, areaList);
	   RDX_WF.setAreaPoint(areaList, areaNumber, pointNumber, familyId, keyVal);
	   RDX_WF.setProcessArea(processId, areaList);    
	END;

	function getCurrentTime(
		processId in integer
	) return date
	is
	   dCurrentTime Date;
	begin
	    select RDX_WF_PROCESS.SIMULATEDTIME into dCurrentTime from RDX_WF_PROCESS where RDX_WF_PROCESS.ID = processId;
	    if dCurrentTime is null then
	        dCurrentTime := sysdate;
	    end if;    
	    return dCurrentTime;
	end;

	function getProcessState(
		processId in integer
	) return integer
	IS
	   state INTEGER;
	BEGIN
	   SELECT RDX_WF_PROCESS.STATE
	     INTO state
	     FROM RDX_WF_PROCESS 
	    WHERE ID = processId;
	    return state;
	END;
end;
/

