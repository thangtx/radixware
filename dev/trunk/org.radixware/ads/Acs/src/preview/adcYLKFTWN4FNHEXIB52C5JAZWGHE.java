
/* Radix::Acs::AcsUtils - Server Executable*/

/*Radix::Acs::AcsUtils-Server Dynamic Class*/

package org.radixware.ads.Acs.server;

import org.radixware.kernel.server.arte.rights.AccessPartition;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils")
public final published class AcsUtils  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	final static public String ERR_CANT_CHECK_USER_RIGHTS_ = "Can\'t check user rights: ";
	final static public String ERR_CANT_PREPARE_DB_QRY = "Can\'t prepare Radix rights system service queries:\n";

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return AcsUtils_mi.rdxMeta;}

	/*Radix::Acs::AcsUtils:Nested classes-Nested Classes*/

	/*Radix::Acs::AcsUtils:Properties-Properties*/





























	/*Radix::Acs::AcsUtils:Methods-Methods*/

	/*Radix::Acs::AcsUtils:getCurUserAllRolesInAllAreas-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:getCurUserAllRolesInAllAreas")
	public static published  java.util.Set<org.radixware.kernel.common.types.Id> getCurUserAllRolesInAllAreas () {
		final String roles = CurUserAllRolesInAllAreasCursor.execute();

		if (roles==null || roles.isEmpty()) {
		    return java.util.Collections.emptySet();
		}

		final String[] idsStrArr = roles.split(",");
		final int len = idsStrArr.length;
		final java.util.Set<Types::Id> result = new java.util.HashSet<Types::Id>(len);
		for (int i=0; i<len; i++){
		    final String idStr = idsStrArr[i];
		    if (idStr!=null && !idStr.isEmpty()){
		        final Types::Id id = Types::Id.Factory.loadFrom(idStr);
		        result.add(id);
		    }
		}
		return result;

	}

	/*Radix::Acs::AcsUtils:isCurUserHasRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:isCurUserHasRole")
	public static published  boolean isCurUserHasRole (org.radixware.kernel.common.types.ArrStr roles) {
		if (roles.isEmpty())
		    return false;

		final java.util.Set<Types::Id> curUserRoleIds = getCurUserAllRolesInAllAreas();
		//if (curUserRoleIds.contains()){
		//    return true;
		//}
		for (String idStr : roles){
		    final Types::Id id = Types::Id.Factory.loadFrom(idStr);
		    if (curUserRoleIds.contains(id))
		        return true;
		}

		return false;

	}

	/*Radix::Acs::AcsUtils:isCurUserHasRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:isCurUserHasRole")
	public static published  boolean isCurUserHasRole (java.util.Collection<org.radixware.kernel.common.types.Id> roles) {
		if (roles.isEmpty())
		    return false;

		final java.util.Set<Types::Id> curUserRoleIds = getCurUserAllRolesInAllAreas();
		//if (curUserRoleIds.contains()){
		//    return true;
		//}

		for (Types::Id id : roles) {
		    if (curUserRoleIds.contains(id))
		        return true;
		}

		return false;

	}

	/*Radix::Acs::AcsUtils:isCurUserHasRoleWithRoleHierarchy-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:isCurUserHasRoleWithRoleHierarchy")
	public static published  boolean isCurUserHasRoleWithRoleHierarchy (java.util.Collection<org.radixware.kernel.common.types.Id> roles) {
		if (roles.isEmpty())
		    return false;

		for(org.radixware.kernel.server.meta.roles.RadRoleDef role : 
		Arte::Arte.getInstance().Rights.getCurUserAllRolesInAllAreasWithRolesHierarchy()) {
		    if(roles.contains(role.getId())) {
		        return true;
		    }
		}
		return false;
	}

	/*Radix::Acs::AcsUtils:isCurUserHasRoleWithRoleHierarchy-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:isCurUserHasRoleWithRoleHierarchy")
	public static published  boolean isCurUserHasRoleWithRoleHierarchy (org.radixware.kernel.common.types.ArrStr roles) {
		if (roles.isEmpty())
		    return false;

		for(org.radixware.kernel.server.meta.roles.RadRoleDef role : 
		Arte::Arte.getInstance().Rights.getCurUserAllRolesInAllAreasWithRolesHierarchy()) {
		    if(roles.contains(role.getId().toString())) {
		        return true;
		    }
		}

		return false;
	}

	/*Radix::Acs::AcsUtils:acceptedUserOrGroup-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:acceptedUserOrGroup")
	public static published  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument acceptedUserOrGroup (boolean isUser, Str name) {
		CommandsXsd:AcceptedRolesDocument rez = CommandsXsd:AcceptedRolesDocument.Factory.newInstance();

		Types::Reference<java.util.List<Integer>> ignoredRoles = new Types::Reference(); 
		Types::Reference<java.util.List<String>> ignoredUsersOrGroups = new Types::Reference();

		final Types::Reference<Integer> addedRoleCount = new Types::Reference<Integer>();
		final Types::Reference<Integer> replacedRoleCount = new Types::Reference<Integer>(); 
		final Types::Reference<Integer> removedRoleCount = new Types::Reference<Integer>(); 

		final Types::Reference<Integer> addedU2GCount = new Types::Reference<Integer>();
		final Types::Reference<Integer> removedU2GCount = new Types::Reference<Integer>();



		rez.AcceptedRoles = CommandsXsd:AcceptedRolesDocument.AcceptedRoles.Factory.newInstance();



		if (isUser){
		    Arte::Arte.getInstance().getRights().
		        getDualControlController().acceptRolesForUser(name, ignoredRoles, ignoredUsersOrGroups, 
		            addedRoleCount, replacedRoleCount, removedRoleCount, 
		            addedU2GCount, removedU2GCount);
		}
		else{
		    Arte::Arte.getInstance().getRights().
		        getDualControlController().acceptRolesForGroup(name, ignoredRoles, ignoredUsersOrGroups, 
		            addedRoleCount, replacedRoleCount, removedRoleCount, 
		            addedU2GCount, removedU2GCount);    
		}







		if (addedRoleCount.get()!=null)
		    rez.AcceptedRoles.AddedRolesCount = addedRoleCount.get().longValue();

		if (replacedRoleCount.get()!=null)
		    rez.AcceptedRoles.ReplacedRolesCount = replacedRoleCount.get().longValue();

		if (removedRoleCount.get()!=null)
		    rez.AcceptedRoles.RemovedRolesCount = removedRoleCount.get().longValue();


		if (addedU2GCount.get()!=null)
		    rez.AcceptedRoles.AddedUser2GroupCount = addedU2GCount.get().longValue();

		if (removedU2GCount.get()!=null)
		    rez.AcceptedRoles.RemovedUser2GroupCount = removedU2GCount.get().longValue();

		java.util.List<Integer> ignoredRolesAsList = ignoredRoles.get();
		if (!ignoredRolesAsList.isEmpty()){   
		    final java.lang.StringBuilder tmpBuilder = new java.lang.StringBuilder();
		    boolean isFirst = true;
		    
		    for (Integer ignoredRole : ignoredRolesAsList){          
		        if (isUser){
		            final User2Role newUser2Role = User2Role.loadByPK(ignoredRole.longValue(), true);
		            if (newUser2Role!=null){
		               if (isFirst){
		                   isFirst = false;
		               }
		               else{
		                   tmpBuilder.append(", ");
		               }
		               tmpBuilder.append(newUser2Role.calcTitle());
		            }
		        }
		        else{
		            final UserGroup2Role newUser2Role = UserGroup2Role.loadByPK(ignoredRole.longValue(), true);
		            if (newUser2Role!=null){
		               if (isFirst){
		                   isFirst = false;
		               }
		               else{
		                   tmpBuilder.append(", ");
		               }
		               tmpBuilder.append(newUser2Role.calcTitle());
		            }
		        }
		    }
		    rez.AcceptedRoles.UnacceptedRoles = tmpBuilder.toString();
		}


		java.util.List<String> ignoredUsersOrGroupsAsList = ignoredUsersOrGroups.get();
		if (!ignoredUsersOrGroupsAsList.isEmpty()){
		    final java.lang.StringBuilder tmpBuilder = new java.lang.StringBuilder();
		    boolean isFirst = true;
		    for (String ignoredUserOrGroup : ignoredUsersOrGroupsAsList){
		        if (isFirst){
		            isFirst = false;
		        }
		        else{
		            tmpBuilder.append(", ");
		        }
		        tmpBuilder.append(ignoredUserOrGroup);
		    }
		    rez.AcceptedRoles.UnacceptedUsersOrGroups = tmpBuilder.toString();
		}

		return rez;
	}

	/*Radix::Acs::AcsUtils:selectEditorPresentation-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:selectEditorPresentation")
	public static published  org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef selectEditorPresentation (org.radixware.ads.Types.server.Entity entity) {
		final java.util.List<Meta::EditorPresentationDef> allPresentations = entity.getPresentationMeta().getEditorPresentations();
		Types::PresentationEntityAdapter<Types::Entity> adapter = entity.getArte().getCache().getPresentationAdapter(entity);
		return adapter.selectEditorPresentation(allPresentations);
	}

	/*Radix::Acs::AcsUtils:getTotalRestrictions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:getTotalRestrictions")
	public static published  org.radixware.kernel.server.types.Restrictions getTotalRestrictions (org.radixware.ads.Types.server.Entity entity, org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef presentation) {
		if (entity==null){
		    throw new IllegalArgumentException("entity must be defined");
		}
		if (presentation==null){
		    throw new IllegalArgumentException("presentation must be defined");
		}
		final Meta::Restrictions presentationRestrictions = presentation.getTotalRestrictions(entity);
		final Types::PresentationEntityAdapter<Types::Entity> adapter = entity.getArte().getCache().getPresentationAdapter(entity);
		final Meta::Restrictions additionalRestrictions = adapter.getAdditionalRestrictions(presentation);
		if (additionalRestrictions==Meta::Restrictions.ZERO){
		    return presentationRestrictions;
		}else{
		    return Meta::Restrictions.Factory.sum(presentationRestrictions, additionalRestrictions);
		}
	}

	/*Radix::Acs::AcsUtils:getTotalRestrictionsForSelectedEditorPresentation-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:getTotalRestrictionsForSelectedEditorPresentation")
	public static published  org.radixware.kernel.server.types.Restrictions getTotalRestrictionsForSelectedEditorPresentation (org.radixware.ads.Types.server.Entity entity) {
		final Meta::EditorPresentationDef presentation = selectEditorPresentation(entity);
		return presentation==null ? Meta::Restrictions.FULL : getTotalRestrictions(entity, presentation);
	}

	/*Radix::Acs::AcsUtils:createAccessPartition-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::AcsUtils:createAccessPartition")
	public static published  org.radixware.kernel.server.arte.rights.AccessPartition createAccessPartition (org.radixware.kernel.server.arte.Arte arte, org.radixware.kernel.common.types.Id familyId, org.radixware.kernel.common.enums.EAccessAreaMode mode, Str singlePartitionVal, Int partitionGroupId) {
		if (AccessAreaMode:Unbounded == mode) {
		    return AccessPartition.Factory.createForUnbounded();
		}
		if (AccessAreaMode:BoundedByPart == mode) {
		    return AccessPartition.Factory.createForBoundedByPart(singlePartitionVal);
		}
		if (AccessAreaMode:Prohibited == mode) {
		    return AccessPartition.Factory.createForProhibited();
		}
		if (AccessAreaMode:BoundedByGroup == mode) {
		    if (partitionGroupId == null) {
		        return AccessPartition.Factory.createForProhibited();
		    }
		    return AccessPartition.Factory.createForBoundedByGroup(arte, partitionGroupId.intValue());
		}
		if (AccessAreaMode:BoundedByUser == mode) {
		    return AccessPartition.Factory.createForBoundedByUser(arte, familyId);
		}

		throw new org.radixware.kernel.common.exceptions.RadixError("Invalid AccessAreaMode " + String.valueOf(mode == null ? null : mode.Value)); 


	}


}

/* Radix::Acs::AcsUtils - Server Meta*/

