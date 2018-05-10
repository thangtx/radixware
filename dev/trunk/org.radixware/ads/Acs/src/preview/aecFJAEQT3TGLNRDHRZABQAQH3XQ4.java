
/* Radix::Acs::UserGroup2Role - Server Executable*/

/*Radix::Acs::UserGroup2Role-Entity Class*/

package org.radixware.ads.Acs.server;

import java.util.HashMap;
import org.radixware.kernel.server.arte.rights.AccessPartition;
 

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role")
public final published class UserGroup2Role  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private String sLastGroupName = null;

	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserGroup2Role_mi.rdxMeta;}

	/*Radix::Acs::UserGroup2Role:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup2Role:Properties-Properties*/

	/*Radix::Acs::UserGroup2Role:hasReplacements-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:hasReplacements")
	public published  Int getHasReplacements() {
		return hasReplacements;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:hasReplacements")
	public published   void setHasReplacements(Int val) {
		hasReplacements = val;
	}

	/*Radix::Acs::UserGroup2Role:acceptorName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:acceptorName")
	public published  Str getAcceptorName() {
		return acceptorName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:acceptorName")
	public published   void setAcceptorName(Str val) {
		acceptorName = val;
	}

	/*Radix::Acs::UserGroup2Role:apUserGroupKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupKey")
	public published  Str getApUserGroupKey() {
		return apUserGroupKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupKey")
	public published   void setApUserGroupKey(Str val) {
		apUserGroupKey = val;
	}

	/*Radix::Acs::UserGroup2Role:apUserGroupLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupLink")
	public published  org.radixware.ads.Acs.server.UserGroup getApUserGroupLink() {
		return apUserGroupLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupLink")
	public published   void setApUserGroupLink(org.radixware.ads.Acs.server.UserGroup val) {
		apUserGroupLink = val;
	}

	/*Radix::Acs::UserGroup2Role:apUserGroupMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupMode")
	public published  org.radixware.kernel.common.enums.EAccessAreaMode getApUserGroupMode() {
		return apUserGroupMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupMode")
	public published   void setApUserGroupMode(org.radixware.kernel.common.enums.EAccessAreaMode val) {
		apUserGroupMode = val;
	}

	/*Radix::Acs::UserGroup2Role:areaTitle-Dynamic Property*/



	protected Str areaTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:areaTitle")
	public published  Str getAreaTitle() {

		return User2Role.getAreaTitle(roleId, apUserGroupMode, apUserGroupLink, apUserGroupPartGroupLink,
		                                      apDashConfigMode, apDashConfigLink, apDashConfigPartGroupLink);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:areaTitle")
	public published   void setAreaTitle(Str val) {
		areaTitle = val;
	}

	/*Radix::Acs::UserGroup2Role:editorName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:editorName")
	public published  Str getEditorName() {
		return editorName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:editorName")
	public published   void setEditorName(Str val) {
		editorName = val;
	}

	/*Radix::Acs::UserGroup2Role:groupName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupName")
	public published  Str getGroupName() {
		return groupName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupName")
	public published   void setGroupName(Str val) {
		groupName = val;
	}

	/*Radix::Acs::UserGroup2Role:groupTitle-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupTitle")
	public published  org.radixware.ads.Acs.server.UserGroup getGroupTitle() {
		return groupTitle;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupTitle")
	public published   void setGroupTitle(org.radixware.ads.Acs.server.UserGroup val) {
		groupTitle = val;
	}

	/*Radix::Acs::UserGroup2Role:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::Acs::UserGroup2Role:isNew-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isNew")
	public published  Bool getIsNew() {
		return isNew;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isNew")
	public published   void setIsNew(Bool val) {
		isNew = val;
	}

	/*Radix::Acs::UserGroup2Role:replacedId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replacedId")
	public published  Int getReplacedId() {
		return replacedId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replacedId")
	public published   void setReplacedId(Int val) {
		replacedId = val;
	}

	/*Radix::Acs::UserGroup2Role:roleId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleId")
	public published  Str getRoleId() {
		return roleId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleId")
	public published   void setRoleId(Str val) {
		roleId = val;
	}

	/*Radix::Acs::UserGroup2Role:roleTitle-Dynamic Property*/



	protected Str roleTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleTitle")
	public published  Str getRoleTitle() {

		if (roleId == null) {
		    if (this.isNewObject()) {
		        return null;
		    }
		    return "<Revoked>";
		}
		return Meta::Utils.getRoleTitle(Types::Id.Factory.loadFrom(roleId));
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleTitle")
	public published   void setRoleTitle(Str val) {
		roleTitle = val;
	}

	/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole-Dynamic Property*/



	protected Bool mayReplaceOrRevokeRole=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole")
	public published  Bool getMayReplaceOrRevokeRole() {

		return getArte().getRights().getDualControlController().mayReplaceOrRevokeUserGroup2Role(this.id.intValue());
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole")
	public published   void setMayReplaceOrRevokeRole(Bool val) {
		mayReplaceOrRevokeRole = val;
	}

	/*Radix::Acs::UserGroup2Role:isDualControlUsed-Dynamic Property*/



	protected Bool isDualControlUsed=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isDualControlUsed")
	public published  Bool getIsDualControlUsed() {

		return getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isDualControlUsed")
	public published   void setIsDualControlUsed(Bool val) {
		isDualControlUsed = val;
	}

	/*Radix::Acs::UserGroup2Role:isReplaced-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isReplaced")
	public  Int getIsReplaced() {
		return isReplaced;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isReplaced")
	public   void setIsReplaced(Int val) {
		isReplaced = val;
	}

	/*Radix::Acs::UserGroup2Role:isRevoked-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isRevoked")
	public  Int getIsRevoked() {
		return isRevoked;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isRevoked")
	public   void setIsRevoked(Int val) {
		isRevoked = val;
	}

	/*Radix::Acs::UserGroup2Role:apDashConfigKey-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigKey")
	public published  Str getApDashConfigKey() {
		return apDashConfigKey;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigKey")
	public published   void setApDashConfigKey(Str val) {
		apDashConfigKey = val;
	}

	/*Radix::Acs::UserGroup2Role:apDashConfigMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigMode")
	public published  org.radixware.kernel.common.enums.EAccessAreaMode getApDashConfigMode() {
		return apDashConfigMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigMode")
	public published   void setApDashConfigMode(org.radixware.kernel.common.enums.EAccessAreaMode val) {
		apDashConfigMode = val;
	}

	/*Radix::Acs::UserGroup2Role:apDashConfigLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigLink")
	public published  org.radixware.ads.SystemMonitor.server.DashConfig getApDashConfigLink() {
		return apDashConfigLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigLink")
	public published   void setApDashConfigLink(org.radixware.ads.SystemMonitor.server.DashConfig val) {
		apDashConfigLink = val;
	}

	/*Radix::Acs::UserGroup2Role:roleDescription-Dynamic Property*/



	protected Str roleDescription=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleDescription")
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
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleDescription")
	public published   void setRoleDescription(Str val) {
		roleDescription = val;
	}

	/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS-Dynamic Property*/



	protected Str ACCESS_FAMILY_ERR_STATUS=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS")
	protected final published  Str getACCESS_FAMILY_ERR_STATUS() {

		return User2Role.getAccessFamilyErrStatus();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS")
	protected final published   void setACCESS_FAMILY_ERR_STATUS(Str val) {
		ACCESS_FAMILY_ERR_STATUS = val;
	}

	/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupId")
	public published  Int getApUserGroupPartGroupId() {
		return apUserGroupPartGroupId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupId")
	public published   void setApUserGroupPartGroupId(Int val) {
		apUserGroupPartGroupId = val;
	}

	/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupId")
	public published  Int getApDashConfigPartGroupId() {
		return apDashConfigPartGroupId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupId")
	public published   void setApDashConfigPartGroupId(Int val) {
		apDashConfigPartGroupId = val;
	}

	/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink")
	public published  org.radixware.ads.Acs.server.PartitionGroup getApUserGroupPartGroupLink() {
		return apUserGroupPartGroupLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink")
	public published   void setApUserGroupPartGroupLink(org.radixware.ads.Acs.server.PartitionGroup val) {
		apUserGroupPartGroupLink = val;
	}

	/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink")
	public published  org.radixware.ads.Acs.server.PartitionGroup getApDashConfigPartGroupLink() {
		return apDashConfigPartGroupLink;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink")
	public published   void setApDashConfigPartGroupLink(org.radixware.ads.Acs.server.PartitionGroup val) {
		apDashConfigPartGroupLink = val;
	}































































































































































































	/*Radix::Acs::UserGroup2Role:Methods-Methods*/

	/*Radix::Acs::UserGroup2Role:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		//HashMap<String, Rights.AccessPartition> accessArea = (getArte().getRights());
		//String sRoleId = ;
		//
		//boolean checkDublication = true;
		//if (getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		//    checkDublication = ! .booleanValue();
		//}
		//
		//if (checkDublication && getArte().getRights().getUserGroupHasRoleForObject(, sRoleId, accessArea, .intValue())){
		//    //System.out.println("after create detected");
		//    throw new ();
		//   }
		getArte().getRights().compileGroupRights(groupName, true);
		return;
	}

	/*Radix::Acs::UserGroup2Role:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:afterDelete")
	protected published  void afterDelete () {
		getArte().getRights().compileGroupRights(sLastGroupName, true);
	}

	/*Radix::Acs::UserGroup2Role:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:afterUpdate")
	protected published  void afterUpdate () {
		getArte().getRights().compileGroupRights(groupName, true);
	}

	/*Radix::Acs::UserGroup2Role:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (getArte().Rights.isCurUserInGroup(groupName)){
		  throw new InvalidEasRequestClientFault("Attempt to give rights to the own group");
		}
		final HashMap<Types::Id, AccessPartition> accessArea = getArea(getArte());
		final String sRoleId = roleId;

		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){
		        throw new InvalidEasRequestClientFault("Attempt to assign rights that are not assigned to you");
		    }
		}

		final java.util.List<Types::Id> roleFamilies = new java.util.ArrayList<>();
		User2Role.fillRoleFamiliesList(roleId, roleFamilies);
		checkRoleFamilies(roleFamilies);


		return true;
	}

	/*Radix::Acs::UserGroup2Role:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:beforeDelete")
	protected published  boolean beforeDelete () {
		if (getArte().Rights.isCurUserInGroup(groupName)){
		  throw new InvalidEasRequestClientFault("Attempt to revoke own rights");
		}
		final HashMap<Types::Id, AccessPartition> accessArea = getArea(getArte());
		final String sRoleId = roleId;

		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)){
		        throw new InvalidEasRequestClientFault("Attempt to revoke rights that you cannot assign");
		    }
		}
		else{    
		    if (!getArte().getRights().getDualControlController().isNewUserGroup2Role(id.intValue())){
		        throw new InvalidEasRequestClientFault("Attempt to violate dual control rights");
		    }    
		}

		sLastGroupName = groupName;
		return true;
	}

	/*Radix::Acs::UserGroup2Role:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:beforeUpdate")
	protected published  boolean beforeUpdate () {
		if (getArte().Rights.isCurUserInGroup(groupName)) {
		    throw new InvalidEasRequestClientFault("Attempt to modify your own rights");
		}
		final HashMap<Types::Id, AccessPartition> accessArea = getArea(getArte());
		final String sRoleId = roleId;

		if (!getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles()){
		    if (!getArte().getRights().curUserHasRightsUserGroup2Role(id.intValue())) {
		        throw new InvalidEasRequestClientFault("Attempt to modify rights that you cannot assign");
		    }

		    if (!getArte().getRights().getUserHasRoleForObject(getArte().getUserName(), sRoleId, accessArea)) {
		        throw new InvalidEasRequestClientFault("Attempt to assign rights that are not assigned to you");
		    }
		}

		final java.util.List<Types::Id> roleFamilies = new java.util.ArrayList<>();
		User2Role.fillRoleFamiliesList(roleId, roleFamilies);
		checkRoleFamilies(roleFamilies);

		return true;
	}

	/*Radix::Acs::UserGroup2Role:onCommand_replaceRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:onCommand_replaceRole")
	public  org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument onCommand_replaceRole (org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return replaceOrRevokeRole(input);
	}

	/*Radix::Acs::UserGroup2Role:onCommand_revokeRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:onCommand_revokeRole")
	public  org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument onCommand_revokeRole (org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return replaceOrRevokeRole(input);
	}

	/*Radix::Acs::UserGroup2Role:replaceOrRevokeRole-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replaceOrRevokeRole")
	public  org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument replaceOrRevokeRole (org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument input) {

		if (!getArte().getRights().getDualControlController().mayReplaceOrRevokeUserGroup2Role(input.ReplaceOrRevokeRole.Id.intValue())){
		     throw new RuntimeException("Unable execute command.");
		}


		CommandsXsd:IntValueDocument rez = CommandsXsd:IntValueDocument.Factory.newInstance();


		 UserGroup2Role srcUserGroup2Role = UserGroup2Role.loadByPK(input.ReplaceOrRevokeRole.Id, true);
		 UserGroup2Role newUserGroup2Role = new UserGroup2Role();
		 newUserGroup2Role.init(null, srcUserGroup2Role);

		 int newId = getArte().getRights().getDualControlController().NextUserGroup2RoleId; 
		 newUserGroup2Role.id = newId;
		 newUserGroup2Role.isNew = true;
		 newUserGroup2Role.create(srcUserGroup2Role);
		 newUserGroup2Role.replacedId = srcUserGroup2Role.id;
		 newUserGroup2Role.editorName = getArte().UserName;
		 newUserGroup2Role.acceptorName = null;
		 
		 if (!input.ReplaceOrRevokeRole.Replace.booleanValue()){
		       newUserGroup2Role.roleId = null;
		 }
		 
		 rez.IntValue1 = newId;
		 
		 return rez;
	}

	/*Radix::Acs::UserGroup2Role:cmdGetAPFamilyList-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:cmdGetAPFamilyList")
	public published  org.radixware.schemas.adsdef.APFamiliesDocument cmdGetAPFamilyList (org.radixware.schemas.adsdef.RoleIDDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		org.radixware.schemas.adsdef.APFamiliesDocument  apFamiliesDocument = org.radixware.schemas.adsdef.APFamiliesDocument.Factory.newInstance();

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

	/*Radix::Acs::UserGroup2Role:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:loadByPK")
	public static published  org.radixware.ads.Acs.server.UserGroup2Role loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),pkValsMap);
		try{
		return (
		org.radixware.ads.Acs.server.UserGroup2Role) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::UserGroup2Role:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:loadByPidStr")
	public static published  org.radixware.ads.Acs.server.UserGroup2Role loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),pidAsStr);
		try{
		return (
		org.radixware.ads.Acs.server.UserGroup2Role) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::UserGroup2Role:getArea-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:getArea")
	protected published  java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.server.arte.rights.AccessPartition> getArea (org.radixware.kernel.server.arte.Arte arte) {
		final HashMap<Types::Id, AccessPartition> accessArea = new HashMap<Types::Id, AccessPartition>();

		accessArea.put(idof[SystemMonitor::DashConfig], 
		              AcsUtils.createAccessPartition(arte, idof[SystemMonitor::DashConfig], apDashConfigMode, apDashConfigKey, apDashConfigPartGroupId));
		              
		accessArea.put(idof[Acs::UserGroup], 
		              AcsUtils.createAccessPartition(arte, idof[Acs::UserGroup], apUserGroupMode, apUserGroupKey, apUserGroupPartGroupId));              

		return accessArea;

	}

	/*Radix::Acs::UserGroup2Role:getAuditPropValTitle-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:getAuditPropValTitle")
	public static  org.radixware.ads.Utils.common.TitledValue getAuditPropValTitle (org.radixware.kernel.common.types.Id propId, java.util.Map<org.radixware.kernel.common.types.Id,org.radixware.ads.Utils.common.TitledValue> propVals) {
		if (idof[UserGroup2Role:roleId] == propId) {
		    Utils::TitledValue ret = new TitledValue();
		    ret.title = "Role";
		    ret.value = Meta::Utils.getRoleTitle(propVals.get(propId).value);
		    return ret;
		} else {
		    return propVals.get(propId);
		}
	}

	/*Radix::Acs::UserGroup2Role:checkRoleFamilies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:checkRoleFamilies")
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





	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdFHB25JLMDJH53HIGXGEQ3GUHXI){
			org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument result = onCommand_replaceRole((org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdR4CZSCYY6FDGZGR2D46RUJTFTI){
			org.radixware.ads.Acs.common.CommandsXsd.IntValueDocument result = onCommand_revokeRole((org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Acs.common.CommandsXsd.ReplaceOrRevokeRoleDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdZZIAPHR2UVHGJGWXBJHLNB5K7M){
			org.radixware.schemas.adsdef.APFamiliesDocument result = cmdGetAPFamilyList((org.radixware.schemas.adsdef.RoleIDDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.adsdef.RoleIDDocument.class),newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Acs::UserGroup2Role - Server Meta*/

/*Radix::Acs::UserGroup2Role-Entity Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup2Role_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),"UserGroup2Role",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Acs::UserGroup2Role:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
							/*Owner Class Name*/
							"UserGroup2Role",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),
							/*Property presentations*/

							/*Radix::Acs::UserGroup2Role:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::UserGroup2Role:hasReplacements:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:acceptorName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apUserGroupKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDWXM6MYALRBC3OYCWTWRGS4DIE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apUserGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCH2ACSH5JCNFIM4AY2JSYFJ6Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apUserGroupMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAULGQS3XVFDL5DE7L3MW46URPQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:areaTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:editorName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:groupTitle:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:isNew:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:replacedId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:roleId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:roleTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:isDualControlUsed:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:isReplaced:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQBZDVK74QJBITNTSSEXXX3A74Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:isRevoked:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5AQUL67EXNCHJJHCS4F6FLR4OA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apDashConfigKey:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apDashConfigMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apDashConfigLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZABU735C45HXPBNS4Z3YERLE5Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2HCHE6TFHFEZVN2LALVY2CK3U4"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:roleDescription:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY2VCR5JSUFFGBHAXLJDMZ3KFTU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPKXDOCYI6FER5AUL5GCC6AXNB4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPNBR6TCPNGUBMULAM34ZPN3CU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSHT4WATZVRFPNEHA7LC6NY6LHI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprU4ITQMDYM5GKJPXE3OKFNOQFGY"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRUWGRI6KBASVAWLH3QWXVCVPU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprTPTX2VY26VCE5MFPXEA3KOE6GQ"),new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::UserGroup2Role:cmdGetAPFamilyList-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZZIAPHR2UVHGJGWXBJHLNB5K7M"),"cmdGetAPFamilyList",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::UserGroup2Role:replaceRole-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFHB25JLMDJH53HIGXGEQ3GUHXI"),"replaceRole",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::UserGroup2Role:revokeRole-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdR4CZSCYY6FDGZGR2D46RUJTFTI"),"revokeRole",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Acs::UserGroup2Role:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									0,

									/*Radix::Acs::UserGroup2Role:General:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colXDHHLFZRY3NBDGMCABQAQH3XQ4"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::Acs::UserGroup2Role:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Acs::UserGroup2Role:General:UserGroup2Role-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiM2TRVD3H3JBN3OLAP3R2S43ZWY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refXOIIB5CPGJANLLW4KK7OF3BC3E"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[] {
										new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"), null, null, null)
									},
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::UserGroup2Role:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),"General",null,16,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"agcFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"pgpSYXXBF7HHVFFZNTZDWYJG3LRNE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = 0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is null</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(280608,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Acs::UserGroup2Role:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colXDHHLFZRY3NBDGMCABQAQH3XQ4"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Acs::UserGroup2Role:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::UserGroup2Role:hasReplacements-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),"hasReplacements",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colFF2PCUS6CPOBDHYRABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> AND ROWNUM&lt;=1)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:acceptorName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),"acceptorName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2CHFOI6OVBXPILKFVHQ2G463Y"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apUserGroupKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDWXM6MYALRBC3OYCWTWRGS4DIE"),"apUserGroupKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MIFOU5ISREB3OFYI6X5B7MILI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apUserGroupLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCH2ACSH5JCNFIM4AY2JSYFJ6Q"),"apUserGroupLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPS35VTNDAFFCHDG6KVQT6XSDHU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refDE3QT6DIU5COXLSAPFPXJQIWBA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apUserGroupMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAULGQS3XVFDL5DE7L3MW46URPQ"),"apUserGroupMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3M7O7CKANBVTAYMOVMAQNCBPA"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:areaTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),"areaTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB2ULGSP5VJEDRN3LXLRUH6T4FY"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:editorName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),"editorName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAGQ753YBNH7RNU7524OYMHZU4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:groupName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXDHHLFZRY3NBDGMCABQAQH3XQ4"),"groupName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXPY4WSO7JGOLMUXS4KB45UWGM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:groupTitle-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),"groupTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDO3O3HXAMBHK5IFXRYZNC2U27I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5QIXNSF2J3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG5AU2K4MXNDHJDTFBCHXRCFTFA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:isNew-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),"isNew",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEWZGVSZJZC6ZCG5GD2JATCIS4"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:replacedId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),"replacedId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFZ5VFV3BRGMFLXGA5SZG4OBMM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:roleId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),"roleId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WNZTWKWJHQRGJ6ZUOQJSEVTM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:roleTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),"roleTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEII4FWV2MFEEHO3GB7PQQIHXWA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),"mayReplaceOrRevokeRole",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:isDualControlUsed-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),"isDualControlUsed",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:isReplaced-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQBZDVK74QJBITNTSSEXXX3A74Y"),"isReplaced",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colFF2PCUS6CPOBDHYRABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colQNEGZPZRY3NBDGMCABQAQH3XQ4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> is not null AND ROWNUM&lt;=1)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:isRevoked-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5AQUL67EXNCHJJHCS4F6FLR4OA"),"isRevoked",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>nvl((select 1 from dual where exists (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"col4D2WF3YC4JAPNHBZJWHR4O53BA\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colFF2PCUS6CPOBDHYRABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colQNEGZPZRY3NBDGMCABQAQH3XQ4\" Owner=\"TABLE\"/></xsc:Item><xsc:Item><xsc:Sql> is null AND ROWNUM&lt;=1)), 0)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apDashConfigKey-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),"apDashConfigKey",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVIHSDFSY5GTJI2MXWWJAW6ADU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apDashConfigMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),"apDashConfigMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQY2RYHV65BFSNJQFHA4B4WQEEI"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apDashConfigLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZABU735C45HXPBNS4Z3YERLE5Y"),"apDashConfigLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKQOBV7ERM5ALBMEYC3GIB7INWU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refN2AFYZ3S55B3ZKIAPLFMJKJDQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVGSKITRX4FHZJAGAQ7V7VGZUJY"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:roleDescription-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"),"roleDescription",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZTQYALYUVF7DNHBKT6MDI53WA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY2VCR5JSUFFGBHAXLJDMZ3KFTU"),"ACCESS_FAMILY_ERR_STATUS",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPKXDOCYI6FER5AUL5GCC6AXNB4"),"apUserGroupPartGroupId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPNBR6TCPNGUBMULAM34ZPN3CU"),"apDashConfigPartGroupId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSHT4WATZVRFPNEHA7LC6NY6LHI"),"apUserGroupPartGroupLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO5GCZDVNMVDEJFN7VINWNCIUSA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2IWGTUOZ25CQLJCD5UD4VWO7Q4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRUWGRI6KBASVAWLH3QWXVCVPU"),"apDashConfigPartGroupLink",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QWOTOJWVFEDBLQNQ6PO4MG3RI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refL45VCCIBKJCIVBMDIIMAMHO5AU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecLNMBHO73GNE2LF5TXFR7QEMICM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::UserGroup2Role:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGEOAN3L5AVCHRARZOGJX2IWAWY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNPD22YH6MJGWLIUGXGXJPISOPU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdFHB25JLMDJH53HIGXGEQ3GUHXI"),"onCommand_replaceRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWRM27O62GJGPXH5GBRVWJNN3JE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprC23USKR4UZDMPEB3UU3A6CDBGU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdR4CZSCYY6FDGZGR2D46RUJTFTI"),"onCommand_revokeRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYVGPPEAG5BFQHIN2VQL2RZAUWA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE6JV7E2U2RE67G5B6LOEAEB6RQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCTHVR5SYUNBENPZFVFMZAYNQ2M"),"replaceOrRevokeRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNZ5TX46QKRHXRMDLMQXNQOOHXA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdZZIAPHR2UVHGJGWXBJHLNB5K7M"),"cmdGetAPFamilyList",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3UDULZGXS5BOXMA42MUVYGC6OM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOTN6SDZPSFCBZLABDJI6TPF6NM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGBCXS76YM5FQDN2YJDZSXC2VA4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYGHFB6FO7VHWPJN6RPZHZYMGYE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS2VKBQV7MRCIPBCWTOY6D4447U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEGGV2XUPERGCHPRXUEL52W642A"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthU4OETV35HFCQJN4DV2PTMDW2XU"),"getArea",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("arte",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHHFH6SUXQRBETDSK5U3GK5PXBA"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRMQK4Q7PBJDF7MTGDCTNZ27S5A"),"getAuditPropValTitle",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEG2XRYN225DS5EQZZVI2HDTU74")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVals",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWOX47VI76VFPDBCUC5IK27MQLU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUAR3IR732BDZ5PRR4ZQDV7X6NM"),"checkRoleFamilies",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("roleFamilies",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZNS7ZEUXKRGTNM4TJG53P3BC6Y"))
								},null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Acs::UserGroup2Role - Desktop Executable*/

/*Radix::Acs::UserGroup2Role-Entity Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role")
public interface UserGroup2Role {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Acs::UserGroup2RoleGroup:isReplacement:isReplacement-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2RoleGroup:isReplacement:isReplacement")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2RoleGroup:isReplacement:isReplacement")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpSYXXBF7HHVFFZNTZDWYJG3LRNE);}







		public org.radixware.ads.Acs.explorer.UserGroup2Role.UserGroup2Role_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.explorer.UserGroup2Role.UserGroup2Role_DefaultModel )  super.getEntity(i);}
	}



































































































































































	/*Radix::Acs::UserGroup2Role:replacedId:replacedId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replacedId:replacedId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replacedId:replacedId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ReplacedId getReplacedId();
	/*Radix::Acs::UserGroup2Role:isRevoked:isRevoked-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isRevoked:isRevoked")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isRevoked:isRevoked")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsRevoked getIsRevoked();
	/*Radix::Acs::UserGroup2Role:editorName:editorName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:editorName:editorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:editorName:editorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EditorName getEditorName();
	/*Radix::Acs::UserGroup2Role:apUserGroupMode:apUserGroupMode-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupMode:apUserGroupMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupMode:apUserGroupMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApUserGroupMode getApUserGroupMode();
	/*Radix::Acs::UserGroup2Role:isNew:isNew-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isNew:isNew")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isNew:isNew")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsNew getIsNew();
	/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:apDashConfigPartGroupId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupId getApDashConfigPartGroupId();
	/*Radix::Acs::UserGroup2Role:apUserGroupLink:apUserGroupLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupLink:apUserGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupLink:apUserGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupLink getApUserGroupLink();
	/*Radix::Acs::UserGroup2Role:apUserGroupKey:apUserGroupKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupKey:apUserGroupKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupKey:apUserGroupKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApUserGroupKey getApUserGroupKey();
	/*Radix::Acs::UserGroup2Role:acceptorName:acceptorName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:acceptorName:acceptorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:acceptorName:acceptorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AcceptorName getAcceptorName();
	/*Radix::Acs::UserGroup2Role:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupLink getApDashConfigPartGroupLink();
	/*Radix::Acs::UserGroup2Role:apDashConfigKey:apDashConfigKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigKey:apDashConfigKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigKey:apDashConfigKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApDashConfigKey getApDashConfigKey();
	/*Radix::Acs::UserGroup2Role:apDashConfigMode:apDashConfigMode-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigMode:apDashConfigMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigMode:apDashConfigMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApDashConfigMode getApDashConfigMode();
	/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:apUserGroupPartGroupId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupId getApUserGroupPartGroupId();
	/*Radix::Acs::UserGroup2Role:isReplaced:isReplaced-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isReplaced:isReplaced")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isReplaced:isReplaced")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsReplaced getIsReplaced();
	/*Radix::Acs::UserGroup2Role:roleId:roleId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleId:roleId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleId:roleId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleId getRoleId();
	/*Radix::Acs::UserGroup2Role:groupTitle:groupTitle-Presentation Property*/


	public class GroupTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public GroupTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupTitle:groupTitle")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupTitle:groupTitle")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupTitle getGroupTitle();
	/*Radix::Acs::UserGroup2Role:hasReplacements:hasReplacements-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:hasReplacements:hasReplacements")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:hasReplacements:hasReplacements")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HasReplacements getHasReplacements();
	/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupLink getApUserGroupPartGroupLink();
	/*Radix::Acs::UserGroup2Role:apDashConfigLink:apDashConfigLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigLink:apDashConfigLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigLink:apDashConfigLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigLink getApDashConfigLink();
	/*Radix::Acs::UserGroup2Role:roleTitle:roleTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleTitle:roleTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleTitle:roleTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleTitle getRoleTitle();
	/*Radix::Acs::UserGroup2Role:roleDescription:roleDescription-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleDescription:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleDescription:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription();
	/*Radix::Acs::UserGroup2Role:isDualControlUsed:isDualControlUsed-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isDualControlUsed:isDualControlUsed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isDualControlUsed:isDualControlUsed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsDualControlUsed getIsDualControlUsed();
	/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ACCESS_FAMILY_ERR_STATUS getACCESS_FAMILY_ERR_STATUS();
	/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public MayReplaceOrRevokeRole getMayReplaceOrRevokeRole();
	/*Radix::Acs::UserGroup2Role:areaTitle:areaTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:areaTitle:areaTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:areaTitle:areaTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle();
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

	public static class CmdGetAPFamilyList extends org.radixware.kernel.common.client.models.items.Command{
		protected CmdGetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.adsdef.APFamiliesDocument send(org.radixware.schemas.adsdef.RoleIDDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.adsdef.APFamiliesDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.adsdef.APFamiliesDocument.class);
		}

	}



}

/* Radix::Acs::UserGroup2Role - Desktop Meta*/

/*Radix::Acs::UserGroup2Role-Entity Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup2Role_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::UserGroup2Role:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::UserGroup2Role",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAHVLSBMRE5H3JE65HW2L6UP6VM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),0,

			/*Radix::Acs::UserGroup2Role:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::UserGroup2Role:hasReplacements:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),
						"hasReplacements",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:hasReplacements:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:acceptorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),
						"acceptorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2CHFOI6OVBXPILKFVHQ2G463Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:acceptorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDWXM6MYALRBC3OYCWTWRGS4DIE"),
						"apUserGroupKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MIFOU5ISREB3OFYI6X5B7MILI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apUserGroupKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCH2ACSH5JCNFIM4AY2JSYFJ6Q"),
						"apUserGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPS35VTNDAFFCHDG6KVQT6XSDHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

					/*Radix::Acs::UserGroup2Role:apUserGroupMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAULGQS3XVFDL5DE7L3MW46URPQ"),
						"apUserGroupMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3M7O7CKANBVTAYMOVMAQNCBPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apUserGroupMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:areaTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),
						"areaTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB2ULGSP5VJEDRN3LXLRUH6T4FY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:areaTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:editorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),
						"editorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAGQ753YBNH7RNU7524OYMHZU4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:editorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:groupTitle:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),
						"groupTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDO3O3HXAMBHK5IFXRYZNC2U27I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
						0,
						0,false),

					/*Radix::Acs::UserGroup2Role:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG5AU2K4MXNDHJDTFBCHXRCFTFA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isNew:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),
						"isNew",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEWZGVSZJZC6ZCG5GD2JATCIS4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::UserGroup2Role:isNew:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:replacedId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),
						"replacedId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFZ5VFV3BRGMFLXGA5SZG4OBMM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:replacedId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:roleId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),
						"roleId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WNZTWKWJHQRGJ6ZUOQJSEVTM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:roleId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:roleTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),
						"roleTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEII4FWV2MFEEHO3GB7PQQIHXWA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:roleTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),
						"mayReplaceOrRevokeRole",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isDualControlUsed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),
						"isDualControlUsed",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:isDualControlUsed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isReplaced:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQBZDVK74QJBITNTSSEXXX3A74Y"),
						"isReplaced",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:isReplaced:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isRevoked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5AQUL67EXNCHJJHCS4F6FLR4OA"),
						"isRevoked",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:isRevoked:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),
						"apDashConfigKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVIHSDFSY5GTJI2MXWWJAW6ADU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apDashConfigKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),
						"apDashConfigMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQY2RYHV65BFSNJQFHA4B4WQEEI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apDashConfigMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZABU735C45HXPBNS4Z3YERLE5Y"),
						"apDashConfigLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKQOBV7ERM5ALBMEYC3GIB7INWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

					/*Radix::Acs::UserGroup2Role:roleDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"),
						"roleDescription",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZTQYALYUVF7DNHBKT6MDI53WA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:roleDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY2VCR5JSUFFGBHAXLJDMZ3KFTU"),
						"ACCESS_FAMILY_ERR_STATUS",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPKXDOCYI6FER5AUL5GCC6AXNB4"),
						"apUserGroupPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPNBR6TCPNGUBMULAM34ZPN3CU"),
						"apDashConfigPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSHT4WATZVRFPNEHA7LC6NY6LHI"),
						"apUserGroupPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO5GCZDVNMVDEJFN7VINWNCIUSA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

					/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRUWGRI6KBASVAWLH3QWXVCVPU"),
						"apDashConfigPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QWOTOJWVFEDBLQNQ6PO4MG3RI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
						133693439,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::UserGroup2Role:cmdGetAPFamilyList-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZZIAPHR2UVHGJGWXBJHLNB5K7M"),
						"cmdGetAPFamilyList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
					/*Radix::Acs::UserGroup2Role:replaceRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFHB25JLMDJH53HIGXGEQ3GUHXI"),
						"replaceRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRABVR6YPEVHBDLOZ2K54J2ICPM"),
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
					/*Radix::Acs::UserGroup2Role:revokeRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdR4CZSCYY6FDGZGR2D46RUJTFTI"),
						"revokeRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJGGHILQ5FAJJFJTUOLHKC2YFI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img43EDIPHW2BACNMLKVPOSGOIRRQ"),
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

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2IWGTUOZ25CQLJCD5UD4VWO7Q4"),"UserGroup2Role=>PartitionGroup (apUserGroupPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPKXDOCYI6FER5AUL5GCC6AXNB4")},new String[]{"apUserGroupPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5QIXNSF2J3NRDAQSABIFNQAAAE"),"UserGroup2Role=>UserGroup (groupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colXDHHLFZRY3NBDGMCABQAQH3XQ4")},new String[]{"groupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refDE3QT6DIU5COXLSAPFPXJQIWBA"),"UserGroup2Role=>UserGroup (apUserGroupKey=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDWXM6MYALRBC3OYCWTWRGS4DIE")},new String[]{"apUserGroupKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refL45VCCIBKJCIVBMDIIMAMHO5AU"),"UserGroup2Role=>PartitionGroup (apDashConfigPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPNBR6TCPNGUBMULAM34ZPN3CU")},new String[]{"apDashConfigPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refN2AFYZ3S55B3ZKIAPLFMJKJDQQ"),"UserGroup2Role=>DashConfig (apDashConfigKey=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E")},new String[]{"apDashConfigKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY")},new String[]{"guid"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refXOIIB5CPGJANLLW4KK7OF3BC3E"),"UserGroup2Role=>UserGroup2Role (replacedId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA")},new String[]{"replacedId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE")},
			false,true,false);
}

/* Radix::Acs::UserGroup2Role - Web Executable*/

/*Radix::Acs::UserGroup2Role-Entity Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role")
public interface UserGroup2Role {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Acs::UserGroup2RoleGroup:isReplacement:isReplacement-Presentation Property*/




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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2RoleGroup:isReplacement:isReplacement")
			public  Bool getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2RoleGroup:isReplacement:isReplacement")
			public   void setValue(Bool val) {
				Value = val;
			}
		}
		public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpSYXXBF7HHVFFZNTZDWYJG3LRNE);}







		public org.radixware.ads.Acs.web.UserGroup2Role.UserGroup2Role_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.web.UserGroup2Role.UserGroup2Role_DefaultModel )  super.getEntity(i);}
	}



































































































































































	/*Radix::Acs::UserGroup2Role:replacedId:replacedId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replacedId:replacedId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:replacedId:replacedId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ReplacedId getReplacedId();
	/*Radix::Acs::UserGroup2Role:isRevoked:isRevoked-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isRevoked:isRevoked")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isRevoked:isRevoked")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsRevoked getIsRevoked();
	/*Radix::Acs::UserGroup2Role:editorName:editorName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:editorName:editorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:editorName:editorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EditorName getEditorName();
	/*Radix::Acs::UserGroup2Role:apUserGroupMode:apUserGroupMode-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupMode:apUserGroupMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupMode:apUserGroupMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApUserGroupMode getApUserGroupMode();
	/*Radix::Acs::UserGroup2Role:isNew:isNew-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isNew:isNew")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isNew:isNew")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsNew getIsNew();
	/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:apDashConfigPartGroupId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:apDashConfigPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupId getApDashConfigPartGroupId();
	/*Radix::Acs::UserGroup2Role:apUserGroupLink:apUserGroupLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupLink:apUserGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupLink:apUserGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupLink getApUserGroupLink();
	/*Radix::Acs::UserGroup2Role:apUserGroupKey:apUserGroupKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupKey:apUserGroupKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupKey:apUserGroupKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApUserGroupKey getApUserGroupKey();
	/*Radix::Acs::UserGroup2Role:acceptorName:acceptorName-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:acceptorName:acceptorName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:acceptorName:acceptorName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AcceptorName getAcceptorName();
	/*Radix::Acs::UserGroup2Role:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:apDashConfigPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigPartGroupLink getApDashConfigPartGroupLink();
	/*Radix::Acs::UserGroup2Role:apDashConfigKey:apDashConfigKey-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigKey:apDashConfigKey")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigKey:apDashConfigKey")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ApDashConfigKey getApDashConfigKey();
	/*Radix::Acs::UserGroup2Role:apDashConfigMode:apDashConfigMode-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigMode:apDashConfigMode")
		public  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigMode:apDashConfigMode")
		public   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {
			Value = val;
		}
	}
	public ApDashConfigMode getApDashConfigMode();
	/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:apUserGroupPartGroupId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:apUserGroupPartGroupId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupId getApUserGroupPartGroupId();
	/*Radix::Acs::UserGroup2Role:isReplaced:isReplaced-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isReplaced:isReplaced")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isReplaced:isReplaced")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public IsReplaced getIsReplaced();
	/*Radix::Acs::UserGroup2Role:roleId:roleId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleId:roleId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleId:roleId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleId getRoleId();
	/*Radix::Acs::UserGroup2Role:groupTitle:groupTitle-Presentation Property*/


	public class GroupTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public GroupTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupTitle:groupTitle")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:groupTitle:groupTitle")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public GroupTitle getGroupTitle();
	/*Radix::Acs::UserGroup2Role:hasReplacements:hasReplacements-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:hasReplacements:hasReplacements")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:hasReplacements:hasReplacements")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HasReplacements getHasReplacements();
	/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:apUserGroupPartGroupLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApUserGroupPartGroupLink getApUserGroupPartGroupLink();
	/*Radix::Acs::UserGroup2Role:apDashConfigLink:apDashConfigLink-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigLink:apDashConfigLink")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:apDashConfigLink:apDashConfigLink")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ApDashConfigLink getApDashConfigLink();
	/*Radix::Acs::UserGroup2Role:roleTitle:roleTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleTitle:roleTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleTitle:roleTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleTitle getRoleTitle();
	/*Radix::Acs::UserGroup2Role:roleDescription:roleDescription-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleDescription:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:roleDescription:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription();
	/*Radix::Acs::UserGroup2Role:isDualControlUsed:isDualControlUsed-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isDualControlUsed:isDualControlUsed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:isDualControlUsed:isDualControlUsed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsDualControlUsed getIsDualControlUsed();
	/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:ACCESS_FAMILY_ERR_STATUS")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ACCESS_FAMILY_ERR_STATUS getACCESS_FAMILY_ERR_STATUS();
	/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:mayReplaceOrRevokeRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public MayReplaceOrRevokeRole getMayReplaceOrRevokeRole();
	/*Radix::Acs::UserGroup2Role:areaTitle:areaTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:areaTitle:areaTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:areaTitle:areaTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle();
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

	public static class CmdGetAPFamilyList extends org.radixware.kernel.common.client.models.items.Command{
		protected CmdGetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.adsdef.APFamiliesDocument send(org.radixware.schemas.adsdef.RoleIDDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.adsdef.APFamiliesDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.adsdef.APFamiliesDocument.class);
		}

	}



}

/* Radix::Acs::UserGroup2Role - Web Meta*/

