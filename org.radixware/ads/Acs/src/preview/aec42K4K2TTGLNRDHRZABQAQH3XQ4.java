
/* Radix::Acs::User2Role - Server Executable*/

/*Radix::Acs::User2Role-Entity Class*/

package org.radixware.ads.Acs.server;

import java.util.HashMap;
import org.radixware.kernel.server.meta.roles.RadRoleDef;
import org.radixware.kernel.server.arte.rights.AccessPartition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role")
public final published class User2Role  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private String sLastUserName = null;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return User2Role_mi.rdxMeta;}

	/*Radix::Acs::User2Role:Nested classes-Nested Classes*/

	/*Radix::Acs::User2Role:Properties-Properties*/

	/*Radix::Acs::User2Role:acceptorName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:acceptorName")
	public published  Str getAcceptorName() {
		return acceptorName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:acceptorName")
	public published   void setAcceptorName(Str val) {
		acceptorName = val;
	}

	/*Radix::Acs::User2Role:apUserGroupKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupKey")
	public published  Str getApUserGroupKey() {
		return apUserGroupKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupKey")
	public published   void setApUserGroupKey(Str val) {
		apUserGroupKey = val;
	}

	/*Radix::Acs::User2Role:apUserGroupLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupLink")
	public published  org.radixware.ads.Acs.server.UserGroup getApUserGroupLink() {
		return apUserGroupLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupLink")
	public published   void setApUserGroupLink(org.radixware.ads.Acs.server.UserGroup val) {
		apUserGroupLink = val;
	}

	/*Radix::Acs::User2Role:apUserGroupMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupMode")
	public published  org.radixware.kernel.common.enums.EAccessAreaMode getApUserGroupMode() {
		return apUserGroupMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupMode")
	public published   void setApUserGroupMode(org.radixware.kernel.common.enums.EAccessAreaMode val) {
		apUserGroupMode = val;
	}

	/*Radix::Acs::User2Role:areaTitle-Dynamic Property*/



	protected Str areaTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:areaTitle")
	public published  Str getAreaTitle() {

		return getAreaTitle(roleId, apUserGroupMode, apUserGroupLink, apUserGroupPartGroupLink,
		                           apDashConfigMode, apDashConfigLink, apDashConfigPartGroupLink);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:areaTitle")
	public published   void setAreaTitle(Str val) {
		areaTitle = val;
	}

	/*Radix::Acs::User2Role:editorName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:editorName")
	public published  Str getEditorName() {
		return editorName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:editorName")
	public published   void setEditorName(Str val) {
		editorName = val;
	}

	/*Radix::Acs::User2Role:hasReplacements-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:hasReplacements")
	public published  Int getHasReplacements() {
		return hasReplacements;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:hasReplacements")
	public published   void setHasReplacements(Int val) {
		hasReplacements = val;
	}

	/*Radix::Acs::User2Role:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Acs::User2Role:isNew-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isNew")
	public published  Bool getIsNew() {
		return isNew;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isNew")
	public published   void setIsNew(Bool val) {
		isNew = val;
	}

	/*Radix::Acs::User2Role:isOwn-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isOwn")
	public published  Bool getIsOwn() {
		return isOwn;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isOwn")
	public published   void setIsOwn(Bool val) {
		isOwn = val;
	}

	/*Radix::Acs::User2Role:replacedId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replacedId")
	public published  Int getReplacedId() {
		return replacedId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replacedId")
	public published   void setReplacedId(Int val) {
		replacedId = val;
	}

	/*Radix::Acs::User2Role:roleId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleId")
	public published  Str getRoleId() {
		return roleId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleId")
	public published   void setRoleId(Str val) {
		roleId = val;
	}

	/*Radix::Acs::User2Role:roleTitle-Dynamic Property*/



	protected Str roleTitle=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("null",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleTitle")
	public published  Str getRoleTitle() {

		if (roleId == null){
		   if (this.isNewObject()){
		       return null;
		   }
		   return "<Revoked>";
		}
		return Meta::Utils.getRoleTitle(Types::Id.Factory.loadFrom(roleId));
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleTitle")
	public published   void setRoleTitle(Str val) {
		roleTitle = val;
	}

	/*Radix::Acs::User2Role:userName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userName")
	public published  Str getUserName() {
		return userName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userName")
	public published   void setUserName(Str val) {
		userName = val;
	}

	/*Radix::Acs::User2Role:userTitle-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userTitle")
	public published  org.radixware.ads.Acs.server.User getUserTitle() {
		return userTitle;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userTitle")
	public published   void setUserTitle(org.radixware.ads.Acs.server.User val) {
		userTitle = val;
	}

	/*Radix::Acs::User2Role:isDualControlUsed-Dynamic Property*/



	protected Bool isDualControlUsed=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isDualControlUsed")
	public published  Bool getIsDualControlUsed() {

		return getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isDualControlUsed")
	public published   void setIsDualControlUsed(Bool val) {
		isDualControlUsed = val;
	}

	/*Radix::Acs::User2Role:mayReplaceOrRevokeRole-Dynamic Property*/



	protected Bool mayReplaceOrRevokeRole=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:mayReplaceOrRevokeRole")
	public published  Bool getMayReplaceOrRevokeRole() {

		return getArte().getRights().getDualControlController().mayReplaceOrRevokeUser2Role(this.id.intValue());
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:mayReplaceOrRevokeRole")
	public published   void setMayReplaceOrRevokeRole(Bool val) {
		mayReplaceOrRevokeRole = val;
	}

	/*Radix::Acs::User2Role:isRevoked-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isRevoked")
	public  Int getIsRevoked() {
		return isRevoked;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isRevoked")
	public   void setIsRevoked(Int val) {
		isRevoked = val;
	}

	/*Radix::Acs::User2Role:isReplaced-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isReplaced")
	public  Int getIsReplaced() {
		return isReplaced;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isReplaced")
	public   void setIsReplaced(Int val) {
		isReplaced = val;
	}

	/*Radix::Acs::User2Role:apDashConfigKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigKey")
	public published  Str getApDashConfigKey() {
		return apDashConfigKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigKey")
	public published   void setApDashConfigKey(Str val) {
		apDashConfigKey = val;
	}

	/*Radix::Acs::User2Role:apDashConfigMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigMode")
	public published  org.radixware.kernel.common.enums.EAccessAreaMode getApDashConfigMode() {
		return apDashConfigMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigMode")
	public published   void setApDashConfigMode(org.radixware.kernel.common.enums.EAccessAreaMode val) {
		apDashConfigMode = val;
	}

	/*Radix::Acs::User2Role:apDashConfigLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigLink")
	public published  org.radixware.ads.SystemMonitor.server.DashConfig getApDashConfigLink() {
		return apDashConfigLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigLink")
	public published   void setApDashConfigLink(org.radixware.ads.SystemMonitor.server.DashConfig val) {
		apDashConfigLink = val;
	}

	/*Radix::Acs::User2Role:roleDescription-Dynamic Property*/



	protected Str roleDescription=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleDescription")
	public published  Str getRoleDescription() {

		if (roleId == null) {
		    if (this.isNewObject()) {
		        return null;
		    }
		    return "<Revoked>";
		}
		for (Meta::RoleDef r : getArte().getDefManager().getAccessibleRoleDefs()) {
		    if (!r.isAbstract()) {
		        if(r.getId().toString() == roleId) {
		            return r.getDescription();
		        }
		    }
		}
		return null;

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleDescription")
	public published   void setRoleDescription(Str val) {
		roleDescription = val;
	}

	/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS-Dynamic Property*/



	protected Str ACCESS_FAMILY_ERR_STATUS=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS")
	protected final published  Str getACCESS_FAMILY_ERR_STATUS() {

		return getAccessFamilyErrStatus();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS")
	protected final published   void setACCESS_FAMILY_ERR_STATUS(Str val) {
		ACCESS_FAMILY_ERR_STATUS = val;
	}

	/*Radix::Acs::User2Role:apDashConfigPartGroupLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupLink")
	public published  org.radixware.ads.Acs.server.PartitionGroup getApDashConfigPartGroupLink() {
		return apDashConfigPartGroupLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupLink")
	public published   void setApDashConfigPartGroupLink(org.radixware.ads.Acs.server.PartitionGroup val) {
		apDashConfigPartGroupLink = val;
	}

	/*Radix::Acs::User2Role:apUserGroupPartGroupLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupLink")
	public published  org.radixware.ads.Acs.server.PartitionGroup getApUserGroupPartGroupLink() {
		return apUserGroupPartGroupLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupLink")
	public published   void setApUserGroupPartGroupLink(org.radixware.ads.Acs.server.PartitionGroup val) {
		apUserGroupPartGroupLink = val;
	}

	/*Radix::Acs::User2Role:apDashConfigPartGroupId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupId")
	public published  Int getApDashConfigPartGroupId() {
		return apDashConfigPartGroupId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupId")
	public published   void setApDashConfigPartGroupId(Int val) {
		apDashConfigPartGroupId = val;
	}

	/*Radix::Acs::User2Role:apUserGroupPartGroupId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupId")
	public published  Int getApUserGroupPartGroupId() {
		return apUserGroupPartGroupId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupId")
	public published   void setApUserGroupPartGroupId(Int val) {
		apUserGroupPartGroupId = val;
	}





































































































































































































	/*Radix::Acs::User2Role:Methods-Methods*/

	/*Radix::Acs::User2Role:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		final HashMap<Types::Id, AccessPartition> accessArea = getArea(getArte());
		final String sRoleId = roleId;

		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){    
		    if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){
		        throw new InvalidEasRequestClientFault("Attempt to assign exceeding rights");
		    }
		}

		final java.util.List<Types::Id> roleFamilies = new java.util.ArrayList<>();
		fillRoleFamiliesList(roleId, roleFamilies);
		checkRoleFamilies(roleFamilies);

		return true;
	}

	/*Radix::Acs::User2Role:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:beforeDelete")
	protected published  boolean beforeDelete () {
		final HashMap<Types::Id, AccessPartition> accessArea = getArea(getArte());
		final String sRoleId = roleId;

		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){
		        throw new InvalidEasRequestClientFault("Attempt to revoke exceeding rights");
		    }
		}
		else{
		if (!getArte().getRights().getDualControlController().isNewUser2Role(id.intValue())){
		        throw new InvalidEasRequestClientFault("Attempt to violate dual control rights");
		    }
		}
		sLastUserName = userName;
		return true;



	}

	/*Radix::Acs::User2Role:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:beforeUpdate")
	protected published  boolean beforeUpdate () {
		final HashMap<Types::Id, AccessPartition> accessArea = getArea(getArte());
		final String sRoleId = roleId;


		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    if (!getArte().getRights().curUserHasRightsUser2Role(id.intValue())){
		        throw new InvalidEasRequestClientFault( "Attempt to modify exceeding rights");
		    }

		    if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){
		        throw new InvalidEasRequestClientFault("Attempt to set exceeding rights");
		    }
		}

		final java.util.List<Types::Id> roleFamilies = new java.util.ArrayList<>();
		fillRoleFamiliesList(roleId, roleFamilies);
		checkRoleFamilies(roleFamilies);

		  
		return true;
	}

	/*Radix::Acs::User2Role:onCommand_replaceOrRevokeRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:onCommand_replaceOrRevokeRole")
	public  org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument onCommand_replaceOrRevokeRole (org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return replaceOrRevokeRole(input);
	}

	/*Radix::Acs::User2Role:onCommand_revokeRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:onCommand_revokeRole")
	public  org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument onCommand_revokeRole (org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return replaceOrRevokeRole(input);
	}

	/*Radix::Acs::User2Role:replaceOrRevokeRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replaceOrRevokeRole")
	public  org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument replaceOrRevokeRole (org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input) {

		if (!getArte().getRights().getDualControlController().mayReplaceOrRevokeUser2Role(input.ReplaceOrRevokeRole.Id.intValue())){
		     throw new RuntimeException("Unable execute command.");
		}



		CommandsXsd:IntValueDocument rez = CommandsXsd:IntValueDocument.Factory.newInstance();
		 User2Role srcUser2Role = User2Role.loadByPK(input.ReplaceOrRevokeRole.Id, true);
		 User2Role newUser2Role = new User2Role();
		 newUser2Role.init(null, srcUser2Role);

		 int newId = getArte().getRights().getDualControlController().NextUser2RoleId; 
		 newUser2Role.id = newId;
		 newUser2Role.isNew = true;
		 newUser2Role.create(srcUser2Role);
		 newUser2Role.replacedId = srcUser2Role.id;
		 newUser2Role.editorName = getArte().UserName;
		 newUser2Role.acceptorName = null;
		 
		 if (!input.ReplaceOrRevokeRole.Replace.booleanValue()){
		       newUser2Role.roleId = null;
		 }

		 rez.IntValue1 = newId;
		 
		 return rez;
	}

	/*Radix::Acs::User2Role:cmdGetAPFamilyList-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:cmdGetAPFamilyList")
	public published  org.radixware.schemas.adsdef.APFamiliesDocument cmdGetAPFamilyList (org.radixware.schemas.adsdef.RoleIDDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		org.radixware.schemas.adsdef.APFamiliesDocument apFamiliesDocument = org.radixware.schemas.adsdef.APFamiliesDocument.Factory.newInstance();

		apFamiliesDocument.APFamilies = org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies.Factory.newInstance();

		String sRoleId = (input == null) ? roleId : input.RoleID.Id.toString();
		String desc = "";

		for (Meta::RoleDef r : getArte().getDefManager().getAccessibleRoleDefs()) {
		    if (!r.isAbstract()) {
		        if (r.getId().toString() == sRoleId) {
		            desc = r.getDescription();
		        }
		    }
		}

		User2Role.fillRoleFamiliesList(sRoleId, apFamiliesDocument.APFamilies.IdList);
		apFamiliesDocument.APFamilies.Description = desc;
		return apFamiliesDocument;
	}

	/*Radix::Acs::User2Role:fillRoleFamiliesList-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:fillRoleFamiliesList")
	public static published  void fillRoleFamiliesList (Str sRoleId, java.util.List<org.radixware.kernel.common.types.Id> list) {
		if (sRoleId==null)
		    return;
		if (sRoleId.equals(idof[Arte::SuperAdmin].toString())) {
		    for (DdsAccessPartitionFamilyDef apf : Arte::Arte.getDefManager().getAccessPartitionFamilyDefs())
		        list.add(apf.getHeadId());
		} else {
		    final RadRoleDef role = Arte::Arte.getDefManager().getRoleDef(Types::Id.Factory.loadFrom(sRoleId));
		    for (Id id : role.getAPFamilies())
		        list.add(Arte::Arte.getDefManager().getAccessPartitionFamilyDef(id).getHeadId());
		}

	}

	/*Radix::Acs::User2Role:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:loadByPK")
	public static published  org.radixware.ads.Acs.server.User2Role loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),pkValsMap);
		try{
		return (
		org.radixware.ads.Acs.server.User2Role) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::User2Role:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:loadByPidStr")
	public static published  org.radixware.ads.Acs.server.User2Role loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),pidAsStr);
		try{
		return (
		org.radixware.ads.Acs.server.User2Role) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::User2Role:getArea-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:getArea")
	protected published  java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.server.arte.rights.AccessPartition> getArea (org.radixware.kernel.server.arte.Arte arte) {
		final HashMap<Types::Id, AccessPartition> accessArea = new HashMap<Types::Id, AccessPartition>();

		accessArea.put(idof[SystemMonitor::DashConfig], 
		              AcsUtils.createAccessPartition(arte, idof[SystemMonitor::DashConfig], apDashConfigMode, apDashConfigKey, apDashConfigPartGroupId));
		              
		accessArea.put(idof[Acs::UserGroup], 
		              AcsUtils.createAccessPartition(arte, idof[Acs::UserGroup], apUserGroupMode, apUserGroupKey, apUserGroupPartGroupId));              

		return accessArea;
	}

	/*Radix::Acs::User2Role:getAuditPropValTitle-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:getAuditPropValTitle")
	public static  org.radixware.ads.Utils.common.TitledValue getAuditPropValTitle (org.radixware.kernel.common.types.Id propId, java.util.Map<org.radixware.kernel.common.types.Id,org.radixware.ads.Utils.common.TitledValue> propVals) {
		if (idof[User2Role:roleId] == propId){
		    Utils::TitledValue ret = new TitledValue();
		    ret.title = "Role";
		    ret.value = Meta::Utils.getRoleTitle(propVals.get(propId).value);
		    return ret;
		} else {
		    return  propVals.get(propId);
		}

	}

	/*Radix::Acs::User2Role:checkRoleFamilies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:checkRoleFamilies")
	protected published  void checkRoleFamilies (java.util.List<org.radixware.kernel.common.types.Id> roleFamilies) {
		if (!roleFamilies.contains(idof[Acs::UserGroup])) {
		    apUserGroupMode = AccessAreaMode:Unbounded;
		    apUserGroupLink = null;
		}
		if (!roleFamilies.contains(idof[SystemMonitor::DashConfig])) {
		    apDashConfigMode = AccessAreaMode:Unbounded;
		    apDashConfigLink = null;
		}
	}

	/*Radix::Acs::User2Role:getAreaTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:getAreaTitle")
	public static published  Str getAreaTitle (Str roleId, org.radixware.kernel.common.enums.EAccessAreaMode apUserGroupMode, org.radixware.ads.Acs.server.UserGroup apUserGroupLink, org.radixware.ads.Acs.server.PartitionGroup apUserGroupPartitions, org.radixware.kernel.common.enums.EAccessAreaMode apDashConfigMode, org.radixware.ads.SystemMonitor.server.DashConfig apDashConfigLink, org.radixware.ads.Acs.server.PartitionGroup apDashConfigPartitions) {
		java.util.List<Types::Id> list = new java.util.ArrayList<Types::Id>();
		fillRoleFamiliesList(roleId, list);
		Str res="";

		//String ss(){return s;}

		try {
		    String groupName = "Administration Group: ";
		    if (list.contains(idof[Acs::UserGroup])) {
		        if (res!="") {
		            res+="; ";
		        }
		        
		        if (apUserGroupMode==AccessAreaMode:BoundedByPart) {
		            res+=groupName;
		            res+=apUserGroupLink==null ? "<null>" : apUserGroupLink.name;
		        }
		        else if (apUserGroupMode==AccessAreaMode:Unbounded) {
		            res+=groupName;
		            res+= "<any>";
		        }
		        else if (apUserGroupMode==AccessAreaMode:BoundedByGroup) {
		            res+= "Group of Administration Groups: ";
		            res+= apUserGroupPartitions==null ? "<none>" : apUserGroupPartitions.title;
		        }
		        else {
		            res+=groupName;
		            res+= "<none>";
		        }
		        
		    } else if (apUserGroupMode!=AccessAreaMode:Unbounded) {
		        return groupName + getAccessFamilyErrStatus();
		    }
		    
		    groupName = "Dashboard: ";
		    if (list.contains(idof[SystemMonitor::DashConfig])) {
		        if (res!="")
		            res+="; ";
		                
		        if (apDashConfigMode==AccessAreaMode:BoundedByPart) {
		            res+=groupName;
		            res+=apDashConfigLink==null ? "<null>":apDashConfigLink.title;
		        }
		        else if (apDashConfigMode==AccessAreaMode:Unbounded) {
		            res+=groupName;
		            res+= "<any>";
		        }
		        else if (apDashConfigMode==AccessAreaMode:BoundedByGroup) {
		            res+= "Dashboard Group: ";
		            
		            res+= apDashConfigPartitions==null ? "<null>" : apDashConfigPartitions.title;
		        }
		        else {
		            res+=groupName;
		            res+= "<none>";
		        }
		    } else if (apDashConfigMode != AccessAreaMode:Unbounded) {
		        return groupName + getAccessFamilyErrStatus();
		    }
		    if (res==null || res.isEmpty())
		        res+= "<not defined>";
		    
		} catch (Throwable e) {
		    res="?";
		}
		return res;
	}

	/*Radix::Acs::User2Role:getAccessFamilyErrStatus-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:getAccessFamilyErrStatus")
	protected static published  Str getAccessFamilyErrStatus () {
		return "<erroneous>";
	}

	/*Radix::Acs::User2Role:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);
		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    getArte().getRights().compileUserRights(this.userName, true);
		}
	}

	/*Radix::Acs::User2Role:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:afterDelete")
	protected published  void afterDelete () {
		super.afterDelete();
		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    getArte().getRights().compileUserRights(sLastUserName, true);
		}
	}

	/*Radix::Acs::User2Role:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:afterUpdate")
	protected published  void afterUpdate () {
		super.afterUpdate();
		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    getArte().getRights().compileUserRights(this.userName, true);
		}
	}





	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd7B4K7HL2JNB67MD3LJGE5SBLGU){
			org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument result = onCommand_replaceOrRevokeRole((org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdLDP4MD5I5BHKJLUFBGOUUME52M){
			org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument result = onCommand_revokeRole((org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdWVP7CH4TCRFWBHFTFJKUFGKPQA){
			org.radixware.schemas.adsdef.APFamiliesDocument result = cmdGetAPFamilyList((org.radixware.schemas.adsdef.RoleIDDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.adsdef.RoleIDDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Acs::User2Role - Server Meta*/

/*Radix::Acs::User2Role-Entity Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User2Role_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),"User2Role",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Acs::User2Role:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
							/*Owner Class Name*/
							"User2Role",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),
							/*Property presentations*/

							/*Radix::Acs::User2Role:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::User2Role:acceptorName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apUserGroupKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD7VLPHOA3VC4XL3QOLTWZLUHJI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apUserGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ4BWGI77MJD2HFPRQSVQXFBDI4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apUserGroupMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4T2IAH72O5E6RNZPX3MNT44CQE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:areaTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:editorName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:hasReplacements:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:isNew:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:isOwn:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC3DKGLHPDNRDHTWABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:replacedId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:roleId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:roleTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:userName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:userTitle:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:isDualControlUsed:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:isRevoked:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQNV5G4S5A6FKT3BLWVWFGCPQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:isReplaced:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2GERGO6Z5FJJBC6T6JHC6VNSQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apDashConfigKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apDashConfigMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apDashConfigLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOAVV6AHEXFAFBG3CS4ON6TCMII"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:roleDescription:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXZ5WCIQRA5FXXHM5HCLUP5LZZ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apDashConfigPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW64YL6WSM5G55OTUM6LV32SHAE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apUserGroupPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCZVEHV5FBJCMLC2PBNO4GKU73A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apDashConfigPartGroupId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLJEJTFC3YFHDZCAUPC5FTWL3CI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User2Role:apUserGroupPartGroupId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKME6XFPIUNEH5GKQJ3BV7URGSE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User2Role:GetAPFamilyList-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWVP7CH4TCRFWBHFTFJKUFGKPQA"),"GetAPFamilyList",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User2Role:replaceRole-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7B4K7HL2JNB67MD3LJGE5SBLGU"),"replaceRole",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User2Role:revokeRole-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLDP4MD5I5BHKJLUFBGOUUME52M"),"revokeRole",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Acs::User2Role:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									0,

									/*Radix::Acs::User2Role:General:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::Acs::User2Role:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Acs::User2Role:General:User2Role-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiRPKROTOMGJDZVEVZWS6653VSWU"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refECCRWGWYL5D73ETLVCXEB5FUN4"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[] {
										new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"), null, null, null)
									},
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::User2Role:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),"General",null,16,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colDC3DKGLHPDNRDHTWABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = 1 and ( </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"agc42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"pgpIPY24DCHVRDOJPZ2TZIQK7JSNU\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null)</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(280608,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Acs::User2Role:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Acs::User2Role:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::User2Role:acceptorName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),"acceptorName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TMGTERPXBA4RNZV3HEJRKFKUU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apUserGroupKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD7VLPHOA3VC4XL3QOLTWZLUHJI"),"apUserGroupKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5TZ7NCGKRD7PBSOIDBPMPXWWQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apUserGroupLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ4BWGI77MJD2HFPRQSVQXFBDI4"),"apUserGroupLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPH4HYYGUXJEBBENYZE2KU6I4PM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refYVMSFRT72FGF5EOKIMU5GZGO4E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apUserGroupMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4T2IAH72O5E6RNZPX3MNT44CQE"),"apUserGroupMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIBLBWKVBJEPVPXLVCYI53IHSY"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:areaTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),"areaTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QEEGVV6NVBSZLRXT62BL6UOUE"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:editorName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),"editorName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQTP77ZOFAX5OH2VAR3Q3PNN4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:hasReplacements-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),"hasReplacements",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"col3FF7Q3S6CPOBDHYRABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> AND ROWNUM&lt;=1)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6RLBLYS7JFK5CAD2G47LERU6A"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:isNew-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),"isNew",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7K3ICAHRPBFFPHGIAWMHXHTSQY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:isOwn-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC3DKGLHPDNRDHTWABQAQH3XQ4"),"isOwn",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXCEWRXONBJHFZJZAG7ZEJ7G32Y"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:replacedId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),"replacedId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMN6BZYS65EIHFJ7X47HX55ZL4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:roleId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),"roleId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLCBJ5RRMNHGHF2WWWVVWCSYYE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:roleTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),"roleTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7Y6B43CKV5FTBMRFE6B5IMLAZI"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:userName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),"userName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls64JV4FE5VNBW5JOKR5KKK6P7RM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:userTitle-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),"userTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGSQZRTC2VGPFFUFQON7Y2MDNQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2ZDQ5EV2J3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:isDualControlUsed-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),"isDualControlUsed",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:mayReplaceOrRevokeRole-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),"mayReplaceOrRevokeRole",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:isRevoked-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQNV5G4S5A6FKT3BLWVWFGCPQ"),"isRevoked",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"col3FF7Q3S6CPOBDHYRABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colFXSCOJCAZDNBDGMDABQAQH3XQ4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null AND ROWNUM&lt;=1)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:isReplaced-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2GERGO6Z5FJJBC6T6JHC6VNSQ"),"isReplaced",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colLLSKGT66EBE4XAS7CB3DODAAM4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tbl42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"col3FF7Q3S6CPOBDHYRABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec42K4K2TTGLNRDHRZABQAQH3XQ4\" PropId=\"colFXSCOJCAZDNBDGMDABQAQH3XQ4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> is null AND ROWNUM&lt;=1)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apDashConfigKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),"apDashConfigKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREHNEHF73RGBFD5IBZFRB2H5GA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apDashConfigMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),"apDashConfigMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUBJFY5KDZDJZKRGHAZKVRZ5CQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apDashConfigLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOAVV6AHEXFAFBG3CS4ON6TCMII"),"apDashConfigLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GZ7G3NHFJFVVMGT7KZI3SJAKA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refUBNZPHQTTFA5NGMY77T6QJANTY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:roleDescription-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"),"roleDescription",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAL4EKCO2BH6NDSOWDXIRKT7SU"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXZ5WCIQRA5FXXHM5HCLUP5LZZ4"),"ACCESS_FAMILY_ERR_STATUS",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apDashConfigPartGroupLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW64YL6WSM5G55OTUM6LV32SHAE"),"apDashConfigPartGroupLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFL3V5FC2NBCF7ODQVJ4Q4LAHT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refNSX4XZZSYVAOLNVRFRA7JIEO6Q"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apUserGroupPartGroupLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCZVEHV5FBJCMLC2PBNO4GKU73A"),"apUserGroupPartGroupLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEVRX4VKO7JAXTCIAOC6QZWWHPQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refEP72BBGGHBEY3FH2S5BRH7OD34"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apDashConfigPartGroupId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLJEJTFC3YFHDZCAUPC5FTWL3CI"),"apDashConfigPartGroupId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User2Role:apUserGroupPartGroupId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKME6XFPIUNEH5GKQJ3BV7URGSE"),"apUserGroupPartGroupId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::User2Role:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC2MUVKZFFRAJ3M4NY5GGR7QJ3U"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd7B4K7HL2JNB67MD3LJGE5SBLGU"),"onCommand_replaceOrRevokeRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOM23A3QALVD4BF3FOF5FOZAX3A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRK3LT4R2ZZFXDOSZC3DT42E6JI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdLDP4MD5I5BHKJLUFBGOUUME52M"),"onCommand_revokeRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4MXD7IV4TVC4VAW7TE6CPKRIAA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRSDK6MMOW5HMHKN625K6H5O2HM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXBZIPM3PTRFNPLKSYZGZKDX6XE"),"replaceOrRevokeRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLS2DB5ZBYFAOFNKFD5XM7PL7RY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdWVP7CH4TCRFWBHFTFJKUFGKPQA"),"cmdGetAPFamilyList",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY4GVTASBSRCTJJLNSHVES37Z5E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZRJVPIKTZ5FN3AR6E3PUM2GDSU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5WW7WQOBKZCYXFWK4HPW47ZIXA"),"fillRoleFamiliesList",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sRoleId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXY3EQDSOLRFKRFDS2BBXRCOFUE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("list",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5QMFEYQX6RAAJFVTQ3PRHJ7EPY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVKJ6LUZ355ADZCPI6XSDWSWXKY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2R2AG4UJA5BMNMGEWW4BH6PQJM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPOSWCGBTBZFWTCSY6LKV6ZZYNY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7PFZFCYBNBGDDGIZDY5ZSREICM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6Q4QUVAMFZHLRA6EUDOUAJDBYA"),"getArea",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("arte",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZWBMTLG435FIXDXE2OQJ63FED4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRMQK4Q7PBJDF7MTGDCTNZ27S5A"),"getAuditPropValTitle",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUNW7KWW3LVFVFI2XINAQGGJNMQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIPR37SNTQRCTDN53NDAIUMBGZE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXAZOQAYI4JET5JFMOPX67LD63A"),"checkRoleFamilies",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleFamilies",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZNS7ZEUXKRGTNM4TJG53P3BC6Y"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVKZC6B2545FD7FSNHRKSSGTOVQ"),"getAreaTitle",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprADWLGVL5FFDEHN77V3YQD4GMME")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("apUserGroupMode",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3ML4J5CGRJEGBD64BTLVBJLZCE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("apUserGroupLink",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7VM2PRJXGRC5PO6U2OEAH45KV4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("apUserGroupPartitions",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLJDYAMHEYNETLM3S6EVA5OU7J4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("apDashConfigMode",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQYMJ5MDDG5DVRPLK4C6UADXC5M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("apDashConfigLink",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPPBSZEPEFVEDRGST7IOELC4BFE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("apDashConfigPartitions",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDHG6QM3LIJHPPPIRGN4NQT5UOA"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthX2YKR45UFVBN5GHUNCJDXZ7MVI"),"getAccessFamilyErrStatus",true,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr34PEEJCVRJDS5FG4WXTXTXE67I"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.INHERITED,org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2ZDQ5EV2J3NRDAQSABIFNQAAAE"),null,false);
}

/* Radix::Acs::User2Role - Desktop Executable*/

/*Radix::Acs::User2Role-Entity Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role")
public interface User2Role {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Acs::User2RoleGroup:isReplacement:isReplacement-Presentation Property*/




		public class IsReplacement extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
			public IsReplacement(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Bool dummy = ((Bool)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2RoleGroup:isReplacement:isReplacement")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2RoleGroup:isReplacement:isReplacement")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpIPY24DCHVRDOJPZ2TZIQK7JSNU);}







		public org.radixware.ads.Acs.explorer.User2Role.User2Role_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.explorer.User2Role.User2Role_DefaultModel )  super.getEntity(i);}
	}













































































































































































	/*Radix::Acs::User2Role:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Acs::User2Role:apUserGroupMode:apUserGroupMode-Presentation Property*/


	public class ApUserGroupMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApUserGroupMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupMode:apUserGroupMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupMode:apUserGroupMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApUserGroupMode getApUserGroupMode();
	/*Radix::Acs::User2Role:apDashConfigMode:apDashConfigMode-Presentation Property*/


	public class ApDashConfigMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApDashConfigMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigMode:apDashConfigMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigMode:apDashConfigMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApDashConfigMode getApDashConfigMode();
	/*Radix::Acs::User2Role:userName:userName-Presentation Property*/


	public class UserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userName:userName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userName:userName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserName getUserName();
	/*Radix::Acs::User2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink-Presentation Property*/


	public class ApUserGroupPartGroupLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApUserGroupPartGroupLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupLink getApUserGroupPartGroupLink();
	/*Radix::Acs::User2Role:apUserGroupKey:apUserGroupKey-Presentation Property*/


	public class ApUserGroupKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ApUserGroupKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupKey:apUserGroupKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupKey:apUserGroupKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApUserGroupKey getApUserGroupKey();
	/*Radix::Acs::User2Role:isOwn:isOwn-Presentation Property*/


	public class IsOwn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsOwn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isOwn:isOwn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isOwn:isOwn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsOwn getIsOwn();
	/*Radix::Acs::User2Role:apDashConfigKey:apDashConfigKey-Presentation Property*/


	public class ApDashConfigKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ApDashConfigKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigKey:apDashConfigKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigKey:apDashConfigKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApDashConfigKey getApDashConfigKey();
	/*Radix::Acs::User2Role:isReplaced:isReplaced-Presentation Property*/


	public class IsReplaced extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public IsReplaced(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isReplaced:isReplaced")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isReplaced:isReplaced")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsReplaced getIsReplaced();
	/*Radix::Acs::User2Role:roleId:roleId-Presentation Property*/


	public class RoleId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleId:roleId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleId:roleId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleId getRoleId();
	/*Radix::Acs::User2Role:apUserGroupLink:apUserGroupLink-Presentation Property*/


	public class ApUserGroupLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApUserGroupLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.UserGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.UserGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupLink:apUserGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupLink:apUserGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupLink getApUserGroupLink();
	/*Radix::Acs::User2Role:acceptorName:acceptorName-Presentation Property*/


	public class AcceptorName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AcceptorName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:acceptorName:acceptorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:acceptorName:acceptorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AcceptorName getAcceptorName();
	/*Radix::Acs::User2Role:apUserGroupPartGroupId:apUserGroupPartGroupId-Presentation Property*/


	public class ApUserGroupPartGroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApUserGroupPartGroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupId getApUserGroupPartGroupId();
	/*Radix::Acs::User2Role:apDashConfigPartGroupId:apDashConfigPartGroupId-Presentation Property*/


	public class ApDashConfigPartGroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApDashConfigPartGroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupId getApDashConfigPartGroupId();
	/*Radix::Acs::User2Role:replacedId:replacedId-Presentation Property*/


	public class ReplacedId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ReplacedId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replacedId:replacedId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replacedId:replacedId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ReplacedId getReplacedId();
	/*Radix::Acs::User2Role:isNew:isNew-Presentation Property*/


	public class IsNew extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsNew(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isNew:isNew")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isNew:isNew")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsNew getIsNew();
	/*Radix::Acs::User2Role:userTitle:userTitle-Presentation Property*/


	public class UserTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public UserTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userTitle:userTitle")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userTitle:userTitle")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserTitle getUserTitle();
	/*Radix::Acs::User2Role:editorName:editorName-Presentation Property*/


	public class EditorName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EditorName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:editorName:editorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:editorName:editorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EditorName getEditorName();
	/*Radix::Acs::User2Role:apDashConfigLink:apDashConfigLink-Presentation Property*/


	public class ApDashConfigLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApDashConfigLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.SystemMonitor.explorer.DashConfig.DashConfig_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.SystemMonitor.explorer.DashConfig.DashConfig_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.SystemMonitor.explorer.DashConfig.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.SystemMonitor.explorer.DashConfig.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigLink:apDashConfigLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigLink:apDashConfigLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigLink getApDashConfigLink();
	/*Radix::Acs::User2Role:isRevoked:isRevoked-Presentation Property*/


	public class IsRevoked extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public IsRevoked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isRevoked:isRevoked")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isRevoked:isRevoked")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsRevoked getIsRevoked();
	/*Radix::Acs::User2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink-Presentation Property*/


	public class ApDashConfigPartGroupLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApDashConfigPartGroupLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.explorer.PartitionGroup.PartitionGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.explorer.PartitionGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupLink getApDashConfigPartGroupLink();
	/*Radix::Acs::User2Role:hasReplacements:hasReplacements-Presentation Property*/


	public class HasReplacements extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HasReplacements(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:hasReplacements:hasReplacements")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:hasReplacements:hasReplacements")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HasReplacements getHasReplacements();
	/*Radix::Acs::User2Role:isDualControlUsed:isDualControlUsed-Presentation Property*/


	public class IsDualControlUsed extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsDualControlUsed(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isDualControlUsed:isDualControlUsed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isDualControlUsed:isDualControlUsed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsDualControlUsed getIsDualControlUsed();
	/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole-Presentation Property*/


	public class MayReplaceOrRevokeRole extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public MayReplaceOrRevokeRole(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public MayReplaceOrRevokeRole getMayReplaceOrRevokeRole();
	/*Radix::Acs::User2Role:areaTitle:areaTitle-Presentation Property*/


	public class AreaTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AreaTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:areaTitle:areaTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:areaTitle:areaTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle();
	/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS-Presentation Property*/


	public class ACCESS_FAMILY_ERR_STATUS extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ACCESS_FAMILY_ERR_STATUS(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ACCESS_FAMILY_ERR_STATUS getACCESS_FAMILY_ERR_STATUS();
	/*Radix::Acs::User2Role:roleDescription:roleDescription-Presentation Property*/


	public class RoleDescription extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleDescription:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleDescription:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription();
	/*Radix::Acs::User2Role:roleTitle:roleTitle-Presentation Property*/


	public class RoleTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleTitle:roleTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleTitle:roleTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleTitle getRoleTitle();
	public static class ReplaceRole extends org.radixware.kernel.common.client.models.items.Command{
		protected ReplaceRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument send(org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument.class);
		}

	}

	public static class RevokeRole extends org.radixware.kernel.common.client.models.items.Command{
		protected RevokeRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument send(org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument.class);
		}

	}

	public static class GetAPFamilyList extends org.radixware.kernel.common.client.models.items.Command{
		protected GetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.adsdef.APFamiliesDocument send(org.radixware.schemas.adsdef.RoleIDDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.adsdef.APFamiliesDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.adsdef.APFamiliesDocument.class);
		}

	}



}

