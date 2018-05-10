
/* Radix::Scheduling::TaskManager - Server Executable*/

/*Radix::Scheduling::TaskManager-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager")
public published class TaskManager  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return TaskManager_mi.rdxMeta;}

	/*Radix::Scheduling::TaskManager:Nested classes-Nested Classes*/

	/*Radix::Scheduling::TaskManager:Properties-Properties*/





























	/*Radix::Scheduling::TaskManager:Methods-Methods*/

	/*Radix::Scheduling::TaskManager:execute-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:execute")
	public static published  void execute (Int taskId, Int execToken, java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		Task task = Task.loadByPK(taskId, true);
		//.("Task " + task. + " with status " + task..Name + " job: " + .().JobQueue.CurrentJobId);
		if (task != null) {
		    Exceptions::Throwable beforeExecuteException = null;
		    boolean needToExecute = false;
		    try {
		        final Int curJobId = Arte::Arte.getInstance().JobQueue.CurrentJobId;
		        Arte::Trace.debug(
		                String.format("TaskManager.execute: taskId = %s, execToken = %s, curJobId = %s, prevExecTime = %s, curExecTime = %s, status = %s",
		                              taskId, execToken, curJobId, prevExecTime, curExecTime, task.status),
		                Arte::EventSource:JobExecutor);
		        if (execToken == null) {
		            //exec token for the root task
		            execToken = curJobId;
		        }
		        needToExecute = task.beforeExecute(execToken);
		        if (needToExecute) {
		            task.status = TaskStatus:Executing;
		        }

		        Arte::Arte.commit();
		    } catch (Throwable t) {
		        beforeExecuteException = t;
		    }
		    Exceptions::Throwable executionException = null;
		    if (needToExecute && beforeExecuteException == null) {
		        try {
		//            System.out.println("Execute task " + task.);
		            Arte::Trace.enterContext(Arte::EventContextType:Task, task.id);
		            try {
		                task.execute(prevExecTime, curExecTime);
		                Arte::Arte.updateDatabase();
		            } finally {
		                Arte::Trace.leaveContext(Arte::EventContextType:Task, task.id);
		            }
		        } catch (Exceptions::Throwable t) {
		            executionException = t;
		            Arte::Arte.rollback();
		        }
		    }
		    if (executionException != null || !needToExecute || task.isAtomic()) {
		        doFinishTask(task, null, needToExecute, executionException, beforeExecuteException, execToken);
		    }
		} else {
		    Arte::Trace.put(eventCode["Unable to execute the task: task #%1 not found"], String.valueOf(taskId));
		}

	}

	/*Radix::Scheduling::TaskManager:finishTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:finishTask")
	public static published  void finishTask (org.radixware.ads.Scheduling.server.Task task, Int execToken) {
		doFinishTask(task, null, true, null, null, execToken);

	}

	/*Radix::Scheduling::TaskManager:notifyParent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:notifyParent")
	public static published  void notifyParent (org.radixware.ads.Scheduling.server.Task task, org.radixware.ads.Scheduling.server.Task nextTask, Int execToken) {
		final Task.AGroup parentTask = task.parentTask;
		if (parentTask != null) {
		    parentTask.afterChildExecFinish(task, nextTask, null, execToken);
		}
	}

	/*Radix::Scheduling::TaskManager:doFinishTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:doFinishTask")
	private static  void doFinishTask (org.radixware.ads.Scheduling.server.Task task, org.radixware.kernel.common.enums.ETaskStatus breakStatus, boolean wasExecuted, java.lang.Throwable executionException, java.lang.Throwable beforeExecuteException, Int execToken) {
		Throwable errOnFinish = null;
		try {
		    if (beforeExecuteException == null) {
		        task.afterExecute(wasExecuted, breakStatus, executionException, execToken);
		    }
		} catch (Throwable t) {
		    errOnFinish = t;
		} finally {
		    if (errOnFinish != null) {
		        task.status = TaskStatus:Failed;
		        final String faultMess = Utils::ExceptionTextFormatter.exceptionStackToString(errOnFinish)
		                + (executionException == null ? "" : "\nExecution error:\n" + Utils::ExceptionTextFormatter.exceptionStackToString(executionException));
		        task.setFaultMess(String.format("Task termination error: %s", faultMess));
		    } else if (task.status == TaskStatus:Scheduled
		            || task.status == TaskStatus:Executing
		            || task.status == TaskStatus:Cancelling
		            || task.status == null) {
		        final String faultMess;
		        if (beforeExecuteException == null) {
		            faultMess = String.format("Task execution is completed, but its status is set to '%s'", task.status == null ? "null" : task.status.Name);
		        } else {
		            faultMess = String.format(Utils::ExceptionTextFormatter.exceptionStackToString(beforeExecuteException));
		        }
		        task.setFaultMess(faultMess);
		        task.status = TaskStatus:Failed;
		    }
		    if (task.status == TaskStatus:Failed) {
		        Arte::Trace.enterContext(Arte::EventContextType:Task, task.id);
		        try {
		            String faultString = "";
		            try {
		                if (task.faultMess != null) {
		                    faultString = task.faultMess.getSubString(1l, (int) task.faultMess.length());
		                }
		            } catch (Exceptions::SQLException ex) {
		                faultString = "Error while parsing fault message: " + Utils::ExceptionTextFormatter.throwableToString(ex);
		            }
		            Arte::Trace.put(eventCode["Task \"%1\" completed with \"Failed\" status:%2%3"], task.debugTitle, "\n", faultString);
		        } finally {
		            Arte::Trace.leaveContext(Arte::EventContextType:Task, task.id);
		        }
		    }
		    if (task.finishExecTime == null) {
		        task.finishExecTime = Arte::Arte.getDatabaseSysDate();
		    }
		    boolean jobDeleted = false;
		    if (Arte::Arte.getInstance().JobQueue.RelatedTaskId == task.id) {
		        Arte::Arte.getInstance().JobQueue.deleteCurrentJob();
		        jobDeleted = true;
		    }
		    Arte::Arte.commit();
		    if (jobDeleted) {
		        Arte::Arte.getInstance().JobQueue.markCurrentJobDeleted();
		    }
		}

		task.performNotifications(execToken);
	}

	/*Radix::Scheduling::TaskManager:postManual-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:postManual")
	public static published  void postManual (org.radixware.ads.Scheduling.server.Task task, Str requesterName) {
		Arte::Trace.enterContext(Arte::EventContextType:Task, task.id);
		try {
		    try {
		        ScheduleTaskStmt.execute(task.id, requesterName, null, null);
		    } catch (Exceptions::RuntimeException ex) {
		        if (ex instanceof Exceptions::DatabaseError) {
		            Exceptions::DatabaseError dbEx = (Exceptions::DatabaseError) ex;
		            if (dbEx.getSqlErrorCode() == 20000) {                
		                String str = dbEx.getCause() == null ? dbEx.getMessage() : dbEx.getCause().getMessage();
		                if(str.contains("ORA-20000: ") && str.contains("\n")) { 
		                    str = str.substring(0, str.indexOf("\n")).replaceFirst("ORA-20000: ", "");
		                }
		                throw new AppError(String.format("Unable to schedule the task: %s", str), dbEx);
		            } else {
		                throw ex;
		            }
		        }
		    }
		    Arte::Trace.put(eventCode["User '%1' posted manual task '%2'"], Arte::Arte.getUserName(), task.debugTitle);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:Task, task.id);
		}


	}

	/*Radix::Scheduling::TaskManager:postChild-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:postChild")
	public static published  void postChild (org.radixware.ads.Scheduling.server.Task task, Str requesterName, java.sql.Timestamp curExecTime, Int execToken) {
		ScheduleTaskStmt.execute(task.id, requesterName, curExecTime, execToken);
	}

	/*Radix::Scheduling::TaskManager:cancel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:cancel")
	public static published  void cancel (org.radixware.ads.Scheduling.server.Task task) {
		task.lockTask();
		if (!(task.status == TaskStatus:Scheduled || task.status == TaskStatus:Executing)) {
		    throw new AppError("This task is not scheduled or being executed");
		}
		task.status = TaskStatus:Cancelling;
		SetChildsCancellingStatement.execute(task.id);
		if (Arte::Arte.getUserName() != null && !Arte::Arte.getUserName().isEmpty()) {
		    Arte::Trace.put(eventCode["User '%1' requested cancellation of task '%2'"], Arte::Arte.getUserName(), task.debugTitle);
		}
	}

	/*Radix::Scheduling::TaskManager:terminateParent-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:terminateParent")
	public static published  void terminateParent (org.radixware.ads.Scheduling.server.Task task, org.radixware.kernel.common.enums.ETaskStatus returnStatus, Int execToken) {
		final Task.AGroup parentTask = task.parentTask;
		if (parentTask != null) {
		    parentTask.afterChildExecFinish(task, null, returnStatus, execToken);
		}
	}

	/*Radix::Scheduling::TaskManager:cancelTree-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:cancelTree")
	public static published  void cancelTree (Int partOfTreeTaskId) {
		Task root = getTreeRoot(partOfTreeTaskId);
		if(root != null) {
		    cancel(root);
		}
	}

	/*Radix::Scheduling::TaskManager:recalcChildSeqNumbers-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:recalcChildSeqNumbers")
	public static published  void recalcChildSeqNumbers (org.radixware.ads.Scheduling.server.Task.AGroup groupTask) {
		if(groupTask == null) {
		    return;
		}
		final java.util.List<Task> childTasks = groupTask.getChilds();
		int seqNumber = 1;
		for(Task child : childTasks) {
		    child.seq = seqNumber;
		    seqNumber++;
		}
	}

	/*Radix::Scheduling::TaskManager:breakTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:breakTask")
	public static published  void breakTask (org.radixware.ads.Scheduling.server.Task task, org.radixware.kernel.common.enums.ETaskStatus breakStatus, Int execToken) {
		doFinishTask(task, breakStatus, true, null, null, execToken);

	}

	/*Radix::Scheduling::TaskManager:getTreeRoot-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:getTreeRoot")
	public static published  org.radixware.ads.Scheduling.server.Task getTreeRoot (Int treePartId) {
		if(treePartId == null) {
		    return null;
		}
		SelectTreeRootCursor rootCursor = SelectTreeRootCursor.open(treePartId);
		try {
		    if (rootCursor.next()) {
		        return Task.loadByPK(rootCursor.id, true);
		    }
		} finally {
		    rootCursor.close();
		}
		return null;
	}

	/*Radix::Scheduling::TaskManager:actualizeStatus-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::TaskManager:actualizeStatus")
	public static published  boolean actualizeStatus (org.radixware.ads.Scheduling.server.Task task) {
		if (!task.isLocked(false)) {
		    try {
		    task.rereadAndLock(0, false);
		    } catch (Exceptions::LockTimeoutError ex) {
		        throw new AppError("Task is locked");
		    }
		}
		if (task.isPhantom()) {
		    Arte::Trace.put(eventCode["Task '%1' is found to be phantom, marking it as cancelled"], task.debugTitle);
		    task.status = TaskStatus:Cancelled;
		    task.setFaultMess("Cancelled by actualization (" + (Arte::Arte.getUserName() == null ? "ARTE" : Arte::Arte.getUserName()) +")");
		    notifyParent(task, null, task.execToken);
		    return true;
		}
		return false;
	}


}

/* Radix::Scheduling::TaskManager - Server Meta*/

