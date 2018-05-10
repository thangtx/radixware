
/* Radix::Scheduling::JobQueueItem - Server Executable*/

/*Radix::Scheduling::JobQueueItem-Entity Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem")
public final published class JobQueueItem  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final java.text.DateFormat SELF_CHECK_TIME_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Types::Id calcClassId() {
	    int lastDotPos = className.lastIndexOf(".");
	    if (lastDotPos == -1) {
	        return null;
	    }
	    Str classId = className.substring(lastDotPos + 1);
	    Types::Id id = Types::Id.Factory.loadFrom(classId);
	    return id;

	}
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return JobQueueItem_mi.rdxMeta;}

	/*Radix::Scheduling::JobQueueItem:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Properties-Properties*/

	/*Radix::Scheduling::JobQueueItem:creatorPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:creatorPid")
	public published  Str getCreatorPid() {
		return creatorPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:creatorPid")
	public published   void setCreatorPid(Str val) {
		creatorPid = val;
	}

	/*Radix::Scheduling::JobQueueItem:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Scheduling::JobQueueItem:className-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:className")
	public published  Str getClassName() {
		return className;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:className")
	public published   void setClassName(Str val) {
		className = val;
	}

	/*Radix::Scheduling::JobQueueItem:creatorEntityGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:creatorEntityGuid")
	public published  Str getCreatorEntityGuid() {
		return creatorEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:creatorEntityGuid")
	public published   void setCreatorEntityGuid(Str val) {
		creatorEntityGuid = val;
	}

	/*Radix::Scheduling::JobQueueItem:priority-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priority")
	public published  Int getPriority() {
		return priority;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priority")
	public published   void setPriority(Int val) {
		priority = val;
	}

	/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priorityBoostingSpeed")
	public published  Int getPriorityBoostingSpeed() {
		return priorityBoostingSpeed;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priorityBoostingSpeed")
	public published   void setPriorityBoostingSpeed(Int val) {
		priorityBoostingSpeed = val;
	}

	/*Radix::Scheduling::JobQueueItem:dueTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:dueTime")
	public published  java.sql.Timestamp getDueTime() {
		return dueTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:dueTime")
	public published   void setDueTime(java.sql.Timestamp val) {
		dueTime = val;
	}

	/*Radix::Scheduling::JobQueueItem:methodName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodName")
	public published  Str getMethodName() {
		return methodName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodName")
	public published   void setMethodName(Str val) {
		methodName = val;
	}

	/*Radix::Scheduling::JobQueueItem:curPriority-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:curPriority")
	public published  Int getCurPriority() {
		return curPriority;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:curPriority")
	public published   void setCurPriority(Int val) {
		curPriority = val;
	}

	/*Radix::Scheduling::JobQueueItem:execRequesterId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:execRequesterId")
	public published  Str getExecRequesterId() {
		return execRequesterId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:execRequesterId")
	public published   void setExecRequesterId(Str val) {
		execRequesterId = val;
	}

	/*Radix::Scheduling::JobQueueItem:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:classTitle")
	public published  Str getClassTitle() {

		Str title = null;

		Meta::ClassDef classDef = getCalledClassDef();

		if (classDef != null) {
		    title = classDef.Name;
		}

		if (title != null) {
		    return title;
		}

		return className;
	}

	/*Radix::Scheduling::JobQueueItem:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Scheduling::JobQueueItem:scpName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:scpName")
	public published  Str getScpName() {
		return scpName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:scpName")
	public published   void setScpName(Str val) {
		scpName = val;
	}

	/*Radix::Scheduling::JobQueueItem:taskId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:taskId")
	public published  Int getTaskId() {
		return taskId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:taskId")
	public published   void setTaskId(Int val) {
		taskId = val;
	}

	/*Radix::Scheduling::JobQueueItem:task-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:task")
	public published  org.radixware.ads.Scheduling.server.Task getTask() {
		return task;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:task")
	public published   void setTask(org.radixware.ads.Scheduling.server.Task val) {
		task = val;
	}

	/*Radix::Scheduling::JobQueueItem:methodTitle-Dynamic Property*/



	protected Str methodTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodTitle")
	public published  Str getMethodTitle() {

		try {
		    Class c = Arte::Arte.class.getClassLoader().loadClass(className);
		    for (java.lang.reflect.Method m : c.getMethods()) {
		        if (m.getName().equals(methodName)) {
		            org.radixware.kernel.common.lang.MetaInfo metaAnno = (org.radixware.kernel.common.lang.MetaInfo) m.getAnnotation(org.radixware.kernel.common.lang.MetaInfo.class);
		            final String name = metaAnno.name();
		            int lastColonIdx = name.lastIndexOf(':');
		            if (lastColonIdx >= 0) {
		                return name.substring(lastColonIdx + 1);
		            }
		            return name;
		        }
		    }
		} catch (Exception ex) {
		}
		return methodName;
	}

	/*Radix::Scheduling::JobQueueItem:executorId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executorId")
	public published  Int getExecutorId() {
		return executorId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executorId")
	public published   void setExecutorId(Int val) {
		executorId = val;
	}

	/*Radix::Scheduling::JobQueueItem:executor-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executor")
	public  org.radixware.ads.System.server.Unit getExecutor() {
		return executor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executor")
	public   void setExecutor(org.radixware.ads.System.server.Unit val) {
		executor = val;
	}

	/*Radix::Scheduling::JobQueueItem:processorTitle-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:processorTitle")
	public published  Str getProcessorTitle() {
		return processorTitle;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:processorTitle")
	public published   void setProcessorTitle(Str val) {
		processorTitle = val;
	}

	/*Radix::Scheduling::JobQueueItem:executingOn-Dynamic Property*/



	protected Str executingOn=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executingOn")
	public published  Str getExecutingOn() {

		if (dueTime == null) {
		    return "<not set up for execution yet>";
		}
		if (processorTitle != null) {
		    return String.format("<being executed by %s>", processorTitle);
		}

		long curMillis = systimestamp.Time;

		if (dueTime.Time <= curMillis) {
		    return "<ready for execution>";
		}

		return Utils::Timing.millisToDurationString(dueTime.Time - curMillis);
	}

	/*Radix::Scheduling::JobQueueItem:systimestamp-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:systimestamp")
	public  java.sql.Timestamp getSystimestamp() {
		return systimestamp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:systimestamp")
	public   void setSystimestamp(java.sql.Timestamp val) {
		systimestamp = val;
	}

	/*Radix::Scheduling::JobQueueItem:selfCheckTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTime")
	public published  java.sql.Timestamp getSelfCheckTime() {
		return selfCheckTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTime")
	public published   void setSelfCheckTime(java.sql.Timestamp val) {
		selfCheckTime = val;
	}

	/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr-Dynamic Property*/



	protected Str selfCheckTimeStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTimeStr")
	public published  Str getSelfCheckTimeStr() {

		if (selfCheckTime == null) {
		    return "<not yet started>";
		}
		return SELF_CHECK_TIME_FORMAT.format(new java.util.Date(selfCheckTime.Time)) + " (" + Utils::Timing.millisToDurationString(System.currentTimeMillis() - selfCheckTime.Time) + " " + "ago"+ ")";
	}

	/*Radix::Scheduling::JobQueueItem:aadcMemberId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:aadcMemberId")
	public published  Int getAadcMemberId() {
		return aadcMemberId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:aadcMemberId")
	public published   void setAadcMemberId(Int val) {
		aadcMemberId = val;
	}

	/*Radix::Scheduling::JobQueueItem:allowRerun-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:allowRerun")
	public published  Bool getAllowRerun() {
		return allowRerun;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:allowRerun")
	public published   void setAllowRerun(Bool val) {
		allowRerun = val;
	}

	/*Radix::Scheduling::JobQueueItem:threadKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadKey")
	public published  Int getThreadKey() {
		return threadKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadKey")
	public published   void setThreadKey(Int val) {
		threadKey = val;
	}

	/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolClassGuid")
	public published  Str getThreadPoolClassGuid() {
		return threadPoolClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolClassGuid")
	public published   void setThreadPoolClassGuid(Str val) {
		threadPoolClassGuid = val;
	}

	/*Radix::Scheduling::JobQueueItem:threadPoolPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolPid")
	public published  Str getThreadPoolPid() {
		return threadPoolPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolPid")
	public published   void setThreadPoolPid(Str val) {
		threadPoolPid = val;
	}

	/*Radix::Scheduling::JobQueueItem:unlockCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:unlockCount")
	public published  Int getUnlockCount() {
		return unlockCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:unlockCount")
	public published   void setUnlockCount(Int val) {
		unlockCount = val;
	}

	/*Radix::Scheduling::JobQueueItem:isInAadc-Dynamic Property*/



	protected Bool isInAadc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:isInAadc")
	protected  Bool getIsInAadc() {

		return Arte::Arte.getInstance().getInstance().getAadcManager().isInAadc();
	}

	/*Radix::Scheduling::JobQueueItem:threadStr-Dynamic Property*/



	protected Str threadStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadStr")
	public published  Str getThreadStr() {

		if (threadPoolClassGuid == null) {
		    return null;
		}
		String poolName = threadPoolClassGuid;
		try {
		    Class c = Arte::Arte.class.getClassLoader().loadClass(threadPoolClassGuid);
		    org.radixware.kernel.common.lang.MetaInfo metaAnno = (org.radixware.kernel.common.lang.MetaInfo) c.getAnnotation(org.radixware.kernel.common.lang.MetaInfo.class);
		    poolName = metaAnno.name();
		    int lastDoubleColonIdx = poolName.lastIndexOf("::");
		    if (lastDoubleColonIdx > 0) {
		        poolName = poolName.substring(lastDoubleColonIdx + 2);
		    }
		} catch (Exception ex) {
		}
		return poolName + "[" + threadPoolPid + "]." + threadKey;

	}































































































































































































	/*Radix::Scheduling::JobQueueItem:Methods-Methods*/

	/*Radix::Scheduling::JobQueueItem:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:loadByPidStr")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),pidAsStr);
		try{
		return (
		org.radixware.ads.Scheduling.server.JobQueueItem) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Scheduling::JobQueueItem:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:loadByPK")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),pkValsMap);
		try{
		return (
		org.radixware.ads.Scheduling.server.JobQueueItem) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Scheduling::JobQueueItem:awake-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:awake")
	public published  void awake (org.radixware.kernel.server.arte.JobQueue.Param[] params) {
		org.radixware.kernel.server.arte.JobQueue.awake(this, params);

	}

	/*Radix::Scheduling::JobQueueItem:onCalcTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:onCalcTitle")
	protected published  Str onCalcTitle (Str title) {
		Str className = "?";
		Str classId = className;
		if (classId!=null) {
		    className = classId;
		    int dotPos  = classId.lastIndexOf( "." );
		    if( dotPos != -1 )
		        classId = classId.substring( dotPos + 2 );
		    try {
		        className = Meta::Utils.getDefinitionName(Types::Id.Factory.loadFrom(classId));
		    } catch( org.radixware.kernel.common.exceptions.DefinitionNotFoundError e) {
		    }
		}
		return id.toString() + ") " +
		        className +
		        "." +
		        methodName +
		        "(...) - " +
		        (dueTime == null ? "" : dueTime.toString());

	}

	/*Radix::Scheduling::JobQueueItem:getCalledClassDef-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:getCalledClassDef")
	public published  org.radixware.kernel.server.meta.clazzes.RadClassDef getCalledClassDef () {
		Types::Id classId = calcClassId();

		if (classId != null) {
		    try {
		        return Arte::Arte.getDefManager().getClassDef(classId);
		    } catch (Exceptions::Exception ex) {
		        //do nothing
		    }
		}

		return null;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::JobQueueItem - Server Meta*/

