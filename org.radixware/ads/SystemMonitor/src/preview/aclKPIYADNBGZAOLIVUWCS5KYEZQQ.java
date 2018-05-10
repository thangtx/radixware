
/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal")
public class MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit.NetPortHandler.Aas  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Net.Aas.ProcDurationTotal;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclKPIYADNBGZAOLIVUWCS5KYEZQQ"),"MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7YQTOPALFETHCNC4FNAWRNRBI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclKPIYADNBGZAOLIVUWCS5KYEZQQ"),
							/*Owner Class Name*/
							"MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7YQTOPALFETHCNC4FNAWRNRBI"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclKPIYADNBGZAOLIVUWCS5KYEZQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),285.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclKPIYADNBGZAOLIVUWCS5KYEZQQ"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5UHOOFXN25BEHCJOCOWG362J6Q"),

						/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal")
public interface MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.NetPortHandler.Aas  {
























}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclKPIYADNBGZAOLIVUWCS5KYEZQQ"),
			"Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5UHOOFXN25BEHCJOCOWG362J6Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7YQTOPALFETHCNC4FNAWRNRBI"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclKPIYADNBGZAOLIVUWCS5KYEZQQ"),
			"Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5UHOOFXN25BEHCJOCOWG362J6Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7YQTOPALFETHCNC4FNAWRNRBI"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Net Port Handler - AAS Processing Total Duration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обслуживание сетевых портов - Полное время обработки в AAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ7YQTOPALFETHCNC4FNAWRNRBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclKPIYADNBGZAOLIVUWCS5KYEZQQ"),"MetricType.Unit.NetPortHandler.Aas.ProcDurationTotal - Localizing Bundle",$$$items$$$);
}
