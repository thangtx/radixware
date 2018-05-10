
/* Radix::Scheduling::Unit.JobExecutor - Server Executable*/

/*Radix::Scheduling::Unit.JobExecutor-Application Class*/

package org.radixware.ads.Scheduling.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor")
public published class Unit.JobExecutor  extends org.radixware.ads.System.server.Unit.SingletonAbstractBase  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit.JobExecutor_mi.rdxMeta;}

	/*Radix::Scheduling::Unit.JobExecutor:Nested classes-Nested Classes*/

	/*Radix::Scheduling::Unit.JobExecutor:Properties-Properties*/

	/*Radix::Scheduling::Unit.JobExecutor:parallelCnt-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:parallelCnt")
	public published  Int getParallelCnt() {
		return parallelCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:parallelCnt")
	public published   void setParallelCnt(Int val) {
		parallelCnt = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:execPeriod-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:execPeriod")
	public published  Num getExecPeriod() {
		return execPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:execPeriod")
	public published   void setExecPeriod(Num val) {
		execPeriod = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta")
	public published  Int getAboveNormalDelta() {
		return aboveNormalDelta;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta")
	public published   void setAboveNormalDelta(Int val) {
		aboveNormalDelta = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:highDelta-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:highDelta")
	public published  Int getHighDelta() {
		return highDelta;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:highDelta")
	public published   void setHighDelta(Int val) {
		highDelta = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:veryHighDelta")
	public published  Int getVeryHighDelta() {
		return veryHighDelta;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:veryHighDelta")
	public published   void setVeryHighDelta(Int val) {
		veryHighDelta = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:criticalDelta-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:criticalDelta")
	public published  Int getCriticalDelta() {
		return criticalDelta;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:criticalDelta")
	public published   void setCriticalDelta(Int val) {
		criticalDelta = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:avgExecCount-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgExecCount")
	public published  Num getAvgExecCount() {
		return avgExecCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgExecCount")
	public published   void setAvgExecCount(Num val) {
		avgExecCount = val;
	}

	/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount-Detail Column Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgWaitCount")
	public  Num getAvgWaitCount() {
		return avgWaitCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgWaitCount")
	public   void setAvgWaitCount(Num val) {
		avgWaitCount = val;
	}













































































	/*Radix::Scheduling::Unit.JobExecutor:Methods-Methods*/

	/*Radix::Scheduling::Unit.JobExecutor:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		type=System::UnitType:JobExecutor;
	}

	/*Radix::Scheduling::Unit.JobExecutor:getUsedAddresses-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:getUsedAddresses")
	public published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		return null;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Scheduling::Unit.JobExecutor - Server Meta*/

/*Radix::Scheduling::Unit.JobExecutor-Application Class*/

