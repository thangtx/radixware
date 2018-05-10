
/* Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem - Server Executable*/

/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem")
public class MetricType.Instance.MetaSpaceMem  extends org.radixware.ads.SystemMonitor.server.MetricType.Instance  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Instance.MetaSpaceMem_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Inst.Memory.MetaSpace;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem - Server Meta*/

/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.MetaSpaceMem_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFIEUFKSHNA25LE7GH6RBJPHRU"),"MetricType.Instance.MetaSpaceMem",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTF4A53TPBFJ5IMVDRFAWK2N2I"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFIEUFKSHNA25LE7GH6RBJPHRU"),
							/*Owner Class Name*/
							"MetricType.Instance.MetaSpaceMem",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTF4A53TPBFJ5IMVDRFAWK2N2I"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFIEUFKSHNA25LE7GH6RBJPHRU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctHNCB6PXQSJDTFDWDBV7GGIJ7BY"),130.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFIEUFKSHNA25LE7GH6RBJPHRU"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),

						/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem")
public interface MetricType.Instance.MetaSpaceMem   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Instance  {
























}

/* Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.MetaSpaceMem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFIEUFKSHNA25LE7GH6RBJPHRU"),
			"Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTF4A53TPBFJ5IMVDRFAWK2N2I"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem - Web Meta*/

/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.MetaSpaceMem_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclRFIEUFKSHNA25LE7GH6RBJPHRU"),
			"Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclVDACCMUGFFBSJBQKINASFWDLJ4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTF4A53TPBFJ5IMVDRFAWK2N2I"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Instance.MetaSpaceMem - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Instance.MetaSpaceMem - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция - Использование памяти MetaSpace (мегабайт)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance - MetaSpace Memory Usage (MB)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTF4A53TPBFJ5IMVDRFAWK2N2I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Instance.MetaSpaceMem - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclRFIEUFKSHNA25LE7GH6RBJPHRU"),"MetricType.Instance.MetaSpaceMem - Localizing Bundle",$$$items$$$);
}
