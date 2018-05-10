
/* Radix::System::Unit - Server Executable*/

/*Radix::System::Unit-Entity Class*/

package org.radixware.ads.System.server;

import java.util.Collection;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit")
public abstract published class Unit  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Unit_mi.rdxMeta;}

	/*Radix::System::Unit:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Properties-Properties*/

	/*Radix::System::Unit:dbTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:dbTraceProfile")
	public published  Str getDbTraceProfile() {
		return dbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:dbTraceProfile")
	public published   void setDbTraceProfile(Str val) {
		dbTraceProfile = val;
	}

	/*Radix::System::Unit:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::System::Unit:use-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:use")
	public published  Bool getUse() {
		return use;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:use")
	public published   void setUse(Bool val) {
		use = val;
	}

	/*Radix::System::Unit:type-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:type")
	public published  org.radixware.ads.System.common.UnitType getType() {
		return type;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:type")
	public published   void setType(org.radixware.ads.System.common.UnitType val) {
		type = val;
	}

	/*Radix::System::Unit:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::System::Unit:instanceStarted-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceStarted")
public published  Bool getInstanceStarted() {
	return instanceStarted;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceStarted")
public published   void setInstanceStarted(Bool val) {
	instanceStarted = val;
}

/*Radix::System::Unit:title-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:title")
public published  Str getTitle() {
	return title;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:title")
public published   void setTitle(Str val) {
	title = val;
}

/*Radix::System::Unit:started-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:started")
public published  Bool getStarted() {
	return started;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:started")
public published   void setStarted(Bool val) {
	started = val;
}

/*Radix::System::Unit:instanceId-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceId")
public published  Int getInstanceId() {
	return instanceId;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceId")
public published   void setInstanceId(Int val) {
	instanceId = val;
}

/*Radix::System::Unit:selfCheckTime-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTime")
public published  java.sql.Timestamp getSelfCheckTime() {
	return selfCheckTime;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTime")
public published   void setSelfCheckTime(java.sql.Timestamp val) {
	selfCheckTime = val;
}

/*Radix::System::Unit:instance-Parent Reference*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instance")
public published  org.radixware.ads.System.server.Instance getInstance() {
	return instance;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instance")
public published   void setInstance(org.radixware.ads.System.server.Instance val) {
	instance = val;
}

/*Radix::System::Unit:classTitle-Dynamic Property*/



protected Str classTitle=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classTitle")
public published  Str getClassTitle() {

	return getClassDefinitionTitle();

}

/*Radix::System::Unit:fileTraceProfile-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:fileTraceProfile")
public published  Str getFileTraceProfile() {
	return fileTraceProfile;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:fileTraceProfile")
public published   void setFileTraceProfile(Str val) {
	fileTraceProfile = val;
}

/*Radix::System::Unit:guiTraceProfile-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:guiTraceProfile")
public published  Str getGuiTraceProfile() {
	return guiTraceProfile;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:guiTraceProfile")
public published   void setGuiTraceProfile(Str val) {
	guiTraceProfile = val;
}

/*Radix::System::Unit:warning-Dynamic Property*/



protected Str warning=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:warning")
protected published  Str getWarning() {

	if (/*org.radixware.kernel.server.SrvRunParams.getIsDevelopmentMode() //!!!_!!!
	        || */org.radixware.kernel.common.utils.SystemPropUtils.getBooleanSystemProp("rdx.disable.auto.ports.check", false)) {
	    return null;
	}

	if (internal[warning] == null) {
	    internal[warning] = checkForConflicts();
	}
	return internal[warning];
}

/*Radix::System::Unit:highArteInstCountView-Dynamic Property*/



protected Int highArteInstCountView=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:highArteInstCountView")
public published  Int getHighArteInstCountView() {

	if(this instanceof Unit.Arte)
	   return ((Unit.Arte)this).highArteInstCount;
	return null;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:highArteInstCountView")
public published   void setHighArteInstCountView(Int val) {
	highArteInstCountView = val;
}

/*Radix::System::Unit:active-Dynamic Property*/



protected Bool active=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:active")
public published  Bool getActive() {

	return Boolean.TRUE.equals(started) && (selfCheckTime != null && System.currentTimeMillis() < selfCheckTime.Time + org.radixware.kernel.server.units.Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS);
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:active")
public published   void setActive(Bool val) {
	active = val;
}

/*Radix::System::Unit:systemId-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:systemId")
public published  Int getSystemId() {
	return systemId;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:systemId")
public published   void setSystemId(Int val) {
	systemId = val;
}

/*Radix::System::Unit:scpName-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scpName")
public published  Str getScpName() {
	return scpName;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scpName")
public published   void setScpName(Str val) {
	scpName = val;
}

/*Radix::System::Unit:scp-Parent Reference*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scp")
public published  org.radixware.ads.System.server.Scp getScp() {
	return scp;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scp")
public published   void setScp(org.radixware.ads.System.server.Scp val) {
	scp = val;
}

/*Radix::System::Unit:activityStatus-Dynamic Property*/



protected org.radixware.ads.System.common.ActivityStatus activityStatus=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:activityStatus")
public published  org.radixware.ads.System.common.ActivityStatus getActivityStatus() {

	if (Boolean.FALSE.equals(started)) {
	    if (Boolean.FALSE.equals(postponed)) {
	        return ActivityStatus:STOPPED;
	    }
	    return ActivityStatus:POSTPONED;
	} else {
	    final DateTime effCheckTime = effectiveSelfCheckTime;
	    if (Boolean.TRUE.equals(started) && (effCheckTime != null && sysdate.Time < effCheckTime.Time + org.radixware.kernel.server.units.Unit.DB_I_AM_ALIVE_TIMEOUT_MILLIS)) {
	        return ActivityStatus:RUNNING;
	    } else {
	        return ActivityStatus:INACTIVE;
	    }
	}
}

/*Radix::System::Unit:selfCheckTimeStr-Dynamic Property*/



protected Str selfCheckTimeStr=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeStr")
public published  Str getSelfCheckTimeStr() {

	return SystemServerUtils.calcSelfCheckTimeStr(effectiveSelfCheckTime, sysdate);
}

/*Radix::System::Unit:sysdate-Expression Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:sysdate")
public  java.sql.Timestamp getSysdate() {
	return sysdate;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:sysdate")
public   void setSysdate(java.sql.Timestamp val) {
	sysdate = val;
}

/*Radix::System::Unit:primaryUnitId-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnitId")
public published  Int getPrimaryUnitId() {
	return primaryUnitId;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnitId")
public published   void setPrimaryUnitId(Int val) {
	primaryUnitId = val;
}

/*Radix::System::Unit:primaryUnit-Parent Reference*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnit")
public published  org.radixware.ads.System.server.Unit getPrimaryUnit() {
	return primaryUnit;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnit")
public published   void setPrimaryUnit(org.radixware.ads.System.server.Unit val) {
	primaryUnit = val;
}

/*Radix::System::Unit:postponed-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:postponed")
public published  Bool getPostponed() {
	return postponed;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:postponed")
public published   void setPostponed(Bool val) {
	postponed = val;
}

/*Radix::System::Unit:aadcTestMode-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcTestMode")
public published  Bool getAadcTestMode() {
	return aadcTestMode;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcTestMode")
public published   void setAadcTestMode(Bool val) {
	aadcTestMode = val;
}

/*Radix::System::Unit:aadcMemberId-Parent Property*/






















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcMemberId")
protected published  Int getAadcMemberId() {
	return aadcMemberId;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcMemberId")
protected published   void setAadcMemberId(Int val) {
	aadcMemberId = val;
}

/*Radix::System::Unit:effectiveSelfCheckTime-Dynamic Property*/



protected java.sql.Timestamp effectiveSelfCheckTime=null;











@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:effectiveSelfCheckTime")
public published  java.sql.Timestamp getEffectiveSelfCheckTime() {

	DateTime result = null;
	if (selfCheckTimeMillis != null) {
	    result = new DateTime(selfCheckTimeMillis.longValue());
	}
	if (selfCheckTime != null && (result == null || selfCheckTime.Time > result.Time)) {
	    result = selfCheckTime;
	}
	DateTime dgTime = Arte::Arte.getInstance().getInstance().getAadcManager().getUnitSelfCheckTime(id.longValue());

	if (dgTime != null && (result == null || dgTime.Time > result.Time)) {
	    result = dgTime;
	}

	return result;
}

/*Radix::System::Unit:selfCheckTimeMillis-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeMillis")
public published  Int getSelfCheckTimeMillis() {
	return selfCheckTimeMillis;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeMillis")
public published   void setSelfCheckTimeMillis(Int val) {
	selfCheckTimeMillis = val;
}

/*Radix::System::Unit:rid-Column-Based Property*/
















@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:rid")
public published  Str getRid() {
	return rid;
}

@SuppressWarnings({"unused","unchecked"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:rid")
public published   void setRid(Str val) {
	rid = val;
}



































































































































































































/*Radix::System::Unit:Methods-Methods*/

/*Radix::System::Unit:loadByPK-System Method*/

@SuppressWarnings("unused")
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:loadByPK")
public static published  org.radixware.ads.System.server.Unit loadByPK (Int id, boolean checkExistance) {
final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
		if(id==null) return null;
		pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),id);
org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),pkValsMap);
	try{
	return (
	org.radixware.ads.System.server.Unit) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
return null;
}

}

/*Radix::System::Unit:restart-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:restart")
public published  org.radixware.schemas.systeminstancecontrol.RestartUnitRs restart () {
	InstanceControlServiceWsdl:RestartUnitDocument doc = InstanceControlServiceWsdl:RestartUnitDocument.Factory.newInstance();
	InstanceControlServiceXsd:RestartUnitRq rq = doc.addNewRestartUnit().addNewRestartUnitRq();
	rq.setUnitId(id.intValue());
	rq.User = Arte::Arte.getUserName();
	doc.set(invoke(doc, InstanceControlServiceWsdl:RestartUnitDocument.class));
	return doc.getRestartUnit().getRestartUnitRs();
}

/*Radix::System::Unit:start-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:start")
public published  org.radixware.schemas.systeminstancecontrol.StartUnitRs start () {
	InstanceControlServiceWsdl:StartUnitDocument doc = InstanceControlServiceWsdl:StartUnitDocument.Factory.newInstance();
	InstanceControlServiceXsd:StartUnitRq rq = doc.addNewStartUnit().addNewStartUnitRq();
	rq.setUnitId(id.intValue());
	rq.User = Arte::Arte.getUserName();
	doc.set(invoke(doc, InstanceControlServiceWsdl:StartUnitDocument.class));

	return doc.getStartUnit().getStartUnitRs();
}

/*Radix::System::Unit:invoke-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:invoke")
private final  org.apache.xmlbeans.XmlObject invoke (org.apache.xmlbeans.XmlObject rq, java.lang.Class<?> resultClass) {
	try {
	    return Arte::Arte.invokeInternalService(rq, resultClass,
	            ServiceUri:InstanceControlWsdl.getValue() + "#" + instanceId.toString(),
	            0, 30, null);
	} catch (Exceptions::ServiceCallFault e) {
	    throw new AppError(e.getMessage(), e);
	} catch (Exceptions::ServiceCallException e) {
	    throw new AppError(e.getMessage(), e);
	} catch (Exceptions::InterruptedException e) {
	    throw new AppError(e.getMessage(), e);
	} catch (Exceptions::ServiceCallTimeout e) {
	    throw new AppError(e.getMessage(), e);
	}
}

/*Radix::System::Unit:stop-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:stop")
public published  org.radixware.schemas.systeminstancecontrol.StopUnitRs stop () {
	InstanceControlServiceWsdl:StopUnitDocument doc = InstanceControlServiceWsdl:StopUnitDocument.Factory.newInstance();
	InstanceControlServiceXsd:StopUnitRq rq = doc.addNewStopUnit().addNewStopUnitRq();
	rq.setUnitId(id.intValue());
	rq.User = Arte::Arte.getUserName();
	doc.set(invoke(doc, InstanceControlServiceWsdl:StopUnitDocument.class));
	return doc.getStopUnit().getStopUnitRs();
}

/*Radix::System::Unit:onCommand_Start-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:onCommand_Start")
private final  org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument onCommand_Start (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument doc = org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument.Factory.newInstance();
	doc.addNewStartUnit().StartUnitRs = start();
	return doc;
}

/*Radix::System::Unit:onCommand_Restart-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:onCommand_Restart")
private final  org.radixware.schemas.systeminstancecontrol.RestartUnitRs onCommand_Restart (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	return restart();
}

/*Radix::System::Unit:onCommand_Stop-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:onCommand_Stop")
private final  org.radixware.schemas.systeminstancecontrol.StopUnitRs onCommand_Stop (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	return stop();
}

/*Radix::System::Unit:afterInit-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:afterInit")
protected  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
	super.afterInit(src, phase);
	started = false;
}

/*Radix::System::Unit:beforeCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:beforeCreate")
protected  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
	boolean res = super.beforeCreate(src);
	if(src != null) {
	    started = false;
	    selfCheckTime = null;
	}
	started = false;
	if (primaryUnit != null) {
	    type = primaryUnit.type;
	}
	return res;
}

/*Radix::System::Unit:beforeDelete-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:beforeDelete")
protected  boolean beforeDelete () {
	//let's check selfCheckTime
	boolean started = true;
	try {
	    java.sql.PreparedStatement st = Arte::Arte.getDbConnection().prepareStatement(
	        "select 1 from "+dbName[Radix::System::Unit]+" where "+dbName[id]+"=? and "+dbName[started]+" <> 0 and  "+dbName[selfCheckTime]+" + 5/24/60 > systimestamp"
	    );
	    try {
	        st.setLong(1,id.longValue());
	        java.sql.ResultSet rs = st.executeQuery();
	        try {
	            started = rs.next();
	        } finally {
	            rs.close();
	        }
	    }finally{
	        st.close();
	    }
	}catch (java.sql.SQLException ex){
	    throw new DatabaseError(ex);
	} 
	if (started)
	    throw new ServiceProcessClientFault(Utils::IEasFaultReasonEnum.ACCESS_VIOLATION.toString(), Str.format("Deletion of started unit '%s' is forbidden. Stop the unit first.", calcTitle()), null, null);

	return super.beforeDelete();
}

/*Radix::System::Unit:getUsedAddresses-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:getUsedAddresses")
protected abstract published  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses ();

/*Radix::System::Unit:beforeUpdate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:beforeUpdate")
protected published  boolean beforeUpdate () {
	if (super.beforeUpdate()) {
	    if (primaryUnit != null) {
	        type = primaryUnit.type;
	    }
	    return true;
	}
	return false;
}

/*Radix::System::Unit:checkForConflicts-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:checkForConflicts")
private final  Str checkForConflicts () {
	try {
	    Collection<AddressConflict> conflicts = AddressConflict.discoverUnitConflicts(this);
	    if (conflicts.isEmpty()) {
	        return null;
	    }
	    StringBuilder description = new StringBuilder();
	    description.append("Network settings of this unit have the following conflicts:");
	    for (AddressConflict conflict : conflicts) {
	        description.append("\n\t");
	        description.append(conflict.thisAddress.getAsStr());
	        description.append(" <-> ");
	        description.append(conflict.conflictedAddress.getAsStr());
	    }
	    return description.toString();
	} catch (Exceptions::Exception ex) {
	    return "Address conflicts check error: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex);
	}

}

/*Radix::System::Unit:afterCreate-User Method*/

@Override
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:afterCreate")
protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
	super.afterCreate(src);
}

