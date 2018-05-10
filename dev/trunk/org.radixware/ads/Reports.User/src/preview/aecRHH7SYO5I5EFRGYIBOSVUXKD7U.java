
/* Radix::Reports.User::UserReport - Server Executable*/

/*Radix::Reports.User::UserReport-Entity Class*/

package org.radixware.ads.Reports.User.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport")
public final published class UserReport  extends org.radixware.ads.Types.server.Entity  implements org.radixware.ads.CfgManagement.server.ICfgReferencedObject,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return UserReport_mi.rdxMeta;}

	/*Radix::Reports.User::UserReport:Nested classes-Nested Classes*/

	/*Radix::Reports.User::UserReport:Properties-Properties*/

	/*Radix::Reports.User::UserReport:guid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:guid")
	public  Str getGuid() {
		return guid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:guid")
	public   void setGuid(Str val) {
		guid = val;
	}

	/*Radix::Reports.User::UserReport:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateTime")
	public  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateTime")
	public   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::Reports.User::UserReport:lastUpdateUserName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateUserName")
	public  Str getLastUpdateUserName() {
		return lastUpdateUserName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateUserName")
	public   void setLastUpdateUserName(Str val) {
		lastUpdateUserName = val;
	}

	/*Radix::Reports.User::UserReport:name-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:name")
	public  Str getName() {
		return name;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:name")
	public   void setName(Str val) {
		name = val;
	}

	/*Radix::Reports.User::UserReport:version-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:version")
	public  Int getVersion() {
		return version;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:version")
	public   void setVersion(Int val) {
		version = val;
	}

	/*Radix::Reports.User::UserReport:descirption-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:descirption")
	public  Str getDescirption() {
		return descirption;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:descirption")
	public   void setDescirption(Str val) {
		descirption = val;
	}

	/*Radix::Reports.User::UserReport:moduleGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleGuid")
	public  Str getModuleGuid() {
		return moduleGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleGuid")
	public   void setModuleGuid(Str val) {
		moduleGuid = val;
	}

	/*Radix::Reports.User::UserReport:module-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:module")
	public  org.radixware.ads.Reports.User.server.UserReportModule getModule() {
		return module;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:module")
	public   void setModule(org.radixware.ads.Reports.User.server.UserReportModule val) {
		module = val;
	}

	/*Radix::Reports.User::UserReport:versionOrder-Dynamic Property*/



	protected Int versionOrder=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:versionOrder")
	public  Int getVersionOrder() {


		ComputeReportVersionOrderCursor c = ComputeReportVersionOrderCursor.open(guid, version);
		try {
		    if (c.next()) {
		        return c.versionOrder;
		    } else
		        return -1;
		} catch (Exceptions::Exception e) {
		    return -1;
		} finally {
		    c.close();
		}


	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:versionOrder")
	public   void setVersionOrder(Int val) {
		versionOrder = val;
	}

	/*Radix::Reports.User::UserReport:contextParameterType-Column-Based Property*/










































	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:contextParameterType")
	public  org.radixware.schemas.xscml.TypeDeclarationDocument getContextParameterType() {
		return contextParameterType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:contextParameterType")
	public   void setContextParameterType(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
		contextParameterType = val;
	}

	/*Radix::Reports.User::UserReport:formatVersion-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:formatVersion")
	public  Int getFormatVersion() {
		return formatVersion;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:formatVersion")
	public   void setFormatVersion(Int val) {
		formatVersion = val;
	}

	/*Radix::Reports.User::UserReport:userReport-Dynamic Property*/



	protected org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef userReport=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:userReport")
	public  org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef getUserReport() {
		return userReport;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:userReport")
	public   void setUserReport(org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef val) {
		userReport = val;
	}

	/*Radix::Reports.User::UserReport:moduleName-Dynamic Property*/



	protected Str moduleName=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleName")
	public  Str getModuleName() {

		return module == null ? "Unknown" : module.title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleName")
	public   void setModuleName(Str val) {
		moduleName = val;
	}

	/*Radix::Reports.User::UserReport:currentVersion-Dynamic Property*/



	protected org.radixware.ads.Reports.User.server.UserReportVersion currentVersion=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:currentVersion")
	public  org.radixware.ads.Reports.User.server.UserReportVersion getCurrentVersion() {

		if(internal[currentVersion] == null){
		    if(version == null){
		        return null;
		    }else {
		        internal[currentVersion] = getVersion(version);
		    }
		}
		return internal[currentVersion];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:currentVersion")
	public   void setCurrentVersion(org.radixware.ads.Reports.User.server.UserReportVersion val) {

		internal[currentVersion] = val;
		if (val == null) {
		    version = null;
		}else{
		    version = val.version;
		}
	}

	/*Radix::Reports.User::UserReport:changeLog-User Property*/


















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLog")
	@Deprecated
	public published  org.radixware.ads.CfgManagement.server.ChangeLog getChangeLog() {
		return changeLog;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLog")
	@Deprecated
	public published   void setChangeLog(org.radixware.ads.CfgManagement.server.ChangeLog val) {
		changeLog = val;
	}

	/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner-Dynamic Property*/



	protected org.radixware.schemas.commondef.ChangeLogDocument changeLogXmlFromReportDesigner=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner")
	private final  org.radixware.schemas.commondef.ChangeLogDocument getChangeLogXmlFromReportDesigner() {
		return changeLogXmlFromReportDesigner;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner")
	private final   void setChangeLogXmlFromReportDesigner(org.radixware.schemas.commondef.ChangeLogDocument val) {
		changeLogXmlFromReportDesigner = val;
	}

	/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogOfCurrentVersion")
private final  org.radixware.ads.CfgManagement.server.ChangeLog getChangeLogOfCurrentVersion() {
	return changeLogOfCurrentVersion;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogOfCurrentVersion")
private final   void setChangeLogOfCurrentVersion(org.radixware.ads.CfgManagement.server.ChangeLog val) {
	changeLogOfCurrentVersion = val;
}



































































































































/*Radix::Reports.User::UserReport:Methods-Methods*/

/*Radix::Reports.User::UserReport:afterCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:afterCreate")
protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
	super.afterCreate(src);
	if (version == null)
	    createVersion(true);

}

/*Radix::Reports.User::UserReport:afterInit-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:afterInit")
protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
	super.afterInit(src, phase);
	if (guid == null) {
	    guid = Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:REPORT).toString();
	}
	lastUpdateUserName = Arte::Arte.getUserName();
	lastUpdateTime = Arte::Arte.getCurrentTime();
}

/*Radix::Reports.User::UserReport:createVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:createVersion")
public  void createVersion (boolean setAsCurrent) {
	UserReportVersion newVersion = new UserReportVersion();
	newVersion.init();
	newVersion.reportId = this.guid;
	newVersion.create();
	if (setAsCurrent) {
	    this.version = newVersion.version;
	}
}

/*Radix::Reports.User::UserReport:loadByPidStr-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:loadByPidStr")
public static  org.radixware.ads.Reports.User.server.UserReport loadByPidStr (Str pidAsStr, boolean checkExistance) {
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),pidAsStr);
	try{
	return (
	org.radixware.ads.Reports.User.server.UserReport) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::Reports.User::UserReport:loadByPK-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:loadByPK")
public static  org.radixware.ads.Reports.User.server.UserReport loadByPK (Str guid, boolean checkExistance) {
final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
		if(guid==null) return null;
		pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),guid);
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),pkValsMap);
	try{
	return (
	org.radixware.ads.Reports.User.server.UserReport) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::Reports.User::UserReport:getVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:getVersion")
public  org.radixware.ads.Reports.User.server.UserReportVersion getVersion (Int version) {
	try {
	    return UserReportVersion.loadByPK(guid, version, false);
	} catch (Exceptions::EntityObjectNotExistsError e) {
	    return null;
	}
}

/*Radix::Reports.User::UserReport:validate-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:validate")
public  Str validate () {
	boolean isValid = false;

	try {
	    Arte::Trace.enterContext(Arte::EventContextType:Development, this.guid);
	    UserReportVersion curVersion = currentVersion;
	    if (curVersion != null && curVersion.reportBinary != null) {
	        try {

	            java.io.InputStream blobStream = new java.io.ByteArrayInputStream(curVersion.reportBinary.getBytes(1, (int) curVersion.reportBinary.length()));
	            try {
	                URClassLoader cl = new URClassLoader("server", Arte::Arte.getInstance().Trace);
	                isValid = cl.validate(curVersion.reportClassGuid, blobStream, getPid().toString());
	            } finally {
	                try {
	                    blobStream.close();
	                } catch (Exceptions::IOException ex) {
	                }
	            }
	        } catch (Exceptions::SQLException ex) {
	            isValid = false;
	            Arte::Trace.put(Arte::EventSeverity:Error, Utils::ExceptionTextFormatter.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	        }
	    } else {

	        String message = "at " + name + ": runtime check is not possible because user defined report is not compiled";

	        Arte::Trace.put(Arte::EventSeverity:Error, message, Arte::EventSource:UserFunc);

	    }
	    String res = "";
	    if (!isValid) {
	        res += "<tr><td>" + module.title + "::" + name + "</td><td>(id = " + guid + ", " + "version ID" + "=" + this.version + ")" + ",</td></tr>";
	    }
	    return res;

	} finally {
	    Arte::Trace.leaveContext(Arte::EventContextType:Development, this.guid);
	}


}

/*Radix::Reports.User::UserReport:onCommand_validate-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:onCommand_validate")
public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_validate (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	String mess;
	String validationMessage = validate();
	final String messTitle = "Validation complete. ";
	boolean wasErrors = false;
	if (validationMessage.isEmpty()) {
	    mess = messTitle + "\n" + "No errors found.";
	} else {
	    
	    mess = "<table><tr><td colspan=\"2\">"+messTitle + "</td></tr>";
	    mess += "<tr><td colspan=\"2\">"+"There are errors!" + " " + "See event log for more information" + "</td></tr>";
	    //mess += validationMessage+"</table>";
	    wasErrors=true;
	}

	if (!wasErrors) {
	    return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Information, null, Client.Resources::DialogButtonType:Close, mess,null), null);
	} else {
	  
	    return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Error, null, Client.Resources::DialogButtonType:Close, mess,null), null);
	}
}

/*Radix::Reports.User::UserReport:beforeUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:beforeUpdate")
protected published  boolean beforeUpdate () {
	if (super.beforeUpdate()) {
	    boolean needChangeUpdateInfo = false;
	    if (this.getPersistentModifiedPropIds().size() <= 2) {
	        for (Types::Id propId : this.getPersistentModifiedPropIds()) {
	            if (!propId.equals(idof[UserReport:lastUpdateTime])
	                    && !propId.equals(idof[UserReport:lastUpdateUserName])) {
	                needChangeUpdateInfo = true;
	                break;
	            }
	        }
	    } else {
	        needChangeUpdateInfo = true;
	    }
	    if (needChangeUpdateInfo) {
	        setUpdateInfo(Utils::Timing.getCurrentTime(), Arte::Arte.getUserName());
	    }

	    return true;
	}
	return false;


}

/*Radix::Reports.User::UserReport:setUpdateInfo-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:setUpdateInfo")
public  void setUpdateInfo (java.sql.Timestamp lastUpdateTime, Str lastUpdateUserName) {
	this.lastUpdateTime = lastUpdateTime;
	this.lastUpdateUserName = lastUpdateUserName;
}

/*Radix::Reports.User::UserReport:compileCurrentVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:compileCurrentVersion")
public  boolean compileCurrentVersion () {
	return compileVersion(new Utils::UDSCompiler(),currentVersion);

}

/*Radix::Reports.User::UserReport:onCommand_compile-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:onCommand_compile")
public  org.radixware.kernel.server.types.FormHandler.NextDialogsRequest onCommand_compile (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	if(!compileCurrentVersion()){
	    return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Error, null, Client.Resources::DialogButtonType:Close, null, "Compilation failed. For details, see the event log."), null);
	} else {
	    return new FormHandlerNextDialogsRequest(new FormHandlerMessageBoxRequest(Client.Resources::DialogType:Information, null, Client.Resources::DialogButtonType:Close, null, "Compilation completed successfully"), null);
	}
}

/*Radix::Reports.User::UserReport:compileVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:compileVersion")
 published  boolean compileVersion (org.radixware.ads.Utils.server.UDSCompiler compiler, Int versionId) {
	final UserReportVersion currentVersion = this.getVersion(versionId);
	return compileVersion(compiler,currentVersion);
}

/*Radix::Reports.User::UserReport:hasValidCurrentVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:hasValidCurrentVersion")
  boolean hasValidCurrentVersion () {
	UserReportVersion version = currentVersion;;
	if(version == null){
	    return false;
	}else{
	    return version.reportBinary != null;
	}
}

/*Radix::Reports.User::UserReport:compileVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:compileVersion")
public  boolean compileVersion (Int version) {
	UserReportVersion versionObj = getVersion(version);
	if (versionObj != null) {
	    Utils::UDSCompiler compiler = new Utils::UDSCompiler();
	    UDSUserReportLoader loader = new UDSUserReportLoader();
	    loader.explicitReportId = guid;
	    loader.explicitVersionId = version.longValue();
	    compiler.externalLoader = loader;
	    return compileVersion(compiler,versionObj);
	} else{
	    Arte::Trace.put(Arte::EventSeverity:Error,"No version #" + version + " found for report" + module.title + "::" + name,Arte::EventSource:UserFunc);
	    return false;
	}


}

/*Radix::Reports.User::UserReport:compileCurrentVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:compileCurrentVersion")
 published  boolean compileCurrentVersion (org.radixware.ads.Utils.server.UDSCompiler compiler) {
	return compileVersion(compiler,currentVersion);
}

/*Radix::Reports.User::UserReport:exportThis-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:exportThis")
public published  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data) {
	exportThis(data, null);
}

/*Radix::Reports.User::UserReport:updateFromCfgItem-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:updateFromCfgItem")
 published  void updateFromCfgItem (org.radixware.ads.Reports.User.server.CfgItem.UserReportSingle cfg, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	importThis(cfg.myData.AdsUserReportExchange, helper);


}

/*Radix::Reports.User::UserReport:create-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:create")
 static  org.radixware.ads.Reports.User.server.UserReport create (org.radixware.ads.Reports.User.server.UserReportModule module, Str guid) {
	UserReport obj = new UserReport();
	obj.init();
	obj.module = module;
	if (guid != null && !guid.startsWith(Meta::DefinitionIdPrefix:USER_DEFINED_REPORT.Value)) {
	    Arte::Trace.warning("Wrong guid used for user report creation: " + guid, Arte::EventSource:UserFunc);
	}
	obj.guid = guid;

	return obj;
}

/*Radix::Reports.User::UserReport:importThis-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:importThis")
 published  boolean importThis (org.radixware.schemas.adsdef.UserReportExchangeType xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	name = xml.Name;
	descirption = xml.Description;
	boolean curVersionChanged = false;
	if (xml.AdsUserReportDefinitionList != null && !xml.AdsUserReportDefinitionList.isEmpty()) {
	    if (xml.Images != null) {
	        java.util.Map<String, String> replaceIdMap = new java.util.HashMap<>();
	        if (!module.addImages(xml.Images, replaceIdMap)) {
	            helper.reportObjectCancelled(this);
	            return false;
	        }
	        if (!replaceIdMap.isEmpty()) {
	            //Replace images ids in report xml
	            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
	            try {
	                Meta::AdsDefXsd:AdsUserReportExchangeDocument xTempXml = Meta::AdsDefXsd:AdsUserReportExchangeDocument.Factory.newInstance();
	                xTempXml.addNewAdsUserReportExchange().set(xml);
	                org.radixware.kernel.common.utils.XmlFormatter.save(xTempXml, out);
	                String asString = new String(out.toByteArray(), org.radixware.kernel.common.utils.FileUtils.XML_ENCODING);
	                out = null;
	                for (java.util.Map.Entry<String, String> e : replaceIdMap.entrySet()) {
	                    asString = asString.replace(e.getKey(), e.getValue());
	                }
	                Meta::AdsDefXsd:AdsUserReportExchangeDocument xDoc = Meta::AdsDefXsd:AdsUserReportExchangeDocument.Factory.parse(asString);
	                xml = xDoc.getAdsUserReportExchange();
	            } catch (Exceptions::Exception ex) {
	                Arte::Trace.error(Arte::Trace.exceptionStackToString(ex), Arte::EventSource:UserFunc);
	                helper.reportObjectCancelled(this);
	                return false;
	            }
	        }
	    }

	    boolean needSetCurrentVersion;
	    if (isInDatabase(false)) {
	        helper.reportObjectUpdated(this);
	        needSetCurrentVersion = false;
	    } else {
	        create();
	        helper.reportObjectCreated(this);
	        needSetCurrentVersion = true;
	    }

	    for (Meta::AdsDefXsd:UserReportDefinitionType xDef : xml.AdsUserReportDefinitionList) {
	        AdsUserReportClassDef.migrateChangeLogToVersionForCompatibility(xml, xDef);
	        UserReportVersion version = importVersion(xDef, helper);
	        if (version != null && (xDef.ActivateVersionAfterImport || needSetCurrentVersion)) {
	            if (needSetCurrentVersion) {
	                currentVersion.delete(); //delete default current version
	                needSetCurrentVersion = false;
	            }
	            currentVersion = version;
	            curVersionChanged = true;
	        }
	    }

	    if (curVersionChanged) {
	        UDSUserReportLoader loader = new UDSUserReportLoader();
	        loader.additionalVersion = currentVersion;
	        Utils::UDSCompiler compiler = new Utils::UDSCompiler();
	        compiler.externalLoader = loader;
	        if (!compileVersion(compiler, loader.additionalVersion)) {
	            helper.reportWarnings(this, "Compilation error");
	        }
	    } else if (currentVersion == null) {
	        helper.reportWarnings(this, "Current version was not set");
	    }

	    if (isModified()) {
	        //New version must be in database, before start import next report,
	        //because version can be used as subreport.
	        update();
	    }
	} else {
	    helper.reportObjectCancelled(this);
	    return false;
	}

	return true;
}

/*Radix::Reports.User::UserReport:exportToXml-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:exportToXml")
public published  org.radixware.schemas.adsdef.AdsUserReportExchangeDocument exportToXml () {
	return export();
}

/*Radix::Reports.User::UserReport:exportVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:exportVersion")
public published  void exportVersion (Int version, org.radixware.schemas.adsdef.UserReportDefinitionType xDef) {
	UserReportVersion v = getVersion(version);
	if (v == null) {
	    return;
	}
	Meta::AdsDefXsd:AdsUserReportDefinitionDocument xRepSrc = v.getReportSourceWithActualChangelog(false);
	if (xRepSrc != null && xRepSrc.AdsUserReportDefinition != null) {
	    xDef.addNewReport().set(xRepSrc.AdsUserReportDefinition.Report);
	    if (xRepSrc.AdsUserReportDefinition.Strings != null)
	        xDef.addNewStrings().set(xRepSrc.AdsUserReportDefinition.Strings);
	}
}

/*Radix::Reports.User::UserReport:export-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:export")
public published  org.radixware.schemas.adsdef.AdsUserReportExchangeDocument export () {
	Meta::AdsDefXsd:AdsUserReportExchangeDocument xDoc = Meta::AdsDefXsd:AdsUserReportExchangeDocument.Factory.newInstance();
	Meta::AdsDefXsd:UserReportExchangeType xRepEcxh = xDoc.addNewAdsUserReportExchange();
	xRepEcxh.Id = Types::Id.Factory.loadFrom(guid);
	xRepEcxh.Name = name;
	xRepEcxh.Description = descirption;
	xRepEcxh.ModuleId = Types::Id.Factory.loadFrom(module.guid);
	xRepEcxh.ModuleName = module.title;

	Meta::AdsDefXsd:UserReportDefinitionType xDef = xRepEcxh.addNewAdsUserReportDefinition();

	exportVersion(version, xDef);

	if(currentVersion == null){
	    return xDoc;
	}

	Blob images = currentVersion.getUsedImages();

	if (images != null) {
	    try {
	        xRepEcxh.Images = org.radixware.kernel.common.utils.Base64.encode(images.getBytes(1, (int) images.length()));
	    } catch (Exceptions::SQLException e) {
	//ignore. lost of images is not so important
	    }
	}

	//    final  cur = .();
	//    try {
	//        while (cur.()) {
	//            if (cur. != ) {
	//                xDef = xRepEcxh.addNewAdsUserReportDefinition();
	//                (cur., xDef);
	//            }
	//        }
	//    } finally {
	//        cur.();
	//    }
	return xDoc;


}

/*Radix::Reports.User::UserReport:importVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:importVersion")
public published  org.radixware.ads.Reports.User.server.UserReportVersion importVersion (org.radixware.schemas.adsdef.UserReportDefinitionType reportData, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	if (reportData.Report == null) {
	    System.out.println(reportData);

	    return null;
	}

	String oldId = reportData.Report.Id.toString();

	Meta::AdsDefXsd:AdsUserReportDefinitionDocument xNewDoc = Meta::AdsDefXsd:AdsUserReportDefinitionDocument.Factory.newInstance();
	xNewDoc.addNewAdsUserReportDefinition().setReport(reportData.Report);
	if (reportData.Strings != null) {
	    xNewDoc.AdsUserReportDefinition.setStrings(reportData.Strings);
	}
	String reportAsText = xNewDoc.xmlText();

	reportAsText.replace(oldId, guid);

	try {
	    UserReportVersion version = new UserReportVersion();
	    version.init();
	    xNewDoc = Meta::AdsDefXsd:AdsUserReportDefinitionDocument.Factory.parse(reportAsText);
	    version.helperFromImport = helper;
	    version.reportSource = xNewDoc;
	    version.reportId = guid;
	    version.create();
	    return version;
	} catch (Exceptions::XmlException ex) {
	    ex.printStackTrace();
	    return null;
	}
}

/*Radix::Reports.User::UserReport:exportAll-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:exportAll")
public static published  void exportAll (Str moduleId, org.radixware.ads.CfgManagement.server.CfgExportData data) {
	ArrStr modules = new ArrStr();
	modules.add(moduleId);
	exportAll(modules, data, null, null);
}

/*Radix::Reports.User::UserReport:importAll-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:importAll")
 static  void importAll (org.radixware.ads.Reports.User.server.UserReportModule module, org.radixware.schemas.adsdef.AdsUserReportsExchangeDocument.AdsUserReportsExchange xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	for (Meta::AdsDefXsd:UserReportExchangeType x : xml.ItemList) {
	    importOne(module,x, helper);
	    if (helper.wasCancelled())
	        break;
	}

}

/*Radix::Reports.User::UserReport:importOne-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:importOne")
 static  org.radixware.ads.Reports.User.server.UserReport importOne (org.radixware.ads.Reports.User.server.UserReportModule module, org.radixware.schemas.adsdef.UserReportExchangeType xml, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
	if (xml == null)
	    return null;

	Str guid = Types::Id.Factory.changePrefix(xml.Id, Meta::DefinitionIdPrefix:USER_DEFINED_REPORT).toString();

	UserReport obj = UserReport.loadByPK(guid, true);
	if (obj == null) {
	    obj = create(module,guid);
	    if(!obj.importThis(xml, helper)){
	        obj.discard();
	        obj = null;
	    }
	} else
	    switch (helper.getActionIfObjExists(obj)) {
	        case UPDATE:
	            obj.importThis(xml, helper);
	            break;
	        case NEW:
	            obj = create(module, Types::Id.Factory.newInstance(Meta::DefinitionIdPrefix:USER_DEFINED_REPORT).toString());
	            if(!obj.importThis(xml, helper)){
	                obj.discard();
	                obj = null;
	            }
	            break;
	        case CANCELL:
	            helper.reportObjectCancelled(obj);
	            break;
	    }
	return obj;
}

/*Radix::Reports.User::UserReport:compileVersion-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:compileVersion")
 published  boolean compileVersion (org.radixware.ads.Utils.server.UDSCompiler compiler, org.radixware.ads.Reports.User.server.UserReportVersion version) {
	final UserReportVersion currentVersion = version;

	try {

	    Arte::Trace.enterContext(Arte::EventContextType:Development, this.guid);

	    return compiler.compileUserReportCurrentVersion(Types::Id.Factory.loadFrom(this.guid), null, new Utils::UDSCompiler.ResultRequestor() {
	        public void accept(byte[] data, String mainClassLocalName, org.radixware.kernel.common.defs.ads.AdsDefinition compileDef) {
	            try {
	                if (data == null) {
	                    currentVersion.reportBinary = null;
	                } else {
	                    Blob blob = Arte::Arte.createTemporaryBlob();
	                    blob.setBytes(1, data);
	                    currentVersion.reportBinary = blob;
	                }

	                if (compileDef instanceof org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef) {
	                    final org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef repDef
	                            = (org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef) compileDef;
	                    Meta::XscmlXsd:TypeDeclarationDocument xCtxTypeDoc = null;
	                    final org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef ctxParamDef = repDef.findContextParameter();
	                    if (ctxParamDef != null) {
	                        xCtxTypeDoc = Meta::XscmlXsd:TypeDeclarationDocument.Factory.newInstance();
	                        ctxParamDef.getValue().getType().appendTo(xCtxTypeDoc.addNewTypeDeclaration());
	                    }
	                    currentVersion.report.contextParameterType = xCtxTypeDoc;
	                }
	                
	                currentVersion.reportClassGuid = mainClassLocalName;
	            } catch (Exceptions::SQLException e) {
	                Arte::Trace.error(org.radixware.kernel.common.utils.ExceptionTextFormatter.exceptionStackToString(e), Arte::EventSource:UserFunc);
	            }
	        }
	    });
	} finally {
	    Arte::Trace.leaveContext(Arte::EventContextType:Development, this.guid);
	}
}

/*Radix::Reports.User::UserReport:beforeCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:beforeCreate")
protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
	if(this.module != null){
	    if(!this.module.isInDatabase(true)){
	        this.module.create();
	    }
	}

	return super.beforeCreate(src);
}

/*Radix::Reports.User::UserReport:exportThis-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:exportThis")
  void exportThis (org.radixware.ads.CfgManagement.server.CfgExportData data, Bool activateVersionAfterImport) {
	data.itemClassId = idof[CfgItem.UserReportSingle];
	data.object = this;
	final Meta::AdsDefXsd:AdsUserReportExchangeDocument xDoc = exportToXml();
	if (activateVersionAfterImport != null) {
	    xDoc.AdsUserReportExchange.AdsUserReportDefinitionList.get(0).ActivateVersionAfterImport
	            = activateVersionAfterImport.booleanValue();
	}
	data.data = xDoc;
	data.fileContent = data.data;
}

/*Radix::Reports.User::UserReport:exportAll-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:exportAll")
 static  void exportAll (org.radixware.kernel.common.types.ArrStr modulesIds, org.radixware.ads.CfgManagement.server.CfgExportData data, java.util.Map<Str,org.radixware.ads.CfgManagement.server.CfgExportData> childrenExportData, Bool activateVersionAfterImport) {
	org.radixware.schemas.adsdef.AdsUserReportsExchangeDocument groupDoc = org.radixware.schemas.adsdef.AdsUserReportsExchangeDocument.Factory.newInstance();
	ListUserReportsCursorByModules c = ListUserReportsCursorByModules.open(modulesIds != null, modulesIds);
	try {
	    while (c.next()) {
	        final Meta::AdsDefXsd:AdsUserReportExchangeDocument singleDoc;
	        if (childrenExportData != null && childrenExportData.containsKey(c.report.guid)) {
	            final CfgManagement::CfgExportData singleData = childrenExportData.get(c.report.guid);
	            singleDoc = (Meta::AdsDefXsd:AdsUserReportExchangeDocument) singleData.data;
	            data.children.add(singleData);
	        } else {
	            singleDoc = c.report.exportToXml();
	            data.children.add(new CfgExportData(c.report, null, idof[CfgItem.UserReportSingle], singleDoc));
	        }
	        if (activateVersionAfterImport != null) {
	            singleDoc.AdsUserReportExchange.AdsUserReportDefinitionList.get(0).ActivateVersionAfterImport
	                    = activateVersionAfterImport.booleanValue();
	        }
	        groupDoc.ensureAdsUserReportsExchange().ItemList.add(singleDoc.AdsUserReportExchange);
	    }
	} finally {
	    c.close();
	}
	data.itemClassId = idof[CfgItem.UserReportGroup];
	data.object = null;
	data.data = null;
	data.fileContent = groupDoc;
}

/*Radix::Reports.User::UserReport:getCfgReferenceExtGuid-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:getCfgReferenceExtGuid")
public published  Str getCfgReferenceExtGuid () {
	return guid;
}

/*Radix::Reports.User::UserReport:getCfgReferencePropId-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:getCfgReferencePropId")
public published  org.radixware.kernel.common.types.Id getCfgReferencePropId () {
	return idof[UserReport:guid];
}




@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
	if(cmdId == cmdTSQDYDLOWZG37KEPGEOQXFPKQI){
		org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_validate(newPropValsById);
	return result;
} else if(cmdId == cmdWSHPDFUGKRH7RETJH2DI5XDQAI){
	org.radixware.kernel.server.types.FormHandler.NextDialogsRequest result = onCommand_compile(newPropValsById);
return result;
} else 
	return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::Reports.User::UserReport - Server Meta*/

/*Radix::Reports.User::UserReport-Entity Class*/

package org.radixware.ads.Reports.User.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserReport_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),"UserReport",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Reports.User::UserReport:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
							/*Owner Class Name*/
							"UserReport",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),
							/*Property presentations*/

							/*Radix::Reports.User::UserReport:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Reports.User::UserReport:guid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4V6ACMPQZVGUDIUW2BJMXKSFJU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:lastUpdateUserName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:name:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:version:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:descirption:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:moduleGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:module:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNZRAHOSI3FF2BPRMJ2DYZDLMDY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::Reports.User::UserReport:versionOrder:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPAFYOEI4J5HBLI25YJYU7UICUE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:contextParameterType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMLR5RZCEI5DXRPTXTCRC46763A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:formatVersion:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS4YCAONUQVCXXJX2JRQCLCRFKA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:moduleName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVDL4NV5RM5F2RNMZ2VYADRWI54"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:changeLog:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDSLY3APDHJBBHCOGYEMUDASI5Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDY6GEQQRD5EDNNHGMJKJENVXJ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion:PropertyPresentation-Object Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVMTKLIG6EFHBXPVRG6N3G6RO2A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,null,null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},null,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.User::UserReport:compile-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWSHPDFUGKRH7RETJH2DI5XDQAI"),"compile",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.User::UserReport:validate-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTSQDYDLOWZG37KEPGEOQXFPKQI"),"validate",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.User::UserReport:Export-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJ6ZKS5GKW5FH3D5E35UZP3C4ZU"),"Export",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Reports.User::UserReport:Import-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFCGDUFAT5FG3HPNJDCPO6GFIB4"),"Import",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::Reports.User::UserReport:byName-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),"byName",null,new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::Reports.User::UserReport:ByName-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQMN6KMIMCZDIHECNONT6FLQVZE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),"ByName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CHWY6D2HJH6VGZPLSYP77LLZ4"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBIO2MFW4VZBOTAQPDR3KZILWEY\"/></xsc:Item><xsc:Item><xsc:Sql> is null) or (upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblRHH7SYO5I5EFRGYIBOSVUXKD7U\" PropId=\"colUVEMGNNQCVCRLKP7GTOHJKGL7I\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBIO2MFW4VZBOTAQPDR3KZILWEY\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),true,null,false,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Reports.User::UserReport:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJIZO5FC2MVAC3NJW6QEZ7EV3PE"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::Reports.User::UserReport:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Reports.User::UserReport:General:UserReportVersion-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiY7CFUEHPWRAPHFZ7QUSLV53IKY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HIFFPRL2VG4BEZYX3IZWOVLKU"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refFKTLCBCHPFBBDMZ7KLWKFPLSVQ"),
													null,
													null),

												/*Radix::Reports.User::UserReport:General:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3YXITE4BERG7DLFH6H2RK3OMRA"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Reports.User::UserReport:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Reports.User::UserReport:Create:Children-Explorer Items*/
										null
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Reports.User::UserReport:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM4F3WGRWD5DTDOI2VXY4IAWHTU"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE")},false,null,false,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQMN6KMIMCZDIHECNONT6FLQVZE")},false,false,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJIZO5FC2MVAC3NJW6QEZ7EV3PE")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(33,null,null,null),null)
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Reports.User::UserReport:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Reports.User::UserReport:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Reports.User::UserReport:guid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),"guid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NQXJC625ZG6LMHNYFIAIE7GC4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4V6ACMPQZVGUDIUW2BJMXKSFJU"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNADDST4ZBB7RCTVQZGRDA2L2U"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:lastUpdateUserName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I"),"lastUpdateUserName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZ6VFCY4V5GJRALTDLSSEXJO3Q"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:name-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),"name",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR276AC2IVNBOPL7H36FWQG7WZU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:version-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),"version",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTGKGGCZBQBHWXAZ5QBRJ6AFMHY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:descirption-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),"descirption",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47MPBDQI3VCBXFDHQWYKJ74DE4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:moduleGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M"),"moduleGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKUQVTGN45EEHDKA2ZCTYWBZ2M"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:module-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNZRAHOSI3FF2BPRMJ2DYZDLMDY"),"module",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4XJNWPUHONE7ZNXP63HHMO6JWU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refSC2JFGD3GFFENGINX22X66YUEA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecN7LNYTJYXBDC7MTGOI4IP2THFA"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:versionOrder-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPAFYOEI4J5HBLI25YJYU7UICUE"),"versionOrder",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:contextParameterType-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMLR5RZCEI5DXRPTXTCRC46763A"),"contextParameterType",null,org.radixware.kernel.common.enums.EValType.XML,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:formatVersion-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS4YCAONUQVCXXJX2JRQCLCRFKA"),"formatVersion",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:userReport-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRITBCEQNZVHCVGWHNZC3FQ27BY"),"userReport",null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:moduleName-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVDL4NV5RM5F2RNMZ2VYADRWI54"),"moduleName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EC5PETNQVBZFHST4LHP5INLVM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:currentVersion-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMM6FR2EYVDGXKG7VTCAH2VAPY"),"currentVersion",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDFRQNSOMJFJ7AT2G3AUGPEQOQ"),org.radixware.kernel.common.enums.EValType.USER_CLASS,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:changeLog-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDSLY3APDHJBBHCOGYEMUDASI5Q"),"changeLog",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYCDXBU5ZNEFNA7B6DEFBU42JI"),org.radixware.kernel.common.enums.EValType.OBJECT,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDY6GEQQRD5EDNNHGMJKJENVXJ4"),"changeLogXmlFromReportDesigner",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVMTKLIG6EFHBXPVRG6N3G6RO2A"),"changeLogOfCurrentVersion",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIX7F2GEQBJDJXLOBTY4OI2KZ5M"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdRMM6FR2EYVDGXKG7VTCAH2VAPY")},org.radixware.kernel.common.types.Id.Factory.loadFrom("pruG7SSVR4UVFGQ5LEPLRP322MBKQ"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Reports.User::UserReport:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTAT2OWN2FFFCTFW5L3X645Z3FE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV2WVKOYSEFHZFKNEF7SW4E7RBE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZLJVPLZSQFHEDOBA3VOPDIRMQY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQZMD7CDTQRD7LKP75QP5LYAKDM"),"createVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("setAsCurrent",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3LIEF5OEDZGUXF2DRHQSDJ4MEU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr75LMSMVAQ5FDHGWXV2T2WLNPVM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK625UZE37FD4NEGIT6KV3L5RIE"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIZLJWUAVJFF7FDSECUYJBDUIPQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOLIPCCRJ4VGBBDSKTME32XTLGU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGVCHKVQWU5CJXL5QUD65UHGS4M"),"getVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("version",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4HJK7VJUABCXPHPXXL45TDCJQI"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLOETVQ4NTZEIZDENY7DMQEXTVA"),"validate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdTSQDYDLOWZG37KEPGEOQXFPKQI"),"onCommand_validate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV6MOE7U2LBDKJJ7MLRICPW4JLM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2SDXY77ME5ARZKCT63LMCX75RY"),"setUpdateInfo",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lastUpdateTime",org.radixware.kernel.common.enums.EValType.DATE_TIME,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCFFOFQF4IFEMJNMGCOE2Z2363E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("lastUpdateUserName",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQIXHWEK7NNF2HCCGSJD3DYY2LU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUQUAKDVBQZFVBCTVFKXCAOK63M"),"compileCurrentVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdWSHPDFUGKRH7RETJH2DI5XDQAI"),"onCommand_compile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ4PF3UURVZC63MPGBCTD6Q2QK4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQT5ZDGPVRRAU3KNOMDD3PLNDMM"),"compileVersion",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("compiler",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7MCEUIZMEVCHXNM6XPLEIGXNCE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("versionId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTPIW4GL2DNENPEMLWFP6ES3QT4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDFZQ36GVJNCW3PXD5U4LMPLQ6Q"),"hasValidCurrentVersion",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTEQ7KF63RBGV7GEKUFLAYISE5A"),"compileVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("version",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJTDJRLLJMFD2DAVYYJVFHVLNPU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQLZMKDIYYNHKTEAZFB6AAMODYI"),"compileCurrentVersion",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("compiler",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7MCEUIZMEVCHXNM6XPLEIGXNCE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth76URMNJO3FDFFBU76INAGS62KE"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5VR5T7UYVREMTN4XYXQWFQG54A"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPSNSCYOC4JGJPC3DVDM4E2634M"),"updateFromCfgItem",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("cfg",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAMRIOYK4ZGAJIMTDCHQBGSUZ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5IUSIB24WNFVZOBXWL2TL57JUQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGUJ7WQVC4RBBRE64ZEWIN6OTGA"),"create",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("module",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY5ZVOS4P2JGWTE23X4CCDOMRQA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("guid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRKD4DQJZ45BCBCWQDGOHYJNCVY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXJVH4K5O35BXPA4P4WNOWQCCWI"),"importThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPK55TF3CIRCULIX6OX7FD4N2LQ"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS3LAWUUV75D2RLCNP36PTP3T2M"),"exportToXml",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBHYIHGNY4BBEFGQZIPZZCAUNQQ"),"exportVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("version",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMWVKM25VZRA6NNNVMZWE5DS5RI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xDef",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXMO6UIHFKBGZHOABQZQIVLIFX4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJGUOZ42V7VEWPMAB4JBGOT54HE"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3U6FA6BIAJG4XKY7K3G3V2POC4"),"importVersion",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("reportData",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCW5PPFWGYNCWJIMNDWGS6A3NAQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXKQ6D2222NCN5BXRDEZMEIJB6I"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDS52HOWT4ZAQZFWRC3K4GU4OH4"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("moduleId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2GSA4GF7FZA7LO4DKCAF2G5PBQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAZDB5CKXUZFOJM3LECPDA7GQKM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthURQMLD5MXRGBJPTHXCXD6K2XP4"),"importAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("module",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE3A7MLX5EFAGVPZYXWTL3NORYE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5AEE44BN5VCFBEDIUMN2FNENKA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6L5FN2IOHZFKBGYX5FCA4GWUGQ"),"importOne",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("module",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4LMFD4YCGZGB5NI3YJG2WDZ5GE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xml",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2RLOK42HYZCS3LYDTUKUPR2RD4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUAHGASFHCNFIBNEQK3K2GZ7XLQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDBTMAFGLKNBKRE2WRDEEGUSHSM"),"compileVersion",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("compiler",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr7MCEUIZMEVCHXNM6XPLEIGXNCE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("version",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTPIW4GL2DNENPEMLWFP6ES3QT4"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAU6VFTOQFDJHPMSSYDCZWTOBI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4LQJMCVR3VCCRNYEAZD6GJ3KQI"),"exportThis",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5VR5T7UYVREMTN4XYXQWFQG54A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("activateVersionAfterImport",org.radixware.kernel.common.enums.EValType.BOOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNTETIIU7YNEAJCD7KABXLBAPSU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6LUFMKGQLNBT5E42OD55E676AQ"),"exportAll",true,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("modulesIds",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2GSA4GF7FZA7LO4DKCAF2G5PBQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("data",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAZDB5CKXUZFOJM3LECPDA7GQKM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("childrenExportData",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN5JHMAO5JRFN3IRIAYVG5AZB5E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("activateVersionAfterImport",org.radixware.kernel.common.enums.EValType.BOOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDX45EY656BDTNMXJ26LLTWINUI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQXRW7PZ4QBBEJAJL57JFPES7EQ"),"getCfgReferenceExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTIFZEEQC2NEUNPFIRAGRH2B7EY"),"getCfgReferencePropId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Reports.User::UserReport - Desktop Executable*/

/*Radix::Reports.User::UserReport-Entity Class*/

package org.radixware.ads.Reports.User.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport")
public interface UserReport {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Reports.User::UserReportGroup:contextModuleId:contextModuleId-Presentation Property*/




		public class ContextModuleId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ContextModuleId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReportGroup:contextModuleId:contextModuleId")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReportGroup:contextModuleId:contextModuleId")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ContextModuleId getContextModuleId(){return (ContextModuleId)getProperty(pgpL6GQYRK2DBFUTOT7AMEBF4BD6A);}







		public org.radixware.ads.Reports.User.explorer.UserReport.UserReport_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Reports.User.explorer.UserReport.UserReport_DefaultModel )  super.getEntity(i);}
	}















































































































	/*Radix::Reports.User::UserReport:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Reports.User::UserReport:moduleGuid:moduleGuid-Presentation Property*/


	public class ModuleGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ModuleGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleGuid:moduleGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleGuid:moduleGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ModuleGuid getModuleGuid();
	/*Radix::Reports.User::UserReport:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::Reports.User::UserReport:contextParameterType:contextParameterType-Presentation Property*/


	public class ContextParameterType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ContextParameterType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.xscml.TypeDeclarationDocument> getValClass(){
			return org.radixware.schemas.xscml.TypeDeclarationDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.xscml.TypeDeclarationDocument dummy = x == null ? null : (org.radixware.schemas.xscml.TypeDeclarationDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.xscml.TypeDeclarationDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:contextParameterType:contextParameterType")
		public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:contextParameterType:contextParameterType")
		public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
			Value = val;
		}
	}
	public ContextParameterType getContextParameterType();
	/*Radix::Reports.User::UserReport:module:module-Presentation Property*/


	public class Module extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Module(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.User.explorer.UserReportModule.UserReportModule_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.User.explorer.UserReportModule.UserReportModule_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.User.explorer.UserReportModule.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.User.explorer.UserReportModule.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:module:module")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:module:module")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Module getModule();
	/*Radix::Reports.User::UserReport:version:version-Presentation Property*/


	public class Version extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Version(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:version:version")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:version:version")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Version getVersion();
	/*Radix::Reports.User::UserReport:formatVersion:formatVersion-Presentation Property*/


	public class FormatVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FormatVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:formatVersion:formatVersion")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:formatVersion:formatVersion")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public FormatVersion getFormatVersion();
	/*Radix::Reports.User::UserReport:descirption:descirption-Presentation Property*/


	public class Descirption extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Descirption(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:descirption:descirption")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:descirption:descirption")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Descirption getDescirption();
	/*Radix::Reports.User::UserReport:name:name-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion:changeLogOfCurrentVersion-Presentation Property*/


	public class ChangeLogOfCurrentVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLogOfCurrentVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogOfCurrentVersion:changeLogOfCurrentVersion")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogOfCurrentVersion:changeLogOfCurrentVersion")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLogOfCurrentVersion getChangeLogOfCurrentVersion();
	/*Radix::Reports.User::UserReport:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


	public class LastUpdateUserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:changeLogXmlFromReportDesigner-Presentation Property*/


	public class ChangeLogXmlFromReportDesigner extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ChangeLogXmlFromReportDesigner(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:changeLogXmlFromReportDesigner")
		public  org.radixware.schemas.commondef.ChangeLogDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:changeLogXmlFromReportDesigner")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogDocument val) {
			Value = val;
		}
	}
	public ChangeLogXmlFromReportDesigner getChangeLogXmlFromReportDesigner();
	/*Radix::Reports.User::UserReport:versionOrder:versionOrder-Presentation Property*/


	public class VersionOrder extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public VersionOrder(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:versionOrder:versionOrder")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:versionOrder:versionOrder")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public VersionOrder getVersionOrder();
	/*Radix::Reports.User::UserReport:moduleName:moduleName-Presentation Property*/


	public class ModuleName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ModuleName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleName:moduleName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleName:moduleName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ModuleName getModuleName();
	/*Radix::Reports.User::UserReport:changeLog:changeLog-Presentation Property*/


	public class ChangeLog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLog:changeLog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLog:changeLog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog();
	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.schemas.adsdef.AdsUserReportExchangeDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Validate extends org.radixware.kernel.common.client.models.items.Command{
		protected Validate(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Compile extends org.radixware.kernel.common.client.models.items.Command{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Reports.User::UserReport - Desktop Meta*/

/*Radix::Reports.User::UserReport-Entity Class*/

package org.radixware.ads.Reports.User.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserReport_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports.User::UserReport:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
			"Radix::Reports.User::UserReport",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTY3GSTHHQJFQHDCDJRPCMJRFKM"),
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4A2O434D5ENHPWYTNGH6OTT5E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),0,

			/*Radix::Reports.User::UserReport:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports.User::UserReport:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NQXJC625ZG6LMHNYFIAIE7GC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4V6ACMPQZVGUDIUW2BJMXKSFJU"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNADDST4ZBB7RCTVQZGRDA2L2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZ6VFCY4V5GJRALTDLSSEXJO3Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR276AC2IVNBOPL7H36FWQG7WZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						true,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:version:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),
						"version",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTGKGGCZBQBHWXAZ5QBRJ6AFMHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:version:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:descirption:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),
						"descirption",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47MPBDQI3VCBXFDHQWYKJ74DE4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:descirption:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:moduleGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M"),
						"moduleGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKUQVTGN45EEHDKA2ZCTYWBZ2M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:moduleGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:module:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNZRAHOSI3FF2BPRMJ2DYZDLMDY"),
						"module",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecN7LNYTJYXBDC7MTGOI4IP2THFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblN7LNYTJYXBDC7MTGOI4IP2THFA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Reports.User::UserReport:versionOrder:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPAFYOEI4J5HBLI25YJYU7UICUE"),
						"versionOrder",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::Reports.User::UserReport:versionOrder:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:contextParameterType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMLR5RZCEI5DXRPTXTCRC46763A"),
						"contextParameterType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:formatVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS4YCAONUQVCXXJX2JRQCLCRFKA"),
						"formatVersion",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:formatVersion:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:moduleName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVDL4NV5RM5F2RNMZ2VYADRWI54"),
						"moduleName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EC5PETNQVBZFHST4LHP5INLVM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:moduleName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:changeLog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDSLY3APDHJBBHCOGYEMUDASI5Q"),
						"changeLog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,true,false),

					/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDY6GEQQRD5EDNNHGMJKJENVXJ4"),
						"changeLogXmlFromReportDesigner",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

					/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVMTKLIG6EFHBXPVRG6N3G6RO2A"),
						"changeLogOfCurrentVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIX7F2GEQBJDJXLOBTY4OI2KZ5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						62,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pruG7SSVR4UVFGQ5LEPLRP322MBKQ"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,false,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.User::UserReport:compile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdWSHPDFUGKRH7RETJH2DI5XDQAI"),
						"compile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3H3IFZ2KJDHFI4FCOHQGIPIWM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgC4CNJN5QPFEQLFDU4HFCIJQ4UY"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.User::UserReport:validate-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTSQDYDLOWZG37KEPGEOQXFPKQI"),
						"validate",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP2LHXXYTDRCRZN7D3VOCHW5D5E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgR7YVE2LNIJAFNACYRFONGF3YTY"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_FORM_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.User::UserReport:Export-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJ6ZKS5GKW5FH3D5E35UZP3C4ZU"),
						"Export",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZWNEMMO7VGWDEZYDI3KMYWAX4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYGRKUM6D5NFB7HJSZFG3KGBC6M"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afh7JTYAUANX5G6PJSV7BOUF5EPTY"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Reports.User::UserReport:Import-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdFCGDUFAT5FG3HPNJDCPO6GFIB4"),
						"Import",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIBHOD4GB5BEEHH2L4CDAGLD6EU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEVEBHW62BFDDBLQVX2YPL7ZERM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Reports.User::UserReport:ByName-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQMN6KMIMCZDIHECNONT6FLQVZE"),
						"ByName",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CHWY6D2HJH6VGZPLSYP77LLZ4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBIO2MFW4VZBOTAQPDR3KZILWEY\"/></xsc:Item><xsc:Item><xsc:Sql> is null) or (upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblRHH7SYO5I5EFRGYIBOSVUXKD7U\" PropId=\"colUVEMGNNQCVCRLKP7GTOHJKGL7I\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBIO2MFW4VZBOTAQPDR3KZILWEY\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmBIO2MFW4VZBOTAQPDR3KZILWEY"),
								"name",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OCFROOZJ5HVXFQLEXLTKYLQQE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
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

						/*Radix::Reports.User::UserReport:ByName:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Reports.User::UserReport:byName-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),
						"byName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref652JVAU3R5ERFEAOH2QYSBTMR4"),"UserReport=>User (lastUpdateUserName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I")},new String[]{"lastUpdateUserName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refSC2JFGD3GFFENGINX22X66YUEA"),"UserReport=>UserReportModule (moduleGuid=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblN7LNYTJYXBDC7MTGOI4IP2THFA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M")},new String[]{"moduleGuid"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colHL2DQINMTBFYBKFSXNSK43NHY4")},new String[]{"guid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJIZO5FC2MVAC3NJW6QEZ7EV3PE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM4F3WGRWD5DTDOI2VXY4IAWHTU")},
			false,false,false);
}

/* Radix::Reports.User::UserReport - Web Executable*/

/*Radix::Reports.User::UserReport-Entity Class*/

package org.radixware.ads.Reports.User.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport")
public interface UserReport {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::Reports.User::UserReportGroup:contextModuleId:contextModuleId-Presentation Property*/




		public class ContextModuleId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public ContextModuleId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReportGroup:contextModuleId:contextModuleId")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReportGroup:contextModuleId:contextModuleId")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public ContextModuleId getContextModuleId(){return (ContextModuleId)getProperty(pgpL6GQYRK2DBFUTOT7AMEBF4BD6A);}







		public org.radixware.ads.Reports.User.web.UserReport.UserReport_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Reports.User.web.UserReport.UserReport_DefaultModel )  super.getEntity(i);}
	}















































































































	/*Radix::Reports.User::UserReport:lastUpdateTime:lastUpdateTime-Presentation Property*/


	public class LastUpdateTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public LastUpdateTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::Reports.User::UserReport:moduleGuid:moduleGuid-Presentation Property*/


	public class ModuleGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ModuleGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleGuid:moduleGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleGuid:moduleGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ModuleGuid getModuleGuid();
	/*Radix::Reports.User::UserReport:guid:guid-Presentation Property*/


	public class Guid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Guid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:guid:guid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:guid:guid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Guid getGuid();
	/*Radix::Reports.User::UserReport:contextParameterType:contextParameterType-Presentation Property*/


	public class ContextParameterType extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ContextParameterType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.xscml.TypeDeclarationDocument> getValClass(){
			return org.radixware.schemas.xscml.TypeDeclarationDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.xscml.TypeDeclarationDocument dummy = x == null ? null : (org.radixware.schemas.xscml.TypeDeclarationDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.xscml.TypeDeclarationDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:contextParameterType:contextParameterType")
		public  org.radixware.schemas.xscml.TypeDeclarationDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:contextParameterType:contextParameterType")
		public   void setValue(org.radixware.schemas.xscml.TypeDeclarationDocument val) {
			Value = val;
		}
	}
	public ContextParameterType getContextParameterType();
	/*Radix::Reports.User::UserReport:module:module-Presentation Property*/


	public class Module extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Module(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.Reports.User.web.UserReportModule.UserReportModule_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.Reports.User.web.UserReportModule.UserReportModule_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.Reports.User.web.UserReportModule.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.Reports.User.web.UserReportModule.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:module:module")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:module:module")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Module getModule();
	/*Radix::Reports.User::UserReport:version:version-Presentation Property*/


	public class Version extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Version(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:version:version")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:version:version")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Version getVersion();
	/*Radix::Reports.User::UserReport:formatVersion:formatVersion-Presentation Property*/


	public class FormatVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public FormatVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:formatVersion:formatVersion")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:formatVersion:formatVersion")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public FormatVersion getFormatVersion();
	/*Radix::Reports.User::UserReport:descirption:descirption-Presentation Property*/


	public class Descirption extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Descirption(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:descirption:descirption")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:descirption:descirption")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Descirption getDescirption();
	/*Radix::Reports.User::UserReport:name:name-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName();
	/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion:changeLogOfCurrentVersion-Presentation Property*/


	public class ChangeLogOfCurrentVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLogOfCurrentVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogOfCurrentVersion:changeLogOfCurrentVersion")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogOfCurrentVersion:changeLogOfCurrentVersion")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLogOfCurrentVersion getChangeLogOfCurrentVersion();
	/*Radix::Reports.User::UserReport:lastUpdateUserName:lastUpdateUserName-Presentation Property*/


	public class LastUpdateUserName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUserName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateUserName:lastUpdateUserName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:lastUpdateUserName:lastUpdateUserName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUserName getLastUpdateUserName();
	/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:changeLogXmlFromReportDesigner-Presentation Property*/


	public class ChangeLogXmlFromReportDesigner extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public ChangeLogXmlFromReportDesigner(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.radixware.schemas.commondef.ChangeLogDocument> getValClass(){
			return org.radixware.schemas.commondef.ChangeLogDocument.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.schemas.commondef.ChangeLogDocument dummy = x == null ? null : (org.radixware.schemas.commondef.ChangeLogDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.radixware.schemas.commondef.ChangeLogDocument.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:changeLogXmlFromReportDesigner")
		public  org.radixware.schemas.commondef.ChangeLogDocument getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:changeLogXmlFromReportDesigner")
		public   void setValue(org.radixware.schemas.commondef.ChangeLogDocument val) {
			Value = val;
		}
	}
	public ChangeLogXmlFromReportDesigner getChangeLogXmlFromReportDesigner();
	/*Radix::Reports.User::UserReport:versionOrder:versionOrder-Presentation Property*/


	public class VersionOrder extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public VersionOrder(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:versionOrder:versionOrder")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:versionOrder:versionOrder")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public VersionOrder getVersionOrder();
	/*Radix::Reports.User::UserReport:moduleName:moduleName-Presentation Property*/


	public class ModuleName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ModuleName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleName:moduleName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:moduleName:moduleName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ModuleName getModuleName();
	/*Radix::Reports.User::UserReport:changeLog:changeLog-Presentation Property*/


	public class ChangeLog extends org.radixware.kernel.common.client.models.items.properties.PropertyObject{
		public ChangeLog(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.web.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Deprecated
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}













		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLog:changeLog")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}
		@Deprecated

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:changeLog:changeLog")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLog getChangeLog();
	public static class Import extends org.radixware.kernel.common.client.models.items.Command{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.StrDocument send(org.radixware.schemas.adsdef.AdsUserReportExchangeDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.StrDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.StrDocument.class);
		}

	}

	public static class Export extends org.radixware.kernel.common.client.models.items.Command{
		protected Export(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class Validate extends org.radixware.kernel.common.client.models.items.Command{
		protected Validate(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class Compile extends org.radixware.kernel.common.client.models.items.Command{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}



}

/* Radix::Reports.User::UserReport - Web Meta*/

/*Radix::Reports.User::UserReport-Entity Class*/

package org.radixware.ads.Reports.User.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserReport_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Reports.User::UserReport:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
			"Radix::Reports.User::UserReport",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTY3GSTHHQJFQHDCDJRPCMJRFKM"),
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4A2O434D5ENHPWYTNGH6OTT5E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),0,

			/*Radix::Reports.User::UserReport:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Reports.User::UserReport:guid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),
						"guid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NQXJC625ZG6LMHNYFIAIE7GC4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:guid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4V6ACMPQZVGUDIUW2BJMXKSFJU"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNADDST4ZBB7RCTVQZGRDA2L2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:lastUpdateUserName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I"),
						"lastUpdateUserName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZ6VFCY4V5GJRALTDLSSEXJO3Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:lastUpdateUserName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:name:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),
						"name",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR276AC2IVNBOPL7H36FWQG7WZU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						true,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:name:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:version:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),
						"version",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTGKGGCZBQBHWXAZ5QBRJ6AFMHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:version:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:descirption:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),
						"descirption",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47MPBDQI3VCBXFDHQWYKJ74DE4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:descirption:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:moduleGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M"),
						"moduleGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKUQVTGN45EEHDKA2ZCTYWBZ2M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Reports.User::UserReport:moduleGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:module:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNZRAHOSI3FF2BPRMJ2DYZDLMDY"),
						"module",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecN7LNYTJYXBDC7MTGOI4IP2THFA"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblN7LNYTJYXBDC7MTGOI4IP2THFA"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::Reports.User::UserReport:versionOrder:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdPAFYOEI4J5HBLI25YJYU7UICUE"),
						"versionOrder",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::Reports.User::UserReport:versionOrder:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:contextParameterType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMLR5RZCEI5DXRPTXTCRC46763A"),
						"contextParameterType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.XML,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:formatVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colS4YCAONUQVCXXJX2JRQCLCRFKA"),
						"formatVersion",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:formatVersion:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:moduleName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVDL4NV5RM5F2RNMZ2VYADRWI54"),
						"moduleName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EC5PETNQVBZFHST4LHP5INLVM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

						/*Radix::Reports.User::UserReport:moduleName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Reports.User::UserReport:changeLog:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDSLY3APDHJBBHCOGYEMUDASI5Q"),
						"changeLog",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,true,false),

					/*Radix::Reports.User::UserReport:changeLogXmlFromReportDesigner:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDY6GEQQRD5EDNNHGMJKJENVXJ4"),
						"changeLogXmlFromReportDesigner",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
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

					/*Radix::Reports.User::UserReport:changeLogOfCurrentVersion:PropertyPresentation-Object Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVMTKLIG6EFHBXPVRG6N3G6RO2A"),
						"changeLogOfCurrentVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIX7F2GEQBJDJXLOBTY4OI2KZ5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.enums.EValType.OBJECT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						62,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pruG7SSVR4UVFGQ5LEPLRP322MBKQ"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblVHY5COSHWNA7PPJ65FW3OGJ4ZQ"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprVRZPY6RJKBHWFLK46ATZLXFW3U")},
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr6HHEKF43V5BUPOUOE7WW7JP2PU")},
						null,
						0L,
						0L,false,false)
			},
			null,
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::Reports.User::UserReport:ByName-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQMN6KMIMCZDIHECNONT6FLQVZE"),
						"ByName",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CHWY6D2HJH6VGZPLSYP77LLZ4"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBIO2MFW4VZBOTAQPDR3KZILWEY\"/></xsc:Item><xsc:Item><xsc:Sql> is null) or (upper(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"tblRHH7SYO5I5EFRGYIBOSVUXKD7U\" PropId=\"colUVEMGNNQCVCRLKP7GTOHJKGL7I\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>) like </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmBIO2MFW4VZBOTAQPDR3KZILWEY\"/></xsc:Item><xsc:Item><xsc:Sql>)</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmBIO2MFW4VZBOTAQPDR3KZILWEY"),
								"name",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OCFROOZJ5HVXFQLEXLTKYLQQE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
								null,
								org.radixware.kernel.common.enums.EValType.STR,
								null,
								null,
								false,
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

						/*Radix::Reports.User::UserReport:ByName:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::Reports.User::UserReport:byName-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),
						"byName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
						null,
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref652JVAU3R5ERFEAOH2QYSBTMR4"),"UserReport=>User (lastUpdateUserName=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I")},new String[]{"lastUpdateUserName"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refSC2JFGD3GFFENGINX22X66YUEA"),"UserReport=>UserReportModule (moduleGuid=>guid)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblN7LNYTJYXBDC7MTGOI4IP2THFA"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colIE7QFFHT6VAJZMYY6E5NAFR57M")},new String[]{"moduleGuid"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colHL2DQINMTBFYBKFSXNSK43NHY4")},new String[]{"guid"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA")},
			false,false,false);
}

/* Radix::Reports.User::UserReport:General - Desktop Meta*/

/*Radix::Reports.User::UserReport:General-Editor Presentation*/

package org.radixware.ads.Reports.User.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJIZO5FC2MVAC3NJW6QEZ7EV3PE"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
	null,
	null,

	/*Radix::Reports.User::UserReport:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Reports.User::UserReport:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgEF5ODV3N2BC6ZLBSTIFJUB3LY4"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJKKPDB5TBAOPON7JVUHMX7CDA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),1,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4V6ACMPQZVGUDIUW2BJMXKSFJU"),1,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVU7VPPUTCFHAZIJVOZS3LW5E2I"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),1,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVDL4NV5RM5F2RNMZ2VYADRWI54"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVMTKLIG6EFHBXPVRG6N3G6RO2A"),0,4,1,false,false)
			},null),

			/*Radix::Reports.User::UserReport:General:VersionsPage-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF6FEYYFSQZF6JP6T2NXQUNWB24"),"VersionsPage",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBEKQZEITPRFDZKN6KEHJ2UEEOY"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepONN6ZMZCO5C6JJ7QSLI35ECLC4")),

			/*Radix::Reports.User::UserReport:General:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgG7IKNIK6RFH6DGPYXMSMPUY46I"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3YXITE4BERG7DLFH6H2RK3OMRA"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgEF5ODV3N2BC6ZLBSTIFJUB3LY4")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgF6FEYYFSQZF6JP6T2NXQUNWB24")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgG7IKNIK6RFH6DGPYXMSMPUY46I"))}
	,

	/*Radix::Reports.User::UserReport:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Reports.User::UserReport:General:UserReportVersion-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiY7CFUEHPWRAPHFZ7QUSLV53IKY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecM2NL42YXRRA5ZH27LCKIW5CQNI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr4HIFFPRL2VG4BEZYX3IZWOVLKU"),
					0,
					null,
					16560,true),

				/*Radix::Reports.User::UserReport:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi3YXITE4BERG7DLFH6H2RK3OMRA"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprKQ5WOSDJZ5AQFOGPY4PK3O2XTA"),
					0,
					null,
					16560,true)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::Reports.User::UserReport:General:Model - Desktop Executable*/

/*Radix::Reports.User::UserReport:General:Model-Entity Model Class*/

package org.radixware.ads.Reports.User.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model")
public class General:Model  extends org.radixware.ads.Reports.User.explorer.UserReport.UserReport_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Reports.User::UserReport:General:Model:Nested classes-Nested Classes*/

	/*Radix::Reports.User::UserReport:General:Model:Properties-Properties*/

	/*Radix::Reports.User::UserReport:General:Model:version-Presentation Property*/




	public class Version extends org.radixware.ads.Reports.User.explorer.UserReport.colRFGZDGVYONGERF2WDQ5THDREOE{
		public Version(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::Reports.User::UserReport:General:Model:version:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Reports.User::UserReport:General:Model:version:Nested classes-Nested Classes*/

		/*Radix::Reports.User::UserReport:General:Model:version:Properties-Properties*/

		/*Radix::Reports.User::UserReport:General:Model:version:Methods-Methods*/

		/*Radix::Reports.User::UserReport:General:Model:version:getTextOptionsMarkers-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:version:getTextOptionsMarkers")
		public published  java.util.EnumSet<org.radixware.kernel.common.client.enums.ETextOptionsMarker> getTextOptionsMarkers () {
			java.util.EnumSet<Client.Text::TextOptionsMarker> markers = super.getTextOptionsMarkers();
			markers.add(Client.Text::TextOptionsMarker.READONLY_VALUE);
			return markers;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:version")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:version")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Version getVersion(){return (Version)getProperty(colRFGZDGVYONGERF2WDQ5THDREOE);}

	/*Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion-Presentation Property*/




	public class ChangeLogOfCurrentVersion extends org.radixware.ads.Reports.User.explorer.UserReport.colVMTKLIG6EFHBXPVRG6N3G6RO2A{
		public ChangeLogOfCurrentVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.CfgManagement.explorer.ChangeLog.ChangeLog_DefaultModel)super.openEntityModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}

		/*Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion:Nested classes-Nested Classes*/

		/*Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion:Properties-Properties*/

		/*Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion:Methods-Methods*/

		/*Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion:createPropertyEditor-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion:createPropertyEditor")
		public published  org.radixware.kernel.common.client.views.IPropEditor createPropertyEditor () {
			return new ChangeLogPropEditor(changeLogOfCurrentVersion, UserReport:General:Model.this);
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:changeLogOfCurrentVersion")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ChangeLogOfCurrentVersion getChangeLogOfCurrentVersion(){return (ChangeLogOfCurrentVersion)getProperty(colVMTKLIG6EFHBXPVRG6N3G6RO2A);}










	/*Radix::Reports.User::UserReport:General:Model:Methods-Methods*/

	/*Radix::Reports.User::UserReport:General:Model:onCommand_compile-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:onCommand_compile")
	public  void onCommand_compile (org.radixware.ads.Reports.User.explorer.UserReport.Compile command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Reports.User::UserReport:General:Model:Editor_closed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:Editor_closed")
	public  void Editor_closed () {

	}

	/*Radix::Reports.User::UserReport:General:Model:onCommand_validate-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:onCommand_validate")
	public  void onCommand_validate (org.radixware.ads.Reports.User.explorer.UserReport.Validate command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}

	}

	/*Radix::Reports.User::UserReport:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.Reports.User.explorer.UserReport.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    Meta::AdsDefXsd:AdsUserReportExchangeDocument input = Meta::AdsDefXsd:AdsUserReportExchangeDocument.Factory.parse(file);
		    Common.Dlg::ClientUtils.viewImportResult(command.send(input));
		    getView().reread();
		} catch (Exceptions::XmlException | Exceptions::ServiceClientException | Exceptions::IOException e) {
		    showException(e);
		} catch (Exceptions::InterruptedException e) {
		}
	}

	/*Radix::Reports.User::UserReport:General:Model:getEventContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:getEventContextId")
	public published  Str getEventContextId () {
		return this.guid.Value;
	}

	/*Radix::Reports.User::UserReport:General:Model:getEventContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:getEventContextType")
	public published  Str getEventContextType () {
		return Arte::EventContextType:Development.Value;
	}

	/*Radix::Reports.User::UserReport:General:Model:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:afterDelete")
	protected published  void afterDelete () {
		super.afterDelete();

	}

	/*Radix::Reports.User::UserReport:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
	}

	/*Radix::Reports.User::UserReport:General:Model:test-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:test")
	public  void test () {

	}

	/*Radix::Reports.User::UserReport:General:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		changeLogOfCurrentVersion.setCanCreate(false);
		changeLogOfCurrentVersion.setCanDelete(false);
		super.beforeOpenView();
	}
	public final class Import extends org.radixware.ads.Reports.User.explorer.UserReport.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class Validate extends org.radixware.ads.Reports.User.explorer.UserReport.Validate{
		protected Validate(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_validate( this );
		}

	}

	public final class Compile extends org.radixware.ads.Reports.User.explorer.UserReport.Compile{
		protected Compile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_compile( this );
		}

	}

















}

/* Radix::Reports.User::UserReport:General:Model - Desktop Meta*/

/*Radix::Reports.User::UserReport:General:Model-Entity Model Class*/

package org.radixware.ads.Reports.User.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemJIZO5FC2MVAC3NJW6QEZ7EV3PE"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports.User::UserReport:General:Model:Properties-Properties*/
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

/* Radix::Reports.User::UserReport:General:VersionsPage:View - Desktop Executable*/

/*Radix::Reports.User::UserReport:General:VersionsPage:View-Custom Page Editor for Desktop*/

package org.radixware.ads.Reports.User.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:VersionsPage:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public org.radixware.kernel.explorer.widgets.EmbeddedSelector embeddedSelector;
	public org.radixware.kernel.explorer.widgets.EmbeddedSelector getEmbeddedSelector(){ return embeddedSelector;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 600, 400));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QHBoxLayout $layout1 = new com.trolltech.qt.gui.QHBoxLayout(EditorPageView);
		$layout1.setObjectName("horizontalLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		embeddedSelector = new org.radixware.kernel.explorer.widgets.EmbeddedSelector(model.getEnvironment(),this, org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiY7CFUEHPWRAPHFZ7QUSLV53IKY"));
		embeddedSelector.setParent(EditorPageView);
		embeddedSelector.setObjectName("embeddedSelector");
		embeddedSelector.setGeometry(new com.trolltech.qt.core.QRect(199, 68, 200, 200));
		embeddedSelector.setFont(DEFAULT_FONT);
		embeddedSelector.bind();
		$layout1.addWidget(embeddedSelector);
		opened.emit(this);
	}
	public final org.radixware.ads.Reports.User.explorer.General:Model getModel() {
		return (org.radixware.ads.Reports.User.explorer.General:Model) super.getModel();
	}

}

/* Radix::Reports.User::UserReport:General:VersionsPage:WebView - Web Executable*/

/*Radix::Reports.User::UserReport:General:VersionsPage:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.Reports.User.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:VersionsPage:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public org.radixware.wps.views.editor.PropertiesGrid widget;
	public org.radixware.wps.views.editor.PropertiesGrid getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::Reports.User::UserReport:General:VersionsPage:WebView:widget ==============
		this.widget = this;
		widget.setWidth(600);
		widget.setHeight(400);
		//============ Radix::Reports.User::UserReport:General:VersionsPage:WebView:widget:widget ==============
		this.widget = new org.radixware.wps.views.editor.PropertiesGrid();
		widget.setObjectName("widget");
		this.widget.add(this.widget);
		widget.getAnchors().setTop(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setLeft(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(0.0f,0));
		widget.getAnchors().setBottom(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		widget.getAnchors().setRight(new org.radixware.wps.rwt.UIObject.Anchors.Anchor(1.0f,0));
		this.wdgVZZARILBINALTO2NNL6GMTVDHM.bind();
		fireOpened();
	}
}

/* Radix::Reports.User::UserReport:Create - Desktop Meta*/

/*Radix::Reports.User::UserReport:Create-Editor Presentation*/

package org.radixware.ads.Reports.User.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
			null,
			null,

			/*Radix::Reports.User::UserReport:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Reports.User::UserReport:Create:Init-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKVRYM73VHZAWVNDOUY5KQK5LSY"),"Init",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKVRYM73VHZAWVNDOUY5KQK5LSY"))}
			,

			/*Radix::Reports.User::UserReport:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			16,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.User.explorer.UserReport.UserReport_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Reports.User::UserReport:Create - Web Meta*/

/*Radix::Reports.User::UserReport:Create-Editor Presentation*/

package org.radixware.ads.Reports.User.web;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
			null,
			null,

			/*Radix::Reports.User::UserReport:Create:Editor Pages-Editor Presentation Pages*/
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

					/*Radix::Reports.User::UserReport:Create:Init-Editor Page*/

					 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKVRYM73VHZAWVNDOUY5KQK5LSY"),"Init",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSAPAWW6RG5HJLMVL34K2YQKHKY"),0,1,1,false,false)
					},null)
			},
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgKVRYM73VHZAWVNDOUY5KQK5LSY"))}
			,

			/*Radix::Reports.User::UserReport:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			16,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			2192,0,0,null);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Reports.User.web.UserReport.UserReport_DefaultModel(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::Reports.User::UserReport:General - Desktop Meta*/

