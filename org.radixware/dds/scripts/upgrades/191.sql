drop package RDX_WF_Vars
/

drop package RDX_Sys_Vars
/

drop package RDX_ACS_Vars
/

create or replace package RDX_ACS as

	cRight_unbounded       constant integer := 0;
	cRight_boundedByPart   constant integer := 1;
	cRight_prohibited      constant integer := 2;
	cRight_boundedByGroup  constant integer := 3;
	cRight_boundedByUser   constant integer := 4;

	Type long_str_list IS TABLE OF VARCHAR2(32767);
	--type int_list_type  is table of integer index BY BINARY_INTEGER;
	Type int_list_type IS TABLE OF integer;

	function constSuperAdminRoleId return varchar2;
	PRAGMA RESTRICT_REFERENCES (constSuperAdminRoleId, RNDS);
	PRAGMA RESTRICT_REFERENCES (constSuperAdminRoleId, RNPS);
	PRAGMA RESTRICT_REFERENCES (constSuperAdminRoleId, WNDS);
	PRAGMA RESTRICT_REFERENCES (constSuperAdminRoleId, WNPS);

	function constErrorMessage return varchar2;
	PRAGMA RESTRICT_REFERENCES (constErrorMessage, RNDS);
	PRAGMA RESTRICT_REFERENCES (constErrorMessage, RNPS);
	PRAGMA RESTRICT_REFERENCES (constErrorMessage, WNDS);
	PRAGMA RESTRICT_REFERENCES (constErrorMessage, WNPS);

	procedure clearInheritRights(
		pUserGroup in varchar2
	);

	/*
	Функции используемые для проверки того чтобы пользователь не смог 
	назначить дублирующие права, ...

	*/

	function curUserHasRoleInArea(
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserHasRoleInArea, WNDS);

	function userHasRoleInArea(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasRoleInArea, WNDS);

	function userHasExplicitRoleInArea(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasExplicitRoleInArea, WNDS);

	function curUserHasAnyOfRolesInArea(
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserHasAnyOfRolesInArea, WNDS);

	function userHasAnyOfRolesInArea(
		pUser in varchar2,
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasAnyOfRolesInArea, WNDS);

	function curUserHasRoleForObject(
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserHasRoleForObject, WNDS);

	function userHasRoleForObject(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasRoleForObject, WNDS);

	function curUserHasAnyOfRolesForObject(
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserHasAnyOfRolesForObject, WNDS);

	function userHasRoleForObject(
		pUser in varchar2,
		pRole in varchar2,
		pPointList in varchar2
	) return integer;

	function userHasAnyOfRolesForObject(
		pUser in varchar2,
		pRolesArr in clob,
		pPointList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasAnyOfRolesForObject, WNDS);

	function curUserAllRolesInAllAreas return varchar2;
	PRAGMA RESTRICT_REFERENCES (curUserAllRolesInAllAreas, WNDS);

	function userAllRolesInAllAreas(
		pUser in varchar2
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (userAllRolesInAllAreas, WNDS);

	function getCurUserAllRolesForObject(
		pPointList in TRdxAcsAreaList
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (getCurUserAllRolesForObject, WNDS);

	function getAllRolesForObject(
		pUser in varchar2,
		pPointList in TRdxAcsAreaList
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (getAllRolesForObject, WNDS);

	-- Принадлежит ли текущий пользователь заданной группе
	function curUserIsInGroup(
		pGroup in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserIsInGroup, WNDS);

	-- Принадлежит ли пользователь заданной группе
	function userIsInGroup(
		pUser in varchar2,
		pGroup in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userIsInGroup, WNDS);

	function areaListToStr(
		areaList_ in TRdxAcsAreaList
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (areaListToStr, WNDS);

	function areaToStr(
		area_ in TRdxAcsArea
	) return varchar2;
	PRAGMA RESTRICT_REFERENCES (areaToStr, WNDS);

	function strToArea(
		str_ in varchar2
	) return TRdxAcsArea;
	PRAGMA RESTRICT_REFERENCES (strToArea, WNDS);

	function strToAreaList(
		str in varchar2
	) return TRdxAcsAreaList;
	PRAGMA RESTRICT_REFERENCES (strToAreaList, WNDS);

	function isGroupExist(
		name_ in varchar2
	) return integer;

	procedure acsUtilsBuild;

	function isUserHaveGroupRights(
		group_ in varchar2,
		user_ in varchar2
	) return integer;

	function isCurUserHaveGroupRights(
		group_ in varchar2
	) return integer;

	-- user_ обладает не меньшими правами чем user2_
	function isUserHaveUserRights(
		user2_ in varchar2,
		user_ in varchar2
	) return integer;

	function isCurUserHaveUserRights(
		user_ in varchar2
	) return integer;

	-- User have own rights
	function isUserHaveOwnRights(
		user_ in varchar2
	) return integer;

	function isGroupHaveRights(
		group_ in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isGroupHaveRights, WNDS);

	function curUserGroupAdministered(
		pGroup in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserGroupAdministered, WNDS);

	function curUserNullGroupAdministered return integer;
	PRAGMA RESTRICT_REFERENCES (curUserNullGroupAdministered, WNDS);

	function mayReplaceOrRevokeRole(
		userTable in integer,
		Id_ in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (mayReplaceOrRevokeRole, WNDS);

	function getNext2RoleId(
		userTable in integer
	) return integer;

	function usedDualControlWhenAssignRoles return integer;
	PRAGMA RESTRICT_REFERENCES (usedDualControlWhenAssignRoles, WNDS);

	procedure getRolesAndU2GCount(
		userTable in integer,
		userOrGroupName in varchar2,
		acceptedRCount out integer,
		unacceptedRCount out integer,
		acceptedU2GCount out integer,
		unacceptedU2GCount out integer
	);
	PRAGMA RESTRICT_REFERENCES (getRolesAndU2GCount, WNDS);

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
	);

	function haveNotAcceptedEntities return integer;

	procedure getNotAcceptedEntities(
		user2UserGroupCount out integer,
		firstUser2UserGroup out varchar2,
		userGroup2RoleCount out integer,
		firstUserGroup2Role out varchar2,
		user2RoleCount out integer,
		firstUser2Role out varchar2
	);

	function isNewUserOrGroup2Role(
		userTable in integer,
		Id_ in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isNewUserOrGroup2Role, WNDS);

	procedure fillPartitions(
		modeIn_ in integer,
		modeOut_ out integer,
		partitionIn_ in varchar2,
		partitionsOut_ in out long_str_list,
		partitionGroupId_ in integer
	);
	PRAGMA RESTRICT_REFERENCES (fillPartitions, WNDS);

	function equalPartition(
		part1 in varchar2,
		part2 in varchar2
	) return integer deterministic;
	PRAGMA RESTRICT_REFERENCES (equalPartition, WNDS);

	function readPartitions(
		partitionGroupId in integer
	) return clob;
end;
/

grant execute on RDX_ACS to &USER&_RUN_ROLE
/

create or replace package RDX_Arte_Vars as

	function getUserName return varchar2;

	procedure setUserName(
		pUserName in varchar2
	);

	function getStationName return varchar2;

	procedure setStationName(
		pStationName in varchar2
	);

	function getClientLanguage return varchar2;

	procedure setClientLanguage(
		pClientLanguage in varchar2
	);

	function getClientCountry return varchar2;

	procedure setClientCountry(
		pClientCountry in varchar2
	);

	function getDefVersion return number;

	procedure setDefVersion(
		pDefVersion in number
	);

	function getSessionId return number;

	procedure setSessionId(
		pSessionId in number
	);
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_Arte_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_Environment_Vars as

	function getInstanceId return integer;

	procedure setInstanceId(
		pInstanceId in integer
	);

	function getSessionOwnerType return varchar2;

	procedure setSessionOwnerType(
		pSessionOwnerType in varchar2
	);

	function getSessionOwnerId return integer;

	procedure setSessionOwnerId(
		pSessionOwnerId in integer
	);

	function getTargetExecutorId return integer;

	procedure setTargetExecutorId(
		pTargetExecutorId in integer
	);

	function getRequestInfo return varchar2;

	procedure setRequestInfo(
		pRequestInfo in varchar2
	);
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_Environment_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_JS_CalendarSchedule as

	function constClassGuidAbsCalendar return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsCalendar, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsCalendar, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsCalendar, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsCalendar, WNPS);

	function constClassGuidDayOfWeek return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfWeek, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfWeek, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfWeek, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfWeek, WNPS);

	function constClassGuidDayOfMonth return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfMonth, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfMonth, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfMonth, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfMonth, WNPS);

	function constClassGuidAbsDate return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsDate, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsDate, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsDate, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidAbsDate, WNPS);

	function constClassGuidIncCalendar return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidIncCalendar, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidIncCalendar, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidIncCalendar, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidIncCalendar, WNPS);

	function constClassGuidDaily return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidDaily, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDaily, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDaily, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDaily, WNPS);

	function constClassGuidDayOfQuarter return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfQuarter, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfQuarter, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfQuarter, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfQuarter, WNPS);

	function constClassGuidDayOfYear return varchar2;
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfYear, RNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfYear, RNPS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfYear, WNDS);
	PRAGMA RESTRICT_REFERENCES (constClassGuidDayOfYear, WNPS);

	function constKeyFormat return varchar2;
	PRAGMA RESTRICT_REFERENCES (constKeyFormat, RNDS);
	PRAGMA RESTRICT_REFERENCES (constKeyFormat, RNPS);
	PRAGMA RESTRICT_REFERENCES (constKeyFormat, WNDS);
	PRAGMA RESTRICT_REFERENCES (constKeyFormat, WNPS);

	function isIn(
		pId in integer,
		pDate in date
	) return integer;

	function next(
		pId in integer,
		pDate in date
	) return date;

	function nextNotIn(
		pId in integer,
		pDate in date
	) return date;

	function prev(
		pId in integer,
		pDate in date
	) return date;

	function prevNotIn(
		pId in integer,
		pDate in date
	) return date;