/*Radix::Acs::UserGroup2Role-Entity Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup2Role_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::UserGroup2Role:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::UserGroup2Role",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAHVLSBMRE5H3JE65HW2L6UP6VM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),0,

			/*Radix::Acs::UserGroup2Role:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::UserGroup2Role:hasReplacements:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),
						"hasReplacements",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:hasReplacements:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:acceptorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),
						"acceptorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2CHFOI6OVBXPILKFVHQ2G463Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:acceptorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDWXM6MYALRBC3OYCWTWRGS4DIE"),
						"apUserGroupKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MIFOU5ISREB3OFYI6X5B7MILI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apUserGroupKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCH2ACSH5JCNFIM4AY2JSYFJ6Q"),
						"apUserGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPS35VTNDAFFCHDG6KVQT6XSDHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

					/*Radix::Acs::UserGroup2Role:apUserGroupMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAULGQS3XVFDL5DE7L3MW46URPQ"),
						"apUserGroupMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3M7O7CKANBVTAYMOVMAQNCBPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apUserGroupMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:areaTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),
						"areaTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB2ULGSP5VJEDRN3LXLRUH6T4FY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:areaTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:editorName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),
						"editorName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAGQ753YBNH7RNU7524OYMHZU4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:editorName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:groupTitle:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),
						"groupTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDO3O3HXAMBHK5IFXRYZNC2U27I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
						0,
						0,false),

					/*Radix::Acs::UserGroup2Role:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG5AU2K4MXNDHJDTFBCHXRCFTFA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isNew:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),
						"isNew",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEWZGVSZJZC6ZCG5GD2JATCIS4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::UserGroup2Role:isNew:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:replacedId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),
						"replacedId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFZ5VFV3BRGMFLXGA5SZG4OBMM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:replacedId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:roleId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),
						"roleId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WNZTWKWJHQRGJ6ZUOQJSEVTM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:roleId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:roleTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),
						"roleTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEII4FWV2MFEEHO3GB7PQQIHXWA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:roleTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),
						"mayReplaceOrRevokeRole",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:mayReplaceOrRevokeRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isDualControlUsed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),
						"isDualControlUsed",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:isDualControlUsed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isReplaced:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQBZDVK74QJBITNTSSEXXX3A74Y"),
						"isReplaced",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:isReplaced:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:isRevoked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5AQUL67EXNCHJJHCS4F6FLR4OA"),
						"isRevoked",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:isRevoked:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigKey:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),
						"apDashConfigKey",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVIHSDFSY5GTJI2MXWWJAW6ADU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apDashConfigKey:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),
						"apDashConfigMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQY2RYHV65BFSNJQFHA4B4WQEEI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apDashConfigMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsX3OOTBYTZTORDIFLABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciIL3ASNQD6ZATJBFLIE4KG26EJ4")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZABU735C45HXPBNS4Z3YERLE5Y"),
						"apDashConfigLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKQOBV7ERM5ALBMEYC3GIB7INWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

					/*Radix::Acs::UserGroup2Role:roleDescription:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"),
						"roleDescription",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZTQYALYUVF7DNHBKT6MDI53WA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:roleDescription:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdY2VCR5JSUFFGBHAXLJDMZ3KFTU"),
						"ACCESS_FAMILY_ERR_STATUS",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:ACCESS_FAMILY_ERR_STATUS:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPKXDOCYI6FER5AUL5GCC6AXNB4"),
						"apUserGroupPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPNBR6TCPNGUBMULAM34ZPN3CU"),
						"apDashConfigPartGroupId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup2Role:apUserGroupPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSHT4WATZVRFPNEHA7LC6NY6LHI"),
						"apUserGroupPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO5GCZDVNMVDEJFN7VINWNCIUSA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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

					/*Radix::Acs::UserGroup2Role:apDashConfigPartGroupLink:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRUWGRI6KBASVAWLH3QWXVCVPU"),
						"apDashConfigPartGroupLink",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QWOTOJWVFEDBLQNQ6PO4MG3RI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
						133693439,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::UserGroup2Role:cmdGetAPFamilyList-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZZIAPHR2UVHGJGWXBJHLNB5K7M"),
						"cmdGetAPFamilyList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
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
					/*Radix::Acs::UserGroup2Role:replaceRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFHB25JLMDJH53HIGXGEQ3GUHXI"),
						"replaceRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRABVR6YPEVHBDLOZ2K54J2ICPM"),
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
					/*Radix::Acs::UserGroup2Role:revokeRole-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdR4CZSCYY6FDGZGR2D46RUJTFTI"),
						"revokeRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJGGHILQ5FAJJFJTUOLHKC2YFI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img43EDIPHW2BACNMLKVPOSGOIRRQ"),
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

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2IWGTUOZ25CQLJCD5UD4VWO7Q4"),"UserGroup2Role=>PartitionGroup (apUserGroupPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colPKXDOCYI6FER5AUL5GCC6AXNB4")},new String[]{"apUserGroupPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5QIXNSF2J3NRDAQSABIFNQAAAE"),"UserGroup2Role=>UserGroup (groupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colXDHHLFZRY3NBDGMCABQAQH3XQ4")},new String[]{"groupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refDE3QT6DIU5COXLSAPFPXJQIWBA"),"UserGroup2Role=>UserGroup (apUserGroupKey=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDWXM6MYALRBC3OYCWTWRGS4DIE")},new String[]{"apUserGroupKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refL45VCCIBKJCIVBMDIIMAMHO5AU"),"UserGroup2Role=>PartitionGroup (apDashConfigPartGroupId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colCPNBR6TCPNGUBMULAM34ZPN3CU")},new String[]{"apDashConfigPartGroupId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAAOGHMLKGBD27BI24WSAXF3OFI")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refN2AFYZ3S55B3ZKIAPLFMJKJDQQ"),"UserGroup2Role=>DashConfig (apDashConfigKey=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVGSKITRX4FHZJAGAQ7V7VGZUJY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E")},new String[]{"apDashConfigKey"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colQU77X4CEHNCMPMBTEGPP2OQ3IY")},new String[]{"guid"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refXOIIB5CPGJANLLW4KK7OF3BC3E"),"UserGroup2Role=>UserGroup2Role (replacedId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA")},new String[]{"replacedId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE")},
			false,true,false);
}

/* Radix::Acs::UserGroup2Role:General - Desktop Meta*/

