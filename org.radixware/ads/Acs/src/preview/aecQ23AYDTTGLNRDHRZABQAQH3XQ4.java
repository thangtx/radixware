
/* Radix::Acs::UserGroup - Server Executable*/

/*Radix::Acs::UserGroup-Entity Class*/

package org.radixware.ads.Acs.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup")
public final published class UserGroup  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserGroup_mi.rdxMeta;}

	/*Radix::Acs::UserGroup:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup:Properties-Properties*/

	/*Radix::Acs::UserGroup:useDualControlOnRoleAssign-Dynamic Property*/



	protected Bool useDualControlOnRoleAssign=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:useDualControlOnRoleAssign")
	public published  Bool getUseDualControlOnRoleAssign() {

		return getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:useDualControlOnRoleAssign")
	public published   void setUseDualControlOnRoleAssign(Bool val) {
		useDualControlOnRoleAssign = val;
	}

	/*Radix::Acs::UserGroup:description-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:description")
	public published  Str getDescription() {
		return description;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:description")
	public published   void setDescription(Str val) {
		description = val;
	}

	/*Radix::Acs::UserGroup:name-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:name")
	public published  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:name")
	public published   void setName(Str val) {
		name = val;
	}

	/*Radix::Acs::UserGroup:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Acs::UserGroup:hasNewRoles-Dynamic Property*/



	protected Bool hasNewRoles=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:hasNewRoles")
	public  Bool getHasNewRoles() {

		{
		GetUserGroupHasNewRoles rs = GetUserGroupHasNewRoles.execute(this.name);
		Int result = rs.hasNewRoles;
		 if (result != null && result.intValue() > 0){
		     return true;
		 }
		}

		GetUserGroupHasNewUsers rs = GetUserGroupHasNewUsers.execute(this.name);
		Int result = rs.hasNewUsers;
		 if (result != null && result.intValue() > 0){
		     return true;
		 }
		return false; 
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:hasNewRoles")
	public   void setHasNewRoles(Bool val) {
		hasNewRoles = val;
	}

	/*Radix::Acs::UserGroup:logonTimeSchedule-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonTimeSchedule")
	public published  org.radixware.ads.Scheduling.server.IntervalSchedule getLogonTimeSchedule() {
		return logonTimeSchedule;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonTimeSchedule")
	public published   void setLogonTimeSchedule(org.radixware.ads.Scheduling.server.IntervalSchedule val) {
		logonTimeSchedule = val;
	}

	/*Radix::Acs::UserGroup:logonScheduleId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonScheduleId")
	public published  Int getLogonScheduleId() {
		return logonScheduleId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonScheduleId")
	public published   void setLogonScheduleId(Int val) {
		logonScheduleId = val;
	}

	/*Radix::Acs::UserGroup:curUserCanAccept-Dynamic Property*/



	protected Bool curUserCanAccept=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:curUserCanAccept")
	public published  Bool getCurUserCanAccept() {

		return getArte().getRights().getDualControlController().isCurUserCanAccept();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:curUserCanAccept")
	public published   void setCurUserCanAccept(Bool val) {
		curUserCanAccept = val;
	}

	/*Radix::Acs::UserGroup:canCopyResourcesFromSrc-Dynamic Property*/



	protected Bool canCopyResourcesFromSrc=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:canCopyResourcesFromSrc")
	private final  Bool getCanCopyResourcesFromSrc() {
		return canCopyResourcesFromSrc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:canCopyResourcesFromSrc")
	private final   void setCanCopyResourcesFromSrc(Bool val) {
		canCopyResourcesFromSrc = val;
	}

	/*Radix::Acs::UserGroup:finalTitle-Dynamic Property*/



	protected Str finalTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:finalTitle")
	public published  Str getFinalTitle() {

		if (title!=null && !title.isEmpty()) {
		    return title;
		}
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:finalTitle")
	public published   void setFinalTitle(Str val) {
		finalTitle = val;
	}

























































































	/*Radix::Acs::UserGroup:Methods-Methods*/

	/*Radix::Acs::UserGroup:AcceptRoles-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:AcceptRoles")
	public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument AcceptRoles (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return AcsUtils.acceptedUserOrGroup(false, name);
	}

	/*Radix::Acs::UserGroup:beforeAccept-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:beforeAccept")
	public published  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument beforeAccept () {
		final Str name = name;

		final CommandsXsd:AcceptedRightsCheckValueDocument rez = CommandsXsd:AcceptedRightsCheckValueDocument.Factory.newInstance();

		final Types::Reference<java.lang.Integer> acceptedRoles = new Types::Reference<java.lang.Integer>();
		final Types::Reference<java.lang.Integer> unacceptedRoles = new Types::Reference<java.lang.Integer>();

		final Types::Reference<java.lang.Integer> acceptedUser2Groups = new Types::Reference<java.lang.Integer>();
		final Types::Reference<java.lang.Integer> unacceptedUser2Groups = new Types::Reference<java.lang.Integer>();

		getArte().getRights().getDualControlController().getRolesCountForGroup(name, acceptedRoles, unacceptedRoles, acceptedUser2Groups, unacceptedUser2Groups);


		rez.AcceptedRightsCheckValue = CommandsXsd:AcceptedRightsCheckValueDocument.AcceptedRightsCheckValue.Factory.newInstance();
		rez.AcceptedRightsCheckValue.AcceptedRoles = acceptedRoles.get().intValue();
		rez.AcceptedRightsCheckValue.UnacceptedRoles = unacceptedRoles.get().intValue();

		rez.AcceptedRightsCheckValue.AcceptedUser2Group = acceptedUser2Groups.get().intValue();
		rez.AcceptedRightsCheckValue.UnacceptedUser2Group = unacceptedUser2Groups.get().intValue();

		return rez;
	}

	/*Radix::Acs::UserGroup:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		super.afterCreate(src);
		if (src!=null){
		    final Str srcGroup =  src.getProp(idof[UserGroup:name]).toString();
		    final Str destGroup =  this.name;
		    
		    if (!Arte.Rights.isCurUserHaveGroupRights(srcGroup)){
		        throw new InvalidEasRequestClientFault("Attempt to assign exceeding rights");
		    }
		    
		    final boolean canCopyResources = this.canCopyResourcesFromSrc.booleanValue();
		    if (canCopyResources) {
		        {
		            final GetEventSubscriptionByUserGroup cursor = GetEventSubscriptionByUserGroup.open(srcGroup);
		            try {

		                while (cursor.next()) {
		                    final PersoComm::EventSubscription srcEventSubscription = PersoComm::EventSubscription.loadByPK(cursor.id, true); 
		                    final PersoComm::EventSubscription newEventSubscription = new PersoComm::EventSubscription();
		                    newEventSubscription.init(null, srcEventSubscription);
		                    newEventSubscription.userGroupName = destGroup;        
		                    newEventSubscription.create(srcEventSubscription); 
		                }
		            } finally {
		                cursor.close();
		            }
		        }
		        {
		            final GetGroup2RolesByGroup cursor = GetGroup2RolesByGroup.open(srcGroup);
		            try {

		                while (cursor.next()) {
		                    final UserGroup2Role srcUserGroup2Role = UserGroup2Role.loadByPK(cursor.id, true); 
		                    final UserGroup2Role newUserGroup2Role = new UserGroup2Role();
		                    newUserGroup2Role.init(null, srcUserGroup2Role);
		                    newUserGroup2Role.groupName = destGroup;        
		                    newUserGroup2Role.create(srcUserGroup2Role); 
		                }
		            } finally {
		                cursor.close();
		            }
		        }
		        {
		            final GetUsersByGroup cursor = GetUsersByGroup.open(srcGroup);
		            try {

		                while (cursor.next()) {
		                    final User2UserGroup newUser2UserGroup = new User2UserGroup();
		                    newUser2UserGroup.init();
		                    newUser2UserGroup.groupName = destGroup;        
		                    newUser2UserGroup.userName = cursor.userName;
		                    newUser2UserGroup.create();
		                }
		            } finally {
		                cursor.close();
		            }
		        }        
		    }
		}      
	}

	/*Radix::Acs::UserGroup:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:beforeDelete")
	protected published  boolean beforeDelete () {
		if (getArte().Rights.isCurUserInGroup(name))
		{
		  throw new InvalidEasRequestClientFault("Attempt to revoke own rights");
		}
		if (!getArte().Rights.isCurUserHaveGroupRights(name)){
		   throw new InvalidEasRequestClientFault("Attempt to revoke rights that you cannot assign");
		}
		//getArte().Rights.compileGroupRightsBeforeDelete(true, );
		return true;
	}

	/*Radix::Acs::UserGroup:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:loadByPidStr")
	public static published  org.radixware.ads.Acs.server.UserGroup loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),pidAsStr);
		try{
		return (
		org.radixware.ads.Acs.server.UserGroup) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::UserGroup:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:loadByPK")
	public static published  org.radixware.ads.Acs.server.UserGroup loadByPK (Str name, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(name==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),name);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),pkValsMap);
		try{
		return (
		org.radixware.ads.Acs.server.UserGroup) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Acs::UserGroup:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:afterDelete")
	protected published  void afterDelete () {
		getArte().Rights.compileRights();
		super.afterDelete();
	}

	/*Radix::Acs::UserGroup:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		if (name != null) {
		    name = name.trim();
		}
		return super.beforeCreate(src);
	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdEMR4DLTXNBEK3MBIW65E7N2F5Y){
			final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdEMR4DLTXNBEK3MBIW65E7N2F5Y_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdQIAIE4S2M5DSXNIRWMTGPCKYX4){
			org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument result = AcceptRoles(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}
	private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdEMR4DLTXNBEK3MBIW65E7N2F5Y_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
		final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
			@Override
			protected  org.radixware.kernel.common.enums.EValType getReturnType(){
				return org.radixware.kernel.common.enums.EValType.XML;
			}
			protected  Object invokeImpl(Object[] arguments){
				Object $res$ =org.radixware.ads.Acs.server.UserGroup.this.mthUIKL2NJBSVBFFFEFHDHIJGU77M();
				return $res$;
			}
		};
		return invoker.invoke();
	}


}

/* Radix::Acs::UserGroup - Server Meta*/

/*Radix::Acs::UserGroup-Entity Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),"UserGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Acs::UserGroup:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
							/*Owner Class Name*/
							"UserGroup",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),
							/*Property presentations*/

							/*Radix::Acs::UserGroup:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:description:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQZD24VYMY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:name:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:hasNewRoles:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd22CPESOUCRD6JKXZSLBL3PHGDA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:logonTimeSchedule:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col72FCQRESWFEQRPGWHTZXZ5LRFY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3668986,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Acs::UserGroup:curUserCanAccept:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKZD3XYFSQBDQPPJRWA5KWS52GY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::UserGroup:finalTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFT2GL72YWVEFREWECVMNLZ7B7I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::UserGroup:AcceptRights-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQIAIE4S2M5DSXNIRWMTGPCKYX4"),"AcceptRights",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::UserGroup:BeforeAccept-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMR4DLTXNBEK3MBIW65E7N2F5Y"),"BeforeAccept",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Acs::UserGroup:Name-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtY4CR7MMSRVEI3NHUNAU2W4CZHE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),"Name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3C4WN6KLZNHD5CXCYL2SXHS45E"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Acs::UserGroup:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Acs::UserGroup:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Acs::UserGroup:General:User-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX6Z6SGTDHZC6ZIJDI3CR5PHUSE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refL5DHK752J3NRDAQSABIFNQAAAE"),
													org.radixware.kernel.server.types.Restrictions.Factory.newInstance(35,null,null,null),
													null),

												/*Radix::Acs::UserGroup:General:User2UserGroup-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiEQPNARTGPFD7DFE5OM2D24PEXE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecDYWJCJTTGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprSMXMQ6XLVZGA7PNQS2XITVNWAM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refTHML75N2J3NRDAQSABIFNQAAAE"),
													org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
													null),

												/*Radix::Acs::UserGroup:General:UserGroup2Role-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi7PSNEUQ47ZC2HHFXSKEKEXEXGM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("ref5QIXNSF2J3NRDAQSABIFNQAAAE"),
													org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
													null),

												/*Radix::Acs::UserGroup:General:EventSubscription-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi2WKHXHNINJETJAEHH2LWNSRFVA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refAXWFGZZZVLOBDCLSAALOMT5GDM"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::UserGroup:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtY4CR7MMSRVEI3NHUNAU2W4CZHE"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(104,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Acs::UserGroup:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFT2GL72YWVEFREWECVMNLZ7B7I"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Acs::UserGroup:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::UserGroup:useDualControlOnRoleAssign-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),"useDualControlOnRoleAssign",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:description-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQZD24VYMY3NBDGMCABQAQH3XQ4"),"description",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls72PP4GJ3MNEXDIZDCNPFTQGBN4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:name-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),"name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJ5V7RLFQ5BCJHYGB2ZG23VMA4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JYE2YH2WREFNEKI5WDAFDBKLQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:hasNewRoles-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd22CPESOUCRD6JKXZSLBL3PHGDA"),"hasNewRoles",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:logonTimeSchedule-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col72FCQRESWFEQRPGWHTZXZ5LRFY"),"logonTimeSchedule",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKYYK6IBZBFX7M5PP342SUG55E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref56AJZXUOR5EUXLQO2X7YE74NVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:logonScheduleId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDGIMFGI2VFC65ALDT35HHU32C4"),"logonScheduleId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:curUserCanAccept-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),"curUserCanAccept",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWZNYPSIFZCRJONMGUTMNJVB3Q"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:canCopyResourcesFromSrc-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKZD3XYFSQBDQPPJRWA5KWS52GY"),"canCopyResourcesFromSrc",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::UserGroup:finalTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFT2GL72YWVEFREWECVMNLZ7B7I"),"finalTitle",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::UserGroup:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdQIAIE4S2M5DSXNIRWMTGPCKYX4"),"AcceptRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZ4MJYHDWMNF33BZYLJY54LQTMI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUIKL2NJBSVBFFFEFHDHIJGU77M"),"beforeAccept",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNEPZX74KNFF6ROKCARITINYPLE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPidAsStr__________________")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV2XEAGILY3NBDGMCABQAQH3XQ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCheckExisstance___________"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLBG2JLB3R5ACXEKYPV2TLB7RFU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.OWN,null,new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea[]{

								new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea(new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition[]{

										new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition(org.radixware.kernel.common.types.Id.Factory.loadFrom("apf1ZOQHCO35XORDCV2AANE2UAFXA"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"))
								})
						},false);
}

/* Radix::Acs::UserGroup - Desktop Executable*/

/*Radix::Acs::UserGroup-Entity Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup")
public interface UserGroup {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel )  super.getEntity(i);}
	}











































































	/*Radix::Acs::UserGroup:logonTimeSchedule:logonTimeSchedule-Presentation Property*/


	public class LogonTimeSchedule extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public LogonTimeSchedule(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.IntervalSchedule.IntervalSchedule_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Scheduling.explorer.IntervalSchedule.IntervalSchedule_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Scheduling.explorer.IntervalSchedule.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Scheduling.explorer.IntervalSchedule.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonTimeSchedule:logonTimeSchedule")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonTimeSchedule:logonTimeSchedule")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LogonTimeSchedule getLogonTimeSchedule();
	/*Radix::Acs::UserGroup:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Acs::UserGroup:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::Acs::UserGroup:name:name-Presentation Property*/


	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::Acs::UserGroup:hasNewRoles:hasNewRoles-Presentation Property*/


	public class HasNewRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasNewRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:hasNewRoles:hasNewRoles")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:hasNewRoles:hasNewRoles")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasNewRoles getHasNewRoles();
	/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:useDualControlOnRoleAssign-Presentation Property*/


	public class UseDualControlOnRoleAssign extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseDualControlOnRoleAssign(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:useDualControlOnRoleAssign:useDualControlOnRoleAssign")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:useDualControlOnRoleAssign:useDualControlOnRoleAssign")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseDualControlOnRoleAssign getUseDualControlOnRoleAssign();
	/*Radix::Acs::UserGroup:finalTitle:finalTitle-Presentation Property*/


	public class FinalTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FinalTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:finalTitle:finalTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:finalTitle:finalTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FinalTitle getFinalTitle();
	/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:canCopyResourcesFromSrc-Presentation Property*/


	public class CanCopyResourcesFromSrc extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanCopyResourcesFromSrc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:canCopyResourcesFromSrc:canCopyResourcesFromSrc")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:canCopyResourcesFromSrc:canCopyResourcesFromSrc")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanCopyResourcesFromSrc getCanCopyResourcesFromSrc();
	/*Radix::Acs::UserGroup:curUserCanAccept:curUserCanAccept-Presentation Property*/


	public class CurUserCanAccept extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CurUserCanAccept(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:curUserCanAccept:curUserCanAccept")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:curUserCanAccept:curUserCanAccept")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CurUserCanAccept getCurUserCanAccept();
	public static class BeforeAccept extends org.radixware.kernel.common.client.models.items.Command{
		protected BeforeAccept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class AcceptRights extends org.radixware.kernel.common.client.models.items.Command{
		protected AcceptRights(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument.class);
		}

	}



}

/* Radix::Acs::UserGroup - Desktop Meta*/

/*Radix::Acs::UserGroup-Entity Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::UserGroup:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::UserGroup",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVJVNZH43MZD2LL3GWVDSJ35HEQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJJ2MSLUBFAAXKXMLZSUWVUZI4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),0,

			/*Radix::Acs::UserGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),
						"useDualControlOnRoleAssign",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQZD24VYMY3NBDGMCABQAQH3XQ4"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls72PP4GJ3MNEXDIZDCNPFTQGBN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJ5V7RLFQ5BCJHYGB2ZG23VMA4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JYE2YH2WREFNEKI5WDAFDBKLQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:hasNewRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd22CPESOUCRD6JKXZSLBL3PHGDA"),
						"hasNewRoles",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::UserGroup:hasNewRoles:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:logonTimeSchedule:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col72FCQRESWFEQRPGWHTZXZ5LRFY"),
						"logonTimeSchedule",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKYYK6IBZBFX7M5PP342SUG55E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ37Z2AGOS5GTBB23ITK7GTZ4BY"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},
						null,
						null,
						3668986,
						3670011,false),

					/*Radix::Acs::UserGroup:curUserCanAccept:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),
						"curUserCanAccept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWZNYPSIFZCRJONMGUTMNJVB3Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:curUserCanAccept:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKZD3XYFSQBDQPPJRWA5KWS52GY"),
						"canCopyResourcesFromSrc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:finalTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFT2GL72YWVEFREWECVMNLZ7B7I"),
						"finalTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:finalTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::UserGroup:AcceptRights-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQIAIE4S2M5DSXNIRWMTGPCKYX4"),
						"AcceptRights",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7EZNQIBZ5FP3N6YCTJCXP7SBQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKQVMYXIXRZFKRHB6P6AC7YNR3I"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::UserGroup:BeforeAccept-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMR4DLTXNBEK3MBIW65E7N2F5Y"),
						"BeforeAccept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::UserGroup:Name-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtY4CR7MMSRVEI3NHUNAU2W4CZHE"),
						"Name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3C4WN6KLZNHD5CXCYL2SXHS45E"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref56AJZXUOR5EUXLQO2X7YE74NVQ"),"UserGroup=>IntervalSchd (logonScheduleId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDGIMFGI2VFC65ALDT35HHU32C4")},new String[]{"logonScheduleId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ")},
			false,true,false);
}

/* Radix::Acs::UserGroup - Web Executable*/

/*Radix::Acs::UserGroup-Entity Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup")
public interface UserGroup {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel )  super.getEntity(i);}
	}











































































	/*Radix::Acs::UserGroup:logonTimeSchedule:logonTimeSchedule-Presentation Property*/


	public class LogonTimeSchedule extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public LogonTimeSchedule(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Scheduling.web.IntervalSchedule.IntervalSchedule_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Scheduling.web.IntervalSchedule.IntervalSchedule_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Scheduling.web.IntervalSchedule.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Scheduling.web.IntervalSchedule.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonTimeSchedule:logonTimeSchedule")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:logonTimeSchedule:logonTimeSchedule")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LogonTimeSchedule getLogonTimeSchedule();
	/*Radix::Acs::UserGroup:title:title-Presentation Property*/


	public class Title extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Title(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Acs::UserGroup:description:description-Presentation Property*/


	public class Description extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Description(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:description:description")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:description:description")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Description getDescription();
	/*Radix::Acs::UserGroup:name:name-Presentation Property*/


	public class Name extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Name(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::Acs::UserGroup:hasNewRoles:hasNewRoles-Presentation Property*/


	public class HasNewRoles extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasNewRoles(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:hasNewRoles:hasNewRoles")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:hasNewRoles:hasNewRoles")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasNewRoles getHasNewRoles();
	/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:useDualControlOnRoleAssign-Presentation Property*/


	public class UseDualControlOnRoleAssign extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseDualControlOnRoleAssign(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:useDualControlOnRoleAssign:useDualControlOnRoleAssign")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:useDualControlOnRoleAssign:useDualControlOnRoleAssign")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseDualControlOnRoleAssign getUseDualControlOnRoleAssign();
	/*Radix::Acs::UserGroup:finalTitle:finalTitle-Presentation Property*/


	public class FinalTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FinalTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:finalTitle:finalTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:finalTitle:finalTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FinalTitle getFinalTitle();
	/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:canCopyResourcesFromSrc-Presentation Property*/


	public class CanCopyResourcesFromSrc extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanCopyResourcesFromSrc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:canCopyResourcesFromSrc:canCopyResourcesFromSrc")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:canCopyResourcesFromSrc:canCopyResourcesFromSrc")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanCopyResourcesFromSrc getCanCopyResourcesFromSrc();
	/*Radix::Acs::UserGroup:curUserCanAccept:curUserCanAccept-Presentation Property*/


	public class CurUserCanAccept extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CurUserCanAccept(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:curUserCanAccept:curUserCanAccept")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:curUserCanAccept:curUserCanAccept")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CurUserCanAccept getCurUserCanAccept();
	public static class BeforeAccept extends org.radixware.kernel.common.client.models.items.Command{
		protected BeforeAccept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public static class AcceptRights extends org.radixware.kernel.common.client.models.items.Command{
		protected AcceptRights(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument.class);
		}

	}



}

/* Radix::Acs::UserGroup - Web Meta*/

/*Radix::Acs::UserGroup-Entity Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::UserGroup:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::UserGroup",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVJVNZH43MZD2LL3GWVDSJ35HEQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJJ2MSLUBFAAXKXMLZSUWVUZI4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),0,

			/*Radix::Acs::UserGroup:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),
						"useDualControlOnRoleAssign",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::UserGroup:useDualControlOnRoleAssign:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:description:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQZD24VYMY3NBDGMCABQAQH3XQ4"),
						"description",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls72PP4GJ3MNEXDIZDCNPFTQGBN4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:description:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJ5V7RLFQ5BCJHYGB2ZG23VMA4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JYE2YH2WREFNEKI5WDAFDBKLQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:hasNewRoles:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd22CPESOUCRD6JKXZSLBL3PHGDA"),
						"hasNewRoles",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::UserGroup:hasNewRoles:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:logonTimeSchedule:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col72FCQRESWFEQRPGWHTZXZ5LRFY"),
						"logonTimeSchedule",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKYYK6IBZBFX7M5PP342SUG55E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ37Z2AGOS5GTBB23ITK7GTZ4BY"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},
						null,
						null,
						3668986,
						3670011,false),

					/*Radix::Acs::UserGroup:curUserCanAccept:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),
						"curUserCanAccept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWZNYPSIFZCRJONMGUTMNJVB3Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:curUserCanAccept:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdKZD3XYFSQBDQPPJRWA5KWS52GY"),
						"canCopyResourcesFromSrc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:canCopyResourcesFromSrc:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::UserGroup:finalTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFT2GL72YWVEFREWECVMNLZ7B7I"),
						"finalTitle",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::UserGroup:finalTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::UserGroup:AcceptRights-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQIAIE4S2M5DSXNIRWMTGPCKYX4"),
						"AcceptRights",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7EZNQIBZ5FP3N6YCTJCXP7SBQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKQVMYXIXRZFKRHB6P6AC7YNR3I"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::UserGroup:BeforeAccept-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMR4DLTXNBEK3MBIW65E7N2F5Y"),
						"BeforeAccept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.RPC,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::UserGroup:Name-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtY4CR7MMSRVEI3NHUNAU2W4CZHE"),
						"Name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3C4WN6KLZNHD5CXCYL2SXHS45E"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref56AJZXUOR5EUXLQO2X7YE74NVQ"),"UserGroup=>IntervalSchd (logonScheduleId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDGIMFGI2VFC65ALDT35HHU32C4")},new String[]{"logonScheduleId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ")},
			false,true,false);
}

/* Radix::Acs::UserGroup:General - Desktop Meta*/

/*Radix::Acs::UserGroup:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
	null,
	null,

	/*Radix::Acs::UserGroup:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::UserGroup:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5O22EF223RGF5I7XFVN6SRVYVQ"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBW44XZ6BNBDKBKQNES7WDMCPVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVJVNZH43MZD2LL3GWVDSJ35HEQ"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQZD24VYMY3NBDGMCABQAQH3XQ4"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col72FCQRESWFEQRPGWHTZXZ5LRFY"),0,2,1,false,false)
			},null),

			/*Radix::Acs::UserGroup:General:Users-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQWHL6CTBEJGZVCFH64DD5PAWFE"),"Users",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiEQPNARTGPFD7DFE5OM2D24PEXE")),

			/*Radix::Acs::UserGroup:General:Roles-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7QVS2IKKU5HGZCTVQIDNZASJ6Q"),"Roles",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi7PSNEUQ47ZC2HHFXSKEKEXEXGM")),

			/*Radix::Acs::UserGroup:General:AdministeredUsers-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAVJGDASWKVEUPMLQOUHNZRRTQI"),"AdministeredUsers",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX6Z6SGTDHZC6ZIJDI3CR5PHUSE")),

			/*Radix::Acs::UserGroup:General:EventSubscriptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2FFXYXRCVNBRPICVAJXTGZHAXM"),"EventSubscriptions",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("img3HB6PW72A7PPVUD65FYM2NHX5Z6YVQ4S"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi2WKHXHNINJETJAEHH2LWNSRFVA"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5O22EF223RGF5I7XFVN6SRVYVQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAVJGDASWKVEUPMLQOUHNZRRTQI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQWHL6CTBEJGZVCFH64DD5PAWFE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7QVS2IKKU5HGZCTVQIDNZASJ6Q")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2FFXYXRCVNBRPICVAJXTGZHAXM"))}
	,

	/*Radix::Acs::UserGroup:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::UserGroup:General:User-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX6Z6SGTDHZC6ZIJDI3CR5PHUSE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7DFVJFGJNCWNFDXH2F4JKYNBE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
					35,
					null,
					16384,true),

				/*Radix::Acs::UserGroup:General:User2UserGroup-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiEQPNARTGPFD7DFE5OM2D24PEXE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBU7NM4AQBAH7KI42IRNDXW6AA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecDYWJCJTTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprSMXMQ6XLVZGA7PNQS2XITVNWAM"),
					0,
					null,
					16384,true),

				/*Radix::Acs::UserGroup:General:UserGroup2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi7PSNEUQ47ZC2HHFXSKEKEXEXGM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVF6Y357SSBCPDJGF3QJRQYDV64"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
					0,
					null,
					16384,true),

				/*Radix::Acs::UserGroup:General:EventSubscription-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi2WKHXHNINJETJAEHH2LWNSRFVA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
					0,
					null,
					16544,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::UserGroup:General - Web Meta*/

/*Radix::Acs::UserGroup:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
	null,
	null,

	/*Radix::Acs::UserGroup:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::UserGroup:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5O22EF223RGF5I7XFVN6SRVYVQ"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBW44XZ6BNBDKBKQNES7WDMCPVQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVJVNZH43MZD2LL3GWVDSJ35HEQ"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQZD24VYMY3NBDGMCABQAQH3XQ4"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col72FCQRESWFEQRPGWHTZXZ5LRFY"),0,2,1,false,false)
			},null),

			/*Radix::Acs::UserGroup:General:Users-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQWHL6CTBEJGZVCFH64DD5PAWFE"),"Users",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiEQPNARTGPFD7DFE5OM2D24PEXE")),

			/*Radix::Acs::UserGroup:General:Roles-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7QVS2IKKU5HGZCTVQIDNZASJ6Q"),"Roles",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi7PSNEUQ47ZC2HHFXSKEKEXEXGM")),

			/*Radix::Acs::UserGroup:General:AdministeredUsers-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAVJGDASWKVEUPMLQOUHNZRRTQI"),"AdministeredUsers",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX6Z6SGTDHZC6ZIJDI3CR5PHUSE")),

			/*Radix::Acs::UserGroup:General:EventSubscriptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2FFXYXRCVNBRPICVAJXTGZHAXM"),"EventSubscriptions",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("img3HB6PW72A7PPVUD65FYM2NHX5Z6YVQ4S"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi2WKHXHNINJETJAEHH2LWNSRFVA"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5O22EF223RGF5I7XFVN6SRVYVQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAVJGDASWKVEUPMLQOUHNZRRTQI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQWHL6CTBEJGZVCFH64DD5PAWFE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg7QVS2IKKU5HGZCTVQIDNZASJ6Q")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2FFXYXRCVNBRPICVAJXTGZHAXM"))}
	,

	/*Radix::Acs::UserGroup:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::UserGroup:General:User-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiX6Z6SGTDHZC6ZIJDI3CR5PHUSE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7DFVJFGJNCWNFDXH2F4JKYNBE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
					35,
					null,
					16384,true),

				/*Radix::Acs::UserGroup:General:User2UserGroup-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiEQPNARTGPFD7DFE5OM2D24PEXE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBU7NM4AQBAH7KI42IRNDXW6AA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecDYWJCJTTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprSMXMQ6XLVZGA7PNQS2XITVNWAM"),
					0,
					null,
					16384,true),

				/*Radix::Acs::UserGroup:General:UserGroup2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi7PSNEUQ47ZC2HHFXSKEKEXEXGM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVF6Y357SSBCPDJGF3QJRQYDV64"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
					0,
					null,
					16384,true),

				/*Radix::Acs::UserGroup:General:EventSubscription-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi2WKHXHNINJETJAEHH2LWNSRFVA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec7HTJAWJXVLOBDCLSAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprWZH3CR26VXOBDCLUAALOMT5GDM"),
					0,
					null,
					16544,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::UserGroup:General:Model - Desktop Executable*/

/*Radix::Acs::UserGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.UserGroup.UserGroup_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup:General:Model:canCopyResourcesFromSrc-Presentation Property*/




	public class CanCopyResourcesFromSrc extends org.radixware.ads.Acs.explorer.UserGroup.prdKZD3XYFSQBDQPPJRWA5KWS52GY{
		public CanCopyResourcesFromSrc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:canCopyResourcesFromSrc")
		public final  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:canCopyResourcesFromSrc")
		public final   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanCopyResourcesFromSrc getCanCopyResourcesFromSrc(){return (CanCopyResourcesFromSrc)getProperty(prdKZD3XYFSQBDQPPJRWA5KWS52GY);}








	/*Radix::Acs::UserGroup:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!isNew()) {
		    try {
		        Bool usetDualControl = useDualControlOnRoleAssign.Value;
		        boolean bUsetDualControl = usetDualControl != null && usetDualControl.booleanValue();
		        this.getCommand(idof[UserGroup:AcceptRights]).setVisible(bUsetDualControl);

		        if (bUsetDualControl){
		            final Bool curUserCanAccept = curUserCanAccept.Value;
		            final boolean bCurUserCanAccept = curUserCanAccept!=null && curUserCanAccept.booleanValue();
		            this.getCommand(idof[UserGroup:AcceptRights]).setEnabled(bCurUserCanAccept);
		        }


		        boolean isEditorContext = !isInSelectorRowContext();
		        if (isEditorContext) {

		            final Explorer.Models::GroupModel rolesGroup = isExplorerItemAccessible(idof[UserGroup:General:UserGroup2Role]) && isEditorContext
		                    ? (Explorer.Models::GroupModel) this.getChildModel(idof[UserGroup:General:UserGroup2Role])
		                    : null;
		            if (rolesGroup != null) {
		                rolesGroup.getSelectorColumn(idof[UserGroup2Role:groupTitle]).setVisible(false);
		            }
		        }
		    } catch (Exception ex) {
		        showException(ex);
		    }
		} else {
		    this.getCommand(idof[UserGroup:AcceptRights]).setVisible(false);
		}
	}

	/*Radix::Acs::UserGroup:General:Model:acceptRoles-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:acceptRoles")
	public  void acceptRoles (org.radixware.ads.Acs.explorer.UserGroup.AcceptRights command) {
		try{
		    final CommandsXsd:AcceptedRolesDocument doc = command.send();
		    AcsClientCommonUtils.processAcceptedCommand(doc, getEnvironment(), false);
		 
		    
		    final boolean isNotSelector = !isInSelectorRowContext();
		    
		    final Types::Id userGroup2RoleId = idof[UserGroup:General:UserGroup2Role];
		    final Types::Id user2UserGroupId = idof[UserGroup:General:User2UserGroup];
		    final Explorer.Models::GroupModel rolesGroup = 
		                    isNotSelector && isExplorerItemAccessible(userGroup2RoleId) ? 
		                    (Explorer.Models::GroupModel) this.getChildModel(userGroup2RoleId) : null;
		    final Explorer.Models::GroupModel user2Group = isNotSelector && isExplorerItemAccessible(user2UserGroupId) ? (Explorer.Models::GroupModel) this.getChildModel(user2UserGroupId) : null;
		                
		    if (rolesGroup!=null){
		        rolesGroup.reread();
		    }
		    
		    if (user2Group!=null){
		        user2Group.reread();
		    }
		    
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::Acs::UserGroup:General:Model:beforeAccept-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:beforeAccept")
	public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument beforeAccept () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.Acs.explorer.UserGroup.BeforeAccept _cmd_cmdEMR4DLTXNBEK3MBIW65E7N2F5Y_instance_ = (org.radixware.ads.Acs.explorer.UserGroup.BeforeAccept)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMR4DLTXNBEK3MBIW65E7N2F5Y"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdEMR4DLTXNBEK3MBIW65E7N2F5Y_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument ? (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) $rpc$call$result$ : (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument.class);

	}

	/*Radix::Acs::UserGroup:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (this.getSrcPid()!=null) {//RADIX-13786
		    final String mess = "Copy also subscriptions, roles and users in new user group?";
		    final boolean copyResources = this.getEnvironment().messageConfirmation(null, mess);
		    this.canCopyResourcesFromSrc.setValue(copyResources);
		}
		return super.beforeCreate();
	}
	public static class BeforeAccept extends org.radixware.ads.Acs.explorer.UserGroup.BeforeAccept{
		protected BeforeAccept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class AcceptRights extends org.radixware.ads.Acs.explorer.UserGroup.AcceptRights{
		protected AcceptRights(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_AcceptRights( this );
		}

	}















}

/* Radix::Acs::UserGroup:General:Model - Desktop Meta*/

/*Radix::Acs::UserGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup:General:Model - Web Executable*/

/*Radix::Acs::UserGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.UserGroup.UserGroup_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup:General:Model:canCopyResourcesFromSrc-Presentation Property*/




	public class CanCopyResourcesFromSrc extends org.radixware.ads.Acs.web.UserGroup.prdKZD3XYFSQBDQPPJRWA5KWS52GY{
		public CanCopyResourcesFromSrc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:canCopyResourcesFromSrc")
		public final  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:canCopyResourcesFromSrc")
		public final   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanCopyResourcesFromSrc getCanCopyResourcesFromSrc(){return (CanCopyResourcesFromSrc)getProperty(prdKZD3XYFSQBDQPPJRWA5KWS52GY);}








	/*Radix::Acs::UserGroup:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!isNew()) {
		    try {
		        Bool usetDualControl = useDualControlOnRoleAssign.Value;
		        boolean bUsetDualControl = usetDualControl != null && usetDualControl.booleanValue();
		        this.getCommand(idof[UserGroup:AcceptRights]).setVisible(bUsetDualControl);

		        if (bUsetDualControl){
		            final Bool curUserCanAccept = curUserCanAccept.Value;
		            final boolean bCurUserCanAccept = curUserCanAccept!=null && curUserCanAccept.booleanValue();
		            this.getCommand(idof[UserGroup:AcceptRights]).setEnabled(bCurUserCanAccept);
		        }


		        boolean isEditorContext = !isInSelectorRowContext();
		        if (isEditorContext) {

		            final Explorer.Models::GroupModel rolesGroup = isExplorerItemAccessible(idof[UserGroup:General:UserGroup2Role]) && isEditorContext
		                    ? (Explorer.Models::GroupModel) this.getChildModel(idof[UserGroup:General:UserGroup2Role])
		                    : null;
		            if (rolesGroup != null) {
		                rolesGroup.getSelectorColumn(idof[UserGroup2Role:groupTitle]).setVisible(false);
		            }
		        }
		    } catch (Exception ex) {
		        showException(ex);
		    }
		} else {
		    this.getCommand(idof[UserGroup:AcceptRights]).setVisible(false);
		}
	}

	/*Radix::Acs::UserGroup:General:Model:acceptRoles-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:acceptRoles")
	public  void acceptRoles (org.radixware.ads.Acs.web.UserGroup.AcceptRights command) {
		try{
		    final CommandsXsd:AcceptedRolesDocument doc = command.send();
		    AcsClientCommonUtils.processAcceptedCommand(doc, getEnvironment(), false);
		 
		    
		    final boolean isNotSelector = !isInSelectorRowContext();
		    
		    final Types::Id userGroup2RoleId = idof[UserGroup:General:UserGroup2Role];
		    final Types::Id user2UserGroupId = idof[UserGroup:General:User2UserGroup];
		    final Explorer.Models::GroupModel rolesGroup = 
		                    isNotSelector && isExplorerItemAccessible(userGroup2RoleId) ? 
		                    (Explorer.Models::GroupModel) this.getChildModel(userGroup2RoleId) : null;
		    final Explorer.Models::GroupModel user2Group = isNotSelector && isExplorerItemAccessible(user2UserGroupId) ? (Explorer.Models::GroupModel) this.getChildModel(user2UserGroupId) : null;
		                
		    if (rolesGroup!=null){
		        rolesGroup.reread();
		    }
		    
		    if (user2Group!=null){
		        user2Group.reread();
		    }
		    
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::Acs::UserGroup:General:Model:beforeAccept-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:beforeAccept")
	public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument beforeAccept () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.Acs.web.UserGroup.BeforeAccept _cmd_cmdEMR4DLTXNBEK3MBIW65E7N2F5Y_instance_ = (org.radixware.ads.Acs.web.UserGroup.BeforeAccept)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMR4DLTXNBEK3MBIW65E7N2F5Y"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdEMR4DLTXNBEK3MBIW65E7N2F5Y_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument ? (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) $rpc$call$result$ : (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument.class);

	}

	/*Radix::Acs::UserGroup:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (this.getSrcPid()!=null) {//RADIX-13786
		    final String mess = "Copy also subscriptions, roles and users in new user group?";
		    final boolean copyResources = this.getEnvironment().messageConfirmation(null, mess);
		    this.canCopyResourcesFromSrc.setValue(copyResources);
		}
		return super.beforeCreate();
	}
	public static class BeforeAccept extends org.radixware.ads.Acs.web.UserGroup.BeforeAccept{
		protected BeforeAccept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class AcceptRights extends org.radixware.ads.Acs.web.UserGroup.AcceptRights{
		protected AcceptRights(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_AcceptRights( this );
		}

	}















}

/* Radix::Acs::UserGroup:General:Model - Web Meta*/

/*Radix::Acs::UserGroup:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemMOOY2WRJ4BFDFGBKRCUPG5ARNQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup:General - Desktop Meta*/

/*Radix::Acs::UserGroup:General-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtY4CR7MMSRVEI3NHUNAU2W4CZHE"),
		null,
		false,
		true,
		null,
		104,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::UserGroup:General - Web Meta*/

/*Radix::Acs::UserGroup:General-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr2G325JYJSZGTPIGCQDEW37IBJQ"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtY4CR7MMSRVEI3NHUNAU2W4CZHE"),
		null,
		false,
		true,
		null,
		104,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIX2QYPQMY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdA5QOB7DP2ZFRPDQ2YMSZYBWUDA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdQNF7TCW77RHMDOEO6RXFFPQ5BA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::UserGroup:General:Model - Desktop Executable*/

/*Radix::Acs::UserGroup:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.UserGroup.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:CurrentEntityHandler")
	public class CurrentEntityHandler  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {

		private Explorer.Models::EntityModel current;

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:Properties-Properties*/

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:Methods-Methods*/

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {
			if (current != null && current.isExists()) {
			    try {
			        current.read();
			    } catch (InterruptedException | Explorer.Exceptions::ObjectNotFoundError e) {
			    } catch (Exceptions::ServiceClientException e) {
			        getEnvironment().getTracer().error(e);
			    } finally {
			        current = null;
			    }
			}
		}

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			current = entity;
		}


	}

	/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandler()) ;



	}


}