end;
/

grant execute on RDX_JS_CalendarSchedule to &USER&_RUN_ROLE
/

create or replace package RDX_JS_JOB as

	function constTaskExecutorClassName return varchar2;
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorClassName, RNDS);
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorClassName, RNPS);
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorClassName, WNDS);
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorClassName, WNPS);

	function constTaskExecutorMethodName return varchar2;
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorMethodName, RNDS);
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorMethodName, RNPS);
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorMethodName, WNDS);
	PRAGMA RESTRICT_REFERENCES (constTaskExecutorMethodName, WNPS);

	procedure shedulePeriodical(
		pTaskId in integer,
		pExecTime in date
	);

	function calcPriority(
		iBasePriority in integer,
		iCurPriority in integer,
		iPriorityBoostingSpeed in integer,
		tDueTime in timestamp
	) return integer;

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
		pAadcMemberId in integer := null,
		threadPoolClassGuid in varchar2 := null,
		threadPoolPid in varchar2 := null,
		threadKey in integer := null
	) return integer;

	procedure awake(
		iJobId in number
	);

	function addParam(
		iParJobId in number,
		sParName in varchar2,
		iParValType in integer,
		sParVal in varchar2
	) return integer;

	function addBlobParam(
		iParJobId in number,
		sParName in varchar2,
		parVal in blob
	) return integer;

	function addClobParam(
		iParJobId in number,
		sParName in varchar2,
		parVal in clob
	) return integer;

	function setParam(
		iParJobId in number,
		sParName in varchar2,
		sParVal in varchar2
	) return integer;

	function calcThreadPriorityMap return varchar2;

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
		iTaskId in integer := null,
		aadcMemberId in integer := null,
		threadPoolClassGuid in varchar2 := null,
		threadPoolPid in varchar2 := null,
		threadKey in integer := null
	) return integer;

	function adjustToNearestMinute(
		iTimestamp in timestamp
	) return timestamp;

	procedure onStartJobExecution(
		jobId in integer
	);

	procedure scheduleTask(
		taskId in integer,
		initiatorName in varchar2,
		-- null - now
		execTime in date,
		-- not null for nested tasks, taken from root task
		execToken in integer
	);

	function isTaskExecuteCall(
		pClassName in varchar2,
		pMethodName in varchar2
	) return integer;

	function adjustToNearest10Sec(
		iTimestamp in timestamp
	) return timestamp;
end;
/

grant execute on RDX_JS_JOB to &USER&_RUN_ROLE
/

create or replace package RDX_JS_Vars as

	TYPE TCalendarItem IS RECORD (
	  classGuid varchar2(29), -- RDX_JS_CALENDARITEM.CLASSGUID%Type,
	  oper char(1), -- RDX_JS_CALENDARITEM.OPER%Type,
	  offsetDir int, -- RDX_JS_CALENDARITEM.OFFSETDIR%Type,
	  offset int, -- RDX_JS_CALENDARITEM.OFFSET%Type,
	  absDate date, -- RDX_JS_CALENDARITEM.ABSDATE%Type,
	  incCalendarId int, --RDX_JS_CALENDARITEM.INCCALENDARID%Type,
	  dayOfWeek int);

	TYPE TItemsNestedTable IS TABLE OF TCalendarItem;
	TYPE TItemsByCalendar IS TABLE OF TItemsNestedTable INDEX BY binary_integer;
	cachedItems TItemsByCalendar;

	-- cached period of calendar dates
	TYPE TDateById IS TABLE OF DATE INDEX BY binary_integer;
	cachedPeriodsBegin TDateById;
	cachedPeriodsEnd TDateById;

	-- cached calendar dates
	-- whether such date is included in calendate (date cache)
	TYPE TDateMap is TABLE of BOOLEAN INDEX BY VARCHAR2(10);         -- key='YYYY-MM-DD'
	TYPE TDateMapById IS TABLE OF TDateMap INDEX BY binary_integer;
	cachedDates TDateMapById;

	function getLastUpdateTime return date;

	procedure setLastUpdateTime(
		pLastUpdateTime in date
	);
	PRAGMA RESTRICT_REFERENCES (DEFAULT, WNDS);
end;
/

grant execute on RDX_JS_Vars to &USER&_RUN_ROLE
/

create or replace package RDX_WF as

	function constDebugRole return varchar2;

	function constAdminRole return varchar2;

	function constClerkRole return varchar2;

	function curUserIsProcessAdmin(
		pProcessId in integer
	) return integer;

	function curUserIsFormAdmin(
		pFormId in integer
	) return integer;

	function curUserIsFormClerk(
		pFormId in integer
	) return integer;

	function userIsFormClerk(
		pUser in varchar2,
		pFormId in integer
	) return integer;

	function getFormAccessibility(
		iFormId in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getFormAccessibility, WNDS);

	function getProcessAccessibility(
		iProcessId in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getProcessAccessibility, WNDS);

	function getSuitableClerk(
		pFormId in integer
	) return varchar2;

	function getActiveSuitableClerk(
		pFormId in integer
	) return varchar2;

	function getFormState(
		iFormId in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getFormState, WNDS);

	function getFormState(
		closeTime in date,
		dueTime in date,
		overdueTime in date,
		expirationTime in date,
		currentTime in date
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getFormState, WNDS);

	function getOwnProcessCount(
		sUserName in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getOwnProcessCount, WNDS);

	function getStateFormCount(
		sUserName in varchar2,
		iFormState in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getStateFormCount, WNDS);

	function getAreaCount(
		areaList in TRdxAcsAreaList
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getAreaCount, WNDS);

	function getAreaPointCount(
		areaList in TRdxAcsAreaList,
		areaNumber in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getAreaPointCount, WNDS);

	procedure getAreaPoint(
		areaList in TRdxAcsAreaList,
		areaNumber in integer,
		pointNumber in integer,
		familyId out varchar2,
		keyVal out varchar2
	);
	PRAGMA RESTRICT_REFERENCES (getAreaPoint, WNDS);

	procedure setAreaPoint(
		areaList in out TRdxAcsAreaList,
		areaNumber in integer,
		pointNumber in integer,
		familyId in varchar2,
		keyVal in varchar2
	);

	procedure getFormArea(
		formId in integer,
		area out TRdxAcsAreaList
	);
	PRAGMA RESTRICT_REFERENCES (getFormArea, WNDS);

	procedure setFormArea(
		formId in integer,
		area in TRdxAcsAreaList
	);

	procedure clearFormArea(
		formId in integer
	);

	function getFormAreaCount(
		formId in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getFormAreaCount, WNDS);

	function getFormAreaPointCount(
		formId in integer,
		areaNumber in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getFormAreaPointCount, WNDS);

	procedure getFormAreaPoint(
		formId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId out varchar2,
		keyVal out varchar2
	);
	PRAGMA RESTRICT_REFERENCES (getFormAreaPoint, WNDS);

	procedure setFormAreaPoint(
		formId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId in varchar2,
		keyVal in varchar2
	);

	procedure getProcessArea(
		processId in integer,
		area out TRdxAcsAreaList
	);
	PRAGMA RESTRICT_REFERENCES (getProcessArea, WNDS);

	procedure setProcessArea(
		processId in integer,
		area in TRdxAcsAreaList
	);

	procedure clearProcessArea(
		processId in integer
	);

	function getProcessAreaCount(
		processId in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getProcessAreaCount, WNDS);

	function getProcessAreaPointCount(
		processId in integer,
		areaNumber in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getProcessAreaPointCount, WNDS);

	procedure getProcessAreaPoint(
		processId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId out varchar2,
		keyVal out varchar2
	);
	PRAGMA RESTRICT_REFERENCES (getProcessAreaPoint, WNDS);

	procedure setProcessAreaPoint(
		processId in integer,
		areaNumber in integer,
		pointNumber in integer,
		familyId in varchar2,
		keyVal in varchar2
	);

	function getCurrentTime(
		processId in integer
	) return date;
	PRAGMA RESTRICT_REFERENCES (getCurrentTime, WNDS);

	function getProcessState(
		processId in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getProcessState, WNDS);
end;
/

grant execute on RDX_WF to &USER&_RUN_ROLE
/

