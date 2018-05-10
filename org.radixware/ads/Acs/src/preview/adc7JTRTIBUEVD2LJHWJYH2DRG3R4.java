
/* Radix::Acs::AcsClientCommonUtils - Client-Common Executable*/

/*Radix::Acs::AcsClientCommonUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.common_client;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsClientCommonUtils")
public published class AcsClientCommonUtils  {



	/*Radix::Acs::AcsClientCommonUtils:Nested classes-Nested Classes*/

	/*Radix::Acs::AcsClientCommonUtils:Properties-Properties*/

	/*Radix::Acs::AcsClientCommonUtils:Methods-Methods*/

	/*Radix::Acs::AcsClientCommonUtils:processAcceptedCommand-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsClientCommonUtils:processAcceptedCommand")
	public static published  void processAcceptedCommand (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument acceptedOutput, org.radixware.kernel.common.client.IClientEnvironment environment, boolean isUserEntry) {
		String message;
		if (isUserEntry){
		    message = String.format("%s role(s) added,...", 
		        acceptedOutput.AcceptedRoles.AddedRolesCount,
		        acceptedOutput.AcceptedRoles.ReplacedRolesCount, 
		        acceptedOutput.AcceptedRoles.RemovedRolesCount,
		        acceptedOutput.AcceptedRoles.AddedUser2GroupCount, 
		        acceptedOutput.AcceptedRoles.RemovedUser2GroupCount);
		}
		else{
		    message = String.format("%s role(s) added,...", 
		        acceptedOutput.AcceptedRoles.AddedRolesCount,
		        acceptedOutput.AcceptedRoles.ReplacedRolesCount, 
		        acceptedOutput.AcceptedRoles.RemovedRolesCount,
		        acceptedOutput.AcceptedRoles.AddedUser2GroupCount, 
		        acceptedOutput.AcceptedRoles.RemovedUser2GroupCount);
		}

		{
		    final String sUnacceptedRoles = acceptedOutput.AcceptedRoles.UnacceptedRoles;
		    if (sUnacceptedRoles!=null && !sUnacceptedRoles.isEmpty()){
		        if (!message.endsWith("\n")){
		            message += "\n";
		        }
		        message += String.format("Unaccepted roles: %s.", sUnacceptedRoles);
		    }
		} 

		{
		    final String sUnacceptedUsersOrGroups = acceptedOutput.AcceptedRoles.UnacceptedUsersOrGroups;
		    if (sUnacceptedUsersOrGroups!=null && !sUnacceptedUsersOrGroups.isEmpty()){
		        if (!message.endsWith("\n")){
		            message += "\n";
		        }
		        if (isUserEntry){
		            message += String.format("Unaccepted groups: %s.", sUnacceptedUsersOrGroups);
		        }
		        else{
		            message += String.format("Unaccepted users: %s.", sUnacceptedUsersOrGroups);
		        }
		    }
		}
		environment.messageInformation("Information", message);


		 


	}

	/*Radix::Acs::AcsClientCommonUtils:validateFamily-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsClientCommonUtils:validateFamily")
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

/* Radix::Acs::AcsClientCommonUtils - Client-Common Meta*/

/*Radix::Acs::AcsClientCommonUtils-Client-Common Dynamic Class*/

package org.radixware.ads.Acs.common_client;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsClientCommonUtils_mi{
}

/* Radix::Acs::AcsClientCommonUtils - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsClientCommonUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неакцептованные группы: %s.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unaccepted groups: %s.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3O5QBC53NVD45ISX7WECV57KBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неакцептованные пользователи: %s.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unaccepted users: %s.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3VQIMQHJRNAPDK5OEJV37KKHI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неакцептованные роли: %s.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unaccepted roles: %s.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53KS2266QNHOJKL7YXIB5QAPTI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"%s ролей добавлено, \n%s заменено,\n%s удалено, \n%s групп добавлено, \n%s удалено.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"%s role(s) added,\n%s role(s) replaced,\n%s role(s) deleted,\n%s group(s) added, \n%s group(s) deleted.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCXCDLVSTONBORHEQQ7ND5Q6S6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"%s ролей добавлено, \n%s заменено,\n%s удалено, \n%s пользователей добавлено, \n%s удалено.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"%s role(s) added,\n%s role(s) replaced,\n%s role(s) deleted,\n%s user(s) added, \n%s user(s) deleted.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILSKQF4AJJDOHACWNKA2YDEULI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль \'%s\' не может иметь ограничений по семейству \'%s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role \'%s\' can not have restriction on family \'%s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMMJ5KBZ5ENFLFB6V6VRW73OH3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Информация");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Information");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTGOXQYABM5H4JFR7PBACSHZS5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"C:\\distrib\\trunk\\org.radixware\\ads\\Acs"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(AcsClientCommonUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadc7JTRTIBUEVD2LJHWJYH2DRG3R4"),"AcsClientCommonUtils - Localizing Bundle",$$$items$$$);
}