/*Radix::System::Unit:onCommand_Move-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:onCommand_Move")
private final  void onCommand_Move (org.radixware.schemas.types.IntDocument input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	rereadAndLock(5, false);
	if (started == Boolean.TRUE) {
	    throw new AppError("Unable to move started unit");
	}
	instance = Instance.loadByPK(input.Int, true);
}

/*Radix::System::Unit:abort-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:abort")
public published  org.radixware.schemas.systeminstancecontrol.AbortUnitRs abort () {
	InstanceControlServiceWsdl:AbortUnitDocument doc = InstanceControlServiceWsdl:AbortUnitDocument.Factory.newInstance();
	InstanceControlServiceXsd:AbortUnitRq rq = doc.addNewAbortUnit().addNewAbortUnitRq();
	rq.setUnitId(id.intValue());
	rq.User = Arte::Arte.getUserName();
	doc.set(invoke(doc, InstanceControlServiceWsdl:AbortUnitDocument.class));
	return doc.getAbortUnit().getAbortUnitRs();
}

/*Radix::System::Unit:onCommand_Abort-Command Handler Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:onCommand_Abort")
private final  org.radixware.schemas.systeminstancecontrol.AbortUnitRs onCommand_Abort (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
	return abort();
}

/*Radix::System::Unit:loadByRid-User Method*/

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:loadByRid")
public static published  org.radixware.ads.System.server.Unit loadByRid (Str rid) {
	if (rid == null) {
	    return null;
	}
	try (UnitByRidCursor c = UnitByRidCursor.open(rid)) {
	    if (c.next()) {
	        return c.unit;
	    }
	}
	return null;
}







@Override
public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
	if(cmdId == cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI){
		onCommand_Move((org.radixware.schemas.types.IntDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.getAppCommandInputData(getClass().getClassLoader(),input,org.radixware.schemas.types.IntDocument.class),newPropValsById);
		return null;
	} else if(cmdId == cmdF3SRJU4KOXOBDCJRAALOMT5GDM){
		org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument result = onCommand_Start(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdJMVRZQQB3ZB7JHODY4HPMXLRCU){
		org.radixware.schemas.systeminstancecontrol.AbortUnitRs result = onCommand_Abort(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdQWDXKHRSO7OBDCJTAALOMT5GDM){
		org.radixware.schemas.systeminstancecontrol.RestartUnitRs result = onCommand_Restart(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else if(cmdId == cmdTZE2P7EKOXOBDCJRAALOMT5GDM){
		org.radixware.schemas.systeminstancecontrol.StopUnitRs result = onCommand_Stop(newPropValsById);
		if(result != null)
			output.set(result);
		return null;
	} else 
		return super.execCommand(cmdId,propId,input,newPropValsById,output);
}


}

/* Radix::System::Unit - Server Meta*/

/*Radix::System::Unit-Entity Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"Unit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::System::Unit:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
							/*Owner Class Name*/
							"Unit",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),
							/*Property presentations*/

							/*Radix::System::Unit:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::Unit:dbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::Unit:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:use:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:type:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:instanceStarted:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::Unit:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:started:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:instanceId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:instance:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262143,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Unit:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Unit:fileTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:guiTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:warning:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJB5ZUMTWZNHV7GGFCOAMFCDVEM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:highArteInstCountView:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCHMMSD634FCPND2N5ME5PT2URE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:active:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP2JMQNROX5GE5POX2SH6R2DLLA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:systemId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNDXRNLLLJB7XATBZI2J4D3VIQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:scpName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJI4SC4LI5ZBMVHI5I5NQQTF67U"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:scp:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFK3IJM2HNGF5N3YAIKN5FJKL4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Unit:activityStatus:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:sysdate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC4OXB2OQBCH7ILZYYR2MS4C4M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:primaryUnitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWMRBA2NRJFYJHNM6Z4QWWRN3Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:primaryUnit:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(66583529,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Unit:postponed:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:aadcTestMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:aadcMemberId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTI2T4DTR7BFPHNLDAG7GDQPRNE"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT)),

									/*Radix::System::Unit:effectiveSelfCheckTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIVDLGOE35NFR5KXIQDCU2E6YSI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:selfCheckTimeMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDANQKJ3N5A6FN6T33U2YP2WDU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Unit:rid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit:Start-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),"Start",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit:Stop-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),"Stop",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit:Restart-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),"Restart",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit:Move-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),"Move",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Unit:Abort-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJMVRZQQB3ZB7JHODY4HPMXLRCU"),"Abort",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::Unit:Started First-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtG5BDINHZA5HCJNIVSRPWSCCYMA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"Started First",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGDM6TB252VC2NJEOZGFHHTJ62Q"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),org.radixware.kernel.common.enums.EOrder.DESC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::Unit:By Id-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"By Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJO67MTZXDORDF72ABIFNQAABA"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>index(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:IndexDbName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::Unit:By Instance-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRIUIWDESO5D7RCZXEPV363HTTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"By Instance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKN6DGNDK5FRDG3TGI7R2GAV6M"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							new org.radixware.kernel.server.meta.presentations.RadFilterDef[]{

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::Unit:Id-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltPWNIPHPWINAI3J4YLBFMA2R7RE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"Id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7XZFKGOAFCVXCQQZW573P32MU"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmUMQDS6JAXZGALPSUVY3FTHOZMI\"/></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::Unit:Class-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUJ346JVHQRBBLLL6WYXECY7AJE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"Class",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ZV2K74IBVDLHPKB4SS6ZZ6AYQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmYGRKR4Y5SRHTLHGLS2YYZVBFHU\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmYGRKR4Y5SRHTLHGLS2YYZVBFHU\"/></xsc:Item><xsc:Item><xsc:Sql> is null)and \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm5FE7DXZSNJE2XAZOAZ4ODJMYRU\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm5FE7DXZSNJE2XAZOAZ4ODJMYRU\"/></xsc:Item><xsc:Item><xsc:Sql> is null) and\n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVX5YH2SS5VDBN2IAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMZGZZZWY7NDWTDTAPSZHXLQGYI\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMZGZZZWY7NDWTDTAPSZHXLQGYI\"/></xsc:Item><xsc:Item><xsc:Sql> is null)\n</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),true,null,false,"org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadFilterDef(
									/*Radix::System::Unit:UsedOrStarted-Filter*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("flt2FKMFBALG5HH5LNVUVARRVORXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"UsedOrStarted",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH2DJTIEYURCONMQMDMJYS2UJOQ"),"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVX5YH2SS5VDBN2IAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > 0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > 0</xsc:Sql></xsc:Item></xsc:Sqml>",null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>",org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),true,null,true,"org.radixware")
							},
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::Unit:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
									11442,
									null,

									/*Radix::System::Unit:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::Unit:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									2192,
									null,

									/*Radix::System::Unit:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::System::Unit:Edit:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFKKQIWDA35GHXKH6SKUDSCBNKE"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::Unit:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),true,null,true,org.radixware.kernel.common.types.Id.Factory.loadFrom("flt2FKMFBALG5HH5LNVUVARRVORXA"),true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(9120,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU")),

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::Unit:SelectStarted-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM474UPJLXTNRDOLKABIFNQAABA"),"SelectStarted",org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),18640,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql>=1</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3575,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::System::Unit:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::System::Unit:Default-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccKOGFFVPJAHOBDA26AAMPGXSZKU"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctIM3THFAESQAESFLHAAJZREMVEY"),null,300.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGWNL4MHWFBR3AQ5TUAQW4IDQE")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctC4I7K35XCFFEDEJ4E3TVGTP4G4"),null,305.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JC26JW46RHDFO4AT64MTWQTTY"))}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(8192,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::System::Unit:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::Unit:dbTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),"dbTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAIUVQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4YUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:use-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),"use",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZAIKPE76VHOBDCLSAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:type-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),"type",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF2TCBU6YX7NRDB6TAALOMT5GDM"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:instanceStarted-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),"instanceStarted",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDUVLLMPOXOBDCJRAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:started-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),"started",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2IUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:instanceId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),"instanceId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:selfCheckTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKOQ3VC7VDJCIDJEWHOMDGRDSYA"),"selfCheckTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:instance-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),"instance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refPDY4KBVSJ3NRDAQSABIFNQAAAE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQY4J3ZXDORDF72ABIFNQAABA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:fileTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),"fileTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCJODNMCP5AG5BRWYS5DDJ7EVQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:guiTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),"guiTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMOOPWBNC5HH5CS3UT5X6XE4ZY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:warning-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJB5ZUMTWZNHV7GGFCOAMFCDVEM"),"warning",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:highArteInstCountView-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCHMMSD634FCPND2N5ME5PT2URE"),"highArteInstCountView",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSHKLORNABFV5MPPJDYIUM3WPQ"),org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:active-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP2JMQNROX5GE5POX2SH6R2DLLA"),"active",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6WKFCHJZBE5NMV4LUYBI5ZFUY"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:systemId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNDXRNLLLJB7XATBZI2J4D3VIQ"),"systemId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:scpName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJI4SC4LI5ZBMVHI5I5NQQTF67U"),"scpName",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:scp-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFK3IJM2HNGF5N3YAIKN5FJKL4"),"scp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3BVFSS24OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refD2E6UYFGFNEYXPORRVSWRRC3RQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),true,null,new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:activityStatus-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),"activityStatus",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBEEDMNDLZA27MPPNT3FQBSXRI"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:selfCheckTimeStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),"selfCheckTimeStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMRHQK3OFVESXKPLLGHBGVK744"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:sysdate-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC4OXB2OQBCH7ILZYYR2MS4C4M"),"sysdate",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,"DATE",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>sysdate</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:primaryUnitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWMRBA2NRJFYJHNM6Z4QWWRN3Q"),"primaryUnitId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:primaryUnit-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),"primaryUnit",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LIIT6LQPZGP5D7EY74FKGFSIA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refI3HCWMKRANDRDA42ZXKFHOFLGY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:postponed-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),"postponed",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQT2GCQ3BUJDVVAYOEY2W3EBXGU"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:aadcTestMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),"aadcTestMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTTAI4YF4BF4JM7YDRRIP4XV4I"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:aadcMemberId-Parent Property*/

								new org.radixware.kernel.server.meta.clazzes.RadParentPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTI2T4DTR7BFPHNLDAG7GDQPRNE"),"aadcMemberId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN4LZUFJOJCNRNCO5N43FTGAAE"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:effectiveSelfCheckTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIVDLGOE35NFR5KXIQDCU2E6YSI"),"effectiveSelfCheckTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2LNGINRLPNFW5H6Y6C2S2GZ6YY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:selfCheckTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDANQKJ3N5A6FN6T33U2YP2WDU"),"selfCheckTimeMillis",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMO2NWZZG5FJHOVNHQDWGUCTAU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Unit:rid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),"rid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPXYJ4HQAZABPEQN6TR5FM2AMM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::System::Unit:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRXIEUHUE5FHKLNRHWALTRTOVOE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMRAPYGXPXBC5NHAEIEZKQDENHM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDZGGOB5YO7OBDCJUAALOMT5GDM"),"restart",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMI3GXXMUOXOBDCJRAALOMT5GDM"),"start",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPA5LHZMUOXOBDCJRAALOMT5GDM"),"invoke",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJGS2OWUDLFFQBD6L4VL3HWPYPA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprL6QIWKEVC5AUHNDY6YADHUYYQM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVQSDO6EVOXOBDCJRAALOMT5GDM"),"stop",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdF3SRJU4KOXOBDCJRAALOMT5GDM"),"onCommand_Start",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU6EQJ63APNFMTJ7M2IAIWEO3LM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdQWDXKHRSO7OBDCJTAALOMT5GDM"),"onCommand_Restart",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIW5YTZXUYRGEDAUYSS5I5COEB4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdTZE2P7EKOXOBDCJRAALOMT5GDM"),"onCommand_Stop",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprYCY46WVUPFGPDJ2NKYPVLYKICI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprARNBLTX3RFCRZNZR3IJRWJGVRU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVJSGC3N26BGMZN24FVUQQBSQLE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprT2YEE4MIZZG3LNMR6IDDOOQM3U"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQIF7CPRFE5GUTIY26PT5SYUD6E"),"getUsedAddresses",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEC4OQERVFVFFVNQZSDPOQPXPGY"),"checkForConflicts",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN2OEG45EDNBWBBOXZ4GPVBSF3U"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),"onCommand_Move",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("input",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHODOPTILHNCWNJ3VXZWCPXWWOA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUVIB642MQFA5BDVOIGXHOJOPQM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQJOYAXKNYNG5LKT7DU4JEH5WHI"),"abort",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdJMVRZQQB3ZB7JHODY4HPMXLRCU"),"onCommand_Abort",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWL4FF2HRAZARFLRPB657IWGK24"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKIMFIXCH6RD7RMAUG3EPCS3YWQ"),"loadByRid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB6PT77SDUBG5NMUZRAITAVLQOM"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::System::Unit - Desktop Executable*/