create or replace package body RDX_ACS as

	Type TIdRecord IS RECORD (IdValue varchar(50));
	Type TIdRecordList is table of TIdRecord index by binary_integer;

	type str_list_type  is table of varchar2(50) index BY BINARY_INTEGER;

	function constSuperAdminRoleId return varchar2
	is
	begin
	    return 'rolSUPER_ADMIN_______________';
	end;

	function constErrorMessage return varchar2
	is
	begin
	    return 'Incorrect input string - ';
	end;

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
	  RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage);
	end if; 
	pos:=pos+1;
	res := TRdxAcsArea(TRdxAcsCoordinates());
	<<lbl1>>while(true)
	    loop
	      if SubStr(str , pos, 1)=')' then
	         exit lbl1;
	      end if;
	      if SubStr(str , pos, 1)<>'(' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)='0' then
	         prohibited := 0;
	      else 
	        if SubStr(str , pos, 1)='1' then
	           prohibited := 1;
	        else
	           RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	        end if;
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;
	      pos:=pos+1;
	      accessPartitionKey := SubStr(str, pos,29); 
	      pos:=pos+29;
	      if SubStr(str, pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
	      end if;
	      pos := pos+1;
	      newPos:=InStr(str, ')', pos);
	      if newPos = 0 then
	         RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str);
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
	             where isNew=0 and isOwn=0 and userName = pUser and  pRole = roleId)
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
	    cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0 and isOwn=0 and userName = pUser and  pRole || cSuperAdminRoleId LIKE '%' || roleId || '%')
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
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
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
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
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
	    cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	begin 
	    flag := pPointList is null or pPointList.COUNT()=0;  
	    for ind in (Select * from RDX_AC_USER2ROLE
	             where isNew=0  and isOwn=0 and userName = pUser and  pRole || cSuperAdminRoleId LIKE '%' || roleId || '%')
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
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
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
	        return RDX_ACS.curUserHasRoleInArea(RDX_ACS.constSuperAdminRoleId, pPointList);
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

	procedure writeToClob(
		clob_ in out clob,
		val in varchar2
	)
	is
	 x VARCHAR2(32767);
	begin
	  x := val  || CHR(13);
	  DBMS_LOB.writeappend(clob_, length(x), x);    
	end;

	procedure fillCompileRightsBody(
		body_ in out clob,
		prefix_ in varchar2,
		str_list0 in str_list_type,
		exists_Package in int_list_type,
		index_main in varchar2,
		srcTableName in varchar2,
		userName_ in varchar2
	)
	is
	  x varchar2(32767);
	  n     integer;
	begin
	    n := str_list0.count();

	    For i in 1 .. n
	    loop
	        writeToClob(body_, prefix_ || ' if (' || index_main || '.MA$$' || str_list0(i) || ' = RDX_ACS.cRight_boundedByUser) then');
	        if (exists_Package(i) = 1) then    
	            writeToClob(body_, prefix_ || '    partitionsList' || to_char(i) || '.delete();');
	            writeToClob(body_, prefix_ || '    partitionsList' || to_char(i) || '.extend(1);');
	            writeToClob(body_, prefix_ || '    partitionsList' || to_char(i) || '(1):=ACS$' || str_list0(i) || '.getUserAssignment(RDX_Arte.getUserName());');
	            writeToClob(body_, prefix_ || '    mode' || to_char(i) || ':=RDX_ACS.cRight_boundedByPart;');
	        else    
	            writeToClob(body_, prefix_ || '    raise_application_error(-20000, ''' || 
	                               'System rights package ACS$' || str_list0(i) || 
	                               ' not found, but found RDX_ACS.cRight_boundedByUser bounding mode (Table=' || 
	                               srcTableName || ', Id = '' || ' || index_main || '.Id || '').'');');
	        end if; 

	        writeToClob(body_, prefix_ || ' else');
	        writeToClob(body_, prefix_ || '    RDX_ACS.fillPartitions(' || index_main || '.MA$$' || str_list0(i) || ', ' ||
	                                                     'mode' || to_char(i)  || ', ' || 
	                                                     '' || index_main || '.PA$$' || str_list0(i) || ', ' ||
	                                                     'partitionsList' || to_char(i) || ', ' || 
	                                                     '' || index_main || '.PG$$' || str_list0(i) || 
	                                                     ');');
	        writeToClob(body_, prefix_ || ' end if;');
	    end loop;
	    
	    For i in 1 .. n
	    loop
	        x := LPAD(' ', i);
	        writeToClob(body_, prefix_ || x || 'for i' || to_char(i) || 
	                 ' in partitionsList' || to_char(i) || '.first() .. partitionsList' || to_char(i) || '.last()');  
	        writeToClob(body_, prefix_ || x || 'loop'); 
	    end loop;     
	    
	        x := LPAD(' ', n) || '   INSERT INTO RDX_AC_USER2ROLE(userName, isOwn, RoleId, ID';
	        For i in 1 .. n
	        loop 
	            x := x || ', PA$$' || str_list0(i) || ', MA$$' || str_list0(i);
	        end loop; 
	        x:= x || ')';
	        writeToClob(body_, prefix_ || x);

	        x := LPAD(' ', n) || '   VALUES (' || userName_ || ', 0, ' || index_main || '.RoleId, SQN_RDX_AC_USER2ROLEID.NEXTVAL';
	        For i in 1 .. n
	        loop 
	            x := x || ', partitionsList' || to_char(i) || '(i' || to_char(i) || '), mode' || to_char(i);
	        end loop;     
	        x:= x || ');';
	        writeToClob(body_, prefix_ || x);
	    
	    For i in 1 .. n
	    loop
	        x := LPAD(' ', n - i + 1);
	        writeToClob(body_, prefix_ || x || 'end loop;'); 
	    end loop;       
	    
	end;

	function existsPackage(
		postfix_ in varchar2
	) return integer
	is
	 rez integer;
	begin
	 SELECT count(*) into rez FROM USER_OBJECTS WHERE OBJECT_TYPE = 'PACKAGE' and OBJECT_NAME = 'ACS$' || upper(postfix_) ;
	 return rez;  
	end;

	procedure fillPartitions(
		modeIn_ in integer,
		modeOut_ out integer,
		partitionIn_ in varchar2,
		partitionsOut_ in out long_str_list,
		partitionGroupId_ in integer
	)
	is
	    flag boolean;
	    partitionVals clob;
	    arr RDX_Array.ARR_STR;
	    partitionVal VARCHAR2(32767);
	begin
	    partitionsOut_.delete();
	    if (modeIn_ = RDX_ACS.cRight_unbounded) or (modeIn_ = RDX_ACS.cRight_prohibited) then
	         modeOut_:=modeIn_;
	         partitionsOut_.Extend(1);
	         partitionsOut_(1):=null;

	    elsif (modeIn_ = RDX_ACS.cRight_boundedByPart) then
	         modeOut_:=modeIn_;
	         partitionsOut_.Extend(1);
	         partitionsOut_(1):=partitionIn_;

	    elsif (modeIn_ = RDX_ACS.cRight_boundedByGroup) then
	         flag := partitionGroupId_ is not null;
	         if flag then
	            Select PARTITIONS into partitionVals from RDX_AC_PARTITIONGROUP where partitionGroupId_ = id;
	            flag := (partitionVals is not null) and (dbms_lob.getlength(partitionVals)<>0) and (partitionVals<>'[0]');
	         end if;
	         
	        if flag then        
	            modeOut_:=RDX_ACS.cRight_boundedByPart;
	            arr :=  RDX_Array.fromStr(partitionVals);
	            partitionsOut_.Extend(arr.count());
	            for i in arr.first() .. arr.last() 
	            loop
	                partitionVal:=arr(i);
	                if (partitionVal is not null) and length(partitionVal)>31 then
	                  partitionVal:=substr(partitionVal, 31);
	                else
	                  partitionVal:=null;
	                end if;
	                partitionsOut_(i) := partitionVal;
	            end loop;
	        else
	            modeOut_:=RDX_ACS.cRight_prohibited;
	            partitionsOut_.Extend(1);
	            partitionsOut_(1):=null;
	        end if;
	    else
	        raise_application_error(-20000, 'Detected invalid partition mode: ' || to_char(modeIn_)); 
	    end if;
	end;

	procedure fillCompileDefineValues(
		body_ in out clob,
		n in integer
	) deterministic
	is
	begin
	For i in 1 .. n
	    loop
	        writeToClob(body_, '  partitionsList' || to_char(i) || ' RDX_ACS.long_str_list;');
	        writeToClob(body_, '  mode' || to_char(i) || ' integer;');
	    end loop;
	end;

	procedure fillCompileInitValues(
		body_ in out clob,
		n in integer
	)
	is
	begin
	    For i in 1 .. n
	    loop        
	        writeToClob(body_, '  partitionsList' || to_char(i) || ' := RDX_ACS.long_str_list();');
	    end loop;
	end;

	procedure fillLongCycle(
		body_ in out clob,
		str_list0 in str_list_type
	)
	is
	    n  integer;
	    x varchar2(32767);
	    x2 varchar2(32767);
	begin
	n := str_list0.COUNT();

	writeToClob(body_, 'n:=calculatedList1.count;');

	writeToClob(body_, 'if (n=0) then');
	    writeToClob(body_, '  return 0;');
	    writeToClob(body_, 'end if;');
	    
	    For i in 1 .. n loop
	        x := LPAD(' ', i);
	        writeToClob(body_, x || 'for i' || to_char(i) || ' in partitionsList' || to_char(i) || '.first() .. partitionsList' || to_char(i) || '.last()');         
	        writeToClob(body_, x || 'loop');
	        writeToClob(body_, x || 'partition' || to_char(i) || ' := partitionsList' || to_char(i) || '(i' || to_char(i) || ');');
	    end loop;
	    
	    x := LPAD(' ', n);
	        
	    writeToClob(body_, x || 'ok_ := false;');
	    writeToClob(body_, x || 'for i in 1..n loop');    
	      For i in 1 .. n loop
	      writeToClob(body_, x || '  calcPartition' || to_char(i) || ' := calculatedList' || to_char(i) || '(i);');
	      writeToClob(body_, x || '  calcMode' || to_char(i) || ' := calculatedMode' || to_char(i) || '(i);');
	      end loop;
	      writeToClob(body_, x || '  if(');
	      
	      For i in 1 .. n loop
	      x2 := x || '  ((mode' || to_char(i) || ' = RDX_ACS.cRight_prohibited) or ' ||
	                     '(calcMode' || to_char(i) || ' = RDX_ACS.cRight_unbounded) or ' ||
	                            '(mode' || to_char(i) || ' = RDX_ACS.cRight_boundedByPart and ' ||
	                            'calcMode' || to_char(i) || ' = RDX_ACS.cRight_boundedByPart and ' ||
	                            'RDX_ACS.equalPartition(partition' || to_char(i) || ', calcPartition' || to_char(i) || ') = 1))';
	      if (i!=n) then
	        x2 := x2 || ' and';
	      else  
	        x2 := x2 || ') then';
	      end if;                            
	       writeToClob(body_, x || x2);      
	      end loop;
	    writeToClob(body_, x || ' ok_ := true;');
	    writeToClob(body_, x || ' exit;');
	     
	    writeToClob(body_, x || 'end if;');
	      
	    writeToClob(body_, x || 'end loop;');
	    
	    writeToClob(body_, x || 'if (not ok_) then');
	    writeToClob(body_, x || '  return 0;');
	    writeToClob(body_, x || 'end if;');
	    
	    
	    
	    
	    For i in 1 .. n loop
	        x := LPAD(' ', n - i + 1);
	        writeToClob(body_, x || 'end loop;');
	    end loop;
	end;

	procedure fillRights2Table(
		table_ in varchar2,
		func_ in varchar2,
		body_ in out clob,
		str_list0 in str_list_type,
		exists_Package in int_list_type
	)
	is
	    n  integer;
	    x varchar2(32767);    
	begin

	    n := str_list0.COUNT();

	    writeToClob(body_, 'function ' || func_ || '(user_ in varchar2, id_ in integer) return integer');
	    writeToClob(body_, 'is');
	    For i in 1 .. n loop
	       writeToClob(body_, ' partition' || to_char(i) || ' VARCHAR2(32767);');
	       writeToClob(body_, ' mode' || to_char(i) || ' integer;');
	       writeToClob(body_, ' calcMode' || to_char(i) || ' integer;');
	       writeToClob(body_, ' groupId' || to_char(i) || ' integer;');
	       writeToClob(body_, ' partitionsList' || to_char(i) || ' RDX_ACS.long_str_list;');
	       writeToClob(body_, ' calculatedList' || to_char(i) || ' RDX_ACS.long_str_list;');
	       writeToClob(body_, ' calculatedMode' || to_char(i) || ' RDX_ACS.int_list_type;');
	       writeToClob(body_, ' calcPartition' || to_char(i) || ' VARCHAR2(32767);');
	              
	    end loop;
	    writeToClob(body_, ' roleId_ varchar(50);');
	    writeToClob(body_, ' i integer;');
	    writeToClob(body_, ' n integer;');
	    writeToClob(body_, ' ok_ boolean;');
	    writeToClob(body_, 'begin');
	    
	    x := '  select roleId';
	    For i in 1 .. n loop
	      x := x || ', MA$$' || str_list0(i) || ', PA$$' || str_list0(i) || ', PG$$' || str_list0(i);      
	    end loop;
	    x := x || ' into roleId_';
	    For i in 1 .. n loop
	      x := x || ', mode' || to_char(i) || ', partition' || to_char(i) || ', groupId' || to_char(i);
	    end loop;    
	    writeToClob(body_,  x || ' from ' || table_ || ' where id = id_;');
	        
	    For i in 1 .. n
	    loop        
	        writeToClob(body_, ' partitionsList' || to_char(i) || ' := RDX_ACS.long_str_list();');
	        writeToClob(body_, ' calculatedList' || to_char(i) || ' := RDX_ACS.long_str_list();');
	        writeToClob(body_, ' calculatedMode' || to_char(i) || ' := RDX_ACS.int_list_type();');        

	    end loop;

	    For i in 1 .. n
	    loop    
	        writeToClob(body_, '  if (mode' || to_char(i) ||  ' = RDX_ACS.cRight_boundedByUser) then');
	        if (exists_Package(i) = 1) then    
	            writeToClob(body_, '    partitionsList' || to_char(i) || '.delete();');
	            writeToClob(body_, '    partitionsList' || to_char(i) || '.extend(1);');
	            writeToClob(body_, '    partitionsList' || to_char(i) || '(1):=ACS$' || str_list0(i) || '.getUserAssignment(RDX_Arte.getUserName());');
	            writeToClob(body_, '    mode' || to_char(i) || ':=RDX_ACS.cRight_boundedByPart;');
	        else    
	            writeToClob(body_, '    raise_application_error(-20000, ''' || 
	                               'System rights package ACS$' || str_list0(i) || 
	                               ' not found, but found RDX_ACS.cRight_boundedByUser bounding mode (Table=' || 
	                               table_ || ', Id = '' || id_ || '').'');');
	        end if; 

	        writeToClob(body_, '  else');
	        writeToClob(body_, '    RDX_ACS.fillPartitions(mode' || to_char(i)  || ', calcMode' || to_char(i) || ', ' ||
	                                                     'partition' || to_char(i) || ', ' ||
	                                                     'partitionsList' || to_char(i) || ', ' || 
	                                                     'groupId' || to_char(i)|| 
	                                                     ');');
	        writeToClob(body_, '  end if;');
	    end loop;
	    
	      writeToClob(body_, '  i:=1;');
	      writeToClob(body_, '  for ind in (Select * from RDX_AC_USER2ROLE where isNew=0 and isOwn = 0 and userName = user_ and ');
	      writeToClob(body_, '                       (roleId_ = roleId or roleId = RDX_ACS_Vars.sysSuperAdminRoleId) )');
	      writeToClob(body_, '  loop');
	      
	    For i in 1 .. n      
	    loop
	      writeToClob(body_, '    calculatedList' || to_char(i) || '.Extend();');
	      writeToClob(body_, '    calculatedMode' || to_char(i) || '.Extend();');
	         
	      writeToClob(body_, '    calculatedList' || to_char(i) || '(i):=ind.PA$$' || str_list0(i) || ';');
	      writeToClob(body_, '    calculatedMode' || to_char(i) || '(i):=ind.MA$$' || str_list0(i) || ';');
	    end loop;      
	      writeToClob(body_, '    i:=i+1;');
	      writeToClob(body_, '  end loop;');
	    
	    RDX_ACS.fillLongCycle(body_, str_list0);
	    
	    writeToClob(body_, '  return 1;');
	    writeToClob(body_, 'end;');

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

	procedure acsUtilsBuild
	is 

	    str_list0 str_list_type;
	    exists_Package int_list_type;

	    cur_index  integer;
	    n  integer;
	    i  integer;

	    cursor_name INTEGER;
	    ret	    INTEGER;

	    x varchar2(32767);
	    x2 varchar2(32767);

	    body_ clob;
	    
	    cSuperAdminRoleId constant varchar2(30) := '''' || RDX_ACS.constSuperAdminRoleId || '''';
	 
	begin

	    cur_index := 1;
	    dbms_lob.createTemporary(body_, false, dbms_lob.SESSION);
	    
	    exists_Package := int_list_type();
	     
	    for ind in (select DISTINCT table_name, column_name from user_tab_columns where  
	                  table_name = 'RDX_AC_USER2ROLE' 
	                and
	                  length(column_name) = 30 
	                and 
	                  column_name like 'PA$$%'
	                  order by column_name 
	               )                  
	    loop
	        x:=ind.column_name;
	        x:= SUBSTR(x, 5);        
	        str_list0(cur_index):=x;        
	        exists_Package.EXTEND();
	        exists_Package(cur_index):= RDX_ACS.existsPackage(x);
	        cur_index := cur_index+1;
	    end loop;
	 


	    -- body_ 
	    n := str_list0.COUNT();
	    
	    writeToClob(body_, 'create or replace package RDX_ACS_UTILS as');
	    writeToClob(body_, 'function buildAssignedAccessAreaU2R(row_ in RDX_AC_User2Role%ROWTYPE) return TRdxAcsArea;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (buildAssignedAccessAreaU2R, WNDS);');
	    writeToClob(body_, 'function buildAssignedAccessAreaG2R(row_ in RDX_AC_UserGroup2Role%ROWTYPE) return TRdxAcsArea;');    
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (buildAssignedAccessAreaG2R, WNDS);');
	    writeToClob(body_, 'procedure compileRights;');
	    writeToClob(body_, 'procedure compileRightsForGroup(pUserGroup in varchar2);');
	    writeToClob(body_, 'procedure compileRightsForUser(pUser in varchar2);');

	    writeToClob(body_, 'procedure moveRightsFromUserToGroup(user_ in varchar2, group_ in varchar2);');
	    
	    x := '';
	    For i in 1 .. n loop
	        x := x || ', mode' || to_char(i) || ' integer, partitions' || to_char(i) || ' clob';
	    end loop;
	    
	    writeToClob(body_, 'function existsRightsOnArea(user_ in varchar2, role_ in varchar2 ' || x || ') return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (existsRightsOnArea, WNDS);');
	    
	    writeToClob(body_, 'function existsRightsOnUser2Role(user_ in varchar2, id_ in integer) return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (existsRightsOnUser2Role, WNDS);');
	    
	    writeToClob(body_, 'function existsRightsOnGroup2Role(user_ in varchar2, id_ in integer) return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (existsRightsOnGroup2Role, WNDS);');
	    
	    writeToClob(body_, 'function isSuperAdmin(user_ in varchar2) return integer;');
	    writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (isSuperAdmin, WNDS);');
	        
	    For i in 1 .. n loop
	        writeToClob(body_, 'function gup_' || str_list0(i) || '(user_ in varchar2) return clob;');
	        --writeToClob(body_, 'PRAGMA RESTRICT_REFERENCES (gup_' || str_list0(i) || ', WNDS);');
	    end loop;
	    
	    
	    writeToClob(body_, 'end;');
	  
	  
	  
	    cursor_name := DBMS_SQL.OPEN_CURSOR;
	    DBMS_SQL.PARSE ( cursor_name, body_, DBMS_SQL.NATIVE );
	    ret := DBMS_SQL.EXECUTE ( cursor_name );
	    DBMS_SQL.CLOSE_CURSOR ( cursor_name );  


	    
	    dbms_lob.createTemporary(body_, false, dbms_lob.SESSION);
	  

	    writeToClob(body_, 'CREATE  OR REPLACE package body RDX_ACS_UTILS as');
	    
	    
	    For i in 1 .. n loop
	    writeToClob(body_, '/*get unbounded partitions*/');
	        writeToClob(body_, 'function gup_' || str_list0(i) || '(user_ in varchar2) return clob');
	        writeToClob(body_, 'is');
	        writeToClob(body_, ' p RDX_Array.ARR_STR;');
	        writeToClob(body_, ' i integer;');
	        writeToClob(body_, 'begin');
	        
	        x:='';
	        For j in 1 .. n loop
	            if (i<>j) then
	                x:= x || ' and MA$$' || str_list0(j) || '= RDX_ACS.cRight_unbounded';
	            end if;    
	        end loop;
	        
	        writeToClob(body_, 'p := RDX_Array.ARR_STR();');
	        writeToClob(body_, 'i := 1;');
	        
	        writeToClob(body_, 'for ind in (select PA$$' || str_list0(i) ||
	                     ' from RDX_AC_USER2ROLE where roleId = ' || cSuperAdminRoleId || ' and isNew = 0 and isOwn = 0 and userName = user_' 
	                      || ' and MA$$' || str_list0(i) || '= RDX_ACS.cRight_boundedByPart' || x || ')'
	                     );
	        writeToClob(body_, ' loop');
	        writeToClob(body_, '  p.extend();');
	        writeToClob(body_, '  p(i) := ind.PA$$' || str_list0(i) || ';');
	        writeToClob(body_, '  i := i+1;');
	        writeToClob(body_, ' end loop;');
	        
	        
	        writeToClob(body_, ' return RDX_Array.fromArrStr(p);');
	        writeToClob(body_, 'end;');    
	    end loop;
	    
	    
	 
	    writeToClob(body_, 'function isSuperAdmin(user_ in varchar2) return integer');
	    writeToClob(body_, 'is');
	    writeToClob(body_, ' count_ integer;');
	    writeToClob(body_, 'begin');
	    
	    x := '';
	    For i in 1 .. n loop
	        x := x || ' and MA$$' || str_list0(i) || '= RDX_ACS.cRight_unbounded';
	    end loop;
	    writeToClob(body_, ' select count(*) into count_ from RDX_AC_USER2ROLE where roleId = ' || cSuperAdminRoleId || ' and isNew = 0 and isOwn = 0 and userName = user_' || x || ';');
	    writeToClob(body_, ' if count_ > 0 then');
	    writeToClob(body_, '   return 1;');
	    writeToClob(body_, ' end if;');
	    
	    writeToClob(body_, ' return 0;');
	    writeToClob(body_, 'end;');

	   
	    
	    RDX_ACS.fillRights2Table('RDX_AC_USER2ROLE', 'existsRightsOnUser2Role', body_, str_list0, exists_Package);
	    RDX_ACS.fillRights2Table('RDX_AC_USERGROUP2ROLE', 'existsRightsOnGroup2Role', body_, str_list0, exists_Package);
	    



	    x := '';
	    For i in 1 .. n loop
	        x := x || ', mode' || to_char(i) || ' integer, partitions' || to_char(i) || ' clob';
	    end loop;

	    

	    --existsRightsOnArea
	    writeToClob(body_, 'function existsRightsOnArea(user_ in varchar2, role_ in varchar2 ' || x || ') return integer');
	    writeToClob(body_, 'is');
	    For i in 1 .. n loop
	       writeToClob(body_, ' partition' || to_char(i) || ' VARCHAR2(32767);');
	       writeToClob(body_, ' calcPartition' || to_char(i) || ' VARCHAR2(32767);');
	       writeToClob(body_, ' calcMode' || to_char(i) || ' integer;');
	       writeToClob(body_, ' partitionsList' || to_char(i) || ' RDX_Array.ARR_STR;');
	       writeToClob(body_, ' calculatedList' || to_char(i) || ' RDX_Array.ARR_STR;');
	       writeToClob(body_, ' calculatedMode' || to_char(i) || ' RDX_ACS.int_list_type;');
	    end loop;
	    writeToClob(body_, ' i integer;');
	    writeToClob(body_, ' n integer;');
	    writeToClob(body_, ' ok_ boolean;');
	    
	    writeToClob(body_, 'begin');
	        
	    For i in 1 .. n loop
	        writeToClob(body_, ' if mode' || to_char(i) ||' = RDX_ACS.cRight_prohibited then');
	        writeToClob(body_, '  partitionsList' || to_char(i) || ':= RDX_Array.fromStr(partitions' || to_char(i) || ');');
	        writeToClob(body_, ' else');
	        writeToClob(body_, '  partitionsList' || to_char(i) || ':= RDX_Array.ARR_STR();');
	        writeToClob(body_, '  partitionsList' || to_char(i) || '.Extend(1);');
	        writeToClob(body_, '  partitionsList' || to_char(i) || '(1):=null;');       
	        writeToClob(body_, ' end if;');
	        writeToClob(body_, ' calculatedList' || to_char(i) || ':= RDX_Array.ARR_STR();');
	        writeToClob(body_, ' calculatedMode' || to_char(i) || ':= RDX_ACS.int_list_type();');
	    end loop;
	    
	    writeToClob(body_, 'i:=1;');
	    writeToClob(body_, 'for ind in (Select * from RDX_AC_USER2ROLE where isNew=0 and isOwn = 0 and userName = user_ and ');
	    writeToClob(body_, '           (role_ = roleId or roleId = RDX_ACS_Vars.sysSuperAdminRoleId) )');
	    writeToClob(body_, 'loop');
	    For i in 1 .. n loop
	        writeToClob(body_, '   calculatedList' || to_char(i) || '.Extend();');
	        writeToClob(body_, '   calculatedMode' || to_char(i) || '.Extend();');
	        
	        writeToClob(body_, '   calculatedList' || to_char(i) || '(i):=ind.PA$$' || str_list0(i) || ';');
	        writeToClob(body_, '   calculatedMode' || to_char(i) || '(i):=ind.MA$$' || str_list0(i) || ';');
	    end loop;
	    writeToClob(body_, '   i:=i+1;');
	    writeToClob(body_, 'end loop;');
	    
	     
	    RDX_ACS.fillLongCycle(body_, str_list0);
	    
	    
	    writeToClob(body_, ' return 1;');
	    writeToClob(body_, 'end;');
	    
	    --writeToClob(body_, 'function existsRightsOnUser2Role(user_ in varchar2, id_ in integer) return integer;');
	    
	    --compileRights
	    writeToClob(body_, 'procedure compileRights');    
	    writeToClob(body_, 'is');

	    RDX_ACS.fillCompileDefineValues(body_, n);
	    
	    writeToClob(body_, 'begin');
	    
	    RDX_ACS.fillCompileInitValues(body_, n);
	    
	    writeToClob(body_, '/*clear all inherit rights*/');--comment    
	    writeToClob(body_, 'delete from RDX_AC_USER2ROLE u2r where u2r.isOwn=0;');    
	    
	    writeToClob(body_, '');
	    writeToClob(body_, '/*collect rights from RDX_AC_USER2ROLE*/');--comment
	    writeToClob(body_, 'for ind1 in (Select * from RDX_AC_USER2ROLE where isOwn=1 and isNew<>1)');
	    writeToClob(body_, 'loop');
	    RDX_ACS.fillCompileRightsBody(body_, '', str_list0, exists_Package, 'ind1', 'RDX_AC_USER2ROLE', 'ind1.userName');
	    writeToClob(body_, 'end loop;');
	        
	    writeToClob(body_, '');
	    writeToClob(body_, '/*collect rights from RDX_AC_USERGROUP2ROLE*/');--comment
	    writeToClob(body_, 'for ind1 in (Select * from RDX_AC_USERGROUP2ROLE where isNew<>1)');
	    writeToClob(body_, 'loop');
	    writeToClob(body_, '  for ind2 in (Select * from RDX_AC_USER2USERGROUP where GroupName=ind1.GroupName and state<>1)');
	    writeToClob(body_, '  loop');
	    RDX_ACS.fillCompileRightsBody(body_, '  ', str_list0, exists_Package, 'ind1', 'RDX_AC_USERGROUP2ROLE', 'ind2.userName');
	    writeToClob(body_, '  end loop;');
	    writeToClob(body_, 'end loop;');
	  
	    writeToClob(body_, 'end;');


	    --compileRightsForGroup     
	    
	    writeToClob(body_, 'procedure compileRightsForGroup(pUserGroup in varchar2)');
	    writeToClob(body_, 'is');
	    RDX_ACS.fillCompileDefineValues(body_, n);
	    
	    writeToClob(body_, 'begin');
	    
	    RDX_ACS.fillCompileInitValues(body_, n);
	    
	    writeToClob(body_, 'RDX_ACS.ClearInheritRights(pUserGroup);');
	    
	    writeToClob(body_, 'for ind1 in (select * from RDX_AC_USER2USERGROUP where GroupName=pUserGroup and state<>1)');
	    writeToClob(body_, 'loop');
	    writeToClob(body_, '  for ind2 in (select * from RDX_AC_USER2USERGROUP where UserName=ind1.UserName and state<>1)');
	    writeToClob(body_, '  loop');
	    
	    --from this user
	    writeToClob(body_, '    for ind3 in (Select * from RDX_AC_USER2ROLE where isOwn=1 and isNew<>1)');
	    writeToClob(body_, '    loop');
	    RDX_ACS.fillCompileRightsBody(body_, '    ', str_list0, exists_Package, 'ind3', 'RDX_AC_USER2ROLE', 'ind1.userName');
	    writeToClob(body_, '    end loop;');
	    writeToClob(body_, '');
	                
	    --from groups
	    writeToClob(body_, '    for ind3 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind2.GroupName and isNew<>1)');
	    writeToClob(body_, '    loop');
	    RDX_ACS.fillCompileRightsBody(body_, '    ', str_list0, exists_Package, 'ind3', 'RDX_AC_USERGROUP2ROLE', 'ind1.UserName');
	    writeToClob(body_, '    end loop;');
	    writeToClob(body_, '  end loop;');
	    writeToClob(body_, 'end loop;');
	    
	    writeToClob(body_, 'end;');


	    --compileRightsForUser 
	    writeToClob(body_, 'procedure compileRightsForUser(pUser in varchar2)');
	    writeToClob(body_, 'is');
	    RDX_ACS.fillCompileDefineValues(body_, n);
	    
	    writeToClob(body_, 'begin');
	    RDX_ACS.fillCompileInitValues(body_, n);

	    writeToClob(body_, 'delete from RDX_AC_USER2ROLE u2r where u2r.isOwn = 0 and username = pUser and isNew<>1;');
	    
	    writeToClob(body_, 'for ind1 in (select * from RDX_AC_USER2ROLE where username = pUser and isOwn=1 and isNew<>1)');
	    writeToClob(body_, 'loop');
	    RDX_ACS.fillCompileRightsBody(body_, '', str_list0, exists_Package, 'ind1', 'RDX_AC_USER2ROLE', 'pUser');
	    writeToClob(body_, 'end loop;');
	                                                                        
	    writeToClob(body_, 'for ind1 in (select * from RDX_AC_USER2USERGROUP where UserName=pUser and state<>1)');
	    writeToClob(body_, 'loop');
	    writeToClob(body_, '  for ind2 in (Select * from RDX_AC_USERGROUP2ROLE where GroupName = ind1.GroupName and isNew<>1)');
	    writeToClob(body_, '  loop');   
	    RDX_ACS.fillCompileRightsBody(body_, '  ', str_list0, exists_Package, 'ind2', 'RDX_AC_USERGROUP2ROLE', 'pUser');
	    writeToClob(body_, '  end loop;');    
	    writeToClob(body_, 'end loop;');
	    writeToClob(body_, 'end;');
	    
	    
	    
	    
	    -- procedure compileRightsForGrpBeforeDel
	    
	    /*
	    writeToClob(body_, 'procedure compileRightsForGrpBeforeDel(pUserGroup in varchar2)');
	    writeToClob(body_, 'is');
	    writeToClob(body_, 'begin');
	    writeToClob(body_, 'null;');
	    writeToClob(body_, 'raise_application_error(-20000, ''Not realized yet'');');
	    writeToClob(body_, 'end;');
	    */
	    
	    
	    /*
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
	    */


	    for i in 1 .. 2 
	    loop
	       if i = 1  then
	          x:='function buildAssignedAccessAreaU2R(row_ in RDX_AC_USER2ROLE%ROWTYPE)return TRdxAcsArea';
	       else
	          x:='function buildAssignedAccessAreaG2R(row_ in RDX_AC_USERGROUP2ROLE%ROWTYPE)return TRdxAcsArea';
	       end if;
	       writeToClob(body_, x);

	       writeToClob(body_, 'is');
	       writeToClob(body_, 'res TRdxAcsArea;');
	       writeToClob(body_, 'Begin');
	       writeToClob(body_, 'res := TRdxAcsArea(TRdxAcsCoordinates());');
	       
	       for cur_index in 1 .. n 
	       loop 
	            writeToClob(body_, '      if row_.MA$$' || str_list0(cur_index) || '<>RDX_ACS.cRight_unbounded then');
	            writeToClob(body_, '        res.boundaries.EXTEND();');
	            writeToClob(body_, '        if row_.MA$$' || str_list0(cur_index) || ' = RDX_ACS.cRight_prohibited  then');
	            writeToClob(body_, '            res.boundaries(res.boundaries.Count()):=TRdxAcsCoordinate(1, ''apf'  || 
	                               str_list0(cur_index) ||  ''', null);');
	            writeToClob(body_, '        elsif row_.MA$$' || str_list0(cur_index) || ' = RDX_ACS.cRight_boundedByPart then');
	            writeToClob(body_, '            res.boundaries(res.boundaries.Count()):=TRdxAcsCoordinate(0, ''apf'  || 
	                               str_list0(cur_index) ||  ''', row_.PA$$' || str_list0(cur_index) ||');');
	            writeToClob(body_, '        else');
	            writeToClob(body_, '            raise_application_error(-20000, ''Invalid argument'');');
	            writeToClob(body_, '        end if;');
	            writeToClob(body_, '      end if;');
	       end loop;
	       writeToClob(body_, ' return res;');
	       writeToClob(body_, ' end;');
	    end loop;
	    
	    
	    --moveRightsFromUserToGroup
	    writeToClob(body_, 'procedure moveRightsFromUserToGroup(user_ in varchar2, group_ in varchar2)');
	    writeToClob(body_, 'is');
	    writeToClob(body_, 'begin');
	    writeToClob(body_, ' INSERT INTO RDX_AC_USERGROUP (name) VALUES (group_);');
	    writeToClob(body_, ' INSERT INTO RDX_AC_USER2USERGROUP (userName, groupName) VALUES (user_, group_);');
	     
	    writeToClob(body_, '  for ind in (Select * from  RDX_AC_USER2ROLE where userName = User_ and  isOwn = 1 and isNew<>1)');
	    writeToClob(body_, '     loop');
	    
	    x:='';
	    For i in 1 .. n
	    loop 
	        x := x || ', PA$$' || str_list0(i) || ', MA$$' || str_list0(i) || ', PG$$' || str_list0(i);
	    end loop;
	    writeToClob(body_, '      INSERT INTO RDX_AC_USERGROUP2ROLE (ID, GroupName, roleId' || x || ')');
	        
	    x:='';
	    For i in 1 .. n
	    loop 
	        x := x || ', IND.PA$$' || str_list0(i) || ', IND.MA$$' || str_list0(i) || ', IND.PG$$' || str_list0(i);
	    end loop;
	    writeToClob(body_, '      VALUES ( SQN_RDX_AC_USERGROUP2ROLEID.NEXTVAL, group_, IND.roleId' || x || ');');

	    writeToClob(body_, '      DELETE FROM RDX_AC_USER2ROLE WHERE id=ind.id;');
	    
	    writeToClob(body_, '     end loop;');
	    writeToClob(body_, 'end;');
	    
	    ----------------------------
	    
	    writeToClob(body_, 'end;');

	   

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
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where isNew=0  and isOwn=0 and  username = pUser)
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
	    for ind in (select distinct ROLEID from RDX_AC_USER2ROLE where isNew=0  and isOwn=0 and  username = pUser)
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
	        RAISE_APPLICATION_ERROR (-20100, RDX_ACS.constErrorMessage || str );
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

	cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	  
	begin
	  for g2r_row in (select DISTINCT  * from RDX_AC_USERGROUP2ROLE where isNew=0 and groupname = group_)
	    loop                                                     
	    flag := false;
	    groupArea := RDX_ACS_UTILS.buildAssignedAccessAreaG2R(g2r_row);
	    
	    <<L>>for u2r_row in (select DISTINCT  * from RDX_AC_USER2ROLE U2R where isNew=0 and isOwn=0 and username = user_ and
	                            (g2r_row.roleId = U2R.roleId or U2R.roleId = cSuperAdminRoleId))
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

	cSuperAdminRoleId constant varchar2(30) := RDX_ACS.constSuperAdminRoleId;
	  
	begin
	  for u2r_row1 in (select DISTINCT * from RDX_AC_USER2ROLE where isNew=0 and isOwn=0 and username = user2_)
	    loop                                                     
	    flag := false;
	    lowerUserArea := RDX_ACS_UTILS.buildAssignedAccessAreaU2R(u2r_row1);
	    <<L>>for u2r_row in (select DISTINCT * from RDX_AC_USER2ROLE U2R where isNew=0 and isOwn=0 and username = user_ and
	                            (u2r_row1.roleId = U2R.roleId or U2R.roleId = cSuperAdminRoleId))
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
	             where isNew=0 and isOwn=0 and userName = RDX_Arte.getUserName())
	     loop         
	         if ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_unbounded or
	            (ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_boundedByPart and 
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
	             where isNew=0 and isOwn=0 and userName = RDX_Arte.getUserName())
	     loop         
	         if ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_unbounded or
	            (ind.MA$$1ZOQHCO35XORDCV2AANE2UAFXA = RDX_ACS.cRight_boundedByPart and 
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
	                    if (RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.ID)=0) then -- access denied
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
	                    if (RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0) then -- access denied
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
	                         RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0
	                         or
	                         RDX_ACS_UTILS.existsRightsOnUser2Role(RDX_Arte.getUserName(), item.ID)=0
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
	                    if (RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.ID)=0) then -- access denied
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
	                    if (RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0) then -- access denied
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
	                         RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.REPLACEDID)=0
	                         or
	                         RDX_ACS_UTILS.existsRightsOnGroup2Role(RDX_Arte.getUserName(), item.ID)=0
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

	function equalPartition(
		part1 in varchar2,
		part2 in varchar2
	) return integer deterministic
	is
	begin
	 if (part1 is null) and (part2 is null) then 
	  return 1;
	 end if;
	 if (part1 is not null) and (part2 is not null) and (part1 = part2) then 
	  return 1;
	 end if;
	 return 0; 
	end;

	function readPartitions(
		partitionGroupId in integer
	) return clob
	is
	 rez clob;
	begin
	 select PARTITIONS into rez from RDX_AC_PARTITIONGROUP where ID = partitionGroupId;
	 return rez;
	exception 
	 when NO_DATA_FOUND then
	 return null; 
	end;