/*Radix::Acs::UserGroup2Role:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYC2TKXIKZDD3KE4WY2YIIXIJ4"),
	null,

	/*Radix::Acs::UserGroup2Role:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::UserGroup2Role:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgO4RMAVFKOBCK3CIX3BYVSJXSUA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAULGQS3XVFDL5DE7L3MW46URPQ"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCH2ACSH5JCNFIM4AY2JSYFJ6Q"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),1,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZABU735C45HXPBNS4Z3YERLE5Y"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSHT4WATZVRFPNEHA7LC6NY6LHI"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRUWGRI6KBASVAWLH3QWXVCVPU"),0,7,2,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgO4RMAVFKOBCK3CIX3BYVSJXSUA"))}
	,

	/*Radix::Acs::UserGroup2Role:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::UserGroup2Role:General:UserGroup2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiM2TRVD3H3JBN3OLAP3R2S43ZWY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[] {
		new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"), org.radixware.kernel.common.enums.EPropertyVisibility.ONLY_FOR_EXISTENT, null, null, null, null)
	},
	0,0,0,null);
}
/* Radix::Acs::UserGroup2Role:General - Web Meta*/

/*Radix::Acs::UserGroup2Role:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYC2TKXIKZDD3KE4WY2YIIXIJ4"),
	null,

	/*Radix::Acs::UserGroup2Role:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::UserGroup2Role:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgO4RMAVFKOBCK3CIX3BYVSJXSUA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colAULGQS3XVFDL5DE7L3MW46URPQ"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCH2ACSH5JCNFIM4AY2JSYFJ6Q"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),1,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZABU735C45HXPBNS4Z3YERLE5Y"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSHT4WATZVRFPNEHA7LC6NY6LHI"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRUWGRI6KBASVAWLH3QWXVCVPU"),0,7,2,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgO4RMAVFKOBCK3CIX3BYVSJXSUA"))}
	,

	/*Radix::Acs::UserGroup2Role:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::UserGroup2Role:General:UserGroup2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiM2TRVD3H3JBN3OLAP3R2S43ZWY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[] {
		new org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFVF7RRCZVVBBRERP4B77QDTP2U"), org.radixware.kernel.common.enums.EPropertyVisibility.ONLY_FOR_EXISTENT, null, null, null, null)
	},
	0,0,0,null);
}
/* Radix::Acs::UserGroup2Role:General:Model - Desktop Executable*/

