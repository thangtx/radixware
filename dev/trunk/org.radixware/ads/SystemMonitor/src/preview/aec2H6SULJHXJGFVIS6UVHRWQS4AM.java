
/* Radix::SystemMonitor::MetricType - Server Executable*/

/*Radix::SystemMonitor::MetricType-Entity Class*/

package org.radixware.ads.SystemMonitor.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType")
public abstract published class MetricType  extends org.radixware.ads.Types.server.Entity  implements org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {


	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return MetricType_mi.rdxMeta;}

	/*Radix::SystemMonitor::MetricType:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType:classGuid-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:classGuid")
	public published  Str getClassGuid() {
		return classGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:classGuid")
	public published   void setClassGuid(Str val) {
		classGuid = val;
	}

	/*Radix::SystemMonitor::MetricType:errorDelay-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:errorDelay")
	public published  Int getErrorDelay() {
		return errorDelay;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:errorDelay")
	public published   void setErrorDelay(Int val) {
		errorDelay = val;
	}

	/*Radix::SystemMonitor::MetricType:esclationDelay-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:esclationDelay")
	public published  Int getEsclationDelay() {
		return esclationDelay;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:esclationDelay")
	public published   void setEsclationDelay(Int val) {
		esclationDelay = val;
	}

	/*Radix::SystemMonitor::MetricType:highErrorVal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highErrorVal")
	public published  Num getHighErrorVal() {
		return highErrorVal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highErrorVal")
	public published   void setHighErrorVal(Num val) {
		highErrorVal = val;
	}

	/*Radix::SystemMonitor::MetricType:highWarnVal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highWarnVal")
	public published  Num getHighWarnVal() {
		return highWarnVal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highWarnVal")
	public published   void setHighWarnVal(Num val) {
		highWarnVal = val;
	}

	/*Radix::SystemMonitor::MetricType:id-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:id")
	public published  Int getId() {
		return id;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:id")
	public published   void setId(Int val) {
		id = val;
	}

	/*Radix::SystemMonitor::MetricType:kind-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kind")
	public published  Str getKind() {
		return kind;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kind")
	public published   void setKind(Str val) {
		kind = val;
	}

	/*Radix::SystemMonitor::MetricType:lastUpdateTime-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateTime")
	public published  java.sql.Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateTime")
	public published   void setLastUpdateTime(java.sql.Timestamp val) {
		lastUpdateTime = val;
	}

	/*Radix::SystemMonitor::MetricType:lastUpdateUser-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateUser")
	public published  Str getLastUpdateUser() {
		return lastUpdateUser;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateUser")
	public published   void setLastUpdateUser(Str val) {
		lastUpdateUser = val;
	}

	/*Radix::SystemMonitor::MetricType:lowErrorVal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowErrorVal")
	public published  Num getLowErrorVal() {
		return lowErrorVal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowErrorVal")
	public published   void setLowErrorVal(Num val) {
		lowErrorVal = val;
	}

	/*Radix::SystemMonitor::MetricType:lowWarnVal-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowWarnVal")
	public published  Num getLowWarnVal() {
		return lowWarnVal;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowWarnVal")
	public published   void setLowWarnVal(Num val) {
		lowWarnVal = val;
	}

	/*Radix::SystemMonitor::MetricType:notes-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:notes")
	public published  Str getNotes() {
		return notes;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:notes")
	public published   void setNotes(Str val) {
		notes = val;
	}

	/*Radix::SystemMonitor::MetricType:period-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:period")
	public published  Int getPeriod() {
		return period;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:period")
	public published   void setPeriod(Int val) {
		period = val;
	}

	/*Radix::SystemMonitor::MetricType:title-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:title")
	public published  Str getTitle() {
		return title;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:title")
	public published   void setTitle(Str val) {
		title = val;
	}

	/*Radix::SystemMonitor::MetricType:warnDelay-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:warnDelay")
	public published  Int getWarnDelay() {
		return warnDelay;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:warnDelay")
	public published   void setWarnDelay(Int val) {
		warnDelay = val;
	}

	/*Radix::SystemMonitor::MetricType:netChannelId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:netChannelId")
	public published  Int getNetChannelId() {
		return netChannelId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:netChannelId")
	public published   void setNetChannelId(Int val) {
		netChannelId = val;
	}

	/*Radix::SystemMonitor::MetricType:instanceId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:instanceId")
	public published  Int getInstanceId() {
		return instanceId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:instanceId")
	public published   void setInstanceId(Int val) {
		instanceId = val;
	}

	/*Radix::SystemMonitor::MetricType:unitId-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:unitId")
	public published  Int getUnitId() {
		return unitId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:unitId")
	public published   void setUnitId(Int val) {
		unitId = val;
	}

	/*Radix::SystemMonitor::MetricType:timingSection-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:timingSection")
	public published  org.radixware.ads.Profiler.common.TimingSection getTimingSection() {
		return timingSection;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:timingSection")
	public published   void setTimingSection(org.radixware.ads.Profiler.common.TimingSection val) {
		timingSection = val;
	}

	/*Radix::SystemMonitor::MetricType:serviceUri-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:serviceUri")
	public published  Str getServiceUri() {
		return serviceUri;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:serviceUri")
	public published   void setServiceUri(Str val) {
		serviceUri = val;
	}

	/*Radix::SystemMonitor::MetricType:enabled-Column-Based Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:enabled")
	public published  Bool getEnabled() {
		return enabled;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:enabled")
	public published   void setEnabled(Bool val) {
		enabled = val;
	}

	/*Radix::SystemMonitor::MetricType:kindTitle-Dynamic Property*/



	protected Str kindTitle=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kindTitle")
	public published  Str getKindTitle() {

		if (getKind() == null) {
		    return "<Not Defined>";
		}
		return getKind().Title;
	}

	/*Radix::SystemMonitor::MetricType:isPeriodSupportedVal-Dynamic Property*/



	protected Bool isPeriodSupportedVal=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:isPeriodSupportedVal")
	protected published  Bool getIsPeriodSupportedVal() {

		return isPeriodSupported();
	}

	/*Radix::SystemMonitor::MetricType:valueType-Dynamic Property*/



	protected org.radixware.ads.SystemMonitor.common.MetricValueType valueType=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:valueType")
	public published  org.radixware.ads.SystemMonitor.common.MetricValueType getValueType() {

		return getValueType();
	}

	/*Radix::SystemMonitor::MetricType:measurePeriodStr-Dynamic Property*/



	protected Str measurePeriodStr=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:measurePeriodStr")
	public published  Str getMeasurePeriodStr() {

		if (isPeriodSupported()) {
		    return String.valueOf(period);
		} else {
		    return "<none>";
		}
	}

	/*Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy")
	public published  org.radixware.kernel.common.enums.EEventSeverity getMinSeverityForSnmpNtfy() {
		return minSeverityForSnmpNtfy;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy")
	public published   void setMinSeverityForSnmpNtfy(org.radixware.kernel.common.enums.EEventSeverity val) {
		minSeverityForSnmpNtfy = val;
	}

	/*Radix::SystemMonitor::MetricType:controlledValue-Column-Based Property*/








			















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:controlledValue")
	public published  org.radixware.ads.SystemMonitor.common.ControlledValue getControlledValue() {
		return controlledValue;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:controlledValue")
	public published   void setControlledValue(org.radixware.ads.SystemMonitor.common.ControlledValue val) {
		controlledValue = val;
	}

	/*Radix::SystemMonitor::MetricType:propControlledValEditable-Dynamic Property*/



	protected Bool propControlledValEditable=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:propControlledValEditable")
	protected  Bool getPropControlledValEditable() {

		return isControlledValueEditable();
	}

















































































































































































	/*Radix::SystemMonitor::MetricType:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType:loadByPidStr-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:loadByPidStr")
	public static published  org.radixware.ads.SystemMonitor.server.MetricType loadByPidStr (Str pidAsStr, boolean checkExistance) {
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),pidAsStr);
		try{
		return (
		org.radixware.ads.SystemMonitor.server.MetricType) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::SystemMonitor::MetricType:loadByPK-System Method*/

	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:loadByPK")
	public static published  org.radixware.ads.SystemMonitor.server.MetricType loadByPK (Int id, boolean checkExistance) {
	final java.util.HashMap<org.radixware.kernel.common.types.Id,Object> pkValsMap = new java.util.HashMap<org.radixware.kernel.common.types.Id,Object>(5);
			if(id==null) return null;
			pkValsMap.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),id);
	org.radixware.kernel.server.types.Pid pid = new org.radixware.kernel.server.types.Pid(org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal(),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),pkValsMap);
		try{
		return (
		org.radixware.ads.SystemMonitor.server.MetricType) org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcArte______________________.__getArteInstanceInternal().getEntityObject(pid,null,checkExistance);
	}catch(org.radixware.kernel.server.exceptions.EntityObjectNotExistsError e){
	return null;
	}

	}

	/*Radix::SystemMonitor::MetricType:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		kind = getKind().Value;
		lastUpdateTime = new java.sql.Timestamp(System.currentTimeMillis());
		lastUpdateUser = Arte::Arte.getUserName();
		checkDuplicates();
		return true;
	}

	/*Radix::SystemMonitor::MetricType:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:beforeUpdate")
	protected published  boolean beforeUpdate () {
		checkDuplicates();
		lastUpdateTime = new DateTime(System.currentTimeMillis());
		if (Arte::Arte.getUserName() != null && !Arte::Arte.getUserName().isEmpty()) {
		    lastUpdateUser = Arte::Arte.getUserName();
		} else {
		    lastUpdateUser = "<none>";
		}
		return super.beforeUpdate();
	}

	/*Radix::SystemMonitor::MetricType:getErrorCode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getErrorCode")
	public published  Str getErrorCode () {
		return eventCode["Metric '%1' of type '%2' for sensor '%9' has entered the error range with value '%3' at %8"];
	}

	/*Radix::SystemMonitor::MetricType:getEscalationCode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getEscalationCode")
	public published  Str getEscalationCode () {
		return eventCode["Metric '%1' of type '%2' for sensor '%9' has been in the warning range for more than '%3' seconds at %8"];
	}

	/*Radix::SystemMonitor::MetricType:getReturnToNormalCode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getReturnToNormalCode")
	public published  Str getReturnToNormalCode () {
		return eventCode["Metric '%1' of type '%2' for sensor '%9' has returned to the normal range with value '%3' at %8"];
	}

	/*Radix::SystemMonitor::MetricType:getReturnToWarningCode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getReturnToWarningCode")
	public published  Str getReturnToWarningCode () {
		return eventCode["Metric '%1' of type '%2' for sensor '%9' has returned to the warning range with value '%3' at %8"];
	}

	/*Radix::SystemMonitor::MetricType:getWarningCode-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getWarningCode")
	public published  Str getWarningCode () {
		return eventCode["Metric '%1' of type '%2' for sensor '%9' has entered the warning range with value '%3' at %8"];
	}

	/*Radix::SystemMonitor::MetricType:checkDuplicates-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:checkDuplicates")
	protected published  void checkDuplicates () {
		if (!shouldCheckForDuplicates()) {
		    return;
		}

		MetricDuplicateCursor duplicateCursor = MetricDuplicateCursor.open(id, kind, instanceId, unitId, serviceUri, timingSection == null ? null : timingSection.Value, netChannelId);
		try {
		    if (duplicateCursor.next() && duplicateCursor.count != null && duplicateCursor.count.longValue() > 0) {
		        throw new AppError("Same metric already exists");
		    }
		} finally {
		    duplicateCursor.close();
		}

	}

	/*Radix::SystemMonitor::MetricType:getKind-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getKind")
	public abstract published  org.radixware.ads.SystemMonitor.common.MetricKind getKind ();

	/*Radix::SystemMonitor::MetricType:getSensorType-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getSensorType")
	public abstract published  org.radixware.kernel.common.enums.ESensorType getSensorType ();

	/*Radix::SystemMonitor::MetricType:getValueType-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:getValueType")
	protected published  org.radixware.ads.SystemMonitor.common.MetricValueType getValueType () {
		return MetricCommonUtils.getMetricValueType(getKind());
	}

	/*Radix::SystemMonitor::MetricType:isPeriodSupported-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:isPeriodSupported")
	protected published  boolean isPeriodSupported () {
		return getKind().isInDomain(idof[MetricValueTypeDomain:Statistic]) || getKind().isInDomain(idof[MetricValueTypeDomain:Event:Point]);
	}

	/*Radix::SystemMonitor::MetricType:shouldCheckForDuplicates-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:shouldCheckForDuplicates")
	protected published  boolean shouldCheckForDuplicates () {
		return true;
	}

	/*Radix::SystemMonitor::MetricType:calcEditorPresentationForState-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:calcEditorPresentationForState")
	protected published  org.radixware.kernel.common.types.Id calcEditorPresentationForState () {
		//point
		if (getSensorType() == SensorType:NONE && getKind().isInDomain(idof[MetricValueTypeDomain:Event:Point])) {
		    return idof[MetricState:PointNone];
		}

		if (getSensorType() == SensorType:INSTANCE && getKind().isInDomain(idof[MetricValueTypeDomain:Event:Point])) {
		    return idof[MetricState:PointInstance];
		}

		if (getSensorType() == SensorType:UNIT && getKind().isInDomain(idof[MetricValueTypeDomain:Event:Point])) {
		    return idof[MetricState:PointUnit];
		}

		if (getSensorType() == SensorType:INSTANCE_SERVICE && getKind().isInDomain(idof[MetricValueTypeDomain:Event:Point])) {
		    return idof[MetricState:PointInstanceService];
		}

		if (getSensorType() == SensorType:NET_CHANNEL && getKind().isInDomain(idof[MetricValueTypeDomain:Event:Point])) {
		    return idof[MetricState:PointNetChannel];
		}

		//event
		if (getSensorType() == SensorType:NONE && getKind().isInDomain(idof[MetricValueTypeDomain:Event])) {
		    return idof[MetricState:EventNone];
		}

		if (getSensorType() == SensorType:INSTANCE && getKind().isInDomain(idof[MetricValueTypeDomain:Event])) {
		    return idof[MetricState:EventInstance];
		}

		if (getSensorType() == SensorType:UNIT && getKind().isInDomain(idof[MetricValueTypeDomain:Event])) {
		    return idof[MetricState:EventUnit];
		}

		if (getSensorType() == SensorType:INSTANCE_SERVICE && getKind().isInDomain(idof[MetricValueTypeDomain:Event])) {
		    return idof[MetricState:EventInstanceService];
		}

		if (getSensorType() == SensorType:NET_CHANNEL && getKind().isInDomain(idof[MetricValueTypeDomain:Event])) {
		    return idof[MetricState:EventNetChannel];
		}

		//statistic
		if (getSensorType() == SensorType:NONE && getKind().isInDomain(idof[MetricValueTypeDomain:Statistic])) {
		    return idof[MetricState:StatisticNone];
		}

		if (getSensorType() == SensorType:INSTANCE && getKind().isInDomain(idof[MetricValueTypeDomain:Statistic])) {
		    return idof[MetricState:StatisticInstance];
		}

		if (getSensorType() == SensorType:UNIT && getKind().isInDomain(idof[MetricValueTypeDomain:Statistic])) {
		    return idof[MetricState:StatisticUnit];
		}

		if (getSensorType() == SensorType:INSTANCE_SERVICE && getKind().isInDomain(idof[MetricValueTypeDomain:Statistic])) {
		    return idof[MetricState:StatisticInstanceService];
		}

		if (getSensorType() == SensorType:NET_CHANNEL && getKind().isInDomain(idof[MetricValueTypeDomain:Statistic])) {
		    return idof[MetricState:StatisticNetChannel];
		}

		return idof[MetricState:General];
	}

	/*Radix::SystemMonitor::MetricType:calcSensorTitle-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:calcSensorTitle")
	protected published  Str calcSensorTitle (org.radixware.ads.SystemMonitor.server.MetricState metricState) {
		if (metricState == null) {
		    return null;
		}

		SensorType sensorType = getSensorType();

		if (sensorType.equals(SensorType:INSTANCE)) {
		    System::Instance instance = System::Instance.loadByPK(metricState.instanceId, true);
		    return instance == null ? null : "Instance: " + instance.calcTitle();

		} else if (sensorType.equals(SensorType:INSTANCE_SERVICE)) {
		    System::Instance instance = System::Instance.loadByPK(metricState.instanceId, true);
		    if (instance != null) {
		        return "Instance: " + instance.calcTitle() + "; " + "Service URI: " + metricState.serviceUri;
		    }
		    return metricState.serviceUri;

		} else if (sensorType.equals(SensorType:UNIT)) {
		    System::Unit unit = System::Unit.loadByPK(metricState.unitId, true);
		    return unit == null ? null : "Unit: " + unit.calcTitle();

		} else if (sensorType.equals(SensorType:NET_CHANNEL)) {
		    Net::NetChannel netChannel = Net::NetChannel.loadByPK(metricState.netChannelId, true);
		    return netChannel == null ? null : "Network Channel: " + netChannel.calcTitle();

		} else if (sensorType.equals(SensorType:TIMING_SECTION)) {
		    System::Instance instance = System::Instance.loadByPK(metricState.instanceId, true);
		    if (instance != null) {
		        return "Instance: " + instance.calcTitle() + "; " + "Time Section: " + timingSection.Value;
		    }
		    return timingSection.Value;
		}
		return null;
	}

	/*Radix::SystemMonitor::MetricType:isControlledValueEditable-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:isControlledValueEditable")
	protected published  boolean isControlledValueEditable () {
		return getKind().isInDomain(idof[MetricValueTypeDomain:Statistic]);
	}

	/*Radix::SystemMonitor::MetricType:isCalculationTaskRequired-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:isCalculationTaskRequired")
	protected published  boolean isCalculationTaskRequired () {
		return false;
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::SystemMonitor::MetricType - Server Meta*/

