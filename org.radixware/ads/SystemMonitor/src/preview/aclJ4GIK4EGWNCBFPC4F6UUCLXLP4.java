
/* Radix::SystemMonitor::MetricType.Unit.PersoComm - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.PersoComm")
public abstract class MetricType.Unit.PersoComm  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.PersoComm_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.Unit.PersoComm:unit-Parent Reference*/






































	/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Methods-Methods*/


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),"MetricType.Unit.PersoComm",null,

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),
							/*Owner Class Name*/
							"MetricType.Unit.PersoComm",
							/*Title Id*/
							null,
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.Unit.PersoComm:unit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> in (\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aci63XTIIPIKFA6RILAJ7X4UBHVQA\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciKSBEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aci2RGIQV74LVDE5NOE2WRRFUDOYY\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciKOBEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciKCBEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciKKBEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciKGBEJTYZVPORDJHCAANE2UAFXA\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciQBQVMQMNYRG4RD2ADOJOMZ3JGI\"/></xsc:Item><xsc:Item><xsc:Sql>,\n        </xsc:Sql></xsc:Item><xsc:Item><xsc:ConstValue EnumId=\"acsLSG35D4GIHNRDJIEACQMTAIZT4\" ItemId=\"aciQ3U6UX4EOBDT7HPRMTVWLMZVBE\"/></xsc:Item><xsc:Item><xsc:Sql>)\n</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							null,
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/
							null,
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDROZAAWX25DKPC4RCACXPIGZCA"),null,220.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGUFZJDT6RGB7KZFR4F7XGD44Q"))}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),

						/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.Unit.PersoComm:unit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),"unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJKBPK6MXFHTZBI6UEO7EXYKSE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refBRBSH532FRC3BOQAMSVBUJQA74"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Methods-Methods*/
						null,
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.PersoComm")
public interface MetricType.Unit.PersoComm   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit  {
























}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),
			"Radix::SystemMonitor::MetricType.Unit.PersoComm",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
			null,
			null,
			null,null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.Unit.PersoComm:unit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSFAITPT5JEYNOOD7WASUJ3LOU"),
						"unit",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),
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
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.PersoComm:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),
			"Radix::SystemMonitor::MetricType.Unit.PersoComm",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclFJSN77CFJJDCDLOZXJ2X3WQUBE"),
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

/* Radix::SystemMonitor::MetricType.Unit.PersoComm - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал персональных коммуникаций");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Personal Communication Channel");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSGUFZJDT6RGB7KZFR4F7XGD44Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.PersoComm - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),"MetricType.Unit.PersoComm - Localizing Bundle",$$$items$$$);
}
