
/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec")
public class MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit.NetPortHandler.Aas  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Net.Aas.InvocationsPerSec;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclY6RFRV2Q7BHMFHGN6DAQAO32JE"),"MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN2DID3C2FFDLFS3I4DR5I77EQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclY6RFRV2Q7BHMFHGN6DAQAO32JE"),
							/*Owner Class Name*/
							"MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN2DID3C2FFDLFS3I4DR5I77EQ"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclY6RFRV2Q7BHMFHGN6DAQAO32JE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),295.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclY6RFRV2Q7BHMFHGN6DAQAO32JE"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5UHOOFXN25BEHCJOCOWG362J6Q"),

						/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec")
public interface MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.NetPortHandler.Aas  {
























}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclY6RFRV2Q7BHMFHGN6DAQAO32JE"),
			"Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5UHOOFXN25BEHCJOCOWG362J6Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN2DID3C2FFDLFS3I4DR5I77EQ"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclY6RFRV2Q7BHMFHGN6DAQAO32JE"),
			"Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("acl5UHOOFXN25BEHCJOCOWG362J6Q"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN2DID3C2FFDLFS3I4DR5I77EQ"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Net Port Handler - Number of AAS Invocations per Second");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обслуживание сетевых портов - Количество обращений к AAS в секунду");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN2DID3C2FFDLFS3I4DR5I77EQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclY6RFRV2Q7BHMFHGN6DAQAO32JE"),"MetricType.Unit.NetPortHandler.Aas.InvocationsPerSec - Localizing Bundle",$$$items$$$);
}