/*Radix::System::Unit-Entity Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit")
public interface Unit {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.System.explorer.Unit.Unit_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.explorer.Unit.Unit_DefaultModel )  super.getEntity(i);}
	}
























































































































































































	/*Radix::System::Unit:dbTraceProfile:dbTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::System::Unit:fileTraceProfile:fileTraceProfile-Presentation Property*/


	public class FileTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FileTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:fileTraceProfile:fileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:fileTraceProfile:fileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileTraceProfile getFileTraceProfile();
	/*Radix::System::Unit:sysdate:sysdate-Presentation Property*/


	public class Sysdate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public Sysdate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:sysdate:sysdate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:sysdate:sysdate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Sysdate getSysdate();
	/*Radix::System::Unit:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::Unit:use:use-Presentation Property*/


	public class Use extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Use(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:use:use")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:use:use")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Use getUse();
	/*Radix::System::Unit:type:type-Presentation Property*/


	public class Type extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Type(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.UnitType> getValClass(){
			return org.radixware.ads.System.common.UnitType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:type:type")
		public  org.radixware.ads.System.common.UnitType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:type:type")
		public   void setValue(org.radixware.ads.System.common.UnitType val) {
			Value = val;
		}
	}
	public Type getType();
	/*Radix::System::Unit:instanceStarted:instanceStarted-Presentation Property*/


	public class InstanceStarted extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public InstanceStarted(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceStarted:instanceStarted")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceStarted:instanceStarted")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public InstanceStarted getInstanceStarted();
	/*Radix::System::Unit:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::System::Unit:started:started-Presentation Property*/


	public class Started extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Started(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:started:started")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:started:started")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Started getStarted();
	/*Radix::System::Unit:scpName:scpName-Presentation Property*/


	public class ScpName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ScpName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scpName:scpName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scpName:scpName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ScpName getScpName();
	/*Radix::System::Unit:postponed:postponed-Presentation Property*/


	public class Postponed extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Postponed(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:postponed:postponed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:postponed:postponed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Postponed getPostponed();
	/*Radix::System::Unit:instanceId:instanceId-Presentation Property*/


	public class InstanceId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstanceId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceId:instanceId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceId:instanceId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstanceId getInstanceId();
	/*Radix::System::Unit:selfCheckTimeMillis:selfCheckTimeMillis-Presentation Property*/


	public class SelfCheckTimeMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SelfCheckTimeMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeMillis:selfCheckTimeMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeMillis:selfCheckTimeMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SelfCheckTimeMillis getSelfCheckTimeMillis();
	/*Radix::System::Unit:systemId:systemId-Presentation Property*/


	public class SystemId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SystemId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:systemId:systemId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:systemId:systemId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SystemId getSystemId();
	/*Radix::System::Unit:primaryUnit:primaryUnit-Presentation Property*/


	public class PrimaryUnit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public PrimaryUnit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Unit.Unit_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Unit.Unit_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Unit.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Unit.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnit:primaryUnit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnit:primaryUnit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PrimaryUnit getPrimaryUnit();
	/*Radix::System::Unit:scp:scp-Presentation Property*/


	public class Scp extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Scp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Scp.Scp_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Scp.Scp_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Scp.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Scp.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scp:scp")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scp:scp")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Scp getScp();
	/*Radix::System::Unit:aadcMemberId:aadcMemberId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::System::Unit:aadcTestMode:aadcTestMode-Presentation Property*/


	public class AadcTestMode extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AadcTestMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcTestMode:aadcTestMode")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcTestMode:aadcTestMode")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AadcTestMode getAadcTestMode();
	/*Radix::System::Unit:primaryUnitId:primaryUnitId-Presentation Property*/


	public class PrimaryUnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrimaryUnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnitId:primaryUnitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnitId:primaryUnitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrimaryUnitId getPrimaryUnitId();
	/*Radix::System::Unit:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::System::Unit:instance:instance-Presentation Property*/


	public class Instance extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Instance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Instance.Instance_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.Instance.Instance_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.Instance.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.Instance.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instance:instance")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instance:instance")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Instance getInstance();
	/*Radix::System::Unit:guiTraceProfile:guiTraceProfile-Presentation Property*/


	public class GuiTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public GuiTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:guiTraceProfile:guiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:guiTraceProfile:guiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public GuiTraceProfile getGuiTraceProfile();
	/*Radix::System::Unit:selfCheckTimeStr:selfCheckTimeStr-Presentation Property*/


	public class SelfCheckTimeStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SelfCheckTimeStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeStr:selfCheckTimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeStr:selfCheckTimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SelfCheckTimeStr getSelfCheckTimeStr();
	/*Radix::System::Unit:activityStatus:activityStatus-Presentation Property*/


	public class ActivityStatus extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ActivityStatus(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.ActivityStatus dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.ActivityStatus ? (org.radixware.ads.System.common.ActivityStatus)x : org.radixware.ads.System.common.ActivityStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.ActivityStatus> getValClass(){
			return org.radixware.ads.System.common.ActivityStatus.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.ActivityStatus dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.ActivityStatus ? (org.radixware.ads.System.common.ActivityStatus)x : org.radixware.ads.System.common.ActivityStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:activityStatus:activityStatus")
		public  org.radixware.ads.System.common.ActivityStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:activityStatus:activityStatus")
		public   void setValue(org.radixware.ads.System.common.ActivityStatus val) {
			Value = val;
		}
	}
	public ActivityStatus getActivityStatus();
	/*Radix::System::Unit:highArteInstCountView:highArteInstCountView-Presentation Property*/


	public class HighArteInstCountView extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighArteInstCountView(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:highArteInstCountView:highArteInstCountView")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:highArteInstCountView:highArteInstCountView")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighArteInstCountView getHighArteInstCountView();
	/*Radix::System::Unit:effectiveSelfCheckTime:effectiveSelfCheckTime-Presentation Property*/


	public class EffectiveSelfCheckTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public EffectiveSelfCheckTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public EffectiveSelfCheckTime getEffectiveSelfCheckTime();
	/*Radix::System::Unit:warning:warning-Presentation Property*/


	public class Warning extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Warning(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:warning:warning")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:warning:warning")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Warning getWarning();
	/*Radix::System::Unit:active:active-Presentation Property*/


	public class Active extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Active(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:active:active")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:active:active")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Active getActive();
	/*Radix::System::Unit:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	public static class Move extends org.radixware.kernel.common.client.models.items.Command{
		protected Move(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.types.IntDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class Start extends org.radixware.kernel.common.client.models.items.Command{
		protected Start(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument.class);
		}

	}

	public static class Abort extends org.radixware.kernel.common.client.models.items.Command{
		protected Abort(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.AbortUnitRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.AbortUnitRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.AbortUnitRs.class);
		}

	}

	public static class Restart extends org.radixware.kernel.common.client.models.items.Command{
		protected Restart(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.RestartUnitRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.RestartUnitRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.RestartUnitRs.class);
		}

	}

	public static class Stop extends org.radixware.kernel.common.client.models.items.Command{
		protected Stop(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.StopUnitRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.StopUnitRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.StopUnitRs.class);
		}

	}



}

/* Radix::System::Unit - Desktop Meta*/

/*Radix::System::Unit-Entity Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Unit:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			"Radix::System::Unit",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgA74ZLJZTSLVAOIIUHDQWNRXPXKP7XTZT"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),8192,

			/*Radix::System::Unit:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Unit:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAIUVQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Unit:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4YUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:use:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),
						"use",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZAIKPE76VHOBDCLSAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:use:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:type:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),
						"type",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),
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

						/*Radix::System::Unit:type:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPVU3ZQLQB5CMFEEPH7S4LO67EA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:instanceStarted:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),
						"instanceStarted",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDUVLLMPOXOBDCJRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						0,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),
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

						/*Radix::System::Unit:instanceStarted:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:started:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),
						"started",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2IUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit:started:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:instanceId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),
						"instanceId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:instanceId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:instance:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),
						"instance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::Unit:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQY4J3ZXDORDF72ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						28,
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

						/*Radix::System::Unit:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:fileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),
						"fileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCJODNMCP5AG5BRWYS5DDJ7EVQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::System::Unit:fileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:guiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),
						"guiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMOOPWBNC5HH5CS3UT5X6XE4ZY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						false,false,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeF3QUTT7XY3NRDB6ZAALOMT5GDM"),
						true,

						/*Radix::System::Unit:guiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:warning:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJB5ZUMTWZNHV7GGFCOAMFCDVEM"),
						"warning",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
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

						/*Radix::System::Unit:warning:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:highArteInstCountView:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCHMMSD634FCPND2N5ME5PT2URE"),
						"highArteInstCountView",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSHKLORNABFV5MPPJDYIUM3WPQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:highArteInstCountView:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:active:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP2JMQNROX5GE5POX2SH6R2DLLA"),
						"active",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6WKFCHJZBE5NMV4LUYBI5ZFUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:active:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:systemId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNDXRNLLLJB7XATBZI2J4D3VIQ"),
						"systemId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:systemId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:scpName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJI4SC4LI5ZBMVHI5I5NQQTF67U"),
						"scpName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:scpName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:scp:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFK3IJM2HNGF5N3YAIKN5FJKL4"),
						"scp",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
						true,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCYL6KXWUFB4NHOB52WVVZGNUI"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::System::Unit:activityStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),
						"activityStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBEEDMNDLZA27MPPNT3FQBSXRI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),
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

						/*Radix::System::Unit:activityStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),
						"selfCheckTimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMRHQK3OFVESXKPLLGHBGVK744"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:selfCheckTimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:sysdate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC4OXB2OQBCH7ILZYYR2MS4C4M"),
						"sysdate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::System::Unit:sysdate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:primaryUnitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWMRBA2NRJFYJHNM6Z4QWWRN3Q"),
						"primaryUnitId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:primaryUnitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:primaryUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),
						"primaryUnit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LIIT6LQPZGP5D7EY74FKGFSIA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						66583529,
						66584555,false),

					/*Radix::System::Unit:postponed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),
						"postponed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQT2GCQ3BUJDVVAYOEY2W3EBXGU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:postponed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:aadcTestMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),
						"aadcTestMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTTAI4YF4BF4JM7YDRRIP4XV4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:aadcTestMode:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ISOXL24AREL3PGGOF6GGWTM44"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTI2T4DTR7BFPHNLDAG7GDQPRNE"),
						"aadcMemberId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						63,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),
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

						/*Radix::System::Instance:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,2L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:effectiveSelfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIVDLGOE35NFR5KXIQDCU2E6YSI"),
						"effectiveSelfCheckTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2LNGINRLPNFW5H6Y6C2S2GZ6YY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::System::Unit:effectiveSelfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:selfCheckTimeMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDANQKJ3N5A6FN6T33U2YP2WDU"),
						"selfCheckTimeMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMO2NWZZG5FJHOVNHQDWGUCTAU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:selfCheckTimeMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPXYJ4HQAZABPEQN6TR5FM2AMM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit:Start-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),
						"Start",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTPQRW4KOXOBDCJRAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEDD52J2QVZFSWWMP4HETHJ7KRTNGJ2PX"),
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
					/*Radix::System::Unit:Stop-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),
						"Stop",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBE2P7EKOXOBDCJRAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTKXKTEYVYAYQDB4RIFDVF4MH2E4QJX5V"),
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
					/*Radix::System::Unit:Restart-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),
						"Restart",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDOIPIBWRTOBDCKLAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgNRNHYK7EL5IILUTWP47R4JW6M5LWTGGH"),
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
					/*Radix::System::Unit:Move-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),
						"Move",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSXTWJ3RUZHWJEQ5BPNOEEPHQE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgFRKJIAKNZBH6VGR35C34FE5HCU"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit:Abort-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJMVRZQQB3ZB7JHODY4HPMXLRCU"),
						"Abort",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKEZQLBI65GA3FCR4A5YL6ZOAY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgLPHDE5DRRVFSVHHZKVUR3M24CM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::Unit:Id-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltPWNIPHPWINAI3J4YLBFMA2R7RE"),
						"Id",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7XZFKGOAFCVXCQQZW573P32MU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmUMQDS6JAXZGALPSUVY3FTHOZMI\"/></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmUMQDS6JAXZGALPSUVY3FTHOZMI"),
								"pId",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU5HC4KNQRNCLLLN2AXDJEHQUEE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::System::Unit:Id:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::Unit:Class-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUJ346JVHQRBBLLL6WYXECY7AJE"),
						"Class",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ZV2K74IBVDLHPKB4SS6ZZ6AYQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmYGRKR4Y5SRHTLHGLS2YYZVBFHU\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmYGRKR4Y5SRHTLHGLS2YYZVBFHU\"/></xsc:Item><xsc:Item><xsc:Sql> is null)and \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm5FE7DXZSNJE2XAZOAZ4ODJMYRU\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm5FE7DXZSNJE2XAZOAZ4ODJMYRU\"/></xsc:Item><xsc:Item><xsc:Sql> is null) and\n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVX5YH2SS5VDBN2IAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMZGZZZWY7NDWTDTAPSZHXLQGYI\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMZGZZZWY7NDWTDTAPSZHXLQGYI\"/></xsc:Item><xsc:Item><xsc:Sql> is null)\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmYGRKR4Y5SRHTLHGLS2YYZVBFHU"),
								"pType",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKN3LYXQ3VJCS5KTCTMLKGPCUII"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUCKLUWFM4BDTXIACJFCENKZ5ZU"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMZGZZZWY7NDWTDTAPSZHXLQGYI"),
								"pUse",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsED5K4INSLRGKFILBK4XCCBH5GY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.BOOL,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBLUWCXKRRFG37KBXEHYCI4VKNA"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm5FE7DXZSNJE2XAZOAZ4ODJMYRU"),
								"pStarted",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XA75MAWXZGJ3AN3IOJ42EZEOQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.BOOL,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEHFDPIEIT5EFJCGYHO72ZXENN4"),
								false,
								null,
								null)
						},

						/*Radix::System::Unit:Class:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::Unit:Class:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAFPO7VLWFRG25KEH76KFH5Q25A"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmYGRKR4Y5SRHTLHGLS2YYZVBFHU"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMZGZZZWY7NDWTDTAPSZHXLQGYI"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm5FE7DXZSNJE2XAZOAZ4ODJMYRU"),2,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAFPO7VLWFRG25KEH76KFH5Q25A"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::Unit:UsedOrStarted-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt2FKMFBALG5HH5LNVUVARRVORXA"),
						"UsedOrStarted",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH2DJTIEYURCONMQMDMJYS2UJOQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVX5YH2SS5VDBN2IAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > 0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > 0</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						null,

						/*Radix::System::Unit:UsedOrStarted:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Unit:Started First-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtG5BDINHZA5HCJNIVSRPWSCCYMA"),
						"Started First",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGDM6TB252VC2NJEOZGFHHTJ62Q"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Unit:By Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						"By Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJO67MTZXDORDF72ABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Unit:By Instance-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRIUIWDESO5D7RCZXEPV363HTTQ"),
						"By Instance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKN6DGNDK5FRDG3TGI7R2GAV6M"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refD2E6UYFGFNEYXPORRVSWRRC3RQ"),"Unit=>Scp (scpName=>name, systemId=>systemId)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJI4SC4LI5ZBMVHI5I5NQQTF67U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNDXRNLLLJB7XATBZI2J4D3VIQ")},new String[]{"scpName","systemId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colI4QSJ5PEHJWDRPOYAAYQQ2Y3GB"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUQ25PISLDNBDJA6ACQMTAIZT4")},new String[]{"name","systemId"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refI3HCWMKRANDRDA42ZXKFHOFLGY"),"Unit=>Unit (primaryUnitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWMRBA2NRJFYJHNM6Z4QWWRN3Q")},new String[]{"primaryUnitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPDY4KBVSJ3NRDAQSABIFNQAAAE"),"Unit=>Instance (instanceId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA")},new String[]{"instanceId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM474UPJLXTNRDOLKABIFNQAABA")},
			true,true,false);
}

/* Radix::System::Unit - Web Executable*/

/*Radix::System::Unit-Entity Class*/

package org.radixware.ads.System.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit")
public interface Unit {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.System.web.Unit.Unit_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.web.Unit.Unit_DefaultModel )  super.getEntity(i);}
	}
























































































































































































	/*Radix::System::Unit:dbTraceProfile:dbTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::System::Unit:fileTraceProfile:fileTraceProfile-Presentation Property*/


	public class FileTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public FileTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:fileTraceProfile:fileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:fileTraceProfile:fileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileTraceProfile getFileTraceProfile();
	/*Radix::System::Unit:sysdate:sysdate-Presentation Property*/


	public class Sysdate extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public Sysdate(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:sysdate:sysdate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:sysdate:sysdate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Sysdate getSysdate();
	/*Radix::System::Unit:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::Unit:use:use-Presentation Property*/


	public class Use extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Use(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:use:use")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:use:use")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Use getUse();
	/*Radix::System::Unit:type:type-Presentation Property*/


	public class Type extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Type(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.UnitType> getValClass(){
			return org.radixware.ads.System.common.UnitType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:type:type")
		public  org.radixware.ads.System.common.UnitType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:type:type")
		public   void setValue(org.radixware.ads.System.common.UnitType val) {
			Value = val;
		}
	}
	public Type getType();
	/*Radix::System::Unit:instanceStarted:instanceStarted-Presentation Property*/


	public class InstanceStarted extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public InstanceStarted(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceStarted:instanceStarted")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceStarted:instanceStarted")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public InstanceStarted getInstanceStarted();
	/*Radix::System::Unit:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::System::Unit:started:started-Presentation Property*/


	public class Started extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Started(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:started:started")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:started:started")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Started getStarted();
	/*Radix::System::Unit:scpName:scpName-Presentation Property*/


	public class ScpName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ScpName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scpName:scpName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scpName:scpName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ScpName getScpName();
	/*Radix::System::Unit:postponed:postponed-Presentation Property*/


	public class Postponed extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Postponed(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:postponed:postponed")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:postponed:postponed")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Postponed getPostponed();
	/*Radix::System::Unit:instanceId:instanceId-Presentation Property*/


	public class InstanceId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public InstanceId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceId:instanceId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instanceId:instanceId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstanceId getInstanceId();
	/*Radix::System::Unit:selfCheckTimeMillis:selfCheckTimeMillis-Presentation Property*/


	public class SelfCheckTimeMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SelfCheckTimeMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeMillis:selfCheckTimeMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeMillis:selfCheckTimeMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SelfCheckTimeMillis getSelfCheckTimeMillis();
	/*Radix::System::Unit:systemId:systemId-Presentation Property*/


	public class SystemId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SystemId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:systemId:systemId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:systemId:systemId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SystemId getSystemId();
	/*Radix::System::Unit:primaryUnit:primaryUnit-Presentation Property*/


	public class PrimaryUnit extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public PrimaryUnit(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Unit.Unit_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Unit.Unit_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Unit.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Unit.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnit:primaryUnit")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnit:primaryUnit")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public PrimaryUnit getPrimaryUnit();
	/*Radix::System::Unit:scp:scp-Presentation Property*/


	public class Scp extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Scp(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Scp.Scp_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Scp.Scp_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Scp.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Scp.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scp:scp")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:scp:scp")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Scp getScp();
	/*Radix::System::Unit:aadcMemberId:aadcMemberId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::System::Unit:aadcTestMode:aadcTestMode-Presentation Property*/


	public class AadcTestMode extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AadcTestMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcTestMode:aadcTestMode")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:aadcTestMode:aadcTestMode")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AadcTestMode getAadcTestMode();
	/*Radix::System::Unit:primaryUnitId:primaryUnitId-Presentation Property*/


	public class PrimaryUnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PrimaryUnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnitId:primaryUnitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:primaryUnitId:primaryUnitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PrimaryUnitId getPrimaryUnitId();
	/*Radix::System::Unit:rid:rid-Presentation Property*/


	public class Rid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Rid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::System::Unit:instance:instance-Presentation Property*/


	public class Instance extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public Instance(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.Instance.Instance_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.Instance.Instance_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.Instance.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.Instance.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instance:instance")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:instance:instance")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Instance getInstance();
	/*Radix::System::Unit:guiTraceProfile:guiTraceProfile-Presentation Property*/


	public class GuiTraceProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public GuiTraceProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:guiTraceProfile:guiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:guiTraceProfile:guiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public GuiTraceProfile getGuiTraceProfile();
	/*Radix::System::Unit:selfCheckTimeStr:selfCheckTimeStr-Presentation Property*/


	public class SelfCheckTimeStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SelfCheckTimeStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeStr:selfCheckTimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:selfCheckTimeStr:selfCheckTimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SelfCheckTimeStr getSelfCheckTimeStr();
	/*Radix::System::Unit:activityStatus:activityStatus-Presentation Property*/


	public class ActivityStatus extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ActivityStatus(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.ActivityStatus dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.ActivityStatus ? (org.radixware.ads.System.common.ActivityStatus)x : org.radixware.ads.System.common.ActivityStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.ActivityStatus> getValClass(){
			return org.radixware.ads.System.common.ActivityStatus.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.ActivityStatus dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.ActivityStatus ? (org.radixware.ads.System.common.ActivityStatus)x : org.radixware.ads.System.common.ActivityStatus.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:activityStatus:activityStatus")
		public  org.radixware.ads.System.common.ActivityStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:activityStatus:activityStatus")
		public   void setValue(org.radixware.ads.System.common.ActivityStatus val) {
			Value = val;
		}
	}
	public ActivityStatus getActivityStatus();
	/*Radix::System::Unit:highArteInstCountView:highArteInstCountView-Presentation Property*/


	public class HighArteInstCountView extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighArteInstCountView(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:highArteInstCountView:highArteInstCountView")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:highArteInstCountView:highArteInstCountView")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighArteInstCountView getHighArteInstCountView();
	/*Radix::System::Unit:effectiveSelfCheckTime:effectiveSelfCheckTime-Presentation Property*/


	public class EffectiveSelfCheckTime extends org.radixware.kernel.common.client.models.items.properties.PropertyDateTime{
		public EffectiveSelfCheckTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public EffectiveSelfCheckTime getEffectiveSelfCheckTime();
	/*Radix::System::Unit:warning:warning-Presentation Property*/


	public class Warning extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Warning(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:warning:warning")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:warning:warning")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Warning getWarning();
	/*Radix::System::Unit:active:active-Presentation Property*/


	public class Active extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Active(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:active:active")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:active:active")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Active getActive();
	/*Radix::System::Unit:classTitle:classTitle-Presentation Property*/


	public class ClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	public static class Move extends org.radixware.kernel.common.client.models.items.Command{
		protected Move(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.schemas.types.IntDocument input ) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,input);
			processResponse(response, null);
		}

	}

	public static class Start extends org.radixware.kernel.common.client.models.items.Command{
		protected Start(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrolWsdl.StartUnitDocument.class);
		}

	}

	public static class Abort extends org.radixware.kernel.common.client.models.items.Command{
		protected Abort(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.AbortUnitRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.AbortUnitRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.AbortUnitRs.class);
		}

	}

	public static class Restart extends org.radixware.kernel.common.client.models.items.Command{
		protected Restart(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.RestartUnitRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.RestartUnitRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.RestartUnitRs.class);
		}

	}

	public static class Stop extends org.radixware.kernel.common.client.models.items.Command{
		protected Stop(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.StopUnitRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.StopUnitRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.StopUnitRs.class);
		}

	}



}

