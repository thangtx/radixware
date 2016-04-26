create or replace type RDX_EVENT_LOG_RECORD as object(
	code varchar2(250 char),
	words varchar2(4000 char),
	component varchar2(1000 char),
	severity number(1,0),
	contextTypes varchar2(4000 char),
	contextIds varchar2(1000 char),
	time timestamp(6),
	userName varchar2(250 char),
	stationName varchar2(100 char),
	isSensitive number(1,0)
);
/

grant execute on RDX_EVENT_LOG_RECORD to &USER&_RUN_ROLE
/

create or replace type RDX_EVENT_LOG_RECORDS as table of RDX_EVENT_LOG_RECORD;
/

grant execute on RDX_EVENT_LOG_RECORDS to &USER&_RUN_ROLE
/

create or replace type TRdxAcsArea as object(
	boundaries TRdxAcsCoordinates
);
/

grant execute on TRdxAcsArea to &USER&_RUN_ROLE
/

create or replace type TRdxAcsAreaList as varray(5) of TRdxAcsArea;
/

grant execute on TRdxAcsAreaList to &USER&_RUN_ROLE
/

create or replace type TRdxAcsCoordinate as object(
	Prohibited integer(1),
	FamilyID varchar2(50),
	KeyVal varchar2(4000)
);
/

grant execute on TRdxAcsCoordinate to &USER&_RUN_ROLE
/

create or replace type TRdxAcsCoordinates as varray(10) of TRdxAcsCoordinate;
/

grant execute on TRdxAcsCoordinates to &USER&_RUN_ROLE
/

create or replace package RDX_ACS as

	cRight_unbounded   constant integer := 0;
	cRight_bounded     constant integer := 1;
	cRight_prohibited  constant integer := 2;

	procedure clearInheritRights(
		pUserGroup in varchar2
	);

	/*
	Функции используемые для проверки того чтобы пользователь не смог 
	назначить дублирующие права, ...

	*/

	function userHasRoleForObjectInternal(
		pUser in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasRoleForObjectInternal, WNDS);

	function userHasRoleForObjectInternal(
		pUser in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea,
		checkInheritGroupRights in integer,
		exceptingID1_ in integer,
		exceptingID2_ in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (userHasRoleForObjectInternal, WNDS);

	function groupHasRoleForObjectInternal(
		pGroup in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea
	) return integer;
	PRAGMA RESTRICT_REFERENCES (groupHasRoleForObjectInternal, WNDS);

	function groupHasRoleForObjectInternal(
		pGroup in varchar2,
		pRole in varchar2,
		pArea in TRdxAcsArea,
		exceptingID1_ in integer,
		exceptingID2_ in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (groupHasRoleForObjectInternal, WNDS);

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

	-- Имеет ли текущий пользователь права не меньшие чем запись в таблице User2Role с заданным идом
	function curUserHasRightsU2RId(
		ID_ in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserHasRightsU2RId, WNDS);

	-- Имеет ли текущий пользователь права не меньшие чем запись в таблице UserGroup2Role с заданным идом
	function curUserHasRightsG2RId(
		ID_ in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (curUserHasRightsG2RId, WNDS);

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

	procedure acsUtilsBuild;

	function isGroupExist(
		name_ in varchar2
	) return integer;

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
end;
/

grant execute on RDX_ACS to &USER&_RUN_ROLE
/

create or replace package RDX_ACS_UTILS as

	function buildAssignedAccessAreaU2R(
		row_ in RDX_AC_User2Role%ROWTYPE
	) return TRdxAcsArea;
	PRAGMA RESTRICT_REFERENCES (buildAssignedAccessAreaU2R, WNDS);

	function buildAssignedAccessAreaG2R(
		row_ in RDX_AC_UserGroup2Role%ROWTYPE
	) return TRdxAcsArea;
	PRAGMA RESTRICT_REFERENCES (buildAssignedAccessAreaG2R, WNDS);

	procedure compileRights;

	procedure compileRightsForGroup(
		pUserGroup in varchar2
	);

	procedure compileRightsForUser(
		pUser in varchar2
	);

	procedure compileRightsForGrpBeforeDel(
		pUserGroup in varchar2
	);

	procedure moveRightsFromUserToGroup(
		user_ in varchar2,
		group_ in varchar2
	);
end;
/

grant execute on RDX_ACS_UTILS to &USER&_RUN_ROLE
/

-- Replacement for DAC_INFO
create or replace package RDX_ADS_META as

	function isDefInDomain(
		pDefId in varchar2,
		pDomainId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isDefInDomain, WNDS);

	function isEnumValueInDomain(
		pEnumId in varchar2,
		pEnumItemValAsStr in varchar2,
		pDomainId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isEnumValueInDomain, WNDS);

	function isClassExtends(
		pClsId in varchar2,
		pBaseClsId in varchar2
	) return integer;
	PRAGMA RESTRICT_REFERENCES (isClassExtends, WNDS);

	function areVersionIndicesDeployed(
		pVersionNum in integer
	) return integer;
	PRAGMA RESTRICT_REFERENCES (areVersionIndicesDeployed, WNDS);

	procedure truncDeployedIndices;
end;
/

grant execute on RDX_ADS_META to &USER&_RUN_ROLE
/

create or replace package RDX_AUDIT as

	procedure dailyMaintenance;

	procedure updateTableAuditState(
		tableGuid in varchar2,
		tableParentGuid in varchar2
	);

	procedure enableTrigger(
		pId in varchar2,
		pType in varchar2
	);

	procedure disableTrigger(
		pTableid in varchar2,
		pType in varchar2,
		pParentTableid in varchar2
	);

	procedure addValueNum(
		pData in out clob,
		pName in varchar2,
		pValue in number
	);

	procedure addValueInt(
		pData in out clob,
		pName in varchar2,
		pValue in integer
	);

	procedure addValueStr(
		pData in out clob,
		pName in varchar2,
		pValue in varchar2
	);

	procedure addValueDate(
		pData in out clob,
		pName in varchar2,
		pValue in date
	);

	procedure regPropNumUpdate(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in number,
		newVal in number
	);

	procedure regPropStrUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in varchar2,
		newVal in varchar2
	);

	procedure regPropBoolUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in boolean,
		newVal in boolean
	);

	procedure regPropDateTimeUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in date,
		newVal in date
	);

	procedure regPropClobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in CLOB,
		newVal in CLOB
	);

	procedure regPropBlobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in BLOB,
		newVal in BLOB
	);

	procedure regPropRawUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in RAW,
		newVal in RAW
	);

	procedure regPropIntUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in integer,
		newVal in integer
	);

	procedure addValueClob(
		pData in out clob,
		pName in varchar2,
		pValue in CLOB
	);

	procedure addValueBlob(
		pData in out clob,
		pName in varchar2,
		pValue in BLOB
	);

	procedure addValueRaw(
		pData in out clob,
		pName in varchar2,
		pValue in RAW
	);

	procedure addValueBool(
		pData in out clob,
		pName in varchar2,
		pValue in boolean
	);

	procedure regPropNumCreated(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	);

	procedure regPropStrCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	);

	procedure regPropBoolCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	);

	procedure regPropDateTimeCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	);

	procedure regPropClobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	);

	procedure regPropBlobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	);

	procedure regPropRawCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	);

	procedure regPropIntCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	);

	procedure regPropNumDeleted(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	);

	procedure regPropStrDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	);

	procedure regPropBoolDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	);

	procedure regPropDateTimeDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	);

	procedure regPropClobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	);

	procedure regPropBlobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	);

	procedure regPropRawDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	);

	procedure regPropIntDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	);

	procedure addChangedValueInt(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in integer,
		pNewValue in integer
	);

	procedure addChangedValueNum(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in number,
		pNewValue in number
	);

	procedure addChangedValueBool(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in boolean,
		pNewValue in boolean
	);

	procedure addChangedValueStr(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in varchar2,
		pNewValue in varchar2
	);

	procedure addChangedValueClob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in CLOB,
		pNewValue in CLOB
	);

	procedure addChangedValueDateTime(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in date,
		pNewValue in date
	);

	procedure addChangedValueRaw(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in RAW,
		pNewValue in RAW
	);

	procedure addChangedValueBlob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in BLOB,
		pNewValue in BLOB
	);

	procedure updateTableAuditStateEventType(
		pTableGuid in varchar2,
		pEventType in varchar2,
		pParentTableGuid in varchar2
	);

	DEFAULTAUDITSCHEME varchar2(50);
end;
/

grant execute on RDX_AUDIT to &USER&_RUN_ROLE
/

create or replace package RDX_Array as

	Type ARR_STR IS TABLE OF VARCHAR2(32767);

	function merge(
		e1 in varchar2
	) return CLOB;

	function merge(
		e1 in varchar2,
		e2 in varchar2
	) return CLOB;

	function merge(
		e1 in varchar2,
		e2 in varchar2,
		e3 in varchar2
	) return CLOB;

	function merge(
		e1 in number
	) return CLOB;

	function merge(
		e1 in number,
		e2 in number
	) return CLOB;

	function merge(
		e1 in number,
		e2 in number,
		e3 in number
	) return CLOB;

	function fromStr(
		lob in clob
	) return ARR_STR deterministic;
	PRAGMA RESTRICT_REFERENCES (fromStr, WNDS);

	function fromArrStr(
		arr_ in ARR_STR
	) return clob;

	procedure appendStr(
		res in out clob,
		x in varchar2
	);

	procedure appendNum(
		res in out clob,
		x in number
	);

	procedure appendDate(
		res in out clob,
		x in timestamp
	);

	procedure appendRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2
	);

	-- Нумерация осуществляется с единицы
	procedure insertStr(
		res in out clob,
		x in varchar2,
		index_ in integer
	);

	-- Нумерация осуществляется с единицы
	procedure insertNum(
		res in out clob,
		x in number,
		index_ in integer
	);

	-- Нумерация осуществляется с единицы
	procedure insertDate(
		res in out clob,
		x in timestamp,
		index_ in integer
	);

	procedure insertRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2,
		index_ in integer
	);

	-- Нумерация осуществляется с единицы
	function getStr(
		lob in clob,
		index_ in integer
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (getStr, WNDS);

	-- Нумерация осуществляется с единицы
	function getNum(
		lob in clob,
		index_ in integer
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (getNum, WNDS);

	-- Нумерация осуществляется с единицы
	function getDate(
		lob in clob,
		index_ in integer
	) return timestamp deterministic;
	PRAGMA RESTRICT_REFERENCES (getDate, WNDS);

	-- Нумерация осуществляется с единицы
	function searchStr(
		lob in clob,
		what in varchar2,
		startIdx in integer
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchStr, WNDS);

	-- Нумерация осуществляется с единицы
	function searchNum(
		lob in clob,
		what in number,
		startIdx in number
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchNum, WNDS);

	-- Нумерация осуществляется с единицы
	function searchDate(
		lob in clob,
		what in timestamp,
		startIdx in number
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchDate, WNDS);

	function searchRef(
		lob in clob,
		tableGuid in varchar2,
		what in varchar2,
		startIdx in integer
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (searchRef, WNDS);

	function getArraySize(
		lob in clob
	) return integer;
	PRAGMA RESTRICT_REFERENCES (getArraySize, RNDS);
	PRAGMA RESTRICT_REFERENCES (getArraySize, RNPS);
	PRAGMA RESTRICT_REFERENCES (getArraySize, WNDS);
	PRAGMA RESTRICT_REFERENCES (getArraySize, WNPS);

	-- Нумерация осуществляется с единицы
	procedure setStr(
		lob in out clob,
		x in varchar2,
		Idx in number
	);

	-- Нумерация осуществляется с единицы
	procedure setNum(
		lob in out clob,
		x in number,
		Idx in number
	);

	-- Нумерация осуществляется с единицы
	procedure setDate(
		lob in out clob,
		x in timestamp,
		idx in number
	);

	procedure setRef(
		lob in out clob,
		tableGuid in varchar2,
		x in varchar2,
		idx in number
	);

	procedure remove(
		res in out clob,
		index_ in integer
	);

	procedure removeAll(
		res in out clob,
		val in varchar2
	);

	function createRef(
		tableGuid in varchar2,
		x in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (createRef, WNDS);

	function search(
		lob in clob,
		what in varchar2,
		startIdx in integer,
		asNumber in boolean
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (search, WNDS);
end;
/

grant execute on RDX_Array to &USER&_RUN_ROLE
/

create or replace package RDX_Arte as

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
	);

	function getClientLanguage return integer;
	PRAGMA RESTRICT_REFERENCES (getClientLanguage, WNDS);

	function getStationName return varchar2;
	PRAGMA RESTRICT_REFERENCES (getStationName, WNDS);

	function getUserName return varchar2;
	PRAGMA RESTRICT_REFERENCES (getUserName, WNDS);

	function getVersion return integer;
	PRAGMA RESTRICT_REFERENCES (getVersion, WNDS);

	function getSessionId return integer;
	PRAGMA RESTRICT_REFERENCES (getSessionId, WNDS);

	procedure setSessionIsActive(
		pSessionId in integer,
		pIsActive in integer
	);

	procedure unregisterSession(
		pEasSessionId in integer
	);
end;
/

grant execute on RDX_Arte to &USER&_RUN_ROLE
/

create or replace package RDX_DDS_META as

	function isDbOptionEnabled(
		optionQualifiedName in varchar2
	) return integer;

	function isDbOptionEnabled(
		optionLayerUri in varchar2,
		optionSimpleName in varchar2
	) return integer;

	function compareVersions(
		versionAsStr1 in varchar2,
		versionAsStr2 in varchar2
	) return integer deterministic;
	PRAGMA RESTRICT_REFERENCES (compareVersions, RNDS);
	PRAGMA RESTRICT_REFERENCES (compareVersions, WNDS);
end;
/

grant execute on RDX_DDS_META to &USER&_RUN_ROLE
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

	type TScheduledOnDelUpOwner is record (
	   sOwnerEntityId  varchar2(100),
	   sOwnerPid  varchar2(250)
	);
	type TScheduledOnDelUpOwnerQueue is table of TScheduledOnDelUpOwner index by binary_integer;
	scheduledOnDelUpOwnerQueue TScheduledOnDelUpOwnerQueue;

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

create or replace package RDX_Environment as

	procedure init(
		instanceId in integer,
		sessionOwnerType in varchar2,
		sessionOwnerId in integer
	);

	procedure setRequestInfo(
		requestInfo in varchar2
	);

	function getTargetExecutorId return integer;

	function getApplicationClientInfo return varchar2;

	function getInstanceId return varchar2;

	function getSessionOwnerType return varchar2;

	function getSessionOwnerId return integer;
end;
/

grant execute on RDX_Environment to &USER&_RUN_ROLE
/

create or replace package RDX_JS_CalendarSchedule as

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

create or replace package RDX_JS_EventSchedule as

	function next(
		pId in integer,
		pDateTime in timestamp
	) return timestamp;

	function prev(
		pId in integer,
		pDateTime in timestamp
	) return timestamp;
end;
/

grant execute on RDX_JS_EventSchedule to &USER&_RUN_ROLE
/

create or replace package RDX_JS_IntervalSchedule as

	function isIn(
		pId in integer,
		pDateTime in date
	) return integer;

	function nextStartTime(
		pId in integer,
		pDateTime in date
	) return date;

	function nextFinishTime(
		pId in integer,
		pDateTime in date
	) return date;

	function prevStartTime(
		pId in integer,
		pDateTime in date
	) return date;

	function prevFinishTime(
		pId in integer,
		pDateTime in date
	) return date;
end;
/

grant execute on RDX_JS_IntervalSchedule to &USER&_RUN_ROLE
/

create or replace package RDX_JS_JOB as

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
		iTaskId in integer := null
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
		iTaskId in integer := null
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
end;
/

grant execute on RDX_JS_JOB to &USER&_RUN_ROLE
/

create or replace package RDX_License as

	procedure dailyMaintenance;
end;
/

grant execute on RDX_License to &USER&_RUN_ROLE
/

create or replace package RDX_Lock as

	function request(
		pTabId in varchar2,
		pPid in varchar2,
		pTimeoutSec in integer,
		pLockId in out varchar2
	) return integer;

	function release(
		pLockId in varchar2
	) return integer;

	function getLockHandle(
		tableId in varchar2,
		pid in varchar2
	) return integer;

	function getLockName(
		tableId in varchar2,
		pid in varchar2
	) return varchar2;
end;
/

grant execute on RDX_Lock to &USER&_RUN_ROLE
/

-- System maintenance
create or replace package RDX_Maintenance as

	-- Ежеминутный (примерно) maintenance
	procedure continualy;

	-- Perform daily maintenance
	procedure daily;

	procedure clearProfileLog;
end;
/

grant execute on RDX_Maintenance to &USER&_RUN_ROLE
/

create or replace package RDX_Net as

	function getNewStan(
		unitId in integer
	) return integer;
end;
/

grant execute on RDX_Net to &USER&_RUN_ROLE
/

create or replace package RDX_PC_Maintenance as

	procedure daily;
end;
/

grant execute on RDX_PC_Maintenance to &USER&_RUN_ROLE
/

create or replace package RDX_PC_Utils as

	function findSuitableChannelId(
		pKind in varchar2,
		pAddress in varchar2,
		pImportance in integer,
		pExcludeUnitId in integer,
		pPrevPriority in integer
	) return integer;

	function extractActualAddress(
		kind in varchar2,
		address in varchar2
	) return varchar2;
end;
/

grant execute on RDX_PC_Utils to &USER&_RUN_ROLE
/

create or replace package RDX_SM_METRIC as

	procedure dailyMaintenance;
end;
/

grant execute on RDX_SM_METRIC to &USER&_RUN_ROLE
/

create or replace package RDX_Trace as

	procedure put(
		pSeverity in integer,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	);

	procedure put(
		pSeverity in integer,
		pCode in varchar2,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	);

	procedure put_1word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	);

	procedure put_2word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	);

	procedure put_3word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pWord3 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	);

	-- for internal use
	function put_internal(
		pCode in varchar2,
		pWords in clob,
		pComponent in varchar2,
		pSeverity in number,
		pContextTypes in varchar2,
		pContextIds in varchar2,
		pTime in timestamp := NULL,
		pUserName in varchar2 := NULL,
		pStationName in varchar2 := NULL,
		pIsSensitive in integer := 0
	) return integer;

	procedure maintenance;

	procedure clearSensitiveData(
		pForce in boolean := false
	);

	procedure deleteDebug;

	function put_records(
		pRecords in RDX_EVENT_LOG_RECORDS
	) return integer;

	procedure dropEventPartitionsOlderThan(
		d in date
	);
end;
/

grant execute on RDX_Trace to &USER&_RUN_ROLE
/

create or replace package RDX_ValAsStr as

	function dateTimeToValAsStr(
		pDateTime in timestamp
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (dateTimeToValAsStr, WNDS);

	function dateTimeFromValAsStr(
		pValAsStr in varchar2
	) return timestamp deterministic;
	PRAGMA RESTRICT_REFERENCES (dateTimeFromValAsStr, WNDS);

	function numToValAsStr(
		pNum in number
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (numToValAsStr, WNDS);

	function numFromValAsStr(
		pValAsStr in varchar2
	) return number deterministic;
	PRAGMA RESTRICT_REFERENCES (numFromValAsStr, WNDS);
end;
/

grant execute on RDX_ValAsStr to &USER&_RUN_ROLE
/

create or replace package RDX_WF as

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

create or replace package RDX_WF_Maintenance as

	procedure daily;
end;
/

grant execute on RDX_WF_Maintenance to &USER&_RUN_ROLE
/

create sequence SQN_COUNTERID
	order
/

grant select on SQN_COUNTERID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_AC_USER2ROLEID
	increment by 1
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_AC_USER2ROLEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_AC_USERGROUP2ROLEID
	increment by 1
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_AC_USERGROUP2ROLEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_AU_AUDITLOGID
	increment by 1
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_AU_AUDITLOGID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_CLASSLOADINGPROFILEID
	order
/

grant select on SQN_RDX_CLASSLOADINGPROFILEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_CLASSLOADINGPROFILEITE
	order
/

grant select on SQN_RDX_CLASSLOADINGPROFILEITE to &USER&_RUN_ROLE
/

create sequence SQN_RDX_CM_ITEMID
	increment by 1
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_CM_ITEMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_CM_PACKETID
	increment by 1
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_CM_PACKETID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_DM_PACKETID
	order
/

grant select on SQN_RDX_DM_PACKETID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_EASSESSIONID
	increment by 1
	start with 1
	maxvalue 999999999
	minvalue 1
	cycle
	order
/

grant select on SQN_RDX_EASSESSIONID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_EVENTLOGID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_EVENTLOGID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_INSTANCEID
	increment by 1
	start with 2
	minvalue 1
	order
/

grant select on SQN_RDX_INSTANCEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_CALENDARID
	order
/

grant select on SQN_RDX_JS_CALENDARID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_CALENDARITEMID
	order
/

grant select on SQN_RDX_JS_CALENDARITEMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_EVENTSCHDID
	minvalue 10
	order
/

grant select on SQN_RDX_JS_EVENTSCHDID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_EVENTSCHDITEMID
	minvalue 10
	order
/

grant select on SQN_RDX_JS_EVENTSCHDITEMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_INTERVALSCHDID
	order
/

grant select on SQN_RDX_JS_INTERVALSCHDID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_INTERVALSCHDITEMID
	order
/

grant select on SQN_RDX_JS_INTERVALSCHDITEMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_JOBID
	minvalue 1
	order
/

grant select on SQN_RDX_JS_JOBID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_JS_SCHDUNITJOBID
	minvalue 10
	order
/

grant select on SQN_RDX_JS_SCHDUNITJOBID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_LICENSEREPORTID
	order
/

grant select on SQN_RDX_LICENSEREPORTID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_MESSAGEQUEUEID
	order
/

grant select on SQN_RDX_MESSAGEQUEUEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_MQPROCESSRORSEQ
	order
/

grant select on SQN_RDX_MQPROCESSRORSEQ to &USER&_RUN_ROLE
/

create sequence SQN_RDX_NETCHANNELID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_NETCHANNELID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_PC_CHANNELGSMMODEMID
	order
/

grant select on SQN_RDX_PC_CHANNELGSMMODEMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_PC_EVENTSUBSCRIPTIONID
	order
/

grant select on SQN_RDX_PC_EVENTSUBSCRIPTIONID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_PC_MESSAGEID
	order
/

grant select on SQN_RDX_PC_MESSAGEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_PROFILERLOGID
	order
/

grant select on SQN_RDX_PROFILERLOGID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_REPORTPARAMID
	order
/

grant select on SQN_RDX_REPORTPARAMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_REPORTPUBID
	order
/

grant select on SQN_RDX_REPORTPUBID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_REPORTPUBLISTID
	order
/

grant select on SQN_RDX_REPORTPUBLISTID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_REPORTPUBTOPICID
	order
/

grant select on SQN_RDX_REPORTPUBTOPICID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SAP_ID
	increment by 1
	start with 10
	minvalue 1
	order
/

grant select on SQN_RDX_SAP_ID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SB_PIPELINEID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_SB_PIPELINEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SB_PIPELINENODEID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_SB_PIPELINENODEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SB_TRANSFORMSTAGEID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_SB_TRANSFORMSTAGEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SB_XPATHTABLEID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_SB_XPATHTABLEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SM_METRICHISTID
	order
/

grant select on SQN_RDX_SM_METRICHISTID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SM_METRICSTATEID
	order
/

grant select on SQN_RDX_SM_METRICSTATEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SM_METRICTYPEID
	order
/

grant select on SQN_RDX_SM_METRICTYPEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SM_SNMPMANAGERID
	order
/

grant select on SQN_RDX_SM_SNMPMANAGERID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_SYSTEM_ID
	increment by 1
	start with 2
	minvalue 2
	order
/

grant select on SQN_RDX_SYSTEM_ID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TESTCASEID
	increment by 1
	start with 1
	maxvalue 999999999
	minvalue 1
	order
/

grant select on SQN_RDX_TESTCASEID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TST_AUDIT
	order
/

grant select on SQN_RDX_TST_AUDIT to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TST_DMTESTCHILDID
	order
/

grant select on SQN_RDX_TST_DMTESTCHILDID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TST_DMTESTID
	order
/

grant select on SQN_RDX_TST_DMTESTID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TST_INHERITANCENODESEQ
	order
/

grant select on SQN_RDX_TST_INHERITANCENODESEQ to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TST_SQNTEDCCHILD
	order
/

grant select on SQN_RDX_TST_SQNTEDCCHILD to &USER&_RUN_ROLE
/

create sequence SQN_RDX_TST_SQNTEDCPARENT
	order
/

grant select on SQN_RDX_TST_SQNTEDCPARENT to &USER&_RUN_ROLE
/

create sequence SQN_RDX_UNITID
	increment by 1
	start with 10
	minvalue 1
	order
/

grant select on SQN_RDX_UNITID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_USERFUNCID
	order
/

grant select on SQN_RDX_USERFUNCID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_USERREPORTVERSIONID
	order
/

grant select on SQN_RDX_USERREPORTVERSIONID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_WF_FORMID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_WF_FORMID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_WF_FORMLOGID
	minvalue 1
	order
/

grant select on SQN_RDX_WF_FORMLOGID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_WF_PROCESSID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_WF_PROCESSID to &USER&_RUN_ROLE
/

create sequence SQN_RDX_WF_PROCESSLINKID
	start with 1
	minvalue 1
	order
/

grant select on SQN_RDX_WF_PROCESSLINKID to &USER&_RUN_ROLE
/

create table COUNTER(
	ID NUMBER(18,0) not null,
	CLASSGUID VARCHAR2(100 char) null,
	VALUE_INCREMENT NUMBER(9,0) null,
	DEFAULTVALUE NUMBER(9,0) default 0 not null,
	VALUE NUMBER(9,0) default 0 not null,
	RESETPERIOD NUMBER(9,0) null,
	LASTUPDATETIME TIMESTAMP(6) null,
	LASTRESETTIME TIMESTAMP(6) null,
	UPDEFID VARCHAR2(100 char) not null,
	UPOWNERENTITYID VARCHAR2(100 char) not null,
	UPOWNERPID VARCHAR2(200 char) not null)
/

grant select, update, insert, delete on COUNTER to &USER&_RUN_ROLE
/

create table RDX_AC_APPROLE(
	GUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(250 char) not null,
	DESCRIPTION VARCHAR2(1000 char) null,
	DEFINITION CLOB null,
	RUNTIME BLOB null,
	ROLECLASSGUID VARCHAR2(100 char) null,
	LASTUPDATETIME TIMESTAMP(6) null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_AC_APPROLE to &USER&_RUN_ROLE
/

create table RDX_AC_USER(
	NAME VARCHAR2(250 char) not null,
	LOCKED NUMBER(1,0) default 0 not null,
	LOCKREASON NUMBER(2,0) null,
	PERSONNAME VARCHAR2(4000 char) null,
	ADMINGROUPNAME VARCHAR2(250 char) null,
	LASTPWDCHANGETIME DATE null,
	PWDEXPIRATIONPERIOD NUMBER(9,0) default 90 null,
	PWDHASH VARCHAR2(32 char) null,
	AUTHTYPES VARCHAR2(250 char) null,
	PWDHASHHISTORY CLOB null,
	CHECKSTATION NUMBER(1,0) default 0 not null,
	EMAIL VARCHAR2(100 char) null,
	MOBILEPHONE VARCHAR2(20 char) null,
	LASTLOGONTIME DATE default SYSDATE not null,
	INVALIDLOGONCNT NUMBER(9,0) default 0 not null,
	INVALIDLOGONTIME DATE not null,
	MUSTCHANGEPWD NUMBER(1,0) default 1 not null,
	DBTRACEPROFILE VARCHAR2(4000 char) null,
	TRACEGUIACTIONS NUMBER(1,0) default 0 not null,
	CREATETIME DATE null,
	LOGONSCHEDULEID NUMBER(9,0) null,
	SESSIONSLIMIT NUMBER(9,0) default 0 null)
/

grant select, update, insert, delete on RDX_AC_USER to &USER&_RUN_ROLE
/

create table RDX_AC_USER2ROLE(
	ID NUMBER(18,0) not null,
	USERNAME VARCHAR2(250 char) not null,
	ISOWN NUMBER(1,0) default 1 not null,
	ROLEID VARCHAR2(100 char) null,
	MA$$1ZOQHCO35XORDCV2AANE2UAFXA NUMBER(1,0) default 0 not null,
	PA$$1ZOQHCO35XORDCV2AANE2UAFXA VARCHAR2(250 char) null,
	ISNEW NUMBER(1,0) default 0 null,
	REPLACEDID NUMBER(18,0) null,
	EDITORNAME VARCHAR2(250 char) null,
	ACCEPTORNAME VARCHAR2(250 char) null,
	MA$$4PQ4U65VK5HFVJ32XCUORBKRJM NUMBER(1,0) default 0 not null,
	PA$$4PQ4U65VK5HFVJ32XCUORBKRJM VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_AC_USER2ROLE to &USER&_RUN_ROLE
/

create table RDX_AC_USER2USERGROUP(
	USERNAME VARCHAR2(250 char) not null,
	GROUPNAME VARCHAR2(250 char) not null,
	STATE NUMBER(1,0) default 0 null,
	EDITORNAME VARCHAR2(250 char) null,
	ACCEPTORNAME VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_AC_USER2USERGROUP to &USER&_RUN_ROLE
/

create table RDX_AC_USERGROUP(
	NAME VARCHAR2(250 char) not null,
	TITLE VARCHAR2(250 char) null,
	DESCRIPTION VARCHAR2(1000 char) null,
	LOGONSCHEDULEID NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_AC_USERGROUP to &USER&_RUN_ROLE
/

create table RDX_AC_USERGROUP2ROLE(
	ID NUMBER(18,0) not null,
	GROUPNAME VARCHAR2(250 char) not null,
	ROLEID VARCHAR2(100 char) null,
	MA$$1ZOQHCO35XORDCV2AANE2UAFXA NUMBER(1,0) default 0 not null,
	PA$$1ZOQHCO35XORDCV2AANE2UAFXA VARCHAR2(250 char) null,
	ISNEW NUMBER(1,0) default 0 null,
	REPLACEDID NUMBER(18,0) null,
	EDITORNAME VARCHAR2(250 char) null,
	ACCEPTORNAME VARCHAR2(250 char) null,
	MA$$4PQ4U65VK5HFVJ32XCUORBKRJM NUMBER(1,0) default 0 not null,
	PA$$4PQ4U65VK5HFVJ32XCUORBKRJM VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_AC_USERGROUP2ROLE to &USER&_RUN_ROLE
/

create table RDX_ARTEINSTANCE(
	INSTANCEID NUMBER(9,0) not null,
	SERIAL NUMBER(9,0) not null,
	SEQ NUMBER(9,0) not null,
	UNITID NUMBER(9,0) null,
	SID NUMBER(9,0) null,
	STATE VARCHAR2(1000 char) null,
	BUSYTIMEMILLIS NUMBER(18,0) null,
	LIFEMILLIS NUMBER(18,0) null)
/

grant select, update, insert, delete on RDX_ARTEINSTANCE to &USER&_RUN_ROLE
/

create table RDX_ARTEUNIT(
	ID NUMBER(9,0) not null,
	SAPID NUMBER(9,0) not null,
	SERVICEURI VARCHAR2(250 char) not null,
	HIGHARTEINSTCOUNT NUMBER(9,0) not null,
	AVGACTIVEARTECOUNT NUMBER default 0 not null,
	THREADPRIORITY NUMBER(2,0) null)
/

grant select, update, insert, delete on RDX_ARTEUNIT to &USER&_RUN_ROLE
/

create table RDX_AU_AUDITLOG(
	ID NUMBER(12,0) not null,
	EVENTTIME TIMESTAMP(6) not null,
	STOREDURATION NUMBER(1,0) default 1 not null,
	USERNAME VARCHAR2(250 char) null,
	STATIONNAME VARCHAR2(100 char) null,
	TABLEID VARCHAR2(50 char) not null,
	CLASSID VARCHAR2(50 char) null,
	PID VARCHAR2(4000 char) not null,
	EVENTTYPE VARCHAR2(1 char) not null,
	EVENTDATA CLOB null)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
	partition by range (EVENTTIME) INTERVAL(NUMTODSINTERVAL(1,'DAY'))
		    subpartition by list (STOREDURATION)
		    subpartition template (
		      SUBPARTITION T1 values(1),
		      SUBPARTITION T2 values(2),
		      SUBPARTITION T3 values(3),
		      SUBPARTITION T4 values(4),
		      SUBPARTITION T5 values(5)
		    )
		    (
		      PARTITION RDX_AU_AUDITLOG_0 VALUES LESS THAN (to_date('&sysdate:yyyy_MM_dd&','YYYY_MM_DD'))
		    )
#ENDIF
/

grant select, update, insert, delete on RDX_AU_AUDITLOG to &USER&_RUN_ROLE
/

create table RDX_AU_SCHEME(
	GUID VARCHAR2(50 char) not null,
	TITLE VARCHAR2(200 char) not null,
	NOTES VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_AU_SCHEME to &USER&_RUN_ROLE
/

create table RDX_AU_SCHEMEITEM(
	SCHEMEGUID VARCHAR2(50 char) not null,
	TABLEID VARCHAR2(50 char) not null,
	EVENTTYPE VARCHAR2(1 char) not null,
	SAVEDATA NUMBER(1,0) default 1 not null,
	STOREDURATION NUMBER(1,0) default 1 not null)
/

grant select, update, insert, delete on RDX_AU_SCHEMEITEM to &USER&_RUN_ROLE
/

create global temporary table RDX_CLASSANCESTOR(
	VERSIONNUM NUMBER(18,0) not null,
	CLASSID VARCHAR2(100 char) not null,
	ANCESTORID VARCHAR2(100 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_CLASSANCESTOR to &USER&_RUN_ROLE
/

create table RDX_CLASSLOADINGPROFILE(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(100 char) not null,
	CREATEDATE DATE not null,
	CREATEUSER VARCHAR2(200 char) null)
/

grant select, update, insert, delete on RDX_CLASSLOADINGPROFILE to &USER&_RUN_ROLE
/

create table RDX_CLASSLOADINGPROFILEITEM(
	ID NUMBER(9,0) not null,
	PROFILEID NUMBER(9,0) not null,
	CLASSNAME VARCHAR2(300 char) not null)
/

grant select, update, insert, delete on RDX_CLASSLOADINGPROFILEITEM to &USER&_RUN_ROLE
/

create table RDX_CM_ITEM(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(50 char) not null,
	PACKETID NUMBER(9,0) not null,
	PARENTID NUMBER(9,0) null,
	SRCOBJECTPID VARCHAR2(1000 char) null,
	SRCOBJECTRID VARCHAR2(1000 char) null,
	SRCOBJECTTITLE VARCHAR2(1000 char) null,
	SKIP NUMBER(1,0) default 0 not null,
	OBJSTATE VARCHAR2(20 char) null,
	ALLREFSSTATE VARCHAR2(20 char) null,
	DATA CLOB null,
	NOTES VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_CM_ITEM to &USER&_RUN_ROLE
/

create table RDX_CM_ITEMREF(
	UPDEFID VARCHAR2(100 char) not null,
	UPOWNERENTITYID VARCHAR2(100 char) not null,
	UPOWNERPID VARCHAR2(200 char) not null,
	CLASSGUID VARCHAR2(29 char) not null,
	ISNILABLE NUMBER(1,0) default 0 not null,
	ISINHERITABLE NUMBER(1,0) default 0 not null,
	EXTCLASSGUID VARCHAR2(29 char) null,
	INTCLASSGUID VARCHAR2(29 char) null,
	SRCPID VARCHAR2(1000 char) null,
	SRCINHERITED NUMBER(1,0) default 0 not null,
	TYPE VARCHAR2(20 char) null,
	INTREF NUMBER(9,0) null,
	EXTREF VARCHAR2(1000 char) null,
	STATE VARCHAR2(20 char) null)
/

grant select, update, insert, delete on RDX_CM_ITEMREF to &USER&_RUN_ROLE
/

create table RDX_CM_PACKET(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(50 char) not null,
	TITLE VARCHAR2(200 char) null,
	NOTES VARCHAR2(4000 char) null,
	SRCDBURL VARCHAR2(1000 char) null,
	SRCPACKETID NUMBER(9,0) null,
	SRCAPPVER VARCHAR2(1000 char) null,
	SRCEXPUSER VARCHAR2(250 char) null,
	SRCEXPTIME DATE null,
	LASTMODIFYTIME DATE not null,
	LASTMODIFYUSER VARCHAR2(250 char) not null,
	ACCEPTTIME DATE null,
	ACCEPTUSER VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_CM_PACKET to &USER&_RUN_ROLE
/

create global temporary table RDX_DEF2DOMAIN(
	VERSIONNUM NUMBER(18,0) not null,
	DEFID VARCHAR2(100 char) not null,
	DOMAINID VARCHAR2(100 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_DEF2DOMAIN to &USER&_RUN_ROLE
/

create table RDX_DM_DATA(
	PACKETID NUMBER(9,0) not null,
	ENTITYGUID VARCHAR2(50 char) not null,
	OBJPID VARCHAR2(1000 char) not null,
	PROPGUID VARCHAR2(50 char) not null,
	PROPVAL CLOB null)
/

grant select, update, insert, delete on RDX_DM_DATA to &USER&_RUN_ROLE
/

create table RDX_DM_OUTLINK(
	PACKETID NUMBER(9,0) not null,
	EXTTABLEGUID VARCHAR2(50 char) not null,
	PARENTPID VARCHAR2(1000 char) not null)
/

grant select, update, insert, delete on RDX_DM_OUTLINK to &USER&_RUN_ROLE
/

create table RDX_DM_OUTLINKVERIFY(
	PACKETID NUMBER(9,0) not null,
	EXTTABLEGUID VARCHAR2(50 char) not null,
	PARENTPID VARCHAR2(1000 char) not null,
	PARENTPROPGUID VARCHAR2(50 char) not null,
	PARENTPROPVAL VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_DM_OUTLINKVERIFY to &USER&_RUN_ROLE
/

create table RDX_DM_PACKET(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(200 char) null,
	NOTES VARCHAR2(4000 char) null,
	STATE CHAR(1 char) not null,
	PREPARETIME DATE null,
	AUTHORNAME VARCHAR2(250 char) null,
	SRCDBURL VARCHAR2(100 char) null,
	SRCPACKETID NUMBER(9,0) null,
	DBSTRUCTUREVER VARCHAR2(200 char) null)
/

grant select, update, insert, delete on RDX_DM_PACKET to &USER&_RUN_ROLE
/

create table RDX_DM_SEGMENT(
	PACKETID NUMBER(9,0) not null,
	SEGMENTGUID VARCHAR2(50 char) not null,
	SELECTCONDITION CLOB null)
/

grant select, update, insert, delete on RDX_DM_SEGMENT to &USER&_RUN_ROLE
/

create table RDX_EASSELECTORADDONS(
	GUID VARCHAR2(50 char) not null,
	CLASSGUID VARCHAR2(50 char) not null,
	SEQ NUMBER(9,0) not null,
	ISACTIVE NUMBER(1,0) default 0 not null,
	TITLE VARCHAR2(100 char) not null,
	TABLEGUID VARCHAR2(50 char) not null,
	SELPRESENTATIONS VARCHAR2(4000 char) null,
	LASTUPDATEUSER VARCHAR2(250 char) not null,
	LASTUPDATETIME TIMESTAMP(6) not null,
	BASEFILTERGUID VARCHAR2(50 char) null,
	CONDITION CLOB null,
	PARAMETERS CLOB null)
/

grant select, update, insert, delete on RDX_EASSELECTORADDONS to &USER&_RUN_ROLE
/

create table RDX_EASSESSION(
	ID NUMBER(9,0) not null,
	USERNAME VARCHAR2(250 char) null,
	STATIONNAME VARCHAR2(100 char) null,
	LANGUAGE VARCHAR2(3 char) null,
	COUNTRY VARCHAR2(3 char) null,
	ENVIRONMENT VARCHAR2(100 char) null,
	SERVERKEY VARCHAR2(32 char) null,
	CLIENTKEY VARCHAR2(32 char) null,
	CHALLENGE VARCHAR2(100 char) not null,
	USERCERTIFICATE BLOB null,
	ISACTIVE NUMBER(1,0) default 0 not null,
	ISINTERACTIVE NUMBER(1,0) default 1 not null,
	LASTCONNECTTIME DATE not null,
	CREATIONTIME DATE default SYSDATE not null)
/

grant select, update, insert, delete on RDX_EASSESSION to &USER&_RUN_ROLE
/

create global temporary table RDX_ENTITYARRUSERREF(
	VERSIONNUM NUMBER(18,0) not null,
	TABLEID VARCHAR2(100 char) not null,
	USERPROPID VARCHAR2(100 char) not null,
	DELETEMODE VARCHAR2(100 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_ENTITYARRUSERREF to &USER&_RUN_ROLE
/

create global temporary table RDX_ENTITYUSERREF(
	VERSIONNUM NUMBER(18,0) not null,
	TABLEID VARCHAR2(100 char) not null,
	USERPROPID VARCHAR2(100 char) not null,
	DELETEMODE VARCHAR2(100 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_ENTITYUSERREF to &USER&_RUN_ROLE
/

create global temporary table RDX_ENUMITEM2DOMAIN(
	VERSIONNUM NUMBER(18,0) not null,
	ENUMID VARCHAR2(100 char) not null,
	ENUMITEMVALASSTR VARCHAR2(2000 char) not null,
	DOMAINID VARCHAR2(100 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_ENUMITEM2DOMAIN to &USER&_RUN_ROLE
/

create global temporary table RDX_EVENTCODE2EVENTPARAMS(
	VERSIONNUM NUMBER(18,0) not null,
	EVENTCODE VARCHAR2(100 char) not null,
	EVENTSEVERITY NUMBER(1,0) not null,
	EVENTSOURCE VARCHAR2(4000 char) not null)
	on commit preserve rows
/

grant select, update, insert, delete on RDX_EVENTCODE2EVENTPARAMS to &USER&_RUN_ROLE
/

create table RDX_EVENTCONTEXT(
	RAISETIME TIMESTAMP(6) not null,
	EVENTID NUMBER(18,0) not null,
	TYPE VARCHAR2(100 char) not null,
	ID VARCHAR2(250 char) not null)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
	partition by range (RAISETIME) INTERVAL(NUMTODSINTERVAL(1,'DAY'))
		    (
		      PARTITION RDX_EVENTCONTEXT_0 VALUES LESS THAN (to_date('&sysdate:yyyy_MM_dd&','YYYY_MM_DD'))
		    )
#ENDIF
/

grant select, update, insert, delete on RDX_EVENTCONTEXT to &USER&_RUN_ROLE
/

create table RDX_EVENTLOG(
	RAISETIME TIMESTAMP(6) not null,
	ID NUMBER(18,0) not null,
	CODE VARCHAR2(250 char) null,
	WORDS CLOB null,
	SEVERITY NUMBER(1,0) not null,
	COMPONENT VARCHAR2(1000 char) null,
	RECEIPTOR VARCHAR2(250 char) null,
	COMMENTARY VARCHAR2(1000 char) null,
	USERNAME VARCHAR2(250 char) null,
	STATIONNAME VARCHAR2(100 char) null,
	ISSENSITIVE NUMBER(1,0) default 0 not null)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
	partition by range (RAISETIME) INTERVAL(NUMTODSINTERVAL(1,'DAY'))
		    (
		      PARTITION RDX_EVENTLOG_0 VALUES LESS THAN (to_date('&sysdate:yyyy_MM_dd&','YYYY_MM_DD'))
		    )
#ENDIF
/

grant select, update, insert, delete on RDX_EVENTLOG to &USER&_RUN_ROLE
/

create table RDX_EVENTSEVERITY(
	EVENTCODE VARCHAR2(250 char) not null,
	EVENTSEVERITY NUMBER(1,0) not null)
/

grant select, update, insert, delete on RDX_EVENTSEVERITY to &USER&_RUN_ROLE
/

create table RDX_FALLBACKMQHANDLER(
	UNITID NUMBER(9,0) not null,
	MAINUNITID NUMBER(9,0) not null)
/

grant select, update, insert, delete on RDX_FALLBACKMQHANDLER to &USER&_RUN_ROLE
/

create table RDX_INSTANCE(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(200 char) null,
	STARTED NUMBER(1,0) default 0 not null,
	ARTEINSTCOUNT NUMBER(9,0) default 0 not null,
	AVGACTIVEARTECOUNT NUMBER default 0 not null,
	DBTRACEPROFILE VARCHAR2(1000 char) default 'Event' not null,
	GUITRACEPROFILE VARCHAR2(1000 char) default 'Event' not null,
	FILETRACEPROFILE VARCHAR2(1000 char) default 'Event' not null,
	TRACEFILESDIR VARCHAR2(250 char) default './logs' not null,
	MAXTRACEFILESIZEKB NUMBER(9,0) default 1024 not null,
	MAXTRACEFILECNT NUMBER(4,0) default 10 not null,
	ROTATETRACEFILESDAILY NUMBER(1,0) default 0 not null,
	STARTOSCOMMAND VARCHAR2(1000 char) null,
	STOPOSCOMMAND VARCHAR2(1000 char) null,
	SELFCHECKTIME TIMESTAMP(6) null,
	SAPID NUMBER(9,0) not null,
	JDWPADDRESS VARCHAR2(1000 char) null,
	SYSTEMID NUMBER(9,0) default 1 null,
	SCPNAME VARCHAR2(100 char) null,
	KEYSTORETYPE NUMBER(1,0) default 0 not null,
	KEYSTOREPATH VARCHAR2(256 char) default 'radixware.org/server/keys.jceks' null,
	KEYTABPATH VARCHAR2(256 char) null,
	AUTOACTUALIZEVER NUMBER(1,0) default 1 not null,
	MEMORYCHECKPERIODSEC NUMBER(9,0) null,
	LOWARTEINSTCOUNT NUMBER(9,0) default 2 not null,
	HIGHARTEINSTCOUNT NUMBER(9,0) default 10 not null,
	ARTECNTABOVENORMAL NUMBER(9,0) null,
	ARTECNTHIGH NUMBER(9,0) null,
	ARTECNTVERYHIGH NUMBER(9,0) null,
	ARTECNTCRITICAL NUMBER(9,0) null,
	ARTEINSTLIVETIME NUMBER(9,0) null,
	HTTPPROXY VARCHAR2(500 char) null,
	HTTPSPROXY VARCHAR2(500 char) null,
	TARGETEXECUTORID NUMBER(9,0) null,
	USEORAIMPLSTMTCACHE NUMBER(1,0) null,
	ORAIMPLSTMTCACHESIZE NUMBER(9,0) null,
	USEACTIVEARTELIMITS NUMBER(1,0) default 0 not null,
	MAXACTIVEARTENORMAL NUMBER(9,0) null,
	MAXACTIVEARTEABOVENORMAL NUMBER(9,0) null,
	MAXACTIVEARTEHIGH NUMBER(9,0) null,
	MAXACTIVEARTEVERYHIGH NUMBER(9,0) null,
	MAXACTIVEARTECRITICAL NUMBER(9,0) null,
	CLASSLOADINGPROFILEID NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_INSTANCE to &USER&_RUN_ROLE
/

create table RDX_JMSHANDLER(
	ID NUMBER(9,0) not null,
	SAPID NUMBER(9,0) not null,
	ISCLIENT NUMBER(1,0) default 1 not null,
	JMSMESSFORMAT NUMBER(9,0) default 2 not null,
	JMSCONNECTPROPS CLOB null,
	JMSMESSPROPS CLOB null,
	JMSLOGIN VARCHAR2(100 char) null,
	JMSPASSWORD VARCHAR2(100 char) null,
	MSRQQUEUENAME VARCHAR2(150 char) not null,
	MSRSQUEUENAME VARCHAR2(150 char) null,
	INSEANCECNT NUMBER(9,0) default 30 null,
	OUTSEANCECNT NUMBER(9,0) default 30 null,
	RSTIMEOUT NUMBER(18,0) null)
/

grant select, update, insert, delete on RDX_JMSHANDLER to &USER&_RUN_ROLE
/

create table RDX_JS_CALENDAR(
	ID NUMBER(9,0) not null,
	GUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) null,
	NOTES VARCHAR2(4000 char) null,
	LASTUPDATETIME TIMESTAMP(6) not null,
	LASTUPDATEUSER VARCHAR2(250 char) null,
	CLASSGUID VARCHAR2(29 char) not null)
/

grant select, update, insert, delete on RDX_JS_CALENDAR to &USER&_RUN_ROLE
/

create table RDX_JS_CALENDARITEM(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(29 char) not null,
	CALENDARID NUMBER(9,0) not null,
	SEQ NUMBER(9,0) not null,
	OPER CHAR(1 char) default '+' not null,
	OFFSET NUMBER(5,0) null,
	OFFSETDIR NUMBER(1,0) null,
	INCCALENDARID NUMBER(9,0) null,
	ABSDATE DATE null)
/

grant select, update, insert, delete on RDX_JS_CALENDARITEM to &USER&_RUN_ROLE
/

create table RDX_JS_EVENTSCHD(
	ID NUMBER(9,0) not null,
	GUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) null,
	NOTES VARCHAR2(4000 char) null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSER VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_JS_EVENTSCHD to &USER&_RUN_ROLE
/

create table RDX_JS_EVENTSCHDITEM(
	SCHEDULEID NUMBER(9,0) not null,
	ID NUMBER(9,0) not null,
	CALENDARID NUMBER(9,0) null,
	REPEATABLE NUMBER(1,0) default 0 not null,
	STARTTIME NUMBER(9,0) default 0 not null,
	ENDTIME NUMBER(9,0) default 1439 not null,
	PERIOD NUMBER(9,0) default 1 not null,
	EVENTTIME NUMBER(9,0) default 0 not null,
	TIMEZONEREGION VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_JS_EVENTSCHDITEM to &USER&_RUN_ROLE
/

create table RDX_JS_INTERVALSCHD(
	ID NUMBER(9,0) not null,
	GUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) null,
	NOTES VARCHAR2(4000 char) null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSER VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_JS_INTERVALSCHD to &USER&_RUN_ROLE
/

create table RDX_JS_INTERVALSCHDITEM(
	SCHEDULEID NUMBER(9,0) not null,
	ID NUMBER(9,0) not null,
	CALENDARID NUMBER(9,0) null,
	STARTTIME NUMBER(9,0) default 0 not null,
	ENDTIME NUMBER(9,0) default 1439 not null,
	TIMEZONEREGION VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_JS_INTERVALSCHDITEM to &USER&_RUN_ROLE
/

create table RDX_JS_JOBEXECUTORPRIORITYMAP(
	APPPRIORITY NUMBER(1,0) not null,
	SYSPRIORITY NUMBER(1,0) not null)
/

grant select, update, insert, delete on RDX_JS_JOBEXECUTORPRIORITYMAP to &USER&_RUN_ROLE
/

create table RDX_JS_JOBEXECUTORUNIT(
	ID NUMBER(9,0) not null,
	AVGEXECCOUNT NUMBER default 0 not null,
	AVGWAITCOUNT NUMBER default 0 not null,
	PARALLELCNT NUMBER(9,0) default 50 not null,
	ABOVENORMALDELTA NUMBER(9,0) default 10 not null,
	HIGHDELTA NUMBER(9,0) default 10 not null,
	VERYHIGHDELTA NUMBER(9,0) default 10 not null,
	CRITICALDELTA NUMBER(9,0) default 10 not null,
	EXECPERIOD NUMBER(9,3) default 0.1 not null)
/

grant select, update, insert, delete on RDX_JS_JOBEXECUTORUNIT to &USER&_RUN_ROLE
/

create table RDX_JS_JOBEXECUTORUNITBOOST(
	SPEED NUMBER(1,0) not null,
	DELAY NUMBER(9,0) not null,
	MAXINCREMENT NUMBER(9,0) not null)
/

grant select, update, insert, delete on RDX_JS_JOBEXECUTORUNITBOOST to &USER&_RUN_ROLE
/

create table RDX_JS_JOBPARAM(
	JOBID NUMBER(18,0) not null,
	NAME VARCHAR2(100 char) not null,
	SEQ NUMBER(9,0) not null,
	VALTYPE NUMBER(9,0) null,
	VAL VARCHAR2(4000 char) null,
	CLOBVAL CLOB null,
	BLOBVAL BLOB null)
/

grant select, update, insert, delete on RDX_JS_JOBPARAM to &USER&_RUN_ROLE
/

create table RDX_JS_JOBQUEUE(
	ID NUMBER(18,0) not null,
	TASKID NUMBER(9,0) null,
	DUETIME TIMESTAMP(6) null,
	CLASSNAME VARCHAR2(1000 char) not null,
	METHODNAME VARCHAR2(1000 char) not null,
	SCPNAME VARCHAR2(100 char) null,
	PRIORITY NUMBER(9,0) not null,
	PRIORITYBOOSTINGSPEED NUMBER(1,0) default 0 not null,
	CURPRIORITY NUMBER(9,0) default 0 not null,
	CREATORENTITYGUID VARCHAR2(100 char) null,
	CREATORPID VARCHAR2(250 char) null,
	EXECREQUESTERID VARCHAR2(250 char) null,
	TITLE VARCHAR2(4000 char) null,
	EXECUTORID NUMBER(9,0) null,
	PROCESSORTITLE VARCHAR2(500 char) null,
	UNLOCKCOUNT NUMBER(9,0) default 0 not null,
	SELFCHECKTIME DATE null,
	ALLOWRERUN NUMBER(1,0) default 1 not null)
/

grant select, update, insert, delete on RDX_JS_JOBQUEUE to &USER&_RUN_ROLE
/

create table RDX_JS_JOBSCHEDULERUNIT(
	ID NUMBER(9,0) not null,
	PARENTID NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_JS_JOBSCHEDULERUNIT to &USER&_RUN_ROLE
/

create table RDX_JS_TASK(
	ID NUMBER(9,0) not null,
	CREATETIME DATE default SYSDATE not null,
	SEQ NUMBER(9,0) default 0 not null,
	PARENTID NUMBER(9,0) null,
	TITLE VARCHAR2(250 char) null,
	UNITID NUMBER(9,0) null,
	SCHEDULEID NUMBER(9,0) null,
	CLASSGUID VARCHAR2(29 char) not null,
	EXECROLEGUIDS VARCHAR2(4000 char) null,
	PRIORITY NUMBER(9,0) default 5 not null,
	PRIORITYBOOSTINGSPEED NUMBER(1,0) default 0 not null,
	EXPIREDPOLICY NUMBER(1,0) default 0 not null,
	SCPNAME VARCHAR2(100 char) null,
	SYSTEMID NUMBER(9,0) null,
	STARTEXECTIME TIMESTAMP(6) null,
	FINISHEXECTIME TIMESTAMP(6) null,
	LASTEXECTIME DATE null,
	PREVEXECTIME DATE null,
	FAULTMESS CLOB null,
	EXECRESULTS CLOB null,
	EXECTOKEN NUMBER(18,0) null,
	FINISHEDCHILDSCOUNT NUMBER(9,0) null,
	STATUS VARCHAR2(100 char) null,
	SELFCHECKTIME DATE default SYSDATE not null,
	ISACTUALIZABLEBYRELATEDJOBS NUMBER(1,0) default 0 not null,
	ISACTIVE NUMBER(1,0) default 1 not null,
	SKIPIFEXECUTING NUMBER(1,0) default 0 not null,
	EXPECTEDDURATIONSEC NUMBER(9,0) null,
	LASTSCHEDULINGTIME DATE null)
/

grant select, update, insert, delete on RDX_JS_TASK to &USER&_RUN_ROLE
/

create table RDX_KAFKAQUEUE(
	QUEUEID NUMBER(9,0) not null,
	SESSIONTIMEOUTSEC NUMBER(9,0) default 30 not null)
/

grant select, update, insert, delete on RDX_KAFKAQUEUE to &USER&_RUN_ROLE
/

create table RDX_LIBUSERFUNC(
	GUID VARCHAR2(300 char) not null,
	LIBNAME VARCHAR2(1000 char) not null,
	FUNCNAME VARCHAR2(300 char) null,
	DESCRIPTION VARCHAR2(4000 char) null,
	PROFILE VARCHAR2(400 char) null)
/

grant select, update, insert, delete on RDX_LIBUSERFUNC to &USER&_RUN_ROLE
/

create table RDX_LICENSEREPORTLOG(
	ID NUMBER(9,0) not null,
	CREATEDATE DATE default SYSDATE not null,
	REPORTXML CLOB not null,
	REPORTHTML CLOB null,
	SENT NUMBER(1,0) default 0 not null)
/

grant select, update, insert, delete on RDX_LICENSEREPORTLOG to &USER&_RUN_ROLE
/

create table RDX_MESSAGEQUEUE(
	ID NUMBER(9,0) not null,
	QUEUEKIND VARCHAR2(100 char) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) null,
	BROKERADDRESS VARCHAR2(200 char) not null,
	QUEUENAME VARCHAR2(200 char) not null,
	PARTITIONNAME VARCHAR2(200 char) null,
	CONSUMERKEY VARCHAR2(200 char) null,
	LOGIN VARCHAR2(200 char) null,
	PASSWORD VARCHAR2(200 char) null)
/

grant select, update, insert, delete on RDX_MESSAGEQUEUE to &USER&_RUN_ROLE
/

create table RDX_MESSAGEQUEUEPROCESSOR(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) null,
	QUEUEID NUMBER(9,0) not null,
	UNITID NUMBER(9,0) not null,
	PARALLELTHREADS NUMBER(9,0) not null,
	AASCALLTIMEOUTSEC NUMBER(9,0) default 120 not null,
	DBTRACEPROFILE VARCHAR2(200 char) null)
/

grant select, update, insert, delete on RDX_MESSAGEQUEUEPROCESSOR to &USER&_RUN_ROLE
/

create table RDX_MSDLSCHEME(
	GUID VARCHAR2(100 char) not null,
	NAME VARCHAR2(100 char) null,
	SOURCE CLOB null,
	RUNTIME BLOB null,
	SCHEMECLASSGUID VARCHAR2(100 char) null,
	LASTUPDATETIME DATE null,
	LASTUPDATEUSERNAME VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_MSDLSCHEME to &USER&_RUN_ROLE
/

create table RDX_NETCHANNEL(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(100 char) null,
	UNITID NUMBER(9,0) not null,
	ADDRESS VARCHAR2(200 char) not null,
	LINKLEVELPROTOCOLKIND NUMBER(9,0) default 0 not null,
	REQUESTFRAME VARCHAR2(200 char) not null,
	RESPONSEFRAME VARCHAR2(200 char) not null,
	RECVTIMEOUT NUMBER(9,0) default 30 not null,
	SENDTIMEOUT NUMBER(9,0) default 30 not null,
	KEEPCONNECTTIMEOUT NUMBER(9,0) null,
	CLASSGUID VARCHAR2(100 char) not null,
	MAXSESSIONCOUNT NUMBER(9,0) default 1 null,
	CURSESSIONCOUNT NUMBER(9,0) default 0 not null,
	GUITRACEPROFILE VARCHAR2(4000 char) default 'Event' not null,
	FILETRACEPROFILE VARCHAR2(4000 char) default 'Event' not null,
	DBTRACEPROFILE VARCHAR2(4000 char) default 'Event' not null,
	USE NUMBER(1,0) default 1 not null,
	ISLISTENER NUMBER(1,0) default 1 not null,
	ISCONNECTREADYNTFON NUMBER(1,0) default 1 not null,
	ISDISCONNECTNTFON NUMBER(1,0) default 1 not null,
	SECURITYPROTOCOL NUMBER(9,0) default 0 not null,
	CHECKCLIENTCERT NUMBER(1,0) default 0 not null,
	SERVERKEYALIASES VARCHAR2(4000 char) null,
	CLIENTCERTALIASES VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_NETCHANNEL to &USER&_RUN_ROLE
/

create table RDX_NETHUB(
	ID NUMBER(9,0) not null,
	SAPID NUMBER(9,0) not null,
	INSEANCECNT NUMBER(9,0) default 3 not null,
	OUTSEANCECNT NUMBER(9,0) default 3 not null,
	ECHOTESTPERIOD NUMBER(9,0) null,
	RECONNECTNOECHOCNT NUMBER(9,0) null,
	EXTPORTISSERVER NUMBER(1,0) not null,
	EXTPORTADDRESS VARCHAR2(150 char) not null,
	EXTPORTFRAME VARCHAR2(200 char) not null,
	OUTTIMEOUT NUMBER(9,0) default 30 not null,
	CONNECTED NUMBER(1,0) default 0 null,
	CUROUTSESSIONCNT NUMBER(9,0) null,
	LASTOUTSTAN NUMBER(9,0) null,
	LASTINUNIQUEKEY VARCHAR2(100 char) null,
	TOPROCESSSTART NUMBER(1,0) default 1 null,
	TOPROCESSSTOP NUMBER(1,0) default 1 null,
	TOPROCESSCONNECT NUMBER(1,0) default 1 null,
	TOPROCESSDISCONNECT NUMBER(1,0) default 1 null,
	TOPROCESSDUPLICATEDRQ NUMBER(1,0) default 1 null,
	TOPROCESSUNCORRELATEDRS NUMBER(1,0) default 1 null)
/

grant select, update, insert, delete on RDX_NETHUB to &USER&_RUN_ROLE
/

create table RDX_NETPORTHANDLER(
	ID NUMBER(9,0) not null,
	SAPID NUMBER(9,0) not null,
	MAXAASSEANCESCOUNT NUMBER(9,0) default 10 not null)
/

grant select, update, insert, delete on RDX_NETPORTHANDLER to &USER&_RUN_ROLE
/

create table RDX_PC_ATTACHMENT(
	MESSID NUMBER(18,0) not null,
	SEQ NUMBER(9,0) not null,
	TITLE VARCHAR2(200 char) null,
	MIMETYPE VARCHAR2(250 char) not null,
	DATA BLOB not null)
/

grant select, update, insert, delete on RDX_PC_ATTACHMENT to &USER&_RUN_ROLE
/

create table RDX_PC_CHANNELGSMMODEM(
	CHANNELUNITID NUMBER(9,0) not null,
	ID NUMBER(9,0) not null,
	COMPORT NUMBER(9,0) not null,
	SPEED NUMBER(9,0) not null)
/

grant select, update, insert, delete on RDX_PC_CHANNELGSMMODEM to &USER&_RUN_ROLE
/

create table RDX_PC_CHANNELHANDLER(
	UNITID NUMBER(9,0) not null,
	SEQ NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(29 char) not null,
	TITLE VARCHAR2(250 char) null,
	SUBJECTREGEXP VARCHAR2(500 char) default '.*' not null,
	BODYREGEXP VARCHAR2(500 char) default '.*' not null)
/

grant select, update, insert, delete on RDX_PC_CHANNELHANDLER to &USER&_RUN_ROLE
/

create table RDX_PC_CHANNELUNIT(
	ID NUMBER(9,0) not null,
	KIND VARCHAR2(20 char) not null,
	MINIMPORTANCE NUMBER(9,0) default 0 not null,
	MAXIMPORTANCE NUMBER(9,0) default 2 not null,
	SENDPERIOD NUMBER(9,0) default 10 null,
	RECVPERIOD NUMBER(9,0) default 10 null,
	SENDADDRESS VARCHAR2(500 char) not null,
	POP3ADDRESS VARCHAR2(500 char) null,
	RECVADDRESS VARCHAR2(500 char) not null,
	MESSADDRESSREGEXP VARCHAR2(500 char) default '.*' not null,
	ROUTINGPRIORITY NUMBER(2,0) default 1 not null,
	ENCODING VARCHAR2(100 char) null,
	SMPPSYSTEMID VARCHAR2(100 char) null,
	SMPPSYSTEMTYPE VARCHAR2(100 char) null,
	SMPPSESSIONTYPE NUMBER(9,0) default 0 null,
	SMPPSESSIONTON NUMBER(9,0) null,
	SMPPSESSIONNPI NUMBER(9,0) null,
	SMPPPASSWORD VARCHAR2(100 char) null,
	SMPPSOURCEADDRESS VARCHAR2(100 char) null,
	SMPPSOURCEADDRESSTON NUMBER(9,0) null,
	SMPPSOURCEADDRESSNPI NUMBER(9,0) null,
	SMPPDESTINATIONTON NUMBER(9,0) null,
	SMPPDESTINATIONNPI NUMBER(9,0) null,
	SMPPINTERFACE NUMBER(9,0) null,
	SMPPENCODING VARCHAR2(64 char) null,
	SMPPMAXLEN NUMBER(9,0) null,
	EMAILLOGIN VARCHAR2(100 char) null,
	EMAILPASSWORD VARCHAR2(100 char) null,
	EMAILSECURECONNECTION VARCHAR2(100 char) null,
	ADDRESSTEMPLATE VARCHAR2(100 char) null,
	SUBJECTTEMPLATE VARCHAR2(100 char) null,
	FILEFORMAT VARCHAR2(9 char) null,
	TWITTERCONSUMERTOKEN VARCHAR2(32 char) null,
	TWITTERCONSUMERSECRET VARCHAR2(50 char) null,
	TWITTERACCESSTOKEN VARCHAR2(100 char) null,
	TWITTERACCESSSECRET VARCHAR2(100 char) null,
	SENDTIMEOUT NUMBER(9,0) null,
	APNSKEYALIAS VARCHAR2(200 char) null,
	APNSMAXPARALLELSENDCOUNT NUMBER(9,0) null,
	APNSSUCCESSFULAFTERMILLIS NUMBER(9,0) null,
	GCMAPIKEY VARCHAR2(600 char) null)
/

grant select, update, insert, delete on RDX_PC_CHANNELUNIT to &USER&_RUN_ROLE
/

create table RDX_PC_EVENTLIMITACC(
	SUBSCRIPTIONID NUMBER(9,0) not null,
	USERNAME VARCHAR2(250 char) not null,
	EVENTCODE VARCHAR2(100 char) not null,
	CHANNELKIND VARCHAR2(100 char) not null,
	STARTTIME DATE not null,
	CNT NUMBER(9,0) not null)
/

grant select, update, insert, delete on RDX_PC_EVENTLIMITACC to &USER&_RUN_ROLE
/

create table RDX_PC_EVENTSUBSCRIPTION(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(250 char) null,
	ISACTIVE NUMBER(1,0) default 1 not null,
	USERGROUPNAME VARCHAR2(250 char) not null,
	CHANNELKIND VARCHAR2(100 char) not null,
	EVENTSOURCE VARCHAR2(100 char) null,
	MINEVENTSEVERITY NUMBER(9,0) default 3 not null,
	SUBJECTTEMPLATE VARCHAR2(100 char) null,
	BODYTEMPLATE VARCHAR2(1000 char) null,
	LANGUAGE VARCHAR2(2 char) null,
	LOWIMPORTANCEMAXSEV NUMBER(9,0) default 1 not null,
	NORMALIMPORTANCEMAXSEV NUMBER(9,0) default 2 not null,
	TOSTOREINHIST NUMBER(1,0) default 0 not null,
	LIMITPERIODKIND VARCHAR2(100 char) null,
	LIMITCNT NUMBER(9,0) null,
	LIMITMESSSUBJECTTEMPLATE VARCHAR2(100 char) null,
	LIMITMESSBODYTEMPLATE VARCHAR2(1000 char) null)
/

grant select, update, insert, delete on RDX_PC_EVENTSUBSCRIPTION to &USER&_RUN_ROLE
/

create table RDX_PC_EVENTSUBSCRIPTIONCODE(
	SUBSCRIPTIONID NUMBER(9,0) not null,
	CODE VARCHAR2(100 char) not null)
/

grant select, update, insert, delete on RDX_PC_EVENTSUBSCRIPTIONCODE to &USER&_RUN_ROLE
/

create table RDX_PC_OUTMESSAGE(
	ID NUMBER(9,0) not null,
	SUBJECT VARCHAR2(200 char) null,
	BODY CLOB null,
	SMPPENCODING VARCHAR2(64 char) null,
	IMPORTANCE NUMBER(9,0) default 1 not null,
	CREATETIME TIMESTAMP(6) not null,
	DUETIME TIMESTAMP(6) null,
	EXPIRETIME TIMESTAMP(6) null,
	HISTMODE NUMBER(9,0) not null,
	CHANNELKIND VARCHAR2(100 char) not null,
	ADDRESS VARCHAR2(1000 char) not null,
	ADDRESSFROM VARCHAR2(1000 char) null,
	SOURCEENTITYGUID VARCHAR2(250 char) not null,
	SOURCEPID VARCHAR2(100 char) not null,
	SOURCEMSGID VARCHAR2(100 char) null,
	DESTENTITYGUID VARCHAR2(100 char) null,
	DESTPID VARCHAR2(100 char) null,
	CALLBACKCLASSNAME VARCHAR2(1000 char) null,
	CALLBACKMETHODNAME VARCHAR2(1000 char) null,
	INPROCESS NUMBER(1,0) default 0 not null,
	CHANNELID NUMBER(9,0) not null,
	SENTTIME TIMESTAMP(6) null,
	FAILEDMESSAGE VARCHAR2(1000 char) null,
	FAILEDTRYCOUNT NUMBER(9,0) default 0 not null,
	FAILEDLASTSENDDATE DATE null,
	FAILEDISUNRECOVERABLE NUMBER(1,0) default 0 not null)
/

grant select, update, insert, delete on RDX_PC_OUTMESSAGE to &USER&_RUN_ROLE
/

create table RDX_PC_RECVMESSAGE(
	ID NUMBER(9,0) not null,
	IMPORTANCE NUMBER(9,0) default 1 not null,
	SUBJECT VARCHAR2(200 char) null,
	BODY CLOB null,
	SENDTIME TIMESTAMP(6) null,
	RECVTIME TIMESTAMP(6) not null,
	CHANNELID NUMBER(9,0) not null,
	ADDRESS VARCHAR2(1000 char) not null,
	DESTENTITYGUID VARCHAR2(100 char) null,
	DESTPID VARCHAR2(100 char) null,
	DESTMSGID VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_PC_RECVMESSAGE to &USER&_RUN_ROLE
/

create table RDX_PC_SENTMESSAGE(
	ID NUMBER(9,0) not null,
	SUBJECT VARCHAR2(200 char) null,
	BODY CLOB null,
	SMPPENCODING VARCHAR2(64 char) null,
	IMPORTANCE NUMBER(9,0) default 1 not null,
	CREATETIME TIMESTAMP(6) not null,
	DUETIME TIMESTAMP(6) null,
	EXPIRETIME TIMESTAMP(6) null,
	HISTMODE NUMBER(9,0) not null,
	CHANNELKIND VARCHAR2(100 char) null,
	ADDRESS VARCHAR2(1000 char) null,
	SOURCEENTITYGUID VARCHAR2(250 char) not null,
	SOURCEPID VARCHAR2(100 char) not null,
	SOURCEMSGID VARCHAR2(100 char) null,
	DESTENTITYGUID VARCHAR2(100 char) null,
	DESTPID VARCHAR2(100 char) null,
	CALLBACKCLASSNAME VARCHAR2(1000 char) null,
	CALLBACKMETHODNAME VARCHAR2(1000 char) null,
	CHANNELID NUMBER(9,0) null,
	SENTTIME TIMESTAMP(6) null,
	SENDERROR VARCHAR2(4000 char) null,
	SMPPBYTESSENT NUMBER(9,0) null,
	SMPPCHARSSENT NUMBER(9,0) null,
	SMPPPARTSSENT NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_PC_SENTMESSAGE to &USER&_RUN_ROLE
/

create table RDX_PROFILERLOG(
	ID NUMBER(12,0) not null,
	INSTANCEID NUMBER(9,0) not null,
	SECTIONID VARCHAR2(250 char) not null,
	PERIODENDTIME TIMESTAMP(6) not null,
	CONTEXT VARCHAR2(2000 char) null,
	DURATION NUMBER(18,0) not null,
	MINDURATION NUMBER(18,0) default 0 not null,
	MAXDURATION NUMBER(18,0) default 0 not null,
	COUNT NUMBER(18,0) not null)
/

grant select, update, insert, delete on RDX_PROFILERLOG to &USER&_RUN_ROLE
/

create table RDX_REPORTPARAM(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(50 char) not null,
	PARENTID NUMBER(9,0) null,
	LASTUPDATETIME TIMESTAMP(6) not null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) not null)
/

grant select, update, insert, delete on RDX_REPORTPARAM to &USER&_RUN_ROLE
/

create table RDX_REPORTPUB(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(50 char) not null,
	LISTID NUMBER(9,0) not null,
	PARENTTOPICID NUMBER(9,0) null,
	SEQ NUMBER(9,0) not null,
	ISENABLED NUMBER(1,0) default 1 not null,
	INHERITEDPUBID NUMBER(9,0) null,
	TITLE VARCHAR2(2000 char) null,
	DESCRIPTION VARCHAR2(4000 char) null,
	REPORTCLASSGUID VARCHAR2(50 char) null,
	USERROLEGUIDS CLOB null,
	ISFORCUSTOMER NUMBER(1,0) default 0 not null,
	PARAMBINDING CLOB null,
	FORMAT VARCHAR2(50 char) null,
	PRINTONLY NUMBER(1,0) default 0 not null)
/

grant select, update, insert, delete on RDX_REPORTPUB to &USER&_RUN_ROLE
/

create table RDX_REPORTPUBLIST(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(50 char) not null,
	PUBCONTEXTCLASSGUID VARCHAR2(50 char) not null,
	CONTEXTPID VARCHAR2(100 char) null,
	LASTUPDATETIME TIMESTAMP(6) not null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) not null)
/

grant select, update, insert, delete on RDX_REPORTPUBLIST to &USER&_RUN_ROLE
/

create table RDX_REPORTPUBTOPIC(
	ID NUMBER(9,0) not null,
	LISTID NUMBER(9,0) not null,
	PARENTTOPICID NUMBER(9,0) null,
	SEQ NUMBER(9,0) not null,
	INHERITEDTOPICID NUMBER(9,0) null,
	TITLE VARCHAR2(200 char) not null,
	DESCRIPTION VARCHAR2(2000 char) null)
/

grant select, update, insert, delete on RDX_REPORTPUBTOPIC to &USER&_RUN_ROLE
/

create table RDX_SAP(
	ID NUMBER(9,0) not null,
	NAME VARCHAR2(200 char) not null,
	NOTES VARCHAR2(4000 char) null,
	SYSTEMID NUMBER(9,0) default 1 not null,
	SYSTEMINSTANCEID NUMBER(9,0) null,
	SYSTEMUNITID NUMBER(9,0) null,
	URI VARCHAR2(300 char) not null,
	ISACTIVE NUMBER(1,0) default 0 not null,
	ADDRESS VARCHAR2(4000 char) not null,
	CHANNELTYPE VARCHAR2(100 char) default 'TCP' not null,
	SECURITYPROTOCOL NUMBER(9,0) default 0 not null,
	CHECKCLIENTCERT NUMBER(1,0) default 0 not null,
	EASKRBAUTH NUMBER(1,0) default 0 not null,
	SELFCHECKTIME TIMESTAMP(6) null,
	ACCESSIBILITY CHAR(1 char) not null,
	SERVERKEYALIASES VARCHAR2(4000 char) null,
	SERVERCERTALIASES VARCHAR2(4000 char) null,
	CLIENTKEYALIASES VARCHAR2(4000 char) null,
	CLIENTCERTALIASES VARCHAR2(4000 char) null,
	CIPHERSUITES VARCHAR2(4000 char) null,
	SERVERATTRS VARCHAR2(4000 char) null,
	CLIENTATTRS VARCHAR2(4000 char) null,
	USEWSSECURITY NUMBER(1,0) default 0 not null,
	SERVICEWSDL CLOB null,
	SERVICEQNAME VARCHAR2(500 char) null,
	PORTQNAME VARCHAR2(500 char) null,
	SERVICELASTUPDATETIME TIMESTAMP(6) default SYSTIMESTAMP not null)
/

grant select, update, insert, delete on RDX_SAP to &USER&_RUN_ROLE
/

create table RDX_SB_DATASCHEME(
	URI VARCHAR2(1000 char) not null,
	TYPE VARCHAR2(30 char) not null,
	TITLE VARCHAR2(1000 char) null,
	DESCRIPTION VARCHAR2(4000 char) null,
	SCHEME CLOB null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_SB_DATASCHEME to &USER&_RUN_ROLE
/

create table RDX_SB_PIPELINE(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(1000 char) null,
	DESCRIPTION VARCHAR2(4000 char) null,
	DIAGRAMM CLOB null,
	CHECKED NUMBER(1,0) default 0 null,
	TRACEPROFILE VARCHAR2(4000 char) null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null,
	EXTGUID VARCHAR2(100 char) not null)
/

grant select, update, insert, delete on RDX_SB_PIPELINE to &USER&_RUN_ROLE
/

create table RDX_SB_PIPELINECONNECTOR(
	NODEID NUMBER(9,0) not null,
	ROLE VARCHAR2(200 char) not null,
	TITLE VARCHAR2(1000 char) null,
	RQTYPE VARCHAR2(1000 char) not null,
	RSTYPE VARCHAR2(1000 char) not null)
/

grant select, update, insert, delete on RDX_SB_PIPELINECONNECTOR to &USER&_RUN_ROLE
/

create table RDX_SB_PIPELINENODE(
	ID NUMBER(9,0) not null,
	EXTGUID VARCHAR2(50 char) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	PIPELINEENTITYGUID VARCHAR2(100 char) not null,
	PIPELINEENTITYPID VARCHAR2(1000 char) not null,
	TITLE VARCHAR2(1000 char) null,
	DESCRIPTION VARCHAR2(4000 char) null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null)
/

grant select, update, insert, delete on RDX_SB_PIPELINENODE to &USER&_RUN_ROLE
/

create table RDX_SB_PIPELINENODEPARAM(
	NODEID NUMBER(9,0) not null,
	NAME VARCHAR2(200 char) not null,
	VAL VARCHAR2(1000 char) null)
/

grant select, update, insert, delete on RDX_SB_PIPELINENODEPARAM to &USER&_RUN_ROLE
/

create table RDX_SB_PIPELINEPARAM(
	PIPELINEID NUMBER(9,0) not null,
	NAME VARCHAR2(200 char) not null,
	VAL VARCHAR2(1000 char) null)
/

grant select, update, insert, delete on RDX_SB_PIPELINEPARAM to &USER&_RUN_ROLE
/

create table RDX_SB_PIPELINEVER(
	PIPELINEID NUMBER(9,0) not null,
	CREATETIME DATE not null,
	CREATORUSERNAME VARCHAR2(250 char) null,
	NOTES VARCHAR2(4000 char) null,
	SETTINGS CLOB null)
/

grant select, update, insert, delete on RDX_SB_PIPELINEVER to &USER&_RUN_ROLE
/

create table RDX_SB_TRANSFORMSTAGE(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	NODEID NUMBER(9,0) not null,
	SEQ NUMBER(9,0) not null,
	TITLE VARCHAR2(1000 char) null)
/

grant select, update, insert, delete on RDX_SB_TRANSFORMSTAGE to &USER&_RUN_ROLE
/

create table RDX_SB_XPATHTABLE(
	ID NUMBER(9,0) not null,
	STAGEID NUMBER(9,0) not null,
	SEQ NUMBER(9,0) not null,
	TITLE VARCHAR2(1000 char) null,
	SRCXPATH VARCHAR2(4000 char) null,
	DSTXPATH VARCHAR2(4000 char) null,
	ISREQUEST NUMBER(1,0) not null)
/

grant select, update, insert, delete on RDX_SB_XPATHTABLE to &USER&_RUN_ROLE
/

create table RDX_SCP(
	NAME VARCHAR2(100 char) not null,
	SYSTEMID NUMBER(9,0) not null,
	NOTES VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_SCP to &USER&_RUN_ROLE
/

create table RDX_SCP2SAP(
	SYSTEMID NUMBER(9,0) not null,
	SAPID NUMBER(9,0) not null,
	SCPNAME VARCHAR2(100 char) not null,
	SAPPRIORITY NUMBER(2,0) default 50 not null,
	CONNECTTIMEOUT NUMBER(9,0) default 10 not null,
	BLOCKINGPERIOD NUMBER(9,0) default 600 not null)
/

grant select, update, insert, delete on RDX_SCP2SAP to &USER&_RUN_ROLE
/

create table RDX_SERVICE(
	SYSTEMID NUMBER(9,0) not null,
	URI VARCHAR2(300 char) not null,
	WSDLURI VARCHAR2(300 char) not null,
	TITLE VARCHAR2(200 char) null,
	IMPLEMENTEDINARTE NUMBER(1,0) null,
	ACCESSIBILITY CHAR(1 char) null)
/

grant select, update, insert, delete on RDX_SERVICE to &USER&_RUN_ROLE
/

create table RDX_SM_DASHCONFIG(
	GUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(300 char) not null,
	XMLCONTENT CLOB not null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSER VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_SM_DASHCONFIG to &USER&_RUN_ROLE
/

create table RDX_SM_METRICCONTROLDATA(
	STATEID NUMBER(9,0) not null,
	LASTPROCESSEDHISTID NUMBER(9,0) null,
	LASTWARNVAL NUMBER(12,3) null,
	LASTERRORVAL NUMBER(12,3) null,
	LASTCONTROLSEVERITY NUMBER(1,0) null,
	LASTCONTROLACTIONMASK NUMBER(9,0) null,
	LASTWARNTIME TIMESTAMP(6) null,
	LASTERRORTIME TIMESTAMP(6) null,
	LASTCONTROLTIME TIMESTAMP(6) null)
/

grant select, update, insert, delete on RDX_SM_METRICCONTROLDATA to &USER&_RUN_ROLE
/

create table RDX_SM_METRICHIST(
	METRICID NUMBER(9,0) not null,
	ENDTIME TIMESTAMP(6) not null,
	ID NUMBER(18,0) not null,
	BEGTIME TIMESTAMP(6) not null,
	BEGVAL NUMBER null,
	ENDVAL NUMBER null,
	MINVAL NUMBER null,
	MAXVAL NUMBER null,
	AVGVAL NUMBER null)
/

grant select, update, insert, delete on RDX_SM_METRICHIST to &USER&_RUN_ROLE
/

create table RDX_SM_METRICSTATE(
	ID NUMBER(9,0) not null,
	LASTHISTID NUMBER(9,0) null,
	TYPEID NUMBER(9,0) not null,
	INSTANCEID NUMBER(9,0) null,
	UNITID NUMBER(9,0) null,
	SYSTEMID NUMBER(9,0) as (1) null,
	SERVICEURI VARCHAR2(300 char) null,
	NETCHANNELID NUMBER(9,0) null,
	BEGTIME TIMESTAMP(6) not null,
	ENDTIME TIMESTAMP(6) not null,
	BEGVAL NUMBER null,
	ENDVAL NUMBER null,
	MINVAL NUMBER null,
	MAXVAL NUMBER null,
	AVGVAL NUMBER null)
/

grant select, update, insert, delete on RDX_SM_METRICSTATE to &USER&_RUN_ROLE
/

create table RDX_SM_METRICTYPE(
	ID NUMBER(9,0) not null,
	ENABLED NUMBER(1,0) default 1 not null,
	CLASSGUID VARCHAR2(50 char) not null,
	KIND VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) not null,
	PERIOD NUMBER(9,0) default 60 not null,
	INSTANCEID NUMBER(9,0) null,
	UNITID NUMBER(9,0) null,
	SYSTEMID NUMBER(9,0) as (1) null,
	SERVICEURI VARCHAR2(300 char) null,
	NETCHANNELID NUMBER(9,0) null,
	TIMINGSECTION VARCHAR2(200 char) null,
	USERQUERY CLOB null,
	LOWERRORVAL NUMBER null,
	LOWWARNVAL NUMBER null,
	HIGHWARNVAL NUMBER null,
	HIGHERRORVAL NUMBER null,
	WARNDELAY NUMBER(9,0) default 0 not null,
	ERRORDELAY NUMBER(9,0) default 0 not null,
	ESCALATIONDELAY NUMBER(9,0) default 0 null,
	CONTROLLEDVALUE VARCHAR2(50 char) null,
	NOTES VARCHAR2(4000 char) null,
	MINSEVERITYFORSNMPNTFY NUMBER(1,0) default 1 null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSER VARCHAR2(200 char) not null)
/

grant select, update, insert, delete on RDX_SM_METRICTYPE to &USER&_RUN_ROLE
/

create table RDX_SM_SNMPAGENTUNIT(
	ID NUMBER(9,0) not null,
	AGENTADDRESS VARCHAR2(100 char) not null,
	SAPID NUMBER(9,0) null,
	USENOTIFICATIONSERVICE NUMBER(1,0) not null,
	MANAGERID NUMBER(9,0) null,
	COMMUNITYSTRING VARCHAR2(100 char) not null)
/

grant select, update, insert, delete on RDX_SM_SNMPAGENTUNIT to &USER&_RUN_ROLE
/

create table RDX_SM_SNMPMANAGER(
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(300 char) null,
	ADDRESS VARCHAR2(100 char) not null,
	ACTIVE NUMBER(1,0) not null)
/

grant select, update, insert, delete on RDX_SM_SNMPMANAGER to &USER&_RUN_ROLE
/

create table RDX_STATION(
	NAME VARCHAR2(100 char) not null,
	NOTES VARCHAR2(1000 char) null,
	SCPSYSTEMID NUMBER(9,0) default 1 null,
	SCPNAME VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_STATION to &USER&_RUN_ROLE
/

create table RDX_SYSTEM(
	ID NUMBER(9,0) not null,
	EXTSYSTEMCODE NUMBER(9,0) null,
	CERTATTRFORUSERLOGIN VARCHAR2(100 char) default 'CN' not null,
	NAME VARCHAR2(100 char) not null,
	NOTES VARCHAR2(4000 char) null,
	ARTELANGUAGE VARCHAR2(3 char) default 'en' not null,
	ARTECOUNTRY VARCHAR2(3 char) default 'US' null,
	ARTEGUITRACEPROFILE VARCHAR2(4000 char) default 'Event' not null,
	ARTEFILETRACEPROFILE VARCHAR2(4000 char) default 'Event' not null,
	ARTEDBTRACEPROFILE VARCHAR2(4000 char) default 'Event' not null,
	EASSESSIONACTIVITYMINS NUMBER(9,0) default 1440 not null,
	EASSESSIONINACTIVITYMINS NUMBER(9,0) default 15 not null,
	EASKRBPRINCNAME VARCHAR2(1000 char) null,
	LIMITEASSESSIONSPERUSR NUMBER(9,0) null,
	ASKUSERPWDAFTERINACTIVITY NUMBER(1,0) default 1 not null,
	UNIQUEPWDSEQLEN NUMBER(9,0) default 4 null,
	PWDMINLEN NUMBER(3,0) default 7 null,
	PWDMUSTDIFFERFROMNAME NUMBER(1,0) default 0 not null,
	PWDMUSTCONTAINACHARS NUMBER(1,0) default 1 not null,
	PWDMUSTCONTAINNCHARS NUMBER(1,0) default 1 not null,
	PWDEXPIRATIONPERIOD NUMBER(9,0) null,
	EVENTSTOREDAYS NUMBER(9,0) default 30 not null,
	PCMSTOREDAYS NUMBER(9,0) default 30 not null,
	AUDITSTOREPERIOD1 NUMBER(9,0) default 1 not null,
	AUDITSTOREPERIOD2 NUMBER(9,0) default 7 not null,
	AUDITSTOREPERIOD3 NUMBER(9,0) default 31 not null,
	AUDITSTOREPERIOD4 NUMBER(9,0) default 100 not null,
	AUDITSTOREPERIOD5 NUMBER(9,0) default 366 not null,
	DEFAULTAUDITSCHEMEID VARCHAR2(50 char) null,
	BLOCKUSERINVALIDLOGONCNT NUMBER(9,0) default 6 null,
	BLOCKUSERINVALIDLOGONMINS NUMBER(9,0) default 30 null,
	ENABLESENSITIVETRACE NUMBER(1,0) default 0 not null,
	PROFILEMODE NUMBER(1,0) default 0 not null,
	PROFILEDPREFIXES VARCHAR2(4000 char) null,
	PROFILEPERIODSEC NUMBER(9,0) default 60 not null,
	PROFILELOGSTOREDAYS NUMBER(9,0) default 1 not null,
	METRICHISTORYSTOREDAYS NUMBER(9,0) default 14 not null,
	USEREXTDEVLANGUAGES CLOB null,
	USEORAIMPLSTMTCACHE NUMBER(1,0) default 0 not null,
	ORAIMPLSTMTCACHESIZE NUMBER(9,0) default 100 not null,
	DUALCONTROLFORASSIGNROLE NUMBER(1,0) default 0 not null,
	DUALCONTROLFORCFGMGMT NUMBER(1,0) default 0 not null,
	WRITECONTEXTTOFILE NUMBER(1,0) default 1 not null,
	FAILEDOUTMESSAGESTOREDAYS NUMBER(9,0) default 14 not null)
/

grant select, update, insert, delete on RDX_SYSTEM to &USER&_RUN_ROLE
/

create table RDX_TESTCASE(
	ID NUMBER(9,0) not null,
	GROUPID NUMBER(9,0) null,
	CLASSGUID VARCHAR2(100 char) null,
	PARENTID NUMBER(9,0) null,
	NOTES VARCHAR2(2000 char) null,
	TRACEPROFILE VARCHAR2(1000 char) default 'Debug' not null,
	ACTOR VARCHAR2(250 char) not null,
	STARTTIME TIMESTAMP(6) not null,
	FINISHTIME TIMESTAMP(6) not null,
	RESULT NUMBER(1,0) not null,
	RESULTCOMMENT CLOB null,
	RUNONIS NUMBER(1,0) default 1 not null)
/

grant select, update, insert, delete on RDX_TESTCASE to &USER&_RUN_ROLE
/

create table RDX_TST_AUDITDETAIL(
	PKCOLINT NUMBER(9,0) not null,
	PKCOLCHAR CHAR(1 char) not null,
	PKCOLNUM NUMBER not null,
	PKCOLDATE DATE not null,
	PKCOLTIMESTAMP TIMESTAMP(6) not null,
	PKCOLSTR VARCHAR2(100 char) not null,
	TITLE VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_TST_AUDITDETAIL to &USER&_RUN_ROLE
/

create table RDX_TST_AUDITMASTER(
	PKCOLINT NUMBER(9,0) not null,
	PKCOLCHAR CHAR(1 char) default 'C' not null,
	PKCOLNUM NUMBER default 123.456 not null,
	PKCOLDATE DATE default SYSDATE not null,
	PKCOLTIMESTAMP TIMESTAMP(6) default SYSTIMESTAMP not null,
	PKCOLSTR VARCHAR2(100 char) default 'Some String' not null,
	CLASSGUID VARCHAR2(100 char) null,
	COLINT NUMBER(9,0) null,
	COLCHAR CHAR(1 char) null,
	COLNUM NUMBER null,
	COLDATE DATE null,
	COLTIMESTAMP TIMESTAMP(6) null,
	COLSTR VARCHAR2(100 char) null,
	COLBIN RAW(2000) null,
	COLBLOB BLOB null,
	COLCLOB CLOB null,
	COLARRBOOL CLOB null,
	COLARRINT VARCHAR2(4000 char) null,
	COLARRCHAR CLOB null,
	COLARRSTR CLOB null,
	COLARRNUM VARCHAR2(4000 char) null,
	COLARRDATETIME CLOB null,
	COLARRBIN CLOB null,
	COLARRPARENTREF CLOB null)
/

grant select, update, insert, delete on RDX_TST_AUDITMASTER to &USER&_RUN_ROLE
/

create table RDX_TST_CHILD(
	AUDITSCHEME VARCHAR2(9 char) null,
	ID NUMBER(9,0) not null,
	PARENTID NUMBER(9,0) not null,
	TITLE VARCHAR2(9 char) not null,
	CHILDTYPE NUMBER(9,0) null,
	COLBOOL NUMBER(1,0) null,
	COLINT NUMBER(9,0) null,
	COLCHAR CHAR(1 char) null,
	COLNUM NUMBER(9,3) null,
	COLDATETIME DATE null,
	COLSTR VARCHAR2(9 char) null,
	COLBIN RAW(2000) null,
	COLCLOB CLOB null,
	COLCLOBASXML CLOB null,
	COLBLOB BLOB null,
	COLARRBOOL CLOB null,
	COLARRINT CLOB null,
	COLARRCHAR CLOB null,
	COLARRNUM CLOB null,
	COLARRDATETIME CLOB null,
	COLARRBIN CLOB null,
	COLARRSTR CLOB null,
	VALTYPE NUMBER(9,0) null,
	DEFINITIONIDPREFIX VARCHAR2(64 char) null)
/

grant select, update, insert, delete on RDX_TST_CHILD to &USER&_RUN_ROLE
/

create table RDX_TST_DMTEST(
	ID NUMBER(9,0) not null,
	KEYINT NUMBER(9,0) null,
	KEYSTR VARCHAR2(100 char) null,
	TITLE VARCHAR2(9 char) not null,
	COLBOOL NUMBER(1,0) null,
	COLINT NUMBER(9,0) null,
	COLCHAR CHAR(1 char) null,
	COLNUM NUMBER(9,3) null,
	COLDATETIME DATE null,
	COLSTR VARCHAR2(9 char) null,
	COLBIN RAW(2000) null,
	COLCLOB CLOB null,
	COLBLOB BLOB null,
	COLARRBOOL CLOB null,
	COLARRINT CLOB null,
	COLARRCHAR CLOB null,
	COLARRNUM CLOB null,
	COLARRDATETIME CLOB null,
	COLARRBIN CLOB null,
	COLARRSTR CLOB null,
	VALTYPE NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_TST_DMTEST to &USER&_RUN_ROLE
/

create table RDX_TST_DMTESTA(
	PKINT NUMBER(9,0) not null,
	PKSTR VARCHAR2(100 char) not null,
	CLASSGUID VARCHAR2(100 char) null,
	TITLE VARCHAR2(9 char) not null)
/

grant select, update, insert, delete on RDX_TST_DMTESTA to &USER&_RUN_ROLE
/

create table RDX_TST_DMTESTB(
	PKINT NUMBER(9,0) not null,
	PKSTR VARCHAR2(100 char) not null,
	TITLE VARCHAR2(9 char) not null)
/

grant select, update, insert, delete on RDX_TST_DMTESTB to &USER&_RUN_ROLE
/

create table RDX_TST_DMTESTCHILD(
	ID NUMBER(9,0) not null,
	KEYID NUMBER(9,0) null,
	TITLE VARCHAR2(9 char) not null)
/

grant select, update, insert, delete on RDX_TST_DMTESTCHILD to &USER&_RUN_ROLE
/

create table RDX_TST_IMAGES(
	ID NUMBER(9,0) not null,
	DATA BLOB not null,
	MIMETYPE VARCHAR2(64 char) not null)
/

grant select, update, insert, delete on RDX_TST_IMAGES to &USER&_RUN_ROLE
/

create table RDX_TST_INHERITANCENODE(
	ID NUMBER(9,0) not null,
	COLCLOB CLOB null,
	COLBLOB BLOB null,
	COLINT NUMBER(9,0) null,
	COLARRPARENTREF CLOB null,
	PARENTID NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_TST_INHERITANCENODE to &USER&_RUN_ROLE
/

create table RDX_TST_NEWTABLE(
	NEWCOLUMN NUMBER(9,0) not null)
/

grant select, update, insert, delete on RDX_TST_NEWTABLE to &USER&_RUN_ROLE
/

create table RDX_TST_PARENT(
	AUDITSCHEME VARCHAR2(50 char) null,
	COLCLOBASXML CLOB null,
	ID NUMBER(9,0) not null,
	TITLE VARCHAR2(100 char) not null)
/

grant select, update, insert, delete on RDX_TST_PARENT to &USER&_RUN_ROLE
/

create table RDX_TST_TABLEWITHDATEPK(
	DATEPK DATE not null,
	INTVALUE NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_TST_TABLEWITHDATEPK to &USER&_RUN_ROLE
/

create table RDX_TST_TEDCCHILD(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	PARENTID NUMBER(9,0) null,
	NOTES VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_TST_TEDCCHILD to &USER&_RUN_ROLE
/

create table RDX_TST_TEDCCHILDDETAIL(
	ID NUMBER(9,0) not null,
	DETAILPARENTID NUMBER(9,0) null)
/

grant select, update, insert, delete on RDX_TST_TEDCCHILDDETAIL to &USER&_RUN_ROLE
/

create table RDX_TST_TEDCPARENT(
	ID NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	NOTES VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_TST_TEDCPARENT to &USER&_RUN_ROLE
/

create table RDX_TST_TESTPAIR(
	KEY NUMBER(9,0) not null,
	VALUE NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_TST_TESTPAIR to &USER&_RUN_ROLE
/

create table RDX_TST_USERCHILD(
	ID NUMBER(9,0) not null)
/

grant select, update, insert, delete on RDX_TST_USERCHILD to &USER&_RUN_ROLE
/

create table RDX_TST_USERPARENTCOMPLEX(
	PK_INT NUMBER(9,0) not null,
	PK_CHAR CHAR(1 char) not null,
	PK_DATE TIMESTAMP(6) not null,
	PK_STR VARCHAR2(100 char) not null)
/

grant select, update, insert, delete on RDX_TST_USERPARENTCOMPLEX to &USER&_RUN_ROLE
/

create table RDX_TST_USERPARENTSIMPLE(
	ID NUMBER(18,0) not null)
/

grant select, update, insert, delete on RDX_TST_USERPARENTSIMPLE to &USER&_RUN_ROLE
/

create table RDX_UNIT(
	ID NUMBER(9,0) not null,
	INSTANCEID NUMBER(9,0) not null,
	TYPE NUMBER(9,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(200 char) null,
	USE NUMBER(1,0) default 1 not null,
	STARTED NUMBER(1,0) default 0 not null,
	GUITRACEPROFILE VARCHAR2(1000 char) default 'Event' not null,
	FILETRACEPROFILE VARCHAR2(1000 char) default 'Event' not null,
	DBTRACEPROFILE VARCHAR2(1000 char) default 'Event' not null,
	SELFCHECKTIME TIMESTAMP(6) null,
	SYSTEMID NUMBER(9,0) null,
	SCPNAME VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_UNIT to &USER&_RUN_ROLE
/

create table RDX_UPVALBLOB(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL BLOB null)
/

grant select, update, insert, delete on RDX_UPVALBLOB to &USER&_RUN_ROLE
/

create table RDX_UPVALCLOB(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL CLOB null)
/

grant select, update, insert, delete on RDX_UPVALCLOB to &USER&_RUN_ROLE
/

create table RDX_UPVALDATETIME(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL TIMESTAMP(6) null)
/

grant select, update, insert, delete on RDX_UPVALDATETIME to &USER&_RUN_ROLE
/

create table RDX_UPVALNUM(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL NUMBER null)
/

grant select, update, insert, delete on RDX_UPVALNUM to &USER&_RUN_ROLE
/

create table RDX_UPVALRAW(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL RAW(2000) null)
/

grant select, update, insert, delete on RDX_UPVALRAW to &USER&_RUN_ROLE
/

create table RDX_UPVALREF(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_UPVALREF to &USER&_RUN_ROLE
/

create table RDX_UPVALSTR(
	DEFID VARCHAR2(100 char) not null,
	OWNERENTITYID VARCHAR2(100 char) not null,
	OWNERPID VARCHAR2(200 char) not null,
	VAL VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_UPVALSTR to &USER&_RUN_ROLE
/

create table RDX_USER2STATION(
	STATIONNAME VARCHAR2(100 char) not null,
	USERNAME VARCHAR2(250 char) not null)
/

grant select, update, insert, delete on RDX_USER2STATION to &USER&_RUN_ROLE
/

create table RDX_USERDEFTEST(
	TITLE VARCHAR2(1000 char) null,
	CLAZZGUID VARCHAR2(100 char) not null,
	DEFINITIONTYPE NUMBER(9,0) null,
	LASTMODIFIED_ DATE null,
	LASTTESTTIME DATE null,
	TESTPASSED NUMBER(1,0) null,
	FAILMESSAGE VARCHAR2(2000 char) null,
	SRC CLOB null,
	RUNTIME BLOB null)
/

grant select, update, insert, delete on RDX_USERDEFTEST to &USER&_RUN_ROLE
/

create table RDX_USERFUNC(
	ID NUMBER(9,0) not null,
	UPDEFID VARCHAR2(100 char) not null,
	LIBFUNCNAME VARCHAR2(1000 char) null,
	JAVASRCVERS CLOB null,
	UPOWNERENTITYID VARCHAR2(100 char) not null,
	UPOWNERPID VARCHAR2(200 char) not null,
	CLASSGUID VARCHAR2(29 char) not null,
	USERCLASSGUID VARCHAR2(200 char) not null,
	TITLE VARCHAR2(250 char) null,
	VERSION NUMBER(9,0) default 0 not null,
	JAVASRC CLOB null,
	JAVARUNTIME BLOB null,
	EXTSRC CLOB null,
	USEDDEFINITIONS CLOB null,
	LASTUPDATETIME DATE not null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null,
	ISLINKUSED NUMBER(1,0) default 0 null,
	PARAMBINDING CLOB null)
/

grant select, update, insert, delete on RDX_USERFUNC to &USER&_RUN_ROLE
/

create table RDX_USERFUNCLIB(
	NAME VARCHAR2(1000 char) not null,
	PIPELINEID NUMBER(9,0) null,
	TITLE VARCHAR2(2000 char) null,
	DESCRIPTION VARCHAR2(4000 char) null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null,
	LASTUPDATETIME TIMESTAMP(6) null)
/

grant select, update, insert, delete on RDX_USERFUNCLIB to &USER&_RUN_ROLE
/

create table RDX_USERFUNCLIBPARAM(
	NAME VARCHAR2(300 char) not null,
	LIBNAME VARCHAR2(1000 char) not null,
	DESCRIPTION VARCHAR2(4000 char) null,
	CLASSGUID VARCHAR2(100 char) not null,
	STRVAL VARCHAR2(1000 char) null,
	INTVAL NUMBER(9,0) null,
	BOOLVAL NUMBER(1,0) null,
	DATETIMEVAL DATE null,
	NUMVAL NUMBER null,
	CHARVAL CHAR(1 char) null)
/

grant select, update, insert, delete on RDX_USERFUNCLIBPARAM to &USER&_RUN_ROLE
/

create table RDX_USERREPORT(
	GUID VARCHAR2(100 char) not null,
	MODULEGUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(2000 char) null,
	CURVERSION NUMBER(9,0) null,
	DESCIRPTION VARCHAR2(2000 char) null,
	CONTEXTPARAMETERTYPE CLOB null,
	LASTUPDATETIME TIMESTAMP(6) null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null,
	FORMATVERSION NUMBER(9,0) default 0 null)
/

grant select, update, insert, delete on RDX_USERREPORT to &USER&_RUN_ROLE
/

create table RDX_USERREPORTMODULE(
	GUID VARCHAR2(100 char) not null,
	TITLE VARCHAR2(1000 char) null,
	DESCRIPTION CLOB null,
	IMAGES BLOB null,
	FORMATVERSION NUMBER(9,0) default 0 null)
/

grant select, update, insert, delete on RDX_USERREPORTMODULE to &USER&_RUN_ROLE
/

create table RDX_USERREPORTVERSION(
	REPORTGUID VARCHAR2(100 char) not null,
	VERSION NUMBER(9,0) not null,
	LASTUPDATETIME TIMESTAMP(6) null,
	LASTUPDATEUSERNAME VARCHAR2(250 char) null,
	REPORTSOURCE CLOB null,
	REPORTBINARY BLOB null,
	REPORTCLASSGUID VARCHAR2(100 char) null)
/

grant select, update, insert, delete on RDX_USERREPORTVERSION to &USER&_RUN_ROLE
/

create table RDX_WEBSETTING(
	USERNAME VARCHAR2(250 char) not null,
	PROPNAME VARCHAR2(1000 char) not null,
	PROPVALUE CLOB null,
	LASTCHANGETIME DATE null)
/

grant select, update, insert, delete on RDX_WEBSETTING to &USER&_RUN_ROLE
/

create table RDX_WF_FORM(
	ID NUMBER(18,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	PROCESSID NUMBER(18,0) not null,
	TITLE VARCHAR2(1000 char) null,
	HEADERTITLE VARCHAR2(1000 char) null,
	FOOTERTITLE VARCHAR2(1000 char) null,
	PRIORITY NUMBER(1,0) not null,
	OPENTIME DATE not null,
	DUETIME DATE not null,
	OVERDUETIME DATE null,
	EXPIRATIONTIME DATE null,
	CLOSETIME DATE null,
	CLERKNAME VARCHAR2(250 char) null,
	SUBMITVARIANTS VARCHAR2(1000 char) null,
	SUBMITVARIANTSTITLE VARCHAR2(4000 char) null,
	SUBMITVARIANTSVISIBLE VARCHAR2(1000 char) null,
	SUBMITVARIANTSNEEDCONFIRM VARCHAR2(1000 char) null,
	SUBMITVARIANTSNEEDUPDATE VARCHAR2(1000 char) null,
	SUBMITVARIANT VARCHAR2(50 char) null,
	OBJCLASSGUID VARCHAR2(100 char) null,
	SELECTORPRESENTATIONID VARCHAR2(100 char) null,
	RESTRICTIONS NUMBER(9,0) null,
	SELECTCONDITION VARCHAR2(4000 char) null,
	EDITORPRESENTATIONIDS VARCHAR2(1000 char) null,
	ADMINPRESENTATIONID VARCHAR2(100 char) null,
	CREATIONPRESENTATIONID VARCHAR2(100 char) null,
	SELECTEDPID VARCHAR2(1000 char) null,
	REPORTGUID VARCHAR2(50 char) null,
	CONTENTSAVING NUMBER(1,0) not null,
	WAITID NUMBER(18,0) null,
	CLERKROLEGUIDS VARCHAR2(4000 char) null,
	ADMINROLEGUIDS VARCHAR2(4000 char) null,
	ACCESSAREA VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_WF_FORM to &USER&_RUN_ROLE
/

create table RDX_WF_FORMLOG(
	FORMID NUMBER(18,0) not null,
	ID NUMBER(18,0) not null,
	TIME DATE not null,
	CLERKNAME VARCHAR2(250 char) not null,
	COMMANDGUID VARCHAR2(50 char) not null,
	COMMENTS VARCHAR2(4000 char) null,
	DESTCLERKNAME VARCHAR2(250 char) null,
	CONTENT CLOB null)
/

grant select, update, insert, delete on RDX_WF_FORMLOG to &USER&_RUN_ROLE
/

create table RDX_WF_PROCESS(
	ID NUMBER(18,0) not null,
	CLASSGUID VARCHAR2(100 char) not null,
	ALGOCLASSGUID VARCHAR2(100 char) not null,
	TYPEGUID VARCHAR2(50 char) not null,
	CREATORNAME VARCHAR2(250 char) null,
	OWNERNAME VARCHAR2(250 char) null,
	LANGUAGE VARCHAR2(2 char) not null,
	TITLE VARCHAR2(1000 char) null,
	STATE NUMBER(1,0) default 0 not null,
	STATEDESC VARCHAR2(1000 char) null,
	STARTTIME DATE not null,
	FINISHTIME DATE null,
	EXCEPTIONTEXT VARCHAR2(4000 char) null,
	CONTEXT CLOB null,
	TRACEPROFILE VARCHAR2(4000 char) null,
	SIMULATEDTIME DATE null,
	PURGETIME DATE null,
	ACCESSAREA VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_WF_PROCESS to &USER&_RUN_ROLE
/

create table RDX_WF_PROCESSLINK(
	ID NUMBER(18,0) not null,
	PROCESSID NUMBER(18,0) not null,
	ROLE VARCHAR2(100 char) not null,
	IDX NUMBER(9,0) not null,
	ENTITYGUID VARCHAR2(50 char) null,
	OBJECTPID VARCHAR2(100 char) null,
	TITLE VARCHAR2(300 char) null)
/

grant select, update, insert, delete on RDX_WF_PROCESSLINK to &USER&_RUN_ROLE
/

create table RDX_WF_PROCESSTYPE(
	GUID VARCHAR2(50 char) not null,
	TITLE VARCHAR2(1000 char) null,
	CLASSGUID VARCHAR2(100 char) not null,
	ALGOCLASSGUID VARCHAR2(100 char) not null,
	TRACEPROFILE VARCHAR2(4000 char) null,
	CLERKROLEGUIDS VARCHAR2(4000 char) null,
	ADMINROLEGUIDS VARCHAR2(4000 char) null,
	PROCESSSTOREDAYS NUMBER(9,0) not null,
	NOTES VARCHAR2(4000 char) null)
/

grant select, update, insert, delete on RDX_WF_PROCESSTYPE to &USER&_RUN_ROLE
/

insert into RDX_JS_JOBEXECUTORUNIT (ID, PARALLELCNT, ABOVENORMALDELTA, HIGHDELTA, VERYHIGHDELTA, CRITICALDELTA, EXECPERIOD) values (
	3,
	8,
	8,
	8,
	8,
	8,
	5)
/

insert into RDX_AC_USER2ROLE (ID, USERNAME, ROLEID) values (
	0,
	'ADMINISTRATOR',
	'rolSUPER_ADMIN_______________')
/

insert into RDX_INSTANCE (ID, TITLE, DBTRACEPROFILE, GUITRACEPROFILE, FILETRACEPROFILE, TRACEFILESDIR, ROTATETRACEFILESDAILY, SAPID, SCPNAME, LOWARTEINSTCOUNT, HIGHARTEINSTCOUNT) values (
	1,
	'Local',
	'Event',
	'Event',
	'Event',
	'./logs',
	0,
	1,
	'Local',
	2,
	10)
/

insert into RDX_UNIT (ID, INSTANCEID, TYPE, CLASSGUID, TITLE, USE, GUITRACEPROFILE, FILETRACEPROFILE, DBTRACEPROFILE) values (
	1,
	1,
	3001,
	'aclFKCYABVIK7OBDCJAAALOMT5GDM',
	'Local EAS',
	1,
	'Event',
	'Event',
	'Event')
/

insert into RDX_UNIT (ID, INSTANCEID, TYPE, CLASSGUID, TITLE, USE, GUITRACEPROFILE, FILETRACEPROFILE, DBTRACEPROFILE) values (
	2,
	1,
	3001,
	'aclFKCYABVIK7OBDCJAAALOMT5GDM',
	'Local AAS',
	1,
	'Event',
	'Event',
	'Event')
/

insert into RDX_UNIT (ID, INSTANCEID, TYPE, CLASSGUID, TITLE, USE, GUITRACEPROFILE, FILETRACEPROFILE, DBTRACEPROFILE) values (
	3,
	1,
	2001,
	'aclESARTHXEX7NRDB6TAALOMT5GDM',
	'Executor',
	1,
	'Event',
	'Event',
	'Event')
/

insert into RDX_UNIT (ID, INSTANCEID, TYPE, CLASSGUID, TITLE, USE, GUITRACEPROFILE, FILETRACEPROFILE, DBTRACEPROFILE) values (
	4,
	1,
	1001,
	'acl7QS5XAHQJPOBDCIUAALOMT5GDM',
	'Scheduler',
	1,
	'Event',
	'Event',
	'Event')
/

insert into RDX_SCP2SAP (SYSTEMID, SAPID, SCPNAME) values (
	1,
	1,
	'Local')
/

insert into RDX_SCP2SAP (SYSTEMID, SAPID, SCPNAME) values (
	1,
	2,
	'Local')
/

insert into RDX_SCP2SAP (SYSTEMID, SAPID, SCPNAME) values (
	1,
	3,
	'Local')
/

insert into RDX_SCP (NAME, SYSTEMID) values (
	'Local',
	1)
/

insert into RDX_TST_CHILD (AUDITSCHEME, ID, PARENTID, TITLE, VALTYPE, DEFINITIONIDPREFIX) values (
	NULL,
	1,
	1,
	'Child 1.1',
	2,
	'tbl')
/

insert into RDX_TST_CHILD (AUDITSCHEME, ID, PARENTID, TITLE, VALTYPE, DEFINITIONIDPREFIX) values (
	NULL,
	2,
	2,
	'Child 2.2',
	21,
	'col')
/

insert into RDX_TST_CHILD (AUDITSCHEME, ID, PARENTID, TITLE, VALTYPE, DEFINITIONIDPREFIX) values (
	NULL,
	3,
	2,
	'Child 2.2',
	NULL,
	NULL)
/

insert into RDX_TST_CHILD (AUDITSCHEME, ID, PARENTID, TITLE, VALTYPE, DEFINITIONIDPREFIX) values (
	NULL,
	4,
	3,
	'Child 3.1',
	NULL,
	NULL)
/

insert into RDX_TST_CHILD (AUDITSCHEME, ID, PARENTID, TITLE, VALTYPE, DEFINITIONIDPREFIX) values (
	NULL,
	5,
	3,
	'Child 3.2',
	NULL,
	NULL)
/

insert into RDX_TST_CHILD (AUDITSCHEME, ID, PARENTID, TITLE, VALTYPE, DEFINITIONIDPREFIX) values (
	NULL,
	6,
	3,
	'Child 3.3',
	NULL,
	NULL)
/

insert into RDX_SERVICE (SYSTEMID, URI, WSDLURI, TITLE, IMPLEMENTEDINARTE, ACCESSIBILITY) values (
	1,
	'http://schemas.radixware.org/eas.wsdl',
	'http://schemas.radixware.org/eas.wsdl',
	'Explorer Access Service',
	1,
	'E')
/

insert into RDX_SERVICE (SYSTEMID, URI, WSDLURI, TITLE, IMPLEMENTEDINARTE, ACCESSIBILITY) values (
	1,
	'http://schemas.radixware.org/systeminstancecontrol.wsdl#1',
	'http://schemas.radixware.org/systeminstancecontrol.wsdl',
	'System Instance #1 Control Service',
	0,
	NULL)
/

insert into RDX_SERVICE (SYSTEMID, URI, WSDLURI, TITLE, IMPLEMENTEDINARTE, ACCESSIBILITY) values (
	1,
	'http://schemas.radixware.org/aas.wsdl',
	'http://schemas.radixware.org/aas.wsdl',
	'ARTE Access Service',
	1,
	NULL)
/

insert into RDX_STATION (NAME, SCPNAME) values (
	'ServerConsole',
	'Local')
/

insert into RDX_TST_PARENT (ID, TITLE) values (
	1,
	'Parent One')
/

insert into RDX_TST_PARENT (ID, TITLE) values (
	2,
	'Parent Two')
/

insert into RDX_TST_PARENT (ID, TITLE) values (
	3,
	'Parent Three')
/

insert into RDX_TST_PARENT (ID, TITLE) values (
	4,
	'Parent Four')
/

insert into RDX_JS_EVENTSCHD (ID, GUID, TITLE, LASTUPDATETIME, LASTUPDATEUSER) values (
	1,
	'ORNZIEXPW5EQPFBHNBHI7FUHJQ',
	'Daily at 01:00',
	TO_TIMESTAMP('14.12.2009 00:00:00.000', 'DD.MM.YYYY HH24:MI:SS.FF'),
	'ADMINISTRATOR')
/

insert into RDX_JS_EVENTSCHD (ID, GUID, TITLE, LASTUPDATETIME, LASTUPDATEUSER) values (
	2,
	'ZJA4EFVINVGZ7GDJRLVMIFHOZY',
	'Each 5 min',
	TO_TIMESTAMP('14.12.2009 00:00:00.000', 'DD.MM.YYYY HH24:MI:SS.FF'),
	'ADMINISTRATOR')
/

insert into RDX_SAP (ID, NAME, SYSTEMID, URI, ISACTIVE, ADDRESS, EASKRBAUTH, ACCESSIBILITY) values (
	1,
	'SAP of instance #1',
	1,
	'http://schemas.radixware.org/systeminstancecontrol.wsdl#1',
	0,
	'localhost:10003',
	0,
	'I')
/

insert into RDX_SAP (ID, NAME, SYSTEMID, URI, ISACTIVE, ADDRESS, EASKRBAUTH, ACCESSIBILITY) values (
	2,
	'SAP of unit #1',
	1,
	'http://schemas.radixware.org/eas.wsdl',
	0,
	'localhost:10001',
	0,
	'E')
/

insert into RDX_SAP (ID, NAME, SYSTEMID, URI, ISACTIVE, ADDRESS, EASKRBAUTH, ACCESSIBILITY) values (
	3,
	'SAP of unit #2',
	1,
	'http://schemas.radixware.org/aas.wsdl',
	0,
	'localhost:10002',
	0,
	'I')
/

insert into RDX_AC_USER (NAME, PWDHASH, INVALIDLOGONTIME, DBTRACEPROFILE, TRACEGUIACTIONS) values (
	'ADMINISTRATOR',
	'254574647F11D2A029040C7B692D5F53',
	TO_TIMESTAMP('27.01.2016 00:00:00.000', 'DD.MM.YYYY HH24:MI:SS.FF'),
	'Debug',
	1)
/

insert into RDX_JS_TASK (ID, UNITID, SCHEDULEID, CLASSGUID, EXPIREDPOLICY, ISACTUALIZABLEBYRELATEDJOBS) values (
	1,
	4,
	1,
	'aclABO344YGVLOBDCLSAALOMT5GDM',
	1,
	1)
/

insert into RDX_JS_TASK (ID, UNITID, SCHEDULEID, CLASSGUID, EXPIREDPOLICY, ISACTUALIZABLEBYRELATEDJOBS) values (
	2,
	4,
	2,
	'aclAVMZXCDXJTOBDCIVAALOMT5GDM',
	0,
	1)
/

insert into RDX_SYSTEM (ID, EXTSYSTEMCODE, NAME) values (
	1,
	1,
	'THIS')
/

insert into RDX_ARTEUNIT (ID, SAPID, SERVICEURI, HIGHARTEINSTCOUNT) values (
	1,
	2,
	'http://schemas.radixware.org/eas.wsdl',
	8)
/

insert into RDX_ARTEUNIT (ID, SAPID, SERVICEURI, HIGHARTEINSTCOUNT) values (
	2,
	3,
	'http://schemas.radixware.org/aas.wsdl',
	8)
/

insert into RDX_JS_EVENTSCHDITEM (SCHEDULEID, ID, REPEATABLE, STARTTIME, ENDTIME, PERIOD, EVENTTIME) values (
	1,
	1,
	0,
	0,
	1439,
	0,
	60)
/

insert into RDX_JS_EVENTSCHDITEM (SCHEDULEID, ID, REPEATABLE, STARTTIME, ENDTIME, PERIOD, EVENTTIME) values (
	2,
	0,
	1,
	0,
	1439,
	300,
	0)
/

alter table RDX_JS_JOBEXECUTORUNITBOOST add constraint CKC_DELAY_RDX_JS_JOBEXECUTORUN check (DELAY>0)
/

alter table RDX_SYSTEM add constraint CKC_EVENTSTOREDAYS_RDX_SYSTEM check (EVENTSTOREDAYS between 1 and 1000)
/

alter table RDX_JS_CALENDARITEM add constraint CKC_OFFSETDIR_RDX_JS_CALENDARI check (OFFSETDIR in (-1, 1))
/

alter table RDX_JS_CALENDARITEM add constraint CKC_OFFSET_RDX_JS_CALENDARITEM check (OFFSET between 0 and 99999)
/

alter table RDX_JS_CALENDARITEM add constraint CKC_OPER_RDX_JS_CALENDARITEM check (OPER in ('+', '-'))
/

alter table RDX_SYSTEM add constraint CKC_PROFILELOGSTOREDAYS_RDX_SY check (PROFILELOGSTOREDAYS between 1 and 1000)
/

alter table RDX_JS_JOBEXECUTORUNITBOOST add constraint CKC_SPEED_RDX_JS_JOBEXECUTORUN check (SPEED>0)
/

create unique index IDX_COUNTER_USERPROPERTYOBJECT on COUNTER (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc)
/

create index IDX_DWF_PROCESS_OWNERTSF on RDX_WF_PROCESS (OWNERNAME asc, TYPEGUID asc, STATE asc, FINISHTIME asc)
/

create index IDX_DWF_PROCESS_OWNERTSS on RDX_WF_PROCESS (OWNERNAME asc, TYPEGUID asc, STATE asc, STARTTIME asc)
/

create index IDX_RDX_AC_USER2ROLE_DASHCONFI on RDX_AC_USER2ROLE (PA$$4PQ4U65VK5HFVJ32XCUORBKRJM asc)
/

create index IDX_RDX_AC_USER2ROLE_REPLACEME on RDX_AC_USER2ROLE (REPLACEDID asc)
/

create index IDX_RDX_AC_USER2ROLE_ROLEIDIND on RDX_AC_USER2ROLE (ROLEID asc)
/

create index IDX_RDX_AC_USER2ROLE_USERGROUP on RDX_AC_USER2ROLE (PA$$1ZOQHCO35XORDCV2AANE2UAFXA asc)
/

create index IDX_RDX_AC_USER2ROLE_USERNAMEI on RDX_AC_USER2ROLE (USERNAME asc)
/

create index IDX_RDX_AC_USERGROUP2ROLE_DASH on RDX_AC_USERGROUP2ROLE (PA$$4PQ4U65VK5HFVJ32XCUORBKRJM asc)
/

create index IDX_RDX_AC_USERGROUP2ROLE_GROU on RDX_AC_USERGROUP2ROLE (GROUPNAME asc)
/

create index IDX_RDX_AC_USERGROUP2ROLE_REPL on RDX_AC_USERGROUP2ROLE (REPLACEDID asc)
/

create index IDX_RDX_AC_USERGROUP2ROLE_ROLE on RDX_AC_USERGROUP2ROLE (ROLEID asc)
/

create index IDX_RDX_AC_USERGROUP2ROLE_USER on RDX_AC_USERGROUP2ROLE (PA$$1ZOQHCO35XORDCV2AANE2UAFXA asc)
/

create index IDX_RDX_AC_USER_PERSONNAME on RDX_AC_USER (PERSONNAME asc)
/

create index IDX_RDX_AU_AUDITLOG_ENTITY on RDX_AU_AUDITLOG (TABLEID asc, PID asc, EVENTTIME asc, ID asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

create index IDX_RDX_CLPI_BY_PROFILE on RDX_CLASSLOADINGPROFILEITEM (PROFILEID asc)
/

create index IDX_RDX_CM_ITEM_PACKET on RDX_CM_ITEM (PACKETID asc, ID asc)
/

create index IDX_RDX_CM_ITEM_PARENT on RDX_CM_ITEM (PARENTID asc, ID asc)
/

create index IDX_RDX_CM_ITEM_SRC on RDX_CM_ITEM (PACKETID asc, CLASSGUID asc, SRCOBJECTPID asc)
/

create index IDX_RDX_EVENTCONTEXT_CONTEXT on RDX_EVENTCONTEXT (TYPE asc, ID asc, RAISETIME asc, EVENTID asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

create index IDX_RDX_EVENTLOG_HASSENSITIVE on RDX_EVENTLOG (ISSENSITIVE asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

create index IDX_RDX_JS_JOBQUEUE_DUE on RDX_JS_JOBQUEUE (EXECUTORID asc, DUETIME asc)
/

create index IDX_RDX_PC_CHANNELUNIT_ROUTING on RDX_PC_CHANNELUNIT (KIND asc, MESSADDRESSREGEXP asc, ROUTINGPRIORITY asc, ID asc)
/

create index IDX_RDX_PC_OUTMESSAGE_DEST on RDX_PC_OUTMESSAGE (DESTENTITYGUID asc, DESTPID asc, DUETIME asc)
/

create index IDX_RDX_PC_OUTMESSAGE_SEND on RDX_PC_OUTMESSAGE (CHANNELID asc, DUETIME asc)
/

create index IDX_RDX_PC_RECVMESSAGE_ADDRESS on RDX_PC_RECVMESSAGE (CHANNELID asc, ADDRESS asc, RECVTIME asc)
/

create index IDX_RDX_PC_RECVMESSAGE_DESTID on RDX_PC_RECVMESSAGE (DESTENTITYGUID asc, DESTPID asc, DESTMSGID asc)
/

create index IDX_RDX_PC_RECVMESSAGE_RECVTIM on RDX_PC_RECVMESSAGE (RECVTIME asc)
/

create index IDX_RDX_PC_SENTMESSAGE_ADDRESS on RDX_PC_SENTMESSAGE (CHANNELID asc, ADDRESS asc, SENTTIME asc)
/

create index IDX_RDX_PC_SENTMESSAGE_DEST on RDX_PC_SENTMESSAGE (DESTENTITYGUID asc, DESTPID asc, SENTTIME asc)
/

create index IDX_RDX_PC_SENTMESSAGE_SENTTIM on RDX_PC_SENTMESSAGE (SENTTIME asc)
/

create index IDX_RDX_PROFILERLOG_SECTIONINS on RDX_PROFILERLOG (SECTIONID asc, INSTANCEID asc)
/

create index IDX_RDX_PROFILERLOG_SESSIONCON on RDX_PROFILERLOG (SECTIONID asc, CONTEXT asc, INSTANCEID asc)
/

create index IDX_RDX_REPORTPUBLIST_CONTEXT on RDX_REPORTPUBLIST (CLASSGUID asc, PUBCONTEXTCLASSGUID asc, CONTEXTPID asc)
/

create index IDX_RDX_REPORTPUBTOPIC_LIST on RDX_REPORTPUBTOPIC (LISTID asc)
/

create index IDX_RDX_REPORTPUB_LIST on RDX_REPORTPUB (LISTID asc)
/

create unique index IDX_RDX_SAP_NAMEINSYSTEM on RDX_SAP (SYSTEMID asc, URI asc, NAME asc)
/

create index IDX_RDX_SAP_SCP2SAP on RDX_SAP (SYSTEMID asc, ID asc)
/

create index IDX_RDX_SB_PIPELINENODE_PIPE on RDX_SB_PIPELINENODE (PIPELINEENTITYGUID asc, PIPELINEENTITYPID asc, ID asc)
/

create index IDX_RDX_SB_XPATHTABLE_STAGE on RDX_SB_XPATHTABLE (STAGEID asc, ISREQUEST asc, SEQ asc)
/

create index IDX_RDX_SM_METRICHIST_ENDTIME on RDX_SM_METRICHIST (ENDTIME asc)
/

create index IDX_RDX_SM_METRICSTATE_INSTANC on RDX_SM_METRICSTATE (INSTANCEID asc)
/

create index IDX_RDX_SM_METRICSTATE_NETCHAN on RDX_SM_METRICSTATE (NETCHANNELID asc)
/

create index IDX_RDX_SM_METRICSTATE_SERVICE on RDX_SM_METRICSTATE (SYSTEMID asc, SERVICEURI asc)
/

create index IDX_RDX_SM_METRICSTATE_TYPE on RDX_SM_METRICSTATE (TYPEID asc)
/

create index IDX_RDX_SM_METRICSTATE_UNIT on RDX_SM_METRICSTATE (UNITID asc)
/

create index IDX_RDX_SM_METRICTYPE_INSTANCE on RDX_SM_METRICTYPE (INSTANCEID asc)
/

create index IDX_RDX_SM_METRICTYPE_NETCHANN on RDX_SM_METRICTYPE (NETCHANNELID asc)
/

create index IDX_RDX_SM_METRICTYPE_SERVICE on RDX_SM_METRICTYPE (SERVICEURI asc, SYSTEMID asc)
/

create index IDX_RDX_SM_METRICTYPE_UNIT on RDX_SM_METRICTYPE (UNITID asc)
/

create unique index IDX_RDX_SYSTEM_NAME on RDX_SYSTEM (NAME asc)
/

create index IDX_RDX_UPVALBLOB_OWNER on RDX_UPVALBLOB (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_UPVALCLOB_OWNER on RDX_UPVALCLOB (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_UPVALDATETIME_OWNER on RDX_UPVALDATETIME (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_UPVALNUM_OWNER on RDX_UPVALNUM (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_UPVALRAW_OWNER on RDX_UPVALRAW (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_UPVALREF_OWNER on RDX_UPVALREF (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_UPVALSTR_OWNER on RDX_UPVALSTR (OWNERENTITYID asc, OWNERPID asc)
/

create index IDX_RDX_USERDEFTEST_TESTED on RDX_USERDEFTEST (TESTPASSED asc, TITLE asc)
/

create unique index IDX_RDX_USERFUNC_SEQID on RDX_USERFUNC (ID asc)
/

create index IDX_RDX_WEBSETTING_USERTIME on RDX_WEBSETTING (USERNAME asc, LASTCHANGETIME asc)
/

create index IDX_RDX_WF_FORM_CLERKCLOSEDUE on RDX_WF_FORM (CLERKNAME asc, CLOSETIME asc, DUETIME asc)
/

create index IDX_RDX_WF_FORM_CLOSEDUE on RDX_WF_FORM (CLOSETIME asc, DUETIME asc)
/

create index IDX_RDX_WF_FORM_PROCESS on RDX_WF_FORM (PROCESSID asc, ID asc)
/

create index IDX_RDX_WF_PROCESSLINK_OBJECT on RDX_WF_PROCESSLINK (ENTITYGUID asc, OBJECTPID asc, ROLE asc, PROCESSID asc)
/

create unique index IDX_RDX_WF_PROCESSLINK_PROCESS on RDX_WF_PROCESSLINK (PROCESSID asc, ROLE asc, IDX asc, ID asc)
/

create index IDX_RDX_WF_PROCESS_OWNERSTATEF on RDX_WF_PROCESS (OWNERNAME asc, STATE asc, FINISHTIME asc)
/

create index IDX_RDX_WF_PROCESS_OWNERSTATES on RDX_WF_PROCESS (OWNERNAME asc, STATE asc, STARTTIME asc)
/

create index IDX_RDX_WF_PROCESS_PURGE on RDX_WF_PROCESS (PURGETIME asc)
/

create index IDX_RDX_WF_PROCESS_STATEFINISH on RDX_WF_PROCESS (STATE asc, FINISHTIME asc)
/

create index IDX_RDX_WF_PROCESS_STATESTART on RDX_WF_PROCESS (STATE asc, STARTTIME asc)
/

create index IDX_RDX_WF_PROCESS_TYPESTATEFI on RDX_WF_PROCESS (TYPEGUID asc, STATE asc, FINISHTIME asc)
/

create index IDX_RDX_WF_PROCESS_TYPESTATEST on RDX_WF_PROCESS (TYPEGUID asc, STATE asc, STARTTIME asc)
/

create unique index PK_COUNTER on COUNTER (ID asc)
/

create unique index PK_RDX_AC_APPROLE on RDX_AC_APPROLE (GUID asc)
/

create unique index PK_RDX_AC_USER on RDX_AC_USER (NAME asc)
/

create unique index PK_RDX_AC_USER2ROLE on RDX_AC_USER2ROLE (ID asc)
/

create unique index PK_RDX_AC_USER2USERGROUP on RDX_AC_USER2USERGROUP (USERNAME asc, GROUPNAME asc)
/

create unique index PK_RDX_AC_USERGROUP on RDX_AC_USERGROUP (NAME asc)
/

create unique index PK_RDX_AC_USERGROUP2ROLE on RDX_AC_USERGROUP2ROLE (ID asc)
/

create unique index PK_RDX_ARTEINSTANCE on RDX_ARTEINSTANCE (INSTANCEID asc, SERIAL asc)
/

create unique index PK_RDX_ARTEUNIT on RDX_ARTEUNIT (ID asc)
/

create unique index PK_RDX_AU_AUDITLOG on RDX_AU_AUDITLOG (EVENTTIME asc, ID asc, STOREDURATION asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

create unique index PK_RDX_AU_SCHEME on RDX_AU_SCHEME (GUID asc)
/

create unique index PK_RDX_AU_SCHEMEITEM on RDX_AU_SCHEMEITEM (SCHEMEGUID asc, TABLEID asc, EVENTTYPE asc)
/

create unique index PK_RDX_CLASSANCESTOR on RDX_CLASSANCESTOR (VERSIONNUM asc, CLASSID asc, ANCESTORID asc)
/

create unique index PK_RDX_CLASSLOADINGPROFILE on RDX_CLASSLOADINGPROFILE (ID asc)
/

create unique index PK_RDX_CLASSLOADINGPROFILEITEM on RDX_CLASSLOADINGPROFILEITEM (ID asc)
/

create unique index PK_RDX_CM_ITEM on RDX_CM_ITEM (ID asc)
/

create unique index PK_RDX_CM_ITEMREF on RDX_CM_ITEMREF (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc)
/

create unique index PK_RDX_CM_PACKET on RDX_CM_PACKET (ID asc)
/

create unique index PK_RDX_DEF2DOMAIN on RDX_DEF2DOMAIN (VERSIONNUM asc, DEFID asc, DOMAINID asc)
/

create unique index PK_RDX_DM_DATA on RDX_DM_DATA (PACKETID asc, ENTITYGUID asc, OBJPID asc, PROPGUID asc)
/

create unique index PK_RDX_DM_OUTLINK on RDX_DM_OUTLINK (PACKETID asc, EXTTABLEGUID asc, PARENTPID asc)
/

create unique index PK_RDX_DM_OUTLINKVERIFY on RDX_DM_OUTLINKVERIFY (PACKETID asc, EXTTABLEGUID asc, PARENTPID asc, PARENTPROPGUID asc)
/

create unique index PK_RDX_DM_PACKET on RDX_DM_PACKET (ID asc)
/

create unique index PK_RDX_DM_SEGMENT on RDX_DM_SEGMENT (PACKETID asc, SEGMENTGUID asc)
/

create unique index PK_RDX_EASSELECTORADDONS on RDX_EASSELECTORADDONS (GUID asc)
/

create unique index PK_RDX_EASSESSION on RDX_EASSESSION (ID asc)
/

create unique index PK_RDX_ENTITYARRUSERREF on RDX_ENTITYARRUSERREF (VERSIONNUM asc, TABLEID asc, USERPROPID asc)
/

create unique index PK_RDX_ENTITYUSERREF on RDX_ENTITYUSERREF (VERSIONNUM asc, TABLEID asc, USERPROPID asc)
/

create unique index PK_RDX_ENUMITEM2DOMAIN on RDX_ENUMITEM2DOMAIN (VERSIONNUM asc, ENUMID asc, DOMAINID asc, ENUMITEMVALASSTR asc)
/

create unique index PK_RDX_EVENTCODE2EVENTPARAMS on RDX_EVENTCODE2EVENTPARAMS (EVENTCODE asc, VERSIONNUM asc)
/

create unique index PK_RDX_EVENTCONTEXT on RDX_EVENTCONTEXT (RAISETIME asc, EVENTID asc, TYPE asc, ID asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

create unique index PK_RDX_EVENTLOG on RDX_EVENTLOG (RAISETIME asc, ID asc)
#IF DB_TYPE == "ORACLE" and isEnabled("org.radixware\\Partitioning") THEN
 local
#ENDIF

/

create unique index PK_RDX_EVENTSEVERITY on RDX_EVENTSEVERITY (EVENTCODE asc)
/

create unique index PK_RDX_FALLBACKMQHANDLER on RDX_FALLBACKMQHANDLER (UNITID asc)
/

create unique index PK_RDX_INSTANCE on RDX_INSTANCE (ID asc)
/

create unique index PK_RDX_JMSHANDLER on RDX_JMSHANDLER (ID asc)
/

create unique index PK_RDX_JS_CALENDAR on RDX_JS_CALENDAR (ID asc)
/

create unique index PK_RDX_JS_CALENDARITEM on RDX_JS_CALENDARITEM (ID asc)
/

create unique index PK_RDX_JS_EVENTSCHD on RDX_JS_EVENTSCHD (ID asc)
/

create unique index PK_RDX_JS_EVENTSCHDITEM on RDX_JS_EVENTSCHDITEM (SCHEDULEID asc, ID asc)
/

create unique index PK_RDX_JS_INTERVALSCHD on RDX_JS_INTERVALSCHD (ID asc)
/

create unique index PK_RDX_JS_INTERVALSCHDITEM on RDX_JS_INTERVALSCHDITEM (SCHEDULEID asc, ID asc)
/

create unique index PK_RDX_JS_JOBEXECUTORPRIORITYM on RDX_JS_JOBEXECUTORPRIORITYMAP (APPPRIORITY asc)
/

create unique index PK_RDX_JS_JOBEXECUTORUNIT on RDX_JS_JOBEXECUTORUNIT (ID asc)
/

create unique index PK_RDX_JS_JOBEXECUTORUNITBOOST on RDX_JS_JOBEXECUTORUNITBOOST (SPEED asc)
/

create unique index PK_RDX_JS_JOBPARAM on RDX_JS_JOBPARAM (JOBID asc, NAME asc)
/

create unique index PK_RDX_JS_JOBQUEUE on RDX_JS_JOBQUEUE (ID asc)
/

create unique index PK_RDX_JS_JOBSCHEDULERUNIT on RDX_JS_JOBSCHEDULERUNIT (ID asc)
/

create unique index PK_RDX_JS_TASK on RDX_JS_TASK (ID asc)
/

create unique index PK_RDX_KAFKAQUEUE on RDX_KAFKAQUEUE (QUEUEID asc)
/

create unique index PK_RDX_LIBUSERFUNC on RDX_LIBUSERFUNC (GUID asc)
/

create unique index PK_RDX_LICENSEREPORTLOG on RDX_LICENSEREPORTLOG (ID asc)
/

create unique index PK_RDX_MESSAGEQUEUE on RDX_MESSAGEQUEUE (ID asc)
/

create unique index PK_RDX_MESSAGEQUEUEPROCESSOR on RDX_MESSAGEQUEUEPROCESSOR (ID asc)
/

create unique index PK_RDX_MSDLSCHEME on RDX_MSDLSCHEME (GUID asc)
/

create unique index PK_RDX_NETCHANNEL on RDX_NETCHANNEL (ID asc)
/

create unique index PK_RDX_NETHUB on RDX_NETHUB (ID asc)
/

create unique index PK_RDX_NETPORTHANDLER on RDX_NETPORTHANDLER (ID asc)
/

create unique index PK_RDX_PC_ATTACHMENT on RDX_PC_ATTACHMENT (MESSID asc, SEQ asc)
/

create unique index PK_RDX_PC_CHANNELGSMMODEM on RDX_PC_CHANNELGSMMODEM (CHANNELUNITID asc, ID asc)
/

create unique index PK_RDX_PC_CHANNELHANDLER on RDX_PC_CHANNELHANDLER (UNITID asc, SEQ asc)
/

create unique index PK_RDX_PC_CHANNELUNIT on RDX_PC_CHANNELUNIT (ID asc)
/

create unique index PK_RDX_PC_EVENTLIMITACC on RDX_PC_EVENTLIMITACC (SUBSCRIPTIONID asc, USERNAME asc, EVENTCODE asc, CHANNELKIND asc)
/

create unique index PK_RDX_PC_EVENTSUBSCRIPTION on RDX_PC_EVENTSUBSCRIPTION (ID asc)
/

create unique index PK_RDX_PC_EVENTSUBSCRIPTIONCOD on RDX_PC_EVENTSUBSCRIPTIONCODE (SUBSCRIPTIONID asc, CODE asc)
/

create unique index PK_RDX_PC_OUTMESSAGE on RDX_PC_OUTMESSAGE (ID asc)
/

create unique index PK_RDX_PC_RECVMESSAGE on RDX_PC_RECVMESSAGE (ID asc)
/

create unique index PK_RDX_PC_SENTMESSAGE on RDX_PC_SENTMESSAGE (ID asc)
/

create unique index PK_RDX_PROFILERLOG on RDX_PROFILERLOG (ID asc)
/

create unique index PK_RDX_REPORTPARAM on RDX_REPORTPARAM (ID asc)
/

create unique index PK_RDX_REPORTPUB on RDX_REPORTPUB (ID asc)
/

create unique index PK_RDX_REPORTPUBLIST on RDX_REPORTPUBLIST (ID asc)
/

create unique index PK_RDX_REPORTPUBTOPIC on RDX_REPORTPUBTOPIC (ID asc)
/

create unique index PK_RDX_SAP on RDX_SAP (ID asc)
/

create unique index PK_RDX_SB_DATASCHEME on RDX_SB_DATASCHEME (URI asc)
/

create unique index PK_RDX_SB_PIPELINE on RDX_SB_PIPELINE (ID asc)
/

create unique index PK_RDX_SB_PIPELINECONNECTOR on RDX_SB_PIPELINECONNECTOR (ROLE asc, NODEID asc)
/

create unique index PK_RDX_SB_PIPELINENODE on RDX_SB_PIPELINENODE (ID asc)
/

create unique index PK_RDX_SB_PIPELINENODEPARAM on RDX_SB_PIPELINENODEPARAM (NODEID asc, NAME asc)
/

create unique index PK_RDX_SB_PIPELINEPARAM on RDX_SB_PIPELINEPARAM (PIPELINEID asc, NAME asc)
/

create unique index PK_RDX_SB_PIPELINEVER on RDX_SB_PIPELINEVER (PIPELINEID asc, CREATETIME asc)
/

create unique index PK_RDX_SB_TRANSFORMSTAGE on RDX_SB_TRANSFORMSTAGE (ID asc)
/

create unique index PK_RDX_SB_XPATHTABLE on RDX_SB_XPATHTABLE (ID asc)
/

create unique index PK_RDX_SCP on RDX_SCP (NAME asc, SYSTEMID asc)
/

create unique index PK_RDX_SCP2SAP on RDX_SCP2SAP (SYSTEMID asc, SAPID asc, SCPNAME asc)
/

create unique index PK_RDX_SERVICE on RDX_SERVICE (SYSTEMID asc, URI asc)
/

create unique index PK_RDX_SM_DASHCONFIG on RDX_SM_DASHCONFIG (GUID asc)
/

create unique index PK_RDX_SM_METRICCONTROLDATA on RDX_SM_METRICCONTROLDATA (STATEID asc)
/

create unique index PK_RDX_SM_METRICHIST on RDX_SM_METRICHIST (METRICID asc, ENDTIME asc, ID asc)
/

create unique index PK_RDX_SM_METRICSTATE on RDX_SM_METRICSTATE (ID asc)
/

create unique index PK_RDX_SM_METRICTYPE on RDX_SM_METRICTYPE (ID asc)
/

create unique index PK_RDX_SM_SNMPAGENTUNIT on RDX_SM_SNMPAGENTUNIT (ID asc)
/

create unique index PK_RDX_SM_SNMPMANAGER on RDX_SM_SNMPMANAGER (ID asc)
/

create unique index PK_RDX_STATION on RDX_STATION (NAME asc)
/

create unique index PK_RDX_SYSTEM on RDX_SYSTEM (ID asc)
/

create unique index PK_RDX_TESTCASE on RDX_TESTCASE (ID asc)
/

create unique index PK_RDX_TST_AUDITDETAIL on RDX_TST_AUDITDETAIL (PKCOLINT asc, PKCOLCHAR asc, PKCOLNUM asc, PKCOLDATE asc, PKCOLSTR asc, PKCOLTIMESTAMP asc)
/

create unique index PK_RDX_TST_AUDITMASTER on RDX_TST_AUDITMASTER (PKCOLINT asc, PKCOLCHAR asc, PKCOLNUM asc, PKCOLDATE asc, PKCOLSTR asc, PKCOLTIMESTAMP asc)
/

create unique index PK_RDX_TST_CHILD on RDX_TST_CHILD (ID asc)
/

create unique index PK_RDX_TST_DMTEST on RDX_TST_DMTEST (ID asc)
/

create unique index PK_RDX_TST_DMTESTA on RDX_TST_DMTESTA (PKINT asc, PKSTR asc)
/

create unique index PK_RDX_TST_DMTESTB on RDX_TST_DMTESTB (PKINT asc, PKSTR asc)
/

create unique index PK_RDX_TST_DMTESTCHILD on RDX_TST_DMTESTCHILD (ID asc)
/

create unique index PK_RDX_TST_IMAGES on RDX_TST_IMAGES (ID asc)
/

create unique index PK_RDX_TST_INHERITANCENODE on RDX_TST_INHERITANCENODE (ID asc)
/

create unique index PK_RDX_TST_NEWTABLE on RDX_TST_NEWTABLE (NEWCOLUMN asc)
/

create unique index PK_RDX_TST_PARENT on RDX_TST_PARENT (ID asc)
/

create unique index PK_RDX_TST_TABLEWITHDATEPK on RDX_TST_TABLEWITHDATEPK (DATEPK asc)
/

create unique index PK_RDX_TST_TEDCCHILD on RDX_TST_TEDCCHILD (ID asc)
/

create unique index PK_RDX_TST_TEDCCHILDDETAIL on RDX_TST_TEDCCHILDDETAIL (ID asc)
/

create unique index PK_RDX_TST_TEDCPARENT on RDX_TST_TEDCPARENT (ID asc)
/

create unique index PK_RDX_TST_TESTPAIR on RDX_TST_TESTPAIR (KEY asc, VALUE asc)
/

create unique index PK_RDX_TST_USERCHILD on RDX_TST_USERCHILD (ID asc)
/

create unique index PK_RDX_TST_USERPARENTCOMPLEX on RDX_TST_USERPARENTCOMPLEX (PK_INT asc, PK_CHAR asc, PK_DATE asc, PK_STR asc)
/

create unique index PK_RDX_TST_USERPARENTSIMPLE on RDX_TST_USERPARENTSIMPLE (ID asc)
/

create unique index PK_RDX_UNIT on RDX_UNIT (ID asc)
/

create unique index PK_RDX_UPVALBLOB on RDX_UPVALBLOB (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_UPVALCLOB on RDX_UPVALCLOB (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_UPVALDATETIME on RDX_UPVALDATETIME (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_UPVALNUM on RDX_UPVALNUM (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_UPVALRAW on RDX_UPVALRAW (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_UPVALREF on RDX_UPVALREF (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_UPVALSTR on RDX_UPVALSTR (DEFID asc, OWNERENTITYID asc, OWNERPID asc)
/

create unique index PK_RDX_USER2STATION on RDX_USER2STATION (STATIONNAME asc, USERNAME asc)
/

create unique index PK_RDX_USERDEFTEST on RDX_USERDEFTEST (CLAZZGUID asc)
/

create unique index PK_RDX_USERFUNC on RDX_USERFUNC (UPDEFID asc, UPOWNERENTITYID asc, UPOWNERPID asc)
/

create unique index PK_RDX_USERFUNCLIB on RDX_USERFUNCLIB (NAME asc)
/

create unique index PK_RDX_USERFUNCLIBPARAM on RDX_USERFUNCLIBPARAM (LIBNAME asc, NAME asc)
/

create unique index PK_RDX_USERREPORT on RDX_USERREPORT (GUID asc)
/

create unique index PK_RDX_USERREPORTMODULE on RDX_USERREPORTMODULE (GUID asc)
/

create unique index PK_RDX_USERREPORTVERSION on RDX_USERREPORTVERSION (REPORTGUID asc, VERSION asc)
/

create unique index PK_RDX_WEBSETTING on RDX_WEBSETTING (USERNAME asc, PROPNAME asc)
/

create unique index PK_RDX_WF_FORM on RDX_WF_FORM (ID asc)
/

create unique index PK_RDX_WF_FORMLOG on RDX_WF_FORMLOG (FORMID asc, ID asc)
/

create unique index PK_RDX_WF_PROCESS on RDX_WF_PROCESS (ID asc)
/

create unique index PK_RDX_WF_PROCESSLINK on RDX_WF_PROCESSLINK (ID asc)
/

create unique index PK_RDX_WF_PROCESSTYPE on RDX_WF_PROCESSTYPE (GUID asc)
/

alter table COUNTER add constraint PK_COUNTER primary key (ID) rely
/

alter table RDX_AC_APPROLE add constraint PK_RDX_AC_APPROLE primary key (GUID) rely
/

alter table RDX_AC_USER add constraint PK_RDX_AC_USER primary key (NAME) rely
/

alter table RDX_AC_USER2ROLE add constraint PK_RDX_AC_USER2ROLE primary key (ID) rely
/

alter table RDX_AC_USER2USERGROUP add constraint PK_RDX_AC_USER2USERGROUP primary key (USERNAME, GROUPNAME) rely
/

alter table RDX_AC_USERGROUP add constraint PK_RDX_AC_USERGROUP primary key (NAME) rely
/

alter table RDX_AC_USERGROUP2ROLE add constraint PK_RDX_AC_USERGROUP2ROLE primary key (ID) rely
/

alter table RDX_ARTEINSTANCE add constraint PK_RDX_ARTEINSTANCE primary key (INSTANCEID, SERIAL) rely
/

alter table RDX_ARTEUNIT add constraint PK_RDX_ARTEUNIT primary key (ID) rely
/

alter table RDX_AU_AUDITLOG add constraint PK_RDX_AU_AUDITLOG primary key (EVENTTIME, ID, STOREDURATION) rely
/

alter table RDX_AU_SCHEME add constraint PK_RDX_AU_SCHEME primary key (GUID) rely
/

alter table RDX_AU_SCHEMEITEM add constraint PK_RDX_AU_SCHEMEITEM primary key (SCHEMEGUID, TABLEID, EVENTTYPE) rely
/

alter table RDX_CLASSANCESTOR add constraint PK_RDX_CLASSANCESTOR primary key (VERSIONNUM, CLASSID, ANCESTORID) rely
/

alter table RDX_CLASSLOADINGPROFILE add constraint PK_RDX_CLASSLOADINGPROFILE primary key (ID) rely
/

alter table RDX_CLASSLOADINGPROFILEITEM add constraint PK_RDX_CLASSLOADINGPROFILEITEM primary key (ID) rely
/

alter table RDX_CM_ITEM add constraint PK_RDX_CM_ITEM primary key (ID) rely
/

alter table RDX_CM_ITEMREF add constraint PK_RDX_CM_ITEMREF primary key (UPDEFID, UPOWNERENTITYID, UPOWNERPID) rely
/

alter table RDX_CM_PACKET add constraint PK_RDX_CM_PACKET primary key (ID) rely
/

alter table RDX_DEF2DOMAIN add constraint PK_RDX_DEF2DOMAIN primary key (VERSIONNUM, DEFID, DOMAINID) rely
/

alter table RDX_DM_DATA add constraint PK_RDX_DM_DATA primary key (PACKETID, ENTITYGUID, OBJPID, PROPGUID) rely
/

alter table RDX_DM_OUTLINK add constraint PK_RDX_DM_OUTLINK primary key (PACKETID, EXTTABLEGUID, PARENTPID) rely
/

alter table RDX_DM_OUTLINKVERIFY add constraint PK_RDX_DM_OUTLINKVERIFY primary key (PACKETID, EXTTABLEGUID, PARENTPID, PARENTPROPGUID) rely
/

alter table RDX_DM_PACKET add constraint PK_RDX_DM_PACKET primary key (ID) rely
/

alter table RDX_DM_SEGMENT add constraint PK_RDX_DM_SEGMENT primary key (PACKETID, SEGMENTGUID) rely
/

alter table RDX_EASSELECTORADDONS add constraint PK_RDX_EASSELECTORADDONS primary key (GUID) rely
/

alter table RDX_EASSESSION add constraint PK_RDX_EASSESSION primary key (ID) rely
/

alter table RDX_ENTITYARRUSERREF add constraint PK_RDX_ENTITYARRUSERREF primary key (VERSIONNUM, TABLEID, USERPROPID) rely
/

alter table RDX_ENTITYUSERREF add constraint PK_RDX_ENTITYUSERREF primary key (VERSIONNUM, TABLEID, USERPROPID) rely
/

alter table RDX_ENUMITEM2DOMAIN add constraint PK_RDX_ENUMITEM2DOMAIN primary key (VERSIONNUM, ENUMID, DOMAINID, ENUMITEMVALASSTR) rely
/

alter table RDX_EVENTCODE2EVENTPARAMS add constraint PK_RDX_EVENTCODE2EVENTPARAMS primary key (EVENTCODE, VERSIONNUM) rely
/

alter table RDX_EVENTCONTEXT add constraint PK_RDX_EVENTCONTEXT primary key (RAISETIME, EVENTID, TYPE, ID) rely
/

alter table RDX_EVENTLOG add constraint PK_RDX_EVENTLOG primary key (RAISETIME, ID) rely
/

alter table RDX_EVENTSEVERITY add constraint PK_RDX_EVENTSEVERITY primary key (EVENTCODE) rely
/

alter table RDX_FALLBACKMQHANDLER add constraint PK_RDX_FALLBACKMQHANDLER primary key (UNITID) rely
/

alter table RDX_INSTANCE add constraint PK_RDX_INSTANCE primary key (ID) rely
/

alter table RDX_JMSHANDLER add constraint PK_RDX_JMSHANDLER primary key (ID) rely
/

alter table RDX_JS_CALENDAR add constraint PK_RDX_JS_CALENDAR primary key (ID) rely
/

alter table RDX_JS_CALENDARITEM add constraint PK_RDX_JS_CALENDARITEM primary key (ID) rely
/

alter table RDX_JS_EVENTSCHD add constraint PK_RDX_JS_EVENTSCHD primary key (ID) rely
/

alter table RDX_JS_EVENTSCHDITEM add constraint PK_RDX_JS_EVENTSCHDITEM primary key (SCHEDULEID, ID) rely
/

alter table RDX_JS_INTERVALSCHD add constraint PK_RDX_JS_INTERVALSCHD primary key (ID) rely
/

alter table RDX_JS_INTERVALSCHDITEM add constraint PK_RDX_JS_INTERVALSCHDITEM primary key (SCHEDULEID, ID) rely
/

alter table RDX_JS_JOBEXECUTORPRIORITYMAP add constraint PK_RDX_JS_JOBEXECUTORPRIORITYM primary key (APPPRIORITY) rely
/

alter table RDX_JS_JOBEXECUTORUNIT add constraint PK_RDX_JS_JOBEXECUTORUNIT primary key (ID) rely
/

alter table RDX_JS_JOBEXECUTORUNITBOOST add constraint PK_RDX_JS_JOBEXECUTORUNITBOOST primary key (SPEED) rely
/

alter table RDX_JS_JOBPARAM add constraint PK_RDX_JS_JOBPARAM primary key (JOBID, NAME) rely
/

alter table RDX_JS_JOBQUEUE add constraint PK_RDX_JS_JOBQUEUE primary key (ID) rely
/

alter table RDX_JS_JOBSCHEDULERUNIT add constraint PK_RDX_JS_JOBSCHEDULERUNIT primary key (ID) rely
/

alter table RDX_JS_TASK add constraint PK_RDX_JS_TASK primary key (ID) rely
/

alter table RDX_KAFKAQUEUE add constraint PK_RDX_KAFKAQUEUE primary key (QUEUEID) rely
/

alter table RDX_LIBUSERFUNC add constraint PK_RDX_LIBUSERFUNC primary key (GUID) rely
/

alter table RDX_LICENSEREPORTLOG add constraint PK_RDX_LICENSEREPORTLOG primary key (ID) rely
/

alter table RDX_MESSAGEQUEUE add constraint PK_RDX_MESSAGEQUEUE primary key (ID) rely
/

alter table RDX_MESSAGEQUEUEPROCESSOR add constraint PK_RDX_MESSAGEQUEUEPROCESSOR primary key (ID) rely
/

alter table RDX_MSDLSCHEME add constraint PK_RDX_MSDLSCHEME primary key (GUID) rely
/

alter table RDX_NETCHANNEL add constraint PK_RDX_NETCHANNEL primary key (ID) rely
/

alter table RDX_NETHUB add constraint PK_RDX_NETHUB primary key (ID) rely
/

alter table RDX_NETPORTHANDLER add constraint PK_RDX_NETPORTHANDLER primary key (ID) rely
/

alter table RDX_PC_ATTACHMENT add constraint PK_RDX_PC_ATTACHMENT primary key (MESSID, SEQ) rely
/

alter table RDX_PC_CHANNELGSMMODEM add constraint PK_RDX_PC_CHANNELGSMMODEM primary key (CHANNELUNITID, ID) rely
/

alter table RDX_PC_CHANNELHANDLER add constraint PK_RDX_PC_CHANNELHANDLER primary key (UNITID, SEQ) rely
/

alter table RDX_PC_CHANNELUNIT add constraint PK_RDX_PC_CHANNELUNIT primary key (ID) rely
/

alter table RDX_PC_EVENTLIMITACC add constraint PK_RDX_PC_EVENTLIMITACC primary key (SUBSCRIPTIONID, USERNAME, EVENTCODE, CHANNELKIND) rely
/

alter table RDX_PC_EVENTSUBSCRIPTION add constraint PK_RDX_PC_EVENTSUBSCRIPTION primary key (ID) rely
/

alter table RDX_PC_EVENTSUBSCRIPTIONCODE add constraint PK_RDX_PC_EVENTSUBSCRIPTIONCOD primary key (SUBSCRIPTIONID, CODE) rely
/

alter table RDX_PC_OUTMESSAGE add constraint PK_RDX_PC_OUTMESSAGE primary key (ID) rely
/

alter table RDX_PC_RECVMESSAGE add constraint PK_RDX_PC_RECVMESSAGE primary key (ID) rely
/

alter table RDX_PC_SENTMESSAGE add constraint PK_RDX_PC_SENTMESSAGE primary key (ID) rely
/

alter table RDX_PROFILERLOG add constraint PK_RDX_PROFILERLOG primary key (ID) rely
/

alter table RDX_REPORTPARAM add constraint PK_RDX_REPORTPARAM primary key (ID) rely
/

alter table RDX_REPORTPUB add constraint PK_RDX_REPORTPUB primary key (ID) rely
/

alter table RDX_REPORTPUBLIST add constraint PK_RDX_REPORTPUBLIST primary key (ID) rely
/

alter table RDX_REPORTPUBTOPIC add constraint PK_RDX_REPORTPUBTOPIC primary key (ID) rely
/

alter table RDX_SAP add constraint PK_RDX_SAP primary key (ID) rely
/

alter table RDX_SB_DATASCHEME add constraint PK_RDX_SB_DATASCHEME primary key (URI) rely
/

alter table RDX_SB_PIPELINE add constraint PK_RDX_SB_PIPELINE primary key (ID) rely
/

alter table RDX_SB_PIPELINECONNECTOR add constraint PK_RDX_SB_PIPELINECONNECTOR primary key (ROLE, NODEID) rely
/

alter table RDX_SB_PIPELINENODE add constraint PK_RDX_SB_PIPELINENODE primary key (ID) rely
/

alter table RDX_SB_PIPELINENODEPARAM add constraint PK_RDX_SB_PIPELINENODEPARAM primary key (NODEID, NAME) rely
/

alter table RDX_SB_PIPELINEPARAM add constraint PK_RDX_SB_PIPELINEPARAM primary key (PIPELINEID, NAME) rely
/

alter table RDX_SB_PIPELINEVER add constraint PK_RDX_SB_PIPELINEVER primary key (PIPELINEID, CREATETIME) rely
/

alter table RDX_SB_TRANSFORMSTAGE add constraint PK_RDX_SB_TRANSFORMSTAGE primary key (ID) rely
/

alter table RDX_SB_XPATHTABLE add constraint PK_RDX_SB_XPATHTABLE primary key (ID) rely
/

alter table RDX_SCP add constraint PK_RDX_SCP primary key (NAME, SYSTEMID) rely
/

alter table RDX_SCP2SAP add constraint PK_RDX_SCP2SAP primary key (SYSTEMID, SAPID, SCPNAME) rely
/

alter table RDX_SERVICE add constraint PK_RDX_SERVICE primary key (SYSTEMID, URI) rely
/

alter table RDX_SM_DASHCONFIG add constraint PK_RDX_SM_DASHCONFIG primary key (GUID) rely
/

alter table RDX_SM_METRICCONTROLDATA add constraint PK_RDX_SM_METRICCONTROLDATA primary key (STATEID) rely
/

alter table RDX_SM_METRICHIST add constraint PK_RDX_SM_METRICHIST primary key (METRICID, ENDTIME, ID) rely
/

alter table RDX_SM_METRICSTATE add constraint PK_RDX_SM_METRICSTATE primary key (ID) rely
/

alter table RDX_SM_METRICTYPE add constraint PK_RDX_SM_METRICTYPE primary key (ID) rely
/

alter table RDX_SM_SNMPAGENTUNIT add constraint PK_RDX_SM_SNMPAGENTUNIT primary key (ID) rely
/

alter table RDX_SM_SNMPMANAGER add constraint PK_RDX_SM_SNMPMANAGER primary key (ID) rely
/

alter table RDX_STATION add constraint PK_RDX_STATION primary key (NAME) rely
/

alter table RDX_SYSTEM add constraint PK_RDX_SYSTEM primary key (ID) rely
/

alter table RDX_TESTCASE add constraint PK_RDX_TESTCASE primary key (ID) rely
/

alter table RDX_TST_AUDITDETAIL add constraint PK_RDX_TST_AUDITDETAIL primary key (PKCOLINT, PKCOLCHAR, PKCOLNUM, PKCOLDATE, PKCOLSTR, PKCOLTIMESTAMP) rely
/

alter table RDX_TST_AUDITMASTER add constraint PK_RDX_TST_AUDITMASTER primary key (PKCOLINT, PKCOLCHAR, PKCOLNUM, PKCOLDATE, PKCOLSTR, PKCOLTIMESTAMP) rely
/

alter table RDX_TST_CHILD add constraint PK_RDX_TST_CHILD primary key (ID) rely
/

alter table RDX_TST_DMTEST add constraint PK_RDX_TST_DMTEST primary key (ID) rely
/

alter table RDX_TST_DMTESTA add constraint PK_RDX_TST_DMTESTA primary key (PKINT, PKSTR) rely
/

alter table RDX_TST_DMTESTB add constraint PK_RDX_TST_DMTESTB primary key (PKINT, PKSTR) rely
/

alter table RDX_TST_DMTESTCHILD add constraint PK_RDX_TST_DMTESTCHILD primary key (ID) rely
/

alter table RDX_TST_IMAGES add constraint PK_RDX_TST_IMAGES primary key (ID) rely
/

alter table RDX_TST_INHERITANCENODE add constraint PK_RDX_TST_INHERITANCENODE primary key (ID) rely
/

alter table RDX_TST_NEWTABLE add constraint PK_RDX_TST_NEWTABLE primary key (NEWCOLUMN) rely
/

alter table RDX_TST_PARENT add constraint PK_RDX_TST_PARENT primary key (ID) rely
/

alter table RDX_TST_TABLEWITHDATEPK add constraint PK_RDX_TST_TABLEWITHDATEPK primary key (DATEPK) rely
/

alter table RDX_TST_TEDCCHILD add constraint PK_RDX_TST_TEDCCHILD primary key (ID) rely
/

alter table RDX_TST_TEDCCHILDDETAIL add constraint PK_RDX_TST_TEDCCHILDDETAIL primary key (ID) rely
/

alter table RDX_TST_TEDCPARENT add constraint PK_RDX_TST_TEDCPARENT primary key (ID) rely
/

alter table RDX_TST_TESTPAIR add constraint PK_RDX_TST_TESTPAIR primary key (KEY, VALUE) rely
/

alter table RDX_TST_USERCHILD add constraint PK_RDX_TST_USERCHILD primary key (ID) rely
/

alter table RDX_TST_USERPARENTCOMPLEX add constraint PK_RDX_TST_USERPARENTCOMPLEX primary key (PK_INT, PK_CHAR, PK_DATE, PK_STR) rely
/

alter table RDX_TST_USERPARENTSIMPLE add constraint PK_RDX_TST_USERPARENTSIMPLE primary key (ID) rely
/

alter table RDX_UNIT add constraint PK_RDX_UNIT primary key (ID) rely
/

alter table RDX_UPVALBLOB add constraint PK_RDX_UPVALBLOB primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_UPVALCLOB add constraint PK_RDX_UPVALCLOB primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_UPVALDATETIME add constraint PK_RDX_UPVALDATETIME primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_UPVALNUM add constraint PK_RDX_UPVALNUM primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_UPVALRAW add constraint PK_RDX_UPVALRAW primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_UPVALREF add constraint PK_RDX_UPVALREF primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_UPVALSTR add constraint PK_RDX_UPVALSTR primary key (DEFID, OWNERENTITYID, OWNERPID) rely
/

alter table RDX_USER2STATION add constraint PK_RDX_USER2STATION primary key (STATIONNAME, USERNAME) rely
/

alter table RDX_USERDEFTEST add constraint PK_RDX_USERDEFTEST primary key (CLAZZGUID) rely
/

alter table RDX_USERFUNC add constraint PK_RDX_USERFUNC primary key (UPDEFID, UPOWNERENTITYID, UPOWNERPID) rely
/

alter table RDX_USERFUNCLIB add constraint PK_RDX_USERFUNCLIB primary key (NAME) rely
/

alter table RDX_USERFUNCLIBPARAM add constraint PK_RDX_USERFUNCLIBPARAM primary key (LIBNAME, NAME) rely
/

alter table RDX_USERREPORT add constraint PK_RDX_USERREPORT primary key (GUID) rely
/

alter table RDX_USERREPORTMODULE add constraint PK_RDX_USERREPORTMODULE primary key (GUID) rely
/

alter table RDX_USERREPORTVERSION add constraint PK_RDX_USERREPORTVERSION primary key (REPORTGUID, VERSION) rely
/

alter table RDX_WEBSETTING add constraint PK_RDX_WEBSETTING primary key (USERNAME, PROPNAME) rely
/

alter table RDX_WF_FORM add constraint PK_RDX_WF_FORM primary key (ID) rely
/

alter table RDX_WF_FORMLOG add constraint PK_RDX_WF_FORMLOG primary key (FORMID, ID) rely
/

alter table RDX_WF_PROCESS add constraint PK_RDX_WF_PROCESS primary key (ID) rely
/

alter table RDX_WF_PROCESSLINK add constraint PK_RDX_WF_PROCESSLINK primary key (ID) rely
/

alter table RDX_WF_PROCESSTYPE add constraint PK_RDX_WF_PROCESSTYPE primary key (GUID) rely
/

alter table RDX_AC_USER2ROLE add constraint UNQ_RDX_AC_USER2ROLE_REPLACEME unique (REPLACEDID) rely
/

alter table RDX_AC_USERGROUP2ROLE add constraint UNQ_RDX_AC_USERGROUP2ROLE_REPL unique (REPLACEDID) rely
/

alter table RDX_REPORTPUBLIST add constraint UNQ_RDX_REPORTPUBLIST_CONTEXT unique (CLASSGUID, PUBCONTEXTCLASSGUID, CONTEXTPID) rely
/

alter table RDX_SYSTEM add constraint UNQ_RDX_SYSTEM_NAME unique (NAME) rely
/

alter table RDX_WF_PROCESSLINK add constraint UNQ_RDX_WF_PROCESSLINK_PROCESS unique (PROCESSID, ROLE, IDX, ID)
/

create or replace view RDX_EASSELECTORADDONSTABLES as
Select Distinct RDX_EASSELECTORADDONS.TABLEGUID TABLEGUID, RDX_EASSELECTORADDONS.CLASSGUID ADDONCLASSGUID From RDX_EASSELECTORADDONS
with read only
/

grant select on RDX_EASSELECTORADDONSTABLES to &USER&_RUN_ROLE
/

create or replace view RDX_EVENTLOGCONTEXT as
Select 
   RDX_EVENTCONTEXT.TYPE TYPE,
   RDX_EVENTCONTEXT.ID CONTEXTID, 
   RDX_EVENTCONTEXT.RAISETIME RAISETIME, 
   RDX_EVENTCONTEXT.EVENTID EVENTID,
   RDX_EVENTLOG.SEVERITY SEVERITY,
   RDX_EVENTLOG.CODE CODE,
   RDX_EVENTLOG.WORDS WORDS,
   RDX_EVENTLOG.COMMENTARY COMMENTARY,
   RDX_EVENTLOG.COMPONENT COMPONENT,
   RDX_EVENTLOG.RECEIPTOR RECEIPTOR
From RDX_EVENTCONTEXT, RDX_EVENTLOG
where 
   RDX_EVENTLOG.RAISETIME = RDX_EVENTCONTEXT.RAISETIME and 
   RDX_EVENTLOG.ID = RDX_EVENTCONTEXT.EVENTID 
   
/

grant select, delete on RDX_EVENTLOGCONTEXT to &USER&_RUN_ROLE
/

create or replace view RDX_PERIODSVIEW as
Select Distinct RDX_PROFILERLOG.PERIODENDTIME PERIODENDTIME,RDX_PROFILERLOG.INSTANCEID INSTANCEID From RDX_PROFILERLOG
/

grant select, delete on RDX_PERIODSVIEW to &USER&_RUN_ROLE
/

create or replace view RDX_SM_METRICSTATEVIEW as
Select RDX_SM_METRICTYPE.ID as TYPEID, RDX_SM_METRICTYPE.TITLE as TYPETITLE,
       RDX_SM_METRICTYPE.INSTANCEID as TYPEINSTANCEID, RDX_SM_METRICTYPE.UNITID as TYPEUNITID,
       RDX_SM_METRICTYPE.NETCHANNELID as TYPENETCHANNELID, RDX_SM_METRICTYPE.SERVICEURI as TYPESERVICEURI,
       RDX_SM_METRICTYPE.TIMINGSECTION as TYPETIMINGSECTION,
       RDX_SM_METRICTYPE.LOWERRORVAL as LOWERRORVAL, RDX_SM_METRICTYPE.LOWWARNVAL as LOWWARNVAL,
       RDX_SM_METRICTYPE.HIGHERRORVAL as HIGHERRORVAL, RDX_SM_METRICTYPE.HIGHWARNVAL as HIGHWARNVAL,
       RDX_SM_METRICTYPE.KIND as KIND,
       
       RDX_SM_METRICSTATE.ID as STATEID, 
       RDX_SM_METRICSTATE.INSTANCEID as STATEINSTANCEID, RDX_SM_METRICSTATE.UNITID as STATEUNITID,
       RDX_SM_METRICSTATE.NETCHANNELID as STATENETCHANNELID,
       RDX_SM_METRICSTATE.AVGVAL as AVGVAL, RDX_SM_METRICSTATE.MAXVAL as MAXVAL, RDX_SM_METRICSTATE.ENDVAL as ENDVAL 
from   RDX_SM_METRICTYPE , RDX_SM_METRICSTATE       
Where  RDX_SM_METRICTYPE.ID = RDX_SM_METRICSTATE.TYPEID 
       --and 
       --((RDX_SM_METRICTYPE.INSTANCEID=RDX_SM_METRICSTATE.INSTANCEID or RDX_SM_METRICTYPE.UNITID= RDX_SM_METRICSTATE.UNITID or
       --RDX_SM_METRICTYPE.NETCHANNELID= RDX_SM_METRICSTATE.NETCHANNELID) or
       --(RDX_SM_METRICTYPE.INSTANCEID is null and RDX_SM_METRICTYPE.UNITID is null and
       --RDX_SM_METRICTYPE.NETCHANNELID is null)) 
       --and        
       --(RDX_SM_METRICTYPE.KIND <> 'Profiling.Cnt' and RDX_SM_METRICTYPE.KIND <> 'Profiling.Duration' and 
       --RDX_SM_METRICTYPE.KIND <> 'Profiling.Freq' and RDX_SM_METRICTYPE.KIND <> 'Profiling.Percent.Cpu' and
       --RDX_SM_METRICTYPE.KIND <> 'Profiling.Percent.Db' and RDX_SM_METRICTYPE.KIND <> 'Profiling.Percent.Ext' and 
       --RDX_SM_METRICTYPE.KIND <> 'Inst.Arte.InstCnt' and RDX_SM_METRICTYPE.KIND <> 'Inst.Service.SessCnt' and 
       --RDX_SM_METRICTYPE.KIND <> 'Inst.Stop' and RDX_SM_METRICTYPE.KIND <> 'User' and
       --RDX_SM_METRICTYPE.KIND <> 'Unit.Hang' and RDX_SM_METRICTYPE.KIND <> 'Unit.Stop' )

/

grant select, delete on RDX_SM_METRICSTATEVIEW to &USER&_RUN_ROLE
/

create or replace view RDX_WF_CLERKSTATISTICS as
SELECT RDX_AC_USER.NAME AS NAME,
       PERSONNAME AS PERSONNAME,
       RDX_WF.getOwnProcessCount(NAME) AS OWNPROCESSCOUNT,
       RDX_WF.getStateFormCount(NAME, 1) AS DUEFORMCOUNT,
       RDX_WF.getStateFormCount(NAME, 2) AS OVERDUEFORMCOUNT
  FROM RDX_AC_USER

UNION

SELECT 
       '-' AS NAME,
       NULL AS PERSONNAME,
       0 AS OWNPROCESSCOUNT,
       RDX_WF.getStateFormCount(NULL, 1) AS DUEFORMCOUNT,
       RDX_WF.getStateFormCount(NULL, 2) AS OVERDUEFORMCOUNT
  FROM Dual

with read only
/

grant select on RDX_WF_CLERKSTATISTICS to &USER&_RUN_ROLE
/

alter table COUNTER
	add constraint FK_COUNTER_UPVALREF foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID)
	references RDX_UPVALREF (DEFID, OWNERENTITYID, OWNERPID) on delete cascade
/

alter table RDX_PC_CHANNELGSMMODEM
	add constraint FK_DPC_CHANNELGSMMODEM_CHANNEL foreign key (CHANNELUNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_PC_CHANNELHANDLER
	add constraint FK_DPC_CHANNELHANDLER_CHANNELU foreign key (UNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_PC_CHANNELUNIT
	add constraint FK_DPC_CHANNELUNIT_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_PC_EVENTSUBSCRIPTIONCODE
	add constraint FK_DPC_EVENTSUBSCRIPTIONCODE_E foreign key (SUBSCRIPTIONID)
	references RDX_PC_EVENTSUBSCRIPTION (ID) on delete cascade
/

alter table RDX_PC_EVENTSUBSCRIPTION
	add constraint FK_DPC_EVENTSUBSCRIPTION_USERG foreign key (USERGROUPNAME)
	references RDX_AC_USERGROUP (NAME) on delete cascade
/

alter table RDX_PC_OUTMESSAGE
	add constraint FK_DPC_OUTMESSAGE_CHANNELUNIT foreign key (CHANNELID)
	references RDX_UNIT (ID)
/

alter table RDX_PC_RECVMESSAGE
	add constraint FK_DPC_RECVMESSAGE_CHANNELUNIT foreign key (CHANNELID)
	references RDX_UNIT (ID)
/

alter table RDX_PC_SENTMESSAGE
	add constraint FK_DPC_SENTMESSAGE_CHANNELUNIT foreign key (CHANNELID)
	references RDX_UNIT (ID)
/

alter table RDX_JS_JOBSCHEDULERUNIT
	add constraint FK_JOBSCHEDULERUNIT_PARENTUNIT foreign key (PARENTID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_AC_USER2ROLE
	add constraint FK_RDX_AC_USER2ROLE_DASHCONFIG foreign key (PA$$4PQ4U65VK5HFVJ32XCUORBKRJM)
	references RDX_SM_DASHCONFIG (GUID) on delete cascade
/

alter table RDX_AC_USER2ROLE
	add constraint FK_RDX_AC_USER2ROLE_USER foreign key (USERNAME)
	references RDX_AC_USER (NAME) on delete cascade
/

alter table RDX_AC_USER2ROLE
	add constraint FK_RDX_AC_USER2ROLE_USER2ROLE foreign key (REPLACEDID)
	references RDX_AC_USER2ROLE (ID) on delete cascade
/

alter table RDX_AC_USER2ROLE
	add constraint FK_RDX_AC_USER2ROLE_USERGROUP foreign key (PA$$1ZOQHCO35XORDCV2AANE2UAFXA)
	references RDX_AC_USERGROUP (NAME) on delete cascade
/

alter table RDX_AC_USER2USERGROUP
	add constraint FK_RDX_AC_USER2UGROUP_USER foreign key (USERNAME)
	references RDX_AC_USER (NAME) on delete cascade
/

alter table RDX_AC_USER2USERGROUP
	add constraint FK_RDX_AC_USER2UGRP_UGRP foreign key (GROUPNAME)
	references RDX_AC_USERGROUP (NAME) on delete cascade
/

alter table RDX_AC_USERGROUP2ROLE
	add constraint FK_RDX_AC_USERGROUP2ROLE_DASHC foreign key (PA$$4PQ4U65VK5HFVJ32XCUORBKRJM)
	references RDX_SM_DASHCONFIG (GUID) on delete cascade
/

alter table RDX_AC_USERGROUP2ROLE
	add constraint FK_RDX_AC_USERGROUP2ROLE_USER1 foreign key (PA$$1ZOQHCO35XORDCV2AANE2UAFXA)
	references RDX_AC_USERGROUP (NAME) on delete cascade
/

alter table RDX_AC_USERGROUP2ROLE
	add constraint FK_RDX_AC_USERGROUP2ROLE_USERG foreign key (REPLACEDID)
	references RDX_AC_USERGROUP2ROLE (ID) on delete cascade
/

alter table RDX_AC_USERGROUP
	add constraint FK_RDX_AC_USERGROUP_INTERVALSC foreign key (LOGONSCHEDULEID)
	references RDX_JS_INTERVALSCHD (ID)
/

alter table RDX_AC_USERGROUP2ROLE
	add constraint FK_RDX_AC_USERGRP2ROLE_NAME_GR foreign key (GROUPNAME)
	references RDX_AC_USERGROUP (NAME) on delete cascade
/

alter table RDX_AC_USER
	add constraint FK_RDX_AC_USER_INTERVALSCHD foreign key (LOGONSCHEDULEID)
	references RDX_JS_INTERVALSCHD (ID)
/

alter table RDX_AC_USER
	add constraint FK_RDX_AC_USER_USERGROUP foreign key (ADMINGROUPNAME)
	references RDX_AC_USERGROUP (NAME)
/

alter table RDX_ARTEINSTANCE
	add constraint FK_RDX_ARTEINSTANCE_INSTANCE foreign key (INSTANCEID)
	references RDX_INSTANCE (ID) on delete cascade
/

alter table RDX_ARTEINSTANCE
	add constraint FK_RDX_ARTEINSTANCE_UNIT foreign key (UNITID)
	references RDX_UNIT (ID) on delete set null
/

alter table RDX_ARTEUNIT
	add constraint FK_RDX_ARTEUNIT_SAP foreign key (SAPID)
	references RDX_SAP (ID)
/

alter table RDX_ARTEUNIT
	add constraint FK_RDX_ARTEUNIT_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_AU_SCHEMEITEM
	add constraint FK_RDX_AU_SCHEMEITEM_SCHEME foreign key (SCHEMEGUID)
	references RDX_AU_SCHEME (GUID) on delete cascade
/

alter table RDX_CLASSLOADINGPROFILEITEM
	add constraint FK_RDX_CLASSLOADINGPROFILEITEM foreign key (PROFILEID)
	references RDX_CLASSLOADINGPROFILE (ID) on delete cascade
/

alter table RDX_CM_ITEMREF
	add constraint FK_RDX_CM_ITEMREF_ITEM foreign key (INTREF)
	references RDX_CM_ITEM (ID) rely disable novalidate
/

alter table RDX_CM_ITEMREF
	add constraint FK_RDX_CM_ITEMREF_UPVALREF foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID)
	references RDX_UPVALREF (DEFID, OWNERENTITYID, OWNERPID) on delete cascade
/

alter table RDX_CM_ITEM
	add constraint FK_RDX_CM_ITEM_ITEM foreign key (PARENTID)
	references RDX_CM_ITEM (ID) on delete cascade
/

alter table RDX_CM_ITEM
	add constraint FK_RDX_CM_ITEM_PACKET foreign key (PACKETID)
	references RDX_CM_PACKET (ID) on delete cascade
/

alter table RDX_DM_DATA
	add constraint FK_RDX_DM_DATA_PACKET foreign key (PACKETID)
	references RDX_DM_PACKET (ID) on delete cascade
/

alter table RDX_DM_OUTLINK
	add constraint FK_RDX_DM_OUTLINK_PACKET foreign key (PACKETID)
	references RDX_DM_PACKET (ID) on delete cascade
/

alter table RDX_DM_OUTLINKVERIFY
	add constraint FK_RDX_DM_OUTLNKVERIFY_OUTLNK foreign key (PACKETID, EXTTABLEGUID, PARENTPID)
	references RDX_DM_OUTLINK (PACKETID, EXTTABLEGUID, PARENTPID) on delete cascade
/

alter table RDX_DM_PACKET
	add constraint FK_RDX_DM_PACKET_USER foreign key (AUTHORNAME)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_DM_SEGMENT
	add constraint FK_RDX_DM_SEGMENT_PACKET foreign key (PACKETID)
	references RDX_DM_PACKET (ID) on delete cascade
/

alter table RDX_EASSELECTORADDONS
	add constraint FK_RDX_EASSELECTORADDONS_USER foreign key (LASTUPDATEUSER)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_EVENTCONTEXT
	add constraint FK_RDX_EVENTCONTEXT_EVENTLOG foreign key (RAISETIME, EVENTID)
	references RDX_EVENTLOG (RAISETIME, ID) rely disable novalidate
/

alter table RDX_EVENTLOG
	add constraint FK_RDX_EVENTLOG_STATION foreign key (STATIONNAME)
	references RDX_STATION (NAME) rely disable novalidate
/

alter table RDX_EVENTLOG
	add constraint FK_RDX_EVENTLOG_USER foreign key (USERNAME)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_EVENTLOG
	add constraint FK_RDX_EVENTLOG_USERRCPTR foreign key (RECEIPTOR)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_FALLBACKMQHANDLER
	add constraint FK_RDX_FALLBACKMQHANDLER_MUNIT foreign key (MAINUNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_FALLBACKMQHANDLER
	add constraint FK_RDX_FALLBACKMQHANDLER_UNIT foreign key (UNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_INSTANCE
	add constraint FK_RDX_INSTANCE_CLASSLOADINGPR foreign key (CLASSLOADINGPROFILEID)
	references RDX_CLASSLOADINGPROFILE (ID)
/

alter table RDX_INSTANCE
	add constraint FK_RDX_INSTANCE_SAP foreign key (SAPID)
	references RDX_SAP (ID)
/

alter table RDX_INSTANCE
	add constraint FK_RDX_INSTANCE_SCP foreign key (SCPNAME, SYSTEMID)
	references RDX_SCP (NAME, SYSTEMID)
/

alter table RDX_INSTANCE
	add constraint FK_RDX_INSTANCE_UNIT foreign key (TARGETEXECUTORID)
	references RDX_UNIT (ID)
/

alter table RDX_JMSHANDLER
	add constraint FK_RDX_JMSHANDLER_SAP foreign key (SAPID)
	references RDX_SAP (ID)
/

alter table RDX_JMSHANDLER
	add constraint FK_RDX_JMSHANDLER_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_JS_CALENDAR
	add constraint FK_RDX_JS_CALENDAR_USER foreign key (LASTUPDATEUSER)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_JS_CALENDARITEM
	add constraint FK_RDX_JS_CALENDIT_CALENDAR foreign key (CALENDARID)
	references RDX_JS_CALENDAR (ID) on delete cascade
/

alter table RDX_JS_CALENDARITEM
	add constraint FK_RDX_JS_CALENDIT_CALEND_IN foreign key (INCCALENDARID)
	references RDX_JS_CALENDAR (ID)
/

alter table RDX_JS_EVENTSCHD
	add constraint FK_RDX_JS_EVENTSCHD_USER foreign key (LASTUPDATEUSER)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_JS_EVENTSCHDITEM
	add constraint FK_RDX_JS_EVSCHDITEM_CALENDAR foreign key (CALENDARID)
	references RDX_JS_CALENDAR (ID)
/

alter table RDX_JS_EVENTSCHDITEM
	add constraint FK_RDX_JS_EVSCHDITEM_EVSCHD foreign key (SCHEDULEID)
	references RDX_JS_EVENTSCHD (ID) on delete cascade
/

alter table RDX_JS_INTERVALSCHD
	add constraint FK_RDX_JS_INTERVALSCHD_USER foreign key (LASTUPDATEUSER)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_JS_INTERVALSCHDITEM
	add constraint FK_RDX_JS_INTERVSCHDIT_CALEND foreign key (CALENDARID)
	references RDX_JS_CALENDAR (ID)
/

alter table RDX_JS_INTERVALSCHDITEM
	add constraint FK_RDX_JS_INTERVSCHDIT_INTERV foreign key (SCHEDULEID)
	references RDX_JS_INTERVALSCHD (ID) on delete cascade
/

alter table RDX_JS_JOBEXECUTORUNIT
	add constraint FK_RDX_JS_JOBEXECUTORUNIT_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_JS_JOBPARAM
	add constraint FK_RDX_JS_JOBPARAM_JOB foreign key (JOBID)
	references RDX_JS_JOBQUEUE (ID) on delete cascade
/

alter table RDX_JS_JOBQUEUE
	add constraint FK_RDX_JS_JOBQUEUE_TASK foreign key (TASKID)
	references RDX_JS_TASK (ID) rely disable novalidate
/

alter table RDX_JS_JOBQUEUE
	add constraint FK_RDX_JS_JOBQUEUE_UNIT foreign key (EXECUTORID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_JS_JOBSCHEDULERUNIT
	add constraint FK_RDX_JS_JOBSCHDLRUNIT_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_JS_TASK
	add constraint FK_RDX_JS_SCHDUNITJOB_EVSCHD foreign key (SCHEDULEID)
	references RDX_JS_EVENTSCHD (ID)
/

alter table RDX_JS_TASK
	add constraint FK_RDX_JS_SCHDUNITJOB_UNIT foreign key (UNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_JS_TASK
	add constraint FK_RDX_JS_TASK_SCP foreign key (SCPNAME, SYSTEMID)
	references RDX_SCP (NAME, SYSTEMID) on delete set null
/

alter table RDX_JS_TASK
	add constraint FK_RDX_JS_TASK_TASK foreign key (PARENTID)
	references RDX_JS_TASK (ID) on delete cascade
/

alter table RDX_KAFKAQUEUE
	add constraint FK_RDX_KAFKAQUEUE_MESSAGEQUEUE foreign key (QUEUEID)
	references RDX_MESSAGEQUEUE (ID) on delete cascade
/

alter table RDX_LIBUSERFUNC
	add constraint FK_RDX_LIBUSERFUNC_USERFUNCLIB foreign key (LIBNAME)
	references RDX_USERFUNCLIB (NAME) on delete cascade
/

alter table RDX_MESSAGEQUEUEPROCESSOR
	add constraint FK_RDX_MESSAGEQUEUEPROCESSOR_M foreign key (QUEUEID)
	references RDX_MESSAGEQUEUE (ID)
/

alter table RDX_MESSAGEQUEUEPROCESSOR
	add constraint FK_RDX_MESSAGEQUEUEPROCESSOR_U foreign key (UNITID)
	references RDX_UNIT (ID)
/

alter table RDX_NETHUB
	add constraint FK_RDX_NETHUB_SAP foreign key (SAPID)
	references RDX_SAP (ID)
/

alter table RDX_NETHUB
	add constraint FK_RDX_NETHUB_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_NETCHANNEL
	add constraint FK_RDX_NETLISTENER_NETPRTHNDLR foreign key (UNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_NETPORTHANDLER
	add constraint FK_RDX_NETPORTHANDLER_SAP foreign key (SAPID)
	references RDX_SAP (ID)
/

alter table RDX_NETPORTHANDLER
	add constraint FK_RDX_NETPORTHANDLER_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_PC_EVENTLIMITACC
	add constraint FK_RDX_PC_EVENTLIMITACC_EVENTS foreign key (SUBSCRIPTIONID)
	references RDX_PC_EVENTSUBSCRIPTION (ID) on delete cascade
/

alter table RDX_PC_EVENTLIMITACC
	add constraint FK_RDX_PC_EVENTLIMITACC_USER foreign key (USERNAME)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_PROFILERLOG
	add constraint FK_RDX_PROFILERLOG_INSTANCE foreign key (INSTANCEID)
	references RDX_INSTANCE (ID) on delete cascade
/

alter table RDX_REPORTPARAM
	add constraint FK_RDX_REPORTPARAM_REPORTPARAM foreign key (PARENTID)
	references RDX_REPORTPARAM (ID) on delete cascade
/

alter table RDX_REPORTPUB
	add constraint FK_RDX_REPORTPUB_REPORTPUBLIST foreign key (LISTID)
	references RDX_REPORTPUBLIST (ID) on delete cascade
/

alter table RDX_REPORTPUB
	add constraint FK_RDX_REPORTPUB_RPTPUBTOPIC foreign key (PARENTTOPICID)
	references RDX_REPORTPUBTOPIC (ID) on delete cascade
/

alter table RDX_REPORTPUBTOPIC
	add constraint FK_RDX_RPTPUBTOPIC_RPTPUBLIST foreign key (LISTID)
	references RDX_REPORTPUBLIST (ID) on delete cascade
/

alter table RDX_SAP
	add constraint FK_RDX_SAP_INSTANCE foreign key (SYSTEMINSTANCEID)
	references RDX_INSTANCE (ID) on delete set null deferrable initially deferred
/

alter table RDX_SAP
	add constraint FK_RDX_SAP_SERVICE foreign key (SYSTEMID, URI)
	references RDX_SERVICE (SYSTEMID, URI) on delete cascade
/

alter table RDX_SAP
	add constraint FK_RDX_SAP_UNIT foreign key (SYSTEMUNITID)
	references RDX_UNIT (ID) on delete cascade deferrable initially deferred
/

alter table RDX_SB_DATASCHEME
	add constraint FK_RDX_SB_DATASCHEME_USER foreign key (LASTUPDATEUSERNAME)
	references RDX_AC_USER (NAME) on delete set null
/

alter table RDX_SB_PIPELINENODE
	add constraint FK_RDX_SB_PIPELINENODE_USER foreign key (LASTUPDATEUSERNAME)
	references RDX_AC_USER (NAME) on delete set null
/

alter table RDX_SB_PIPELINEVER
	add constraint FK_RDX_SB_PIPELINEVER_PIPELINE foreign key (PIPELINEID)
	references RDX_SB_PIPELINE (ID) on delete cascade
/

alter table RDX_SB_PIPELINEVER
	add constraint FK_RDX_SB_PIPELINEVER_USER foreign key (CREATORUSERNAME)
	references RDX_AC_USER (NAME) on delete set null
/

alter table RDX_SB_PIPELINE
	add constraint FK_RDX_SB_PIPELINE_USER foreign key (LASTUPDATEUSERNAME)
	references RDX_AC_USER (NAME) on delete set null
/

alter table RDX_SB_PIPELINECONNECTOR
	add constraint FK_RDX_SB_PIPELNCCTR_PIPELNND foreign key (NODEID)
	references RDX_SB_PIPELINENODE (ID) on delete cascade
/

alter table RDX_SB_PIPELINENODEPARAM
	add constraint FK_RDX_SB_PIPELNENDPRM_PIPLNND foreign key (NODEID)
	references RDX_SB_PIPELINENODE (ID) on delete cascade
/

alter table RDX_SB_PIPELINEPARAM
	add constraint FK_RDX_SB_PIPELNEPARAM_PIPELN foreign key (PIPELINEID)
	references RDX_SB_PIPELINE (ID) on delete cascade
/

alter table RDX_SB_TRANSFORMSTAGE
	add constraint FK_RDX_SB_TRANSFRMSTG_PIPELNND foreign key (NODEID)
	references RDX_SB_PIPELINENODE (ID) on delete cascade
/

alter table RDX_SB_XPATHTABLE
	add constraint FK_RDX_SB_XPATHTABLE_TRANSFORM foreign key (STAGEID)
	references RDX_SB_TRANSFORMSTAGE (ID) on delete cascade
/

alter table RDX_SCP2SAP
	add constraint FK_RDX_SCP2SAP_SAP foreign key (SAPID)
	references RDX_SAP (ID) on delete cascade
/

alter table RDX_SCP2SAP
	add constraint FK_RDX_SCP2SAP_SCP foreign key (SCPNAME, SYSTEMID)
	references RDX_SCP (NAME, SYSTEMID) on delete cascade
/

alter table RDX_SCP
	add constraint FK_RDX_SCP_SYSTEM foreign key (SYSTEMID)
	references RDX_SYSTEM (ID) on delete cascade
/

alter table RDX_SERVICE
	add constraint FK_RDX_SERVICE_SYSTEM foreign key (SYSTEMID)
	references RDX_SYSTEM (ID) on delete cascade
/

alter table RDX_SM_METRICCONTROLDATA
	add constraint FK_RDX_SM_METRCTRLDT_METRSTT foreign key (STATEID)
	references RDX_SM_METRICSTATE (ID) on delete cascade
/

alter table RDX_SM_METRICHIST
	add constraint FK_RDX_SM_METRICHIST_METRICSTT foreign key (METRICID)
	references RDX_SM_METRICSTATE (ID) on delete cascade
/

alter table RDX_SM_METRICSTATE
	add constraint FK_RDX_SM_METRICSTATE_INSTANCE foreign key (INSTANCEID)
	references RDX_INSTANCE (ID) on delete cascade
/

alter table RDX_SM_METRICSTATE
	add constraint FK_RDX_SM_METRICSTATE_NETCHNL foreign key (NETCHANNELID)
	references RDX_NETCHANNEL (ID) on delete cascade
/

alter table RDX_SM_METRICSTATE
	add constraint FK_RDX_SM_METRICSTATE_SERVICE foreign key (SYSTEMID, SERVICEURI)
	references RDX_SERVICE (SYSTEMID, URI) on delete cascade
/

alter table RDX_SM_METRICSTATE
	add constraint FK_RDX_SM_METRICSTATE_UNIT foreign key (UNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_SM_METRICSTATE
	add constraint FK_RDX_SM_METRICST_METRICTYPE foreign key (TYPEID)
	references RDX_SM_METRICTYPE (ID) on delete cascade
/

alter table RDX_SM_METRICTYPE
	add constraint FK_RDX_SM_METRICTYPE_INSTANCE foreign key (INSTANCEID)
	references RDX_INSTANCE (ID) on delete cascade
/

alter table RDX_SM_METRICTYPE
	add constraint FK_RDX_SM_METRICTYPE_NETCHNL foreign key (NETCHANNELID)
	references RDX_NETCHANNEL (ID) on delete cascade
/

alter table RDX_SM_METRICTYPE
	add constraint FK_RDX_SM_METRICTYPE_SERVICE foreign key (SYSTEMID, SERVICEURI)
	references RDX_SERVICE (SYSTEMID, URI) on delete cascade
/

alter table RDX_SM_METRICTYPE
	add constraint FK_RDX_SM_METRICTYPE_UNIT foreign key (UNITID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_SM_SNMPAGENTUNIT
	add constraint FK_RDX_SM_SNMPAGENTUNIT_SAP foreign key (SAPID)
	references RDX_SAP (ID)
/

alter table RDX_SM_SNMPAGENTUNIT
	add constraint FK_RDX_SM_SNMPAGENTUNIT_SNMPMA foreign key (MANAGERID)
	references RDX_SM_SNMPMANAGER (ID)
/

alter table RDX_SM_SNMPAGENTUNIT
	add constraint FK_RDX_SM_SNMPAGENTUNIT_UNIT foreign key (ID)
	references RDX_UNIT (ID) on delete cascade
/

alter table RDX_STATION
	add constraint FK_RDX_STATION_SCP foreign key (SCPNAME, SCPSYSTEMID)
	references RDX_SCP (NAME, SYSTEMID)
/

alter table RDX_SYSTEM
	add constraint FK_RDX_SYSTEM_SCHEME foreign key (DEFAULTAUDITSCHEMEID)
	references RDX_AU_SCHEME (GUID)
/

alter table RDX_TESTCASE
	add constraint FK_RDX_TESTCASE_TESTCASE foreign key (PARENTID)
	references RDX_TESTCASE (ID) on delete cascade
/

alter table RDX_TESTCASE
	add constraint FK_RDX_TESTCASE_TESTCASE_GRP foreign key (GROUPID)
	references RDX_TESTCASE (ID) on delete set null
/

alter table RDX_TST_AUDITDETAIL
	add constraint FK_RDX_TST_AUDITDETAIL_AUDITMA foreign key (PKCOLINT, PKCOLCHAR, PKCOLNUM, PKCOLDATE, PKCOLSTR, PKCOLTIMESTAMP)
	references RDX_TST_AUDITMASTER (PKCOLINT, PKCOLCHAR, PKCOLNUM, PKCOLDATE, PKCOLSTR, PKCOLTIMESTAMP) on delete cascade
/

alter table RDX_TST_CHILD
	add constraint FK_RDX_TST_CHILD_PARENT foreign key (PARENTID)
	references RDX_TST_PARENT (ID) on delete cascade
/

alter table RDX_TST_DMTESTCHILD
	add constraint FK_RDX_TST_DMTESTCHILD_DMTEST foreign key (KEYID)
	references RDX_TST_DMTEST (ID) on delete cascade
/

alter table RDX_TST_DMTEST
	add constraint FK_RDX_TST_DMTEST_DMTESTA foreign key (KEYINT, KEYSTR)
	references RDX_TST_DMTESTA (PKINT, PKSTR) on delete cascade
/

alter table RDX_TST_INHERITANCENODE
	add constraint FK_RDX_TST_INHERITANCENODE_INH foreign key (PARENTID)
	references RDX_TST_INHERITANCENODE (ID) on delete cascade
/

alter table RDX_TST_PARENT
	add constraint FK_RDX_TST_PARENT_SCHEME foreign key (AUDITSCHEME)
	references RDX_AU_SCHEME (GUID) on delete cascade
/

alter table RDX_TST_TEDCCHILDDETAIL
	add constraint FK_RDX_TST_TEDCCHILDDTL_TEDCLD foreign key (ID)
	references RDX_TST_TEDCCHILD (ID) on delete cascade
/

alter table RDX_TST_TEDCCHILDDETAIL
	add constraint FK_RDX_TST_TEDCCHILDDTL_TED_P foreign key (DETAILPARENTID)
	references RDX_TST_TEDCPARENT (ID)
/

alter table RDX_TST_TEDCCHILD
	add constraint FK_RDX_TST_TEDCCHILD_TEDCPAREN foreign key (PARENTID)
	references RDX_TST_TEDCPARENT (ID)
/

alter table RDX_UNIT
	add constraint FK_RDX_UNIT_INSTANCE foreign key (INSTANCEID)
	references RDX_INSTANCE (ID)
/

alter table RDX_UNIT
	add constraint FK_RDX_UNIT_SCP foreign key (SCPNAME, SYSTEMID)
	references RDX_SCP (NAME, SYSTEMID)
/

alter table RDX_USER2STATION
	add constraint FK_RDX_USER2STATION_STATION foreign key (STATIONNAME)
	references RDX_STATION (NAME) on delete cascade
/

alter table RDX_USER2STATION
	add constraint FK_RDX_USER2STATION_USER foreign key (USERNAME)
	references RDX_AC_USER (NAME) on delete cascade
/

alter table RDX_USERFUNCLIBPARAM
	add constraint FK_RDX_USERFUNCLIBPARAM_USERFU foreign key (LIBNAME)
	references RDX_USERFUNCLIB (NAME) on delete cascade
/

alter table RDX_USERFUNCLIB
	add constraint FK_RDX_USERFUNCLIB_PIPELINE foreign key (PIPELINEID)
	references RDX_SB_PIPELINE (ID) on delete cascade
/

alter table RDX_USERFUNC
	add constraint FK_RDX_USERFUNC_UPVALREF foreign key (UPDEFID, UPOWNERENTITYID, UPOWNERPID)
	references RDX_UPVALREF (DEFID, OWNERENTITYID, OWNERPID) on delete cascade
/

alter table RDX_USERFUNC
	add constraint FK_RDX_USERFUNC_USER foreign key (LASTUPDATEUSERNAME)
	references RDX_AC_USER (NAME) rely disable novalidate
/

alter table RDX_USERREPORTVERSION
	add constraint FK_RDX_USERREPORTVERSION_USER foreign key (LASTUPDATEUSERNAME)
	references RDX_AC_USER (NAME) on delete set null
/

alter table RDX_USERREPORTVERSION
	add constraint FK_RDX_USERREPORTVERSION_USERR foreign key (REPORTGUID)
	references RDX_USERREPORT (GUID) on delete cascade
/

alter table RDX_USERREPORT
	add constraint FK_RDX_USERREPORT_USER foreign key (LASTUPDATEUSERNAME)
	references RDX_AC_USER (NAME) on delete set null
/

alter table RDX_USERREPORT
	add constraint FK_RDX_USERRPT_USERRPTMODULE foreign key (MODULEGUID)
	references RDX_USERREPORTMODULE (GUID) on delete cascade
/

alter table RDX_WF_FORMLOG
	add constraint FK_RDX_WF_FORMLOG_FORM foreign key (FORMID)
	references RDX_WF_FORM (ID) on delete cascade
/

alter table RDX_WF_FORM
	add constraint FK_RDX_WF_FORM_PROCESS foreign key (PROCESSID)
	references RDX_WF_PROCESS (ID) on delete cascade
/

alter table RDX_WF_PROCESSLINK
	add constraint FK_RDX_WF_PROCESSLINK_PROCESS foreign key (PROCESSID)
	references RDX_WF_PROCESS (ID) on delete cascade
/

alter table RDX_WF_PROCESS
	add constraint FK_RDX_WF_PROCESS_PROCESSTYPE foreign key (TYPEGUID)
	references RDX_WF_PROCESSTYPE (GUID)
/

alter table RDX_REPORTPUBTOPIC
	add constraint FK_REPORTPUBTOPIC_INHERIT foreign key (INHERITEDTOPICID)
	references RDX_REPORTPUBTOPIC (ID) on delete cascade
/

alter table RDX_REPORTPUBTOPIC
	add constraint FK_REPORTPUBTOPIC_PARENT foreign key (PARENTTOPICID)
	references RDX_REPORTPUBTOPIC (ID) on delete cascade
/

alter table RDX_REPORTPUB
	add constraint FK_REPORTPUB_INHERIT foreign key (INHERITEDPUBID)
	references RDX_REPORTPUB (ID) on delete cascade
/

create or replace package body RDX_ACS as

	sysSuperAdminRoleId  constant varchar2(30) := 'rolSUPER_ADMIN_______________';
	error_Message constant varchar2(30) := 'Incorrect input string - ';

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
	  RAISE_APPLICATION_ERROR (-20100, error_Message);
	end if; 
	pos:=pos+1;
	res := TRdxAcsArea(TRdxAcsCoordinates());
	<<lbl1>>while(true)
	    loop
	      if SubStr(str , pos, 1)=')' then
	         exit lbl1;
	      end if;
	      if SubStr(str , pos, 1)<>'(' then
	         RAISE_APPLICATION_ERROR (-20100, error_Message || str);
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)='0' then
	         prohibited := 0;
	      else 
	        if SubStr(str , pos, 1)='1' then
	           prohibited := 1;
	        else
	           RAISE_APPLICATION_ERROR (-20100, error_Message || str);
	        end if;
	      end if;
	      pos:=pos+1;
	      if SubStr(str , pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, error_Message || str);
	      end if;
	      pos:=pos+1;
	      accessPartitionKey := SubStr(str, pos,29); 
	      pos:=pos+29;
	      if SubStr(str, pos, 1)<>',' then
	         RAISE_APPLICATION_ERROR (-20100, error_Message || str);
	      end if;
	      pos := pos+1;
	      newPos:=InStr(str, ')', pos);
	      if newPos = 0 then
	         RAISE_APPLICATION_ERROR (-20100, error_Message || str);
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
	                      pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	                     where isNew=0 and exceptingID1<>Id and exceptingID2<>Id and isOwn <> 0 and userName = pUser and  pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	                     where  userName = pUser and  pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	                     where isNew=0 and isOwn <> 0 and userName = pUser and  pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	                    pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	                   where isNew=0 and  groupName = pGroup and  pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	             where isNew=0 and  userName = pUser and  pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	        return RDX_ACS.curUserHasRoleInArea(sysSuperAdminRoleId, pPointList);
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
	        return RDX_ACS.curUserHasRoleInArea(sysSuperAdminRoleId, pPointList);
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
	             where isNew=0 and userName = pUser and  pRole || sysSuperAdminRoleId LIKE '%' || roleId || '%')
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
	        return RDX_ACS.curUserHasRoleInArea(sysSuperAdminRoleId, pPointList);
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
	        return RDX_ACS.curUserHasRoleInArea(sysSuperAdminRoleId, pPointList);
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
	                       (roleid = ind.roleid or roleid = sysSuperAdminRoleId)
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
	                       (roleid = ind.roleid or roleid = sysSuperAdminRoleId)
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
	        RAISE_APPLICATION_ERROR (-20100, error_Message || str );
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
	                            (g2r_row.roleId = U2R.roleId or U2R.roleId = sysSuperAdminRoleId))
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
	                            (u2r_row1.roleId = U2R.roleId or U2R.roleId = sysSuperAdminRoleId))
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

create or replace package body RDX_ACS_UTILS as

	function buildAssignedAccessAreaU2R(
		row_ in RDX_AC_User2Role%ROWTYPE
	) return TRdxAcsArea
	is
	begin
	 return NULL;   
	end;

	function buildAssignedAccessAreaG2R(
		row_ in RDX_AC_UserGroup2Role%ROWTYPE
	) return TRdxAcsArea
	is
	begin
	return NULL;   
	end;

	procedure compileRights
	is
	begin
	null;
	end;

	procedure compileRightsForGroup(
		pUserGroup in varchar2
	)
	is
	begin
	null;
	end;

	procedure compileRightsForUser(
		pUser in varchar2
	)
	is
	begin
	null;
	end;

	procedure compileRightsForGrpBeforeDel(
		pUserGroup in varchar2
	)
	is
	begin
	null;
	end;

	procedure moveRightsFromUserToGroup(
		user_ in varchar2,
		group_ in varchar2
	)
	is
	begin
	 INSERT INTO RDX_AC_USERGROUP (name) VALUES (group_);
	 INSERT INTO RDX_AC_USER2USERGROUP (userName, groupName) VALUES (user_, group_);
	  for ind in (Select * from RDX_AC_USER2ROLE where userName = User_ and  isOwn = 1)
	          loop
	          INSERT INTO RDX_AC_USERGROUP2ROLE (ID, GroupName) VALUES ( SQN_RDX_AC_USERGROUP2ROLEID.NEXTVAL, group_);
	          DELETE FROM RDX_AC_USER2ROLE WHERE id=ind.id;
	          end loop;
	end;
end;
/

create or replace package body RDX_ADS_META as

	function isDefInDomain(
		pDefId in varchar2,
		pDomainId in varchar2
	) return integer
	is
	begin
	    if pDefId = pDomainId then
	        return 1;
	    end if;
	    for cur in (select DOMAINID domainId from RDX_DEF2DOMAIN where VERSIONNUM = RDX_Arte.getVersion and DEFID = pDefId) loop
	      if RDX_ADS_META.isDefInDomain(cur.domainId, pDomainId) != 0 then  
	          return 1;
	      end if;
	    end loop;
	    return 0;
	end;

	function isClassExtends(
		pClsId in varchar2,
		pBaseClsId in varchar2
	) return integer
	is
	begin
	    if pClsId = pBaseClsId then
	        return 1;
	    end if;
	    for cur in (select /*+ INDEX(RDX_CLASSANCESTOR PK_RDX_CLASSANCESTOR) */ ANCESTORID from RDX_CLASSANCESTOR where VERSIONNUM = RDX_Arte.getVersion() and CLASSID = pClsId) loop
	      if RDX_ADS_META.isClassExtends(cur.ancestorId, pBaseClsId) != 0 then  
	          return 1;
	      end if;
	    end loop;
	    return 0;
	end;

	function areVersionIndicesDeployed(
		pVersionNum in integer
	) return integer
	is
	begin
	    for cur in (select 1 from dual 
	        where   exists (select * from RDX_CLASSANCESTOR where VERSIONNUM = pVersionNum) or 
	                exists (select * from RDX_DEF2DOMAIN where VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_ENUMITEM2DOMAIN where VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_ENTITYUSERREF where RDX_ENTITYUSERREF.VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_ENTITYARRUSERREF where  RDX_ENTITYARRUSERREF.VERSIONNUM = pVersionNum) or
	                exists (select * from RDX_EVENTCODE2EVENTPARAMS where  RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM = pVersionNum)
	    ) loop
	        return 1;
	    end loop;
	    return 0;
	end;

	function isEnumValueInDomain(
		pEnumId in varchar2,
		pEnumItemValAsStr in varchar2,
		pDomainId in varchar2
	) return integer
	is
	begin
	    for cur in (select DOMAINID enumItemDomainId from RDX_ENUMITEM2DOMAIN where 
	        VERSIONNUM = RDX_Arte.getVersion() and ENUMID = pEnumId and
	        ENUMITEMVALASSTR = pEnumItemValAsStr) 
	    loop
	        if (RDX_ADS_META.isDefInDomain(pDefId => cur.enumItemDomainId, pDomainId => pDomainId) <> 0) then        
	            return 1;
	        end if;
	    end loop;
	    return 0;
	end;

	function getIndicesOldestVersion return integer
	is
	  tmp integer;
	  res integer;
	begin
	    select min(VERSIONNUM ) into res from RDX_CLASSANCESTOR;
	    select min(VERSIONNUM) into tmp from RDX_DEF2DOMAIN;
	    if (tmp < res) then
	        res := tmp;
	    end if;    
	    select min(VERSIONNUM) into tmp from RDX_ENUMITEM2DOMAIN;
	    if (tmp < res) then
	        res := tmp;
	    end if;    
	    select min(RDX_ENTITYUSERREF.VERSIONNUM) into tmp from RDX_ENTITYUSERREF;
	    if (tmp < res) then
	        res := tmp;
	    end if;    
	    select min(RDX_ENTITYARRUSERREF.VERSIONNUM) into tmp from RDX_ENTITYARRUSERREF;
	    if (tmp < res) then
	        res := tmp;
	    end if;
	    select min(RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM) into tmp from RDX_EVENTCODE2EVENTPARAMS;
	    if (tmp < res) then
	        res := tmp;
	    end if;     
	    return res;
	end;

	function getIndexedVersionsCnt return integer
	is
	  res integer;
	begin
	  select count(*) into res from (
	    select distinct versionnum from (
	         select distinct VERSIONNUM from RDX_CLASSANCESTOR union
	         select distinct VERSIONNUM from RDX_DEF2DOMAIN union
	         select distinct VERSIONNUM from RDX_ENUMITEM2DOMAIN union
	         select distinct RDX_ENTITYUSERREF.VERSIONNUM from RDX_ENTITYUSERREF union
	         select distinct RDX_ENTITYARRUSERREF.VERSIONNUM from RDX_ENTITYARRUSERREF union
	         select distinct RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM from RDX_EVENTCODE2EVENTPARAMS
	    )
	  );
	  return res;
	end;

	procedure truncDeployedIndices
	is
	    oldestVer integer;
	begin
	   if RDX_ADS_META.getIndexedVersionsCnt() > 4 then
	      oldestVer := RDX_ADS_META.getIndicesOldestVersion();
	      delete from RDX_CLASSANCESTOR where VERSIONNUM = oldestVer;  
	      delete from RDX_DEF2DOMAIN where VERSIONNUM = oldestVer;  
	      delete from RDX_ENUMITEM2DOMAIN where VERSIONNUM = oldestVer;
	      delete from RDX_ENTITYUSERREF where RDX_ENTITYUSERREF.VERSIONNUM = oldestVer;
	      delete from RDX_ENTITYARRUSERREF where RDX_ENTITYARRUSERREF.VERSIONNUM = oldestVer;
	      delete from RDX_EVENTCODE2EVENTPARAMS where RDX_EVENTCODE2EVENTPARAMS.VERSIONNUM = oldestVer;
	   end if;
	end;
end;
/

create or replace package body RDX_AUDIT as

	function dropSubPartition(
		d in date,
		type in integer
	) return boolean
	is
	  dayName   varchar2(100);
	  NOT_EXIST exception;   PRAGMA EXCEPTION_INIT(NOT_EXIST, -2149);
	  THE_ONLY exception;    PRAGMA EXCEPTION_INIT(THE_ONLY, -14758);
	  THE_ONLY_SUB exception;    PRAGMA EXCEPTION_INIT(THE_ONLY_SUB, -14629);
	  INVALID_SUB exception;    PRAGMA EXCEPTION_INIT(INVALID_SUB, -14702);
	begin
	   select to_char(d,'YYYY_MM_DD') into dayName from dual;
	   begin
	      EXECUTE IMMEDIATE 'alter table RDX_AU_AUDITLOG drop subpartition for (to_date(''' || dayName || ''', ''YYYY_MM_DD''), '|| TO_CHAR(type) ||')';
	   exception 
	     when NOT_EXIST or INVALID_SUB then
	          return false;
	     when THE_ONLY_SUB then
	      begin    
	         EXECUTE IMMEDIATE 'alter table RDX_AU_AUDITLOG drop partition for (to_date(''' || dayName || ''', ''YYYY_MM_DD''))';
	      exception 
	        when THE_ONLY or NOT_EXIST then 
	          return false;
	      end;
	   end;
	   return true;
	end;

	procedure dropOldSubPartitions(
		duration in integer,
		type in integer
	)
	is
	   d date;
	begin
	   d:=sysdate-duration;
	   #IF DB_TYPE == "ORACLE" AND isEnabled("org.radixware\\Partitioning") THEN
	   loop
	      d:=d-1;  
	      exit when RDX_AUDIT.dropSubPartition(d, Type)!=TRUE;
	   end loop;
	   #ELSE
	   delete from RDX_AU_AUDITLOG where RDX_AU_AUDITLOG.EVENTTIME < d and RDX_AU_AUDITLOG.STOREDURATION = type;
	   #ENDIF
	end;

	procedure cleanPartitions
	is  
	  v1 integer; v2 integer; v3 integer; v4 integer;v5 integer;
	   
	begin
	   select AUDITSTOREPERIOD1, AUDITSTOREPERIOD2, AUDITSTOREPERIOD3, AUDITSTOREPERIOD4, AUDITSTOREPERIOD5
	      into v1,v2,v3,v4,v5 
	      from RDX_SYSTEM where ID=1;
	   RDX_AUDIT.dropOldSubPartitions(v1,1);
	   RDX_AUDIT.dropOldSubPartitions(v2,2);
	   RDX_AUDIT.dropOldSubPartitions(v3,3);
	   RDX_AUDIT.dropOldSubPartitions(v4,4);
	   RDX_AUDIT.dropOldSubPartitions(v5,5);
	end;

	procedure dailyMaintenance
	is 
	begin

	  --удаление разделов
	  RDX_AUDIT.cleanPartitions;
	  
	  --создание раздела на завтра
	  commit;
	  insert into RDX_AU_AUDITLOG (EVENTTIME, ID, EVENTTYPE,  STOREDURATION, TABLEID, PID) values (sysdate+1, SQN_RDX_AU_AUDITLOGID.NextVal, 'I', 1, 'x', 'x');
	  rollback;
	end;

	procedure enableTrigger(
		pId in varchar2,
		pType in varchar2
	)
	is
	    vQuery varchar2(250);
	begin
	    vQuery := 'ALTER TRIGGER at' || Lower(pType) || substr(pId,4) || ' ENABLE';
	    begin
	        EXECUTE IMMEDIATE vQuery;
	        exception when OTHERS then return;       
	    end;
	end;

	procedure disableTrigger(
		pTableid in varchar2,
		pType in varchar2,
		pParentTableid in varchar2
	)
	is 
	    vQuery varchar2(250);
	    vCnt integer;
	begin
	--    select count(*) into vCnt from RDX_AU_SCHEMEITEM where tableid = pParentTableid and eventType = Upper(pType);
	--    if vCnt <= 1 then 
	--        begin
	    vQuery := 'ALTER TRIGGER at' || Lower(pType) || substr(pTableId,4) || ' DISABLE';
	    begin
	        EXECUTE IMMEDIATE vQuery;
	        exception when OTHERS then return;       
	    end;
	--        end;    
	--    end if;    
	end;

	procedure updateTableAuditStateEventType(
		pTableGuid in varchar2,
		pEventType in varchar2,
		pParentTableGuid in varchar2
	)
	is
	    vSchemeId varchar2(50);
	    vCount integer;
	begin
	    select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID = 1;
	    if vSchemeId is not null then
	        select Count(*) into vCount from RDX_AU_SCHEMEITEM 
	            where SCHEMEGUID = vSchemeId and TABLEID = pParentTableGuid and EVENTTYPE = pEventType and Rownum < 2;
	        if vCount > 0 then 
	            RDX_AUDIT.enableTrigger(pTableGuid, pEventType);
	        else    
	            RDX_AUDIT.disableTrigger(pTableGuid, pEventType, pParentTableGuid);
	        end if;
	    else
	        RDX_AUDIT.disableTrigger(pTableGuid, pEventType, pParentTableGuid);
	    end if;
	end;

	procedure updateTableAuditState(
		tableGuid in varchar2,
		tableParentGuid in varchar2
	)
	is
	    vSaveData integer;
	begin
	    RDX_AUDIT.updateTableAuditStateEventType(tableGuid, 'U', tableParentGuid);
	    RDX_AUDIT.updateTableAuditStateEventType(tableGuid, 'I', tableParentGuid);
	    RDX_AUDIT.updateTableAuditStateEventType(tableGuid, 'D', tableParentGuid);
	end;

	function getStoreDuration(
		pSchemeId in varchar2,
		pTableId in varchar2,
		pType in varchar2
	) return integer
	is vStoreDuration integer;
	begin
	    begin
	        select STOREDURATION into vStoreDuration
	        from RDX_AU_SCHEMEITEM
	        where SCHEMEGUID = pSchemeId and
	        TABLEID = pTableId and 
	        EVENTTYPE = pType and
	        SAVEDATA = 1;
	    exception when no_data_found then vStoreDuration := -1;
	    end;   
	    return vStoreDuration; 
	end;

	procedure addValueStr(
		pData in out clob,
		pName in varchar2,
		pValue in varchar2
	)
	is
	  vTemp CLOB; 
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(Length(pValue))||']'||pValue;
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueBlob(
		pData in out clob,
		pName in varchar2,
		pValue in BLOB
	)
	is
	  vTemp CLOB; val varchar2(32767); 
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     val := UTL_RAW.CAST_TO_VARCHAR2(UTL_ENCODE.base64_encode(DBMS_LOB.SUBSTR(pValue)));
	     vTemp := pName||'=['||TO_CHAR(length(val))||']'||val;
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueRaw(
		pData in out clob,
		pName in varchar2,
		pValue in RAW
	)
	is
	  vTemp CLOB; val varchar2(32767);
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else 
	     val := UTL_RAW.CAST_TO_VARCHAR2(UTL_ENCODE.base64_encode(pValue));
	     vTemp := pName||'=['||TO_CHAR(Length(val))||']'||val;
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueClob(
		pData in out clob,
		pName in varchar2,
		pValue in CLOB
	)
	is
	  vTemp CLOB; 
	begin                          
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if; 
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(dbms_lob.getlength(pValue))||']';
	     dbms_lob.append(vTemp,pValue);
	  end if;
	  pData := pData||vTemp;
	end;

	procedure addValueBool(
		pData in out clob,
		pName in varchar2,
		pValue in boolean
	)
	is
	  vTemp CLOB; 
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;              
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	    if pValue then
	        vTemp := pName||'=[0]true';
	    else
	        vTemp := pName||'=[0]false';
	    end if;    
	  end if;     
	  pData:=pData||vTemp;
	end;

	procedure addValueInt(
		pData in out clob,
		pName in varchar2,
		pValue in integer
	)
	is
	  vTemp CLOB; 
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;              
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(Length(TO_CHAR(pValue)))||']'||TO_CHAR(pValue);
	  end if;     
	  pData:=pData||vTemp;
	end;

	procedure addValueNum(
		pData in out clob,
		pName in varchar2,
		pValue in number
	)
	is
	  vTemp CLOB; 
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vTemp := pName||'=['||TO_CHAR(Length(TO_CHAR(pValue)))||']'||TO_CHAR(pValue,'FM9999999999999999999999999999999.9999999999999999999999999999999');
	  end if; 
	  pData:=pData||vTemp;
	end;

	procedure addValueDate(
		pData in out clob,
		pName in varchar2,
		pValue in date
	)
	is
	  vTemp CLOB;        
	  vDate varchar2(100);
	begin
	  if Length(pData) != 0 then
	     pData:=pData||CHR(10);
	  end if;             
	  if pValue IS NULL then
	     vTemp := pName||'=[0]NULL';
	  else
	     vDate := TO_CHAR(pValue,'DD.MM.YYYY HH24:MI:SS'); 
	     vTemp := pName||'=['||TO_CHAR(Length(vDate))||']'||vDate;
	  end if;
	  pData:=pData||vTemp;
	end;

	procedure regPropUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		updateData in CLOB,
		pidKey in varchar2
	)
	is 
	   vStoreDuration integer; 
	   vSchemeId varchar2(50);
	begin
	    vSchemeId:=schemeId;
	    if (schemeId is null) then
	        begin
	             select RDX_SYSTEM.DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM, RDX_AU_SCHEME where RDX_SYSTEM.ID =1 and RDX_AU_SCHEME.GUID = RDX_SYSTEM.DEFAULTAUDITSCHEMEID;
	        exception 
	            when no_data_found then return;
	        end;
	    end if;
	    vStoreDuration := RDX_AUDIT.getStoreDuration(vSchemeId,tableId,'U');
	    if vStoreDuration != -1 then
	        insert into RDX_AU_AUDITLOG 
	                (RDX_AU_AUDITLOG.ID,RDX_AU_AUDITLOG.EVENTTIME,RDX_AU_AUDITLOG.TABLEID,RDX_AU_AUDITLOG.CLASSID,
	                 RDX_AU_AUDITLOG.EVENTTYPE,RDX_AU_AUDITLOG.STOREDURATION,RDX_AU_AUDITLOG.USERNAME,
	                 RDX_AU_AUDITLOG.STATIONNAME,RDX_AU_AUDITLOG.PID,RDX_AU_AUDITLOG.EVENTDATA)
	             values 
	                (SQN_RDX_AU_AUDITLOGID.NextVal,SysDate,tableId,pClassId,
	                 'U',vStoreDuration,RDX_Arte.getUserName, 
	                 RDX_Arte.getStationName, pidKey, updateData);
	    end if;        
	end;

	procedure regPropIntUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in integer,
		newVal in integer
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueInt(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueInt(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId, tableId, pClassId, propId, vData,pid);
	end;

	procedure regPropRawUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in RAW,
		newVal in RAW
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueRaw(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueRaw(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId, tableId, pClassId, propId, vData, pid);
	end;

	procedure regPropBlobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in BLOB,
		newVal in BLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBlob(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueBlob(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId, tableId, pClassId, propId, vData, pid);
	end;

	procedure regPropClobUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in CLOB,
		newVal in CLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueClob(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueClob(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId, pClassId, propId,vData,pid);
	end;

	procedure regPropDateTimeUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in date,
		newVal in date
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueDate(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueDate(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropNumCreated(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueNum(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId, pClassId, propId,vData,pid);
	end;

	procedure regPropStrCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueStr(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropBoolCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBool(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropDateTimeCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueDate(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId, pClassId, propId,vData,pid);
	end;

	procedure regPropClobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueClob(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropBlobCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBlob(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropRawCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueRaw(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropNumDeleted(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in number
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueNum(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropStrDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in varchar2
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueStr(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropBoolDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in boolean
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBool(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropDateTimeDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in date
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueDate(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropClobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in CLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueClob(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropBlobDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in BLOB
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBlob(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropRawDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in RAW
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueRaw(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropIntDeleted(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueInt(vData,'old.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure regPropIntCreated(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		val in integer
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueInt(vData,'new.'||vId,val);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropBoolUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in boolean,
		newVal in boolean
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueBool(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueBool(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropStrUpdate(
		schemeId in varchar2,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in varchar2,
		newVal in varchar2
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueStr(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueStr(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId, propId,vData,pid);
	end;

	procedure regPropNumUpdate(
		schemeId in number,
		tableId in varchar2,
		pClassId in varchar2,
		propId in varchar2,
		pid in varchar2,
		oldVal in number,
		newVal in number
	)
	is vData CLOB; vId varchar2(250);
	begin
	    select NULL into vData from DUAL;
	    vId := TO_CHAR(propId);
	    RDX_AUDIT.addValueNum(vData,'old.'||vId,oldVal);
	    RDX_AUDIT.addValueNum(vData,'new.'||vId,newVal);
	    RDX_AUDIT.regPropUpdate(schemeId,tableId,pClassId,propId,vData,pid);
	end;

	procedure addChangedValueDateTime(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in date,
		pNewValue in date
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueDate(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueDate(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueRaw(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in RAW,
		pNewValue in RAW
	)
	is
	begin
	    if  dbms_lob.compare(pOldValue,pNewValue)!=0 then
	        begin
	            RDX_AUDIT.addValueRaw(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueRaw(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueBlob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in BLOB,
		pNewValue in BLOB
	)
	is
	begin
	    if  dbms_lob.compare(pOldValue,pNewValue)!=0 then
	        begin
	            RDX_AUDIT.addValueBlob(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueBlob(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueClob(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in CLOB,
		pNewValue in CLOB
	)
	is
	begin
	    if  dbms_lob.compare(pOldValue,pNewValue)!=0 then
	        begin
	            RDX_AUDIT.addValueClob(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueClob(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueBool(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in boolean,
		pNewValue in boolean
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueBool(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueBool(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueStr(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in varchar2,
		pNewValue in varchar2
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueStr(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueStr(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueNum(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in number,
		pNewValue in number
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueNum(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueNum(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;

	procedure addChangedValueInt(
		pChangeData in out CLOB,
		pName in varchar2,
		pOldValue in integer,
		pNewValue in integer
	)
	is
	begin
	    if (pOldValue IS NULL and pNewValue IS NOT NULL)
	            OR(pOldValue IS NOT NULL and pNewValue IS NULL)
	            OR pOldValue != pNewValue then
	        begin
	            RDX_AUDIT.addValueInt(pChangeData,'old.'||pName,pOldValue);
	            RDX_AUDIT.addValueInt(pChangeData,'new.'||pName,pNewValue);
	        end;
	    end if;
	end;
end;
/

create or replace package body RDX_Array as

	function createRef(
		tableGuid in varchar2,
		x in varchar2
	) return varchar2 deterministic
	is
	begin
	  return tableGuid || chr(10) || x;
	end;

	function prepareClobArr(
		pSize in integer
	) return CLOB
	is
	  res CLOB; 
	begin
	  dbms_lob.createTemporary(res, true); 
	  dbms_lob.append(res,to_char(pSize));
	  return res;
	end;

	function getArraySize(
		lob in clob
	) return integer
	is
	   pos integer;
	begin
	   if (lob is null) or (DBMS_LOB.GETLENGTH(lob) < 1) then
	       return 0;
	   end if;   
	   pos := INSTR(lob, '['); 
	   if (pos < 2) then
	      return 0;
	   end if;   
	   return TO_NUMBER(SUBSTR(lob, 1, pos-1));
	end;

	function search(
		lob in clob,
		what in varchar2,
		startIdx in integer,
		asNumber in boolean
	) return number deterministic
	is
	 arrSize integer;
	 ind_    integer;
	 pos1 integer;
	 pos2 integer;
	 len integer;
	 whatLen integer;
	 temp varchar2(30);
	 item VARCHAR2(32767);
	 tempWhat number; 
	begin
	 IF asNumber THEN
	   BEGIN		  
	     tempWhat :=  Rdx_Valasstr.numFromValAsStr(what);
	     EXCEPTION WHEN VALUE_ERROR THEN		
	       RETURN 0 ;
	   END;
	 END IF;

	 arrSize := RDX_Array.getArraySize(lob);
	 if (startIdx>arrSize)then
	  return 0;
	 end if;
	 whatLen := LENGTH(what);

	 pos1 := LENGTH(arrSize) + 1;

	 for  ind_ in 1 ..  startIdx-1  loop
	    pos2  := INSTR(lob, ']', pos1);

	    temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	    if (temp is not null and LENGTH(temp) > 0) then
	       len := TO_NUMBER(temp);
	     else  
	       len := 0;
	    end if;
	    pos1 := pos2 + len + 1;        
	 end loop;
	 
	 
	 for  ind_ in startIdx .. arrSize loop
	    pos2  := INSTR(lob, ']', pos1);

	    temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	    if (temp is not null and LENGTH(temp) > 0) then
	       len := TO_NUMBER(temp);
	    else
	       len := 0;
	    end if;    
	    item := SUBSTR(lob, pos2+1, len);
	    IF (what IS NULL AND item IS NULL ) THEN     
	       return ind_;    
	    end if;    
	    IF (what IS not NULL and item IS not NULL ) THEN     
	         if (asNumber) then         
	            BEGIN            
	             IF (RDX_ValAsStr.numFromValAsStr(item) = tempWhat) THEN     
	               return ind_;    
	             end if;
	             EXCEPTION WHEN VALUE_ERROR THEN
	               NULL;
	             END;
	          else
	             IF (whatLen = len AND item = what) THEN     
	               return ind_;    
	             end if;          
	          end if;
	    end if;     
	    pos1 := pos2 + len + 1;        
	 end loop;
	 return 0;
	end;

	procedure incrementHeader(
		res in out clob,
		oldHeaderSize in integer
	)
	is
	  newItemCount varchar2(30);
	  newItemCountSize integer;
	  oldItemCountSize integer;
	  moveSize integer;
	  tempClob clob;
	  oldSize integer;

	begin
	     newItemCount := to_char(oldHeaderSize + 1);
	     newItemCountSize := LENGTH(newItemCount );
	     oldItemCountSize := LENGTH(to_char(oldHeaderSize));
	     if (oldItemCountSize <> newItemCountSize) then
	           -- slow branch
	           dbms_lob.createTemporary(tempClob, true); 
	           oldSize := DBMS_LOB.GETLENGTH(res);

	           moveSize := oldSize - oldItemCountSize;
	           dbms_lob.copy(tempClob, res, moveSize, 1, oldItemCountSize+1);
	           dbms_lob.copy(res, tempClob, moveSize, newItemCountSize+1, 1);
	     end if;
	     dbms_lob.WRITE (res, newItemCountSize ,1, newItemCount);
	end;

	procedure decrementHeader(
		res in out clob,
		oldHeaderSize in integer
	)
	is
	  newItemCount varchar2(30);
	  newItemCountSize integer;
	  oldItemCountSize integer;
	  moveSize integer;
	  tempClob clob;
	  oldSize integer;

	begin
	     newItemCount := to_char(oldHeaderSize - 1);
	     newItemCountSize := LENGTH(newItemCount );
	     oldItemCountSize := LENGTH(to_char(oldHeaderSize));
	     if (oldItemCountSize <> newItemCountSize) then
	           -- slow branch
	           dbms_lob.createTemporary(tempClob, true); 
	           oldSize := DBMS_LOB.GETLENGTH(res);

	           moveSize := oldSize - oldItemCountSize;
	           dbms_lob.copy(tempClob, res, moveSize, 1, oldItemCountSize+1);
	           dbms_lob.copy(res, tempClob, moveSize, newItemCountSize+1, 1);

	       dbms_lob.trim(res, oldSize-1); 
	 end if;
	     dbms_lob.WRITE (res, newItemCountSize ,1, newItemCount);
	end;

	procedure appendWithoutHeaderModifying(
		res in out clob,
		x in varchar2
	)
	is
	len_ integer;
	begin
	  len_ := length(x);
	  dbms_lob.append(res,'[');
	  if (x is not null) and (len_>0)  then 
		  dbms_lob.append(res,to_char(len_));
		  dbms_lob.append(res,']');
		  dbms_lob.append(res,x);
	  else
		  dbms_lob.append(res,'0]');
	  end if;
	end;

	procedure insertWithoutHeaderModifing(
		rez in out clob,
		pos_ in integer,
		x in varchar2
	)
	is
	len_     integer;
	oldSize  integer;
	tempClob clob;
	begin
	 len_ := length(x);
	 oldSize:=DBMS_LOB.GETLENGTH(rez);
	 dbms_lob.createTemporary(tempClob, true);
	 
	 DBMS_LOB.copy(tempClob, rez, oldSize, 1, 1);
	 
	 DBMS_LOB.copy(rez, tempClob, pos_-1, 1, 1);
	 DBMS_LOB.write(rez, len_, pos_,  x);
	 DBMS_LOB.copy(rez, tempClob, oldSize - pos_ + 1, pos_+ len_, pos_ );
	 
	end;

	function merge(
		e1 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(1);  
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  return res;
	end;

	function merge(
		e1 in number,
		e2 in number
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(2);
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e1));
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e2));
	  return res;
	end;

	function merge(
		e1 in varchar2,
		e2 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(2);
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  RDX_Array.appendWithoutHeaderModifying(res,e2);
	  return res;
	end;

	function merge(
		e1 in number,
		e2 in number,
		e3 in number
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(3);
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e1));
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e2));
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e3));
	  return res;
	end;

	function merge(
		e1 in varchar2,
		e2 in varchar2,
		e3 in varchar2
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(3);
	  RDX_Array.appendWithoutHeaderModifying(res,e1);
	  RDX_Array.appendWithoutHeaderModifying(res,e2);
	  RDX_Array.appendWithoutHeaderModifying(res,e3);
	  return res;
	end;

	function merge(
		e1 in number
	) return CLOB
	is
	  res CLOB; 
	begin
	  res:=RDX_Array.prepareClobArr(1);  
	  RDX_Array.appendWithoutHeaderModifying(res,to_char(e1));
	  return res;
	end;

	function fromStr(
		lob in clob
	) return ARR_STR deterministic
	is
	   arr ARR_STR := ARR_STR();
	   item VARCHAR2(32767);
	   temp VARCHAR2(30);
	   
	   pos1   INTEGER;
	   pos2   INTEGER;
	   len    INTEGER;   
	   ind_   INTEGER; 
	   size_  INTEGER; 
	begin
	   if (lob is null) or (DBMS_LOB.GETLENGTH(lob) < 2) then
	       return arr;
	   end if;
	   
	   pos1 := INSTR(lob, '[');
	   
	   if (pos1<2) then
	       return arr;
	   end if;   
	   
	   pos2  := INSTR(lob, ']', pos1);
	   
	   temp  := SUBSTR(lob, 1, pos1-1);
	   

	   
	   size_:= TO_NUMBER(temp);
	   
	   arr.Extend(size_);
	   
	   FOR ind_ IN 1 .. size_ 
	    loop        
	       len := -1;
	       temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	       if (temp is not null and LENGTH(temp) > 0) then
	           len := TO_NUMBER(temp);
	       else
	           len := 0;
	       end if;
	       
	       item := null;
	       if (len > -1) then
	           item := SUBSTR(lob, pos2+1, len);
	       end if;       
	 

	       pos1 := pos2 + len + 1;
	       pos2 := INSTR(lob, ']', pos1);

	       arr(ind_) := item;       
	   end loop;
	   
	   return arr;
	end;

	function fromArrStr(
		arr_ in ARR_STR
	) return clob
	is
	   res clob;
	   size_ integer;
	   temp VARCHAR2(30);
	begin
	 dbms_lob.createTemporary(res, true); 
	 if (arr_ is null) then
	   return res;
	 end if;
	 size_ := arr_.count();
	 temp := to_char(size_);
	 DBMS_LOB.writeappend(res, length(temp), temp);
	 if size_<>0 then
	    FOR element# IN arr_.FIRST() .. arr_.LAST() 
	       LOOP 
	          IF (arr_.EXISTS(element#))
	          THEN 
	             size_ := length(arr_(element#));             
	             if ((size_ is null) or (size_ = 0)) then
	                DBMS_LOB.writeappend(res, 3, '[0]');
	             else
	                temp := to_char(size_);
	                DBMS_LOB.writeappend(res, 1, '[');
	                DBMS_LOB.writeappend(res, length(temp), temp);
	                DBMS_LOB.writeappend(res, 1, ']');
	                DBMS_LOB.writeappend(res, size_, arr_(element#));         
	             end if;
	             
	          END IF;
	       END LOOP; 
	end if;

	return res;
	end;

	procedure appendStr(
		res in out clob,
		x in varchar2
	)
	is
	 arrSize integer;
	  
	begin
	 
	 arrSize := RDX_Array.getArraySize(res);
	 
	 RDX_Array.incrementHeader(res, arrSize);
	 
	 RDX_Array.appendWithoutHeaderModifying(res, x);     
	end;

	procedure appendNum(
		res in out clob,
		x in number
	)
	is
	begin
	RDX_Array.appendStr(res, RDX_ValAsStr.numToValAsStr(x));
	end;

	procedure appendDate(
		res in out clob,
		x in timestamp
	)
	is
	begin
	RDX_Array.appendStr(res, RDX_ValAsStr.dateTimeToValAsStr(x));
	end;

	procedure appendRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2
	)
	is
	begin
	RDX_Array.appendStr(res, RDX_Array.createRef(tableGuid, x));
	end;

	-- Нумерация осуществляется с единицы
	procedure insertStr(
		res in out clob,
		x in varchar2,
		index_ in integer
	)
	is
	 arrSize integer;
	 ind_    integer;
	 pos1 integer;
	 pos2 integer;
	 len integer;
	 temp varchar2(30);
	begin
	arrSize := RDX_Array.getArraySize(res);

	if index_ > arrSize then
	     RDX_Array.appendStr(res, x);
	else
	     RDX_Array.incrementHeader(res, arrSize);
	     arrSize:=arrSize+1;
	     
	     
	     pos1 := LENGTH(arrSize) + 1;
	     for  ind_ in 1 ..  index_-1  loop
	        pos2  := INSTR(res, ']', pos1);
	        
	        --len := 0;
	        temp := SUBSTR(res, pos1+1, pos2-pos1-1);
	        if (temp is not null and LENGTH(temp) > 0) then
	           len := TO_NUMBER(temp);
	        else
	           len := 0;
	        end if;
	      
	        pos1 := pos2 + len + 1;        
	     end loop;
	     if (x is null) then
	         RDX_Array.insertWithoutHeaderModifing(res, pos1, '[0]');
	     else
	         RDX_Array.insertWithoutHeaderModifing(res, pos1,'[' || to_char(LENGTH(x)) || ']' || x);
	     end if;
	     
	end if; 
	end;

	-- Нумерация осуществляется с единицы
	procedure insertNum(
		res in out clob,
		x in number,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_ValAsStr.numToValAsStr(x), index_);
	end;

	-- Нумерация осуществляется с единицы
	procedure insertDate(
		res in out clob,
		x in timestamp,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_ValAsStr.dateTimeToValAsStr(x), index_);
	end;

	procedure insertRef(
		res in out clob,
		tableGuid in varchar2,
		x in varchar2,
		index_ in integer
	)
	is
	begin
	  RDX_Array.insertStr(res, RDX_Array.createRef(tableGuid, x), index_);
	end;

	-- Нумерация осуществляется с единицы
	function getStr(
		lob in clob,
		index_ in integer
	) return varchar2 deterministic
	is
	 arrSize integer;
	 ind_    integer;
	 pos1 integer;
	 pos2 integer;
	 len integer;
	 temp varchar2(30);
	begin

	 arrSize := RDX_Array.getArraySize(lob);
	 if (index_>arrSize or index_<1)then
	  return null;
	 end if;


	 pos1 := LENGTH(arrSize) + 1;

	 for  ind_ in 1 ..  index_-1  loop
	    pos2  := INSTR(lob, ']', pos1);

	    temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	    if (temp is not null and LENGTH(temp) > 0) then
	       len := TO_NUMBER(temp);
	    else   
	       len := 0;
	    end if;

	    pos1 := pos2 + len + 1;        
	 end loop;

	 pos2  := INSTR(lob, ']', pos1);
	 temp := SUBSTR(lob, pos1+1, pos2-pos1-1);
	 if (temp is not null and LENGTH(temp) > 0) then
	     len := TO_NUMBER(temp);
	 else    
	     len := 0;
	 end if;

	 return SUBSTR(lob, pos2+1, len);
	  
	end;

	-- Нумерация осуществляется с единицы
	function getNum(
		lob in clob,
		index_ in integer
	) return number deterministic
	is
	begin
	 return RDX_ValAsStr.numFromValAsStr(RDX_Array.getStr(lob, index_));
	end;

	-- Нумерация осуществляется с единицы
	function getDate(
		lob in clob,
		index_ in integer
	) return timestamp deterministic
	is
	begin
	 return RDX_ValAsStr.dateTimeFromValAsStr(RDX_Array.getStr(lob, index_));
	end;

	-- Нумерация осуществляется с единицы
	function searchStr(
		lob in clob,
		what in varchar2,
		startIdx in integer
	) return number deterministic
	is
	begin
	 return RDX_Array.search(lob, what, startIdx, false);
	end;

	-- Нумерация осуществляется с единицы
	function searchNum(
		lob in clob,
		what in number,
		startIdx in number
	) return number deterministic
	is
	begin 
	 return RDX_Array.search(lob, what, startIdx, true);
	end;

	-- Нумерация осуществляется с единицы
	function searchDate(
		lob in clob,
		what in timestamp,
		startIdx in number
	) return number deterministic
	is
	begin
	  return RDX_Array.search(lob, RDX_ValAsStr.dateTimeToValAsStr(what), startIdx, false);
	end;

	function searchRef(
		lob in clob,
		tableGuid in varchar2,
		what in varchar2,
		startIdx in integer
	) return number deterministic
	is
	begin
	 return RDX_Array.search(lob, RDX_Array.createRef(tableGuid, what), startIdx, false);
	end;

	-- Нумерация осуществляется с единицы
	procedure setStr(
		lob in out clob,
		x in varchar2,
		Idx in number
	)
	is
	 allClobSize INTEGER;
	 arrSize INTEGER;
	 ind_    INTEGER;
	 pos1 INTEGER;
	 pos2 INTEGER;
	 len INTEGER;
	 newLen INTEGER;
	 tempPos INTEGER;
	 temp VARCHAR2(30);
	 tempPos2 INTEGER;
	 moveSize INTEGER;
	tempClob CLOB; 

	BEGIN
	 
	 arrSize := Rdx_Array.getArraySize(LOB);
	 IF (idx > arrSize) THEN
	   RETURN;
	 END IF;
	 
	 allClobSize:=DBMS_LOB.GETLENGTH(LOB);   
	 pos1 := LENGTH(arrSize) + 1;

	 FOR  ind_ IN 1 ..  idx-1  LOOP
	    pos2  := INSTR(LOB, ']', pos1);
	    temp := SUBSTR(LOB, pos1+1, pos2-pos1-1);
	    IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	       len := TO_NUMBER(temp);
	    else
	       len := 0;
	    END IF;

	    pos1 := pos2 + len + 1;        
	 END LOOP;


	 
	 IF x IS NULL THEN
	    newLen := 0;
	 ELSE
	    newLen := LENGTH(x);
	 END IF;
	 
	 pos2  := INSTR(LOB, ']', pos1);
	 temp := SUBSTR(LOB, pos1+1, pos2-pos1-1);
	 IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	     len := TO_NUMBER(temp);
	 ELSE
	     len := 0; 
	 END IF; 
	 
	 IF len = newLen THEN
	   IF len > 0 THEN
	     DBMS_LOB.WRITE(LOB, len, pos2+1,  x);
	   END IF;
	 ELSE   
	    temp := TO_CHAR(newLen);
	    tempPos2 := LENGTH(temp);
	    
	   IF (arrSize <> idx) THEN
	      dbms_lob.createTemporary(tempClob, TRUE);
	      tempPos := pos2 + len + 1;
		  moveSize := allClobSize - (tempPos) +2;
	      dbms_lob.copy(tempClob, LOB,  moveSize, 1, tempPos);
	         

	      dbms_lob. WRITE(LOB, tempPos2, pos1+1, temp);
	      dbms_lob.WRITE(LOB, 1, pos1+1+tempPos2, ']');
		  IF (newLen <> 0 )THEN
	        dbms_lob.WRITE(LOB, newLen, pos1+2+tempPos2, x);
		  END IF;
		   

	      dbms_lob. copy(LOB, tempClob, allClobSize - (tempPos) +2 ,  pos1 + 2 + tempPos2 + newLen, 1);
		  IF (newlen<len)THEN
	        dbms_lob.trim(LOB, newLen + pos1+ tempPos2 + moveSize);
	      END IF;  

	   ELSE 
	      dbms_lob. WRITE(LOB, tempPos2, pos1+1 , temp);
	      dbms_lob.WRITE(LOB, 1, pos1 +tempPos2+1, ']');
	      if x is not null then
	         dbms_lob.WRITE(LOB, newLen, pos1+2+tempPos2, x);
	      end if;      
	      IF (newLen < len) THEN
	        dbms_lob.trim(LOB, newLen + pos1+1+tempPos2);
	      END IF;  
	        
	   END IF;
	 END IF; 
	END;

	-- Нумерация осуществляется с единицы
	procedure setNum(
		lob in out clob,
		x in number,
		Idx in number
	)
	is
	begin
	  RDX_Array.setStr(lob, RDX_ValAsStr.numToValAsStr(x), idx);
	end;

	-- Нумерация осуществляется с единицы
	procedure setDate(
		lob in out clob,
		x in timestamp,
		idx in number
	)
	is
	begin
	 RDX_Array.setStr(lob, RDX_ValAsStr.dateTimeToValAsStr(x), idx);
	end;

	procedure setRef(
		lob in out clob,
		tableGuid in varchar2,
		x in varchar2,
		idx in number
	)
	is
	begin
	  RDX_Array.setStr(lob, RDX_Array.createRef(tableGuid, x), idx);
	end;

	procedure remove(
		res in out clob,
		index_ in integer
	)
	is
	  allClobSize INTEGER;
	 arrSize INTEGER;
	 ind_    INTEGER;
	 pos1 INTEGER;
	 pos2 INTEGER;
	 len INTEGER;
	 preffixLen integer; 
	 temp VARCHAR2(30); 
	 tempClob CLOB;
	 tailLen integer;  
	BEGIN 
	 arrSize := Rdx_Array.getArraySize(res);
	 IF (index_ > arrSize) THEN
	    RETURN;
	 END IF;
	     
	 preffixLen := length(to_char(arrSize-1));     
	decrementHeader(res, arrSize);
	 allClobSize:=DBMS_LOB.GETLENGTH(res);

	 pos1 := preffixLen + 1;

	 FOR  ind_ IN 1 ..  index_-1  LOOP
	    pos2  := INSTR(res, ']', pos1);
	    temp := SUBSTR(res, pos1+1, pos2-pos1-1);
	    IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	       len := TO_NUMBER(temp);
	    END IF;
	    pos1 := pos2 + len + 1;        
	 END LOOP;

	IF (arrSize = index_) THEN
	    dbms_lob.trim(res, pos1-1);

	else
	     pos2  := INSTR(res, ']', pos1);
	     temp := SUBSTR(res, pos1+1, pos2-pos1-1);
	     IF (temp IS NOT NULL AND LENGTH(temp) > 0) THEN
	         len := TO_NUMBER(temp);
	     ELSE
	         len := 0; 
	     END IF; 
	     len := len + pos2-pos1 + 1;
	     dbms_lob.createTemporary(tempClob, TRUE);
	     dbms_lob.copy(tempClob, res,  pos1 - 1, 1, 1);
	     tailLen := allClobSize - (pos1 + len - 1);     
	     dbms_lob.copy(tempClob, res,  tailLen,pos1,  pos1 + len );
	     dbms_lob.copy(res,  tempClob, pos1 + len);
	     dbms_lob.trim(res, allClobSize - len);
	end if;
	END;

	procedure removeAll(
		res in out clob,
		val in varchar2
	)
	IS
	   arrSize       INTEGER;
	   preffixLen    INTEGER;
	   tempStr       VARCHAR2 (32767);
	   pos1          INTEGER;
	   pos2          INTEGER;
	   temp          VARCHAR2 (30);
	   len           INTEGER;
	   removeCount   INTEGER;

	   TYPE ArrInt IS TABLE OF INTEGER
	                     INDEX BY BINARY_INTEGER;

	   fromArr       ArrInt;
	   lenArr        ArrInt;

	   ind_          INTEGER;
	   tempClob      CLOB;
	   flag          BOOLEAN;
	BEGIN
	   removeCount := 0;

	   arrSize := Rdx_Array.getArraySize (res);
	   preffixLen := LENGTH (TO_CHAR (arrSize));

	   ind_ := 1;

	   pos1 := preffixLen + 1;

	   fromArr (ind_) := pos1;

	   FOR i_ IN 1 .. arrSize
	   LOOP
	      pos2 := INSTR (res, ']', pos1);
	      temp := SUBSTR (res, pos1 + 1, pos2 - pos1 - 1);
	      flag := TRUE;

	      IF (temp IS NOT NULL AND LENGTH (temp) > 0)
	      THEN
	         len := TO_NUMBER (temp);
	      else   
	         len := 0;
	      END IF;   

	     tempStr := DBMS_LOB.SUBSTR (res, len, pos2 + 1);

	     IF ( (val IS NULL AND tempStr IS NULL)
	         OR (val IS NOT NULL AND tempStr IS NOT NULL AND tempStr = val))
	     THEN
	        lenArr (ind_) := pos1 - fromArr (ind_);
	        ind_ := ind_ + 1;
	        fromArr (ind_) := pos2 + len + 1;
	        removeCount := removeCount + 1;
	        flag := FALSE;
	     END IF;
	      

	      pos1 := pos2 + len + 1;
	   END LOOP;

	   IF removeCount = 0
	   THEN
	      RETURN;
	   END IF;

	   IF (flag)
	   THEN
	      lenArr (ind_) := pos1 - fromArr (ind_ - 1);
	   ELSE
	      lenArr (ind_) := 0;
	   END IF;

	   DBMS_LOB.createTemporary (tempClob, TRUE);

	   temp := TO_CHAR (arrSize - removeCount);
	   DBMS_LOB.append (tempClob, temp);
	   pos1 := LENGTH (temp) + 1;
	 
	   FOR i_ IN 1 .. ind_
	   LOOP
	      IF lenArr (i_) <> 0
	      THEN
	         DBMS_LOB.COPY (tempClob,
	                        res,
	                        lenArr (i_),
	                        pos1,
	                        fromArr (i_));
	         pos1 := pos1 + lenArr (i_);
	      END IF;
	   END LOOP;

	   pos1 := DBMS_LOB.GETLENGTH (tempClob);
	   DBMS_LOB.COPY (res, tempClob, pos1);
	   DBMS_LOB.TRIM (res, pos1);

	END;
end;
/

create or replace package body RDX_Arte as

		userName varchar2(250);
		stationName varchar2(250);
		clientLanguage varchar2(3);
	        clientCountry varchar2(3);
		defVersion number;
		sessionId number;

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
	   userName:=pUserName;
	   stationName:=pStationName;
	   clientLanguage:=pClientLanguage;
	   clientCountry:=pClientCountry;
	   sessionId:=pSessionId;
	   defVersion:=pDefVersion;
	   --dbms_application_info.set_client_info('ARTE #'||pArteInstSeq||': SAP-'||pSapId||' CLIENT-'||pClientAddress||' DB-'||dbms_debug_jdwp.current_session_id||'/'||dbms_debug_jdwp.current_session_serial);
	   RDX_Environment.setRequestInfo(requestInfo => 'U#'|| pUnitId || '-S#' || pSapId || '-' || pClientAddress);
	   if pSessionId is not null then
	        RDX_Arte.setSessionIsActive(pSessionId => pSessionId, pIsActive => 1);
	   end if;
	end;

	function getUserName return varchar2
	is
	begin
	  return userName;
	end;

	function getVersion return integer
	is
	begin
	    if defVersion is null then
	       raise_application_error(-20001,'Definition version is not deployed to database. '||chr(10)||'onStartRequestExecution() method of Arte (java class) should have been called  '||chr(10)||'before the database request executing.');
	    end if;
	  return defVersion;
	end;

	function getStationName return varchar2
	is
	begin
	  return stationName;
	end;

	function getClientLanguage return integer
	is
	begin
	  return clientLanguage;
	end;

	function getSessionId return integer
	is
	begin
	  return sessionId;
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

create or replace package body RDX_DDS_META as

	Type TArrInt is table of Integer index by binary_integer;

	function getVersion(
		versionAsStr in varchar2
	) return TArrInt deterministic
	is
	 rez TArrInt;
	 curIndex integer;
	 pos integer;
	 newPos integer;
	 buffer varchar2(32767);
	 
	begin
	 
	 if (versionAsStr is null) then
	   rez(1):=0;
	   return rez;
	 end if;
	 pos:=1;
	 
	 <<l>> while true loop
	   
	   newPos := instr(versionAsStr, '.', pos);
	   
	   if newPos = 0 then
	      buffer := substr(versionAsStr, pos);
	   else
	      buffer := substr(versionAsStr, pos, newPos - pos + 1);
	      pos := newPos+1;
	   end if;
	   
	   curIndex := rez.count() + 1;
	   rez(curIndex) := to_number(buffer);
	   
	   if newPos = 0 then
	      exit l;
	   end if;
	   
	 end loop;
	 
	 dbms_output.put_line('---------------------------------------------');
	 dbms_output.put_line(rez.count());
	 dbms_output.put_line('~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~');
	  if (rez.first() is not null) then
	   for i in rez.first() .. rez.last() 
	   loop 
	     if rez.exists(i) then 
	        dbms_output.put_line(to_char(i) || ') ' || to_char(rez(i)));
	     end if;
	   end loop;                               
	 end if; 
	 dbms_output.put_line('---------------------------------------------');
	 return rez;
	 
	end;

	function compareVersions(
		versionAsStr1 in varchar2,
		versionAsStr2 in varchar2
	) return integer deterministic
	is
	    ver1 TArrInt;
	    ver2 TArrInt;
	    i integer;
	 
	begin
	    ver1 := RDX_DDS_META.getVersion(versionAsStr1);
	    ver2 := RDX_DDS_META.getVersion(versionAsStr2);
	 
	    i := 1;
	 
	    while (i <= ver1.count()) and (i <= ver2.count())
	    loop
	        if ver1(i) > ver2(i) then        
	            return 1;
	        end if;
	        if ver1(i) < ver2(i) then
	            return -1;
	        end if;
	        i:=i+1;
	    end loop;
	    
	    while (i <= ver1.count()) 
	    loop
	        if ver1(i) != 0 then
	            return 1;
	        end if;
	        i:=i+1;
	    end loop;
	    
	    while (i <= ver2.count()) 
	    loop
	        if ver2(i) != 0 then
	            return -1;
	        end if;
	        i:=i+1;
	    end loop;
	    return 0;
	end;

	function isDbOptionEnabled(
		optionQualifiedName in varchar2
	) return integer
	is
	begin
	    for cur in (select 1 from RDX_DDSOPTION where layerUri || '\' || name = optionQualifiedName and value = 'ENABLED') loop
	        return 1;
	    end loop;
	    return 0;
	end;

	function isDbOptionEnabled(
		optionLayerUri in varchar2,
		optionSimpleName in varchar2
	) return integer
	is
	begin
	return RDX_DDS_META.isDbOptionEnabled(optionLayerUri || '\' || optionSimpleName);
	end;
end;
/

create or replace package body RDX_Entity as

	TYPE StrByInt IS TABLE OF varchar2(100) INDEX BY BINARY_INTEGER;
	upValTabByValType StrByInt;

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
	        '(select * from '|| upValTabByValType(pPropValType) ||'  where DEFID = :1 and OWNERENTITYID = :2 and OWNERPID = :3 )'||
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
	   i := scheduledOnDelUpOwnerQueue.first;
	   while i is not null loop 
	      if scheduledOnDelUpOwnerQueue(i).sOwnerEntityId = pEntityId then
	         mustDie(mustDie.count+1) := scheduledOnDelUpOwnerQueue(i).sOwnerPid;                
	         j := i;
	         i := scheduledOnDelUpOwnerQueue.next(i); 
	         scheduledOnDelUpOwnerQueue.delete(j);                 
	      else
	         i := scheduledOnDelUpOwnerQueue.next(i);           
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
	   scheduledOnDelUpOwnerQueue(scheduledOnDelUpOwnerQueue.count + 1).sOwnerEntityId :=pEntityId;  
	   scheduledOnDelUpOwnerQueue(scheduledOnDelUpOwnerQueue.count).sOwnerPid    := pInstancePid;  
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
	    upTabName := upValTabByValType(pPropValType);
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

	        --filled after creating jdbc connection
		instanceId integer;
	        sessionOwnerType varchar2(100);
	        sessionOwnerId integer;
	        --other
	        targetExecutorId integer := -1;
	        requestInfo varchar2(500);

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
	    
	    info := RDX_Environment.calcProgramName(instanceId) || '/' || RDX_Environment.calcModuleName(sessionOwnerType, sessionOwnerId);
	    
	    if requestInfo is not null then
	        info := info || '/' || requestInfo;
	    end if;
	    
	    dbms_application_info.set_client_info(info);
	    dbms_application_info.set_action(requestInfo);
	end;

	procedure init(
		instanceId in integer,
		sessionOwnerType in varchar2,
		sessionOwnerId in integer
	)
	is
	begin
	    RDX_Environment.instanceId := instanceId;
	    RDX_Environment.sessionOwnerType := sessionOwnerType;
	    RDX_Environment.sessionOwnerId := sessionOwnerId;
	    RDX_Environment.updateApplicationClientInfo();
	    dbms_application_info.set_module(RDX_Environment.calcProgramName(instanceId) || '/' || RDX_Environment.calcModuleName(sessionOwnerType, sessionOwnerId), null);
	end;

	procedure setRequestInfo(
		requestInfo in varchar2
	)
	is
	begin
	    RDX_Environment.requestInfo := requestInfo;
	    RDX_Environment.updateApplicationClientInfo();
	end;

	function getTargetExecutorId return integer
	is
	begin
	    if targetExecutorId = -1 then
	        select RDX_INSTANCE.TARGETEXECUTORID into targetExecutorId from RDX_INSTANCE where RDX_INSTANCE.ID = instanceId;
	    end if;
	    return targetExecutorId;
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
	    return instanceId;
	end;

	function getSessionOwnerType return varchar2
	is
	begin
	return sessionOwnerType;
	end;

	function getSessionOwnerId return integer
	is
	begin
	    return sessionOwnerId;
	end;
end;
/

create or replace package body RDX_JS_CalendarSchedule as

	--constants
	class_guid_abs_calendar CONSTANT VARCHAR(29) := 'aclVV67ZO4QF5GJ7KY7J2AW6UMQUI';
	class_guid_day_of_week CONSTANT VARCHAR(29) := 'aclHUCM56OUWZDBXCTBJLJ2S6QQ7E';
	class_guid_day_of_month CONSTANT VARCHAR(29) := 'aclPJITOKTE6ZDY5OXI7Z53SB222I';
	class_guid_abs_date CONSTANT VARCHAR(29) := 'aclRFJRGJ5REZDHDDTBB5R34QG4GQ';
	class_guid_inc_calendar CONSTANT VARCHAR(29) := 'aclDWOG2C7CWVCWBFOEMH62ET56HI';
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

	                WHEN class_guid_inc_calendar THEN
	                    vIsInIncCalendar := RDX_JS_CalendarSchedule.isInCached(pId => vCurItem.incCalendarId, pDate => vCurDate);
	                    IF vIsInIncCalendar is not null THEN
	                         vResult := (vCurItem.oper = '+' and vIsInIncCalendar = true) or (vCurItem.oper = '-' and vIsInIncCalendar = false); 
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
	    if pNow > pEndTime THEN
	        return (TRUNC((pEndTime-pStartTime)/pPeriod))*pPeriod + pStartTime;
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
	            select from_tz(pDateTime, sessiontimezone) at time zone (schedItem.timeZoneRegion) into tmpFrom from dual;            
	            tmpToday := Trunc(tmpFrom);
	            tmpTomorrow := tmpToday + interval '1' day;
	            tmpFromDaySeconds := RDX_JS_EventSchedule.getDaySeconds(tmpFrom);
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
	                select (from_tz(tmpTimestamp, schedItem.timeZoneRegion)) at time zone sessiontimezone into tmpTimestamp from dual;                     
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
	            select from_tz(pDateTime, sessiontimezone) at time zone (schedItem.timeZoneRegion) into tmpFrom from dual;            
	            tmpToday := Trunc(tmpFrom);
	            tmpYesterday := tmpToday - interval '1' day;
	            tmpFromDaySeconds := RDX_JS_EventSchedule.getDaySeconds(tmpFrom);
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
	                select (from_tz(tmpTimestamp, schedItem.timeZoneRegion)) at time zone sessiontimezone into tmpTimestamp from dual;                     
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

	CURSOR cIntervalSchedule (SchId RDX_JS_INTERVALSCHDITEM.SCHEDULEID%TYPE)  is
	                          Select CALENDARID, ENDTIME, STARTTIME
	                          From RDX_JS_INTERVALSCHDITEM
	                          Where SCHEDULEID=SchId;

	function isIn(
		pId in integer,
		pDateTime in date
	) return integer
	is
	    ts timestamp;
	    minutes int;
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

	TASK_EXECUTOR_CLASS_NAME constant varchar2(200) := 'org.radixware.ads.mdlOWL3XYSM2ZDNHOW7W7XKT54GRE.server.adc2YO4JA7JKJGGBLTARIEQHUSHGI';
	TASK_EXECUTOR_METHOD_NAME constant varchar2(200) := 'mthA4SZ4ODO4NFL3A5KMYIJ54MTZA';

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
		iTaskId in integer := null
	) return integer
	is
	   iJobNewID number(18);
	   
	begin
	   select SQN_RDX_JS_JOBID.NextVal into iJobNewID from dual;
	               
	   insert into RDX_JS_JOBQUEUE(ID,CREATORENTITYGUID, CREATORPID, DUETIME, CLASSNAME, METHODNAME, PRIORITY, CURPRIORITY, PRIORITYBOOSTINGSPEED, EXECREQUESTERID, TITLE, SCPNAME, TASKID, EXECUTORID, ALLOWRERUN)
	   values (iJobNewID, sCreatorEntityGuid, sCreatorPid,tJobTime, sJobClass, sJobMethod, nvl(iJobPriority,5), nvl(iJobPriority,5), iJobBoosting, sExecRequesterId, sTitle, sScpName, iTaskId, RDX_Environment.getTargetExecutorId(), iAllowRerun);
	   
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
	begin
	  select           
	    t.PRIORITY, t.PRIORITYBOOSTINGSPEED, t.STATUS, t.TITLE,
	    (select t1.SCPNAME from RDX_JS_TASK t1 
	        where t1.SCPNAME is not null
	        start with t1.ID = t.ID
	        connect by prior t1.PARENTID = t1.ID and prior t1.SCPNAME is null) 
	    into priority, priorityBoostingSpeed, curStatus, taskTitle, scpName
	  from RDX_JS_TASK t where t.ID = taskId for update of t.STATUS;
	  
	  if curStatus = 'Scheduled' or curStatus = 'Executing' or curStatus = 'Cancelling' then
	    raise_application_error(-20000, 'Task #' || to_char(taskId) || ' has wrong status:' || curStatus);
	  end if;
	  
	  select nvl(execTime, sysdate) into actualExecTime from dual;
	  
	  jobTitle := 'Initial job for task #' || to_char(taskId) || ' ''' || taskTitle || ''' created by ' || initiatorName;
	  
	  vJobId := RDX_JS_JOB.schedule(iAllowRerun => 0, tJobTime => actualExecTime, sJobClass => TASK_EXECUTOR_CLASS_NAME, sJobMethod => TASK_EXECUTOR_METHOD_NAME, iJobPriority => priority, iJobBoosting => priorityBoostingSpeed, sExecRequesterId => 'ScheduledTask-'|| taskId, sCreatorEntityGuid => 'tblWZB7K4HLJPOBDCIUAALOMT5GDM', sCreatorPid => taskId, sTitle => jobTitle, sScpName => scpName, iTaskId => taskId);
	    
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
	    
	    if pClassName = TASK_EXECUTOR_CLASS_NAME and pMethodName = TASK_EXECUTOR_METHOD_NAME then
	        return 1;
	    end if;
	    return 0;
	end;
end;
/

create or replace package body RDX_License as

	procedure dailyMaintenance
	is
	   lastSent date;
	begin
	    select max(RDX_LICENSEREPORTLOG.CREATEDATE) into lastSent from RDX_LICENSEREPORTLOG where RDX_LICENSEREPORTLOG.SENT > 0;    
	    if lastSent is null then
	        RDX_Trace.put(2,'No license reports were sent to product vendor','App.License',0);
	    else 
	        if lastSent > (sysdate - 30) then
	            RDX_Trace.put(2,'Last sent license report expired', 'App.License',0);
	        end if;
	    end if;
	end;
end;
/

create or replace package body RDX_Lock as

	function request(
		pTabId in varchar2,
		pPid in varchar2,
		pTimeoutSec in integer,
		pLockId in out varchar2
	) return integer
	is
	begin
	    pLockId := RDX_Lock.getLockHandle(pTabId, pPid);
	    return DBMS_LOCK.REQUEST(pLockId, DBMS_LOCK.X_MODE, nvl(pTimeoutSec, DBMS_LOCK.MAXWAIT) , false);
	end;

	function release(
		pLockId in varchar2
	) return integer
	is
	begin
	    return DBMS_LOCK.RELEASE(pLockId);
	end;

	function getLockHandle(
		tableId in varchar2,
		pid in varchar2
	) return integer
	is
	    PRAGMA AUTONOMOUS_TRANSACTION;
	    res integer;
	begin
	    DBMS_LOCK.ALLOCATE_UNIQUE(RDX_Lock.getLockName(tableId, pid), res);
	    return res;
	end;

	function getLockName(
		tableId in varchar2,
		pid in varchar2
	) return varchar2
	is
	begin
	    return 'http://dblock.radixware.org/' ||  SYS_CONTEXT('userenv','current_schema') || '/' || tableId || '/' || pid;
	end;
end;
/

create or replace package body RDX_Maintenance as

	procedure clearSessions(
		pIsActive in integer
	)
	is
	   limitMins integer;
	begin
	    if (pIsActive = 0) then
	        select EASSESSIONINACTIVITYMINS into limitMins from RDX_SYSTEM where ID = 1;
	    else 
	        select EASSESSIONACTIVITYMINS into limitMins from RDX_SYSTEM where ID = 1;
	    end if;
	    delete 
	        from RDX_EASSESSION
	        where ISACTIVE = pIsActive and
	            (sysdate - LASTCONNECTTIME)*60*24 > limitMins;
	end;

	-- Perform daily maintenance
	procedure daily
	is
	begin       
	   RDX_Trace.put(1, 'Radix System maintenance started', 'Arte.Db');
	   
	   --RDX_Trace.put(0, 'Oracle audit cleared', 'Arte.Db');
	   
	   RDX_AUDIT.dailyMaintenance();
	   RDX_Trace.put(0, 'Radix audit maintenance finished', 'Arte.Db');
	   
	   RDX_Trace.maintenance;
	   RDX_Trace.put(0,  'Event log cleared', 'Arte.Db');
	   
	   RDX_Maintenance.clearSessions(pIsActive => 0); 
	   RDX_Maintenance.clearSessions(pIsActive => 1); 
	   RDX_Trace.put(0,  'Session log cleared', 'Arte.Db');
	   
	   RDX_Maintenance.clearProfileLog;
	   RDX_Trace.put(0,  'Profiler log cleared', 'Arte.Db');
	   
	   RDX_Trace.clearSensitiveData;
	   RDX_Trace.put(0,  'Sensitive data removed from Event Log', 'Arte.Db');
	   
	   RDX_SM_METRIC.dailyMaintenance;
	   RDX_Trace.put(0,  'Metrics history cleared', 'Arte.Db');
	   
	   RDX_License.dailyMaintenance();
	   RDX_Trace.put(0, 'License maintenance finished', 'Arte.Db');
	   
	   RDX_WF_Maintenance.daily;
	   RDX_PC_Maintenance.daily;
	   
	   RDX_Trace.put(1,  'Radix System maintenance finished', 'Arte.Db');
	end;

	-- Ежеминутный (примерно) maintenance
	procedure continualy
	is
	begin       
	   RDX_Maintenance.clearSessions(pIsActive => 0);             
	end;

	procedure clearProfileLog
	is
	   pStoreDays number;
	begin
	   select PROFILELOGSTOREDAYS into pStoreDays from RDX_SYSTEM where ID =1;
	   loop  
	      delete /*+ index(RDX_PROFILERLOG PK_RDX_PROFILERLOG) */ from RDX_PROFILERLOG where PERIODENDTIME < sysdate - pStoreDays and ROWNUM <= 10000;
	      exit when SQL%NOTFOUND;
	      commit;
	   end loop;
	end;
end;
/

create or replace package body RDX_Net as

	function getNewStan(
		unitId in integer
	) return integer
	is         
	   PRAGMA AUTONOMOUS_TRANSACTION;
	   vStan integer;
	   vMaxStan integer;
	begin
	   --select LASTOUTSTAN, MAXSTAN into vStan, vMaxStan from RDX_NETHUB where ID = unitId for update of LASTOUTSTAN;
	   --vStan := mod(nvl(vStan, 0)+1, vMaxStan + 1);
	   --update RDX_NETHUB set LASTOUTSTAN = vStan where ID = unitId;
	   --commit;
	   return 1;
	end;
end;
/

create or replace package body RDX_PC_Maintenance as

	procedure daily
	is
	  storeDays integer;
	  storeFailedDays integer;
	begin
	  Select PCMSTOREDAYS into storeDays from RDX_SYSTEM where ID=1;
	  Select RDX_SYSTEM.FAILEDOUTMESSAGESTOREDAYS into storeFailedDays from RDX_SYSTEM where ID=1;
	  Delete from RDX_PC_SENTMESSAGE where DUETIME+storeDays < SYSDATE;
	  Delete from RDX_PC_RECVMESSAGE where RECVTIME+storeDays < SYSDATE;
	  Insert into RDX_PC_SENTMESSAGE (RDX_PC_SENTMESSAGE.ADDRESS, RDX_PC_SENTMESSAGE.BODY, RDX_PC_SENTMESSAGE.CALLBACKCLASSNAME, RDX_PC_SENTMESSAGE.CALLBACKMETHODNAME, RDX_PC_SENTMESSAGE.CHANNELID,
	          RDX_PC_SENTMESSAGE.CHANNELKIND, RDX_PC_SENTMESSAGE.CREATETIME, RDX_PC_SENTMESSAGE.DESTENTITYGUID, RDX_PC_SENTMESSAGE.DESTPID, RDX_PC_SENTMESSAGE.DUETIME, RDX_PC_SENTMESSAGE.EXPIRETIME,
	          RDX_PC_SENTMESSAGE.HISTMODE, RDX_PC_SENTMESSAGE.ID, RDX_PC_SENTMESSAGE.IMPORTANCE, 
	          RDX_PC_SENTMESSAGE.SENDERROR, 
	          RDX_PC_SENTMESSAGE.SENTTIME, RDX_PC_SENTMESSAGE.SMPPBYTESSENT, RDX_PC_SENTMESSAGE.SMPPCHARSSENT, RDX_PC_SENTMESSAGE.SMPPENCODING, RDX_PC_SENTMESSAGE.SMPPPARTSSENT, RDX_PC_SENTMESSAGE.SOURCEENTITYGUID,
	          RDX_PC_SENTMESSAGE.SOURCEMSGID, RDX_PC_SENTMESSAGE.SOURCEPID, RDX_PC_SENTMESSAGE.SUBJECT
	        ) 
	  select RDX_PC_OUTMESSAGE.ADDRESS, RDX_PC_OUTMESSAGE.BODY, RDX_PC_OUTMESSAGE.CALLBACKCLASSNAME, RDX_PC_OUTMESSAGE.CALLBACKMETHODNAME, RDX_PC_OUTMESSAGE.CHANNELID,
	          RDX_PC_OUTMESSAGE.CHANNELKIND, RDX_PC_OUTMESSAGE.CREATETIME, RDX_PC_OUTMESSAGE.DESTENTITYGUID, RDX_PC_OUTMESSAGE.DESTPID, RDX_PC_OUTMESSAGE.DUETIME, RDX_PC_OUTMESSAGE.EXPIRETIME,
	          RDX_PC_OUTMESSAGE.HISTMODE, RDX_PC_OUTMESSAGE.ID, RDX_PC_OUTMESSAGE.IMPORTANCE, 
	          'Permanent transmission failure for this message was detected and keep time for failed messages was exhausted. Last send error was '||RDX_PC_OUTMESSAGE.FAILEDMESSAGE||', retry count was ' || to_char(RDX_PC_OUTMESSAGE.FAILEDTRYCOUNT,'999999'),
	          SYSDATE, 0, 0, RDX_PC_OUTMESSAGE.SMPPENCODING, 0, RDX_PC_OUTMESSAGE.SOURCEENTITYGUID,
	          RDX_PC_OUTMESSAGE.SOURCEMSGID, RDX_PC_OUTMESSAGE.SOURCEPID, RDX_PC_OUTMESSAGE.SUBJECT
	  from RDX_PC_OUTMESSAGE where RDX_PC_OUTMESSAGE.FAILEDLASTSENDDATE+storeFailedDays < SYSDATE;
	end;
end;
/

create or replace package body RDX_PC_Utils as

	function findSuitableChannelId(
		pKind in varchar2,
		pAddress in varchar2,
		pImportance in integer,
		pExcludeUnitId in integer,
		pPrevPriority in integer
	) return integer
	is
	    res number(9,0);
	begin
	    select unitId into res 
	    from (select * from
	    (select RDX_PC_CHANNELUNIT.ID unitId from RDX_PC_CHANNELUNIT inner join RDX_UNIT on RDX_PC_CHANNELUNIT.ID=RDX_UNIT.ID 
	    where 
	        RDX_UNIT.USE != 0 
	        and (pExcludeUnitId is null or RDX_PC_CHANNELUNIT.ID != pExcludeUnitId) 
	        and RDX_PC_CHANNELUNIT.KIND = pKind
	        and regexp_like(RDX_PC_Utils.extractActualAddress(pKind, pAddress), RDX_PC_CHANNELUNIT.MESSADDRESSREGEXP) 
	        and pImportance >= RDX_PC_CHANNELUNIT.MINIMPORTANCE and pImportance <= RDX_PC_CHANNELUNIT.MAXIMPORTANCE 
	    order by 
	        (case when pPrevPriority is null or RDX_PC_CHANNELUNIT.ROUTINGPRIORITY < pPrevPriority then 1 else 0 end) desc, 
	        RDX_UNIT.STARTED desc, 
	        (RDX_PC_CHANNELUNIT.ROUTINGPRIORITY * 10 + dbms_random.value) desc, 
	        RDX_PC_CHANNELUNIT.ID
	    )) where rownum < 2;
	    return res;
	exception when others then return null;
	end;

	function extractActualAddress(
		kind in varchar2,
		address in varchar2
	) return varchar2
	is
	    actualAddress varchar2(1000);
	    addressXml XmlType;
	begin
	    if kind in ('Gcm', 'Apns') then
	        select xmltype(address) into addressXml from dual;
	        select extractValue(addressXml, '/pc:PushNotificationAddress/pc:AppName', 'xmlns:pc="http://schemas.radixware.org/personalcommunications.xsd"') into actualAddress from dual;
	    else
	        actualAddress := address;
	    end if;
	    return actualAddress;
	    exception when others then return null;
	end;
end;
/

create or replace package body RDX_SM_METRIC as

	procedure dailyMaintenance
	is
	    TYPE metricStateRec is RECORD (
	        metricId    RDX_SM_METRICSTATE.ID%TYPE,
	        lastHistId    RDX_SM_METRICSTATE.LASTHISTID%TYPE
	    );
	    TYPE metricStateRecs is table of metricStateRec;
	    
	    storeDays integer;
	    metricStates metricStateRecs;    
	begin
	    select RDX_SYSTEM.METRICHISTORYSTOREDAYS into storeDays from RDX_SYSTEM where RDX_SYSTEM.ID = 1;
	    
	    select RDX_SM_METRICSTATE.ID, RDX_SM_METRICSTATE.LASTHISTID
	    BULK COLLECT INTO metricStates
	    from RDX_SM_METRICSTATE;
	    
	    FORALL indx in 1 .. metricStates.COUNT
	        delete /*+ index(RDX_SM_METRICHIST PK_RDX_SM_METRICHIST) */ from RDX_SM_METRICHIST
	        where RDX_SM_METRICHIST.ENDTIME < (sysdate - storedays)
	        and RDX_SM_METRICHIST.METRICID = metricStates(indx).metricId
	        and RDX_SM_METRICHIST.ID != metricStates(indx).lastHistId;
	end;
end;
/

create or replace package body RDX_Trace as

	-- for internal use
	function put_internal(
		pCode in varchar2,
		pWords in clob,
		pComponent in varchar2,
		pSeverity in number,
		pContextTypes in varchar2,
		pContextIds in varchar2,
		pTime in timestamp := NULL,
		pUserName in varchar2 := NULL,
		pStationName in varchar2 := NULL,
		pIsSensitive in integer := 0
	) return integer
	is
	   vId  integer;                          
	   time TIMESTAMP;
	   sensTraceOn  integer;
	   pt  integer;
	   pt2 integer;
	   pi  integer;
	   pi2 integer;
	   ct  varchar2(250);
	   ci  varchar2(250);                                   
	   eDublicated         exception;
	   PRAGMA EXCEPTION_INIT(eDublicated,-1);  -- ORA-00001 unique constraint
	   PRAGMA AUTONOMOUS_TRANSACTION;
	begin
	    if pIsSensitive != 0 then
	        select ENABLESENSITIVETRACE into sensTraceOn from RDX_SYSTEM where ID = 1;
	        if sensTraceOn = 0 then 
	            commit;    
	            return null;
	        end if;    
	    end if;
	   select SQN_RDX_EVENTLOGID.NextVal into vId from DUAL;
	   if pTime is null then
	       time:=SYSTIMESTAMP;
	   else   
	       time := pTime;              
	   end if;
	   insert into RDX_EVENTLOG (RAISETIME, ID, CODE, WORDS, SEVERITY, COMPONENT, USERNAME, STATIONNAME, ISSENSITIVE)
	                 values (time, vId, pCode, pWords, pSeverity, pComponent, pUserName, pStationName, pIsSensitive);
	   pt:=1; pi:=1;
	   loop
	      exit when pContextTypes is null or pt>=length(pContextTypes) or
	                pContextIds is null or pi >= length(pContextIds);
	      pt2:=instr(pContextTypes,chr(0),pt);
	      ct:=substr(pContextTypes,pt,pt2-pt);
	      pi2:=instr(pContextIds,chr(0),pi); 
	      if (pi2 > pi) then
	         ci:=substr(pContextIds,pi,pi2-pi);
	      else
	         ci:='-';
	      end if;
	      pt:=pt2+1;  pi:=pi2+1;      
	      begin
	          insert into RDX_EVENTCONTEXT (TYPE, ID, RAISETIME, EVENTID)
	                            values (ct,ci,time,vId);
	      exception
	          when eDublicated then continue;
	      end;                      
	   end loop;   
	   commit;
	   return vId;
	end;

	procedure put(
		pSeverity in integer,
		pCode in varchar2,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	begin
	  RDX_Trace.put_1word(pSeverity => pSeverity, pCode => pCode, pWord1 => pMess, pSource => pSource, pIsSensitive => pIsSensitive);
	end;

	procedure put(
		pSeverity in integer,
		pMess in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	begin
	  RDX_Trace.put(pSeverity => pSeverity, pCode => NULL, pMess => pMess, pSource => pSource, pIsSensitive => pIsSensitive); 
	end;

	procedure put_1word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_2word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure put_3word(
		pSeverity in integer,
		pCode in varchar2,
		pWord1 in varchar2,
		pWord2 in varchar2,
		pWord3 in varchar2,
		pSource in varchar2,
		pIsSensitive in integer := 0
	)
	is
	  vId integer;
	begin
	  vId := RDX_Trace.put_internal(pCode, RDX_Array.merge(e1 => pWord1, e2 => pWord2, e3 => pWord3), pSource, pSeverity, null, null, null, null, null, pIsSensitive); 
	end;

	procedure deleteDebug
	is
	begin
	   loop  
	      delete from RDX_EVENTLOG where RDX_EVENTLOG.SEVERITY = 0 and ROWNUM <= 1000;
	      exit when SQL%NOTFOUND;
	      commit;
	   end loop;
	end;

	procedure clearSensitiveData(
		pForce in boolean := false
	)
	is
	   isOn integer default 0;
	begin
	   if not pForce then
	       select ENABLESENSITIVETRACE
	         into isOn 
	         from RDX_SYSTEM where ID=1;
	   end if;  
	   if isOn = 0 then
	       loop  
	          update /*+ index(RDX_EVENTLOG IDX_RDX_EVENTLOG_HASSENSITIVE) */  RDX_EVENTLOG set WORDS = null where ISSENSITIVE > 0 and WORDS is not null and ROWNUM <= 10000;
	          exit when SQL%NOTFOUND;
	          commit;
	       end loop;
	   end if;    
	end;

	procedure createPartition(
		d in date
	)
	is
	begin
	  commit;
	  insert into RDX_EVENTLOG(RAISETIME, ID, WORDS, SEVERITY, COMPONENT) values (d, SQN_RDX_EVENTLOGID.NextVal, 'x',0,'x');
	  insert into RDX_EVENTCONTEXT(RAISETIME, EVENTID, TYPE, ID) values (d, SQN_RDX_EVENTLOGID.CurrVal, 'x','x');
	  rollback;
	end;

	procedure dropEventPartitionsOlderThan(
		d in date
	)
	is
	 NOT_EXIST exception;   PRAGMA EXCEPTION_INIT(NOT_EXIST, -2149);
	 THE_ONLY exception;    PRAGMA EXCEPTION_INIT(THE_ONLY, -14083);
	 THE_LAST exception;    PRAGMA EXCEPTION_INIT(THE_LAST, -14758);
	 upperExclusiveBoundary date;
	begin
	    for r in (select *
	              from   all_tab_partitions tp
	              where  (tp.table_name = 'RDX_EVENTLOG' or tp.table_name = 'RDX_EVENTCONTEXT')
	              and    tp.table_owner = (SYS_CONTEXT('USERENV', 'CURRENT_SCHEMA')))
	    loop
	        execute immediate 'SELECT ' || r.high_value || ' from dual'
	            into upperExclusiveBoundary;
	        if upperExclusiveBoundary <= d then
	            if upper(r.partition_name) not like '%' || upper('RDX_EVENTLOG') || '%' and  upper(r.partition_name) not like '%' || upper('RDX_EVENTCONTEXT') || '%' then
	                begin                                      
	                    execute immediate 'alter table ' || r.table_owner || '.' || r.table_name || ' drop partition ' ||
	                                  r.partition_name;
	                    RDX_Trace.put(0,  'Partition of ' || r.table_name || ' for less than ' || r.high_value ||' dropped', 'App.Db');
	                exception when NOT_EXIST or THE_ONLY or THE_LAST then null;
	                end;
	            END IF;
	        END IF;
	    END LOOP;
	end;

	procedure maintenance
	as
	  d date; 
	  res boolean;
	begin
	  --удаление устаревших разделов
	  select trunc(sysdate)-RDX_SYSTEM.EVENTSTOREDAYS into d from RDX_SYSTEM where id=1;
	  #IF DB_TYPE == "ORACLE" AND isEnabled("org.radixware\\Partitioning") THEN
	  RDX_Trace.dropEventPartitionsOlderThan(d);   
	  --подготовка будущих разделов
	  RDX_Trace.createPartition(sysdate+1);
	  RDX_Trace.createPartition(sysdate+2);
	  #ELSE
	  loop  
	     delete from RDX_EVENTCONTEXT where RDX_EVENTCONTEXT.RAISETIME < d and ROWNUM < 10000;
	     exit when SQL%NOTFOUND;
	     commit;
	  end loop;
	  loop  
	     delete from RDX_EVENTLOG where RDX_EVENTLOG.RAISETIME < d and ROWNUM < 10000;
	     exit when SQL%NOTFOUND;
	     commit;
	  end loop;
	  #ENDIF
	end;

	function put_records(
		pRecords in RDX_EVENT_LOG_RECORDS
	) return integer
	is
	  rec RDX_EVENT_LOG_RECORD;
	  vId integer := null;  
	begin
	  if pRecords is null then return null; end if;
	  for idx in 1 .. pRecords.count
	  loop
	    rec := pRecords(idx);
	    vId := rdx_trace.put_internal(rec.code, rec.words, rec.component, rec.severity, rec.contextTypes, rec.contextIds, rec.time, rec.userName, rec.stationName, rec.isSensitive);
	  end loop;
	  return vId;
	end;
end;
/

create or replace package body RDX_ValAsStr as

	function dateTimeToValAsStr(
		pDateTime in timestamp
	) return varchar2 deterministic
	is
	    res varchar2(100);
	begin
	    res := replace(to_char(pDateTime, 'yyyy-mm-dd HH24:mi:SS.FF'),' ','T');
	  --  replace(to_char(systimestamp, 'yyyy-mm-dd HH24:mi:SS.FF'),' ','T')    
	    return substr(res, 1, length(res) - 6);
	end;

	function dateTimeFromValAsStr(
		pValAsStr in varchar2
	) return timestamp deterministic
	is 
	begin
	    return to_timestamp(replace(pValAsStr, 'T', ' '), 'yyyy-mm-dd HH24:mi:SS.FF');
	end;

	function numToValAsStr(
		pNum in number
	) return varchar2 deterministic
	is
	begin
	  BEGIN
	    return to_char(pNum, '999999999999999999999999999999999999999999999999.9999999999EEEE');   
	  EXCEPTION WHEN VALUE_ERROR THEN
	    return to_char(pNum, '999999999999999999999999999999999999999999999999.9999999999');
	  END;     
	end;

	function numFromValAsStr(
		pValAsStr in varchar2
	) return number deterministic
	IS
	item VARCHAR2(32767); 
	BEGIN
	    BEGIN 
	    item := REPLACE(pValAsStr, ',', '.');
	          BEGIN 
	              RETURN TO_NUMBER(item, '9999999999999999999999999999999999999999999999999999.9999999999');
	          EXCEPTION WHEN VALUE_ERROR THEN
	              RETURN TO_NUMBER(item, '999999999999999999999999999999999999999999999999.9999999999EEEE');
	          END;     
	    EXCEPTION WHEN VALUE_ERROR THEN
	          BEGIN 
	              RETURN TO_NUMBER(pValAsStr, '9999999999999999999999999999999999999999999999999999.9999999999');
	          EXCEPTION WHEN VALUE_ERROR THEN
	              RETURN TO_NUMBER(pValAsStr, '999999999999999999999999999999999999999999999999.9999999999EEEE');
	          END;
	    END;
	END;
end;
/

create or replace package body RDX_WF as

	DEBUG_ROLE CONSTANT VARCHAR2(50) := 'rol7MTXJKWQORCJFD7EEZHGZ7TOJM';
	ADMIN_ROLE CONSTANT VARCHAR2(50) := 'rolHM6FIFA4JFCJFIYPB3P2KC435U';
	CLERK_ROLE CONSTANT VARCHAR2(50) := 'rolOYMAGGZIXNF6TMGZASCZET6VOI';



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

	    if RDX_ACS.curUserHasRoleInArea(ADMIN_ROLE, accessArea) = 0 and RDX_ACS.curUserHasRoleInArea(DEBUG_ROLE, accessArea) = 0 then
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

	    if RDX_ACS.curUserHasRoleInArea(ADMIN_ROLE, accessArea) = 0 and RDX_ACS.curUserHasRoleInArea(DEBUG_ROLE, accessArea) = 0then
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

	    if RDX_ACS.userHasRoleInArea(pUser, CLERK_ROLE, accessArea) = 0 then
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

create or replace package body RDX_WF_Maintenance as

	procedure daily
	is
	begin
	   RDX_Trace.put(1, 'Workflow maintenance started', 'Arte.Db');
	   delete from RDX_WF_PROCESS
	      where STATE>1 and FINISHTIME < sysdate - (select RDX_WF_PROCESSTYPE.PROCESSSTOREDAYS from RDX_WF_PROCESSTYPE where RDX_WF_PROCESSTYPE.GUID = RDX_WF_PROCESS.TYPEGUID);
	   RDX_Trace.put(1,  'Workflow maintenance finished', 'Arte.Db');
	end;
end;
/

create or replace trigger TADR_RDX_AC_APPROLE
after delete on RDX_AC_APPROLE
for each row
begin
 delete from RDX_AC_USER2ROLE where  RDX_AC_USER2ROLE.ROLEID = :OLD.GUID;
 delete from RDX_AC_USERGROUP2ROLE where  RDX_AC_USERGROUP2ROLE.ROLEID = :OLD.GUID; 
end;
/

create or replace trigger TADR_RDX_PC_OUTMESSAGE
after delete on RDX_PC_OUTMESSAGE
for each row
Begin
  if :old.HISTMODE = 0 then
    Delete from RDX_PC_ATTACHMENT where RDX_PC_ATTACHMENT.MESSID=:old.ID;
  end if;
End;
/

create or replace trigger TADR_RDX_PC_RECVMESSAGE
after delete on RDX_PC_RECVMESSAGE
for each row
Begin
  Delete from RDX_PC_ATTACHMENT where RDX_PC_ATTACHMENT.MESSID=:old.ID;
End;
/

create or replace trigger TADR_RDX_PC_SENTMESSAGE
after delete on RDX_PC_SENTMESSAGE
for each row
Begin
  Delete from RDX_PC_ATTACHMENT where RDX_PC_ATTACHMENT.MESSID=:old.ID;
End;
/

create or replace trigger TAIDU_RDX_SYSTEM
after delete or insert or update on RDX_SYSTEM
begin
    RDX_AUDIT.DEFAULTAUDITSCHEME := NULL;
end;
/

create or replace trigger TAIUR_RDX_SM_METRICSTATE
after insert or update of ENDTIME on RDX_SM_METRICSTATE
for each row
begin
    Insert into RDX_SM_METRICHIST(METRICID, ENDTIME, ID, BEGTIME, BEGVAL, ENDVAL, MINVAL, MAXVAL, AVGVAL)
    values (:NEW.ID, :NEW.ENDTIME, :NEW.LASTHISTID, :NEW.BEGTIME, 
        :NEW.BEGVAL, :NEW.ENDVAL, 
        :NEW.MINVAL, :NEW.MAXVAL,
        :NEW.AVGVAL);
end;
/

create or replace trigger TAUR_RDX_WF_PROCESS
after update of STATE on RDX_WF_PROCESS
for each row
Declare
  currentTime DATE;
Begin
  if :new.state <> 1 then
     delete from RDX_JS_JOBQUEUE where RDX_JS_JOBQUEUE.CREATORENTITYGUID='tblRDXQVFY6PLNRDANMABIFNQAABA' and RDX_JS_JOBQUEUE.CREATORPID=:new.id; 
  end if;
  if :new.state in (4, 3, 2) then
     currentTime := :new.simulatedTime;
     if currentTime is null then
        currentTime := sysdate;
     end if;
     update RDX_WF_FORM set RDX_WF_FORM.CLOSETIME = currentTime
        where RDX_WF_FORM.PROCESSID = :new.ID and RDX_WF.getFormState(RDX_WF_FORM.CLOSETIME, RDX_WF_FORM.DUETIME, RDX_WF_FORM.OVERDUETIME, RDX_WF_FORM.EXPIRATIONTIME, currentTime) in (0, 1, 2);
  end if;  
End;
/

create or replace trigger TBIDU_RDX_SYSTEM
before delete or insert or update on RDX_SYSTEM
declare auditSchemaId varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into auditSchemaId
      from RDX_SYSTEM
      where ID=1;
   exception
      when no_data_found then 
         auditSchemaId := null;
   end;
    
   if auditSchemaId IS NOT NULL then 
       RDX_AUDIT.DEFAULTAUDITSCHEME := auditSchemaId;
   else
       RDX_AUDIT.DEFAULTAUDITSCHEME := '-1';
   end if;
end;
/

create or replace trigger TBIUR_RDX_SM_METRICSTATE
before insert or update of ENDTIME on RDX_SM_METRICSTATE
for each row
begin
    select SQN_RDX_SM_METRICHISTID.NextVal into :NEW.LASTHISTID from dual;
end;
/

create or replace trigger TBUR_RDX_AC_USER
before update of PWDHASH on RDX_AC_USER
for each row
begin
    :new.LASTPWDCHANGETIME := sysdate;
    :new.MUSTCHANGEPWD := 0; 
end;
/

create or replace trigger UPF_44TUT3JSR5BPTNBRF3PO2HF6OU
after delete on COUNTER
begin
   RDX_ENTITY.flushUserPropOnDelOwner('tbl44TUT3JSR5BPTNBRF3PO2HF6OU');
end;
/

create or replace trigger UPF_XWLPEIJAGRAXJG7SHDM4DH4X6I
after delete on RDX_CM_ITEMREF
begin
   RDX_ENTITY.flushUserPropOnDelOwner('tblXWLPEIJAGRAXJG7SHDM4DH4X6I');
end;
/

create or replace trigger UPO_2H6SULJHXJGFVIS6UVHRWQS4AM
after delete on RDX_SM_METRICTYPE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl2H6SULJHXJGFVIS6UVHRWQS4AM', :old.ID);
end;
/

create or replace trigger UPO_4UBCGVD5ZZF6ZIJN4J3YISPFZA
after delete on RDX_TST_AUDITMASTER
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA', :old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF'));
end;
/

create or replace trigger UPO_5HP4XTP3EGWDBRCRAAIT4AGD7E
after delete on RDX_UNIT
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', :old.ID);
end;
/

create or replace trigger UPO_72TT5CRGUJEY3L2OOOKZHO3L2M
after delete on RDX_REPORTPARAM
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl72TT5CRGUJEY3L2OOOKZHO3L2M', :old.ID);
end;
/

create or replace trigger UPO_73I26X3GUNGMPDAF3YPGJE272Y
after delete on RDX_TST_TEDCCHILD
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl73I26X3GUNGMPDAF3YPGJE272Y', :old.ID);
end;
/

create or replace trigger UPO_7HTJAWJXVLOBDCLSAALOMT5GDM
after delete on RDX_PC_EVENTSUBSCRIPTION
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tbl7HTJAWJXVLOBDCLSAALOMT5GDM', :old.ID);
end;
/

create or replace trigger UPO_B6QEDVV5BNEWZHKZRTVR67TKOY
after delete on RDX_TST_CHILD
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblB6QEDVV5BNEWZHKZRTVR67TKOY', :old.ID);
end;
/

create or replace trigger UPO_CJ53NERF4VBJDEDGNHBFXRNNC4
after delete on RDX_TST_DMTESTA
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblCJ53NERF4VBJDEDGNHBFXRNNC4', :old.PKINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKSTR));
end;
/

create or replace trigger UPO_FW5F7B7PS5DVXPQS2XAUUSITJQ
after delete on RDX_TST_USERCHILD
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblFW5F7B7PS5DVXPQS2XAUUSITJQ', :old.ID);
end;
/

create or replace trigger UPO_HBE24OUE2DNRDB7AAALOMT5GDM
after delete on RDX_NETCHANNEL
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblHBE24OUE2DNRDB7AAALOMT5GDM', :old.ID);
end;
/

create or replace trigger UPO_HQSOHRWZH5ESLD2Y6IVF6XFBE4
after delete on RDX_EASSELECTORADDONS
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblHQSOHRWZH5ESLD2Y6IVF6XFBE4', RDX_ENTITY.PackPIDStr(:old.GUID));
end;
/

create or replace trigger UPO_HW4OSVMS27NRDISQAAAAAAAAAA
after delete on RDX_TESTCASE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblHW4OSVMS27NRDISQAAAAAAAAAA', :old.ID);
end;
/

create or replace trigger UPO_J4WZRLJ23BGMBKQ2JBSOBEGBP4
after delete on RDX_CM_ITEM
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblJ4WZRLJ23BGMBKQ2JBSOBEGBP4', :old.ID);
end;
/

create or replace trigger UPO_KPUY4CMANZH3XDOZZB7FXKHWEY
after delete on RDX_TST_DMTEST
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblKPUY4CMANZH3XDOZZB7FXKHWEY', :old.ID);
end;
/

create or replace trigger UPO_PNJV5QTXIBGJPBHK64RO3LH73A
after delete on RDX_SB_PIPELINENODE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblPNJV5QTXIBGJPBHK64RO3LH73A', :old.ID);
end;
/

create or replace trigger UPO_QZ2AJHN3PFDWXC5BT6I66OS5PQ
after delete on RDX_MESSAGEQUEUE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblQZ2AJHN3PFDWXC5BT6I66OS5PQ', :old.ID);
end;
/

create or replace trigger UPO_RDXQVFY6PLNRDANMABIFNQAABA
after delete on RDX_WF_PROCESS
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblRDXQVFY6PLNRDANMABIFNQAABA', :old.ID);
end;
/

create or replace trigger UPO_TDP5BP5ZXBEOTJX74NH43PEPJA
after delete on RDX_SB_TRANSFORMSTAGE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblTDP5BP5ZXBEOTJX74NH43PEPJA', :old.ID);
end;
/

create or replace trigger UPO_U5S5JH3DFFGHXMRP4JVYGLQQTQ
after delete on RDX_MESSAGEQUEUEPROCESSOR
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblU5S5JH3DFFGHXMRP4JVYGLQQTQ', :old.ID);
end;
/

create or replace trigger UPO_UNC67IF5OBCX7NOPNLQOCUG374
after delete on RDX_REPORTPUB
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblUNC67IF5OBCX7NOPNLQOCUG374', :old.ID);
end;
/

create or replace trigger UPO_VP66JC4HXLNRDF5JABIFNQAABA
after delete on RDX_WF_FORM
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblVP66JC4HXLNRDF5JABIFNQAABA', :old.ID);
end;
/

create or replace trigger UPO_W7BRVVQLHBE2FCNK7ZBRMAUJGM
after delete on RDX_LIBUSERFUNC
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblW7BRVVQLHBE2FCNK7ZBRMAUJGM', RDX_ENTITY.PackPIDStr(:old.GUID));
end;
/

create or replace trigger UPO_WZB7K4HLJPOBDCIUAALOMT5GDM
after delete on RDX_JS_TASK
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblWZB7K4HLJPOBDCIUAALOMT5GDM', :old.ID);
end;
/

create or replace trigger UPO_XD3B6CBJ4BFIDJ2LUNLMJ4CXOE
after delete on RDX_TST_TEDCPARENT
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblXD3B6CBJ4BFIDJ2LUNLMJ4CXOE', :old.ID);
end;
/

create or replace trigger UPO_XI6CAOIGDBAGJEE6JQEGJME43Q
after delete on RDX_WF_PROCESSTYPE
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblXI6CAOIGDBAGJEE6JQEGJME43Q', RDX_ENTITY.PackPIDStr(:old.GUID));
end;
/

create or replace trigger UPO_YDGQACJVVLOBDCLSAALOMT5GDM
after delete on RDX_PC_CHANNELHANDLER
for each row
begin
   RDX_ENTITY.UserPropOnDelOwner('tblYDGQACJVVLOBDCLSAALOMT5GDM', :old.UNITID||'~'||:old.SEQ);
end;
/

create or replace trigger UPS_44TUT3JSR5BPTNBRF3PO2HF6OU
after delete on COUNTER
for each row
begin
   RDX_ENTITY.ScheduleUserPropOnDelOwner('tbl44TUT3JSR5BPTNBRF3PO2HF6OU', :old.ID);
end;
/

create or replace trigger UPS_XWLPEIJAGRAXJG7SHDM4DH4X6I
after delete on RDX_CM_ITEMREF
for each row
begin
   RDX_ENTITY.ScheduleUserPropOnDelOwner('tblXWLPEIJAGRAXJG7SHDM4DH4X6I', RDX_ENTITY.PackPIDStr(:old.UPDEFID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERENTITYID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERPID));
end;
/

create or replace trigger UPV_73I26X3GUNGMPDAF3YPGJE272Y
after delete on RDX_TST_TEDCCHILD
for each row
begin
    RDX_ENTITY.userPropOnDelValue('tbl73I26X3GUNGMPDAF3YPGJE272Y', :old.ID);
end;

/

create or replace trigger UPV_IOIXD5DJVVD53KTA2RNHOYYJGY
after delete on RDX_TST_USERPARENTSIMPLE
for each row
begin
    RDX_ENTITY.userPropOnDelValue('tblIOIXD5DJVVD53KTA2RNHOYYJGY', :old.ID);
end;

/

create or replace trigger UPV_OIKKJ54YSNFLTMVHR4R6DKTYBQ
after delete on RDX_TST_USERPARENTCOMPLEX
for each row
begin
    RDX_ENTITY.userPropOnDelValue('tblOIKKJ54YSNFLTMVHR4R6DKTYBQ', :old.PK_INT||'~'||RDX_ENTITY.PackPIDStr(:old.PK_CHAR)||'~'||to_char(:old.PK_DATE,'yyyy-mm-dd hh24:mi:ss.FF6')||'~'||RDX_ENTITY.PackPIDStr(:old.PK_STR));
end;

/

create or replace trigger UPV_QZ2AJHN3PFDWXC5BT6I66OS5PQ
after delete on RDX_MESSAGEQUEUE
for each row
begin
    RDX_ENTITY.userPropOnDelValue('tblQZ2AJHN3PFDWXC5BT6I66OS5PQ', :old.ID);
end;

/

create or replace trigger UPV_XD3B6CBJ4BFIDJ2LUNLMJ4CXOE
after delete on RDX_TST_TEDCPARENT
for each row
begin
    RDX_ENTITY.userPropOnDelValue('tblXD3B6CBJ4BFIDJ2LUNLMJ4CXOE', :old.ID);
end;

/

create or replace trigger atd3DD7EMEOIHNRDJIEACQMTAIZT4
after delete on RDX_JS_JOBEXECUTORUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aec3DD7EMEOIHNRDJIEACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl3DD7EMEOIHNRDJIEACQMTAIZT4', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd42K4K2TTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USER2ROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl42K4K2TTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colCN3XEDKAZDNBDGMDABQAQH3XQ4',:old.USERNAME);
            RDX_AUDIT.addValueInt(vChangeData,'old.colDC3DKGLHPDNRDHTWABQAQH3XQ4',:old.ISOWN);
            RDX_AUDIT.addValueStr(vChangeData,'old.colFXSCOJCAZDNBDGMDABQAQH3XQ4',:old.ROLEID);
            RDX_AUDIT.addValueInt(vChangeData,'old.col4T2IAH72O5E6RNZPX3MNT44CQE',:old.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addValueStr(vChangeData,'old.colD7VLPHOA3VC4XL3QOLTWZLUHJI',:old.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aec42K4K2TTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl42K4K2TTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl42K4K2TTGLNRDHRZABQAQH3XQ4', 'D','tbl42K4K2TTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atd4UBCGVD5ZZF6ZIJN4J3YISPFZA
after delete on RDX_TST_AUDITMASTER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colIY54HRUWQRC5LI4AMLNJDHNCAU',:old.COLINT);
            RDX_AUDIT.addValueStr(vChangeData,'old.colSW4DJGZ5LRCLFJCYZLQZGDNAME',:old.COLCHAR);
            RDX_AUDIT.addValueNum(vChangeData,'old.col6WWL3SC5TZBMZGHZSPPPJVV2BQ',:old.COLNUM);
            RDX_AUDIT.addValueDate(vChangeData,'old.colJ7FU6BTM75HQTCIAKYQDRXDY5U',:old.COLDATE);
            RDX_AUDIT.addValueDate(vChangeData,'old.col4TWPKWPOL5EB3GHUIUL6UFRHWQ',:old.COLTIMESTAMP);
            RDX_AUDIT.addValueStr(vChangeData,'old.colV5EPQ4QOLREEZOV2UUHUWZHYEE',:old.COLSTR);
            RDX_AUDIT.addValueRaw(vChangeData,'old.colTGVNWDGLGNBI3N5BYXZLI5RXQQ',:old.COLBIN);
            RDX_AUDIT.addValueBlob(vChangeData,'old.colFDVZCLAYD5HZRN2JGORFYFA3NE',:old.COLBLOB);
            RDX_AUDIT.addValueClob(vChangeData,'old.colL76ZGR3OPVHQFHYKDPWUIZQA34',:old.COLCLOB);
            RDX_AUDIT.addValueStr(vChangeData,'old.colI7RDFSIDUZE4PPBNSJFU4RCEIM',:old.COLARRINT);
            RDX_AUDIT.addValueClob(vChangeData,'old.colGAR4IF3GRVAFLA4DTQXPQGCSME',:old.COLARRCHAR);
            RDX_AUDIT.addValueClob(vChangeData,'old.colY5PXD2VKE5FBTFGVSCPTLGE46U',:old.COLARRSTR);
            RDX_AUDIT.addValueStr(vChangeData,'old.colE3SYXVPOCVEIFEEYBEIFXY66XQ',:old.COLARRNUM);
            RDX_AUDIT.addValueClob(vChangeData,'old.colYZYQHXB75VHEVG2BTGNGPNJZEA',:old.COLARRDATETIME);
            RDX_AUDIT.addValueClob(vChangeData,'old.col54URWAVOAVE4ZFLUZI7QEQYCDM',:old.COLARRBIN);
            RDX_AUDIT.addValueClob(vChangeData,'old.colD6G6MRNNKVGJFGSYHPF3DFQ66Y',:old.COLARRPARENTREF);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA', 'D','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atd52CHFNO3EGWDBRCRAAIT4AGD7E
after delete on RDX_INSTANCE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl52CHFNO3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aec52CHFNO3EGWDBRCRAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl52CHFNO3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl52CHFNO3EGWDBRCRAAIT4AGD7E', 'D','tbl52CHFNO3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd5HP4XTP3EGWDBRCRAAIT4AGD7E
after delete on RDX_UNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd5WO5N3BEVLOBDCLSAALOMT5GDM
after delete on RDX_PC_CHANNELUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colB2VQZVZFVLOBDCLSAALOMT5GDM',:old.KIND);
   end if;

   vClassGuid := 'aec5WO5N3BEVLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5WO5N3BEVLOBDCLSAALOMT5GDM', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atd6HF4SHAXLDNBDJA6ACQMTAIZT4
after delete on RDX_SCP2SAP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SYSTEMID||'~'||:old.SAPID||'~'||RDX_ENTITY.PackPIDStr(:old.SCPNAME);

   vClassGuid := 'aec6HF4SHAXLDNBDJA6ACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl6HF4SHAXLDNBDJA6ACQMTAIZT4', 'D','tbl6HF4SHAXLDNBDJA6ACQMTAIZT4');
end;
/

create or replace trigger atd7HTJAWJXVLOBDCLSAALOMT5GDM
after delete on RDX_PC_EVENTSUBSCRIPTION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl7HTJAWJXVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colDHOISLJZVLOBDCLSAALOMT5GDM',:old.ISACTIVE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colLHS5GRJZVLOBDCLSAALOMT5GDM',:old.USERGROUPNAME);
            RDX_AUDIT.addValueStr(vChangeData,'old.col5OTM5EZZVLOBDCLSAALOMT5GDM',:old.CHANNELKIND);
            RDX_AUDIT.addValueStr(vChangeData,'old.colZQGUBQRZVLOBDCLSAALOMT5GDM',:old.EVENTSOURCE);
            RDX_AUDIT.addValueInt(vChangeData,'old.col6MRC5ZR3VLOBDCLSAALOMT5GDM',:old.MINEVENTSEVERITY);
            RDX_AUDIT.addValueStr(vChangeData,'old.colJOXDSNJ4VLOBDCLSAALOMT5GDM',:old.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colIS5OGPB4VLOBDCLSAALOMT5GDM',:old.BODYTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.colRJPBERR4VLOBDCLSAALOMT5GDM',:old.LANGUAGE);
            RDX_AUDIT.addValueInt(vChangeData,'old.col3RTQXCB5VLOBDCLSAALOMT5GDM',:old.LOWIMPORTANCEMAXSEV);
            RDX_AUDIT.addValueInt(vChangeData,'old.colAQICTNR5VLOBDCLSAALOMT5GDM',:old.NORMALIMPORTANCEMAXSEV);
            RDX_AUDIT.addValueInt(vChangeData,'old.colVNCG43RIBREHHI5FFCUATH4PTM',:old.TOSTOREINHIST);
            RDX_AUDIT.addValueStr(vChangeData,'old.colVH3DNHSDUBD3JPNV535DVDBYBU',:old.LIMITMESSSUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'old.col2FJBO5EEGJCW3MOVEG3KLYQCGA',:old.LIMITMESSBODYTEMPLATE);
   end if;

   vClassGuid := 'aec7HTJAWJXVLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl7HTJAWJXVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl7HTJAWJXVLOBDCLSAALOMT5GDM', 'D','tbl7HTJAWJXVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atdAAU55TOEHJWDRPOYAAYQQ2Y3GB
after delete on RDX_SCP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblAAU55TOEHJWDRPOYAAYQQ2Y3GB'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME)||'~'||:old.SYSTEMID;

   vClassGuid := 'aecAAU55TOEHJWDRPOYAAYQQ2Y3GB';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblAAU55TOEHJWDRPOYAAYQQ2Y3GB',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblAAU55TOEHJWDRPOYAAYQQ2Y3GB', 'D','tblAAU55TOEHJWDRPOYAAYQQ2Y3GB');
end;
/

create or replace trigger atdARTOV5KBI3OBDCIOAALOMT5GDM
after delete on RDX_JS_INTERVALSCHDITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblARTOV5KBI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SCHEDULEID||'~'||:old.ID;

   vClassGuid := 'aecARTOV5KBI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblARTOV5KBI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblARTOV5KBI3OBDCIOAALOMT5GDM', 'D','tblARTOV5KBI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdB6QEDVV5BNEWZHKZRTVR67TKOY
after delete on RDX_TST_CHILD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblB6QEDVV5BNEWZHKZRTVR67TKOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.col6OTOT3XC7BHKVIE3Y6WRX4O2OY',:old.CHILDTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'old.colZDX2SYK7TJAFTJENP6ATORQOTI',:old.COLBOOL);
            RDX_AUDIT.addValueStr(vChangeData,'old.colVTKWXW5C7RHLVACWBAAOZ3XJGY',:old.COLCHAR);
            RDX_AUDIT.addValueDate(vChangeData,'old.col7CYI2GEMAJA5LNUWSJ27NVIUZ4',:old.COLDATETIME);
   end if;

   vClassGuid := 'aecB6QEDVV5BNEWZHKZRTVR67TKOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblB6QEDVV5BNEWZHKZRTVR67TKOY',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblB6QEDVV5BNEWZHKZRTVR67TKOY', 'D','tblB6QEDVV5BNEWZHKZRTVR67TKOY');
end;
/

create or replace trigger atdC2OWQGDVVHWDBROXAAIT4AGD7E
after delete on RDX_SERVICE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblC2OWQGDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SYSTEMID||'~'||RDX_ENTITY.PackPIDStr(:old.URI);

   vClassGuid := 'aecC2OWQGDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblC2OWQGDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblC2OWQGDVVHWDBROXAAIT4AGD7E', 'D','tblC2OWQGDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atdCRD53OZ5I3OBDCIOAALOMT5GDM
after delete on RDX_JS_CALENDARITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblCRD53OZ5I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblCRD53OZ5I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblCRD53OZ5I3OBDCIOAALOMT5GDM', 'D','tblCRD53OZ5I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdDYWJCJTTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USER2USERGROUP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblDYWJCJTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.USERNAME)||'~'||RDX_ENTITY.PackPIDStr(:old.GROUPNAME);

   vClassGuid := 'aecDYWJCJTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblDYWJCJTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblDYWJCJTTGLNRDHRZABQAQH3XQ4', 'D','tblDYWJCJTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdFDFDUXBHJ3NRDJIRACQMTAIZT4
after delete on RDX_STATION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFDFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);

   vClassGuid := 'aecFDFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFDFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFDFDUXBHJ3NRDJIRACQMTAIZT4', 'D','tblFDFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atdFJAEQT3TGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USERGROUP2ROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFJAEQT3TGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colXDHHLFZRY3NBDGMCABQAQH3XQ4',:old.GROUPNAME);
            RDX_AUDIT.addValueStr(vChangeData,'old.colQNEGZPZRY3NBDGMCABQAQH3XQ4',:old.ROLEID);
            RDX_AUDIT.addValueInt(vChangeData,'old.colAULGQS3XVFDL5DE7L3MW46URPQ',:old.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addValueStr(vChangeData,'old.colDWXM6MYALRBC3OYCWTWRGS4DIE',:old.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aecFJAEQT3TGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFJAEQT3TGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFJAEQT3TGLNRDHRZABQAQH3XQ4', 'D','tblFJAEQT3TGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdGPFDUXBHJ3NRDJIRACQMTAIZT4
after delete on RDX_USER2STATION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblGPFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.STATIONNAME)||'~'||RDX_ENTITY.PackPIDStr(:old.USERNAME);

   vClassGuid := 'aecGPFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblGPFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblGPFDUXBHJ3NRDJIRACQMTAIZT4', 'D','tblGPFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atdHBE24OUE2DNRDB7AAALOMT5GDM
after delete on RDX_NETCHANNEL
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHBE24OUE2DNRDB7AAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.col6OTLV7WBZVGDFM7SYNM5KXYSGQ',:old.SERVERKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colSOCMOJR6SRCGBL63P2ALGCKNUQ',:old.CLIENTCERTALIASES);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHBE24OUE2DNRDB7AAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHBE24OUE2DNRDB7AAALOMT5GDM', 'D','tblHBE24OUE2DNRDB7AAALOMT5GDM');
end;
/

create or replace trigger atdHC6VVBZ4I3OBDCIOAALOMT5GDM
after delete on RDX_JS_CALENDAR
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHC6VVBZ4I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colUJIT2SZ5I3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHC6VVBZ4I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHC6VVBZ4I3OBDCIOAALOMT5GDM', 'D','tblHC6VVBZ4I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdJ5AGLYGB6RGKNJ36XBBX5AZ4ZA
after delete on RDX_TST_PARENT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecJ5AGLYGB6RGKNJ36XBBX5AZ4ZA';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA', 'D','tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA');
end;
/

create or replace trigger atdJ6SOXKD3ZHOBDCMTAALOMT5GDM
after delete on RDX_USERFUNC
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.UPDEFID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERENTITYID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERPID);
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colYF2NT733ZHOBDCMTAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ6SOXKD3ZHOBDCMTAALOMT5GDM', 'D','tblJ6SOXKD3ZHOBDCMTAALOMT5GDM');
end;
/

create or replace trigger atdLSU45NKDI3OBDCIOAALOMT5GDM
after delete on RDX_JS_EVENTSCHD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblLSU45NKDI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colYZCK5ZCDI3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := 'aecLSU45NKDI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblLSU45NKDI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblLSU45NKDI3OBDCIOAALOMT5GDM', 'D','tblLSU45NKDI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdM47V6KTSNJC3FKWW2W6DME6Z2I
after delete on RDX_NETHUB
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colBBSLHM5KRFC6VCPLABSQU5OUIY',:old.SAPID);
   end if;

   vClassGuid := 'aecM47V6KTSNJC3FKWW2W6DME6Z2I';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM47V6KTSNJC3FKWW2W6DME6Z2I', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atdM7J46MP6F3PBDIJEABQAQH3XQ4
after delete on RDX_AC_APPROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblM7J46MP6F3PBDIJEABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colMFRUYXP6F3PBDIJEABQAQH3XQ4',:old.TITLE);
   end if;

   vClassGuid := 'aecM7J46MP6F3PBDIJEABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblM7J46MP6F3PBDIJEABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM7J46MP6F3PBDIJEABQAQH3XQ4', 'D','tblM7J46MP6F3PBDIJEABQAQH3XQ4');
end;
/

create or replace trigger atdOVPOORJSBFAGZERW5IXTRFTWOY
after delete on RDX_PC_EVENTLIMITACC
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblOVPOORJSBFAGZERW5IXTRFTWOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SUBSCRIPTIONID||'~'||RDX_ENTITY.PackPIDStr(:old.USERNAME)||'~'||RDX_ENTITY.PackPIDStr(:old.EVENTCODE)||'~'||RDX_ENTITY.PackPIDStr(:old.CHANNELKIND);

   vClassGuid := 'aecOVPOORJSBFAGZERW5IXTRFTWOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblOVPOORJSBFAGZERW5IXTRFTWOY',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblOVPOORJSBFAGZERW5IXTRFTWOY', 'D','tblOVPOORJSBFAGZERW5IXTRFTWOY');
end;
/

create or replace trigger atdPCE24TUPIHNRDJIEACQMTAIZT4
after delete on RDX_JS_JOBEXECUTORUNITBOOST
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblPCE24TUPIHNRDJIEACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SPEED;

   vClassGuid := 'aecPCE24TUPIHNRDJIEACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblPCE24TUPIHNRDJIEACQMTAIZT4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblPCE24TUPIHNRDJIEACQMTAIZT4', 'D','tblPCE24TUPIHNRDJIEACQMTAIZT4');
end;
/

create or replace trigger atdQ23AYDTTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USERGROUP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQ23AYDTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);

   vClassGuid := 'aecQ23AYDTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQ23AYDTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQ23AYDTTGLNRDHRZABQAQH3XQ4', 'D','tblQ23AYDTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdR7FXMYDVVHWDBROXAAIT4AGD7E
after delete on RDX_SAP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblR7FXMYDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colIHQTWYMFTJC5VEZAM3WWYLHZK4',:old.SERVERKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.col6F5NIDGUUNDYNLVMFKURFV4QOY',:old.SERVERCERTALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colFT5F6TB3TRFDHHAFWSW7TIYZRA',:old.CLIENTKEYALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.col6X5HPPFXLJGOBCWX5CU6VISYTU',:old.CLIENTCERTALIASES);
            RDX_AUDIT.addValueStr(vChangeData,'old.colJ4MTAXJHPZHJZK6TW6NHKZY5MM',:old.CIPHERSUITES);
   end if;

   vClassGuid := 'aecR7FXMYDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblR7FXMYDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblR7FXMYDVVHWDBROXAAIT4AGD7E', 'D','tblR7FXMYDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atdS4NVQKVQI5HLTG33G43UK5QMXU
after delete on RDX_TST_AUDITDETAIL
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.colB7LCJXCMXVFF7PZ7UH3YBVJXOQ',:old.TITLE);
   end if;

   vClassGuid := 'aecS4NVQKVQI5HLTG33G43UK5QMXU';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblS4NVQKVQI5HLTG33G43UK5QMXU', 'D','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atdSY4KIOLTGLNRDHRZABQAQH3XQ4
after delete on RDX_AC_USER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblSY4KIOLTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);

   vClassGuid := 'aecSY4KIOLTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblSY4KIOLTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblSY4KIOLTGLNRDHRZABQAQH3XQ4', 'D','tblSY4KIOLTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atdUEE3YXA72HNRDB7BAALOMT5GDM
after delete on RDX_NETPORTHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colUVIFSVD4RHWDBRCXAAIT4AGD7E',:old.SAPID);
   end if;

   vClassGuid := 'aecUEE3YXA72HNRDB7BAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblUEE3YXA72HNRDB7BAALOMT5GDM', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atdWZB7K4HLJPOBDCIUAALOMT5GDM
after delete on RDX_JS_TASK
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblWZB7K4HLJPOBDCIUAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblWZB7K4HLJPOBDCIUAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblWZB7K4HLJPOBDCIUAALOMT5GDM', 'D','tblWZB7K4HLJPOBDCIUAALOMT5GDM');
end;
/

create or replace trigger atdX5TD7JDVVHWDBROXAAIT4AGD7E
after delete on RDX_SYSTEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT.DEFAULTAUDITSCHEME
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblX5TD7JDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecX5TD7JDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblX5TD7JDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblX5TD7JDVVHWDBROXAAIT4AGD7E', 'D','tblX5TD7JDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atdXI6CAOIGDBAGJEE6JQEGJME43Q
after delete on RDX_WF_PROCESSTYPE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblXI6CAOIGDBAGJEE6JQEGJME43Q'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'old.col6SKMJNSZZNFVDNGCP26PG6QPEM',:old.CLERKROLEGUIDS);
            RDX_AUDIT.addValueStr(vChangeData,'old.col6KIGNQEL7NEWFDQMOEIO4POSTQ',:old.ADMINROLEGUIDS);
            RDX_AUDIT.addValueInt(vChangeData,'old.colNWJ6DLDISBBWZKPVZUMMGLM5LI',:old.PROCESSSTOREDAYS);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblXI6CAOIGDBAGJEE6JQEGJME43Q',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXI6CAOIGDBAGJEE6JQEGJME43Q', 'D','tblXI6CAOIGDBAGJEE6JQEGJME43Q');
end;
/

create or replace trigger atdXOKR7CSUNFG3DCVQGB5LWIWIIU
after delete on RDX_ARTEUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecXOKR7CSUNFG3DCVQGB5LWIWIIU';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXOKR7CSUNFG3DCVQGB5LWIWIIU', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atdY2BRNTJ5VLOBDCLSAALOMT5GDM
after delete on RDX_PC_EVENTSUBSCRIPTIONCODE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblY2BRNTJ5VLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SUBSCRIPTIONID||'~'||RDX_ENTITY.PackPIDStr(:old.CODE);

   vClassGuid := 'aecY2BRNTJ5VLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblY2BRNTJ5VLOBDCLSAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblY2BRNTJ5VLOBDCLSAALOMT5GDM', 'D','tblY2BRNTJ5VLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atdYDGQACJVVLOBDCLSAALOMT5GDM
after delete on RDX_PC_CHANNELHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblYDGQACJVVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.UNITID||'~'||:old.SEQ;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblYDGQACJVVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblYDGQACJVVLOBDCLSAALOMT5GDM', 'D','tblYDGQACJVVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atdZ3SDCSSEI3OBDCIOAALOMT5GDM
after delete on RDX_JS_EVENTSCHDITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZ3SDCSSEI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SCHEDULEID||'~'||:old.ID;

   vClassGuid := 'aecZ3SDCSSEI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZ3SDCSSEI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZ3SDCSSEI3OBDCIOAALOMT5GDM', 'D','tblZ3SDCSSEI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdZBS5MS2BI3OBDCIOAALOMT5GDM
after delete on RDX_JS_INTERVALSCHD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZBS5MS2BI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueDate(vChangeData,'old.colKCIYPFSBI3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME);
   end if;

   vClassGuid := 'aecZBS5MS2BI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZBS5MS2BI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZBS5MS2BI3OBDCIOAALOMT5GDM', 'D','tblZBS5MS2BI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atdZUTGVH5TKRFWZEUGZHC5KRIOFA
after delete on RDX_JMSHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      vSchemeId := RDX_AUDIT.DEFAULTAUDITSCHEME;
      if vSchemeId IS NULL then 
           select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      end if;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'D';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'old.colFMGGFQ6MORC67LQVLCPFLZHOJM',:old.SAPID);
   end if;

   vClassGuid := 'aecZUTGVH5TKRFWZEUGZHC5KRIOFA';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'D',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZUTGVH5TKRFWZEUGZHC5KRIOFA', 'D','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger ati3DD7EMEOIHNRDJIEACQMTAIZT4
after insert on RDX_JS_JOBEXECUTORUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colPJDE6LVONJB3PPTQ7ZCF6UPQOI',:new.PARALLELCNT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colYS346KYNLVBNFJEHV2EA2KPKKM',:new.ABOVENORMALDELTA);
            RDX_AUDIT.addValueInt(vChangeData,'new.colHBV4J6LEWNCOTM4WHJRSIMLKFM',:new.HIGHDELTA);
            RDX_AUDIT.addValueInt(vChangeData,'new.colNJB3FZVNRNGXXMYCHMBRAXJZI4',:new.VERYHIGHDELTA);
            RDX_AUDIT.addValueInt(vChangeData,'new.colUX424WYE3ZGARCSQMV4REKOFK4',:new.CRITICALDELTA);
            RDX_AUDIT.addValueNum(vChangeData,'new.colCCFCKQ4OIHNRDJIEACQMTAIZT4',:new.EXECPERIOD);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl3DD7EMEOIHNRDJIEACQMTAIZT4', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger ati42K4K2TTGLNRDHRZABQAQH3XQ4
after insert on RDX_AC_USER2ROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl42K4K2TTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colCN3XEDKAZDNBDGMDABQAQH3XQ4',:new.USERNAME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colDC3DKGLHPDNRDHTWABQAQH3XQ4',:new.ISOWN);
            RDX_AUDIT.addValueStr(vChangeData,'new.colFXSCOJCAZDNBDGMDABQAQH3XQ4',:new.ROLEID);
            RDX_AUDIT.addValueInt(vChangeData,'new.col4T2IAH72O5E6RNZPX3MNT44CQE',:new.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addValueStr(vChangeData,'new.colD7VLPHOA3VC4XL3QOLTWZLUHJI',:new.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aec42K4K2TTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl42K4K2TTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl42K4K2TTGLNRDHRZABQAQH3XQ4', 'I','tbl42K4K2TTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger ati4UBCGVD5ZZF6ZIJN4J3YISPFZA
after insert on RDX_TST_AUDITMASTER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:new.PKCOLCHAR)||'~'||Replace(:new.PKCOLNUM, ',', '.')||'~'||to_char(:new.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:new.PKCOLSTR)||'~'||to_char(:new.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colIY54HRUWQRC5LI4AMLNJDHNCAU',:new.COLINT);
            RDX_AUDIT.addValueStr(vChangeData,'new.colSW4DJGZ5LRCLFJCYZLQZGDNAME',:new.COLCHAR);
            RDX_AUDIT.addValueNum(vChangeData,'new.col6WWL3SC5TZBMZGHZSPPPJVV2BQ',:new.COLNUM);
            RDX_AUDIT.addValueDate(vChangeData,'new.colJ7FU6BTM75HQTCIAKYQDRXDY5U',:new.COLDATE);
            RDX_AUDIT.addValueDate(vChangeData,'new.col4TWPKWPOL5EB3GHUIUL6UFRHWQ',:new.COLTIMESTAMP);
            RDX_AUDIT.addValueStr(vChangeData,'new.colV5EPQ4QOLREEZOV2UUHUWZHYEE',:new.COLSTR);
            RDX_AUDIT.addValueRaw(vChangeData,'new.colTGVNWDGLGNBI3N5BYXZLI5RXQQ',:new.COLBIN);
            RDX_AUDIT.addValueBlob(vChangeData,'new.colFDVZCLAYD5HZRN2JGORFYFA3NE',:new.COLBLOB);
            RDX_AUDIT.addValueClob(vChangeData,'new.colL76ZGR3OPVHQFHYKDPWUIZQA34',:new.COLCLOB);
            RDX_AUDIT.addValueStr(vChangeData,'new.colI7RDFSIDUZE4PPBNSJFU4RCEIM',:new.COLARRINT);
            RDX_AUDIT.addValueClob(vChangeData,'new.colGAR4IF3GRVAFLA4DTQXPQGCSME',:new.COLARRCHAR);
            RDX_AUDIT.addValueClob(vChangeData,'new.colY5PXD2VKE5FBTFGVSCPTLGE46U',:new.COLARRSTR);
            RDX_AUDIT.addValueStr(vChangeData,'new.colE3SYXVPOCVEIFEEYBEIFXY66XQ',:new.COLARRNUM);
            RDX_AUDIT.addValueClob(vChangeData,'new.colYZYQHXB75VHEVG2BTGNGPNJZEA',:new.COLARRDATETIME);
            RDX_AUDIT.addValueClob(vChangeData,'new.col54URWAVOAVE4ZFLUZI7QEQYCDM',:new.COLARRBIN);
            RDX_AUDIT.addValueClob(vChangeData,'new.colD6G6MRNNKVGJFGSYHPF3DFQ66Y',:new.COLARRPARENTREF);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA', 'I','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger ati52CHFNO3EGWDBRCRAAIT4AGD7E
after insert on RDX_INSTANCE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl52CHFNO3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colUJAMRNBTMVE2BHL3BKCXMXSNMI',:new.DBTRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.col24KG33MBMRAATA7S5VFV34PLTE',:new.GUITRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colF666DZAJ3NF3BOLDRS6BDK75T4',:new.FILETRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colXGRPSYS6JVGHRMXMH2FEUSJMQU',:new.TRACEFILESDIR);
            RDX_AUDIT.addValueInt(vChangeData,'new.colKTN2GUM43VD5TH34XEED3NBSWM',:new.MAXTRACEFILESIZEKB);
            RDX_AUDIT.addValueInt(vChangeData,'new.colGPBWOB42OZCATJ6INHY24PAYLI',:new.MAXTRACEFILECNT);
            RDX_AUDIT.addValueStr(vChangeData,'new.colO6KNHKT4RHWDBRCXAAIT4AGD7E',:new.STARTOSCOMMAND);
            RDX_AUDIT.addValueStr(vChangeData,'new.colROO4LOT4RHWDBRCXAAIT4AGD7E',:new.STOPOSCOMMAND);
            RDX_AUDIT.addValueInt(vChangeData,'new.colA333YBDFB5EMVI6N4N6IUKFG3M',:new.SAPID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colNPEGS5CTHRHRDF7ZU3IPI7GELU',:new.SYSTEMID);
            RDX_AUDIT.addValueStr(vChangeData,'new.colYSPDMMOVEBGPZIMQX2RBMXCBQE',:new.SCPNAME);
            RDX_AUDIT.addValueInt(vChangeData,'new.col3YSQY2R375FKFKYACAY762WGD4',:new.KEYSTORETYPE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colDAQ557DC6BAZRI6VNI3WDLB5HQ',:new.KEYSTOREPATH);
            RDX_AUDIT.addValueStr(vChangeData,'new.colTLHWR7V5OJE53K6G4EJDPJY2XY',:new.KEYTABPATH);
            RDX_AUDIT.addValueInt(vChangeData,'new.col4Z2JD6POZNCZJLGNTEGIJHZEQQ',:new.LOWARTEINSTCOUNT);
            RDX_AUDIT.addValueInt(vChangeData,'new.col27DHEMV4S5FAVBSPLT6B6R4E6M',:new.HIGHARTEINSTCOUNT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colPVFIBHTN5NA35FF6RJCZIM6SAY',:new.ARTEINSTLIVETIME);
   end if;

   vClassGuid := 'aec52CHFNO3EGWDBRCRAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl52CHFNO3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl52CHFNO3EGWDBRCRAAIT4AGD7E', 'I','tbl52CHFNO3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger ati5HP4XTP3EGWDBRCRAAIT4AGD7E
after insert on RDX_UNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colEVX5YH2SS5VDBN2IAAUMFADAIA',:new.USE);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger ati5WO5N3BEVLOBDCLSAALOMT5GDM
after insert on RDX_PC_CHANNELUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colB2VQZVZFVLOBDCLSAALOMT5GDM',:new.KIND);
            RDX_AUDIT.addValueInt(vChangeData,'new.colNSHJLYZFVLOBDCLSAALOMT5GDM',:new.SENDPERIOD);
            RDX_AUDIT.addValueInt(vChangeData,'new.col6ARRB4BFVLOBDCLSAALOMT5GDM',:new.RECVPERIOD);
            RDX_AUDIT.addValueStr(vChangeData,'new.colWASXULBLVLOBDCLSAALOMT5GDM',:new.SENDADDRESS);
            RDX_AUDIT.addValueStr(vChangeData,'new.col4UVYQI3O4DOBDAPTABQJO5ADDQ',:new.POP3ADDRESS);
            RDX_AUDIT.addValueStr(vChangeData,'new.colNOHTUMZLVLOBDCLSAALOMT5GDM',:new.RECVADDRESS);
            RDX_AUDIT.addValueStr(vChangeData,'new.colPSI5Q6JLVLOBDCLSAALOMT5GDM',:new.MESSADDRESSREGEXP);
            RDX_AUDIT.addValueInt(vChangeData,'new.colVV73PCZLVLOBDCLSAALOMT5GDM',:new.ROUTINGPRIORITY);
            RDX_AUDIT.addValueStr(vChangeData,'new.colRPM5HGZLVLOBDCLSAALOMT5GDM',:new.ENCODING);
            RDX_AUDIT.addValueStr(vChangeData,'new.colHEW6Y7BQVLOBDCLSAALOMT5GDM',:new.SMPPSYSTEMID);
            RDX_AUDIT.addValueStr(vChangeData,'new.colZM275BJQVLOBDCLSAALOMT5GDM',:new.SMPPSYSTEMTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col2KCZRM4VHBC2XNT32CRN5UDKWE',:new.SMPPSESSIONTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col66NFXWMWJ5HDBJFYGSP3WGJZDI',:new.SMPPSESSIONTON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colSXQZYQ3C7JCLFP4XIHFNAS42P4',:new.SMPPSESSIONNPI);
            RDX_AUDIT.addValueStr(vChangeData,'new.colZ5DQRVBQVLOBDCLSAALOMT5GDM',:new.SMPPPASSWORD);
            RDX_AUDIT.addValueStr(vChangeData,'new.colAYWB5XRQVLOBDCLSAALOMT5GDM',:new.SMPPSOURCEADDRESS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colADKKX2RQVLOBDCLSAALOMT5GDM',:new.SMPPSOURCEADDRESSTON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colK43Q5NBRVLOBDCLSAALOMT5GDM',:new.SMPPSOURCEADDRESSNPI);
            RDX_AUDIT.addValueInt(vChangeData,'new.colHTZKDXRRVLOBDCLSAALOMT5GDM',:new.SMPPDESTINATIONTON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colHPZKDXRRVLOBDCLSAALOMT5GDM',:new.SMPPDESTINATIONNPI);
            RDX_AUDIT.addValueInt(vChangeData,'new.colJUHQZ4BRVLOBDCLSAALOMT5GDM',:new.SMPPINTERFACE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colJYHQZ4BRVLOBDCLSAALOMT5GDM',:new.SMPPMAXLEN);
            RDX_AUDIT.addValueStr(vChangeData,'new.colHR7J6PZSVLOBDCLSAALOMT5GDM',:new.EMAILLOGIN);
            RDX_AUDIT.addValueStr(vChangeData,'new.colVKPD2RZSVLOBDCLSAALOMT5GDM',:new.EMAILPASSWORD);
            RDX_AUDIT.addValueStr(vChangeData,'new.colDSHWAVJSVLOBDCLSAALOMT5GDM',:new.ADDRESSTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.col6CT2K2RSVLOBDCLSAALOMT5GDM',:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colTQE4J6T4NJEWHIDFMFRCFVLYIU',:new.FILEFORMAT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colV4JLFAINZBDR3MMLVE36PDT4FA',:new.SENDTIMEOUT);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5WO5N3BEVLOBDCLSAALOMT5GDM', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger ati6HF4SHAXLDNBDJA6ACQMTAIZT4
after insert on RDX_SCP2SAP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SYSTEMID||'~'||:new.SAPID||'~'||RDX_ENTITY.PackPIDStr(:new.SCPNAME);
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.col3EQFARYXLDNBDJA6ACQMTAIZT4',:new.SAPPRIORITY);
            RDX_AUDIT.addValueInt(vChangeData,'new.colJ6USVIY4J3NRDJIRACQMTAIZT4',:new.CONNECTTIMEOUT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colMMZ4BMI4J3NRDJIRACQMTAIZT4',:new.BLOCKINGPERIOD);
   end if;

   vClassGuid := 'aec6HF4SHAXLDNBDJA6ACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl6HF4SHAXLDNBDJA6ACQMTAIZT4', 'I','tbl6HF4SHAXLDNBDJA6ACQMTAIZT4');
end;
/

create or replace trigger ati7HTJAWJXVLOBDCLSAALOMT5GDM
after insert on RDX_PC_EVENTSUBSCRIPTION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl7HTJAWJXVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colDHOISLJZVLOBDCLSAALOMT5GDM',:new.ISACTIVE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colLHS5GRJZVLOBDCLSAALOMT5GDM',:new.USERGROUPNAME);
            RDX_AUDIT.addValueStr(vChangeData,'new.col5OTM5EZZVLOBDCLSAALOMT5GDM',:new.CHANNELKIND);
            RDX_AUDIT.addValueStr(vChangeData,'new.colZQGUBQRZVLOBDCLSAALOMT5GDM',:new.EVENTSOURCE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col6MRC5ZR3VLOBDCLSAALOMT5GDM',:new.MINEVENTSEVERITY);
            RDX_AUDIT.addValueStr(vChangeData,'new.colJOXDSNJ4VLOBDCLSAALOMT5GDM',:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colIS5OGPB4VLOBDCLSAALOMT5GDM',:new.BODYTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colRJPBERR4VLOBDCLSAALOMT5GDM',:new.LANGUAGE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col3RTQXCB5VLOBDCLSAALOMT5GDM',:new.LOWIMPORTANCEMAXSEV);
            RDX_AUDIT.addValueInt(vChangeData,'new.colAQICTNR5VLOBDCLSAALOMT5GDM',:new.NORMALIMPORTANCEMAXSEV);
            RDX_AUDIT.addValueInt(vChangeData,'new.colVNCG43RIBREHHI5FFCUATH4PTM',:new.TOSTOREINHIST);
            RDX_AUDIT.addValueStr(vChangeData,'new.colVH3DNHSDUBD3JPNV535DVDBYBU',:new.LIMITMESSSUBJECTTEMPLATE);
            RDX_AUDIT.addValueStr(vChangeData,'new.col2FJBO5EEGJCW3MOVEG3KLYQCGA',:new.LIMITMESSBODYTEMPLATE);
   end if;

   vClassGuid := 'aec7HTJAWJXVLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl7HTJAWJXVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl7HTJAWJXVLOBDCLSAALOMT5GDM', 'I','tbl7HTJAWJXVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atiAAU55TOEHJWDRPOYAAYQQ2Y3GB
after insert on RDX_SCP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblAAU55TOEHJWDRPOYAAYQQ2Y3GB'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.NAME)||'~'||:new.SYSTEMID;

   vClassGuid := 'aecAAU55TOEHJWDRPOYAAYQQ2Y3GB';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblAAU55TOEHJWDRPOYAAYQQ2Y3GB',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblAAU55TOEHJWDRPOYAAYQQ2Y3GB', 'I','tblAAU55TOEHJWDRPOYAAYQQ2Y3GB');
end;
/

create or replace trigger atiARTOV5KBI3OBDCIOAALOMT5GDM
after insert on RDX_JS_INTERVALSCHDITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblARTOV5KBI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SCHEDULEID||'~'||:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.col3J7ROKKCI3OBDCIOAALOMT5GDM',:new.CALENDARID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colPKW6YWCCI3OBDCIOAALOMT5GDM',:new.STARTTIME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colFCO7W2SCI3OBDCIOAALOMT5GDM',:new.ENDTIME);
   end if;

   vClassGuid := 'aecARTOV5KBI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblARTOV5KBI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblARTOV5KBI3OBDCIOAALOMT5GDM', 'I','tblARTOV5KBI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atiB6QEDVV5BNEWZHKZRTVR67TKOY
after insert on RDX_TST_CHILD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblB6QEDVV5BNEWZHKZRTVR67TKOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.col6OTOT3XC7BHKVIE3Y6WRX4O2OY',:new.CHILDTYPE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colZDX2SYK7TJAFTJENP6ATORQOTI',:new.COLBOOL);
            RDX_AUDIT.addValueStr(vChangeData,'new.colVTKWXW5C7RHLVACWBAAOZ3XJGY',:new.COLCHAR);
            RDX_AUDIT.addValueDate(vChangeData,'new.col7CYI2GEMAJA5LNUWSJ27NVIUZ4',:new.COLDATETIME);
   end if;

   vClassGuid := 'aecB6QEDVV5BNEWZHKZRTVR67TKOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblB6QEDVV5BNEWZHKZRTVR67TKOY',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblB6QEDVV5BNEWZHKZRTVR67TKOY', 'I','tblB6QEDVV5BNEWZHKZRTVR67TKOY');
end;
/

create or replace trigger atiC2OWQGDVVHWDBROXAAIT4AGD7E
after insert on RDX_SERVICE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblC2OWQGDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SYSTEMID||'~'||RDX_ENTITY.PackPIDStr(:new.URI);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.col3VUCNEI3RHWDBRCXAAIT4AGD7E',:new.WSDLURI);
            RDX_AUDIT.addValueStr(vChangeData,'new.colXXBGA3KXGZEP5EQS26UU6KWTEA',:new.ACCESSIBILITY);
   end if;

   vClassGuid := 'aecC2OWQGDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblC2OWQGDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblC2OWQGDVVHWDBROXAAIT4AGD7E', 'I','tblC2OWQGDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atiCRD53OZ5I3OBDCIOAALOMT5GDM
after insert on RDX_JS_CALENDARITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblCRD53OZ5I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colKRZR7QR5I3OBDCIOAALOMT5GDM',:new.CALENDARID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colOCF4R7J5I3OBDCIOAALOMT5GDM',:new.SEQ);
            RDX_AUDIT.addValueStr(vChangeData,'new.colFDW6KVZ6I3OBDCIOAALOMT5GDM',:new.OPER);
            RDX_AUDIT.addValueInt(vChangeData,'new.colYTXRDPZ6I3OBDCIOAALOMT5GDM',:new.OFFSET);
            RDX_AUDIT.addValueInt(vChangeData,'new.colDKQ7RUH5YZGIZMYREMQUYMV4OM',:new.OFFSETDIR);
            RDX_AUDIT.addValueInt(vChangeData,'new.colFQ4EXXZ6I3OBDCIOAALOMT5GDM',:new.INCCALENDARID);
            RDX_AUDIT.addValueDate(vChangeData,'new.colAZNNMYJ7I3OBDCIOAALOMT5GDM',:new.ABSDATE);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblCRD53OZ5I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblCRD53OZ5I3OBDCIOAALOMT5GDM', 'I','tblCRD53OZ5I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atiDYWJCJTTGLNRDHRZABQAQH3XQ4
after insert on RDX_AC_USER2USERGROUP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblDYWJCJTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.USERNAME)||'~'||RDX_ENTITY.PackPIDStr(:new.GROUPNAME);

   vClassGuid := 'aecDYWJCJTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblDYWJCJTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblDYWJCJTTGLNRDHRZABQAQH3XQ4', 'I','tblDYWJCJTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atiFDFDUXBHJ3NRDJIRACQMTAIZT4
after insert on RDX_STATION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFDFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.NAME);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colGHFDUXBHJ3NRDJIRACQMTAIZT4',:new.SCPNAME);
   end if;

   vClassGuid := 'aecFDFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFDFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFDFDUXBHJ3NRDJIRACQMTAIZT4', 'I','tblFDFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atiFJAEQT3TGLNRDHRZABQAQH3XQ4
after insert on RDX_AC_USERGROUP2ROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFJAEQT3TGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colXDHHLFZRY3NBDGMCABQAQH3XQ4',:new.GROUPNAME);
            RDX_AUDIT.addValueStr(vChangeData,'new.colQNEGZPZRY3NBDGMCABQAQH3XQ4',:new.ROLEID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colAULGQS3XVFDL5DE7L3MW46URPQ',:new.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addValueStr(vChangeData,'new.colDWXM6MYALRBC3OYCWTWRGS4DIE',:new.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aecFJAEQT3TGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFJAEQT3TGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFJAEQT3TGLNRDHRZABQAQH3XQ4', 'I','tblFJAEQT3TGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atiGPFDUXBHJ3NRDJIRACQMTAIZT4
after insert on RDX_USER2STATION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblGPFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.STATIONNAME)||'~'||RDX_ENTITY.PackPIDStr(:new.USERNAME);

   vClassGuid := 'aecGPFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblGPFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblGPFDUXBHJ3NRDJIRACQMTAIZT4', 'I','tblGPFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atiHBE24OUE2DNRDB7AAALOMT5GDM
after insert on RDX_NETCHANNEL
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHBE24OUE2DNRDB7AAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colQ2GUXJUE2DNRDB7AAALOMT5GDM',:new.ADDRESS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colGRXTHMME2DNRDB7AAALOMT5GDM',:new.LINKLEVELPROTOCOLKIND);
            RDX_AUDIT.addValueStr(vChangeData,'new.colTFACUXG6NJAMNJZ33CKFZTYORY',:new.REQUESTFRAME);
            RDX_AUDIT.addValueStr(vChangeData,'new.colJWZOTJWGMFBWDMHWTBSXO6MZNY',:new.RESPONSEFRAME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colVGDODIF6BLOBDCGJAALOMT5GDM',:new.RECVTIMEOUT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colKSQETMV6BLOBDCGJAALOMT5GDM',:new.SENDTIMEOUT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colF3F5Y5EF2DNRDB7AAALOMT5GDM',:new.MAXSESSIONCOUNT);
            RDX_AUDIT.addValueStr(vChangeData,'new.colMVKQVABX6FH7PAEUHL7URGEEAY',:new.GUITRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colNXKEWDHZK5CILMRUHOP7KW46Q4',:new.FILETRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colXOEZ5O4YNXOBDCJJAALOMT5GDM',:new.DBTRACEPROFILE);
            RDX_AUDIT.addValueInt(vChangeData,'new.col4QBLQUIZAZFQPILQILYXGOSMGM',:new.USE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colUUCCDQOTEJG77LPBXMHD6QDK5E',:new.ISCONNECTREADYNTFON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colWNURWZD5KBF7DPUPXHUSTXY2UI',:new.ISDISCONNECTNTFON);
            RDX_AUDIT.addValueInt(vChangeData,'new.colH2W5NJG535G7TCZERIU3I2KW6I',:new.SECURITYPROTOCOL);
            RDX_AUDIT.addValueInt(vChangeData,'new.colI5R5UG4B5JEUPMEJLRID7SISTY',:new.CHECKCLIENTCERT);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHBE24OUE2DNRDB7AAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHBE24OUE2DNRDB7AAALOMT5GDM', 'I','tblHBE24OUE2DNRDB7AAALOMT5GDM');
end;
/

create or replace trigger atiHC6VVBZ4I3OBDCIOAALOMT5GDM
after insert on RDX_JS_CALENDAR
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHC6VVBZ4I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHC6VVBZ4I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHC6VVBZ4I3OBDCIOAALOMT5GDM', 'I','tblHC6VVBZ4I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atiJ5AGLYGB6RGKNJ36XBBX5AZ4ZA
after insert on RDX_TST_PARENT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   vClassGuid := 'aecJ5AGLYGB6RGKNJ36XBBX5AZ4ZA';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA', 'I','tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA');
end;
/

create or replace trigger atiJ6SOXKD3ZHOBDCMTAALOMT5GDM
after insert on RDX_USERFUNC
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.UPDEFID)||'~'||RDX_ENTITY.PackPIDStr(:new.UPOWNERENTITYID)||'~'||RDX_ENTITY.PackPIDStr(:new.UPOWNERPID);

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ6SOXKD3ZHOBDCMTAALOMT5GDM', 'I','tblJ6SOXKD3ZHOBDCMTAALOMT5GDM');
end;
/

create or replace trigger atiLSU45NKDI3OBDCIOAALOMT5GDM
after insert on RDX_JS_EVENTSCHD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblLSU45NKDI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   vClassGuid := 'aecLSU45NKDI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblLSU45NKDI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblLSU45NKDI3OBDCIOAALOMT5GDM', 'I','tblLSU45NKDI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atiM47V6KTSNJC3FKWW2W6DME6Z2I
after insert on RDX_NETHUB
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM47V6KTSNJC3FKWW2W6DME6Z2I', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atiM7J46MP6F3PBDIJEABQAQH3XQ4
after insert on RDX_AC_APPROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblM7J46MP6F3PBDIJEABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colMFRUYXP6F3PBDIJEABQAQH3XQ4',:new.TITLE);
   end if;

   vClassGuid := 'aecM7J46MP6F3PBDIJEABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblM7J46MP6F3PBDIJEABQAQH3XQ4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM7J46MP6F3PBDIJEABQAQH3XQ4', 'I','tblM7J46MP6F3PBDIJEABQAQH3XQ4');
end;
/

create or replace trigger atiOVPOORJSBFAGZERW5IXTRFTWOY
after insert on RDX_PC_EVENTLIMITACC
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblOVPOORJSBFAGZERW5IXTRFTWOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SUBSCRIPTIONID||'~'||RDX_ENTITY.PackPIDStr(:new.USERNAME)||'~'||RDX_ENTITY.PackPIDStr(:new.EVENTCODE)||'~'||RDX_ENTITY.PackPIDStr(:new.CHANNELKIND);

   vClassGuid := 'aecOVPOORJSBFAGZERW5IXTRFTWOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblOVPOORJSBFAGZERW5IXTRFTWOY',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblOVPOORJSBFAGZERW5IXTRFTWOY', 'I','tblOVPOORJSBFAGZERW5IXTRFTWOY');
end;
/

create or replace trigger atiPCE24TUPIHNRDJIEACQMTAIZT4
after insert on RDX_JS_JOBEXECUTORUNITBOOST
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblPCE24TUPIHNRDJIEACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SPEED;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.col2SZPQ7UPIHNRDJIEACQMTAIZT4',:new.DELAY);
            RDX_AUDIT.addValueInt(vChangeData,'new.col33CWYGOTI3NRDJIIACQMTAIZT4',:new.MAXINCREMENT);
   end if;

   vClassGuid := 'aecPCE24TUPIHNRDJIEACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblPCE24TUPIHNRDJIEACQMTAIZT4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblPCE24TUPIHNRDJIEACQMTAIZT4', 'I','tblPCE24TUPIHNRDJIEACQMTAIZT4');
end;
/

create or replace trigger atiQ23AYDTTGLNRDHRZABQAQH3XQ4
after insert on RDX_AC_USERGROUP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQ23AYDTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.NAME);
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colIX2QYPQMY3NBDGMCABQAQH3XQ4',:new.TITLE);
   end if;

   vClassGuid := 'aecQ23AYDTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQ23AYDTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQ23AYDTTGLNRDHRZABQAQH3XQ4', 'I','tblQ23AYDTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atiR7FXMYDVVHWDBROXAAIT4AGD7E
after insert on RDX_SAP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblR7FXMYDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colFHZWU3QRLDNBDJA6ACQMTAIZT4',:new.SYSTEMID);
            RDX_AUDIT.addValueStr(vChangeData,'new.colDLZ4FCERW3NRDJLUACQMTAIZT4',:new.URI);
            RDX_AUDIT.addValueStr(vChangeData,'new.colKAQHY523RHWDBRCXAAIT4AGD7E',:new.ADDRESS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colREBZTTQ3J3NRDJIRACQMTAIZT4',:new.SECURITYPROTOCOL);
            RDX_AUDIT.addValueInt(vChangeData,'new.colZUEE6WCMK7NBDJA5ACQMTAIZT4',:new.CHECKCLIENTCERT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colMVGATV2VDJDSDLAPNPSZORVJZA',:new.EASKRBAUTH);
            RDX_AUDIT.addValueStr(vChangeData,'new.colUPLKI3MFVRHV5LOVX56JEFNYTY',:new.ACCESSIBILITY);
   end if;

   vClassGuid := 'aecR7FXMYDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblR7FXMYDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblR7FXMYDVVHWDBROXAAIT4AGD7E', 'I','tblR7FXMYDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atiS4NVQKVQI5HLTG33G43UK5QMXU
after insert on RDX_TST_AUDITDETAIL
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:new.PKCOLCHAR)||'~'||Replace(:new.PKCOLNUM, ',', '.')||'~'||to_char(:new.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:new.PKCOLSTR)||'~'||to_char(:new.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colB7LCJXCMXVFF7PZ7UH3YBVJXOQ',:new.TITLE);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_TST_AUDITMASTER M
       where M.PKCOLINT=:new.PKCOLINT and M.PKCOLCHAR=:new.PKCOLCHAR and M.PKCOLNUM=:new.PKCOLNUM and M.PKCOLDATE=:new.PKCOLDATE and M.PKCOLSTR=:new.PKCOLSTR and M.PKCOLTIMESTAMP=:new.PKCOLTIMESTAMP;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblS4NVQKVQI5HLTG33G43UK5QMXU', 'I','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atiSY4KIOLTGLNRDHRZABQAQH3XQ4
after insert on RDX_AC_USER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblSY4KIOLTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.NAME);
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colQ62TN7IYY3NBDGMCABQAQH3XQ4',:new.LOCKED);
            RDX_AUDIT.addValueStr(vChangeData,'new.col3FYUIAY5J3NRDJIRACQMTAIZT4',:new.PERSONNAME);
            RDX_AUDIT.addValueStr(vChangeData,'new.colNMVAZDYYY3NBDGMCABQAQH3XQ4',:new.ADMINGROUPNAME);
            RDX_AUDIT.addValueDate(vChangeData,'new.colPUPBTVQYY3NBDGMCABQAQH3XQ4',:new.LASTPWDCHANGETIME);
            RDX_AUDIT.addValueInt(vChangeData,'new.col5XNCT3QYY3NBDGMCABQAQH3XQ4',:new.PWDEXPIRATIONPERIOD);
            RDX_AUDIT.addValueInt(vChangeData,'new.colRC2TN7IYY3NBDGMCABQAQH3XQ4',:new.CHECKSTATION);
            RDX_AUDIT.addValueStr(vChangeData,'new.colWXQAHXB5V3OBDCLVAALOMT5GDM',:new.EMAIL);
            RDX_AUDIT.addValueStr(vChangeData,'new.colW3QAHXB5V3OBDCLVAALOMT5GDM',:new.MOBILEPHONE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colANJQ6ZDT6FALRCPTTSR3UUR5VQ',:new.MUSTCHANGEPWD);
   end if;

   vClassGuid := 'aecSY4KIOLTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblSY4KIOLTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblSY4KIOLTGLNRDHRZABQAQH3XQ4', 'I','tblSY4KIOLTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atiUEE3YXA72HNRDB7BAALOMT5GDM
after insert on RDX_NETPORTHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblUEE3YXA72HNRDB7BAALOMT5GDM', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atiWZB7K4HLJPOBDCIUAALOMT5GDM
after insert on RDX_JS_TASK
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblWZB7K4HLJPOBDCIUAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.colNAPRJRHLJPOBDCIUAALOMT5GDM',:new.SCHEDULEID);
            RDX_AUDIT.addValueStr(vChangeData,'new.colFDCRWKPMJPOBDCIUAALOMT5GDM',:new.CLASSGUID);
            RDX_AUDIT.addValueInt(vChangeData,'new.col7JDCGRXMJPOBDCIUAALOMT5GDM',:new.PRIORITY);
            RDX_AUDIT.addValueInt(vChangeData,'new.col33LE4YHMJPOBDCIUAALOMT5GDM',:new.PRIORITYBOOSTINGSPEED);
            RDX_AUDIT.addValueInt(vChangeData,'new.colYQWRAN7SK5GA3EGFI6TEPHYWL4',:new.EXPIREDPOLICY);
            RDX_AUDIT.addValueInt(vChangeData,'new.colRMH5P2FSTNABBMBNUIXU3AINEQ',:new.ISACTIVE);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblWZB7K4HLJPOBDCIUAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblWZB7K4HLJPOBDCIUAALOMT5GDM', 'I','tblWZB7K4HLJPOBDCIUAALOMT5GDM');
end;
/

create or replace trigger atiX5TD7JDVVHWDBROXAAIT4AGD7E
after insert on RDX_SYSTEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT.DEFAULTAUDITSCHEME
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblX5TD7JDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colTCOQ4HOZRHWDBRCXAAIT4AGD7E',:new.NAME);
            RDX_AUDIT.addValueStr(vChangeData,'new.col546FXWYZJ3NRDJIRACQMTAIZT4',:new.ARTELANGUAGE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colWGL2VHS7RNBIRA3NYYV3KGAIPI',:new.ARTECOUNTRY);
            RDX_AUDIT.addValueStr(vChangeData,'new.colMJDA72X3RRHX5AQ4RSCK3YNA44',:new.ARTEGUITRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colMYF3QYYJ7BFS7AJ6YRMZUFBE4M',:new.ARTEFILETRACEPROFILE);
            RDX_AUDIT.addValueStr(vChangeData,'new.colXLSJR3OUKLNRDAQSABIFNQAAAE',:new.ARTEDBTRACEPROFILE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colM6ABGYQYNVGYRC6NTJ76DIBZ4I',:new.EASSESSIONACTIVITYMINS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colLPOXOGA2J3NRDJIRACQMTAIZT4',:new.EASSESSIONINACTIVITYMINS);
            RDX_AUDIT.addValueStr(vChangeData,'new.colM6KLR5HJ3JDI7DTS5S52SRQEFU',:new.EASKRBPRINCNAME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI',:new.ASKUSERPWDAFTERINACTIVITY);
            RDX_AUDIT.addValueInt(vChangeData,'new.colUMLKGQCFD6VDBNKJAAUMFADAIA',:new.EVENTSTOREDAYS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colIH2FRGR6EKWDRWGPAAUUN6FMUG',:new.PCMSTOREDAYS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colRJ6YTFHGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD1);
            RDX_AUDIT.addValueInt(vChangeData,'new.col3J6FBUPGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD2);
            RDX_AUDIT.addValueInt(vChangeData,'new.col3N6FBUPGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD3);
            RDX_AUDIT.addValueInt(vChangeData,'new.colTJMVJWXGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD4);
            RDX_AUDIT.addValueInt(vChangeData,'new.colTNMVJWXGWPNBDPLGABQJO5ADDQ',:new.AUDITSTOREPERIOD5);
            RDX_AUDIT.addValueStr(vChangeData,'new.colEKZMW4XGWPNBDPLGABQJO5ADDQ',:new.DEFAULTAUDITSCHEMEID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colDCLAU6NMGVDSNKN7ZPB6SQJE5Y',:new.BLOCKUSERINVALIDLOGONCNT);
            RDX_AUDIT.addValueInt(vChangeData,'new.colQTEXFSBLXJES3AOB6SXXKNGQQE',:new.BLOCKUSERINVALIDLOGONMINS);
            RDX_AUDIT.addValueInt(vChangeData,'new.colLRLGIOF3DVB2XEFQXLCJLPUYTU',:new.ENABLESENSITIVETRACE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colKDV6X7RAL5DCBO2HENGADFNFTY',:new.PROFILELOGSTOREDAYS);
   end if;

   vClassGuid := 'aecX5TD7JDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblX5TD7JDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblX5TD7JDVVHWDBROXAAIT4AGD7E', 'I','tblX5TD7JDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atiXI6CAOIGDBAGJEE6JQEGJME43Q
after insert on RDX_WF_PROCESSTYPE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblXI6CAOIGDBAGJEE6JQEGJME43Q'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:new.GUID);

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblXI6CAOIGDBAGJEE6JQEGJME43Q',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXI6CAOIGDBAGJEE6JQEGJME43Q', 'I','tblXI6CAOIGDBAGJEE6JQEGJME43Q');
end;
/

create or replace trigger atiXOKR7CSUNFG3DCVQGB5LWIWIIU
after insert on RDX_ARTEUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colAQ5QDYFUHRCB7KJ5TDUEMJMRYI',:new.SERVICEURI);
            RDX_AUDIT.addValueInt(vChangeData,'new.colJP6YRRR6BVD73IIDYKAYED5HMQ',:new.HIGHARTEINSTCOUNT);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXOKR7CSUNFG3DCVQGB5LWIWIIU', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atiY2BRNTJ5VLOBDCLSAALOMT5GDM
after insert on RDX_PC_EVENTSUBSCRIPTIONCODE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblY2BRNTJ5VLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SUBSCRIPTIONID||'~'||RDX_ENTITY.PackPIDStr(:new.CODE);

   vClassGuid := 'aecY2BRNTJ5VLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblY2BRNTJ5VLOBDCLSAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblY2BRNTJ5VLOBDCLSAALOMT5GDM', 'I','tblY2BRNTJ5VLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atiYDGQACJVVLOBDCLSAALOMT5GDM
after insert on RDX_PC_CHANNELHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblYDGQACJVVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.UNITID||'~'||:new.SEQ;
   if vSaveData=1 then
            RDX_AUDIT.addValueStr(vChangeData,'new.colQMAHWLJVVLOBDCLSAALOMT5GDM',:new.CLASSGUID);
            RDX_AUDIT.addValueStr(vChangeData,'new.col2M2OGMZVVLOBDCLSAALOMT5GDM',:new.SUBJECTREGEXP);
            RDX_AUDIT.addValueStr(vChangeData,'new.colKQYXQWRVVLOBDCLSAALOMT5GDM',:new.BODYREGEXP);
   end if;

   vClassGuid := :new.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblYDGQACJVVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblYDGQACJVVLOBDCLSAALOMT5GDM', 'I','tblYDGQACJVVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atiZ3SDCSSEI3OBDCIOAALOMT5GDM
after insert on RDX_JS_EVENTSCHDITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZ3SDCSSEI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.SCHEDULEID||'~'||:new.ID;
   if vSaveData=1 then
            RDX_AUDIT.addValueInt(vChangeData,'new.col2Y4Z3HSRI3OBDCIOAALOMT5GDM',:new.CALENDARID);
            RDX_AUDIT.addValueInt(vChangeData,'new.colIINJLJ2RI3OBDCIOAALOMT5GDM',:new.REPEATABLE);
            RDX_AUDIT.addValueInt(vChangeData,'new.colVZL6POCRI3OBDCIOAALOMT5GDM',:new.STARTTIME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colQ2O2LSKRI3OBDCIOAALOMT5GDM',:new.ENDTIME);
            RDX_AUDIT.addValueInt(vChangeData,'new.colR3BKZWCRI3OBDCIOAALOMT5GDM',:new.PERIOD);
            RDX_AUDIT.addValueInt(vChangeData,'new.colKDDQ37CRI3OBDCIOAALOMT5GDM',:new.EVENTTIME);
   end if;

   vClassGuid := 'aecZ3SDCSSEI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZ3SDCSSEI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZ3SDCSSEI3OBDCIOAALOMT5GDM', 'I','tblZ3SDCSSEI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atiZBS5MS2BI3OBDCIOAALOMT5GDM
after insert on RDX_JS_INTERVALSCHD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZBS5MS2BI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   vClassGuid := 'aecZBS5MS2BI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZBS5MS2BI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZBS5MS2BI3OBDCIOAALOMT5GDM', 'I','tblZBS5MS2BI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atiZUTGVH5TKRFWZEUGZHC5KRIOFA
after insert on RDX_JMSHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'I';
   exception
      when no_data_found then return;
   end;
   vPID:=:new.ID;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:new.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'I',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZUTGVH5TKRFWZEUGZHC5KRIOFA', 'I','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu3DD7EMEOIHNRDJIEACQMTAIZT4
after update of ABOVENORMALDELTA, CRITICALDELTA, EXECPERIOD, HIGHDELTA, PARALLELCNT, VERYHIGHDELTA on RDX_JS_JOBEXECUTORUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colPJDE6LVONJB3PPTQ7ZCF6UPQOI',:old.PARALLELCNT,:new.PARALLELCNT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colYS346KYNLVBNFJEHV2EA2KPKKM',:old.ABOVENORMALDELTA,:new.ABOVENORMALDELTA);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colHBV4J6LEWNCOTM4WHJRSIMLKFM',:old.HIGHDELTA,:new.HIGHDELTA);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colNJB3FZVNRNGXXMYCHMBRAXJZI4',:old.VERYHIGHDELTA,:new.VERYHIGHDELTA);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colUX424WYE3ZGARCSQMV4REKOFK4',:old.CRITICALDELTA,:new.CRITICALDELTA);
            RDX_AUDIT.addChangedValueNum(vChangeData,'colCCFCKQ4OIHNRDJIEACQMTAIZT4',:old.EXECPERIOD,:new.EXECPERIOD);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl3DD7EMEOIHNRDJIEACQMTAIZT4', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu42K4K2TTGLNRDHRZABQAQH3XQ4
after update of ISOWN, MA$$1ZOQHCO35XORDCV2AANE2UAFXA, PA$$1ZOQHCO35XORDCV2AANE2UAFXA, ROLEID, USERNAME on RDX_AC_USER2ROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl42K4K2TTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colCN3XEDKAZDNBDGMDABQAQH3XQ4',:old.USERNAME,:new.USERNAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colDC3DKGLHPDNRDHTWABQAQH3XQ4',:old.ISOWN,:new.ISOWN);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colFXSCOJCAZDNBDGMDABQAQH3XQ4',:old.ROLEID,:new.ROLEID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col4T2IAH72O5E6RNZPX3MNT44CQE',:old.MA$$1ZOQHCO35XORDCV2AANE2UAFXA,:new.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colD7VLPHOA3VC4XL3QOLTWZLUHJI',:old.PA$$1ZOQHCO35XORDCV2AANE2UAFXA,:new.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aec42K4K2TTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl42K4K2TTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl42K4K2TTGLNRDHRZABQAQH3XQ4', 'U','tbl42K4K2TTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atu4UBCGVD5ZZF6ZIJN4J3YISPFZA
after update of COLARRNUM, COLBIN, COLCHAR, COLDATE, COLINT, COLNUM, COLSTR, COLTIMESTAMP on RDX_TST_AUDITMASTER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colIY54HRUWQRC5LI4AMLNJDHNCAU',:old.COLINT,:new.COLINT);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colSW4DJGZ5LRCLFJCYZLQZGDNAME',:old.COLCHAR,:new.COLCHAR);
            RDX_AUDIT.addChangedValueNum(vChangeData,'col6WWL3SC5TZBMZGHZSPPPJVV2BQ',:old.COLNUM,:new.COLNUM);
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colJ7FU6BTM75HQTCIAKYQDRXDY5U',:old.COLDATE,:new.COLDATE);
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'col4TWPKWPOL5EB3GHUIUL6UFRHWQ',:old.COLTIMESTAMP,:new.COLTIMESTAMP);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colV5EPQ4QOLREEZOV2UUHUWZHYEE',:old.COLSTR,:new.COLSTR);
            RDX_AUDIT.addChangedValueRaw(vChangeData,'colTGVNWDGLGNBI3N5BYXZLI5RXQQ',:old.COLBIN,:new.COLBIN);
            RDX_AUDIT.addChangedValueBlob(vChangeData,'colFDVZCLAYD5HZRN2JGORFYFA3NE',:old.COLBLOB,:new.COLBLOB);
            RDX_AUDIT.addChangedValueClob(vChangeData,'colL76ZGR3OPVHQFHYKDPWUIZQA34',:old.COLCLOB,:new.COLCLOB);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colI7RDFSIDUZE4PPBNSJFU4RCEIM',:old.COLARRINT,:new.COLARRINT);
            RDX_AUDIT.addChangedValueClob(vChangeData,'colGAR4IF3GRVAFLA4DTQXPQGCSME',:old.COLARRCHAR,:new.COLARRCHAR);
            RDX_AUDIT.addChangedValueClob(vChangeData,'colY5PXD2VKE5FBTFGVSCPTLGE46U',:old.COLARRSTR,:new.COLARRSTR);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colE3SYXVPOCVEIFEEYBEIFXY66XQ',:old.COLARRNUM,:new.COLARRNUM);
            RDX_AUDIT.addChangedValueClob(vChangeData,'colYZYQHXB75VHEVG2BTGNGPNJZEA',:old.COLARRDATETIME,:new.COLARRDATETIME);
            RDX_AUDIT.addChangedValueClob(vChangeData,'col54URWAVOAVE4ZFLUZI7QEQYCDM',:old.COLARRBIN,:new.COLARRBIN);
            RDX_AUDIT.addChangedValueClob(vChangeData,'colD6G6MRNNKVGJFGSYHPF3DFQ66Y',:old.COLARRPARENTREF,:new.COLARRPARENTREF);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA', 'U','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atu52CHFNO3EGWDBRCRAAIT4AGD7E
after update of ARTEINSTLIVETIME, DBTRACEPROFILE, FILETRACEPROFILE, GUITRACEPROFILE, HIGHARTEINSTCOUNT, KEYSTOREPATH, KEYSTORETYPE, KEYTABPATH, LOWARTEINSTCOUNT, MAXTRACEFILECNT, MAXTRACEFILESIZEKB, SCPNAME, STARTOSCOMMAND, STOPOSCOMMAND, TRACEFILESDIR on RDX_INSTANCE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl52CHFNO3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colUJAMRNBTMVE2BHL3BKCXMXSNMI',:old.DBTRACEPROFILE,:new.DBTRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col24KG33MBMRAATA7S5VFV34PLTE',:old.GUITRACEPROFILE,:new.GUITRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colF666DZAJ3NF3BOLDRS6BDK75T4',:old.FILETRACEPROFILE,:new.FILETRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXGRPSYS6JVGHRMXMH2FEUSJMQU',:old.TRACEFILESDIR,:new.TRACEFILESDIR);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKTN2GUM43VD5TH34XEED3NBSWM',:old.MAXTRACEFILESIZEKB,:new.MAXTRACEFILESIZEKB);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colGPBWOB42OZCATJ6INHY24PAYLI',:old.MAXTRACEFILECNT,:new.MAXTRACEFILECNT);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colO6KNHKT4RHWDBRCXAAIT4AGD7E',:old.STARTOSCOMMAND,:new.STARTOSCOMMAND);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colROO4LOT4RHWDBRCXAAIT4AGD7E',:old.STOPOSCOMMAND,:new.STOPOSCOMMAND);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colYSPDMMOVEBGPZIMQX2RBMXCBQE',:old.SCPNAME,:new.SCPNAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3YSQY2R375FKFKYACAY762WGD4',:old.KEYSTORETYPE,:new.KEYSTORETYPE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colDAQ557DC6BAZRI6VNI3WDLB5HQ',:old.KEYSTOREPATH,:new.KEYSTOREPATH);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTLHWR7V5OJE53K6G4EJDPJY2XY',:old.KEYTABPATH,:new.KEYTABPATH);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col4Z2JD6POZNCZJLGNTEGIJHZEQQ',:old.LOWARTEINSTCOUNT,:new.LOWARTEINSTCOUNT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col27DHEMV4S5FAVBSPLT6B6R4E6M',:old.HIGHARTEINSTCOUNT,:new.HIGHARTEINSTCOUNT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colPVFIBHTN5NA35FF6RJCZIM6SAY',:old.ARTEINSTLIVETIME,:new.ARTEINSTLIVETIME);
   end if;

   vClassGuid := 'aec52CHFNO3EGWDBRCRAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl52CHFNO3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl52CHFNO3EGWDBRCRAAIT4AGD7E', 'U','tbl52CHFNO3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu5HP4XTP3EGWDBRCRAAIT4AGD7E
after update of USE on RDX_UNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colEVX5YH2SS5VDBN2IAAUMFADAIA',:old.USE,:new.USE);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5HP4XTP3EGWDBRCRAAIT4AGD7E', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu5WO5N3BEVLOBDCLSAALOMT5GDM
after update of ADDRESSTEMPLATE, EMAILLOGIN, EMAILPASSWORD, ENCODING, FILEFORMAT, KIND, MESSADDRESSREGEXP, POP3ADDRESS, RECVADDRESS, RECVPERIOD, ROUTINGPRIORITY, SENDADDRESS, SENDPERIOD, SENDTIMEOUT, SMPPDESTINATIONNPI, SMPPDESTINATIONTON, SMPPINTERFACE, SMPPMAXLEN, SMPPPASSWORD, SMPPSESSIONNPI, SMPPSESSIONTON, SMPPSESSIONTYPE, SMPPSOURCEADDRESS, SMPPSOURCEADDRESSNPI, SMPPSOURCEADDRESSTON, SMPPSYSTEMID, SMPPSYSTEMTYPE, SUBJECTTEMPLATE on RDX_PC_CHANNELUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colB2VQZVZFVLOBDCLSAALOMT5GDM',:old.KIND,:new.KIND);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colNSHJLYZFVLOBDCLSAALOMT5GDM',:old.SENDPERIOD,:new.SENDPERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col6ARRB4BFVLOBDCLSAALOMT5GDM',:old.RECVPERIOD,:new.RECVPERIOD);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colWASXULBLVLOBDCLSAALOMT5GDM',:old.SENDADDRESS,:new.SENDADDRESS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col4UVYQI3O4DOBDAPTABQJO5ADDQ',:old.POP3ADDRESS,:new.POP3ADDRESS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colNOHTUMZLVLOBDCLSAALOMT5GDM',:old.RECVADDRESS,:new.RECVADDRESS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colPSI5Q6JLVLOBDCLSAALOMT5GDM',:old.MESSADDRESSREGEXP,:new.MESSADDRESSREGEXP);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colVV73PCZLVLOBDCLSAALOMT5GDM',:old.ROUTINGPRIORITY,:new.ROUTINGPRIORITY);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colRPM5HGZLVLOBDCLSAALOMT5GDM',:old.ENCODING,:new.ENCODING);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colHEW6Y7BQVLOBDCLSAALOMT5GDM',:old.SMPPSYSTEMID,:new.SMPPSYSTEMID);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colZM275BJQVLOBDCLSAALOMT5GDM',:old.SMPPSYSTEMTYPE,:new.SMPPSYSTEMTYPE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col2KCZRM4VHBC2XNT32CRN5UDKWE',:old.SMPPSESSIONTYPE,:new.SMPPSESSIONTYPE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col66NFXWMWJ5HDBJFYGSP3WGJZDI',:old.SMPPSESSIONTON,:new.SMPPSESSIONTON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colSXQZYQ3C7JCLFP4XIHFNAS42P4',:old.SMPPSESSIONNPI,:new.SMPPSESSIONNPI);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colZ5DQRVBQVLOBDCLSAALOMT5GDM',:old.SMPPPASSWORD,:new.SMPPPASSWORD);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colAYWB5XRQVLOBDCLSAALOMT5GDM',:old.SMPPSOURCEADDRESS,:new.SMPPSOURCEADDRESS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colADKKX2RQVLOBDCLSAALOMT5GDM',:old.SMPPSOURCEADDRESSTON,:new.SMPPSOURCEADDRESSTON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colK43Q5NBRVLOBDCLSAALOMT5GDM',:old.SMPPSOURCEADDRESSNPI,:new.SMPPSOURCEADDRESSNPI);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colHTZKDXRRVLOBDCLSAALOMT5GDM',:old.SMPPDESTINATIONTON,:new.SMPPDESTINATIONTON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colHPZKDXRRVLOBDCLSAALOMT5GDM',:old.SMPPDESTINATIONNPI,:new.SMPPDESTINATIONNPI);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJUHQZ4BRVLOBDCLSAALOMT5GDM',:old.SMPPINTERFACE,:new.SMPPINTERFACE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJYHQZ4BRVLOBDCLSAALOMT5GDM',:old.SMPPMAXLEN,:new.SMPPMAXLEN);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colHR7J6PZSVLOBDCLSAALOMT5GDM',:old.EMAILLOGIN,:new.EMAILLOGIN);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colVKPD2RZSVLOBDCLSAALOMT5GDM',:old.EMAILPASSWORD,:new.EMAILPASSWORD);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colDSHWAVJSVLOBDCLSAALOMT5GDM',:old.ADDRESSTEMPLATE,:new.ADDRESSTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6CT2K2RSVLOBDCLSAALOMT5GDM',:old.SUBJECTTEMPLATE,:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTQE4J6T4NJEWHIDFMFRCFVLYIU',:old.FILEFORMAT,:new.FILEFORMAT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colV4JLFAINZBDR3MMLVE36PDT4FA',:old.SENDTIMEOUT,:new.SENDTIMEOUT);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl5WO5N3BEVLOBDCLSAALOMT5GDM', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atu6HF4SHAXLDNBDJA6ACQMTAIZT4
after update of BLOCKINGPERIOD, CONNECTTIMEOUT, SAPPRIORITY on RDX_SCP2SAP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SYSTEMID||'~'||:old.SAPID||'~'||RDX_ENTITY.PackPIDStr(:old.SCPNAME);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3EQFARYXLDNBDJA6ACQMTAIZT4',:old.SAPPRIORITY,:new.SAPPRIORITY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJ6USVIY4J3NRDJIRACQMTAIZT4',:old.CONNECTTIMEOUT,:new.CONNECTTIMEOUT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colMMZ4BMI4J3NRDJIRACQMTAIZT4',:old.BLOCKINGPERIOD,:new.BLOCKINGPERIOD);
   end if;

   vClassGuid := 'aec6HF4SHAXLDNBDJA6ACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl6HF4SHAXLDNBDJA6ACQMTAIZT4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl6HF4SHAXLDNBDJA6ACQMTAIZT4', 'U','tbl6HF4SHAXLDNBDJA6ACQMTAIZT4');
end;
/

create or replace trigger atu7HTJAWJXVLOBDCLSAALOMT5GDM
after update of BODYTEMPLATE, CHANNELKIND, EVENTSOURCE, ISACTIVE, LANGUAGE, LIMITMESSBODYTEMPLATE, LIMITMESSSUBJECTTEMPLATE, LOWIMPORTANCEMAXSEV, MINEVENTSEVERITY, NORMALIMPORTANCEMAXSEV, SUBJECTTEMPLATE, TOSTOREINHIST, USERGROUPNAME on RDX_PC_EVENTSUBSCRIPTION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl7HTJAWJXVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colDHOISLJZVLOBDCLSAALOMT5GDM',:old.ISACTIVE,:new.ISACTIVE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colLHS5GRJZVLOBDCLSAALOMT5GDM',:old.USERGROUPNAME,:new.USERGROUPNAME);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col5OTM5EZZVLOBDCLSAALOMT5GDM',:old.CHANNELKIND,:new.CHANNELKIND);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colZQGUBQRZVLOBDCLSAALOMT5GDM',:old.EVENTSOURCE,:new.EVENTSOURCE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col6MRC5ZR3VLOBDCLSAALOMT5GDM',:old.MINEVENTSEVERITY,:new.MINEVENTSEVERITY);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colJOXDSNJ4VLOBDCLSAALOMT5GDM',:old.SUBJECTTEMPLATE,:new.SUBJECTTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colIS5OGPB4VLOBDCLSAALOMT5GDM',:old.BODYTEMPLATE,:new.BODYTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colRJPBERR4VLOBDCLSAALOMT5GDM',:old.LANGUAGE,:new.LANGUAGE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3RTQXCB5VLOBDCLSAALOMT5GDM',:old.LOWIMPORTANCEMAXSEV,:new.LOWIMPORTANCEMAXSEV);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colAQICTNR5VLOBDCLSAALOMT5GDM',:old.NORMALIMPORTANCEMAXSEV,:new.NORMALIMPORTANCEMAXSEV);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colVNCG43RIBREHHI5FFCUATH4PTM',:old.TOSTOREINHIST,:new.TOSTOREINHIST);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colVH3DNHSDUBD3JPNV535DVDBYBU',:old.LIMITMESSSUBJECTTEMPLATE,:new.LIMITMESSSUBJECTTEMPLATE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col2FJBO5EEGJCW3MOVEG3KLYQCGA',:old.LIMITMESSBODYTEMPLATE,:new.LIMITMESSBODYTEMPLATE);
   end if;

   vClassGuid := 'aec7HTJAWJXVLOBDCLSAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl7HTJAWJXVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tbl7HTJAWJXVLOBDCLSAALOMT5GDM', 'U','tbl7HTJAWJXVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atuARTOV5KBI3OBDCIOAALOMT5GDM
after update of CALENDARID, ENDTIME, STARTTIME on RDX_JS_INTERVALSCHDITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblARTOV5KBI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SCHEDULEID||'~'||:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3J7ROKKCI3OBDCIOAALOMT5GDM',:old.CALENDARID,:new.CALENDARID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colPKW6YWCCI3OBDCIOAALOMT5GDM',:old.STARTTIME,:new.STARTTIME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colFCO7W2SCI3OBDCIOAALOMT5GDM',:old.ENDTIME,:new.ENDTIME);
   end if;

   vClassGuid := 'aecARTOV5KBI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblARTOV5KBI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblARTOV5KBI3OBDCIOAALOMT5GDM', 'U','tblARTOV5KBI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atuB6QEDVV5BNEWZHKZRTVR67TKOY
after update of CHILDTYPE, COLBOOL, COLCHAR, COLDATETIME on RDX_TST_CHILD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblB6QEDVV5BNEWZHKZRTVR67TKOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'col6OTOT3XC7BHKVIE3Y6WRX4O2OY',:old.CHILDTYPE,:new.CHILDTYPE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colZDX2SYK7TJAFTJENP6ATORQOTI',:old.COLBOOL,:new.COLBOOL);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colVTKWXW5C7RHLVACWBAAOZ3XJGY',:old.COLCHAR,:new.COLCHAR);
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'col7CYI2GEMAJA5LNUWSJ27NVIUZ4',:old.COLDATETIME,:new.COLDATETIME);
   end if;

   vClassGuid := 'aecB6QEDVV5BNEWZHKZRTVR67TKOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblB6QEDVV5BNEWZHKZRTVR67TKOY',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblB6QEDVV5BNEWZHKZRTVR67TKOY', 'U','tblB6QEDVV5BNEWZHKZRTVR67TKOY');
end;
/

create or replace trigger atuC2OWQGDVVHWDBROXAAIT4AGD7E
after update of ACCESSIBILITY, WSDLURI on RDX_SERVICE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblC2OWQGDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SYSTEMID||'~'||RDX_ENTITY.PackPIDStr(:old.URI);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'col3VUCNEI3RHWDBRCXAAIT4AGD7E',:old.WSDLURI,:new.WSDLURI);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXXBGA3KXGZEP5EQS26UU6KWTEA',:old.ACCESSIBILITY,:new.ACCESSIBILITY);
   end if;

   vClassGuid := 'aecC2OWQGDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblC2OWQGDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblC2OWQGDVVHWDBROXAAIT4AGD7E', 'U','tblC2OWQGDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atuCRD53OZ5I3OBDCIOAALOMT5GDM
after update of ABSDATE, CALENDARID, INCCALENDARID, OFFSET, OFFSETDIR, OPER, SEQ on RDX_JS_CALENDARITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblCRD53OZ5I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKRZR7QR5I3OBDCIOAALOMT5GDM',:old.CALENDARID,:new.CALENDARID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colOCF4R7J5I3OBDCIOAALOMT5GDM',:old.SEQ,:new.SEQ);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colFDW6KVZ6I3OBDCIOAALOMT5GDM',:old.OPER,:new.OPER);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colYTXRDPZ6I3OBDCIOAALOMT5GDM',:old.OFFSET,:new.OFFSET);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colDKQ7RUH5YZGIZMYREMQUYMV4OM',:old.OFFSETDIR,:new.OFFSETDIR);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colFQ4EXXZ6I3OBDCIOAALOMT5GDM',:old.INCCALENDARID,:new.INCCALENDARID);
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colAZNNMYJ7I3OBDCIOAALOMT5GDM',:old.ABSDATE,:new.ABSDATE);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblCRD53OZ5I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblCRD53OZ5I3OBDCIOAALOMT5GDM', 'U','tblCRD53OZ5I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atuFDFDUXBHJ3NRDJIRACQMTAIZT4
after update of SCPNAME on RDX_STATION
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFDFDUXBHJ3NRDJIRACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colGHFDUXBHJ3NRDJIRACQMTAIZT4',:old.SCPNAME,:new.SCPNAME);
   end if;

   vClassGuid := 'aecFDFDUXBHJ3NRDJIRACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFDFDUXBHJ3NRDJIRACQMTAIZT4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFDFDUXBHJ3NRDJIRACQMTAIZT4', 'U','tblFDFDUXBHJ3NRDJIRACQMTAIZT4');
end;
/

create or replace trigger atuFJAEQT3TGLNRDHRZABQAQH3XQ4
after update of GROUPNAME, MA$$1ZOQHCO35XORDCV2AANE2UAFXA, PA$$1ZOQHCO35XORDCV2AANE2UAFXA, ROLEID on RDX_AC_USERGROUP2ROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblFJAEQT3TGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXDHHLFZRY3NBDGMCABQAQH3XQ4',:old.GROUPNAME,:new.GROUPNAME);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colQNEGZPZRY3NBDGMCABQAQH3XQ4',:old.ROLEID,:new.ROLEID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colAULGQS3XVFDL5DE7L3MW46URPQ',:old.MA$$1ZOQHCO35XORDCV2AANE2UAFXA,:new.MA$$1ZOQHCO35XORDCV2AANE2UAFXA);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colDWXM6MYALRBC3OYCWTWRGS4DIE',:old.PA$$1ZOQHCO35XORDCV2AANE2UAFXA,:new.PA$$1ZOQHCO35XORDCV2AANE2UAFXA);
   end if;

   vClassGuid := 'aecFJAEQT3TGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblFJAEQT3TGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblFJAEQT3TGLNRDHRZABQAQH3XQ4', 'U','tblFJAEQT3TGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atuHBE24OUE2DNRDB7AAALOMT5GDM
after update of ADDRESS, CHECKCLIENTCERT, CLIENTCERTALIASES, DBTRACEPROFILE, FILETRACEPROFILE, GUITRACEPROFILE, ISCONNECTREADYNTFON, ISDISCONNECTNTFON, LINKLEVELPROTOCOLKIND, MAXSESSIONCOUNT, RECVTIMEOUT, REQUESTFRAME, RESPONSEFRAME, SECURITYPROTOCOL, SENDTIMEOUT, SERVERKEYALIASES, USE on RDX_NETCHANNEL
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHBE24OUE2DNRDB7AAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colQ2GUXJUE2DNRDB7AAALOMT5GDM',:old.ADDRESS,:new.ADDRESS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colGRXTHMME2DNRDB7AAALOMT5GDM',:old.LINKLEVELPROTOCOLKIND,:new.LINKLEVELPROTOCOLKIND);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTFACUXG6NJAMNJZ33CKFZTYORY',:old.REQUESTFRAME,:new.REQUESTFRAME);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colJWZOTJWGMFBWDMHWTBSXO6MZNY',:old.RESPONSEFRAME,:new.RESPONSEFRAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colVGDODIF6BLOBDCGJAALOMT5GDM',:old.RECVTIMEOUT,:new.RECVTIMEOUT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKSQETMV6BLOBDCGJAALOMT5GDM',:old.SENDTIMEOUT,:new.SENDTIMEOUT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colF3F5Y5EF2DNRDB7AAALOMT5GDM',:old.MAXSESSIONCOUNT,:new.MAXSESSIONCOUNT);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colMVKQVABX6FH7PAEUHL7URGEEAY',:old.GUITRACEPROFILE,:new.GUITRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colNXKEWDHZK5CILMRUHOP7KW46Q4',:old.FILETRACEPROFILE,:new.FILETRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXOEZ5O4YNXOBDCJJAALOMT5GDM',:old.DBTRACEPROFILE,:new.DBTRACEPROFILE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col4QBLQUIZAZFQPILQILYXGOSMGM',:old.USE,:new.USE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colUUCCDQOTEJG77LPBXMHD6QDK5E',:old.ISCONNECTREADYNTFON,:new.ISCONNECTREADYNTFON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colWNURWZD5KBF7DPUPXHUSTXY2UI',:old.ISDISCONNECTNTFON,:new.ISDISCONNECTNTFON);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colH2W5NJG535G7TCZERIU3I2KW6I',:old.SECURITYPROTOCOL,:new.SECURITYPROTOCOL);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colI5R5UG4B5JEUPMEJLRID7SISTY',:old.CHECKCLIENTCERT,:new.CHECKCLIENTCERT);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6OTLV7WBZVGDFM7SYNM5KXYSGQ',:old.SERVERKEYALIASES,:new.SERVERKEYALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colSOCMOJR6SRCGBL63P2ALGCKNUQ',:old.CLIENTCERTALIASES,:new.CLIENTCERTALIASES);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHBE24OUE2DNRDB7AAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHBE24OUE2DNRDB7AAALOMT5GDM', 'U','tblHBE24OUE2DNRDB7AAALOMT5GDM');
end;
/

create or replace trigger atuHC6VVBZ4I3OBDCIOAALOMT5GDM
after update of LASTUPDATETIME on RDX_JS_CALENDAR
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblHC6VVBZ4I3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colUJIT2SZ5I3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME,:new.LASTUPDATETIME);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblHC6VVBZ4I3OBDCIOAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblHC6VVBZ4I3OBDCIOAALOMT5GDM', 'U','tblHC6VVBZ4I3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atuJ5AGLYGB6RGKNJ36XBBX5AZ4ZA
after update on RDX_TST_PARENT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;

   vClassGuid := 'aecJ5AGLYGB6RGKNJ36XBBX5AZ4ZA';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA', 'U','tblJ5AGLYGB6RGKNJ36XBBX5AZ4ZA');
end;
/

create or replace trigger atuJ6SOXKD3ZHOBDCMTAALOMT5GDM
after update of LASTUPDATETIME on RDX_USERFUNC
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.UPDEFID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERENTITYID)||'~'||RDX_ENTITY.PackPIDStr(:old.UPOWNERPID);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colYF2NT733ZHOBDCMTAALOMT5GDM',:old.LASTUPDATETIME,:new.LASTUPDATETIME);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblJ6SOXKD3ZHOBDCMTAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblJ6SOXKD3ZHOBDCMTAALOMT5GDM', 'U','tblJ6SOXKD3ZHOBDCMTAALOMT5GDM');
end;
/

create or replace trigger atuLSU45NKDI3OBDCIOAALOMT5GDM
after update of LASTUPDATETIME on RDX_JS_EVENTSCHD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblLSU45NKDI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colYZCK5ZCDI3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME,:new.LASTUPDATETIME);
   end if;

   vClassGuid := 'aecLSU45NKDI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblLSU45NKDI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblLSU45NKDI3OBDCIOAALOMT5GDM', 'U','tblLSU45NKDI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atuM47V6KTSNJC3FKWW2W6DME6Z2I
after update of SAPID on RDX_NETHUB
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colBBSLHM5KRFC6VCPLABSQU5OUIY',:old.SAPID,:new.SAPID);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM47V6KTSNJC3FKWW2W6DME6Z2I', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atuM7J46MP6F3PBDIJEABQAQH3XQ4
after update of TITLE on RDX_AC_APPROLE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblM7J46MP6F3PBDIJEABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colMFRUYXP6F3PBDIJEABQAQH3XQ4',:old.TITLE,:new.TITLE);
   end if;

   vClassGuid := 'aecM7J46MP6F3PBDIJEABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblM7J46MP6F3PBDIJEABQAQH3XQ4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblM7J46MP6F3PBDIJEABQAQH3XQ4', 'U','tblM7J46MP6F3PBDIJEABQAQH3XQ4');
end;
/

create or replace trigger atuOVPOORJSBFAGZERW5IXTRFTWOY
after update on RDX_PC_EVENTLIMITACC
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblOVPOORJSBFAGZERW5IXTRFTWOY'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SUBSCRIPTIONID||'~'||RDX_ENTITY.PackPIDStr(:old.USERNAME)||'~'||RDX_ENTITY.PackPIDStr(:old.EVENTCODE)||'~'||RDX_ENTITY.PackPIDStr(:old.CHANNELKIND);

   vClassGuid := 'aecOVPOORJSBFAGZERW5IXTRFTWOY';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblOVPOORJSBFAGZERW5IXTRFTWOY',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblOVPOORJSBFAGZERW5IXTRFTWOY', 'U','tblOVPOORJSBFAGZERW5IXTRFTWOY');
end;
/

create or replace trigger atuPCE24TUPIHNRDJIEACQMTAIZT4
after update of DELAY, MAXINCREMENT on RDX_JS_JOBEXECUTORUNITBOOST
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblPCE24TUPIHNRDJIEACQMTAIZT4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SPEED;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'col2SZPQ7UPIHNRDJIEACQMTAIZT4',:old.DELAY,:new.DELAY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col33CWYGOTI3NRDJIIACQMTAIZT4',:old.MAXINCREMENT,:new.MAXINCREMENT);
   end if;

   vClassGuid := 'aecPCE24TUPIHNRDJIEACQMTAIZT4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblPCE24TUPIHNRDJIEACQMTAIZT4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblPCE24TUPIHNRDJIEACQMTAIZT4', 'U','tblPCE24TUPIHNRDJIEACQMTAIZT4');
end;
/

create or replace trigger atuQ23AYDTTGLNRDHRZABQAQH3XQ4
after update of TITLE on RDX_AC_USERGROUP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblQ23AYDTTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colIX2QYPQMY3NBDGMCABQAQH3XQ4',:old.TITLE,:new.TITLE);
   end if;

   vClassGuid := 'aecQ23AYDTTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblQ23AYDTTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblQ23AYDTTGLNRDHRZABQAQH3XQ4', 'U','tblQ23AYDTTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atuR7FXMYDVVHWDBROXAAIT4AGD7E
after update of ACCESSIBILITY, ADDRESS, CHECKCLIENTCERT, CIPHERSUITES, CLIENTCERTALIASES, CLIENTKEYALIASES, EASKRBAUTH, SECURITYPROTOCOL, SERVERCERTALIASES, SERVERKEYALIASES on RDX_SAP
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblR7FXMYDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colKAQHY523RHWDBRCXAAIT4AGD7E',:old.ADDRESS,:new.ADDRESS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colREBZTTQ3J3NRDJIRACQMTAIZT4',:old.SECURITYPROTOCOL,:new.SECURITYPROTOCOL);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colZUEE6WCMK7NBDJA5ACQMTAIZT4',:old.CHECKCLIENTCERT,:new.CHECKCLIENTCERT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colMVGATV2VDJDSDLAPNPSZORVJZA',:old.EASKRBAUTH,:new.EASKRBAUTH);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colUPLKI3MFVRHV5LOVX56JEFNYTY',:old.ACCESSIBILITY,:new.ACCESSIBILITY);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colIHQTWYMFTJC5VEZAM3WWYLHZK4',:old.SERVERKEYALIASES,:new.SERVERKEYALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6F5NIDGUUNDYNLVMFKURFV4QOY',:old.SERVERCERTALIASES,:new.SERVERCERTALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colFT5F6TB3TRFDHHAFWSW7TIYZRA',:old.CLIENTKEYALIASES,:new.CLIENTKEYALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6X5HPPFXLJGOBCWX5CU6VISYTU',:old.CLIENTCERTALIASES,:new.CLIENTCERTALIASES);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colJ4MTAXJHPZHJZK6TW6NHKZY5MM',:old.CIPHERSUITES,:new.CIPHERSUITES);
   end if;

   vClassGuid := 'aecR7FXMYDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblR7FXMYDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblR7FXMYDVVHWDBROXAAIT4AGD7E', 'U','tblR7FXMYDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atuS4NVQKVQI5HLTG33G43UK5QMXU
after update of TITLE on RDX_TST_AUDITDETAIL
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.PKCOLINT||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLCHAR)||'~'||Replace(:old.PKCOLNUM, ',', '.')||'~'||to_char(:old.PKCOLDATE,'yyyy-mm-dd hh24:mi:ss')||'~'||RDX_ENTITY.PackPIDStr(:old.PKCOLSTR)||'~'||to_char(:old.PKCOLTIMESTAMP,'yyyy-mm-dd hh24:mi:ss.FF6');
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colB7LCJXCMXVFF7PZ7UH3YBVJXOQ',:old.TITLE,:new.TITLE);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_TST_AUDITMASTER M
       where M.PKCOLINT=:old.PKCOLINT and M.PKCOLCHAR=:old.PKCOLCHAR and M.PKCOLNUM=:old.PKCOLNUM and M.PKCOLDATE=:old.PKCOLDATE and M.PKCOLSTR=:old.PKCOLSTR and M.PKCOLTIMESTAMP=:old.PKCOLTIMESTAMP;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblS4NVQKVQI5HLTG33G43UK5QMXU', 'U','tbl4UBCGVD5ZZF6ZIJN4J3YISPFZA');
end;
/

create or replace trigger atuSY4KIOLTGLNRDHRZABQAQH3XQ4
after update of ADMINGROUPNAME, CHECKSTATION, EMAIL, LASTPWDCHANGETIME, LOCKED, MOBILEPHONE, MUSTCHANGEPWD, PERSONNAME, PWDEXPIRATIONPERIOD on RDX_AC_USER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblSY4KIOLTGLNRDHRZABQAQH3XQ4'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.NAME);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colQ62TN7IYY3NBDGMCABQAQH3XQ4',:old.LOCKED,:new.LOCKED);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col3FYUIAY5J3NRDJIRACQMTAIZT4',:old.PERSONNAME,:new.PERSONNAME);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colNMVAZDYYY3NBDGMCABQAQH3XQ4',:old.ADMINGROUPNAME,:new.ADMINGROUPNAME);
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colPUPBTVQYY3NBDGMCABQAQH3XQ4',:old.LASTPWDCHANGETIME,:new.LASTPWDCHANGETIME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col5XNCT3QYY3NBDGMCABQAQH3XQ4',:old.PWDEXPIRATIONPERIOD,:new.PWDEXPIRATIONPERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colRC2TN7IYY3NBDGMCABQAQH3XQ4',:old.CHECKSTATION,:new.CHECKSTATION);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colWXQAHXB5V3OBDCLVAALOMT5GDM',:old.EMAIL,:new.EMAIL);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colW3QAHXB5V3OBDCLVAALOMT5GDM',:old.MOBILEPHONE,:new.MOBILEPHONE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colANJQ6ZDT6FALRCPTTSR3UUR5VQ',:old.MUSTCHANGEPWD,:new.MUSTCHANGEPWD);
   end if;

   vClassGuid := 'aecSY4KIOLTGLNRDHRZABQAQH3XQ4';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblSY4KIOLTGLNRDHRZABQAQH3XQ4',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblSY4KIOLTGLNRDHRZABQAQH3XQ4', 'U','tblSY4KIOLTGLNRDHRZABQAQH3XQ4');
end;
/

create or replace trigger atuUEE3YXA72HNRDB7BAALOMT5GDM
after update of SAPID on RDX_NETPORTHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colUVIFSVD4RHWDBRCXAAIT4AGD7E',:old.SAPID,:new.SAPID);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblUEE3YXA72HNRDB7BAALOMT5GDM', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atuWZB7K4HLJPOBDCIUAALOMT5GDM
after update of CLASSGUID, EXPIREDPOLICY, ISACTIVE, PRIORITY, PRIORITYBOOSTINGSPEED, SCHEDULEID on RDX_JS_TASK
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblWZB7K4HLJPOBDCIUAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colNAPRJRHLJPOBDCIUAALOMT5GDM',:old.SCHEDULEID,:new.SCHEDULEID);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colFDCRWKPMJPOBDCIUAALOMT5GDM',:old.CLASSGUID,:new.CLASSGUID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col7JDCGRXMJPOBDCIUAALOMT5GDM',:old.PRIORITY,:new.PRIORITY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col33LE4YHMJPOBDCIUAALOMT5GDM',:old.PRIORITYBOOSTINGSPEED,:new.PRIORITYBOOSTINGSPEED);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colYQWRAN7SK5GA3EGFI6TEPHYWL4',:old.EXPIREDPOLICY,:new.EXPIREDPOLICY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colRMH5P2FSTNABBMBNUIXU3AINEQ',:old.ISACTIVE,:new.ISACTIVE);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblWZB7K4HLJPOBDCIUAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblWZB7K4HLJPOBDCIUAALOMT5GDM', 'U','tblWZB7K4HLJPOBDCIUAALOMT5GDM');
end;
/

create or replace trigger atuX5TD7JDVVHWDBROXAAIT4AGD7E
after update of ARTECOUNTRY, ARTEDBTRACEPROFILE, ARTEFILETRACEPROFILE, ARTEGUITRACEPROFILE, ARTELANGUAGE, ASKUSERPWDAFTERINACTIVITY, AUDITSTOREPERIOD1, AUDITSTOREPERIOD2, AUDITSTOREPERIOD3, AUDITSTOREPERIOD4, AUDITSTOREPERIOD5, BLOCKUSERINVALIDLOGONCNT, BLOCKUSERINVALIDLOGONMINS, DEFAULTAUDITSCHEMEID, EASKRBPRINCNAME, EASSESSIONACTIVITYMINS, EASSESSIONINACTIVITYMINS, ENABLESENSITIVETRACE, EVENTSTOREDAYS, NAME, PCMSTOREDAYS, PROFILELOGSTOREDAYS, PWDMINLEN, PWDMUSTCONTAINACHARS, PWDMUSTCONTAINNCHARS, PWDMUSTDIFFERFROMNAME, UNIQUEPWDSEQLEN on RDX_SYSTEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID=RDX_AUDIT.DEFAULTAUDITSCHEME
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblX5TD7JDVVHWDBROXAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'colTCOQ4HOZRHWDBRCXAAIT4AGD7E',:old.NAME,:new.NAME);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col546FXWYZJ3NRDJIRACQMTAIZT4',:old.ARTELANGUAGE,:new.ARTELANGUAGE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colWGL2VHS7RNBIRA3NYYV3KGAIPI',:old.ARTECOUNTRY,:new.ARTECOUNTRY);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colMJDA72X3RRHX5AQ4RSCK3YNA44',:old.ARTEGUITRACEPROFILE,:new.ARTEGUITRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colMYF3QYYJ7BFS7AJ6YRMZUFBE4M',:old.ARTEFILETRACEPROFILE,:new.ARTEFILETRACEPROFILE);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colXLSJR3OUKLNRDAQSABIFNQAAAE',:old.ARTEDBTRACEPROFILE,:new.ARTEDBTRACEPROFILE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colM6ABGYQYNVGYRC6NTJ76DIBZ4I',:old.EASSESSIONACTIVITYMINS,:new.EASSESSIONACTIVITYMINS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colLPOXOGA2J3NRDJIRACQMTAIZT4',:old.EASSESSIONINACTIVITYMINS,:new.EASSESSIONINACTIVITYMINS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colM6KLR5HJ3JDI7DTS5S52SRQEFU',:old.EASKRBPRINCNAME,:new.EASKRBPRINCNAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI',:old.ASKUSERPWDAFTERINACTIVITY,:new.ASKUSERPWDAFTERINACTIVITY);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colCK5BEU6WO5H4VKRDA46ALDEZ3M',:old.UNIQUEPWDSEQLEN,:new.UNIQUEPWDSEQLEN);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col2J4RCA57UBFPFNVYLNMEXJI5CU',:old.PWDMINLEN,:new.PWDMINLEN);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colSLXTNR7MBRF23O655OVBOAL5LA',:old.PWDMUSTDIFFERFROMNAME,:new.PWDMUSTDIFFERFROMNAME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTY6XN5X66FD2FIC7ZQVWKRV7YQ',:old.PWDMUSTCONTAINACHARS,:new.PWDMUSTCONTAINACHARS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colOJXZPIRBDZG6HCJZ4F2JDH7UFI',:old.PWDMUSTCONTAINNCHARS,:new.PWDMUSTCONTAINNCHARS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colUMLKGQCFD6VDBNKJAAUMFADAIA',:old.EVENTSTOREDAYS,:new.EVENTSTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colIH2FRGR6EKWDRWGPAAUUN6FMUG',:old.PCMSTOREDAYS,:new.PCMSTOREDAYS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colRJ6YTFHGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD1,:new.AUDITSTOREPERIOD1);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3J6FBUPGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD2,:new.AUDITSTOREPERIOD2);
            RDX_AUDIT.addChangedValueInt(vChangeData,'col3N6FBUPGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD3,:new.AUDITSTOREPERIOD3);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTJMVJWXGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD4,:new.AUDITSTOREPERIOD4);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colTNMVJWXGWPNBDPLGABQJO5ADDQ',:old.AUDITSTOREPERIOD5,:new.AUDITSTOREPERIOD5);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colEKZMW4XGWPNBDPLGABQJO5ADDQ',:old.DEFAULTAUDITSCHEMEID,:new.DEFAULTAUDITSCHEMEID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colDCLAU6NMGVDSNKN7ZPB6SQJE5Y',:old.BLOCKUSERINVALIDLOGONCNT,:new.BLOCKUSERINVALIDLOGONCNT);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colQTEXFSBLXJES3AOB6SXXKNGQQE',:old.BLOCKUSERINVALIDLOGONMINS,:new.BLOCKUSERINVALIDLOGONMINS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colLRLGIOF3DVB2XEFQXLCJLPUYTU',:old.ENABLESENSITIVETRACE,:new.ENABLESENSITIVETRACE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKDV6X7RAL5DCBO2HENGADFNFTY',:old.PROFILELOGSTOREDAYS,:new.PROFILELOGSTOREDAYS);
   end if;

   vClassGuid := 'aecX5TD7JDVVHWDBROXAAIT4AGD7E';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblX5TD7JDVVHWDBROXAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblX5TD7JDVVHWDBROXAAIT4AGD7E', 'U','tblX5TD7JDVVHWDBROXAAIT4AGD7E');
end;
/

create or replace trigger atuXI6CAOIGDBAGJEE6JQEGJME43Q
after update of ADMINROLEGUIDS, CLERKROLEGUIDS, PROCESSSTOREDAYS on RDX_WF_PROCESSTYPE
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblXI6CAOIGDBAGJEE6JQEGJME43Q'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=RDX_ENTITY.PackPIDStr(:old.GUID);
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6SKMJNSZZNFVDNGCP26PG6QPEM',:old.CLERKROLEGUIDS,:new.CLERKROLEGUIDS);
            RDX_AUDIT.addChangedValueStr(vChangeData,'col6KIGNQEL7NEWFDQMOEIO4POSTQ',:old.ADMINROLEGUIDS,:new.ADMINROLEGUIDS);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colNWJ6DLDISBBWZKPVZUMMGLM5LI',:old.PROCESSSTOREDAYS,:new.PROCESSSTOREDAYS);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblXI6CAOIGDBAGJEE6JQEGJME43Q',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXI6CAOIGDBAGJEE6JQEGJME43Q', 'U','tblXI6CAOIGDBAGJEE6JQEGJME43Q');
end;
/

create or replace trigger atuXOKR7CSUNFG3DCVQGB5LWIWIIU
after update of HIGHARTEINSTCOUNT on RDX_ARTEUNIT
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colJP6YRRR6BVD73IIDYKAYED5HMQ',:old.HIGHARTEINSTCOUNT,:new.HIGHARTEINSTCOUNT);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblXOKR7CSUNFG3DCVQGB5LWIWIIU', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

create or replace trigger atuYDGQACJVVLOBDCLSAALOMT5GDM
after update of BODYREGEXP, SUBJECTREGEXP on RDX_PC_CHANNELHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblYDGQACJVVLOBDCLSAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.UNITID||'~'||:old.SEQ;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueStr(vChangeData,'col2M2OGMZVVLOBDCLSAALOMT5GDM',:old.SUBJECTREGEXP,:new.SUBJECTREGEXP);
            RDX_AUDIT.addChangedValueStr(vChangeData,'colKQYXQWRVVLOBDCLSAALOMT5GDM',:old.BODYREGEXP,:new.BODYREGEXP);
   end if;

   vClassGuid := :old.CLASSGUID;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblYDGQACJVVLOBDCLSAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblYDGQACJVVLOBDCLSAALOMT5GDM', 'U','tblYDGQACJVVLOBDCLSAALOMT5GDM');
end;
/

create or replace trigger atuZ3SDCSSEI3OBDCIOAALOMT5GDM
after update of CALENDARID, ENDTIME, EVENTTIME, PERIOD, REPEATABLE, STARTTIME on RDX_JS_EVENTSCHDITEM
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZ3SDCSSEI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.SCHEDULEID||'~'||:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'col2Y4Z3HSRI3OBDCIOAALOMT5GDM',:old.CALENDARID,:new.CALENDARID);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colIINJLJ2RI3OBDCIOAALOMT5GDM',:old.REPEATABLE,:new.REPEATABLE);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colVZL6POCRI3OBDCIOAALOMT5GDM',:old.STARTTIME,:new.STARTTIME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colQ2O2LSKRI3OBDCIOAALOMT5GDM',:old.ENDTIME,:new.ENDTIME);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colR3BKZWCRI3OBDCIOAALOMT5GDM',:old.PERIOD,:new.PERIOD);
            RDX_AUDIT.addChangedValueInt(vChangeData,'colKDDQ37CRI3OBDCIOAALOMT5GDM',:old.EVENTTIME,:new.EVENTTIME);
   end if;

   vClassGuid := 'aecZ3SDCSSEI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZ3SDCSSEI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZ3SDCSSEI3OBDCIOAALOMT5GDM', 'U','tblZ3SDCSSEI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atuZBS5MS2BI3OBDCIOAALOMT5GDM
after update of LASTUPDATETIME on RDX_JS_INTERVALSCHD
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tblZBS5MS2BI3OBDCIOAALOMT5GDM'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueDateTime(vChangeData,'colKCIYPFSBI3OBDCIOAALOMT5GDM',:old.LASTUPDATETIME,:new.LASTUPDATETIME);
   end if;

   vClassGuid := 'aecZBS5MS2BI3OBDCIOAALOMT5GDM';
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tblZBS5MS2BI3OBDCIOAALOMT5GDM',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZBS5MS2BI3OBDCIOAALOMT5GDM', 'U','tblZBS5MS2BI3OBDCIOAALOMT5GDM');
end;
/

create or replace trigger atuZUTGVH5TKRFWZEUGZHC5KRIOFA
after update of SAPID on RDX_JMSHANDLER
for each row
disable
declare vStoreDuration integer; vSaveData integer; vPID varchar2(4000); vChangeData clob; vSchemeId varchar2(50); vClassGuid varchar2(50);
begin
   begin
      select DEFAULTAUDITSCHEMEID into vSchemeId from RDX_SYSTEM where ID=1;
      select RDX_AU_SCHEMEITEM.STOREDURATION, RDX_AU_SCHEMEITEM.SAVEDATA into vStoreDuration, vSaveData
      from RDX_AU_SCHEMEITEM
      where RDX_AU_SCHEMEITEM.SCHEMEGUID = vSchemeId
         and RDX_AU_SCHEMEITEM.TABLEID = 'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E'
         and RDX_AU_SCHEMEITEM.EVENTTYPE = 'U';
   exception
      when no_data_found then return;
   end;
   vPID:=:old.ID;
   if vSaveData=1 then
            RDX_AUDIT.addChangedValueInt(vChangeData,'colFMGGFQ6MORC67LQVLCPFLZHOJM',:old.SAPID,:new.SAPID);
   end if;

   begin
       select CLASSGUID into vClassGuid from RDX_UNIT M
       where M.ID=:old.ID;
   exception
      when no_data_found then null;
   end;
   insert into RDX_AU_AUDITLOG (ID,EVENTTIME,STOREDURATION,USERNAME,STATIONNAME,TABLEID,CLASSID,PID,EVENTTYPE,EVENTDATA)
   values(SQN_RDX_AU_AUDITLOGID.NextVal,SYSTIMESTAMP,vStoreDuration,RDX_Arte.getUserName,RDX_Arte.getStationName,'tbl5HP4XTP3EGWDBRCRAAIT4AGD7E',vClassGuid,vPID,'U',vChangeData);
end;
/

begin
   RDX_AUDIT.updateTableAuditStateEventType('tblZUTGVH5TKRFWZEUGZHC5KRIOFA', 'U','tbl5HP4XTP3EGWDBRCRAAIT4AGD7E');
end;
/

begin
	RDX_ACS.AcsUtilsBuild();
end;
/

audit alter on RDX_EVENTLOG
/
audit update, delete on  RDX_EVENTLOG
/