
/* Radix::Acs::ApFamilyClientUtils - Client-Common Executable*/

/*Radix::Acs::ApFamilyClientUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.common_client;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ApFamilyClientUtils")
public published class ApFamilyClientUtils  {



	/*Radix::Acs::ApFamilyClientUtils:Nested classes-Nested Classes*/

	/*Radix::Acs::ApFamilyClientUtils:Properties-Properties*/

	/*Radix::Acs::ApFamilyClientUtils:Methods-Methods*/

	/*Radix::Acs::ApFamilyClientUtils:validateFamily-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::ApFamilyClientUtils:validateFamily")
	public static published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateFamily (Str roleTitle, org.radixware.kernel.common.enums.EAccessAreaMode mode, Str modeTitle, java.util.Set<org.radixware.kernel.common.types.Id> restrictedFamilies, org.radixware.kernel.common.types.Id familyId) {
		if (mode != AccessAreaMode:Unbounded && !restrictedFamilies.contains(familyId)) {
		    return Client.Validators::ValidationResult.Factory.newInvalidResult(
		            Str.format("Role '%s' can not have restriction on family '%s'",
		                    roleTitle,
		                    modeTitle));
		}
		return Client.Validators::ValidationResult.ACCEPTABLE;
	}


}

/* Radix::Acs::ApFamilyClientUtils - Client-Common Meta*/

/*Radix::Acs::ApFamilyClientUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.common_client;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ApFamilyClientUtils_mi{
}

/* Radix::Acs::ApFamilyClientUtils - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ApFamilyClientUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль \'%s\' не может иметь ограничений по семейству \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role \'%s\' can not have restriction on family \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7G23XRCKHZEODLSFY6T7CG3YBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(ApFamilyClientUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcKLRGFHLQC5F57FYNCMTIQ6DHIE"),"ApFamilyClientUtils - Localizing Bundle",$$$items$$$);
}
