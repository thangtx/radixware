
/* Radix::MessageQueue::MessageQueueProcessor - Server Executable*/

/*Radix::MessageQueue::MessageQueueProcessor-Entity Class*/

package org.radixware.ads.MessageQueue.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor")
public abstract published class MessageQueueProcessor  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MessageQueueProcessor_mi.rdxMeta;}

	/*Radix::MessageQueue::MessageQueueProcessor:Nested classes-Nested Classes*/

	/*Radix::MessageQueue::MessageQueueProcessor:Properties-Properties*/

	/*Radix::MessageQueue::MessageQueueProcessor:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:parallelThreads")
	public published  Int getParallelThreads() {
		return parallelThreads;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:parallelThreads")
	public published   void setParallelThreads(Int val) {
		parallelThreads = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:queueId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:queueId")
	public published  Int getQueueId() {
		return queueId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:queueId")
	public published   void setQueueId(Int val) {
		queueId = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:unitId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unitId")
	public published  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unitId")
	public published   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:unit-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unit")
	public published  org.radixware.ads.System.server.Unit getUnit() {
		return unit;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unit")
	public published   void setUnit(org.radixware.ads.System.server.Unit val) {
		unit = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:messageQueue-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:messageQueue")
	public published  org.radixware.ads.MessageQueue.server.MessageQueue.Consumer getMessageQueue() {
		return messageQueue;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:messageQueue")
	public published   void setMessageQueue(org.radixware.ads.MessageQueue.server.MessageQueue.Consumer val) {
		messageQueue = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classTitle")
	public published  Str getClassTitle() {

		return getClassDefinitionTitle();
	}

	/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile")
	public published  Str getDbTraceProfile() {
		return dbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile")
	public published   void setDbTraceProfile(Str val) {
		dbTraceProfile = val;
	}

	/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec")
	public published  Int getAasCallTimeoutSec() {
		return aasCallTimeoutSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec")
	public published   void setAasCallTimeoutSec(Int val) {
		aasCallTimeoutSec = val;
	}



























































































	/*Radix::MessageQueue::MessageQueueProcessor:Methods-Methods*/

	/*Radix::MessageQueue::MessageQueueProcessor:process-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:process")
	public published  org.radixware.schemas.messagequeue.MqProcessRsDocument process (org.radixware.schemas.messagequeue.MqProcessRq xProcessRq) {
		if (xProcessRq != null && xProcessRq.isSetMessage()) {
		    Arte::Trace.enterContext(Arte::EventContextType:MqProcessor, id);
		    try {
		        final Object traceHandle;
		        if (dbTraceProfile != null) {
		            traceHandle = Arte::Trace.addContextProfile(dbTraceProfile, this);
		        } else {
		            traceHandle = null;
		        }
		        try {
		            process(xProcessRq.Message);
		        } finally {
		            if (traceHandle != null) {
		                Arte::Trace.delContextProfile(traceHandle);
		            }
		        }
		    } finally {
		        Arte::Trace.leaveContext(Arte::EventContextType:MqProcessor, id);
		    }
		}
		final MessageQueueXsd:MqProcessRsDocument rsDoc = MessageQueueXsd:MqProcessRsDocument.Factory.newInstance();
		rsDoc.addNewMqProcessRs();
		return rsDoc;

	}

	/*Radix::MessageQueue::MessageQueueProcessor:process-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:process")
	public abstract published  void process (org.radixware.schemas.messagequeue.MqMessage xMessage);


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::MessageQueue::MessageQueueProcessor - Server Meta*/

/*Radix::MessageQueue::MessageQueueProcessor-Entity Class*/

