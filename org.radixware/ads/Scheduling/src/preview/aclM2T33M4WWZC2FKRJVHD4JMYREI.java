
/* Radix::Scheduling::Task.Dir - Server Executable*/

/*Radix::Scheduling::Task.Dir-Application Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir")
public published class Task.Dir  extends org.radixware.ads.Scheduling.server.Task.AGroup  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.Dir_mi.rdxMeta;}

	/*Radix::Scheduling::Task.Dir:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.Dir:Properties-Properties*/

	/*Radix::Scheduling::Task.Dir:hasChildren-Expression Property*/




	@Override
	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:hasChildren")
	public published  Int getHasChildren() {

		return hasChildrenWorkaround;
	}

	/*Radix::Scheduling::Task.Dir:hasChildrenWorkaround-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:hasChildrenWorkaround")
	  Int getHasChildrenWorkaround() {
		return hasChildrenWorkaround;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:hasChildrenWorkaround")
	   void setHasChildrenWorkaround(Int val) {
		hasChildrenWorkaround = val;
	}

	/*Radix::Scheduling::Task.Dir:status-Column-Based Property*/




	/*Radix::Scheduling::Task.Dir:timeSinceLastExec-Dynamic Property*/




	@Override
	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:timeSinceLastExec")
	public published  Str getTimeSinceLastExec() {

		return null;
	}

	/*Radix::Scheduling::Task.Dir:lastDurationTime-Dynamic Property*/




	@Override
	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:lastDurationTime")
	public published  Str getLastDurationTime() {

		return null;
	}



















































	/*Radix::Scheduling::Task.Dir:Methods-Methods*/

	/*Radix::Scheduling::Task.Dir:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		throw new UnsupportedOperationException("Not supported for this type of task");
	}

	/*Radix::Scheduling::Task.Dir:isDead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:isDead")
	public published  boolean isDead () {
		return false;
	}

	/*Radix::Scheduling::Task.Dir:isRunningOnlyWhenRelatedJobsExist-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:isRunningOnlyWhenRelatedJobsExist")
	public published  boolean isRunningOnlyWhenRelatedJobsExist () {
		return false;
	}

	/*Radix::Scheduling::Task.Dir:afterChildExecFinish-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:afterChildExecFinish")
	public published  void afterChildExecFinish (org.radixware.ads.Scheduling.server.Task childTask, org.radixware.ads.Scheduling.server.Task nextTask, org.radixware.kernel.common.enums.ETaskStatus returnStatus, Int execToken) {
		//do nothing
	}

	/*Radix::Scheduling::Task.Dir:getPrevChild-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:getPrevChild")
	@Deprecated
	public published  org.radixware.ads.Scheduling.server.Task getPrevChild (org.radixware.ads.Scheduling.server.Task child) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	/*Radix::Scheduling::Task.Dir:getChilds-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:getChilds")
	public published  java.util.List<org.radixware.ads.Scheduling.server.Task> getChilds () {
		ChildTasksByDirCursor childsCursor = ChildTasksByDirCursor.open(id);
		java.util.List<Task> childs = new java.util.ArrayList<>();
		try {
		    while (childsCursor.next()) {
		        childs.add(childsCursor.task);
		    }
		    return childs;
		} finally {
		    childsCursor.close();
		}
	}

	/*Radix::Scheduling::Task.Dir:getChildsCountFromDb-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:getChildsCountFromDb")
	public published  int getChildsCountFromDb () {
		ChildDirTasksCountCursor countCursor = ChildDirTasksCountCursor.open(id);
		try {
		    if (countCursor.next() && countCursor.count != null) {
		        return countCursor.count.intValue();
		    }
		} finally {
		    countCursor.close();
		}
		return 0;
	}

	/*Radix::Scheduling::Task.Dir:decreaseChildSeqNumbers-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:decreaseChildSeqNumbers")
	  void decreaseChildSeqNumbers (long seqNum) {
		DecreaseChildSeqNumbersStatement.execute(id, seqNum, false);
	}

	/*Radix::Scheduling::Task.Dir:canContainDirectory-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:canContainDirectory")
	  boolean canContainDirectory () {
		return true;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Task.Dir - Server Meta*/