/*Radix::Acs::AcsUtils-Server Dynamic Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsUtils_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("adcYLKFTWN4FNHEXIB52C5JAZWGHE"),"AcsUtils",null,

						org.radixware.kernel.common.enums.EClassType.DYNAMIC,
						null,
						null,
						null,

						/*Radix::Acs::AcsUtils:Properties-Properties*/
						null,

						/*Radix::Acs::AcsUtils:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKE5JROUIBZEXNMZKRETLSN7MA4"),"getCurUserAllRolesInAllAreas",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIAQ2AR4L75E6TIC4M6XDLGOQ6A"),"isCurUserHasRole",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roles",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMONWZ4ILSBGFTP5T2P45OPCT2A"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthY5QK4BXTRVGJZMOAZD4PDWAVJM"),"isCurUserHasRole",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roles",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBZS7CSUNFFCDRKFSP2TLPNW4I4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSBGGKVQ6SFCCNHAKOBD7AB7YXY"),"isCurUserHasRoleWithRoleHierarchy",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roles",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFOCYJ5LDQJGP3CEGOMYPENIMQY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYIQCIIZ7BFGBRKGSUEPDYOO6MI"),"isCurUserHasRoleWithRoleHierarchy",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roles",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLNW7QXQUVRF2XKUFOTV74SWX5I"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPDEDBJJ5I5BIXBML737COQ3YDU"),"acceptedUserOrGroup",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("isUser",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCWBQU3UFN5H6FBSEP7MKYF3G3M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZFVXCF6JPVE2VKBMQP26BMTBGM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5TJ3WKHBFJCRFLT4PZX3OKYLQE"),"selectEditorPresentation",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprH7AFRRL4YFBAROR7NQ2K667I3A"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGUZRQL6RBVEW3FTYHNVBRWUCNY"),"getTotalRestrictions",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXOYRN4AEZJAU7E4VSHYIMMELGQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("presentation",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPZM6CGGJ6VDADPCYGVADSKAPK4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZL7223FHYNBMRFDPANX6OBUIYE"),"getTotalRestrictionsForSelectedEditorPresentation",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("entity",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEY5IKYES5FDC3MTPOJT2AUKV4Q"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB22SDZROKVFRJPPQW7OMQ4QDU4"),"createAccessPartition",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("arte",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT3J3EMRAENCHLCYP736LJ6B7IE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("familyId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFSAIMIUT7NGELIYQRW7BBPVW44")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mode",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIK4KB6Q7KFFBNAVAWAWA4QUGBQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("singlePartitionVal",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDIQUJHTSHVEBDOMNFYYSQUJ76I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("partitionGroupId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTYS4EOK3FNDL7OEJD5C5P5G7AM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::Acs::AcsUtils - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class AcsUtils - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns true if the current user has the roles specified in the parameter.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает значение \"истина\", если текущий пользователь имеет указанные в параметре роли.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43MJ3GCUWVANFAT7BHZJHBMHKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns IDs of all the roles in all the access areas of the current user.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает идентификаторы всех ролей во всех областях доступа для текущего пользователя.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD2E2OZHCGVCRFC7YKWDAB7A7HM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Returns true if the current user has the roles specified in the parameter.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Возвращает значение \"истина\", если текущий пользователь имеет указанные в параметре роли.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDL7ISASAKNDMZGXQYOPHCR5JZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Roles being checked.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверяемые роли.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGMURPUWPFVE53HALX5D7QDQETE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Access Control System (ACS) utilities. Allow to get information on roles.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Различные утилиты для работы с системой контроля доступа (ACS). Позволяют получать информацию по ролям.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOYCDCYF7RDO7OVB22JVW25XXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Roles being checked.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверяемые роли.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNBSRLBAV45FEXKAJY33LDDJFAA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(AcsUtils - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbadcYLKFTWN4FNHEXIB52C5JAZWGHE"),"AcsUtils - Localizing Bundle",$$$items$$$);
}