package org.radixware.ads.MessageQueue.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueueProcessor_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),"MessageQueueProcessor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::MessageQueue::MessageQueueProcessor:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
							/*Owner Class Name*/
							"MessageQueueProcessor",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),
							/*Property presentations*/

							/*Radix::MessageQueue::MessageQueueProcessor:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::MessageQueue::MessageQueueProcessor:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWT3EM43DNCLNLELPBPTWABBLI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7IMQMPGEX5COVHOB23CGVPAQ6M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:queueId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB5IPJ2R6TRD27KWL3NLR5MG7WY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:unit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3QNYLD7ZNDINGW4I7DZBV4MOQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colF2TCBU6YX7NRDB6TAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclPC7EWUX4OJHNLM3A47ZR2TDUDY\"/></xsc:Item><xsc:Item><xsc:Sql>) > 0\nand </xsc:Sql></xsc:Item><xsc:Item><xsc:DbFuncCall FuncId=\"dfn3OXAHT4FYLORDBBJABIFNQAABA\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colF2TCBU6YX7NRDB6TAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:Id Path=\"aclVL4JVALRV5BGLHMTFRIGF3YRWI\"/></xsc:Item><xsc:Item><xsc:Sql>) = 0 \nand not exists(select 1 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblU5S5JH3DFFGHXMRP4JVYGLQQTQ\"/></xsc:Item><xsc:Item><xsc:Sql> mp where mp.</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecU5S5JH3DFFGHXMRP4JVYGLQQTQ\" PropId=\"colD2DRWL3JUFBCVMQF7CVVQAQ3EA\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(33029099,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::MessageQueue::MessageQueueProcessor:messageQueue:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5DTM6IDIBDCLC5EUPSUAWW63M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBU6D3OXQLFGJPK5ZZ64L3N3DNA"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(33029099,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWEEVKI2IT5DQJAS5RJFI4FFDS4")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZZEI4AN6ZFLVEDWNQ6VJK2PSU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWPQQH54DTFA4HO7U7EFFHMJRMI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::MessageQueue::MessageQueueProcessor:ByID-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYSJBJBBPJCSTLDLUWXIYLTGVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),"ByID",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUR6N62XPBBGXDCZQVDXJMAFTU"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::MessageQueue::MessageQueueProcessor:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::MessageQueue::MessageQueueProcessor:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::MessageQueue::MessageQueueProcessor:Edit:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJAOTMKI6SNCTRJT2YNFLSH3LTU"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::MessageQueue::MessageQueueProcessor:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
									40114,
									null,

									/*Radix::MessageQueue::MessageQueueProcessor:Create:Children-Explorer Items*/
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
									/*Radix::MessageQueue::MessageQueueProcessor:Default-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM"),"Default",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYSJBJBBPJCSTLDLUWXIYLTGVQ"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc7ULRYUHYGFHF5ADXUH33ZRP25Y"))
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::MessageQueue::MessageQueueProcessor:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),"{0})  ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::MessageQueue::MessageQueueProcessor:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("ecc7ULRYUHYGFHF5ADXUH33ZRP25Y"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::MessageQueue::MessageQueueProcessor:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::MessageQueue::MessageQueueProcessor:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWT3EM43DNCLNLELPBPTWABBLI"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMUIYUKLYMRFSTPJXLMEZ5L2DUA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7IMQMPGEX5COVHOB23CGVPAQ6M"),"parallelThreads",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSLODS7DS4JFUNGZKEM3LSVZHBA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:queueId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB5IPJ2R6TRD27KWL3NLR5MG7WY"),"queueId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS7GJSCGQJFHRBF37AINDEIGNFY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:unitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD2DRWL3JUFBCVMQF7CVVQAQ3EA"),"unitId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:unit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3QNYLD7ZNDINGW4I7DZBV4MOQ"),"unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref7T3TT7VY7JG4PEH6XQVJSV2E5Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:messageQueue-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5DTM6IDIBDCLC5EUPSUAWW63M"),"messageQueue",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMSWKEXF5LVEFBJFQGAJ74V3MEA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refX34R2CK4KRGH3DIHG6YU2LLALQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVVLMXYALLZFKTLZR3AG4UK5W4I"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD3KVXC7HPBEBZOMNQNOX2JI2D4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZZEI4AN6ZFLVEDWNQ6VJK2PSU"),"dbTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KBKNZCMLBF3NBRTL4WRZDRWF4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWPQQH54DTFA4HO7U7EFFHMJRMI"),"aasCallTimeoutSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFGA3LKCABFDNGND5AUFQ53IGI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("120")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::MessageQueue::MessageQueueProcessor:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXRTT576Z7FA2NPFSNC2IHGK6GI"),"process",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xProcessRq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4XZQMW24E5CCXJFWSHWWQKKNVE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXZIMLTKPH5EMXB5OLJYBPGCJOI"),"process",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xMessage",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY32UYCG3FFBBVBI5AQ2K6TKDHQ"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::MessageQueue::MessageQueueProcessor - Desktop Executable*/

