
/* Radix::Net::NetChannel - Server Executable*/

/*Radix::Net::NetChannel-Entity Class*/

package org.radixware.ads.Net.server;

import java.util.Collections;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel")
public abstract published class NetChannel  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return NetChannel_mi.rdxMeta;}

	/*Radix::Net::NetChannel:Nested classes-Nested Classes*/

	/*Radix::Net::NetChannel:Properties-Properties*/

	/*Radix::Net::NetChannel:unitId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitId")
	public final published  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitId")
	public final published   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::Net::NetChannel:curSessionCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curSessionCount")
	public final published  Int getCurSessionCount() {
		return curSessionCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curSessionCount")
	public final published   void setCurSessionCount(Int val) {
		curSessionCount = val;
	}

	/*Radix::Net::NetChannel:maxSessionCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:maxSessionCount")
	public final published  Int getMaxSessionCount() {
		return maxSessionCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:maxSessionCount")
	public final published   void setMaxSessionCount(Int val) {
		maxSessionCount = val;
	}

	/*Radix::Net::NetChannel:linkLevelProtocolKind-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:linkLevelProtocolKind")
	public published  org.radixware.ads.Net.common.LinkLevelProtocolKind getLinkLevelProtocolKind() {
		return linkLevelProtocolKind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:linkLevelProtocolKind")
	public published   void setLinkLevelProtocolKind(org.radixware.ads.Net.common.LinkLevelProtocolKind val) {
		linkLevelProtocolKind = val;
	}

	/*Radix::Net::NetChannel:responseFrame-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:responseFrame")
	public published  Str getResponseFrame() {
		return responseFrame;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:responseFrame")
	public published   void setResponseFrame(Str val) {
		responseFrame = val;
	}

	/*Radix::Net::NetChannel:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:id")
	public final published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:id")
	public final published   void setId(Int val) {
		id = val;
	}

	/*Radix::Net::NetChannel:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:title")
	public final published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:title")
	public final published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::Net::NetChannel:sendTimeout-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:sendTimeout")
	public published  Int getSendTimeout() {
		return sendTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:sendTimeout")
	public published   void setSendTimeout(Int val) {
		sendTimeout = val;
	}

	/*Radix::Net::NetChannel:address-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:address")
	public final published  Str getAddress() {
		return address;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:address")
	public final published   void setAddress(Str val) {
		address = val;
	}

	/*Radix::Net::NetChannel:requestFrame-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:requestFrame")
	public published  Str getRequestFrame() {
		return requestFrame;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:requestFrame")
	public published   void setRequestFrame(Str val) {
		requestFrame = val;
	}

	/*Radix::Net::NetChannel:recvTimeout-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:recvTimeout")
	public published  Int getRecvTimeout() {
		return recvTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:recvTimeout")
	public published   void setRecvTimeout(Int val) {
		recvTimeout = val;
	}

	/*Radix::Net::NetChannel:dbTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:dbTraceProfile")
	public final published  Str getDbTraceProfile() {
		return dbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:dbTraceProfile")
	public final published   void setDbTraceProfile(Str val) {
		dbTraceProfile = val;
	}

	/*Radix::Net::NetChannel:protocolHandlerClassTitle-Dynamic Property*/



	protected Str protocolHandlerClassTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:protocolHandlerClassTitle")
	public final published  Str getProtocolHandlerClassTitle() {

		try {
		    return Meta::Utils.getDefinitionTitle(getProtocolHandlerClassId());
		} catch (java.lang.Exception e) {
		    return e.getMessage();
		}
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:protocolHandlerClassTitle")
	public final published   void setProtocolHandlerClassTitle(Str val) {
		protocolHandlerClassTitle = val;
	}

	/*Radix::Net::NetChannel:fileTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:fileTraceProfile")
	public final published  Str getFileTraceProfile() {
		return fileTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:fileTraceProfile")
	public final published   void setFileTraceProfile(Str val) {
		fileTraceProfile = val;
	}

	/*Radix::Net::NetChannel:guiTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:guiTraceProfile")
	public final published  Str getGuiTraceProfile() {
		return guiTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:guiTraceProfile")
	public final published   void setGuiTraceProfile(Str val) {
		guiTraceProfile = val;
	}

	/*Radix::Net::NetChannel:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classGuid")
	public final published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classGuid")
	public final published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::Net::NetChannel:isConnectReadyNtfOn-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isConnectReadyNtfOn")
	public published  Bool getIsConnectReadyNtfOn() {
		return isConnectReadyNtfOn;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isConnectReadyNtfOn")
	public published   void setIsConnectReadyNtfOn(Bool val) {
		isConnectReadyNtfOn = val;
	}

	/*Radix::Net::NetChannel:isDisconnectNtfOn-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isDisconnectNtfOn")
	public published  Bool getIsDisconnectNtfOn() {
		return isDisconnectNtfOn;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isDisconnectNtfOn")
	public published   void setIsDisconnectNtfOn(Bool val) {
		isDisconnectNtfOn = val;
	}

	/*Radix::Net::NetChannel:securityProtocol-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:securityProtocol")
	public published  org.radixware.ads.System.common.PortSecurityProtocol getSecurityProtocol() {
		return securityProtocol;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:securityProtocol")
	public published   void setSecurityProtocol(org.radixware.ads.System.common.PortSecurityProtocol val) {
		securityProtocol = val;
	}

	/*Radix::Net::NetChannel:checkClientCert-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkClientCert")
	public published  org.radixware.kernel.common.enums.EClientAuthentication getCheckClientCert() {
		return checkClientCert;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkClientCert")
	public published   void setCheckClientCert(org.radixware.kernel.common.enums.EClientAuthentication val) {
		checkClientCert = val;
	}

	/*Radix::Net::NetChannel:serverKeyAliases-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:serverKeyAliases")
	public published  org.radixware.kernel.common.types.ArrStr getServerKeyAliases() {
		return serverKeyAliases;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:serverKeyAliases")
	public published   void setServerKeyAliases(org.radixware.kernel.common.types.ArrStr val) {
		serverKeyAliases = val;
	}

	/*Radix::Net::NetChannel:clientCertAliases-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:clientCertAliases")
	public published  org.radixware.kernel.common.types.ArrStr getClientCertAliases() {
		return clientCertAliases;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:clientCertAliases")
	public published   void setClientCertAliases(org.radixware.kernel.common.types.ArrStr val) {
		clientCertAliases = val;
	}

	/*Radix::Net::NetChannel:keyAliases-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr keyAliases=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keyAliases")
	public published  org.radixware.kernel.common.types.ArrStr getKeyAliases() {

		if (internal[keyAliases] == null) {
		    try {
		        if (Arte::Arte.getInstance().getInstance().getKeyStorePath() != null && !"".equals(Arte::Arte.getInstance().getInstance().getKeyStorePath())) {
		            internal[keyAliases] = new ArrStr(Pki::KeystoreController.getServerKeystoreKeyAliases());
		        }
		    } catch (Exceptions::Throwable e) {
		        Arte::Trace.error("Error reading the key aliases from the keystore:" + e.toString(), Arte::EventSource:NetPortHandler);
		    }
		}

		return internal[keyAliases];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keyAliases")
	public published   void setKeyAliases(org.radixware.kernel.common.types.ArrStr val) {
		keyAliases = val;
	}

	/*Radix::Net::NetChannel:trustedCertificateAliases-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr trustedCertificateAliases=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:trustedCertificateAliases")
	public published  org.radixware.kernel.common.types.ArrStr getTrustedCertificateAliases() {

		if (internal[trustedCertificateAliases] == null) {
		    try {
		        if (Arte::Arte.getInstance().getInstance().getKeyStorePath() != null && !"".equals(Arte::Arte.getInstance().getInstance().getKeyStorePath())) {
		            internal[trustedCertificateAliases] = new ArrStr(Pki::KeystoreController.getServerKeystoreTrustedCertAliases());
		        }
		    } catch (Exceptions::Throwable e) {
		        Arte::Trace.error("Error reading the trusted certificate aliases from the keystore:" + e.toString(), Arte::EventSource:NetPortHandler);
		    }
		}
		return internal[trustedCertificateAliases];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:trustedCertificateAliases")
	public published   void setTrustedCertificateAliases(org.radixware.kernel.common.types.ArrStr val) {
		trustedCertificateAliases = val;
	}

	/*Radix::Net::NetChannel:isListener-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isListener")
	public published  Bool getIsListener() {
		return isListener;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isListener")
	public published   void setIsListener(Bool val) {
		isListener = val;
	}

	/*Radix::Net::NetChannel:classTitle-Dynamic Property*/



	protected Str classTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classTitle")
	public published  Str getClassTitle() {

		return getClassDefinitionTitle();
	}

	/*Radix::Net::NetChannel:keepConnectTimeout-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keepConnectTimeout")
	public published  Int getKeepConnectTimeout() {
		return keepConnectTimeout;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keepConnectTimeout")
	public published   void setKeepConnectTimeout(Int val) {
		keepConnectTimeout = val;
	}

	/*Radix::Net::NetChannel:unitStarted-Dynamic Property*/



	protected Bool unitStarted=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitStarted")
	public published  Bool getUnitStarted() {

		System::Unit u = System::Unit.loadByPK(unitId, true);
		return u != null && u.started;
	}

	/*Radix::Net::NetChannel:use-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:use")
	public published  Bool getUse() {
		return use;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:use")
	public published   void setUse(Bool val) {
		use = val;
	}

	/*Radix::Net::NetChannel:useKeepalive-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:useKeepalive")
	public published  Bool getUseKeepalive() {
		return useKeepalive;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:useKeepalive")
	public published   void setUseKeepalive(Bool val) {
		useKeepalive = val;
	}

	/*Radix::Net::NetChannel:warning-Dynamic Property*/



	protected Str warning=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:warning")
	protected  Str getWarning() {

		if (/*org.radixware.kernel.server.SrvRunParams.getIsDevelopmentMode() //!!!_!!!
		        || */this.isNewObject() || org.radixware.kernel.common.utils.SystemPropUtils.getBooleanSystemProp("rdx.disable.auto.ports.check", false)) {
		    return null;
		}

		if (internal[warning] == null) {  
		    
		    //System.err.println("CHECK of Warning getter");
		    internal[warning] = checkForConflicts();
		   // .("CHECK of Warning getter: " + ( != null ? "NOT NULL" : "IS NULL"), );
		}
		return internal[warning];


	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:warning")
	protected   void setWarning(Str val) {
		warning = val;
	}

	/*Radix::Net::NetChannel:syncMode-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:syncMode")
	public published  Bool getSyncMode() {
		return syncMode;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:syncMode")
	public published   void setSyncMode(Bool val) {
		syncMode = val;
	}

	/*Radix::Net::NetChannel:curBusySessionCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curBusySessionCount")
	public published  Int getCurBusySessionCount() {
		return curBusySessionCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curBusySessionCount")
	public published   void setCurBusySessionCount(Int val) {
		curBusySessionCount = val;
	}

	/*Radix::Net::NetChannel:isCurBusySessionCountOn-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isCurBusySessionCountOn")
	public published  Bool getIsCurBusySessionCountOn() {
		return isCurBusySessionCountOn;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isCurBusySessionCountOn")
	public published   void setIsCurBusySessionCountOn(Bool val) {
		isCurBusySessionCountOn = val;
	}

	/*Radix::Net::NetChannel:activityStatus-Dynamic Property*/



	protected org.radixware.ads.System.common.ActivityStatus activityStatus=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:activityStatus")
	public published  org.radixware.ads.System.common.ActivityStatus getActivityStatus() {

		return (unitStarted && curSessionCount != null && curSessionCount.longValue() >= 0) ? System::ActivityStatus:RUNNING : System::ActivityStatus:STOPPED;
	}

	/*Radix::Net::NetChannel:aadcAffinityHandler-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:aadcAffinityHandler")
	public published  Str getAadcAffinityHandler() {
		return aadcAffinityHandler;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:aadcAffinityHandler")
	public published   void setAadcAffinityHandler(Str val) {
		aadcAffinityHandler = val;
	}

	/*Radix::Net::NetChannel:rid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:rid")
	public published  Str getRid() {
		return rid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:rid")
	public published   void setRid(Str val) {
		rid = val;
	}















































































































































































































































	/*Radix::Net::NetChannel:Methods-Methods*/

	/*Radix::Net::NetChannel:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:loadByPK")
	public static published  org.radixware.ads.Net.server.NetChannel loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),pkValsMap);
		try{
		return (
		org.radixware.ads.Net.server.NetChannel) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::Net::NetChannel:getNetProtocolHandler-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:getNetProtocolHandler")
	protected abstract published  org.radixware.kernel.server.net.NetProtocolHandler getNetProtocolHandler ();

	/*Radix::Net::NetChannel:getChannel-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:getChannel")
	private static  org.radixware.ads.Net.server.NetChannel getChannel (Int id) {
		NetChannel l = loadByPK(id, true);
		if (l.needCaching()) {
		    l.keepInCache(30);
		}

		return l;
	}

	/*Radix::Net::NetChannel:onConnect-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onConnect")
	public static published  org.radixware.schemas.netporthandler.OnConnectRsDocument onConnect (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid) throws org.radixware.kernel.common.exceptions.AppException {
		NetChannel channel = getChannel(channelId);

		java.lang.Object traceTargetHandler = null;
		Arte::Trace.enterContext(Arte::EventContextType:NetChannel, channelId);
		if (channel.dbTraceProfile != null)
		    traceTargetHandler = Arte::Trace.addContextProfile(channel.dbTraceProfile, channel);

		try {
		    return channel.getNetProtocolHandler().onConnect(channelId, serverAddr, clientAddr, clientCertCn, sid);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:NetChannel, channelId);
		    if (traceTargetHandler != null)
		        Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::Net::NetChannel:onDisconnect-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onDisconnect")
	public static published  void onDisconnect (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) {
		NetChannel channel = getChannel(channelId);

		java.lang.Object traceTargetHandler = null;
		Arte::Trace.enterContext(Arte::EventContextType:NetChannel, channelId);
		if (channel.dbTraceProfile != null)
		    traceTargetHandler = Arte::Trace.addContextProfile(channel.dbTraceProfile, channel);

		try {
		    channel.getNetProtocolHandler().onDisconnect(channelId, serverAddr, clientAddr, clientCertCn, sid, callbackPid, callbackWid);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:NetChannel, channelId);
		    if (traceTargetHandler != null)
		        Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::Net::NetChannel:onRecvTimeout-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onRecvTimeout")
	public static published  org.radixware.schemas.netporthandler.OnRecvTimeoutRsDocument onRecvTimeout (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) throws org.radixware.kernel.common.exceptions.AppException {
		NetChannel channel = getChannel(channelId);

		java.lang.Object traceTargetHandler = null;
		Arte::Trace.enterContext(Arte::EventContextType:NetChannel, channelId);
		if (channel.dbTraceProfile != null)
		    traceTargetHandler = Arte::Trace.addContextProfile(channel.dbTraceProfile, channel);

		try {
		    return channel.getNetProtocolHandler().onRecvTimeout(channelId, serverAddr, clientAddr, clientCertCn, sid, callbackPid, callbackWid);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:NetChannel, channelId);
		    if (traceTargetHandler != null)
		        Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::Net::NetChannel:onRecv-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onRecv")
	public static published  org.radixware.schemas.netporthandler.OnRecvRsDocument onRecv (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid, java.util.HashMap<Str,Str> recvPacketHeaders, org.radixware.kernel.common.types.Bin recvPacket, Bool connected) throws org.radixware.kernel.common.exceptions.AppException {
		NetChannel channel = getChannel(channelId);

		java.lang.Object traceTargetHandler = null;
		Arte::Trace.enterContext(Arte::EventContextType:NetChannel, channelId);
		if (channel.dbTraceProfile != null)
		    traceTargetHandler = Arte::Trace.addContextProfile(channel.dbTraceProfile, channel);

		try {
		    return channel.getNetProtocolHandler().onRecv(channelId, serverAddr, clientAddr, clientCertCn, sid, callbackPid, callbackWid, recvPacketHeaders, recvPacket, connected);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:NetChannel, channelId);
		    if (traceTargetHandler != null)
		        Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::Net::NetChannel:onConnectTimeout-User Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onConnectTimeout")
	public static published  org.radixware.schemas.netporthandler.OnConnectTimeoutRsDocument onConnectTimeout (Int channelId, Str serverAddr, Str clientAddr, Str clientCertCn, Str sid, Str callbackPid, Str callbackWid) throws org.radixware.kernel.common.exceptions.AppException {
		NetChannel channel = getChannel(channelId);

		java.lang.Object traceTargetHandler = null;
		Arte::Trace.enterContext(Arte::EventContextType:NetChannel, channelId);
		if (channel.dbTraceProfile != null)
		    traceTargetHandler = Arte::Trace.addContextProfile(channel.dbTraceProfile, channel);

		try {
		    return channel.getNetProtocolHandler().onConnectTimeout(channelId, serverAddr, clientAddr, clientCertCn, sid, callbackPid, callbackWid);
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:NetChannel, channelId);
		    if (traceTargetHandler != null)
		        Arte::Trace.delContextProfile(traceTargetHandler);
		}
	}

	/*Radix::Net::NetChannel:needCaching-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:needCaching")
	public published  boolean needCaching () {
		return true;
	}

	/*Radix::Net::NetChannel:getProtocolHandlerClassId-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:getProtocolHandlerClassId")
	public published  org.radixware.kernel.common.types.Id getProtocolHandlerClassId () {
		try {
		    INetProtocolHandler handl = getNetProtocolHandler();
		    final Class<? extends INetProtocolHandler> c = handl.getClass();
		    return ((Meta::ClassDef) c.getMethod("getRadMeta", new Class<?>[]{}).invoke(handl, new Object[]{})).getId();
		} catch (java.lang.Exception e) {
		    throw new org.radixware.kernel.common.exceptions.RadixError("Unable to extract id from ./exmeta", e);
		}

	}

	/*Radix::Net::NetChannel:onCommand_startChannel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onCommand_startChannel")
	public  org.radixware.schemas.types.BoolDocument onCommand_startChannel (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		NetPortHandlerWsdl:ChannelControlDocument doc = NetPortHandlerWsdl:ChannelControlDocument.Factory.newInstance();
		doc.ensureChannelControl().ensureChannelControlRq().ensureStart().ChannelId = id;
		try {
		    NetPortHandlerWsdl:ChannelControlDocument rsDoc = (NetPortHandlerWsdl:ChannelControlDocument) Arte::Arte.invokeInternalService(doc, NetPortHandlerWsdl:ChannelControlDocument.class, "http://schemas.radixware.org/netporthandler.wsdl#" + unitId, 0, 10, null);
		    Arte::TypesXsd:BoolDocument xResult = Arte::TypesXsd:BoolDocument.Factory.newInstance();
		    xResult.Bool = rsDoc.ChannelControl.ChannelControlRs.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE;
		    return xResult;
		} catch (Exception ex) {
		    throw new AppError("Error sending the request to the unit service" + ": " + ex.getMessage(), ex);
		}
	}

	/*Radix::Net::NetChannel:onCommand_stopChannel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:onCommand_stopChannel")
	public  org.radixware.schemas.types.BoolDocument onCommand_stopChannel (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		NetPortHandlerWsdl:ChannelControlDocument doc = NetPortHandlerWsdl:ChannelControlDocument.Factory.newInstance();
		doc.ensureChannelControl().ensureChannelControlRq().ensureStop().ChannelId = id;
		try {
		    NetPortHandlerWsdl:ChannelControlDocument rsDoc = (NetPortHandlerWsdl:ChannelControlDocument) Arte::Arte.invokeInternalService(doc, NetPortHandlerWsdl:ChannelControlDocument.class, "http://schemas.radixware.org/netporthandler.wsdl#" + unitId, 0, 10, null);
		    Arte::TypesXsd:BoolDocument xResult = Arte::TypesXsd:BoolDocument.Factory.newInstance();
		    xResult.Bool = rsDoc.ChannelControl.ChannelControlRs.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE;
		    return xResult;
		} catch (Exception ex) {
		    throw new AppError("Error sending the request to the unit service" + ": " + ex.getMessage(), ex);
		}
	}

	/*Radix::Net::NetChannel:getUsedAddresses-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:getUsedAddresses")
	public  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		if (address != null) {
		    return Collections.singletonList(new AddressInfo(address, System::SapChannelType:TCP, System::Unit.loadByPK(unitId, false).instanceId, unitId, null, id));
		}
		return Collections.emptyList();


	}

	/*Radix::Net::NetChannel:checkForConflicts-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkForConflicts")
	public final  Str checkForConflicts () {
		try {
		    java.util.Collection<System::AddressConflict> conflicts = System::AddressConflict.discoverNetChannelConflicts(this);
		    if (conflicts.isEmpty()) {
		        return null;
		    }
		    StringBuilder description = new StringBuilder();
		    description.append("Network settings of this NetChannel have the following conflicts:");
		    
		    for (System::AddressConflict conflict : conflicts) {
		        description.append("\n\t");
		        description.append(conflict.thisAddress.getAsStr());
		        description.append(" <-> ");
		        description.append(conflict.conflictedAddress.getAsStr());
		    }
		    return description.toString();
		} catch (Exceptions::Exception ex) {
		    return "Error checking the addresses for conflicts: " + Utils::ExceptionTextFormatter.exceptionStackToString(ex);
		}

	}

	/*Radix::Net::NetChannel:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:beforeUpdate")
	protected published  boolean beforeUpdate () {
		alertOnRidUniquenessViolation();

		if (linkLevelProtocolKind == LinkLevelProtocolKind:POS2) {
		    requestFrame = requestFrame == null || requestFrame.equals("") ? "?" : requestFrame;
		    responseFrame = responseFrame == null || responseFrame.equals("") ? "?" : responseFrame;
		}
		return super.beforeUpdate();
	}

	/*Radix::Net::NetChannel:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		alertOnRidUniquenessViolation();

		if (linkLevelProtocolKind == LinkLevelProtocolKind:POS2) {
		    requestFrame = requestFrame == null || requestFrame.equals("") ? "?" : requestFrame;
		    responseFrame = responseFrame == null || responseFrame.equals("") ? "?" : responseFrame;
		}
		return super.beforeCreate(src);
	}

	/*Radix::Net::NetChannel:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (src != null) {
		    curBusySessionCount = 0;
		    curSessionCount = -1;
		    
		}

	}

	/*Radix::Net::NetChannel:loadByRid-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:loadByRid")
	public static published  org.radixware.ads.Net.server.NetChannel loadByRid (Int unitId, Str rid) {
		if (rid == null) {
		    return null;
		}
		try (GetNetChannelByRid cur = GetNetChannelByRid.open(unitId, rid)) {
		    if (cur.next()) {
		        return cur.channel;
		    }
		}
		return null;
	}

	/*Radix::Net::NetChannel:checkRidUniqueness-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkRidUniqueness")
	public published  boolean checkRidUniqueness (Int unitId, Str rid) {
		if (rid == null)
		    return true;

		final NetChannel ch = loadByRid(unitId, rid);
		if (ch == null || ch == this)
		    return true;

		return false;
	}

	/*Radix::Net::NetChannel:alertOnRidUniquenessViolation-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:alertOnRidUniquenessViolation")
	private final  void alertOnRidUniquenessViolation () {
		if (isPersistentPropModified(idof[NetChannel:rid]) && !checkRidUniqueness(unitId, rid)) {
		    throw new InvalidPropertyValueError(idof[NetChannel], 
		                                        idof[NetChannel:rid], 
		                                        "Unique constraint violation for objects 'NetChannel' by key: Reference ID");
		}
	}

	/*Radix::Net::NetChannel:start-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:start")
	public published  boolean start (int timeout) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		final NetPortHandlerWsdl:ChannelControlDocument doc = NetPortHandlerWsdl:ChannelControlDocument.Factory.newInstance();
		doc.addNewChannelControl().addNewChannelControlRq().addNewStart().ChannelId = id;

		final NetPortHandlerWsdl:ChannelControlDocument rsDoc = (NetPortHandlerWsdl:ChannelControlDocument) Arte::Arte.invokeInternalService(
		    doc, NetPortHandlerWsdl:ChannelControlDocument.class, "http://schemas.radixware.org/netporthandler.wsdl#" + unitId, 0, timeout, null
		);

		return rsDoc.ChannelControl.ChannelControlRs.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE;
	}

	/*Radix::Net::NetChannel:stop-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:stop")
	public published  boolean stop (int timeout) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		final NetPortHandlerWsdl:ChannelControlDocument doc = NetPortHandlerWsdl:ChannelControlDocument.Factory.newInstance();
		doc.addNewChannelControl().addNewChannelControlRq().addNewStop().ChannelId = id;

		final NetPortHandlerWsdl:ChannelControlDocument rsDoc = (NetPortHandlerWsdl:ChannelControlDocument) Arte::Arte.invokeInternalService(
		    doc, NetPortHandlerWsdl:ChannelControlDocument.class, "http://schemas.radixware.org/netporthandler.wsdl#" + unitId, 0, timeout, null
		);

		return rsDoc.ChannelControl.ChannelControlRs.Result == System::InstanceControlServiceXsd:ActionStateEnum.DONE;
	}




	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmdJ3CH6OMXZ5GUBC2TB5AWRSTL3U){
			org.radixware.schemas.types.BoolDocument result = onCommand_startChannel(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdLN64DGQYQFHPZDIFDDCZTPLIYI){
			org.radixware.schemas.types.BoolDocument result = onCommand_stopChannel(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::Net::NetChannel - Server Meta*/

/*Radix::Net::NetChannel-Entity Class*/

package org.radixware.ads.Net.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NetChannel_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),"NetChannel",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::Net::NetChannel:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
							/*Owner Class Name*/
							"NetChannel",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::Net::NetChannel:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::Net::NetChannel:unitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7A274VUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:curSessionCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:maxSessionCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:linkLevelProtocolKind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:responseFrame:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:sendTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:address:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:requestFrame:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:recvTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:dbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::Net::NetChannel:protocolHandlerClassTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::Net::NetChannel:fileTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:guiTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:isConnectReadyNtfOn:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:isDisconnectNtfOn:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWNURWZD5KBF7DPUPXHUSTXY2UI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:securityProtocol:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:checkClientCert:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:serverKeyAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:clientCertAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:keyAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLSMYTVUFZADBFMEEGYBGAUZLY"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJIKGBMNQ4ZGZ3PGEHPR3PWJO2U"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:isListener:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW7RFZLTFEZECHEAFABQSI62TGY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:classTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:keepConnectTimeout:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:unitStarted:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:use:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:useKeepalive:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:warning:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHUJ5FP6UTZAXJCTV2K62TA6J7Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:syncMode:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:curBusySessionCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:isCurBusySessionCountOn:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:activityStatus:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:aadcAffinityHandler:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHTSOBNUDRVEBJM4OLCOZ2FAFK4"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::Net::NetChannel:rid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Net::NetChannel:selectProtocolHandler-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ4MWUCGC7VGD7LS4JV2ZCMX7NA"),"selectProtocolHandler",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.LOCAL,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Net::NetChannel:keystoreAliasesCmd-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdI5GJG6TPRJF3DP6DQRIWL456KE"),"keystoreAliasesCmd",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Net::NetChannel:startChannel-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJ3CH6OMXZ5GUBC2TB5AWRSTL3U"),"startChannel",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::Net::NetChannel:stopChannel-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLN64DGQYQFHPZDIFDDCZTPLIYI"),"stopChannel",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),false,true)
							},
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::Net::NetChannel:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::Net::NetChannel:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::Net::NetChannel:General:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiWIWWGSLLTBEKDDDOIS7UELOPI4"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ4MWUCGC7VGD7LS4JV2ZCMX7NA")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::Net::NetChannel:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM"),"General",null,2192,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(null,true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(262176,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccEUBA33RZPJCDLICJT4IXDDEN24"))
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::Net::NetChannel:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::Net::NetChannel:All-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccEUBA33RZPJCDLICJT4IXDDEN24"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
									}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::Net::NetChannel:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::Net::NetChannel:unitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7A274VUE2DNRDB7AAALOMT5GDM"),"unitId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5T53WQMRXOBDCKMAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:curSessionCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),"curSessionCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:maxSessionCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),"maxSessionCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:linkLevelProtocolKind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),"linkLevelProtocolKind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEQC6IAMF2DNRDB7AAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:responseFrame-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),"responseFrame",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTHJ2QWFTFFCLOMM3VPJKYFUIM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:sendTimeout-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),"sendTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:address-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),"address",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3BFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:requestFrame-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),"requestFrame",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RH5R64R7FED5CH25VTXXYFBKU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:recvTimeout-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),"recvTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("30")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:dbTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),"dbTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MDSW5BU4RD55E4PD7LRII7IMI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:protocolHandlerClassTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),"protocolHandlerClassTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:fileTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),"fileTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5HIWGO3CBCKJF2DLK4EKV3AHI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:guiTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),"guiTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGDOE7NJDBEGXNGGJFLH5Y6JMY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:isConnectReadyNtfOn-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),"isConnectReadyNtfOn",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4QZHHMPS5CIVHO6ESKJYF2WSA"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:isDisconnectNtfOn-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWNURWZD5KBF7DPUPXHUSTXY2UI"),"isDisconnectNtfOn",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5K46V22INGV3JOYZ3SGSNXI4I"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:securityProtocol-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),"securityProtocol",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIJ7LRUUUJFYRLFJ2EDDTDT4UI"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOQBHNCGYX7NRDB6TAALOMT5GDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:checkClientCert-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),"checkClientCert",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLZ4VBD2PVEORBAOUFIYFKMFKA"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs5YMQPO4VDRHS3IIM4EK3AHKYVM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:serverKeyAliases-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),"serverKeyAliases",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VAGMA4TKJEJJKT7W6AWFLIUVI"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:clientCertAliases-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),"clientCertAliases",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JL52KHQRZCZTEMWGIW3VMOB2U"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:keyAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLSMYTVUFZADBFMEEGYBGAUZLY"),"keyAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:trustedCertificateAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJIKGBMNQ4ZGZ3PGEHPR3PWJO2U"),"trustedCertificateAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:isListener-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW7RFZLTFEZECHEAFABQSI62TGY"),"isListener",null,org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:classTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),"classTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZR77AUTFPJEOLJW7SFEZS4LGUE"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:keepConnectTimeout-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),"keepConnectTimeout",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIMKW7A7JVE5FF43YDKV2BSXM4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:unitStarted-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),"unitStarted",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XKAUFW2VJEILM4WAOCFVIZEA4"),org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:use-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),"use",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIRJF2U6E7JGRFFM4J7O6SSTTEI"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:useKeepalive-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),"useKeepalive",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBB45RR7ODNCUDMZBNAJ2ZRHYPQ"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:warning-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHUJ5FP6UTZAXJCTV2K62TA6J7Y"),"warning",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:syncMode-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),"syncMode",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG6JAYG5NNEUHNYMKK3CNU6VJA"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:curBusySessionCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),"curBusySessionCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DWFL3RYVJADBAZBTWPX6TRTFE"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:isCurBusySessionCountOn-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),"isCurBusySessionCountOn",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLAIWKURQQZEXVNBH4BOJAJBSXA"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:activityStatus-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),"activityStatus",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFGKOK6XKBDRJCHDHFQIOAZB5Q"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:aadcAffinityHandler-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHTSOBNUDRVEBJM4OLCOZ2FAFK4"),"aadcAffinityHandler",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINZIHDW3AVBRHEWU4FMU2GYALA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::Net::NetChannel:rid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),"rid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCH2VDVIJO5DZDOZD26C5NZW4LI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::Net::NetChannel:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNJF3QXJ6XBBK3LRMFLGXYMZRWQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWFZV364YIJFAPECM24CFLVHQIQ"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthK2JMTSV73FBGBGSJJOH2ZLBYCI"),"getNetProtocolHandler",false,true,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2U36X5PJCZFV3IWVONANPBAISQ"),"getChannel",true,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOE5RMYVT7NCN7P3QTD24TM6ZAA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI5YABQPZBVGDBIRUZ4DGWHYKKY"),"onConnect",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3CXZXI2NC5A3RKWRCKADBL4EWI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprAQUDZOVSLNDZ3BVWSVGKN3BZOM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUMAJCC5UXJBZZLRY7SXUC7ZQ2Q")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEXVZGCYHZFE63KRO5BH5HB3BEQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprM3AJP2TJ3BHFXLESUNOTB45ZPI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6RAQH773VRD7VGRLMWFGKG4IAA"),"onDisconnect",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEYYTXY5NFREO5L3PEXZVLFTDE4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprI2BLKIH6R5FWPPUYFXPFKJZTI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEGHCXANJURDXNAKHG4RJPUY6JE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCK7MBXYA2NBYNA3LT4P6AAQDTE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUNEQGIWJVNASJN24FG5IXWCE4I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGNDJLP6VGJAEDPZXRDTWJHLBOY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr644L662FRRB5NBSMGS7RFJKHWE"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthUEI3NFHBGNHSHMPJ33RIRMAZKI"),"onRecvTimeout",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr73QW5Y2LB5AGRGED6RHP5ROGI4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprK27FWHZ2QRGO5N3OKZ7VDYRPYE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNBJWHZCVUFAT5CUVXTARSBDSXQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRFXE3UZARBFEPCYZ3KWEQ3UY6E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIZV5GXQVQNCIHNGJJQEBSSLM5I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCPBBIMLR25FG7NKC72BRJEO7E4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR7443CFUKRGO5PSDFKYM3PU4FY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDPLQGAWSPVG2HDLYDVRGKFWPZ4"),"onRecv",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFQZ4VXJ2XRBQZL33INTKJ4KP6E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprREZ4YCLIW5F5RIEVFJYZKS5W5A")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprY77NCQ7S7ZBLBDQRELU7REOP7E")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUATZPVGADFABPD3QVARC2RRSXI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDQVOWXQVZZATTGPELC62UMBRJ4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVZSTHPOUFFDPHPK4MODR2KUNFQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXQH37EQH3JHHBCT5Y4NW5LEG6I")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recvPacketHeaders",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNLFUDIXP6BA6JCVGP56YIJBCA4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("recvPacket",org.radixware.kernel.common.enums.EValType.BIN,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6BJC7WQBRVHX3MYXLVSDJNOGVI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("connected",org.radixware.kernel.common.enums.EValType.BOOL,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPKHRCMT3AZCO3FOQ6DQR4PFFPM"))
								},org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFR5YPKF6Z5GRNJDWHMETZW4NY4"),"onConnectTimeout",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("channelId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJS2XP7QX6RA2LKYXSFFMOPQWWQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("serverAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWMKCNLSNKVFJ7LKCVKRJPUVGMM")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientAddr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRNGRVAEGC5CKDBSFZEJR3CFLOQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("clientCertCn",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLXGG5S6LVBBH5EGWO5HZUXHJGY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIOSF5TXXHZD6ZHWDNZRUCEIDQE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackPid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprQDWYSLJEMVH7NI7ON2C5TXF4XU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("callbackWid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5HBI7U256RF3BLKVPDMXPT3FBQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3VOSK24FZVFTLKSZISQ3XGMJDE"),"needCaching",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth3V7ILFY3WZCK5KKGI47IBPPDQI"),"getProtocolHandlerClassId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdJ3CH6OMXZ5GUBC2TB5AWRSTL3U"),"onCommand_startChannel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprMDGSZI3RJBAI7L4B2QPR64W6CM"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdLN64DGQYQFHPZDIFDDCZTPLIYI"),"onCommand_stopChannel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3CHKHICQN5AK5AOAQMW2NG4SAI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVKKVYCXQUJANXP2ALA2W6OHDIQ"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOF2YV3DKYZCO3N4AGHGN2N6SC4"),"checkForConflicts",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB7MHZTZX7RGQ7GHVQENHDRJKEE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIHCAPVHXHJGEBK2RJ4EXK2BQKE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD4VHKM327VE7FHWLB7YXCJSAWI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVC4NSIZ3INF4NB2T7CMHDZQ2QQ"),"loadByRid",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unitId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP7OJ453WQZA2BDAZ4R2BHZUCFU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCAEVKANAKJFMPL7MCKMCGMNM7U"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKVPGYZY3IBCC5NBGPHZKOV3E6E"),"checkRidUniqueness",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unitId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprN5GSRIR3KFE5ZJNYEHWE35VKFI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIFSHFFSHEVEBBDHF5CUI3OIN4A"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthJM46NK6QRVCULDUMM4FFIBQOKM"),"alertOnRidUniquenessViolation",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIPGFGXISMJEAJPFOY7WTKGJUAU"),"start",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWXBBAHAX7FA4FEP6JP4T7QEWHY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthR7336I5BZVCBNHRMP2GKSII54Y"),"stop",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeout",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWXBBAHAX7FA4FEP6JP4T7QEWHY"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::Net::NetChannel - Desktop Executable*/

