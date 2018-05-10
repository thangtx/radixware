
/* Radix::System::Instance - Server Executable*/

/*Radix::System::Instance-Entity Class*/

package org.radixware.ads.System.server;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;


@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance")
public final published class Instance  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Instance_mi.rdxMeta;}

	/*Radix::System::Instance:Nested classes-Nested Classes*/

	/*Radix::System::Instance:Properties-Properties*/

	/*Radix::System::Instance:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::System::Instance:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::System::Instance:startOSCommand-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startOSCommand")
	public published  Str getStartOSCommand() {
		return startOSCommand;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startOSCommand")
	public published   void setStartOSCommand(Str val) {
		startOSCommand = val;
	}

	/*Radix::System::Instance:stopOSCommand-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopOSCommand")
	public published  Str getStopOSCommand() {
		return stopOSCommand;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopOSCommand")
	public published   void setStopOSCommand(Str val) {
		stopOSCommand = val;
	}

	/*Radix::System::Instance:deletedSapId-Dynamic Property*/



	protected Int deletedSapId=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:deletedSapId")
	private final  Int getDeletedSapId() {
		return deletedSapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:deletedSapId")
	private final   void setDeletedSapId(Int val) {
		deletedSapId = val;
	}

	/*Radix::System::Instance:systemId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:systemId")
	public published  Int getSystemId() {
		return systemId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:systemId")
	public published   void setSystemId(Int val) {
		systemId = val;
	}

	/*Radix::System::Instance:keyAliases-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr keyAliases=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyAliases")
	public published  org.radixware.kernel.common.types.ArrStr getKeyAliases() {

		if (internal[keyAliases]==null){
		    try{
		        internal[keyAliases] = new ArrStr(Pki::KeystoreController.getServerKeystoreKeyAliases());
		    } catch (Exceptions::InvalidServerKeystoreSettingsException e){
		        Arte::Trace.debug("Error reading the key aliases from the keystore:"+e.toString(), Arte::EventSource:App);
		    } catch (Exceptions::Throwable e){
		        Arte::Trace.error("Error reading the key aliases from the keystore:"+e.toString(), Arte::EventSource:App);
		    }
		}

		return internal[keyAliases];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyAliases")
	public published   void setKeyAliases(org.radixware.kernel.common.types.ArrStr val) {
		keyAliases = val;
	}

	/*Radix::System::Instance:trustedCertificateAliases-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr trustedCertificateAliases=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:trustedCertificateAliases")
	public published  org.radixware.kernel.common.types.ArrStr getTrustedCertificateAliases() {

		if (internal[trustedCertificateAliases]==null){
		    try{
		        internal[trustedCertificateAliases] = new ArrStr(Pki::KeystoreController.getServerKeystoreTrustedCertAliases());
		    } catch (Exceptions::InvalidServerKeystoreSettingsException e){
		        Arte::Trace.debug("Error reading the trusted certificate aliases from the keystore:"+e.toString(), Arte::EventSource:App);
		    } catch (Exceptions::Throwable e){
		        Arte::Trace.error("Error reading the trusted certificate aliases from the keystore:"+e.toString(), Arte::EventSource:App);
		    }
		}

		return internal[trustedCertificateAliases];
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:trustedCertificateAliases")
	public published   void setTrustedCertificateAliases(org.radixware.kernel.common.types.ArrStr val) {
		trustedCertificateAliases = val;
	}

	/*Radix::System::Instance:sapPropsXml-Dynamic Property*/



	protected Str sapPropsXml=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapPropsXml")
	public published  Str getSapPropsXml() {
		return sapPropsXml;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapPropsXml")
	public published   void setSapPropsXml(Str val) {
		sapPropsXml = val;
	}

	/*Radix::System::Instance:warning-Dynamic Property*/



	protected Str warning=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:warning")
	protected  Str getWarning() {

		if (/*org.radixware.kernel.server.SrvRunParams.getIsDevelopmentMode() //!!!_!!!
		        || */org.radixware.kernel.common.utils.SystemPropUtils.getBooleanSystemProp("rdx.disable.auto.ports.check", false)) {
		    return null; 
		}
		try {
		    Collection<AddressConflict> conflicts;
		    if (sapId == 0 || sapId == null) {
		        return null;
		    }
		    final Sap sap = Sap.loadByPK(sapId, false);
		    if (!(sap.address == null || sap.address.isEmpty())) {
		        conflicts = AddressConflict.discoverInstanceConflicts(this);
		    } else {//creation time
		        conflicts = Collections.emptyList();
		    }

		    conflicts = new ArrayList<AddressConflict>(conflicts);
		    InstanceUnitsCursor units = InstanceUnitsCursor.open(id);
		    while (units.next()) {
		        //    System.out.println("Check for unit conflicts: " + units.);
		        Collection<AddressConflict> unitConflicts = AddressConflict.discoverUnitConflicts(units.id);
		        conflicts.addAll(unitConflicts);
		    }

		    if (conflicts.isEmpty()) {
		        warning = null;
		        //    System.out.println("No warnings");
		        return null;
		    }

		    StringBuilder description = new StringBuilder();
		    description.append("Network settings of this instance have the following conflicts:");
		    int idx = 0;
		    for (AddressConflict conflict : conflicts) {
		        description.append("\n");
		        description.append(++idx);
		        description.append(") ");
		        description.append(conflict.thisAddress.getAsStr());
		        description.append(" <-> ");
		        description.append(conflict.conflictedAddress.getAsStr());
		    }

		    return description.toString();
		} catch (Exceptions::Throwable t) {
		    return "Address conflicts check error: " + Utils::ExceptionTextFormatter.exceptionStackToString(t);
		}

	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:warning")
	protected   void setWarning(Str val) {
		warning = val;
	}

	/*Radix::System::Instance:system-Dynamic Property*/



	protected org.radixware.ads.System.server.System system=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:system")
	public published  org.radixware.ads.System.server.System getSystem() {

		if (internal[system] == null) {
		    internal[system] = System.loadByPK(1, false);
		}
		return internal[system];
	}

	/*Radix::System::Instance:arteCntAboveNormal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntAboveNormal")
	public  Int getArteCntAboveNormal() {
		return arteCntAboveNormal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntAboveNormal")
	public   void setArteCntAboveNormal(Int val) {
		arteCntAboveNormal = val;
	}

	/*Radix::System::Instance:arteCntCritical-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntCritical")
	public  Int getArteCntCritical() {
		return arteCntCritical;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntCritical")
	public   void setArteCntCritical(Int val) {
		arteCntCritical = val;
	}

	/*Radix::System::Instance:arteCntHigh-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntHigh")
	public  Int getArteCntHigh() {
		return arteCntHigh;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntHigh")
	public   void setArteCntHigh(Int val) {
		arteCntHigh = val;
	}

	/*Radix::System::Instance:arteCntVeryHigh-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntVeryHigh")
	public  Int getArteCntVeryHigh() {
		return arteCntVeryHigh;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntVeryHigh")
	public   void setArteCntVeryHigh(Int val) {
		arteCntVeryHigh = val;
	}

	/*Radix::System::Instance:arteInstCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstCount")
	public published  Int getArteInstCount() {
		return arteInstCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstCount")
	public published   void setArteInstCount(Int val) {
		arteInstCount = val;
	}

	/*Radix::System::Instance:arteInstLiveTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstLiveTime")
	public published  Int getArteInstLiveTime() {
		return arteInstLiveTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstLiveTime")
	public published   void setArteInstLiveTime(Int val) {
		arteInstLiveTime = val;
	}

	/*Radix::System::Instance:useActiveArteLimits-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useActiveArteLimits")
	public published  Bool getUseActiveArteLimits() {
		return useActiveArteLimits;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useActiveArteLimits")
	public published   void setUseActiveArteLimits(Bool val) {
		useActiveArteLimits = val;
	}

	/*Radix::System::Instance:lowArteInstCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:lowArteInstCount")
	public published  Int getLowArteInstCount() {
		return lowArteInstCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:lowArteInstCount")
	public published   void setLowArteInstCount(Int val) {
		lowArteInstCount = val;
	}

	/*Radix::System::Instance:highArteInstCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:highArteInstCount")
	public published  Int getHighArteInstCount() {
		return highArteInstCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:highArteInstCount")
	public published   void setHighArteInstCount(Int val) {
		highArteInstCount = val;
	}

	/*Radix::System::Instance:started-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:started")
	public published  Bool getStarted() {
		return started;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:started")
	public published   void setStarted(Bool val) {
		started = val;
	}

	/*Radix::System::Instance:selfCheckTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTime")
	public published  java.sql.Timestamp getSelfCheckTime() {
		return selfCheckTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTime")
	public published   void setSelfCheckTime(java.sql.Timestamp val) {
		selfCheckTime = val;
	}

	/*Radix::System::Instance:maxTraceFileCnt-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileCnt")
	public published  Int getMaxTraceFileCnt() {
		return maxTraceFileCnt;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileCnt")
	public published   void setMaxTraceFileCnt(Int val) {
		maxTraceFileCnt = val;
	}

	/*Radix::System::Instance:maxTraceFileSizeKb-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileSizeKb")
	public published  Int getMaxTraceFileSizeKb() {
		return maxTraceFileSizeKb;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileSizeKb")
	public published   void setMaxTraceFileSizeKb(Int val) {
		maxTraceFileSizeKb = val;
	}

	/*Radix::System::Instance:rotateTraceFilesDaily-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:rotateTraceFilesDaily")
	public published  Bool getRotateTraceFilesDaily() {
		return rotateTraceFilesDaily;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:rotateTraceFilesDaily")
	public published   void setRotateTraceFilesDaily(Bool val) {
		rotateTraceFilesDaily = val;
	}

	/*Radix::System::Instance:traceFilesDir-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:traceFilesDir")
	public published  Str getTraceFilesDir() {
		return traceFilesDir;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:traceFilesDir")
	public published   void setTraceFilesDir(Str val) {
		traceFilesDir = val;
	}

	/*Radix::System::Instance:guiTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:guiTraceProfile")
	public published  Str getGuiTraceProfile() {
		return guiTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:guiTraceProfile")
	public published   void setGuiTraceProfile(Str val) {
		guiTraceProfile = val;
	}

	/*Radix::System::Instance:fileTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fileTraceProfile")
	public published  Str getFileTraceProfile() {
		return fileTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fileTraceProfile")
	public published   void setFileTraceProfile(Str val) {
		fileTraceProfile = val;
	}

	/*Radix::System::Instance:dbTraceProfile-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbTraceProfile")
	public published  Str getDbTraceProfile() {
		return dbTraceProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbTraceProfile")
	public published   void setDbTraceProfile(Str val) {
		dbTraceProfile = val;
	}

	/*Radix::System::Instance:autoActualizeVer-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoActualizeVer")
	public published  Bool getAutoActualizeVer() {
		return autoActualizeVer;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoActualizeVer")
	public published   void setAutoActualizeVer(Bool val) {
		autoActualizeVer = val;
	}

	/*Radix::System::Instance:httpProxy-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpProxy")
	public published  Str getHttpProxy() {
		return httpProxy;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpProxy")
	public published   void setHttpProxy(Str val) {
		httpProxy = val;
	}

	/*Radix::System::Instance:httpsProxy-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpsProxy")
	public published  Str getHttpsProxy() {
		return httpsProxy;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpsProxy")
	public published   void setHttpsProxy(Str val) {
		httpsProxy = val;
	}

	/*Radix::System::Instance:jdwpAddress-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:jdwpAddress")
	public published  Str getJdwpAddress() {
		return jdwpAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:jdwpAddress")
	public published   void setJdwpAddress(Str val) {
		jdwpAddress = val;
	}

	/*Radix::System::Instance:keyStorePath-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStorePath")
	protected published  Str getKeyStorePath() {
		return keyStorePath;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStorePath")
	protected published   void setKeyStorePath(Str val) {
		keyStorePath = val;
	}

	/*Radix::System::Instance:keyStoreType-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStoreType")
	protected published  org.radixware.kernel.common.enums.EKeyStoreType getKeyStoreType() {
		return keyStoreType;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStoreType")
	protected published   void setKeyStoreType(org.radixware.kernel.common.enums.EKeyStoreType val) {
		keyStoreType = val;
	}

	/*Radix::System::Instance:keyTabFilePath-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyTabFilePath")
	public published  Str getKeyTabFilePath() {
		return keyTabFilePath;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyTabFilePath")
	public published   void setKeyTabFilePath(Str val) {
		keyTabFilePath = val;
	}

	/*Radix::System::Instance:oraImplStmtCacheSize-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:oraImplStmtCacheSize")
	public published  Int getOraImplStmtCacheSize() {
		return oraImplStmtCacheSize;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:oraImplStmtCacheSize")
	public published   void setOraImplStmtCacheSize(Int val) {
		oraImplStmtCacheSize = val;
	}

	/*Radix::System::Instance:useOraImplStmtCache-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useOraImplStmtCache")
	public published  Bool getUseOraImplStmtCache() {
		return useOraImplStmtCache;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useOraImplStmtCache")
	public published   void setUseOraImplStmtCache(Bool val) {
		useOraImplStmtCache = val;
	}

	/*Radix::System::Instance:sapId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapId")
	public published  Int getSapId() {
		return sapId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapId")
	public published   void setSapId(Int val) {
		sapId = val;
	}

	/*Radix::System::Instance:scp-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scp")
	public published  org.radixware.ads.System.server.Scp getScp() {
		return scp;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scp")
	public published   void setScp(org.radixware.ads.System.server.Scp val) {
		scp = val;
	}

	/*Radix::System::Instance:scpName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scpName")
	public published  Str getScpName() {
		return scpName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scpName")
	public published   void setScpName(Str val) {
		scpName = val;
	}

	/*Radix::System::Instance:avgActiveArteCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:avgActiveArteCount")
	public published  Num getAvgActiveArteCount() {
		return avgActiveArteCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:avgActiveArteCount")
	public published   void setAvgActiveArteCount(Num val) {
		avgActiveArteCount = val;
	}

	/*Radix::System::Instance:memoryCheckPeriodSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:memoryCheckPeriodSec")
	public  Int getMemoryCheckPeriodSec() {
		return memoryCheckPeriodSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:memoryCheckPeriodSec")
	public   void setMemoryCheckPeriodSec(Int val) {
		memoryCheckPeriodSec = val;
	}

	/*Radix::System::Instance:targetExecutor-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutor")
	public published  org.radixware.ads.System.server.Unit getTargetExecutor() {
		return targetExecutor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutor")
	public published   void setTargetExecutor(org.radixware.ads.System.server.Unit val) {
		targetExecutor = val;
	}

	/*Radix::System::Instance:targetExecutorId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutorId")
	public  Int getTargetExecutorId() {
		return targetExecutorId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutorId")
	public   void setTargetExecutorId(Int val) {
		targetExecutorId = val;
	}

	/*Radix::System::Instance:maxActiveArteNormal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteNormal")
	public published  Int getMaxActiveArteNormal() {
		return maxActiveArteNormal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteNormal")
	public published   void setMaxActiveArteNormal(Int val) {
		maxActiveArteNormal = val;
	}

	/*Radix::System::Instance:maxActiveArteAboveNormal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteAboveNormal")
	public published  Int getMaxActiveArteAboveNormal() {
		return maxActiveArteAboveNormal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteAboveNormal")
	public published   void setMaxActiveArteAboveNormal(Int val) {
		maxActiveArteAboveNormal = val;
	}

	/*Radix::System::Instance:maxActiveArteHigh-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteHigh")
	public published  Int getMaxActiveArteHigh() {
		return maxActiveArteHigh;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteHigh")
	public published   void setMaxActiveArteHigh(Int val) {
		maxActiveArteHigh = val;
	}

	/*Radix::System::Instance:maxActiveArteVeryHigh-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteVeryHigh")
	public published  Int getMaxActiveArteVeryHigh() {
		return maxActiveArteVeryHigh;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteVeryHigh")
	public published   void setMaxActiveArteVeryHigh(Int val) {
		maxActiveArteVeryHigh = val;
	}

	/*Radix::System::Instance:maxActiveArteCritical-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteCritical")
	public published  Int getMaxActiveArteCritical() {
		return maxActiveArteCritical;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteCritical")
	public published   void setMaxActiveArteCritical(Int val) {
		maxActiveArteCritical = val;
	}

	/*Radix::System::Instance:sensTraceFinishTime-Dynamic Property*/



	protected Str sensTraceFinishTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sensTraceFinishTime")
	public  Str getSensTraceFinishTime() {

		if (activityStatus == ActivityStatus:RUNNING) {
		    String errorText = null;
		    long finishMillis = 0;
		    if (Arte::Arte.getInstance().Instance.getId() == id) {
		        finishMillis = Arte::Arte.getInstance().Instance.getSensitiveTracingFinishMillis();
		    } else {
		        try {
		            final java.util.Calendar endTime = getStatus().SensitiveDataTracingFinishTime;
		            if (endTime != null) {
		                finishMillis = endTime.TimeInMillis;
		            }
		        } catch (Exception ex) {
		            errorText = Utils::ExceptionTextFormatter.exceptionStackToString(ex);
		        }
		    }
		    if (errorText != null) {
		        return "Error while discovering: " + errorText;
		    }
		    final long curTimeMillis = System.currentTimeMillis();
		    if (finishMillis > curTimeMillis) {
		        return new DateTime(finishMillis).toString() + " (" + "time left:" + " " + Utils::Timing.millisToDurationString(finishMillis - curTimeMillis) + ")";
		    }
		} else if (activityStatus == ActivityStatus:INACTIVE) {
		    return "<unable to determine because instance is inactive>";
		}

		return "<tracing disabled>";

	}

	/*Radix::System::Instance:classLoadingProfileId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfileId")
	public published  Int getClassLoadingProfileId() {
		return classLoadingProfileId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfileId")
	public published   void setClassLoadingProfileId(Int val) {
		classLoadingProfileId = val;
	}

	/*Radix::System::Instance:classLoadingProfile-Parent Reference*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfile")
	public  org.radixware.ads.System.server.ClassLoadingProfile getClassLoadingProfile() {
		return classLoadingProfile;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfile")
	public   void setClassLoadingProfile(org.radixware.ads.System.server.ClassLoadingProfile val) {
		classLoadingProfile = val;
	}

	/*Radix::System::Instance:sysdate-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sysdate")
	public  java.sql.Timestamp getSysdate() {
		return sysdate;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sysdate")
	public   void setSysdate(java.sql.Timestamp val) {
		sysdate = val;
	}

	/*Radix::System::Instance:selfCheckTimeStr-Dynamic Property*/



	protected Str selfCheckTimeStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeStr")
	public published  Str getSelfCheckTimeStr() {

		return SystemServerUtils.calcSelfCheckTimeStr(effectiveSelfCheckTime, sysdate);
	}

	/*Radix::System::Instance:activityStatus-Dynamic Property*/



	protected org.radixware.ads.System.common.ActivityStatus activityStatus=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:activityStatus")
	public published  org.radixware.ads.System.common.ActivityStatus getActivityStatus() {

		if (Boolean.FALSE.equals(this.started)) {
		    return ActivityStatus:STOPPED;
		}
		else if (Boolean.TRUE.equals(this.started) && (this.effectiveSelfCheckTime != null && sysdate.Time < this.effectiveSelfCheckTime.Time + org.radixware.kernel.server.instance.Instance.DB_I_AM_ALIVE_TIMEOUT_MILLIS)) {
		    return ActivityStatus:RUNNING;
		}
		else {
		    return ActivityStatus:INACTIVE;
		}

	}

	/*Radix::System::Instance:aadcDgAddress-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcDgAddress")
	public published  Str getAadcDgAddress() {
		return aadcDgAddress;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcDgAddress")
	public published   void setAadcDgAddress(Str val) {
		aadcDgAddress = val;
	}

	/*Radix::System::Instance:aadcMemberId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMemberId")
	public published  Int getAadcMemberId() {
		return aadcMemberId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMemberId")
	public published   void setAadcMemberId(Int val) {
		aadcMemberId = val;
	}

	/*Radix::System::Instance:aadcMyScn-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMyScn")
	public  Int getAadcMyScn() {
		return aadcMyScn;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMyScn")
	public   void setAadcMyScn(Int val) {
		aadcMyScn = val;
	}

	/*Radix::System::Instance:aadcMyTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMyTime")
	public  java.sql.Timestamp getAadcMyTime() {
		return aadcMyTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMyTime")
	public   void setAadcMyTime(java.sql.Timestamp val) {
		aadcMyTime = val;
	}

	/*Radix::System::Instance:isAadcSysMember-Dynamic Property*/



	protected Bool isAadcSysMember=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:isAadcSysMember")
	private final  Bool getIsAadcSysMember() {

		return System.thisSystem.aadcMemberId != null;

	}

	/*Radix::System::Instance:autoRestartDelaySec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoRestartDelaySec")
	public published  Int getAutoRestartDelaySec() {
		return autoRestartDelaySec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoRestartDelaySec")
	public published   void setAutoRestartDelaySec(Int val) {
		autoRestartDelaySec = val;
	}

	/*Radix::System::Instance:selfCheckTimeMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeMillis")
	public published  Int getSelfCheckTimeMillis() {
		return selfCheckTimeMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeMillis")
	public published   void setSelfCheckTimeMillis(Int val) {
		selfCheckTimeMillis = val;
	}

	/*Radix::System::Instance:osPid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:osPid")
	public published  Int getOsPid() {
		return osPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:osPid")
	public published   void setOsPid(Int val) {
		osPid = val;
	}

	/*Radix::System::Instance:appVersion-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:appVersion")
	public published  Str getAppVersion() {
		return appVersion;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:appVersion")
	public published   void setAppVersion(Str val) {
		appVersion = val;
	}

	/*Radix::System::Instance:kernelVersion-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:kernelVersion")
	public published  Str getKernelVersion() {
		return kernelVersion;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:kernelVersion")
	public published   void setKernelVersion(Str val) {
		kernelVersion = val;
	}

	/*Radix::System::Instance:startTimeMillis-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startTimeMillis")
	public published  Int getStartTimeMillis() {
		return startTimeMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startTimeMillis")
	public published   void setStartTimeMillis(Int val) {
		startTimeMillis = val;
	}

	/*Radix::System::Instance:uptimeStr-Dynamic Property*/



	protected Str uptimeStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:uptimeStr")
	public published  Str getUptimeStr() {

		if (activityStatus == ActivityStatus:RUNNING && startTimeMillis != null) {
		    return Utils::Timing.millisToDurationString(dbCurMillis.longValue() - startTimeMillis.longValue());
		}
		return "<unknown>";
	}

	/*Radix::System::Instance:dbCurMillis-Expression Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbCurMillis")
	public  Int getDbCurMillis() {
		return dbCurMillis;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbCurMillis")
	public   void setDbCurMillis(Int val) {
		dbCurMillis = val;
	}

	/*Radix::System::Instance:revision-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:revision")
	public published  Int getRevision() {
		return revision;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:revision")
	public published   void setRevision(Int val) {
		revision = val;
	}

	/*Radix::System::Instance:canEditActualizeVer-Dynamic Property*/



	protected Bool canEditActualizeVer=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:canEditActualizeVer")
	public  Bool getCanEditActualizeVer() {

		return org.radixware.kernel.server.SrvRunParams.getIsDevelopmentMode();
	}

	/*Radix::System::Instance:effectiveSelfCheckTime-Dynamic Property*/



	protected java.sql.Timestamp effectiveSelfCheckTime=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:effectiveSelfCheckTime")
	public published  java.sql.Timestamp getEffectiveSelfCheckTime() {

		DateTime result = null;
		if (selfCheckTimeMillis != null) {
		    result = new DateTime(selfCheckTimeMillis.longValue());
		}
		if (selfCheckTime != null && (result == null || selfCheckTime.Time > result.Time)) {
		    result = selfCheckTime;
		}
		DateTime dgTime = Arte::Arte.getInstance().getInstance().getAadcManager().getInstanceSelfCheckTime(id.longValue());

		if (dgTime != null && (result == null || dgTime.Time > result.Time)) {
		    result = dgTime;
		}

		return result;
	}

	/*Radix::System::Instance:instStateForcedGatherPeriodSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateForcedGatherPeriodSec")
	public published  Int getInstStateForcedGatherPeriodSec() {
		return instStateForcedGatherPeriodSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateForcedGatherPeriodSec")
	public published   void setInstStateForcedGatherPeriodSec(Int val) {
		instStateForcedGatherPeriodSec = val;
	}

	/*Radix::System::Instance:instStateGatherPeriodSec-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateGatherPeriodSec")
	public published  Int getInstStateGatherPeriodSec() {
		return instStateGatherPeriodSec;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateGatherPeriodSec")
	public published   void setInstStateGatherPeriodSec(Int val) {
		instStateGatherPeriodSec = val;
	}

	/*Radix::System::Instance:instStateHistoryStorePeriodDays-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateHistoryStorePeriodDays")
	public published  Int getInstStateHistoryStorePeriodDays() {
		return instStateHistoryStorePeriodDays;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateHistoryStorePeriodDays")
	public published   void setInstStateHistoryStorePeriodDays(Int val) {
		instStateHistoryStorePeriodDays = val;
	}

	/*Radix::System::Instance:cpuCoreCount-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:cpuCoreCount")
	public published  Int getCpuCoreCount() {
		return cpuCoreCount;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:cpuCoreCount")
	public published   void setCpuCoreCount(Int val) {
		cpuCoreCount = val;
	}

	/*Radix::System::Instance:hostIpAddressesStr-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddressesStr")
	public  Str getHostIpAddressesStr() {
		return hostIpAddressesStr;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddressesStr")
	public   void setHostIpAddressesStr(Str val) {
		hostIpAddressesStr = val;
	}

	/*Radix::System::Instance:hostName-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostName")
	public published  Str getHostName() {
		return hostName;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostName")
	public published   void setHostName(Str val) {
		hostName = val;
	}

	/*Radix::System::Instance:hostIpAddresses-Dynamic Property*/



	protected org.radixware.kernel.common.types.ArrStr hostIpAddresses=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddresses")
	public published  org.radixware.kernel.common.types.ArrStr getHostIpAddresses() {

		final Str[] arr = hostIpAddressesStr == null || hostIpAddressesStr.isEmpty()
		        ? new Str[0]
		        : hostIpAddressesStr.split(",");
		final ArrStr result = new ArrStr(arr);        
		return result;

	}



















































































































































































































































































































































































































































































	/*Radix::System::Instance:Methods-Methods*/

	/*Radix::System::Instance:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		Int srcSapId = null;
		if (src != null) {
		    srcSapId = ((Instance) src).sapId;
		    sapId = 0;
		    sapPropsXml = ServerSapUtils.writePropsForCopy(srcSapId, sapPropsXml);
		}
		updateService();
		if (srcSapId != null) {
		    ServerSapUtils.copyScpLinks(srcSapId, sapId);
		}
		return true;
	}

	/*Radix::System::Instance:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:beforeDelete")
	protected published  boolean beforeDelete () {
		deletedSapId = sapId;
		return true;
	}

	/*Radix::System::Instance:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:beforeUpdate")
	protected published  boolean beforeUpdate () {
		updateService();
		return true;
	}

	/*Radix::System::Instance:onCommand_StopAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_StopAllUnits")
	private final  org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs onCommand_StopAllUnits (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return stopAllUnits();           
	}

	/*Radix::System::Instance:onCommand_StartAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_StartAllUnits")
	private final  org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs onCommand_StartAllUnits (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return startAllUnits();  
	}

	/*Radix::System::Instance:onCommand_RestartAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_RestartAllUnits")
	private final  org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs onCommand_RestartAllUnits (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return restartAllUnits();                                         
	}

	/*Radix::System::Instance:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:loadByPK")
	public static published  org.radixware.ads.System.server.Instance loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),pkValsMap);
		try{
		return (
		org.radixware.ads.System.server.Instance) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::System::Instance:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:afterCreate")
	protected published  void afterCreate (org.radixware.kernel.server.types.Entity src) {
		if (src != null) {
		    Instance source = (Instance) src;
		    try (final InstanceUnitsCursor units = InstanceUnitsCursor.open(source.id)) {
		        while (units.next()) {
		            Unit srcUnit = Unit.loadByPK(units.id, true);
		            Unit unit = (Unit) Arte::Arte.newObject(Types::Id.Factory.loadFrom(srcUnit.classGuid));
		            unit.init(null, srcUnit);
		            unit.instanceId = id;
		            unit.create(srcUnit);
		        }
		    }
		}
	}

	/*Radix::System::Instance:onCommand_ActualizeVer-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_ActualizeVer")
	public  void onCommand_ActualizeVer (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		org.radixware.schemas.systeminstancecontrolWsdl.ActualizeVerDocument doc =
		        org.radixware.schemas.systeminstancecontrolWsdl.ActualizeVerDocument.Factory.newInstance();
		doc.addNewActualizeVer().addNewActualizeVerRq();
		invoke(doc, null, 10);
	}

	/*Radix::System::Instance:onCommand_ShowUsedAddresses-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_ShowUsedAddresses")
	private final  org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument onCommand_ShowUsedAddresses (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		Acs::CommandsXsd:StrValueDocument str = Acs::CommandsXsd:StrValueDocument.Factory.newInstance();
		str.StrValue = AddressConflict.getInstancePortTable(id);
		return str;
	}

	/*Radix::System::Instance:getUsedAddresses-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:getUsedAddresses")
	  java.util.Collection<org.radixware.ads.System.server.AddressInfo> getUsedAddresses () {
		if (isInDatabase(false)) {
		    Collection<AddressInfo> res = new ArrayList<>();
		    if (sapId != null)
		        res.add(new AddressInfo(id, null, sapId));
		    if (aadcDgAddress != null)
		        res.add(new AddressInfo(aadcDgAddress, SapChannelType:TCP));
		    return res;
		} else
		    return Collections.emptyList();
	}

	/*Radix::System::Instance:updateService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:updateService")
	private final  void updateService () {
		final Sap sap;

		final boolean isSapExists = sapId != null && sapId != 0;

		if (!isSapExists) {
		    Service service = new Service();
		    service.init();
		    service.systemId = 1;
		    service.accessibility = null;
		    service.implementedInArte = false;
		    service.uri = "http://schemas.radixware.org/systeminstancecontrol.wsdl#" + id;
		    service.wsdlUri = "http://schemas.radixware.org/systeminstancecontrol.wsdl";
		    service.title = "System Instance #" + id + " Control Service";

		    service.create();

		    sap = new Sap();
		    sap.init();
		    ServerSapUtils.fillProps(sap, sapPropsXml);
		    sap.service = service;
		    sap.title = "SAP of the instance #" + id;
		    sap.accessibility = ServiceAccessibility:INTRA_SYSTEM;
		} else {
		    sap = Sap.loadByPK(sapId, true);
		    ServerSapUtils.fillProps(sap, sapPropsXml);
		}

		if (!isSapExists) {
		    sap.systemInstanceId = id;
		    sap.create();
		    sapId = sap.id;
		} else {
		    sap.update();
		}
	}

	/*Radix::System::Instance:afterDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:afterDelete")
	protected published  void afterDelete () {
		super.afterDelete();
		deleteService();
	}

	/*Radix::System::Instance:deleteService-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:deleteService")
	private final  void deleteService () {
		Sap sap = Sap.loadByPK(deletedSapId, false);
		Service service = sap.service;
		sap.delete();
		service.delete();
	}

	/*Radix::System::Instance:onCommand_reloadArtePool-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_reloadArtePool")
	private final  org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs onCommand_reloadArtePool (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return reloadArtePool();
	}

	/*Radix::System::Instance:onCommand_performMaintenance-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_performMaintenance")
	private final  org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs onCommand_performMaintenance (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		return performMaintenance();
	}

	/*Radix::System::Instance:beforeInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:beforeInit")
	protected published  boolean beforeInit (org.radixware.kernel.server.types.PropValHandlersByIdMap initPropValsById, org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		if (src != null) {
		    started = false;
		    selfCheckTime = null;
		    avgActiveArteCount = 0;
		    arteInstCount = 0;
		    targetExecutorId = null;
		    aadcMyScn = null;
		    aadcMyTime = null;
		    appVersion = null;
		    kernelVersion = null;
		    revision = null;
		    osPid = null;
		}
		return super.beforeInit(initPropValsById, src, phase);
	}

	/*Radix::System::Instance:onCommand_fillArteTable-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:onCommand_fillArteTable")
	private final  org.radixware.schemas.systeminstancecontrol.FillArteTableRs onCommand_fillArteTable (org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValHandlersById) {
		org.radixware.kernel.server.instance.Instance thisInst = Arte::Arte.getInstance().Instance;
		if (thisInst.Id == id) {
		    return fillArteTable();
		} else {
		    InstanceControlServiceWsdl:FillArteTableDocument doc = InstanceControlServiceWsdl:FillArteTableDocument.Factory.newInstance();
		    InstanceControlServiceXsd:FillArteTableRq rq = doc.addNewFillArteTable().addNewFillArteTableRq();
		    rq.User = Arte::Arte.getUserName();
		    doc.set(invoke(doc, InstanceControlServiceWsdl:FillArteTableDocument.class, 30));
		    return doc.FillArteTable.FillArteTableRs;
		}
	}

	/*Radix::System::Instance:applyOptions-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:applyOptions")
	public published  org.radixware.schemas.systeminstancecontrol.ApplyConfigFileOptionsRs applyOptions (org.radixware.kernel.common.types.ArrStr options) throws org.radixware.kernel.common.exceptions.ServiceCallFault {
		InstanceControlServiceWsdl:ApplyConfigFileOptionsDocument doc = InstanceControlServiceWsdl:ApplyConfigFileOptionsDocument.Factory.newInstance();
		List<Str> optionList = new ArrayList<Str>();
		for (Str option : options) {
		    optionList.add(option);
		}
		doc.addNewApplyConfigFileOptions().addNewApplyConfigFileOptionsRq().Options = optionList;
		return ((InstanceControlServiceWsdl:ApplyConfigFileOptionsDocument) invoke(doc, InstanceControlServiceWsdl:ApplyConfigFileOptionsDocument.class, 30)).ApplyConfigFileOptions.ApplyConfigFileOptionsRs;

	}

	/*Radix::System::Instance:fillArteTable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fillArteTable")
	private final  org.radixware.schemas.systeminstancecontrol.FillArteTableRs fillArteTable () {
		InstanceControlServiceWsdl:FillArteTableDocument doc = InstanceControlServiceWsdl:FillArteTableDocument.Factory.newInstance();
		InstanceControlServiceXsd:FillArteTableRs rs = doc.addNewFillArteTable().addNewFillArteTableRs();

		org.radixware.kernel.server.instance.ArteStateWriter.UpdateRequestHandle handle =
		        Arte::Arte.getInstance().Instance.getArteStateWriter().requestUpdate();
		try {
		    if (handle.await(2000)) {
		        rs.Result = org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE;
		    } else {
		        rs.Result = org.radixware.schemas.systeminstancecontrol.ActionStateEnum.FAILED;
		        rs.ResultComment = "Error during processing request.";
		    }
		} catch (Exceptions::InterruptedException ex) {
		    rs.Result = org.radixware.schemas.systeminstancecontrol.ActionStateEnum.FAILED;
		    rs.ResultComment = "Thread was interrupted during processing request.";
		}

		return rs;
	}

	/*Radix::System::Instance:getArteClassLoadingProfile-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:getArteClassLoadingProfile")
	public published  org.radixware.schemas.systeminstancecontrol.GetArteClassLoadingProfileRs getArteClassLoadingProfile () throws org.radixware.kernel.common.exceptions.ServiceCallFault {
		InstanceControlServiceWsdl:GetArteClassLoadingProfileDocument doc = InstanceControlServiceWsdl:GetArteClassLoadingProfileDocument.Factory.newInstance();
		try {
		    doc.addNewGetArteClassLoadingProfile().addNewGetArteClassLoadingProfileRq();
		    doc.set(Arte::Arte.invokeInternalService(doc, InstanceControlServiceWsdl:GetArteClassLoadingProfileDocument.class,
		            ServiceUri:InstanceControlWsdl.getValue() + "#" + id.toString(),
		            0, 300, null));
		} catch (Exceptions::ServiceCallException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::ServiceCallTimeout e) {
		    throw new AppError(e.getMessage());
		}
		return doc.GetArteClassLoadingProfile.GetArteClassLoadingProfileRs;

	}

	/*Radix::System::Instance:getStatus-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:getStatus")
	public published  org.radixware.schemas.systeminstancecontrol.GetStatusRs getStatus () throws org.radixware.kernel.common.exceptions.ServiceCallFault {
		InstanceControlServiceWsdl:GetStatusDocument doc = InstanceControlServiceWsdl:GetStatusDocument.Factory.newInstance();
		try {
		    doc.addNewGetStatus().addNewGetStatusRq();
		    doc.set(Arte::Arte.invokeInternalService(doc, InstanceControlServiceWsdl:GetStatusDocument.class,
		            ServiceUri:InstanceControlWsdl.getValue() + "#" + id.toString(),
		            0, 120, null));
		} catch (Exceptions::ServiceCallException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::ServiceCallTimeout e) {
		    throw new AppError(e.getMessage());
		}
		return doc.GetStatus.GetStatusRs;

	}

	/*Radix::System::Instance:invokeUnitCommand-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:invokeUnitCommand")
	public published  org.radixware.schemas.systeminstancecontrol.UnitCommandRs invokeUnitCommand (Int unitId, org.apache.xmlbeans.XmlObject xmlRq, int timeoutSec) throws java.lang.InterruptedException,org.radixware.kernel.common.exceptions.ServiceCallException,org.radixware.kernel.common.exceptions.ServiceCallFault,org.radixware.kernel.common.exceptions.ServiceCallTimeout {
		InstanceControlServiceWsdl:UnitCommandDocument xDoc = InstanceControlServiceWsdl:UnitCommandDocument.Factory.newInstance();
		xDoc.addNewUnitCommand().addNewUnitCommandRq().UnitId = unitId.intValue();
		xDoc.UnitCommand.UnitCommandRq.User = Arte::Arte.getUserName();
		xDoc.UnitCommand.UnitCommandRq.Request = xmlRq;
		InstanceControlServiceWsdl:UnitCommandDocument rsDoc = (InstanceControlServiceWsdl:UnitCommandDocument) Arte::Arte.invokeInternalService(xDoc, InstanceControlServiceWsdl:UnitCommandDocument.class,
		        ServiceUri:InstanceControlWsdl.getValue() + "#" + id.toString(),
		        0, timeoutSec, null);

		return rsDoc.UnitCommand.UnitCommandRs;
	}

	/*Radix::System::Instance:stopInstance-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopInstance")
	public published  org.radixware.schemas.systeminstancecontrol.StopInstanceRs stopInstance (Int timeoutBeforeHardStop) throws org.radixware.kernel.common.exceptions.ServiceCallFault {
		InstanceControlServiceWsdl:StopInstanceDocument doc = InstanceControlServiceWsdl:StopInstanceDocument.Factory.newInstance();
		try {
		    doc.addNewStopInstance().addNewStopInstanceRq().setTimeout(timeoutBeforeHardStop);
		    doc.set(Arte::Arte.invokeInternalService(doc, InstanceControlServiceWsdl:StopInstanceDocument.class,
		            ServiceUri:InstanceControlWsdl.getValue() + "#" + id.toString(),
		            0, 30, 10, null));
		} catch (Exceptions::ServiceCallException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::ServiceCallTimeout e) {
		    throw new AppError(e.getMessage());
		}
		return doc.StopInstance.StopInstanceRs;

	}

	/*Radix::System::Instance:stopAllUnits-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopAllUnits")
	private final  org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs stopAllUnits () {
		InstanceControlServiceWsdl:StopAllUnitsDocument doc = InstanceControlServiceWsdl:StopAllUnitsDocument.Factory.newInstance();
		doc.addNewStopAllUnits().addNewStopAllUnitsRq();
		doc.StopAllUnits.StopAllUnitsRq.User = Arte::Arte.getUserName();
		doc.set(invoke(doc, InstanceControlServiceWsdl:StopAllUnitsDocument.class, 60));
		return doc.getStopAllUnits().getStopAllUnitsRs();
	}

	/*Radix::System::Instance:startAllUnits-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startAllUnits")
	private final  org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs startAllUnits () {
		InstanceControlServiceWsdl:StartAllUnitsDocument doc = InstanceControlServiceWsdl:StartAllUnitsDocument.Factory.newInstance();
		doc.addNewStartAllUnits().addNewStartAllUnitsRq();
		doc.StartAllUnits.StartAllUnitsRq.User = Arte::Arte.getUserName();
		doc.set(invoke(doc, InstanceControlServiceWsdl:StartAllUnitsDocument.class, 60));
		return doc.getStartAllUnits().getStartAllUnitsRs();
	}

	/*Radix::System::Instance:restartAllUnits-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:restartAllUnits")
	private final  org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs restartAllUnits () {
		InstanceControlServiceWsdl:RestartAllUnitsDocument doc = InstanceControlServiceWsdl:RestartAllUnitsDocument.Factory.newInstance();
		// rq = 
		doc.addNewRestartAllUnits().addNewRestartAllUnitsRq();
		doc.RestartAllUnits.RestartAllUnitsRq.User = Arte::Arte.getUserName();
		doc.set(invoke(doc, InstanceControlServiceWsdl:RestartAllUnitsDocument.class, 60));
		return doc.getRestartAllUnits().getRestartAllUnitsRs();
	}

	/*Radix::System::Instance:reloadArtePool-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:reloadArtePool")
	private final  org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs reloadArtePool () {
		InstanceControlServiceWsdl:ReloadArtePoolDocument doc = InstanceControlServiceWsdl:ReloadArtePoolDocument.Factory.newInstance();
		doc.addNewReloadArtePool().addNewReloadArtePoolRq();
		doc.ReloadArtePool.ReloadArtePoolRq.User = Arte::Arte.getUserName();
		doc.set(invoke(doc, InstanceControlServiceWsdl:ReloadArtePoolDocument.class, 30));
		return doc.ReloadArtePool.ReloadArtePoolRs;
	}

	/*Radix::System::Instance:performMaintenance-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:performMaintenance")
	private final  org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs performMaintenance () {
		InstanceControlServiceWsdl:InstanceMaintenanceDocument doc = InstanceControlServiceWsdl:InstanceMaintenanceDocument.Factory.newInstance();
		doc.addNewInstanceMaintenance().addNewInstanceMaintenanceRq();
		doc.InstanceMaintenance.InstanceMaintenanceRq.User = Arte::Arte.getUserName();
		doc.set(invoke(doc, InstanceControlServiceWsdl:InstanceMaintenanceDocument.class, 30));
		return doc.InstanceMaintenance.InstanceMaintenanceRs;
	}

	/*Radix::System::Instance:invoke-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:invoke")
	private final  org.apache.xmlbeans.XmlObject invoke (org.apache.xmlbeans.XmlObject rq, java.lang.Class<?> resultClass, Int timeoutSec) {
		try {
		    return Arte::Arte.invokeInternalService(rq, resultClass,
		            ServiceUri:InstanceControlWsdl.getValue() + "#" + id.toString(),
		            0, timeoutSec.intValue(), null);
		} catch (Exceptions::ServiceCallFault e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::ServiceCallException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::ServiceCallTimeout e) {
		    throw new AppError(e.getMessage());
		}
	}

	/*Radix::System::Instance:restartInstance-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:restartInstance")
	public published  org.radixware.schemas.systeminstancecontrol.RestartInstanceRs restartInstance (Int delayBeforeHardStopSec) throws org.radixware.kernel.common.exceptions.ServiceCallFault {
		InstanceControlServiceWsdl:RestartInstanceDocument doc = InstanceControlServiceWsdl:RestartInstanceDocument.Factory.newInstance();
		try {
		    doc.addNewRestartInstance().addNewRestartInstanceRq().User = Arte::Arte.getUserName();
		    if (delayBeforeHardStopSec != null) {
		        doc.getRestartInstance().getRestartInstanceRq().setTimeoutSeconds(delayBeforeHardStopSec);
		    }
		    doc.set(Arte::Arte.invokeInternalService(doc, InstanceControlServiceWsdl:RestartInstanceDocument.class,
		            ServiceUri:InstanceControlWsdl.getValue() + "#" + id.toString(),
		            0, 30, null));
		} catch (Exceptions::ServiceCallException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::InterruptedException e) {
		    throw new AppError(e.getMessage());
		} catch (Exceptions::ServiceCallTimeout e) {
		    throw new AppError(e.getMessage());
		}
		return doc.RestartInstance.RestartInstanceRs;

	}

	/*Radix::System::Instance:execUpgradeTask-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:execUpgradeTask")
	public  org.radixware.schemas.systeminstancecontrol.UpgradeInstanceMess execUpgradeTask (org.radixware.schemas.systeminstancecontrol.UpgradeInstanceMess upgradeTask) {
		InstanceControlServiceWsdl:UpgradeInstanceDocument doc = InstanceControlServiceWsdl:UpgradeInstanceDocument.Factory.newInstance();
		doc.setUpgradeInstance(upgradeTask);
		return ((InstanceControlServiceWsdl:UpgradeInstanceDocument) invoke(doc, InstanceControlServiceWsdl:UpgradeInstanceDocument.class, 30)).UpgradeInstance;
	}










	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{
		if(cmdId == cmd5TAASNN6RNFOBLLWUQIK3S6WN4){
			org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs result = onCommand_performMaintenance(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmd5VYLPC23CBCP3JAZLZAX7GZJOU){
			onCommand_ActualizeVer(newPropValsById);
			return null;
		} else if(cmdId == cmdB3BDOSBJHVFDVCOMB34ZKXWFRM){
			org.radixware.schemas.systeminstancecontrol.FillArteTableRs result = onCommand_fillArteTable(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdD37DTAK4AFDZ3ISCFNXNREG2HY){
			org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs result = onCommand_reloadArtePool(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdEMWWCWA3SHOBDCKRAALOMT5GDM){
			org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs result = onCommand_StopAllUnits(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdIVHWGK3YW5CGFAX3SASK74K5ZI){
			org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument result = onCommand_ShowUsedAddresses(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdNXCBARNCSLOBDCKTAALOMT5GDM){
			org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs result = onCommand_StartAllUnits(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else if(cmdId == cmdXG6DDFFSQPOBDCKCAALOMT5GDM){
			org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs result = onCommand_RestartAllUnits(newPropValsById);
			if(result != null)
				output.set(result);
			return null;
		} else 
			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::System::Instance - Server Meta*/

/*Radix::System::Instance-Entity Class*/

package org.radixware.ads.System.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Instance_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),"Instance",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::System::Instance:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
							/*Owner Class Name*/
							"Instance",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),
							/*Property presentations*/

							/*Radix::System::Instance:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::System::Instance:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::Instance:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::Instance:startOSCommand:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO6KNHKT4RHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Instance:stopOSCommand:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colROO4LOT4RHWDBRCXAAIT4AGD7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Instance:keyAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdULKYARIUXBFI5MPQNEWNKM6QKI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL3UHR26LYRDOPNHTBAQ2MDLTCM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:sapPropsXml:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMUOPZ76SOVFJBNZVEXOLVE2TSA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:warning:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP3N5ITE3DFCU7HDUIKM2MCWIIU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:arteCntAboveNormal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7TINTWQVARGBRPLD2OLCBC2PJI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:arteCntCritical:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AFWSTW7EJAHDCWFP4FXF57W5M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:arteCntHigh:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM522N4URWVD3BAYUQ4DBXFV6XA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:arteCntVeryHigh:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY4ZKX4SUCBAHDLYFDL64EZSY6M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:arteInstCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBCHU436MFFEPLQPW6H6QGHNOE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:arteInstLiveTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPVFIBHTN5NA35FF6RJCZIM6SAY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:useActiveArteLimits:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ABN5MPEFBGRNERT3MSWLBFILE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:lowArteInstCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z2JD6POZNCZJLGNTEGIJHZEQQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:highArteInstCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27DHEMV4S5FAVBSPLT6B6R4E6M"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:started:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.noneOf(org.radixware.kernel.common.enums.EPropAttrInheritance.class)),

									/*Radix::System::Instance:maxTraceFileCnt:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPBWOB42OZCATJ6INHY24PAYLI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:maxTraceFileSizeKb:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTN2GUM43VD5TH34XEED3NBSWM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:rotateTraceFilesDaily:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCHSEV5KJGVACBL2YSMNARJCWKI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:traceFilesDir:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXGRPSYS6JVGHRMXMH2FEUSJMQU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:guiTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col24KG33MBMRAATA7S5VFV34PLTE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:fileTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF666DZAJ3NF3BOLDRS6BDK75T4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:dbTraceProfile:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUJAMRNBTMVE2BHL3BKCXMXSNMI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:autoActualizeVer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDM5FZM6CNAAVLLCHIQB4F6WL4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:httpProxy:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5B6RPEAWRBJVMM2OVLX3MSGKY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:httpsProxy:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMQKXC2RA7BFJTI5YEECPN2JU2I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:keyStorePath:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDAQ557DC6BAZRI6VNI3WDLB5HQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:keyStoreType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YSQY2R375FKFKYACAY762WGD4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:keyTabFilePath:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTLHWR7V5OJE53K6G4EJDPJY2XY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:oraImplStmtCacheSize:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:useOraImplStmtCache:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD3UDCXDDAJDR7B3YEBXV3TBNAU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:sapId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA333YBDFB5EMVI6N4N6IUKFG3M"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:scp:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDRGOFLVET7NRDB56AALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecAAU55TOEHJWDRPOYAAYQQ2Y3GB\" PropId=\"colWUQ25PISLDNBDJA6ACQMTAIZT4\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = 1</xsc:Sql></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWDOEJDIMZPNRDB64AALOMT5GDM")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:avgActiveArteCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5F32BDXFU5GLJDKLCF5OHFYQDI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:memoryCheckPeriodSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colETPNDLR7SVE4JEEF664PVZPKN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:targetExecutor:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4KPRPAXBXRCYBBGYUXJNALPQQQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(133693439,null,null,null),null,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Instance:targetExecutorId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFNUWBLQL3RCATBI3O5KPEXF7A4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:maxActiveArteNormal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:maxActiveArteAboveNormal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:maxActiveArteHigh:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:maxActiveArteVeryHigh:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:maxActiveArteCritical:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:sensTraceFinishTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVRAIUGRMHVFLZJ4VCWCCPZTQPM"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,true,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:classLoadingProfileId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJMKGEQX2FG53KHYXUTIJMMKYE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:classLoadingProfile:PropertyPresentation-Parent Ref Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadParentTitlePropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UQLYTWVCJEUDHVXJFXYGP7KN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,null,null,new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),org.radixware.kernel.server.types.Restrictions.Factory.newInstance(3668987,null,null,null),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHGKCI7PIZRAK3BTNOQ75K2MUJA")},null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.TITLE,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR)),

									/*Radix::System::Instance:sysdate:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7COJCFXLD5BU3HOJD4DMPNJF3E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:activityStatus:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:aadcDgAddress:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOF6APP4I7FHV7BCQLHO75CZR74"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:aadcMemberId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),org.radixware.kernel.common.enums.EEditPossibility.ON_CREATE,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:isAadcSysMember:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFJL567ZQ4NDZTPCDLNPJFKIUCU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:autoRestartDelaySec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM65QF5VLNZD6FPDDMADFENBBUU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:osPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col55KRIWQ6TVBX3CIF4UY25YTMZE"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:appVersion:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYGFOEBBHJEPHGO5B2Y4E6QUBA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:kernelVersion:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFPV5MEWHSNFFRGQN4V5CEMIXXQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:uptimeStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVITFLAYDGNHBZOXAQIT3GUPQ2Q"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:dbCurMillis:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWOPG2OAWFD2PIM5SAH75DXSPI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:revision:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3YVJTL7CFCALHOXPVVDPZID7Q"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:canEditActualizeVer:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:effectiveSelfCheckTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5RRJ2XRTYZCRJOOTZPSFGIVIMM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:instStateForcedGatherPeriodSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIKMV3NWRZCDXHXWRERFBXVFT4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:instStateGatherPeriodSec:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2X4UBSDEPBBG5LYPLRDF6YSOIU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:instStateHistoryStorePeriodDays:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGHZ3KYMJURDDBHJ3IJXSUY35LA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:cpuCoreCount:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZAGLT4KO6VC45P3ANFRXEJEFRA"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:hostName:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Q5VAISXVREI3K3APBJWYJWQYQ"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::System::Instance:hostIpAddresses:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTUDXYLL6HVEIZORAMXTOVJJSHA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:startAllUnitsCmd-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNXCBARNCSLOBDCKTAALOMT5GDM"),"startAllUnitsCmd",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:stopAllUnitsCmd-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMWWCWA3SHOBDCKRAALOMT5GDM"),"stopAllUnitsCmd",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:restartAllUnitsCmd-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdXG6DDFFSQPOBDCKCAALOMT5GDM"),"restartAllUnitsCmd",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:editKeystoreAliasesCmd-Property Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUJAYTWUX35CGFJTSTUMQNAB6EY"),"editKeystoreAliasesCmd",org.radixware.kernel.common.enums.ECommandScope.PROPERTY,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAO4GDPUN4VCSJPJ57OZ5W7S7RA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEH3ABYCN6JC33KQ24QKS2LTF24"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAGDAVFAP5RB4ZEJMLLJZYD3XM4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7UOXOTN33FDA3OWKL4CAPPYDJQ")},org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:showUsedPortsCmd-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIVHWGK3YW5CGFAX3SASK74K5ZI"),"showUsedPortsCmd",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:applyOptionsFromFile-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMKKHVZCNBVC2PCLYYDG2PXJQ2I"),"applyOptionsFromFile",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:actualizeVer-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5VYLPC23CBCP3JAZLZAX7GZJOU"),"actualizeVer",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:reloadArtePool-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdD37DTAK4AFDZ3ISCFNXNREG2HY"),"reloadArtePool",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:performMaintenance-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5TAASNN6RNFOBLLWUQIK3S6WN4"),"performMaintenance",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:getInstanceStateReport-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOCIRXO2BUZET3HKCEEN22NL7EI"),"getInstanceStateReport",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:fillArteTable-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdB3BDOSBJHVFDVCOMB34ZKXWFRM"),"fillArteTable",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true),

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::System::Instance:captureClassLoadingProfile-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLYBOIRRR3JALTG5PGK7PURRMNE"),"captureClassLoadingProfile",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::Instance:Started First-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQBCIUYAERTNRDB5PAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),"Started First",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJWK3KXQYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),org.radixware.kernel.common.enums.EOrder.DESC),

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::System::Instance:By ID-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYLZEGABQFCE7HXEA5CZC6IOQ4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),"By ID",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWY6H2WEAJDPLIEEBKGIVIXNRY"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>index(</xsc:Sql></xsc:Item><xsc:Item><xsc:ThisTableSqlName/></xsc:Item><xsc:Item><xsc:Sql> </xsc:Sql></xsc:Item><xsc:Item><xsc:IndexDbName TableId=\"tbl52CHFNO3EGWDBRCRAAIT4AGD7E\"/></xsc:Item><xsc:Item><xsc:Sql>) </xsc:Sql></xsc:Item></xsc:Sqml>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::System::Instance:General-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM"),
									"General",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									null,
									2192,
									null,

									/*Radix::System::Instance:General:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::System::Instance:General:Unit-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiOQENK3JCXTNRDB6QAALOMT5GDM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refPDY4KBVSJ3NRDAQSABIFNQAAAE"),
													null,
													null),

												/*Radix::System::Instance:General:EventLog-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUNTJ4UIBTJA6JCY7XR4IHRZ4OM"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null),

												/*Radix::System::Instance:General:ArteInstance-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFJUXS7VBXJF3DDHROPUQXLIKHQ"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA24OTIGNQBCGHIIJLHL2FF75KY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLZ2MP65VX5GZJP62JFIJSII76Q"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refHPVDHI3FWZE4ZF3Y2MSXY7NB5E"),
													null,
													null),

												/*Radix::System::Instance:General:InstanceStateHistory-Entity Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadTableExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiPZKBAATGQRDXLC432TZ4F3COUY"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef("<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:PropSqlName TableId=\"aecRHY4TVWIC5C4TJN2GKL2ABRKKQ\" PropId=\"colIFISIE3XWBD3LLQ3MZGOOG7N6M\" Owner=\"THIS\"/></xsc:Item><xsc:Item><xsc:Sql> = </xsc:Sql></xsc:Item><xsc:Item><xsc:PropSqlName TableId=\"aec52CHFNO3EGWDBRCRAAIT4AGD7E\" PropId=\"col3VINP666G5VDBFSUAAUMFADAIA\" Owner=\"PARENT\"/></xsc:Item></xsc:Sqml>","<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMWWCWA3SHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNXCBARNCSLOBDCKTAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdXG6DDFFSQPOBDCKCAALOMT5GDM")},null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::System::Instance:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM"),"General",null,144,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),false)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQBCIUYAERTNRDB5PAALOMT5GDM"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16800,null,null,null),null)
							},
							/*Default selector presentation Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM"),
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::System::Instance:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),"",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
							},org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEAEBMIIYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							null,
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16384,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::System::Instance:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::System::Instance:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXSBA5QYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYWM27QYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:startOSCommand-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO6KNHKT4RHWDBRCXAAIT4AGD7E"),"startOSCommand",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFVMK2IZSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:stopOSCommand-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colROO4LOT4RHWDBRCXAAIT4AGD7E"),"stopOSCommand",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVMK2IZSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:deletedSapId-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB3QZMHJ4LTOBDCJFAALOMT5GDM"),"deletedSapId",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:systemId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNPEGS5CTHRHRDF7ZU3IPI7GELU"),"systemId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNKFHSKWBOBCPZD7DUDFBP5VPGY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:keyAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdULKYARIUXBFI5MPQNEWNKM6QKI"),"keyAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:trustedCertificateAliases-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL3UHR26LYRDOPNHTBAQ2MDLTCM"),"trustedCertificateAliases",null,org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:sapPropsXml-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMUOPZ76SOVFJBNZVEXOLVE2TSA"),"sapPropsXml",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:warning-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP3N5ITE3DFCU7HDUIKM2MCWIIU"),"warning",null,org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:system-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLOKA72I4FD47GOUZFUTM2ZHIM"),"system",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDQNZTSWLHJHNZCQPRKGHU5ZS4I"),org.radixware.kernel.common.enums.EValType.PARENT_REF,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("aecX5TD7JDVVHWDBROXAAIT4AGD7E"),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:arteCntAboveNormal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7TINTWQVARGBRPLD2OLCBC2PJI"),"arteCntAboveNormal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM55QIJUMBEBPFEWUKCEGCGCFU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:arteCntCritical-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AFWSTW7EJAHDCWFP4FXF57W5M"),"arteCntCritical",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLDAJAN2OVDI5IQGJNUKQKAOKU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:arteCntHigh-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM522N4URWVD3BAYUQ4DBXFV6XA"),"arteCntHigh",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JFGOBLVGZG67EIKZ2P6ZLLEAI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:arteCntVeryHigh-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY4ZKX4SUCBAHDLYFDL64EZSY6M"),"arteCntVeryHigh",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUHMGQLCEVAF5HQKVW64XU27SU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:arteInstCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBCHU436MFFEPLQPW6H6QGHNOE"),"arteInstCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X2FHQRQPFARBMC66VYNPSS3CQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:arteInstLiveTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPVFIBHTN5NA35FF6RJCZIM6SAY"),"arteInstLiveTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB7JIIV5BLJBD7KP6SJHU4K3JZQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:useActiveArteLimits-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ABN5MPEFBGRNERT3MSWLBFILE"),"useActiveArteLimits",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDLLCNOLAFEWVD6LNG647RVZUY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:lowArteInstCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z2JD6POZNCZJLGNTEGIJHZEQQ"),"lowArteInstCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHAGMYJJ6VRDGTGZRG46SEWDD5M"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:highArteInstCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27DHEMV4S5FAVBSPLT6B6R4E6M"),"highArteInstCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SOMLSZPBRFQVEJEZNNHALADRI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:started-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),"started",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTANRE6IIJB7BHDKLB6PTCNX24"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:selfCheckTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY5RSJBWJBDWDRD25AAYQQMVFBB"),"selfCheckTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSWSWMAGHFEX7FZBUIZBI2A5YA"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxTraceFileCnt-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPBWOB42OZCATJ6INHY24PAYLI"),"maxTraceFileCnt",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMPOSUEDVBCCJE7LSWHJFCDJIU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("10")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxTraceFileSizeKb-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTN2GUM43VD5TH34XEED3NBSWM"),"maxTraceFileSizeKb",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYWANGGUTVRGFDJDM3KVGMVDOYU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1024")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:rotateTraceFilesDaily-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCHSEV5KJGVACBL2YSMNARJCWKI"),"rotateTraceFilesDaily",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAMF3NHHVJVHFBKCB3EEBP3R6M4"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:traceFilesDir-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXGRPSYS6JVGHRMXMH2FEUSJMQU"),"traceFilesDir",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGEPL4H3H3VAXHAON6PV36CML7A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("./logs")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:guiTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col24KG33MBMRAATA7S5VFV34PLTE"),"guiTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27UGHLZD5NG7RKVDX27XMCOSNE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:fileTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF666DZAJ3NF3BOLDRS6BDK75T4"),"fileTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTFVLVJQRDHXIB6MDYFL4SZHY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:dbTraceProfile-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUJAMRNBTMVE2BHL3BKCXMXSNMI"),"dbTraceProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOR2TT376VGZDJ4MYS2RKRMTTY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Event")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:autoActualizeVer-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDM5FZM6CNAAVLLCHIQB4F6WL4"),"autoActualizeVer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBNN2FUK7BCNXHK73KJMKPOOZA"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:httpProxy-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5B6RPEAWRBJVMM2OVLX3MSGKY"),"httpProxy",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7KIBZJJXZHOZCCFGKTNZLGWG4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:httpsProxy-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMQKXC2RA7BFJTI5YEECPN2JU2I"),"httpsProxy",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXWXBOPA5ZFPBPHTKQCRAGQU2Q"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:jdwpAddress-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJTNXYUJB65C75JRATUZVYH6Y5Y"),"jdwpAddress",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:keyStorePath-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDAQ557DC6BAZRI6VNI3WDLB5HQ"),"keyStorePath",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34SDG7OPZEQXPFKS6TEPIVIXE"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("radixware.org/server/keys.jceks")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:keyStoreType-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YSQY2R375FKFKYACAY762WGD4"),"keyStoreType",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDVBTHYGRRCIPNG3SA3GT3KGJU"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFOUQICKMYJAGDL356HLXUNICKE"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:keyTabFilePath-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTLHWR7V5OJE53K6G4EJDPJY2XY"),"keyTabFilePath",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOADIICLERFHS3LN5XEPACZLRSU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:oraImplStmtCacheSize-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"),"oraImplStmtCacheSize",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls35EJSP3CJ5AMFNQRAIVDT54SH4"),org.radixware.kernel.common.enums.EValType.INT,null,true,null,new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLOKA72I4FD47GOUZFUTM2ZHIM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colJN25VAA7SVD5FKDTO47ERVQIDQ"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:useOraImplStmtCache-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD3UDCXDDAJDR7B3YEBXV3TBNAU"),"useOraImplStmtCache",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XVB54JPJJD5FKJXAA4DHG74QU"),org.radixware.kernel.common.enums.EValType.BOOL,null,true,null,new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLOKA72I4FD47GOUZFUTM2ZHIM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colKQKCWI72DVAO7OFVSNBZ7XN2WQ"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:sapId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA333YBDFB5EMVI6N4N6IUKFG3M"),"sapId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURH44I562NG25AKUUKACQEIYYY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:scp-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"),"scp",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOXLRPPSZ75GJZKE4LROFLCCUNY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refX5LLPHQYPBGLPJJHOZJ5RGEVUU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:scpName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYSPDMMOVEBGPZIMQX2RBMXCBQE"),"scpName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBYSN6NRC75DDTHQRVDN5XZFSNA"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:avgActiveArteCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5F32BDXFU5GLJDKLCF5OHFYQDI"),"avgActiveArteCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWF7HHSS2RVEHJL64Y6DH2QA3FY"),org.radixware.kernel.common.enums.EValType.NUM,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:memoryCheckPeriodSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colETPNDLR7SVE4JEEF664PVZPKN4"),"memoryCheckPeriodSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYFPIIMFMBD5BIS5LSEHENYCMY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("600")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:targetExecutor-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4KPRPAXBXRCYBBGYUXJNALPQQQ"),"targetExecutor",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTS4SNFXWUJAUXPPLMGBBEDKRDY"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refPT7FJIZP3JCIROWGR2UA2XGL3Y"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:targetExecutorId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFNUWBLQL3RCATBI3O5KPEXF7A4"),"targetExecutorId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxActiveArteNormal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"),"maxActiveArteNormal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO65EAYZJ5A2JFS2235KXKK3L4"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxActiveArteAboveNormal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"),"maxActiveArteAboveNormal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW67PPWIVA5EKXCR6T74XIHB7TU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxActiveArteHigh-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"),"maxActiveArteHigh",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWTXZ7RTBGHXLLCSOMNZGR6DM"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxActiveArteVeryHigh-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"),"maxActiveArteVeryHigh",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5B443SCPYZAS3FHDTQ4NLF2BJQ"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:maxActiveArteCritical-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"),"maxActiveArteCritical",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6POZVFOIZEQXHCVG7T576S66Q"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:sensTraceFinishTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVRAIUGRMHVFLZJ4VCWCCPZTQPM"),"sensTraceFinishTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRRZW4W3JNDXXJD4IE5BVMHMDA"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:classLoadingProfileId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJMKGEQX2FG53KHYXUTIJMMKYE"),"classLoadingProfileId",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:classLoadingProfile-Parent Reference*/

								new org.radixware.kernel.server.meta.clazzes.RadInnateRefPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UQLYTWVCJEUDHVXJFXYGP7KN4"),"classLoadingProfile",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGU222PSDVCWVLNA4IPVRAYLQU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("refKGDSAFFBXZGTJMYA6W5QP3RUWQ"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aecS6IN3XOILNANJG3BCB7NTDDMDM"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:sysdate-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7COJCFXLD5BU3HOJD4DMPNJF3E"),"sysdate",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,"DATE",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:Sql>sysdate</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:selfCheckTimeStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),"selfCheckTimeStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJJZGBX3IRHAVP7DECEU3YFFTM"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:activityStatus-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),"activityStatus",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPQARQYQ2VDLXMHQJ77MELKG2E"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:aadcDgAddress-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOF6APP4I7FHV7BCQLHO75CZR74"),"aadcDgAddress",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVF5JGZRMVCV7DSKPLTXKAF3UM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:aadcMemberId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),"aadcMemberId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN4LZUFJOJCNRNCO5N43FTGAAE"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:aadcMyScn-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYOS4XWY6OJAUPDBVXC7EOFBZ7M"),"aadcMyScn",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:aadcMyTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colG5VAJYLF65AC3LPOYSOUMTGJU4"),"aadcMyTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:isAadcSysMember-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFJL567ZQ4NDZTPCDLNPJFKIUCU"),"isAadcSysMember",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:autoRestartDelaySec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM65QF5VLNZD6FPDDMADFENBBUU"),"autoRestartDelaySec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRWVUWS7HVC3VH2MIWKNQCQ4BA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:selfCheckTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colK3BQWEKR6RD3TNL6K6CCHNLQZ4"),"selfCheckTimeMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:osPid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col55KRIWQ6TVBX3CIF4UY25YTMZE"),"osPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZCSYUK7ZFGBTLENATKOZ2DWLU"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:appVersion-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYGFOEBBHJEPHGO5B2Y4E6QUBA"),"appVersion",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGPVMIPUDSRCRJN56RQYHGU2V4Q"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:kernelVersion-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFPV5MEWHSNFFRGQN4V5CEMIXXQ"),"kernelVersion",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7CYYIAQBBETJI3JU3VR3IVSWU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:startTimeMillis-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZ6GOA7GCZ5C7JLT7TXUKT7JVHA"),"startTimeMillis",null,org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:uptimeStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVITFLAYDGNHBZOXAQIT3GUPQ2Q"),"uptimeStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXJO7WUUNDZJIRMO45U2FOLK4"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:dbCurMillis-Expression Property*/

								new org.radixware.kernel.server.meta.clazzes.RadSqmlPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWOPG2OAWFD2PIM5SAH75DXSPI"),"dbCurMillis",null,org.radixware.kernel.common.enums.EValType.INT,"NUMBER(9,0)",null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"><xsc:Item><xsc:DbFuncCall FuncId=\"dfnSEB3USUCPZCZNHQIZFMK4B5SIE\"/></xsc:Item><xsc:Item><xsc:Sql>()</xsc:Sql></xsc:Item></xsc:Sqml>",true,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:revision-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3YVJTL7CFCALHOXPVVDPZID7Q"),"revision",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWMZC6XE3NDBJH22DNJJ3DEI7U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:canEditActualizeVer-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),"canEditActualizeVer",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:effectiveSelfCheckTime-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5RRJ2XRTYZCRJOOTZPSFGIVIMM"),"effectiveSelfCheckTime",null,org.radixware.kernel.common.enums.EValType.DATE_TIME,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:instStateForcedGatherPeriodSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIKMV3NWRZCDXHXWRERFBXVFT4"),"instStateForcedGatherPeriodSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVL23IIZ34FCN5MG6J7OWSKL6WI"),org.radixware.kernel.common.enums.EValType.INT,null,true,org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLOKA72I4FD47GOUZFUTM2ZHIM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colIG74GFS3QNA5PFW3P27DVCWSPQ"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:instStateGatherPeriodSec-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2X4UBSDEPBBG5LYPLRDF6YSOIU"),"instStateGatherPeriodSec",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMKMU66PPJG3NFVQH3BUH5PI2A"),org.radixware.kernel.common.enums.EValType.INT,null,true,org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLOKA72I4FD47GOUZFUTM2ZHIM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colLKDHAIE6KZH7BKNDT4ALVM2HCI"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:instStateHistoryStorePeriodDays-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGHZ3KYMJURDDBHJ3IJXSUY35LA"),"instStateHistoryStorePeriodDays",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCFTW5WON5GDLCZLPYBKSFTLIA"),org.radixware.kernel.common.enums.EValType.INT,null,true,null,new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath[]{

										new org.radixware.kernel.server.meta.clazzes.RadPropDef.ValInheritancePath(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJLOKA72I4FD47GOUZFUTM2ZHIM")},org.radixware.kernel.common.types.Id.Factory.loadFrom("colA4GEOKW25BFTZIBY5PJ5W3KTCI"))
								},org.radixware.kernel.common.enums.EPropInitializationPolicy.DEFINE_IF_NOT_INHERITED,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:cpuCoreCount-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZAGLT4KO6VC45P3ANFRXEJEFRA"),"cpuCoreCount",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFXD5D3ULZD35AXQTECYBRT55U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:hostIpAddressesStr-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFZIFMBLFCNFBXEAV7LGJAS2LNM"),"hostIpAddressesStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EZJB6V3PRAQFIPZOFKFR622PI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:hostName-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Q5VAISXVREI3K3APBJWYJWQYQ"),"hostName",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLB2GYJPR2NAGBEZPQO5DUZAGGI"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::System::Instance:hostIpAddresses-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTUDXYLL6HVEIZORAMXTOVJJSHA"),"hostIpAddresses",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIKLMMDE7NEQJHHKJFGV2MKID4"),org.radixware.kernel.common.enums.EValType.ARR_STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::System::Instance:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4B4U2PB5M5CH7GZTVV33WSGVKU"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdEMWWCWA3SHOBDCKRAALOMT5GDM"),"onCommand_StopAllUnits",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3ERTHVX2NVAXHF4KPCYNPKUXXI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdNXCBARNCSLOBDCKTAALOMT5GDM"),"onCommand_StartAllUnits",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOE3VZCIHVJCF7P4JDC5RIF53V4"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdXG6DDFFSQPOBDCKCAALOMT5GDM"),"onCommand_RestartAllUnits",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprS4BBT7VQ4RFNBDU7O2K3TJQF44"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprTITISOLB6RFG7DI72V3N7K6QVE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprG7PEDGZC5ZGYDGLOYBU5GMDPEA"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6KS7LCP4W5AW5OCCJHH5KGO4YQ"),"afterCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr4DG4IAROZZE5HKJB2YRODUF7HM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd5VYLPC23CBCP3JAZLZAX7GZJOU"),"onCommand_ActualizeVer",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr72GMRK3C6VEFLFAGJ5ATL5UNFA"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdIVHWGK3YW5CGFAX3SASK74K5ZI"),"onCommand_ShowUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXYZCV6PE2ZFPNNCXAIFZJG5KLE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRSM4WBYDU5E3RMPP4X3CZBPCPI"),"getUsedAddresses",false,false,org.radixware.kernel.common.enums.EAccess.DEFAULT,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNKUERC7IJBU5L4IGOMQNFXRIE"),"updateService",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2UCNHZSLQVGXPG7Z4SJQJPPSAY"),"afterDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2QDZMS2EHVALTDRHZORG7BT6ZE"),"deleteService",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdD37DTAK4AFDZ3ISCFNXNREG2HY"),"onCommand_reloadArtePool",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHKE2ZIBI3BC4DMTUSTCEECBMFA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmd5TAASNN6RNFOBLLWUQIK3S6WN4"),"onCommand_performMaintenance",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprNFNR3AYWCJHQHJQRMLTEPMO4VQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU"),"beforeInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("initPropValsById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprERT5REYX3VFS7GYW4ETH2HDJ6U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJUEQ2THACFHVNMQYLD7G63OLYY")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprANOI5OQ3KRG7ZOPVKIFUX22MOE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthcmdB3BDOSBJHVFDVCOMB34ZKXWFRM"),"onCommand_fillArteTable",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("newPropValHandlersById",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr26V2DPAKKNCNHALP7IOPYYR5GI"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthCHTMZHFAEZBRNCZYD2EZ2TBFK4"),"applyOptions",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("options",org.radixware.kernel.common.enums.EValType.ARR_STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOLA2V5EWBVCHNKF2LY2DAFGYXY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZ44U3RB2VJBBNO5Y2SSEUXLG54"),"fillArteTable",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTRJHTJ7FOJE33BBK2LQUXD2JXU"),"getArteClassLoadingProfile",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMY4TW32MFB2ZMKABNFN4W6PPY"),"getStatus",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthI53TZEBJCFAJPNLDA4H7SNTKTM"),"invokeUnitCommand",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("unitId",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGQPEXVIFGBF4RM3DDCZIQJOP7U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xmlRq",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr3KM5KI626ZBBZAAQK36MCSFXGI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutSec",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprRT4RXS76UZBT3KVU54XJO534PA"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDK6FEQYOWVFINIJ7FSXKRBUKWM"),"stopInstance",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutBeforeHardStop",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2I6QFSW4LJCABERXD3NB2AGZHQ"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBOGNZSWXSHOBDCKSAALOMT5GDM"),"stopAllUnits",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYETGRFVESLOBDCKTAALOMT5GDM"),"startAllUnits",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthMMUQ3UPOSDOBDCKRAALOMT5GDM"),"restartAllUnits",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthND5NIXLJ75D3VLPKS27ZFN7HQE"),"reloadArtePool",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTWMPGHKSDNAVTPEVRZJAHTTUNY"),"performMaintenance",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth2MK6I67PSDOBDCKRAALOMT5GDM"),"invoke",false,false,org.radixware.kernel.common.enums.EAccess.PRIVATE,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2MNCSSH54JFUVNSL67VUAXOR3M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("resultClass",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprB2ICNVYV25ERFIRS7JCTTTVCWQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("timeoutSec",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprUP3SVAKKKJBXLCQHAK3AA53KDU"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOKUR3OKKDRHMTDOT5RMHQYAGUE"),"restartInstance",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("delayBeforeHardStopSec",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprIXE2FUSZKRCEZC77IILSTGRSJE"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEKNTFC2QHNGIXJYNXIJ5FEZU2Q"),"execUpgradeTask",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("upgradeTask",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEIKI6MBIB5E5HAHWG3D7KSDHEA"))
								},org.radixware.kernel.common.enums.EValType.XML)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::System::Instance - Desktop Executable*/