/* Radix::System::Unit - Web Meta*/

/*Radix::System::Unit-Entity Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Unit:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
			"Radix::System::Unit",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgA74ZLJZTSLVAOIIUHDQWNRXPXKP7XTZT"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREUFQOK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),8192,

			/*Radix::System::Unit:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Unit:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAIUVQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Unit:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4YUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:use:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),
						"use",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZAIKPE76VHOBDCLSAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:use:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:type:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),
						"type",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),
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

						/*Radix::System::Unit:type:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciPVU3ZQLQB5CMFEEPH7S4LO67EA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:instanceStarted:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),
						"instanceStarted",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDUVLLMPOXOBDCJRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						0,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),
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

						/*Radix::System::Unit:instanceStarted:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:started:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),
						"started",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2IUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Unit:started:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:instanceId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),
						"instanceId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:instanceId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:instance:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),
						"instance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMUFQOK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},
						null,
						null,
						262143,
						262143,false),

					/*Radix::System::Unit:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQY4J3ZXDORDF72ABIFNQAABA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						28,
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

						/*Radix::System::Unit:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:fileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),
						"fileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCJODNMCP5AG5BRWYS5DDJ7EVQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						false,false,
						false,
						true,
						null,
						true,

						/*Radix::System::Unit:fileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:guiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),
						"guiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMOOPWBNC5HH5CS3UT5X6XE4ZY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						false,false,
						false,
						true,
						null,
						true,

						/*Radix::System::Unit:guiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:warning:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJB5ZUMTWZNHV7GGFCOAMFCDVEM"),
						"warning",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						null,
						true,
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

						/*Radix::System::Unit:warning:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:highArteInstCountView:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdCHMMSD634FCPND2N5ME5PT2URE"),
						"highArteInstCountView",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSHKLORNABFV5MPPJDYIUM3WPQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:highArteInstCountView:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:active:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP2JMQNROX5GE5POX2SH6R2DLLA"),
						"active",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6WKFCHJZBE5NMV4LUYBI5ZFUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:active:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:systemId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNDXRNLLLJB7XATBZI2J4D3VIQ"),
						"systemId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:systemId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:scpName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJI4SC4LI5ZBMVHI5I5NQQTF67U"),
						"scpName",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:scpName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:scp:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFK3IJM2HNGF5N3YAIKN5FJKL4"),
						"scp",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						21,
						null,
						null,
						true,
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCYL6KXWUFB4NHOB52WVVZGNUI"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::System::Unit:activityStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),
						"activityStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBEEDMNDLZA27MPPNT3FQBSXRI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),
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

						/*Radix::System::Unit:activityStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),
						"selfCheckTimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMRHQK3OFVESXKPLLGHBGVK744"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:selfCheckTimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:sysdate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDC4OXB2OQBCH7ILZYYR2MS4C4M"),
						"sysdate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::System::Unit:sysdate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:primaryUnitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWMRBA2NRJFYJHNM6Z4QWWRN3Q"),
						"primaryUnitId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:primaryUnitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:primaryUnit:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),
						"primaryUnit",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LIIT6LQPZGP5D7EY74FKGFSIA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
						null,
						null,
						66583529,
						66584555,false),

					/*Radix::System::Unit:postponed:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),
						"postponed",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQT2GCQ3BUJDVVAYOEY2W3EBXGU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:postponed:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:aadcTestMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),
						"aadcTestMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTTAI4YF4BF4JM7YDRRIP4XV4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:aadcTestMode:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ISOXL24AREL3PGGOF6GGWTM44"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTI2T4DTR7BFPHNLDAG7GDQPRNE"),
						"aadcMemberId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.PARENT_PROP,
						63,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),
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

						/*Radix::System::Instance:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,2L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:effectiveSelfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIVDLGOE35NFR5KXIQDCU2E6YSI"),
						"effectiveSelfCheckTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2LNGINRLPNFW5H6Y6C2S2GZ6YY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.DATE_TIME,
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

						/*Radix::System::Unit:effectiveSelfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:selfCheckTimeMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKDANQKJ3N5A6FN6T33U2YP2WDU"),
						"selfCheckTimeMillis",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMO2NWZZG5FJHOVNHQDWGUCTAU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:selfCheckTimeMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Unit:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPXYJ4HQAZABPEQN6TR5FM2AMM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Unit:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit:Start-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdF3SRJU4KOXOBDCJRAALOMT5GDM"),
						"Start",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTPQRW4KOXOBDCJRAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgEDD52J2QVZFSWWMP4HETHJ7KRTNGJ2PX"),
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
					/*Radix::System::Unit:Stop-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdTZE2P7EKOXOBDCJRAALOMT5GDM"),
						"Stop",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBE2P7EKOXOBDCJRAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTKXKTEYVYAYQDB4RIFDVF4MH2E4QJX5V"),
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
					/*Radix::System::Unit:Restart-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdQWDXKHRSO7OBDCJTAALOMT5GDM"),
						"Restart",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDOIPIBWRTOBDCKLAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgNRNHYK7EL5IILUTWP47R4JW6M5LWTGGH"),
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
					/*Radix::System::Unit:Move-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdCJC7DXYHFJGQLPCZGJ6WHNYPBI"),
						"Move",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSXTWJ3RUZHWJEQ5BPNOEEPHQE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgFRKJIAKNZBH6VGR35C34FE5HCU"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Unit:Abort-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJMVRZQQB3ZB7JHODY4HPMXLRCU"),
						"Abort",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKEZQLBI65GA3FCR4A5YL6ZOAY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgLPHDE5DRRVFSVHHZKVUR3M24CM"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			new org.radixware.kernel.common.client.meta.filters.RadFilterDef[]{

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::Unit:Id-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltPWNIPHPWINAI3J4YLBFMA2R7RE"),
						"Id",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7XZFKGOAFCVXCQQZW573P32MU"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"tbl5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVLUYADHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmUMQDS6JAXZGALPSUVY3FTHOZMI\"/></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmUMQDS6JAXZGALPSUVY3FTHOZMI"),
								"pId",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU5HC4KNQRNCLLLN2AXDJEHQUEE"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
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

						/*Radix::System::Unit:Id:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::Unit:Class-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("fltUJ346JVHQRBBLLL6WYXECY7AJE"),
						"Class",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ZV2K74IBVDLHPKB4SS6ZZ6AYQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEWJT6TIHR5VDBNSIAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmYGRKR4Y5SRHTLHGLS2YYZVBFHU\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmYGRKR4Y5SRHTLHGLS2YYZVBFHU\"/></xsc:Item><xsc:Item><xsc:Sql> is null)and \n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm5FE7DXZSNJE2XAZOAZ4ODJMYRU\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prm5FE7DXZSNJE2XAZOAZ4ODJMYRU\"/></xsc:Item><xsc:Item><xsc:Sql> is null) and\n(</xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVX5YH2SS5VDBN2IAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMZGZZZWY7NDWTDTAPSZHXLQGYI\"/></xsc:Item><xsc:Item><xsc:Sql> or </xsc:Sql></xsc:Item><xsc:Item><xsc:Parameter ParamId=\"prmMZGZZZWY7NDWTDTAPSZHXLQGYI\"/></xsc:Item><xsc:Item><xsc:Sql> is null)\n</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef[]{

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmYGRKR4Y5SRHTLHGLS2YYZVBFHU"),
								"pType",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKN3LYXQ3VJCS5KTCTMLKGPCUII"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.INT,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("acsLSG35D4GIHNRDJIEACQMTAIZT4"),
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUCKLUWFM4BDTXIACJFCENKZ5ZU"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMZGZZZWY7NDWTDTAPSZHXLQGYI"),
								"pUse",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsED5K4INSLRGKFILBK4XCCBH5GY"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.BOOL,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBLUWCXKRRFG37KBXEHYCI4VKNA"),
								false,
								null,
								null),

								new org.radixware.kernel.common.client.meta.filters.RadFilterParamDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm5FE7DXZSNJE2XAZOAZ4ODJMYRU"),
								"pStarted",
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XA75MAWXZGJ3AN3IOJ42EZEOQ"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
								null,
								org.radixware.kernel.common.enums.EValType.BOOL,
								null,
								null,
								false,
								false,
								false,
								null,
								false,
								null,
								org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEHFDPIEIT5EFJCGYHO72ZXENN4"),
								false,
								null,
								null)
						},

						/*Radix::System::Unit:Class:Editor Pages-Filter Pages*/
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

								/*Radix::System::Unit:Class:Main-Editor Page*/

								 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAFPO7VLWFRG25KEH76KFH5Q25A"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmYGRKR4Y5SRHTLHGLS2YYZVBFHU"),0,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prmMZGZZZWY7NDWTDTAPSZHXLQGYI"),1,0,1,false,false),

										new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prm5FE7DXZSNJE2XAZOAZ4ODJMYRU"),2,0,1,false,false)
								},null)
						},
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
							new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgAFPO7VLWFRG25KEH76KFH5Q25A"))}
						,
						null),

					new org.radixware.kernel.common.client.meta.filters.RadFilterDef(
					/*Radix::System::Unit:UsedOrStarted-Filter*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("flt2FKMFBALG5HH5LNVUVARRVORXA"),
						"UsedOrStarted",
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH2DJTIEYURCONMQMDMJYS2UJOQ"),
						"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colEVX5YH2SS5VDBN2IAAUMFADAIA\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > 0 or </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec5HP4XTP3EGWDBRCRAAIT4AGD7E\" PropId=\"colIREKNEWHBDWDRD25AAYQQMVFBB\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> > 0</xsc:Sql></xsc:Item></xsc:Sqml>",
						null,
						null,
						true,
						true,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						null,

						/*Radix::System::Unit:UsedOrStarted:Editor Pages-Filter Pages*/
						null,
						new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
						}
						,
						null)
			},
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Unit:Started First-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtG5BDINHZA5HCJNIVSRPWSCCYMA"),
						"Started First",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGDM6TB252VC2NJEOZGFHHTJ62Q"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Unit:By Id-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
						"By Id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJO67MTZXDORDF72ABIFNQAABA"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Unit:By Instance-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRIUIWDESO5D7RCZXEPV363HTTQ"),
						"By Instance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKN6DGNDK5FRDG3TGI7R2GAV6M"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refD2E6UYFGFNEYXPORRVSWRRC3RQ"),"Unit=>Scp (scpName=>name, systemId=>systemId)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colJI4SC4LI5ZBMVHI5I5NQQTF67U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colMNDXRNLLLJB7XATBZI2J4D3VIQ")},new String[]{"scpName","systemId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colI4QSJ5PEHJWDRPOYAAYQQ2Y3GB"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUQ25PISLDNBDJA6ACQMTAIZT4")},new String[]{"name","systemId"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refI3HCWMKRANDRDA42ZXKFHOFLGY"),"Unit=>Unit (primaryUnitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colUWMRBA2NRJFYJHNM6Z4QWWRN3Q")},new String[]{"primaryUnitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPDY4KBVSJ3NRDAQSABIFNQAAAE"),"Unit=>Instance (instanceId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA")},new String[]{"instanceId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM474UPJLXTNRDOLKABIFNQAABA")},
			true,true,false);
}

/* Radix::System::Unit:Create - Desktop Meta*/

/*Radix::System::Unit:Create-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit:Create:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::Unit:Create:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QOKJZDYXDORDF72ABIFNQAABA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),0,1,1,false,false)
			},null)
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA"))}
	,

	/*Radix::System::Unit:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	11442,0,0);
}
/* Radix::System::Unit:Create - Web Meta*/

/*Radix::System::Unit:Create-Editor Presentation*/

package org.radixware.ads.System.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit:Create:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::System::Unit:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	11442,0,0);
}
/* Radix::System::Unit:Create:Model - Desktop Executable*/