/*Radix::Net::NetChannel-Entity Class*/

package org.radixware.ads.Net.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel")
public interface NetChannel {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Net.explorer.NetChannel.NetChannel_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Net.explorer.NetChannel.NetChannel_DefaultModel )  super.getEntity(i);}
	}





























































































































































































































	/*Radix::Net::NetChannel:useKeepalive:useKeepalive-Presentation Property*/


	public class UseKeepalive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseKeepalive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:useKeepalive:useKeepalive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:useKeepalive:useKeepalive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseKeepalive getUseKeepalive();
	/*Radix::Net::NetChannel:use:use-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:use:use")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:use:use")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Use getUse();
	/*Radix::Net::NetChannel:serverKeyAliases:serverKeyAliases-Presentation Property*/


	public class ServerKeyAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ServerKeyAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:serverKeyAliases:serverKeyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:serverKeyAliases:serverKeyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ServerKeyAliases getServerKeyAliases();
	/*Radix::Net::NetChannel:unitId:unitId-Presentation Property*/


	public class UnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::Net::NetChannel:curSessionCount:curSessionCount-Presentation Property*/


	public class CurSessionCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurSessionCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curSessionCount:curSessionCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curSessionCount:curSessionCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurSessionCount getCurSessionCount();
	/*Radix::Net::NetChannel:keepConnectTimeout:keepConnectTimeout-Presentation Property*/


	public class KeepConnectTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public KeepConnectTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keepConnectTimeout:keepConnectTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keepConnectTimeout:keepConnectTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public KeepConnectTimeout getKeepConnectTimeout();
	/*Radix::Net::NetChannel:maxSessionCount:maxSessionCount-Presentation Property*/


	public class MaxSessionCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxSessionCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:maxSessionCount:maxSessionCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:maxSessionCount:maxSessionCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxSessionCount getMaxSessionCount();
	/*Radix::Net::NetChannel:rid:rid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::Net::NetChannel:linkLevelProtocolKind:linkLevelProtocolKind-Presentation Property*/


	public class LinkLevelProtocolKind extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LinkLevelProtocolKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Net.common.LinkLevelProtocolKind> getValClass(){
			return org.radixware.ads.Net.common.LinkLevelProtocolKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:linkLevelProtocolKind:linkLevelProtocolKind")
		public  org.radixware.ads.Net.common.LinkLevelProtocolKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:linkLevelProtocolKind:linkLevelProtocolKind")
		public   void setValue(org.radixware.ads.Net.common.LinkLevelProtocolKind val) {
			Value = val;
		}
	}
	public LinkLevelProtocolKind getLinkLevelProtocolKind();
	/*Radix::Net::NetChannel:securityProtocol:securityProtocol-Presentation Property*/


	public class SecurityProtocol extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SecurityProtocol(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.PortSecurityProtocol dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.PortSecurityProtocol ? (org.radixware.ads.System.common.PortSecurityProtocol)x : org.radixware.ads.System.common.PortSecurityProtocol.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.PortSecurityProtocol> getValClass(){
			return org.radixware.ads.System.common.PortSecurityProtocol.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.PortSecurityProtocol dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.PortSecurityProtocol ? (org.radixware.ads.System.common.PortSecurityProtocol)x : org.radixware.ads.System.common.PortSecurityProtocol.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:securityProtocol:securityProtocol")
		public  org.radixware.ads.System.common.PortSecurityProtocol getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:securityProtocol:securityProtocol")
		public   void setValue(org.radixware.ads.System.common.PortSecurityProtocol val) {
			Value = val;
		}
	}
	public SecurityProtocol getSecurityProtocol();
	/*Radix::Net::NetChannel:aadcAffinityHandler:aadcAffinityHandler-Presentation Property*/


	public class AadcAffinityHandler extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AadcAffinityHandler(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:aadcAffinityHandler:aadcAffinityHandler")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:aadcAffinityHandler:aadcAffinityHandler")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AadcAffinityHandler getAadcAffinityHandler();
	/*Radix::Net::NetChannel:checkClientCert:checkClientCert-Presentation Property*/


	public class CheckClientCert extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CheckClientCert(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EClientAuthentication dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EClientAuthentication ? (org.radixware.kernel.common.enums.EClientAuthentication)x : org.radixware.kernel.common.enums.EClientAuthentication.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EClientAuthentication> getValClass(){
			return org.radixware.kernel.common.enums.EClientAuthentication.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EClientAuthentication dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EClientAuthentication ? (org.radixware.kernel.common.enums.EClientAuthentication)x : org.radixware.kernel.common.enums.EClientAuthentication.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkClientCert:checkClientCert")
		public  org.radixware.kernel.common.enums.EClientAuthentication getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkClientCert:checkClientCert")
		public   void setValue(org.radixware.kernel.common.enums.EClientAuthentication val) {
			Value = val;
		}
	}
	public CheckClientCert getCheckClientCert();
	/*Radix::Net::NetChannel:responseFrame:responseFrame-Presentation Property*/


	public class ResponseFrame extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ResponseFrame(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:responseFrame:responseFrame")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:responseFrame:responseFrame")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ResponseFrame getResponseFrame();
	/*Radix::Net::NetChannel:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Net::NetChannel:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Net::NetChannel:sendTimeout:sendTimeout-Presentation Property*/


	public class SendTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:sendTimeout:sendTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:sendTimeout:sendTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendTimeout getSendTimeout();
	/*Radix::Net::NetChannel:guiTraceProfile:guiTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:guiTraceProfile:guiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:guiTraceProfile:guiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public GuiTraceProfile getGuiTraceProfile();
	/*Radix::Net::NetChannel:isCurBusySessionCountOn:isCurBusySessionCountOn-Presentation Property*/


	public class IsCurBusySessionCountOn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsCurBusySessionCountOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isCurBusySessionCountOn:isCurBusySessionCountOn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isCurBusySessionCountOn:isCurBusySessionCountOn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsCurBusySessionCountOn getIsCurBusySessionCountOn();
	/*Radix::Net::NetChannel:curBusySessionCount:curBusySessionCount-Presentation Property*/


	public class CurBusySessionCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurBusySessionCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curBusySessionCount:curBusySessionCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curBusySessionCount:curBusySessionCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurBusySessionCount getCurBusySessionCount();
	/*Radix::Net::NetChannel:fileTraceProfile:fileTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:fileTraceProfile:fileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:fileTraceProfile:fileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileTraceProfile getFileTraceProfile();
	/*Radix::Net::NetChannel:syncMode:syncMode-Presentation Property*/


	public class SyncMode extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public SyncMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:syncMode:syncMode")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:syncMode:syncMode")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public SyncMode getSyncMode();
	/*Radix::Net::NetChannel:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Net::NetChannel:address:address-Presentation Property*/


	public class Address extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Address(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::Net::NetChannel:clientCertAliases:clientCertAliases-Presentation Property*/


	public class ClientCertAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ClientCertAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:clientCertAliases:clientCertAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:clientCertAliases:clientCertAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ClientCertAliases getClientCertAliases();
	/*Radix::Net::NetChannel:requestFrame:requestFrame-Presentation Property*/


	public class RequestFrame extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RequestFrame(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:requestFrame:requestFrame")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:requestFrame:requestFrame")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RequestFrame getRequestFrame();
	/*Radix::Net::NetChannel:isConnectReadyNtfOn:isConnectReadyNtfOn-Presentation Property*/


	public class IsConnectReadyNtfOn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsConnectReadyNtfOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isConnectReadyNtfOn:isConnectReadyNtfOn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isConnectReadyNtfOn:isConnectReadyNtfOn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsConnectReadyNtfOn getIsConnectReadyNtfOn();
	/*Radix::Net::NetChannel:recvTimeout:recvTimeout-Presentation Property*/


	public class RecvTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RecvTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:recvTimeout:recvTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:recvTimeout:recvTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RecvTimeout getRecvTimeout();
	/*Radix::Net::NetChannel:isListener:isListener-Presentation Property*/


	public class IsListener extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsListener(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isListener:isListener")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isListener:isListener")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsListener getIsListener();
	/*Radix::Net::NetChannel:isDisconnectNtfOn:isDisconnectNtfOn-Presentation Property*/


	public class IsDisconnectNtfOn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsDisconnectNtfOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isDisconnectNtfOn:isDisconnectNtfOn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isDisconnectNtfOn:isDisconnectNtfOn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsDisconnectNtfOn getIsDisconnectNtfOn();
	/*Radix::Net::NetChannel:dbTraceProfile:dbTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::Net::NetChannel:activityStatus:activityStatus-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:activityStatus:activityStatus")
		public  org.radixware.ads.System.common.ActivityStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:activityStatus:activityStatus")
		public   void setValue(org.radixware.ads.System.common.ActivityStatus val) {
			Value = val;
		}
	}
	public ActivityStatus getActivityStatus();
	/*Radix::Net::NetChannel:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Net::NetChannel:keyAliases:keyAliases-Presentation Property*/


	public class KeyAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public KeyAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	/*Radix::Net::NetChannel:warning:warning-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:warning:warning")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:warning:warning")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Warning getWarning();
	/*Radix::Net::NetChannel:protocolHandlerClassTitle:protocolHandlerClassTitle-Presentation Property*/


	public class ProtocolHandlerClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProtocolHandlerClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:protocolHandlerClassTitle:protocolHandlerClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:protocolHandlerClassTitle:protocolHandlerClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProtocolHandlerClassTitle getProtocolHandlerClassTitle();
	/*Radix::Net::NetChannel:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


	public class TrustedCertificateAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public TrustedCertificateAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::Net::NetChannel:unitStarted:unitStarted-Presentation Property*/


	public class UnitStarted extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UnitStarted(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitStarted:unitStarted")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitStarted:unitStarted")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UnitStarted getUnitStarted();
	public static class KeystoreAliasesCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected KeystoreAliasesCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}

	public static class StartChannel extends org.radixware.kernel.common.client.models.items.Command{
		protected StartChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.BoolDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.BoolDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.BoolDocument.class);
		}

	}

	public static class StopChannel extends org.radixware.kernel.common.client.models.items.Command{
		protected StopChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.BoolDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.BoolDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.BoolDocument.class);
		}

	}

	public static class SelectProtocolHandler extends org.radixware.kernel.common.client.models.items.Command{
		protected SelectProtocolHandler(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::Net::NetChannel - Desktop Meta*/

/*Radix::Net::NetChannel-Entity Class*/

package org.radixware.ads.Net.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NetChannel_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Net::NetChannel:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
			"Radix::Net::NetChannel",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgNXHCQX6J4LGPHTGXYRDBS73HRIKCJJJ7"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),0,

			/*Radix::Net::NetChannel:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Net::NetChannel:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7A274VUE2DNRDB7AAALOMT5GDM"),
						"unitId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5T53WQMRXOBDCKMAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:curSessionCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),
						"curSessionCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:curSessionCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:maxSessionCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),
						"maxSessionCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:maxSessionCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:linkLevelProtocolKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),
						"linkLevelProtocolKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEQC6IAMF2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:linkLevelProtocolKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEQC6IAMF2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciFSBEJTYZVPORDJHCAANE2UAFXA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:responseFrame:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),
						"responseFrame",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTHJ2QWFTFFCLOMM3VPJKYFUIM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRK5PPCJLJJD3LJ63WX3Y4NAQDI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:responseFrame:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:sendTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),
						"sendTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:sendTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:requestFrame:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),
						"requestFrame",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RH5R64R7FED5CH25VTXXYFBKU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJBC6JM45I5A47EHOW6EUBGWWUE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:requestFrame:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:recvTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),
						"recvTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:recvTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MDSW5BU4RD55E4PD7LRII7IMI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						false,

						/*Radix::Net::NetChannel:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:protocolHandlerClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),
						"protocolHandlerClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						0,
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

						/*Radix::Net::NetChannel:protocolHandlerClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:fileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),
						"fileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5HIWGO3CBCKJF2DLK4EKV3AHI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						false,

						/*Radix::Net::NetChannel:fileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:guiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),
						"guiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGDOE7NJDBEGXNGGJFLH5Y6JMY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						false,

						/*Radix::Net::NetChannel:guiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isConnectReadyNtfOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),
						"isConnectReadyNtfOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4QZHHMPS5CIVHO6ESKJYF2WSA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isConnectReadyNtfOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isDisconnectNtfOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWNURWZD5KBF7DPUPXHUSTXY2UI"),
						"isDisconnectNtfOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5K46V22INGV3JOYZ3SGSNXI4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isDisconnectNtfOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:securityProtocol:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),
						"securityProtocol",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIJ7LRUUUJFYRLFJ2EDDTDT4UI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOQBHNCGYX7NRDB6TAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:securityProtocol:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOQBHNCGYX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:checkClientCert:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),
						"checkClientCert",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLZ4VBD2PVEORBAOUFIYFKMFKA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs5YMQPO4VDRHS3IIM4EK3AHKYVM"),
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

						/*Radix::Net::NetChannel:checkClientCert:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs5YMQPO4VDRHS3IIM4EK3AHKYVM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:serverKeyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),
						"serverKeyAliases",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VAGMA4TKJEJJKT7W6AWFLIUVI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:serverKeyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFGQZ4Z6URE4DNYFOAUT7HS66E"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:clientCertAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),
						"clientCertAliases",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JL52KHQRZCZTEMWGIW3VMOB2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:clientCertAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVVBB7NDPNFJTEUOBTIH2BJRGI"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLSMYTVUFZADBFMEEGYBGAUZLY"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EFJLI7OGNCD5MUZNLNIHYZ5UI"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJIKGBMNQ4ZGZ3PGEHPR3PWJO2U"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BCWLS5AOBEAHFAI5R5OP7BWKE"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isListener:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW7RFZLTFEZECHEAFABQSI62TGY"),
						"isListener",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isListener:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZR77AUTFPJEOLJW7SFEZS4LGUE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:keepConnectTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),
						"keepConnectTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIMKW7A7JVE5FF43YDKV2BSXM4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:keepConnectTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVJ4H6CAG5CDRK2WRCDNLP26NQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:unitStarted:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),
						"unitStarted",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XKAUFW2VJEILM4WAOCFVIZEA4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:unitStarted:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:use:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),
						"use",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIRJF2U6E7JGRFFM4J7O6SSTTEI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:use:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:useKeepalive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),
						"useKeepalive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBB45RR7ODNCUDMZBNAJ2ZRHYPQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMASG324UH5CMPNM63ASKI2F3OU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:useKeepalive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:warning:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHUJ5FP6UTZAXJCTV2K62TA6J7Y"),
						"warning",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:warning:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:syncMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),
						"syncMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG6JAYG5NNEUHNYMKK3CNU6VJA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:syncMode:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:curBusySessionCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),
						"curBusySessionCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DWFL3RYVJADBAZBTWPX6TRTFE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:curBusySessionCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isCurBusySessionCountOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),
						"isCurBusySessionCountOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLAIWKURQQZEXVNBH4BOJAJBSXA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isCurBusySessionCountOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:activityStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),
						"activityStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFGKOK6XKBDRJCHDHFQIOAZB5Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:activityStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:aadcAffinityHandler:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHTSOBNUDRVEBJM4OLCOZ2FAFK4"),
						"aadcAffinityHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINZIHDW3AVBRHEWU4FMU2GYALA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:aadcAffinityHandler:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCH2VDVIJO5DZDOZD26C5NZW4LI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::NetChannel:selectProtocolHandler-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ4MWUCGC7VGD7LS4JV2ZCMX7NA"),
						"selectProtocolHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLBFKUK4OXOBDFKUAAMPGXUWTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgS7NO7SKNZCYU3CYW3MGDKHIQSATXPWHS"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::NetChannel:keystoreAliasesCmd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdI5GJG6TPRJF3DP6DQRIWL456KE"),
						"keystoreAliasesCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WD6TJI4NBHTTDRP7JYLGLAXUM"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::NetChannel:startChannel-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJ3CH6OMXZ5GUBC2TB5AWRSTL3U"),
						"startChannel",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYSX3CUMFREJ7CL653VDRHOVJA"),
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
					/*Radix::Net::NetChannel:stopChannel-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLN64DGQYQFHPZDIFDDCZTPLIYI"),
						"stopChannel",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQS7RVYNNIZAJBPOKELX6F4EZYI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTKXKTEYVYAYQDB4RIFDVF4MH2E4QJX5V"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refLP2PLKPZ3DNRDISQAAAAAAAAAA"),"NetChannel=>Unit (unitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col7A274VUE2DNRDB7AAALOMT5GDM")},new String[]{"unitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM")},
			true,true,false);
}

/* Radix::Net::NetChannel - Web Executable*/

/*Radix::Net::NetChannel-Entity Class*/

package org.radixware.ads.Net.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel")
public interface NetChannel {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}
		public org.radixware.ads.Net.web.NetChannel.NetChannel_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.Net.web.NetChannel.NetChannel_DefaultModel )  super.getEntity(i);}
	}





























































































































































































































	/*Radix::Net::NetChannel:useKeepalive:useKeepalive-Presentation Property*/


	public class UseKeepalive extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseKeepalive(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:useKeepalive:useKeepalive")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:useKeepalive:useKeepalive")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseKeepalive getUseKeepalive();
	/*Radix::Net::NetChannel:use:use-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:use:use")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:use:use")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Use getUse();
	/*Radix::Net::NetChannel:serverKeyAliases:serverKeyAliases-Presentation Property*/


	public class ServerKeyAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ServerKeyAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:serverKeyAliases:serverKeyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:serverKeyAliases:serverKeyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ServerKeyAliases getServerKeyAliases();
	/*Radix::Net::NetChannel:unitId:unitId-Presentation Property*/


	public class UnitId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public UnitId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::Net::NetChannel:curSessionCount:curSessionCount-Presentation Property*/


	public class CurSessionCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurSessionCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curSessionCount:curSessionCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curSessionCount:curSessionCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurSessionCount getCurSessionCount();
	/*Radix::Net::NetChannel:keepConnectTimeout:keepConnectTimeout-Presentation Property*/


	public class KeepConnectTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public KeepConnectTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keepConnectTimeout:keepConnectTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keepConnectTimeout:keepConnectTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public KeepConnectTimeout getKeepConnectTimeout();
	/*Radix::Net::NetChannel:maxSessionCount:maxSessionCount-Presentation Property*/


	public class MaxSessionCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxSessionCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:maxSessionCount:maxSessionCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:maxSessionCount:maxSessionCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxSessionCount getMaxSessionCount();
	/*Radix::Net::NetChannel:rid:rid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:rid:rid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:rid:rid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Rid getRid();
	/*Radix::Net::NetChannel:linkLevelProtocolKind:linkLevelProtocolKind-Presentation Property*/


	public class LinkLevelProtocolKind extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LinkLevelProtocolKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Net.common.LinkLevelProtocolKind> getValClass(){
			return org.radixware.ads.Net.common.LinkLevelProtocolKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:linkLevelProtocolKind:linkLevelProtocolKind")
		public  org.radixware.ads.Net.common.LinkLevelProtocolKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:linkLevelProtocolKind:linkLevelProtocolKind")
		public   void setValue(org.radixware.ads.Net.common.LinkLevelProtocolKind val) {
			Value = val;
		}
	}
	public LinkLevelProtocolKind getLinkLevelProtocolKind();
	/*Radix::Net::NetChannel:securityProtocol:securityProtocol-Presentation Property*/


	public class SecurityProtocol extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SecurityProtocol(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.PortSecurityProtocol dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.PortSecurityProtocol ? (org.radixware.ads.System.common.PortSecurityProtocol)x : org.radixware.ads.System.common.PortSecurityProtocol.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.PortSecurityProtocol> getValClass(){
			return org.radixware.ads.System.common.PortSecurityProtocol.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.PortSecurityProtocol dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.PortSecurityProtocol ? (org.radixware.ads.System.common.PortSecurityProtocol)x : org.radixware.ads.System.common.PortSecurityProtocol.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:securityProtocol:securityProtocol")
		public  org.radixware.ads.System.common.PortSecurityProtocol getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:securityProtocol:securityProtocol")
		public   void setValue(org.radixware.ads.System.common.PortSecurityProtocol val) {
			Value = val;
		}
	}
	public SecurityProtocol getSecurityProtocol();
	/*Radix::Net::NetChannel:aadcAffinityHandler:aadcAffinityHandler-Presentation Property*/


	public class AadcAffinityHandler extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AadcAffinityHandler(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:aadcAffinityHandler:aadcAffinityHandler")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:aadcAffinityHandler:aadcAffinityHandler")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AadcAffinityHandler getAadcAffinityHandler();
	/*Radix::Net::NetChannel:checkClientCert:checkClientCert-Presentation Property*/


	public class CheckClientCert extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CheckClientCert(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EClientAuthentication dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EClientAuthentication ? (org.radixware.kernel.common.enums.EClientAuthentication)x : org.radixware.kernel.common.enums.EClientAuthentication.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EClientAuthentication> getValClass(){
			return org.radixware.kernel.common.enums.EClientAuthentication.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EClientAuthentication dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EClientAuthentication ? (org.radixware.kernel.common.enums.EClientAuthentication)x : org.radixware.kernel.common.enums.EClientAuthentication.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkClientCert:checkClientCert")
		public  org.radixware.kernel.common.enums.EClientAuthentication getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:checkClientCert:checkClientCert")
		public   void setValue(org.radixware.kernel.common.enums.EClientAuthentication val) {
			Value = val;
		}
	}
	public CheckClientCert getCheckClientCert();
	/*Radix::Net::NetChannel:responseFrame:responseFrame-Presentation Property*/


	public class ResponseFrame extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ResponseFrame(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:responseFrame:responseFrame")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:responseFrame:responseFrame")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ResponseFrame getResponseFrame();
	/*Radix::Net::NetChannel:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::Net::NetChannel:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::Net::NetChannel:sendTimeout:sendTimeout-Presentation Property*/


	public class SendTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SendTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:sendTimeout:sendTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:sendTimeout:sendTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SendTimeout getSendTimeout();
	/*Radix::Net::NetChannel:guiTraceProfile:guiTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:guiTraceProfile:guiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:guiTraceProfile:guiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public GuiTraceProfile getGuiTraceProfile();
	/*Radix::Net::NetChannel:isCurBusySessionCountOn:isCurBusySessionCountOn-Presentation Property*/


	public class IsCurBusySessionCountOn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsCurBusySessionCountOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isCurBusySessionCountOn:isCurBusySessionCountOn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isCurBusySessionCountOn:isCurBusySessionCountOn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsCurBusySessionCountOn getIsCurBusySessionCountOn();
	/*Radix::Net::NetChannel:curBusySessionCount:curBusySessionCount-Presentation Property*/


	public class CurBusySessionCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CurBusySessionCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curBusySessionCount:curBusySessionCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:curBusySessionCount:curBusySessionCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CurBusySessionCount getCurBusySessionCount();
	/*Radix::Net::NetChannel:fileTraceProfile:fileTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:fileTraceProfile:fileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:fileTraceProfile:fileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileTraceProfile getFileTraceProfile();
	/*Radix::Net::NetChannel:syncMode:syncMode-Presentation Property*/


	public class SyncMode extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public SyncMode(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:syncMode:syncMode")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:syncMode:syncMode")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public SyncMode getSyncMode();
	/*Radix::Net::NetChannel:classGuid:classGuid-Presentation Property*/


	public class ClassGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ClassGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::Net::NetChannel:address:address-Presentation Property*/


	public class Address extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Address(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:address:address")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:address:address")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Address getAddress();
	/*Radix::Net::NetChannel:clientCertAliases:clientCertAliases-Presentation Property*/


	public class ClientCertAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public ClientCertAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:clientCertAliases:clientCertAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:clientCertAliases:clientCertAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public ClientCertAliases getClientCertAliases();
	/*Radix::Net::NetChannel:requestFrame:requestFrame-Presentation Property*/


	public class RequestFrame extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public RequestFrame(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:requestFrame:requestFrame")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:requestFrame:requestFrame")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public RequestFrame getRequestFrame();
	/*Radix::Net::NetChannel:isConnectReadyNtfOn:isConnectReadyNtfOn-Presentation Property*/


	public class IsConnectReadyNtfOn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsConnectReadyNtfOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isConnectReadyNtfOn:isConnectReadyNtfOn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isConnectReadyNtfOn:isConnectReadyNtfOn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsConnectReadyNtfOn getIsConnectReadyNtfOn();
	/*Radix::Net::NetChannel:recvTimeout:recvTimeout-Presentation Property*/


	public class RecvTimeout extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public RecvTimeout(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:recvTimeout:recvTimeout")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:recvTimeout:recvTimeout")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public RecvTimeout getRecvTimeout();
	/*Radix::Net::NetChannel:isListener:isListener-Presentation Property*/


	public class IsListener extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsListener(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isListener:isListener")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isListener:isListener")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsListener getIsListener();
	/*Radix::Net::NetChannel:isDisconnectNtfOn:isDisconnectNtfOn-Presentation Property*/


	public class IsDisconnectNtfOn extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsDisconnectNtfOn(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isDisconnectNtfOn:isDisconnectNtfOn")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:isDisconnectNtfOn:isDisconnectNtfOn")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsDisconnectNtfOn getIsDisconnectNtfOn();
	/*Radix::Net::NetChannel:dbTraceProfile:dbTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::Net::NetChannel:activityStatus:activityStatus-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:activityStatus:activityStatus")
		public  org.radixware.ads.System.common.ActivityStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:activityStatus:activityStatus")
		public   void setValue(org.radixware.ads.System.common.ActivityStatus val) {
			Value = val;
		}
	}
	public ActivityStatus getActivityStatus();
	/*Radix::Net::NetChannel:classTitle:classTitle-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classTitle:classTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:classTitle:classTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassTitle getClassTitle();
	/*Radix::Net::NetChannel:keyAliases:keyAliases-Presentation Property*/


	public class KeyAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public KeyAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	/*Radix::Net::NetChannel:warning:warning-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:warning:warning")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:warning:warning")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Warning getWarning();
	/*Radix::Net::NetChannel:protocolHandlerClassTitle:protocolHandlerClassTitle-Presentation Property*/


	public class ProtocolHandlerClassTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ProtocolHandlerClassTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:protocolHandlerClassTitle:protocolHandlerClassTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:protocolHandlerClassTitle:protocolHandlerClassTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ProtocolHandlerClassTitle getProtocolHandlerClassTitle();
	/*Radix::Net::NetChannel:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


	public class TrustedCertificateAliases extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public TrustedCertificateAliases(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::Net::NetChannel:unitStarted:unitStarted-Presentation Property*/


	public class UnitStarted extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UnitStarted(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitStarted:unitStarted")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:unitStarted:unitStarted")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UnitStarted getUnitStarted();
	public static class KeystoreAliasesCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected KeystoreAliasesCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}

	public static class StartChannel extends org.radixware.kernel.common.client.models.items.Command{
		protected StartChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.BoolDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.BoolDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.BoolDocument.class);
		}

	}

	public static class StopChannel extends org.radixware.kernel.common.client.models.items.Command{
		protected StopChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.types.BoolDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.types.BoolDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.types.BoolDocument.class);
		}

	}

	public static class SelectProtocolHandler extends org.radixware.kernel.common.client.models.items.Command{
		protected SelectProtocolHandler(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::Net::NetChannel - Web Meta*/

/*Radix::Net::NetChannel-Entity Class*/

package org.radixware.ads.Net.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NetChannel_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::Net::NetChannel:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
			"Radix::Net::NetChannel",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgNXHCQX6J4LGPHTGXYRDBS73HRIKCJJJ7"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDBFKUK4OXOBDFKUAAMPGXUWTQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),0,

			/*Radix::Net::NetChannel:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::Net::NetChannel:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7A274VUE2DNRDB7AAALOMT5GDM"),
						"unitId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5T53WQMRXOBDCKMAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:curSessionCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),
						"curSessionCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:curSessionCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:maxSessionCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),
						"maxSessionCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:maxSessionCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:linkLevelProtocolKind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),
						"linkLevelProtocolKind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEQC6IAMF2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:linkLevelProtocolKind:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsEQC6IAMF2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.EXCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aciFSBEJTYZVPORDJHCAANE2UAFXA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:responseFrame:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),
						"responseFrame",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTHJ2QWFTFFCLOMM3VPJKYFUIM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRK5PPCJLJJD3LJ63WX3Y4NAQDI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:responseFrame:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:sendTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),
						"sendTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:sendTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:address:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),
						"address",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3BFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:address:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:requestFrame:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),
						"requestFrame",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RH5R64R7FED5CH25VTXXYFBKU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJBC6JM45I5A47EHOW6EUBGWWUE"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("%[2R]L%P"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:requestFrame:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:recvTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),
						"recvTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:recvTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MDSW5BU4RD55E4PD7LRII7IMI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeDPJZYTP5U5AZHJI724SY2T5B6A"),
						false,

						/*Radix::Net::NetChannel:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:protocolHandlerClassTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),
						"protocolHandlerClassTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHBFKUK4OXOBDFKUAAMPGXUWTQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						0,
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

						/*Radix::Net::NetChannel:protocolHandlerClassTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:fileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),
						"fileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5HIWGO3CBCKJF2DLK4EKV3AHI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeDPJZYTP5U5AZHJI724SY2T5B6A"),
						false,

						/*Radix::Net::NetChannel:fileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:guiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),
						"guiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGDOE7NJDBEGXNGGJFLH5Y6JMY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("cpeDPJZYTP5U5AZHJI724SY2T5B6A"),
						false,

						/*Radix::Net::NetChannel:guiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isConnectReadyNtfOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUUCCDQOTEJG77LPBXMHD6QDK5E"),
						"isConnectReadyNtfOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4QZHHMPS5CIVHO6ESKJYF2WSA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isConnectReadyNtfOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isDisconnectNtfOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colWNURWZD5KBF7DPUPXHUSTXY2UI"),
						"isDisconnectNtfOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5K46V22INGV3JOYZ3SGSNXI4I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isDisconnectNtfOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:securityProtocol:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),
						"securityProtocol",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIJ7LRUUUJFYRLFJ2EDDTDT4UI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOQBHNCGYX7NRDB6TAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:securityProtocol:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsOQBHNCGYX7NRDB6TAALOMT5GDM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:checkClientCert:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),
						"checkClientCert",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLZ4VBD2PVEORBAOUFIYFKMFKA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs5YMQPO4VDRHS3IIM4EK3AHKYVM"),
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

						/*Radix::Net::NetChannel:checkClientCert:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs5YMQPO4VDRHS3IIM4EK3AHKYVM"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:serverKeyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),
						"serverKeyAliases",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VAGMA4TKJEJJKT7W6AWFLIUVI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:serverKeyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFGQZ4Z6URE4DNYFOAUT7HS66E"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:clientCertAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),
						"clientCertAliases",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JL52KHQRZCZTEMWGIW3VMOB2U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.PROGRAMMATICALLY,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:clientCertAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVVBB7NDPNFJTEUOBTIH2BJRGI"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdDLSMYTVUFZADBFMEEGYBGAUZLY"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EFJLI7OGNCD5MUZNLNIHYZ5UI"),
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJIKGBMNQ4ZGZ3PGEHPR3PWJO2U"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,true,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BCWLS5AOBEAHFAI5R5OP7BWKE"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isListener:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colW7RFZLTFEZECHEAFABQSI62TGY"),
						"isListener",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isListener:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:classTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),
						"classTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZR77AUTFPJEOLJW7SFEZS4LGUE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:classTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:keepConnectTimeout:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),
						"keepConnectTimeout",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIMKW7A7JVE5FF43YDKV2BSXM4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:keepConnectTimeout:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVJ4H6CAG5CDRK2WRCDNLP26NQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:unitStarted:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),
						"unitStarted",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XKAUFW2VJEILM4WAOCFVIZEA4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:unitStarted:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:use:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),
						"use",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIRJF2U6E7JGRFFM4J7O6SSTTEI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:use:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:useKeepalive:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),
						"useKeepalive",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBB45RR7ODNCUDMZBNAJ2ZRHYPQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMASG324UH5CMPNM63ASKI2F3OU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:useKeepalive:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:warning:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdHUJ5FP6UTZAXJCTV2K62TA6J7Y"),
						"warning",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:warning:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:syncMode:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),
						"syncMode",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG6JAYG5NNEUHNYMKK3CNU6VJA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:syncMode:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:curBusySessionCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),
						"curBusySessionCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DWFL3RYVJADBAZBTWPX6TRTFE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:curBusySessionCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:isCurBusySessionCountOn:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),
						"isCurBusySessionCountOn",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLAIWKURQQZEXVNBH4BOJAJBSXA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:isCurBusySessionCountOn:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:activityStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),
						"activityStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFGKOK6XKBDRJCHDHFQIOAZB5Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::Net::NetChannel:activityStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:aadcAffinityHandler:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHTSOBNUDRVEBJM4OLCOZ2FAFK4"),
						"aadcAffinityHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINZIHDW3AVBRHEWU4FMU2GYALA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:aadcAffinityHandler:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::Net::NetChannel:rid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),
						"rid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCH2VDVIJO5DZDOZD26C5NZW4LI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
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

						/*Radix::Net::NetChannel:rid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::NetChannel:selectProtocolHandler-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdZ4MWUCGC7VGD7LS4JV2ZCMX7NA"),
						"selectProtocolHandler",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLBFKUK4OXOBDFKUAAMPGXUWTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgS7NO7SKNZCYU3CYW3MGDKHIQSATXPWHS"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.LOCAL,
						false,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::NetChannel:keystoreAliasesCmd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdI5GJG6TPRJF3DP6DQRIWL456KE"),
						"keystoreAliasesCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WD6TJI4NBHTTDRP7JYLGLAXUM"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::Net::NetChannel:startChannel-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdJ3CH6OMXZ5GUBC2TB5AWRSTL3U"),
						"startChannel",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYSX3CUMFREJ7CL653VDRHOVJA"),
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
					/*Radix::Net::NetChannel:stopChannel-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLN64DGQYQFHPZDIFDDCZTPLIYI"),
						"stopChannel",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQS7RVYNNIZAJBPOKELX6F4EZYI"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgTKXKTEYVYAYQDB4RIFDVF4MH2E4QJX5V"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			null,
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refLP2PLKPZ3DNRDISQAAAAAAAAAA"),"NetChannel=>Unit (unitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col7A274VUE2DNRDB7AAALOMT5GDM")},new String[]{"unitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM")},
			true,true,false);
}

/* Radix::Net::NetChannel:General - Desktop Meta*/

/*Radix::Net::NetChannel:General-Editor Presentation*/

package org.radixware.ads.Net.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
	null,
	null,

	/*Radix::Net::NetChannel:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Net::NetChannel:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYN5EJB2VMJAZ3KBALBGUS3VCCA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),1,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),0,14,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),0,15,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),0,16,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),0,17,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),0,13,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),1,11,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),0,18,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),0,8,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),1,19,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),0,1,2,false,false)
			},null),

			/*Radix::Net::NetChannel:General:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYHFJSF7EPRGXFAM55A75C3IGQQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),0,2,1,false,false)
			},null),

			/*Radix::Net::NetChannel:General:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiWIWWGSLLTBEKDDDOIS7UELOPI4"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"))}
	,

	/*Radix::Net::NetChannel:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Net::NetChannel:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiWIWWGSLLTBEKDDDOIS7UELOPI4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,
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
/* Radix::Net::NetChannel:General - Web Meta*/

/*Radix::Net::NetChannel:General-Editor Presentation*/

package org.radixware.ads.Net.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
	null,
	null,

	/*Radix::Net::NetChannel:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::Net::NetChannel:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYN5EJB2VMJAZ3KBALBGUS3VCCA"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),0,0,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),0,3,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),0,6,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGRXTHMME2DNRDB7AAALOMT5GDM"),0,10,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),0,4,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF3F5Y5EF2DNRDB7AAALOMT5GDM"),0,7,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colVGDODIF6BLOBDCGJAALOMT5GDM"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKSQETMV6BLOBDCGJAALOMT5GDM"),1,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colH2W5NJG535G7TCZERIU3I2KW6I"),0,14,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6OTLV7WBZVGDFM7SYNM5KXYSGQ"),0,15,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSOCMOJR6SRCGBL63P2ALGCKNUQ"),0,16,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5R5UG4B5JEUPMEJLRID7SISTY"),0,17,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),0,2,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3UVBAV46ZHJ5POIV5R3JDFQ2E"),0,13,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTFACUXG6NJAMNJZ33CKFZTYORY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJWZOTJWGMFBWDMHWTBSXO6MZNY"),1,11,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),0,5,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col22YVF3LEOFE5BCJXA5KDPU73NA"),0,18,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPRKQ2IQSPJDUJFZWAVDVZYN6ZI"),0,8,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colN7MFLE4DNJB3RIQUOC2I37BXPI"),1,19,1,true,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVZZGWK24VGBBAQGW4QAVPJ4GY"),0,9,2,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGEUOICAGRBAU3NBME7T63HEL7I"),0,1,2,false,false)
			},null),

			/*Radix::Net::NetChannel:General:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYHFJSF7EPRGXFAM55A75C3IGQQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXOEZ5O4YNXOBDCJJAALOMT5GDM"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVKQVABX6FH7PAEUHL7URGEEAY"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNXKEWDHZK5CILMRUHOP7KW46Q4"),0,2,1,false,false)
			},null),

			/*Radix::Net::NetChannel:General:EventLog-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"),"EventLog",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiWIWWGSLLTBEKDDDOIS7UELOPI4"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg5562VEGTBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgCWCNH76WQNGY5IUPVIUKKE4CCQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg2OZOQGDAB5G2XP3FXEHO6W53BQ"))}
	,

	/*Radix::Net::NetChannel:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::Net::NetChannel:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiWIWWGSLLTBEKDDDOIS7UELOPI4"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),null,
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
/* Radix::Net::NetChannel:General:Model - Desktop Executable*/

/*Radix::Net::NetChannel:General:Model-Entity Model Class*/

package org.radixware.ads.Net.explorer;

//import com.trolltech.qt.gui.QDialog;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model")
public class General:Model  extends org.radixware.ads.Net.explorer.NetChannel.NetChannel_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Net::NetChannel:General:Model:Nested classes-Nested Classes*/

	/*Radix::Net::NetChannel:General:Model:Properties-Properties*/

	/*Radix::Net::NetChannel:General:Model:securityProtocol-Presentation Property*/




	public class SecurityProtocol extends org.radixware.ads.Net.explorer.NetChannel.colH2W5NJG535G7TCZERIU3I2KW6I{
		public SecurityProtocol(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.System.common.PortSecurityProtocol dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.PortSecurityProtocol ? (org.radixware.ads.System.common.PortSecurityProtocol)x : org.radixware.ads.System.common.PortSecurityProtocol.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.System.common.PortSecurityProtocol> getValClass(){
			return org.radixware.ads.System.common.PortSecurityProtocol.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.System.common.PortSecurityProtocol dummy = x == null ? null : (x instanceof org.radixware.ads.System.common.PortSecurityProtocol ? (org.radixware.ads.System.common.PortSecurityProtocol)x : org.radixware.ads.System.common.PortSecurityProtocol.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:securityProtocol")
		public published  org.radixware.ads.System.common.PortSecurityProtocol getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:securityProtocol")
		public published   void setValue(org.radixware.ads.System.common.PortSecurityProtocol val) {

			internal[securityProtocol] = val;
			setVisibility();
		}
	}
	public SecurityProtocol getSecurityProtocol(){return (SecurityProtocol)getProperty(colH2W5NJG535G7TCZERIU3I2KW6I);}

	/*Radix::Net::NetChannel:General:Model:linkLevelProtocolKind-Presentation Property*/




	public class LinkLevelProtocolKind extends org.radixware.ads.Net.explorer.NetChannel.colGRXTHMME2DNRDB7AAALOMT5GDM{
		public LinkLevelProtocolKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Net.common.LinkLevelProtocolKind> getValClass(){
			return org.radixware.ads.Net.common.LinkLevelProtocolKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:linkLevelProtocolKind")
		public published  org.radixware.ads.Net.common.LinkLevelProtocolKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:linkLevelProtocolKind")
		public published   void setValue(org.radixware.ads.Net.common.LinkLevelProtocolKind val) {

			internal[linkLevelProtocolKind] = val;
			setVisibility();
		}
	}
	public LinkLevelProtocolKind getLinkLevelProtocolKind(){return (LinkLevelProtocolKind)getProperty(colGRXTHMME2DNRDB7AAALOMT5GDM);}










	/*Radix::Net::NetChannel:General:Model:Methods-Methods*/

	/*Radix::Net::NetChannel:General:Model:onCommand_selectProtocolHandler-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:onCommand_selectProtocolHandler")
	private final  void onCommand_selectProtocolHandler (org.radixware.ads.Net.explorer.NetChannel.SelectProtocolHandler command, org.radixware.kernel.common.types.Id propertyId) {
		// dlg = new ();
		// model = ()dlg.getModel();
		//model. = .getTitle();
		//model. = .Factory.newInstance();
		//model..addNewSuperClass().QName = "org.radixware.kernel.server.net.NetProtocolHandler";
		//model. = .getValue();
		//if(dlg.exec() == QDialog.DialogCode.Accepted.value())
		//{
		//  .setValue(model..Id.toString());
		//  .setValue(model..Name);
		//}
	}

	/*Radix::Net::NetChannel:General:Model:fillKeystoreMaskLists-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:fillKeystoreMaskLists")
	protected published  void fillKeystoreMaskLists () {
		if (keyAliases != null && keyAliases.getValue() != null) {
		    serverKeyAliases.setPredefinedValuesForArrayItem(new java.util.ArrayList<Object>(keyAliases.Value));
		}
		if (trustedCertificateAliases != null && trustedCertificateAliases.getValue() != null) {
		    clientCertAliases.setPredefinedValuesForArrayItem(new java.util.ArrayList<Object>(trustedCertificateAliases.Value));
		}
	}

	/*Radix::Net::NetChannel:General:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:setVisibility")
	public published  void setVisibility () {
		boolean isSsl = securityProtocol.Value != System::PortSecurityProtocol:None;
		checkClientCert.setVisible(isSsl);
		serverKeyAliases.setVisible(isSsl);
		clientCertAliases.setVisible(isSsl);
		requestFrame.setVisible(linkLevelProtocolKind.Value != LinkLevelProtocolKind:POS2);
		responseFrame.setVisible(linkLevelProtocolKind.Value != LinkLevelProtocolKind:POS2);
		linkLevelProtocolKind.setVisible(false);

		final boolean isSync = syncMode.Value != null && syncMode.Value;
		curBusySessionCount.setVisible(isSync);
		isCurBusySessionCountOn.setVisible(isSync);

		if (isNew()) {
		    curSessionCount.setVisible(false);
		    curBusySessionCount.setVisible(false);
		}

	}

	/*Radix::Net::NetChannel:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!isInSelectorRowContext()) {
		    setVisibility();
		}
		if (!isNew() && getRestrictions().canBeAllowed(Meta::PresentationRestriction:DELETE)) {
		    getRestrictions().setDeleteRestricted(curSessionCount.Value.longValue() >= 0);
		}


	}

	/*Radix::Net::NetChannel:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:getContextId")
	public published  Str getContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::Net::NetChannel:General:Model:getContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:getContextType")
	public published  Str getContextType () {
		return Arte::EventContextType:NetChannel.Value;
	}

	/*Radix::Net::NetChannel:General:Model:onCommand_startChannel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:onCommand_startChannel")
	public  void onCommand_startChannel (org.radixware.ads.Net.explorer.NetChannel.StartChannel command) {
		try {
		    Arte::TypesXsd:BoolDocument xOut = command.send();
		    if (xOut.Bool == Boolean.TRUE) {
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Channel started");
		    } else {
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Unable to start the channel");
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Net::NetChannel:General:Model:onCommand_stopChannel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:onCommand_stopChannel")
	public  void onCommand_stopChannel (org.radixware.ads.Net.explorer.NetChannel.StopChannel command) {
		try {
		    Arte::TypesXsd:BoolDocument xOut = command.send();
		    if (xOut.Bool == Boolean.TRUE) {
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Channel stopped");
		    } else {
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Unable to stop the channel");
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Net::NetChannel:General:Model:keystoreAliasesCmd-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:keystoreAliasesCmd")
	public  void keystoreAliasesCmd (org.radixware.ads.Net.explorer.NetChannel.KeystoreAliasesCmd command, org.radixware.kernel.common.types.Id propertyId) {
		fillKeystoreMaskLists();

		Explorer.Models.Properties::PropertyArrStr property = (Explorer.Models.Properties::PropertyArrStr)this.properties.get(propertyId);
		final org.radixware.kernel.common.client.views.IArrayEditorDialog dialog = property.getEditorDialog(null);
		dialog.setReadonly(property.isReadonly());
		if (dialog.execDialog()==org.radixware.kernel.common.client.views.IDialog.DialogResult.ACCEPTED){
		    property.setValueObject(dialog.getCurrentValue());
		}
	}

	/*Radix::Net::NetChannel:General:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		boolean started = curSessionCount.Value != null && curSessionCount.Value.longValue() >= 0;
		if (idof[NetChannel:startChannel].equals(command.Id)) {
		    return unitStarted.Value == true && !started && use.Value == true;
		}
		if (idof[NetChannel:stopChannel].equals(command.Id)) {
		    return unitStarted.Value == true && started;
		}
		return super.isCommandEnabled(command);
	}

	/*Radix::Net::NetChannel:General:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterCreate();
		try {
		    readProperty(idof[NetChannel:warning]);
		} catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		    Environment.getTracer().error("Error on read warning property", ex);
		}
		if (warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}

	/*Radix::Net::NetChannel:General:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterUpdate();
		try {
		    readProperty(idof[NetChannel:warning]);
		} catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		    Environment.getTracer().error("Error on read warning property", ex);
		}
		if(warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}
	public final class KeystoreAliasesCmd extends org.radixware.ads.Net.explorer.NetChannel.KeystoreAliasesCmd{
		protected KeystoreAliasesCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_keystoreAliasesCmd( this, propertyId );
		}

	}

	public final class StartChannel extends org.radixware.ads.Net.explorer.NetChannel.StartChannel{
		protected StartChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_startChannel( this );
		}

	}

	public final class StopChannel extends org.radixware.ads.Net.explorer.NetChannel.StopChannel{
		protected StopChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_stopChannel( this );
		}

	}

	public final class SelectProtocolHandler extends org.radixware.ads.Net.explorer.NetChannel.SelectProtocolHandler{
		protected SelectProtocolHandler(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_selectProtocolHandler( this, propertyId );
		}

	}



















}

/* Radix::Net::NetChannel:General:Model - Desktop Meta*/

/*Radix::Net::NetChannel:General:Model-Entity Model Class*/

package org.radixware.ads.Net.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFCNRWSZS2HNRDB7BAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Net::NetChannel:General:Model:Properties-Properties*/
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

/* Radix::Net::NetChannel:General:Model - Web Executable*/

/*Radix::Net::NetChannel:General:Model-Entity Model Class*/

package org.radixware.ads.Net.web;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model")
public class General:Model  extends org.radixware.ads.Net.web.NetChannel.NetChannel_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::Net::NetChannel:General:Model:Nested classes-Nested Classes*/

	/*Radix::Net::NetChannel:General:Model:Properties-Properties*/

	/*Radix::Net::NetChannel:General:Model:linkLevelProtocolKind-Presentation Property*/




	public class LinkLevelProtocolKind extends org.radixware.ads.Net.web.NetChannel.colGRXTHMME2DNRDB7AAALOMT5GDM{
		public LinkLevelProtocolKind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Net.common.LinkLevelProtocolKind> getValClass(){
			return org.radixware.ads.Net.common.LinkLevelProtocolKind.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Net.common.LinkLevelProtocolKind dummy = x == null ? null : (x instanceof org.radixware.ads.Net.common.LinkLevelProtocolKind ? (org.radixware.ads.Net.common.LinkLevelProtocolKind)x : org.radixware.ads.Net.common.LinkLevelProtocolKind.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:linkLevelProtocolKind")
		public published  org.radixware.ads.Net.common.LinkLevelProtocolKind getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:linkLevelProtocolKind")
		public published   void setValue(org.radixware.ads.Net.common.LinkLevelProtocolKind val) {

			internal[linkLevelProtocolKind] = val;
			setVisibility();
		}
	}
	public LinkLevelProtocolKind getLinkLevelProtocolKind(){return (LinkLevelProtocolKind)getProperty(colGRXTHMME2DNRDB7AAALOMT5GDM);}








	/*Radix::Net::NetChannel:General:Model:Methods-Methods*/

	/*Radix::Net::NetChannel:General:Model:onCommand_selectProtocolHandler-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:onCommand_selectProtocolHandler")
	private final  void onCommand_selectProtocolHandler (org.radixware.ads.Net.web.NetChannel.SelectProtocolHandler command, org.radixware.kernel.common.types.Id propertyId) {
		// dlg = new ();
		// model = ()dlg.getModel();
		//model. = .getTitle();
		//model. = .Factory.newInstance();
		//model..addNewSuperClass().QName = "org.radixware.kernel.server.net.NetProtocolHandler";
		//model. = .getValue();
		//if(dlg.exec() == QDialog.DialogCode.Accepted.value())
		//{
		//  .setValue(model..Id.toString());
		//  .setValue(model..Name);
		//}
	}

	/*Radix::Net::NetChannel:General:Model:fillKeystoreMaskLists-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:fillKeystoreMaskLists")
	protected published  void fillKeystoreMaskLists () {
		if (keyAliases != null && keyAliases.getValue() != null) {
		    serverKeyAliases.setPredefinedValuesForArrayItem(new java.util.ArrayList<Object>(keyAliases.Value));
		}
		if (trustedCertificateAliases != null && trustedCertificateAliases.getValue() != null) {
		    clientCertAliases.setPredefinedValuesForArrayItem(new java.util.ArrayList<Object>(trustedCertificateAliases.Value));
		}
	}

	/*Radix::Net::NetChannel:General:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:setVisibility")
	public published  void setVisibility () {
		boolean isSsl = securityProtocol.Value != System::PortSecurityProtocol:None;
		checkClientCert.setVisible(isSsl);
		serverKeyAliases.setVisible(isSsl);
		clientCertAliases.setVisible(isSsl);
		requestFrame.setVisible(linkLevelProtocolKind.Value != LinkLevelProtocolKind:POS2);
		responseFrame.setVisible(linkLevelProtocolKind.Value != LinkLevelProtocolKind:POS2);
		linkLevelProtocolKind.setVisible(false);

		final boolean isSync = syncMode.Value != null && syncMode.Value;
		curBusySessionCount.setVisible(isSync);
		isCurBusySessionCountOn.setVisible(isSync);

		if (isNew()) {
		    curSessionCount.setVisible(false);
		    curBusySessionCount.setVisible(false);
		}

	}

	/*Radix::Net::NetChannel:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if (!isInSelectorRowContext()) {
		    setVisibility();
		}
		if (!isNew() && getRestrictions().canBeAllowed(Meta::PresentationRestriction:DELETE)) {
		    getRestrictions().setDeleteRestricted(curSessionCount.Value.longValue() >= 0);
		}


	}

	/*Radix::Net::NetChannel:General:Model:getContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:getContextId")
	public published  Str getContextId () {
		return String.valueOf(id.Value);
	}

	/*Radix::Net::NetChannel:General:Model:getContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:getContextType")
	public published  Str getContextType () {
		return Arte::EventContextType:NetChannel.Value;
	}

	/*Radix::Net::NetChannel:General:Model:onCommand_startChannel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:onCommand_startChannel")
	public  void onCommand_startChannel (org.radixware.ads.Net.web.NetChannel.StartChannel command) {
		try {
		    Arte::TypesXsd:BoolDocument xOut = command.send();
		    if (xOut.Bool == Boolean.TRUE) {
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Channel started");
		    } else {
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Unable to start the channel");
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Net::NetChannel:General:Model:onCommand_stopChannel-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:onCommand_stopChannel")
	public  void onCommand_stopChannel (org.radixware.ads.Net.web.NetChannel.StopChannel command) {
		try {
		    Arte::TypesXsd:BoolDocument xOut = command.send();
		    if (xOut.Bool == Boolean.TRUE) {
		        Explorer.Dialogs::SimpleDlg.messageInformation(Environment, "Channel stopped");
		    } else {
		        Explorer.Dialogs::SimpleDlg.messageError(Environment, "Unable to stop the channel");
		    }
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::Net::NetChannel:General:Model:keystoreAliasesCmd-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:keystoreAliasesCmd")
	public  void keystoreAliasesCmd (org.radixware.ads.Net.web.NetChannel.KeystoreAliasesCmd command, org.radixware.kernel.common.types.Id propertyId) {
		fillKeystoreMaskLists();

		Explorer.Models.Properties::PropertyArrStr property = (Explorer.Models.Properties::PropertyArrStr)this.properties.get(propertyId);
		final org.radixware.kernel.common.client.views.IArrayEditorDialog dialog = property.getEditorDialog(null);
		dialog.setReadonly(property.isReadonly());
		if (dialog.execDialog()==org.radixware.kernel.common.client.views.IDialog.DialogResult.ACCEPTED){
		    property.setValueObject(dialog.getCurrentValue());
		}
	}

	/*Radix::Net::NetChannel:General:Model:isCommandEnabled-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:isCommandEnabled")
	public published  boolean isCommandEnabled (org.radixware.kernel.common.client.meta.RadCommandDef command) {
		boolean started = curSessionCount.Value != null && curSessionCount.Value.longValue() >= 0;
		if (idof[NetChannel:startChannel].equals(command.Id)) {
		    return unitStarted.Value == true && !started && use.Value == true;
		}
		if (idof[NetChannel:stopChannel].equals(command.Id)) {
		    return unitStarted.Value == true && started;
		}
		return super.isCommandEnabled(command);
	}

	/*Radix::Net::NetChannel:General:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterCreate();
		try {
		    readProperty(idof[NetChannel:warning]);
		} catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		    Environment.getTracer().error("Error on read warning property", ex);
		}
		if (warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}

	/*Radix::Net::NetChannel:General:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::Net::NetChannel:General:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		super.afterUpdate();
		try {
		    readProperty(idof[NetChannel:warning]);
		} catch (Exceptions::InterruptedException | Exceptions::ServiceClientException ex) {
		    Environment.getTracer().error("Error on read warning property", ex);
		}
		if(warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}
	public final class KeystoreAliasesCmd extends org.radixware.ads.Net.web.NetChannel.KeystoreAliasesCmd{
		protected KeystoreAliasesCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_keystoreAliasesCmd( this, propertyId );
		}

	}

	public final class StartChannel extends org.radixware.ads.Net.web.NetChannel.StartChannel{
		protected StartChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_startChannel( this );
		}

	}

	public final class StopChannel extends org.radixware.ads.Net.web.NetChannel.StopChannel{
		protected StopChannel(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_stopChannel( this );
		}

	}

	public final class SelectProtocolHandler extends org.radixware.ads.Net.web.NetChannel.SelectProtocolHandler{
		protected SelectProtocolHandler(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_selectProtocolHandler( this, propertyId );
		}

	}



















}

/* Radix::Net::NetChannel:General:Model - Web Meta*/

/*Radix::Net::NetChannel:General:Model-Entity Model Class*/

package org.radixware.ads.Net.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemFCNRWSZS2HNRDB7BAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::Net::NetChannel:General:Model:Properties-Properties*/
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

/* Radix::Net::NetChannel:General - Desktop Meta*/

/*Radix::Net::NetChannel:General-Selector Presentation*/

package org.radixware.ads.Net.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
		null,
		null,
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		262176,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Net.explorer.NetChannel.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Net::NetChannel:General - Web Meta*/

/*Radix::Net::NetChannel:General-Selector Presentation*/

package org.radixware.ads.Net.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprK5SCPQJQ2HNRDB7BAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aecHBE24OUE2DNRDB7AAALOMT5GDM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),
		null,
		null,
		null,
		null,
		true,
		null,
		null,
		false,
		true,
		null,
		262176,
		null,
		2192,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprFCNRWSZS2HNRDB7BAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5UDIMHIHB5EVJFO47HORGJNT3Q"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7QM5XU6LIJHIPK2G47EWNFLRF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKE3TGQME2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQ2GUXJUE2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colB3IQTAEF2DNRDB7AAALOMT5GDM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPZXGT2TDJFF2RPSP4YQWWIIHQ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdIULJCR2EB5FNXMUQMTJDNMC4SE"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4QBLQUIZAZFQPILQILYXGOSMGM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJRLWTD3PWZGHZPUE6OY5DODG6A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.Net.web.NetChannel.DefaultGroupModel(userSession,this);
	}
}
/* Radix::Net::NetChannel - Localizing Bundle */
package org.radixware.ads.Net.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class NetChannel - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2BCWLS5AOBEAHFAI5R5OP7BWKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2EFJLI7OGNCD5MUZNLNIHYZ5UI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client certificates");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сертификаты клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2JL52KHQRZCZTEMWGIW3VMOB2U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4MGOF4NONZHOJI27UWSS6HANBM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of busy connections");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество занятых соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DWFL3RYVJADBAZBTWPX6TRTFE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Server keys");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключи сервера");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5VAGMA4TKJEJJKT7W6AWFLIUVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5WD6TJI4NBHTTDRP7JYLGLAXUM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Request frame");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат пакета запроса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6RH5R64R7FED5CH25VTXXYFBKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending the request to the unit service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса с сервису модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7LC27QAC4JF75NLC2J5DGNPT2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Database trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7MDSW5BU4RD55E4PD7LRII7IMI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7TFCKOPWVXOBDCLVAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unique constraint violation for objects \'NetChannel\' by key: Reference ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нарушено ограничение уникальности объектов \'Сетевой канал\' по ключу: Ссылочный ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7WYBKHY46NBUVOFSWWXFAB4S6I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль запущен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7XKAUFW2VJEILM4WAOCFVIZEA4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Disconnect notification");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Оповещение о разрыве соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB5K46V22INGV3JOYZ3SGSNXI4I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use TCP keepalive");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать TCP keepalive");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBB45RR7ODNCUDMZBNAJ2ZRHYPQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reference ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ссылочный ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCH2VDVIJO5DZDOZD26C5NZW4LI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал запущен\n");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCH3P5VGJPZGDJPDKYKMHWZ6FZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Keep connection timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время удержания соединения (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCIMKW7A7JVE5FF43YDKV2BSXM4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<permanent connection>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<постоянное соединение>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCVJ4H6CAG5CDRK2WRCDNLP26NQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDFGKOK6XKBDRJCHDHFQIOAZB5Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDVVBB7NDPNFJTEUOBTIH2BJRGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFIJS7GY3HBD6BH46BYMGD7ISBQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYSX3CUMFREJ7CL653VDRHOVJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Адрес");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH3BFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to stop the channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось остановить канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH4QCVAXZFZARPKUMM6Z6OVCHXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channels");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевые каналы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Protocol handler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обработчик протокола");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHHBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error checking the addresses for conflicts: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при проверке адресов на конфликты: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHIOW27ZFIRGBTNZLAZB3KBWJIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Receive timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тайм-аут приема (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHPBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of sessions");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное число сессий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI3BFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Channel stopped");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Канал остановлен\n");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI5T3HS3ZQFE3VBZDJULFMCNQYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Send timeout (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тайм-аут передачи (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIDBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIHBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsILBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC affinity handler class name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя класса для вычисления AADC Affinity");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsINZIHDW3AVBRHEWU4FMU2GYALA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIRJF2U6E7JGRFFM4J7O6SSTTEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Link-level protocol");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Протокол канального уровня");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsITBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"%[R]H - HTTP Request\n%[S]H - HTTP Response\n%[2]L%P - L1 L2 <###> \n%[2R]L%P - L2 L1 <###> \n%S%P%E - STX <###> ETX \n%[6]D%P - L:ASCII[6] <###> \n%[2R *2-3]L\\x00%P - L2 L1 0 <###> \n...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"%[R]H - HTTP Request\n%[S]H - HTTP Response\n%[2]L%P - L1 L2 <###> \n%[2R]L%P - L2 L1 <###> \n%S%P%E - STX <###> ETX \n%[6]D%P - L:ASCII[6] <###> \n%[2R *2-3]L\\x00%P - L2 L1 0 <###> \n...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJBC6JM45I5A47EHOW6EUBGWWUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of open connections");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество открытых соединений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJDBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не определен>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJHBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Select Protocol Handler");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выбрать обработчик событий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJLBFKUK4OXOBDFKUAAMPGXUWTQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsK5T53WQMRXOBDCKMAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Write number of busy connections to database");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Записывать количество занятых соединений в базу данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLAIWKURQQZEXVNBH4BOJAJBSXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Security protocol");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Протокол безопасности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLIJ7LRUUUJFYRLFJ2EDDTDT4UI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enables the keepalive option for the TCP socket. The keepalive settings (timeout, number of retries) are taken from the OS.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Включает опцию keepalive для сетевого порта. Настройки (таймаут, количество повторов) берутся из операционной системы.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMASG324UH5CMPNM63ASKI2F3OU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMFGQZ4Z6URE4DNYFOAUT7HS66E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network settings of this NetChannel have the following conflicts:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевые настройки данного сетевого канала имеют конфликты:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMGQOKEQMXZBDZGJK3T4RRPU34U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error sending the request to the unit service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при отправке запроса с сервису модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOAJJ2HKX4RAWXE5Q5JGX5XFHIQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Synchronous mode");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Синхронный режим");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOG6JAYG5NNEUHNYMKK3CNU6VJA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Connection ready notification");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Оповещение о готовности соединения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP4QZHHMPS5CIVHO6ESKJYF2WSA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop Channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQS7RVYNNIZAJBPOKELX6F4EZYI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"%[R]H - HTTP Request\n%[S]H - HTTP Response\n%[2]L%P - L1 L2 <###> \n%[2R]L%P - L2 L1 <###> \n%S%P%E - STX <###> ETX \n%[6]D%P - L:ASCII[6] <###> \n%[2R *2-3]L\\x00%P - L2 L1 0 <###> \n...");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"%[R]H - HTTP Request\n%[S]H - HTTP Response\n%[2]L%P - L1 L2 <###> \n%[2R]L%P - L2 L1 <###> \n%S%P%E - STX <###> ETX \n%[6]D%P - L:ASCII[6] <###> \n%[2R *2-3]L\\x00%P - L2 L1 0 <###> \n...");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRK5PPCJLJJD3LJ63WX3Y4NAQDI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Client certificate check");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка сертификатов клиента");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRLZ4VBD2PVEORBAOUFIYFKMFKA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsS5HIWGO3CBCKJF2DLK4EKV3AHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Response frame");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Формат пакета ответа");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTTHJ2QWFTFFCLOMM3VPJKYFUIM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUUQEPX72BNDF5HH2QA7PAEAJCI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"GUI trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки на экран");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYGDOE7NJDBEGXNGGJFLH5Y6JMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYHFJSF7EPRGXFAM55A75C3IGQQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYN5EJB2VMJAZ3KBALBGUS3VCCA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unable to start the channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Не удалось запустить канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYYWZK4LLG5DCDM3CUO5ONBB6XI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Class");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Класс");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZR77AUTFPJEOLJW7SFEZS4LGUE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(NetChannel - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaecHBE24OUE2DNRDB7AAALOMT5GDM"),"NetChannel - Localizing Bundle",$$$items$$$);
}