end;
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
	    RDX_Aadc.setupTriggers;
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
	    RDX_Aadc.setupTriggers;
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
	    SUSPENSION_TAG constant RAW(10) := HEXTORAW('AADC0000');
	begin
	    prevTag := DBMS_STREAMS.GET_TAG();
	    DBMS_STREAMS.SET_TAG(SUSPENSION_TAG);
	    return prevTag;
	end;

	procedure restoreReplication(
		prevState in RAW
	)
	is
	begin
	    DBMS_STREAMS.SET_TAG(prevState);
	end;

	procedure setupTriggers
	is
	begin
	    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
	        trig_owner => sys_context( 'userenv', 'current_schema' ), 
	        trig_name  => 'TAIR_RDX_AADC_OGGERROR',
	        fire_once  => false);
	    DBMS_DDL.SET_TRIGGER_FIRING_PROPERTY(
	        trig_owner => sys_context( 'userenv', 'current_schema' ), 
	        trig_name  => 'TBIR_RDX_AADC_OGGERROR',
	        fire_once  => false);
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
	   RDX_Arte_Vars.setUserName(pUserName);
	   RDX_Arte_Vars.setStationName(pStationName);
	   RDX_Arte_Vars.setClientLanguage(pClientLanguage);
	   RDX_Arte_Vars.setClientCountry(pClientCountry);
	   RDX_Arte_Vars.setSessionId(pSessionId);
	   RDX_Arte_Vars.setDefVersion(pDefVersion);
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
	   RDX_Arte_Vars.setUserName(pUserName);
	   RDX_Arte_Vars.setStationName(pStationName);
	   RDX_Arte_Vars.setClientLanguage(pClientLanguage);
	   RDX_Arte_Vars.setClientCountry(pClientCountry);
	   RDX_Arte_Vars.setSessionId(pSessionId);
	   RDX_Arte_Vars.setDefVersion(pDefVersion);
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo( 'U#'|| pUnitId ||  '/J#' || nvl(to_char(pJobId), '-') || '/T#' || nvl(to_char(pTaskId), '-') );
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	function getUserName return varchar2
	is
	begin
	  return RDX_Arte_Vars.getUserName;
	end;

	function getVersion return integer
	is
	begin
	    if RDX_Arte_Vars.getDefVersion is null then
	       raise_application_error(-20001,'Definition version is not deployed to database. '||chr(10)||'onStartRequestExecution() method of Arte (java class) should have been called  '||chr(10)||'before the database request executing.');
	    end if;
	  return RDX_Arte_Vars.getDefVersion;
	end;

	function getStationName return varchar2
	is
	begin
	  return RDX_Arte_Vars.getStationName;
	end;

	function getClientLanguage return varchar2
	is
	begin
	  return RDX_Arte_Vars.getClientLanguage;
	end;

	function getSessionId return integer
	is
	begin
	  return RDX_Arte_Vars.getSessionId;
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