/*Radix::SystemMonitor::MetricType-Entity Class*/

package org.radixware.ads.SystemMonitor.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),"MetricType",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),

						org.radixware.kernel.common.enums.EClassType.ENTITY,

						/*Radix::SystemMonitor::MetricType:Presentations-Entity Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
							/*Owner Class Name*/
							"MetricType",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),
							/*Property presentations*/

							/*Radix::SystemMonitor::MetricType:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::SystemMonitor::MetricType:classGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI36OSBML5D73KDJOQKMSMISQI"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:errorDelay:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWD3JCD37ZDAVKKCLTUEPMO3TQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:esclationDelay:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4YKCVXTHZVETTGSLJZFJB6PPEY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:highErrorVal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:highWarnVal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:id:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:kind:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJNBDFWIVG47OCFOCMCXVBFXI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:lastUpdateTime:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFAATRVDUDJBSLA2GVBLSKLS3EA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:lastUpdateUser:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYUWJY5SQSZEHZM7TT2SNIIB54Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:lowErrorVal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:lowWarnVal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:notes:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUH6LIE6A55C5BEX56Y7GPPZMHQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:period:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col773ODXVAM5DIZIORVOBGIBYK7Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:warnDelay:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5HMBXQKAZBVTJSJ5YVNSGCR5Y"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:netChannelId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:instanceId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSR5Z6WKAZB4ZEAJQTDWKJA6J4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:unitId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:timingSection:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3MKU34INANH5XP7OKUZ64JQNF4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:serviceUri:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2GTEKTBT5A7RLD32JUJ7AZ52E"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:enabled:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:kindTitle:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:isPeriodSupportedVal:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB3E3XXUZSZD7RIIKG5IRNXZ7YA"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:valueType:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFKCD4PYVUNCZDPOQ57WUGMDXBI"),org.radixware.kernel.common.enums.EEditPossibility.NEVER,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:measurePeriodStr:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXD65NGMR4FASNAF5K2KSMQUO7I"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJERRR2BH2ZD2XGDYMYUS67KOKY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:controlledValue:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::SystemMonitor::MetricType:propControlledValEditable:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOXD6NG3ENHEVMON2CGXVHJ47A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT))
							},
							/*Commands*/
							new org.radixware.kernel.server.meta.presentations.RadCommandDef[]{

									new org.radixware.kernel.server.meta.presentations.RadCommandDef(
									/*Radix::SystemMonitor::MetricType:calcMetric-Object Command*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIUH3LXIN2ND4LFNS2EN4EB25N4"),"calcMetric",org.radixware.kernel.common.enums.ECommandScope.OBJECT,null,org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),false,true)
							},
							/*Sortings*/
							new org.radixware.kernel.server.meta.presentations.RadSortingDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSortingDef(
									/*Radix::SystemMonitor::MetricType:ID-Sorting*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEX6MHATMINDDBCNIA4U62BBBCU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),"ID",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWUMURLZAJNEHJKEBHOTCBELOKU"),new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item[]{

											new org.radixware.kernel.server.meta.presentations.RadSortingDef.Item(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),org.radixware.kernel.common.enums.EOrder.ASC)
									},"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware")
							},
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SystemMonitor::MetricType:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									null,
									144,

									/*Radix::SystemMonitor::MetricType:Edit:TitleFormat-Object Title Format*/

									new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem[]{

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),"{0}) ",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null),

											new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.TitleItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),"{0}",org.radixware.kernel.common.enums.ETitleNullFormat.SAME_AS_NOT_NULL,null)
									},null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.EDITOR_PRESENTATION),

									/*Radix::SystemMonitor::MetricType:Edit:Children-Explorer Items*/
										new org.radixware.kernel.server.meta.presentations.RadExplorerItemDef[]{

												/*Radix::SystemMonitor::MetricType:Edit:MetricState-Child Ref Explorer Item*/

												new org.radixware.kernel.server.meta.presentations.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi6B74WDD67FDR3FNAC23XRLBY6Y"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aec4QTN5SWVEFB55HPL32XOX6KZ7Y"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM576QUTA55AYZDLJED3BWD4LOU"),
													new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),
													org.radixware.kernel.common.types.Id.Factory.loadFrom("refPKWMUTUDJZBOTDIDPVBV5O4NFA"),
													null,
													null)
										}
									,
									org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null,null),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::SystemMonitor::MetricType:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
									36018,
									null,

									/*Radix::SystemMonitor::MetricType:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.NONE,
									null)
							},
							/*Selector presentations*/
							new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef(
									/*Radix::SystemMonitor::MetricType:General-Selector Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUT72FHD2WBCJ3BFZAKTCGS4WKU"),"General",null,128,new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn[]{

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB3E3XXUZSZD7RIIKG5IRNXZ7YA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJNBDFWIVG47OCFOCMCXVBFXI"),false),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXD65NGMR4FASNAF5K2KSMQUO7I"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSR5Z6WKAZB4ZEAJQTDWKJA6J4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3MKU34INANH5XP7OKUZ64JQNF4"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2GTEKTBT5A7RLD32JUJ7AZ52E"),true),

											new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),true)
									},new org.radixware.kernel.server.meta.presentations.RadConditionDef(null,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.server.meta.presentations.RadSelectorPresentationDef.Addons(org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEX6MHATMINDDBCNIA4U62BBBCU"),true,null,true,null,true,null,false,true,"<xsc:Sqml xmlns:xsc=\"http://schemas.radixware.org/xscml.xsd\"/>","org.radixware"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I")},org.radixware.kernel.server.types.Restrictions.Factory.newInstance(16424,null,null,null),org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"))
							},
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							null,
							/*Object title format*/

							/*Radix::SystemMonitor::MetricType:TitleFormat-Object Title Format*/

							new org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),null,null,org.radixware.kernel.server.meta.presentations.RadEntityTitleFormatDef.DefinitionContextType.ENTITY),
							/*Class catalogs*/
							new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog[]{

									new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog(
									/*Radix::SystemMonitor::MetricType:Common-Dynamic Class Catalog*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eccXISAEUV6HRA7JNQ453IS2OBSFI"),org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.ELinkMode.VIRTUAL,new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Item[]{
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctDQ3TJ6OI6FGBZAA7VFXHRLRHIY"),null,230.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QDDIQ7HCBEN3AXROOPXT2TOZE")),
										new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef.ClassCatalog.Topic(org.radixware.kernel.common.types.Id.Factory.loadFrom("cctF4SGPJDFJJFPFPAXLFD3GBKXTA"),null,205.0,org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNSJ355IUXFGRPGT72P5ZARET2I"))}
									)
							},
							/*Restrictions*/
							org.radixware.kernel.server.types.Restrictions.Factory.newInstance(0,null,null,null),null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("pdcEntity____________________"),

						/*Radix::SystemMonitor::MetricType:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::SystemMonitor::MetricType:classGuid-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI36OSBML5D73KDJOQKMSMISQI"),"classGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:errorDelay-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWD3JCD37ZDAVKKCLTUEPMO3TQ"),"errorDelay",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2FS2DTYP5E7BCLUK5DM5CS4FA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:esclationDelay-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4YKCVXTHZVETTGSLJZFJB6PPEY"),"esclationDelay",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCRNASPYS4VCZFNVEQR432YSJWY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:highErrorVal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),"highErrorVal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN2KR76BFLFD2HCVQ2NNPEZMQOM"),org.radixware.kernel.common.enums.EValType.NUM,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:highWarnVal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),"highWarnVal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZJX4CJT5UVEMRJAT3GR3VXMHCU"),org.radixware.kernel.common.enums.EValType.NUM,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:id-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),"id",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGUGVZI7TBBMDFKLOKOD3HDSXY"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:kind-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJNBDFWIVG47OCFOCMCXVBFXI"),"kind",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOIMMZMC5NFPBDXK5Y3QUZ6BSU"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:lastUpdateTime-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFAATRVDUDJBSLA2GVBLSKLS3EA"),"lastUpdateTime",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM54R4NS43RFXJBLO5ZZVX7L4GY"),org.radixware.kernel.common.enums.EValType.DATE_TIME,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:lastUpdateUser-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYUWJY5SQSZEHZM7TT2SNIIB54Y"),"lastUpdateUser",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJVLZM7JW5A27J6ANMEWJYWHCM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:lowErrorVal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),"lowErrorVal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGMUV43HR4FAGLAGIIJYFTHENAE"),org.radixware.kernel.common.enums.EValType.NUM,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:lowWarnVal-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),"lowWarnVal",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZXIDO3QKERDMLMM5YVP2OK372Q"),org.radixware.kernel.common.enums.EValType.NUM,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:notes-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUH6LIE6A55C5BEX56Y7GPPZMHQ"),"notes",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFGJV77YNFFCNEGFAPHMOKUDYY"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:period-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col773ODXVAM5DIZIORVOBGIBYK7Y"),"period",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ537FURE4JHURCQDBLZY4T275I"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("60")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDH7IOIXOV5BNJEDQEX3NWRZD5E"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:warnDelay-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5HMBXQKAZBVTJSJ5YVNSGCR5Y"),"warnDelay",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GSRKZTYRNFLNNY2U4G6WGUXOI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("0")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:netChannelId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),"netChannelId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJZ3VS3N3NHVHMFWWIG3DYBQYA"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:instanceId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSR5Z6WKAZB4ZEAJQTDWKJA6J4"),"instanceId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls37PH3BCOIRH3HGQQUE52KEHE5U"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:unitId-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),"unitId",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFDMM3C3ZJHQBCX4X3ANQN4ZVI"),org.radixware.kernel.common.enums.EValType.INT,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:timingSection-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3MKU34INANH5XP7OKUZ64JQNF4"),"timingSection",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EGPCVY3W5ABRJ2STVUB3RSSIY"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsJIXOZF6PJJAM5AZKRLYLYFA75E"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:serviceUri-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2GTEKTBT5A7RLD32JUJ7AZ52E"),"serviceUri",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG6VUV2Z3FCE5F5PLPE3R5VH3A"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:enabled-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),"enabled",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEG2FSL5ZMFETRKB4LKDHOV2NUY"),org.radixware.kernel.common.enums.EValType.BOOL,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:kindTitle-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),"kindTitle",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXOEKANACYZG63BPLOBKDXKHI6Q"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:isPeriodSupportedVal-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB3E3XXUZSZD7RIIKG5IRNXZ7YA"),"isPeriodSupportedVal",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:valueType-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFKCD4PYVUNCZDPOQ57WUGMDXBI"),"valueType",null,org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsJW5X3HYYBFFT3LECMFTSPIZYQE"),null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:measurePeriodStr-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXD65NGMR4FASNAF5K2KSMQUO7I"),"measurePeriodStr",org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6R3KDBTP55AGPK7IGW5RZF5W4A"),org.radixware.kernel.common.enums.EValType.STR,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJERRR2BH2ZD2XGDYMYUS67KOKY"),"minSeverityForSnmpNtfy",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKAEJDVDHJBNTAAY52YC2UJVJQ"),org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:controlledValue-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),"controlledValue",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBPAH5NSRRBDJJDYOYSW3W2BMU"),org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("MIN_MAX")),org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::SystemMonitor::MetricType:propControlledValEditable-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOXD6NG3ENHEVMON2CGXVHJ47A"),"propControlledValEditable",null,org.radixware.kernel.common.enums.EValType.BOOL,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE)
						},

						/*Radix::SystemMonitor::MetricType:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPidStr_____________"),"loadByPidStr",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pidAsStr",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHDCGB3KLJBDSZAQBJCP7PFXHJU")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprE55B2DOR6NHMRJOWAZKUG63HEY"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth_loadByPK_________________"),"loadByPK",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprLO27TGRO3FDPXDJZ5PHZQ3YUX4")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("checkExistance",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprBVYWNLK3D5G6ZM6JJIEOPAXKSU"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprP7QJ2EFH6FDIXKWLW5VBUFRH4Y"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6N5WAYWUEBEM3GFV6PIFCDH5VU"),"getErrorCode",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWUU2HGDSLFAS3J6FA47N42EDVE"),"getEscalationCode",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth5WR56WU3O5FIFJXLAI6I266MRQ"),"getReturnToNormalCode",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthINDOWT6J25F6BHK2JI5BYNQ2RA"),"getReturnToWarningCode",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthS2WHP4ZYBJDVLBUCW4G625IIZY"),"getWarningCode",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthISP7L3DDEZH6FGKNCBS6S3Q6XM"),"checkDuplicates",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSK64PGWTZNFZFAJVJ5NHXBAC6M"),"getKind",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7M6YK3J5OREYTFHKMRDGE42Z3Q"),"getSensorType",false,true,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.INT),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIT2CW4S27RB3XNEN22I7VDSY74"),"getValueType",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTGVVJKYZRREP5NQJBFZEVC4EUY"),"isPeriodSupported",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthF2QE73QYI5BKBNWCZIUEFSE2C4"),"shouldCheckForDuplicates",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEPCXNP4FHNEIBGSCCJO5DLDTIA"),"calcEditorPresentationForState",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZUYHLBWEIFFSTENQDPACAIEAZA"),"calcSensorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("metricState",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHVD73EAVYRH45ELGEXVZKG6ITI"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWWMO5AMVONHYFPXVGHKONUKSPY"),"isControlledValueEditable",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIBIAYL5YD5CIVMPIUHJV4NRAYA"),"isCalculationTaskRequired",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE)
						},
						null,
						org.radixware.kernel.common.enums.EAccessAreaType.NONE,null,null,false);
}

/* Radix::SystemMonitor::MetricType - Desktop Executable*/

/*Radix::SystemMonitor::MetricType-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType")
public interface MetricType {
	/*Group model adapter class. This is the base class for 
	all of selector presentation models of this entity*/
	public static class DefaultGroupModel extends org.radixware.kernel.common.client.models.GroupModel{
		public DefaultGroupModel(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadSelectorPresentationDef def){super(userSession,def);}

		/*Radix::SystemMonitor::MetricTypeGroup:unitIdParam:unitIdParam-Presentation Property*/




		public class UnitIdParam extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public UnitIdParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:unitIdParam:unitIdParam")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:unitIdParam:unitIdParam")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public UnitIdParam getUnitIdParam(){return (UnitIdParam)getProperty(pgpNVVT2YLWSNHGNMVW2UIEXEDFXU);}

		/*Radix::SystemMonitor::MetricTypeGroup:kindParam:kindParam-Presentation Property*/




		public class KindParam extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
			public KindParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:kindParam:kindParam")
			public  Str getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:kindParam:kindParam")
			public   void setValue(Str val) {
				Value = val;
			}
		}
		public KindParam getKindParam(){return (KindParam)getProperty(pgpDS45GNBCMJG6RLIBCFY644CY2Y);}

		/*Radix::SystemMonitor::MetricTypeGroup:instanceIdParam:instanceIdParam-Presentation Property*/




		public class InstanceIdParam extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public InstanceIdParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:instanceIdParam:instanceIdParam")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:instanceIdParam:instanceIdParam")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public InstanceIdParam getInstanceIdParam(){return (InstanceIdParam)getProperty(pgpLWQCZ2SMP5BUPFGXW3EFHCDAMU);}

		/*Radix::SystemMonitor::MetricTypeGroup:netListenerParam:netListenerParam-Presentation Property*/




		public class NetListenerParam extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public NetListenerParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:netListenerParam:netListenerParam")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:netListenerParam:netListenerParam")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public NetListenerParam getNetListenerParam(){return (NetListenerParam)getProperty(pgpEYKUE52BIZHSDN7YTX6VHFHATU);}

		/*Radix::SystemMonitor::MetricTypeGroup:netClientParam:netClientParam-Presentation Property*/




		public class NetClientParam extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
			public NetClientParam(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:netClientParam:netClientParam")
			public  Int getValue() {
				return Value;
			}

			@SuppressWarnings({"unused","unchecked"})
			@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricTypeGroup:netClientParam:netClientParam")
			public   void setValue(Int val) {
				Value = val;
			}
		}
		public NetClientParam getNetClientParam(){return (NetClientParam)getProperty(pgpOJGN4PGQIVDQPBVOMCFL3N4DXE);}















		public org.radixware.ads.SystemMonitor.explorer.MetricType.MetricType_DefaultModel getEntity(int i) throws InterruptedException, org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException, org.radixware.kernel.common.exceptions.ServiceClientException { return (org.radixware.ads.SystemMonitor.explorer.MetricType.MetricType_DefaultModel )  super.getEntity(i);}
	}







































































































































































	/*Radix::SystemMonitor::MetricType:kind:kind-Presentation Property*/


	public class Kind extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public Kind(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kind:kind")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kind:kind")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Kind getKind();
	/*Radix::SystemMonitor::MetricType:highErrorVal:highErrorVal-Presentation Property*/


	public class HighErrorVal extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public HighErrorVal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highErrorVal:highErrorVal")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highErrorVal:highErrorVal")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public HighErrorVal getHighErrorVal();
	/*Radix::SystemMonitor::MetricType:timingSection:timingSection-Presentation Property*/


	public class TimingSection extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public TimingSection(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.Profiler.common.TimingSection dummy = x == null ? null : (x instanceof org.radixware.ads.Profiler.common.TimingSection ? (org.radixware.ads.Profiler.common.TimingSection)x : org.radixware.ads.Profiler.common.TimingSection.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.Profiler.common.TimingSection> getValClass(){
			return org.radixware.ads.Profiler.common.TimingSection.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.Profiler.common.TimingSection dummy = x == null ? null : (x instanceof org.radixware.ads.Profiler.common.TimingSection ? (org.radixware.ads.Profiler.common.TimingSection)x : org.radixware.ads.Profiler.common.TimingSection.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:timingSection:timingSection")
		public  org.radixware.ads.Profiler.common.TimingSection getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:timingSection:timingSection")
		public   void setValue(org.radixware.ads.Profiler.common.TimingSection val) {
			Value = val;
		}
	}
	public TimingSection getTimingSection();
	/*Radix::SystemMonitor::MetricType:esclationDelay:esclationDelay-Presentation Property*/


	public class EsclationDelay extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public EsclationDelay(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:esclationDelay:esclationDelay")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:esclationDelay:esclationDelay")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public EsclationDelay getEsclationDelay();
	/*Radix::SystemMonitor::MetricType:period:period-Presentation Property*/


	public class Period extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public Period(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:period:period")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:period:period")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Period getPeriod();
	/*Radix::SystemMonitor::MetricType:classGuid:classGuid-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:classGuid:classGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:classGuid:classGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ClassGuid getClassGuid();
	/*Radix::SystemMonitor::MetricType:lastUpdateTime:lastUpdateTime-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateTime:lastUpdateTime")
		public  java.sql.Timestamp getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateTime:lastUpdateTime")
		public   void setValue(java.sql.Timestamp val) {
			Value = val;
		}
	}
	public LastUpdateTime getLastUpdateTime();
	/*Radix::SystemMonitor::MetricType:unitId:unitId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:unitId:unitId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:unitId:unitId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public UnitId getUnitId();
	/*Radix::SystemMonitor::MetricType:warnDelay:warnDelay-Presentation Property*/


	public class WarnDelay extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public WarnDelay(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:warnDelay:warnDelay")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:warnDelay:warnDelay")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public WarnDelay getWarnDelay();
	/*Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy:minSeverityForSnmpNtfy-Presentation Property*/


	public class MinSeverityForSnmpNtfy extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public MinSeverityForSnmpNtfy(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.kernel.common.enums.EEventSeverity> getValClass(){
			return org.radixware.kernel.common.enums.EEventSeverity.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.kernel.common.enums.EEventSeverity dummy = x == null ? null : (x instanceof org.radixware.kernel.common.enums.EEventSeverity ? (org.radixware.kernel.common.enums.EEventSeverity)x : org.radixware.kernel.common.enums.EEventSeverity.getForValue(((Int)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.INT)).longValue()));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy:minSeverityForSnmpNtfy")
		public  org.radixware.kernel.common.enums.EEventSeverity getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy:minSeverityForSnmpNtfy")
		public   void setValue(org.radixware.kernel.common.enums.EEventSeverity val) {
			Value = val;
		}
	}
	public MinSeverityForSnmpNtfy getMinSeverityForSnmpNtfy();
	/*Radix::SystemMonitor::MetricType:title:title-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:title:title")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:title:title")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Title getTitle();
	/*Radix::SystemMonitor::MetricType:netChannelId:netChannelId-Presentation Property*/


	public class NetChannelId extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public NetChannelId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:netChannelId:netChannelId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:netChannelId:netChannelId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public NetChannelId getNetChannelId();
	/*Radix::SystemMonitor::MetricType:id:id-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:id:id")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:id:id")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public Id getId();
	/*Radix::SystemMonitor::MetricType:errorDelay:errorDelay-Presentation Property*/


	public class ErrorDelay extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public ErrorDelay(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:errorDelay:errorDelay")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:errorDelay:errorDelay")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public ErrorDelay getErrorDelay();
	/*Radix::SystemMonitor::MetricType:instanceId:instanceId-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:instanceId:instanceId")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:instanceId:instanceId")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public InstanceId getInstanceId();
	/*Radix::SystemMonitor::MetricType:serviceUri:serviceUri-Presentation Property*/


	public class ServiceUri extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ServiceUri(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:serviceUri:serviceUri")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:serviceUri:serviceUri")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ServiceUri getServiceUri();
	/*Radix::SystemMonitor::MetricType:lowWarnVal:lowWarnVal-Presentation Property*/


	public class LowWarnVal extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public LowWarnVal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowWarnVal:lowWarnVal")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowWarnVal:lowWarnVal")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public LowWarnVal getLowWarnVal();
	/*Radix::SystemMonitor::MetricType:controlledValue:controlledValue-Presentation Property*/


	public class ControlledValue extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ControlledValue(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.SystemMonitor.common.ControlledValue dummy = x == null ? null : (x instanceof org.radixware.ads.SystemMonitor.common.ControlledValue ? (org.radixware.ads.SystemMonitor.common.ControlledValue)x : org.radixware.ads.SystemMonitor.common.ControlledValue.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.SystemMonitor.common.ControlledValue> getValClass(){
			return org.radixware.ads.SystemMonitor.common.ControlledValue.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.SystemMonitor.common.ControlledValue dummy = x == null ? null : (x instanceof org.radixware.ads.SystemMonitor.common.ControlledValue ? (org.radixware.ads.SystemMonitor.common.ControlledValue)x : org.radixware.ads.SystemMonitor.common.ControlledValue.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:controlledValue:controlledValue")
		public  org.radixware.ads.SystemMonitor.common.ControlledValue getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:controlledValue:controlledValue")
		public   void setValue(org.radixware.ads.SystemMonitor.common.ControlledValue val) {
			Value = val;
		}
	}
	public ControlledValue getControlledValue();
	/*Radix::SystemMonitor::MetricType:enabled:enabled-Presentation Property*/


	public class Enabled extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public Enabled(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:enabled:enabled")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:enabled:enabled")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public Enabled getEnabled();
	/*Radix::SystemMonitor::MetricType:lowErrorVal:lowErrorVal-Presentation Property*/


	public class LowErrorVal extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public LowErrorVal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowErrorVal:lowErrorVal")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lowErrorVal:lowErrorVal")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public LowErrorVal getLowErrorVal();
	/*Radix::SystemMonitor::MetricType:notes:notes-Presentation Property*/


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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:notes:notes")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:notes:notes")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public Notes getNotes();
	/*Radix::SystemMonitor::MetricType:highWarnVal:highWarnVal-Presentation Property*/


	public class HighWarnVal extends org.radixware.kernel.common.client.models.items.properties.PropertyNum{
		public HighWarnVal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highWarnVal:highWarnVal")
		public  Num getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:highWarnVal:highWarnVal")
		public   void setValue(Num val) {
			Value = val;
		}
	}
	public HighWarnVal getHighWarnVal();
	/*Radix::SystemMonitor::MetricType:lastUpdateUser:lastUpdateUser-Presentation Property*/


	public class LastUpdateUser extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public LastUpdateUser(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateUser:lastUpdateUser")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:lastUpdateUser:lastUpdateUser")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public LastUpdateUser getLastUpdateUser();
	/*Radix::SystemMonitor::MetricType:propControlledValEditable:propControlledValEditable-Presentation Property*/


	public class PropControlledValEditable extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public PropControlledValEditable(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:propControlledValEditable:propControlledValEditable")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:propControlledValEditable:propControlledValEditable")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public PropControlledValEditable getPropControlledValEditable();
	/*Radix::SystemMonitor::MetricType:isPeriodSupportedVal:isPeriodSupportedVal-Presentation Property*/


	public class IsPeriodSupportedVal extends org.radixware.kernel.common.client.models.items.properties.PropertyBool{
		public IsPeriodSupportedVal(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:isPeriodSupportedVal:isPeriodSupportedVal")
		public  Bool getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:isPeriodSupportedVal:isPeriodSupportedVal")
		public   void setValue(Bool val) {
			Value = val;
		}
	}
	public IsPeriodSupportedVal getIsPeriodSupportedVal();
	/*Radix::SystemMonitor::MetricType:valueType:valueType-Presentation Property*/


	public class ValueType extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ValueType(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public void setServerValue(org.radixware.kernel.common.client.models.items.properties.PropertyValue val){
			Object x = val.getValue();
			org.radixware.ads.SystemMonitor.common.MetricValueType dummy = x == null ? null : (x instanceof org.radixware.ads.SystemMonitor.common.MetricValueType ? (org.radixware.ads.SystemMonitor.common.MetricValueType)x : org.radixware.ads.SystemMonitor.common.MetricValueType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			val.refineValue(dummy);
			super.setServerValue(val);
		}
		@Override
		public Class<org.radixware.ads.SystemMonitor.common.MetricValueType> getValClass(){
			return org.radixware.ads.SystemMonitor.common.MetricValueType.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.radixware.ads.SystemMonitor.common.MetricValueType dummy = x == null ? null : (x instanceof org.radixware.ads.SystemMonitor.common.MetricValueType ? (org.radixware.ads.SystemMonitor.common.MetricValueType)x : org.radixware.ads.SystemMonitor.common.MetricValueType.getForValue((Str)org.radixware.kernel.common.utils.Utils.convertValueToEnumAcceptable(x,org.radixware.kernel.common.enums.EValType.STR)));
			setValue(dummy);
		}





				














		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:valueType:valueType")
		public  org.radixware.ads.SystemMonitor.common.MetricValueType getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:valueType:valueType")
		public   void setValue(org.radixware.ads.SystemMonitor.common.MetricValueType val) {
			Value = val;
		}
	}
	public ValueType getValueType();
	/*Radix::SystemMonitor::MetricType:kindTitle:kindTitle-Presentation Property*/


	public class KindTitle extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public KindTitle(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kindTitle:kindTitle")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:kindTitle:kindTitle")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public KindTitle getKindTitle();
	/*Radix::SystemMonitor::MetricType:measurePeriodStr:measurePeriodStr-Presentation Property*/


	public class MeasurePeriodStr extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public MeasurePeriodStr(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:measurePeriodStr:measurePeriodStr")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:measurePeriodStr:measurePeriodStr")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public MeasurePeriodStr getMeasurePeriodStr();
	public static class CalcMetric extends org.radixware.kernel.common.client.models.items.Command{
		protected CalcMetric(org.radixware.kernel.common.client.models.Model model,org.radixware.kernel.common.client.meta.RadCommandDef def){super(model,def);}

	}



}

/* Radix::SystemMonitor::MetricType - Desktop Meta*/

/*Radix::SystemMonitor::MetricType-Entity Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
			"Radix::SystemMonitor::MetricType",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G7DBLYWZFEMFFWKKO3PFTU3W4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),0,

			/*Radix::SystemMonitor::MetricType:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::SystemMonitor::MetricType:classGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colBI36OSBML5D73KDJOQKMSMISQI"),
						"classGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:classGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,50,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:errorDelay:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWD3JCD37ZDAVKKCLTUEPMO3TQ"),
						"errorDelay",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2FS2DTYP5E7BCLUK5DM5CS4FA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:errorDelay:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:esclationDelay:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4YKCVXTHZVETTGSLJZFJB6PPEY"),
						"esclationDelay",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCRNASPYS4VCZFNVEQR432YSJWY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:esclationDelay:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:highErrorVal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),
						"highErrorVal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN2KR76BFLFD2HCVQ2NNPEZMQOM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.NUM,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:highErrorVal:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:highWarnVal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),
						"highWarnVal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZJX4CJT5UVEMRJAT3GR3VXMHCU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.NUM,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:highWarnVal:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:id:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),
						"id",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGUGVZI7TBBMDFKLOKOD3HDSXY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:id:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:kind:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJNBDFWIVG47OCFOCMCXVBFXI"),
						"kind",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOIMMZMC5NFPBDXK5Y3QUZ6BSU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:kind:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,100,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:lastUpdateTime:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFAATRVDUDJBSLA2GVBLSKLS3EA"),
						"lastUpdateTime",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM54R4NS43RFXJBLO5ZZVX7L4GY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:lastUpdateTime:PropertyPresentation:Edit Options:-Edit Mask Date Time*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskDateTime(org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,org.radixware.kernel.common.enums.EDateTimeStyle.DEFAULT,null,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:lastUpdateUser:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colYUWJY5SQSZEHZM7TT2SNIIB54Y"),
						"lastUpdateUser",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJVLZM7JW5A27J6ANMEWJYWHCM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:lastUpdateUser:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:lowErrorVal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),
						"lowErrorVal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGMUV43HR4FAGLAGIIJYFTHENAE"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.NUM,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:lowErrorVal:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:lowWarnVal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),
						"lowWarnVal",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZXIDO3QKERDMLMM5YVP2OK372Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.NUM,
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:lowWarnVal:PropertyPresentation:Edit Options:-Edit Mask Num*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskNum(null,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.DEFAULT,null,null,(byte)-1),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:notes:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUH6LIE6A55C5BEX56Y7GPPZMHQ"),
						"notes",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFGJV77YNFFCNEGFAPHMOKUDYY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:notes:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,1000,true),
						null,
						null,
						null,
						true,-1,-1,1,
						true,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:period:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col773ODXVAM5DIZIORVOBGIBYK7Y"),
						"period",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ537FURE4JHURCQDBLZY4T275I"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:period:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(1L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDH7IOIXOV5BNJEDQEX3NWRZD5E"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,false),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:warnDelay:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5HMBXQKAZBVTJSJ5YVNSGCR5Y"),
						"warnDelay",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GSRKZTYRNFLNNY2U4G6WGUXOI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:warnDelay:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:netChannelId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),
						"netChannelId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJZ3VS3N3NHVHMFWWIG3DYBQYA"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:netChannelId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:instanceId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSR5Z6WKAZB4ZEAJQTDWKJA6J4"),
						"instanceId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls37PH3BCOIRH3HGQQUE52KEHE5U"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:instanceId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:unitId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),
						"unitId",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFDMM3C3ZJHQBCX4X3ANQN4ZVI"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:unitId:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(-999999999L,999999999L,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:timingSection:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3MKU34INANH5XP7OKUZ64JQNF4"),
						"timingSection",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EGPCVY3W5ABRJ2STVUB3RSSIY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsJIXOZF6PJJAM5AZKRLYLYFA75E"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:timingSection:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsJIXOZF6PJJAM5AZKRLYLYFA75E"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_TITLE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:serviceUri:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2GTEKTBT5A7RLD32JUJ7AZ52E"),
						"serviceUri",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG6VUV2Z3FCE5F5PLPE3R5VH3A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:serviceUri:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,300,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:enabled:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),
						"enabled",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEG2FSL5ZMFETRKB4LKDHOV2NUY"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:enabled:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:kindTitle:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),
						"kindTitle",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXOEKANACYZG63BPLOBKDXKHI6Q"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:kindTitle:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:isPeriodSupportedVal:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB3E3XXUZSZD7RIIKG5IRNXZ7YA"),
						"isPeriodSupportedVal",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:isPeriodSupportedVal:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:valueType:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdFKCD4PYVUNCZDPOQ57WUGMDXBI"),
						"valueType",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.DYNAMIC,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsJW5X3HYYBFFT3LECMFTSPIZYQE"),
						false,
						false,
						null,
						null,
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.NEVER,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:valueType:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsJW5X3HYYBFFT3LECMFTSPIZYQE"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:measurePeriodStr:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXD65NGMR4FASNAF5K2KSMQUO7I"),
						"measurePeriodStr",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6R3KDBTP55AGPK7IGW5RZF5W4A"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:measurePeriodStr:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJERRR2BH2ZD2XGDYMYUS67KOKY"),
						"minSeverityForSnmpNtfy",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKAEJDVDHJBNTAAY52YC2UJVJQ"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.INT,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("1"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:minSeverityForSnmpNtfy:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acsFRO5RTQOG7NRDJH2ACQMTAIZT4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_VALUE,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.INCLUDE,new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("aci52AUJTYZVPORDJHCAANE2UAFXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aci5WAUJTYZVPORDJHCAANE2UAFXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aci6CAUJTYZVPORDJHCAANE2UAFXA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("aci56AUJTYZVPORDJHCAANE2UAFXA")}),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:controlledValue:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),
						"controlledValue",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBPAH5NSRRBDJJDYOYSW3W2BMU"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						4,
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),
						false,
						false,
						null,
						org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("MIN_MAX"),
						/*Edit options*/
						org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						true,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:controlledValue:PropertyPresentation:Edit Options:-Edit Mask Enum*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskConstSet(org.radixware.kernel.common.types.Id.Factory.loadFrom("acs3JQBPC2NCJD37LVXZ6FUAXVNZ4"),org.radixware.kernel.common.enums.EEditMaskEnumOrder.BY_ORDER,org.radixware.kernel.common.enums.EEditMaskEnumCorrection.NONE,null),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::SystemMonitor::MetricType:propControlledValEditable:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdAOXD6NG3ENHEVMON2CGXVHJ47A"),
						"propControlledValEditable",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
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
						org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
						false,false,
						false,
						false,
						null,
						false,

						/*Radix::SystemMonitor::MetricType:propControlledValEditable:PropertyPresentation:Edit Options:-Edit Mask Bool*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskBool("true","false",org.radixware.kernel.common.enums.ECompatibleTypesForBool.DEFAULT,"","",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),null,null,Boolean.FALSE),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			new org.radixware.kernel.common.client.meta.RadCommandDef[]{
					new org.radixware.kernel.common.client.meta.RadPresentationCommandDef(
					/*Radix::SystemMonitor::MetricType:calcMetric-Object Command*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("cmdIUH3LXIN2ND4LFNS2EN4EB25N4"),
						"calcMetric",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJ5EO7XOCZDRJOCYUXF4VAJY5Y"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKNNPRFVPT5ZUBCLMIQZXIIA2KL6DGGUU"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("afhT3U57ZBAK5DODG6CM6AKBBQU4Q"),
						org.radixware.kernel.common.enums.ECommandNature.FORM_IN_OUT,
						true,
						false,
						false,
						org.radixware.kernel.common.enums.ECommandScope.OBJECT,
						null,
						org.radixware.kernel.common.enums.ECommandAccessibility.ONLY_FOR_FIXED,
						true)
			},
			null,
			new org.radixware.kernel.common.client.meta.RadSortingDef[]{

					new org.radixware.kernel.common.client.meta.RadSortingDef(
					/*Radix::SystemMonitor::MetricType:ID-Sorting*/

						org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEX6MHATMINDDBCNIA4U62BBBCU"),
						"ID",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWUMURLZAJNEHJKEBHOTCBELOKU"),
						new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem[]{

								new org.radixware.kernel.common.client.meta.RadSortingDef.SortingItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),
								false)
						})
			},
			new org.radixware.kernel.common.client.meta.RadReferenceDef[]{

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2L6D7LCZH5HOLESXXT4PBROHSE"),"MetricType=>User (lastUpdateUser=>name)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblSY4KIOLTGLNRDHRZABQAQH3XQ4"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colYUWJY5SQSZEHZM7TT2SNIIB54Y")},new String[]{"lastUpdateUser"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEF6ODCYWY3NBDGMCABQAQH3XQ4")},new String[]{"name"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("ref2RXWMT2TDVERPBYVVIWPKUQGHY"),"MetricType=>NetChannel (netChannelId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblHBE24OUE2DNRDB7AAALOMT5GDM"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4")},new String[]{"netChannelId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colKA3TGQME2DNRDB7AAALOMT5GDM")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refBCXO4FN5ABD6ZDZIQIGY67G6ME"),"MetricType=>Instance (instanceId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl52CHFNO3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSR5Z6WKAZB4ZEAJQTDWKJA6J4")},new String[]{"instanceId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("col3VINP666G5VDBFSUAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refBRBSH532FRC3BOQAMSVBUJQA74"),"MetricType=>Unit (unitId=>id)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl5HP4XTP3EGWDBRCRAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU")},new String[]{"unitId"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colEVLUYADHR5VDBNSIAAUMFADAIA")},new String[]{"id"}),

					new org.radixware.kernel.common.client.meta.RadReferenceDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("refIXSOX75UENE4FFXOIBRBWLS5RQ"),"MetricType=>Service (systemId=>systemId, serviceUri=>uri)",org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("tblC2OWQGDVVHWDBROXAAIT4AGD7E"),new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colAFYZPWLHIZGWPG44UVQ43ZX4ZA"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2GTEKTBT5A7RLD32JUJ7AZ52E")},new String[]{"systemId","serviceUri"},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("colBYT75RIQLDNBDJA6ACQMTAIZT4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("colGPLF7QQ2J3NRDJIRACQMTAIZT4")},new String[]{"systemId","uri"})
			},
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I"),org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUT72FHD2WBCJ3BFZAKTCGS4WKU")},
			true,false,false);
}