/*Radix::Scheduling::JobQueueItem-Entity Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobQueueItem_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),"JobQueueItem",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Scheduling::JobQueueItem:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
							/*Owner Class Name*/
							"JobQueueItem",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::Scheduling::JobQueueItem:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::JobQueueItem:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::Scheduling::JobQueueItem:className:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:priority:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:dueTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:methodName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:curPriority:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:scpName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:task:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colESHGJVLPDZDOZGTTVYGT4GWHLA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::JobQueueItem:methodTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:executor:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Scheduling::JobQueueItem:processorTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RDV26FFFVAQFKMPGQ2BHZT36Y"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:executingOn:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:systimestamp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3KM4QRDFJHU5JBJPLEJVKYEWQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:selfCheckTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4UPMUTPR5DZ5NOZ3N4A232YJA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:aadcMemberId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:allowRerun:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGWUJPPZSBFFFKCPSF7VX7ZNHA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:threadKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSQ62QBZJ6NBR5HDABNEIOOTF7U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:threadPoolPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGGVHH5VP2RA3NBSWE6F4COHET4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:unlockCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYF4OD52OBHS5B2XMSUBN6T3RI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:isInAadc:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHEDVYLZZZBFXPN4FXH2BL2VJPE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::JobQueueItem:threadStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Scheduling::JobQueueItem:UnlockJob-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUGB22UPXAVGK3OJUFICS7IFHDQ"),"UnlockJob",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Scheduling::JobQueueItem:Default-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),"Default",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXIOYUMT4JFEZIH3MI6A3DR64Q"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EOrder.ASC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EOrder.DESC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Scheduling::JobQueueItem:Ready-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltHOGUEK67YNBQVJ3XZLLOGKZUV4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),"Ready",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LRT5K6VJZBQ7LRCPGPWAMTMSE"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or\nnot exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\"/></xsc:Item><xsc:Item><xsc:Sql> q where q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null\n))</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),true,null,true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Scheduling::JobQueueItem:Executing-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltZFW2RYUYJJAE7KG7S3W7UHXRVA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),"Executing",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGYJHYAXDVHDVCRM7RXDWRHRAE"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),true,null,true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Scheduling::JobQueueItem:Waiting-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQ4NNO3O35REUVBLUCLGC72IXJE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),"Waiting",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQLPLW5MWZDHTCFIPCHFFUIRAQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and\nexists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\"/></xsc:Item><xsc:Item><xsc:Sql> q where q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null\n))</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),true,null,true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Scheduling::JobQueueItem:Future-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltN53CPBZTYRATRAUQ5HMNNG5QXE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),"Future",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ZAQ6K3ZEREEPDMD7ZSFTIGQBQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),true,null,true,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::JobQueueItem:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5SU3TPIFWHNRDCJQABIFNQAABA"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::Scheduling::JobQueueItem:General:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(5,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Scheduling::JobQueueItem:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RDV26FFFVAQFKMPGQ2BHZT36Y"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5SU3TPIFWHNRDCJQABIFNQAABA")},null,org.radixware.kernel.server.types.Restrictions.Factory.newInstance(291209,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Scheduling::JobQueueItem:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),"{0}.",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),"{0,date,dd/MM/yyyy H:m:ss}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN3BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(28673,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Scheduling::JobQueueItem:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::JobQueueItem:creatorPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3QEST2453NB6VO2QYXLW6IJTMM"),"creatorPid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:className-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),"className",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:creatorEntityGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKO3GLUKLZVBJ5LB74GGO6QEBKE"),"creatorEntityGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:priority-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),"priority",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),"priorityBoostingSpeed",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:dueTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),"dueTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM7BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:methodName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),"methodName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM3BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:curPriority-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),"curPriority",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:execRequesterId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBOGCPSGVIBAB7JKMSBOY55RGH4"),"execRequesterId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JWPTAAJ7FDZJEN56SDNQJOOX4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREBT7FUR5ZC33DWEGQR2W6M7VE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:scpName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),"scpName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYHXYR73INDJZNSGTFQ7FPAVKY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:taskId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBMKNBRS6MFDOHOCOER7UVUAKY4"),"taskId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:task-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colESHGJVLPDZDOZGTTVYGT4GWHLA"),"task",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEZETUM24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refF6XD46FP6FEWDFSXAX42HAQ2RI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:methodTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),"methodTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWMJGLYY45BZJIBTTLCUVWEBDU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:executorId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUMGEJUNOFGHFAJ7DVSCUCIP2A"),"executorId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:executor-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),"executor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWGLJ3JWAVH6NBUYNFJOP76WIU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refLSE7DXO36FG4DEJGTZWLIASZPY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:processorTitle-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RDV26FFFVAQFKMPGQ2BHZT36Y"),"processorTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKZXA5B5P5BCFMQCBFAQCX7MI4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:executingOn-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),"executingOn",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREKJ3KKVTFHUNIYF2G5CS4T7OA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:systimestamp-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3KM4QRDFJHU5JBJPLEJVKYEWQ"),"systimestamp",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,"DATE",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>systimestamp</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:selfCheckTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4UPMUTPR5DZ5NOZ3N4A232YJA"),"selfCheckTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4C5MHFM6JHOXIRUTRPGVOYZJU"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),"selfCheckTimeStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFV3GUVGAKFDGVG4WQUNXUSNKIQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:aadcMemberId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),"aadcMemberId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3MQH74IK2FEQHDPS2F2EPQ5FQM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:allowRerun-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGWUJPPZSBFFFKCPSF7VX7ZNHA"),"allowRerun",null,org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:threadKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSQ62QBZJ6NBR5HDABNEIOOTF7U"),"threadKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDZOERJHBGJCQ3DLYQAXGEZMGH4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4"),"threadPoolClassGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR4LHPTEENBGRZDCT2QWBU76TNM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:threadPoolPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGGVHH5VP2RA3NBSWE6F4COHET4"),"threadPoolPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVLHKVMNHNBYVNKWJ4FSXY43BA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:unlockCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYF4OD52OBHS5B2XMSUBN6T3RI"),"unlockCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3FAR7OOXVAVRNIHWSUFABAZBQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:isInAadc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHEDVYLZZZBFXPN4FXH2BL2VJPE"),"isInAadc",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobQueueItem:threadStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),"threadStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMARUWVNAVFA7P3DLRGO3M4JME"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::JobQueueItem:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprO2R5FE3DKNDLNOEEEDD5LECXG4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFLEAXLWBBRCT7BU4AMSKHL6ON4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2WL6F2CU6VFMPCENUXLL5HVGHE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFKS5DOEDE5D55LINJBR3Z24AOQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2RPASAS32DNRDISQAAAAAAAAAA"),"awake",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZI7IIJRY6ZEGNCAPWXHFMBD2XU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4D6ME6VX45BYVNJXZPBTAA7KP4"),"onCalcTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNHGFEAQOHJBADPNFEKFM3JIGSI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthF4IMHWK2ENCB5KXBWSQYS3UWOU"),"getCalledClassDef",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Scheduling::JobQueueItem - Desktop Executable*/