/*Radix::System::Unit:Create:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Create:Model")
public class Create:Model  extends org.radixware.ads.System.explorer.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit:Create:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Create:Model:Properties-Properties*/

	/*Radix::System::Unit:Create:Model:Methods-Methods*/


}

/* Radix::System::Unit:Create:Model - Desktop Meta*/

/*Radix::System::Unit:Create:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemS6PW3X2EKJFUNPGCXYDBACXY7A"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Create:Model:Properties-Properties*/
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

/* Radix::System::Unit:Create:Model - Web Executable*/

/*Radix::System::Unit:Create:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Create:Model")
public class Create:Model  extends org.radixware.ads.System.web.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit:Create:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Create:Model:Properties-Properties*/

	/*Radix::System::Unit:Create:Model:Methods-Methods*/


}

/* Radix::System::Unit:Create:Model - Web Meta*/

/*Radix::System::Unit:Create:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemS6PW3X2EKJFUNPGCXYDBACXY7A"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Create:Model:Properties-Properties*/
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

/* Radix::System::Unit:Edit - Desktop Meta*/

/*Radix::System::Unit:Edit-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::Unit:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDIUVQOK4OXOBDFKUAAMPGXUWTQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5NZZOJBXTNRDB6QAALOMT5GDM"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),1,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTYUCIQQYTZCDVDV7MJQVDW2M5A"),1,6,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVA2Y4NZKIZAS7DIOUYDAT6VCQI"),0,1,2,false,false)
			},null),

			/*Radix::System::Unit:Edit:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTVPUU5PYBDAJF3YLSDHE72H5Y"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col33BGOAFDTZGY3IZ3IUZLVC2UPM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZF4DFPFNQZFWXENBIJJQ7HAYSY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6L4O4LIEOFAGTJ4KMHSY5RMTZE"),0,2,1,false,false)
			},null),

			/*Radix::System::Unit:Edit:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFKKQIWDA35GHXKH6SKUDSCBNKE"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgOCDVZAOTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgUJB2RCH6KZHD5IMTQTE76DQSZA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgJ4BHY7HBLBBBXIIQZEPCKTW2MU"))}
	,

	/*Radix::System::Unit:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::System::Unit:Edit:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFKKQIWDA35GHXKH6SKUDSCBNKE"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
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
/* Radix::System::Unit:Edit - Web Meta*/