/* Radix::Acs::UserGroup:General:Model - Desktop Meta*/

/*Radix::Acs::UserGroup:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm2G325JYJSZGTPIGCQDEW37IBJQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup:General:Model - Web Executable*/

/*Radix::Acs::UserGroup:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.UserGroup.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::UserGroup:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:CurrentEntityHandler")
	public class CurrentEntityHandler  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {

		private Explorer.Models::EntityModel current;

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:Nested classes-Nested Classes*/

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:Properties-Properties*/

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:Methods-Methods*/

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {
			if (current != null && current.isExists()) {
			    try {
			        current.read();
			    } catch (InterruptedException | Explorer.Exceptions::ObjectNotFoundError e) {
			    } catch (Exceptions::ServiceClientException e) {
			        getEnvironment().getTracer().error(e);
			    } finally {
			        current = null;
			    }
			}
		}

		/*Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:CurrentEntityHandler:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			current = entity;
		}


	}

	/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/

	/*Radix::Acs::UserGroup:General:Model:Methods-Methods*/

	/*Radix::Acs::UserGroup:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::UserGroup:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandler()) ;



	}


}

/* Radix::Acs::UserGroup:General:Model - Web Meta*/

/*Radix::Acs::UserGroup:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agm2G325JYJSZGTPIGCQDEW37IBJQ"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::UserGroup:General:Model:Properties-Properties*/
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