/*Radix::Reports.User::UserReport:General-Selector Presentation*/

package org.radixware.ads.Reports.User.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM4F3WGRWD5DTDOI2VXY4IAWHTU"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHH7SYO5I5EFRGYIBOSVUXKD7U"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblRHH7SYO5I5EFRGYIBOSVUXKD7U"),
		null,
		null,
		null,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE")},
		false,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtNCXMNRIO7VC3FH4SO6N2ZWWZUE"),
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("fltQMN6KMIMCZDIHECNONT6FLQVZE")},
		false,
		false,
		null,
		33,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr4OVCICCOEVAKHDEDRPGTIZEOUA")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprJIZO5FC2MVAC3NJW6QEZ7EV3PE")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJENVO3VYOBBRXCG3GAOQDPK72Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUVEMGNNQCVCRLKP7GTOHJKGL7I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colRFGZDGVYONGERF2WDQ5THDREOE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::Reports.User::UserReport:General:Model - Desktop Executable*/

/*Radix::Reports.User::UserReport:General:Model-Group Model Class*/

package org.radixware.ads.Reports.User.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model")
public class General:Model  extends org.radixware.ads.Reports.User.explorer.UserReport.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::Reports.User::UserReport:General:Model:Nested classes-Nested Classes*/

	/*Radix::Reports.User::UserReport:General:Model:Properties-Properties*/

	/*Radix::Reports.User::UserReport:General:Model:Methods-Methods*/

	/*Radix::Reports.User::UserReport:General:Model:onCommand_compileAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:onCommand_compileAll")
	public  void onCommand_compileAll (org.radixware.ads.Reports.User.explorer.UserReportGroup.CompileAll command) {

		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Reports.User::UserReport:General:Model:onCommand_validateAll-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:onCommand_validateAll")
	public  void onCommand_validateAll (org.radixware.ads.Reports.User.explorer.UserReportGroup.ValidateAll command) {
		//String s="";
		////long start = System.currentTimeMillis();
		// handle = ().().();
		//try {
		//    handle.(, false); 
		//     resDoc=command.send();
		//    s=resDoc.ReportValidationRs.ValidationResult;
		//}catch( ex){    
		//}catch( ex){
		//    (ex);
		//}finally {
		//    handle.();
		//}
		//
		//String messTitle=;
		//if(s.isEmpty()){
		//     String mess=;
		//     Environment.(messTitle, mess);
		//}else{
		//    String mess= + "<html><b>"+s.substring(0, s.length()-6)+ "</br></b></html>." +"\n";
		//    mess+="<br>"++"</br>";
		//    Environment.(messTitle, mess);
		//}

		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		        showException(e);
		}catch(Exceptions::ServiceClientException  e){
		        showException(e);
		}
	}

	/*Radix::Reports.User::UserReport:General:Model:onCommand_Import-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:General:Model:onCommand_Import")
	protected  void onCommand_Import (org.radixware.ads.Reports.User.explorer.UserReportGroup.Import command) {
		java.io.File file = CfgManagement::DesktopUtils.openFileForImport(findNearestView(), command.getTitle());
		if (file == null)
		    return;

		try {
		    Meta::AdsDefXsd:AdsUserReportsExchangeDocument xDoc;
		    try {
		        xDoc = Meta::AdsDefXsd:AdsUserReportsExchangeDocument.Factory.parse(file);
		    } catch (Exceptions::XmlException ex) {
		        Meta::AdsDefXsd:AdsUserReportExchangeDocument x = Meta::AdsDefXsd:AdsUserReportExchangeDocument.Factory.parse(file);
		        xDoc = Meta::AdsDefXsd:AdsUserReportsExchangeDocument.Factory.newInstance();
		        xDoc.addNewAdsUserReportsExchange().ItemList.add(x.AdsUserReportExchange);
		    }

		    Explorer.Dialogs::ISelectEntityDialog dialog = Explorer.Dialogs::SelectEntityDialog.newInstance(Explorer.Models::GroupModel.openTableContextlessSelectorModel(getEnvironment(),
		    idof[UserReportModule],idof[UserReportModule:Select]), false);
		    if (dialog.execDialog() == Client.Views::DialogResult.ACCEPTED) {

		        UserReportModule module = (UserReportModule) dialog.getSelectedEntity();
		        this.contextModuleId.Value = module.guid.Value;

		        Common.Dlg::ClientUtils.viewImportResult(command.send(xDoc));
		        reread();
		    }
		} catch (Exceptions::Exception e) {
		    showException(e);
		}


	}
	public final class Import extends org.radixware.ads.Reports.User.explorer.UserReportGroup.Import{
		protected Import(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Import( this );
		}

	}

	public final class ValidateAll extends org.radixware.ads.Reports.User.explorer.UserReportGroup.ValidateAll{
		protected ValidateAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_validateAll( this );
		}

	}

	public final class CompileAll extends org.radixware.ads.Reports.User.explorer.UserReportGroup.CompileAll{
		protected CompileAll(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_compileAll( this );
		}

	}

















}

/* Radix::Reports.User::UserReport:General:Model - Desktop Meta*/

