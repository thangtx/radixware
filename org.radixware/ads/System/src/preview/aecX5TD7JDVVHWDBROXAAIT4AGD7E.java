
/* Radix::System::System - Server Executable*/

/*Radix::System::System-Entity Class*/

package org.radixware.ads.System.server;

import java.util.Map;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System")
public final published class System  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	protected boolean isArteDbTraceProfileModified   = false;
	protected boolean isArteGuiTraceProfileModified  = false;
	protected boolean isArteFileTraceProfileModified = false;
	protected boolean isDefaultAuditSchemeIdModified = false;

	protected Str oldArteDbTraceProfile   = null;
	protected Str oldArteGuiTraceProfile  = null;
	protected Str oldArteFileTraceProfile = null;
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return System_mi.rdxMeta;}

	/*Radix::System::System:Nested classes-Nested Classes*/

	/*Radix::System::System:Properties-Properties*/

	/*Radix::System::System:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::System::System:name-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:name")
	public published  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:name")
	public published   void setName(Str val) {
		name = val;
	}

	/*Radix::System::System:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::System::System:blockUserInvalidLogonCnt-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonCnt")
	public published  Int getBlockUserInvalidLogonCnt() {
		return blockUserInvalidLogonCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonCnt")
	public published   void setBlockUserInvalidLogonCnt(Int val) {
		blockUserInvalidLogonCnt = val;
	}

	/*Radix::System::System:blockUserInvalidLogonMins-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonMins")
	public published  Int getBlockUserInvalidLogonMins() {
		return blockUserInvalidLogonMins;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonMins")
	public published   void setBlockUserInvalidLogonMins(Int val) {
		blockUserInvalidLogonMins = val;
	}

	/*Radix::System::System:askUserPwdAfterInactivity-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:askUserPwdAfterInactivity")
	public published  Bool getAskUserPwdAfterInactivity() {
		return askUserPwdAfterInactivity;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:askUserPwdAfterInactivity")
	public published   void setAskUserPwdAfterInactivity(Bool val) {
		askUserPwdAfterInactivity = val;
	}

	/*Radix::System::System:arteDbTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteDbTraceProfile")
	public published  Str getArteDbTraceProfile() {
		return arteDbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteDbTraceProfile")
	public published   void setArteDbTraceProfile(Str val) {

		if (!PersistentModifiedPropIds.contains(idof[System:arteDbTraceProfile])){
		    oldArteDbTraceProfile = arteDbTraceProfile;
		}
		internal[arteDbTraceProfile] = val;
	}

	/*Radix::System::System:arteFileTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteFileTraceProfile")
	public published  Str getArteFileTraceProfile() {
		return arteFileTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteFileTraceProfile")
	public published   void setArteFileTraceProfile(Str val) {

		if (!PersistentModifiedPropIds.contains(idof[System:arteFileTraceProfile])){
		    oldArteFileTraceProfile = arteFileTraceProfile;
		}
		internal[arteFileTraceProfile] = val;
	}

	/*Radix::System::System:arteGuiTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteGuiTraceProfile")
	public published  Str getArteGuiTraceProfile() {
		return arteGuiTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteGuiTraceProfile")
	public published   void setArteGuiTraceProfile(Str val) {

		if (!PersistentModifiedPropIds.contains(idof[System:arteGuiTraceProfile])){
		    oldArteGuiTraceProfile = arteGuiTraceProfile;
		}
		internal[arteGuiTraceProfile] = val;
	}

	/*Radix::System::System:easSessionActivityMins-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionActivityMins")
	public published  Int getEasSessionActivityMins() {
		return easSessionActivityMins;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionActivityMins")
	public published   void setEasSessionActivityMins(Int val) {
		easSessionActivityMins = val;
	}

	/*Radix::System::System:easSessionInactivityMins-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionInactivityMins")
	public published  Int getEasSessionInactivityMins() {
		return easSessionInactivityMins;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionInactivityMins")
	public published   void setEasSessionInactivityMins(Int val) {
		easSessionInactivityMins = val;
	}

	/*Radix::System::System:arteLanguage-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteLanguage")
	public published  org.radixware.kernel.common.enums.EIsoLanguage getArteLanguage() {
		return arteLanguage;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteLanguage")
	public published   void setArteLanguage(org.radixware.kernel.common.enums.EIsoLanguage val) {
		arteLanguage = val;
	}

	/*Radix::System::System:auditStorePeriod1-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod1")
	public published  Int getAuditStorePeriod1() {
		return auditStorePeriod1;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod1")
	public published   void setAuditStorePeriod1(Int val) {
		auditStorePeriod1 = val;
	}

	/*Radix::System::System:auditStorePeriod2-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod2")
	public published  Int getAuditStorePeriod2() {
		return auditStorePeriod2;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod2")
	public published   void setAuditStorePeriod2(Int val) {
		auditStorePeriod2 = val;
	}

	/*Radix::System::System:auditStorePeriod3-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod3")
	public published  Int getAuditStorePeriod3() {
		return auditStorePeriod3;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod3")
	public published   void setAuditStorePeriod3(Int val) {
		auditStorePeriod3 = val;
	}

	/*Radix::System::System:auditStorePeriod4-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod4")
	public published  Int getAuditStorePeriod4() {
		return auditStorePeriod4;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod4")
	public published   void setAuditStorePeriod4(Int val) {
		auditStorePeriod4 = val;
	}

	/*Radix::System::System:auditStorePeriod5-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod5")
	public published  Int getAuditStorePeriod5() {
		return auditStorePeriod5;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod5")
	public published   void setAuditStorePeriod5(Int val) {
		auditStorePeriod5 = val;
	}

	/*Radix::System::System:defaultAuditSchemeId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditSchemeId")
	public published  Str getDefaultAuditSchemeId() {
		return defaultAuditSchemeId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditSchemeId")
	public published   void setDefaultAuditSchemeId(Str val) {
		defaultAuditSchemeId = val;
	}

	/*Radix::System::System:eventStoreDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:eventStoreDays")
	public published  Int getEventStoreDays() {
		return eventStoreDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:eventStoreDays")
	public published   void setEventStoreDays(Int val) {
		eventStoreDays = val;
	}

	/*Radix::System::System:notificationStoreDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notificationStoreDays")
	public published  Int getNotificationStoreDays() {
		return notificationStoreDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notificationStoreDays")
	public published   void setNotificationStoreDays(Int val) {
		notificationStoreDays = val;
	}

	/*Radix::System::System:defaultAuditScheme-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditScheme")
	public published  org.radixware.ads.Audit.server.AuditScheme getDefaultAuditScheme() {
		return defaultAuditScheme;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditScheme")
	public published   void setDefaultAuditScheme(org.radixware.ads.Audit.server.AuditScheme val) {
		defaultAuditScheme = val;
	}

	/*Radix::System::System:profileMode-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileMode")
	public published  org.radixware.kernel.common.enums.EProfileMode getProfileMode() {
		return profileMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileMode")
	public published   void setProfileMode(org.radixware.kernel.common.enums.EProfileMode val) {
		profileMode = val;
	}

	/*Radix::System::System:profilePeriodSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profilePeriodSec")
	public published  Int getProfilePeriodSec() {
		return profilePeriodSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profilePeriodSec")
	public published   void setProfilePeriodSec(Int val) {
		profilePeriodSec = val;
	}

	/*Radix::System::System:profiledPrefixes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profiledPrefixes")
	public published  Str getProfiledPrefixes() {
		return profiledPrefixes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profiledPrefixes")
	public published   void setProfiledPrefixes(Str val) {
		profiledPrefixes = val;
	}

	/*Radix::System::System:uniquePwdSeqLen-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:uniquePwdSeqLen")
	public published  Int getUniquePwdSeqLen() {
		return uniquePwdSeqLen;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:uniquePwdSeqLen")
	public published   void setUniquePwdSeqLen(Int val) {
		uniquePwdSeqLen = val;
	}

	/*Radix::System::System:enableSensitiveTrace-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:enableSensitiveTrace")
	public published  Bool getEnableSensitiveTrace() {
		return enableSensitiveTrace;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:enableSensitiveTrace")
	public published   void setEnableSensitiveTrace(Bool val) {
		enableSensitiveTrace = val;
	}

	/*Radix::System::System:pwdMinLen-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMinLen")
	public published  Int getPwdMinLen() {
		return pwdMinLen;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMinLen")
	public published   void setPwdMinLen(Int val) {
		pwdMinLen = val;
	}

	/*Radix::System::System:pwdMustContainAChars-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainAChars")
	public published  Bool getPwdMustContainAChars() {
		return pwdMustContainAChars;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainAChars")
	public published   void setPwdMustContainAChars(Bool val) {
		pwdMustContainAChars = val;
	}

	/*Radix::System::System:pwdMustContainNChars-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainNChars")
	public published  Bool getPwdMustContainNChars() {
		return pwdMustContainNChars;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainNChars")
	public published   void setPwdMustContainNChars(Bool val) {
		pwdMustContainNChars = val;
	}

	/*Radix::System::System:profileLogStoreDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileLogStoreDays")
	public published  Int getProfileLogStoreDays() {
		return profileLogStoreDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileLogStoreDays")
	public published   void setProfileLogStoreDays(Int val) {
		profileLogStoreDays = val;
	}

	/*Radix::System::System:metricHistoryStoreDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:metricHistoryStoreDays")
	public published  Int getMetricHistoryStoreDays() {
		return metricHistoryStoreDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:metricHistoryStoreDays")
	public published   void setMetricHistoryStoreDays(Int val) {
		metricHistoryStoreDays = val;
	}

	/*Radix::System::System:pwdExpirationPeriod-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdExpirationPeriod")
	public published  Int getPwdExpirationPeriod() {
		return pwdExpirationPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdExpirationPeriod")
	public published   void setPwdExpirationPeriod(Int val) {
		pwdExpirationPeriod = val;
	}

	/*Radix::System::System:arteCountry-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteCountry")
	public published  org.radixware.kernel.common.enums.EIsoCountry getArteCountry() {
		return arteCountry;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteCountry")
	public published   void setArteCountry(org.radixware.kernel.common.enums.EIsoCountry val) {
		arteCountry = val;
	}

	/*Radix::System::System:certAttrForUserLogin-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:certAttrForUserLogin")
	public published  Str getCertAttrForUserLogin() {
		return certAttrForUserLogin;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:certAttrForUserLogin")
	public published   void setCertAttrForUserLogin(Str val) {
		certAttrForUserLogin = val;
	}

	/*Radix::System::System:easKrbPrincName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easKrbPrincName")
	public published  Str getEasKrbPrincName() {
		return easKrbPrincName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easKrbPrincName")
	public published   void setEasKrbPrincName(Str val) {
		easKrbPrincName = val;
	}

	/*Radix::System::System:userExtDevLanguages-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:userExtDevLanguages")
	public published  org.radixware.ads.Common.common.Language.Arr getUserExtDevLanguages() {
		return userExtDevLanguages;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:userExtDevLanguages")
	public published   void setUserExtDevLanguages(org.radixware.ads.Common.common.Language.Arr val) {
		userExtDevLanguages = val;
	}

	/*Radix::System::System:extSystemCode-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:extSystemCode")
	public published  Int getExtSystemCode() {
		return extSystemCode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:extSystemCode")
	public published   void setExtSystemCode(Int val) {
		extSystemCode = val;
	}

	/*Radix::System::System:oraImplStmtCacheSize-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:oraImplStmtCacheSize")
	public published  Int getOraImplStmtCacheSize() {
		return oraImplStmtCacheSize;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:oraImplStmtCacheSize")
	public published   void setOraImplStmtCacheSize(Int val) {
		oraImplStmtCacheSize = val;
	}

	/*Radix::System::System:useOraImplStmtCache-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:useOraImplStmtCache")
	public published  Bool getUseOraImplStmtCache() {
		return useOraImplStmtCache;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:useOraImplStmtCache")
	public published   void setUseOraImplStmtCache(Bool val) {
		useOraImplStmtCache = val;
	}

	/*Radix::System::System:pwdMustDifferFromName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustDifferFromName")
	public published  Bool getPwdMustDifferFromName() {
		return pwdMustDifferFromName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustDifferFromName")
	public published   void setPwdMustDifferFromName(Bool val) {
		pwdMustDifferFromName = val;
	}

	/*Radix::System::System:dualControlForAssignRole-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForAssignRole")
	public published  Bool getDualControlForAssignRole() {
		return dualControlForAssignRole;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForAssignRole")
	public published   void setDualControlForAssignRole(Bool val) {

		if (val!=null && !val.booleanValue()){
		    if (getArte().getRights().getDualControlController().haveNotAcceptedEntities()){
		        
		        final org.radixware.kernel.server.arte.rights.RightsDualControlController.UnacceptedEntities unacceptedEntities =
		                                getArte().getRights().getDualControlController().getNotAcceptedEntities();


		        if (unacceptedEntities.UnacceptedUsers.Count>0){
		            
		            throw new InvalidEasRequestClientFault(String.format("Unable to unset 'Dual control for roles assignment'....", unacceptedEntities.UnacceptedUsers.UserOrGroup + " - " + 
		                   (unacceptedEntities.UnacceptedUsers.Role==null ? 
		                   unacceptedEntities.UnacceptedUsers.RoleId.toString() : 
		                   (unacceptedEntities.UnacceptedUsers.Role.Title==null ? unacceptedEntities.UnacceptedUsers.Role.Name :  unacceptedEntities.UnacceptedUsers.Role.Title)
		                   )));
		        }
		        
		        if (unacceptedEntities.UnacceptedUserGroups.Count>0){
		            throw new InvalidEasRequestClientFault(String.format("Unable to unset 'Dual control for roles assignment'....", unacceptedEntities.UnacceptedUserGroups.UserOrGroup + " - " + 
		                   (unacceptedEntities.UnacceptedUserGroups.Role==null ? 
		                   unacceptedEntities.UnacceptedUserGroups.RoleId.toString() : 
		                   (unacceptedEntities.UnacceptedUserGroups.Role.Title==null ? unacceptedEntities.UnacceptedUserGroups.Role.Name :  unacceptedEntities.UnacceptedUserGroups.Role.Title)
		                   )));
		        }
		        
		        if (unacceptedEntities.UnacceptedUser2UserGroups.Count>0){            
		            throw new InvalidEasRequestClientFault(String.format("Unable to unset 'Dual control for roles assignment'....", 
		                    unacceptedEntities.UnacceptedUser2UserGroups.User + " - " + unacceptedEntities.UnacceptedUser2UserGroups.UserGroup));
		        }
		       
		        throw new InvalidEasRequestClientFault("Unable to unset 'Dual control for roles assignment'....");
		    }
		}
		internal[dualControlForAssignRole] = val;



	}

	/*Radix::System::System:onStartArteUserFunc-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onStartArteUserFunc")
	public published  org.radixware.ads.System.server.UserFunc.OnStartArte getOnStartArteUserFunc() {
		return onStartArteUserFunc;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onStartArteUserFunc")
	public published   void setOnStartArteUserFunc(org.radixware.ads.System.server.UserFunc.OnStartArte val) {
		onStartArteUserFunc = val;
	}

	/*Radix::System::System:dualControlForCfgMgmt-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForCfgMgmt")
	public published  Bool getDualControlForCfgMgmt() {
		return dualControlForCfgMgmt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForCfgMgmt")
	public published   void setDualControlForCfgMgmt(Bool val) {
		dualControlForCfgMgmt = val;
	}

	/*Radix::System::System:canEditDualControlForAssignRole-Dynamic Property*/



	protected Bool canEditDualControlForAssignRole=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:canEditDualControlForAssignRole")
	public published  Bool getCanEditDualControlForAssignRole() {

		boolean serverParameter = org.radixware.kernel.server.SrvRunParams.IsDualControlRoleAssignment;
		boolean databaseParameter = dualControlForAssignRole != null && dualControlForAssignRole.booleanValue();

		if (!serverParameter) {
		    return true;
		} else {
		    if (!databaseParameter)
		        return true;
		}
		return true;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:canEditDualControlForAssignRole")
	public published   void setCanEditDualControlForAssignRole(Bool val) {
		canEditDualControlForAssignRole = val;
	}

	/*Radix::System::System:limitEasSessionsPerUsr-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:limitEasSessionsPerUsr")
	public published  Int getLimitEasSessionsPerUsr() {
		return limitEasSessionsPerUsr;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:limitEasSessionsPerUsr")
	public published   void setLimitEasSessionsPerUsr(Int val) {
		limitEasSessionsPerUsr = val;
	}

	/*Radix::System::System:writeContextToFile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:writeContextToFile")
	public  Bool getWriteContextToFile() {
		return writeContextToFile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:writeContextToFile")
	public   void setWriteContextToFile(Bool val) {
		writeContextToFile = val;
	}

	/*Radix::System::System:pwdMustContainACharsInMixedCase-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainACharsInMixedCase")
	public published  Bool getPwdMustContainACharsInMixedCase() {
		return pwdMustContainACharsInMixedCase;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainACharsInMixedCase")
	public published   void setPwdMustContainACharsInMixedCase(Bool val) {
		pwdMustContainACharsInMixedCase = val;
	}

	/*Radix::System::System:pwdMustContainSChars-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainSChars")
	public published  Bool getPwdMustContainSChars() {
		return pwdMustContainSChars;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainSChars")
	public published   void setPwdMustContainSChars(Bool val) {
		pwdMustContainSChars = val;
	}

	/*Radix::System::System:pwdBlackList-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdBlackList")
	public published  org.radixware.kernel.common.types.ArrStr getPwdBlackList() {
		return pwdBlackList;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdBlackList")
	public published   void setPwdBlackList(org.radixware.kernel.common.types.ArrStr val) {
		pwdBlackList = val;
	}

	/*Radix::System::System:temporaryPwdExpirationPeriod-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:temporaryPwdExpirationPeriod")
	public published  Int getTemporaryPwdExpirationPeriod() {
		return temporaryPwdExpirationPeriod;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:temporaryPwdExpirationPeriod")
	public published   void setTemporaryPwdExpirationPeriod(Int val) {
		temporaryPwdExpirationPeriod = val;
	}

	/*Radix::System::System:aadcMemberId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcMemberId")
	public published  Int getAadcMemberId() {
		return aadcMemberId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcMemberId")
	public published   void setAadcMemberId(Int val) {
		aadcMemberId = val;
	}

	/*Radix::System::System:thisSystem-Dynamic Property*/



	protected static org.radixware.ads.System.server.System thisSystem=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:thisSystem")
	public static published  org.radixware.ads.System.server.System getThisSystem() {

		internal[thisSystem]=loadByPK(1,true);
		internal[thisSystem].keepInCache( 600 ); 
		return internal[thisSystem];   
	}

	/*Radix::System::System:aadcTestedMemberId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcTestedMemberId")
	public published  Int getAadcTestedMemberId() {
		return aadcTestedMemberId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcTestedMemberId")
	public published   void setAadcTestedMemberId(Int val) {
		aadcTestedMemberId = val;
	}

	/*Radix::System::System:aadcCommitedLockExp-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcCommitedLockExp")
	public published  Int getAadcCommitedLockExp() {
		return aadcCommitedLockExp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcCommitedLockExp")
	public published   void setAadcCommitedLockExp(Int val) {
		aadcCommitedLockExp = val;
	}

	/*Radix::System::System:aadcUnlockTables-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcUnlockTables")
	public  org.radixware.ads.System.common.AadcLockedTable.Arr getAadcUnlockTables() {
		return aadcUnlockTables;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcUnlockTables")
	public   void setAadcUnlockTables(org.radixware.ads.System.common.AadcLockedTable.Arr val) {
		aadcUnlockTables = val;
	}

	/*Radix::System::System:instStateGatherPeriodSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateGatherPeriodSec")
	public published  Int getInstStateGatherPeriodSec() {
		return instStateGatherPeriodSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateGatherPeriodSec")
	public published   void setInstStateGatherPeriodSec(Int val) {
		instStateGatherPeriodSec = val;
	}

	/*Radix::System::System:instStateForcedGatherPeriodSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateForcedGatherPeriodSec")
	public published  Int getInstStateForcedGatherPeriodSec() {
		return instStateForcedGatherPeriodSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateForcedGatherPeriodSec")
	public published   void setInstStateForcedGatherPeriodSec(Int val) {
		instStateForcedGatherPeriodSec = val;
	}

	/*Radix::System::System:instStateHistoryStorePeriodDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateHistoryStorePeriodDays")
	public published  Int getInstStateHistoryStorePeriodDays() {
		return instStateHistoryStorePeriodDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateHistoryStorePeriodDays")
	public published   void setInstStateHistoryStorePeriodDays(Int val) {
		instStateHistoryStorePeriodDays = val;
	}

	/*Radix::System::System:maxResultSetCacheSizeKb-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:maxResultSetCacheSizeKb")
	public  Int getMaxResultSetCacheSizeKb() {
		return maxResultSetCacheSizeKb;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:maxResultSetCacheSizeKb")
	public   void setMaxResultSetCacheSizeKb(Int val) {
		maxResultSetCacheSizeKb = val;
	}

	/*Radix::System::System:aadcAffinityTimeoutSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcAffinityTimeoutSec")
	public published  Int getAadcAffinityTimeoutSec() {
		return aadcAffinityTimeoutSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcAffinityTimeoutSec")
	public published   void setAadcAffinityTimeoutSec(Int val) {
		aadcAffinityTimeoutSec = val;
	}

































































































































































































































































































































































































	/*Radix::System::System:Methods-Methods*/

	/*Radix::System::System:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:loadByPK")
	public static published  org.radixware.ads.System.server.System loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),pkValsMap);
		try{
		return (
		org.radixware.ads.System.server.System) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::System::System:importManifest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:importManifest")
	private final  Str importManifest (org.radixware.schemas.services.SystemDocument x_Manifest) {
		// delete all system data
		if (id == null)
		    throw new AppError( "Manifest import is not permitted for new system." );
		if(id == 1)
		    throw new AppError("Manifest import is not permitted for internal system (#1).");

		// id  - delete all services and SCPs for this system

		Types::SqlStatement stmt = Types::SqlStatement.prepare("delete from "+dbName[Radix::System::Service]+" where "+dbName[systemId]+" = ?");
		try {
		    stmt.setInt( 1, id );
		    stmt.executeUpdate();
		} finally {
		    stmt.close(); 
		}
		stmt = Types::SqlStatement.prepare("delete from "+dbName[Radix::System::Scp]+" where "+dbName[systemId]+" = ?");
		try {
		    stmt.setInt( 1, id );
		    stmt.executeUpdate();
		} finally {
		    stmt.close();
		}

		//import new system data
		ServicesXsd:SystemDocument.System x_System = x_Manifest.getSystem();

		String sErrors="";
		ArrStr SAPs[] = new ArrStr[3];
		SAPs[0] = new ArrStr();
		SAPs[1] = new ArrStr();
		SAPs[2] = new ArrStr();

		stmt = Types::SqlStatement.prepare("Select count(*) from "+dbName[Radix::System::System]+" where "+dbName[name]+" = ? and "+dbName[id]+" <> ?");
		try {
		    stmt.setStr(1, x_System.Name);
		    stmt.setInt(2, id);
		    Types::SqlResultSet rs = stmt.executeQuery();
		    try {
		        rs.next();
		        Long result = rs.getInt(1);
		        if(result != 0)
		            sErrors += "System name in the Manifest duplicates the name of the existing system. The current system name has been saved. " + "\n";
		        else
		            name = x_System.Name;
		    } finally {
		        rs.close();
		    }
		} finally {
		    stmt.close();
		}

		notes = x_System.Notes;
		update();


		//create Services with their SAPs
		int ServiceCount = x_System.sizeOfServiceArray();
		while(ServiceCount > 0) {
		    ServicesXsd:SystemDocument.System.Service x_Service = x_System.getServiceArray(--ServiceCount);
		    try{
		        Service newServ = Service.create(id, x_Service.URI, x_Service.WSDLURI);
		        newServ.accessibility = ServiceAccessibility:INTER_SYSTEM; 
		        newServ.title = x_Service.Title;
		        newServ.update();
		    } catch(Exceptions::DatabaseError e) {
		        sErrors += "Service registration error: " + x_Service.URI + ": " + String.valueOf(org.radixware.kernel.common.utils.ExceptionTextFormatter.getExceptionMess(e))+ "\n";
		        continue;
		    }
		    int SapCount = x_Service.sizeOfSAPArray();
		    while(SapCount > 0) {
		        ServicesXsd:SystemDocument.System.Service.SAP x_SAP = x_Service.getSAPArray(--SapCount);
		        try{
		            Sap newSap = Sap.create(id, x_Service.URI, x_SAP.Address, x_SAP.Name);
		            
		            SAPs[0].add(x_Service.URI);
		            SAPs[1].add(x_SAP.Name);
		            SAPs[2].add(newSap.id.toString());
		            
		            newSap.notes = x_SAP.Notes;
		            newSap.accessibility = ServiceAccessibility:INTER_SYSTEM;
		            // newSap. = .getForValue(x_SAP.SecurityProtocol);
		            newSap.update();
		        } catch(Exceptions::DatabaseError e) {
		            sErrors += "SAP registration error: " + x_SAP.Name + ": " + String.valueOf(org.radixware.kernel.common.utils.ExceptionTextFormatter.getExceptionMess(e))+ "\n";
		            continue;
		        }
		    }
		    
		}

		//create SCPs and fill SCP2SAP table
		int ScpCount = x_System.sizeOfSCPArray();
		while(ScpCount > 0) {
		    ServicesXsd:SystemDocument.System.SCP x_SCP = x_System.getSCPArray(--ScpCount);
		    try {
		        Scp newScp = Scp.create(id, x_SCP.Name);
		        newScp.notes = x_SCP.Notes;
		        newScp.update();
		    } catch(Exceptions::DatabaseError e) {
		        sErrors += "SCP registration error: " + x_SCP.Name + ": " + String.valueOf(org.radixware.kernel.common.utils.ExceptionTextFormatter.getExceptionMess(e))+ "\n";
		        continue;
		    }
		    
		    int SCPServiceCount = x_SCP.sizeOfServiceArray();
		    while( SCPServiceCount > 0) {
		        ServicesXsd:SystemDocument.System.SCP.Service x_ServiceUnderSCP = x_SCP.getServiceArray(--SCPServiceCount);
		        int SapCount = x_ServiceUnderSCP.sizeOfSAPArray();
		        while( SapCount > 0) {
		            ServicesXsd:SystemDocument.System.SCP.Service.SAP x_Sap = x_ServiceUnderSCP.getSAPArray(--SapCount);
		            Long sapId = null;
		            for (int i = 0; i < SAPs[0].size(); i++) {
		                if (SAPs[0].get(i) == x_ServiceUnderSCP.URI && SAPs[1].get(i) == x_Sap.Name)
		                    sapId = Long.decode(SAPs[2].get(i));
		            }
		            if(sapId == 0) {
		                sErrors += "Error registering the SCP to SAP association. SAP not found: " + x_ServiceUnderSCP.URI + " - " + x_Sap.Name + "\n";
		            } else {
		                try {
		                    Scp2Sap newScp2Sap = Scp2Sap.create(id, x_SCP.Name, sapId.longValue());
		                    //Radix::Arte::Trace.debug("SCP2SAP inserted: " + x_SCP.Name + " <-> " + sapId.toString(), Radix::Arte::EventSource:App);
		                    newScp2Sap.sapPriority = x_Sap.Priority;
		                    newScp2Sap.connectTimeout = x_Sap.ConnectTimeout;
		                    newScp2Sap.blockingPeriod = x_Sap.BlockingPeriod;
		                    newScp2Sap.update();
		                } catch(Exceptions::DatabaseError e) {
		                    sErrors += "Error registering SCP to SAP association. SAP not found: " + x_SCP.Name + " <-> " + sapId.toString() + "\n";
		                    continue;
		                }
		            }
		        }
		    }
		}

		return sErrors;
	}

	/*Radix::System::System:exportManifest-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:exportManifest")
	private final  org.radixware.schemas.services.SystemDocument exportManifest () {
		ServicesXsd:SystemDocument Manifest = ServicesXsd:SystemDocument.Factory.newInstance();
		ServicesXsd:SystemDocument.System x_System = Manifest.addNewSystem();

		x_System.Notes=notes;
		x_System.Name=name;

		// находим все SCP текущей системы
		ScpsOfSystemCursor c_SCPs = ScpsOfSystemCursor.open(id);
		try {
		    while( c_SCPs.next() ) {
		        ServicesXsd:SystemDocument.System.SCP x_SCP = x_System.addNewSCP();
		        x_SCP.Name=c_SCPs.name;
		        x_SCP.Notes=c_SCPs.notes;

		        // находим все сервисы и их SAPы текущего SCP
		        String prev_ServiceUri="";
		        ServicesXsd:SystemDocument.System.SCP.Service x_ServiceUnderSCP=null;
		        ExtSapsByScpCursor saps = ExtSapsByScpCursor.open(id, c_SCPs.name);
		        try {
		            while( saps.next() ) {
		                // service PK = {systemId, uri}
		                // systemId не менялся
		                if(prev_ServiceUri != saps.serviceUri) {
		                    x_ServiceUnderSCP = x_SCP.addNewService();
		                    prev_ServiceUri = saps.serviceUri;
		                    x_ServiceUnderSCP.URI=saps.serviceUri;
		                }
		                ServicesXsd:SystemDocument.System.SCP.Service.SAP x_SAPUnderSCP = x_ServiceUnderSCP.addNewSAP();
		                x_SAPUnderSCP.Name=saps.sapName;
		                x_SAPUnderSCP.Priority=saps.sapPriority;
		                x_SAPUnderSCP.ConnectTimeout=saps.connectTimeout;
		                x_SAPUnderSCP.BlockingPeriod=saps.blockingPeriod;
		            }//saps
		        } finally {
		            saps.close();
		        }
		    }//c_SCPs
		} finally {
		    c_SCPs.close();
		}
		String prev_ServiceUri="";
		ServicesXsd:SystemDocument.System.Service x_ServiceUnderSystem = null;
		ExtSapsOfSystemCursor c_ServicesAndSaps = ExtSapsOfSystemCursor.open(id);
		while(c_ServicesAndSaps.next()) {
		    if(prev_ServiceUri != c_ServicesAndSaps.ServiceURI) {
		        x_ServiceUnderSystem = x_System.addNewService();
		        prev_ServiceUri = c_ServicesAndSaps.ServiceURI;
		        x_ServiceUnderSystem.URI=c_ServicesAndSaps.ServiceURI;
		    }
		    ServicesXsd:SystemDocument.System.Service.SAP x_SAPUnderSystem = x_ServiceUnderSystem.addNewSAP();
		    x_ServiceUnderSystem.Title=c_ServicesAndSaps.ServiceTitle;
		    x_ServiceUnderSystem.WSDLURI=c_ServicesAndSaps.ServiceWSDL_URI;
		    x_SAPUnderSystem.setType("TCP");//???
		    //:TODO: Security protocol options saving...






		 //   try {
		//        Radix::System::PortSecurityProtocol psp = c_ServicesAndSaps.SapSecurityProtocol; //???
		//        x_SAPUnderSystem.setSecurityProtocol(psp.getValue());
		//    } catch(Radix::Exceptions::NoConstItemWithSuchValueError e)     { 
		    //  x_SAPUnderSystem.setSecurityProtocol(Radix::System::PortSecurityProtocol:None.getName());
		//    }
		    
		    x_SAPUnderSystem.Name=c_ServicesAndSaps.SapName;
		    x_SAPUnderSystem.Notes=c_ServicesAndSaps.SapNotes;
		    x_SAPUnderSystem.Address=c_ServicesAndSaps.SapAddress;
		} //while( c_ServicesAndSaps.next() )
		c_ServicesAndSaps.close();
		return Manifest;
	}

	/*Radix::System::System:onCommand_ExportManifest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onCommand_ExportManifest")
	private final  org.radixware.schemas.services.SystemDocument onCommand_ExportManifest (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return exportManifest();
	}

	/*Radix::System::System:onCommand_ImportManifest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onCommand_ImportManifest")
	private final  void onCommand_ImportManifest (org.radixware.schemas.services.SystemDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		Str errors = importManifest(input);
		try {
		    if (errors != null && errors.isEmpty() == false) {
		        Client.Resources::MessageDialogResource.error(getArte(), "Import Result", "Errors occurred while importing Manifest:" + "\n" + errors);
		    } else {
		        Client.Resources::MessageDialogResource.information(getArte(), "Import Result", "Manifest imported successfully");
		    }
		} catch (Exception e) {
		    throw new AppError("Terminal resource usage error", e);
		}
	}

	/*Radix::System::System:onCommand_ClearSensitiveTracedData-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onCommand_ClearSensitiveTracedData")
	private final  void onCommand_ClearSensitiveTracedData (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		ClearTracedSensitiveDataStmt q;
		do {
		    q = ClearTracedSensitiveDataStmt.execute(10000);
		    Arte::Arte.commit();
		} while (!Arte::Arte.needBreak() && q.updatedCount.longValue() > 0);
	}

	/*Radix::System::System:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:afterUpdate")
	protected published  void afterUpdate () {
		super.afterUpdate();

		if (isArteDbTraceProfileModified) {
		    Arte::Trace.put(eventCode["User '%1' modified ARTE database trace profile. Old value: '%2'. New value: '%3'"],
		            new ArrStr(Arte::Arte.getUserName(), oldArteDbTraceProfile, arteDbTraceProfile));
		}

		if (isArteFileTraceProfileModified) {
		    Arte::Trace.put(eventCode["User '%1' modified ARTE file trace profile. Old value: '%2'. New value: '%3'"],
		            new ArrStr(Arte::Arte.getUserName(), oldArteFileTraceProfile, arteFileTraceProfile));
		}

		if (isArteGuiTraceProfileModified) {
		    Arte::Trace.put(eventCode["User '%1' modified ARTE GUI trace profile. Old value: '%2'. New value: '%3'"],
		            new ArrStr(Arte::Arte.getUserName(), oldArteGuiTraceProfile, arteGuiTraceProfile));
		}

		if (isDefaultAuditSchemeIdModified) {
		    Audit::AuditScheme.updateAllTriggers();
		}
	}

	/*Radix::System::System:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:beforeUpdate")
	protected published  boolean beforeUpdate () {
		isArteDbTraceProfileModified = 
		    PersistentModifiedPropIds.contains(idof[System:arteDbTraceProfile]) &&
		    (arteDbTraceProfile != oldArteDbTraceProfile);

		isArteFileTraceProfileModified = 
		    PersistentModifiedPropIds.contains(idof[System:arteFileTraceProfile]) &&
		    (arteFileTraceProfile != oldArteFileTraceProfile);

		isArteGuiTraceProfileModified = 
		    PersistentModifiedPropIds.contains(idof[System:arteGuiTraceProfile]) &&
		    (arteGuiTraceProfile != oldArteGuiTraceProfile);

		isDefaultAuditSchemeIdModified = 
		    isPersistentPropModified(idof[System:defaultAuditSchemeId]);
		    
		return super.beforeUpdate();
	}

	/*Radix::System::System:onCommand_ShowUsedPorts-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onCommand_ShowUsedPorts")
	private final  org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument onCommand_ShowUsedPorts (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		Acs::CommandsXsd:StrValueDocument str = Acs::CommandsXsd:StrValueDocument.Factory.newInstance();
		str.StrValue = AddressConflict.getSystemPortTable(id);
		return str;
	}

	/*Radix::System::System:checkValues-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:checkValues")
	public  void checkValues () {
		final Map<Str, Str> appParams = Arte::Arte.getAppParams();

		if (appParams == null || appParams.isEmpty()) {
		    return;
		}

		if (id != null && id == 1l) {

		    if (appParams.containsKey(SecurityOption:ASK_USER_PWD_AFTER_INACTIVITY.Value) && askUserPwdAfterInactivity == Boolean.FALSE) {
		        throw new InvalidPropertyValueError(idof[System], idof[System:askUserPwdAfterInactivity], "Application startup parameters require this option to be true");
		    }
		    if (appParams.containsKey(SecurityOption:BLOCK_USER_INVALID_LOGON_CNT.Value)) {
		        final Int valueFromFile = strToInt(appParams.get(SecurityOption:BLOCK_USER_INVALID_LOGON_CNT.Value));
		        if (valueFromFile != null && blockUserInvalidLogonCnt == null || blockUserInvalidLogonCnt.longValue() < valueFromFile.longValue()) {
		            throw new InvalidPropertyValueError(idof[System], idof[System:blockUserInvalidLogonCnt], String.format("Application startup parameters require this option to be not less than %s", valueFromFile));
		        }
		    }
		    if (appParams.containsKey(SecurityOption:BLOCK_USER_INVALID_LOGON_MINS.Value)) {
		        final Int valueFromFile = strToInt(appParams.get(SecurityOption:BLOCK_USER_INVALID_LOGON_MINS.Value));
		        if (valueFromFile != null && blockUserInvalidLogonMins == null || blockUserInvalidLogonMins.longValue() < valueFromFile.longValue()) {
		            throw new InvalidPropertyValueError(idof[System], idof[System:blockUserInvalidLogonMins], String.format("Application startup parameters require this option to be not less than %s", valueFromFile));
		        }
		    }
		    if (appParams.containsKey(SecurityOption:PWD_MIN_LEN.Value)) {
		        final Int valueFromFile = strToInt(appParams.get(SecurityOption:PWD_MIN_LEN.Value));
		        if (valueFromFile != null && pwdMinLen == null || pwdMinLen.longValue() < valueFromFile.longValue()) {
		            throw new InvalidPropertyValueError(idof[System], idof[System:pwdMinLen], String.format("Application startup parameters require this option to be not less than %s", valueFromFile));
		        }
		    }
		    if (appParams.containsKey(SecurityOption:PWD_MUST_CONTAINS_A_CHARS.Value) && pwdMustContainAChars == Boolean.FALSE) {
		        throw new InvalidPropertyValueError(idof[System], idof[System:pwdMustContainAChars], "Application startup parameters require this option to be true");
		    }
		    if (appParams.containsKey(SecurityOption:PWD_MUST_CONTAINS_A_CHARS_IN_MIXED_CASE.Value) && pwdMustContainACharsInMixedCase == Boolean.FALSE) {
		        throw new InvalidPropertyValueError(idof[System], idof[System:pwdMustContainACharsInMixedCase], "Application startup parameters require this option to be true");
		    }
		    if (appParams.containsKey(SecurityOption:PWD_MUST_CONTAIN_N_CHAR.Value) && pwdMustContainNChars == Boolean.FALSE) {
		        throw new InvalidPropertyValueError(idof[System], idof[System:pwdMustContainNChars], "Application startup parameters require this option to be true");
		    }
		    if (appParams.containsKey(SecurityOption:PWD_MUST_CONTAIN_S_CHAR.Value) && pwdMustContainSChars == Boolean.FALSE) {
		        throw new InvalidPropertyValueError(idof[System], idof[System:pwdMustContainSChars], "Application startup parameters require this option to be true");
		    }

		    if (appParams.containsKey(SecurityOption:UNIQ_PWD_SEQ_LEN.Value)) {
		        final Int valueFromFile = strToInt(appParams.get(SecurityOption:UNIQ_PWD_SEQ_LEN.Value));
		        if (valueFromFile != null && uniquePwdSeqLen == null || uniquePwdSeqLen.longValue() < valueFromFile.longValue()) {
		            throw new InvalidPropertyValueError(idof[System], idof[System:uniquePwdSeqLen], String.format("Application startup parameters require this option to be not less than %s", valueFromFile));
		        }
		    }
		    if (appParams.containsKey(SecurityOption:PWD_EXPIRATION_PERIOD.Value)) {
		        final Int valueFromFile = strToInt(appParams.get(SecurityOption:PWD_EXPIRATION_PERIOD.Value));
		        if (valueFromFile != null && pwdExpirationPeriod == null || pwdExpirationPeriod.longValue() > valueFromFile.longValue()) {
		            throw new InvalidPropertyValueError(idof[System], idof[System:pwdExpirationPeriod], String.format("Application startup parameters require this option to be not more than %s", valueFromFile));
		        }
		    }
		    if (appParams.containsKey(SecurityOption:PWD_MUST_DIFFER_FROM_NAME.Value) && pwdMustDifferFromName == Boolean.FALSE) {
		        throw new InvalidPropertyValueError(idof[System], idof[System:pwdMustDifferFromName], "Application startup parameters require this option to be true");
		    }

		    if (easKrbPrincName != null) {
		        try {
		            new javax.security.auth.kerberos.KerberosPrincipal(easKrbPrincName, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		        } catch (IllegalArgumentException exception) {
		            throw new InvalidPropertyValueError(idof[System], idof[System:easKrbPrincName], "Wrong Format of EAS Principal Name");
		        }
		    }
		}
	}

	/*Radix::System::System:strToInt-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:strToInt")
	public  Int strToInt (Str str) {
		if(str == null || str.isEmpty()) {
		    return null;
		}
		try {
		    return new Int(str);
		} catch (Exceptions::Exception ex) {
		    return null;
		}
	}

	/*Radix::System::System:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		if (extSystemCode == null) {
		    extSystemCode = id;
		}
	}

	/*Radix::System::System:getPasswordRequirements-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:getPasswordRequirements")
	public  org.radixware.schemas.eas.PasswordRequirementsDocument getPasswordRequirements () {
		if (this.id!=1){
		    return null;
		}
		Arte::EasXsd:PasswordRequirementsDocument pwdRequirementsDoc = Arte::EasXsd:PasswordRequirementsDocument.Factory.newInstance();
		Arte::EasXsd:PasswordRequirements requirements = pwdRequirementsDoc.addNewPasswordRequirements();
		requirements.setMinLen(pwdMinLen==null ? 0 : pwdMinLen.intValue());
		if (pwdMustContainAChars!=null && pwdMustContainAChars.booleanValue()) {
		    requirements.setAlphabeticCharsRequired(true);
		    if (pwdMustContainACharsInMixedCase!=null && pwdMustContainACharsInMixedCase.booleanValue()){
		        requirements.setAlphabeticCharsInMixedCaseRequired(true);
		    }
		}
		if (pwdMustContainNChars!=null && pwdMustContainNChars.booleanValue()) {
		    requirements.setNumericCharsRequired(true);
		}
		if (pwdMustContainSChars!=null && pwdMustContainSChars.booleanValue()) {
		    requirements.setSpecialCharsRequired(true);
		}
		if (pwdMustDifferFromName!=null && pwdMustDifferFromName.booleanValue()){
		    requirements.setPwdMustDifferFromName(true);
		}
		                        
		if (pwdBlackList!=null && !pwdBlackList.isEmpty()){
		    Arte::EasXsd:PasswordRequirements.BlackList blackList = requirements.addNewBlackList();
		    for (String forbiddenPwd: pwdBlackList){
		        blackList.addItem(forbiddenPwd);
		    }
		}
		return pwdRequirementsDoc;
	}

	/*Radix::System::System:getEffectiveUdsLanguages-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:getEffectiveUdsLanguages")
	public  org.radixware.ads.Common.common.Language.Arr getEffectiveUdsLanguages () {
		if  (userExtDevLanguages != null) {
		    return userExtDevLanguages;
		}

		return new Arr<Common::Language>(Arte::Arte.getInstance().getLanguages());
	}

	/*Radix::System::System:onCommand_ShowAADCLocks-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onCommand_ShowAADCLocks")
	private final published  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_ShowAADCLocks (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return new FormHandlerNextDialogsRequest(null, new AADCLocksListForm(null, null));
	}

	/*Radix::System::System:onCommand_MoveJobsFromOtherAadcMember-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onCommand_MoveJobsFromOtherAadcMember")
	private final  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_MoveJobsFromOtherAadcMember (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		final org.radixware.kernel.server.instance.aadc.AadcManager manager = Arte::Arte.getInstance().getInstance().getAadcManager();
		org.apache.ignite.cluster.ClusterGroup group = manager.getGrid().cluster().forAttribute(org.radixware.kernel.server.instance.aadc.AadcManager.AADC_MEMBER_ATTRIBUTE_NAME, String.valueOf(manager.getOtherMemberId()));
		final StringBuilder sb = new StringBuilder();
		for (org.apache.ignite.cluster.ClusterNode node : group.nodes()) {
		    if (sb.length() > 0) {
		        sb.append(", ");
		    }
		    sb.append(node.attribute(org.radixware.kernel.server.instance.aadc.AadcManager.INSTANCE_ID_ATTRIBUTE_NAME));
		}

		if (sb.length() > 0) {
		    throw new AppError(String.format("The following system instances are running on AADC member %d: %s", manager.getOtherMemberId(), sb.toString()));
		}

		Int movedCount = MoveJobsFromAadcMemberStmt.execute(manager.getOtherMemberId()).movedCount;


		return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Information, Client.Resources::DialogButtonType:Ok, null, null, String.format("%d jobs has been moved", movedCount)), null);

	}








	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd3TMA2OVCZZA5BMFG6J7RO2HMWA){
			org.radixware.schemas.services.SystemDocument result = onCommand_ExportManifest(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmd4WUJPZM72ZHA3DMIP7CPI73WFE){
			org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_MoveJobsFromOtherAadcMember(newPropValsById);
		return result;
	} else if(cmdId == cmdE6I2SUR7ZZD45PRCNSIF77BAQI){
		org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument result = onCommand_ShowUsedPorts(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdTJZ54QM6R5DEFD6ALWIRHUZSBU){
		onCommand_ImportManifest((org.radixware.schemas.services.SystemDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.services.SystemDocument.class),newPropValsById);
		return null;
	} else if(cmdId == cmdXUOGGQH5RNDMNBWVSUB3EP3BAI){
		onCommand_ClearSensitiveTracedData(newPropValsById);
		return null;
	} else if(cmdId == cmdYPFAVUMQHRBSFNCLJ7BPZMBIAU){
		org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_ShowAADCLocks(newPropValsById);
	return result;
} else 
	return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::System::System - Server Meta*/

/*Radix::System::System-Entity Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class System_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),"System",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::System::System:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
							/*Owner Class Name*/
							"System",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),
							/*Property presentations*/

							/*Radix::System::System:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::System:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:name:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGFPIY4PW4OWDRRWUAAIT4AGD7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:blockUserInvalidLogonCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCLAU6NMGVDSNKN7ZPB6SQJE5Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:blockUserInvalidLogonMins:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQTEXFSBLXJES3AOB6SXXKNGQQE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:askUserPwdAfterInactivity:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:arteDbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXLSJR3OUKLNRDAQSABIFNQAAAE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:arteFileTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYF3QYYJ7BFS7AJ6YRMZUFBE4M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:arteGuiTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJDA72X3RRHX5AQ4RSCK3YNA44"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:easSessionActivityMins:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6ABGYQYNVGYRC6NTJ76DIBZ4I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:easSessionInactivityMins:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLPOXOGA2J3NRDJIRACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:arteLanguage:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col546FXWYZJ3NRDJIRACQMTAIZT4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:auditStorePeriod1:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJ6YTFHGWPNBDPLGABQJO5ADDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:auditStorePeriod2:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3J6FBUPGWPNBDPLGABQJO5ADDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:auditStorePeriod3:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3N6FBUPGWPNBDPLGABQJO5ADDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:auditStorePeriod4:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTJMVJWXGWPNBDPLGABQJO5ADDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:auditStorePeriod5:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTNMVJWXGWPNBDPLGABQJO5ADDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:defaultAuditSchemeId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEKZMW4XGWPNBDPLGABQJO5ADDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:eventStoreDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUMLKGQCFD6VDBNKJAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:notificationStoreDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIH2FRGR6EKWDRWGPAAUUN6FMUG"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:defaultAuditScheme:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOT4CGMPQVDNNBAKW4BRLODW4Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::System:profileMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:profilePeriodSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7NWAJN2XXFEUNERD2PHDEGCCYQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:profiledPrefixes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLE6RFD22ZHT7OIHCXEYU4WMVQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:uniquePwdSeqLen:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK5BEU6WO5H4VKRDA46ALDEZ3M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:enableSensitiveTrace:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRLGIOF3DVB2XEFQXLCJLPUYTU"),org.radixware.kernel.common.enums.EEditPossibility.ONLY_EXISTING,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdMinLen:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2J4RCA57UBFPFNVYLNMEXJI5CU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdMustContainAChars:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTY6XN5X66FD2FIC7ZQVWKRV7YQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdMustContainNChars:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJXZPIRBDZG6HCJZ4F2JDH7UFI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:profileLogStoreDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDV6X7RAL5DCBO2HENGADFNFTY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:metricHistoryStoreDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2XLLDK4RVAMFAL7O6XQGRTVNY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdExpirationPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:arteCountry:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWGL2VHS7RNBIRA3NYYV3KGAIPI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:certAttrForUserLogin:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WZAHHSUZVCAVPPUZDJX5WWCA4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:easKrbPrincName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6KLR5HJ3JDI7DTS5S52SRQEFU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:userExtDevLanguages:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:extSystemCode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFRZFQPKGI5EM7DCHTHZ4PSKVCA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:oraImplStmtCacheSize:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:useOraImplStmtCache:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKQKCWI72DVAO7OFVSNBZ7XN2WQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdMustDifferFromName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSLXTNR7MBRF23O655OVBOAL5LA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:dualControlForAssignRole:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ7OWNFGW3RCUVIE7PJUK3OO5GI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:onStartArteUserFunc:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFXLCNMISE5EALECQELQKF5XGMI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:dualControlForCfgMgmt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5QRDG2EULRGM3IGS4RAPZWD2RQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:canEditDualControlForAssignRole:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:limitEasSessionsPerUsr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:writeContextToFile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col42FD7ZUMLNA77LXG67VMOVRC5I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdMustContainACharsInMixedCase:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdMustContainSChars:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSC5YLHYXJD3ZICERRHVMMWRZI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:pwdBlackList:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:temporaryPwdExpirationPeriod:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6EXIXL45PZEBZOF2MKFF7RJWSA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:aadcMemberId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2PT6HH34FHXRHWAOZLL6HQATU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:aadcTestedMemberId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:aadcCommitedLockExp:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGS3V2W2ZNNEXJA3CYQJ5O4AFSA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:aadcUnlockTables:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA7LVBINPNFH2RB6LUIQLSE34DE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:instStateGatherPeriodSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:instStateForcedGatherPeriodSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:instStateHistoryStorePeriodDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:maxResultSetCacheSizeKb:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::System:aadcAffinityTimeoutSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5M4PIAUSFCG5HDFBZHIFMWHAA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ImportManifest-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTJZ54QM6R5DEFD6ALWIRHUZSBU"),"ImportManifest",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ExportManifest-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3TMA2OVCZZA5BMFG6J7RO2HMWA"),"ExportManifest",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ClearTracedSensitiveData-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdXUOGGQH5RNDMNBWVSUB3EP3BAI"),"ClearTracedSensitiveData",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ShowUsedPorts-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdE6I2SUR7ZZD45PRCNSIF77BAQI"),"ShowUsedPorts",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ExportPwdBlackList-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMOQUXWDV55HEJBULXWCZINEBRM"),"ExportPwdBlackList",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),true,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ImportPwdBlackList-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQQKIK4GNFVBKLM7VF7Y3PP5KLU"),"ImportPwdBlackList",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:ShowAadcLocks-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdYPFAVUMQHRBSFNCLJ7BPZMBIAU"),"ShowAadcLocks",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::System:MoveJobsFromOtherAadcMember-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd4WUJPZM72ZHA3DMIP7CPI73WFE"),"MoveJobsFromOtherAadcMember",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::System:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									144,

									/*Radix::System::System:General:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),"{0} - ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::System::System:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::System::System:General:Service-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiN3U3M644MJCXFAVTKQ7G3GSENE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBWN5EPD5Q3NRDB5JAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refRYFENBFUKLNRDJIVACQMTAIZT4"),
													null,
													null),

												/*Radix::System::System:General:JobExecutorPriorityMap-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi53ISKTXR3FBMZCTX3FOO5L7TME"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2ZW5SCE4DVFAXABS7LA5PFIURY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZBR2DHYVUVFCHA4ZNPCHBYHP2U"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null),

												/*Radix::System::System:General:JobExecutorUnitBoost-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3OUH2ZLNDJGJLJSA54W2TG7XOM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPCE24TUPIHNRDJIEACQMTAIZT4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprASXNPKNDZPNRDB65AALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
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
									/*Radix::System::System:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,false,new org.radixware.kernel.common.types.Id[]{},false,null,false,new org.radixware.kernel.common.types.Id[]{},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(32,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::System::System:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::System::System:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::System:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBMVVID4SJVHSZN77POWURCNBUU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:name-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),"name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QCIURENNRFBZMOBOBUJYCAVNI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGFPIY4PW4OWDRRWUAAIT4AGD7E"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2EUYB5G2BFQFPR2JOYCS5KT3A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:blockUserInvalidLogonCnt-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCLAU6NMGVDSNKN7ZPB6SQJE5Y"),"blockUserInvalidLogonCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWAOYX6X4RFF6XEYQ5ML7S4WEVY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("6")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:blockUserInvalidLogonMins-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQTEXFSBLXJES3AOB6SXXKNGQQE"),"blockUserInvalidLogonMins",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls24HMJTW7TRGSDH342B75DXDFQI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:askUserPwdAfterInactivity-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI"),"askUserPwdAfterInactivity",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73T4HKGMGJDSJEABRTO3Z2ZH3M"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:arteDbTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXLSJR3OUKLNRDAQSABIFNQAAAE"),"arteDbTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBATUBSKH4JCSHKW27MQBKMX7IE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:arteFileTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYF3QYYJ7BFS7AJ6YRMZUFBE4M"),"arteFileTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54UK5YQD3BA4JO42VTY6NB7SNM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:arteGuiTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJDA72X3RRHX5AQ4RSCK3YNA44"),"arteGuiTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6F2K2XUC6RAURKEX6LSZUUQJPA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:easSessionActivityMins-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6ABGYQYNVGYRC6NTJ76DIBZ4I"),"easSessionActivityMins",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNOOXLWEZF5AHDHK7KCQK25G3WU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1440")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:easSessionInactivityMins-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLPOXOGA2J3NRDJIRACQMTAIZT4"),"easSessionInactivityMins",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WDOS74FBJCRLPOXMAEHLOUHX4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("15")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:arteLanguage-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col546FXWYZJ3NRDJIRACQMTAIZT4"),"arteLanguage",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH7ZA7XDFWREHHCPOIKLNTLTWYY"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("en")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:auditStorePeriod1-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJ6YTFHGWPNBDPLGABQJO5ADDQ"),"auditStorePeriod1",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIGDACHB74VDKFLE6IOU6WRI7XM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:auditStorePeriod2-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3J6FBUPGWPNBDPLGABQJO5ADDQ"),"auditStorePeriod2",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVVHYBY32BFZBKRTZKFMJI5P34"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("7")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:auditStorePeriod3-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3N6FBUPGWPNBDPLGABQJO5ADDQ"),"auditStorePeriod3",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTPQ4HWFOFFGFF7DABTXOKHNSM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("31")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:auditStorePeriod4-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTJMVJWXGWPNBDPLGABQJO5ADDQ"),"auditStorePeriod4",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5F5BJTJR7JFIVH3DCKB3GS755Y"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("100")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:auditStorePeriod5-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTNMVJWXGWPNBDPLGABQJO5ADDQ"),"auditStorePeriod5",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X3YPJQ2UZF5ZDV2UHCSFZD2WA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("366")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:defaultAuditSchemeId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEKZMW4XGWPNBDPLGABQJO5ADDQ"),"defaultAuditSchemeId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTS7Q6ACKBHFTP43WZL4EJ4WKE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:eventStoreDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUMLKGQCFD6VDBNKJAAUMFADAIA"),"eventStoreDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFG4HE3KQLNC65NOTWPHD73QEG4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:notificationStoreDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIH2FRGR6EKWDRWGPAAUUN6FMUG"),"notificationStoreDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDEOJYAVPBDGNNGKN27HNRUO3I"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:defaultAuditScheme-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOT4CGMPQVDNNBAKW4BRLODW4Y"),"defaultAuditScheme",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5VT5JFWDFFLNDJKC3RWV3V7CE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3MAHBP5YJ3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJBWGL34ATTNBDPK5ABQJO5ADDQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:profileMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),"profileMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VRVICQMXBCRPBJ7KV4WCWLT3Q"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsCP5FOQAA6FBYBEKO5SCIALT5SQ"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:profilePeriodSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7NWAJN2XXFEUNERD2PHDEGCCYQ"),"profilePeriodSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEASZSL2HONH6VBQX44IGC6FE3A"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:profiledPrefixes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLE6RFD22ZHT7OIHCXEYU4WMVQ"),"profiledPrefixes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXW2SS6PTDFDTRARH67Q4ZW4AP4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:uniquePwdSeqLen-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK5BEU6WO5H4VKRDA46ALDEZ3M"),"uniquePwdSeqLen",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJJGIZTWWBBVLEXZ2SFHMRU62A"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("4")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:enableSensitiveTrace-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRLGIOF3DVB2XEFQXLCJLPUYTU"),"enableSensitiveTrace",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6X6BH3GNFBHLPHHZQUH7UHT74"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdMinLen-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2J4RCA57UBFPFNVYLNMEXJI5CU"),"pwdMinLen",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIF5YA2YSJC4RM27QEAWWONNDQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("7")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdMustContainAChars-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTY6XN5X66FD2FIC7ZQVWKRV7YQ"),"pwdMustContainAChars",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKMHYO5UGZB4NP56UOX4SEL3XQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdMustContainNChars-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJXZPIRBDZG6HCJZ4F2JDH7UFI"),"pwdMustContainNChars",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGARWWVW27JF63JIA5BEWQCZGSE"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:profileLogStoreDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDV6X7RAL5DCBO2HENGADFNFTY"),"profileLogStoreDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYJSOLZHLFBH7BDDHW2FBY653Q"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:metricHistoryStoreDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2XLLDK4RVAMFAL7O6XQGRTVNY"),"metricHistoryStoreDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSR3TXHYIVBDWBEIYXNMV2QLYGQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdExpirationPeriod-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),"pwdExpirationPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2L6IOLO2NCOXN5NIWIVQXQUXY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:arteCountry-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWGL2VHS7RNBIRA3NYYV3KGAIPI"),"arteCountry",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRENN54WPHFE63BYZKKGAXXF6Q4"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQK3EKSSOTZDKVFKO3FY5PTAV7A"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("US")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:certAttrForUserLogin-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WZAHHSUZVCAVPPUZDJX5WWCA4"),"certAttrForUserLogin",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQQSEHVQM6FGATHBCAO66ND6TNQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("CN")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:easKrbPrincName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6KLR5HJ3JDI7DTS5S52SRQEFU"),"easKrbPrincName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSPAPCBJMJFJ5FTKOZJO65G7OU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:userExtDevLanguages-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4"),"userExtDevLanguages",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsECVKEGYGUFFWZIA5FCUVX5MGGA"),org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:extSystemCode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFRZFQPKGI5EM7DCHTHZ4PSKVCA"),"extSystemCode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYPDVMQINNGVRAFO43KDZ2TR2Q"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:oraImplStmtCacheSize-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"),"oraImplStmtCacheSize",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BOWQW5VAVCERGJRCXO6K2VE4Y"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("100")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:useOraImplStmtCache-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKQKCWI72DVAO7OFVSNBZ7XN2WQ"),"useOraImplStmtCache",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVI6M5A3URCVVJNDVCOQ34LPCI"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdMustDifferFromName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSLXTNR7MBRF23O655OVBOAL5LA"),"pwdMustDifferFromName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5SHVT4RQNCZDB64TNPGSSFQB4"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:dualControlForAssignRole-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ7OWNFGW3RCUVIE7PJUK3OO5GI"),"dualControlForAssignRole",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQIK4SZMH5ALFCSF5SGU3ZBIQY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:onStartArteUserFunc-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFXLCNMISE5EALECQELQKF5XGMI"),"onStartArteUserFunc",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQBLFROYBZRA2LEN3X2O2C5QWOE"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMZEXQBYWYBEYFOLCIZCSSOBTOM"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:dualControlForCfgMgmt-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5QRDG2EULRGM3IGS4RAPZWD2RQ"),"dualControlForCfgMgmt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQD2G3JOHXBHD7LOIMUS2FAZJ6M"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:canEditDualControlForAssignRole-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),"canEditDualControlForAssignRole",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:limitEasSessionsPerUsr-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"),"limitEasSessionsPerUsr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQI3B2WEYTJG43HUBW6FPF5ET5E"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:writeContextToFile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col42FD7ZUMLNA77LXG67VMOVRC5I"),"writeContextToFile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOIWG2WZFJZHQPCBZFVLUHFFH3U"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdMustContainACharsInMixedCase-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"),"pwdMustContainACharsInMixedCase",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGWU2LKTHJH6XCDZXEJHTWNKCQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdMustContainSChars-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSC5YLHYXJD3ZICERRHVMMWRZI"),"pwdMustContainSChars",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHFHFWIXTNFSDLPV3JAKFQHMZM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:pwdBlackList-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM"),"pwdBlackList",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYLKTKYP7JB7BOA5HQAXTQWAOY"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:temporaryPwdExpirationPeriod-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6EXIXL45PZEBZOF2MKFF7RJWSA"),"temporaryPwdExpirationPeriod",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDAARJHO4ZZFBTNZZAWF2EN2L64"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("72")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:aadcMemberId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2PT6HH34FHXRHWAOZLL6HQATU"),"aadcMemberId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM2XPCRZZINEQDIBUOB2D6SIMPE"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:thisSystem-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPLEYYUDVXXNRDISQAAAAAAAAAA"),"thisSystem",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::System:aadcTestedMemberId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q"),"aadcTestedMemberId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3D3IFOTV25BLPA6U7AMANTBMUQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:aadcCommitedLockExp-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGS3V2W2ZNNEXJA3CYQJ5O4AFSA"),"aadcCommitedLockExp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKFLGQE3PJCQTCPVL7QHQ4A4H4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("600")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:aadcUnlockTables-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA7LVBINPNFH2RB6LUIQLSE34DE"),"aadcUnlockTables",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls342YRUHE2NHA7E7UB4MM53BU4Q"),org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsML5IS6GATJGYZARZVIXHB2C724"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:instStateGatherPeriodSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"),"instStateGatherPeriodSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLW6NYEH5PFFWDEBOIFT2V7SXWE"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:instStateForcedGatherPeriodSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"),"instStateForcedGatherPeriodSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTYYWK7Q45RAPHJX2L6N35IMGNI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:instStateHistoryStorePeriodDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"),"instStateHistoryStorePeriodDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD5WBZFOIQFERPMHIXFVFDRD5BQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:maxResultSetCacheSizeKb-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"),"maxResultSetCacheSizeKb",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4524VRNEZRBHHDI535GADAZYJU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("102400")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::System:aadcAffinityTimeoutSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5M4PIAUSFCG5HDFBZHIFMWHAA"),"aadcAffinityTimeoutSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FFZPYU5RAAZE6LLK5366JINI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::System::System:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFC5VGJO4ZFAVDFBQZCEAZPWLQY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPOQKPFRGYJDEJDUE5QQSWXEK7Y"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4WV2NXQKP5D5LGRCTUIFTP4E6Y"),"importManifest",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("x_Manifest",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr373OQVY5VJABLOLKTLPGAXBBNY"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFBEX4JWHI5F6DCQTDS5CUOEWEE"),"exportManifest",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd3TMA2OVCZZA5BMFG6J7RO2HMWA"),"onCommand_ExportManifest",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr53GEH6CNLRFIBIUZN75P7CIO4A"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdTJZ54QM6R5DEFD6ALWIRHUZSBU"),"onCommand_ImportManifest",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr36HCC33KRVG73ABU674GGODSIY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP7B24P3KDJE7TN2BPVZOZVY4EI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdXUOGGQH5RNDMNBWVSUB3EP3BAI"),"onCommand_ClearSensitiveTracedData",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7G7O6EIODZFADFBBVTBNEO52AI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5W3TRDKMDRCGFJYNWSP72FQKSE"),"afterUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdE6I2SUR7ZZD45PRCNSIF77BAQI"),"onCommand_ShowUsedPorts",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSAJKX6RZEFDRDGIGADPK53HGEI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGWYTYMM6PZAFTBEVSWLIMPB5G4"),"checkValues",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHBMCVTAM7FFXVBKUKOTKKDSE6A"),"strToInt",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("str",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYHPDKIB67VEZDBA6MIMC7CALPY"))
								},org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAO2VWPMEANHAVCKXYGXK5BN4EY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLETTQTJNSVE7FM5NAIHAOLDWNQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UT74VINXVHNBECWEUSVNLQZLU"),"getPasswordRequirements",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCSRVEY53SFFJTC5X5ZNFYWFUDM"),"getEffectiveUdsLanguages",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.ARR_STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdYPFAVUMQHRBSFNCLJ7BPZMBIAU"),"onCommand_ShowAADCLocks",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAJL36MRYCNEZNAWUWGSOCZYRDY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd4WUJPZM72ZHA3DMIP7CPI73WFE"),"onCommand_MoveJobsFromOtherAadcMember",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUYXXOOEMJBHVVHBNWEJDKZ4JZE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::System::System - Desktop Executable*/

