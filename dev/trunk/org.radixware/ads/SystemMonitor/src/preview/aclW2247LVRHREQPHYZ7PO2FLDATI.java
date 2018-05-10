
/* Radix::SystemMonitor::MetricType.Profiling.Percent.Db - Server Executable*/

/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Percent.Db")
public final class MetricType.Profiling.Percent.Db  extends org.radixware.ads.SystemMonitor.server.MetricType.Instance  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Profiling.Percent.Db_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:controlledValue-Column-Based Property*/






































	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Percent.Db:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Profiling.Percent.Db;
	}

	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:isControlledValueEditable-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Percent.Db:isControlledValueEditable")
	protected published  boolean isControlledValueEditable () {
		return false;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Profiling.Percent.Db - Server Meta*/

/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Percent.Db_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),"MetricType.Profiling.Percent.Db",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDOTYIH5BVGIPMKOEODSKEGTIU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),
							/*Owner Class Name*/
							"MetricType.Profiling.Percent.Db",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDOTYIH5BVGIPMKOEODSKEGTIU"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:controlledValue:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
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
									/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctHR4BWYSWZNDETBR3HCHK7WGRIY"),110.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),

						/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:controlledValue-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),"controlledValue",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBPAH5NSRRBDJJDYOYSW3W2BMU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("AVG")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWWMO5AMVONHYFPXVGHKONUKSPY"),"isControlledValueEditable",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Profiling.Percent.Db - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Percent.Db")
public interface MetricType.Profiling.Percent.Db   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Instance  {
























}

/* Radix::SystemMonitor::MetricType.Profiling.Percent.Db - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Percent.Db_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),
			"Radix::SystemMonitor::MetricType.Profiling.Percent.Db",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDOTYIH5BVGIPMKOEODSKEGTIU"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:controlledValue:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),
						"controlledValue",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						63,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("AVG"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:controlledValue:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
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
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Profiling.Percent.Db - Web Meta*/

/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Percent.Db_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Profiling.Percent.Db:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclW2247LVRHREQPHYZ7PO2FLDATI"),
			"Radix::SystemMonitor::MetricType.Profiling.Percent.Db",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDOTYIH5BVGIPMKOEODSKEGTIU"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Profiling.Percent.Db - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Percent.Db - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Доля времени ожидания базы данных");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Database Time Percentage");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDOTYIH5BVGIPMKOEODSKEGTIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Profiling.Percent.Db - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclW2247LVRHREQPHYZ7PO2FLDATI"),"MetricType.Profiling.Percent.Db - Localizing Bundle",$$$items$$$);
}