/*Radix::Reports.User::UserReport:General:Model-Group Model Class*/

package org.radixware.ads.Reports.User.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmM4F3WGRWD5DTDOI2VXY4IAWHTU"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports.User::UserReport:General:Model:Properties-Properties*/
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

/* Radix::Reports.User::UserReport:ByName:Model - Desktop Executable*/

/*Radix::Reports.User::UserReport:ByName:Model-Filter Model Class*/

package org.radixware.ads.Reports.User.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:ByName:Model")
public class ByName:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ByName:Model_mi.rdxMeta; }



	public ByName:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Reports.User::UserReport:ByName:Model:Nested classes-Nested Classes*/

	/*Radix::Reports.User::UserReport:ByName:Model:Properties-Properties*/








	/*Radix::Reports.User::UserReport:ByName:Model:Methods-Methods*/

	/*Radix::Reports.User::UserReport:ByName:name:name-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:ByName:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:ByName:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName(){return (Name)getProperty(prmBIO2MFW4VZBOTAQPDR3KZILWEY);}


}

/* Radix::Reports.User::UserReport:ByName:Model - Desktop Meta*/

/*Radix::Reports.User::UserReport:ByName:Model-Filter Model Class*/

package org.radixware.ads.Reports.User.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ByName:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcQMN6KMIMCZDIHECNONT6FLQVZE"),
						"ByName:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports.User::UserReport:ByName:Model:Properties-Properties*/
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