/* Radix::Acs::User2Role - Desktop Meta*/

/*Radix::Acs::User2Role-Entity Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User2Role_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::User2Role:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::User2Role",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPTY2QQWGBEIPG37PIP2R6G52M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),0,

			/*Radix::Acs::User2Role:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::User2Role:acceptorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),
						"acceptorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TMGTERPXBA4RNZV3HEJRKFKUU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:acceptorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apUserGroupKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD7VLPHOA3VC4XL3QOLTWZLUHJI"),
						"apUserGroupKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5TZ7NCGKRD7PBSOIDBPMPXWWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apUserGroupKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apUserGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ4BWGI77MJD2HFPRQSVQXFBDI4"),
						"apUserGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPH4HYYGUXJEBBENYZE2KU6I4PM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
						0,
						0,false),

					/*Radix::Acs::User2Role:apUserGroupMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4T2IAH72O5E6RNZPX3MNT44CQE"),
						"apUserGroupMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIBLBWKVBJEPVPXLVCYI53IHSY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apUserGroupMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:areaTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),
						"areaTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QEEGVV6NVBSZLRXT62BL6UOUE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:areaTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:editorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),
						"editorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQTP77ZOFAX5OH2VAR3Q3PNN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:editorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:hasReplacements:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),
						"hasReplacements",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:hasReplacements:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6RLBLYS7JFK5CAD2G47LERU6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isNew:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),
						"isNew",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7K3ICAHRPBFFPHGIAWMHXHTSQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isNew:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isOwn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC3DKGLHPDNRDHTWABQAQH3XQ4"),
						"isOwn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXCEWRXONBJHFZJZAG7ZEJ7G32Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isOwn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:replacedId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),
						"replacedId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMN6BZYS65EIHFJ7X47HX55ZL4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:replacedId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:roleId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),
						"roleId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLCBJ5RRMNHGHF2WWWVVWCSYYE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:roleId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:roleTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),
						"roleTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7Y6B43CKV5FTBMRFE6B5IMLAZI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpe2ATLT7MVSPORDID2ABQAQH3XQ4"),
						true,

						/*Radix::Acs::User2Role:roleTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:userName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),
						"userName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls64JV4FE5VNBW5JOKR5KKK6P7RM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:userName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:userTitle:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),
						"userTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGSQZRTC2VGPFFUFQON7Y2MDNQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
						0,
						0,false),

					/*Radix::Acs::User2Role:isDualControlUsed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),
						"isDualControlUsed",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isDualControlUsed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),
						"mayReplaceOrRevokeRole",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isRevoked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQNV5G4S5A6FKT3BLWVWFGCPQ"),
						"isRevoked",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isRevoked:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isReplaced:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2GERGO6Z5FJJBC6T6JHC6VNSQ"),
						"isReplaced",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isReplaced:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),
						"apDashConfigKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREHNEHF73RGBFD5IBZFRB2H5GA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apDashConfigKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),
						"apDashConfigMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUBJFY5KDZDJZKRGHAZKVRZ5CQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apDashConfigMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOAVV6AHEXFAFBG3CS4ON6TCMII"),
						"apDashConfigLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GZ7G3NHFJFVVMGT7KZI3SJAKA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
						133693439,
						133693439,false),

					/*Radix::Acs::User2Role:roleDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"),
						"roleDescription",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAL4EKCO2BH6NDSOWDXIRKT7SU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:roleDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXZ5WCIQRA5FXXHM5HCLUP5LZZ4"),
						"ACCESS_FAMILY_ERR_STATUS",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW64YL6WSM5G55OTUM6LV32SHAE"),
						"apDashConfigPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFL3V5FC2NBCF7ODQVJ4Q4LAHT4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),
						133693439,
						133693439,false),

					/*Radix::Acs::User2Role:apUserGroupPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCZVEHV5FBJCMLC2PBNO4GKU73A"),
						"apUserGroupPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEVRX4VKO7JAXTCIAOC6QZWWHPQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),
						133693439,
						133693439,false),

					/*Radix::Acs::User2Role:apDashConfigPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLJEJTFC3YFHDZCAUPC5FTWL3CI"),
						"apDashConfigPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apDashConfigPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apUserGroupPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKME6XFPIUNEH5GKQJ3BV7URGSE"),
						"apUserGroupPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apUserGroupPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User2Role:GetAPFamilyList-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWVP7CH4TCRFWBHFTFJKUFGKPQA"),
						"GetAPFamilyList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User2Role:replaceRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7B4K7HL2JNB67MD3LJGE5SBLGU"),
						"replaceRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25WZH4IGDNCJFKCO7QOIMAYKBI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3F33WBHETRFFLEXYM37OBQFCKY"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User2Role:revokeRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLDP4MD5I5BHKJLUFBGOUUME52M"),
						"revokeRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFT6GR67KSJBARDGQYKQQFRTUE4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYHTVMMFW4VHVZJRUDLRALGGYXI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2ZDQ5EV2J3NRDAQSABIFNQAAAE"),"User2Role=>User (userName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4")},new String[]{"userName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refECCRWGWYL5D73ETLVCXEB5FUN4"),"User2Role=>User2Role (replacedId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4")},new String[]{"replacedId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refEP72BBGGHBEY3FH2S5BRH7OD34"),"User2Role=>PartitionGroup (apUserGroupPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colKME6XFPIUNEH5GKQJ3BV7URGSE")},new String[]{"apUserGroupPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refNSX4XZZSYVAOLNVRFRA7JIEO6Q"),"User2Role=>PartitionGroup (apDashConfigPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLJEJTFC3YFHDZCAUPC5FTWL3CI")},new String[]{"apDashConfigPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refUBNZPHQTTFA5NGMY77T6QJANTY"),"User2Role=>DashConfig (apDashConfigKey=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY")},new String[]{"apDashConfigKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY")},new String[]{"guid"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refYVMSFRT72FGF5EOKIMU5GZGO4E"),"User2Role=>UserGroup (apUserGroupKey=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colD7VLPHOA3VC4XL3QOLTWZLUHJI")},new String[]{"apUserGroupKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI")},
			false,true,false);
}

/* Radix::Acs::User2Role - Web Executable*/