/*Radix::System::Instance-Entity Class*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance")
public interface Instance {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.System.explorer.Instance.Instance_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.explorer.Instance.Instance_DefaultModel )  super.getEntity(i);}
	}
















































































































































































































































































































































































































	/*Radix::System::Instance:guiTraceProfile:guiTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:guiTraceProfile:guiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:guiTraceProfile:guiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public GuiTraceProfile getGuiTraceProfile();
	/*Radix::System::Instance:highArteInstCount:highArteInstCount-Presentation Property*/


	public class HighArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:highArteInstCount:highArteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:highArteInstCount:highArteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighArteInstCount getHighArteInstCount();
	/*Radix::System::Instance:useActiveArteLimits:useActiveArteLimits-Presentation Property*/


	public class UseActiveArteLimits extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseActiveArteLimits(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useActiveArteLimits:useActiveArteLimits")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useActiveArteLimits:useActiveArteLimits")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseActiveArteLimits getUseActiveArteLimits();
	/*Radix::System::Instance:instStateGatherPeriodSec:instStateGatherPeriodSec-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateGatherPeriodSec getInstStateGatherPeriodSec();
	/*Radix::System::Instance:arteCntCritical:arteCntCritical-Presentation Property*/


	public class ArteCntCritical extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntCritical(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntCritical:arteCntCritical")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntCritical:arteCntCritical")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntCritical getArteCntCritical();
	/*Radix::System::Instance:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::Instance:keyStoreType:keyStoreType-Presentation Property*/


	public class KeyStoreType extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public KeyStoreType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EKeyStoreType dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EKeyStoreType ? (org.radixware.kernel.common.enums.EKeyStoreType)x : org.radixware.kernel.common.enums.EKeyStoreType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EKeyStoreType> getValClass(){
			return org.radixware.kernel.common.enums.EKeyStoreType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EKeyStoreType dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EKeyStoreType ? (org.radixware.kernel.common.enums.EKeyStoreType)x : org.radixware.kernel.common.enums.EKeyStoreType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStoreType:keyStoreType")
		public  org.radixware.kernel.common.enums.EKeyStoreType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStoreType:keyStoreType")
		public   void setValue(org.radixware.kernel.common.enums.EKeyStoreType val) {
			Value = val;
		}
	}
	public KeyStoreType getKeyStoreType();
	/*Radix::System::Instance:maxActiveArteNormal:maxActiveArteNormal-Presentation Property*/


	public class MaxActiveArteNormal extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteNormal:maxActiveArteNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteNormal:maxActiveArteNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteNormal getMaxActiveArteNormal();
	/*Radix::System::Instance:targetExecutor:targetExecutor-Presentation Property*/


	public class TargetExecutor extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public TargetExecutor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutor:targetExecutor")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutor:targetExecutor")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public TargetExecutor getTargetExecutor();
	/*Radix::System::Instance:lowArteInstCount:lowArteInstCount-Presentation Property*/


	public class LowArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LowArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:lowArteInstCount:lowArteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:lowArteInstCount:lowArteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LowArteInstCount getLowArteInstCount();
	/*Radix::System::Instance:osPid:osPid-Presentation Property*/


	public class OsPid extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OsPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:osPid:osPid")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:osPid:osPid")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OsPid getOsPid();
	/*Radix::System::Instance:avgActiveArteCount:avgActiveArteCount-Presentation Property*/


	public class AvgActiveArteCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgActiveArteCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:avgActiveArteCount:avgActiveArteCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:avgActiveArteCount:avgActiveArteCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgActiveArteCount getAvgActiveArteCount();
	/*Radix::System::Instance:maxActiveArteCritical:maxActiveArteCritical-Presentation Property*/


	public class MaxActiveArteCritical extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteCritical(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteCritical:maxActiveArteCritical")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteCritical:maxActiveArteCritical")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteCritical getMaxActiveArteCritical();
	/*Radix::System::Instance:hostName:hostName-Presentation Property*/


	public class HostName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HostName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostName:hostName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostName:hostName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HostName getHostName();
	/*Radix::System::Instance:sysdate:sysdate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sysdate:sysdate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sysdate:sysdate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Sysdate getSysdate();
	/*Radix::System::Instance:arteCntAboveNormal:arteCntAboveNormal-Presentation Property*/


	public class ArteCntAboveNormal extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntAboveNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntAboveNormal:arteCntAboveNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntAboveNormal:arteCntAboveNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntAboveNormal getArteCntAboveNormal();
	/*Radix::System::Instance:classLoadingProfile:classLoadingProfile-Presentation Property*/


	public class ClassLoadingProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ClassLoadingProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.explorer.ClassLoadingProfile.ClassLoadingProfile_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.explorer.ClassLoadingProfile.ClassLoadingProfile_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.explorer.ClassLoadingProfile.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.explorer.ClassLoadingProfile.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfile:classLoadingProfile")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfile:classLoadingProfile")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ClassLoadingProfile getClassLoadingProfile();
	/*Radix::System::Instance:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapId:sapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapId:sapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::System::Instance:httpProxy:httpProxy-Presentation Property*/


	public class HttpProxy extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HttpProxy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpProxy:httpProxy")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpProxy:httpProxy")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HttpProxy getHttpProxy();
	/*Radix::System::Instance:rotateTraceFilesDaily:rotateTraceFilesDaily-Presentation Property*/


	public class RotateTraceFilesDaily extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public RotateTraceFilesDaily(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:rotateTraceFilesDaily:rotateTraceFilesDaily")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:rotateTraceFilesDaily:rotateTraceFilesDaily")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public RotateTraceFilesDaily getRotateTraceFilesDaily();
	/*Radix::System::Instance:useOraImplStmtCache:useOraImplStmtCache-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useOraImplStmtCache:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useOraImplStmtCache:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache();
	/*Radix::System::Instance:keyStorePath:keyStorePath-Presentation Property*/


	public class KeyStorePath extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KeyStorePath(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStorePath:keyStorePath")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStorePath:keyStorePath")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KeyStorePath getKeyStorePath();
	/*Radix::System::Instance:arteInstCount:arteInstCount-Presentation Property*/


	public class ArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstCount:arteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstCount:arteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteInstCount getArteInstCount();
	/*Radix::System::Instance:scp:scp-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scp:scp")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scp:scp")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Scp getScp();
	/*Radix::System::Instance:memoryCheckPeriodSec:memoryCheckPeriodSec-Presentation Property*/


	public class MemoryCheckPeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MemoryCheckPeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:memoryCheckPeriodSec:memoryCheckPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:memoryCheckPeriodSec:memoryCheckPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MemoryCheckPeriodSec getMemoryCheckPeriodSec();
	/*Radix::System::Instance:fileTraceProfile:fileTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fileTraceProfile:fileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fileTraceProfile:fileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileTraceProfile getFileTraceProfile();
	/*Radix::System::Instance:started:started-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:started:started")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:started:started")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Started getStarted();
	/*Radix::System::Instance:targetExecutorId:targetExecutorId-Presentation Property*/


	public class TargetExecutorId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TargetExecutorId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutorId:targetExecutorId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutorId:targetExecutorId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TargetExecutorId getTargetExecutorId();
	/*Radix::System::Instance:kernelVersion:kernelVersion-Presentation Property*/


	public class KernelVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KernelVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:kernelVersion:kernelVersion")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:kernelVersion:kernelVersion")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KernelVersion getKernelVersion();
	/*Radix::System::Instance:maxActiveArteHigh:maxActiveArteHigh-Presentation Property*/


	public class MaxActiveArteHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteHigh:maxActiveArteHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteHigh:maxActiveArteHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteHigh getMaxActiveArteHigh();
	/*Radix::System::Instance:dbCurMillis:dbCurMillis-Presentation Property*/


	public class DbCurMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DbCurMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbCurMillis:dbCurMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbCurMillis:dbCurMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbCurMillis getDbCurMillis();
	/*Radix::System::Instance:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::System::Instance:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateHistoryStorePeriodDays getInstStateHistoryStorePeriodDays();
	/*Radix::System::Instance:maxTraceFileCnt:maxTraceFileCnt-Presentation Property*/


	public class MaxTraceFileCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxTraceFileCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileCnt:maxTraceFileCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileCnt:maxTraceFileCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxTraceFileCnt getMaxTraceFileCnt();
	/*Radix::System::Instance:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateForcedGatherPeriodSec getInstStateForcedGatherPeriodSec();
	/*Radix::System::Instance:maxTraceFileSizeKb:maxTraceFileSizeKb-Presentation Property*/


	public class MaxTraceFileSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxTraceFileSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileSizeKb:maxTraceFileSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileSizeKb:maxTraceFileSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxTraceFileSizeKb getMaxTraceFileSizeKb();
	/*Radix::System::Instance:revision:revision-Presentation Property*/


	public class Revision extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Revision(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:revision:revision")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:revision:revision")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Revision getRevision();
	/*Radix::System::Instance:maxActiveArteAboveNormal:maxActiveArteAboveNormal-Presentation Property*/


	public class MaxActiveArteAboveNormal extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteAboveNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteAboveNormal:maxActiveArteAboveNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteAboveNormal:maxActiveArteAboveNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteAboveNormal getMaxActiveArteAboveNormal();
	/*Radix::System::Instance:arteCntHigh:arteCntHigh-Presentation Property*/


	public class ArteCntHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntHigh:arteCntHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntHigh:arteCntHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntHigh getArteCntHigh();
	/*Radix::System::Instance:autoRestartDelaySec:autoRestartDelaySec-Presentation Property*/


	public class AutoRestartDelaySec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AutoRestartDelaySec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoRestartDelaySec:autoRestartDelaySec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoRestartDelaySec:autoRestartDelaySec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AutoRestartDelaySec getAutoRestartDelaySec();
	/*Radix::System::Instance:classLoadingProfileId:classLoadingProfileId-Presentation Property*/


	public class ClassLoadingProfileId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ClassLoadingProfileId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfileId:classLoadingProfileId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfileId:classLoadingProfileId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ClassLoadingProfileId getClassLoadingProfileId();
	/*Radix::System::Instance:httpsProxy:httpsProxy-Presentation Property*/


	public class HttpsProxy extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HttpsProxy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpsProxy:httpsProxy")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpsProxy:httpsProxy")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HttpsProxy getHttpsProxy();
	/*Radix::System::Instance:startOSCommand:startOSCommand-Presentation Property*/


	public class StartOSCommand extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StartOSCommand(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startOSCommand:startOSCommand")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startOSCommand:startOSCommand")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StartOSCommand getStartOSCommand();
	/*Radix::System::Instance:aadcDgAddress:aadcDgAddress-Presentation Property*/


	public class AadcDgAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AadcDgAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcDgAddress:aadcDgAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcDgAddress:aadcDgAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AadcDgAddress getAadcDgAddress();
	/*Radix::System::Instance:maxActiveArteVeryHigh:maxActiveArteVeryHigh-Presentation Property*/


	public class MaxActiveArteVeryHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteVeryHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteVeryHigh:maxActiveArteVeryHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteVeryHigh:maxActiveArteVeryHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteVeryHigh getMaxActiveArteVeryHigh();
	/*Radix::System::Instance:aadcMemberId:aadcMemberId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::System::Instance:arteInstLiveTime:arteInstLiveTime-Presentation Property*/


	public class ArteInstLiveTime extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteInstLiveTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstLiveTime:arteInstLiveTime")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstLiveTime:arteInstLiveTime")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteInstLiveTime getArteInstLiveTime();
	/*Radix::System::Instance:oraImplStmtCacheSize:oraImplStmtCacheSize-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize();
	/*Radix::System::Instance:appVersion:appVersion-Presentation Property*/


	public class AppVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AppVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:appVersion:appVersion")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:appVersion:appVersion")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AppVersion getAppVersion();
	/*Radix::System::Instance:stopOSCommand:stopOSCommand-Presentation Property*/


	public class StopOSCommand extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StopOSCommand(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopOSCommand:stopOSCommand")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopOSCommand:stopOSCommand")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StopOSCommand getStopOSCommand();
	/*Radix::System::Instance:keyTabFilePath:keyTabFilePath-Presentation Property*/


	public class KeyTabFilePath extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KeyTabFilePath(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyTabFilePath:keyTabFilePath")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyTabFilePath:keyTabFilePath")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KeyTabFilePath getKeyTabFilePath();
	/*Radix::System::Instance:dbTraceProfile:dbTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::System::Instance:traceFilesDir:traceFilesDir-Presentation Property*/


	public class TraceFilesDir extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceFilesDir(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:traceFilesDir:traceFilesDir")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:traceFilesDir:traceFilesDir")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceFilesDir getTraceFilesDir();
	/*Radix::System::Instance:arteCntVeryHigh:arteCntVeryHigh-Presentation Property*/


	public class ArteCntVeryHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntVeryHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntVeryHigh:arteCntVeryHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntVeryHigh:arteCntVeryHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntVeryHigh getArteCntVeryHigh();
	/*Radix::System::Instance:cpuCoreCount:cpuCoreCount-Presentation Property*/


	public class CpuCoreCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CpuCoreCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:cpuCoreCount:cpuCoreCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:cpuCoreCount:cpuCoreCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuCoreCount getCpuCoreCount();
	/*Radix::System::Instance:autoActualizeVer:autoActualizeVer-Presentation Property*/


	public class AutoActualizeVer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AutoActualizeVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoActualizeVer:autoActualizeVer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoActualizeVer:autoActualizeVer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AutoActualizeVer getAutoActualizeVer();
	/*Radix::System::Instance:effectiveSelfCheckTime:effectiveSelfCheckTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public EffectiveSelfCheckTime getEffectiveSelfCheckTime();
	/*Radix::System::Instance:isAadcSysMember:isAadcSysMember-Presentation Property*/


	public class IsAadcSysMember extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsAadcSysMember(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:isAadcSysMember:isAadcSysMember")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:isAadcSysMember:isAadcSysMember")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsAadcSysMember getIsAadcSysMember();
	/*Radix::System::Instance:canEditActualizeVer:canEditActualizeVer-Presentation Property*/


	public class CanEditActualizeVer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanEditActualizeVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:canEditActualizeVer:canEditActualizeVer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:canEditActualizeVer:canEditActualizeVer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanEditActualizeVer getCanEditActualizeVer();
	/*Radix::System::Instance:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::System::Instance:sapPropsXml:sapPropsXml-Presentation Property*/


	public class SapPropsXml extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SapPropsXml(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapPropsXml:sapPropsXml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapPropsXml:sapPropsXml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SapPropsXml getSapPropsXml();
	/*Radix::System::Instance:warning:warning-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:warning:warning")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:warning:warning")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Warning getWarning();
	/*Radix::System::Instance:activityStatus:activityStatus-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:activityStatus:activityStatus")
		public  org.radixware.ads.System.common.ActivityStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:activityStatus:activityStatus")
		public   void setValue(org.radixware.ads.System.common.ActivityStatus val) {
			Value = val;
		}
	}
	public ActivityStatus getActivityStatus();
	/*Radix::System::Instance:hostIpAddresses:hostIpAddresses-Presentation Property*/


	public class HostIpAddresses extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public HostIpAddresses(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddresses:hostIpAddresses")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddresses:hostIpAddresses")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public HostIpAddresses getHostIpAddresses();
	/*Radix::System::Instance:selfCheckTimeStr:selfCheckTimeStr-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeStr:selfCheckTimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeStr:selfCheckTimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SelfCheckTimeStr getSelfCheckTimeStr();
	/*Radix::System::Instance:keyAliases:keyAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	/*Radix::System::Instance:uptimeStr:uptimeStr-Presentation Property*/


	public class UptimeStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UptimeStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:uptimeStr:uptimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:uptimeStr:uptimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UptimeStr getUptimeStr();
	/*Radix::System::Instance:sensTraceFinishTime:sensTraceFinishTime-Presentation Property*/


	public class SensTraceFinishTime extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SensTraceFinishTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sensTraceFinishTime:sensTraceFinishTime")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sensTraceFinishTime:sensTraceFinishTime")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SensTraceFinishTime getSensTraceFinishTime();
	public static class PerformMaintenance extends org.radixware.kernel.common.client.models.items.Command{
		protected PerformMaintenance(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs.class);
		}

	}

	public static class ActualizeVer extends org.radixware.kernel.common.client.models.items.Command{
		protected ActualizeVer(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class FillArteTable extends org.radixware.kernel.common.client.models.items.Command{
		protected FillArteTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.FillArteTableRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.FillArteTableRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.FillArteTableRs.class);
		}

	}

	public static class ReloadArtePool extends org.radixware.kernel.common.client.models.items.Command{
		protected ReloadArtePool(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs.class);
		}

	}

	public static class StopAllUnitsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected StopAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs.class);
		}

	}

	public static class ShowUsedPortsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected ShowUsedPortsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class);
		}

	}

	public static class CaptureClassLoadingProfile extends org.radixware.kernel.common.client.models.items.Command{
		protected CaptureClassLoadingProfile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ApplyOptionsFromFile extends org.radixware.kernel.common.client.models.items.Command{
		protected ApplyOptionsFromFile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class StartAllUnitsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected StartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs.class);
		}

	}

	public static class GetInstanceStateReport extends org.radixware.kernel.common.client.models.items.Command{
		protected GetInstanceStateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class EditKeystoreAliasesCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected EditKeystoreAliasesCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}

	public static class RestartAllUnitsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected RestartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs.class);
		}

	}



}