/*Radix::Acs::UserGroup2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.UserGroup2Role.UserGroup2Role_DefaultModel implements org.radixware.ads.Acs.common_client.IRoleHolder  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup2Role:General:Model:roleId-Presentation Property*/




	public class RoleId extends org.radixware.ads.Acs.explorer.UserGroup2Role.colQNEGZPZRY3NBDGMCABQAQH3XQ4{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleId")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleId")
		public published   void setValue(Str val) {

			internal[roleId]= val;
			UserGroup2Role:General:Model.this.getCommand(idof[UserGroup2Role:cmdGetAPFamilyList]).execute();
		}
	}
	public RoleId getRoleId(){return (RoleId)getProperty(colQNEGZPZRY3NBDGMCABQAQH3XQ4);}

	/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode-Presentation Property*/




	public class ApUserGroupMode extends org.radixware.ads.Acs.explorer.UserGroup2Role.colAULGQS3XVFDL5DE7L3MW46URPQ{
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

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[Acs::UserGroup]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apUserGroupMode]!=val)
			{
			apUserGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			apUserGroupPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value);
			internal[apUserGroupMode] = val;
			}
		}
	}
	public ApUserGroupMode getApUserGroupMode(){return (ApUserGroupMode)getProperty(colAULGQS3XVFDL5DE7L3MW46URPQ);}

	/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode-Presentation Property*/




	public class ApDashConfigMode extends org.radixware.ads.Acs.explorer.UserGroup2Role.colMDAIC32BGNFNLBDBLN62LS3LUY{
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

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[SystemMonitor::DashConfig]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apDashConfigMode]!=val)
			{
			 apDashConfigLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			 apDashConfigPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value); 
			 internal[apDashConfigMode] = val;
			}
		}
	}
	public ApDashConfigMode getApDashConfigMode(){return (ApDashConfigMode)getProperty(colMDAIC32BGNFNLBDBLN62LS3LUY);}

	/*Radix::Acs::UserGroup2Role:General:Model:areaTitle-Presentation Property*/




	public class AreaTitle extends org.radixware.ads.Acs.explorer.UserGroup2Role.prdZ2FNEE75BNH7JCOEJUYXWZC6N4{
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

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:areaTitle:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			if (areaTitle.Value != null && areaTitle.Value.contains(ACCESS_FAMILY_ERR_STATUS.Value)) {
			    return options.changeForegroundColor(Utils::Color.RED);
			} else {
			    return options;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:areaTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:areaTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle(){return (AreaTitle)getProperty(prdZ2FNEE75BNH7JCOEJUYXWZC6N4);}

	/*Radix::Acs::UserGroup2Role:General:Model:restrictedApFamilies-Dynamic Property*/



	protected java.util.Set<org.radixware.kernel.common.types.Id> restrictedApFamilies=new java.util.HashSet<Types::Id>();;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:restrictedApFamilies")
	public final published  java.util.Set<org.radixware.kernel.common.types.Id> getRestrictedApFamilies() {
		return restrictedApFamilies;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:restrictedApFamilies")
	public final published   void setRestrictedApFamilies(java.util.Set<org.radixware.kernel.common.types.Id> val) {
		restrictedApFamilies = val;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:roleDescription-Presentation Property*/




	public class RoleDescription extends org.radixware.ads.Acs.explorer.UserGroup2Role.prdFVF7RRCZVVBBRERP4B77QDTP2U{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription(){return (RoleDescription)getProperty(prdFVF7RRCZVVBBRERP4B77QDTP2U);}
















	/*Radix::Acs::UserGroup2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup2Role:General:Model:getRoleIdProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:getRoleIdProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleIdProp (org.radixware.kernel.common.types.Id roleTitlePropId) {
		return roleId;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:cmdGetAPFamilyList-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:cmdGetAPFamilyList")
	public published  void cmdGetAPFamilyList (org.radixware.ads.Acs.explorer.UserGroup2Role.CmdGetAPFamilyList command) {
		try {
		    if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)) {
		        if (roleId.getValue() != null) {
		            org.radixware.schemas.adsdef.RoleIDDocument RoleID = org.radixware.schemas.adsdef.RoleIDDocument.Factory.newInstance();
		            RoleID.RoleID = org.radixware.schemas.adsdef.RoleIDDocument.RoleID.Factory.newInstance();
		            RoleID.RoleID.Id = Types::Id.Factory.loadFrom(roleId.getValue());
		            org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies output = command.send(RoleID).APFamilies;
		            if (output.Description != null) {
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
		            apDashConfigPartGroupLink.setVisible(flag && apUserGroupMode.Value == AccessAreaMode:BoundedByGroup);
		            
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

	/*Radix::Acs::UserGroup2Role:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:afterRead")
	protected published  void afterRead () {
		this.getCommand(idof[UserGroup2Role:cmdGetAPFamilyList]).execute();
		setupActions();

	}

	/*Radix::Acs::UserGroup2Role:General:Model:getRoleTitleProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:getRoleTitleProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleTitleProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		return roleTitle;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:onCommand_replaceRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:onCommand_replaceRole")
	public  void onCommand_replaceRole (org.radixware.ads.Acs.explorer.UserGroup2Role.ReplaceRole command) {
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
		} catch (Exception e) {
		    showException(e);
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:onCommand_revokeRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:onCommand_revokeRole")
	public  void onCommand_revokeRole (org.radixware.ads.Acs.explorer.UserGroup2Role.RevokeRole command) {
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
		} catch (Exception e) {
		    showException(e);
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:checkDualControlOptions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:checkDualControlOptions")
	protected published  void checkDualControlOptions () {

		String currConnectionUser = Environment.UserName;

		Bool usedDualControl = false;
		try {
		    Bool isNew = isNew.Value;

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
		                this.groupTitle.setReadonly(true);
		                enabledFamilies(false);
		            } else {
		                isNew.setValueObject(true);
		                this.editorName.setValueObject(currConnectionUser);
		            }
		            this.getCommand(idof[UserGroup2Role:replaceRole]).setEnabled(isMayReplaceOrRevokeRole);
		            this.getCommand(idof[UserGroup2Role:revokeRole]).setEnabled(isMayReplaceOrRevokeRole);
		            
		               
		            
		            // добавил grorbunov 12 05 2016
		            // для во вкладке наследуемых ролей у пользователей
		            // были видны эти две команды
		                boolean isInUserPresentation = false;
		                boolean isInUserGroupPresentation = false;
		                if (isInSelectorRowContext()) {
		                    Explorer.Context::SelectorRowContext ctx = (Explorer.Context::SelectorRowContext) getContext();
		                    Explorer.Models::Model model = ctx.parentGroupModel;

		                    if (model != null) {
		                        Client.Views::IView view = model.getView();


		                        while (view != null && !isInUserPresentation && !isInUserGroupPresentation) {

		                            Types::Id classId = null;
		                            if (view.getModel()!=null && view.getModel().getDefinition()!=null){
		                                classId = view.getModel().getDefinition().getOwnerClassId();
		                            }                        
		                            isInUserPresentation = (idof[User].equals(classId));
		                            isInUserGroupPresentation = (idof[UserGroup].equals(classId));
		                            view = view.findParentView();
		                        }
		                    }
		                }

		                if (isInUserPresentation){
		                    this.getCommand(idof[UserGroup2Role:replaceRole]).setEnabled(false);
		                    this.getCommand(idof[UserGroup2Role:revokeRole]).setVisible(false);
		                }
		            // end добавил grorbunov 12 05 2016
		            
		            
		            
		        } else {
		            this.roleTitle.setReadonly(this.roleId.Value==null);
		            this.getCommand(idof[UserGroup2Role:replaceRole]).setEnabled(false);
		            this.getCommand(idof[UserGroup2Role:revokeRole]).setEnabled(false);
		        }
		    } else {
		        this.getCommand(idof[UserGroup2Role:replaceRole]).setVisible(false);
		        this.getCommand(idof[UserGroup2Role:revokeRole]).setVisible(false);
		    }
		} catch (Exception ex) {
		    showException(ex);
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (this.roleId.Value == null){
		     Environment.messageError( "Select Role");
		    return false;
		}
		return super.beforeCreate();
	}

	/*Radix::Acs::UserGroup2Role:General:Model:setupActions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:setupActions")
	public  void setupActions () {
		checkDualControlOptions();
		roleTitle.setCanOpenPropEditorDialog(!roleTitle.isReadonly());
	}

	/*Radix::Acs::UserGroup2Role:General:Model:expandInSelector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:expandInSelector")
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

	/*Radix::Acs::UserGroup2Role:General:Model:enabledFamilies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:enabledFamilies")
	public published  void enabledFamilies (boolean val) {
		this.apUserGroupMode.setReadonly(!val);
		this.apUserGroupLink.setReadonly(!val);
		this.apDashConfigMode.setReadonly(!val);
		this.apDashConfigLink.setReadonly(!val);
	}
	public final class ReplaceRole extends org.radixware.ads.Acs.explorer.UserGroup2Role.ReplaceRole{
		protected ReplaceRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_replaceRole( this );
		}

	}

	public final class RevokeRole extends org.radixware.ads.Acs.explorer.UserGroup2Role.RevokeRole{
		protected RevokeRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_revokeRole( this );
		}

	}

	public final class CmdGetAPFamilyList extends org.radixware.ads.Acs.explorer.UserGroup2Role.CmdGetAPFamilyList{
		protected CmdGetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_cmdGetAPFamilyList( this );
		}

	}

















}

/* Radix::Acs::UserGroup2Role:General:Model - Desktop Meta*/

/*Radix::Acs::UserGroup2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEQ7UIJIUWJAFVAKCR6ELGWNPWY"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup2Role:General:Model - Web Executable*/

/*Radix::Acs::UserGroup2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.UserGroup2Role.UserGroup2Role_DefaultModel implements org.radixware.ads.Acs.common_client.IRoleHolder  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup2Role:General:Model:roleId-Presentation Property*/




	public class RoleId extends org.radixware.ads.Acs.web.UserGroup2Role.colQNEGZPZRY3NBDGMCABQAQH3XQ4{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleId")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleId")
		public published   void setValue(Str val) {

			internal[roleId]= val;
			UserGroup2Role:General:Model.this.getCommand(idof[UserGroup2Role:cmdGetAPFamilyList]).execute();
		}
	}
	public RoleId getRoleId(){return (RoleId)getProperty(colQNEGZPZRY3NBDGMCABQAQH3XQ4);}

	/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode-Presentation Property*/




	public class ApUserGroupMode extends org.radixware.ads.Acs.web.UserGroup2Role.colAULGQS3XVFDL5DE7L3MW46URPQ{
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

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[Acs::UserGroup]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apUserGroupMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apUserGroupMode]!=val)
			{
			apUserGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			apUserGroupPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value);
			internal[apUserGroupMode] = val;
			}
		}
	}
	public ApUserGroupMode getApUserGroupMode(){return (ApUserGroupMode)getProperty(colAULGQS3XVFDL5DE7L3MW46URPQ);}

	/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode-Presentation Property*/




	public class ApDashConfigMode extends org.radixware.ads.Acs.web.UserGroup2Role.colMDAIC32BGNFNLBDBLN62LS3LUY{
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

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			return AcsClientCommonUtils.validateFamily(roleTitle.Value, this.Value, this.getTitle(), restrictedApFamilies, idof[SystemMonitor::DashConfig]);
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode")
		public published  org.radixware.kernel.common.enums.EAccessAreaMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:apDashConfigMode")
		public published   void setValue(org.radixware.kernel.common.enums.EAccessAreaMode val) {

			if (internal[apDashConfigMode]!=val)
			{
			 apDashConfigLink.setVisible(val.Value==AccessAreaMode:BoundedByPart.Value);
			 apDashConfigPartGroupLink.setVisible(val.Value==AccessAreaMode:BoundedByGroup.Value); 
			 internal[apDashConfigMode] = val;
			}
		}
	}
	public ApDashConfigMode getApDashConfigMode(){return (ApDashConfigMode)getProperty(colMDAIC32BGNFNLBDBLN62LS3LUY);}

	/*Radix::Acs::UserGroup2Role:General:Model:areaTitle-Presentation Property*/




	public class AreaTitle extends org.radixware.ads.Acs.web.UserGroup2Role.prdZ2FNEE75BNH7JCOEJUYXWZC6N4{
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

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:areaTitle:getTextOptions-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:areaTitle:getTextOptions")
		protected published  org.radixware.kernel.common.client.text.ITextOptions getTextOptions (java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> markers, org.radixware.kernel.common.client.text.ITextOptions options) {
			if (areaTitle.Value != null && areaTitle.Value.contains(ACCESS_FAMILY_ERR_STATUS.Value)) {
			    return options.changeForegroundColor(Utils::Color.RED);
			} else {
			    return options;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:areaTitle")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:areaTitle")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public AreaTitle getAreaTitle(){return (AreaTitle)getProperty(prdZ2FNEE75BNH7JCOEJUYXWZC6N4);}

	/*Radix::Acs::UserGroup2Role:General:Model:restrictedApFamilies-Dynamic Property*/



	protected java.util.Set<org.radixware.kernel.common.types.Id> restrictedApFamilies=new java.util.HashSet<Types::Id>();;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:restrictedApFamilies")
	public final published  java.util.Set<org.radixware.kernel.common.types.Id> getRestrictedApFamilies() {
		return restrictedApFamilies;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:restrictedApFamilies")
	public final published   void setRestrictedApFamilies(java.util.Set<org.radixware.kernel.common.types.Id> val) {
		restrictedApFamilies = val;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:roleDescription-Presentation Property*/




	public class RoleDescription extends org.radixware.ads.Acs.web.UserGroup2Role.prdFVF7RRCZVVBBRERP4B77QDTP2U{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleDescription")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:roleDescription")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RoleDescription getRoleDescription(){return (RoleDescription)getProperty(prdFVF7RRCZVVBBRERP4B77QDTP2U);}
















	/*Radix::Acs::UserGroup2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup2Role:General:Model:getRoleIdProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:getRoleIdProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleIdProp (org.radixware.kernel.common.types.Id roleTitlePropId) {
		return roleId;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:cmdGetAPFamilyList-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:cmdGetAPFamilyList")
	public published  void cmdGetAPFamilyList (org.radixware.ads.Acs.web.UserGroup2Role.CmdGetAPFamilyList command) {
		try {
		    if (!(getContext() instanceof org.radixware.kernel.common.client.models.IContext.SelectorRow)) {
		        if (roleId.getValue() != null) {
		            org.radixware.schemas.adsdef.RoleIDDocument RoleID = org.radixware.schemas.adsdef.RoleIDDocument.Factory.newInstance();
		            RoleID.RoleID = org.radixware.schemas.adsdef.RoleIDDocument.RoleID.Factory.newInstance();
		            RoleID.RoleID.Id = Types::Id.Factory.loadFrom(roleId.getValue());
		            org.radixware.schemas.adsdef.APFamiliesDocument.APFamilies output = command.send(RoleID).APFamilies;
		            if (output.Description != null) {
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
		            apDashConfigPartGroupLink.setVisible(flag && apUserGroupMode.Value == AccessAreaMode:BoundedByGroup);
		            
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

	/*Radix::Acs::UserGroup2Role:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:afterRead")
	protected published  void afterRead () {
		this.getCommand(idof[UserGroup2Role:cmdGetAPFamilyList]).execute();
		setupActions();

	}

	/*Radix::Acs::UserGroup2Role:General:Model:getRoleTitleProp-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:getRoleTitleProp")
	public published  org.radixware.kernel.common.client.models.items.properties.PropertyStr getRoleTitleProp (org.radixware.kernel.common.types.Id editedRolePropId) {
		return roleTitle;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:onCommand_replaceRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:onCommand_replaceRole")
	public  void onCommand_replaceRole (org.radixware.ads.Acs.web.UserGroup2Role.ReplaceRole command) {
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
		} catch (Exception e) {
		    showException(e);
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:onCommand_revokeRole-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:onCommand_revokeRole")
	public  void onCommand_revokeRole (org.radixware.ads.Acs.web.UserGroup2Role.RevokeRole command) {
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
		} catch (Exception e) {
		    showException(e);
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:checkDualControlOptions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:checkDualControlOptions")
	protected published  void checkDualControlOptions () {

		String currConnectionUser = Environment.UserName;

		Bool usedDualControl = false;
		try {
		    Bool isNew = isNew.Value;

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
		                this.groupTitle.setReadonly(true);
		                enabledFamilies(false);
		            } else {
		                isNew.setValueObject(true);
		                this.editorName.setValueObject(currConnectionUser);
		            }
		            this.getCommand(idof[UserGroup2Role:replaceRole]).setEnabled(isMayReplaceOrRevokeRole);
		            this.getCommand(idof[UserGroup2Role:revokeRole]).setEnabled(isMayReplaceOrRevokeRole);
		            
		               
		            
		            // добавил grorbunov 12 05 2016
		            // для во вкладке наследуемых ролей у пользователей
		            // были видны эти две команды
		                boolean isInUserPresentation = false;
		                boolean isInUserGroupPresentation = false;
		                if (isInSelectorRowContext()) {
		                    Explorer.Context::SelectorRowContext ctx = (Explorer.Context::SelectorRowContext) getContext();
		                    Explorer.Models::Model model = ctx.parentGroupModel;

		                    if (model != null) {
		                        Client.Views::IView view = model.getView();


		                        while (view != null && !isInUserPresentation && !isInUserGroupPresentation) {

		                            Types::Id classId = null;
		                            if (view.getModel()!=null && view.getModel().getDefinition()!=null){
		                                classId = view.getModel().getDefinition().getOwnerClassId();
		                            }                        
		                            isInUserPresentation = (idof[User].equals(classId));
		                            isInUserGroupPresentation = (idof[UserGroup].equals(classId));
		                            view = view.findParentView();
		                        }
		                    }
		                }

		                if (isInUserPresentation){
		                    this.getCommand(idof[UserGroup2Role:replaceRole]).setEnabled(false);
		                    this.getCommand(idof[UserGroup2Role:revokeRole]).setVisible(false);
		                }
		            // end добавил grorbunov 12 05 2016
		            
		            
		            
		        } else {
		            this.roleTitle.setReadonly(this.roleId.Value==null);
		            this.getCommand(idof[UserGroup2Role:replaceRole]).setEnabled(false);
		            this.getCommand(idof[UserGroup2Role:revokeRole]).setEnabled(false);
		        }
		    } else {
		        this.getCommand(idof[UserGroup2Role:replaceRole]).setVisible(false);
		        this.getCommand(idof[UserGroup2Role:revokeRole]).setVisible(false);
		    }
		} catch (Exception ex) {
		    showException(ex);
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (this.roleId.Value == null){
		     Environment.messageError( "Select Role");
		    return false;
		}
		return super.beforeCreate();
	}

	/*Radix::Acs::UserGroup2Role:General:Model:setupActions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:setupActions")
	public  void setupActions () {
		checkDualControlOptions();
		roleTitle.setCanOpenPropEditorDialog(!roleTitle.isReadonly());
	}

	/*Radix::Acs::UserGroup2Role:General:Model:expandInSelector-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:expandInSelector")
	public  void expandInSelector (boolean edit) {
		if (isInSelectorRowContext()) {
		    Explorer.Context::SelectorRowContext ctx = (Explorer.Context::SelectorRowContext) getContext();
		    Explorer.Models::Model model = ctx.parentGroupModel;

		    if (model != null) {
		        Client.Views::IView view = model.getView();
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

	/*Radix::Acs::UserGroup2Role:General:Model:enabledFamilies-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:enabledFamilies")
	public published  void enabledFamilies (boolean val) {
		this.apUserGroupMode.setReadonly(!val);
		this.apUserGroupLink.setReadonly(!val);
		this.apDashConfigMode.setReadonly(!val);
		this.apDashConfigLink.setReadonly(!val);
	}
	public final class ReplaceRole extends org.radixware.ads.Acs.web.UserGroup2Role.ReplaceRole{
		protected ReplaceRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_replaceRole( this );
		}

	}

	public final class RevokeRole extends org.radixware.ads.Acs.web.UserGroup2Role.RevokeRole{
		protected RevokeRole(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_revokeRole( this );
		}

	}

	public final class CmdGetAPFamilyList extends org.radixware.ads.Acs.web.UserGroup2Role.CmdGetAPFamilyList{
		protected CmdGetAPFamilyList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_cmdGetAPFamilyList( this );
		}

	}

















}

/* Radix::Acs::UserGroup2Role:General:Model - Web Meta*/

/*Radix::Acs::UserGroup2Role:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemEQ7UIJIUWJAFVAKCR6ELGWNPWY"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup2Role:General - Desktop Meta*/

/*Radix::Acs::UserGroup2Role:General-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJI2Z6LSNWNDX7MRMTEG6I2SCTY"),
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
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOY6MHGLGWZEHPOCAT56G45PBKQ")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETL3RCX6OFH67DMD6YX2PUQTBQ")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTSWPU3UONGDTDXLGXISAALZ7M")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::UserGroup2Role:General - Web Meta*/

/*Radix::Acs::UserGroup2Role:General-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblFJAEQT3TGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJI2Z6LSNWNDX7MRMTEG6I2SCTY"),
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
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprEQ7UIJIUWJAFVAKCR6ELGWNPWY")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQNEGZPZRY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRTXDSNJ5CVF53GPE57HAMUULMM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRONW5IE6CJCNDEY4FPIKXYTTFE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOY6MHGLGWZEHPOCAT56G45PBKQ")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4BJ4IJWWXVB43KTVHV4QOTOHCQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETL3RCX6OFH67DMD6YX2PUQTBQ")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZ2FNEE75BNH7JCOEJUYXWZC6N4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTSWPU3UONGDTDXLGXISAALZ7M")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBIFNJQZ5NGNFMJ4LBNAWMOV44"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5FWFVTUD4VCDXJSLFFBZU2VLGE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFF2PCUS6CPOBDHYRABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC3327E6VZNB3RBSQSYPLWSO5KI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4D2WF3YC4JAPNHBZJWHR4O53BA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdYQRXRSBZYRG6PEZYIA5Z3YRNFI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdX646GGMYIVE4JKPNWM3LFYJDJQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGSIQQZWQVJCPBMVNAD6ECFBO6E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMDAIC32BGNFNLBDBLN62LS3LUY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::UserGroup2Role:General:Model - Desktop Executable*/

/*Radix::Acs::UserGroup2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.UserGroup2Role.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx")
	public class CurrentEntityHandlerEx  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {



		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			try {
			    if (entity instanceof UserGroup2Role:General:Model) {
			        ((UserGroup2Role:General:Model) entity).setupActions();
			    }
			} catch (java.lang.Exception ex) {
			    showException(ex);
			}
		}

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {

		}


	}

	/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup2Role:General:Model:isReplacement-Presentation Property*/




	public class IsReplacement extends org.radixware.ads.Acs.explorer.UserGroup2Role.DefaultGroupModel.pgpSYXXBF7HHVFFZNTZDWYJG3LRNE{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:isReplacement")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:isReplacement")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpSYXXBF7HHVFFZNTZDWYJG3LRNE);}








	/*Radix::Acs::UserGroup2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup2Role:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandlerEx());

		boolean usDualControl = this.useDualControlOnRoleAssign();

		this.getSelectorColumn(idof[UserGroup2Role:acceptorName]).setForbidden(!usDualControl);
		this.getSelectorColumn(idof[UserGroup2Role:editorName]).setForbidden(!usDualControl);
	}

	/*Radix::Acs::UserGroup2Role:General:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		if (useDualControlOnRoleAssign()) {
		    return new AcsSubject2RoleSelectorView(getEnvironment(),idof[UserGroup2Role:General:UserGroup2Role] , idof[UserGroup2Role:hasReplacements]);
		} else
		    return super.createView();
	}

	/*Radix::Acs::UserGroup2Role:General:Model:useDualControlOnRoleAssign-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:useDualControlOnRoleAssign")
	public  boolean useDualControlOnRoleAssign () {
		UserGroup ownerUserGroup = (UserGroup) findOwnerByClass(UserGroup.class);
		if (ownerUserGroup != null) {
		    Bool useDualConrol = ownerUserGroup.useDualControlOnRoleAssign.Value;
		    return useDualConrol != null && useDualConrol.booleanValue();
		}
		return false;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();

		this.isReplacement.Value = false;
		if(getContext() instanceof Explorer.Context::ChildTableSelectContext){
		    Explorer.Context::ChildTableSelectContext context = (Explorer.Context::ChildTableSelectContext)getContext();
		    if(context.explorerItemDef.Id != idof[UserGroup2Role:General:UserGroup2Role] && useDualControlOnRoleAssign()){
		        this.isReplacement.Value = true;     
		    }
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:removeRow-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:removeRow")
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

/* Radix::Acs::UserGroup2Role:General:Model - Desktop Meta*/

/*Radix::Acs::UserGroup2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4M72LWZY6BEFDBSMYOB27SEVBE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup2Role:General:Model - Web Executable*/

/*Radix::Acs::UserGroup2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.UserGroup2Role.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup2Role:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx")
	public class CurrentEntityHandlerEx  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {



		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:Properties-Properties*/

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:Methods-Methods*/

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			try {
			    if (entity instanceof UserGroup2Role:General:Model) {
			        ((UserGroup2Role:General:Model) entity).setupActions();
			    }
			} catch (java.lang.Exception ex) {
			    showException(ex);
			}
		}

		/*Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:CurrentEntityHandlerEx:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {

		}


	}

	/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup2Role:General:Model:isReplacement-Presentation Property*/




	public class IsReplacement extends org.radixware.ads.Acs.web.UserGroup2Role.DefaultGroupModel.pgpSYXXBF7HHVFFZNTZDWYJG3LRNE{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:isReplacement")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:isReplacement")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsReplacement getIsReplacement(){return (IsReplacement)getProperty(pgpSYXXBF7HHVFFZNTZDWYJG3LRNE);}








	/*Radix::Acs::UserGroup2Role:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup2Role:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandlerEx());

		boolean usDualControl = this.useDualControlOnRoleAssign();

		this.getSelectorColumn(idof[UserGroup2Role:acceptorName]).setForbidden(!usDualControl);
		this.getSelectorColumn(idof[UserGroup2Role:editorName]).setForbidden(!usDualControl);
	}

	/*Radix::Acs::UserGroup2Role:General:Model:createView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:createView")
	public published  org.radixware.kernel.common.client.views.IView createView () {
		if (useDualControlOnRoleAssign()) {
		    return new AcsSubject2RoleSelectorWebView(getEnvironment(),idof[UserGroup2Role:General:UserGroup2Role] , idof[UserGroup2Role:hasReplacements]);
		} else
		    return super.createView();
	}

	/*Radix::Acs::UserGroup2Role:General:Model:useDualControlOnRoleAssign-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:useDualControlOnRoleAssign")
	public  boolean useDualControlOnRoleAssign () {
		UserGroup ownerUserGroup = (UserGroup) findOwnerByClass(UserGroup.class);
		if (ownerUserGroup != null) {
		    Bool useDualConrol = ownerUserGroup.useDualControlOnRoleAssign.Value;
		    return useDualConrol != null && useDualConrol.booleanValue();
		}
		return false;
	}

	/*Radix::Acs::UserGroup2Role:General:Model:beforeReadFirstPage-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:beforeReadFirstPage")
	protected published  void beforeReadFirstPage () {
		super.beforeReadFirstPage();

		this.isReplacement.Value = false;
		if(getContext() instanceof Explorer.Context::ChildTableSelectContext){
		    Explorer.Context::ChildTableSelectContext context = (Explorer.Context::ChildTableSelectContext)getContext();
		    if(context.explorerItemDef.Id != idof[UserGroup2Role:General:UserGroup2Role] && useDualControlOnRoleAssign()){
		        this.isReplacement.Value = true;     
		    }
		}
	}

	/*Radix::Acs::UserGroup2Role:General:Model:removeRow-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup2Role:General:Model:removeRow")
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

/* Radix::Acs::UserGroup2Role:General:Model - Web Meta*/

/*Radix::Acs::UserGroup2Role:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm4M72LWZY6BEFDBSMYOB27SEVBE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup2Role:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup2Role - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup2Role - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3HGGFM6S3BHTZP3NSXKUTAZN5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group for \'Dashboard Configuration\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов для семейства \'Конфигурация приборной панели\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3QWOTOJWVFEDBLQNQ6PO4MG3RI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls43WNZTWKWJHQRGJ6ZUOQJSEVTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль группы пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4J6PWQ44TZG3VCJGRIQIT2KN4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MIFOU5ISREB3OFYI6X5B7MILI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to modify rights that you cannot assign");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка изменить права, которые вы не можете назначить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6KEISLUOJVFE7FENOSNIIDZ7DQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke own rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка удалить собственные права");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7656GPKCTRFLDPAFG3PKINOBIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign rights that are not assigned to you");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка назначить права, которыми вы не обладаете");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7Q6VII2RTNCUTEMOZOFDA3HNEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли группы пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAHVLSBMRE5H3JE65HW2L6UP6VM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Scope");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Область");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB2ULGSP5VJEDRN3LXLRUH6T4FY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last edited by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор изменений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBAGQ753YBNH7RNU7524OYMHZU4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Rights duplication");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Дублирование прав");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBJ7V3KLSBRFSLFPZJWG6R4JWKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBPMLKTTUU5EBLB4W7T54PDVLGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXPY4WSO7JGOLMUXS4KB45UWGM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to violate dual control rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка нарушить двойной контроль прав");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC7J36ZO2G5A7THRIXENUWYKC6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDO3O3HXAMBHK5IFXRYZNC2U27I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEII4FWV2MFEEHO3GB7PQQIHXWA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsETL3RCX6OFH67DMD6YX2PUQTBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsG5AU2K4MXNDHJDTFBCHXRCFTFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revoke Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJGGHILQ5FAJJFJTUOLHKC2YFI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роль группы пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYC2TKXIKZDD3KE4WY2YIIXIJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Bounding mode for \'User Group\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим ограничения для семейства \'Группа пользователей\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3M7O7CKANBVTAYMOVMAQNCBPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Scope");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Область");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTSWPU3UONGDTDXLGXISAALZ7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<Revoked>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<Удалена>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIRD7Q4M57RFG7GQNEZJKC27EDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли группы пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJI2Z6LSNWNDX7MRMTEG6I2SCTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard configuration");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конфигурация приборной панели");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKQOBV7ERM5ALBMEYC3GIB7INWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign rights that are not assigned to you");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка назначить права, которыми вы не обладаете");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKW6H3HZ3CJHBDKJP6KNPGFHJYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accepted by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцептор");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2CHFOI6OVBXPILKFVHQ2G463Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"isNew");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"isNew");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLEWZGVSZJZC6ZCG5GD2JATCIS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Role description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZTQYALYUVF7DNHBKT6MDI53WA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<Revoked>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<Удалена>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNJ2UJW3H5BFHTI5C7IMDTVY5VU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Partition group for \'User Group\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа разделов для семейства \'Группа пользователей\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO5GCZDVNMVDEJFN7VINWNCIUSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dashboard Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки приборной панели");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOVIHSDFSY5GTJI2MXWWJAW6ADU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOY6MHGLGWZEHPOCAT56G45PBKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPS35VTNDAFFCHDG6KVQT6XSDHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"replacedId");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"replacedId");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFZ5VFV3BRGMFLXGA5SZG4OBMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Bounding mode for \'Dashboard Configuration\' family");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим ограничения для семейства \'Конфигурация приборной панели\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQY2RYHV65BFSNJQFHA4B4WQEEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Replace Role");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заменить роль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRABVR6YPEVHBDLOZ2K54J2ICPM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to modify your own rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка изменить собственные права");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV2DWYMJYUVGG7OWCHX74TREJ6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke rights that you cannot assign");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка забрать права, которые вы не сможете назначить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW23JVQTTZRELRKDFYQV5XMI6M4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to give rights to the own group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка дать права собственной группе");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXG6Q6SUZMZFFFNXKED2LFAR3OU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserGroup2Role - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecFJAEQT3TGLNRDHRZABQAQH3XQ4"),"UserGroup2Role - Localizing Bundle",$$$items$$$);
}