create or replace package body RDX_Arte_Vars as

	userName varchar2(250);
	stationName varchar2(250);
	clientLanguage varchar2(3);
	clientCountry varchar2(3);
	defVersion number;
	sessionId number;

	function getUserName return varchar2
	is
	begin
	    return userName;
	end;

	procedure setUserName(
		pUserName in varchar2
	)
	is
	begin
	    userName := pUserName;
	end;

	function getStationName return varchar2
	is
	begin
	    return stationName;
	end;

	procedure setStationName(
		pStationName in varchar2
	)
	is
	begin
	    stationName := pStationName;
	end;

	function getClientLanguage return varchar2
	is
	begin
	    return clientLanguage;
	end;

	procedure setClientLanguage(
		pClientLanguage in varchar2
	)
	is
	begin
	    clientLanguage := pClientLanguage;
	end;

	function getClientCountry return varchar2
	is
	begin
	    return clientCountry;
	end;

	procedure setClientCountry(
		pClientCountry in varchar2
	)
	is
	begin
	    clientCountry := pClientCountry;
	end;

	function getDefVersion return number
	is
	begin
	    return defVersion;
	end;

	procedure setDefVersion(
		pDefVersion in number
	)
	is
	begin
	    defVersion := pDefVersion;
	end;

	function getSessionId return number
	is
	begin
	    return sessionId;
	end;

	procedure setSessionId(
		pSessionId in number
	)
	is
	begin
	    sessionId := pSessionId;
	end;
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
	    
	    info := RDX_Environment.calcProgramName(RDX_Environment_Vars.getInstanceId) || '/' || RDX_Environment.calcModuleName(RDX_Environment_Vars.getSessionOwnerType, RDX_Environment_Vars.getSessionOwnerId);
	    
	    if RDX_Environment_Vars.getRequestInfo is not null then
	        info := info || '/' || RDX_Environment_Vars.getRequestInfo;
	    end if;
	    
	    dbms_application_info.set_client_info(info);
	    dbms_application_info.set_action(RDX_Environment_Vars.getRequestInfo);
	end;

	procedure init(
		instanceId in integer,
		sessionOwnerType in varchar2,
		sessionOwnerId in integer
	)
	is
	begin
	    RDX_Environment_Vars.setInstanceId(instanceId);
	    RDX_Environment_Vars.setSessionOwnerType(sessionOwnerType);
	    RDX_Environment_Vars.setSessionOwnerId(sessionOwnerId);
	    RDX_Environment.updateApplicationClientInfo();
	    dbms_application_info.set_module(RDX_Environment.calcProgramName(instanceId) || '/' || RDX_Environment.calcModuleName(sessionOwnerType, sessionOwnerId), null);
	end;

	procedure setRequestInfo(
		requestInfo in varchar2
	)
	is
	begin
	    RDX_Environment_Vars.setRequestInfo(requestInfo);
	    RDX_Environment.updateApplicationClientInfo();
	end;

	function getTargetExecutorId return integer
	is
	    vInstanceId integer := RDX_Environment_Vars.getInstanceId;
	    vTargetExecutorId integer := RDX_Environment_Vars.getTargetExecutorId;
	begin
	    if vTargetExecutorId = -1 then
	        select RDX_INSTANCE.TARGETEXECUTORID into vTargetExecutorId from RDX_INSTANCE where RDX_INSTANCE.ID = vInstanceId;
	        RDX_Environment_Vars.setTargetExecutorId(vTargetExecutorId);
	    end if;
	    return vTargetExecutorId;
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
	    return RDX_Environment_Vars.getInstanceId;
	end;

	function getSessionOwnerType return varchar2
	is
	begin
	    return RDX_Environment_Vars.getSessionOwnerType;
	end;

	function getSessionOwnerId return integer
	is
	begin
	    return RDX_Environment_Vars.getSessionOwnerId;
	end;
