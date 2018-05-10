
/* Radix::System::Task.DailyMaintenance - Server Executable*/

/*Radix::System::Task.DailyMaintenance-Application Class*/

package org.radixware.ads.System.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DailyMaintenance")
 class Task.DailyMaintenance  extends org.radixware.ads.Scheduling.server.Task.Atomic  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Task.DailyMaintenance_mi.rdxMeta;}

	/*Radix::System::Task.DailyMaintenance:Nested classes-Nested Classes*/

	/*Radix::System::Task.DailyMaintenance:Properties-Properties*/

	/*Radix::System::Task.DailyMaintenance:expiredPolicy-Column-Based Property*/






































	/*Radix::System::Task.DailyMaintenance:Methods-Methods*/

	/*Radix::System::Task.DailyMaintenance:execute-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DailyMaintenance:execute")
	public published  void execute (java.sql.Timestamp prevExecTime, java.sql.Timestamp curExecTime) {
		Maintenance.daily();
	}

	/*Radix::System::Task.DailyMaintenance:canBeManual-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DailyMaintenance:canBeManual")
	public published  boolean canBeManual () {
		return false;
	}

	/*Radix::System::Task.DailyMaintenance:isSingletone-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DailyMaintenance:isSingletone")
	public published  boolean isSingletone () {
		return true;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::System::Task.DailyMaintenance - Server Meta*/

/*Radix::System::Task.DailyMaintenance-Application Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DailyMaintenance_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),"Task.DailyMaintenance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFO344YGVLOBDCLSAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::System::Task.DailyMaintenance:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),
							/*Owner Class Name*/
							"Task.DailyMaintenance",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFO344YGVLOBDCLSAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::System::Task.DailyMaintenance:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::Task.DailyMaintenance:expiredPolicy:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::System::Task.DailyMaintenance:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccVXWRKQLYJTOBDCIVAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctBVFUCASJQBEZJA7YW67UOIRROI"),100.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblWZB7K4HLJPOBDCIUAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),

						/*Radix::System::Task.DailyMaintenance:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::Task.DailyMaintenance:expiredPolicy-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),"expiredPolicy",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJB6AQM6NRRDRHDOG7KENX27GIQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsMX5WQ6IW55D5BMVQYQBOPJEMSQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::System::Task.DailyMaintenance:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthN36NOGMSW5EGZIX2JAVCBWM2RU"),"execute",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prevExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNSLYPRMPDNF57OMKPTZREWY4UU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("curExecTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOCLRZN2HM5HVLAFLTWCEQFAHUA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTMY4LSZKOVENHEHZ2G7FKDNXUU"),"canBeManual",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKIF62UYHLRDRPPGLIIMJRLXSUI"),"isSingletone",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::System::Task.DailyMaintenance - Desktop Executable*/

/*Radix::System::Task.DailyMaintenance-Application Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Task.DailyMaintenance")
public interface Task.DailyMaintenance   extends org.radixware.ads.Scheduling.explorer.Task.Atomic  {
























}

/* Radix::System::Task.DailyMaintenance - Desktop Meta*/

/*Radix::System::Task.DailyMaintenance-Application Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DailyMaintenance_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Task.DailyMaintenance:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),
			"Radix::System::Task.DailyMaintenance",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFO344YGVLOBDCLSAALOMT5GDM"),null,null,0,

			/*Radix::System::Task.DailyMaintenance:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Task.DailyMaintenance:expiredPolicy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYQWRAN7SK5GA3EGFI6TEPHYWL4"),
						"expiredPolicy",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						61,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsMX5WQ6IW55D5BMVQYQBOPJEMSQ"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Task.DailyMaintenance:expiredPolicy:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsMX5WQ6IW55D5BMVQYQBOPJEMSQ"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
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

/* Radix::System::Task.DailyMaintenance - Web Meta*/

/*Radix::System::Task.DailyMaintenance-Application Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DailyMaintenance_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Task.DailyMaintenance:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclABO344YGVLOBDCLSAALOMT5GDM"),
			"Radix::System::Task.DailyMaintenance",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclZDLN466RV5FE3BQIWSX56ZAETQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFO344YGVLOBDCLSAALOMT5GDM"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::System::Task.DailyMaintenance - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Task.DailyMaintenance - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Daily System Maintenance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ежедневное обслуживание системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAFO344YGVLOBDCLSAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Task.DailyMaintenance - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclABO344YGVLOBDCLSAALOMT5GDM"),"Task.DailyMaintenance - Localizing Bundle",$$$items$$$);
}