/* Radix::System::Instance - Desktop Meta*/

/*Radix::System::Instance-Entity Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Instance_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Instance:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
			"Radix::System::Instance",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgPKOWNB4VQWDQHK3PBAZSKPGSN3ZMUVLN"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYDG2SQYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),16384,

			/*Radix::System::Instance:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Instance:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXSBA5QYSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Instance:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYWM27QYSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Instance:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:startOSCommand:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO6KNHKT4RHWDBRCXAAIT4AGD7E"),
						"startOSCommand",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFVMK2IZSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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

						/*Radix::System::Instance:startOSCommand:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:stopOSCommand:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colROO4LOT4RHWDBRCXAAIT4AGD7E"),
						"stopOSCommand",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVMK2IZSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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

						/*Radix::System::Instance:stopOSCommand:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdULKYARIUXBFI5MPQNEWNKM6QKI"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYJOSHPDUNFY5HUFRZHZQ3KCRE"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL3UHR26LYRDOPNHTBAQ2MDLTCM"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSS7F4MOTAFEPZJMUNXPPAIYNII"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:sapPropsXml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMUOPZ76SOVFJBNZVEXOLVE2TSA"),
						"sapPropsXml",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:sapPropsXml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:warning:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP3N5ITE3DFCU7HDUIKM2MCWIIU"),
						"warning",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:warning:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntAboveNormal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7TINTWQVARGBRPLD2OLCBC2PJI"),
						"arteCntAboveNormal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM55QIJUMBEBPFEWUKCEGCGCFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntAboveNormal:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJDC3VPAKRD4JFTP3XIEEJP5NQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntCritical:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AFWSTW7EJAHDCWFP4FXF57W5M"),
						"arteCntCritical",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLDAJAN2OVDI5IQGJNUKQKAOKU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntCritical:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5UG737NJK5ACFCZW5RCB7QRBIE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM522N4URWVD3BAYUQ4DBXFV6XA"),
						"arteCntHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JFGOBLVGZG67EIKZ2P6ZLLEAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XEAZYUJEREOVLFAKZMJJAVQKE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntVeryHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY4ZKX4SUCBAHDLYFDL64EZSY6M"),
						"arteCntVeryHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUHMGQLCEVAF5HQKVW64XU27SU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntVeryHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPUL6YTXZJAZROVXEKN375S7DE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBCHU436MFFEPLQPW6H6QGHNOE"),
						"arteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X2FHQRQPFARBMC66VYNPSS3CQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:arteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteInstLiveTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPVFIBHTN5NA35FF6RJCZIM6SAY"),
						"arteInstLiveTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB7JIIV5BLJBD7KP6SJHU4K3JZQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteInstLiveTime:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOL3OKSUKOBD3VCAFHPCAIR7BCM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:useActiveArteLimits:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ABN5MPEFBGRNERT3MSWLBFILE"),
						"useActiveArteLimits",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDLLCNOLAFEWVD6LNG647RVZUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:useActiveArteLimits:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:lowArteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z2JD6POZNCZJLGNTEGIJHZEQQ"),
						"lowArteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHAGMYJJ6VRDGTGZRG46SEWDD5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:lowArteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:highArteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27DHEMV4S5FAVBSPLT6B6R4E6M"),
						"highArteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SOMLSZPBRFQVEJEZNNHALADRI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:highArteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:started:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),
						"started",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTANRE6IIJB7BHDKLB6PTCNX24"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Instance:started:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxTraceFileCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPBWOB42OZCATJ6INHY24PAYLI"),
						"maxTraceFileCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMPOSUEDVBCCJE7LSWHJFCDJIU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:maxTraceFileCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,9999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxTraceFileSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTN2GUM43VD5TH34XEED3NBSWM"),
						"maxTraceFileSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYWANGGUTVRGFDJDM3KVGMVDOYU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1024"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:maxTraceFileSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:rotateTraceFilesDaily:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCHSEV5KJGVACBL2YSMNARJCWKI"),
						"rotateTraceFilesDaily",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAMF3NHHVJVHFBKCB3EEBP3R6M4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:rotateTraceFilesDaily:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:traceFilesDir:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXGRPSYS6JVGHRMXMH2FEUSJMQU"),
						"traceFilesDir",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGEPL4H3H3VAXHAON6PV36CML7A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("./logs"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:traceFilesDir:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:guiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col24KG33MBMRAATA7S5VFV34PLTE"),
						"guiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27UGHLZD5NG7RKVDX27XMCOSNE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:guiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:fileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF666DZAJ3NF3BOLDRS6BDK75T4"),
						"fileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTFVLVJQRDHXIB6MDYFL4SZHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:fileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUJAMRNBTMVE2BHL3BKCXMXSNMI"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOR2TT376VGZDJ4MYS2RKRMTTY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:autoActualizeVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDM5FZM6CNAAVLLCHIQB4F6WL4"),
						"autoActualizeVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBNN2FUK7BCNXHK73KJMKPOOZA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:autoActualizeVer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:httpProxy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5B6RPEAWRBJVMM2OVLX3MSGKY"),
						"httpProxy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7KIBZJJXZHOZCCFGKTNZLGWG4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:httpProxy:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:httpsProxy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMQKXC2RA7BFJTI5YEECPN2JU2I"),
						"httpsProxy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXWXBOPA5ZFPBPHTKQCRAGQU2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:httpsProxy:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyStorePath:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDAQ557DC6BAZRI6VNI3WDLB5HQ"),
						"keyStorePath",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34SDG7OPZEQXPFKS6TEPIVIXE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("radixware.org/server/keys.jceks"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:keyStorePath:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,256,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyStoreType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YSQY2R375FKFKYACAY762WGD4"),
						"keyStoreType",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDVBTHYGRRCIPNG3SA3GT3KGJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFOUQICKMYJAGDL356HLXUNICKE"),
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

						/*Radix::System::Instance:keyStoreType:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFOUQICKMYJAGDL356HLXUNICKE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyTabFilePath:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTLHWR7V5OJE53K6G4EJDPJY2XY"),
						"keyTabFilePath",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOADIICLERFHS3LN5XEPACZLRSU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:keyTabFilePath:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,256,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZN7D6K4DGJAMTOAZJRNW22QVMU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:oraImplStmtCacheSize:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"),
						"oraImplStmtCacheSize",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls35EJSP3CJ5AMFNQRAIVDT54SH4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::System::Instance:oraImplStmtCacheSize:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:useOraImplStmtCache:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD3UDCXDDAJDR7B3YEBXV3TBNAU"),
						"useOraImplStmtCache",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XVB54JPJJD5FKJXAA4DHG74QU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::System::Instance:useOraImplStmtCache:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA333YBDFB5EMVI6N4N6IUKFG3M"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURH44I562NG25AKUUKACQEIYYY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:scp:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"),
						"scp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOXLRPPSZ75GJZKE4LROFLCCUNY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLCOZUPH3GNA43BCNANGFGGW62Y"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWDOEJDIMZPNRDB64AALOMT5GDM")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDRGOFLVET7NRDB56AALOMT5GDM"),
						0,
						0,false),

					/*Radix::System::Instance:avgActiveArteCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5F32BDXFU5GLJDKLCF5OHFYQDI"),
						"avgActiveArteCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWF7HHSS2RVEHJL64Y6DH2QA3FY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.NUM,
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

						/*Radix::System::Instance:avgActiveArteCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:memoryCheckPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colETPNDLR7SVE4JEEF664PVZPKN4"),
						"memoryCheckPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYFPIIMFMBD5BIS5LSEHENYCMY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHMAAYM53MNA5TD56KGA6NZ7VY4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:memoryCheckPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSLL5K7DA5BAXEVOXDNZGDOWB4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:targetExecutor:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4KPRPAXBXRCYBBGYUXJNALPQQQ"),
						"targetExecutor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTS4SNFXWUJAUXPPLMGBBEDKRDY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::System::Instance:targetExecutorId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFNUWBLQL3RCATBI3O5KPEXF7A4"),
						"targetExecutorId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:targetExecutorId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteNormal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"),
						"maxActiveArteNormal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO65EAYZJ5A2JFS2235KXKK3L4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteNormal:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBYQFI2SW7VE6RCLJFOD6GZHT2Y"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteAboveNormal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"),
						"maxActiveArteAboveNormal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW67PPWIVA5EKXCR6T74XIHB7TU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteAboveNormal:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGIPBF54YVNBC3OXTULIWX3TIF4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"),
						"maxActiveArteHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWTXZ7RTBGHXLLCSOMNZGR6DM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQNJRHADXRVFZLCFBYSEEXIA7XU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteVeryHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"),
						"maxActiveArteVeryHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5B443SCPYZAS3FHDTQ4NLF2BJQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteVeryHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7M63WS3URHJHL2M3SBHGIUORM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteCritical:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"),
						"maxActiveArteCritical",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6POZVFOIZEQXHCVG7T576S66Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteCritical:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBTO6VHTRNGGTGKW5KJCO6D2GA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:sensTraceFinishTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVRAIUGRMHVFLZJ4VCWCCPZTQPM"),
						"sensTraceFinishTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRRZW4W3JNDXXJD4IE5BVMHMDA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:sensTraceFinishTime:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:classLoadingProfileId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJMKGEQX2FG53KHYXUTIJMMKYE"),
						"classLoadingProfileId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:classLoadingProfileId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:classLoadingProfile:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UQLYTWVCJEUDHVXJFXYGP7KN4"),
						"classLoadingProfile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecS6IN3XOILNANJG3BCB7NTDDMDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblS6IN3XOILNANJG3BCB7NTDDMDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHGKCI7PIZRAK3BTNOQ75K2MUJA")},
						null,
						null,
						3668987,
						3669995,false),

					/*Radix::System::Instance:sysdate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7COJCFXLD5BU3HOJD4DMPNJF3E"),
						"sysdate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:sysdate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),
						"selfCheckTimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJJZGBX3IRHAVP7DECEU3YFFTM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:selfCheckTimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:activityStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),
						"activityStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPQARQYQ2VDLXMHQJ77MELKG2E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:activityStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:aadcDgAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOF6APP4I7FHV7BCQLHO75CZR74"),
						"aadcDgAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVF5JGZRMVCV7DSKPLTXKAF3UM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:aadcDgAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN4LZUFJOJCNRNCO5N43FTGAAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,2L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:isAadcSysMember:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFJL567ZQ4NDZTPCDLNPJFKIUCU"),
						"isAadcSysMember",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:isAadcSysMember:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:autoRestartDelaySec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM65QF5VLNZD6FPDDMADFENBBUU"),
						"autoRestartDelaySec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRWVUWS7HVC3VH2MIWKNQCQ4BA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:autoRestartDelaySec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:osPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col55KRIWQ6TVBX3CIF4UY25YTMZE"),
						"osPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZCSYUK7ZFGBTLENATKOZ2DWLU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:osPid:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:appVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYGFOEBBHJEPHGO5B2Y4E6QUBA"),
						"appVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGPVMIPUDSRCRJN56RQYHGU2V4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:appVersion:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:kernelVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFPV5MEWHSNFFRGQN4V5CEMIXXQ"),
						"kernelVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7CYYIAQBBETJI3JU3VR3IVSWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:kernelVersion:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:uptimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVITFLAYDGNHBZOXAQIT3GUPQ2Q"),
						"uptimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXJO7WUUNDZJIRMO45U2FOLK4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:uptimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:dbCurMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWOPG2OAWFD2PIM5SAH75DXSPI"),
						"dbCurMillis",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:dbCurMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:revision:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3YVJTL7CFCALHOXPVVDPZID7Q"),
						"revision",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWMZC6XE3NDBJH22DNJJ3DEI7U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:revision:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:canEditActualizeVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),
						"canEditActualizeVer",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:canEditActualizeVer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:effectiveSelfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5RRJ2XRTYZCRJOOTZPSFGIVIMM"),
						"effectiveSelfCheckTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:effectiveSelfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:instStateForcedGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIKMV3NWRZCDXHXWRERFBXVFT4"),
						"instStateForcedGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVL23IIZ34FCN5MG6J7OWSKL6WI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:instStateForcedGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,300L,(byte)-1,null,10L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWFIABAFRJH7BMAPTRJZS4PTFY"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:instStateGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2X4UBSDEPBBG5LYPLRDF6YSOIU"),
						"instStateGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMKMU66PPJG3NFVQH3BUH5PI2A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:instStateGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,120L,(byte)-1,null,5L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVOUNSTMCSFEUZC7AB37CYIUJHU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:instStateHistoryStorePeriodDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGHZ3KYMJURDDBHJ3IJXSUY35LA"),
						"instStateHistoryStorePeriodDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCFTW5WON5GDLCZLPYBKSFTLIA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::System::Instance:instStateHistoryStorePeriodDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,90L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:cpuCoreCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZAGLT4KO6VC45P3ANFRXEJEFRA"),
						"cpuCoreCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFXD5D3ULZD35AXQTECYBRT55U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:cpuCoreCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:hostName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Q5VAISXVREI3K3APBJWYJWQYQ"),
						"hostName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLB2GYJPR2NAGBEZPQO5DUZAGGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:hostName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,255,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:hostIpAddresses:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTUDXYLL6HVEIZORAMXTOVJJSHA"),
						"hostIpAddresses",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIKLMMDE7NEQJHHKJFGV2MKID4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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

						/*Radix::System::Instance:hostIpAddresses:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:startAllUnitsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNXCBARNCSLOBDCKTAALOMT5GDM"),
						"startAllUnitsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZAPYPPJ7FGURIGIRMD7SHI65E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYTQLIJ3AFIXRTYR3VKVAGGLM346GSPPC"),
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
					/*Radix::System::Instance:stopAllUnitsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMWWCWA3SHOBDCKRAALOMT5GDM"),
						"stopAllUnitsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QEANBWVS5H37JCHEJDTPW3ZMQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img7JYIJZYBESRV5BYGLERHDLWECGFYG6CC"),
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
					/*Radix::System::Instance:restartAllUnitsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdXG6DDFFSQPOBDCKCAALOMT5GDM"),
						"restartAllUnitsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDFTYV2MPJCKTHRZ2GLJQJIVZY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgFCS5AS7TDYYOG6KX3TO6HLJDGJK4JZD7"),
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
					/*Radix::System::Instance:editKeystoreAliasesCmd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUJAYTWUX35CGFJTSTUMQNAB6EY"),
						"editKeystoreAliasesCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZM3X7J43VHCVPQDM7UPYTON4M"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAO4GDPUN4VCSJPJ57OZ5W7S7RA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEH3ABYCN6JC33KQ24QKS2LTF24"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAGDAVFAP5RB4ZEJMLLJZYD3XM4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7UOXOTN33FDA3OWKL4CAPPYDJQ")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:showUsedPortsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIVHWGK3YW5CGFAX3SASK74K5ZI"),
						"showUsedPortsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIWKI3VDRFVC3ZA3QNT5QXTHQFM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgAR26WN32ABFQVNKGDBYBZGE6E4"),
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
					/*Radix::System::Instance:applyOptionsFromFile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMKKHVZCNBVC2PCLYYDG2PXJQ2I"),
						"applyOptionsFromFile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU3DNZJVNBC3BG4IPM3NGZNVZU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhH7V4LFQZ5ZGSJFCISDGOYFJCNU"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:actualizeVer-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5VYLPC23CBCP3JAZLZAX7GZJOU"),
						"actualizeVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3TSRPYMCRF3LJ5EIGJFO5IR5U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3FU3DWZ2QJEMJNH6W3QEM2RVIA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:reloadArtePool-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdD37DTAK4AFDZ3ISCFNXNREG2HY"),
						"reloadArtePool",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJRG2FWCSFHTLA4HCHCZR7S4B4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQQDSD3LDVRB37ON5QRIBTFTAQQ"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:performMaintenance-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5TAASNN6RNFOBLLWUQIK3S6WN4"),
						"performMaintenance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZQJ47IOWZEAFNOR35NW7IDSWY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgF7F5VDLGVZC3XHP7IWBS7B4H7Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:getInstanceStateReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOCIRXO2BUZET3HKCEEN22NL7EI"),
						"getInstanceStateReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRWB3W47ORCORNWE5LJX3Y225U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYDNPPCMGSBBPJOQKFNPS7TGQ3E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhSZZQMAM5WZFIJD5ZI5NPJK4XIA"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:fillArteTable-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdB3BDOSBJHVFDVCOMB34ZKXWFRM"),
						"fillArteTable",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:captureClassLoadingProfile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLYBOIRRR3JALTG5PGK7PURRMNE"),
						"captureClassLoadingProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYHX4RXAEFCL3HUKN23XPCGOJY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWEQPJUYLXCAGMHF4KKUK6VWQPOORINWN"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhBLMVWQ2JQNDETEZTIWEG2JQEVQ"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Instance:Started First-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQBCIUYAERTNRDB5PAALOMT5GDM"),
						"Started First",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJWK3KXQYSHOBDCKRAALOMT5GDM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Instance:By ID-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYLZEGABQFCE7HXEA5CZC6IOQ4"),
						"By ID",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWY6H2WEAJDPLIEEBKGIVIXNRY"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refKGDSAFFBXZGTJMYA6W5QP3RUWQ"),"Instance=>ClassLoadingProfile (classLoadingProfileId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblS6IN3XOILNANJG3BCB7NTDDMDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJMKGEQX2FG53KHYXUTIJMMKYE")},new String[]{"classLoadingProfileId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colUQJF45R5AJD3VJDJ4LI5OMHDVY")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPT7FJIZP3JCIROWGR2UA2XGL3Y"),"Instance=>Unit (targetExecutorId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFNUWBLQL3RCATBI3O5KPEXF7A4")},new String[]{"targetExecutorId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refX5LLPHQYPBGLPJJHOZJ5RGEVUU"),"Instance=>Scp (scpName=>name, systemId=>systemId)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colYSPDMMOVEBGPZIMQX2RBMXCBQE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNPEGS5CTHRHRDF7ZU3IPI7GELU")},new String[]{"scpName","systemId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colI4QSJ5PEHJWDRPOYAAYQQ2Y3GB"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUQ25PISLDNBDJA6ACQMTAIZT4")},new String[]{"name","systemId"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refXTQO52VDLLOBDCJDAALOMT5GDM"),"Instance=>Sap (sapId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblR7FXMYDVVHWDBROXAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colA333YBDFB5EMVI6N4N6IUKFG3M")},new String[]{"sapId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ZDYU3V3RHWDBRCXAAIT4AGD7E")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM")},
			false,true,false);
}

/* Radix::System::Instance - Web Executable*/

