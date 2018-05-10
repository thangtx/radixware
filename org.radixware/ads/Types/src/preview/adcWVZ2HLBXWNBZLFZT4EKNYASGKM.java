
/* Radix::Types::MessageBundle - Server Executable*/

/*Radix::Types::MessageBundle-Server Dynamic Class*/

package org.radixware.ads.Types.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::MessageBundle")
public final published class MessageBundle  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MessageBundle_mi.rdxMeta;}

	/*Radix::Types::MessageBundle:Nested classes-Nested Classes*/

	/*Radix::Types::MessageBundle:Properties-Properties*/





























	/*Radix::Types::MessageBundle:Methods-Methods*/

	/*Radix::Types::MessageBundle:messages-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Types::MessageBundle:messages")
	public final published  void messages () {
		//PresentationEntityAdapter
		//
		//
		//
	}


}

/* Radix::Types::MessageBundle - Server Meta*/

/*Radix::Types::MessageBundle-Server Dynamic Class*/

package org.radixware.ads.Types.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageBundle_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcWVZ2HLBXWNBZLFZT4EKNYASGKM"),"MessageBundle",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Types::MessageBundle:Properties-Properties*/
						null,

						/*Radix::Types::MessageBundle:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSMSM63HHX5F2TFOSS7RZOV7BIE"),"messages",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null)
						},
						null,
						null,null,null,false);
}

/* Radix::Types::MessageBundle - Localizing Bundle */
package org.radixware.ads.Types.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MessageBundle - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Can\'\'t delete the object because it is used by {0} object.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удаление объекта запрещено, так как этот объект используется объектом {0}.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MQJUCQPB5C2LJYOOCKLMSKW4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"Arte.Entity",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Do you really want to clear references to this object from child objects: {0}?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обнулить ссылки на этот объект в дочерних подобъектах: {0}?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls57IZ3IRS25C7XF4KYB46AGNUSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"Arte.Entity",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Do you really want to delete the subobjects: {0}?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить подобъекты: {0}?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCWATYS6G4NHQDMP5ATUSND7G6E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"Arte.Entity",null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MessageBundle - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcWVZ2HLBXWNBZLFZT4EKNYASGKM"),"MessageBundle - Localizing Bundle",$$$items$$$);
}
