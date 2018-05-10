
/* Radix::Scheduling::JobBuilder - Server Executable*/

/*Radix::Scheduling::JobBuilder-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder")
public published class JobBuilder  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return JobBuilder_mi.rdxMeta;}

	/*Radix::Scheduling::JobBuilder:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobBuilder:Properties-Properties*/

	/*Radix::Scheduling::JobBuilder:title-Dynamic Property*/



	protected Str title=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:title")
	private final  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:title")
	private final   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Scheduling::JobBuilder:aadcMemberId-Dynamic Property*/



	protected Int aadcMemberId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:aadcMemberId")
	private final  Int getAadcMemberId() {
		return aadcMemberId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:aadcMemberId")
	private final   void setAadcMemberId(Int val) {
		aadcMemberId = val;
	}

	/*Radix::Scheduling::JobBuilder:boosting-Dynamic Property*/



	protected Int boosting=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:boosting")
	private final  Int getBoosting() {
		return boosting;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:boosting")
	private final   void setBoosting(Int val) {
		boosting = val;
	}

	/*Radix::Scheduling::JobBuilder:className-Dynamic Property*/



	protected Str className=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:className")
	private final  Str getClassName() {
		return className;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:className")
	private final   void setClassName(Str val) {
		className = val;
	}

	/*Radix::Scheduling::JobBuilder:creator-Dynamic Property*/



	protected org.radixware.ads.Types.server.Entity creator=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:creator")
	private final  org.radixware.ads.Types.server.Entity getCreator() {
		return creator;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:creator")
	private final   void setCreator(org.radixware.ads.Types.server.Entity val) {
		creator = val;
	}

	/*Radix::Scheduling::JobBuilder:delayMillis-Dynamic Property*/



	protected Int delayMillis=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:delayMillis")
	private final  Int getDelayMillis() {
		return delayMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:delayMillis")
	private final   void setDelayMillis(Int val) {
		delayMillis = val;
	}

	/*Radix::Scheduling::JobBuilder:methodId-Dynamic Property*/



	protected Str methodId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:methodId")
	private final  Str getMethodId() {
		return methodId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:methodId")
	private final   void setMethodId(Str val) {
		methodId = val;
	}

	/*Radix::Scheduling::JobBuilder:dueTime-Dynamic Property*/



	protected java.sql.Timestamp dueTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:dueTime")
	private final  java.sql.Timestamp getDueTime() {
		return dueTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:dueTime")
	private final   void setDueTime(java.sql.Timestamp val) {
		dueTime = val;
	}

	/*Radix::Scheduling::JobBuilder:params-Dynamic Property*/



	protected org.radixware.kernel.server.arte.JobQueue.Param[] params=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:params")
	private final  org.radixware.kernel.server.arte.JobQueue.Param[] getParams() {
		return params;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:params")
	private final   void setParams(org.radixware.kernel.server.arte.JobQueue.Param[] val) {
		params = val;
	}

	/*Radix::Scheduling::JobBuilder:priority-Dynamic Property*/



	protected Int priority=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:priority")
	private final  Int getPriority() {
		return priority;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:priority")
	private final   void setPriority(Int val) {
		priority = val;
	}

	/*Radix::Scheduling::JobBuilder:scpName-Dynamic Property*/



	protected Str scpName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:scpName")
	private final  Str getScpName() {
		return scpName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:scpName")
	private final   void setScpName(Str val) {
		scpName = val;
	}

	/*Radix::Scheduling::JobBuilder:taskId-Dynamic Property*/



	protected Int taskId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:taskId")
	private final  Int getTaskId() {
		return taskId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:taskId")
	private final   void setTaskId(Int val) {
		taskId = val;
	}

	/*Radix::Scheduling::JobBuilder:threadKey-Dynamic Property*/



	protected Int threadKey=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:threadKey")
	private final  Int getThreadKey() {
		return threadKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:threadKey")
	private final   void setThreadKey(Int val) {
		threadKey = val;
	}

	/*Radix::Scheduling::JobBuilder:theadPoolClassGuid-Dynamic Property*/



	protected Str theadPoolClassGuid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:theadPoolClassGuid")
	private final  Str getTheadPoolClassGuid() {
		return theadPoolClassGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:theadPoolClassGuid")
	private final   void setTheadPoolClassGuid(Str val) {
		theadPoolClassGuid = val;
	}

	/*Radix::Scheduling::JobBuilder:threadPoolPid-Dynamic Property*/



	protected Str threadPoolPid=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:threadPoolPid")
	private final  Str getThreadPoolPid() {
		return threadPoolPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:threadPoolPid")
	private final   void setThreadPoolPid(Str val) {
		threadPoolPid = val;
	}

	/*Radix::Scheduling::JobBuilder:allowRerun-Dynamic Property*/



	protected boolean allowRerun=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:allowRerun")
	private final  boolean getAllowRerun() {
		return allowRerun;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:allowRerun")
	private final   void setAllowRerun(boolean val) {
		allowRerun = val;
	}





























































































































	/*Radix::Scheduling::JobBuilder:Methods-Methods*/

	/*Radix::Scheduling::JobBuilder:JobBuilder-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:JobBuilder")
	public published  JobBuilder (boolean allowRerun) {
		allowRerun = allowRerun;
	}

	/*Radix::Scheduling::JobBuilder:setClassName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setClassName")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setClassName (Str className) {
		className = className;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setMethodId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setMethodId")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setMethodId (Str methodId) {
		methodId = methodId;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setBoosting-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setBoosting")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setBoosting (Int boosting) {
		boosting = boosting;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setDelayMillis-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setDelayMillis")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setDelayMillis (Int delayMillis) {
		if (delayMillis != null && dueTime != null) {
		    throw new IllegalArgumentException("Trying to set delayMillis when dueTime is not null, only one of them should be defined");
		}
		delayMillis = delayMillis;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setAadcMemberId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setAadcMemberId")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setAadcMemberId (Int taskId) {
		taskId = taskId;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setTaskId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setTaskId")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setTaskId (Int taskId) {
		taskId = taskId;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setPriority-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setPriority")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setPriority (Int priority) {
		priority = priority;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setDueTime-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setDueTime")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setDueTime (java.sql.Timestamp dueTime) {
		if (dueTime != null && delayMillis != null) {
		    throw new IllegalArgumentException("Trying to set dueTime when delayMillis is not null, only one of them should be defined");
		}
		dueTime = dueTime;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setThreadKey-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setThreadKey")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setThreadKey (Int threadKey) {
		threadKey = threadKey;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setThreadPoolPid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setThreadPoolPid")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setThreadPoolPid (Str threadPoolPid) {
		threadPoolPid = threadPoolPid;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setThreadPoolClassGuid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setThreadPoolClassGuid")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setThreadPoolClassGuid (Str threadPoolClassGuid) {
		theadPoolClassGuid = threadPoolClassGuid;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setParams-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setParams")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setParams (org.radixware.kernel.server.arte.JobQueue.Param[] params) {
		params = params;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setTitle")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setTitle (Str title) {
		title = title;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setScpName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setScpName")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setScpName (Str scpName) {
		scpName = scpName;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:setCreator-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:setCreator")
	public published  org.radixware.ads.Scheduling.server.JobBuilder setCreator (org.radixware.ads.Types.server.Entity creator) {
		creator = creator;
		return this;
	}

	/*Radix::Scheduling::JobBuilder:createJob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobBuilder:createJob")
	public published  org.radixware.ads.Scheduling.server.JobQueueItem createJob () {
		if (delayMillis != null) {
		    return (JobQueueItem) Arte::Arte.getInstance().getJobQueue().scheduleRelative(
		            title,
		            creator == null ? null : creator.getPid(),
		            delayMillis.longValue(),
		            className,
		            methodId,
		            params,
		            scpName,
		            priority,
		            boosting,
		            taskId,
		            allowRerun,
		            aadcMemberId == null ? null : aadcMemberId.intValue(),
		            theadPoolClassGuid,
		            threadPoolPid,
		            threadKey == null ? null : threadKey.intValue()
		    );
		} else {
		    return (JobQueueItem) Arte::Arte.getInstance().getJobQueue().schedule(
		            title,
		            creator == null ? null : creator.getPid(),
		            dueTime,
		            className,
		            methodId,
		            params,
		            scpName,
		            priority,
		            boosting,
		            taskId,
		            allowRerun,
		            aadcMemberId == null ? null : aadcMemberId.intValue(),
		            theadPoolClassGuid,
		            threadPoolPid,
		            threadKey == null ? null : threadKey.intValue()
		    );
		}
	}


}

/* Radix::Scheduling::JobBuilder - Server Meta*/

/*Radix::Scheduling::JobBuilder-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobBuilder_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcJGT5FO6C2FGSVBU3DMQLPF24LU"),"JobBuilder",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Scheduling::JobBuilder:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::JobBuilder:title-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdWG5LFVAVCRDBPLZ7Y6UVDIKB6M"),"title",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:aadcMemberId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLF5XETXWLVBGJKCZ34YKE6QMFQ"),"aadcMemberId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:boosting-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd626JGJBFKJCHPK5WUAWWAKL6JY"),"boosting",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:className-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXGFVNVNPYVDXLC46LXSXELROSY"),"className",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:creator-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCKE35J63TNCT3DR6STEV3TASUM"),"creator",null,org.radixware.kernel.common.enums.EValType.USER_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:delayMillis-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSGC3FKTNN5BFRKWOAVSSADIII4"),"delayMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:methodId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFSZDZGFWVVH7NODVGW2NYX4XJQ"),"methodId",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:dueTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5OYZO3MEX5H4BKC4FYUOL2BV4E"),"dueTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:params-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdT2TETBCESJB53OQYNQH4ZMOH5Y"),"params",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:priority-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7ZA5MBWNORBJZNGICFUULJMAPA"),"priority",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:scpName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDDGALYNEBACHHQQ6XQFIY332Q"),"scpName",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:taskId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7ME24N4GPBAHZP3MJR6JYAYATQ"),"taskId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:threadKey-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd33VOEOC2VNDQHI5NIZX3XSPYJA"),"threadKey",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:theadPoolClassGuid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYVB4JYJ5YRGL3JZ2IKDZV5SJ6A"),"theadPoolClassGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:threadPoolPid-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6JDQDAWUONCS5JJNZJEWQUZSYU"),"threadPoolPid",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::JobBuilder:allowRerun-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRPP3SAMEX5H3BMEBXVCEZZSXCU"),"allowRerun",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::JobBuilder:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCJXXHGZHLJDILIQKFFHWCGK6NI"),"JobBuilder",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKE7C6LPJOJAY3PZ5UMOVMEFIVU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQRI576S4IVCZFAVSSK5ON72WMY"),"setClassName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTDBFXGZBQFHD5KOHE6Z5HEA64A"),"setMethodId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZJTF7QUJAFENFGS6ITRO3TDVIE"),"setBoosting",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRT7GKFSFCRHMNLTHIHD62WCP5Q"),"setDelayMillis",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delayMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWZK63FGPMRCBLBHTWRBPM444CM"),"setAadcMemberId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY27H57LHABB3XH3CUSUVCHSLGY"),"setTaskId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAWI4IKIGKZFLNIEZLNFDSZB4BE"),"setPriority",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3H4GMISLEZGELJJ3WA6HST3QYE"),"setDueTime",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("dueTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPAY65JENCNHKDNPYJICOY7TKXI"),"setThreadKey",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("threadKey",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLRHLYLZKBZBYTE3HKRJ4CZ66RE"),"setThreadPoolPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("threadPoolPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthA6VZEPTIKVG3FOHAQ2WD664EFA"),"setThreadPoolClassGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("threadPoolClassGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPJODY7PNWBA25C2YFAHPBZ3CDY"),"setParams",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLSGPXAWRZ5EKNBVVMHHXJS2QII"),"setTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFEWYWM45RZBDZB2RVWIDWBXHJQ"),"setScpName",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5QDKQK2Y25C67HUQAGLTAOZGXU"),"setCreator",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIYKWTCUPFCPVCRYLQ6DV7TC7I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVJRWJ75RVA4JEVZOVCANMQAMQ"),"createJob",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}