package org.radixware.ads.Scheduling.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JobExecutor_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),"Unit.JobExecutor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YUVQOK4OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::Scheduling::Unit.JobExecutor:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
							/*Owner Class Name*/
							"Unit.JobExecutor",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YUVQOK4OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::Scheduling::Unit.JobExecutor:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFUSSBVSND7PBDMRRABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::Scheduling::Unit.JobExecutor:execPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLETMMJJY3NRDB6YAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7MRFYQR72JFWRFU64UIBOWU3DE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Unit.JobExecutor:highDelta:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRQGOY4M2DVBOBGRXOL5M7X5H2Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE7VFSCMMPBB5VAA62IZDS5QPJQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAI3BW64PA5BX3AHQWT3JINC5W4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXA4RSYOIREI3HY2XNK6Q3F4OQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2C3352WQAJEFXO7K5IUBW5257M"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
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
									/*Radix::Scheduling::Unit.JobExecutor:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CPN5BSZHJAN5NTGR7LYEXFEDQ"),
									27826,
									null,

									/*Radix::Scheduling::Unit.JobExecutor:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Scheduling::Unit.JobExecutor:Edit:JobQueueItem-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiIYWWEYJ5DFHCXLYZBDHCAM5PAU"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Scheduling::Unit.JobExecutor:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTD3DFCQBGHOBDCHWAALOMT5GDM"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),
									15536,
									null,

									/*Radix::Scheduling::Unit.JobExecutor:Create:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTD3DFCQBGHOBDCHWAALOMT5GDM")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Scheduling::Unit.JobExecutor:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctIM3THFAESQAESFLHAAJZREMVEY"),300.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDPASXTH4VHOBDCLSAALOMT5GDM"),

						/*Radix::Scheduling::Unit.JobExecutor:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Scheduling::Unit.JobExecutor:parallelCnt-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFUSSBVSND7PBDMRRABIFNQAABA"),"parallelCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7IHB4KND7PBDMRRABIFNQAABA"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colPJDE6LVONJB3PPTQ7ZCF6UPQOI"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("50")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:execPeriod-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLETMMJJY3NRDB6YAALOMT5GDM"),"execPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.NUM,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colCCFCKQ4OIHNRDJIEACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0.1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7MRFYQR72JFWRFU64UIBOWU3DE"),"aboveNormalDelta",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3FLHV4JFQ5HT7BUMLKSPEKHAXE"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colYS346KYNLVBNFJEHV2EA2KPKKM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:highDelta-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRQGOY4M2DVBOBGRXOL5M7X5H2Y"),"highDelta",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSAQ47T7P3BAQZHJFVKECCNQZX4"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colHBV4J6LEWNCOTM4WHJRSIMLKFM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE7VFSCMMPBB5VAA62IZDS5QPJQ"),"veryHighDelta",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2P3AEBOM7RDS5BKM7BVTT5WPMA"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNJB3FZVNRNGXXMYCHMBRAXJZI4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:criticalDelta-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAI3BW64PA5BX3AHQWT3JINC5W4"),"criticalDelta",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls327E6Q3CDRDCDNF24NOV2RFISQ"),org.radixware.kernel.common.enums.EValType.INT,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colUX424WYE3ZGARCSQMV4REKOFK4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:avgExecCount-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXA4RSYOIREI3HY2XNK6Q3F4OQ"),"avgExecCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKAT44EVIJDL7FPLHCMYC4I6UU"),org.radixware.kernel.common.enums.EValType.NUM,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOBNJSQM5BFTJLTZLAOM4U2QOE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount-Detail Column Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDetailPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2C3352WQAJEFXO7K5IUBW5257M"),"avgWaitCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5YPL7A57DVFONIHHUW4TZH2OME"),org.radixware.kernel.common.enums.EValType.NUM,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colUT45Q6GJB5EZ7ESVA3I35OFKKE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Scheduling::Unit.JobExecutor:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRBOOREXOAZBHBOWEVXGJJWHOYQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIZVNX5QNRC7HEVGYVREIK7STI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refJXQCGBUPIHNRDJIEACQMTAIZT4")},
						null,null,null,false);
}

/* Radix::Scheduling::Unit.JobExecutor - Desktop Executable*/

/*Radix::Scheduling::Unit.JobExecutor-Application Class*/

package org.radixware.ads.Scheduling.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor")
public interface Unit.JobExecutor   extends org.radixware.ads.System.explorer.Unit.SingletonAbstractBase  {











































































































































	/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:avgWaitCount-Presentation Property*/


	public class AvgWaitCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgWaitCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgWaitCount:avgWaitCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgWaitCount:avgWaitCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgWaitCount getAvgWaitCount();
	/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:aboveNormalDelta-Presentation Property*/


	public class AboveNormalDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AboveNormalDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:aboveNormalDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:aboveNormalDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AboveNormalDelta getAboveNormalDelta();
	/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:criticalDelta-Presentation Property*/


	public class CriticalDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CriticalDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:criticalDelta:criticalDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:criticalDelta:criticalDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CriticalDelta getCriticalDelta();
	/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:veryHighDelta-Presentation Property*/


	public class VeryHighDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public VeryHighDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:veryHighDelta:veryHighDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:veryHighDelta:veryHighDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public VeryHighDelta getVeryHighDelta();
	/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:parallelCnt-Presentation Property*/


	public class ParallelCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParallelCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:parallelCnt:parallelCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:parallelCnt:parallelCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParallelCnt getParallelCnt();
	/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:avgExecCount-Presentation Property*/