/*Radix::Acs::User2Role-Entity Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role")
public interface User2Role {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Acs::User2RoleGroup:isReplacement:isReplacement-Presentation Property*/




		public class IsReplacement extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
			public IsReplacement(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
				super(model,def);
			}
			@Override
			public Object getValueObject(){return getValue();}
			@Override
			public void setValueObject(final Object x){
				Bool dummy = ((Bool)x);
				setValue(dummy);
			}












			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2RoleGroup:isReplacement:isReplacement")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2RoleGroup:isReplacement:isReplacement")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpIPY24DCHVRDOJPZ2TZIQK7JSNU);}







		public org.radixware.ads.Acs.web.User2Role.User2Role_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.web.User2Role.User2Role_DefaultModel )  super.getEntity(i);}
	}













































































































































































	/*Radix::Acs::User2Role:id:id-Presentation Property*/


	public class Id extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Id(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Acs::User2Role:apUserGroupMode:apUserGroupMode-Presentation Property*/


	public class ApUserGroupMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApUserGroupMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupMode:apUserGroupMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupMode:apUserGroupMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApUserGroupMode getApUserGroupMode();
	/*Radix::Acs::User2Role:apDashConfigMode:apDashConfigMode-Presentation Property*/


	public class ApDashConfigMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApDashConfigMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigMode:apDashConfigMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigMode:apDashConfigMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApDashConfigMode getApDashConfigMode();
	/*Radix::Acs::User2Role:userName:userName-Presentation Property*/


	public class UserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userName:userName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userName:userName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UserName getUserName();
	/*Radix::Acs::User2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink-Presentation Property*/


	public class ApUserGroupPartGroupLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApUserGroupPartGroupLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupLink getApUserGroupPartGroupLink();
	/*Radix::Acs::User2Role:apUserGroupKey:apUserGroupKey-Presentation Property*/


	public class ApUserGroupKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ApUserGroupKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupKey:apUserGroupKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupKey:apUserGroupKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApUserGroupKey getApUserGroupKey();
	/*Radix::Acs::User2Role:isOwn:isOwn-Presentation Property*/


	public class IsOwn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsOwn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isOwn:isOwn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isOwn:isOwn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsOwn getIsOwn();
	/*Radix::Acs::User2Role:apDashConfigKey:apDashConfigKey-Presentation Property*/


	public class ApDashConfigKey extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ApDashConfigKey(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigKey:apDashConfigKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigKey:apDashConfigKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApDashConfigKey getApDashConfigKey();
	/*Radix::Acs::User2Role:isReplaced:isReplaced-Presentation Property*/


	public class IsReplaced extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public IsReplaced(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isReplaced:isReplaced")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isReplaced:isReplaced")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsReplaced getIsReplaced();
	/*Radix::Acs::User2Role:roleId:roleId-Presentation Property*/


	public class RoleId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleId:roleId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleId:roleId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleId getRoleId();
	/*Radix::Acs::User2Role:apUserGroupLink:apUserGroupLink-Presentation Property*/


	public class ApUserGroupLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApUserGroupLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.UserGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.UserGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupLink:apUserGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupLink:apUserGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupLink getApUserGroupLink();
	/*Radix::Acs::User2Role:acceptorName:acceptorName-Presentation Property*/


	public class AcceptorName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AcceptorName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:acceptorName:acceptorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:acceptorName:acceptorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AcceptorName getAcceptorName();
	/*Radix::Acs::User2Role:apUserGroupPartGroupId:apUserGroupPartGroupId-Presentation Property*/


	public class ApUserGroupPartGroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApUserGroupPartGroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupId getApUserGroupPartGroupId();
	/*Radix::Acs::User2Role:apDashConfigPartGroupId:apDashConfigPartGroupId-Presentation Property*/


	public class ApDashConfigPartGroupId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ApDashConfigPartGroupId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupId getApDashConfigPartGroupId();
	/*Radix::Acs::User2Role:replacedId:replacedId-Presentation Property*/


	public class ReplacedId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ReplacedId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replacedId:replacedId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:replacedId:replacedId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ReplacedId getReplacedId();
	/*Radix::Acs::User2Role:isNew:isNew-Presentation Property*/


	public class IsNew extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsNew(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isNew:isNew")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isNew:isNew")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsNew getIsNew();
	/*Radix::Acs::User2Role:userTitle:userTitle-Presentation Property*/


	public class UserTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public UserTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.User_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.User.User_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.User.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.User.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userTitle:userTitle")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:userTitle:userTitle")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public UserTitle getUserTitle();
	/*Radix::Acs::User2Role:editorName:editorName-Presentation Property*/


	public class EditorName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EditorName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:editorName:editorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:editorName:editorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EditorName getEditorName();
	/*Radix::Acs::User2Role:apDashConfigLink:apDashConfigLink-Presentation Property*/


	public class ApDashConfigLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApDashConfigLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.SystemMonitor.web.DashConfig.DashConfig_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.SystemMonitor.web.DashConfig.DashConfig_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.SystemMonitor.web.DashConfig.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.SystemMonitor.web.DashConfig.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigLink:apDashConfigLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigLink:apDashConfigLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigLink getApDashConfigLink();
	/*Radix::Acs::User2Role:isRevoked:isRevoked-Presentation Property*/


	public class IsRevoked extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public IsRevoked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isRevoked:isRevoked")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isRevoked:isRevoked")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsRevoked getIsRevoked();
	/*Radix::Acs::User2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink-Presentation Property*/


	public class ApDashConfigPartGroupLink extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ApDashConfigPartGroupLink(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Acs.web.PartitionGroup.PartitionGroup_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Acs.web.PartitionGroup.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupLink getApDashConfigPartGroupLink();
	/*Radix::Acs::User2Role:hasReplacements:hasReplacements-Presentation Property*/


	public class HasReplacements extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HasReplacements(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:hasReplacements:hasReplacements")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:hasReplacements:hasReplacements")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HasReplacements getHasReplacements();
	/*Radix::Acs::User2Role:isDualControlUsed:isDualControlUsed-Presentation Property*/


	public class IsDualControlUsed extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsDualControlUsed(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isDualControlUsed:isDualControlUsed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:isDualControlUsed:isDualControlUsed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsDualControlUsed getIsDualControlUsed();
	/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole-Presentation Property*/


	public class MayReplaceOrRevokeRole extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public MayReplaceOrRevokeRole(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public MayReplaceOrRevokeRole getMayReplaceOrRevokeRole();
	/*Radix::Acs::User2Role:areaTitle:areaTitle-Presentation Property*/


	public class AreaTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AreaTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:areaTitle:areaTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:areaTitle:areaTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle();
	/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS-Presentation Property*/


	public class ACCESS_FAMILY_ERR_STATUS extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ACCESS_FAMILY_ERR_STATUS(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ACCESS_FAMILY_ERR_STATUS getACCESS_FAMILY_ERR_STATUS();
	/*Radix::Acs::User2Role:roleDescription:roleDescription-Presentation Property*/


	public class RoleDescription extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleDescription:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleDescription:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription();
	/*Radix::Acs::User2Role:roleTitle:roleTitle-Presentation Property*/


	public class RoleTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RoleTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleTitle:roleTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:roleTitle:roleTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleTitle getRoleTitle();
	public static class ReplaceRole extends org.radixware.kernel.common.client.models.items.Command{
		protected ReplaceRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument send(org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument.class);
		}

	}

	public static class RevokeRole extends org.radixware.kernel.common.client.models.items.Command{
		protected RevokeRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument send(org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument.class);
		}

	}

	public static class GetAPFamilyList extends org.radixware.kernel.common.client.models.items.Command{
		protected GetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.adsdef.APFamiliesDocument send(org.radixware.schemas.adsdef.RoleIDDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.adsdef.APFamiliesDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.adsdef.APFamiliesDocument.class);
		}

	}



}

