
/* Radix::Scheduling::JobQueue - Server Executable*/

/*Radix::Scheduling::JobQueue-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue")
public final published class JobQueue  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return JobQueue_mi.rdxMeta;}

	/*Radix::Scheduling::JobQueue:Nested classes-Nested Classes*/

	/*Radix::Scheduling::JobQueue:Properties-Properties*/





























	/*Radix::Scheduling::JobQueue:Methods-Methods*/

	/*Radix::Scheduling::JobQueue:scheduleRelative-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:scheduleRelative")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem scheduleRelative (Str title, org.radixware.ads.Types.server.Entity creator, Int delayMillis, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Str scpName, Int priority, Int boosting, Int taskId, boolean allowRerun) {
		return (JobQueueItem) Arte::Arte.getInstance().JobQueue.scheduleRelative(
		        title,
		        creator == null ? null : creator.getPid(),
		        delayMillis == null || delayMillis.longValue() < 0 ? 0 : delayMillis.longValue(),
		        className,
		        methodId,
		        params,
		        scpName,
		        priority,
		        boosting, 
		        taskId,
		        allowRerun);
	}

	/*Radix::Scheduling::JobQueue:scheduleRelative-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:scheduleRelative")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem scheduleRelative (Str title, org.radixware.ads.Types.server.Entity creator, Int delayMillis, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Str scpName, Int priority, Int boosting, boolean allowRerun) {
		return scheduleRelative(
		        title,
		        creator,
		        delayMillis,
		        className,
		        methodId,
		        params,
		        scpName,
		        priority,
		        boosting,
		        null,
		        allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, Num delay, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Int priority, Int boosting, boolean allowRerun) {
		return (JobQueueItem) org.radixware.kernel.server.arte.JobQueue.schedule(Arte::Arte.getInstance(), title, creator==null?null:creator.getPid(), delay.doubleValue(), className, methodId, params, priority, boosting, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, java.sql.Timestamp time, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Str scpName, Int priority, Int boosting, Int taskId, boolean allowRerun) {
		return (JobQueueItem) Arte::Arte.getInstance().JobQueue.schedule(title, creator == null ? null : creator.getPid(), time, className, methodId, params, scpName, priority, boosting, taskId, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, java.sql.Timestamp time, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Int priority, Int boosting, Int taskId, boolean allowRerun) {
		return (JobQueueItem) Arte::Arte.getInstance().JobQueue.schedule(title, creator==null?null:creator.getPid(), time, className, methodId, params, priority, boosting, taskId, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, java.sql.Timestamp time, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Int priority, Int boosting, boolean allowRerun) {
		return schedule(
		    title,
		    creator,
		    time,
		    className,
		    methodId,
		    params,
		    priority,
		    boosting,
		    null,
		    allowRerun
		);
	}

	/*Radix::Scheduling::JobQueue:post-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:post")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem post (Str title, org.radixware.ads.Types.server.Entity creator, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Int priority, Int boosting, boolean allowRerun) {
		return post(
		        title,
		        creator,
		        className,
		        methodId,
		        params,
		        null,
		        priority,
		        boosting,
		        allowRerun);

	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Int priority, Int boosting, Int taskId, boolean allowRerun) {
		return (JobQueueItem) Arte::Arte.getInstance().JobQueue.schedule(title, creator==null?null:creator.getPid(), null, className, methodId, params, priority, boosting, taskId, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Int priority, Int boosting, boolean allowRerun) {
		return schedule(
		    title,
		    creator, 
		    className, 
		    methodId,
		    params,
		    priority,
		    boosting,
		    null,
		    allowRerun
		);
	}

	/*Radix::Scheduling::JobQueue:unsheduleJob-Algorithm Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:unsheduleJob")
	public static published  void unsheduleJob (Int jobId) {
		Arte::Arte.getInstance().JobQueue.unsheduleJob(jobId);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, Num delay, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, org.radixware.kernel.common.enums.EPriority priority, Int boosting, boolean allowRerun) {
		return schedule(title, creator, delay, className, methodId, params, priority.getValue(), boosting, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, java.sql.Timestamp time, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, org.radixware.kernel.common.enums.EPriority priority, Int boosting, boolean allowRerun) {
		return schedule(title, creator, time, className, methodId, params, priority.getValue(), boosting, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:schedule-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:schedule")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem schedule (Str title, org.radixware.ads.Types.server.Entity creator, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, org.radixware.kernel.common.enums.EPriority priority, Int boosting, boolean allowRerun) {
		return schedule(title, creator, className, methodId, params, priority.getValue(), boosting, allowRerun);
	}

	/*Radix::Scheduling::JobQueue:post-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:post")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem post (Str title, org.radixware.ads.Types.server.Entity creator, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, Str scpName, Int priority, Int boosting, boolean allowRerun) {
		return schedule(
		    title,
		    creator,
		    null,
		    className,
		    methodId,
		    params,
		    scpName,
		    priority,
		    boosting,
		    null,
		    allowRerun
		);
	}

	/*Radix::Scheduling::JobQueue:post-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:post")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem post (Str title, org.radixware.ads.Types.server.Entity creator, Str className, Str methodId, org.radixware.kernel.server.arte.JobQueue.Param[] params, org.radixware.kernel.common.enums.EPriority priority, Int boosting, boolean allowRerun) {
		return post(
		        title,
		        creator,
		        className,
		        methodId,
		        params,
		        priority.getValue(),
		        boosting,
		        allowRerun);
	}

	/*Radix::Scheduling::JobQueue:getCurrentJob-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::JobQueue:getCurrentJob")
	public static published  org.radixware.ads.Scheduling.server.JobQueueItem getCurrentJob () {
		final Int jobId = Arte::Arte.getInstance().getJobQueue().CurrentJobId;
		if (jobId != null) {
		    return JobQueueItem.loadByPK(jobId, true);
		}
		return null;
	}


}

/* Radix::Scheduling::JobQueue - Server Meta*/

