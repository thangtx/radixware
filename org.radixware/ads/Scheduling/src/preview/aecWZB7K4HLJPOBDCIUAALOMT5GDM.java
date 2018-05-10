
/* Radix::Scheduling::Task - Server Executable*/

/*Radix::Scheduling::Task-Entity Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task")
public abstract published class Task  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task_mi.rdxMeta;}

	/*Radix::Scheduling::Task:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task:Properties-Properties*/

	/*Radix::Scheduling::Task:priorityBoostingSpeed-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priorityBoostingSpeed")
	public published  Int getPriorityBoostingSpeed() {
		return priorityBoostingSpeed;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priorityBoostingSpeed")
	public published   void setPriorityBoostingSpeed(Int val) {
		priorityBoostingSpeed = val;
	}

	/*Radix::Scheduling::Task:schedule-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:schedule")
	public published  org.radixware.ads.Scheduling.server.EventSchedule getSchedule() {
		return schedule;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:schedule")
	public published   void setSchedule(org.radixware.ads.Scheduling.server.EventSchedule val) {
		schedule = val;
	}

	/*Radix::Scheduling::Task:priority-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priority")
	public published  org.radixware.kernel.common.enums.EPriority getPriority() {
		return priority;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priority")
	public published   void setPriority(org.radixware.kernel.common.enums.EPriority val) {
		priority = val;
	}

	/*Radix::Scheduling::Task:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Scheduling::Task:lastExecTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastExecTime")
	public published  java.sql.Timestamp getLastExecTime() {
		return lastExecTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastExecTime")
	public published   void setLastExecTime(java.sql.Timestamp val) {
		lastExecTime = val;
	}

	/*Radix::Scheduling::Task:unit-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:unit")
	public published  org.radixware.ads.System.server.Unit getUnit() {
		return unit;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:unit")
	public published   void setUnit(org.radixware.ads.System.server.Unit val) {
		unit = val;
	}

	/*Radix::Scheduling::Task:scheduleId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scheduleId")
	public published  Int getScheduleId() {
		return scheduleId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scheduleId")
	public published   void setScheduleId(Int val) {
		scheduleId = val;
	}

	/*Radix::Scheduling::Task:unitId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:unitId")
	public published  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:unitId")
	public published   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::Scheduling::Task:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Scheduling::Task:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Scheduling::Task:expiredPolicy-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expiredPolicy")
	public published  org.radixware.kernel.common.enums.EExpiredJobExecPolicy getExpiredPolicy() {
		return expiredPolicy;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expiredPolicy")
	public published   void setExpiredPolicy(org.radixware.kernel.common.enums.EExpiredJobExecPolicy val) {
		expiredPolicy = val;
	}

	/*Radix::Scheduling::Task:isActive-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isActive")
	public published  Bool getIsActive() {
		return isActive;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isActive")
	public published   void setIsActive(Bool val) {
		isActive = val;
	}

	/*Radix::Scheduling::Task:prevExecTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:prevExecTime")
	public published  java.sql.Timestamp getPrevExecTime() {
		return prevExecTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:prevExecTime")
	public published   void setPrevExecTime(java.sql.Timestamp val) {
		prevExecTime = val;
	}

	/*Radix::Scheduling::Task:status-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:status")
	public published  org.radixware.kernel.common.enums.ETaskStatus getStatus() {
		return status;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:status")
	public published   void setStatus(org.radixware.kernel.common.enums.ETaskStatus val) {
		status = val;
	}

	/*Radix::Scheduling::Task:faultMess-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:faultMess")
	public published  java.sql.Clob getFaultMess() {
		return faultMess;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:faultMess")
	public published   void setFaultMess(java.sql.Clob val) {
		faultMess = val;
	}

	/*Radix::Scheduling::Task:finishExecTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishExecTime")
	public published  java.sql.Timestamp getFinishExecTime() {
		return finishExecTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishExecTime")
	public published   void setFinishExecTime(java.sql.Timestamp val) {
		finishExecTime = val;
	}

	/*Radix::Scheduling::Task:startExecTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:startExecTime")
	public published  java.sql.Timestamp getStartExecTime() {
		return startExecTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:startExecTime")
	public published   void setStartExecTime(java.sql.Timestamp val) {
		startExecTime = val;
	}

	/*Radix::Scheduling::Task:execResults-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execResults")
	public published  java.sql.Clob getExecResults() {
		return execResults;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execResults")
	public published   void setExecResults(java.sql.Clob val) {
		execResults = val;
	}

	/*Radix::Scheduling::Task:parentId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentId")
	public published  Int getParentId() {
		return parentId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentId")
	public published   void setParentId(Int val) {
		parentId = val;
	}

	/*Radix::Scheduling::Task:seq-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seq")
	public published  Int getSeq() {
		return seq;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seq")
	public published   void setSeq(Int val) {
		seq = val;
	}

	/*Radix::Scheduling::Task:checkExecCondition-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:checkExecCondition")
	public published  org.radixware.ads.Scheduling.server.UserFunc.TaskExecCondition getCheckExecCondition() {
		return checkExecCondition;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:checkExecCondition")
	public published   void setCheckExecCondition(org.radixware.ads.Scheduling.server.UserFunc.TaskExecCondition val) {
		checkExecCondition = val;
	}

	/*Radix::Scheduling::Task:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:classTitle")
	public published  Str getClassTitle() {

		if (internal[classTitle] == null) {
		    internal[classTitle] = Meta::Utils.getDefinitionTitle(classGuid);
		}
		return internal[classTitle];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:classTitle")
	public published   void setClassTitle(Str val) {
		classTitle = val;
	}

	/*Radix::Scheduling::Task:resultMap-Dynamic Property*/



	protected java.util.Map<Str,Str> resultMap=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:resultMap")
	public published  java.util.Map<Str,Str> getResultMap() {

		if(internal[resultMap] == null) {
		    loadResultsMap();
		}
		return internal[resultMap];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:resultMap")
	public published   void setResultMap(java.util.Map<Str,Str> val) {
		resultMap = val;
	}

	/*Radix::Scheduling::Task:parentTask-Dynamic Property*/



	protected org.radixware.ads.Scheduling.server.Task.AGroup parentTask=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentTask")
	public published  org.radixware.ads.Scheduling.server.Task.AGroup getParentTask() {

		return directoryId != null ? directory : (Task.AGroup) parentTaskInternal;
	}

	/*Radix::Scheduling::Task:hasChildren-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasChildren")
	public published  Int getHasChildren() {
		return hasChildren;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasChildren")
	public published   void setHasChildren(Int val) {
		hasChildren = val;
	}

	/*Radix::Scheduling::Task:executionMode-Dynamic Property*/



	protected org.radixware.ads.Scheduling.common.TaskExecutionMode executionMode=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:executionMode")
	public published  org.radixware.ads.Scheduling.common.TaskExecutionMode getExecutionMode() {

		if (parentId != null) {
		    return TaskExecutionMode:Nested;
		}
		if (unitId != null) {
		    return TaskExecutionMode:Schedule;
		}
		return TaskExecutionMode:Manual;
	}

	/*Radix::Scheduling::Task:execToken-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execToken")
	public published  Int getExecToken() {
		return execToken;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execToken")
	public published   void setExecToken(Int val) {
		execToken = val;
	}

	/*Radix::Scheduling::Task:finishedChildsCount-Column-Based Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishedChildsCount")
	@Deprecated
	public published  Int getFinishedChildsCount() {
		return finishedChildsCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishedChildsCount")
	@Deprecated
	public published   void setFinishedChildsCount(Int val) {
		finishedChildsCount = val;
	}

	/*Radix::Scheduling::Task:seqTitle-Dynamic Property*/



	protected Str seqTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seqTitle")
	public published  Str getSeqTitle() {

		if(seq == null || seq.longValue() <= 0) {
		    return "";
		}
		return Int.toString(seq.longValue());
	}

	/*Radix::Scheduling::Task:debugTitle-Dynamic Property*/



	protected Str debugTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:debugTitle")
	public published  Str getDebugTitle() {

		return "#" + id + " '" + title + "'";
	}

	/*Radix::Scheduling::Task:lastDurationTime-Dynamic Property*/



	protected Str lastDurationTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastDurationTime")
	public published  Str getLastDurationTime() {

		if (startExecTime == null) 
		    return null;

		if (status == TaskStatus:Cancelled) 
		    return "Cancelled";

		if (finishExecTime == null) {
		    if (status == TaskStatus:Executing) 
		        return "Still in progress...";
		    else
		        return null;
		}

		return Utils::Timing.millisToDurationString(finishExecTime.Time - startExecTime.Time);

	}

	/*Radix::Scheduling::Task:loadedResultsMapStr-Dynamic Property*/



	protected Str loadedResultsMapStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:loadedResultsMapStr")
	private final  Str getLoadedResultsMapStr() {
		return loadedResultsMapStr;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:loadedResultsMapStr")
	private final   void setLoadedResultsMapStr(Str val) {
		loadedResultsMapStr = val;
	}

	/*Radix::Scheduling::Task:execRoleGuids-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execRoleGuids")
	public published  org.radixware.kernel.common.types.ArrStr getExecRoleGuids() {
		return execRoleGuids;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execRoleGuids")
	public published   void setExecRoleGuids(org.radixware.kernel.common.types.ArrStr val) {
		execRoleGuids = val;
	}

	/*Radix::Scheduling::Task:systemId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:systemId")
	public  Int getSystemId() {
		return systemId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:systemId")
	public   void setSystemId(Int val) {
		systemId = val;
	}

	/*Radix::Scheduling::Task:scpName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scpName")
	public  Str getScpName() {
		return scpName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scpName")
	public   void setScpName(Str val) {
		scpName = val;
	}

	/*Radix::Scheduling::Task:scp-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scp")
	public published  org.radixware.ads.System.server.Scp getScp() {
		return scp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scp")
	public published   void setScp(org.radixware.ads.System.server.Scp val) {
		scp = val;
	}

	/*Radix::Scheduling::Task:skipIfExecuting-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:skipIfExecuting")
	public published  Bool getSkipIfExecuting() {
		return skipIfExecuting;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:skipIfExecuting")
	public published   void setSkipIfExecuting(Bool val) {
		skipIfExecuting = val;
	}

	/*Radix::Scheduling::Task:timeSinceLastExec-Dynamic Property*/



	protected Str timeSinceLastExec=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:timeSinceLastExec")
	public published  Str getTimeSinceLastExec() {

		if (startExecTime != null) {
		    if (finishExecTime == null && (status == TaskStatus:Executing || status == TaskStatus:Cancelling)) {
		        return String.format("<being executed for %s>", Utils::Timing.millisToDurationString(System.currentTimeMillis() - startExecTime.Time).replace("< ", ""));
		    } else {
		        return Utils::Timing.millisToDurationString(System.currentTimeMillis() - startExecTime.Time);
		    }
		}
		return "<unknown>";

	}

	/*Radix::Scheduling::Task:isActualizableByRelatedJobs-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isActualizableByRelatedJobs")
	public published  Bool getIsActualizableByRelatedJobs() {
		return isActualizableByRelatedJobs;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isActualizableByRelatedJobs")
	public published   void setIsActualizableByRelatedJobs(Bool val) {
		isActualizableByRelatedJobs = val;
	}

	/*Radix::Scheduling::Task:expectedDurationSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expectedDurationSec")
	public  Int getExpectedDurationSec() {
		return expectedDurationSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expectedDurationSec")
	public   void setExpectedDurationSec(Int val) {
		expectedDurationSec = val;
	}

	/*Radix::Scheduling::Task:createTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:createTime")
	public published  java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:createTime")
	public published   void setCreateTime(java.sql.Timestamp val) {
		createTime = val;
	}

	/*Radix::Scheduling::Task:scheduleTitle-Dynamic Property*/



	protected Str scheduleTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scheduleTitle")
	public published  Str getScheduleTitle() {

		if (executionMode == TaskExecutionMode:Schedule) {
		    return schedule == null ? "<not defined>" : schedule.calcTitle();
		}
		return "<not used>";
	}

	/*Radix::Scheduling::Task:parentTaskInternal-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentTaskInternal")
	private final  org.radixware.ads.Scheduling.server.Task getParentTaskInternal() {
		return parentTaskInternal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentTaskInternal")
	private final   void setParentTaskInternal(org.radixware.ads.Scheduling.server.Task val) {
		parentTaskInternal = val;
	}

	/*Radix::Scheduling::Task:selfCheckTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:selfCheckTime")
	public published  java.sql.Timestamp getSelfCheckTime() {
		return selfCheckTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:selfCheckTime")
	public published   void setSelfCheckTime(java.sql.Timestamp val) {
		selfCheckTime = val;
	}

	/*Radix::Scheduling::Task:rid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:rid")
	public published  Str getRid() {
		return rid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:rid")
	public published   void setRid(Str val) {
		rid = val;
	}

	/*Radix::Scheduling::Task:aadcMemberId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:aadcMemberId")
	public published  Int getAadcMemberId() {
		return aadcMemberId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:aadcMemberId")
	public published   void setAadcMemberId(Int val) {
		aadcMemberId = val;
	}

	/*Radix::Scheduling::Task:isAadcSysMember-Dynamic Property*/



	protected Bool isAadcSysMember=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isAadcSysMember")
	private final  Bool getIsAadcSysMember() {

		return System::System.thisSystem.aadcMemberId != null;

	}

	/*Radix::Scheduling::Task:directoryId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directoryId")
	public published  Int getDirectoryId() {
		return directoryId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directoryId")
	public published   void setDirectoryId(Int val) {
		directoryId = val;
	}

	/*Radix::Scheduling::Task:directory-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directory")
	public  org.radixware.ads.Scheduling.server.Task.Dir getDirectory() {
		return directory;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directory")
	public   void setDirectory(org.radixware.ads.Scheduling.server.Task.Dir val) {
		directory = val;
	}

	/*Radix::Scheduling::Task:pathInTree-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr pathInTree=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:pathInTree")
	public  org.radixware.kernel.common.types.ArrStr getPathInTree() {

		ArrStr pathInTree = new ArrStr();
		pathInTree.add(getEntityDefinitionId().toString() + "\n" + getPid());

		Int dirId = directoryId;
		Int parentId = parentId;
		while (dirId != null || parentId != null) {
		    final Int containerItemId = dirId != null ? dirId : parentId;
		    final Task owner = Task.loadByPK(containerItemId, true);
		    pathInTree.add(owner.getEntityDefinitionId().toString() + "\n" + owner.getPid());
		    dirId = owner.directoryId;
		    parentId = owner.parentId;
		}

		return pathInTree;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:pathInTree")
	public   void setPathInTree(org.radixware.kernel.common.types.ArrStr val) {
		pathInTree = val;
	}

	/*Radix::Scheduling::Task:canBeMoved-Dynamic Property*/



	protected Bool canBeMoved=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:canBeMoved")
	public  Bool getCanBeMoved() {

		return parentId != null || directoryId != null;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:canBeMoved")
	public   void setCanBeMoved(Bool val) {
		canBeMoved = val;
	}

	/*Radix::Scheduling::Task:hasChildrenOnlyDirs-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasChildrenOnlyDirs")
	  Int getHasChildrenOnlyDirs() {
		return hasChildrenOnlyDirs;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasChildrenOnlyDirs")
	   void setHasChildrenOnlyDirs(Int val) {
		hasChildrenOnlyDirs = val;
	}





















































































































































































































































































































	/*Radix::Scheduling::Task:Methods-Methods*/

	/*Radix::Scheduling::Task:beforeExecute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:beforeExecute")
	public final published  boolean beforeExecute (Int execToken) {
		Arte::Trace.enterContext(Arte::EventContextType:Task, id);
		try {
		    lockTask();

		//System.out.println("Before execute for task " + );

		    if (status != TaskStatus:Scheduled && status != TaskStatus:Cancelling) {
		//    System.out.println("task " +  + " is already running, throw exception");
		//    .(, );
		        throw new IllegalStateException(String.format("Task '%s' is in the wrong state at execution start: %s", debugTitle, status == null ? "<null>" : status.Name));
		    }

		    Arte::Trace.put(eventCode["Prepare for execution of task '%1'"], debugTitle);

		    execToken = execToken;
		    startExecTime = Arte::Arte.getDatabaseSysDate();
		    finishExecTime = null;

		    if (status == TaskStatus:Cancelling) {
		        return false;
		    }

		    boolean allowedToExecute = true;
		    if (checkExecCondition != null) {
		        try {
		            if (!checkExecCondition.check(this)) {
		                allowedToExecute = false;
		            }
		        } catch (Exceptions::Throwable t) {
		            Arte::Trace.put(eventCode["Task '%1' was skipped due to exception in precondition check: '%2'"], debugTitle, Arte::Trace.exceptionStackToString(t));
		            allowedToExecute = false;
		        }
		    }
		    if (allowedToExecute) {
		        try {
		            prepareForExecution();
		            return true;
		        } catch (Exceptions::Throwable t) {
		            Arte::Trace.put(eventCode["Task '%1' was skipped due to exception in execution preparation check: '%2'"], debugTitle, Arte::Trace.exceptionStackToString(t));
		        }
		    }
		    return false;

		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:Task, id);
		}
	}

	/*Radix::Scheduling::Task:execute-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execute")
	public static published  void execute (Int taskId, java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		//Task execution and other task management operations are now processed by TaskManager.
		//this code is left here for backward compatibility and can be used only to launch root tasks
		TaskManager.execute(taskId, Arte::Arte.getInstance().JobQueue.CurrentJobId, prevExecTime, curExecTime);
	}

	/*Radix::Scheduling::Task:execute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execute")
	public abstract published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime);

	/*Radix::Scheduling::Task:refineStatusOnComplete-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:refineStatusOnComplete")
	public published  void refineStatusOnComplete () {
		//do nothing by default
	}

	/*Radix::Scheduling::Task:afterExecute-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:afterExecute")
	public final published  void afterExecute (boolean wasExecuted, org.radixware.kernel.common.enums.ETaskStatus breakStatus, java.lang.Throwable executionException, Int execToken) {
		Arte::Trace.enterContext(Arte::EventContextType:Task, id);
		try {
		    lockTask();

		    if (this.execToken != execToken) {
		        return;
		    }

		    if (status == TaskStatus:Cancelling) {
		        status = TaskStatus:Cancelled;
		        Arte::Trace.put(eventCode["Task '%1' cancelled"], debugTitle);
		        return;
		    }

		    if (status != TaskStatus:Scheduled && status != TaskStatus:Executing) {
		        throw new IllegalStateException(String.format("Task '%s' is in the wrong state: %s", debugTitle, status == null ? "<null>" : status.Name));
		    }

		    finishExecTime = Arte::Arte.getDatabaseSysDate();

		    if (wasExecuted) {
		//    System.out.println();
		        storeResultsMapIfModified();
		        if (executionException != null) {
		            status = TaskStatus:Failed;
		            setFaultMess(Arte::Trace.exceptionStackToString(executionException));
		        } else {
		            if (breakStatus != null) {
		                status = breakStatus;
		            } else {
		                Exceptions::Throwable exceptionOnRefine = null;
		                try {
		                    refineStatusOnComplete();
		                } catch (Exceptions::Throwable t) {
		                    Arte::Trace.put(eventCode["Refining status of task '%1' resulted in thrown exception: %2"], debugTitle, Arte::Trace.exceptionStackToString(t));
		                    exceptionOnRefine = t;
		                }
		                if (exceptionOnRefine != null) {
		                    status = TaskStatus:Failed;
		                    setFaultMess(Arte::Trace.exceptionStackToString(exceptionOnRefine));
		                } else if (status == TaskStatus:Executing) {//execute and refineStatus didn't do anything with status
		                    status = TaskStatus:Success;
		                }
		            }
		        }
		        if (status == TaskStatus:Success) {
		            prevExecTime = lastExecTime;
		        }
		        Arte::Trace.put(eventCode["Task '%1' completed with status '%2'"], debugTitle, status != null ? status.Name : "<null>");
		    } else {
		        status = TaskStatus:Skipped;
		        Arte::Trace.put(eventCode["Task '%1' was skipped"], debugTitle);
		    }
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:Task, id);
		}
	}

	/*Radix::Scheduling::Task:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (isSingletone()) {
		    checkSingletone();
		}
		if (src != null) {
		    status = null;
		    lastExecTime = null;
		    prevExecTime = null;
		    startExecTime = null;
		    finishExecTime = null;
		    execToken = null;
		    faultMess = null;
		    execResults = null;
		    createTime = null;
		    seq = null;
		}
		if (phase == Types::EntityInitializationPhase:TEMPLATE_PREPARATION) {
		    if (parentTask != null)
		        aadcMemberId = parentTask.aadcMemberId;
		}

	}

	/*Radix::Scheduling::Task:setFaultMess-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:setFaultMess")
	public published  void setFaultMess (Str mess) {
		if (faultMess == null) {
		    faultMess = Arte::Arte.createTemporaryClob();
		}
		try {
		    faultMess.setString(1, mess);
		} catch (Exceptions::SQLException ex) {
		    Arte::Trace.put(eventCode["Error creating clob for error message: '%1'. Error message: '%2'"], Arte::Trace.exceptionStackToString(ex), mess);
		}
	}

	/*Radix::Scheduling::Task:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (src != null) {
		    final Task srcTask = (Task) src;
		    if (java.util.Objects.equals(rid, srcTask.rid)) {
		        rid = null;
		    }
		}


		if (title == null) {
		    title = classTitle;
		}
		if (!canBeRoot() && parentId == null) {
		    throw new AppError("Task of this type can be nested in group only");
		}

		if (!canBeManual() && unitId == null && parentId == null) {
		    throw new AppError("Task of this type can be used within Scheduler unit only");
		}

		if (parentTask != null) {
		    seq = parentTask.getChildsCountFromDb() + 1;
		} else {
		    seq = 0;
		}

		if (parentId != null) {
		    unitId = null;
		    scheduleId = null;
		}

		isActualizableByRelatedJobs = isRunningOnlyWhenRelatedJobsExist();

		if (isSingletone()) {
		    checkSingletone();
		}

		createTime = Arte::Arte.getDatabaseSysDate();
		selfCheckTime = createTime;

		return true;
	}

	/*Radix::Scheduling::Task:performNotifications-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:performNotifications")
	public published  void performNotifications (Int execToken) {
		TaskManager.notifyParent(this, null, execToken);
	}

	/*Radix::Scheduling::Task:prepareForExecution-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:prepareForExecution")
	public published  void prepareForExecution () {
		execResults = null;
		faultMess = null;
	}

	/*Radix::Scheduling::Task:isAtomic-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isAtomic")
	public published  boolean isAtomic () {
		return true;
	}

	/*Radix::Scheduling::Task:onCommand_Post-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onCommand_Post")
	public  void onCommand_Post (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		checkAccess();
		if (executionMode == TaskExecutionMode:Nested) {
		    throw new AppError("Nested task cannot be scheduled by command");
		}
		TaskManager.postManual(this, "'Post' command from " + Arte::Arte.getUserName());
	}

	/*Radix::Scheduling::Task:onCommand_Cancel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onCommand_Cancel")
	public  void onCommand_Cancel (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		checkAccess();
		TaskManager.cancel(this);

	}

	/*Radix::Scheduling::Task:canBeRoot-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:canBeRoot")
	public published  boolean canBeRoot () {
		return true;
	}

	/*Radix::Scheduling::Task:onCommand_MoveDown-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onCommand_MoveDown")
	public  void onCommand_MoveDown (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		if(!canBeMoved.booleanValue()) {
		    throw new AppError("Task is not nested");
		}
		Task prevChild = null;
		Task curChild = null;
		for(Task child : parentTask.getChilds()) {
		    prevChild = curChild;
		    curChild = child;
		    if(prevChild != null && prevChild.id == id) {
		        Int prevChildSeq = prevChild.seq;
		        prevChild.seq = curChild.seq;
		        curChild.seq = prevChildSeq;
		        parentTask.clearChildsCache();
		        parentTask.purgePropCache();
		    }
		}
	}

	/*Radix::Scheduling::Task:onCommand_MoveUp-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onCommand_MoveUp")
	public  void onCommand_MoveUp (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		if(!canBeMoved.booleanValue()) {
		    throw new AppError("Task is not nested");
		}
		Task prevChild = null;
		Task curChild = null;
		for(Task child : parentTask.getChilds()) {
		    prevChild = curChild;
		    curChild = child;
		    if(prevChild != null && curChild.id == id) {
		        Int prevChildSeq = prevChild.seq;
		        prevChild.seq = curChild.seq;
		        curChild.seq = prevChildSeq;
		        parentTask.clearChildsCache();
		        parentTask.purgePropCache();
		    }
		}
	}

	/*Radix::Scheduling::Task:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:beforeDelete")
	protected published  boolean beforeDelete () {
		lockTask();
		if (status == TaskStatus:Executing || status == TaskStatus:Cancelling) {
		    throw new AppError("Running task can not be removed");
		}
		if (parentTask != null) {
		    parentTask.decreaseChildSeqNumbers(seq.longValue());
		}
		return super.beforeDelete();
	}

	/*Radix::Scheduling::Task:resultMapToStr-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:resultMapToStr")
	private final  Str resultMapToStr () {
		if(resultMap == null || resultMap.isEmpty()) {
		    return null;
		}
		final StringBuilder resultBuilder = new StringBuilder();
		for (java.util.Map.Entry<Str, Str> entry : resultMap.entrySet()) {
		    resultBuilder.append(entry.getKey());
		    resultBuilder.append('=');
		    resultBuilder.append(org.apache.commons.lang.StringEscapeUtils.escapeJava(entry.getValue()));
		}
		return resultBuilder.toString();
	}

	/*Radix::Scheduling::Task:loadResultsMap-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:loadResultsMap")
	private final  void loadResultsMap () {
		resultMap = new java.util.HashMap<Str, Str>();
		if (execResults == null) {
		    return;
		} else {
		    try {
		        java.util.Scanner sc = new java.util.Scanner(execResults.getCharacterStream());
		        try {
		            while (sc.hasNextLine()) {
		                final String nextLine = sc.nextLine();
		                final int indexOfEq = nextLine.indexOf("=");
		                boolean unableToParse = false;
		                if (indexOfEq > 0) {
		                    final String key = nextLine.substring(0, indexOfEq);
		                    final String value = nextLine.substring(indexOfEq + 1);
		                    resultMap.put(key, org.apache.commons.lang.StringEscapeUtils.unescapeJava(value));
		                } else {
		                    unableToParse = true;
		                }
		                if (unableToParse) {
		                    Arte::Trace.put(eventCode["Unable to parse line from results of task '%1': '%2'"], "#" + id + " " + title, nextLine);
		                }
		            }
		        } finally {
		            sc.close();
		        }
		    } catch (Exceptions::SQLException ex) {
		        throw new DatabaseError(ex);
		    }
		}
	}

	/*Radix::Scheduling::Task:storeResultsMapIfModified-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:storeResultsMapIfModified")
	private final  void storeResultsMapIfModified () {
		final Str currentResultMapStr = resultMapToStr();
		//System.out.println("Current results: " + currentResultMapStr);
		//System.out.println("Loaded results: " + );
		if (org.radixware.kernel.common.utils.Utils.equals(currentResultMapStr, loadedResultsMapStr)) {
		    return;
		}

		if (currentResultMapStr == null) {
		    execResults = null;
		} else {
		    if (execResults == null) {
		        execResults = Arte::Arte.createTemporaryClob();
		    }

		    try {
		        execResults.setString(1, currentResultMapStr);
		    } catch (Exceptions::SQLException ex) {
		        throw new DatabaseError(ex);
		    }
		}
	}

	/*Radix::Scheduling::Task:checkAccess-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:checkAccess")
	private final  void checkAccess () {
		if (execRoleGuids != null) {
		    if (!Acs::AcsUtils.isCurUserHasRole(execRoleGuids)) {
		        throw new AppError("Insufficient access rights");
		    }
		}

	}

	/*Radix::Scheduling::Task:beforeCreateOnImport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:beforeCreateOnImport")
	protected published  void beforeCreateOnImport (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask) {
		//by default do nothing
	}

	/*Radix::Scheduling::Task:lockTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lockTask")
	public published  void lockTask () {
		//wait one minute
		if (!isLocked(false)) {
		    rereadAndLock(10, false);
		}

	}

	/*Radix::Scheduling::Task:canBeManual-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:canBeManual")
	public published  boolean canBeManual () {
		return true;
	}

	/*Radix::Scheduling::Task:isPhantom-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isPhantom")
	public published  boolean isPhantom () {
		if (status == TaskStatus:Executing || status == TaskStatus:Scheduled || status == TaskStatus:Cancelling) {
		    return isDead();
		} else {
		    return false;
		}
	}

	/*Radix::Scheduling::Task:onCommand_ActualizeStatus-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onCommand_ActualizeStatus")
	public  void onCommand_ActualizeStatus (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		TaskManager.actualizeStatus(this);
	}

	/*Radix::Scheduling::Task:afterImport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:afterImport")
	protected published  void afterImport (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask) {
		//do nothing
	}

	/*Radix::Scheduling::Task:onExport-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onExport")
	protected published  void onExport (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask) {

	}

	/*Radix::Scheduling::Task:isRunningOnlyWhenRelatedJobsExist-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isRunningOnlyWhenRelatedJobsExist")
	public abstract published  boolean isRunningOnlyWhenRelatedJobsExist ();

	/*Radix::Scheduling::Task:isDead-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isDead")
	public abstract published  boolean isDead ();

	/*Radix::Scheduling::Task:hasRelatedJobs-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasRelatedJobs")
	public published  boolean hasRelatedJobs () {

		final TaskRelatedJobsCountCursor cursor = TaskRelatedJobsCountCursor.open(id);
		try {
		    if (cursor.next()) {
		        return cursor.count.intValue() > 0;
		    }
		    return false;
		} finally {
		    cursor.close();
		}
	}

	/*Radix::Scheduling::Task:isSingletone-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isSingletone")
	public published  boolean isSingletone () {
		return false;
	}

	/*Radix::Scheduling::Task:checkSingletone-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:checkSingletone")
	protected published  void checkSingletone () {
		Int thisUnitId = unitId;
		if (thisUnitId == null && parentId != null) {
		    Task root = TaskManager.getTreeRoot(parentId);
		    if (root != null) {
		        thisUnitId = root.unitId;
		    }
		}

		if (thisUnitId == null) {//do not perform singletone check for manual tasks
		    return;
		}

		TasksOfTypeCursor cursor = TasksOfTypeCursor.open(classGuid);
		try {
		    while (cursor.next()) {
		        if (!org.radixware.kernel.server.SrvRunParams.getIsDevelopmentMode()) {
		            throw new AppError("Task of this type already exists in the system");
		        }
		        Task rootForAnalogTask = TaskManager.getTreeRoot(cursor.id);
		        if (rootForAnalogTask != null && rootForAnalogTask.unitId == thisUnitId) {
		            throw new AppError("Task of this type already exists in this unit");
		        }
		    }
		} finally {
		    cursor.close();
		}
	}

	/*Radix::Scheduling::Task:beforeRelatedJobRestart-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:beforeRelatedJobRestart")
	public published  void beforeRelatedJobRestart (org.radixware.ads.Scheduling.server.JobQueueItem job) {
		Arte::Trace.enterContext(Arte::EventContextType:Task, id);
		try {
		    lockTask();
		    if (TaskManager.class.getName().equals(job.className) && idof[TaskManager:execute].toString().equals(job.methodName)) {
		        status = TaskStatus:Scheduled;
		        Arte::Trace.put(eventCode["Status of the task %1 has been changed to %2 due to the restart of the job %3 by the user %4"], new ArrStr(debugTitle, status.Name, "#" + String.valueOf(job.id) + " '" + job.title + "'", Arte::Arte.getUserName()));
		    }
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:Task, id);
		}
	}

	/*Radix::Scheduling::Task:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:onCommand_Export")
	public  org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument onCommand_Export (org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return TaskImpExpUtils.exportTasks(input);
	}

	/*Radix::Scheduling::Task:startTracing-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:startTracing")
	public published  void startTracing () {
		Arte::Trace.enterContext(Arte::EventContextType:Task, id);
	}

	/*Radix::Scheduling::Task:endTracing-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:endTracing")
	public published  void endTracing () {
		Arte::Trace.leaveContext(Arte::EventContextType:Task, id);
	}

	/*Radix::Scheduling::Task:findByRid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:findByRid")
	public static published  org.radixware.ads.Scheduling.server.Task findByRid (Str rid) {
		if (rid == null) {
		    return null;
		}
		try (FindTaskByRidCur cur = FindTaskByRidCur.open(rid)) {
		    if (cur.next()) {
		        return Task.loadByPK(cur.id, true);
		    }
		}
		return null;
	}

	/*Radix::Scheduling::Task:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:loadByPK")
	public static published  org.radixware.ads.Scheduling.server.Task loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),pkValsMap);
		try{
		return (
		org.radixware.ads.Scheduling.server.Task) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}








	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd36DOQAUK4NBXRA7B5OCHPTNIVQ){
			onCommand_ActualizeStatus(newPropValsById);
			return null;
		} else if(cmdId == cmdGS3G6CAUIJBUFIKKDOBUHNHW5M){
			onCommand_Post(newPropValsById);
			return null;
		} else if(cmdId == cmdRD2P2YJVCBAH3IDAODHSZKC6UQ){
			onCommand_MoveUp(newPropValsById);
			return null;
		} else if(cmdId == cmdRINKBQV7KJBLRPI5X6MPAV3JKU){
			onCommand_MoveDown(newPropValsById);
			return null;
		} else if(cmdId == cmdTAVTLGAOKFGIJFDLXU3RI4H3MU){
			org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument result = onCommand_Export((org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdVVLKTQJEBVDJXC66TP546OCFCA){
			onCommand_Cancel(newPropValsById);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Task - Server Meta*/

/*Radix::Scheduling::Task-Entity Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),"Task",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Scheduling::Task:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
							/*Owner Class Name*/
							"Task",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::Scheduling::Task:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task:priorityBoostingSpeed:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33LE4YHMJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:schedule:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6DGLMQLQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(130026,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHULQPEI5JPOBDCITAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:priority:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7JDCGRXMJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:lastExecTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colITNAQ6PMJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:unit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:expiredPolicy:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:isActive:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:prevExecTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPXFOPZCTFAZLBTSI3N2YRAZQM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:status:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:faultMess:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Y2V4U6ZQ5AJRCY4AAORP72JFA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:finishExecTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDQSM4MYVNVDZJIYOCLNDQG3HWM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:startExecTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU6DGXRTARRBNPLA6PLNVYQCOVM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:execResults:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2GMOWN56FG3ROKFDXV4TH3OHU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:parentId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBHMUC3KGIJE3PA6RVXKLXFHP3U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:seq:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:checkExecCondition:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5OYUNLRVBNH7VOX3C6NDYPUUHI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:hasChildren:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:executionMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:execToken:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQ7GFBANJFXBJRZTRXETWZFG4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:finishedChildsCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3JBEZJEWOJG3TIZHOM2T2IQ6AQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:seqTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:debugTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYMED5YPMPFGKBIZ4CLBYRTJ4VQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:lastDurationTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:loadedResultsMapStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBUEMKQGLNEW3GZND7XDMTQIZU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:execRoleGuids:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5D6MV3QAFFGVTMVNH4JQCJUDSI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:scp:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecAAU55TOEHJWDRPOYAAYQQ2Y3GB\" PropId=\"colWUQ25PISLDNBDJA6ACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = 1</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:skipIfExecuting:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOGDESIMOTNGTHJH4B53GOIAQKU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:timeSinceLastExec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:expectedDurationSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUT4YIFRL5CALJVFCEHD2GYJPY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:createTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMU7WRHOUZDINIVZWJHT6KSOFM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:scheduleTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:parentTaskInternal:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLM6AHQJI6NE4NP3NY3ZS4EGNHI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:selfCheckTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMX5YRN5VQBDXDENSJNDBHHMKPE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:rid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:aadcMemberId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:isAadcSysMember:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPJD2CMUXCRC2TGOABNSGAUDJHM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:directoryId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7IXI3MF4ZHHRJGGR7KVS6LL7Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:directory:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTW3TDB3TA5EBRJGYEJDEXL5T7A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::Task:pathInTree:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDC7PWNRD6FH4JMNPGHP6Z2CFKA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task:canBeMoved:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4OX6MPF45JFO3MLIGIFS324NJY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task:Post-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGS3G6CAUIJBUFIKKDOBUHNHW5M"),"Post",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task:Cancel-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVVLKTQJEBVDJXC66TP546OCFCA"),"Cancel",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task:ActualizeStatus-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd36DOQAUK4NBXRA7B5OCHPTNIVQ"),"ActualizeStatus",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task:MoveUp-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdRD2P2YJVCBAH3IDAODHSZKC6UQ"),"MoveUp",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task:MoveDown-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdRINKBQV7KJBLRPI5X6MPAV3JKU"),"MoveDown",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::Task:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTAVTLGAOKFGIJFDLXU3RI4H3MU"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Scheduling::Task:Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT52RXEDID3PBDFHQABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>index(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:IndexDbName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware",org.radixware.kernel.common.enums.EPaginationMethod.ABSOLUTE),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Scheduling::Task:Seq-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srt66P2ZSGOBJAS5MOTSUQJUNILVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),"Seq",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware",org.radixware.kernel.common.enums.EPaginationMethod.ABSOLUTE),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Scheduling::Task:IdDesc-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFUGUUZGWOBABJHHIS2EWQLHZX4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),"IdDesc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIHXAHIO6RBXTJFV6XDNXWMQRI"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>index_desc(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:IndexDbName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware",org.radixware.kernel.common.enums.EPaginationMethod.ABSOLUTE)
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::Scheduling::Task:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Scheduling::Task:Edit:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUHPXMMT535HS7GJPQMUQTISAZM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null),

												/*Radix::Scheduling::Task:Edit:JobQueueItem-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3T47QOI6WVHYJD4XNCCSMQINYA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refF6XD46FP6FEWDFSXAX42HAQ2RI"),
													null,
													null),

												/*Radix::Scheduling::Task:Edit:Task-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiDEYDWERWXNG3TPD5OZOFB3OJAY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql>) and\n(\n    </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or (\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colFDCRWKPMJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclM2T33M4WWZC2FKRJVHD4JMYREI\"/></xsc:Item><xsc:Item><xsc:Sql>) > 0\n        and\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>&lt;>nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpJYY3VQQLENCTXN54C4BF4AXOYU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, -1) \n        and \n        </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> NOT IN (--filter this dir and it\'s child dirs\n            select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>\n            from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql>\n            start with </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n            connect by prior </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>)\n    )\n)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									15538,
									null,

									/*Radix::Scheduling::Task:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::Task:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),true,null,false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16808,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM")),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::Task:BaseForTree-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),"BaseForTree",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srt66P2ZSGOBJAS5MOTSUQJUNILVQ"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt66P2ZSGOBJAS5MOTSUQJUNILVQ")},false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16424,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM")),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::Task:TreeRootManual-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWPGPSZ46FNHUHONW7QCHY5COZE"),"TreeRootManual",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),16560,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colP2PKJLPLJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null \nand --filter moved task and her parent directory\n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> not in (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpJYY3VQQLENCTXN54C4BF4AXOYU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, -1)))\nor ----add other childs of filtered root directory\n(\n</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and\n</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpJYY3VQQLENCTXN54C4BF4AXOYU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;> </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)\n)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFUGUUZGWOBABJHHIS2EWQLHZX4")},false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::Task:TreeRootForScheduler-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCIQEXEK5GNCKNBTQGKVGWRXCNM"),"TreeRootForScheduler",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWPGPSZ46FNHUHONW7QCHY5COZE"),16560,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpD3SFQIQMX5D3HADR4R5ASRTJS4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colP2PKJLPLJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpD3SFQIQMX5D3HADR4R5ASRTJS4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)\nand --filter moved task and her parent directory\n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> not in (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, nvl(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpJYY3VQQLENCTXN54C4BF4AXOYU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, -1)))\nor ----add other childs of filtered root directory\n(\n</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and\n</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> in (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpJYY3VQQLENCTXN54C4BF4AXOYU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;> </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agcWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"pgpA4UVFEA7ABACRAXLSXMAUADMSA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)\n)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFUGUUZGWOBABJHHIS2EWQLHZX4")},false,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},null,null),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::Task:ForEventContextParams-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprRQKQZU524BFGLDZBPY5C6TUAOE"),"ForEventContextParams",org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),16569,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),true)
									},null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},null,null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Scheduling::Task:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTU4RQWDDDZHDXKTRWIAPN7O6P4"),null,51.5625,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQUM2L2D7OBHR5K22VAKORW2XTU")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctBVFUCASJQBEZJA7YW67UOIRROI"),null,52.34375,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJB33GVUNQFFTFJVCFFRAZGTRYQ"))}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Scheduling::Task:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task:priorityBoostingSpeed-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33LE4YHMJPOBDCIUAALOMT5GDM"),"priorityBoostingSpeed",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:schedule-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6DGLMQLQJTOBDCIVAALOMT5GDM"),"schedule",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refZAAZ347LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLSU45NKDI3OBDCIOAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:priority-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7JDCGRXMJPOBDCIUAALOMT5GDM"),"priority",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF5ETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("5")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFDCRWKPMJPOBDCIUAALOMT5GDM"),"classGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFPBX3Y2OBEOZOWMO54N4UBOOQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:lastExecTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colITNAQ6PMJPOBDCIUAALOMT5GDM"),"lastExecTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:unit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),"unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFRETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refP6PKJLPLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:scheduleId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNAPRJRHLJPOBDCIUAALOMT5GDM"),"scheduleId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:unitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2PKJLPLJPOBDCIUAALOMT5GDM"),"unitId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFJETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFFETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2JLCMT4SLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:expiredPolicy-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),"expiredPolicy",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJB6AQM6NRRDRHDOG7KENX27GIQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsMX5WQ6IW55D5BMVQYQBOPJEMSQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:isActive-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),"isActive",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4G7LJN3BS5BOTDZPR7GIHK3LQM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:prevExecTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPXFOPZCTFAZLBTSI3N2YRAZQM"),"prevExecTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHYSH63IURDDXOO6GVTE7ZFMBM"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:status-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),"status",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZ2GFA4Y5ZDB5NT45JBLZYWASU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:faultMess-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Y2V4U6ZQ5AJRCY4AAORP72JFA"),"faultMess",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OLWS2NJK5DTREEJLW5KLGMSAU"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:finishExecTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDQSM4MYVNVDZJIYOCLNDQG3HWM"),"finishExecTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFQQXOFOIBE3DH3DKTEEYYABQ4"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:startExecTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU6DGXRTARRBNPLA6PLNVYQCOVM"),"startExecTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY57A3BCCKJAHDFZT56TMBAFE6Y"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:execResults-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2GMOWN56FG3ROKFDXV4TH3OHU"),"execResults",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWILAL2NWYJH7JKSVS3D7VPYJ7A"),org.radixware.kernel.common.enums.EValType.CLOB,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:parentId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBHMUC3KGIJE3PA6RVXKLXFHP3U"),"parentId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:seq-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),"seq",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls63L7NOXVPZFLPO2NT6JZ23RAUE"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:checkExecCondition-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5OYUNLRVBNH7VOX3C6NDYPUUHI"),"checkExecCondition",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDCDBOU6WNECFLNBOHTMJYEZWQ"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTTVLLBAC2FCU5POAOKJITDKDVY"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFT5TCCPPVGLJCALWCLSRH6URU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:resultMap-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDYZWK2KF2FAMLCDGTQUY5UPUDY"),"resultMap",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:parentTask-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSBSUX2ZY35HIVBMMSUONJNTTTE"),"parentTask",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:hasChildren-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),"hasChildren",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select * from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:executionMode-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),"executionMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNP3LKVMZBCR5CONTCY4FRFFGM"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFYR2GEKPFNACVPEUMJCFVNYGDM"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:execToken-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQ7GFBANJFXBJRZTRXETWZFG4"),"execToken",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:finishedChildsCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3JBEZJEWOJG3TIZHOM2T2IQ6AQ"),"finishedChildsCount",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:seqTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),"seqTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXI6Q7PTUE5DGJB24DLUQKUJOTI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:debugTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYMED5YPMPFGKBIZ4CLBYRTJ4VQ"),"debugTitle",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:lastDurationTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),"lastDurationTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFELRX23WYVHCXIOUBAVMMR74U4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:loadedResultsMapStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBUEMKQGLNEW3GZND7XDMTQIZU"),"loadedResultsMapStr",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:execRoleGuids-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5D6MV3QAFFGVTMVNH4JQCJUDSI"),"execRoleGuids",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNI6DGWIPXJGYBKWGTTQJXPN7BI"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:systemId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KGCFAFD3BEEDB6F3B5VKDFKMI"),"systemId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:scpName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUMR2SR6ONCFXNV5Z7HRKETX4U"),"scpName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:scp-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),"scp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refTE4SUWTG5RGNPETFKKYSIT2CS4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),true,org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB\na\\~b\\~c~1312321"),new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLM6AHQJI6NE4NP3NY3ZS4EGNHI")},org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:skipIfExecuting-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOGDESIMOTNGTHJH4B53GOIAQKU"),"skipIfExecuting",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQIMSNGKMOFD4HDBWMTWVYVLRQM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:timeSinceLastExec-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),"timeSinceLastExec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPCAKAARVKZFVFPVOJT52SROHE4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:isActualizableByRelatedJobs-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3YHDMKAUBATXCL5OSXE7ZU63Y"),"isActualizableByRelatedJobs",null,org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:expectedDurationSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUT4YIFRL5CALJVFCEHD2GYJPY"),"expectedDurationSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXC5EVDTFAUJFDC3PSUPRMXPU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:createTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMU7WRHOUZDINIVZWJHT6KSOFM"),"createTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OL46WR3BNCMNMIFLCS6CZ75EI"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:scheduleTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),"scheduleTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZFNI2UY25FKZFXYTSH44DDRLY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:parentTaskInternal-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLM6AHQJI6NE4NP3NY3ZS4EGNHI"),"parentTaskInternal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2HESFGUAAVG75GNB3WBVAXAMTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:selfCheckTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMX5YRN5VQBDXDENSJNDBHHMKPE"),"selfCheckTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXBB3QGRN5EQDO3QIRBZTIUYEY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:rid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),"rid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZRR5YH2OFFZXKNOAJL2CA3V64"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:aadcMemberId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),"aadcMemberId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLBWQVCYFFG53HNFK3HOVM3YPY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:isAadcSysMember-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPJD2CMUXCRC2TGOABNSGAUDJHM"),"isAadcSysMember",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:directoryId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7IXI3MF4ZHHRJGGR7KVS6LL7Y"),"directoryId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6FTQ6Y5AZDDXPLFOMYGDDFX4U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:directory-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTW3TDB3TA5EBRJGYEJDEXL5T7A"),"directory",null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refXHY652GB7RAKNDBMYRW3TMDH2E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:pathInTree-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDC7PWNRD6FH4JMNPGHP6Z2CFKA"),"pathInTree",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:canBeMoved-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4OX6MPF45JFO3MLIGIFS324NJY"),"canBeMoved",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task:hasChildrenOnlyDirs-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUCM3D2EBDVCBBKQVOJWCRM4XSY"),"hasChildrenOnlyDirs",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>NVL(\n(SELECT 1\nFROM dual\nWHERE EXISTS\n  (SELECT 1\n  FROM </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql> \n  WHERE </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\n  AND </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colFDCRWKPMJPOBDCIUAALOMT5GDM\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclM2T33M4WWZC2FKRJVHD4JMYREI\"/></xsc:Item><xsc:Item><xsc:Sql>) > 0\n  )\n), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDBO4KAX6VNHR7J4HIT7NJDBL5M"),"beforeExecute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBRQL7C35CVBVTBTYUFXOCMDLBQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTERJH6IP7BD5HKBOA7E7E4F43U"),"execute",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKYUAZMF3Y5ECRFOHHRJTGLK7OE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTWB26VXEGFARBM6JSOVY7TISEA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCOSSQ6N7CJF3HNIQKYIP3S5TMY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE4FNBS5BPNHRRCBKZ4TYI2RTFI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM4YPEPPSQZDRFNMGBYJ5V4LJ6U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKGQSXNEDNRCNTLI5TBHXE62D6U"),"refineStatusOnComplete",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJUZMKA2YT5GSZCJ7SUQVLDB5OE"),"afterExecute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("wasExecuted",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFE3EYZ5MP5AFDEXO6DFVBGCH4Y")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("breakStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRJRZJGKB6BD3LBMB2VUWJI5YAU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("executionException",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprULFY7PF3YBB3HPLEWAOIMYZNFY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGF6IQVDALFEEZLIU2GA54XE7AE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRNZUO6NDUVD3TMQJQ3KFVGPJGI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHKCF7UFSP5EEVJAITAYSGBFJTI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXQWGS4T7IRHWHJEQMRDQZ2F2S4"),"setFaultMess",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHWYRQJ5HLJA6FOO4OTKCCDAKWE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7CGTUNJ6QVHLRAXTEOD66TW2QU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQJ36OAD25NFR5DEA5XREYYJ3VI"),"performNotifications",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7QPJUNU4VVBGHNVTMNRM5WXBA4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOFXDILMIP5BNVCHAN33NERN2SE"),"prepareForExecution",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3IQ77RONJZA6XLYRMT6K5X6DXI"),"isAtomic",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdGS3G6CAUIJBUFIKKDOBUHNHW5M"),"onCommand_Post",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC3PHJILZ6FASTJIVFDI2FL7Y4M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdVVLKTQJEBVDJXC66TP546OCFCA"),"onCommand_Cancel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI6QWQEX4YVD4JA6TIXBPWCWPMA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBOIFVDBJQVFIDKL2NUR5D6KA64"),"canBeRoot",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdRINKBQV7KJBLRPI5X6MPAV3JKU"),"onCommand_MoveDown",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDADTZUX3EBGS7ESILAYDGA2X5Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdRD2P2YJVCBAH3IDAODHSZKC6UQ"),"onCommand_MoveUp",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLWPXN7XNTND3LNTTZB3RFQFAFE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQAYKNEEZD5D4PGUDRHQCF6O3XU"),"resultMapToStr",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKOASM3QS4RHHZNCJ62RUDTWC4A"),"loadResultsMap",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthITELWHP57ZDRXOB4HL4HQACJPM"),"storeResultsMapIfModified",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZW63XHVTUVDD5NPVKU5CLSJPNI"),"checkAccess",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6MXY6NXCG5FENER2I7MAVEY3QI"),"beforeCreateOnImport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAMQ3VCKZP5HCVLBIZPR5UKYJ6Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKUZMCX37LBCMPKENNSVZQVBVSI"),"lockTask",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTMY4LSZKOVENHEHZ2G7FKDNXUU"),"canBeManual",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3WYBGLGT4BBCZNQ5RW33FBR4NU"),"isPhantom",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd36DOQAUK4NBXRA7B5OCHPTNIVQ"),"onCommand_ActualizeStatus",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPDOA7A36SVCTZO52YBBQDRH77I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthC4YB3FDESBG7ZPHVFVPL7EMHWA"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYLZE3V4PCJHSJDC3KNAXMFMH6M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZR4GWJ7U5ZHMZOZPZSL2IMMGAY"),"onExport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAZ3I5ZCGVVEHXCZJJ7Y7V4YVGA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3AJ224J3KFH4PKRZDARTG6KR2M"),"isRunningOnlyWhenRelatedJobsExist",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRM7DUPZ7CFAU7NRFYMBPOXCMSY"),"isDead",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6GQAQXI57FGWZKLGIMQZIVCVTQ"),"hasRelatedJobs",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKIF62UYHLRDRPPGLIIMJRLXSUI"),"isSingletone",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQYDFSAHWRBS3HJ3N7P26O2CAU"),"checkSingletone",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBJCFZ22YBZFRRAJVPMA7JNXXSE"),"beforeRelatedJobRestart",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("job",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKD76DJF6MZE5LCDJERSFS3QITQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdTAVTLGAOKFGIJFDLXU3RI4H3MU"),"onCommand_Export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2POIISFGYFFIJMINCXTL7VWOLA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5UVKUGV2TRBODCIW3WYWKPZ43E"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEOFZDHPPRVB5HGKVAZFWRZLIEQ"),"startTracing",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth534JXQYRSZESXK7WP4A5KG5TUM"),"endTracing",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3IZK3NTEANAB3P6TNSIBJ4JPPY"),"findByRid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCBGU4P4EHNH5LJ7TZCX2G5SL5A"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPU65PF7LJPOBDCIUAALOMT5GDM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Scheduling::Task - Desktop Executable*/