/*Radix::MessageQueue::MessageQueueProcessor-Entity Class*/

package org.radixware.ads.MessageQueue.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor")
public interface MessageQueueProcessor {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.MessageQueue.explorer.MessageQueueProcessor.MessageQueueProcessor_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.MessageQueue.explorer.MessageQueueProcessor.MessageQueueProcessor_DefaultModel )  super.getEntity(i);}
	}








































































	/*Radix::MessageQueue::MessageQueueProcessor:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:parallelThreads-Presentation Property*/


	public class ParallelThreads extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParallelThreads(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:parallelThreads:parallelThreads")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:parallelThreads:parallelThreads")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParallelThreads getParallelThreads();
	/*Radix::MessageQueue::MessageQueueProcessor:queueId:queueId-Presentation Property*/


	public class QueueId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public QueueId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:queueId:queueId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:queueId:queueId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueId getQueueId();
	/*Radix::MessageQueue::MessageQueueProcessor:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:dbTraceProfile-Presentation Property*/


	public class DbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::MessageQueue::MessageQueueProcessor:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::MessageQueue::MessageQueueProcessor:unit:unit-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unit:unit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unit:unit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Unit getUnit();
	/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:aasCallTimeoutSec-Presentation Property*/


	public class AasCallTimeoutSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AasCallTimeoutSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:aasCallTimeoutSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:aasCallTimeoutSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AasCallTimeoutSec getAasCallTimeoutSec();
	/*Radix::MessageQueue::MessageQueueProcessor:messageQueue:messageQueue-Presentation Property*/


	public class MessageQueue extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public MessageQueue(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.MessageQueue.explorer.MessageQueue.MessageQueue_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.MessageQueue.explorer.MessageQueue.MessageQueue_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.MessageQueue.explorer.MessageQueue.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.MessageQueue.explorer.MessageQueue.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:messageQueue:messageQueue")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:messageQueue:messageQueue")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public MessageQueue getMessageQueue();
	/*Radix::MessageQueue::MessageQueueProcessor:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();


}

/* Radix::MessageQueue::MessageQueueProcessor - Desktop Meta*/

/*Radix::MessageQueue::MessageQueueProcessor-Entity Class*/

package org.radixware.ads.MessageQueue.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueueProcessor_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::MessageQueue::MessageQueueProcessor:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			"Radix::MessageQueue::MessageQueueProcessor",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRLSL53FCOZBR3NDUPV5NZHOBXE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5HLGPG7WZHUHKEIYZKNEJQBEY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),0,

			/*Radix::MessageQueue::MessageQueueProcessor:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::MessageQueue::MessageQueueProcessor:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWT3EM43DNCLNLELPBPTWABBLI"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMUIYUKLYMRFSTPJXLMEZ5L2DUA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7IMQMPGEX5COVHOB23CGVPAQ6M"),
						"parallelThreads",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSLODS7DS4JFUNGZKEM3LSVZHBA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,9999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:queueId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB5IPJ2R6TRD27KWL3NLR5MG7WY"),
						"queueId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:queueId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS7GJSCGQJFHRBF37AINDEIGNFY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3QNYLD7ZNDINGW4I7DZBV4MOQ"),
						"unit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
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
						33029099,
						33030123,false),

					/*Radix::MessageQueue::MessageQueueProcessor:messageQueue:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5DTM6IDIBDCLC5EUPSUAWW63M"),
						"messageQueue",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMSWKEXF5LVEFBJFQGAJ74V3MEA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVVLMXYALLZFKTLZR3AG4UK5W4I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWEEVKI2IT5DQJAS5RJFI4FFDS4")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBU6D3OXQLFGJPK5ZZ64L3N3DNA"),
						33029099,
						33030123,false),

					/*Radix::MessageQueue::MessageQueueProcessor:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD3KVXC7HPBEBZOMNQNOX2JI2D4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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

						/*Radix::MessageQueue::MessageQueueProcessor:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZZEI4AN6ZFLVEDWNQ6VJK2PSU"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KBKNZCMLBF3NBRTL4WRZDRWF4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWPQQH54DTFA4HO7U7EFFHMJRMI"),
						"aasCallTimeoutSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFGA3LKCABFDNGND5AUFQ53IGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("120"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.SPECIFIED,Character.valueOf(' '),(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::MessageQueue::MessageQueueProcessor:ByID-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYSJBJBBPJCSTLDLUWXIYLTGVQ"),
						"ByID",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUR6N62XPBBGXDCZQVDXJMAFTU"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref7T3TT7VY7JG4PEH6XQVJSV2E5Q"),"MessageQueueProcessor=>Unit (unitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colD2DRWL3JUFBCVMQF7CVVQAQ3EA")},new String[]{"unitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refX34R2CK4KRGH3DIHG6YU2LLALQ"),"MessageQueueProcessor=>MessageQueue (queueId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colB5IPJ2R6TRD27KWL3NLR5MG7WY")},new String[]{"queueId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDZ4CALBLBGEJCZMA5VSTVPUGQ")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM")},
			true,false,false);
}

/* Radix::MessageQueue::MessageQueueProcessor - Web Executable*/