/*Radix::System::Instance-Entity Class*/

package org.radixware.ads.System.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance")
public interface Instance {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}





		public org.radixware.ads.System.web.Instance.Instance_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.System.web.Instance.Instance_DefaultModel )  super.getEntity(i);}
	}
















































































































































































































































































































































































































	/*Radix::System::Instance:guiTraceProfile:guiTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:guiTraceProfile:guiTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:guiTraceProfile:guiTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public GuiTraceProfile getGuiTraceProfile();
	/*Radix::System::Instance:highArteInstCount:highArteInstCount-Presentation Property*/


	public class HighArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public HighArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:highArteInstCount:highArteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:highArteInstCount:highArteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public HighArteInstCount getHighArteInstCount();
	/*Radix::System::Instance:useActiveArteLimits:useActiveArteLimits-Presentation Property*/


	public class UseActiveArteLimits extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public UseActiveArteLimits(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useActiveArteLimits:useActiveArteLimits")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useActiveArteLimits:useActiveArteLimits")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseActiveArteLimits getUseActiveArteLimits();
	/*Radix::System::Instance:instStateGatherPeriodSec:instStateGatherPeriodSec-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateGatherPeriodSec:instStateGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateGatherPeriodSec getInstStateGatherPeriodSec();
	/*Radix::System::Instance:arteCntCritical:arteCntCritical-Presentation Property*/


	public class ArteCntCritical extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntCritical(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntCritical:arteCntCritical")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntCritical:arteCntCritical")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntCritical getArteCntCritical();
	/*Radix::System::Instance:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::System::Instance:keyStoreType:keyStoreType-Presentation Property*/


	public class KeyStoreType extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public KeyStoreType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EKeyStoreType dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EKeyStoreType ? (org.radixware.kernel.common.enums.EKeyStoreType)x : org.radixware.kernel.common.enums.EKeyStoreType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EKeyStoreType> getValClass(){
			return org.radixware.kernel.common.enums.EKeyStoreType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EKeyStoreType dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EKeyStoreType ? (org.radixware.kernel.common.enums.EKeyStoreType)x : org.radixware.kernel.common.enums.EKeyStoreType.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStoreType:keyStoreType")
		public  org.radixware.kernel.common.enums.EKeyStoreType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStoreType:keyStoreType")
		public   void setValue(org.radixware.kernel.common.enums.EKeyStoreType val) {
			Value = val;
		}
	}
	public KeyStoreType getKeyStoreType();
	/*Radix::System::Instance:maxActiveArteNormal:maxActiveArteNormal-Presentation Property*/


	public class MaxActiveArteNormal extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteNormal:maxActiveArteNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteNormal:maxActiveArteNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteNormal getMaxActiveArteNormal();
	/*Radix::System::Instance:targetExecutor:targetExecutor-Presentation Property*/


	public class TargetExecutor extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public TargetExecutor(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutor:targetExecutor")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutor:targetExecutor")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public TargetExecutor getTargetExecutor();
	/*Radix::System::Instance:lowArteInstCount:lowArteInstCount-Presentation Property*/


	public class LowArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public LowArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:lowArteInstCount:lowArteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:lowArteInstCount:lowArteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public LowArteInstCount getLowArteInstCount();
	/*Radix::System::Instance:osPid:osPid-Presentation Property*/


	public class OsPid extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public OsPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:osPid:osPid")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:osPid:osPid")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OsPid getOsPid();
	/*Radix::System::Instance:avgActiveArteCount:avgActiveArteCount-Presentation Property*/


	public class AvgActiveArteCount extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public AvgActiveArteCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Num dummy = ((Num)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:avgActiveArteCount:avgActiveArteCount")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:avgActiveArteCount:avgActiveArteCount")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public AvgActiveArteCount getAvgActiveArteCount();
	/*Radix::System::Instance:maxActiveArteCritical:maxActiveArteCritical-Presentation Property*/


	public class MaxActiveArteCritical extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteCritical(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteCritical:maxActiveArteCritical")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteCritical:maxActiveArteCritical")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteCritical getMaxActiveArteCritical();
	/*Radix::System::Instance:hostName:hostName-Presentation Property*/


	public class HostName extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HostName(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostName:hostName")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostName:hostName")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HostName getHostName();
	/*Radix::System::Instance:sysdate:sysdate-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sysdate:sysdate")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sysdate:sysdate")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public Sysdate getSysdate();
	/*Radix::System::Instance:arteCntAboveNormal:arteCntAboveNormal-Presentation Property*/


	public class ArteCntAboveNormal extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntAboveNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntAboveNormal:arteCntAboveNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntAboveNormal:arteCntAboveNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntAboveNormal getArteCntAboveNormal();
	/*Radix::System::Instance:classLoadingProfile:classLoadingProfile-Presentation Property*/


	public class ClassLoadingProfile extends org.radixware.kernel.common.client.models.items.properties.PropertyRef{
		public ClassLoadingProfile(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadParentRefPropertyDef def){
			super(model,def);
		}
		@Override
		public 
		org.radixware.ads.System.web.ClassLoadingProfile.ClassLoadingProfile_DefaultModel openEntityModel()  throws org.radixware.kernel.common.exceptions.ServiceClientException, InterruptedException{
		     return (org.radixware.ads.System.web.ClassLoadingProfile.ClassLoadingProfile_DefaultModel)super.openEntityModel();
		}
		@Override
		public 
		org.radixware.ads.System.web.ClassLoadingProfile.DefaultGroupModel openGroupModel(){
		     return (org.radixware.ads.System.web.ClassLoadingProfile.DefaultGroupModel)super.openGroupModel();
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.client.types.Reference dummy = ((org.radixware.kernel.common.client.types.Reference)x);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfile:classLoadingProfile")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfile:classLoadingProfile")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public ClassLoadingProfile getClassLoadingProfile();
	/*Radix::System::Instance:sapId:sapId-Presentation Property*/


	public class SapId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public SapId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapId:sapId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapId:sapId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public SapId getSapId();
	/*Radix::System::Instance:httpProxy:httpProxy-Presentation Property*/


	public class HttpProxy extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HttpProxy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpProxy:httpProxy")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpProxy:httpProxy")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HttpProxy getHttpProxy();
	/*Radix::System::Instance:rotateTraceFilesDaily:rotateTraceFilesDaily-Presentation Property*/


	public class RotateTraceFilesDaily extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public RotateTraceFilesDaily(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:rotateTraceFilesDaily:rotateTraceFilesDaily")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:rotateTraceFilesDaily:rotateTraceFilesDaily")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public RotateTraceFilesDaily getRotateTraceFilesDaily();
	/*Radix::System::Instance:useOraImplStmtCache:useOraImplStmtCache-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useOraImplStmtCache:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:useOraImplStmtCache:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache();
	/*Radix::System::Instance:keyStorePath:keyStorePath-Presentation Property*/


	public class KeyStorePath extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KeyStorePath(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStorePath:keyStorePath")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyStorePath:keyStorePath")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KeyStorePath getKeyStorePath();
	/*Radix::System::Instance:arteInstCount:arteInstCount-Presentation Property*/


	public class ArteInstCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteInstCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstCount:arteInstCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstCount:arteInstCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteInstCount getArteInstCount();
	/*Radix::System::Instance:scp:scp-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scp:scp")
		public  org.radixware.kernel.common.client.types.Reference getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:scp:scp")
		public   void setValue(org.radixware.kernel.common.client.types.Reference val) {
			Value = val;
		}
	}
	public Scp getScp();
	/*Radix::System::Instance:memoryCheckPeriodSec:memoryCheckPeriodSec-Presentation Property*/


	public class MemoryCheckPeriodSec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MemoryCheckPeriodSec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:memoryCheckPeriodSec:memoryCheckPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:memoryCheckPeriodSec:memoryCheckPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MemoryCheckPeriodSec getMemoryCheckPeriodSec();
	/*Radix::System::Instance:fileTraceProfile:fileTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fileTraceProfile:fileTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:fileTraceProfile:fileTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public FileTraceProfile getFileTraceProfile();
	/*Radix::System::Instance:started:started-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:started:started")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:started:started")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Started getStarted();
	/*Radix::System::Instance:targetExecutorId:targetExecutorId-Presentation Property*/


	public class TargetExecutorId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TargetExecutorId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutorId:targetExecutorId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:targetExecutorId:targetExecutorId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TargetExecutorId getTargetExecutorId();
	/*Radix::System::Instance:kernelVersion:kernelVersion-Presentation Property*/


	public class KernelVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KernelVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:kernelVersion:kernelVersion")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:kernelVersion:kernelVersion")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KernelVersion getKernelVersion();
	/*Radix::System::Instance:maxActiveArteHigh:maxActiveArteHigh-Presentation Property*/


	public class MaxActiveArteHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteHigh:maxActiveArteHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteHigh:maxActiveArteHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteHigh getMaxActiveArteHigh();
	/*Radix::System::Instance:dbCurMillis:dbCurMillis-Presentation Property*/


	public class DbCurMillis extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public DbCurMillis(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbCurMillis:dbCurMillis")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbCurMillis:dbCurMillis")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public DbCurMillis getDbCurMillis();
	/*Radix::System::Instance:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::System::Instance:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateHistoryStorePeriodDays:instStateHistoryStorePeriodDays")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateHistoryStorePeriodDays getInstStateHistoryStorePeriodDays();
	/*Radix::System::Instance:maxTraceFileCnt:maxTraceFileCnt-Presentation Property*/


	public class MaxTraceFileCnt extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxTraceFileCnt(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileCnt:maxTraceFileCnt")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileCnt:maxTraceFileCnt")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxTraceFileCnt getMaxTraceFileCnt();
	/*Radix::System::Instance:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:instStateForcedGatherPeriodSec:instStateForcedGatherPeriodSec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstStateForcedGatherPeriodSec getInstStateForcedGatherPeriodSec();
	/*Radix::System::Instance:maxTraceFileSizeKb:maxTraceFileSizeKb-Presentation Property*/


	public class MaxTraceFileSizeKb extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxTraceFileSizeKb(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileSizeKb:maxTraceFileSizeKb")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxTraceFileSizeKb:maxTraceFileSizeKb")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxTraceFileSizeKb getMaxTraceFileSizeKb();
	/*Radix::System::Instance:revision:revision-Presentation Property*/


	public class Revision extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Revision(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:revision:revision")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:revision:revision")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Revision getRevision();
	/*Radix::System::Instance:maxActiveArteAboveNormal:maxActiveArteAboveNormal-Presentation Property*/


	public class MaxActiveArteAboveNormal extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteAboveNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteAboveNormal:maxActiveArteAboveNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteAboveNormal:maxActiveArteAboveNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteAboveNormal getMaxActiveArteAboveNormal();
	/*Radix::System::Instance:arteCntHigh:arteCntHigh-Presentation Property*/


	public class ArteCntHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntHigh:arteCntHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntHigh:arteCntHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntHigh getArteCntHigh();
	/*Radix::System::Instance:autoRestartDelaySec:autoRestartDelaySec-Presentation Property*/


	public class AutoRestartDelaySec extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public AutoRestartDelaySec(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoRestartDelaySec:autoRestartDelaySec")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoRestartDelaySec:autoRestartDelaySec")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AutoRestartDelaySec getAutoRestartDelaySec();
	/*Radix::System::Instance:classLoadingProfileId:classLoadingProfileId-Presentation Property*/


	public class ClassLoadingProfileId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ClassLoadingProfileId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfileId:classLoadingProfileId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:classLoadingProfileId:classLoadingProfileId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ClassLoadingProfileId getClassLoadingProfileId();
	/*Radix::System::Instance:httpsProxy:httpsProxy-Presentation Property*/


	public class HttpsProxy extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public HttpsProxy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpsProxy:httpsProxy")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:httpsProxy:httpsProxy")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public HttpsProxy getHttpsProxy();
	/*Radix::System::Instance:startOSCommand:startOSCommand-Presentation Property*/


	public class StartOSCommand extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StartOSCommand(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startOSCommand:startOSCommand")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:startOSCommand:startOSCommand")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StartOSCommand getStartOSCommand();
	/*Radix::System::Instance:aadcDgAddress:aadcDgAddress-Presentation Property*/


	public class AadcDgAddress extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AadcDgAddress(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcDgAddress:aadcDgAddress")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcDgAddress:aadcDgAddress")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AadcDgAddress getAadcDgAddress();
	/*Radix::System::Instance:maxActiveArteVeryHigh:maxActiveArteVeryHigh-Presentation Property*/


	public class MaxActiveArteVeryHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MaxActiveArteVeryHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteVeryHigh:maxActiveArteVeryHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:maxActiveArteVeryHigh:maxActiveArteVeryHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteVeryHigh getMaxActiveArteVeryHigh();
	/*Radix::System::Instance:aadcMemberId:aadcMemberId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMemberId:aadcMemberId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:aadcMemberId:aadcMemberId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public AadcMemberId getAadcMemberId();
	/*Radix::System::Instance:arteInstLiveTime:arteInstLiveTime-Presentation Property*/


	public class ArteInstLiveTime extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteInstLiveTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstLiveTime:arteInstLiveTime")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteInstLiveTime:arteInstLiveTime")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteInstLiveTime getArteInstLiveTime();
	/*Radix::System::Instance:oraImplStmtCacheSize:oraImplStmtCacheSize-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:oraImplStmtCacheSize:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize();
	/*Radix::System::Instance:appVersion:appVersion-Presentation Property*/


	public class AppVersion extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public AppVersion(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:appVersion:appVersion")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:appVersion:appVersion")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public AppVersion getAppVersion();
	/*Radix::System::Instance:stopOSCommand:stopOSCommand-Presentation Property*/


	public class StopOSCommand extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public StopOSCommand(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopOSCommand:stopOSCommand")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:stopOSCommand:stopOSCommand")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public StopOSCommand getStopOSCommand();
	/*Radix::System::Instance:keyTabFilePath:keyTabFilePath-Presentation Property*/


	public class KeyTabFilePath extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KeyTabFilePath(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyTabFilePath:keyTabFilePath")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyTabFilePath:keyTabFilePath")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KeyTabFilePath getKeyTabFilePath();
	/*Radix::System::Instance:dbTraceProfile:dbTraceProfile-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbTraceProfile:dbTraceProfile")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:dbTraceProfile:dbTraceProfile")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public DbTraceProfile getDbTraceProfile();
	/*Radix::System::Instance:traceFilesDir:traceFilesDir-Presentation Property*/


	public class TraceFilesDir extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TraceFilesDir(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:traceFilesDir:traceFilesDir")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:traceFilesDir:traceFilesDir")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public TraceFilesDir getTraceFilesDir();
	/*Radix::System::Instance:arteCntVeryHigh:arteCntVeryHigh-Presentation Property*/


	public class ArteCntVeryHigh extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ArteCntVeryHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntVeryHigh:arteCntVeryHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:arteCntVeryHigh:arteCntVeryHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ArteCntVeryHigh getArteCntVeryHigh();
	/*Radix::System::Instance:cpuCoreCount:cpuCoreCount-Presentation Property*/


	public class CpuCoreCount extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public CpuCoreCount(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:cpuCoreCount:cpuCoreCount")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:cpuCoreCount:cpuCoreCount")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public CpuCoreCount getCpuCoreCount();
	/*Radix::System::Instance:autoActualizeVer:autoActualizeVer-Presentation Property*/


	public class AutoActualizeVer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public AutoActualizeVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoActualizeVer:autoActualizeVer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:autoActualizeVer:autoActualizeVer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public AutoActualizeVer getAutoActualizeVer();
	/*Radix::System::Instance:effectiveSelfCheckTime:effectiveSelfCheckTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:effectiveSelfCheckTime:effectiveSelfCheckTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public EffectiveSelfCheckTime getEffectiveSelfCheckTime();
	/*Radix::System::Instance:isAadcSysMember:isAadcSysMember-Presentation Property*/


	public class IsAadcSysMember extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsAadcSysMember(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:isAadcSysMember:isAadcSysMember")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:isAadcSysMember:isAadcSysMember")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsAadcSysMember getIsAadcSysMember();
	/*Radix::System::Instance:canEditActualizeVer:canEditActualizeVer-Presentation Property*/


	public class CanEditActualizeVer extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public CanEditActualizeVer(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:canEditActualizeVer:canEditActualizeVer")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:canEditActualizeVer:canEditActualizeVer")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public CanEditActualizeVer getCanEditActualizeVer();
	/*Radix::System::Instance:trustedCertificateAliases:trustedCertificateAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:trustedCertificateAliases:trustedCertificateAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:trustedCertificateAliases:trustedCertificateAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public TrustedCertificateAliases getTrustedCertificateAliases();
	/*Radix::System::Instance:sapPropsXml:sapPropsXml-Presentation Property*/


	public class SapPropsXml extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SapPropsXml(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapPropsXml:sapPropsXml")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sapPropsXml:sapPropsXml")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SapPropsXml getSapPropsXml();
	/*Radix::System::Instance:warning:warning-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:warning:warning")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:warning:warning")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Warning getWarning();
	/*Radix::System::Instance:activityStatus:activityStatus-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:activityStatus:activityStatus")
		public  org.radixware.ads.System.common.ActivityStatus getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:activityStatus:activityStatus")
		public   void setValue(org.radixware.ads.System.common.ActivityStatus val) {
			Value = val;
		}
	}
	public ActivityStatus getActivityStatus();
	/*Radix::System::Instance:hostIpAddresses:hostIpAddresses-Presentation Property*/


	public class HostIpAddresses extends org.radixware.kernel.common.client.models.items.properties.PropertyArrStr{
		public HostIpAddresses(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddresses:hostIpAddresses")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:hostIpAddresses:hostIpAddresses")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public HostIpAddresses getHostIpAddresses();
	/*Radix::System::Instance:selfCheckTimeStr:selfCheckTimeStr-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeStr:selfCheckTimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:selfCheckTimeStr:selfCheckTimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SelfCheckTimeStr getSelfCheckTimeStr();
	/*Radix::System::Instance:keyAliases:keyAliases-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyAliases:keyAliases")
		public  org.radixware.kernel.common.types.ArrStr getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:keyAliases:keyAliases")
		public   void setValue(org.radixware.kernel.common.types.ArrStr val) {
			Value = val;
		}
	}
	public KeyAliases getKeyAliases();
	/*Radix::System::Instance:uptimeStr:uptimeStr-Presentation Property*/


	public class UptimeStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public UptimeStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:uptimeStr:uptimeStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:uptimeStr:uptimeStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public UptimeStr getUptimeStr();
	/*Radix::System::Instance:sensTraceFinishTime:sensTraceFinishTime-Presentation Property*/


	public class SensTraceFinishTime extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public SensTraceFinishTime(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sensTraceFinishTime:sensTraceFinishTime")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:sensTraceFinishTime:sensTraceFinishTime")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public SensTraceFinishTime getSensTraceFinishTime();
	public static class PerformMaintenance extends org.radixware.kernel.common.client.models.items.Command{
		protected PerformMaintenance(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.InstanceMaintenanceRs.class);
		}

	}

	public static class ActualizeVer extends org.radixware.kernel.common.client.models.items.Command{
		protected ActualizeVer(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			processResponse(response, null);
		}

	}

	public static class FillArteTable extends org.radixware.kernel.common.client.models.items.Command{
		protected FillArteTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.FillArteTableRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.FillArteTableRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.FillArteTableRs.class);
		}

	}

	public static class ReloadArtePool extends org.radixware.kernel.common.client.models.items.Command{
		protected ReloadArtePool(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.ReloadArtePoolRs.class);
		}

	}

	public static class StopAllUnitsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected StopAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.StopAllUnitsRs.class);
		}

	}

	public static class ShowUsedPortsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected ShowUsedPortsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.ads.Acs.common.CommandsXsd.StrValueDocument.class);
		}

	}

	public static class CaptureClassLoadingProfile extends org.radixware.kernel.common.client.models.items.Command{
		protected CaptureClassLoadingProfile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class ApplyOptionsFromFile extends org.radixware.kernel.common.client.models.items.Command{
		protected ApplyOptionsFromFile(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class StartAllUnitsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected StartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.StartAllUnitsRs.class);
		}

	}

	public static class GetInstanceStateReport extends org.radixware.kernel.common.client.models.items.Command{
		protected GetInstanceStateReport(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}

	public static class EditKeystoreAliasesCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected EditKeystoreAliasesCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public void send(org.radixware.kernel.common.types.Id propertyId) throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),propertyId,null);
			processResponse(response, propertyId);
		}

	}

	public static class RestartAllUnitsCmd extends org.radixware.kernel.common.client.models.items.Command{
		protected RestartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		public org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs send() throws org.radixware.kernel.common.exceptions.ServiceClientException,InterruptedException{
			org.radixware.schemas.eas.CommandRs response = getEnvironment().getEasSession().executeCommand(owner,null,getDefinition().getId(),null,null);
			org.apache.xmlbeans.XmlObject xmlResponse = processResponse(response, null);
			return (org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),xmlResponse,org.radixware.schemas.systeminstancecontrol.RestartAllUnitsRs.class);
		}

	}



}

