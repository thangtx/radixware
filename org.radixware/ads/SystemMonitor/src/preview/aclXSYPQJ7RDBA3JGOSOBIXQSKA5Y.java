
/* Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration")
public class MetricType.Unit.PersoComm.MessageReceivedProcDuration  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit.PersoComm  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.PersoComm.MessageReceivedProcDuration_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Unit.PersoComm.MessageReceivedProcDuration;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm.MessageReceivedProcDuration_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),"MetricType.Unit.PersoComm.MessageReceivedProcDuration",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOXIBSN2GJFYJINJV7BTZ2QUIU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),
							/*Owner Class Name*/
							"MetricType.Unit.PersoComm.MessageReceivedProcDuration",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOXIBSN2GJFYJINJV7BTZ2QUIU"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDROZAAWX25DKPC4RCACXPIGZCA"),160.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),

						/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration")
public interface MetricType.Unit.PersoComm.MessageReceivedProcDuration   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.PersoComm  {
























}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm.MessageReceivedProcDuration_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),
			"Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOXIBSN2GJFYJINJV7BTZ2QUIU"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm.MessageReceivedProcDuration_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),
			"Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclJ4GIK4EGWNCBFPC4F6UUCLXLP4"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOXIBSN2GJFYJINJV7BTZ2QUIU"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.PersoComm.MessageReceivedProcDuration - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.PersoComm.MessageReceivedProcDuration - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал персональных коммуникаций - Длительность обработки принятого сообщения");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Personal Communication Channel - Received Message Processing Duration");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOXIBSN2GJFYJINJV7BTZ2QUIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.PersoComm.MessageReceivedProcDuration - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclXSYPQJ7RDBA3JGOSOBIXQSKA5Y"),"MetricType.Unit.PersoComm.MessageReceivedProcDuration - Localizing Bundle",$$$items$$$);
}