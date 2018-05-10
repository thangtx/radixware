create or replace package RDX_Entity as

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

	function createQualifiedPid(
		tableGuid in varchar2,
		objPid in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (createQualifiedPid, RNDS);
	PRAGMA RESTRICT_REFERENCES (createQualifiedPid, RNPS);
	PRAGMA RESTRICT_REFERENCES (createQualifiedPid, WNDS);
	PRAGMA RESTRICT_REFERENCES (createQualifiedPid, WNPS);

	function packPidStr(
		s in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (packPidStr, RNDS);
	PRAGMA RESTRICT_REFERENCES (packPidStr, RNPS);
	PRAGMA RESTRICT_REFERENCES (packPidStr, WNDS);
	PRAGMA RESTRICT_REFERENCES (packPidStr, WNPS);

	function unpackPidStr(
		s in varchar2
	) return varchar2 deterministic;
	PRAGMA RESTRICT_REFERENCES (unpackPidStr, RNDS);
	PRAGMA RESTRICT_REFERENCES (unpackPidStr, RNPS);
	PRAGMA RESTRICT_REFERENCES (unpackPidStr, WNDS);
	PRAGMA RESTRICT_REFERENCES (unpackPidStr, WNPS);

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

	procedure userPropOnDelValue(
		pEntityId in varchar2,
		pValuePid in varchar2
	);
end;
/

grant execute on RDX_Entity to &USER&_RUN_ROLE
/

create or replace package body RDX_Entity as

	function createPid(
		field in number
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.createPid(To_Char(field));
	end;

	function createPid(
		field in varchar2
	) return varchar2 deterministic
	is
	begin
	 return RDX_Entity.packPidStr(field);
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

	function createQualifiedPid(
		tableGuid in varchar2,
		objPid in varchar2
	) return varchar2 deterministic
	is
	begin
	   return  tableGuid || chr(10) || objPid;
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

	function unpackPidStr(
		s in varchar2
	) return varchar2 deterministic
	is
	  res varchar2(32767);
	begin
	  res := Replace(s,   CHR(92) || CHR(92),  CHR(92));   -- '\\' => '\'
	  res := Replace(res, CHR(92) || CHR(126), CHR(126)); -- '\~' => '~'
	  res := Replace(res, CHR(92) || 'r',  CHR(13));      -- '\\r' => '\r'
	  res := Replace(res, CHR(92) || 'n',  CHR(10));      -- '\\n' => '\n'
	  res := Replace(res, CHR(92) || 't',  CHR(9));      -- '\\t' => '\t'
	  res := Replace(res, CHR(92) || ' ',      ' ');     -- '\ '  => ' '
	  return res;
	end;

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