/* Radix::Acs::User2Role - Web Meta*/

/*Radix::Acs::User2Role-Entity Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User2Role_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::User2Role:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::User2Role",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPTY2QQWGBEIPG37PIP2R6G52M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),0,

			/*Radix::Acs::User2Role:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::User2Role:acceptorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),
						"acceptorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TMGTERPXBA4RNZV3HEJRKFKUU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:acceptorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apUserGroupKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD7VLPHOA3VC4XL3QOLTWZLUHJI"),
						"apUserGroupKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5TZ7NCGKRD7PBSOIDBPMPXWWQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apUserGroupKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apUserGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ4BWGI77MJD2HFPRQSVQXFBDI4"),
						"apUserGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPH4HYYGUXJEBBENYZE2KU6I4PM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
						0,
						0,false),

					/*Radix::Acs::User2Role:apUserGroupMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4T2IAH72O5E6RNZPX3MNT44CQE"),
						"apUserGroupMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIBLBWKVBJEPVPXLVCYI53IHSY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apUserGroupMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:areaTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),
						"areaTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QEEGVV6NVBSZLRXT62BL6UOUE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:areaTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:editorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),
						"editorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQTP77ZOFAX5OH2VAR3Q3PNN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:editorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:hasReplacements:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),
						"hasReplacements",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:hasReplacements:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6RLBLYS7JFK5CAD2G47LERU6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isNew:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),
						"isNew",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7K3ICAHRPBFFPHGIAWMHXHTSQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isNew:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isOwn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC3DKGLHPDNRDHTWABQAQH3XQ4"),
						"isOwn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXCEWRXONBJHFZJZAG7ZEJ7G32Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isOwn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:replacedId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),
						"replacedId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMN6BZYS65EIHFJ7X47HX55ZL4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:replacedId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:roleId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),
						"roleId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLCBJ5RRMNHGHF2WWWVVWCSYYE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:roleId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:roleTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),
						"roleTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7Y6B43CKV5FTBMRFE6B5IMLAZI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeNRYE7B3JDZALLFTFUBNWYVYJWA"),
						true,

						/*Radix::Acs::User2Role:roleTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:userName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),
						"userName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls64JV4FE5VNBW5JOKR5KKK6P7RM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:userName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:userTitle:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),
						"userTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGSQZRTC2VGPFFUFQON7Y2MDNQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
						0,
						0,false),

					/*Radix::Acs::User2Role:isDualControlUsed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),
						"isDualControlUsed",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isDualControlUsed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),
						"mayReplaceOrRevokeRole",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:mayReplaceOrRevokeRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isRevoked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSPQNV5G4S5A6FKT3BLWVWFGCPQ"),
						"isRevoked",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isRevoked:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:isReplaced:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2GERGO6Z5FJJBC6T6JHC6VNSQ"),
						"isReplaced",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.EXPRESSION,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:isReplaced:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),
						"apDashConfigKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREHNEHF73RGBFD5IBZFRB2H5GA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apDashConfigKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),
						"apDashConfigMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUBJFY5KDZDJZKRGHAZKVRZ5CQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apDashConfigMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOAVV6AHEXFAFBG3CS4ON6TCMII"),
						"apDashConfigLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GZ7G3NHFJFVVMGT7KZI3SJAKA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),
						133693439,
						133693439,false),

					/*Radix::Acs::User2Role:roleDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"),
						"roleDescription",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAL4EKCO2BH6NDSOWDXIRKT7SU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:roleDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXZ5WCIQRA5FXXHM5HCLUP5LZZ4"),
						"ACCESS_FAMILY_ERR_STATUS",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apDashConfigPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW64YL6WSM5G55OTUM6LV32SHAE"),
						"apDashConfigPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFL3V5FC2NBCF7ODQVJ4Q4LAHT4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),
						133693439,
						133693439,false),

					/*Radix::Acs::User2Role:apUserGroupPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCZVEHV5FBJCMLC2PBNO4GKU73A"),
						"apUserGroupPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEVRX4VKO7JAXTCIAOC6QZWWHPQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),
						133693439,
						133693439,false),

					/*Radix::Acs::User2Role:apDashConfigPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLJEJTFC3YFHDZCAUPC5FTWL3CI"),
						"apDashConfigPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apDashConfigPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User2Role:apUserGroupPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKME6XFPIUNEH5GKQJ3BV7URGSE"),
						"apUserGroupPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User2Role:apUserGroupPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User2Role:GetAPFamilyList-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWVP7CH4TCRFWBHFTFJKUFGKPQA"),
						"GetAPFamilyList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User2Role:replaceRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7B4K7HL2JNB67MD3LJGE5SBLGU"),
						"replaceRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25WZH4IGDNCJFKCO7QOIMAYKBI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3F33WBHETRFFLEXYM37OBQFCKY"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User2Role:revokeRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLDP4MD5I5BHKJLUFBGOUUME52M"),
						"revokeRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFT6GR67KSJBARDGQYKQQFRTUE4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYHTVMMFW4VHVZJRUDLRALGGYXI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2ZDQ5EV2J3NRDAQSABIFNQAAAE"),"User2Role=>User (userName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4")},new String[]{"userName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refECCRWGWYL5D73ETLVCXEB5FUN4"),"User2Role=>User2Role (replacedId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4")},new String[]{"replacedId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refEP72BBGGHBEY3FH2S5BRH7OD34"),"User2Role=>PartitionGroup (apUserGroupPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colKME6XFPIUNEH5GKQJ3BV7URGSE")},new String[]{"apUserGroupPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refNSX4XZZSYVAOLNVRFRA7JIEO6Q"),"User2Role=>PartitionGroup (apDashConfigPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colLJEJTFC3YFHDZCAUPC5FTWL3CI")},new String[]{"apDashConfigPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refUBNZPHQTTFA5NGMY77T6QJANTY"),"User2Role=>DashConfig (apDashConfigKey=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY")},new String[]{"apDashConfigKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY")},new String[]{"guid"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refYVMSFRT72FGF5EOKIMU5GZGO4E"),"User2Role=>UserGroup (apUserGroupKey=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colD7VLPHOA3VC4XL3QOLTWZLUHJI")},new String[]{"apUserGroupKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI")},
			false,true,false);
}

/* Radix::Acs::User2Role:General - Desktop Meta*/