/*Radix::Scheduling::Task.Dir-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Dir_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),"Task.Dir",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL745RFMRV5HR7JN7YGCR6KCY7M"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Task.Dir:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
							/*Owner Class Name*/
							"Task.Dir",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL745RFMRV5HR7JN7YGCR6KCY7M"),
							/*Property presentations*/

							/*Radix::Scheduling::Task.Dir:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Task.Dir:hasChildren:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::Scheduling::Task.Dir:status:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::Scheduling::Task.Dir:timeSinceLastExec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::Scheduling::Task.Dir:lastDurationTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::Scheduling::Task.Dir:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWABRAQF3OZDDVBPYA4AX2SBQ3A"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
									36018,
									null,

									/*Radix::Scheduling::Task.Dir:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Task.Dir:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOYEWKPLQBHGPFBVJTR4SDGK74"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
									36018,
									null,

									/*Radix::Scheduling::Task.Dir:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWABRAQF3OZDDVBPYA4AX2SBQ3A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOYEWKPLQBHGPFBVJTR4SDGK74")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Task.Dir:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctKWLFFJ24KVGHXD5FOCWSR27WJM"),50.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),

						/*Radix::Scheduling::Task.Dir:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Task.Dir:hasChildren-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),"hasChildren",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select * from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Dir:hasChildrenWorkaround-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJDITQVG7QRGIPPZQ4BICW5SISA"),"hasChildrenWorkaround",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select * from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colC7IXI3MF4ZHHRJGGR7KVS6LL7Y\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecWZB7K4HLJPOBDCIUAALOMT5GDM\" PropId=\"colPU65PF7LJPOBDCIUAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Dir:status-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),"status",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZ2GFA4Y5ZDB5NT45JBLZYWASU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Dir:timeSinceLastExec-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),"timeSinceLastExec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPCAKAARVKZFVFPVOJT52SROHE4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Task.Dir:lastDurationTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),"lastDurationTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFELRX23WYVHCXIOUBAVMMR74U4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Task.Dir:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI3XJAGBYXVHV5L35DF5MASV5KY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWAWOWC2FP5ENDGSCBWSY5L2ZII"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRM7DUPZ7CFAU7NRFYMBPOXCMSY"),"isDead",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3AJ224J3KFH4PKRZDARTG6KR2M"),"isRunningOnlyWhenRelatedJobsExist",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZBBPKW3Z6RAUTC3LR3FVNCTIXY"),"afterChildExecFinish",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("childTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOPTOKFPEUNFW5LRFC4FF6NTGTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("nextTask",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRHXH66PP3JDAVDZHKPQGNUQ3LE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("returnStatus",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI2YUXFZTDNF2HKVH732GU2DHWE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("execToken",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWOF2P3TWJFDR5BT5NTZC2PERPM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTHJHLFF7GJE45MGPI5TEEFNPYY"),"getPrevChild",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("child",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKMDNQZ6DVZD2VLEAEZFOATNVIE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCMOZTIRUORE4FDJG67MVTWQCQE"),"getChilds",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5DTKCHKSHJDIRAUKR47UTBXOUQ"),"getChildsCountFromDb",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W6SOWX2PNCHNJ2MZZSRILJSKU"),"decreaseChildSeqNumbers",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("seqNum",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCHMFRBGQJJFHVCEOBMCPCGJAU4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthA6H22KEZF5A33NV7PIU4A5JRFQ"),"canContainDirectory",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::Scheduling::Task.Dir - Desktop Executable*/

/*Radix::Scheduling::Task.Dir-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir")
public interface Task.Dir   extends org.radixware.ads.Scheduling.explorer.Task.AGroup  {












































































}

/* Radix::Scheduling::Task.Dir - Desktop Meta*/

/*Radix::Scheduling::Task.Dir-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Dir_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.Dir:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
			"Radix::Scheduling::Task.Dir",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL745RFMRV5HR7JN7YGCR6KCY7M"),null,null,0,

			/*Radix::Scheduling::Task.Dir:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Task.Dir:hasChildren:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN4UP2RJZNVGGFHJ2O6GKFQNLAI"),
						"hasChildren",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						63,
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

						/*Radix::Scheduling::Task.Dir:hasChildren:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.Dir:status:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPT5IWXJ4O5AIPIYP3NSPZTDK54"),
						"status",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						61,
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

						/*Radix::Scheduling::Task.Dir:status:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEIXEKC5YG5CVBEOCLYU33L3WGI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNZIK5OL4VFVLL7KGBYKXYJJAM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.Dir:timeSinceLastExec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOMIZVMCJZHFLCSICRPP7CFGJQ"),
						"timeSinceLastExec",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						61,
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

						/*Radix::Scheduling::Task.Dir:timeSinceLastExec:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI2T3CEP7GZD43B46WELYJF3T5I"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Task.Dir:lastDurationTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNDQRMIMTBRAKDCLS3FZU4MFC6U"),
						"lastDurationTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						61,
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

						/*Radix::Scheduling::Task.Dir:lastDurationTime:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGOM5YRHYLZEDJCHSQLCDDQ5ZI4"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWABRAQF3OZDDVBPYA4AX2SBQ3A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOYEWKPLQBHGPFBVJTR4SDGK74")},
			true,true,false);
}

/* Radix::Scheduling::Task.Dir - Web Meta*/

