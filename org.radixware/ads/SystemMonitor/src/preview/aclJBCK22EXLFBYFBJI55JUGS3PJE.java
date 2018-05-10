
/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt")
public final class MetricType.Unit.Arte.SessionCnt  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.Arte.SessionCnt_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:unit-Parent Reference*/






































	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Unit.Arte.SessionCnt;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Arte.SessionCnt_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),"MetricType.Unit.Arte.SessionCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsARDH5WIU6NDF3JJDTLTZULW3B4"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),
							/*Owner Class Name*/
							"MetricType.Unit.Arte.SessionCnt",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsARDH5WIU6NDF3JJDTLTZULW3B4"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:unit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciKWBEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6M7MOE3LLVCPVMN3LPBKCMIHGU"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
									40112,
									null,

									/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:General:Children-Explorer Items*/
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
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6M7MOE3LLVCPVMN3LPBKCMIHGU")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctTNNO3THJ4BEYTKUDKXWCKXRAP4"),110.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),

						/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:unit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),"unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJKBPK6MXFHTZBI6UEO7EXYKSE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refBRBSH532FRC3BOQAMSVBUJQA74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt")
public interface MetricType.Unit.Arte.SessionCnt   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit  {


















































}

/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Arte.SessionCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),
			"Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsARDH5WIU6NDF3JJDTLTZULW3B4"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),
						"unit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						55,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43QMEDG2SVAY5MEQ6XPTEKCYTM"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						133693439,
						133693439,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6M7MOE3LLVCPVMN3LPBKCMIHGU")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Arte.SessionCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),
			"Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsARDH5WIU6NDF3JJDTLTZULW3B4"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:General - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:General-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi{
	private static final class General_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		General_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6M7MOE3LLVCPVMN3LPBKCMIHGU"),
			"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprQZBCBFUUOZHCJL2XJ2L3PATCRM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJBCK22EXLFBYFBJI55JUGS3PJE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,
			null,
			null,

			/*Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt:General:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			40112,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.Arte.SessionCnt.MetricType.Unit.Arte.SessionCnt_DefaultModel.eprQZBCBFUUOZHCJL2XJ2L3PATCRM_ModelAdapter(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new General_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType.Unit.Arte.SessionCnt - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Arte.SessionCnt - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль - Количество активных ARTE сессий");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit - Number of Active ARTE Sessions");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsARDH5WIU6NDF3JJDTLTZULW3B4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.Arte.SessionCnt - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclJBCK22EXLFBYFBJI55JUGS3PJE"),"MetricType.Unit.Arte.SessionCnt - Localizing Bundle",$$$items$$$);
}
