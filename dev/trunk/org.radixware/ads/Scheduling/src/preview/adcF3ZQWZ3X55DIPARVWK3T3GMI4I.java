
/* Radix::Scheduling::TaskImpExpUtils - Server Executable*/

/*Radix::Scheduling::TaskImpExpUtils-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils")
public published class TaskImpExpUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TaskImpExpUtils_mi.rdxMeta;}

	/*Radix::Scheduling::TaskImpExpUtils:Nested classes-Nested Classes*/

	/*Radix::Scheduling::TaskImpExpUtils:Properties-Properties*/





























	/*Radix::Scheduling::TaskImpExpUtils:Methods-Methods*/

	/*Radix::Scheduling::TaskImpExpUtils:exportTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils:exportTask")
	public static published  org.radixware.ads.Scheduling.common.TaskExportXsd.TaskDocument exportTask (org.radixware.ads.Scheduling.server.Task task) {
		TaskExportXsd:TaskDocument taskDoc = TaskExportXsd:TaskDocument.Factory.newInstance();
		TaskExportXsd:Task xTask = taskDoc.addNewTask();
		doExportTask(xTask, task);
		return taskDoc;
	}

	/*Radix::Scheduling::TaskImpExpUtils:doExportTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils:doExportTask")
	private static  void doExportTask (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask, org.radixware.ads.Scheduling.server.Task task) {
		CfgManagement::ImpExpUtils.exportEntity(xTask, task, true, null);
		xTask.Priority = java.math.BigInteger.valueOf(task.priority.Value.longValue());
		xTask.PriorityBoostingSpeed = java.math.BigInteger.valueOf(task.priorityBoostingSpeed.longValue());
		xTask.IsActive = task.isActive;
		xTask.ExpiredPolicy = java.math.BigInteger.valueOf(task.expiredPolicy.Value.longValue());
		xTask.Seq = task.seq;
		xTask.Rid = task.rid;

		if (task.execRoleGuids != null) {
		    xTask.ExecRoleGuids = Types::ValAsStr.toStr(task.execRoleGuids, Meta::ValType:ArrStr);
		}
		if (task.title != null) {
		    xTask.Title = task.title;
		}
		if (task instanceof Task.AGroup) {
		    xTask.addNewChildTasks();
		    Task.AGroup groupTask = (Task.AGroup) task;
		    for (Task childTask : groupTask.getChilds()) {
		        doExportTask(xTask.ChildTasks.addNewChildTask(), childTask);
		    }
		}

		if (task.schedule != null) {
		    xTask.ScheduleGuid = task.schedule.guid;
		}

		task.onExport(xTask);

	}

	/*Radix::Scheduling::TaskImpExpUtils:importTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils:importTask")
	public static published  org.radixware.ads.Scheduling.server.Task importTask (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask) {
		return doImportTask(xTask, null);
	}

	/*Radix::Scheduling::TaskImpExpUtils:doImportTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils:doImportTask")
	private static  org.radixware.ads.Scheduling.server.Task doImportTask (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask, Int parentTaskId) {
		Task task = (Task) Arte::Arte.getInstance().newObject(xTask.ClassId);
		if (parentTaskId != null) {
		    final Task parent = Task.loadByPK(parentTaskId, true);
		    if (parent instanceof Task.Dir) {
		        task.directoryId = parentTaskId;
		    } else {
		        task.parentId = parentTaskId;
		    }
		}
		task.init(null, null, Types::EntityInitializationPhase:PROGRAMMED_CREATION);

		task.isActive = xTask.IsActive;
		task.priority = Common::Priority.getForValue(xTask.Priority.longValue());
		task.priorityBoostingSpeed = xTask.PriorityBoostingSpeed.longValue();
		task.expiredPolicy = ExpiredTaskExecPolicy.getForValue(xTask.ExpiredPolicy.longValue());
		task.seq = xTask.isSetSeq() ? xTask.Seq.longValue() : 0;
		task.rid = xTask.Rid;

		if (xTask.ExecRoleGuids != null) {
		    task.execRoleGuids = ArrStr.fromValAsStr(xTask.ExecRoleGuids);
		}
		if (xTask.Title != null) {
		    task.title = xTask.Title;
		}
		//Already do this earlear
		//if (parentTaskId != null) {
		//    task. = parentTaskId;
		//}

		CfgManagement::ImpExpUtils.importProps(xTask, task, null);

		if (xTask.SchedulerUnitId != null) {
		    task.unitId = xTask.SchedulerUnitId;
		}

		if (xTask.ScheduleId != null) {
		    task.scheduleId = xTask.ScheduleId;
		} else if (xTask.ScheduleGuid != null && !xTask.ScheduleGuid.isEmpty()) {
		    EventSchedule schedule = EventSchedule.loadByGuid(xTask.ScheduleGuid);
		    if (schedule != null) {
		        task.scheduleId = schedule.id;
		    }
		}


		task.beforeCreateOnImport(xTask);

		task.create();

		if (xTask.ChildTasks != null && xTask.ChildTasks.ChildTaskList != null && !xTask.ChildTasks.ChildTaskList.isEmpty()) {
		    for (TaskExportXsd:Task childTask : xTask.ChildTasks.ChildTaskList) {
		        doImportTask(childTask, task.id);
		    }
		}

		task.afterImport(xTask);

		return task;
	}

	/*Radix::Scheduling::TaskImpExpUtils:importTasks-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils:importTasks")
	public static published  void importTasks (org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument xTasks) {
		if (xTasks == null) {
		    return;
		}

		if (xTasks.TaskSet != null && xTasks.TaskSet.TaskList != null) {
		    for (TaskExportXsd:Task xTask : xTasks.TaskSet.TaskList) {
		        TaskImpExpUtils.importTask(xTask);
		    }
		}
	}

	/*Radix::Scheduling::TaskImpExpUtils:exportTasks-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskImpExpUtils:exportTasks")
	public static published  org.radixware.ads.Scheduling.common.TaskExportXsd.TaskSetDocument exportTasks (org.radixware.ads.Scheduling.common.TaskExportXsd.ExportRequestDocument exportRq) {
		if (exportRq == null) {
		    return null;
		}
		TaskExportXsd:TaskSetDocument xDoc = TaskExportXsd:TaskSetDocument.Factory.newInstance();
		TaskExportXsd:TaskSet taskSet = xDoc.addNewTaskSet();
		for (TaskExportXsd:ExportRequest.ExportItem exportItem : exportRq.ExportRequest.ExportItemList) {
		    Task task = Task.loadByPK(exportItem.TaskId, true);
		    taskSet.addNewTask().set(TaskImpExpUtils.exportTask(task).Task);
		}
		return xDoc;
	}


}

/* Radix::Scheduling::TaskImpExpUtils - Server Meta*/

