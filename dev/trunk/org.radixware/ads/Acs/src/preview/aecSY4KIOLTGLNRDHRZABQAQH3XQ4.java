
/* Radix::Acs::User - Server Executable*/

/*Radix::Acs::User-Entity Class*/

package org.radixware.ads.Acs.server;

import org.radixware.schemas.eas.ExceptionEnum;
import org.radixware.kernel.common.auth.PasswordHash;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User")
public final published class User  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return User_mi.rdxMeta;}

	/*Radix::Acs::User:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Properties-Properties*/

	/*Radix::Acs::User:name-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:name")
	public published  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:name")
	public published   void setName(Str val) {
		name = val;
	}

	/*Radix::Acs::User:adminGroupName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroupName")
	public published  Str getAdminGroupName() {
		return adminGroupName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroupName")
	public published   void setAdminGroupName(Str val) {
		adminGroupName = val;
	}

	/*Radix::Acs::User:checkStation-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkStation")
	public published  Bool getCheckStation() {
		return checkStation;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkStation")
	public published   void setCheckStation(Bool val) {
		checkStation = val;
	}

	/*Radix::Acs::User:email-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:email")
	public published  Str getEmail() {
		return email;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:email")
	public published   void setEmail(Str val) {
		email = val;
	}

	/*Radix::Acs::User:lastPwdChangeTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastPwdChangeTime")
	public published  java.sql.Timestamp getLastPwdChangeTime() {
		return lastPwdChangeTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastPwdChangeTime")
	public published   void setLastPwdChangeTime(java.sql.Timestamp val) {
		lastPwdChangeTime = val;
	}

	/*Radix::Acs::User:mobilePhone-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mobilePhone")
	public published  Str getMobilePhone() {
		return mobilePhone;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mobilePhone")
	public published   void setMobilePhone(Str val) {
		mobilePhone = val;
	}

	/*Radix::Acs::User:personName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:personName")
	public published  Str getPersonName() {
		return personName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:personName")
	public published   void setPersonName(Str val) {
		personName = val;
	}

	/*Radix::Acs::User:pwdExpirationPeriod-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdExpirationPeriod")
	public published  Int getPwdExpirationPeriod() {
		return pwdExpirationPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdExpirationPeriod")
	public published   void setPwdExpirationPeriod(Int val) {
		pwdExpirationPeriod = val;
	}

	/*Radix::Acs::User:pwdHash-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHash")
	public published  Str getPwdHash() {

		if (!this.isInDatabase(false))//is new object
		    return internal[pwdHash];
		if (internal[pwdHash]==null)
		    return null;
		return "*";
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHash")
	public published   void setPwdHash(Str val) {

		if (val!=null && !"*".equals(val) && getArte().getUserName()!=null && !getArte().getUserName().isEmpty()){
		    internal[pwdHash] = val;    
		    getArte().getTrace().put(eventCode["User %1 set password for user %2 "], java.util.Arrays.asList(new Str[]{getArte().getUserName(), name}));    
		}
	}

	/*Radix::Acs::User:adminGroup-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroup")
	public published  org.radixware.ads.Acs.server.UserGroup getAdminGroup() {
		return adminGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroup")
	public published   void setAdminGroup(org.radixware.ads.Acs.server.UserGroup val) {
		adminGroup = val;
	}

	/*Radix::Acs::User:passwordDefined-Dynamic Property*/



	protected Str passwordDefined=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordDefined")
	public published  Str getPasswordDefined() {
		return passwordDefined;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordDefined")
	public published   void setPasswordDefined(Str val) {
		passwordDefined = val;
	}

	/*Radix::Acs::User:invalidLogonCnt-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonCnt")
	public published  Int getInvalidLogonCnt() {
		return invalidLogonCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonCnt")
	public published   void setInvalidLogonCnt(Int val) {
		invalidLogonCnt = val;
	}

	/*Radix::Acs::User:invalidLogonTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonTime")
	public published  java.sql.Timestamp getInvalidLogonTime() {
		return invalidLogonTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonTime")
	public published   void setInvalidLogonTime(java.sql.Timestamp val) {
		invalidLogonTime = val;
	}

	/*Radix::Acs::User:mustChangePwd-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mustChangePwd")
	public published  Bool getMustChangePwd() {
		return mustChangePwd;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mustChangePwd")
	public published   void setMustChangePwd(Bool val) {
		mustChangePwd = val;
	}

	/*Radix::Acs::User:autoLocked-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:autoLocked")
	public published  Bool getAutoLocked() {
		return autoLocked;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:autoLocked")
	public published   void setAutoLocked(Bool val) {
		autoLocked = val;
	}

	/*Radix::Acs::User:locked-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:locked")
	public published  Bool getLocked() {
		return locked;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:locked")
	public published   void setLocked(Bool val) {

		if(Boolean.TRUE.equals(val)){
		    lockReason = AccountLockReason:BY_ADMIN;
		}else{
		    lockReason = null;
		}
		internal[locked] = val;
	}

	/*Radix::Acs::User:totalLock-Dynamic Property*/



	protected Bool totalLock=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:totalLock")
	public published  Bool getTotalLock() {

		return locked != null && locked.booleanValue() 
		       || autoLocked != null && autoLocked.booleanValue() 
		       || isTemporaryPwdExpired!=null && isTemporaryPwdExpired.booleanValue();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:totalLock")
	public published   void setTotalLock(Bool val) {
		totalLock = val;
	}

	/*Radix::Acs::User:isMaySetNullForAdminGroup-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isMaySetNullForAdminGroup")
	public published  Bool getIsMaySetNullForAdminGroup() {
		return isMaySetNullForAdminGroup;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isMaySetNullForAdminGroup")
	public published   void setIsMaySetNullForAdminGroup(Bool val) {
		isMaySetNullForAdminGroup = val;
	}

	/*Radix::Acs::User:pwdHashHistory-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashHistory")
	public published  org.radixware.kernel.common.types.ArrStr getPwdHashHistory() {
		return pwdHashHistory;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashHistory")
	public published   void setPwdHashHistory(org.radixware.kernel.common.types.ArrStr val) {
		pwdHashHistory = val;
	}

	/*Radix::Acs::User:dbTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:dbTraceProfile")
	public published  Str getDbTraceProfile() {
		return dbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:dbTraceProfile")
	public published   void setDbTraceProfile(Str val) {

		if (!PersistentModifiedPropIds.contains(idof[User:dbTraceProfile])) {
		    oldDbTraceProfile = dbTraceProfile;
		}
		internal[dbTraceProfile] = val;
	}

	/*Radix::Acs::User:oldDbTraceProfile-Dynamic Property*/



	protected Str oldDbTraceProfile=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:oldDbTraceProfile")
	protected published  Str getOldDbTraceProfile() {
		return oldDbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:oldDbTraceProfile")
	protected published   void setOldDbTraceProfile(Str val) {
		oldDbTraceProfile = val;
	}

	/*Radix::Acs::User:isDbTraceProfileModified-Dynamic Property*/



	protected boolean isDbTraceProfileModified=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isDbTraceProfileModified")
	protected published  boolean getIsDbTraceProfileModified() {
		return isDbTraceProfileModified;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isDbTraceProfileModified")
	protected published   void setIsDbTraceProfileModified(boolean val) {
		isDbTraceProfileModified = val;
	}

	/*Radix::Acs::User:traceGuiActions-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:traceGuiActions")
	public published  Bool getTraceGuiActions() {
		return traceGuiActions;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:traceGuiActions")
	public published   void setTraceGuiActions(Bool val) {
		traceGuiActions = val;
	}

	/*Radix::Acs::User:isGuiActivityTraceSwitched-Dynamic Property*/



	protected boolean isGuiActivityTraceSwitched=false;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isGuiActivityTraceSwitched")
	public  boolean getIsGuiActivityTraceSwitched() {
		return isGuiActivityTraceSwitched;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isGuiActivityTraceSwitched")
	public   void setIsGuiActivityTraceSwitched(boolean val) {
		isGuiActivityTraceSwitched = val;
	}

	/*Radix::Acs::User:authTypes-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:authTypes")
	public published  org.radixware.ads.Acs.common.AuthType.Arr getAuthTypes() {
		return authTypes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:authTypes")
	public published   void setAuthTypes(org.radixware.ads.Acs.common.AuthType.Arr val) {
		authTypes = val;
	}

	/*Radix::Acs::User:createTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:createTime")
	public published  java.sql.Timestamp getCreateTime() {
		return createTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:createTime")
	public published   void setCreateTime(java.sql.Timestamp val) {
		createTime = val;
	}

	/*Radix::Acs::User:hasNewRolesOrGroup-Dynamic Property*/



	protected Bool hasNewRolesOrGroup=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:hasNewRolesOrGroup")
	public  Bool getHasNewRolesOrGroup() {

		{
		    GetUserHasNewRoles rs = GetUserHasNewRoles.execute(this.name);

		    Int result = rs.hasNewRoles;
		    if (result != null && result.intValue() > 0){
		        return true;
		    }
		}
		GetUserHasNewGroups rs = GetUserHasNewGroups.execute(this.name);

		Int result = rs.hasNewGroups;
		if (result != null && result.intValue() > 0){
		    return true;
		}
		return false;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:hasNewRolesOrGroup")
	public   void setHasNewRolesOrGroup(Bool val) {
		hasNewRolesOrGroup = val;
	}

	/*Radix::Acs::User:usedDualControl-Dynamic Property*/



	protected Bool usedDualControl=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:usedDualControl")
	public published  Bool getUsedDualControl() {

		return getArte().getRights().getDualControlController().isUsedDualControlWhenAssigningRoles();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:usedDualControl")
	public published   void setUsedDualControl(Bool val) {
		usedDualControl = val;
	}

	/*Radix::Acs::User:logonTimeSchedule-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonTimeSchedule")
	public published  org.radixware.ads.Scheduling.server.IntervalSchedule getLogonTimeSchedule() {
		return logonTimeSchedule;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonTimeSchedule")
	public published   void setLogonTimeSchedule(org.radixware.ads.Scheduling.server.IntervalSchedule val) {
		logonTimeSchedule = val;
	}

	/*Radix::Acs::User:logonScheduleId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonScheduleId")
	public published  Int getLogonScheduleId() {
		return logonScheduleId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonScheduleId")
	public published   void setLogonScheduleId(Int val) {
		logonScheduleId = val;
	}

	/*Radix::Acs::User:sessionsLimit-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:sessionsLimit")
	public published  Int getSessionsLimit() {
		return sessionsLimit;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:sessionsLimit")
	public published   void setSessionsLimit(Int val) {
		sessionsLimit = val;
	}

	/*Radix::Acs::User:system1-Dynamic Property*/



	protected org.radixware.ads.System.server.System system1=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:system1")
	public final published  org.radixware.ads.System.server.System getSystem1() {

		return System::System.loadByPK(1,false);
	}

	/*Radix::Acs::User:curUserCanAccept-Dynamic Property*/



	protected Bool curUserCanAccept=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:curUserCanAccept")
	public published  Bool getCurUserCanAccept() {

		return getArte().getRights().getDualControlController().isCurUserCanAccept();
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:curUserCanAccept")
	public published   void setCurUserCanAccept(Bool val) {
		curUserCanAccept = val;
	}

	/*Radix::Acs::User:lastLogonTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastLogonTime")
	public published  java.sql.Timestamp getLastLogonTime() {
		return lastLogonTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastLogonTime")
	public published   void setLastLogonTime(java.sql.Timestamp val) {
		lastLogonTime = val;
	}

	/*Radix::Acs::User:lockReason-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lockReason")
	public  org.radixware.kernel.common.enums.EAccountLockReason getLockReason() {
		return lockReason;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lockReason")
	public   void setLockReason(org.radixware.kernel.common.enums.EAccountLockReason val) {
		lockReason = val;
	}

	/*Radix::Acs::User:finalLockReason-Dynamic Property*/



	protected org.radixware.kernel.common.enums.EAccountLockReason finalLockReason=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:finalLockReason")
	public  org.radixware.kernel.common.enums.EAccountLockReason getFinalLockReason() {

		if(lockReason != null){
		    return lockReason;
		}else{    
		    if(Boolean.TRUE.equals(locked) || Boolean.TRUE.equals(autoLocked)){
		        return AccountLockReason:CREDENTIALS;
		    }else if (Boolean.TRUE==isTemporaryPwdExpired){
		        return AccountLockReason:TMP_PWD_EXPIRED;
		    }else{
		        return null;
		    }
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:finalLockReason")
	public   void setFinalLockReason(org.radixware.kernel.common.enums.EAccountLockReason val) {
		finalLockReason = val;
	}

	/*Radix::Acs::User:passwordRequirements-Dynamic Property*/



	protected org.radixware.schemas.eas.PasswordRequirementsDocument passwordRequirements=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordRequirements")
	public published  org.radixware.schemas.eas.PasswordRequirementsDocument getPasswordRequirements() {

		if (internal[passwordRequirements]==null){
		    internal[passwordRequirements] = System::System.loadByPK(1, false).getPasswordRequirements();
		}
		return internal[passwordRequirements];
	}

	/*Radix::Acs::User:pwdHashBytes-Dynamic Property*/



	protected org.radixware.kernel.common.types.Bin pwdHashBytes=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashBytes")
	public  org.radixware.kernel.common.types.Bin getPwdHashBytes() {

		if (!this.isInDatabase(false))//is new object
		    return null;
		if (pwdHash==null)
		    return null;
		return new Bin(new byte[1]);
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashBytes")
	public   void setPwdHashBytes(org.radixware.kernel.common.types.Bin val) {

		final byte[] newVal = val==null ? null : val.get();
		if (newVal!=null && newVal.length==1){//dummy value from getter
		    return;
		}
		final Int len = System::System.loadByPK(1, false).uniquePwdSeqLen;
		ArrStr old = pwdHashHistory;

		if (old != null && !old.isEmpty() && getPersistentModifiedPropIds().contains(idof[User:pwdHashHistory])) {
		    //contains unsaved pwdHash
		    old.remove(0); //removing it (NEYVABANKRU-91)
		}

		final PasswordHash pwdHash = newVal==null ? null : PasswordHash.Factory.fromBytes(newVal);

		if (len != null && old != null && pwdHash != null){
		    for (int i = 0; i < len.longValue() && i < old.size(); i++){
		        final byte[] oldPwdHash = Utils::Hex.decode(old.get(i));
		        for (PasswordHash.Algorithm hashAlgorithm: PasswordHash.Algorithm.values()){
		            if (java.util.Arrays.equals(oldPwdHash, pwdHash.getBytes(hashAlgorithm))){
		                throw new InvalidPropertyValueError(idof[User], idof[User:passwordDefined], "New password matches the one used previously");
		            }
		        }
		    }
		}

		final ArrStr history;
		final Str pwdHashAsHexStr = pwdHash==null ? null : Utils::Hex.encode(pwdHash.getBytes(PasswordHash.DEFAULT_ALGORITHM));
		if (len == null || len.longValue()<1) {
		    history = null;
		}else{
		    if (pwdHashAsHexStr==null){
		        history = old;
		    }else{
		        history = new ArrStr(pwdHashAsHexStr);
		        if (old != null) {
		            for (int i = 0; i < len.longValue()-1 && i < old.size(); i++){
		                history.add(old.get(i));
		            }
		        }
		    }
		}
		pwdHashHistory = history;
		pwdHash = pwdHashAsHexStr;
		pwdHashAlgo = PasswordHash.DEFAULT_ALGORITHM.Title;
	}

	/*Radix::Acs::User:encryptedPwdHash-Dynamic Property*/



	protected org.radixware.kernel.common.types.Bin encryptedPwdHash=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:encryptedPwdHash")
	public  org.radixware.kernel.common.types.Bin getEncryptedPwdHash() {
		return encryptedPwdHash;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:encryptedPwdHash")
	public   void setEncryptedPwdHash(org.radixware.kernel.common.types.Bin val) {
		encryptedPwdHash = val;
	}

	/*Radix::Acs::User:pwdHashAlgo-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashAlgo")
	public  Str getPwdHashAlgo() {
		return pwdHashAlgo;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashAlgo")
	public   void setPwdHashAlgo(Str val) {
		pwdHashAlgo = val;
	}

	/*Radix::Acs::User:systemPwdExpirationPeriod-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:systemPwdExpirationPeriod")
public published  Int getSystemPwdExpirationPeriod() {
	return systemPwdExpirationPeriod;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:systemPwdExpirationPeriod")
public published   void setSystemPwdExpirationPeriod(Int val) {
	systemPwdExpirationPeriod = val;
}

/*Radix::Acs::User:temporaryPwdStartTime-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:temporaryPwdStartTime")
public published  java.sql.Timestamp getTemporaryPwdStartTime() {
	return temporaryPwdStartTime;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:temporaryPwdStartTime")
public published   void setTemporaryPwdStartTime(java.sql.Timestamp val) {
	temporaryPwdStartTime = val;
}

/*Radix::Acs::User:isTemporaryPwdExpired-Expression Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isTemporaryPwdExpired")
public published  Bool getIsTemporaryPwdExpired() {
	return isTemporaryPwdExpired;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isTemporaryPwdExpired")
public published   void setIsTemporaryPwdExpired(Bool val) {
	isTemporaryPwdExpired = val;
}























































































































































































































































































/*Radix::Acs::User:Methods-Methods*/

/*Radix::Acs::User:unLock-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:unLock")
  void unLock (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	this.invalidLogonCnt = 0;
	this.locked = false;
	if (isTemporaryPwdExpired==Bool.TRUE){
	    temporaryPwdStartTime = Arte::Arte.getDatabaseSysDate();
	}
}

/*Radix::Acs::User:moveRightsToGroup-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:moveRightsToGroup")
public published  org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument moveRightsToGroup (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	Str val = input.StrValue;
	CommandsXsd:StrValueDocument xOut = CommandsXsd:StrValueDocument.Factory.newInstance();        

	if (this.Arte.Rights.isUserGroupExists(val))
	{
	    xOut.StrValue = "User group already exists.";
	    return xOut;
	}

	if (!getArte().Rights.isUserHaveOwnRights(name))
	{
	    xOut.StrValue = "Own rights for this user not defined.";
	    return xOut;
	}

	getArte().Rights.moveRightsFromUserToGroup(name, val);
	   
	xOut.StrValue = "";
	return xOut;
}

/*Radix::Acs::User:loadByPidStr-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:loadByPidStr")
public static  org.radixware.ads.Acs.server.User loadByPidStr (Str pidAsStr, boolean checkExistance) {
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),pidAsStr);
	try{
	return (
	org.radixware.ads.Acs.server.User) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::Acs::User:loadByPK-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:loadByPK")
public static published  org.radixware.ads.Acs.server.User loadByPK (Str name, boolean checkExistance) {
final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
		if(name==null) return null;
		pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),name);
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),pkValsMap);
	try{
	return (
	org.radixware.ads.Acs.server.User) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::Acs::User:beforeDelete-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:beforeDelete")
protected published  boolean beforeDelete () {
	if (!getArte().Rights.isCurUserHaveUserRights(name)){
	   throw new ServiceProcessClientFault(ExceptionEnum.ACCESS_VIOLATION.toString(), "Attempt to revoke rights that you cannot assign", null, null);
	} 
	return super.beforeDelete();
}

/*Radix::Acs::User:afterUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:afterUpdate")
protected published  void afterUpdate () {
	super.afterUpdate();
	afterUpdateForRadix();
}

/*Radix::Acs::User:beforeUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:beforeUpdate")
protected published  boolean beforeUpdate () {
	beforeUpdateForRadix();
	return super.beforeUpdate();
}

/*Radix::Acs::User:onCommand_GenerateReport-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:onCommand_GenerateReport")
public  void onCommand_GenerateReport (org.radixware.schemas.reports.GenerateReportRqDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	Reports::ReportsServerUtils.generateReport(this, input);
}

/*Radix::Acs::User:afterCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:afterCreate")
protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
	super.afterCreate(src);

	if (src!=null){    
	    Str srcUser =  src.getProp(idof[User:name]).toString();
	    Str destUser =  this.name;
	    
	    if (!Arte.Rights.isCurUserHaveUserRights(srcUser)){
	        throw new InvalidEasRequestClientFault("Attempt to assign exceeding rights");
	    }
	    {
	        GetUser2RolesByUser cursor = GetUser2RolesByUser.open(srcUser);
	        try {

	            while (cursor.next()) {
	                User2Role srcUser2Role = User2Role.loadByPK(cursor.id, true);
	                if (srcUser2Role.isOwn.booleanValue()){
	                    User2Role newUser2Role = new User2Role();                    
	                    newUser2Role.init(null, srcUser2Role);
	                    newUser2Role.userName = destUser;        
	                    newUser2Role.create(srcUser2Role); 
	                }
	            }
	        } finally {
	            cursor.close();
	        }
	    }
	    {
	        GetGroupsByUser cursor = GetGroupsByUser.open(srcUser);
	        try {

	            while (cursor.next()) {
	                User2UserGroup newUser2UserGroup = new User2UserGroup();
	                newUser2UserGroup.init();
	                newUser2UserGroup.userName = destUser;        
	                newUser2UserGroup.groupName = cursor.groupName;
	                newUser2UserGroup.create();
	            }
	        } finally {
	            cursor.close();
	        }
	    }
	    {
	        GetStationsByUser cursor = GetStationsByUser.open(srcUser);
	        try {

	            while (cursor.next()) {
	                User2Station newUser2Station = new User2Station();
	                newUser2Station.init();
	                newUser2Station.userName = destUser;        
	                newUser2Station.stationName = cursor.stationName;
	                newUser2Station.create();
	            }
	        } finally {
	            cursor.close();
	        }
	    }    
	    
	}
}

/*Radix::Acs::User:acceptRoles-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:acceptRoles")
public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument acceptRoles (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	return AcsUtils.acceptedUserOrGroup(true, name);


}

/*Radix::Acs::User:beforeAccept-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:beforeAccept")
public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument beforeAccept () {
	final Str name = name;

	final CommandsXsd:AcceptedRightsCheckValueDocument rez =  CommandsXsd:AcceptedRightsCheckValueDocument.Factory.newInstance();

	final Types::Reference<java.lang.Integer> acceptedRoles = new Types::Reference<java.lang.Integer>();
	final Types::Reference<java.lang.Integer> unacceptedRoles = new Types::Reference<java.lang.Integer>();

	final Types::Reference<java.lang.Integer> acceptedUser2Group = new Types::Reference<java.lang.Integer>();
	final Types::Reference<java.lang.Integer> unacceptedUser2Group = new Types::Reference<java.lang.Integer>();

	getArte().getRights().getDualControlController().getRolesCountForUser(name, acceptedRoles, unacceptedRoles , acceptedUser2Group, unacceptedUser2Group );


	rez.AcceptedRightsCheckValue = CommandsXsd:AcceptedRightsCheckValueDocument.AcceptedRightsCheckValue.Factory.newInstance();
	rez.AcceptedRightsCheckValue.AcceptedRoles = acceptedRoles.get().intValue();
	rez.AcceptedRightsCheckValue.UnacceptedRoles = unacceptedRoles.get().intValue();

	rez.AcceptedRightsCheckValue.AcceptedUser2Group = acceptedUser2Group.get().intValue();
	rez.AcceptedRightsCheckValue.UnacceptedUser2Group = unacceptedUser2Group.get().intValue();

	return rez;
}

/*Radix::Acs::User:beforeCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:beforeCreate")
protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
	checkPwdExpirationPeriod();
	lastLogonTime = new DateTime(System.currentTimeMillis());
	decryptPwdHash();
	if (pwdHash!=null){
	    lastPwdChangeTime = Arte::Arte.getDatabaseSysDate();
	}
	temporaryPwdStartTime = mustChangePwd==Bool.TRUE ? Arte::Arte.getDatabaseSysDate() : null;
	if (name != null) {
	    name = name.trim();
	}
	return super.beforeCreate(src);
}

/*Radix::Acs::User:decryptPwdHash-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:decryptPwdHash")
protected final published  void decryptPwdHash () {
	if (encryptedPwdHash!=null && getArte().isEasSessionKeyAccessible()){
	    final byte[] decryptedHash = getArte().decryptByEasSessionKey(encryptedPwdHash.get());
	    pwdHashBytes = Bin.wrap(decryptedHash);
	    encryptedPwdHash = null;
	}
}

/*Radix::Acs::User:checkPwdExpirationPeriod-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkPwdExpirationPeriod")
private final  void checkPwdExpirationPeriod () {
	System::System system = System::System.loadByPK(1, true);
	if(system.pwdExpirationPeriod != null && pwdExpirationPeriod != null && pwdExpirationPeriod.longValue() > system.pwdExpirationPeriod.longValue()) {
	    throw new InvalidPropertyValueError(idof[User], idof[User:pwdExpirationPeriod], String.format("Validity period of user password cannot exceed global value specified in system settings (%s).", system.pwdExpirationPeriod));
	}
}

/*Radix::Acs::User:afterInit-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:afterInit")
protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
	super.afterInit(src, phase);
	if (phase==Types::EntityInitializationPhase:TEMPLATE_PREPARATION
	    && systemPwdExpirationPeriod!=null){
	    pwdExpirationPeriod = systemPwdExpirationPeriod;
	}
}

/*Radix::Acs::User:copyPropVal-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:copyPropVal")
public published  void copyPropVal (org.radixware.kernel.server.meta.clazzes.RadPropDef prop, org.radixware.kernel.server.types.Entity src) {
	final java.util.Collection<Types::Id> ignoreCopyVal = new java.util.LinkedList<>();
	ignoreCopyVal.add(idof[User:encryptedPwdHash]);
	ignoreCopyVal.add(idof[User:invalidLogonCnt]);
	ignoreCopyVal.add(idof[User:invalidLogonTime]);
	ignoreCopyVal.add(idof[User:lastLogonTime]);
	ignoreCopyVal.add(idof[User:lastPwdChangeTime]);
	ignoreCopyVal.add(idof[User:lockReason]);
	ignoreCopyVal.add(idof[User:passwordDefined]);
	ignoreCopyVal.add(idof[User:pwdHash]);
	ignoreCopyVal.add(idof[User:pwdHashAlgo]);
	ignoreCopyVal.add(idof[User:pwdHashBytes]);
	ignoreCopyVal.add(idof[User:pwdHashHistory]);
	ignoreCopyVal.add(idof[User:temporaryPwdStartTime]);
	if (!ignoreCopyVal.contains(prop.getId())){
	    super.copyPropVal(prop, src);
	}
}

/*Radix::Acs::User:afterUpdateForRadix-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:afterUpdateForRadix")
public published  void afterUpdateForRadix () {
	if (isDbTraceProfileModified) {
	    Arte::Trace.put(eventCode["User '%1' modified database trace profile for user '%2'. Old value: '%3'. New value: '%4'"],
	            new ArrStr(Arte::Arte.getUserName(), name, oldDbTraceProfile, dbTraceProfile));
	}
	if (isGuiActivityTraceSwitched
	        && (traceGuiActions == null || !traceGuiActions.booleanValue())) {
	    Arte::Trace.put(eventCode["User '%1' disabled GUI activity tracing for user '%2'"],
	            new ArrStr(Arte::Arte.getUserName(), name));
	}
}

/*Radix::Acs::User:beforeUpdateForRadix-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:beforeUpdateForRadix")
public published  void beforeUpdateForRadix () {
	isDbTraceProfileModified = 
	    PersistentModifiedPropIds.contains(idof[User:dbTraceProfile]) &&
	    (dbTraceProfile != oldDbTraceProfile);
	isGuiActivityTraceSwitched = PersistentModifiedPropIds.contains(idof[User:traceGuiActions]);
	checkPwdExpirationPeriod();
	if (PersistentModifiedPropIds.contains(idof[User:pwdHash])) {
	    lastPwdChangeTime = Arte::Arte.getDatabaseSysDate();
	}
	if (PersistentModifiedPropIds.contains(idof[User:mustChangePwd])){
	    temporaryPwdStartTime = mustChangePwd==Bool.TRUE ? Arte::Arte.getDatabaseSysDate() : null;
	}
	decryptPwdHash();
}







@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
	if(cmdId == cmd7WZ4L4SL65HVXAAB7VADERPDJM){
		onCommand_GenerateReport((org.radixware.schemas.reports.GenerateReportRqDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.reports.GenerateReportRqDocument.class),newPropValsById);
		return null;
	} else if(cmdId == cmdBCNKLHK2C5CNLAOPP44HLQEDXI){
		final org.radixware.schemas.utils.RPCResponseDocument result =  rpc_callcmdBCNKLHK2C5CNLAOPP44HLQEDXI_implementation((org.radixware.schemas.utils.RPCRequestDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.utils.RPCRequestDocument.class));
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdMXS7QC4BQJED7CM4TKDWTPOLUM){
		org.radixware.ads.Acs.common.CommandsXsd.AcceptedRolesDocument result = acceptRoles(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdSNYYAJ37T5G65ESWDW6CHEX64Q){
		org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument result = moveRightsToGroup((org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class),newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdV3R6LGIKURC2BCD7HW4YW4TOXM){
		unLock(newPropValsById);
		return null;
	} else 
		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}
private final org.radixware.schemas.utils.RPCResponseDocument rpc_callcmdBCNKLHK2C5CNLAOPP44HLQEDXI_implementation(org.radixware.schemas.utils.RPCRequestDocument input){
	final org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation invoker = new org.radixware.kernel.common.utils.RPCProcessor.ServerSideInvocation(input){
		@Override
		protected  org.radixware.kernel.common.enums.EValType getReturnType(){
			return org.radixware.kernel.common.enums.EValType.XML;
		}
		protected  Object invokeImpl(Object[] arguments){
			Object $res$ =org.radixware.ads.Acs.server.User.this.mth2KCYAI5Y3NEUHJZGJFJSDIOVHU();
			return $res$;
		}
	};
	return invoker.invoke();
}


}

/* Radix::Acs::User - Server Meta*/

/*Radix::Acs::User-Entity Class*/

package org.radixware.ads.Acs.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"User",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Acs::User:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
							/*Owner Class Name*/
							"User",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),
							/*Property presentations*/

							/*Radix::Acs::User:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Acs::User:name:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:checkStation:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRC2TN7IYY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:email:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:lastPwdChangeTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPUPBTVQYY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:mobilePhone:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:personName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:pwdExpirationPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XNCT3QYY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:adminGroup:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHSW2Y4C4YJF4NLVE5A3F62ZY6U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfnPYKLHTXRWVA65MUJ2UTJ2NMKNE\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecQ23AYDTTGLNRDHRZABQAQH3XQ4\" PropId=\"colV2XEAGILY3NBDGMCABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>)=1</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Acs::User:passwordDefined:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6IFAQA7VIFF2TK56SOTVEJR6GI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:invalidLogonCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZIYRXEXNJFLFIZGTXRUSIM4AE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:invalidLogonTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNBBMGSDE5BF5FODR4F5TE2UAD4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:mustChangePwd:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANJQ6ZDT6FALRCPTTSR3UUR5VQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:autoLocked:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:locked:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:totalLock:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:isMaySetNullForAdminGroup:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLIAZG6JPFEGFFGAOORNELFSCA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:dbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3G4FLB6M6FBVTL3BZLIJUG2DII"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:oldDbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBNGCOGAZINEMZENFUJT546WWYI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:traceGuiActions:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37HDYBNIXJAHFBGS6QUSHOFH2U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:authTypes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZ5HBUOKRVBUXB5LM7WNIBBFP4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:createTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5GRW4H5KNDC7PHXE7Y52C7KUM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:hasNewRolesOrGroup:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7EWBCN4XXFCDRCQYPUYBNHH25E"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:usedDualControl:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:logonTimeSchedule:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY45VD62NWJD3FKO22HTQTVXY7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3668986,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprDBH5KY2HJLOBDCISAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Acs::User:sessionsLimit:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWVM572KI6JELDKJ7OB6SWDMUJY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:curUserCanAccept:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:lastLogonTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNF6GYZT3FVFLLKTBIOC7F7IN3A"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:lockReason:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FODGXNBXBCM5MJZ4D5FPOY2MI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:finalLockReason:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:passwordRequirements:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJGY5G5O23VG27HRRWMZD223LPY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:pwdHashBytes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:encryptedPwdHash:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:systemPwdExpirationPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colERICYL54QBAKFH6PABOXUBRHR4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::Acs::User:temporaryPwdStartTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3BCFXYVBNH65NBW7YHFNUX7BA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Acs::User:isTemporaryPwdExpired:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User:Unlock-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV3R6LGIKURC2BCD7HW4YW4TOXM"),"Unlock",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User:MoveRightsToGroup-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdSNYYAJ37T5G65ESWDW6CHEX64Q"),"MoveRightsToGroup",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User:AcceptRights-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMXS7QC4BQJED7CM4TKDWTPOLUM"),"AcceptRights",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User:GenerateReport-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7WZ4L4SL65HVXAAB7VADERPDJM"),"GenerateReport",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Acs::User:BeforeAccept-Remote Call Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdBCNKLHK2C5CNLAOPP44HLQEDXI"),"BeforeAccept",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Acs::User:Name-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"Name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HZX77BMPJC7ZCL4QX4ZYD53ME"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Acs::User:PersonName-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"PersonName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSZRWJD3EFGVZFYW7LHL2ZNMIQ"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Acs::User:Email-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"Email",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XWDYKW2TJHJNEUJOJ5KATMYZI"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Acs::User:Phone-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"Phone",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZXFF3JXBNFN7DN5MP7VCQVDSA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Acs::User:Name-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltXDV2WXUPKRA6LPQ4VPQ5WOXCQM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"Name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYARASEC3WJEORB6ZRKBEWA4TWY"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colEF6ODCYWY3NBDGMCABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmAQE3EICYKVD73LK744QJXSXOWM\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),false,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Acs::User:PersonName-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltSRG6OBEIWFB6HO3O55DYMRLDEY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"PersonName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3UEP4UMOWRDW5KFJLH3FBDWO6U"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"col3FYUIAY5J3NRDJIRACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMLERCKVL4BFCHE36AB3M2776QI\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),true,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Acs::User:Email-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBTTJNCPNR5DADH4XGHF23GLUSI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"Email",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54XSYWLOMNAWXEXAHAVIWCRDK4"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colWXQAHXB5V3OBDCLVAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmJ3OGIPSYIVFEHMEIOJ7FQK72KE\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),true,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),"org.radixware")
									},true,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Acs::User:Phone-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltP2KKOGX34BGZJONNDRIFHXC7WI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"Phone",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54S4Z4LOPBEI5KAYCE6SNEK2UI"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colW3QAHXB5V3OBDCLVAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVYWMK4QDVEQFFGPIVHZ3GJHH4\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),true,new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting[]{

											new org.radixware.kernel.server.meta.presentations.RadFilterDef.EnabledSorting("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),"org.radixware")
									},true,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Acs::User:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Acs::User:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Acs::User:General:User2Role-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4B7AJF4FJVBLNM4H6SXJX3PY6Q"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2ZDQ5EV2J3NRDAQSABIFNQAAAE"),
													org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
													null),

												/*Radix::Acs::User:General:User2UserGroup-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi5D6C6BTS6VHYZCQL2MVYRTMVXI"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecDYWJCJTTGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKSID2LQ4PBAEJEM4W7FEDEL2FQ"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2URGD352J3NRDAQSABIFNQAAAE"),
													null,
													null),

												/*Radix::Acs::User:General:UserGroup2Role-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiKLLWEBFDWBG7PKSVW53WWQXZTM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfnB2R5G7DU4NBCTFON7ZRHCC2724\"/></xsc:Item><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colEF6ODCYWY3NBDGMCABQAQH3XQ4\" Owner=\"PARENT\"/></xsc:Item><xsc:Item><xsc:Sql>, </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colXDHHLFZRY3NBDGMCABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> )=1 and </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblFJAEQT3TGLNRDHRZABQAQH3XQ4\" PropId=\"colC3327E6VZNB3RBSQSYPLWSO5KI\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=0</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.server.types.Restrictions.Factory.newInstance(19495,null,null,null),
													null),

												/*Radix::Acs::User:General:User2Station-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiSB3NZZPD6ZHNJFMWWR6IGNLKIQ"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGPFDUXBHJ3NRDJIRACQMTAIZT4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNK2VXHAQTNA4FMLHOEF7XIWSVU"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refGLC46EW4KHNRDAQSABIFNQAAAE"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Acs::User:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(104,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Acs::User:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Acs::User:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Acs::User:name-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),"name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJ53S5PYRZGZTBTMFRE77NEL74"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:adminGroupName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNMVAZDYYY3NBDGMCABQAQH3XQ4"),"adminGroupName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOFXVHTPXRAL5ITLFXFU3APE4Y"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:checkStation-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRC2TN7IYY3NBDGMCABQAQH3XQ4"),"checkStation",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTC6DSITNBAVPNVASWEML6FPZ4"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:email-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),"email",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSY7MTF23ZE57K66FTROSC2QJU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:lastPwdChangeTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPUPBTVQYY3NBDGMCABQAQH3XQ4"),"lastPwdChangeTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSOVIMNXICJG4ZAARHZLVX3XZKY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:mobilePhone-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),"mobilePhone",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT4QJIFPKKZHEJDR2RGRUDSAA2Q"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:personName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),"personName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGULLAHHBJHK5PXLSFJ25Y44U4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:pwdExpirationPeriod-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XNCT3QYY3NBDGMCABQAQH3XQ4"),"pwdExpirationPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFG5FSDDB5GTLKYGGWVWKXFG4M"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("90")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:pwdHash-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col53PYQMVBCDNRDHQQABQAQH3XQ4"),"pwdHash",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYM22C2JIBJHWBKBN6JHQWAGLXM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:adminGroup-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHSW2Y4C4YJF4NLVE5A3F62ZY6U"),"adminGroup",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PPPOAZDJVDVHC7HMI2UTJZW7Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refL5DHK752J3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:passwordDefined-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6IFAQA7VIFF2TK56SOTVEJR6GI"),"passwordDefined",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGM5S2KDHJAARLOLGQIFNO4BIA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:invalidLogonCnt-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZIYRXEXNJFLFIZGTXRUSIM4AE"),"invalidLogonCnt",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:invalidLogonTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNBBMGSDE5BF5FODR4F5TE2UAD4"),"invalidLogonTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCLRYTJC35E4NBJNHNMVFKPWMA"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:mustChangePwd-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANJQ6ZDT6FALRCPTTSR3UUR5VQ"),"mustChangePwd",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZX6MH575FA2TPPKQT67VFTQUQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:autoLocked-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),"autoLocked",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5PY3P6I5CNDJVGS77HWIBXLI6A"),org.radixware.kernel.common.enums.EValType.BOOL,"NUMBER(1,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>decode\n(\n (\n select 1 from dual where \n  </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colJZIYRXEXNJFLFIZGTXRUSIM4AE\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> >= (select </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"colDCLAU6NMGVDSNKN7ZPB6SQJE5Y\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblX5TD7JDVVHWDBROXAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"col27EXNSNZRHWDBRCXAAIT4AGD7E\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = 1)\n and\n  SYSDATE &lt; </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colNBBMGSDE5BF5FODR4F5TE2UAD4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> + \n   (select (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"colQTEXFSBLXJES3AOB6SXXKNGQQE\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>/24)/60 from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblX5TD7JDVVHWDBROXAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql>  where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"col27EXNSNZRHWDBRCXAAIT4AGD7E\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = 1)\n ),\n1,\n1,\n0    \n)\n</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:locked-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),"locked",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ERS2ARWXVBU5K5VP4J3GBAQJM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:totalLock-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),"totalLock",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYSFNKAXJVBWLEUW5JQUD5AZPU"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:isMaySetNullForAdminGroup-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLIAZG6JPFEGFFGAOORNELFSCA"),"isMaySetNullForAdminGroup",null,org.radixware.kernel.common.enums.EValType.BOOL,"NUMBER(1,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfnQMOYAWHOVRGPTOVAKI5N54BHMQ\"/></xsc:Item><xsc:Item><xsc:Sql>()</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:pwdHashHistory-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYVFZTZ7ZJBAKBIB77ZUF2OYSGY"),"pwdHashHistory",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:dbTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3G4FLB6M6FBVTL3BZLIJUG2DII"),"dbTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWHL2HEZZBADZE3RMHNFGRBNLY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:oldDbTraceProfile-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBNGCOGAZINEMZENFUJT546WWYI"),"oldDbTraceProfile",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:isDbTraceProfileModified-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdI4S6XAMF2RHK5LXSSBC6ORHMKQ"),"isDbTraceProfileModified",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:traceGuiActions-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37HDYBNIXJAHFBGS6QUSHOFH2U"),"traceGuiActions",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY5T547WITND3FMF7GWOKZIVVYI"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:isGuiActivityTraceSwitched-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdASAZMDCREFBZZBMDP6IYWRVT5I"),"isGuiActivityTraceSwitched",null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:authTypes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZ5HBUOKRVBUXB5LM7WNIBBFP4"),"authTypes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4PMYISQIRCJJOZRGQFQTACGLQ"),org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOMXM7EAWVZBNJHWQBKZELLOYKM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:createTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5GRW4H5KNDC7PHXE7Y52C7KUM"),"createTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45GPZMNIMRCU7CZLDQQGSSYYHU"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:hasNewRolesOrGroup-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7EWBCN4XXFCDRCQYPUYBNHH25E"),"hasNewRolesOrGroup",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:usedDualControl-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),"usedDualControl",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:logonTimeSchedule-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY45VD62NWJD3FKO22HTQTVXY7E"),"logonTimeSchedule",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA4IPRR4J2RDO3HWDYS5X57F2S4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refQM4AVQRQWBAHVLH3GDT2F7CN7A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecZBS5MS2BI3OBDCIOAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:logonScheduleId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7GBPTNS6VFMFAYXSSQLWYJRPM"),"logonScheduleId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:sessionsLimit-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWVM572KI6JELDKJ7OB6SWDMUJY"),"sessionsLimit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27NYQRCRRJEGXLPJQMUXDCSPYM"),org.radixware.kernel.common.enums.EValType.INT,null,true,org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4RWLEYFM35HJFK34SDC75BFGFY")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:system1-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4RWLEYFM35HJFK34SDC75BFGFY"),"system1",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:curUserCanAccept-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),"curUserCanAccept",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:lastLogonTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNF6GYZT3FVFLLKTBIOC7F7IN3A"),"lastLogonTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:lockReason-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FODGXNBXBCM5MJZ4D5FPOY2MI"),"lockReason",null,org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:finalLockReason-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"),"finalLockReason",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3G6WSF66BEQDHEZGJQYKOUPPA"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:passwordRequirements-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJGY5G5O23VG27HRRWMZD223LPY"),"passwordRequirements",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:pwdHashBytes-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M"),"pwdHashBytes",null,org.radixware.kernel.common.enums.EValType.BIN,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:encryptedPwdHash-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ"),"encryptedPwdHash",null,org.radixware.kernel.common.enums.EValType.BIN,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:pwdHashAlgo-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPMFRZB2GRF2LLE2QNMNOSTTTM"),"pwdHashAlgo",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("SHA-1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:systemPwdExpirationPeriod-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colERICYL54QBAKFH6PABOXUBRHR4"),"systemPwdExpirationPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2L6IOLO2NCOXN5NIWIVQXQUXY"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4RWLEYFM35HJFK34SDC75BFGFY")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:temporaryPwdStartTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3BCFXYVBNH65NBW7YHFNUX7BA"),"temporaryPwdStartTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCBTJMWRM5C77IPYDQ6FW3ESTU"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Acs::User:isTemporaryPwdExpired-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),"isTemporaryPwdExpired",null,org.radixware.kernel.common.enums.EValType.BOOL,"NUMBER(1,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>decode\n(\n (\n select 1 from dual where \n  </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colANJQ6ZDT6FALRCPTTSR3UUR5VQ\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> != 0\n and\n  (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colO3BCFXYVBNH65NBW7YHFNUX7BA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> is not NULL)\n and\n  (select (case when (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"col6EXIXL45PZEBZOF2MKFF7RJWSA\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>>0 and (</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colO3BCFXYVBNH65NBW7YHFNUX7BA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>+</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"col6EXIXL45PZEBZOF2MKFF7RJWSA\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql>/24 &lt;sysdate)) then 1 else 0 end)  from </xsc:Sql></xsc:Item><xsc:Item><xsc:TableSqlName TableId=\"tblX5TD7JDVVHWDBROXAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql> where </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecX5TD7JDVVHWDBROXAAIT4AGD7E\" PropId=\"col27EXNSNZRHWDBRCXAAIT4AGD7E\" Owner=\"NONE\"/></xsc:Item><xsc:Item><xsc:Sql> = 1) > 0\n ),\n1,\n1,\n0    \n)</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Acs::User:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdV3R6LGIKURC2BCD7HW4YW4TOXM"),"unLock",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWG2O5JHQNBHC3L34PV2UP3FDOA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdSNYYAJ37T5G65ESWDW6CHEX64Q"),"moveRightsToGroup",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQ6RWIWWRMJHWTCDDMEP37QNYTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCRSJ4QDYRRAJVOLJGCVG2PJNWM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprONZQZNH5QJB4PPRCEYN5B7LEXQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY7ZF5RWKQRDNBFD4BHTH5JQGEY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2KUOAZ7DGZGVJNGREFJ45GJEO4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7ECKNXPGCBDLHJKO3WA2YSU4WE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd7WZ4L4SL65HVXAAB7VADERPDJM"),"onCommand_GenerateReport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXVX3KFGULBH2VOTUK3FFPJVXJI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPP4GDUK65NCJDGR4RD6QAVH74E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYS6QLAA6BJF7PMCIBXJRJ3V5JA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdMXS7QC4BQJED7CM4TKDWTPOLUM"),"acceptRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNAAAOGX3VFFQLMOIG6WETATWU4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2KCYAI5Y3NEUHJZGJFJSDIOVHU"),"beforeAccept",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGDLMNDXVFFA7ZJTPL7L2UFMVZQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVBONH5YM2FC2LHRI66B3BZQRL4"),"decryptPwdHash",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5CAAIN6VK5H6TBZCJ7CN374HIA"),"checkPwdExpirationPeriod",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOY3V2ECV7NAS5FFIIWKPQNY2QY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr77QK4K5F7BDYPP73HUGVMCMTH4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2ID6LLB4ORG75JFEH45HYYK75U"),"copyPropVal",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("prop",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNHJQHT3GRNER7M7E34OYVB2HYA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMKBKDJK3DBBE3K27PRZELN5Q4E"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZW7GO5VMUZFEZIIVAJ56WNCAHA"),"afterUpdateForRadix",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHGNXQLQSPFBF5IMOEZ3PP4WINM"),"beforeUpdateForRadix",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.OWN,null,new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea[]{

								new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea(new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition[]{

										new org.radixware.kernel.server.meta.clazzes.RadClassAccessArea.Partition(org.radixware.kernel.common.types.Id.Factory.loadFrom("apf1ZOQHCO35XORDCV2AANE2UAFXA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("refL5DHK752J3NRDAQSABIFNQAAAE")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4"))
								})
						},false);
}

/* Radix::Acs::User - Desktop Executable*/

/*Radix::Acs::User-Entity Class*/

package org.radixware.ads.Acs.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User")
public interface User {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Acs.explorer.User.User_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.explorer.User.User_DefaultModel )  super.getEntity(i);}
	}






















































































































































































































	/*Radix::Acs::User:lockReason:lockReason-Presentation Property*/


	public class LockReason extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LockReason(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccountLockReason> getValClass(){
			return org.radixware.kernel.common.enums.EAccountLockReason.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lockReason:lockReason")
		public  org.radixware.kernel.common.enums.EAccountLockReason getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lockReason:lockReason")
		public   void setValue(org.radixware.kernel.common.enums.EAccountLockReason val) {
			Value = val;
		}
	}
	public LockReason getLockReason();
	/*Radix::Acs::User:traceGuiActions:traceGuiActions-Presentation Property*/


	public class TraceGuiActions extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public TraceGuiActions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:traceGuiActions:traceGuiActions")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:traceGuiActions:traceGuiActions")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public TraceGuiActions getTraceGuiActions();
	/*Radix::Acs::User:personName:personName-Presentation Property*/


	public class PersonName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PersonName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:personName:personName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:personName:personName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PersonName getPersonName();
	/*Radix::Acs::User:dbTraceProfile:dbTraceProfile-Presentation Property*/


	public class DbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::Acs::User:isTemporaryPwdExpired:isTemporaryPwdExpired-Presentation Property*/


	public class IsTemporaryPwdExpired extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsTemporaryPwdExpired(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isTemporaryPwdExpired:isTemporaryPwdExpired")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isTemporaryPwdExpired:isTemporaryPwdExpired")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsTemporaryPwdExpired getIsTemporaryPwdExpired();
	/*Radix::Acs::User:pwdExpirationPeriod:pwdExpirationPeriod-Presentation Property*/


	public class PwdExpirationPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdExpirationPeriod:pwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdExpirationPeriod:pwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdExpirationPeriod getPwdExpirationPeriod();
	/*Radix::Acs::User:mustChangePwd:mustChangePwd-Presentation Property*/


	public class MustChangePwd extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public MustChangePwd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mustChangePwd:mustChangePwd")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mustChangePwd:mustChangePwd")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public MustChangePwd getMustChangePwd();
	/*Radix::Acs::User:authTypes:authTypes-Presentation Property*/


	public class AuthTypes extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AuthTypes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Acs.common.AuthType.Arr> getValClass(){
			return org.radixware.ads.Acs.common.AuthType.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:authTypes:authTypes")
		public  org.radixware.ads.Acs.common.AuthType.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:authTypes:authTypes")
		public   void setValue(org.radixware.ads.Acs.common.AuthType.Arr val) {
			Value = val;
		}
	}
	public AuthTypes getAuthTypes();
	/*Radix::Acs::User:name:name-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::Acs::User:systemPwdExpirationPeriod:systemPwdExpirationPeriod-Presentation Property*/


	public class SystemPwdExpirationPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SystemPwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:systemPwdExpirationPeriod:systemPwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:systemPwdExpirationPeriod:systemPwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SystemPwdExpirationPeriod getSystemPwdExpirationPeriod();
	/*Radix::Acs::User:isMaySetNullForAdminGroup:isMaySetNullForAdminGroup-Presentation Property*/


	public class IsMaySetNullForAdminGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsMaySetNullForAdminGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isMaySetNullForAdminGroup:isMaySetNullForAdminGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isMaySetNullForAdminGroup:isMaySetNullForAdminGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMaySetNullForAdminGroup getIsMaySetNullForAdminGroup();
	/*Radix::Acs::User:adminGroup:adminGroup-Presentation Property*/


	public class AdminGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public AdminGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroup:adminGroup")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroup:adminGroup")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public AdminGroup getAdminGroup();
	/*Radix::Acs::User:invalidLogonCnt:invalidLogonCnt-Presentation Property*/


	public class InvalidLogonCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InvalidLogonCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonCnt:invalidLogonCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonCnt:invalidLogonCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InvalidLogonCnt getInvalidLogonCnt();
	/*Radix::Acs::User:invalidLogonTime:invalidLogonTime-Presentation Property*/


	public class InvalidLogonTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public InvalidLogonTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonTime:invalidLogonTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonTime:invalidLogonTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public InvalidLogonTime getInvalidLogonTime();
	/*Radix::Acs::User:lastLogonTime:lastLogonTime-Presentation Property*/


	public class LastLogonTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastLogonTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastLogonTime:lastLogonTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastLogonTime:lastLogonTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastLogonTime getLastLogonTime();
	/*Radix::Acs::User:temporaryPwdStartTime:temporaryPwdStartTime-Presentation Property*/


	public class TemporaryPwdStartTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public TemporaryPwdStartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:temporaryPwdStartTime:temporaryPwdStartTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:temporaryPwdStartTime:temporaryPwdStartTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public TemporaryPwdStartTime getTemporaryPwdStartTime();
	/*Radix::Acs::User:lastPwdChangeTime:lastPwdChangeTime-Presentation Property*/


	public class LastPwdChangeTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastPwdChangeTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastPwdChangeTime:lastPwdChangeTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastPwdChangeTime:lastPwdChangeTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastPwdChangeTime getLastPwdChangeTime();
	/*Radix::Acs::User:createTime:createTime-Presentation Property*/


	public class CreateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public CreateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::Acs::User:locked:locked-Presentation Property*/


	public class Locked extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Locked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:locked:locked")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:locked:locked")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Locked getLocked();
	/*Radix::Acs::User:checkStation:checkStation-Presentation Property*/


	public class CheckStation extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CheckStation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkStation:checkStation")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkStation:checkStation")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CheckStation getCheckStation();
	/*Radix::Acs::User:mobilePhone:mobilePhone-Presentation Property*/


	public class MobilePhone extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MobilePhone(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mobilePhone:mobilePhone")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mobilePhone:mobilePhone")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MobilePhone getMobilePhone();
	/*Radix::Acs::User:autoLocked:autoLocked-Presentation Property*/


	public class AutoLocked extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AutoLocked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:autoLocked:autoLocked")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:autoLocked:autoLocked")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AutoLocked getAutoLocked();
	/*Radix::Acs::User:sessionsLimit:sessionsLimit-Presentation Property*/


	public class SessionsLimit extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SessionsLimit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:sessionsLimit:sessionsLimit")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:sessionsLimit:sessionsLimit")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SessionsLimit getSessionsLimit();
	/*Radix::Acs::User:email:email-Presentation Property*/


	public class Email extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Email(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:email:email")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:email:email")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Email getEmail();
	/*Radix::Acs::User:logonTimeSchedule:logonTimeSchedule-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonTimeSchedule:logonTimeSchedule")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonTimeSchedule:logonTimeSchedule")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LogonTimeSchedule getLogonTimeSchedule();
	/*Radix::Acs::User:encryptedPwdHash:encryptedPwdHash-Presentation Property*/


	public class EncryptedPwdHash extends org.radixware.kernel.common.client.models.items.properties.PropertyBin{
		public EncryptedPwdHash(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:encryptedPwdHash:encryptedPwdHash")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:encryptedPwdHash:encryptedPwdHash")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public EncryptedPwdHash getEncryptedPwdHash();
	/*Radix::Acs::User:passwordDefined:passwordDefined-Presentation Property*/


	public class PasswordDefined extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PasswordDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordDefined:passwordDefined")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordDefined:passwordDefined")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PasswordDefined getPasswordDefined();
	/*Radix::Acs::User:hasNewRolesOrGroup:hasNewRolesOrGroup-Presentation Property*/


	public class HasNewRolesOrGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasNewRolesOrGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:hasNewRolesOrGroup:hasNewRolesOrGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:hasNewRolesOrGroup:hasNewRolesOrGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasNewRolesOrGroup getHasNewRolesOrGroup();
	/*Radix::Acs::User:oldDbTraceProfile:oldDbTraceProfile-Presentation Property*/


	public class OldDbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OldDbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:oldDbTraceProfile:oldDbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:oldDbTraceProfile:oldDbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OldDbTraceProfile getOldDbTraceProfile();
	/*Radix::Acs::User:curUserCanAccept:curUserCanAccept-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:curUserCanAccept:curUserCanAccept")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:curUserCanAccept:curUserCanAccept")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CurUserCanAccept getCurUserCanAccept();
	/*Radix::Acs::User:usedDualControl:usedDualControl-Presentation Property*/


	public class UsedDualControl extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UsedDualControl(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:usedDualControl:usedDualControl")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:usedDualControl:usedDualControl")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UsedDualControl getUsedDualControl();
	/*Radix::Acs::User:passwordRequirements:passwordRequirements-Presentation Property*/


	public class PasswordRequirements extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public PasswordRequirements(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.eas.PasswordRequirementsDocument> getValClass(){
			return org.radixware.schemas.eas.PasswordRequirementsDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.eas.PasswordRequirementsDocument dummy = x == null ? null : (org.radixware.schemas.eas.PasswordRequirementsDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.eas.PasswordRequirementsDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordRequirements:passwordRequirements")
		public  org.radixware.schemas.eas.PasswordRequirementsDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordRequirements:passwordRequirements")
		public   void setValue(org.radixware.schemas.eas.PasswordRequirementsDocument val) {
			Value = val;
		}
	}
	public PasswordRequirements getPasswordRequirements();
	/*Radix::Acs::User:totalLock:totalLock-Presentation Property*/


	public class TotalLock extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public TotalLock(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:totalLock:totalLock")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:totalLock:totalLock")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public TotalLock getTotalLock();
	/*Radix::Acs::User:finalLockReason:finalLockReason-Presentation Property*/


	public class FinalLockReason extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FinalLockReason(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccountLockReason> getValClass(){
			return org.radixware.kernel.common.enums.EAccountLockReason.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:finalLockReason:finalLockReason")
		public  org.radixware.kernel.common.enums.EAccountLockReason getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:finalLockReason:finalLockReason")
		public   void setValue(org.radixware.kernel.common.enums.EAccountLockReason val) {
			Value = val;
		}
	}
	public FinalLockReason getFinalLockReason();
	/*Radix::Acs::User:pwdHashBytes:pwdHashBytes-Presentation Property*/


	public class PwdHashBytes extends org.radixware.kernel.common.client.models.items.properties.PropertyBin{
		public PwdHashBytes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashBytes:pwdHashBytes")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashBytes:pwdHashBytes")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public PwdHashBytes getPwdHashBytes();
	public static class GenerateReport extends org.radixware.kernel.common.client.models.items.Command{
		protected GenerateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

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

	public static class MoveRightsToGroup extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveRightsToGroup(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument send(org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class);
		}

	}

	public static class Unlock extends org.radixware.kernel.common.client.models.items.Command{
		protected Unlock(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Acs::User - Desktop Meta*/

/*Radix::Acs::User-Entity Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::User:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::User",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF3YUCJZIIRDFPARXX53HDOSLLA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),0,

			/*Radix::Acs::User:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::User:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJ53S5PYRZGZTBTMFRE77NEL74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:checkStation:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRC2TN7IYY3NBDGMCABQAQH3XQ4"),
						"checkStation",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTC6DSITNBAVPNVASWEML6FPZ4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:checkStation:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:email:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),
						"email",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSY7MTF23ZE57K66FTROSC2QJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:email:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:lastPwdChangeTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPUPBTVQYY3NBDGMCABQAQH3XQ4"),
						"lastPwdChangeTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSOVIMNXICJG4ZAARHZLVX3XZKY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:lastPwdChangeTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:mobilePhone:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),
						"mobilePhone",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT4QJIFPKKZHEJDR2RGRUDSAA2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:mobilePhone:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,20,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:personName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),
						"personName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGULLAHHBJHK5PXLSFJ25Y44U4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:personName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:pwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XNCT3QYY3NBDGMCABQAQH3XQ4"),
						"pwdExpirationPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFG5FSDDB5GTLKYGGWVWKXFG4M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("90"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:pwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCNTHEUMGW5EHHN4EON3ECL3AXE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:adminGroup:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHSW2Y4C4YJF4NLVE5A3F62ZY6U"),
						"adminGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PPPOAZDJVDVHC7HMI2UTJZW7Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},
						null,
						null,
						0,
						0,false),

					/*Radix::Acs::User:passwordDefined:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6IFAQA7VIFF2TK56SOTVEJR6GI"),
						"passwordDefined",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGM5S2KDHJAARLOLGQIFNO4BIA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeTEEXCO6SRNE4PHPI4VCV5YO5RE"),
						true,

						/*Radix::Acs::User:passwordDefined:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:invalidLogonCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZIYRXEXNJFLFIZGTXRUSIM4AE"),
						"invalidLogonCnt",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::Acs::User:invalidLogonCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:invalidLogonTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNBBMGSDE5BF5FODR4F5TE2UAD4"),
						"invalidLogonTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCLRYTJC35E4NBJNHNMVFKPWMA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:invalidLogonTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:mustChangePwd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANJQ6ZDT6FALRCPTTSR3UUR5VQ"),
						"mustChangePwd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZX6MH575FA2TPPKQT67VFTQUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:mustChangePwd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:autoLocked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),
						"autoLocked",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5PY3P6I5CNDJVGS77HWIBXLI6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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

						/*Radix::Acs::User:autoLocked:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:locked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),
						"locked",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ERS2ARWXVBU5K5VP4J3GBAQJM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:locked:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:totalLock:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),
						"totalLock",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYSFNKAXJVBWLEUW5JQUD5AZPU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:totalLock:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:isMaySetNullForAdminGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLIAZG6JPFEGFFGAOORNELFSCA"),
						"isMaySetNullForAdminGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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

						/*Radix::Acs::User:isMaySetNullForAdminGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3G4FLB6M6FBVTL3BZLIJUG2DII"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWHL2HEZZBADZE3RMHNFGRBNLY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::Acs::User:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:oldDbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBNGCOGAZINEMZENFUJT546WWYI"),
						"oldDbTraceProfile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:oldDbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:traceGuiActions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37HDYBNIXJAHFBGS6QUSHOFH2U"),
						"traceGuiActions",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY5T547WITND3FMF7GWOKZIVVYI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:traceGuiActions:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:authTypes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZ5HBUOKRVBUXB5LM7WNIBBFP4"),
						"authTypes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4PMYISQIRCJJOZRGQFQTACGLQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOMXM7EAWVZBNJHWQBKZELLOYKM"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:authTypes:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOMXM7EAWVZBNJHWQBKZELLOYKM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPRVITUOKNEEXBJMLGWWKYZBLM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMAGDPOMJ5NDHXBE3323TJJ6LF4"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5GRW4H5KNDC7PHXE7Y52C7KUM"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45GPZMNIMRCU7CZLDQQGSSYYHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:hasNewRolesOrGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7EWBCN4XXFCDRCQYPUYBNHH25E"),
						"hasNewRolesOrGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:hasNewRolesOrGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:usedDualControl:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),
						"usedDualControl",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:usedDualControl:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:logonTimeSchedule:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY45VD62NWJD3FKO22HTQTVXY7E"),
						"logonTimeSchedule",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA4IPRR4J2RDO3HWDYS5X57F2S4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGHGLDHI2RVCJLP63YAV343C6IA"),
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

					/*Radix::Acs::User:sessionsLimit:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWVM572KI6JELDKJ7OB6SWDMUJY"),
						"sessionsLimit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27NYQRCRRJEGXLPJQMUXDCSPYM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:sessionsLimit:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6WWZGHT4XVCDPPSR6X4IDGPLQA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:curUserCanAccept:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),
						"curUserCanAccept",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:curUserCanAccept:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:lastLogonTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNF6GYZT3FVFLLKTBIOC7F7IN3A"),
						"lastLogonTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:lastLogonTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:lockReason:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FODGXNBXBCM5MJZ4D5FPOY2MI"),
						"lockReason",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),
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

						/*Radix::Acs::User:lockReason:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:finalLockReason:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"),
						"finalLockReason",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3G6WSF66BEQDHEZGJQYKOUPPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),
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

						/*Radix::Acs::User:finalLockReason:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LM4P27AFVAAFAV3JHWRWJNCBI"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:passwordRequirements:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJGY5G5O23VG27HRRWMZD223LPY"),
						"passwordRequirements",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:pwdHashBytes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M"),
						"pwdHashBytes",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BIN,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:encryptedPwdHash:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ"),
						"encryptedPwdHash",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BIN,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:systemPwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colERICYL54QBAKFH6PABOXUBRHR4"),
						"systemPwdExpirationPeriod",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						61,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),
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

						/*Radix::Acs::User:systemPwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:temporaryPwdStartTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3BCFXYVBNH65NBW7YHFNUX7BA"),
						"temporaryPwdStartTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCBTJMWRM5C77IPYDQ6FW3ESTU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:temporaryPwdStartTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:isTemporaryPwdExpired:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),
						"isTemporaryPwdExpired",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:isTemporaryPwdExpired:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:Unlock-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV3R6LGIKURC2BCD7HW4YW4TOXM"),
						"Unlock",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHQC7AJB65GRJCYWWDLQWY2L6U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRGURYMOTERAO3OAQ72IMY67RAE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:MoveRightsToGroup-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdSNYYAJ37T5G65ESWDW6CHEX64Q"),
						"MoveRightsToGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGG3ZU2FH25BT3FF7WYRJ6UMCZY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img6DMBDLPNWNC5PCZLW5VXMTAS4M"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:AcceptRights-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMXS7QC4BQJED7CM4TKDWTPOLUM"),
						"AcceptRights",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5CI2XOXI5GKPLD4XKHGNOT2ZI"),
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
					/*Radix::Acs::User:GenerateReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7WZ4L4SL65HVXAAB7VADERPDJM"),
						"GenerateReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY53VJVMXANDMPASWUTYGV4WV34"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						false),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:BeforeAccept-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdBCNKLHK2C5CNLAOPP44HLQEDXI"),
						"BeforeAccept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:Name-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltXDV2WXUPKRA6LPQ4VPQ5WOXCQM"),
						"Name",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYARASEC3WJEORB6ZRKBEWA4TWY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colEF6ODCYWY3NBDGMCABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmAQE3EICYKVD73LK744QJXSXOWM\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmAQE3EICYKVD73LK744QJXSXOWM"),
								"name",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA2TZQPO3VDJXMBMLJXUXIQBFU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:Name:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:PersonName-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltSRG6OBEIWFB6HO3O55DYMRLDEY"),
						"PersonName",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3UEP4UMOWRDW5KFJLH3FBDWO6U"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"col3FYUIAY5J3NRDJIRACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMLERCKVL4BFCHE36AB3M2776QI\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMLERCKVL4BFCHE36AB3M2776QI"),
								"personName",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWCXBLNH4BBSHPVKXTBFVTQSOY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:PersonName:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:Email-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBTTJNCPNR5DADH4XGHF23GLUSI"),
						"Email",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54XSYWLOMNAWXEXAHAVIWCRDK4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colWXQAHXB5V3OBDCLVAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmJ3OGIPSYIVFEHMEIOJ7FQK72KE\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmJ3OGIPSYIVFEHMEIOJ7FQK72KE"),
								"email",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH74ZLTZZMZBSZC2R5XQ6TQQOZA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:Email:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:Phone-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltP2KKOGX34BGZJONNDRIFHXC7WI"),
						"Phone",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54S4Z4LOPBEI5KAYCE6SNEK2UI"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colW3QAHXB5V3OBDCLVAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVYWMK4QDVEQFFGPIVHZ3GJHH4\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSVYWMK4QDVEQFFGPIVHZ3GJHH4"),
								"phone",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ONI3E5YMZCWBEKGPA7IFUF544"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:Phone:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:Name-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),
						"Name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HZX77BMPJC7ZCL4QX4ZYD53ME"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:PersonName-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),
						"PersonName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSZRWJD3EFGVZFYW7LHL2ZNMIQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:Email-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),
						"Email",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XWDYKW2TJHJNEUJOJ5KATMYZI"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:Phone-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),
						"Phone",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZXFF3JXBNFN7DN5MP7VCQVDSA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refL5DHK752J3NRDAQSABIFNQAAAE"),"User=>UserGroup (adminGroupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colNMVAZDYYY3NBDGMCABQAQH3XQ4")},new String[]{"adminGroupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refQM4AVQRQWBAHVLH3GDT2F7CN7A"),"User=>IntervalSchd (logonScheduleId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7GBPTNS6VFMFAYXSSQLWYJRPM")},new String[]{"logonScheduleId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A")},
			false,true,false);
}

/* Radix::Acs::User - Web Executable*/

/*Radix::Acs::User-Entity Class*/

package org.radixware.ads.Acs.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User")
public interface User {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Acs.web.User.User_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Acs.web.User.User_DefaultModel )  super.getEntity(i);}
	}






















































































































































































































	/*Radix::Acs::User:lockReason:lockReason-Presentation Property*/


	public class LockReason extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LockReason(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccountLockReason> getValClass(){
			return org.radixware.kernel.common.enums.EAccountLockReason.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lockReason:lockReason")
		public  org.radixware.kernel.common.enums.EAccountLockReason getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lockReason:lockReason")
		public   void setValue(org.radixware.kernel.common.enums.EAccountLockReason val) {
			Value = val;
		}
	}
	public LockReason getLockReason();
	/*Radix::Acs::User:traceGuiActions:traceGuiActions-Presentation Property*/


	public class TraceGuiActions extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public TraceGuiActions(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:traceGuiActions:traceGuiActions")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:traceGuiActions:traceGuiActions")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public TraceGuiActions getTraceGuiActions();
	/*Radix::Acs::User:personName:personName-Presentation Property*/


	public class PersonName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PersonName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:personName:personName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:personName:personName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PersonName getPersonName();
	/*Radix::Acs::User:dbTraceProfile:dbTraceProfile-Presentation Property*/


	public class DbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::Acs::User:isTemporaryPwdExpired:isTemporaryPwdExpired-Presentation Property*/


	public class IsTemporaryPwdExpired extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsTemporaryPwdExpired(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isTemporaryPwdExpired:isTemporaryPwdExpired")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isTemporaryPwdExpired:isTemporaryPwdExpired")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsTemporaryPwdExpired getIsTemporaryPwdExpired();
	/*Radix::Acs::User:pwdExpirationPeriod:pwdExpirationPeriod-Presentation Property*/


	public class PwdExpirationPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdExpirationPeriod:pwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdExpirationPeriod:pwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdExpirationPeriod getPwdExpirationPeriod();
	/*Radix::Acs::User:mustChangePwd:mustChangePwd-Presentation Property*/


	public class MustChangePwd extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public MustChangePwd(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mustChangePwd:mustChangePwd")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mustChangePwd:mustChangePwd")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public MustChangePwd getMustChangePwd();
	/*Radix::Acs::User:authTypes:authTypes-Presentation Property*/


	public class AuthTypes extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AuthTypes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Acs.common.AuthType.Arr> getValClass(){
			return org.radixware.ads.Acs.common.AuthType.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:authTypes:authTypes")
		public  org.radixware.ads.Acs.common.AuthType.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:authTypes:authTypes")
		public   void setValue(org.radixware.ads.Acs.common.AuthType.Arr val) {
			Value = val;
		}
	}
	public AuthTypes getAuthTypes();
	/*Radix::Acs::User:name:name-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::Acs::User:systemPwdExpirationPeriod:systemPwdExpirationPeriod-Presentation Property*/


	public class SystemPwdExpirationPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SystemPwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:systemPwdExpirationPeriod:systemPwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:systemPwdExpirationPeriod:systemPwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SystemPwdExpirationPeriod getSystemPwdExpirationPeriod();
	/*Radix::Acs::User:isMaySetNullForAdminGroup:isMaySetNullForAdminGroup-Presentation Property*/


	public class IsMaySetNullForAdminGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsMaySetNullForAdminGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isMaySetNullForAdminGroup:isMaySetNullForAdminGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:isMaySetNullForAdminGroup:isMaySetNullForAdminGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMaySetNullForAdminGroup getIsMaySetNullForAdminGroup();
	/*Radix::Acs::User:adminGroup:adminGroup-Presentation Property*/


	public class AdminGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public AdminGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroup:adminGroup")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:adminGroup:adminGroup")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public AdminGroup getAdminGroup();
	/*Radix::Acs::User:invalidLogonCnt:invalidLogonCnt-Presentation Property*/


	public class InvalidLogonCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InvalidLogonCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonCnt:invalidLogonCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonCnt:invalidLogonCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InvalidLogonCnt getInvalidLogonCnt();
	/*Radix::Acs::User:invalidLogonTime:invalidLogonTime-Presentation Property*/


	public class InvalidLogonTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public InvalidLogonTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonTime:invalidLogonTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:invalidLogonTime:invalidLogonTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public InvalidLogonTime getInvalidLogonTime();
	/*Radix::Acs::User:lastLogonTime:lastLogonTime-Presentation Property*/


	public class LastLogonTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastLogonTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastLogonTime:lastLogonTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastLogonTime:lastLogonTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastLogonTime getLastLogonTime();
	/*Radix::Acs::User:temporaryPwdStartTime:temporaryPwdStartTime-Presentation Property*/


	public class TemporaryPwdStartTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public TemporaryPwdStartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:temporaryPwdStartTime:temporaryPwdStartTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:temporaryPwdStartTime:temporaryPwdStartTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public TemporaryPwdStartTime getTemporaryPwdStartTime();
	/*Radix::Acs::User:lastPwdChangeTime:lastPwdChangeTime-Presentation Property*/


	public class LastPwdChangeTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastPwdChangeTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastPwdChangeTime:lastPwdChangeTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:lastPwdChangeTime:lastPwdChangeTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastPwdChangeTime getLastPwdChangeTime();
	/*Radix::Acs::User:createTime:createTime-Presentation Property*/


	public class CreateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public CreateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:createTime:createTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:createTime:createTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public CreateTime getCreateTime();
	/*Radix::Acs::User:locked:locked-Presentation Property*/


	public class Locked extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Locked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:locked:locked")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:locked:locked")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Locked getLocked();
	/*Radix::Acs::User:checkStation:checkStation-Presentation Property*/


	public class CheckStation extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CheckStation(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkStation:checkStation")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:checkStation:checkStation")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CheckStation getCheckStation();
	/*Radix::Acs::User:mobilePhone:mobilePhone-Presentation Property*/


	public class MobilePhone extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MobilePhone(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mobilePhone:mobilePhone")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:mobilePhone:mobilePhone")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MobilePhone getMobilePhone();
	/*Radix::Acs::User:autoLocked:autoLocked-Presentation Property*/


	public class AutoLocked extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AutoLocked(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:autoLocked:autoLocked")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:autoLocked:autoLocked")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AutoLocked getAutoLocked();
	/*Radix::Acs::User:sessionsLimit:sessionsLimit-Presentation Property*/


	public class SessionsLimit extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SessionsLimit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:sessionsLimit:sessionsLimit")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:sessionsLimit:sessionsLimit")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SessionsLimit getSessionsLimit();
	/*Radix::Acs::User:email:email-Presentation Property*/


	public class Email extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Email(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:email:email")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:email:email")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Email getEmail();
	/*Radix::Acs::User:logonTimeSchedule:logonTimeSchedule-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonTimeSchedule:logonTimeSchedule")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:logonTimeSchedule:logonTimeSchedule")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public LogonTimeSchedule getLogonTimeSchedule();
	/*Radix::Acs::User:encryptedPwdHash:encryptedPwdHash-Presentation Property*/


	public class EncryptedPwdHash extends org.radixware.kernel.common.client.models.items.properties.PropertyBin{
		public EncryptedPwdHash(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:encryptedPwdHash:encryptedPwdHash")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:encryptedPwdHash:encryptedPwdHash")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public EncryptedPwdHash getEncryptedPwdHash();
	/*Radix::Acs::User:passwordDefined:passwordDefined-Presentation Property*/


	public class PasswordDefined extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PasswordDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordDefined:passwordDefined")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordDefined:passwordDefined")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PasswordDefined getPasswordDefined();
	/*Radix::Acs::User:hasNewRolesOrGroup:hasNewRolesOrGroup-Presentation Property*/


	public class HasNewRolesOrGroup extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public HasNewRolesOrGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:hasNewRolesOrGroup:hasNewRolesOrGroup")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:hasNewRolesOrGroup:hasNewRolesOrGroup")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public HasNewRolesOrGroup getHasNewRolesOrGroup();
	/*Radix::Acs::User:oldDbTraceProfile:oldDbTraceProfile-Presentation Property*/


	public class OldDbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OldDbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:oldDbTraceProfile:oldDbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:oldDbTraceProfile:oldDbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OldDbTraceProfile getOldDbTraceProfile();
	/*Radix::Acs::User:curUserCanAccept:curUserCanAccept-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:curUserCanAccept:curUserCanAccept")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:curUserCanAccept:curUserCanAccept")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CurUserCanAccept getCurUserCanAccept();
	/*Radix::Acs::User:usedDualControl:usedDualControl-Presentation Property*/


	public class UsedDualControl extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UsedDualControl(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:usedDualControl:usedDualControl")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:usedDualControl:usedDualControl")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UsedDualControl getUsedDualControl();
	/*Radix::Acs::User:passwordRequirements:passwordRequirements-Presentation Property*/


	public class PasswordRequirements extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public PasswordRequirements(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.eas.PasswordRequirementsDocument> getValClass(){
			return org.radixware.schemas.eas.PasswordRequirementsDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.eas.PasswordRequirementsDocument dummy = x == null ? null : (org.radixware.schemas.eas.PasswordRequirementsDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.eas.PasswordRequirementsDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordRequirements:passwordRequirements")
		public  org.radixware.schemas.eas.PasswordRequirementsDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:passwordRequirements:passwordRequirements")
		public   void setValue(org.radixware.schemas.eas.PasswordRequirementsDocument val) {
			Value = val;
		}
	}
	public PasswordRequirements getPasswordRequirements();
	/*Radix::Acs::User:totalLock:totalLock-Presentation Property*/


	public class TotalLock extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public TotalLock(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:totalLock:totalLock")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:totalLock:totalLock")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public TotalLock getTotalLock();
	/*Radix::Acs::User:finalLockReason:finalLockReason-Presentation Property*/


	public class FinalLockReason extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FinalLockReason(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccountLockReason> getValClass(){
			return org.radixware.kernel.common.enums.EAccountLockReason.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:finalLockReason:finalLockReason")
		public  org.radixware.kernel.common.enums.EAccountLockReason getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:finalLockReason:finalLockReason")
		public   void setValue(org.radixware.kernel.common.enums.EAccountLockReason val) {
			Value = val;
		}
	}
	public FinalLockReason getFinalLockReason();
	/*Radix::Acs::User:pwdHashBytes:pwdHashBytes-Presentation Property*/


	public class PwdHashBytes extends org.radixware.kernel.common.client.models.items.properties.PropertyBin{
		public PwdHashBytes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashBytes:pwdHashBytes")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:pwdHashBytes:pwdHashBytes")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public PwdHashBytes getPwdHashBytes();
	public static class GenerateReport extends org.radixware.kernel.common.client.models.items.Command{
		protected GenerateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.reports.GenerateReportRqDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

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

	public static class MoveRightsToGroup extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveRightsToGroup(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument send(org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class);
		}

	}

	public static class Unlock extends org.radixware.kernel.common.client.models.items.Command{
		protected Unlock(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Acs::User - Web Meta*/

/*Radix::Acs::User-Entity Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Acs::User:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
			"Radix::Acs::User",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF3YUCJZIIRDFPARXX53HDOSLLA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),0,

			/*Radix::Acs::User:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Acs::User:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJ53S5PYRZGZTBTMFRE77NEL74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:checkStation:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRC2TN7IYY3NBDGMCABQAQH3XQ4"),
						"checkStation",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTC6DSITNBAVPNVASWEML6FPZ4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:checkStation:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:email:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),
						"email",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSY7MTF23ZE57K66FTROSC2QJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:email:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:lastPwdChangeTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPUPBTVQYY3NBDGMCABQAQH3XQ4"),
						"lastPwdChangeTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSOVIMNXICJG4ZAARHZLVX3XZKY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:lastPwdChangeTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:mobilePhone:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),
						"mobilePhone",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT4QJIFPKKZHEJDR2RGRUDSAA2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:mobilePhone:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,20,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:personName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),
						"personName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGULLAHHBJHK5PXLSFJ25Y44U4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:personName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:pwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XNCT3QYY3NBDGMCABQAQH3XQ4"),
						"pwdExpirationPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFG5FSDDB5GTLKYGGWVWKXFG4M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("90"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:pwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCNTHEUMGW5EHHN4EON3ECL3AXE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:adminGroup:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHSW2Y4C4YJF4NLVE5A3F62ZY6U"),
						"adminGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PPPOAZDJVDVHC7HMI2UTJZW7Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprMOOY2WRJ4BFDFGBKRCUPG5ARNQ")},
						null,
						null,
						0,
						0,false),

					/*Radix::Acs::User:passwordDefined:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6IFAQA7VIFF2TK56SOTVEJR6GI"),
						"passwordDefined",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGM5S2KDHJAARLOLGQIFNO4BIA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeABFMBPRW7ZCQNPVSGYUUUBZHZI"),
						true,

						/*Radix::Acs::User:passwordDefined:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:invalidLogonCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJZIYRXEXNJFLFIZGTXRUSIM4AE"),
						"invalidLogonCnt",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::Acs::User:invalidLogonCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:invalidLogonTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNBBMGSDE5BF5FODR4F5TE2UAD4"),
						"invalidLogonTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCLRYTJC35E4NBJNHNMVFKPWMA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:invalidLogonTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:mustChangePwd:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANJQ6ZDT6FALRCPTTSR3UUR5VQ"),
						"mustChangePwd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZX6MH575FA2TPPKQT67VFTQUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:mustChangePwd:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:autoLocked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),
						"autoLocked",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5PY3P6I5CNDJVGS77HWIBXLI6A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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

						/*Radix::Acs::User:autoLocked:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:locked:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),
						"locked",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ERS2ARWXVBU5K5VP4J3GBAQJM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:locked:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:totalLock:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),
						"totalLock",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYSFNKAXJVBWLEUW5JQUD5AZPU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:totalLock:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:isMaySetNullForAdminGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLIAZG6JPFEGFFGAOORNELFSCA"),
						"isMaySetNullForAdminGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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

						/*Radix::Acs::User:isMaySetNullForAdminGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3G4FLB6M6FBVTL3BZLIJUG2DII"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWHL2HEZZBADZE3RMHNFGRBNLY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeDPJZYTP5U5AZHJI724SY2T5B6A"),
						true,

						/*Radix::Acs::User:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:oldDbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBNGCOGAZINEMZENFUJT546WWYI"),
						"oldDbTraceProfile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:oldDbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:traceGuiActions:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37HDYBNIXJAHFBGS6QUSHOFH2U"),
						"traceGuiActions",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY5T547WITND3FMF7GWOKZIVVYI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:traceGuiActions:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:authTypes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZ5HBUOKRVBUXB5LM7WNIBBFP4"),
						"authTypes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4PMYISQIRCJJOZRGQFQTACGLQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOMXM7EAWVZBNJHWQBKZELLOYKM"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:authTypes:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOMXM7EAWVZBNJHWQBKZELLOYKM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPRVITUOKNEEXBJMLGWWKYZBLM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMAGDPOMJ5NDHXBE3323TJJ6LF4"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:createTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5GRW4H5KNDC7PHXE7Y52C7KUM"),
						"createTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45GPZMNIMRCU7CZLDQQGSSYYHU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:createTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:hasNewRolesOrGroup:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7EWBCN4XXFCDRCQYPUYBNHH25E"),
						"hasNewRolesOrGroup",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:hasNewRolesOrGroup:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:usedDualControl:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),
						"usedDualControl",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:usedDualControl:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:logonTimeSchedule:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY45VD62NWJD3FKO22HTQTVXY7E"),
						"logonTimeSchedule",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA4IPRR4J2RDO3HWDYS5X57F2S4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGHGLDHI2RVCJLP63YAV343C6IA"),
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

					/*Radix::Acs::User:sessionsLimit:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWVM572KI6JELDKJ7OB6SWDMUJY"),
						"sessionsLimit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27NYQRCRRJEGXLPJQMUXDCSPYM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0"),
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:sessionsLimit:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6WWZGHT4XVCDPPSR6X4IDGPLQA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:curUserCanAccept:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),
						"curUserCanAccept",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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

						/*Radix::Acs::User:curUserCanAccept:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:lastLogonTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNF6GYZT3FVFLLKTBIOC7F7IN3A"),
						"lastLogonTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:lastLogonTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:lockReason:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2FODGXNBXBCM5MJZ4D5FPOY2MI"),
						"lockReason",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),
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

						/*Radix::Acs::User:lockReason:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:finalLockReason:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"),
						"finalLockReason",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3G6WSF66BEQDHEZGJQYKOUPPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),
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

						/*Radix::Acs::User:finalLockReason:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFGMB6LAARFGZZFWHHDXOKASYL4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LM4P27AFVAAFAV3JHWRWJNCBI"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:passwordRequirements:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJGY5G5O23VG27HRRWMZD223LPY"),
						"passwordRequirements",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:pwdHashBytes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M"),
						"pwdHashBytes",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BIN,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:encryptedPwdHash:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ"),
						"encryptedPwdHash",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BIN,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:systemPwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colERICYL54QBAKFH6PABOXUBRHR4"),
						"systemPwdExpirationPeriod",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						61,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),
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

						/*Radix::Acs::User:systemPwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:temporaryPwdStartTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3BCFXYVBNH65NBW7YHFNUX7BA"),
						"temporaryPwdStartTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCBTJMWRM5C77IPYDQ6FW3ESTU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::Acs::User:temporaryPwdStartTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Acs::User:isTemporaryPwdExpired:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),
						"isTemporaryPwdExpired",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.enums.EValType.BOOL,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Acs::User:isTemporaryPwdExpired:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:Unlock-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdV3R6LGIKURC2BCD7HW4YW4TOXM"),
						"Unlock",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHQC7AJB65GRJCYWWDLQWY2L6U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRGURYMOTERAO3OAQ72IMY67RAE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:MoveRightsToGroup-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdSNYYAJ37T5G65ESWDW6CHEX64Q"),
						"MoveRightsToGroup",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGG3ZU2FH25BT3FF7WYRJ6UMCZY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img6DMBDLPNWNC5PCZLW5VXMTAS4M"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:AcceptRights-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMXS7QC4BQJED7CM4TKDWTPOLUM"),
						"AcceptRights",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5CI2XOXI5GKPLD4XKHGNOT2ZI"),
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
					/*Radix::Acs::User:GenerateReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd7WZ4L4SL65HVXAAB7VADERPDJM"),
						"GenerateReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY53VJVMXANDMPASWUTYGV4WV34"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3627K6GYUFGXFJYWMVCBQAPHUA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						false),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Acs::User:BeforeAccept-Remote Call Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdBCNKLHK2C5CNLAOPP44HLQEDXI"),
						"BeforeAccept",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
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
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:Name-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltXDV2WXUPKRA6LPQ4VPQ5WOXCQM"),
						"Name",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYARASEC3WJEORB6ZRKBEWA4TWY"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colEF6ODCYWY3NBDGMCABQAQH3XQ4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmAQE3EICYKVD73LK744QJXSXOWM\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY")},
						true,
						false,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmAQE3EICYKVD73LK744QJXSXOWM"),
								"name",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA2TZQPO3VDJXMBMLJXUXIQBFU"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:Name:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:PersonName-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltSRG6OBEIWFB6HO3O55DYMRLDEY"),
						"PersonName",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3UEP4UMOWRDW5KFJLH3FBDWO6U"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"col3FYUIAY5J3NRDJIRACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMLERCKVL4BFCHE36AB3M2776QI\"/></xsc:Item><xsc:Item><xsc:Sql>\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMLERCKVL4BFCHE36AB3M2776QI"),
								"personName",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWCXBLNH4BBSHPVKXTBFVTQSOY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:PersonName:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:Email-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltBTTJNCPNR5DADH4XGHF23GLUSI"),
						"Email",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54XSYWLOMNAWXEXAHAVIWCRDK4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colWXQAHXB5V3OBDCLVAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmJ3OGIPSYIVFEHMEIOJ7FQK72KE\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmJ3OGIPSYIVFEHMEIOJ7FQK72KE"),
								"email",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH74ZLTZZMZBSZC2R5XQ6TQQOZA"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:Email:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Acs::User:Phone-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltP2KKOGX34BGZJONNDRIFHXC7WI"),
						"Phone",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54S4Z4LOPBEI5KAYCE6SNEK2UI"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>UPPER(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aecSY4KIOLTGLNRDHRZABQAQH3XQ4\" PropId=\"colW3QAHXB5V3OBDCLVAALOMT5GDM\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmSVYWMK4QDVEQFFGPIVHZ3GJHH4\"/></xsc:Item></xsc:Sqml>",
						null,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU")},
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmSVYWMK4QDVEQFFGPIVHZ3GJHH4"),
								"phone",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ONI3E5YMZCWBEKGPA7IFUF544"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								true,
								false,
								false,
								null,
								false,
								null,
								null,
								false,
								null,
								null)
						},

						/*Radix::Acs::User:Phone:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:Name-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),
						"Name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HZX77BMPJC7ZCL4QX4ZYD53ME"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:PersonName-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtXN3565IDL5CA5NF5EEDUAY5X64"),
						"PersonName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSZRWJD3EFGVZFYW7LHL2ZNMIQ"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:Email-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtGBX7GDFN35CNVDADD2EP2V2A5A"),
						"Email",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XWDYKW2TJHJNEUJOJ5KATMYZI"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Acs::User:Phone-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtS376NUSLWVHQXDOEUNOF2W4NDU"),
						"Phone",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZXFF3JXBNFN7DN5MP7VCQVDSA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refL5DHK752J3NRDAQSABIFNQAAAE"),"User=>UserGroup (adminGroupName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblQ23AYDTTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colNMVAZDYYY3NBDGMCABQAQH3XQ4")},new String[]{"adminGroupName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colV2XEAGILY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refQM4AVQRQWBAHVLH3GDT2F7CN7A"),"User=>IntervalSchd (logonScheduleId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblZBS5MS2BI3OBDCIOAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colR7GBPTNS6VFMFAYXSSQLWYJRPM")},new String[]{"logonScheduleId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colDHYHOUSBI3OBDCIOAALOMT5GDM")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A")},
			false,true,false);
}

/* Radix::Acs::User:General - Desktop Meta*/

/*Radix::Acs::User:General-Editor Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
	null,
	null,

	/*Radix::Acs::User:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::User:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgATPHIXZI7VAEFGI2WCEDE7GPWA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDVE7PY5A5F7DLC565NNXYVHC4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHSW2Y4C4YJF4NLVE5A3F62ZY6U"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRC2TN7IYY3NBDGMCABQAQH3XQ4"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6IFAQA7VIFF2TK56SOTVEJR6GI"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANJQ6ZDT6FALRCPTTSR3UUR5VQ"),0,11,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPUPBTVQYY3NBDGMCABQAQH3XQ4"),0,12,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNBBMGSDE5BF5FODR4F5TE2UAD4"),0,15,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XNCT3QYY3NBDGMCABQAQH3XQ4"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3G4FLB6M6FBVTL3BZLIJUG2DII"),0,16,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37HDYBNIXJAHFBGS6QUSHOFH2U"),0,17,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZ5HBUOKRVBUXB5LM7WNIBBFP4"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY45VD62NWJD3FKO22HTQTVXY7E"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWVM572KI6JELDKJ7OB6SWDMUJY"),0,8,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"),1,14,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3BCFXYVBNH65NBW7YHFNUX7BA"),0,13,2,false,false)
			},null),

			/*Radix::Acs::User:General:Groups-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKQICKB7BNGHVK65EI5HOBNLGA"),"Groups",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVJVNZH43MZD2LL3GWVDSJ35HEQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi5D6C6BTS6VHYZCQL2MVYRTMVXI")),

			/*Radix::Acs::User:General:OwnRoles-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6DQC46O63FGFXHTNLEVF65XVSQ"),"OwnRoles",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4B7AJF4FJVBLNM4H6SXJX3PY6Q")),

			/*Radix::Acs::User:General:Stations-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVFQV6SA6OVHKXIB7SBVLJCHS3E"),"Stations",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQIJRO4OWI5AO7AOKSIUUNIGJZY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiSB3NZZPD6ZHNJFMWWR6IGNLKIQ")),

			/*Radix::Acs::User:General:InheritRights-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGVFWGDSVUJB67L4FCCW7W7EQQI"),"InheritRights",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ5AUXDYGYBCPNCP36JDXAB6NC4"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiKLLWEBFDWBG7PKSVW53WWQXZTM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgATPHIXZI7VAEFGI2WCEDE7GPWA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6DQC46O63FGFXHTNLEVF65XVSQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKQICKB7BNGHVK65EI5HOBNLGA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGVFWGDSVUJB67L4FCCW7W7EQQI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVFQV6SA6OVHKXIB7SBVLJCHS3E"))}
	,

	/*Radix::Acs::User:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::User:General:User2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4B7AJF4FJVBLNM4H6SXJX3PY6Q"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJBJGPNER5HM7PT4W772BJWB2Q"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
					0,
					null,
					16384,true),

				/*Radix::Acs::User:General:User2UserGroup-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi5D6C6BTS6VHYZCQL2MVYRTMVXI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVZQR4RFDZDJLC7B2RC23OSYR4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecDYWJCJTTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKSID2LQ4PBAEJEM4W7FEDEL2FQ"),
					0,
					null,
					16416,true),

				/*Radix::Acs::User:General:UserGroup2Role-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiKLLWEBFDWBG7PKSVW53WWQXZTM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3HV5FLY6TVDHBAY5P7FHI3JXAU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
					19495,
					null,
					16400,true),

				/*Radix::Acs::User:General:User2Station-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiSB3NZZPD6ZHNJFMWWR6IGNLKIQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUSA6IKG7VBAJE5BRZG2QD57PM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGPFDUXBHJ3NRDJIRACQMTAIZT4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNK2VXHAQTNA4FMLHOEF7XIWSVU"),
					0,
					null,
					16416,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	32,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::User:General - Web Meta*/

/*Radix::Acs::User:General-Editor Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
	null,
	null,

	/*Radix::Acs::User:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Acs::User:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgATPHIXZI7VAEFGI2WCEDE7GPWA"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDVE7PY5A5F7DLC565NNXYVHC4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("img6GSK5NAPPNHURHWSF6J2FEZC6M"),new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),0,1,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWXQAHXB5V3OBDCLVAALOMT5GDM"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHSW2Y4C4YJF4NLVE5A3F62ZY6U"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRC2TN7IYY3NBDGMCABQAQH3XQ4"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd6IFAQA7VIFF2TK56SOTVEJR6GI"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colANJQ6ZDT6FALRCPTTSR3UUR5VQ"),0,11,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPUPBTVQYY3NBDGMCABQAQH3XQ4"),0,12,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNBBMGSDE5BF5FODR4F5TE2UAD4"),0,15,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW3QAHXB5V3OBDCLVAALOMT5GDM"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5XNCT3QYY3NBDGMCABQAQH3XQ4"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3G4FLB6M6FBVTL3BZLIJUG2DII"),0,16,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col37HDYBNIXJAHFBGS6QUSHOFH2U"),0,17,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDZ5HBUOKRVBUXB5LM7WNIBBFP4"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY45VD62NWJD3FKO22HTQTVXY7E"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWVM572KI6JELDKJ7OB6SWDMUJY"),0,8,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"),1,14,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO3BCFXYVBNH65NBW7YHFNUX7BA"),0,13,2,false,false)
			},null),

			/*Radix::Acs::User:General:Groups-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKQICKB7BNGHVK65EI5HOBNLGA"),"Groups",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgVJVNZH43MZD2LL3GWVDSJ35HEQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi5D6C6BTS6VHYZCQL2MVYRTMVXI")),

			/*Radix::Acs::User:General:OwnRoles-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6DQC46O63FGFXHTNLEVF65XVSQ"),"OwnRoles",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgRWDDDR2YBZED5B3BUYIQRTLAQQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4B7AJF4FJVBLNM4H6SXJX3PY6Q")),

			/*Radix::Acs::User:General:Stations-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVFQV6SA6OVHKXIB7SBVLJCHS3E"),"Stations",null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQIJRO4OWI5AO7AOKSIUUNIGJZY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiSB3NZZPD6ZHNJFMWWR6IGNLKIQ")),

			/*Radix::Acs::User:General:InheritRights-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGVFWGDSVUJB67L4FCCW7W7EQQI"),"InheritRights",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ5AUXDYGYBCPNCP36JDXAB6NC4"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiKLLWEBFDWBG7PKSVW53WWQXZTM"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgATPHIXZI7VAEFGI2WCEDE7GPWA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6DQC46O63FGFXHTNLEVF65XVSQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgDKQICKB7BNGHVK65EI5HOBNLGA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGVFWGDSVUJB67L4FCCW7W7EQQI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVFQV6SA6OVHKXIB7SBVLJCHS3E"))}
	,

	/*Radix::Acs::User:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Acs::User:General:User2Role-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi4B7AJF4FJVBLNM4H6SXJX3PY6Q"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJBJGPNER5HM7PT4W772BJWB2Q"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec42K4K2TTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4ANIOJDV5BEANCPBFYUJD6WQBI"),
					0,
					null,
					16384,true),

				/*Radix::Acs::User:General:User2UserGroup-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi5D6C6BTS6VHYZCQL2MVYRTMVXI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVZQR4RFDZDJLC7B2RC23OSYR4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecDYWJCJTTGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKSID2LQ4PBAEJEM4W7FEDEL2FQ"),
					0,
					null,
					16416,true),

				/*Radix::Acs::User:General:UserGroup2Role-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiKLLWEBFDWBG7PKSVW53WWQXZTM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3HV5FLY6TVDHBAY5P7FHI3JXAU"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecFJAEQT3TGLNRDHRZABQAQH3XQ4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4M72LWZY6BEFDBSMYOB27SEVBE"),
					19495,
					null,
					16400,true),

				/*Radix::Acs::User:General:User2Station-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiSB3NZZPD6ZHNJFMWWR6IGNLKIQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUSA6IKG7VBAJE5BRZG2QD57PM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecGPFDUXBHJ3NRDJIRACQMTAIZT4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprNK2VXHAQTNA4FMLHOEF7XIWSVU"),
					0,
					null,
					16416,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	32,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Acs::User:General:Model - Desktop Executable*/

/*Radix::Acs::User:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;

import org.radixware.kernel.common.auth.PasswordHash;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.User.User_DefaultModel implements org.radixware.ads.Reports.common_client.IReportPubModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:General:Model:Properties-Properties*/

	/*Radix::Acs::User:General:Model:passwordDefined-Presentation Property*/




	public class PasswordDefined extends org.radixware.ads.Acs.explorer.User.prd6IFAQA7VIFF2TK56SOTVEJR6GI{
		public PasswordDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:passwordDefined")
		public published  Str getValue() {

			if ( pwdHashBytes.getValue()!=null || encryptedPwdHash.getValue()!=null || rawPassword.getValue()!=null ){
			    return "<defined>";
			}
			return null;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:passwordDefined")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public PasswordDefined getPasswordDefined(){return (PasswordDefined)getProperty(prd6IFAQA7VIFF2TK56SOTVEJR6GI);}

	/*Radix::Acs::User:General:Model:name-Presentation Property*/




	public class Name extends org.radixware.ads.Acs.explorer.User.colEF6ODCYWY3NBDGMCABQAQH3XQ4{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:name")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:name")
		public published   void setValue(Str val) {

			internal[name] = val;
			//.setEnabled(val!=null &&  !val.isEmpty());
			//.(null);
			//.afterModify();
		}
	}
	public Name getName(){return (Name)getProperty(colEF6ODCYWY3NBDGMCABQAQH3XQ4);}

	/*Radix::Acs::User:General:Model:totalLock-Presentation Property*/




	public class TotalLock extends org.radixware.ads.Acs.explorer.User.prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE{
		public TotalLock(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemK3FTXQSCQRAAJPVNYCKOQSCK7U.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"))) {
				this.addDependent(aemK3FTXQSCQRAAJPVNYCKOQSCK7U.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:totalLock")
		public published  Bool getValue() {

			return locked.Value != null && locked.Value.booleanValue() 
			       || autoLocked.Value != null && autoLocked.Value.booleanValue() 
			       || isTemporaryPwdExpired.Value != null && isTemporaryPwdExpired.Value.booleanValue();
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:totalLock")
		public published   void setValue(Bool val) {

			if (!val.booleanValue())
			{
			  invalidLogonCnt.Value = 0; 
			  autoLocked.Value = false;
			} 

			locked.Value = val;
			if(Boolean.TRUE.equals(val) && finalLockReason.Value == null){
			    lockReason.Value = AccountLockReason:BY_ADMIN;
			    finalLockReason.Value = AccountLockReason:BY_ADMIN;
			}else{
			    lockReason.cancelChanges();
			    finalLockReason.cancelChanges();
			}
			internal[totalLock] = val;


			totalLock.afterModify();

			checkCommandEnabled();

		}
	}
	public TotalLock getTotalLock(){return (TotalLock)getProperty(prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE);}

	/*Radix::Acs::User:General:Model:isMaySetNullForAdminGroup-Presentation Property*/




	public class IsMaySetNullForAdminGroup extends org.radixware.ads.Acs.explorer.User.colHLIAZG6JPFEGFFGAOORNELFSCA{
		public IsMaySetNullForAdminGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:isMaySetNullForAdminGroup")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:isMaySetNullForAdminGroup")
		public published   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMaySetNullForAdminGroup getIsMaySetNullForAdminGroup(){return (IsMaySetNullForAdminGroup)getProperty(colHLIAZG6JPFEGFFGAOORNELFSCA);}

	/*Radix::Acs::User:General:Model:authTypes-Presentation Property*/




	public class AuthTypes extends org.radixware.ads.Acs.explorer.User.colDZ5HBUOKRVBUXB5LM7WNIBBFP4{
		public AuthTypes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Acs.common.AuthType.Arr> getValClass(){
			return org.radixware.ads.Acs.common.AuthType.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:authTypes")
		public  org.radixware.ads.Acs.common.AuthType.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:authTypes")
		public   void setValue(org.radixware.ads.Acs.common.AuthType.Arr val) {

			internal[authTypes] = val;
			applyAuthTypes();
		}
	}
	public AuthTypes getAuthTypes(){return (AuthTypes)getProperty(colDZ5HBUOKRVBUXB5LM7WNIBBFP4);}

	/*Radix::Acs::User:General:Model:finalLockReason-Presentation Property*/




	public class FinalLockReason extends org.radixware.ads.Acs.explorer.User.prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU{
		public FinalLockReason(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccountLockReason> getValClass(){
			return org.radixware.kernel.common.enums.EAccountLockReason.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:finalLockReason:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:finalLockReason:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:finalLockReason:Properties-Properties*/

		/*Radix::Acs::User:General:Model:finalLockReason:Methods-Methods*/

		/*Radix::Acs::User:General:Model:finalLockReason:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:finalLockReason:isVisible")
		public published  boolean isVisible () {
			if (totalLock.Value == null || !Boolean.TRUE.equals(totalLock.Value))
			    return false;
			return super.isVisible();
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:finalLockReason")
		public  org.radixware.kernel.common.enums.EAccountLockReason getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:finalLockReason")
		public   void setValue(org.radixware.kernel.common.enums.EAccountLockReason val) {
			Value = val;
		}
	}
	public FinalLockReason getFinalLockReason(){return (FinalLockReason)getProperty(prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU);}

	/*Radix::Acs::User:General:Model:pwdHashBytes-Presentation Property*/




	public class PwdHashBytes extends org.radixware.ads.Acs.explorer.User.prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M{
		public PwdHashBytes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdHashBytes")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdHashBytes")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {

			if (val!=null && getEnvironment().getEasSession().isSessionKeyAccessible()){
			    final byte[] rawPwdHash = val.get();
			    final byte[] encryptedPwdHash = getEnvironment().getEasSession().encryptBySessionKey(rawPwdHash);
			    java.util.Arrays.fill(rawPwdHash, (byte)0);
			    encryptedPwdHash.setValue(Bin.wrap(encryptedPwdHash));        
			}else{
			    internal[pwdHashBytes] = val;
			}
			passwordDefined.afterModify();
		}
	}
	public PwdHashBytes getPwdHashBytes(){return (PwdHashBytes)getProperty(prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M);}

	/*Radix::Acs::User:General:Model:rawPassword-Presentation Property*/




	public class RawPassword extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RawPassword(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:rawPassword")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:rawPassword")
		public   void setValue(Str val) {

			internal[rawPassword] = val;
			passwordDefined.afterModify();
		}
	}
	public RawPassword getRawPassword(){return (RawPassword)getProperty(prdSGL3QG35NVANTBGL7EJW7HAXPI);}

	/*Radix::Acs::User:General:Model:pwdExpirationPeriod-Presentation Property*/




	public class PwdExpirationPeriod extends org.radixware.ads.Acs.explorer.User.col5XNCT3QYY3NBDGMCABQAQH3XQ4{
		public PwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:Properties-Properties*/

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:Methods-Methods*/

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdExpirationPeriod:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			final Client.Validators::ValidationResult result = super.validateValue();
			if (result.getState()==org.radixware.kernel.common.client.meta.mask.validators.EValidatorState.ACCEPTABLE
			    && getValue()!=null
			    && systemPwdExpirationPeriod.getValue()!=null    
			    && systemPwdExpirationPeriod.getValue().longValue()<getValue().longValue()){
			    final String reason = String.format("Validity period of user password cannot exceed global value specified in system settings (%s).", systemPwdExpirationPeriod.getValue());
			    return Client.Validators::ValidationResult.Factory.newInvalidResult(reason);
			}
			return result;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdExpirationPeriod getPwdExpirationPeriod(){return (PwdExpirationPeriod)getProperty(col5XNCT3QYY3NBDGMCABQAQH3XQ4);}

	/*Radix::Acs::User:General:Model:encryptedPwdHash-Presentation Property*/




	public class EncryptedPwdHash extends org.radixware.ads.Acs.explorer.User.prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ{
		public EncryptedPwdHash(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:encryptedPwdHash:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:encryptedPwdHash:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:encryptedPwdHash:Properties-Properties*/

		/*Radix::Acs::User:General:Model:encryptedPwdHash:Methods-Methods*/

		/*Radix::Acs::User:General:Model:encryptedPwdHash:setServerValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:encryptedPwdHash:setServerValue")
		public published  void setServerValue (org.radixware.kernel.common.client.models.items.properties.PropertyValue value) {
			super.setServerValue(value);
			rawPassword.Value = null;
			rawPassword.setValEdited(false);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:encryptedPwdHash")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:encryptedPwdHash")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public EncryptedPwdHash getEncryptedPwdHash(){return (EncryptedPwdHash)getProperty(prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ);}

	/*Radix::Acs::User:General:Model:temporaryPwdStartTime-Presentation Property*/




	public class TemporaryPwdStartTime extends org.radixware.ads.Acs.explorer.User.colO3BCFXYVBNH65NBW7YHFNUX7BA{
		public TemporaryPwdStartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:Properties-Properties*/

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:Methods-Methods*/

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:temporaryPwdStartTime:isVisible")
		public published  boolean isVisible () {
			return getValue()!=null;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:temporaryPwdStartTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:temporaryPwdStartTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public TemporaryPwdStartTime getTemporaryPwdStartTime(){return (TemporaryPwdStartTime)getProperty(colO3BCFXYVBNH65NBW7YHFNUX7BA);}




























	/*Radix::Acs::User:General:Model:Methods-Methods*/

	/*Radix::Acs::User:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		checkCommandEnabled();
		totalLock.afterModify();
		//.setEnabled(.Value != null && !.Value.isEmpty());

		final boolean isNotSelector = !isInSelectorRowContext();
		if (isNotSelector){
		    applyAuthTypes();
		}
		try { 
		           
		    final Bool usetDualControl = usedDualControl.Value;
		    final boolean bUsetDualControl = usetDualControl!=null && usetDualControl.booleanValue();
		    this.getCommand(idof[User:AcceptRights]).setVisible(bUsetDualControl);
		    
		    if (bUsetDualControl){
		        final Bool curUserCanAccept = curUserCanAccept.Value;
		        final boolean bCurUserCanAccept = curUserCanAccept!=null && curUserCanAccept.booleanValue();
		        this.getCommand(idof[User:AcceptRights]).setEnabled(bCurUserCanAccept);
		    }
		    
		    if (!isNew()) {
		         

		        final Explorer.Models::GroupModel rolesGroup = isNotSelector && isExplorerItemAccessible(idof[User:General:User2Role])
		                ? (Explorer.Models::GroupModel) this.getChildModel(idof[User:General:User2Role])
		                : null;

		//        final  groupsGroup = isNotSelector && ()
		//                ? () this.()
		//                : null;

		        if (rolesGroup != null) {
		            rolesGroup.getSelectorColumn(idof[User2Role:userTitle]).setVisible(false);
		        }

		        if (isNotSelector) {
		            Boolean isMayChangeAdminGroup = (Boolean) isMaySetNullForAdminGroup.ValueObject;
		            this.adminGroup.setMandatory(
		                    isMayChangeAdminGroup != null
		                    && !isMayChangeAdminGroup.booleanValue());
		        }

		        if (name.Value == Environment.UserName) {


		            if (isNotSelector) {

		                if (rolesGroup != null) {
		                    rolesGroup.getRestrictions().setCreateRestricted(true);
		                    rolesGroup.getRestrictions().setDeleteRestricted(true);
		                }
		//                if (groupsGroup != null) {
		//                     RADIX-7681
		//                    groupsGroup.getRestrictions().setCreateRestricted(true);
		//                    groupsGroup.getRestrictions().setDeleteRestricted(true);
		//                }
		                getRestrictions().setDeleteRestricted(true);


		                this.getCommand(idof[User:MoveRightsToGroup]).setEnabled(false);
		            }
		            totalLock.setReadonly(true);



		        } else {
		            totalLock.setReadonly(totalLock.getValue()==Boolean.TRUE);
		            this.getCommand(idof[User:MoveRightsToGroup]).setEnabled(true);
		        }
		    }
		    else{
		        this.getCommand(idof[User:AcceptRights]).setVisible(false);
		    }

		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
	}

	/*Radix::Acs::User:General:Model:checkCommandEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:checkCommandEnabled")
	public published  void checkCommandEnabled () {


		boolean isAutoLock = autoLocked.Value != null && autoLocked.Value.booleanValue();
		boolean isLock;


		if (this.isNew())
		    isLock = false;
		else
		    isLock = isAutoLock
		            || (locked.Value != null && locked.Value.booleanValue())
		            || (isTemporaryPwdExpired.Value != null && isTemporaryPwdExpired.Value.booleanValue());
		this.getCommand(idof[User:Unlock]).setEnabled(isLock);
		invalidLogonTime.setVisible(isAutoLock);
	}

	/*Radix::Acs::User:General:Model:delete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:delete")
	public published  boolean delete (boolean forced) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException {
		if (Environment.messageConfirmation(
		                    "Confirm Object Deletion", 
		                    "Do you really want to delete" +
		                     " \'" + name.ValueAsString + "\'"
		                    ))
		    return super.delete(true);
		return false;
	}

	/*Radix::Acs::User:General:Model:askGroupName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:askGroupName")
	public  Str askGroupName () {
		return com.trolltech.qt.gui.QInputDialog.getText(Explorer.Env::Application.getMainWindow(), "Move Rights to Group","Group Name");
	}

	/*Radix::Acs::User:General:Model:beforePreparePaste-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforePreparePaste")
	protected published  boolean beforePreparePaste (org.radixware.kernel.common.client.models.EntityModel sourceModel) {
		pwdHashBytes.Value = null;
		encryptedPwdHash.Value = null;
		rawPassword.Value = null;
		return super.beforePreparePaste(sourceModel);
	}

	/*Radix::Acs::User:General:Model:getContextClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:getContextClassId")
	public published  org.radixware.kernel.common.types.Id getContextClassId () {
		return idof[User];

	}

	/*Radix::Acs::User:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:getContextId")
	public published  Str getContextId () {
		return name.Value;
	}

	/*Radix::Acs::User:General:Model:getParentContext-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:getParentContext")
	public published  org.radixware.ads.Reports.common_client.IReportPubModel getParentContext () {
		return new Reports::CommonReportContext();
	}

	/*Radix::Acs::User:General:Model:onCommand_GenerateReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_GenerateReport")
	public  void onCommand_GenerateReport (org.radixware.ads.Acs.explorer.User.GenerateReport command) {
		Reports::ReportsExplorerUtils.generateReport(
		        command, 
		        idof[ReportPubList.User]);

	}

	/*Radix::Acs::User:General:Model:applyAuthTypes-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:applyAuthTypes")
	protected published  void applyAuthTypes () {

		org.radixware.kernel.common.types.Arr<AuthType> authTypes = authTypes.Value;

		boolean isPasswordEnabled = authTypes == null || authTypes.contains(AuthType:PASSWORD);

		if (isPasswordEnabled) {
		    passwordDefined.setVisible(true);
		    pwdExpirationPeriod.setVisible(true);
		    mustChangePwd.setVisible(true);
		    lastPwdChangeTime.setVisible(true);
		} else {
		    passwordDefined.setVisible(false);
		    pwdExpirationPeriod.setVisible(false);
		    mustChangePwd.setVisible(false);
		    lastPwdChangeTime.setVisible(false);
		}
	}

	/*Radix::Acs::User:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		boolean rez = super.beforeCreate();

		if (rez && rawPassword.Value!=null && name.Value!=null){
		    name.Value = name.Value.trim();//RADIX-11527 
		    if (!updatePwdHash()){
		        return false;
		    }
		}

		return rez;
	}

	/*Radix::Acs::User:General:Model:onCommand_moveRightsToGroup-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_moveRightsToGroup")
	protected  void onCommand_moveRightsToGroup (org.radixware.ads.Acs.explorer.User.MoveRightsToGroup command) {
		try{
		    Str groupName = askGroupName();
		    if (groupName == null || groupName.isEmpty())    
		        return;
		    
		    CommandsXsd:StrValueDocument xIn = CommandsXsd:StrValueDocument.Factory.newInstance();        
		    xIn.StrValue = groupName;
		    CommandsXsd:StrValueDocument xOut = command.send(xIn);

		    if (xOut.StrValue == null || xOut.StrValue.isEmpty())
		    {
		        //all ok - refresh list
		        final Explorer.Models::GroupModel rolesGroup = (Explorer.Models::GroupModel)this.getChildModel(idof[User:General:User2Role]);
		        final Explorer.Models::GroupModel groupsGroup = (Explorer.Models::GroupModel)this.getChildModel(idof[User:General:User2UserGroup]);        
		        rolesGroup.reread();
		        groupsGroup.reread();
		    }
		    else
		    {
		        Environment.messageError(xOut.StrValue);
		    }
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::Acs::User:General:Model:onCommand_unlock-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_unlock")
	protected  void onCommand_unlock (org.radixware.ads.Acs.explorer.User.Unlock command) {
		try{
		    command.send();
		    locked.Value = false;
		    invalidLogonCnt.Value = 0;
		    read();
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}

	}

	/*Radix::Acs::User:General:Model:onCommand_acceptRoles-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_acceptRoles")
	public  void onCommand_acceptRoles (org.radixware.ads.Acs.explorer.User.AcceptRights command) {
		try{
		    final CommandsXsd:AcceptedRolesDocument doc = command.send();    
		    AcsClientCommonUtils.processAcceptedCommand(doc, getEnvironment(), true);
		    
		    final boolean isNotSelector = !isInSelectorRowContext();

		    final Types::Id user2RoleId = idof[User:General:User2Role];
		    final Types::Id user2UserGroupId = idof[User:General:User2UserGroup];


		    final Explorer.Models::GroupModel rolesGroup = isNotSelector && isExplorerItemAccessible(user2RoleId) ? (Explorer.Models::GroupModel) this.getChildModel(user2RoleId) : null;
		    final Explorer.Models::GroupModel user2UserGroup = isNotSelector && isExplorerItemAccessible(user2UserGroupId) ? (Explorer.Models::GroupModel) this.getChildModel(user2UserGroupId) : null;
		                
		    if (rolesGroup!=null){
		        rolesGroup.reread();
		    }
		    
		    if (user2UserGroup!=null){
		        user2UserGroup.reread();
		    }
		        
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::Acs::User:General:Model:beforeAccept-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeAccept")
	public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument beforeAccept () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.Acs.explorer.User.BeforeAccept _cmd_cmdBCNKLHK2C5CNLAOPP44HLQEDXI_instance_ = (org.radixware.ads.Acs.explorer.User.BeforeAccept)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdBCNKLHK2C5CNLAOPP44HLQEDXI"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdBCNKLHK2C5CNLAOPP44HLQEDXI_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument ? (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) $rpc$call$result$ : (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument.class);

	}

	/*Radix::Acs::User:General:Model:updatePwdHash-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:updatePwdHash")
	private final  boolean updatePwdHash () {
		if  (name.Value == null || name.Value.isEmpty()){
		    getEnvironment().messageError("User name not defined");
		    name.setFocused();
		    return false;
		}
		Str userName = name.Value.trim();
		if (Acs.Dlg::PasswordDialogController.checkPasswordRequirements(passwordRequirements.Value,
		                                                                 rawPassword.Value,
		                                                                 userName,
		                                                                 getEnvironment())
		    ){
		    final PasswordHash pwdHash = PasswordHash.Factory.newInstance(userName, rawPassword.Value);
		    final byte[] bytes = pwdHash.export();
		    pwdHashBytes.Value = Bin.wrap(bytes);
		    pwdHash.erase();
		    final Str modifyUser = this.name.Value;
		    final Str currUser = getEnvironment().UserName;
		    getEnvironment().Tracer.event(  String.format("User %s set password for user %s ", currUser, modifyUser) );    
		    return true;
		}else{
		    return false;
		}
	}

	/*Radix::Acs::User:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		boolean rez = super.beforeUpdate();

		if (rez && rawPassword.Value !=null && name.Value!=null){
		    name.Value = name.Value.trim();//RADIX-11527 
		    if (!updatePwdHash()){
		        return false;
		    }
		}

		return rez;
	}
	public final class GenerateReport extends org.radixware.ads.Acs.explorer.User.GenerateReport{
		protected GenerateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_GenerateReport( this );
		}

	}

	public static class BeforeAccept extends org.radixware.ads.Acs.explorer.User.BeforeAccept{
		protected BeforeAccept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class AcceptRights extends org.radixware.ads.Acs.explorer.User.AcceptRights{
		protected AcceptRights(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_AcceptRights( this );
		}

	}

	public final class MoveRightsToGroup extends org.radixware.ads.Acs.explorer.User.MoveRightsToGroup{
		protected MoveRightsToGroup(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveRightsToGroup( this );
		}

	}

	public final class Unlock extends org.radixware.ads.Acs.explorer.User.Unlock{
		protected Unlock(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Unlock( this );
		}

	}





















}

/* Radix::Acs::User:General:Model - Desktop Meta*/

/*Radix::Acs::User:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemK3FTXQSCQRAAJPVNYCKOQSCK7U"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:General:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::Acs::User:General:Model:rawPassword:rawPassword:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSGL3QG35NVANTBGL7EJW7HAXPI"),
									"rawPassword",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
									org.radixware.kernel.common.enums.EValType.STR,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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

									/*Radix::Acs::User:General:Model:rawPassword:rawPassword:PropertyPresentation:Edit Options:-Edit Mask Str*/
									new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
									null,
									null,
									null,
									true,-1,-1,1,
									false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
						},
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

/* Radix::Acs::User:General:Model - Web Executable*/

/*Radix::Acs::User:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;

import org.radixware.kernel.common.auth.PasswordHash;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.User.User_DefaultModel implements org.radixware.ads.Reports.common_client.IReportPubModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:General:Model:Properties-Properties*/

	/*Radix::Acs::User:General:Model:passwordDefined-Presentation Property*/




	public class PasswordDefined extends org.radixware.ads.Acs.web.User.prd6IFAQA7VIFF2TK56SOTVEJR6GI{
		public PasswordDefined(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:passwordDefined")
		public published  Str getValue() {

			if ( pwdHashBytes.getValue()!=null || encryptedPwdHash.getValue()!=null || rawPassword.getValue()!=null ){
			    return "<defined>";
			}
			return null;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:passwordDefined")
		public published   void setValue(Str val) {
			Value = val;
		}
	}
	public PasswordDefined getPasswordDefined(){return (PasswordDefined)getProperty(prd6IFAQA7VIFF2TK56SOTVEJR6GI);}

	/*Radix::Acs::User:General:Model:name-Presentation Property*/




	public class Name extends org.radixware.ads.Acs.web.User.colEF6ODCYWY3NBDGMCABQAQH3XQ4{
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:name")
		public published  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:name")
		public published   void setValue(Str val) {

			internal[name] = val;
			//.setEnabled(val!=null &&  !val.isEmpty());
			//.(null);
			//.afterModify();
		}
	}
	public Name getName(){return (Name)getProperty(colEF6ODCYWY3NBDGMCABQAQH3XQ4);}

	/*Radix::Acs::User:General:Model:totalLock-Presentation Property*/




	public class TotalLock extends org.radixware.ads.Acs.web.User.prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE{
		public TotalLock(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemK3FTXQSCQRAAJPVNYCKOQSCK7U.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU"))) {
				this.addDependent(aemK3FTXQSCQRAAJPVNYCKOQSCK7U.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU")));
			}
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:totalLock")
		public published  Bool getValue() {

			return locked.Value != null && locked.Value.booleanValue() 
			       || autoLocked.Value != null && autoLocked.Value.booleanValue() 
			       || isTemporaryPwdExpired.Value != null && isTemporaryPwdExpired.Value.booleanValue();
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:totalLock")
		public published   void setValue(Bool val) {

			if (!val.booleanValue())
			{
			  invalidLogonCnt.Value = 0; 
			  autoLocked.Value = false;
			} 

			locked.Value = val;
			if(Boolean.TRUE.equals(val) && finalLockReason.Value == null){
			    lockReason.Value = AccountLockReason:BY_ADMIN;
			    finalLockReason.Value = AccountLockReason:BY_ADMIN;
			}else{
			    lockReason.cancelChanges();
			    finalLockReason.cancelChanges();
			}
			internal[totalLock] = val;


			totalLock.afterModify();

			checkCommandEnabled();

		}
	}
	public TotalLock getTotalLock(){return (TotalLock)getProperty(prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE);}

	/*Radix::Acs::User:General:Model:isMaySetNullForAdminGroup-Presentation Property*/




	public class IsMaySetNullForAdminGroup extends org.radixware.ads.Acs.web.User.colHLIAZG6JPFEGFFGAOORNELFSCA{
		public IsMaySetNullForAdminGroup(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:isMaySetNullForAdminGroup")
		public published  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:isMaySetNullForAdminGroup")
		public published   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsMaySetNullForAdminGroup getIsMaySetNullForAdminGroup(){return (IsMaySetNullForAdminGroup)getProperty(colHLIAZG6JPFEGFFGAOORNELFSCA);}

	/*Radix::Acs::User:General:Model:authTypes-Presentation Property*/




	public class AuthTypes extends org.radixware.ads.Acs.web.User.colDZ5HBUOKRVBUXB5LM7WNIBBFP4{
		public AuthTypes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Acs.common.AuthType.Arr> getValClass(){
			return org.radixware.ads.Acs.common.AuthType.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Acs.common.AuthType.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Acs.common.AuthType.Arr ? (org.radixware.ads.Acs.common.AuthType.Arr)x : new org.radixware.ads.Acs.common.AuthType.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:authTypes")
		public  org.radixware.ads.Acs.common.AuthType.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:authTypes")
		public   void setValue(org.radixware.ads.Acs.common.AuthType.Arr val) {

			internal[authTypes] = val;
			applyAuthTypes();
		}
	}
	public AuthTypes getAuthTypes(){return (AuthTypes)getProperty(colDZ5HBUOKRVBUXB5LM7WNIBBFP4);}

	/*Radix::Acs::User:General:Model:finalLockReason-Presentation Property*/




	public class FinalLockReason extends org.radixware.ads.Acs.web.User.prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU{
		public FinalLockReason(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EAccountLockReason> getValClass(){
			return org.radixware.kernel.common.enums.EAccountLockReason.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EAccountLockReason dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EAccountLockReason ? (org.radixware.kernel.common.enums.EAccountLockReason)x : org.radixware.kernel.common.enums.EAccountLockReason.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:finalLockReason:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:finalLockReason:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:finalLockReason:Properties-Properties*/

		/*Radix::Acs::User:General:Model:finalLockReason:Methods-Methods*/

		/*Radix::Acs::User:General:Model:finalLockReason:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:finalLockReason:isVisible")
		public published  boolean isVisible () {
			if (totalLock.Value == null || !Boolean.TRUE.equals(totalLock.Value))
			    return false;
			return super.isVisible();
		}






				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:finalLockReason")
		public  org.radixware.kernel.common.enums.EAccountLockReason getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:finalLockReason")
		public   void setValue(org.radixware.kernel.common.enums.EAccountLockReason val) {
			Value = val;
		}
	}
	public FinalLockReason getFinalLockReason(){return (FinalLockReason)getProperty(prdNX5SOHMJ6JDKFGZ3M6Z54PVNXU);}

	/*Radix::Acs::User:General:Model:pwdHashBytes-Presentation Property*/




	public class PwdHashBytes extends org.radixware.ads.Acs.web.User.prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M{
		public PwdHashBytes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdHashBytes")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdHashBytes")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {

			if (val!=null && getEnvironment().getEasSession().isSessionKeyAccessible()){
			    final byte[] rawPwdHash = val.get();
			    final byte[] encryptedPwdHash = getEnvironment().getEasSession().encryptBySessionKey(rawPwdHash);
			    java.util.Arrays.fill(rawPwdHash, (byte)0);
			    encryptedPwdHash.setValue(Bin.wrap(encryptedPwdHash));        
			}else{
			    internal[pwdHashBytes] = val;
			}
			passwordDefined.afterModify();
		}
	}
	public PwdHashBytes getPwdHashBytes(){return (PwdHashBytes)getProperty(prdOSCJI6ZRKJDYRJ7RJS7VMH5E3M);}

	/*Radix::Acs::User:General:Model:rawPassword-Presentation Property*/




	public class RawPassword extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RawPassword(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:rawPassword")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:rawPassword")
		public   void setValue(Str val) {

			internal[rawPassword] = val;
			passwordDefined.afterModify();
		}
	}
	public RawPassword getRawPassword(){return (RawPassword)getProperty(prdSGL3QG35NVANTBGL7EJW7HAXPI);}

	/*Radix::Acs::User:General:Model:pwdExpirationPeriod-Presentation Property*/




	public class PwdExpirationPeriod extends org.radixware.ads.Acs.web.User.col5XNCT3QYY3NBDGMCABQAQH3XQ4{
		public PwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:Properties-Properties*/

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:Methods-Methods*/

		/*Radix::Acs::User:General:Model:pwdExpirationPeriod:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdExpirationPeriod:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			final Client.Validators::ValidationResult result = super.validateValue();
			if (result.getState()==org.radixware.kernel.common.client.meta.mask.validators.EValidatorState.ACCEPTABLE
			    && getValue()!=null
			    && systemPwdExpirationPeriod.getValue()!=null    
			    && systemPwdExpirationPeriod.getValue().longValue()<getValue().longValue()){
			    final String reason = String.format("Validity period of user password cannot exceed global value specified in system settings (%s).", systemPwdExpirationPeriod.getValue());
			    return Client.Validators::ValidationResult.Factory.newInvalidResult(reason);
			}
			return result;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:pwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdExpirationPeriod getPwdExpirationPeriod(){return (PwdExpirationPeriod)getProperty(col5XNCT3QYY3NBDGMCABQAQH3XQ4);}

	/*Radix::Acs::User:General:Model:encryptedPwdHash-Presentation Property*/




	public class EncryptedPwdHash extends org.radixware.ads.Acs.web.User.prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ{
		public EncryptedPwdHash(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.Bin dummy = ((org.radixware.kernel.common.types.Bin)x);
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:encryptedPwdHash:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:encryptedPwdHash:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:encryptedPwdHash:Properties-Properties*/

		/*Radix::Acs::User:General:Model:encryptedPwdHash:Methods-Methods*/

		/*Radix::Acs::User:General:Model:encryptedPwdHash:setServerValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:encryptedPwdHash:setServerValue")
		public published  void setServerValue (org.radixware.kernel.common.client.models.items.properties.PropertyValue value) {
			super.setServerValue(value);
			rawPassword.Value = null;
			rawPassword.setValEdited(false);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:encryptedPwdHash")
		public  org.radixware.kernel.common.types.Bin getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:encryptedPwdHash")
		public   void setValue(org.radixware.kernel.common.types.Bin val) {
			Value = val;
		}
	}
	public EncryptedPwdHash getEncryptedPwdHash(){return (EncryptedPwdHash)getProperty(prd3N2JFEJ2VVD3BC2OPXNMJZFTGQ);}

	/*Radix::Acs::User:General:Model:temporaryPwdStartTime-Presentation Property*/




	public class TemporaryPwdStartTime extends org.radixware.ads.Acs.web.User.colO3BCFXYVBNH65NBW7YHFNUX7BA{
		public TemporaryPwdStartTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			java.sql.Timestamp dummy = ((java.sql.Timestamp)x);
			setValue(dummy);
		}

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:Properties-Properties*/

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:Methods-Methods*/

		/*Radix::Acs::User:General:Model:temporaryPwdStartTime:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:temporaryPwdStartTime:isVisible")
		public published  boolean isVisible () {
			return getValue()!=null;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:temporaryPwdStartTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:temporaryPwdStartTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public TemporaryPwdStartTime getTemporaryPwdStartTime(){return (TemporaryPwdStartTime)getProperty(colO3BCFXYVBNH65NBW7YHFNUX7BA);}




























	/*Radix::Acs::User:General:Model:Methods-Methods*/

	/*Radix::Acs::User:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		checkCommandEnabled();
		totalLock.afterModify();
		//.setEnabled(.Value != null && !.Value.isEmpty());

		final boolean isNotSelector = !isInSelectorRowContext();
		if (isNotSelector){
		    applyAuthTypes();
		}
		try { 
		           
		    final Bool usetDualControl = usedDualControl.Value;
		    final boolean bUsetDualControl = usetDualControl!=null && usetDualControl.booleanValue();
		    this.getCommand(idof[User:AcceptRights]).setVisible(bUsetDualControl);
		    
		    if (bUsetDualControl){
		        final Bool curUserCanAccept = curUserCanAccept.Value;
		        final boolean bCurUserCanAccept = curUserCanAccept!=null && curUserCanAccept.booleanValue();
		        this.getCommand(idof[User:AcceptRights]).setEnabled(bCurUserCanAccept);
		    }
		    
		    if (!isNew()) {
		         

		        final Explorer.Models::GroupModel rolesGroup = isNotSelector && isExplorerItemAccessible(idof[User:General:User2Role])
		                ? (Explorer.Models::GroupModel) this.getChildModel(idof[User:General:User2Role])
		                : null;

		//        final  groupsGroup = isNotSelector && ()
		//                ? () this.()
		//                : null;

		        if (rolesGroup != null) {
		            rolesGroup.getSelectorColumn(idof[User2Role:userTitle]).setVisible(false);
		        }

		        if (isNotSelector) {
		            Boolean isMayChangeAdminGroup = (Boolean) isMaySetNullForAdminGroup.ValueObject;
		            this.adminGroup.setMandatory(
		                    isMayChangeAdminGroup != null
		                    && !isMayChangeAdminGroup.booleanValue());
		        }

		        if (name.Value == Environment.UserName) {


		            if (isNotSelector) {

		                if (rolesGroup != null) {
		                    rolesGroup.getRestrictions().setCreateRestricted(true);
		                    rolesGroup.getRestrictions().setDeleteRestricted(true);
		                }
		//                if (groupsGroup != null) {
		//                     RADIX-7681
		//                    groupsGroup.getRestrictions().setCreateRestricted(true);
		//                    groupsGroup.getRestrictions().setDeleteRestricted(true);
		//                }
		                getRestrictions().setDeleteRestricted(true);


		                this.getCommand(idof[User:MoveRightsToGroup]).setEnabled(false);
		            }
		            totalLock.setReadonly(true);



		        } else {
		            totalLock.setReadonly(totalLock.getValue()==Boolean.TRUE);
		            this.getCommand(idof[User:MoveRightsToGroup]).setEnabled(true);
		        }
		    }
		    else{
		        this.getCommand(idof[User:AcceptRights]).setVisible(false);
		    }

		} catch (Exceptions::InterruptedException ex) {
		    showException(ex);
		} catch (Exceptions::ServiceClientException ex) {
		    showException(ex);
		}
	}

	/*Radix::Acs::User:General:Model:checkCommandEnabled-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:checkCommandEnabled")
	public published  void checkCommandEnabled () {


		boolean isAutoLock = autoLocked.Value != null && autoLocked.Value.booleanValue();
		boolean isLock;


		if (this.isNew())
		    isLock = false;
		else
		    isLock = isAutoLock
		            || (locked.Value != null && locked.Value.booleanValue())
		            || (isTemporaryPwdExpired.Value != null && isTemporaryPwdExpired.Value.booleanValue());
		this.getCommand(idof[User:Unlock]).setEnabled(isLock);
		invalidLogonTime.setVisible(isAutoLock);
	}

	/*Radix::Acs::User:General:Model:delete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:delete")
	public published  boolean delete (boolean forced) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceClientException {
		if (Environment.messageConfirmation(
		                    "Confirm Object Deletion", 
		                    "Do you really want to delete" +
		                     " \'" + name.ValueAsString + "\'"
		                    ))
		    return super.delete(true);
		return false;
	}

	/*Radix::Acs::User:General:Model:askGroupName-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:askGroupName")
	public  Str askGroupName () {
		Web.Widgets::InputValueDialog inputValueDialog = new InputValueDialog(
		                Meta::ValType:Str, new EditMaskStr(), "Group Name", getEnvironment());
		                inputValueDialog.setWindowTitle("Move Rights to Group");
		return (Str)inputValueDialog.getValue();
	}

	/*Radix::Acs::User:General:Model:beforePreparePaste-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforePreparePaste")
	protected published  boolean beforePreparePaste (org.radixware.kernel.common.client.models.EntityModel sourceModel) {
		pwdHashBytes.Value = null;
		encryptedPwdHash.Value = null;
		rawPassword.Value = null;
		return super.beforePreparePaste(sourceModel);
	}

	/*Radix::Acs::User:General:Model:getContextClassId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:getContextClassId")
	public published  org.radixware.kernel.common.types.Id getContextClassId () {
		return idof[User];

	}

	/*Radix::Acs::User:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:getContextId")
	public published  Str getContextId () {
		return name.Value;
	}

	/*Radix::Acs::User:General:Model:getParentContext-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:getParentContext")
	public published  org.radixware.ads.Reports.common_client.IReportPubModel getParentContext () {
		return new Reports::CommonReportContext();
	}

	/*Radix::Acs::User:General:Model:onCommand_GenerateReport-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_GenerateReport")
	public  void onCommand_GenerateReport (org.radixware.ads.Acs.web.User.GenerateReport command) {
		Reports::ReportsExplorerUtils.generateReport(
		        command, 
		        idof[ReportPubList.User]);

	}

	/*Radix::Acs::User:General:Model:applyAuthTypes-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:applyAuthTypes")
	protected published  void applyAuthTypes () {

		org.radixware.kernel.common.types.Arr<AuthType> authTypes = authTypes.Value;

		boolean isPasswordEnabled = authTypes == null || authTypes.contains(AuthType:PASSWORD);

		if (isPasswordEnabled) {
		    passwordDefined.setVisible(true);
		    pwdExpirationPeriod.setVisible(true);
		    mustChangePwd.setVisible(true);
		    lastPwdChangeTime.setVisible(true);
		} else {
		    passwordDefined.setVisible(false);
		    pwdExpirationPeriod.setVisible(false);
		    mustChangePwd.setVisible(false);
		    lastPwdChangeTime.setVisible(false);
		}
	}

	/*Radix::Acs::User:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		boolean rez = super.beforeCreate();

		if (rez && rawPassword.Value!=null && name.Value!=null){
		    name.Value = name.Value.trim();//RADIX-11527 
		    if (!updatePwdHash()){
		        return false;
		    }
		}

		return rez;
	}

	/*Radix::Acs::User:General:Model:onCommand_moveRightsToGroup-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_moveRightsToGroup")
	protected  void onCommand_moveRightsToGroup (org.radixware.ads.Acs.web.User.MoveRightsToGroup command) {
		try{
		    Str groupName = askGroupName();
		    if (groupName == null || groupName.isEmpty())    
		        return;
		    
		    CommandsXsd:StrValueDocument xIn = CommandsXsd:StrValueDocument.Factory.newInstance();        
		    xIn.StrValue = groupName;
		    CommandsXsd:StrValueDocument xOut = command.send(xIn);

		    if (xOut.StrValue == null || xOut.StrValue.isEmpty())
		    {
		        //all ok - refresh list
		        final Explorer.Models::GroupModel rolesGroup = (Explorer.Models::GroupModel)this.getChildModel(idof[User:General:User2Role]);
		        final Explorer.Models::GroupModel groupsGroup = (Explorer.Models::GroupModel)this.getChildModel(idof[User:General:User2UserGroup]);        
		        rolesGroup.reread();
		        groupsGroup.reread();
		    }
		    else
		    {
		        Environment.messageError(xOut.StrValue);
		    }
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::Acs::User:General:Model:onCommand_unlock-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_unlock")
	protected  void onCommand_unlock (org.radixware.ads.Acs.web.User.Unlock command) {
		try{
		    command.send();
		    locked.Value = false;
		    invalidLogonCnt.Value = 0;
		    read();
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}

	}

	/*Radix::Acs::User:General:Model:onCommand_acceptRoles-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:onCommand_acceptRoles")
	public  void onCommand_acceptRoles (org.radixware.ads.Acs.web.User.AcceptRights command) {
		try{
		    final CommandsXsd:AcceptedRolesDocument doc = command.send();    
		    AcsClientCommonUtils.processAcceptedCommand(doc, getEnvironment(), true);
		    
		    final boolean isNotSelector = !isInSelectorRowContext();

		    final Types::Id user2RoleId = idof[User:General:User2Role];
		    final Types::Id user2UserGroupId = idof[User:General:User2UserGroup];


		    final Explorer.Models::GroupModel rolesGroup = isNotSelector && isExplorerItemAccessible(user2RoleId) ? (Explorer.Models::GroupModel) this.getChildModel(user2RoleId) : null;
		    final Explorer.Models::GroupModel user2UserGroup = isNotSelector && isExplorerItemAccessible(user2UserGroupId) ? (Explorer.Models::GroupModel) this.getChildModel(user2UserGroupId) : null;
		                
		    if (rolesGroup!=null){
		        rolesGroup.reread();
		    }
		    
		    if (user2UserGroup!=null){
		        user2UserGroup.reread();
		    }
		        
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::Acs::User:General:Model:beforeAccept-Remote Procedure Call Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeAccept")
	public  org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument beforeAccept () throws org.radixware.kernel.common.exceptions.ServiceClientException,java.lang.InterruptedException {
		final org.radixware.ads.Acs.web.User.BeforeAccept _cmd_cmdBCNKLHK2C5CNLAOPP44HLQEDXI_instance_ = (org.radixware.ads.Acs.web.User.BeforeAccept)getCommand(org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdBCNKLHK2C5CNLAOPP44HLQEDXI"));
		final java.util.List<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo> $remote$call$arg$list$store$ = new java.util.LinkedList<org.radixware.kernel.common.utils.RPCProcessor.ArgumentInfo>();
		final org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation $invoker_instance$ = new org.radixware.kernel.common.utils.RPCProcessor.ClientSideInvocation($remote$call$arg$list$store$){
			protected org.radixware.schemas.utils.RPCResponseDocument invokeImpl(org.radixware.schemas.utils.RPCRequestDocument rq) throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
				return _cmd_cmdBCNKLHK2C5CNLAOPP44HLQEDXI_instance_.send(rq);
			}
		};
		final Object $rpc$call$result$ = $invoker_instance$.invoke();
		return $rpc$call$result$ == null ? null : $rpc$call$result$ instanceof org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument ? (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) $rpc$call$result$ : (org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument) org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)$rpc$call$result$,org.radixware.ads.Acs.common.CommandsXsd.AcceptedRightsCheckValueDocument.class);

	}

	/*Radix::Acs::User:General:Model:updatePwdHash-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:updatePwdHash")
	private final  boolean updatePwdHash () {
		if  (name.Value == null || name.Value.isEmpty()){
		    getEnvironment().messageError("User name not defined");
		    name.setFocused();
		    return false;
		}
		Str userName = name.Value.trim();
		if (Acs.Dlg::PasswordDialogController.checkPasswordRequirements(passwordRequirements.Value,
		                                                                 rawPassword.Value,
		                                                                 userName,
		                                                                 getEnvironment())
		    ){
		    final PasswordHash pwdHash = PasswordHash.Factory.newInstance(userName, rawPassword.Value);
		    final byte[] bytes = pwdHash.export();
		    pwdHashBytes.Value = Bin.wrap(bytes);
		    pwdHash.erase();
		    final Str modifyUser = this.name.Value;
		    final Str currUser = getEnvironment().UserName;
		    getEnvironment().Tracer.event(  String.format("User %s set password for user %s ", currUser, modifyUser) );    
		    return true;
		}else{
		    return false;
		}
	}

	/*Radix::Acs::User:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		boolean rez = super.beforeUpdate();

		if (rez && rawPassword.Value !=null && name.Value!=null){
		    name.Value = name.Value.trim();//RADIX-11527 
		    if (!updatePwdHash()){
		        return false;
		    }
		}

		return rez;
	}
	public final class GenerateReport extends org.radixware.ads.Acs.web.User.GenerateReport{
		protected GenerateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_GenerateReport( this );
		}

	}

	public static class BeforeAccept extends org.radixware.ads.Acs.web.User.BeforeAccept{
		protected BeforeAccept(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.utils.RPCResponseDocument send(org.radixware.schemas.utils.RPCRequestDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.utils.RPCResponseDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.utils.RPCResponseDocument.class);
		}

	}

	public final class AcceptRights extends org.radixware.ads.Acs.web.User.AcceptRights{
		protected AcceptRights(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_AcceptRights( this );
		}

	}

	public final class MoveRightsToGroup extends org.radixware.ads.Acs.web.User.MoveRightsToGroup{
		protected MoveRightsToGroup(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveRightsToGroup( this );
		}

	}

	public final class Unlock extends org.radixware.ads.Acs.web.User.Unlock{
		protected Unlock(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Unlock( this );
		}

	}





















}

/* Radix::Acs::User:General:Model - Web Meta*/

/*Radix::Acs::User:General:Model-Entity Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemK3FTXQSCQRAAJPVNYCKOQSCK7U"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:General:Model:Properties-Properties*/
						new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

								/*Radix::Acs::User:General:Model:rawPassword:rawPassword:PropertyPresentation-Property Presentation*/
								new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSGL3QG35NVANTBGL7EJW7HAXPI"),
									"rawPassword",
									null,
									null,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
									org.radixware.kernel.common.enums.EValType.STR,
									org.radixware.kernel.common.enums.EPropNature.VIRTUAL,
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

									/*Radix::Acs::User:General:Model:rawPassword:rawPassword:PropertyPresentation:Edit Options:-Edit Mask Str*/
									new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
									null,
									null,
									null,
									true,-1,-1,1,
									false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
						},
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

/* Radix::Acs::User:General - Desktop Meta*/

/*Radix::Acs::User:General-Selector Presentation*/

package org.radixware.ads.Acs.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),
		null,
		false,
		true,
		null,
		104,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U")},
		false,true,false,346,346);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZFLQEDYDZAQLHBFOS3UHG6VV4")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::User:General - Web Meta*/

/*Radix::Acs::User:General-Selector Presentation*/

package org.radixware.ads.Acs.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZONO647CNRGC5O5GHBHD6F7C7A"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecSY4KIOLTGLNRDHRZABQAQH3XQ4"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtITTPLGXSABANNEXACIX23PBTPY"),
		null,
		false,
		true,
		null,
		104,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprK3FTXQSCQRAAJPVNYCKOQSCK7U")},
		false,true,false,222,222);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZFLQEDYDZAQLHBFOS3UHG6VV4")),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3FYUIAY5J3NRDJIRACQMTAIZT4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdM2CJTKA4Y5CQ3M4TNAJAE3SMEE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ62TN7IYY3NBDGMCABQAQH3XQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWO5IPEW7JBH5XMNUNXDUZVMRFY"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3TLLWOMV6NBZNFVIIQ55ZSCOEM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdINOLKO7HQNFYPFXW5XRIR22GVU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCV5XGXGMEFAK3HNOJVFQEW2NGI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Acs::User:General:Model - Desktop Executable*/

/*Radix::Acs::User:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model")
public class General:Model  extends org.radixware.ads.Acs.explorer.User.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:General:Model:CurrentEntityHandler-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:CurrentEntityHandler")
	public class CurrentEntityHandler  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {

		private Explorer.Models::EntityModel current;

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:Properties-Properties*/

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:Methods-Methods*/

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:CurrentEntityHandler:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {
			if (current != null && current.isExists()) {
			    try {
			        current.read();
			    } catch (InterruptedException | Explorer.Exceptions::ObjectNotFoundError e) {    
			    } catch (Exceptions::ServiceClientException e) {
			        getEnvironment().getTracer().error(e);
			    }
			    finally {
			        current = null;
			    }
			}
		}

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:CurrentEntityHandler:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			current = entity;
		}


	}

	/*Radix::Acs::User:General:Model:Properties-Properties*/

	/*Radix::Acs::User:General:Model:Methods-Methods*/

	/*Radix::Acs::User:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandler()) ;

	}


}

/* Radix::Acs::User:General:Model - Desktop Meta*/

/*Radix::Acs::User:General:Model-Group Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmZONO647CNRGC5O5GHBHD6F7C7A"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:General:Model:Properties-Properties*/
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

/* Radix::Acs::User:General:Model - Web Executable*/

/*Radix::Acs::User:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model")
public class General:Model  extends org.radixware.ads.Acs.web.User.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Acs::User:General:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:General:Model:CurrentEntityHandler-Client-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:CurrentEntityHandler")
	public class CurrentEntityHandler  implements org.radixware.kernel.common.client.views.ISelector.CurrentEntityHandler  {

		private Explorer.Models::EntityModel current;

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:Nested classes-Nested Classes*/

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:Properties-Properties*/

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:Methods-Methods*/

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:onLeaveCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:CurrentEntityHandler:onLeaveCurrentEntity")
		public published  void onLeaveCurrentEntity () {
			if (current != null && current.isExists()) {
			    try {
			        current.read();
			    } catch (InterruptedException | Explorer.Exceptions::ObjectNotFoundError e) {    
			    } catch (Exceptions::ServiceClientException e) {
			        getEnvironment().getTracer().error(e);
			    }
			    finally {
			        current = null;
			    }
			}
		}

		/*Radix::Acs::User:General:Model:CurrentEntityHandler:onSetCurrentEntity-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:CurrentEntityHandler:onSetCurrentEntity")
		public published  void onSetCurrentEntity (org.radixware.kernel.common.client.models.EntityModel entity) {
			current = entity;
		}


	}

	/*Radix::Acs::User:General:Model:Properties-Properties*/

	/*Radix::Acs::User:General:Model:Methods-Methods*/

	/*Radix::Acs::User:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		this.GroupView.addCurrentEntityHandler(new CurrentEntityHandler()) ;

	}


}

/* Radix::Acs::User:General:Model - Web Meta*/

/*Radix::Acs::User:General:Model-Group Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmZONO647CNRGC5O5GHBHD6F7C7A"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:General:Model:Properties-Properties*/
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

/* Radix::Acs::User:Name:Model - Desktop Executable*/

/*Radix::Acs::User:Name:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:Model")
public class Name:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Name:Model_mi.rdxMeta; }



	public Name:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:Name:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Name:Model:Properties-Properties*/








	/*Radix::Acs::User:Name:Model:Methods-Methods*/

	/*Radix::Acs::User:Name:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (name.Value==null)
		    name.Value="%";

	}

	/*Radix::Acs::User:Name:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:Model:beforeApply")
	public published  boolean beforeApply () {
		if (name.Value!=null)
		    name.Value=name.Value.toUpperCase();
		return super.beforeApply();
	}

	/*Radix::Acs::User:Name:name:name-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName(){return (Name)getProperty(prmAQE3EICYKVD73LK744QJXSXOWM);}


}

/* Radix::Acs::User:Name:Model - Desktop Meta*/

/*Radix::Acs::User:Name:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Name:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcXDV2WXUPKRA6LPQ4VPQ5WOXCQM"),
						"Name:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:Name:Model:Properties-Properties*/
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

/* Radix::Acs::User:Name:Model - Web Executable*/

/*Radix::Acs::User:Name:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:Model")
public class Name:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Name:Model_mi.rdxMeta; }



	public Name:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:Name:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Name:Model:Properties-Properties*/








	/*Radix::Acs::User:Name:Model:Methods-Methods*/

	/*Radix::Acs::User:Name:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (name.Value==null)
		    name.Value="%";

	}

	/*Radix::Acs::User:Name:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:Model:beforeApply")
	public published  boolean beforeApply () {
		if (name.Value!=null)
		    name.Value=name.Value.toUpperCase();
		return super.beforeApply();
	}

	/*Radix::Acs::User:Name:name:name-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Name:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName(){return (Name)getProperty(prmAQE3EICYKVD73LK744QJXSXOWM);}


}

/* Radix::Acs::User:Name:Model - Web Meta*/

/*Radix::Acs::User:Name:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Name:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcXDV2WXUPKRA6LPQ4VPQ5WOXCQM"),
						"Name:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:Name:Model:Properties-Properties*/
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

/* Radix::Acs::User:PersonName:Model - Desktop Executable*/

/*Radix::Acs::User:PersonName:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:Model")
public class PersonName:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return PersonName:Model_mi.rdxMeta; }



	public PersonName:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:PersonName:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:PersonName:Model:Properties-Properties*/








	/*Radix::Acs::User:PersonName:Model:Methods-Methods*/

	/*Radix::Acs::User:PersonName:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:Model:beforeApply")
	public published  boolean beforeApply () {
		if (personName.Value!=null)
		    personName.Value=personName.Value.toUpperCase();
		return super.beforeApply();
	}

	/*Radix::Acs::User:PersonName:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (personName.Value==null)
		    personName.Value="%";
		    
	}

	/*Radix::Acs::User:PersonName:personName:personName-Presentation Property*/




	public class PersonName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PersonName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:personName:personName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:personName:personName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PersonName getPersonName(){return (PersonName)getProperty(prmMLERCKVL4BFCHE36AB3M2776QI);}


}

/* Radix::Acs::User:PersonName:Model - Desktop Meta*/

/*Radix::Acs::User:PersonName:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PersonName:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcSRG6OBEIWFB6HO3O55DYMRLDEY"),
						"PersonName:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:PersonName:Model:Properties-Properties*/
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

/* Radix::Acs::User:PersonName:Model - Web Executable*/

/*Radix::Acs::User:PersonName:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:Model")
public class PersonName:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return PersonName:Model_mi.rdxMeta; }



	public PersonName:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:PersonName:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:PersonName:Model:Properties-Properties*/








	/*Radix::Acs::User:PersonName:Model:Methods-Methods*/

	/*Radix::Acs::User:PersonName:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:Model:beforeApply")
	public published  boolean beforeApply () {
		if (personName.Value!=null)
		    personName.Value=personName.Value.toUpperCase();
		return super.beforeApply();
	}

	/*Radix::Acs::User:PersonName:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (personName.Value==null)
		    personName.Value="%";
		    
	}

	/*Radix::Acs::User:PersonName:personName:personName-Presentation Property*/




	public class PersonName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PersonName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:personName:personName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:PersonName:personName:personName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PersonName getPersonName(){return (PersonName)getProperty(prmMLERCKVL4BFCHE36AB3M2776QI);}


}

/* Radix::Acs::User:PersonName:Model - Web Meta*/

/*Radix::Acs::User:PersonName:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class PersonName:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcSRG6OBEIWFB6HO3O55DYMRLDEY"),
						"PersonName:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:PersonName:Model:Properties-Properties*/
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

/* Radix::Acs::User:Email:Model - Desktop Executable*/

/*Radix::Acs::User:Email:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:Model")
public class Email:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Email:Model_mi.rdxMeta; }



	public Email:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:Email:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Email:Model:Properties-Properties*/








	/*Radix::Acs::User:Email:Model:Methods-Methods*/

	/*Radix::Acs::User:Email:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:Model:beforeApply")
	public published  boolean beforeApply () {
		if (email.Value!=null)
		    email.Value = email.Value.toUpperCase();
		return super.beforeApply();
	}

	/*Radix::Acs::User:Email:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (email.Value == null)
		    email.Value = "%";
	}

	/*Radix::Acs::User:Email:email:email-Presentation Property*/




	public class Email extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Email(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:email:email")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:email:email")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Email getEmail(){return (Email)getProperty(prmJ3OGIPSYIVFEHMEIOJ7FQK72KE);}


}

/* Radix::Acs::User:Email:Model - Desktop Meta*/

/*Radix::Acs::User:Email:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Email:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcBTTJNCPNR5DADH4XGHF23GLUSI"),
						"Email:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:Email:Model:Properties-Properties*/
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

/* Radix::Acs::User:Email:Model - Web Executable*/

/*Radix::Acs::User:Email:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:Model")
public class Email:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Email:Model_mi.rdxMeta; }



	public Email:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:Email:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Email:Model:Properties-Properties*/








	/*Radix::Acs::User:Email:Model:Methods-Methods*/

	/*Radix::Acs::User:Email:Model:beforeApply-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:Model:beforeApply")
	public published  boolean beforeApply () {
		if (email.Value!=null)
		    email.Value = email.Value.toUpperCase();
		return super.beforeApply();
	}

	/*Radix::Acs::User:Email:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (email.Value == null)
		    email.Value = "%";
	}

	/*Radix::Acs::User:Email:email:email-Presentation Property*/




	public class Email extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Email(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:email:email")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Email:email:email")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Email getEmail(){return (Email)getProperty(prmJ3OGIPSYIVFEHMEIOJ7FQK72KE);}


}

/* Radix::Acs::User:Email:Model - Web Meta*/

/*Radix::Acs::User:Email:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Email:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcBTTJNCPNR5DADH4XGHF23GLUSI"),
						"Email:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:Email:Model:Properties-Properties*/
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

/* Radix::Acs::User:Phone:Model - Desktop Executable*/

/*Radix::Acs::User:Phone:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:Model")
public class Phone:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Phone:Model_mi.rdxMeta; }



	public Phone:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:Phone:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Phone:Model:Properties-Properties*/








	/*Radix::Acs::User:Phone:Model:Methods-Methods*/

	/*Radix::Acs::User:Phone:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (phone.Value==null)
		    phone.Value="%";
		    
	}

	/*Radix::Acs::User:Phone:phone:phone-Presentation Property*/




	public class Phone extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Phone(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:phone:phone")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:phone:phone")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Phone getPhone(){return (Phone)getProperty(prmSVYWMK4QDVEQFFGPIVHZ3GJHH4);}


}

/* Radix::Acs::User:Phone:Model - Desktop Meta*/

/*Radix::Acs::User:Phone:Model-Filter Model Class*/

package org.radixware.ads.Acs.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Phone:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcP2KKOGX34BGZJONNDRIFHXC7WI"),
						"Phone:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:Phone:Model:Properties-Properties*/
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

/* Radix::Acs::User:Phone:Model - Web Executable*/

/*Radix::Acs::User:Phone:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:Model")
public class Phone:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Phone:Model_mi.rdxMeta; }



	public Phone:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Acs::User:Phone:Model:Nested classes-Nested Classes*/

	/*Radix::Acs::User:Phone:Model:Properties-Properties*/








	/*Radix::Acs::User:Phone:Model:Methods-Methods*/

	/*Radix::Acs::User:Phone:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		if (phone.Value==null)
		    phone.Value="%";
		    
	}

	/*Radix::Acs::User:Phone:phone:phone-Presentation Property*/




	public class Phone extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Phone(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:phone:phone")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Acs::User:Phone:phone:phone")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Phone getPhone(){return (Phone)getProperty(prmSVYWMK4QDVEQFFGPIVHZ3GJHH4);}


}

/* Radix::Acs::User:Phone:Model - Web Meta*/

/*Radix::Acs::User:Phone:Model-Filter Model Class*/

package org.radixware.ads.Acs.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Phone:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcP2KKOGX34BGZJONNDRIFHXC7WI"),
						"Phone:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Acs::User:Phone:Model:Properties-Properties*/
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

/* Radix::Acs::User - Localizing Bundle */
package org.radixware.ads.Acs.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class User - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of sessions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество сессий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27NYQRCRRJEGXLPJQMUXDCSPYM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Administration group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Администрирующая группа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2PPPOAZDJVDVHC7HMI2UTJZW7Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inherited Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Унаследованные роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3HV5FLY6TVDHBAY5P7FHI3JXAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By person name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По Ф.И.О.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3UEP4UMOWRDW5KFJLH3FBDWO6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Created");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время создания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls45GPZMNIMRCU7CZLDQQGSSYYHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Locked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заблокирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ERS2ARWXVBU5K5VP4J3GBAQJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unknown");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неизвестна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4LM4P27AFVAAFAV3JHWRWJNCBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By phone number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По номеру телефона");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54S4Z4LOPBEI5KAYCE6SNEK2UI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By email");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По email");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54XSYWLOMNAWXEXAHAVIWCRDK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По имени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5HZX77BMPJC7ZCL4QX4ZYD53ME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Phone number like");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Номер телефона как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5ONI3E5YMZCWBEKGPA7IFUF544"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Temporarily locked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Временно заблокирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5PY3P6I5CNDJVGS77HWIBXLI6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Do you really want to delete");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вы действительно хотите удалить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls65QMVMQJSZCELF6F22CHR5YPTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6WWZGHT4XVCDPPSR6X4IDGPLQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User group already exists.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группа пользователей уже существует.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GURROSUM5HHXBCHIXOHBKMWJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By email");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По email");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XWDYKW2TJHJNEUJOJ5KATMYZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Logon time restriction");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ограничение времени логина");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA4IPRR4J2RDO3HWDYS5X57F2S4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Invalid logon time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время неверного ввода пароля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBCLRYTJC35E4NBJNHNMVFKPWMA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBDVE7PY5A5F7DLC565NNXYVHC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCNTHEUMGW5EHHN4EON3ECL3AXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Permitted authentication types");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разрешенные способы аутентификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4PMYISQIRCJJOZRGQFQTACGLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя группы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7LBOXR5YVGNNAZ326W4VNMLSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name like");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEA2TZQPO3VDJXMBMLJXUXIQBFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Own rights for this user not defined.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отсутствуют собственные права пользователя.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKCFTTC37FC37EHONIYOFXQVSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Users");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF3YUCJZIIRDFPARXX53HDOSLLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User %1 set password for user %2 ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь %1  изменил пароль пользователя %2");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFS7GENRX2FC3JETG7GAY77SM5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to revoke rights that you cannot assign");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка забрать права, которые вы не сможете назначить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWM54SL55JBFJCQLDKST6YKLWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Rights to Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенести права в группу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGG3ZU2FH25BT3FF7WYRJ6UMCZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<no restrictions>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<без ограничений>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGHGLDHI2RVCJLP63YAV343C6IA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Locked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Заблокирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYSFNKAXJVBWLEUW5JQUD5AZPU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Group Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя группы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH55IYR7LFJHZVBZ66D4EFZJADA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Email like");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Email как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH74ZLTZZMZBSZC2R5XQ6TQQOZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Email");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Email");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSY7MTF23ZE57K66FTROSC2QJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' disabled GUI activity tracing for user \'%2\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \"%1\" отключил трассировку активности в Проводнике для пользователя \"%2\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI4FGF3WTSFGEXPOOQ3KHQIXAWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attempt to assign exceeding rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Попытка дать права, которыми пользователь не обладает");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHCFSV77OBBDVJIXYLVF7IYF6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inherited Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Унаследованные роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ5AUXDYGYBCPNCP36JDXAB6NC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Rights to Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенести права в группу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ6DCR7PPFFCALIYPFNWHNTSCPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Rights to Group");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенести права в группу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIIPKT3JO5GPHBEYROHU5K7VIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User name not defined");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не задано имя пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJP3I2VC37NELLC65AAT5BD3VEU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Database trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWHL2HEZZBADZE3RMHNFGRBNLY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLGM5S2KDHJAARLOLGQIFNO4BIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Account name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя учетной записи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJ53S5PYRZGZTBTMFRE77NEL74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password change required");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Необходимо сменить пароль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZX6MH575FA2TPPKQT67VFTQUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"None");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отсутствуют");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMAGDPOMJ5NDHXBE3323TJJ6LF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password validity period (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок действия пароля (дней)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFG5FSDDB5GTLKYGGWVWKXFG4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unlock");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разблокировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMHQC7AJB65GRJCYWWDLQWY2L6U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New password matches the one used previously");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый пароль совпадает с одним из использованных ранее.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNI44EIGNPBFWHGZOYM7WX4C7XY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By phone number");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По номеру телефона");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZXFF3JXBNFN7DN5MP7VCQVDSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Check station");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверять станцию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPTC6DSITNBAVPNVASWEML6FPZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lock reason");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Причина блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQ3G6WSF66BEQDHEZGJQYKOUPPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Person name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ф.И.О.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGULLAHHBJHK5PXLSFJ25Y44U4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Own Roles");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Собственные роли");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQJBJGPNER5HM7PT4W772BJWB2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Administration group name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя администрирующей группы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOFXVHTPXRAL5ITLFXFU3APE4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Groups");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Группы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQVZQR4RFDZDJLC7B2RC23OSYR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Temporary password set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время установки временного пароля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRCBTJMWRM5C77IPYDQ6FW3ESTU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validity period of user password cannot exceed global value specified in system settings (%s).");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок действия пароля для пользователя не может быть больше значения, глобально заданного в системе (%s).");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRSGNS5JOWNFJLNEECVRBAMMRMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Person name like");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ф.И.О. как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRWCXBLNH4BBSHPVKXTBFVTQSOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password last changed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Последняя смена пароля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSOVIMNXICJG4ZAARHZLVX3XZKY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Mobile phone");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Мобильный телефон");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT4QJIFPKKZHEJDR2RGRUDSAA2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTF2ASJIRGRAB3OITDRRRKFGI7E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stations");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Станции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUSA6IKG7VBAJE5BRZG2QD57PM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTWJFJ647F5AJTOVO6WJO5Z4FPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Users");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUFD34XZ5QBBDFPFCAA4GJGXSEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User %s set password for user %s ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь %s  изменил пароль пользователя %s ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUXX4TZMUJZEKNIBSEPUNNYKA2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' modified database trace profile for user \'%2\'. Old value: \'%3\'. New value: \'%4\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \"%1\" изменил профиль трассировки в БД пользователя \"%2\". Старое значение: \"%3\". Новое значение: \"%4\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSWIS6VSURFATGNF36Y7UY2MSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By user name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По имени пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVSZRWJD3EFGVZFYW7LHL2ZNMIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validity period of user password cannot exceed global value specified in system settings (%s).");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок действия пароля для пользователя не может быть больше значения, глобально заданного в системе (%s).");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWXLFGDQEZNGSTH3TDMRTLHOYCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZFLQEDYDZAQLHBFOS3UHG6VV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Generate Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сгенерировать отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY53VJVMXANDMPASWUTYGV4WV34"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace GUI activity");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Трассировать активность в Проводнике");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY5T547WITND3FMF7GWOKZIVVYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По имени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYARASEC3WJEORB6ZRKBEWA4TWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYM22C2JIBJHWBKBN6JHQWAGLXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Confirm Object Deletion");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подтвердите удаление объекта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYODJLM3OHNCQVLE7GHZTDRKPV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Accept Rights");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Акцептовать права");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZ5CI2XOXI5GKPLD4XKHGNOT2ZI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<все>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZPRVITUOKNEEXBJMLGWWKYZBLM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(User - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecSY4KIOLTGLNRDHRZABQAQH3XQ4"),"User - Localizing Bundle",$$$items$$$);
}
