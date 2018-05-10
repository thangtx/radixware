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