/*Radix::Scheduling::TaskImpExpUtils-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskImpExpUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcF3ZQWZ3X55DIPARVWK3T3GMI4I"),"TaskImpExpUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Scheduling::TaskImpExpUtils:Properties-Properties*/
						null,

						/*Radix::Scheduling::TaskImpExpUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCW6DQASBGZCGZBYJWEVTMXAULM"),"exportTask",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7UKMF6DRSNGVTNEYNE6C3KPKUQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4F6UM3YDGFGUJKVQ6I4KVNMSZI"),"doExportTask",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTFGD6DI3BFFPJEEKTH5DA7EFCI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ3OH5NWOPZFVTNA5CPWCKVYDNQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHOJOYXJQEJBPLJB36MIQTPR77M"),"importTask",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprODVJTU46ZJEMZDRBNEZMXCVWXI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDOIOTMZB3JE27IA4AJLGJXJJWQ"),"doImportTask",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCY463GQUP5DXRNEWKFK5FJ5IP4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("parentTaskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKEYVXMKFRVHEZIOZJJG4ZMBTEM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQD73ZG7TCBBOVDYQ5PFE52ICJ4"),"importTasks",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTasks",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprACD77YVLVVARXCMTCOBZUYGEAA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMDIKRUCIXZHITKSBG5F4GCZFP4"),"exportTasks",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("exportRq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDMXRIJWJPFCRHLFFMD6LW7ESXY"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::TaskImpExpUtils - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskImpExpUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TaskImpExpUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcF3ZQWZ3X55DIPARVWK3T3GMI4I"),"TaskImpExpUtils - Localizing Bundle",$$$items$$$);
}