/* Radix::SystemMonitor::MetricType - Web Meta*/

/*Radix::SystemMonitor::MetricType-Entity Class*/

package org.radixware.ads.SystemMonitor.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::SystemMonitor::MetricType:Presentations-Entity Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
			"Radix::SystemMonitor::MetricType",
			null,
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G7DBLYWZFEMFFWKKO3PFTU3W4"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),0,
			null,
			null,
			null,
			null,
			null,
			null,
			false,false,false);
}

/* Radix::SystemMonitor::MetricType:Edit - Desktop Meta*/

/*Radix::SystemMonitor::MetricType:Edit-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
	null,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
	null,
	null,

	/*Radix::SystemMonitor::MetricType:Edit:Editor Pages-Editor Presentation Pages*/
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef[]{

			/*Radix::SystemMonitor::MetricType:Edit:General-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA"),"General",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR65WXCOMSBBR5FANFI3YDVI44Q"),null,new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem[]{

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),0,2,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),0,5,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),0,6,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),0,7,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),0,8,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colI5HMBXQKAZBVTJSJ5YVNSGCR5Y"),0,9,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNWD3JCD37ZDAVKKCLTUEPMO3TQ"),0,10,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col4YKCVXTHZVETTGSLJZFJB6PPEY"),0,11,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("col773ODXVAM5DIZIORVOBGIBYK7Y"),0,3,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colUH6LIE6A55C5BEX56Y7GPPZMHQ"),0,14,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),0,0,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),0,4,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),0,1,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colJERRR2BH2ZD2XGDYMYUS67KOKY"),0,13,1,false,false),

					new org.radixware.kernel.common.client.meta.editorpages.RadStandardEditorPageDef.PageItem(org.radixware.kernel.common.types.Id.Factory.loadFrom("colPSRIBABNSJD45B3FB3UDKHACIM"),0,12,1,false,false)
			},null),

			/*Radix::SystemMonitor::MetricType:Edit:State-Editor Page*/

			 new org.radixware.kernel.common.client.meta.editorpages.RadCustomEditorPageDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6O4DDW7XZRHEHF7JGHBYCEPBEQ"),"State",org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6T63DBDNXVG2NLOXPMT5VF7HBU"),null,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("cepIXT5YZZ2ANBPRDOCGARYUUCV4U"))
	},
	new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA")),
		new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epg6O4DDW7XZRHEHF7JGHBYCEPBEQ"))}
	,

	/*Radix::SystemMonitor::MetricType:Edit:Children-Explorer Items*/
		new org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef[]{

				/*Radix::SystemMonitor::MetricType:Edit:MetricState-Child Ref Explorer Item*/

				new org.radixware.kernel.common.client.meta.explorerItems.RadChildRefExplorerItemDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("xpi6B74WDD67FDR3FNAC23XRLBY6Y"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDMSK3EZ55FE6XBPSUEKKG65GFQ"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("aec4QTN5SWVEFB55HPL32XOX6KZ7Y"),
					org.radixware.kernel.common.types.Id.Factory.loadFrom("sprM576QUTA55AYZDLJED3BWD4LOU"),
					0,
					null,
					16432,false)
		}
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	144,0,0,null);
}
/* Radix::SystemMonitor::MetricType:Edit:Model - Desktop Executable*/