/*Radix::Acs::User2Role:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZSLQQQXYFCHNEG32CLKL6DIXQ"),
	null,

	/*Radix::Acs::User2Role:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::User2Role:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAIDVVLFWOBAQHL722TFTVWO6LM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4T2IAH72O5E6RNZPX3MNT44CQE"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ4BWGI77MJD2HFPRQSVQXFBDI4"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),1,9,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOAVV6AHEXFAFBG3CS4ON6TCMII"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCZVEHV5FBJCMLC2PBNO4GKU73A"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW64YL6WSM5G55OTUM6LV32SHAE"),0,8,2,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAIDVVLFWOBAQHL722TFTVWO6LM"))}
	,

	/*Radix::Acs::User2Role:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::User2Role:General:User2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiRPKROTOMGJDZVEVZWS6653VSWU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[] {
		new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"), org.radixware.kernel.common.enums.EPropertyVisibility.ONLY_FOR_EXISTENT, null, null, null, null)
	},
	0,0,0,null);
}
/* Radix::Acs::User2Role:General - Web Meta*/

/*Radix::Acs::User2Role:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZSLQQQXYFCHNEG32CLKL6DIXQ"),
	null,

	/*Radix::Acs::User2Role:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::User2Role:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAIDVVLFWOBAQHL722TFTVWO6LM"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4T2IAH72O5E6RNZPX3MNT44CQE"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ4BWGI77MJD2HFPRQSVQXFBDI4"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),1,9,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOAVV6AHEXFAFBG3CS4ON6TCMII"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCZVEHV5FBJCMLC2PBNO4GKU73A"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW64YL6WSM5G55OTUM6LV32SHAE"),0,8,2,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAIDVVLFWOBAQHL722TFTVWO6LM"))}
	,

	/*Radix::Acs::User2Role:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::User2Role:General:User2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiRPKROTOMGJDZVEVZWS6653VSWU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[] {
		new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZCORQ4HP5FH6JHKJGNCFKFV5S4"), org.radixware.kernel.common.enums.EPropertyVisibility.ONLY_FOR_EXISTENT, null, null, null, null)
	},
	0,0,0,null);
}
/* Radix::Acs::User2Role:General:Model - Desktop Executable*/

