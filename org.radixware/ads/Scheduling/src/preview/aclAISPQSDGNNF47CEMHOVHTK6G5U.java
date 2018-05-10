
/* Radix::Scheduling::Task.AGroup - Server Executable*/

/*Radix::Scheduling::Task.AGroup-Application Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup")
public abstract published class Task.AGroup  extends org.radixware.ads.Scheduling.server.Task  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.AGroup_mi.rdxMeta;}

	/*Radix::Scheduling::Task.AGroup:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.AGroup:Properties-Properties*/

	/*Radix::Scheduling::Task.AGroup:cachedChilds-Dynamic Property*/



	protected java.util.List<org.radixware.ads.Scheduling.server.Task> cachedChilds=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:cachedChilds")
	protected  java.util.List<org.radixware.ads.Scheduling.server.Task> getCachedChilds() {
		return cachedChilds;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:cachedChilds")
	protected   void setCachedChilds(java.util.List<org.radixware.ads.Scheduling.server.Task> val) {
		cachedChilds = val;
	}

	/*Radix::Scheduling::Task.AGroup:atomic-Dynamic Property*/



	protected boolean atomic=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:atomic")
	protected published  boolean getAtomic() {
		return atomic;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:atomic")
	protected published   void setAtomic(boolean val) {
		atomic = val;
	}

	/*Radix::Scheduling::Task.AGroup:calcResultFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:calcResultFunc")
	public published  org.radixware.ads.Scheduling.server.UserFunc.CalcGroupResult getCalcResultFunc() {
		return calcResultFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:calcResultFunc")
	public published   void setCalcResultFunc(org.radixware.ads.Scheduling.server.UserFunc.CalcGroupResult val) {
		calcResultFunc = val;
	}

	/*Radix::Scheduling::Task.AGroup:msgChildTaskFinished-Event Code Property*/



	protected static Str msgChildTaskFinished=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("mlbaclAISPQSDGNNF47CEMHOVHTK6G5U-mlsDHCJ4FOEXJAHJDD65WHNKUTN7M",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:msgChildTaskFinished")
	public static  Str getMsgChildTaskFinished() {
		return msgChildTaskFinished;
	}

















































	/*Radix::Scheduling::Task.AGroup:Methods-Methods*/

	/*Radix::Scheduling::Task.AGroup:afterChildExecFinish-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:afterChildExecFinish")
	public abstract published  void afterChildExecFinish (org.radixware.ads.Scheduling.server.Task childTask, org.radixware.ads.Scheduling.server.Task nextTask, org.radixware.kernel.common.enums.ETaskStatus returnStatus, Int execToken);

	/*Radix::Scheduling::Task.AGroup:getChilds-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:getChilds")
	public published  java.util.List<org.radixware.ads.Scheduling.server.Task> getChilds () {
		if (cachedChilds != null) {
		    return cachedChilds;
		}
		cachedChilds = new java.util.ArrayList<Task>();
		ChildTasksCursor childsCursor = ChildTasksCursor.open(id, 0, true);
		try {
		    while (childsCursor.next()) {
		        cachedChilds.add(childsCursor.task);
		    }
		    return cachedChilds;
		} finally {
		    childsCursor.close();
		}
	}

	/*Radix::Scheduling::Task.AGroup:getPrevChild-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:getPrevChild")
	@Deprecated
	public abstract published  org.radixware.ads.Scheduling.server.Task getPrevChild (org.radixware.ads.Scheduling.server.Task child);

	/*Radix::Scheduling::Task.AGroup:isAtomic-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:isAtomic")
	public published  boolean isAtomic () {
		return atomic;
	}

	/*Radix::Scheduling::Task.AGroup:prepareForExecution-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:prepareForExecution")
	public published  void prepareForExecution () {
		super.prepareForExecution();
		if (parentId == null) {
		    SetChildsStatusStatement.execute(null, id);
		}
	}

	/*Radix::Scheduling::Task.AGroup:getChildsCountFromDb-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:getChildsCountFromDb")
	public published  int getChildsCountFromDb () {
		ChildTasksCountCursor countCursor = ChildTasksCountCursor.open(id);
		try {
		    if (countCursor.next() && countCursor.count != null) {
		        return countCursor.count.intValue();
		    }
		} finally {
		    countCursor.close();
		}
		return 0;
	}

	/*Radix::Scheduling::Task.AGroup:refineStatusOnComplete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:refineStatusOnComplete")
	public published  void refineStatusOnComplete () {
		if (calcResultFunc != null) {
		    status = calcResultFunc.calc(this);
		    return;
		}

		FailedChildsCountCursor failedChildCountCursor = FailedChildsCountCursor.open(id, execToken);
		try {
		    failedChildCountCursor.next();
		    if (failedChildCountCursor.count != null && failedChildCountCursor.count.longValue() > 0) {
		        status = TaskStatus:Failed;
		        setFaultMess(failedChildCountCursor.count.toString() + " children failed");
		    }
		} finally {
		    failedChildCountCursor.close();
		}
	}

	/*Radix::Scheduling::Task.AGroup:clearChildsCache-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:clearChildsCache")
	public published  void clearChildsCache () {
		cachedChilds = null;
	}

	/*Radix::Scheduling::Task.AGroup:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);
		if (src instanceof Task.AGroup) {
		    Task.AGroup srcTask = (Task.AGroup) src;
		    int seq = 1;
		    for (Task childTask : srcTask.getChilds()) {
		        Task childCopy = (Task) Arte::Arte.getInstance().newObject(Types::Id.Factory.loadFrom(childTask.classGuid));
		        childCopy.init(null, childTask);
		        if (src instanceof Task.Dir) {
		            childCopy.directoryId = id;
		        } else {
		            childCopy.parentId = id;
		        }
		        childCopy.seq = seq;
		        childCopy.create(childTask);
		        seq++;
		    }
		}
	}

	/*Radix::Scheduling::Task.AGroup:scheduleChildIfActive-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:scheduleChildIfActive")
	public published  boolean scheduleChildIfActive (org.radixware.ads.Scheduling.server.Task childTask, java.sql.Timestamp curExecTime) {
		if (childTask == null) {
		    return false;
		}
		if (childTask.isActive == Boolean.TRUE) {
		    Arte::Trace.put(eventCode["Task '%1': child task '%2' posted"], debugTitle, childTask.debugTitle);
		    TaskManager.postChild(childTask, debugTitle, curExecTime, execToken);
		    return true;
		} else {
		    Arte::Trace.put(eventCode["Task '%1': skip child task '%2' because it's not active"], debugTitle, childTask.debugTitle);
		    return false;
		}
	}

	/*Radix::Scheduling::Task.AGroup:isDead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:isDead")
	public published  boolean isDead () {
		lockTask();

		if (status == TaskStatus:Scheduled) {
		    return !hasRelatedJobs();
		}
		for (Task child : getChilds()) {
		    child.lockTask();
		    if (child.status == TaskStatus:Scheduled || child.status == TaskStatus:Cancelling || child.status == TaskStatus:Executing) {
		        return false;
		    }
		}
		//when last child finishes it's work, there is a time window between commit of
		//child termination and update of the status of the parent task. 

		final RecentlyActiveChildsCount cursor = RecentlyActiveChildsCount.open(id);
		try {
		    cursor.next();
		    return cursor.count.intValue() == 0;
		} finally {
		    cursor.close();
		}

	}

	/*Radix::Scheduling::Task.AGroup:isRunningOnlyWhenRelatedJobsExist-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:isRunningOnlyWhenRelatedJobsExist")
	public published  boolean isRunningOnlyWhenRelatedJobsExist () {
		return false;
	}

	/*Radix::Scheduling::Task.AGroup:decreaseChildSeqNumbers-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:decreaseChildSeqNumbers")
	  void decreaseChildSeqNumbers (long seqNum) {
		DecreaseChildSeqNumbersStatement.execute(id, seqNum, true);
	}

	/*Radix::Scheduling::Task.AGroup:canContainDirectory-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:canContainDirectory")
	  boolean canContainDirectory () {
		return false;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Task.AGroup - Server Meta*/

/*Radix::Scheduling::Task.AGroup-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.AGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),"Task.AGroup",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Task.AGroup:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
							/*Owner Class Name*/
							"Task.AGroup",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::Scheduling::Task.AGroup:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task.AGroup:calcResultFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruURRGBDPRP5HWZH3NB2L2HE4XLY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.AGroup:EditGroup-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWHRSKD2YXBFVNL54IQSUHYCA4M"),
									"EditGroup",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									36016,
									null,

									/*Radix::Scheduling::Task.AGroup:EditGroup:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWHRSKD2YXBFVNL54IQSUHYCA4M")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task.AGroup:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctKWLFFJ24KVGHXD5FOCWSR27WJM"),null,50.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICPRCBPR6RF7PLDF2T5PQHHXB4"))}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),

						/*Radix::Scheduling::Task.AGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task.AGroup:cachedChilds-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKTQGK5S4PNAIVNFOX6ZLFAOFXE"),"cachedChilds",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.AGroup:atomic-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6SIS7FJ3TFE3TAA4T3VL7MXQVQ"),"atomic",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.AGroup:calcResultFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruURRGBDPRP5HWZH3NB2L2HE4XLY"),"calcResultFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYUJMT4GMWBALDJD6POQRYEVQE4"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK7V3DBSXSNFRPGF5MUCJ733HQI"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.AGroup:msgChildTaskFinished-Event Code Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNE2MYEHK7VH3NAXJ4LU22OSN4U"),"msgChildTaskFinished",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("mlbaclAISPQSDGNNF47CEMHOVHTK6G5U-mlsDHCJ4FOEXJAHJDD65WHNKUTN7M")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task.AGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZBBPKW3Z6RAUTC3LR3FVNCTIXY"),"afterChildExecFinish",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("childTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr53NN2HAZLBBNXOZ5KC34S7RZTA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nextTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4P3PGAM36ZCOBOPHKOYGZQKBXM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("returnStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW7YGDUGN7VEEDD56APORIT3YVA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7FJ5IVVGLJE3VANUJQGOPE5AGU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCMOZTIRUORE4FDJG67MVTWQCQE"),"getChilds",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTHJHLFF7GJE45MGPI5TEEFNPYY"),"getPrevChild",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("child",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJQHBHPDAGVF2DNCEE4U2EKBJLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3IQ77RONJZA6XLYRMT6K5X6DXI"),"isAtomic",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOFXDILMIP5BNVCHAN33NERN2SE"),"prepareForExecution",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5DTKCHKSHJDIRAUKR47UTBXOUQ"),"getChildsCountFromDb",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKGQSXNEDNRCNTLI5TBHXE62D6U"),"refineStatusOnComplete",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQNM7VA6U7ZFX3JJJSGWIW52SUE"),"clearChildsCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWJN644GJOVETBGMWSOLILAWFMY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTFLOALU7KBHFHK3HIMW26EK7AM"),"scheduleChildIfActive",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("childTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3I4E5VY6B5CJDAZNOVKVUEOTKY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF5LTS5DZZVHQVAD3NENH3ONJAQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRM7DUPZ7CFAU7NRFYMBPOXCMSY"),"isDead",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3AJ224J3KFH4PKRZDARTG6KR2M"),"isRunningOnlyWhenRelatedJobsExist",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W6SOWX2PNCHNJ2MZZSRILJSKU"),"decreaseChildSeqNumbers",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("seqNum",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAYWVSINYCZANDETBKZXJBOH5KE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthA6H22KEZF5A33NV7PIU4A5JRFQ"),"canContainDirectory",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Task.AGroup - Desktop Executable*/

/*Radix::Scheduling::Task.AGroup-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup")
public interface Task.AGroup   extends org.radixware.ads.Scheduling.explorer.Task  {























































	/*Radix::Scheduling::Task.AGroup:calcResultFunc:calcResultFunc-Presentation Property*/


	public class CalcResultFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public CalcResultFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:calcResultFunc:calcResultFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.AGroup:calcResultFunc:calcResultFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public CalcResultFunc getCalcResultFunc();


}

/* Radix::Scheduling::Task.AGroup - Desktop Meta*/

/*Radix::Scheduling::Task.AGroup-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.AGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.AGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
			"Radix::Scheduling::Task.AGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,null,null,0,

			/*Radix::Scheduling::Task.AGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task.AGroup:calcResultFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruURRGBDPRP5HWZH3NB2L2HE4XLY"),
						"calcResultFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGHFTFGCINFJJGUSGGUHVS5VJQ"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclK7V3DBSXSNFRPGF5MUCJ733HQI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWHRSKD2YXBFVNL54IQSUHYCA4M")},
			true,true,false);
}

/* Radix::Scheduling::Task.AGroup - Web Meta*/

/*Radix::Scheduling::Task.AGroup-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.AGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.AGroup:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
			"Radix::Scheduling::Task.AGroup",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task.AGroup:EditGroup - Desktop Meta*/

/*Radix::Scheduling::Task.AGroup:EditGroup-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class EditGroup_mi{
	private static final class EditGroup_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		EditGroup_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWHRSKD2YXBFVNL54IQSUHYCA4M"),
			"EditGroup",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,

			/*Radix::Scheduling::Task.AGroup:EditGroup:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Scheduling::Task.AGroup:EditGroup:Additional-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVQV25OPRRRDLXJIZBAS3KBH57I"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBOC4AR4QZC3FKUSNWUHGU7ERA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruURRGBDPRP5HWZH3NB2L2HE4XLY"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVQV25OPRRRDLXJIZBAS3KBH57I")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
			,

			/*Radix::Scheduling::Task.AGroup:EditGroup:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36016,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Task.AGroup.Task.AGroup_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new EditGroup_DEF(); 
;
}
/* Radix::Scheduling::Task.AGroup - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.AGroup - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\': child task \'%2\' posted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\': запланирована дочерняя задача \'%2\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3IZCJM6H5ZGKJJYEFUTLVZRIYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.DEBUG,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Checks if this task is neither executing or planned for execution. Used in task status actualization.\nIf task has status SCHEDULED, EXECUTING or CANCELLING and isDead() returns true, then task become CANCELLED.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBNKBPUV25ZHVPN7N4QRNSAXFZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\': child task \'%2\' complete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\': дочерняя задача \'%2\' завершена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDHCJ4FOEXJAHJDD65WHNKUTN7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.DEBUG,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsICPRCBPR6RF7PLDF2T5PQHHXB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBOC4AR4QZC3FKUSNWUHGU7ERA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<default>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<по умолчанию>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRGHFTFGCINFJJGUSGGUHVS5VJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Task \'%1\': skip child task \'%2\' because it\'s not active");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача \'%1\': следующая дочерняя задача \'%2\' пропущена, поскольку она неактивна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXOMCTWQ5JJHJJMNZPFD6VV7OUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.DEBUG,"App.Task",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.AGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclAISPQSDGNNF47CEMHOVHTK6G5U"),"Task.AGroup - Localizing Bundle",$$$items$$$);
}
