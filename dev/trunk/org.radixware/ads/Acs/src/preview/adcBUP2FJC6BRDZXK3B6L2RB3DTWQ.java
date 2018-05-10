
/* Radix::Acs::AcsCommonUtils - Common Executable*/

/*Radix::Acs::AcsCommonUtils-Common Dynamic Class*/

package org.radixware.ads.Acs.common;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsCommonUtils")
public published class AcsCommonUtils  {



	/*Radix::Acs::AcsCommonUtils:Nested classes-Nested Classes*/

	/*Radix::Acs::AcsCommonUtils:Properties-Properties*/

	/*Radix::Acs::AcsCommonUtils:ACCESS_FAMILY_ERR_STATUS-Dynamic Property*/



	protected static Str ACCESS_FAMILY_ERR_STATUS="<erroneous>";











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsCommonUtils:ACCESS_FAMILY_ERR_STATUS")
	public static published  Str getACCESS_FAMILY_ERR_STATUS() {
		return ACCESS_FAMILY_ERR_STATUS;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsCommonUtils:ACCESS_FAMILY_ERR_STATUS")
	public static published   void setACCESS_FAMILY_ERR_STATUS(Str val) {
		ACCESS_FAMILY_ERR_STATUS = val;
	}

	/*Radix::Acs::AcsCommonUtils:Methods-Methods*/


}

/* Radix::Acs::AcsCommonUtils - Server Meta*/

/*Radix::Acs::AcsCommonUtils-Common Dynamic Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsCommonUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcBUP2FJC6BRDZXK3B6L2RB3DTWQ"),"AcsCommonUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Acs::AcsCommonUtils:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::AcsCommonUtils:ACCESS_FAMILY_ERR_STATUS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXZ5WCIQRA5FXXHM5HCLUP5LZZ4"),"ACCESS_FAMILY_ERR_STATUS",null,org.radixware.kernel.common.enums.EValType.STR,null,null,null)
						},

						/*Radix::Acs::AcsCommonUtils:Methods-Methods*/
						null,
						null,
						null,null,null,false);
}

/* Radix::Acs::AcsCommonUtils - Client-Common Meta*/

/*Radix::Acs::AcsCommonUtils-Common Dynamic Class*/

package org.radixware.ads.Acs.common_client;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsCommonUtils_mi{
}

/* Radix::Acs::AcsCommonUtils - Desktop Meta*/

/*Radix::Acs::AcsCommonUtils-Common Dynamic Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsCommonUtils_mi{
}

/* Radix::Acs::AcsCommonUtils - Web Meta*/

/*Radix::Acs::AcsCommonUtils-Common Dynamic Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsCommonUtils_mi{
}

/* Radix::Acs::AcsCommonUtils - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsCommonUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ошибочно>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<erroneous>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAAGJVUS6ZGB3HPZ7T2CVTJJNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(AcsCommonUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcBUP2FJC6BRDZXK3B6L2RB3DTWQ"),"AcsCommonUtils - Localizing Bundle",$$$items$$$);
}
