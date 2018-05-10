
/* Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec - Server Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec")
public class MetricType.Unit.Mq.MessagesPerSec  extends org.radixware.ads.SystemMonitor.server.MetricType.Unit.Mq  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.Unit.Mq.MessagesPerSec_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:Unit.Mq.MessagesPerSec;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec - Server Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.MessagesPerSec_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),"MetricType.Unit.Mq.MessagesPerSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ6AGXBH7F5ENNHNL4C67X7TECI"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),
							/*Owner Class Name*/
							"MetricType.Unit.Mq.MessagesPerSec",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ6AGXBH7F5ENNHNL4C67X7TECI"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDKISRODKXRERDGL5SXFJVUM5TQ"),120.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOB57CSUJFN7A2O7PRNFX4YQE"),

						/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec")
public interface MetricType.Unit.Mq.MessagesPerSec   extends org.radixware.ads.SystemMonitor.explorer.MetricType.Unit.Mq  {
























}

/* Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.MessagesPerSec_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),
			"Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOB57CSUJFN7A2O7PRNFX4YQE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ6AGXBH7F5ENNHNL4C67X7TECI"),null,null,0,

			/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec - Web Meta*/

/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.MessagesPerSec_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),
			"Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclUSOB57CSUJFN7A2O7PRNFX4YQE"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ6AGXBH7F5ENNHNL4C67X7TECI"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.Unit.Mq.MessagesPerSec - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.Unit.Mq.MessagesPerSec - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обслуживание входящей очереди сообщений - Количество обработанных сообщений в секунду");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound Message Queue Handler - Number of Messages Processed per Second");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ6AGXBH7F5ENNHNL4C67X7TECI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.Unit.Mq.MessagesPerSec - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclTO3Q6NBFDJGMJJFO5QM4JFFOHE"),"MetricType.Unit.Mq.MessagesPerSec - Localizing Bundle",$$$items$$$);
}