	public class AvgExecCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgExecCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgExecCount:avgExecCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgExecCount:avgExecCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgExecCount getAvgExecCount();
	/*Radix::Scheduling::Unit.JobExecutor:execPeriod:execPeriod-Presentation Property*/


	public class ExecPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public ExecPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:execPeriod:execPeriod")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:execPeriod:execPeriod")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public ExecPeriod getExecPeriod();
	/*Radix::Scheduling::Unit.JobExecutor:highDelta:highDelta-Presentation Property*/


	public class HighDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:highDelta:highDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:highDelta:highDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighDelta getHighDelta();


}

/* Radix::Scheduling::Unit.JobExecutor - Desktop Meta*/

/*Radix::Scheduling::Unit.JobExecutor-Application Class*/

package org.radixware.ads.Scheduling.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JobExecutor_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Unit.JobExecutor:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
			"Radix::Scheduling::Unit.JobExecutor",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDPASXTH4VHOBDCLSAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YUVQOK4OXOBDFKUAAMPGXUWTQ"),null,null,0,

			/*Radix::Scheduling::Unit.JobExecutor:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFUSSBVSND7PBDMRRABIFNQAABA"),
						"parallelCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7IHB4KND7PBDMRRABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("50"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,1000L,(byte)0,Character.valueOf('0'),1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:execPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLETMMJJY3NRDB6YAALOMT5GDM"),
						"execPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0.1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						false,

						/*Radix::Scheduling::Unit.JobExecutor:execPeriod:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(new java.math.BigDecimal("0.05",java.math.MathContext.UNLIMITED),null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,null,(byte)2),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7MRFYQR72JFWRFU64UIBOWU3DE"),
						"aboveNormalDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3FLHV4JFQ5HT7BUMLKSPEKHAXE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:highDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRQGOY4M2DVBOBGRXOL5M7X5H2Y"),
						"highDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSAQ47T7P3BAQZHJFVKECCNQZX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:highDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE7VFSCMMPBB5VAA62IZDS5QPJQ"),
						"veryHighDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2P3AEBOM7RDS5BKM7BVTT5WPMA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAI3BW64PA5BX3AHQWT3JINC5W4"),
						"criticalDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls327E6Q3CDRDCDNF24NOV2RFISQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXA4RSYOIREI3HY2XNK6Q3F4OQ"),
						"avgExecCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKAT44EVIJDL7FPLHCMYC4I6UU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2C3352WQAJEFXO7K5IUBW5257M"),
						"avgWaitCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5YPL7A57DVFONIHHUW4TZH2OME"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTD3DFCQBGHOBDCHWAALOMT5GDM")},
			true,true,false);
}

/* Radix::Scheduling::Unit.JobExecutor - Web Executable*/

/*Radix::Scheduling::Unit.JobExecutor-Application Class*/

package org.radixware.ads.Scheduling.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor")
public interface Unit.JobExecutor   extends org.radixware.ads.System.web.Unit.SingletonAbstractBase  {











































































































































	/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:avgWaitCount-Presentation Property*/


	public class AvgWaitCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgWaitCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgWaitCount:avgWaitCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgWaitCount:avgWaitCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgWaitCount getAvgWaitCount();
	/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:aboveNormalDelta-Presentation Property*/


	public class AboveNormalDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AboveNormalDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:aboveNormalDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:aboveNormalDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AboveNormalDelta getAboveNormalDelta();
	/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:criticalDelta-Presentation Property*/


	public class CriticalDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CriticalDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:criticalDelta:criticalDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:criticalDelta:criticalDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CriticalDelta getCriticalDelta();
	/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:veryHighDelta-Presentation Property*/


	public class VeryHighDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public VeryHighDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:veryHighDelta:veryHighDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:veryHighDelta:veryHighDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public VeryHighDelta getVeryHighDelta();
	/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:parallelCnt-Presentation Property*/


	public class ParallelCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ParallelCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:parallelCnt:parallelCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:parallelCnt:parallelCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ParallelCnt getParallelCnt();
	/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:avgExecCount-Presentation Property*/


	public class AvgExecCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgExecCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgExecCount:avgExecCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:avgExecCount:avgExecCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgExecCount getAvgExecCount();
	/*Radix::Scheduling::Unit.JobExecutor:execPeriod:execPeriod-Presentation Property*/