/* Radix::System::Instance - Web Meta*/

/*Radix::System::Instance-Entity Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Instance_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::System::Instance:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
			"Radix::System::Instance",
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("imgPKOWNB4VQWDQHK3PBAZSKPGSN3ZMUVLN"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYDG2SQYSHOBDCKRAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),16384,

			/*Radix::System::Instance:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::System::Instance:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXSBA5QYSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Instance:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYWM27QYSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Instance:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:startOSCommand:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO6KNHKT4RHWDBRCXAAIT4AGD7E"),
						"startOSCommand",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFVMK2IZSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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

						/*Radix::System::Instance:startOSCommand:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:stopOSCommand:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colROO4LOT4RHWDBRCXAAIT4AGD7E"),
						"stopOSCommand",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVMK2IZSHOBDCKRAALOMT5GDM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
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

						/*Radix::System::Instance:stopOSCommand:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdULKYARIUXBFI5MPQNEWNKM6QKI"),
						"keyAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:keyAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYJOSHPDUNFY5HUFRZHZQ3KCRE"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:trustedCertificateAliases:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdL3UHR26LYRDOPNHTBAQ2MDLTCM"),
						"trustedCertificateAliases",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:trustedCertificateAliases:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSS7F4MOTAFEPZJMUNXPPAIYNII"),
						null,
						null,
						false,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:sapPropsXml:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdMUOPZ76SOVFJBNZVEXOLVE2TSA"),
						"sapPropsXml",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:sapPropsXml:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:warning:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdP3N5ITE3DFCU7HDUIKM2MCWIIU"),
						"warning",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:warning:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntAboveNormal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7TINTWQVARGBRPLD2OLCBC2PJI"),
						"arteCntAboveNormal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM55QIJUMBEBPFEWUKCEGCGCFU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntAboveNormal:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJDC3VPAKRD4JFTP3XIEEJP5NQ"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntCritical:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AFWSTW7EJAHDCWFP4FXF57W5M"),
						"arteCntCritical",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLDAJAN2OVDI5IQGJNUKQKAOKU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntCritical:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5UG737NJK5ACFCZW5RCB7QRBIE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM522N4URWVD3BAYUQ4DBXFV6XA"),
						"arteCntHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JFGOBLVGZG67EIKZ2P6ZLLEAI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XEAZYUJEREOVLFAKZMJJAVQKE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteCntVeryHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY4ZKX4SUCBAHDLYFDL64EZSY6M"),
						"arteCntVeryHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUHMGQLCEVAF5HQKVW64XU27SU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteCntVeryHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPUL6YTXZJAZROVXEKN375S7DE"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBCHU436MFFEPLQPW6H6QGHNOE"),
						"arteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X2FHQRQPFARBMC66VYNPSS3CQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:arteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:arteInstLiveTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPVFIBHTN5NA35FF6RJCZIM6SAY"),
						"arteInstLiveTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB7JIIV5BLJBD7KP6SJHU4K3JZQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:arteInstLiveTime:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOL3OKSUKOBD3VCAFHPCAIR7BCM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:useActiveArteLimits:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ABN5MPEFBGRNERT3MSWLBFILE"),
						"useActiveArteLimits",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDLLCNOLAFEWVD6LNG647RVZUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:useActiveArteLimits:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:lowArteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z2JD6POZNCZJLGNTEGIJHZEQQ"),
						"lowArteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHAGMYJJ6VRDGTGZRG46SEWDD5M"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("2"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:lowArteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:highArteInstCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27DHEMV4S5FAVBSPLT6B6R4E6M"),
						"highArteInstCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SOMLSZPBRFQVEJEZNNHALADRI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:highArteInstCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:started:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),
						"started",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTANRE6IIJB7BHDKLB6PTCNX24"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						0,
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

						/*Radix::System::Instance:started:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxTraceFileCnt:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPBWOB42OZCATJ6INHY24PAYLI"),
						"maxTraceFileCnt",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMPOSUEDVBCCJE7LSWHJFCDJIU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:maxTraceFileCnt:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,9999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxTraceFileSizeKb:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTN2GUM43VD5TH34XEED3NBSWM"),
						"maxTraceFileSizeKb",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYWANGGUTVRGFDJDM3KVGMVDOYU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1024"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:maxTraceFileSizeKb:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:rotateTraceFilesDaily:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCHSEV5KJGVACBL2YSMNARJCWKI"),
						"rotateTraceFilesDaily",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAMF3NHHVJVHFBKCB3EEBP3R6M4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:rotateTraceFilesDaily:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:traceFilesDir:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXGRPSYS6JVGHRMXMH2FEUSJMQU"),
						"traceFilesDir",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGEPL4H3H3VAXHAON6PV36CML7A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("./logs"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:traceFilesDir:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,250,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:guiTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col24KG33MBMRAATA7S5VFV34PLTE"),
						"guiTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27UGHLZD5NG7RKVDX27XMCOSNE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:guiTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:fileTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF666DZAJ3NF3BOLDRS6BDK75T4"),
						"fileTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTFVLVJQRDHXIB6MDYFL4SZHY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:fileTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:dbTraceProfile:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUJAMRNBTMVE2BHL3BKCXMXSNMI"),
						"dbTraceProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOR2TT376VGZDJ4MYS2RKRMTTY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:dbTraceProfile:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:autoActualizeVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDM5FZM6CNAAVLLCHIQB4F6WL4"),
						"autoActualizeVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBNN2FUK7BCNXHK73KJMKPOOZA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:autoActualizeVer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:httpProxy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5B6RPEAWRBJVMM2OVLX3MSGKY"),
						"httpProxy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7KIBZJJXZHOZCCFGKTNZLGWG4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:httpProxy:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:httpsProxy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMQKXC2RA7BFJTI5YEECPN2JU2I"),
						"httpsProxy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXWXBOPA5ZFPBPHTKQCRAGQU2Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:httpsProxy:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,500,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyStorePath:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDAQ557DC6BAZRI6VNI3WDLB5HQ"),
						"keyStorePath",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34SDG7OPZEQXPFKS6TEPIVIXE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("radixware.org/server/keys.jceks"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:keyStorePath:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,256,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyStoreType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YSQY2R375FKFKYACAY762WGD4"),
						"keyStoreType",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDVBTHYGRRCIPNG3SA3GT3KGJU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFOUQICKMYJAGDL356HLXUNICKE"),
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

						/*Radix::System::Instance:keyStoreType:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFOUQICKMYJAGDL356HLXUNICKE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:keyTabFilePath:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTLHWR7V5OJE53K6G4EJDPJY2XY"),
						"keyTabFilePath",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOADIICLERFHS3LN5XEPACZLRSU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:keyTabFilePath:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,256,true),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZN7D6K4DGJAMTOAZJRNW22QVMU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:oraImplStmtCacheSize:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"),
						"oraImplStmtCacheSize",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls35EJSP3CJ5AMFNQRAIVDT54SH4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::System::Instance:oraImplStmtCacheSize:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:useOraImplStmtCache:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD3UDCXDDAJDR7B3YEBXV3TBNAU"),
						"useOraImplStmtCache",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XVB54JPJJD5FKJXAA4DHG74QU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.BOOL,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::System::Instance:useOraImplStmtCache:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:sapId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colA333YBDFB5EMVI6N4N6IUKFG3M"),
						"sapId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURH44I562NG25AKUUKACQEIYYY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:sapId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:scp:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"),
						"scp",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOXLRPPSZ75GJZKE4LROFLCCUNY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLCOZUPH3GNA43BCNANGFGGW62Y"),
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprWDOEJDIMZPNRDB64AALOMT5GDM")},
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("sprDRGOFLVET7NRDB56AALOMT5GDM"),
						0,
						0,false),

					/*Radix::System::Instance:avgActiveArteCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5F32BDXFU5GLJDKLCF5OHFYQDI"),
						"avgActiveArteCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWF7HHSS2RVEHJL64Y6DH2QA3FY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.NUM,
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

						/*Radix::System::Instance:avgActiveArteCount:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:memoryCheckPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colETPNDLR7SVE4JEEF664PVZPKN4"),
						"memoryCheckPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYFPIIMFMBD5BIS5LSEHENYCMY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHMAAYM53MNA5TD56KGA6NZ7VY4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:memoryCheckPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSLL5K7DA5BAXEVOXDNZGDOWB4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:targetExecutor:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4KPRPAXBXRCYBBGYUXJNALPQQQ"),
						"targetExecutor",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTS4SNFXWUJAUXPPLMGBBEDKRDY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.PARENT_REF,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						20,
						null,
						null,
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
						null,
						null,
						null,
						null,
						true,-1,-1,1,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						133693439,
						133693439,false),

					/*Radix::System::Instance:targetExecutorId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFNUWBLQL3RCATBI3O5KPEXF7A4"),
						"targetExecutorId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:targetExecutorId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteNormal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"),
						"maxActiveArteNormal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO65EAYZJ5A2JFS2235KXKK3L4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteNormal:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBYQFI2SW7VE6RCLJFOD6GZHT2Y"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteAboveNormal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"),
						"maxActiveArteAboveNormal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW67PPWIVA5EKXCR6T74XIHB7TU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteAboveNormal:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGIPBF54YVNBC3OXTULIWX3TIF4"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"),
						"maxActiveArteHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWTXZ7RTBGHXLLCSOMNZGR6DM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQNJRHADXRVFZLCFBYSEEXIA7XU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteVeryHigh:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"),
						"maxActiveArteVeryHigh",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5B443SCPYZAS3FHDTQ4NLF2BJQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteVeryHigh:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7M63WS3URHJHL2M3SBHGIUORM"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:maxActiveArteCritical:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"),
						"maxActiveArteCritical",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6POZVFOIZEQXHCVG7T576S66Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:maxActiveArteCritical:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBTO6VHTRNGGTGKW5KJCO6D2GA"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:sensTraceFinishTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVRAIUGRMHVFLZJ4VCWCCPZTQPM"),
						"sensTraceFinishTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRRZW4W3JNDXXJD4IE5BVMHMDA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:sensTraceFinishTime:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:classLoadingProfileId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJMKGEQX2FG53KHYXUTIJMMKYE"),
						"classLoadingProfileId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:classLoadingProfileId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:classLoadingProfile:PropertyPresentation-Parent Ref Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadParentRefPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UQLYTWVCJEUDHVXJFXYGP7KN4"),
						"classLoadingProfile",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecS6IN3XOILNANJG3BCB7NTDDMDM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblS6IN3XOILNANJG3BCB7NTDDMDM"),
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHGKCI7PIZRAK3BTNOQ75K2MUJA")},
						null,
						null,
						3668987,
						3669995,false),

					/*Radix::System::Instance:sysdate:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7COJCFXLD5BU3HOJD4DMPNJF3E"),
						"sysdate",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:sysdate:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:selfCheckTimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),
						"selfCheckTimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJJZGBX3IRHAVP7DECEU3YFFTM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:selfCheckTimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:activityStatus:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),
						"activityStatus",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPQARQYQ2VDLXMHQJ77MELKG2E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:activityStatus:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsR6P4CGKI2RF77BXRMWNZWXWDLI"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:aadcDgAddress:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOF6APP4I7FHV7BCQLHO75CZR74"),
						"aadcDgAddress",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVF5JGZRMVCV7DSKPLTXKAF3UM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:aadcDgAddress:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:aadcMemberId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),
						"aadcMemberId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN4LZUFJOJCNRNCO5N43FTGAAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:aadcMemberId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,2L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:isAadcSysMember:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFJL567ZQ4NDZTPCDLNPJFKIUCU"),
						"isAadcSysMember",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:isAadcSysMember:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:autoRestartDelaySec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM65QF5VLNZD6FPDDMADFENBBUU"),
						"autoRestartDelaySec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRWVUWS7HVC3VH2MIWKNQCQ4BA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:autoRestartDelaySec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(0L,999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:osPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col55KRIWQ6TVBX3CIF4UY25YTMZE"),
						"osPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZCSYUK7ZFGBTLENATKOZ2DWLU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:osPid:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:appVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYGFOEBBHJEPHGO5B2Y4E6QUBA"),
						"appVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGPVMIPUDSRCRJN56RQYHGU2V4Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:appVersion:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:kernelVersion:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFPV5MEWHSNFFRGQN4V5CEMIXXQ"),
						"kernelVersion",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7CYYIAQBBETJI3JU3VR3IVSWU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:kernelVersion:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:uptimeStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVITFLAYDGNHBZOXAQIT3GUPQ2Q"),
						"uptimeStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXJO7WUUNDZJIRMO45U2FOLK4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:uptimeStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:dbCurMillis:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWOPG2OAWFD2PIM5SAH75DXSPI"),
						"dbCurMillis",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:dbCurMillis:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:revision:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3YVJTL7CFCALHOXPVVDPZID7Q"),
						"revision",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWMZC6XE3NDBJH22DNJJ3DEI7U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:revision:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999999999999L,999999999999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:canEditActualizeVer:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),
						"canEditActualizeVer",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:canEditActualizeVer:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:effectiveSelfCheckTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd5RRJ2XRTYZCRJOOTZPSFGIVIMM"),
						"effectiveSelfCheckTime",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:effectiveSelfCheckTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:instStateForcedGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIKMV3NWRZCDXHXWRERFBXVFT4"),
						"instStateForcedGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVL23IIZ34FCN5MG6J7OWSKL6WI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:instStateForcedGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,300L,(byte)-1,null,10L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWFIABAFRJH7BMAPTRJZS4PTFY"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:instStateGatherPeriodSec:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2X4UBSDEPBBG5LYPLRDF6YSOIU"),
						"instStateGatherPeriodSec",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMKMU66PPJG3NFVQH3BUH5PI2A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
						true,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("-1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::System::Instance:instStateGatherPeriodSec:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,120L,(byte)-1,null,5L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVOUNSTMCSFEUZC7AB37CYIUJHU"),
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:instStateHistoryStorePeriodDays:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGHZ3KYMJURDDBHJ3IJXSUY35LA"),
						"instStateHistoryStorePeriodDays",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCFTW5WON5GDLCZLPYBKSFTLIA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						null,
						false,
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

						/*Radix::System::Instance:instStateHistoryStorePeriodDays:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,90L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:cpuCoreCount:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZAGLT4KO6VC45P3ANFRXEJEFRA"),
						"cpuCoreCount",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFXD5D3ULZD35AXQTECYBRT55U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:cpuCoreCount:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:hostName:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col6Q5VAISXVREI3K3APBJWYJWQYQ"),
						"hostName",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLB2GYJPR2NAGBEZPQO5DUZAGGI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
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

						/*Radix::System::Instance:hostName:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,255,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::System::Instance:hostIpAddresses:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdTUDXYLL6HVEIZORAMXTOVJJSHA"),
						"hostIpAddresses",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIKLMMDE7NEQJHHKJFGV2MKID4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.enums.EValType.ARR_STR,
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

						/*Radix::System::Instance:hostIpAddresses:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:startAllUnitsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdNXCBARNCSLOBDCKTAALOMT5GDM"),
						"startAllUnitsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZAPYPPJ7FGURIGIRMD7SHI65E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYTQLIJ3AFIXRTYR3VKVAGGLM346GSPPC"),
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
					/*Radix::System::Instance:stopAllUnitsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdEMWWCWA3SHOBDCKRAALOMT5GDM"),
						"stopAllUnitsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QEANBWVS5H37JCHEJDTPW3ZMQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img7JYIJZYBESRV5BYGLERHDLWECGFYG6CC"),
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
					/*Radix::System::Instance:restartAllUnitsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdXG6DDFFSQPOBDCKCAALOMT5GDM"),
						"restartAllUnitsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDFTYV2MPJCKTHRZ2GLJQJIVZY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgFCS5AS7TDYYOG6KX3TO6HLJDGJK4JZD7"),
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
					/*Radix::System::Instance:editKeystoreAliasesCmd-Property Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdUJAYTWUX35CGFJTSTUMQNAB6EY"),
						"editKeystoreAliasesCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZM3X7J43VHCVPQDM7UPYTON4M"),
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.PROPERTY,
						new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAO4GDPUN4VCSJPJ57OZ5W7S7RA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdEH3ABYCN6JC33KQ24QKS2LTF24"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAGDAVFAP5RB4ZEJMLLJZYD3XM4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("prd7UOXOTN33FDA3OWKL4CAPPYDJQ")},
						org.radixware.kernel.common.enums.ECommandAccessibility.ALWAYS),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:showUsedPortsCmd-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIVHWGK3YW5CGFAX3SASK74K5ZI"),
						"showUsedPortsCmd",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIWKI3VDRFVC3ZA3QNT5QXTHQFM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgAR26WN32ABFQVNKGDBYBZGE6E4"),
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
					/*Radix::System::Instance:applyOptionsFromFile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdMKKHVZCNBVC2PCLYYDG2PXJQ2I"),
						"applyOptionsFromFile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU3DNZJVNBC3BG4IPM3NGZNVZU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgMUCCZVMA4JC4NMTVVEGYXEXU5Q"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhH7V4LFQZ5ZGSJFCISDGOYFJCNU"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:actualizeVer-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5VYLPC23CBCP3JAZLZAX7GZJOU"),
						"actualizeVer",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3TSRPYMCRF3LJ5EIGJFO5IR5U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("img3FU3DWZ2QJEMJNH6W3QEM2RVIA"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:reloadArtePool-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdD37DTAK4AFDZ3ISCFNXNREG2HY"),
						"reloadArtePool",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJRG2FWCSFHTLA4HCHCZR7S4B4"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgQQDSD3LDVRB37ON5QRIBTFTAQQ"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:performMaintenance-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmd5TAASNN6RNFOBLLWUQIK3S6WN4"),
						"performMaintenance",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZQJ47IOWZEAFNOR35NW7IDSWY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgF7F5VDLGVZC3XHP7IWBS7B4H7Q"),
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						true,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:getInstanceStateReport-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdOCIRXO2BUZET3HKCEEN22NL7EI"),
						"getInstanceStateReport",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRWB3W47ORCORNWE5LJX3Y225U"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgYDNPPCMGSBBPJOQKFNPS7TGQ3E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhSZZQMAM5WZFIJD5ZI5NPJK4XIA"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:fillArteTable-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdB3BDOSBJHVFDVCOMB34ZKXWFRM"),
						"fillArteTable",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						null,
						null,
						null,
						org.radixware.kernel.common.enums.ECommandNature.XML_IN_OUT,
						false,
						true,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT),
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::System::Instance:captureClassLoadingProfile-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdLYBOIRRR3JALTG5PGK7PURRMNE"),
						"captureClassLoadingProfile",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYHX4RXAEFCL3HUKN23XPCGOJY"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgWEQPJUYLXCAGMHF4KKUK6VWQPOORINWN"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhBLMVWQ2JQNDETEZTIWEG2JQEVQ"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_EXISTENT)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Instance:Started First-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQBCIUYAERTNRDB5PAALOMT5GDM"),
						"Started First",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJWK3KXQYSHOBDCKRAALOMT5GDM"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),
								true),

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),
								false)
						}),

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::System::Instance:By ID-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtSYLZEGABQFCE7HXEA5CZC6IOQ4"),
						"By ID",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWY6H2WEAJDPLIEEBKGIVIXNRY"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refKGDSAFFBXZGTJMYA6W5QP3RUWQ"),"Instance=>ClassLoadingProfile (classLoadingProfileId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblS6IN3XOILNANJG3BCB7NTDDMDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colMJMKGEQX2FG53KHYXUTIJMMKYE")},new String[]{"classLoadingProfileId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colUQJF45R5AJD3VJDJ4LI5OMHDVY")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refPT7FJIZP3JCIROWGR2UA2XGL3Y"),"Instance=>Unit (targetExecutorId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFNUWBLQL3RCATBI3O5KPEXF7A4")},new String[]{"targetExecutorId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refX5LLPHQYPBGLPJJHOZJ5RGEVUU"),"Instance=>Scp (scpName=>name, systemId=>systemId)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblAAU55TOEHJWDRPOYAAYQQ2Y3GB"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colYSPDMMOVEBGPZIMQX2RBMXCBQE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNPEGS5CTHRHRDF7ZU3IPI7GELU")},new String[]{"scpName","systemId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colI4QSJ5PEHJWDRPOYAAYQQ2Y3GB"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colWUQ25PISLDNBDJA6ACQMTAIZT4")},new String[]{"name","systemId"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refXTQO52VDLLOBDCJDAALOMT5GDM"),"Instance=>Sap (sapId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblR7FXMYDVVHWDBROXAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colA333YBDFB5EMVI6N4N6IUKFG3M")},new String[]{"sapId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ZDYU3V3RHWDBRCXAAIT4AGD7E")},new String[]{"id"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM")},
			false,true,false);
}

/* Radix::System::Instance:General - Desktop Meta*/

