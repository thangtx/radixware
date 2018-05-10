
/* Radix::SystemMonitor::MetricType.Profiling.Duration - Server Executable*/

/*Radix::SystemMonitor::MetricType.Profiling.Duration-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Duration")
public final class MetricType.Profiling.Duration  extends org.radixware.ads.SystemMonitor.server.MetricType.Profiling  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Profiling.Duration_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Profiling.Duration:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Profiling.Duration:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Profiling.Duration:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Profiling.Duration:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Duration:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Profiling.Duration;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Profiling.Duration - Server Meta*/

/*Radix::SystemMonitor::MetricType.Profiling.Duration-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Duration_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),"MetricType.Profiling.Duration",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM76WKNP7VZH3RLYL33K4PWK4H4"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Profiling.Duration:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),
							/*Owner Class Name*/
							"MetricType.Profiling.Duration",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM76WKNP7VZH3RLYL33K4PWK4H4"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Profiling.Duration:Properties-Properties*/
							null,
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
									/*Radix::SystemMonitor::MetricType.Profiling.Duration:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctHR4BWYSWZNDETBR3HCHK7WGRIY"),140.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclA4SR72ELAVEWTMRWOYXOK6EANU"),

						/*Radix::SystemMonitor::MetricType.Profiling.Duration:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Profiling.Duration:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Profiling.Duration - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Profiling.Duration-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Profiling.Duration")
public interface MetricType.Profiling.Duration   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Profiling  {
























}

/* Radix::SystemMonitor::MetricType.Profiling.Duration - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Profiling.Duration-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Duration_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Profiling.Duration:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),
			"Radix::SystemMonitor::MetricType.Profiling.Duration",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclA4SR72ELAVEWTMRWOYXOK6EANU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM76WKNP7VZH3RLYL33K4PWK4H4"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Profiling.Duration:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Profiling.Duration - Web Meta*/

/*Radix::SystemMonitor::MetricType.Profiling.Duration-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Duration_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Profiling.Duration:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),
			"Radix::SystemMonitor::MetricType.Profiling.Duration",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclA4SR72ELAVEWTMRWOYXOK6EANU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM76WKNP7VZH3RLYL33K4PWK4H4"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Profiling.Duration - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Profiling.Duration - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ARTE - Длительность исполнения секции профилирования");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE - Profiling Section Execution Duration");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM76WKNP7VZH3RLYL33K4PWK4H4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Profiling.Duration - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclJ6BHRMSWBZFRLPUEXHMDWXP66Y"),"MetricType.Profiling.Duration - Localizing Bundle",$$$items$$$);
}
