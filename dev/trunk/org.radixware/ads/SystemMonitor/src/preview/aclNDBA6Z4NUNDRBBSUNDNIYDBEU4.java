
/* Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt")
public class MetricType.Unit.Mq.UsedArteCnt  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit.Mq  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.Mq.UsedArteCnt_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Unit.Mq.UsedArteCnt;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.UsedArteCnt_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),"MetricType.Unit.Mq.UsedArteCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIY6WD3JLMFB7JCAF6UGK5MKJQI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),
							/*Owner Class Name*/
							"MetricType.Unit.Mq.UsedArteCnt",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIY6WD3JLMFB7JCAF6UGK5MKJQI"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDKISRODKXRERDGL5SXFJVUM5TQ"),100.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOB57CSUJFN7A2O7PRNFX4YQE"),

						/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt")
public interface MetricType.Unit.Mq.UsedArteCnt   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.Mq  {
























}

/* Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.UsedArteCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),
			"Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOB57CSUJFN7A2O7PRNFX4YQE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIY6WD3JLMFB7JCAF6UGK5MKJQI"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.UsedArteCnt_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),
			"Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOB57CSUJFN7A2O7PRNFX4YQE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIY6WD3JLMFB7JCAF6UGK5MKJQI"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Mq.UsedArteCnt - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.UsedArteCnt - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обслуживание входящей очереди сообщений - Количество используемых ARTE");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound Message Queue Handler - Number of ARTE in Use");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIY6WD3JLMFB7JCAF6UGK5MKJQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.Mq.UsedArteCnt - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclNDBA6Z4NUNDRBBSUNDNIYDBEU4"),"MetricType.Unit.Mq.UsedArteCnt - Localizing Bundle",$$$items$$$);
}
