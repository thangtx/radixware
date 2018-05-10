
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Server Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer")
public published class Endpoint.MessageQueue.Consumer  extends org.radixware.ads.MessageQueue.server.MessageQueueProcessor  implements org.radixware.ads.ServiceBus.server.IPipelineNode,org.radixware.kernel.server.meta.clazzes.IRadPropAccessorProvider  {

	private static final java.nio.charset.Charset UTF_8_CHARSET = java.nio.charset.Charset.forName("UTF-8");
	/**Metainformation accessor method*/
	@SuppressWarnings("unused")
	public org.radixware.kernel.server.meta.clazzes.RadClassDef getRadMeta(){return Endpoint.MessageQueue.Consumer_mi.rdxMeta;}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid")
	public published  Str getExtGuid() {
		return extGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid")
	public published   void setExtGuid(Str val) {
		extGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId")
	public published  Str getOutbConnectId() {
		return outbConnectId;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId")
	public published   void setOutbConnectId(Str val) {
		outbConnectId = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid")
	public published  Str getPipelineEntityGuid() {
		return pipelineEntityGuid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid")
	public published   void setPipelineEntityGuid(Str val) {
		pipelineEntityGuid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid-User Property*/
















	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid")
	public published  Str getPipelineEntityPid() {
		return pipelineEntityPid;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid")
	public published   void setPipelineEntityPid(Str val) {
		pipelineEntityPid = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject rqData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData")
	public published  org.apache.xmlbeans.XmlObject getRqData() {
		return rqData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData")
	public published   void setRqData(org.apache.xmlbeans.XmlObject val) {
		rqData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData-Dynamic Property*/



	protected org.apache.xmlbeans.XmlObject rsData=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData")
	public published  org.apache.xmlbeans.XmlObject getRsData() {
		return rsData;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData")
	public published   void setRsData(org.apache.xmlbeans.XmlObject val) {
		rsData = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel-Dynamic Property*/



	protected Int traceLevel=null;











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel")
	private final  Int getTraceLevel() {
		return traceLevel;
	}

	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel")
	private final   void setTraceLevel(Int val) {
		traceLevel = val;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:out-Dynamic Property*/



	protected static Str out=(Str)org.radixware.kernel.common.defs.value.ValAsStr.fromStr("Out",org.radixware.kernel.common.enums.EValType.STR);











	@SuppressWarnings({"unused","unchecked"})
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:out")
	public static published  Str getOut() {
		return out;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title-Column-Based Property*/


















































































	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterResponseDelivered-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterResponseDelivered")
	public published  void afterResponseDelivered (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs rs) throws org.radixware.ads.ServiceBus.server.PipelineException {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:checkConfig-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:checkConfig")
	public published  void checkConfig () throws org.radixware.ads.ServiceBus.server.PipelineConfigException {
		ServiceBus::SbServerUtil.checkConnection(this, out, outbConnectId);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getCreatingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getCreatingPresentationId")
	public published  org.radixware.kernel.common.types.Id getCreatingPresentationId () {
		return idof[Endpoint.MessageQueue.Consumer:Create];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getDescription-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getDescription")
	public published  Str getDescription () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getEditingPresentationId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getEditingPresentationId")
	public published  org.radixware.kernel.common.types.Id getEditingPresentationId () {
		return idof[Endpoint.MessageQueue.Consumer:Edit];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getExtGuid")
	public published  Str getExtGuid () {
		return extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getHolderEntityPid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getHolderEntityPid")
	public published  org.radixware.kernel.server.types.Pid getHolderEntityPid () {
		return getPid();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getIconId")
	public published  org.radixware.kernel.common.types.Id getIconId () {
		return idof[MessageQueue::MessageQueueIn];
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getPipeline")
	public published  org.radixware.ads.ServiceBus.server.IPipeline getPipeline () {
		return ServiceBus::SbServerUtil.getPipeline(new Pid(Arte, Types::Id.Factory.loadFrom(pipelineEntityGuid), pipelineEntityPid));
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getPipelineNodeParam-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getPipelineNodeParam")
	public published  Str getPipelineNodeParam (Str name) {
		final java.util.Map<Str,Str> params = getPipeline().getParams();
		if (params != null)
		    params.get(name);
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getTitle")
	public published  Str getTitle () {
		return title;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:refreshCache-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:refreshCache")
	public published  void refreshCache () {
		traceLevel = null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:setExtGuid-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:setExtGuid")
	public published  void setExtGuid (Str extGuid) {
		extGuid = extGuid;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:setPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:setPipeline")
	public published  void setPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		pipelineEntityGuid = Str.valueOf(pipeline.getHolderEntityPid().EntityId);
		pipelineEntityPid = Str.valueOf(pipeline.getHolderEntityPid());
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:trace-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:trace")
	public published  void trace (org.radixware.kernel.common.enums.EEventSeverity severity, Str mess, boolean sensitive) {
		if (!checkTraceLevel(severity)) {
		    return;
		}

		Arte::Trace.enterContext(Arte::EventContextType:MqProcessor, id);
		try {
		    final Object traceTargetHandler = Arte::Trace.addContextProfile(getPipeline().getTraceProfile(), (getPipeline() instanceof Types::Entity) ? ((Types::Entity) getPipeline()) : this);
		    try {
		        if (sensitive)
		            Arte::Trace.putSensitive(severity, mess, Arte::EventSource:ServiceBus);
		        else
		            Arte::Trace.put(severity, mess, Arte::EventSource:ServiceBus);
		    } finally {
		        Arte::Trace.delContextProfile(traceTargetHandler);
		    }
		} finally {
		    Arte::Trace.leaveContext(Arte::EventContextType:MqProcessor, id);
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceDebug-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceDebug")
	public published  void traceDebug (Str mess, boolean sensitive) {
		trace(Arte::EventSeverity:Debug, mess, sensitive);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceError-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceError")
	public published  void traceError (Str mess) {
		trace(Arte::EventSeverity:Error, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceEvent-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceEvent")
	public published  void traceEvent (Str mess) {
		trace(Arte::EventSeverity:Event, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceWarning-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceWarning")
	public published  void traceWarning (Str mess) {
		trace(Arte::EventSeverity:Warning, mess, false);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastInRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastInRqData")
	public published  org.apache.xmlbeans.XmlObject getLastInRqData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastInRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastInRsData")
	public published  org.apache.xmlbeans.XmlObject getLastInRsData () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastOutRqData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastOutRqData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRqData () {
		return rqData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastOutRsData-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getLastOutRsData")
	public published  org.apache.xmlbeans.XmlObject getLastOutRsData () {
		return rsData;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:export-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:export")
	public published  org.radixware.ads.ServiceBus.common.ImpExpXsd.Node export () {
		ServiceBus::ImpExpXsd:Node xNode = ServiceBus::ImpExpXsd:Node.Factory.newInstance();
		Common::CommonXsd:EditableProperties xEditableProps = Common::CommonXsd:EditableProperties.Factory.newInstance();
		Common::CommonXsd:EditableProperty xMqProp = xEditableProps.addNewItem();
		xMqProp.Id = idof[MessageQueue::MessageQueueProcessor:messageQueue];

		Common::CommonXsd:EditableProperty xUnitProp = xEditableProps.addNewItem();
		xUnitProp.Id = idof[MessageQueue::MessageQueueProcessor:unit];
		final Explorer.Utils::SqmlExpression mqHandlerUnitExpr = Explorer.Utils::SqmlExpression.equal(
		        Explorer.Utils::SqmlExpression.this_property(idof[System::Unit], idof[System::Unit:type]),
		        Explorer.Utils::SqmlExpression.valueInt(System::UnitType:MQ_HANDLER.intValue()));
		        
		final Explorer.Utils::SqmlExpression isFallbackExpr = Explorer.Utils::SqmlExpression.equal(
		        Explorer.Utils::SqmlExpression.this_property(idof[System::Unit], idof[System::Unit:classGuid]),
		        Explorer.Utils::SqmlExpression.valueStr(idof[MessageQueue::Unit.MessageQueueHandler.Consumer.Fallback].toString()));

		xUnitProp.Condition = Explorer.Utils::SqmlExpression.and(mqHandlerUnitExpr, Explorer.Utils::SqmlExpression.not(isFallbackExpr)).asXsqml();
		if (unit != null && unit.rid != null) {
		    xUnitProp.Rid = unit.rid;
		    xUnitProp.EntityId = idof[System::Unit];
		}


		ServiceBus::SbImpExpUtil.exportNode(xNode, this, extGuid, title,
		        java.util.Arrays.asList(idof[Endpoint.MessageQueue.Consumer:pipelineEntityGuid], idof[Endpoint.MessageQueue.Consumer:pipelineEntityPid], idof[Endpoint.MessageQueue.Consumer:extGuid]),
		        xEditableProps
		);

		ServiceBus::ImpExpXsd:MessageQueueProcessor xProcessor = ServiceBus::ImpExpXsd:MessageQueueProcessor.Factory.newInstance();
		xProcessor.Title = title;
		xProcessor.DbTraceProfile = dbTraceProfile;
		xProcessor.AasCallTimeoutMs = aasCallTimeoutSec;
		xProcessor.ParallelThreads = this.parallelThreads;

		xNode.addNewExt().set(xProcessor);
		return xNode;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterImport-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterImport")
	public published  void afterImport (java.util.Map<org.radixware.kernel.server.types.Pid,org.radixware.kernel.server.types.Pid> pids) {

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:checkTraceLevel-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:checkTraceLevel")
	public published  boolean checkTraceLevel (org.radixware.kernel.common.enums.EEventSeverity severity) {
		if (traceLevel == null) {
		    final Object traceTargetHandler = Arte::Trace.addContextProfile(getPipeline().getTraceProfile(), (getPipeline() instanceof Types::Entity) ? ((Types::Entity) getPipeline()) : this);
		    try {
		        traceLevel = Arte::Trace.getMinSeverity(Arte::EventSource:ServiceBus);
		    } finally {
		        Arte::Trace.delContextProfile(traceTargetHandler);
		    }
		}

		return traceLevel.intValue() <= severity.Value.intValue();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:connect-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:connect")
	public published  void connect (Str outConnectorRole, Str inConnectorId) {
		if (outConnectorRole == out) {
		    outbConnectId = inConnectorId;
		} else    
		    throw new AppError("Unknown role " + outConnectorRole);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorIconId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorIconId")
	public published  org.radixware.kernel.common.types.Id getConnectorIconId (Str role) {
		if (role == out)
		    return idof[ServiceBus::next];
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorId-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorId")
	public published  Str getConnectorId (Str role) {
		if (role == out) {
		    return outbConnectId;
		}
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorRole-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorRole")
	public published  Str getConnectorRole (Str id) {
		if (id == outbConnectId)
		    return out;
		throw new AppError("Unknown id " + id);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorRqDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorRqDataType")
	public published  Str getConnectorRqDataType (Str role) {
		if (role == out)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorRsDataType-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorRsDataType")
	public published  Str getConnectorRsDataType (Str role) {
		if (role == out)
		    return BINARY_TYPE;
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorSide-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorSide")
	public published  org.radixware.ads.ServiceBus.common.Side getConnectorSide (Str role) {
		if (role == out) {
		    return ServiceBus::Side:Right;
		}
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorTitle-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getConnectorTitle")
	public published  Str getConnectorTitle (Str role) {
		if (role == out) {
		    return "Inbound messages";
		}
		throw new AppError("Unknown role " + role);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getInConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getInConnectorRoles")
	public published  Str[] getInConnectorRoles () {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getOutConnectorRoles-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getOutConnectorRoles")
	public published  Str[] getOutConnectorRoles () {
		return new Str[] {out};
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:import-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:import")
	public published  void import (org.radixware.ads.ServiceBus.common.ImpExpXsd.Node xNode, boolean overwriteLocalSettings, org.radixware.ads.CfgManagement.server.ICfgImportHelper helper) {
		ServiceBus::SbImpExpUtil.importNode(xNode, this, overwriteLocalSettings, helper);

		if (overwriteLocalSettings) {
		    ServiceBus::ImpExpXsd:MessageQueueProcessor xProcessor;
		    try {
		        xProcessor = ServiceBus::ImpExpXsd:MessageQueueProcessor.Factory.parse(xNode.Ext.newInputStream());
		    } catch (Exceptions::Exception e) {
		        throw new AppError(e.getMessage(), e);
		    }
		    
		    title = xProcessor.Title;
		    parallelThreads = xProcessor.ParallelThreads;
		    aasCallTimeoutSec = xProcessor.AasCallTimeoutMs;
		    dbTraceProfile = xProcessor.getDbTraceProfile();
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:processRequest-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:processRequest")
	public published  org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRs processRequest (org.radixware.ads.ServiceBus.common.SbXsd.PipelineMessageRq rq) throws org.radixware.ads.ServiceBus.server.PipelineException {
		return null;
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:process-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:process")
	public published  void process (org.radixware.schemas.messagequeue.MqMessage xMessage) {
		getPipeline().refreshCache();
		if (!getPipeline().getChecked()) {
		    if (checkTraceLevel(Arte::EventSeverity:Debug))
		        traceWarning("Pipeline not checked");
		    else
		        throw new AppError("Pipeline not checked");
		}

		Xml xData = null;
		if (xMessage.getBody() != null) {
		    if (xMessage.getBody().getByteBody() != null) {
		        xData = ServiceBus::SbServerUtil.castToBinBase64(xMessage.getBody().getByteBody());
		    } else if (xMessage.getBody().getStrBody() != null) {
		        xData = ServiceBus::SbServerUtil.castToBinBase64(xMessage.getBody().getStrBody().getBytes(UTF_8_CHARSET));
		    }
		}
		if (xData == null) {
		    xData = ServiceBus::SbServerUtil.castToBinBase64(new byte[]{});
		}

		rqData = ServiceBus::SbCommonUtil.copyXml(xData);

		ServiceBus::SbXsd:PipelineMessageRq rq
		        = ServiceBus::SbServerUtil.prepareNextRequest(null, out, outbConnectId, xData);

		rq.InExtHeader.NodeEntityId = Str.valueOf(Pid.EntityId);
		rq.InExtHeader.NodeEntityPid = Str.valueOf(Pid);
		rq.Event = ServiceBus::PipelineEvent:Rq;
		if (xMessage.isSetMeta()) {
		    rq.InExtHeader.ensureMqMeta().set(xMessage.Meta);
		}

		String debugKey = null;

		if (checkTraceLevel(Arte::EventSeverity:Debug)) {
		    if (xMessage.isSetMeta()) {
		        debugKey = xMessage.getMeta().isSetDebugKey() ? xMessage.getMeta().getDebugKey() : (xMessage.getMeta().isSetMessageId() ? xMessage.getMeta().getMessageId() : null);
		    } else {
		        debugKey = null;
		    }
		    traceDebug("Processor received message from '" + this.messageQueue.calcTitle() + "': " + debugKey, false);
		    traceDebug("Message " + debugKey + " body: " + (xMessage.getBody().isSetByteBody() ? Utils::Hex.encode(xMessage.getBody().getByteBody()) : xMessage.getBody().getStrBody()), true);
		}

		ServiceBus::SbXsd:PipelineMessageRs rs;
		try {
		    rs = ServiceBus::SbServerUtil.processRequest(outbConnectId, rq);
		    ServiceBus::SbServerUtil.processAfterResponseDelivered(rs);
		} catch (ServiceBus::PipelineException e) {
		    throw new AppError("Pipeline processing exception: " + e.getMessage(), e);
		} catch (Exceptions::Exception e) {
		    if (e instanceof Exceptions::RuntimeException)
		        throw (Exceptions::RuntimeException) e;
		    throw new AppError(e.getMessage(), e);
		}

		rsData = ServiceBus::SbCommonUtil.copyXml(rs.CurStage.Data);

		if (checkTraceLevel(Arte::EventSeverity:Debug)) {
		    traceDebug("Processed message '" + debugKey + "' from '" + this.messageQueue.calcTitle() + "'", false);
		}

	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterInit-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterInit")
	protected published  void afterInit (org.radixware.kernel.server.types.Entity src, org.radixware.kernel.common.enums.EEntityInitializationPhase phase) {
		super.afterInit(src, phase);
		if (extGuid == null || (src != null && ((Endpoint.MessageQueue.Consumer) src).extGuid == extGuid)) {
		    extGuid = Arte::Arte.generateGuid();
		}
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterUpdatePropObject-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:afterUpdatePropObject")
	protected published  void afterUpdatePropObject (org.radixware.kernel.common.types.Id propId, org.radixware.kernel.server.types.Entity propVal) {
		onUpdate();
		super.afterUpdatePropObject(propId, propVal);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:onUpdate-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:onUpdate")
	public published  void onUpdate () {
		getPipeline().onUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:beforeCreate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:beforeCreate")
	protected published  boolean beforeCreate (org.radixware.kernel.server.types.Entity src) {
		onUpdate();
		return super.beforeCreate(src);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:beforeDelete-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:beforeDelete")
	protected published  boolean beforeDelete () {
		onUpdate();
		return super.beforeDelete();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:beforeUpdate-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:beforeUpdate")
	protected published  boolean beforeUpdate () {
		onUpdate();
		return super.beforeUpdate();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getOrderPos-User Method*/

	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getOrderPos")
	public static published  org.radixware.ads.ServiceBus.common.SbPos getOrderPos () {
		return new SbPos(1.621);
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:isSuitableForPipeline-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:isSuitableForPipeline")
	public published  boolean isSuitableForPipeline (org.radixware.ads.ServiceBus.server.IPipeline pipeline) {
		return !pipeline.isTransformation();
	}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getDescriptiveOwner-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:getDescriptiveOwner")
	public published  org.radixware.kernel.server.types.Entity getDescriptiveOwner () {
		return (Types::Entity) getPipeline();
	}


	@Override
	public org.radixware.kernel.server.types.FormHandler.NextDialogsRequest execCommand(org.radixware.kernel.common.types.Id cmdId, org.radixware.kernel.common.types.Id propId, org.apache.xmlbeans.XmlObject input, org.radixware.kernel.server.types.PropValHandlersByIdMap newPropValsById, org.apache.xmlbeans.XmlObject output) throws org.radixware.kernel.common.exceptions.AppException,java.lang.InterruptedException{

			return super.execCommand(cmdId,propId,input,newPropValsById,output);
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Server Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.server;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Consumer_mi{
	@SuppressWarnings("deprecation")
	public static final org.radixware.kernel.server.meta.clazzes.RadClassDef rdxMeta = new org.radixware.kernel.server.meta.clazzes.RadClassDef(org.radixware.kernel.server.arte.MetaContextProvider.getRelease(),org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),"Endpoint.MessageQueue.Consumer",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZNTSU4R2RA3XPYACSLT2XQJLQ"),

						org.radixware.kernel.common.enums.EClassType.APPLICATION,

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Presentations-Entity Object Presentations*/
						/*Class presentation attributes*/
						 new org.radixware.kernel.server.meta.presentations.RadClassPresentationDef(/*Owner Class Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
							/*Owner Class Name*/
							"Endpoint.MessageQueue.Consumer",
							/*Title Id*/
							org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZNTSU4R2RA3XPYACSLT2XQJLQ"),
							/*Property presentations*/

							/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Properties-Properties*/
							new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef[]{

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru76K4KBKARZCV7N3E77JJ2W3AEE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDVRTUPQU45AQHKNIYHGFKXOJ6Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBTAAPQZLENCJTKDHQ7NL7VDM3Q"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5ZD6D7WRVNA4HIVIQHQRMUE4GM"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,true,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4MHOY37HXVBF7DBMUJO4DBJUQY"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2LF6GUISVDOZGI3G3NIYOLIN4"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZQPT4JXYFVDEXNU77WFOJ6DXSE"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT)),

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title:PropertyPresentation-Property Presentation*/
									new org.radixware.kernel.server.meta.presentations.RadPropertyPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),org.radixware.kernel.common.enums.EEditPossibility.ALWAYS,false,false,java.util.EnumSet.of(org.radixware.kernel.common.enums.EPropAttrInheritance.EDITING,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_TITLE_FORMAT,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECT_CONDITION,org.radixware.kernel.common.enums.EPropAttrInheritance.PARENT_SELECTOR,org.radixware.kernel.common.enums.EPropAttrInheritance.HINT))
							},
							/*Commands*/
							null,
							/*Sortings*/
							null,
							/*Filters*/
							null,
							/*Editor presentations*/
							new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef[]{

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),
									"Edit",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
									40112,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU")),

									new org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef(
									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create-Editor Presentation*/

									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHMISRHKHINBFHI5E5KVMWIVE6I"),
									"Create",
									org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
									org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),
									40112,
									null,

									/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Children-Explorer Items*/
										null
									,
									null,
									(org.radixware.kernel.server.meta.presentations.RadPropertyPresentationAttributes[]) null,
									org.radixware.kernel.common.enums.EEditorPresentationRightsInheritanceMode.FROM_REPLACED,
									null)
							},
							/*Selector presentations*/
							null,
							/*Default selector presentation Id*/
							null,
							/*Presentation Map*/
							org.radixware.kernel.common.utils.Maps.fromArrays(new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprPWHXGTPNDBCC5JXWTSPNESU3RQ")},new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHMISRHKHINBFHI5E5KVMWIVE6I")}),
							/*Object title format*/
							null,
							/*Class catalogs*/
							null,
							/*Restrictions*/
							null,null,0),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Properties-Properties*/
						new org.radixware.kernel.server.meta.clazzes.RadPropDef[]{

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru76K4KBKARZCV7N3E77JJ2W3AEE"),"extGuid",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDVRTUPQU45AQHKNIYHGFKXOJ6Q"),"outbConnectId",null,org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBTAAPQZLENCJTKDHQ7NL7VDM3Q"),"pipelineEntityGuid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGW5QGSMMFDUPNQ74G2ZMHT4E4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid-User Property*/

								new org.radixware.kernel.server.meta.clazzes.RadUserPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5ZD6D7WRVNA4HIVIQHQRMUE4GM"),"pipelineEntityPid",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRN2EI5GCN5ANRMVI3CTZDNA3R4"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),false,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4MHOY37HXVBF7DBMUJO4DBJUQY"),"rqData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2LF6GUISVDOZGI3G3NIYOLIN4"),"rsData",null,org.radixware.kernel.common.enums.EValType.XML,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZQPT4JXYFVDEXNU77WFOJ6DXSE"),"traceLevel",null,org.radixware.kernel.common.enums.EValType.INT,null,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:out-Dynamic Property*/

								new org.radixware.kernel.server.meta.clazzes.RadDynamicPropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdLVOXMSMB7NBCJOCLORH5XADZGU"),"out",null,org.radixware.kernel.common.enums.EValType.STR,null,org.radixware.kernel.common.defs.value.RadixDefaultValue.Factory.newValAsStr(org.radixware.kernel.common.defs.value.ValAsStr.Factory.loadFrom("Out")),org.radixware.kernel.server.meta.clazzes.RadPropReadAccessorProxy.R_ACCESSOR_INSTANCE),

								/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title-Column-Based Property*/

								new org.radixware.kernel.server.meta.clazzes.RadInnatePropDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),"title",org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIS6RHGBDBCHFC325QQMOSDQXM"),org.radixware.kernel.common.enums.EValType.STR,null,false,null,null,org.radixware.kernel.common.enums.EPropInitializationPolicy.DO_NOT_DEFINE,null,org.radixware.kernel.server.meta.clazzes.RadPropReadWriteAccessorProxy.RW_ACCESSOR_INSTANCE)
						},

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Methods-Methods*/
						new org.radixware.kernel.server.meta.clazzes.RadMethodDef[]{

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTO3N6ZUC25BNLNVHR6WUL3OSCU"),"afterResponseDelivered",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rs",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEYHOODQI5NDMFPG3ZV7X7KZDJ4"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOC7IFQSQQNCXLAY4YULNTEVVL4"),"checkConfig",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLILYOANG5RBVTHLQIJLYCSDJM4"),"getCreatingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthVA6DRQSCRRCMRNAAYI556P43VE"),"getDescription",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIOMPW3QBZ5ASTPONSTPGMH5ZHQ"),"getEditingPresentationId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthW4LHL5WOQJF7NK3VL3LMQETFTI"),"getExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth4Y335FOB6VF3JHZ5WI7UI5TCHM"),"getHolderEntityPid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOK5OVIZTJ5CUTKASBMH6PUUWGQ"),"getIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthYFZPW475EFFCLLXOYQHNVMU3UM"),"getPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPI4ONCPYCFAIJNIXF3ZKSVPAYM"),"getPipelineNodeParam",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("name",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF5QINESPB5EEXAH2IWLGLR3K3M"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRGPLXVTN4BB5LNLYHDLSL3K444"),"getTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthM7KLX4PIANEH3NYQZZSZ55GMIE"),"refreshCache",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZVCMFP66RZEELGIQECRMCICA3E"),"setExtGuid",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("extGuid",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprHQIWURGPPVFUBI6U2WX4G5DXVM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthG2W65RASFFH7RIEA37LOBPKUFI"),"setPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprR56EMHD33ZALFFKDZJ5RH33HVI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNLROOCGTF5CUVM66WIOJDRSHCE"),"trace",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprV5ENLRN6CRFPZILQH53YRFKPGI")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprU7R2JLV5INCWNEFQIHNWVT2OTQ")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprGPNAF3ALU5H2JMPIOF6PRHWISM"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWQC7HI6DMJB33MVZT5VNNK6LR4"),"traceDebug",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprDK3Y4OHZAZA2DNRT4OLMUHNJ3M")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("sensitive",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprCG73HTI4KZAEHDD6NIFMBYTE5Q"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthQ7MQNDFPEVCSZM46CIFMM4ISAI"),"traceError",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr5KJWPLHG6BHCZHS4UWQYZYKNSQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNY56F4MVVVAABKIBM7GQ52OD5U"),"traceEvent",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJP3JRIDCFVH4JB2S6MFVX2XL7M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthOYF3L7CFTFB27GLSUU7QWQ44PM"),"traceWarning",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("mess",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVIYZO7O2JBBIBAFUETS5OR764M"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNYO5JRURNRFWLFFLCVODWPOTLI"),"getLastInRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6XF3CCTG7NGA7O7SEKOQTWLIHE"),"getLastInRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLC5CACZEKVCFHEZPWGJ7AN732I"),"getLastOutRqData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth6D7TO5XHNRCCZMYQJQZMIWCSUQ"),"getLastOutRsData",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthSJVD663SIFFXVLMYDV2Y2VU544"),"export",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthBXD5LT3S2VA2ZGTSBZIOEVFN2I"),"afterImport",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pids",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJQXYAU5TD5GMRLSAEQDAXO4TBI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7JXSRN7HJNANHAN7CZOA2VSTJU"),"checkTraceLevel",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("severity",org.radixware.kernel.common.enums.EValType.INT,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVJ3FCEFH55FKZKGDFMUR7V3EEE"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthRJJVN7SZFRGFPKSWMK5OODDPHM"),"connect",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("outConnectorRole",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprEEU34DEJT5HBJALS3MIBQTDYZE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("inConnectorId",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprXOC4RM64QRCSLGLACG7BQKM2MU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZEZONIMUSNHEJHOFQ7OPMP3CLU"),"getConnectorIconId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW7IZFQMQUBBNROBLN3GX3KMNU4"))
								},org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFST4X6BGFNA5PH6LQNOG73MSTQ"),"getConnectorId",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr6HF4R6DZUZDTVHWFWDIWU5DO4M"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthWE5I24A36RE3BGGGIIG4FYAV7M"),"getConnectorRole",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("id",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr35I6DEQSRJBGNACDHJ23TU5HLE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthEIEXINMQGJB35G65UDWFOOSDZU"),"getConnectorRqDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKUZTAQ44JNEATJZ5UP5JOCSH6M"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthHZIY6G7TCVCX3DR6QWA77MJJHI"),"getConnectorRsDataType",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFW3AC67ZHVCOREGRXUNEM4CO5U"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthB25WTYWZUJAJXMCSH7UVSSJMP4"),"getConnectorSide",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprW4E3PGSIIBARNBT7HE3VJEFHNE"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthLB23XTDDKVGSRN5R4BPLLXUMFY"),"getConnectorTitle",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("role",org.radixware.kernel.common.enums.EValType.STR,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprOR2KWUBSANC2NCNHHFRCJTYLTQ"))
								},org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthIQ7FIDN6DFEYXJSX6G5WONK32M"),"getInConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthZHFRUV5SQ5CYNG4JLLR26M4UWU"),"getOutConnectorRoles",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.STR),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKMN6A6YHPVBNFON237GIJY4ZPU"),"import",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xNode",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprKMXCDMXERFDU3DY3J4PAGB2Z6U")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("overwriteLocalSettings",org.radixware.kernel.common.enums.EValType.JAVA_TYPE,org.radixware.kernel.common.types.Id.Factory.loadFrom("mpr2UPXB75HQBFMVAX3D6CSYO2S24")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("helper",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprJ4QHTLOUYNGJBKD7ELPV4H54WI"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mth7MEKN6RYYBASBPAYYLM556VGPM"),"processRequest",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("rq",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprF3U6OL46NJCDLJYTKA75TDEJPY"))
								},org.radixware.kernel.common.enums.EValType.XML),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthXZIMLTKPH5EMXB5OLJYBPGCJOI"),"process",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("xMessage",org.radixware.kernel.common.enums.EValType.XML,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprZSUG5BHUUNGXHGOBFJZYKJQIGQ"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA"),"afterInit",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprVAHMT5WXTFCWRGRKRWI7FNZUVE")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("phase",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprWK5PMPN455DRVNW7DK3PBN6DNY"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAZ2HAURWXVB53HEDENC67YLQAU"),"afterUpdatePropObject",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propId",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprD2YGWPJJBNEL7MAIYGCHYAIYDA")),

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("propVal",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprFXKVCUKTC5CMZNIA2VWX2PO2BU"))
								},null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthDWPUX7PPI5GUTBFQ2R73BYQZYY"),"onUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,null),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthFMXUFM5J25E4TNM4PR73F6V3E4"),"beforeCreate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("src",org.radixware.kernel.common.enums.EValType.JAVA_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprPZ6IAU7H2VCK5AXDYTR6MMLOEI"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthKNZ5Z7AS6RFRPIX6ZD5JDKEC34"),"beforeDelete",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthPT5PMP2WVZGUDB7OTLOE2SBWKE"),"beforeUpdate",false,false,org.radixware.kernel.common.enums.EAccess.PROTECTED,null,org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthAXJJZNLFOJGVBNHZN5AEP5GJGM"),"getOrderPos",true,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.USER_CLASS),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthNSQ7CEKCGFFWRLVESA72LZ7DEM"),"isSuitableForPipeline",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter[]{

										new org.radixware.kernel.server.meta.clazzes.RadMethodDef.Parameter("pipeline",org.radixware.kernel.common.enums.EValType.USER_CLASS,org.radixware.kernel.common.types.Id.Factory.loadFrom("mprSR4BBVPLDZH5NI4FPGEGZD633Q"))
								},org.radixware.kernel.common.enums.EValType.JAVA_TYPE),

								new org.radixware.kernel.server.meta.clazzes.RadMethodDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("mthTX7AFQWDNRDV5JJBAXUEYH43II"),"getDescriptiveOwner",false,false,org.radixware.kernel.common.enums.EAccess.PUBLIC,null,org.radixware.kernel.common.enums.EValType.JAVA_CLASS)
						},
						null,
						null,null,null,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer")
public interface Endpoint.MessageQueue.Consumer   extends org.radixware.ads.MessageQueue.explorer.MessageQueueProcessor  {































































































































	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:rqData-Presentation Property*/


	public class RqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public RqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:rqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:rqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public RqData getRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:rsData-Presentation Property*/


	public class RsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public RsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:rsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:rsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public RsData getRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:traceLevel-Presentation Property*/


	public class TraceLevel extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TraceLevel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


	public class PipelineEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:extGuid-Presentation Property*/


	public class ExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


	public class PipelineEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:outbConnectId-Presentation Property*/


	public class OutbConnectId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:outbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:outbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Consumer_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
			"Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZNTSU4R2RA3XPYACSLT2XQJLQ"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru76K4KBKARZCV7N3E77JJ2W3AEE"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDVRTUPQU45AQHKNIYHGFKXOJ6Q"),
						"outbConnectId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBTAAPQZLENCJTKDHQ7NL7VDM3Q"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGW5QGSMMFDUPNQ74G2ZMHT4E4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5ZD6D7WRVNA4HIVIQHQRMUE4GM"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRN2EI5GCN5ANRMVI3CTZDNA3R4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4MHOY37HXVBF7DBMUJO4DBJUQY"),
						"rqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2LF6GUISVDOZGI3G3NIYOLIN4"),
						"rsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZQPT4JXYFVDEXNU77WFOJ6DXSE"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIS6RHGBDBCHFC325QQMOSDQXM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						62,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHMISRHKHINBFHI5E5KVMWIVE6I")},
			true,false,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;

@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer")
public interface Endpoint.MessageQueue.Consumer   extends org.radixware.ads.MessageQueue.web.MessageQueueProcessor  {































































































































	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:rqData-Presentation Property*/


	public class RqData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public RqData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:rqData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:rqData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public RqData getRqData();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:rsData-Presentation Property*/


	public class RsData extends org.radixware.kernel.common.client.models.items.properties.PropertyXml{
		public RsData(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
			super(model,def);
		}
		@Override
		public Class<org.apache.xmlbeans.XmlObject> getValClass(){
			return org.apache.xmlbeans.XmlObject.class;
		}
		@Override
		public Object getValueObject(){return getValue();}
		@Override
		public void setValueObject(final Object x){
			org.apache.xmlbeans.XmlObject dummy = x == null ? null : (org.apache.xmlbeans.XmlObject)org.radixware.kernel.common.utils.XmlObjectProcessor.castToXmlClass(getClass().getClassLoader(),(org.apache.xmlbeans.XmlObject)x,org.apache.xmlbeans.XmlObject.class);
			setValue(dummy);
		}












		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:rsData")
		public  org.apache.xmlbeans.XmlObject getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:rsData")
		public   void setValue(org.apache.xmlbeans.XmlObject val) {
			Value = val;
		}
	}
	public RsData getRsData();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:traceLevel-Presentation Property*/


	public class TraceLevel extends org.radixware.kernel.common.client.models.items.properties.PropertyInt{
		public TraceLevel(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:traceLevel")
		public  Int getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:traceLevel")
		public   void setValue(Int val) {
			Value = val;
		}
	}
	public TraceLevel getTraceLevel();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:pipelineEntityPid-Presentation Property*/


	public class PipelineEntityPid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityPid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:pipelineEntityPid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:pipelineEntityPid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityPid getPipelineEntityPid();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:extGuid-Presentation Property*/


	public class ExtGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public ExtGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:extGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:extGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public ExtGuid getExtGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:pipelineEntityGuid-Presentation Property*/


	public class PipelineEntityGuid extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public PipelineEntityGuid(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:pipelineEntityGuid")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:pipelineEntityGuid")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public PipelineEntityGuid getPipelineEntityGuid();
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:outbConnectId-Presentation Property*/


	public class OutbConnectId extends org.radixware.kernel.common.client.models.items.properties.PropertyStr{
		public OutbConnectId(org.radixware.kernel.common.client.models.Model  model,org.radixware.kernel.common.client.meta.RadPropertyDef def){
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
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:outbConnectId")
		public  Str getValue() {
			return Value;
		}

		@SuppressWarnings({"unused","unchecked"})
		@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:outbConnectId")
		public   void setValue(Str val) {
			Value = val;
		}
	}
	public OutbConnectId getOutbConnectId();


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer-Application Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Consumer_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta = 
	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Presentations-Entity Object Presentations*/
	new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
			"Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer",
			org.radixware.kernel.common.types.Id.Factory.loadFrom("aecU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
			null,
			null,
			org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZNTSU4R2RA3XPYACSLT2XQJLQ"),null,null,0,

			/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Properties-Properties*/
			new org.radixware.kernel.common.client.meta.RadPropertyDef[]{

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru76K4KBKARZCV7N3E77JJ2W3AEE"),
						"extGuid",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:extGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruDVRTUPQU45AQHKNIYHGFKXOJ6Q"),
						"outbConnectId",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:outbConnectId:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pruBTAAPQZLENCJTKDHQ7NL7VDM3Q"),
						"pipelineEntityGuid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGW5QGSMMFDUPNQ74G2ZMHT4E4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityGuid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("pru5ZD6D7WRVNA4HIVIQHQRMUE4GM"),
						"pipelineEntityPid",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRN2EI5GCN5ANRMVI3CTZDNA3R4"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.USER,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:pipelineEntityPid:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,-1,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rqData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prd4MHOY37HXVBF7DBMUJO4DBJUQY"),
						"rqData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:rsData:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdF2LF6GUISVDOZGI3G3NIYOLIN4"),
						"rsData",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
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

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("prdZQPT4JXYFVDEXNU77WFOJ6DXSE"),
						"traceLevel",
						null,
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:traceLevel:PropertyPresentation:Edit Options:-Edit Mask Int*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskInt(null,null,(byte)-1,null,1L,org.radixware.kernel.common.enums.ETriadDelimeterType.NONE,null,(byte)10),
						null,
						null,
						null,
						true,-1,-1,1,
						false,false,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false),

					/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title:PropertyPresentation-Property Presentation*/
					new org.radixware.kernel.common.client.meta.RadPropertyDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("col2N5HTUV4BVAARJ5AUDECTOG22A"),
						"title",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIS6RHGBDBCHFC325QQMOSDQXM"),
						null,
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
						org.radixware.kernel.common.enums.EValType.STR,
						org.radixware.kernel.common.enums.EPropNature.INNATE,
						62,
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

						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:title:PropertyPresentation:Edit Options:-Edit Mask Str*/
						new org.radixware.kernel.common.client.meta.mask.EditMaskStr("",true,false,200,true),
						null,
						null,
						null,
						true,-1,-1,1,
						false,true,org.radixware.kernel.common.enums.EPropertyValueStorePossibility.NONE,false)
			},
			null,
			null,
			null,
			null,
			new org.radixware.kernel.common.types.Id[]{org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHMISRHKHINBFHI5E5KVMWIVE6I")},
			true,false,false);
}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	null,
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	40112,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Edit_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),
	"Edit",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprIOZWMJBLGVCM7DQZSYFGGUMQWU"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	null,
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	40112,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Endpoint.MessageQueue.Consumer.Endpoint.MessageQueue.Consumer_DefaultModel.eprIOZWMJBLGVCM7DQZSYFGGUMQWU_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Methods-Methods*/


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAKAMBMWKF5EAJCJMQOAKUMU23U"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemIOZWMJBLGVCM7DQZSYFGGUMQWU"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model")
public class Edit:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Endpoint.MessageQueue.Consumer.Endpoint.MessageQueue.Consumer_DefaultModel.eprIOZWMJBLGVCM7DQZSYFGGUMQWU_ModelAdapter {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Edit:Model_mi.rdxMeta; }



	public Edit:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Methods-Methods*/


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Edit:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAKAMBMWKF5EAJCJMQOAKUMU23U"),
						"Edit:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemIOZWMJBLGVCM7DQZSYFGGUMQWU"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Edit:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHMISRHKHINBFHI5E5KVMWIVE6I"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	null,
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	40112,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create-Editor Presentation*/

package org.radixware.ads.ServiceBus.Nodes.web;
public final class Create_mi{
	public static final org.radixware.kernel.common.client.meta.RadEditorPresentationDef rdxMeta = new org.radixware.kernel.common.client.meta.RadEditorPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("eprHMISRHKHINBFHI5E5KVMWIVE6I"),
	"Create",org.radixware.kernel.common.enums.ERuntimeEnvironmentType.COMMON_CLIENT,
	org.radixware.kernel.common.types.Id.Factory.loadFrom("eprAKAMBMWKF5EAJCJMQOAKUMU23U"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("aclEBOHN3MHFBHGLP76IRT6RVFSDI"),
	org.radixware.kernel.common.types.Id.Factory.loadFrom("tblU5S5JH3DFFGHXMRP4JVYGLQQTQ"),
	null,
	null,
	null,
	null,

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Children-Explorer Items*/
		null
	,(org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemsSettings[])null,
	null,
	0,
	null,
	(org.radixware.kernel.common.client.meta.RadPropertyPresentationAttributes[]) null,
	40112,0,0);
}
/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model - Desktop Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.explorer.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    pipelineEntityGuid.Value = Str.valueOf(context.getPipelinePid().getTableId());
		    pipelineEntityPid.Value = Str.valueOf(context.getPipelinePid());
		    
		}
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model - Desktop Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.explorer;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHMISRHKHINBFHI5E5KVMWIVE6I"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAKAMBMWKF5EAJCJMQOAKUMU23U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model - Web Executable*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;



@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model")
public class Create:Model  extends org.radixware.ads.ServiceBus.Nodes.web.Edit:Model  {
	public org.radixware.kernel.common.client.meta.RadClassPresentationDef getRadMeta(){ return Create:Model_mi.rdxMeta; }



	public Create:Model(org.radixware.kernel.common.client.IClientEnvironment userSession,org.radixware.kernel.common.client.meta.RadEditorPresentationDef def){super(userSession,def);}

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Nested classes-Nested Classes*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Properties-Properties*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Methods-Methods*/

	/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:beforeOpenView-User Method*/

	@Override
	@org.radixware.kernel.common.lang.MetaInfo(name="Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:beforeOpenView")
	protected published  void beforeOpenView () {
		if (getContext() instanceof ServiceBus::ISbContext) {
		    final ServiceBus::ISbContext context = (ServiceBus::ISbContext)getContext();
		    pipelineEntityGuid.Value = Str.valueOf(context.getPipelinePid().getTableId());
		    pipelineEntityPid.Value = Str.valueOf(context.getPipelinePid());
		    
		}
		super.beforeOpenView();
	}


}

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model - Web Meta*/

/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model-Entity Model Class*/

package org.radixware.ads.ServiceBus.Nodes.web;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Create:Model_mi{
	public static final org.radixware.kernel.common.client.meta.RadClassPresentationDef rdxMeta =  new org.radixware.kernel.common.client.meta.RadClassPresentationDef(org.radixware.kernel.common.types.Id.Factory.loadFrom("aemHMISRHKHINBFHI5E5KVMWIVE6I"),
						"Create:Model",
						org.radixware.kernel.common.types.Id.Factory.loadFrom("aemAKAMBMWKF5EAJCJMQOAKUMU23U"),
						null,
						null,
						null,
						null,
						null,
						0,
						/*Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer:Create:Model:Properties-Properties*/
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

/* Radix::ServiceBus.Nodes::Endpoint.MessageQueue.Consumer - Localizing Bundle */
package org.radixware.ads.ServiceBus.Nodes.common;
@org.radixware.kernel.common.lang.RadMetaClass(shareabilityArea=org.radixware.kernel.common.enums.EShareabilityArea.RELEASE)
public final class Endpoint.MessageQueue.Consumer - Localizing Bundle_mi{
	@SuppressWarnings("unused")
	private static final java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString> $$$items$$$ = new java.util.HashMap<org.radixware.kernel.common.types.Id,org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString>();
	static{
		loadStrings1();
	}

	@SuppressWarnings("unused")
	private static void loadStrings1(){
		java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>$$$strings$$$ = new java.util.HashMap<org.radixware.kernel.common.enums.EIsoLanguage,String>();
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls2DEH6VLEKFCJRP67LKDQ7GD5OM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline not checked");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Конвейер не проверен");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mls77NKTC4CX5BGNAE6OY7SDJOAYE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Message Queue Consumer");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Получатель очереди сообщений");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsBZNTSU4R2RA3XPYACSLT2XQJLQ"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity ID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ид. сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsDGW5QGSMMFDUPNQ74G2ZMHT4E4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Pipeline processing exception: ");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Исключение при обработке узлов конвейера: ");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsIPVBEGP77ZG2HGHPNE5XEHQHTE"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Name");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Название");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsJIS6RHGBDBCHFC325QQMOSDQXM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,null,null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Entity PID");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Ключ сущности");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsRN2EI5GCN5ANRMVI3CTZDNA3R4"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
		$$$strings$$$.clear();
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,"Inbound messages");
		$$$strings$$$.put(org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN,"Входящие сообщения");
		$$$items$$$.put(org.radixware.kernel.common.types.Id.Factory.loadFrom("mlsZT2NY3YFAJC6VD6F36KTZNQNWM"),new org.radixware.kernel.common.meta.RadMlStringBundleDef.MultilingualString($$$strings$$$,org.radixware.kernel.common.enums.ELocalizedStringKind.SIMPLE,null,null,java.util.EnumSet.of(org.radixware.kernel.common.enums.EIsoLanguage.ENGLISH,org.radixware.kernel.common.enums.EIsoLanguage.RUSSIAN),null));
	}

	public static final org.radixware.kernel.common.meta.RadMlStringBundleDef rdxMeta = new org.radixware.kernel.common.meta.RadMlStringBundleDef(Endpoint.MessageQueue.Consumer - Localizing Bundle_mi.class,org.radixware.kernel.common.types.Id.Factory.loadFrom("mlbaclEBOHN3MHFBHGLP76IRT6RVFSDI"),"Endpoint.MessageQueue.Consumer - Localizing Bundle",$$$items$$$);
}