/*Radix::Scheduling::Task.Dir-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Dir_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Task.Dir:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
			"Radix::Scheduling::Task.Dir",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAISPQSDGNNF47CEMHOVHTK6G5U"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL745RFMRV5HR7JN7YGCR6KCY7M"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::Scheduling::Task.Dir:Edit - Desktop Meta*/

/*Radix::Scheduling::Task.Dir:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWABRAQF3OZDDVBPYA4AX2SBQ3A"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAIWBLDTSJTOBDCIVAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
	null,
	null,

	/*Radix::Scheduling::Task.Dir:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Scheduling::Task.Dir:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZSKPYPT64G4ENTLKOOWJ6OT56KBWJ4YR"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRMU7WRHOUZDINIVZWJHT6KSOFM"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM"))}
	,

	/*Radix::Scheduling::Task.Dir:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	36018,0,0);
}
/* Radix::Scheduling::Task.Dir:Edit:Model - Desktop Executable*/

/*Radix::Scheduling::Task.Dir:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:Edit:Model")
public class Edit:Model  extends org.radixware.ads.Scheduling.explorer.Task.Dir.Task.Dir_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Scheduling::Task.Dir:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Task.Dir:Edit:Model:Properties-Properties*/

	/*Radix::Scheduling::Task.Dir:Edit:Model:isActive-Presentation Property*/




	public class IsActive extends org.radixware.ads.Scheduling.explorer.Task.Dir.Task.Dir_DefaultModel.eprAIWBLDTSJTOBDCIVAALOMT5GDM_ModelAdapter.colRMH5P2FSTNABBMBNUIXU3AINEQ{
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

		/*Radix::Scheduling::Task.Dir:Edit:Model:isActive:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Scheduling::Task.Dir:Edit:Model:isActive:Nested classes-Nested Classes*/

		/*Radix::Scheduling::Task.Dir:Edit:Model:isActive:Properties-Properties*/

		/*Radix::Scheduling::Task.Dir:Edit:Model:isActive:Methods-Methods*/

		/*Radix::Scheduling::Task.Dir:Edit:Model:isActive:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:Edit:Model:isActive:isVisible")
		public published  boolean isVisible () {
			return false;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:Edit:Model:isActive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:Edit:Model:isActive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsActive getIsActive(){return (IsActive)getProperty(colRMH5P2FSTNABBMBNUIXU3AINEQ);}








	/*Radix::Scheduling::Task.Dir:Edit:Model:Methods-Methods*/

	/*Radix::Scheduling::Task.Dir:Edit:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Task.Dir:Edit:Model:isCommandAccessible")
	public published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		return commandDef.getId() != idof[Task:Post]
		        && commandDef.getId() != idof[Task:Cancel]
		        && commandDef.getId() != idof[Task:ActualizeStatus]
		        && super.isCommandAccessible(commandDef);
	}


}

/* Radix::Scheduling::Task.Dir:Edit:Model - Desktop Meta*/

/*Radix::Scheduling::Task.Dir:Edit:Model-Entity Model Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemWABRAQF3OZDDVBPYA4AX2SBQ3A"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAIWBLDTSJTOBDCIVAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Scheduling::Task.Dir:Edit:Model:Properties-Properties*/
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

/* Radix::Scheduling::Task.Dir:Create - Desktop Meta*/

/*Radix::Scheduling::Task.Dir:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHOYEWKPLQBHGPFBVJTR4SDGK74"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXGQ6C3VMQPOBDCKCAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
			null,
			null,

			/*Radix::Scheduling::Task.Dir:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Scheduling::Task.Dir:Create:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclM2T33M4WWZC2FKRJVHD4JMYREI"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgZSKPYPT64G4ENTLKOOWJ6OT56KBWJ4YR"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPU65PF7LJPOBDCIUAALOMT5GDM"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIYGJ6NTQJTOBDCIVAALOMT5GDM"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVVAT5W3YSLOBDCKTAALOMT5GDM"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTCVRBPKC6JBMFIRHRNV7XKWEAE"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWDY2TRTHU5CBNOYBLCQRTQSCZM"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7PEOSLRVM5GQLNRYMHH4V7B2JI"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHHRMHELUJTOBDCIVAALOMT5GDM"))}
			,

			/*Radix::Scheduling::Task.Dir:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36018,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Task.Dir.Task.Dir_DefaultModel.eprXGQ6C3VMQPOBDCKCAALOMT5GDM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Scheduling::Task.Dir - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.Dir - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFNZIK5OL4VFVLL7KGBYKXYJJAM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGOM5YRHYLZEDJCHSQLCDDQ5ZI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI2T3CEP7GZD43B46WELYJF3T5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Directory");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Директория");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL745RFMRV5HR7JN7YGCR6KCY7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.Dir - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclM2T33M4WWZC2FKRJVHD4JMYREI"),"Task.Dir - Localizing Bundle",$$$items$$$);
}