/*Radix::Scheduling::TaskManager-Server Dynamic Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskManager_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adc2YO4JA7JKJGGBLTARIEQHUSHGI"),"TaskManager",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Scheduling::TaskManager:Properties-Properties*/
						null,

						/*Radix::Scheduling::TaskManager:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthA4SZ4ODO4NFL3A5KMYIJ54MTZA"),"execute",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("taskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL2HBFGWMH5GL7GCJHX2N5LGEY4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK63LM3VOY5D7XO4ECPCML75TQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVKCTVCHAJJBHNLD4Q2W667DURM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJCXN42ERC5BKLM4JHPOCPSO5YM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3XTY3XAUDBAHJEEM6OMP7N35QA"),"finishTask",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS5MCEWGNRRDSNKW6JAANCWCHTM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDBUCYGXPHRFULNRLXH7EB4L3JA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLKG3SYA4UJAYLAIC34ROW2ULYQ"),"notifyParent",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZS7J3EZCMVCZ7B2U4XZUI6BVZY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nextTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUOZP7MTE4NFFPAU3JRH63BNZGI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6XBEQBAM75BFNEBXX4S4JL4T3U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMR67CF3ZIZFQNJ3RP6ZLVK6QZU"),"doFinishTask",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVGKDRTHEKFBNRPX7KGXOTUZVPI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("breakStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr67YLDEDGYNE6ZCEBA6N5HC4B4Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("wasExecuted",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBCWKJUEWH5B6NJIHY4J3VI6FPY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("executionException",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4FQWNALENNFAHPXMRY3334P3YE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("beforeExecuteException",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK6MIP6KVCVEYPOGWNNYC22EZHI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2S3VAHIPONCT3JUC4YCCFCNJZA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSN3PQ3GSIFHWXIKBOVEHAZEKXI"),"postManual",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr36TCSDXOWJA35F5AM56FLFNUHQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("requesterName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM2R5WJXOGVFGLJE7LEUBKQ6RO4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTQQPHJF7JBF4PIQR3VFYAR2FBA"),"postChild",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVYVPE6Q3BJCWPAYXMK3IBJP62A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("requesterName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCH7MI72J35FLFIBNE6GWJ4FJGY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP7AYLFOY6JBGJCJXRJUR4IRPVU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5LHJDJYCGJEIVJQCKTYU7L2HHI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQQTZALBTVNHNLPXEO457CWYQBQ"),"cancel",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJANTXFU23BCC3ONUN6BHV6BKDQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGPGEKXZQIVBW7MFL4JJWDGXSBY"),"terminateParent",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBY46MKY75FAQZJJKU6UJFDFBNU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("returnStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFTPDB57PZVGFDHAG74HWUPGRUY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWSEQSTTHMZB4XKCEDN3EXYFB2A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNWREC3E23ZCRBNO6WIPX2XEMGU"),"cancelTree",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("partOfTreeTaskId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZMEAS4N7IFGC5EFPUELCAPP2YY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEDW7XWYDPVGUZBPUM7CLBRJPNY"),"recalcChildSeqNumbers",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("groupTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJJ7IKXONABE43D3PXWBQF4CJYA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAETX36QP5BC3BPAGGZXT64VPQI"),"breakTask",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE6NI5VRIPRCWTPHS26IA222QNY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("breakStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQR4OEI566RHEDIUJLP6NKFS2WM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQNMCE7JD3NHX5ALGRH4FU27PAM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZXHCS2SCUBDVTKDMJMJNFX43TU"),"getTreeRoot",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("treePartId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBWAN7XGUE5AXRC2IYKFIZMZMWY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ4NPQVVAKBHRFDLVCNCTNCZ4RA"),"actualizeStatus",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("task",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2E7OLUR3FZHJFDZVLW5H4QZX2Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::TaskManager - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class TaskManager - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Невозможно выполнить задание: задание #%1 не найдено");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to execute the task: task #%1 not found");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBYV227ZJNVHBTHKQSMB7NGVAI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.Task",null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\' не выполняется и будет помечена как отмененная");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\' is found to be phantom, marking it as cancelled");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE4ZEYNZ6B5EVTAYMZ3B6IMWSUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось запланировать задачу: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to schedule the task: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH7BKZOIBCRH5VIKUIBQHGAIAIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача не является запланированной или выполняющейся");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"This task is not scheduled or being executed");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJVUXGUYLDNGZPINP47IWH4HIEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \'%1\' запланировал выполнение задачи \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' posted manual task \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCTTDZMXDZBZ5LQI5LILP5YLMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнение задачи завершено, однако статус установлен в значение \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task execution is completed, but its status is set to \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPU4KZW6T5A5ZC6353Z4S5CSZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \'%1\' запросил отмену выполнения задачи \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' requested cancellation of task \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTAM6QTZYRA4JA2EHGDNQ4RJZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \"%1\" завершилась со статусом \"Не выполнена\":%2%3");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \"%1\" completed with \"Failed\" status:%2%3");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDHYHYTVX5DANMNAYVBETBJJAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при завершении задачи: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task termination error: %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYLGER3PRTRC5RDBOA3THTTO57A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача заблокирована");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task is locked");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZMJIPXRBTZB4XD4QVETBFBO46Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\svn\\radix.trunk1\\org.radixware\\ads\\Scheduling"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(TaskManager - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc2YO4JA7JKJGGBLTARIEQHUSHGI"),"TaskManager - Localizing Bundle",$$$items$$$);
}