/*Radix::System::Instance:General-Editor Presentation*/

package org.radixware.ads.System.explorer;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Instance:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::Instance:General:Units-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHL6DLN6U3PORDOFEABIFNQAABA"),"Units",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHP6DLN6U3PORDOFEABIFNQAABA"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiOQENK3JCXTNRDB6QAALOMT5GDM")),

			/*Radix::System::Instance:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW5Z3GX6TBXOBDNTPAAMPGXSZKU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3CPCSBSV4NHUBJH6P2UGZK7WVU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO6KNHKT4RHWDBRCXAAIT4AGD7E"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colROO4LOT4RHWDBRCXAAIT4AGD7E"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YSQY2R375FKFKYACAY762WGD4"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDAQ557DC6BAZRI6VNI3WDLB5HQ"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDM5FZM6CNAAVLLCHIQB4F6WL4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colETPNDLR7SVE4JEEF664PVZPKN4"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5B6RPEAWRBJVMM2OVLX3MSGKY"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMQKXC2RA7BFJTI5YEECPN2JU2I"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTLHWR7V5OJE53K6G4EJDPJY2XY"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBCHU436MFFEPLQPW6H6QGHNOE"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5F32BDXFU5GLJDKLCF5OHFYQDI"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4KPRPAXBXRCYBBGYUXJNALPQQQ"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOF6APP4I7FHV7BCQLHO75CZR74"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM65QF5VLNZD6FPDDMADFENBBUU"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFPV5MEWHSNFFRGQN4V5CEMIXXQ"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYGFOEBBHJEPHGO5B2Y4E6QUBA"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVITFLAYDGNHBZOXAQIT3GUPQ2Q"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col55KRIWQ6TVBX3CIF4UY25YTMZE"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3YVJTL7CFCALHOXPVVDPZID7Q"),0,25,1,false,false)
			},null),

			/*Radix::System::Instance:General:Events-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TF4CA5IQFB5HAHIZRFBCYFRXI"),"Events",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUNTJ4UIBTJA6JCY7XR4IHRZ4OM")),

			/*Radix::System::Instance:General:TraceOptions-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQYYILPHH7NFXZJEPGAIG72VOQI"),"TraceOptions",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO2M4CNS3O5EOHBAE3NYFRDLZ6Y"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUJAMRNBTMVE2BHL3BKCXMXSNMI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col24KG33MBMRAATA7S5VFV34PLTE"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colF666DZAJ3NF3BOLDRS6BDK75T4"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXGRPSYS6JVGHRMXMH2FEUSJMQU"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKTN2GUM43VD5TH34XEED3NBSWM"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPBWOB42OZCATJ6INHY24PAYLI"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colCHSEV5KJGVACBL2YSMNARJCWKI"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVRAIUGRMHVFLZJ4VCWCCPZTQPM"),0,7,1,false,false)
			},null),

			/*Radix::System::Instance:General:Service-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgIACHAOH6LRAAXJP7I2CS6D3OYE"),"Service",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls256NGNP7B5CAHENCHD32D7KFEU"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepPAYQ4ALZORGHDNMKUQN5PFYU6A")),

			/*Radix::System::Instance:General:ArtePoolSettings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRV6ZNJRAVNBGNEOM2LHXSZ2FUA"),"ArtePoolSettings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULY54RII55CMJHLLTWUMABXWS4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgIUEPQAS4CRDNPIVLN6H7G34EEI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgSN2RM3E5OJFGXOI5GMKG57XOBE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ABN5MPEFBGRNERT3MSWLBFILE"),0,1,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgIUEPQAS4CRDNPIVLN6H7G34EEI"),"Size",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCEPT2RVGUZFRTPLU7BM6WLZ3QU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27DHEMV4S5FAVBSPLT6B6R4E6M"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z2JD6POZNCZJLGNTEGIJHZEQQ"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7TINTWQVARGBRPLD2OLCBC2PJI"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM522N4URWVD3BAYUQ4DBXFV6XA"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY4ZKX4SUCBAHDLYFDL64EZSY6M"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AFWSTW7EJAHDCWFP4FXF57W5M"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPVFIBHTN5NA35FF6RJCZIM6SAY"),0,6,1,false,false)
					}),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgSN2RM3E5OJFGXOI5GMKG57XOBE"),"ActivityLimits",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPHVLDJZ5FGBTDANMJ27HP7FPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"),0,4,1,false,false)
					})
			}),

			/*Radix::System::Instance:General:Performance-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg547YUVGI5RDP5J2723Y5WEWLHU"),"Performance",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP44YK2O2FBGQHA72VSIJEZ75MQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD3UDCXDDAJDR7B3YEBXV3TBNAU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UQLYTWVCJEUDHVXJFXYGP7KN4"),0,0,1,false,false)
			},null),

			/*Radix::System::Instance:General:ArtePoolState-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFQUCB2IAQNBQVNV5KPULT2TLRQ"),"ArtePoolState",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3HCYQ53KFFXLANE273IKEA4E4"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFJUXS7VBXJF3DDHROPUQXLIKHQ")),

			/*Radix::System::Instance:General:ArtePool-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgYPFRJWN3VZGGRKPWWLSEHJNAAE"),"ArtePool",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZDK6I7E4Z5FP3E46SCJKDBOMSI"),null,null,null),

			/*Radix::System::Instance:General:Monitoring-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGYA4T5HTNRESJN3WGOUTESOPZI"),"Monitoring",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMZJKU2UDNAMJOB4S4HKWYGWLA"),null,null,null),

			/*Radix::System::Instance:General:Settings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVVSWJV3KIVEJBGKA65XDHS35YQ"),"Settings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZK5ZQQD6RCSTM4DEK22RYEEIU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2X4UBSDEPBBG5LYPLRDF6YSOIU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIKMV3NWRZCDXHXWRERFBXVFT4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGHZ3KYMJURDDBHJ3IJXSUY35LA"),0,2,1,false,false)
			},null),

			/*Radix::System::Instance:General:History-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTV7YNKJVYFDMPBNE5OKAZV377I"),"History",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiPZKBAATGQRDXLC432TZ4F3COUY"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW5Z3GX6TBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgYPFRJWN3VZGGRKPWWLSEHJNAAE")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRV6ZNJRAVNBGNEOM2LHXSZ2FUA")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFQUCB2IAQNBQVNV5KPULT2TLRQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgIACHAOH6LRAAXJP7I2CS6D3OYE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgHL6DLN6U3PORDOFEABIFNQAABA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgQYYILPHH7NFXZJEPGAIG72VOQI")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg547YUVGI5RDP5J2723Y5WEWLHU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGYA4T5HTNRESJN3WGOUTESOPZI")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVVSWJV3KIVEJBGKA65XDHS35YQ")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTV7YNKJVYFDMPBNE5OKAZV377I")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TF4CA5IQFB5HAHIZRFBCYFRXI"))}
	,

	/*Radix::System::Instance:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::System::Instance:General:Unit-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiOQENK3JCXTNRDB6QAALOMT5GDM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec5HP4XTP3EGWDBRCRAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("spr6AFPZS6TRHNRDB5MAALOMT5GDM"),
					32,
					null,
					16560,true),

				/*Radix::System::Instance:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUNTJ4UIBTJA6JCY7XR4IHRZ4OM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
					0,
					null,
					16560,true),

				/*Radix::System::Instance:General:ArteInstance-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFJUXS7VBXJF3DDHROPUQXLIKHQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA24OTIGNQBCGHIIJLHL2FF75KY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLZ2MP65VX5GZJP62JFIJSII76Q"),
					0,
					null,
					16560,false),

				/*Radix::System::Instance:General:InstanceStateHistory-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiPZKBAATGQRDXLC432TZ4F3COUY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ2SAJZEWG5CZXKPRJ6VSWMIUEI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),
					0,
					null,
					16432,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::Instance:General - Web Meta*/

/*Radix::System::Instance:General-Editor Presentation*/

package org.radixware.ads.System.web;
public final class General_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM"),
	"General",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
	null,
	null,

	/*Radix::System::Instance:General:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::System::Instance:General:Main-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW5Z3GX6TBXOBDNTPAAMPGXSZKU"),"Main",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3CPCSBSV4NHUBJH6P2UGZK7WVU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colO6KNHKT4RHWDBRCXAAIT4AGD7E"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colROO4LOT4RHWDBRCXAAIT4AGD7E"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEPZ6ANCU3ZCX7NYBHECLKVCHCQ"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3YSQY2R375FKFKYACAY762WGD4"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colDAQ557DC6BAZRI6VNI3WDLB5HQ"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colZDM5FZM6CNAAVLLCHIQB4F6WL4"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colETPNDLR7SVE4JEEF664PVZPKN4"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colC5B6RPEAWRBJVMM2OVLX3MSGKY"),0,12,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMQKXC2RA7BFJTI5YEECPN2JU2I"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colTLHWR7V5OJE53K6G4EJDPJY2XY"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colEBCHU436MFFEPLQPW6H6QGHNOE"),0,20,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col5F32BDXFU5GLJDKLCF5OHFYQDI"),0,21,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4KPRPAXBXRCYBBGYUXJNALPQQQ"),0,22,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),0,15,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),0,17,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),0,16,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5PSVWUEQBHPTA6CXUPA2UVOWI"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOF6APP4I7FHV7BCQLHO75CZR74"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM65QF5VLNZD6FPDDMADFENBBUU"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFPV5MEWHSNFFRGQN4V5CEMIXXQ"),0,23,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQYGFOEBBHJEPHGO5B2Y4E6QUBA"),0,24,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdVITFLAYDGNHBZOXAQIT3GUPQ2Q"),0,18,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col55KRIWQ6TVBX3CIF4UY25YTMZE"),0,19,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colL3YVJTL7CFCALHOXPVVDPZID7Q"),0,25,1,false,false)
			},null),

			/*Radix::System::Instance:General:Events-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TF4CA5IQFB5HAHIZRFBCYFRXI"),"Events",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUNTJ4UIBTJA6JCY7XR4IHRZ4OM")),

			/*Radix::System::Instance:General:Service-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgIACHAOH6LRAAXJP7I2CS6D3OYE"),"Service",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls256NGNP7B5CAHENCHD32D7KFEU"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepQETRL7AIT5GOBK7X6PRCBPC5BU")),

			/*Radix::System::Instance:General:ArtePoolSettings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRV6ZNJRAVNBGNEOM2LHXSZ2FUA"),"ArtePoolSettings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULY54RII55CMJHLLTWUMABXWS4"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgIUEPQAS4CRDNPIVLN6H7G34EEI"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgSN2RM3E5OJFGXOI5GMKG57XOBE"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2ABN5MPEFBGRNERT3MSWLBFILE"),0,1,1,false,false)
			},new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgIUEPQAS4CRDNPIVLN6H7G34EEI"),"Size",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCEPT2RVGUZFRTPLU7BM6WLZ3QU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col27DHEMV4S5FAVBSPLT6B6R4E6M"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4Z2JD6POZNCZJLGNTEGIJHZEQQ"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7TINTWQVARGBRPLD2OLCBC2PJI"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colM522N4URWVD3BAYUQ4DBXFV6XA"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colY4ZKX4SUCBAHDLYFDL64EZSY6M"),0,4,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3AFWSTW7EJAHDCWFP4FXF57W5M"),0,5,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPVFIBHTN5NA35FF6RJCZIM6SAY"),0,6,1,false,false)
					}),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PropertiesGroup(org.radixware.kernel.common.types.Id.Factory.loadFrom("ppgSN2RM3E5OJFGXOI5GMKG57XOBE"),"ActivityLimits",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPHVLDJZ5FGBTDANMJ27HP7FPE"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),false,true,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"),0,0,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"),0,1,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"),0,2,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"),0,3,1,false,false),

							new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"),0,4,1,false,false)
					})
			}),

			/*Radix::System::Instance:General:Performance-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg547YUVGI5RDP5J2723Y5WEWLHU"),"Performance",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP44YK2O2FBGQHA72VSIJEZ75MQ"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colD3UDCXDDAJDR7B3YEBXV3TBNAU"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col7UQLYTWVCJEUDHVXJFXYGP7KN4"),0,0,1,false,false)
			},null),

			/*Radix::System::Instance:General:ArtePoolState-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFQUCB2IAQNBQVNV5KPULT2TLRQ"),"ArtePoolState",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3HCYQ53KFFXLANE273IKEA4E4"),null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFJUXS7VBXJF3DDHROPUQXLIKHQ")),

			/*Radix::System::Instance:General:ArtePool-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgYPFRJWN3VZGGRKPWWLSEHJNAAE"),"ArtePool",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZDK6I7E4Z5FP3E46SCJKDBOMSI"),null,null,null),

			/*Radix::System::Instance:General:Monitoring-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGYA4T5HTNRESJN3WGOUTESOPZI"),"Monitoring",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMZJKU2UDNAMJOB4S4HKWYGWLA"),null,null,null),

			/*Radix::System::Instance:General:Settings-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVVSWJV3KIVEJBGKA65XDHS35YQ"),"Settings",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZK5ZQQD6RCSTM4DEK22RYEEIU"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2X4UBSDEPBBG5LYPLRDF6YSOIU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colHIKMV3NWRZCDXHXWRERFBXVFT4"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGHZ3KYMJURDDBHJ3IJXSUY35LA"),0,2,1,false,false)
			},null),

			/*Radix::System::Instance:General:History-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadContainerEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTV7YNKJVYFDMPBNE5OKAZV377I"),"History",null,null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiPZKBAATGQRDXLC432TZ4F3COUY"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgW5Z3GX6TBXOBDNTPAAMPGXSZKU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgYPFRJWN3VZGGRKPWWLSEHJNAAE")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgRV6ZNJRAVNBGNEOM2LHXSZ2FUA")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgFQUCB2IAQNBQVNV5KPULT2TLRQ")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgIACHAOH6LRAAXJP7I2CS6D3OYE")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg547YUVGI5RDP5J2723Y5WEWLHU")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgGYA4T5HTNRESJN3WGOUTESOPZI")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgVVSWJV3KIVEJBGKA65XDHS35YQ")),
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(2,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgTV7YNKJVYFDMPBNE5OKAZV377I")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6TF4CA5IQFB5HAHIZRFBCYFRXI"))}
	,

	/*Radix::System::Instance:General:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::System::Instance:General:EventLog-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiUNTJ4UIBTJA6JCY7XR4IHRZ4OM"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecACQVZOMVVHWDBROXAAIT4AGD7E"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprVJGCRERZOBDS7GULG6RLDVLG3A"),
					0,
					null,
					16560,true),

				/*Radix::System::Instance:General:ArteInstance-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiFJUXS7VBXJF3DDHROPUQXLIKHQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),null,
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecA24OTIGNQBCGHIIJLHL2FF75KY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprLZ2MP65VX5GZJP62JFIJSII76Q"),
					0,
					null,
					16560,false),

				/*Radix::System::Instance:General:InstanceStateHistory-Entity Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadSelectorExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpiPZKBAATGQRDXLC432TZ4F3COUY"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ2SAJZEWG5CZXKPRJ6VSWMIUEI"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aecRHY4TVWIC5C4TJN2GKL2ABRKKQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprPWR74F5KQJH57IPCYXNIPARDT4"),
					0,
					null,
					16432,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	2192,0,0,null);
}
/* Radix::System::Instance:General:Model - Desktop Executable*/