/*Radix::System::System-Entity Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System")
public interface System {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.System.explorer.System.System_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.explorer.System.System_DefaultModel )  super.getEntity(i);}
	}























































































































































































































































































































































	/*Radix::System::System:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::System:pwdMinLen:pwdMinLen-Presentation Property*/


	public class PwdMinLen extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PwdMinLen(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMinLen:pwdMinLen")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMinLen:pwdMinLen")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdMinLen getPwdMinLen();
	/*Radix::System::System:userExtDevLanguages:userExtDevLanguages-Presentation Property*/


	public class UserExtDevLanguages extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public UserExtDevLanguages(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Common.common.Language.Arr> getValClass(){
			return org.radixware.ads.Common.common.Language.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:userExtDevLanguages:userExtDevLanguages")
		public  org.radixware.ads.Common.common.Language.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:userExtDevLanguages:userExtDevLanguages")
		public   void setValue(org.radixware.ads.Common.common.Language.Arr val) {
			Value = val;
		}
	}
	public UserExtDevLanguages getUserExtDevLanguages();
	/*Radix::System::System:auditStorePeriod2:auditStorePeriod2-Presentation Property*/


	public class AuditStorePeriod2 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod2(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod2:auditStorePeriod2")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod2:auditStorePeriod2")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod2 getAuditStorePeriod2();
	/*Radix::System::System:auditStorePeriod3:auditStorePeriod3-Presentation Property*/


	public class AuditStorePeriod3 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod3(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod3:auditStorePeriod3")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod3:auditStorePeriod3")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod3 getAuditStorePeriod3();
	/*Radix::System::System:writeContextToFile:writeContextToFile-Presentation Property*/


	public class WriteContextToFile extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public WriteContextToFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:writeContextToFile:writeContextToFile")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:writeContextToFile:writeContextToFile")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public WriteContextToFile getWriteContextToFile();
	/*Radix::System::System:arteLanguage:arteLanguage-Presentation Property*/


	public class ArteLanguage extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteLanguage(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EIsoLanguage> getValClass(){
			return org.radixware.kernel.common.enums.EIsoLanguage.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteLanguage:arteLanguage")
		public  org.radixware.kernel.common.enums.EIsoLanguage getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteLanguage:arteLanguage")
		public   void setValue(org.radixware.kernel.common.enums.EIsoLanguage val) {
			Value = val;
		}
	}
	public ArteLanguage getArteLanguage();
	/*Radix::System::System:dualControlForCfgMgmt:dualControlForCfgMgmt-Presentation Property*/


	public class DualControlForCfgMgmt extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public DualControlForCfgMgmt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForCfgMgmt:dualControlForCfgMgmt")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForCfgMgmt:dualControlForCfgMgmt")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public DualControlForCfgMgmt getDualControlForCfgMgmt();
	/*Radix::System::System:temporaryPwdExpirationPeriod:temporaryPwdExpirationPeriod-Presentation Property*/


	public class TemporaryPwdExpirationPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TemporaryPwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:temporaryPwdExpirationPeriod:temporaryPwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:temporaryPwdExpirationPeriod:temporaryPwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TemporaryPwdExpirationPeriod getTemporaryPwdExpirationPeriod();
	/*Radix::System::System:profilePeriodSec:profilePeriodSec-Presentation Property*/


	public class ProfilePeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProfilePeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profilePeriodSec:profilePeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profilePeriodSec:profilePeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ProfilePeriodSec getProfilePeriodSec();
	/*Radix::System::System:certAttrForUserLogin:certAttrForUserLogin-Presentation Property*/


	public class CertAttrForUserLogin extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public CertAttrForUserLogin(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:certAttrForUserLogin:certAttrForUserLogin")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:certAttrForUserLogin:certAttrForUserLogin")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public CertAttrForUserLogin getCertAttrForUserLogin();
	/*Radix::System::System:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays-Presentation Property*/


	public class InstStateHistoryStorePeriodDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstStateHistoryStorePeriodDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateHistoryStorePeriodDays getInstStateHistoryStorePeriodDays();
	/*Radix::System::System:aadcUnlockTables:aadcUnlockTables-Presentation Property*/


	public class AadcUnlockTables extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AadcUnlockTables(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.AadcLockedTable.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.AadcLockedTable.Arr ? (org.radixware.ads.System.common.AadcLockedTable.Arr)x : new org.radixware.ads.System.common.AadcLockedTable.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.AadcLockedTable.Arr> getValClass(){
			return org.radixware.ads.System.common.AadcLockedTable.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.AadcLockedTable.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.AadcLockedTable.Arr ? (org.radixware.ads.System.common.AadcLockedTable.Arr)x : new org.radixware.ads.System.common.AadcLockedTable.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcUnlockTables:aadcUnlockTables")
		public  org.radixware.ads.System.common.AadcLockedTable.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcUnlockTables:aadcUnlockTables")
		public   void setValue(org.radixware.ads.System.common.AadcLockedTable.Arr val) {
			Value = val;
		}
	}
	public AadcUnlockTables getAadcUnlockTables();
	/*Radix::System::System:uniquePwdSeqLen:uniquePwdSeqLen-Presentation Property*/


	public class UniquePwdSeqLen extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UniquePwdSeqLen(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:uniquePwdSeqLen:uniquePwdSeqLen")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:uniquePwdSeqLen:uniquePwdSeqLen")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UniquePwdSeqLen getUniquePwdSeqLen();
	/*Radix::System::System:blockUserInvalidLogonCnt:blockUserInvalidLogonCnt-Presentation Property*/


	public class BlockUserInvalidLogonCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public BlockUserInvalidLogonCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonCnt:blockUserInvalidLogonCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonCnt:blockUserInvalidLogonCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public BlockUserInvalidLogonCnt getBlockUserInvalidLogonCnt();
	/*Radix::System::System:metricHistoryStoreDays:metricHistoryStoreDays-Presentation Property*/


	public class MetricHistoryStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MetricHistoryStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:metricHistoryStoreDays:metricHistoryStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:metricHistoryStoreDays:metricHistoryStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MetricHistoryStoreDays getMetricHistoryStoreDays();
	/*Radix::System::System:pwdMustContainACharsInMixedCase:pwdMustContainACharsInMixedCase-Presentation Property*/


	public class PwdMustContainACharsInMixedCase extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainACharsInMixedCase(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainACharsInMixedCase:pwdMustContainACharsInMixedCase")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainACharsInMixedCase:pwdMustContainACharsInMixedCase")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainACharsInMixedCase getPwdMustContainACharsInMixedCase();
	/*Radix::System::System:defaultAuditSchemeId:defaultAuditSchemeId-Presentation Property*/


	public class DefaultAuditSchemeId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DefaultAuditSchemeId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditSchemeId:defaultAuditSchemeId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditSchemeId:defaultAuditSchemeId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DefaultAuditSchemeId getDefaultAuditSchemeId();
	/*Radix::System::System:profileMode:profileMode-Presentation Property*/


	public class ProfileMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProfileMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EProfileMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EProfileMode ? (org.radixware.kernel.common.enums.EProfileMode)x : org.radixware.kernel.common.enums.EProfileMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EProfileMode> getValClass(){
			return org.radixware.kernel.common.enums.EProfileMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EProfileMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EProfileMode ? (org.radixware.kernel.common.enums.EProfileMode)x : org.radixware.kernel.common.enums.EProfileMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileMode:profileMode")
		public  org.radixware.kernel.common.enums.EProfileMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileMode:profileMode")
		public   void setValue(org.radixware.kernel.common.enums.EProfileMode val) {
			Value = val;
		}
	}
	public ProfileMode getProfileMode();
	/*Radix::System::System:extSystemCode:extSystemCode-Presentation Property*/


	public class ExtSystemCode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExtSystemCode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:extSystemCode:extSystemCode")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:extSystemCode:extSystemCode")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtSystemCode getExtSystemCode();
	/*Radix::System::System:notes:notes-Presentation Property*/


	public class Notes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Notes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::System::System:aadcCommitedLockExp:aadcCommitedLockExp-Presentation Property*/


	public class AadcCommitedLockExp extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcCommitedLockExp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcCommitedLockExp:aadcCommitedLockExp")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcCommitedLockExp:aadcCommitedLockExp")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcCommitedLockExp getAadcCommitedLockExp();
	/*Radix::System::System:pwdBlackList:pwdBlackList-Presentation Property*/


	public class PwdBlackList extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public PwdBlackList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdBlackList:pwdBlackList")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdBlackList:pwdBlackList")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PwdBlackList getPwdBlackList();
	/*Radix::System::System:profiledPrefixes:profiledPrefixes-Presentation Property*/


	public class ProfiledPrefixes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProfiledPrefixes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profiledPrefixes:profiledPrefixes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profiledPrefixes:profiledPrefixes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProfiledPrefixes getProfiledPrefixes();
	/*Radix::System::System:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec-Presentation Property*/


	public class InstStateForcedGatherPeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstStateForcedGatherPeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateForcedGatherPeriodSec getInstStateForcedGatherPeriodSec();
	/*Radix::System::System:notificationStoreDays:notificationStoreDays-Presentation Property*/


	public class NotificationStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NotificationStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notificationStoreDays:notificationStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notificationStoreDays:notificationStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NotificationStoreDays getNotificationStoreDays();
	/*Radix::System::System:pwdExpirationPeriod:pwdExpirationPeriod-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdExpirationPeriod:pwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdExpirationPeriod:pwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdExpirationPeriod getPwdExpirationPeriod();
	/*Radix::System::System:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb-Presentation Property*/


	public class MaxResultSetCacheSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxResultSetCacheSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxResultSetCacheSizeKb getMaxResultSetCacheSizeKb();
	/*Radix::System::System:dualControlForAssignRole:dualControlForAssignRole-Presentation Property*/


	public class DualControlForAssignRole extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public DualControlForAssignRole(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForAssignRole:dualControlForAssignRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForAssignRole:dualControlForAssignRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public DualControlForAssignRole getDualControlForAssignRole();
	/*Radix::System::System:oraImplStmtCacheSize:oraImplStmtCacheSize-Presentation Property*/


	public class OraImplStmtCacheSize extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OraImplStmtCacheSize(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize();
	/*Radix::System::System:profileLogStoreDays:profileLogStoreDays-Presentation Property*/


	public class ProfileLogStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProfileLogStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileLogStoreDays:profileLogStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileLogStoreDays:profileLogStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ProfileLogStoreDays getProfileLogStoreDays();
	/*Radix::System::System:defaultAuditScheme:defaultAuditScheme-Presentation Property*/


	public class DefaultAuditScheme extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public DefaultAuditScheme(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Audit.explorer.AuditScheme.AuditScheme_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Audit.explorer.AuditScheme.AuditScheme_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Audit.explorer.AuditScheme.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Audit.explorer.AuditScheme.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditScheme:defaultAuditScheme")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditScheme:defaultAuditScheme")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public DefaultAuditScheme getDefaultAuditScheme();
	/*Radix::System::System:useOraImplStmtCache:useOraImplStmtCache-Presentation Property*/


	public class UseOraImplStmtCache extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseOraImplStmtCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:useOraImplStmtCache:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:useOraImplStmtCache:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache();
	/*Radix::System::System:instStateGatherPeriodSec:instStateGatherPeriodSec-Presentation Property*/


	public class InstStateGatherPeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstStateGatherPeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateGatherPeriodSec getInstStateGatherPeriodSec();
	/*Radix::System::System:easSessionInactivityMins:easSessionInactivityMins-Presentation Property*/


	public class EasSessionInactivityMins extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EasSessionInactivityMins(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionInactivityMins:easSessionInactivityMins")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionInactivityMins:easSessionInactivityMins")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EasSessionInactivityMins getEasSessionInactivityMins();
	/*Radix::System::System:enableSensitiveTrace:enableSensitiveTrace-Presentation Property*/


	public class EnableSensitiveTrace extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public EnableSensitiveTrace(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:enableSensitiveTrace:enableSensitiveTrace")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:enableSensitiveTrace:enableSensitiveTrace")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public EnableSensitiveTrace getEnableSensitiveTrace();
	/*Radix::System::System:aadcMemberId:aadcMemberId-Presentation Property*/


	public class AadcMemberId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcMemberId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::System::System:easSessionActivityMins:easSessionActivityMins-Presentation Property*/


	public class EasSessionActivityMins extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EasSessionActivityMins(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionActivityMins:easSessionActivityMins")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionActivityMins:easSessionActivityMins")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EasSessionActivityMins getEasSessionActivityMins();
	/*Radix::System::System:easKrbPrincName:easKrbPrincName-Presentation Property*/


	public class EasKrbPrincName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EasKrbPrincName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easKrbPrincName:easKrbPrincName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easKrbPrincName:easKrbPrincName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EasKrbPrincName getEasKrbPrincName();
	/*Radix::System::System:limitEasSessionsPerUsr:limitEasSessionsPerUsr-Presentation Property*/


	public class LimitEasSessionsPerUsr extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LimitEasSessionsPerUsr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:limitEasSessionsPerUsr:limitEasSessionsPerUsr")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:limitEasSessionsPerUsr:limitEasSessionsPerUsr")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LimitEasSessionsPerUsr getLimitEasSessionsPerUsr();
	/*Radix::System::System:arteGuiTraceProfile:arteGuiTraceProfile-Presentation Property*/


	public class ArteGuiTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteGuiTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteGuiTraceProfile:arteGuiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteGuiTraceProfile:arteGuiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ArteGuiTraceProfile getArteGuiTraceProfile();
	/*Radix::System::System:arteFileTraceProfile:arteFileTraceProfile-Presentation Property*/


	public class ArteFileTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteFileTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteFileTraceProfile:arteFileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteFileTraceProfile:arteFileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ArteFileTraceProfile getArteFileTraceProfile();
	/*Radix::System::System:askUserPwdAfterInactivity:askUserPwdAfterInactivity-Presentation Property*/


	public class AskUserPwdAfterInactivity extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AskUserPwdAfterInactivity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:askUserPwdAfterInactivity:askUserPwdAfterInactivity")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:askUserPwdAfterInactivity:askUserPwdAfterInactivity")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AskUserPwdAfterInactivity getAskUserPwdAfterInactivity();
	/*Radix::System::System:pwdMustContainNChars:pwdMustContainNChars-Presentation Property*/


	public class PwdMustContainNChars extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainNChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainNChars:pwdMustContainNChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainNChars:pwdMustContainNChars")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainNChars getPwdMustContainNChars();
	/*Radix::System::System:pwdMustContainSChars:pwdMustContainSChars-Presentation Property*/


	public class PwdMustContainSChars extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainSChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainSChars:pwdMustContainSChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainSChars:pwdMustContainSChars")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainSChars getPwdMustContainSChars();
	/*Radix::System::System:aadcAffinityTimeoutSec:aadcAffinityTimeoutSec-Presentation Property*/


	public class AadcAffinityTimeoutSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcAffinityTimeoutSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcAffinityTimeoutSec:aadcAffinityTimeoutSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcAffinityTimeoutSec:aadcAffinityTimeoutSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcAffinityTimeoutSec getAadcAffinityTimeoutSec();
	/*Radix::System::System:blockUserInvalidLogonMins:blockUserInvalidLogonMins-Presentation Property*/


	public class BlockUserInvalidLogonMins extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public BlockUserInvalidLogonMins(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonMins:blockUserInvalidLogonMins")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonMins:blockUserInvalidLogonMins")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public BlockUserInvalidLogonMins getBlockUserInvalidLogonMins();
	/*Radix::System::System:auditStorePeriod1:auditStorePeriod1-Presentation Property*/


	public class AuditStorePeriod1 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod1(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod1:auditStorePeriod1")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod1:auditStorePeriod1")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod1 getAuditStorePeriod1();
	/*Radix::System::System:pwdMustDifferFromName:pwdMustDifferFromName-Presentation Property*/


	public class PwdMustDifferFromName extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustDifferFromName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustDifferFromName:pwdMustDifferFromName")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustDifferFromName:pwdMustDifferFromName")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustDifferFromName getPwdMustDifferFromName();
	/*Radix::System::System:name:name-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::System::System:auditStorePeriod4:auditStorePeriod4-Presentation Property*/


	public class AuditStorePeriod4 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod4(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod4:auditStorePeriod4")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod4:auditStorePeriod4")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod4 getAuditStorePeriod4();
	/*Radix::System::System:auditStorePeriod5:auditStorePeriod5-Presentation Property*/


	public class AuditStorePeriod5 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod5(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod5:auditStorePeriod5")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod5:auditStorePeriod5")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod5 getAuditStorePeriod5();
	/*Radix::System::System:pwdMustContainAChars:pwdMustContainAChars-Presentation Property*/


	public class PwdMustContainAChars extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainAChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainAChars:pwdMustContainAChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainAChars:pwdMustContainAChars")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainAChars getPwdMustContainAChars();
	/*Radix::System::System:eventStoreDays:eventStoreDays-Presentation Property*/


	public class EventStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EventStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:eventStoreDays:eventStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:eventStoreDays:eventStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EventStoreDays getEventStoreDays();
	/*Radix::System::System:aadcTestedMemberId:aadcTestedMemberId-Presentation Property*/


	public class AadcTestedMemberId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcTestedMemberId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcTestedMemberId:aadcTestedMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcTestedMemberId:aadcTestedMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcTestedMemberId getAadcTestedMemberId();
	/*Radix::System::System:arteCountry:arteCountry-Presentation Property*/


	public class ArteCountry extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteCountry(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EIsoCountry dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoCountry ? (org.radixware.kernel.common.enums.EIsoCountry)x : org.radixware.kernel.common.enums.EIsoCountry.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EIsoCountry> getValClass(){
			return org.radixware.kernel.common.enums.EIsoCountry.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EIsoCountry dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoCountry ? (org.radixware.kernel.common.enums.EIsoCountry)x : org.radixware.kernel.common.enums.EIsoCountry.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteCountry:arteCountry")
		public  org.radixware.kernel.common.enums.EIsoCountry getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteCountry:arteCountry")
		public   void setValue(org.radixware.kernel.common.enums.EIsoCountry val) {
			Value = val;
		}
	}
	public ArteCountry getArteCountry();
	/*Radix::System::System:arteDbTraceProfile:arteDbTraceProfile-Presentation Property*/


	public class ArteDbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteDbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteDbTraceProfile:arteDbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteDbTraceProfile:arteDbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ArteDbTraceProfile getArteDbTraceProfile();
	/*Radix::System::System:canEditDualControlForAssignRole:canEditDualControlForAssignRole-Presentation Property*/


	public class CanEditDualControlForAssignRole extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanEditDualControlForAssignRole(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:canEditDualControlForAssignRole:canEditDualControlForAssignRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:canEditDualControlForAssignRole:canEditDualControlForAssignRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanEditDualControlForAssignRole getCanEditDualControlForAssignRole();
	/*Radix::System::System:onStartArteUserFunc:onStartArteUserFunc-Presentation Property*/


	public class OnStartArteUserFunc extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public OnStartArteUserFunc(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.UserFunc.explorer.UserFunc.UserFunc_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onStartArteUserFunc:onStartArteUserFunc")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:onStartArteUserFunc:onStartArteUserFunc")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public OnStartArteUserFunc getOnStartArteUserFunc();
	public static class ExportManifest extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportManifest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.services.SystemDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.services.SystemDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.services.SystemDocument.class);
		}

	}

	public static class MoveJobsFromOtherAadcMember extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveJobsFromOtherAadcMember(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class ShowUsedPorts extends org.radixware.kernel.common.client.models.items.Command{
		protected ShowUsedPorts(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class);
		}

	}

	public static class ExportPwdBlackList extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportPwdBlackList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ImportPwdBlackList extends org.radixware.kernel.common.client.models.items.Command{
		protected ImportPwdBlackList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ImportManifest extends org.radixware.kernel.common.client.models.items.Command{
		protected ImportManifest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.services.SystemDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class ClearTracedSensitiveData extends org.radixware.kernel.common.client.models.items.Command{
		protected ClearTracedSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class ShowAadcLocks extends org.radixware.kernel.common.client.models.items.Command{
		protected ShowAadcLocks(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::System::System - Desktop Meta*/

/*Radix::System::System-Entity Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class System_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::System:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
			"Radix::System::System",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEVI4WYHRWU3GKERLIZ5THVPBBPDUIMWZ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6MYEBR27BCYLKIESD3AI6T2QM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),0,

			/*Radix::System::System:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::System:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBMVVID4SJVHSZN77POWURCNBUU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QCIURENNRFBZMOBOBUJYCAVNI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGFPIY4PW4OWDRRWUAAIT4AGD7E"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2EUYB5G2BFQFPR2JOYCS5KT3A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:blockUserInvalidLogonCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCLAU6NMGVDSNKN7ZPB6SQJE5Y"),
						"blockUserInvalidLogonCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWAOYX6X4RFF6XEYQ5ML7S4WEVY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("6"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:blockUserInvalidLogonCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:blockUserInvalidLogonMins:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQTEXFSBLXJES3AOB6SXXKNGQQE"),
						"blockUserInvalidLogonMins",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls24HMJTW7TRGSDH342B75DXDFQI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:blockUserInvalidLogonMins:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:askUserPwdAfterInactivity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI"),
						"askUserPwdAfterInactivity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73T4HKGMGJDSJEABRTO3Z2ZH3M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:askUserPwdAfterInactivity:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteDbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXLSJR3OUKLNRDAQSABIFNQAAAE"),
						"arteDbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBATUBSKH4JCSHKW27MQBKMX7IE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::System::System:arteDbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteFileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYF3QYYJ7BFS7AJ6YRMZUFBE4M"),
						"arteFileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54UK5YQD3BA4JO42VTY6NB7SNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::System::System:arteFileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteGuiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJDA72X3RRHX5AQ4RSCK3YNA44"),
						"arteGuiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6F2K2XUC6RAURKEX6LSZUUQJPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::System::System:arteGuiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:easSessionActivityMins:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6ABGYQYNVGYRC6NTJ76DIBZ4I"),
						"easSessionActivityMins",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNOOXLWEZF5AHDHK7KCQK25G3WU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1440"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:easSessionActivityMins:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:easSessionInactivityMins:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLPOXOGA2J3NRDJIRACQMTAIZT4"),
						"easSessionInactivityMins",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WDOS74FBJCRLPOXMAEHLOUHX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("15"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:easSessionInactivityMins:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteLanguage:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col546FXWYZJ3NRDJIRACQMTAIZT4"),
						"arteLanguage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH7ZA7XDFWREHHCPOIKLNTLTWYY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("en"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:arteLanguage:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod1:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJ6YTFHGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod1",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIGDACHB74VDKFLE6IOU6WRI7XM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::System::System:auditStorePeriod1:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod2:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3J6FBUPGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod2",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVVHYBY32BFZBKRTZKFMJI5P34"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("7"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod2:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod3:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3N6FBUPGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod3",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTPQ4HWFOFFGFF7DABTXOKHNSM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("31"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod3:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod4:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTJMVJWXGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod4",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5F5BJTJR7JFIVH3DCKB3GS755Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("100"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod4:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod5:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTNMVJWXGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod5",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X3YPJQ2UZF5ZDV2UHCSFZD2WA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("366"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod5:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:defaultAuditSchemeId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEKZMW4XGWPNBDPLGABQJO5ADDQ"),
						"defaultAuditSchemeId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTS7Q6ACKBHFTP43WZL4EJ4WKE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:defaultAuditSchemeId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:eventStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUMLKGQCFD6VDBNKJAAUMFADAIA"),
						"eventStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFG4HE3KQLNC65NOTWPHD73QEG4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:eventStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:notificationStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIH2FRGR6EKWDRWGPAAUUN6FMUG"),
						"notificationStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDEOJYAVPBDGNNGKN27HNRUO3I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:notificationStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:defaultAuditScheme:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOT4CGMPQVDNNBAKW4BRLODW4Y"),
						"defaultAuditScheme",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5VT5JFWDFFLNDJKC3RWV3V7CE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJBWGL34ATTNBDPK5ABQJO5ADDQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::System:profileMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),
						"profileMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VRVICQMXBCRPBJ7KV4WCWLT3Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsCP5FOQAA6FBYBEKO5SCIALT5SQ"),
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

						/*Radix::System::System:profileMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsCP5FOQAA6FBYBEKO5SCIALT5SQ"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:profilePeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7NWAJN2XXFEUNERD2PHDEGCCYQ"),
						"profilePeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEASZSL2HONH6VBQX44IGC6FE3A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNG5EDRADNFLFMHRYHRYNIVZRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:profilePeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:profiledPrefixes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLE6RFD22ZHT7OIHCXEYU4WMVQ"),
						"profiledPrefixes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXW2SS6PTDFDTRARH67Q4ZW4AP4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeVFOABNMNURCWZLCZXPBULEZGAI"),
						true,

						/*Radix::System::System:profiledPrefixes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:uniquePwdSeqLen:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK5BEU6WO5H4VKRDA46ALDEZ3M"),
						"uniquePwdSeqLen",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJJGIZTWWBBVLEXZ2SFHMRU62A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("4"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:uniquePwdSeqLen:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDHWA65432RB4BCZEECT6PQWKO4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:enableSensitiveTrace:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRLGIOF3DVB2XEFQXLCJLPUYTU"),
						"enableSensitiveTrace",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6X6BH3GNFBHLPHHZQUH7UHT74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_EXISTING,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:enableSensitiveTrace:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMinLen:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2J4RCA57UBFPFNVYLNMEXJI5CU"),
						"pwdMinLen",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIF5YA2YSJC4RM27QEAWWONNDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("7"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:pwdMinLen:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4KLS7EXAAVC6JIXJWPLZHKHGUE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainAChars:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTY6XN5X66FD2FIC7ZQVWKRV7YQ"),
						"pwdMustContainAChars",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKMHYO5UGZB4NP56UOX4SEL3XQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainAChars:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainNChars:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJXZPIRBDZG6HCJZ4F2JDH7UFI"),
						"pwdMustContainNChars",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGARWWVW27JF63JIA5BEWQCZGSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainNChars:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:profileLogStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDV6X7RAL5DCBO2HENGADFNFTY"),
						"profileLogStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYJSOLZHLFBH7BDDHW2FBY653Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::System::System:profileLogStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:metricHistoryStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2XLLDK4RVAMFAL7O6XQGRTVNY"),
						"metricHistoryStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSR3TXHYIVBDWBEIYXNMV2QLYGQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:metricHistoryStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),
						"pwdExpirationPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2L6IOLO2NCOXN5NIWIVQXQUXY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2PGFYGYXZALZP6L6J4BBFRXPY"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteCountry:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWGL2VHS7RNBIRA3NYYV3KGAIPI"),
						"arteCountry",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRENN54WPHFE63BYZKKGAXXF6Q4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQK3EKSSOTZDKVFKO3FY5PTAV7A"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("US"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:arteCountry:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQK3EKSSOTZDKVFKO3FY5PTAV7A"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:certAttrForUserLogin:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WZAHHSUZVCAVPPUZDJX5WWCA4"),
						"certAttrForUserLogin",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQQSEHVQM6FGATHBCAO66ND6TNQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("CN"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:certAttrForUserLogin:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VZI6BUPLNG5LMIXHOSQEND7UY"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:easKrbPrincName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6KLR5HJ3JDI7DTS5S52SRQEFU"),
						"easKrbPrincName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSPAPCBJMJFJ5FTKOZJO65G7OU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:easKrbPrincName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:userExtDevLanguages:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4"),
						"userExtDevLanguages",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsECVKEGYGUFFWZIA5FCUVX5MGGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),
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

						/*Radix::System::System:userExtDevLanguages:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRUSBOMVINE7LMQ2SEQB5V33LM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCZ4MLVRTXJGQZH6VWZ2AMWMOQE"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:extSystemCode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFRZFQPKGI5EM7DCHTHZ4PSKVCA"),
						"extSystemCode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYPDVMQINNGVRAFO43KDZ2TR2Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRRERXT5RBGXZAQV6WQ37VNZWI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:extSystemCode:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:oraImplStmtCacheSize:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"),
						"oraImplStmtCacheSize",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BOWQW5VAVCERGJRCXO6K2VE4Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("100"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:oraImplStmtCacheSize:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:useOraImplStmtCache:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKQKCWI72DVAO7OFVSNBZ7XN2WQ"),
						"useOraImplStmtCache",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVI6M5A3URCVVJNDVCOQ34LPCI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:useOraImplStmtCache:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustDifferFromName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSLXTNR7MBRF23O655OVBOAL5LA"),
						"pwdMustDifferFromName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5SHVT4RQNCZDB64TNPGSSFQB4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustDifferFromName:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:dualControlForAssignRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ7OWNFGW3RCUVIE7PJUK3OO5GI"),
						"dualControlForAssignRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQIK4SZMH5ALFCSF5SGU3ZBIQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:dualControlForAssignRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:onStartArteUserFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFXLCNMISE5EALECQELQKF5XGMI"),
						"onStartArteUserFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMZEXQBYWYBEYFOLCIZCSSOBTOM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr7IKMB24XZHOBDCMTAALOMT5GDM")},
						null,
						0L,
						0L,false,false),

					/*Radix::System::System:dualControlForCfgMgmt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5QRDG2EULRGM3IGS4RAPZWD2RQ"),
						"dualControlForCfgMgmt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQD2G3JOHXBHD7LOIMUS2FAZJ6M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:dualControlForCfgMgmt:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:canEditDualControlForAssignRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),
						"canEditDualControlForAssignRole",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:canEditDualControlForAssignRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:limitEasSessionsPerUsr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"),
						"limitEasSessionsPerUsr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQI3B2WEYTJG43HUBW6FPF5ET5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:limitEasSessionsPerUsr:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ABEVLLODBGIRAKIL7IJWE6PPI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:writeContextToFile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col42FD7ZUMLNA77LXG67VMOVRC5I"),
						"writeContextToFile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOIWG2WZFJZHQPCBZFVLUHFFH3U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:writeContextToFile:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainACharsInMixedCase:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"),
						"pwdMustContainACharsInMixedCase",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGWU2LKTHJH6XCDZXEJHTWNKCQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainACharsInMixedCase:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainSChars:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSC5YLHYXJD3ZICERRHVMMWRZI"),
						"pwdMustContainSChars",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHFHFWIXTNFSDLPV3JAKFQHMZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainSChars:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdBlackList:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM"),
						"pwdBlackList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYLKTKYP7JB7BOA5HQAXTQWAOY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::System::System:pwdBlackList:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,false),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDFA3GVWRNBA3P3JJLDYR3A5KA"),
						null,
						null,
						false,1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:temporaryPwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6EXIXL45PZEBZOF2MKFF7RJWSA"),
						"temporaryPwdExpirationPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDAARJHO4ZZFBTNZZAWF2EN2L64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("72"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:temporaryPwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAU2ZG3FLJA4NEQ7ETSXDZRJN4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2PT6HH34FHXRHWAOZLL6HQATU"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM2XPCRZZINEQDIBUOB2D6SIMPE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcTestedMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q"),
						"aadcTestedMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3D3IFOTV25BLPA6U7AMANTBMUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:aadcTestedMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcCommitedLockExp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGS3V2W2ZNNEXJA3CYQJ5O4AFSA"),
						"aadcCommitedLockExp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKFLGQE3PJCQTCPVL7QHQ4A4H4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("600"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:aadcCommitedLockExp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(60L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcUnlockTables:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA7LVBINPNFH2RB6LUIQLSE34DE"),
						"aadcUnlockTables",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls342YRUHE2NHA7E7UB4MM53BU4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsML5IS6GATJGYZARZVIXHB2C724"),
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

						/*Radix::System::System:aadcUnlockTables:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsML5IS6GATJGYZARZVIXHB2C724"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3C54IPQI5BEPBNBZ2SGAST36Y"),
						null,
						null,
						false,1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:instStateGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"),
						"instStateGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLW6NYEH5PFFWDEBOIFT2V7SXWE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:instStateGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,120L,(byte)-1,null,5L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCDQGQBAWREB7DDJBBYE53D4RI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:instStateForcedGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"),
						"instStateForcedGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTYYWK7Q45RAPHJX2L6N35IMGNI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:instStateForcedGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,300L,(byte)-1,null,10L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26INKFHONJBX5JY6UAZQ6V2J5Y"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:instStateHistoryStorePeriodDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"),
						"instStateHistoryStorePeriodDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD5WBZFOIQFERPMHIXFVFDRD5BQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:instStateHistoryStorePeriodDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,90L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:maxResultSetCacheSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"),
						"maxResultSetCacheSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4524VRNEZRBHHDI535GADAZYJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("102400"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:maxResultSetCacheSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcAffinityTimeoutSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5M4PIAUSFCG5HDFBZHIFMWHAA"),
						"aadcAffinityTimeoutSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FFZPYU5RAAZE6LLK5366JINI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:aadcAffinityTimeoutSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ImportManifest-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTJZ54QM6R5DEFD6ALWIRHUZSBU"),
						"ImportManifest",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZA6SR5BPZHMDOCSPCT6KKQRY4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEVEBHW62BFDDBLQVX2YPL7ZERM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ExportManifest-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd3TMA2OVCZZA5BMFG6J7RO2HMWA"),
						"ExportManifest",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4HPW3JIEBBAPMLTFVWFLSPQ2U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYGRKUM6D5NFB7HJSZFG3KGBC6M"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ClearTracedSensitiveData-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdXUOGGQH5RNDMNBWVSUB3EP3BAI"),
						"ClearTracedSensitiveData",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZFYKIYRC3ZGXJI5PYJOUGP7S6A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgBETHPABPOJEGJA4OH34ARRFYPI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ShowUsedPorts-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdE6I2SUR7ZZD45PRCNSIF77BAQI"),
						"ShowUsedPorts",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NREAYARLVH6NL45SMR3W2QBRI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgAR26WN32ABFQVNKGDBYBZGE6E4"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ExportPwdBlackList-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMOQUXWDV55HEJBULXWCZINEBRM"),
						"ExportPwdBlackList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT5WZXIFJ7FABJPV6LXA36BGXSM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgHFEM4G4OTFCMVCRVIUE5KEUSX4"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						true,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ImportPwdBlackList-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQQKIK4GNFVBKLM7VF7Y3PP5KLU"),
						"ImportPwdBlackList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6C7JDDBY3FDNXGSYAAMRHHELJQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3RSWZFQZSBBSXJHQRLSFWZ3BJE"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ShowAadcLocks-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdYPFAVUMQHRBSFNCLJ7BPZMBIAU"),
						"ShowAadcLocks",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCX3AFOUTFHYROFXYVORR4ZIGI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgD2LY2OKLJZETTLJ734ZBIGGFTU"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:MoveJobsFromOtherAadcMember-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd4WUJPZM72ZHA3DMIP7CPI73WFE"),
						"MoveJobsFromOtherAadcMember",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWRBYFLI3FGCHPGBTD7HVFQW3M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKJLXMBKFKRDNRKVPMEZZL7EAPI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3MAHBP5YJ3NRDAQSABIFNQAAAE"),"System=>Scheme (defaultAuditSchemeId=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEKZMW4XGWPNBDPLGABQJO5ADDQ")},new String[]{"defaultAuditSchemeId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAB22MAMBTTNBDPK5ABQJO5ADDQ")},new String[]{"guid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY")},
			false,true,false);
}

/* Radix::System::System - Web Executable*/

/*Radix::System::System-Entity Class*/

package org.radixware.ads.System.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System")
public interface System {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.System.web.System.System_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.web.System.System_DefaultModel )  super.getEntity(i);}
	}


















































































































































































































































































































































	/*Radix::System::System:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::System:pwdMinLen:pwdMinLen-Presentation Property*/


	public class PwdMinLen extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PwdMinLen(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMinLen:pwdMinLen")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMinLen:pwdMinLen")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdMinLen getPwdMinLen();
	/*Radix::System::System:userExtDevLanguages:userExtDevLanguages-Presentation Property*/


	public class UserExtDevLanguages extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public UserExtDevLanguages(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Common.common.Language.Arr> getValClass(){
			return org.radixware.ads.Common.common.Language.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:userExtDevLanguages:userExtDevLanguages")
		public  org.radixware.ads.Common.common.Language.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:userExtDevLanguages:userExtDevLanguages")
		public   void setValue(org.radixware.ads.Common.common.Language.Arr val) {
			Value = val;
		}
	}
	public UserExtDevLanguages getUserExtDevLanguages();
	/*Radix::System::System:auditStorePeriod2:auditStorePeriod2-Presentation Property*/


	public class AuditStorePeriod2 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod2(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod2:auditStorePeriod2")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod2:auditStorePeriod2")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod2 getAuditStorePeriod2();
	/*Radix::System::System:auditStorePeriod3:auditStorePeriod3-Presentation Property*/


	public class AuditStorePeriod3 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod3(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod3:auditStorePeriod3")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod3:auditStorePeriod3")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod3 getAuditStorePeriod3();
	/*Radix::System::System:writeContextToFile:writeContextToFile-Presentation Property*/


	public class WriteContextToFile extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public WriteContextToFile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:writeContextToFile:writeContextToFile")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:writeContextToFile:writeContextToFile")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public WriteContextToFile getWriteContextToFile();
	/*Radix::System::System:arteLanguage:arteLanguage-Presentation Property*/


	public class ArteLanguage extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteLanguage(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EIsoLanguage> getValClass(){
			return org.radixware.kernel.common.enums.EIsoLanguage.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EIsoLanguage dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoLanguage ? (org.radixware.kernel.common.enums.EIsoLanguage)x : org.radixware.kernel.common.enums.EIsoLanguage.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteLanguage:arteLanguage")
		public  org.radixware.kernel.common.enums.EIsoLanguage getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteLanguage:arteLanguage")
		public   void setValue(org.radixware.kernel.common.enums.EIsoLanguage val) {
			Value = val;
		}
	}
	public ArteLanguage getArteLanguage();
	/*Radix::System::System:dualControlForCfgMgmt:dualControlForCfgMgmt-Presentation Property*/


	public class DualControlForCfgMgmt extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public DualControlForCfgMgmt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForCfgMgmt:dualControlForCfgMgmt")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForCfgMgmt:dualControlForCfgMgmt")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public DualControlForCfgMgmt getDualControlForCfgMgmt();
	/*Radix::System::System:temporaryPwdExpirationPeriod:temporaryPwdExpirationPeriod-Presentation Property*/


	public class TemporaryPwdExpirationPeriod extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TemporaryPwdExpirationPeriod(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:temporaryPwdExpirationPeriod:temporaryPwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:temporaryPwdExpirationPeriod:temporaryPwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TemporaryPwdExpirationPeriod getTemporaryPwdExpirationPeriod();
	/*Radix::System::System:profilePeriodSec:profilePeriodSec-Presentation Property*/


	public class ProfilePeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProfilePeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profilePeriodSec:profilePeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profilePeriodSec:profilePeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ProfilePeriodSec getProfilePeriodSec();
	/*Radix::System::System:certAttrForUserLogin:certAttrForUserLogin-Presentation Property*/


	public class CertAttrForUserLogin extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public CertAttrForUserLogin(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:certAttrForUserLogin:certAttrForUserLogin")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:certAttrForUserLogin:certAttrForUserLogin")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public CertAttrForUserLogin getCertAttrForUserLogin();
	/*Radix::System::System:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays-Presentation Property*/


	public class InstStateHistoryStorePeriodDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstStateHistoryStorePeriodDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateHistoryStorePeriodDays getInstStateHistoryStorePeriodDays();
	/*Radix::System::System:aadcUnlockTables:aadcUnlockTables-Presentation Property*/


	public class AadcUnlockTables extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public AadcUnlockTables(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.AadcLockedTable.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.AadcLockedTable.Arr ? (org.radixware.ads.System.common.AadcLockedTable.Arr)x : new org.radixware.ads.System.common.AadcLockedTable.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.AadcLockedTable.Arr> getValClass(){
			return org.radixware.ads.System.common.AadcLockedTable.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.AadcLockedTable.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.AadcLockedTable.Arr ? (org.radixware.ads.System.common.AadcLockedTable.Arr)x : new org.radixware.ads.System.common.AadcLockedTable.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcUnlockTables:aadcUnlockTables")
		public  org.radixware.ads.System.common.AadcLockedTable.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcUnlockTables:aadcUnlockTables")
		public   void setValue(org.radixware.ads.System.common.AadcLockedTable.Arr val) {
			Value = val;
		}
	}
	public AadcUnlockTables getAadcUnlockTables();
	/*Radix::System::System:uniquePwdSeqLen:uniquePwdSeqLen-Presentation Property*/


	public class UniquePwdSeqLen extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UniquePwdSeqLen(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:uniquePwdSeqLen:uniquePwdSeqLen")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:uniquePwdSeqLen:uniquePwdSeqLen")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UniquePwdSeqLen getUniquePwdSeqLen();
	/*Radix::System::System:blockUserInvalidLogonCnt:blockUserInvalidLogonCnt-Presentation Property*/


	public class BlockUserInvalidLogonCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public BlockUserInvalidLogonCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonCnt:blockUserInvalidLogonCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonCnt:blockUserInvalidLogonCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public BlockUserInvalidLogonCnt getBlockUserInvalidLogonCnt();
	/*Radix::System::System:metricHistoryStoreDays:metricHistoryStoreDays-Presentation Property*/


	public class MetricHistoryStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MetricHistoryStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:metricHistoryStoreDays:metricHistoryStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:metricHistoryStoreDays:metricHistoryStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MetricHistoryStoreDays getMetricHistoryStoreDays();
	/*Radix::System::System:pwdMustContainACharsInMixedCase:pwdMustContainACharsInMixedCase-Presentation Property*/


	public class PwdMustContainACharsInMixedCase extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainACharsInMixedCase(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainACharsInMixedCase:pwdMustContainACharsInMixedCase")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainACharsInMixedCase:pwdMustContainACharsInMixedCase")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainACharsInMixedCase getPwdMustContainACharsInMixedCase();
	/*Radix::System::System:defaultAuditSchemeId:defaultAuditSchemeId-Presentation Property*/


	public class DefaultAuditSchemeId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public DefaultAuditSchemeId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditSchemeId:defaultAuditSchemeId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditSchemeId:defaultAuditSchemeId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DefaultAuditSchemeId getDefaultAuditSchemeId();
	/*Radix::System::System:profileMode:profileMode-Presentation Property*/


	public class ProfileMode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProfileMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EProfileMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EProfileMode ? (org.radixware.kernel.common.enums.EProfileMode)x : org.radixware.kernel.common.enums.EProfileMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EProfileMode> getValClass(){
			return org.radixware.kernel.common.enums.EProfileMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EProfileMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EProfileMode ? (org.radixware.kernel.common.enums.EProfileMode)x : org.radixware.kernel.common.enums.EProfileMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileMode:profileMode")
		public  org.radixware.kernel.common.enums.EProfileMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileMode:profileMode")
		public   void setValue(org.radixware.kernel.common.enums.EProfileMode val) {
			Value = val;
		}
	}
	public ProfileMode getProfileMode();
	/*Radix::System::System:extSystemCode:extSystemCode-Presentation Property*/


	public class ExtSystemCode extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ExtSystemCode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:extSystemCode:extSystemCode")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:extSystemCode:extSystemCode")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ExtSystemCode getExtSystemCode();
	/*Radix::System::System:notes:notes-Presentation Property*/


	public class Notes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Notes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::System::System:aadcCommitedLockExp:aadcCommitedLockExp-Presentation Property*/


	public class AadcCommitedLockExp extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcCommitedLockExp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcCommitedLockExp:aadcCommitedLockExp")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcCommitedLockExp:aadcCommitedLockExp")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcCommitedLockExp getAadcCommitedLockExp();
	/*Radix::System::System:pwdBlackList:pwdBlackList-Presentation Property*/


	public class PwdBlackList extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public PwdBlackList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdBlackList:pwdBlackList")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdBlackList:pwdBlackList")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PwdBlackList getPwdBlackList();
	/*Radix::System::System:profiledPrefixes:profiledPrefixes-Presentation Property*/


	public class ProfiledPrefixes extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProfiledPrefixes(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profiledPrefixes:profiledPrefixes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profiledPrefixes:profiledPrefixes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProfiledPrefixes getProfiledPrefixes();
	/*Radix::System::System:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec-Presentation Property*/


	public class InstStateForcedGatherPeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstStateForcedGatherPeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateForcedGatherPeriodSec getInstStateForcedGatherPeriodSec();
	/*Radix::System::System:notificationStoreDays:notificationStoreDays-Presentation Property*/


	public class NotificationStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NotificationStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notificationStoreDays:notificationStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:notificationStoreDays:notificationStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NotificationStoreDays getNotificationStoreDays();
	/*Radix::System::System:pwdExpirationPeriod:pwdExpirationPeriod-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdExpirationPeriod:pwdExpirationPeriod")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdExpirationPeriod:pwdExpirationPeriod")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PwdExpirationPeriod getPwdExpirationPeriod();
	/*Radix::System::System:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb-Presentation Property*/


	public class MaxResultSetCacheSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxResultSetCacheSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:maxResultSetCacheSizeKb:maxResultSetCacheSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxResultSetCacheSizeKb getMaxResultSetCacheSizeKb();
	/*Radix::System::System:dualControlForAssignRole:dualControlForAssignRole-Presentation Property*/


	public class DualControlForAssignRole extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public DualControlForAssignRole(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForAssignRole:dualControlForAssignRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:dualControlForAssignRole:dualControlForAssignRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public DualControlForAssignRole getDualControlForAssignRole();
	/*Radix::System::System:oraImplStmtCacheSize:oraImplStmtCacheSize-Presentation Property*/


	public class OraImplStmtCacheSize extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OraImplStmtCacheSize(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize();
	/*Radix::System::System:profileLogStoreDays:profileLogStoreDays-Presentation Property*/


	public class ProfileLogStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ProfileLogStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileLogStoreDays:profileLogStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:profileLogStoreDays:profileLogStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ProfileLogStoreDays getProfileLogStoreDays();
	/*Radix::System::System:defaultAuditScheme:defaultAuditScheme-Presentation Property*/


	public class DefaultAuditScheme extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public DefaultAuditScheme(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Audit.web.AuditScheme.AuditScheme_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Audit.web.AuditScheme.AuditScheme_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Audit.web.AuditScheme.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Audit.web.AuditScheme.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditScheme:defaultAuditScheme")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:defaultAuditScheme:defaultAuditScheme")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public DefaultAuditScheme getDefaultAuditScheme();
	/*Radix::System::System:useOraImplStmtCache:useOraImplStmtCache-Presentation Property*/


	public class UseOraImplStmtCache extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseOraImplStmtCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:useOraImplStmtCache:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:useOraImplStmtCache:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache();
	/*Radix::System::System:instStateGatherPeriodSec:instStateGatherPeriodSec-Presentation Property*/


	public class InstStateGatherPeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstStateGatherPeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateGatherPeriodSec getInstStateGatherPeriodSec();
	/*Radix::System::System:easSessionInactivityMins:easSessionInactivityMins-Presentation Property*/


	public class EasSessionInactivityMins extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EasSessionInactivityMins(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionInactivityMins:easSessionInactivityMins")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionInactivityMins:easSessionInactivityMins")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EasSessionInactivityMins getEasSessionInactivityMins();
	/*Radix::System::System:enableSensitiveTrace:enableSensitiveTrace-Presentation Property*/


	public class EnableSensitiveTrace extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public EnableSensitiveTrace(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:enableSensitiveTrace:enableSensitiveTrace")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:enableSensitiveTrace:enableSensitiveTrace")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public EnableSensitiveTrace getEnableSensitiveTrace();
	/*Radix::System::System:aadcMemberId:aadcMemberId-Presentation Property*/


	public class AadcMemberId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcMemberId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::System::System:easSessionActivityMins:easSessionActivityMins-Presentation Property*/


	public class EasSessionActivityMins extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EasSessionActivityMins(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionActivityMins:easSessionActivityMins")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easSessionActivityMins:easSessionActivityMins")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EasSessionActivityMins getEasSessionActivityMins();
	/*Radix::System::System:easKrbPrincName:easKrbPrincName-Presentation Property*/


	public class EasKrbPrincName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public EasKrbPrincName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easKrbPrincName:easKrbPrincName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:easKrbPrincName:easKrbPrincName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public EasKrbPrincName getEasKrbPrincName();
	/*Radix::System::System:limitEasSessionsPerUsr:limitEasSessionsPerUsr-Presentation Property*/


	public class LimitEasSessionsPerUsr extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LimitEasSessionsPerUsr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:limitEasSessionsPerUsr:limitEasSessionsPerUsr")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:limitEasSessionsPerUsr:limitEasSessionsPerUsr")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LimitEasSessionsPerUsr getLimitEasSessionsPerUsr();
	/*Radix::System::System:arteGuiTraceProfile:arteGuiTraceProfile-Presentation Property*/


	public class ArteGuiTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteGuiTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteGuiTraceProfile:arteGuiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteGuiTraceProfile:arteGuiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ArteGuiTraceProfile getArteGuiTraceProfile();
	/*Radix::System::System:arteFileTraceProfile:arteFileTraceProfile-Presentation Property*/


	public class ArteFileTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteFileTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteFileTraceProfile:arteFileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteFileTraceProfile:arteFileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ArteFileTraceProfile getArteFileTraceProfile();
	/*Radix::System::System:askUserPwdAfterInactivity:askUserPwdAfterInactivity-Presentation Property*/


	public class AskUserPwdAfterInactivity extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AskUserPwdAfterInactivity(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:askUserPwdAfterInactivity:askUserPwdAfterInactivity")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:askUserPwdAfterInactivity:askUserPwdAfterInactivity")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AskUserPwdAfterInactivity getAskUserPwdAfterInactivity();
	/*Radix::System::System:pwdMustContainNChars:pwdMustContainNChars-Presentation Property*/


	public class PwdMustContainNChars extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainNChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainNChars:pwdMustContainNChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainNChars:pwdMustContainNChars")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainNChars getPwdMustContainNChars();
	/*Radix::System::System:pwdMustContainSChars:pwdMustContainSChars-Presentation Property*/


	public class PwdMustContainSChars extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainSChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainSChars:pwdMustContainSChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainSChars:pwdMustContainSChars")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainSChars getPwdMustContainSChars();
	/*Radix::System::System:aadcAffinityTimeoutSec:aadcAffinityTimeoutSec-Presentation Property*/


	public class AadcAffinityTimeoutSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcAffinityTimeoutSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcAffinityTimeoutSec:aadcAffinityTimeoutSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcAffinityTimeoutSec:aadcAffinityTimeoutSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcAffinityTimeoutSec getAadcAffinityTimeoutSec();
	/*Radix::System::System:blockUserInvalidLogonMins:blockUserInvalidLogonMins-Presentation Property*/


	public class BlockUserInvalidLogonMins extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public BlockUserInvalidLogonMins(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonMins:blockUserInvalidLogonMins")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:blockUserInvalidLogonMins:blockUserInvalidLogonMins")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public BlockUserInvalidLogonMins getBlockUserInvalidLogonMins();
	/*Radix::System::System:auditStorePeriod1:auditStorePeriod1-Presentation Property*/


	public class AuditStorePeriod1 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod1(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod1:auditStorePeriod1")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod1:auditStorePeriod1")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod1 getAuditStorePeriod1();
	/*Radix::System::System:pwdMustDifferFromName:pwdMustDifferFromName-Presentation Property*/


	public class PwdMustDifferFromName extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustDifferFromName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustDifferFromName:pwdMustDifferFromName")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustDifferFromName:pwdMustDifferFromName")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustDifferFromName getPwdMustDifferFromName();
	/*Radix::System::System:name:name-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::System::System:auditStorePeriod4:auditStorePeriod4-Presentation Property*/


	public class AuditStorePeriod4 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod4(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod4:auditStorePeriod4")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod4:auditStorePeriod4")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod4 getAuditStorePeriod4();
	/*Radix::System::System:auditStorePeriod5:auditStorePeriod5-Presentation Property*/


	public class AuditStorePeriod5 extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AuditStorePeriod5(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod5:auditStorePeriod5")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:auditStorePeriod5:auditStorePeriod5")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AuditStorePeriod5 getAuditStorePeriod5();
	/*Radix::System::System:pwdMustContainAChars:pwdMustContainAChars-Presentation Property*/


	public class PwdMustContainAChars extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PwdMustContainAChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainAChars:pwdMustContainAChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:pwdMustContainAChars:pwdMustContainAChars")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainAChars getPwdMustContainAChars();
	/*Radix::System::System:eventStoreDays:eventStoreDays-Presentation Property*/


	public class EventStoreDays extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EventStoreDays(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:eventStoreDays:eventStoreDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:eventStoreDays:eventStoreDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EventStoreDays getEventStoreDays();
	/*Radix::System::System:aadcTestedMemberId:aadcTestedMemberId-Presentation Property*/


	public class AadcTestedMemberId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AadcTestedMemberId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcTestedMemberId:aadcTestedMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:aadcTestedMemberId:aadcTestedMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcTestedMemberId getAadcTestedMemberId();
	/*Radix::System::System:arteCountry:arteCountry-Presentation Property*/


	public class ArteCountry extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteCountry(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EIsoCountry dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoCountry ? (org.radixware.kernel.common.enums.EIsoCountry)x : org.radixware.kernel.common.enums.EIsoCountry.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EIsoCountry> getValClass(){
			return org.radixware.kernel.common.enums.EIsoCountry.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EIsoCountry dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EIsoCountry ? (org.radixware.kernel.common.enums.EIsoCountry)x : org.radixware.kernel.common.enums.EIsoCountry.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteCountry:arteCountry")
		public  org.radixware.kernel.common.enums.EIsoCountry getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteCountry:arteCountry")
		public   void setValue(org.radixware.kernel.common.enums.EIsoCountry val) {
			Value = val;
		}
	}
	public ArteCountry getArteCountry();
	/*Radix::System::System:arteDbTraceProfile:arteDbTraceProfile-Presentation Property*/


	public class ArteDbTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ArteDbTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteDbTraceProfile:arteDbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:arteDbTraceProfile:arteDbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ArteDbTraceProfile getArteDbTraceProfile();
	/*Radix::System::System:canEditDualControlForAssignRole:canEditDualControlForAssignRole-Presentation Property*/


	public class CanEditDualControlForAssignRole extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanEditDualControlForAssignRole(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:canEditDualControlForAssignRole:canEditDualControlForAssignRole")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:canEditDualControlForAssignRole:canEditDualControlForAssignRole")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanEditDualControlForAssignRole getCanEditDualControlForAssignRole();
	public static class ExportManifest extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportManifest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.services.SystemDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.services.SystemDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.services.SystemDocument.class);
		}

	}

	public static class MoveJobsFromOtherAadcMember extends org.radixware.kernel.common.client.models.items.Command{
		protected MoveJobsFromOtherAadcMember(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class ShowUsedPorts extends org.radixware.kernel.common.client.models.items.Command{
		protected ShowUsedPorts(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class);
		}

	}

	public static class ExportPwdBlackList extends org.radixware.kernel.common.client.models.items.Command{
		protected ExportPwdBlackList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ImportPwdBlackList extends org.radixware.kernel.common.client.models.items.Command{
		protected ImportPwdBlackList(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ImportManifest extends org.radixware.kernel.common.client.models.items.Command{
		protected ImportManifest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.services.SystemDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class ClearTracedSensitiveData extends org.radixware.kernel.common.client.models.items.Command{
		protected ClearTracedSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class ShowAadcLocks extends org.radixware.kernel.common.client.models.items.Command{
		protected ShowAadcLocks(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::System::System - Web Meta*/

/*Radix::System::System-Entity Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class System_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::System:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
			"Radix::System::System",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEVI4WYHRWU3GKERLIZ5THVPBBPDUIMWZ"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6MYEBR27BCYLKIESD3AI6T2QM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),0,

			/*Radix::System::System:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::System:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBMVVID4SJVHSZN77POWURCNBUU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QCIURENNRFBZMOBOBUJYCAVNI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGFPIY4PW4OWDRRWUAAIT4AGD7E"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2EUYB5G2BFQFPR2JOYCS5KT3A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:blockUserInvalidLogonCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCLAU6NMGVDSNKN7ZPB6SQJE5Y"),
						"blockUserInvalidLogonCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWAOYX6X4RFF6XEYQ5ML7S4WEVY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("6"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:blockUserInvalidLogonCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:blockUserInvalidLogonMins:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQTEXFSBLXJES3AOB6SXXKNGQQE"),
						"blockUserInvalidLogonMins",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls24HMJTW7TRGSDH342B75DXDFQI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:blockUserInvalidLogonMins:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:askUserPwdAfterInactivity:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI"),
						"askUserPwdAfterInactivity",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73T4HKGMGJDSJEABRTO3Z2ZH3M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:askUserPwdAfterInactivity:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteDbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXLSJR3OUKLNRDAQSABIFNQAAAE"),
						"arteDbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBATUBSKH4JCSHKW27MQBKMX7IE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						null,
						true,

						/*Radix::System::System:arteDbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteFileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYF3QYYJ7BFS7AJ6YRMZUFBE4M"),
						"arteFileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54UK5YQD3BA4JO42VTY6NB7SNM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						null,
						true,

						/*Radix::System::System:arteFileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteGuiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJDA72X3RRHX5AQ4RSCK3YNA44"),
						"arteGuiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6F2K2XUC6RAURKEX6LSZUUQJPA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						true,
						null,
						true,

						/*Radix::System::System:arteGuiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:easSessionActivityMins:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6ABGYQYNVGYRC6NTJ76DIBZ4I"),
						"easSessionActivityMins",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNOOXLWEZF5AHDHK7KCQK25G3WU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1440"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:easSessionActivityMins:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:easSessionInactivityMins:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLPOXOGA2J3NRDJIRACQMTAIZT4"),
						"easSessionInactivityMins",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WDOS74FBJCRLPOXMAEHLOUHX4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("15"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:easSessionInactivityMins:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteLanguage:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col546FXWYZJ3NRDJIRACQMTAIZT4"),
						"arteLanguage",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH7ZA7XDFWREHHCPOIKLNTLTWYY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("en"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:arteLanguage:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod1:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJ6YTFHGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod1",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIGDACHB74VDKFLE6IOU6WRI7XM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::System::System:auditStorePeriod1:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod2:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3J6FBUPGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod2",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVVHYBY32BFZBKRTZKFMJI5P34"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("7"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod2:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod3:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3N6FBUPGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod3",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTPQ4HWFOFFGFF7DABTXOKHNSM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("31"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod3:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod4:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTJMVJWXGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod4",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5F5BJTJR7JFIVH3DCKB3GS755Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("100"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod4:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:auditStorePeriod5:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTNMVJWXGWPNBDPLGABQJO5ADDQ"),
						"auditStorePeriod5",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X3YPJQ2UZF5ZDV2UHCSFZD2WA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("366"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:auditStorePeriod5:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:defaultAuditSchemeId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEKZMW4XGWPNBDPLGABQJO5ADDQ"),
						"defaultAuditSchemeId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTS7Q6ACKBHFTP43WZL4EJ4WKE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:defaultAuditSchemeId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:eventStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUMLKGQCFD6VDBNKJAAUMFADAIA"),
						"eventStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFG4HE3KQLNC65NOTWPHD73QEG4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:eventStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:notificationStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIH2FRGR6EKWDRWGPAAUUN6FMUG"),
						"notificationStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDEOJYAVPBDGNNGKN27HNRUO3I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:notificationStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:defaultAuditScheme:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOT4CGMPQVDNNBAKW4BRLODW4Y"),
						"defaultAuditScheme",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5VT5JFWDFFLNDJKC3RWV3V7CE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecJBWGL34ATTNBDPK5ABQJO5ADDQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ"),
						null,
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::System:profileMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),
						"profileMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VRVICQMXBCRPBJ7KV4WCWLT3Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsCP5FOQAA6FBYBEKO5SCIALT5SQ"),
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

						/*Radix::System::System:profileMode:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsCP5FOQAA6FBYBEKO5SCIALT5SQ"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:profilePeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7NWAJN2XXFEUNERD2PHDEGCCYQ"),
						"profilePeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEASZSL2HONH6VBQX44IGC6FE3A"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNG5EDRADNFLFMHRYHRYNIVZRQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:profilePeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:profiledPrefixes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLE6RFD22ZHT7OIHCXEYU4WMVQ"),
						"profiledPrefixes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXW2SS6PTDFDTRARH67Q4ZW4AP4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						null,
						true,

						/*Radix::System::System:profiledPrefixes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:uniquePwdSeqLen:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK5BEU6WO5H4VKRDA46ALDEZ3M"),
						"uniquePwdSeqLen",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJJGIZTWWBBVLEXZ2SFHMRU62A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("4"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:uniquePwdSeqLen:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDHWA65432RB4BCZEECT6PQWKO4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:enableSensitiveTrace:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRLGIOF3DVB2XEFQXLCJLPUYTU"),
						"enableSensitiveTrace",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6X6BH3GNFBHLPHHZQUH7UHT74"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ONLY_EXISTING,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:enableSensitiveTrace:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMinLen:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2J4RCA57UBFPFNVYLNMEXJI5CU"),
						"pwdMinLen",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIF5YA2YSJC4RM27QEAWWONNDQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("7"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:pwdMinLen:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4KLS7EXAAVC6JIXJWPLZHKHGUE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainAChars:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTY6XN5X66FD2FIC7ZQVWKRV7YQ"),
						"pwdMustContainAChars",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKMHYO5UGZB4NP56UOX4SEL3XQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainAChars:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainNChars:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJXZPIRBDZG6HCJZ4F2JDH7UFI"),
						"pwdMustContainNChars",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGARWWVW27JF63JIA5BEWQCZGSE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainNChars:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:profileLogStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDV6X7RAL5DCBO2HENGADFNFTY"),
						"profileLogStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYJSOLZHLFBH7BDDHW2FBY653Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::System::System:profileLogStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:metricHistoryStoreDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2XLLDK4RVAMFAL7O6XQGRTVNY"),
						"metricHistoryStoreDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSR3TXHYIVBDWBEIYXNMV2QLYGQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:metricHistoryStoreDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),
						"pwdExpirationPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2L6IOLO2NCOXN5NIWIVQXQUXY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2PGFYGYXZALZP6L6J4BBFRXPY"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:arteCountry:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWGL2VHS7RNBIRA3NYYV3KGAIPI"),
						"arteCountry",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRENN54WPHFE63BYZKKGAXXF6Q4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQK3EKSSOTZDKVFKO3FY5PTAV7A"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("US"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:arteCountry:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsQK3EKSSOTZDKVFKO3FY5PTAV7A"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:certAttrForUserLogin:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WZAHHSUZVCAVPPUZDJX5WWCA4"),
						"certAttrForUserLogin",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQQSEHVQM6FGATHBCAO66ND6TNQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("CN"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:certAttrForUserLogin:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VZI6BUPLNG5LMIXHOSQEND7UY"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:easKrbPrincName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6KLR5HJ3JDI7DTS5S52SRQEFU"),
						"easKrbPrincName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSPAPCBJMJFJ5FTKOZJO65G7OU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:easKrbPrincName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:userExtDevLanguages:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4"),
						"userExtDevLanguages",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsECVKEGYGUFFWZIA5FCUVX5MGGA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),
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

						/*Radix::System::System:userExtDevLanguages:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs2G44GBNBE5D7RO2QHQSVRZNLG4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRUSBOMVINE7LMQ2SEQB5V33LM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCZ4MLVRTXJGQZH6VWZ2AMWMOQE"),
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:extSystemCode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFRZFQPKGI5EM7DCHTHZ4PSKVCA"),
						"extSystemCode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYPDVMQINNGVRAFO43KDZ2TR2Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRRERXT5RBGXZAQV6WQ37VNZWI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:extSystemCode:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:oraImplStmtCacheSize:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"),
						"oraImplStmtCacheSize",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BOWQW5VAVCERGJRCXO6K2VE4Y"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("100"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:oraImplStmtCacheSize:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:useOraImplStmtCache:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKQKCWI72DVAO7OFVSNBZ7XN2WQ"),
						"useOraImplStmtCache",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVI6M5A3URCVVJNDVCOQ34LPCI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:useOraImplStmtCache:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustDifferFromName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSLXTNR7MBRF23O655OVBOAL5LA"),
						"pwdMustDifferFromName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5SHVT4RQNCZDB64TNPGSSFQB4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustDifferFromName:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:dualControlForAssignRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ7OWNFGW3RCUVIE7PJUK3OO5GI"),
						"dualControlForAssignRole",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQIK4SZMH5ALFCSF5SGU3ZBIQY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:dualControlForAssignRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:onStartArteUserFunc:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFXLCNMISE5EALECQELQKF5XGMI"),
						"onStartArteUserFunc",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.USER,
						5,
						null,
						null,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclMZEXQBYWYBEYFOLCIZCSSOBTOM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM"),
						null,
						null,
						null,
						0L,
						0L,false,false),

					/*Radix::System::System:dualControlForCfgMgmt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5QRDG2EULRGM3IGS4RAPZWD2RQ"),
						"dualControlForCfgMgmt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQD2G3JOHXBHD7LOIMUS2FAZJ6M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:dualControlForCfgMgmt:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:canEditDualControlForAssignRole:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),
						"canEditDualControlForAssignRole",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:canEditDualControlForAssignRole:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:limitEasSessionsPerUsr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"),
						"limitEasSessionsPerUsr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQI3B2WEYTJG43HUBW6FPF5ET5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:limitEasSessionsPerUsr:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ABEVLLODBGIRAKIL7IJWE6PPI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:writeContextToFile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col42FD7ZUMLNA77LXG67VMOVRC5I"),
						"writeContextToFile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOIWG2WZFJZHQPCBZFVLUHFFH3U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:writeContextToFile:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainACharsInMixedCase:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"),
						"pwdMustContainACharsInMixedCase",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGWU2LKTHJH6XCDZXEJHTWNKCQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainACharsInMixedCase:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdMustContainSChars:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSC5YLHYXJD3ZICERRHVMMWRZI"),
						"pwdMustContainSChars",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHFHFWIXTNFSDLPV3JAKFQHMZM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:pwdMustContainSChars:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:pwdBlackList:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM"),
						"pwdBlackList",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYLKTKYP7JB7BOA5HQAXTQWAOY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::System::System:pwdBlackList:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,false),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDFA3GVWRNBA3P3JJLDYR3A5KA"),
						null,
						null,
						false,1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:temporaryPwdExpirationPeriod:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6EXIXL45PZEBZOF2MKFF7RJWSA"),
						"temporaryPwdExpirationPeriod",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDAARJHO4ZZFBTNZZAWF2EN2L64"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("72"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:temporaryPwdExpirationPeriod:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAU2ZG3FLJA4NEQ7ETSXDZRJN4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2PT6HH34FHXRHWAOZLL6HQATU"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM2XPCRZZINEQDIBUOB2D6SIMPE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcTestedMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q"),
						"aadcTestedMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3D3IFOTV25BLPA6U7AMANTBMUQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
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

						/*Radix::System::System:aadcTestedMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-9L,9L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcCommitedLockExp:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGS3V2W2ZNNEXJA3CYQJ5O4AFSA"),
						"aadcCommitedLockExp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKFLGQE3PJCQTCPVL7QHQ4A4H4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("600"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:aadcCommitedLockExp:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(60L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcUnlockTables:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA7LVBINPNFH2RB6LUIQLSE34DE"),
						"aadcUnlockTables",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls342YRUHE2NHA7E7UB4MM53BU4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsML5IS6GATJGYZARZVIXHB2C724"),
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

						/*Radix::System::System:aadcUnlockTables:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsML5IS6GATJGYZARZVIXHB2C724"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3C54IPQI5BEPBNBZ2SGAST36Y"),
						null,
						null,
						false,1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:instStateGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"),
						"instStateGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLW6NYEH5PFFWDEBOIFT2V7SXWE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:instStateGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,120L,(byte)-1,null,5L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCDQGQBAWREB7DDJBBYE53D4RI"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:instStateForcedGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"),
						"instStateForcedGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTYYWK7Q45RAPHJX2L6N35IMGNI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:instStateForcedGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,300L,(byte)-1,null,10L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26INKFHONJBX5JY6UAZQ6V2J5Y"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:instStateHistoryStorePeriodDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"),
						"instStateHistoryStorePeriodDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD5WBZFOIQFERPMHIXFVFDRD5BQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("14"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:instStateHistoryStorePeriodDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,90L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:maxResultSetCacheSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"),
						"maxResultSetCacheSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4524VRNEZRBHHDI535GADAZYJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("102400"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:maxResultSetCacheSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::System:aadcAffinityTimeoutSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5M4PIAUSFCG5HDFBZHIFMWHAA"),
						"aadcAffinityTimeoutSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FFZPYU5RAAZE6LLK5366JINI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::System:aadcAffinityTimeoutSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:ShowAadcLocks-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdYPFAVUMQHRBSFNCLJ7BPZMBIAU"),
						"ShowAadcLocks",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCX3AFOUTFHYROFXYVORR4ZIGI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgD2LY2OKLJZETTLJ734ZBIGGFTU"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::System:MoveJobsFromOtherAadcMember-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd4WUJPZM72ZHA3DMIP7CPI73WFE"),
						"MoveJobsFromOtherAadcMember",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWRBYFLI3FGCHPGBTD7HVFQW3M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKJLXMBKFKRDNRKVPMEZZL7EAPI"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref3MAHBP5YJ3NRDAQSABIFNQAAAE"),"System=>Scheme (defaultAuditSchemeId=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblJBWGL34ATTNBDPK5ABQJO5ADDQ"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEKZMW4XGWPNBDPLGABQJO5ADDQ")},new String[]{"defaultAuditSchemeId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAB22MAMBTTNBDPK5ABQJO5ADDQ")},new String[]{"guid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY")},
			false,true,false);
}

/* Radix::System::System:General - Desktop Meta*/

/*Radix::System::System:General-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::System:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::System:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGRQQSUEERFH7JFCJPQSB7XXAJE"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVVQMVMSDWZD75LDNUQGSBGTDLU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCLAU6NMGVDSNKN7ZPB6SQJE5Y"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQTEXFSBLXJES3AOB6SXXKNGQQE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGFPIY4PW4OWDRRWUAAIT4AGD7E"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col546FXWYZJ3NRDJIRACQMTAIZT4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOT4CGMPQVDNNBAKW4BRLODW4Y"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWGL2VHS7RNBIRA3NYYV3KGAIPI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ7OWNFGW3RCUVIE7PJUK3OO5GI"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5QRDG2EULRGM3IGS4RAPZWD2RQ"),0,8,1,false,false)
			},null),

			/*Radix::System::System:General:Trace-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgX5EHUJGZOVDUND3ISHNCWSHFMY"),"Trace",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2327TTOL5RGFLASMVLBMX4R7II"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXLSJR3OUKLNRDAQSABIFNQAAAE"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJDA72X3RRHX5AQ4RSCK3YNA44"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYF3QYYJ7BFS7AJ6YRMZUFBE4M"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLRLGIOF3DVB2XEFQXLCJLPUYTU"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col42FD7ZUMLNA77LXG67VMOVRC5I"),0,4,1,false,false)
			},null),

			/*Radix::System::System:General:Eas-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJCYHGIUJVVE6FCFYIIZUL3MXOU"),"Eas",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVO77QYHNIRE5TM7JHMZYJ2LIIQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6ABGYQYNVGYRC6NTJ76DIBZ4I"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLPOXOGA2J3NRDJIRACQMTAIZT4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK5BEU6WO5H4VKRDA46ALDEZ3M"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2J4RCA57UBFPFNVYLNMEXJI5CU"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTY6XN5X66FD2FIC7ZQVWKRV7YQ"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJXZPIRBDZG6HCJZ4F2JDH7UFI"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WZAHHSUZVCAVPPUZDJX5WWCA4"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6KLR5HJ3JDI7DTS5S52SRQEFU"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSLXTNR7MBRF23O655OVBOAL5LA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSC5YLHYXJD3ZICERRHVMMWRZI"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6EXIXL45PZEBZOF2MKFF7RJWSA"),0,12,1,false,false)
			},null),

			/*Radix::System::System:General:Storage-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg24FJ7QAPONHAVJ2WLSDRNJGKME"),"Storage",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINFDZDLHYNAK3MHZ7EOKQHGPDQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUMLKGQCFD6VDBNKJAAUMFADAIA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIH2FRGR6EKWDRWGPAAUUN6FMUG"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJ6YTFHGWPNBDPLGABQJO5ADDQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3J6FBUPGWPNBDPLGABQJO5ADDQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3N6FBUPGWPNBDPLGABQJO5ADDQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTJMVJWXGWPNBDPLGABQJO5ADDQ"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTNMVJWXGWPNBDPLGABQJO5ADDQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDV6X7RAL5DCBO2HENGADFNFTY"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2XLLDK4RVAMFAL7O6XQGRTVNY"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"),0,9,1,false,false)
			},null),

			/*Radix::System::System:General:Performance-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFPRVWBR7QRE4DCSTHTIBVIZEZE"),"Performance",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCS55454FFFBZPOE4XHR5MF7MI"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHLE6RFD22ZHT7OIHCXEYU4WMVQ"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7NWAJN2XXFEUNERD2PHDEGCCYQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKQKCWI72DVAO7OFVSNBZ7XN2WQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruFXLCNMISE5EALECQELQKF5XGMI"),0,5,1,false,false)
			},null),

			/*Radix::System::System:General:Development-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZTEACN6CPFF3HPSFK57XG5Y5XY"),"Development",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCQBDFV4ZBZEBTLKTWLNN2ZNOOY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4"),0,0,1,false,false)
			},null),

			/*Radix::System::System:General:Priorities-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTGYM26OEC5H6RJSXNAY7CPAQKY"),"Priorities",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIWSU6H5LFBHLB7HCHADCJWBRI"),null,null,null),

			/*Radix::System::System:General:PriorityMap-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgC4OJW3X4JJCHLHPT4VDJ4QGMUI"),"PriorityMap",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi53ISKTXR3FBMZCTX3FOO5L7TME")),

			/*Radix::System::System:General:Boosting-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQLUPDISTNJH6BGCAAMZGUQ3THY"),"Boosting",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3OUH2ZLNDJGJLJSA54W2TG7XOM")),

			/*Radix::System::System:General:Services-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAW7OFX57SRHQBJVQ4ULWQDY3PE"),"Services",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiN3U3M644MJCXFAVTKQ7G3GSENE")),

			/*Radix::System::System:General:AADC-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5YTYPJB4RRFL5G3MYPX27MM25U"),"AADC",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5DWIFNUTFD3XJS6BDYIBAMCUU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2PT6HH34FHXRHWAOZLL6HQATU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA7LVBINPNFH2RB6LUIQLSE34DE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGS3V2W2ZNNEXJA3CYQJ5O4AFSA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5M4PIAUSFCG5HDFBZHIFMWHAA"),0,4,1,false,false)
			},null),

			/*Radix::System::System:General:Monitoring-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTPMCMFWZO5DFHOFAXAEUF2UGME"),"Monitoring",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSF6NYEIRFAIZEUZ7Q7NUSEVQU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"),0,1,1,false,false)
			},null),

			/*Radix::System::System:General:Miscellaneous-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHRNEWBC76FHKXOZGWLDBS2GWXE"),"Miscellaneous",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls247QGK7RNJBAJF6IVY4DSRQBKQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGRQQSUEERFH7JFCJPQSB7XXAJE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5YTYPJB4RRFL5G3MYPX27MM25U")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgX5EHUJGZOVDUND3ISHNCWSHFMY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJCYHGIUJVVE6FCFYIIZUL3MXOU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg24FJ7QAPONHAVJ2WLSDRNJGKME")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFPRVWBR7QRE4DCSTHTIBVIZEZE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTPMCMFWZO5DFHOFAXAEUF2UGME")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZTEACN6CPFF3HPSFK57XG5Y5XY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTGYM26OEC5H6RJSXNAY7CPAQKY")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgC4OJW3X4JJCHLHPT4VDJ4QGMUI")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQLUPDISTNJH6BGCAAMZGUQ3THY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAW7OFX57SRHQBJVQ4ULWQDY3PE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHRNEWBC76FHKXOZGWLDBS2GWXE"))}
	,

	/*Radix::System::System:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::System::System:General:Service-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiN3U3M644MJCXFAVTKQ7G3GSENE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecC2OWQGDVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBWN5EPD5Q3NRDB5JAALOMT5GDM"),
					65535,
					null,
					16560,true),

				/*Radix::System::System:General:JobExecutorPriorityMap-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi53ISKTXR3FBMZCTX3FOO5L7TME"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2ZW5SCE4DVFAXABS7LA5PFIURY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZBR2DHYVUVFCHA4ZNPCHBYHP2U"),
					0,
					null,
					16560,true),

				/*Radix::System::System:General:JobExecutorUnitBoost-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3OUH2ZLNDJGJLJSA54W2TG7XOM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPCE24TUPIHNRDJIEACQMTAIZT4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprASXNPKNDZPNRDB65AALOMT5GDM"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	144,0,0,null);
}
/* Radix::System::System:General - Web Meta*/

/*Radix::System::System:General-Editor Presentation*/

package org.radixware.ads.System.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::System:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::System:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGRQQSUEERFH7JFCJPQSB7XXAJE"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVVQMVMSDWZD75LDNUQGSBGTDLU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDCLAU6NMGVDSNKN7ZPB6SQJE5Y"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQTEXFSBLXJES3AOB6SXXKNGQQE"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGFPIY4PW4OWDRRWUAAIT4AGD7E"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col546FXWYZJ3NRDJIRACQMTAIZT4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOT4CGMPQVDNNBAKW4BRLODW4Y"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWGL2VHS7RNBIRA3NYYV3KGAIPI"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ7OWNFGW3RCUVIE7PJUK3OO5GI"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5QRDG2EULRGM3IGS4RAPZWD2RQ"),0,8,1,false,false)
			},null),

			/*Radix::System::System:General:Eas-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJCYHGIUJVVE6FCFYIIZUL3MXOU"),"Eas",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVO77QYHNIRE5TM7JHMZYJ2LIIQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6ABGYQYNVGYRC6NTJ76DIBZ4I"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLPOXOGA2J3NRDJIRACQMTAIZT4"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMYUJ6LOEMVDTHCVJ6BTLZ2PTHI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCK5BEU6WO5H4VKRDA46ALDEZ3M"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2J4RCA57UBFPFNVYLNMEXJI5CU"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTY6XN5X66FD2FIC7ZQVWKRV7YQ"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOJXZPIRBDZG6HCJZ4F2JDH7UFI"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIVNUPJC565CXRNA3SBZKHI2LG4"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7WZAHHSUZVCAVPPUZDJX5WWCA4"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM6KLR5HJ3JDI7DTS5S52SRQEFU"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSLXTNR7MBRF23O655OVBOAL5LA"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMBAPYLBLXFEOPBVK2IYG2HSVC4"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSC5YLHYXJD3ZICERRHVMMWRZI"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH573HIYACZAATKHFKQK53NQOPM"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6EXIXL45PZEBZOF2MKFF7RJWSA"),0,12,1,false,false)
			},null),

			/*Radix::System::System:General:Storage-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg24FJ7QAPONHAVJ2WLSDRNJGKME"),"Storage",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINFDZDLHYNAK3MHZ7EOKQHGPDQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUMLKGQCFD6VDBNKJAAUMFADAIA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIH2FRGR6EKWDRWGPAAUUN6FMUG"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRJ6YTFHGWPNBDPLGABQJO5ADDQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3J6FBUPGWPNBDPLGABQJO5ADDQ"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3N6FBUPGWPNBDPLGABQJO5ADDQ"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTJMVJWXGWPNBDPLGABQJO5ADDQ"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTNMVJWXGWPNBDPLGABQJO5ADDQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDV6X7RAL5DCBO2HENGADFNFTY"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colE2XLLDK4RVAMFAL7O6XQGRTVNY"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"),0,9,1,false,false)
			},null),

			/*Radix::System::System:General:Development-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZTEACN6CPFF3HPSFK57XG5Y5XY"),"Development",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCQBDFV4ZBZEBTLKTWLNN2ZNOOY"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JACLKO3UNAMTIFUAMZ43AXNB4"),0,0,1,false,false)
			},null),

			/*Radix::System::System:General:Priorities-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTGYM26OEC5H6RJSXNAY7CPAQKY"),"Priorities",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIWSU6H5LFBHLB7HCHADCJWBRI"),null,null,null),

			/*Radix::System::System:General:PriorityMap-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgC4OJW3X4JJCHLHPT4VDJ4QGMUI"),"PriorityMap",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi53ISKTXR3FBMZCTX3FOO5L7TME")),

			/*Radix::System::System:General:Boosting-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQLUPDISTNJH6BGCAAMZGUQ3THY"),"Boosting",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3OUH2ZLNDJGJLJSA54W2TG7XOM")),

			/*Radix::System::System:General:AADC-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5YTYPJB4RRFL5G3MYPX27MM25U"),"AADC",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5DWIFNUTFD3XJS6BDYIBAMCUU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM2PT6HH34FHXRHWAOZLL6HQATU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWT5OQ6VLRDUXIBQ6CZ5S7O53Q"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA7LVBINPNFH2RB6LUIQLSE34DE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGS3V2W2ZNNEXJA3CYQJ5O4AFSA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ5M4PIAUSFCG5HDFBZHIFMWHAA"),0,4,1,false,false)
			},null),

			/*Radix::System::System:General:Monitoring-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTPMCMFWZO5DFHOFAXAEUF2UGME"),"Monitoring",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSF6NYEIRFAIZEUZ7Q7NUSEVQU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"),0,1,1,false,false)
			},null),

			/*Radix::System::System:General:Miscellaneous-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHRNEWBC76FHKXOZGWLDBS2GWXE"),"Miscellaneous",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls247QGK7RNJBAJF6IVY4DSRQBKQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJ5TXYWQQLNBKPDAZKGEQRQTLK4"),0,0,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGRQQSUEERFH7JFCJPQSB7XXAJE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5YTYPJB4RRFL5G3MYPX27MM25U")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJCYHGIUJVVE6FCFYIIZUL3MXOU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg24FJ7QAPONHAVJ2WLSDRNJGKME")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTPMCMFWZO5DFHOFAXAEUF2UGME")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgZTEACN6CPFF3HPSFK57XG5Y5XY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTGYM26OEC5H6RJSXNAY7CPAQKY")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgC4OJW3X4JJCHLHPT4VDJ4QGMUI")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQLUPDISTNJH6BGCAAMZGUQ3THY")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHRNEWBC76FHKXOZGWLDBS2GWXE"))}
	,

	/*Radix::System::System:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::System::System:General:JobExecutorPriorityMap-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi53ISKTXR3FBMZCTX3FOO5L7TME"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2ZW5SCE4DVFAXABS7LA5PFIURY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprZBR2DHYVUVFCHA4ZNPCHBYHP2U"),
					0,
					null,
					16560,true),

				/*Radix::System::System:General:JobExecutorUnitBoost-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3OUH2ZLNDJGJLJSA54W2TG7XOM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecPCE24TUPIHNRDJIEACQMTAIZT4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprASXNPKNDZPNRDB65AALOMT5GDM"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	144,0,0,null);
}
/* Radix::System::System:General:Model - Desktop Executable*/

/*Radix::System::System:General:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model")
public class General:Model  extends org.radixware.ads.System.explorer.System.System_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::System:General:Model:Nested classes-Nested Classes*/

	/*Radix::System::System:General:Model:ExportPwdBlackList-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList")
	public class ExportPwdBlackList  extends org.radixware.kernel.common.client.models.items.Command  {



		/*Radix::System::System:General:Model:ExportPwdBlackList:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:ExportPwdBlackList:Properties-Properties*/

		/*Radix::System::System:General:Model:ExportPwdBlackList:Methods-Methods*/

		/*Radix::System::System:General:Model:ExportPwdBlackList:ExportPwdBlackList-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList:ExportPwdBlackList")
		public  ExportPwdBlackList (org.radixware.kernel.common.client.models.Model model, org.radixware.kernel.common.client.meta.RadCommandDef command) {
			super(model,command);
		}

		/*Radix::System::System:General:Model:ExportPwdBlackList:isEnabledForProperty-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList:isEnabledForProperty")
		public  boolean isEnabledForProperty (org.radixware.kernel.common.types.Id propertyId) {
			return pwdBlackList.Value!=null && !pwdBlackList.Value.isEmpty();
		}

		/*Radix::System::System:General:Model:ExportPwdBlackList:execute-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList:execute")
		public  void execute (org.radixware.kernel.common.types.Id propertyId) {
			final ArrStr arrPwdBlackList = pwdBlackList.getValue();
			if (arrPwdBlackList!=null && !arrPwdBlackList.isEmpty()){
			    final Explorer.Qt.Types::QFileDialog dialog = 
			        new QFileDialog(null, "Select file to store list of forbidden passwords", pwdBlackList.getInitialDir());
			    dialog.setAcceptMode(Explorer.Qt.Types::QFileDialog.AcceptMode.AcceptSave);
			    dialog.setConfirmOverwrite(true);
			    getEnvironment().getProgressHandleManager().blockProgress();
			    try{
			        if (dialog.exec() == Explorer.Qt.Types::QDialog.DialogCode.Accepted.value()){
			            final java.util.List<String> selectedFiles = dialog.selectedFiles();
			            if (selectedFiles!=null && !selectedFiles.isEmpty() && selectedFiles.get(0)!=null && !selectedFiles.get(0).isEmpty()){                                
			                final String selectedFile = selectedFiles.get(0);
			                final java.io.File file = new java.io.File(selectedFile);
			                if (file.exists()){
			                    pwdBlackList.setInitialDir(file.getParentFile().getAbsolutePath());
			                }
			                if (file.isDirectory()){
			                    getEnvironment().messageError("Export error", String.format("Unable to write file '%1$s'",selectedFile));   
			                    return;
			                }
			                if (file.exists() && !org.radixware.kernel.common.utils.FileUtils.deleteFile(file)){
			                    getEnvironment().messageError("Export error", String.format("Unable to rewrite file '%1$s'",selectedFile));   
			                    return;                        
			                }
			                try{
			                    if (!file.createNewFile()){
			                        getEnvironment().messageError("Export error", String.format("Unable to create file '%1$s'",selectedFile));   
			                        return;                        
			                    }
			                }catch(Exceptions::IOException exception){
			                    getEnvironment().messageError("Export error", String.format("Unable to create file '%1$s'",selectedFile));
			                    return;
			                }
			                try(java.io.PrintWriter writer = new java.io.PrintWriter(file,"UTF-8")){
			                    for (String forbiddenPassword: arrPwdBlackList){
			                        writer.println(forbiddenPassword);
			                    }
			                }catch(Exceptions::IOException ex){
			                    final String mess = String.format("Error writing to the file '%1$s'", selectedFile);
			                    getEnvironment().processException(mess, ex);                    
			                }
			            }
			        }
			    }finally{
			        getEnvironment().getProgressHandleManager().unblockProgress();
			    }
			}
		}


	}

	/*Radix::System::System:General:Model:ImportPwdBlackList-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList")
	public class ImportPwdBlackList  extends org.radixware.kernel.common.client.models.items.Command  {



		/*Radix::System::System:General:Model:ImportPwdBlackList:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:ImportPwdBlackList:Properties-Properties*/

		/*Radix::System::System:General:Model:ImportPwdBlackList:Methods-Methods*/

		/*Radix::System::System:General:Model:ImportPwdBlackList:ImportPwdBlackList-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList:ImportPwdBlackList")
		public  ImportPwdBlackList (org.radixware.kernel.common.client.models.Model model, org.radixware.kernel.common.client.meta.RadCommandDef command) {
			super(model,command);
		}

		/*Radix::System::System:General:Model:ImportPwdBlackList:isVisibleForProperty-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList:isVisibleForProperty")
		public  boolean isVisibleForProperty (org.radixware.kernel.common.types.Id propertyId) {
			return !pwdBlackList.isReadonly();
		}

		/*Radix::System::System:General:Model:ImportPwdBlackList:execute-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList:execute")
		public  void execute (org.radixware.kernel.common.types.Id propertyId) {
			final Explorer.Qt.Types::QFileDialog dialog = 
			    new QFileDialog(null, "Select file to store list of forbidden passwords", pwdBlackList.getInitialDir());
			dialog.setFileMode(Explorer.Qt.Types::QFileDialog.FileMode.ExistingFile);
			dialog.setAcceptMode(Explorer.Qt.Types::QFileDialog.AcceptMode.AcceptOpen);
			getEnvironment().getProgressHandleManager().blockProgress();
			try{
			    if (dialog.exec() == Explorer.Qt.Types::QDialog.DialogCode.Accepted.value()){
			        final java.util.List<String> selectedFiles = dialog.selectedFiles();
			        if (selectedFiles!=null && !selectedFiles.isEmpty() && selectedFiles.get(0)!=null && !selectedFiles.get(0).isEmpty()){            
			            final String selectedFile = selectedFiles.get(0);
			            final java.io.File file = new java.io.File(selectedFile);
			            if (file.exists()){
			                pwdBlackList.setInitialDir(file.getParentFile().getAbsolutePath());
			            }
			            if (file.isFile() && file.canRead()){
			                final ArrStr newValue = new ArrStr();
			                try(java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(file), "UTF-8"))){
			                    for(String line; (line = reader.readLine()) != null; ) {
			                        if (!line.isEmpty()){
			                            newValue.add(line);
			                        }
			                    }
			                }catch(Exceptions::IOException ex){
			                    final String mess = String.format("Error writing to the file '%1$s'", selectedFile);
			                    getEnvironment().processException(mess, ex);
			                    return;
			                }
			                if (newValue!=null){
			                    pwdBlackList.Value = newValue;
			                }
			            }else{
			                getEnvironment().messageError("Import error", String.format("Unable to read file '%1$s'",selectedFile));   
			            }
			        }
			    }
			}finally{
			    getEnvironment().getProgressHandleManager().unblockProgress();
			}
		}


	}

	/*Radix::System::System:General:Model:Properties-Properties*/

	/*Radix::System::System:General:Model:profileMode-Presentation Property*/




	public class ProfileMode extends org.radixware.ads.System.explorer.System.colFJUUBIGQRJFVBLOGHVZGSLVOSU{
		public ProfileMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EProfileMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EProfileMode ? (org.radixware.kernel.common.enums.EProfileMode)x : org.radixware.kernel.common.enums.EProfileMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EProfileMode> getValClass(){
			return org.radixware.kernel.common.enums.EProfileMode.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EProfileMode dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EProfileMode ? (org.radixware.kernel.common.enums.EProfileMode)x : org.radixware.kernel.common.enums.EProfileMode.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:profileMode")
		public  org.radixware.kernel.common.enums.EProfileMode getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:profileMode")
		public   void setValue(org.radixware.kernel.common.enums.EProfileMode val) {

			internal[profileMode] = val;
			actualizeProfileOptions();
		}
	}
	public ProfileMode getProfileMode(){return (ProfileMode)getProperty(colFJUUBIGQRJFVBLOGHVZGSLVOSU);}

	/*Radix::System::System:General:Model:enableSensitiveTrace-Presentation Property*/




	public class EnableSensitiveTrace extends org.radixware.ads.System.explorer.System.colLRLGIOF3DVB2XEFQXLCJLPUYTU{
		public EnableSensitiveTrace(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:enableSensitiveTrace")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:enableSensitiveTrace")
		public   void setValue(Bool val) {

			internal[enableSensitiveTrace] = val;
			Environment.messageWarning(
			    val.booleanValue() ? "You are about to grant the global permission to save sensitive data to Event Log...."
			        : "Disabling of this option will not cause deletion of sensitive data from Event Log...."
			);

		}
	}
	public EnableSensitiveTrace getEnableSensitiveTrace(){return (EnableSensitiveTrace)getProperty(colLRLGIOF3DVB2XEFQXLCJLPUYTU);}

	/*Radix::System::System:General:Model:userExtDevLanguages-Presentation Property*/




	public class UserExtDevLanguages extends org.radixware.ads.System.explorer.System.col2JACLKO3UNAMTIFUAMZ43AXNB4{
		public UserExtDevLanguages(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Common.common.Language.Arr> getValClass(){
			return org.radixware.ads.Common.common.Language.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:userExtDevLanguages")
		public published  org.radixware.ads.Common.common.Language.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:userExtDevLanguages")
		public published   void setValue(org.radixware.ads.Common.common.Language.Arr val) {
			Value = val;
		}
	}
	public UserExtDevLanguages getUserExtDevLanguages(){return (UserExtDevLanguages)getProperty(col2JACLKO3UNAMTIFUAMZ43AXNB4);}

	/*Radix::System::System:General:Model:useOraImplStmtCache-Presentation Property*/




	public class UseOraImplStmtCache extends org.radixware.ads.System.explorer.System.colKQKCWI72DVAO7OFVSNBZ7XN2WQ{
		public UseOraImplStmtCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"))) {
				this.addDependent(aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache(){return (UseOraImplStmtCache)getProperty(colKQKCWI72DVAO7OFVSNBZ7XN2WQ);}

	/*Radix::System::System:General:Model:oraImplStmtCacheSize-Presentation Property*/




	public class OraImplStmtCacheSize extends org.radixware.ads.System.explorer.System.colJN25VAA7SVD5FKDTO47ERVQIDQ{
		public OraImplStmtCacheSize(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::System:General:Model:oraImplStmtCacheSize:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:Properties-Properties*/

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:Methods-Methods*/

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:oraImplStmtCacheSize:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useOraImplStmtCache.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize(){return (OraImplStmtCacheSize)getProperty(colJN25VAA7SVD5FKDTO47ERVQIDQ);}

	/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase-Presentation Property*/




	public class PwdMustContainACharsInMixedCase extends org.radixware.ads.System.explorer.System.colEFLQTDCHFZCTFNM77TYMJH35JY{
		public PwdMustContainACharsInMixedCase(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:Properties-Properties*/

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:Methods-Methods*/

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:isEnabled-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:isEnabled")
		public published  boolean isEnabled () {
			return super.isEnabled() && pwdMustContainAChars.Value==true;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainACharsInMixedCase")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainACharsInMixedCase")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainACharsInMixedCase getPwdMustContainACharsInMixedCase(){return (PwdMustContainACharsInMixedCase)getProperty(colEFLQTDCHFZCTFNM77TYMJH35JY);}

	/*Radix::System::System:General:Model:pwdMustContainAChars-Presentation Property*/




	public class PwdMustContainAChars extends org.radixware.ads.System.explorer.System.colTY6XN5X66FD2FIC7ZQVWKRV7YQ{
		public PwdMustContainAChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"))) {
				this.addDependent(aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainAChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainAChars")
		public   void setValue(Bool val) {

			internal[pwdMustContainAChars] = val;
			if (val==false){
			    pwdMustContainACharsInMixedCase.setValue(false);
			}
		}
	}
	public PwdMustContainAChars getPwdMustContainAChars(){return (PwdMustContainAChars)getProperty(colTY6XN5X66FD2FIC7ZQVWKRV7YQ);}

	/*Radix::System::System:General:Model:pwdBlackList-Presentation Property*/




	public class PwdBlackList extends org.radixware.ads.System.explorer.System.colH573HIYACZAATKHFKQK53NQOPM{
		public PwdBlackList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::System::System:General:Model:pwdBlackList:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::System:General:Model:pwdBlackList:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:pwdBlackList:Properties-Properties*/

		/*Radix::System::System:General:Model:pwdBlackList:initialDirSettingName-Dynamic Property*/

		protected Str initialDirSettingName=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:initialDirSettingName")
		private final  Str getInitialDirSettingName() {

			return getConfigStoreGroupName()+"/"+pwdBlackList.getId()+"/initialDir";
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:initialDirSettingName")
		private final   void setInitialDirSettingName(Str val) {
			initialDirSettingName = val;
		}

		/*Radix::System::System:General:Model:pwdBlackList:Methods-Methods*/

		/*Radix::System::System:General:Model:pwdBlackList:getInitialDir-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:getInitialDir")
		private final  Str getInitialDir () {
			final String initialDir = getEnvironment().getConfigStore().readString(initialDirSettingName, System.getProperty("user.home"));
			final java.nio.file.Path dirPath = java.nio.file.Paths.get(initialDir);
			return dirPath == null || !dirPath.toFile().isDirectory() ? System.getProperty("user.home") : initialDir;
		}

		/*Radix::System::System:General:Model:pwdBlackList:setInitialDir-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:setInitialDir")
		private final  void setInitialDir (Str path) {
			getEnvironment().getConfigStore().writeString(initialDirSettingName,path);
		}

		/*Radix::System::System:General:Model:pwdBlackList:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			final Client.Validators::ValidationResult defaultValidationResult = super.validateValue();
			if (super.validateValue()==Client.Validators::ValidationResult.ACCEPTABLE){
			    final ArrStr value = getValue();
			    final StringBuilder invalidValueMessageBuilder = new StringBuilder();
			    if (value!=null){
			        String pwdTemplate;
			        for (int i=0,count=value.size(); i<count; i++){
			            pwdTemplate = value.get(i);
			            if (pwdTemplate.replaceAll("\\*","").isEmpty()){
			                if (invalidValueMessageBuilder.length()>0){
			                    invalidValueMessageBuilder.append(' ');
			                }
			                invalidValueMessageBuilder.append(String.format("Wrong template '%1$s' at index %2$s.",pwdTemplate,String.valueOf(i+1)));
			            }
			        }
			        final String invalidValueMessage = invalidValueMessageBuilder.toString();
			        if (invalidValueMessage!=null && !invalidValueMessage.isEmpty()){
			            return Client.Validators::ValidationResult.Factory.newInvalidResult(invalidValueMessage);
			        }
			    }
			    return Client.Validators::ValidationResult.ACCEPTABLE;
			}else{
			    return defaultValidationResult;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PwdBlackList getPwdBlackList(){return (PwdBlackList)getProperty(colH573HIYACZAATKHFKQK53NQOPM);}






















	/*Radix::System::System:General:Model:Methods-Methods*/

	/*Radix::System::System:General:Model:onCommand_ClearSensitiveTracedData-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_ClearSensitiveTracedData")
	private final  void onCommand_ClearSensitiveTracedData (org.radixware.ads.System.explorer.System.ClearTracedSensitiveData command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::System::System:General:Model:onCommand_ExportManifest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_ExportManifest")
	private final  void onCommand_ExportManifest (org.radixware.ads.System.explorer.System.ExportManifest command) {
		final ServicesXsd:SystemDocument xManifest;

		try {
		   xManifest = command.send();
		}catch(Exceptions::ServiceClientException flt){
		    showException(flt);
		    return;
		}catch(Exceptions::InterruptedException ex){
		    return;
		}
		final String fileName = com.trolltech.qt.gui.QFileDialog.getSaveFileName(
		    getEntityView() != null ? (Explorer.Qt.Types::QWidget)getEntityView() : Explorer.Env::Application.getMainWindow(),
		    "Select File to Save Manifest",
		    null,
		    new com.trolltech.qt.gui.QFileDialog.Filter("XML files (*.xml);;All files (*.*)"));

		if (fileName == null || fileName.isEmpty())
		    return; //user canceled
		try {    
		    java.io.File file = new java.io.File (fileName);
		    xManifest.save(file);
		} catch (Exceptions::IOException e){
		    showException(e);
		    return;
		}

	}

	/*Radix::System::System:General:Model:onCommand_ImportManifest-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_ImportManifest")
	private final  void onCommand_ImportManifest (org.radixware.ads.System.explorer.System.ImportManifest command) {
		final String fileName = com.trolltech.qt.gui.QFileDialog.getOpenFileName(
		    getEntityView() != null ?(Explorer.Qt.Types::QWidget)getEntityView() : Explorer.Env::Application.getMainWindow(),
		    "Select Manifest File",
		    null,
		    new com.trolltech.qt.gui.QFileDialog.Filter("XML files (*.xml);;All files (*.*)"));

		if (fileName == null || fileName.isEmpty())
		    return; //user canceled

		try {
		    final java.io.File file = new java.io.File (fileName);
		    final ServicesXsd:SystemDocument xManifest = ServicesXsd:SystemDocument.Factory.parse(file);
		    command.send(xManifest);
		}catch(Exceptions::ServiceClientException flt){
		    showException(flt);
		    return;
		}catch(Exceptions::IOException e){
		    showException(e);
		    return;
		}catch(Exceptions::XmlException e){
		    showException(e);
		    return;
		}catch(Exceptions::InterruptedException ex){
		    return;
		}

	}

	/*Radix::System::System:General:Model:onCommand_ShowUsedPorts-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_ShowUsedPorts")
	private final  void onCommand_ShowUsedPorts (org.radixware.ads.System.explorer.System.ShowUsedPorts command) {
		try{
		    Acs::CommandsXsd:StrValueDocument xOut = command.send();
		    StringBuilder sb = new StringBuilder();
		    sb.append("<html>");
		    sb.append(xOut.StrValue);
		    sb.append("</html>");
		    Explorer.Utils::WidgetUtils.showHtml(getEnvironment(), "Used Ports", sb.toString(), (Explorer.Qt.Types::QWidget)findNearestView());
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::System::System:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:afterRead")
	protected published  void afterRead () {
		boolean thisSys = afterReadCommon();

		if (!isInSelectorRowContext()) {
		    if (isEditorPageExists(idof[System:General:AADC])) {
		        getEditorPage(idof[System:General:AADC]).setVisible(thisSys && aadcMemberId.Value != null);
		    }
		    getEditorPage(idof[System:General:Eas]).setVisible(thisSys);
		    getEditorPage(idof[System:General:Trace]).setVisible(thisSys);
		    getEditorPage(idof[System:General:Storage]).setVisible(thisSys);
		    getEditorPage(idof[System:General:Performance]).setVisible(thisSys);
		    if (isEditorPageExists(idof[System:General:Development])) {
		        getEditorPage(idof[System:General:Development]).setVisible(thisSys);
		    }
		    if (isEditorPageExists(idof[System:General:Priorities])) {
		        getEditorPage(idof[System:General:Priorities]).setVisible(thisSys);
		    }

		    actualizeProfileOptions();

		    if (isEditorPageExists(idof[System:General:Services])) {
		        getEditorPage(idof[System:General:Services]).setVisible(!thisSys);
		    }

		    getCommand(idof[System:ShowAadcLocks]).setVisible(thisSys && aadcMemberId.Value != null);
		    getCommand(idof[System:MoveJobsFromOtherAadcMember]).setVisible(thisSys && aadcMemberId.Value != null);
		}

		checkEnabledDualControl();

	}

	/*Radix::System::System:General:Model:actualizeProfileOptions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:actualizeProfileOptions")
	private final  void actualizeProfileOptions () {
		if (getEditorPage(idof[System:General:Performance]).isVisible()){
		    profiledPrefixes.setVisible(profileMode.Value == Profiler::ProfileMode:SPECIFIED);
		    profilePeriodSec.setVisible(profileMode.Value != Profiler::ProfileMode:OFF);
		}
	}

	/*Radix::System::System:General:Model:afterReadCommon-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:afterReadCommon")
	private final  boolean afterReadCommon () {
		boolean thisSys = (id.getValue() == 1);
		askUserPwdAfterInactivity.setVisible(thisSys);
		blockUserInvalidLogonCnt.setVisible(thisSys);
		blockUserInvalidLogonMins.setVisible(thisSys);
		defaultAuditScheme.setVisible(thisSys);
		arteLanguage.setVisible(thisSys);
		arteCountry.setVisible(thisSys);
		return thisSys;
	}

	/*Radix::System::System:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		final Explorer.Meta::EnumDef ldapX500AttrTypeEnumDef = Environment.getDefManager().getEnumPresentationDef(idof[LdapX500AttrType]);
		final java.util.List<Object> predefinedValues = new java.util.LinkedList<Object>();
		for (Explorer.Meta::EnumDef.Item attrType: ldapX500AttrTypeEnumDef.Items){
		    predefinedValues.add(attrType.getValue());
		}
		certAttrForUserLogin.PredefinedValues = predefinedValues;

		easKrbPrincName.EditMask.setNoValueStr(calcDefaultEasPN());
		if (getDefinition().isCommandDefExistsById(idof[System:ExportPwdBlackList])){
		    pwdBlackList.addDependent(getCommand(idof[System:ExportPwdBlackList]));
		}
		if (getDefinition().isCommandDefExistsById(idof[System:ImportPwdBlackList])){
		    pwdBlackList.addDependent(getCommand(idof[System:ImportPwdBlackList]));
		}
	}

	/*Radix::System::System:General:Model:calcDefaultEasPN-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:calcDefaultEasPN")
	private final  Str calcDefaultEasPN () {
		final Str spn = "HTTP/eas.radixware.org";
		final javax.security.auth.kerberos.KerberosPrincipal principal;
		try {
		    principal = 
		        new javax.security.auth.kerberos.KerberosPrincipal(spn, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		} catch (IllegalArgumentException ex) {
		    return spn;
		}
		return principal.getName();
	}

	/*Radix::System::System:General:Model:getPropertyValueState-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:getPropertyValueState")
	public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult getPropertyValueState (org.radixware.kernel.common.types.Id propertyId) {
		if (propertyId == idof[System:easKrbPrincName] && easKrbPrincName.Value!=null){
		    try{
		        new javax.security.auth.kerberos.KerberosPrincipal(easKrbPrincName.Value, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		    }catch(IllegalArgumentException exception){
		        return Client.Validators::ValidationResult.Factory.newInvalidResult(Client.Validators::InvalidValueReason.WRONG_FORMAT);
		    }
		}
		return super.getPropertyValueState(propertyId);
	}

	/*Radix::System::System:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (easKrbPrincName.Value!=null){
		    final javax.security.auth.kerberos.KerberosPrincipal krbPrinc;
		    try{
		        krbPrinc = new javax.security.auth.kerberos.KerberosPrincipal(easKrbPrincName.Value, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		    }catch(IllegalArgumentException exception){
		        throw new InvalidPropertyValueException(this,easKrbPrincName.Definition,Client.Validators::InvalidValueReason.WRONG_FORMAT);
		    }
		    for (char letter: krbPrinc.getRealm().toCharArray()){
		        if (Character.isLowerCase(letter)){
		            if (getEnvironment().messageConfirmation("Confirm Usage of These Options","Kerberos realm name is usually in uppercase....")){
		                break;
		            }else{
		                easKrbPrincName.setFocused();
		                return false;
		            }
		        }
		    }
		}
		return super.beforeUpdate();
	}

	/*Radix::System::System:General:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if(propertyId == idof[System:General:General:Model:userExtDevLanguages]){
		    Arr<Common::Language> langs = userExtDevLanguages.Value;
		    if(langs == null || langs.isEmpty()){
		        
		        java.util.List<Common::Language> defaultLanguages = new org.radixware.kernel.common.client.env.UserDefinitionSettings(getEnvironment()).getDefaultLanguageSet();
		        
		        StringBuilder title = new StringBuilder();
		        title.append(defaultDisplayString).append(" (");
		        boolean first=  true;
		        for(Common::Language l : defaultLanguages){
		            if(first)
		                first = false;
		            else
		                title.append(", ");
		            title.append(l.Name);
		        }
		        title.append(")");
		        return title.toString();
		    }else{
		        return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
		    }
		}else if (propertyId == idof[System:pwdBlackList]){
		    return pwdBlackList.Value==null || pwdBlackList.Value.isEmpty() ? defaultDisplayString : "<defined>";
		}else
		   return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::System:General:Model:checkEnabledDualControl-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:checkEnabledDualControl")
	private final  void checkEnabledDualControl () {
		boolean canEditDualControlWhenAssigningRoles = canEditDualControlForAssignRole.Value == true;
		this.dualControlForAssignRole.setEnabled(canEditDualControlWhenAssigningRoles);
	}

	/*Radix::System::System:General:Model:onCommand_ShowAADCLocks-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_ShowAADCLocks")
	private final  void onCommand_ShowAADCLocks (org.radixware.ads.System.explorer.System.ShowAadcLocks command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::System::System:General:Model:onCommand_MoveJobsFromOtherAadcMember-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_MoveJobsFromOtherAadcMember")
	private final  void onCommand_MoveJobsFromOtherAadcMember (org.radixware.ads.System.explorer.System.MoveJobsFromOtherAadcMember command) {
		if (getEnvironment().messageConfirmation("Move Jobs From Other AADC Member", String.format("Attention!<br>...", aadcMemberId.Value == 1 ? 2 : 1))) {
		    try {
		        command.send();
		    } catch (Exceptions::InterruptedException e) {
		        showException(e);
		    } catch (Exceptions::ServiceClientException e) {
		        showException(e);
		    }
		}
	}
	public final class ExportManifest extends org.radixware.ads.System.explorer.System.ExportManifest{
		protected ExportManifest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ExportManifest( this );
		}

	}

	public final class MoveJobsFromOtherAadcMember extends org.radixware.ads.System.explorer.System.MoveJobsFromOtherAadcMember{
		protected MoveJobsFromOtherAadcMember(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveJobsFromOtherAadcMember( this );
		}

	}

	public final class ShowUsedPorts extends org.radixware.ads.System.explorer.System.ShowUsedPorts{
		protected ShowUsedPorts(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ShowUsedPorts( this );
		}

	}

	public final class ImportManifest extends org.radixware.ads.System.explorer.System.ImportManifest{
		protected ImportManifest(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ImportManifest( this );
		}

	}

	public final class ClearTracedSensitiveData extends org.radixware.ads.System.explorer.System.ClearTracedSensitiveData{
		protected ClearTracedSensitiveData(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ClearTracedSensitiveData( this );
		}

	}

	public final class ShowAadcLocks extends org.radixware.ads.System.explorer.System.ShowAadcLocks{
		protected ShowAadcLocks(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ShowAadcLocks( this );
		}

	}



























}

/* Radix::System::System:General:Model - Desktop Meta*/

/*Radix::System::System:General:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemRF6WOIPPZBD3TKO4ECANZHYFAA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::System:General:Model:Properties-Properties*/
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

/* Radix::System::System:General:Model - Web Executable*/

/*Radix::System::System:General:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model")
public class General:Model  extends org.radixware.ads.System.web.System.System_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::System:General:Model:Nested classes-Nested Classes*/

	/*Radix::System::System:General:Model:ExportPwdBlackList-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList")
	public class ExportPwdBlackList  extends org.radixware.kernel.common.client.models.items.Command  {



		/*Radix::System::System:General:Model:ExportPwdBlackList:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:ExportPwdBlackList:Properties-Properties*/

		/*Radix::System::System:General:Model:ExportPwdBlackList:Methods-Methods*/

		/*Radix::System::System:General:Model:ExportPwdBlackList:ExportPwdBlackList-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList:ExportPwdBlackList")
		public  ExportPwdBlackList (org.radixware.kernel.common.client.models.Model model, org.radixware.kernel.common.client.meta.RadCommandDef command) {
			super(model,command);
		}

		/*Radix::System::System:General:Model:ExportPwdBlackList:isEnabledForProperty-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ExportPwdBlackList:isEnabledForProperty")
		public  boolean isEnabledForProperty (org.radixware.kernel.common.types.Id propertyId) {
			return pwdBlackList.Value!=null && !pwdBlackList.Value.isEmpty();
		}


	}

	/*Radix::System::System:General:Model:ImportPwdBlackList-Common Dynamic Class*/


	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList")
	public class ImportPwdBlackList  extends org.radixware.kernel.common.client.models.items.Command  {



		/*Radix::System::System:General:Model:ImportPwdBlackList:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:ImportPwdBlackList:Properties-Properties*/

		/*Radix::System::System:General:Model:ImportPwdBlackList:Methods-Methods*/

		/*Radix::System::System:General:Model:ImportPwdBlackList:ImportPwdBlackList-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList:ImportPwdBlackList")
		public  ImportPwdBlackList (org.radixware.kernel.common.client.models.Model model, org.radixware.kernel.common.client.meta.RadCommandDef command) {
			super(model,command);
		}

		/*Radix::System::System:General:Model:ImportPwdBlackList:isVisibleForProperty-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:ImportPwdBlackList:isVisibleForProperty")
		public  boolean isVisibleForProperty (org.radixware.kernel.common.types.Id propertyId) {
			return !pwdBlackList.isReadonly();
		}


	}

	/*Radix::System::System:General:Model:Properties-Properties*/

	/*Radix::System::System:General:Model:enableSensitiveTrace-Presentation Property*/




	public class EnableSensitiveTrace extends org.radixware.ads.System.web.System.colLRLGIOF3DVB2XEFQXLCJLPUYTU{
		public EnableSensitiveTrace(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:enableSensitiveTrace")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:enableSensitiveTrace")
		public   void setValue(Bool val) {

			internal[enableSensitiveTrace] = val;
			Environment.messageWarning(
			    val.booleanValue() ? "You are about to grant the global permission to save sensitive data to Event Log...."
			        : "Disabling of this option will not cause deletion of sensitive data from Event Log...."
			);

		}
	}
	public EnableSensitiveTrace getEnableSensitiveTrace(){return (EnableSensitiveTrace)getProperty(colLRLGIOF3DVB2XEFQXLCJLPUYTU);}

	/*Radix::System::System:General:Model:userExtDevLanguages-Presentation Property*/




	public class UserExtDevLanguages extends org.radixware.ads.System.web.System.col2JACLKO3UNAMTIFUAMZ43AXNB4{
		public UserExtDevLanguages(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Common.common.Language.Arr> getValClass(){
			return org.radixware.ads.Common.common.Language.Arr.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Common.common.Language.Arr dummy = x == null ? null : (x instanceof org.radixware.ads.Common.common.Language.Arr ? (org.radixware.ads.Common.common.Language.Arr)x : new org.radixware.ads.Common.common.Language.Arr((org.radixware.kernel.common.types.ArrStr)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.ARR_STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:userExtDevLanguages")
		public published  org.radixware.ads.Common.common.Language.Arr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:userExtDevLanguages")
		public published   void setValue(org.radixware.ads.Common.common.Language.Arr val) {
			Value = val;
		}
	}
	public UserExtDevLanguages getUserExtDevLanguages(){return (UserExtDevLanguages)getProperty(col2JACLKO3UNAMTIFUAMZ43AXNB4);}

	/*Radix::System::System:General:Model:useOraImplStmtCache-Presentation Property*/




	public class UseOraImplStmtCache extends org.radixware.ads.System.web.System.colKQKCWI72DVAO7OFVSNBZ7XN2WQ{
		public UseOraImplStmtCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"))) {
				this.addDependent(aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache(){return (UseOraImplStmtCache)getProperty(colKQKCWI72DVAO7OFVSNBZ7XN2WQ);}

	/*Radix::System::System:General:Model:oraImplStmtCacheSize-Presentation Property*/




	public class OraImplStmtCacheSize extends org.radixware.ads.System.web.System.colJN25VAA7SVD5FKDTO47ERVQIDQ{
		public OraImplStmtCacheSize(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::System:General:Model:oraImplStmtCacheSize:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:Properties-Properties*/

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:Methods-Methods*/

		/*Radix::System::System:General:Model:oraImplStmtCacheSize:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:oraImplStmtCacheSize:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useOraImplStmtCache.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize(){return (OraImplStmtCacheSize)getProperty(colJN25VAA7SVD5FKDTO47ERVQIDQ);}

	/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase-Presentation Property*/




	public class PwdMustContainACharsInMixedCase extends org.radixware.ads.System.web.System.colEFLQTDCHFZCTFNM77TYMJH35JY{
		public PwdMustContainACharsInMixedCase(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Bool dummy = ((Bool)x);
			setValue(dummy);
		}

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:Properties-Properties*/

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:Methods-Methods*/

		/*Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:isEnabled-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainACharsInMixedCase:isEnabled")
		public published  boolean isEnabled () {
			return super.isEnabled() && pwdMustContainAChars.Value==true;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainACharsInMixedCase")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainACharsInMixedCase")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PwdMustContainACharsInMixedCase getPwdMustContainACharsInMixedCase(){return (PwdMustContainACharsInMixedCase)getProperty(colEFLQTDCHFZCTFNM77TYMJH35JY);}

	/*Radix::System::System:General:Model:pwdMustContainAChars-Presentation Property*/




	public class PwdMustContainAChars extends org.radixware.ads.System.web.System.colTY6XN5X66FD2FIC7ZQVWKRV7YQ{
		public PwdMustContainAChars(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY"))) {
				this.addDependent(aemRF6WOIPPZBD3TKO4ECANZHYFAA.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEFLQTDCHFZCTFNM77TYMJH35JY")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainAChars")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdMustContainAChars")
		public   void setValue(Bool val) {

			internal[pwdMustContainAChars] = val;
			if (val==false){
			    pwdMustContainACharsInMixedCase.setValue(false);
			}
		}
	}
	public PwdMustContainAChars getPwdMustContainAChars(){return (PwdMustContainAChars)getProperty(colTY6XN5X66FD2FIC7ZQVWKRV7YQ);}

	/*Radix::System::System:General:Model:pwdBlackList-Presentation Property*/




	public class PwdBlackList extends org.radixware.ads.System.web.System.colH573HIYACZAATKHFKQK53NQOPM{
		public PwdBlackList(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.types.ArrStr dummy = ((org.radixware.kernel.common.types.ArrStr)x);
			setValue(dummy);
		}

		/*Radix::System::System:General:Model:pwdBlackList:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::System:General:Model:pwdBlackList:Nested classes-Nested Classes*/

		/*Radix::System::System:General:Model:pwdBlackList:Properties-Properties*/

		/*Radix::System::System:General:Model:pwdBlackList:initialDirSettingName-Dynamic Property*/

		protected Str initialDirSettingName=null;











		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:initialDirSettingName")
		private final  Str getInitialDirSettingName() {

			return getConfigStoreGroupName()+"/"+pwdBlackList.getId()+"/initialDir";
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:initialDirSettingName")
		private final   void setInitialDirSettingName(Str val) {
			initialDirSettingName = val;
		}

		/*Radix::System::System:General:Model:pwdBlackList:Methods-Methods*/

		/*Radix::System::System:General:Model:pwdBlackList:getInitialDir-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:getInitialDir")
		private final  Str getInitialDir () {
			final String initialDir = getEnvironment().getConfigStore().readString(initialDirSettingName, System.getProperty("user.home"));
			final java.nio.file.Path dirPath = java.nio.file.Paths.get(initialDir);
			return dirPath == null || !dirPath.toFile().isDirectory() ? System.getProperty("user.home") : initialDir;
		}

		/*Radix::System::System:General:Model:pwdBlackList:setInitialDir-User Method*/

		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:setInitialDir")
		private final  void setInitialDir (Str path) {
			getEnvironment().getConfigStore().writeString(initialDirSettingName,path);
		}

		/*Radix::System::System:General:Model:pwdBlackList:validateValue-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList:validateValue")
		public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult validateValue () {
			final Client.Validators::ValidationResult defaultValidationResult = super.validateValue();
			if (super.validateValue()==Client.Validators::ValidationResult.ACCEPTABLE){
			    final ArrStr value = getValue();
			    final StringBuilder invalidValueMessageBuilder = new StringBuilder();
			    if (value!=null){
			        String pwdTemplate;
			        for (int i=0,count=value.size(); i<count; i++){
			            pwdTemplate = value.get(i);
			            if (pwdTemplate.replaceAll("\\*","").isEmpty()){
			                if (invalidValueMessageBuilder.length()>0){
			                    invalidValueMessageBuilder.append(' ');
			                }
			                invalidValueMessageBuilder.append(String.format("Wrong template '%1$s' at index %2$s.",pwdTemplate,String.valueOf(i+1)));
			            }
			        }
			        final String invalidValueMessage = invalidValueMessageBuilder.toString();
			        if (invalidValueMessage!=null && !invalidValueMessage.isEmpty()){
			            return Client.Validators::ValidationResult.Factory.newInvalidResult(invalidValueMessage);
			        }
			    }
			    return Client.Validators::ValidationResult.ACCEPTABLE;
			}else{
			    return defaultValidationResult;
			}
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:pwdBlackList")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public PwdBlackList getPwdBlackList(){return (PwdBlackList)getProperty(colH573HIYACZAATKHFKQK53NQOPM);}




















	/*Radix::System::System:General:Model:Methods-Methods*/

	/*Radix::System::System:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:afterRead")
	protected published  void afterRead () {
		afterReadCommon();
		checkEnabledDualControl();
	}

	/*Radix::System::System:General:Model:afterReadCommon-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:afterReadCommon")
	private final  boolean afterReadCommon () {
		boolean thisSys = (id.getValue() == 1);
		askUserPwdAfterInactivity.setVisible(thisSys);
		blockUserInvalidLogonCnt.setVisible(thisSys);
		blockUserInvalidLogonMins.setVisible(thisSys);
		defaultAuditScheme.setVisible(thisSys);
		arteLanguage.setVisible(thisSys);
		arteCountry.setVisible(thisSys);
		return thisSys;
	}

	/*Radix::System::System:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		super.beforeOpenView();
		final Explorer.Meta::EnumDef ldapX500AttrTypeEnumDef = Environment.getDefManager().getEnumPresentationDef(idof[LdapX500AttrType]);
		final java.util.List<Object> predefinedValues = new java.util.LinkedList<Object>();
		for (Explorer.Meta::EnumDef.Item attrType: ldapX500AttrTypeEnumDef.Items){
		    predefinedValues.add(attrType.getValue());
		}
		certAttrForUserLogin.PredefinedValues = predefinedValues;

		easKrbPrincName.EditMask.setNoValueStr(calcDefaultEasPN());
		if (getDefinition().isCommandDefExistsById(idof[System:ExportPwdBlackList])){
		    pwdBlackList.addDependent(getCommand(idof[System:ExportPwdBlackList]));
		}
		if (getDefinition().isCommandDefExistsById(idof[System:ImportPwdBlackList])){
		    pwdBlackList.addDependent(getCommand(idof[System:ImportPwdBlackList]));
		}
	}

	/*Radix::System::System:General:Model:calcDefaultEasPN-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:calcDefaultEasPN")
	private final  Str calcDefaultEasPN () {
		final Str spn = "HTTP/eas.radixware.org";
		final javax.security.auth.kerberos.KerberosPrincipal principal;
		try {
		    principal = 
		        new javax.security.auth.kerberos.KerberosPrincipal(spn, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		} catch (IllegalArgumentException ex) {
		    return spn;
		}
		return principal.getName();
	}

	/*Radix::System::System:General:Model:getPropertyValueState-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:getPropertyValueState")
	public published  org.radixware.kernel.common.client.meta.mask.validators.ValidationResult getPropertyValueState (org.radixware.kernel.common.types.Id propertyId) {
		if (propertyId == idof[System:easKrbPrincName] && easKrbPrincName.Value!=null){
		    try{
		        new javax.security.auth.kerberos.KerberosPrincipal(easKrbPrincName.Value, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		    }catch(IllegalArgumentException exception){
		        return Client.Validators::ValidationResult.Factory.newInvalidResult(Client.Validators::InvalidValueReason.WRONG_FORMAT);
		    }
		}
		return super.getPropertyValueState(propertyId);
	}

	/*Radix::System::System:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (easKrbPrincName.Value!=null){
		    final javax.security.auth.kerberos.KerberosPrincipal krbPrinc;
		    try{
		        krbPrinc = new javax.security.auth.kerberos.KerberosPrincipal(easKrbPrincName.Value, javax.security.auth.kerberos.KerberosPrincipal.KRB_NT_SRV_INST);
		    }catch(IllegalArgumentException exception){
		        throw new InvalidPropertyValueException(this,easKrbPrincName.Definition,Client.Validators::InvalidValueReason.WRONG_FORMAT);
		    }
		    for (char letter: krbPrinc.getRealm().toCharArray()){
		        if (Character.isLowerCase(letter)){
		            if (getEnvironment().messageConfirmation("Confirm Usage of These Options","Kerberos realm name is usually in uppercase....")){
		                break;
		            }else{
		                easKrbPrincName.setFocused();
		                return false;
		            }
		        }
		    }
		}
		return super.beforeUpdate();
	}

	/*Radix::System::System:General:Model:getDisplayString-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:getDisplayString")
	public published  Str getDisplayString (org.radixware.kernel.common.types.Id propertyId, java.lang.Object propertyValue, Str defaultDisplayString, boolean isInherited) {
		if(propertyId == idof[System:General:General:Model:userExtDevLanguages]){
		    Arr<Common::Language> langs = userExtDevLanguages.Value;
		    if(langs == null || langs.isEmpty()){
		        
		        java.util.List<Common::Language> defaultLanguages = new org.radixware.kernel.common.client.env.UserDefinitionSettings(getEnvironment()).getDefaultLanguageSet();
		        
		        StringBuilder title = new StringBuilder();
		        title.append(defaultDisplayString).append(" (");
		        boolean first=  true;
		        for(Common::Language l : defaultLanguages){
		            if(first)
		                first = false;
		            else
		                title.append(", ");
		            title.append(l.Name);
		        }
		        title.append(")");
		        return title.toString();
		    }else{
		        return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
		    }
		}else if (propertyId == idof[System:pwdBlackList]){
		    return pwdBlackList.Value==null || pwdBlackList.Value.isEmpty() ? defaultDisplayString : "<defined>";
		}else
		   return super.getDisplayString(propertyId, propertyValue, defaultDisplayString, isInherited);
	}

	/*Radix::System::System:General:Model:checkEnabledDualControl-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:checkEnabledDualControl")
	private final  void checkEnabledDualControl () {
		boolean canEditDualControlWhenAssigningRoles = canEditDualControlForAssignRole.Value == true;
		this.dualControlForAssignRole.setEnabled(canEditDualControlWhenAssigningRoles);
	}

	/*Radix::System::System:General:Model:onCommand_ShowAADCLocks-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_ShowAADCLocks")
	private final  void onCommand_ShowAADCLocks (org.radixware.ads.System.web.System.ShowAadcLocks command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::System::System:General:Model:onCommand_MoveJobsFromOtherAadcMember-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::System:General:Model:onCommand_MoveJobsFromOtherAadcMember")
	private final  void onCommand_MoveJobsFromOtherAadcMember (org.radixware.ads.System.web.System.MoveJobsFromOtherAadcMember command) {
		if (getEnvironment().messageConfirmation("Move Jobs From Other AADC Member", String.format("Attention!<br>...", aadcMemberId.Value == 1 ? 2 : 1))) {
		    try {
		        command.send();
		    } catch (Exceptions::InterruptedException e) {
		        showException(e);
		    } catch (Exceptions::ServiceClientException e) {
		        showException(e);
		    }
		}
	}
	public final class MoveJobsFromOtherAadcMember extends org.radixware.ads.System.web.System.MoveJobsFromOtherAadcMember{
		protected MoveJobsFromOtherAadcMember(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_MoveJobsFromOtherAadcMember( this );
		}

	}

	public final class ShowAadcLocks extends org.radixware.ads.System.web.System.ShowAadcLocks{
		protected ShowAadcLocks(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_ShowAadcLocks( this );
		}

	}



















}

/* Radix::System::System:General:Model - Web Meta*/

/*Radix::System::System:General:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemRF6WOIPPZBD3TKO4ECANZHYFAA"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::System:General:Model:Properties-Properties*/
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

/* Radix::System::System:General - Desktop Meta*/

/*Radix::System::System:General-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.System.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::System:General - Web Meta*/

/*Radix::System::System:General-Selector Presentation*/

package org.radixware.ads.System.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprJKS6FYKTAJDN5FSYFHJTHWIPBY"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		null,
		new org.radixware.kernel.common.types.Id[]{},
		false,
		false,
		null,
		32,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRF6WOIPPZBD3TKO4ECANZHYFAA")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27EXNSNZRHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTCOQ4HOZRHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJUUBIGQRJFVBLOGHVZGSLVOSU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd526UQYGW4BFVJO2TUDBCBEUPY4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.System.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::System - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class System - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
		loadStrings2();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2327TTOL5RGFLASMVLBMX4R7II"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Miscellaneous");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разное");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls247QGK7RNJBAJF6IVY4DSRQBKQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User lock period (min)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время блокировки пользователей (мин)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls24HMJTW7TRGSDH342B75DXDFQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to unset \'Dual control for roles assignment\'.\nFound unaccepted user group rights: \'%s\'.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не могу убрать значение свойства \'Двойной контроль при назначении ролей\'.\nОбнаружены неакцептованные права группы пользователей: \'%s\'.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls25VDK3AYVZE6NKRG57S7FTU47E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not to register>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не регистрировать>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls26INKFHONJBX5JY6UAZQ6V2J5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error writing to the file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при записи в файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AAYTDKVIZDGTGNFFVDWOREIQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2OBK2DO6PJGLNA2A4QQVIBXDZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Terminal resource usage error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при обращении к ресурсу терминала");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2TLVPDMLA5ALLED3RE2ZTIBHEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Release hung lock for tables");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Снимать зависшие блокировки для таблиц");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls342YRUHE2NHA7E7UB4MM53BU4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Oracle Implicit Statement Cache size");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Размер Oracle Implicit Statement Cache");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BOWQW5VAVCERGJRCXO6K2VE4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC member in test mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тестируемый узел AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3D3IFOTV25BLPA6U7AMANTBMUQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum size of report result cache (Kb)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальный размер кеша результатов выполнения отчета (Кб)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4524VRNEZRBHHDI535GADAZYJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Kerberos realm name is usually in uppercase.\nContinue?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обычно в имени рабочей области Kerberos (realm) используется только верхний регистр.\nХотите продолжить?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47AIGAD72NE4PEUBUUQUFECCP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<do not control>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не контролировать>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4KLS7EXAAVC6JIXJWPLZHKHGUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error registering SCP to SAP association. SAP not found: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка регистрации ассоциации профиля клиента сервисов и точки доступа:  ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4NCJMO42EBAR5JZTE56V4ENTEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profiling mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Режим профилирования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4VRVICQMXBCRPBJ7KV4WCWLT3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inactive user session retention period (min)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сохранение неактивных пользовательских сессий (мин)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WDOS74FBJCRLPOXMAEHLOUHX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"You are about to grant the global permission to save sensitive data to Event Log.\n\nFor security reasons, it is strongly recommended to switch this option off as soon as tracing of such data becomes unnecessary. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вы собираетесь дать глобальное разрешение на сохранение чувствительных данных в Журнале событий.\n\nВ целях безопасности рекомендуется  отозвать данное разрешение, как только необходимость в трассировке таких данных исчезнет.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4X3CSPXJWBCJ7I65I2LOS22O4Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be not less than %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция имела значение не менее %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4ZR2EMK6FBAPRBQMTXURYB6RKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be not less than %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция имела значение не менее %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls53LFQEB6XZHL7F77UFDW3H2ICM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE file trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки ARTE в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls54UK5YQD3BA4JO42VTY6NB7SNM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for audit log - long (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения журнала аудита - долгий (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5F5BJTJR7JFIVH3DCKB3GS755Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QCIURENNRFBZMOBOBUJYCAVNI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<CN>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<CN>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VZI6BUPLNG5LMIXHOSQEND7UY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for audit log - longest (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения журнала аудита - самый долгий (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X3YPJQ2UZF5ZDV2UHCSFZD2WA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6C7JDDBY3FDNXGSYAAMRHHELJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE GUI trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки ARTE на экран");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6F2K2XUC6RAURKEX6LSZUUQJPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error registering the SCP to SAP association. SAP not found: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка регистрации ассоциации профиля клиента сервисов и точки доступа. Точка доступа не найдена:  ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6QA3VDKQ7JFF7HVMYSMMM34YDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request password after inactivity period");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запрашивать пароль после бездействия пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls73T4HKGMGJDSJEABRTO3Z2ZH3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not limited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ABEVLLODBGIRAKIL7IJWE6PPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Show Used Ports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Показать используемые порты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7NREAYARLVH6NL45SMR3W2QBRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Manifest import is not permitted for internal system (#1).");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт Манифеста для текущей системы (#1) не поддерживается. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7QLF2B6K4ZCWFDFDAUT7USPK2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to create file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удается создать файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TWQUSSEPBC5DHGTHZ4LPPW2QM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Manifest imported successfully");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Манифест импортирован успешно");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAQWRODWFNZBO7GKSWOTSVLGLT4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be true");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция была задана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsARGHQYMCU5GIFK626URI5F3XVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE DB trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки ARTE в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBATUBSKH4JCSHKW27MQBKMX7IE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be true");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция была задана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBG6OOZ6QAZHSNH2FY3IUVZ27HA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be true");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция была задана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBM3EB2SWKJBURA7MZCITYAZ63M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBMVVID4SJVHSZN77POWURCNBUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Monitoring");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Мониторинг");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBSF6NYEIRFAIZEUZ7Q7NUSEVQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Default audit scheme ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. схемы аудита по умолчанию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBTS7Q6ACKBHFTP43WZL4EJ4WKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wrong Format of EAS Principal Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неправильный формат имени сервиса EAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBUW4ETAFQZDXNLIC6HQYJFL24E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Development");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Разработка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCQBDFV4ZBZEBTLKTWLNN2ZNOOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<default language set>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<исходный список языков>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCZ4MLVRTXJGQZH6VWZ2AMWMOQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for instance threads state history (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения истории состояния потоков инстанций (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD5WBZFOIQFERPMHIXFVFDRD5BQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to unset \'Dual control for roles assignment\'.\nFound unaccepted user rights: \'%s\'.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не могу убрать значение свойства \'Двойной контроль при назначении ролей\'.\nОбнаружены неакцептованные права пользователя: \'%s\'.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD7T62RWSFNGNLPSAV6Q3N3BH4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Temporary password validity period (hours)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок действия временного пароля (часы)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDAARJHO4ZZFBTNZZAWF2EN2L64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<do not control>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не контролировать>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDHWA65432RB4BCZEECT6PQWKO4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"New password must not match last (number)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Новый пароль не должен повторять последние (кол-во)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDJJGIZTWWBBVLEXZ2SFHMRU62A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Система");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE3C54IPQI5BEPBNBZ2SGAST36Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE5DWIFNUTFD3XJS6BDYIBAMCUU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profiling period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период профилирования (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEASZSL2HONH6VBQX44IGC6FE3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User definition development languages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Языки разработки пользовательских дефиниций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsECVKEGYGUFFWZIA5FCUVX5MGGA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDFA3GVWRNBA3P3JJLDYR3A5KA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password validity period (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок действия пароля (дней)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF2L6IOLO2NCOXN5NIWIVQXQUXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for event (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения событий (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFG4HE3KQLNC65NOTWPHD73QEG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFGPYEHIOSVH33A5H45NRTZ2ABQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum password length");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальная длина пароля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIF5YA2YSJC4RM27QEAWWONNDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"List of forbidden passwords");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Список запрещенных паролей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYLKTKYP7JB7BOA5HQAXTQWAOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password must contain numeric characters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль должен содержать цифры");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGARWWVW27JF63JIA5BEWQCZGSE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select file to store list of forbidden passwords");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите файл для сохранения списка запрещенных паролей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGRUJGYRWX5FCTIJJ3AQXMA6GGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for profiler log (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения данных профилирования (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGYJSOLZHLFBH7BDDHW2FBY653Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Systems");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH6MYEBR27BCYLKIESD3AI6T2QM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Default language");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Язык по умолчанию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH7ZA7XDFWREHHCPOIKLNTLTWYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used Ports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использованные порты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHX2K65RW2NDJJEDVUAIQJXXFBI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SCP registration error: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при регистрации профиля клиента сервисов: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZXEFIECNFAHFGPTW65AMSVFEY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be true");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция была задана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3XUDDYEGFBR5LYHSAHUSSH5SM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enable sensitive data tracing");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включить трассировку чувствительных данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI6X6BH3GNFBHLPHHZQUH7UHT74"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be true");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция была задана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIB2HBJV2XVAWHDHTLZMFOD77AM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for audit log - shortest (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения журнала аудита - самый короткий (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIGDACHB74VDKFLE6IOU6WRI7XM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Priorities");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Приоритеты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIIWSU6H5LFBHLB7HCHADCJWBRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Хранение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINFDZDLHYNAK3MHZ7EOKQHGPDQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use Oracle Implicit Statement Cache");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать Oracle Implicit Statement Cache");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIVI6M5A3URCVVJNDVCOQ34LPCI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"%d jobs has been moved");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"%d заданий перенесено");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ3NXYNRWNBCCDPZJIDZUYQD624"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password must contain alphabetic characters of both cases");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль должен содержать буквы разного регистра");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJGWU2LKTHJH6XCDZXEJHTWNKCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Система");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJK2IYLT2BRGNLGODL4SY4T333U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Affinity timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Таймаут привязки (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4FFZPYU5RAAZE6LLK5366JINI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результаты импорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK555YVAJ5ZHLHJRSCIWLZDRX3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to unset \'Dual control for roles assignment\'.\nFound unaccepted (user - user group) rights: \'%s\'.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не могу убрать значение свойства \'Двойной контроль при назначении ролей\'.\nОбнаружены неакцептованные права (пользователя - группы пользователей): \'%s\'.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5EAUOHF7RD5HHINTPBVRZN534"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be not less than %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция имела значение не менее %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5UEC7OUZRE4LB74OWHUE3OI6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Result");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Результаты импорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKNUKLCRPXJBPRCTEQPHELXKCSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System name in the Manifest duplicates the name of the existing system. The current system name has been saved. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя системы в манифесте повторяет имя существующей системы. Прежнее имя системы сохранено. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKUZ5GVBSCRFYTO4SR26I2RCJRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error writing to the file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при записи в файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKVGUHJCVPNGLBDGVYOU3TD6VVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Jobs from Other AADC Member");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенос заданий с другого узла AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWRBYFLI3FGCHPGBTD7HVFQW3M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Attention!<br>\n1. Please make sure that all application server instances belonging to AADC member %1$s are stopped.<br>\n2. Do not start application server instances belonging to AADC member %1$s until changes<br>\nin job\'s assignment to AADC member will not be replicated to member %1$s.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL4UDEKHQRFCWVCIXLEXGYKVCOU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for notification log record (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения записей журнала уведомлений (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDEOJYAVPBDGNNGKN27HNRUO3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to create file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удается создать файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLAQJ45PM5E3NL2ZFUJUTC2ILY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period for instance threads state registration (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период регистрации состояния потоков инстанций (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLW6NYEH5PFFWDEBOIFT2V7SXWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import Manifest");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт манифеста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZA6SR5BPZHMDOCSPCT6KKQRY4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC member ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. узла AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM2XPCRZZINEQDIBUOB2D6SIMPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Errors occurred while importing Manifest:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Во время импорта Манифеста возникли ошибки:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMC52VKQPZRFSNFAOPS2XHNQT3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not to register>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не регистрировать>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCDQGQBAWREB7DDJBBYE53D4RI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Performance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Производительность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCS55454FFFBZPOE4XHR5MF7MI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be true");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция была задана");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsME7RK3ZC55EOZFPB67FYCIYXCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password must contain alphabetic characters");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль должен содержать буквы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKMHYO5UGZB4NP56UOX4SEL3XQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for audit log - short (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения журнала аудита - короткий (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMVVHYBY32BFZBKRTZKFMJI5P34"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML files (*.xml);;All files (*.*)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файлы XML (*.xml);;Все файлы (*.*)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMWE7DJJ5GZFRPLL4I2JSFISSMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"The following system instances are running on AADC member %d: %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанции системы # %s запущены на узле AADC %d");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNAP4E5TJ7RC7ZJFNLQMYO73X3E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period (s) within which the statistics are accumulated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период (c), в течение которого статистика накапливается");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNNG5EDRADNFLFMHRYHRYNIVZRQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum period of EAS session activity (min)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное время активности EAS сессии (мин)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNOOXLWEZF5AHDHK7KCQK25G3WU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	@SuppressWarnings("unused")
	private static void loadStrings2(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be not less than %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция имела значение не менее %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNTLFG6GJ3FE47MIMANGPJFRLME"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Wrong template \'%1$s\' at index %2$s.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Неправильный шаблон \'%1$s\' по индексу %2$s.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO6A3WZBXGJECZEYXBZ7E7X4DCQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Write context to log file");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записывать контекст в файловую трассу");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOIWG2WZFJZHQPCBZFVLUHFFH3U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' modified ARTE file trace profile. Old value: \'%2\'. New value: \'%3\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \"%1\" изменил профиль трассировки ARTE в файл. Старое значение: \"%2\". Новое значение: \"%3\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOKOQWUU5XVDKZDSF66YHCQCO2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Manifest import is not permitted for new system.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импорт манифеста для новых систем не поддерживается.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsON3VUQCJSJEH3GK2DSLTCQG5EE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Services Manifest");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать манифест сервисов");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4HPW3JIEBBAPMLTFVWFLSPQ2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"XML files (*.xml);;All files (*.*)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файлы XML (*.xml);;Все файлы (*.*)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4MOT7FHCJGZBCYBHGJU52YR5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move Jobs From Other AADC Member");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенос заданий с другого узла AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6RIYP2XWNGZTKHZI2K6FHXC7M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Confirm Usage of These Options");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подтвердите использование этих настроек");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP7GO5RVH3VA27A7QKA2SONNPXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dual control for configuration loading");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Двойной контроль при загрузке конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQD2G3JOHXBHD7LOIMUS2FAZJ6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Contains the settings of the user definition development environment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Содержит настройка среды разработки пользовательских дефиниций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQFSNCPVIBBDWTELMJ67RVGZDDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of EAS sessions per user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество EAS-сессий для пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQI3B2WEYTJG43HUBW6FPF5ET5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Certificate attribute containing login name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Атрибут сертификата, содержащий имя учетной записи");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQQSEHVQM6FGATHBCAO66ND6TNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Kerberos service principal name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя сервиса в Kerberos");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSPAPCBJMJFJ5FTKOZJO65G7OU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to unset \'Dual control for roles assignment\'.\nFound unaccepted entries.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не могу убрать значение свойства \'Двойной контроль при назначении ролей\'.\nОбнаружены неакцептованные сущности системы прав  {0}.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQW3OUYK7MFCCZLPBJ2U6HACSQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Default country");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Страна по умолчанию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRENN54WPHFE63BYZKKGAXXF6Q4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select file to store list of forbidden passwords");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите файл для сохранения списка запрещенных паролей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRO3VJR5WRJHRBOUJUQLVOD66GM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for audit log - medium (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения журнала аудита - средний (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRTPQ4HWFOFFGFF7DABTXOKHNSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS2EUYB5G2BFQFPR2JOYCS5KT3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application startup parameters require this option to be not more than %s");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Параметры запуска приложения требуют, чтобы данная опция имела значение не более %s");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS3JSUKPKOFDC5CX5SMP2GWYAQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password must differ from user name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль должен отличаться от имени пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5SHVT4RQNCZDB64TNPGSSFQB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to write file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удается записать файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSDKCMOS6NFCBBKUOLETFTFYOCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to rewrite file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удается перезаписать файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSN4F7VHVMZGCNHXDP4XWUQNMTU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for metric history (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения истории метрик (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSR3TXHYIVBDWBEIYXNMV2QLYGQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SAP registration error: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при регистрации точки доступа:  ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSR4GQ24QVNAI7NDNPPKEJRURJE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT2PGFYGYXZALZP6L6J4BBFRXPY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export Value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать значение");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT5WZXIFJ7FABJPV6LXA36BGXSM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAU2ZG3FLJA4NEQ7ETSXDZRJN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTBWY6FCE4FB4LN76EIVDC35ULA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period to force instance threads state registration (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период принудительной регистрации состояния потоков инстанций (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTYYWK7Q45RAPHJX2L6N35IMGNI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service registration error: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при регистрации сервиса: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUDYNKA5IRNHWDJ74I3VP2PURHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Systems");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUETOIVJM7ZAMNAUVBCDJZVASDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYO6SZTTQ5CLFDSNB4FHSNAESY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка импорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVH3CAYD62FA4LMX3RKHS5VKVII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"EAS");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"EAS");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVO77QYHNIRE5TM7JHMZYJ2LIIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка экспорта");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVRJL6IWVOVCDXKHTKMJ6CBMD2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVVQMVMSDWZD75LDNUQGSBGTDLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"External code");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Внешний код");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVYPDVMQINNGVRAFO43KDZ2TR2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to read file \'%1$s\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удается прочитать файл \'%1$s\'");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5OABATGX5CR3PQRC3EUEDWCUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Default audit scheme");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Схема аудита по умолчанию");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW5VT5JFWDFFLNDJKC3RWV3V7CE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of incorrect logon attempts to lock user");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество неверных вводов до блокировки пользователя");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWAOYX6X4RFF6XEYQ5ML7S4WEVY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Password must contain special character");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пароль должен содержать специальный символ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWHFHFWIXTNFSDLPV3JAKFQHMZM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Release hung locks after (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Снимать зависшие блокировки через (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWKFLGQE3PJCQTCPVL7QHQ4A4H4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' modified ARTE GUI trace profile. Old value: \'%2\'. New value: \'%3\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \"%1\" изменил профиль трассировки ARTE на экран. Старое значение: \"%2\". Новое значение: \"%3\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMQBSH5YARAGVIE6HUQMGUPFAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Dual control for role assignment");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Двойной контроль при назначении ролей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWQIK4SZMH5ALFCSF5SGU3ZBIQY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select File to Save Manifest");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите файл для сохранения Манифеста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX3S7TIAIINHONNYO3PXMI5R6V4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User \'%1\' modified ARTE database trace profile. Old value: \'%2\'. New value: \'%3\'");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь \"%1\" изменил профиль трассировки ARTE в БД. Старое значение: \"%2\". Новое значение: \"%3\"");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXGRYFXJP3NAHDIX4A5P5BROIYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.Audit",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used as part of SNMP address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Используется в составе адреса SNMP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRRERXT5RBGXZAQV6WQ37VNZWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<default language set>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<исходный список языков>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXRUSBOMVINE7LMQ2SEQB5V33LM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Prefixes of profiled time sections ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Префиксы профилируемых временных секций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXW2SS6PTDFDTRARH67Q4ZW4AP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Disabling of this option will not cause deletion of sensitive data from Event Log.\n\nIt is strongly recommend to execute command \"Remove All Sensitive Data from Event Log\" as soon as possible.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выключение данной опции не приведет к очистке Журнала событий от чувствительных данных.\n\nРекомендуется выполнить команду \"Удалить все чувствительные данные из Журнала событий\" как можно скорее.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYS5BVVLEVVAL3C6WNJU72CVKIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Show AADC Locks");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Показать AADC блокировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZCX3AFOUTFHYROFXYVORR4ZIGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delete All Sensitive Data from Event Log");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удалить все чувствительные данные из журнала событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZFYKIYRC3ZGXJI5PYJOUGP7S6A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Manifest File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выберите файл Манифеста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZRZJRWSHMVBEXFYQDL4HIQKGFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(System - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecX5TD7JDVVHWDBROXAAIT4AGD7E"),"System - Localizing Bundle",$$$items$$$);
}