/*Radix::MessageQueue::MessageQueueProcessor-Entity Class*/

package org.radixware.ads.MessageQueue.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor")
public interface MessageQueueProcessor {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.MessageQueue.web.MessageQueueProcessor.MessageQueueProcessor_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.MessageQueue.web.MessageQueueProcessor.MessageQueueProcessor_DefaultModel )  super.getEntity(i);}
	}



































































	/*Radix::MessageQueue::MessageQueueProcessor:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:parallelThreads-Presentation Property*/


	public class ParallelThreads extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParallelThreads(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:parallelThreads:parallelThreads")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:parallelThreads:parallelThreads")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParallelThreads getParallelThreads();
	/*Radix::MessageQueue::MessageQueueProcessor:queueId:queueId-Presentation Property*/


	public class QueueId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public QueueId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:queueId:queueId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:queueId:queueId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public QueueId getQueueId();
	/*Radix::MessageQueue::MessageQueueProcessor:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:dbTraceProfile-Presentation Property*/


	public class DbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::MessageQueue::MessageQueueProcessor:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::MessageQueue::MessageQueueProcessor:unit:unit-Presentation Property*/


	public class Unit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Unit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unit:unit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:unit:unit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Unit getUnit();
	/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:aasCallTimeoutSec-Presentation Property*/


	public class AasCallTimeoutSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AasCallTimeoutSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:aasCallTimeoutSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:aasCallTimeoutSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AasCallTimeoutSec getAasCallTimeoutSec();
	/*Radix::MessageQueue::MessageQueueProcessor:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();


}

/* Radix::MessageQueue::MessageQueueProcessor - Web Meta*/

/*Radix::MessageQueue::MessageQueueProcessor-Entity Class*/