	public class ExecPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public ExecPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:execPeriod:execPeriod")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:execPeriod:execPeriod")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public ExecPeriod getExecPeriod();
	/*Radix::Scheduling::Unit.JobExecutor:highDelta:highDelta-Presentation Property*/


	public class HighDelta extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighDelta(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:highDelta:highDelta")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Scheduling::Unit.JobExecutor:highDelta:highDelta")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighDelta getHighDelta();


}

/* Radix::Scheduling::Unit.JobExecutor - Web Meta*/

/*Radix::Scheduling::Unit.JobExecutor-Application Class*/

package org.radixware.ads.Scheduling.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JobExecutor_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Scheduling::Unit.JobExecutor:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
			"Radix::Scheduling::Unit.JobExecutor",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclDPASXTH4VHOBDCLSAALOMT5GDM"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YUVQOK4OXOBDFKUAAMPGXUWTQ"),null,null,0,

			/*Radix::Scheduling::Unit.JobExecutor:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFUSSBVSND7PBDMRRABIFNQAABA"),
						"parallelCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7IHB4KND7PBDMRRABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						0,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("50"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:parallelCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,1000L,(byte)0,Character.valueOf('0'),1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:execPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLETMMJJY3NRDB6YAALOMT5GDM"),
						"execPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0.1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:execPeriod:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(new java.math.BigDecimal("0.05",java.math.MathContext.UNLIMITED),null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,null,(byte)2),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7MRFYQR72JFWRFU64UIBOWU3DE"),
						"aboveNormalDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3FLHV4JFQ5HT7BUMLKSPEKHAXE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:aboveNormalDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:highDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRQGOY4M2DVBOBGRXOL5M7X5H2Y"),
						"highDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSAQ47T7P3BAQZHJFVKECCNQZX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:highDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE7VFSCMMPBB5VAA62IZDS5QPJQ"),
						"veryHighDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2P3AEBOM7RDS5BKM7BVTT5WPMA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:veryHighDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAI3BW64PA5BX3AHQWT3JINC5W4"),
						"criticalDelta",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls327E6Q3CDRDCDNF24NOV2RFISQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Scheduling::Unit.JobExecutor:criticalDelta:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXA4RSYOIREI3HY2XNK6Q3F4OQ"),
						"avgExecCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKAT44EVIJDL7FPLHCMYC4I6UU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::Scheduling::Unit.JobExecutor:avgExecCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2C3352WQAJEFXO7K5IUBW5257M"),
						"avgWaitCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5YPL7A57DVFONIHHUW4TZH2OME"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.NUM,
						org.radixware.kernel.common.enums.EPropNature.DETAIL_PROP,
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

						/*Radix::Scheduling::Unit.JobExecutor:avgWaitCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			null,
			true,true,false);
}

/* Radix::Scheduling::Unit.JobExecutor:Edit - Desktop Meta*/