/*Radix::System::Unit:Edit-Editor Presentation*/

package org.radixware.ads.System.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Unit:Edit:Editor Pages-Editor Presentation Pages*/
	null,
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
	}
	,

	/*Radix::System::Unit:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::Unit:Edit:Model - Desktop Executable*/

/*Radix::System::Unit:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model")
public class Edit:Model  extends org.radixware.ads.System.explorer.Unit.Unit_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Edit:Model:Properties-Properties*/

	/*Radix::System::Unit:Edit:Model:Methods-Methods*/

	/*Radix::System::Unit:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:afterRead")
	protected  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    updateVisibility();
		}
		if (getRestrictions().canBeAllowed(Meta::PresentationRestriction:DELETE)) {
		    getRestrictions().setDeleteRestricted(activityStatus.Value != ActivityStatus:STOPPED);
		}


	}

	/*Radix::System::Unit:Edit:Model:onCommand_Start-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Start")
	public published  void onCommand_Start (org.radixware.ads.System.explorer.Unit.Start command) {
		try {
		    InstanceControlServiceWsdl:StartUnitDocument output = command.send();
		    if (output.StartUnit.StartUnitRs.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit started");
		    else if (output.StartUnit.StartUnitRs.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.POSTPONED) {
		        final String reason = output.StartUnit.StartUnitRs.ResultComment == null || output.StartUnit.StartUnitRs.ResultComment.isEmpty() ? "" : "\n" + "Reason: " + output.StartUnit.StartUnitRs.ResultComment;
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit start postponed" + reason);
		    } else if (output.StartUnit.StartUnitRs.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit start scheduled");
		    else
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Error starting unit");
		    //read(); called by Explorer
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:onCommand_Restart-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Restart")
	public published  void onCommand_Restart (org.radixware.ads.System.explorer.Unit.Restart command) {
		try{
		    InstanceControlServiceXsd:RestartUnitRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		       Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit restarted");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		       Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit restart scheduled");
		    else
		       Explorer.Dialogs::SimpleDlg.messageError(Environment,"Error restarting unit");     
		    //read(); called by Explorer
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:onCommand_Stop-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Stop")
	public published  void onCommand_Stop (org.radixware.ads.System.explorer.Unit.Stop command) {
		try{
		    InstanceControlServiceXsd:StopUnitRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		      Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit stopped");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		      Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit stop scheduled");
		    else
		         Explorer.Dialogs::SimpleDlg.messageError(Environment,"Error stopping unit");
		    //read(); called by Explorer
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (warning.Value != null) {   
		   Environment.messageWarning(warning.Value);
		}

	}

	/*Radix::System::Unit:Edit:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}



	}

	/*Radix::System::Unit:Edit:Model:onCommand_Move-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Move")
	public published  void onCommand_Move (org.radixware.ads.System.explorer.Unit.Move command) {
		try {
		    final Explorer.Types::Reference ref = instance.selectParent();
		    if (ref != null && !ref.equals(instance.Value)) {
		        org.radixware.schemas.types.IntDocument doc = org.radixware.schemas.types.IntDocument.Factory.newInstance();
		        doc.Int = Int.valueOf(ref.getPid().toString());
		        command.send(doc);
		        onDeleteSynchronization();
		        getEnvironment().messageInformation("Module movement", "Unit moved");
		    }
		} catch (Exception ex) {
		    getEnvironment().messageException("Module movement", "Error", ex);
		}
	}

	/*Radix::System::Unit:Edit:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		boolean use = use.Value == true;
		boolean started = started.Value == true;
		boolean instanceStarted = instanceStarted.Value == true;
		boolean postponed = postponed.Value == true;

		if (idof[Unit:Move].equals(command.Id)) {
		    return !started && !postponed;
		}

		if (idof[Unit:Start].equals(command.Id)) {
		    return use && !started && instanceStarted;
		}
		if (idof[Unit:Stop].equals(command.Id)) {
		    return (started || postponed) && instanceStarted;
		}
		if (idof[Unit:Restart].equals(command.Id)) {
		    return use && started && instanceStarted;
		};

		if (idof[Unit:Abort].equals(command.Id)) {
		    return instanceStarted && (started || postponed);
		}
		return super.isCommandEnabled(command);
	}

	/*Radix::System::Unit:Edit:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:getContextId")
	public published  Str getContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::System::Unit:Edit:Model:getContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:getContextType")
	public published  Str getContextType () {
		return Arte::EventContextType:SystemUnit.Value;
	}

	/*Radix::System::Unit:Edit:Model:onCommand_Abort-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Abort")
	public published  void onCommand_Abort (org.radixware.ads.System.explorer.Unit.Abort command) {
		try {
		    InstanceControlServiceXsd:AbortUnitRs output = command.send();
		    if (output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit aborted and unloaded");
		    else {
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Unable to abort and unload the unit");
		    }
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:updateVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:updateVisibility")
	protected published  void updateVisibility () {
		primaryUnit.setVisible(false);
		aadcTestMode.setVisible(aadcMemberId.Value != null);
		activityStatus.setVisible(!isNew());
		selfCheckTimeStr.setVisible(!isNew());
	}
	public final class Move extends org.radixware.ads.System.explorer.Unit.Move{
		protected Move(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Move( this );
		}

	}

	public final class Start extends org.radixware.ads.System.explorer.Unit.Start{
		protected Start(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Start( this );
		}

	}

	public final class Abort extends org.radixware.ads.System.explorer.Unit.Abort{
		protected Abort(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Abort( this );
		}

	}

	public final class Restart extends org.radixware.ads.System.explorer.Unit.Restart{
		protected Restart(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Restart( this );
		}

	}

	public final class Stop extends org.radixware.ads.System.explorer.Unit.Stop{
		protected Stop(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Stop( this );
		}

	}





















}

/* Radix::System::Unit:Edit:Model - Desktop Meta*/