/*Radix::Scheduling::JobQueueItem-Entity Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem")
public interface JobQueueItem {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Scheduling.explorer.JobQueueItem.JobQueueItem_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Scheduling.explorer.JobQueueItem.JobQueueItem_DefaultModel )  super.getEntity(i);}
	}





























































































































































	/*Radix::Scheduling::JobQueueItem:processorTitle:processorTitle-Presentation Property*/


	public class ProcessorTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProcessorTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:processorTitle:processorTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:processorTitle:processorTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProcessorTitle getProcessorTitle();
	/*Radix::Scheduling::JobQueueItem:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Scheduling::JobQueueItem:unlockCount:unlockCount-Presentation Property*/


	public class UnlockCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnlockCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:unlockCount:unlockCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:unlockCount:unlockCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnlockCount getUnlockCount();
	/*Radix::Scheduling::JobQueueItem:task:task-Presentation Property*/


	public class Task extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Task(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:task:task")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:task:task")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Task getTask();
	/*Radix::Scheduling::JobQueueItem:threadPoolPid:threadPoolPid-Presentation Property*/


	public class ThreadPoolPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadPoolPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolPid:threadPoolPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolPid:threadPoolPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadPoolPid getThreadPoolPid();
	/*Radix::Scheduling::JobQueueItem:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Scheduling::JobQueueItem:className:className-Presentation Property*/


	public class ClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:className:className")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:className:className")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassName getClassName();
	/*Radix::Scheduling::JobQueueItem:aadcMemberId:aadcMemberId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::Scheduling::JobQueueItem:scpName:scpName-Presentation Property*/


	public class ScpName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ScpName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:scpName:scpName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:scpName:scpName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ScpName getScpName();
	/*Radix::Scheduling::JobQueueItem:allowRerun:allowRerun-Presentation Property*/


	public class AllowRerun extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AllowRerun(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:allowRerun:allowRerun")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:allowRerun:allowRerun")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AllowRerun getAllowRerun();
	/*Radix::Scheduling::JobQueueItem:priority:priority-Presentation Property*/


	public class Priority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Priority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priority:priority")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priority:priority")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Priority getPriority();
	/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:priorityBoostingSpeed-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:priorityBoostingSpeed")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:priorityBoostingSpeed")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PriorityBoostingSpeed getPriorityBoostingSpeed();
	/*Radix::Scheduling::JobQueueItem:dueTime:dueTime-Presentation Property*/


	public class DueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public DueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:dueTime:dueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:dueTime:dueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime();
	/*Radix::Scheduling::JobQueueItem:threadKey:threadKey-Presentation Property*/


	public class ThreadKey extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadKey:threadKey")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadKey:threadKey")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ThreadKey getThreadKey();
	/*Radix::Scheduling::JobQueueItem:systimestamp:systimestamp-Presentation Property*/


	public class Systimestamp extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public Systimestamp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:systimestamp:systimestamp")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:systimestamp:systimestamp")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Systimestamp getSystimestamp();
	/*Radix::Scheduling::JobQueueItem:selfCheckTime:selfCheckTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTime:selfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTime:selfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SelfCheckTime getSelfCheckTime();
	/*Radix::Scheduling::JobQueueItem:methodName:methodName-Presentation Property*/


	public class MethodName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodName:methodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodName:methodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MethodName getMethodName();
	/*Radix::Scheduling::JobQueueItem:curPriority:curPriority-Presentation Property*/


	public class CurPriority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurPriority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:curPriority:curPriority")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:curPriority:curPriority")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurPriority getCurPriority();
	/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:threadPoolClassGuid-Presentation Property*/


	public class ThreadPoolClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadPoolClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolClassGuid:threadPoolClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolClassGuid:threadPoolClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadPoolClassGuid getThreadPoolClassGuid();
	/*Radix::Scheduling::JobQueueItem:executor:executor-Presentation Property*/


	public class Executor extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Executor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executor:executor")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executor:executor")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Executor getExecutor();
	/*Radix::Scheduling::JobQueueItem:executingOn:executingOn-Presentation Property*/


	public class ExecutingOn extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExecutingOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executingOn:executingOn")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executingOn:executingOn")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExecutingOn getExecutingOn();
	/*Radix::Scheduling::JobQueueItem:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Scheduling::JobQueueItem:isInAadc:isInAadc-Presentation Property*/


	public class IsInAadc extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsInAadc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:isInAadc:isInAadc")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:isInAadc:isInAadc")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsInAadc getIsInAadc();
	/*Radix::Scheduling::JobQueueItem:methodTitle:methodTitle-Presentation Property*/


	public class MethodTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MethodTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodTitle:methodTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodTitle:methodTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MethodTitle getMethodTitle();
	/*Radix::Scheduling::JobQueueItem:threadStr:threadStr-Presentation Property*/


	public class ThreadStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadStr:threadStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadStr:threadStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadStr getThreadStr();
	/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:selfCheckTimeStr-Presentation Property*/


	public class SelfCheckTimeStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SelfCheckTimeStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTimeStr:selfCheckTimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTimeStr:selfCheckTimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SelfCheckTimeStr getSelfCheckTimeStr();
	public static class UnlockJob extends org.radixware.kernel.common.client.models.items.Command{
		protected UnlockJob(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Scheduling::JobQueueItem - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem-Entity Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobQueueItem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::JobQueueItem:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
			"Radix::Scheduling::JobQueueItem",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUNXYSS3HB5HE7ATQRCYGUOVIRY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),28673,

			/*Radix::Scheduling::JobQueueItem:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::JobQueueItem:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:className:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),
						"className",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:className:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:priority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),
						"priority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:priority:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),
						"priorityBoostingSpeed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:dueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),
						"dueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM7BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:dueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:methodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),
						"methodName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM3BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:methodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:curPriority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),
						"curPriority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:curPriority:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JWPTAAJ7FDZJEN56SDNQJOOX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREBT7FUR5ZC33DWEGQR2W6M7VE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:scpName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),
						"scpName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYHXYR73INDJZNSGTFQ7FPAVKY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:scpName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLGE5F2GHNDDBCOONBRJ2WF3M4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:task:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colESHGJVLPDZDOZGTTVYGT4GWHLA"),
						"task",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Scheduling::JobQueueItem:methodTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),
						"methodTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWMJGLYY45BZJIBTTLCUVWEBDU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:methodTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:executor:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),
						"executor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWGLJ3JWAVH6NBUYNFJOP76WIU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Scheduling::JobQueueItem:processorTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RDV26FFFVAQFKMPGQ2BHZT36Y"),
						"processorTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKZXA5B5P5BCFMQCBFAQCX7MI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:processorTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SYGCAVKXBCUPGCFLFS3K5D7AQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:executingOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),
						"executingOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREKJ3KKVTFHUNIYF2G5CS4T7OA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:executingOn:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:systimestamp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3KM4QRDFJHU5JBJPLEJVKYEWQ"),
						"systimestamp",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:systimestamp:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:selfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4UPMUTPR5DZ5NOZ3N4A232YJA"),
						"selfCheckTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4C5MHFM6JHOXIRUTRPGVOYZJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:selfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IFJI5PA7BC3VE5ASI3SNQQLSQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),
						"selfCheckTimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFV3GUVGAKFDGVG4WQUNXUSNKIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3MQH74IK2FEQHDPS2F2EPQ5FQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:allowRerun:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGWUJPPZSBFFFKCPSF7VX7ZNHA"),
						"allowRerun",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:allowRerun:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSQ62QBZJ6NBR5HDABNEIOOTF7U"),
						"threadKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDZOERJHBGJCQ3DLYQAXGEZMGH4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadKey:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4"),
						"threadPoolClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR4LHPTEENBGRZDCT2QWBU76TNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadPoolPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGGVHH5VP2RA3NBSWE6F4COHET4"),
						"threadPoolPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVLHKVMNHNBYVNKWJ4FSXY43BA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadPoolPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:unlockCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYF4OD52OBHS5B2XMSUBN6T3RI"),
						"unlockCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3FAR7OOXVAVRNIHWSUFABAZBQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:unlockCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:isInAadc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHEDVYLZZZBFXPN4FXH2BL2VJPE"),
						"isInAadc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:isInAadc:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),
						"threadStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMARUWVNAVFA7P3DLRGO3M4JME"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27C2JOIOZCT5NDIVSEJCHRQ3Y"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::JobQueueItem:UnlockJob-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUGB22UPXAVGK3OJUFICS7IFHDQ"),
						"UnlockJob",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR77D3HURO5D57OTSI676JM4GSA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRGURYMOTERAO3OAQ72IMY67RAE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Ready-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltHOGUEK67YNBQVJ3XZLLOGKZUV4"),
						"Ready",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LRT5K6VJZBQ7LRCPGPWAMTMSE"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or\nnot exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\"/></xsc:Item><xsc:Item><xsc:Sql> q where q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null\n))</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Ready:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Executing-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltZFW2RYUYJJAE7KG7S3W7UHXRVA"),
						"Executing",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGYJHYAXDVHDVCRM7RXDWRHRAE"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Executing:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Waiting-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQ4NNO3O35REUVBLUCLGC72IXJE"),
						"Waiting",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQLPLW5MWZDHTCFIPCHFFUIRAQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and\nexists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\"/></xsc:Item><xsc:Item><xsc:Sql> q where q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null\n))</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Waiting:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Future-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltN53CPBZTYRATRAUQ5HMNNG5QXE"),
						"Future",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ZAQ6K3ZEREEPDMD7ZSFTIGQBQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Future:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::JobQueueItem:Default-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						"Default",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXIOYUMT4JFEZIH3MI6A3DR64Q"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refF6XD46FP6FEWDFSXAX42HAQ2RI"),"JobQueue=>Task (taskId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBMKNBRS6MFDOHOCOER7UVUAKY4")},new String[]{"taskId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refLSE7DXO36FG4DEJGTZWLIASZPY"),"JobQueue=>Unit (executorId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUMGEJUNOFGHFAJ7DVSCUCIP2A")},new String[]{"executorId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5SU3TPIFWHNRDCJQABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA")},
			false,false,false);
}

/* Radix::Scheduling::JobQueueItem - Web Executable*/

/*Radix::Scheduling::JobQueueItem-Entity Class*/

package org.radixware.ads.Scheduling.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem")
public interface JobQueueItem {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Scheduling.web.JobQueueItem.JobQueueItem_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Scheduling.web.JobQueueItem.JobQueueItem_DefaultModel )  super.getEntity(i);}
	}
























































































































































	/*Radix::Scheduling::JobQueueItem:processorTitle:processorTitle-Presentation Property*/


	public class ProcessorTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProcessorTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:processorTitle:processorTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:processorTitle:processorTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProcessorTitle getProcessorTitle();
	/*Radix::Scheduling::JobQueueItem:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Scheduling::JobQueueItem:unlockCount:unlockCount-Presentation Property*/


	public class UnlockCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnlockCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:unlockCount:unlockCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:unlockCount:unlockCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnlockCount getUnlockCount();
	/*Radix::Scheduling::JobQueueItem:threadPoolPid:threadPoolPid-Presentation Property*/


	public class ThreadPoolPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadPoolPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolPid:threadPoolPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolPid:threadPoolPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadPoolPid getThreadPoolPid();
	/*Radix::Scheduling::JobQueueItem:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Scheduling::JobQueueItem:className:className-Presentation Property*/


	public class ClassName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:className:className")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:className:className")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassName getClassName();
	/*Radix::Scheduling::JobQueueItem:aadcMemberId:aadcMemberId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::Scheduling::JobQueueItem:scpName:scpName-Presentation Property*/


	public class ScpName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ScpName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:scpName:scpName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:scpName:scpName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ScpName getScpName();
	/*Radix::Scheduling::JobQueueItem:allowRerun:allowRerun-Presentation Property*/


	public class AllowRerun extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AllowRerun(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:allowRerun:allowRerun")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:allowRerun:allowRerun")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AllowRerun getAllowRerun();
	/*Radix::Scheduling::JobQueueItem:priority:priority-Presentation Property*/


	public class Priority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Priority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priority:priority")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priority:priority")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Priority getPriority();
	/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:priorityBoostingSpeed-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:priorityBoostingSpeed")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:priorityBoostingSpeed")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PriorityBoostingSpeed getPriorityBoostingSpeed();
	/*Radix::Scheduling::JobQueueItem:dueTime:dueTime-Presentation Property*/


	public class DueTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public DueTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:dueTime:dueTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:dueTime:dueTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public DueTime getDueTime();
	/*Radix::Scheduling::JobQueueItem:threadKey:threadKey-Presentation Property*/


	public class ThreadKey extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ThreadKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadKey:threadKey")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadKey:threadKey")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ThreadKey getThreadKey();
	/*Radix::Scheduling::JobQueueItem:systimestamp:systimestamp-Presentation Property*/


	public class Systimestamp extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public Systimestamp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:systimestamp:systimestamp")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:systimestamp:systimestamp")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Systimestamp getSystimestamp();
	/*Radix::Scheduling::JobQueueItem:selfCheckTime:selfCheckTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTime:selfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTime:selfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public SelfCheckTime getSelfCheckTime();
	/*Radix::Scheduling::JobQueueItem:methodName:methodName-Presentation Property*/


	public class MethodName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MethodName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodName:methodName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodName:methodName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MethodName getMethodName();
	/*Radix::Scheduling::JobQueueItem:curPriority:curPriority-Presentation Property*/


	public class CurPriority extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurPriority(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:curPriority:curPriority")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:curPriority:curPriority")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurPriority getCurPriority();
	/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:threadPoolClassGuid-Presentation Property*/


	public class ThreadPoolClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadPoolClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolClassGuid:threadPoolClassGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadPoolClassGuid:threadPoolClassGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadPoolClassGuid getThreadPoolClassGuid();
	/*Radix::Scheduling::JobQueueItem:executor:executor-Presentation Property*/


	public class Executor extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Executor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Unit.Unit_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Unit.Unit_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Unit.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Unit.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executor:executor")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executor:executor")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Executor getExecutor();
	/*Radix::Scheduling::JobQueueItem:executingOn:executingOn-Presentation Property*/


	public class ExecutingOn extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExecutingOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executingOn:executingOn")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:executingOn:executingOn")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExecutingOn getExecutingOn();
	/*Radix::Scheduling::JobQueueItem:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Scheduling::JobQueueItem:isInAadc:isInAadc-Presentation Property*/


	public class IsInAadc extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsInAadc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:isInAadc:isInAadc")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:isInAadc:isInAadc")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsInAadc getIsInAadc();
	/*Radix::Scheduling::JobQueueItem:methodTitle:methodTitle-Presentation Property*/


	public class MethodTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MethodTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodTitle:methodTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:methodTitle:methodTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MethodTitle getMethodTitle();
	/*Radix::Scheduling::JobQueueItem:threadStr:threadStr-Presentation Property*/


	public class ThreadStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ThreadStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadStr:threadStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:threadStr:threadStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ThreadStr getThreadStr();
	/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:selfCheckTimeStr-Presentation Property*/


	public class SelfCheckTimeStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SelfCheckTimeStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTimeStr:selfCheckTimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:selfCheckTimeStr:selfCheckTimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SelfCheckTimeStr getSelfCheckTimeStr();
	public static class UnlockJob extends org.radixware.kernel.common.client.models.items.Command{
		protected UnlockJob(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Scheduling::JobQueueItem - Web Meta*/

/*Radix::Scheduling::JobQueueItem-Entity Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobQueueItem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::JobQueueItem:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
			"Radix::Scheduling::JobQueueItem",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgUNXYSS3HB5HE7ATQRCYGUOVIRY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),28673,

			/*Radix::Scheduling::JobQueueItem:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::JobQueueItem:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:className:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),
						"className",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:className:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:priority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),
						"priority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:priority:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),
						"priorityBoostingSpeed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:priorityBoostingSpeed:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:dueTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),
						"dueTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM7BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:dueTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:methodName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),
						"methodName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM3BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:methodName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:curPriority:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),
						"curPriority",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:curPriority:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JWPTAAJ7FDZJEN56SDNQJOOX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREBT7FUR5ZC33DWEGQR2W6M7VE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:scpName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),
						"scpName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYHXYR73INDJZNSGTFQ7FPAVKY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:scpName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLGE5F2GHNDDBCOONBRJ2WF3M4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:task:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colESHGJVLPDZDOZGTTVYGT4GWHLA"),
						"task",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
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

					/*Radix::Scheduling::JobQueueItem:methodTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),
						"methodTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWMJGLYY45BZJIBTTLCUVWEBDU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:methodTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:executor:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),
						"executor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWGLJ3JWAVH6NBUYNFJOP76WIU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Scheduling::JobQueueItem:processorTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RDV26FFFVAQFKMPGQ2BHZT36Y"),
						"processorTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKZXA5B5P5BCFMQCBFAQCX7MI4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:processorTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SYGCAVKXBCUPGCFLFS3K5D7AQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:executingOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),
						"executingOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREKJ3KKVTFHUNIYF2G5CS4T7OA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:executingOn:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:systimestamp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3KM4QRDFJHU5JBJPLEJVKYEWQ"),
						"systimestamp",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:systimestamp:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:selfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colU4UPMUTPR5DZ5NOZ3N4A232YJA"),
						"selfCheckTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4C5MHFM6JHOXIRUTRPGVOYZJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:selfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IFJI5PA7BC3VE5ASI3SNQQLSQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),
						"selfCheckTimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFV3GUVGAKFDGVG4WQUNXUSNKIQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:selfCheckTimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3MQH74IK2FEQHDPS2F2EPQ5FQM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:allowRerun:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKGWUJPPZSBFFFKCPSF7VX7ZNHA"),
						"allowRerun",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:allowRerun:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSQ62QBZJ6NBR5HDABNEIOOTF7U"),
						"threadKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDZOERJHBGJCQ3DLYQAXGEZMGH4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadKey:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4"),
						"threadPoolClassGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR4LHPTEENBGRZDCT2QWBU76TNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadPoolClassGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadPoolPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGGVHH5VP2RA3NBSWE6F4COHET4"),
						"threadPoolPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVLHKVMNHNBYVNKWJ4FSXY43BA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadPoolPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:unlockCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAYF4OD52OBHS5B2XMSUBN6T3RI"),
						"unlockCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3FAR7OOXVAVRNIHWSUFABAZBQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:unlockCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:isInAadc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHEDVYLZZZBFXPN4FXH2BL2VJPE"),
						"isInAadc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:isInAadc:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::JobQueueItem:threadStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),
						"threadStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMARUWVNAVFA7P3DLRGO3M4JME"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::JobQueueItem:threadStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27C2JOIOZCT5NDIVSEJCHRQ3Y"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Scheduling::JobQueueItem:UnlockJob-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUGB22UPXAVGK3OJUFICS7IFHDQ"),
						"UnlockJob",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR77D3HURO5D57OTSI676JM4GSA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRGURYMOTERAO3OAQ72IMY67RAE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Ready-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltHOGUEK67YNBQVJ3XZLLOGKZUV4"),
						"Ready",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LRT5K6VJZBQ7LRCPGPWAMTMSE"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null or\nnot exists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\"/></xsc:Item><xsc:Item><xsc:Sql> q where q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null\n))</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Ready:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Executing-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltZFW2RYUYJJAE7KG7S3W7UHXRVA"),
						"Executing",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGYJHYAXDVHDVCRM7RXDWRHRAE"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Executing:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Waiting-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQ4NNO3O35REUVBLUCLGC72IXJE"),
						"Waiting",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQLPLW5MWZDHTCFIPCHFFUIRAQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> &lt;= sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null\nand \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and\nexists (\nselect 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\"/></xsc:Item><xsc:Item><xsc:Sql> q where q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colVTHNRM2E6ZHQ3CN4YNPTZZ7VJ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colGGVHH5VP2RA3NBSWE6F4COHET4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colSQ62QBZJ6NBR5HDABNEIOOTF7U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>\nand q.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null\n))</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Waiting:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Scheduling::JobQueueItem:Future-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltN53CPBZTYRATRAUQ5HMNNG5QXE"),
						"Future",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ZAQ6K3ZEREEPDMD7ZSFTIGQBQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"colPSF7WRUMIHNRDJIEACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > sysdate and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecHRWUWEEMIHNRDJIEACQMTAIZT4\" PropId=\"col2RDV26FFFVAQFKMPGQ2BHZT36Y\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						null,

						/*Radix::Scheduling::JobQueueItem:Future:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Scheduling::JobQueueItem:Default-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
						"Default",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXIOYUMT4JFEZIH3MI6A3DR64Q"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),
								false),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),
								true)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refF6XD46FP6FEWDFSXAX42HAQ2RI"),"JobQueue=>Task (taskId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBMKNBRS6MFDOHOCOER7UVUAKY4")},new String[]{"taskId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refLSE7DXO36FG4DEJGTZWLIASZPY"),"JobQueue=>Unit (executorId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colOUMGEJUNOFGHFAJ7DVSCUCIP2A")},new String[]{"executorId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			null,
			false,false,false);
}

/* Radix::Scheduling::JobQueueItem:General - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:General-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5SU3TPIFWHNRDCJQABIFNQAABA"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),
	null,
	null,

	/*Radix::Scheduling::JobQueueItem:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::JobQueueItem:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCXIJOWOTBXOBDNTPAAMPGXSZKU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEEXC6D7HLFAQ5F5LFLCUIUEMOY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),1,6,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),1,5,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colESHGJVLPDZDOZGTTVYGT4GWHLA"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),1,4,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),1,3,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),0,11,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),0,12,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),0,8,2,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCXIJOWOTBXOBDNTPAAMPGXSZKU"))}
	,

	/*Radix::Scheduling::JobQueueItem:General:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	5,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Scheduling::JobQueueItem:General:Model - Desktop Executable*/

/*Radix::Scheduling::JobQueueItem:General:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:General:Model")
public class General:Model  extends org.radixware.ads.Scheduling.explorer.JobQueueItem.JobQueueItem_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:General:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:General:Model:Properties-Properties*/

	/*Radix::Scheduling::JobQueueItem:General:Model:Methods-Methods*/

	/*Radix::Scheduling::JobQueueItem:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:General:Model:afterRead")
	protected published  void afterRead () {
		executor.setVisible(executor.Value != null);
		aadcMemberId.setVisible(isInAadc.getValue().booleanValue() || aadcMemberId.getValue() != null);
	}


}

/* Radix::Scheduling::JobQueueItem:General:Model - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:General:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem5SU3TPIFWHNRDCJQABIFNQAABA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:General:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:General - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:General-Selector Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHRWUWEEMIHNRDJIEACQMTAIZT4"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtFFBLJWMYVJEBTG3TTQR2X2GOOQ"),
		null,
		false,
		true,
		null,
		291209,
		null,
		2192,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr5SU3TPIFWHNRDCJQABIFNQAABA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAQIIHUMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGYABEOPPRBD53G55ZT4ZW24ZIQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFA74CNUCAZHGXKVGXRSBQ3CJUY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHCKW54UMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIRU64EYOOVA6DNVJKYWZDVC5NY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUA2AEBUNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUDYWH5FGQZFJJG3AJRY3U4G5EA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHQPRKNPYGZFVRPPKUDNLNLD5G4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOB766EUNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJ766EUNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUC47CLMNIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSF7WRUMIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5ZY2POKPRDRVK3IC2OJNUZNZU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDSYX5TZS7JBYHKPQMZRQGAI7NE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXUWKRTM22REK7MAYU6RKJTOPYM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RDV26FFFVAQFKMPGQ2BHZT36Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWHBA3EDJLBF5ZAAYDNYL2QIU2Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.JobQueueItem.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Scheduling::JobQueueItem:Ready:Model - Desktop Executable*/

/*Radix::Scheduling::JobQueueItem:Ready:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Ready:Model")
public class Ready:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Ready:Model_mi.rdxMeta; }



	public Ready:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Ready:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Ready:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Ready:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Ready:Model - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:Ready:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Ready:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcHOGUEK67YNBQVJ3XZLLOGKZUV4"),
						"Ready:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Ready:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Ready:Model - Web Executable*/

/*Radix::Scheduling::JobQueueItem:Ready:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Ready:Model")
public class Ready:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Ready:Model_mi.rdxMeta; }



	public Ready:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Ready:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Ready:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Ready:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Ready:Model - Web Meta*/

/*Radix::Scheduling::JobQueueItem:Ready:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Ready:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcHOGUEK67YNBQVJ3XZLLOGKZUV4"),
						"Ready:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Ready:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Executing:Model - Desktop Executable*/

/*Radix::Scheduling::JobQueueItem:Executing:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Executing:Model")
public class Executing:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Executing:Model_mi.rdxMeta; }



	public Executing:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Executing:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Executing:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Executing:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Executing:Model - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:Executing:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Executing:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcZFW2RYUYJJAE7KG7S3W7UHXRVA"),
						"Executing:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Executing:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Executing:Model - Web Executable*/

/*Radix::Scheduling::JobQueueItem:Executing:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Executing:Model")
public class Executing:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Executing:Model_mi.rdxMeta; }



	public Executing:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Executing:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Executing:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Executing:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Executing:Model - Web Meta*/

/*Radix::Scheduling::JobQueueItem:Executing:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Executing:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcZFW2RYUYJJAE7KG7S3W7UHXRVA"),
						"Executing:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Executing:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Waiting:Model - Desktop Executable*/

/*Radix::Scheduling::JobQueueItem:Waiting:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Waiting:Model")
public class Waiting:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Waiting:Model_mi.rdxMeta; }



	public Waiting:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Waiting:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Waiting:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Waiting:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Waiting:Model - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:Waiting:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Waiting:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcQ4NNO3O35REUVBLUCLGC72IXJE"),
						"Waiting:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Waiting:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Waiting:Model - Web Executable*/

/*Radix::Scheduling::JobQueueItem:Waiting:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Waiting:Model")
public class Waiting:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Waiting:Model_mi.rdxMeta; }



	public Waiting:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Waiting:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Waiting:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Waiting:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Waiting:Model - Web Meta*/

/*Radix::Scheduling::JobQueueItem:Waiting:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Waiting:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcQ4NNO3O35REUVBLUCLGC72IXJE"),
						"Waiting:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Waiting:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Future:Model - Desktop Executable*/

/*Radix::Scheduling::JobQueueItem:Future:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Future:Model")
public class Future:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Future:Model_mi.rdxMeta; }



	public Future:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Future:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Future:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Future:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Future:Model - Desktop Meta*/

/*Radix::Scheduling::JobQueueItem:Future:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Future:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcN53CPBZTYRATRAUQ5HMNNG5QXE"),
						"Future:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Future:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem:Future:Model - Web Executable*/

/*Radix::Scheduling::JobQueueItem:Future:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueueItem:Future:Model")
public class Future:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Future:Model_mi.rdxMeta; }



	public Future:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Scheduling::JobQueueItem:Future:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueueItem:Future:Model:Properties-Properties*/






	/*Radix::Scheduling::JobQueueItem:Future:Model:Methods-Methods*/


}

/* Radix::Scheduling::JobQueueItem:Future:Model - Web Meta*/

/*Radix::Scheduling::JobQueueItem:Future:Model-Filter Model Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Future:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcN53CPBZTYRATRAUQ5HMNNG5QXE"),
						"Future:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::JobQueueItem:Future:Model:Properties-Properties*/
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

/* Radix::Scheduling::JobQueueItem - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobQueueItem - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя класса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JWPTAAJ7FDZJEN56SDNQJOOX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Execute at AADC member");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполнить на AADC узле");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3MQH74IK2FEQHDPS2F2EPQ5FQM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Future");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запланированы на будущее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3ZAQ6K3ZEREEPDMD7ZSFTIGQBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not yet started>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<еще не запущено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4IFJI5PA7BC3VE5ASI3SNQQLSQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<waiting for execution>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ожидает выполнения>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SYGCAVKXBCUPGCFLFS3K5D7AQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not yet started>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<еще не запущено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5BLEEUJHNJEC5J4TJA2S5UHJH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Ready");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Готовы к исполнению");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LRT5K6VJZBQ7LRCPGPWAMTMSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<being executed by %s>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<выполняется на %s>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB3NZD7OCWRBWTD3JNJKFNMLAU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Executor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполнитель");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWGLJ3JWAVH6NBUYNFJOP76WIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLGE5F2GHNDDBCOONBRJ2WF3M4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service\'s client profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль клиента сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDYHXYR73INDJZNSGTFQ7FPAVKY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread key");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ потока");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDZOERJHBGJCQ3DLYQAXGEZMGH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Numbe of execution attempts");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество попыток исполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3FAR7OOXVAVRNIHWSUFABAZBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEEXC6D7HLFAQ5F5LFLCUIUEMOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last self-check time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней самопроверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFV3GUVGAKFDGVG4WQUNXUSNKIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not set up for execution yet>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<еще не подготовлена к выполнению>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIFU42VUZ6RAYNGJWPX66UDGIB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Executing on");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполняется на");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKZXA5B5P5BCFMQCBFAQCX7MI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK27C2JOIOZCT5NDIVSEJCHRQ3Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<ready for execution>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<готова к выполнению>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKMJZOE6UKNBDVGYW5URUPXUOEU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Job");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7BFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Waiting");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"В ожидании");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLQLPLW5MWZDHTCFIPCHFFUIRAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Method name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя метода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWMJGLYY45BZJIBTTLCUVWEBDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Method");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метод");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM3BFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Плановое время");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM7BFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Jobs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current priority");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущий приоритет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMXBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задано>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN3BFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Priority boosting rate");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Скорость повышения приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNHBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Priority");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приоритет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNLBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNPBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNXBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Self-check time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время самопроверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO4C5MHFM6JHOXIRUTRPGVOYZJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread pool class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс пула потоков");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR4LHPTEENBGRZDCT2QWBU76TNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Make job available for execution");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сделать задание снова доступным для выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR77D3HURO5D57OTSI676JM4GSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREBT7FUR5ZC33DWEGQR2W6M7VE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time left before execution");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Осталось до начала выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREKJ3KKVTFHUNIYF2G5CS4T7OA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Поток");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRMARUWVNAVFA7P3DLRGO3M4JME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By due time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По времени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXIOYUMT4JFEZIH3MI6A3DR64Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ago");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"назад");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7QJZKDKE5A7ZDQRX7CVAWEGFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Executing");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполняются");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWGYJHYAXDVHDVCRM7RXDWRHRAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Thread pool identifier");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор пула потоков");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVLHKVMNHNBYVNKWJ4FSXY43BA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(JobQueueItem - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecHRWUWEEMIHNRDJIEACQMTAIZT4"),"JobQueueItem - Localizing Bundle",$$$items$$$);
}