end;
/

create or replace package body RDX_Environment_Vars as

	--filled after creating jdbc connection
	instanceId integer;
	sessionOwnerType varchar2(100);
	sessionOwnerId integer;
	--other
	targetExecutorId integer := -1;
	requestInfo varchar2(500);

	function getInstanceId return integer
	is
	begin
	    return instanceId;
	end;

	procedure setInstanceId(
		pInstanceId in integer
	)
	is
	begin
	    instanceId := pInstanceId;
	end;

	function getSessionOwnerType return varchar2
	is
	begin
	    return sessionOwnerType;
	end;

	procedure setSessionOwnerType(
		pSessionOwnerType in varchar2
	)
	is
	begin
	    sessionOwnerType := pSessionOwnerType;
	end;

	function getSessionOwnerId return integer
	is
	begin
	    return sessionOwnerId;
	end;

	procedure setSessionOwnerId(
		pSessionOwnerId in integer
	)
	is
	begin
	    sessionOwnerId := pSessionOwnerId;
	end;

	function getTargetExecutorId return integer
	is
	begin
	    return targetExecutorId;
	end;

	procedure setTargetExecutorId(
		pTargetExecutorId in integer
	)
	is
	begin
	    targetExecutorId := pTargetExecutorId;
	end;

	function getRequestInfo return varchar2
	is
	begin
	    return requestInfo;
	end;

	procedure setRequestInfo(
		pRequestInfo in varchar2
	)
	is
	begin
	    requestInfo := pRequestInfo;
	end;
