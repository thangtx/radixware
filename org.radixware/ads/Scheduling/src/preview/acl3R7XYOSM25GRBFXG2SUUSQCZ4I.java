
/* Radix::Scheduling::Task.Goto - Server Executable*/

/*Radix::Scheduling::Task.Goto-Application Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto")
public published class Task.Goto  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.Goto_mi.rdxMeta;}

	/*Radix::Scheduling::Task.Goto:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.Goto:Properties-Properties*/

	/*Radix::Scheduling::Task.Goto:taskToJump-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:taskToJump")
	public published  org.radixware.ads.Scheduling.server.Task getTaskToJump() {
		return taskToJump;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:taskToJump")
	public published   void setTaskToJump(org.radixware.ads.Scheduling.server.Task val) {
		taskToJump = val;
	}

	/*Radix::Scheduling::Task.Goto:destination-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:destination")
	public published  org.radixware.ads.Scheduling.common.GotoTaskDestination getDestination() {
		return destination;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:destination")
	public published   void setDestination(org.radixware.ads.Scheduling.common.GotoTaskDestination val) {
		destination = val;
	}

	/*Radix::Scheduling::Task.Goto:groupExitStatus-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:groupExitStatus")
	public published  org.radixware.kernel.common.enums.ETaskStatus getGroupExitStatus() {
		return groupExitStatus;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:groupExitStatus")
	public published   void setGroupExitStatus(org.radixware.kernel.common.enums.ETaskStatus val) {
		groupExitStatus = val;
	}

	/*Radix::Scheduling::Task.Goto:prevStatusMatched-Dynamic Property*/



	protected boolean prevStatusMatched=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prevStatusMatched")
	private final  boolean getPrevStatusMatched() {
		return prevStatusMatched;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prevStatusMatched")
	private final   void setPrevStatusMatched(boolean val) {
		prevStatusMatched = val;
	}

	/*Radix::Scheduling::Task.Goto:prevTaskStatus-User Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prevTaskStatus")
	public published  org.radixware.kernel.common.enums.ETaskStatus getPrevTaskStatus() {
		return prevTaskStatus;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prevTaskStatus")
	public published   void setPrevTaskStatus(org.radixware.kernel.common.enums.ETaskStatus val) {
		prevTaskStatus = val;
	}



























































	/*Radix::Scheduling::Task.Goto:Methods-Methods*/

	/*Radix::Scheduling::Task.Goto:canBeRoot-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:canBeRoot")
	public published  boolean canBeRoot () {
		return false;
	}

	/*Radix::Scheduling::Task.Goto:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (parentTask == null || !(parentTask instanceof Task.Sequence)) {
		    throw new InvalidPropertyValueError(idof[Task.Goto], idof[Task:parentId], "GoTo task can only be part of a sequence group");
		}

		return super.beforeCreate(src);
	}

	/*Radix::Scheduling::Task.Goto:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		if (prevTaskStatus == null) {//any
		    prevStatusMatched = true;
		} else {
		    Task prevTask = Task.loadByPK(((Task.Sequence)parentTask).lastFinishedChildId, true);
		    if (prevTask != null && prevTask.status == prevTaskStatus) {
		        prevStatusMatched = true;
		    } else {
		        prevStatusMatched = false;
		    }
		}

	}

	/*Radix::Scheduling::Task.Goto:prepareForExecution-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prepareForExecution")
	public published  void prepareForExecution () {
		super.prepareForExecution();
		prevStatusMatched = false;
	}

	/*Radix::Scheduling::Task.Goto:performNotifications-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:performNotifications")
	public published  void performNotifications (Int execToken) {
		if (!prevStatusMatched || destination == null) {
		    super.performNotifications(execToken);
		} else {
		    if (destination == GotoTaskDestination:THIS_GROUP_TASK) {
		        TaskManager.notifyParent(this, this.taskToJump, execToken);
		    } else if (destination == GotoTaskDestination:RETURN_FROM_GROUP) {
		        TaskManager.terminateParent(this, groupExitStatus, execToken);
		    } else if (destination == GotoTaskDestination:CANCEL_TREE) {
		        TaskManager.cancelTree(id);
		        super.performNotifications(execToken);//to process cancel in parent task
		    }
		}
	}

	/*Radix::Scheduling::Task.Goto:onExport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:onExport")
	protected published  void onExport (org.radixware.ads.Scheduling.common.TaskExportXsd.Task xTask) {
		super.onExport(xTask);
		if (destination == GotoTaskDestination:THIS_GROUP_TASK && taskToJump != null) {
		    xTask.TaskToJumpIndex = taskToJump.seq.intValue() - 1;
		    if (xTask.isSetUserProps()) {
		        for (Common::CommonXsd:UserProp prop : xTask.UserProps.UserPropList) {
		            if (idof[Task.Goto:taskToJump].equals(prop.Id)) {
		                prop.Value = null;
		                prop.SafeValue = null;
		            }
		        }
		    }
		}
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Task.Goto - Server Meta*/

/*Radix::Scheduling::Task.Goto-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Goto_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),"Task.Goto",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSGZOOWMUZD65PUR4HYYQMIBNU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Task.Goto:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
							/*Owner Class Name*/
							"Task.Goto",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSGZOOWMUZD65PUR4HYYQMIBNU"),
							/*Property presentations*/

							/*Radix::Scheduling::Task.Goto:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task.Goto:taskToJump:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruY3KDUFW4U5EGBKLQ26CAD3HHLU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not null and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colBHMUC3KGIJE3PA6RVXKLXFHP3U\" Owner=\"CHILD\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.Goto:destination:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBPNZFBTI4BGKNEXOJBP26Q4WKY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.Goto:groupExitStatus:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFVKL5O5EUJGTTHWXPGF3FCPHBU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Task.Goto:prevTaskStatus:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBLAIJQFYUNAYNOFXWLZVWXY6OM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Scheduling::Task.Goto:EditGoto-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMQ2PK3TV6BC4RPWXADP6L5SYFA"),
									"EditGoto",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									36016,
									null,

									/*Radix::Scheduling::Task.Goto:EditGoto:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.Goto:CreateGoto-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4IDPMGLB75HILDSGVY7YLJD5XY"),
									"CreateGoto",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMQ2PK3TV6BC4RPWXADP6L5SYFA"),
									40112,
									null,

									/*Radix::Scheduling::Task.Goto:CreateGoto:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMQ2PK3TV6BC4RPWXADP6L5SYFA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4IDPMGLB75HILDSGVY7YLJD5XY")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task.Goto:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),null,56.25,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::Scheduling::Task.Goto:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task.Goto:taskToJump-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruY3KDUFW4U5EGBKLQ26CAD3HHLU"),"taskToJump",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGLD7OSIIJHYRJ2RZGQVZIUBVA"),org.radixware.kernel.common.enums.EValType.PARENT_REF,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Goto:destination-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBPNZFBTI4BGKNEXOJBP26Q4WKY"),"destination",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRIQFYSK5ZAIPOHMC62ZSUCRJY"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFM3CYAC365CGHDRL5USB36CH4Q"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("THIS_GROUP_TASK")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Goto:groupExitStatus-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFVKL5O5EUJGTTHWXPGF3FCPHBU"),"groupExitStatus",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXSFMWS4U5BINJNIBG5M23VVDU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Success")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Goto:prevStatusMatched-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIG5LVCYLZFGHTKGSLZ32W2JIQI"),"prevStatusMatched",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Goto:prevTaskStatus-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBLAIJQFYUNAYNOFXWLZVWXY6OM"),"prevTaskStatus",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLN5PRJ53XRFYXJMGHHAEUMSTE4"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Success")),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task.Goto:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBOIFVDBJQVFIDKL2NUR5D6KA64"),"canBeRoot",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJRNH4GE6NVE3BFJ6EXMVDIQQEI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr47732N4S3VH63AATZIJJVL6VAY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSGCF67USVBDWJHBG5X54KOYRVI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOFXDILMIP5BNVCHAN33NERN2SE"),"prepareForExecution",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQJ36OAD25NFR5DEA5XREYYJ3VI"),"performNotifications",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2HWNHVEJ4VBEHEOHBLRZKQKCOA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZR4GWJ7U5ZHMZOZPZSL2IMMGAY"),"onExport",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPVJXO3MREZHHHEPWGT2LI64KBQ"))
								},null)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Task.Goto - Desktop Executable*/

/*Radix::Scheduling::Task.Goto-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto")
public interface Task.Goto   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {



























































































	/*Radix::Scheduling::Task.Goto:prevTaskStatus:prevTaskStatus-Presentation Property*/


	public class PrevTaskStatus extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PrevTaskStatus(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prevTaskStatus:prevTaskStatus")
		public  org.radixware.kernel.common.enums.ETaskStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:prevTaskStatus:prevTaskStatus")
		public   void setValue(org.radixware.kernel.common.enums.ETaskStatus val) {
			Value = val;
		}
	}
	public PrevTaskStatus getPrevTaskStatus();
	/*Radix::Scheduling::Task.Goto:destination:destination-Presentation Property*/


	public class Destination extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Destination(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Scheduling.common.GotoTaskDestination dummy = x == null ? null : (x instanceof org.radixware.ads.Scheduling.common.GotoTaskDestination ? (org.radixware.ads.Scheduling.common.GotoTaskDestination)x : org.radixware.ads.Scheduling.common.GotoTaskDestination.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Scheduling.common.GotoTaskDestination> getValClass(){
			return org.radixware.ads.Scheduling.common.GotoTaskDestination.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Scheduling.common.GotoTaskDestination dummy = x == null ? null : (x instanceof org.radixware.ads.Scheduling.common.GotoTaskDestination ? (org.radixware.ads.Scheduling.common.GotoTaskDestination)x : org.radixware.ads.Scheduling.common.GotoTaskDestination.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:destination:destination")
		public  org.radixware.ads.Scheduling.common.GotoTaskDestination getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:destination:destination")
		public   void setValue(org.radixware.ads.Scheduling.common.GotoTaskDestination val) {
			Value = val;
		}
	}
	public Destination getDestination();
	/*Radix::Scheduling::Task.Goto:groupExitStatus:groupExitStatus-Presentation Property*/


	public class GroupExitStatus extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public GroupExitStatus(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:groupExitStatus:groupExitStatus")
		public  org.radixware.kernel.common.enums.ETaskStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:groupExitStatus:groupExitStatus")
		public   void setValue(org.radixware.kernel.common.enums.ETaskStatus val) {
			Value = val;
		}
	}
	public GroupExitStatus getGroupExitStatus();
	/*Radix::Scheduling::Task.Goto:taskToJump:taskToJump-Presentation Property*/


	public class TaskToJump extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public TaskToJump(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:taskToJump:taskToJump")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:taskToJump:taskToJump")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public TaskToJump getTaskToJump();


}

/* Radix::Scheduling::Task.Goto - Desktop Meta*/

/*Radix::Scheduling::Task.Goto-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Goto_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.Goto:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
			"Radix::Scheduling::Task.Goto",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSGZOOWMUZD65PUR4HYYQMIBNU"),null,null,0,

			/*Radix::Scheduling::Task.Goto:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task.Goto:taskToJump:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruY3KDUFW4U5EGBKLQ26CAD3HHLU"),
						"taskToJump",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGLD7OSIIJHYRJ2RZGQVZIUBVA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSCZDV2QJPZBJDDFYBDLQUTOREM"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUBSJVMRTFZCDPKMFQD5V6SQ3EY"),
						133693439,
						133693439,false),

					/*Radix::Scheduling::Task.Goto:destination:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBPNZFBTI4BGKNEXOJBP26Q4WKY"),
						"destination",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRIQFYSK5ZAIPOHMC62ZSUCRJY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFM3CYAC365CGHDRL5USB36CH4Q"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("THIS_GROUP_TASK"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.Goto:destination:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFM3CYAC365CGHDRL5USB36CH4Q"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.Goto:groupExitStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFVKL5O5EUJGTTHWXPGF3FCPHBU"),
						"groupExitStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXSFMWS4U5BINJNIBG5M23VVDU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Success"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.Goto:groupExitStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7Z626KWOCBG25D3MNAVANFNXMM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciJCCIEXZNGBEAZGSBORYHGTY25Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aci7RLLCQDYBVFCBG3PMYGMF7KRWQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aciMJQDB7OIVFFBPIY3ZIWRI4O5AM")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.Goto:prevTaskStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBLAIJQFYUNAYNOFXWLZVWXY6OM"),
						"prevTaskStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLN5PRJ53XRFYXJMGHHAEUMSTE4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Success"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Task.Goto:prevTaskStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXV5G2LZE5F4FD3UUH7KDZBBEI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMQ2PK3TV6BC4RPWXADP6L5SYFA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4IDPMGLB75HILDSGVY7YLJD5XY")},
			true,true,false);
}

/* Radix::Scheduling::Task.Goto - Web Meta*/

/*Radix::Scheduling::Task.Goto-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Goto_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.Goto:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
			"Radix::Scheduling::Task.Goto",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSGZOOWMUZD65PUR4HYYQMIBNU"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task.Goto:EditGoto - Desktop Meta*/

/*Radix::Scheduling::Task.Goto:EditGoto-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class EditGoto_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMQ2PK3TV6BC4RPWXADP6L5SYFA"),
	"EditGoto",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::Task.Goto:EditGoto:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::Task.Goto:EditGoto:Destination-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVU3BQNMP5VGEJNVMKN6IXS4WOA"),"Destination",org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDH2MOMME5G5TGXBCUO4WQSAYE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBPNZFBTI4BGKNEXOJBP26Q4WKY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFVKL5O5EUJGTTHWXPGF3FCPHBU"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruY3KDUFW4U5EGBKLQ26CAD3HHLU"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBLAIJQFYUNAYNOFXWLZVWXY6OM"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVU3BQNMP5VGEJNVMKN6IXS4WOA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgL6FUZ36EMVEU3BPQ4FLI3TZ6KA"))}
	,

	/*Radix::Scheduling::Task.Goto:EditGoto:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36016,0,0);
}
/* Radix::Scheduling::Task.Goto:EditGoto:Model - Desktop Executable*/

/*Radix::Scheduling::Task.Goto:EditGoto:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:EditGoto:Model")
public class EditGoto:Model  extends org.radixware.ads.Scheduling.explorer.Task.Goto.Task.Goto_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return EditGoto:Model_mi.rdxMeta; }



	public EditGoto:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task.Goto:EditGoto:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.Goto:EditGoto:Model:Properties-Properties*/

	/*Radix::Scheduling::Task.Goto:EditGoto:Model:destination-Presentation Property*/




	public class Destination extends org.radixware.ads.Scheduling.explorer.Task.Goto.Task.Goto_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter.pruBPNZFBTI4BGKNEXOJBP26Q4WKY{
		public Destination(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Scheduling.common.GotoTaskDestination dummy = x == null ? null : (x instanceof org.radixware.ads.Scheduling.common.GotoTaskDestination ? (org.radixware.ads.Scheduling.common.GotoTaskDestination)x : org.radixware.ads.Scheduling.common.GotoTaskDestination.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Scheduling.common.GotoTaskDestination> getValClass(){
			return org.radixware.ads.Scheduling.common.GotoTaskDestination.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Scheduling.common.GotoTaskDestination dummy = x == null ? null : (x instanceof org.radixware.ads.Scheduling.common.GotoTaskDestination ? (org.radixware.ads.Scheduling.common.GotoTaskDestination)x : org.radixware.ads.Scheduling.common.GotoTaskDestination.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:EditGoto:Model:destination")
		public  org.radixware.ads.Scheduling.common.GotoTaskDestination getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:EditGoto:Model:destination")
		public   void setValue(org.radixware.ads.Scheduling.common.GotoTaskDestination val) {

			internal[destination] = val;
			updateDestSettingsVisibility();
		}
	}
	public Destination getDestination(){return (Destination)getProperty(pruBPNZFBTI4BGKNEXOJBP26Q4WKY);}








	/*Radix::Scheduling::Task.Goto:EditGoto:Model:Methods-Methods*/

	/*Radix::Scheduling::Task.Goto:EditGoto:Model:updateDestSettingsVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:EditGoto:Model:updateDestSettingsVisibility")
	private final  void updateDestSettingsVisibility () {
		taskToJump.setVisible(false);
		groupExitStatus.setVisible(false);
		groupExitStatus.setMandatory(false);
		if (destination.Value == GotoTaskDestination:RETURN_FROM_GROUP) {
		    groupExitStatus.setVisible(true);
		    groupExitStatus.setMandatory(true);
		} else if (destination.Value == GotoTaskDestination:THIS_GROUP_TASK) {
		    taskToJump.setVisible(true);
		}
	}

	/*Radix::Scheduling::Task.Goto:EditGoto:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Goto:EditGoto:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		updateDestSettingsVisibility();
	}


}

/* Radix::Scheduling::Task.Goto:EditGoto:Model - Desktop Meta*/

/*Radix::Scheduling::Task.Goto:EditGoto:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class EditGoto:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemMQ2PK3TV6BC4RPWXADP6L5SYFA"),
						"EditGoto:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAIWBLDTSJTOBDCIVAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task.Goto:EditGoto:Model:Properties-Properties*/
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

/* Radix::Scheduling::Task.Goto:CreateGoto - Desktop Meta*/

/*Radix::Scheduling::Task.Goto:CreateGoto-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class CreateGoto_mi{
	private static final class CreateGoto_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		CreateGoto_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4IDPMGLB75HILDSGVY7YLJD5XY"),
			"CreateGoto",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMQ2PK3TV6BC4RPWXADP6L5SYFA"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl3R7XYOSM25GRBFXG2SUUSQCZ4I"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,
			null,
			null,

			/*Radix::Scheduling::Task.Goto:CreateGoto:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.EditGoto:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new CreateGoto_DEF(); 
;
}
/* Radix::Scheduling::Task.Goto - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Goto - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAXV5G2LZE5F4FD3UUH7KDZBBEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Jump on Condition");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Переход по условию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSGZOOWMUZD65PUR4HYYQMIBNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Previous task status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Статус предыдущей задачи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLN5PRJ53XRFYXJMGHHAEUMSTE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Destination");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес перехода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQDH2MOMME5G5TGXBCUO4WQSAYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<next>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<следующее>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSCZDV2QJPZBJDDFYBDLQUTOREM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Jump to task");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача для перехода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUGLD7OSIIJHYRJ2RZGQVZIUBVA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Finish parent task with status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Завершить родительскую задачу со статусом");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXSFMWS4U5BINJNIBG5M23VVDU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Operation type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип перехода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRIQFYSK5ZAIPOHMC62ZSUCRJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"GoTo task can only be part of a sequence group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задача условного перехода может являться только частью последовательной группы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZL52WGDJQVFFXCINRFTOGXOEJI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.Goto - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbacl3R7XYOSM25GRBFXG2SUUSQCZ4I"),"Task.Goto - Localizing Bundle",$$$items$$$);
}