/* Radix::Reports.User::UserReport:ByName:Model - Web Executable*/

/*Radix::Reports.User::UserReport:ByName:Model-Filter Model Class*/

package org.radixware.ads.Reports.User.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:ByName:Model")
public class ByName:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return ByName:Model_mi.rdxMeta; }



	public ByName:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::Reports.User::UserReport:ByName:Model:Nested classes-Nested Classes*/

	/*Radix::Reports.User::UserReport:ByName:Model:Properties-Properties*/








	/*Radix::Reports.User::UserReport:ByName:Model:Methods-Methods*/

	/*Radix::Reports.User::UserReport:ByName:name:name-Presentation Property*/




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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:ByName:name:name")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Reports.User::UserReport:ByName:name:name")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Name getName(){return (Name)getProperty(prmBIO2MFW4VZBOTAQPDR3KZILWEY);}


}

/* Radix::Reports.User::UserReport:ByName:Model - Web Meta*/

/*Radix::Reports.User::UserReport:ByName:Model-Filter Model Class*/

package org.radixware.ads.Reports.User.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class ByName:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcQMN6KMIMCZDIHECNONT6FLQVZE"),
						"ByName:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Reports.User::UserReport:ByName:Model:Properties-Properties*/
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

/* Radix::Reports.User::UserReport - Localizing Bundle */
package org.radixware.ads.Reports.User.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UserReport - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Module");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EC5PETNQVBZFHST4LHP5INLVM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательский отчет");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2WLEGQCYE5CV3KNERRYIOUVVDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Description");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Описание");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47MPBDQI3VCBXFDHQWYKJ74DE4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По Имени");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4CHWY6D2HJH6VGZPLSYP77LLZ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6NQXJC625ZG6LMHNYFIAIE7GC4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Report like");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет как");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6OCFROOZJ5HVXFQLEXLTKYLQQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No errors found.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибок не найдено!");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJ6NVPBXQ5DDVMYG4KJZAX5IAQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Versions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBEKQZEITPRFDZKN6KEHJ2UEEOY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCEA36FLIYZEXBHS5V2GNRBMJX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Reports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательские отчеты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD4A2O434D5ENHPWYTNGH6OTT5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"See event log for more information");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Подробности в журнале событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF77ZGTWE4JG5LBW3OSDPDUCDQI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,null,"App",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validation...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Валидация...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGSTABJTCBREUPGYA6C5INFWW6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current version was not set");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущая версия не была установлена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSNHSTD7VBC57H4JGNYPQBGJYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Import");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Импортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIBHOD4GB5BEEHH2L4CDAGLD6EU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"There are errors!");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Отчет содержит ошибки! ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHXL2FJFR5BUTK6OK72UZGQQJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Change log for current version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Журнал изменений текущей версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIX7F2GEQBJDJXLOBTY4OI2KZ5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компилировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3H3IFZ2KJDHFI4FCOHQGIPIWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автор последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLZ6VFCY4V5GJRALTDLSSEXJO3Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Module ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Идентификатор модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMKUQVTGN45EEHDKA2ZCTYWBZ2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Export");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Экспортировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNZWNEMMO7VGWDEZYDI3KMYWAX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation failed. For details, see the event log.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция завершилась с ошибками. Подробности в журнале событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOL6PRDGYHJC7HPCBM4J5475FXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validate");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP2LHXXYTDRCRZN7D3VOCHW5D5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"For more information, please, open reports designer.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Для получения более подробной информации, пожалуйста, откройте дизайнер отчетов.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQOP62PHCMVBQHDPDOFJNMIWBYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,null,"App",null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка компиляции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQXEEVNLGNVDLHPSF4I2JNH43RA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR276AC2IVNBOPL7H36FWQG7WZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJKKPDB5TBAOPON7JVUHMX7CDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Compilation completed successfully");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Компиляция успешно завершена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSEB7HEHWTVDVVFN7TSW46STIDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"version ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"ид. версии");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSV5Z6VUQMRFLFPUQNUTUMXDDCE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущая версия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTGKGGCZBQBHWXAZ5QBRJ6AFMHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTISYLIAD3VEJZMOK2R4CTLFONU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Following reports have errors: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Следующие отчеты содержат ошибки: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUCJOLBARTVFY5KMSU2X2QIXMOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней модификации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNADDST4ZBB7RCTVQZGRDA2L2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validation complete!");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Валидация завершена!");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVOKQLYV53NHQBG2O5BDGLEUUPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Validation complete. ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка завершена. ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXYGUE7R6T5E6VNJYY4AWAUQERE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"No errors found!");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибок не найдено!");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYP7CAA4Q6VHBJDZLE5OHXAREMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(UserReport - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecRHH7SYO5I5EFRGYIBOSVUXKD7U"),"UserReport - Localizing Bundle",$$$items$$$);
}