end;
/

create or replace package body RDX_JS_CalendarSchedule as

	--constants
	foreread_size CONSTANT INTEGER := 400; --days

	function constClassGuidAbsCalendar return varchar2
	is
	begin
	    return 'aclVV67ZO4QF5GJ7KY7J2AW6UMQUI';
	end;

	function constClassGuidDayOfWeek return varchar2
	is
	begin
	    return 'aclHUCM56OUWZDBXCTBJLJ2S6QQ7E';
	end;

	function constClassGuidDayOfMonth return varchar2
	is
	begin
	    return 'aclPJITOKTE6ZDY5OXI7Z53SB222I';
	end;

	function constClassGuidAbsDate return varchar2
	is
	begin
	    return 'aclRFJRGJ5REZDHDDTBB5R34QG4GQ';
	end;

	function constClassGuidIncCalendar return varchar2
	is
	begin
	    return 'aclDWOG2C7CWVCWBFOEMH62ET56HI';
	end;

	function constClassGuidDaily return varchar2
	is
	begin
	    return 'aclWCDMIFJ755C37LMG7MTQA4DEZQ';
	end;

	function constClassGuidDayOfQuarter return varchar2
	is
	begin
	    return 'aclZMU6F5DMMZCHPPRW4E4ISFQZ4U';
	end;

	function constClassGuidDayOfYear return varchar2
	is
	begin
	    return 'acl3A5HLW7W7ZFE3E6QYCXAC5CYE4';
	end;

	function constKeyFormat return varchar2
	is
	begin
	    return 'YYYY-MM-DD';
	end;

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
	    return TO_CHAR(pDate, RDX_JS_CalendarSchedule.constKeyFormat);
	end;

	function keyToDate(
		pKey in varchar2
	) return date deterministic
	is
	begin
	    return TO_DATE(pKey, RDX_JS_CalendarSchedule.constKeyFormat);
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
	    
	    IF vCalendarClassGuid != RDX_JS_CalendarSchedule.constClassGuidAbsCalendar THEN
	        raise_application_error(-20001, 'Wrong calendar type');
	    END IF;

	    FOR vCalendarItem in cCalendarItems(pId) LOOP
	        curItem.classGuid :=  vCalendarItem.CLASSGUID;
	        curItem.oper :=  vCalendarItem.OPER;
	        curItem.offsetDir :=  vCalendarItem.OFFSETDIR;
	        curItem.offset :=  vCalendarItem.OFFSET; 
	        curItem.absDate :=  vCalendarItem.ABSDATE;
	        curItem.incCalendarId :=  vCalendarItem.INCCALENDARID;

	        IF curItem.classGuid = RDX_JS_CalendarSchedule.constClassGuidDayOfWeek THEN
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
	    
	    IF RDX_JS_Vars.getLastUpdateTime is null or vLastUp != RDX_JS_Vars.getLastUpdateTime THEN
	        RDX_JS_Vars.cachedItems.DELETE();
	        RDX_JS_Vars.cachedPeriodsBegin.DELETE();
	        RDX_JS_Vars.cachedItems.DELETE();
	        RDX_JS_Vars.cachedPeriodsEnd.DELETE();
	        RDX_JS_Vars.cachedDates.DELETE();
	        RDX_JS_Vars.setLastUpdateTime(vLastUp);
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
	                WHEN RDX_JS_CalendarSchedule.constClassGuidDayOfYear THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.isDayInYear(vCurItem.OffsetDir, vCurItem.Offset, vCurDate)=1;
	            
	                WHEN RDX_JS_CalendarSchedule.constClassGuidDayOfQuarter THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.isDayInQuarter(vCurItem.OffsetDir, vCurItem.Offset, vCurDate)=1;            
	            
	                WHEN RDX_JS_CalendarSchedule.constClassGuidDayOfWeek THEN
	                    vDayMatches := RDX_JS_CalendarSchedule.getDayOfWeek(pDate => vCurDate) = vCurItem.dayOfWeek;
	                WHEN RDX_JS_CalendarSchedule.constClassGuidDayOfMonth THEN
	                    IF vCurItem.OffsetDir > 0 THEN
	                        vDayMatches := RDX_JS_CalendarSchedule.getDayOfMonth(pDate => vCurDate) = vCurItem.Offset;
	                    ELSE
	                        vDayMatches := vCurItem.Offset = (RDX_JS_CalendarSchedule.getDaysInMonth(pDate => vCurDate) - RDX_JS_CalendarSchedule.getDayOfMonth(pDate => vCurDate) + 1);
	                    END IF;

	                WHEN RDX_JS_CalendarSchedule.constClassGuidAbsDate THEN
	                    vDayMatches := vCurDate = vCurItem.AbsDate;
	                   
	                WHEN RDX_JS_CalendarSchedule.constClassGuidDaily THEN
	                    vDayMatches := true;

	                WHEN RDX_JS_CalendarSchedule.constClassGuidIncCalendar THEN
	                    vIsInIncCalendar := RDX_JS_CalendarSchedule.isInCached(pId => vCurItem.incCalendarId, pDate => vCurDate);
	                    IF vIsInIncCalendar is not null THEN
	                         vResult := (vCurItem.oper = '+' and vIsInIncCalendar = true) or 
	                         (vCurItem.oper = '-' and 
	                         vIsInIncCalendar = true -- change in RADIX-12059 from 'false' to 'true'
	                         ); 
	                    END IF;
	            END CASE;

	            if vCurItem.classGuid != RDX_JS_CalendarSchedule.constClassGuidIncCalendar and vDayMatches THEN
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