/*Radix::Acs::User2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.User2Role.User2Role_DefaultModel implements org.radixware.ads.Acs.common_client.IRoleHolder  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::User2Role:General:Model:roleId-Presentation Property*/




	public class RoleId extends org.radixware.ads.Acs.explorer.User2Role.colFXSCOJCAZDNBDGMDABQAQH3XQ4{
		public RoleId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleId")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleId")
		public published   void setValue(Str val) {

			internal[roleId]= val;
			User2Role:General:Model.this.getCommand(idof[User2Role:GetAPFamilyList]).execute();
		}
	}
	public RoleId getRoleId(){return (RoleId)getProperty(colFXSCOJCAZDNBDGMDABQAQH3XQ4);}

	/*Radix::Acs::User2Role:General:Model:apUserGroupMode-Presentation Property*/




	public class ApUserGroupMode extends org.radixware.ads.Acs.explorer.User2Role.col4T2IAH72O5E6RNZPX3MNT44CQE{
		public ApUserGroupMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apUserGroupMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[Acs::UserGroup]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apUserGroupMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apUserGroupMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apUserGroupMode]!=val)
			{
			 apUserGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			 apUserGroupPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value);
			 internal[apUserGroupMode] = val;
			} 
		}
	}
	public ApUserGroupMode getApUserGroupMode(){return (ApUserGroupMode)getProperty(col4T2IAH72O5E6RNZPX3MNT44CQE);}

	/*Radix::Acs::User2Role:General:Model:apDashConfigMode-Presentation Property*/




	public class ApDashConfigMode extends org.radixware.ads.Acs.explorer.User2Role.colCFPY2QWLVRD7HFRENRKJ32K6CQ{
		public ApDashConfigMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apDashConfigMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[SystemMonitor::DashConfig]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apDashConfigMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apDashConfigMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apDashConfigMode]!=val)
			{
			 apDashConfigLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			 apDashConfigPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value); 
			 internal[apDashConfigMode] = val;
			}
		}
	}
	public ApDashConfigMode getApDashConfigMode(){return (ApDashConfigMode)getProperty(colCFPY2QWLVRD7HFRENRKJ32K6CQ);}

	/*Radix::Acs::User2Role:General:Model:restrictedApFamilies-Dynamic Property*/



	protected java.util.Set<org.radixware.kernel.common.types.Id> restrictedApFamilies=new java.util.HashSet<Types::Id>();;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:restrictedApFamilies")
	public final published  java.util.Set<org.radixware.kernel.common.types.Id> getRestrictedApFamilies() {
		return restrictedApFamilies;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:restrictedApFamilies")
	public final published   void setRestrictedApFamilies(java.util.Set<org.radixware.kernel.common.types.Id> val) {
		restrictedApFamilies = val;
	}

	/*Radix::Acs::User2Role:General:Model:areaTitle-Presentation Property*/




	public class AreaTitle extends org.radixware.ads.Acs.explorer.User2Role.prdUQAPLPPIBRFVLO4VAPILCZSDLM{
		public AreaTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::Acs::User2Role:General:Model:areaTitle:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User2Role:General:Model:areaTitle:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:areaTitle:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:areaTitle:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:areaTitle:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:areaTitle:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			if (areaTitle.Value != null && areaTitle.Value.contains(ACCESS_FAMILY_ERR_STATUS.Value)) {
			    return options.changeForegroundColor(Utils::Color.RED);
			} else {
			    return options;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:areaTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:areaTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle(){return (AreaTitle)getProperty(prdUQAPLPPIBRFVLO4VAPILCZSDLM);}

	/*Radix::Acs::User2Role:General:Model:roleDescription-Presentation Property*/




	public class RoleDescription extends org.radixware.ads.Acs.explorer.User2Role.prdZCORQ4HP5FH6JHKJGNCFKFV5S4{
		public RoleDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription(){return (RoleDescription)getProperty(prdZCORQ4HP5FH6JHKJGNCFKFV5S4);}
















	/*Radix::Acs::User2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::User2Role:General:Model:getRoleIdProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:getRoleIdProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleIdProp (org.radixware.kernel.common.types.Id roleTitlePropId) {
		return roleId;
	}

	/*Radix::Acs::User2Role:General:Model:onCommand_cmdGetAPFamilyList-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:onCommand_cmdGetAPFamilyList")
	public published  void onCommand_cmdGetAPFamilyList (org.radixware.ads.Acs.explorer.User2Role.GetAPFamilyList command) {
		try {
		    if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)) {
		        if (roleId.getValue() != null) {
		            org.radixware.schemas.adsdef.RoleIDDocument RoleID = org.radixware.schemas.adsdef.RoleIDDocument.Factory.newInstance();
		            RoleID.RoleID = org.radixware.schemas.adsdef.RoleIDDocument.RoleID.Factory.newInstance();
		            RoleID.RoleID.Id = Types::Id.Factory.loadFrom(roleId.getValue());
		            org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies output = command.send(RoleID).APFamilies;
		            if(output.Description != null) {
		                roleDescription.setValue(output.Description);
		            }
		            boolean flag;
		            restrictedApFamilies.clear();
		            restrictedApFamilies.addAll(output.IdList);
		            final boolean showStoredValue = !isNew() && !roleId.isValEdited();
		            
		            flag = output.IdList.contains(idof[Acs::UserGroup]) || (showStoredValue && apUserGroupMode.Value != AccessAreaMode:Unbounded);
		            apUserGroupMode.setVisible(flag);
		            apUserGroupLink.setVisible(flag && apUserGroupMode.Value == AccessAreaMode:BoundedByPart);
		            apUserGroupPartGroupLink.setVisible(flag && apUserGroupMode.Value == AccessAreaMode:BoundedByGroup);

		            flag = output.IdList.contains(idof[SystemMonitor::DashConfig]) || (showStoredValue && apDashConfigMode.Value != AccessAreaMode:Unbounded);
		            apDashConfigMode.setVisible(flag);
		            apDashConfigLink.setVisible(flag && apDashConfigMode.Value == AccessAreaMode:BoundedByPart);
		            apDashConfigPartGroupLink.setVisible(flag && apDashConfigMode.Value == AccessAreaMode:BoundedByGroup);
		                        
		        } else {
		            apUserGroupMode.setVisible(false);            
		            apUserGroupLink.setVisible(false);
		            apUserGroupPartGroupLink.setVisible(false);
		            
		            apDashConfigMode.setVisible(false);
		            apDashConfigLink.setVisible(false);
		            apDashConfigPartGroupLink.setVisible(false);
		        }
		    }
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException | java.lang.InterruptedException e) {
		    showException(e);
		} 
	}

	/*Radix::Acs::User2Role:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:afterRead")
	protected published  void afterRead () {
		if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)) {
		    this.getCommand(idof[User2Role:GetAPFamilyList]).execute();
		}
		setupActions();
	}

	/*Radix::Acs::User2Role:General:Model:getRoleTitleProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:getRoleTitleProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleTitleProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		return roleTitle;
	}

	/*Radix::Acs::User2Role:General:Model:replaceRoleCommand-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:replaceRoleCommand")
	public  void replaceRoleCommand (org.radixware.ads.Acs.explorer.User2Role.ReplaceRole command) {
		try {
		    
		    CommandsXsd:ReplaceOrRevokeRoleDocument rec = CommandsXsd:ReplaceOrRevokeRoleDocument.Factory.newInstance();
		    rec.ReplaceOrRevokeRole = CommandsXsd:ReplaceOrRevokeRoleDocument.ReplaceOrRevokeRole.Factory.newInstance();
		    rec.ReplaceOrRevokeRole.Id = this.id.Value;
		    rec.ReplaceOrRevokeRole.Replace = true;
		    rec.ReplaceOrRevokeRole.UseUser2Role = true;

		    // output = 
		    command.send(rec);
		    
		    final Explorer.Models::GroupModel parentGroupModel;
		    if (getEntityContext() instanceof Explorer.Context::InSelectorEditingContext){
		        parentGroupModel = ((Explorer.Context::InSelectorEditingContext)getEntityContext()).getGroupModel();
		    }else{
		        parentGroupModel = ((Explorer.Context::SelectorRowContext)getEntityContext()).parentGroupModel;
		    }
		    if (parentGroupModel!=null && parentGroupModel.getView()!=null){
		        parentGroupModel.getGroupView().reread();
		    }
		    expandInSelector(true);

		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (java.lang.InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Acs::User2Role:General:Model:revokeRoleCommand-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:revokeRoleCommand")
	public  void revokeRoleCommand (org.radixware.ads.Acs.explorer.User2Role.RevokeRole command) {
		try {

		    CommandsXsd:ReplaceOrRevokeRoleDocument rec = CommandsXsd:ReplaceOrRevokeRoleDocument.Factory.newInstance();
		    rec.ReplaceOrRevokeRole = CommandsXsd:ReplaceOrRevokeRoleDocument.ReplaceOrRevokeRole.Factory.newInstance();
		    rec.ReplaceOrRevokeRole.Id = this.id.Value;
		    rec.ReplaceOrRevokeRole.Replace = false;
		    rec.ReplaceOrRevokeRole.UseUser2Role = true;
		    // output = 
		    command.send(rec);

		    final Explorer.Models::GroupModel parentGroupModel;
		    if (getEntityContext() instanceof Explorer.Context::InSelectorEditingContext) {
		        parentGroupModel = ((Explorer.Context::InSelectorEditingContext) getEntityContext()).getGroupModel();
		    } else {
		        parentGroupModel = ((Explorer.Context::SelectorRowContext) getEntityContext()).parentGroupModel;
		    }
		    if (parentGroupModel != null && parentGroupModel.getView() != null) {
		        parentGroupModel.getGroupView().reread();
		    }
		    expandInSelector(false);
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (java.lang.InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Acs::User2Role:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (this.roleId.Value == null){
		     Environment.messageError( "Select Role");
		    return false;
		}
		return super.beforeCreate();
	}

	/*Radix::Acs::User2Role:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		try{
		    Bool usedDualControl = isDualControlUsed.Value;
		    if (usedDualControl!=null && usedDualControl.booleanValue()){       
		        this.editorName.Value = Environment.UserName;
		    }
		}
		catch(Exception ex){
		    showException(ex);
		}
		return super.beforeUpdate();
	}

	/*Radix::Acs::User2Role:General:Model:checkDualControlOptions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:checkDualControlOptions")
	protected published  void checkDualControlOptions (Str currConnectionUser) {
		Bool usedDualControl = false;
		Bool isNew = isNew.Value;
		try {
		    {
		        usedDualControl = isDualControlUsed.Value;
		        boolean usedDualControl_ = usedDualControl != null && usedDualControl.booleanValue();
		    
		        this.acceptorName.setVisible(usedDualControl_);
		        this.editorName.setVisible(usedDualControl_);
		        
		        if (usedDualControl_) {

		            Bool isMayReplaceOrRevokeRoleB = mayReplaceOrRevokeRole.Value;
		            boolean isMayReplaceOrRevokeRole = isMayReplaceOrRevokeRoleB != null && isMayReplaceOrRevokeRoleB.booleanValue();


		            this.getRestrictions().setCopyRestricted(true);
		            if (isNew != null && !isNew.booleanValue()) {
		                
		                
		                
		                this.getRestrictions().setDeleteRestricted(true);
		                if (!this.isNew()) {                    
		                    this.roleTitle.setReadonly(true);
		                    this.userTitle.setReadonly(true);       
		                    enabledFamilies(false);
		                } else {                    
		                    
		                    isNew.setValueObject(true);
		                    this.editorName.setValueObject(currConnectionUser);
		                }
		                this.getCommand(idof[User2Role:replaceRole]).setEnabled(isMayReplaceOrRevokeRole);
		                this.getCommand(idof[User2Role:revokeRole]).setEnabled(isMayReplaceOrRevokeRole);
		            } else {
		                    this.roleTitle.setReadonly(this.roleId.Value==null);

		                    this.getCommand(idof[User2Role:replaceRole]).setEnabled(false);
		                    this.getCommand(idof[User2Role:revokeRole]).setEnabled(false);
		            }
		        } else {
		            this.getCommand(idof[User2Role:replaceRole]).setVisible(false);
		            this.getCommand(idof[User2Role:revokeRole]).setVisible(false);
		        }
		    }
		} catch (Exception ex) {
		    showException(ex);
		}
	}

	/*Radix::Acs::User2Role:General:Model:setupActions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:setupActions")
	public  void setupActions () {

		String currUser = (String) userName.ValueObject;
		String currConnectionUser = Environment.UserName;


		if (currConnectionUser.equals(currUser)) {
		    this.getRestrictions().setDeleteRestricted(true);
		    this.roleTitle.setReadonly(true);
		    this.userTitle.setReadonly(true);
		    enabledFamilies(false);
		    this.getCommand(idof[User2Role:replaceRole]).setEnabled(false);
		    this.getCommand(idof[User2Role:revokeRole]).setEnabled(false);

		} else {
		    checkDualControlOptions(currConnectionUser);

		}
		roleTitle.setCanOpenPropEditorDialog(!roleTitle.isReadonly());
	}

	/*Radix::Acs::User2Role:General:Model:expandInSelector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:expandInSelector")
	public  void expandInSelector (boolean edit) {
		if (isInSelectorRowContext()) {
		    Explorer.Context::SelectorRowContext ctx = (Explorer.Context::SelectorRowContext) getContext();
		    Explorer.Models::Model model = ctx.parentGroupModel;

		    if (model != null) {
		        Client.Views::IView view = model.getView();
		        while (view != null) {

		            if (view instanceof Explorer.Views::SelectorView) {
		                Explorer.Views::SelectorView selector = (Explorer.Views::SelectorView) view;
		                Client.Views::ISelectorWidget widget = selector.getSelectorWidget();
		                if (widget instanceof Explorer.Widgets::SelectorTree) {
		                    Explorer.Widgets::SelectorTree tree = (Explorer.Widgets::SelectorTree) widget;
		                    if (edit && view instanceof AcsSubject2RoleSelectorView) {
		                        ((AcsSubject2RoleSelectorView) view).shouldEditOnInsert = true;
		                    }
		                    tree.expand(tree.currentIndex());
		                }
		                break;
		            }
		            view = view.findParentView();
		        }
		    }
		}
	}

	/*Radix::Acs::User2Role:General:Model:enabledFamilies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:enabledFamilies")
	public published  void enabledFamilies (boolean val) {
		this.apUserGroupMode.setReadonly(!val);
		this.apUserGroupLink.setReadonly(!val);
		this.apUserGroupPartGroupLink.setReadonly(!val);

		this.apDashConfigMode.setReadonly(!val);
		this.apDashConfigLink.setReadonly(!val);
		this.apDashConfigPartGroupLink.setReadonly(!val);
	}
	public final class ReplaceRole extends org.radixware.ads.Acs.explorer.User2Role.ReplaceRole{
		protected ReplaceRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_replaceRole( this );
		}

	}

	public final class RevokeRole extends org.radixware.ads.Acs.explorer.User2Role.RevokeRole{
		protected RevokeRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_revokeRole( this );
		}

	}

	public final class GetAPFamilyList extends org.radixware.ads.Acs.explorer.User2Role.GetAPFamilyList{
		protected GetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_GetAPFamilyList( this );
		}

	}

















}

/* Radix::Acs::User2Role:General:Model - Desktop Meta*/

/*Radix::Acs::User2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem3HIYWUVLLJCLDPWFCQPRHLHLWY"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User2Role:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Acs::User2Role:General:Model - Web Executable*/

/*Radix::Acs::User2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.User2Role.User2Role_DefaultModel implements org.radixware.ads.Acs.common_client.IRoleHolder  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::User2Role:General:Model:roleId-Presentation Property*/




	public class RoleId extends org.radixware.ads.Acs.web.User2Role.colFXSCOJCAZDNBDGMDABQAQH3XQ4{
		public RoleId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleId")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleId")
		public published   void setValue(Str val) {

			internal[roleId]= val;
			User2Role:General:Model.this.getCommand(idof[User2Role:GetAPFamilyList]).execute();
		}
	}
	public RoleId getRoleId(){return (RoleId)getProperty(colFXSCOJCAZDNBDGMDABQAQH3XQ4);}

	/*Radix::Acs::User2Role:General:Model:apUserGroupMode-Presentation Property*/




	public class ApUserGroupMode extends org.radixware.ads.Acs.web.User2Role.col4T2IAH72O5E6RNZPX3MNT44CQE{
		public ApUserGroupMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:apUserGroupMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apUserGroupMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[Acs::UserGroup]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apUserGroupMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apUserGroupMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apUserGroupMode]!=val)
			{
			 apUserGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			 apUserGroupPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value);
			 internal[apUserGroupMode] = val;
			} 
		}
	}
	public ApUserGroupMode getApUserGroupMode(){return (ApUserGroupMode)getProperty(col4T2IAH72O5E6RNZPX3MNT44CQE);}

	/*Radix::Acs::User2Role:General:Model:apDashConfigMode-Presentation Property*/




	public class ApDashConfigMode extends org.radixware.ads.Acs.web.User2Role.colCFPY2QWLVRD7HFRENRKJ32K6CQ{
		public ApDashConfigMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccessAreaMode> getValClass(){
			return org.radixware.kernel.common.enums.EAccessAreaMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccessAreaMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccessAreaMode ? (org.radixware.kernel.common.enums.EAccessAreaMode)x : org.radixware.kernel.common.enums.EAccessAreaMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:apDashConfigMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apDashConfigMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[SystemMonitor::DashConfig]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apDashConfigMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:apDashConfigMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apDashConfigMode]!=val)
			{
			 apDashConfigLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			 apDashConfigPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value); 
			 internal[apDashConfigMode] = val;
			}
		}
	}
	public ApDashConfigMode getApDashConfigMode(){return (ApDashConfigMode)getProperty(colCFPY2QWLVRD7HFRENRKJ32K6CQ);}

	/*Radix::Acs::User2Role:General:Model:restrictedApFamilies-Dynamic Property*/



	protected java.util.Set<org.radixware.kernel.common.types.Id> restrictedApFamilies=new java.util.HashSet<Types::Id>();;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:restrictedApFamilies")
	public final published  java.util.Set<org.radixware.kernel.common.types.Id> getRestrictedApFamilies() {
		return restrictedApFamilies;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:restrictedApFamilies")
	public final published   void setRestrictedApFamilies(java.util.Set<org.radixware.kernel.common.types.Id> val) {
		restrictedApFamilies = val;
	}

	/*Radix::Acs::User2Role:General:Model:areaTitle-Presentation Property*/




	public class AreaTitle extends org.radixware.ads.Acs.web.User2Role.prdUQAPLPPIBRFVLO4VAPILCZSDLM{
		public AreaTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}

		/*Radix::Acs::User2Role:General:Model:areaTitle:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User2Role:General:Model:areaTitle:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:areaTitle:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:areaTitle:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:areaTitle:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:areaTitle:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			if (areaTitle.Value != null && areaTitle.Value.contains(ACCESS_FAMILY_ERR_STATUS.Value)) {
			    return options.changeForegroundColor(Utils::Color.RED);
			} else {
			    return options;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:areaTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:areaTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle(){return (AreaTitle)getProperty(prdUQAPLPPIBRFVLO4VAPILCZSDLM);}

	/*Radix::Acs::User2Role:General:Model:roleDescription-Presentation Property*/




	public class RoleDescription extends org.radixware.ads.Acs.web.User2Role.prdZCORQ4HP5FH6JHKJGNCFKFV5S4{
		public RoleDescription(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Str dummy = ((Str)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription(){return (RoleDescription)getProperty(prdZCORQ4HP5FH6JHKJGNCFKFV5S4);}
















	/*Radix::Acs::User2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::User2Role:General:Model:getRoleIdProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:getRoleIdProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleIdProp (org.radixware.kernel.common.types.Id roleTitlePropId) {
		return roleId;
	}

	/*Radix::Acs::User2Role:General:Model:onCommand_cmdGetAPFamilyList-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:onCommand_cmdGetAPFamilyList")
	public published  void onCommand_cmdGetAPFamilyList (org.radixware.ads.Acs.web.User2Role.GetAPFamilyList command) {
		try {
		    if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)) {
		        if (roleId.getValue() != null) {
		            org.radixware.schemas.adsdef.RoleIDDocument RoleID = org.radixware.schemas.adsdef.RoleIDDocument.Factory.newInstance();
		            RoleID.RoleID = org.radixware.schemas.adsdef.RoleIDDocument.RoleID.Factory.newInstance();
		            RoleID.RoleID.Id = Types::Id.Factory.loadFrom(roleId.getValue());
		            org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies output = command.send(RoleID).APFamilies;
		            if(output.Description != null) {
		                roleDescription.setValue(output.Description);
		            }
		            boolean flag;
		            restrictedApFamilies.clear();
		            restrictedApFamilies.addAll(output.IdList);
		            final boolean showStoredValue = !isNew() && !roleId.isValEdited();
		            
		            flag = output.IdList.contains(idof[Acs::UserGroup]) || (showStoredValue && apUserGroupMode.Value != AccessAreaMode:Unbounded);
		            apUserGroupMode.setVisible(flag);
		            apUserGroupLink.setVisible(flag && apUserGroupMode.Value == AccessAreaMode:BoundedByPart);
		            apUserGroupPartGroupLink.setVisible(flag && apUserGroupMode.Value == AccessAreaMode:BoundedByGroup);

		            flag = output.IdList.contains(idof[SystemMonitor::DashConfig]) || (showStoredValue && apDashConfigMode.Value != AccessAreaMode:Unbounded);
		            apDashConfigMode.setVisible(flag);
		            apDashConfigLink.setVisible(flag && apDashConfigMode.Value == AccessAreaMode:BoundedByPart);
		            apDashConfigPartGroupLink.setVisible(flag && apDashConfigMode.Value == AccessAreaMode:BoundedByGroup);
		                        
		        } else {
		            apUserGroupMode.setVisible(false);            
		            apUserGroupLink.setVisible(false);
		            apUserGroupPartGroupLink.setVisible(false);
		            
		            apDashConfigMode.setVisible(false);
		            apDashConfigLink.setVisible(false);
		            apDashConfigPartGroupLink.setVisible(false);
		        }
		    }
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException | java.lang.InterruptedException e) {
		    showException(e);
		} 
	}

	/*Radix::Acs::User2Role:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:afterRead")
	protected published  void afterRead () {
		if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)) {
		    this.getCommand(idof[User2Role:GetAPFamilyList]).execute();
		}
		setupActions();
	}

	/*Radix::Acs::User2Role:General:Model:getRoleTitleProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:getRoleTitleProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleTitleProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		return roleTitle;
	}

	/*Radix::Acs::User2Role:General:Model:replaceRoleCommand-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:replaceRoleCommand")
	public  void replaceRoleCommand (org.radixware.ads.Acs.web.User2Role.ReplaceRole command) {
		try {
		    
		    CommandsXsd:ReplaceOrRevokeRoleDocument rec = CommandsXsd:ReplaceOrRevokeRoleDocument.Factory.newInstance();
		    rec.ReplaceOrRevokeRole = CommandsXsd:ReplaceOrRevokeRoleDocument.ReplaceOrRevokeRole.Factory.newInstance();
		    rec.ReplaceOrRevokeRole.Id = this.id.Value;
		    rec.ReplaceOrRevokeRole.Replace = true;
		    rec.ReplaceOrRevokeRole.UseUser2Role = true;

		    // output = 
		    command.send(rec);
		    
		    final Explorer.Models::GroupModel parentGroupModel;
		    if (getEntityContext() instanceof Explorer.Context::InSelectorEditingContext){
		        parentGroupModel = ((Explorer.Context::InSelectorEditingContext)getEntityContext()).getGroupModel();
		    }else{
		        parentGroupModel = ((Explorer.Context::SelectorRowContext)getEntityContext()).parentGroupModel;
		    }
		    if (parentGroupModel!=null && parentGroupModel.getView()!=null){
		        parentGroupModel.getGroupView().reread();
		    }
		    expandInSelector(true);

		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (java.lang.InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Acs::User2Role:General:Model:revokeRoleCommand-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:revokeRoleCommand")
	public  void revokeRoleCommand (org.radixware.ads.Acs.web.User2Role.RevokeRole command) {
		try {

		    CommandsXsd:ReplaceOrRevokeRoleDocument rec = CommandsXsd:ReplaceOrRevokeRoleDocument.Factory.newInstance();
		    rec.ReplaceOrRevokeRole = CommandsXsd:ReplaceOrRevokeRoleDocument.ReplaceOrRevokeRole.Factory.newInstance();
		    rec.ReplaceOrRevokeRole.Id = this.id.Value;
		    rec.ReplaceOrRevokeRole.Replace = false;
		    rec.ReplaceOrRevokeRole.UseUser2Role = true;
		    // output = 
		    command.send(rec);

		    final Explorer.Models::GroupModel parentGroupModel;
		    if (getEntityContext() instanceof Explorer.Context::InSelectorEditingContext) {
		        parentGroupModel = ((Explorer.Context::InSelectorEditingContext) getEntityContext()).getGroupModel();
		    } else {
		        parentGroupModel = ((Explorer.Context::SelectorRowContext) getEntityContext()).parentGroupModel;
		    }
		    if (parentGroupModel != null && parentGroupModel.getView() != null) {
		        parentGroupModel.getGroupView().reread();
		    }
		    expandInSelector(false);
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (java.lang.InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::Acs::User2Role:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (this.roleId.Value == null){
		     Environment.messageError( "Select Role");
		    return false;
		}
		return super.beforeCreate();
	}

	/*Radix::Acs::User2Role:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		try{
		    Bool usedDualControl = isDualControlUsed.Value;
		    if (usedDualControl!=null && usedDualControl.booleanValue()){       
		        this.editorName.Value = Environment.UserName;
		    }
		}
		catch(Exception ex){
		    showException(ex);
		}
		return super.beforeUpdate();
	}

	/*Radix::Acs::User2Role:General:Model:checkDualControlOptions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:checkDualControlOptions")
	protected published  void checkDualControlOptions (Str currConnectionUser) {
		Bool usedDualControl = false;
		Bool isNew = isNew.Value;
		try {
		    {
		        usedDualControl = isDualControlUsed.Value;
		        boolean usedDualControl_ = usedDualControl != null && usedDualControl.booleanValue();
		    
		        this.acceptorName.setVisible(usedDualControl_);
		        this.editorName.setVisible(usedDualControl_);
		        
		        if (usedDualControl_) {

		            Bool isMayReplaceOrRevokeRoleB = mayReplaceOrRevokeRole.Value;
		            boolean isMayReplaceOrRevokeRole = isMayReplaceOrRevokeRoleB != null && isMayReplaceOrRevokeRoleB.booleanValue();


		            this.getRestrictions().setCopyRestricted(true);
		            if (isNew != null && !isNew.booleanValue()) {
		                
		                
		                
		                this.getRestrictions().setDeleteRestricted(true);
		                if (!this.isNew()) {                    
		                    this.roleTitle.setReadonly(true);
		                    this.userTitle.setReadonly(true);       
		                    enabledFamilies(false);
		                } else {                    
		                    
		                    isNew.setValueObject(true);
		                    this.editorName.setValueObject(currConnectionUser);
		                }
		                this.getCommand(idof[User2Role:replaceRole]).setEnabled(isMayReplaceOrRevokeRole);
		                this.getCommand(idof[User2Role:revokeRole]).setEnabled(isMayReplaceOrRevokeRole);
		            } else {
		                    this.roleTitle.setReadonly(this.roleId.Value==null);

		                    this.getCommand(idof[User2Role:replaceRole]).setEnabled(false);
		                    this.getCommand(idof[User2Role:revokeRole]).setEnabled(false);
		            }
		        } else {
		            this.getCommand(idof[User2Role:replaceRole]).setVisible(false);
		            this.getCommand(idof[User2Role:revokeRole]).setVisible(false);
		        }
		    }
		} catch (Exception ex) {
		    showException(ex);
		}
	}

	/*Radix::Acs::User2Role:General:Model:setupActions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:setupActions")
	public  void setupActions () {

		String currUser = (String) userName.ValueObject;
		String currConnectionUser = Environment.UserName;


		if (currConnectionUser.equals(currUser)) {
		    this.getRestrictions().setDeleteRestricted(true);
		    this.roleTitle.setReadonly(true);
		    this.userTitle.setReadonly(true);
		    enabledFamilies(false);
		    this.getCommand(idof[User2Role:replaceRole]).setEnabled(false);
		    this.getCommand(idof[User2Role:revokeRole]).setEnabled(false);

		} else {
		    checkDualControlOptions(currConnectionUser);

		}
		roleTitle.setCanOpenPropEditorDialog(!roleTitle.isReadonly());
	}

	/*Radix::Acs::User2Role:General:Model:expandInSelector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:expandInSelector")
	public  void expandInSelector (boolean edit) {
		if (isInSelectorRowContext()) {
		    Explorer.Context::SelectorRowContext ctx = (Explorer.Context::SelectorRowContext) getContext();
		    Explorer.Models::Model model = ctx.parentGroupModel;

		    if (model != null) {
		        Client.Views::IView  view = model.getView();
		        while (view != null) {
		            if (view instanceof Web.Widgets::RwtSelector) {
		                Web.Widgets::RwtSelector selector = (Web.Widgets::RwtSelector) view;
		                Client.Views::ISelectorWidget widget = selector.getSelectorWidget();
		                if (widget instanceof Web.Widgets.SelectorTree::SelectorTree) {
		                    Web.Widgets.SelectorTree::SelectorTree tree = (Web.Widgets.SelectorTree::SelectorTree) widget;
		                    tree.expandNode(tree.SelectedNode);
		                }
		                break;
		            }
		            view = view.findParentView();
		        }
		    }
		}
	}

	/*Radix::Acs::User2Role:General:Model:enabledFamilies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:enabledFamilies")
	public published  void enabledFamilies (boolean val) {
		this.apUserGroupMode.setReadonly(!val);
		this.apUserGroupLink.setReadonly(!val);
		this.apUserGroupPartGroupLink.setReadonly(!val);

		this.apDashConfigMode.setReadonly(!val);
		this.apDashConfigLink.setReadonly(!val);
		this.apDashConfigPartGroupLink.setReadonly(!val);
	}
	public final class ReplaceRole extends org.radixware.ads.Acs.web.User2Role.ReplaceRole{
		protected ReplaceRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_replaceRole( this );
		}

	}

	public final class RevokeRole extends org.radixware.ads.Acs.web.User2Role.RevokeRole{
		protected RevokeRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_revokeRole( this );
		}

	}

	public final class GetAPFamilyList extends org.radixware.ads.Acs.web.User2Role.GetAPFamilyList{
		protected GetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_GetAPFamilyList( this );
		}

	}

















}

/* Radix::Acs::User2Role:General:Model - Web Meta*/

/*Radix::Acs::User2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem3HIYWUVLLJCLDPWFCQPRHLHLWY"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User2Role:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Acs::User2Role:General - Desktop Meta*/

/*Radix::Acs::User2Role:General-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSN5XGOFKNBG7P25HQTYTQFSQA"),
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		280608,
		null,
		16,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVTWGAROM7ZDNRHKKEWRB6NYLJY")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG5N2BNS5JD7FHMLQPDB7HYMH4")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB4UUWF5W55GNFOKNMUL26XSNGU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::User2Role:General - Web Meta*/

/*Radix::Acs::User2Role:General-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl42K4K2TTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSN5XGOFKNBG7P25HQTYTQFSQA"),
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		280608,
		null,
		16,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3HIYWUVLLJCLDPWFCQPRHLHLWY")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFXSCOJCAZDNBDGMDABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXVVFLB43YFCMZAMVSNV5TY7K7E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN3PZN3PNZZFR5MWH2OOQ6AMKQA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVTWGAROM7ZDNRHKKEWRB6NYLJY")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZDABSTFUW5GMTEMXH4CRXJVGMM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG5N2BNS5JD7FHMLQPDB7HYMH4")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUQAPLPPIBRFVLO4VAPILCZSDLM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB4UUWF5W55GNFOKNMUL26XSNGU")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJIWHHJJGCVFU5PAD6MRU5MH44Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNNB4N4LN4ZC4VKV4FJL6OKQ6YU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYZDS2RAS5BTVCUSKEF5DYCAP4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLLSKGT66EBE4XAS7CB3DODAAM4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FF7Q3S6CPOBDHYRABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd2IIUKZLUDBD6DJPS6JPEY4MHCQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUO6NVDSSHBB2FP5ALIEM3I2AEQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCN3XEDKAZDNBDGMDABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDLFSFTKCUZCPLFUHOOIEVJ34XY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCFPY2QWLVRD7HFRENRKJ32K6CQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::User2Role:General:Model - Desktop Executable*/

/*Radix::Acs::User2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.User2Role.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx")
	public class CurrentEntityHandlerEx  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {



		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			try {
			    if (entity instanceof User2Role:General:Model) {
			        ((User2Role:General:Model) entity).setupActions();
			    }
			} catch (java.lang.Exception ex) {
			    showException(ex);
			}
		}

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {

		}


	}

	/*Radix::Acs::User2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::User2Role:General:Model:isReplacement-Presentation Property*/




	public class IsReplacement extends org.radixware.ads.Acs.explorer.User2Role.DefaultGroupModel.pgpIPY24DCHVRDOJPZ2TZIQK7JSNU{
		public IsReplacement(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:isReplacement")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:isReplacement")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpIPY24DCHVRDOJPZ2TZIQK7JSNU);}








	/*Radix::Acs::User2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::User2Role:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandlerEx());

		boolean usDualControl = this.useDualControlOnRoleAssign();

		this.getSelectorColumn(idof[User2Role:acceptorName]).setForbidden(!usDualControl);
		this.getSelectorColumn(idof[User2Role:editorName]).setForbidden(!usDualControl);

	}

	/*Radix::Acs::User2Role:General:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		if (useDualControlOnRoleAssign()) {
		    return new AcsSubject2RoleSelectorView(getEnvironment(), idof[User2Role:General:User2Role], idof[User2Role:hasReplacements]);
		} else
		    return super.createView();
	}

	/*Radix::Acs::User2Role:General:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();

		this.isReplacement.Value = false;
		if(getContext() instanceof Explorer.Context::ChildTableSelectContext){
		    Explorer.Context::ChildTableSelectContext context = (Explorer.Context::ChildTableSelectContext)getContext();
		    if(context.explorerItemDef.Id != idof[User2Role:General:User2Role] && useDualControlOnRoleAssign()){
		        this.isReplacement.Value = true;     
		    }
		}


	}

	/*Radix::Acs::User2Role:General:Model:useDualControlOnRoleAssign-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:useDualControlOnRoleAssign")
	public  boolean useDualControlOnRoleAssign () {
		User ownerUser = (User) findOwnerByClass(User.class);
		if (ownerUser != null) {
		    Bool useDualConrol = ownerUser.usedDualControl.Value;
		    return useDualConrol != null && useDualConrol.booleanValue();
		}
		return false;
	}

	/*Radix::Acs::User2Role:General:Model:removeRow-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:removeRow")
	public published  void removeRow (int row) {
		super.removeRow(row);

		if (getParentModel() instanceof Explorer.Models::EntityModel && getParentModel().getContext() instanceof Explorer.Context::SelectorRowContext) {
		    try {
		        ((Explorer.Models::EntityModel) getParentModel()).read();
		    } catch (InterruptedException e) {
		    } catch (Exceptions::ServiceClientException e) {
		        getEnvironment().processException(e);
		    }

		}
	}


}

/* Radix::Acs::User2Role:General:Model - Desktop Meta*/

/*Radix::Acs::User2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4ANIOJDV5BEANCPBFYUJD6WQBI"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User2Role:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Acs::User2Role:General:Model - Web Executable*/

/*Radix::Acs::User2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.User2Role.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx")
	public class CurrentEntityHandlerEx  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {



		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:Nested classes-Nested Classes*/

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:Properties-Properties*/

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:Methods-Methods*/

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			try {
			    if (entity instanceof User2Role:General:Model) {
			        ((User2Role:General:Model) entity).setupActions();
			    }
			} catch (java.lang.Exception ex) {
			    showException(ex);
			}
		}

		/*Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {

		}


	}

	/*Radix::Acs::User2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::User2Role:General:Model:isReplacement-Presentation Property*/




	public class IsReplacement extends org.radixware.ads.Acs.web.User2Role.DefaultGroupModel.pgpIPY24DCHVRDOJPZ2TZIQK7JSNU{
		public IsReplacement(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:isReplacement")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:isReplacement")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpIPY24DCHVRDOJPZ2TZIQK7JSNU);}








	/*Radix::Acs::User2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::User2Role:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandlerEx());

		boolean usDualControl = this.useDualControlOnRoleAssign();

		this.getSelectorColumn(idof[User2Role:acceptorName]).setForbidden(!usDualControl);
		this.getSelectorColumn(idof[User2Role:editorName]).setForbidden(!usDualControl);

	}

	/*Radix::Acs::User2Role:General:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		if (useDualControlOnRoleAssign()) {
		    return new AcsSubject2RoleSelectorWebView(getEnvironment(), idof[User2Role:General:User2Role], idof[User2Role:hasReplacements]);
		} else
		    return super.createView();
	}

	/*Radix::Acs::User2Role:General:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();

		this.isReplacement.Value = false;
		if(getContext() instanceof Explorer.Context::ChildTableSelectContext){
		    Explorer.Context::ChildTableSelectContext context = (Explorer.Context::ChildTableSelectContext)getContext();
		    if(context.explorerItemDef.Id != idof[User2Role:General:User2Role] && useDualControlOnRoleAssign()){
		        this.isReplacement.Value = true;     
		    }
		}


	}

	/*Radix::Acs::User2Role:General:Model:useDualControlOnRoleAssign-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:useDualControlOnRoleAssign")
	public  boolean useDualControlOnRoleAssign () {
		User ownerUser = (User) findOwnerByClass(User.class);
		if (ownerUser != null) {
		    Bool useDualConrol = ownerUser.usedDualControl.Value;
		    return useDualConrol != null && useDualConrol.booleanValue();
		}
		return false;
	}

	/*Radix::Acs::User2Role:General:Model:removeRow-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User2Role:General:Model:removeRow")
	public published  void removeRow (int row) {
		super.removeRow(row);

		if (getParentModel() instanceof Explorer.Models::EntityModel && getParentModel().getContext() instanceof Explorer.Context::SelectorRowContext) {
		    try {
		        ((Explorer.Models::EntityModel) getParentModel()).read();
		    } catch (InterruptedException e) {
		    } catch (Exceptions::ServiceClientException e) {
		        getEnvironment().processException(e);
		    }

		}
	}


}

/* Radix::Acs::User2Role:General:Model - Web Meta*/

/*Radix::Acs::User2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4ANIOJDV5BEANCPBFYUJD6WQBI"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User2Role:General:Model:Properties-Properties*/
						null,
						null,
						null,
						null,
						null,
						null,
						false,
						false,
						false
						);
}