package org.radixware.ads.MessageQueue.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueueProcessor_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::MessageQueue::MessageQueueProcessor:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			"Radix::MessageQueue::MessageQueueProcessor",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRLSL53FCOZBR3NDUPV5NZHOBXE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5HLGPG7WZHUHKEIYZKNEJQBEY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),0,

			/*Radix::MessageQueue::MessageQueueProcessor:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::MessageQueue::MessageQueueProcessor:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMWT3EM43DNCLNLELPBPTWABBLI"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMUIYUKLYMRFSTPJXLMEZ5L2DUA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7IMQMPGEX5COVHOB23CGVPAQ6M"),
						"parallelThreads",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSLODS7DS4JFUNGZKEM3LSVZHBA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:parallelThreads:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,9999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:queueId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB5IPJ2R6TRD27KWL3NLR5MG7WY"),
						"queueId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:queueId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS7GJSCGQJFHRBF37AINDEIGNFY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3QNYLD7ZNDINGW4I7DZBV4MOQ"),
						"unit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
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
						33029099,
						33030123,false),

					/*Radix::MessageQueue::MessageQueueProcessor:messageQueue:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5DTM6IDIBDCLC5EUPSUAWW63M"),
						"messageQueue",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMSWKEXF5LVEFBJFQGAJ74V3MEA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVVLMXYALLZFKTLZR3AG4UK5W4I"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),
						null,
						null,
						null,
						33029099,
						33030123,false),

					/*Radix::MessageQueue::MessageQueueProcessor:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD3KVXC7HPBEBZOMNQNOX2JI2D4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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

						/*Radix::MessageQueue::MessageQueueProcessor:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZZEI4AN6ZFLVEDWNQ6VJK2PSU"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KBKNZCMLBF3NBRTL4WRZDRWF4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeDPJZYTP5U5AZHJI724SY2T5B6A"),
						true,

						/*Radix::MessageQueue::MessageQueueProcessor:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWPQQH54DTFA4HO7U7EFFHMJRMI"),
						"aasCallTimeoutSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFGA3LKCABFDNGND5AUFQ53IGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("120"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::MessageQueue::MessageQueueProcessor:aasCallTimeoutSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.SPECIFIED,Character.valueOf(' '),(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::MessageQueue::MessageQueueProcessor:ByID-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYSJBJBBPJCSTLDLUWXIYLTGVQ"),
						"ByID",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUR6N62XPBBGXDCZQVDXJMAFTU"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref7T3TT7VY7JG4PEH6XQVJSV2E5Q"),"MessageQueueProcessor=>Unit (unitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colD2DRWL3JUFBCVMQF7CVVQAQ3EA")},new String[]{"unitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refX34R2CK4KRGH3DIHG6YU2LLALQ"),"MessageQueueProcessor=>MessageQueue (queueId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQZ2AJHN3PFDWXC5BT6I66OS5PQ"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colB5IPJ2R6TRD27KWL3NLR5MG7WY")},new String[]{"queueId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDZ4CALBLBGEJCZMA5VSTVPUGQ")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM")},
			true,false,false);
}

/* Radix::MessageQueue::MessageQueueProcessor:Edit - Desktop Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Edit-Editor Presentation*/

package org.radixware.ads.MessageQueue.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	null,
	null,

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::MessageQueue::MessageQueueProcessor:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVSILKMELHFHEXDFTNJ3L6AGGCY"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6LP2FAHC5DVZBEASTRUBIP6OE"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7IMQMPGEX5COVHOB23CGVPAQ6M"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5DTM6IDIBDCLC5EUPSUAWW63M"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3QNYLD7ZNDINGW4I7DZBV4MOQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEZZEI4AN6ZFLVEDWNQ6VJK2PSU"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWPQQH54DTFA4HO7U7EFFHMJRMI"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),0,2,1,false,false)
			},null),

			/*Radix::MessageQueue::MessageQueueProcessor:Edit:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgI7FRFY2ORJCA3L4YLS3XRFKHTY"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJAOTMKI6SNCTRJT2YNFLSH3LTU"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVSILKMELHFHEXDFTNJ3L6AGGCY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgI7FRFY2ORJCA3L4YLS3XRFKHTY"))}
	,

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::MessageQueue::MessageQueueProcessor:Edit:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJAOTMKI6SNCTRJT2YNFLSH3LTU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::MessageQueue::MessageQueueProcessor:Edit - Web Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Edit-Editor Presentation*/

package org.radixware.ads.MessageQueue.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	null,
	null,

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::MessageQueue::MessageQueueProcessor:Edit:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgI7FRFY2ORJCA3L4YLS3XRFKHTY"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJAOTMKI6SNCTRJT2YNFLSH3LTU"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgI7FRFY2ORJCA3L4YLS3XRFKHTY"))}
	,

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::MessageQueue::MessageQueueProcessor:Edit:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiJAOTMKI6SNCTRJT2YNFLSH3LTU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::MessageQueue::MessageQueueProcessor:Edit:Model - Desktop Executable*/

/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model-Entity Model Class*/

package org.radixware.ads.MessageQueue.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:Edit:Model")
public class Edit:Model  extends org.radixware.ads.MessageQueue.explorer.MessageQueueProcessor.MessageQueueProcessor_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Properties-Properties*/

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Methods-Methods*/

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextId")
	public published  Str getEventContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextType")
	public published  Str getEventContextType () {
		return Arte::EventContextType:MqProcessor.Value;
	}


}