create or replace package body RDX_JS_JOB as

	function constTaskExecutorClassName return varchar2
	is
	begin
	    return 'org.radixware.ads.mdlOWL3XYSM2ZDNHOW7W7XKT54GRE.server.adc2YO4JA7JKJGGBLTARIEQHUSHGI';
	end;

	function constTaskExecutorMethodName return varchar2
	is
	begin
	    return 'mthA4SZ4ODO4NFL3A5KMYIJ54MTZA';
	end;

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
		iTaskId in integer := null,
		aadcMemberId in integer := null,
		threadPoolClassGuid in varchar2 := null,
		threadPoolPid in varchar2 := null,
		threadKey in integer := null
	) return integer
	is
	begin  
	  return RDX_JS_JOB.schedule(iAllowRerun => pAllowRerun, tJobTime => systimestamp+numtodsinterval(iDelayMillis/1000, 'SECOND'), sJobClass => sJobClass, sJobMethod => sJobMethod, iJobPriority => iJobPriority, iJobBoosting => iJobBoosting, sExecRequesterId => sExecRequesterId, sCreatorEntityGuid => sCreatorEntityGuid, sCreatorPid => sCreatorPid, sTitle => sTitle, sScpName => sScpName, iTaskId => iTaskId, pAadcMemberId => aadcMemberId, threadPoolClassGuid => threadPoolClassGuid, threadPoolPid => threadPoolPid, threadKey => threadKey);
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
		pAadcMemberId in integer := null,
		threadPoolClassGuid in varchar2 := null,
		threadPoolPid in varchar2 := null,
		threadKey in integer := null
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
	        SCPNAME, TASKID, EXECUTORID, ALLOWRERUN, AADCMEMBERID, THREADPOOLCLASSGUID, THREADPOOLPID, THREADKEY)
	   values (iJobNewID, sCreatorEntityGuid, sCreatorPid,tJobTime, sJobClass, sJobMethod, nvl(iJobPriority,5), nvl(iJobPriority,5), iJobBoosting, sExecRequesterId, sTitle, 
	        sScpName, iTaskId, RDX_Environment.getTargetExecutorId(), iAllowRerun, vAadcMemberId, threadPoolClassGuid, threadPoolPid, threadKey);
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
	  
	  vJobId := RDX_JS_JOB.schedule(iAllowRerun => 0, tJobTime => actualExecTime, sJobClass => RDX_JS_JOB.constTaskExecutorClassName, sJobMethod => RDX_JS_JOB.constTaskExecutorMethodName, iJobPriority => priority, iJobBoosting => priorityBoostingSpeed, sExecRequesterId => 'ScheduledTask-'|| taskId, sCreatorEntityGuid => 'tblWZB7K4HLJPOBDCIUAALOMT5GDM', sCreatorPid => taskId, sTitle => jobTitle, sScpName => scpName, iTaskId => taskId, pAadcMemberId => vAadcMemberId);
	    
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
	    
	    if pClassName = RDX_JS_JOB.constTaskExecutorClassName and pMethodName = RDX_JS_JOB.constTaskExecutorMethodName then
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

create or replace package body RDX_JS_Vars as

	lastUpdateTime date;

	function getLastUpdateTime return date
	is
	begin
	    return lastUpdateTime;
	end;

	procedure setLastUpdateTime(
		pLastUpdateTime in date
	)
	is
	begin
	    lastUpdateTime := pLastUpdateTime;
	end;
end;
/

create or replace package body RDX_WF as

	function constDebugRole return varchar2
	is
	begin
	    return 'rol7MTXJKWQORCJFD7EEZHGZ7TOJM';
	end;

	function constAdminRole return varchar2
	is
	begin
	    return 'rolHM6FIFA4JFCJFIYPB3P2KC435U';
	end;

	function constClerkRole return varchar2
	is
	begin
	    return 'rolOYMAGGZIXNF6TMGZASCZET6VOI';
	end;



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

	    if RDX_ACS.curUserHasRoleInArea( RDX_WF.constAdminRole, accessArea) = 0 and RDX_ACS.curUserHasRoleInArea(RDX_WF.constDebugRole, accessArea) = 0 then
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

	    if RDX_ACS.curUserHasRoleInArea(RDX_WF.constAdminRole, accessArea) = 0 and RDX_ACS.curUserHasRoleInArea(RDX_WF.constDebugRole, accessArea) = 0then
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

	    if RDX_ACS.userHasRoleInArea(pUser, RDX_WF.constClerkRole, accessArea) = 0 then
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