/*Radix::SystemMonitor::MetricType:Edit:Model-Entity Model Class*/

package org.radixware.ads.SystemMonitor.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model")
public class Edit:Model  extends org.radixware.ads.SystemMonitor.explorer.MetricType.MetricType_DefaultModel {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::SystemMonitor::MetricType:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::SystemMonitor::MetricType:Edit:Model:Properties-Properties*/

	/*Radix::SystemMonitor::MetricType:Edit:Model:stateView-Dynamic Property*/



	protected org.radixware.kernel.common.client.views.IView stateView=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:stateView")
	public published  org.radixware.kernel.common.client.views.IView getStateView() {
		return stateView;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:stateView")
	public published   void setStateView(org.radixware.kernel.common.client.views.IView val) {
		stateView = val;
	}






	/*Radix::SystemMonitor::MetricType:Edit:Model:Methods-Methods*/

	/*Radix::SystemMonitor::MetricType:Edit:Model:statePageViewOpened-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:statePageViewOpened")
	public  void statePageViewOpened (com.trolltech.qt.gui.QWidget widget) {
		refreshStatePage();
	}

	/*Radix::SystemMonitor::MetricType:Edit:Model:setVisibility-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:setVisibility")
	protected published  void setVisibility () {
		period.setVisible(isPeriodSupportedVal.Value == Boolean.TRUE);
		controlledValue.setVisible(propControlledValEditable.Value == Boolean.TRUE);
	}

	/*Radix::SystemMonitor::MetricType:Edit:Model:afterRead-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:afterRead")
	protected published  void afterRead () {
		super.afterRead();
		if(!isInSelectorRowContext()) {
		    setVisibility();
		    refreshStatePage();
		}
	}

	/*Radix::SystemMonitor::MetricType:Edit:Model:statePageViewClosed-Presentation Slot Method*/

	@org.radixware.kernel.common.lang.ReflectiveCallable
	@SuppressWarnings("unused")
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:statePageViewClosed")
	public  void statePageViewClosed () {
		if (stateView != null) {
		    stateView.close(true);
		    stateView = null;
		}
	}

	/*Radix::SystemMonitor::MetricType:Edit:Model:refreshStatePage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:refreshStatePage")
	public  void refreshStatePage () {
		if (!isEditorPageExists(idof[MetricType:Edit:State]) || getEditorPage(idof[MetricType:Edit:State]).getView() == null) {
		    return;
		}

		try {
		    Explorer.Models::GroupModel groupModel = (Explorer.Models::GroupModel) getChildModel(idof[MetricType:Edit:MetricState]);
		    groupModel.reread();
		    boolean moreThanOne = groupModel.getEntity(1) != null;
		    final Client.Views::IView view;
		    final Explorer.Models::Model model;
		    if (moreThanOne) {
		        view = groupModel.createView();
		        model = groupModel;
		    } else {
		        Explorer.Models::EntityModel first = groupModel.getEntity(0);
		        if (first != null) {
		            first = first.openInSelectorEditModel();
		            view = first.createView();
		            model = first;
		        } else {
		            view = null;
		            model = null;
		        }
		    }

		    if (view != null && view instanceof Explorer.Widgets::IQWidgetProvider) {
		        if (stateView != null) {
		            if (moreThanOne && stateView.getModel() instanceof Explorer.Models::GroupModel) {
		                view.close(true);
		                return;
		            }
		            if (!moreThanOne && stateView.getModel() instanceof Explorer.Models::EntityModel) {
		                if (((Explorer.Models::EntityModel) stateView.getModel()).getPid().equals(((Explorer.Models::EntityModel) model).getPid())) {
		                    stateView.reread();
		                    view.close(true);
		                    return;
		                }
		            }
		            stateView.close(true);
		        }
		        clearStatePage();
		        stateView = view;
		        final Explorer.Qt.Types::QWidget editorWidget = ((Explorer.Widgets::IQWidgetProvider) view).asQWidget();
		        editorWidget.setParent(MetricType:Edit:State:View:EditorPageView);
		        com.trolltech.qt.gui.QLayout layout = MetricType:Edit:State:View:EditorPageView.layout();
		        layout.addWidget(editorWidget);
		        if (model instanceof Explorer.Models::EntityModel) {
		            ((Explorer.Models::EntityModel) model).activateAllProperties();
		        }
		        view.open(model);
		        if (view instanceof Client.Views::IEditor) {
		            ((Client.Views::IEditor) view).setToolBarHidden(true);
		        }
		    } else {
		        if (stateView != null) {
		            stateView.close(true);
		            stateView = null;
		        }
		        clearStatePage();
		        com.trolltech.qt.gui.QHBoxLayout layout = (com.trolltech.qt.gui.QHBoxLayout) MetricType:Edit:State:View:EditorPageView.layout();
		        layout.addStretch();
		        layout.addWidget(new com.trolltech.qt.gui.QLabel("<none>"));
		        layout.addStretch();
		    }
		} catch (Exceptions::Exception ex) {
		    getEnvironment().messageException("Error", "Error reading metric state", ex);
		}
	}

	/*Radix::SystemMonitor::MetricType:Edit:Model:clearStatePage-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:clearStatePage")
	public  void clearStatePage () {
		org.radixware.kernel.explorer.utils.WidgetUtils.removeChildrenWidgets(MetricType:Edit:State:View:EditorPageView);
		com.trolltech.qt.gui.QLayout layout = MetricType:Edit:State:View:EditorPageView.layout();
		if (layout == null) {
		    layout = new com.trolltech.qt.gui.QHBoxLayout(MetricType:Edit:State:View:EditorPageView);
		} else {
		    while (layout.count() > 0) {
		        layout.removeItem(layout.itemAt(0));
		    }
		}
	}

	/*Radix::SystemMonitor::MetricType:Edit:Model:isCommandAccessible-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:Model:isCommandAccessible")
	protected published  boolean isCommandAccessible (org.radixware.kernel.common.client.meta.RadCommandDef commandDef) {
		//().().("class id : " + ());
		if (idof[MetricType:calcMetric].equals(commandDef.getId())) {
		    return Explorer.Meta::Utils.isClassExtends(getEnvironment(), getClassId(), idof[MetricType.AppMetric])
		    || idof[MetricType.AppMetric].equals(getClassId())
		    || Explorer.Meta::Utils.isClassExtends(getEnvironment(), getClassId(), idof[MetricType.UserFunc])
		    || idof[MetricType.UserFunc].equals(getClassId())
		    || Explorer.Meta::Utils.isClassExtends(getEnvironment(), getClassId(), idof[MetricType.UserQuery])
		    || idof[MetricType.UserQuery].equals(getClassId());
		}
		return super.isCommandAccessible(commandDef);
	}







}

/* Radix::SystemMonitor::MetricType:Edit:Model - Desktop Meta*/

/*Radix::SystemMonitor::MetricType:Edit:Model-Entity Model Class*/

package org.radixware.ads.SystemMonitor.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemSE464PGOZVAYPG5C7DZYJQ4P3E"),
						"Edit:Model",
						null,
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::SystemMonitor::MetricType:Edit:Model:Properties-Properties*/
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

/* Radix::SystemMonitor::MetricType:Edit:State:View - Desktop Executable*/

/*Radix::SystemMonitor::MetricType:Edit:State:View-Custom Page Editor for Desktop*/

package org.radixware.ads.SystemMonitor.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:State:View")
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
		EditorPageView.setFont(DEFAULT_FONT);
		EditorPageView.opened.connect(model, "mthVVSIGN37FJFD7JCGMRUXJEHRBQ(com.trolltech.qt.gui.QWidget)");
		EditorPageView.closed.connect(model, "mthEGOZ3ISX3BH4PBKOWPYEUAHLUU()");
		opened.emit(this);
	}
	public final org.radixware.ads.SystemMonitor.explorer.Edit:Model getModel() {
		return (org.radixware.ads.SystemMonitor.explorer.Edit:Model) super.getModel();
	}

}