/* Radix::MessageQueue::MessageQueueProcessor:Edit:Model - Desktop Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model-Entity Model Class*/

package org.radixware.ads.MessageQueue.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemIOZWMJBLGVCM7DQZSYFGGUMQWU"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Properties-Properties*/
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

/* Radix::MessageQueue::MessageQueueProcessor:Edit:Model - Web Executable*/

/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model-Entity Model Class*/

package org.radixware.ads.MessageQueue.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:Edit:Model")
public class Edit:Model  extends org.radixware.ads.MessageQueue.web.MessageQueueProcessor.MessageQueueProcessor_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Properties-Properties*/

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Methods-Methods*/

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextId")
	public published  Str getEventContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::MessageQueue::MessageQueueProcessor:Edit:Model:getEventContextType")
	public published  Str getEventContextType () {
		return Arte::EventContextType:MqProcessor.Value;
	}


}

/* Radix::MessageQueue::MessageQueueProcessor:Edit:Model - Web Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model-Entity Model Class*/

package org.radixware.ads.MessageQueue.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemIOZWMJBLGVCM7DQZSYFGGUMQWU"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::MessageQueue::MessageQueueProcessor:Edit:Model:Properties-Properties*/
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

/* Radix::MessageQueue::MessageQueueProcessor:Create - Desktop Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Create-Editor Presentation*/

package org.radixware.ads.MessageQueue.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			null,
			null,
			null,
			null,

			/*Radix::MessageQueue::MessageQueueProcessor:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.MessageQueue.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::MessageQueue::MessageQueueProcessor:Create - Web Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Create-Editor Presentation*/

package org.radixware.ads.MessageQueue.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			null,
			null,
			null,
			null,

			/*Radix::MessageQueue::MessageQueueProcessor:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40114,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.MessageQueue.web.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::MessageQueue::MessageQueueProcessor:Default - Desktop Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Default-Selector Presentation*/

package org.radixware.ads.MessageQueue.explorer;
public final class Default_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Default_mi();
	private Default_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM"),
		"Default",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYSJBJBBPJCSTLDLUWXIYLTGVQ"),
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.MessageQueue.explorer.MessageQueueProcessor.DefaultGroupModel(userSession,this);
	}
}
/* Radix::MessageQueue::MessageQueueProcessor:Default - Web Meta*/

/*Radix::MessageQueue::MessageQueueProcessor:Default-Selector Presentation*/

package org.radixware.ads.MessageQueue.web;
public final class Default_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new Default_mi();
	private Default_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKOE4BKG7TJCGLHEYCXCLNP3LOM"),
		"Default",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYSJBJBBPJCSTLDLUWXIYLTGVQ"),
		null,
		false,
		true,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBXVGQMN5J5BKTDUZH7P6HXGI4E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNQMJB2KUOJEQBJCYNFAGNO7COM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.MessageQueue.web.MessageQueueProcessor.DefaultGroupModel(userSession,this);
	}
}
/* Radix::MessageQueue::MessageQueueProcessor - Localizing Bundle */
package org.radixware.ads.MessageQueue.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageQueueProcessor - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KBKNZCMLBF3NBRTL4WRZDRWF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD3KVXC7HPBEBZOMNQNOX2JI2D4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6LP2FAHC5DVZBEASTRUBIP6OE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message Queue Processors");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчики очередей сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL5HLGPG7WZHUHKEIYZKNEJQBEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message queue");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Очередь сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMSWKEXF5LVEFBJFQGAJ74V3MEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMUIYUKLYMRFSTPJXLMEZ5L2DUA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS7GJSCGQJFHRBF37AINDEIGNFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of processing threads");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество потоков обработки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSLODS7DS4JFUNGZKEM3LSVZHBA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AAS service call timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тайм-аут обращения к сервису AAS (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFGA3LKCABFDNGND5AUFQ53IGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message Queue Processor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик очереди сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRY5Y6KFPVARJECIHIQ6LMCHNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По Идентификатору");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXUR6N62XPBBGXDCZQVDXJMAFTU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MessageQueueProcessor - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),"MessageQueueProcessor - Localizing Bundle",$$$items$$$);
}