/*Radix::Scheduling::JobQueue-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobQueue_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcKHAQBRKV2DNRDISQAAAAAAAAAA"),"JobQueue",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Scheduling::JobQueue:Properties-Properties*/
						null,

						/*Radix::Scheduling::JobQueue:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3JUWKB7455HI3DSWPLLPEEL4S4"),"scheduleRelative",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBGNACFLOLBDHHMWIEQCT7VMCEI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNARLGOLCTNCCDGBHB7IADWH6MQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delayMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJPRDVXCANFCXRJGAM7V36TWOS4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVYMPICQDDBBAXKIPXI2BCJL7NI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5UKEMCMMFFGWRNIJOH43BZGVRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOSMYP34QZBM3ESQ3MUOTGLGUI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKLN7KGW6TNB2BE4CC5M2JMCFRU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY6BRW4M275AWXNES47BPEQICKQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPC7B7CPA7ZEKVIJPM6SZV2LV74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXV35RB4Q7ZCPVNNJHH6ZQAZA4A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr25VHA6QVBVAEPCKW7PQLJQYPF4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXLL22T2ZTJC5FF2QPNEDIICKIM"),"scheduleRelative",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMCI6ANHJGZCNRL2JRYNQOW5KRI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRDBP5N3WT5AODDP5KS46FVJLJM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delayMillis",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB5RZL5DNFZFXLBRH3KASXADX4U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFBFTACE5WJDBDFQAHJDW7WJQII")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr374F5X72CRE5VHCOPK4BVUJVYI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUIJT34C3SBFJRDC7MDCG7QMWNU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBDGSUWIFZNCRBOEQNHGPJ64WCQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4FXJ54QYA5DEBHFZKNJ3S6RC7M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC6HEKKGMPBBNBFGILBM7UXQXR4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYGJGMMX26NEPNEGE5FSY33XNAQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAHH2D735R3OBDCWOAAMPGXSZKU"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB2BU24SINZC2VL2SZICNNI4V5E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprROTB2D33HFHZVHLXRAKC2SZBMI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delay",org.radixware.kernel.common.enums.EValType.NUM,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW5GPUGTTDVC6NAUFC2HLMJTMYI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH37GV5GPDJEX5ATJIE4QKQJJP4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IRW43PYSRBETHCA42TZ2E3KQE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOM33X6S2PVBUVKZGOKI6RTZVEA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RSGGF2OHBGFZMPB6O72LQXGB4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRQBZ3DGBAFC7RCSHTE5P7DSUU4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZFM3XSS3SBEYVGLDVUAG4DETNU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLPLRELMT25CL7KZWI2HNUFIYN4"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLHGGT5RJAVAR3BUZSSAXODZTSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPZCCCAS25ZH37DZO5ZADELFTJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWNYJDPODYFD2HJ3UOU553G76JM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprML6QUYLDHNHOPJ66RD7G3EIOOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWEG3HTZGDFDXBKP2VWE2LPVNOQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOQVEARZMQ5EFBFUZTBQ65EHGSI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI56GA2GFV5EJLKYE3SVM37BD24")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM4VVW5ELCVFEZIE5GUKQUTPMY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB3BZRY6HRBEXVLKNWZ4AVPDKAM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprICGRZIPCOJE2VLIZSZFE3IEPTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQP27HWLE5ND4NBOG6FERD3ORUQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDMG5E6RRWNHFPEQFQ2PSVI7UWA"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLHGGT5RJAVAR3BUZSSAXODZTSU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPZCCCAS25ZH37DZO5ZADELFTJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWNYJDPODYFD2HJ3UOU553G76JM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprML6QUYLDHNHOPJ66RD7G3EIOOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWEG3HTZGDFDXBKP2VWE2LPVNOQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOQVEARZMQ5EFBFUZTBQ65EHGSI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM4VVW5ELCVFEZIE5GUKQUTPMY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB3BZRY6HRBEXVLKNWZ4AVPDKAM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprICGRZIPCOJE2VLIZSZFE3IEPTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQP27HWLE5ND4NBOG6FERD3ORUQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB3JTV3D5R3OBDCWOAAMPGXSZKU"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMLPX2PHURBBVDOHNJE4SE5B4VE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ4L7QVT4O5GEFJ5QXV3YGIDCIM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIP6AXJB3NVAYHDVNMPUEP7YZJ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTWQBZYIOJFAKRDSLS2KK7CLKPU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY7U32G6FNBEQ5KMWM6UDUAXQUM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW3XKCMV7EFH75AYCOPC3TIIBAY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTVCD6NYR6BBKBB4QLTXRDR3IVY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOVP7OFFHDFGHHAXY4PC3AKF57A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXY7E7BJD35CZRJUX3LU3QEEI34"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOWIQPTL5R3OBDCWOAAMPGXSZKU"),"post",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKQ77OXRCLZH5NGWUDHEVY6FADY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQRCDXBNWD5HSZK5I5MXMVKL2M4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLVBTPIRVMFGNLNIP53B4Y5LZPA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMKNHIJEUUREUBA2CDLVK27KAPM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYG2FKQVFIZA7RAWMCZZWHIX36U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYS52QBGULFFAVMERGHZZTCY5KM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4RHGENZSZ5C4RPQ4OE2JSZLHUQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC3ZULAHUSZDMJG3SCL7NR64MYU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPFBLT5TQKZBMNAOTLCEGOM6HXI"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSUGTSJRCFNFRPLYN7KB5Z343DU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEFYXKAVEUZAUHCKCYWM5NMIMDI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAOXKXKYB6BGXPEIA4LBNKF3FOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA2ILXSC2LZGDZL3G2IDZ7TBKSY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7HIZKDV4M5DMHNM3CFE3HYGUVM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHDTQY76I5NH3TMOPFNHUISLFRA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5I6MZZT225DNJOMX4J6TK3F3WE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWHRC7AETG5G4VDA2Z25SHXUYYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr26EMU4MO5ZDMHGTEVFLPHXQGPQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVHZAGDT6R3OBDCWOAAMPGXSZKU"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQD5WCEU3FNDENAWG4PDUUOAMDA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7GUJ3TPGW5HO3L3RF3L4UW6AOI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBV3EDKJVWFF7TOWATGADUOL3J4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPP337BO2SFEVPEJVCPQFVWBVYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG3KVWUQS2NCXVOBNDRVUZS7TO4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQPR2GMYQJZEI7HOG5AUPXXFZVE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNQHVHHLOZ5E3PE3JPC4LA7DQH4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFHHD3VEZW5AO5PWMKPMMYHSX64"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRSICXRN3UFHRLDFWXXXN4CB7XY"),"unsheduleJob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("jobId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNNHOMIQK6RBIDC2A3CMXJGQOC4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEGRZNJ2YXNESHIUMDAGIGWEJYU"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6OWYHNSUKJDWJGGRCSQL56SZ74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA2TTUCKLJVFPVMF5NOZDZ7OVFE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delay",org.radixware.kernel.common.enums.EValType.NUM,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU7SGRDOAIVGCHPWOKNPJJCNQS4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKNMNSDEKMZH7VPARXCBTUKKYHE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI5XYECF5VJBL3O5SKHXHJDCU4I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLDNW7XWYTBGCBD5I7P4EMCXTAA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWQYKIH4CUFCVZILFYZ2GA3MMJ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS6FVDSGLNFA7PNUWHWGOSQFMMY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTKDBB35VDREZFBKT7RFYR7F3VI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGV24MJ2JPNCJRH4GESVXHKPZVM"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEJGOC55NLRFCHM4KU2OBSNGKXQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2YCGE7DI6RBVPBK4GGQVCLDG6I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("time",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPEAMDSDIFRD6FE6LQYXSDD32BE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN65KFG2DMZESJOWWWKZSNBAJVA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr72QKOZAUQRDW7AEUWZWBMFDMPE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ4NFJBEWAFD55EX5DMHNWZNJUM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ4SFIB6ZUNF2PCI2QZMLW2MZPI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRWIJT33HUFANNNCUKAEYPV33JQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIFCZ4PG5TNFTLCOLFLWJ5IXDTY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTSDWAFWY7RGDFIIHCUCNEEK3MQ"),"schedule",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprA4C37ODIBRCIPEVJP2G4ICW7SQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPW3NQTZMZ5GFHAQFQUYILF6T2U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSWNJ5XWUMVGYZH6SJMTSW2YZLM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQKXE7ITUURC5NBDUFP2IORXG4E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQZEYBEJQWNCGZHPQRI5NMCYI3U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4LHSS2UCVVD7RF3OENKRPZ4HIU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJWMDGO6BJRCIHONSUDRKEPFCGM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRZDZEBZABBB7PALAIUDEZ6PBEA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5B5JGMPWXRAXPPR6WUEB7KOV4I"),"post",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZHLQ7HJYYZGLFOJDCUK2CPW32E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKCDMGCOGCFAK3CNTQ6L5W3JKU4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJUJ43YPL7JFQRHGNITFA4G4HVM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIWSKTNSQMVAHZP4Q2TFFJZGVEQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOOAEURTSSVFCVBDSXABOAWRPRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("scpName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEUKCWXXRA5FPRLPETFDC4UKS2E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOTTBKHEEVHGRG2252Q5US5F2E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXF6CLMSXANBYXOYED7NYQLAS7M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTWL7UWXQINHYVM7MUGXZCI5SSQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5SWGWN667ZBPPIHSVRKYK3MGSU"),"post",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("title",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZHLQ7HJYYZGLFOJDCUK2CPW32E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("creator",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKCDMGCOGCFAK3CNTQ6L5W3JKU4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("className",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJUJ43YPL7JFQRHGNITFA4G4HVM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("methodId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIWSKTNSQMVAHZP4Q2TFFJZGVEQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("params",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOOAEURTSSVFCVBDSXABOAWRPRQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("priority",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOTTBKHEEVHGRG2252Q5US5F2E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("boosting",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXF6CLMSXANBYXOYED7NYQLAS7M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("allowRerun",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTWL7UWXQINHYVM7MUGXZCI5SSQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCVYKRCGUH5CKTJEPRNB36YRHFQ"),"getCurrentJob",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::JobQueue - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class JobQueue - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(JobQueue - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcKHAQBRKV2DNRDISQAAAAAAAAAA"),"JobQueue - Localizing Bundle",$$$items$$$);
}