/* Radix::SystemMonitor::MetricType:Edit:State:WebView - Web Executable*/

/*Radix::SystemMonitor::MetricType:Edit:State:WebView-Rwt Custom Page Editor*/

package org.radixware.ads.SystemMonitor.web;
@SuppressWarnings({"unchecked","rawtypes"})
@org.radixware.kernel.common.lang.MetaInfo(name="Radix::SystemMonitor::MetricType:Edit:State:WebView")
public class WebView extends org.radixware.wps.views.editor.EditorPageView{
	public WebView widget;
	public WebView getWidget(){ return  widget;}
	public WebView(org.radixware.kernel.common.client.IClientEnvironment env,org.radixware.kernel.common.client.views.IView view
	,org.radixware.kernel.common.client.meta.editorpages.RadEditorPageDef page){
		super(env,view,page);
	}
	public void open(org.radixware.kernel.common.client.models.Model model){
		super.open(model);
		//============ Radix::SystemMonitor::MetricType:Edit:State:WebView:widget ==============
		this.widget = this;
		fireOpened();
	}
}

/* Radix::SystemMonitor::MetricType:Create - Desktop Meta*/

/*Radix::SystemMonitor::MetricType:Create-Editor Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class Create_mi{
	private static final class Create_DEF extends org.radixware.kernel.common.client.meta.RadEditorPresentationDef{
		Create_DEF(){
			super(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I"),
			"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
			org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
			null,
			null,

			/*Radix::SystemMonitor::MetricType:Create:Editor Pages-Editor Presentation Pages*/
			null,
			new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder[]{
				new org.radixware.kernel.common.client.meta.editorpages.RadEditorPages.PageOrder(1,org.radixware.kernel.common.types.Id.Factory.loadFrom("epgBLOETUBE3RCINP7GTEZFWRN2KA"))}
			,

			/*Radix::SystemMonitor::MetricType:Create:Children-Explorer Items*/
				null
			,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
			null,
			0,
			null,
			(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
			36018,0,0);
		}
		@Override
		protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.Edit:Model(userSession,this);
		}
	}
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new Create_DEF(); 
;
}
/* Radix::SystemMonitor::MetricType:General - Desktop Meta*/