/*Radix::Scheduling::Unit.JobExecutor:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CPN5BSZHJAN5NTGR7LYEXFEDQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,

			/*Radix::Scheduling::Unit.JobExecutor:Edit:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Scheduling::Unit.JobExecutor:Edit:General-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ56EWJQN2ZAL5ITXPY6JDM5MLQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),0,0,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),0,2,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),0,3,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),0,4,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),0,11,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLETMMJJY3NRDB6YAALOMT5GDM"),0,5,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFUSSBVSND7PBDMRRABIFNQAABA"),0,6,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAI3BW64PA5BX3AHQWT3JINC5W4"),0,10,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE7VFSCMMPBB5VAA62IZDS5QPJQ"),0,9,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRQGOY4M2DVBOBGRXOL5M7X5H2Y"),0,8,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7MRFYQR72JFWRFU64UIBOWU3DE"),0,7,2,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHXA4RSYOIREI3HY2XNK6Q3F4OQ"),0,13,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2C3352WQAJEFXO7K5IUBW5257M"),1,13,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),1,11,1,true,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),1,12,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),0,12,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),0,1,2,false,false)
					},null),

					/*Radix::Scheduling::Unit.JobExecutor:Edit:Jobs-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgYNNRFJJGPNCXDLPRMKSJAGN72Y"),"Jobs",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiIYWWEYJ5DFHCXLYZBDHCAM5PAU")),

					/*Radix::Scheduling::Unit.JobExecutor:Edit:Additional-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgXWVLAPMSONBEHEVDLX6WDMTGXE"),"Additional",org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5ZTR7BCDFF6VL6B7LAXCDPQXA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFK3IJM2HNGF5N3YAIKN5FJKL4"),0,0,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgXWVLAPMSONBEHEVDLX6WDMTGXE")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgYNNRFJJGPNCXDLPRMKSJAGN72Y")),
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
			,

			/*Radix::Scheduling::Unit.JobExecutor:Edit:Children-Explorer Items*/
				new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

						/*Radix::Scheduling::Unit.JobExecutor:Edit:JobQueueItem-Entity Explorer Item*/

						new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiIYWWEYJ5DFHCXLYZBDHCAM5PAU"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),null,
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHRWUWEEMIHNRDJIEACQMTAIZT4"),
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWCIUVAQFWHNRDCJQABIFNQAABA"),
							0,
							null,
							16560,true)
				}
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			27826,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.explorer.Unit.JobExecutor.Unit.JobExecutor_DefaultModel.epr6CPN5BSZHJAN5NTGR7LYEXFEDQ_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::Scheduling::Unit.JobExecutor:Edit - Web Meta*/

/*Radix::Scheduling::Unit.JobExecutor:Edit-Editor Presentation*/

package org.radixware.ads.Scheduling.web;
public final class Edit_mi{
	private static final class Edit_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Edit_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),
			"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6CPN5BSZHJAN5NTGR7LYEXFEDQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,

			/*Radix::Scheduling::Unit.JobExecutor:Edit:Editor Pages-Editor Presentation Pages*/
			null,
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
			}
			,

			/*Radix::Scheduling::Unit.JobExecutor:Edit:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			27826,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Scheduling.web.Unit.JobExecutor.Unit.JobExecutor_DefaultModel.epr6CPN5BSZHJAN5NTGR7LYEXFEDQ_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Edit_DEF(); 
;
}
/* Radix::Scheduling::Unit.JobExecutor:Create - Desktop Meta*/

/*Radix::Scheduling::Unit.JobExecutor:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTD3DFCQBGHOBDCHWAALOMT5GDM"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::Scheduling::Unit.JobExecutor:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			15536,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Scheduling::Unit.JobExecutor:Create - Web Meta*/

/*Radix::Scheduling::Unit.JobExecutor:Create-Editor Presentation*/

package org.radixware.ads.Scheduling.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprTD3DFCQBGHOBDCHWAALOMT5GDM"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQK6UHHXIX7NRDB6TAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclESARTHXEX7NRDB6TAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			null,
			null,
			null,
			null,

			/*Radix::Scheduling::Unit.JobExecutor:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			15536,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Scheduling::Unit.JobExecutor - Localizing Bundle */
package org.radixware.ads.Scheduling.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit.JobExecutor - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Very high priority delta");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для очень высокого приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2P3AEBOM7RDS5BKM7BVTT5WPMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Critical priority delta");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для критического приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls327E6Q3CDRDCDNF24NOV2RFISQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Above normal priority delta");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для приоритета выше обычного");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3FLHV4JFQ5HT7BUMLKSPEKHAXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Job Executor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исполнитель заданий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3YUVQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Average number of waiting jobs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Среднее количество заданий, ожидающих выполнения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5YPL7A57DVFONIHHUW4TZH2OME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of threads for normal or lower priority");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество потоков для нормального или пониженного приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7IHB4KND7PBDMRRABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Average number of running jobs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Среднее количество исполняющихся заданий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLKAT44EVIJDL7FPLHCMYC4I6UU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ56EWJQN2ZAL5ITXPY6JDM5MLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"High priority delta");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для высокого приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSAQ47T7P3BAQZHJFVKECCNQZX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Additional");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дополнительно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5ZTR7BCDFF6VL6B7LAXCDPQXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Execution period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период исполнения (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit.JobExecutor - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclESARTHXEX7NRDB6TAALOMT5GDM"),"Unit.JobExecutor - Localizing Bundle",$$$items$$$);
}