/*Radix::Scheduling::Task-Entity Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task")
public interface Task {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Scheduling::TaskGroup:taskToMoveId:taskToMoveId-Presentation Property*/




		public class TaskToMoveId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TaskToMoveId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveId:taskToMoveId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveId:taskToMoveId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TaskToMoveId getTaskToMoveId(){return (TaskToMoveId)getProperty(pgpA4UVFEA7ABACRAXLSXMAUADMSA);}

		/*Radix::Scheduling::TaskGroup:taskToMoveDirId:taskToMoveDirId-Presentation Property*/




		public class TaskToMoveDirId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public TaskToMoveDirId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveDirId:taskToMoveDirId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:taskToMoveDirId:taskToMoveDirId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public TaskToMoveDirId getTaskToMoveDirId(){return (TaskToMoveDirId)getProperty(pgpJYY3VQQLENCTXN54C4BF4AXOYU);}

		/*Radix::Scheduling::TaskGroup:contextUnitId:contextUnitId-Presentation Property*/




		public class ContextUnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public ContextUnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Int dummy = ((Int)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:contextUnitId:contextUnitId")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskGroup:contextUnitId:contextUnitId")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public ContextUnitId getContextUnitId(){return (ContextUnitId)getProperty(pgpD3SFQIQMX5D3HADR4R5ASRTJS4);}











		public org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel )  super.getEntity(i);}
	}

































































































































































































































































	/*Radix::Scheduling::Task:priorityBoostingSpeed:priorityBoostingSpeed-Presentation Property*/


	public class PriorityBoostingSpeed extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PriorityBoostingSpeed(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priorityBoostingSpeed:priorityBoostingSpeed")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priorityBoostingSpeed:priorityBoostingSpeed")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PriorityBoostingSpeed getPriorityBoostingSpeed();
	/*Radix::Scheduling::Task:finishedChildsCount:finishedChildsCount-Presentation Property*/


	public class FinishedChildsCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FinishedChildsCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishedChildsCount:finishedChildsCount")
		public  Int getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishedChildsCount:finishedChildsCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public FinishedChildsCount getFinishedChildsCount();
	/*Radix::Scheduling::Task:faultMess:faultMess-Presentation Property*/


	public class FaultMess extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public FaultMess(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:faultMess:faultMess")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:faultMess:faultMess")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FaultMess getFaultMess();
	/*Radix::Scheduling::Task:aadcMemberId:aadcMemberId-Presentation Property*/


	public class AadcMemberId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcMemberId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::Scheduling::Task:execRoleGuids:execRoleGuids-Presentation Property*/


	public class ExecRoleGuids extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ExecRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execRoleGuids:execRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execRoleGuids:execRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ExecRoleGuids getExecRoleGuids();
	/*Radix::Scheduling::Task:schedule:schedule-Presentation Property*/


	public class Schedule extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Schedule(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.EventSchedule.EventSchedule_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Scheduling.explorer.EventSchedule.EventSchedule_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.EventSchedule.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Scheduling.explorer.EventSchedule.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:schedule:schedule")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:schedule:schedule")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Schedule getSchedule();
	/*Radix::Scheduling::Task:scp:scp-Presentation Property*/


	public class Scp extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Scp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Scp.Scp_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Scp.Scp_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Scp.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Scp.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scp:scp")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scp:scp")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Scp getScp();
	/*Radix::Scheduling::Task:priority:priority-Presentation Property*/


	public class Priority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Priority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EPriority dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPriority ? (org.radixware.kernel.common.enums.EPriority)x : org.radixware.kernel.common.enums.EPriority.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EPriority> getValClass(){
			return org.radixware.kernel.common.enums.EPriority.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EPriority dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EPriority ? (org.radixware.kernel.common.enums.EPriority)x : org.radixware.kernel.common.enums.EPriority.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priority:priority")
		public  org.radixware.kernel.common.enums.EPriority getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:priority:priority")
		public   void setValue(org.radixware.kernel.common.enums.EPriority val) {
			Value = val;
		}
	}
	public Priority getPriority();
	/*Radix::Scheduling::Task:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::Scheduling::Task:parentId:parentId-Presentation Property*/


	public class ParentId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParentId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentId:parentId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentId:parentId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParentId getParentId();
	/*Radix::Scheduling::Task:directoryId:directoryId-Presentation Property*/


	public class DirectoryId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DirectoryId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directoryId:directoryId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directoryId:directoryId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DirectoryId getDirectoryId();
	/*Radix::Scheduling::Task:prevExecTime:prevExecTime-Presentation Property*/


	public class PrevExecTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public PrevExecTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:prevExecTime:prevExecTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:prevExecTime:prevExecTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public PrevExecTime getPrevExecTime();
	/*Radix::Scheduling::Task:finishExecTime:finishExecTime-Presentation Property*/


	public class FinishExecTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public FinishExecTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishExecTime:finishExecTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:finishExecTime:finishExecTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public FinishExecTime getFinishExecTime();
	/*Radix::Scheduling::Task:lastExecTime:lastExecTime-Presentation Property*/


	public class LastExecTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastExecTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastExecTime:lastExecTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastExecTime:lastExecTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastExecTime getLastExecTime();
	/*Radix::Scheduling::Task:unit:unit-Presentation Property*/


	public class Unit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Unit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Unit.Unit_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Unit.Unit_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Unit.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Unit.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:unit:unit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:unit:unit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Unit getUnit();
	/*Radix::Scheduling::Task:parentTaskInternal:parentTaskInternal-Presentation Property*/


	public class ParentTaskInternal extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ParentTaskInternal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentTaskInternal:parentTaskInternal")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:parentTaskInternal:parentTaskInternal")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ParentTaskInternal getParentTaskInternal();
	/*Radix::Scheduling::Task:selfCheckTime:selfCheckTime-Presentation Property*/


	public class SelfCheckTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public SelfCheckTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:selfCheckTime:selfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:selfCheckTime:selfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SelfCheckTime getSelfCheckTime();
	/*Radix::Scheduling::Task:hasChildren:hasChildren-Presentation Property*/


	public class HasChildren extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HasChildren(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasChildren:hasChildren")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:hasChildren:hasChildren")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HasChildren getHasChildren();
	/*Radix::Scheduling::Task:skipIfExecuting:skipIfExecuting-Presentation Property*/


	public class SkipIfExecuting extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public SkipIfExecuting(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:skipIfExecuting:skipIfExecuting")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:skipIfExecuting:skipIfExecuting")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public SkipIfExecuting getSkipIfExecuting();
	/*Radix::Scheduling::Task:status:status-Presentation Property*/


	public class Status extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Status(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.ETaskStatus dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.ETaskStatus ? (org.radixware.kernel.common.enums.ETaskStatus)x : org.radixware.kernel.common.enums.ETaskStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.ETaskStatus> getValClass(){
			return org.radixware.kernel.common.enums.ETaskStatus.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.ETaskStatus dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.ETaskStatus ? (org.radixware.kernel.common.enums.ETaskStatus)x : org.radixware.kernel.common.enums.ETaskStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:status:status")
		public  org.radixware.kernel.common.enums.ETaskStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:status:status")
		public   void setValue(org.radixware.kernel.common.enums.ETaskStatus val) {
			Value = val;
		}
	}
	public Status getStatus();
	/*Radix::Scheduling::Task:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Scheduling::Task:isActive:isActive-Presentation Property*/


	public class IsActive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsActive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isActive:isActive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isActive:isActive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsActive getIsActive();
	/*Radix::Scheduling::Task:createTime:createTime-Presentation Property*/


	public class CreateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public CreateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::Scheduling::Task:execResults:execResults-Presentation Property*/


	public class ExecResults extends org.radixware.kernel.common.client.models.items.properties.PropertyClob{
		public ExecResults(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execResults:execResults")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execResults:execResults")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExecResults getExecResults();
	/*Radix::Scheduling::Task:execToken:execToken-Presentation Property*/


	public class ExecToken extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExecToken(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execToken:execToken")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:execToken:execToken")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExecToken getExecToken();
	/*Radix::Scheduling::Task:directory:directory-Presentation Property*/


	public class Directory extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Directory(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directory:directory")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:directory:directory")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Directory getDirectory();
	/*Radix::Scheduling::Task:startExecTime:startExecTime-Presentation Property*/


	public class StartExecTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public StartExecTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:startExecTime:startExecTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:startExecTime:startExecTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public StartExecTime getStartExecTime();
	/*Radix::Scheduling::Task:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Scheduling::Task:seq:seq-Presentation Property*/


	public class Seq extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Seq(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seq:seq")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seq:seq")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Seq getSeq();
	/*Radix::Scheduling::Task:expectedDurationSec:expectedDurationSec-Presentation Property*/


	public class ExpectedDurationSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExpectedDurationSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expectedDurationSec:expectedDurationSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expectedDurationSec:expectedDurationSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExpectedDurationSec getExpectedDurationSec();
	/*Radix::Scheduling::Task:expiredPolicy:expiredPolicy-Presentation Property*/


	public class ExpiredPolicy extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExpiredPolicy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EExpiredJobExecPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EExpiredJobExecPolicy ? (org.radixware.kernel.common.enums.EExpiredJobExecPolicy)x : org.radixware.kernel.common.enums.EExpiredJobExecPolicy.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EExpiredJobExecPolicy> getValClass(){
			return org.radixware.kernel.common.enums.EExpiredJobExecPolicy.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EExpiredJobExecPolicy dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EExpiredJobExecPolicy ? (org.radixware.kernel.common.enums.EExpiredJobExecPolicy)x : org.radixware.kernel.common.enums.EExpiredJobExecPolicy.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expiredPolicy:expiredPolicy")
		public  org.radixware.kernel.common.enums.EExpiredJobExecPolicy getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:expiredPolicy:expiredPolicy")
		public   void setValue(org.radixware.kernel.common.enums.EExpiredJobExecPolicy val) {
			Value = val;
		}
	}
	public ExpiredPolicy getExpiredPolicy();
	/*Radix::Scheduling::Task:scheduleTitle:scheduleTitle-Presentation Property*/


	public class ScheduleTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ScheduleTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scheduleTitle:scheduleTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:scheduleTitle:scheduleTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ScheduleTitle getScheduleTitle();
	/*Radix::Scheduling::Task:canBeMoved:canBeMoved-Presentation Property*/


	public class CanBeMoved extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanBeMoved(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:canBeMoved:canBeMoved")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:canBeMoved:canBeMoved")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanBeMoved getCanBeMoved();
	/*Radix::Scheduling::Task:timeSinceLastExec:timeSinceLastExec-Presentation Property*/


	public class TimeSinceLastExec extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TimeSinceLastExec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:timeSinceLastExec:timeSinceLastExec")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:timeSinceLastExec:timeSinceLastExec")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TimeSinceLastExec getTimeSinceLastExec();
	/*Radix::Scheduling::Task:pathInTree:pathInTree-Presentation Property*/


	public class PathInTree extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public PathInTree(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:pathInTree:pathInTree")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:pathInTree:pathInTree")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PathInTree getPathInTree();
	/*Radix::Scheduling::Task:seqTitle:seqTitle-Presentation Property*/


	public class SeqTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SeqTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seqTitle:seqTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:seqTitle:seqTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SeqTitle getSeqTitle();
	/*Radix::Scheduling::Task:lastDurationTime:lastDurationTime-Presentation Property*/


	public class LastDurationTime extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastDurationTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastDurationTime:lastDurationTime")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:lastDurationTime:lastDurationTime")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastDurationTime getLastDurationTime();
	/*Radix::Scheduling::Task:isAadcSysMember:isAadcSysMember-Presentation Property*/


	public class IsAadcSysMember extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsAadcSysMember(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isAadcSysMember:isAadcSysMember")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:isAadcSysMember:isAadcSysMember")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsAadcSysMember getIsAadcSysMember();
	/*Radix::Scheduling::Task:executionMode:executionMode-Presentation Property*/


	public class ExecutionMode extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExecutionMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Scheduling.common.TaskExecutionMode dummy = x == null ? null : (x instanceof org.radixware.ads.Scheduling.common.TaskExecutionMode ? (org.radixware.ads.Scheduling.common.TaskExecutionMode)x : org.radixware.ads.Scheduling.common.TaskExecutionMode.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Scheduling.common.TaskExecutionMode> getValClass(){
			return org.radixware.ads.Scheduling.common.TaskExecutionMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Scheduling.common.TaskExecutionMode dummy = x == null ? null : (x instanceof org.radixware.ads.Scheduling.common.TaskExecutionMode ? (org.radixware.ads.Scheduling.common.TaskExecutionMode)x : org.radixware.ads.Scheduling.common.TaskExecutionMode.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:executionMode:executionMode")
		public  org.radixware.ads.Scheduling.common.TaskExecutionMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:executionMode:executionMode")
		public   void setValue(org.radixware.ads.Scheduling.common.TaskExecutionMode val) {
			Value = val;
		}
	}
	public ExecutionMode getExecutionMode();
	/*Radix::Scheduling::Task:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Scheduling::Task:loadedResultsMapStr:loadedResultsMapStr-Presentation Property*/


	public class LoadedResultsMapStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LoadedResultsMapStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:loadedResultsMapStr:loadedResultsMapStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:loadedResultsMapStr:loadedResultsMapStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LoadedResultsMapStr getLoadedResultsMapStr();
	/*Radix::Scheduling::Task:debugTitle:debugTitle-Presentation Property*/


	public class DebugTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DebugTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:debugTitle:debugTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:debugTitle:debugTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DebugTitle getDebugTitle();
	/*Radix::Scheduling::Task:checkExecCondition:checkExecCondition-Presentation Property*/


	public class CheckExecCondition extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CheckExecCondition(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:checkExecCondition:checkExecCondition")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:checkExecCondition:checkExecCondition")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CheckExecCondition getCheckExecCondition();
	public static class ActualizeStatus extends org.radixware.kernel.common.client.models.items.Command{
		protected ActualizeStatus(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Post extends org.radixware.kernel.common.client.models.items.Command{
		protected Post(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class MoveUp extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveUp(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class MoveDown extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveDown(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument send(org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument.class);
		}

	}

	public static class Cancel extends org.radixware.kernel.common.client.models.items.Command{
		protected Cancel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Scheduling::Task - Desktop Meta*/

/*Radix::Scheduling::Task-Entity Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
			"Radix::Scheduling::Task",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZSKPYPT64G4ENTLKOOWJ6OT56KBWJ4YR"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),0,

			/*Radix::Scheduling::Task:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task:priorityBoostingSpeed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33LE4YHMJPOBDCIUAALOMT5GDM"),
						"priorityBoostingSpeed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJETUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:priorityBoostingSpeed:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:schedule:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6DGLMQLQJTOBDCIVAALOMT5GDM"),
						"schedule",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBETUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUES3RITTDFGHVJVTQ5ZFCN5EVQ"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLSU45NKDI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLSU45NKDI3OBDCIOAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHULQPEI5JPOBDCITAALOMT5GDM")},
						null,
						null,
						130026,
						131051,false),

					/*Radix::Scheduling::Task:priority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7JDCGRXMJPOBDCIUAALOMT5GDM"),
						"priority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF5ETUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("5"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:priority:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsYING7OYUJ3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:lastExecTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colITNAQ6PMJPOBDCIUAALOMT5GDM"),
						"lastExecTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVETUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:lastExecTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),
						"unit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFRETUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::Scheduling::Task:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFFETUM24OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						28,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2JLCMT4SLOBDCKTAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:expiredPolicy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),
						"expiredPolicy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJB6AQM6NRRDRHDOG7KENX27GIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsMX5WQ6IW55D5BMVQYQBOPJEMSQ"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:expiredPolicy:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsMX5WQ6IW55D5BMVQYQBOPJEMSQ"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:isActive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),
						"isActive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4G7LJN3BS5BOTDZPR7GIHK3LQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:isActive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:prevExecTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPXFOPZCTFAZLBTSI3N2YRAZQM"),
						"prevExecTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHYSH63IURDDXOO6GVTE7ZFMBM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:prevExecTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:status:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),
						"status",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZ2GFA4Y5ZDB5NT45JBLZYWASU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:status:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TBZJHMSKBEVZE2TKPH2P4NEFE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:faultMess:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Y2V4U6ZQ5AJRCY4AAORP72JFA"),
						"faultMess",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OLWS2NJK5DTREEJLW5KLGMSAU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:faultMess:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5UURKV2YVHG5DZ4FROAUGLAVM"),
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:finishExecTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDQSM4MYVNVDZJIYOCLNDQG3HWM"),
						"finishExecTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFQQXOFOIBE3DH3DKTEEYYABQ4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:finishExecTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:startExecTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU6DGXRTARRBNPLA6PLNVYQCOVM"),
						"startExecTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY57A3BCCKJAHDFZT56TMBAFE6Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:startExecTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:execResults:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS2GMOWN56FG3ROKFDXV4TH3OHU"),
						"execResults",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWILAL2NWYJH7JKSVS3D7VPYJ7A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.CLOB,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:execResults:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:parentId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBHMUC3KGIJE3PA6RVXKLXFHP3U"),
						"parentId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:parentId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:seq:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),
						"seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls63L7NOXVPZFLPO2NT6JZ23RAUE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:seq:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQV545HKTUJG2TLHBTMOAXA5N7E"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:checkExecCondition:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5OYUNLRVBNH7VOX3C6NDYPUUHI"),
						"checkExecCondition",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDCDBOU6WNECFLNBOHTMJYEZWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTTVLLBAC2FCU5POAOKJITDKDVY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::Scheduling::Task:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFT5TCCPPVGLJCALWCLSRH6URU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:hasChildren:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),
						"hasChildren",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:hasChildren:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:executionMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),
						"executionMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNP3LKVMZBCR5CONTCY4FRFFGM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFYR2GEKPFNACVPEUMJCFVNYGDM"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:executionMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFYR2GEKPFNACVPEUMJCFVNYGDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:execToken:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQ7GFBANJFXBJRZTRXETWZFG4"),
						"execToken",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:execToken:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:finishedChildsCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3JBEZJEWOJG3TIZHOM2T2IQ6AQ"),
						"finishedChildsCount",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:finishedChildsCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,true),

					/*Radix::Scheduling::Task:seqTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),
						"seqTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXI6Q7PTUE5DGJB24DLUQKUJOTI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:seqTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:debugTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYMED5YPMPFGKBIZ4CLBYRTJ4VQ"),
						"debugTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:debugTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:lastDurationTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),
						"lastDurationTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFELRX23WYVHCXIOUBAVMMR74U4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:lastDurationTime:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF3ZTQ3RVX5BWZN7RJNIKYRQFWQ"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:loadedResultsMapStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUBUEMKQGLNEW3GZND7XDMTQIZU"),
						"loadedResultsMapStr",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:loadedResultsMapStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:execRoleGuids:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5D6MV3QAFFGVTMVNH4JQCJUDSI"),
						"execRoleGuids",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNI6DGWIPXJGYBKWGTTQJXPN7BI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVBBSP3NKG5HVVODE7MOX4U7DWI"),
						true,

						/*Radix::Scheduling::Task:execRoleGuids:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNTQLDLPUSVFF3AM2WD7R47E46I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTG7XRFZI6BDQDFEJE2M5TH4W6Y"),
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:scp:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),
						"scp",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB\na\\~b\\~c~1312321"),
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMD7VBWKYUFGX3AGLMZBRAG7EUQ"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Scheduling::Task:skipIfExecuting:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOGDESIMOTNGTHJH4B53GOIAQKU"),
						"skipIfExecuting",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQIMSNGKMOFD4HDBWMTWVYVLRQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:skipIfExecuting:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:timeSinceLastExec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),
						"timeSinceLastExec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPCAKAARVKZFVFPVOJT52SROHE4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOQKH3FYBC7TCMQER73XWJKT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:timeSinceLastExec:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:expectedDurationSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUT4YIFRL5CALJVFCEHD2GYJPY"),
						"expectedDurationSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXC5EVDTFAUJFDC3PSUPRMXPU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:expectedDurationSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3RSDT5PCIJB63EGEPOEEWKTYBA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMU7WRHOUZDINIVZWJHT6KSOFM"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OL46WR3BNCMNMIFLCS6CZ75EI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:scheduleTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),
						"scheduleTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZFNI2UY25FKZFXYTSH44DDRLY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:scheduleTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:parentTaskInternal:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLM6AHQJI6NE4NP3NY3ZS4EGNHI"),
						"parentTaskInternal",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Scheduling::Task:selfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMX5YRN5VQBDXDENSJNDBHHMKPE"),
						"selfCheckTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXBB3QGRN5EQDO3QIRBZTIUYEY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:selfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZRR5YH2OFFZXKNOAJL2CA3V64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLBWQVCYFFG53HNFK3HOVM3YPY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_IN_EDITOR,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,2L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:isAadcSysMember:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPJD2CMUXCRC2TGOABNSGAUDJHM"),
						"isAadcSysMember",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:isAadcSysMember:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:directoryId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7IXI3MF4ZHHRJGGR7KVS6LL7Y"),
						"directoryId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6FTQ6Y5AZDDXPLFOMYGDDFX4U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:directoryId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:directory:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTW3TDB3TA5EBRJGYEJDEXL5T7A"),
						"directory",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Scheduling::Task:pathInTree:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDC7PWNRD6FH4JMNPGHP6Z2CFKA"),
						"pathInTree",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:pathInTree:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task:canBeMoved:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4OX6MPF45JFO3MLIGIFS324NJY"),
						"canBeMoved",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task:canBeMoved:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task:Post-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdGS3G6CAUIJBUFIKKDOBUHNHW5M"),
						"Post",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3UXPCCSJJG5VAAGBGQR4UNJCQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWZZPS3ZBBNFVNHFNSKTITBMHQE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task:Cancel-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdVVLKTQJEBVDJXC66TP546OCFCA"),
						"Cancel",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVF2FNOMQBET7OTGENDVNHVR4E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img43EDIPHW2BACNMLKVPOSGOIRRQ"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task:ActualizeStatus-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd36DOQAUK4NBXRA7B5OCHPTNIVQ"),
						"ActualizeStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDUYF66YWNBFVBJS5YSIU7IZHIU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgNSEPSKHDVFGIXPXGQCIDWK532E"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task:MoveUp-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdRD2P2YJVCBAH3IDAODHSZKC6UQ"),
						"MoveUp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFF6UTPQOGBFHNJEGK6UWUEVU54"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgU72DZZSNEZAWHGSLWNRIFA4MW4"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task:MoveDown-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdRINKBQV7KJBLRPI5X6MPAV3JKU"),
						"MoveDown",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKJZVNEHESVAJRLQ3WGBK6FQI3Y"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgXXQ4APUH2FGA7B63EQSIKA46V4"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::Task:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTAVTLGAOKFGIJFDLXU3RI4H3MU"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTWOVKT3URA6JP2RULPONQO2JQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVORNT5ZFORBEHLSYMJNUWJK6II"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::Task:Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),
						"Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT52RXEDID3PBDFHQABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::Task:Seq-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srt66P2ZSGOBJAS5MOTSUQJUNILVQ"),
						"Seq",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::Task:IdDesc-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFUGUUZGWOBABJHHIS2EWQLHZX4"),
						"IdDesc",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIHXAHIO6RBXTJFV6XDNXWMQRI"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2HESFGUAAVG75GNB3WBVAXAMTY"),"Task=>Task (parentId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBHMUC3KGIJE3PA6RVXKLXFHP3U")},new String[]{"parentId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refP6PKJLPLJPOBDCIUAALOMT5GDM"),"Task=>Unit (unitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2PKJLPLJPOBDCIUAALOMT5GDM")},new String[]{"unitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refTE4SUWTG5RGNPETFKKYSIT2CS4"),"Task=>Scp (scpName=>name, systemId=>systemId)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUMR2SR6ONCFXNV5Z7HRKETX4U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col3KGCFAFD3BEEDB6F3B5VKDFKMI")},new String[]{"scpName","systemId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colI4QSJ5PEHJWDRPOYAAYQQ2Y3GB"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUQ25PISLDNBDJA6ACQMTAIZT4")},new String[]{"name","systemId"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refXHY652GB7RAKNDBMYRW3TMDH2E"),"Task=>Task (directoryId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colC7IXI3MF4ZHHRJGGR7KVS6LL7Y")},new String[]{"directoryId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refZAAZ347LJPOBDCIUAALOMT5GDM"),"Task=>EventSchd (scheduleId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLSU45NKDI3OBDCIOAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colNAPRJRHLJPOBDCIUAALOMT5GDM")},new String[]{"scheduleId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDQQG5QKDI3OBDCIOAALOMT5GDM")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWPGPSZ46FNHUHONW7QCHY5COZE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCIQEXEK5GNCKNBTQGKVGWRXCNM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprRQKQZU524BFGLDZBPY5C6TUAOE")},
			true,true,false);
}

/* Radix::Scheduling::Task - Web Meta*/

/*Radix::Scheduling::Task-Entity Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
			"Radix::Scheduling::Task",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task:Edit - Desktop Meta*/

/*Radix::Scheduling::Task:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::Task:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::Task:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36JWPW5JLNFO5AQLMWV7B7HLHQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZSKPYPT64G4ENTLKOOWJ6OT56KBWJ4YR"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6DGLMQLQJTOBDCIVAALOMT5GDM"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7JDCGRXMJPOBDCIUAALOMT5GDM"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33LE4YHMJPOBDCIUAALOMT5GDM"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colITNAQ6PMJPOBDCIUAALOMT5GDM"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPXFOPZCTFAZLBTSI3N2YRAZQM"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5OYUNLRVBNH7VOX3C6NDYPUUHI"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU6DGXRTARRBNPLA6PLNVYQCOVM"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDQSM4MYVNVDZJIYOCLNDQG3HWM"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOGDESIMOTNGTHJH4B53GOIAQKU"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUT4YIFRL5CALJVFCEHD2GYJPY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMU7WRHOUZDINIVZWJHT6KSOFM"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3Y2V4U6ZQ5AJRCY4AAORP72JFA"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMX5YRN5VQBDXDENSJNDBHHMKPE"),0,25,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5D6MV3QAFFGVTMVNH4JQCJUDSI"),0,10,1,false,false)
			},null),

			/*Radix::Scheduling::Task:Edit:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUHPXMMT535HS7GJPQMUQTISAZM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
	,

	/*Radix::Scheduling::Task:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Scheduling::Task:Edit:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUHPXMMT535HS7GJPQMUQTISAZM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
					0,
					null,
					16560,true),

				/*Radix::Scheduling::Task:Edit:JobQueueItem-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3T47QOI6WVHYJD4XNCCSMQINYA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
					0,
					null,
					16560,true),

				/*Radix::Scheduling::Task:Edit:Task-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiDEYDWERWXNG3TPD5OZOFB3OJAY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
					0,
					null,
					16560,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Scheduling::Task:Edit:Model - Desktop Executable*/

/*Radix::Scheduling::Task:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Scheduling.explorer.Task.Task_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task:Edit:Model:Properties-Properties*/

	/*Radix::Scheduling::Task:Edit:Model:execRoleGuids-Presentation Property*/




	public class ExecRoleGuids extends org.radixware.ads.Scheduling.explorer.Task.col5D6MV3QAFFGVTMVNH4JQCJUDSI{
		public ExecRoleGuids(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::Scheduling::Task:Edit:Model:execRoleGuids:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Scheduling::Task:Edit:Model:execRoleGuids:Nested classes-Nested Classes*/

		/*Radix::Scheduling::Task:Edit:Model:execRoleGuids:Properties-Properties*/

		/*Radix::Scheduling::Task:Edit:Model:execRoleGuids:Methods-Methods*/

		/*Radix::Scheduling::Task:Edit:Model:execRoleGuids:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:execRoleGuids:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new RoleArrPropEditor(this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:execRoleGuids")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:execRoleGuids")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ExecRoleGuids getExecRoleGuids(){return (ExecRoleGuids)getProperty(col5D6MV3QAFFGVTMVNH4JQCJUDSI);}








	/*Radix::Scheduling::Task:Edit:Model:Methods-Methods*/

	/*Radix::Scheduling::Task:Edit:Model:onCommand_Cancel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:onCommand_Cancel")
	public  void onCommand_Cancel (org.radixware.ads.Scheduling.explorer.Task.Cancel command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
		rereadSelector();
	}

	/*Radix::Scheduling::Task:Edit:Model:onCommand_MoveDown-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:onCommand_MoveDown")
	public  void onCommand_MoveDown (org.radixware.ads.Scheduling.explorer.Task.MoveDown command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
		rereadSelector();
	}

	/*Radix::Scheduling::Task:Edit:Model:onCommand_MoveUp-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:onCommand_MoveUp")
	public  void onCommand_MoveUp (org.radixware.ads.Scheduling.explorer.Task.MoveUp command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
		rereadSelector();
	}

	/*Radix::Scheduling::Task:Edit:Model:onCommand_Post-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:onCommand_Post")
	public  void onCommand_Post (org.radixware.ads.Scheduling.explorer.Task.Post command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
		rereadSelector();
	}

	/*Radix::Scheduling::Task:Edit:Model:rereadSelector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:rereadSelector")
	public  void rereadSelector () {
		Explorer.Models::GroupModel groupModel = Explorer.Context::Utils.getOwnerGroup(this);
		if (groupModel != null) {
		    try {
		        groupModel.reread();
		    } catch (Exceptions::ServiceCallException ex) {
		        groupModel.showException(ex);
		    } catch (Exceptions::ServiceClientException ex) {
		        groupModel.showException(ex);
		    }

		}
	}

	/*Radix::Scheduling::Task:Edit:Model:updateFieldsVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:updateFieldsVisibility")
	protected published  void updateFieldsVisibility () {
		seq.setVisible(false);
		unit.setVisible(false);
		schedule.setVisible(false);
		expiredPolicy.setVisible(false);
		execRoleGuids.setVisible(false);
		isActive.setVisible(false);
		skipIfExecuting.setVisible(false);
		expectedDurationSec.setVisible(false);
		switch (executionMode.Value) {
		    case TaskExecutionMode:Nested:
		        seq.setVisible(true);
		        isActive.setVisible(directoryId.Value == null);
		        break;
		    case TaskExecutionMode:Schedule:
		        unit.setVisible(true);
		        schedule.setVisible(true);
		        isActive.setVisible(true);
		        expiredPolicy.setVisible(true);
		        skipIfExecuting.setVisible(true);
		        expectedDurationSec.setVisible(true);
		        break;
		    case TaskExecutionMode:Manual:
		        execRoleGuids.setVisible(true);
		        break;
		}
		aadcMemberId.setVisible(isAadcSysMember.Value == true);

	}

	/*Radix::Scheduling::Task:Edit:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:isCommandAccessible")
	public published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (commandDef.Id.equals(idof[Task:MoveUp]) || commandDef.Id.equals(idof[Task:MoveDown])) {
		    Explorer.Models::GroupModel thisGroupModel = Explorer.Context::Utils.getOwnerGroup(this);
		    if (thisGroupModel == null) {
		        return false;
		    }
		    Explorer.Models::EntityModel parentModel = Explorer.Context::Utils.findOwnerEntityModel(thisGroupModel, Task.class);
		    if (parentModel == null || parentModel.getClassPresentationDef().isDerivedFrom(getEnvironment().getDefManager().getClassPresentationDef(idof[Task.Parallel])) || parentModel.getClassId().equals(idof[Task.Parallel])) {
		        return false;
		    }
		}
		if(commandDef.Id.equals(idof[Task:Post])) {
		    return executionMode.Value != TaskExecutionMode:Nested;
		}
		return super.isCommandAccessible(commandDef);
	}

	/*Radix::Scheduling::Task:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:afterRead")
	protected published  void afterRead () {
		if (!isInSelectorRowContext()) {
		    updateFieldsVisibility();
		}
	}

	/*Radix::Scheduling::Task:Edit:Model:activateProperty-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:activateProperty")
	protected published  org.radixware.kernel.common.client.models.items.properties.Property activateProperty (org.radixware.kernel.common.types.Id id) {
		Explorer.Models.Properties::Property property = super.activateProperty(id);
		if (idof[Task:status].equals(id)) {
		    property.addDependent(getCommand(idof[Task:Cancel]));
		    property.addDependent(getCommand(idof[Task:Post]));
		}
		return property;
	}

	/*Radix::Scheduling::Task:Edit:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		if (command.Id.equals(idof[Task:Post])) {
		    return status.Value != TaskStatus:Scheduled && status.Value != TaskStatus:Executing && status.Value != TaskStatus:Cancelling;
		}
		if (command.Id.equals(idof[Task:MoveUp]) || command.Id.equals(idof[Task:MoveDown])) {
		    Explorer.Models::GroupModel thisGroupModel = Explorer.Context::Utils.getOwnerGroup(this);
		    if (thisGroupModel == null) {
		        return false;
		    }
		    Explorer.Models::EntityModel parentModel = Explorer.Context::Utils.findOwnerEntityModel(thisGroupModel, Task.class);
		    if (parentModel == null || parentModel.getClassPresentationDef().isDerivedFrom(getEnvironment().getDefManager().getClassPresentationDef(idof[Task.Parallel])) || parentModel.getClassId().equals(idof[Task.Parallel])) {
		        return false;
		    }
		    if (canBeMoved.Value.booleanValue()) {
		        if (command.Id.equals(idof[Task:MoveUp])) {
		            return seq.Value.intValue() > 1;
		        } else {
		            try {
		                if (!thisGroupModel.hasMoreRows() && thisGroupModel.getEntity(thisGroupModel.getEntitiesCount() - 1).getPid().equals(getPid())) {
		                    return false;
		                }
		            } catch (Explorer.Exceptions::BrokenEntityObjectException ex) {
		                return false;
		            } catch (InterruptedException ex) {
		                return false;
		            } catch (Exceptions::ServiceClientException ex) {
		                return false;
		            }
		            return true;
		        }
		    } else {
		        return false;
		    }
		}

		if (command.Id.equals(idof[Task:Cancel])) {
		    return (status.Value == TaskStatus:Executing || status.Value == TaskStatus:Scheduled);
		}

		return super.isCommandEnabled(command);
	}

	/*Radix::Scheduling::Task:Edit:Model:onCommand_ActualizeStatus-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:onCommand_ActualizeStatus")
	public  void onCommand_ActualizeStatus (org.radixware.ads.Scheduling.explorer.Task.ActualizeStatus command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
		rereadSelector();
	}

	/*Radix::Scheduling::Task:Edit:Model:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:onCommand_Export")
	public  void onCommand_Export (org.radixware.ads.Scheduling.explorer.Task.Export command) {
		try {
		    TaskExportXsd:ExportRequestDocument xIn = TaskExportXsd:ExportRequestDocument.Factory.newInstance();
		    xIn.addNewExportRequest().addNewExportItem().TaskId = id.Value;
		    TaskExportXsd:TaskSetDocument xOut = command.send(xIn);
		    Client.Views::IView parentView =  findNearestView();    
		    CfgManagement::DesktopUtils.saveXmlAndShowCompleteMessage(parentView, xOut);
		} catch (Exception e) {
		    showException(e);
		}

	}

	/*Radix::Scheduling::Task:Edit:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:getContextId")
	public published  Str getContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::Scheduling::Task:Edit:Model:getContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Edit:Model:getContextType")
	public published  Str getContextType () {
		return Arte::EventContextType:Task.Value;
	}
	public final class ActualizeStatus extends org.radixware.ads.Scheduling.explorer.Task.ActualizeStatus{
		protected ActualizeStatus(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ActualizeStatus( this );
		}

	}

	public final class Post extends org.radixware.ads.Scheduling.explorer.Task.Post{
		protected Post(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Post( this );
		}

	}

	public final class MoveUp extends org.radixware.ads.Scheduling.explorer.Task.MoveUp{
		protected MoveUp(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveUp( this );
		}

	}

	public final class MoveDown extends org.radixware.ads.Scheduling.explorer.Task.MoveDown{
		protected MoveDown(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveDown( this );
		}

	}

	public final class Export extends org.radixware.ads.Scheduling.explorer.Task.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Export( this );
		}

	}

	public final class Cancel extends org.radixware.ads.Scheduling.explorer.Task.Cancel{
		protected Cancel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Cancel( this );
		}

	}























}

/* Radix::Scheduling::Task:Edit:Model - Desktop Meta*/

/*Radix::Scheduling::Task:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAIWBLDTSJTOBDCIVAALOMT5GDM"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task:Edit:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::Task:Create - Desktop Meta*/

/*Radix::Scheduling::Task:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,
	null,
	null,

	/*Radix::Scheduling::Task:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	15538,0,0);
}
/* Radix::Scheduling::Task:Create:Model - Desktop Executable*/

/*Radix::Scheduling::Task:Create:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Create:Model")
public class Create:Model  extends org.radixware.ads.Scheduling.explorer.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task:Create:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task:Create:Model:Properties-Properties*/

	/*Radix::Scheduling::Task:Create:Model:Methods-Methods*/

	/*Radix::Scheduling::Task:Create:Model:beforePrepareCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:Create:Model:beforePrepareCreate")
	protected published  boolean beforePrepareCreate () {
		Explorer.Models::EntityModel parentModel = (Explorer.Models::EntityModel) findOwnerByClass(Explorer.Models::EntityModel.class);
		if (parentModel == null) {
		    unit.Value = null;
		    parentId.Value = null;
		    directoryId.Value = null;
		} else if (parentModel instanceof System::Unit) { 
		    parentId.Value = null;
		    directoryId.Value = null;
		    unit.setServerValue(new PropertyValue(unit.getDefinition(), new Reference(parentModel)));//do not send set parent request
		} else if (parentModel instanceof Task) {
		    unit.Value = null;
		    final boolean isDir = parentModel.getClassId() == idof[Task.Dir] || 
		        getEnvironment().getDefManager().getRepository().isClassExtends(parentModel.getClassId(), idof[Task.Dir]);
		    if (isDir) {
		        directoryId.Value = ((Task) parentModel).id.Value;
		    } else {
		        parentId.Value = ((Task) parentModel).id.Value;
		    }
		}
		return super.beforePrepareCreate();
	}


}

/* Radix::Scheduling::Task:Create:Model - Desktop Meta*/

/*Radix::Scheduling::Task:Create:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXGQ6C3VMQPOBDCKCAALOMT5GDM"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAIWBLDTSJTOBDCIVAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task:Create:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::Task:General - Desktop Meta*/

/*Radix::Scheduling::Task:General-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
		null,
		null,
		null,
		null,
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),
		null,
		false,
		true,
		null,
		16808,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Scheduling::Task:BaseForTree - Desktop Meta*/

/*Radix::Scheduling::Task:BaseForTree-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class BaseForTree_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new BaseForTree_mi();
	private BaseForTree_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
		"BaseForTree",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srt66P2ZSGOBJAS5MOTSUQJUNILVQ")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srt66P2ZSGOBJAS5MOTSUQJUNILVQ"),
		null,
		false,
		true,
		null,
		16424,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Scheduling::Task:BaseForTree:Model - Desktop Executable*/

/*Radix::Scheduling::Task:BaseForTree:Model-Group Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:BaseForTree:Model")
public class BaseForTree:Model  extends org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return BaseForTree:Model_mi.rdxMeta; }



	public BaseForTree:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task:BaseForTree:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task:BaseForTree:Model:Properties-Properties*/

	/*Radix::Scheduling::Task:BaseForTree:Model:Methods-Methods*/

	/*Radix::Scheduling::Task:BaseForTree:Model:canPaste-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:BaseForTree:Model:canPaste")
	public published  boolean canPaste (org.radixware.kernel.common.client.models.EntityModel entityModel) {
		if (!super.canPaste(entityModel) || !(entityModel instanceof Task)) {
		    return false;
		}
		final Explorer.Models::Model pastedModel = entityModel;
		Explorer.Models::Model pastedModelAsParent = findOwner(new Explorer.Models::IModelFinder() {
		    public boolean isTarget(Explorer.Models::Model model) {
		        if (model instanceof Task && ((Task) model).id.Value == ((Task) pastedModel).id.Value) {
		            return true;
		        }
		        return false;
		    }
		});

		return pastedModelAsParent == null;
	}


}

/* Radix::Scheduling::Task:BaseForTree:Model - Desktop Meta*/

/*Radix::Scheduling::Task:BaseForTree:Model-Group Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class BaseForTree:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
						"BaseForTree:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task:BaseForTree:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::Task:TreeRootManual - Desktop Meta*/

/*Radix::Scheduling::Task:TreeRootManual-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class TreeRootManual_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new TreeRootManual_mi();
	private TreeRootManual_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWPGPSZ46FNHUHONW7QCHY5COZE"),
		"TreeRootManual",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFUGUUZGWOBABJHHIS2EWQLHZX4")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),
		null,
		false,
		true,
		null,
		0,null,
		16560,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Scheduling::Task:TreeRootManual:Model - Desktop Executable*/

/*Radix::Scheduling::Task:TreeRootManual:Model-Group Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model")
public class TreeRootManual:Model  extends org.radixware.ads.Scheduling.explorer.BaseForTree:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return TreeRootManual:Model_mi.rdxMeta; }



	public TreeRootManual:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	@Override
	@SuppressWarnings("deprecation")
	public org.radixware.kernel.common.client.views.IView createView() { return new 
	org.radixware.ads.Scheduling.explorer.View(getEnvironment());}
	@Override
	public org.radixware.kernel.common.types.Id getCustomViewId() {return org.radixware.kernel.common.types.Id.Factory.loadFrom("cesWPGPSZ46FNHUHONW7QCHY5COZE");}


	/*Radix::Scheduling::Task:TreeRootManual:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task:TreeRootManual:Model:Properties-Properties*/

	/*Radix::Scheduling::Task:TreeRootManual:Model:pathInTree-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr pathInTree=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:pathInTree")
	  org.radixware.kernel.common.types.ArrStr getPathInTree() {
		return pathInTree;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:pathInTree")
	   void setPathInTree(org.radixware.kernel.common.types.ArrStr val) {
		pathInTree = val;
	}






	/*Radix::Scheduling::Task:TreeRootManual:Model:Methods-Methods*/

	/*Radix::Scheduling::Task:TreeRootManual:Model:Selector_opened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:Selector_opened")
	public  void Selector_opened (com.trolltech.qt.gui.QWidget widget) {
		final boolean isManual = this.SelectorPresentationDef.Id == idof[Task:TreeRootManual];

		Explorer.Views.Wraps::StandardSelectorTreeModel model = new StandardSelectorTreeModel(this, idof[Task:Edit:Task]) {

		    private final com.trolltech.qt.gui.QIcon dirIcon = Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(idof[Explorer.Icons::open]));
		    private final com.trolltech.qt.gui.QIcon seqGroupIcon = Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(idof[SeqTaskGroup]));
		    private final com.trolltech.qt.gui.QIcon parallelGroupIcon = Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(idof[ParallelTaskGroup]));
		    private final com.trolltech.qt.gui.QIcon taskIcon = Explorer.Icons::ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(idof[SchedulerJob.AbstractBase]));

		    private boolean isDirectory(Explorer.Models::EntityModel em) {
		        return em.getClassId() == idof[Task.Dir]
		                || getEnvironment().getDefManager().getRepository().isClassExtends(em.getClassId(), idof[Task.Dir]);
		    }

		    private boolean isSeqGroup(Explorer.Models::EntityModel em) {
		        return em.getClassId() == idof[Task.Sequence]
		                || getEnvironment().getDefManager().getRepository().isClassExtends(em.getClassId(), idof[Task.Sequence]);
		    }

		    private boolean isParallelGroup(Explorer.Models::EntityModel em) {
		        return em.getClassId() == idof[Task.Parallel]
		                || getEnvironment().getDefManager().getRepository().isClassExtends(em.getClassId(), idof[Task.Parallel]);
		    }

		    protected Types::Id mapSelectorColumn(Explorer.Models::SelectorColumnModel columnModel, Explorer.Models::GroupModel groupModel) {
		        if (columnModel != null && columnModel.Id == idof[Task:isActive]) {
		            if (isManual && groupModel == null || groupModel == getRootGroupModel()) {
		                return null;
		            }
		            final Explorer.Models::EntityModel owner = (Explorer.Models::EntityModel) groupModel.findOwnerByClass(Explorer.Models::EntityModel.class);
		            if (owner != null && isDirectory(owner)) {
		                return null;
		            }
		        }
		        return super.mapSelectorColumn(columnModel, groupModel);
		    }

		    public Types::Id getHasChildrenPropertyId(Explorer.Models::EntityModel model) {
		        return idof[Task:hasChildren];
		    }

		    @Override
		    protected com.trolltech.qt.gui.QIcon getIconForProperty(final Explorer.Models.Properties::Property property) {
		        if (property.Id == idof[Task:id] && property.getOwner() instanceof Explorer.Models::EntityModel) {
		            final Explorer.Models::EntityModel model = (Explorer.Models::EntityModel) property.getOwner();
		            if (isDirectory(model)) {
		                return dirIcon;
		            } else if (isSeqGroup(model)) {
		                return seqGroupIcon;
		            } else if (isParallelGroup(model)) {
		                return parallelGroupIcon;
		            } else {
		                return taskIcon;
		            }
		        }
		        return super.getIconForProperty(property);
		    }

		    @Override
		    public boolean canCreateChild(final org.radixware.kernel.common.client.models.EntityModel parentEntity) {
		        if (!(getEnvironment().getDefManager().getRepository().isClassExtends(parentEntity.getClassId(), idof[Task.AGroup])
		                || isDirectory(parentEntity))) {
		            return false;
		        }
		        return super.canCreateChild(parentEntity);
		    }

		    @Override
		    protected Explorer.Models::GroupModel createChildGroupModel(Explorer.Models::EntityModel parentEntity) {
		        final Explorer.Models::GroupModel gm = super.createChildGroupModel(parentEntity);
		        gm.getProperty(idof[TaskGroup:taskToMoveId]).setValueObject(getRootGroupModel().getProperty(idof[TaskGroup:taskToMoveId]).getValueObject());
		        gm.getProperty(idof[TaskGroup:taskToMoveDirId]).setValueObject(getRootGroupModel().getProperty(idof[TaskGroup:taskToMoveDirId]).getValueObject());
		        gm.getProperty(idof[TaskGroup:contextUnitId]).setValueObject(getRootGroupModel().getProperty(idof[TaskGroup:contextUnitId]).getValueObject());
		        return gm;
		    }
		};
		Explorer.Widgets::SelectorTree selectorTree = new SelectorTree((Explorer.Views::SelectorView) this.GroupView, model);
		com.trolltech.qt.gui.QLayout layout = new com.trolltech.qt.gui.QVBoxLayout(widget);
		layout.addWidget(selectorTree);
		this.GroupView.setSelectorWidget(selectorTree);
	}

	/*Radix::Scheduling::Task:TreeRootManual:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:onCommand_Import")
	public  void onCommand_Import (org.radixware.ads.Scheduling.explorer.TaskGroup.Import command) {
		final java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file != null) {
		    try {
		        TaskExportXsd:TaskDocument xSingleTaskDoc = null;
		        try {
		            //try old format
		            xSingleTaskDoc = TaskExportXsd:TaskDocument.Factory.parse(file);
		        } catch (Exception ex) {
		            //ignore - new format is used
		        }
		        TaskExportXsd:TaskSetDocument xDoc;
		        if (xSingleTaskDoc != null) {
		            xDoc = TaskExportXsd:TaskSetDocument.Factory.newInstance();
		            xDoc.addNewTaskSet().addNewTask().set(xSingleTaskDoc.Task);
		        } else {
		            xDoc = TaskExportXsd:TaskSetDocument.Factory.parse(file);
		        }

		        TaskImportDialog dialog = new TaskImportDialog(getEnvironment());
		        dialog.setParent((Explorer.Qt.Types::QWidget) getGroupView());
		        dialog.Model.taskSet = xDoc.TaskSet;
		        dialog.Model.requireSchedules = findOwnerByClass(Unit.JobScheduler.class) != null;
		        final Client.Views::DialogResult result = dialog.execDialog();
		        if (result == Client.Views::DialogResult.ACCEPTED) {
		            Unit.JobScheduler parentModel = (Unit.JobScheduler) Explorer.Context::Utils.findOwnerEntityModel(this, Unit.JobScheduler.class);
		            if (parentModel != null) {
		                for (TaskExportXsd:Task xTask : xDoc.TaskSet.TaskList) {
		                    xTask.SchedulerUnitId = parentModel.id.Value;
		                }
		            }
		            command.send(xDoc);
		            if (getView() != null) {
		                getView().reread();
		            }
		            Explorer.Env::Application.messageInformation("Import complete");
		        }
		    } catch (Exceptions::Exception e) {
		        showException(e);
		        return;
		    }
		}
	}

	/*Radix::Scheduling::Task:TreeRootManual:Model:onCommand_Export-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:onCommand_Export")
	public  void onCommand_Export (org.radixware.ads.Scheduling.explorer.TaskGroup.Export command) {
		try {
		    TaskExportDialog exportDialog = new TaskExportDialog(getEnvironment());
		    while (hasMoreRows()) {
		        readMore();
		    }
		    exportDialog.Model.taskGroup = this;
		    Client.Views::DialogResult dialogResult = exportDialog.execDialog();
		    if (dialogResult == Client.Views::DialogResult.ACCEPTED) {
		        TaskExportXsd:ExportRequestDocument xRqDoc = exportDialog.Model.exportRequest;
		        if (xRqDoc == null || xRqDoc.ExportRequest.ExportItemList.isEmpty()) {
		            return;
		        }
		        TaskExportXsd:TaskSetDocument xml = command.send(xRqDoc);
		        CfgManagement::DesktopUtils.saveXmlAndShowCompleteMessage(getView(), xml);
		    }
		} catch (Exception ex) {
		    showException(ex);
		}
	}

	/*Radix::Scheduling::Task:TreeRootManual:Model:onCommand_MoveToDir-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:onCommand_MoveToDir")
	public  void onCommand_MoveToDir (org.radixware.ads.Scheduling.explorer.TaskGroup.MoveToDir command) {
		try {
		    command.send();
		    if (pathInTree != null) {
		        if (getView() != null) {
		            getView().reread();
		        }
		        afterUpdateTaskDirSettings(pathInTree);
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Scheduling::Task:TreeRootManual:Model:afterUpdateTaskDirSettings-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:Model:afterUpdateTaskDirSettings")
	private final  void afterUpdateTaskDirSettings (org.radixware.kernel.common.types.ArrStr pathInTree) {
		Client.Views::ISelector selector = getGroupView();
		if (selector != null) {
		    try {
		        selector.reread(null);
		    } catch (Throwable ex) {
		        getEnvironment().processException(ex);
		    }

		    Client.Views::IView view = selector;
		    while (view != null) {
		        if (view instanceof Explorer.Views::SelectorView) {
		            Explorer.Views::SelectorView selectorView = (Explorer.Views::SelectorView) view;
		            Client.Views::ISelectorWidget widget = selectorView.getSelectorWidget();
		            if (widget instanceof Explorer.Widgets::SelectorTree) {
		                Explorer.Widgets::SelectorTree tree = (Explorer.Widgets::SelectorTree) widget;
		                try {
		                    com.trolltech.qt.core.QModelIndex index = tree.pathToIndex(pathInTree, true);
		                    if (index != null) {
		                        tree.setCurrentIndex(index);
		                    }
		                } catch (Throwable ex) {
		                    ex.printStackTrace();
		                }
		            }
		            break;
		        }
		        view = view.findParentView();
		    }
		}
	}
	public final class MoveToDir extends org.radixware.ads.Scheduling.explorer.TaskGroup.MoveToDir{
		protected MoveToDir(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveToDir( this );
		}

	}

	public final class Export extends org.radixware.ads.Scheduling.explorer.TaskGroup.Export{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Export( this );
		}

	}

	public final class Import extends org.radixware.ads.Scheduling.explorer.TaskGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

















}

/* Radix::Scheduling::Task:TreeRootManual:Model - Desktop Meta*/

/*Radix::Scheduling::Task:TreeRootManual:Model-Group Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TreeRootManual:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmWPGPSZ46FNHUHONW7QCHY5COZE"),
						"TreeRootManual:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("agmUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task:TreeRootManual:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Scheduling::Task:TreeRootManual:View - Desktop Executable*/

/*Radix::Scheduling::Task:TreeRootManual:View-Custom Selector for Desktop*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task:TreeRootManual:View")
public class View extends org.radixware.kernel.explorer.views.selector.Selector {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View Selector;
	public View getSelector(){ return Selector;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession) {
		super(userSession);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		Selector = this;
		Selector.setObjectName("Selector");
		Selector.setFont(DEFAULT_FONT);
		Selector.opened.connect(model, "mthWWA35G3PCVDI5FQRWLZA2OWOZE(com.trolltech.qt.gui.QWidget)");
		opened.emit(this.content);
	}
	public final org.radixware.ads.Scheduling.explorer.TreeRootManual:Model getModel() {
		return (org.radixware.ads.Scheduling.explorer.TreeRootManual:Model) super.getModel();
	}

}

/* Radix::Scheduling::Task:TreeRootForScheduler - Desktop Meta*/

/*Radix::Scheduling::Task:TreeRootForScheduler-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class TreeRootForScheduler_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new TreeRootForScheduler_mi();
	private TreeRootForScheduler_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprCIQEXEK5GNCKNBTQGKVGWRXCNM"),
		"TreeRootForScheduler",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWPGPSZ46FNHUHONW7QCHY5COZE"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFUGUUZGWOBABJHHIS2EWQLHZX4")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtTZ2RXEDID3PBDFHQABIFNQAABA"),
		null,
		false,
		true,
		null,
		0,null,
		16560,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLF3UQU34BEYVGNYQ3VNZ3GLK4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6SABFPVW75FF3O4KIR6T3KW4DM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRTERPGHHJZFBRAMUL45CAEPQQE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.TreeRootManual:Model(userSession,this);
	}
}
/* Radix::Scheduling::Task:ForEventContextParams - Desktop Meta*/

/*Radix::Scheduling::Task:ForEventContextParams-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class ForEventContextParams_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new ForEventContextParams_mi();
	private ForEventContextParams_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprRQKQZU524BFGLDZBPY5C6TUAOE"),
		"ForEventContextParams",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTLCJTB3SJTOBDCIVAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
		null,
		null,
		null,
		null,
		false,
		null,
		null,
		false,
		false,
		null,
		0,null,
		16569,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd43XIPIU4ZFCE5BJEVD3QEKELJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4W7CE76W7VATBNLH5FLEE3FAVA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMH5P2FSTNABBMBNUIXU3AINEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Task.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Scheduling::Task - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%s\' is in the wrong state at execution start: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задание \'%s\' в момент начала выполнения находится в некорректном состоянии: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2YXXOSMSZNCGZBMWMEAX6GZHNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls36JWPW5JLNFO5AQLMWV7B7HLHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3RSDT5PCIJB63EGEPOEEWKTYBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Активна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4G7LJN3BS5BOTDZPR7GIHK3LQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IT4LFFCIZA2PDNDXUDMCVY73U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task of this type already exists in this unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача данного типа уже существует в данном модуле");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LFLE26XZJH2LJ6ALQQFGZ72PA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error message");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сообщение об ошибке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OLWS2NJK5DTREEJLW5KLGMSAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Still in progress...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Еще выполняется...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54NS7F5TPRFSFF4OFRCCDYGFVE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sequence number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Порядковый номер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls63L7NOXVPZFLPO2NT6JZ23RAUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OL46WR3BNCMNMIFLCS6CZ75EI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<ready>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<готова к выполнению>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6TBZJHMSKBEVZE2TKPH2P4NEFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%s\' is in the wrong state: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задание \'%s\' находится в некорректном состоянии: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73I3FCUIZJF7RKS74VC5BCVOAY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import complete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт завершен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQH4USZTPFG6FAMPR6KTB2JFHA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ссылочный ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZRR5YH2OFFZXKNOAJL2CA3V64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Task");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать задачу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTWOVKT3URA6JP2RULPONQO2JQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' was skipped due to exception in execution preparation check: \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение задачи \'%2\' было пропущено из-за исключения во время подготовки к выполнению: \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDTL4YU6PCJA6ZLFN5CQREKDA6E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Update Status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Актуализировать статус");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDUYF66YWNBFVBJS5YSIU7IZHIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task is not nested");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача не является вложенной");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDXWTLAVVD5E65I6NZV32LATHAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unknown>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неизвестно>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF3ZTQ3RVX5BWZN7RJNIKYRQFWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Priority");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приоритет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF5ETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Tasks");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFBETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Precondition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Предусловие");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDCDBOU6WNECFLNBOHTMJYEZWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last execution duration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Длительность последнего выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFELRX23WYVHCXIOUBAVMMR74U4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Up");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вверх");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFF6UTPQOGBFHNJEGK6UWUEVU54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFFETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFJETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schedule ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. расписания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFRETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last scheduled execution time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последнее запланированное время выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFVETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schedule");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расписание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFZFNI2UY25FKZFXYTSH44DDRLY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schedule");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Расписание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGBETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task of this type can be nested in group only");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача данного типа может быть только вложенной в группу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGGGFSAN3L5F73BBOTK5S63JZEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Priority boosting rate");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Скорость повышения приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to parse line from results of task \'%1\': \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось разобрать строку из результатов выполнения задачи \'%1\': \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGQC2GSINSFASRPPZBOTNDFZD6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZETUM24OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<отсутствует>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH5UURKV2YVHG5DZ4FROAUGLAVM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' is already running");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\' уже запущена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGVSNX7EAZDQ7ACYLTHNSAZTDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Scheduled time of last successful execution ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Плановое время последнего успешного выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHYSH63IURDDXOO6GVTE7ZFMBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Directory ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. директории");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6FTQ6Y5AZDDXPLFOMYGDDFX4U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error creating clob for error message: \'%1\'. Error message: \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при создании clob для сообщения об ошибке: \'%1\'. Сообщение об ошибке: \'%2\'.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJY3YDMFCNDBBHNX3HAIHEHJEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.Task",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maintenance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обслуживание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJB33GVUNQFFTFJVCFFRAZGTRYQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Expired tasks execution");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение просроченных задач");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJB6AQM6NRRDRHDOG7KENX27GIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFPBX3Y2OBEOZOWMO54N4UBOOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFT5TCCPPVGLJCALWCLSRH6URU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Down");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вниз");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKJZVNEHESVAJRLQ3WGBK6FQI3Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' cancelled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение задачи \'%1\' было отменено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKK2XXTHCLBAYLKOT6L64CBHKTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Execution mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим запуска");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLNP3LKVMZBCR5CONTCY4FRFFGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<take from executor settings>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<брать из настроек исполнителя>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMD7VBWKYUFGX3AGLMZBRAG7EUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last execution completed (actual time)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фактическое окончание последнего исполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFQQXOFOIBE3DH3DKTEEYYABQ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Expected execution time (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ожидаемое время выполнения (c)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXC5EVDTFAUJFDC3PSUPRMXPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Nested task cannot be scheduled by command");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вложенная задача не может быть запланирована при помощи команды");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMY7DNFZDEFGYPLF7KDICMOBYQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Execution roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли исполнителей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNI6DGWIPXJGYBKWGTTQJXPN7BI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unrestricted access>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неограниченный доступ>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNTQLDLPUSVFF3AM2WD7R47E46I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time elapsed since last execution");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прошло времени с последнего запуска");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYOQKH3FYBC7TCMQER73XWJKT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' completed with status \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\' завершилась со статусом \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOWQ2DJCRQJHODOHMPYHVI5BPZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.DEBUG,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time since last execution");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прошло с последнего запуска");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPCAKAARVKZFVFPVOJT52SROHE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Refining status of task \'%1\' resulted in thrown exception: %2");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"При уточнении статуса задачи \'%1\' выброшено исключение: %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPEE5HKXBZNGSJFEBBH3BK56RRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<being executed for %s>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<выполняется %s>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPQRKW43WOBGARLWAIWQRQ5RXHA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статус");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZ2GFA4Y5ZDB5NT45JBLZYWASU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Skip if still in progress");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пропускать, если еще выполняется");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQIMSNGKMOFD4HDBWMTWVYVLRQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Monitoring");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Мониторинг");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQUM2L2D7OBHR5K22VAKORW2XTU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<last>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<последняя>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQV545HKTUJG2TLHBTMOAXA5N7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' was skipped due to exception in precondition check: \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение задачи \'%1\' было пропущено из-за исключения во время проверки предусловия: \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR44HEBZZVRE3BG5OAHPIWXPMJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Insufficient access rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Недостаточно прав");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRFZQEEVWYZCFXJVV3NGP4Q5UWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not used>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не используется>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2PRKMTQSVCI5DWFEAAKGNAW7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Schedule for Execution");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запланировать к выполнению");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3UXPCCSJJG5VAAGBGQR4UNJCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT52RXEDID3PBDFHQABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Running task can not be removed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполняющаяся задача не может быть удалена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTDTL33BBYVHH5DVDZ6HJZRPLUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<access is denied>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<доступ запрещен>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTG7XRFZI6BDQDFEJE2M5TH4W6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task is not nested");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача не является вложенной");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTW5N4AU4J5FUBPZ2RCEBLTP66Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Status of the task %1 has been changed to %2 due to the restart of the job %3 by the user %4");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статус задачи %1 был изменен на %2 из-за перезапуска задания %3 пользователем %4");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU7ADFZIMTRBAHHMKNN4GIULKU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.Task",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not used>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не используется>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUES3RITTDFGHVJVTQ5ZFCN5EVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' was skipped");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\' не была выполнена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUIRDA43IUJFADLZ6BRJ7GETHEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.DEBUG,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Prepare for execution of task \'%1\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подготовка к выполнению задачи \'%1\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXZKRTDQHFDO7ALHP74MLT3MEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.DEBUG,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2JLCMT4SLOBDCKTAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task of this type can be used within Scheduler unit only");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача данного типа может быть использована только внутри планировщика");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGWJXH44HVC65B3U6TLJ7D2GCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last self-check time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней самопроверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXBB3QGRN5EQDO3QIRBZTIUYEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Execution result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результат выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWILAL2NWYJH7JKSVS3D7VPYJ7A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Checks if this task is neither executing or planned for execution. Used in task status actualization.\nIf task has status SCHEDULED, EXECUTING or CANCELLING and isDead() returns true, then task become CANCELLED.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX2KOVR7C6FERRL5OM25DHYFRHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"№");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXI6Q7PTUE5DGJB24DLUQKUJOTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last execution started (actual time)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Фактическое начало последнего исполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY57A3BCCKJAHDFZT56TMBAFE6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cancelled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отменено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYE5R6S7QLVDGPKTJGBL26M6LVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Cancel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отменить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYVF2FNOMQBET7OTGENDVNHVR4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unknown>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неизвестно>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ4JFSNABDFEELI5AAFGYEZAMLM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID (descending)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид. (в обратном порядке)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIHXAHIO6RBXTJFV6XDNXWMQRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task of this type already exists in the system");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача данного типа уже существует в системе");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZIKQRRCGHZGCBNI2B77ZVUY32I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Execute at AADC member");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполнять на AADC узле");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZLBWQVCYFFG53HNFK3HOVM3YPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecWZB7K4HLJPOBDCIUAALOMT5GDM"),"Task - Localizing Bundle",$$$items$$$);
}