/* Radix::Acs::User2Role - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User2Role - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка забрать права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls23TWOT6TCZHGLMOTVUZGBRML2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Replace Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заменить роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25WZH4IGDNCJFKCO7QOIMAYKBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<Revoked>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<Удалена>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QW7CYKKYNAKNBG2UODMQS3LAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Administration Group: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Административная группа: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls456APM5FABCKFBI6VLYODJ7Z4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard configuration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конфигурация приборной панели");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GZ7G3NHFJFVVMGT7KZI3SJAKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<Revoked>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<Удалена>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls56TMCIYSHJG3JBJHHNYRWI4KZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls64JV4FE5VNBW5JOKR5KKK6P7RM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Scope");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Область");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QEEGVV6NVBSZLRXT62BL6UOUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to set exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка установить права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls77YQZP7ZHVDBTIQ6YGZ4UU637Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7DH2SS5V4FB5ZDSSONELNGFFG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"isNew");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"isNew");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7K3ICAHRPBFFPHGIAWMHXHTSQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accepted by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцептор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TMGTERPXBA4RNZV3HEJRKFKUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7Y6B43CKV5FTBMRFE6B5IMLAZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard Group: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа приборных панелей: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAS6C63AS4BDRJMW7MS52D23MII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Scope");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Область");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB4UUWF5W55GNFOKNMUL26XSNGU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Bounding mode for \'Dashboard Configuration\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим ограничения для семейства \'Конфигурация приборной панели\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUBJFY5KDZDJZKRGHAZKVRZ5CQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ни одна>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXXXYBQQ2FE7LHM3GSKU3HHY4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приборная панель: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDKSBD6LMLJE6FE3LEHIIRFYOEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group for \'User Group\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов для семейства \'Группа пользователей\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEVRX4VKO7JAXTCIAOC6QZWWHPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group for \'Dashboard Configuration\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов для семейства \'Конфигурация приборной панели\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFL3V5FC2NBCF7ODQVJ4Q4LAHT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revoke Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFT6GR67KSJBARDGQYKQQFRTUE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5TZ7NCGKRD7PBSOIDBPMPXWWQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6RLBLYS7JFK5CAD2G47LERU6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Bounding mode for \'User Group\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим ограничения для семейства \'Группа пользователей\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIBLBWKVBJEPVPXLVCYI53IHSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ни одна>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJSLBWRMP2NGGBHHV33TGQRZ6TY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любая>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJV32EYXJH5EUPOUZBM5OPI4U44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKGQAJ6O56RCILGIRH2SEDPXMJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задана>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKIGKEE3E3RHXNOVLQNVPXDH3FE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLCBJ5RRMNHGHF2WWWVVWCSYYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFUKD4FRTVDNBJXIJXUINFCWIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGSQZRTC2VGPFFUFQON7Y2MDNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка дать права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN4MZAK2MKJGWTK5TXTNLDW4QGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to violate dual control rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка нарушить двойной контроль прав");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNL7VRZOBSVBW5MECMOIIZ43TVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZSLQQQXYFCHNEG32CLKL6DIXQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"replacedId");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"replacedId");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMN6BZYS65EIHFJ7X47HX55ZL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPH4HYYGUXJEBBENYZE2KU6I4PM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRBF4XMWZDBCVJK6IARYKPHMEM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard configuration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конфигурация приборной панели");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREHNEHF73RGBFD5IBZFRB2H5GA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ни одна>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRKV6H3DMZRBTRJST3O4SUVE5HU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSN5XGOFKNBG7P25HQTYTQFSQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to modify exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка изменить права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3IDX3MNMJFBZATVCLAQTGC4PI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAL4EKCO2BH6NDSOWDXIRKT7SU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last edited by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор изменений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQTP77ZOFAX5OH2VAR3Q3PNN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group of Administration Groups: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа административных групп: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVQJA3ZLS3VAHZOKLO7UKESTWOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVTWGAROM7ZDNRHKKEWRB6NYLJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPTY2QQWGBEIPG37PIP2R6G52M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Own rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Собственные права");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXCEWRXONBJHFZJZAG7ZEJ7G32Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<erroneous>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<ошибочно>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYH6KDDVO7RGULAQCJYNFU3C23U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZG5N2BNS5JD7FHMLQPDB7HYMH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(User2Role - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaec42K4K2TTGLNRDHRZABQAQH3XQ4"),"User2Role - Localizing Bundle",$$$items$$$);
}
