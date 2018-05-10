
/* Radix::SystemMonitor::MetricType.Instance.HeapMem - Server Executable*/

/*Radix::SystemMonitor::MetricType.Instance.HeapMem-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.HeapMem")
public class MetricType.Instance.HeapMem  extends org.radixware.ads.SystemMonitor.server.MetricType.Instance  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Instance.HeapMem_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Instance.HeapMem:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.HeapMem:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Inst.Memory.Heap;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Instance.HeapMem - Server Meta*/

/*Radix::SystemMonitor::MetricType.Instance.HeapMem-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.HeapMem_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),"MetricType.Instance.HeapMem",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRZ4JP3NS75GTLNPDKCRLHNWY4M"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),
							/*Owner Class Name*/
							"MetricType.Instance.HeapMem",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRZ4JP3NS75GTLNPDKCRLHNWY4M"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctHNCB6PXQSJDTFDWDBV7GGIJ7BY"),110.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),

						/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Instance.HeapMem - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Instance.HeapMem-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.HeapMem")
public interface MetricType.Instance.HeapMem   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Instance  {
























}

/* Radix::SystemMonitor::MetricType.Instance.HeapMem - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Instance.HeapMem-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.HeapMem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),
			"Radix::SystemMonitor::MetricType.Instance.HeapMem",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRZ4JP3NS75GTLNPDKCRLHNWY4M"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Instance.HeapMem - Web Meta*/

/*Radix::SystemMonitor::MetricType.Instance.HeapMem-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.HeapMem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Instance.HeapMem:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),
			"Radix::SystemMonitor::MetricType.Instance.HeapMem",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRZ4JP3NS75GTLNPDKCRLHNWY4M"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Instance.HeapMem - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.HeapMem - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция - Процент использования памяти Heap");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance - Heap Memory Usage Percent");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRZ4JP3NS75GTLNPDKCRLHNWY4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Instance.HeapMem - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclBE3OA5VJ5BEFVCPFKB4HT5FNG4"),"MetricType.Instance.HeapMem - Localizing Bundle",$$$items$$$);
}