/*Radix::System::Unit:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Edit:Model:Properties-Properties*/
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

/* Radix::System::Unit:Edit:Model - Web Executable*/

/*Radix::System::Unit:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model")
public class Edit:Model  extends org.radixware.ads.System.web.Unit.Unit_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Unit:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Edit:Model:Properties-Properties*/

	/*Radix::System::Unit:Edit:Model:Methods-Methods*/

	/*Radix::System::Unit:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:afterRead")
	protected  void afterRead () {
		super.afterRead();
		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    updateVisibility();
		}
		if (getRestrictions().canBeAllowed(Meta::PresentationRestriction:DELETE)) {
		    getRestrictions().setDeleteRestricted(activityStatus.Value != ActivityStatus:STOPPED);
		}


	}

	/*Radix::System::Unit:Edit:Model:onCommand_Start-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Start")
	public published  void onCommand_Start (org.radixware.ads.System.web.Unit.Start command) {
		try {
		    InstanceControlServiceWsdl:StartUnitDocument output = command.send();
		    if (output.StartUnit.StartUnitRs.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit started");
		    else if (output.StartUnit.StartUnitRs.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.POSTPONED) {
		        final String reason = output.StartUnit.StartUnitRs.ResultComment == null || output.StartUnit.StartUnitRs.ResultComment.isEmpty() ? "" : "\n" + "Reason: " + output.StartUnit.StartUnitRs.ResultComment;
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit start postponed" + reason);
		    } else if (output.StartUnit.StartUnitRs.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit start scheduled");
		    else
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Error starting unit");
		    //read(); called by Explorer
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:onCommand_Restart-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Restart")
	public published  void onCommand_Restart (org.radixware.ads.System.web.Unit.Restart command) {
		try{
		    InstanceControlServiceXsd:RestartUnitRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		       Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit restarted");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		       Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit restart scheduled");
		    else
		       Explorer.Dialogs::SimpleDlg.messageError(Environment,"Error restarting unit");     
		    //read(); called by Explorer
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:onCommand_Stop-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Stop")
	public published  void onCommand_Stop (org.radixware.ads.System.web.Unit.Stop command) {
		try{
		    InstanceControlServiceXsd:StopUnitRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		      Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit stopped");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		      Explorer.Dialogs::SimpleDlg.messageInformation(Environment,"Unit stop scheduled");
		    else
		         Explorer.Dialogs::SimpleDlg.messageError(Environment,"Error stopping unit");
		    //read(); called by Explorer
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (warning.Value != null) {   
		   Environment.messageWarning(warning.Value);
		}

	}

	/*Radix::System::Unit:Edit:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}



	}

	/*Radix::System::Unit:Edit:Model:onCommand_Move-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Move")
	public published  void onCommand_Move (org.radixware.ads.System.web.Unit.Move command) {
		try {
		    final Explorer.Types::Reference ref = instance.selectParent();
		    if (ref != null && !ref.equals(instance.Value)) {
		        org.radixware.schemas.types.IntDocument doc = org.radixware.schemas.types.IntDocument.Factory.newInstance();
		        doc.Int = Int.valueOf(ref.getPid().toString());
		        command.send(doc);
		        onDeleteSynchronization();
		        getEnvironment().messageInformation("Module movement", "Unit moved");
		    }
		} catch (Exception ex) {
		    getEnvironment().messageException("Module movement", "Error", ex);
		}
	}

	/*Radix::System::Unit:Edit:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		boolean use = use.Value == true;
		boolean started = started.Value == true;
		boolean instanceStarted = instanceStarted.Value == true;
		boolean postponed = postponed.Value == true;

		if (idof[Unit:Move].equals(command.Id)) {
		    return !started && !postponed;
		}

		if (idof[Unit:Start].equals(command.Id)) {
		    return use && !started && instanceStarted;
		}
		if (idof[Unit:Stop].equals(command.Id)) {
		    return (started || postponed) && instanceStarted;
		}
		if (idof[Unit:Restart].equals(command.Id)) {
		    return use && started && instanceStarted;
		};

		if (idof[Unit:Abort].equals(command.Id)) {
		    return instanceStarted && (started || postponed);
		}
		return super.isCommandEnabled(command);
	}

	/*Radix::System::Unit:Edit:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:getContextId")
	public published  Str getContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::System::Unit:Edit:Model:getContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:getContextType")
	public published  Str getContextType () {
		return Arte::EventContextType:SystemUnit.Value;
	}

	/*Radix::System::Unit:Edit:Model:onCommand_Abort-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:onCommand_Abort")
	public published  void onCommand_Abort (org.radixware.ads.System.web.Unit.Abort command) {
		try {
		    InstanceControlServiceXsd:AbortUnitRs output = command.send();
		    if (output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Unit aborted and unloaded");
		    else {
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Unable to abort and unload the unit");
		    }
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::System::Unit:Edit:Model:updateVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Edit:Model:updateVisibility")
	protected published  void updateVisibility () {
		primaryUnit.setVisible(false);
		aadcTestMode.setVisible(aadcMemberId.Value != null);
		activityStatus.setVisible(!isNew());
		selfCheckTimeStr.setVisible(!isNew());
	}
	public final class Move extends org.radixware.ads.System.web.Unit.Move{
		protected Move(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Move( this );
		}

	}

	public final class Start extends org.radixware.ads.System.web.Unit.Start{
		protected Start(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Start( this );
		}

	}

	public final class Abort extends org.radixware.ads.System.web.Unit.Abort{
		protected Abort(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Abort( this );
		}

	}

	public final class Restart extends org.radixware.ads.System.web.Unit.Restart{
		protected Restart(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Restart( this );
		}

	}

	public final class Stop extends org.radixware.ads.System.web.Unit.Stop{
		protected Stop(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_Stop( this );
		}

	}





















}

/* Radix::System::Unit:Edit:Model - Web Meta*/

/*Radix::System::Unit:Edit:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemXCFCDDGTRHNRDB5MAALOMT5GDM"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Edit:Model:Properties-Properties*/
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

/* Radix::System::Unit:General - Desktop Meta*/

/*Radix::System::Unit:General-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
		null,
		false,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("flt2FKMFBALG5HH5LNVUVARRVORXA"),
		9120,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.Unit.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::Unit:General - Web Meta*/

/*Radix::System::Unit:General-Selector Presentation*/

package org.radixware.ads.System.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
		null,
		false,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("flt2FKMFBALG5HH5LNVUVARRVORXA"),
		9120,
		null,
		144,
		null,
		null,
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdBWQ33IP5GJER3MDGKP5K7RNDHQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3AUOLEHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEWJT6TIHR5VDBNSIAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdU7QY4J3ZXDORDF72ABIFNQAABA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI6NERFZYS5VDBFCXAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVX5YH2SS5VDBN2IAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd65KAVNFIANDIFIOL2PUOBZWAZI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colIREKNEWHBDWDRD25AAYQQMVFBB"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFSR2FKUOOXOBDCJRAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colT3XHLBWPY5GGRBETJTUPM3WC54"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJNRCI76RXRBNDK7KGQFJT3XF4I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.Unit.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::Unit:SelectStarted - Desktop Meta*/

/*Radix::System::Unit:SelectStarted-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class SelectStarted_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new SelectStarted_mi();
	private SelectStarted_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM474UPJLXTNRDOLKABIFNQAABA"),
		"SelectStarted",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
		null,
		false,
		true,
		null,
		3575,
		null,
		18640,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprS6PW3X2EKJFUNPGCXYDBACXY7A")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprXCFCDDGTRHNRDB5MAALOMT5GDM")},
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.explorer.Unit.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::Unit:SelectStarted - Web Meta*/

/*Radix::System::Unit:SelectStarted-Selector Presentation*/

package org.radixware.ads.System.web;
public final class SelectStarted_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new SelectStarted_mi();
	private SelectStarted_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM474UPJLXTNRDOLKABIFNQAABA"),
		"SelectStarted",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtRFO67MTZXDORDF72ABIFNQAABA"),
		null,
		false,
		true,
		null,
		3575,
		null,
		18640,
		null,
		null,
		false,true,false,0,0);
		columns = null;;
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.System.web.Unit.DefaultGroupModel(userSession,this);
	}
}
/* Radix::System::Unit:Id:Model - Desktop Executable*/

/*Radix::System::Unit:Id:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Id:Model")
public class Id:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Id:Model_mi.rdxMeta; }



	public Id:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::Unit:Id:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Id:Model:Properties-Properties*/








	/*Radix::System::Unit:Id:Model:Methods-Methods*/

	/*Radix::System::Unit:Id:pId:pId-Presentation Property*/




	public class PId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Id:pId:pId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Id:pId:pId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PId getPId(){return (PId)getProperty(prmUMQDS6JAXZGALPSUVY3FTHOZMI);}


}

/* Radix::System::Unit:Id:Model - Desktop Meta*/

/*Radix::System::Unit:Id:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Id:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcPWNIPHPWINAI3J4YLBFMA2R7RE"),
						"Id:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Id:Model:Properties-Properties*/
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

/* Radix::System::Unit:Id:Model - Web Executable*/

/*Radix::System::Unit:Id:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Id:Model")
public class Id:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Id:Model_mi.rdxMeta; }



	public Id:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::Unit:Id:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Id:Model:Properties-Properties*/








	/*Radix::System::Unit:Id:Model:Methods-Methods*/

	/*Radix::System::Unit:Id:pId:pId-Presentation Property*/




	public class PId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Id:pId:pId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Id:pId:pId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public PId getPId(){return (PId)getProperty(prmUMQDS6JAXZGALPSUVY3FTHOZMI);}


}