/* Radix::Acs::UserGroup - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserGroup - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По имени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3C4WN6KLZNHD5CXCYL2SXHS45E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JYE2YH2WREFNEKI5WDAFDBKLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка дать права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5YL73ZTPR5CY7J2IOPOIZ2UCEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls72PP4GJ3MNEXDIZDCNPFTQGBN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBW44XZ6BNBDKBKQNES7WDMCPVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User can accept rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь может акцептовать права");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBWZNYPSIFZCRJONMGUTMNJVB3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke own rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка удалить собственные права.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBXFFARBU5RCUVFEJQVIWEN4AQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Logon time restriction");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ограничение времени логина");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKYYK6IBZBFX7M5PP342SUG55E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Copy also subscriptions, roles and users in new user group?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Копировать также подписки, роли и пользователей в новую группу пользователей?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHMBBQAABHRDTDI55KVEBW5TWZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<no restrictions>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<без ограничений>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ37Z2AGOS5GTBB23ITK7GTZ4BY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accept Rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцептовать права");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7EZNQIBZ5FP3N6YCTJCXP7SBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Administered Users");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Администрируемые пользователи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL7DFVJFGJNCWNFDXH2F4JKYNBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMJ5V7RLFQ5BCJHYGB2ZG23VMA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group Members");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Члены группы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBU7NM4AQBAH7KI42IRNDXW6AA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Groups");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группы пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJJ2MSLUBFAAXKXMLZSUWVUZI4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke rights that you cannot assign");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка забрать права, которые вы не сможете назначить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3VIKOQLGVH53NXWQKJ7I7SXJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVF6Y357SSBCPDJGF3QJRQYDV64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXW76T7COZ5BYNHXEGPCI7KYMYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUZZUNL7H5DNRJTW3EJLNNYPSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserGroup - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecQ23AYDTTGLNRDHRZABQAQH3XQ4"),"UserGroup - Localizing Bundle",$$$items$$$);
}