/*Radix::System::Instance:General:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model")
public class General:Model  extends org.radixware.ads.System.explorer.Instance.Instance_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Instance:General:Model:Nested classes-Nested Classes*/

	/*Radix::System::Instance:General:Model:Properties-Properties*/

	/*Radix::System::Instance:General:Model:sapEditor-Dynamic Property*/



	protected org.radixware.ads.System.common_client.ISapEditorModel sapEditor=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:sapEditor")
	protected published  org.radixware.ads.System.common_client.ISapEditorModel getSapEditor() {
		return sapEditor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:sapEditor")
	protected published   void setSapEditor(org.radixware.ads.System.common_client.ISapEditorModel val) {
		sapEditor = val;
	}

	/*Radix::System::Instance:General:Model:useOraImplStmtCache-Presentation Property*/




	public class UseOraImplStmtCache extends org.radixware.ads.System.explorer.Instance.colD3UDCXDDAJDR7B3YEBXV3TBNAU{
		public UseOraImplStmtCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache(){return (UseOraImplStmtCache)getProperty(colD3UDCXDDAJDR7B3YEBXV3TBNAU);}

	/*Radix::System::Instance:General:Model:oraImplStmtCacheSize-Presentation Property*/




	public class OraImplStmtCacheSize extends org.radixware.ads.System.explorer.Instance.colQW66DBXQ2BFOTAKTX7LRPNLTZY{
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

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:Properties-Properties*/

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:Methods-Methods*/

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:oraImplStmtCacheSize:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useOraImplStmtCache.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize(){return (OraImplStmtCacheSize)getProperty(colQW66DBXQ2BFOTAKTX7LRPNLTZY);}

	/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal-Presentation Property*/




	public class MaxActiveArteAboveNormal extends org.radixware.ads.System.explorer.Instance.colLXK2TPSGGJDEZK6YZB5HJJC5RI{
		public MaxActiveArteAboveNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteAboveNormal:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteAboveNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteAboveNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteAboveNormal getMaxActiveArteAboveNormal(){return (MaxActiveArteAboveNormal)getProperty(colLXK2TPSGGJDEZK6YZB5HJJC5RI);}

	/*Radix::System::Instance:General:Model:maxActiveArteCritical-Presentation Property*/




	public class MaxActiveArteCritical extends org.radixware.ads.System.explorer.Instance.col63MN22Q6GRFUVDV2J6UD2I4VOU{
		public MaxActiveArteCritical(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteCritical:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteCritical:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteCritical")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteCritical")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteCritical getMaxActiveArteCritical(){return (MaxActiveArteCritical)getProperty(col63MN22Q6GRFUVDV2J6UD2I4VOU);}

	/*Radix::System::Instance:General:Model:maxActiveArteHigh-Presentation Property*/




	public class MaxActiveArteHigh extends org.radixware.ads.System.explorer.Instance.colFWLWDVK6E5FZHA3FOQEMLGSEI4{
		public MaxActiveArteHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteHigh:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteHigh:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteHigh getMaxActiveArteHigh(){return (MaxActiveArteHigh)getProperty(colFWLWDVK6E5FZHA3FOQEMLGSEI4);}

	/*Radix::System::Instance:General:Model:maxActiveArteNormal-Presentation Property*/




	public class MaxActiveArteNormal extends org.radixware.ads.System.explorer.Instance.col4FNLYMXZMRF7VNKWXW4LAG2VMY{
		public MaxActiveArteNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteNormal:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteNormal:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteNormal getMaxActiveArteNormal(){return (MaxActiveArteNormal)getProperty(col4FNLYMXZMRF7VNKWXW4LAG2VMY);}

	/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh-Presentation Property*/




	public class MaxActiveArteVeryHigh extends org.radixware.ads.System.explorer.Instance.colOTSOCR6D45FQ5FFHIDN4SYAR7E{
		public MaxActiveArteVeryHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteVeryHigh:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteVeryHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteVeryHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteVeryHigh getMaxActiveArteVeryHigh(){return (MaxActiveArteVeryHigh)getProperty(colOTSOCR6D45FQ5FFHIDN4SYAR7E);}

	/*Radix::System::Instance:General:Model:useActiveArteLimits-Presentation Property*/




	public class UseActiveArteLimits extends org.radixware.ads.System.explorer.Instance.col2ABN5MPEFBGRNERT3MSWLBFILE{
		public UseActiveArteLimits(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useActiveArteLimits")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useActiveArteLimits")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseActiveArteLimits getUseActiveArteLimits(){return (UseActiveArteLimits)getProperty(col2ABN5MPEFBGRNERT3MSWLBFILE);}






















	/*Radix::System::Instance:General:Model:Methods-Methods*/

	/*Radix::System::Instance:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:afterRead")
	protected published  void afterRead () {
		boolean started = started.Value == true;

		getCommand(idof[Instance:stopAllUnitsCmd]).setEnabled(started);
		getCommand(idof[Instance:startAllUnitsCmd]).setEnabled(started);
		getCommand(idof[Instance:restartAllUnitsCmd]).setEnabled(started);
		getCommand(idof[Instance:applyOptionsFromFile]).setEnabled(started);
		getCommand(idof[Instance:actualizeVer]).setEnabled(started);
		getCommand(idof[Instance:reloadArtePool]).setEnabled(started);
		getCommand(idof[Instance:performMaintenance]).setEnabled(started);
		getCommand(idof[Instance:captureClassLoadingProfile]).setEnabled(started);


		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    targetExecutor.setVisible(targetExecutor.Value != null);
		    getCommand(idof[Instance:getInstanceStateReport]).setEnabled(started);
		    if (isEditorPageExists(idof[Instance:General:ArtePoolState])) {
		        getEditorPage(idof[Instance:General:ArtePoolState]).setVisible(started);
		    }
		    if (getView() != null) {
		        sensTraceFinishTime.afterModify();
		    }
		    aadcMemberId.setVisible(isAadcSysMember.Value == true);
		    aadcDgAddress.setMandatory(aadcMemberId.Value != null);
		    autoActualizeVer.setVisible(canEditActualizeVer.Value == true);
		    autoRestartDelaySec.setVisible(canEditActualizeVer.Value == true);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_StopAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_StopAllUnits")
	private final  void onCommand_StopAllUnits (org.radixware.ads.System.explorer.Instance.StopAllUnitsCmd command) {
		if(!Environment.messageConfirmation("Stop All Units", "Are you sure you want to stop ALL the instance units?"))
		  return;

		try{
		    InstanceControlServiceXsd:StopAllUnitsRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Environment.messageInformation("Stop All Units", "All units stopped");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		    	Environment.messageInformation("Stop All Units", "Stop of all units scheduled");
		    else
		  	Environment.messageError("Stop All Units", "Units stop error");
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_StartAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_StartAllUnits")
	private final  void onCommand_StartAllUnits (org.radixware.ads.System.explorer.Instance.StartAllUnitsCmd command) {
		try{
		    InstanceControlServiceXsd:StartAllUnitsRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		      Environment.messageInformation("Start All Units", "All units started");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		      Environment.messageInformation("Start All Units", "Start of all units scheduled");
		    else
		      Environment.messageError("Start All Units", "Error starting units");
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		} 
	}

	/*Radix::System::Instance:General:Model:onCommand_RestartAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_RestartAllUnits")
	private final  void onCommand_RestartAllUnits (org.radixware.ads.System.explorer.Instance.RestartAllUnitsCmd command) {
		try{
		    InstanceControlServiceXsd:RestartAllUnitsRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Environment.messageInformation("Restart All Units", "All units restarted");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Environment.messageInformation("Restart All Units", "Restart of all units scheduled");
		    else
		  	Environment.messageError("Restart All Units", "Error restarting the units");
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if(warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}

	/*Radix::System::Instance:General:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if(warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_ShowUsedAddresses-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_ShowUsedAddresses")
	private final  void onCommand_ShowUsedAddresses (org.radixware.ads.System.explorer.Instance.ShowUsedPortsCmd command) {
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

	/*Radix::System::Instance:General:Model:onCommand_ActualizeVer-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_ActualizeVer")
	public  void onCommand_ActualizeVer (org.radixware.ads.System.explorer.Instance.ActualizeVer command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::System::Instance:General:Model:sapEditorPageOpened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:sapEditorPageOpened")
	public  void sapEditorPageOpened (com.trolltech.qt.gui.QWidget widget) {
		sapEditor = ClientSapUtils.initSapEditor(sapId.Value, sapPropsXml, widget);
		sapEditor.setAvailableCertAliases(trustedCertificateAliases != null ? trustedCertificateAliases.Value : null);
		sapEditor.setAvailableKeyAliases(keyAliases != null ? keyAliases.Value : null);
		ClientSapUtils.configureEditorForInstanceSap(sapEditor);
		sapEditor.setParentPage(getEditorPage(idof[Instance:General:Service]));

	}

	/*Radix::System::Instance:General:Model:checkSapEditorsBeforeCreate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:checkSapEditorsBeforeCreate")
	protected published  boolean checkSapEditorsBeforeCreate () {
		if (sapEditor == null) {
		    getEditorPage(idof[Instance:General:Service]).setFocused();
		    try {
		        checkPropertyValues();
		    } catch (Exception ex) {
		        showException(ex);
		        return false;
		    }
		}
		return true;

	}

	/*Radix::System::Instance:General:Model:sapEditorPageClosed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:sapEditorPageClosed")
	public  void sapEditorPageClosed () {
		sapEditor = null;
	}

	/*Radix::System::Instance:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (highArteInstCount.Value == 0) {
		    Explorer.Utils::Trace.warning(getEnvironment(), "Available number of ARTEs for normal or lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		    getEnvironment().messageWarning("Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		}
		return checkSapEditorsBeforeCreate() && super.beforeCreate();

	}

	/*Radix::System::Instance:General:Model:onCommand_reloadArtePool-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_reloadArtePool")
	public  void onCommand_reloadArtePool (org.radixware.ads.System.explorer.Instance.ReloadArtePool command) {
		try {
		    InstanceControlServiceXsd:ReloadArtePoolRs output = command.send();
		    if (output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Environment.messageInformation("Reload ARTE Pool", "ARTE pool reload scheduled");
		    else
		        Environment.messageError("Reload ARTE Pool", "Error");
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    //ignore, because in this case error message put in trace during processing request
		} catch (InterruptedException e) {
		    Environment.getTracer().warning(e.getLocalizedMessage());
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_performMaintenance-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_performMaintenance")
	public  void onCommand_performMaintenance (org.radixware.ads.System.explorer.Instance.PerformMaintenance command) {
		try {
		    InstanceControlServiceXsd:InstanceMaintenanceRs output = command.send();
		    if (output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Environment.messageInformation(command.getTitle(), "Instance maintenance scheduled");
		    else
		        Environment.messageError(command.getTitle(), "Error");
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:checkPropertyValues-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:checkPropertyValues")
	protected published  void checkPropertyValues () throws org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException,org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException {
		super.checkPropertyValues();

		int maxArteCount = 0;
		if (highArteInstCount.Value != null) {
		    maxArteCount += highArteInstCount.Value.intValue();
		}
		if (arteCntAboveNormal.Value != null) {
		    maxArteCount += arteCntAboveNormal.Value.intValue();
		}
		if (arteCntHigh.Value != null) {
		    maxArteCount += arteCntHigh.Value.intValue();
		}
		if (arteCntVeryHigh.Value != null) {
		    maxArteCount += arteCntVeryHigh.Value.intValue();
		}
		if (arteCntCritical.Value != null) {
		    maxArteCount += arteCntCritical.Value.intValue();
		}

		if (maxArteCount < lowArteInstCount.Value.intValue()) {
		    throw new InvalidPropertyValueException(this, lowArteInstCount.Definition, Client.Validators::InvalidValueReason.Factory.createForTooBigValue(getEnvironment(), String.valueOf(maxArteCount)));
		}
	}

	/*Radix::System::Instance:General:Model:getEventContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:getEventContextId")
	public published  Str getEventContextId () {
		return String.valueOf(id.ValueObject);
	}

	/*Radix::System::Instance:General:Model:getEventContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:getEventContextType")
	public published  Str getEventContextType () {
		return Arte::EventContextType:SystemInstance.Value;
	}

	/*Radix::System::Instance:General:Model:onCommand_fillArteTable-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_fillArteTable")
	private final  void onCommand_fillArteTable (org.radixware.ads.System.explorer.Instance.FillArteTable command) {
		try {
		    command.send();
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    //ignore, because in this case error message put in trace during processing request
		} catch (InterruptedException e) {
		    Environment.getTracer().warning(e.getLocalizedMessage());
		}
	}

	/*Radix::System::Instance:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (highArteInstCount.Value == 0) {
		    Explorer.Utils::Trace.warning(getEnvironment(), "Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		    getEnvironment().messageWarning("Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");    
		}
		return super.beforeUpdate();
	}

	/*Radix::System::Instance:General:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (commandDef.getId().equals(idof[Instance:actualizeVer])) {
		    return canEditActualizeVer.Value.booleanValue();
		}
		return super.isCommandAccessible(commandDef);
	}
	public final class PerformMaintenance extends org.radixware.ads.System.explorer.Instance.PerformMaintenance{
		protected PerformMaintenance(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_performMaintenance( this );
		}

	}

	public final class ActualizeVer extends org.radixware.ads.System.explorer.Instance.ActualizeVer{
		protected ActualizeVer(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_actualizeVer( this );
		}

	}

	public final class FillArteTable extends org.radixware.ads.System.explorer.Instance.FillArteTable{
		protected FillArteTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_fillArteTable( this );
		}

	}

	public final class ReloadArtePool extends org.radixware.ads.System.explorer.Instance.ReloadArtePool{
		protected ReloadArtePool(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_reloadArtePool( this );
		}

	}

	public final class StopAllUnitsCmd extends org.radixware.ads.System.explorer.Instance.StopAllUnitsCmd{
		protected StopAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_stopAllUnitsCmd( this );
		}

	}

	public final class ShowUsedPortsCmd extends org.radixware.ads.System.explorer.Instance.ShowUsedPortsCmd{
		protected ShowUsedPortsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_showUsedPortsCmd( this );
		}

	}

	public final class StartAllUnitsCmd extends org.radixware.ads.System.explorer.Instance.StartAllUnitsCmd{
		protected StartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_startAllUnitsCmd( this );
		}

	}

	public final class RestartAllUnitsCmd extends org.radixware.ads.System.explorer.Instance.RestartAllUnitsCmd{
		protected RestartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_restartAllUnitsCmd( this );
		}

	}



























}

/* Radix::System::Instance:General:Model - Desktop Meta*/

/*Radix::System::Instance:General:Model-Entity Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem3M27YSVRRHNRDB5MAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Instance:General:Model:Properties-Properties*/
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

/* Radix::System::Instance:General:Model - Web Executable*/

/*Radix::System::Instance:General:Model-Entity Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model")
public class General:Model  extends org.radixware.ads.System.web.Instance.Instance_DefaultModel implements org.radixware.ads.System.common_client.IEventContextProvider  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::System::Instance:General:Model:Nested classes-Nested Classes*/

	/*Radix::System::Instance:General:Model:Properties-Properties*/

	/*Radix::System::Instance:General:Model:sapEditor-Dynamic Property*/



	protected org.radixware.ads.System.common_client.ISapEditorModel sapEditor=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:sapEditor")
	protected published  org.radixware.ads.System.common_client.ISapEditorModel getSapEditor() {
		return sapEditor;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:sapEditor")
	protected published   void setSapEditor(org.radixware.ads.System.common_client.ISapEditorModel val) {
		sapEditor = val;
	}

	/*Radix::System::Instance:General:Model:useOraImplStmtCache-Presentation Property*/




	public class UseOraImplStmtCache extends org.radixware.ads.System.web.Instance.colD3UDCXDDAJDR7B3YEBXV3TBNAU{
		public UseOraImplStmtCache(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQW66DBXQ2BFOTAKTX7LRPNLTZY")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useOraImplStmtCache")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useOraImplStmtCache")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseOraImplStmtCache getUseOraImplStmtCache(){return (UseOraImplStmtCache)getProperty(colD3UDCXDDAJDR7B3YEBXV3TBNAU);}

	/*Radix::System::Instance:General:Model:oraImplStmtCacheSize-Presentation Property*/




	public class OraImplStmtCacheSize extends org.radixware.ads.System.web.Instance.colQW66DBXQ2BFOTAKTX7LRPNLTZY{
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

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:Properties-Properties*/

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:Methods-Methods*/

		/*Radix::System::Instance:General:Model:oraImplStmtCacheSize:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:oraImplStmtCacheSize:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useOraImplStmtCache.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:oraImplStmtCacheSize")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:oraImplStmtCacheSize")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public OraImplStmtCacheSize getOraImplStmtCacheSize(){return (OraImplStmtCacheSize)getProperty(colQW66DBXQ2BFOTAKTX7LRPNLTZY);}

	/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal-Presentation Property*/




	public class MaxActiveArteAboveNormal extends org.radixware.ads.System.web.Instance.colLXK2TPSGGJDEZK6YZB5HJJC5RI{
		public MaxActiveArteAboveNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteAboveNormal:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteAboveNormal:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteAboveNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteAboveNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteAboveNormal getMaxActiveArteAboveNormal(){return (MaxActiveArteAboveNormal)getProperty(colLXK2TPSGGJDEZK6YZB5HJJC5RI);}

	/*Radix::System::Instance:General:Model:maxActiveArteCritical-Presentation Property*/




	public class MaxActiveArteCritical extends org.radixware.ads.System.web.Instance.col63MN22Q6GRFUVDV2J6UD2I4VOU{
		public MaxActiveArteCritical(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteCritical:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteCritical:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteCritical:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteCritical")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteCritical")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteCritical getMaxActiveArteCritical(){return (MaxActiveArteCritical)getProperty(col63MN22Q6GRFUVDV2J6UD2I4VOU);}

	/*Radix::System::Instance:General:Model:maxActiveArteHigh-Presentation Property*/




	public class MaxActiveArteHigh extends org.radixware.ads.System.web.Instance.colFWLWDVK6E5FZHA3FOQEMLGSEI4{
		public MaxActiveArteHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteHigh:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteHigh:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteHigh:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteHigh getMaxActiveArteHigh(){return (MaxActiveArteHigh)getProperty(colFWLWDVK6E5FZHA3FOQEMLGSEI4);}

	/*Radix::System::Instance:General:Model:maxActiveArteNormal-Presentation Property*/




	public class MaxActiveArteNormal extends org.radixware.ads.System.web.Instance.col4FNLYMXZMRF7VNKWXW4LAG2VMY{
		public MaxActiveArteNormal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteNormal:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteNormal:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteNormal:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteNormal")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteNormal")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteNormal getMaxActiveArteNormal(){return (MaxActiveArteNormal)getProperty(col4FNLYMXZMRF7VNKWXW4LAG2VMY);}

	/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh-Presentation Property*/




	public class MaxActiveArteVeryHigh extends org.radixware.ads.System.web.Instance.colOTSOCR6D45FQ5FFHIDN4SYAR7E{
		public MaxActiveArteVeryHigh(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			Int dummy = ((Int)x);
			setValue(dummy);
		}

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:EmbeddedClass-Property Presentation Embedded Class*/




		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:Nested classes-Nested Classes*/

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:Properties-Properties*/

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:Methods-Methods*/

		/*Radix::System::Instance:General:Model:maxActiveArteVeryHigh:isVisible-User Method*/

		@Override
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteVeryHigh:isVisible")
		public published  boolean isVisible () {
			return super.isVisible() && useActiveArteLimits.Value == Boolean.TRUE;
		}













		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteVeryHigh")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:maxActiveArteVeryHigh")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public MaxActiveArteVeryHigh getMaxActiveArteVeryHigh(){return (MaxActiveArteVeryHigh)getProperty(colOTSOCR6D45FQ5FFHIDN4SYAR7E);}

	/*Radix::System::Instance:General:Model:useActiveArteLimits-Presentation Property*/




	public class UseActiveArteLimits extends org.radixware.ads.System.web.Instance.col2ABN5MPEFBGRNERT3MSWLBFILE{
		public UseActiveArteLimits(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("col63MN22Q6GRFUVDV2J6UD2I4VOU")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colLXK2TPSGGJDEZK6YZB5HJJC5RI")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFWLWDVK6E5FZHA3FOQEMLGSEI4")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOTSOCR6D45FQ5FFHIDN4SYAR7E")));
			}
			if (aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getDefinition().isPropertyDefExistsById(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY"))) {
				this.addDependent(aem3M27YSVRRHNRDB5MAALOMT5GDM.this.getProperty(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4FNLYMXZMRF7VNKWXW4LAG2VMY")));
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useActiveArteLimits")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:useActiveArteLimits")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public UseActiveArteLimits getUseActiveArteLimits(){return (UseActiveArteLimits)getProperty(col2ABN5MPEFBGRNERT3MSWLBFILE);}






















	/*Radix::System::Instance:General:Model:Methods-Methods*/

	/*Radix::System::Instance:General:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:afterRead")
	protected published  void afterRead () {
		boolean started = started.Value == true;

		getCommand(idof[Instance:stopAllUnitsCmd]).setEnabled(started);
		getCommand(idof[Instance:startAllUnitsCmd]).setEnabled(started);
		getCommand(idof[Instance:restartAllUnitsCmd]).setEnabled(started);
		getCommand(idof[Instance:applyOptionsFromFile]).setEnabled(started);
		getCommand(idof[Instance:actualizeVer]).setEnabled(started);
		getCommand(idof[Instance:reloadArtePool]).setEnabled(started);
		getCommand(idof[Instance:performMaintenance]).setEnabled(started);
		getCommand(idof[Instance:captureClassLoadingProfile]).setEnabled(started);


		if (!(getContext() instanceof Explorer.Context::SelectorRowContext)) {
		    targetExecutor.setVisible(targetExecutor.Value != null);
		    getCommand(idof[Instance:getInstanceStateReport]).setEnabled(started);
		    if (isEditorPageExists(idof[Instance:General:ArtePoolState])) {
		        getEditorPage(idof[Instance:General:ArtePoolState]).setVisible(started);
		    }
		    if (getView() != null) {
		        sensTraceFinishTime.afterModify();
		    }
		    aadcMemberId.setVisible(isAadcSysMember.Value == true);
		    aadcDgAddress.setMandatory(aadcMemberId.Value != null);
		    autoActualizeVer.setVisible(canEditActualizeVer.Value == true);
		    autoRestartDelaySec.setVisible(canEditActualizeVer.Value == true);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_StopAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_StopAllUnits")
	private final  void onCommand_StopAllUnits (org.radixware.ads.System.web.Instance.StopAllUnitsCmd command) {
		if(!Environment.messageConfirmation("Stop All Units", "Are you sure you want to stop ALL the instance units?"))
		  return;

		try{
		    InstanceControlServiceXsd:StopAllUnitsRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Environment.messageInformation("Stop All Units", "All units stopped");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		    	Environment.messageInformation("Stop All Units", "Stop of all units scheduled");
		    else
		  	Environment.messageError("Stop All Units", "Units stop error");
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_StartAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_StartAllUnits")
	private final  void onCommand_StartAllUnits (org.radixware.ads.System.web.Instance.StartAllUnitsCmd command) {
		try{
		    InstanceControlServiceXsd:StartAllUnitsRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		      Environment.messageInformation("Start All Units", "All units started");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		      Environment.messageInformation("Start All Units", "Start of all units scheduled");
		    else
		      Environment.messageError("Start All Units", "Error starting units");
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		} 
	}

	/*Radix::System::Instance:General:Model:onCommand_RestartAllUnits-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_RestartAllUnits")
	private final  void onCommand_RestartAllUnits (org.radixware.ads.System.web.Instance.RestartAllUnitsCmd command) {
		try{
		    InstanceControlServiceXsd:RestartAllUnitsRs output = command.send();
		    if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.DONE)
		        Environment.messageInformation("Restart All Units", "All units restarted");
		    else if(output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Environment.messageInformation("Restart All Units", "Restart of all units scheduled");
		    else
		  	Environment.messageError("Restart All Units", "Error restarting the units");
		}catch(org.radixware.kernel.common.exceptions.ServiceClientException e){
			showException(e);
		}catch(InterruptedException e){
			showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:afterCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:afterCreate")
	protected published  void afterCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if(warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}

	/*Radix::System::Instance:General:Model:afterUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:afterUpdate")
	protected published  void afterUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if(warning.Value != null) {
		    Environment.messageWarning(warning.Value);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_ShowUsedAddresses-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_ShowUsedAddresses")
	private final  void onCommand_ShowUsedAddresses (org.radixware.ads.System.web.Instance.ShowUsedPortsCmd command) {
		try{
		    Acs::CommandsXsd:StrValueDocument xOut = command.send();
		    StringBuilder sb = new StringBuilder();
		    sb.append("<html>");
		    sb.append(xOut.StrValue);
		    sb.append("</html>");
		    getEnvironment().messageInformation("Used Ports",sb.toString());    
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_ActualizeVer-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_ActualizeVer")
	public  void onCommand_ActualizeVer (org.radixware.ads.System.web.Instance.ActualizeVer command) {
		try{
		    command.send();
		}catch(Exceptions::InterruptedException  e){
		    Environment.processException(e);
		}catch(Exceptions::ServiceClientException  e){
		    Environment.processException(e);
		}
	}

	/*Radix::System::Instance:General:Model:checkSapEditorsBeforeCreate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:checkSapEditorsBeforeCreate")
	protected published  boolean checkSapEditorsBeforeCreate () {
		if (sapEditor == null) {
		    getEditorPage(idof[Instance:General:Service]).setFocused();
		    try {
		        checkPropertyValues();
		    } catch (Exception ex) {
		        showException(ex);
		        return false;
		    }
		}
		return true;

	}

	/*Radix::System::Instance:General:Model:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:beforeCreate")
	protected published  boolean beforeCreate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (highArteInstCount.Value == 0) {
		    Explorer.Utils::Trace.warning(getEnvironment(), "Available number of ARTEs for normal or lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		    getEnvironment().messageWarning("Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		}
		return checkSapEditorsBeforeCreate() && super.beforeCreate();

	}

	/*Radix::System::Instance:General:Model:onCommand_reloadArtePool-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_reloadArtePool")
	public  void onCommand_reloadArtePool (org.radixware.ads.System.web.Instance.ReloadArtePool command) {
		try {
		    InstanceControlServiceXsd:ReloadArtePoolRs output = command.send();
		    if (output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Environment.messageInformation("Reload ARTE Pool", "ARTE pool reload scheduled");
		    else
		        Environment.messageError("Reload ARTE Pool", "Error");
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    //ignore, because in this case error message put in trace during processing request
		} catch (InterruptedException e) {
		    Environment.getTracer().warning(e.getLocalizedMessage());
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_performMaintenance-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_performMaintenance")
	public  void onCommand_performMaintenance (org.radixware.ads.System.web.Instance.PerformMaintenance command) {
		try {
		    InstanceControlServiceXsd:InstanceMaintenanceRs output = command.send();
		    if (output.Result == org.radixware.schemas.systeminstancecontrol.ActionStateEnum.SCHEDULED)
		        Environment.messageInformation(command.getTitle(), "Instance maintenance scheduled");
		    else
		        Environment.messageError(command.getTitle(), "Error");
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    showException(e);
		} catch (InterruptedException e) {
		    showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:checkPropertyValues-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:checkPropertyValues")
	protected published  void checkPropertyValues () throws org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException,org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException {
		super.checkPropertyValues();

		int maxArteCount = 0;
		if (highArteInstCount.Value != null) {
		    maxArteCount += highArteInstCount.Value.intValue();
		}
		if (arteCntAboveNormal.Value != null) {
		    maxArteCount += arteCntAboveNormal.Value.intValue();
		}
		if (arteCntHigh.Value != null) {
		    maxArteCount += arteCntHigh.Value.intValue();
		}
		if (arteCntVeryHigh.Value != null) {
		    maxArteCount += arteCntVeryHigh.Value.intValue();
		}
		if (arteCntCritical.Value != null) {
		    maxArteCount += arteCntCritical.Value.intValue();
		}

		if (maxArteCount < lowArteInstCount.Value.intValue()) {
		    throw new InvalidPropertyValueException(this, lowArteInstCount.Definition, Client.Validators::InvalidValueReason.Factory.createForTooBigValue(getEnvironment(), String.valueOf(maxArteCount)));
		}
	}

	/*Radix::System::Instance:General:Model:getEventContextId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:getEventContextId")
	public published  Str getEventContextId () {
		return String.valueOf(id.ValueObject);
	}

	/*Radix::System::Instance:General:Model:getEventContextType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:getEventContextType")
	public published  Str getEventContextType () {
		return Arte::EventContextType:SystemInstance.Value;
	}

	/*Radix::System::Instance:General:Model:onCommand_fillArteTable-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_fillArteTable")
	private final  void onCommand_fillArteTable (org.radixware.ads.System.web.Instance.FillArteTable command) {
		try {
		    command.send();
		} catch (org.radixware.kernel.common.exceptions.ServiceClientException e) {
		    //ignore, because in this case error message put in trace during processing request
		} catch (InterruptedException e) {
		    Environment.getTracer().warning(e.getLocalizedMessage());
		}
	}

	/*Radix::System::Instance:General:Model:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:beforeUpdate")
	protected published  boolean beforeUpdate () throws org.radixware.kernel.common.client.exceptions.ModelException {
		if (highArteInstCount.Value == 0) {
		    Explorer.Utils::Trace.warning(getEnvironment(), "Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		    getEnvironment().messageWarning("Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");    
		}
		return super.beforeUpdate();
	}

	/*Radix::System::Instance:General:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		if (commandDef.getId().equals(idof[Instance:actualizeVer])) {
		    return canEditActualizeVer.Value.booleanValue();
		}
		return super.isCommandAccessible(commandDef);
	}
	public final class PerformMaintenance extends org.radixware.ads.System.web.Instance.PerformMaintenance{
		protected PerformMaintenance(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_performMaintenance( this );
		}

	}

	public final class ActualizeVer extends org.radixware.ads.System.web.Instance.ActualizeVer{
		protected ActualizeVer(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_actualizeVer( this );
		}

	}

	public final class FillArteTable extends org.radixware.ads.System.web.Instance.FillArteTable{
		protected FillArteTable(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_fillArteTable( this );
		}

	}

	public final class ReloadArtePool extends org.radixware.ads.System.web.Instance.ReloadArtePool{
		protected ReloadArtePool(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_reloadArtePool( this );
		}

	}

	public final class StopAllUnitsCmd extends org.radixware.ads.System.web.Instance.StopAllUnitsCmd{
		protected StopAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_stopAllUnitsCmd( this );
		}

	}

	public final class ShowUsedPortsCmd extends org.radixware.ads.System.web.Instance.ShowUsedPortsCmd{
		protected ShowUsedPortsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_showUsedPortsCmd( this );
		}

	}

	public final class StartAllUnitsCmd extends org.radixware.ads.System.web.Instance.StartAllUnitsCmd{
		protected StartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_startAllUnitsCmd( this );
		}

	}

	public final class RestartAllUnitsCmd extends org.radixware.ads.System.web.Instance.RestartAllUnitsCmd{
		protected RestartAllUnitsCmd(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_restartAllUnitsCmd( this );
		}

	}



























}

/* Radix::System::Instance:General:Model - Web Meta*/

/*Radix::System::Instance:General:Model-Entity Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aem3M27YSVRRHNRDB5MAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Instance:General:Model:Properties-Properties*/
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

/* Radix::System::Instance:General:Service:View - Desktop Executable*/

/*Radix::System::Instance:General:Service:View-Custom Page Editor for Desktop*/

package org.radixware.ads.System.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Service:View")
public class View extends org.radixware.kernel.explorer.views.EditorPageView {
	final private com.trolltech.qt.gui.QFont DEFAULT_FONT = org.radixware.kernel.explorer.text.ExplorerTextOptions.Factory.getDefault().getQFont();
	public View EditorPageView;
	public View getEditorPageView(){ return EditorPageView;}
	public View(org.radixware.kernel.common.client.IClientEnvironment userSession,
	org.radixware.kernel.common.client.views.IView parent, org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page) {
		super(userSession,(org.radixware.kernel.explorer.views.IExplorerView)parent, page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model) {
		super.open(model);
		EditorPageView = this;
		EditorPageView.setObjectName("EditorPageView");
		EditorPageView.setGeometry(new com.trolltech.qt.core.QRect(0, 0, 611, 378));
		EditorPageView.setFont(DEFAULT_FONT);
		final com.trolltech.qt.gui.QVBoxLayout $layout1 = new com.trolltech.qt.gui.QVBoxLayout(EditorPageView);
		$layout1.setObjectName("verticalLayout");
		$layout1.setContentsMargins(9, 9, 9, 9);
		$layout1.setSizeConstraint(com.trolltech.qt.gui.QLayout.SizeConstraint.SetDefaultConstraint);
		$layout1.setSpacing(6);
		EditorPageView.opened.connect(model, "mthSFW3ZMTUWBCJ3AX3RV464MSHAE(com.trolltech.qt.gui.QWidget)");
		EditorPageView.closed.connect(model, "mthWIHLY4PXSVF2POUQSBRN4TOEVI()");
		opened.emit(this);
	}
	public final org.radixware.ads.System.explorer.General:Model getModel() {
		return (org.radixware.ads.System.explorer.General:Model) super.getModel();
	}

}

/* Radix::System::Instance:General:Service:WebView - Web Executable*/

/*Radix::System::Instance:General:Service:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.System.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Service:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::System::Instance:General:Service:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::System::Instance:General - Desktop Meta*/

/*Radix::System::Instance:General-Selector Presentation*/

package org.radixware.ads.System.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQBCIUYAERTNRDB5PAALOMT5GDM"),
		null,
		false,
		true,
		null,
		16800,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::System::Instance:General - Web Meta*/

/*Radix::System::Instance:General-Selector Presentation*/

package org.radixware.ads.System.web;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprBCFJSMAERTNRDB5PAALOMT5GDM"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec52CHFNO3EGWDBRCRAAIT4AGD7E"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),
		null,
		null,
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtQBCIUYAERTNRDB5PAALOMT5GDM"),
		null,
		false,
		true,
		null,
		16800,
		null,
		144,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("epr3M27YSVRRHNRDB5MAALOMT5GDM")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdSCFDPNGA3FGNPGSWPBLQV2SLZQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.RESIZE_BY_CONTENT,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colGERU3AQZS5VDBFCXAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.STRETCH,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdUCGYPGLYTBCYPMABIDJDXDXV3Y"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFJIT2NHUT5VDBFGXAAUMFADAIA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFLKGBMZFOFF6FOQIHRF5OZWBPI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.AUTO,null)
		};
	}
}
/* Radix::System::Instance:General:Model - Desktop Executable*/

/*Radix::System::Instance:General:Model-Group Model Class*/

package org.radixware.ads.System.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model")
public class General:Model  extends org.radixware.ads.System.explorer.Instance.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::Instance:General:Model:Nested classes-Nested Classes*/

	/*Radix::System::Instance:General:Model:Properties-Properties*/

	/*Radix::System::Instance:General:Model:Methods-Methods*/

	/*Radix::System::Instance:General:Model:onCommand_RestartInstances-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_RestartInstances")
	public  void onCommand_RestartInstances (org.radixware.ads.System.explorer.InstanceGroup.RestartInstances command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_SwitchAppVersion-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_SwitchAppVersion")
	public  void onCommand_SwitchAppVersion (org.radixware.ads.System.explorer.InstanceGroup.SwitchAppVersion command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_StopInstances-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_StopInstances")
	public  void onCommand_StopInstances (org.radixware.ads.System.explorer.InstanceGroup.StopInstances command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}
	public final class RestartInstances extends org.radixware.ads.System.explorer.InstanceGroup.RestartInstances{
		protected RestartInstances(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_RestartInstances( this );
		}

	}

	public final class SwitchAppVersion extends org.radixware.ads.System.explorer.InstanceGroup.SwitchAppVersion{
		protected SwitchAppVersion(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_SwitchAppVersion( this );
		}

	}

	public final class StopInstances extends org.radixware.ads.System.explorer.InstanceGroup.StopInstances{
		protected StopInstances(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_StopInstances( this );
		}

	}

















}

/* Radix::System::Instance:General:Model - Desktop Meta*/

/*Radix::System::Instance:General:Model-Group Model Class*/

package org.radixware.ads.System.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmBCFJSMAERTNRDB5PAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Instance:General:Model:Properties-Properties*/
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

/* Radix::System::Instance:General:Model - Web Executable*/

/*Radix::System::Instance:General:Model-Group Model Class*/

package org.radixware.ads.System.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model")
public class General:Model  extends org.radixware.ads.System.web.Instance.DefaultGroupModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return General:Model_mi.rdxMeta; }



	public General:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

	/*Radix::System::Instance:General:Model:Nested classes-Nested Classes*/

	/*Radix::System::Instance:General:Model:Properties-Properties*/

	/*Radix::System::Instance:General:Model:Methods-Methods*/

	/*Radix::System::Instance:General:Model:onCommand_RestartInstances-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_RestartInstances")
	public  void onCommand_RestartInstances (org.radixware.ads.System.web.InstanceGroup.RestartInstances command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_SwitchAppVersion-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_SwitchAppVersion")
	public  void onCommand_SwitchAppVersion (org.radixware.ads.System.web.InstanceGroup.SwitchAppVersion command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}

	/*Radix::System::Instance:General:Model:onCommand_StopInstances-Command Handler Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::System::Instance:General:Model:onCommand_StopInstances")
	public  void onCommand_StopInstances (org.radixware.ads.System.web.InstanceGroup.StopInstances command) {
		try {
		    command.send();
		} catch (Exceptions::InterruptedException e) {
		    showException(e);
		} catch (Exceptions::ServiceClientException e) {
		    showException(e);
		}
	}
	public final class RestartInstances extends org.radixware.ads.System.web.InstanceGroup.RestartInstances{
		protected RestartInstances(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_RestartInstances( this );
		}

	}

	public final class SwitchAppVersion extends org.radixware.ads.System.web.InstanceGroup.SwitchAppVersion{
		protected SwitchAppVersion(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_SwitchAppVersion( this );
		}

	}

	public final class StopInstances extends org.radixware.ads.System.web.InstanceGroup.StopInstances{
		protected StopInstances(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}
		@Override
		public void execute( org.radixware.kernel.common.types.Id propertyId ) {
			onCommand_StopInstances( this );
		}

	}

















}

/* Radix::System::Instance:General:Model - Web Meta*/

/*Radix::System::Instance:General:Model-Group Model Class*/

package org.radixware.ads.System.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class General:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("agmBCFJSMAERTNRDB5PAALOMT5GDM"),
						"General:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::System::Instance:General:Model:Properties-Properties*/
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

/* Radix::System::Instance - Localizing Bundle */
package org.radixware.ads.System.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Instance - Localizing Bundle_mi{
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
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance Management Service");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сервис управления инстанцией");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls256NGNP7B5CAHENCHD32D7KFEU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"GUI trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки на экран");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls27UGHLZD5NG7RKVDX27XMCOSNE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2AFRKP3TDBGIXHS34SZUZKKWXI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<0>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<0>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XEAZYUJEREOVLFAKZMJJAVQKE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use Oracle Implicit Statement Cache");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать Oracle Implicit Statement Cache");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2XVB54JPJJD5FKJXAA4DHG74QU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Available number of ARTEs for normal or lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступное количество ARTE для запросов нормального или пониженного приоритета выставлено в нуль. Инстанция не сможет обслуживать запросы к модулям ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2YHMTNBYUZFLTNQUUQPE44SLPA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls34VYCPVJX5DXFGZRSG7HGZRD4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Oracle Implicit Statement Cache size");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Размер Oracle Implicit Statement Cache");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls35EJSP3CJ5AMFNQRAIVDT54SH4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls3CPCSBSV4NHUBJH6P2UGZK7WVU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error restarting the units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при перезапуске модулей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls47KI2FZNRBBUBKFIPDV4AM7IWI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Host IP addresses (comma-delimited)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"IP-адреса хоста (разделены запятой)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4EZJB6V3PRAQFIPZOFKFR622PI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reload ARTE Pool");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезагрузить ARTE Pool");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4GVBDUQW6JBRVLFFBL5EXRYY3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4OVMWH6DHFDENJU6XISLPLE2EY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Available number of ARTEs for normal or lower priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступное количество ARTE для запросов нормального или пониженного приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls4SOMLSZPBRFQVEJEZNNHALADRI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active ARTEs reserved for very high priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв активных ARTE для запросов очень высокого приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5B443SCPYZAS3FHDTQ4NLF2BJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reserved for high priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для запросов высокого приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5JFGOBLVGZG67EIKZ2P6ZLLEAI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5QEANBWVS5H37JCHEJDTPW3ZMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<0>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<0>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5UG737NJK5ACFCZW5RCB7QRBIE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Current number of ARTEs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Текущее количество ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5X2FHQRQPFARBMC66VYNPSS3CQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE pool reload scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезагрузка ARTE пула запланирована");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6GFXHWK3GJBYJKDGLPU7SACQIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Instance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6XXWGNYYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active ARTEs reserved for high priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв активных ARTE для запросов высокого приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7KWTXZ7RTBGHXLLCSOMNZGR6DM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Kernel code version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия кода ядра");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsA7CYYIAQBBETJI3JU3VR3IVSWU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<0>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<0>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAJDC3VPAKRD4JFTP3XIEEJP5NQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Create new trace files at the beginning of each day");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Создавать новые файлы трассировки в начале каждого дня");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAMF3NHHVJVHFBKCB3EEBP3R6M4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAYWM27QYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsAZ4PV5FFCNH2NPOG6ZBBE3R25U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE lifetime (min)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время жизни ARTE (мин)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsB7JIIV5BLJBD7KP6SJHU4K3JZQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Autoupdate runtime components");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Автообновление исполняемых компонент");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBBNN2FUK7BCNXHK73KJMKPOOZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<number of ARTEs for normal and lower priority requests>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<количество ARTE для запросов нормального и пониженного приоритета>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBYQFI2SW7VE6RCLJFOD6GZHT2Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Services\' client profile for instance units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль клиента сервисов для модулей инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBYSN6NRC75DDTHQRVDN5XZFSNA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Keystore file path");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файл хранилища ключей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsC34SDG7OPZEQXPFKS6TEPIVIXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pool Size");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Размер пула");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCEPT2RVGUZFRTPLU7BM6WLZ3QU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unknown>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<неизвестно>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCLW6XN4HWZB3BCGAMDIRSYYP64"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCYJOSHPDUNFY5HUFRZHZQ3KCRE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDSNH6LLVQRCK7KOCRRPZGHWCR4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<ARTEs reserved for high priority requests>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<резерв количества ARTE для запросов очень высокого приоритета>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsE7M63WS3URHJHL2M3SBHGIUORM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not defined>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не задан>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEAEBMIIYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEDFTYV2MPJCKTHRZ2GLJQJIVZY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the trusted certificate aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка доверенных сертификатов из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIKC2RFODVE6DOFCZ3ZXXWBPX4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Host IP addresses");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"IP-адреса хоста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEIKLMMDE7NEQJHHKJFGV2MKID4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last self-check time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последней самопроверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEJJZGBX3IRHAVP7DECEU3YFFTM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Threads state registration period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период регистрации состояния потоков (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEMKMU66PPJG3NFVQH3BUH5PI2A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Capture ARTE class loading profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Снять профиль загрузки классов в ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEYHX4RXAEFCL3HUKN23XPCGOJY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"time left:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"осталось:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFCWVJTX3NFCGXLALAN4CFDMIPM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Use active ARTE limits");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использовать лимиты на количество активных ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFDLLCNOLAFEWVD6LNG647RVZUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not to register>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не регистрировать>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFWFIABAFRJH7BMAPTRJZS4PTFY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Memory consumption check period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период проверки использования памяти (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsFYFPIIMFMBD5BIS5LSEHENYCMY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace file directory");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Каталог файлов трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGEPL4H3H3VAXHAON6PV36CML7A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"OS command on startup");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Команда ОС при запуске");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFVMK2IZSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<ARTEs reserved for above normal priority requests>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<резерв количества ARTE для запросов повышенного приоритета>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGIPBF54YVNBC3OXTULIWX3TIF4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reload ARTE Pool");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезагрузить ARTE Pool");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJRG2FWCSFHTLA4HCHCZR7S4B4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"OS command on stop");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Команда ОС при остановке");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGJVMK2IZSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Application code version");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Версия прикладного кода");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGPVMIPUDSRCRJN56RQYHGU2V4Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGUIY3QNHQ5HXVLMNA7AGNHYM7Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"HTTPS proxy");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"HTTPS прокси");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGXWXBOPA5ZFPBPHTKQCRAGQU2Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network settings of this instance have the following conflicts:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевые настройки данной инстанции имеют конфликты:");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsH722NXQR45APRL6V3TDGWVK2AE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading the key aliases from the keystore:");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка чтения списка ключей из хранилища: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHAF2GKLNXBB57GNBI3X3KMJO3I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum number of ARTEs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальное количество ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHAGMYJJ6VRDGTGZRG46SEWDD5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Keystore type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Тип хранилища ключей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHDVBTHYGRRCIPNG3SA3GT3KGJU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Memory check can include garbage collection that can cause performance degradation");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Проверка использования памяти может включать сборку мусора, которая может привести к снижению производительности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHMAAYM53MNA5TD56KGA6NZ7VY4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC member ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. узла AADC");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHN4LZUFJOJCNRNCO5N43FTGAAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHP6DLN6U3PORDOFEABIFNQAABA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Sensitive data tracing end time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время окончания трассировки чувствительных данных");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRRZW4W3JNDXXJD4IE5BVMHMDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Get Instance State Report");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получить отчёт о состоянии инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHRWB3W47ORCORNWE5LJX3Y225U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущена");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHTANRE6IIJB7BHDKLB6PTCNX24"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHXSBA5QYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHZK5ZQQD6RCSTM4DEK22RYEEIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Show Used Ports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Показать используемые порты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIWKI3VDRFVC3ZA3QNT5QXTHQFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"History");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"История");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ2SAJZEWG5CZXKPRJ6VSWMIUEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ34NQR2AHNE4HBWWF3MCZB4Q6M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"HTTP proxy");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"HTTP прокси");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ7KIBZJJXZHOZCCFGKTNZLGWG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Delay before restart on kernel upgrade (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задержка перед перезапуском при обновлении ядра (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJRWVUWS7HVC3VH2MIWKNQCQ4BA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Started First");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запущенные - в начале");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJWK3KXQYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used Ports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использованные порты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKNUY555QWVE6DBAOM4MGWBLKFA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKPQARQYQ2VDLXMHQJ77MELKG2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKUOQDRAUYZCTBPUYJCXRQW6ABM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Revision");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ревизия");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWMZC6XE3NDBJH22DNJJ3DEI7U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKWY6H2WEAJDPLIEEBKGIVIXNRY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error while discovering: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при определении: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKYI5VSQ5ZZFF5KO456YKMC77OA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL3HCYQ53KFFXLANE273IKEA4E4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Used Ports");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Использованные порты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL6MTUMVSCNFBXKBBKIHRNO5GTA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Host name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Имя хоста");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLB2GYJPR2NAGBEZPQO5DUZAGGI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unrestricted>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<без ограничений>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLCOZUPH3GNA43BCNANGFGGW62Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<tracing disabled>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<трассировка отключена>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLLMBUTT44VE5LNNG5365BHFSEQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Max number of active ARTEs for normal and lower priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество активных ARTE для запросов нормального и пониженного приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLO65EAYZJ5A2JFS2235KXKK3L4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All units restarted");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все модули перезапущены");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLVWO7MDMWVENRLJHLULOXACFEI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unable to determine because instance is inactive>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не удалось определить, поскольку инстанция неактивна>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLWONN37YPFDJPGEBTNI636A3EI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Uptime");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время работы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMIXJO7WUUNDZJIRMO45U2FOLK4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Database trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассы в БД");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMOR2TT376VGZDJ4MYS2RKRMTTY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"OS process PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"PID процесса ОС");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZCSYUK7ZFGBTLENATKOZ2DWLU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Edit");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Редактировать");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMZM3X7J43VHCVPQDM7UPYTON4M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Units stop error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при остановке модулей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN5PYNXLDINFATE24KHKG7BTRL4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступное количество ARTE для запросов нормального или пониженного приоритета выставлено в нуль. Инстанция не сможет обслуживать запросы к модулям ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNCKF4J3DLRFBXHTPDPYQ6R7IFM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Number of CPU cores");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Количество процессорных ядер");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNFXD5D3ULZD35AXQTECYBRT55U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNKFHSKWBOBCPZD7DUDFBP5VPGY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNKOD4L3WZJEEXGB24BAEYOTSRA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNYAUVPBORBA4XNMLPV3NAL3L5I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Trace Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки трассы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO2M4CNS3O5EOHBAE3NYFRDLZ6Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Kerberos keys file (keytab file)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Файл ключей Kerberos (keytab-файл)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOADIICLERFHS3LN5XEPACZLRSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<ARTEs reserved for critical priority requests>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<резерв количества ARTE для запросов критического приоритета>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOBTO6VHTRNGGTGKW5KJCO6D2GA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	@SuppressWarnings("unused")
	private static void loadStrings2(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<unlimited>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не ограничено>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOL3OKSUKOBD3VCAFHPCAIR7BCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOMWEDRBCCZFWZJ5Q6HFNRGBSG4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Address conflicts check error: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при проверке адресов на конфликты: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsONT4BKKM7VCXLHN325PJVO7GVM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start of all units scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запуск всех модулей запланирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOTPRVA344VFJFHFLR6HOTMJD5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Services\' client profile for instance units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль клиента сервисов для модулей инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOXLRPPSZ75GJZKE4LROFLCCUNY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Performance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Производительность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsP44YK2O2FBGQHA72VSIJEZ75MQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Are you sure you want to stop ALL the instance units?");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вы уверены, что хотите остановить ВСЕ модули этой инстанции?");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPBNMXH2KNJEYDIF4GQ2ZNHOSHI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum number of trace files");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальное количество файлов трассировки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPMPOSUEDVBCCJE7LSWHJFCDJIU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Self-check time");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время самопроверки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPSWSWMAGHFEX7FZBUIZBI2A5YA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPZAPYPPJ7FGURIGIRMD7SHI65E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<ARTEs reserved for high priority requests>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<резерв количества ARTE для запросов высокого приоритета>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQNJRHADXRVFZLCFBYSEEXIA7XU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"File trace profile");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Профиль трассировки в файл");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQSTFVLVJQRDHXIB6MDYFL4SZHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Start All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Запустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQV4JTY7MRVHXNJCIQP3Q7T5S5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reserved for above normal priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для запросов повышенного приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRM55QIJUMBEBPFEWUKCEGCGCFU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error starting units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при запуске модулей");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSBLFZ4IO75ELBJKEY2H6NZSFEA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступное количество ARTE для запросов нормального или пониженного приоритета выставлено в нуль. Инстанция не сможет обслуживать запросы к модулям ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSPCVBZHVFJFLXFMZLOBEVBVXOA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Available ARTE count for normal and lower priority requests is set to zero. The instance will not be able to serve requests to ARTE units.");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Доступное количество ARTE для запросов нормального или пониженного приоритета выставлено в нуль. Инстанция не сможет обслуживать запросы к модулям ARTE.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSRUU4USHN5AUTECK6VQVNSLNQA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<any>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<любые>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSS7F4MOTAFEPZJMUNXPPAIYNII"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<do not check>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не проверять>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsSSLL5K7DA5BAXEVOXDNZGDOWB4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Update Runtime Components");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обновить исполняемые компоненты");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsT3TSRPYMCRF3LJ5EIGJFO5IR5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance is using local Job Executor");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция использует локальный Исполнитель Заданий");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTS4SNFXWUJAUXPPLMGBBEDKRDY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reserved for very high priority requests ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для запросов очень высокого приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTUHMGQLCEVAF5HQKVW64XU27SU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance activity status");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние активности инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUKGELSTPPVG3RMBHSS7AXUKW2E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Settings");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Настройки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsULY54RII55CMJHLLTWUMABXWS4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All units started");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все модули запущены");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUNBNG23VS5BCNLZCG3OXFKFUDA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"SAP ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. SAP");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsURH44I562NG25AKUUKACQEIYYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"All units stopped");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Все модули остановлены");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUSOLFTXGINDZTIZPLBDBGAIKQE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"System Instances");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанции системы");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsUYDG2SQYSHOBDCKRAALOMT5GDM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7G6XDBJINBRFIUI3WV24JDY5M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Storage period for thread state history (days)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Срок хранения истории состояния потоков (суток)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVCFTW5WON5GDLCZLPYBKSFTLIA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Threads state forced registration period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период принудительной регистрации состояния потоков (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVL23IIZ34FCN5MG6J7OWSKL6WI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<not to register>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<не регистрировать>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVOUNSTMCSFEUZC7AB37CYIUJHU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<0>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<0>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVPUL6YTXZJAZROVXEKN375S7DE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Apply Settings from Configuration File");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Применить настройки из файла конфигурации");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVU3DNZJVNBC3BG4IPM3NGZNVZU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active ARTEs reserved for above normal priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв активных ARTE для запросов повышенного приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsW67PPWIVA5EKXCR6T74XIHB7TU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Average number of active ARTEs");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Среднее количество активных ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWF7HHSS2RVEHJL64Y6DH2QA3FY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Stop of all units scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Остановка всех модулей запланирована");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWIKXBOVYXNEY5BWUHZHP4R6KYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reserved for critical priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв для запросов критического приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWLDAJAN2OVDI5IQGJNUKQKAOKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Monitoring");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Мониторинг");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWMZJKU2UDNAMJOB4S4HKWYGWLA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Activity Limits");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Лимиты на активность");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPHVLDJZ5FGBTDANMJ27HP7FPE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Reload ARTE Pool");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезагрузить ARTE Pool");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWPKYZCZPOVH4TDE3EVHR33RQ54"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Active ARTEs reserved for critical priority requests");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Резерв активных ARTE для запросов критического приоритета");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6POZVFOIZEQXHCVG7T576S66Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance maintenance scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Обслуживание инстанции запланировано");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsX6Y3EYA4BZBZTKSJ3XUMYVV4UQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"AADC Data Grid IP address");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"IP адрес AADC Data Grid");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXVF5JGZRMVCV7DSKPLTXKAF3UM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Perform Instance Maintenance");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Выполнить обслуживание инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXZQJ47IOWZEAFNOR35NW7IDSWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart of all units scheduled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапуск всех модулей запланирован");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsY4SOYCMYVNEI3ADFIKXTKEZ6DM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Restart All Units");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Перезапустить все модули");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYDJ744CB55B7BA2WWVNBRWBT2M"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Maximum size of trace file (KB)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Максимальный размер файла трассировки (Кб)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsYWANGGUTVRGFDJDM3KVGMVDOYU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"ARTE Pool");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пул ARTE");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZDK6I7E4Z5FP3E46SCJKDBOMSI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<default>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<по умолчанию>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZN7D6K4DGJAMTOAZJRNW22QVMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,"null"));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Instance - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaec52CHFNO3EGWDBRCRAAIT4AGD7E"),"Instance - Localizing Bundle",$$$items$$$);
}