/*Radix::SystemMonitor::MetricType:General-Selector Presentation*/

package org.radixware.ads.SystemMonitor.explorer;
public final class General_mi extends org.radixware.kernel.common.client.meta.RadSelectorPresentationDef{
	public static final org.radixware.kernel.common.client.meta.RadSelectorPresentationDef rdxMeta = new General_mi();
	private General_mi(){
		super(org.radixware.kernel.common.types.Id.Factory.loadFrom("sprUT72FHD2WBCJ3BFZAKTCGS4WKU"),
		"General",
		org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER,
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("aec2H6SULJHXJGFVIS6UVHRWQS4AM"),
		org.radixware.kernel.common.types.Id.Factory.loadFrom("tbl2H6SULJHXJGFVIS6UVHRWQS4AM"),
		null,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("imgKWUH5E7PFFE47NWTGBF6ILYADI"),
		null,
		null,
		true,
		org.radixware.kernel.common.types.Id.Factory.loadFrom("srtEX6MHATMINDDBCNIA4U62BBBCU"),
		null,
		false,
		true,
		null,
		16424,
		null,
		128,
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprRG25UU3GFJAXXMVK7ZE4GQBL4I")},
		new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprSE464PGOZVAYPG5C7DZYJQ4P3E")},
		false,true,false,0,0);
		columns = new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn[]{

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdB3E3XXUZSZD7RIIKG5IRNXZ7YA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2RJUTHZLBFGDPHT5NR6S6APSYM"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colXHO3TMONUZFWVLGCBFATASRCZ4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colSYQ72HEL2RD3VFYFLWVG6GFZEA"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP5GRYC4WIRFDVCOV3AYNBAPYY4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2JJNBDFWIVG47OCFOCMCXVBFXI"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.NEVER,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colNS4TO66OSNH3DCZS64EDNL7VDU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdJHAQ5ULMWBAVTHGBDBL34ZHGME"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colKK3G7VOVNBESRPNV7SEYWFKLDQ"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdXD65NGMR4FASNAF5K2KSMQUO7I"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colQKG6VKSKMBFG7G2WMCOW2DCD2A"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.INITIAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colOSR5Z6WKAZB4ZEAJQTDWKJA6J4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colFYLCZQEXTJG5JGNSHCWLJTQUSU"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("col3MKU34INANH5XP7OKUZ64JQNF4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colP2GTEKTBT5A7RLD32JUJ7AZ52E"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null),

				new org.radixware.kernel.common.client.meta.RadSelectorPresentationDef.SelectorColumn(org.radixware.kernel.common.types.Id.Factory.loadFrom("colMVLLX5SQNVDBRMQ2ZYOQ6WDVH4"),org.radixware.kernel.common.enums.ESelectorColumnVisibility.OPTIONAL,org.radixware.kernel.common.enums.ESelectorColumnAlign.DEFAULT,org.radixware.kernel.common.enums.ESelectorColumnSizePolicy.MANUAL_RESIZE,null)
		};
	}
	@Override
	protected org.radixware.kernel.common.client.models.Model createModelImpl(org.radixware.kernel.common.client.IClientEnvironment userSession) {    return new org.radixware.ads.SystemMonitor.explorer.MetricType.DefaultGroupModel(userSession,this);
	}
}
/* Radix::SystemMonitor::MetricType - Localizing Bundle */
package org.radixware.ads.SystemMonitor.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class MetricType - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metrics");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрики");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2G7DBLYWZFEMFFWKKO3PFTU3W4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"User-Defined Metrics");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользовательские метрики");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2QDDIQ7HCBEN3AXROOPXT2TOZE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. инстанции");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls37PH3BCOIRH3HGQQUE52KEHE5U"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service URI: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"URI сервиса: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5DAC5DAOXNA5XMMIBHTUZTZMSY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Profiling section");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Секция профилирования");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls5EGPCVY3W5ABRJ2STVUB3RSSIY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Period");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6R3KDBTP55AGPK7IGW5RZF5W4A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6T63DBDNXVG2NLOXPMT5VF7HBU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Модуль: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls6W5BP5SVXJCFRKN2NKFYKA72VU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Warning delay (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задержка перед предупреждением (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls7GSRKZTYRNFLNNY2U4G6WGUXOI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error reading metric state");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка при получении состояния метрики");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBRIJYIHLLBAD7MRMLUOS67DTHY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Minimum level for sending SNMP Trap");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Минимальный уровень для отправки SNMP Trap");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCKAEJDVDHJBNTAAY52YC2UJVJQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Escalation delay (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задержка перед эскалацией (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCRNASPYS4VCZFNVEQR432YSJWY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsCXNBHBAMJ5EOPK3XXRE5FWS4UM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<нет>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsD2UIEUDJEVA2RHJ3LJSC37AFQU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Value used for control");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Значение, используемое для контроля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDBPAH5NSRRBDJJDYOYSW3W2BMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDH7IOIXOV5BNJEDQEX3NWRZD5E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"State");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Состояние");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDMSK3EZ55FE6XBPSUEKKG65GFQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDZOKMVJVIBC4BNBRQJGDRJ4LKI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Enabled");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Активна");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsEG2FSL5ZMFETRKB4LKDHOV2NUY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Notes");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Примечания");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGFGJV77YNFFCNEGFAPHMOKUDYY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lower error value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нижнее значение для ошибки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsGMUV43HR4FAGLAGIIJYFTHENAE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Unit ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. модуля");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsHFDMM3C3ZJHQBCX4X3ANQN4ZVI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric \'%1\' of type \'%2\' for sensor \'%9\' has returned to the warning range with value \'%3\' at %8");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрика \'%1\' типа \'%2\' для сенсора \'%9\' вернулась в диапазон предупреждения со значением \'%3\' в %8");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsI257DPJ2QFCMLBZN63FP7J67JE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.SystemMonitoring",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network channel");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIJZ3VS3N3NHVHMFWWIG3DYBQYA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Measurement period (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Период измерения (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJ537FURE4JHURCQDBLZY4T275I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"<none>");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"<отсутствует>");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJFJV2KKC25HVVK5EXFMJB4VF3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Time Section: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Временная секция: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsKMT5BFL7OZELVCCAS25PDH2ZZA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error delay (s)");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Задержка перед ошибкой (с)");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2FS2DTYP5E7BCLUK5DM5CS4FA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Instance: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Инстанция: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsL2RQO6M34RFODCIGBQCPRCWEPI"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric \'%1\' of type \'%2\' for sensor \'%9\' has entered the warning range with value \'%3\' at %8");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрика \'%1\' типа \'%2\' для сенсора \'%9\' вошла в диапазон предупреждения со значением \'%3\' в %8");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLI56NRJSTFCA3KTPBLCD3AJXXA"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.WARNING,"App.SystemMonitoring",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Calculate Metric Values");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вычислить значения метрики");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsLJ5EO7XOCZDRJOCYUXF4VAJY5Y"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Время последнего обновления");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsM54R4NS43RFXJBLO5ZZVX7L4GY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Service URI");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"URI сервиса");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsMG6VUV2Z3FCE5F5PLPE3R5VH3A"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Upper error value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Верхнее значение для ошибки");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsN2KR76BFLFD2HCVQ2NNPEZMQOM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сеть");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsNSJ355IUXFGRPGT72P5ZARET2I"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metrics");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрики");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsO66VDXBRLZDZPGUQEYHTQTOJMU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric \'%1\' of type \'%2\' for sensor \'%9\' has returned to the normal range with value \'%3\' at %8");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрика \'%1\' типа \'%2\' для сенсора \'%9\' вернулась в нормальный интервал со значением \'%3\' в %8");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsOSKIQOFWYBD3VAVSBJULNYUVXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.EVENT,"App.SystemMonitoring",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsPOIMMZMC5NFPBDXK5Y3QUZ6BSU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric \'%1\' of type \'%2\' for sensor \'%9\' has entered the error range with value \'%3\' at %8");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрика \'%1\' типа \'%2\'  для сенсора \'%9\' вошла в диапазон ошибки со значением \'%3\' в %8");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsQWVO3KHA5FAV3GQJHLDVVB3WMQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.SystemMonitoring",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"General");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Общее");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsR65WXCOMSBBR5FANFI3YDVI44Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Same metric already exists");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Аналогичная метрика уже существует");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTAV4QIVSDBD6TINTRVI2PO4RNU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Last updated by");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Пользователь, последним выполнивший обновление");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsTJVLZM7JW5A27J6ANMEWJYWHCM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Network Channel: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Сетевой канал: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsV7CRHOYFVVCLNLOQOUKKA4JQHQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. метрики");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsVGUGVZI7TBBMDFKLOKOD3HDSXY"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Error");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ошибка");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWEVHMXF7WBFXJDQIGNFQYKWZXE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"By ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"По ид.");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsWUMURLZAJNEHJKEBHOTCBELOKU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Type");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Вид");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsXOEKANACYZG63BPLOBKDXKHI6Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Metric \'%1\' of type \'%2\' for sensor \'%9\' has been in the warning range for more than \'%3\' seconds at %8");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Метрика \'%1\' типа \'%2\' для сенсора \'%9\' пробыла в интервале предупреждения более \'%3\' секунд в %8");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZD7USWI27BDRHPVHNSTVCW7F4E"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.EVENT_CODE,org.radixware.kernel.common.enums.EEventSeverity.ERROR,"App.SystemMonitoring",java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Upper warning value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Верхнее значение для предупреждения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZJX4CJT5UVEMRJAT3GR3VXMHCU"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Lower warning value");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Нижнее значение для предупреждения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZXIDO3QKERDMLMM5YVP2OK372Q"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(MetricType - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaec2H6SULJHXJGFVIS6UVHRWQS4AM"),"MetricType - Localizing Bundle",$$$items$$$);
}
