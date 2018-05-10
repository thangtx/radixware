
/* Radix::SystemMonitor::MetricType.UserQuery.Point - Server Executable*/

/*Radix::SystemMonitor::MetricType.UserQuery.Point-Application Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserQuery.Point")
public class MetricType.UserQuery.Point  extends org.radixware.ads.SystemMonitor.server.MetricType.UserQuery  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType.UserQuery.Point_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType.UserQuery.Point:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType.UserQuery.Point:Properties-Properties*/





























	/*Radix::SystemMonitor::MetricType.UserQuery.Point:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType.UserQuery.Point:getKind-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserQuery.Point:getKind")
	public published  org.radixware.ads.SystemMonitor.common.MetricKind getKind () {
		return MetricKind:User.Query.Point;
	}

	/*Radix::SystemMonitor::MetricType.UserQuery.Point:getValueType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserQuery.Point:getValueType")
	protected published  org.radixware.ads.SystemMonitor.common.MetricValueType getValueType () {
		return MetricValueType:Point;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType.UserQuery.Point - Server Meta*/

/*Radix::SystemMonitor::MetricType.UserQuery.Point-Application Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserQuery.Point_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),"MetricType.UserQuery.Point",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXHSCSGGS3JB2LPNSJDQSEVAIHU"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::SystemMonitor::MetricType.UserQuery.Point:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),
							/*Owner Class Name*/
							"MetricType.UserQuery.Point",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXHSCSGGS3JB2LPNSJDQSEVAIHU"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType.UserQuery.Point:Properties-Properties*/
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
									/*Radix::SystemMonitor::MetricType.UserQuery.Point:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ClassRef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDQ3TJ6OI6FGBZAA7VFXHRLRHIY"),100.0,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),null)}
									)
							},
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAHCHZGQJUZA67LQUVMOZLDWRSU"),

						/*Radix::SystemMonitor::MetricType.UserQuery.Point:Properties-Properties*/
						null,

						/*Radix::SystemMonitor::MetricType.UserQuery.Point:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIT2CW4S27RB3XNEN22I7VDSY74"),"getValueType",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR)
						},
						null,
						null,null,null,false);
}

/* Radix::SystemMonitor::MetricType.UserQuery.Point - Desktop Executable*/

/*Radix::SystemMonitor::MetricType.UserQuery.Point-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType.UserQuery.Point")
public interface MetricType.UserQuery.Point   extends org.radixware.ads.SystemMonitor.explorer.MetricType.UserQuery  {
























}

/* Radix::SystemMonitor::MetricType.UserQuery.Point - Desktop Meta*/

/*Radix::SystemMonitor::MetricType.UserQuery.Point-Application Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserQuery.Point_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.UserQuery.Point:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),
			"Radix::SystemMonitor::MetricType.UserQuery.Point",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAHCHZGQJUZA67LQUVMOZLDWRSU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXHSCSGGS3JB2LPNSJDQSEVAIHU"),null,null,0,

			/*Radix::SystemMonitor::MetricType.UserQuery.Point:Properties-Properties*/
			null,
			null,
			null,
			null,
			null,
			null,
			true,false,false);
}

/* Radix::SystemMonitor::MetricType.UserQuery.Point - Web Meta*/

/*Radix::SystemMonitor::MetricType.UserQuery.Point-Application Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserQuery.Point_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType.UserQuery.Point:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),
			"Radix::SystemMonitor::MetricType.UserQuery.Point",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aclAHCHZGQJUZA67LQUVMOZLDWRSU"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXHSCSGGS3JB2LPNSJDQSEVAIHU"),null,null,0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType.UserQuery.Point - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType.UserQuery.Point - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательская метрика - точечный SQL запрос");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Metric - Point SQL Query");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXHSCSGGS3JB2LPNSJDQSEVAIHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"/home/ayanichkin/ssd/tranzaxis/radix/org.radixware/ads/SystemMonitor"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType.UserQuery.Point - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclQDZGCRNJLJCMTPNAHQR2NYZY6Q"),"MetricType.UserQuery.Point - Localizing Bundle",$$$items$$$);
}