/* Radix::System::Unit:Id:Model - Web Meta*/

/*Radix::System::Unit:Id:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Id:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcPWNIPHPWINAI3J4YLBFMA2R7RE"),
						"Id:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Id:Model:Properties-Properties*/
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

/* Radix::System::Unit:Class:Model - Desktop Executable*/

/*Radix::System::Unit:Class:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:Model")
public class Class:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Class:Model_mi.rdxMeta; }



	public Class:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::Unit:Class:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Class:Model:Properties-Properties*/












	/*Radix::System::Unit:Class:Model:Methods-Methods*/

	/*Radix::System::Unit:Class:pType:pType-Presentation Property*/




	public class PType extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.UnitType> getValClass(){
			return org.radixware.ads.System.common.UnitType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pType:pType")
		public  org.radixware.ads.System.common.UnitType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pType:pType")
		public   void setValue(org.radixware.ads.System.common.UnitType val) {
			Value = val;
		}
	}
	public PType getPType(){return (PType)getProperty(prmYGRKR4Y5SRHTLHGLS2YYZVBFHU);}

	/*Radix::System::Unit:Class:pUse:pUse-Presentation Property*/




	public class PUse extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PUse(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pUse:pUse")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pUse:pUse")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PUse getPUse(){return (PUse)getProperty(prmMZGZZZWY7NDWTDTAPSZHXLQGYI);}

	/*Radix::System::Unit:Class:pStarted:pStarted-Presentation Property*/




	public class PStarted extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PStarted(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pStarted:pStarted")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pStarted:pStarted")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PStarted getPStarted(){return (PStarted)getProperty(prm5FE7DXZSNJE2XAZOAZ4ODJMYRU);}


}

/* Radix::System::Unit:Class:Model - Desktop Meta*/

/*Radix::System::Unit:Class:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Class:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcUJ346JVHQRBBLLL6WYXECY7AJE"),
						"Class:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Class:Model:Properties-Properties*/
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

/* Radix::System::Unit:Class:Model - Web Executable*/

/*Radix::System::Unit:Class:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:Model")
public class Class:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Class:Model_mi.rdxMeta; }



	public Class:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::Unit:Class:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:Class:Model:Properties-Properties*/












	/*Radix::System::Unit:Class:Model:Methods-Methods*/

	/*Radix::System::Unit:Class:pType:pType-Presentation Property*/




	public class PType extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public PType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.UnitType> getValClass(){
			return org.radixware.ads.System.common.UnitType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.UnitType dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.UnitType ? (org.radixware.ads.System.common.UnitType)x : org.radixware.ads.System.common.UnitType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pType:pType")
		public  org.radixware.ads.System.common.UnitType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pType:pType")
		public   void setValue(org.radixware.ads.System.common.UnitType val) {
			Value = val;
		}
	}
	public PType getPType(){return (PType)getProperty(prmYGRKR4Y5SRHTLHGLS2YYZVBFHU);}

	/*Radix::System::Unit:Class:pUse:pUse-Presentation Property*/




	public class PUse extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PUse(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pUse:pUse")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pUse:pUse")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PUse getPUse(){return (PUse)getProperty(prmMZGZZZWY7NDWTDTAPSZHXLQGYI);}

	/*Radix::System::Unit:Class:pStarted:pStarted-Presentation Property*/




	public class PStarted extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PStarted(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pStarted:pStarted")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:Class:pStarted:pStarted")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PStarted getPStarted(){return (PStarted)getProperty(prm5FE7DXZSNJE2XAZOAZ4ODJMYRU);}


}

/* Radix::System::Unit:Class:Model - Web Meta*/

/*Radix::System::Unit:Class:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Class:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmcUJ346JVHQRBBLLL6WYXECY7AJE"),
						"Class:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:Class:Model:Properties-Properties*/
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

/* Radix::System::Unit:UsedOrStarted:Model - Desktop Executable*/

/*Radix::System::Unit:UsedOrStarted:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:UsedOrStarted:Model")
public class UsedOrStarted:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return UsedOrStarted:Model_mi.rdxMeta; }



	public UsedOrStarted:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::Unit:UsedOrStarted:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:UsedOrStarted:Model:Properties-Properties*/






	/*Radix::System::Unit:UsedOrStarted:Model:Methods-Methods*/


}

/* Radix::System::Unit:UsedOrStarted:Model - Desktop Meta*/

/*Radix::System::Unit:UsedOrStarted:Model-Filter Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UsedOrStarted:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc2FKMFBALG5HH5LNVUVARRVORXA"),
						"UsedOrStarted:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:UsedOrStarted:Model:Properties-Properties*/
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

/* Radix::System::Unit:UsedOrStarted:Model - Web Executable*/

/*Radix::System::Unit:UsedOrStarted:Model-Filter Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Unit:UsedOrStarted:Model")
public class UsedOrStarted:Model  extends org.radixware.kernel.common.client.models.FilterModel  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return UsedOrStarted:Model_mi.rdxMeta; }



	public UsedOrStarted:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.filters.RadFilterDef def){super(userSession,def);}

	/*Radix::System::Unit:UsedOrStarted:Model:Nested classes-Nested Classes*/

	/*Radix::System::Unit:UsedOrStarted:Model:Properties-Properties*/






	/*Radix::System::Unit:UsedOrStarted:Model:Methods-Methods*/


}

/* Radix::System::Unit:UsedOrStarted:Model - Web Meta*/

/*Radix::System::Unit:UsedOrStarted:Model-Filter Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class UsedOrStarted:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("fmc2FKMFBALG5HH5LNVUVARRVORXA"),
						"UsedOrStarted:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Unit:UsedOrStarted:Model:Properties-Properties*/
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

/* Radix::System::Unit - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Unit - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reason: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Причина: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2CBQEZZGKZAGJAFX5WQRRPTAJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2IUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"effectiveSelfCheckTime");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"effectiveSelfCheckTime");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2LNGINRLPNFW5H6Y6C2S2GZ6YY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2MUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error starting unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при запуске модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2UKSK2D74JG3FNTFMSLO7HR6H4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit moved");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль перемещен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EI5EEXA7BCYHMRCSYVGDMRXJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QOKJZDYXDORDF72ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4QUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Module movement");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перемещение модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4WLRKUM57NBR5PQYRVAXJ6LHWE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4YUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit activity status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние активности модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5IWTXVUABVHPXOGXFWLBN6AHBE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Communication Channels");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Каналы коммуникаций");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JC26JW46RHDFO4AT64MTWQTTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to abort and unload the unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось прервать и выгрузить модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QYPPA7BZJH3BESL3CLFDIBYP4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5XA75MAWXZGJ3AN3IOJ42EZEOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error stopping unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при остановке модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls76B7EZNDI5AITMX52MTDUP2YLI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any mode>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой режим>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ISOXL24AREL3PGGOF6GGWTM44"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Main unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Основной модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LIIT6LQPZGP5D7EY74FKGFSIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7ZV2K74IBVDLHPKB4SS6ZZ6AYQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Database trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассы в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAIUVQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit aborted and unloaded");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль прерван и выгружен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBIPFE7D46RDIROVBJ3TABMNQNQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBLUWCXKRRFG37KBXEHYCI4VKNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit stop scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановка модуля запланирована");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCHFI5FWQOVBSZEMK7V53HSTFJ4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCTVPUU5PYBDAJF3YLSDHE72H5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDIUVQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Move to another system instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перенести в другую инстанцию системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSXTWJ3RUZHWJEQ5BPNOEEPHQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Используется");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsED5K4INSLRGKFILBK4XCCBH5GY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEHFDPIEIT5EFJCGYHO72ZXENN4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Abort and Unload");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Прервать и выгрузить");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEKEZQLBI65GA3FCR4A5YL6ZOAY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Deletion of started unit \'%s\' is forbidden. Stop the unit first.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Удаление запущенного модуля \'%s\' запрещено.  Модуль необходимо предварительно остановить.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEX32NT4XV5ESFGHZX63ANKAC5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network settings of this unit have the following conflicts:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевые настройки данного модуля имеют конфликты:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsF3QHSAE3OBDQDP4ZU6N4OTTU5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"selfCheckTimeMillis");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"selfCheckTimeMillis");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMO2NWZZG5FJHOVNHQDWGUCTAU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"GUI trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки на экран");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFMOOPWBNC5HH5CS3UT5X6XE4ZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Started First");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущенные - в начале");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGDM6TB252VC2NJEOZGFHHTJ62Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль запущен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGHYJRRLZMZBI7OXP2KD3433JL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error restarting unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при перезапуске модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGZ2D67FLCBG6VLIV3YOKAAQMWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used or started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Используемые или запущенные");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH2DJTIEYURCONMQMDMJYS2UJOQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модули общего назначения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHGWNL4MHWFBR3AQ5TUAQW4IDQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to move started unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущенный модуль не может быть перемещен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHLMSQ3BIUBGMHHJ6MNFRJQB7KE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit stopped");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль остановлен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHSUWQZE5EVF45IAOROJXULGJV4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By instance ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид. инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIKN6DGNDK5FRDG3TGI7R2GAV6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use in AADC test mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать в режиме тестирования AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJTTAI4YF4BF4JM7YDRRIP4XV4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit start scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запуск модуля запланирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK4SFKNRAZFALHP6BOTFECFIHAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKN3LYXQ3VJCS5KTCTMLKGPCUII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit restarted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль перезапущен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKRQYV4KDIRENXOZJSYNW67M2FA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLBEEDMNDLZA27MPPNT3FQBSXRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart Unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапустить модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLDOIPIBWRTOBDCKLAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMCJODNMCP5AG5BRWYS5DDJ7EVQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start Unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMTPQRW4KOXOBDCJRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address conflicts check error: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при проверке адресов на конфликты: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO3LQTVQUHJBRDKGMT4SYW2RNGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last self-check time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней самопроверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMRHQK3OFVESXKPLLGHBGVK744"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used or started units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Используемые или запущенные модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOUL2CO5OTVAXTFIM45XJVAHBJM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Активен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP6WKFCHJZBE5NMV4LUYBI5ZFUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQAUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit restart scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапуск модуля запланирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQGHBUOWCNJDQ7PWR5NY7WE27NA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start postponed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запуск отложен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQT2GCQ3BUJDVVAYOEY2W3EBXGU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модули системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsREUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRJO67MTZXDORDF72ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ссылочный ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRPXYJ4HQAZABPEQN6TR5FM2AMM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSZSKWL6YIRDRDHLTJUM2MJX4UQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsU5HC4KNQRNCLLLN2AXDJEHQUEE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop Unit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить модуль");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUBE2P7EKOXOBDCJRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любой>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUCKLUWFM4BDTXIACJFCENKZ5ZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of ARTEs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное число ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSHKLORNABFV5MPPJDYIUM3WPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVDQY4J3ZXDORDF72ABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVMUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Module movement");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перемещение модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVXNRT366CRDXNMGLVGF6OCGK6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW7XZFKGOAFCVXCQQZW573P32MU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция запущена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXDUVLLMPOXOBDCJRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<take from instance settings>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<брать из настроек инстанции>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYCYL6KXWUFB4NHOB52WVVZGNUI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit start postponed");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запуск модуля отложен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYUXJYADSLJG7LECY6MAZ3UCIJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZAIKPE76VHOBDCLSAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZUUFQOK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Unit - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaec5HP4XTP3EGWDBRCRAAIT4AGD7E"),"Unit - Localizing Bundle",$$$items$$$);
}
